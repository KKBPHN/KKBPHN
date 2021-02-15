package com.android.camera.upgrade;

import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Runnable {
    private final /* synthetic */ long O0OOoO0;

    public /* synthetic */ O000000o(long j) {
        this.O0OOoO0 = j;
    }

    public final void run() {
        DataRepository.dataItemGlobal().editor().putLong(CameraSettings.KEY_MIUICAMERA_VERSION_CODE, this.O0OOoO0).apply();
    }
}
