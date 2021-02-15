package com.android.camera.dualvideo.view;

import com.android.camera.dualvideo.render.CameraItemInterface;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.view.O00000oo reason: case insensitive filesystem */
public final /* synthetic */ class C0246O00000oo implements Predicate {
    private final /* synthetic */ TouchHelper O0OOoO0;
    private final /* synthetic */ float O0OOoOO;
    private final /* synthetic */ float O0OOoOo;

    public /* synthetic */ C0246O00000oo(TouchHelper touchHelper, float f, float f2) {
        this.O0OOoO0 = touchHelper;
        this.O0OOoOO = f;
        this.O0OOoOo = f2;
    }

    public final boolean test(Object obj) {
        return this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, (CameraItemInterface) obj);
    }
}
