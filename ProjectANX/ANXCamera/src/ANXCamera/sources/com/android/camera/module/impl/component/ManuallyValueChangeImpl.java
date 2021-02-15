package com.android.camera.module.impl.component;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import com.android.camera.ActivityBase;
import com.android.camera.CameraApplicationDelegate;
import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.config.ComponentConfigVideoQuality;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.config.ComponentManuallyET;
import com.android.camera.data.data.config.ComponentManuallyEV;
import com.android.camera.data.data.config.ComponentManuallyFocus;
import com.android.camera.data.data.config.ComponentManuallyISO;
import com.android.camera.data.data.config.ComponentManuallyWB;
import com.android.camera.data.data.config.ComponentRunningMacroMode;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.EvChangedProtocol;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.ManuallyValueChanged;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import java.util.ArrayList;
import java.util.List;

public class ManuallyValueChangeImpl implements ManuallyValueChanged {
    private static final String TAG = "ManuallyValueChangeImpl";
    private ActivityBase mActivity;
    private BaseModule mCurrentModule = ((BaseModule) this.mActivity.getCurrentModule());

    private ManuallyValueChangeImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
    }

    public static ManuallyValueChangeImpl create(ActivityBase activityBase) {
        return new ManuallyValueChangeImpl(activityBase);
    }

    public void onBokehFNumberValueChanged(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onBokehFNumberValueChanged: newFNumber=");
        sb.append(str);
        Log.u(str2, sb.toString());
        CameraSettings.writeFNumber(str);
        this.mCurrentModule.updatePreferenceInWorkThread(48);
    }

    public void onDualLensSwitch(ComponentManuallyDualLens componentManuallyDualLens, int i) {
        CameraSettings.resetRetainZoom();
        String componentValue = componentManuallyDualLens.getComponentValue(i);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onDualLensSwitch: currValue=");
        sb.append(componentValue);
        Log.u(str, sb.toString());
        String next = componentManuallyDualLens.next(componentValue, i);
        if (!(i == 167 || i == 180)) {
            componentValue = next;
        }
        String str2 = "macro";
        if (i == 180 && str2.equalsIgnoreCase(componentValue)) {
            DataRepository.dataItemRunning().getComponentRunningAiAudio().setComponentValue(i, "normal");
        }
        String str3 = ComponentManuallyDualLens.LENS_WIDE;
        if (!str3.equalsIgnoreCase(componentValue)) {
            CameraSettings.setProVideoLog(false);
        }
        componentManuallyDualLens.setComponentValue(i, componentValue);
        String str4 = ComponentManuallyDualLens.LENS_ULTRA;
        CameraSettings.setUltraWideConfig(i, str4.equalsIgnoreCase(componentValue));
        boolean OOOO0oo = C0122O00000o.instance().OOOO0oo();
        String str5 = ComponentManuallyDualLens.LENS_ULTRA_TELE;
        if ((!OOOO0oo || !str5.equalsIgnoreCase(componentValue)) && !str3.equalsIgnoreCase(componentValue)) {
            CameraSettings.switchOffUltraPixel();
        }
        ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
        if (str2.equalsIgnoreCase(componentValue)) {
            componentRunningMacroMode.setSwitchOn(i);
        } else {
            componentRunningMacroMode.setSwitchOff(i);
        }
        CameraStatUtils.trackLensChanged(componentValue, this.mCurrentModule.getModuleIndex());
        if (CameraSettings.isVideoQuality8KOpen(i)) {
            int i2 = ComponentManuallyDualLens.LENS_TELE.equals(componentValue) ? Camera2DataContainer.getInstance().getAuxCameraId() : str4.equals(componentValue) ? Camera2DataContainer.getInstance().getUltraWideCameraId() : str2.equals(componentValue) ? Camera2DataContainer.getInstance().getStandaloneMacroCameraId() : str5.equals(componentValue) ? Camera2DataContainer.getInstance().getUltraTeleCameraId() : Camera2DataContainer.getInstance().getMainBackCameraId();
            if (!CameraSettings.is8KCamcorderSupported(i2)) {
                CameraSettings.setVideoQuality8KOff(i);
            }
        }
        if (i != 180) {
            ComponentConfigVideoQuality componentConfigVideoQuality = DataRepository.dataItemConfig().getComponentConfigVideoQuality();
            componentConfigVideoQuality.setComponentValue(i, componentConfigVideoQuality.getDefaultValue(i));
        }
        this.mActivity.onModeSelected(StartControl.create(i).setResetType(5).setViewConfigType(2).setNeedBlurAnimation(true));
    }

    public void onDualLensZooming(boolean z) {
        BaseModule baseModule = this.mCurrentModule;
        if (baseModule.isAlive() && !CameraSettings.isZoomByCameraSwitchingSupported() && baseModule.getActualCameraId() == Camera2DataContainer.getInstance().getSATCameraId()) {
            baseModule.notifyZooming(z);
        }
    }

    public void onDualZoomHappened(boolean z) {
        BaseModule baseModule = this.mCurrentModule;
        if (baseModule.isAlive() && !CameraSettings.isZoomByCameraSwitchingSupported() && baseModule.getActualCameraId() == Camera2DataContainer.getInstance().getSATCameraId()) {
            baseModule.notifyDualZoom(z);
        }
    }

    public void onDualZoomValueChanged(float f, int i) {
        if (this.mCurrentModule.isAlive()) {
            this.mCurrentModule.onZoomRatioChanged(f, i);
        }
    }

    public void onETValueChanged(ComponentManuallyET componentManuallyET, String str, String str2) {
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onETValueChanged: oldValue=");
        sb.append(str);
        sb.append(", newValue=");
        sb.append(str2);
        sb.append(", name=");
        sb.append(CameraApplicationDelegate.getAndroidContext().getString(componentManuallyET.getValueDisplayString(this.mCurrentModule.getModuleIndex())));
        Log.u(str3, sb.toString());
        CameraStatUtils.trackExposureTimeChanged(str2, this.mCurrentModule.getModuleIndex());
        if (this.mCurrentModule.getModuleIndex() == 167 && C0122O00000o.instance().OO0O0o()) {
            String str4 = "0";
            if (str4.equals(str) || str4.equals(str2)) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.updateConfigItem(193);
                }
            }
        }
        this.mCurrentModule.updatePreferenceInWorkThread(16, 20, 30, 34, 10);
    }

    public void onEVValueChanged(ComponentManuallyEV componentManuallyEV, String str) {
        CameraStatUtils.trackEVChanged(str, this.mCurrentModule.getModuleIndex());
        EvChangedProtocol evChangedProtocol = (EvChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(169);
        int parseFloat = (int) (Float.parseFloat(str) / Camera2DataContainer.getInstance().getCapabilitiesByBogusCameraId(DataRepository.dataItemGlobal().getCurrentCameraId(), DataRepository.dataItemGlobal().getCurrentMode()).getExposureCompensationStep());
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onEVValueChanged: newValue=");
        sb.append(str);
        sb.append(", evValue=");
        sb.append(parseFloat);
        Log.u(str2, sb.toString());
        if (evChangedProtocol != null) {
            evChangedProtocol.onEvChanged(parseFloat, 3);
        }
    }

    public void onFocusValueChanged(ComponentManuallyFocus componentManuallyFocus, String str, String str2) {
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onFocusValueChanged: oldValue=");
        sb.append(str);
        sb.append(", newValue=");
        sb.append(str2);
        sb.append(", getManualFocusName=");
        sb.append(CameraSettings.getManualFocusName(CameraApplicationDelegate.getAndroidContext(), Integer.parseInt(str2)));
        Log.u(str3, sb.toString());
        if (!CameraSettings.getMappingFocusMode(Integer.valueOf(str).intValue()).equals(CameraSettings.getMappingFocusMode(Integer.valueOf(str2).intValue()))) {
            CameraSettings.setFocusModeSwitching(true);
            boolean equals = str2.equals(componentManuallyFocus.getDefaultValue(167));
            if (C0124O00000oO.Oo00oO0()) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).updateFocusMode(equals ? "auto" : "manual");
            }
        }
        if (this.mCurrentModule.isIgnoreTouchEvent()) {
            this.mCurrentModule.enableCameraControls(true);
        }
        this.mCurrentModule.updatePreferenceInWorkThread(14);
    }

    public void onISOValueChanged(ComponentManuallyISO componentManuallyISO, String str, String str2) {
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onISOValueChanged: oldValue=");
        sb.append(str);
        sb.append(", newValue=");
        sb.append(str2);
        Log.u(str3, sb.toString());
        CameraStatUtils.trackIsoChanged(str2, this.mCurrentModule.getModuleIndex());
        if (this.mCurrentModule.getModuleIndex() == 167 && C0122O00000o.instance().OO0O0o()) {
            String str4 = "0";
            if (str4.equals(str) || str4.equals(str2)) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.updateConfigItem(193);
                }
            }
        }
        this.mCurrentModule.updatePreferenceInWorkThread(15, 10);
    }

    public void onWBValueChanged(ComponentManuallyWB componentManuallyWB, String str, boolean z) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onWBValueChanged: newValue=");
        sb.append(str);
        sb.append(", isCustomValue=");
        sb.append(z);
        Log.u(str2, sb.toString());
        if (!z) {
            componentManuallyWB.getKey(167);
        }
        if (z) {
            str = "manual";
        }
        CameraStatUtils.trackAwbChanged(str, this.mCurrentModule.getModuleIndex());
        this.mCurrentModule.updatePreferenceInWorkThread(6);
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(174, this);
    }

    public void resetManuallyParameters(List list) {
        int i;
        int i2;
        Integer valueOf;
        if (list != null && list.size() != 0) {
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            ArrayList arrayList = new ArrayList();
            for (int i3 = 0; i3 < list.size(); i3++) {
                ComponentData componentData = (ComponentData) list.get(i3);
                if (componentData instanceof ComponentManuallyWB) {
                    i = 6;
                } else {
                    boolean z = componentData instanceof ComponentManuallyISO;
                    String str = SupportedConfigFactory.CLOSE_BY_MANUAL_MODE;
                    if (z) {
                        configChanges.restoreMutexFlash(str);
                        i2 = 15;
                    } else if (componentData instanceof ComponentManuallyET) {
                        configChanges.restoreMutexFlash(str);
                        arrayList.add(Integer.valueOf(16));
                        arrayList.add(Integer.valueOf(30));
                        arrayList.add(Integer.valueOf(34));
                        i2 = 20;
                    } else if (componentData instanceof ComponentManuallyFocus) {
                        i = 14;
                    } else {
                        if (componentData instanceof ComponentManuallyEV) {
                            ((EvChangedProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(169)).resetEvValue();
                        }
                    }
                    arrayList.add(Integer.valueOf(i2));
                    valueOf = Integer.valueOf(10);
                    arrayList.add(valueOf);
                }
                valueOf = Integer.valueOf(i);
                arrayList.add(valueOf);
            }
            int[] iArr = new int[arrayList.size()];
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                iArr[i4] = ((Integer) arrayList.get(i4)).intValue();
            }
            this.mCurrentModule.updatePreferenceInWorkThread((int[]) iArr.clone());
        }
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(174, this);
    }

    public void updateSATIsZooming(boolean z) {
        this.mCurrentModule.updateSATZooming(z);
    }
}
