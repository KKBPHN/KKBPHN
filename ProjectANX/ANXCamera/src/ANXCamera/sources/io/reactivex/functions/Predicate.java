package io.reactivex.functions;

import io.reactivex.annotations.NonNull;

public interface Predicate {
    boolean test(@NonNull Object obj);
}
