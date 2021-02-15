package io.reactivex.internal.operators.parallel;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelReduceFull extends Flowable {
    final BiFunction reducer;
    final ParallelFlowable source;

    final class ParallelReduceFullInnerSubscriber extends AtomicReference implements FlowableSubscriber {
        private static final long serialVersionUID = -7954444275102466525L;
        boolean done;
        final ParallelReduceFullMainSubscriber parent;
        final BiFunction reducer;
        Object value;

        ParallelReduceFullInnerSubscriber(ParallelReduceFullMainSubscriber parallelReduceFullMainSubscriber, BiFunction biFunction) {
            this.parent = parallelReduceFullMainSubscriber;
            this.reducer = biFunction;
        }

        /* access modifiers changed from: 0000 */
        public void cancel() {
            SubscriptionHelper.cancel(this);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.innerComplete(this.value);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.parent.innerError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                Object obj2 = this.value;
                if (obj2 != null) {
                    try {
                        obj = this.reducer.apply(obj2, obj);
                        ObjectHelper.requireNonNull(obj, "The reducer returned a null value");
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        ((Subscription) get()).cancel();
                        onError(th);
                        return;
                    }
                }
                this.value = obj;
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    final class ParallelReduceFullMainSubscriber extends DeferredScalarSubscription {
        private static final long serialVersionUID = -5370107872170712765L;
        final AtomicReference current = new AtomicReference();
        final AtomicReference error = new AtomicReference();
        final BiFunction reducer;
        final AtomicInteger remaining = new AtomicInteger();
        final ParallelReduceFullInnerSubscriber[] subscribers;

        ParallelReduceFullMainSubscriber(Subscriber subscriber, int i, BiFunction biFunction) {
            super(subscriber);
            ParallelReduceFullInnerSubscriber[] parallelReduceFullInnerSubscriberArr = new ParallelReduceFullInnerSubscriber[i];
            for (int i2 = 0; i2 < i; i2++) {
                parallelReduceFullInnerSubscriberArr[i2] = new ParallelReduceFullInnerSubscriber(this, biFunction);
            }
            this.subscribers = parallelReduceFullInnerSubscriberArr;
            this.reducer = biFunction;
            this.remaining.lazySet(i);
        }

        /* access modifiers changed from: 0000 */
        public SlotPair addValue(Object obj) {
            SlotPair slotPair;
            int tryAcquireSlot;
            while (true) {
                slotPair = (SlotPair) this.current.get();
                if (slotPair == null) {
                    slotPair = new SlotPair();
                    if (!this.current.compareAndSet(null, slotPair)) {
                    }
                }
                tryAcquireSlot = slotPair.tryAcquireSlot();
                if (tryAcquireSlot >= 0) {
                    break;
                }
                this.current.compareAndSet(slotPair, null);
            }
            if (tryAcquireSlot == 0) {
                slotPair.first = obj;
            } else {
                slotPair.second = obj;
            }
            if (!slotPair.releaseSlot()) {
                return null;
            }
            this.current.compareAndSet(slotPair, null);
            return slotPair;
        }

        public void cancel() {
            for (ParallelReduceFullInnerSubscriber cancel : this.subscribers) {
                cancel.cancel();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(Object obj) {
            if (obj != null) {
                while (true) {
                    SlotPair addValue = addValue(obj);
                    if (addValue == null) {
                        break;
                    }
                    try {
                        obj = this.reducer.apply(addValue.first, addValue.second);
                        ObjectHelper.requireNonNull(obj, "The reducer returned a null value");
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        innerError(th);
                        return;
                    }
                }
            }
            if (this.remaining.decrementAndGet() == 0) {
                SlotPair slotPair = (SlotPair) this.current.get();
                this.current.lazySet(null);
                if (slotPair != null) {
                    complete(slotPair.first);
                } else {
                    this.actual.onComplete();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable th) {
            if (this.error.compareAndSet(null, th)) {
                cancel();
                this.actual.onError(th);
            } else if (th != this.error.get()) {
                RxJavaPlugins.onError(th);
            }
        }
    }

    final class SlotPair extends AtomicInteger {
        private static final long serialVersionUID = 473971317683868662L;
        Object first;
        final AtomicInteger releaseIndex = new AtomicInteger();
        Object second;

        SlotPair() {
        }

        /* access modifiers changed from: 0000 */
        public boolean releaseSlot() {
            return this.releaseIndex.incrementAndGet() == 2;
        }

        /* access modifiers changed from: 0000 */
        public int tryAcquireSlot() {
            int i;
            do {
                i = get();
                if (i >= 2) {
                    return -1;
                }
            } while (!compareAndSet(i, i + 1));
            return i;
        }
    }

    public ParallelReduceFull(ParallelFlowable parallelFlowable, BiFunction biFunction) {
        this.source = parallelFlowable;
        this.reducer = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        ParallelReduceFullMainSubscriber parallelReduceFullMainSubscriber = new ParallelReduceFullMainSubscriber(subscriber, this.source.parallelism(), this.reducer);
        subscriber.onSubscribe(parallelReduceFullMainSubscriber);
        this.source.subscribe(parallelReduceFullMainSubscriber.subscribers);
    }
}
