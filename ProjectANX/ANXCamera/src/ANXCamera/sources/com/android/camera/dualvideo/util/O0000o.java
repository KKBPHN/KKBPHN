package com.android.camera.dualvideo.util;

import com.android.camera.dualvideo.render.LayoutType;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O0000o implements Predicate {
    private final /* synthetic */ int O0OOoO0;

    public /* synthetic */ O0000o(int i) {
        this.O0OOoO0 = i;
    }

    public final boolean test(Object obj) {
        return DualVideoConfigManager.O000000o(this.O0OOoO0, (LayoutType) obj);
    }
}
