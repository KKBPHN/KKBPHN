package com.android.camera.module.loader.camera2;

import android.util.SparseArray;
import androidx.annotation.Nullable;
import com.android.camera.log.Log;
import com.android.camera2.CameraCapabilities;

public abstract class Camera2CompatAdapter implements Camera2Compat {
    protected static final int INVALID_CAMERA_ID = -1;
    private static final String TAG = "Camera2CompatAdapter";
    public static final int TELE_CAMERA_ID = 20;
    protected volatile SparseArray mCapabilities = null;
    protected volatile int mCurrentOpenedCameraId = -1;

    public SparseArray getCapabilities() {
        return this.mCapabilities;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0032, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    public synchronized CameraCapabilities getCapabilities(int i) {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getCapabilities(): #init() failed.");
            return null;
        }
        CameraCapabilities cameraCapabilities = (CameraCapabilities) this.mCapabilities.get(i);
        if (cameraCapabilities == null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Warning: getCapabilities(): return null for camera: ");
            sb.append(i);
            Log.d(str, sb.toString());
        }
    }

    public synchronized CameraCapabilities getCurrentCameraCapabilities() {
        if (this.mCurrentOpenedCameraId == -1) {
            Log.d(TAG, "Warning: getCurrentCameraCapabilities(): mCurrentOpenedCameraId is invalid.");
        }
        return getCapabilities(this.mCurrentOpenedCameraId);
    }

    public int getMaxJpegSize() {
        SparseArray capabilities = getCapabilities();
        int i = 0;
        if (capabilities != null) {
            int i2 = 0;
            while (i < capabilities.size()) {
                CameraCapabilities cameraCapabilities = (CameraCapabilities) capabilities.valueAt(i);
                if (cameraCapabilities != null) {
                    int maxJpegSize = cameraCapabilities.getMaxJpegSize();
                    if (i2 < maxJpegSize) {
                        i2 = maxJpegSize;
                    }
                }
                i++;
            }
            i = i2;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getMaxJpegSize: ");
        sb.append(i);
        Log.v(str, sb.toString());
        return i;
    }

    public synchronized void setCurrentOpenedCameraId(int i) {
        this.mCurrentOpenedCameraId = i;
    }
}
