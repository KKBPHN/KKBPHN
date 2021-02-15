package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDelaySubscriptionOther extends Flowable {
    final Publisher main;
    final Publisher other;

    final class DelaySubscriber implements FlowableSubscriber {
        final Subscriber child;
        boolean done;
        final SubscriptionArbiter serial;

        final class DelaySubscription implements Subscription {
            private final Subscription s;

            DelaySubscription(Subscription subscription) {
                this.s = subscription;
            }

            public void cancel() {
                this.s.cancel();
            }

            public void request(long j) {
            }
        }

        final class OnCompleteSubscriber implements FlowableSubscriber {
            OnCompleteSubscriber() {
            }

            public void onComplete() {
                DelaySubscriber.this.child.onComplete();
            }

            public void onError(Throwable th) {
                DelaySubscriber.this.child.onError(th);
            }

            public void onNext(Object obj) {
                DelaySubscriber.this.child.onNext(obj);
            }

            public void onSubscribe(Subscription subscription) {
                DelaySubscriber.this.serial.setSubscription(subscription);
            }
        }

        DelaySubscriber(SubscriptionArbiter subscriptionArbiter, Subscriber subscriber) {
            this.serial = subscriptionArbiter;
            this.child = subscriber;
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                FlowableDelaySubscriptionOther.this.main.subscribe(new OnCompleteSubscriber());
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.child.onError(th);
        }

        public void onNext(Object obj) {
            onComplete();
        }

        public void onSubscribe(Subscription subscription) {
            this.serial.setSubscription(new DelaySubscription(subscription));
            subscription.request(Long.MAX_VALUE);
        }
    }

    public FlowableDelaySubscriptionOther(Publisher publisher, Publisher publisher2) {
        this.main = publisher;
        this.other = publisher2;
    }

    public void subscribeActual(Subscriber subscriber) {
        SubscriptionArbiter subscriptionArbiter = new SubscriptionArbiter();
        subscriber.onSubscribe(subscriptionArbiter);
        this.other.subscribe(new DelaySubscriber(subscriptionArbiter, subscriber));
    }
}
