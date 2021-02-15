package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SerializedObserver;
import java.util.Collection;
import java.util.concurrent.Callable;

public final class ObservableBufferExactBoundary extends AbstractObservableWithUpstream {
    final ObservableSource boundary;
    final Callable bufferSupplier;

    final class BufferBoundaryObserver extends DisposableObserver {
        final BufferExactBoundaryObserver parent;

        BufferBoundaryObserver(BufferExactBoundaryObserver bufferExactBoundaryObserver) {
            this.parent = bufferExactBoundaryObserver;
        }

        public void onComplete() {
            this.parent.onComplete();
        }

        public void onError(Throwable th) {
            this.parent.onError(th);
        }

        public void onNext(Object obj) {
            this.parent.next();
        }
    }

    final class BufferExactBoundaryObserver extends QueueDrainObserver implements Observer, Disposable {
        final ObservableSource boundary;
        Collection buffer;
        final Callable bufferSupplier;
        Disposable other;
        Disposable s;

        BufferExactBoundaryObserver(Observer observer, Callable callable, ObservableSource observableSource) {
            super(observer, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.boundary = observableSource;
        }

        public void accept(Observer observer, Collection collection) {
            this.actual.onNext(collection);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.other.dispose();
                this.s.dispose();
                if (enter()) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                Collection collection = (Collection) call;
                synchronized (this) {
                    Collection collection2 = this.buffer;
                    if (collection2 != null) {
                        this.buffer = collection;
                        fastPathEmit(collection2, false, this);
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                dispose();
                this.actual.onError(th);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
            io.reactivex.internal.util.QueueDrainHelper.drainLoop(r3.queue, r3.actual, false, r3, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000b, code lost:
            r3.queue.offer(r0);
            r3.done = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
            if (enter() == false) goto L_0x0021;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onComplete() {
            synchronized (this) {
                Collection collection = this.buffer;
                if (collection != null) {
                    this.buffer = null;
                }
            }
        }

        public void onError(Throwable th) {
            dispose();
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            synchronized (this) {
                Collection collection = this.buffer;
                if (collection != null) {
                    collection.add(obj);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                    this.buffer = (Collection) call;
                    BufferBoundaryObserver bufferBoundaryObserver = new BufferBoundaryObserver(this);
                    this.other = bufferBoundaryObserver;
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        this.boundary.subscribe(bufferBoundaryObserver);
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.cancelled = true;
                    disposable.dispose();
                    EmptyDisposable.error(th, this.actual);
                }
            }
        }
    }

    public ObservableBufferExactBoundary(ObservableSource observableSource, ObservableSource observableSource2, Callable callable) {
        super(observableSource);
        this.boundary = observableSource2;
        this.bufferSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new BufferExactBoundaryObserver(new SerializedObserver(observer), this.bufferSupplier, this.boundary));
    }
}
