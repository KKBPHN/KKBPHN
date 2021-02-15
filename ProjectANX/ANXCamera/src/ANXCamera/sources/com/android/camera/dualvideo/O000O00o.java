package com.android.camera.dualvideo;

import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera2.Camera2Proxy;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000O00o implements Consumer {
    private final /* synthetic */ Camera2Proxy O0OOoO0;

    public /* synthetic */ O000O00o(Camera2Proxy camera2Proxy) {
        this.O0OOoO0 = camera2Proxy;
    }

    public final void accept(Object obj) {
        DualVideoConfigManager.instance().getConfigs().stream().filter(new C0154O000O0oo((UserSelectData) obj)).forEach(new C0170O00oOooO(this.O0OOoO0));
    }
}
