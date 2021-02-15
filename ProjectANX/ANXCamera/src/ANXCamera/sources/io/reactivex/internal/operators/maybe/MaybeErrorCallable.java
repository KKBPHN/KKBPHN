package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class MaybeErrorCallable extends Maybe {
    final Callable errorSupplier;

    public MaybeErrorCallable(Callable callable) {
        this.errorSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        maybeObserver.onSubscribe(Disposables.disposed());
        try {
            Object call = this.errorSupplier.call();
            ObjectHelper.requireNonNull(call, "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
            th = (Throwable) call;
        } catch (Throwable th) {
            th = th;
            Exceptions.throwIfFatal(th);
        }
        maybeObserver.onError(th);
    }
}
