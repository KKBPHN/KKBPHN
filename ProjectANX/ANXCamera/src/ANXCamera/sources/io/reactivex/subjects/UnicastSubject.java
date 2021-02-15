package io.reactivex.subjects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.observers.BasicIntQueueDisposable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class UnicastSubject extends Subject {
    final AtomicReference actual;
    final boolean delayError;
    volatile boolean disposed;
    volatile boolean done;
    boolean enableOperatorFusion;
    Throwable error;
    final AtomicReference onTerminate;
    final AtomicBoolean once;
    final SpscLinkedArrayQueue queue;
    final BasicIntQueueDisposable wip;

    final class UnicastQueueDisposable extends BasicIntQueueDisposable {
        private static final long serialVersionUID = 7926949470189395511L;

        UnicastQueueDisposable() {
        }

        public void clear() {
            UnicastSubject.this.queue.clear();
        }

        public void dispose() {
            if (!UnicastSubject.this.disposed) {
                UnicastSubject unicastSubject = UnicastSubject.this;
                unicastSubject.disposed = true;
                unicastSubject.doTerminate();
                UnicastSubject.this.actual.lazySet(null);
                if (UnicastSubject.this.wip.getAndIncrement() == 0) {
                    UnicastSubject.this.actual.lazySet(null);
                    UnicastSubject.this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return UnicastSubject.this.disposed;
        }

        public boolean isEmpty() {
            return UnicastSubject.this.queue.isEmpty();
        }

        @Nullable
        public Object poll() {
            return UnicastSubject.this.queue.poll();
        }

        public int requestFusion(int i) {
            if ((i & 2) == 0) {
                return 0;
            }
            UnicastSubject.this.enableOperatorFusion = true;
            return 2;
        }
    }

    UnicastSubject(int i, Runnable runnable) {
        this(i, runnable, true);
    }

    UnicastSubject(int i, Runnable runnable, boolean z) {
        ObjectHelper.verifyPositive(i, "capacityHint");
        this.queue = new SpscLinkedArrayQueue(i);
        ObjectHelper.requireNonNull(runnable, "onTerminate");
        this.onTerminate = new AtomicReference(runnable);
        this.delayError = z;
        this.actual = new AtomicReference();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueDisposable();
    }

    UnicastSubject(int i, boolean z) {
        ObjectHelper.verifyPositive(i, "capacityHint");
        this.queue = new SpscLinkedArrayQueue(i);
        this.onTerminate = new AtomicReference();
        this.delayError = z;
        this.actual = new AtomicReference();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueDisposable();
    }

    @CheckReturnValue
    public static UnicastSubject create() {
        return new UnicastSubject(Observable.bufferSize(), true);
    }

    @CheckReturnValue
    public static UnicastSubject create(int i) {
        return new UnicastSubject(i, true);
    }

    @CheckReturnValue
    public static UnicastSubject create(int i, Runnable runnable) {
        return new UnicastSubject(i, runnable, true);
    }

    @CheckReturnValue
    @Experimental
    public static UnicastSubject create(int i, Runnable runnable, boolean z) {
        return new UnicastSubject(i, runnable, z);
    }

    @CheckReturnValue
    @Experimental
    public static UnicastSubject create(boolean z) {
        return new UnicastSubject(Observable.bufferSize(), z);
    }

    /* access modifiers changed from: 0000 */
    public void doTerminate() {
        Runnable runnable = (Runnable) this.onTerminate.get();
        if (runnable != null && this.onTerminate.compareAndSet(runnable, null)) {
            runnable.run();
        }
    }

    /* access modifiers changed from: 0000 */
    public void drain() {
        if (this.wip.getAndIncrement() == 0) {
            Observer observer = (Observer) this.actual.get();
            int i = 1;
            while (observer == null) {
                i = this.wip.addAndGet(-i);
                if (i != 0) {
                    observer = (Observer) this.actual.get();
                } else {
                    return;
                }
            }
            if (this.enableOperatorFusion) {
                drainFused(observer);
            } else {
                drainNormal(observer);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void drainFused(Observer observer) {
        SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
        int i = 1;
        boolean z = !this.delayError;
        while (!this.disposed) {
            boolean z2 = this.done;
            if (!z || !z2 || !failedFast(spscLinkedArrayQueue, observer)) {
                observer.onNext(null);
                if (z2) {
                    errorOrComplete(observer);
                    return;
                }
                i = this.wip.addAndGet(-i);
                if (i == 0) {
                    return;
                }
            } else {
                return;
            }
        }
        this.actual.lazySet(null);
        spscLinkedArrayQueue.clear();
    }

    /* access modifiers changed from: 0000 */
    public void drainNormal(Observer observer) {
        SpscLinkedArrayQueue spscLinkedArrayQueue = this.queue;
        boolean z = !this.delayError;
        boolean z2 = true;
        int i = 1;
        while (!this.disposed) {
            boolean z3 = this.done;
            Object poll = this.queue.poll();
            boolean z4 = poll == null;
            if (z3) {
                if (z && z2) {
                    if (!failedFast(spscLinkedArrayQueue, observer)) {
                        z2 = false;
                    } else {
                        return;
                    }
                }
                if (z4) {
                    errorOrComplete(observer);
                    return;
                }
            }
            if (z4) {
                i = this.wip.addAndGet(-i);
                if (i == 0) {
                    return;
                }
            } else {
                observer.onNext(poll);
            }
        }
        this.actual.lazySet(null);
        spscLinkedArrayQueue.clear();
    }

    /* access modifiers changed from: 0000 */
    public void errorOrComplete(Observer observer) {
        this.actual.lazySet(null);
        Throwable th = this.error;
        if (th != null) {
            observer.onError(th);
        } else {
            observer.onComplete();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean failedFast(SimpleQueue simpleQueue, Observer observer) {
        Throwable th = this.error;
        if (th == null) {
            return false;
        }
        this.actual.lazySet(null);
        simpleQueue.clear();
        observer.onError(th);
        return true;
    }

    public Throwable getThrowable() {
        if (this.done) {
            return this.error;
        }
        return null;
    }

    public boolean hasComplete() {
        return this.done && this.error == null;
    }

    public boolean hasObservers() {
        return this.actual.get() != null;
    }

    public boolean hasThrowable() {
        return this.done && this.error != null;
    }

    public void onComplete() {
        if (!this.done && !this.disposed) {
            this.done = true;
            doTerminate();
            drain();
        }
    }

    public void onError(Throwable th) {
        ObjectHelper.requireNonNull(th, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.done || this.disposed) {
            RxJavaPlugins.onError(th);
            return;
        }
        this.error = th;
        this.done = true;
        doTerminate();
        drain();
    }

    public void onNext(Object obj) {
        ObjectHelper.requireNonNull(obj, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (!this.done && !this.disposed) {
            this.queue.offer(obj);
            drain();
        }
    }

    public void onSubscribe(Disposable disposable) {
        if (this.done || this.disposed) {
            disposable.dispose();
        }
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        if (this.once.get() || !this.once.compareAndSet(false, true)) {
            EmptyDisposable.error((Throwable) new IllegalStateException("Only a single observer allowed."), observer);
        } else {
            observer.onSubscribe(this.wip);
            this.actual.lazySet(observer);
            if (this.disposed) {
                this.actual.lazySet(null);
                return;
            }
            drain();
        }
    }
}
