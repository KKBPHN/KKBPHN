package com.android.camera.zoommap;

import com.android.camera.ui.GLTextureView.EGLShareContextGetter;
import javax.microedition.khronos.egl.EGLContext;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements EGLShareContextGetter {
    private final /* synthetic */ ZoomMapController O0OOoO0;

    public /* synthetic */ O00000Oo(ZoomMapController zoomMapController) {
        this.O0OOoO0 = zoomMapController;
    }

    public final EGLContext getShareContext() {
        return this.O0OOoO0.O00oooOO();
    }
}
