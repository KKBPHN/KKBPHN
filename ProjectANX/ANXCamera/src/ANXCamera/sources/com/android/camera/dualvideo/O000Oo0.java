package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000Oo0 implements Consumer {
    public static final /* synthetic */ O000Oo0 INSTANCE = new O000Oo0();

    private /* synthetic */ O000Oo0() {
    }

    public final void accept(Object obj) {
        ((RenderManager) obj).enableContinuousRender(true);
    }
}
