package com.iqiyi.android.qigsaw.core.splitreport;

import androidx.annotation.WorkerThread;
import java.util.List;

public interface SplitUninstallReporter {
    @WorkerThread
    void onSplitUninstallOK(List list, long j);
}
