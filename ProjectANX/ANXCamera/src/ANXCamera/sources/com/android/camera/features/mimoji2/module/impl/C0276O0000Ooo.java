package com.android.camera.features.mimoji2.module.impl;

import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiBottomList;

/* compiled from: lambda */
/* renamed from: com.android.camera.features.mimoji2.module.impl.O0000Ooo reason: case insensitive filesystem */
public final /* synthetic */ class C0276O0000Ooo implements Runnable {
    private final /* synthetic */ MimojiBottomList O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;

    public /* synthetic */ C0276O0000Ooo(MimojiBottomList mimojiBottomList, boolean z) {
        this.O0OOoO0 = mimojiBottomList;
        this.O0OOoOO = z;
    }

    public final void run() {
        MimojiAvatarEngine2Impl.O000000o(this.O0OOoO0, this.O0OOoOO);
    }
}
