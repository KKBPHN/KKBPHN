package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSamplePublisher extends Flowable {
    final boolean emitLast;
    final Publisher other;
    final Publisher source;

    final class SampleMainEmitLast extends SamplePublisherSubscriber {
        private static final long serialVersionUID = -3029755663834015785L;
        volatile boolean done;
        final AtomicInteger wip = new AtomicInteger();

        SampleMainEmitLast(Subscriber subscriber, Publisher publisher) {
            super(subscriber, publisher);
        }

        /* access modifiers changed from: 0000 */
        public void completeMain() {
            this.done = true;
            if (this.wip.getAndIncrement() == 0) {
                emit();
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void completeOther() {
            this.done = true;
            if (this.wip.getAndIncrement() == 0) {
                emit();
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            if (this.wip.getAndIncrement() == 0) {
                do {
                    boolean z = this.done;
                    emit();
                    if (z) {
                        this.actual.onComplete();
                        return;
                    }
                } while (this.wip.decrementAndGet() != 0);
            }
        }
    }

    final class SampleMainNoLast extends SamplePublisherSubscriber {
        private static final long serialVersionUID = -3029755663834015785L;

        SampleMainNoLast(Subscriber subscriber, Publisher publisher) {
            super(subscriber, publisher);
        }

        /* access modifiers changed from: 0000 */
        public void completeMain() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void completeOther() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            emit();
        }
    }

    abstract class SamplePublisherSubscriber extends AtomicReference implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = -3517602651313910099L;
        final Subscriber actual;
        final AtomicReference other = new AtomicReference();
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final Publisher sampler;

        SamplePublisherSubscriber(Subscriber subscriber, Publisher publisher) {
            this.actual = subscriber;
            this.sampler = publisher;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.other);
            this.s.cancel();
        }

        public void complete() {
            this.s.cancel();
            completeOther();
        }

        public abstract void completeMain();

        public abstract void completeOther();

        /* access modifiers changed from: 0000 */
        public void emit() {
            Object andSet = getAndSet(null);
            if (andSet == null) {
                return;
            }
            if (this.requested.get() != 0) {
                this.actual.onNext(andSet);
                BackpressureHelper.produced(this.requested, 1);
                return;
            }
            cancel();
            this.actual.onError(new MissingBackpressureException("Couldn't emit value due to lack of requests!"));
        }

        public void error(Throwable th) {
            this.s.cancel();
            this.actual.onError(th);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            completeMain();
        }

        public void onError(Throwable th) {
            SubscriptionHelper.cancel(this.other);
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            lazySet(obj);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                if (this.other.get() == null) {
                    this.sampler.subscribe(new SamplerSubscriber(this));
                    subscription.request(Long.MAX_VALUE);
                }
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
            }
        }

        public abstract void run();

        /* access modifiers changed from: 0000 */
        public boolean setOther(Subscription subscription) {
            return SubscriptionHelper.setOnce(this.other, subscription);
        }
    }

    final class SamplerSubscriber implements FlowableSubscriber {
        final SamplePublisherSubscriber parent;

        SamplerSubscriber(SamplePublisherSubscriber samplePublisherSubscriber) {
            this.parent = samplePublisherSubscriber;
        }

        public void onComplete() {
            this.parent.complete();
        }

        public void onError(Throwable th) {
            this.parent.error(th);
        }

        public void onNext(Object obj) {
            this.parent.run();
        }

        public void onSubscribe(Subscription subscription) {
            if (this.parent.setOther(subscription)) {
                subscription.request(Long.MAX_VALUE);
            }
        }
    }

    public FlowableSamplePublisher(Publisher publisher, Publisher publisher2, boolean z) {
        this.source = publisher;
        this.other = publisher2;
        this.emitLast = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        Publisher publisher;
        Subscriber subscriber2;
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        if (this.emitLast) {
            publisher = this.source;
            subscriber2 = new SampleMainEmitLast(serializedSubscriber, this.other);
        } else {
            publisher = this.source;
            subscriber2 = new SampleMainNoLast(serializedSubscriber, this.other);
        }
        publisher.subscribe(subscriber2);
    }
}
