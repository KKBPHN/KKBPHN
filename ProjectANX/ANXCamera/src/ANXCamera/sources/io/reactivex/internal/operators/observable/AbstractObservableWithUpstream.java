package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;

abstract class AbstractObservableWithUpstream extends Observable implements HasUpstreamObservableSource {
    protected final ObservableSource source;

    AbstractObservableWithUpstream(ObservableSource observableSource) {
        this.source = observableSource;
    }

    public final ObservableSource source() {
        return this.source;
    }
}
