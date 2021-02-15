package com.android.camera.module.loader;

import android.hardware.camera2.CaptureResult;
import com.android.camera.FuncPreviewMetadata;
import com.android.camera2.Camera2Proxy.LivePhotoResultCallback;
import com.xiaomi.camera.liveshot.LivePhotoResult;
import java.lang.ref.WeakReference;

public class FunctionLivePhoto extends FuncPreviewMetadata {
    private WeakReference mLivePhotoResultCallbackReference;

    public FunctionLivePhoto(WeakReference weakReference, WeakReference weakReference2) {
        super(weakReference);
        this.mLivePhotoResultCallbackReference = weakReference2;
    }

    public CaptureResult onPreviewMetadata(CaptureResult captureResult) {
        if (this.mLivePhotoResultCallbackReference.get() == null) {
            return captureResult;
        }
        LivePhotoResultCallback livePhotoResultCallback = (LivePhotoResultCallback) this.mLivePhotoResultCallbackReference.get();
        if (!livePhotoResultCallback.isLivePhotoStarted()) {
            return captureResult;
        }
        Integer num = (Integer) captureResult.get(CaptureResult.CONTROL_AE_STATE);
        Integer num2 = (Integer) captureResult.get(CaptureResult.CONTROL_AWB_STATE);
        Long l = (Long) captureResult.get(CaptureResult.SENSOR_TIMESTAMP);
        LivePhotoResult livePhotoResult = new LivePhotoResult();
        int i = 0;
        livePhotoResult.setAEState(num == null ? 0 : num.intValue());
        if (num2 != null) {
            i = num2.intValue();
        }
        livePhotoResult.setAWBState(i);
        livePhotoResult.setTimeStamp(l == null ? 0 : l.longValue());
        livePhotoResult.setGyroscropStable(livePhotoResultCallback.isGyroStable());
        livePhotoResult.setFilterId(livePhotoResultCallback.getFilterId());
        livePhotoResultCallback.onLivePhotoResultCallback(livePhotoResult);
        return captureResult;
    }
}
