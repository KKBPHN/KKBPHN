package com.android.camera.dualvideo.view;

import com.android.camera.dualvideo.render.CameraItemInterface;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo0 implements Predicate {
    private final /* synthetic */ int O0OOoO0;

    public /* synthetic */ O0000Oo0(int i) {
        this.O0OOoO0 = i;
    }

    public final boolean test(Object obj) {
        return TouchHelper.O00000Oo(this.O0OOoO0, (CameraItemInterface) obj);
    }
}
