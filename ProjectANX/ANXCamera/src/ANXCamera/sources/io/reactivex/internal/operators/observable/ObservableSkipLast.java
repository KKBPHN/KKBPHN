package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.ArrayDeque;

public final class ObservableSkipLast extends AbstractObservableWithUpstream {
    final int skip;

    final class SkipLastObserver extends ArrayDeque implements Observer, Disposable {
        private static final long serialVersionUID = -3807491841935125653L;
        final Observer actual;
        Disposable s;
        final int skip;

        SkipLastObserver(Observer observer, int i) {
            super(i);
            this.actual = observer;
            this.skip = i;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (this.skip == size()) {
                this.actual.onNext(poll());
            }
            offer(obj);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableSkipLast(ObservableSource observableSource, int i) {
        super(observableSource);
        this.skip = i;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new SkipLastObserver(observer, this.skip));
    }
}
