package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableReduceSeedSingle extends Single {
    final BiFunction reducer;
    final Object seed;
    final ObservableSource source;

    final class ReduceSeedObserver implements Observer, Disposable {
        final SingleObserver actual;
        Disposable d;
        final BiFunction reducer;
        Object value;

        ReduceSeedObserver(SingleObserver singleObserver, BiFunction biFunction, Object obj) {
            this.actual = singleObserver;
            this.value = obj;
            this.reducer = biFunction;
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            Object obj = this.value;
            this.value = null;
            if (obj != null) {
                this.actual.onSuccess(obj);
            }
        }

        public void onError(Throwable th) {
            Object obj = this.value;
            this.value = null;
            if (obj != null) {
                this.actual.onError(th);
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void onNext(Object obj) {
            Object obj2 = this.value;
            if (obj2 != null) {
                try {
                    Object apply = this.reducer.apply(obj2, obj);
                    ObjectHelper.requireNonNull(apply, "The reducer returned a null value");
                    this.value = apply;
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.d.dispose();
                    onError(th);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableReduceSeedSingle(ObservableSource observableSource, Object obj, BiFunction biFunction) {
        this.source = observableSource;
        this.seed = obj;
        this.reducer = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new ReduceSeedObserver(singleObserver, this.reducer, this.seed));
    }
}
