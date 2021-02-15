package com.android.camera.dualvideo.render;

import com.android.gallery3d.ui.GLCanvas;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000o00o reason: case insensitive filesystem */
public final /* synthetic */ class C0207O000o00o implements Consumer {
    private final /* synthetic */ RenderManager O0OOoO0;
    private final /* synthetic */ GLCanvas O0OOoOO;

    public /* synthetic */ C0207O000o00o(RenderManager renderManager, GLCanvas gLCanvas) {
        this.O0OOoO0 = renderManager;
        this.O0OOoOO = gLCanvas;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O00000Oo(this.O0OOoOO, (CameraItemInterface) obj);
    }
}
