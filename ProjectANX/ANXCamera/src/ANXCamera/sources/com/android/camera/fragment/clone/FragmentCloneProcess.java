package com.android.camera.fragment.clone;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.bottom.BottomAnimationConfig;
import com.android.camera.fragment.vv.VVShareAdapter;
import com.android.camera.fragment.vv.page.PageIndicatorView;
import com.android.camera.fragment.vv.page.PagerGridLayoutManager;
import com.android.camera.fragment.vv.page.PagerGridLayoutManager.PageListener;
import com.android.camera.fragment.vv.page.PagerGridSnapHelper;
import com.android.camera.log.Log;
import com.android.camera.module.Module;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CloneAction;
import com.android.camera.protocol.ModeProtocol.CloneChooser;
import com.android.camera.protocol.ModeProtocol.CloneProcess;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.CloneAttr;
import com.android.camera.storage.MediaProviderUtil;
import com.android.camera.ui.CameraSnapView;
import com.android.camera.ui.CameraSnapView.SnapListener;
import com.android.camera.ui.ScrollTextview;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import com.xiaomi.fenshen.FenShenCam;
import com.xiaomi.fenshen.FenShenCam.Mode;
import io.reactivex.Completable;
import java.util.List;
import miui.extension.target.ActivityTarget;

public class FragmentCloneProcess extends BaseFragment implements OnClickListener, HandleBackTrace, CloneProcess, SnapListener {
    private static final String TAG = "FragmentCloneProcess";
    private ScrollTextview mAdjustButton;
    private ViewGroup mBottomActionView;
    private ViewGroup mBottomLayout;
    protected CameraSnapView mCameraSnapView;
    private ImageView mCancelCapture;
    protected TextView mCaptureHint;
    protected boolean mCaptureHintPined;
    protected boolean mDetectedPersonInPreview;
    private View mExitDialog;
    private TextView mExitDialogCancel;
    private TextView mExitDialogConfirm;
    private TextView mExitDialogMessage;
    protected boolean mExitToDummyClone;
    private GestureDetector mGestureDetector;
    private SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
        public boolean onDown(MotionEvent motionEvent) {
            CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
            if (cloneAction != null) {
                return cloneAction.onTouchDown(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
            if (cloneAction != null) {
                return cloneAction.onScroll(motionEvent, motionEvent2, f, f2);
            }
            return false;
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
            if (cloneAction != null) {
                return cloneAction.onTapUp(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        }
    };
    private ImageView mGiveUpToPreview;
    private boolean mIsPendingShowComposeResult;
    protected TextView mLandscapeHint;
    protected Mode mMode;
    private int mOldControlStream = -1;
    /* access modifiers changed from: private */
    public PageIndicatorView mPageIndicatorView;
    private PagerGridSnapHelper mPagerGridSnapHelper;
    protected boolean mPaused;
    private boolean mPendingShare;
    protected ImageView mPlayButton;
    private ImageView mResetEdit;
    private View mRootView;
    private ImageView mSaveAndShare;
    protected LottieAnimationView mSaveButton;
    protected ContentValues mSaveContentValues;
    private ImageView mSaveEdit;
    protected String mSavedPath;
    private VVShareAdapter mShareAdapter;
    private View mShareDialog;
    private TextView mShareMessage;
    private ProgressBar mShareProgress;
    private RecyclerView mShareRecyclerView;
    private ImageView mSnapViewProgress;
    protected Status mStatus;
    private ImageView mStopCapture;

    static /* synthetic */ float O0000Oo0(float f) {
        if (f == 1.0f) {
            return 1.0f;
        }
        return (float) ((-Math.pow(2.0d, (double) (f * -10.0f))) + 1.0d);
    }

    private void addViewForGestureRecognize(ViewGroup viewGroup) {
        Log.d(TAG, "addViewForGestureRecognize: ");
        View view = new View(viewGroup.getContext());
        view.setOnTouchListener(new O00000o0(this));
        Rect displayRect = Display.getDisplayRect(1);
        LayoutParams layoutParams = new LayoutParams(displayRect.width(), displayRect.height());
        layoutParams.topMargin = Display.getTopMargin() + Display.getTopBarHeight();
        viewGroup.addView(view, 0, layoutParams);
    }

    private void alphaAnimateIn(View view) {
        if (view.getVisibility() != 0) {
            Completable.create(new AlphaInOnSubscribe(view)).subscribe();
        }
    }

    private void alphaAnimateOut(View view) {
        if (view.getVisibility() == 0) {
            Completable.create(new AlphaOutOnSubscribe(view)).subscribe();
        }
    }

    private boolean checkAndShare() {
        if (this.mSavedPath == null) {
            return false;
        }
        showShareSheet();
        return true;
    }

    private void restoreVolumeControlStream() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            int i = this.mOldControlStream;
            if (i != -1) {
                activity.setVolumeControlStream(i);
            }
        }
    }

    private void setProgressBarVisible(boolean z) {
        int i;
        ImageView imageView;
        if (z && this.mSnapViewProgress.getVisibility() == 0) {
            return;
        }
        if (z || this.mSnapViewProgress.getVisibility() == 0) {
            if (z) {
                RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, (float) (this.mSnapViewProgress.getLayoutParams().width / 2), (float) (this.mSnapViewProgress.getLayoutParams().height / 2));
                rotateAnimation.setDuration((long) getResources().getInteger(R.integer.post_process_duration));
                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setRepeatMode(1);
                rotateAnimation.setRepeatCount(-1);
                this.mSnapViewProgress.setAnimation(rotateAnimation);
                imageView = this.mSnapViewProgress;
                i = 0;
            } else {
                this.mSnapViewProgress.clearAnimation();
                imageView = this.mSnapViewProgress;
                i = 8;
            }
            imageView.setVisibility(i);
        }
    }

