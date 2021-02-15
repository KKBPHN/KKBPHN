package io.reactivex.functions;

import io.reactivex.annotations.NonNull;

public interface Function3 {
    @NonNull
    Object apply(@NonNull Object obj, @NonNull Object obj2, @NonNull Object obj3);
}
