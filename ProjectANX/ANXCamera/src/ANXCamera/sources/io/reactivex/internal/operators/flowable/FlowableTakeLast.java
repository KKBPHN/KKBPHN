package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeLast extends AbstractFlowableWithUpstream {
    final int count;

    final class TakeLastSubscriber extends ArrayDeque implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = 7240042530241604978L;
        final Subscriber actual;
        volatile boolean cancelled;
        final int count;
        volatile boolean done;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final AtomicInteger wip = new AtomicInteger();

        TakeLastSubscriber(Subscriber subscriber, int i) {
            this.actual = subscriber;
            this.count = i;
        }

        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                long j = this.requested.get();
                while (!this.cancelled) {
                    if (this.done) {
                        long j2 = 0;
                        while (j2 != j) {
                            if (!this.cancelled) {
                                Object poll = poll();
                                if (poll == null) {
                                    subscriber.onComplete();
                                    return;
                                } else {
                                    subscriber.onNext(poll);
                                    j2++;
                                }
                            } else {
                                return;
                            }
                        }
                        if (!(j2 == 0 || j == Long.MAX_VALUE)) {
                            j = this.requested.addAndGet(-j2);
                        }
                    }
                    if (this.wip.decrementAndGet() == 0) {
                    }
                }
            }
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (this.count == size()) {
                poll();
            }
            offer(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }
    }

    public FlowableTakeLast(Flowable flowable, int i) {
        super(flowable);
        this.count = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new TakeLastSubscriber(subscriber, this.count));
    }
}
