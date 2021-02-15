package com.android.camera.dualvideo.render;

import android.media.ImageReader;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oo reason: case insensitive filesystem */
public final /* synthetic */ class C0228O000oo implements Consumer {
    public static final /* synthetic */ C0228O000oo INSTANCE = new C0228O000oo();

    private /* synthetic */ C0228O000oo() {
    }

    public final void accept(Object obj) {
        ((ImageReader) obj).close();
    }
}
