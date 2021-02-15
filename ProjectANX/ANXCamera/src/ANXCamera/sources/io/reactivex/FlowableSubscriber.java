package io.reactivex;

import io.reactivex.annotations.Beta;
import io.reactivex.annotations.NonNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Beta
public interface FlowableSubscriber extends Subscriber {
    void onSubscribe(@NonNull Subscription subscription);
}
