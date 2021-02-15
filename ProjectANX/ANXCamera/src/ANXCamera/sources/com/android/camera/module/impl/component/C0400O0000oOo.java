package com.android.camera.module.impl.component;

import com.android.camera.dualvideo.DualVideoSelectModule;
import com.android.camera.module.BaseModule;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.impl.component.O0000oOo reason: case insensitive filesystem */
public final /* synthetic */ class C0400O0000oOo implements Consumer {
    public static final /* synthetic */ C0400O0000oOo INSTANCE = new C0400O0000oOo();

    private /* synthetic */ C0400O0000oOo() {
    }

    public final void accept(Object obj) {
        ((DualVideoSelectModule) ((BaseModule) obj)).addUserGuide();
    }
}
