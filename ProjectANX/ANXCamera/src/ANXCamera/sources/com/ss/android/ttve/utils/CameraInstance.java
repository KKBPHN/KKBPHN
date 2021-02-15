package com.ss.android.ttve.utils;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import androidx.annotation.Keep;
import androidx.core.app.NotificationManagerCompat;
import com.android.camera.Util;
import com.android.camera.constant.AutoFocus;
import com.ss.android.vesdk.VELogUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Keep
public class CameraInstance {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String ASSERT_MSG = "检测到CameraDevice 为 null! 请检查";
    public static final int DEFAULT_PREVIEW_RATE = 30;
    public static final String TAG = "CameraInstance";
    private static CameraInstance mThisInstance;
    private Comparator comparatorBigger = new Comparator() {
        public int compare(Size size, Size size2) {
            int i = size2.width - size.width;
            return i == 0 ? size2.height - size.height : i;
        }
    };
    private Comparator comparatorSmaller = new Comparator() {
        public int compare(Size size, Size size2) {
            int i = size.width - size2.width;
            return i == 0 ? size.height - size2.height : i;
        }
    };
    private Camera mCameraDevice;
    private int mDefaultCameraID = -1;
    private int mFacing = 0;
    private boolean mIsPreviewing = false;
    private Parameters mParams;
    private int mPictureHeight = Util.LIMIT_SURFACE_WIDTH;
    private int mPictureWidth = 1280;
    private int mPreferPreviewHeight = Util.LIMIT_SURFACE_WIDTH;
    private int mPreferPreviewWidth = 1280;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private int mRotation;

    public interface CameraOpenCallback {
        void cameraReady();
    }

    private static int clamp(int i) {
        return clamp(i, NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, 1000);
    }

    private static int clamp(int i, int i2, int i3) {
        return i > i3 ? i3 : i < i2 ? i2 : i;
    }

    public static synchronized CameraInstance getInstance() {
        CameraInstance cameraInstance;
        synchronized (CameraInstance.class) {
            if (mThisInstance == null) {
                mThisInstance = new CameraInstance();
            }
            cameraInstance = mThisInstance;
        }
        return cameraInstance;
    }

