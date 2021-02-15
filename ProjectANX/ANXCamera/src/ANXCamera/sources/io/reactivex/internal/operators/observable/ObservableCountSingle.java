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

public final class ObservableCountSingle extends Single implements FuseToObservable {
    final ObservableSource source;

    final class CountObserver implements Observer, Disposable {
        final SingleObserver actual;
        long count;
        Disposable d;

        CountObserver(SingleObserver singleObserver) {
            this.actual = singleObserver;
        }

        public void dispose() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(Long.valueOf(this.count));
        }

        public void onError(Throwable th) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.count++;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableCountSingle(ObservableSource observableSource) {
        this.source = observableSource;
    }

    public Observable fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableCount(this.source));
    }

    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new CountObserver(singleObserver));
    }
}
