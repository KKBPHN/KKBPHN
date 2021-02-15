package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.TimeUnit;

public final class SingleDelay extends Single {
    final boolean delayError;
    final Scheduler scheduler;
    final SingleSource source;
    final long time;
    final TimeUnit unit;

    final class Delay implements SingleObserver {
        final SingleObserver s;
        private final SequentialDisposable sd;

        final class OnError implements Runnable {
            private final Throwable e;

            OnError(Throwable th) {
                this.e = th;
            }

            public void run() {
                Delay.this.s.onError(this.e);
            }
        }

        final class OnSuccess implements Runnable {
            private final Object value;

            OnSuccess(Object obj) {
                this.value = obj;
            }

            public void run() {
                Delay.this.s.onSuccess(this.value);
            }
        }

        Delay(SequentialDisposable sequentialDisposable, SingleObserver singleObserver) {
            this.sd = sequentialDisposable;
            this.s = singleObserver;
        }

        public void onError(Throwable th) {
            SequentialDisposable sequentialDisposable = this.sd;
            Scheduler scheduler = SingleDelay.this.scheduler;
            OnError onError = new OnError(th);
            SingleDelay singleDelay = SingleDelay.this;
            sequentialDisposable.replace(scheduler.scheduleDirect(onError, singleDelay.delayError ? singleDelay.time : 0, SingleDelay.this.unit));
        }

        public void onSubscribe(Disposable disposable) {
            this.sd.replace(disposable);
        }

        public void onSuccess(Object obj) {
            SequentialDisposable sequentialDisposable = this.sd;
            Scheduler scheduler = SingleDelay.this.scheduler;
            OnSuccess onSuccess = new OnSuccess(obj);
            SingleDelay singleDelay = SingleDelay.this;
            sequentialDisposable.replace(scheduler.scheduleDirect(onSuccess, singleDelay.time, singleDelay.unit));
        }
    }

    public SingleDelay(SingleSource singleSource, long j, TimeUnit timeUnit, Scheduler scheduler2, boolean z) {
        this.source = singleSource;
        this.time = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.delayError = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        singleObserver.onSubscribe(sequentialDisposable);
        this.source.subscribe(new Delay(sequentialDisposable, singleObserver));
    }
}
