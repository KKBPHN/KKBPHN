package com.android.camera.log;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements RejectedExecutionHandler {
    public static final /* synthetic */ O00000o INSTANCE = new O00000o();

    private /* synthetic */ O00000o() {
    }

    public final void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        FileLogger.O000000o(runnable, threadPoolExecutor);
    }
}
