package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamSingleSource;

public final class MaybeFromSingle extends Maybe implements HasUpstreamSingleSource {
    final SingleSource source;

    final class FromSingleObserver implements SingleObserver, Disposable {
        final MaybeObserver actual;
        Disposable d;

        FromSingleObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
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
            this.actual.onSuccess(obj);
        }
    }

    public MaybeFromSingle(SingleSource singleSource) {
        this.source = singleSource;
    }

    public SingleSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new FromSingleObserver(maybeObserver));
    }
}
