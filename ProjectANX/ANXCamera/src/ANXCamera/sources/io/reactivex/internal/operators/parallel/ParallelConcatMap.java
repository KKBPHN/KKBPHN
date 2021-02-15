package io.reactivex.internal.operators.parallel;

import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.flowable.FlowableConcatMap;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.parallel.ParallelFlowable;
import org.reactivestreams.Subscriber;

public final class ParallelConcatMap extends ParallelFlowable {
    final ErrorMode errorMode;
    final Function mapper;
    final int prefetch;
    final ParallelFlowable source;

    public ParallelConcatMap(ParallelFlowable parallelFlowable, Function function, int i, ErrorMode errorMode2) {
        this.source = parallelFlowable;
        ObjectHelper.requireNonNull(function, "mapper");
        this.mapper = function;
        this.prefetch = i;
        ObjectHelper.requireNonNull(errorMode2, "errorMode");
        this.errorMode = errorMode2;
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    public void subscribe(Subscriber[] subscriberArr) {
        if (validate(subscriberArr)) {
            int length = subscriberArr.length;
            Subscriber[] subscriberArr2 = new Subscriber[length];
            for (int i = 0; i < length; i++) {
                subscriberArr2[i] = FlowableConcatMap.subscribe(subscriberArr[i], this.mapper, this.prefetch, this.errorMode);
            }
            this.source.subscribe(subscriberArr2);
        }
    }
}
