package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

@Experimental
public final class MaybeDoAfterSuccess extends AbstractMaybeWithUpstream {
    final Consumer onAfterSuccess;

    final class DoAfterObserver implements MaybeObserver, Disposable {
        final MaybeObserver actual;
        Disposable d;
        final Consumer onAfterSuccess;

        DoAfterObserver(MaybeObserver maybeObserver, Consumer consumer) {
            this.actual = maybeObserver;
            this.onAfterSuccess = consumer;
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

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
            try {
                this.onAfterSuccess.accept(obj);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }
    }

    public MaybeDoAfterSuccess(MaybeSource maybeSource, Consumer consumer) {
        super(maybeSource);
        this.onAfterSuccess = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new DoAfterObserver(maybeObserver, this.onAfterSuccess));
    }
}
