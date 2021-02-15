package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.subscriptions.EmptySubscription;
import org.reactivestreams.Subscriber;

public final class FlowableEmpty extends Flowable implements ScalarCallable {
    public static final Flowable INSTANCE = new FlowableEmpty();

    private FlowableEmpty() {
    }

    public Object call() {
        return null;
    }

    public void subscribeActual(Subscriber subscriber) {
        EmptySubscription.complete(subscriber);
    }
}
