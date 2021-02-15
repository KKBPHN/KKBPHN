package io.reactivex.functions;

import io.reactivex.annotations.NonNull;

public interface BiPredicate {
    boolean test(@NonNull Object obj, @NonNull Object obj2);
}
