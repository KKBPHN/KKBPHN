package com.android.camera.module.loader;

import android.hardware.camera2.CaptureResult;
import com.android.camera.log.Log;
import com.android.camera2.Camera2Proxy.HdrCheckerCallback;
import com.android.camera2.CaptureResultParser;
import io.reactivex.functions.Function;
import java.lang.ref.WeakReference;

public class FunctionHdrDetect implements Function {
    private static final String TAG = "FunctionParseAsdHdr";
    private WeakReference mHdrCheckerCallback;
    private boolean mSupportHdrMotion;

    public FunctionHdrDetect(HdrCheckerCallback hdrCheckerCallback, boolean z) {
        this.mHdrCheckerCallback = new WeakReference(hdrCheckerCallback);
        this.mSupportHdrMotion = z;
    }

    public CaptureResult apply(CaptureResult captureResult) {
        boolean z;
        HdrCheckerCallback hdrCheckerCallback = (HdrCheckerCallback) this.mHdrCheckerCallback.get();
        if (hdrCheckerCallback == null || !hdrCheckerCallback.isHdrSceneDetectionStarted()) {
            return captureResult;
        }
        boolean z2 = false;
        if (this.mSupportHdrMotion) {
            z = CaptureResultParser.isHdrMotionDetected(captureResult);
            hdrCheckerCallback.onHdrMotionDetectionResult(z);
        } else {
            z = false;
        }
        int hdrDetectedScene = CaptureResultParser.getHdrDetectedScene(captureResult);
        int hdrMode = CaptureResultParser.getHdrMode(captureResult);
        StringBuilder sb = new StringBuilder();
        sb.append("apply() called with: hdrMode = ");
        sb.append(hdrMode);
        sb.append(",hdrStatus = ");
        sb.append(hdrDetectedScene);
        sb.append(", isMotionDetected = ");
        sb.append(z);
        Log.c(TAG, sb.toString());
        if (hdrCheckerCallback.isMatchCurrentHdrMode(hdrMode)) {
            if (hdrDetectedScene == 1 && !z) {
                z2 = true;
            }
            hdrCheckerCallback.onHdrSceneChanged(z2);
        }
        return captureResult;
    }
}
