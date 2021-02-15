package com.android.camera.dualvideo.util;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements Predicate {
    private final /* synthetic */ int O0OOoO0;

    public /* synthetic */ O00000Oo(int i) {
        this.O0OOoO0 = i;
    }

    public final boolean test(Object obj) {
        return DualVideoConfigManager.O000000o(this.O0OOoO0, (ConfigItem) obj);
    }
}
