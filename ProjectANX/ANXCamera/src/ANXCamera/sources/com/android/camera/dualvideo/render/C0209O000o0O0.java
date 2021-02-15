package com.android.camera.dualvideo.render;

import com.android.gallery3d.ui.GLCanvas;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000o0O0 reason: case insensitive filesystem */
public final /* synthetic */ class C0209O000o0O0 implements Consumer {
    private final /* synthetic */ RenderManager O0OOoO0;
    private final /* synthetic */ GLCanvas O0OOoOO;

    public /* synthetic */ C0209O000o0O0(RenderManager renderManager, GLCanvas gLCanvas) {
        this.O0OOoO0 = renderManager;
        this.O0OOoOO = gLCanvas;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, (CameraItemInterface) obj);
    }
}
