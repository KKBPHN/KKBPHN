package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableToList extends AbstractFlowableWithUpstream {
    final Callable collectionSupplier;

    final class ToListSubscriber extends DeferredScalarSubscription implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -8134157938864266736L;
        Subscription s;

        ToListSubscriber(Subscriber subscriber, Collection collection) {
            super(subscriber);
            this.value = collection;
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }

        public void onComplete() {
            complete(this.value);
        }

        public void onError(Throwable th) {
            this.value = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            Collection collection = (Collection) this.value;
            if (collection != null) {
                collection.add(obj);
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

    public FlowableToList(Flowable flowable, Callable callable) {
        super(flowable);
        this.collectionSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        try {
            Object call = this.collectionSupplier.call();
            ObjectHelper.requireNonNull(call, "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
            this.source.subscribe((FlowableSubscriber) new ToListSubscriber(subscriber, (Collection) call));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
