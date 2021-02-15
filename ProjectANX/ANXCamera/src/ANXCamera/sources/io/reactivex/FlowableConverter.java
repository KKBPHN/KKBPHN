package io.reactivex;

import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;

@Experimental
public interface FlowableConverter {
    @NonNull
    Object apply(@NonNull Flowable flowable);
}
