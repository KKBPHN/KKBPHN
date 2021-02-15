package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.operators.observable.ObservableScalarXMap.ScalarDisposable;

public final class ObservableJust extends Observable implements ScalarCallable {
    private final Object value;

    public ObservableJust(Object obj) {
        this.value = obj;
    }

    public Object call() {
        return this.value;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        ScalarDisposable scalarDisposable = new ScalarDisposable(observer, this.value);
        observer.onSubscribe(scalarDisposable);
        scalarDisposable.run();
    }
}
