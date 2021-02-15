package io.reactivex.internal.subscriptions;

import io.reactivex.annotations.Nullable;
import org.reactivestreams.Subscriber;

public class DeferredScalarSubscription extends BasicIntQueueSubscription {
    static final int CANCELLED = 4;
    static final int FUSED_CONSUMED = 32;
    static final int FUSED_EMPTY = 8;
    static final int FUSED_READY = 16;
    static final int HAS_REQUEST_HAS_VALUE = 3;
    static final int HAS_REQUEST_NO_VALUE = 2;
    static final int NO_REQUEST_HAS_VALUE = 1;
    static final int NO_REQUEST_NO_VALUE = 0;
    private static final long serialVersionUID = -2151279923272604993L;
    protected final Subscriber actual;
    protected Object value;

    public DeferredScalarSubscription(Subscriber subscriber) {
        this.actual = subscriber;
    }

    public void cancel() {
        set(4);
        this.value = null;
    }

    public final void clear() {
        lazySet(32);
        this.value = null;
    }

    public final void complete(Object obj) {
        int i = get();
        while (i != 8) {
            if ((i & -3) == 0) {
                if (i == 2) {
                    lazySet(3);
                    Subscriber subscriber = this.actual;
                    subscriber.onNext(obj);
                    if (get() != 4) {
                        subscriber.onComplete();
                    }
                    return;
                }
                this.value = obj;
                if (!compareAndSet(0, 1)) {
                    i = get();
                    if (i == 4) {
                        this.value = null;
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        this.value = obj;
        lazySet(16);
        Subscriber subscriber2 = this.actual;
        subscriber2.onNext(obj);
        if (get() != 4) {
            subscriber2.onComplete();
        }
    }

    public final boolean isCancelled() {
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
        lazySet(32);
        Object obj = this.value;
        this.value = null;
        return obj;
    }

    public final void request(long j) {
        if (SubscriptionHelper.validate(j)) {
            do {
                int i = get();
                if ((i & -2) == 0) {
                    if (i == 1) {
                        if (compareAndSet(1, 3)) {
                            Object obj = this.value;
                            if (obj != null) {
                                this.value = null;
                                Subscriber subscriber = this.actual;
                                subscriber.onNext(obj);
                                if (get() != 4) {
                                    subscriber.onComplete();
                                }
                            }
                        }
                        return;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(0, 2));
        }
    }

    public final int requestFusion(int i) {
        if ((i & 2) == 0) {
            return 0;
        }
        lazySet(8);
        return 2;
    }

    public final boolean tryCancel() {
        return getAndSet(4) != 4;
    }
}
