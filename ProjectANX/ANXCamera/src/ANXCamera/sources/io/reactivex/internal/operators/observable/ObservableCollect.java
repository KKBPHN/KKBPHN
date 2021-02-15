package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class ObservableCollect extends AbstractObservableWithUpstream {
    final BiConsumer collector;
    final Callable initialSupplier;

    final class CollectObserver implements Observer, Disposable {
        final Observer actual;
        final BiConsumer collector;
        boolean done;
        Disposable s;
        final Object u;

        CollectObserver(Observer observer, Object obj, BiConsumer biConsumer) {
            this.actual = observer;
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
                this.actual.onNext(this.u);
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

    public ObservableCollect(ObservableSource observableSource, Callable callable, BiConsumer biConsumer) {
        super(observableSource);
        this.initialSupplier = callable;
        this.collector = biConsumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        try {
            Object call = this.initialSupplier.call();
            ObjectHelper.requireNonNull(call, "The initialSupplier returned a null value");
            this.source.subscribe(new CollectObserver(observer, call, this.collector));
        } catch (Throwable th) {
            EmptyDisposable.error(th, observer);
        }
    }
}
