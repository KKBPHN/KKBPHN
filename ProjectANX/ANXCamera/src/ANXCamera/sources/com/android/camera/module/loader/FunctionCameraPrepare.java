package com.android.camera.module.loader;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.text.TextUtils;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentConfigBeauty;
import com.android.camera.data.data.config.ComponentConfigBokeh;
import com.android.camera.data.data.config.ComponentConfigFlash;
import com.android.camera.data.data.config.ComponentConfigGradienter;
import com.android.camera.data.data.config.ComponentConfigHdr;
import com.android.camera.data.data.config.ComponentConfigRatio;
import com.android.camera.data.data.config.ComponentConfigUltraWide;
import com.android.camera.data.data.config.ComponentConfigVideoQuality;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.data.data.runing.ComponentRunningColorEnhance;
import com.android.camera.data.data.runing.ComponentRunningLighting;
import com.android.camera.data.data.runing.ComponentRunningSubtitle;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera.dualvideo.recorder.MultiRecorderManager;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.permission.PermissionManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.StandaloneRecorderProtocol;
import io.reactivex.annotations.NonNull;

public class FunctionCameraPrepare extends Func1Base {
    private static final String TAG = "FunctionCameraPrepare";
    private BaseModule baseModule;
    private int mLastMode;
    private boolean mNeedReConfigureData;
    private int mResetType;

    public FunctionCameraPrepare(int i, int i2, int i3, boolean z, BaseModule baseModule2) {
        super(i);
        this.mLastMode = i2;
        this.mResetType = i3;
        this.mNeedReConfigureData = z;
        this.baseModule = baseModule2;
    }

