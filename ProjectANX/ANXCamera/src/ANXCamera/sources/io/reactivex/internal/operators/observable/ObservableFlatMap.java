package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableFlatMap extends AbstractObservableWithUpstream {
    final int bufferSize;
    final boolean delayErrors;
    final Function mapper;
    final int maxConcurrency;

    final class InnerObserver extends AtomicReference implements Observer {
        private static final long serialVersionUID = -4606175640614850599L;
        volatile boolean done;
        int fusionMode;
        final long id;
        final MergeObserver parent;
        volatile SimpleQueue queue;

        InnerObserver(MergeObserver mergeObserver, long j) {
            this.id = j;
            this.parent = mergeObserver;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void onError(Throwable th) {
            if (this.parent.errors.addThrowable(th)) {
                MergeObserver mergeObserver = this.parent;
                if (!mergeObserver.delayErrors) {
                    mergeObserver.disposeAll();
                }
                this.done = true;
                this.parent.drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onNext(Object obj) {
            if (this.fusionMode == 0) {
                this.parent.tryEmit(obj, this);
            } else {
                this.parent.drain();
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable) && (disposable instanceof QueueDisposable)) {
                QueueDisposable queueDisposable = (QueueDisposable) disposable;
                int requestFusion = queueDisposable.requestFusion(7);
                if (requestFusion == 1) {
                    this.fusionMode = requestFusion;
                    this.queue = queueDisposable;
                    this.done = true;
                    this.parent.drain();
                } else if (requestFusion == 2) {
                    this.fusionMode = requestFusion;
                    this.queue = queueDisposable;
                }
            }
        }
    }

    final class MergeObserver extends AtomicInteger implements Disposable, Observer {
        static final InnerObserver[] CANCELLED = new InnerObserver[0];
        static final InnerObserver[] EMPTY = new InnerObserver[0];
        private static final long serialVersionUID = -2117620485640801370L;
        final Observer actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        long lastId;
        int lastIndex;
        final Function mapper;
        final int maxConcurrency;
        final AtomicReference observers;
        volatile SimplePlainQueue queue;
        Disposable s;
        Queue sources;
        long uniqueId;
        int wip;

        MergeObserver(Observer observer, Function function, boolean z, int i, int i2) {
            this.actual = observer;
            this.mapper = function;
            this.delayErrors = z;
            this.maxConcurrency = i;
            this.bufferSize = i2;
            if (i != Integer.MAX_VALUE) {
                this.sources = new ArrayDeque(i);
            }
            this.observers = new AtomicReference(EMPTY);
        }

        /* access modifiers changed from: 0000 */
        public boolean addInner(InnerObserver innerObserver) {
            InnerObserver[] innerObserverArr;
            InnerObserver[] innerObserverArr2;
            do {
                innerObserverArr = (InnerObserver[]) this.observers.get();
                if (innerObserverArr == CANCELLED) {
                    innerObserver.dispose();
                    return false;
                }
                int length = innerObserverArr.length;
                innerObserverArr2 = new InnerObserver[(length + 1)];
                System.arraycopy(innerObserverArr, 0, innerObserverArr2, 0, length);
                innerObserverArr2[length] = innerObserver;
            } while (!this.observers.compareAndSet(innerObserverArr, innerObserverArr2));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminate() {
            if (this.cancelled) {
                return true;
            }
            Throwable th = (Throwable) this.errors.get();
            if (this.delayErrors || th == null) {
                return false;
            }
            disposeAll();
            Throwable terminate = this.errors.terminate();
            if (terminate != ExceptionHelper.TERMINATED) {
                this.actual.onError(terminate);
            }
            return true;
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (disposeAll()) {
                    Throwable terminate = this.errors.terminate();
                    if (terminate != null && terminate != ExceptionHelper.TERMINATED) {
                        RxJavaPlugins.onError(terminate);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean disposeAll() {
            this.s.dispose();
            InnerObserver[] innerObserverArr = (InnerObserver[]) this.observers.get();
            InnerObserver[] innerObserverArr2 = CANCELLED;
            if (innerObserverArr != innerObserverArr2) {
                InnerObserver[] innerObserverArr3 = (InnerObserver[]) this.observers.getAndSet(innerObserverArr2);
                if (innerObserverArr3 != CANCELLED) {
                    for (InnerObserver dispose : innerObserverArr3) {
                        dispose.dispose();
                    }
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a2, code lost:
            if (r11 != null) goto L_0x0090;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            Observer observer = this.actual;
            int i = 1;
            while (!checkTerminate()) {
                SimplePlainQueue simplePlainQueue = this.queue;
                if (simplePlainQueue != null) {
                    while (!checkTerminate()) {
                        Object poll = simplePlainQueue.poll();
                        if (poll != null) {
                            observer.onNext(poll);
                        } else if (poll == null) {
                        }
                    }
                    return;
                }
                boolean z = this.done;
                SimplePlainQueue simplePlainQueue2 = this.queue;
                InnerObserver[] innerObserverArr = (InnerObserver[]) this.observers.get();
                int length = innerObserverArr.length;
                if (!z || ((simplePlainQueue2 != null && !simplePlainQueue2.isEmpty()) || length != 0)) {
                    boolean z2 = false;
                    if (length != 0) {
                        long j = this.lastId;
                        int i2 = this.lastIndex;
                        if (length <= i2 || innerObserverArr[i2].id != j) {
                            if (length <= i2) {
                                i2 = 0;
                            }
                            int i3 = i2;
                            for (int i4 = 0; i4 < length && innerObserverArr[i3].id != j; i4++) {
                                i3++;
                                if (i3 == length) {
                                    i3 = 0;
                                }
                            }
                            this.lastIndex = i3;
                            this.lastId = innerObserverArr[i3].id;
                            i2 = i3;
                        }
                        boolean z3 = false;
                        int i5 = i2;
                        int i6 = 0;
                        while (i6 < length) {
                            if (!checkTerminate()) {
                                InnerObserver innerObserver = innerObserverArr[i5];
                                while (!checkTerminate()) {
                                    SimpleQueue simpleQueue = innerObserver.queue;
                                    if (simpleQueue != null) {
                                        while (true) {
                                            try {
                                                Object poll2 = simpleQueue.poll();
                                                if (poll2 == null) {
                                                    break;
                                                }
                                                observer.onNext(poll2);
                                                if (checkTerminate()) {
                                                    return;
                                                }
                                            } catch (Throwable th) {
                                                Exceptions.throwIfFatal(th);
                                                innerObserver.dispose();
                                                this.errors.addThrowable(th);
                                                if (!checkTerminate()) {
                                                    removeInner(innerObserver);
                                                    i6++;
                                                    z3 = true;
                                                } else {
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    boolean z4 = innerObserver.done;
                                    SimpleQueue simpleQueue2 = innerObserver.queue;
                                    if (z4 && (simpleQueue2 == null || simpleQueue2.isEmpty())) {
                                        removeInner(innerObserver);
                                        if (!checkTerminate()) {
                                            z3 = true;
                                        } else {
                                            return;
                                        }
                                    }
                                    i5++;
                                    if (i5 == length) {
                                        i5 = 0;
                                    }
                                    i6++;
                                }
                                return;
                            }
                            return;
                        }
                        this.lastIndex = i5;
                        this.lastId = innerObserverArr[i5].id;
                        z2 = z3;
                    }
                    if (!z2) {
                        i = addAndGet(-i);
                        if (i == 0) {
                            return;
                        }
                    } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                        synchronized (this) {
                            ObservableSource observableSource = (ObservableSource) this.sources.poll();
                            if (observableSource == null) {
                                this.wip--;
                            } else {
                                subscribeInner(observableSource);
                            }
                        }
                    } else {
                        continue;
                    }
                } else {
                    Throwable terminate = this.errors.terminate();
                    if (terminate != ExceptionHelper.TERMINATED) {
                        if (terminate == null) {
                            observer.onComplete();
                        } else {
                            observer.onError(terminate);
                        }
                    }
                    return;
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (this.errors.addThrowable(th)) {
                this.done = true;
                drain();
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void onNext(Object obj) {
            if (!this.done) {
                try {
                    Object apply = this.mapper.apply(obj);
                    ObjectHelper.requireNonNull(apply, "The mapper returned a null ObservableSource");
                    ObservableSource observableSource = (ObservableSource) apply;
                    if (this.maxConcurrency != Integer.MAX_VALUE) {
                        synchronized (this) {
                            if (this.wip == this.maxConcurrency) {
                                this.sources.offer(observableSource);
                                return;
                            }
                            this.wip++;
                        }
                    }
                    subscribeInner(observableSource);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.s.dispose();
                    onError(th);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public void removeInner(InnerObserver innerObserver) {
            InnerObserver[] innerObserverArr;
            InnerObserver[] innerObserverArr2;
            do {
                innerObserverArr = (InnerObserver[]) this.observers.get();
                int length = innerObserverArr.length;
                if (length != 0) {
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerObserverArr[i2] == innerObserver) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            innerObserverArr2 = EMPTY;
                        } else {
                            InnerObserver[] innerObserverArr3 = new InnerObserver[(length - 1)];
                            System.arraycopy(innerObserverArr, 0, innerObserverArr3, 0, i);
                            System.arraycopy(innerObserverArr, i + 1, innerObserverArr3, i, (length - i) - 1);
                            innerObserverArr2 = innerObserverArr3;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(innerObserverArr, innerObserverArr2));
        }

        /* access modifiers changed from: 0000 */
        public void subscribeInner(ObservableSource observableSource) {
            while (observableSource instanceof Callable) {
                tryEmitScalar((Callable) observableSource);
                if (this.maxConcurrency != Integer.MAX_VALUE) {
                    synchronized (this) {
                        observableSource = (ObservableSource) this.sources.poll();
                        if (observableSource == null) {
                            this.wip--;
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
            long j = this.uniqueId;
            this.uniqueId = 1 + j;
            InnerObserver innerObserver = new InnerObserver(this, j);
            if (addInner(innerObserver)) {
                observableSource.subscribe(innerObserver);
            }
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(Object obj, InnerObserver innerObserver) {
            if (get() != 0 || !compareAndSet(0, 1)) {
                SimpleQueue simpleQueue = innerObserver.queue;
                if (simpleQueue == null) {
                    simpleQueue = new SpscLinkedArrayQueue(this.bufferSize);
                    innerObserver.queue = simpleQueue;
                }
                simpleQueue.offer(obj);
                if (getAndIncrement() != 0) {
                    return;
                }
            } else {
                this.actual.onNext(obj);
                if (decrementAndGet() == 0) {
                    return;
                }
            }
            drainLoop();
        }

        /* access modifiers changed from: 0000 */
        public void tryEmitScalar(Callable callable) {
            try {
                Object call = callable.call();
                if (call != null) {
                    if (get() != 0 || !compareAndSet(0, 1)) {
                        SimplePlainQueue simplePlainQueue = this.queue;
                        if (simplePlainQueue == null) {
                            int i = this.maxConcurrency;
                            simplePlainQueue = i == Integer.MAX_VALUE ? new SpscLinkedArrayQueue(this.bufferSize) : new SpscArrayQueue(i);
                            this.queue = simplePlainQueue;
                        }
                        if (!simplePlainQueue.offer(call)) {
                            onError(new IllegalStateException("Scalar queue full?!"));
                            return;
                        } else if (getAndIncrement() != 0) {
                            return;
                        }
                    } else {
                        this.actual.onNext(call);
                        if (decrementAndGet() == 0) {
                            return;
                        }
                    }
                    drainLoop();
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.errors.addThrowable(th);
                drain();
            }
        }
    }

    public ObservableFlatMap(ObservableSource observableSource, Function function, boolean z, int i, int i2) {
        super(observableSource);
        this.mapper = function;
        this.delayErrors = z;
        this.maxConcurrency = i;
        this.bufferSize = i2;
    }

    public void subscribeActual(Observer observer) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, observer, this.mapper)) {
            ObservableSource observableSource = this.source;
            MergeObserver mergeObserver = new MergeObserver(observer, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize);
            observableSource.subscribe(mergeObserver);
        }
    }
}
