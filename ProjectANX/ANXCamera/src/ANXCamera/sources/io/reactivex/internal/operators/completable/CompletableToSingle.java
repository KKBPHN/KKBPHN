package io.reactivex.internal.operators.completable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import java.util.concurrent.Callable;

public final class CompletableToSingle extends Single {
    final Object completionValue;
    final Callable completionValueSupplier;
    final CompletableSource source;

    final class ToSingle implements CompletableObserver {
        private final SingleObserver observer;

        ToSingle(SingleObserver singleObserver) {
            this.observer = singleObserver;
        }

        public void onComplete() {
            Object obj;
            CompletableToSingle completableToSingle = CompletableToSingle.this;
            Callable callable = completableToSingle.completionValueSupplier;
            if (callable != null) {
                try {
                    obj = callable.call();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.observer.onError(th);
                    return;
                }
            } else {
                obj = completableToSingle.completionValue;
            }
            SingleObserver singleObserver = this.observer;
            if (obj == null) {
                singleObserver.onError(new NullPointerException("The value supplied is null"));
            } else {
                singleObserver.onSuccess(obj);
            }
        }

        public void onError(Throwable th) {
            this.observer.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.observer.onSubscribe(disposable);
        }
    }

    public CompletableToSingle(CompletableSource completableSource, Callable callable, Object obj) {
        this.source = completableSource;
        this.completionValue = obj;
        this.completionValueSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new ToSingle(singleObserver));
    }
}
