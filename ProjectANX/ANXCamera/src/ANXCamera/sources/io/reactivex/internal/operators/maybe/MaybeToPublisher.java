package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;

public enum MaybeToPublisher implements Function {
    INSTANCE;

    public static Function instance() {
        return INSTANCE;
    }

    public Publisher apply(MaybeSource maybeSource) {
        return new MaybeToFlowable(maybeSource);
    }
}
