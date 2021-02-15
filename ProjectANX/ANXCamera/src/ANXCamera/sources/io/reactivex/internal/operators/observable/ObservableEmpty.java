package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.fuseable.ScalarCallable;

public final class ObservableEmpty extends Observable implements ScalarCallable {
    public static final Observable INSTANCE = new ObservableEmpty();

    private ObservableEmpty() {
    }

    public Object call() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        EmptyDisposable.complete(observer);
    }
}
