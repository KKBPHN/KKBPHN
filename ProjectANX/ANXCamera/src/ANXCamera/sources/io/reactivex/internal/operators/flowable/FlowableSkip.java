package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSkip extends AbstractFlowableWithUpstream {
    final long n;

    final class SkipSubscriber implements FlowableSubscriber, Subscription {
        final Subscriber actual;
        long remaining;
        Subscription s;

        SkipSubscriber(Subscriber subscriber, long j) {
            this.actual = subscriber;
            this.remaining = j;
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            long j = this.remaining;
            if (j != 0) {
                this.remaining = j - 1;
            } else {
                this.actual.onNext(obj);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                long j = this.remaining;
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(j);
            }
        }

        public void request(long j) {
            this.s.request(j);
        }
    }

    public FlowableSkip(Flowable flowable, long j) {
        super(flowable);
        this.n = j;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new SkipSubscriber(subscriber, this.n));
    }
}
