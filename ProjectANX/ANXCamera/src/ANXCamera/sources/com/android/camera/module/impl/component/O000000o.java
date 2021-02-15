package com.android.camera.module.impl.component;

import com.android.camera.module.BaseModule;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Consumer {
    public static final /* synthetic */ O000000o INSTANCE = new O000000o();

    private /* synthetic */ O000000o() {
    }

    public final void accept(Object obj) {
        ((BaseModule) obj).resetAiSceneInDocumentModeOn();
    }
}
