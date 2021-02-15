package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableFlattenIterable extends AbstractObservableWithUpstream {
    final Function mapper;

    final class FlattenIterableObserver implements Observer, Disposable {
        final Observer actual;
        Disposable d;
        final Function mapper;

        FlattenIterableObserver(Observer observer, Function function) {
            this.actual = observer;
            this.mapper = function;
        }

        public void dispose() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            Disposable disposable = this.d;
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (disposable != disposableHelper) {
                this.d = disposableHelper;
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            Disposable disposable = this.d;
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (disposable == disposableHelper) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.d = disposableHelper;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            if (this.d != DisposableHelper.DISPOSED) {
                try {
                    Observer observer = this.actual;
                    for (Object next : (Iterable) this.mapper.apply(obj)) {
                        ObjectHelper.requireNonNull(next, "The iterator returned a null value");
                        observer.onNext(next);
                    }
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

    public ObservableFlattenIterable(ObservableSource observableSource, Function function) {
        super(observableSource);
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        this.source.subscribe(new FlattenIterableObserver(observer, this.mapper));
    }
}
