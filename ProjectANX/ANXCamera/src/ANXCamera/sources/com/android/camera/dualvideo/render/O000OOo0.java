package com.android.camera.dualvideo.render;

import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000OOo0 implements Consumer {
    public static final /* synthetic */ O000OOo0 INSTANCE = new O000OOo0();

    private /* synthetic */ O000OOo0() {
    }

    public final void accept(Object obj) {
        ((MiscRenderItem) obj).getBasicTexture().recycle();
    }
}
