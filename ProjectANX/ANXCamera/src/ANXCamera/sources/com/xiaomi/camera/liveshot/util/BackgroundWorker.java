package com.xiaomi.camera.liveshot.util;

import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundWorker {
    private static final int SHUTDOWN_TIMEOUT_MILLISECONDS = 2000;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    /* access modifiers changed from: private */
    public Looper mLooper;
    /* access modifiers changed from: private */
    public volatile boolean mReady = false;

    public BackgroundWorker(final String str) {
        this.mExecutor.execute(new Runnable() {
            public void run() {
                Thread.currentThread().setName(str);
                Looper.prepare();
                BackgroundWorker.this.mLooper = Looper.myLooper();
                synchronized (BackgroundWorker.this.mLock) {
                    BackgroundWorker.this.mReady = true;
                    BackgroundWorker.this.mLock.notifyAll();
                }
                Looper.loop();
                synchronized (BackgroundWorker.this.mLock) {
                    BackgroundWorker.this.mReady = false;
                }
            }
        });
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0003 */
    /* JADX WARNING: Removed duplicated region for block: B:2:0x0003 A[LOOP:0: B:2:0x0003->B:14:0x0003, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void waitUntilReady() {
        synchronized (this.mLock) {
            while (!this.mReady) {
                this.mLock.wait();
            }
        }
    }

    public Looper getLooper() {
        waitUntilReady();
        return this.mLooper;
    }

    public void quit() {
        waitUntilReady();
        this.mLooper.quitSafely();
        this.mExecutor.shutdown();
    }
}
