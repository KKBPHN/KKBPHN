package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableFromPublisher extends Flowable {
    final Publisher publisher;

    public FlowableFromPublisher(Publisher publisher2) {
        this.publisher = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.publisher.subscribe(subscriber);
    }
}
