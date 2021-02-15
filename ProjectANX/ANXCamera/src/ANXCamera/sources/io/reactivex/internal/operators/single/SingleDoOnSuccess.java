package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;

public final class SingleDoOnSuccess extends Single {
    final Consumer onSuccess;
    final SingleSource source;

    final class DoOnSuccess implements SingleObserver {
        private final SingleObserver s;

        DoOnSuccess(SingleObserver singleObserver) {
            this.s = singleObserver;
        }

        public void onError(Throwable th) {
            this.s.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.s.onSubscribe(disposable);
        }

        public void onSuccess(Object obj) {
            try {
                SingleDoOnSuccess.this.onSuccess.accept(obj);
                this.s.onSuccess(obj);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.s.onError(th);
            }
        }
    }

    public SingleDoOnSuccess(SingleSource singleSource, Consumer consumer) {
        this.source = singleSource;
        this.onSuccess = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new DoOnSuccess(singleObserver));
    }
}
