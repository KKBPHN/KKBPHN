package com.android.camera.dualvideo.util;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements Function {
    public static final /* synthetic */ O00000o INSTANCE = new O00000o();

    private /* synthetic */ O00000o() {
    }

    public final Object apply(Object obj) {
        return Integer.valueOf(((ConfigItem) obj).mCameraId);
    }
}
