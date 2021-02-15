package com.iqiyi.android.qigsaw.core;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.iqiyi.android.qigsaw.core.splitinstall.SplitApkInstaller;
import com.iqiyi.android.qigsaw.core.splitinstall.remote.SplitInstallSupervisor;
import java.util.List;

public abstract class ObtainUserConfirmationDialog extends Activity {
    private SplitInstallSupervisor installService;
    private List moduleNames;
    private long realTotalBytesNeedToDownload;
    private int sessionId;

    /* access modifiers changed from: protected */
    public boolean checkInternParametersIllegal() {
        if (this.sessionId != 0 && this.realTotalBytesNeedToDownload > 0) {
            List list = this.moduleNames;
            if (list != null && !list.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public List getModuleNames() {
        return this.moduleNames;
    }

    /* access modifiers changed from: protected */
    public long getRealTotalBytesNeedToDownload() {
        return this.realTotalBytesNeedToDownload;
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.sessionId = getIntent().getIntExtra("sessionId", 0);
        this.realTotalBytesNeedToDownload = getIntent().getLongExtra("realTotalBytesNeedToDownload", 0);
        this.moduleNames = getIntent().getStringArrayListExtra("moduleNames");
        this.installService = SplitApkInstaller.getSplitInstallSupervisor();
    }

    /* access modifiers changed from: protected */
    public void onUserCancel() {
        SplitInstallSupervisor splitInstallSupervisor = this.installService;
        if (splitInstallSupervisor != null) {
            if (splitInstallSupervisor.cancelInstallWithoutUserConfirmation(this.sessionId)) {
                setResult(0);
            }
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onUserConfirm() {
        SplitInstallSupervisor splitInstallSupervisor = this.installService;
        if (splitInstallSupervisor != null) {
            if (splitInstallSupervisor.continueInstallWithUserConfirmation(this.sessionId)) {
                setResult(-1);
            }
            finish();
        }
    }
}
