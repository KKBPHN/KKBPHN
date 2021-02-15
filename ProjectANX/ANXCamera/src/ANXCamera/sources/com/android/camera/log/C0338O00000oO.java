package com.android.camera.log;

import com.miui.internal.log.Level;

/* compiled from: lambda */
/* renamed from: com.android.camera.log.O00000oO reason: case insensitive filesystem */
public final /* synthetic */ class C0338O00000oO implements Runnable {
    private final /* synthetic */ String O0OOoO0;
    private final /* synthetic */ String O0OOoOO;

    public /* synthetic */ C0338O00000oO(String str, String str2) {
        this.O0OOoO0 = str;
        this.O0OOoOO = str2;
    }

    public final void run() {
        FileLogger.mLogger.log(Level.INFO, this.O0OOoO0, this.O0OOoOO);
    }
}
