package io.reactivex;

import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;

@Experimental
public interface CompletableConverter {
    @NonNull
    Object apply(@NonNull Completable completable);
}
