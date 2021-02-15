package com.android.camera.module.impl.component;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import com.android.camera.ActivityBase;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraPreferenceActivity;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.ModeEditorActivity;
import com.android.camera.MutexModeManager;
import com.android.camera.R;
import com.android.camera.ThermalDetector;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.aiwatermark.chain.AbstractPriorityChain;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.constant.EyeLightConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.config.ComponentConfigAi;
import com.android.camera.data.data.config.ComponentConfigBeauty;
import com.android.camera.data.data.config.ComponentConfigFilter;
import com.android.camera.data.data.config.ComponentConfigFlash;
import com.android.camera.data.data.config.ComponentConfigHdr;
import com.android.camera.data.data.config.ComponentConfigLiveShot;
import com.android.camera.data.data.config.ComponentConfigMeter;
import com.android.camera.data.data.config.ComponentConfigRatio;
import com.android.camera.data.data.config.ComponentConfigRaw;
import com.android.camera.data.data.config.ComponentConfigSlowMotion;
import com.android.camera.data.data.config.ComponentConfigSlowMotionQuality;
import com.android.camera.data.data.config.ComponentConfigVideoQuality;
import com.android.camera.data.data.config.ComponentConfigVideoSubFPS;
import com.android.camera.data.data.config.ComponentConfigVideoSubQuality;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.config.ComponentManuallyET;
import com.android.camera.data.data.config.ComponentManuallyEV;
import com.android.camera.data.data.config.ComponentManuallyFocus;
import com.android.camera.data.data.config.ComponentManuallyISO;
import com.android.camera.data.data.config.ComponentManuallyWB;
import com.android.camera.data.data.config.ComponentRunningMacroMode;
import com.android.camera.data.data.config.ComponentRunningUltraPixel;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.android.camera.data.data.extra.ComponentLiveVideoQuality;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.data.data.runing.ComponentRunningAiAudio;
import com.android.camera.data.data.runing.ComponentRunningColorEnhance;
import com.android.camera.data.data.runing.ComponentRunningEisPro;
import com.android.camera.data.data.runing.ComponentRunningEyeLight;
import com.android.camera.data.data.runing.ComponentRunningFastMotionDuration;
import com.android.camera.data.data.runing.ComponentRunningFastMotionSpeed;
import com.android.camera.data.data.runing.ComponentRunningMoon;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.data.data.runing.ComponentRunningTiltValue;
import com.android.camera.data.data.runing.ComponentRunningTimer;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.data.observeable.FilmDreamProcessing;
import com.android.camera.data.observeable.VMFeature.FeatureModule;
import com.android.camera.data.observeable.VMProcessing;
import com.android.camera.dualvideo.ModuleUtil;
import com.android.camera.dualvideo.recorder.RecordType;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FilterInfo;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.ShineHelper;
import com.android.camera.fragment.clone.Config;
import com.android.camera.fragment.film.FilmItem;
import com.android.camera.fragment.manually.ManuallyListener;
import com.android.camera.fragment.settings.CameraPreferenceFragment;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.fragment.vv.FragmentVVPreview;
import com.android.camera.fragment.vv.FragmentVVWorkspace;
import com.android.camera.fragment.vv.VVItem;
import com.android.camera.fragment.vv.VVWorkspaceItem;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.Camera2Module;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.VideoBase;
import com.android.camera.module.VideoModule;
import com.android.camera.module.interceptor.ConfigChangeInterceptor;
import com.android.camera.module.loader.StartControl;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AIWatermarkDetect;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.AmbilightSelector;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CloneAction;
import com.android.camera.protocol.ModeProtocol.CloneChooser;
import com.android.camera.protocol.ModeProtocol.CloneProcess;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DollyZoomProcess;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.FastMotionProtocol;
import com.android.camera.protocol.ModeProtocol.FastmotionProAdjust;
import com.android.camera.protocol.ModeProtocol.FilmDreamProcess;
import com.android.camera.protocol.ModeProtocol.IDCardModeProtocol;
import com.android.camera.protocol.ModeProtocol.KaleidoscopeProtocol;
import com.android.camera.protocol.ModeProtocol.LensProtocol;
import com.android.camera.protocol.ModeProtocol.LiveVVChooser;
import com.android.camera.protocol.ModeProtocol.LiveVVProcess;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.ManuallyAdjust;
import com.android.camera.protocol.ModeProtocol.MasterFilterProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import com.android.camera.protocol.ModeProtocol.OnShineChangedProtocol;
import com.android.camera.protocol.ModeProtocol.PanoramaProtocol;
import com.android.camera.protocol.ModeProtocol.SpeechShutterDetect;
import com.android.camera.protocol.ModeProtocol.StandaloneRecorderProtocol;
import com.android.camera.protocol.ModeProtocol.SubtitleRecording;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.WatermarkProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.AIWatermark;
import com.android.camera.statistic.MistatsConstants.AlgoAttr;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.CUSTOMIZE_CAMERA;
import com.android.camera.statistic.MistatsConstants.CaptureAttr;
import com.android.camera.statistic.MistatsConstants.CaptureSence;
import com.android.camera.statistic.MistatsConstants.CloneAttr;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import com.android.camera.statistic.MistatsConstants.FilmAttr;
import com.android.camera.statistic.MistatsConstants.MacroAttr;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.PortraitAttr;
import com.android.camera.statistic.MistatsConstants.ProColor;
import com.android.camera.statistic.MistatsConstants.SuperMoon;
import com.android.camera.statistic.MistatsConstants.VideoAttr;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.timerburst.TimerBurstController;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.CameraCapabilities;
import com.xiaomi.fenshen.FenShenCam.Mode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ConfigChangeImpl implements ConfigChanges {
    private static final String TAG = "ConfigChangeImpl";
    private ActivityBase mActivity;
    private AbstractPriorityChain mChain = null;
    private ConfigChangeInterceptor mChangeInterceptor;
    private int[] mRecordingClosedElements;

    public ConfigChangeImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
    }

    static /* synthetic */ void O000000o(int[] iArr, BaseModule baseModule) {
        baseModule.updatePreferenceTrampoline(iArr);
        Camera2Proxy cameraDevice = baseModule.getCameraDevice();
        if (cameraDevice != null) {
            cameraDevice.resumePreview();
        }
    }

    static /* synthetic */ void O00000Oo(boolean z, BaseModule baseModule) {
        if (baseModule instanceof Camera2Module) {
            Camera2Module camera2Module = (Camera2Module) baseModule;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("(moon_mode) config moon:");
            sb.append(z);
            Log.u(str, sb.toString());
            camera2Module.updateMoon(z);
        }
    }

    static /* synthetic */ void O00000o0(boolean z, BaseModule baseModule) {
        if ((baseModule instanceof Camera2Module) && z) {
            ((Camera2Module) baseModule).closeMoonMode(0, 8);
        }
    }

    static /* synthetic */ void O0000Oo0(BaseModule baseModule) {
        if (baseModule instanceof Camera2Module) {
            Camera2Module camera2Module = (Camera2Module) baseModule;
            Log.u(TAG, "configMoonBacklight");
            camera2Module.updateBacklight();
        }
    }

    static /* synthetic */ void O0000Ooo(BaseModule baseModule) {
        if (baseModule instanceof Camera2Module) {
            Camera2Module camera2Module = (Camera2Module) baseModule;
            Log.u(TAG, "configNearRangeMode");
            camera2Module.updateNearRangeMode(false, true);
        }
        HashMap hashMap = new HashMap();
        hashMap.put(CaptureAttr.PARAM_NEAR_RANGE_MODE, CaptureAttr.VALUE_CLOSE_NEAR_RANGE_MODE);
        MistatsWrapper.mistatEvent(FeatureName.KEY_COMMON_TIPS, hashMap);
    }

    static /* synthetic */ void O0000o0O(BaseModule baseModule) {
        if (baseModule instanceof Camera2Module) {
            Camera2Module camera2Module = (Camera2Module) baseModule;
            Log.u(TAG, "configSilhouette");
            camera2Module.updateSilhouette();
        }
    }

    private void applyConfig(int i, int i2) {
        if (i == 161) {
            showOrHideMimoji();
        } else if (i == 162) {
            configGif(i2);
        } else if (i != 195) {
            if (i != 196) {
                switch (i) {
                    case 164:
                        getBaseModule().ifPresent(new C0392O00000oo(this));
                        return;
                    case 165:
                        configSuperEISPro();
                        return;
                    case 166:
                        configIDCard();
                        return;
                    case 167:
                        configNearRangeMode();
                        return;
                    case 168:
                        configAiAudio();
                        return;
                    case 169:
                        configPanoramaDirection();
                        return;
                    case 170:
                        configTimerBurst(i2);
                        return;
                    case 171:
                        configAi108();
                        return;
                    case 172:
                        configIntoWorkspace();
                        return;
                    case 175:
                        configAiEnhancedVideo(i2);
                        return;
                    case 179:
                        configCloneUseGuide();
                        return;
                    case 199:
                        configFocusPeakSwitch(i2);
                        return;
                    case 201:
                        configAiSceneSwitch(i2);
                        return;
                    case 203:
                        showOrHideLighting(true);
                        return;
                    case 209:
                        configSwitchUltraPixel(i2);
                        return;
                    case 212:
                        break;
                    case 223:
                        configAIWatermark(i2);
                        return;
                    case 225:
                        showSetting();
                        return;
                    case 243:
                        configVideoBokehSwitch(i2);
                        return;
                    case 255:
                        configMacroMode();
                        return;
                    case 258:
                        configExposureFeedbackSwitch(i2);
                        return;
                    case 512:
                        configRemoteCamera();
                        return;
                    case 513:
                        configMultiCamReselect();
                        return;
                    default:
                        switch (i) {
                            case 205:
                                configSwitchUltraWide();
                                return;
                            case 206:
                                configLiveShotSwitch(i2);
                                return;
                            case 207:
                                configSwitchUltraWideBokeh();
                                return;
                            default:
                                switch (i) {
                                    case 215:
                                        configUltraPixelPortrait(i2);
                                        return;
                                    case 216:
                                        configVV();
                                        return;
                                    case 217:
                                        configBack();
                                        return;
                                    case 218:
                                        configSuperEIS();
                                        return;
                                    case 219:
                                        configReferenceLineSwitch(i2);
                                        return;
                                    case 220:
                                        configVideoSubtitle();
                                        return;
                                    case 221:
                                        configDocumentMode(i2);
                                        return;
                                    default:
                                        switch (i) {
                                            case 227:
                                                configColorEnhance(i2);
                                                return;
                                            case 228:
                                                configTiltSwitch(i2);
                                                return;
                                            case 229:
                                                configGradienterSwitch(i2);
                                                return;
                                            case 230:
                                                configHHTSwitch(i2);
                                                return;
                                            case 231:
                                                configMagicFocusSwitch();
                                                return;
                                            default:
                                                switch (i) {
                                                    case 233:
                                                        configVideoFast();
                                                        return;
                                                    case 234:
                                                        beautyMutexHandle();
                                                        configScene(i2);
                                                        return;
                                                    case 235:
                                                        configGroupSwitch(i2);
                                                        return;
                                                    case 236:
                                                        configMagicMirrorSwitch(i2);
                                                        return;
                                                    case 237:
                                                        configRawSwitch(i2);
                                                        return;
                                                    case 238:
                                                        configGenderAgeSwitch(i2);
                                                        return;
                                                    case 239:
                                                        break;
                                                    case 240:
                                                        configDualWaterMarkSwitch();
                                                        return;
                                                    case 241:
                                                        configSuperResolutionSwitch(i2);
                                                        return;
                                                    default:
                                                        switch (i) {
                                                            case 246:
                                                                configMoon(true);
                                                                return;
                                                            case 247:
                                                                configMoonNight();
                                                                return;
                                                            case 248:
                                                                configSilhouette();
                                                                return;
                                                            case 249:
                                                                configMoonBacklight();
                                                                return;
                                                            default:
                                                                switch (i) {
                                                                    case 251:
                                                                        configCinematicAspectRatio(i2);
                                                                        return;
                                                                    case 252:
                                                                        configSwitchHandGesture();
                                                                        return;
                                                                    case 253:
                                                                        configAutoZoom();
                                                                        return;
                                                                    default:
                                                                        switch (i) {
                                                                            case 260:
                                                                                configVideoLogSwitch(i2);
                                                                                return;
                                                                            case 261:
                                                                                configRGBHistogramSwitch(i2);
                                                                                return;
                                                                            case 262:
                                                                                configSpeechShutter();
                                                                                return;
                                                                            case 263:
                                                                                showOrHideMasterFilter();
                                                                                return;
                                                                            default:
                                                                                return;
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
            }
            showOrHideShine();
        } else {
            configPortraitSwitch(i2);
        }
    }

    private void applyConfigValue(int i, String str) {
        if (i == 173) {
            configVideoSubQuality(str);
        } else if (i == 174) {
            configVideoSubFps(str);
        } else if (i == 187) {
            configLiveVideoQuality(str);
        } else if (i == 204) {
            configFPS960(str);
        } else if (i == 208) {
            configVideoQuality(str);
        } else if (i == 210) {
            configRatio(false, str);
        } else if (i == 213) {
            configSlowQuality(str);
        } else if (i == 226) {
            configTimerSwitch(str);
        } else if (i == 228) {
            configTilt(str);
        } else if (i == 246) {
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 3357441) {
                if (hashCode == 104817688 && str.equals(ComponentRunningMoon.NIGHT)) {
                    c = 1;
                }
            } else if (str.equals(ComponentRunningMoon.MOON)) {
                c = 0;
            }
            if (c == 0) {
                configMoon(true);
            } else if (c == 1) {
                configMoonNight();
            }
        } else if (i == 221) {
            configDocumentModeValue(str);
        } else if (i == 222) {
            configDualVideo(str);
        }
    }

    private void beautyMutexHandle() {
    }

    private void changeMode(int i) {
        DataRepository.dataItemGlobal().setCurrentMode(i);
        ActivityBase activityBase = this.mActivity;
        if (activityBase != null) {
            activityBase.onModeSelected(StartControl.create(i).setViewConfigType(2).setNeedBlurAnimation(true).setNeedReConfigureCamera(true));
            return;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("ignore changeMode ");
        sb.append(i);
        Log.d(str, sb.toString());
    }

    private void changeModeWithoutConfigureData(int i, boolean z) {
        ActivityBase activityBase = this.mActivity;
        int i2 = 2;
        StartControl viewConfigType = StartControl.create(i).setViewConfigType(2);
        if (!z) {
            i2 = 7;
        }
        activityBase.onModeSelected(viewConfigType.setResetType(i2).setNeedBlurAnimation(true).setNeedReConfigureCamera(true));
    }

    private void clearToast() {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.directlyHideTips();
            bottomPopupTips.setPortraitHintVisible(8);
            bottomPopupTips.hideLeftTipImage();
            bottomPopupTips.hideRightTipImage();
            bottomPopupTips.hideCenterTipImage();
            bottomPopupTips.directHideLyingDirectHint();
            bottomPopupTips.reConfigQrCodeTip();
        }
    }

    private boolean closeVideoFast() {
        DataItemGlobal dataItemGlobal = (DataItemGlobal) DataRepository.provider().dataGlobal();
        if (dataItemGlobal.getCurrentMode() != 169) {
            return false;
        }
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        dataItemGlobal.setCurrentMode(162);
        dataItemRunning.switchOff("pref_video_speed_fast_key");
        return true;
    }

    private void configAIWatermark(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            int i2 = 163;
            if (moduleIndex == 163 || moduleIndex == 165 || moduleIndex == 188) {
                ComponentRunningAIWatermark componentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
                boolean aIWatermarkEnable = componentRunningAIWatermark.getAIWatermarkEnable();
                if (i != 3 || aIWatermarkEnable) {
                    setTipsState("ai_watermark", true);
                    boolean z = !aIWatermarkEnable;
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("configAIWatermark: ");
                    sb.append(z);
                    Log.u(str, sb.toString());
                    if (z) {
                        configDocumentMode(3);
                        ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
                        if (componentRunningMacroMode.isSwitchOn(moduleIndex)) {
                            componentRunningMacroMode.setSwitchOff(moduleIndex);
                        }
                        watermarkToast();
                        configTimerBurst(3);
                    }
                    componentRunningAIWatermark.setAIWatermarkEnable(z);
                    componentRunningAIWatermark.resetAIWatermark(z);
                    setWatermarkEnable(z);
                    if (1 == i) {
                        CameraStatUtils.trackAIWatermarkClick(z ? AIWatermark.AI_WATERMARK_OPEN : AIWatermark.AI_WATERMARK_CLOSE);
                    }
                    updateASDForWatermark();
                    componentRunningAIWatermark.setEnable(CameraSettings.getBogusCameraId(), z);
                    if (!z && TextUtils.equals(DataRepository.dataItemConfig().getComponentConfigRatio().getComponentValue(165), ComponentConfigRatio.RATIO_1X1)) {
                        i2 = 165;
                    }
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("newMode=");
                    sb2.append(i2);
                    Log.d(str2, sb2.toString());
                    DataRepository.dataItemGlobal().setCurrentMode(i2);
                    this.mActivity.onModeSelected(StartControl.create(i2).setViewConfigType(2).setResetType(7).setNeedBlurAnimation(true).setNeedReConfigureCamera(true));
                }
            }
        }
    }

    private void configAiAudio() {
        boolean z;
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            int i = 0;
            if (CameraSettings.isMacroModeEnabled(moduleIndex)) {
                DataRepository.dataItemRunning().getComponentRunningMacroMode().setSwitchOff(moduleIndex);
                z = true;
            } else {
                z = false;
            }
            if (CameraSettings.isAutoZoomEnabled(moduleIndex)) {
                CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                z = true;
            }
            if (moduleIndex == 180) {
                ComponentManuallyDualLens manuallyDualLens = DataRepository.dataItemConfig().getManuallyDualLens();
                if (manuallyDualLens != null && TextUtils.equals(manuallyDualLens.getComponentValue(moduleIndex), "macro")) {
                    manuallyDualLens.setComponentValue(moduleIndex, ComponentManuallyDualLens.LENS_WIDE);
                    z = true;
                }
            }
            if (z) {
                setTipsState(FragmentTopAlert.TIP_AI_AUDIO, true);
                changeMode(moduleIndex);
                return;
            }
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                ComponentRunningAiAudio componentRunningAiAudio = DataRepository.dataItemRunning().getComponentRunningAiAudio();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configAiAudio: ");
                sb.append(componentRunningAiAudio.getComponentValue(moduleIndex));
                Log.u(str, sb.toString());
                int currentStringRes = componentRunningAiAudio.getCurrentStringRes(moduleIndex);
                if (currentStringRes == -1) {
                    i = 8;
                }
                topAlert.alertAiAudioBGHint(i, currentStringRes);
            }
        }
    }

    private void configAiEnhancedVideo(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            boolean isAiEnhancedVideoEnabled = CameraSettings.isAiEnhancedVideoEnabled(moduleIndex);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configAiEnhancedVideo: ");
            sb.append(!isAiEnhancedVideoEnabled);
            Log.u(str, sb.toString());
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            String str2 = VideoAttr.PARAM_AI_ENHANCED_VIDEO;
            if (isAiEnhancedVideoEnabled) {
                CameraSettings.setAiEnhancedVideoEnabled(moduleIndex, false);
                topAlert.updateConfigItem(175);
                CameraStatUtils.trackVideoCommonClickB(str2, false);
            } else {
                CameraSettings.setAiEnhancedVideoEnabled(moduleIndex, true);
                topAlert.updateConfigItem(175);
                CameraStatUtils.trackVideoCommonClickB(str2, true);
                switchOffElementsSilent(216);
                closeVideoFast();
                singeSwitchVideoBeauty(false);
                CameraSettings.setVideoMasterFilter(0);
                CameraSettings.setSuperEISEnabled(moduleIndex, false);
                CameraSettings.setVideoQuality8KOff(moduleIndex);
                CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                resetVideoFilter();
                MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                    miBeautyProtocol.dismiss(3);
                }
                MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
                if (masterFilterProtocol != null && masterFilterProtocol.isShowing()) {
                    masterFilterProtocol.dismiss(2, 5);
                }
                if (CameraSettings.isVhdrOn(((BaseModule) baseModule.get()).getCameraCapabilities(), moduleIndex)) {
                    updateComponentHdr(true);
                }
            }
            if (moduleIndex == 204) {
                DataRepository.dataItemGlobal().setCurrentMode(162);
            }
            changeMode(162);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            bottomPopupTips.updateLeftTipImage();
            bottomPopupTips.updateTipImage();
        }
    }

    private void configAiSceneSwitch(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            boolean aiSceneOpen = CameraSettings.getAiSceneOpen(moduleIndex);
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (i == 1) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configAiSceneSwitch: ");
                sb.append(!aiSceneOpen);
                Log.u(str, sb.toString());
                String str2 = AlgoAttr.VAULE_AI_CC;
                String str3 = AlgoAttr.VAULE_AI_SCENE;
                String str4 = "ai";
                if (!aiSceneOpen) {
                    topAlert.alertSwitchTip(str4, 0, (int) R.string.pref_camera_front_ai_scene_entry_on);
                    CameraSettings.setAiSceneOpen(moduleIndex, true);
                    if (EffectController.getInstance().getAiColorCorrectionVersion() >= 1) {
                        MistatsWrapper.commonKeyTriggerEvent(str2, Boolean.valueOf(true), null);
                    } else {
                        MistatsWrapper.commonKeyTriggerEvent(str3, Boolean.valueOf(true), null);
                    }
                } else {
                    topAlert.alertSwitchTip(str4, 8, (int) R.string.pref_camera_front_ai_scene_entry_off);
                    CameraSettings.setAiSceneOpen(moduleIndex, false);
                    if (EffectController.getInstance().getAiColorCorrectionVersion() >= 1) {
                        MistatsWrapper.commonKeyTriggerEvent(str2, Boolean.valueOf(false), null);
                    } else {
                        MistatsWrapper.commonKeyTriggerEvent(str3, Boolean.valueOf(false), null);
                    }
                    BaseModule baseModule2 = (BaseModule) baseModule.get();
                    if (baseModule2 != null && (baseModule2 instanceof Camera2Module)) {
                        ((Camera2Module) baseModule2).closeMoonMode(0, 8);
                    }
                }
                topAlert.updateConfigItem(201);
                if (CameraSettings.isGroupShotOn()) {
                    ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).configGroupSwitch(4);
                    topAlert.refreshExtraMenu();
                }
            } else if (i != 2 && i == 3) {
                Log.u(TAG, "configAiSceneSwitch: MUTEX false");
                CameraSettings.setAiSceneOpen(moduleIndex, false);
                topAlert.updateConfigItem(201);
            }
            ((BaseModule) baseModule.get()).updatePreferenceTrampoline(36);
            Camera2Proxy cameraDevice = ((BaseModule) baseModule.get()).getCameraDevice();
            if (cameraDevice != null) {
                cameraDevice.resumePreview();
            }
            if (i == 1 && CameraSettings.isUltraPixelOn()) {
                configSwitchUltraPixel(3);
            }
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.reConfigQrCodeTip();
            }
            if (i == 1 && CameraSettings.isUltraPixelPortraitFrontOn()) {
                configUltraPixelPortrait(3);
            }
        }
    }

    private void configAutoZoom() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                setTipsState(FragmentTopAlert.TIP_AUTO_ZOOM, true);
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                boolean isAutoZoomEnabled = CameraSettings.isAutoZoomEnabled(moduleIndex);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configAutoZoom: ");
                sb.append(!isAutoZoomEnabled);
                Log.u(str, sb.toString());
                CameraSettings.resetRetainZoom();
                if (isAutoZoomEnabled) {
                    CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                    topAlert.updateConfigItem(253);
                } else {
                    CameraSettings.setAutoZoomEnabled(moduleIndex, true);
                    topAlert.updateConfigItem(253);
                    switchOffElementsSilent(216);
                    closeVideoFast();
                    singeSwitchVideoBeauty(false);
                    CameraSettings.setVideoMasterFilter(0);
                    CameraSettings.setSuperEISEnabled(moduleIndex, false);
                    CameraSettings.setSubtitleEnabled(moduleIndex, false);
                    CameraSettings.setGradienterOn(false);
                    CameraSettings.setCinematicAspectRatioEnabled(moduleIndex, false);
                    CameraSettings.setVideoQuality8KOff(moduleIndex);
                    DataRepository.dataItemRunning().getComponentRunningAiAudio().setComponentValue(moduleIndex, "normal");
                    CameraSettings.setAiEnhancedVideoEnabled(moduleIndex, false);
                    if (CameraSettings.isVhdrOn(((BaseModule) baseModule.get()).getCameraCapabilities(), moduleIndex)) {
                        updateComponentHdr(true);
                    }
                    MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                    if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                        miBeautyProtocol.dismiss(3);
                    }
                    MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
                    if (masterFilterProtocol != null && masterFilterProtocol.isShowing()) {
                        masterFilterProtocol.dismiss(2, 5);
                    }
                }
                ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
                if (componentRunningMacroMode.isSwitchOn(moduleIndex)) {
                    componentRunningMacroMode.setSwitchOff(moduleIndex);
                }
                if (moduleIndex == 204) {
                    DataRepository.dataItemGlobal().setCurrentMode(162);
                }
                changeModeWithoutConfigureData(162, false);
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                bottomPopupTips.updateLeftTipImage();
                bottomPopupTips.updateTipImage();
            }
        }
    }

    private void configBack() {
        Log.u(TAG, "configBack");
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex == 179) {
                configVVBack();
            } else if (moduleIndex == 182) {
                configIDCardBack();
            } else if (moduleIndex == 185) {
                configCloneModeBack();
            } else if (moduleIndex != 189) {
                if (!(moduleIndex == 207 || moduleIndex == 208)) {
                    if (moduleIndex == 212) {
                        configFilmDreamBack();
                    } else if (moduleIndex == 213) {
                        CameraStatUtils.trackFilmTimeFreezeClick(FilmAttr.VALUE_TIME_FREEZE_CLICK_EXIT_PREVIEW);
                    }
                }
                configFilm(null, false, false);
            } else {
                configDollyZoomBack();
            }
        }
    }

    private void configCloneModeBack() {
        Log.u(TAG, "configCloneModeBack");
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null) {
            CameraStatUtils.trackCloneClick(CloneAttr.VALUE_BACK_CLICK);
            cloneProcess.showExitConfirm(true);
        }
    }

    private void configCloneUseGuide() {
        String str;
        int i;
        CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
        if (cloneAction != null) {
            cloneAction.onCloneGuideClicked();
        }
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            Log.u(TAG, "configCloneUseGuide");
            BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex != 185) {
                if (moduleIndex == 189) {
                    CameraStatUtils.trackFilmUseGuideClick();
                    i = 39;
                } else if (moduleIndex == 207) {
                    CameraStatUtils.trackFilmUseGuideClick();
                    i = 35;
                } else if (moduleIndex == 213) {
                    CameraStatUtils.trackFilmUseGuideClick();
                    i = 38;
                }
                baseDelegate.delegateEvent(i);
            } else {
                baseDelegate.delegateEvent(23);
                if (Config.getCloneMode() == Mode.PHOTO) {
                    str = CloneAttr.VALUE_PHOTO_GUIDE_CLICK;
                } else if (Config.getCloneMode() == Mode.VIDEO) {
                    str = CloneAttr.VALUE_VIDEO_GUIDE_CLICK;
                } else if (Config.getCloneMode() == Mode.MCOPY) {
                    str = CloneAttr.VALUE_FREEZE_FRAME_GUIDE_CLICK;
                }
                CameraStatUtils.trackCloneClick(str);
            }
        }
    }

    private void configColorEnhance(int i) {
        String str;
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            ComponentRunningColorEnhance componentRunningColorEnhance = DataRepository.dataItemRunning().getComponentRunningColorEnhance();
            boolean isEnabled = componentRunningColorEnhance.isEnabled(moduleIndex);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configColorEnhance: ");
            sb.append(!isEnabled);
            Log.u(str2, sb.toString());
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (i == 1) {
                if (isEnabled) {
                    componentRunningColorEnhance.setEnabled(false, 1);
                    topAlert.alertProColourHint(8, R.string.pro_color_mode);
                    str = ProColor.VALUE_CLOSE;
                } else {
                    componentRunningColorEnhance.setEnabled(true, 1);
                    topAlert.alertProColourHint(0, R.string.pro_color_mode);
                    str = ProColor.VALUE_OPEN;
                }
                CameraStatUtils.trackProColorClick(str);
            }
            topAlert.updateConfigItem(227);
            ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(74);
        }
    }

    private void configDocumentMode(int i) {
        if (C0122O00000o.instance().OO0ooo0() || C0122O00000o.instance().OO0ooo()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                    boolean isDocumentModeOn = CameraSettings.isDocumentModeOn(moduleIndex);
                    if (i == 1) {
                        String str = "p";
                        if (isDocumentModeOn) {
                            CameraSettings.setDocumentModeOn(false);
                            restoreAllMutexElement(str);
                            isDocumentModeOn = false;
                        } else {
                            CameraSettings.setDocumentModeOn(true);
                            closeMutexElement(str, 196);
                            configLiveShotSwitch(3);
                            configTiltSwitch(3);
                            if (CameraSettings.isUltraPixelOn()) {
                                configSwitchUltraPixel(3);
                            }
                            isDocumentModeOn = true;
                        }
                        String str2 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("configDocumentMode: ");
                        sb.append(isDocumentModeOn);
                        Log.u(str2, sb.toString());
                    } else if (i != 2 && i == 3) {
                        if (isDocumentModeOn) {
                            Log.u(TAG, "configDocumentMode: MUTEX false");
                            CameraSettings.setDocumentModeOn(false);
                            isDocumentModeOn = false;
                        } else {
                            return;
                        }
                    }
                    if (isDocumentModeOn) {
                        topAlert.alertSlideSwitchLayout(true, 221);
                        getBaseModule().ifPresent(O000000o.INSTANCE);
                    } else {
                        topAlert.alertSlideSwitchLayout(false, 221);
                    }
                    CameraStatUtils.trackDocumentModeChanged(CameraStatUtils.getDocumentModeValue(moduleIndex));
                }
            }
        }
    }

    private void configDocumentModeValue(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configDocumentModeValue: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        DataRepository.dataItemRunning().getComponentRunningDocument().setComponentValue(186, str);
    }

    private void configDollyZoomBack() {
        DollyZoomProcess dollyZoomProcess = (DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676);
        if (dollyZoomProcess != null) {
            dollyZoomProcess.onBackPressed();
        }
    }

    private void configDualVideo(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configDualVideo: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        CameraSettings.getDualVideoConfig().setRecordType(RecordType.getTypeByName(str));
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).adjustViewBackground();
    }

    private void configDualVideoUserGuide() {
        getBaseModule().ifPresent(C0400O0000oOo.INSTANCE);
    }

    private void configFilmDreamBack() {
        Log.u(TAG, "configFilmDreamBack");
        FilmDreamProcess filmDreamProcess = (FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931);
        if (filmDreamProcess != null) {
            filmDreamProcess.showExitConfirm();
        }
    }

    private void configGif(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            boolean z = !CameraSettings.isGifOn();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configGif: ");
            sb.append(z);
            Log.u(str, sb.toString());
            MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
            if (mimojiAvatarEngine2 != null) {
                mimojiAvatarEngine2.changeToGif(z);
            }
            CameraSettings.setGifSwitch(z);
            if (z) {
                DataRepository.dataItemLive().getMimojiStatusManager2().setMimojiRecordState(1);
            }
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.updateConfigItem(162);
            }
            changeModeWithoutConfigureData(((BaseModule) baseModule.get()).getModuleIndex(), false);
        }
    }

    private void configIDCardBack() {
        ((IDCardModeProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(233)).callBackEvent();
    }

    private void configIntoWorkspace() {
        if (((MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929)).hasFeatureInstalled(FeatureModule.MODULE_VLOG2)) {
            Log.u(TAG, "configIntoWorkspace");
            FragmentVVPreview fragmentVVPreview = (FragmentVVPreview) FragmentUtils.getFragmentByTag(this.mActivity.getSupportFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_VV_PREVIEW));
            if (fragmentVVPreview != null && fragmentVVPreview.isVisible()) {
                fragmentVVPreview.controlPlay(false);
            }
            FragmentVVWorkspace fragmentVVWorkspace = new FragmentVVWorkspace();
            fragmentVVWorkspace.setStyle(2, R.style.TTMusicDialogFragment);
            this.mActivity.getSupportFragmentManager().beginTransaction().add((Fragment) fragmentVVWorkspace, FragmentVVWorkspace.TAG).commitAllowingStateLoss();
        }
    }

    private void configLiveVideoQuality(String str) {
        ComponentLiveVideoQuality componentLiveVideoQuality = DataRepository.dataItemLive().getComponentLiveVideoQuality();
        int currentMode = ((DataItemGlobal) DataRepository.provider().dataGlobal()).getCurrentMode();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configLiveVideoQuality: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        CameraStatUtils.trackVideoQuality(CameraSettings.KEY_MI_LIVE_QUALITY, CameraSettings.isFrontCamera(), str);
        componentLiveVideoQuality.setComponentValue(160, str);
        changeModeWithoutConfigureData(currentMode, false);
    }

    private void configMacroMode() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            boolean z = !CameraSettings.isMacroModeEnabled(moduleIndex);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configMacroMode: ");
            sb.append(z);
            Log.u(str, sb.toString());
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            boolean z2 = false;
            switchOffElementsSilent(216);
            CameraSettings.setAutoZoomEnabled(moduleIndex, false);
            CameraSettings.setSuperEISEnabled(moduleIndex, false);
            if (CameraSettings.isVhdrOn(((BaseModule) baseModule.get()).getCameraCapabilities(), moduleIndex)) {
                updateComponentHdr(true);
            }
            if (z && (moduleIndex == 162 || moduleIndex == 169)) {
                singeSwitchVideoBeauty(false);
                CameraSettings.setVideoMasterFilter(0);
                MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                    miBeautyProtocol.dismiss(3);
                }
                MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
                if (masterFilterProtocol != null && masterFilterProtocol.isShowing()) {
                    masterFilterProtocol.dismiss(2, 5);
                }
            }
            CameraSettings.setVideoQuality8KOff(moduleIndex);
            DataRepository.dataItemRunning().getComponentRunningAiAudio().setComponentValue(moduleIndex, "normal");
            CameraSettings.resetRetainZoom();
            setTipsState("macro", true);
            ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
            String str2 = "m";
            if (z) {
                componentRunningMacroMode.setSwitchOn(moduleIndex);
                if (((BaseModule) baseModule.get()).getCameraCapabilities().isMacroHdrMutex()) {
                    closeMutexElement(str2, 194);
                    DataRepository.dataItemRunning().setRecordingClosedElements(this.mRecordingClosedElements);
                }
            } else {
                if (((BaseModule) baseModule.get()).getCameraCapabilities().isMacroHdrMutex()) {
                    this.mRecordingClosedElements = DataRepository.dataItemRunning().getRecordingClosedElements();
                    restoreAllMutexElement(str2);
                }
                componentRunningMacroMode.setSwitchOff(moduleIndex);
            }
            HashMap hashMap = new HashMap();
            hashMap.put(MacroAttr.PARAM_SWITCH_MACRO, z ? "on" : "off");
            MistatsWrapper.mistatEvent(MacroAttr.FUCNAME_MACRO_MODE, hashMap);
            changeModeWithoutConfigureData(moduleIndex, false);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
            MiBeautyProtocol miBeautyProtocol2 = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            if (z) {
                if (bottomPopupTips != null) {
                    bottomPopupTips.directHideTipImage();
                    bottomPopupTips.directShowOrHideLeftTipImage(false);
                }
                if (dualController != null) {
                    dualController.hideZoomButton();
                }
            } else {
                if (miBeautyProtocol2 != null) {
                    z2 = miBeautyProtocol2.isBeautyPanelShow();
                }
                if (bottomPopupTips != null && !z2) {
                    bottomPopupTips.reInitTipImage();
                }
                if (dualController != null && !z2) {
                    if (!CameraSettings.isUltraWideConfigOpen(moduleIndex) && (moduleIndex != 172 || !C0122O00000o.instance().OOO0oo())) {
                        dualController.showZoomButton();
                    }
                    if (topAlert != null) {
                        topAlert.clearAlertStatus();
                    }
                }
            }
        }
    }

    private void configMoon(boolean z) {
        getBaseModule().ifPresent(new O0000O0o(z));
    }

    private void configMoonBacklight() {
        getBaseModule().ifPresent(C0402O0000oo0.INSTANCE);
    }

    private void configMoonNight() {
        getBaseModule().ifPresent(new O00000Oo(this));
    }

    private void configMultiCamReselect() {
        Log.d(TAG, "configMultiCamReselect: ");
        getBaseModule().ifPresent(C0394O0000Ooo.INSTANCE);
    }

    private void configPanoramaDirection() {
        boolean isPanoramaVertical = CameraSettings.isPanoramaVertical(CameraAppImpl.getAndroidContext());
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configPanoramaDirection: ");
        sb.append(!isPanoramaVertical);
        Log.u(str, sb.toString());
        PanoramaProtocol panoramaProtocol = (PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176);
        if (panoramaProtocol != null) {
            panoramaProtocol.toggleCaptureDirection();
        }
    }

    private void configReferenceLineSwitch(int i) {
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        boolean z = false;
        String str = CameraSettings.KEY_REFERENCE_LINE;
        if (!dataItemGlobal.getBoolean(str, false)) {
            z = true;
        }
        DataRepository.dataItemGlobal().putBoolean(str, z).apply();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configReferenceLineSwitch: ");
        sb.append(z);
        Log.u(str2, sb.toString());
        if (1 == i) {
            trackReferenceLineChanged(z);
        }
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null) {
                if (DataRepository.dataItemGlobal().isNormalIntent() || !((BaseModule) baseModule.get()).getCameraCapabilities().isSupportLightTripartite()) {
                    mainContentProtocol.updateReferenceGradienterSwitched();
                } else {
                    mainContentProtocol.hideReferenceGradienter();
                }
            }
        }
    }

    private void configRemoteCamera() {
        getBaseModule().ifPresent(O0000Oo.INSTANCE);
    }

    private void configSilhouette() {
        getBaseModule().ifPresent(C0409O00oOooo.INSTANCE);
    }

    private void configSlowQuality(String str) {
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        ComponentConfigSlowMotionQuality componentConfigSlowMotionQuality = dataItemConfig.getComponentConfigSlowMotionQuality();
        int currentMode = ((DataItemGlobal) DataRepository.provider().dataGlobal()).getCurrentMode();
        String componentValue = dataItemConfig.getComponentConfigSlowMotion().getComponentValue(currentMode);
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configSlowQuality: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        CameraStatUtils.trackSlowMotionQuality(componentValue, str);
        componentConfigSlowMotionQuality.setComponentValue(currentMode, str);
        changeModeWithoutConfigureData(currentMode, false);
    }

    private void configSpeechShutter() {
        if (DataRepository.dataItemRunning().supportSpeechShutter()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                boolean z = !CameraSettings.isSpeechShutterOpen();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configSpeechShutter: ");
                sb.append(z);
                Log.u(str, sb.toString());
                CameraSettings.setSpeechShutterStatus(z);
                CameraStatUtils.trackSpeechShutterStatus(z);
                if (z) {
                    setTipsState(FragmentTopAlert.TIP_SPEECH_SHUTTER_DESC, true);
                }
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                if (moduleIndex == 254 || moduleIndex == 209 || moduleIndex == 210) {
                    z = false;
                }
                ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                if (actionProcessing != null) {
                    actionProcessing.processingSpeechShutter(z);
                }
                SpeechShutterDetect speechShutterDetect = (SpeechShutterDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(255);
                if (speechShutterDetect != null) {
                    speechShutterDetect.processingSpeechShutter(z);
                }
            }
        }
    }

    private void configSuperEIS() {
        Optional baseModule = getBaseModule();
        if (baseModule != null) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                String str = FragmentTopAlert.TIP_SUPER_EIS;
                setTipsState(str, true);
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                boolean isSuperEISEnabled = CameraSettings.isSuperEISEnabled(moduleIndex);
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configSuperEIS: ");
                sb.append(!isSuperEISEnabled);
                Log.u(str2, sb.toString());
                CameraSettings.resetRetainZoom();
                if (isSuperEISEnabled) {
                    CameraSettings.setSuperEISEnabled(moduleIndex, false);
                    topAlert.updateConfigItem(218);
                } else {
                    CameraSettings.setSuperEISEnabled(moduleIndex, true);
                    topAlert.updateConfigItem(218);
                    switchOffElementsSilent(216);
                    closeVideoFast();
                    singeSwitchVideoBeauty(false);
                    resetVideoBokehLevel();
                    resetVideoFilter();
                    CameraSettings.setVideoMasterFilter(0);
                    ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
                    if (componentRunningMacroMode.isSwitchOn(moduleIndex)) {
                        componentRunningMacroMode.setSwitchOff(moduleIndex);
                    }
                    CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                    CameraSettings.setAiEnhancedVideoEnabled(moduleIndex, false);
                    if (CameraSettings.isVhdrOn(((BaseModule) baseModule.get()).getCameraCapabilities(), moduleIndex)) {
                        updateComponentHdr(true);
                    }
                    CameraSettings.setCinematicAspectRatioEnabled(moduleIndex, false);
                    CameraSettings.setVideoQuality8KOff(moduleIndex);
                }
                trackSuperEISChanged(!isSuperEISEnabled);
                if (moduleIndex == 204) {
                    DataRepository.dataItemGlobal().setCurrentMode(162);
                }
                changeModeWithoutConfigureData(162, false);
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (isSuperEISEnabled) {
                    topAlert.alertSwitchTip(str, 8, (int) R.string.super_eis_disabled_hint);
                }
                bottomPopupTips.updateLeftTipImage();
                bottomPopupTips.updateTipImage();
            }
        }
    }

    private void configSuperEISPro() {
        Optional baseModule = getBaseModule();
        if (baseModule != null) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                setTipsState("super_eis_pro", true);
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                String componentValue = DataRepository.dataItemRunning().getComponentRunningEisPro().getComponentValue(moduleIndex);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configSuperEISPro: ");
                sb.append(componentValue);
                Log.u(str, sb.toString());
                CameraSettings.resetRetainZoom();
                topAlert.updateConfigItem(165);
                if (!componentValue.equals("off")) {
                    switchOffElementsSilent(216);
                    closeVideoFast();
                    singeSwitchVideoBeauty(false);
                    resetVideoBokehLevel();
                    resetVideoFilter();
                    CameraSettings.setVideoMasterFilter(0);
                    ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
                    if (componentRunningMacroMode.isSwitchOn(moduleIndex)) {
                        componentRunningMacroMode.setSwitchOff(moduleIndex);
                    }
                    CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                    CameraSettings.setAiEnhancedVideoEnabled(moduleIndex, false);
                    CameraSettings.setCinematicAspectRatioEnabled(moduleIndex, false);
                    if (CameraSettings.isVhdrOn(((BaseModule) baseModule.get()).getCameraCapabilities(), moduleIndex)) {
                        updateComponentHdr(true);
                    }
                    CameraSettings.setVideoQuality8KOff(moduleIndex);
                }
                if (moduleIndex == 204) {
                    DataRepository.dataItemGlobal().setCurrentMode(162);
                }
                changeModeWithoutConfigureData(162, true);
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                bottomPopupTips.updateLeftTipImage();
                bottomPopupTips.updateTipImage();
            }
        }
    }

    private void configSwitchHandGesture() {
        if (DataRepository.dataItemRunning().supportHandGesture()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                if (baseModule2 != null) {
                    boolean z = !CameraSettings.isHandGestureOpen();
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("configSwitchHandGesture: ");
                    sb.append(z);
                    Log.u(str, sb.toString());
                    if (z) {
                        setTipsState(FragmentTopAlert.TIP_HAND_GESTURE_DESC, true);
                    }
                    CameraSettings.setHandGestureStatus(z);
                    ((Camera2Module) baseModule2).onHanGestureSwitched(z);
                }
            }
        }
    }

    private void configSwitchUltraWide() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            boolean z = !CameraSettings.isUltraWideConfigOpen(moduleIndex);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configSwitchUltraWide: ");
            sb.append(z);
            Log.u(str, sb.toString());
            if (CameraSettings.isUltraPixelOn()) {
                CameraSettings.switchOffUltraPixel();
            }
            CameraSettings.setUltraWideConfig(moduleIndex, z);
            LensProtocol lensProtocol = (LensProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(200);
            if (lensProtocol != null) {
                lensProtocol.onSwitchLens(true, false);
            }
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                if (z) {
                    bottomPopupTips.showTips(13, R.string.ultra_wide_open_tip, 7, 500);
                } else {
                    bottomPopupTips.showTips(13, (int) R.string.ultra_wide_close_tip, 6);
                }
                bottomPopupTips.reConfigQrCodeTip();
            }
        }
    }

    private void configSwitchUltraWideBokeh() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (!(topAlert == null || this.mActivity == null)) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                String str = "pref_ultra_wide_bokeh_enabled";
                boolean isSwitchOn = DataRepository.dataItemRunning().isSwitchOn(str);
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configSwitchUltraWideBokeh: ");
                sb.append(!isSwitchOn);
                Log.u(str2, sb.toString());
                MistatsWrapper.moduleUIClickEvent("M_portrait_", PortraitAttr.PARAM_ULTRA_WIDE_BOKEH, (Object) Boolean.valueOf(!isSwitchOn));
                String str3 = FragmentTopAlert.TIP_ULTRA_WIDE_BOKEH;
                if (isSwitchOn) {
                    DataRepository.dataItemRunning().switchOff(str);
                    topAlert.alertSwitchTip(str3, 0, (int) R.string.ultra_wide_bokeh_close_tip);
                } else {
                    setTipsState(str3, true);
                    DataRepository.dataItemRunning().switchOn(str);
                    topAlert.alertSwitchTip(str3, 8, (int) R.string.ultra_wide_bokeh_open_tip);
                }
                changeModeWithoutConfigureData(((BaseModule) baseModule.get()).getModuleIndex(), false);
            }
        }
    }

    private void configTilt(String str) {
        Optional baseModule = getBaseModule();
        BaseModule baseModule2 = (BaseModule) baseModule.get();
        if (baseModule.isPresent() && (baseModule2 instanceof Camera2Module)) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configTilt: ");
            sb.append(str);
            Log.u(str2, sb.toString());
            DataRepository.dataItemRunning().getComponentRunningTiltValue().setComponentValue(160, str);
            ((Camera2Module) baseModule.get()).showOrHideChip(false);
            ((Camera2Module) baseModule.get()).onTiltShiftSwitched(true);
            EffectController.getInstance().setDrawTilt(true);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.reConfigQrCodeTip();
            }
        }
    }

    private void configTimerBurst(int i) {
        boolean isTimerBurstEnable = CameraSettings.isTimerBurstEnable();
        if (i != 3) {
            boolean z = !isTimerBurstEnable;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configTimerBurst: ");
            sb.append(z);
            Log.u(str, sb.toString());
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                CameraSettings.setTimerBurstEnable(z);
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                MistatsWrapper.commonKeyTriggerEvent(CaptureSence.PARAM_TIMER_BURST, Boolean.valueOf(z), null);
                if (z) {
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null) {
                        configTiltSwitch(3);
                        configLiveShotSwitch(3);
                        if (CameraSettings.isUltraPixelOn()) {
                            CameraSettings.switchOffUltraPixel();
                            topAlert.updateConfigItem(209);
                            changeModeWithoutConfigureData(baseModule2.getModuleIndex(), false);
                        }
                        topAlert.alertTimerBurstHint(0, R.string.timer_burst);
                    }
                }
            }
        } else if (isTimerBurstEnable) {
            Log.u(TAG, "configTimerBurst: MUTEX false");
            DataRepository.dataItemLive().getTimerBurstController().muteTimerConfig();
            TopAlert topAlert2 = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert2 != null) {
                topAlert2.alertTimerBurstHint(8, R.string.timer_burst);
            }
        }
    }

    private void configVV() {
        boolean z;
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate.getActiveFragment(R.id.bottom_action) != 65530) {
            int moduleIndex = ((BaseModule) getBaseModule().get()).getModuleIndex();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null && C0122O00000o.instance().OOO0oO0()) {
                topAlert.clearVideoUltraClear();
            }
            if (moduleIndex == 169) {
                closeVideoFast();
                z = true;
            } else {
                z = false;
            }
            if (CameraSettings.isFaceBeautyOn(moduleIndex, null)) {
                singeSwitchVideoBeauty(false);
                z = true;
            }
            if (CameraSettings.isSuperEISEnabled(moduleIndex)) {
                CameraSettings.setSuperEISEnabled(moduleIndex, false);
                z = true;
            }
            if (CameraSettings.isSubtitleEnabled(moduleIndex)) {
                CameraSettings.setSubtitleEnabled(moduleIndex, false);
                z = true;
            }
            if (CameraSettings.isAutoZoomEnabled(moduleIndex)) {
                CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                z = true;
            }
            if (CameraSettings.isMacroModeEnabled(moduleIndex)) {
                DataRepository.dataItemRunning().getComponentRunningMacroMode().setSwitchOff(moduleIndex);
                z = true;
            }
            if (CameraSettings.isVideoQuality8KOpen(162)) {
                CameraSettings.setVideoQuality8KOff(162);
                z = true;
            }
            if (z) {
                CameraSettings.setRetainZoom(1.0f, moduleIndex);
            }
            DataRepository.dataItemConfig().getComponentConfigVideoQuality().setForceValueOverlay("6");
            CameraSettings.setCinematicAspectRatioEnabled(moduleIndex, false);
            ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).updateCinematicAspectRatioSwitched(false);
            baseDelegate.delegateEvent(15);
        } else {
            DataRepository.dataItemConfig().getComponentConfigVideoQuality().setForceValueOverlay(null);
            TopAlert topAlert2 = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert2 != null) {
                topAlert2.alertTopHint(8, (int) R.string.vv_use_hint_text_title);
            }
            baseDelegate.delegateEvent(15);
            reCheckVideoUltraClearTip();
            z = false;
        }
        TopAlert topAlert3 = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert3 != null) {
            topAlert3.updateConfigItem(216);
            topAlert3.updateConfigItem(256);
            topAlert3.refreshExtraMenu();
            if (z) {
                changeMode(162);
            }
        }
    }

    private void configVVBack() {
        Log.u(TAG, "configVVBack");
        LiveVVProcess liveVVProcess = (LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230);
        if (liveVVProcess != null) {
            liveVVProcess.showExitConfirm();
        }
    }

    private void configVideoBokehSwitch(int i) {
        String str;
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
            String str2 = "pref_video_bokeh_key";
            boolean isSwitchOn = dataItemConfig.isSwitchOn(str2);
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configVideoBokehSwitch: switchOn = ");
            sb.append(!isSwitchOn);
            Log.u(str3, sb.toString());
            HashMap hashMap = new HashMap();
            String str4 = AlgoAttr.PARAM_BOKEN;
            if (!isSwitchOn) {
                singeSwitchVideoBeauty(false);
                dataItemConfig.switchOn(str2);
                str = "on";
            } else {
                dataItemConfig.switchOff(str2);
                str = "off";
            }
            hashMap.put(str4, str);
            MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
            topAlert.updateConfigItem(243);
            changeModeWithoutConfigureData(((BaseModule) baseModule.get()).getModuleIndex(), true);
            String str5 = FragmentTopAlert.TIP_VIDEO_BOKEH;
            if (!isSwitchOn) {
                topAlert.alertSwitchTip(str5, 0, (int) R.string.pref_camera_video_bokeh_on);
            } else {
                topAlert.alertSwitchTip(str5, 8, (int) R.string.pref_camera_video_bokeh_off);
            }
        }
    }

    private boolean configVideoHdrIfNeed() {
        Optional baseModule = getBaseModule();
        if (!baseModule.isPresent()) {
            return false;
        }
        setTipsState(FragmentTopAlert.TIP_HDR, true);
        int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
        CameraCapabilities cameraCapabilities = ((BaseModule) baseModule.get()).getCameraCapabilities();
        if (moduleIndex != 162 || cameraCapabilities == null || !cameraCapabilities.isSupportVideoHdr()) {
            return false;
        }
        boolean equals = "off".equals(DataRepository.dataItemConfig().getComponentHdr().getComponentValue(moduleIndex));
        String str = VideoAttr.PARAM_VIDEO_HDR;
        if (!equals) {
            CameraStatUtils.trackVideoCommonClickB(str, true);
            Log.u(TAG, "video Hdr mutex");
            CameraSettings.setAiEnhancedVideoEnabled(moduleIndex, false);
            CameraSettings.setAutoZoomEnabled(moduleIndex, false);
            switchOffElementsSilent(216);
            closeVideoFast();
            singeSwitchVideoBeauty(false);
            CameraSettings.setVideoMasterFilter(0);
            CameraSettings.setSuperEISEnabled(moduleIndex, false);
            CameraSettings.setSubtitleEnabled(moduleIndex, false);
            CameraSettings.setVideoQuality8KOff(moduleIndex);
            DataRepository.dataItemRunning().getComponentRunningAiAudio().setComponentValue(moduleIndex, "normal");
            ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
            if (componentRunningMacroMode.isSwitchOn(moduleIndex)) {
                componentRunningMacroMode.setSwitchOff(moduleIndex);
            }
            if (((BaseModule) baseModule.get()).getZoomRatio() <= 1.0f) {
                CameraSettings.setRetainZoom(1.0f, moduleIndex);
            }
        } else {
            CameraStatUtils.trackVideoCommonClickB(str, false);
        }
        changeMode(moduleIndex);
        return true;
    }

    private void configVideoQuality(String str) {
        switchOffElementsSilent(216);
        ComponentConfigVideoQuality componentConfigVideoQuality = DataRepository.dataItemConfig().getComponentConfigVideoQuality();
        int currentMode = ((DataItemGlobal) DataRepository.provider().dataGlobal()).getCurrentMode();
        boolean supportVideoSATForVideoQuality = CameraSettings.supportVideoSATForVideoQuality(currentMode);
        componentConfigVideoQuality.setComponentValue(currentMode, str);
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configVideoQuality: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        CameraStatUtils.trackVideoQuality("pref_video_quality_key", CameraSettings.isFrontCamera(), str);
        if (supportVideoSATForVideoQuality && !CameraSettings.supportVideoSATForVideoQuality(currentMode)) {
            CameraSettings.resetRetainZoom();
        }
        changeModeWithoutConfigureData(currentMode, false);
    }

    private void configVideoSubFps(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configVideoSubFps: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        ComponentConfigVideoSubFPS componentConfigVideoSubFps = DataRepository.dataItemConfig().getComponentConfigVideoSubFps();
        int currentMode = ((DataItemGlobal) DataRepository.provider().dataGlobal()).getCurrentMode();
        componentConfigVideoSubFps.setComponentValue(currentMode, str);
        changeModeWithoutConfigureData(currentMode, false);
    }

    private void configVideoSubQuality(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configVideoSubQuality: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        ComponentConfigVideoSubQuality componentConfigVideoSubQuality = DataRepository.dataItemConfig().getComponentConfigVideoSubQuality();
        int currentMode = ((DataItemGlobal) DataRepository.provider().dataGlobal()).getCurrentMode();
        componentConfigVideoSubQuality.setComponentValue(currentMode, str);
        if (str.equals("3001")) {
            openVideo8K();
        }
        changeModeWithoutConfigureData(currentMode, false);
    }

    private void configVideoSubtitle() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                boolean isSubtitleEnabled = CameraSettings.isSubtitleEnabled(moduleIndex);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configVideoSubtitle: ");
                sb.append(!isSubtitleEnabled);
                Log.u(str, sb.toString());
                CameraStatUtils.trackSubtitle(!isSubtitleEnabled);
                if (isSubtitleEnabled) {
                    CameraSettings.setSubtitleEnabled(moduleIndex, false);
                    topAlert.updateConfigItem(220);
                } else {
                    CameraSettings.setSubtitleEnabled(moduleIndex, true);
                    topAlert.updateConfigItem(220);
                    if (moduleIndex != 214) {
                        CameraSettings.setVideoQuality8KOff(162);
                        switchOffElementsSilent(216);
                        closeVideoFast();
                        if (CameraSettings.isVhdrOn(((BaseModule) baseModule.get()).getCameraCapabilities(), moduleIndex)) {
                            updateComponentHdr(true);
                        }
                        CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                        CameraSettings.setRetainZoom(1.0f, moduleIndex);
                    }
                }
                if (moduleIndex == 204) {
                    DataRepository.dataItemGlobal().setCurrentMode(162);
                }
                changeModeWithoutConfigureData(DataRepository.dataItemGlobal().getCurrentMode(), false);
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (CameraSettings.isSubtitleEnabled(moduleIndex)) {
                    SubtitleRecording subtitleRecording = (SubtitleRecording) ModeCoordinatorImpl.getInstance().getAttachProtocol(231);
                    subtitleRecording.initPermission();
                    subtitleRecording.checkNetWorkStatus();
                }
                bottomPopupTips.updateLeftTipImage();
                bottomPopupTips.updateTipImage();
            }
        }
    }

    private void conflictWithFlashAndHdr() {
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        dataItemRunning.switchOff("pref_camera_hand_night_key");
        dataItemRunning.switchOff("pref_camera_groupshot_mode_key");
        dataItemRunning.switchOff("pref_camera_super_resolution_key");
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips == null) {
            Log.d(TAG, "conflictWithFlashAndHdr return, BottomPopupTips is null");
        } else if (!CameraSettings.isMacroModeEnabled(ModuleManager.getActiveModuleIndex()) || bottomPopupTips.getCurrentBottomTipType() != 18) {
            bottomPopupTips.directlyHideTips();
        }
    }

    public static ConfigChangeImpl create(ActivityBase activityBase) {
        return new ConfigChangeImpl(activityBase);
    }

    /* access modifiers changed from: private */
    public Optional getBaseModule() {
        ActivityBase activityBase = this.mActivity;
        return activityBase == null ? Optional.empty() : Optional.ofNullable((BaseModule) activityBase.getCurrentModule());
    }

    private boolean getState(int i, String str) {
        if (i == 2) {
            return DataRepository.dataItemRunning().isSwitchOn(str);
        }
        if (i != 4) {
            return DataRepository.dataItemRunning().triggerSwitchAndGet(str);
        }
        DataRepository.dataItemRunning().switchOff(str);
        return false;
    }

    private int getUpDistance() {
        int distanceForWM;
        WatermarkProtocol watermarkProtocol = (WatermarkProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(253);
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        if (watermarkProtocol != null) {
            if (!watermarkProtocol.isWatermarkPanelShow()) {
                return 0;
            }
            distanceForWM = watermarkProtocol.getDistanceForWM();
        } else if (miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow()) {
            return 0;
        } else {
            distanceForWM = miBeautyProtocol.getDistanceForWM();
        }
        return distanceForWM;
    }

    private void hideTipMessage(@StringRes int i) {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (i <= 0 || bottomPopupTips.containTips(i)) {
            bottomPopupTips.directlyHideTips();
        }
    }

    private boolean is4KQuality(int i, int i2) {
        return i >= 3840 && i2 >= 2160;
    }

    private boolean is8KQuality(int i, int i2) {
        return i >= 7680 && i2 >= 4320;
    }

    private boolean isAlive() {
        return this.mActivity != null;
    }

    private boolean isBeautyPanelShow() {
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        if (miBeautyProtocol != null) {
            return miBeautyProtocol.isBeautyPanelShow();
        }
        return false;
    }

    private boolean isChangeManuallyParameters() {
        Optional baseModule = getBaseModule();
        boolean z = false;
        if (!baseModule.isPresent()) {
            return false;
        }
        int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        ComponentManuallyWB componentManuallyWB = dataItemConfig.getmComponentManuallyWB();
        ComponentManuallyET componentManuallyET = dataItemConfig.getmComponentManuallyET();
        ComponentManuallyISO componentManuallyISO = dataItemConfig.getmComponentManuallyISO();
        ComponentManuallyFocus manuallyFocus = dataItemConfig.getManuallyFocus();
        ComponentManuallyEV componentManuallyEV = dataItemConfig.getComponentManuallyEV();
        boolean isModified = componentManuallyWB.isModified(moduleIndex);
        boolean isModified2 = componentManuallyET.isModified(moduleIndex);
        boolean isModified3 = componentManuallyISO.isModified(moduleIndex);
        boolean isModified4 = manuallyFocus.isModified(moduleIndex);
        boolean isModified5 = componentManuallyEV.isModified(moduleIndex);
        if (isModified || isModified2 || isModified3 || isModified4 || isModified5) {
            z = true;
        }
        return z;
    }

    private boolean isVideoRecoding(BaseModule baseModule) {
        if (baseModule == null || !(baseModule instanceof VideoBase)) {
            return false;
        }
        return baseModule.isRecording();
    }

    private void mutexBeautyBusiness(int i) {
        DataRepository.dataItemConfig().getComponentConfigBeauty().setClosed(false, i);
        switchOffElementsSilent(216);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (CameraSettings.isAutoZoomEnabled(i)) {
            CameraSettings.resetRetainZoom();
            CameraSettings.setAutoZoomEnabled(i, false);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            bottomPopupTips.updateLeftTipImage();
            bottomPopupTips.updateTipImage();
            if (topAlert != null) {
                topAlert.hideSwitchTip();
            }
        }
        if (CameraSettings.isAiEnhancedVideoEnabled(i)) {
            CameraSettings.setAiEnhancedVideoEnabled(i, false);
            BottomPopupTips bottomPopupTips2 = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            bottomPopupTips2.updateLeftTipImage();
            bottomPopupTips2.updateTipImage();
            if (topAlert != null) {
                topAlert.hideSwitchTip();
            }
        }
        if (CameraSettings.isSuperEISEnabled(i)) {
            CameraSettings.resetRetainZoom();
            CameraSettings.setSuperEISEnabled(i, false);
            BottomPopupTips bottomPopupTips3 = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            bottomPopupTips3.updateLeftTipImage();
            bottomPopupTips3.updateTipImage();
            if (topAlert != null) {
                topAlert.hideSwitchTip();
            }
        }
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent() && CameraSettings.isVhdrOn(((BaseModule) baseModule.get()).getCameraCapabilities(), i)) {
            updateComponentHdr(true);
        }
        ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
        if (componentRunningMacroMode.isSwitchOn(i)) {
            CameraSettings.resetRetainZoom();
            componentRunningMacroMode.setSwitchOff(i);
        }
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        String str = "pref_video_bokeh_key";
        if (dataItemConfig.isSwitchOn(str)) {
            dataItemConfig.switchOff(str);
        }
        CameraSettings.setVideoQuality8KOff(i);
        CameraSettings.setProVideoLog(false);
        if (topAlert != null) {
            topAlert.updateConfigItem(256);
        }
    }

    private void openVideo8K() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                DataItemGlobal dataItemGlobal = (DataItemGlobal) DataRepository.provider().dataGlobal();
                int currentMode = dataItemGlobal.getCurrentMode();
                CameraStatUtils.track8KVideo(currentMode, String.valueOf(30));
                if (closeVideoFast()) {
                    currentMode = dataItemGlobal.getCurrentMode();
                }
                switchOffElementsSilent(216);
                if (!C0122O00000o.instance().OOOooo()) {
                    CameraSettings.setCinematicAspectRatioEnabled(currentMode, false);
                }
                if (!CameraSettings.is8KCamcorderSupported(((BaseModule) baseModule.get()).getActualCameraId())) {
                    CameraSettings.resetRetainZoom();
                    if (currentMode == 180) {
                        DataRepository.dataItemConfig().getManuallyDualLens().setComponentValue(currentMode, ComponentManuallyDualLens.LENS_WIDE);
                    }
                }
                singeSwitchVideoBeauty(false);
                resetVideoBokehLevel();
                resetVideoFilter();
                CameraSettings.setVideoMasterFilter(0);
                CameraSettings.setAutoZoomEnabled(currentMode, false);
                CameraSettings.setAiEnhancedVideoEnabled(currentMode, false);
                CameraSettings.setSuperEISEnabled(currentMode, false);
                if (CameraSettings.isVhdrOn(((BaseModule) baseModule.get()).getCameraCapabilities(), currentMode)) {
                    updateComponentHdr(true);
                }
                DataRepository.dataItemRunning().getComponentRunningMacroMode().setSwitchOff(currentMode);
                CameraSettings.setProVideoLog(false);
                CameraSettings.setSubtitleEnabled(currentMode, false);
                topAlert.updateConfigItem(256);
                topAlert.alertVideoUltraClear(0, (int) R.string.pref_camera_video_8k_tips);
            }
        }
    }

    private void persistFilter(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("persistFilter: filterId = ");
        sb.append(i);
        Log.d(str, sb.toString());
        CameraSettings.setShaderEffect(i);
    }

    public static void preload() {
        Log.i(TAG, "preload");
    }

    private void reConfigTipOfHdr(int i) {
        ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
        if (!componentHdr.isEmpty()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                String componentValue = componentHdr.getComponentValue(i);
                if ("on".equals(componentValue) || "normal".equals(componentValue)) {
                    topAlert.alertHDR(0, false, false);
                } else if ("live".equals(componentValue)) {
                    topAlert.alertHDR(0, true, false);
                } else if (topAlert.isHDRShowing()) {
                    topAlert.alertHDR(8, false, false);
                }
            }
        }
    }

    private void resetVideoBokehLevel() {
        CameraSettings.setVideoBokehRatio(0.0f);
    }

    private void resetVideoFilter() {
        CameraSettings.setShaderEffect(0);
    }

    private void setTipsState(String str, boolean z) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.setTipsState(str, z);
        }
    }

    private void showDualController(boolean z) {
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null) {
            boolean isBackCamera = CameraSettings.isBackCamera();
            if (z || !isBackCamera) {
                dualController.hideZoomButton();
            } else {
                dualController.showZoomButton();
            }
        }
    }

    private void showOrHideTipImage(boolean z) {
        if (!z && CameraSettings.isFrontCamera()) {
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.updateTipImage();
            }
        }
    }

    private void singeSwitchVideoBeauty(boolean z) {
        ComponentRunningShine componentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
        int i = 0;
        if (componentRunningShine.supportBeautyLevel()) {
            if (z) {
                i = 3;
            }
            CameraSettings.setFaceBeautyLevel(i);
        } else if (componentRunningShine.supportSmoothLevel()) {
            CameraSettings.setFaceBeautySmoothLevel(z ? 40 : 0);
            if (!z) {
                DataRepository.dataItemRunning().getComponentRunningShine().setVideoShineForceOn(DataRepository.dataItemGlobal().getCurrentMode(), false);
                if (DataRepository.dataItemRunning().getComponentRunningShine().supportVideoFilter()) {
                    resetVideoFilter();
                    CameraSettings.setVideoBokehRatio(0.0f);
                }
                ShineHelper.onBeautyChanged();
                ShineHelper.onVideoBokehRatioChanged();
                ShineHelper.onVideoFilterChanged();
                ShineHelper.onShineStateChanged();
            }
        } else if (ModuleManager.isFastMotionModule() && !z && DataRepository.dataItemRunning().getComponentRunningShine().supportVideoFilter()) {
            resetVideoFilter();
        }
    }

    private void trackFocusPeakChanged(boolean z) {
        MistatsWrapper.moduleUIClickEvent("M_manual_", Manual.MANUAL_FOCUS_PEAK, (Object) String.valueOf(z));
    }

    private void trackGenderAgeChanged(boolean z) {
        MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_GENDER_AGE, Boolean.valueOf(z), null);
    }

    private void trackGotoSettings() {
        BaseModule baseModule = (BaseModule) this.mActivity.getCurrentModule();
        if (baseModule != null) {
            CameraStatUtils.trackGotoSettings(baseModule.getModuleIndex());
        }
    }

    private void trackGradienterChanged(boolean z) {
        MistatsWrapper.moduleUIClickEvent(DataRepository.dataItemGlobal().getCurrentMode(), Manual.GRADIENT, (Object) Boolean.valueOf(z));
    }

    private void trackHHTChanged(boolean z) {
        MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_HHT, Boolean.valueOf(z), null);
    }

    private void trackMagicMirrorChanged(boolean z) {
        MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_MAGIC_MIRROR, Boolean.valueOf(z), null);
    }

    private void trackReferenceLineChanged(boolean z) {
        MistatsWrapper.commonKeyTriggerEvent("attr_reference_line", Boolean.valueOf(z), null);
    }

    private void trackSuperEISChanged(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.MODULE_NAME, MistatsWrapper.getStatsModuleKey(DataRepository.dataItemGlobal().getCurrentMode()));
        hashMap.put(BaseEvent.SENSOR_ID, CameraSettings.isFrontCamera() ? "front" : "back");
        hashMap.put(VideoAttr.PARAM_SUPER_EIS, String.valueOf(z));
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    private void trackSuperResolutionChanged(boolean z) {
        MistatsWrapper.moduleUIClickEvent("M_manual_", Manual.SUPER_RESOLUTION, (Object) String.valueOf(z));
    }

    private void trackUltraPixelPortraitChanged(boolean z) {
        MistatsWrapper.commonKeyTriggerEvent(PortraitAttr.ULTRAPIXEL_PORTRAIT, String.valueOf(z), null);
    }

    private void updateAiScene(boolean z) {
        DataRepository.dataItemGlobal().getCurrentMode();
        ComponentConfigAi componentConfigAi = DataRepository.dataItemConfig().getComponentConfigAi();
        if (!componentConfigAi.isEmpty() && componentConfigAi.isClosed() != z) {
            componentConfigAi.setClosed(z);
            getBaseModule().ifPresent(new O00000o0(z));
            ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).updateConfigItem(201);
        }
    }

    private void updateAutoZoom(boolean z) {
    }

    private void updateComponentBeauty(boolean z) {
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        ComponentConfigBeauty componentConfigBeauty = DataRepository.dataItemConfig().getComponentConfigBeauty();
        if (!componentConfigBeauty.isEmpty() && componentConfigBeauty.isClosed(currentMode) != z) {
            componentConfigBeauty.setClosed(z, currentMode);
            if (z) {
                MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                    miBeautyProtocol.dismiss(2);
                }
            }
            OnShineChangedProtocol onShineChangedProtocol = (OnShineChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(234);
            if (onShineChangedProtocol != null) {
                onShineChangedProtocol.onShineChanged(true, 239);
            }
        }
    }

    private void updateComponentFilter(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateComponentFilter: close = ");
        sb.append(z);
        Log.d(str, sb.toString());
        ComponentConfigFilter componentConfigFilter = DataRepository.dataItemRunning().getComponentConfigFilter();
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        if (!componentConfigFilter.isEmpty() && componentConfigFilter.isClosed(currentMode) != z) {
            componentConfigFilter.setClosed(z, currentMode);
            ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).updateConfigItem(212);
            if (z) {
                MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                    miBeautyProtocol.dismiss(2);
                }
            }
        }
    }

    private void updateComponentFlash(String str, boolean z) {
        ComponentConfigFlash componentFlash = DataRepository.dataItemConfig().getComponentFlash();
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        if (!componentFlash.isEmpty() && componentFlash.isClosed() != z && (!z || !componentFlash.getComponentValue(currentMode).equals("2") || !SupportedConfigFactory.CLOSE_BY_BURST_SHOOT.equals(str))) {
            componentFlash.setClosed(z);
            ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).updateConfigItem(193);
        }
    }

    private void updateComponentHdr(boolean z) {
        ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
        DataRepository.dataItemGlobal().getCurrentMode();
        if (!componentHdr.isEmpty() && componentHdr.isClosed() != z) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                if (moduleIndex == 162 && z) {
                    componentHdr.setComponentValue(moduleIndex, "off");
                }
            }
            componentHdr.setClosed(z);
            ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).updateConfigItem(194);
        }
    }

    private void updateComponentShine(boolean z) {
        ComponentRunningShine componentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
        if (!componentRunningShine.isEmpty() && componentRunningShine.isClosed() != z) {
            componentRunningShine.setClosed(z);
        }
    }

    private void updateEyeLight(boolean z) {
        ComponentRunningEyeLight componentRunningEyeLight = DataRepository.dataItemRunning().getComponentRunningEyeLight();
        if (componentRunningEyeLight.isClosed() != z) {
            componentRunningEyeLight.setClosed(z);
        }
    }

    private void updateFlashModeAndRefreshUI(BaseModule baseModule, String str) {
        int moduleIndex = baseModule.getModuleIndex();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateFlashModeAndRefreshUI flashMode = ");
        sb.append(str);
        Log.d(str2, sb.toString());
        if (!TextUtils.isEmpty(str)) {
            CameraSettings.setFlashMode(moduleIndex, str);
        }
        String str3 = "0";
        if (str3.equals(str)) {
            ToastUtils.showToast((Context) this.mActivity, CameraSettings.isFrontCamera() ? R.string.close_front_flash_toast : R.string.close_back_flash_toast);
        }
        if (!baseModule.isDoingAction() || str3.equals(str)) {
            baseModule.updatePreferenceInWorkThread(10);
        } else {
            baseModule.updatePreferenceTrampoline(10);
        }
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.updateConfigItem(193);
        }
    }

    private void updateLiveShot(boolean z) {
        if (C0122O00000o.instance().OOO0o0o()) {
            int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
            if (currentMode == 163 || currentMode == 165) {
                ComponentConfigLiveShot componentConfigLiveShot = DataRepository.dataItemConfig().getComponentConfigLiveShot();
                if (componentConfigLiveShot.isClosed() != z) {
                    componentConfigLiveShot.setClosed(z);
                    ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).updateConfigItem(206);
                }
            }
        }
    }

    private void updateRaw(boolean z) {
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        ComponentConfigRaw componentConfigRaw = DataRepository.dataItemConfig().getComponentConfigRaw();
        if (!componentConfigRaw.isEmpty() && componentConfigRaw.isClosed(currentMode) != z) {
            componentConfigRaw.setClosed(z, currentMode);
        }
    }

    private void updateTipMessage(int i, @StringRes int i2, int i3) {
        ((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175)).showTips(i, i2, i3);
    }

    private void updateUltraPixel(boolean z) {
        ComponentRunningUltraPixel componentUltraPixel = DataRepository.dataItemRunning().getComponentUltraPixel();
        if (!componentUltraPixel.isEmpty() && componentUltraPixel.isClosed() != z) {
            componentUltraPixel.setClosed(z);
        }
    }

    private void watermarkToast() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertSwitchTip("ai_watermark", 0, (int) R.string.ai_watermark_title);
        }
    }

    public /* synthetic */ void O000000o(BaseModule baseModule) {
        int moduleIndex = baseModule.getModuleIndex();
        if (moduleIndex != 185) {
            if (moduleIndex == 204) {
                configDualVideoUserGuide();
                return;
            } else if (!(moduleIndex == 207 || moduleIndex == 210 || moduleIndex == 213)) {
                return;
            }
        }
        configCloneUseGuide();
    }

    public /* synthetic */ void O000000o(boolean z, BaseModule baseModule) {
        if ("auto" != DataRepository.dataItemConfig().getComponentHdr().getComponentValue(DataRepository.dataItemGlobal().getCurrentMode()) && z && (getBaseModule().get() instanceof Camera2Module)) {
            ((Camera2Module) getBaseModule().get()).resetMagneticInfo();
        }
        if (z) {
            baseModule.updatePreferenceInWorkThread(11);
        }
        baseModule.updatePreferenceInWorkThread(10);
    }

    public /* synthetic */ void O00000Oo(BaseModule baseModule) {
        if (172 != baseModule.getModuleIndex()) {
            conflictWithFlashAndHdr();
        }
        baseModule.updatePreferenceInWorkThread(11, 58);
    }

    public /* synthetic */ void O0000Oo(BaseModule baseModule) {
        if (baseModule instanceof Camera2Module) {
            Camera2Module camera2Module = (Camera2Module) baseModule;
            configMoon(false);
            Log.u(TAG, "(moon_mode) config moon night");
            camera2Module.updateMoonNight();
        }
    }

    public /* synthetic */ void O0000oO0(BaseModule baseModule) {
        int moduleIndex = baseModule.getModuleIndex();
        if (moduleIndex == 172) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null && !topAlert.isExtraMenuShowing()) {
                String valueSelectedStringIdIgnoreClose = DataRepository.dataItemConfig().getComponentConfigSlowMotion().getValueSelectedStringIdIgnoreClose(moduleIndex);
                topAlert.alertVideoUltraClear(valueSelectedStringIdIgnoreClose != null ? 0 : 8, valueSelectedStringIdIgnoreClose);
                if (CameraSettings.isAlgoFPS(baseModule.getModuleIndex()) && topAlert != null) {
                    String str = FragmentTopAlert.TIP_480FPS_960FPS_DESC;
                    if (topAlert.getTipsState(str)) {
                        setTipsState(str, false);
                        topAlert.alertRecommendDescTip(str, 0, R.string.fps960_toast);
                    }
                }
            }
        }
    }

    public void closeMutexElement(String str, int... iArr) {
        int[] iArr2 = new int[iArr.length];
        this.mRecordingClosedElements = iArr;
        for (int i = 0; i < iArr.length; i++) {
            int i2 = iArr[i];
            if (i2 == 193) {
                updateComponentFlash(str, true);
                iArr2[i] = 10;
            } else if (i2 == 194) {
                updateComponentHdr(true);
                iArr2[i] = 11;
            } else if (i2 == 196) {
                updateComponentFilter(true);
                iArr2[i] = 2;
            } else if (i2 == 201) {
                updateAiScene(true);
                iArr2[i] = 36;
            } else if (i2 == 206) {
                updateLiveShot(true);
                iArr2[i] = 49;
            } else if (i2 == 209) {
                updateUltraPixel(true);
                iArr2[i] = 50;
            } else if (i2 == 212) {
                updateComponentShine(true);
                iArr2[i] = 2;
            } else if (i2 == 227) {
                iArr2[i] = 74;
            } else if (i2 == 237) {
                updateRaw(true);
                iArr2[i] = 44;
            } else if (i2 == 239) {
                updateComponentBeauty(true);
                iArr2[i] = 13;
            } else if (i2 == 253) {
                updateAutoZoom(true);
                iArr2[i] = 51;
            } else if (i2 == 254) {
                updateEyeLight(true);
                iArr2[i] = 45;
            } else {
                throw new RuntimeException("unknown mutex element");
            }
        }
        getBaseModule().ifPresent(new C0399O0000oOO(iArr2));
    }

    public void configAi108() {
        if (getBaseModule().isPresent()) {
            boolean ai108Running = DataRepository.dataItemRunning().getAi108Running();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configAi108: ");
            sb.append(!ai108Running);
            Log.u(str, sb.toString());
            if (ai108Running) {
                DataRepository.dataItemConfig().getManuallyDualLens().reset(175);
            }
            DataRepository.dataItemRunning().setAi108Running(!ai108Running);
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.setTipsState(FragmentTopAlert.TIP_ULTRA_PIXEL, true);
                topAlert.updateConfigItem(171);
            }
            changeModeWithoutConfigureData(175, true);
        }
    }

    public void configBackSoftLightSwitch(String str) {
        getBaseModule().ifPresent(new O0000o(this));
    }

    public void configBeautySwitch(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            boolean z = moduleIndex == 162 || moduleIndex == 169;
            if (moduleIndex == 163 || moduleIndex == 165 || moduleIndex == 171 || moduleIndex == 161 || z) {
                ComponentConfigBeauty componentConfigBeauty = DataRepository.dataItemConfig().getComponentConfigBeauty();
                String nextValue = componentConfigBeauty.getNextValue(moduleIndex);
                String componentValue = componentConfigBeauty.getComponentValue(moduleIndex);
                String str = BeautyConstant.LEVEL_CLOSE;
                boolean z2 = (!str.equals(componentValue)) ^ (!str.equals(nextValue));
                componentConfigBeauty.setComponentValue(moduleIndex, nextValue);
                CameraStatUtils.trackBeautySwitchChanged(nextValue);
                if (z2 && z) {
                    if (moduleIndex != 162) {
                        DataItemGlobal dataItemGlobal = (DataItemGlobal) DataRepository.provider().dataGlobal();
                        DataRepository.dataItemRunning().switchOff("pref_video_speed_fast_key");
                        CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                        CameraSettings.setAiEnhancedVideoEnabled(moduleIndex, false);
                        dataItemGlobal.setCurrentMode(162);
                        CameraStatUtils.trackVideoModeChanged("normal");
                    }
                    changeModeWithoutConfigureData(162, true);
                } else if (!z2 || moduleIndex != 161) {
                    ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(13);
                } else {
                    changeModeWithoutConfigureData(161, true);
                }
            }
        }
    }

    public void configBokeh(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configBokeh: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        if (str.equals("on")) {
            updateTipMessage(4, R.string.bokeh_use_hint, 2);
        } else {
            hideTipMessage(R.string.bokeh_use_hint);
        }
        getBaseModule().ifPresent(O0000o0.INSTANCE);
    }

    public void configCinematicAspectRatio(int i) {
        if (isAlive()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                if (baseModule2.isFrameAvailable()) {
                    int moduleIndex = baseModule2.getModuleIndex();
                    boolean z = !CameraSettings.isCinematicAspectRatioEnabled(moduleIndex);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("configCinematicAspectRatio: ");
                    sb.append(z);
                    Log.u(str, sb.toString());
                    if (moduleIndex == 165) {
                        moduleIndex = 163;
                    }
                    CameraSettings.setCinematicAspectRatioEnabled(moduleIndex, z);
                    String str2 = "on";
                    String str3 = "off";
                    if (moduleIndex == 171 || moduleIndex == 163 || moduleIndex == 173) {
                        if (!z) {
                            str2 = str3;
                        }
                        MistatsWrapper.commonKeyTriggerEvent(BaseEvent.PARAM_PHOTO_RATIO_MOVIE, str2, null);
                        configRatio(true, null);
                    } else {
                        if (!z) {
                            str2 = str3;
                        }
                        MistatsWrapper.commonKeyTriggerEvent(BaseEvent.PARAM_VIDEO_RATIO_MOVIE, str2, null);
                        switchOffElementsSilent(216);
                        CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                        CameraSettings.setSuperEISEnabled(moduleIndex, false);
                        if (!C0122O00000o.instance().OOOooo()) {
                            CameraSettings.setVideoQuality8KOff(moduleIndex);
                        }
                        changeModeWithoutConfigureData(moduleIndex, false);
                    }
                }
            }
        }
    }

    public void configClone(Mode mode, boolean z) {
        int i;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configClone: mode=");
        sb.append(mode);
        sb.append(", enter=");
        sb.append(z);
        Log.u(str, sb.toString());
        if (z) {
            CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
            if (cloneProcess != null) {
                CloneChooser cloneChooser = (CloneChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(416);
                if (cloneChooser != null) {
                    cloneChooser.hide();
                }
                cloneProcess.prepare(mode, false);
                i = 185;
            } else {
                return;
            }
        } else {
            CloneProcess cloneProcess2 = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
            if (cloneProcess2 != null) {
                cloneProcess2.quit();
            }
            i = 210;
        }
        changeMode(i);
    }

    public void configDualWaterMarkSwitch() {
        boolean z;
        boolean isDualCameraWaterMarkOpen = CameraSettings.isDualCameraWaterMarkOpen();
        CameraStatUtils.trackDualWaterMarkChanged(!isDualCameraWaterMarkOpen);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configDualWaterMarkSwitch: ");
        sb.append(!isDualCameraWaterMarkOpen);
        Log.u(str, sb.toString());
        if (isDualCameraWaterMarkOpen) {
            hideTipMessage(R.string.hunt_dual_water_mark);
            z = false;
        } else {
            updateTipMessage(4, R.string.hunt_dual_water_mark, 2);
            z = true;
        }
        CameraSettings.setDualCameraWaterMarkOpen(z);
        getBaseModule().ifPresent(O0000OOo.INSTANCE);
    }

    public void configExposureFeedbackSwitch(int i) {
        String str = "pref_camera_exposure_feedback";
        boolean isSwitchOn = DataRepository.dataItemRunning().isSwitchOn(str);
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            if (1 == i) {
                isSwitchOn = !isSwitchOn;
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                if (1 == i) {
                    MistatsWrapper.moduleUIClickEvent(moduleIndex == 167 ? "M_manual_" : "M_proVideo_", Manual.MANUAL_FOCUS_PEAK, (Object) isSwitchOn ? "on" : "off");
                }
            }
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configExposureFeedbackSwitch: ");
            sb.append(isSwitchOn);
            Log.u(str2, sb.toString());
            DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
            if (isSwitchOn) {
                dataItemRunning.switchOn(str);
                DataRepository.dataItemRunning().switchOff("pref_camera_peak_key");
                EffectController.getInstance().setDrawPeaking(false);
            } else {
                dataItemRunning.switchOff(str);
            }
            EffectController.getInstance().setDrawExposure(isSwitchOn);
        }
    }

    public void configFPS960(String str) {
        ComponentConfigSlowMotion componentConfigSlowMotion = DataRepository.dataItemConfig().getComponentConfigSlowMotion();
        if (componentConfigSlowMotion.getItems().size() > 1) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configFPS960: ");
            sb.append(str);
            Log.u(str2, sb.toString());
            if (ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_480.equals(str) || ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_960.equals(str) || ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_1920.equals(str)) {
                setTipsState(FragmentTopAlert.TIP_480FPS_960FPS_DESC, true);
            }
            componentConfigSlowMotion.setComponentValue(172, str);
            changeModeWithoutConfigureData(172, false);
        }
    }

    public void configFilm(FilmItem filmItem, boolean z, boolean z2) {
        StringBuilder sb;
        String str = "configFilm: start=";
        String str2 = TAG;
        if (filmItem == null) {
            sb = new StringBuilder();
            sb.append(str);
            sb.append(z);
        } else {
            sb = new StringBuilder();
            sb.append(str);
            sb.append(z);
            sb.append(", filmItem.id=");
            sb.append(filmItem.id);
        }
        Log.u(str2, sb.toString());
        if (z) {
            DataRepository.dataItemLive().setCurrentFilmItem(filmItem);
            if (filmItem.id.equals("video_a")) {
                changeMode(189);
            } else if (filmItem.id.equals("video_b")) {
                changeMode(207);
            } else if (filmItem.id.equals("video_c")) {
                ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                if (configChanges == null) {
                    Log.w(TAG, "startShot ignore, configChanges is null");
                    return;
                } else {
                    Config.setCloneMode(Mode.TIMEFREEZE);
                    configChanges.configTimeFreeze(Mode.TIMEFREEZE);
                }
            } else if (filmItem.id.equals("video_d")) {
                changeMode(208);
            }
            if (filmItem.id.equals("video_e")) {
                ((FilmDreamProcessing) DataRepository.dataItemObservable().get(FilmDreamProcessing.class)).reset();
                changeMode(212);
            }
            String string = CameraAppImpl.getAndroidContext().getResources().getString(R.string.pref_camera_volumekey_function_entryvalue_volume);
            String string2 = CameraAppImpl.getAndroidContext().getResources().getString(R.string.pref_camera_volumekey_function_entryvalue_shutter);
            DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
            String str3 = CameraSettings.KEY_VOLUME_VIDEO_FUNCTION;
            String string3 = dataItemGlobal.getString(str3, string2);
            if (!string3.equals(string) && !string3.equals(string2)) {
                DataRepository.dataItemGlobal().putString(str3, string2);
            }
        } else {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                if (moduleIndex == 189 || moduleIndex == 207 || moduleIndex == 208 || moduleIndex == 212 || moduleIndex == 213) {
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null && moduleIndex == 208) {
                        topAlert.setAlertAnim(false);
                        topAlert.alertAiDetectTipHint(8, R.string.film_video_tip, -1);
                    }
                    changeMode(211);
                }
            }
        }
    }

    public void configFlash(String str, boolean z) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configFlash: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        if (!ModuleManager.isVideoNewSlowMotion()) {
            conflictWithFlashAndHdr();
        }
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && z) {
            topAlert.alertHDR(8, false, false);
        }
        getBaseModule().ifPresent(new C0403O0000ooO(this, z));
    }

    public void configFocusPeakSwitch(int i) {
        String str = "pref_camera_peak_key";
        boolean isSwitchOn = DataRepository.dataItemRunning().isSwitchOn(str);
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (1 == i) {
                isSwitchOn = !isSwitchOn;
                MistatsWrapper.moduleUIClickEvent(moduleIndex == 167 ? "M_manual_" : "M_proVideo_", Manual.MANUAL_FOCUS_PEAK, (Object) isSwitchOn ? "on" : "off");
            }
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configFocusPeakSwitch: ");
            sb.append(isSwitchOn);
            Log.u(str2, sb.toString());
            DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
            if (isSwitchOn) {
                dataItemRunning.switchOn(str);
                DataRepository.dataItemRunning().switchOff("pref_camera_exposure_feedback");
                EffectController.getInstance().setDrawExposure(false);
            } else {
                dataItemRunning.switchOff(str);
            }
            EffectController.getInstance().setDrawPeaking(isSwitchOn);
        }
    }

    public void configGenderAgeSwitch(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            boolean state = getState(i, "pref_camera_show_gender_age_key");
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configGenderAgeSwitch: ");
            sb.append(state);
            Log.u(str, sb.toString());
            if (1 == i) {
                trackGenderAgeChanged(state);
            }
            ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).setShowGenderAndAge(state);
            ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(38);
            Camera2Proxy cameraDevice = ((BaseModule) baseModule.get()).getCameraDevice();
            if (state) {
                if (cameraDevice != null) {
                    String string = CameraAppImpl.getAndroidContext().getResources().getString(R.string.face_age_info);
                    cameraDevice.setFaceWaterMarkEnable(true);
                    cameraDevice.setFaceWaterMarkFormat(string);
                }
            } else if (cameraDevice != null) {
                cameraDevice.setFaceWaterMarkEnable(false);
            }
        }
    }

    public void configGradienterSwitch(int i) {
        boolean z;
        boolean z2 = true;
        if (i == 2) {
            z = CameraSettings.isGradienterOn();
        } else if (i != 4) {
            boolean isGradienterOn = CameraSettings.isGradienterOn();
            CameraSettings.setGradienterOn(!isGradienterOn);
            z = !isGradienterOn;
        } else {
            CameraSettings.setGradienterOn(false);
            z = false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configGradienterSwitch: ");
        sb.append(z);
        Log.u(str, sb.toString());
        if (1 == i) {
            trackGradienterChanged(z);
        }
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            ((BaseModule) baseModule.get()).onGradienterSwitched(z);
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null) {
                if (DataRepository.dataItemGlobal().isNormalIntent() || !((BaseModule) baseModule.get()).getCameraCapabilities().isSupportLightTripartite()) {
                    mainContentProtocol.updateReferenceGradienterSwitched();
                } else {
                    mainContentProtocol.hideReferenceGradienter();
                }
            }
            BaseModule baseModule2 = (BaseModule) baseModule.get();
            if (z) {
                z2 = false;
            }
            baseModule2.showOrHideChip(z2);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.reConfigQrCodeTip();
            }
            if (z && CameraSettings.isAutoZoomEnabled(moduleIndex)) {
                CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                CameraSettings.setRetainZoom(1.0f, moduleIndex);
                changeModeWithoutConfigureData(162, false);
            }
        }
    }

    public void configGroupSwitch(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            boolean state = getState(i, "pref_camera_groupshot_mode_key");
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configGroupSwitch: ");
            sb.append(!state);
            Log.u(str, sb.toString());
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            Camera2Module camera2Module = (Camera2Module) baseModule.get();
            camera2Module.showOrHideChip(!state);
            boolean isBeautyPanelShow = isBeautyPanelShow();
            String str2 = SupportedConfigFactory.CLOSE_BY_GROUP;
            if (state) {
                if (!isBeautyPanelShow) {
                    updateTipMessage(17, R.string.hint_groupshot, 2);
                }
                closeMutexElement(str2, 193, 194, 196, 201, 254);
            } else {
                restoreAllMutexElement(str2);
                hideTipMessage(R.string.hint_groupshot);
            }
            camera2Module.onSharedPreferenceChanged();
            ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(42, 34, 30);
            bottomPopupTips.reConfigQrCodeTip();
        }
    }

    public void configHHTSwitch(int i) {
        boolean state = getState(i, "pref_camera_hand_night_key");
        if (1 == i) {
            trackHHTChanged(state);
        }
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configHHTSwitch: ");
            sb.append(state);
            Log.u(str, sb.toString());
            MutexModeManager mutexModePicker = ((BaseModule) baseModule.get()).getMutexModePicker();
            String str2 = SupportedConfigFactory.CLOSE_BY_HHT;
            if (state) {
                updateTipMessage(4, R.string.hine_hht, 2);
                closeMutexElement(str2, 193, 194);
                mutexModePicker.setMutexModeMandatory(3);
            } else {
                hideTipMessage(R.string.hine_hht);
                mutexModePicker.clearMandatoryFlag();
                ((BaseModule) baseModule.get()).resetMutexModeManually();
                restoreAllMutexElement(str2);
            }
        }
    }

    public void configHdr(String str) {
        if (!configVideoHdrIfNeed()) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configHdr: ");
            sb.append(str);
            Log.u(str2, sb.toString());
            if (getBaseModule().get() != null && (getBaseModule().get() instanceof Camera2Module)) {
                ((Camera2Module) getBaseModule().get()).setHdrModeChange(str);
            }
            conflictWithFlashAndHdr();
            ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
            int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
            if (!(getBaseModule().get() == null || "auto" == componentHdr.getComponentValue(currentMode) || !(getBaseModule().get() instanceof Camera2Module))) {
                ((Camera2Module) getBaseModule().get()).resetMagneticInfo();
            }
            getBaseModule().ifPresent(C0401O0000oo.INSTANCE);
            String str3 = "off";
            if (str3 != str && CameraSettings.isUltraPixelRearOn()) {
                configSwitchUltraPixel(3);
            }
            if (str3 != str && CameraSettings.isUltraPixelPortraitFrontOn()) {
                configUltraPixelPortrait(3);
            }
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && ((BaseModule) baseModule.get()).getCameraCapabilities().isMacroHdrMutex()) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                if (str3 != str && CameraSettings.isMacroModeEnabled(moduleIndex)) {
                    DataRepository.dataItemRunning().getComponentRunningMacroMode().setSwitchOff(moduleIndex);
                    CameraSettings.resetRetainZoom();
                    changeMode(currentMode);
                }
            }
            reConfigTipOfHdr(currentMode);
        }
    }

    public void configIDCard() {
        ActivityBase activityBase = this.mActivity;
        if (activityBase != null) {
            Log.u(TAG, "configIDCard");
            DataRepository.dataItemRunning().setEntranceMode(((BaseModule) getBaseModule().get()).getModuleIndex());
            ((ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179)).changeModeByNewMode(182, activityBase.getResources().getString(R.string.ai_scene_mode_id_card), 0);
            CameraStatUtils.trackGotoIDCard();
        }
    }

    public void configLiveReview() {
        Log.u(TAG, "configLiveReview");
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.delegateEvent(11);
        }
    }

    public void configLiveShotSwitch(int i) {
        if (isAlive()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                if (baseModule2.isFrameAvailable()) {
                    if ((baseModule2.getModuleIndex() == 163 || baseModule2.getModuleIndex() == 165) && C0122O00000o.instance().OOO0o0o()) {
                        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                        if (topAlert != null) {
                            Camera2Module camera2Module = (Camera2Module) baseModule2;
                            if (i == 1) {
                                boolean isLiveShotOn = CameraSettings.isLiveShotOn();
                                CameraSettings.setLiveShotOn(!isLiveShotOn);
                                String str = TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("configLiveShotSwitch: ");
                                sb.append(!isLiveShotOn);
                                Log.u(str, sb.toString());
                                if (isLiveShotOn) {
                                    camera2Module.stopLiveShot(false);
                                    topAlert.alertLiveShotHint(8, R.string.camera_liveshot_off_tip);
                                } else {
                                    configDocumentMode(3);
                                    configTimerBurst(3);
                                    if (!CameraSettings.isUltraPixelOn()) {
                                        if (camera2Module.getModuleIndex() == 165) {
                                            configRatio(false, DataRepository.dataItemConfig().getComponentConfigRatio().getDefaultValue(163));
                                        } else {
                                            camera2Module.startLiveShot();
                                            topAlert.alertLiveShotHint(0, R.string.camera_liveshot_on_tip);
                                        }
                                        setTipsState(FragmentTopAlert.TIP_LIVE_SHOT, true);
                                    } else {
                                        Log.d(TAG, "Ignore #startLiveShot in ultra pixel photography mode");
                                    }
                                }
                            } else if ((i == 3 || i == 4) && CameraSettings.isLiveShotOn()) {
                                Log.u(TAG, "configLiveShotSwitch: MUTEX false");
                                CameraSettings.setLiveShotOn(false);
                                camera2Module.stopLiveShot(false);
                            }
                            topAlert.updateConfigItem(206);
                        }
                    }
                }
            }
        }
    }

    public void configLiveVV(VVItem vVItem, VVWorkspaceItem vVWorkspaceItem, boolean z, boolean z2) {
        int i;
        VMProcessing vMProcessing = (VMProcessing) DataRepository.dataItemObservable().get(VMProcessing.class);
        vMProcessing.reset();
        vMProcessing.setCurrentWorkspaceItem(vVWorkspaceItem);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configLiveVV ");
        sb.append(z);
        Log.u(str, sb.toString());
        if (z) {
            LiveVVProcess liveVVProcess = (LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230);
            if (liveVVProcess != null) {
                ((LiveVVChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(229)).hide();
                liveVVProcess.prepare(vVItem, vVWorkspaceItem);
                DataRepository.dataItemLive().setCurrentVVItem(vVItem);
                i = 179;
            } else {
                return;
            }
        } else {
            if (z2) {
                configVV();
            } else {
                VVItem currentVVItem = DataRepository.dataItemLive().getCurrentVVItem();
                if (currentVVItem != null) {
                    int i2 = currentVVItem.index;
                }
                ((LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230)).quit();
            }
            i = 209;
        }
        changeMode(i);
    }

    public void configMagicFocusSwitch() {
        boolean triggerSwitchAndGet = DataRepository.dataItemRunning().triggerSwitchAndGet("pref_camera_ubifocus_key");
        trackMagicMirrorChanged(triggerSwitchAndGet);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configMagicFocusSwitch: ");
        sb.append(triggerSwitchAndGet);
        Log.u(str, sb.toString());
    }

    public void configMagicMirrorSwitch(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            boolean state = getState(i, "pref_camera_magic_mirror_key");
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configMagicMirrorSwitch: ");
            sb.append(state);
            Log.u(str, sb.toString());
            if (1 == i) {
                trackMagicMirrorChanged(state);
            }
            ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).setShowMagicMirror(state);
            ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(39);
            Camera2Proxy cameraDevice = ((BaseModule) baseModule.get()).getCameraDevice();
            if (state) {
                if (cameraDevice != null) {
                    String string = CameraAppImpl.getAndroidContext().getResources().getString(R.string.face_score_info);
                    cameraDevice.setFaceWaterMarkEnable(true);
                    cameraDevice.setFaceWaterMarkFormat(string);
                }
            } else if (cameraDevice != null) {
                cameraDevice.setFaceWaterMarkEnable(false);
            }
        }
    }

    public void configMeter(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configMeter: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        reCheckParameterResetTip(false);
        getBaseModule().ifPresent(new Consumer() {
            public void accept(BaseModule baseModule) {
                int valueSelectedStringIdIgnoreClose = DataRepository.dataItemConfig().getComponentConfigMeter().getValueSelectedStringIdIgnoreClose(((BaseModule) ConfigChangeImpl.this.getBaseModule().get()).getModuleIndex());
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.alertSwitchTip(FragmentTopAlert.TIP_METER, 0, valueSelectedStringIdIgnoreClose);
                }
            }
        });
        getBaseModule().ifPresent(C0396O0000o0o.INSTANCE);
    }

    public void configModeEdit() {
        if (this.mActivity != null) {
            Log.u(TAG, "configModeEdit");
            Intent intent = new Intent(this.mActivity, ModeEditorActivity.class);
            intent.putExtra("from_where", ModuleManager.getActiveModuleIndex());
            if (this.mActivity.startFromKeyguard()) {
                intent.putExtra("StartActivityWhenLocked", true);
            }
            this.mActivity.startActivity(intent);
            this.mActivity.setJumpFlag(7);
            MistatsWrapper.customizeCameraSettingClick(CUSTOMIZE_CAMERA.PREF_KEY_CUSTOM_MODE_ICON);
        }
    }

    public void configModeMore(boolean z) {
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.delegateEvent(z ? 31 : 30);
        }
    }

    public void configNearRangeMode() {
        getBaseModule().ifPresent(C0404O0000ooo.INSTANCE);
    }

    public void configPortraitSwitch(int i) {
        getBaseModule().ifPresent(O0000o00.INSTANCE);
    }

    public void configRGBHistogramSwitch(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            BaseModule baseModule2 = (BaseModule) baseModule.get();
            if (baseModule2.isFrameAvailable()) {
                int moduleIndex = baseModule2.getModuleIndex();
                boolean isProVideoHistogramOpen = CameraSettings.isProVideoHistogramOpen(moduleIndex);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configRGBHistogramSwitch: ");
                sb.append(!isProVideoHistogramOpen);
                Log.u(str, sb.toString());
                CameraSettings.setProVideoHistogramOpen(!isProVideoHistogramOpen);
                MistatsWrapper.moduleUIClickEvent("M_proVideo_", Manual.HISTOGRAM, (Object) isProVideoHistogramOpen ? "off" : "on");
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.updateRGBHistogramSwitched(!isProVideoHistogramOpen);
                }
                changeModeWithoutConfigureData(moduleIndex, false);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00cc, code lost:
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00d6, code lost:
        r5 = r0;
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00d8, code lost:
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00d9, code lost:
        if (r0 == false) goto L_0x00e6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00db, code lost:
        com.android.camera.data.DataRepository.dataItemRunning().getComponentRunningAIWatermark().setAIWatermarkEnable(false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00e6, code lost:
        if (r1 == false) goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00ec, code lost:
        if (com.android.camera.CameraSettings.isUltraPixelOn() == false) goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00ee, code lost:
        switchOffElementsSilent(209);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00f7, code lost:
        if (r9 != false) goto L_0x0112;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00f9, code lost:
        r9 = TAG;
        r0 = new java.lang.StringBuilder();
        r0.append("configRatio: ");
        r0.append(r10);
        com.android.camera.log.Log.u(r9, r0.toString());
        r2.setComponentValue(r5, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0112, code lost:
        com.android.camera.data.DataRepository.dataItemGlobal().setCurrentMode(r5);
        changeModeWithoutConfigureData(r5, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x011c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void configRatio(boolean z, String str) {
        if (isAlive()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                if (baseModule2.isFrameAvailable()) {
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    int moduleIndex = baseModule2.getModuleIndex();
                    ComponentConfigRatio componentConfigRatio = DataRepository.dataItemConfig().getComponentConfigRatio();
                    if (z) {
                        str = componentConfigRatio.getDefaultValue(moduleIndex);
                    } else {
                        CameraStatUtils.trackPictureSize(moduleIndex, str);
                        if (DataRepository.dataItemRunning().reConfigCinematicAspectRatioIfRatioChanged(moduleIndex, str) && topAlert != null) {
                            topAlert.updateConfigItem(251);
                        }
                    }
                    boolean isCinematicAspectRatioEnabled = CameraSettings.isCinematicAspectRatioEnabled(moduleIndex);
                    String str2 = ComponentConfigRatio.RATIO_16X9;
                    if (isCinematicAspectRatioEnabled) {
                        z = true;
                        str = str2;
                    }
                    char c = 65535;
                    switch (str.hashCode()) {
                        case 50858:
                            if (str.equals(ComponentConfigRatio.RATIO_1X1)) {
                                c = 6;
                                break;
                            }
                            break;
                        case 53743:
                            if (str.equals(ComponentConfigRatio.RATIO_4X3)) {
                                c = 0;
                                break;
                            }
                            break;
                        case 1515430:
                            if (str.equals(str2)) {
                                c = 1;
                                break;
                            }
                            break;
                        case 1517352:
                            if (str.equals(ComponentConfigRatio.RATIO_FULL_18X9)) {
                                c = 2;
                                break;
                            }
                            break;
                        case 1518313:
                            if (str.equals(ComponentConfigRatio.RATIO_FULL_19X9)) {
                                c = 3;
                                break;
                            }
                            break;
                        case 1539455:
                            if (str.equals(ComponentConfigRatio.RATIO_FULL_20X9)) {
                                c = 5;
                                break;
                            }
                            break;
                        case 1456894192:
                            if (str.equals(ComponentConfigRatio.RATIO_FULL_195X9)) {
                                c = 4;
                                break;
                            }
                            break;
                    }
                    int i = 165;
                    switch (c) {
                        case 0:
                            if (moduleIndex == 165 || moduleIndex == 163) {
                                boolean z2 = false;
                                boolean z3 = false;
                                i = 163;
                                break;
                            }
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            if (moduleIndex == 165 || moduleIndex == 163) {
                                moduleIndex = 163;
                            }
                            i = moduleIndex;
                            break;
                        case 6:
                            componentConfigRatio.setComponentValue(moduleIndex, componentConfigRatio.getDefaultValue(moduleIndex));
                            if (moduleIndex == 165 || moduleIndex == 163) {
                                configLiveShotSwitch(3);
                                break;
                            }
                    }
                }
            }
        }
    }

    public void configRawSwitch(int i) {
        String str;
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            ComponentConfigRaw componentConfigRaw = DataRepository.dataItemConfig().getComponentConfigRaw();
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            boolean isSwitchOn = componentConfigRaw.isSwitchOn(moduleIndex);
            CameraCapabilities cameraCapabilities = ((BaseModule) baseModule.get()).getCameraCapabilities();
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configRawSwitch: ");
            sb.append(!isSwitchOn);
            Log.u(str2, sb.toString());
            if (i == 1) {
                String str3 = "raw";
                String str4 = "M_manual_";
                if (isSwitchOn) {
                    componentConfigRaw.setRaw(moduleIndex, false);
                    str = "off";
                } else {
                    componentConfigRaw.setRaw(moduleIndex, true);
                    if (C0122O00000o.instance().O000000o(cameraCapabilities)) {
                        closeMutexElement("n", 209);
                    }
                    str = "on";
                }
                MistatsWrapper.moduleUIClickEvent(str4, str3, (Object) str);
                changeModeWithoutConfigureData(moduleIndex, false);
                reCheckRaw();
            } else if (i != 2) {
            }
        }
    }

    public void configRotationChange(int i, int i2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configRotationChange: show=");
        sb.append(i);
        sb.append(", degree=");
        sb.append(i2);
        Log.u(str, sb.toString());
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        boolean z = true;
        if (!(i2 == 0 || i2 == 90)) {
            if (i2 == 180) {
                if (topAlert != null) {
                    topAlert.updateLyingDirectHint(false, false);
                }
                if (bottomPopupTips != null) {
                    if (i != 1) {
                        z = false;
                    }
                    bottomPopupTips.updateLyingDirectHint(z, false);
                    return;
                }
                return;
            } else if (i2 != 270) {
                return;
            }
        }
        if (bottomPopupTips != null) {
            bottomPopupTips.updateLyingDirectHint(false, false);
        }
        if (topAlert != null) {
            if (i != 1) {
                z = false;
            }
            topAlert.updateLyingDirectHint(z, false);
        }
    }

    public void configScene(int i) {
        final Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            boolean state = getState(i, "pref_camera_scenemode_setting_key");
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configScene: ");
            sb.append(state);
            Log.u(str, sb.toString());
            ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            if (state) {
                bottomPopupTips.hideTipImage();
                if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                    miBeautyProtocol.dismiss(2);
                }
                manuallyAdjust.setManuallyVisible(2, 1, new ManuallyListener() {
                    public void onManuallyDataChanged(ComponentData componentData, String str, String str2, boolean z, int i) {
                        ((BaseModule) baseModule.get()).onSharedPreferenceChanged();
                        ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(4);
                    }
                });
            } else {
                bottomPopupTips.reInitTipImage();
                manuallyAdjust.setManuallyVisible(2, i == 1 ? 4 : 3, null);
            }
            ((BaseModule) baseModule.get()).onSharedPreferenceChanged();
            ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(4);
        }
    }

    public void configSuperResolutionSwitch(int i) {
        boolean state = getState(i, "pref_camera_super_resolution_key");
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configSuperResolutionSwitch: ");
        sb.append(state);
        Log.u(str, sb.toString());
        if (1 == i) {
            trackSuperResolutionChanged(state);
        }
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            MutexModeManager mutexModePicker = ((BaseModule) baseModule.get()).getMutexModePicker();
            String str2 = SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION;
            if (state) {
                closeMutexElement(str2, 193, 194);
                mutexModePicker.setMutexModeMandatory(9);
            } else {
                mutexModePicker.clearMandatoryFlag();
                ((BaseModule) baseModule.get()).resetMutexModeManually();
                restoreAllMutexElement(str2);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0104  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0124  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void configSwitchUltraPixel(int i) {
        char c;
        int[] iArr;
        int i2 = i;
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (!(topAlert == null || this.mActivity == null)) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                int moduleIndex = baseModule2.getModuleIndex();
                boolean isUltraPixelOn = CameraSettings.isUltraPixelOn();
                boolean z = !isUltraPixelOn;
                ComponentRunningUltraPixel componentUltraPixel = DataRepository.dataItemRunning().getComponentUltraPixel();
                String currentSupportUltraPixel = componentUltraPixel.getCurrentSupportUltraPixel();
                CameraCapabilities cameraCapabilities = ((BaseModule) baseModule.get()).getCameraCapabilities();
                String str = ComponentRunningUltraPixel.ULTRA_PIXEL_ON_REAR_48M;
                String str2 = FragmentTopAlert.TIP_ULTRA_PIXEL;
                String str3 = SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL;
                if (i2 == 1) {
                    String str4 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("configSwitchUltraPixel: ");
                    sb.append(z);
                    Log.u(str4, sb.toString());
                    if (CameraSettings.isUltraWideConfigOpen(moduleIndex)) {
                        CameraSettings.setUltraWideConfig(moduleIndex, false);
                        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                        bottomPopupTips.updateLeftTipImage();
                        bottomPopupTips.directlyHideTips();
                    }
                    configDocumentMode(3);
                    if (z) {
                        int hashCode = currentSupportUltraPixel.hashCode();
                        if (hashCode != -1379357773) {
                            switch (hashCode) {
                                case -70725170:
                                    if (currentSupportUltraPixel.equals(ComponentRunningUltraPixel.ULTRA_PIXEL_ON_REAR_64M)) {
                                        c = 2;
                                        break;
                                    }
                                case -70725169:
                                    if (currentSupportUltraPixel.equals(str)) {
                                        c = 0;
                                        break;
                                    }
                                case -70725168:
                                    if (currentSupportUltraPixel.equals(ComponentRunningUltraPixel.ULTRA_PIXEL_ON_REAR_108M)) {
                                        c = 3;
                                        break;
                                    }
                            }
                        } else if (currentSupportUltraPixel.equals(ComponentRunningUltraPixel.ULTRA_PIXEL_ON_FRONT_32M)) {
                            c = 1;
                            if (c == 0) {
                                if (c == 1) {
                                    iArr = new int[]{196, 201, 206};
                                } else if ((c == 2 || c == 3) && C0122O00000o.instance().O000000o(cameraCapabilities)) {
                                    iArr = new int[]{237};
                                }
                                closeMutexElement(str3, iArr);
                            } else {
                                int[] iArr2 = {194, 239, 201, 206};
                                if (C0122O00000o.instance().O000000o(cameraCapabilities)) {
                                    iArr2 = Arrays.copyOf(iArr2, iArr2.length + 1);
                                    iArr2[iArr2.length - 1] = 237;
                                }
                                closeMutexElement(str3, iArr2);
                            }
                            DataRepository.dataItemRunning().setRecordingClosedElements(this.mRecordingClosedElements);
                            CameraSettings.switchOnUltraPixel(currentSupportUltraPixel);
                            configTimerBurst(3);
                        }
                        c = 65535;
                        if (c == 0) {
                        }
                        DataRepository.dataItemRunning().setRecordingClosedElements(this.mRecordingClosedElements);
                        CameraSettings.switchOnUltraPixel(currentSupportUltraPixel);
                        configTimerBurst(3);
                    } else {
                        this.mRecordingClosedElements = DataRepository.dataItemRunning().getRecordingClosedElements();
                        restoreAllMutexElement(str3);
                        CameraSettings.switchOffUltraPixel();
                    }
                    MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                    if (miBeautyProtocol != null ? miBeautyProtocol.isBeautyPanelShow() : false) {
                        miBeautyProtocol.dismiss(2);
                    }
                    if (baseModule2.getModuleIndex() == 165) {
                        changeMode(163);
                    } else {
                        CameraSettings.resetRetainZoom();
                        changeModeWithoutConfigureData(baseModule2.getModuleIndex(), false);
                    }
                    if (z) {
                        setTipsState(str2, true);
                    } else {
                        topAlert.alertSwitchTip(str2, 8, componentUltraPixel.getUltraPixelCloseTip());
                    }
                    if (baseModule2.getModuleIndex() == 167) {
                        StringBuilder sb2 = new StringBuilder(16);
                        sb2.append(String.valueOf(z));
                        sb2.append(currentSupportUltraPixel);
                        MistatsWrapper.moduleUIClickEvent("M_manual_", Manual.SUPERME_PIXEL, (Object) sb2.toString());
                    }
                } else if (i2 == 3 && isUltraPixelOn) {
                    Log.u(TAG, "configSwitchUltraPixel: MUTEX false");
                    this.mRecordingClosedElements = DataRepository.dataItemRunning().getRecordingClosedElements();
                    if (this.mRecordingClosedElements != null) {
                        restoreAllMutexElement(str3);
                    }
                    CameraSettings.switchOffUltraPixel();
                    if (DataRepository.dataItemRunning().getLastUiStyle() == 3) {
                        changeMode(baseModule2.getModuleIndex());
                    } else {
                        baseModule2.restartModule();
                    }
                    topAlert.alertSwitchTip(str2, 8, componentUltraPixel.getUltraPixelCloseTip());
                }
                BottomPopupTips bottomPopupTips2 = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                MiBeautyProtocol miBeautyProtocol2 = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                if (z) {
                    if (str.equals(currentSupportUltraPixel) && bottomPopupTips2 != null) {
                        bottomPopupTips2.directHideTipImage();
                        bottomPopupTips2.directShowOrHideLeftTipImage(false);
                        bottomPopupTips2.hideQrCodeTip();
                    }
                    if (dualController != null) {
                        dualController.hideZoomButton();
                    }
                } else {
                    boolean z2 = false;
                    if (miBeautyProtocol2 != null) {
                        z2 = miBeautyProtocol2.isBeautyPanelShow();
                    }
                    if (bottomPopupTips2 != null && !z2) {
                        bottomPopupTips2.reInitTipImage();
                    }
                    if (dualController != null && !z2) {
                        if (moduleIndex != 167) {
                            dualController.showZoomButton();
                        }
                        if (topAlert != null) {
                            topAlert.clearAlertStatus();
                        }
                    }
                }
            }
        }
    }

    public void configTiltSwitch(int i) {
        Optional baseModule = getBaseModule();
        BaseModule baseModule2 = (BaseModule) baseModule.get();
        if (baseModule.isPresent() && (baseModule2 instanceof Camera2Module)) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                ComponentRunningTiltValue componentRunningTiltValue = DataRepository.dataItemRunning().getComponentRunningTiltValue();
                boolean isSwitchOn = componentRunningTiltValue.isSwitchOn(160);
                if (i == 1) {
                    if (!isSwitchOn) {
                        CameraStatUtils.trackTiltShiftChanged(ComponentRunningTiltValue.TILT_CIRCLE);
                        componentRunningTiltValue.toSwitch(160, true);
                        configDocumentMode(3);
                        configTimerBurst(3);
                        isSwitchOn = true;
                    } else {
                        CameraStatUtils.trackTiltShiftChanged("off");
                        componentRunningTiltValue.toSwitch(160, false);
                        isSwitchOn = false;
                    }
                    ((Camera2Module) baseModule.get()).showOrHideChip(!isSwitchOn);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("configTiltSwitch: ");
                    sb.append(isSwitchOn);
                    Log.u(str, sb.toString());
                } else if (i != 2 && i == 3) {
                    Log.u(TAG, "configTiltSwitch: MUTEX false");
                    componentRunningTiltValue.toSwitch(160, false);
                    isSwitchOn = false;
                }
                topAlert.alertSlideSwitchLayout(isSwitchOn, 228);
                ((Camera2Module) baseModule.get()).onTiltShiftSwitched(isSwitchOn);
                EffectController.getInstance().setDrawTilt(isSwitchOn);
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (bottomPopupTips != null) {
                    bottomPopupTips.reConfigQrCodeTip();
                }
            }
        }
    }

    public void configTimeFreeze(Mode mode) {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null) {
            CloneChooser cloneChooser = (CloneChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(416);
            if (cloneChooser != null) {
                cloneChooser.hide();
            }
            cloneProcess.prepare(mode, false);
            changeMode(213);
        }
    }

    public void configTimerSwitch(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configTimerSwitch: ");
        sb.append(str);
        Log.u(str2, sb.toString());
        ComponentRunningTimer componentRunningTimer = DataRepository.dataItemRunning().getComponentRunningTimer();
        CameraStatUtils.trackTimerChanged(str);
        componentRunningTimer.setComponentValue(160, str);
    }

    public void configUltraPixelPortrait(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
            String str = "pref_camera_ultra_pixel_portrait_mode_key";
            boolean isSwitchOn = dataItemRunning.isSwitchOn(str);
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            String str2 = SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT;
            String str3 = FragmentTopAlert.TIP_ULTRA_PIXEL_PORTRAIT;
            if (i == 1) {
                String str4 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("configUltraPixelPortrait: ");
                sb.append(!isSwitchOn);
                Log.u(str4, sb.toString());
                if (isSwitchOn) {
                    dataItemRunning.switchOff(str);
                    topAlert.alertSwitchTip(str3, 8, (int) R.string.ultra_pixel_portrait_hint);
                    this.mRecordingClosedElements = DataRepository.dataItemRunning().getRecordingClosedElements();
                    restoreAllMutexElement(str2);
                } else {
                    dataItemRunning.switchOn(str);
                    closeMutexElement(str2, 194, 196, 201, 239, 254);
                    dataItemRunning.setRecordingClosedElements(this.mRecordingClosedElements);
                    MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                    if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                        miBeautyProtocol.dismiss(2);
                    }
                    topAlert.alertSwitchTip(str3, 0, (int) R.string.ultra_pixel_portrait_hint);
                }
                trackUltraPixelPortraitChanged(!isSwitchOn);
            } else if (i == 3 && isSwitchOn) {
                Log.u(TAG, "configUltraPixelPortrait: MUTEX false");
                topAlert.alertSwitchTip(str3, 8, (int) R.string.ultra_pixel_portrait_hint);
                this.mRecordingClosedElements = DataRepository.dataItemRunning().getRecordingClosedElements();
                if (this.mRecordingClosedElements != null) {
                    restoreAllMutexElement(str2);
                }
                dataItemRunning.switchOff(str);
            }
            ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(57);
            ((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175)).updateTipImage();
            topAlert.updateConfigItem(215);
        }
    }

    public void configVideoFast() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex != 169) {
                CameraStatUtils.trackVideoModeChanged(CameraSettings.VIDEO_SPEED_FAST);
                switchOffElementsSilent(216);
                CameraSettings.setAutoZoomEnabled(moduleIndex, false);
                CameraSettings.setAiEnhancedVideoEnabled(moduleIndex, false);
                CameraSettings.setSuperEISEnabled(moduleIndex, false);
                singeSwitchVideoBeauty(false);
                CameraSettings.setVideoMasterFilter(0);
                if (CameraSettings.isMacroModeEnabled(moduleIndex)) {
                    DataRepository.dataItemRunning().getComponentRunningMacroMode().setSwitchOff(moduleIndex);
                }
                CameraSettings.setSubtitleEnabled(moduleIndex, false);
                CameraSettings.setVideoQuality8KOff(moduleIndex);
                changeMode(169);
                updateTipMessage(4, R.string.hint_fast_motion, 2);
            } else {
                hideTipMessage(R.string.hint_fast_motion);
                dataItemRunning.switchOff("pref_video_speed_fast_key");
                CameraStatUtils.trackVideoModeChanged("normal");
                DataRepository.dataItemGlobal().setCurrentMode(162);
                changeMode(162);
            }
        }
    }

    public void configVideoLogSwitch(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            boolean isProVideoLogOpen = CameraSettings.isProVideoLogOpen(moduleIndex);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configVideoLogSwitch: ");
            sb.append(!isProVideoLogOpen);
            Log.u(str, sb.toString());
            CameraSettings.setProVideoLog(!isProVideoLogOpen);
            MistatsWrapper.moduleUIClickEvent("M_proVideo_", Manual.LOG, (Object) isProVideoLogOpen ? "off" : "on");
            if (!isProVideoLogOpen) {
                CameraSettings.resetRetainZoom();
                resetVideoFilter();
                CameraSettings.setVideoMasterFilter(0);
                MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                    miBeautyProtocol.dismiss(3);
                }
                MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
                if (masterFilterProtocol != null && masterFilterProtocol.isShowing()) {
                    masterFilterProtocol.dismiss(2, 5);
                }
                CameraSettings.setVideoQuality8KOff(moduleIndex);
                DataRepository.dataItemConfig().getManuallyDualLens().setComponentValue(moduleIndex, ComponentManuallyDualLens.LENS_WIDE);
                ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
                if (componentRunningMacroMode.isSwitchOn(moduleIndex)) {
                    componentRunningMacroMode.setSwitchOff(moduleIndex);
                }
            }
            changeModeWithoutConfigureData(moduleIndex, false);
        }
    }

    public void findBestWatermarkItem(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(i);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0077, code lost:
        if (r14 == 206) goto L_0x008a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x008c, code lost:
        if (r14 == 229) goto L_0x008a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onConfigChanged(int i) {
        if (isAlive()) {
            ConfigChangeInterceptor configChangeInterceptor = this.mChangeInterceptor;
            if (configChangeInterceptor == null || !configChangeInterceptor.consumeConfigChanged(i) || !this.mChangeInterceptor.asBlocker()) {
                Optional baseModule = getBaseModule();
                if (!baseModule.isPresent() || !((BaseModule) baseModule.get()).dispatchConfigChange(i)) {
                    if (SupportedConfigFactory.isMutexConfig(i)) {
                        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
                        int[] iArr = SupportedConfigFactory.MUTEX_MENU_CONFIGS;
                        int length = iArr.length;
                        boolean z = false;
                        int i2 = 176;
                        for (int i3 = 0; i3 < length; i3++) {
                            int i4 = iArr[i3];
                            if (!(i4 == i || ((i == 209 && i4 == 229) || (i == 229 && i4 == 209)))) {
                                if (i4 != 203) {
                                    if (i4 != 206) {
                                        if (i4 != 209) {
                                            if (i4 != 229) {
                                                if (!dataItemRunning.isSwitchOn(SupportedConfigFactory.getConfigKey(i4))) {
                                                }
                                                i2 = i4;
                                            } else if (!CameraSettings.isGradienterOn()) {
                                            }
                                        } else if (CameraSettings.isUltraPixelOn()) {
                                            z = true;
                                            i2 = i4;
                                        }
                                    } else if (CameraSettings.isLiveShotOn()) {
                                        if (i != 209) {
                                        }
                                    }
                                    i2 = 176;
                                } else if (((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).isShowLightingView()) {
                                    showOrHideLighting(false);
                                }
                            }
                        }
                        if (z) {
                            applyConfig(i, 1);
                            if (i2 != 176) {
                                applyConfig(i2, 3);
                            }
                        } else if (i2 != 176) {
                            applyConfig(i2, 3);
                        }
                    }
                    applyConfig(i, 1);
                }
            }
        }
    }

    public void onConfigValueChanged(int i, String str) {
        if (isAlive()) {
            applyConfigValue(i, str);
        }
    }

    public void onShineDismiss(int i) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex == 167 || moduleIndex == 180) {
                ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
                if (manuallyAdjust != null) {
                    manuallyAdjust.setManuallyLayoutVisible(true);
                }
            } else if (moduleIndex == 187) {
                AmbilightSelector ambilightSelector = (AmbilightSelector) ModeCoordinatorImpl.getInstance().getAttachProtocol(421);
                if (ambilightSelector != null) {
                    ambilightSelector.setSelectorLayoutVisible(true);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a5 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00a6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onThermalNotification(int i) {
        String str;
        String str2;
        String str3;
        String str4;
        if (isAlive()) {
            Optional baseModule = getBaseModule();
            if (!baseModule.isPresent()) {
                str = TAG;
                str2 = "onThermalNotification current module is null";
            } else {
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                if (!baseModule2.isFrameAvailable() || baseModule2.isSelectingCapturedResult()) {
                    str = TAG;
                    str2 = "onThermalNotification current module has not ready";
                } else {
                    baseModule2.setThermalLevel(i);
                    if (ThermalDetector.getInstance().thermalConstrained()) {
                        Log.w(TAG, "thermalConstrained");
                        baseModule2.thermalConstrained();
                    }
                    ComponentConfigFlash componentFlash = DataRepository.dataItemConfig().getComponentFlash();
                    if (componentFlash.isEmpty() || !componentFlash.isHardwareSupported()) {
                        str = TAG;
                        str2 = "onThermalNotification don't support hardware flash";
                    } else {
                        baseModule2.updatePreferenceInWorkThread(66);
                        String str5 = "0";
                        if (ThermalDetector.getInstance().thermalCloseFlash()) {
                            str3 = TAG;
                            str4 = "thermalCloseFlash";
                        } else if (!baseModule2.isThermalThreshold() || ((!ThermalDetector.getInstance().thermalCloseFront() || !CameraSettings.isFrontCamera()) && !ThermalDetector.getInstance().thermalCloseBoth())) {
                            str5 = "";
                            if (TextUtils.isEmpty(str5)) {
                                updateFlashModeAndRefreshUI(baseModule2, str5);
                                return;
                            }
                            return;
                        } else {
                            str3 = TAG;
                            str4 = "recording time is up to thermal threshold";
                        }
                        Log.w(str3, str4);
                        if (TextUtils.isEmpty(str5)) {
                        }
                    }
                }
            }
            Log.w(str, str2);
        }
    }

    public void reCheckAIWatermark(boolean z) {
        boolean OOOoooo = C0122O00000o.instance().OOOoooo();
        boolean OOOOo0o = C0122O00000o.instance().OOOOo0o();
        if (OOOoooo || OOOOo0o) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                ComponentRunningAIWatermark componentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
                if (moduleIndex == 205 || moduleIndex == 188) {
                    componentRunningAIWatermark.setAIWatermarkEnable(true);
                    WatermarkItem majorWatermarkItem = componentRunningAIWatermark.getMajorWatermarkItem();
                    boolean z2 = moduleIndex == 205;
                    int i = 2;
                    if (majorWatermarkItem != null) {
                        int type = majorWatermarkItem.getType();
                        z2 = (type == 0 || type == 1 || type == 2 || type == 3 || type == 4 || type == 11 || type == 12) ? false : true;
                    }
                    if (z2) {
                        AIWatermarkDetect aIWatermarkDetect = (AIWatermarkDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(254);
                        if (aIWatermarkDetect != null) {
                            aIWatermarkDetect.resetResult();
                        }
                        showWatermarkSample(true);
                        updateASDForWatermark();
                    } else {
                        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
                        if (mainContentProtocol != null) {
                            mainContentProtocol.updateWatermarkSample(majorWatermarkItem);
                            int upDistance = getUpDistance();
                            if (upDistance > 0) {
                                i = 1;
                            }
                            mainContentProtocol.moveWatermarkLayout(i, upDistance);
                        }
                    }
                    configTimerBurst(3);
                } else {
                    boolean aIWatermarkEnable = componentRunningAIWatermark.getAIWatermarkEnable(moduleIndex);
                    boolean needForceDisable = componentRunningAIWatermark.needForceDisable(moduleIndex);
                    if (!aIWatermarkEnable || !needForceDisable) {
                        if (aIWatermarkEnable) {
                            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                            if (topAlert != null) {
                                topAlert.alertSwitchTip("ai_watermark", 0, (int) R.string.ai_watermark_title);
                            }
                        }
                        if (!z) {
                            boolean iWatermarkEnable = componentRunningAIWatermark.getIWatermarkEnable();
                            if (aIWatermarkEnable && iWatermarkEnable) {
                                AIWatermarkDetect aIWatermarkDetect2 = (AIWatermarkDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(254);
                                if (aIWatermarkDetect2 != null) {
                                    aIWatermarkDetect2.resetResult();
                                }
                                setWatermarkEnable(true);
                            }
                            if (moduleIndex == 163 || moduleIndex == 165 || moduleIndex == 188) {
                                showDualController(aIWatermarkEnable);
                            }
                        }
                    } else {
                        showWatermarkSample(false);
                        componentRunningAIWatermark.setAIWatermarkEnable(false);
                    }
                }
            }
        }
    }

    public void reCheckAiAudio() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                int currentStringRes = DataRepository.dataItemRunning().getComponentRunningAiAudio().getCurrentStringRes(moduleIndex);
                topAlert.alertAiAudioBGHint(currentStringRes != -1 ? 0 : 8, currentStringRes);
            }
        }
    }

    public void reCheckAiEnhancedVideo() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                if (CameraSettings.isAiEnhancedVideoEnabled(baseModule2.getModuleIndex()) && !isVideoRecoding(baseModule2)) {
                    topAlert.alertAiEnhancedVideoHint(0, R.string.pref_camera_video_ai_scene_title);
                }
            }
        }
    }

    public void reCheckAiScene() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent() && CameraSettings.getAiSceneOpen(((BaseModule) baseModule.get()).getModuleIndex())) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.alertSwitchTip("ai", 0, (int) R.string.pref_camera_front_ai_scene_entry_on);
            }
        }
    }

    public void reCheckAmbilight() {
        if (C0122O00000o.instance().oO0OO0()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && ((BaseModule) baseModule.get()).getModuleIndex() == 187) {
                AmbilightSelector ambilightSelector = (AmbilightSelector) ModeCoordinatorImpl.getInstance().getAttachProtocol(421);
                if (ambilightSelector != null) {
                    ambilightSelector.updateTips();
                }
            }
        }
    }

    public void reCheckColorEnhance() {
        if (C0122O00000o.instance().supportColorEnhance()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                if (DataRepository.dataItemRunning().getComponentRunningColorEnhance().isEnabled(((BaseModule) baseModule.get()).getModuleIndex())) {
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null) {
                        topAlert.alertProColourHint(0, R.string.pro_color_mode);
                    }
                }
            }
        }
    }

    public void reCheckDocumentMode() {
        if (C0122O00000o.instance().OO0ooo0() || C0122O00000o.instance().OO0ooo()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
            if (topAlert != null) {
                Optional baseModule = getBaseModule();
                if (baseModule.isPresent() && CameraSettings.isDocumentModeOn(((BaseModule) baseModule.get()).getModuleIndex())) {
                    topAlert.alertSlideSwitchLayout(true, 221);
                    getBaseModule().ifPresent(O0000Oo0.INSTANCE);
                }
            }
        }
    }

    public void reCheckDualVideoMode() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        Optional baseModule = getBaseModule();
        boolean ismDrawSelectWindow = CameraSettings.getDualVideoConfig().ismDrawSelectWindow();
        boolean OOO000o = C0122O00000o.instance().OOO000o();
        if (baseModule.isPresent()) {
            BaseModule baseModule2 = (BaseModule) baseModule.get();
            StandaloneRecorderProtocol standaloneRecorderProtocol = (StandaloneRecorderProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(429);
            boolean z = standaloneRecorderProtocol != null && standaloneRecorderProtocol.getRecorderManager(null).isRecording();
            if (baseModule2.getModuleIndex() == 204) {
                if (ismDrawSelectWindow && !z) {
                    topAlert.alertSlideSwitchLayout(true, SupportedConfigFactory.DUAL_VIDEO);
                }
                if (!ismDrawSelectWindow && !z && OOO000o) {
                    topAlert.alertDualVideoHint(0, ModuleUtil.getTopTipRes());
                }
            }
        }
    }

    public void reCheckExposureFeedbackConfig() {
        if (isAlive()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && ((BaseModule) baseModule.get()).isCreated()) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                if ((moduleIndex == 180 || moduleIndex == 167) && DataRepository.dataItemRunning().isSwitchOn("pref_camera_exposure_feedback")) {
                    Log.d(TAG, "reCheckExposureFeedbackConfig: configExposureFeedbackSwitch");
                    configExposureFeedbackSwitch(2);
                }
            }
        }
    }

    public void reCheckEyeLight() {
        String eyeLightType = CameraSettings.getEyeLightType();
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (!(topAlert == null || bottomPopupTips == null || "-1".equals(eyeLightType))) {
            topAlert.alertTopHint(0, (int) R.string.eye_light);
        }
    }

    public void reCheckFastMotion(boolean z) {
        String str;
        String str2;
        String str3;
        int i;
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (!(topAlert == null || this.mActivity == null)) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && ((BaseModule) baseModule.get()).getModuleIndex() == 169 && ((C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo()) && !topAlert.isExtraMenuShowing())) {
                ComponentRunningFastMotionSpeed componentRunningFastMotionSpeed = DataRepository.dataItemRunning().getComponentRunningFastMotionSpeed();
                ComponentRunningFastMotionDuration componentRunningFastMotionDuration = DataRepository.dataItemRunning().getComponentRunningFastMotionDuration();
                boolean z2 = componentRunningFastMotionSpeed.isModified() || componentRunningFastMotionDuration.isModified();
                FastMotionProtocol fastMotionProtocol = (FastMotionProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(674);
                CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                FastmotionProAdjust fastmotionProAdjust = (FastmotionProAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(932);
                if (!z2 || ((fastMotionProtocol != null && fastMotionProtocol.isShowing()) || ((fastmotionProAdjust != null && fastmotionProAdjust.isShowing()) || cameraAction == null || cameraAction.isRecording()))) {
                    i = 8;
                    str3 = "";
                    str2 = "";
                    str = "";
                } else {
                    String valueDisplayStringNotFromResource = componentRunningFastMotionSpeed.getValueDisplayStringNotFromResource(160);
                    String str4 = "";
                    String componentValue = componentRunningFastMotionDuration.isModified() ? componentRunningFastMotionDuration.getComponentValue(160) : str4;
                    boolean isModified = componentRunningFastMotionDuration.isModified();
                    ActivityBase activityBase = this.mActivity;
                    str = isModified ? activityBase.getResources().getQuantityString(R.plurals.pref_camera_fastmotion_duration_unit, 10, new Object[]{str4}) : activityBase.getString(R.string.pref_camera_fastmotion_duration_infinity);
                    i = 0;
                    str3 = valueDisplayStringNotFromResource;
                    str2 = componentValue;
                }
                topAlert.alertFastmotionIndicator(i, str3, str2, str, z);
            }
        }
    }

    public void reCheckFilm() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (!(topAlert == null || this.mActivity == null)) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                if (moduleIndex != 208 && moduleIndex != 212 && moduleIndex != 207) {
                    return;
                }
                if ((moduleIndex != 212 || ((FilmDreamProcessing) DataRepository.dataItemObservable().get(FilmDreamProcessing.class)).getCurrentState() == 0) && !topAlert.isExtraMenuShowing()) {
                    topAlert.setAlertAnim(false);
                    topAlert.alertAiDetectTipHint(0, moduleIndex == 207 ? R.string.film_video_b_tip : R.string.film_video_tip, -1);
                }
            }
        }
    }

    public void reCheckFocusPeakConfig() {
        if (isAlive()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && ((BaseModule) baseModule.get()).isCreated()) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                if ((moduleIndex == 180 || moduleIndex == 167) && DataRepository.dataItemRunning().isSwitchOn("pref_camera_peak_key")) {
                    Log.d(TAG, "reCheckFocusPeakConfig: configFocusPeakSwitch");
                    configFocusPeakSwitch(2);
                }
            }
        }
    }

    public void reCheckFrontBokenTip() {
        if (C0122O00000o.instance().OOO0O0o() && ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)) != null) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                if ("on".equals(DataRepository.dataItemConfig().getComponentBokeh().getComponentValue(((BaseModule) baseModule.get()).getModuleIndex()))) {
                    updateTipMessage(4, R.string.bokeh_use_hint, 2);
                }
            }
        }
    }

    public void reCheckGradienter() {
        if (CameraSettings.isGradienterOn()) {
            configGradienterSwitch(2);
        }
    }

    public void reCheckHanGestureDescTip() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            String str = FragmentTopAlert.TIP_HAND_GESTURE_DESC;
            if (topAlert.getTipsState(str)) {
                setTipsState(str, false);
                if (CameraSettings.isHandGestureOpen()) {
                    topAlert.alertRecommendDescTip(str, 0, R.string.hand_gesture_open_tip);
                }
            }
        }
    }

    public void reCheckHandGesture() {
        if (getBaseModule().isPresent() && CameraSettings.isHandGestureOpen()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.alertHandGestureHint(R.string.hand_gesture_tip);
            }
        }
    }

    public void reCheckLighting() {
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        if (actionProcessing != null) {
            if (DataRepository.dataItemRunning().getComponentRunningLighting().getComponentValue(171).equals("0")) {
                actionProcessing.setLightingViewStatus(false);
            } else if (!actionProcessing.isShowLightingView()) {
                actionProcessing.showOrHideLightingView();
            }
            if (actionProcessing.isShowLightingView()) {
                actionProcessing.showOrHideLightingView();
                showOrHideLighting(false);
            }
        }
    }

    public void reCheckLiveShot() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            BaseModule baseModule2 = (BaseModule) baseModule.get();
            if ((baseModule2.getModuleIndex() == 163 || baseModule2.getModuleIndex() == 165) && C0122O00000o.instance().OOO0o0o()) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null && CameraSettings.isLiveShotOn()) {
                    topAlert.alertLiveShotHint(0, R.string.camera_liveshot_on_tip);
                }
            }
        }
    }

    public void reCheckLiveVV() {
        if (C0122O00000o.instance().OOO0o()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && ((BaseModule) baseModule.get()).getModuleIndex() == 162) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (!(topAlert == null || this.mActivity == null)) {
                    BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                    if (baseDelegate != null && baseDelegate.getActiveFragment(R.id.bottom_action) == 65530) {
                        topAlert.alertTopHint(0, (int) R.string.vv_use_hint_text_title);
                    }
                }
            }
        }
    }

    public void reCheckMacroMode() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                if ((baseModule2.getModuleIndex() == 163 || baseModule2.getModuleIndex() == 162 || baseModule2.getModuleIndex() == 165 || baseModule2.getModuleIndex() == 172 || baseModule2.getModuleIndex() == 186 || baseModule2.getModuleIndex() == 205 || baseModule2.getModuleIndex() == 169) && !topAlert.isExtraMenuShowing() && CameraSettings.isMacroModeEnabled(baseModule2.getModuleIndex())) {
                    topAlert.alertMacroModeHint(0, DataRepository.dataItemRunning().getComponentRunningMacroMode().getResText());
                    CameraCapabilities cameraCapabilities = baseModule2.getCameraCapabilities();
                    if (cameraCapabilities != null && cameraCapabilities.isMacroHdrMutex()) {
                        updateComponentHdr(true);
                    }
                }
            }
        }
    }

    public void reCheckModuleUltraPixel() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && this.mActivity != null && getBaseModule().isPresent()) {
            String str = "pref_module_ultra_pixel_tip";
            if (!DataRepository.dataItemRunning().getBoolean(str, false) && CameraSettings.isUltraPixelOn()) {
                DataRepository.dataItemRunning().putBoolean(str, true);
                topAlert.alertSwitchTip(FragmentTopAlert.TIP_ULTRA_PIXEL, 0, DataRepository.dataItemRunning().getComponentUltraPixel().getUltraPixelOpenTip());
            }
        }
    }

    public void reCheckMutexConfigs(int i) {
        if (isAlive()) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && ((BaseModule) baseModule.get()).isCreated()) {
                DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
                int[] iArr = SupportedConfigFactory.MUTEX_MENU_CONFIGS;
                int length = iArr.length;
                for (int i2 = 0; i2 < length; i2++) {
                    int i3 = iArr[i2];
                    if (i3 == 203) {
                        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                        if (DataRepository.dataItemRunning().getComponentRunningLighting().getPortraitLightVersion() <= 1) {
                            reCheckLighting();
                        } else {
                            ActionProcessing actionProcessing2 = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                            if (actionProcessing2 != null) {
                                actionProcessing2.setLightingViewStatus(false);
                            }
                        }
                    } else if (i3 != 209 && dataItemRunning.isSwitchOn(SupportedConfigFactory.getConfigKey(i3))) {
                        applyConfig(i3, 2);
                    }
                }
            }
        }
    }

    public void reCheckMutexEarly(int i) {
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        boolean isUltraPixelPortraitFrontOn = CameraSettings.isUltraPixelPortraitFrontOn();
        boolean aiSceneOpen = CameraSettings.getAiSceneOpen(i);
        if (isUltraPixelPortraitFrontOn && aiSceneOpen) {
            this.mRecordingClosedElements = DataRepository.dataItemRunning().getRecordingClosedElements();
            if (this.mRecordingClosedElements != null) {
                restoreAllMutexElement(SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT);
            }
            dataItemRunning.switchOff("pref_camera_ultra_pixel_portrait_mode_key");
        }
    }

    public void reCheckParameterDescriptionTip() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            boolean isParameterDescriptionEnable = CameraSettings.isParameterDescriptionEnable();
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
            int i = 0;
            if (!(moduleIndex == 162 || moduleIndex == 167 || moduleIndex == 180 || moduleIndex == 169 || moduleIndex == 187)) {
                isParameterDescriptionEnable = false;
            }
            if (moduleIndex == 169 && !C0122O00000o.instance().OOO00O0() && !C0122O00000o.instance().OOO00Oo()) {
                isParameterDescriptionEnable = false;
            }
            if (moduleIndex == 162 && (masterFilterProtocol == null || !masterFilterProtocol.isShowing())) {
                isParameterDescriptionEnable = false;
            }
            int i2 = (moduleIndex != 162 || masterFilterProtocol == null || !masterFilterProtocol.isShowing()) ? 0 : 1;
            if (moduleIndex == 180 && masterFilterProtocol != null && masterFilterProtocol.isShowing()) {
                i2 = 1;
            }
            boolean z = (!isVideoRecoding((BaseModule) baseModule.get())) & isParameterDescriptionEnable;
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                if (!z || !(!topAlert.isExtraMenuShowing())) {
                    i = 8;
                }
                topAlert.alertParameterDescriptionTip(i, i2);
            }
        }
    }

    public void reCheckParameterResetTip(boolean z) {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex == 167 || moduleIndex == 180 || ModuleManager.isFastmotionModulePro()) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null && !topAlert.isExtraMenuShowing()) {
                    topAlert.alertParameterResetTip(z, !isChangeManuallyParameters() ? 8 : 0, R.string.reset_manually_parameter_hint);
                }
            }
        }
    }

    public void reCheckRaw() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (!(topAlert == null || this.mActivity == null)) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
                if (moduleIndex == 167 && !topAlert.isExtraMenuShowing()) {
                    topAlert.alertVideoUltraClear(DataRepository.dataItemConfig().getComponentConfigRaw().isSwitchOn(moduleIndex) ? 0 : 8, (int) R.string.manually_raw_hint);
                }
            }
        }
    }

    public void reCheckSpeechShutterDescTip() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            String str = FragmentTopAlert.TIP_SPEECH_SHUTTER_DESC;
            if (topAlert.getTipsState(str) && CameraSettings.isSpeechShutterOpen()) {
                topAlert.alertRecommendDescTip(str, 0, R.string.speech_shutter_open_tip);
            }
        }
    }

    public void reCheckSubtitleMode() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent()) {
                BaseModule baseModule2 = (BaseModule) baseModule.get();
                if (CameraSettings.isSubtitleEnabled(baseModule2.getModuleIndex()) && !isVideoRecoding(baseModule2)) {
                    topAlert.alertSubtitleHint(0, R.string.pref_video_subtitle);
                }
            }
        }
    }

    public void reCheckSuperEIS() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && this.mActivity != null && getBaseModule().isPresent() && CameraSettings.isSuperEISEnabled(((BaseModule) getBaseModule().get()).getModuleIndex())) {
            topAlert.alertSwitchTip(FragmentTopAlert.TIP_SUPER_EIS, 0, (int) R.string.super_eis);
        }
    }

    public void reCheckSuperEISPro() {
        int i;
        int i2;
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && this.mActivity != null && getBaseModule().isPresent()) {
            ComponentRunningEisPro componentRunningEisPro = DataRepository.dataItemRunning().getComponentRunningEisPro();
            String componentValue = componentRunningEisPro.getComponentValue(((BaseModule) getBaseModule().get()).getModuleIndex());
            String componentPreValue = componentRunningEisPro.getComponentPreValue();
            boolean equals = componentValue.equals("off");
            String str = ComponentRunningEisPro.EIS_VALUE_PRO;
            String str2 = "normal";
            String str3 = FragmentTopAlert.TIP_SUPER_EIS;
            if (equals) {
                i2 = 8;
                if (componentPreValue.equals(str2)) {
                    i = R.string.super_eis_disabled_hint;
                } else {
                    if (componentPreValue.equals(str)) {
                        i = R.string.super_eis_pro_disabled_hint;
                    }
                }
            } else {
                i2 = 0;
                if (componentValue.equals(str2)) {
                    i = R.string.super_eis;
                } else {
                    if (componentValue.equals(str)) {
                        i = R.string.super_eis_pro;
                    }
                }
            }
            topAlert.alertSwitchTip(str3, i2, i);
        }
    }

    public void reCheckTilt() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && CameraSettings.isTiltShiftOn()) {
            topAlert.alertSlideSwitchLayout(true, 228);
        }
    }

    public void reCheckTimerBurst() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && TimerBurstController.isSupportTimerBurst(((BaseModule) baseModule.get()).getModuleIndex()) && !topAlert.isExtraMenuShowing() && CameraSettings.isTimerBurstEnable()) {
                topAlert.alertTimerBurstHint(0, R.string.timer_burst);
            }
        }
    }

    public void reCheckUltraWideBokeh() {
        int i;
        int i2;
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && this.mActivity != null && getBaseModule().isPresent()) {
            boolean isSwitchOn = DataRepository.dataItemRunning().isSwitchOn("pref_ultra_wide_bokeh_enabled");
            String str = FragmentTopAlert.TIP_ULTRA_WIDE_BOKEH;
            if (isSwitchOn) {
                i = 0;
                i2 = R.string.ultra_wide_bokeh_open_tip;
            } else {
                i = 8;
                i2 = R.string.ultra_wide_bokeh_close_tip;
            }
            topAlert.alertSwitchTip(str, i, i2);
        }
    }

    public void reCheckVideoBeautify() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            Optional baseModule = getBaseModule();
            if (baseModule.isPresent() && ((BaseModule) baseModule.get()).getModuleIndex() == 162 && !topAlert.isExtraMenuShowing() && CameraSettings.isFaceBeautyOn(162, null)) {
                List items = DataRepository.dataItemRunning().getComponentRunningShine().getItems();
                String str = FragmentTopAlert.TIP_VIDEO_BEAUTIFY;
                int i = (items == null || items.size() <= 1) ? R.string.video_beauty_tip : R.string.video_beauty_tip_beautification;
                topAlert.alertSwitchTip(str, 0, i);
            }
        }
    }

    public void reCheckVideoBeautyPipeline() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex == 162 || moduleIndex == 180) {
                changeModeWithoutConfigureData(((BaseModule) baseModule.get()).getModuleIndex(), false);
            }
        }
    }

    public void reCheckVideoHDR10Tip() {
        int i;
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null && !topAlert.isExtraMenuShowing()) {
                if (CameraSettings.isHdr10Alive(moduleIndex)) {
                    i = R.string.video_hdr10_tip;
                } else if (CameraSettings.isHdr10PlusAlive(moduleIndex)) {
                    i = R.string.video_hdr10plus_tip;
                }
                topAlert.alertVideoUltraClear(0, i);
            }
        }
    }

    public void reCheckVideoHdr() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && this.mActivity != null && getBaseModule().isPresent()) {
            if (!"off".equals(DataRepository.dataItemConfig().getComponentHdr().getComponentValue(((BaseModule) getBaseModule().get()).getModuleIndex()))) {
                topAlert.alertHDR(0, false, false);
            }
        }
    }

    public void reCheckVideoLog() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex == 180 && CameraSettings.isProVideoLogOpen(moduleIndex) && !topAlert.isExtraMenuShowing()) {
                topAlert.alertVideoUltraClear(0, (int) R.string.pref_camera_video_log_tips);
            }
        }
    }

    public void reCheckVideoUltraClearTip() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex == 162 || moduleIndex == 169 || moduleIndex == 180) {
                CameraSize videoSize = ((VideoModule) baseModule.get()).getVideoSize();
                int i = videoSize.width;
                int i2 = videoSize.height;
                if (is8KQuality(i, i2)) {
                    BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                    if (baseDelegate == null || baseDelegate.getActiveFragment(R.id.bottom_action) != 65530) {
                        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                        if (!topAlert.isExtraMenuShowing()) {
                            boolean isProVideoLogOpen = CameraSettings.isProVideoLogOpen(moduleIndex);
                            if (topAlert != null && !isProVideoLogOpen && !CameraSettings.isReal8K()) {
                                topAlert.alertVideoUltraClear(0, (int) R.string.video_ultra_clear_tip_8k);
                            }
                            DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
                            String str = DataItemGlobal.DATA_COMMON_CAMCORDER_TIP_8K_MAX_VIDEO_DURATION_SHOWN;
                            if (dataItemGlobal.getBoolean(str, true)) {
                                DataRepository.dataItemGlobal().editor().putBoolean(str, false).apply();
                                if (topAlert != null && is8KQuality(i, i2)) {
                                    topAlert.alertRecommendDescTip(FragmentTopAlert.TIP_8K_DESC, 0, R.string.camcorder_tip_8k_max_video_duration);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void reConfigAiAudio(Context context, int i, boolean z) {
        int currentStringRes = DataRepository.dataItemRunning().getComponentRunningAiAudio().getCurrentStringRes(i);
        boolean isWiredHeadsetOn = Util.isWiredHeadsetOn();
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (currentStringRes != -1 && topAlert != null) {
            if (isWiredHeadsetOn) {
                topAlert.alertAiAudioBGHint(8, currentStringRes);
                topAlert.alertAiAudio(8, currentStringRes);
                topAlert.alertAiAudioMutexToastIfNeed(context, currentStringRes);
            } else if (z) {
                topAlert.alertAiAudio(0, currentStringRes);
            }
        }
    }

    public void reConfigSpeechShutter() {
        SpeechShutterDetect speechShutterDetect = (SpeechShutterDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(255);
        if (!DataRepository.dataItemRunning().supportSpeechShutter()) {
            if (speechShutterDetect != null) {
                speechShutterDetect.processingSpeechShutter(false);
            }
            return;
        }
        boolean isSpeechShutterOpen = CameraSettings.isSpeechShutterOpen();
        CameraSettings.setSpeechShutterStatus(isSpeechShutterOpen);
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (isSpeechShutterOpen && (moduleIndex == 185 || moduleIndex == 213)) {
                CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
                if (cloneProcess != null && (cloneProcess.getMode() == Mode.VIDEO || cloneProcess.getMode() == Mode.TIMEFREEZE)) {
                    isSpeechShutterOpen = false;
                }
            }
            if (moduleIndex == 254 || moduleIndex == 209 || moduleIndex == 210) {
                isSpeechShutterOpen = false;
            }
            ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
            if (actionProcessing != null) {
                actionProcessing.processingSpeechShutter(isSpeechShutterOpen);
            }
            if (speechShutterDetect != null) {
                speechShutterDetect.processingSpeechShutter(isSpeechShutterOpen);
            }
        }
    }

    public void recheckFunAR() {
        getBaseModule().ifPresent(C0408O00oOooO.INSTANCE);
    }

    public void recheckVideoFps(boolean z) {
        getBaseModule().ifPresent(new C0395O0000o0O(this));
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(164, this);
    }

    public void resetMeter(int i) {
        if (i == 167 || i == 180) {
            ComponentConfigMeter componentConfigMeter = DataRepository.dataItemConfig().getComponentConfigMeter();
            if (componentConfigMeter.isModified(i)) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                topAlert.reverseExpandTopBar(true);
                componentConfigMeter.reset(i);
                topAlert.updateConfigItem(214);
                getBaseModule().ifPresent(C0397O0000oO.INSTANCE);
            }
        }
    }

    public void restoreAllMutexElement(String str) {
        int[] iArr = this.mRecordingClosedElements;
        if (iArr != null) {
            int[] iArr2 = new int[iArr.length];
            int i = 0;
            while (true) {
                int[] iArr3 = this.mRecordingClosedElements;
                if (i < iArr3.length) {
                    int i2 = iArr3[i];
                    if (i2 == 193) {
                        updateComponentFlash(null, false);
                        iArr2[i] = 10;
                    } else if (i2 == 194) {
                        updateComponentHdr(false);
                        iArr2[i] = 11;
                    } else if (i2 == 196) {
                        updateComponentFilter(false);
                        iArr2[i] = 2;
                    } else if (i2 == 201) {
                        updateAiScene(false);
                        iArr2[i] = 36;
                    } else if (i2 == 206) {
                        updateLiveShot(false);
                        if (str != SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL) {
                            iArr2[i] = 49;
                        } else {
                            iArr2[i] = 50;
                        }
                    } else if (i2 == 209) {
                        updateUltraPixel(false);
                        iArr2[i] = 50;
                    } else if (i2 == 212) {
                        updateComponentShine(false);
                        iArr2[i] = 2;
                    } else if (i2 == 237) {
                        updateRaw(false);
                        iArr2[i] = 44;
                    } else if (i2 == 239) {
                        updateComponentBeauty(false);
                        iArr2[i] = 13;
                    } else if (i2 == 253) {
                        updateAutoZoom(false);
                        iArr2[i] = 51;
                    } else if (i2 == 254) {
                        updateEyeLight(false);
                        iArr2[i] = 45;
                    } else {
                        throw new RuntimeException("unknown mutex element");
                    }
                    i++;
                } else {
                    this.mRecordingClosedElements = null;
                    getBaseModule().ifPresent(new C0398O0000oO0(iArr2));
                    return;
                }
            }
        }
    }

    public void restoreMutexFlash(String str) {
        if (DataRepository.dataItemConfig().getComponentFlash().isClosed()) {
            updateComponentFlash(str, false);
            getBaseModule().ifPresent(O00000o.INSTANCE);
        }
    }

    public void setEyeLight(String str) {
        CameraSettings.setEyeLight(str);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.showTips(10, EyeLightConstant.getString(str), 4);
        }
        getBaseModule().ifPresent(C0393O0000OoO.INSTANCE);
    }

    public void setFilter(int i) {
        int shaderEffect = CameraSettings.getShaderEffect();
        persistFilter(i);
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        if ((i == 0 || shaderEffect == 0) && shaderEffect != i && currentMode == 180) {
            CameraSettings.setVideoQuality8KOff(currentMode);
            CameraSettings.setProVideoLog(false);
            changeModeWithoutConfigureData(currentMode, false);
        }
        if ((i == 0 || shaderEffect == 0) && shaderEffect != i && currentMode == 169 && (C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo())) {
            ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
            if (componentRunningMacroMode.isSwitchOn(169)) {
                CameraSettings.resetRetainZoom();
                componentRunningMacroMode.setSwitchOff(169);
            }
            changeModeWithoutConfigureData(currentMode, false);
            DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
            if (dualController != null) {
                dualController.hideZoomButton();
            }
        }
        EffectController.getInstance().setInvertFlag(0);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (CameraSettings.isGroupShotOn()) {
            ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).configGroupSwitch(4);
            topAlert.refreshExtraMenu();
        }
        OnShineChangedProtocol onShineChangedProtocol = (OnShineChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(234);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFilter: filterId = ");
        sb.append(i);
        sb.append(", FilterProtocol = ");
        sb.append(onShineChangedProtocol);
        Log.u(str, sb.toString());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onFilterChanged: category = ");
        sb2.append(FilterInfo.getCategory(i));
        sb2.append(", newIndex = ");
        sb2.append(FilterInfo.getIndex(i));
        Log.u(str2, sb2.toString());
        if (onShineChangedProtocol != null) {
            onShineChangedProtocol.onShineChanged(false, 196);
        }
        if (CameraSettings.isUltraPixelFront32MPOn()) {
            configSwitchUltraPixel(3);
        }
        if (i != FilterInfo.FILTER_ID_NONE) {
            configDocumentMode(3);
        }
    }

    public void setKaleidoscope(String str) {
        KaleidoscopeProtocol kaleidoscopeProtocol = (KaleidoscopeProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(236);
        if (kaleidoscopeProtocol != null) {
            kaleidoscopeProtocol.onKaleidoscopeChanged(str);
        }
        OnShineChangedProtocol onShineChangedProtocol = (OnShineChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(234);
        if (onShineChangedProtocol != null) {
            onShineChangedProtocol.onShineChanged(false, 196);
        }
    }

    public void setLighting(boolean z, String str, String str2, boolean z2) {
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setLighting: newValue = ");
        sb.append(str2);
        Log.u(str3, sb.toString());
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        int portraitLightVersion = DataRepository.dataItemRunning().getComponentRunningLighting().getPortraitLightVersion();
        String str4 = "0";
        if (portraitLightVersion > 1 || str.equals(str4) || str2.equals(str4)) {
            boolean z3 = false;
            topAlert.updateConfigItem(203);
            if (str2.equals(str4) || portraitLightVersion > 1) {
                z3 = true;
            }
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
            if (z3) {
                mainContentProtocol.lightingCancel();
                if (z) {
                    ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).onRestoreCameraActionMenu(6);
                    BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                    if (baseDelegate != null) {
                        baseDelegate.delegateEvent(33);
                    }
                }
            } else {
                mainContentProtocol.lightingStart();
                actionProcessing.setLightingViewStatus(true);
            }
        }
        if (str2 == str4) {
            topAlert.alertLightingTip(-1);
        }
        getBaseModule().ifPresent(C0391O00000oO.INSTANCE);
        if (z2) {
            CameraStatUtils.trackLightingChanged(171, str2);
        }
    }

    public void setMasterFilter(int i) {
        int videoMasterFilter = CameraSettings.getVideoMasterFilter();
        CameraSettings.setVideoMasterFilter(i);
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            if (i != 0) {
                mutexBeautyBusiness(currentMode);
            }
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.updateConfigItem(263);
            }
            if ((!CameraSettings.isFaceBeautyOn(currentMode, null) && ((i == 0 || videoMasterFilter == 0) && videoMasterFilter != i)) || i == 200 || !(i == 200 || i == 0 || (videoMasterFilter != 200 && videoMasterFilter != 0))) {
                changeModeWithoutConfigureData(currentMode, false);
                DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                if (dualController != null) {
                    dualController.hideZoomButton();
                }
            }
            EffectController.getInstance().setInvertFlag(0);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setMasterFilter: filterId = ");
            sb.append(i);
            Log.u(str, sb.toString());
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onFilterChanged: category = ");
            sb2.append(FilterInfo.getCategory(i));
            sb2.append(", newIndex = ");
            sb2.append(FilterInfo.getIndex(i));
            Log.u(str2, sb2.toString());
            ((BaseModule) baseModule.get()).onShineChanged(196);
        }
    }

    public void setWatermarkEnable(boolean z) {
        clearToast();
        showDualController(z);
        showWatermarkSample(z);
        showOrHideTipImage(z);
        WatermarkProtocol watermarkProtocol = (WatermarkProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(253);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            if (watermarkProtocol == null || !watermarkProtocol.isWatermarkPanelShow()) {
                bottomPopupTips.directShowOrHideLeftTipImage(z);
            } else {
                bottomPopupTips.directShowOrHideLeftTipImage(false);
            }
        }
        if (watermarkProtocol != null && !z) {
            watermarkProtocol.dismiss(2, 6);
        }
    }

    public void showOrHideAIWatermark() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            clearToast();
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            if (moduleIndex == 163) {
                ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
                if (manuallyAdjust != null) {
                    manuallyAdjust.setManuallyVisible(0, 4, null);
                }
            }
            ComponentRunningAIWatermark componentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
            WatermarkProtocol watermarkProtocol = (WatermarkProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(253);
            BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
            if (watermarkProtocol == null || !watermarkProtocol.isWatermarkPanelShow()) {
                bottomMenuProtocol.expandAIWatermarkBottomMenu(componentRunningAIWatermark);
                if (watermarkProtocol != null) {
                    watermarkProtocol.show();
                } else {
                    BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                    if (baseDelegate != null) {
                        baseDelegate.delegateEvent(21);
                    }
                }
                if (moduleIndex != 188) {
                    CameraStatUtils.trackAIWatermarkClick(AIWatermark.AI_WATERMARK_LIST_SHOW);
                } else {
                    CameraStatUtils.trackSuperMoonClick(SuperMoon.PARAM_SUPER_MOON_EFFECT_CLICK);
                }
            } else {
                watermarkProtocol.dismiss(2, 6);
            }
        }
    }

    public void showOrHideFilter() {
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        if (((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)) != null) {
            ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
            boolean isShowLightingView = actionProcessing.isShowLightingView();
            boolean showOrHideFilterView = actionProcessing.showOrHideFilterView();
            DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
            if (showOrHideFilterView && isShowLightingView) {
                actionProcessing.showOrHideLightingView();
                String componentValue = DataRepository.dataItemRunning().getComponentRunningLighting().getComponentValue(171);
                String str = "0";
                DataRepository.dataItemRunning().getComponentRunningLighting().setComponentValue(171, str);
                setLighting(true, componentValue, str, false);
            }
            if (showOrHideFilterView && dualController != null && currentMode == 171) {
                dualController.showBokehButton();
            }
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            if (bottomPopupTips != null) {
                if (showOrHideFilterView || miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow()) {
                    bottomPopupTips.updateLeftTipImage();
                }
                bottomPopupTips.reConfigQrCodeTip();
            }
        }
    }

    public void showOrHideLighting(boolean z) {
        beautyMutexHandle();
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            boolean showOrHideLightingView = ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).showOrHideLightingView();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("showOrHideLighting: ");
            sb.append(showOrHideLightingView);
            Log.u(str, sb.toString());
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
            BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
            String str2 = "0";
            if (showOrHideLightingView) {
                Optional baseModule = getBaseModule();
                if (baseModule.isPresent()) {
                    if (DataRepository.dataItemRunning().getComponentRunningLighting().getPortraitLightVersion() <= 1) {
                        DataRepository.dataItemRunning().getComponentConfigFilter().reset(((BaseModule) baseModule.get()).getModuleIndex());
                        setFilter(FilterInfo.FILTER_ID_NONE);
                    }
                    if (dualController != null) {
                        dualController.hideBokehButton(false);
                    }
                    bottomPopupTips.directHideTipImage();
                    topAlert.refreshExtraMenu();
                    bottomMenuProtocol.onSwitchLiveActionMenu(167);
                    if (baseDelegate.getActiveFragment(R.id.bottom_beauty) != 4087) {
                        baseDelegate.delegateEvent(33);
                    }
                    setLighting(false, str2, DataRepository.dataItemRunning().getComponentRunningLighting().getComponentValue(171), false);
                } else {
                    return;
                }
            } else {
                String componentValue = DataRepository.dataItemRunning().getComponentRunningLighting().getComponentValue(171);
                String componentValue2 = DataRepository.dataItemRunning().getComponentRunningLighting().getComponentValue(171);
                if (DataRepository.dataItemRunning().getComponentRunningLighting().getPortraitLightVersion() <= 1) {
                    DataRepository.dataItemRunning().getComponentRunningLighting().setComponentValue(171, str2);
                    componentValue2 = str2;
                }
                setLighting(true, componentValue, componentValue2, false);
            }
            if (z) {
                MistatsWrapper.commonKeyTriggerEvent(PortraitAttr.VALUE_LIGHTING_OUT_BUTTON, null, null);
            }
            if (bottomPopupTips != null && showOrHideLightingView) {
                bottomPopupTips.hideLeftTipImage();
            }
        }
    }

    public void showOrHideMasterFilter() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            boolean z = true;
            MasterFilterProtocol masterFilterProtocol = (MasterFilterProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(934);
            if (masterFilterProtocol != null && masterFilterProtocol.isShowing()) {
                z = false;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("showOrHideMasterFilter: ");
            sb.append(z);
            Log.u(str, sb.toString());
            if (z) {
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (bottomPopupTips != null) {
                    bottomPopupTips.hideQrCodeTip();
                    bottomPopupTips.directlyHideTips();
                    bottomPopupTips.setPortraitHintVisible(8);
                    bottomPopupTips.hideTipImage();
                    bottomPopupTips.hideLeftTipImage();
                    bottomPopupTips.hideRightTipImage();
                    bottomPopupTips.hideCenterTipImage();
                    bottomPopupTips.directHideLeftImageIntro();
                    bottomPopupTips.directHideLyingDirectHint();
                }
                DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                if (dualController != null) {
                    dualController.hideZoomButton();
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null) {
                        topAlert.alertUpdateValue(0, 0, null);
                    }
                }
                ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
                if ((moduleIndex == 167 || moduleIndex == 180) && manuallyAdjust != null) {
                    manuallyAdjust.setManuallyLayoutVisible(false);
                }
                ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).expandMasterFilterBottomMenu(DataRepository.dataItemRunning().getComponentRunningMasterFilter());
                if (masterFilterProtocol != null) {
                    masterFilterProtocol.show();
                } else {
                    BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                    if (baseDelegate != null) {
                        baseDelegate.delegateEvent(37);
                    }
                }
            } else if (masterFilterProtocol != null) {
                masterFilterProtocol.dismiss(2, 6);
            }
        }
    }

    public void showOrHideMimoji() {
        beautyMutexHandle();
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        BottomMenuProtocol bottomMenuProtocol = (BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197);
        if (baseDelegate != null && actionProcessing != null && bottomPopupTips != null && bottomMenuProtocol != null) {
            boolean showOrHideMiMojiView = actionProcessing.showOrHideMiMojiView();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("showOrHideMimoji: ");
            sb.append(showOrHideMiMojiView);
            Log.u(str, sb.toString());
            if (showOrHideMiMojiView) {
                if (topAlert != null) {
                    topAlert.updateConfigItem(193);
                }
                if (bottomPopupTips != null) {
                    bottomPopupTips.directlyHideTips();
                }
                bottomMenuProtocol.onSwitchLiveActionMenu(168);
            } else {
                bottomMenuProtocol.onRestoreCameraActionMenu(6);
            }
            bottomPopupTips.updateMimojiBottomTipImage();
            baseDelegate.delegateEvent(14);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x017e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void showOrHideShine() {
        boolean z;
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            int moduleIndex = ((BaseModule) baseModule.get()).getModuleIndex();
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            boolean z2 = true;
            boolean z3 = miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("showOrHideShine: ");
            sb.append(z3);
            Log.u(str, sb.toString());
            boolean isFaceBeautyOn = CameraSettings.isFaceBeautyOn(moduleIndex, null);
            ComponentRunningShine componentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
            if (moduleIndex == 162) {
                z = false;
            } else if (moduleIndex != 204) {
                z2 = false;
                if (!z3) {
                    BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                    if (bottomPopupTips != null) {
                        bottomPopupTips.hideQrCodeTip();
                        bottomPopupTips.directlyHideTips();
                        bottomPopupTips.setPortraitHintVisible(8);
                        bottomPopupTips.hideTipImage();
                        bottomPopupTips.hideLeftTipImage();
                        bottomPopupTips.hideRightTipImage();
                        bottomPopupTips.hideCenterTipImage();
                        bottomPopupTips.directHideLeftImageIntro();
                        bottomPopupTips.directHideLyingDirectHint();
                    }
                    DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                    if (dualController != null) {
                        dualController.hideZoomButton();
                        if (!(moduleIndex == 171 || z2 || topAlert == null)) {
                            topAlert.alertUpdateValue(0, 0, null);
                        }
                    }
                    ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
                    if (moduleIndex != 163) {
                        if (moduleIndex != 167) {
                            if (moduleIndex != 171) {
                                if (moduleIndex != 180) {
                                    if (moduleIndex == 187) {
                                        AmbilightSelector ambilightSelector = (AmbilightSelector) ModeCoordinatorImpl.getInstance().getAttachProtocol(421);
                                        if (ambilightSelector != null) {
                                            ambilightSelector.setSelectorLayoutVisible(false);
                                        }
                                    }
                                }
                            } else if (dualController != null && dualController.isButtonVisible()) {
                                dualController.hideBokehButton(false);
                            }
                        }
                        if (manuallyAdjust != null) {
                            manuallyAdjust.setManuallyLayoutVisible(false);
                        }
                    } else if (manuallyAdjust != null) {
                        manuallyAdjust.setManuallyVisible(0, 4, null);
                    }
                    ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).expandShineBottomMenu(componentRunningShine);
                    if (miBeautyProtocol != null) {
                        miBeautyProtocol.show();
                    } else {
                        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                        if (baseDelegate != null) {
                            baseDelegate.delegateEvent(2);
                        }
                    }
                } else if (miBeautyProtocol != null) {
                    miBeautyProtocol.dismiss(2);
                }
            } else {
                closeVideoFast();
                z = true;
            }
            setTipsState(FragmentTopAlert.TIP_VIDEO_BEAUTIFY, true);
            if (!isFaceBeautyOn) {
                mutexBeautyBusiness(moduleIndex);
            } else {
                z = true;
                boolean z4 = false;
            }
            if (z3) {
                componentRunningShine.setTargetShow(true);
            }
            boolean z5 = !isFaceBeautyOn;
            if (componentRunningShine.isTopBeautyEntry()) {
                singeSwitchVideoBeauty(z5);
            } else {
                componentRunningShine.setVideoShineForceOn(162, z5);
                if (topAlert != null) {
                    topAlert.updateConfigItem(212);
                }
            }
            if (z) {
                CameraSettings.setFaceBeautySmoothLevel(0);
                resetVideoFilter();
                CameraSettings.setVideoBokehRatio(0.0f);
                ShineHelper.onShineStateChanged();
                ShineHelper.onBeautyChanged();
                ShineHelper.onVideoBokehRatioChanged();
                ShineHelper.onVideoFilterChanged();
            }
            if (!z || moduleIndex == 162) {
                reCheckVideoBeautyPipeline();
            } else {
                changeMode(162);
            }
            if (componentRunningShine.isTopBeautyEntry()) {
                return;
            }
            if (!z3) {
            }
        }
    }

    public void showSetting() {
        ActivityBase activityBase = this.mActivity;
        if (activityBase != null) {
            Log.u(TAG, "config showSetting");
            switchOffElementsSilent(216);
            Intent intent = new Intent();
            intent.setClass(activityBase, CameraPreferenceActivity.class);
            intent.putExtra("from_where", DataRepository.dataItemGlobal().getCurrentMode());
            int intentType = DataRepository.dataItemGlobal().getIntentType();
            intent.putExtra(CameraPreferenceFragment.INTENT_TYPE, intentType);
            if (intentType == 2) {
                intent.putExtra(CameraPreferenceFragment.INTENT_VIDEO_QUALITY, DataRepository.dataItemGlobal().getIntentVideoQuality());
            }
            intent.putExtra(":miui:starting_window_label", activityBase.getResources().getString(R.string.pref_camera_settings_category));
            if (activityBase.startFromKeyguard()) {
                intent.putExtra("StartActivityWhenLocked", true);
            }
            activityBase.getIntent().removeExtra(CameraIntentManager.EXTRAS_CAMERA_FACING);
            activityBase.startActivity(intent);
            activityBase.setJumpFlag(2);
            trackGotoSettings();
        }
    }

    public void showWatermarkSample(boolean z) {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol == null) {
            return;
        }
        if (z) {
            findBestWatermarkItem(88);
        } else {
            mainContentProtocol.setWatermarkVisible(4);
        }
    }

    public void switchOffElementsSilent(int... iArr) {
        for (int i : iArr) {
            if (i == 203) {
                ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                if (actionProcessing != null && actionProcessing.isShowLightingView()) {
                    actionProcessing.showOrHideLightingView();
                }
            } else if (i == 209) {
                this.mRecordingClosedElements = DataRepository.dataItemRunning().getRecordingClosedElements();
                if (this.mRecordingClosedElements != null) {
                    restoreAllMutexElement(SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL);
                }
                CameraSettings.switchOffUltraPixel();
            }
        }
    }

    public void unRegisterProtocol() {
        this.mActivity = null;
        ModeCoordinatorImpl.getInstance().detachProtocol(164, this);
    }

    public void updateASDForWatermark() {
        Optional baseModule = getBaseModule();
        if (baseModule.isPresent()) {
            ((BaseModule) baseModule.get()).updatePreferenceInWorkThread(73);
        }
    }
}
