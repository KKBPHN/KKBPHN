package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposables;

public final class SingleJust extends Single {
    final Object value;

    public SingleJust(Object obj) {
        this.value = obj;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        singleObserver.onSubscribe(Disposables.disposed());
        singleObserver.onSuccess(this.value);
    }
}
