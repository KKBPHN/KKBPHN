package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.subscriptions.ScalarSubscription;
import org.reactivestreams.Subscriber;

public final class FlowableJust extends Flowable implements ScalarCallable {
    private final Object value;

    public FlowableJust(Object obj) {
        this.value = obj;
    }

    public Object call() {
        return this.value;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        subscriber.onSubscribe(new ScalarSubscription(subscriber, this.value));
    }
}
