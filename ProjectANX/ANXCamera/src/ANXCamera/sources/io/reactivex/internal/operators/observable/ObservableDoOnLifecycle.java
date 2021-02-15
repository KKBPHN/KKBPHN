package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.DisposableLambdaObserver;

public final class ObservableDoOnLifecycle extends AbstractObservableWithUpstream {
    private final Action onDispose;
    private final Consumer onSubscribe;

    public ObservableDoOnLifecycle(Observable observable, Consumer consumer, Action action) {
        super(observable);
        this.onSubscribe = consumer;
        this.onDispose = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new DisposableLambdaObserver(observer, this.onSubscribe, this.onDispose));
    }
}
