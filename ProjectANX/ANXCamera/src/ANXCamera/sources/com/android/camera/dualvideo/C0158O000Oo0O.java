package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Function;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000Oo0O reason: case insensitive filesystem */
public final /* synthetic */ class C0158O000Oo0O implements Function {
    public static final /* synthetic */ C0158O000Oo0O INSTANCE = new C0158O000Oo0O();

    private /* synthetic */ C0158O000Oo0O() {
    }

    public final Object apply(Object obj) {
        return Integer.valueOf(((RenderManager) obj).mStatCaptureTimes);
    }
}
