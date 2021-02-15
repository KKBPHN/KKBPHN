package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface MaybeOperator {
    @NonNull
    MaybeObserver apply(@NonNull MaybeObserver maybeObserver);
}
