package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeSwitchIfEmpty extends AbstractMaybeWithUpstream {
    final MaybeSource other;

    final class SwitchIfEmptyMaybeObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = -2223459372976438024L;
        final MaybeObserver actual;
        final MaybeSource other;

        final class OtherMaybeObserver implements MaybeObserver {
            final MaybeObserver actual;
            final AtomicReference parent;

            OtherMaybeObserver(MaybeObserver maybeObserver, AtomicReference atomicReference) {
                this.actual = maybeObserver;
                this.parent = atomicReference;
            }

            public void onComplete() {
                this.actual.onComplete();
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

        SwitchIfEmptyMaybeObserver(MaybeObserver maybeObserver, MaybeSource maybeSource) {
            this.actual = maybeObserver;
            this.other = maybeSource;
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
                this.other.subscribe(new OtherMaybeObserver(this.actual, this));
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

    public MaybeSwitchIfEmpty(MaybeSource maybeSource, MaybeSource maybeSource2) {
        super(maybeSource);
        this.other = maybeSource2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new SwitchIfEmptyMaybeObserver(maybeObserver, this.other));
    }
}
