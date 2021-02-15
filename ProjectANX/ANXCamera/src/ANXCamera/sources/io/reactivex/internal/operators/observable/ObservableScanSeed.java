package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class ObservableScanSeed extends AbstractObservableWithUpstream {
    final BiFunction accumulator;
    final Callable seedSupplier;

    final class ScanSeedObserver implements Observer, Disposable {
        final BiFunction accumulator;
        final Observer actual;
        boolean done;
        Disposable s;
        Object value;

        ScanSeedObserver(Observer observer, BiFunction biFunction, Object obj) {
            this.actual = observer;
            this.accumulator = biFunction;
            this.value = obj;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (!this.done) {
                try {
                    Object apply = this.accumulator.apply(this.value, obj);
                    ObjectHelper.requireNonNull(apply, "The accumulator returned a null value");
                    this.value = apply;
                    this.actual.onNext(apply);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.s.dispose();
                    onError(th);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
                this.actual.onNext(this.value);
            }
        }
    }

    public ObservableScanSeed(ObservableSource observableSource, Callable callable, BiFunction biFunction) {
        super(observableSource);
        this.accumulator = biFunction;
        this.seedSupplier = callable;
    }

    public void subscribeActual(Observer observer) {
        try {
            Object call = this.seedSupplier.call();
            ObjectHelper.requireNonNull(call, "The seed supplied is null");
            this.source.subscribe(new ScanSeedObserver(observer, this.accumulator, call));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
