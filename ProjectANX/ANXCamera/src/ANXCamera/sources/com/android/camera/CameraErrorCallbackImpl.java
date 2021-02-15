package com.android.camera;

import com.android.camera.aftersales.AftersalesManager;
import com.android.camera.log.Log;
import com.android.camera.module.Module;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraErrorCallback;
import java.lang.ref.WeakReference;

public class CameraErrorCallbackImpl implements CameraErrorCallback {
    private static final String TAG = "CameraErrorCallback";
    private WeakReference mWeakActivity;

    public CameraErrorCallbackImpl(ActivityBase activityBase) {
        this.mWeakActivity = new WeakReference(activityBase);
    }

    public void onCameraError(Camera2Proxy camera2Proxy, int i) {
        String sb;
        String str = TAG;
        if (i == 5) {
            sb = "onCameraError: camera service error";
        } else if (i == 4) {
            sb = "onCameraError: camera device error";
        } else if (i == 3) {
            sb = "onCameraError: camera disabled";
        } else if (i == 2) {
            sb = "onCameraError: max camera in use";
        } else if (i == 1) {
            sb = "onCameraError: camera in use";
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onCameraError: other error ");
            sb2.append(i);
            sb = sb2.toString();
        }
        Log.k(6, str, sb);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append(i);
        CameraStatUtils.trackCameraError(sb3.toString());
        AftersalesManager.getInstance().count(System.currentTimeMillis(), 5, CameraSettings.getCameraId());
        ActivityBase activityBase = (ActivityBase) this.mWeakActivity.get();
        if (activityBase != null) {
            Module currentModule = activityBase.getCurrentModule();
            if (currentModule != null && currentModule.isCreated()) {
                currentModule.notifyError();
                return;
            }
            return;
        }
        Log.d(str, "mActivity has been collected.");
    }
}
