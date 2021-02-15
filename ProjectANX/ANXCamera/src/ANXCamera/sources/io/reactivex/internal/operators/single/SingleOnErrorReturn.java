package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;

public final class SingleOnErrorReturn extends Single {
    final SingleSource source;
    final Object value;
    final Function valueSupplier;

    final class OnErrorReturn implements SingleObserver {
        private final SingleObserver observer;

        OnErrorReturn(SingleObserver singleObserver) {
            this.observer = singleObserver;
        }

        public void onError(Throwable th) {
            Object obj;
            SingleOnErrorReturn singleOnErrorReturn = SingleOnErrorReturn.this;
            Function function = singleOnErrorReturn.valueSupplier;
            if (function != null) {
                try {
                    obj = function.apply(th);
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.observer.onError(new CompositeException(th, th2));
                    return;
                }
            } else {
                obj = singleOnErrorReturn.value;
            }
            if (obj == null) {
                NullPointerException nullPointerException = new NullPointerException("Value supplied was null");
                nullPointerException.initCause(th);
                this.observer.onError(nullPointerException);
                return;
            }
            this.observer.onSuccess(obj);
        }

        public void onSubscribe(Disposable disposable) {
            this.observer.onSubscribe(disposable);
        }

        public void onSuccess(Object obj) {
            this.observer.onSuccess(obj);
        }
    }

    public SingleOnErrorReturn(SingleSource singleSource, Function function, Object obj) {
        this.source = singleSource;
        this.valueSupplier = function;
        this.value = obj;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new OnErrorReturn(singleObserver));
    }
}
