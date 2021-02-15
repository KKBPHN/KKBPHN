package io.reactivex.internal.observers;

import io.reactivex.internal.fuseable.QueueDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BasicIntQueueDisposable extends AtomicInteger implements QueueDisposable {
    private static final long serialVersionUID = -1001730202384742097L;

    public final boolean offer(Object obj) {
        throw new UnsupportedOperationException("Should not be called");
    }

    public final boolean offer(Object obj, Object obj2) {
        throw new UnsupportedOperationException("Should not be called");
    }
}
