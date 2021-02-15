package com.android.camera.scene;

import androidx.annotation.StringRes;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.module.BaseModule;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraModuleSpecial;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import java.lang.ref.WeakReference;

public abstract class ASDResultParse implements IResultParse {
    private WeakReference mDualController;
    private boolean mIsMacroModeEnable;
    protected final WeakReference mModule;
    private WeakReference mTips;
    private WeakReference mTopAlert;

    public ASDResultParse(WeakReference weakReference) {
        this.mModule = weakReference;
        WeakReference weakReference2 = this.mModule;
        if (weakReference2 != null && weakReference2.get() != null) {
            this.mIsMacroModeEnable = CameraSettings.isMacroModeEnabled(((BaseModule) this.mModule.get()).getModuleIndex());
        }
    }

    private DualController getDualController() {
        WeakReference weakReference = this.mDualController;
        if (weakReference == null || weakReference.get() == null) {
            this.mDualController = new WeakReference((DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182));
        }
        return (DualController) this.mDualController.get();
    }

    private BottomPopupTips getTips() {
        WeakReference weakReference = this.mTips;
        if (weakReference == null || weakReference.get() == null) {
            this.mTips = new WeakReference((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175));
        }
        return (BottomPopupTips) this.mTips.get();
    }

    public /* synthetic */ void O00000Oo(int i, int i2, int i3) {
        CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
        if (cameraModuleSpecial != null) {
            cameraModuleSpecial.showOrHideChip(false);
        }
        getTips().showTips(i, i2, i3);
    }

    public /* synthetic */ void O00ooo0o() {
        getTips().directlyHideTips();
    }

    /* access modifiers changed from: protected */
    public boolean closeTip() {
        if (getTips() == null) {
            return false;
        }
        BaseModule baseModule = (BaseModule) this.mModule.get();
        if (baseModule == null) {
            return false;
        }
        baseModule.getHandler().post(new O000000o(this));
        return true;
    }

    /* access modifiers changed from: protected */
    public TopAlert getTopAlert() {
        WeakReference weakReference = this.mTopAlert;
        if (weakReference == null || weakReference.get() == null) {
            this.mTopAlert = new WeakReference((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172));
        }
        return (TopAlert) this.mTopAlert.get();
    }

    /* access modifiers changed from: protected */
    public boolean isGeneralInterception() {
        return this.mIsMacroModeEnable;
    }

    /* access modifiers changed from: protected */
    public boolean isSuggestionIntercept(ASDScene aSDScene) {
        String str;
        BaseModule baseModule = (BaseModule) this.mModule.get();
        if (baseModule == null || isGeneralInterception()) {
            return true;
        }
        if (baseModule.getBogusCameraId() != 0) {
            str = "no back camera!";
        } else if (1.0f != baseModule.getZoomRatio()) {
            str = "zoom > 1x!";
        } else if (!MiAlgoAsdSceneProfile.isAlreadyTip() || !MiAlgoAsdSceneProfile.isCheckSceneEnable(aSDScene.type, aSDScene.value)) {
            if (getTopAlert() != null) {
                if (getTopAlert().isCurrentRecommendTipText(baseModule.isFrontCamera() ? R.string.lens_dirty_detected_title_front : R.string.lens_dirty_detected_title_back)) {
                    str = "dirty tip is visible!";
                }
            }
            boolean z = getDualController() != null && getDualController().isZoomPanelVisible();
            if (!z) {
                return false;
            }
            str = "Zoom bar is in effect, no prompt！";
        } else {
            str = "A tip has occurred this time.!";
        }
        FunctionMiAlgoASDEngine.LOGD(str);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean showTip(int i, @StringRes int i2, int i3) {
        if (i2 <= 0 || getTips() == null) {
            return false;
        }
        BaseModule baseModule = (BaseModule) this.mModule.get();
        if (baseModule == null) {
            return false;
        }
        baseModule.getHandler().post(new O00000Oo(this, i, i2, i3));
        return true;
    }
}
