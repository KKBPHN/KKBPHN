package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class O000o0 implements Function {
    public static final /* synthetic */ O000o0 INSTANCE = new O000o0();

    private /* synthetic */ O000o0() {
    }

    public final Object apply(Object obj) {
        return Boolean.valueOf(((RenderManager) obj).isSwitching());
    }
}
