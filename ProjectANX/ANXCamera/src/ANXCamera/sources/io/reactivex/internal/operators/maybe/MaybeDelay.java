package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeDelay extends AbstractMaybeWithUpstream {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    final class DelayMaybeObserver extends AtomicReference implements MaybeObserver, Disposable, Runnable {
        private static final long serialVersionUID = 5566860102500855068L;
        final MaybeObserver actual;
        final long delay;
        Throwable error;
        final Scheduler scheduler;
        final TimeUnit unit;
        Object value;

        DelayMaybeObserver(MaybeObserver maybeObserver, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.actual = maybeObserver;
            this.delay = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onComplete() {
            schedule();
        }

        public void onError(Throwable th) {
            this.error = th;
            schedule();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object obj) {
            this.value = obj;
            schedule();
        }

        public void run() {
            Throwable th = this.error;
            if (th != null) {
                this.actual.onError(th);
                return;
            }
            Object obj = this.value;
            MaybeObserver maybeObserver = this.actual;
            if (obj != null) {
                maybeObserver.onSuccess(obj);
            } else {
                maybeObserver.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void schedule() {
            DisposableHelper.replace(this, this.scheduler.scheduleDirect(this, this.delay, this.unit));
        }
    }

    public MaybeDelay(MaybeSource maybeSource, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        super(maybeSource);
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver maybeObserver) {
        MaybeSource maybeSource = this.source;
        DelayMaybeObserver delayMaybeObserver = new DelayMaybeObserver(maybeObserver, this.delay, this.unit, this.scheduler);
        maybeSource.subscribe(delayMaybeObserver);
    }
}
