package com.android.camera.module.impl.component;

import com.android.camera.module.BaseModule;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.impl.component.O0000OoO reason: case insensitive filesystem */
public final /* synthetic */ class C0393O0000OoO implements Consumer {
    public static final /* synthetic */ C0393O0000OoO INSTANCE = new C0393O0000OoO();

    private /* synthetic */ C0393O0000OoO() {
    }

    public final void accept(Object obj) {
        ((BaseModule) obj).updatePreferenceInWorkThread(45);
    }
}
