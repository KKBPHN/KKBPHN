package com.android.camera.dualvideo.render;

import com.android.gallery3d.ui.GLCanvas;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000OOOo implements Consumer {
    private final /* synthetic */ GLCanvas O0OOoO0;

    public /* synthetic */ O000OOOo(GLCanvas gLCanvas) {
        this.O0OOoO0 = gLCanvas;
    }

    public final void accept(Object obj) {
        ((MiscRenderItem) obj).getBasicTexture().onBind(this.O0OOoO0);
    }
}
