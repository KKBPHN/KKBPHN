package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableRetryPredicate extends AbstractObservableWithUpstream {
    final long count;
    final Predicate predicate;

    final class RepeatObserver extends AtomicInteger implements Observer {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer actual;
        final Predicate predicate;
        long remaining;
        final SequentialDisposable sa;
        final ObservableSource source;

        RepeatObserver(Observer observer, long j, Predicate predicate2, SequentialDisposable sequentialDisposable, ObservableSource observableSource) {
            this.actual = observer;
            this.sa = sequentialDisposable;
            this.source = observableSource;
            this.predicate = predicate2;
            this.remaining = j;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            long j = this.remaining;
            if (j != Long.MAX_VALUE) {
                this.remaining = j - 1;
            }
            if (j == 0) {
                this.actual.onError(th);
            } else {
                try {
                    if (!this.predicate.test(th)) {
                        this.actual.onError(th);
                        return;
                    }
                    subscribeNext();
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.actual.onError(new CompositeException(th, th2));
                }
            }
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            this.sa.update(disposable);
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                while (!this.sa.isDisposed()) {
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                    if (i == 0) {
                    }
                }
            }
        }
    }

    public ObservableRetryPredicate(Observable observable, long j, Predicate predicate2) {
        super(observable);
        this.predicate = predicate2;
        this.count = j;
    }

    public void subscribeActual(Observer observer) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        RepeatObserver repeatObserver = new RepeatObserver(observer, this.count, this.predicate, sequentialDisposable, this.source);
        repeatObserver.subscribeNext();
    }
}
