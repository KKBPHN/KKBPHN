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
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableBufferBoundarySupplier extends AbstractObservableWithUpstream {
    final Callable boundarySupplier;
    final Callable bufferSupplier;

    final class BufferBoundaryObserver extends DisposableObserver {
        boolean once;
        final BufferBoundarySupplierObserver parent;

        BufferBoundaryObserver(BufferBoundarySupplierObserver bufferBoundarySupplierObserver) {
            this.parent = bufferBoundarySupplierObserver;
        }

        public void onComplete() {
            if (!this.once) {
                this.once = true;
                this.parent.next();
            }
        }

        public void onError(Throwable th) {
            if (this.once) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.once = true;
            this.parent.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.once) {
                this.once = true;
                dispose();
                this.parent.next();
            }
        }
    }

    final class BufferBoundarySupplierObserver extends QueueDrainObserver implements Observer, Disposable {
        final Callable boundarySupplier;
        Collection buffer;
        final Callable bufferSupplier;
        final AtomicReference other = new AtomicReference();
        Disposable s;

        BufferBoundarySupplierObserver(Observer observer, Callable callable, Callable callable2) {
            super(observer, new MpscLinkedQueue());
            this.bufferSupplier = callable;
            this.boundarySupplier = callable2;
        }

        public void accept(Observer observer, Collection collection) {
            this.actual.onNext(collection);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.dispose();
                disposeOther();
                if (enter()) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeOther() {
            DisposableHelper.dispose(this.other);
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
                try {
                    Object call2 = this.boundarySupplier.call();
                    ObjectHelper.requireNonNull(call2, "The boundary ObservableSource supplied is null");
                    ObservableSource observableSource = (ObservableSource) call2;
                    BufferBoundaryObserver bufferBoundaryObserver = new BufferBoundaryObserver(this);
                    if (this.other.compareAndSet((Disposable) this.other.get(), bufferBoundaryObserver)) {
                        synchronized (this) {
                            Collection collection2 = this.buffer;
                            if (collection2 != null) {
                                this.buffer = collection;
                                observableSource.subscribe(bufferBoundaryObserver);
                                fastPathEmit(collection2, false, this);
                            }
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.cancelled = true;
                    this.s.dispose();
                    this.actual.onError(th);
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                dispose();
                this.actual.onError(th2);
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
                Observer observer = this.actual;
                try {
                    Object call = this.bufferSupplier.call();
                    ObjectHelper.requireNonNull(call, "The buffer supplied is null");
                    this.buffer = (Collection) call;
                    Object call2 = this.boundarySupplier.call();
                    ObjectHelper.requireNonNull(call2, "The boundary ObservableSource supplied is null");
                    ObservableSource observableSource = (ObservableSource) call2;
                    BufferBoundaryObserver bufferBoundaryObserver = new BufferBoundaryObserver(this);
                    this.other.set(bufferBoundaryObserver);
                    observer.onSubscribe(this);
                    if (!this.cancelled) {
                        observableSource.subscribe(bufferBoundaryObserver);
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.cancelled = true;
                    disposable.dispose();
                    EmptyDisposable.error(th, observer);
                }
            }
        }
    }

    public ObservableBufferBoundarySupplier(ObservableSource observableSource, Callable callable, Callable callable2) {
        super(observableSource);
        this.boundarySupplier = callable;
        this.bufferSupplier = callable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new BufferBoundarySupplierObserver(new SerializedObserver(observer), this.bufferSupplier, this.boundarySupplier));
    }
}
