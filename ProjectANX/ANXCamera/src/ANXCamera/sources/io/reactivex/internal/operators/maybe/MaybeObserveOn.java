package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeObserveOn extends AbstractMaybeWithUpstream {
    final Scheduler scheduler;

    final class ObserveOnMaybeObserver extends AtomicReference implements MaybeObserver, Disposable, Runnable {
        private static final long serialVersionUID = 8571289934935992137L;
        final MaybeObserver actual;
        Throwable error;
        final Scheduler scheduler;
        Object value;

        ObserveOnMaybeObserver(MaybeObserver maybeObserver, Scheduler scheduler2) {
            this.actual = maybeObserver;
            this.scheduler = scheduler2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
        }

        public void onError(Throwable th) {
            this.error = th;
            DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.value = obj;
            DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
        }

        public void run() {
            Throwable th = this.error;
            if (th != null) {
                this.error = null;
                this.actual.onError(th);
                return;
            }
            Object obj = this.value;
            if (obj != null) {
                this.value = null;
                this.actual.onSuccess(obj);
                return;
            }
            this.actual.onComplete();
        }
    }

    public MaybeObserveOn(MaybeSource maybeSource, Scheduler scheduler2) {
        super(maybeSource);
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        this.source.subscribe(new ObserveOnMaybeObserver(maybeObserver, this.scheduler));
    }
}
