package com.android.camera.module.loader;

import com.android.camera.Camera;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.effect.EffectController;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.xiaomi.camera.device.exception.CameraNotOpenException;
import io.reactivex.annotations.NonNull;

public class FunctionModuleSetup extends Func1Base {
    private static final String TAG = "FunctionModuleSetup";

    public FunctionModuleSetup(int i) {
        super(i);
    }

    public NullHolder apply(@NonNull NullHolder nullHolder) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("apply: module isPresent = ");
        sb.append(nullHolder.isPresent());
        Log.d(str, sb.toString());
        if (!nullHolder.isPresent()) {
            return nullHolder;
        }
        BaseModule baseModule = (BaseModule) nullHolder.get();
        if (baseModule.isDeparted()) {
            return NullHolder.ofNullable(baseModule, 225);
        }
        EffectController.getInstance().reset();
        Camera activity = baseModule.getActivity();
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        int i = this.mTargetMode;
        String str2 = "pref_video_speed_fast_key";
        if (i != 162) {
            String str3 = "pref_camera_square_mode_key";
            if (i == 163 || i == 165) {
                dataItemRunning.switchOn(str3);
            } else if (i == 167) {
                dataItemRunning.switchOn("pref_camera_manual_mode_key");
            } else if (i == 169) {
                dataItemRunning.switchOn(str2);
            } else if (i == 171) {
                String str4 = "pref_camera_portrait_mode_key";
                if (dataItemGlobal.getCurrentCameraId() == 0) {
                    dataItemRunning.switchOn(str4);
                } else {
                    dataItemRunning.switchOff(str4);
                }
            } else if (i == 186) {
                dataItemRunning.getComponentRunningDocument().setEnabled(true);
            } else if (i == 205) {
                dataItemRunning.getComponentRunningAIWatermark().setAIWatermarkEnable(true);
            } else if (i != 174) {
                if (i == 175) {
                    int currentCameraId = dataItemGlobal.getCurrentCameraId();
                    dataItemRunning.getComponentUltraPixel().switchOnCurrentSupported(175, currentCameraId, Camera2DataContainer.getInstance().getCapabilitiesByBogusCameraId(currentCameraId, 175));
                }
            } else if (activity.startFromKeyguard()) {
                DataRepository.dataItemLive().setRecordSegmentTimeInfo(null);
            }
        } else {
            dataItemRunning.switchOff(str2);
        }
        if (baseModule.isDeparted()) {
            return NullHolder.ofNullable(baseModule, 225);
        }
        try {
            baseModule.onCreate(this.mTargetMode, dataItemGlobal.getCurrentCameraId());
            if (baseModule.isCreated()) {
                baseModule.registerProtocol();
                baseModule.onResume();
            }
            return nullHolder;
        } catch (CameraNotOpenException | IllegalArgumentException e) {
            Log.e(TAG, "Module init error: ", e);
            baseModule.setDeparted();
            return NullHolder.ofNullable(null, 237);
        }
    }
}
