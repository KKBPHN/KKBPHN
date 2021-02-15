package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class ObservableCollectSingle extends Single implements FuseToObservable {
    final BiConsumer collector;
    final Callable initialSupplier;
    final ObservableSource source;

    final class CollectObserver implements Observer, Disposable {
        final SingleObserver actual;
        final BiConsumer collector;
        boolean done;
        Disposable s;
        final Object u;

        CollectObserver(SingleObserver singleObserver, Object obj, BiConsumer biConsumer) {
            this.actual = singleObserver;
            this.collector = biConsumer;
            this.u = obj;
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
                this.actual.onSuccess(this.u);
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
                    this.collector.accept(this.u, obj);
                } catch (Throwable th) {
                    this.s.dispose();
                    onError(th);
                }
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableCollectSingle(ObservableSource observableSource, Callable callable, BiConsumer biConsumer) {
        this.source = observableSource;
        this.initialSupplier = callable;
        this.collector = biConsumer;
    }

    public Observable fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableCollect(this.source, this.initialSupplier, this.collector));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        try {
            Object call = this.initialSupplier.call();
            ObjectHelper.requireNonNull(call, "The initialSupplier returned a null value");
            this.source.subscribe(new CollectObserver(singleObserver, call, this.collector));
        } catch (Throwable th) {
            EmptyDisposable.error(th, singleObserver);
        }
    }
}
