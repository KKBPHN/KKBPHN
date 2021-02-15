package io.reactivex.parallel;

import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;

@Experimental
public interface ParallelTransformer {
    @NonNull
    ParallelFlowable apply(@NonNull ParallelFlowable parallelFlowable);
}
