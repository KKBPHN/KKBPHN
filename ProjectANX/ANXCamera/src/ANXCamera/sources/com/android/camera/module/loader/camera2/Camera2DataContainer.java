package com.android.camera.module.loader.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.camera2.CameraManager;
import android.os.SystemProperties;
import android.util.SparseArray;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.runing.ComponentRunningEisPro;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.log.Log;
import com.android.camera2.CameraCapabilities;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@SuppressLint({"MissingPermission"})
@TargetApi(21)
public class Camera2DataContainer implements Camera2Compat {
    public static final int BOGUS_CAMERA_ID_BACK = 0;
    public static final int BOGUS_CAMERA_ID_FRONT = 1;
    private static final String TAG = "Camera2DataContainer";
    private static final Camera2DataContainer sInstance = new Camera2DataContainer();
    private Camera2CompatAdapter mCamera2CompatAdapter;

    protected Camera2DataContainer() {
        this.mCamera2CompatAdapter = C0124O00000oO.Oo00() ? new Camera2CompatAdapterRole() : new Camera2CompatAdapterCommon();
    }

    public static Camera2DataContainer getInstance() {
        synchronized (sInstance) {
            if (!sInstance.isInitialized()) {
                sInstance.init((CameraManager) CameraAppImpl.getAndroidContext().getSystemService("camera"));
            }
        }
        return sInstance;
    }

    public static Camera2DataContainer getInstance(CameraManager cameraManager) {
        synchronized (sInstance) {
            if (!sInstance.isInitialized()) {
                sInstance.init(cameraManager);
            }
        }
        return sInstance;
    }

