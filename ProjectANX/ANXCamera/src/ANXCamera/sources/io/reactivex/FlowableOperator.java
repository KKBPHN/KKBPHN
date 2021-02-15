package io.reactivex;

import io.reactivex.annotations.NonNull;
import org.reactivestreams.Subscriber;

public interface FlowableOperator {
    @NonNull
    Subscriber apply(@NonNull Subscriber subscriber);
}
