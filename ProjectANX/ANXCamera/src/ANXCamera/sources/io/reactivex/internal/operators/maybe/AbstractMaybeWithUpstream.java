package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

abstract class AbstractMaybeWithUpstream extends Maybe implements HasUpstreamMaybeSource {
    protected final MaybeSource source;

    AbstractMaybeWithUpstream(MaybeSource maybeSource) {
        this.source = maybeSource;
    }

    public final MaybeSource source() {
        return this.source;
    }
}
