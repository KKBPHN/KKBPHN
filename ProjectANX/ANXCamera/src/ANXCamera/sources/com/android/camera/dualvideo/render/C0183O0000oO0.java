package com.android.camera.dualvideo.render;

import java.util.ArrayList;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O0000oO0 reason: case insensitive filesystem */
public final /* synthetic */ class C0183O0000oO0 implements Consumer {
    private final /* synthetic */ ArrayList O0OOoO0;

    public /* synthetic */ C0183O0000oO0(ArrayList arrayList) {
        this.O0OOoO0 = arrayList;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.stream().filter(new O0000o((CameraItemInterface) obj)).findFirst().ifPresent(new O0000OOo((CameraItemInterface) obj));
    }
}
