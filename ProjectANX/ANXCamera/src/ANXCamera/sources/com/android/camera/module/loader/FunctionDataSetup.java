package com.android.camera.module.loader;

import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.extra.DataItemLive;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.module.BaseModule;
import io.reactivex.annotations.NonNull;

public class FunctionDataSetup extends Func1Base {
    public FunctionDataSetup(int i) {
        super(i);
    }

    public NullHolder apply(@NonNull NullHolder nullHolder) {
        if (!nullHolder.isPresent()) {
            return nullHolder;
        }
        BaseModule baseModule = (BaseModule) nullHolder.get();
        if (baseModule.isDeparted()) {
            return NullHolder.ofNullable(baseModule, 225);
        }
        if (!baseModule.isCreated()) {
            return nullHolder;
        }
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        DataItemLive dataItemLive = DataRepository.dataItemLive();
        dataItemConfig.reInitComponent(this.mTargetMode, baseModule.getBogusCameraId(), baseModule.getCameraCapabilities());
        dataItemRunning.reInitComponent(this.mTargetMode, baseModule.getBogusCameraId(), baseModule.getCameraCapabilities(), dataItemGlobal.getIntentType());
        dataItemLive.reInitComponent(this.mTargetMode, baseModule.getBogusCameraId(), baseModule.getCameraCapabilities());
        dataItemGlobal.reInit();
        CameraSize previewSize = baseModule.getPreviewSize();
        DataRepository.dataItemRunning().setUiStyle(CameraSettings.getUIStyleByPreview(previewSize.width, previewSize.height));
        return nullHolder;
    }
}
