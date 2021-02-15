package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeIsEmpty extends AbstractMaybeWithUpstream {

    final class IsEmptyMaybeObserver implements MaybeObserver, Disposable {
        final MaybeObserver actual;
        Disposable d;

        IsEmptyMaybeObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.actual.onSuccess(Boolean.valueOf(true));
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
            this.actual.onSuccess(Boolean.valueOf(false));
        }
    }

    public MaybeIsEmpty(MaybeSource maybeSource) {
        super(maybeSource);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new IsEmptyMaybeObserver(maybeObserver));
    }
}
