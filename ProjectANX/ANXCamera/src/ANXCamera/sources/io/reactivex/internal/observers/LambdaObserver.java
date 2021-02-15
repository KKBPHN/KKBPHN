package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observers.LambdaConsumerIntrospection;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class LambdaObserver extends AtomicReference implements Observer, Disposable, LambdaConsumerIntrospection {
    private static final long serialVersionUID = -7251123623727029452L;
    final Action onComplete;
    final Consumer onError;
    final Consumer onNext;
    final Consumer onSubscribe;

    public LambdaObserver(Consumer consumer, Consumer consumer2, Action action, Consumer consumer3) {
        this.onNext = consumer;
        this.onError = consumer2;
        this.onComplete = action;
        this.onSubscribe = consumer3;
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

    public void onComplete() {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                this.onComplete.run();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }
    }

    public void onError(Throwable th) {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                this.onError.accept(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                RxJavaPlugins.onError(new CompositeException(th, th2));
            }
        }
    }

    public void onNext(Object obj) {
        if (!isDisposed()) {
            try {
                this.onNext.accept(obj);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                ((Disposable) get()).dispose();
                onError(th);
            }
        }
    }

    public void onSubscribe(Disposable disposable) {
        if (DisposableHelper.setOnce(this, disposable)) {
            try {
                this.onSubscribe.accept(this);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                disposable.dispose();
                onError(th);
            }
        }
    }
}
