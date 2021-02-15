package com.android.camera.dualvideo.view;

import com.android.camera.protocol.ModeProtocol.DualVideoRenderProtocol;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class O0000O0o implements Function {
    private final /* synthetic */ float O0OOoO0;
    private final /* synthetic */ float O0OOoOO;

    public /* synthetic */ O0000O0o(float f, float f2) {
        this.O0OOoO0 = f;
        this.O0OOoOO = f2;
    }

    public final Object apply(Object obj) {
        return ((DualVideoRenderProtocol) obj).getRenderManager().getRenderComposeTypeByPosition(this.O0OOoO0, this.O0OOoOO);
    }
}
