package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class ObservableMapNotification extends AbstractObservableWithUpstream {
    final Callable onCompleteSupplier;
    final Function onErrorMapper;
    final Function onNextMapper;

    final class MapNotificationObserver implements Observer, Disposable {
        final Observer actual;
        final Callable onCompleteSupplier;
        final Function onErrorMapper;
        final Function onNextMapper;
        Disposable s;

        MapNotificationObserver(Observer observer, Function function, Function function2, Callable callable) {
            this.actual = observer;
            this.onNextMapper = function;
            this.onErrorMapper = function2;
            this.onCompleteSupplier = callable;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            try {
                Object call = this.onCompleteSupplier.call();
                ObjectHelper.requireNonNull(call, "The onComplete ObservableSource returned is null");
                this.actual.onNext((ObservableSource) call);
                this.actual.onComplete();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }

        public void onError(Throwable th) {
            try {
                Object apply = this.onErrorMapper.apply(th);
                ObjectHelper.requireNonNull(apply, "The onError ObservableSource returned is null");
                this.actual.onNext((ObservableSource) apply);
                this.actual.onComplete();
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
            }
        }

        public void onNext(Object obj) {
            try {
                Object apply = this.onNextMapper.apply(obj);
                ObjectHelper.requireNonNull(apply, "The onNext ObservableSource returned is null");
                this.actual.onNext((ObservableSource) apply);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.actual.onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableMapNotification(ObservableSource observableSource, Function function, Function function2, Callable callable) {
        super(observableSource);
        this.onNextMapper = function;
        this.onErrorMapper = function2;
        this.onCompleteSupplier = callable;
    }

    public void subscribeActual(Observer observer) {
        this.source.subscribe(new MapNotificationObserver(observer, this.onNextMapper, this.onErrorMapper, this.onCompleteSupplier));
    }
}
