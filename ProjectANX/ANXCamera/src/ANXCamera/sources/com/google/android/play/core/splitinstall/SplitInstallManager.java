package com.google.android.play.core.splitinstall;

import android.app.Activity;
import com.google.android.play.core.tasks.Task;
import java.util.List;
import java.util.Set;

public interface SplitInstallManager {
    Task cancelInstall(int i);

    Task deferredInstall(List list);

    Task deferredUninstall(List list);

    Set getInstalledModules();

    Task getSessionState(int i);

    Task getSessionStates();

    void registerListener(SplitInstallStateUpdatedListener splitInstallStateUpdatedListener);

    boolean startConfirmationDialogForResult(SplitInstallSessionState splitInstallSessionState, Activity activity, int i);

    Task startInstall(SplitInstallRequest splitInstallRequest);

    void unregisterListener(SplitInstallStateUpdatedListener splitInstallStateUpdatedListener);
}
