package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.ArrayDeque;

public final class ObservableTakeLast extends AbstractObservableWithUpstream {
    final int count;

    final class TakeLastObserver extends ArrayDeque implements Observer, Disposable {
        private static final long serialVersionUID = 7240042530241604978L;
        final Observer actual;
        volatile boolean cancelled;
        final int count;
        Disposable s;

        TakeLastObserver(Observer observer, int i) {
            this.actual = observer;
            this.count = i;
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            Observer observer = this.actual;
            while (!this.cancelled) {
                Object poll = poll();
                if (poll == null) {
                    if (!this.cancelled) {
                        observer.onComplete();
                    }
                    return;
                }
                observer.onNext(poll);
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (this.count == size()) {
                poll();
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

    public ObservableTakeLast(ObservableSource observableSource, int i) {
        super(observableSource);
        this.count = i;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new TakeLastObserver(observer, this.count));
    }
}
