package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleTimer extends Single {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    final class TimerDisposable extends AtomicReference implements Disposable, Runnable {
        private static final long serialVersionUID = 8465401857522493082L;
        final SingleObserver actual;

        TimerDisposable(SingleObserver singleObserver) {
            this.actual = singleObserver;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void run() {
            this.actual.onSuccess(Long.valueOf(0));
        }

        /* access modifiers changed from: 0000 */
        public void setFuture(Disposable disposable) {
            DisposableHelper.replace(this, disposable);
        }
    }

    public SingleTimer(long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        TimerDisposable timerDisposable = new TimerDisposable(singleObserver);
        singleObserver.onSubscribe(timerDisposable);
        timerDisposable.setFuture(this.scheduler.scheduleDirect(timerDisposable, this.delay, this.unit));
    }
}
