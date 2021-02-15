package com.iqiyi.android.qigsaw.core.splitinstall;

import java.util.Collection;

final class SplitDeferredInstallTask extends SplitInstallTask {
    SplitDeferredInstallTask(SplitInstaller splitInstaller, Collection collection) {
        super(splitInstaller, collection);
    }

    /* access modifiers changed from: 0000 */
    public boolean isStartInstallOperation() {
        return false;
    }
}
