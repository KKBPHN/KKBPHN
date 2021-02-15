package com.iqiyi.android.qigsaw.core.splitinstall;

import com.iqiyi.android.qigsaw.core.splitreport.SplitBriefInfo;
import com.iqiyi.android.qigsaw.core.splitreport.SplitInstallError;
import com.iqiyi.android.qigsaw.core.splitreport.SplitInstallReporter;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class SplitInstallTask implements Runnable {
    private final SplitInstaller installer;
    private final Collection needUpdateSplits;

    SplitInstallTask(SplitInstaller splitInstaller, Collection collection) {
        this.installer = splitInstaller;
        this.needUpdateSplits = collection;
    }

    public abstract boolean isStartInstallOperation();

    /* access modifiers changed from: 0000 */
    public void onInstallCompleted(List list) {
    }

    /* access modifiers changed from: 0000 */
    public void onInstallFailed(List list) {
    }

    /* access modifiers changed from: protected */
    public void onPreInstall() {
    }

    public final void run() {
        onPreInstall();
        long currentTimeMillis = System.currentTimeMillis();
        boolean isStartInstallOperation = isStartInstallOperation();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(this.needUpdateSplits.size());
        ArrayList arrayList3 = new ArrayList();
        boolean z = true;
        for (SplitInfo splitInfo : this.needUpdateSplits) {
            SplitBriefInfo splitBriefInfo = new SplitBriefInfo(splitInfo.getSplitName(), splitInfo.getSplitVersion(), splitInfo.isBuiltIn());
            try {
                InstallResult install = this.installer.install(isStartInstallOperation, splitInfo);
                splitBriefInfo.setInstallFlag(install.firstInstalled ? 1 : 2);
                arrayList2.add(splitBriefInfo);
                arrayList.add(install);
            } catch (InstallException e) {
                arrayList3.add(new SplitInstallError(splitBriefInfo, e.getErrorCode(), e.getCause()));
                z = false;
                if (isStartInstallOperation) {
                    break;
                }
            }
        }
        SplitInstallReporter installReporter = SplitInstallReporterManager.getInstallReporter();
        if (z) {
            onInstallCompleted(arrayList);
            if (installReporter == null) {
                return;
            }
            if (isStartInstallOperation) {
                installReporter.onStartInstallOK(arrayList2, System.currentTimeMillis() - currentTimeMillis);
            } else {
                installReporter.onDeferredInstallOK(arrayList2, System.currentTimeMillis() - currentTimeMillis);
            }
        } else {
            onInstallFailed(arrayList3);
            if (installReporter == null) {
                return;
            }
            if (isStartInstallOperation) {
                installReporter.onStartInstallFailed(arrayList2, (SplitInstallError) arrayList3.get(0), System.currentTimeMillis() - currentTimeMillis);
            } else {
                installReporter.onDeferredInstallFailed(arrayList2, arrayList3, System.currentTimeMillis() - currentTimeMillis);
            }
        }
    }
}
