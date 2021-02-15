package com.android.camera.dualvideo.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O0000O0o implements Consumer {
    private final /* synthetic */ ConcurrentHashMap O0OOoO0;

    public /* synthetic */ O0000O0o(ConcurrentHashMap concurrentHashMap) {
        this.O0OOoO0 = concurrentHashMap;
    }

    public final void accept(Object obj) {
        DualVideoConfigManager.O00000Oo(this.O0OOoO0, (Integer) obj);
    }
}
