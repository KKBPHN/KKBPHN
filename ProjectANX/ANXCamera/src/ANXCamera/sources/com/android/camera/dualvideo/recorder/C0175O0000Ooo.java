package com.android.camera.dualvideo.recorder;

import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.recorder.O0000Ooo reason: case insensitive filesystem */
public final /* synthetic */ class C0175O0000Ooo implements Predicate {
    public static final /* synthetic */ C0175O0000Ooo INSTANCE = new C0175O0000Ooo();

    private /* synthetic */ C0175O0000Ooo() {
    }

    public final boolean test(Object obj) {
        return ((MiRecorder) obj).isPaused();
    }
}
