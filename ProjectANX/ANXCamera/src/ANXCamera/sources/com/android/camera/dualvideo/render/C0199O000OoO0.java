package com.android.camera.dualvideo.render;

import android.util.Size;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000OoO0 reason: case insensitive filesystem */
public final /* synthetic */ class C0199O000OoO0 implements Consumer {
    private final /* synthetic */ int O0OOoO0;
    private final /* synthetic */ Size O0OOoOO;

    public /* synthetic */ C0199O000OoO0(int i, Size size) {
        this.O0OOoO0 = i;
        this.O0OOoOO = size;
    }

    public final void accept(Object obj) {
        RenderManager.O000000o(this.O0OOoO0, this.O0OOoOO, (RenderSource) obj);
    }
}
