package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableReduce extends AbstractFlowableWithUpstream {
    final BiFunction reducer;

    final class ReduceSubscriber extends DeferredScalarSubscription implements FlowableSubscriber {
        private static final long serialVersionUID = -4663883003264602070L;
        final BiFunction reducer;
        Subscription s;

        ReduceSubscriber(Subscriber subscriber, BiFunction biFunction) {
            super(subscriber);
            this.reducer = biFunction;
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }

        public void onComplete() {
            Subscription subscription = this.s;
            SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
            if (subscription != subscriptionHelper) {
                this.s = subscriptionHelper;
                Object obj = this.value;
                if (obj != null) {
                    complete(obj);
                } else {
                    this.actual.onComplete();
                }
            }
        }

        public void onError(Throwable th) {
            Subscription subscription = this.s;
            SubscriptionHelper subscriptionHelper = SubscriptionHelper.CANCELLED;
            if (subscription == subscriptionHelper) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.s = subscriptionHelper;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (this.s != SubscriptionHelper.CANCELLED) {
                Object obj2 = this.value;
                if (obj2 == null) {
                    this.value = obj;
                } else {
                    try {
                        Object apply = this.reducer.apply(obj2, obj);
                        ObjectHelper.requireNonNull(apply, "The reducer returned a null value");
                        this.value = apply;
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.s.cancel();
                        onError(th);
                    }
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

    public FlowableReduce(Flowable flowable, BiFunction biFunction) {
        super(flowable);
        this.reducer = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new ReduceSubscriber(subscriber, this.reducer));
    }
}
