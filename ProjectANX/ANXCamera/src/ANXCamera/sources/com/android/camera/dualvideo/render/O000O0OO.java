package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O000O0OO implements Predicate {
    private final /* synthetic */ CameraItemInterface O0OOoO0;

    public /* synthetic */ O000O0OO(CameraItemInterface cameraItemInterface) {
        this.O0OOoO0 = cameraItemInterface;
    }

    public final boolean test(Object obj) {
        return CameraItemManager.O00000o0(this.O0OOoO0, (UserSelectData) obj);
    }
}
