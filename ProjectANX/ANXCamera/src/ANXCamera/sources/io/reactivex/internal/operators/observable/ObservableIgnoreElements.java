package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public final class ObservableIgnoreElements extends AbstractObservableWithUpstream {

    final class IgnoreObservable implements Observer, Disposable {
        final Observer actual;
        Disposable d;

        IgnoreObservable(Observer observer) {
            this.actual = observer;
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
        }

        public void onSubscribe(Disposable disposable) {
            this.d = disposable;
            this.actual.onSubscribe(this);
        }
    }

    public ObservableIgnoreElements(ObservableSource observableSource) {
        super(observableSource);
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new IgnoreObservable(observer));
    }
}
