package io.reactivex.internal.operators.observable;

import io.reactivex.Notification;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableMaterialize extends AbstractObservableWithUpstream {

    final class MaterializeObserver implements Observer, Disposable {
        final Observer actual;
        Disposable s;

        MaterializeObserver(Observer observer) {
            this.actual = observer;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            this.actual.onNext(Notification.createOnComplete());
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onNext(Notification.createOnError(th));
            this.actual.onComplete();
        }

        public void onNext(Object obj) {
            this.actual.onNext(Notification.createOnNext(obj));
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableMaterialize(ObservableSource observableSource) {
        super(observableSource);
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new MaterializeObserver(observer));
    }
}
