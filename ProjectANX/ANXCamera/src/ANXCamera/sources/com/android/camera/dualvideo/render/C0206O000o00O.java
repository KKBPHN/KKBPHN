package com.android.camera.dualvideo.render;

import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000o00O reason: case insensitive filesystem */
public final /* synthetic */ class C0206O000o00O implements Consumer {
    public static final /* synthetic */ C0206O000o00O INSTANCE = new C0206O000o00O();

    private /* synthetic */ C0206O000o00O() {
    }

    public final void accept(Object obj) {
        ((RenderSource) obj).getSurfaceTexture().updateTexImage();
    }
}
