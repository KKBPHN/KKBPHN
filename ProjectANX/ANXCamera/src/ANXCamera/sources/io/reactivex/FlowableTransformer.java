package io.reactivex;

import io.reactivex.annotations.NonNull;
import org.reactivestreams.Publisher;

public interface FlowableTransformer {
    @NonNull
    Publisher apply(@NonNull Flowable flowable);
}
