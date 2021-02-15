package com.android.camera.module;

import com.android.camera.protocol.ModeProtocol.DollyZoomProcess;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O000Oo0O reason: case insensitive filesystem */
public final /* synthetic */ class C0362O000Oo0O implements Runnable {
    private final /* synthetic */ DollyZoomModule O0OOoO0;
    private final /* synthetic */ DollyZoomProcess O0OOoOO;

    public /* synthetic */ C0362O000Oo0O(DollyZoomModule dollyZoomModule, DollyZoomProcess dollyZoomProcess) {
        this.O0OOoO0 = dollyZoomModule;
        this.O0OOoOO = dollyZoomProcess;
    }

    public final void run() {
        this.O0OOoO0.O000000o(this.O0OOoOO);
    }
}
