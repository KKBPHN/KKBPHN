package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;

public final class ObservableFromUnsafeSource extends Observable {
    final ObservableSource source;

    public ObservableFromUnsafeSource(ObservableSource observableSource) {
        this.source = observableSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(observer);
    }
}
