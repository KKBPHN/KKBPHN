package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSequenceEqual extends Flowable {
    final BiPredicate comparer;
    final Publisher first;
    final int prefetch;
    final Publisher second;

    final class EqualCoordinator extends DeferredScalarSubscription implements EqualCoordinatorHelper {
        private static final long serialVersionUID = -6178010334400373240L;
        final BiPredicate comparer;
        final AtomicThrowable error;
        final EqualSubscriber first;
        final EqualSubscriber second;
        Object v1;
        Object v2;
        final AtomicInteger wip = new AtomicInteger();

        EqualCoordinator(Subscriber subscriber, int i, BiPredicate biPredicate) {
            super(subscriber);
            this.comparer = biPredicate;
            this.first = new EqualSubscriber(this, i);
            this.second = new EqualSubscriber(this, i);
            this.error = new AtomicThrowable();
        }

        public void cancel() {
            super.cancel();
            this.first.cancel();
            this.second.cancel();
            if (this.wip.getAndIncrement() == 0) {
                this.first.clear();
                this.second.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAndClear() {
            this.first.cancel();
            this.first.clear();
            this.second.cancel();
            this.second.clear();
        }

        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                int i = 1;
                do {
                    SimpleQueue simpleQueue = this.first.queue;
                    SimpleQueue simpleQueue2 = this.second.queue;
                    if (simpleQueue != null && simpleQueue2 != null) {
                        while (!isCancelled()) {
                            if (((Throwable) this.error.get()) != null) {
                                cancelAndClear();
                                this.actual.onError(this.error.terminate());
                                return;
                            }
                            boolean z = this.first.done;
                            Object obj = this.v1;
                            if (obj == null) {
                                try {
                                    obj = simpleQueue.poll();
                                    this.v1 = obj;
                                } catch (Throwable th) {
                                    Exceptions.throwIfFatal(th);
                                    cancelAndClear();
                                    this.error.addThrowable(th);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean z2 = obj == null;
                            boolean z3 = this.second.done;
                            Object obj2 = this.v2;
                            if (obj2 == null) {
                                try {
                                    obj2 = simpleQueue2.poll();
                                    this.v2 = obj2;
                                } catch (Throwable th2) {
                                    Exceptions.throwIfFatal(th2);
                                    cancelAndClear();
                                    this.error.addThrowable(th2);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean z4 = obj2 == null;
                            if (z && z3 && z2 && z4) {
                                complete(Boolean.valueOf(true));
                                return;
                            } else if (z && z3 && z2 != z4) {
                                cancelAndClear();
                                complete(Boolean.valueOf(false));
                                return;
                            } else if (!z2 && !z4) {
                                try {
                                    if (!this.comparer.test(obj, obj2)) {
                                        cancelAndClear();
                                        complete(Boolean.valueOf(false));
                                        return;
                                    }
                                    this.v1 = null;
                                    this.v2 = null;
                                    this.first.request();
                                    this.second.request();
                                } catch (Throwable th3) {
                                    Exceptions.throwIfFatal(th3);
                                    cancelAndClear();
                                    this.error.addThrowable(th3);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                        }
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (isCancelled()) {
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (((Throwable) this.error.get()) != null) {
                        cancelAndClear();
                        this.actual.onError(this.error.terminate());
                        return;
                    }
                    i = this.wip.addAndGet(-i);
                } while (i != 0);
            }
        }

        public void innerError(Throwable th) {
            if (this.error.addThrowable(th)) {
                drain();
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(Publisher publisher, Publisher publisher2) {
            publisher.subscribe(this.first);
            publisher2.subscribe(this.second);
        }
    }

    interface EqualCoordinatorHelper {
        void drain();

        void innerError(Throwable th);
    }

    final class EqualSubscriber extends AtomicReference implements FlowableSubscriber {
        private static final long serialVersionUID = 4804128302091633067L;
        volatile boolean done;
        final int limit;
        final EqualCoordinatorHelper parent;
        final int prefetch;
        long produced;
        volatile SimpleQueue queue;
        int sourceMode;

        EqualSubscriber(EqualCoordinatorHelper equalCoordinatorHelper, int i) {
            this.parent = equalCoordinatorHelper;
            this.limit = i - (i >> 2);
            this.prefetch = i;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            SimpleQueue simpleQueue = this.queue;
            if (simpleQueue != null) {
                simpleQueue.clear();
            }
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void onError(Throwable th) {
            this.parent.innerError(th);
        }

        public void onNext(Object obj) {
            if (this.sourceMode != 0 || this.queue.offer(obj)) {
                this.parent.drain();
            } else {
                onError(new MissingBackpressureException());
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        subscription.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                subscription.request((long) this.prefetch);
            }
        }

        public void request() {
            if (this.sourceMode != 1) {
                long j = this.produced + 1;
                if (j >= ((long) this.limit)) {
                    this.produced = 0;
                    ((Subscription) get()).request(j);
                    return;
                }
                this.produced = j;
            }
        }
    }

    public FlowableSequenceEqual(Publisher publisher, Publisher publisher2, BiPredicate biPredicate, int i) {
        this.first = publisher;
        this.second = publisher2;
        this.comparer = biPredicate;
        this.prefetch = i;
    }

    public void subscribeActual(Subscriber subscriber) {
        EqualCoordinator equalCoordinator = new EqualCoordinator(subscriber, this.prefetch, this.comparer);
        subscriber.onSubscribe(equalCoordinator);
        equalCoordinator.subscribe(this.first, this.second);
    }
}
