package com.android.camera.dualvideo.view;

import android.graphics.Rect;
import com.android.camera.dualvideo.render.CameraItemInterface;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo implements Consumer {
    private final /* synthetic */ TouchHelper O0OOoO0;
    private final /* synthetic */ Rect O0OOoOO;

    public /* synthetic */ O0000Oo(TouchHelper touchHelper, Rect rect) {
        this.O0OOoO0 = touchHelper;
        this.O0OOoOO = rect;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O00000Oo(this.O0OOoOO, (CameraItemInterface) obj);
    }
}
