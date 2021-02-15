package com.iqiyi.android.qigsaw.core.splitreport;

import android.content.Context;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import java.util.List;

public class DefaultSplitUpdateReporter implements SplitUpdateReporter {
    private static final String TAG = "SplitUpdateReporter";
    protected final Context context;

    public DefaultSplitUpdateReporter(Context context2) {
        this.context = context2;
    }

    public void onNewSplitInfoVersionLoaded(String str) {
        SplitLog.i(TAG, "Success to load new split info version ", str);
    }

    public void onUpdateFailed(String str, String str2, int i) {
        SplitLog.i(TAG, "Failed to update version from %s to %s, errorCode %d.", str, str2, Integer.valueOf(i));
    }

    public void onUpdateOK(String str, String str2, List list) {
        SplitLog.i(TAG, "Success to update version from %s to %s, update splits: %s.", str, str2, list.toString());
    }
}
