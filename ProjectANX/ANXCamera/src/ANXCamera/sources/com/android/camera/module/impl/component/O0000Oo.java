package com.android.camera.module.impl.component;

import com.android.camera.dualvideo.DualVideoModuleBase;
import com.android.camera.module.BaseModule;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo implements Consumer {
    public static final /* synthetic */ O0000Oo INSTANCE = new O0000Oo();

    private /* synthetic */ O0000Oo() {
    }

    public final void accept(Object obj) {
        ((DualVideoModuleBase) ((BaseModule) obj)).switchRemoteCamera(true);
    }
}
