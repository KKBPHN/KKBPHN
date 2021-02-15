package io.reactivex.processors;

import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.Experimental;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList.NonThrowingPredicate;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class BehaviorProcessor extends FlowableProcessor {
    static final BehaviorSubscription[] EMPTY = new BehaviorSubscription[0];
    static final Object[] EMPTY_ARRAY = new Object[0];
    static final BehaviorSubscription[] TERMINATED = new BehaviorSubscription[0];
    long index;
    final ReadWriteLock lock;
    final Lock readLock;
    final AtomicReference subscribers;
    final AtomicReference terminalEvent;
    final AtomicReference value;
    final Lock writeLock;

    final class BehaviorSubscription extends AtomicLong implements Subscription, NonThrowingPredicate {
        private static final long serialVersionUID = 3293175281126227086L;
        final Subscriber actual;
        volatile boolean cancelled;
        boolean emitting;
        boolean fastPath;
        long index;
        boolean next;
        AppendOnlyLinkedArrayList queue;
        final BehaviorProcessor state;

        BehaviorSubscription(Subscriber subscriber, BehaviorProcessor behaviorProcessor) {
            this.actual = subscriber;
            this.state = behaviorProcessor;
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.state.remove(this);
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0031, code lost:
            if (r0 == null) goto L_0x003d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0037, code lost:
            if (test(r0) == false) goto L_0x003a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0039, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003a, code lost:
            emitLoop();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x003d, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitFirst() {
            if (!this.cancelled) {
                synchronized (this) {
                    if (!this.cancelled) {
                        if (!this.next) {
                            BehaviorProcessor behaviorProcessor = this.state;
                            Lock lock = behaviorProcessor.readLock;
                            lock.lock();
                            this.index = behaviorProcessor.index;
                            Object obj = behaviorProcessor.value.get();
                            lock.unlock();
                            this.emitting = obj != null;
                            this.next = true;
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0013, code lost:
            r0.forEachWhile(r2);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitLoop() {
            while (!this.cancelled) {
                synchronized (this) {
                    AppendOnlyLinkedArrayList appendOnlyLinkedArrayList = this.queue;
                    if (appendOnlyLinkedArrayList == null) {
                        this.emitting = false;
                        return;
                    }
                    this.queue = null;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0031, code lost:
            r2.fastPath = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitNext(Object obj, long j) {
            if (!this.cancelled) {
                if (!this.fastPath) {
                    synchronized (this) {
                        if (!this.cancelled) {
                            if (this.index != j) {
                                if (this.emitting) {
                                    AppendOnlyLinkedArrayList appendOnlyLinkedArrayList = this.queue;
                                    if (appendOnlyLinkedArrayList == null) {
                                        appendOnlyLinkedArrayList = new AppendOnlyLinkedArrayList(4);
                                        this.queue = appendOnlyLinkedArrayList;
                                    }
                                    appendOnlyLinkedArrayList.add(obj);
                                    return;
                                }
                                this.next = true;
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
                test(obj);
            }
        }

        public boolean isFull() {
            return get() == 0;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this, j);
            }
        }

        public boolean test(Object obj) {
            if (this.cancelled) {
                return true;
            }
            if (NotificationLite.isComplete(obj)) {
                this.actual.onComplete();
                return true;
            } else if (NotificationLite.isError(obj)) {
                this.actual.onError(NotificationLite.getError(obj));
                return true;
            } else {
                long j = get();
                if (j != 0) {
                    Subscriber subscriber = this.actual;
                    NotificationLite.getValue(obj);
                    subscriber.onNext(obj);
                    if (j != Long.MAX_VALUE) {
                        decrementAndGet();
                    }
                    return false;
                }
                cancel();
                this.actual.onError(new MissingBackpressureException("Could not deliver value due to lack of requests"));
                return true;
            }
        }
    }

    BehaviorProcessor() {
        this.value = new AtomicReference();
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.subscribers = new AtomicReference(EMPTY);
        this.terminalEvent = new AtomicReference();
    }

    BehaviorProcessor(Object obj) {
        this();
        AtomicReference atomicReference = this.value;
        ObjectHelper.requireNonNull(obj, "defaultValue is null");
        atomicReference.lazySet(obj);
    }

    @CheckReturnValue
    public static BehaviorProcessor create() {
        return new BehaviorProcessor();
    }

    @CheckReturnValue
    public static BehaviorProcessor createDefault(Object obj) {
        ObjectHelper.requireNonNull(obj, "defaultValue is null");
        return new BehaviorProcessor(obj);
    }

    /* access modifiers changed from: 0000 */
    public boolean add(BehaviorSubscription behaviorSubscription) {
        BehaviorSubscription[] behaviorSubscriptionArr;
        BehaviorSubscription[] behaviorSubscriptionArr2;
        do {
            behaviorSubscriptionArr = (BehaviorSubscription[]) this.subscribers.get();
            if (behaviorSubscriptionArr == TERMINATED) {
                return false;
            }
            int length = behaviorSubscriptionArr.length;
            behaviorSubscriptionArr2 = new BehaviorSubscription[(length + 1)];
            System.arraycopy(behaviorSubscriptionArr, 0, behaviorSubscriptionArr2, 0, length);
            behaviorSubscriptionArr2[length] = behaviorSubscription;
        } while (!this.subscribers.compareAndSet(behaviorSubscriptionArr, behaviorSubscriptionArr2));
        return true;
    }

    public Throwable getThrowable() {
        Object obj = this.value.get();
        if (NotificationLite.isError(obj)) {
            return NotificationLite.getError(obj);
        }
        return null;
    }

    public Object getValue() {
        Object obj = this.value.get();
        if (NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) {
            return null;
        }
        NotificationLite.getValue(obj);
        return obj;
    }

    public Object[] getValues() {
        Object[] values = getValues(EMPTY_ARRAY);
        return values == EMPTY_ARRAY ? new Object[0] : values;
    }

    public Object[] getValues(Object[] objArr) {
        Object obj = this.value.get();
        if (obj == null || NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) {
            if (objArr.length != 0) {
                objArr[0] = null;
            }
            return objArr;
        }
        NotificationLite.getValue(obj);
        if (objArr.length != 0) {
            objArr[0] = obj;
            if (objArr.length != 1) {
                objArr[1] = null;
            }
        } else {
            objArr = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), 1);
            objArr[0] = obj;
        }
        return objArr;
    }

    public boolean hasComplete() {
        return NotificationLite.isComplete(this.value.get());
    }

    public boolean hasSubscribers() {
        return ((BehaviorSubscription[]) this.subscribers.get()).length != 0;
    }

    public boolean hasThrowable() {
        return NotificationLite.isError(this.value.get());
    }

    public boolean hasValue() {
        Object obj = this.value.get();
        return obj != null && !NotificationLite.isComplete(obj) && !NotificationLite.isError(obj);
    }

    @Experimental
    public boolean offer(Object obj) {
        if (obj == null) {
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            return true;
        }
        BehaviorSubscription[] behaviorSubscriptionArr = (BehaviorSubscription[]) this.subscribers.get();
        for (BehaviorSubscription isFull : behaviorSubscriptionArr) {
            if (isFull.isFull()) {
                return false;
            }
        }
        NotificationLite.next(obj);
        setCurrent(obj);
        for (BehaviorSubscription emitNext : behaviorSubscriptionArr) {
            emitNext.emitNext(obj, this.index);
        }
        return true;
    }

    public void onComplete() {
        if (this.terminalEvent.compareAndSet(null, ExceptionHelper.TERMINATED)) {
            Object complete = NotificationLite.complete();
            for (BehaviorSubscription emitNext : terminate(complete)) {
                emitNext.emitNext(complete, this.index);
            }
        }
    }

    public void onError(Throwable th) {
        ObjectHelper.requireNonNull(th, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (!this.terminalEvent.compareAndSet(null, th)) {
            RxJavaPlugins.onError(th);
            return;
        }
        Object error = NotificationLite.error(th);
        for (BehaviorSubscription emitNext : terminate(error)) {
            emitNext.emitNext(error, this.index);
        }
    }

    public void onNext(Object obj) {
        ObjectHelper.requireNonNull(obj, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.terminalEvent.get() == null) {
            NotificationLite.next(obj);
            setCurrent(obj);
            for (BehaviorSubscription emitNext : (BehaviorSubscription[]) this.subscribers.get()) {
                emitNext.emitNext(obj, this.index);
            }
        }
    }

    public void onSubscribe(Subscription subscription) {
        if (this.terminalEvent.get() != null) {
            subscription.cancel();
        } else {
            subscription.request(Long.MAX_VALUE);
        }
    }

    /* access modifiers changed from: 0000 */
    public void remove(BehaviorSubscription behaviorSubscription) {
        BehaviorSubscription[] behaviorSubscriptionArr;
        BehaviorSubscription[] behaviorSubscriptionArr2;
        do {
            behaviorSubscriptionArr = (BehaviorSubscription[]) this.subscribers.get();
            if (behaviorSubscriptionArr != TERMINATED && behaviorSubscriptionArr != EMPTY) {
                int length = behaviorSubscriptionArr.length;
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (behaviorSubscriptionArr[i2] == behaviorSubscription) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        behaviorSubscriptionArr2 = EMPTY;
                    } else {
                        BehaviorSubscription[] behaviorSubscriptionArr3 = new BehaviorSubscription[(length - 1)];
                        System.arraycopy(behaviorSubscriptionArr, 0, behaviorSubscriptionArr3, 0, i);
                        System.arraycopy(behaviorSubscriptionArr, i + 1, behaviorSubscriptionArr3, i, (length - i) - 1);
                        behaviorSubscriptionArr2 = behaviorSubscriptionArr3;
                    }
                } else {
                    return;
                }
            }
        } while (!this.subscribers.compareAndSet(behaviorSubscriptionArr, behaviorSubscriptionArr2));
    }

    /* access modifiers changed from: 0000 */
    public void setCurrent(Object obj) {
        Lock lock2 = this.writeLock;
        lock2.lock();
        this.index++;
        this.value.lazySet(obj);
        lock2.unlock();
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        BehaviorSubscription behaviorSubscription = new BehaviorSubscription(subscriber, this);
        subscriber.onSubscribe(behaviorSubscription);
        if (!add(behaviorSubscription)) {
            Throwable th = (Throwable) this.terminalEvent.get();
            if (th == ExceptionHelper.TERMINATED) {
                subscriber.onComplete();
            } else {
                subscriber.onError(th);
            }
        } else if (behaviorSubscription.cancelled) {
            remove(behaviorSubscription);
        } else {
            behaviorSubscription.emitFirst();
        }
    }

    /* access modifiers changed from: 0000 */
    public int subscriberCount() {
        return ((BehaviorSubscription[]) this.subscribers.get()).length;
    }

    /* access modifiers changed from: 0000 */
    public BehaviorSubscription[] terminate(Object obj) {
        BehaviorSubscription[] behaviorSubscriptionArr = (BehaviorSubscription[]) this.subscribers.get();
        BehaviorSubscription[] behaviorSubscriptionArr2 = TERMINATED;
        if (behaviorSubscriptionArr != behaviorSubscriptionArr2) {
            behaviorSubscriptionArr = (BehaviorSubscription[]) this.subscribers.getAndSet(behaviorSubscriptionArr2);
            if (behaviorSubscriptionArr != TERMINATED) {
                setCurrent(obj);
            }
        }
        return behaviorSubscriptionArr;
    }
}
