package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.Scheduler.Worker;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSubscribeOn extends AbstractFlowableWithUpstream {
    final boolean nonScheduledRequests;
    final Scheduler scheduler;

    final class SubscribeOnSubscriber extends AtomicReference implements FlowableSubscriber, Subscription, Runnable {
        private static final long serialVersionUID = 8094547886072529208L;
        final Subscriber actual;
        final boolean nonScheduledRequests;
        final AtomicLong requested = new AtomicLong();
        final AtomicReference s = new AtomicReference();
        Publisher source;
        final Worker worker;

        final class Request implements Runnable {
            private final long n;
            private final Subscription s;

            Request(Subscription subscription, long j) {
                this.s = subscription;
                this.n = j;
            }

            public void run() {
                this.s.request(this.n);
            }
        }

        SubscribeOnSubscriber(Subscriber subscriber, Worker worker2, Publisher publisher, boolean z) {
            this.actual = subscriber;
            this.worker = worker2;
            this.source = publisher;
            this.nonScheduledRequests = !z;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.s);
            this.worker.dispose();
        }

        public void onComplete() {
            this.actual.onComplete();
            this.worker.dispose();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
            this.worker.dispose();
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this.s, subscription)) {
                long andSet = this.requested.getAndSet(0);
                if (andSet != 0) {
                    requestUpstream(andSet, subscription);
                }
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                Subscription subscription = (Subscription) this.s.get();
                if (subscription != null) {
                    requestUpstream(j, subscription);
                    return;
                }
                BackpressureHelper.add(this.requested, j);
                Subscription subscription2 = (Subscription) this.s.get();
                if (subscription2 != null) {
                    long andSet = this.requested.getAndSet(0);
                    if (andSet != 0) {
                        requestUpstream(andSet, subscription2);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void requestUpstream(long j, Subscription subscription) {
            if (this.nonScheduledRequests || Thread.currentThread() == get()) {
                subscription.request(j);
            } else {
                this.worker.schedule(new Request(subscription, j));
            }
        }

        public void run() {
            lazySet(Thread.currentThread());
            Publisher publisher = this.source;
            this.source = null;
            publisher.subscribe(this);
        }
    }

    public FlowableSubscribeOn(Flowable flowable, Scheduler scheduler2, boolean z) {
        super(flowable);
        this.scheduler = scheduler2;
        this.nonScheduledRequests = z;
    }

    public void subscribeActual(Subscriber subscriber) {
        Worker createWorker = this.scheduler.createWorker();
        SubscribeOnSubscriber subscribeOnSubscriber = new SubscribeOnSubscriber(subscriber, createWorker, this.source, this.nonScheduledRequests);
        subscriber.onSubscribe(subscribeOnSubscriber);
        createWorker.schedule(subscribeOnSubscriber);
    }
}
