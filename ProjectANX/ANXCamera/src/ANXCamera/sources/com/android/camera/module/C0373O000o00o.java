package com.android.camera.module;

import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O000o00o reason: case insensitive filesystem */
public final /* synthetic */ class C0373O000o00o implements Consumer {
    private final /* synthetic */ Camera2Module O0OOoO0;

    public /* synthetic */ C0373O000o00o(Camera2Module camera2Module) {
        this.O0OOoO0 = camera2Module;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.resetToIdleNoKeep(((Boolean) obj).booleanValue());
    }
}
