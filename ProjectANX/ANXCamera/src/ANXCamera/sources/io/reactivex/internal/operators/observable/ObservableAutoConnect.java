package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableAutoConnect extends Observable {
    final AtomicInteger clients = new AtomicInteger();
    final Consumer connection;
    final int numberOfObservers;
    final ConnectableObservable source;

    public ObservableAutoConnect(ConnectableObservable connectableObservable, int i, Consumer consumer) {
        this.source = connectableObservable;
        this.numberOfObservers = i;
        this.connection = consumer;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(observer);
        if (this.clients.incrementAndGet() == this.numberOfObservers) {
            this.source.connect(this.connection);
        }
    }
}
