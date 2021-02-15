package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class SingleDefer extends Single {
    final Callable singleSupplier;

    public SingleDefer(Callable callable) {
        this.singleSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        try {
            Object call = this.singleSupplier.call();
            ObjectHelper.requireNonNull(call, "The singleSupplier returned a null SingleSource");
            ((SingleSource) call).subscribe(singleObserver);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, singleObserver);
        }
    }
}
