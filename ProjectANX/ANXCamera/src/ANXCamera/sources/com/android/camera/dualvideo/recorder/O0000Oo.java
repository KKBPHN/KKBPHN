package com.android.camera.dualvideo.recorder;

import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo implements Consumer {
    public static final /* synthetic */ O0000Oo INSTANCE = new O0000Oo();

    private /* synthetic */ O0000Oo() {
    }

    public final void accept(Object obj) {
        ((MiRecorder) obj).release();
    }
}
