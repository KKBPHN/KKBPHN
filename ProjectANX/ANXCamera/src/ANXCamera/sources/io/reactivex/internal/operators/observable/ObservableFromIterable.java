package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicQueueDisposable;
import java.util.Iterator;

public final class ObservableFromIterable extends Observable {
    final Iterable source;

    final class FromIterableDisposable extends BasicQueueDisposable {
        final Observer actual;
        boolean checkNext;
        volatile boolean disposed;
        boolean done;
        boolean fusionMode;
        final Iterator it;

        FromIterableDisposable(Observer observer, Iterator it2) {
            this.actual = observer;
            this.it = it2;
        }

        public void clear() {
            this.done = true;
        }

        public void dispose() {
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public boolean isEmpty() {
            return this.done;
        }

        @Nullable
        public Object poll() {
            if (this.done) {
                return null;
            }
            if (!this.checkNext) {
                this.checkNext = true;
            } else if (!this.it.hasNext()) {
                this.done = true;
                return null;
            }
            Object next = this.it.next();
            ObjectHelper.requireNonNull(next, "The iterator returned a null value");
            return next;
        }

        public int requestFusion(int i) {
            if ((i & 1) == 0) {
                return 0;
            }
            this.fusionMode = true;
            return 1;
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            while (!isDisposed()) {
                try {
                    Object next = this.it.next();
                    ObjectHelper.requireNonNull(next, "The iterator returned a null value");
                    this.actual.onNext(next);
                    if (!isDisposed()) {
                        if (!this.it.hasNext()) {
                            if (!isDisposed()) {
                                this.actual.onComplete();
                            }
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.actual.onError(th);
                    return;
                }
            }
        }
    }

    public ObservableFromIterable(Iterable iterable) {
        this.source = iterable;
    }

    public void subscribeActual(Observer observer) {
        try {
            Iterator it = this.source.iterator();
            if (!it.hasNext()) {
                EmptyDisposable.complete(observer);
                return;
            }
            FromIterableDisposable fromIterableDisposable = new FromIterableDisposable(observer, it);
            observer.onSubscribe(fromIterableDisposable);
            if (!fromIterableDisposable.fusionMode) {
                fromIterableDisposable.run();
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
