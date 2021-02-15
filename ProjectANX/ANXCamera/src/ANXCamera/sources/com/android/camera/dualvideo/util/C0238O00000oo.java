package com.android.camera.dualvideo.util;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.util.O00000oo reason: case insensitive filesystem */
public final /* synthetic */ class C0238O00000oo implements Predicate {
    private final /* synthetic */ int O0OOoO0;

    public /* synthetic */ C0238O00000oo(int i) {
        this.O0OOoO0 = i;
    }

    public final boolean test(Object obj) {
        return DualVideoConfigManager.O00000Oo(this.O0OOoO0, (ConfigItem) obj);
    }
}
