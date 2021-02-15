package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.SerializedSubscriber;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRetryWhen extends AbstractFlowableWithUpstream {
    final Function handler;

    final class RetryWhenSubscriber extends WhenSourceSubscriber {
        private static final long serialVersionUID = -2680129890138081029L;

        RetryWhenSubscriber(Subscriber subscriber, FlowableProcessor flowableProcessor, Subscription subscription) {
            super(subscriber, flowableProcessor, subscription);
        }

        public void onComplete() {
            this.receiver.cancel();
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            again(th);
        }
    }

    public FlowableRetryWhen(Flowable flowable, Function function) {
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
            RetryWhenSubscriber retryWhenSubscriber = new RetryWhenSubscriber(serializedSubscriber, serialized, whenReceiver);
            whenReceiver.subscriber = retryWhenSubscriber;
            subscriber.onSubscribe(retryWhenSubscriber);
            publisher.subscribe(whenReceiver);
            whenReceiver.onNext(Integer.valueOf(0));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
