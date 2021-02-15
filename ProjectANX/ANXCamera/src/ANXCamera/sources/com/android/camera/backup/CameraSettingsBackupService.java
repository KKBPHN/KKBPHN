package com.android.camera.backup;

import android.content.Intent;
import com.android.camera.log.Log;
import miui.cloud.backup.CloudBackupServiceBase;
import miui.cloud.backup.ICloudBackup;

public class CameraSettingsBackupService extends CloudBackupServiceBase {
    /* access modifiers changed from: protected */
    public ICloudBackup getBackupImpl() {
        return new CameraSettingsBackupImpl();
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        try {
            CameraSettingsBackupService.super.onHandleIntent(intent);
        } catch (Exception e) {
            Log.e("", "exception when onHandleIntent ", (Throwable) e);
        }
    }
}
