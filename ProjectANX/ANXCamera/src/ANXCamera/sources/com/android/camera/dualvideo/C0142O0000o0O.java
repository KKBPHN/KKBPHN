package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import com.android.camera.dualvideo.render.RenderUtil;
import com.android.camera.dualvideo.util.RenderSourceType;
import java.util.function.Function;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000o0O reason: case insensitive filesystem */
public final /* synthetic */ class C0142O0000o0O implements Function {
    public static final /* synthetic */ C0142O0000o0O INSTANCE = new C0142O0000o0O();

    private /* synthetic */ C0142O0000o0O() {
    }

    public final Object apply(Object obj) {
        return ((RenderManager) obj).genOrUpdateRenderSource(RenderSourceType.SUB.getIndex(), RenderUtil.getSubPreviewSize());
    }
}
