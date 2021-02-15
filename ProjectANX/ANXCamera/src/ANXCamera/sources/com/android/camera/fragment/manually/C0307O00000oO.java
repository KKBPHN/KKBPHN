package com.android.camera.fragment.manually;

import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.config.ComponentManuallyWB;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.fragment.manually.O00000oO reason: case insensitive filesystem */
public final /* synthetic */ class C0307O00000oO implements Consumer {
    private final /* synthetic */ ComponentData O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;

    public /* synthetic */ C0307O00000oO(ComponentData componentData, boolean z) {
        this.O0OOoO0 = componentData;
        this.O0OOoOO = z;
    }

    public final void accept(Object obj) {
        ((FragmentManuallyExtra) obj).showCustomWB((ComponentManuallyWB) this.O0OOoO0, this.O0OOoOO);
    }
}
