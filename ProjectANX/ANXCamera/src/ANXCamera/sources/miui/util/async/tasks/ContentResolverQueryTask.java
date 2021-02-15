package miui.util.async.tasks;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import java.lang.ref.WeakReference;
import miui.util.AppConstants;
import miui.util.Pools;
import miui.util.async.Task;
import miui.util.async.TaskManager;

public abstract class ContentResolverQueryTask extends Task {
    private boolean mAutoRequery;
    private String mDescription;
    private boolean mHasMissingContentChange;
    private boolean mPauseAutoRequery;
    private final String[] mProjection;
    private final String mSelection;
    private final String[] mSelectionArgs;
    private final String mSortOrder;
    private WeakReference mTaskManager;
    private final Uri mUri;
    private UriObserver mUriObserver;

    public class Cookie {
        public Cookie() {
        }

        public void pause() {
            ContentResolverQueryTask.this.pause();
        }

        public void release() {
            ContentResolverQueryTask.this.disableAutoRequery();
        }

        public void resume() {
            ContentResolverQueryTask.this.resume();
        }
    }

    class UriObserver extends ContentObserver {
        private final WeakReference mTask;

        public UriObserver(ContentResolverQueryTask contentResolverQueryTask) {
            super(new Handler(AppConstants.getCurrentApplication().getMainLooper()));
            this.mTask = new WeakReference(contentResolverQueryTask);
        }

        public void onChange(boolean z) {
            ContentResolverQueryTask contentResolverQueryTask = (ContentResolverQueryTask) this.mTask.get();
            if (contentResolverQueryTask != null) {
                contentResolverQueryTask.requery();
            }
        }
    }

    public ContentResolverQueryTask(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        this.mUri = uri;
        this.mProjection = strArr;
        this.mSelection = str;
        this.mSelectionArgs = strArr2;
        this.mSortOrder = str2;
    }

    public void disableAutoRequery() {
        this.mAutoRequery = false;
        if (this.mUriObserver != null) {
            AppConstants.getCurrentApplication().getContentResolver().unregisterContentObserver(this.mUriObserver);
            this.mUriObserver = null;
        }
    }

    public Cookie enableAutoRequery() {
        if (!this.mAutoRequery) {
            this.mAutoRequery = true;
            this.mUriObserver = new UriObserver(this);
            AppConstants.getCurrentApplication().getContentResolver().registerContentObserver(this.mUri, true, this.mUriObserver);
        }
        return new Cookie();
    }

    public String getDescription() {
        if (this.mDescription == null) {
            StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
            String str = "]@";
            if (this.mProjection != null) {
                sb.append('[');
                sb.append(this.mProjection[0]);
                for (int i = 1; i < this.mProjection.length; i++) {
                    sb.append(';');
                    sb.append(this.mProjection[i]);
                }
                sb.append(str);
            }
            sb.append(this.mUri);
            if (this.mSelection != null) {
                sb.append(" WHERE ");
                if (this.mSelectionArgs != null) {
                    sb.append('[');
                    sb.append(this.mSelectionArgs[0]);
                    for (int i2 = 1; i2 < this.mSelectionArgs.length; i2++) {
                        sb.append(';');
                        sb.append(this.mSelectionArgs[i2]);
                    }
                    sb.append(str);
                }
                sb.append(this.mSelection);
            }
            if (this.mSortOrder != null) {
                sb.append(" ORDER BY ");
                sb.append(this.mSortOrder);
            }
            this.mDescription = sb.toString();
            Pools.getStringBuilderPool().release(sb);
        }
        return this.mDescription;
    }

    public void onPrepare(TaskManager taskManager) {
        this.mTaskManager = new WeakReference(taskManager);
    }

    /* access modifiers changed from: 0000 */
    public void pause() {
        this.mPauseAutoRequery = true;
    }

    /* access modifiers changed from: protected */
    public Cursor query() {
        return AppConstants.getCurrentApplication().getContentResolver().query(this.mUri, this.mProjection, this.mSelection, this.mSelectionArgs, this.mSortOrder);
    }

    /* access modifiers changed from: 0000 */
    public void requery() {
        if (this.mPauseAutoRequery) {
            this.mHasMissingContentChange = true;
            return;
        }
        WeakReference weakReference = this.mTaskManager;
        if (weakReference != null) {
            TaskManager taskManager = (TaskManager) weakReference.get();
            if (taskManager != null) {
                restart();
                taskManager.add(this);
                return;
            }
            disableAutoRequery();
        }
    }

    /* access modifiers changed from: 0000 */
    public void resume() {
        this.mPauseAutoRequery = false;
        if (this.mHasMissingContentChange) {
            this.mHasMissingContentChange = false;
            requery();
        }
    }
}
