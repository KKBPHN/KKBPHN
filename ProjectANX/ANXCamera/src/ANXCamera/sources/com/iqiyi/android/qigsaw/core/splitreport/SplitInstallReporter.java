package com.iqiyi.android.qigsaw.core.splitreport;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import java.util.List;

public interface SplitInstallReporter {
    @WorkerThread
    void onDeferredInstallFailed(@NonNull List list, @NonNull List list2, long j);

    @WorkerThread
    void onDeferredInstallOK(@NonNull List list, long j);

    @WorkerThread
    void onStartInstallFailed(@NonNull List list, @NonNull SplitInstallError splitInstallError, long j);

    @WorkerThread
    void onStartInstallOK(@NonNull List list, long j);
}
