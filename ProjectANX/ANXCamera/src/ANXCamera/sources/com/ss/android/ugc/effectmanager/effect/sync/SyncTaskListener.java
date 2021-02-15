package com.ss.android.ugc.effectmanager.effect.sync;

import com.ss.android.ugc.effectmanager.common.task.ExceptionResult;

public interface SyncTaskListener {
    void onFailed(SyncTask syncTask, ExceptionResult exceptionResult);

    void onFinally(SyncTask syncTask);

    void onResponse(SyncTask syncTask, Object obj);

    void onStart(SyncTask syncTask);
}
