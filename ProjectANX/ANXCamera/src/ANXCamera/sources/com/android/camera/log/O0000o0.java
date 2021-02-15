package com.android.camera.log;

import com.miui.internal.log.Level;

/* compiled from: lambda */
public final /* synthetic */ class O0000o0 implements Runnable {
    private final /* synthetic */ String O0OOoO0;
    private final /* synthetic */ String O0OOoOO;
    private final /* synthetic */ Throwable O0OOoOo;

    public /* synthetic */ O0000o0(String str, String str2, Throwable th) {
        this.O0OOoO0 = str;
        this.O0OOoOO = str2;
        this.O0OOoOo = th;
    }

    public final void run() {
        FileLogger.mLogger.log(Level.WARNING, this.O0OOoO0, this.O0OOoOO, this.O0OOoOo);
    }
}
