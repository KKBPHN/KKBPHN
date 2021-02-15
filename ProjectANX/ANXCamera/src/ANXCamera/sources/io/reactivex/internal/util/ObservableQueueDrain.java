package io.reactivex.internal.util;

import io.reactivex.Observer;

public interface ObservableQueueDrain {
    void accept(Observer observer, Object obj);

    boolean cancelled();

    boolean done();

    boolean enter();

    Throwable error();

    int leave(int i);
}
