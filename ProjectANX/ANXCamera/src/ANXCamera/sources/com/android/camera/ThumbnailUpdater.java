package com.android.camera;

import android.content.ContentResolver;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import java.io.File;

public class ThumbnailUpdater {
    private static final String TAG = "ThumbnailUpdater";
    /* access modifiers changed from: private */
    public ActivityBase mActivityBase;
    /* access modifiers changed from: private */
    public ContentResolver mContentResolver = this.mActivityBase.getContentResolver();
    private AsyncTask mLoadThumbnailTask;
    /* access modifiers changed from: private */
    public Thumbnail mThumbnail;
    private Rect mViewRect;

    class LoadThumbnailTask extends AsyncTask {
        private boolean mLookAtCache;

        public LoadThumbnailTask(boolean z) {
            this.mLookAtCache = z;
        }

        /* access modifiers changed from: protected */
        public Thumbnail doInBackground(Void... voidArr) {
            StringBuilder sb;
            int i;
            String str;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("LoadThumbnailTask execute, lookatcache=");
            sb2.append(this.mLookAtCache);
            String sb3 = sb2.toString();
            String str2 = ThumbnailUpdater.TAG;
            Log.d(str2, sb3);
            if (isCancelled()) {
                return null;
            }
            if (ThumbnailUpdater.this.mThumbnail != null) {
                Uri uri = ThumbnailUpdater.this.mThumbnail.getUri();
                if (Util.isUriValid(uri, ThumbnailUpdater.this.mContentResolver)) {
                    if (uri.equals(Thumbnail.getLastThumbnailUri(ThumbnailUpdater.this.mContentResolver))) {
                        return ThumbnailUpdater.this.mThumbnail;
                    }
                    this.mLookAtCache = true;
                }
            }
            Uri lastThumbnailUri = Thumbnail.getLastThumbnailUri(ThumbnailUpdater.this.mContentResolver);
            Thumbnail createThumbnailFromUri = (lastThumbnailUri == null || ((ThumbnailUpdater.this.mActivityBase.startFromSecureKeyguard() || ThumbnailUpdater.this.mActivityBase.isGalleryLocked()) && (ThumbnailUpdater.this.mActivityBase.getSecureUriList() == null || ThumbnailUpdater.this.mActivityBase.getSecureUriList().size() <= 0)) || !this.mLookAtCache) ? null : Thumbnail.createThumbnailFromUri(ThumbnailUpdater.this.mActivityBase, lastThumbnailUri, false);
            if (isCancelled()) {
                return null;
            }
            Thumbnail[] thumbnailArr = new Thumbnail[1];
            if (ThumbnailUpdater.this.mActivityBase.startFromSecureKeyguard() || ThumbnailUpdater.this.mActivityBase.isGalleryLocked()) {
                i = Thumbnail.getLastThumbnailFromUriList(ThumbnailUpdater.this.mActivityBase, thumbnailArr, ThumbnailUpdater.this.mActivityBase.getSecureUriList(), lastThumbnailUri);
                sb = new StringBuilder();
                str = "get last thumbnail from uri list, code is ";
            } else {
                i = Thumbnail.getLastThumbnailFromContentResolver(ThumbnailUpdater.this.mActivityBase, thumbnailArr, lastThumbnailUri);
                sb = new StringBuilder();
                str = "get last thumbnail from provider, code is ";
            }
            sb.append(str);
            sb.append(i);
            Log.d(str2, sb.toString());
            if (i == -1) {
                return createThumbnailFromUri;
            }
            if (i == 0) {
                return null;
            }
            if (i == 1) {
                return thumbnailArr[0];
            }
            if (i != 2) {
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Thumbnail thumbnail) {
            StringBuilder sb = new StringBuilder();
            sb.append("LoadThumbnailTask onPostExecute, thumbnail is ");
            sb.append(thumbnail);
            sb.append(isCancelled() ? ", canceled" : ", not canceled");
            Log.d(ThumbnailUpdater.TAG, sb.toString());
            if (!isCancelled()) {
                ThumbnailUpdater.this.setThumbnail(thumbnail, true, false);
            }
        }
    }

    class SaveThumbnailTask extends AsyncTask {
        private SaveThumbnailTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Thumbnail... thumbnailArr) {
            int length = thumbnailArr.length;
            File filesDir = ThumbnailUpdater.this.mActivityBase.getFilesDir();
            for (int i = 0; i < length; i++) {
                if (thumbnailArr[i] != null) {
                    thumbnailArr[i].saveLastThumbnailToFile(filesDir);
                }
            }
            return null;
        }
    }

    public ThumbnailUpdater(ActivityBase activityBase) {
        this.mActivityBase = activityBase;
    }

    public void cancelTask() {
        AsyncTask asyncTask = this.mLoadThumbnailTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            this.mLoadThumbnailTask = null;
        }
    }

    public void getLastThumbnail() {
        StringBuilder sb = new StringBuilder();
        sb.append("getLastThumbnail, current thumbnailtask is ");
        sb.append(this.mLoadThumbnailTask);
        Log.d(TAG, sb.toString());
        AsyncTask asyncTask = this.mLoadThumbnailTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        this.mLoadThumbnailTask = new LoadThumbnailTask(true).execute(new Void[0]);
    }

    public void getLastThumbnailUncached() {
        AsyncTask asyncTask = this.mLoadThumbnailTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        this.mLoadThumbnailTask = new LoadThumbnailTask(false).execute(new Void[0]);
    }

    public Thumbnail getThumbnail() {
        return this.mThumbnail;
    }

    public Rect getViewRect() {
        return this.mViewRect;
    }

    public void saveThumbnailToFile() {
        Thumbnail thumbnail = this.mThumbnail;
        if (thumbnail != null && !thumbnail.fromFile()) {
            new SaveThumbnailTask().execute(new Thumbnail[]{this.mThumbnail});
        }
    }

    public void setThumbnail(Thumbnail thumbnail, boolean z, boolean z2) {
        this.mThumbnail = thumbnail;
        if (z) {
            updateThumbnailView(z2);
        }
    }

    public void setViewRect(Rect rect) {
        this.mViewRect = rect;
    }

    public void updatePreviewThumbnailUri(Uri uri) {
        Thumbnail thumbnail = this.mThumbnail;
        if (thumbnail != null) {
            thumbnail.setUri(uri);
        }
    }

    public void updateThumbnailView(final boolean z) {
        this.mActivityBase.runOnUiThread(new Runnable() {
            public void run() {
                ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                if (actionProcessing == null) {
                    Log.e(ThumbnailUpdater.TAG, "won't update thumbnail", (Throwable) new RuntimeException());
                    return;
                }
                actionProcessing.updateThumbnail(ThumbnailUpdater.this.mThumbnail, z, ThumbnailUpdater.this.mActivityBase.hashCode());
            }
        });
    }
}
