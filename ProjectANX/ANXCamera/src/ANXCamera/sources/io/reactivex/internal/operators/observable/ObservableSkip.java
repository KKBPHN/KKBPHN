package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public final class ObservableSkip extends AbstractObservableWithUpstream {
    final long n;

    final class SkipObserver implements Observer, Disposable {
        final Observer actual;
        Disposable d;
        long remaining;

        SkipObserver(Observer observer, long j) {
            this.actual = observer;
            this.remaining = j;
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
            long j = this.remaining;
            if (j != 0) {
                this.remaining = j - 1;
            } else {
                this.actual.onNext(obj);
            }
        }

        public void onSubscribe(Disposable disposable) {
            this.d = disposable;
            this.actual.onSubscribe(this);
        }
    }

    public ObservableSkip(ObservableSource observableSource, long j) {
        super(observableSource);
        this.n = j;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new SkipObserver(observer, this.n));
    }
}
