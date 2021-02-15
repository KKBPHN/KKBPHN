package io.reactivex;

import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;

@Experimental
public interface SingleConverter {
    @NonNull
    Object apply(@NonNull Single single);
}
