package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeWhile extends AbstractFlowableWithUpstream {
    final Predicate predicate;

    final class TakeWhileSubscriber implements FlowableSubscriber, Subscription {
        final Subscriber actual;
        boolean done;
        final Predicate predicate;
        Subscription s;

        TakeWhileSubscriber(Subscriber subscriber, Predicate predicate2) {
            this.actual = subscriber;
            this.predicate = predicate2;
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
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
                    if (!this.predicate.test(obj)) {
                        this.done = true;
                        this.s.cancel();
                        this.actual.onComplete();
                        return;
                    }
                    this.actual.onNext(obj);
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
            }
        }

        public void request(long j) {
            this.s.request(j);
        }
    }

    public FlowableTakeWhile(Flowable flowable, Predicate predicate2) {
        super(flowable);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new TakeWhileSubscriber(subscriber, this.predicate));
    }
}
