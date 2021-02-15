package io.reactivex.internal.fuseable;

import io.reactivex.MaybeSource;

public interface HasUpstreamMaybeSource {
    MaybeSource source();
}
