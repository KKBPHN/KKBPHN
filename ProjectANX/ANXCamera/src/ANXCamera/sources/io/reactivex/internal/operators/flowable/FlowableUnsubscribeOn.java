package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableUnsubscribeOn extends AbstractFlowableWithUpstream {
    final Scheduler scheduler;

    final class UnsubscribeSubscriber extends AtomicBoolean implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = 1015244841293359600L;
        final Subscriber actual;
        Subscription s;
        final Scheduler scheduler;

        final class Cancellation implements Runnable {
            Cancellation() {
            }

            public void run() {
                UnsubscribeSubscriber.this.s.cancel();
            }
        }

        UnsubscribeSubscriber(Subscriber subscriber, Scheduler scheduler2) {
            this.actual = subscriber;
            this.scheduler = scheduler2;
        }

        public void cancel() {
            if (compareAndSet(false, true)) {
                this.scheduler.scheduleDirect(new Cancellation());
            }
        }

        public void onComplete() {
            if (!get()) {
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (get()) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        public void onNext(Object obj) {
            if (!get()) {
                this.actual.onNext(obj);
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

    public FlowableUnsubscribeOn(Flowable flowable, Scheduler scheduler2) {
        super(flowable);
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new UnsubscribeSubscriber(subscriber, this.scheduler));
    }
}