    private void setVolumeControlStream(int i) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            this.mOldControlStream = activity.getVolumeControlStream();
            activity.setVolumeControlStream(i);
            StringBuilder sb = new StringBuilder();
            sb.append("setVolumeControlStream ");
            sb.append(i);
            Log.d(TAG, sb.toString());
        }
    }

    private void showAdjustButton() {
        if (this.mAdjustButton.getVisibility() != 0) {
            Rect displayRect = Util.getDisplayRect();
            Context context = getContext();
            int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R.dimen.clone_adjust_width);
            int dimensionPixelOffset2 = context.getResources().getDimensionPixelOffset(R.dimen.clone_adjust_height) / 2;
            int dimensionPixelOffset3 = (context.getResources().getDimensionPixelOffset(R.dimen.clone_adjust_margin_left) + dimensionPixelOffset2) - (dimensionPixelOffset / 2);
            int height = (displayRect.top + (displayRect.height() / 2)) - dimensionPixelOffset2;
            LayoutParams layoutParams = (LayoutParams) this.mAdjustButton.getLayoutParams();
            layoutParams.leftMargin = dimensionPixelOffset3;
            layoutParams.topMargin = height;
            this.mAdjustButton.setVisibility(0);
        }
    }

    private void showCloneUseGuide() {
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).delegateEvent(23);
    }

    private void showShareSheet() {
        this.mPendingShare = false;
        if (!this.mPaused) {
            Intent shareMediaIntent = Util.getShareMediaIntent(getContext(), this.mSavedPath, isVideoMode());
            PackageManager packageManager = getContext().getPackageManager();
            List queryIntentActivities = packageManager.queryIntentActivities(shareMediaIntent, 65536);
            if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
                Log.d(TAG, "no IntentActivities");
                return;
            }
            int i = this.mShareRecyclerView.getLayoutParams().width / 4;
            VVShareAdapter vVShareAdapter = this.mShareAdapter;
            if (vVShareAdapter == null || vVShareAdapter.getItemCount() != queryIntentActivities.size()) {
                this.mShareAdapter = new VVShareAdapter(packageManager, queryIntentActivities, this, i);
                PagerGridLayoutManager pagerGridLayoutManager = new PagerGridLayoutManager(2, 4, 1);
                pagerGridLayoutManager.setPageListener(new PageListener() {
                    public void onPageSelect(int i) {
                        FragmentCloneProcess.this.mPageIndicatorView.setSelectedPage(i);
                    }

                    public void onPageSizeChanged(int i) {
                    }
                });
                int ceil = (int) Math.ceil((double) (((float) queryIntentActivities.size()) / 8.0f));
                this.mPageIndicatorView.initIndicator(ceil);
                if (ceil <= 1) {
                    this.mPageIndicatorView.setVisibility(8);
                } else {
                    this.mPageIndicatorView.setVisibility(0);
                }
                if (this.mPagerGridSnapHelper == null) {
                    this.mPagerGridSnapHelper = new PagerGridSnapHelper();
                    this.mShareRecyclerView.setLayoutManager(pagerGridLayoutManager);
                    this.mPagerGridSnapHelper.attachToRecyclerView(this.mShareRecyclerView);
                }
                this.mShareRecyclerView.setAdapter(this.mShareAdapter);
                this.mShareMessage.setText(R.string.snap_cancel);
                this.mShareMessage.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FragmentCloneProcess.this.hideShareSheet();
                    }
                });
            } else {
                this.mShareAdapter.setShareInfoList(queryIntentActivities);
                this.mShareAdapter.notifyDataSetChanged();
            }
            Completable.create(new AlphaInOnSubscribe(this.mShareDialog)).subscribe();
        }
    }

    private void updateUiOnOrientationChanged() {
        StringBuilder sb = new StringBuilder();
        sb.append("updateUiOnOrientationChanged isLandScape = ");
        sb.append(isLandScape());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (this.mStatus != Status.CAPTURING) {
            Log.d(str, "ignore updateUiOnOrientationChanged when not capturing");
            return;
        }
        if (isLandScape()) {
            this.mLandscapeHint.setVisibility(8);
            if (this.mCaptureHint.getTag() instanceof Boolean) {
                updateCaptureHintBackground(((Boolean) this.mCaptureHint.getTag()).booleanValue());
            }
            if (!TextUtils.isEmpty(this.mCaptureHint.getText())) {
                showCaptureHint();
            }
            setSnapButtonEnable(true, true);
        } else {
            this.mLandscapeHint.setVisibility(0);
            this.mCaptureHint.setVisibility(8);
            setSnapButtonEnable(false, true);
        }
    }

    public /* synthetic */ boolean O00000oO(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 1) {
            CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
            if (cloneAction != null) {
                cloneAction.onTouchUp(motionEvent.getX(), motionEvent.getY());
            }
        }
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    public boolean canSnap() {
        if (isLandScape()) {
            CameraSnapView cameraSnapView = this.mCameraSnapView;
            if (cameraSnapView != null && cameraSnapView.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void checkModeValidation() {
        if (this.mCurrentMode != 185) {
            quit();
        }
    }

    /* access modifiers changed from: protected */
    public void enableUseGuideMenu(boolean z) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.enableMenuItem(z, 164);
        }
    }

    /* access modifiers changed from: protected */
    public int getDurationVideoRecording() {
        return this.mMode == Mode.MCOPY ? 10000 : 5000;
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_CLONE_PROCESS;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_clone_process;
    }

    public Mode getMode() {
        return this.mMode;
    }

    public Status getStatus() {
        return this.mStatus;
    }

    /* access modifiers changed from: protected */
    public void hiddenPlayButton() {
        this.mPlayButton.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public boolean hideExitDialog() {
        if (this.mExitDialog.getVisibility() != 0) {
            return false;
        }
        setSnapButtonEnable(true, true);
        this.mExitDialog.setVisibility(8);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean hideShareSheet() {
        if (this.mShareDialog.getVisibility() != 0) {
            return false;
        }
        this.mShareDialog.setVisibility(8);
        return true;
    }

    /* access modifiers changed from: protected */
    public void initShutterButton(Mode mode) {
        CameraSnapView cameraSnapView;
        this.mCurrentMode = 185;
        this.mMode = mode;
        StringBuilder sb = new StringBuilder();
        sb.append("initShutterButton ");
        sb.append(mode);
        Log.d(TAG, sb.toString());
        if (mode == Mode.VIDEO || mode == Mode.MCOPY) {
            PaintConditionReferred create = PaintConditionReferred.create(this.mCurrentMode);
            create.setTargetFrameRatio(1.7777777f);
            this.mCameraSnapView.setParameters(create);
            this.mCameraSnapView.showCirclePaintItem();
            this.mCameraSnapView.showRoundPaintItem();
            cameraSnapView = this.mCameraSnapView;
        } else {
            PaintConditionReferred create2 = PaintConditionReferred.create(163);
            create2.setTargetFrameRatio(1.7777777f);
            this.mCameraSnapView.setParameters(create2);
            this.mCameraSnapView.showCirclePaintItem();
            this.mCameraSnapView.showRoundPaintItem();
            boolean isSpeechShutterOpen = CameraSettings.isSpeechShutterOpen();
            cameraSnapView = this.mCameraSnapView;
            if (isSpeechShutterOpen) {
                cameraSnapView.startSpeech();
                return;
            }
        }
        cameraSnapView.stopSpeech();
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mRootView = view;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        this.mSnapViewProgress = (ImageView) view.findViewById(R.id.clone_snap_view_progress);
        this.mShareProgress = (ProgressBar) view.findViewById(R.id.clone_share_progress);
        this.mCameraSnapView = (CameraSnapView) view.findViewById(R.id.clone_shutter_button);
        this.mCameraSnapView.setSnapListener(this);
        setSnapButtonEnable(false, true);
        this.mGiveUpToPreview = (ImageView) view.findViewById(R.id.clone_give_up_to_preview);
        this.mSaveAndShare = (ImageView) view.findViewById(R.id.clone_save_and_share);
        this.mStopCapture = (ImageView) view.findViewById(R.id.clone_stop_capture);
        this.mSaveEdit = (ImageView) view.findViewById(R.id.clone_save_edit);
        this.mResetEdit = (ImageView) view.findViewById(R.id.clone_reset_edit);
        this.mAdjustButton = (ScrollTextview) view.findViewById(R.id.clone_adjust);
        this.mPlayButton = (ImageView) view.findViewById(R.id.clone_resume_play);
        this.mCancelCapture = (ImageView) view.findViewById(R.id.clone_cancel_capture);
        this.mSaveButton = (LottieAnimationView) view.findViewById(R.id.clone_save_button);
        this.mLandscapeHint = (TextView) view.findViewById(R.id.clone_landscape_hint);
        this.mCaptureHint = (TextView) view.findViewById(R.id.clone_capture_hint);
        this.mExitDialog = view.findViewById(R.id.vv_dialog);
        this.mExitDialogMessage = (TextView) this.mExitDialog.findViewById(R.id.vv_dialog_message);
        this.mExitDialogConfirm = (TextView) this.mExitDialog.findViewById(R.id.vv_dialog_positive);
        this.mExitDialogCancel = (TextView) this.mExitDialog.findViewById(R.id.vv_dialog_negative);
        this.mShareDialog = view.findViewById(R.id.vv_share);
        this.mShareMessage = (TextView) this.mShareDialog.findViewById(R.id.vv_share_message);
        this.mShareRecyclerView = (RecyclerView) this.mShareDialog.findViewById(R.id.vv_share_recyclerview);
        this.mShareRecyclerView.setFocusable(false);
        this.mPageIndicatorView = (PageIndicatorView) this.mShareDialog.findViewById(R.id.vv_share_recyclerview_indicator);
        Rect displayRect = Util.getDisplayRect();
        int i = displayRect.top;
        int windowHeight = (i - ((Display.getWindowHeight() - i) - displayRect.height())) / 2;
        ViewCompat.setRotation(this.mCameraSnapView, 90.0f);
        ViewCompat.setRotation(this.mGiveUpToPreview, 90.0f);
        ViewCompat.setRotation(this.mSaveAndShare, 90.0f);
        ViewCompat.setRotation(this.mSaveButton, 90.0f);
        ViewCompat.setRotation(this.mStopCapture, 90.0f);
        ViewCompat.setRotation(this.mCancelCapture, 90.0f);
        ViewCompat.setRotation(this.mLandscapeHint, 90.0f);
        ViewCompat.setRotation(this.mCaptureHint, 90.0f);
        ViewCompat.setRotation(this.mExitDialog, 90.0f);
        ViewCompat.setRotation(this.mShareDialog, 90.0f);
        ViewCompat.setRotation(this.mResetEdit, 90.0f);
        ViewCompat.setRotation(this.mSaveEdit, 90.0f);
        ViewCompat.setRotation(this.mAdjustButton, 90.0f);
        ViewCompat.setRotation(this.mPlayButton, 90.0f);
        addViewForGestureRecognize((ViewGroup) this.mRootView);
        this.mCameraSnapView.setOnClickListener(this);
        this.mGiveUpToPreview.setOnClickListener(this);
        this.mSaveAndShare.setOnClickListener(this);
        this.mCancelCapture.setOnClickListener(this);
        this.mStopCapture.setOnClickListener(this);
        this.mSaveButton.setOnClickListener(this);
        this.mSaveEdit.setOnClickListener(this);
        this.mResetEdit.setOnClickListener(this);
        this.mAdjustButton.setOnClickListener(this);
        this.mPlayButton.setOnClickListener(this);
        FolmeUtils.touchScaleTint(this.mCancelCapture, this.mStopCapture, this.mGiveUpToPreview, this.mSaveAndShare, this.mResetEdit, this.mAdjustButton, this.mPlayButton);
        FolmeUtils.touchDialogButtonTint(this.mExitDialogConfirm, this.mExitDialogCancel, this.mShareMessage);
        FolmeUtils.touchScale(this.mSaveButton);
        FolmeUtils.touchScale(this.mSaveEdit);
        this.mBottomActionView = (FrameLayout) view.findViewById(R.id.vv_preview_bottom_action);
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mBottomActionView.getLayoutParams();
        marginLayoutParams2.height = Display.getBottomHeight();
        if (Display.fitDisplayFull(1.3333333f)) {
            marginLayoutParams2.bottomMargin = Display.getNavigationBarHeight();
        }
        this.mBottomLayout = (RelativeLayout) view.findViewById(R.id.clone_preview_bottom_parent);
        MarginLayoutParams marginLayoutParams3 = (MarginLayoutParams) this.mBottomLayout.getLayoutParams();
        marginLayoutParams3.height = Math.round(((float) Display.getBottomBarHeight()) * 0.7f);
        marginLayoutParams3.bottomMargin = Display.getBottomMargin();
        marginLayoutParams3.topMargin = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        checkModeValidation();
    }

    /* access modifiers changed from: protected */
    public boolean isVideoMode() {
        return this.mMode == Mode.VIDEO;
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (this.mExitToDummyClone) {
            this.mExitToDummyClone = false;
            resetToPreview();
        } else if (this.mMode == Mode.PHOTO && DataRepository.dataItemGlobal().isFirstUseClonePhoto()) {
            showCloneUseGuide();
            DataRepository.dataItemGlobal().setFirstUseClonePhoto(false);
        } else if (this.mMode != Mode.VIDEO || !DataRepository.dataItemGlobal().isFirstUseCloneVideo()) {
            if (this.mMode == Mode.MCOPY && DataRepository.dataItemGlobal().isFirstUseCloneFreezeFrame()) {
                showCloneUseGuide();
                DataRepository.dataItemGlobal().setFirstUseCloneFreezeFrame(false);
            }
        } else {
            showCloneUseGuide();
            DataRepository.dataItemGlobal().setFirstUseCloneVideo(false);
        }
    }

    public boolean onBackEvent(int i) {
        return hideExitDialog() || hideShareSheet();
    }

    public void onBackPress() {
        if (FenShenCam.sIsEdit) {
            this.mGiveUpToPreview.callOnClick();
        } else {
            showExitConfirm(true);
        }
    }

    public void onClick(View view) {
        String str;
        int visibility = this.mSnapViewProgress.getVisibility();
        String str2 = TAG;
        if (visibility == 0 || this.mShareProgress.getVisibility() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("ignore onClick, progress show ");
            sb.append(this.mSnapViewProgress.getVisibility());
            Log.w(str2, sb.toString());
        } else if (view.getId() != R.id.live_share_item && (this.mShareDialog.getVisibility() == 0 || this.mExitDialog.getVisibility() == 0)) {
            Log.w(str2, "ignore onClick, dialog show");
        } else if (!onClick(view, this.mMode)) {
            int id = view.getId();
            if (id == R.id.clone_give_up_to_preview) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onClick: clone_give_up_to_preview, sIsEdit=");
                sb2.append(FenShenCam.sIsEdit);
                Log.u(str2, sb2.toString());
                if (FenShenCam.sIsEdit) {
                    CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
                    if (cloneAction != null) {
                        cloneAction.onGiveUpEditClicked();
                    }
                    showSaveAndGiveUp(false);
                    CameraStatUtils.trackCloneClick(CloneAttr.VALUE_CANCEL_EDIT_CLICK);
                    return;
                }
                CameraStatUtils.trackCloneClick(CloneAttr.VALUE_GIVEUP_CLICK);
                showExitConfirm(false);
            } else if (id == R.id.clone_stop_capture) {
                Log.u(str2, "onClick: clone_stop_capture");
                if (this.mIsPendingShowComposeResult) {
                    Log.w(str2, "ignore stop capture");
                } else {
                    CameraStatUtils.trackCloneClick(CloneAttr.VALUE_STOP_CAPTURE_CLICK);
                    stopCaptureToPreviewResult(false);
                }
            } else if (id != R.id.live_share_item) {
                switch (id) {
                    case R.id.clone_adjust /*2131296419*/:
                        Log.u(str2, "onClick: clone_adjust");
                        hiddenPlayButton();
                        CloneAction cloneAction2 = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
                        if (cloneAction2 != null) {
                            cloneAction2.onAdjustClicked();
                        }
                        showResetAndSaveEdit();
                        str = CloneAttr.VALUE_ADJUST_CLICK;
                        break;
                    case R.id.clone_cancel_capture /*2131296420*/:
                        Log.u(str2, "onClick: clone_cancel_capture");
                        if (!this.mIsPendingShowComposeResult) {
                            CameraStatUtils.trackCloneClick(CloneAttr.VALUE_CANCEL_CLICK);
                            enableUseGuideMenu(true);
                            CloneAction cloneAction3 = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
                            if (cloneAction3 != null) {
                                cloneAction3.onCancelClicked();
                            }
                            prepare(this.mMode, false);
                            break;
                        } else {
                            Log.w(str2, "ignore cancel capture");
                            return;
                        }
                    default:
                        switch (id) {
                            case R.id.clone_reset_edit /*2131296443*/:
                                Log.u(str2, "onClick: clone_reset_edit");
                                CloneAction cloneAction4 = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
                                if (cloneAction4 != null) {
                                    cloneAction4.onResetEditClicked();
                                }
                                str = CloneAttr.VALUE_RESET_EDIT_CLICK;
                                break;
                            case R.id.clone_resume_play /*2131296444*/:
                                Log.u(str2, "onClick: clone_resume_play");
                                hiddenPlayButton();
                                CloneAction cloneAction5 = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
                                if (cloneAction5 != null) {
                                    cloneAction5.onPlayClicked();
                                    break;
                                }
                                break;
                            case R.id.clone_save_and_share /*2131296445*/:
                                Log.u(str2, "onClick: clone_save_and_share");
                                CameraStatUtils.trackCloneClick(CloneAttr.VALUE_SHARE_CLICK);
                                if (!checkAndShare()) {
                                    this.mPendingShare = true;
                                    onSaveButtonClick(true);
                                    break;
                                }
                                break;
                            case R.id.clone_save_button /*2131296446*/:
                                Log.u(str2, "onClick: clone_save_button");
                                onSaveButtonClick(false);
                                break;
                            case R.id.clone_save_edit /*2131296447*/:
                                Log.u(str2, "onClick: clone_save_edit");
                                if (FenShenCam.sIsEdit) {
                                    CloneAction cloneAction6 = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
                                    if (cloneAction6 != null) {
                                        cloneAction6.onSaveEditClicked();
                                    }
                                    showSaveAndGiveUp(true);
                                    str = CloneAttr.VALUE_SAVE_EDIT_CLICK;
                                    break;
                                } else {
                                    onSaveButtonClick(false);
                                    return;
                                }
                        }
                }
                CameraStatUtils.trackCloneClick(str);
            } else {
                Log.u(str2, "onClick: live_share_item");
                hideShareSheet();
                this.mExitToDummyClone = true;
                ResolveInfo resolveInfo = (ResolveInfo) view.getTag();
                Util.startShareMedia(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name, getContext(), this.mSavedPath, isVideoMode());
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean onClick(View view, Mode mode) {
        return false;
    }

    public void onCountDownFinished() {
    }

    public void onCreate(@Nullable Bundle bundle) {
        Log.d(TAG, ActivityTarget.ACTION_ON_CREATE);
        super.onCreate(bundle);
        this.mGestureDetector = new GestureDetector(getContext(), this.mGestureListener);
        this.mGestureDetector.setIsLongpressEnabled(false);
    }

    public void onDestroy() {
        super.onDestroy();
        ImageView imageView = this.mSnapViewProgress;
        if (imageView != null) {
            imageView.clearAnimation();
        }
    }

    public void onFilmRatioChanged(boolean z, boolean z2) {
    }

    public void onFrameAvailable() {
    }

    public void onPause() {
        String str = TAG;
        Log.d(str, "onPause");
        super.onPause();
        this.mPaused = true;
        hideExitDialog();
        hideShareSheet();
        CloneChooser cloneChooser = (CloneChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(416);
        if (cloneChooser == null || !cloneChooser.isShow()) {
            Mode mode = this.mMode;
            if (mode != null) {
                prepare(mode, false);
            }
            restoreVolumeControlStream();
            return;
        }
        Log.d(str, "skip prepare when show choose dialog");
    }

    public void onPreviewPrepare(ContentValues contentValues) {
        this.mSaveContentValues = contentValues;
    }

    public void onReplayPause() {
    }

    public void onReplayResume() {
    }

    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        this.mPaused = false;
        resumeFragment();
    }

    /* access modifiers changed from: protected */
    public void onSaveButtonClick(boolean z) {
        if (this.mSavedPath != null) {
            resetToPreview();
            return;
        }
        CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
        String str = TAG;
        if (cloneAction == null) {
            Log.w(str, "onSaveButtonClick: no clone action");
            return;
        }
        Log.d(str, "onSaveButtonClick");
        if (!this.mPendingShare) {
            enableUseGuideMenu(false);
        }
        if (z) {
            this.mSaveAndShare.setVisibility(8);
            this.mShareProgress.setVisibility(0);
        } else {
            this.mSaveEdit.setVisibility(8);
            this.mSaveButton.setVisibility(8);
            setProgressBarVisible(true);
        }
        cloneAction.onSaveClicked();
    }

    public void onSaveFinish(Uri uri) {
        StringBuilder sb = new StringBuilder();
        sb.append("onSaveFinish ");
        sb.append(uri);
        Log.d(TAG, sb.toString());
        onSaveFinish(this.mMode, uri);
        if (this.mPendingShare) {
            setProgressBarVisible(false);
            this.mShareProgress.setVisibility(8);
            this.mSaveButton.setVisibility(0);
            this.mSaveAndShare.setVisibility(0);
            showShareSheet();
            return;
        }
        resetToPreview();
    }

    /* access modifiers changed from: protected */
    public void onSaveFinish(Mode mode, Uri uri) {
        String asString;
        if (mode == Mode.PHOTO) {
            asString = MediaProviderUtil.getPathFromUri(getContext(), uri);
        } else {
            ContentValues contentValues = this.mSaveContentValues;
            if (contentValues != null) {
                asString = contentValues.getAsString("_data");
            } else {
                return;
            }
        }
        this.mSavedPath = asString;
    }

    public void onSnapClick() {
        String str = TAG;
        Log.d(str, "onSnapClick");
        if (this.mSnapViewProgress.getVisibility() == 0 || this.mShareProgress.getVisibility() == 0) {
            Log.d(str, "onSnapClick ignore click case 1");
        } else if (this.mExitDialog.getVisibility() == 0) {
            Log.d(str, "onSnapClick ignore click case 2");
        } else {
            Camera camera = (Camera) getContext();
            if (camera != null) {
                Module currentModule = camera.getCurrentModule();
                if (currentModule != null && currentModule.isIgnoreTouchEvent() && !currentModule.isFrameAvailable()) {
                    Log.d(str, "onSnapClick ignore click case 3");
                } else if (this.mSaveButton.getVisibility() == 0) {
                    Log.d(str, "onSnapClick performClick mSaveButton");
                    this.mSaveButton.performClick();
                } else if (this.mIsPendingShowComposeResult) {
                    Log.d(str, "onSnapClick ignore click case 4");
                } else {
                    CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                    if (cameraAction != null) {
                        onSnapClick(cameraAction);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSnapClick(CameraAction cameraAction) {
        if (this.mCurrentMode == 185) {
            cameraAction.onShutterButtonClick(10);
        }
    }

    public void onSnapLongPress() {
    }

    public void onSnapLongPressCancelIn() {
    }

    public void onSnapLongPressCancelOut() {
    }

    public void onSnapPrepare() {
    }

    public void onTrackSnapMissTaken(long j) {
    }

    public void onTrackSnapTaken(long j) {
    }

    public void prepare(Mode mode, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("prepare E ");
        sb.append(mode.toString());
        sb.append(", isLandScape ");
        sb.append(isLandScape());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        this.mSavedPath = null;
        this.mPendingShare = false;
        this.mRootView.setVisibility(0);
        initShutterButton(mode);
        setProgressBarVisible(false);
        this.mShareProgress.setVisibility(8);
        this.mSaveAndShare.setVisibility(8);
        this.mSaveButton.setVisibility(8);
        this.mSaveEdit.setVisibility(8);
        this.mResetEdit.setVisibility(8);
        this.mGiveUpToPreview.setVisibility(8);
        this.mStopCapture.setVisibility(8);
        this.mCancelCapture.setVisibility(8);
        this.mAdjustButton.setVisibility(8);
        hiddenPlayButton();
        if (!z) {
            this.mCaptureHint.setText("");
            this.mCaptureHint.setVisibility(8);
        }
        enableUseGuideMenu(true);
        showBackButton(true);
        this.mIsPendingShowComposeResult = false;
        this.mCaptureHintPined = false;
        if (!isLandScape()) {
            this.mLandscapeHint.setVisibility(0);
            setSnapButtonEnable(false, true);
        } else {
            this.mLandscapeHint.setVisibility(8);
            setSnapButtonEnable(true, true);
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("prepare X ");
        sb3.append(mode.toString());
        Log.d(str, sb3.toString());
        this.mStatus = Status.CAPTURING;
    }

    public void processingFinish() {
        this.mCameraSnapView.triggerAnimation(BottomAnimationConfig.generate(false, this.mCurrentMode, false, false, false).configVariables());
        if (Util.isAccessible() && isAdded()) {
            this.mCameraSnapView.announceForAccessibility(getString(R.string.accessibility_camera_shutter_finish));
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_button));
        }
        showBackButton(true);
    }

    public void processingPrepare() {
        this.mCameraSnapView.prepareRecording(BottomAnimationConfig.generate(false, this.mCurrentMode, true, false, false).configVariables());
        showBackButton(false);
    }

    public void processingStart() {
        Log.d(TAG, "processingResume");
        this.mLandscapeHint.setVisibility(8);
        BottomAnimationConfig configVariables = BottomAnimationConfig.generate(false, this.mCurrentMode, true, false, false).configVariables();
        configVariables.setSpecifiedDuration(getDurationVideoRecording());
        this.mCameraSnapView.triggerAnimation(configVariables);
        if (Util.isAccessible() && isVideoMode()) {
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_process_button));
        }
    }

    /* access modifiers changed from: protected */
    public void provideAnimateElement(int i) {
        if (this.mCurrentMode == 185) {
            if (this.mIsPendingShowComposeResult) {
                this.mIsPendingShowComposeResult = false;
                if (this.mMode != null) {
                    Log.d(TAG, "provideAnimateElement restore ui");
                    prepare(this.mMode, false);
                }
            }
            updateUiOnOrientationChanged();
            return;
        }
        this.mLandscapeHint.setVisibility(8);
        this.mCaptureHint.setVisibility(8);
        this.mSaveButton.setVisibility(8);
        this.mSaveEdit.setVisibility(8);
        this.mResetEdit.setVisibility(8);
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        this.mCurrentMode = i;
        this.mResetType = i2;
        StringBuilder sb = new StringBuilder();
        sb.append("provideAnimateElement mCurrentMode ");
        sb.append(this.mCurrentMode);
        sb.append(", mIsPendingShowComposeResult ");
        sb.append(this.mIsPendingShowComposeResult);
        Log.d(TAG, sb.toString());
        provideAnimateElement(i);
    }

    public void provideOrientationChanged(int i, List list, int i2) {
        super.provideOrientationChanged(i, list, i2);
        provideRotateItem(list, i2);
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null || !cameraAction.isDoingAction()) {
            updateUiOnOrientationChanged();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ignore provideRotateItem newDegree ");
        sb.append(i);
        Log.d(TAG, sb.toString());
    }

    public void quit() {
        Log.d(TAG, "quit");
        this.mRootView.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(418, this);
        registerBackStack(modeCoordinator, this);
    }

    public void resetToPreview() {
        String str = TAG;
        Log.d(str, "resetToPreview");
        resetToPreview(this.mMode);
        if (this.mSnapViewProgress.getVisibility() == 0) {
            setProgressBarVisible(false);
        }
        if (this.mShareProgress.getVisibility() == 0) {
            this.mShareProgress.setVisibility(8);
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null) {
            Log.d(str, "resetToPreview error, action null");
        } else {
            cameraAction.onReviewDoneClicked();
        }
    }

    /* access modifiers changed from: protected */
    public void resetToPreview(Mode mode) {
    }

    /* access modifiers changed from: protected */
    public void resumeFragment() {
        Mode cloneMode = Config.getCloneMode();
        if (cloneMode != null && this.mCurrentMode == 185) {
            prepare(cloneMode, true);
            CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
            if (cloneAction != null) {
                cloneAction.onFragmentResume();
            }
        }
    }

    public void setDetectedPersonInPreview(boolean z) {
        this.mDetectedPersonInPreview = z;
        if (isLandScape()) {
            setSnapButtonEnable(z, true);
        }
    }

    /* access modifiers changed from: protected */
    public void setSnapButtonEnable(boolean z, boolean z2) {
        String str = TAG;
        if (!z || (isLandScape() && this.mDetectedPersonInPreview)) {
            StringBuilder sb = new StringBuilder();
            sb.append("setSnapButtonEnable ");
            sb.append(z);
            Log.d(str, sb.toString());
            CameraSnapView cameraSnapView = this.mCameraSnapView;
            if (cameraSnapView != null) {
                cameraSnapView.setSnapClickEnable(z);
                this.mCameraSnapView.setEnabled(z);
                if (z || z2) {
                    this.mCameraSnapView.setAlpha(z ? 1.0f : 0.5f);
                }
            }
            return;
        }
        Log.w(str, "setSnapButtonEnable ignore, is not landScape");
    }

    /* access modifiers changed from: protected */
    public void showBackButton(boolean z) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert == null) {
            Log.w(TAG, "ignore showBackButton, topAlert is null");
            return;
        }
        if (z) {
            topAlert.enableMenuItem(true, 217);
        } else {
            topAlert.disableMenuItem(true, 217);
        }
    }

    /* access modifiers changed from: protected */
    public void showCaptureHint() {
        if (this.mCaptureHint.getVisibility() != 0) {
            TextView textView = this.mCaptureHint;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, "translationX", new float[]{(float) textView.getHeight(), 0.0f});
            ofFloat.setDuration(600);
            ofFloat.setInterpolator(O00000o.INSTANCE);
            ofFloat.start();
            this.mCaptureHint.setVisibility(0);
        }
    }

    public void showExitConfirm(final boolean z) {
        int i;
        TextView textView;
        if (this.mExitDialog.getVisibility() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("showExitConfirm exitToModeList ");
            sb.append(z);
            Log.u(TAG, sb.toString());
            setSnapButtonEnable(false, false);
            TextView textView2 = this.mExitDialogMessage;
            if (z) {
                textView2.setText(R.string.live_edit_exit_message);
                textView = this.mExitDialogConfirm;
                i = R.string.live_edit_exit_confirm;
            } else {
                textView2.setText(R.string.clone_recapture_alert);
                textView = this.mExitDialogConfirm;
                i = R.string.dialog_ok;
            }
            textView.setText(i);
            this.mExitDialogCancel.setText(R.string.snap_cancel);
            this.mExitDialogConfirm.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Log.u(FragmentCloneProcess.TAG, "showExitConfirm onClick positive");
                    if (z) {
                        FragmentCloneProcess.this.enableUseGuideMenu(true);
                        CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
                        if (cloneAction != null) {
                            cloneAction.onExitClicked();
                        }
                        FragmentCloneProcess.this.resetToPreview();
                    } else {
                        FragmentCloneProcess fragmentCloneProcess = FragmentCloneProcess.this;
                        fragmentCloneProcess.prepare(fragmentCloneProcess.mMode, false);
                        CloneAction cloneAction2 = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
                        if (cloneAction2 != null) {
                            cloneAction2.onGiveUpClicked();
                        }
                    }
                    FragmentCloneProcess.this.hideExitDialog();
                }
            });
            this.mExitDialogCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Log.u(FragmentCloneProcess.TAG, "showExitConfirm onClick negative");
                    FragmentCloneProcess.this.hideExitDialog();
                }
            });
            Completable.create(new AlphaInOnSubscribe(this.mExitDialog)).subscribe();
        }
    }

    public void showPlayButton() {
        ((LayoutParams) this.mPlayButton.getLayoutParams()).topMargin = Display.getTopMargin() + Display.getTopBarHeight() + ((Display.getDisplayRect(1).height() - getResources().getDimensionPixelOffset(R.dimen.bottom_picker_width)) / 2);
        this.mPlayButton.setVisibility(0);
    }

    public void showResetAndSaveEdit() {
        Status status = this.mStatus;
        Status status2 = Status.SAVE;
        String str = TAG;
        if (status != status2) {
            Log.w(str, "showResetAndSaveEdit ignore, not stop");
            return;
        }
        Log.d(str, "showResetAndSaveEdit");
        this.mStatus = Status.EDIT;
        this.mCameraSnapView.hideCirclePaintItem();
        this.mCameraSnapView.hideRoundPaintItem();
        AlphaInOnSubscribe.directSetResult(this.mSaveEdit);
        AlphaInOnSubscribe.directSetResult(this.mResetEdit);
        this.mCaptureHint.setVisibility(0);
        this.mCaptureHint.setText(R.string.clone_drag_to_adjust_the_posture);
        alphaAnimateOut(this.mSaveButton);
        alphaAnimateOut(this.mSaveAndShare);
        this.mAdjustButton.setVisibility(8);
        showBackButton(false);
    }

    /* access modifiers changed from: protected */
    public void showSaveAndGiveUp(Mode mode) {
        showBackButton(true);
    }

    public void showSaveAndGiveUp(boolean z) {
        Status status = this.mStatus;
        Status status2 = Status.STOP;
        String str = TAG;
        if (status == status2 || status == Status.EDIT) {
            StringBuilder sb = new StringBuilder();
            sb.append("showSaveAndGiveUp showSaveButtonAnimation ");
            sb.append(z);
            Log.d(str, sb.toString());
            setVolumeControlStream(3);
            this.mStatus = Status.SAVE;
            this.mCameraSnapView.hideCirclePaintItem();
            this.mCameraSnapView.hideRoundPaintItem();
            if (z) {
                AlphaInOnSubscribe.directSetResult(this.mSaveButton);
                this.mSaveButton.setVisibility(0);
                this.mSaveButton.setScale(0.38f);
                this.mSaveButton.O0000O0o((int) R.raw.vv_save);
                this.mSaveButton.O0000oO();
                this.mSaveEdit.setVisibility(8);
            } else {
                this.mSaveEdit.setVisibility(0);
            }
            if (this.mMode == Mode.MCOPY && FenShenCam.getCurrentSubjectCount() > 0) {
                showAdjustButton();
            }
            this.mStopCapture.setVisibility(8);
            this.mCancelCapture.setVisibility(8);
            if (!this.mCaptureHintPined) {
                this.mCaptureHint.setVisibility(8);
            }
            this.mResetEdit.setVisibility(8);
            setProgressBarVisible(false);
            showSaveAndGiveUp(this.mMode);
            alphaAnimateIn(this.mSaveAndShare);
            alphaAnimateIn(this.mGiveUpToPreview);
            this.mIsPendingShowComposeResult = false;
            setSnapButtonEnable(true, true);
            return;
        }
        Log.w(str, "showSaveAndGiveUp ignore, not stop or edit");
    }

    public void showStopAndCancel() {
        Log.d(TAG, "showFinishAndCancel");
        alphaAnimateIn(this.mStopCapture);
        alphaAnimateIn(this.mCancelCapture);
    }

    public void stopCaptureToPreviewResult(boolean z) {
        CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
        String str = TAG;
        if (cloneAction == null) {
            Log.w(str, "stopCaptureToPreviewResult cloneAction is null");
        } else if (this.mIsPendingShowComposeResult) {
            Log.w(str, "stopCaptureToPreviewResult ignore, pending show composeResult");
        } else if (this.mStatus != Status.CAPTURING) {
            Log.w(str, "stopCaptureToPreviewResult ignore, not capturing");
        } else {
            String str2 = "stopCaptureToPreviewResult";
            Log.d(str2, str2);
            this.mStatus = Status.STOP;
            if (!z && !this.mCaptureHintPined) {
                this.mCaptureHint.setVisibility(8);
            }
            cloneAction.onStopClicked();
            this.mIsPendingShowComposeResult = true;
            updatePreviewUI();
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(418, this);
        unRegisterBackStack(modeCoordinator, this);
    }

    /* access modifiers changed from: protected */
    public void updateCaptureHintBackground(boolean z) {
        this.mCaptureHint.setBackgroundResource(z ? R.drawable.clone_warning_textview_corner_bg : R.drawable.clone_textview_corner_bg);
    }

    public void updateCaptureMessage(int i, boolean z) {
        boolean z2 = this.mPaused;
        String str = TAG;
        if (z2 && i == R.string.clone_offset_is_too_large) {
            Log.w(str, "ignore updateCaptureMessage, paused");
        } else if (this.mMode != Mode.MCOPY || i != R.string.clone_mode_start_hint) {
            if (!isAdded()) {
                Log.w(str, "ignore updateCaptureMessage, fragment not added");
            } else if (isLandScape()) {
                this.mCaptureHint.setTag(Boolean.valueOf(z));
                updateCaptureHintBackground(z);
                this.mCaptureHint.setText(i);
                showCaptureHint();
            } else {
                this.mCaptureHint.setTag(Boolean.valueOf(z));
                this.mCaptureHint.setText(i);
            }
        }
    }

    public void updateCaptureMessage(String str, boolean z) {
    }

    /* access modifiers changed from: protected */
    public void updatePreviewUI() {
        Mode mode = this.mMode;
        if (mode == Mode.VIDEO || mode == Mode.MCOPY) {
            showSaveAndGiveUp(true);
        } else if (mode == Mode.PHOTO) {
            this.mCameraSnapView.hideRoundPaintItem();
            setSnapButtonEnable(false, false);
            setProgressBarVisible(true);
        }
    }
}
