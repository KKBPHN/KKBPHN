package com.iqiyi.android.qigsaw.core.splitreport;

import android.content.Context;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import java.util.List;

public class DefaultSplitLoadReporter implements SplitLoadReporter {
    private static final String TAG = "SplitLoadReporter";
    protected final Context context;

    public DefaultSplitLoadReporter(Context context2) {
        this.context = context2;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.iqiyi.android.qigsaw.core.splitreport.SplitLoadError>, for r6v0, types: [java.util.List, java.util.List<com.iqiyi.android.qigsaw.core.splitreport.SplitLoadError>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onLoadFailed(String str, @NonNull List list, @NonNull List<SplitLoadError> list2, long j) {
        for (SplitLoadError splitLoadError : list2) {
            SplitLog.printErrStackTrace(TAG, splitLoadError.cause, "Failed to load split %s in process %s cost %d ms, error code: %d!", splitLoadError.splitName, str, Long.valueOf(j), Integer.valueOf(splitLoadError.errorCode));
        }
    }

    public void onLoadOK(String str, @NonNull List list, long j) {
        SplitLog.i(TAG, "Success to load %s in process %s cost %d ms!", list, str, Long.valueOf(j));
    }
}
