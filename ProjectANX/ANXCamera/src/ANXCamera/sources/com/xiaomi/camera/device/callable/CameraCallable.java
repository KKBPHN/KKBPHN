package com.xiaomi.camera.device.callable;

import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import com.xiaomi.camera.device.CameraHandlerThread;
import com.xiaomi.camera.device.CameraHandlerThread.CookieStore;
import com.xiaomi.camera.rx.CameraSchedulers;
import java.lang.ref.WeakReference;

public abstract class CameraCallable {
    private long mBeginning;
    protected final WeakReference mCallbackHandler;
    protected final String mCameraId;
    protected final WeakReference mCameraListener;

    public CameraCallable(String str, CallableListener callableListener) {
        this(str, callableListener, null);
    }

    public CameraCallable(String str, CallableListener callableListener, Handler handler) {
        this.mCameraId = str;
        this.mCameraListener = new WeakReference(callableListener);
        this.mCallbackHandler = new WeakReference(handler);
    }

    protected static void runOnUiThread(Runnable runnable) {
        CameraSchedulers.sMainThreadScheduler.scheduleDirect(runnable);
    }

    @NonNull
    public abstract CallableReturn call();

    /* renamed from: callback */
    public void O000000o(CallableReturn callableReturn) {
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mBeginning;
        CallableListener callableListener = (CallableListener) this.mCameraListener.get();
        String str = ")";
        String str2 = " (dur: ";
        if (callableReturn.exception != null) {
            String tag = getTag();
            StringBuilder sb = new StringBuilder();
            sb.append("Failure: cid: ");
            sb.append(this.mCameraId);
            sb.append(str2);
            sb.append(elapsedRealtime);
            sb.append(str);
            Log.w(tag, sb.toString(), (Throwable) callableReturn.exception);
            if (callableListener != null) {
                callableListener.onFailure(callableReturn.exception);
            }
            return;
        }
        String tag2 = getTag();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Success: cid: ");
        sb2.append(this.mCameraId);
        sb2.append(str2);
        sb2.append(elapsedRealtime);
        sb2.append(str);
        Log.d(tag2, sb2.toString());
        if (callableListener != null) {
            callableListener.onSuccess(callableReturn.value);
        }
    }

    @NonNull
    public CookieStore getCookieStore() {
        return ((CameraHandlerThread) Thread.currentThread()).getCookieStore();
    }

    public abstract String getTag();

    /* access modifiers changed from: protected */
    public void postCallback(Runnable runnable) {
        Handler handler = (Handler) this.mCallbackHandler.get();
        if (handler != null) {
            handler.post(runnable);
        } else {
            runOnUiThread(runnable);
        }
    }

    public void run() {
        String tag = getTag();
        StringBuilder sb = new StringBuilder();
        sb.append("E: cid: ");
        sb.append(this.mCameraId);
        Log.d(tag, sb.toString());
        this.mBeginning = SystemClock.elapsedRealtime();
        CallableReturn call = call();
        String tag2 = getTag();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("X: cid: ");
        sb2.append(this.mCameraId);
        sb2.append(" (dur: ");
        sb2.append(SystemClock.elapsedRealtime() - this.mBeginning);
        sb2.append(")");
        Log.d(tag2, sb2.toString());
        postCallback(new O000000o(this, call));
    }
}