    private void rotateRectForOrientation(int i, Rect rect, Rect rect2) {
        Matrix matrix = new Matrix();
        matrix.setRotate((float) (-i));
        RectF rectF = new RectF(rect);
        RectF rectF2 = new RectF(rect2);
        matrix.mapRect(rectF);
        matrix.mapRect(rectF2);
        matrix.reset();
        matrix.setTranslate(-rectF.left, -rectF.top);
        matrix.mapRect(rectF);
        matrix.mapRect(rectF2);
        rect.set((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
        rect2.set((int) rectF2.left, (int) rectF2.top, (int) rectF2.right, (int) rectF2.bottom);
    }

    private void setCameraDisplayOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int i = 0;
        if (rotation != 0) {
            if (rotation == 1) {
                i = 90;
            } else if (rotation == 2) {
                i = 180;
            } else if (rotation == 3) {
                i = 270;
            }
        }
        this.mRotation = setOrientationDegrees(i);
    }

    public Rect calculateTapArea(float f, float f2, float f3, int i) {
        int intValue = Float.valueOf(f3 * 1000.0f).intValue();
        int i2 = ((int) (f2 * 2000.0f)) - 1000;
        int i3 = intValue / 2;
        int clamp = clamp((((int) (f * 2000.0f)) - 1000) - i3, NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, 1000);
        int clamp2 = clamp(i2 - i3, NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, 1000);
        RectF rectF = new RectF((float) clamp, (float) clamp2, (float) clamp(clamp + intValue), (float) clamp(clamp2 + intValue));
        Rect rect = new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
        rotateRectForOrientation(i, new Rect(NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, 1000, 1000), rect);
        Rect rect2 = new Rect(rect.left - 1000, rect.top - 1000, rect.right - 1000, rect.bottom - 1000);
        rect2.left = clamp(rect2.left);
        rect2.right = clamp(rect2.right);
        rect2.top = clamp(rect2.top);
        rect2.bottom = clamp(rect2.bottom);
        return rect2;
    }

    public void focusAtPoint(float f, float f2) {
        focusAtPoint(f, f2, 0.08f);
    }

    public synchronized void focusAtPoint(float f, float f2, float f3) {
        if (this.mCameraDevice == null) {
            VELogUtil.e(TAG, "Error: focus after release.");
            return;
        }
        this.mParams = this.mCameraDevice.getParameters();
        if (this.mParams.getMaxNumMeteringAreas() > 0) {
            Rect calculateTapArea = calculateTapArea(f, f2, f3, this.mRotation);
            VELogUtil.d(TAG, String.format("FocusAreas: [%d, %d, %d, %d]", new Object[]{Integer.valueOf(calculateTapArea.left), Integer.valueOf(calculateTapArea.top), Integer.valueOf(calculateTapArea.right), Integer.valueOf(calculateTapArea.bottom)}));
            ArrayList arrayList = new ArrayList();
            arrayList.add(new Area(calculateTapArea, 1000));
            try {
                this.mCameraDevice.cancelAutoFocus();
                if (!TextUtils.equals(Build.BRAND, "Multilaser") && !TextUtils.equals(Build.BRAND, "MS40")) {
                    this.mParams.setFocusAreas(arrayList);
                }
                this.mParams.setMeteringAreas(arrayList);
                this.mParams.setFocusMode(AutoFocus.LEGACY_CONTINUOUS_VIDEO);
                this.mCameraDevice.setParameters(this.mParams);
                this.mCameraDevice.autoFocus(new AutoFocusCallback() {
                    public void onAutoFocus(boolean z, Camera camera) {
                        String str = CameraInstance.TAG;
                        if (z) {
                            VELogUtil.i(str, "Camera Focus Succeed!");
                            return;
                        }
                        VELogUtil.i(str, "Camera Focus Failed!");
                        try {
                            Parameters parameters = camera.getParameters();
                            parameters.setFocusMode("auto");
                            camera.setParameters(parameters);
                        } catch (Exception unused) {
                        }
                    }
                });
            } catch (Exception e) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Error: focusAtPoint failed: ");
                sb.append(e.toString());
                VELogUtil.e(str, sb.toString());
            }
        } else {
            VELogUtil.i(TAG, "The device does not support metering areas...");
        }
    }

    public Camera getCameraDevice() {
        return this.mCameraDevice;
    }

    public int getCameraFacing() {
        return this.mFacing;
    }

    public synchronized Parameters getParams() {
        if (this.mCameraDevice == null) {
            return null;
        }
        return this.mCameraDevice.getParameters();
    }

    public int getRotation() {
        return this.mRotation;
    }

