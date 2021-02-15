package com.android.camera.module.loader.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.SparseArray;
import com.android.camera.CameraSettings;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.log.Log;
import com.android.camera2.CameraCapabilities;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class Camera2CompatAdapterCommon extends Camera2CompatAdapter {
    private static final int INDEX_AUX = 1;
    private static final int INDEX_BOKEH = 3;
    private static final int INDEX_INFRARED = 5;
    private static final int INDEX_MAIN = 0;
    private static final int INDEX_SAT = 2;
    private static final int INDEX_VIRTUAL = 4;
    private static final int MAX_TYPES_OF_CAMERAS_OF_EACH_FACING_DIRECTION = 6;
    public static final int STANDALONE_MACRO_CAMERA_ID = 22;
    private static final int STANDALONE_SAT_CAMERA_ID = 180;
    private static final String TAG = "Camera2CompatAdapterCommon";
    public static final int TRIPLE_SAT_CAMERA_ID = 120;
    public static final int ULTRA_TELE_CAMERA_ID = 23;
    public static final int ULTRA_WIDE_BOKEH_CAMERA_ID = 63;
    public static final int ULTRA_WIDE_CAMERA_ID = 21;
    private volatile int[] mOrderedCameraIds = null;

    protected Camera2CompatAdapterCommon() {
    }

    private void dumpCameraIds() {
        int[] iArr = new int[6];
        int[] iArr2 = new int[6];
        for (int i = 0; i < 6; i++) {
            iArr[i] = this.mOrderedCameraIds[i];
            iArr2[i] = this.mOrderedCameraIds[(this.mOrderedCameraIds.length / 2) + i];
        }
        String str = "====================================================================";
        Log.d(TAG, str);
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append(" BACK: [main, aux, sat, bokeh, virtual, infrared] = ");
        sb.append(Arrays.toString(iArr));
        Log.d(str2, sb.toString());
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("FRONT: [main, aux, sat, bokeh, virtual, infrared] = ");
        sb2.append(Arrays.toString(iArr2));
        Log.d(str3, sb2.toString());
        Log.d(TAG, str);
    }

    private synchronized boolean isValid(int i) {
        for (int i2 : this.mOrderedCameraIds) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public synchronized int getAuxCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getAuxCameraId(): #init() failed.");
            return -1;
        }
        return this.mOrderedCameraIds[1];
    }

    public synchronized int getAuxFrontCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getAuxFrontCameraId(): #init() failed.");
            return -1;
        }
        return this.mOrderedCameraIds[(this.mOrderedCameraIds.length / 2) + 1];
    }

    public synchronized int getBokehCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getBokehCameraId(): #init() failed.");
            return -1;
        } else if (this.mOrderedCameraIds[3] != -1) {
            return this.mOrderedCameraIds[3];
        } else {
            return this.mOrderedCameraIds[2];
        }
    }

    public synchronized int getBokehFrontCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getBokehFrontCameraId(): #init() failed.");
            return -1;
        }
        return this.mOrderedCameraIds[(this.mOrderedCameraIds.length / 2) + 3];
    }

    public ConcurrentHashMap getDefaultDualVideoCameraIds() {
        int i;
        RenderSourceType renderSourceType;
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        if (C0122O00000o.instance().OOO000o()) {
            renderSourceType = RenderSourceType.MAIN;
            i = getUltraWideCameraId();
        } else {
            renderSourceType = RenderSourceType.MAIN;
            i = getMainBackCameraId();
        }
        concurrentHashMap.put(renderSourceType, Integer.valueOf(i));
        concurrentHashMap.put(RenderSourceType.SUB, Integer.valueOf(getFrontCameraId()));
        return concurrentHashMap;
    }

    public synchronized int getFrontCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getFrontCameraId(): #init() failed.");
            return -1;
        }
        return this.mOrderedCameraIds[(this.mOrderedCameraIds.length / 2) + 0];
    }

    public synchronized int getMainBackCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getMainBackCameraId(): #init() failed.");
            return -1;
        }
        return this.mOrderedCameraIds[0];
    }

    public int getParallelVirtualCameraId() {
        return -1;
    }

    public synchronized int getRoleIdByActualId(int i) {
        return i;
    }

    public synchronized int getSATCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getSATCameraId(): #init() failed.");
            return -1;
        } else if (HybridZoomingSystem.IS_4_SAT && CameraSettings.isSupportedOpticalZoom() && isValid(180)) {
            return 180;
        } else {
            if (HybridZoomingSystem.IS_3_SAT && CameraSettings.isSupportedOpticalZoom() && isValid(120)) {
                return 120;
            }
            return this.mOrderedCameraIds[2];
        }
    }

    public synchronized int getSATFrontCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getSATFrontCameraId(): #init() failed.");
            return -1;
        }
        return this.mOrderedCameraIds[(this.mOrderedCameraIds.length / 2) + 2];
    }

    public synchronized int getStandaloneMacroCameraId() {
        int i;
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getStandaloneMacroCameraId(): #init() failed.");
            i = -1;
        } else {
            i = 22;
        }
        return i;
    }

    public synchronized int getUltraTeleCameraId() {
        int i;
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getUltraWideCameraId(): #init() failed.");
            i = -1;
        } else {
            i = 23;
        }
        return i;
    }

    public synchronized int getUltraWideBokehCameraId() {
        int i;
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getUltraWideBokehCameraId(): #init() failed.");
            i = -1;
        } else {
            i = 63;
        }
        return i;
    }

    public synchronized int getUltraWideCameraId() {
        int i;
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getUltraWideCameraId(): #init() failed.");
            i = -1;
        } else {
            i = 21;
        }
        return i;
    }

    public synchronized int getVideoSATCameraId() {
        return -1;
    }

    public int getVirtualBackCameraId() {
        return -1;
    }

    public int getVirtualFrontCameraId() {
        return -1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001b, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean hasBokehCamera() {
        boolean z = false;
        if (!isInitialized()) {
            Log.d(TAG, "Warning: hasBokehCamera(): #init() failed.");
            return false;
        } else if (this.mOrderedCameraIds[3] != -1) {
            z = true;
        }
    }

    public boolean hasPortraitCamera() {
        return hasSATCamera();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001b, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean hasSATCamera() {
        boolean z = false;
        if (!isInitialized()) {
            Log.d(TAG, "Warning: hasSATCamera(): #init() failed.");
            return false;
        } else if (this.mOrderedCameraIds[2] != -1) {
            z = true;
        }
    }

    public boolean hasTeleCamera() {
        return (getAuxCameraId() == -1 && getUltraTeleCameraId() == -1) ? false : true;
    }

    public void init(CameraManager cameraManager) {
        Log.d(TAG, "E: init()");
        try {
            reset();
            String[] cameraIdList = cameraManager.getCameraIdList();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("All available camera ids: ");
            sb.append(Arrays.deepToString(cameraIdList));
            Log.d(str, sb.toString());
            int max = Math.max(6, cameraIdList.length);
            this.mOrderedCameraIds = new int[(max * 2)];
            Arrays.fill(this.mOrderedCameraIds, -1);
            this.mCapabilities = new SparseArray(cameraIdList.length);
            int length = cameraIdList.length;
            int i = 0;
            int i2 = max;
            int i3 = 0;
            while (i < length) {
                String str2 = cameraIdList[i];
                try {
                    int parseInt = Integer.parseInt(str2);
                    CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(str2);
                    this.mCapabilities.put(parseInt, new CameraCapabilities(cameraCharacteristics, parseInt));
                    if (C0122O00000o.instance().isSupportUltraWide()) {
                        if (21 == parseInt && !C0124O00000oO.O0o00o0) {
                            i++;
                        } else if (63 == parseInt) {
                            i++;
                        }
                    }
                    if (C0122O00000o.instance().isSupportMacroMode() && 22 == parseInt) {
                        i++;
                    } else if (!C0122O00000o.instance().OOOOoOo() || 23 != parseInt) {
                        Integer num = (Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                        if (num == null) {
                            String str3 = TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Unknown facing direction of camera ");
                            sb2.append(parseInt);
                            Log.d(str3, sb2.toString());
                        } else if (num.intValue() == 1) {
                            int i4 = i3 + 1;
                            this.mOrderedCameraIds[i3] = parseInt;
                            i3 = i4;
                        } else if (num.intValue() == 0) {
                            int i5 = i2 + 1;
                            this.mOrderedCameraIds[i2] = parseInt;
                            i2 = i5;
                        }
                        i++;
                    } else {
                        i++;
                    }
                } catch (NumberFormatException unused) {
                    String str4 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("non-integer camera id: ");
                    sb3.append(str2);
                    Log.e(str4, sb3.toString());
                }
            }
            dumpCameraIds();
        } catch (Exception e) {
            String str5 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Failed to init Camera2DataContainer: ");
            sb4.append(e);
            Log.e(str5, sb4.toString());
            reset();
        }
        Log.d(TAG, "X: init()");
    }

    public boolean isInitialized() {
        return (this.mCapabilities == null || this.mCapabilities.size() <= 0 || this.mOrderedCameraIds == null) ? false : true;
    }

    public synchronized boolean isPartSAT() {
        int sATCameraId = getSATCameraId();
        if (sATCameraId == -1) {
            return false;
        }
        if (sATCameraId != 120) {
            if (sATCameraId != 180) {
                if (HybridZoomingSystem.IS_2_SAT) {
                    return false;
                }
            } else if (HybridZoomingSystem.IS_4_SAT) {
                return false;
            }
        } else if (HybridZoomingSystem.IS_3_SAT) {
            return false;
        }
        return true;
    }

    public synchronized void reset() {
        Log.d(TAG, "E: reset()");
        this.mCurrentOpenedCameraId = -1;
        this.mCapabilities = null;
        this.mOrderedCameraIds = null;
        Log.d(TAG, "X: reset()");
    }
}
