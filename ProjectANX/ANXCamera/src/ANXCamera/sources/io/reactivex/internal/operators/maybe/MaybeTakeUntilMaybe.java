package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeTakeUntilMaybe extends AbstractMaybeWithUpstream {
    final MaybeSource other;

    final class TakeUntilMainMaybeObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = -2187421758664251153L;
        final MaybeObserver actual;
        final TakeUntilOtherMaybeObserver other = new TakeUntilOtherMaybeObserver(this);

        final class TakeUntilOtherMaybeObserver extends AtomicReference implements MaybeObserver {
            private static final long serialVersionUID = -1266041316834525931L;
            final TakeUntilMainMaybeObserver parent;

            TakeUntilOtherMaybeObserver(TakeUntilMainMaybeObserver takeUntilMainMaybeObserver) {
                this.parent = takeUntilMainMaybeObserver;
            }

            public void onComplete() {
                this.parent.otherComplete();
            }

            public void onError(Throwable th) {
                this.parent.otherError(th);
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }

            public void onSuccess(Object obj) {
                this.parent.otherComplete();
            }
        }

        TakeUntilMainMaybeObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            DisposableHelper.dispose(this.other);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            DisposableHelper.dispose(this.other);
            if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
                this.actual.onComplete();
            }
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.other);
            if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
                this.actual.onError(th);
            } else {
                RxJavaPlugins.onError(th);
            }
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(Object obj) {
            DisposableHelper.dispose(this.other);
            if (getAndSet(DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
                this.actual.onSuccess(obj);
            }
        }

        /* access modifiers changed from: 0000 */
        public void otherComplete() {
            if (DisposableHelper.dispose(this)) {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void otherError(Throwable th) {
            if (DisposableHelper.dispose(this)) {
                this.actual.onError(th);
            } else {
                RxJavaPlugins.onError(th);
            }
        }
    }

    public MaybeTakeUntilMaybe(MaybeSource maybeSource, MaybeSource maybeSource2) {
        super(maybeSource);
        this.other = maybeSource2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        TakeUntilMainMaybeObserver takeUntilMainMaybeObserver = new TakeUntilMainMaybeObserver(maybeObserver);
        maybeObserver.onSubscribe(takeUntilMainMaybeObserver);
        this.other.subscribe(takeUntilMainMaybeObserver.other);
        this.source.subscribe(takeUntilMainMaybeObserver);
    }
}