    public void initCamera(int i) {
        Camera camera = this.mCameraDevice;
        String str = TAG;
        if (camera == null) {
            VELogUtil.e(str, "initCamera: Camera is not opened!");
            return;
        }
        this.mParams = camera.getParameters();
        for (Integer intValue : this.mParams.getSupportedPictureFormats()) {
            VELogUtil.i(str, String.format("Picture Format: %x", new Object[]{Integer.valueOf(intValue.intValue())}));
        }
        this.mParams.setPictureFormat(256);
        List<Size> supportedPictureSizes = this.mParams.getSupportedPictureSizes();
        Collections.sort(supportedPictureSizes, this.comparatorBigger);
        Size size = null;
        Size size2 = null;
        for (Size size3 : supportedPictureSizes) {
            if (size2 == null || (size3.width >= this.mPictureWidth && size3.height >= this.mPictureHeight)) {
                size2 = size3;
            }
        }
        List<Size> supportedPreviewSizes = this.mParams.getSupportedPreviewSizes();
        Collections.sort(supportedPreviewSizes, this.comparatorBigger);
        for (Size size4 : supportedPreviewSizes) {
            VELogUtil.d(str, String.format("Supported preview size: %d x %d", new Object[]{Integer.valueOf(size4.width), Integer.valueOf(size4.height)}));
            if (size == null || (size4.width >= this.mPreferPreviewWidth && size4.height >= this.mPreferPreviewHeight)) {
                size = size4;
            }
        }
        List<Integer> supportedPreviewFrameRates = this.mParams.getSupportedPreviewFrameRates();
        this.mParams.setPreviewSize(size.width, size.height);
        this.mParams.setPictureSize(size2.width, size2.height);
        List supportedFocusModes = this.mParams.getSupportedFocusModes();
        String str2 = AutoFocus.LEGACY_CONTINUOUS_VIDEO;
        if (supportedFocusModes.contains(str2)) {
            this.mParams.setFocusMode(str2);
        }
        int i2 = 30;
        int i3 = (i * 2) / 3;
        int i4 = (i * 4) / 3;
        int i5 = 0;
        for (Integer num : supportedPreviewFrameRates) {
            StringBuilder sb = new StringBuilder();
            sb.append("Supported frame rate: ");
            sb.append(num);
            VELogUtil.v(str, sb.toString());
            if (i5 < num.intValue()) {
                i5 = num.intValue();
            }
            if (i2 > num.intValue()) {
                i2 = num.intValue();
            }
        }
        int max = Math.max(i2, i3);
        int min = Math.min(i4, i5);
        StringBuilder sb2 = new StringBuilder();
        String str3 = "FpsRange = [";
        sb2.append(str3);
        sb2.append(max);
        String str4 = ", ";
        sb2.append(str4);
        sb2.append(min);
        String str5 = "]";
        sb2.append(str5);
        VELogUtil.i(str, sb2.toString());
        this.mParams.setPreviewFpsRange(max * 1000, min * 1000);
        int[] iArr = new int[2];
        this.mParams.getPreviewFpsRange(iArr);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str3);
        sb3.append(iArr[0] / 1000);
        sb3.append(str4);
        sb3.append(iArr[1] / 1000);
        sb3.append(str5);
        VELogUtil.i(str, sb3.toString());
        try {
            this.mCameraDevice.setParameters(this.mParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mParams = this.mCameraDevice.getParameters();
        Size pictureSize = this.mParams.getPictureSize();
        Size previewSize = this.mParams.getPreviewSize();
        this.mPreviewWidth = previewSize.width;
        this.mPreviewHeight = previewSize.height;
        int i6 = pictureSize.width;
        this.mPictureWidth = i6;
        this.mPictureHeight = pictureSize.height;
        VELogUtil.i(str, String.format("Camera Picture Size: %d x %d", new Object[]{Integer.valueOf(i6), Integer.valueOf(pictureSize.height)}));
        VELogUtil.i(str, String.format("Camera Preview Size: %d x %d", new Object[]{Integer.valueOf(previewSize.width), Integer.valueOf(previewSize.height)}));
    }

    public boolean isCameraOpened() {
        return this.mCameraDevice != null;
    }

    public boolean isPreviewing() {
        return this.mIsPreviewing;
    }

    public boolean isUsingFrontCamera() {
        return this.mFacing == 1;
    }

    public int pictureHeight() {
        return this.mPictureHeight;
    }

    public int pictureWidth() {
        return this.mPictureWidth;
    }

    public int previewHeight() {
        return this.mPreviewHeight;
    }

    public int previewWidth() {
        return this.mPreviewWidth;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setFocusMode(String str) {
        if (this.mCameraDevice != null) {
            this.mParams = this.mCameraDevice.getParameters();
            if (this.mParams.getSupportedFocusModes().contains(str)) {
                this.mParams.setFocusMode(str);
            }
        }
    }

    public int setOrientationDegrees(int i) {
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(this.mDefaultCameraID, cameraInfo);
        int i2 = cameraInfo.facing;
        int i3 = cameraInfo.orientation;
        int i4 = i2 == 1 ? ((360 - ((i3 + i) % m.cQ)) + 180) % m.cQ : ((i3 - i) + m.cQ) % m.cQ;
        try {
            this.mCameraDevice.setDisplayOrientation(i4);
        } catch (Exception unused) {
        }
        return i4;
    }

    public synchronized void setParams(Parameters parameters) {
        if (this.mCameraDevice != null) {
            this.mParams = parameters;
            this.mCameraDevice.setParameters(this.mParams);
        }
    }

    public synchronized void setPictureSize(int i, int i2, boolean z) {
        Size size;
        if (this.mCameraDevice == null) {
            this.mPictureWidth = i;
            this.mPictureHeight = i2;
            return;
        }
        this.mParams = this.mCameraDevice.getParameters();
        List<Size> supportedPictureSizes = this.mParams.getSupportedPictureSizes();
        Size size2 = null;
        if (z) {
            Collections.sort(supportedPictureSizes, this.comparatorBigger);
            for (Size size3 : supportedPictureSizes) {
                if (size2 == null || (size3.width >= i && size3.height >= i2)) {
                    size2 = size3;
                }
            }
        } else {
            Collections.sort(supportedPictureSizes, this.comparatorSmaller);
            for (Size size4 : supportedPictureSizes) {
                if (size2 == null || (size4.width <= i && size4.height <= i2)) {
                    size2 = size4;
                }
            }
        }
        if (size != null) {
            this.mPictureWidth = size.width;
        }
        if (size != null) {
            this.mPictureHeight = size.height;
        }
        try {
            this.mParams.setPictureSize(this.mPictureWidth, this.mPictureHeight);
            this.mCameraDevice.setParameters(this.mParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPreferPreviewSize(int i, int i2) {
        this.mPreferPreviewHeight = i;
        this.mPreferPreviewWidth = i2;
    }

    public synchronized void startPreview(SurfaceTexture surfaceTexture) {
        VELogUtil.i(TAG, "Camera startPreview...");
        if (this.mIsPreviewing) {
            VELogUtil.w(TAG, "Camera is previewing...");
        } else if (this.mCameraDevice != null) {
            try {
                this.mCameraDevice.setPreviewTexture(surfaceTexture);
                this.mCameraDevice.startPreview();
                this.mIsPreviewing = true;
            } catch (Exception e) {
                e.printStackTrace();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startPreview: Error ");
                sb.append(e.getMessage());
                VELogUtil.e(str, sb.toString());
                this.mIsPreviewing = false;
                try {
                    this.mCameraDevice.release();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                this.mCameraDevice = null;
            }
        }
    }

    public synchronized void stopCamera() {
        VELogUtil.i(TAG, "stopCamera...");
        if (this.mCameraDevice != null) {
            this.mIsPreviewing = false;
            this.mCameraDevice.stopPreview();
            try {
                this.mCameraDevice.setPreviewTexture(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mCameraDevice.release();
            this.mCameraDevice = null;
        }
    }

    public synchronized void stopPreview() {
        VELogUtil.d(TAG, "Camera stopPreview...");
        if (this.mIsPreviewing && this.mCameraDevice != null) {
            this.mIsPreviewing = false;
            this.mCameraDevice.stopPreview();
            VELogUtil.i(TAG, "Camera stopped!");
        }
    }

    public boolean tryOpenCamera(CameraOpenCallback cameraOpenCallback) {
        return tryOpenCamera(cameraOpenCallback, 0);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:35|36|37|38) */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0080, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r6.mCameraDevice.release();
        r6.mCameraDevice = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0089, code lost:
        return false;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0081 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean tryOpenCamera(CameraOpenCallback cameraOpenCallback, int i) {
        VELogUtil.i(TAG, "try open camera...");
        try {
            if (VERSION.SDK_INT > 8) {
                int numberOfCameras = Camera.getNumberOfCameras();
                CameraInfo cameraInfo = new CameraInfo();
                for (int i2 = 0; i2 < numberOfCameras; i2++) {
                    Camera.getCameraInfo(i2, cameraInfo);
                    if (cameraInfo.facing == i) {
                        this.mDefaultCameraID = i2;
                        this.mFacing = i;
                    }
                }
            }
            stopPreview();
            if (this.mCameraDevice != null) {
                this.mCameraDevice.release();
            }
            if (this.mDefaultCameraID >= 0) {
                this.mCameraDevice = Camera.open(this.mDefaultCameraID);
            } else {
                this.mCameraDevice = Camera.open();
                this.mFacing = 0;
            }
            this.mRotation = setOrientationDegrees(0);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Camera rotation = ");
            sb.append(this.mRotation);
            VELogUtil.d(str, sb.toString());
            if (this.mCameraDevice == null) {
                return false;
            }
            VELogUtil.i(TAG, "Camera opened!");
            initCamera(30);
            if (cameraOpenCallback != null) {
                cameraOpenCallback.cameraReady();
            }
        } catch (Exception e) {
            VELogUtil.e(TAG, "Open Camera Failed!");
            e.printStackTrace();
            this.mCameraDevice = null;
            return false;
        }
    }
}
