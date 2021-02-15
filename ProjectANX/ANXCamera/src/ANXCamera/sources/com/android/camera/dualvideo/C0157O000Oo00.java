package com.android.camera.dualvideo;

import com.android.camera.protocol.ModeProtocol.TopAlert;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000Oo00 reason: case insensitive filesystem */
public final /* synthetic */ class C0157O000Oo00 implements Consumer {
    public static final /* synthetic */ C0157O000Oo00 INSTANCE = new C0157O000Oo00();

    private /* synthetic */ C0157O000Oo00() {
    }

    public final void accept(Object obj) {
        ((TopAlert) obj).alertDualVideoHint(8, ModuleUtil.getTopTipRes());
    }
}
