package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeHide extends AbstractMaybeWithUpstream {

    final class HideMaybeObserver implements MaybeObserver, Disposable {
        final MaybeObserver actual;
        Disposable d;

        HideMaybeObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
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
        }
    }

    public MaybeHide(MaybeSource maybeSource) {
        super(maybeSource);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new HideMaybeObserver(maybeObserver));
    }
}
