package com.android.camera.dualvideo.view;

import android.graphics.Rect;
import com.android.camera.dualvideo.render.CameraItemInterface;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000o00 implements Consumer {
    private final /* synthetic */ Rect O0OOoO0;

    public /* synthetic */ O0000o00(Rect rect) {
        this.O0OOoO0 = rect;
    }

    public final void accept(Object obj) {
        TouchHelper.O000000o(this.O0OOoO0, (CameraItemInterface) obj);
    }
}
