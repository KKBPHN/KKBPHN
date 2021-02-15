package com.android.camera.module;

import android.net.Uri;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O000o00O reason: case insensitive filesystem */
public final /* synthetic */ class C0372O000o00O implements Runnable {
    private final /* synthetic */ MiLiveModule O0OOoO0;
    private final /* synthetic */ String O0OOoOO;
    private final /* synthetic */ Uri O0OOoOo;

    public /* synthetic */ C0372O000o00O(MiLiveModule miLiveModule, String str, Uri uri) {
        this.O0OOoO0 = miLiveModule;
        this.O0OOoOO = str;
        this.O0OOoOo = uri;
    }

    public final void run() {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo);
    }
}
