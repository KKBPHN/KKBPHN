package io.reactivex.functions;

import io.reactivex.annotations.NonNull;

public interface BiFunction {
    @NonNull
    Object apply(@NonNull Object obj, @NonNull Object obj2);
}
