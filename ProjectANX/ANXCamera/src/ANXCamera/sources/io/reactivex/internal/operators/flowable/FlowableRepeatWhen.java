package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRepeatWhen extends AbstractFlowableWithUpstream {
    final Function handler;

    final class RepeatWhenSubscriber extends WhenSourceSubscriber {
        private static final long serialVersionUID = -2680129890138081029L;

        RepeatWhenSubscriber(Subscriber subscriber, FlowableProcessor flowableProcessor, Subscription subscription) {
            super(subscriber, flowableProcessor, subscription);
        }

        public void onComplete() {
            again(Integer.valueOf(0));
        }

        public void onError(Throwable th) {
            this.receiver.cancel();
            this.actual.onError(th);
        }
    }

    final class WhenReceiver extends AtomicInteger implements FlowableSubscriber, Subscription {
        private static final long serialVersionUID = 2827772011130406689L;
        final AtomicLong requested = new AtomicLong();
        final Publisher source;
        WhenSourceSubscriber subscriber;
        final AtomicReference subscription = new AtomicReference();

        WhenReceiver(Publisher publisher) {
            this.source = publisher;
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.subscription);
        }

        public void onComplete() {
            this.subscriber.cancel();
            this.subscriber.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.subscriber.cancel();
            this.subscriber.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (getAndIncrement() == 0) {
                while (!SubscriptionHelper.isCancelled((Subscription) this.subscription.get())) {
                    this.source.subscribe(this.subscriber);
                    if (decrementAndGet() == 0) {
                    }
                }
            }
        }

        public void onSubscribe(Subscription subscription2) {
            SubscriptionHelper.deferredSetOnce(this.subscription, this.requested, subscription2);
        }

        public void request(long j) {
            SubscriptionHelper.deferredRequest(this.subscription, this.requested, j);
        }
    }

    abstract class WhenSourceSubscriber extends SubscriptionArbiter implements FlowableSubscriber {
        private static final long serialVersionUID = -5604623027276966720L;
        protected final Subscriber actual;
        protected final FlowableProcessor processor;
        private long produced;
        protected final Subscription receiver;

        WhenSourceSubscriber(Subscriber subscriber, FlowableProcessor flowableProcessor, Subscription subscription) {
            this.actual = subscriber;
            this.processor = flowableProcessor;
            this.receiver = subscription;
        }

        /* access modifiers changed from: protected */
        public final void again(Object obj) {
            long j = this.produced;
            if (j != 0) {
                this.produced = 0;
                produced(j);
            }
            this.receiver.request(1);
            this.processor.onNext(obj);
        }

        public final void cancel() {
            super.cancel();
            this.receiver.cancel();
        }

        public final void onNext(Object obj) {
            this.produced++;
            this.actual.onNext(obj);
        }

        public final void onSubscribe(Subscription subscription) {
            setSubscription(subscription);
        }
    }

    public FlowableRepeatWhen(Flowable flowable, Function function) {
        super(flowable);
        this.handler = function;
    }

    public void subscribeActual(Subscriber subscriber) {
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        FlowableProcessor serialized = UnicastProcessor.create(8).toSerialized();
        try {
            Object apply = this.handler.apply(serialized);
            ObjectHelper.requireNonNull(apply, "handler returned a null Publisher");
            Publisher publisher = (Publisher) apply;
            WhenReceiver whenReceiver = new WhenReceiver(this.source);
            RepeatWhenSubscriber repeatWhenSubscriber = new RepeatWhenSubscriber(serializedSubscriber, serialized, whenReceiver);
            whenReceiver.subscriber = repeatWhenSubscriber;
            subscriber.onSubscribe(repeatWhenSubscriber);
            publisher.subscribe(whenReceiver);
            whenReceiver.onNext(Integer.valueOf(0));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
