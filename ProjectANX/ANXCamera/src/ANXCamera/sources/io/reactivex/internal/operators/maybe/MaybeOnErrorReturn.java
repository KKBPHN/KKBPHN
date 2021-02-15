package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;

public final class MaybeOnErrorReturn extends AbstractMaybeWithUpstream {
    final Function valueSupplier;

    final class OnErrorReturnMaybeObserver implements MaybeObserver, Disposable {
        final MaybeObserver actual;
        Disposable d;
        final Function valueSupplier;

        OnErrorReturnMaybeObserver(MaybeObserver maybeObserver, Function function) {
            this.actual = maybeObserver;
            this.valueSupplier = function;
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            try {
                Object apply = this.valueSupplier.apply(th);
                ObjectHelper.requireNonNull(apply, "The valueSupplier returned a null value");
                this.actual.onSuccess(apply);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
        }
    }

    public MaybeOnErrorReturn(MaybeSource maybeSource, Function function) {
        super(maybeSource);
        this.valueSupplier = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new OnErrorReturnMaybeObserver(maybeObserver, this.valueSupplier));
    }
}
