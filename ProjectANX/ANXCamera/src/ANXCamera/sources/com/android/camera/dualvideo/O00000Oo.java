package com.android.camera.dualvideo;

import com.android.camera2.Camera2Proxy;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements Consumer {
    public static final /* synthetic */ O00000Oo INSTANCE = new O00000Oo();

    private /* synthetic */ O00000Oo() {
    }

    public final void accept(Object obj) {
        ((Camera2Proxy) obj).resumePreview();
    }
}
