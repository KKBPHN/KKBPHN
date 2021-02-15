package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.ResumeSingleObserver;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleResumeNext extends Single {
    final Function nextFunction;
    final SingleSource source;

    final class ResumeMainSingleObserver extends AtomicReference implements SingleObserver, Disposable {
        private static final long serialVersionUID = -5314538511045349925L;
        final SingleObserver actual;
        final Function nextFunction;

        ResumeMainSingleObserver(SingleObserver singleObserver, Function function) {
            this.actual = singleObserver;
            this.nextFunction = function;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onError(Throwable th) {
            try {
                Object apply = this.nextFunction.apply(th);
                ObjectHelper.requireNonNull(apply, "The nextFunction returned a null SingleSource.");
                ((SingleSource) apply).subscribe(new ResumeSingleObserver(this, this.actual));
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
        }
    }

    public SingleResumeNext(SingleSource singleSource, Function function) {
        this.source = singleSource;
        this.nextFunction = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new ResumeMainSingleObserver(singleObserver, this.nextFunction));
    }
}
