package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;

public final class ObservableZipIterable extends Observable {
    final Iterable other;
    final Observable source;
    final BiFunction zipper;

    final class ZipIterableObserver implements Observer, Disposable {
        final Observer actual;
        boolean done;
        final Iterator iterator;
        Disposable s;
        final BiFunction zipper;

        ZipIterableObserver(Observer observer, Iterator it, BiFunction biFunction) {
            this.actual = observer;
            this.iterator = it;
            this.zipper = biFunction;
        }

        public void dispose() {
            this.s.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void error(Throwable th) {
            this.done = true;
            this.s.dispose();
            this.actual.onError(th);
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
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
                    Object next = this.iterator.next();
                    ObjectHelper.requireNonNull(next, "The iterator returned a null value");
                    Object apply = this.zipper.apply(obj, next);
                    ObjectHelper.requireNonNull(apply, "The zipper function returned a null value");
                    this.actual.onNext(apply);
                    if (!this.iterator.hasNext()) {
                        this.done = true;
                        this.s.dispose();
                        this.actual.onComplete();
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    error(th);
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

    public ObservableZipIterable(Observable observable, Iterable iterable, BiFunction biFunction) {
        this.source = observable;
        this.other = iterable;
        this.zipper = biFunction;
    }

    public void subscribeActual(Observer observer) {
        try {
            Iterator it = this.other.iterator();
            ObjectHelper.requireNonNull(it, "The iterator returned by other is null");
            Iterator it2 = it;
            try {
                if (!it2.hasNext()) {
                    EmptyDisposable.complete(observer);
                } else {
                    this.source.subscribe((Observer) new ZipIterableObserver(observer, it2, this.zipper));
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptyDisposable.error(th, observer);
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            EmptyDisposable.error(th2, observer);
        }
    }
}
