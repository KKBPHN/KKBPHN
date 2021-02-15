package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRetryPredicate extends AbstractFlowableWithUpstream {
    final long count;
    final Predicate predicate;

    final class RetrySubscriber extends AtomicInteger implements FlowableSubscriber {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber actual;
        final Predicate predicate;
        long produced;
        long remaining;
        final SubscriptionArbiter sa;
        final Publisher source;

        RetrySubscriber(Subscriber subscriber, long j, Predicate predicate2, SubscriptionArbiter subscriptionArbiter, Publisher publisher) {
            this.actual = subscriber;
            this.sa = subscriptionArbiter;
            this.source = publisher;
            this.predicate = predicate2;
            this.remaining = j;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            long j = this.remaining;
            if (j != Long.MAX_VALUE) {
                this.remaining = j - 1;
            }
            if (j == 0) {
                this.actual.onError(th);
            } else {
                try {
                    if (!this.predicate.test(th)) {
                        this.actual.onError(th);
                        return;
                    }
                    subscribeNext();
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.actual.onError(new CompositeException(th, th2));
                }
            }
        }

        public void onNext(Object obj) {
            this.produced++;
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            this.sa.setSubscription(subscription);
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                while (!this.sa.isCancelled()) {
                    long j = this.produced;
                    if (j != 0) {
                        this.produced = 0;
                        this.sa.produced(j);
                    }
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                    if (i == 0) {
                    }
                }
            }
        }
    }

    public FlowableRetryPredicate(Flowable flowable, long j, Predicate predicate2) {
        super(flowable);
        this.predicate = predicate2;
        this.count = j;
    }

    public void subscribeActual(Subscriber subscriber) {
        SubscriptionArbiter subscriptionArbiter = new SubscriptionArbiter();
        subscriber.onSubscribe(subscriptionArbiter);
        RetrySubscriber retrySubscriber = new RetrySubscriber(subscriber, this.count, this.predicate, subscriptionArbiter, this.source);
        retrySubscriber.subscribeNext();
    }
}
