package io.reactivex.internal.fuseable;

import io.reactivex.SingleSource;

public interface HasUpstreamSingleSource {
    SingleSource source();
}
