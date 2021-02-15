package com.android.camera.dualvideo.util;

import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O0000OOo implements Predicate {
    private final /* synthetic */ LayoutType O0OOoO0;

    public /* synthetic */ O0000OOo(LayoutType layoutType) {
        this.O0OOoO0 = layoutType;
    }

    public final boolean test(Object obj) {
        return DualVideoConfigManager.O00000Oo(this.O0OOoO0, (ConfigItem) obj);
    }
}
