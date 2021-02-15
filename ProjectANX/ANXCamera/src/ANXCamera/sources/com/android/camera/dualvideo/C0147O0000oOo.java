package com.android.camera.dualvideo;

import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000oOo reason: case insensitive filesystem */
public final /* synthetic */ class C0147O0000oOo implements Consumer {
    private final /* synthetic */ boolean O0OOoO0;

    public /* synthetic */ C0147O0000oOo(boolean z) {
        this.O0OOoO0 = z;
    }

    public final void accept(Object obj) {
        ((ActionProcessing) obj).showOrHideBottom(this.O0OOoO0);
    }
}
