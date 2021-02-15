package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.ObserverFullArbiter;

public final class FullArbiterObserver implements Observer {
    final ObserverFullArbiter arbiter;
    Disposable s;

    public FullArbiterObserver(ObserverFullArbiter observerFullArbiter) {
        this.arbiter = observerFullArbiter;
    }

    public void onComplete() {
        this.arbiter.onComplete(this.s);
    }

    public void onError(Throwable th) {
        this.arbiter.onError(th, this.s);
    }

    public void onNext(Object obj) {
        this.arbiter.onNext(obj, this.s);
    }

    public void onSubscribe(Disposable disposable) {
        if (DisposableHelper.validate(this.s, disposable)) {
            this.s = disposable;
            this.arbiter.setDisposable(disposable);
        }
    }
}
