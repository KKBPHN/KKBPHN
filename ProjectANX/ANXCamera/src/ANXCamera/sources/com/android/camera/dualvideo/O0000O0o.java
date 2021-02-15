package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class O0000O0o implements Function {
    public static final /* synthetic */ O0000O0o INSTANCE = new O0000O0o();

    private /* synthetic */ O0000O0o() {
    }

    public final Object apply(Object obj) {
        return Boolean.valueOf(((RenderManager) obj).isZoomEnabled());
    }
}
