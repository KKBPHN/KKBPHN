package com.android.camera2;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import androidx.annotation.NonNull;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera2.Camera2Proxy.PictureCallback;

public class MiCamera2ShotVideo extends MiCamera2Shot {
    /* access modifiers changed from: private */
    public static final String TAG = "MiCamera2ShotVideo";

    public MiCamera2ShotVideo(MiCamera2 miCamera2) {
        super(miCamera2);
    }

    /* access modifiers changed from: protected */
    public CaptureCallback generateCaptureCallback() {
        return new CaptureCallback() {
            public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
                String access$000 = MiCamera2ShotVideo.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureCompleted: ");
                sb.append(totalCaptureResult.getFrameNumber());
                Log.d(access$000, sb.toString());
            }
        };
    }

    /* access modifiers changed from: protected */
    public Builder generateRequestBuilder() {
        CameraDevice cameraDevice;
        int i = 2;
        if (2 == this.mMiCamera.getCapabilities().getSupportedHardwareLevel()) {
            cameraDevice = this.mMiCamera.getCameraDevice();
        } else {
            cameraDevice = this.mMiCamera.getCameraDevice();
            i = 4;
        }
        Builder createCaptureRequest = cameraDevice.createCaptureRequest(i);
        ImageReader videoSnapshotImageReader = this.mMiCamera.getVideoSnapshotImageReader();
        createCaptureRequest.addTarget(videoSnapshotImageReader.getSurface());
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("size=");
        sb.append(videoSnapshotImageReader.getWidth());
        sb.append("x");
        sb.append(videoSnapshotImageReader.getHeight());
        Log.d(str, sb.toString());
        if (this.mMiCamera.getPreviewSurface() != null) {
            createCaptureRequest.addTarget(this.mMiCamera.getPreviewSurface());
        }
        if (this.mMiCamera.getRecordSurface() != null) {
            createCaptureRequest.addTarget(this.mMiCamera.getRecordSurface());
        }
        this.mMiCamera.applySettingsForVideoShot(createCaptureRequest, 3);
        return createCaptureRequest;
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void notifyResultData(byte[] bArr) {
        PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback != null) {
            pictureCallback.onPictureTaken(bArr, null);
        } else {
            Log.w(TAG, "notifyResultData: null picture callback");
        }
    }

    /* access modifiers changed from: protected */
    public void onImageReceived(Image image, int i) {
        byte[] firstPlane = Util.getFirstPlane(image);
        image.close();
        notifyResultData(firstPlane);
    }

    /* access modifiers changed from: protected */
    public void prepare() {
    }

    /* access modifiers changed from: protected */
    public void startSessionCapture() {
        MiCamera2 miCamera2;
        int i;
        try {
            Builder generateRequestBuilder = generateRequestBuilder();
            this.mMiCamera.getCaptureSession().capture(generateRequestBuilder.build(), generateCaptureCallback(), this.mCameraHandler);
            return;
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot capture a video snapshot");
            miCamera2 = this.mMiCamera;
            i = e.getReason();
        } catch (IllegalStateException e2) {
            Log.e(TAG, "Failed to capture a video snapshot, IllegalState", (Throwable) e2);
            miCamera2 = this.mMiCamera;
            i = 256;
        }
        miCamera2.notifyOnError(i);
    }
}
