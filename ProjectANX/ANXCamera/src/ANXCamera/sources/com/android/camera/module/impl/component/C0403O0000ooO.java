package com.android.camera.module.impl.component;

import com.android.camera.module.BaseModule;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.impl.component.O0000ooO reason: case insensitive filesystem */
public final /* synthetic */ class C0403O0000ooO implements Consumer {
    private final /* synthetic */ ConfigChangeImpl O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;

    public /* synthetic */ C0403O0000ooO(ConfigChangeImpl configChangeImpl, boolean z) {
        this.O0OOoO0 = configChangeImpl;
        this.O0OOoOO = z;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, (BaseModule) obj);
    }
}
