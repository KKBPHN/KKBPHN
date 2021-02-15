package io.reactivex.parallel;

import io.reactivex.annotations.Experimental;
import io.reactivex.functions.BiFunction;

@Experimental
public enum ParallelFailureHandling implements BiFunction {
    STOP,
    ERROR,
    SKIP,
    RETRY;

    public ParallelFailureHandling apply(Long l, Throwable th) {
        return this;
    }
}
