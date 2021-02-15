package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSkipWhile extends AbstractFlowableWithUpstream {
    final Predicate predicate;

    final class SkipWhileSubscriber implements FlowableSubscriber, Subscription {
        final Subscriber actual;
        boolean notSkipping;
        final Predicate predicate;
        Subscription s;

        SkipWhileSubscriber(Subscriber subscriber, Predicate predicate2) {
            this.actual = subscriber;
            this.predicate = predicate2;
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
            if (!this.notSkipping) {
                try {
                    if (this.predicate.test(obj)) {
                        this.s.request(1);
                    }
                    this.notSkipping = true;
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.s.cancel();
                    this.actual.onError(th);
                    return;
                }
            }
            this.actual.onNext(obj);
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

    public FlowableSkipWhile(Flowable flowable, Predicate predicate2) {
        super(flowable);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new SkipWhileSubscriber(subscriber, this.predicate));
    }
}
