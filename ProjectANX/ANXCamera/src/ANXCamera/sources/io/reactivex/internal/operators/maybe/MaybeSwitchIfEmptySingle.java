package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeSwitchIfEmptySingle extends Single implements HasUpstreamMaybeSource {
    final SingleSource other;
    final MaybeSource source;

    final class SwitchIfEmptyMaybeObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = 4603919676453758899L;
        final SingleObserver actual;
        final SingleSource other;

        final class OtherSingleObserver implements SingleObserver {
            final SingleObserver actual;
            final AtomicReference parent;

            OtherSingleObserver(SingleObserver singleObserver, AtomicReference atomicReference) {
                this.actual = singleObserver;
                this.parent = atomicReference;
            }

            public void onError(Throwable th) {
                this.actual.onError(th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this.parent, disposable);
            }

            public void onSuccess(Object obj) {
                this.actual.onSuccess(obj);
            }
        }

        SwitchIfEmptyMaybeObserver(SingleObserver singleObserver, SingleSource singleSource) {
            this.actual = singleObserver;
            this.other = singleSource;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            Disposable disposable = (Disposable) get();
            if (disposable != DisposableHelper.DISPOSED && compareAndSet(disposable, null)) {
                this.other.subscribe(new OtherSingleObserver(this.actual, this));
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
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

    public MaybeSwitchIfEmptySingle(MaybeSource maybeSource, SingleSource singleSource) {
        this.source = maybeSource;
        this.other = singleSource;
    }

    public MaybeSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        this.source.subscribe(new SwitchIfEmptyMaybeObserver(singleObserver, this.other));
    }
}
