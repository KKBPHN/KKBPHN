package io.reactivex;

import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;

@Experimental
public interface MaybeConverter {
    @NonNull
    Object apply(@NonNull Maybe maybe);
}
