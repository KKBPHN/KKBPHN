package com.android.camera.dualvideo;

import com.android.camera.protocol.ModeProtocol.ActionProcessing;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000oo reason: case insensitive filesystem */
public final /* synthetic */ class C0148O0000oo implements Runnable {
    private final /* synthetic */ ActionProcessing O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;

    public /* synthetic */ C0148O0000oo(ActionProcessing actionProcessing, boolean z) {
        this.O0OOoO0 = actionProcessing;
        this.O0OOoOO = z;
    }

    public final void run() {
        this.O0OOoO0.switchThumbnailFunction(this.O0OOoOO);
    }
}
