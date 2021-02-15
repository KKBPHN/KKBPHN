package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
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
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeUsing extends Maybe {
    final boolean eager;
    final Consumer resourceDisposer;
    final Callable resourceSupplier;
    final Function sourceSupplier;

    final class UsingObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = -674404550052917487L;
        final MaybeObserver actual;
        Disposable d;
        final Consumer disposer;
        final boolean eager;

        UsingObserver(MaybeObserver maybeObserver, Object obj, Consumer consumer, boolean z) {
            super(obj);
            this.actual = maybeObserver;
            this.disposer = consumer;
            this.eager = z;
        }

        public void dispose() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
            disposeResourceAfter();
        }

        /* access modifiers changed from: 0000 */
        public void disposeResourceAfter() {
            Object andSet = getAndSet(this);
            if (andSet != this) {
                try {
                    this.disposer.accept(andSet);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.d = DisposableHelper.DISPOSED;
            if (this.eager) {
                Object andSet = getAndSet(this);
                if (andSet != this) {
                    try {
                        this.disposer.accept(andSet);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.actual.onError(th);
                    }
                }
                return;
            }
            this.actual.onComplete();
            if (!this.eager) {
                disposeResourceAfter();
            }
        }

        public void onError(Throwable th) {
            this.d = DisposableHelper.DISPOSED;
            if (this.eager) {
                Object andSet = getAndSet(this);
                if (andSet != this) {
                    try {
                        this.disposer.accept(andSet);
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        th = new CompositeException(th, th2);
                    }
                } else {
                    return;
                }
            }
            this.actual.onError(th);
            if (!this.eager) {
                disposeResourceAfter();
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.d = DisposableHelper.DISPOSED;
            if (this.eager) {
                Object andSet = getAndSet(this);
                if (andSet != this) {
                    try {
                        this.disposer.accept(andSet);
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.actual.onError(th);
                    }
                }
                return;
            }
            this.actual.onSuccess(obj);
            if (!this.eager) {
                disposeResourceAfter();
            }
        }
    }

    public MaybeUsing(Callable callable, Function function, Consumer consumer, boolean z) {
        this.resourceSupplier = callable;
        this.sourceSupplier = function;
        this.resourceDisposer = consumer;
        this.eager = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        try {
            Object call = this.resourceSupplier.call();
            try {
                Object apply = this.sourceSupplier.apply(call);
                ObjectHelper.requireNonNull(apply, "The sourceSupplier returned a null MaybeSource");
                ((MaybeSource) apply).subscribe(new UsingObserver(maybeObserver, call, this.resourceDisposer, this.eager));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            EmptyDisposable.error(th2, maybeObserver);
        }
    }
}
