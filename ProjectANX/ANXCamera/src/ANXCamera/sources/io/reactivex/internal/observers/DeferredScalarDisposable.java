package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.plugins.RxJavaPlugins;

public class DeferredScalarDisposable extends BasicIntQueueDisposable {
    static final int DISPOSED = 4;
    static final int FUSED_CONSUMED = 32;
    static final int FUSED_EMPTY = 8;
    static final int FUSED_READY = 16;
    static final int TERMINATED = 2;
    private static final long serialVersionUID = -5502432239815349361L;
    protected final Observer actual;
    protected Object value;

    public DeferredScalarDisposable(Observer observer) {
        this.actual = observer;
    }

    public final void clear() {
        lazySet(32);
        this.value = null;
    }

    public final void complete() {
        if ((get() & 54) == 0) {
            lazySet(2);
            this.actual.onComplete();
        }
    }

    public final void complete(Object obj) {
        int i;
        int i2 = get();
        if ((i2 & 54) == 0) {
            if (i2 == 8) {
                this.value = obj;
                i = 16;
            } else {
                i = 2;
            }
            lazySet(i);
            Observer observer = this.actual;
            observer.onNext(obj);
            if (get() != 4) {
                observer.onComplete();
            }
        }
    }

    public void dispose() {
        set(4);
        this.value = null;
    }

    public final void error(Throwable th) {
        if ((get() & 54) != 0) {
            RxJavaPlugins.onError(th);
            return;
        }
        lazySet(2);
        this.actual.onError(th);
    }

    public final boolean isDisposed() {
        return get() == 4;
    }

    public final boolean isEmpty() {
        return get() != 16;
    }

    @Nullable
    public final Object poll() {
        if (get() != 16) {
            return null;
        }
        Object obj = this.value;
        this.value = null;
        lazySet(32);
        return obj;
    }

    public final int requestFusion(int i) {
        if ((i & 2) == 0) {
            return 0;
        }
        lazySet(8);
        return 2;
    }

    public final boolean tryDispose() {
        return getAndSet(4) != 4;
    }
}
