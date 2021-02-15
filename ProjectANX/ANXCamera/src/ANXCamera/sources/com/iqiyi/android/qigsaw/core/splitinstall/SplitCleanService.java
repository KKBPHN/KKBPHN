package com.iqiyi.android.qigsaw.core.splitinstall;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitCleanService extends IntentService {
    public SplitCleanService() {
        super("qigsaw_split_clean");
    }

    private void doClean() {
        SplitPathManager.require().clearCache();
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(@Nullable Intent intent) {
        try {
            doClean();
        } catch (Exception unused) {
        }
    }
}
