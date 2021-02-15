package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000OO0o implements Consumer {
    private final /* synthetic */ CameraItem O0OOoO0;

    public /* synthetic */ O000OO0o(CameraItem cameraItem) {
        this.O0OOoO0 = cameraItem;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.setSelectTypeWithAnim(((UserSelectData) obj).getSelectIndex(), false);
    }
}
