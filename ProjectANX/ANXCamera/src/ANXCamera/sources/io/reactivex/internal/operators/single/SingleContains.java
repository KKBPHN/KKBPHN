package io.reactivex.internal.operators.single;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;

public final class SingleContains extends io.reactivex.Single {
    final BiPredicate comparer;
    final SingleSource source;
    final Object value;

    final class Single implements SingleObserver {
        private final SingleObserver s;

        Single(SingleObserver singleObserver) {
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
                this.s.onSuccess(Boolean.valueOf(SingleContains.this.comparer.test(obj, SingleContains.this.value)));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.s.onError(th);
            }
        }
    }

    public SingleContains(SingleSource singleSource, Object obj, BiPredicate biPredicate) {
        this.source = singleSource;
        this.value = obj;
        this.comparer = biPredicate;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new Single(singleObserver));
    }
}
