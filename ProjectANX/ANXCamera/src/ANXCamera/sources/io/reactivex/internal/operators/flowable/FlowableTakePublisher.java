package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableTakePublisher extends Flowable {
    final long limit;
    final Publisher source;

    public FlowableTakePublisher(Publisher publisher, long j) {
        this.source = publisher;
        this.limit = j;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe(new TakeSubscriber(subscriber, this.limit));
    }
}
