package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeTimeoutMaybe extends AbstractMaybeWithUpstream {
    final MaybeSource fallback;
    final MaybeSource other;

    final class TimeoutFallbackMaybeObserver extends AtomicReference implements MaybeObserver {
        private static final long serialVersionUID = 8663801314800248617L;
        final MaybeObserver actual;

        TimeoutFallbackMaybeObserver(MaybeObserver maybeObserver) {
            this.actual = maybeObserver;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(Object obj) {
            this.actual.onSuccess(obj);
        }
    }

    final class TimeoutMainMaybeObserver extends AtomicReference implements MaybeObserver, Disposable {
        private static final long serialVersionUID = -5955289211445418871L;
        final MaybeObserver actual;
        final MaybeSource fallback;
        final TimeoutOtherMaybeObserver other = new TimeoutOtherMaybeObserver(this);
        final TimeoutFallbackMaybeObserver otherObserver;

        TimeoutMainMaybeObserver(MaybeObserver maybeObserver, MaybeSource maybeSource) {
            this.actual = maybeObserver;
            this.fallback = maybeSource;
            this.otherObserver = maybeSource != null ? new TimeoutFallbackMaybeObserver(maybeObserver) : null;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            DisposableHelper.dispose(this.other);
            TimeoutFallbackMaybeObserver timeoutFallbackMaybeObserver = this.otherObserver;
            if (timeoutFallbackMaybeObserver != null) {
                DisposableHelper.dispose(timeoutFallbackMaybeObserver);
            }
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

        public void otherComplete() {
            if (DisposableHelper.dispose(this)) {
                MaybeSource maybeSource = this.fallback;
                if (maybeSource == null) {
                    this.actual.onError(new TimeoutException());
                } else {
                    maybeSource.subscribe(this.otherObserver);
                }
            }
        }

        public void otherError(Throwable th) {
            if (DisposableHelper.dispose(this)) {
                this.actual.onError(th);
            } else {
                RxJavaPlugins.onError(th);
            }
        }
    }

    final class TimeoutOtherMaybeObserver extends AtomicReference implements MaybeObserver {
        private static final long serialVersionUID = 8663801314800248617L;
        final TimeoutMainMaybeObserver parent;

        TimeoutOtherMaybeObserver(TimeoutMainMaybeObserver timeoutMainMaybeObserver) {
            this.parent = timeoutMainMaybeObserver;
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

    public MaybeTimeoutMaybe(MaybeSource maybeSource, MaybeSource maybeSource2, MaybeSource maybeSource3) {
        super(maybeSource);
        this.other = maybeSource2;
        this.fallback = maybeSource3;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        TimeoutMainMaybeObserver timeoutMainMaybeObserver = new TimeoutMainMaybeObserver(maybeObserver, this.fallback);
        maybeObserver.onSubscribe(timeoutMainMaybeObserver);
        this.other.subscribe(timeoutMainMaybeObserver.other);
        this.source.subscribe(timeoutMainMaybeObserver);
    }
}
