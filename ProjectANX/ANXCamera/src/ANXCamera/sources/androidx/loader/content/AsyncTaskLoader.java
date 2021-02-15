package androidx.loader.content;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.OperationCanceledException;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public abstract class AsyncTaskLoader extends Loader {
    private static final boolean DEBUG = false;
    private static final String TAG = "AsyncTaskLoader";
    private volatile LoadTask mCancellingTask;
    private Executor mExecutor;
    private Handler mHandler;
    private long mLastLoadCompleteTime = -10000;
    private volatile LoadTask mTask;
    private long mUpdateThrottle;

    final class LoadTask extends ModernAsyncTask implements Runnable {
        boolean waiting;

        LoadTask() {
        }

        /* JADX WARNING: type inference failed for: r1v1, types: [androidx.loader.content.ModernAsyncTask] */
        /* JADX WARNING: type inference failed for: r1v5 */
        /* JADX WARNING: type inference failed for: r1v6 */
        /* access modifiers changed from: protected */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public Object doInBackground() {
            try {
                this = this;
                this = AsyncTaskLoader.this.onLoadInBackground();
                r1 = this;
                return this;
            } catch (OperationCanceledException e) {
                if (r1.isCancelled()) {
                    return null;
                }
                throw e;
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled(Object obj) {
            AsyncTaskLoader.this.dispatchOnCancelled(this, obj);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Object obj) {
            AsyncTaskLoader.this.dispatchOnLoadComplete(this, obj);
        }

        public void run() {
            this.waiting = false;
            AsyncTaskLoader.this.executePendingTask();
        }
    }

    public AsyncTaskLoader(@NonNull Context context) {
        super(context);
    }

    public void cancelLoadInBackground() {
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnCancelled(LoadTask loadTask, Object obj) {
        onCanceled(obj);
        if (this.mCancellingTask == loadTask) {
            rollbackContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mCancellingTask = null;
            deliverCancellation();
            executePendingTask();
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnLoadComplete(LoadTask loadTask, Object obj) {
        if (this.mTask != loadTask) {
            dispatchOnCancelled(loadTask, obj);
        } else if (isAbandoned()) {
            onCanceled(obj);
        } else {
            commitContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mTask = null;
            deliverResult(obj);
        }
    }

    @Deprecated
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str2;
        super.dump(str, fileDescriptor, printWriter, strArr);
        String str3 = " waiting=";
        if (this.mTask != null) {
            printWriter.print(str);
            printWriter.print("mTask=");
            printWriter.print(this.mTask);
            printWriter.print(str3);
            printWriter.println(this.mTask.waiting);
        }
        if (this.mCancellingTask != null) {
            printWriter.print(str);
            printWriter.print("mCancellingTask=");
            printWriter.print(this.mCancellingTask);
            printWriter.print(str3);
            printWriter.println(this.mCancellingTask.waiting);
        }
        if (this.mUpdateThrottle != 0) {
            printWriter.print(str);
            printWriter.print("mUpdateThrottle=");
            printWriter.print(DateUtils.formatElapsedTime(TimeUnit.MILLISECONDS.toSeconds(this.mUpdateThrottle)));
            printWriter.print(" mLastLoadCompleteTime=");
            if (this.mLastLoadCompleteTime == -10000) {
                str2 = "--";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("-");
                sb.append(DateUtils.formatElapsedTime(TimeUnit.MILLISECONDS.toSeconds(SystemClock.uptimeMillis() - this.mLastLoadCompleteTime)));
                str2 = sb.toString();
            }
            printWriter.print(str2);
            printWriter.println();
        }
    }

    /* access modifiers changed from: 0000 */
    public void executePendingTask() {
        if (this.mCancellingTask == null && this.mTask != null) {
            if (this.mTask.waiting) {
                this.mTask.waiting = false;
                this.mHandler.removeCallbacks(this.mTask);
            }
            if (this.mUpdateThrottle <= 0 || SystemClock.uptimeMillis() >= this.mLastLoadCompleteTime + this.mUpdateThrottle) {
                if (this.mExecutor == null) {
                    this.mExecutor = getExecutor();
                }
                this.mTask.executeOnExecutor(this.mExecutor);
            } else {
                this.mTask.waiting = true;
                this.mHandler.postAtTime(this.mTask, this.mLastLoadCompleteTime + this.mUpdateThrottle);
            }
        }
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Executor getExecutor() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

    public boolean isLoadInBackgroundCanceled() {
        return this.mCancellingTask != null;
    }

    @Nullable
    public abstract Object loadInBackground();

    /* access modifiers changed from: protected */
    public boolean onCancelLoad() {
        if (this.mTask == null) {
            return false;
        }
        if (!isStarted()) {
            onContentChanged();
        }
        if (this.mCancellingTask != null) {
            if (this.mTask.waiting) {
                this.mTask.waiting = false;
                this.mHandler.removeCallbacks(this.mTask);
            }
            this.mTask = null;
            return false;
        } else if (this.mTask.waiting) {
            this.mTask.waiting = false;
            this.mHandler.removeCallbacks(this.mTask);
            this.mTask = null;
            return false;
        } else {
            boolean cancel = this.mTask.cancel(false);
            if (cancel) {
                this.mCancellingTask = this.mTask;
                cancelLoadInBackground();
            }
            this.mTask = null;
            return cancel;
        }
    }

    public void onCanceled(@Nullable Object obj) {
    }

    /* access modifiers changed from: protected */
    public void onForceLoad() {
        super.onForceLoad();
        cancelLoad();
        this.mTask = new LoadTask();
        executePendingTask();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Object onLoadInBackground() {
        return loadInBackground();
    }

    public void setUpdateThrottle(long j) {
        this.mUpdateThrottle = j;
        if (j != 0) {
            this.mHandler = new Handler();
        }
    }
}
