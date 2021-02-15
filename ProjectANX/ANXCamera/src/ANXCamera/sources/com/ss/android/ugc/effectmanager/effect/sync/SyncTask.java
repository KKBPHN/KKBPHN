package com.ss.android.ugc.effectmanager.effect.sync;

import androidx.annotation.NonNull;
import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;

public class SyncTask {
    private boolean mCanceled;
    private SyncTaskListener mSyncTaskListener;

    public void cancel() {
        this.mCanceled = true;
    }

    public void execute() {
    }

    public boolean isCanceled() {
        return this.mCanceled;
    }

    public void onFailed(SyncTask syncTask, ExceptionResult exceptionResult) {
        SyncTaskListener syncTaskListener = this.mSyncTaskListener;
        if (syncTaskListener != null) {
            syncTaskListener.onFailed(syncTask, exceptionResult);
        }
    }

    public void onFinally(SyncTask syncTask) {
        SyncTaskListener syncTaskListener = this.mSyncTaskListener;
        if (syncTaskListener != null) {
            syncTaskListener.onFinally(syncTask);
        }
    }

    public void onResponse(SyncTask syncTask, Object obj) {
        SyncTaskListener syncTaskListener = this.mSyncTaskListener;
        if (syncTaskListener != null) {
            syncTaskListener.onResponse(syncTask, obj);
        }
    }

    public void onStart(SyncTask syncTask) {
        SyncTaskListener syncTaskListener = this.mSyncTaskListener;
        if (syncTaskListener != null) {
            syncTaskListener.onStart(syncTask);
        }
    }

    public void setListener(@NonNull SyncTaskListener syncTaskListener) {
        this.mSyncTaskListener = syncTaskListener;
    }
}
