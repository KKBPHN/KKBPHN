package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;

public final class ObservableElementAtSingle extends Single implements FuseToObservable {
    final Object defaultValue;
    final long index;
    final ObservableSource source;

    final class ElementAtObserver implements Observer, Disposable {
        final SingleObserver actual;
        long count;
        final Object defaultValue;
        boolean done;
        final long index;
        Disposable s;

        ElementAtObserver(SingleObserver singleObserver, long j, Object obj) {
            this.actual = singleObserver;
            this.index = j;
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
                Object obj = this.defaultValue;
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
                long j = this.count;
                if (j == this.index) {
                    this.done = true;
                    this.s.dispose();
                    this.actual.onSuccess(obj);
                    return;
                }
                this.count = j + 1;
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableElementAtSingle(ObservableSource observableSource, long j, Object obj) {
        this.source = observableSource;
        this.index = j;
        this.defaultValue = obj;
    }

    public Observable fuseToObservable() {
        ObservableElementAt observableElementAt = new ObservableElementAt(this.source, this.index, this.defaultValue, true);
        return RxJavaPlugins.onAssembly((Observable) observableElementAt);
    }

    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new ElementAtObserver(singleObserver, this.index, this.defaultValue));
    }
}
