package io.reactivex.internal.observers;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observers.LambdaConsumerIntrospection;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class ConsumerSingleObserver extends AtomicReference implements SingleObserver, Disposable, LambdaConsumerIntrospection {
    private static final long serialVersionUID = -7012088219455310787L;
    final Consumer onError;
    final Consumer onSuccess;

    public ConsumerSingleObserver(Consumer consumer, Consumer consumer2) {
        this.onSuccess = consumer;
        this.onError = consumer2;
    }

    public void dispose() {
        DisposableHelper.dispose(this);
    }

    public boolean hasCustomOnError() {
        return this.onError != Functions.ON_ERROR_MISSING;
    }

    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    public void onError(Throwable th) {
        lazySet(DisposableHelper.DISPOSED);
        try {
            this.onError.accept(th);
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            RxJavaPlugins.onError(new CompositeException(th, th2));
        }
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.setOnce(this, disposable);
    }

    public void onSuccess(Object obj) {
        lazySet(DisposableHelper.DISPOSED);
        try {
            this.onSuccess.accept(obj);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
        }
    }
}
