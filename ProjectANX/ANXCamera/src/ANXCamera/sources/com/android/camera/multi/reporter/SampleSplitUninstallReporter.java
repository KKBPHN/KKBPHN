package com.android.camera.multi.reporter;

import android.content.Context;
import com.iqiyi.android.qigsaw.core.splitreport.DefaultSplitUninstallReporter;
import java.util.List;

public class SampleSplitUninstallReporter extends DefaultSplitUninstallReporter {
    public SampleSplitUninstallReporter(Context context) {
        super(context);
    }

    public void onSplitUninstallOK(List list, long j) {
        super.onSplitUninstallOK(list, j);
    }
}
