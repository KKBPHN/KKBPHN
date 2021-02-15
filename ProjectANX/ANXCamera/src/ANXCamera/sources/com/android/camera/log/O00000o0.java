package com.android.camera.log;

import com.miui.internal.log.Level;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements Runnable {
    private final /* synthetic */ String O0OOoO0;
    private final /* synthetic */ Throwable O0OOoOO;

    public /* synthetic */ O00000o0(String str, Throwable th) {
        this.O0OOoO0 = str;
        this.O0OOoOO = th;
    }

    public final void run() {
        FileLogger.mLogger.log(Level.WARNING, this.O0OOoO0, "", this.O0OOoOO);
    }
}
