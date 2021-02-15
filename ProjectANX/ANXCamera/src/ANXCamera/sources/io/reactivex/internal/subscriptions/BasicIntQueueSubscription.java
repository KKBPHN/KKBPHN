package io.reactivex.internal.subscriptions;

import io.reactivex.internal.fuseable.QueueSubscription;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BasicIntQueueSubscription extends AtomicInteger implements QueueSubscription {
    private static final long serialVersionUID = -6671519529404341862L;

    public final boolean offer(Object obj) {
        throw new UnsupportedOperationException("Should not be called!");
    }

    public final boolean offer(Object obj, Object obj2) {
        throw new UnsupportedOperationException("Should not be called!");
    }
}
