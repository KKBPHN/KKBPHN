package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import org.reactivestreams.Publisher;

abstract class AbstractFlowableWithUpstream extends Flowable implements HasUpstreamPublisher {
    protected final Flowable source;

    AbstractFlowableWithUpstream(Flowable flowable) {
        ObjectHelper.requireNonNull(flowable, "source is null");
        this.source = flowable;
    }

    public final Publisher source() {
        return this.source;
    }
}
