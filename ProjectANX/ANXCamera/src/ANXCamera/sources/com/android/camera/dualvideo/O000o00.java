package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000o00 implements Consumer {
    public static final /* synthetic */ O000o00 INSTANCE = new O000o00();

    private /* synthetic */ O000o00() {
    }

    public final void accept(Object obj) {
        ((RenderManager) obj).switch6patch2preview();
    }
}
