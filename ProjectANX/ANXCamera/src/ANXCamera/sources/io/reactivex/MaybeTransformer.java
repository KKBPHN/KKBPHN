package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface MaybeTransformer {
    @NonNull
    MaybeSource apply(@NonNull Maybe maybe);
}
