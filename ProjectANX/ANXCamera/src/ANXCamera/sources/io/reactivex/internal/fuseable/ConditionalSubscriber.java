package io.reactivex.internal.fuseable;

import io.reactivex.FlowableSubscriber;

public interface ConditionalSubscriber extends FlowableSubscriber {
    boolean tryOnNext(Object obj);
}
