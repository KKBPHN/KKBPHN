package com.android.camera.dualvideo;

import com.android.camera2.Camera2Proxy;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000OOo implements Consumer {
    private final /* synthetic */ DualVideoModuleBase O0OOoO0;

    public /* synthetic */ O000OOo(DualVideoModuleBase dualVideoModuleBase) {
        this.O0OOoO0 = dualVideoModuleBase;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.applyZoomForDevices((Camera2Proxy) obj);
    }
}
