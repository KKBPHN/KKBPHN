package com.android.camera.multi.reporter;

import android.content.Context;
import com.iqiyi.android.qigsaw.core.splitreport.DefaultSplitUpdateReporter;
import java.util.List;

public class SampleSplitUpdateReporter extends DefaultSplitUpdateReporter {
    public SampleSplitUpdateReporter(Context context) {
        super(context);
    }

    public void onNewSplitInfoVersionLoaded(String str) {
        super.onNewSplitInfoVersionLoaded(str);
    }

    public void onUpdateFailed(String str, String str2, int i) {
        super.onUpdateFailed(str, str2, i);
    }

    public void onUpdateOK(String str, String str2, List list) {
        super.onUpdateOK(str, str2, list);
    }
}
