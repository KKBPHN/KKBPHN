package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.internal.disposables.EmptyDisposable;

public final class ObservableNever extends Observable {
    public static final Observable INSTANCE = new ObservableNever();

    private ObservableNever() {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        observer.onSubscribe(EmptyDisposable.NEVER);
    }
}
