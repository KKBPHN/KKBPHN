package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.subscriptions.EmptySubscription;
import org.reactivestreams.Subscriber;

public final class FlowableNever extends Flowable {
    public static final Flowable INSTANCE = new FlowableNever();

    private FlowableNever() {
    }

    public void subscribeActual(Subscriber subscriber) {
        subscriber.onSubscribe(EmptySubscription.INSTANCE);
    }
}
