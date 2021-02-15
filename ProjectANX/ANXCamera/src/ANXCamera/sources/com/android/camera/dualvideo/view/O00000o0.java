package com.android.camera.dualvideo.view;

import com.android.camera.protocol.ModeProtocol.DualVideoRenderProtocol;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements Function {
    public static final /* synthetic */ O00000o0 INSTANCE = new O00000o0();

    private /* synthetic */ O00000o0() {
    }

    public final Object apply(Object obj) {
        return ((DualVideoRenderProtocol) obj).getRenderManager();
    }
}
