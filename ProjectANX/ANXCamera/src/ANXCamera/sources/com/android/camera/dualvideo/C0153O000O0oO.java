package com.android.camera.dualvideo;

import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000O0oO reason: case insensitive filesystem */
public final /* synthetic */ class C0153O000O0oO implements Consumer {
    private final /* synthetic */ DualVideoModuleBase O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;

    public /* synthetic */ C0153O000O0oO(DualVideoModuleBase dualVideoModuleBase, boolean z) {
        this.O0OOoO0 = dualVideoModuleBase;
        this.O0OOoOO = z;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O00000o0(this.O0OOoOO, (ActionProcessing) obj);
    }
}
