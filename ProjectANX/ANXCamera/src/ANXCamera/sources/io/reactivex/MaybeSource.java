package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface MaybeSource {
    void subscribe(@NonNull MaybeObserver maybeObserver);
}
