package io.reactivex.disposables;

import io.reactivex.annotations.NonNull;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

abstract class ReferenceDisposable extends AtomicReference implements Disposable {
    private static final long serialVersionUID = 6537757548749041217L;

    ReferenceDisposable(Object obj) {
        ObjectHelper.requireNonNull(obj, "value is null");
        super(obj);
    }

    public final void dispose() {
        if (get() != null) {
            Object andSet = getAndSet(null);
            if (andSet != null) {
                onDisposed(andSet);
            }
        }
    }

    public final boolean isDisposed() {
        return get() == null;
    }

    public abstract void onDisposed(@NonNull Object obj);
}
