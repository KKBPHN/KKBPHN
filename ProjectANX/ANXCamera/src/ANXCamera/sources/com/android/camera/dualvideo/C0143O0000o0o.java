package com.android.camera.dualvideo;

import com.android.camera2.Camera2Proxy;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000o0o reason: case insensitive filesystem */
public final /* synthetic */ class C0143O0000o0o implements Consumer {
    private final /* synthetic */ int[] O0OOoO0;

    public /* synthetic */ C0143O0000o0o(int[] iArr) {
        this.O0OOoO0 = iArr;
    }

    public final void accept(Object obj) {
        ((Camera2Proxy) obj).setMtkPipDevices(this.O0OOoO0);
    }
}
