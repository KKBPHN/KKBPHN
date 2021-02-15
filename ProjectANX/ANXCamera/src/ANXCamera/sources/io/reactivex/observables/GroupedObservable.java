package io.reactivex.observables;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;

public abstract class GroupedObservable extends Observable {
    final Object key;

    protected GroupedObservable(@Nullable Object obj) {
        this.key = obj;
    }

    @Nullable
    public Object getKey() {
        return this.key;
    }
}
