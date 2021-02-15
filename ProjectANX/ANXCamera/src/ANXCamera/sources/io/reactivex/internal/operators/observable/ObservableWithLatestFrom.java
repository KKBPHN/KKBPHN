package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWithLatestFrom extends AbstractObservableWithUpstream {
    final BiFunction combiner;
    final ObservableSource other;

    final class WithLastFrom implements Observer {
        private final WithLatestFromObserver wlf;

        WithLastFrom(WithLatestFromObserver withLatestFromObserver) {
            this.wlf = withLatestFromObserver;
        }

        public void onComplete() {
        }

        public void onError(Throwable th) {
            this.wlf.otherError(th);
        }

        public void onNext(Object obj) {
            this.wlf.lazySet(obj);
        }

        public void onSubscribe(Disposable disposable) {
            this.wlf.setOther(disposable);
        }
    }

    final class WithLatestFromObserver extends AtomicReference implements Observer, Disposable {
        private static final long serialVersionUID = -312246233408980075L;
        final Observer actual;
        final BiFunction combiner;
        final AtomicReference other = new AtomicReference();
        final AtomicReference s = new AtomicReference();

        WithLatestFromObserver(Observer observer, BiFunction biFunction) {
            this.actual = observer;
            this.combiner = biFunction;
        }

        public void dispose() {
            DisposableHelper.dispose(this.s);
            DisposableHelper.dispose(this.other);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.s.get());
        }

        public void onComplete() {
            DisposableHelper.dispose(this.other);
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.other);
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            Object obj2 = get();
            if (obj2 != null) {
                try {
                    Object apply = this.combiner.apply(obj, obj2);
                    ObjectHelper.requireNonNull(apply, "The combiner returned a null value");
                    this.actual.onNext(apply);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    dispose();
                    this.actual.onError(th);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.s, disposable);
        }

        public void otherError(Throwable th) {
            DisposableHelper.dispose(this.s);
            this.actual.onError(th);
        }

        public boolean setOther(Disposable disposable) {
            return DisposableHelper.setOnce(this.other, disposable);
        }
    }

    public ObservableWithLatestFrom(ObservableSource observableSource, BiFunction biFunction, ObservableSource observableSource2) {
        super(observableSource);
        this.combiner = biFunction;
        this.other = observableSource2;
    }

    public void subscribeActual(Observer observer) {
        SerializedObserver serializedObserver = new SerializedObserver(observer);
        WithLatestFromObserver withLatestFromObserver = new WithLatestFromObserver(serializedObserver, this.combiner);
        serializedObserver.onSubscribe(withLatestFromObserver);
        this.other.subscribe(new WithLastFrom(withLatestFromObserver));
        this.source.subscribe(withLatestFromObserver);
    }
}
