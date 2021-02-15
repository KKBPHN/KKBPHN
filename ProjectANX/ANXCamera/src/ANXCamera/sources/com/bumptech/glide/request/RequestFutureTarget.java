package com.bumptech.glide.request;

import android.graphics.drawable.Drawable;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestFutureTarget implements FutureTarget, RequestListener {
    private static final Waiter DEFAULT_WAITER = new Waiter();
    private final boolean assertBackgroundThread;
    @GuardedBy("this")
    @Nullable
    private GlideException exception;
    private final int height;
    @GuardedBy("this")
    private boolean isCancelled;
    @GuardedBy("this")
    private boolean loadFailed;
    @GuardedBy("this")
    @Nullable
    private Request request;
    @GuardedBy("this")
    @Nullable
    private Object resource;
    @GuardedBy("this")
    private boolean resultReceived;
    private final Waiter waiter;
    private final int width;

    @VisibleForTesting
    class Waiter {
        Waiter() {
        }

        /* access modifiers changed from: 0000 */
        public void notifyAll(Object obj) {
            obj.notifyAll();
        }

        /* access modifiers changed from: 0000 */
        public void waitForTimeout(Object obj, long j) {
            obj.wait(j);
        }
    }

    public RequestFutureTarget(int i, int i2) {
        this(i, i2, true, DEFAULT_WAITER);
    }

    RequestFutureTarget(int i, int i2, boolean z, Waiter waiter2) {
        this.width = i;
        this.height = i2;
        this.assertBackgroundThread = z;
        this.waiter = waiter2;
    }

    private synchronized Object doGet(Long l) {
        if (this.assertBackgroundThread && !isDone()) {
            Util.assertBackgroundThread();
        }
        if (this.isCancelled) {
            throw new CancellationException();
        } else if (this.loadFailed) {
            throw new ExecutionException(this.exception);
        } else if (this.resultReceived) {
            return this.resource;
        } else {
            if (l == null) {
                this.waiter.waitForTimeout(this, 0);
            } else if (l.longValue() > 0) {
                long currentTimeMillis = System.currentTimeMillis();
                long longValue = l.longValue() + currentTimeMillis;
                while (!isDone() && currentTimeMillis < longValue) {
                    this.waiter.waitForTimeout(this, longValue - currentTimeMillis);
                    currentTimeMillis = System.currentTimeMillis();
                }
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            } else if (this.loadFailed) {
                throw new ExecutionException(this.exception);
            } else if (this.isCancelled) {
                throw new CancellationException();
            } else if (this.resultReceived) {
                return this.resource;
            } else {
                throw new TimeoutException();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001c, code lost:
        if (r3 == null) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001e, code lost:
        r3.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0021, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean cancel(boolean z) {
        Request request2;
        synchronized (this) {
            if (isDone()) {
                return false;
            }
            this.isCancelled = true;
            this.waiter.notifyAll(this);
            if (z) {
                request2 = this.request;
                this.request = null;
            } else {
                request2 = null;
            }
        }
    }

    public Object get() {
        try {
            return doGet(null);
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    public Object get(long j, @NonNull TimeUnit timeUnit) {
        return doGet(Long.valueOf(timeUnit.toMillis(j)));
    }

    @Nullable
    public synchronized Request getRequest() {
        return this.request;
    }

    public void getSize(@NonNull SizeReadyCallback sizeReadyCallback) {
        sizeReadyCallback.onSizeReady(this.width, this.height);
    }

    public synchronized boolean isCancelled() {
        return this.isCancelled;
    }

    public synchronized boolean isDone() {
        boolean z;
        z = this.isCancelled || this.resultReceived || this.loadFailed;
        return z;
    }

    public void onDestroy() {
    }

    public void onLoadCleared(@Nullable Drawable drawable) {
    }

    public synchronized void onLoadFailed(@Nullable Drawable drawable) {
    }

    public synchronized boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target target, boolean z) {
        this.loadFailed = true;
        this.exception = glideException;
        this.waiter.notifyAll(this);
        return false;
    }

    public void onLoadStarted(@Nullable Drawable drawable) {
    }

    public synchronized void onResourceReady(@NonNull Object obj, @Nullable Transition transition) {
    }

    public synchronized boolean onResourceReady(Object obj, Object obj2, Target target, DataSource dataSource, boolean z) {
        this.resultReceived = true;
        this.resource = obj;
        this.waiter.notifyAll(this);
        return false;
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void removeCallback(@NonNull SizeReadyCallback sizeReadyCallback) {
    }

    public synchronized void setRequest(@Nullable Request request2) {
        this.request = request2;
    }
}
