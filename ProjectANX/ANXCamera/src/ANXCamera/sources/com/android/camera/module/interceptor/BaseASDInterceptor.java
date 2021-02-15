package com.android.camera.module.interceptor;

import android.hardware.camera2.CaptureResult;

public abstract class BaseASDInterceptor extends BaseModuleInterceptor {
    public abstract void conditionDect();

    public int getPriority() {
        return 0;
    }

    public int getScope() {
        return 6;
    }

    public abstract int getType();

    public abstract void onCaptureResultNext(CaptureResult captureResult);
}
