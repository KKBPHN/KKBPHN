package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.DualVideoConfigManager;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O000OO00 implements Predicate {
    public static final /* synthetic */ O000OO00 INSTANCE = new O000OO00();

    private /* synthetic */ O000OO00() {
    }

    public final boolean test(Object obj) {
        return DualVideoConfigManager.instance().getConfigs().stream().noneMatch(new C0181O0000o0o((CameraItemInterface) obj));
    }
}
