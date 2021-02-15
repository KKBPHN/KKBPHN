package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitinstall.SplitPendingUninstallManager;
import com.iqiyi.android.qigsaw.core.splitinstall.SplitUninstallReporterManager;
import com.iqiyi.android.qigsaw.core.splitreport.SplitUninstallReporter;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class SplitStartUninstallTask implements Runnable {
    private static final String TAG = "SplitStartUninstallTask";
    private final List uninstallSplits;

    SplitStartUninstallTask(List list) {
        this.uninstallSplits = list;
    }

    public void run() {
        String str;
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList(this.uninstallSplits.size());
        Iterator it = this.uninstallSplits.iterator();
        while (true) {
            boolean hasNext = it.hasNext();
            str = TAG;
            if (!hasNext) {
                break;
            }
            SplitInfo splitInfo = (SplitInfo) it.next();
            SplitLog.d(str, "split %s need to be uninstalled, try to delete its files", splitInfo.getSplitName());
            FileUtil.deleteDir(SplitPathManager.require().getSplitRootDir(splitInfo));
            arrayList.add(splitInfo.getSplitName());
        }
        SplitUninstallReporter uninstallReporter = SplitUninstallReporterManager.getUninstallReporter();
        if (uninstallReporter != null) {
            uninstallReporter.onSplitUninstallOK(arrayList, System.currentTimeMillis() - currentTimeMillis);
        }
        Object[] objArr = new Object[1];
        objArr[0] = new SplitPendingUninstallManager().deletePendingUninstallSplitsRecord() ? "Succeed" : "Failed";
        SplitLog.d(str, "%s to delete record file of pending uninstall splits!", objArr);
    }
}
