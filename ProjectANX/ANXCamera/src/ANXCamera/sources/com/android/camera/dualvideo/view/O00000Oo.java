package com.android.camera.dualvideo.view;

import com.android.camera.dualvideo.render.CameraItemManager;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements Function {
    public static final /* synthetic */ O00000Oo INSTANCE = new O00000Oo();

    private /* synthetic */ O00000Oo() {
    }

    public final Object apply(Object obj) {
        return ((CameraItemManager) obj).getVisibleRenderList();
    }
}
