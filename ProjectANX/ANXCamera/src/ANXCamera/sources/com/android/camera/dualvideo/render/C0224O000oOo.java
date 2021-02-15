package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oOo reason: case insensitive filesystem */
public final /* synthetic */ class C0224O000oOo implements Predicate {
    private final /* synthetic */ LayoutType O0OOoO0;

    public /* synthetic */ C0224O000oOo(LayoutType layoutType) {
        this.O0OOoO0 = layoutType;
    }

    public final boolean test(Object obj) {
        return RenderUtil.O00000Oo(this.O0OOoO0, (UserSelectData) obj);
    }
}
