package com.android.camera.dualvideo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.dualvideo.user_guide.FragmentDualVideoUserGuide;
import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.log.Log;
import com.android.camera.module.VideoBase;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ModuleContent;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.MultiCameraAttr;
import java.util.ArrayList;

public class DualVideoSelectModule extends DualVideoModuleBase {
    private ArrayList mSelectDataBak;

    public DualVideoSelectModule() {
        super(DualVideoSelectModule.class.getSimpleName());
    }

    /* access modifiers changed from: private */
    public void addBottomControl() {
        View view;
        ViewGroup parent = ((ModuleContent) ModeCoordinatorImpl.getInstance().getAttachProtocol(431)).getParent();
        if (ModuleUtil.isFatScreen()) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.dual_video_right_control, parent, true);
            ((MarginLayoutParams) ((RelativeLayout) view.findViewById(R.id.dual_video_bottom_parent)).getLayoutParams()).width = (int) ((((float) Display.getWindowWidth()) - ((((float) Display.getWindowHeight()) / 16.0f) * 9.0f)) / 2.0f);
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.dual_video_bottom_control, parent, true);
            ((MarginLayoutParams) ((RelativeLayout) view.findViewById(R.id.dual_video_bottom_parent)).getLayoutParams()).height = Display.getBottomHeight();
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.dual_video_confirm);
        ImageView imageView2 = (ImageView) findViewById(R.id.dual_video_cancel);
        imageView.setOnClickListener(new C0162O000OoOO(this));
        imageView2.setOnClickListener(new C0163O000OoOo(this));
        FolmeUtils.touchTint((View) imageView2);
        FolmeUtils.touchScale(imageView, imageView2);
        this.mRotateAnimator.addView(imageView);
        this.mRotateAnimator.addView(imageView2);
    }

    private void cancelSelsectAndBackRecording() {
        restoreSelectData();
        confirmAndBackRecording();
        CameraStatUtils.trackDualVideoCommonClick(MultiCameraAttr.VALUE_CANCEL_SELECT);
    }

    private void confirmAndBackRecording() {
        this.mKeepRecorderWhenSwitching = true;
        switchToRecordWindow(-1);
        showOrHideBottom(true);
    }

    private void copySelectData() {
        if (this.mMultiRecorderManager.isRecording()) {
            if (this.mSelectDataBak == null) {
                this.mSelectDataBak = new ArrayList();
            }
            this.mSelectDataBak.clear();
            CameraSettings.getDualVideoConfig().getSelectedData().forEach(new C0165O000Ooo0(this));
        }
    }

    private void onCancelViewClicked() {
        cancelSelsectAndBackRecording();
    }

    /* access modifiers changed from: private */
    public void onConfirmClicked(View view) {
        confirmAndBackRecording();
        CameraStatUtils.trackDualVideoCommonClick(MultiCameraAttr.VALUE_CONFIRM_SELECT);
    }

    private void restoreSelectData() {
        if (this.mSelectDataBak != null) {
            ArrayList selectedData = CameraSettings.getDualVideoConfig().getSelectedData();
            selectedData.clear();
            selectedData.addAll(this.mSelectDataBak);
            this.mSelectDataBak.clear();
            this.mSelectDataBak = null;
        }
    }

    private void switchToRecordWindow(int i) {
        Log.d(VideoBase.TAG, "switchSelecteWindowToRecord: ");
        if (!isSwitching() && this.mMainFrameIsAvailable) {
            switchThumbnailFunction(false);
            DataRepository.dataItemRunning().getComponentRunningDualVideo().setmDrawSelectWindow(false);
            showModeSwitchLayout(false);
            getRenderManager().ifPresent(O000o00.INSTANCE);
            getRenderManager().ifPresent(C0164O000Ooo.INSTANCE);
            getActivity().getGLView().requestRender();
            switchCameraLens(false, false, false, i);
        }
    }

    public /* synthetic */ void O0000Oo(UserSelectData userSelectData) {
        this.mSelectDataBak.add(new UserSelectData(userSelectData));
    }

    public /* synthetic */ void O000O0OO(View view) {
        onCancelViewClicked();
    }

    public void addUserGuide() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) topAlert;
            String str = FragmentDualVideoUserGuide.TAG;
            if (FragmentUtils.getFragmentByTag(baseFragment.getFragmentManager(), str) == null) {
                FragmentDualVideoUserGuide fragmentDualVideoUserGuide = new FragmentDualVideoUserGuide();
                fragmentDualVideoUserGuide.setStyle(2, R.style.ManuallyDescriptionFragment);
                baseFragment.getFragmentManager().beginTransaction().add((Fragment) fragmentDualVideoUserGuide, str).commitAllowingStateLoss();
            }
            CameraStatUtils.trackDualVideoCommonClick(MultiCameraAttr.VALUE_USER_GUIDE);
            return;
        }
        Log.e(VideoBase.TAG, "addUserGuide:  failed ");
    }

    public boolean onBackPressed() {
        if (!isFrameAvailable()) {
            return false;
        }
        if (this.mPaused || this.mActivity.isActivityPaused()) {
            return true;
        }
        if (!this.mMultiRecorderManager.isRecording()) {
            return super.onBackPressed();
        }
        cancelSelsectAndBackRecording();
        return true;
    }

    public void onResume() {
        super.onResume();
        copySelectData();
        if (this.mMultiRecorderManager.isRecordingPaused()) {
            showModeSwitchLayout(false);
            getActivity().runOnUiThread(new C0166O000OooO(this));
            return;
        }
        showModeSwitchLayout(true);
    }

    public void onShutterButtonClick(int i) {
        Log.k(3, VideoBase.TAG, "onShutterButtonClick");
        switchToRecordWindow(2);
    }
}
