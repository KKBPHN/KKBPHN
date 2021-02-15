package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Scheduler.Worker;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.TimeUnit;

public final class ObservableDelay extends AbstractObservableWithUpstream {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    final class DelayObserver implements Observer, Disposable {
        final Observer actual;
        final long delay;
        final boolean delayError;
        Disposable s;
        final TimeUnit unit;
        final Worker w;

        final class OnComplete implements Runnable {
            OnComplete() {
            }

            public void run() {
                try {
                    DelayObserver.this.actual.onComplete();
                } finally {
                    DelayObserver.this.w.dispose();
                }
            }
        }

        final class OnError implements Runnable {
            private final Throwable throwable;

            OnError(Throwable th) {
                this.throwable = th;
            }

            public void run() {
                try {
                    DelayObserver.this.actual.onError(this.throwable);
                } finally {
                    DelayObserver.this.w.dispose();
                }
            }
        }

        final class OnNext implements Runnable {
            private final Object t;

            OnNext(Object obj) {
                this.t = obj;
            }

            public void run() {
                DelayObserver.this.actual.onNext(this.t);
            }
        }

        DelayObserver(Observer observer, long j, TimeUnit timeUnit, Worker worker, boolean z) {
            this.actual = observer;
            this.delay = j;
            this.unit = timeUnit;
            this.w = worker;
            this.delayError = z;
        }

        public void dispose() {
            this.s.dispose();
            this.w.dispose();
        }

        public boolean isDisposed() {
            return this.w.isDisposed();
        }

        public void onComplete() {
            this.w.schedule(new OnComplete(), this.delay, this.unit);
        }

        public void onError(Throwable th) {
            this.w.schedule(new OnError(th), this.delayError ? this.delay : 0, this.unit);
        }

        public void onNext(Object obj) {
            this.w.schedule(new OnNext(obj), this.delay, this.unit);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableDelay(ObservableSource observableSource, long j, TimeUnit timeUnit, Scheduler scheduler2, boolean z) {
        super(observableSource);
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.delayError = z;
    }

    public void subscribeActual(Observer observer) {
        Observer serializedObserver = this.delayError ? observer : new SerializedObserver(observer);
        Worker createWorker = this.scheduler.createWorker();
        ObservableSource observableSource = this.source;
        DelayObserver delayObserver = new DelayObserver(serializedObserver, this.delay, this.unit, createWorker, this.delayError);
        observableSource.subscribe(delayObserver);
    }
}
