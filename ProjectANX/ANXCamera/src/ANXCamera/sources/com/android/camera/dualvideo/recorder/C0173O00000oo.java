package com.android.camera.dualvideo.recorder;

import android.util.SparseArray;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.recorder.O00000oo reason: case insensitive filesystem */
public final /* synthetic */ class C0173O00000oo implements Consumer {
    private final /* synthetic */ SparseArray O0OOoO0;

    public /* synthetic */ C0173O00000oo(SparseArray sparseArray) {
        this.O0OOoO0 = sparseArray;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.put(((MiRecorder) obj).getId(), ((MiRecorder) obj).getSurface());
    }
}
