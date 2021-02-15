package com.iqiyi.android.qigsaw.core.splitinstall;

import java.util.List;
import java.util.concurrent.Executor;

final class SplitSessionInstallerImpl implements SplitSessionInstaller {
    private final Executor executor;
    private final SplitInstallSessionManager sessionManager;
    private final SplitInstaller splitInstaller;

    SplitSessionInstallerImpl(SplitInstaller splitInstaller2, SplitInstallSessionManager splitInstallSessionManager, Executor executor2) {
        this.splitInstaller = splitInstaller2;
        this.sessionManager = splitInstallSessionManager;
        this.executor = executor2;
    }

    public void install(int i, List list) {
        this.executor.execute(new SplitStartInstallTask(i, this.splitInstaller, this.sessionManager, list));
    }
}
