package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.schedulers.Timed;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTimeInterval extends AbstractFlowableWithUpstream {
    final Scheduler scheduler;
    final TimeUnit unit;

    final class TimeIntervalSubscriber implements FlowableSubscriber, Subscription {
        final Subscriber actual;
        long lastTime;
        Subscription s;
        final Scheduler scheduler;
        final TimeUnit unit;

        TimeIntervalSubscriber(Subscriber subscriber, TimeUnit timeUnit, Scheduler scheduler2) {
            this.actual = subscriber;
            this.scheduler = scheduler2;
            this.unit = timeUnit;
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
            long now = this.scheduler.now(this.unit);
            long j = this.lastTime;
            this.lastTime = now;
            this.actual.onNext(new Timed(obj, now - j, this.unit));
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.lastTime = this.scheduler.now(this.unit);
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void request(long j) {
            this.s.request(j);
        }
    }

    public FlowableTimeInterval(Flowable flowable, TimeUnit timeUnit, Scheduler scheduler2) {
        super(flowable);
        this.scheduler = scheduler2;
        this.unit = timeUnit;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe((FlowableSubscriber) new TimeIntervalSubscriber(subscriber, this.unit, this.scheduler));
    }
}
