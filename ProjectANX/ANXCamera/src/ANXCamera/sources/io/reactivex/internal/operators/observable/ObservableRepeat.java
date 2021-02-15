package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableRepeat extends AbstractObservableWithUpstream {
    final long count;

    final class RepeatObserver extends AtomicInteger implements Observer {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer actual;
        long remaining;
        final SequentialDisposable sd;
        final ObservableSource source;

        RepeatObserver(Observer observer, long j, SequentialDisposable sequentialDisposable, ObservableSource observableSource) {
            this.actual = observer;
            this.sd = sequentialDisposable;
            this.source = observableSource;
            this.remaining = j;
        }

        public void onComplete() {
            long j = this.remaining;
            if (j != Long.MAX_VALUE) {
                this.remaining = j - 1;
            }
            if (j != 0) {
                subscribeNext();
            } else {
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            this.sd.replace(disposable);
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                while (!this.sd.isDisposed()) {
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                    if (i == 0) {
                    }
                }
            }
        }
    }

    public ObservableRepeat(Observable observable, long j) {
        super(observable);
        this.count = j;
    }

    public void subscribeActual(Observer observer) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        long j = this.count;
        long j2 = Long.MAX_VALUE;
        if (j != Long.MAX_VALUE) {
            j2 = j - 1;
        }
        RepeatObserver repeatObserver = new RepeatObserver(observer, j2, sequentialDisposable, this.source);
        repeatObserver.subscribeNext();
    }
}
