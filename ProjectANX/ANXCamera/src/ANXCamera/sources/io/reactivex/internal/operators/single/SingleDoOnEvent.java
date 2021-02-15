package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;

public final class SingleDoOnEvent extends Single {
    final BiConsumer onEvent;
    final SingleSource source;

    final class DoOnEvent implements SingleObserver {
        private final SingleObserver s;

        DoOnEvent(SingleObserver singleObserver) {
            this.s = singleObserver;
        }

        public void onError(Throwable th) {
            try {
                SingleDoOnEvent.this.onEvent.accept(null, th);
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
            try {
                SingleDoOnEvent.this.onEvent.accept(obj, null);
                this.s.onSuccess(obj);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.s.onError(th);
            }
        }
    }

    public SingleDoOnEvent(SingleSource singleSource, BiConsumer biConsumer) {
        this.source = singleSource;
        this.onEvent = biConsumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new DoOnEvent(singleObserver));
    }
}
