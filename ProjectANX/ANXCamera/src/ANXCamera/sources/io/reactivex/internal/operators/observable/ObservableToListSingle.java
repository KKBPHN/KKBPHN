package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.concurrent.Callable;

public final class ObservableToListSingle extends Single implements FuseToObservable {
    final Callable collectionSupplier;
    final ObservableSource source;

    final class ToListObserver implements Observer, Disposable {
        final SingleObserver actual;
        Collection collection;
        Disposable s;

        ToListObserver(SingleObserver singleObserver, Collection collection2) {
            this.actual = singleObserver;
            this.collection = collection2;
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            Collection collection2 = this.collection;
            this.collection = null;
            this.actual.onSuccess(collection2);
        }

        public void onError(Throwable th) {
            this.collection = null;
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            this.collection.add(obj);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableToListSingle(ObservableSource observableSource, int i) {
        this.source = observableSource;
        this.collectionSupplier = Functions.createArrayList(i);
    }

    public ObservableToListSingle(ObservableSource observableSource, Callable callable) {
        this.source = observableSource;
        this.collectionSupplier = callable;
    }

    public Observable fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable) new ObservableToList(this.source, this.collectionSupplier));
    }

    public void subscribeActual(SingleObserver singleObserver) {
        try {
            Object call = this.collectionSupplier.call();
            ObjectHelper.requireNonNull(call, "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
            this.source.subscribe(new ToListObserver(singleObserver, (Collection) call));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, singleObserver);
        }
    }
}
