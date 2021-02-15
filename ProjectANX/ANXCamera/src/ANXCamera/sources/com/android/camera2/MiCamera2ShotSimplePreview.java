package com.android.camera2;

import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CaptureRequest.Builder;
import android.location.Location;
import android.media.Image;
import com.android.camera.LocationManager;
import com.android.camera.SurfaceTextureScreenNail.PreviewSaveListener;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.storage.ImageSaver;
import com.android.camera2.Camera2Proxy.PictureCallback;

public class MiCamera2ShotSimplePreview extends MiCamera2Shot implements PreviewSaveListener {
    private static final String TAG = "MiCamera2ShotSimplePreview";
    private ImageSaver mSaver;

    public MiCamera2ShotSimplePreview(MiCamera2 miCamera2) {
        super(miCamera2);
    }

    /* access modifiers changed from: protected */
    public CaptureCallback generateCaptureCallback() {
        return null;
    }

    /* access modifiers changed from: protected */
    public Builder generateRequestBuilder() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void notifyResultData(byte[] bArr) {
    }

    /* access modifiers changed from: protected */
    public void onImageReceived(Image image, int i) {
    }

    /* access modifiers changed from: protected */
    public void prepare() {
    }

    public void save(byte[] bArr, int i, int i2, int i3) {
        PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback != null) {
            pictureCallback.onPictureTakenFinished(true);
        }
        long currentTimeMillis = System.currentTimeMillis();
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        ImageSaver imageSaver = this.mSaver;
        if (imageSaver != null) {
            imageSaver.addSimpleImage(bArr, true, Util.createJpegName(currentTimeMillis), null, System.currentTimeMillis(), null, currentLocation, i, i2, null, i3, false, false, true, false, false, null, null, -1, null);
        }
    }

    public void setImageSaver(ImageSaver imageSaver) {
        this.mSaver = imageSaver;
    }

    /* access modifiers changed from: protected */
    public void startSessionCapture() {
        PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback != null) {
            pictureCallback.onCaptureShutter(true, false, false, false);
        } else {
            Log.w(TAG, "startSessionCapture: null picture callback");
        }
    }
}
