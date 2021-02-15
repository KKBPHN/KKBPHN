package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSingle extends AbstractFlowableWithUpstream {
    final Object defaultValue;

    final class SingleElementSubscriber extends DeferredScalarSubscription implements FlowableSubscriber {
        private static final long serialVersionUID = -5526049321428043809L;
        final Object defaultValue;
        boolean done;
        Subscription s;

        SingleElementSubscriber(Subscriber subscriber, Object obj) {
            super(subscriber);
            this.defaultValue = obj;
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Object obj = this.value;
                this.value = null;
                if (obj == null) {
                    obj = this.defaultValue;
                }
                if (obj == null) {
                    this.actual.onComplete();
                } else {
                    complete(obj);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.s.cancel();
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = obj;
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableSingle(Flowable flowable, Object obj) {
        super(flowable);
        this.defaultValue = obj;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new SingleElementSubscriber(subscriber, this.defaultValue));
    }
}
