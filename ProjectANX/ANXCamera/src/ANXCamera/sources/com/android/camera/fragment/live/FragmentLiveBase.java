package com.android.camera.fragment.live;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import androidx.annotation.Nullable;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomMenuProtocol;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import java.util.List;

public abstract class FragmentLiveBase extends BaseFragment implements HandleBackTrace {
    protected boolean mIsNeedShowWhenExit = true;
    protected boolean mRemoveFragment;

    static /* synthetic */ void O000oo() {
    }

    public void onAnimationEnd() {
        if (this.mRemoveFragment) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction != null && !cameraAction.isDoingAction() && this.mIsNeedShowWhenExit) {
                ((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175)).reInitTipImage();
            }
            this.mRemoveFragment = false;
        }
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            int i = this.mCurrentMode;
            if ((i == 174 || i == 161 || i == 183) && DataRepository.dataItemGlobal().getCurrentCameraId() == 0) {
                DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                if (dualController != null) {
                    dualController.showZoomButton();
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null) {
                        topAlert.clearAlertStatus();
                    }
                }
            }
        }
    }

    public boolean onBackEvent(int i) {
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate == null || baseDelegate.getActiveFragment(R.id.bottom_beauty) != getFragmentInto()) {
            return false;
        }
        this.mRemoveFragment = true;
        baseDelegate.delegateEvent(10);
        ((BottomMenuProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(197)).onRestoreCameraActionMenu(i);
        FolmeUtils.animateDeparture(getView(), O00000Oo.INSTANCE);
        onAnimationEnd();
        return true;
    }

    public void onPause() {
        super.onPause();
        onBackEvent(5);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        FolmeUtils.animateEntrance(view);
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        onBackEvent(i2 != 3 ? 5 : 4);
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return null;
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
    }
}
