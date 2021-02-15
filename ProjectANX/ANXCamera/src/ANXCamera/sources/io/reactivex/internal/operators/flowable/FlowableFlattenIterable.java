package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlattenIterable extends AbstractFlowableWithUpstream {
    final Function mapper;
    final int prefetch;

    final class FlattenIterableSubscriber extends BasicIntQueueSubscription implements FlowableSubscriber {
        private static final long serialVersionUID = -3096000382929934955L;
        final Subscriber actual;
        volatile boolean cancelled;
        int consumed;
        Iterator current;
        volatile boolean done;
        final AtomicReference error = new AtomicReference();
        int fusionMode;
        final int limit;
        final Function mapper;
        final int prefetch;
        SimpleQueue queue;
        final AtomicLong requested = new AtomicLong();
        Subscription s;

        FlattenIterableSubscriber(Subscriber subscriber, Function function, int i) {
            this.actual = subscriber;
            this.mapper = function;
            this.prefetch = i;
            this.limit = i - (i >> 2);
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2, Subscriber subscriber, SimpleQueue simpleQueue) {
            if (this.cancelled) {
                this.current = null;
                simpleQueue.clear();
                return true;
            }
            if (z) {
                if (((Throwable) this.error.get()) != null) {
                    Throwable terminate = ExceptionHelper.terminate(this.error);
                    this.current = null;
                    simpleQueue.clear();
                    subscriber.onError(terminate);
                    return true;
                } else if (z2) {
                    subscriber.onComplete();
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            this.current = null;
            this.queue.clear();
        }

        /* access modifiers changed from: 0000 */
        public void consumedOne(boolean z) {
            if (z) {
                int i = this.consumed + 1;
                if (i == this.limit) {
                    this.consumed = 0;
                    this.s.request((long) i);
                    return;
                }
                this.consumed = i;
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x0102, code lost:
            if (r6 == null) goto L_0x010d;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            Throwable th;
            if (getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                SimpleQueue simpleQueue = this.queue;
                boolean z = true;
                boolean z2 = this.fusionMode != 1;
                Iterator it = this.current;
                int i = 1;
                while (true) {
                    if (it == null) {
                        boolean z3 = this.done;
                        try {
                            Object poll = simpleQueue.poll();
                            if (!checkTerminated(z3, poll == null ? z : false, subscriber, simpleQueue)) {
                                if (poll != null) {
                                    try {
                                        it = ((Iterable) this.mapper.apply(poll)).iterator();
                                        if (!it.hasNext()) {
                                            consumedOne(z2);
                                            it = null;
                                        } else {
                                            this.current = it;
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        Exceptions.throwIfFatal(th);
                                        this.s.cancel();
                                        ExceptionHelper.addThrowable(this.error, th);
                                        th = ExceptionHelper.terminate(this.error);
                                        subscriber.onError(th);
                                        return;
                                    }
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable th3) {
                            Throwable th4 = th3;
                            Exceptions.throwIfFatal(th4);
                            this.s.cancel();
                            ExceptionHelper.addThrowable(this.error, th4);
                            th = ExceptionHelper.terminate(this.error);
                            this.current = null;
                            simpleQueue.clear();
                            subscriber.onError(th);
                            return;
                        }
                    }
                    if (it != null) {
                        long j = this.requested.get();
                        long j2 = 0;
                        while (true) {
                            if (j2 == j) {
                                break;
                            } else if (!checkTerminated(this.done, false, subscriber, simpleQueue)) {
                                try {
                                    Object next = it.next();
                                    ObjectHelper.requireNonNull(next, "The iterator returned a null value");
                                    subscriber.onNext(next);
                                    if (!checkTerminated(this.done, false, subscriber, simpleQueue)) {
                                        j2++;
                                        try {
                                            if (!it.hasNext()) {
                                                consumedOne(z2);
                                                this.current = null;
                                                it = null;
                                                break;
                                            }
                                        } catch (Throwable th5) {
                                            Throwable th6 = th5;
                                            Exceptions.throwIfFatal(th6);
                                            this.current = null;
                                            this.s.cancel();
                                            ExceptionHelper.addThrowable(this.error, th6);
                                            th = ExceptionHelper.terminate(this.error);
                                            subscriber.onError(th);
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                } catch (Throwable th7) {
                                    th = th7;
                                    Exceptions.throwIfFatal(th);
                                    this.current = null;
                                    this.s.cancel();
                                    ExceptionHelper.addThrowable(this.error, th);
                                    th = ExceptionHelper.terminate(this.error);
                                    subscriber.onError(th);
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                        if (j2 == j) {
                            boolean z4 = this.done;
                            boolean z5 = simpleQueue.isEmpty() && it == null;
                            if (checkTerminated(z4, z5, subscriber, simpleQueue)) {
                                return;
                            }
                        }
                        if (!(j2 == 0 || j == Long.MAX_VALUE)) {
                            this.requested.addAndGet(-j2);
                        }
                    }
                    i = addAndGet(-i);
                    if (i == 0) {
                        return;
                    }
                    z = true;
                }
            }
        }

        public boolean isEmpty() {
            Iterator it = this.current;
            return it == null ? this.queue.isEmpty() : !it.hasNext();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void onError(Throwable th) {
            if (this.done || !ExceptionHelper.addThrowable(this.error, th)) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            drain();
        }

        public void onNext(Object obj) {
            if (!this.done) {
                if (this.fusionMode != 0 || this.queue.offer(obj)) {
                    drain();
                } else {
                    onError(new MissingBackpressureException("Queue is full?!"));
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        return;
                    } else if (requestFusion == 2) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        this.actual.onSubscribe(this);
                        subscription.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                this.actual.onSubscribe(this);
                subscription.request((long) this.prefetch);
            }
        }

        @Nullable
        public Object poll() {
            Iterator it = this.current;
            while (true) {
                if (it != null) {
                    break;
                }
                Object poll = this.queue.poll();
                if (poll != null) {
                    it = ((Iterable) this.mapper.apply(poll)).iterator();
                    if (it.hasNext()) {
                        this.current = it;
                        break;
                    }
                    it = null;
                } else {
                    return null;
                }
            }
            Object next = it.next();
            ObjectHelper.requireNonNull(next, "The iterator returned a null value");
            if (!it.hasNext()) {
                this.current = null;
            }
            return next;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public int requestFusion(int i) {
            return ((i & 1) == 0 || this.fusionMode != 1) ? 0 : 1;
        }
    }

    public FlowableFlattenIterable(Flowable flowable, Function function, int i) {
        super(flowable);
        this.mapper = function;
        this.prefetch = i;
    }

    public void subscribeActual(Subscriber subscriber) {
        Flowable flowable = this.source;
        if (flowable instanceof Callable) {
            try {
                Object call = ((Callable) flowable).call();
                if (call == null) {
                    EmptySubscription.complete(subscriber);
                    return;
                }
                try {
                    FlowableFromIterable.subscribe(subscriber, ((Iterable) this.mapper.apply(call)).iterator());
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    EmptySubscription.error(th, subscriber);
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                EmptySubscription.error(th2, subscriber);
            }
        } else {
            flowable.subscribe((FlowableSubscriber) new FlattenIterableSubscriber(subscriber, this.mapper, this.prefetch));
        }
    }
}
