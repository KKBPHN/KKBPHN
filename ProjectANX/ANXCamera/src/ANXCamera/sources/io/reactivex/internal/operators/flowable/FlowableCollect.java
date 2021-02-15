package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableCollect extends AbstractFlowableWithUpstream {
    final BiConsumer collector;
    final Callable initialSupplier;

    final class CollectSubscriber extends DeferredScalarSubscription implements FlowableSubscriber {
        private static final long serialVersionUID = -3589550218733891694L;
        final BiConsumer collector;
        boolean done;
        Subscription s;
        final Object u;

        CollectSubscriber(Subscriber subscriber, Object obj, BiConsumer biConsumer) {
            super(subscriber);
            this.collector = biConsumer;
            this.u = obj;
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                complete(this.u);
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
                    this.collector.accept(this.u, obj);
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

    public FlowableCollect(Flowable flowable, Callable callable, BiConsumer biConsumer) {
        super(flowable);
        this.initialSupplier = callable;
        this.collector = biConsumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        try {
            Object call = this.initialSupplier.call();
            ObjectHelper.requireNonNull(call, "The initial value supplied is null");
            this.source.subscribe((FlowableSubscriber) new CollectSubscriber(subscriber, call, this.collector));
        } catch (Throwable th) {
            EmptySubscription.error(th, subscriber);
        }
    }
}
