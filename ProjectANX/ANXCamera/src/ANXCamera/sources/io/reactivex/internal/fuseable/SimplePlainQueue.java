package io.reactivex.internal.fuseable;

import io.reactivex.annotations.Nullable;

public interface SimplePlainQueue extends SimpleQueue {
    @Nullable
    Object poll();
}
