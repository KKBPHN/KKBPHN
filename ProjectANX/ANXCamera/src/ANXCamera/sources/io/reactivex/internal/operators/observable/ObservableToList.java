package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.Collection;
import java.util.concurrent.Callable;

public final class ObservableToList extends AbstractObservableWithUpstream {
    final Callable collectionSupplier;

    final class ToListObserver implements Observer, Disposable {
        final Observer actual;
        Collection collection;
        Disposable s;

        ToListObserver(Observer observer, Collection collection2) {
            this.actual = observer;
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
            this.actual.onNext(collection2);
            this.actual.onComplete();
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

    public ObservableToList(ObservableSource observableSource, int i) {
        super(observableSource);
        this.collectionSupplier = Functions.createArrayList(i);
    }

    public ObservableToList(ObservableSource observableSource, Callable callable) {
        super(observableSource);
        this.collectionSupplier = callable;
    }

    public void subscribeActual(Observer observer) {
        try {
            Object call = this.collectionSupplier.call();
            ObjectHelper.requireNonNull(call, "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
            this.source.subscribe(new ToListObserver(observer, (Collection) call));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
