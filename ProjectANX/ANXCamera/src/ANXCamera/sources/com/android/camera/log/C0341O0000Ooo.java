package com.android.camera.log;

import com.miui.internal.log.Level;

/* compiled from: lambda */
/* renamed from: com.android.camera.log.O0000Ooo reason: case insensitive filesystem */
public final /* synthetic */ class C0341O0000Ooo implements Runnable {
    private final /* synthetic */ String O0OOoO0;
    private final /* synthetic */ String O0OOoOO;
    private final /* synthetic */ Throwable O0OOoOo;

    public /* synthetic */ C0341O0000Ooo(String str, String str2, Throwable th) {
        this.O0OOoO0 = str;
        this.O0OOoOO = str2;
        this.O0OOoOo = th;
    }

    public final void run() {
        FileLogger.mLogger.log(Level.INFO, this.O0OOoO0, this.O0OOoOO, this.O0OOoOo);
    }
}
