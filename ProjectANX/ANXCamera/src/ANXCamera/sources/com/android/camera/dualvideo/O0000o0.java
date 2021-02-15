package com.android.camera.dualvideo;

import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera2.Camera2Proxy;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O0000o0 implements Predicate {
    private final /* synthetic */ Camera2Proxy O0OOoO0;

    public /* synthetic */ O0000o0(Camera2Proxy camera2Proxy) {
        this.O0OOoO0 = camera2Proxy;
    }

    public final boolean test(Object obj) {
        return DualVideoModuleBase.O000000o(this.O0OOoO0, (UserSelectData) obj);
    }
}
