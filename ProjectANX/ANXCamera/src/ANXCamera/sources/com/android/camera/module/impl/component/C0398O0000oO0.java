package com.android.camera.module.impl.component;

import com.android.camera.module.BaseModule;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.impl.component.O0000oO0 reason: case insensitive filesystem */
public final /* synthetic */ class C0398O0000oO0 implements Consumer {
    private final /* synthetic */ int[] O0OOoO0;

    public /* synthetic */ C0398O0000oO0(int[] iArr) {
        this.O0OOoO0 = iArr;
    }

    public final void accept(Object obj) {
        ((BaseModule) obj).updatePreferenceInWorkThread(this.O0OOoO0);
    }
}
