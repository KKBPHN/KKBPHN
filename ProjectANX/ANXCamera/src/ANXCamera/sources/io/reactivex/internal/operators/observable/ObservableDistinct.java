package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicFuseableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.concurrent.Callable;

public final class ObservableDistinct extends AbstractObservableWithUpstream {
    final Callable collectionSupplier;
    final Function keySelector;

    final class DistinctObserver extends BasicFuseableObserver {
        final Collection collection;
        final Function keySelector;

        DistinctObserver(Observer observer, Function function, Collection collection2) {
            super(observer);
            this.keySelector = function;
            this.collection = collection2;
        }

        public void clear() {
            this.collection.clear();
            super.clear();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.collection.clear();
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.collection.clear();
            this.actual.onError(th);
        }

        public void onNext(Object obj) {
            Observer observer;
            if (!this.done) {
                if (this.sourceMode == 0) {
                    try {
                        Object apply = this.keySelector.apply(obj);
                        ObjectHelper.requireNonNull(apply, "The keySelector returned a null key");
                        if (this.collection.add(apply)) {
                            observer = this.actual;
                        }
                    } catch (Throwable th) {
                        fail(th);
                        return;
                    }
                } else {
                    observer = this.actual;
                    obj = null;
                }
                observer.onNext(obj);
            }
        }

        @Nullable
        public Object poll() {
            Object poll;
            Collection collection2;
            Object apply;
            do {
                poll = this.qs.poll();
                if (poll == null) {
                    break;
                }
                collection2 = this.collection;
                apply = this.keySelector.apply(poll);
                ObjectHelper.requireNonNull(apply, "The keySelector returned a null key");
            } while (!collection2.add(apply));
            return poll;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }
    }

    public ObservableDistinct(ObservableSource observableSource, Function function, Callable callable) {
        super(observableSource);
        this.keySelector = function;
        this.collectionSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer observer) {
        try {
            Object call = this.collectionSupplier.call();
            ObjectHelper.requireNonNull(call, "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
            this.source.subscribe(new DistinctObserver(observer, this.keySelector, (Collection) call));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }
}
