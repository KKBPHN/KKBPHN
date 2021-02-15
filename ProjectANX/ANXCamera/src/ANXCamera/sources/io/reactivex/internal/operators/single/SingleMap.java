package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;

public final class SingleMap extends Single {
    final Function mapper;
    final SingleSource source;

    final class MapSingleObserver implements SingleObserver {
        final Function mapper;
        final SingleObserver t;

        MapSingleObserver(SingleObserver singleObserver, Function function) {
            this.t = singleObserver;
            this.mapper = function;
        }

        public void onError(Throwable th) {
            this.t.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.t.onSubscribe(disposable);
        }

        public void onSuccess(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The mapper function returned a null value.");
                this.t.onSuccess(apply);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                onError(th);
            }
        }
    }

    public SingleMap(SingleSource singleSource, Function function) {
        this.source = singleSource;
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new MapSingleObserver(singleObserver, this.mapper));
    }
}
