package com.android.camera.dualvideo;

import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class O000o000 implements Function {
    public static final /* synthetic */ O000o000 INSTANCE = new O000o000();

    private /* synthetic */ O000o000() {
    }

    public final Object apply(Object obj) {
        return Boolean.valueOf(((RenderManager) obj).isRecording());
    }
}
