package androidx.concurrent.futures;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public final class CallbackToFutureAdapter {

    public final class Completer {
        private boolean attemptedSetting;
        private ResolvableFuture cancellationFuture = ResolvableFuture.create();
        SafeFuture future;
        Object tag;

        Completer() {
        }

        private void setCompletedNormally() {
            this.tag = null;
            this.future = null;
            this.cancellationFuture = null;
        }

        public void addCancellationListener(@NonNull Runnable runnable, @NonNull Executor executor) {
            ResolvableFuture resolvableFuture = this.cancellationFuture;
            if (resolvableFuture != null) {
                resolvableFuture.addListener(runnable, executor);
            }
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            SafeFuture safeFuture = this.future;
            if (safeFuture != null && !safeFuture.isDone()) {
                StringBuilder sb = new StringBuilder();
                sb.append("The completer object was garbage collected - this future would otherwise never complete. The tag was: ");
                sb.append(this.tag);
                safeFuture.setException(new FutureGarbageCollectedException(sb.toString()));
            }
            if (!this.attemptedSetting) {
                ResolvableFuture resolvableFuture = this.cancellationFuture;
                if (resolvableFuture != null) {
                    resolvableFuture.set(null);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void fireCancellationListeners() {
            this.tag = null;
            this.future = null;
            this.cancellationFuture.set(null);
        }

        public boolean set(Object obj) {
            boolean z = true;
            this.attemptedSetting = true;
            SafeFuture safeFuture = this.future;
            if (safeFuture == null || !safeFuture.set(obj)) {
                z = false;
            }
            if (z) {
                setCompletedNormally();
            }
            return z;
        }

        public boolean setCancelled() {
            boolean z = true;
            this.attemptedSetting = true;
            SafeFuture safeFuture = this.future;
            if (safeFuture == null || !safeFuture.cancelWithoutNotifyingCompleter(true)) {
                z = false;
            }
            if (z) {
                setCompletedNormally();
            }
            return z;
        }

        public boolean setException(@NonNull Throwable th) {
            boolean z = true;
            this.attemptedSetting = true;
            SafeFuture safeFuture = this.future;
            if (safeFuture == null || !safeFuture.setException(th)) {
                z = false;
            }
            if (z) {
                setCompletedNormally();
            }
            return z;
        }
    }

    final class FutureGarbageCollectedException extends Throwable {
        FutureGarbageCollectedException(String str) {
            super(str);
        }

        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    public interface Resolver {
        @Nullable
        Object attachCompleter(@NonNull Completer completer);
    }

    final class SafeFuture implements ListenableFuture {
        final WeakReference completerWeakReference;
        private final AbstractResolvableFuture delegate = new AbstractResolvableFuture() {
            /* access modifiers changed from: protected */
            public String pendingToString() {
                Completer completer = (Completer) SafeFuture.this.completerWeakReference.get();
                if (completer == null) {
                    return "Completer object has been garbage collected, future will fail soon";
                }
                StringBuilder sb = new StringBuilder();
                sb.append("tag=[");
                sb.append(completer.tag);
                sb.append("]");
                return sb.toString();
            }
        };

        SafeFuture(Completer completer) {
            this.completerWeakReference = new WeakReference(completer);
        }

        public void addListener(@NonNull Runnable runnable, @NonNull Executor executor) {
            this.delegate.addListener(runnable, executor);
        }

        public boolean cancel(boolean z) {
            Completer completer = (Completer) this.completerWeakReference.get();
            boolean cancel = this.delegate.cancel(z);
            if (cancel && completer != null) {
                completer.fireCancellationListeners();
            }
            return cancel;
        }

        /* access modifiers changed from: 0000 */
        public boolean cancelWithoutNotifyingCompleter(boolean z) {
            return this.delegate.cancel(z);
        }

        public Object get() {
            return this.delegate.get();
        }

        public Object get(long j, @NonNull TimeUnit timeUnit) {
            return this.delegate.get(j, timeUnit);
        }

        public boolean isCancelled() {
            return this.delegate.isCancelled();
        }

        public boolean isDone() {
            return this.delegate.isDone();
        }

        /* access modifiers changed from: 0000 */
        public boolean set(Object obj) {
            return this.delegate.set(obj);
        }

        /* access modifiers changed from: 0000 */
        public boolean setException(Throwable th) {
            return this.delegate.setException(th);
        }

        public String toString() {
            return this.delegate.toString();
        }
    }

    private CallbackToFutureAdapter() {
    }

    @NonNull
    public static ListenableFuture getFuture(@NonNull Resolver resolver) {
        Completer completer = new Completer();
        SafeFuture safeFuture = new SafeFuture(completer);
        completer.future = safeFuture;
        completer.tag = resolver.getClass();
        try {
            Object attachCompleter = resolver.attachCompleter(completer);
            if (attachCompleter != null) {
                completer.tag = attachCompleter;
            }
        } catch (Exception e) {
            safeFuture.setException(e);
        }
        return safeFuture;
    }
}
