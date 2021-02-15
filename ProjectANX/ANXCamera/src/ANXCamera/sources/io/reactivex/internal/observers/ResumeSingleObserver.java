package io.reactivex.internal.observers;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class ResumeSingleObserver implements SingleObserver {
    final SingleObserver actual;
    final AtomicReference parent;

    public ResumeSingleObserver(AtomicReference atomicReference, SingleObserver singleObserver) {
        this.parent = atomicReference;
        this.actual = singleObserver;
    }

    public void onError(Throwable th) {
        this.actual.onError(th);
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.replace(this.parent, disposable);
    }

    public void onSuccess(Object obj) {
        this.actual.onSuccess(obj);
    }
}
