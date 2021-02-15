package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableRetryBiPredicate extends AbstractObservableWithUpstream {
    final BiPredicate predicate;

    final class RetryBiObserver extends AtomicInteger implements Observer {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer actual;
        final BiPredicate predicate;
        int retries;
        final SequentialDisposable sa;
        final ObservableSource source;

        RetryBiObserver(Observer observer, BiPredicate biPredicate, SequentialDisposable sequentialDisposable, ObservableSource observableSource) {
            this.actual = observer;
            this.sa = sequentialDisposable;
            this.source = observableSource;
            this.predicate = biPredicate;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            char c = 1;
            try {
                BiPredicate biPredicate = this.predicate;
                int i = this.retries + 1;
                this.retries = i;
                c = biPredicate.test(Integer.valueOf(i), th);
                if (c == 0) {
                    this.actual.onError(th);
                } else {
                    subscribeNext();
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                Observer observer = this.actual;
                Throwable[] thArr = new Throwable[2];
                thArr[0] = th;
                thArr[c] = th2;
                observer.onError(new CompositeException(thArr));
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

    public ObservableRetryBiPredicate(Observable observable, BiPredicate biPredicate) {
        super(observable);
        this.predicate = biPredicate;
    }

    public void subscribeActual(Observer observer) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        new RetryBiObserver(observer, this.predicate, sequentialDisposable, this.source).subscribeNext();
    }
}
