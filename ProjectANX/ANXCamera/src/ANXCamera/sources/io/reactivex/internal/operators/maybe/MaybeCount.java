package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

public final class MaybeCount extends Single implements HasUpstreamMaybeSource {
    final MaybeSource source;

    final class CountMaybeObserver implements MaybeObserver, Disposable {
        final SingleObserver actual;
        Disposable d;

        CountMaybeObserver(SingleObserver singleObserver) {
            this.actual = singleObserver;
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
            this.actual.onSuccess(Long.valueOf(0));
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
            this.actual.onSuccess(Long.valueOf(1));
        }
    }

    public MaybeCount(MaybeSource maybeSource) {
        this.source = maybeSource;
    }

    public MaybeSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new CountMaybeObserver(singleObserver));
    }
}
