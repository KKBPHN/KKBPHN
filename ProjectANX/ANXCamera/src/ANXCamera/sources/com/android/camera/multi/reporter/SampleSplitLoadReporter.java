package com.android.camera.multi.reporter;

import android.content.Context;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.splitreport.DefaultSplitLoadReporter;
import java.util.List;

public class SampleSplitLoadReporter extends DefaultSplitLoadReporter {
    public SampleSplitLoadReporter(Context context) {
        super(context);
    }

    public void onLoadFailed(String str, @NonNull List list, @NonNull List list2, long j) {
        super.onLoadFailed(str, list, list2, j);
    }

    public void onLoadOK(String str, @NonNull List list, long j) {
        super.onLoadOK(str, list, j);
    }
}
