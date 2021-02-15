package com.android.camera.dualvideo;

import android.graphics.Point;
import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.module.loader.camera2.FocusManager2;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O000O0Oo reason: case insensitive filesystem */
public final /* synthetic */ class C0152O000O0Oo implements Consumer {
    private final /* synthetic */ DualVideoModuleBase O0OOoO0;
    private final /* synthetic */ LayoutType O0OOoOO;
    private final /* synthetic */ Point O0OOoOo;
    private final /* synthetic */ boolean O0OOoo0;

    public /* synthetic */ C0152O000O0Oo(DualVideoModuleBase dualVideoModuleBase, LayoutType layoutType, Point point, boolean z) {
        this.O0OOoO0 = dualVideoModuleBase;
        this.O0OOoOO = layoutType;
        this.O0OOoOo = point;
        this.O0OOoo0 = z;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, this.O0OOoo0, (FocusManager2) obj);
    }
}
