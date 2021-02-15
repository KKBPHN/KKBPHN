package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000OOo implements Consumer {
    private final /* synthetic */ CameraItemInterface O0OOoO0;

    public /* synthetic */ O0000OOo(CameraItemInterface cameraItemInterface) {
        this.O0OOoO0 = cameraItemInterface;
    }

    public final void accept(Object obj) {
        ((UserSelectData) obj).setSelectWindowLayoutType(this.O0OOoO0.getSelectWindowLayoutType());
    }
}
