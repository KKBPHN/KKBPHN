package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRetryBiPredicate extends AbstractFlowableWithUpstream {
    final BiPredicate predicate;

    final class RetryBiSubscriber extends AtomicInteger implements FlowableSubscriber {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber actual;
        final BiPredicate predicate;
        long produced;
        int retries;
        final SubscriptionArbiter sa;
        final Publisher source;

        RetryBiSubscriber(Subscriber subscriber, BiPredicate biPredicate, SubscriptionArbiter subscriptionArbiter, Publisher publisher) {
            this.actual = subscriber;
            this.sa = subscriptionArbiter;
            this.source = publisher;
            this.predicate = biPredicate;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            char c = 1;
            try {
                BiPredicate biPredicate = this.predicate;
                int i = this.retries + 1;
                this.retries = i;
                c = biPredicate.test(Integer.valueOf(i), th);
                if (c == 0) {
                    this.actual.onError(th);
                } else {
                    subscribeNext();
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                Subscriber subscriber = this.actual;
                Throwable[] thArr = new Throwable[2];
                thArr[0] = th;
                thArr[c] = th2;
                subscriber.onError(new CompositeException(thArr));
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

    public FlowableRetryBiPredicate(Flowable flowable, BiPredicate biPredicate) {
        super(flowable);
        this.predicate = biPredicate;
    }

    public void subscribeActual(Subscriber subscriber) {
        SubscriptionArbiter subscriptionArbiter = new SubscriptionArbiter();
        subscriber.onSubscribe(subscriptionArbiter);
        new RetryBiSubscriber(subscriber, this.predicate, subscriptionArbiter, this.source).subscribeNext();
    }
}
