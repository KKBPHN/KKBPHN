package com.android.camera.dualvideo;

import com.android.camera.protocol.ModeProtocol.ActionProcessing;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000oo0 reason: case insensitive filesystem */
public final /* synthetic */ class C0149O0000oo0 implements Runnable {
    private final /* synthetic */ ActionProcessing O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;

    public /* synthetic */ C0149O0000oo0(ActionProcessing actionProcessing, boolean z) {
        this.O0OOoO0 = actionProcessing;
        this.O0OOoOO = z;
    }

    public final void run() {
        this.O0OOoO0.switchModeOrExternalTipLayout(this.O0OOoOO);
    }
}
