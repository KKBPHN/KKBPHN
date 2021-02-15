package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.annotations.Nullable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.BasicQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import org.reactivestreams.Subscriber;

public final class FlowableFromArray extends Flowable {
    final Object[] array;

    final class ArrayConditionalSubscription extends BaseArraySubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final ConditionalSubscriber actual;

        ArrayConditionalSubscription(ConditionalSubscriber conditionalSubscriber, Object[] objArr) {
            super(objArr);
            this.actual = conditionalSubscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Object[] objArr = this.array;
            int length = objArr.length;
            ConditionalSubscriber conditionalSubscriber = this.actual;
            int i = this.index;
            while (i != length) {
                if (!this.cancelled) {
                    Object obj = objArr[i];
                    if (obj == null) {
                        conditionalSubscriber.onError(new NullPointerException("array element is null"));
                        return;
                    } else {
                        conditionalSubscriber.tryOnNext(obj);
                        i++;
                    }
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                conditionalSubscriber.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Object[] objArr = this.array;
            int length = objArr.length;
            int i = this.index;
            ConditionalSubscriber conditionalSubscriber = this.actual;
            long j2 = j;
            do {
                long j3 = 0;
                while (true) {
                    if (j3 == j2 || i == length) {
                        if (i == length) {
                            if (!this.cancelled) {
                                conditionalSubscriber.onComplete();
                            }
                            return;
                        }
                        j2 = get();
                        if (j3 == j2) {
                            this.index = i;
                            j2 = addAndGet(-j3);
                        }
                    } else if (!this.cancelled) {
                        Object obj = objArr[i];
                        if (obj == null) {
                            conditionalSubscriber.onError(new NullPointerException("array element is null"));
                            return;
                        }
                        if (conditionalSubscriber.tryOnNext(obj)) {
                            j3++;
                        }
                        i++;
                    } else {
                        return;
                    }
                }
            } while (j2 != 0);
        }
    }

    final class ArraySubscription extends BaseArraySubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final Subscriber actual;

        ArraySubscription(Subscriber subscriber, Object[] objArr) {
            super(objArr);
            this.actual = subscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Object[] objArr = this.array;
            int length = objArr.length;
            Subscriber subscriber = this.actual;
            int i = this.index;
            while (i != length) {
                if (!this.cancelled) {
                    Object obj = objArr[i];
                    if (obj == null) {
                        subscriber.onError(new NullPointerException("array element is null"));
                        return;
                    } else {
                        subscriber.onNext(obj);
                        i++;
                    }
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                subscriber.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Object[] objArr = this.array;
            int length = objArr.length;
            int i = this.index;
            Subscriber subscriber = this.actual;
            long j2 = j;
            do {
                long j3 = 0;
                while (true) {
                    if (j3 == j2 || i == length) {
                        if (i == length) {
                            if (!this.cancelled) {
                                subscriber.onComplete();
                            }
                            return;
                        }
                        j2 = get();
                        if (j3 == j2) {
                            this.index = i;
                            j2 = addAndGet(-j3);
                        }
                    } else if (!this.cancelled) {
                        Object obj = objArr[i];
                        if (obj == null) {
                            subscriber.onError(new NullPointerException("array element is null"));
                            return;
                        }
                        subscriber.onNext(obj);
                        j3++;
                        i++;
                    } else {
                        return;
                    }
                }
            } while (j2 != 0);
        }
    }

    abstract class BaseArraySubscription extends BasicQueueSubscription {
        private static final long serialVersionUID = -2252972430506210021L;
        final Object[] array;
        volatile boolean cancelled;
        int index;

        BaseArraySubscription(Object[] objArr) {
            this.array = objArr;
        }

        public final void cancel() {
            this.cancelled = true;
        }

        public final void clear() {
            this.index = this.array.length;
        }

        public abstract void fastPath();

        public final boolean isEmpty() {
            return this.index == this.array.length;
        }

        @Nullable
        public final Object poll() {
            int i = this.index;
            Object[] objArr = this.array;
            if (i == objArr.length) {
                return null;
            }
            this.index = i + 1;
            Object obj = objArr[i];
            ObjectHelper.requireNonNull(obj, "array element is null");
            return obj;
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

    public FlowableFromArray(Object[] objArr) {
        this.array = objArr;
    }

    public void subscribeActual(Subscriber subscriber) {
        subscriber.onSubscribe(subscriber instanceof ConditionalSubscriber ? new ArrayConditionalSubscription((ConditionalSubscriber) subscriber, this.array) : new ArraySubscription(subscriber, this.array));
    }
}
