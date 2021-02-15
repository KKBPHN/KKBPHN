package com.android.camera.multi.reporter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import com.iqiyi.android.qigsaw.core.splitreport.DefaultSplitInstallReporter;
import com.iqiyi.android.qigsaw.core.splitreport.SplitBriefInfo;
import com.iqiyi.android.qigsaw.core.splitreport.SplitInstallError;
import java.util.List;

public class SampleSplitInstallReporter extends DefaultSplitInstallReporter {
    private static final String TAG = "SampleSplitInstallReporter";

    public SampleSplitInstallReporter(Context context) {
        super(context);
    }

    public void onDeferredInstallFailed(@NonNull List list, @NonNull List list2, long j) {
        super.onDeferredInstallFailed(list, list2, j);
    }

    public void onDeferredInstallOK(@NonNull List list, long j) {
        super.onDeferredInstallOK(list, j);
    }

    public void onStartInstallFailed(@NonNull List list, @NonNull SplitInstallError splitInstallError, long j) {
        super.onStartInstallFailed(list, splitInstallError, j);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.iqiyi.android.qigsaw.core.splitreport.SplitBriefInfo>, for r4v0, types: [java.util.List, java.util.List<com.iqiyi.android.qigsaw.core.splitreport.SplitBriefInfo>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @SuppressLint({"LongLogTag"})
    public void onStartInstallOK(@NonNull List<SplitBriefInfo> list, long j) {
        Object[] objArr;
        String str;
        super.onStartInstallOK(list, j);
        for (SplitBriefInfo splitBriefInfo : list) {
            int installFlag = splitBriefInfo.getInstallFlag();
            String str2 = TAG;
            if (installFlag == 2) {
                objArr = new Object[]{splitBriefInfo.splitName};
                str = "Split %s has been installed, don't need delivery this result";
            } else if (splitBriefInfo.getInstallFlag() == 1) {
                objArr = new Object[]{splitBriefInfo.splitName};
                str = "Split %s is installed firstly, you can delivery this result";
            }
            Log.d(str2, String.format(str, objArr));
        }
    }
}
