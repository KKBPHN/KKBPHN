package com.android.camera.dualvideo;

import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera2.Camera2Proxy.FocusCallback;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000OoO reason: case insensitive filesystem */
public final /* synthetic */ class C0140O0000OoO implements FocusCallback {
    private final /* synthetic */ DualVideoModuleBase O0OOoO0;

    public /* synthetic */ C0140O0000OoO(DualVideoModuleBase dualVideoModuleBase) {
        this.O0OOoO0 = dualVideoModuleBase;
    }

    public final void onFocusStateChanged(FocusTask focusTask) {
        this.O0OOoO0.O000000o(focusTask);
    }
}
