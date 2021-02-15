package com.android.camera.dualvideo;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000O0oo reason: case insensitive filesystem */
public final /* synthetic */ class C0154O000O0oo implements Predicate {
    private final /* synthetic */ UserSelectData O0OOoO0;

    public /* synthetic */ C0154O000O0oo(UserSelectData userSelectData) {
        this.O0OOoO0 = userSelectData;
    }

    public final boolean test(Object obj) {
        return DualVideoModuleBase.O00000o0(this.O0OOoO0, (ConfigItem) obj);
    }
}
