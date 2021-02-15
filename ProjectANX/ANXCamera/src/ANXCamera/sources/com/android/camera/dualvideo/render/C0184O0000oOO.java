package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.SelectIndex;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O0000oOO reason: case insensitive filesystem */
public final /* synthetic */ class C0184O0000oOO implements Consumer {
    public static final /* synthetic */ C0184O0000oOO INSTANCE = new C0184O0000oOO();

    private /* synthetic */ C0184O0000oOO() {
    }

    public final void accept(Object obj) {
        ((CameraItemInterface) obj).setSelectTypeWithAnim(SelectIndex.INDEX_1, true);
    }
}
