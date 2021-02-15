package io.reactivex.internal.operators.parallel;

import io.reactivex.parallel.ParallelFlowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class ParallelFromArray extends ParallelFlowable {
    final Publisher[] sources;

    public ParallelFromArray(Publisher[] publisherArr) {
        this.sources = publisherArr;
    }

    public int parallelism() {
        return this.sources.length;
    }

    public void subscribe(Subscriber[] subscriberArr) {
        if (validate(subscriberArr)) {
            int length = subscriberArr.length;
            for (int i = 0; i < length; i++) {
                this.sources[i].subscribe(subscriberArr[i]);
            }
        }
    }
}
