package com.android.camera.module.impl.component;

import com.android.camera.module.BaseModule;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements Consumer {
    public static final /* synthetic */ O00000o INSTANCE = new O00000o();

    private /* synthetic */ O00000o() {
    }

    public final void accept(Object obj) {
        ((BaseModule) obj).updatePreferenceInWorkThread(10);
    }
}
