package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O0000o implements Predicate {
    private final /* synthetic */ CameraItemInterface O0OOoO0;

    public /* synthetic */ O0000o(CameraItemInterface cameraItemInterface) {
        this.O0OOoO0 = cameraItemInterface;
    }

    public final boolean test(Object obj) {
        return CameraItemManager.O000000o(this.O0OOoO0, (UserSelectData) obj);
    }
}
