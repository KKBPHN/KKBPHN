package io.reactivex.flowables;

import io.reactivex.Flowable;
import io.reactivex.annotations.Nullable;

public abstract class GroupedFlowable extends Flowable {
    final Object key;

    protected GroupedFlowable(@Nullable Object obj) {
        this.key = obj;
    }

    @Nullable
    public Object getKey() {
        return this.key;
    }
}
