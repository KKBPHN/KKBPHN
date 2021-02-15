package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.internal.disposables.EmptyDisposable;

public final class MaybeNever extends Maybe {
    public static final MaybeNever INSTANCE = new MaybeNever();

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        maybeObserver.onSubscribe(EmptyDisposable.NEVER);
    }
}