    private int getVideoModeCameraId(int i, int i2) {
        return CameraSettings.isVideoQuality8KOpen(i2) ? (i2 != 162 || !C0122O00000o.instance().OOOOoOo() || !CameraSettings.is8KCamcorderSupported(getInstance().getUltraTeleCameraId())) ? getMainBackCameraId() : CameraSettings.getRetainZoom(i2) < HybridZoomingSystem.getUltraTeleMinZoomRatio() ? getMainBackCameraId() : getUltraTeleCameraId() : (!CameraSettings.isDualCameraSatEnable() || !C0122O00000o.instance().OOoOo()) ? i : !CameraSettings.supportVideoSATForVideoQuality(i2) ? getMainBackCameraId() : getVideoSATCameraId();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:115:0x01da, code lost:
        if (com.android.camera.data.data.config.ComponentManuallyDualLens.LENS_ULTRA_TELE.equals(r2) != false) goto L_0x01dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:246:0x03d5, code lost:
        if (r2 == false) goto L_0x03e9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00ec, code lost:
        if (r2 >= com.android.camera.HybridZoomingSystem.getUltraTeleMinZoomRatio()) goto L_0x00ee;
     */
    /* JADX WARNING: Removed duplicated region for block: B:171:0x02b2  */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x02b4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int getActualOpenCameraId(int i, int i2) {
        int i3;
        float retainZoom;
        if (!isInitialized()) {
            Log.d(TAG, "Warning: getActualOpenCameraId(): #init() failed.");
            return i;
        }
        if (i == 0) {
            boolean z = CameraSettings.isDualCameraEnable() && (CameraSettings.isSupportedOpticalZoom() || CameraSettings.isSupportedPortrait()) && !DataRepository.dataItemGlobal().isForceMainBackCamera();
            if (!z && i2 != 167) {
                return i;
            }
            if (DataRepository.dataItemGlobal().isNormalIntent() || !((CameraCapabilities) this.mCamera2CompatAdapter.getCapabilities().get(getMainBackCameraId())).isSupportLightTripartite()) {
                if (i2 != 169) {
                    if (!(i2 == 186 || i2 == 188)) {
                        if (i2 != 179) {
                            if (i2 != 180) {
                                if (i2 != 182) {
                                    if (i2 != 183) {
                                        if (i2 != 204) {
                                            if (i2 != 205) {
                                                switch (i2) {
                                                    case 161:
                                                    case 162:
                                                        break;
                                                    case 163:
                                                        break;
                                                    default:
                                                        switch (i2) {
                                                            case 165:
                                                                break;
                                                            case 166:
                                                            case 167:
                                                                break;
                                                            default:
                                                                switch (i2) {
                                                                    case 171:
                                                                        if (!DataRepository.dataItemRunning().isSwitchOn("pref_ultra_wide_bokeh_enabled") || getUltraWideBokehCameraId() == -1) {
                                                                            if (getBokehCameraId() != -1) {
                                                                                i3 = getBokehCameraId();
                                                                                break;
                                                                            }
                                                                        } else {
                                                                            i3 = getUltraWideBokehCameraId();
                                                                            break;
                                                                        }
                                                                        break;
                                                                    case 172:
                                                                        if (C0122O00000o.instance().OOOOOo0()) {
                                                                            break;
                                                                        }
                                                                        break;
                                                                    case 173:
                                                                        if (CameraSettings.isSuperNightUWOpen(i2)) {
                                                                            if (!CameraSettings.isUltraWideConfigOpen(i2)) {
                                                                                if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                                                                                    float retainZoom2 = CameraSettings.getRetainZoom(i2);
                                                                                    String str = TAG;
                                                                                    StringBuilder sb = new StringBuilder();
                                                                                    sb.append("Currently user selected zoom ratio is ");
                                                                                    sb.append(retainZoom2);
                                                                                    Log.d(str, sb.toString());
                                                                                    if (retainZoom2 >= 1.0f) {
                                                                                        if (C0122O00000o.instance().OOOOoOo()) {
                                                                                            if (C0122O00000o.instance().getConfig().Oo0OoOo()) {
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        break;
                                                                    case 174:
                                                                        break;
                                                                    case 175:
                                                                        break;
                                                                }
                                                        }
                                                }
                                            }
                                        } else {
                                            i3 = ((Integer) DataRepository.dataItemRunning().getComponentRunningDualVideo().getLocalCameraId().get(RenderSourceType.MAIN)).intValue();
                                        }
                                        Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                        return i3;
                                    } else if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                                        float retainZoom3 = CameraSettings.getRetainZoom(i2);
                                        String str2 = TAG;
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("Currently user selected zoom ratio is ");
                                        sb2.append(retainZoom3);
                                        Log.d(str2, sb2.toString());
                                        if (retainZoom3 < 1.0f && !CameraSettings.supportVideoSATForVideoQuality(i2)) {
                                            i3 = getUltraWideCameraId();
                                            Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                            return i3;
                                        } else if (C0122O00000o.instance().OOOo000() && retainZoom3 >= HybridZoomingSystem.getTeleMinZoomRatio()) {
                                            i3 = getAuxCameraId();
                                            if (!DataRepository.dataItemLive().getComponentLiveVideoQuality().isSupportVideoQuality(i2, i3)) {
                                                i3 = getMainBackCameraId();
                                            }
                                            Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                            return i3;
                                        } else if (C0122O00000o.instance().OOo000() && retainZoom3 >= HybridZoomingSystem.getUltraTeleMinZoomRatio()) {
                                            i3 = getUltraTeleCameraId();
                                            if (!DataRepository.dataItemLive().getComponentLiveVideoQuality().isSupportVideoQuality(i2, i3)) {
                                                i3 = getMainBackCameraId();
                                            }
                                            Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                            return i3;
                                        }
                                    }
                                }
                                if (CameraSettings.isZoomByCameraSwitchingSupported() && !CameraSettings.isSRTo108mModeOn()) {
                                    String cameraLensType = CameraSettings.getCameraLensType(i2);
                                    if (ComponentManuallyDualLens.LENS_WIDE.equals(cameraLensType)) {
                                    }
                                }
                                i3 = getMainBackCameraId();
                                Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                return i3;
                            }
                            if (CameraSettings.isZoomByCameraSwitchingSupported()) {
                                String cameraLensType2 = CameraSettings.getCameraLensType(i2);
                                if (ComponentManuallyDualLens.LENS_WIDE.equals(cameraLensType2)) {
                                    i3 = getMainBackCameraId();
                                    Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                    return i3;
                                }
                                if (!ComponentManuallyDualLens.LENS_TELE.equals(cameraLensType2)) {
                                    if (ComponentManuallyDualLens.LENS_ULTRA.equals(cameraLensType2)) {
                                        i3 = getUltraWideCameraId();
                                        Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                        return i3;
                                    }
                                    if (!"macro".equals(cameraLensType2)) {
                                        if (ComponentManuallyDualLens.LENS_ULTRA_TELE.equals(cameraLensType2)) {
                                            i3 = getUltraTeleCameraId();
                                            Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                            return i3;
                                        }
                                    }
                                    i3 = getStandaloneMacroCameraId();
                                    Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                    return i3;
                                }
                                i3 = getAuxCameraId();
                                Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                return i3;
                            }
                        } else {
                            if (C0122O00000o.instance().O0000O0o()) {
                                i3 = getUltraWideCameraId();
                                Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                return i3;
                            }
                            i3 = getMainBackCameraId();
                            Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                            return i3;
                        }
                    }
                    if (!CameraSettings.isMacroModeEnabled(i2)) {
                        if (!CameraSettings.isUltraPixelOn()) {
                            if (!CameraSettings.isDualCameraSatEnable() || CameraSettings.isFakePartSAT() || !C0124O00000oO.isSupportedOpticalZoom()) {
                                if (CameraSettings.isUltraWideConfigOpen(i2)) {
                                }
                            } else if (!CameraSettings.isUltraWideConfigOpen(i2)) {
                                i3 = getSATCameraId();
                                if ((!CameraSettings.isSupportedOpticalZoom() || CameraSettings.isFakePartSAT()) && HybridZoomingSystem.IS_3_OR_MORE_SAT && !CameraSettings.isUltraPixelOn()) {
                                    retainZoom = CameraSettings.getRetainZoom(i2);
                                    String str3 = TAG;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Currently user selected zoom ratio is ");
                                    sb3.append(retainZoom);
                                    Log.d(str3, sb3.toString());
                                    if (retainZoom >= 1.0f) {
                                        if (CameraSettings.isFakePartSAT()) {
                                            i3 = getSATCameraId();
                                        }
                                    }
                                }
                                Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                return i3;
                            }
                            i3 = getUltraWideCameraId();
                            retainZoom = CameraSettings.getRetainZoom(i2);
                            String str32 = TAG;
                            StringBuilder sb32 = new StringBuilder();
                            sb32.append("Currently user selected zoom ratio is ");
                            sb32.append(retainZoom);
                            Log.d(str32, sb32.toString());
                            if (retainZoom >= 1.0f) {
                            }
                        }
                        i3 = getMainBackCameraId();
                        retainZoom = CameraSettings.getRetainZoom(i2);
                        String str322 = TAG;
                        StringBuilder sb322 = new StringBuilder();
                        sb322.append("Currently user selected zoom ratio is ");
                        sb322.append(retainZoom);
                        Log.d(str322, sb322.toString());
                        if (retainZoom >= 1.0f) {
                        }
                    } else if (C0122O00000o.instance().OOoOO0o()) {
                        i3 = getStandaloneMacroCameraId();
                        Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                        return i3;
                    } else if (C0122O00000o.instance().OOoOOo()) {
                        i3 = getAuxCameraId();
                        Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                        return i3;
                    }
                    i3 = getUltraWideCameraId();
                    Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                    return i3;
                }
                if (CameraSettings.isMacroModeEnabled(i2)) {
                    if (C0122O00000o.instance().OOoOO0o() && !C0124O00000oO.O0o0O00) {
                        i3 = getStandaloneMacroCameraId();
                        Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                        return i3;
                    } else if (C0122O00000o.instance().OOoOOo()) {
                        i3 = getAuxCameraId();
                        Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                        return i3;
                    }
                } else if (!CameraSettings.isAutoZoomEnabled(i2)) {
                    if (!CameraSettings.isVideoQuality8KOpen(i2)) {
                        if (!CameraSettings.isVhdrOn(i2)) {
                            if (CameraSettings.isSuperEISEnabled(i2)) {
                                if (!CameraSettings.getSuperEISProValue(i2).equals(ComponentRunningEisPro.EIS_VALUE_PRO)) {
                                    if (!CameraSettings.getSuperEISProValue(i2).equals("normal")) {
                                        if (C0122O00000o.instance().OO0o0o0()) {
                                        }
                                    }
                                    i3 = getMainBackCameraId();
                                    Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                    return i3;
                                }
                            } else if (!CameraSettings.isUltraWideConfigOpen(i2)) {
                                if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                                    float retainZoom4 = CameraSettings.getRetainZoom(i2);
                                    String str4 = TAG;
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Currently user selected zoom ratio is ");
                                    sb4.append(retainZoom4);
                                    Log.d(str4, sb4.toString());
                                    if (retainZoom4 >= 1.0f || CameraSettings.supportVideoSATForVideoQuality(i2)) {
                                        if (C0122O00000o.instance().OOOo000() && retainZoom4 >= HybridZoomingSystem.getTeleMinZoomRatio() && !CameraSettings.supportVideoSATForVideoQuality(i2)) {
                                            i3 = getAuxCameraId();
                                            if (!DataRepository.dataItemConfig().getComponentConfigVideoQuality().isSupportVideoQuality(i2, i3)) {
                                                i3 = getMainBackCameraId();
                                            }
                                            Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                            return i3;
                                        } else if (C0122O00000o.instance().OOo000() && retainZoom4 >= HybridZoomingSystem.getUltraTeleMinZoomRatio() && !CameraSettings.supportVideoSATForVideoQuality(i2)) {
                                            i3 = getUltraTeleCameraId();
                                            if (!DataRepository.dataItemConfig().getComponentConfigVideoQuality().isSupportVideoQuality(i2, i3)) {
                                                i3 = getMainBackCameraId();
                                            }
                                            Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                                            return i3;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    i3 = getVideoModeCameraId(i, i2);
                    Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                    return i3;
                }
                i3 = getUltraWideCameraId();
                Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
                return i3;
            }
            return getMainBackCameraId();
        } else if (i == 1) {
            if (i2 == 161 || i2 == 162) {
                if (CameraSettings.isVideoBokehOn() && getBokehFrontCameraId() != -1) {
                }
            } else if (i2 == 171) {
                if (getBokehFrontCameraId() != -1) {
                    boolean isIntentAction = DataRepository.dataItemGlobal().isIntentAction();
                    if (C0124O00000oO.OOooOoO()) {
                    }
                }
            }
            i3 = getBokehFrontCameraId();
            Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
            return i3;
        }
        i3 = i;
        Log.d(TAG, String.format(Locale.US, "getActualOpenCameraId: mode=%x, id=%d->%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(i3)}));
        return i3;
    }

    public synchronized int getAuxCameraId() {
        return this.mCamera2CompatAdapter.getAuxCameraId();
    }

    public synchronized int getAuxFrontCameraId() {
        return this.mCamera2CompatAdapter.getAuxFrontCameraId();
    }

    public synchronized int getBokehCameraId() {
        return this.mCamera2CompatAdapter.getBokehCameraId();
    }

    public synchronized int getBokehFrontCameraId() {
        return this.mCamera2CompatAdapter.getBokehFrontCameraId();
    }

    public SparseArray getCapabilities() {
        return this.mCamera2CompatAdapter.getCapabilities();
    }

    public synchronized CameraCapabilities getCapabilities(int i) {
        return this.mCamera2CompatAdapter.getCapabilities(i);
    }

    public synchronized CameraCapabilities getCapabilitiesByBogusCameraId(int i, int i2) {
        return getCapabilities(getActualOpenCameraId(i, i2));
    }

    public synchronized CameraCapabilities getCurrentCameraCapabilities() {
        return this.mCamera2CompatAdapter.getCurrentCameraCapabilities();
    }

    public ConcurrentHashMap getDefaultDualVideoCameraIds() {
        int i = SystemProperties.getInt("camera.dualvideo.firstid", -1);
        int i2 = SystemProperties.getInt("camera.dualvideo.secondid", -1);
        if (i == -1 || i2 == -1 || i == i2) {
            return this.mCamera2CompatAdapter.getDefaultDualVideoCameraIds();
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put(RenderSourceType.MAIN, Integer.valueOf(i));
        concurrentHashMap.put(RenderSourceType.SUB, Integer.valueOf(i2));
        return concurrentHashMap;
    }

    public synchronized int getFrontCameraId() {
        return this.mCamera2CompatAdapter.getFrontCameraId();
    }

    public synchronized int getMainBackCameraId() {
        return this.mCamera2CompatAdapter.getMainBackCameraId();
    }

    public int getMaxJpegSize() {
        return this.mCamera2CompatAdapter.getMaxJpegSize();
    }

    public int getParallelVirtualCameraId() {
        return this.mCamera2CompatAdapter.getParallelVirtualCameraId();
    }

    public synchronized int getRoleIdByActualId(int i) {
        return this.mCamera2CompatAdapter.getRoleIdByActualId(i);
    }

    public synchronized int getSATCameraId() {
        return this.mCamera2CompatAdapter.getSATCameraId();
    }

    public synchronized int getSATFrontCameraId() {
        return this.mCamera2CompatAdapter.getSATFrontCameraId();
    }

    public synchronized int getStandaloneMacroCameraId() {
        return this.mCamera2CompatAdapter.getStandaloneMacroCameraId();
    }

    public synchronized int getUltraTeleCameraId() {
        return this.mCamera2CompatAdapter.getUltraTeleCameraId();
    }

    public synchronized int getUltraWideBokehCameraId() {
        return this.mCamera2CompatAdapter.getUltraWideBokehCameraId();
    }

    public synchronized int getUltraWideCameraId() {
        return this.mCamera2CompatAdapter.getUltraWideCameraId();
    }

    public synchronized int getVideoSATCameraId() {
        return this.mCamera2CompatAdapter.getVideoSATCameraId();
    }

    public int getVirtualBackCameraId() {
        return this.mCamera2CompatAdapter.getVirtualBackCameraId();
    }

    public int getVirtualFrontCameraId() {
        return this.mCamera2CompatAdapter.getVirtualFrontCameraId();
    }

    public synchronized boolean hasBokehCamera() {
        return this.mCamera2CompatAdapter.hasBokehCamera();
    }

    public boolean hasPortraitCamera() {
        return this.mCamera2CompatAdapter.hasPortraitCamera();
    }

    public synchronized boolean hasSATCamera() {
        return this.mCamera2CompatAdapter.hasSATCamera();
    }

    public boolean hasTeleCamera() {
        return this.mCamera2CompatAdapter.hasTeleCamera();
    }

    public void init(CameraManager cameraManager) {
        this.mCamera2CompatAdapter.init(cameraManager);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0019, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean isFrontCameraId(int i) {
        CameraCapabilities capabilities = getCapabilities(i);
        boolean z = false;
        if (capabilities == null) {
            Log.d(TAG, "Warning: isFrontCameraId(): #init() failed.");
            return false;
        } else if (capabilities.getFacing() == 0) {
            z = true;
        }
    }

    public boolean isInitialized() {
        return this.mCamera2CompatAdapter.isInitialized();
    }

    public synchronized boolean isPartSAT() {
        return this.mCamera2CompatAdapter.isPartSAT();
    }

    public synchronized void reset() {
        this.mCamera2CompatAdapter.reset();
    }

    public synchronized void setCurrentOpenedCameraId(int i) {
        this.mCamera2CompatAdapter.setCurrentOpenedCameraId(i);
    }
}
