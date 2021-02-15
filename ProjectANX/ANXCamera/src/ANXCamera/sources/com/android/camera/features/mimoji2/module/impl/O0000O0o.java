package com.android.camera.features.mimoji2.module.impl;

import java.nio.ByteBuffer;

/* compiled from: lambda */
public final /* synthetic */ class O0000O0o implements Runnable {
    private final /* synthetic */ MimojiAvatarEngine2Impl O0OOoO0;
    private final /* synthetic */ ByteBuffer O0OOoOO;
    private final /* synthetic */ int O0OOoOo;
    private final /* synthetic */ int O0OOoo0;

    public /* synthetic */ O0000O0o(MimojiAvatarEngine2Impl mimojiAvatarEngine2Impl, ByteBuffer byteBuffer, int i, int i2) {
        this.O0OOoO0 = mimojiAvatarEngine2Impl;
        this.O0OOoOO = byteBuffer;
        this.O0OOoOo = i;
        this.O0OOoo0 = i2;
    }

    public final void run() {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, this.O0OOoo0);
    }
}
