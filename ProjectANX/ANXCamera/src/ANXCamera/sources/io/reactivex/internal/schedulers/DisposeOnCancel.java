package io.reactivex.internal.schedulers;

import io.reactivex.disposables.Disposable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

final class DisposeOnCancel implements Future {
    final Disposable d;

    DisposeOnCancel(Disposable disposable) {
        this.d = disposable;
    }

    public boolean cancel(boolean z) {
        this.d.dispose();
        return false;
    }

    public Object get() {
        return null;
    }

    public Object get(long j, TimeUnit timeUnit) {
        return null;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return false;
    }
}
