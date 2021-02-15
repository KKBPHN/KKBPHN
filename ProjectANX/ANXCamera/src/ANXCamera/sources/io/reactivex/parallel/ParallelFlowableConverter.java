package io.reactivex.parallel;

import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;

@Experimental
public interface ParallelFlowableConverter {
    @NonNull
    Object apply(@NonNull ParallelFlowable parallelFlowable);
}
