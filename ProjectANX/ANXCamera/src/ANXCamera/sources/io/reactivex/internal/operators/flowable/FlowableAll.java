package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableAll extends AbstractFlowableWithUpstream {
    final Predicate predicate;

    final class AllSubscriber extends DeferredScalarSubscription implements FlowableSubscriber {
        private static final long serialVersionUID = -3521127104134758517L;
        boolean done;
        final Predicate predicate;
        Subscription s;

        AllSubscriber(Subscriber subscriber, Predicate predicate2) {
            super(subscriber);
            this.predicate = predicate2;
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                complete(Boolean.valueOf(true));
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
                try {
                    if (!this.predicate.test(obj)) {
                        this.done = true;
                        this.s.cancel();
                        complete(Boolean.valueOf(false));
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
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableAll(Flowable flowable, Predicate predicate2) {
        super(flowable);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new AllSubscriber(subscriber, this.predicate));
    }
}
