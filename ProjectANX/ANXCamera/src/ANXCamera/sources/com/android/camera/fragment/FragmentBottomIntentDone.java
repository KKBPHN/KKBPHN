package com.android.camera.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.FragmentAnimationFactory;
import com.android.camera.data.DataRepository;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;

public class FragmentBottomIntentDone extends BaseFragment implements OnClickListener, HandleBackTrace {
    public static final int FRAGMENT_INFO = 4083;
    private ImageView mApplyView;
    private View mMainView;
    private ImageView mRetryView;

    private void adjustViewBackground(View view, int i) {
        ImageView imageView;
        int i2;
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        if (uiStyle == 1 || uiStyle == 3) {
            view.setBackgroundResource(R.color.fullscreen_background);
            imageView = this.mRetryView;
            i2 = R.drawable.bg_thumbnail_background_full;
        } else {
            view.setBackgroundResource(R.color.halfscreen_background);
            imageView = this.mRetryView;
            i2 = R.drawable.bg_thumbnail_background_half;
        }
        imageView.setBackgroundResource(i2);
        this.mApplyView.setBackgroundResource(i2);
    }

    public int getFragmentInto() {
        return 4083;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_bottom_intent_done;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mMainView = view.findViewById(R.id.intent_bottom);
        ((MarginLayoutParams) this.mMainView.getLayoutParams()).height = Display.getBottomHeight();
        ((MarginLayoutParams) view.findViewById(R.id.intent_bg).getLayoutParams()).height = Display.getBottomHeight() + Display.getSquareBottomCoverHeight();
        this.mRetryView = (ImageView) view.findViewById(R.id.intent_done_retry);
        this.mApplyView = (ImageView) view.findViewById(R.id.done_button);
        adjustViewBackground(this.mMainView, this.mCurrentMode);
        this.mRetryView.setOnClickListener(this);
        this.mApplyView.setOnClickListener(this);
        FolmeUtils.touchScaleTint(this.mRetryView, this.mApplyView);
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        if (i == 2 || i == 3) {
            adjustViewBackground(this.mMainView, this.mCurrentMode);
        }
    }

    public boolean onBackEvent(int i) {
        if (i != 1 || !canProvide()) {
            return false;
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null) {
            cameraAction.onReviewCancelClicked();
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null) {
            int id = view.getId();
            if (id == R.id.done_button) {
                cameraAction.onReviewDoneClicked();
            } else if (id == R.id.intent_done_retry) {
                cameraAction.onReviewCancelClicked();
            }
        }
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(167, 161);
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(168, 162);
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
