package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscriber;

public final class FlowableAutoConnect extends Flowable {
    final AtomicInteger clients = new AtomicInteger();
    final Consumer connection;
    final int numberOfSubscribers;
    final ConnectableFlowable source;

    public FlowableAutoConnect(ConnectableFlowable connectableFlowable, int i, Consumer consumer) {
        this.source = connectableFlowable;
        this.numberOfSubscribers = i;
        this.connection = consumer;
    }

    public void subscribeActual(Subscriber subscriber) {
        this.source.subscribe(subscriber);
        if (this.clients.incrementAndGet() == this.numberOfSubscribers) {
            this.source.connect(this.connection);
        }
    }
}
