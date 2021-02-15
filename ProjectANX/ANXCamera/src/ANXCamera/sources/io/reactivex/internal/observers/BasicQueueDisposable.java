package io.reactivex.internal.observers;

import io.reactivex.internal.fuseable.QueueDisposable;

public abstract class BasicQueueDisposable implements QueueDisposable {
    public final boolean offer(Object obj) {
        throw new UnsupportedOperationException("Should not be called");
    }

    public final boolean offer(Object obj, Object obj2) {
        throw new UnsupportedOperationException("Should not be called");
    }
}
