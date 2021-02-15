package com.android.camera.data.data.runing;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.data.data.runing.O0000o0O reason: case insensitive filesystem */
public final /* synthetic */ class C0133O0000o0O implements Predicate {
    private final /* synthetic */ ComponentRunningDualVideo O0OOoO0;

    public /* synthetic */ C0133O0000o0O(ComponentRunningDualVideo componentRunningDualVideo) {
        this.O0OOoO0 = componentRunningDualVideo;
    }

    public final boolean test(Object obj) {
        return this.O0OOoO0.O00000o0((ConfigItem) obj);
    }
}
