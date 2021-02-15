package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableCount extends AbstractObservableWithUpstream {

    final class CountObserver implements Observer, Disposable {
        final Observer actual;
        long count;
        Disposable s;

        CountObserver(Observer observer) {
            this.actual = observer;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            this.actual.onNext(Long.valueOf(this.count));
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.count++;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableCount(ObservableSource observableSource) {
        super(observableSource);
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new CountObserver(observer));
    }
}
