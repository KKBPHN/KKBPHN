package com.iqiyi.android.qigsaw.core.splitreport;

import android.content.Context;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import java.util.List;

public class DefaultSplitInstallReporter implements SplitInstallReporter {
    private static final String TAG = "SplitInstallReporter";
    protected final Context context;

    public DefaultSplitInstallReporter(Context context2) {
        this.context = context2;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.iqiyi.android.qigsaw.core.splitreport.SplitInstallError>, for r5v0, types: [java.util.List, java.util.List<com.iqiyi.android.qigsaw.core.splitreport.SplitInstallError>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDeferredInstallFailed(@NonNull List list, @NonNull List<SplitInstallError> list2, long j) {
        for (SplitInstallError splitInstallError : list2) {
            SplitLog.printErrStackTrace(TAG, splitInstallError.cause, "Defer to install split %s failed with error code %d, cost time %d ms.", splitInstallError.splitName, Integer.valueOf(splitInstallError.errorCode), Long.valueOf(j));
        }
    }

    public void onDeferredInstallOK(@NonNull List list, long j) {
        SplitLog.i(TAG, "Deferred install %s OK, cost time %d ms.", list.toString(), Long.valueOf(j));
    }

    public void onStartInstallFailed(@NonNull List list, @NonNull SplitInstallError splitInstallError, long j) {
        SplitLog.printErrStackTrace(TAG, splitInstallError.cause, "Start to install split %s failed, cost time %d ms.", splitInstallError.splitName, Long.valueOf(j));
    }

    public void onStartInstallOK(@NonNull List list, long j) {
        SplitLog.i(TAG, "Start install %s OK, cost time %d ms.", list.toString(), Long.valueOf(j));
    }
}
