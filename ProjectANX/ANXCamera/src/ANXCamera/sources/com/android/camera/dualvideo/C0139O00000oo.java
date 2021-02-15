package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O00000oo reason: case insensitive filesystem */
public final /* synthetic */ class C0139O00000oo implements Consumer {
    public static final /* synthetic */ C0139O00000oo INSTANCE = new C0139O00000oo();

    private /* synthetic */ C0139O00000oo() {
    }

    public final void accept(Object obj) {
        ((RenderManager) obj).updateRenderData();
    }
}
