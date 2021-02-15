package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableUsing extends Observable {
    final Consumer disposer;
    final boolean eager;
    final Callable resourceSupplier;
    final Function sourceSupplier;

    final class UsingObserver extends AtomicBoolean implements Observer, Disposable {
        private static final long serialVersionUID = 5904473792286235046L;
        final Observer actual;
        final Consumer disposer;
        final boolean eager;
        final Object resource;
        Disposable s;

        UsingObserver(Observer observer, Object obj, Consumer consumer, boolean z) {
            this.actual = observer;
            this.resource = obj;
            this.disposer = consumer;
            this.eager = z;
        }

        public void dispose() {
            disposeAfter();
            this.s.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void disposeAfter() {
            if (compareAndSet(false, true)) {
                try {
                    this.disposer.accept(this.resource);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }

        public boolean isDisposed() {
            return get();
        }

        public void onComplete() {
            if (this.eager) {
                if (compareAndSet(false, true)) {
                    try {
                        this.disposer.accept(this.resource);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.actual.onError(th);
                        return;
                    }
                }
                this.s.dispose();
                this.actual.onComplete();
            } else {
                this.actual.onComplete();
                this.s.dispose();
                disposeAfter();
            }
        }

        public void onError(Throwable th) {
            if (this.eager) {
                if (compareAndSet(false, true)) {
                    try {
                        this.disposer.accept(this.resource);
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        th = new CompositeException(th, th2);
                    }
                }
                this.s.dispose();
                this.actual.onError(th);
                return;
            }
            this.actual.onError(th);
            this.s.dispose();
            disposeAfter();
        }

        public void onNext(Object obj) {
            this.actual.onNext(obj);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableUsing(Callable callable, Function function, Consumer consumer, boolean z) {
        this.resourceSupplier = callable;
        this.sourceSupplier = function;
        this.disposer = consumer;
        this.eager = z;
    }

    public void subscribeActual(Observer observer) {
        try {
            Object call = this.resourceSupplier.call();
            try {
                Object apply = this.sourceSupplier.apply(call);
                ObjectHelper.requireNonNull(apply, "The sourceSupplier returned a null ObservableSource");
                ((ObservableSource) apply).subscribe(new UsingObserver(observer, call, this.disposer, this.eager));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptyDisposable.error((Throwable) new CompositeException(th, th), observer);
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            EmptyDisposable.error(th2, observer);
        }
    }
}
