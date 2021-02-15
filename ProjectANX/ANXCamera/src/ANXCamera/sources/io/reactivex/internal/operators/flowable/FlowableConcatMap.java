package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableConcatMap extends AbstractFlowableWithUpstream {
    final ErrorMode errorMode;
    final Function mapper;
    final int prefetch;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatMap$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$reactivex$internal$util$ErrorMode = new int[ErrorMode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$io$reactivex$internal$util$ErrorMode[ErrorMode.BOUNDARY.ordinal()] = 1;
            $SwitchMap$io$reactivex$internal$util$ErrorMode[ErrorMode.END.ordinal()] = 2;
        }
    }

    abstract class BaseConcatMapSubscriber extends AtomicInteger implements FlowableSubscriber, ConcatMapSupport, Subscription {
        private static final long serialVersionUID = -3511336836796789179L;
        volatile boolean active;
        volatile boolean cancelled;
        int consumed;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        final ConcatMapInner inner = new ConcatMapInner(this);
        final int limit;
        final Function mapper;
        final int prefetch;
        SimpleQueue queue;
        Subscription s;
        int sourceMode;

        BaseConcatMapSubscriber(Function function, int i) {
            this.mapper = function;
            this.prefetch = i;
            this.limit = i - (i >> 2);
        }

        public abstract void drain();

        public final void innerComplete() {
            this.active = false;
            drain();
        }

        public final void onComplete() {
            this.done = true;
            drain();
        }

        public final void onNext(Object obj) {
            if (this.sourceMode == 2 || this.queue.offer(obj)) {
                drain();
                return;
            }
            this.s.cancel();
            onError(new IllegalStateException("Queue full?!"));
        }

        public final void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        subscribeActual();
                        drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        subscribeActual();
                        subscription.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                subscribeActual();
                subscription.request((long) this.prefetch);
            }
        }

        public abstract void subscribeActual();
    }

    final class ConcatMapDelayed extends BaseConcatMapSubscriber {
        private static final long serialVersionUID = -2945777694260521066L;
        final Subscriber actual;
        final boolean veryEnd;

        ConcatMapDelayed(Subscriber subscriber, Function function, int i, boolean z) {
            super(function, i);
            this.actual = subscriber;
            this.veryEnd = z;
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.inner.cancel();
                this.s.cancel();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                while (!this.cancelled) {
                    if (!this.active) {
                        boolean z = this.done;
                        if (!z || this.veryEnd || ((Throwable) this.errors.get()) == null) {
                            try {
                                Object poll = this.queue.poll();
                                boolean z2 = poll == null;
                                if (z && z2) {
                                    Throwable terminate = this.errors.terminate();
                                    Subscriber subscriber = this.actual;
                                    if (terminate != null) {
                                        subscriber.onError(terminate);
                                    } else {
                                        subscriber.onComplete();
                                    }
                                    return;
                                } else if (!z2) {
                                    Object apply = this.mapper.apply(poll);
                                    ObjectHelper.requireNonNull(apply, "The mapper returned a null Publisher");
                                    Publisher publisher = (Publisher) apply;
                                    if (this.sourceMode != 1) {
                                        int i = this.consumed + 1;
                                        if (i == this.limit) {
                                            this.consumed = 0;
                                            this.s.request((long) i);
                                        } else {
                                            this.consumed = i;
                                        }
                                    }
                                    if (publisher instanceof Callable) {
                                        Object call = ((Callable) publisher).call();
                                        if (call != null) {
                                            if (this.inner.isUnbounded()) {
                                                this.actual.onNext(call);
                                            } else {
                                                this.active = true;
                                                ConcatMapInner concatMapInner = this.inner;
                                                concatMapInner.setSubscription(new WeakScalarSubscription(call, concatMapInner));
                                            }
                                        }
                                    } else {
                                        this.active = true;
                                        publisher.subscribe(this.inner);
                                    }
                                }
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                this.s.cancel();
                                this.errors.addThrowable(th);
                            }
                        } else {
                            this.actual.onError(this.errors.terminate());
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                    }
                }
            }
        }

        public void innerError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                if (!this.veryEnd) {
                    this.s.cancel();
                    this.done = true;
                }
                this.active = false;
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void innerNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void request(long j) {
            this.inner.request(j);
        }

        /* access modifiers changed from: 0000 */
        public void subscribeActual() {
            this.actual.onSubscribe(this);
        }
    }

    final class ConcatMapImmediate extends BaseConcatMapSubscriber {
        private static final long serialVersionUID = 7898995095634264146L;
        final Subscriber actual;
        final AtomicInteger wip = new AtomicInteger();

        ConcatMapImmediate(Subscriber subscriber, Function function, int i) {
            super(function, i);
            this.actual = subscriber;
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.inner.cancel();
                this.s.cancel();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                while (!this.cancelled) {
                    if (!this.active) {
                        boolean z = this.done;
                        try {
                            Object poll = this.queue.poll();
                            boolean z2 = poll == null;
                            if (z && z2) {
                                this.actual.onComplete();
                                return;
                            } else if (!z2) {
                                try {
                                    Object apply = this.mapper.apply(poll);
                                    ObjectHelper.requireNonNull(apply, "The mapper returned a null Publisher");
                                    Publisher publisher = (Publisher) apply;
                                    if (this.sourceMode != 1) {
                                        int i = this.consumed + 1;
                                        if (i == this.limit) {
                                            this.consumed = 0;
                                            this.s.request((long) i);
                                        } else {
                                            this.consumed = i;
                                        }
                                    }
                                    if (publisher instanceof Callable) {
                                        try {
                                            Object call = ((Callable) publisher).call();
                                            if (call != null) {
                                                if (!this.inner.isUnbounded()) {
                                                    this.active = true;
                                                    ConcatMapInner concatMapInner = this.inner;
                                                    concatMapInner.setSubscription(new WeakScalarSubscription(call, concatMapInner));
                                                } else if (get() == 0 && compareAndSet(0, 1)) {
                                                    this.actual.onNext(call);
                                                    if (!compareAndSet(1, 0)) {
                                                        this.actual.onError(this.errors.terminate());
                                                        return;
                                                    }
                                                }
                                            }
                                        } catch (Throwable th) {
                                            Exceptions.throwIfFatal(th);
                                            this.s.cancel();
                                            this.errors.addThrowable(th);
                                            this.actual.onError(this.errors.terminate());
                                            return;
                                        }
                                    } else {
                                        this.active = true;
                                        publisher.subscribe(this.inner);
                                    }
                                } catch (Throwable th2) {
                                    Exceptions.throwIfFatal(th2);
                                    this.s.cancel();
                                    this.errors.addThrowable(th2);
                                    this.actual.onError(this.errors.terminate());
                                    return;
                                }
                            }
                        } catch (Throwable th3) {
                            Exceptions.throwIfFatal(th3);
                            this.s.cancel();
                            this.errors.addThrowable(th3);
                            this.actual.onError(this.errors.terminate());
                            return;
                        }
                    }
                    if (this.wip.decrementAndGet() == 0) {
                    }
                }
            }
        }

        public void innerError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                this.s.cancel();
                if (getAndIncrement() == 0) {
                    this.actual.onError(this.errors.terminate());
                    return;
                }
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void innerNext(Object obj) {
            if (get() == 0 && compareAndSet(0, 1)) {
                this.actual.onNext(obj);
                if (!compareAndSet(1, 0)) {
                    this.actual.onError(this.errors.terminate());
                }
            }
        }

        public void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                this.inner.cancel();
                if (getAndIncrement() == 0) {
                    this.actual.onError(this.errors.terminate());
                    return;
                }
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void request(long j) {
            this.inner.request(j);
        }

        /* access modifiers changed from: 0000 */
        public void subscribeActual() {
            this.actual.onSubscribe(this);
        }
    }

    final class ConcatMapInner extends SubscriptionArbiter implements FlowableSubscriber {
        private static final long serialVersionUID = 897683679971470653L;
        final ConcatMapSupport parent;
        long produced;

        ConcatMapInner(ConcatMapSupport concatMapSupport) {
            this.parent = concatMapSupport;
        }

        public void onComplete() {
            long j = this.produced;
            if (j != 0) {
                this.produced = 0;
                produced(j);
            }
            this.parent.innerComplete();
        }

        public void onError(Throwable th) {
            long j = this.produced;
            if (j != 0) {
                this.produced = 0;
                produced(j);
            }
            this.parent.innerError(th);
        }

        public void onNext(Object obj) {
            this.produced++;
            this.parent.innerNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            setSubscription(subscription);
        }
    }

    interface ConcatMapSupport {
        void innerComplete();

        void innerError(Throwable th);

        void innerNext(Object obj);
    }

    final class WeakScalarSubscription implements Subscription {
        final Subscriber actual;
        boolean once;
        final Object value;

        WeakScalarSubscription(Object obj, Subscriber subscriber) {
            this.value = obj;
            this.actual = subscriber;
        }

        public void cancel() {
        }

        public void request(long j) {
            if (j > 0 && !this.once) {
                this.once = true;
                Subscriber subscriber = this.actual;
                subscriber.onNext(this.value);
                subscriber.onComplete();
            }
        }
    }

    public FlowableConcatMap(Flowable flowable, Function function, int i, ErrorMode errorMode2) {
        super(flowable);
        this.mapper = function;
        this.prefetch = i;
        this.errorMode = errorMode2;
    }

    public static Subscriber subscribe(Subscriber subscriber, Function function, int i, ErrorMode errorMode2) {
        int i2 = AnonymousClass1.$SwitchMap$io$reactivex$internal$util$ErrorMode[errorMode2.ordinal()];
        return i2 != 1 ? i2 != 2 ? new ConcatMapImmediate(subscriber, function, i) : new ConcatMapDelayed(subscriber, function, i, true) : new ConcatMapDelayed(subscriber, function, i, false);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, subscriber, this.mapper)) {
            this.source.subscribe(subscribe(subscriber, this.mapper, this.prefetch, this.errorMode));
        }
    }
}
