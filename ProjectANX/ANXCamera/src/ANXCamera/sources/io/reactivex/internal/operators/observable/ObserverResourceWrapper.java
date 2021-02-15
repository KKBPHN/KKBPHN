package io.reactivex.internal.operators.observable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class ObserverResourceWrapper extends AtomicReference implements Observer, Disposable {
    private static final long serialVersionUID = -8612022020200669122L;
    final Observer actual;
    final AtomicReference subscription = new AtomicReference();

    public ObserverResourceWrapper(Observer observer) {
        this.actual = observer;
    }

    public void dispose() {
        DisposableHelper.dispose(this.subscription);
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return this.subscription.get() == DisposableHelper.DISPOSED;
    }

    public void onComplete() {
        dispose();
        this.actual.onComplete();
    }

    public void onError(Throwable th) {
        dispose();
        this.actual.onError(th);
    }

    public void onNext(Object obj) {
        this.actual.onNext(obj);
    }

    public void onSubscribe(Disposable disposable) {
        if (DisposableHelper.setOnce(this.subscription, disposable)) {
            this.actual.onSubscribe(this);
        }
    }

    public void setResource(Disposable disposable) {
        DisposableHelper.set(this, disposable);
    }
}
