package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;

public final class MaybeUnsafeCreate extends AbstractMaybeWithUpstream {
    public MaybeUnsafeCreate(MaybeSource maybeSource) {
        super(maybeSource);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(maybeObserver);
    }
}