    private boolean needKeepFlashForDualVideo() {
        StandaloneRecorderProtocol standaloneRecorderProtocol = (StandaloneRecorderProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(429);
        boolean z = false;
        if (standaloneRecorderProtocol == null) {
            return false;
        }
        MultiRecorderManager recorderManager = standaloneRecorderProtocol.getRecorderManager(null);
        if (recorderManager == null) {
            return false;
        }
        if (this.mLastMode == 204 && this.mTargetMode == 204 && recorderManager.isRecording()) {
            z = true;
        }
        return z;
    }

    private void reConfigFlash(DataItemConfig dataItemConfig, ProviderEditor providerEditor) {
        if (!needKeepFlashForDualVideo()) {
            ComponentConfigFlash componentFlash = dataItemConfig.getComponentFlash();
            ComponentConfigHdr componentHdr = dataItemConfig.getComponentHdr();
            String persistValue = componentFlash.getPersistValue(this.mLastMode);
            String persistValue2 = componentFlash.getPersistValue(this.mTargetMode);
            if (!componentFlash.isValidFlashValue(persistValue2)) {
                providerEditor.remove(componentFlash.getKey(this.mTargetMode));
            }
            if (this.mResetType != 7) {
                String str = "2";
                String str2 = "5";
                if (persistValue2.equals(str) || persistValue2.equals(str2)) {
                    providerEditor.remove(componentFlash.getKey(this.mTargetMode));
                    providerEditor.remove(componentHdr.getKey(this.mTargetMode));
                }
                if (persistValue.equals(str) || persistValue.equals(str2)) {
                    providerEditor.remove(componentFlash.getKey(this.mLastMode));
                    providerEditor.remove(componentHdr.getKey(this.mLastMode));
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:84:0x019e, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOo0Oo() != false) goto L_0x0191;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0044, code lost:
        if (r10 != 4) goto L_0x0049;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void reconfigureData() {
        boolean z;
        DataItemConfig dataItemConfig;
        int i;
        boolean z2;
        if (!this.mNeedReConfigureData) {
            DataRepository.dataItemConfig().editor().remove(CameraSettings.KEY_ZOOM).apply();
            return;
        }
        CameraSettings.upgradeGlobalPreferences();
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        DataItemConfig dataItemConfig2 = DataRepository.dataItemConfig();
        int lastCameraId = dataItemGlobal.getLastCameraId();
        ProviderEditor editor = dataItemConfig2.editor();
        ProviderEditor editor2 = dataItemGlobal.editor();
        ProviderEditor editor3 = DataRepository.dataItemLive().editor();
        if (lastCameraId != 1) {
            int i2 = this.mResetType;
            if (i2 != 3) {
            }
        }
        CameraSettings.resetRetainZoom();
        editor.remove(CameraSettings.KEY_EXPOSURE);
        reConfigFlash(dataItemConfig2, editor);
        String defaultWatermarkStr = CameraSettings.getDefaultWatermarkStr();
        String str = CameraSettings.KEY_CUSTOM_WATERMARK;
        if (TextUtils.equals(defaultWatermarkStr, dataItemGlobal.getString(str, defaultWatermarkStr))) {
            editor2.remove(str);
        }
        ComponentConfigRatio componentConfigRatio = dataItemConfig2.getComponentConfigRatio();
        int i3 = this.mTargetMode;
        if (i3 == 163 || i3 == 165 || i3 == 167 || i3 == 173 || i3 == 175 || i3 == 171) {
            String[] fullSupportRatioValues = componentConfigRatio.getFullSupportRatioValues();
            String persistValue = componentConfigRatio.getPersistValue(this.mTargetMode);
            int length = fullSupportRatioValues.length;
            int i4 = 0;
            while (true) {
                if (i4 >= length) {
                    z2 = false;
                    break;
                } else if (TextUtils.equals(fullSupportRatioValues[i4], persistValue)) {
                    z2 = true;
                    break;
                } else {
                    i4++;
                }
            }
            if (!z2 && (this.mTargetMode == 165 || !persistValue.equals(ComponentConfigRatio.RATIO_1X1))) {
                Log.d(TAG, "reconfigureData: clear DATA_CONFIG_RATIO");
                editor.remove("pref_camera_picturesize_key");
            }
        }
        if (this.mTargetMode == 167) {
            String string = CameraAppImpl.getAndroidContext().getString(R.string.pref_camera_iso_default);
            String str2 = CameraSettings.KEY_QC_ISO;
            String string2 = dataItemConfig2.getString(str2, string);
            int i5 = (C0122O00000o.instance().OOoO0o() || C0122O00000o.instance().OOo0oo0()) ? R.array.pref_camera_iso_entryvalues_new : R.array.pref_camera_iso_entryvalues;
            if (!Util.isStringValueContained((Object) string2, i5)) {
                editor.remove(str2);
            }
        }
        if (!C0124O00000oO.Oo00o00()) {
            editor.remove(CameraSettings.KEY_QC_FOCUS_POSITION);
            editor.remove(CameraSettings.KEY_QC_EXPOSURETIME);
        }
        if (!Util.isLabOptionsVisible()) {
            editor2.remove(CameraSettings.KEY_FACE_DETECTION).remove(CameraSettings.KEY_CAMERA_PORTRAIT_WITH_FACEBEAUTY).remove(CameraSettings.KEY_CAMERA_FACE_DETECTION_AUTO_HIDDEN).remove(CameraSettings.KEY_VIDEO_SHOW_FACE_VIEW).remove(CameraSettings.KEY_CAMERA_DUAL_ENABLE).remove(CameraSettings.KEY_CAMERA_DUAL_SAT_ENABLE).remove(CameraSettings.KEY_CAMERA_MFNR_SAT_ENABLE).remove(CameraSettings.KEY_CAMERA_SR_ENABLE);
        }
        String str3 = CameraSettings.KEY_ANTIBANDING;
        if (!Util.isValidValue(dataItemGlobal.getString(str3, "1"))) {
            editor2.remove(str3);
        }
        if (!C0124O00000oO.OOoo0o0()) {
            editor2.remove(CameraSettings.KEY_FINGERPRINT_CAPTURE);
        }
        int i6 = this.mResetType;
        if ((i6 == 4 || i6 == 2) && C0122O00000o.instance().OOOO0oo()) {
            editor.remove(CameraSettings.KEY_CAMERA_PIXEL_LENS);
        }
        switch (this.mResetType) {
            case 2:
            case 7:
                z = false;
                ComponentRunningLighting componentRunningLighting = dataItemRunning.getComponentRunningLighting();
                int i7 = this.mTargetMode;
                if (!componentRunningLighting.checkValueValid(i7, componentRunningLighting.getPersistValue(i7))) {
                    int i8 = this.mTargetMode;
                    componentRunningLighting.setComponentValue(i8, componentRunningLighting.getDefaultValue(i8));
                    break;
                }
                break;
            case 3:
            case 6:
                dataItemRunning.getComponentRunningDualVideo().reInit();
                resetFlash(dataItemConfig2.getComponentFlash(), editor);
                resetHdr(dataItemConfig2.getComponentHdr(), editor);
                resetBeautyTransientAndVideoPersist(dataItemConfig2.getComponentConfigBeauty(), editor);
                resetUltraWide(dataItemConfig2.getComponentConfigUltraWide(), editor);
                resetLensType(dataItemConfig2.getComponentConfigUltraWide(), dataItemConfig2.getManuallyDualLens(), editor);
                resetGradienter(dataItemConfig2.getComponentConfigGradienter(), editor);
                resetAIWatermark(dataItemRunning.getComponentRunningAIWatermark(), false);
                resetColorEnhance(dataItemRunning.getComponentRunningColorEnhance(), false);
                editor.remove(dataItemConfig2.getComponentConfigSlowMotion().getKey(this.mTargetMode));
                resetVideoQualityPartially(dataItemConfig2.getComponentConfigVideoQuality());
                editor2.remove(DataItemConfig.DATA_CONFIG_GIF);
                resetFastmotionPro(editor);
                dataItemRunning.getComponentRunningShine().clearArrayMap();
                if (dataItemGlobal.getCurrentCameraId() == 0) {
                    resetBeautyBackLevel(editor);
                    resetBeautyCaptureFigure(editor);
                    dataItemConfig = (DataItemConfig) DataRepository.provider().dataConfig(1);
                    z = false;
                } else {
                    z = false;
                    dataItemConfig = (DataItemConfig) DataRepository.provider().dataConfig(0);
                }
                ProviderEditor editor4 = dataItemConfig.editor();
                resetFlash(dataItemConfig.getComponentFlash(), editor4);
                resetHdr(dataItemConfig.getComponentHdr(), editor4);
                resetFrontBokenh(dataItemConfig.getComponentBokeh(), editor4);
                resetBeautyTransientAndVideoPersist(dataItemConfig.getComponentConfigBeauty(), editor4);
                resetVideoQualityPartially(dataItemConfig.getComponentConfigVideoQuality());
                resetBeautyVideoFront(dataItemConfig);
                editor4.apply();
                DataRepository.dataItemLive().clearAll();
                resetSubtitle(dataItemRunning.getComponentRunningSubtitle());
                if (C0122O00000o.instance().OOO0o0O() || C0122O00000o.instance().OOO0ooO()) {
                    editor3.remove(CameraSettings.KEY_LIVE_MUSIC_PATH).remove(CameraSettings.KEY_LIVE_MUSIC_HINT).remove(CameraSettings.KEY_LIVE_STICKER).remove(CameraSettings.KEY_LIVE_STICKER_NAME).remove(CameraSettings.KEY_LIVE_STICKER_HINT).remove(CameraSettings.KEY_LIVE_SPEED).remove(CameraSettings.KEY_LIVE_FILTER).remove("key_live_shrink_face_ratio").remove("key_live_enlarge_eye_ratio").remove("key_live_smooth_strength").remove(CameraSettings.KEY_LIVE_BEAUTY_STATUS);
                }
                if (C0122O00000o.instance().OOO0Oo0()) {
                    editor3.remove(CameraSettings.KEY_MIMOJI_INDEX).remove(CameraSettings.KEY_MIMOJI_PANNEL_STATE);
                }
                if (C0122O00000o.instance().OOoO0o()) {
                    editor2.remove(CameraSettings.KEY_CAMERA_PRO_VIDEO_LOG_FROMAT).remove(CameraSettings.KEY_CAMERA_PRO_HISTOGRAM);
                    break;
                }
                break;
            case 4:
            case 5:
                int i9 = this.mTargetMode;
                if (!(i9 == 161 || i9 == 162)) {
                    if (!(i9 == 166 || i9 == 167)) {
                        if (i9 != 169) {
                            if (i9 == 171) {
                                break;
                            } else if (i9 != 174) {
                            }
                        }
                    }
                    i = 0;
                    dataItemGlobal.setCameraIdTransient(i);
                    break;
                }
                i = dataItemGlobal.getCurrentCameraId();
                dataItemGlobal.setCameraIdTransient(i);
        }
        z = false;
        boolean OOO0o00 = C0122O00000o.instance().OOO0o00();
        if (this.mResetType == 4 && lastCameraId == dataItemGlobal.getCurrentCameraId()) {
            OOO0o00 = z;
        }
        if (OOO0o00) {
            editor.putBoolean(CameraSettings.KEY_LENS_DIRTY_DETECT_ENABLED, true);
        }
        editor.apply();
        editor2.apply();
        editor3.apply();
    }

    private void resetAIWatermark(ComponentRunningAIWatermark componentRunningAIWatermark, boolean z) {
        if (componentRunningAIWatermark != null) {
            componentRunningAIWatermark.resetAIWatermark(z);
        }
    }

    private void resetBeautyBackLevel(ProviderEditor providerEditor) {
        String[] strArr;
        for (String str : BeautyConstant.BEAUTY_CATEGORY_LEVEL) {
            providerEditor.remove(BeautyConstant.wrappedSettingKeyForCapture(str));
            providerEditor.remove(BeautyConstant.wrappedSettingKeyForVideo(str));
            providerEditor.remove(BeautyConstant.wrappedSettingKeyForPortrait(str));
            providerEditor.remove(BeautyConstant.wrappedSettingKeyForFun(str));
        }
    }

    private void resetBeautyCaptureFigure(ProviderEditor providerEditor) {
        for (String wrappedSettingKeyForCapture : BeautyConstant.BEAUTY_CATEGORY_BACK_FIGURE) {
            providerEditor.remove(BeautyConstant.wrappedSettingKeyForCapture(wrappedSettingKeyForCapture));
        }
    }

    private void resetBeautyTransientAndVideoPersist(ComponentConfigBeauty componentConfigBeauty, ProviderEditor providerEditor) {
        componentConfigBeauty.clearClosed();
        String persistValue = componentConfigBeauty.getPersistValue(162);
        String defaultValue = componentConfigBeauty.getDefaultValue(162);
        if (!TextUtils.equals(persistValue, defaultValue)) {
            providerEditor.putString(componentConfigBeauty.getKey(162), defaultValue);
        }
    }

    private void resetBeautyVideoFront(ProviderEditor providerEditor) {
        for (String wrappedSettingKeyForVideo : BeautyConstant.BEAUTY_CATEGORY_LEVEL) {
            providerEditor.remove(BeautyConstant.wrappedSettingKeyForVideo(wrappedSettingKeyForVideo));
        }
    }

    private void resetColorEnhance(ComponentRunningColorEnhance componentRunningColorEnhance, boolean z) {
        if (componentRunningColorEnhance != null) {
            componentRunningColorEnhance.reset(z);
        }
    }

    private void resetFastmotionPro(ProviderEditor providerEditor) {
        providerEditor.remove(CameraSettings.KEY_QC_FASTMOTION_PRO_ISO).remove(CameraSettings.KEY_QC_FASTMOTION_PRO_EXPOSURE_VALUE).remove(CameraSettings.KEY_QC_FASTMOTION_PRO_EXPOSURETIME).remove(CameraSettings.KEY_QC_FASTMOTION_PRO_FOCUS_POSITION).remove(CameraSettings.KEY_FASTMOTION_PRO_WHITE_BALANCE).remove(CameraSettings.KEY_QC_FASTMOTION_PRO_MANUAL_WHITEBALANCE_VALUE);
    }

    private void resetFlash(ComponentConfigFlash componentConfigFlash, ProviderEditor providerEditor) {
        componentConfigFlash.resetToDefault(providerEditor);
    }

    private void resetFrontBokenh(ComponentConfigBokeh componentConfigBokeh, ProviderEditor providerEditor) {
        if ("on".equals(componentConfigBokeh.getPersistValue(this.mTargetMode))) {
            componentConfigBokeh.setComponentValue(this.mTargetMode, "off");
        }
    }

    private void resetGradienter(ComponentConfigGradienter componentConfigGradienter, ProviderEditor providerEditor) {
        if (componentConfigGradienter != null) {
            componentConfigGradienter.resetToDefault(providerEditor);
        }
    }

    private void resetHdr(ComponentConfigHdr componentConfigHdr, ProviderEditor providerEditor) {
        String persistValue = componentConfigHdr.getPersistValue(this.mTargetMode);
        if (persistValue.equals("on") || persistValue.equals("normal")) {
            componentConfigHdr.setComponentValue(this.mTargetMode, "auto");
        }
    }

    private void resetLensType(ComponentConfigUltraWide componentConfigUltraWide, ComponentManuallyDualLens componentManuallyDualLens, ProviderEditor providerEditor) {
        if (componentConfigUltraWide != null && componentManuallyDualLens != null) {
            componentManuallyDualLens.resetLensType(componentConfigUltraWide, providerEditor);
        }
    }

    private void resetSubtitle(ComponentRunningSubtitle componentRunningSubtitle) {
        componentRunningSubtitle.clearArrayMap();
    }

    private void resetUltraWide(ComponentConfigUltraWide componentConfigUltraWide, ProviderEditor providerEditor) {
        if (componentConfigUltraWide != null) {
            componentConfigUltraWide.resetUltraWide(providerEditor);
        }
    }

    private void resetVideoQualityPartially(ComponentConfigVideoQuality componentConfigVideoQuality) {
        componentConfigVideoQuality.reset();
    }

    public NullHolder apply(@NonNull NullHolder nullHolder) {
        if (!nullHolder.isPresent()) {
            return NullHolder.ofNullable(null, 234);
        }
        if (!PermissionManager.checkCameraLaunchPermissions()) {
            return NullHolder.ofNullable(null, 229);
        }
        Camera camera = (Camera) nullHolder.get();
        if (camera.isFinishing()) {
            Log.d(TAG, "activity is finishing, the content of BaseModuleHolder is set to null");
            return NullHolder.ofNullable(null, 235);
        }
        camera.changeRequestOrientation();
        if (this.baseModule.isDeparted()) {
            return NullHolder.ofNullable(this.baseModule, 225);
        }
        reconfigureData();
        return NullHolder.ofNullable(this.baseModule);
    }
}
