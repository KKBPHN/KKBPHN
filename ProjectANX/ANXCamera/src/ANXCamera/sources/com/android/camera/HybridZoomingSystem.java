package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.Rect;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class HybridZoomingSystem {
    private static final String DEFAULT_OPTICAL_ZOOM_RATIO_COMBINATION = (C0124O00000oO.isSupportedOpticalZoom() ? "1.0:2.0" : "1.0");
    public static final float FLOAT_MICRO_SCENE_ZOOM_MAX = 1.0f;
    public static final float FLOAT_MOON_MODE_ZOOM_MAX = 20.0f;
    public static final float FLOAT_MOON_MODE_ZOOM_MIN = 1.0f;
    public static final float FLOAT_STEP_FOR_ZOOM_RATIO_CHANGE = 0.1f;
    public static final float FLOAT_ULTRA_WIDE_ZOOM_MAX = 6.0f;
    public static final float FLOAT_ZOOM_RATIO_10X = 10.0f;
    public static final float FLOAT_ZOOM_RATIO_2X = 2.0f;
    public static final float FLOAT_ZOOM_RATIO_30X = 30.0f;
    public static final float FLOAT_ZOOM_RATIO_3X = 3.0f;
    public static final float FLOAT_ZOOM_RATIO_5X = 5.0f;
    public static final float FLOAT_ZOOM_RATIO_MACRO = 0.0f;
    public static final float FLOAT_ZOOM_RATIO_NONE = 1.0f;
    public static float FLOAT_ZOOM_RATIO_ULTR = 0.0f;
    public static final float FLOAT_ZOOM_RATIO_ULTRA_TELE_THRESHHOLD = 3.7f;
    public static final float FLOAT_ZOOM_RATIO_WIDE = 1.0f;
    public static final boolean IS_2_OR_MORE_SAT = (OPTICAL_ZOOM_RATIO_COMBINATION >= 2);
    public static final boolean IS_2_SAT = (OPTICAL_ZOOM_RATIO_COMBINATION == 2);
    public static final boolean IS_3_OR_MORE_SAT = (OPTICAL_ZOOM_RATIO_COMBINATION >= 3);
    public static final boolean IS_3_SAT = (OPTICAL_ZOOM_RATIO_COMBINATION == 3);
    public static final boolean IS_4_OR_MORE_SAT;
    public static final boolean IS_4_SAT = (OPTICAL_ZOOM_RATIO_COMBINATION == 4);
    private static final String MACRO_OPTICAL_ZOOM_RATIO_COMBINATION = C0122O00000o.instance().O000OOoO(DEFAULT_OPTICAL_ZOOM_RATIO_COMBINATION);
    private static final int OPTICAL_ZOOM_RATIO_COMBINATION = C0122O00000o.instance().O00oOooO(C0124O00000oO.isSupportedOpticalZoom() ? 2 : 1);
    public static float[] SAT_ZOOM_RATIO_X = C0122O00000o.instance().OO00O0o();
    public static float[] SAT_ZOOM_RATIO_Y = C0122O00000o.instance().OO00O();
    public static final String STRING_ZOOM_RATIO_NONE = "1.0";
    public static final boolean SUPPORT_PROGRESS_ZOOM_EFFECT = C0122O00000o.instance().OOo0Ooo();
    private static final String TAG = "HybridZoomingSystem";
    public static final float TOLERANCE_FOR_ZOOM_RATIO_CHANGED = 0.01f;
    public static float[] VIDEO_SAT_ZOOM_RATIO_X = C0122O00000o.instance().OO00o0O();
    public static float[] VIDEO_SAT_ZOOM_RATIO_Y = C0122O00000o.instance().OO00o0o();
    public static final String ZOOM_INDEXS_DEFAULT;
    public static final String ZOOM_RULER_DEFAULT;
    public static float sDefaultMacroOpticalZoomRatio;
    private static float[] sMacroSupportedOpticalZoomRatios;
    private static final Map sZoomRatioHistory = new ConcurrentHashMap();

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x013a, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x013f, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0140, code lost:
        r1.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0143, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static {
        boolean z = true;
        String str = (CameraSettings.isFakePartSAT() || !C0124O00000oO.isSupportedOpticalZoom()) ? "1:2:4:6:8:10" : "0.6:1:2:5:10";
        ZOOM_INDEXS_DEFAULT = str;
        String str2 = (CameraSettings.isFakePartSAT() || !C0124O00000oO.isSupportedOpticalZoom()) ? "10:10:5:5:5" : "4:10:10:5";
        ZOOM_RULER_DEFAULT = str2;
        sDefaultMacroOpticalZoomRatio = 1.0f;
        FLOAT_ZOOM_RATIO_ULTR = 0.6f;
        ArrayList<String> arrayList = new ArrayList<>();
        float[] O00oOooo = C0122O00000o.instance().O00oOooo(163);
        int i = 0;
        while (true) {
            if (i >= O00oOooo.length) {
                break;
            } else if (O00oOooo[i] < 1.0f) {
                FLOAT_ZOOM_RATIO_ULTR = O00oOooo[i];
                break;
            } else {
                i++;
            }
        }
        if (C0122O00000o.instance().isSupportMacroMode()) {
            arrayList.clear();
            Scanner scanner = new Scanner(MACRO_OPTICAL_ZOOM_RATIO_COMBINATION);
            scanner.useDelimiter("\\s*[:,]\\s*");
            int i2 = -1;
            while (scanner.hasNext()) {
                String next = scanner.next();
                if (next != null && next.length() > 0) {
                    arrayList.add(next);
                    i2++;
                    if (i2 == 0) {
                        sDefaultMacroOpticalZoomRatio = Float.valueOf(next).floatValue();
                    }
                }
            }
            scanner.close();
            if (arrayList.size() >= 1) {
                sMacroSupportedOpticalZoomRatios = new float[arrayList.size()];
                int i3 = 0;
                for (String parseFloat : arrayList) {
                    int i4 = i3 + 1;
                    sMacroSupportedOpticalZoomRatios[i3] = Float.parseFloat(parseFloat);
                    i3 = i4;
                }
                Log.d(TAG, Arrays.toString(sMacroSupportedOpticalZoomRatios));
            } else {
                throw new IllegalStateException("The supported optical zoom ratios are probably not configured correctly");
            }
        }
        if (OPTICAL_ZOOM_RATIO_COMBINATION < 4) {
            z = false;
        }
        IS_4_OR_MORE_SAT = z;
    }

    private HybridZoomingSystem() {
    }

    public static float add(float f, float f2) {
        return ((float) (((int) (f * 10.0f)) + ((int) (f2 * 10.0f)))) / 10.0f;
    }

    public static float clamp(float f, float f2, float f3) {
        return f > f3 ? f3 : f < f2 ? f2 : f;
    }

    public static void clearZoomRatioHistory() {
        Log.d(TAG, "clearZoomRatioHistory()");
        sZoomRatioHistory.clear();
    }

    public static int getDefaultOpticalZoomRatioIndex(int i) {
        float[] supportedOpticalZoomRatios = getSupportedOpticalZoomRatios(i);
        for (int i2 = 0; i2 < supportedOpticalZoomRatios.length; i2++) {
            if (supportedOpticalZoomRatios[i2] >= 1.0f) {
                return i2;
            }
        }
        return 0;
    }

    public static float getMaximumMacroOpticalZoomRatio() {
        float[] fArr = sMacroSupportedOpticalZoomRatios;
        return fArr[fArr.length - 1];
    }

    public static float getMaximumOpticalZoomRatio(int i) {
        float[] supportedOpticalZoomRatios = getSupportedOpticalZoomRatios(i);
        return supportedOpticalZoomRatios[supportedOpticalZoomRatios.length - 1];
    }

    public static float getMinimumMacroOpticalZoomRatio() {
        return sMacroSupportedOpticalZoomRatios[0];
    }

    public static float getMinimumOpticalZoomRatio(int i) {
        return getSupportedOpticalZoomRatios(i)[0];
    }

    public static float getOpticalZoomRatioAt(int i, int i2) {
        float[] supportedOpticalZoomRatios = getSupportedOpticalZoomRatios(i);
        int length = supportedOpticalZoomRatios.length;
        String str = ")   curIndex error : ";
        String str2 = "The given index must be in range [0, ";
        String str3 = TAG;
        if (i2 < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(length);
            sb.append(str);
            sb.append(i2);
            Log.e(str3, sb.toString());
            i2 = 0;
        } else if (i2 >= length) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(length);
            sb2.append(str);
            sb2.append(i2);
            Log.e(str3, sb2.toString());
            i2 = length - 1;
        }
        return supportedOpticalZoomRatios[i2];
    }

    public static int getOpticalZoomRatioIndex(int i, float f) {
        float[] supportedOpticalZoomRatios = getSupportedOpticalZoomRatios(i);
        for (int length = supportedOpticalZoomRatios.length - 1; length >= 0; length--) {
            if (f >= supportedOpticalZoomRatios[length]) {
                return length;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Illegal zoom ratio: ");
        sb.append(f);
        throw new IllegalArgumentException(sb.toString());
    }

    public static float[] getSupportedOpticalZoomRatios(int i) {
        return C0122O00000o.instance().O00oOooo(i);
    }

    public static float getTeleMinZoomRatio() {
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getAuxCameraId());
        if (capabilities == null) {
            return 2.0f;
        }
        return capabilities.getMinZoomRatio(2.0f);
    }

    public static float getTeleZoomRatio(int i) {
        float[] supportedOpticalZoomRatios = getSupportedOpticalZoomRatios(i);
        int defaultOpticalZoomRatioIndex = getDefaultOpticalZoomRatioIndex(i) + 1;
        if (defaultOpticalZoomRatioIndex < supportedOpticalZoomRatios.length) {
            return supportedOpticalZoomRatios[defaultOpticalZoomRatioIndex];
        }
        return 2.0f;
    }

    public static float getUltraTeleMinZoomRatio() {
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getUltraTeleCameraId());
        if (capabilities == null) {
            return 5.0f;
        }
        return capabilities.getMinZoomRatio(5.0f);
    }

    public static String getZoomRatioHistory(int i, String str) {
        if (i == 165) {
            i = 163;
        }
        String str2 = (String) sZoomRatioHistory.get(Integer.valueOf(i));
        if (i == 188) {
            if (str2 == null) {
                str2 = String.valueOf(getMinimumOpticalZoomRatio(i));
            }
            return str2;
        }
        if (str2 != null) {
            str = str2;
        }
        return str;
    }

    public static float getZoomRatioNone(boolean z, int i) {
        if (!C0122O00000o.instance().OO0oo0O() || !z || (i != 0 && i != 180)) {
            return 1.0f;
        }
        return C0122O00000o.instance().O0ooO0O();
    }

    public static boolean isOpticalZoomRatio(int i, float f) {
        return Arrays.binarySearch(getSupportedOpticalZoomRatios(i), f) >= 0;
    }

    public static boolean isZoomRatioNone(float f, boolean z) {
        boolean z2 = true;
        if (!C0122O00000o.instance().OO0oo0O() || !z) {
            if (f != 1.0f) {
                z2 = false;
            }
            return z2;
        }
        if (!(f == 1.0f || f == C0122O00000o.instance().O0ooO0O())) {
            z2 = false;
        }
        return z2;
    }

    static void preload() {
        Log.d(TAG, "preload");
    }

    public static void setZoomRatioHistory(int i, String str) {
        if (i == 165) {
            i = 163;
        }
        sZoomRatioHistory.put(Integer.valueOf(i), str);
    }

    public static float sub(float f, float f2) {
        return ((float) (((int) (f * 10.0f)) - ((int) (f2 * 10.0f)))) / 10.0f;
    }

    public static Rect toCropRegion(float f, Rect rect) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Zoom ratio must be greater than 0.0f");
        } else if (rect != null) {
            int width = rect.width() / 2;
            int height = rect.height() / 2;
            float f2 = 2.0f * f;
            int width2 = (int) (((float) rect.width()) / f2);
            int height2 = (int) (((float) rect.height()) / f2);
            Rect rect2 = new Rect();
            rect2.set(width - width2, height - height2, width + width2, height + height2);
            StringBuilder sb = new StringBuilder();
            sb.append("toCropRegion(): zoom ratio = ");
            sb.append(f);
            sb.append(", crop region = ");
            sb.append(rect2);
            Log.d(TAG, sb.toString());
            return rect2;
        } else {
            throw new IllegalArgumentException("activeArraySize must be non null");
        }
    }

    public static float toDecimal(float f) {
        return ((float) ((int) (f * 10.0f))) / 10.0f;
    }

    public static float toFloat(String str, float f) {
        try {
            r2 = str;
            r2 = Float.parseFloat(str);
            r2 = r2;
            return r2;
        } catch (Exception unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid zoom: ");
            sb.append(r2);
            Log.e(TAG, sb.toString());
            return f;
        }
    }

    public static int[] toMTKCropRegion(float f, Rect rect) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Zoom ratio must be greater than 0.0f");
        } else if (rect != null) {
            int width = rect.width() / 2;
            int height = rect.height() / 2;
            float f2 = 2.0f * f;
            int width2 = (int) (((float) rect.width()) / f2);
            int height2 = (int) (((float) rect.height()) / f2);
            int[] iArr = {width - width2, height - height2, width2 * 2, height2 * 2};
            StringBuilder sb = new StringBuilder();
            sb.append("int[]{");
            sb.append(iArr[0]);
            String str = ", ";
            sb.append(str);
            sb.append(iArr[1]);
            sb.append(str);
            sb.append(iArr[2]);
            sb.append(str);
            sb.append(iArr[3]);
            sb.append("}");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("toMTKCropRegion(): zoom ratio = ");
            sb3.append(f);
            sb3.append(", crop region = ");
            sb3.append(sb2);
            Log.d(TAG, sb3.toString());
            return iArr;
        } else {
            throw new IllegalArgumentException("activeArraySize must be non null");
        }
    }

    public static String toString(float f) {
        Locale locale;
        Object[] objArr;
        String str = "%.1fx";
        if (FLOAT_ZOOM_RATIO_ULTR == f || 1.0f == f || getTeleMinZoomRatio() == f || getUltraTeleMinZoomRatio() == f) {
            locale = Locale.US;
            objArr = new Object[]{Float.valueOf(f)};
        } else if (0.0f == f) {
            return "macro";
        } else {
            float decimal = toDecimal(f);
            locale = Locale.US;
            objArr = new Object[]{Float.valueOf(decimal)};
        }
        return String.format(locale, str, objArr);
    }

    public static float toZoomRatio(Rect rect, Rect rect2) {
        if (rect == null || rect2 == null) {
            throw new IllegalArgumentException("activeArraySize & cropRegion must be non null");
        }
        float width = ((float) rect.width()) / ((float) rect2.width());
        StringBuilder sb = new StringBuilder();
        sb.append("toZoomRatio(): activeArraySize = ");
        sb.append(rect);
        sb.append(", crop region = ");
        sb.append(rect2);
        Log.c(TAG, sb.toString());
        return width;
    }
}
