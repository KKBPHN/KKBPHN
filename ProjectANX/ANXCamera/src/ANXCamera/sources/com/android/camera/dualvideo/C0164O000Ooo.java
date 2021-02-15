package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000Ooo reason: case insensitive filesystem */
public final /* synthetic */ class C0164O000Ooo implements Consumer {
    public static final /* synthetic */ C0164O000Ooo INSTANCE = new C0164O000Ooo();

    private /* synthetic */ C0164O000Ooo() {
    }

    public final void accept(Object obj) {
        ((RenderManager) obj).enableContinuousRender(true);
    }
}
