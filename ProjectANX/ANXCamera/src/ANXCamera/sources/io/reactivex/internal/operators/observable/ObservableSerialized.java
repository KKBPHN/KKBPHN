package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.observers.SerializedObserver;

public final class ObservableSerialized extends AbstractObservableWithUpstream {
    public ObservableSerialized(Observable observable) {
        super(observable);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new SerializedObserver(observer));
    }
}
