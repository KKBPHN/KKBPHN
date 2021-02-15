package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.internal.disposables.EmptyDisposable;

public final class SingleNever extends Single {
    public static final Single INSTANCE = new SingleNever();

    private SingleNever() {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        singleObserver.onSubscribe(EmptyDisposable.NEVER);
    }
}
