package com.android.camera.dualvideo;

import com.android.camera2.Camera2Proxy;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000o00 implements Consumer {
    public static final /* synthetic */ O0000o00 INSTANCE = new O0000o00();

    private /* synthetic */ O0000o00() {
    }

    public final void accept(Object obj) {
        ((Camera2Proxy) obj).setAWBLock(false);
    }
}
