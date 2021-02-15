package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class MaybeDefer extends Maybe {
    final Callable maybeSupplier;

    public MaybeDefer(Callable callable) {
        this.maybeSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        try {
            Object call = this.maybeSupplier.call();
            ObjectHelper.requireNonNull(call, "The maybeSupplier returned a null MaybeSource");
            ((MaybeSource) call).subscribe(maybeObserver);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, maybeObserver);
        }
    }
}
