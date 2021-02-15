package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Function;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oOo0 reason: case insensitive filesystem */
public final /* synthetic */ class C0225O000oOo0 implements Function {
    private final /* synthetic */ float O0OOoO0;

    public /* synthetic */ C0225O000oOo0(float f) {
        this.O0OOoO0 = f;
    }

    public final Object apply(Object obj) {
        return RenderUtil.O000000o(this.O0OOoO0, (ConfigItem) obj);
    }
}
