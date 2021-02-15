package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class ObservableReduceWithSingle extends Single {
    final BiFunction reducer;
    final Callable seedSupplier;
    final ObservableSource source;

    public ObservableReduceWithSingle(ObservableSource observableSource, Callable callable, BiFunction biFunction) {
        this.source = observableSource;
        this.seedSupplier = callable;
        this.reducer = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        try {
            Object call = this.seedSupplier.call();
            ObjectHelper.requireNonNull(call, "The seedSupplier returned a null value");
            this.source.subscribe(new ReduceSeedObserver(singleObserver, this.reducer, call));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, singleObserver);
        }
    }
}
