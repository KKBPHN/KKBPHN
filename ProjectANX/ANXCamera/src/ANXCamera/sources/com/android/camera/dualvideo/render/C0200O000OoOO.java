package com.android.camera.dualvideo.render;

import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000OoOO reason: case insensitive filesystem */
public final /* synthetic */ class C0200O000OoOO implements Consumer {
    public static final /* synthetic */ C0200O000OoOO INSTANCE = new C0200O000OoOO();

    private /* synthetic */ C0200O000OoOO() {
    }

    public final void accept(Object obj) {
        ((RenderSource) obj).getSurfaceTexture().updateTexImage();
    }
}
