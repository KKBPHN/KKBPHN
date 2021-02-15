package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000o00o reason: case insensitive filesystem */
public final /* synthetic */ class C0169O000o00o implements Consumer {
    public static final /* synthetic */ C0169O000o00o INSTANCE = new C0169O000o00o();

    private /* synthetic */ C0169O000o00o() {
    }

    public final void accept(Object obj) {
        ((RenderManager) obj).stopRecording();
    }
}
