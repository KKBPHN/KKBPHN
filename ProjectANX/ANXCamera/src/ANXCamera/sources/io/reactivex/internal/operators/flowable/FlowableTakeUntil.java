package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeUntil extends AbstractFlowableWithUpstream {
    final Publisher other;

    final class TakeUntilMainSubscriber extends AtomicInteger implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -4945480365982832967L;
        final Subscriber actual;
        final AtomicThrowable error = new AtomicThrowable();
        final OtherSubscriber other = new OtherSubscriber();
        final AtomicLong requested = new AtomicLong();
        final AtomicReference s = new AtomicReference();

        final class OtherSubscriber extends AtomicReference implements FlowableSubscriber {
            private static final long serialVersionUID = -3592821756711087922L;

            OtherSubscriber() {
            }

            public void onComplete() {
                SubscriptionHelper.cancel(TakeUntilMainSubscriber.this.s);
                TakeUntilMainSubscriber takeUntilMainSubscriber = TakeUntilMainSubscriber.this;
                HalfSerializer.onComplete(takeUntilMainSubscriber.actual, (AtomicInteger) takeUntilMainSubscriber, takeUntilMainSubscriber.error);
            }

            public void onError(Throwable th) {
                SubscriptionHelper.cancel(TakeUntilMainSubscriber.this.s);
                TakeUntilMainSubscriber takeUntilMainSubscriber = TakeUntilMainSubscriber.this;
                HalfSerializer.onError(takeUntilMainSubscriber.actual, th, (AtomicInteger) takeUntilMainSubscriber, takeUntilMainSubscriber.error);
            }

            public void onNext(Object obj) {
                SubscriptionHelper.cancel(this);
                onComplete();
            }

            public void onSubscribe(Subscription subscription) {
                if (SubscriptionHelper.setOnce(this, subscription)) {
                    subscription.request(Long.MAX_VALUE);
                }
            }
        }

        TakeUntilMainSubscriber(Subscriber subscriber) {
            this.actual = subscriber;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.s);
            SubscriptionHelper.cancel(this.other);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
        }

        public void onError(Throwable th) {
            SubscriptionHelper.cancel(this.other);
            HalfSerializer.onError(this.actual, th, (AtomicInteger) this, this.error);
        }

        public void onNext(Object obj) {
            HalfSerializer.onNext(this.actual, obj, (AtomicInteger) this, this.error);
        }

        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this.s, this.requested, subscription);
        }

        public void request(long j) {
            SubscriptionHelper.deferredRequest(this.s, this.requested, j);
        }
    }

    public FlowableTakeUntil(Flowable flowable, Publisher publisher) {
        super(flowable);
        this.other = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        TakeUntilMainSubscriber takeUntilMainSubscriber = new TakeUntilMainSubscriber(subscriber);
        subscriber.onSubscribe(takeUntilMainSubscriber);
        this.other.subscribe(takeUntilMainSubscriber.other);
        this.source.subscribe((FlowableSubscriber) takeUntilMainSubscriber);
    }
}
