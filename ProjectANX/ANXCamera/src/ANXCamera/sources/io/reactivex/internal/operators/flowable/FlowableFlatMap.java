package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlatMap extends AbstractFlowableWithUpstream {
    final int bufferSize;
    final boolean delayErrors;
    final Function mapper;
    final int maxConcurrency;

    final class InnerSubscriber extends AtomicReference implements FlowableSubscriber, Disposable {
        private static final long serialVersionUID = -4606175640614850599L;
        final int bufferSize;
        volatile boolean done;
        int fusionMode;
        final long id;
        final int limit = (this.bufferSize >> 2);
        final MergeSubscriber parent;
        long produced;
        volatile SimpleQueue queue;

        InnerSubscriber(MergeSubscriber mergeSubscriber, long j) {
            this.id = j;
            this.parent = mergeSubscriber;
            this.bufferSize = mergeSubscriber.bufferSize;
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return get() == SubscriptionHelper.CANCELLED;
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void onError(Throwable th) {
            lazySet(SubscriptionHelper.CANCELLED);
            this.parent.innerError(this, th);
        }

        public void onNext(Object obj) {
            if (this.fusionMode != 2) {
                this.parent.tryEmit(obj, this);
            } else {
                this.parent.drain();
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(7);
                    if (requestFusion == 1) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                    }
                }
                subscription.request((long) this.bufferSize);
            }
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long j) {
            if (this.fusionMode != 1) {
                long j2 = this.produced + j;
                if (j2 >= ((long) this.limit)) {
                    this.produced = 0;
                    ((Subscription) get()).request(j2);
                    return;
                }
                this.produced = j2;
            }
        }
    }

    final class MergeSubscriber extends AtomicInteger implements FlowableSubscriber, Subscription {
        static final InnerSubscriber[] CANCELLED = new InnerSubscriber[0];
        static final InnerSubscriber[] EMPTY = new InnerSubscriber[0];
        private static final long serialVersionUID = -2117620485640801370L;
        final Subscriber actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errs = new AtomicThrowable();
        long lastId;
        int lastIndex;
        final Function mapper;
        final int maxConcurrency;
        volatile SimplePlainQueue queue;
        final AtomicLong requested = new AtomicLong();
        int scalarEmitted;
        final int scalarLimit;
        final AtomicReference subscribers = new AtomicReference();
        long uniqueId;
        Subscription upstream;

        MergeSubscriber(Subscriber subscriber, Function function, boolean z, int i, int i2) {
            this.actual = subscriber;
            this.mapper = function;
            this.delayErrors = z;
            this.maxConcurrency = i;
            this.bufferSize = i2;
            this.scalarLimit = Math.max(1, i >> 1);
            this.subscribers.lazySet(EMPTY);
        }

        /* access modifiers changed from: 0000 */
        public boolean addInner(InnerSubscriber innerSubscriber) {
            InnerSubscriber[] innerSubscriberArr;
            InnerSubscriber[] innerSubscriberArr2;
            do {
                innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                if (innerSubscriberArr == CANCELLED) {
                    innerSubscriber.dispose();
                    return false;
                }
                int length = innerSubscriberArr.length;
                innerSubscriberArr2 = new InnerSubscriber[(length + 1)];
                System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr2, 0, length);
                innerSubscriberArr2[length] = innerSubscriber;
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, innerSubscriberArr2));
            return true;
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.upstream.cancel();
                disposeAll();
                if (getAndIncrement() == 0) {
                    SimplePlainQueue simplePlainQueue = this.queue;
                    if (simplePlainQueue != null) {
                        simplePlainQueue.clear();
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminate() {
            if (this.cancelled) {
                clearScalarQueue();
                return true;
            } else if (this.delayErrors || this.errs.get() == null) {
                return false;
            } else {
                clearScalarQueue();
                Throwable terminate = this.errs.terminate();
                if (terminate != ExceptionHelper.TERMINATED) {
                    this.actual.onError(terminate);
                }
                return true;
            }
        }

        /* access modifiers changed from: 0000 */
        public void clearScalarQueue() {
            SimplePlainQueue simplePlainQueue = this.queue;
            if (simplePlainQueue != null) {
                simplePlainQueue.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeAll() {
            InnerSubscriber[] innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
            InnerSubscriber[] innerSubscriberArr2 = CANCELLED;
            if (innerSubscriberArr != innerSubscriberArr2) {
                InnerSubscriber[] innerSubscriberArr3 = (InnerSubscriber[]) this.subscribers.getAndSet(innerSubscriberArr2);
                if (innerSubscriberArr3 != CANCELLED) {
                    for (InnerSubscriber dispose : innerSubscriberArr3) {
                        dispose.dispose();
                    }
                    Throwable terminate = this.errs.terminate();
                    if (terminate != null && terminate != ExceptionHelper.TERMINATED) {
                        RxJavaPlugins.onError(terminate);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            long j;
            long j2;
            boolean z;
            long j3;
            long j4;
            boolean z2;
            long j5;
            int i;
            long j6;
            Subscriber subscriber = this.actual;
            int i2 = 1;
            while (!checkTerminate()) {
                SimplePlainQueue simplePlainQueue = this.queue;
                long j7 = this.requested.get();
                boolean z3 = j7 == Long.MAX_VALUE;
                long j8 = 0;
                long j9 = 0;
                if (simplePlainQueue != null) {
                    while (true) {
                        long j10 = 0;
                        Object obj = null;
                        while (true) {
                            if (j7 == 0) {
                                break;
                            }
                            Object poll = simplePlainQueue.poll();
                            if (!checkTerminate()) {
                                if (poll == null) {
                                    obj = poll;
                                    break;
                                }
                                subscriber.onNext(poll);
                                j9++;
                                j10++;
                                j7--;
                                obj = poll;
                            } else {
                                return;
                            }
                        }
                        if (j10 != 0) {
                            j7 = z3 ? Long.MAX_VALUE : this.requested.addAndGet(-j10);
                        }
                        if (j7 == 0 || obj == null) {
                            break;
                        }
                    }
                }
                boolean z4 = this.done;
                SimplePlainQueue simplePlainQueue2 = this.queue;
                InnerSubscriber[] innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                int length = innerSubscriberArr.length;
                if (!z4 || ((simplePlainQueue2 != null && !simplePlainQueue2.isEmpty()) || length != 0)) {
                    int i3 = i2;
                    if (length != 0) {
                        long j11 = this.lastId;
                        int i4 = this.lastIndex;
                        if (length <= i4 || innerSubscriberArr[i4].id != j11) {
                            if (length <= i4) {
                                i4 = 0;
                            }
                            int i5 = i4;
                            for (int i6 = 0; i6 < length && innerSubscriberArr[i5].id != j11; i6++) {
                                i5++;
                                if (i5 == length) {
                                    i5 = 0;
                                }
                            }
                            this.lastIndex = i5;
                            this.lastId = innerSubscriberArr[i5].id;
                            i4 = i5;
                        }
                        int i7 = i4;
                        boolean z5 = false;
                        int i8 = 0;
                        while (true) {
                            if (i8 >= length) {
                                z2 = z5;
                                break;
                            } else if (!checkTerminate()) {
                                InnerSubscriber innerSubscriber = innerSubscriberArr[i7];
                                Object obj2 = null;
                                while (!checkTerminate()) {
                                    SimpleQueue simpleQueue = innerSubscriber.queue;
                                    int i9 = length;
                                    if (simpleQueue != null) {
                                        Object obj3 = obj2;
                                        long j12 = j8;
                                        while (true) {
                                            if (j2 == j8) {
                                                break;
                                            }
                                            try {
                                                Object poll2 = simpleQueue.poll();
                                                if (poll2 == null) {
                                                    obj3 = poll2;
                                                    j8 = 0;
                                                    break;
                                                }
                                                subscriber.onNext(poll2);
                                                if (!checkTerminate()) {
                                                    j2--;
                                                    j12++;
                                                    obj3 = poll2;
                                                    j8 = 0;
                                                } else {
                                                    return;
                                                }
                                            } catch (Throwable th) {
                                                Throwable th2 = th;
                                                Exceptions.throwIfFatal(th2);
                                                innerSubscriber.dispose();
                                                this.errs.addThrowable(th2);
                                                if (!this.delayErrors) {
                                                    this.upstream.cancel();
                                                }
                                                if (!checkTerminate()) {
                                                    removeInner(innerSubscriber);
                                                    i8++;
                                                    z5 = true;
                                                    i = 1;
                                                } else {
                                                    return;
                                                }
                                            }
                                        }
                                        if (j12 != j8) {
                                            j2 = !z3 ? this.requested.addAndGet(-j12) : Long.MAX_VALUE;
                                            innerSubscriber.requestMore(j12);
                                            j6 = 0;
                                        } else {
                                            j6 = j8;
                                        }
                                        if (!(j2 == j6 || obj3 == null)) {
                                            length = i9;
                                            obj2 = obj3;
                                            j8 = 0;
                                        }
                                    }
                                    boolean z6 = innerSubscriber.done;
                                    SimpleQueue simpleQueue2 = innerSubscriber.queue;
                                    if (z6 && (simpleQueue2 == null || simpleQueue2.isEmpty())) {
                                        removeInner(innerSubscriber);
                                        if (!checkTerminate()) {
                                            j++;
                                            z5 = true;
                                        } else {
                                            return;
                                        }
                                    }
                                    if (j2 == 0) {
                                        z2 = z5;
                                        break;
                                    }
                                    i7++;
                                    if (i7 == i9) {
                                        i7 = 0;
                                    }
                                    i = 1;
                                    i8 += i;
                                    length = i9;
                                    j8 = 0;
                                }
                                return;
                            } else {
                                return;
                            }
                        }
                        this.lastIndex = i7;
                        this.lastId = innerSubscriberArr[i7].id;
                        z = z2;
                        j4 = j5;
                        j3 = 0;
                    } else {
                        j3 = 0;
                        j4 = j;
                        z = false;
                    }
                    if (j4 != j3 && !this.cancelled) {
                        this.upstream.request(j4);
                    }
                    if (z) {
                        i2 = i3;
                    } else {
                        i2 = addAndGet(-i3);
                        if (i2 == 0) {
                            return;
                        }
                    }
                } else {
                    Throwable terminate = this.errs.terminate();
                    if (terminate != ExceptionHelper.TERMINATED) {
                        if (terminate == null) {
                            subscriber.onComplete();
                        } else {
                            subscriber.onError(terminate);
                        }
                    }
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public SimpleQueue getInnerQueue(InnerSubscriber innerSubscriber) {
            SimpleQueue simpleQueue = innerSubscriber.queue;
            if (simpleQueue != null) {
                return simpleQueue;
            }
            SpscArrayQueue spscArrayQueue = new SpscArrayQueue(this.bufferSize);
            innerSubscriber.queue = spscArrayQueue;
            return spscArrayQueue;
        }

        /* access modifiers changed from: 0000 */
        public SimpleQueue getMainQueue() {
            SimplePlainQueue simplePlainQueue = this.queue;
            if (simplePlainQueue == null) {
                int i = this.maxConcurrency;
                simplePlainQueue = i == Integer.MAX_VALUE ? new SpscLinkedArrayQueue(this.bufferSize) : new SpscArrayQueue(i);
                this.queue = simplePlainQueue;
            }
            return simplePlainQueue;
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerSubscriber innerSubscriber, Throwable th) {
            if (this.errs.addThrowable(th)) {
                innerSubscriber.done = true;
                if (!this.delayErrors) {
                    this.upstream.cancel();
                    for (InnerSubscriber dispose : (InnerSubscriber[]) this.subscribers.getAndSet(CANCELLED)) {
                        dispose.dispose();
                    }
                }
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
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
            if (this.errs.addThrowable(th)) {
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
                    ObjectHelper.requireNonNull(apply, "The mapper returned a null Publisher");
                    Publisher publisher = (Publisher) apply;
                    if (publisher instanceof Callable) {
                        try {
                            Object call = ((Callable) publisher).call();
                            if (call != null) {
                                tryEmitScalar(call);
                            } else if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled) {
                                int i = this.scalarEmitted + 1;
                                this.scalarEmitted = i;
                                int i2 = this.scalarLimit;
                                if (i == i2) {
                                    this.scalarEmitted = 0;
                                    this.upstream.request((long) i2);
                                }
                            }
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            this.errs.addThrowable(th);
                            drain();
                        }
                    } else {
                        long j = this.uniqueId;
                        this.uniqueId = 1 + j;
                        InnerSubscriber innerSubscriber = new InnerSubscriber(this, j);
                        if (addInner(innerSubscriber)) {
                            publisher.subscribe(innerSubscriber);
                        }
                    }
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.upstream.cancel();
                    onError(th2);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.upstream, subscription)) {
                this.upstream = subscription;
                this.actual.onSubscribe(this);
                if (!this.cancelled) {
                    int i = this.maxConcurrency;
                    subscription.request(i == Integer.MAX_VALUE ? Long.MAX_VALUE : (long) i);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void removeInner(InnerSubscriber innerSubscriber) {
            InnerSubscriber[] innerSubscriberArr;
            InnerSubscriber[] innerSubscriberArr2;
            do {
                innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                if (innerSubscriberArr != CANCELLED && innerSubscriberArr != EMPTY) {
                    int length = innerSubscriberArr.length;
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerSubscriberArr[i2] == innerSubscriber) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            innerSubscriberArr2 = EMPTY;
                        } else {
                            InnerSubscriber[] innerSubscriberArr3 = new InnerSubscriber[(length - 1)];
                            System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr3, 0, i);
                            System.arraycopy(innerSubscriberArr, i + 1, innerSubscriberArr3, i, (length - i) - 1);
                            innerSubscriberArr2 = innerSubscriberArr3;
                        }
                    } else {
                        return;
                    }
                }
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, innerSubscriberArr2));
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(Object obj, InnerSubscriber innerSubscriber) {
            MissingBackpressureException missingBackpressureException;
            String str = "Inner queue full?!";
            if (get() != 0 || !compareAndSet(0, 1)) {
                SimpleQueue simpleQueue = innerSubscriber.queue;
                if (simpleQueue == null) {
                    simpleQueue = new SpscArrayQueue(this.bufferSize);
                    innerSubscriber.queue = simpleQueue;
                }
                if (!simpleQueue.offer(obj)) {
                    missingBackpressureException = new MissingBackpressureException(str);
                } else {
                    if (getAndIncrement() != 0) {
                        return;
                    }
                    drainLoop();
                    return;
                }
            } else {
                long j = this.requested.get();
                SimpleQueue simpleQueue2 = innerSubscriber.queue;
                if (j == 0 || (simpleQueue2 != null && !simpleQueue2.isEmpty())) {
                    if (simpleQueue2 == null) {
                        simpleQueue2 = getInnerQueue(innerSubscriber);
                    }
                    if (!simpleQueue2.offer(obj)) {
                        missingBackpressureException = new MissingBackpressureException(str);
                    }
                } else {
                    this.actual.onNext(obj);
                    if (j != Long.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    innerSubscriber.requestMore(1);
                }
                if (decrementAndGet() == 0) {
                    return;
                }
                drainLoop();
                return;
            }
            onError(missingBackpressureException);
        }

        /* access modifiers changed from: 0000 */
        public void tryEmitScalar(Object obj) {
            IllegalStateException illegalStateException;
            String str = "Scalar queue full?!";
            if (get() == 0 && compareAndSet(0, 1)) {
                long j = this.requested.get();
                SimpleQueue simpleQueue = this.queue;
                if (j == 0 || (simpleQueue != null && !simpleQueue.isEmpty())) {
                    if (simpleQueue == null) {
                        simpleQueue = getMainQueue();
                    }
                    if (!simpleQueue.offer(obj)) {
                        illegalStateException = new IllegalStateException(str);
                    }
                } else {
                    this.actual.onNext(obj);
                    if (j != Long.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled) {
                        int i = this.scalarEmitted + 1;
                        this.scalarEmitted = i;
                        int i2 = this.scalarLimit;
                        if (i == i2) {
                            this.scalarEmitted = 0;
                            this.upstream.request((long) i2);
                        }
                    }
                }
                if (decrementAndGet() == 0) {
                    return;
                }
                drainLoop();
                return;
            } else if (!getMainQueue().offer(obj)) {
                illegalStateException = new IllegalStateException(str);
            } else {
                if (getAndIncrement() != 0) {
                    return;
                }
                drainLoop();
                return;
            }
            onError(illegalStateException);
        }
    }

    public FlowableFlatMap(Flowable flowable, Function function, boolean z, int i, int i2) {
        super(flowable);
        this.mapper = function;
        this.delayErrors = z;
        this.maxConcurrency = i;
        this.bufferSize = i2;
    }

    public static FlowableSubscriber subscribe(Subscriber subscriber, Function function, boolean z, int i, int i2) {
        MergeSubscriber mergeSubscriber = new MergeSubscriber(subscriber, function, z, i, i2);
        return mergeSubscriber;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, subscriber, this.mapper)) {
            this.source.subscribe(subscribe(subscriber, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize));
        }
    }
}
