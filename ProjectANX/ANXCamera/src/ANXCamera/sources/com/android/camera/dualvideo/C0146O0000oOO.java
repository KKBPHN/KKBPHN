package com.android.camera.dualvideo;

import android.util.Range;
import com.android.camera2.Camera2Proxy;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.O0000oOO reason: case insensitive filesystem */
public final /* synthetic */ class C0146O0000oOO implements Consumer {
    private final /* synthetic */ Range O0OOoO0;

    public /* synthetic */ C0146O0000oOO(Range range) {
        this.O0OOoO0 = range;
    }

    public final void accept(Object obj) {
        ((Camera2Proxy) obj).setFpsRange(this.O0OOoO0);
    }
}
