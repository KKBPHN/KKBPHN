package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Arrays;

final class SplitInstalledDisposer implements Runnable {
    private final SplitInstallManagerImpl splitInstallManager;
    private final SplitInstallRequest splitInstallRequest;

    SplitInstalledDisposer(SplitInstallManagerImpl splitInstallManagerImpl, SplitInstallRequest splitInstallRequest2) {
        this.splitInstallManager = splitInstallManagerImpl;
        this.splitInstallRequest = splitInstallRequest2;
    }

    private Bundle makeInstalledSessionState(String[] strArr) {
        Bundle bundle = new Bundle();
        bundle.putInt("session_id", 0);
        bundle.putInt("status", 5);
        bundle.putInt("error_code", 0);
        bundle.putStringArrayList("module_names", new ArrayList(Arrays.asList(strArr)));
        bundle.putLong("total_bytes_to_download", 0);
        bundle.putLong("bytes_downloaded", 0);
        return bundle;
    }

    public void run() {
        this.splitInstallManager.getRegistry().notifyListeners(SplitInstallSessionState.createFrom(makeInstalledSessionState((String[]) this.splitInstallRequest.getModuleNames().toArray(new String[0]))));
    }
}
