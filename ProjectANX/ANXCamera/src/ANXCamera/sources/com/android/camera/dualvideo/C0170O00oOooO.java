package com.android.camera.dualvideo;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera2.Camera2Proxy;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O00oOooO reason: case insensitive filesystem */
public final /* synthetic */ class C0170O00oOooO implements Consumer {
    private final /* synthetic */ Camera2Proxy O0OOoO0;

    public /* synthetic */ C0170O00oOooO(Camera2Proxy camera2Proxy) {
        this.O0OOoO0 = camera2Proxy;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.setZoomRatio(((ConfigItem) obj).mPresentZoom / ((ConfigItem) obj).mRelativeZoom);
    }
}
