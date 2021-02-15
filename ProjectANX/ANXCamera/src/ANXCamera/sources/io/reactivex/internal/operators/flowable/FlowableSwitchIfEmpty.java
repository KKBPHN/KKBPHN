package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSwitchIfEmpty extends AbstractFlowableWithUpstream {
    final Publisher other;

    final class SwitchIfEmptySubscriber implements FlowableSubscriber {
        final Subscriber actual;
        final SubscriptionArbiter arbiter = new SubscriptionArbiter();
        boolean empty = true;
        final Publisher other;

        SwitchIfEmptySubscriber(Subscriber subscriber, Publisher publisher) {
            this.actual = subscriber;
            this.other = publisher;
        }

        public void onComplete() {
            if (this.empty) {
                this.empty = false;
                this.other.subscribe(this);
                return;
            }
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (this.empty) {
                this.empty = false;
            }
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            this.arbiter.setSubscription(subscription);
        }
    }

    public FlowableSwitchIfEmpty(Flowable flowable, Publisher publisher) {
        super(flowable);
        this.other = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        SwitchIfEmptySubscriber switchIfEmptySubscriber = new SwitchIfEmptySubscriber(subscriber, this.other);
        subscriber.onSubscribe(switchIfEmptySubscriber.arbiter);
        this.source.subscribe((FlowableSubscriber) switchIfEmptySubscriber);
    }
}
