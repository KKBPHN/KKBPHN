package com.android.camera.data.data.runing;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo0 implements Consumer {
    private final /* synthetic */ UserSelectData O0OOoO0;

    public /* synthetic */ O0000Oo0(UserSelectData userSelectData) {
        this.O0OOoO0 = userSelectData;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.setSelectWindowLayoutType(((ConfigItem) obj).mLayoutType);
    }
}
