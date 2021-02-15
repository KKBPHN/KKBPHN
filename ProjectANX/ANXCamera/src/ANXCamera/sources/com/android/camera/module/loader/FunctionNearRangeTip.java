package com.android.camera.module.loader;

import android.hardware.camera2.CaptureResult;
import android.os.Handler;
import com.android.camera.CameraSettings;
import com.android.camera.FuncPreviewMetadata;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.log.Log;
import com.android.camera.module.Camera2Module;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera2.CaptureResultParser;
import com.xiaomi.camera.util.SystemProperties;
import java.lang.ref.WeakReference;

public class FunctionNearRangeTip extends FuncPreviewMetadata {
    private static final boolean DEBUG;
    private static final String TAG = "FunctionNearRangeTip";
    private WeakReference mBottomPopupTips = new WeakReference((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175));
    private WeakReference mDualController = new WeakReference((DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182));

    static {
        boolean z = false;
        if (SystemProperties.getInt("near_range_dbg", 0) == 1) {
            z = true;
        }
        DEBUG = z;
    }

    public FunctionNearRangeTip(WeakReference weakReference) {
        super(weakReference);
    }

    private void DEBUG(String str, String str2) {
        if (DEBUG) {
            Log.d(str, str2);
        }
    }

    public /* synthetic */ void O00o0Ooo() {
        ((BottomPopupTips) this.mBottomPopupTips.get()).showNearRangeTip();
    }

    public /* synthetic */ void O00o0o00() {
        ((BottomPopupTips) this.mBottomPopupTips.get()).hideNearRangeTip();
    }

    public CaptureResult onPreviewMetadata(CaptureResult captureResult) {
        Runnable runnable;
        Handler handler;
        boolean z;
        if (this.mModuleReference.get() != null && (this.mModuleReference.get() instanceof Camera2Module)) {
            Camera2Module camera2Module = (Camera2Module) this.mModuleReference.get();
            boolean z2 = false;
            if (!camera2Module.getCameraCapabilities().supportNearRangeMode()) {
                camera2Module.setNearRangeModeUIStatus(false);
                camera2Module.setNearRangeMode(false);
                return captureResult;
            }
            boolean z3 = true;
            if (CaptureResultParser.getFallbackRoleId(camera2Module.getCameraCapabilities(), captureResult) != 1) {
                DEBUG(TAG, "NearRangeMode:Not satisfied <fallback role id UW>!");
                camera2Module.setNearRangeMode(false);
            } else {
                camera2Module.setNearRangeMode(true);
                if (camera2Module.getBogusCameraId() != 0) {
                    DEBUG(TAG, "NearRangeMode:Not satisfed <back facing>!");
                    z = false;
                } else {
                    z = true;
                }
                if (!camera2Module.isNeedNearRangeTip()) {
                    z = false;
                }
                if (!(camera2Module.getModuleIndex() == 163 || camera2Module.getModuleIndex() == 165)) {
                    DEBUG(TAG, "NearRangeMode:Not satisfed <capture mode>!");
                    z = false;
                }
                if (!HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                    DEBUG(TAG, "NearRangeMode:Not satisfed <sat device>!");
                    z = false;
                }
                if (CameraSettings.isDocumentModeOn(camera2Module.getModuleIndex())) {
                    DEBUG(TAG, "NearRangeMode:Not satisfed <document mode>!");
                    z = false;
                }
                if (this.mDualController.get() != null && ((DualController) this.mDualController.get()).isZoomPanelVisible()) {
                    DEBUG(TAG, "NearRangeMode:Not satisfed <zoom slide>!");
                    z = false;
                }
                MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                boolean z4 = miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow();
                if (z4) {
                    DEBUG(TAG, "NearRangeMode:Not satisfed <beauty panel>!");
                    z = false;
                }
                MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
                if (makeupProtocol == null || !makeupProtocol.isSeekBarVisible()) {
                    z3 = false;
                }
                if (z3) {
                    DEBUG(TAG, "NearRangeMode:Not satisfed <seek bar>!");
                } else {
                    z2 = z;
                }
            }
            Object obj = this.mBottomPopupTips.get();
            if (z2) {
                if (obj != null && !((BottomPopupTips) this.mBottomPopupTips.get()).isNearRangeTipShowing()) {
                    DEBUG(TAG, "NearRangeMode:Enter near range mode");
                    handler = camera2Module.getHandler();
                    runnable = new O000000o(this);
                }
                camera2Module.setNearRangeModeUIStatus(z2);
            } else {
                if (obj != null && ((BottomPopupTips) this.mBottomPopupTips.get()).isNearRangeTipShowing()) {
                    DEBUG(TAG, "NearRangeMode: hide near range mode tip");
                    handler = camera2Module.getHandler();
                    runnable = new O00000Oo(this);
                }
                camera2Module.setNearRangeModeUIStatus(z2);
            }
            handler.post(runnable);
            camera2Module.setNearRangeModeUIStatus(z2);
        }
        return captureResult;
    }
}
