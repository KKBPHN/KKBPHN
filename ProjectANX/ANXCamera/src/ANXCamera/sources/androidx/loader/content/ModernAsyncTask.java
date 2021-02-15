package androidx.loader.content;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

abstract class ModernAsyncTask {
    private static final String LOG_TAG = "AsyncTask";
    private static Handler sHandler;
    final AtomicBoolean mCancelled = new AtomicBoolean();
    private final FutureTask mFuture = new FutureTask(new Callable() {
        public Object call() {
            ModernAsyncTask.this.mTaskInvoked.set(true);
            Object obj = null;
            try {
                Process.setThreadPriority(10);
                obj = ModernAsyncTask.this.doInBackground();
                Binder.flushPendingCommands();
                ModernAsyncTask.this.postResult(obj);
                return obj;
            } catch (Throwable th) {
                ModernAsyncTask.this.postResult(obj);
                throw th;
            }
        }
    }) {
        /* access modifiers changed from: protected */
        public void done() {
            String str = "An error occurred while executing doInBackground()";
            try {
                ModernAsyncTask.this.postResultIfNotInvoked(get());
            } catch (InterruptedException e) {
                Log.w(ModernAsyncTask.LOG_TAG, e);
            } catch (ExecutionException e2) {
                throw new RuntimeException(str, e2.getCause());
            } catch (CancellationException unused) {
                ModernAsyncTask.this.postResultIfNotInvoked(null);
            } catch (Throwable th) {
                throw new RuntimeException(str, th);
            }
        }
    };
    private volatile Status mStatus = Status.PENDING;
    final AtomicBoolean mTaskInvoked = new AtomicBoolean();

    /* renamed from: androidx.loader.content.ModernAsyncTask$4 reason: invalid class name */
    /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$androidx$loader$content$ModernAsyncTask$Status = new int[Status.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$androidx$loader$content$ModernAsyncTask$Status[Status.RUNNING.ordinal()] = 1;
            $SwitchMap$androidx$loader$content$ModernAsyncTask$Status[Status.FINISHED.ordinal()] = 2;
        }
    }

    public enum Status {
        PENDING,
        RUNNING,
        FINISHED
    }

    ModernAsyncTask() {
    }

    private static Handler getHandler() {
        Handler handler;
        synchronized (ModernAsyncTask.class) {
            if (sHandler == null) {
                sHandler = new Handler(Looper.getMainLooper());
            }
            handler = sHandler;
        }
        return handler;
    }

    public final boolean cancel(boolean z) {
        this.mCancelled.set(true);
        return this.mFuture.cancel(z);
    }

    public abstract Object doInBackground();

    public final void executeOnExecutor(@NonNull Executor executor) {
        if (this.mStatus != Status.PENDING) {
            int i = AnonymousClass4.$SwitchMap$androidx$loader$content$ModernAsyncTask$Status[this.mStatus.ordinal()];
            if (i == 1) {
                throw new IllegalStateException("Cannot execute task: the task is already running.");
            } else if (i != 2) {
                throw new IllegalStateException("We should never reach this state");
            } else {
                throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        } else {
            this.mStatus = Status.RUNNING;
            executor.execute(this.mFuture);
        }
    }

    /* access modifiers changed from: 0000 */
    public void finish(Object obj) {
        if (isCancelled()) {
            onCancelled(obj);
        } else {
            onPostExecute(obj);
        }
        this.mStatus = Status.FINISHED;
    }

    public final boolean isCancelled() {
        return this.mCancelled.get();
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Object obj) {
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Object obj) {
    }

    /* access modifiers changed from: 0000 */
    public void postResult(final Object obj) {
        getHandler().post(new Runnable() {
            public void run() {
                ModernAsyncTask.this.finish(obj);
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void postResultIfNotInvoked(Object obj) {
        if (!this.mTaskInvoked.get()) {
            postResult(obj);
        }
    }
}
