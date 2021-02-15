package com.android.camera.module.loader;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.hardware.camera2.CaptureResult;
import android.util.Log;
import com.android.camera.constant.AsdSceneConstant;
import com.android.camera.module.BaseModule;
import com.android.camera.module.ModuleManager;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.ASDSceneCallback;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CaptureResultParser;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import io.reactivex.functions.Function;
import java.lang.ref.WeakReference;

public class FunctionAsdSceneDetect implements Function, AsdSceneConstant {
    private static final float AEC_LUX_HEIGHT_LIGHT = ((float) C0122O00000o.instance().O0oo0oo());
    private static final float AEC_LUX_LAST_LIGHT = ((float) C0122O00000o.instance().O0oo());
    private static final float AEC_LUX_LOW_LIGHT = 450.0f;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int FRAME_BYPASS_NUMBER = 2;
    private static final float LENS_FOCUS_DISTANCE_TOO_CLOSE = 2.5f;
    private static final float LENS_FOCUS_DISTANCE_TOO_FAR = 0.5f;
    private static final Integer REAL_BV_HEIGHT_LIGHT = Integer.valueOf(-1800);
    private static final Integer REAL_BV_LAST_LIGHT = Integer.valueOf(-2000);
    private static final String TAG = "FunctionParseAsdScene";
    private static int mFrameNumber;
    private static boolean mIsFlashRetain;
    private final boolean mEnableBv;
    private float mLowLightValue;
    private WeakReference mModuleWeakReference;

    public FunctionAsdSceneDetect(BaseModule baseModule, CameraCapabilities cameraCapabilities) {
        this.mModuleWeakReference = new WeakReference(baseModule);
        this.mLowLightValue = ModuleManager.isFunARModule() ? ((float) REAL_BV_HEIGHT_LIGHT.intValue()) * 0.87f : (float) REAL_BV_HEIGHT_LIGHT.intValue();
        boolean z = C0124O00000oO.isMTKPlatform() && cameraCapabilities != null && cameraCapabilities.isTagDefined(CaptureResultVendorTags.REAL_BV.getName());
        this.mEnableBv = z;
    }

    private static float getLowLightValue() {
        return ModuleManager.isFunARModule() ? (float) C0122O00000o.instance().getConfig().O0000OOo() : AEC_LUX_LOW_LIGHT;
    }

    public Integer apply(CaptureResult captureResult) {
        BaseModule baseModule = (BaseModule) this.mModuleWeakReference.get();
        Integer valueOf = Integer.valueOf(-1);
        if (baseModule != null) {
            Camera2Proxy cameraDevice = baseModule.getCameraDevice();
            boolean isNeedFlashForAuto = cameraDevice != null ? cameraDevice.isNeedFlashForAuto((Integer) captureResult.get(CaptureResult.CONTROL_AE_STATE), cameraDevice.getFlashMode()) : false;
            boolean isFrontCamera = baseModule.isFrontCamera();
            boolean isScreenSlideOff = baseModule.getActivity() != null ? baseModule.getActivity().isScreenSlideOff() : false;
            if (baseModule.isPortraitMode() || isFrontCamera || baseModule.isMimojiMode()) {
                return Integer.valueOf(parseRtbSceneResult(captureResult, isFrontCamera, isScreenSlideOff));
            }
            if (isNeedFlashForAuto) {
                return (!(baseModule instanceof ASDSceneCallback) || !((ASDSceneCallback) baseModule).isAutoFlashOff()) ? Integer.valueOf(0) : valueOf;
            }
        }
        return valueOf;
    }

    public int parseRtbSceneResult(CaptureResult captureResult, boolean z, boolean z2) {
        float aecLux = CaptureResultParser.getAecLux(captureResult);
        String str = TAG;
        if (!z) {
            mIsFlashRetain = false;
            this.mLowLightValue = getLowLightValue();
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("<back facing>aecLux:");
                sb.append(aecLux);
                sb.append(",low_light_value:");
                sb.append(this.mLowLightValue);
                com.android.camera.log.Log.d(str, sb.toString());
            }
            if (aecLux > this.mLowLightValue) {
                return 6;
            }
            if (captureResult.get(CaptureResult.LENS_FOCUS_DISTANCE) == null) {
                return -1;
            }
            float floatValue = ((Float) captureResult.get(CaptureResult.LENS_FOCUS_DISTANCE)).floatValue();
            if (floatValue >= LENS_FOCUS_DISTANCE_TOO_CLOSE) {
                return 4;
            }
            return floatValue <= 0.5f ? 5 : 7;
        } else if (!z2) {
            if (C0122O00000o.instance().OO0o00o()) {
                int i = mFrameNumber;
                if (i < 2) {
                    mFrameNumber = i + 1;
                    return -1;
                }
            }
            String str2 = ",mIsFlashRetain:";
            String str3 = ",,low_light_value:";
            if (this.mEnableBv) {
                Integer realBV = CaptureResultParser.getRealBV(captureResult);
                if (DEBUG) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("<front facing>realBV:");
                    sb2.append(realBV);
                    sb2.append(",REAL_BV_LAST_LIGHT:");
                    sb2.append(REAL_BV_LAST_LIGHT);
                    sb2.append(str3);
                    sb2.append(this.mLowLightValue);
                    sb2.append(str2);
                    sb2.append(mIsFlashRetain);
                    com.android.camera.log.Log.d(str, sb2.toString());
                }
                if (mIsFlashRetain && ((float) realBV.intValue()) < this.mLowLightValue) {
                    return 9;
                }
                if (realBV.intValue() < REAL_BV_LAST_LIGHT.intValue()) {
                    mIsFlashRetain = true;
                    return 9;
                }
                mIsFlashRetain = false;
                return -1;
            }
            this.mLowLightValue = ModuleManager.isFunARModule() ? AEC_LUX_HEIGHT_LIGHT * 0.87f : AEC_LUX_HEIGHT_LIGHT;
            if (DEBUG) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("<front facing>aecLux:");
                sb3.append(aecLux);
                sb3.append(",AEC_LUX_LAST_LIGHT:");
                sb3.append(AEC_LUX_LAST_LIGHT);
                sb3.append(str3);
                sb3.append(this.mLowLightValue);
                sb3.append(str2);
                sb3.append(mIsFlashRetain);
                com.android.camera.log.Log.d(str, sb3.toString());
            }
            if (mIsFlashRetain && aecLux > this.mLowLightValue) {
                return 9;
            }
            if (aecLux > AEC_LUX_LAST_LIGHT) {
                mIsFlashRetain = true;
                return 9;
            }
            mIsFlashRetain = false;
            return -1;
        } else {
            mFrameNumber = 0;
            return -1;
        }
    }
}
