package com.android.camera.data.data.runing;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O0000o00 implements Predicate {
    private final /* synthetic */ UserSelectData O0OOoO0;

    public /* synthetic */ O0000o00(UserSelectData userSelectData) {
        this.O0OOoO0 = userSelectData;
    }

    public final boolean test(Object obj) {
        return ComponentRunningDualVideo.O000000o(this.O0OOoO0, (ConfigItem) obj);
    }
}
