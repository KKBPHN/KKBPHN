package com.android.camera.dualvideo.render;

import com.android.gallery3d.ui.GLCanvas;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oO0O reason: case insensitive filesystem */
public final /* synthetic */ class C0218O000oO0O implements Consumer {
    private final /* synthetic */ GLCanvas O0OOoO0;

    public /* synthetic */ C0218O000oO0O(GLCanvas gLCanvas) {
        this.O0OOoO0 = gLCanvas;
    }

    public final void accept(Object obj) {
        ((RenderSource) obj).attachToGL(this.O0OOoO0);
    }
}
