package com.android.camera.module.loader;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.hardware.camera2.CaptureResult;
import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.module.BaseModule;
import com.android.camera.module.Camera2Module;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MiAsdDetect;
import com.android.camera.scene.MiAlgoAsdSceneProfile;
import com.android.camera2.CaptureResultParser;
import io.reactivex.functions.Function;
import java.lang.ref.WeakReference;

public class FunctionParseAsdUltraWide implements Function {
    private static final int[] ULTRA_WIDE_RECOM = {2, 1};
    public static final int ULTRA_WIDE_SIDEFACE_TYPE = 2;
    public static final int ULTRA_WIDE_TOWER_BUILDING_TYPE = 1;
    private boolean mEnable;
    private boolean mIsOpenUltraWide;
    private WeakReference mMiAsdDetectProtocol;
    private WeakReference mModule;

    public FunctionParseAsdUltraWide(int i, BaseModule baseModule, float f) {
        boolean z = C0122O00000o.instance().isSupportUltraWide() && (i == 163 || i == 165) && !CameraSettings.isUltraWideConfigOpen(i) && !CameraSettings.isUltraPixelOn() && !CameraSettings.isMacroModeEnabled(i) && f < 3.0f;
        this.mEnable = z;
        if (this.mEnable) {
            this.mModule = new WeakReference(baseModule);
        }
    }

    private boolean isOpenUltraWide(int i) {
        int i2 = 0;
        boolean z = false;
        while (true) {
            int[] iArr = ULTRA_WIDE_RECOM;
            if (i2 >= iArr.length) {
                return z;
            }
            z = iArr[i2] == i;
            if (z) {
                return z;
            }
            i2++;
        }
    }

    public CaptureResult apply(CaptureResult captureResult) {
        if (!this.mEnable) {
            return captureResult;
        }
        BaseModule baseModule = (BaseModule) this.mModule.get();
        if (baseModule == null) {
            return captureResult;
        }
        if (this.mMiAsdDetectProtocol == null) {
            this.mMiAsdDetectProtocol = new WeakReference((MiAsdDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(235));
        }
        int ultraWideDetectedResult = CaptureResultParser.getUltraWideDetectedResult(captureResult);
        boolean isOpenUltraWide = isOpenUltraWide(ultraWideDetectedResult);
        if (((Camera2Module) baseModule).isZoomRatioBetweenUltraAndWide()) {
            isOpenUltraWide = false;
        }
        boolean aIWatermarkEnable = DataRepository.dataItemRunning().getComponentRunningAIWatermark().getAIWatermarkEnable(baseModule.getModuleIndex());
        if (this.mIsOpenUltraWide != isOpenUltraWide && !aIWatermarkEnable) {
            this.mIsOpenUltraWide = isOpenUltraWide;
            if (this.mMiAsdDetectProtocol.get() == null || MiAlgoAsdSceneProfile.isAlreadyTip()) {
                return captureResult;
            }
            ((MiAsdDetect) this.mMiAsdDetectProtocol.get()).updateUltraWide(isOpenUltraWide, ultraWideDetectedResult);
        }
        return captureResult;
    }
}
