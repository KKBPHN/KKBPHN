package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposables;
import io.reactivex.internal.fuseable.ScalarCallable;

public final class MaybeJust extends Maybe implements ScalarCallable {
    final Object value;

    public MaybeJust(Object obj) {
        this.value = obj;
    }

    public Object call() {
        return this.value;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        maybeObserver.onSubscribe(Disposables.disposed());
        maybeObserver.onSuccess(this.value);
    }
}
