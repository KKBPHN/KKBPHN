package com.iqiyi.android.qigsaw.core.splitreport;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import java.util.List;

public interface SplitUpdateReporter {
    @MainThread
    void onNewSplitInfoVersionLoaded(String str);

    @WorkerThread
    void onUpdateFailed(String str, String str2, int i);

    @WorkerThread
    void onUpdateOK(String str, String str2, List list);
}
