package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;

public final class SingleDoOnError extends Single {
    final Consumer onError;
    final SingleSource source;

    final class DoOnError implements SingleObserver {
        private final SingleObserver s;

        DoOnError(SingleObserver singleObserver) {
            this.s = singleObserver;
        }

        public void onError(Throwable th) {
            try {
                SingleDoOnError.this.onError.accept(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                th = new CompositeException(th, th2);
            }
            this.s.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.s.onSubscribe(disposable);
        }

        public void onSuccess(Object obj) {
            this.s.onSuccess(obj);
        }
    }

    public SingleDoOnError(SingleSource singleSource, Consumer consumer) {
        this.source = singleSource;
        this.onError = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new DoOnError(singleObserver));
    }
}
