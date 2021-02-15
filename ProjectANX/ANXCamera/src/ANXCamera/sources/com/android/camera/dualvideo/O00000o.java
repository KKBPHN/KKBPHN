package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements Consumer {
    public static final /* synthetic */ O00000o INSTANCE = new O00000o();

    private /* synthetic */ O00000o() {
    }

    public final void accept(Object obj) {
        ((RenderManager) obj).release();
    }
}
