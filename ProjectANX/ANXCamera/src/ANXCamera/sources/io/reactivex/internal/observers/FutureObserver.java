package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class FutureObserver extends CountDownLatch implements Observer, Future, Disposable {
    Throwable error;
    final AtomicReference s = new AtomicReference();
    Object value;

    public FutureObserver() {
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

    public void onComplete() {
        if (this.value == null) {
            onError(new NoSuchElementException("The source is empty"));
            return;
        }
        while (true) {
            Disposable disposable = (Disposable) this.s.get();
            if (disposable != this && disposable != DisposableHelper.DISPOSED) {
                if (this.s.compareAndSet(disposable, this)) {
                    countDown();
                    break;
                }
            } else {
                break;
            }
        }
    }

    public void onError(Throwable th) {
        Disposable disposable;
        if (this.error == null) {
            this.error = th;
            do {
                disposable = (Disposable) this.s.get();
                if (disposable == this || disposable == DisposableHelper.DISPOSED) {
                    RxJavaPlugins.onError(th);
                    return;
                }
            } while (!this.s.compareAndSet(disposable, this));
            countDown();
            return;
        }
        RxJavaPlugins.onError(th);
    }

    public void onNext(Object obj) {
        if (this.value != null) {
            ((Disposable) this.s.get()).dispose();
            onError(new IndexOutOfBoundsException("More than one element received"));
            return;
        }
        this.value = obj;
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.setOnce(this.s, disposable);
    }
}
