package com.android.camera.dualvideo;

import com.android.camera2.Camera2Proxy;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000oO0 reason: case insensitive filesystem */
public final /* synthetic */ class C0145O0000oO0 implements Consumer {
    private final /* synthetic */ DualVideoModuleBase O0OOoO0;
    private final /* synthetic */ int O0OOoOO;

    public /* synthetic */ C0145O0000oO0(DualVideoModuleBase dualVideoModuleBase, int i) {
        this.O0OOoO0 = dualVideoModuleBase;
        this.O0OOoOO = i;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, (Camera2Proxy) obj);
    }
}
