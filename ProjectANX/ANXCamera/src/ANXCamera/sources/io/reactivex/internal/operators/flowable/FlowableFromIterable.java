package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.BasicQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.Iterator;
import org.reactivestreams.Subscriber;

public final class FlowableFromIterable extends Flowable {
    final Iterable source;

    abstract class BaseRangeSubscription extends BasicQueueSubscription {
        private static final long serialVersionUID = -2252972430506210021L;
        volatile boolean cancelled;
        Iterator it;
        boolean once;

        BaseRangeSubscription(Iterator it2) {
            this.it = it2;
        }

        public final void cancel() {
            this.cancelled = true;
        }

        public final void clear() {
            this.it = null;
        }

        public abstract void fastPath();

        public final boolean isEmpty() {
            Iterator it2 = this.it;
            return it2 == null || !it2.hasNext();
        }

        @Nullable
        public final Object poll() {
            Iterator it2 = this.it;
            if (it2 == null) {
                return null;
            }
            if (!this.once) {
                this.once = true;
            } else if (!it2.hasNext()) {
                return null;
            }
            Object next = this.it.next();
            ObjectHelper.requireNonNull(next, "Iterator.next() returned a null value");
            return next;
        }

        public final void request(long j) {
            if (SubscriptionHelper.validate(j) && BackpressureHelper.add(this, j) == 0) {
                if (j == Long.MAX_VALUE) {
                    fastPath();
                } else {
                    slowPath(j);
                }
            }
        }

        public final int requestFusion(int i) {
            return i & 1;
        }

        public abstract void slowPath(long j);
    }

    final class IteratorConditionalSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = -6022804456014692607L;
        final ConditionalSubscriber actual;

        IteratorConditionalSubscription(ConditionalSubscriber conditionalSubscriber, Iterator it) {
            super(it);
            this.actual = conditionalSubscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Iterator it = this.it;
            ConditionalSubscriber conditionalSubscriber = this.actual;
            while (!this.cancelled) {
                try {
                    Object next = it.next();
                    if (!this.cancelled) {
                        if (next == null) {
                            th = new NullPointerException("Iterator.next() returned a null value");
                            conditionalSubscriber.onError(th);
                            return;
                        }
                        conditionalSubscriber.tryOnNext(next);
                        if (!this.cancelled) {
                            if (!it.hasNext()) {
                                if (!this.cancelled) {
                                    conditionalSubscriber.onComplete();
                                }
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th) {
                    th = th;
                    Exceptions.throwIfFatal(th);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Iterator it = this.it;
            ConditionalSubscriber conditionalSubscriber = this.actual;
            long j2 = j;
            loop0:
            do {
                long j3 = 0;
                while (true) {
                    if (j3 == j2) {
                        j2 = get();
                        if (j3 == j2) {
                            j2 = addAndGet(-j3);
                        }
                    } else if (!this.cancelled) {
                        try {
                            Object next = it.next();
                            if (!this.cancelled) {
                                if (next == null) {
                                    th = new NullPointerException("Iterator.next() returned a null value");
                                    break loop0;
                                }
                                boolean tryOnNext = conditionalSubscriber.tryOnNext(next);
                                if (!this.cancelled) {
                                    if (!it.hasNext()) {
                                        if (!this.cancelled) {
                                            conditionalSubscriber.onComplete();
                                        }
                                        return;
                                    } else if (tryOnNext) {
                                        j3++;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable th) {
                            th = th;
                            Exceptions.throwIfFatal(th);
                        }
                    } else {
                        return;
                    }
                }
                conditionalSubscriber.onError(th);
                return;
            } while (j2 != 0);
        }
    }

    final class IteratorSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = -6022804456014692607L;
        final Subscriber actual;

        IteratorSubscription(Subscriber subscriber, Iterator it) {
            super(it);
            this.actual = subscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Iterator it = this.it;
            Subscriber subscriber = this.actual;
            while (!this.cancelled) {
                try {
                    Object next = it.next();
                    if (!this.cancelled) {
                        if (next == null) {
                            th = new NullPointerException("Iterator.next() returned a null value");
                            subscriber.onError(th);
                            return;
                        }
                        subscriber.onNext(next);
                        if (!this.cancelled) {
                            if (!it.hasNext()) {
                                if (!this.cancelled) {
                                    subscriber.onComplete();
                                }
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th) {
                    th = th;
                    Exceptions.throwIfFatal(th);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Iterator it = this.it;
            Subscriber subscriber = this.actual;
            long j2 = j;
            loop0:
            do {
                long j3 = 0;
                while (true) {
                    if (j3 == j2) {
                        j2 = get();
                        if (j3 == j2) {
                            j2 = addAndGet(-j3);
                        }
                    } else if (!this.cancelled) {
                        try {
                            Object next = it.next();
                            if (!this.cancelled) {
                                if (next == null) {
                                    th = new NullPointerException("Iterator.next() returned a null value");
                                    break loop0;
                                }
                                subscriber.onNext(next);
                                if (!this.cancelled) {
                                    if (!it.hasNext()) {
                                        if (!this.cancelled) {
                                            subscriber.onComplete();
                                        }
                                        return;
                                    }
                                    j3++;
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable th) {
                            th = th;
                            Exceptions.throwIfFatal(th);
                        }
                    } else {
                        return;
                    }
                }
                subscriber.onError(th);
                return;
            } while (j2 != 0);
        }
    }

    public FlowableFromIterable(Iterable iterable) {
        this.source = iterable;
    }

    public static void subscribe(Subscriber subscriber, Iterator it) {
        try {
            if (!it.hasNext()) {
                EmptySubscription.complete(subscriber);
            } else {
                subscriber.onSubscribe(subscriber instanceof ConditionalSubscriber ? new IteratorConditionalSubscription((ConditionalSubscriber) subscriber, it) : new IteratorSubscription(subscriber, it));
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }

    public void subscribeActual(Subscriber subscriber) {
        try {
            subscribe(subscriber, this.source.iterator());
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
