package io.reactivex.internal.operators.observable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableReduceMaybe extends Maybe {
    final BiFunction reducer;
    final ObservableSource source;

    final class ReduceObserver implements Observer, Disposable {
        final MaybeObserver actual;
        Disposable d;
        boolean done;
        final BiFunction reducer;
        Object value;

        ReduceObserver(MaybeObserver maybeObserver, BiFunction biFunction) {
            this.actual = maybeObserver;
            this.reducer = biFunction;
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Object obj = this.value;
                this.value = null;
                MaybeObserver maybeObserver = this.actual;
                if (obj != null) {
                    maybeObserver.onSuccess(obj);
                } else {
                    maybeObserver.onComplete();
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.value = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                Object obj2 = this.value;
                if (obj2 == null) {
                    this.value = obj;
                    return;
                }
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

    public ObservableReduceMaybe(ObservableSource observableSource, BiFunction biFunction) {
        this.source = observableSource;
        this.reducer = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new ReduceObserver(maybeObserver, this.reducer));
    }
}
