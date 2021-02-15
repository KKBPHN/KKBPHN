package com.android.camera.multi;

import com.android.camera.log.Log;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;

public class SplitUpdateListenerAdapter implements SplitInstallStateUpdatedListener {
    public void onStateUpdate(SplitInstallSessionState splitInstallSessionState) {
        String str;
        int status = splitInstallSessionState.status();
        String str2 = "SplitTest:";
        switch (status) {
            case 2:
                str = "DOWNLOADING";
                break;
            case 3:
                str = "DOWNLOADED";
                break;
            case 4:
                str = "INSTALLING";
                break;
            case 5:
                str = "INSTALLED";
                break;
            case 6:
                str = "FAILED";
                break;
            default:
                return;
        }
        Log.e(str2, str);
    }
}
