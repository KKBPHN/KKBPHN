package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000OoO0 reason: case insensitive filesystem */
public final /* synthetic */ class C0161O000OoO0 implements Consumer {
    private final /* synthetic */ DualVideoRecordModule O0OOoO0;

    public /* synthetic */ C0161O000OoO0(DualVideoRecordModule dualVideoRecordModule) {
        this.O0OOoO0 = dualVideoRecordModule;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O00000oO((RenderManager) obj);
    }
}
