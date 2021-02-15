package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleTimeout extends Single {
    final SingleSource other;
    final Scheduler scheduler;
    final SingleSource source;
    final long timeout;
    final TimeUnit unit;

    final class TimeoutMainObserver extends AtomicReference implements SingleObserver, Runnable, Disposable {
        private static final long serialVersionUID = 37497744973048446L;
        final SingleObserver actual;
        final TimeoutFallbackObserver fallback;
        SingleSource other;
        final AtomicReference task = new AtomicReference();

        final class TimeoutFallbackObserver extends AtomicReference implements SingleObserver {
            private static final long serialVersionUID = 2071387740092105509L;
            final SingleObserver actual;

            TimeoutFallbackObserver(SingleObserver singleObserver) {
                this.actual = singleObserver;
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

        TimeoutMainObserver(SingleObserver singleObserver, SingleSource singleSource) {
            this.actual = singleObserver;
            this.other = singleSource;
            if (singleSource != null) {
                this.fallback = new TimeoutFallbackObserver(singleObserver);
            } else {
                this.fallback = null;
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            DisposableHelper.dispose(this.task);
            TimeoutFallbackObserver timeoutFallbackObserver = this.fallback;
            if (timeoutFallbackObserver != null) {
                DisposableHelper.dispose(timeoutFallbackObserver);
            }
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onError(Throwable th) {
            Disposable disposable = (Disposable) get();
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (disposable == disposableHelper || !compareAndSet(disposable, disposableHelper)) {
                RxJavaPlugins.onError(th);
                return;
            }
            DisposableHelper.dispose(this.task);
            this.actual.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onSuccess(Object obj) {
            Disposable disposable = (Disposable) get();
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (disposable != disposableHelper && compareAndSet(disposable, disposableHelper)) {
                DisposableHelper.dispose(this.task);
                this.actual.onSuccess(obj);
            }
        }

        public void run() {
            Disposable disposable = (Disposable) get();
            DisposableHelper disposableHelper = DisposableHelper.DISPOSED;
            if (disposable != disposableHelper && compareAndSet(disposable, disposableHelper)) {
                if (disposable != null) {
                    disposable.dispose();
                }
                SingleSource singleSource = this.other;
                if (singleSource == null) {
                    this.actual.onError(new TimeoutException());
                    return;
                }
                this.other = null;
                singleSource.subscribe(this.fallback);
            }
        }
    }

    public SingleTimeout(SingleSource singleSource, long j, TimeUnit timeUnit, Scheduler scheduler2, SingleSource singleSource2) {
        this.source = singleSource;
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.other = singleSource2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver singleObserver) {
        TimeoutMainObserver timeoutMainObserver = new TimeoutMainObserver(singleObserver, this.other);
        singleObserver.onSubscribe(timeoutMainObserver);
        DisposableHelper.replace(timeoutMainObserver.task, this.scheduler.scheduleDirect(timeoutMainObserver, this.timeout, this.unit));
        this.source.subscribe(timeoutMainObserver);
    }
}
