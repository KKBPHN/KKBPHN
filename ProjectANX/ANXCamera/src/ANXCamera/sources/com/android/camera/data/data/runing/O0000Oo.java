package com.android.camera.data.data.runing;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo implements Predicate {
    private final /* synthetic */ ConfigItem O0OOoO0;

    public /* synthetic */ O0000Oo(ConfigItem configItem) {
        this.O0OOoO0 = configItem;
    }

    public final boolean test(Object obj) {
        return ComponentRunningDualVideo.O000000o(this.O0OOoO0, (UserSelectData) obj);
    }
}
