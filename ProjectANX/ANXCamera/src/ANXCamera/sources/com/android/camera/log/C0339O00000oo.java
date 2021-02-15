package com.android.camera.log;

import com.miui.internal.log.Level;

/* compiled from: lambda */
/* renamed from: com.android.camera.log.O00000oo reason: case insensitive filesystem */
public final /* synthetic */ class C0339O00000oo implements Runnable {
    private final /* synthetic */ String O0OOoO0;
    private final /* synthetic */ String O0OOoOO;

    public /* synthetic */ C0339O00000oo(String str, String str2) {
        this.O0OOoO0 = str;
        this.O0OOoOO = str2;
    }

    public final void run() {
        FileLogger.mLogger.log(Level.ERROR, this.O0OOoO0, this.O0OOoOO);
    }
}
