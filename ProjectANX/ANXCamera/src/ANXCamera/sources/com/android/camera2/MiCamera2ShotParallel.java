package com.android.camera2;

import android.graphics.Rect;
import android.hardware.camera2.CaptureResult;
import android.media.Image;
import android.util.Size;
import android.view.Surface;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import com.xiaomi.camera.base.CameraDeviceUtil;
import com.xiaomi.engine.BufferFormat;
import com.xiaomi.engine.GraphDescriptorBean;

public abstract class MiCamera2ShotParallel extends MiCamera2Shot {
    private static final String TAG = "ShotParallelBase";
    protected final Rect mActiveArraySize;
    protected CameraSize mAlgoSize;
    protected Size mLockedAlgoSize;
    protected CaptureResult mPreviewCaptureResult;

    MiCamera2ShotParallel(MiCamera2 miCamera2) {
        super(miCamera2);
        this.mActiveArraySize = miCamera2.getCapabilities().getActiveArraySize();
        CameraSize lockedAlgoSize = miCamera2.getCameraConfigs().getLockedAlgoSize();
        if (lockedAlgoSize != null) {
            this.mLockedAlgoSize = new Size(lockedAlgoSize.getWidth(), lockedAlgoSize.getHeight());
            StringBuilder sb = new StringBuilder();
            sb.append("lockedAlgoSize = ");
            sb.append(this.mLockedAlgoSize);
            Log.d(TAG, sb.toString());
        }
    }

    private boolean hasDualCamera() {
        return this.mMiCamera.getId() == Camera2DataContainer.getInstance().getSATFrontCameraId() || this.mMiCamera.getId() == Camera2DataContainer.getInstance().getBokehFrontCameraId() || this.mMiCamera.getId() == Camera2DataContainer.getInstance().getSATCameraId() || this.mMiCamera.getId() == Camera2DataContainer.getInstance().getBokehCameraId() || this.mMiCamera.getId() == Camera2DataContainer.getInstance().getUltraWideBokehCameraId();
    }

    /* access modifiers changed from: 0000 */
    public void configParallelSession(Size size) {
        GraphDescriptorBean graphDescriptorBean;
        int cameraCombinationMode = CameraDeviceUtil.getCameraCombinationMode(Camera2DataContainer.getInstance().getRoleIdByActualId(this.mMiCamera.getId()));
        if (ModuleManager.isPortraitModule()) {
            graphDescriptorBean = new GraphDescriptorBean(32770, hasDualCamera() ? 2 : 1, true, cameraCombinationMode);
        } else {
            graphDescriptorBean = ModuleManager.isProPhotoModule() ? new GraphDescriptorBean(32771, 1, true, cameraCombinationMode) : ModuleManager.isUltraPixel() ? new GraphDescriptorBean(33011, 1, true, cameraCombinationMode) : new GraphDescriptorBean(0, 1, true, cameraCombinationMode);
        }
        int width = size.getWidth();
        int height = size.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("configParallelSession:  streamNbr = ");
        sb.append(graphDescriptorBean.getStreamNumber());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("configParallelSession: ");
        sb3.append(width);
        sb3.append("x");
        sb3.append(height);
        Log.d(str, sb3.toString());
        AlgoConnector.getInstance().getLocalBinder().configCaptureSession(new BufferFormat(width, height, 35, graphDescriptorBean), this.mMiCamera.getBokehDepthSize());
        this.mAlgoSize = new CameraSize(width, height);
    }

    /* access modifiers changed from: 0000 */
    public void configParallelSession(Size size, int i) {
        GraphDescriptorBean graphDescriptorBean;
        int i2 = 2;
        if (ModuleManager.isPortraitModule()) {
            if (!hasDualCamera()) {
                i2 = 1;
            }
            graphDescriptorBean = new GraphDescriptorBean(32770, i2, true, i);
        } else {
            graphDescriptorBean = i == 516 ? new GraphDescriptorBean(0, 2, true, i) : new GraphDescriptorBean(0, 1, true, i);
        }
        if (this.mMiCamera.isFakeSatEnable() && this.mMiCamera.getFakeSatOutputSize() != null) {
            size = this.mMiCamera.getFakeSatOutputSize().toSizeObject();
        }
        int width = size.getWidth();
        int height = size.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("configParallelSession:  streamNbr = ");
        sb.append(graphDescriptorBean.getStreamNumber());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("configParallelSession: streamSize = ");
        sb3.append(width);
        sb3.append("x");
        sb3.append(height);
        Log.d(str, sb3.toString());
        AlgoConnector.getInstance().getLocalBinder().configCaptureSession(new BufferFormat(width, height, 35, graphDescriptorBean), this.mMiCamera.getBokehDepthSize());
        this.mAlgoSize = new CameraSize(width, height);
    }

