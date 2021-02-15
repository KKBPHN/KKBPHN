package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableBufferBoundary extends AbstractObservableWithUpstream {
    final Function bufferClose;
    final ObservableSource bufferOpen;
    final Callable bufferSupplier;

    final class BufferBoundaryObserver extends AtomicInteger implements Observer, Disposable {
        private static final long serialVersionUID = -8466418554264089604L;
        final Observer actual;
        final Function bufferClose;
        final ObservableSource bufferOpen;
        final Callable bufferSupplier;
        Map buffers = new LinkedHashMap();
        volatile boolean cancelled;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        long index;
        final CompositeDisposable observers = new CompositeDisposable();
        final SpscLinkedArrayQueue queue = new SpscLinkedArrayQueue(Observable.bufferSize());
        final AtomicReference upstream = new AtomicReference();

        final class BufferOpenObserver extends AtomicReference implements Observer, Disposable {
            private static final long serialVersionUID = -8498650778633225126L;
            final BufferBoundaryObserver parent;

            BufferOpenObserver(BufferBoundaryObserver bufferBoundaryObserver) {
                this.parent = bufferBoundaryObserver;
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }

            public boolean isDisposed() {
                return get() == DisposableHelper.DISPOSED;
            }

            public void onComplete() {
                lazySet(DisposableHelper.DISPOSED);
                this.parent.openComplete(this);
            }

            public void onError(Throwable th) {
                lazySet(DisposableHelper.DISPOSED);
                this.parent.boundaryError(this, th);
            }

            public void onNext(Object obj) {
                this.parent.open(obj);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }
        }

        BufferBoundaryObserver(Observer observer, ObservableSource observableSource, Function function, Callable callable) {
            this.actual = observer;
            this.bufferSupplier = callable;
            this.bufferOpen = observableSource;
            this.bufferClose = function;
        }

        /* access modifiers changed from: 0000 */
        public void boundaryError(Disposable disposable, Throwable th) {
            DisposableHelper.dispose(this.upstream);
            this.observers.delete(disposable);
            onError(th);
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x002d, code lost:
            if (r4 == false) goto L_0x0031;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x002f, code lost:
            r3.done = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0031, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0034, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void close(BufferCloseObserver bufferCloseObserver, long j) {
            boolean z;
            this.observers.delete(bufferCloseObserver);
            if (this.observers.size() == 0) {
                DisposableHelper.dispose(this.upstream);
                z = true;
            } else {
                z = false;
            }
            synchronized (this) {
                if (this.buffers != null) {
                    this.queue.offer(this.buffers.remove(Long.valueOf(j)));
                }
            }
        }

        public void dispose() {
            if (DisposableHelper.dispose(this.upstream)) {
                this.cancelled = true;
                this.observers.dispose();
                synchronized (this) {
                    this.buffers = null;
                }
                if (getAndIncrement() != 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                Observer observer = this.actual;
                SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
                int i = 1;
                while (!this.cancelled) {
                    boolean z = this.done;
                    if (!z || this.errors.get() == null) {
                        Collection collection = (Collection) spscLinkedArrayQueue.poll();
                        boolean z2 = collection == null;
                        if (z && z2) {
                            observer.onComplete();
                            return;
                        } else if (z2) {
                            i = addAndGet(-i);
                            if (i == 0) {
                                return;
                            }
                        } else {
                            observer.onNext(collection);
                        }
                    } else {
                        spscLinkedArrayQueue.clear();
                        observer.onError(this.errors.terminate());
                        return;
                    }
                }
                spscLinkedArrayQueue.clear();
            }
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.upstream.get());
        }

        public void onComplete() {
            this.observers.dispose();
            synchronized (this) {
                Map map = this.buffers;
                if (map != null) {
                    for (Collection offer : map.values()) {
                        this.queue.offer(offer);
                    }
                    this.buffers = null;
                    this.done = true;
                    drain();
                }
            }
        }

        public void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                this.observers.dispose();
                synchronized (this) {
                    this.buffers = null;
                }
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            synchronized (this) {
                Map map = this.buffers;
                if (map != null) {
                    for (Collection add : map.values()) {
                        add.add(obj);
                    }
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this.upstream, disposable)) {
                BufferOpenObserver bufferOpenObserver = new BufferOpenObserver(this);
                this.observers.add(bufferOpenObserver);
                this.bufferOpen.subscribe(bufferOpenObserver);
            }
        }

        /* access modifiers changed from: 0000 */
        public void open(Object obj) {
            try {
                Object call = this.bufferSupplier.call();
                ObjectHelper.requireNonNull(call, "The bufferSupplier returned a null Collection");
                Collection collection = (Collection) call;
                Object apply = this.bufferClose.apply(obj);
                ObjectHelper.requireNonNull(apply, "The bufferClose returned a null ObservableSource");
                ObservableSource observableSource = (ObservableSource) apply;
                long j = this.index;
                this.index = 1 + j;
                synchronized (this) {
                    Map map = this.buffers;
                    if (map != null) {
                        map.put(Long.valueOf(j), collection);
                        BufferCloseObserver bufferCloseObserver = new BufferCloseObserver(this, j);
                        this.observers.add(bufferCloseObserver);
                        observableSource.subscribe(bufferCloseObserver);
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                DisposableHelper.dispose(this.upstream);
                onError(th);
            }
        }

        /* access modifiers changed from: 0000 */
        public void openComplete(BufferOpenObserver bufferOpenObserver) {
            this.observers.delete(bufferOpenObserver);
            if (this.observers.size() == 0) {
                DisposableHelper.dispose(this.upstream);
                this.done = true;
                drain();
            }
        }
    }

    final class BufferCloseObserver extends AtomicReference implements Observer, Disposable {
        private static final long serialVersionUID = -8498650778633225126L;
        final long index;
        final BufferBoundaryObserver parent;

        BufferCloseObserver(BufferBoundaryObserver bufferBoundaryObserver, long j) {
            this.parent = bufferBoundaryObserver;
            this.index = j;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return get() == DisposableHelper.DISPOSED;
        }

        public void onComplete() {
            Object obj = get();
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (obj != disposableHelper) {
                lazySet(disposableHelper);
                this.parent.close(this, this.index);
            }
        }

        public void onError(Throwable th) {
            Object obj = get();
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (obj != disposableHelper) {
                lazySet(disposableHelper);
                this.parent.boundaryError(this, th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            Disposable disposable = (Disposable) get();
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (disposable != disposableHelper) {
                lazySet(disposableHelper);
                disposable.dispose();
                this.parent.close(this, this.index);
            }
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }
    }

    public ObservableBufferBoundary(ObservableSource observableSource, ObservableSource observableSource2, Function function, Callable callable) {
        super(observableSource);
        this.bufferOpen = observableSource2;
        this.bufferClose = function;
        this.bufferSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        BufferBoundaryObserver bufferBoundaryObserver = new BufferBoundaryObserver(observer, this.bufferOpen, this.bufferClose, this.bufferSupplier);
        observer.onSubscribe(bufferBoundaryObserver);
        this.source.subscribe(bufferBoundaryObserver);
    }
}
