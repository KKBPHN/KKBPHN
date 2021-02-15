package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class CompletableDefer extends Completable {
    final Callable completableSupplier;

    public CompletableDefer(Callable callable) {
        this.completableSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        try {
            Object call = this.completableSupplier.call();
            ObjectHelper.requireNonNull(call, "The completableSupplier returned a null CompletableSource");
            ((CompletableSource) call).subscribe(completableObserver);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, completableObserver);
        }
    }
}
