package io.reactivex.internal.operators.observable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableIgnoreElementsCompletable extends Completable implements FuseToObservable {
    final ObservableSource source;

    final class IgnoreObservable implements Observer, Disposable {
        final CompletableObserver actual;
        Disposable d;

        IgnoreObservable(CompletableObserver completableObserver) {
            this.actual = completableObserver;
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
        }

        public void onSubscribe(Disposable disposable) {
            this.d = disposable;
            this.actual.onSubscribe(this);
        }
    }

    public ObservableIgnoreElementsCompletable(ObservableSource observableSource) {
        this.source = observableSource;
    }

    public Observable fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableIgnoreElements(this.source));
    }

    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new IgnoreObservable(completableObserver));
    }
}
