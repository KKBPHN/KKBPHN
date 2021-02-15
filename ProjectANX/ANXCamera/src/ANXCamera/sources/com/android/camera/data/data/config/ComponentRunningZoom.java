package com.android.camera.data.data.config;

import com.android.camera.CameraSettings;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import java.util.List;

public class ComponentRunningZoom extends ComponentData {
    private static final String TAG = "ComponentConfigZoom";
    private int mActualCameraId;
    private int mCameraId;

    public ComponentRunningZoom(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    private final boolean isAuxCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getAuxCameraId();
    }

    private final boolean isBackCamera() {
        return this.mCameraId == 0;
    }

    private final boolean isUltraTeleCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getUltraTeleCameraId();
    }

    private final boolean isUltraWideBackCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId();
    }

    public String getComponentValue(int i) {
        return super.getComponentValue(i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0043, code lost:
        if (com.android.camera.CameraSettings.isSuperNightUWOpen(r4) == false) goto L_0x0045;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String getDefaultValue(int i) {
        float f;
        String str = "1.0";
        if (!isBackCamera() || !HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            return str;
        }
        if (i != 165) {
            if (i != 167) {
                if (i != 169) {
                    if (i != 177) {
                        if (i != 180) {
                            if (i != 186) {
                                if (i == 188) {
                                    f = HybridZoomingSystem.getMinimumOpticalZoomRatio(i);
                                    return Float.toString(f);
                                } else if (i != 173) {
                                    if (!(i == 174 || i == 183)) {
                                        if (i != 184) {
                                            switch (i) {
                                                case 161:
                                                case 162:
                                                    break;
                                                case 163:
                                                    break;
                                                default:
                                                    return str;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (CameraSettings.isUltraWideConfigOpen(i)) {
                    return str;
                }
                if (!CameraSettings.isAutoZoomEnabled(i) && (!CameraSettings.isSuperEISEnabled(i) || !isUltraWideBackCamera())) {
                    if (!CameraSettings.isMacroModeEnabled(i)) {
                        return str;
                    }
                    f = HybridZoomingSystem.sDefaultMacroOpticalZoomRatio;
                    return Float.toString(f);
                }
                f = HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR;
                return Float.toString(f);
            }
            if (!CameraSettings.isMacroModeEnabled(i)) {
                if (!isUltraWideBackCamera()) {
                    if (isAuxCamera()) {
                        f = HybridZoomingSystem.getTeleMinZoomRatio();
                    } else if (isUltraTeleCamera()) {
                        f = HybridZoomingSystem.getUltraTeleMinZoomRatio();
                    } else {
                        if (CameraSettings.isUltraPixelRearOn()) {
                            return str;
                        }
                        return Float.toString(1.0f);
                    }
                    return Float.toString(f);
                }
                f = HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR;
                return Float.toString(f);
            }
            f = HybridZoomingSystem.sDefaultMacroOpticalZoomRatio;
            return Float.toString(f);
        }
        if (!CameraSettings.isMacroModeEnabled(i)) {
            boolean isUltraPixelRearOn = CameraSettings.isUltraPixelRearOn();
            return str;
        }
        f = HybridZoomingSystem.sDefaultMacroOpticalZoomRatio;
        return Float.toString(f);
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        return null;
    }

    public String getKey(int i) {
        return "pref_camera_zoom_retain_key";
    }

    public void reInit(int i, int i2) {
        this.mCameraId = i2;
        this.mActualCameraId = Camera2DataContainer.getInstance().getActualOpenCameraId(i2, i);
    }

    public void reset(int i) {
        setComponentValue(i, getDefaultValue(i));
    }

    public void setComponentValue(int i, String str) {
        super.setComponentValue(i, str);
    }
}
