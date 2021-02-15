package io.reactivex.internal.operators.maybe;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.FuseToMaybe;
import io.reactivex.plugins.RxJavaPlugins;

public final class MaybeIgnoreElementCompletable extends Completable implements FuseToMaybe {
    final MaybeSource source;

    final class IgnoreMaybeObserver implements MaybeObserver, Disposable {
        final CompletableObserver actual;
        Disposable d;

        IgnoreMaybeObserver(CompletableObserver completableObserver) {
            this.actual = completableObserver;
        }

        public void dispose() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.d, disposable)) {
                this.d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onComplete();
        }
    }

    public MaybeIgnoreElementCompletable(MaybeSource maybeSource) {
        this.source = maybeSource;
    }

    public Maybe fuseToMaybe() {
        return RxJavaPlugins.onAssembly((Maybe) new MaybeIgnoreElement(this.source));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new IgnoreMaybeObserver(completableObserver));
    }
}
