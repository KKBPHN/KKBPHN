package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000OO0o implements Consumer {
    public static final /* synthetic */ O000OO0o INSTANCE = new O000OO0o();

    private /* synthetic */ O000OO0o() {
    }

    public final void accept(Object obj) {
        ((RenderManager) obj).enableContinuousRender(true);
    }
}
