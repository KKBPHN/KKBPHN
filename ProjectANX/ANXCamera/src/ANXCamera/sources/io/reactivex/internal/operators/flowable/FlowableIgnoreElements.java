package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Nullable;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableIgnoreElements extends AbstractFlowableWithUpstream {

    final class IgnoreElementsSubscriber implements FlowableSubscriber, QueueSubscription {
        final Subscriber actual;
        Subscription s;

        IgnoreElementsSubscriber(Subscriber subscriber) {
            this.actual = subscriber;
        }

        public void cancel() {
            this.s.cancel();
        }

        public void clear() {
        }

        public boolean isEmpty() {
            return true;
        }

        public boolean offer(Object obj) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        public boolean offer(Object obj, Object obj2) {
            throw new UnsupportedOperationException("Should not be called!");
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }

        @Nullable
        public Object poll() {
            return null;
        }

        public void request(long j) {
        }

        public int requestFusion(int i) {
            return i & 2;
        }
    }

    public FlowableIgnoreElements(Flowable flowable) {
        super(flowable);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new IgnoreElementsSubscriber(subscriber));
    }
}
