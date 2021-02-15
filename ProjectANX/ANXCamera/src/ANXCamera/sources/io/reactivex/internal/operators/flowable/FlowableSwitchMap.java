package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSwitchMap extends AbstractFlowableWithUpstream {
    final int bufferSize;
    final boolean delayErrors;
    final Function mapper;

    final class SwitchMapInnerSubscriber extends AtomicReference implements FlowableSubscriber {
        private static final long serialVersionUID = 3837284832786408377L;
        final int bufferSize;
        volatile boolean done;
        int fusionMode;
        final long index;
        final SwitchMapSubscriber parent;
        volatile SimpleQueue queue;

        SwitchMapInnerSubscriber(SwitchMapSubscriber switchMapSubscriber, long j, int i) {
            this.parent = switchMapSubscriber;
            this.index = j;
            this.bufferSize = i;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }

        public void onComplete() {
            SwitchMapSubscriber switchMapSubscriber = this.parent;
            if (this.index == switchMapSubscriber.unique) {
                this.done = true;
                switchMapSubscriber.drain();
            }
        }

        public void onError(Throwable th) {
            SwitchMapSubscriber switchMapSubscriber = this.parent;
            if (this.index != switchMapSubscriber.unique || !switchMapSubscriber.error.addThrowable(th)) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (!switchMapSubscriber.delayErrors) {
                switchMapSubscriber.s.cancel();
            }
            this.done = true;
            switchMapSubscriber.drain();
        }

        public void onNext(Object obj) {
            SwitchMapSubscriber switchMapSubscriber = this.parent;
            if (this.index == switchMapSubscriber.unique) {
                if (this.fusionMode != 0 || this.queue.offer(obj)) {
                    switchMapSubscriber.drain();
                } else {
                    onError(new MissingBackpressureException("Queue full?!"));
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.fusionMode = requestFusion;
                        this.queue = queueSubscription;
                        subscription.request((long) this.bufferSize);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.bufferSize);
                subscription.request((long) this.bufferSize);
            }
        }
    }

    final class SwitchMapSubscriber extends AtomicInteger implements FlowableSubscriber, Subscription {
        static final SwitchMapInnerSubscriber CANCELLED = new SwitchMapInnerSubscriber(null, -1, 1);
        private static final long serialVersionUID = -3491074160481096299L;
        final AtomicReference active = new AtomicReference();
        final Subscriber actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable error;
        final Function mapper;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        volatile long unique;

        static {
            CANCELLED.cancel();
        }

        SwitchMapSubscriber(Subscriber subscriber, Function function, int i, boolean z) {
            this.actual = subscriber;
            this.mapper = function;
            this.bufferSize = i;
            this.delayErrors = z;
            this.error = new AtomicThrowable();
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.s.cancel();
                disposeInner();
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeInner() {
            SwitchMapInnerSubscriber switchMapInnerSubscriber = (SwitchMapInnerSubscriber) this.active.get();
            SwitchMapInnerSubscriber switchMapInnerSubscriber2 = CANCELLED;
            if (switchMapInnerSubscriber != switchMapInnerSubscriber2) {
                SwitchMapInnerSubscriber switchMapInnerSubscriber3 = (SwitchMapInnerSubscriber) this.active.getAndSet(switchMapInnerSubscriber2);
                if (switchMapInnerSubscriber3 != CANCELLED && switchMapInnerSubscriber3 != null) {
                    switchMapInnerSubscriber3.cancel();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0097, code lost:
            if (r7.isEmpty() != false) goto L_0x00a0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x009e, code lost:
            if (r7.isEmpty() != false) goto L_0x00a0;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            boolean z;
            Object obj;
            if (getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                int i = 1;
                while (!this.cancelled) {
                    if (this.done) {
                        if (this.delayErrors) {
                            if (this.active.get() == null) {
                                if (((Throwable) this.error.get()) != null) {
                                    subscriber.onError(this.error.terminate());
                                } else {
                                    subscriber.onComplete();
                                }
                                return;
                            }
                        } else if (((Throwable) this.error.get()) != null) {
                            disposeInner();
                            subscriber.onError(this.error.terminate());
                            return;
                        } else if (this.active.get() == null) {
                            subscriber.onComplete();
                            return;
                        }
                    }
                    SwitchMapInnerSubscriber switchMapInnerSubscriber = (SwitchMapInnerSubscriber) this.active.get();
                    SimpleQueue simpleQueue = switchMapInnerSubscriber != null ? switchMapInnerSubscriber.queue : null;
                    if (simpleQueue != null) {
                        if (switchMapInnerSubscriber.done) {
                            if (!this.delayErrors) {
                                if (((Throwable) this.error.get()) != null) {
                                    disposeInner();
                                    subscriber.onError(this.error.terminate());
                                    return;
                                }
                            }
                            this.active.compareAndSet(switchMapInnerSubscriber, null);
                        }
                        long j = this.requested.get();
                        long j2 = 0;
                        while (true) {
                            z = false;
                            if (j2 != j) {
                                if (!this.cancelled) {
                                    boolean z2 = switchMapInnerSubscriber.done;
                                    try {
                                        obj = simpleQueue.poll();
                                    } catch (Throwable th) {
                                        Throwable th2 = th;
                                        Exceptions.throwIfFatal(th2);
                                        switchMapInnerSubscriber.cancel();
                                        this.error.addThrowable(th2);
                                        obj = null;
                                        z2 = true;
                                    }
                                    boolean z3 = obj == null;
                                    if (switchMapInnerSubscriber != this.active.get()) {
                                        break;
                                    }
                                    if (z2) {
                                        if (this.delayErrors) {
                                            if (z3) {
                                                break;
                                            }
                                        } else if (((Throwable) this.error.get()) == null) {
                                            if (z3) {
                                                break;
                                            }
                                        } else {
                                            subscriber.onError(this.error.terminate());
                                            return;
                                        }
                                    }
                                    if (z3) {
                                        break;
                                    }
                                    subscriber.onNext(obj);
                                    j2++;
                                } else {
                                    return;
                                }
                            } else {
                                break;
                            }
                        }
                        this.active.compareAndSet(switchMapInnerSubscriber, null);
                        z = true;
                        if (j2 != 0 && !this.cancelled) {
                            if (j != Long.MAX_VALUE) {
                                this.requested.addAndGet(-j2);
                            }
                            ((Subscription) switchMapInnerSubscriber.get()).request(j2);
                        }
                        if (z) {
                        }
                    }
                    i = addAndGet(-i);
                    if (i == 0) {
                        return;
                    }
                }
                this.active.lazySet(null);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void onError(Throwable th) {
            if (this.done || !this.error.addThrowable(th)) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (!this.delayErrors) {
                disposeInner();
            }
            this.done = true;
            drain();
        }

        public void onNext(Object obj) {
            if (!this.done) {
                long j = this.unique + 1;
                this.unique = j;
                SwitchMapInnerSubscriber switchMapInnerSubscriber = (SwitchMapInnerSubscriber) this.active.get();
                if (switchMapInnerSubscriber != null) {
                    switchMapInnerSubscriber.cancel();
                }
                try {
                    Object apply = this.mapper.apply(obj);
                    ObjectHelper.requireNonNull(apply, "The publisher returned is null");
                    Publisher publisher = (Publisher) apply;
                    SwitchMapInnerSubscriber switchMapInnerSubscriber2 = new SwitchMapInnerSubscriber(this, j, this.bufferSize);
                    while (true) {
                        SwitchMapInnerSubscriber switchMapInnerSubscriber3 = (SwitchMapInnerSubscriber) this.active.get();
                        if (switchMapInnerSubscriber3 != CANCELLED) {
                            if (this.active.compareAndSet(switchMapInnerSubscriber3, switchMapInnerSubscriber2)) {
                                publisher.subscribe(switchMapInnerSubscriber2);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.s.cancel();
                    onError(th);
                }
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                if (this.unique == 0) {
                    this.s.request(Long.MAX_VALUE);
                } else {
                    drain();
                }
            }
        }
    }

    public FlowableSwitchMap(Flowable flowable, Function function, int i, boolean z) {
        super(flowable);
        this.mapper = function;
        this.bufferSize = i;
        this.delayErrors = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, subscriber, this.mapper)) {
            this.source.subscribe((FlowableSubscriber) new SwitchMapSubscriber(subscriber, this.mapper, this.bufferSize, this.delayErrors));
        }
    }
}
