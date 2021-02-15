package com.android.camera.dualvideo;

import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000ooO reason: case insensitive filesystem */
public final /* synthetic */ class C0150O0000ooO implements Consumer {
    private final /* synthetic */ DualVideoModuleBase O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;

    public /* synthetic */ C0150O0000ooO(DualVideoModuleBase dualVideoModuleBase, boolean z) {
        this.O0OOoO0 = dualVideoModuleBase;
        this.O0OOoOO = z;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, (ActionProcessing) obj);
    }
}
