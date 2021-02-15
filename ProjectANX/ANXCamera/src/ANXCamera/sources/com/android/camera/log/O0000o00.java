package com.android.camera.log;

import java.util.concurrent.ThreadFactory;

/* compiled from: lambda */
public final /* synthetic */ class O0000o00 implements ThreadFactory {
    public static final /* synthetic */ O0000o00 INSTANCE = new O0000o00();

    private /* synthetic */ O0000o00() {
    }

    public final Thread newThread(Runnable runnable) {
        return FileLogger.O000000o(runnable);
    }
}
