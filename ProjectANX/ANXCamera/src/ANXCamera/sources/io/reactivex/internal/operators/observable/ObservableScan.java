package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableScan extends AbstractObservableWithUpstream {
    final BiFunction accumulator;

    final class ScanObserver implements Observer, Disposable {
        final BiFunction accumulator;
        final Observer actual;
        boolean done;
        Disposable s;
        Object value;

        ScanObserver(Observer observer, BiFunction biFunction) {
            this.actual = observer;
            this.accumulator = biFunction;
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
                Observer observer = this.actual;
                Object obj2 = this.value;
                if (obj2 != null) {
                    try {
                        obj = this.accumulator.apply(obj2, obj);
                        ObjectHelper.requireNonNull(obj, "The value returned by the accumulator is null");
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.s.dispose();
                        onError(th);
                        return;
                    }
                }
                this.value = obj;
                observer.onNext(obj);
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableScan(ObservableSource observableSource, BiFunction biFunction) {
        super(observableSource);
        this.accumulator = biFunction;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new ScanObserver(observer, this.accumulator));
    }
}
