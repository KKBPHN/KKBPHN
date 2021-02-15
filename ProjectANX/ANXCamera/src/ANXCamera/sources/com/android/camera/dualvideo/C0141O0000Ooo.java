package com.android.camera.dualvideo;

import com.android.camera2.Camera2Proxy;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000Ooo reason: case insensitive filesystem */
public final /* synthetic */ class C0141O0000Ooo implements Consumer {
    public static final /* synthetic */ C0141O0000Ooo INSTANCE = new C0141O0000Ooo();

    private /* synthetic */ C0141O0000Ooo() {
    }

    public final void accept(Object obj) {
        ((Camera2Proxy) obj).lockExposure(true);
    }
}
