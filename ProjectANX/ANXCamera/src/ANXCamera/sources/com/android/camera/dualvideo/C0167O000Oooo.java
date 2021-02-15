package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000Oooo reason: case insensitive filesystem */
public final /* synthetic */ class C0167O000Oooo implements Consumer {
    public static final /* synthetic */ C0167O000Oooo INSTANCE = new C0167O000Oooo();

    private /* synthetic */ C0167O000Oooo() {
    }

    public final void accept(Object obj) {
        ((RenderManager) obj).reStartRenderWindow();
    }
}
