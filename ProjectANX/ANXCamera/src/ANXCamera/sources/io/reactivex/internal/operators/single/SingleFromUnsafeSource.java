package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;

public final class SingleFromUnsafeSource extends Single {
    final SingleSource source;

    public SingleFromUnsafeSource(SingleSource singleSource) {
        this.source = singleSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(singleObserver);
    }
}
