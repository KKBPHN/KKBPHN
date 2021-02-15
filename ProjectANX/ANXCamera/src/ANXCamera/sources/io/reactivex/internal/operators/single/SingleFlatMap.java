package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleFlatMap extends Single {
    final Function mapper;
    final SingleSource source;

    final class SingleFlatMapCallback extends AtomicReference implements SingleObserver, Disposable {
        private static final long serialVersionUID = 3258103020495908596L;
        final SingleObserver actual;
        final Function mapper;

        final class FlatMapSingleObserver implements SingleObserver {
            final SingleObserver actual;
            final AtomicReference parent;

            FlatMapSingleObserver(AtomicReference atomicReference, SingleObserver singleObserver) {
                this.parent = atomicReference;
                this.actual = singleObserver;
            }

            public void onError(Throwable th) {
                this.actual.onError(th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.replace(this.parent, disposable);
            }

            public void onSuccess(Object obj) {
                this.actual.onSuccess(obj);
            }
        }

        SingleFlatMapCallback(SingleObserver singleObserver, Function function) {
            this.actual = singleObserver;
            this.mapper = function;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            try {
                Object apply = this.mapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The single returned by the mapper is null");
                SingleSource singleSource = (SingleSource) apply;
                if (!isDisposed()) {
                    singleSource.subscribe(new FlatMapSingleObserver(this, this.actual));
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }
    }

    public SingleFlatMap(SingleSource singleSource, Function function) {
        this.mapper = function;
        this.source = singleSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new SingleFlatMapCallback(singleObserver, this.mapper));
    }
}
