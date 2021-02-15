package io.reactivex.internal.fuseable;

import io.reactivex.ObservableSource;

public interface HasUpstreamObservableSource {
    ObservableSource source();
}
