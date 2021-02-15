package com.ss.android.ttve.nativePort;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import androidx.annotation.Keep;
import com.ss.android.ttve.utils.CameraInstance;
import com.ss.android.ttve.utils.CameraInstance.CameraOpenCallback;
import com.ss.android.vesdk.VELogUtil;

@Keep
public class TECameraProxy extends CameraInstance {
    private static final String TAG = "TECameraProxy";
    /* access modifiers changed from: private */
    public static long mNativeAddr;
    private CameraOpenCallback mCameraOpenCallback = new CameraOpenCallback() {
        public void cameraReady() {
            TECameraProxy.this.nativeOnCameraCreate(TECameraProxy.mNativeAddr, 0);
        }
    };
    private int mCameraTextureID = 0;
    private SurfaceTexture mSurfaceTexture;
    /* access modifiers changed from: private */
    public boolean mbSurfaceTextureReleased = false;

    static {
        TENativeLibsLoader.loadLibrary();
    }

    public static TECameraProxy create(long j) {
        mNativeAddr = j;
        return new TECameraProxy();
    }

    public native int nativeOnCameraCreate(long j, int i);

    public native int nativeOnFrameAvailable(long j, SurfaceTexture surfaceTexture);

    public synchronized void getNextFrame() {
        this.mSurfaceTexture.updateTexImage();
    }

    public synchronized boolean open(int i) {
        return tryOpenCamera(this.mCameraOpenCallback, i);
    }

    public int setSurfaceTexture(Object obj, int i) {
        String str;
        String str2;
        VELogUtil.d(TAG, "setSurfaceTexture...");
        if (i == 0) {
            str = TAG;
            str2 = "Invalid Texture ID!";
        } else if (obj instanceof SurfaceTexture) {
            this.mSurfaceTexture = (SurfaceTexture) obj;
            this.mSurfaceTexture.setOnFrameAvailableListener(new OnFrameAvailableListener() {
                public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    if (!TECameraProxy.this.mbSurfaceTextureReleased) {
                        TECameraProxy.this.nativeOnFrameAvailable(TECameraProxy.mNativeAddr, surfaceTexture);
                    }
                }
            });
            return 0;
        } else {
            str = TAG;
            str2 = "Invalid SurfaceTexture!";
        }
        VELogUtil.e(str, str2);
        return -100;
    }

    public synchronized void startPreview() {
        startPreview(this.mSurfaceTexture);
        this.mbSurfaceTextureReleased = false;
    }

    public synchronized void stopCamera() {
        this.mbSurfaceTextureReleased = true;
        this.mSurfaceTexture.setOnFrameAvailableListener(null);
        super.stopCamera();
    }

    public synchronized boolean switchCamera(int i) {
        boolean z;
        if (tryOpenCamera(this.mCameraOpenCallback, i)) {
            startPreview(this.mSurfaceTexture);
            z = true;
        } else {
            z = false;
        }
        return z;
    }
}
