package com.arcsoft.supernight;

import android.graphics.Rect;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.CaptureResult.Key;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.media.Image;
import android.media.Image.Plane;
import android.os.Environment;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SuperNightProcess implements ProgressCallback {
    public static final int ARC_SN_CAMERA_MODE_UNKNOWN = -1;
    public static final int ARC_SN_CAMERA_MODE_XIAOMI_G80_GW1 = 1793;
    public static final int ARC_SN_CAMERA_MODE_XIAOMI_G90_GW1 = 1793;
    public static final int ARC_SN_CAMERA_MODE_XIAOMI_G90_GW1_INDIA = 1796;
    public static final int ARC_SN_CAMERA_MODE_XIAOMI_J15S_G85_S5KGM1_N = 1819;
    public static final int ARC_SN_CAMERA_MODE_XIAOMI_J15S_G85_S5KGM1_N_INDIA = 1820;
    public static final int ARC_SN_CAMERA_MODE_XIAOMI_SDM855_12MB_IMX586 = 1792;
    public static final int ARC_SN_CAMERA_STATE_HAND = 2;
    public static final int ARC_SN_CAMERA_STATE_UNKNOWN = 0;
    public static final int ARC_SN_MAX_INPUT_IMAGE_NUM = 20;
    public static final int ARC_SN_SCENEMODE_INDOOR = 1;
    public static final int ARC_SN_SCENEMODE_LOWLIGHT = 3;
    public static final int ARC_SN_SCENEMODE_OUTDOOR = 2;
    public static final int ARC_SN_SCENEMODE_PORTRAIT = 4;
    public static final int ARC_SN_SCENEMODE_UNKNOW = 0;
    public static final int ASVL_PAF_NV12 = 2049;
    public static final int ASVL_PAF_NV21 = 2050;
    public static final int ASVL_PAF_RAW10_BGGR_10B = 3332;
    public static final int ASVL_PAF_RAW10_BGGR_16B = 3340;
    public static final int ASVL_PAF_RAW10_BGGR_16B_LSB = 3428;
    public static final int ASVL_PAF_RAW10_BGGR_16B_MSB = 3412;
    public static final int ASVL_PAF_RAW10_GBRG_10B = 3331;
    public static final int ASVL_PAF_RAW10_GBRG_16B = 3339;
    public static final int ASVL_PAF_RAW10_GBRG_16B_LSB = 3427;
    public static final int ASVL_PAF_RAW10_GBRG_16B_MSB = 3411;
    public static final int ASVL_PAF_RAW10_GRAY_10B = 3585;
    public static final int ASVL_PAF_RAW10_GRAY_16B = 3713;
    public static final int ASVL_PAF_RAW10_GRBG_10B = 3330;
    public static final int ASVL_PAF_RAW10_GRBG_16B = 3338;
    public static final int ASVL_PAF_RAW10_GRBG_16B_LSB = 3426;
    public static final int ASVL_PAF_RAW10_GRBG_16B_MSB = 3410;
    public static final int ASVL_PAF_RAW10_RGGB_10B = 3329;
    public static final int ASVL_PAF_RAW10_RGGB_16B = 3337;
    public static final int ASVL_PAF_RAW10_RGGB_16B_LSB = 3425;
    public static final int ASVL_PAF_RAW10_RGGB_16B_MSB = 3409;
    public static final int ASVL_PAF_RAW12_BGGR_12B = 3336;
    public static final int ASVL_PAF_RAW12_BGGR_16B = 3348;
    public static final int ASVL_PAF_RAW12_BGGR_16B_LSB = 3433;
    public static final int ASVL_PAF_RAW12_BGGR_16B_MSB = 3417;
    public static final int ASVL_PAF_RAW12_GBRG_12B = 3335;
    public static final int ASVL_PAF_RAW12_GBRG_16B = 3347;
    public static final int ASVL_PAF_RAW12_GBRG_16B_LSB = 3432;
    public static final int ASVL_PAF_RAW12_GBRG_16B_MSB = 3416;
    public static final int ASVL_PAF_RAW12_GRAY_12B = 3601;
    public static final int ASVL_PAF_RAW12_GRAY_16B = 3729;
    public static final int ASVL_PAF_RAW12_GRBG_12B = 3334;
    public static final int ASVL_PAF_RAW12_GRBG_16B = 3346;
    public static final int ASVL_PAF_RAW12_GRBG_16B_LSB = 3431;
    public static final int ASVL_PAF_RAW12_GRBG_16B_MSB = 3415;
    public static final int ASVL_PAF_RAW12_RGGB_12B = 3333;
    public static final int ASVL_PAF_RAW12_RGGB_16B = 3345;
    public static final int ASVL_PAF_RAW12_RGGB_16B_LSB = 3430;
    public static final int ASVL_PAF_RAW12_RGGB_16B_MSB = 3414;
    public static final int ASVL_PAF_RAW14_BGGR_14B = 3384;
    public static final int ASVL_PAF_RAW14_BGGR_16B = 3396;
    public static final int ASVL_PAF_RAW14_BGGR_16B_LSB = 3437;
    public static final int ASVL_PAF_RAW14_BGGR_16B_MSB = 3421;
    public static final int ASVL_PAF_RAW14_GBRG_14B = 3383;
    public static final int ASVL_PAF_RAW14_GBRG_16B = 3395;
    public static final int ASVL_PAF_RAW14_GBRG_16B_LSB = 3436;
    public static final int ASVL_PAF_RAW14_GBRG_16B_MSB = 3420;
    public static final int ASVL_PAF_RAW14_GRAY_14B = 3617;
    public static final int ASVL_PAF_RAW14_GRAY_16B = 3745;
    public static final int ASVL_PAF_RAW14_GRBG_14B = 3382;
    public static final int ASVL_PAF_RAW14_GRBG_16B = 3394;
    public static final int ASVL_PAF_RAW14_GRBG_16B_LSB = 3435;
    public static final int ASVL_PAF_RAW14_GRBG_16B_MSB = 3419;
    public static final int ASVL_PAF_RAW14_RGGB_14B = 3381;
    public static final int ASVL_PAF_RAW14_RGGB_16B = 3393;
    public static final int ASVL_PAF_RAW14_RGGB_16B_LSB = 3434;
    public static final int ASVL_PAF_RAW14_RGGB_16B_MSB = 3418;
    public static final int ASVL_PAF_RAW16_BGGR_16B = 3364;
    public static final int ASVL_PAF_RAW16_GBRG_16B = 3363;
    public static final int ASVL_PAF_RAW16_GRAY_16B = 3633;
    public static final int ASVL_PAF_RAW16_GRBG_16B = 3362;
    public static final int ASVL_PAF_RAW16_RGGB_16B = 3361;
    private static final String DEBUG_FILE;
    private static final String DUMP_KEY = "dumpSNImage";
    private static final String HINT_FOR_RAW_REPROCESS_KEY = "com.mediatek.control.capture.hintForRawReprocess";
    private static final String LOG_KEY = "debugSNLog";
    private static final String SUPPERNIGHT_ADRCGAIN_KEY = "com.mediatek.suppernightfeature.fadrcgain";
    private static final String SUPPERNIGHT_BLACKLEVEL_KEY = "com.mediatek.suppernightfeature.blacklevel";
    private static final String SUPPERNIGHT_BRIGHTLEVEL_KEY = "com.mediatek.suppernightfeature.brightlevel";
    private static final String SUPPERNIGHT_EXPINDEX_KEY = "com.mediatek.suppernightfeature.expindex";
    private static final String SUPPERNIGHT_ISPGAIN_KEY = "com.mediatek.suppernightfeature.fispgain";
    private static final String SUPPERNIGHT_LUXINDEX_KEY = "com.mediatek.suppernightfeature.luxindex";
    private static final String SUPPERNIGHT_SENSORGAIN_KEY = "com.mediatek.suppernightfeature.fsensorgain";
    private static final String SUPPERNIGHT_SHUTTER_KEY = "com.mediatek.suppernightfeature.fshutter";
    private static final String SUPPERNIGHT_TOTALGAIN_KEY = "com.mediatek.suppernightfeature.ftotalgain";
    private static final String SUPPERNIGHT_WBGAIN_KEY = "com.mediatek.suppernightfeature.fwbgain";
    private static final String TAG = "SuperNightProcess";
    private Key ADRC_GAIN_RESULT_KEY = null;
    private Key BLACK_LEVEL_RESULT_KEY = null;
    private Key BRIGHT_LEVEL_RESULT_KEY = null;
    private Key EXP_INDEX_RESULT_KEY = null;
    private Key ISP_GAIN_RESULT_KEY = null;
    private Key LUX_INDEX_RESULT_KEY = null;
    private Key SENSOR_GAIN_RESULT_KEY = null;
    private Key SHUTTER_RESULT_KEY = null;
    private Key TOTAL_GAIN_RESULT_KEY = null;
    private Key WB_GAIN_RESULT_KEY = null;
    private Rect mArrayRect = null;
    private CountDownLatch mCountDownLatch;
    private boolean mDumpFile = false;
    private boolean mEnableAdrcGain = true;
    private boolean mEnableBlackLevel = true;
    private boolean mEnableWbGain = true;
    private int mFaceOrientation = 90;
    private volatile boolean mInit = false;
    private volatile boolean mIsCancel = false;
    private TotalCaptureResult mMetdata;
    private SuperNightJni mSuperNightJni = new SuperNightJni();

    public class FaceInfo {
        public int faceNum;
        public int faceOrientation;
        public Rect[] faceRects;

        public FaceInfo() {
        }
    }

    public class InputInfo {
        public int cameraState;
        public int curIndex;
        public int imgNum;
        public int[] inputFd = new int[20];
        public RawImage[] inputImages = new RawImage[20];
        public float[] inputImagesEV = new float[20];

        public InputInfo() {
        }
    }

    public class Param {
        public int curveBrightness;
        public int curveContrast;
        public int curveHighlight;
        public int curveMid;
        public int curveShadow;
        public int edgeSharpIntensity;
        public int noiseLength;
        public int sharpIntensity;

        public Param() {
        }
    }

    public class RawInfo {
        public int[] blackLevel = new int[4];
        public int[] brightLevel = new int[4];
        public int[] evList = new int[20];
        public int expIndex;
        public float fAdrcGain;
        public float fISPGain;
        public float fSensorGain;
        public float fShutter;
        public float fTotalGain;
        public float[] fWbGain = new float[4];
        public int luxIndex;
        public int rawType;

        public RawInfo() {
            for (int i = 0; i < 4; i++) {
                this.fWbGain[i] = 1.0f;
            }
            this.fAdrcGain = 1.0f;
        }
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().toString());
        sb.append("/DCIM/arc_super_night/dump_file.txt");
        DEBUG_FILE = sb.toString();
    }

    public SuperNightProcess(Rect rect) {
        readDebugFileValue();
        this.mSuperNightJni.setDumpImageFile(this.mDumpFile);
        this.mArrayRect = rect;
        StringBuilder sb = new StringBuilder();
        sb.append("dumpFile = ");
        sb.append(this.mDumpFile);
        sb.append(" debugLog = ");
        sb.append(LOG.DEBUG);
        String sb2 = sb.toString();
        String str = TAG;
        LOG.d(str, sb2);
        StringBuilder sb3 = new StringBuilder();
        String str2 = "mArrayRect = ";
        sb3.append(str2);
        sb3.append(this.mArrayRect);
        LOG.d(str, sb3.toString());
        if (this.mArrayRect != null) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(this.mArrayRect.toString());
            LOG.d(str, sb4.toString());
        }
        LOG.d("Version", "--01/18--");
    }

    private void conversionCropRect(Rect rect, int i, int i2) {
        if (rect != null) {
            Rect rect2 = this.mArrayRect;
            if (rect2 != null && i > 0 && i2 > 0) {
                float width = ((float) rect2.width()) / ((float) i);
                float height = ((float) this.mArrayRect.height()) / ((float) i2);
                StringBuilder sb = new StringBuilder();
                sb.append("fMultipleW = ");
                sb.append(width);
                sb.append(", fMultipleH = ");
                sb.append(height);
                String sb2 = sb.toString();
                String str = TAG;
                LOG.d(str, sb2);
                rect.left = (int) (((float) rect.left) * width);
                rect.top = (int) (((float) rect.top) * height);
                Rect rect3 = this.mArrayRect;
                rect.right = rect3.right - ((int) (((float) (i - rect.right)) * width));
                rect.bottom = rect3.bottom - ((int) (((float) (i2 - rect.bottom)) * height));
                int i3 = rect.left;
                if (i3 % 2 != 0) {
                    rect.left = i3 + 1;
                }
                int i4 = rect.top;
                if (i4 % 2 != 0) {
                    rect.top = i4 + 1;
                }
                int i5 = rect.right;
                if (i5 % 2 != 0) {
                    rect.right = i5 - 1;
                }
                int i6 = rect.bottom;
                if (i6 % 2 != 0) {
                    rect.bottom = i6 - 1;
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("conversionCropRect -> cropRect = ");
                sb3.append(rect.toString());
                LOG.d(str, sb3.toString());
            }
        }
    }

    private FaceInfo getFaceInfo(TotalCaptureResult totalCaptureResult, int i, int i2) {
        FaceInfo faceInfo;
        if (totalCaptureResult == null || this.mArrayRect == null || i <= 0 || i2 <= 0) {
            return null;
        }
        Face[] faceArr = (Face[]) totalCaptureResult.get(CaptureResult.STATISTICS_FACES);
        String str = TAG;
        if (faceArr != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("face length = ");
            sb.append(faceArr.length);
            LOG.d(str, sb.toString());
        }
        if (faceArr == null || faceArr.length <= 0) {
            faceInfo = null;
        } else {
            float width = ((float) this.mArrayRect.width()) / ((float) i);
            float height = ((float) this.mArrayRect.height()) / ((float) i2);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("fMultipleW = ");
            sb2.append(width);
            sb2.append(", fMultipleH = ");
            sb2.append(height);
            LOG.d(str, sb2.toString());
            faceInfo = new FaceInfo();
            faceInfo.faceRects = new Rect[faceArr.length];
            faceInfo.faceNum = faceArr.length;
            faceInfo.faceOrientation = this.mFaceOrientation;
            for (int i3 = 0; i3 < faceArr.length; i3++) {
                faceInfo.faceRects[i3] = new Rect(faceArr[i3].getBounds());
                Rect[] rectArr = faceInfo.faceRects;
                rectArr[i3].left = (int) (((float) rectArr[i3].left) / width);
                rectArr[i3].top = (int) (((float) rectArr[i3].top) / height);
                rectArr[i3].right = (int) (((float) rectArr[i3].right) / width);
                rectArr[i3].bottom = (int) (((float) rectArr[i3].bottom) / height);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("conversionFaceRect -> faceRect = ");
                sb3.append(faceInfo.faceRects[i3].toString());
                LOG.d(str, sb3.toString());
            }
        }
        return faceInfo;
    }

    private RawImage getRawImage(Image image, int i) {
        RawImage rawImage = new RawImage();
        int format = image.getFormat();
        rawImage.mWidth = image.getWidth();
        rawImage.mHeight = image.getHeight();
        Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        buffer.rewind();
        rawImage.mPitch1 = 0;
        rawImage.mPlane1 = null;
        if (format == 35) {
            rawImage.mPixelArrayFormat = 2050;
            ByteBuffer buffer2 = planes[2].getBuffer();
            buffer2.rewind();
            rawImage.mPitch1 = planes[2].getRowStride();
            rawImage.mPlane1 = buffer2;
        } else if (format == 32) {
            rawImage.mPixelArrayFormat = i;
        }
        rawImage.mPitch0 = planes[0].getRowStride();
        rawImage.mPitch2 = 0;
        rawImage.mPitch3 = 0;
        rawImage.mPlane0 = buffer;
        rawImage.mPlane2 = null;
        rawImage.mPlane3 = null;
        return rawImage;
    }

    private void getVendorTagValue(TotalCaptureResult totalCaptureResult, RawInfo rawInfo) {
        Key key = this.BRIGHT_LEVEL_RESULT_KEY;
        String str = TAG;
        String str2 = "vendorTag";
        String str3 = "] = ";
        if (key != null) {
            int[] iArr = (int[]) totalCaptureResult.get(key);
            if (iArr != null && iArr.length > 0) {
                if (this.mEnableBlackLevel) {
                    int i = 0;
                    while (true) {
                        int[] iArr2 = rawInfo.brightLevel;
                        if (i >= iArr2.length) {
                            break;
                        }
                        iArr2[i] = iArr[0] / 1024;
                        StringBuilder sb = new StringBuilder();
                        sb.append("brightLevel[");
                        sb.append(i);
                        sb.append(str3);
                        sb.append(rawInfo.brightLevel[i]);
                        LOG.d(str, sb.toString());
                        i++;
                    }
                }
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("bright[");
                    sb2.append(i2);
                    sb2.append(str3);
                    sb2.append(iArr[i2]);
                    LOG.d(str2, sb2.toString());
                }
            }
        }
        Key key2 = this.BLACK_LEVEL_RESULT_KEY;
        if (key2 != null) {
            int[] iArr3 = (int[]) totalCaptureResult.get(key2);
            if (iArr3 != null && iArr3.length > 0) {
                if (this.mEnableBlackLevel) {
                    int i3 = 0;
                    while (true) {
                        int[] iArr4 = rawInfo.blackLevel;
                        if (i3 >= iArr4.length) {
                            break;
                        }
                        iArr4[i3] = iArr3[0] / 1024;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("blackLevel[");
                        sb3.append(i3);
                        sb3.append(str3);
                        sb3.append(rawInfo.blackLevel[i3]);
                        LOG.d(str, sb3.toString());
                        i3++;
                    }
                }
                for (int i4 = 0; i4 < iArr3.length; i4++) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("black[");
                    sb4.append(i4);
                    sb4.append(str3);
                    sb4.append(iArr3[i4]);
                    LOG.d(str2, sb4.toString());
                }
            }
        }
        Key key3 = this.WB_GAIN_RESULT_KEY;
        if (key3 != null) {
            float[] fArr = (float[]) totalCaptureResult.get(key3);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("awbGain = ");
            sb5.append(fArr);
            LOG.d(str2, sb5.toString());
            if (fArr != null && fArr.length > 0) {
                if (this.mEnableWbGain) {
                    float[] fArr2 = rawInfo.fWbGain;
                    fArr2[0] = fArr[0];
                    fArr2[1] = fArr[1];
                    fArr2[2] = fArr[2];
                    fArr2[3] = fArr[3];
                }
                for (int i5 = 0; i5 < fArr.length; i5++) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("wbGain[");
                    sb6.append(i5);
                    sb6.append(str3);
                    sb6.append(fArr[i5]);
                    LOG.d(str2, sb6.toString());
                }
            }
        }
        Key key4 = this.SHUTTER_RESULT_KEY;
        if (key4 != null) {
            long[] jArr = (long[]) totalCaptureResult.get(key4);
            if (jArr != null && jArr.length > 0) {
                rawInfo.fShutter = (float) jArr[0];
                StringBuilder sb7 = new StringBuilder();
                sb7.append("fShutter = ");
                sb7.append(rawInfo.fShutter);
                LOG.d(str, sb7.toString());
                for (int i6 = 0; i6 < jArr.length; i6++) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("shutter[");
                    sb8.append(i6);
                    sb8.append(str3);
                    sb8.append(jArr[i6]);
                    LOG.d(str2, sb8.toString());
                }
            }
        }
        Key key5 = this.SENSOR_GAIN_RESULT_KEY;
        if (key5 != null) {
            int[] iArr5 = (int[]) totalCaptureResult.get(key5);
            if (iArr5 != null && iArr5.length > 0) {
                rawInfo.fSensorGain = ((float) iArr5[0]) / 1024.0f;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("fSensorGain = ");
                sb9.append(rawInfo.fSensorGain);
                LOG.d(str, sb9.toString());
                for (int i7 = 0; i7 < iArr5.length; i7++) {
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("sensorGain[");
                    sb10.append(i7);
                    sb10.append(str3);
                    sb10.append(iArr5[i7]);
                    LOG.d(str2, sb10.toString());
                }
            }
        }
        Key key6 = this.ISP_GAIN_RESULT_KEY;
        if (key6 != null) {
            int[] iArr6 = (int[]) totalCaptureResult.get(key6);
            if (iArr6 != null && iArr6.length > 0) {
                rawInfo.fISPGain = ((float) iArr6[0]) / 1024.0f;
                StringBuilder sb11 = new StringBuilder();
                sb11.append("fISPGain = ");
                sb11.append(rawInfo.fISPGain);
                LOG.d(str, sb11.toString());
                for (int i8 = 0; i8 < iArr6.length; i8++) {
                    StringBuilder sb12 = new StringBuilder();
                    sb12.append("ispGain[");
                    sb12.append(i8);
                    sb12.append(str3);
                    sb12.append(iArr6[i8]);
                    LOG.d(str2, sb12.toString());
                }
            }
        }
        Key key7 = this.LUX_INDEX_RESULT_KEY;
        if (key7 != null) {
            int[] iArr7 = (int[]) totalCaptureResult.get(key7);
            if (iArr7 != null && iArr7.length > 0) {
                rawInfo.luxIndex = iArr7[0] / 10000;
                StringBuilder sb13 = new StringBuilder();
                sb13.append("luxIndex = ");
                sb13.append(rawInfo.luxIndex);
                LOG.d(str, sb13.toString());
                for (int i9 = 0; i9 < iArr7.length; i9++) {
                    StringBuilder sb14 = new StringBuilder();
                    sb14.append("luxIndex[");
                    sb14.append(i9);
                    sb14.append(str3);
                    sb14.append(iArr7[i9]);
                    LOG.d(str2, sb14.toString());
                }
            }
        }
        Key key8 = this.EXP_INDEX_RESULT_KEY;
        if (key8 != null) {
            int[] iArr8 = (int[]) totalCaptureResult.get(key8);
            if (iArr8 != null && iArr8.length > 0) {
                rawInfo.expIndex = iArr8[0];
                StringBuilder sb15 = new StringBuilder();
                sb15.append("expIndex = ");
                sb15.append(rawInfo.expIndex);
                LOG.d(str, sb15.toString());
                for (int i10 = 0; i10 < iArr8.length; i10++) {
                    StringBuilder sb16 = new StringBuilder();
                    sb16.append("expIndex[");
                    sb16.append(i10);
                    sb16.append(str3);
                    sb16.append(iArr8[i10]);
                    LOG.d(str2, sb16.toString());
                }
            }
        }
        Key key9 = this.ADRC_GAIN_RESULT_KEY;
        if (key9 != null) {
            int[] iArr9 = (int[]) totalCaptureResult.get(key9);
            if (iArr9 != null && iArr9.length > 0) {
                if (this.mEnableAdrcGain) {
                    rawInfo.fAdrcGain = ((float) iArr9[0]) / 1000.0f;
                    StringBuilder sb17 = new StringBuilder();
                    sb17.append("fAdrcGain = ");
                    sb17.append(rawInfo.fAdrcGain);
                    LOG.d(str, sb17.toString());
                }
                for (int i11 = 0; i11 < iArr9.length; i11++) {
                    StringBuilder sb18 = new StringBuilder();
                    sb18.append("adrcGain[");
                    sb18.append(i11);
                    sb18.append(str3);
                    sb18.append(iArr9[i11]);
                    LOG.d(str2, sb18.toString());
                }
            }
        }
        Key key10 = this.TOTAL_GAIN_RESULT_KEY;
        if (key10 != null) {
            int[] iArr10 = (int[]) totalCaptureResult.get(key10);
            if (iArr10 != null && iArr10.length > 0) {
                rawInfo.fTotalGain = ((float) iArr10[0]) / 100.0f;
                StringBuilder sb19 = new StringBuilder();
                sb19.append("fTotalGain = ");
                sb19.append(rawInfo.fTotalGain);
                LOG.d(str, sb19.toString());
                for (int i12 = 0; i12 < iArr10.length; i12++) {
                    StringBuilder sb20 = new StringBuilder();
                    sb20.append("totalGain[");
                    sb20.append(i12);
                    sb20.append(str3);
                    sb20.append(iArr10[i12]);
                    LOG.d(str2, sb20.toString());
                }
            }
        }
    }

    private boolean setupSomeVendorTag(TotalCaptureResult totalCaptureResult) {
        String str = TAG;
        LOG.d(str, "setupSomeVendorTag");
        if (this.BRIGHT_LEVEL_RESULT_KEY != null && this.BLACK_LEVEL_RESULT_KEY != null && this.WB_GAIN_RESULT_KEY != null && this.SHUTTER_RESULT_KEY != null && this.SENSOR_GAIN_RESULT_KEY != null && this.ISP_GAIN_RESULT_KEY != null && this.LUX_INDEX_RESULT_KEY != null && this.ADRC_GAIN_RESULT_KEY != null && this.TOTAL_GAIN_RESULT_KEY != null && this.EXP_INDEX_RESULT_KEY != null) {
            return true;
        }
        List<Key> keys = totalCaptureResult.getKeys();
        if (keys == null || keys.size() <= 0) {
            LOG.d(str, "List<CaptureResult.Key<?>> is error");
            if (keys != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("List<CaptureResult.Key<?>> lenth = ");
                sb.append(keys.size());
                LOG.d(str, sb.toString());
            }
            return false;
        }
        for (Key key : keys) {
            if (SUPPERNIGHT_BRIGHTLEVEL_KEY.equals(key.getName())) {
                LOG.d(str, "BRIGHT_LEVEL_RESULT_KEY");
                this.BRIGHT_LEVEL_RESULT_KEY = key;
            }
            if (SUPPERNIGHT_BLACKLEVEL_KEY.equals(key.getName())) {
                LOG.d(str, "BLACK_LEVEL_RESULT_KEY");
                this.BLACK_LEVEL_RESULT_KEY = key;
            }
            if (SUPPERNIGHT_WBGAIN_KEY.equals(key.getName())) {
                LOG.d(str, "WB_GAIN_RESULT_KEY");
                this.WB_GAIN_RESULT_KEY = key;
            }
            if (SUPPERNIGHT_SHUTTER_KEY.equals(key.getName())) {
                LOG.d(str, "SHUTTER_RESULT_KEY");
                this.SHUTTER_RESULT_KEY = key;
            }
            if (SUPPERNIGHT_SENSORGAIN_KEY.equals(key.getName())) {
                LOG.d(str, "SENSOR_GAIN_RESULT_KEY");
                this.SENSOR_GAIN_RESULT_KEY = key;
            }
            if (SUPPERNIGHT_ISPGAIN_KEY.equals(key.getName())) {
                LOG.d(str, "ISP_GAIN_RESULT_KEY");
                this.ISP_GAIN_RESULT_KEY = key;
            }
            if (SUPPERNIGHT_LUXINDEX_KEY.equals(key.getName())) {
                LOG.d(str, "LUX_INDEX_RESULT_KEY");
                this.LUX_INDEX_RESULT_KEY = key;
            }
            if (SUPPERNIGHT_EXPINDEX_KEY.equals(key.getName())) {
                LOG.d(str, "EXP_INDEX_RESULT_KEY");
                this.EXP_INDEX_RESULT_KEY = key;
            }
            if (SUPPERNIGHT_ADRCGAIN_KEY.equals(key.getName())) {
                LOG.d(str, "ADRC_GAIN_RESULT_KEY");
                this.ADRC_GAIN_RESULT_KEY = key;
            }
            if (SUPPERNIGHT_TOTALGAIN_KEY.equals(key.getName())) {
                LOG.d(str, "TOTAL_GAIN_RESULT_KEY");
                this.TOTAL_GAIN_RESULT_KEY = key;
            }
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:75:0x019a, code lost:
        if (r7.mCountDownLatch != null) goto L_0x019c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x019c, code lost:
        com.arcsoft.supernight.LOG.d(r9, r8);
        r7.mCountDownLatch.countDown();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x01c5, code lost:
        if (r7.mCountDownLatch != null) goto L_0x019c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x01c8, code lost:
        return r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int addAllInputInfo(ArrayList arrayList, ArrayList arrayList2, int i, Image image, Rect rect) {
        ArrayList arrayList3 = arrayList;
        ArrayList arrayList4 = arrayList2;
        int i2 = i;
        Image image2 = image;
        String str = "mCountDownLatch.countDown() 0";
        String str2 = TAG;
        LOG.d(str2, "-- addAllInputInfoEx --");
        int i3 = -1;
        if (arrayList3 == null || arrayList.size() <= 0 || arrayList4 == null || arrayList2.size() <= 0 || arrayList.size() != arrayList2.size() || this.mSuperNightJni == null || image2 == null || rect == null) {
            LOG.d(str2, "addAllInputInfo - > error invalid param");
            if (this.mCountDownLatch != null) {
                LOG.d(str2, "mCountDownLatch.countDown() 1");
                this.mCountDownLatch.countDown();
            }
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" imageList size =  ");
        sb.append(arrayList.size());
        LOG.d(str2, sb.toString());
        int size = arrayList.size();
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i4 < arrayList.size()) {
            try {
                if (this.mIsCancel) {
                    try {
                        LOG.d(str2, "is cancel 0");
                        if (this.mCountDownLatch != null) {
                            LOG.d(str2, str);
                            this.mCountDownLatch.countDown();
                        }
                        return i3;
                    } catch (IllegalStateException e) {
                        e = e;
                        String str3 = "Error";
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("e-> ");
                            sb2.append(e.toString());
                            LOG.d(str3, sb2.toString());
                        } catch (Throwable th) {
                            if (this.mCountDownLatch != null) {
                                LOG.d(str2, str);
                                this.mCountDownLatch.countDown();
                            }
                            throw th;
                        }
                    }
                } else {
                    Image image3 = (Image) arrayList3.get(i4);
                    TotalCaptureResult totalCaptureResult = (TotalCaptureResult) arrayList4.get(i4);
                    if (image3 != null) {
                        if (totalCaptureResult != null) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(" ------ ");
                            sb3.append(i4);
                            LOG.d("vendorTag", sb3.toString());
                            if (!setupSomeVendorTag(totalCaptureResult)) {
                                LOG.d(str2, "setupSomeVendorTag is error!!");
                                if (this.mCountDownLatch != null) {
                                    LOG.d(str2, str);
                                    this.mCountDownLatch.countDown();
                                }
                                return i3;
                            }
                            InputInfo inputInfo = new InputInfo();
                            inputInfo.curIndex = i4;
                            inputInfo.imgNum = size;
                            inputInfo.cameraState = 2;
                            int width = image3.getWidth();
                            int height = image3.getHeight();
                            RawInfo rawInfo = new RawInfo();
                            rawInfo.rawType = 0;
                            getVendorTagValue(totalCaptureResult, rawInfo);
                            Integer num = (Integer) totalCaptureResult.get(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION);
                            if (num != null) {
                                rawInfo.evList[0] = num.intValue();
                                inputInfo.inputImagesEV[0] = (float) num.intValue();
                            }
                            inputInfo.inputImages[0] = getRawImage(image3, i2);
                            this.mSuperNightJni.addOneInputInfo(rawInfo, inputInfo);
                            i4++;
                            i5 = width;
                            i6 = height;
                            i3 = -1;
                        }
                    }
                    LOG.d(str2, "TotalCaptureResult - > error invalid param");
                    if (this.mCountDownLatch != null) {
                        LOG.d(str2, str);
                        this.mCountDownLatch.countDown();
                    }
                    return -1;
                }
            } catch (IllegalStateException e2) {
                e = e2;
                i3 = -1;
                String str32 = "Error";
                StringBuilder sb22 = new StringBuilder();
                sb22.append("e-> ");
                sb22.append(e.toString());
                LOG.d(str32, sb22.toString());
            }
        }
        FaceInfo faceInfo = null;
        Iterator it = arrayList2.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            TotalCaptureResult totalCaptureResult2 = (TotalCaptureResult) it.next();
            Integer num2 = (Integer) totalCaptureResult2.get(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION);
            if (num2 != null && num2.intValue() == 0) {
                faceInfo = getFaceInfo(totalCaptureResult2, i5, i6);
                break;
            }
        }
        if (this.mIsCancel) {
            LOG.d(str2, "is cancel 1");
            if (this.mCountDownLatch != null) {
                LOG.d(str2, str);
                this.mCountDownLatch.countDown();
            }
            return -1;
        }
        InputInfo inputInfo2 = new InputInfo();
        inputInfo2.curIndex = 0;
        inputInfo2.imgNum = size;
        inputInfo2.cameraState = 2;
        inputInfo2.inputImages[0] = getRawImage(image2, i2);
        i3 = this.mSuperNightJni.process(faceInfo, inputInfo2, 3, rect, this);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("process = ");
        sb4.append(i3);
        sb4.append(", cropRect0 = ");
        sb4.append(rect.toString());
        LOG.d(str2, sb4.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:72:0x018e, code lost:
        if (r7.mCountDownLatch != null) goto L_0x0190;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0190, code lost:
        com.arcsoft.supernight.LOG.d(r10, r8);
        r7.mCountDownLatch.countDown();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x01bb, code lost:
        if (r7.mCountDownLatch != null) goto L_0x0190;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x01be, code lost:
        return r9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int addAllInputInfoEx(ArrayList arrayList, ArrayList arrayList2, int i, Rect rect) {
        ArrayList arrayList3 = arrayList;
        ArrayList arrayList4 = arrayList2;
        String str = "mCountDownLatch.countDown() 2";
        int i2 = -1;
        String str2 = TAG;
        if (arrayList3 == null || arrayList.size() <= 0 || arrayList4 == null || arrayList2.size() <= 0 || arrayList.size() != arrayList2.size() || this.mSuperNightJni == null || rect == null) {
            LOG.d(str2, "addAllInputInfo - > error invalid param");
            if (this.mCountDownLatch != null) {
                LOG.d(str2, "mCountDownLatch.countDown() 3");
                this.mCountDownLatch.countDown();
            }
            return -1;
        }
        InputInfo inputInfo = new InputInfo();
        StringBuilder sb = new StringBuilder();
        sb.append(" imageList size =  ");
        sb.append(arrayList.size());
        LOG.d(str2, sb.toString());
        int size = arrayList.size();
        InputInfo inputInfo2 = inputInfo;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < arrayList.size()) {
            try {
                if (this.mIsCancel) {
                    if (this.mCountDownLatch != null) {
                        LOG.d(str2, str);
                        this.mCountDownLatch.countDown();
                    }
                    return i2;
                }
                Image image = (Image) arrayList3.get(i3);
                TotalCaptureResult totalCaptureResult = (TotalCaptureResult) arrayList4.get(i3);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Image format - > ");
                sb2.append(image.getFormat());
                LOG.d(str2, sb2.toString());
                if (image != null) {
                    if (totalCaptureResult != null) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(" ------ ");
                        sb3.append(i3);
                        LOG.d("vendorTag", sb3.toString());
                        if (!setupSomeVendorTag(totalCaptureResult)) {
                            try {
                                LOG.d(str2, "setupSomeVendorTag is error!!");
                                if (this.mCountDownLatch != null) {
                                    LOG.d(str2, str);
                                    this.mCountDownLatch.countDown();
                                }
                                return i2;
                            } catch (IllegalStateException e) {
                                e = e;
                                String str3 = "Error";
                                try {
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("e-> ");
                                    sb4.append(e.toString());
                                    LOG.d(str3, sb4.toString());
                                } catch (Throwable th) {
                                    if (this.mCountDownLatch != null) {
                                        LOG.d(str2, str);
                                        this.mCountDownLatch.countDown();
                                    }
                                    throw th;
                                }
                            }
                        } else {
                            InputInfo inputInfo3 = new InputInfo();
                            inputInfo3.curIndex = i3;
                            inputInfo3.imgNum = size;
                            inputInfo3.cameraState = 2;
                            int width = image.getWidth();
                            int height = image.getHeight();
                            RawInfo rawInfo = new RawInfo();
                            rawInfo.rawType = 0;
                            getVendorTagValue(totalCaptureResult, rawInfo);
                            Integer num = (Integer) totalCaptureResult.get(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION);
                            if (num != null) {
                                rawInfo.evList[0] = num.intValue();
                                inputInfo3.inputImagesEV[0] = (float) num.intValue();
                            }
                            inputInfo3.inputImages[0] = getRawImage(image, i);
                            this.mSuperNightJni.addOneInputInfo(rawInfo, inputInfo3);
                            if (i3 == 0) {
                                inputInfo2 = inputInfo3;
                            }
                            i3++;
                            i4 = width;
                            i5 = height;
                            i2 = -1;
                        }
                    }
                }
                LOG.d(str2, "TotalCaptureResult - > error invalid param");
                if (this.mCountDownLatch != null) {
                    LOG.d(str2, str);
                    this.mCountDownLatch.countDown();
                }
                return -1;
            } catch (IllegalStateException e2) {
                e = e2;
                i2 = -1;
                String str32 = "Error";
                StringBuilder sb42 = new StringBuilder();
                sb42.append("e-> ");
                sb42.append(e.toString());
                LOG.d(str32, sb42.toString());
            }
        }
        FaceInfo faceInfo = null;
        Iterator it = arrayList2.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            TotalCaptureResult totalCaptureResult2 = (TotalCaptureResult) it.next();
            Integer num2 = (Integer) totalCaptureResult2.get(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION);
            if (num2 != null && num2.intValue() == 0) {
                faceInfo = getFaceInfo(totalCaptureResult2, i4, i5);
                break;
            }
        }
        FaceInfo faceInfo2 = faceInfo;
        if (this.mIsCancel) {
            if (this.mCountDownLatch != null) {
                LOG.d(str2, str);
                this.mCountDownLatch.countDown();
            }
            return -1;
        }
        i2 = this.mSuperNightJni.process(faceInfo2, inputInfo2, 3, rect, this);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("process = ");
        sb5.append(i2);
        sb5.append(", cropRect0 = ");
        sb5.append(rect.toString());
        LOG.d(str2, sb5.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:85:0x01ef, code lost:
        if (r1.mCountDownLatch != null) goto L_0x01f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01f1, code lost:
        com.arcsoft.supernight.LOG.d(r7, r6);
        r1.mCountDownLatch.countDown();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x021a, code lost:
        if (r1.mCountDownLatch != null) goto L_0x01f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x021d, code lost:
        return r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int addAllInputInfo_Fd(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, int i, Image image, int i2, Rect rect) {
        ArrayList arrayList4 = arrayList;
        ArrayList arrayList5 = arrayList2;
        ArrayList arrayList6 = arrayList3;
        int i3 = i;
        Image image2 = image;
        String str = "mCountDownLatch.countDown() 0";
        String str2 = TAG;
        LOG.d(str2, "-- addAllInputInfo by fd --");
        int i4 = -1;
        if (arrayList4 == null || arrayList.size() <= 0 || arrayList5 == null || arrayList2.size() <= 0 || arrayList6 == null || arrayList3.size() <= 0 || arrayList.size() != arrayList2.size() || arrayList.size() != arrayList3.size() || this.mSuperNightJni == null || image2 == null || rect == null) {
            LOG.d(str2, "addAllInputInfo - > error invalid param");
            if (this.mCountDownLatch != null) {
                LOG.d(str2, "mCountDownLatch.countDown() 1");
                this.mCountDownLatch.countDown();
            }
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" imageList size =  ");
        sb.append(arrayList.size());
        LOG.d(str2, sb.toString());
        int size = arrayList.size();
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (i5 < arrayList.size()) {
            try {
                if (this.mIsCancel) {
                    try {
                        LOG.d(str2, "is cancel 0");
                        if (this.mCountDownLatch != null) {
                            LOG.d(str2, str);
                            this.mCountDownLatch.countDown();
                        }
                        return i4;
                    } catch (IllegalStateException e) {
                        e = e;
                        String str3 = "Error";
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("e-> ");
                            sb2.append(e.toString());
                            LOG.d(str3, sb2.toString());
                        } catch (Throwable th) {
                            if (this.mCountDownLatch != null) {
                                LOG.d(str2, str);
                                this.mCountDownLatch.countDown();
                            }
                            throw th;
                        }
                    }
                } else {
                    Image image3 = (Image) arrayList4.get(i5);
                    TotalCaptureResult totalCaptureResult = (TotalCaptureResult) arrayList5.get(i5);
                    if (image3 != null) {
                        if (totalCaptureResult != null) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(" ------ ");
                            sb3.append(i5);
                            LOG.d("vendorTag", sb3.toString());
                            if (!setupSomeVendorTag(totalCaptureResult)) {
                                LOG.d(str2, "setupSomeVendorTag is error!!");
                                if (this.mCountDownLatch != null) {
                                    LOG.d(str2, str);
                                    this.mCountDownLatch.countDown();
                                }
                                return i4;
                            }
                            InputInfo inputInfo = new InputInfo();
                            inputInfo.curIndex = i5;
                            inputInfo.imgNum = size;
                            inputInfo.cameraState = 2;
                            Integer num = (Integer) arrayList6.get(i5);
                            if (num != null) {
                                inputInfo.inputFd[0] = num.intValue();
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("input fd[");
                                sb4.append(i5);
                                sb4.append("] = ");
                                sb4.append(inputInfo.inputFd[0]);
                                LOG.d(str2, sb4.toString());
                            }
                            int width = image3.getWidth();
                            int height = image3.getHeight();
                            RawInfo rawInfo = new RawInfo();
                            rawInfo.rawType = 0;
                            getVendorTagValue(totalCaptureResult, rawInfo);
                            Integer num2 = (Integer) totalCaptureResult.get(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION);
                            if (num2 != null) {
                                rawInfo.evList[0] = num2.intValue();
                                inputInfo.inputImagesEV[0] = (float) num2.intValue();
                            }
                            inputInfo.inputImages[0] = getRawImage(image3, i3);
                            this.mSuperNightJni.addOneInputInfo(rawInfo, inputInfo);
                            i5++;
                            arrayList4 = arrayList;
                            i6 = width;
                            i7 = height;
                            i4 = -1;
                        }
                    }
                    LOG.d(str2, "TotalCaptureResult - > error invalid param");
                    if (this.mCountDownLatch != null) {
                        LOG.d(str2, str);
                        this.mCountDownLatch.countDown();
                    }
                    return -1;
                }
            } catch (IllegalStateException e2) {
                e = e2;
                i4 = -1;
                String str32 = "Error";
                StringBuilder sb22 = new StringBuilder();
                sb22.append("e-> ");
                sb22.append(e.toString());
                LOG.d(str32, sb22.toString());
            }
        }
        FaceInfo faceInfo = null;
        Iterator it = arrayList2.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            TotalCaptureResult totalCaptureResult2 = (TotalCaptureResult) it.next();
            Integer num3 = (Integer) totalCaptureResult2.get(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION);
            if (num3 != null && num3.intValue() == 0) {
                faceInfo = getFaceInfo(totalCaptureResult2, i6, i7);
                break;
            }
        }
        if (this.mIsCancel) {
            LOG.d(str2, "is cancel 1");
            if (this.mCountDownLatch != null) {
                LOG.d(str2, str);
                this.mCountDownLatch.countDown();
            }
            return -1;
        }
        InputInfo inputInfo2 = new InputInfo();
        inputInfo2.curIndex = 0;
        inputInfo2.imgNum = size;
        inputInfo2.cameraState = 2;
        inputInfo2.inputImages[0] = getRawImage(image2, i3);
        inputInfo2.inputFd[0] = i2;
        i4 = this.mSuperNightJni.process(faceInfo, inputInfo2, 3, rect, this);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("process = ");
        sb5.append(i4);
        sb5.append(", cropRect0 = ");
        sb5.append(rect.toString());
        LOG.d(str2, sb5.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0079, code lost:
        if (r7.mCountDownLatch != null) goto L_0x007b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x007b, code lost:
        com.arcsoft.supernight.LOG.d(r2, r0);
        r7.mCountDownLatch.countDown();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x008c, code lost:
        if (r7.mCountDownLatch == null) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x008f, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int addOneInputInfo(Image image, TotalCaptureResult totalCaptureResult, int i, int i2, int i3) {
        String str = "mCountDownLatch.countDown() 5";
        int i4 = -1;
        String str2 = TAG;
        if (image == null || totalCaptureResult == null || this.mSuperNightJni == null || i < 0) {
            LOG.d(str2, "addOneInputInfo - > error invalid param");
            return -1;
        }
        try {
            if (!setupSomeVendorTag(totalCaptureResult)) {
                LOG.d(str2, "setupSomeVendorTag is error!!");
                if (this.mCountDownLatch != null) {
                    LOG.d(str2, str);
                    this.mCountDownLatch.countDown();
                }
                return -1;
            }
            InputInfo inputInfo = new InputInfo();
            inputInfo.curIndex = i;
            inputInfo.imgNum = i3;
            inputInfo.cameraState = 2;
            RawInfo rawInfo = new RawInfo();
            rawInfo.rawType = 0;
            getVendorTagValue(totalCaptureResult, rawInfo);
            Integer num = (Integer) totalCaptureResult.get(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION);
            if (num != null) {
                rawInfo.evList[0] = num.intValue();
                inputInfo.inputImagesEV[0] = (float) num.intValue();
            }
            inputInfo.inputImages[0] = getRawImage(image, i2);
            i4 = this.mSuperNightJni.addOneInputInfo(rawInfo, inputInfo);
            if (num != null && num.intValue() == 0 && this.mMetdata == null) {
                this.mMetdata = totalCaptureResult;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Throwable th) {
            if (this.mCountDownLatch != null) {
                LOG.d(str2, str);
                this.mCountDownLatch.countDown();
            }
            throw th;
        }
    }

    public void cancelSuperNight() {
        SuperNightJni superNightJni = this.mSuperNightJni;
        if (superNightJni != null) {
            this.mIsCancel = true;
            superNightJni.cancelSuperNight();
            if (!this.mInit) {
                LOG.d(TAG, "mInit is false ,cancelSuperNight return!!!");
                return;
            }
            this.mCountDownLatch = new CountDownLatch(1);
            try {
                this.mCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int init(int i, int i2, int i3, int i4) {
        SuperNightJni superNightJni = this.mSuperNightJni;
        if (superNightJni == null) {
            return -1;
        }
        superNightJni.init(i, i2, i3, i4);
        int preProcess = this.mSuperNightJni.preProcess();
        this.mIsCancel = false;
        this.mInit = true;
        StringBuilder sb = new StringBuilder();
        sb.append("preprocess = ");
        sb.append(preProcess);
        LOG.d(TAG, sb.toString());
        return preProcess;
    }

    public void onProgress(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("progress = ");
        sb.append(i);
        sb.append(" status = ");
        sb.append(i2);
        LOG.d(TAG, sb.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x006c, code lost:
        if (r10.mCountDownLatch == null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x006f, code lost:
        return r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0059, code lost:
        if (r10.mCountDownLatch != null) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x005b, code lost:
        com.arcsoft.supernight.LOG.d(r8, r7);
        r10.mCountDownLatch.countDown();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int process(Image image, int i, Rect rect, int i2) {
        String str = "mCountDownLatch.countDown() 4";
        String str2 = TAG;
        int i3 = -1;
        if (image == null || rect == null) {
            return -1;
        }
        try {
            FaceInfo faceInfo = getFaceInfo(this.mMetdata, image.getWidth(), image.getHeight());
            InputInfo inputInfo = new InputInfo();
            inputInfo.curIndex = 0;
            inputInfo.imgNum = i2;
            inputInfo.cameraState = 2;
            inputInfo.inputImages[0] = getRawImage(image, i);
            i3 = this.mSuperNightJni.process(faceInfo, inputInfo, 3, rect, this);
            StringBuilder sb = new StringBuilder();
            sb.append("process = ");
            sb.append(i3);
            sb.append(", cropRect0 = ");
            sb.append(rect.toString());
            LOG.d(str2, sb.toString());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Throwable th) {
            if (this.mCountDownLatch != null) {
                LOG.d(str2, str);
                this.mCountDownLatch.countDown();
            }
            throw th;
        }
    }

    public void readDebugFileValue() {
        File file = new File(DEBUG_FILE);
        boolean exists = file.exists();
        String str = TAG;
        if (exists || file.isFile()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                int i = 0;
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("dump file line = ");
                    sb.append(readLine);
                    LOG.d(str, sb.toString());
                    if (!TextUtils.isEmpty(readLine)) {
                        if (i >= 2) {
                            break;
                        }
                        boolean z = true;
                        if (readLine.contains(DUMP_KEY)) {
                            String trim = readLine.trim();
                            String substring = trim.substring(trim.length() - 1);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("dump file value =");
                            sb2.append(substring);
                            LOG.d(str, sb2.toString());
                            if (Integer.parseInt(substring) != 1) {
                                z = false;
                            }
                            this.mDumpFile = z;
                        } else if (readLine.contains(LOG_KEY)) {
                            String trim2 = readLine.trim();
                            String substring2 = trim2.substring(trim2.length() - 1);
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("debug log value =");
                            sb3.append(substring2);
                            LOG.d(str, sb3.toString());
                            if (Integer.parseInt(substring2) != 1) {
                                z = false;
                            }
                            LOG.DEBUG = z;
                            TimeConsumingUtil.DEBUG = LOG.DEBUG;
                        }
                        i++;
                    }
                }
                bufferedReader.close();
                inputStreamReader.close();
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (NumberFormatException e3) {
                e3.printStackTrace();
            }
            return;
        }
        LOG.d(str, "dump file return false 0");
    }

    public void setEnableAdrcGain(boolean z) {
        this.mEnableAdrcGain = z;
    }

    public void setEnableBlackLevel(boolean z) {
        this.mEnableBlackLevel = z;
    }

    public void setEnableWbGain(boolean z) {
        this.mEnableWbGain = z;
    }

    public void setFaceOrientation(int i) {
        this.mFaceOrientation = i;
    }

    public int unInit() {
        SuperNightJni superNightJni = this.mSuperNightJni;
        if (superNightJni == null) {
            return -1;
        }
        int postProcess = superNightJni.postProcess();
        StringBuilder sb = new StringBuilder();
        sb.append("postProcess = ");
        sb.append(postProcess);
        String sb2 = sb.toString();
        String str = TAG;
        LOG.d(str, sb2);
        int unInit = this.mSuperNightJni.unInit();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("unInit = ");
        sb3.append(unInit);
        LOG.d(str, sb3.toString());
        this.mMetdata = null;
        CountDownLatch countDownLatch = this.mCountDownLatch;
        if (countDownLatch != null && countDownLatch.getCount() > 0) {
            LOG.d(str, "mCountDownLatch.countDown() 6");
            this.mCountDownLatch.countDown();
        }
        this.mInit = false;
        return unInit;
    }
}
