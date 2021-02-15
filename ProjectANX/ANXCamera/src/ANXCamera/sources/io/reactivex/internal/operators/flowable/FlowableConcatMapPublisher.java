package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.ErrorMode;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableConcatMapPublisher extends Flowable {
    final ErrorMode errorMode;
    final Function mapper;
    final int prefetch;
    final Publisher source;

    public FlowableConcatMapPublisher(Publisher publisher, Function function, int i, ErrorMode errorMode2) {
        this.source = publisher;
        this.mapper = function;
        this.prefetch = i;
        this.errorMode = errorMode2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber subscriber) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, subscriber, this.mapper)) {
            this.source.subscribe(FlowableConcatMap.subscribe(subscriber, this.mapper, this.prefetch, this.errorMode));
        }
    }
}
