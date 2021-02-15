package com.android.camera.module.loader.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.camera2.CameraManager;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.camera.LocalParallelService.LocalBinder;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.log.Log;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera2.CameraCapabilities;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressLint({"MissingPermission"})
@TargetApi(21)
public class Camera2CompatAdapterRole extends Camera2CompatAdapter {
    public static final int BOKEH_ROLE_ID = 61;
    public static final int DEPTH_ROLE_ID = 25;
    public static final int FRONT_AUX_ROLE_ID = 40;
    public static final int FRONT_BOKEH_ROLE_ID = 81;
    public static final int FRONT_SAT_ROLE_ID = 80;
    public static final int MACRO_2X_ROLE_ID = 24;
    public static final int MACRO_ROLE_ID = 22;
    public static final int MAIN_BACK_ROLE_ID = 0;
    public static final int MAIN_FRONT_ROLE_ID = 1;
    public static final int PARALLEL_VIRTUAL_ROLE_ID = 102;
    public static final int PHOTO_SAT_ROLE_ID = 60;
    public static final int PIP_ROLE_ID = 64;
    private static final String TAG = "Camera2CompatAdapterRole";
    public static final int TELE_4X_ROLE_ID = 23;
    public static final int TELE_ROLE_ID = 20;
    public static final int ULTRA_WIDE_BOKEH_ROLE_ID = 63;
    public static final int ULTRA_WIDE_ROLE_ID = 21;
    public static final int VIDEO_SAT_ROLE_ID = 62;
    public static final int VIRTUAL_BACK_ROLE_ID = 100;
    public static final int VIRTUAL_FRONT_ROLE_ID = 101;
    private volatile SparseIntArray mCameraRoleIdMap;

    protected Camera2CompatAdapterRole() {
    }

    private void dumpCameraIds() {
        for (int i = 0; i < this.mCameraRoleIdMap.size(); i++) {
            int keyAt = this.mCameraRoleIdMap.keyAt(i);
            int valueAt = this.mCameraRoleIdMap.valueAt(i);
            Set physicalCameraIds = ((CameraCapabilities) this.mCapabilities.get(valueAt)).getPhysicalCameraIds();
            float viewAngle = ((CameraCapabilities) this.mCapabilities.get(valueAt)).getViewAngle(false);
            if (physicalCameraIds == null || physicalCameraIds.isEmpty()) {
                Log.d(TAG, String.format("role: %3d (%5.1f°) <-> %2d", new Object[]{Integer.valueOf(keyAt), Float.valueOf(viewAngle), Integer.valueOf(valueAt)}));
            } else {
                Log.d(TAG, String.format("role: %3d (%5.1f°) <-> %2d = %s", new Object[]{Integer.valueOf(keyAt), Float.valueOf(viewAngle), Integer.valueOf(valueAt), physicalCameraIds}));
            }
        }
    }

    public synchronized int getAuxCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getAuxCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(20, -1);
    }

    public synchronized int getAuxFrontCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getAuxFrontCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(40, -1);
    }

    public synchronized int getBokehCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getBokehCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(61, -1);
    }

    public synchronized int getBokehFrontCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getBokehFrontCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(81, -1);
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
        return this.mCameraRoleIdMap.get(1, -1);
    }

    public synchronized int getMainBackCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getMainBackCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(0, -1);
    }

    public synchronized int getParallelVirtualCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getParallelVirtualCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(102, -1);
    }

    public synchronized int getRoleIdByActualId(int i) {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getRoleIdByActualId(): #init() failed.");
            return -1;
        }
        int indexOfValue = this.mCameraRoleIdMap.indexOfValue(i);
        if (indexOfValue < 0) {
            return -1;
        }
        return this.mCameraRoleIdMap.keyAt(indexOfValue);
    }

    public synchronized int getSATCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getSATCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(60, -1);
    }

    public synchronized int getSATFrontCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getSATFrontCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(80, -1);
    }

    public synchronized int getStandaloneMacroCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getStandaloneMacroCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(22, this.mCameraRoleIdMap.get(24, -1));
    }

    public synchronized int getUltraTeleCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getUltraTeleCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(23, -1);
    }

    public synchronized int getUltraWideBokehCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getUltraWideBokehCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(63, -1);
    }

    public synchronized int getUltraWideCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getUltraWideCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(21, -1);
    }

    public synchronized int getVideoSATCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getVideoSATCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(62, -1);
    }

    public synchronized int getVirtualBackCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getVirtualBackCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(100, -1);
    }

    public synchronized int getVirtualFrontCameraId() {
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getVirtualFrontCameraId(): #init() failed.");
            return -1;
        }
        return this.mCameraRoleIdMap.get(101, -1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean hasBokehCamera() {
        boolean z = false;
        if (!isInitialized()) {
            Log.d(TAG, "Warning: hasBokehCamera(): #init() failed.");
            return false;
        } else if (this.mCameraRoleIdMap.indexOfKey(61) >= 0) {
            z = true;
        }
    }

    public boolean hasPortraitCamera() {
        return hasBokehCamera();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean hasSATCamera() {
        boolean z = false;
        if (!isInitialized()) {
            Log.d(TAG, "Warning: hasSATCamera(): #init() failed.");
            return false;
        } else if (this.mCameraRoleIdMap.indexOfKey(60) >= 0) {
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
            Log.k(3, str, sb.toString());
            this.mCapabilities = new SparseArray(cameraIdList.length);
            this.mCameraRoleIdMap = new SparseIntArray(cameraIdList.length);
            for (String str2 : cameraIdList) {
                try {
                    int parseInt = Integer.parseInt(str2);
                    CameraCapabilities cameraCapabilities = new CameraCapabilities(cameraManager.getCameraCharacteristics(str2), parseInt);
                    this.mCapabilities.put(parseInt, cameraCapabilities);
                    int[] cameraRoleIds = cameraCapabilities.getCameraRoleIds();
                    if (cameraRoleIds != null) {
                        for (int put : cameraRoleIds) {
                            this.mCameraRoleIdMap.put(put, parseInt);
                        }
                    } else {
                        this.mCameraRoleIdMap.put(cameraCapabilities.getCameraRoleId(), parseInt);
                    }
                } catch (NumberFormatException unused) {
                    String str3 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("non-integer camera id: ");
                    sb2.append(str2);
                    Log.e(str3, sb2.toString());
                }
            }
            LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
            if (localBinder != null && isInitialized()) {
                localBinder.updateVirtualCameraIds();
            }
            dumpCameraIds();
        } catch (Exception e) {
            String str4 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Failed to init Camera2RoleContainer: ");
            sb3.append(e);
            Log.e(str4, sb3.toString());
            reset();
        }
        Log.d(TAG, "X: init()");
    }

    public boolean isInitialized() {
        return (this.mCapabilities == null || this.mCapabilities.size() <= 0 || this.mCameraRoleIdMap == null) ? false : true;
    }

    public synchronized boolean isPartSAT() {
        return false;
    }

    public synchronized void reset() {
        Log.d(TAG, "E: reset()");
        this.mCurrentOpenedCameraId = -1;
        this.mCapabilities = null;
        this.mCameraRoleIdMap = null;
        Log.d(TAG, "X: reset()");
    }
}
