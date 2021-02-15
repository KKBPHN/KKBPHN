package io.reactivex.internal.observers;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class FutureSingleObserver extends CountDownLatch implements SingleObserver, Future, Disposable {
    Throwable error;
    final AtomicReference s = new AtomicReference();
    Object value;

    public FutureSingleObserver() {
        super(1);
    }

    public boolean cancel(boolean z) {
        Disposable disposable;
        DisposableHelper disposableHelper;
        do {
            disposable = (Disposable) this.s.get();
            if (disposable != this) {
                disposableHelper = DisposableHelper.DISPOSED;
                if (disposable == disposableHelper) {
                }
            }
            return false;
        } while (!this.s.compareAndSet(disposable, disposableHelper));
        if (disposable != null) {
            disposable.dispose();
        }
        countDown();
        return true;
    }

    public void dispose() {
    }

    public Object get() {
        if (getCount() != 0) {
            BlockingHelper.verifyNonBlocking();
            await();
        }
        if (!isCancelled()) {
            Throwable th = this.error;
            if (th == null) {
                return this.value;
            }
            throw new ExecutionException(th);
        }
        throw new CancellationException();
    }

    public Object get(long j, TimeUnit timeUnit) {
        if (getCount() != 0) {
            BlockingHelper.verifyNonBlocking();
            if (!await(j, timeUnit)) {
                throw new TimeoutException();
            }
        }
        if (!isCancelled()) {
            Throwable th = this.error;
            if (th == null) {
                return this.value;
            }
            throw new ExecutionException(th);
        }
        throw new CancellationException();
    }

    public boolean isCancelled() {
        return DisposableHelper.isDisposed((Disposable) this.s.get());
    }

    public boolean isDisposed() {
        return isDone();
    }

    public boolean isDone() {
        return getCount() == 0;
    }

    public void onError(Throwable th) {
        Disposable disposable;
        do {
            disposable = (Disposable) this.s.get();
            if (disposable == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
        } while (!this.s.compareAndSet(disposable, this));
        countDown();
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.setOnce(this.s, disposable);
    }

    public void onSuccess(Object obj) {
        Disposable disposable = (Disposable) this.s.get();
        if (disposable != DisposableHelper.DISPOSED) {
            this.value = obj;
            this.s.compareAndSet(disposable, this);
            countDown();
        }
    }
}
