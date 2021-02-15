package com.xiaomi.camera.device.callable;

import android.os.Handler;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CaptureBusyCallback;
import com.xiaomi.camera.device.CameraHandlerThread.Cookie;
import com.xiaomi.camera.device.CameraService;

public class ReleaseCameraCallable extends CameraCallable implements CaptureBusyCallback {
    private static final String TAG = "ReleaseCameraCallable";
    private final boolean mImmediately;

    public ReleaseCameraCallable(String str, boolean z, CameraListener cameraListener, Handler handler) {
        super(str, cameraListener, handler);
        this.mImmediately = z;
    }

    @NonNull
    public CallableReturn call() {
        for (Cookie cookie : getCookieStore().getCookies()) {
            Camera2Proxy camera2Proxy = cookie.mCamera2Device;
            if (camera2Proxy != null) {
                String valueOf = String.valueOf(camera2Proxy.getId());
                String str = this.mCameraId;
                if (str == null || str.equals(valueOf)) {
                    String str2 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("E: releaseCamera: cid = ");
                    sb.append(valueOf);
                    Log.d(str2, sb.toString());
                    cookie.mCamera2Device.setCaptureBusyCallback(this);
                    String str3 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("X: releaseCamera: cid = ");
                    sb2.append(valueOf);
                    Log.d(str3, sb2.toString());
                }
            }
        }
        return new CallableReturn((Exception) null);
    }

    public String getTag() {
        return TAG;
    }

    public void onCaptureCompleted(boolean z) {
        if (this.mImmediately) {
            CameraService.addCameraCallable(new CloseCameraCallable(null, null, null, new String[0]));
        }
    }
}
