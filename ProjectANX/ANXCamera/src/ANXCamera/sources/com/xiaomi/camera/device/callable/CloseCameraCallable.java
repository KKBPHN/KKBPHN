package com.xiaomi.camera.device.callable;

import android.os.Handler;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import com.android.camera2.Camera2Proxy;
import com.xiaomi.camera.device.CameraHandlerThread.Cookie;
import com.xiaomi.camera.device.CameraHandlerThread.CookieStore;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CloseCameraCallable extends CameraCallable {
    private static final String TAG = "CloseCameraCallable";
    private final Set mExclusions = new HashSet();

    public CloseCameraCallable(String str, CameraListener cameraListener, Handler handler, String... strArr) {
        super(str, cameraListener, handler);
        if (strArr != null && strArr.length > 0) {
            this.mExclusions.addAll(Arrays.asList(strArr));
            if (str != null && this.mExclusions.contains(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append("ambiguous camera id: ");
                sb.append(str);
                sb.append("");
                throw new IllegalArgumentException(sb.toString());
            }
        }
    }

    @NonNull
    public CallableReturn call() {
        CookieStore cookieStore = getCookieStore();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("E: closeCamera: cid = ");
        sb.append(this.mCameraId);
        String str2 = ", excludes = ";
        sb.append(str2);
        sb.append(this.mExclusions);
        Log.k(3, str, sb.toString());
        for (Cookie cookie : cookieStore.getCookies()) {
            Camera2Proxy camera2Proxy = cookie.mCamera2Device;
            if (camera2Proxy != null) {
                String valueOf = String.valueOf(camera2Proxy.getId());
                String str3 = this.mCameraId;
                if (str3 == null || str3.equals(valueOf)) {
                    if (this.mExclusions.contains(valueOf)) {
                        String str4 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("closeCamera: camera '");
                        sb2.append(valueOf);
                        sb2.append("' is excluded");
                        Log.d(str4, sb2.toString());
                    } else {
                        String str5 = TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("closeCamera: cid = ");
                        sb3.append(valueOf);
                        Log.d(str5, sb3.toString());
                        cookie.mCamera2Device.releasePreview(0);
                        cookie.mCamera2Device.resetConfigs();
                        cookie.mIsOpening = false;
                        cookie.mIsClosing = cookie.mCamera2Device.close();
                        cookie.mCamera2Device = null;
                    }
                }
            }
        }
        String str6 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("X: closeCamera: cid = ");
        sb4.append(this.mCameraId);
        sb4.append(str2);
        sb4.append(this.mExclusions);
        Log.k(3, str6, sb4.toString());
        return new CallableReturn((Exception) null);
    }

    public String getTag() {
        return TAG;
    }
}
