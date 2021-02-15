package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.graphics.Rect;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import androidx.annotation.NonNull;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.log.Log;
import com.arcsoft.supernight.SuperNightProcess;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class SuperNightReprocessHandler extends Handler {
    private static final int MSG_CLEANUP = 19;
    private static final int MSG_DATA = 16;
    private static final int MSG_META = 17;
    private static final int MSG_PREPARE = 18;
    private static final int RAW_FORMAT_RAW10 = 1;
    private static final int RAW_FORMAT_RAW12 = 0;
    private static final String TAG = "SNReprocessHandler";
    private final int mBaseEvIndex = 3;
    /* access modifiers changed from: private */
    public MiCamera2ShotRawBurst mCamera2Shot;
    /* access modifiers changed from: private */
    public final MiCamera2 mCameraDevice;
    private final int mCameraMode;
    private final Handler mCaptureStateHandler;
    private final AtomicBoolean mIsCancelled;
    private final int mMaxInputImageCount = MiCamera2ShotRawBurst.EV_LIST.length;
    private final SuperNightProcess mSuperNightProcess;
    private final ArrayList mUnprocessedData = new ArrayList();
    private final ArrayList mUnprocessedMeta = new ArrayList();
    private int superNightRawFormat;

    public SuperNightReprocessHandler(Looper looper, MiCamera2 miCamera2, int i) {
        int i2;
        super(looper);
        if (i == 0) {
            this.superNightRawFormat = SuperNightProcess.ASVL_PAF_RAW12_GRBG_16B;
            i2 = C0122O00000o.instance().OO0OOoo() ? SuperNightProcess.ARC_SN_CAMERA_MODE_XIAOMI_G90_GW1_INDIA : 1793;
        } else if (i != 1) {
            i2 = 0;
        } else {
            this.superNightRawFormat = SuperNightProcess.ASVL_PAF_RAW10_GRBG_16B;
            i2 = C0122O00000o.instance().OO0OOoo() ? SuperNightProcess.ARC_SN_CAMERA_MODE_XIAOMI_J15S_G85_S5KGM1_N_INDIA : SuperNightProcess.ARC_SN_CAMERA_MODE_XIAOMI_J15S_G85_S5KGM1_N;
        }
        this.mCameraDevice = miCamera2;
        this.mCaptureStateHandler = this.mCameraDevice.getCameraHandler();
        this.mSuperNightProcess = new SuperNightProcess(this.mCameraDevice.getCapabilities().getActiveArraySize());
        this.mIsCancelled = new AtomicBoolean(false);
        this.mCameraMode = i2;
    }

    private void clearCache() {
        String str = TAG;
        Log.e(str, "clearCache: E");
        this.mUnprocessedMeta.clear();
        Iterator it = this.mUnprocessedData.iterator();
        while (it.hasNext()) {
            ((Image) it.next()).close();
        }
        this.mUnprocessedData.clear();
        Log.e(str, "clearCache: X");
    }

    public static boolean convert(Rect rect, int i, int i2, Rect rect2, float f) {
        int i3 = rect.left;
        int i4 = i - rect.right;
        if (i3 > i4) {
            rect.right = i - i3;
        } else if (i3 < i4) {
            rect.left = i4;
        }
        int i5 = rect.top;
        int i6 = i2 - rect.bottom;
        if (i5 > i6) {
            rect.bottom = i2 - i5;
        } else if (i5 < i6) {
            rect.top = i6;
        }
        int width = rect.width() * i2;
        int height = rect.height() * i;
        if (width > height) {
            int i7 = (int) ((((float) height) * 0.5f) / ((float) i2));
            rect.left = rect.centerX() - i7;
            rect.right = rect.centerX() + i7;
        } else if (width < height) {
            int i8 = (int) ((((float) width) * 0.5f) / ((float) i));
            rect.top = rect.centerY() - i8;
            rect.bottom = rect.centerY() + i8;
        }
        float width2 = ((float) rect2.width()) / ((float) i);
        float height2 = ((float) rect2.height()) / ((float) i2);
        rect.left = (int) (((float) rect.left) * width2);
        rect.top = (int) (((float) rect.top) * height2);
        int i9 = rect2.right;
        int i10 = rect.left;
        rect.right = i9 - i10;
        rect.bottom = rect2.bottom - rect.top;
        if (i10 % 2 != 0) {
            rect.left = i10 + 1;
        }
        int i11 = rect.top;
        if (i11 % 2 != 0) {
            rect.top = i11 + 1;
        }
        int i12 = rect.right;
        if (i12 % 2 != 0) {
            rect.right = i12 - 1;
        }
        int i13 = rect.bottom;
        if (i13 % 2 != 0) {
            rect.bottom = i13 - 1;
        }
        return rect.intersect(HybridZoomingSystem.toCropRegion(f, rect2));
    }

    private CaptureCallback generateReprocessCaptureCallback() {
        return new CaptureCallback() {
            public void onCaptureBufferLost(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull Surface surface, long j) {
                super.onCaptureBufferLost(cameraCaptureSession, captureRequest, surface, j);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureBufferLost:<JPEG>: frameNumber = ");
                sb.append(j);
                Log.e(SuperNightReprocessHandler.TAG, sb.toString());
                if (SuperNightReprocessHandler.this.mCameraDevice.getSuperNight()) {
                    SuperNightReprocessHandler.this.mCameraDevice.setAWBLock(false);
                }
                SuperNightReprocessHandler.this.mCameraDevice.onCapturePictureFinished(false, SuperNightReprocessHandler.this.mCamera2Shot);
            }

            public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureCompleted:<JPEG>: ");
                sb.append(totalCaptureResult.getFrameNumber());
                Log.d(SuperNightReprocessHandler.TAG, sb.toString());
                if (SuperNightReprocessHandler.this.mCameraDevice.getSuperNight()) {
                    SuperNightReprocessHandler.this.mCameraDevice.setAWBLock(false);
                }
                SuperNightReprocessHandler.this.mCameraDevice.onCapturePictureFinished(true, SuperNightReprocessHandler.this.mCamera2Shot);
            }

            public void onCaptureFailed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureFailure captureFailure) {
                super.onCaptureFailed(cameraCaptureSession, captureRequest, captureFailure);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureFailed:<JPEG>: reason = ");
                sb.append(captureFailure.getReason());
                Log.e(SuperNightReprocessHandler.TAG, sb.toString());
                if (SuperNightReprocessHandler.this.mCameraDevice.getSuperNight()) {
                    SuperNightReprocessHandler.this.mCameraDevice.setAWBLock(false);
                }
                SuperNightReprocessHandler.this.mCameraDevice.onCapturePictureFinished(false, SuperNightReprocessHandler.this.mCamera2Shot);
            }

            public void onCaptureStarted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, long j, long j2) {
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureStarted:<JPEG>: ");
                sb.append(j2);
                Log.d(SuperNightReprocessHandler.TAG, sb.toString());
                super.onCaptureStarted(cameraCaptureSession, captureRequest, j, j2);
            }
        };
    }

    private void sendReprocessRequest() {
        int size = this.mUnprocessedData.size();
        int i = this.mMaxInputImageCount;
        String str = TAG;
        if (size == i && this.mUnprocessedMeta.size() == this.mMaxInputImageCount && this.mSuperNightProcess != null && !this.mIsCancelled.get()) {
            Collections.reverse(this.mUnprocessedData);
            Collections.reverse(this.mUnprocessedMeta);
            Log.d(str, "sendReprocessRequest:<SNP>: E");
            this.mSuperNightProcess.init(this.mCameraMode, ((Image) this.mUnprocessedData.get(0)).getWidth(), ((Image) this.mUnprocessedData.get(0)).getHeight(), ((Image) this.mUnprocessedData.get(0)).getPlanes()[0].getRowStride());
            Image dequeueInputImage = this.mCameraDevice.getRawImageWriter().dequeueInputImage();
            Rect rect = new Rect();
            this.mSuperNightProcess.addAllInputInfo(this.mUnprocessedData, this.mUnprocessedMeta, this.superNightRawFormat, dequeueInputImage, rect);
            this.mSuperNightProcess.unInit();
            Log.d(str, "sendReprocessRequest:<SNP>: X");
            if (this.mIsCancelled.get()) {
                clearCache();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("sendReprocessRequest:<CROP>: E ");
            sb.append(rect);
            Log.d(str, sb.toString());
            convert(rect, ((Image) this.mUnprocessedData.get(0)).getWidth(), ((Image) this.mUnprocessedData.get(0)).getHeight(), this.mCameraDevice.getCapabilities().getActiveArraySize(), this.mCameraDevice.getZoomRatio());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("sendReprocessRequest:<CROP>: X ");
            sb2.append(rect);
            Log.d(str, sb2.toString());
            TotalCaptureResult totalCaptureResult = (TotalCaptureResult) this.mUnprocessedMeta.get((this.mMaxInputImageCount - 1) - 3);
            try {
                Log.d(str, "sendReprocessRequest:<CAM>: E");
                this.mCameraDevice.getRawImageWriter().queueInputImage(dequeueInputImage);
                Builder createReprocessCaptureRequest = this.mCameraDevice.getCameraDevice().createReprocessCaptureRequest(totalCaptureResult);
                this.mCameraDevice.applySettingsForJpeg(createReprocessCaptureRequest);
                createReprocessCaptureRequest.addTarget(this.mCameraDevice.getPhotoImageReader().getSurface());
                createReprocessCaptureRequest.set(CaptureRequest.SCALER_CROP_REGION, rect);
                this.mCameraDevice.getCaptureSession().capture(createReprocessCaptureRequest.build(), generateReprocessCaptureCallback(), this.mCaptureStateHandler);
                Log.d(str, "sendReprocessRequest:<CAM>: X");
            } catch (Exception e) {
                Log.d(str, "sendReprocessRequest:<CAM>", (Throwable) e);
            } catch (Throwable th) {
                clearCache();
                throw th;
            }
            clearCache();
        } else if (this.mIsCancelled.get()) {
            clearCache();
            Log.d(str, "sendReprocessRequest:<CAM>: CANCELLED");
        }
    }

    public void cancel() {
        if (this.mSuperNightProcess != null) {
            String str = TAG;
            Log.d(str, "cancelSuperNight: E");
            this.mIsCancelled.set(true);
            this.mSuperNightProcess.cancelSuperNight();
            Log.d(str, "cancelSuperNight: X");
        }
        obtainMessage(19).sendToTarget();
    }

    public void handleMessage(Message message) {
        Object obj;
        ArrayList arrayList;
        switch (message.what) {
            case 16:
                arrayList = this.mUnprocessedData;
                obj = (Image) message.obj;
                break;
            case 17:
                arrayList = this.mUnprocessedMeta;
                obj = (TotalCaptureResult) message.obj;
                break;
            case 18:
                this.mIsCancelled.set(false);
                clearCache();
                this.mCamera2Shot = (MiCamera2ShotRawBurst) message.obj;
                return;
            case 19:
                clearCache();
                return;
            default:
                return;
        }
        arrayList.add(obj);
        sendReprocessRequest();
    }

    public void prepare(MiCamera2ShotRawBurst miCamera2ShotRawBurst) {
        obtainMessage(18, miCamera2ShotRawBurst).sendToTarget();
    }

    public void queueCaptureResult(TotalCaptureResult totalCaptureResult) {
        obtainMessage(17, totalCaptureResult).sendToTarget();
    }

    public void queueImage(Image image) {
        obtainMessage(16, image).sendToTarget();
    }
}
