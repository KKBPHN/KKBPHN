package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeOnErrorNext extends AbstractMaybeWithUpstream {
    final boolean allowFatal;
    final Function resumeFunction;

    final class OnErrorNextMaybeObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = 2026620218879969836L;
        final MaybeObserver actual;
        final boolean allowFatal;
        final Function resumeFunction;

        final class NextMaybeObserver implements MaybeObserver {
            final MaybeObserver actual;
            final AtomicReference d;

            NextMaybeObserver(MaybeObserver maybeObserver, AtomicReference atomicReference) {
                this.actual = maybeObserver;
                this.d = atomicReference;
            }

            public void onComplete() {
                this.actual.onComplete();
            }

            public void onError(Throwable th) {
                this.actual.onError(th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this.d, disposable);
            }

            public void onSuccess(Object obj) {
                this.actual.onSuccess(obj);
            }
        }

        OnErrorNextMaybeObserver(MaybeObserver maybeObserver, Function function, boolean z) {
            this.actual = maybeObserver;
            this.resumeFunction = function;
            this.allowFatal = z;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            if (this.allowFatal || (th instanceof Exception)) {
                try {
                    Object apply = this.resumeFunction.apply(th);
                    ObjectHelper.requireNonNull(apply, "The resumeFunction returned a null MaybeSource");
                    MaybeSource maybeSource = (MaybeSource) apply;
                    DisposableHelper.replace(this, null);
                    maybeSource.subscribe(new NextMaybeObserver(this.actual, this));
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.actual.onError(new CompositeException(th, th2));
                }
            } else {
                this.actual.onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
        }
    }

    public MaybeOnErrorNext(MaybeSource maybeSource, Function function, boolean z) {
        super(maybeSource);
        this.resumeFunction = function;
        this.allowFatal = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new OnErrorNextMaybeObserver(maybeObserver, this.resumeFunction, this.allowFatal));
    }
}
