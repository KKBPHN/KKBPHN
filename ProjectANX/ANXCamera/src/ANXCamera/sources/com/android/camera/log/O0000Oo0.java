package com.android.camera.log;

import com.miui.internal.log.Level;

/* compiled from: lambda */
public final /* synthetic */ class O0000Oo0 implements Runnable {
    private final /* synthetic */ String O0OOoO0;
    private final /* synthetic */ String O0OOoOO;

    public /* synthetic */ O0000Oo0(String str, String str2) {
        this.O0OOoO0 = str;
        this.O0OOoOO = str2;
    }

    public final void run() {
        FileLogger.mLogger.log(Level.WARNING, this.O0OOoO0, this.O0OOoOO);
    }
}
