package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import androidx.annotation.NonNull;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.compat.MiCameraCompat;

public class MiCamera2ShotBurst extends MiCamera2Shot {
    private static final int INVALID_SEQUENCE_ID = -1;
    /* access modifiers changed from: private */
    public static final String TAG = "MiCamera2ShotBurst";
    private boolean isFirstRequest = true;
    private int mCaptureRequestNum;
    /* access modifiers changed from: private */
    public TotalCaptureResult mCaptureResult;
    /* access modifiers changed from: private */
    public int mCurrentSequenceId;
    /* access modifiers changed from: private */
    public int mLatestSequenceId = 0;
    /* access modifiers changed from: private */
    public int mMaxCapturedNum;
    private boolean mNeedPausePreview;

    public MiCamera2ShotBurst(MiCamera2 miCamera2, int i, boolean z) {
        super(miCamera2);
        this.mMaxCapturedNum = i;
        this.mNeedPausePreview = z;
    }

    private void notifyResultData(byte[] bArr, CaptureResult captureResult) {
        PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback != null) {
            pictureCallback.onPictureTaken(bArr, captureResult);
        } else {
            Log.w(TAG, "notifyResultData: null picture callback");
        }
    }

    /* access modifiers changed from: private */
    public void onRepeatingEnd(boolean z, int i) {
        this.mMiCamera.setAWBLock(false);
        this.mMiCamera.resumePreview();
        if (-1 != i) {
            PictureCallback pictureCallback = getPictureCallback();
            if (pictureCallback != null) {
                pictureCallback.onPictureTakenFinished(z);
            } else {
                Log.w(TAG, "onRepeatingEnd: null picture callback");
            }
            this.mMiCamera.onMultiSnapEnd(z, this);
        }
    }

    /* access modifiers changed from: protected */
    public CaptureCallback generateCaptureCallback() {
        return new CaptureCallback() {
            public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
                MiCamera2ShotBurst.this.mCaptureResult = totalCaptureResult;
                MiCamera2ShotBurst.this.mMiCamera.updateFrameNumber(totalCaptureResult.getFrameNumber());
                if (C0124O00000oO.isMTKPlatform() && C0122O00000o.instance().OOOO00O() && MiCamera2ShotBurst.this.mMaxCapturedNum > 0) {
                    if (MiCamera2ShotBurst.this.mMiCamera.isMultiSnapStopRequest()) {
                        MiCamera2ShotBurst miCamera2ShotBurst = MiCamera2ShotBurst.this;
                        miCamera2ShotBurst.mLatestSequenceId = miCamera2ShotBurst.mCurrentSequenceId;
                        String access$100 = MiCamera2ShotBurst.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onCaptureCompleted:  latestSequenceId: ");
                        sb.append(MiCamera2ShotBurst.this.mLatestSequenceId);
                        Log.d(access$100, sb.toString());
                        return;
                    }
                    MiCamera2ShotBurst.this.startSessionCapture();
                }
            }

            public void onCaptureFailed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureFailure captureFailure) {
                super.onCaptureFailed(cameraCaptureSession, captureRequest, captureFailure);
                MiCamera2ShotBurst.this.onRepeatingEnd(false, -1);
                String access$100 = MiCamera2ShotBurst.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureFailed: ");
                sb.append(captureFailure.getReason());
                sb.append(" frameNumber=");
                sb.append(captureFailure.getFrameNumber());
                Log.e(access$100, sb.toString());
            }

            public void onCaptureSequenceAborted(@NonNull CameraCaptureSession cameraCaptureSession, int i) {
                super.onCaptureSequenceAborted(cameraCaptureSession, i);
                String access$100 = MiCamera2ShotBurst.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureSequenceAborted: ");
                sb.append(i);
                Log.w(access$100, sb.toString());
                if (MiCamera2ShotBurst.this.mLatestSequenceId == i) {
                    MiCamera2ShotBurst.this.onRepeatingEnd(false, i);
                }
            }

            public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession cameraCaptureSession, int i, long j) {
                String access$100 = MiCamera2ShotBurst.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureSequenceCompleted: sequenceId=");
                sb.append(i);
                sb.append(" latestSequenceId = ");
                sb.append(MiCamera2ShotBurst.this.mLatestSequenceId);
                sb.append(" frameNumber=");
                sb.append(j);
                Log.d(access$100, sb.toString());
                if (MiCamera2ShotBurst.this.mLatestSequenceId == i) {
                    MiCamera2ShotBurst.this.onRepeatingEnd(true, i);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public Builder generateRequestBuilder() {
        Builder builder;
        if (this.mMiCamera.getCameraDevice() == null) {
            return null;
        }
        if (!C0124O00000oO.isMTKPlatform()) {
            builder = this.mMiCamera.getCameraDevice().createCaptureRequest(2);
            builder.set(CaptureRequest.CONTROL_CAPTURE_INTENT, Integer.valueOf(2));
        } else if (!C0122O00000o.instance().OOOO00O() || this.mMaxCapturedNum <= 0) {
            builder = this.mMiCamera.getCameraDevice().createCaptureRequest(1);
            builder.set(CaptureRequest.CONTROL_CAPTURE_INTENT, Integer.valueOf(1));
            Log.d(TAG, "applyPanoramaP2SEnabled true");
            MiCameraCompat.applyPanoramaP2SEnabled(builder, true);
        } else {
            builder = this.mMiCamera.getCameraDevice().createCaptureRequest(2);
            MiCameraCompat.applyCShotFeatureCapture(builder, true);
            MiCameraCompat.applyNotificationTrigger(builder, true);
        }
        builder.addTarget(this.mMiCamera.getPhotoImageReader().getSurface());
        if (!this.mNeedPausePreview) {
            builder.addTarget(this.mMiCamera.getPreviewSurface());
        }
        this.mMiCamera.applySettingsForCapture(builder, 4);
        if (C0124O00000oO.isMTKPlatform()) {
            MiCameraCompat.applyZsl(builder, false);
        }
        if (this.mMiCamera.getCapabilities().isSupportBurstHint()) {
            MiCameraCompat.applyBurstHint(builder, 1);
        }
        return builder;
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void notifyResultData(byte[] bArr) {
        notifyResultData(bArr, null);
    }

    /* access modifiers changed from: protected */
    public void onImageReceived(Image image, int i) {
        PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback == null) {
            image.close();
        } else if (!pictureCallback.onPictureTakenImageConsumed(image, this.mCaptureResult)) {
            byte[] firstPlane = Util.getFirstPlane(image);
            image.close();
            notifyResultData(firstPlane, this.mCaptureResult);
        }
    }

    /* access modifiers changed from: protected */
    public void prepare() {
    }

    /* access modifiers changed from: protected */
    public void startSessionCapture() {
        MiCamera2 miCamera2;
        int i;
        if (C0124O00000oO.isMTKPlatform() && C0122O00000o.instance().OOOO00O()) {
            int i2 = this.mMaxCapturedNum;
            if (i2 > 0 && this.mCaptureRequestNum >= i2) {
                return;
            }
        }
        Log.d(TAG, "startSessionCapture");
        this.mMiCamera.pausePreview();
        try {
            CaptureCallback generateCaptureCallback = generateCaptureCallback();
            Builder generateRequestBuilder = generateRequestBuilder();
            if (generateRequestBuilder != null) {
                if (!C0124O00000oO.isMTKPlatform() || !C0122O00000o.instance().OOOO00O() || this.mMaxCapturedNum <= 0) {
                    this.mLatestSequenceId = this.mMiCamera.getCaptureSession().setRepeatingRequest(generateRequestBuilder.build(), generateCaptureCallback, this.mCameraHandler);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("repeating sequenceId: ");
                    sb.append(this.mLatestSequenceId);
                    Log.d(str, sb.toString());
                }
                int i3 = this.isFirstRequest ? 3 : 1;
                this.isFirstRequest = false;
                for (int i4 = 0; i4 < i3; i4++) {
                    int capture = this.mMiCamera.getCaptureSession().capture(generateRequestBuilder.build(), generateCaptureCallback, this.mCameraHandler);
                    this.mCaptureRequestNum++;
                    this.mCurrentSequenceId = capture;
                    if (this.mCaptureRequestNum == this.mMaxCapturedNum) {
                        this.mLatestSequenceId = capture;
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("mtk cshot repeating latestSequenceId: ");
                        sb2.append(this.mLatestSequenceId);
                        Log.d(str2, sb2.toString());
                    }
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("mtk cshot repeating sequenceId: ");
                    sb3.append(capture);
                    sb3.append(" captureRequestNum=");
                    sb3.append(this.mCaptureRequestNum);
                    Log.d(str3, sb3.toString());
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            miCamera2 = this.mMiCamera;
            i = e.getReason();
            miCamera2.notifyOnError(i);
        } catch (IllegalStateException e2) {
            Log.e(TAG, "Failed to capture burst, IllegalState", (Throwable) e2);
            miCamera2 = this.mMiCamera;
            i = 256;
            miCamera2.notifyOnError(i);
        }
    }
}
