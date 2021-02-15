package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.ArrayDeque;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSkipLast extends AbstractFlowableWithUpstream {
    final int skip;

    final class SkipLastSubscriber extends ArrayDeque implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -3807491841935125653L;
        final Subscriber actual;
        Subscription s;
        final int skip;

        SkipLastSubscriber(Subscriber subscriber, int i) {
            super(i);
            this.actual = subscriber;
            this.skip = i;
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
            if (this.skip == size()) {
                this.actual.onNext(poll());
            } else {
                this.s.request(1);
            }
            offer(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            this.s.request(j);
        }
    }

    public FlowableSkipLast(Flowable flowable, int i) {
        super(flowable);
        this.skip = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new SkipLastSubscriber(subscriber, this.skip));
    }
}
