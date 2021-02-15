package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeLastOne extends AbstractFlowableWithUpstream {

    final class TakeLastOneSubscriber extends DeferredScalarSubscription implements FlowableSubscriber {
        private static final long serialVersionUID = -5467847744262967226L;
        Subscription s;

        TakeLastOneSubscriber(Subscriber subscriber) {
            super(subscriber);
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }

        public void onComplete() {
            Object obj = this.value;
            if (obj != null) {
                complete(obj);
            } else {
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            this.value = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.value = obj;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableTakeLastOne(Flowable flowable) {
        super(flowable);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new TakeLastOneSubscriber(subscriber));
    }
}
