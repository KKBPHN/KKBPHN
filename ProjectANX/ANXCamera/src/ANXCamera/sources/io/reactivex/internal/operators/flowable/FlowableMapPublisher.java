package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableMapPublisher extends Flowable {
    final Function mapper;
    final Publisher source;

    public FlowableMapPublisher(Publisher publisher, Function function) {
        this.source = publisher;
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe(new MapSubscriber(subscriber, this.mapper));
    }
}
