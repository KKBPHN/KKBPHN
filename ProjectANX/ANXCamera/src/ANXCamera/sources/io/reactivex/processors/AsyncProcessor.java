package io.reactivex.processors;

import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class AsyncProcessor extends FlowableProcessor {
    static final AsyncSubscription[] EMPTY = new AsyncSubscription[0];
    static final AsyncSubscription[] TERMINATED = new AsyncSubscription[0];
    Throwable error;
    final AtomicReference subscribers = new AtomicReference(EMPTY);
    Object value;

    final class AsyncSubscription extends DeferredScalarSubscription {
        private static final long serialVersionUID = 5629876084736248016L;
        final AsyncProcessor parent;

        AsyncSubscription(Subscriber subscriber, AsyncProcessor asyncProcessor) {
            super(subscriber);
            this.parent = asyncProcessor;
        }

        public void cancel() {
            if (super.tryCancel()) {
                this.parent.remove(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public void onComplete() {
            if (!isCancelled()) {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void onError(Throwable th) {
            if (isCancelled()) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }
    }

    AsyncProcessor() {
    }

    @CheckReturnValue
    @NonNull
    public static AsyncProcessor create() {
        return new AsyncProcessor();
    }

    /* access modifiers changed from: 0000 */
    public boolean add(AsyncSubscription asyncSubscription) {
        AsyncSubscription[] asyncSubscriptionArr;
        AsyncSubscription[] asyncSubscriptionArr2;
        do {
            asyncSubscriptionArr = (AsyncSubscription[]) this.subscribers.get();
            if (asyncSubscriptionArr == TERMINATED) {
                return false;
            }
            int length = asyncSubscriptionArr.length;
            asyncSubscriptionArr2 = new AsyncSubscription[(length + 1)];
            System.arraycopy(asyncSubscriptionArr, 0, asyncSubscriptionArr2, 0, length);
            asyncSubscriptionArr2[length] = asyncSubscription;
        } while (!this.subscribers.compareAndSet(asyncSubscriptionArr, asyncSubscriptionArr2));
        return true;
    }

    public Throwable getThrowable() {
        if (this.subscribers.get() == TERMINATED) {
            return this.error;
        }
        return null;
    }

    public Object getValue() {
        if (this.subscribers.get() == TERMINATED) {
            return this.value;
        }
        return null;
    }

    public Object[] getValues() {
        Object value2 = getValue();
        if (value2 == null) {
            return new Object[0];
        }
        return new Object[]{value2};
    }

    public Object[] getValues(Object[] objArr) {
        Object value2 = getValue();
        if (value2 == null) {
            if (objArr.length != 0) {
                objArr[0] = null;
            }
            return objArr;
        }
        if (objArr.length == 0) {
            objArr = Arrays.copyOf(objArr, 1);
        }
        objArr[0] = value2;
        if (objArr.length != 1) {
            objArr[1] = null;
        }
        return objArr;
    }

    public boolean hasComplete() {
        return this.subscribers.get() == TERMINATED && this.error == null;
    }

    public boolean hasSubscribers() {
        return ((AsyncSubscription[]) this.subscribers.get()).length != 0;
    }

    public boolean hasThrowable() {
        return this.subscribers.get() == TERMINATED && this.error != null;
    }

    public boolean hasValue() {
        return this.subscribers.get() == TERMINATED && this.value != null;
    }

    public void onComplete() {
        Object obj = this.subscribers.get();
        Object obj2 = TERMINATED;
        if (obj != obj2) {
            Object obj3 = this.value;
            AsyncSubscription[] asyncSubscriptionArr = (AsyncSubscription[]) this.subscribers.getAndSet(obj2);
            int i = 0;
            if (obj3 == null) {
                int length = asyncSubscriptionArr.length;
                while (i < length) {
                    asyncSubscriptionArr[i].onComplete();
                    i++;
                }
            } else {
                int length2 = asyncSubscriptionArr.length;
                while (i < length2) {
                    asyncSubscriptionArr[i].complete(obj3);
                    i++;
                }
            }
        }
    }

    public void onError(Throwable th) {
        ObjectHelper.requireNonNull(th, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        Object obj = this.subscribers.get();
        Object obj2 = TERMINATED;
        if (obj == obj2) {
            RxJavaPlugins.onError(th);
            return;
        }
        this.value = null;
        this.error = th;
        for (AsyncSubscription onError : (AsyncSubscription[]) this.subscribers.getAndSet(obj2)) {
            onError.onError(th);
        }
    }

    public void onNext(Object obj) {
        ObjectHelper.requireNonNull(obj, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.subscribers.get() != TERMINATED) {
            this.value = obj;
        }
    }

    public void onSubscribe(Subscription subscription) {
        if (this.subscribers.get() == TERMINATED) {
            subscription.cancel();
        } else {
            subscription.request(Long.MAX_VALUE);
        }
    }

    /* access modifiers changed from: 0000 */
    public void remove(AsyncSubscription asyncSubscription) {
        AsyncSubscription[] asyncSubscriptionArr;
        AsyncSubscription[] asyncSubscriptionArr2;
        do {
            asyncSubscriptionArr = (AsyncSubscription[]) this.subscribers.get();
            int length = asyncSubscriptionArr.length;
            if (length != 0) {
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (asyncSubscriptionArr[i2] == asyncSubscription) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        asyncSubscriptionArr2 = EMPTY;
                    } else {
                        AsyncSubscription[] asyncSubscriptionArr3 = new AsyncSubscription[(length - 1)];
                        System.arraycopy(asyncSubscriptionArr, 0, asyncSubscriptionArr3, 0, i);
                        System.arraycopy(asyncSubscriptionArr, i + 1, asyncSubscriptionArr3, i, (length - i) - 1);
                        asyncSubscriptionArr2 = asyncSubscriptionArr3;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(asyncSubscriptionArr, asyncSubscriptionArr2));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        AsyncSubscription asyncSubscription = new AsyncSubscription(subscriber, this);
        subscriber.onSubscribe(asyncSubscription);
        if (!add(asyncSubscription)) {
            Throwable th = this.error;
            if (th != null) {
                subscriber.onError(th);
                return;
            }
            Object obj = this.value;
            if (obj != null) {
                asyncSubscription.complete(obj);
            } else {
                asyncSubscription.onComplete();
            }
        } else if (asyncSubscription.isCancelled()) {
            remove(asyncSubscription);
        }
    }
}