    /* access modifiers changed from: protected */
    public Surface getMainCaptureSurface() {
        return this.mMiCamera.getMainCaptureSurface(this.mMiCamera.getSatMasterCameraId());
    }

    /* access modifiers changed from: protected */
    public boolean isIn3OrMoreSatMode() {
        return this.mMiCamera.isIn3OrMoreSatMode();
    }

    /* access modifiers changed from: protected */
    public boolean isInMultiSurfaceSatMode() {
        return this.mMiCamera.isInMultiSurfaceSatMode();
    }

    /* access modifiers changed from: protected */
    public boolean isSatFusionShotEnabled() {
        boolean isSatFusionShotSupported = this.mMiCamera.getCapabilities().isSatFusionShotSupported();
        StringBuilder sb = new StringBuilder();
        sb.append("SAT_FUSION_SHOT_SUPPORTED: ");
        sb.append(isSatFusionShotSupported);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        boolean z = false;
        if (!isSatFusionShotSupported) {
            return false;
        }
        CaptureResult captureResult = this.mPreviewCaptureResult;
        Byte b = captureResult == null ? null : (Byte) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.SAT_FUSION_SHOT_PIPELINE_READY);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("SAT_FUSION_SHOT_PIPELINE_READY: ");
        sb3.append(b);
        Log.d(str, sb3.toString());
        if (!(b == null || b.byteValue() == 0)) {
            boolean isSuperNightOn = CameraSettings.isSuperNightOn();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("SAT_FUSION_SUPER_NIGHT_SE_ENABLED: ");
            sb4.append(isSuperNightOn);
            Log.d(str, sb4.toString());
            if (isSuperNightOn) {
                return false;
            }
            boolean isNeedFlash = this.mMiCamera.getCameraConfigs().isNeedFlash();
            StringBuilder sb5 = new StringBuilder();
            sb5.append("SAT_FUSION_FLASH_NEEDED: ");
            sb5.append(isNeedFlash);
            Log.d(str, sb5.toString());
            if (isNeedFlash) {
                return false;
            }
            boolean isHDREnabled = this.mMiCamera.getCameraConfigs().isHDREnabled();
            StringBuilder sb6 = new StringBuilder();
            sb6.append("SAT_FUSION_HDR_NEEDED: ");
            sb6.append(isHDREnabled);
            Log.d(str, sb6.toString());
            if (isHDREnabled) {
                return false;
            }
            float zoomRatio = this.mMiCamera.getCameraConfigs().getZoomRatio();
            StringBuilder sb7 = new StringBuilder();
            sb7.append("SAT_FUSION_ZOOM_RATIO: ");
            sb7.append(zoomRatio);
            Log.d(str, sb7.toString());
            if (zoomRatio < 3.0f) {
                return false;
            }
            int id = this.mMiCamera.getId();
            StringBuilder sb8 = new StringBuilder();
            sb8.append("SAT_FUSION_ACTUAL_CAMERA_ID: ");
            sb8.append(id);
            Log.d(str, sb8.toString());
            if (id != Camera2DataContainer.getInstance().getSATCameraId()) {
                return false;
            }
            Surface mainCaptureSurface = getMainCaptureSurface();
            if (!(this.mMiCamera.getSATSubCameraIds() == null || mainCaptureSurface == null || mainCaptureSurface != this.mMiCamera.getTeleRemoteSurface() || this.mMiCamera.getUltraTeleRemoteSurface() == null)) {
                z = true;
            }
            StringBuilder sb9 = new StringBuilder();
            sb9.append("SAT_FUSION_T_UT_COMBINATION: ");
            sb9.append(z);
            Log.d(str, sb9.toString());
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void notifyResultData(Object obj) {
    }

    /* access modifiers changed from: protected */
    public void onImageReceived(Image image, int i) {
    }
}
