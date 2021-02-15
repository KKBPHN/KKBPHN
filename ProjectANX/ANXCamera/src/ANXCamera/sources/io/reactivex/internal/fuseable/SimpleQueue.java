package io.reactivex.internal.fuseable;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface SimpleQueue {
    void clear();

    boolean isEmpty();

    boolean offer(@NonNull Object obj);

    boolean offer(@NonNull Object obj, @NonNull Object obj2);

    @Nullable
    Object poll();
}
