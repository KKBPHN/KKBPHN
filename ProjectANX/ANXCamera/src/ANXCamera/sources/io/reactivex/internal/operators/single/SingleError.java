package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class SingleError extends Single {
    final Callable errorSupplier;

    public SingleError(Callable callable) {
        this.errorSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        try {
            Object call = this.errorSupplier.call();
            ObjectHelper.requireNonNull(call, "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
            th = (Throwable) call;
        } catch (Throwable th) {
            th = th;
            Exceptions.throwIfFatal(th);
        }
        EmptyDisposable.error(th, singleObserver);
    }
}
