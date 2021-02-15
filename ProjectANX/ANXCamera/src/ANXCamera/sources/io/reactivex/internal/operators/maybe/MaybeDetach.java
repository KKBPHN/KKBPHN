package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeDetach extends AbstractMaybeWithUpstream {

    final class DetachMaybeObserver implements MaybeObserver, Disposable {
        MaybeObserver actual;
        Disposable d;

        DetachMaybeObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.actual = null;
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onComplete() {
            this.d = DisposableHelper.DISPOSED;
            MaybeObserver maybeObserver = this.actual;
            if (maybeObserver != null) {
                this.actual = null;
                maybeObserver.onComplete();
            }
        }

        public void onError(Throwable th) {
            this.d = DisposableHelper.DISPOSED;
            MaybeObserver maybeObserver = this.actual;
            if (maybeObserver != null) {
                this.actual = null;
                maybeObserver.onError(th);
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
            MaybeObserver maybeObserver = this.actual;
            if (maybeObserver != null) {
                this.actual = null;
                maybeObserver.onSuccess(obj);
            }
        }
    }

    public MaybeDetach(MaybeSource maybeSource) {
        super(maybeSource);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new DetachMaybeObserver(maybeObserver));
    }
}
