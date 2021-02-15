package com.android.camera.module.impl.component;

import com.android.camera.dualvideo.DualVideoRecordModule;
import com.android.camera.module.BaseModule;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.impl.component.O0000Ooo reason: case insensitive filesystem */
public final /* synthetic */ class C0394O0000Ooo implements Consumer {
    public static final /* synthetic */ C0394O0000Ooo INSTANCE = new C0394O0000Ooo();

    private /* synthetic */ C0394O0000Ooo() {
    }

    public final void accept(Object obj) {
        ((DualVideoRecordModule) ((BaseModule) obj)).reselectCamera();
    }
}
