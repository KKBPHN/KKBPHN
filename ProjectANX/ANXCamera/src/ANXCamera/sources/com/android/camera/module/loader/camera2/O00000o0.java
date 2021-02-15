package com.android.camera.module.loader.camera2;

import io.reactivex.functions.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements Consumer {
    private final /* synthetic */ Camera2OpenManager O0OOoO0;

    public /* synthetic */ O00000o0(Camera2OpenManager camera2OpenManager) {
        this.O0OOoO0 = camera2OpenManager;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.fire((Camera2Result) obj);
    }
}
