package com.android.camera.module;

import com.android.camera.R;
import com.android.camera.protocol.ModeProtocol.DollyZoomProcess;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O000Oo0o reason: case insensitive filesystem */
public final /* synthetic */ class C0363O000Oo0o implements Runnable {
    private final /* synthetic */ DollyZoomProcess O0OOoO0;

    public /* synthetic */ C0363O000Oo0o(DollyZoomProcess dollyZoomProcess) {
        this.O0OOoO0 = dollyZoomProcess;
    }

    public final void run() {
        this.O0OOoO0.updateCaptureMessage(R.string.dolly_zoom_capture_tip1, false);
    }
}
