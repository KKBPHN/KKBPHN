package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;

public final class ObservableSingleSingle extends Single {
    final Object defaultValue;
    final ObservableSource source;

    final class SingleElementObserver implements Observer, Disposable {
        final SingleObserver actual;
        final Object defaultValue;
        boolean done;
        Disposable s;
        Object value;

        SingleElementObserver(SingleObserver singleObserver, Object obj) {
            this.actual = singleObserver;
            this.defaultValue = obj;
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
                Object obj = this.value;
                this.value = null;
                if (obj == null) {
                    obj = this.defaultValue;
                }
                SingleObserver singleObserver = this.actual;
                if (obj != null) {
                    singleObserver.onSuccess(obj);
                } else {
                    singleObserver.onError(new NoSuchElementException());
                }
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
                if (this.value != null) {
                    this.done = true;
                    this.s.dispose();
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = obj;
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableSingleSingle(ObservableSource observableSource, Object obj) {
        this.source = observableSource;
        this.defaultValue = obj;
    }

    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new SingleElementObserver(singleObserver, this.defaultValue));
    }
}
