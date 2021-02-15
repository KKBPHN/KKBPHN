package com.android.camera.module.impl.component;

import com.android.camera.module.BaseModule;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000OOo implements Consumer {
    public static final /* synthetic */ O0000OOo INSTANCE = new O0000OOo();

    private /* synthetic */ O0000OOo() {
    }

    public final void accept(Object obj) {
        ((BaseModule) obj).onSharedPreferenceChanged();
    }
}
