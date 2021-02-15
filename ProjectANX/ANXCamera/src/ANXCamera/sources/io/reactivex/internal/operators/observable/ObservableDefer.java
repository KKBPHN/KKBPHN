package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class ObservableDefer extends Observable {
    final Callable supplier;

    public ObservableDefer(Callable callable) {
        this.supplier = callable;
    }

    public void subscribeActual(Observer observer) {
        try {
            Object call = this.supplier.call();
            ObjectHelper.requireNonNull(call, "null ObservableSource supplied");
            ((ObservableSource) call).subscribe(observer);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
