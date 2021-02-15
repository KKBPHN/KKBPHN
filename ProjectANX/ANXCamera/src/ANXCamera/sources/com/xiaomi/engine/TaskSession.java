package com.xiaomi.engine;

import android.media.Image;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.List;

public class TaskSession {
    private static final boolean DEBUG = false;
    private static final String TAG = "TaskSession";
    private boolean mHasDestroyed;
    private boolean mHasFlushed;
    private final long mSessionHandle;

    @FunctionalInterface
    public interface FrameCallback {
        void onFrameProcessed(int i, String str, Object obj);
    }

    @FunctionalInterface
    public interface SessionStatusCallback {
        void onSessionCallback(int i, String str, Object obj);
    }

    TaskSession(long j) {
        this.mSessionHandle = j;
    }

    private void destroy() {
        if (!this.mHasDestroyed) {
            int destroySession = MiCamAlgoInterfaceJNI.destroySession(this.mSessionHandle);
            Util.assertOrNot(destroySession);
            if (destroySession == 0) {
                this.mHasDestroyed = true;
            }
        }
    }

    private void flush() {
        if (!this.mHasFlushed) {
            int flush = MiCamAlgoInterfaceJNI.flush(this.mSessionHandle);
            Util.assertOrNot(flush);
            if (flush == 0) {
                this.mHasFlushed = true;
            }
        }
    }

    public void close() {
        flush();
        destroy();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("close: session has closed: ");
        sb.append(this);
        Log.d(str, sb.toString());
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        close();
        super.finalize();
    }

    public void processFrame(@NonNull FrameData frameData, FrameCallback frameCallback) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("processFrame: ");
        sb.append(frameData.toString());
        Log.d(str, sb.toString());
        int processFrame = MiCamAlgoInterfaceJNI.processFrame(this.mSessionHandle, frameData, frameCallback);
        if (processFrame == 0) {
            frameCallback.onFrameProcessed(processFrame, "onProcessStarted", null);
        } else {
            Util.assertOrNot(processFrame);
        }
    }

    public int processFrameWithSync(@NonNull List list, @NonNull Image image, int i) {
        long currentTimeMillis = System.currentTimeMillis();
        Log.d(TAG, "processFrameWithSync: start");
        int processFrameWithSync = MiCamAlgoInterfaceJNI.processFrameWithSync(this.mSessionHandle, list, image, i);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("processFrameWithSync: end, cost: ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(str, sb.toString());
        return processFrameWithSync;
    }
}
