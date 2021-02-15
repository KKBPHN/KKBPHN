package com.miui.internal.util.async;

import android.os.HandlerThread;
import android.os.Looper;
import java.util.Map;
import miui.util.ArrayMap;

public class WorkerThreads {
    public static final String TAG_COMMON_WORK = "common_work";
    private static final Map sWorkers = new ArrayMap();

    class ThreadWrapper {
        int refCount = 1;
        final HandlerThread thread;

        ThreadWrapper(String str) {
            this.thread = new HandlerThread(str);
            this.thread.start();
        }
    }

    private WorkerThreads() {
    }

    public static synchronized Looper aquireWorker(String str) {
        Looper looper;
        synchronized (WorkerThreads.class) {
            ThreadWrapper threadWrapper = (ThreadWrapper) sWorkers.get(str);
            if (threadWrapper == null) {
                threadWrapper = new ThreadWrapper(str);
                sWorkers.put(str, threadWrapper);
            } else {
                threadWrapper.refCount++;
            }
            looper = threadWrapper.thread.getLooper();
        }
        return looper;
    }

    public static synchronized void releaseWorker(String str) {
        synchronized (WorkerThreads.class) {
            ThreadWrapper threadWrapper = (ThreadWrapper) sWorkers.get(str);
            if (threadWrapper != null) {
                threadWrapper.refCount--;
                if (threadWrapper.refCount == 0) {
                    sWorkers.remove(str);
                    threadWrapper.thread.quitSafely();
                }
            }
        }
    }
}
