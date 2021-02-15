package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.ErrorMode;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableConcatMapEagerPublisher extends Flowable {
    final ErrorMode errorMode;
    final Function mapper;
    final int maxConcurrency;
    final int prefetch;
    final Publisher source;

    public FlowableConcatMapEagerPublisher(Publisher publisher, Function function, int i, int i2, ErrorMode errorMode2) {
        this.source = publisher;
        this.mapper = function;
        this.maxConcurrency = i;
        this.prefetch = i2;
        this.errorMode = errorMode2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        Publisher publisher = this.source;
        ConcatMapEagerDelayErrorSubscriber concatMapEagerDelayErrorSubscriber = new ConcatMapEagerDelayErrorSubscriber(subscriber, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode);
        publisher.subscribe(concatMapEagerDelayErrorSubscriber);
    }
}
