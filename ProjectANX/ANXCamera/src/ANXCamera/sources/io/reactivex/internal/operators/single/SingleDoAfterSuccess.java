package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

@Experimental
public final class SingleDoAfterSuccess extends Single {
    final Consumer onAfterSuccess;
    final SingleSource source;

    final class DoAfterObserver implements SingleObserver, Disposable {
        final SingleObserver actual;
        Disposable d;
        final Consumer onAfterSuccess;

        DoAfterObserver(SingleObserver singleObserver, Consumer consumer) {
            this.actual = singleObserver;
            this.onAfterSuccess = consumer;
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
            try {
                this.onAfterSuccess.accept(obj);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }
    }

    public SingleDoAfterSuccess(SingleSource singleSource, Consumer consumer) {
        this.source = singleSource;
        this.onAfterSuccess = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new DoAfterObserver(singleObserver, this.onAfterSuccess));
    }
}
