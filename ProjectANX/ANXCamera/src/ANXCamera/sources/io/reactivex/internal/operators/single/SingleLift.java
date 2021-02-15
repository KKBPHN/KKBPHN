package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOperator;
import io.reactivex.SingleSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;

public final class SingleLift extends Single {
    final SingleOperator onLift;
    final SingleSource source;

    public SingleLift(SingleSource singleSource, SingleOperator singleOperator) {
        this.source = singleSource;
        this.onLift = singleOperator;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        try {
            SingleObserver apply = this.onLift.apply(singleObserver);
            ObjectHelper.requireNonNull(apply, "The onLift returned a null SingleObserver");
            this.source.subscribe(apply);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, singleObserver);
        }
    }
}
