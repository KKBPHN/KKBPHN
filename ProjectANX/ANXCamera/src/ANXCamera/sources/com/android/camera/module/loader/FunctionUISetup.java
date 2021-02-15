package com.android.camera.module.loader;

import android.graphics.Rect;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.LocationManager;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.module.BaseModule;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import io.reactivex.annotations.NonNull;

public class FunctionUISetup extends Func1Base {
    private boolean mNeedNotifyUI;

    public FunctionUISetup(int i, boolean z) {
        super(i);
        this.mNeedNotifyUI = z;
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
        baseModule.getActivity().showGuide(this.mTargetMode);
        Rect previewRect = Util.getPreviewRect(CameraAppImpl.getAndroidContext());
        baseModule.onPreviewLayoutChanged(previewRect);
        baseModule.onPreviewSizeChanged(previewRect.width(), previewRect.height());
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        int lastCameraId = DataRepository.dataItemGlobal().getLastCameraId();
        int currentCameraId = DataRepository.dataItemGlobal().getCurrentCameraId();
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        int i = 1;
        if (lastCameraId != currentCameraId) {
            i = 2;
        } else if (DataRepository.dataItemRunning().getLastUiStyle() != uiStyle) {
            i = 3;
        }
        baseModule.setDisplayRectAndUIStyle(previewRect, uiStyle);
        if (this.mNeedNotifyUI && baseDelegate != null) {
            baseDelegate.getAnimationComposite().notifyDataChanged(i, this.mTargetMode);
        }
        LocationManager.instance().recordLocation(CameraSettings.isRecordLocation());
        CameraSize previewSize = baseModule.getPreviewSize();
        if (previewSize != null) {
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null) {
                mainContentProtocol.setPreviewAspectRatio(CameraSettings.getPreviewAspectRatio(previewSize.width, previewSize.height));
            }
        }
        return nullHolder;
    }
}
