package com.android.camera.fragment.dollyZoom;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.Camera;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.bottom.BottomAnimationConfig;
import com.android.camera.fragment.vv.VVShareAdapter;
import com.android.camera.fragment.vv.page.PageIndicatorView;
import com.android.camera.fragment.vv.page.PagerGridLayoutManager;
import com.android.camera.fragment.vv.page.PagerGridLayoutManager.PageListener;
import com.android.camera.fragment.vv.page.PagerGridSnapHelper;
import com.android.camera.log.Log;
import com.android.camera.module.DollyZoomModule;
import com.android.camera.module.Module;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.DollyZoomAction;
import com.android.camera.protocol.ModeProtocol.DollyZoomProcess;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.FilmAttr;
import com.android.camera.ui.CameraSnapView;
import com.android.camera.ui.CameraSnapView.SnapListener;
import com.android.camera.ui.TextureVideoView;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import io.reactivex.Completable;
import java.util.List;
import miui.extension.target.ActivityTarget;

public class FragmentDollyZoomProcess extends BaseFragment implements OnClickListener, HandleBackTrace, DollyZoomProcess, SnapListener {
    private static final int STATUS_CAPTURING = 1;
    private static final int STATUS_PREPARE = 0;
    private static final int STATUS_SAVE = 3;
    private static final int STATUS_STOP = 2;
    private static final String TAG = "FragmentDollyZoomProcess";
    private ViewGroup mBottomActionView;
    private ViewGroup mBottomLayout;
    private CameraSnapView mCameraSnapView;
    private ImageView mCancelCapture;
    /* access modifiers changed from: private */
    public TextView mCaptureHint;
    private View mExitDialog;
    private TextView mExitDialogCancel;
    private TextView mExitDialogConfirm;
    private TextView mExitDialogMessage;
    private ImageView mGiveUpToPreview;
    private boolean mIsPause;
    private boolean mIsPendingShowComposeResult;
    private boolean mIsPlaying;
    /* access modifiers changed from: private */
    public PageIndicatorView mPageIndicatorView;
    private PagerGridSnapHelper mPagerGridSnapHelper;
    private boolean mPaused;
    private boolean mPendingShare;
    private ImageView mPlayButton;
    private View mRootView;
    private ImageView mSaveAndShare;
    private LottieAnimationView mSaveButton;
    private ContentValues mSaveContentValues;
    private String mSavedPath;
    private VVShareAdapter mShareAdapter;
    private View mShareDialog;
    private TextView mShareMessage;
    private ProgressBar mShareProgress;
    private RecyclerView mShareRecyclerView;
    private ImageView mSnapViewProgress;
    private int mStatus;
    private ImageView mStopCapture;
    private TextureVideoView mTextureVideoView;

    static /* synthetic */ float O0000Oo(float f) {
        if (f == 1.0f) {
            return 1.0f;
        }
        return (float) ((-Math.pow(2.0d, (double) (f * -10.0f))) + 1.0d);
    }

    static /* synthetic */ float O0000OoO(float f) {
        if (f == 1.0f) {
            return 1.0f;
        }
        return (float) ((-Math.pow(2.0d, (double) (f * -10.0f))) + 1.0d);
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

    private void enableUseGuideMenu(boolean z) {
        Log.d(TAG, "enableUseGuideMenu");
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.enableMenuItem(z, 179);
        }
    }

    private void hideCaptureHint() {
        if (this.mCaptureHint.getVisibility() == 0) {
            TextView textView = this.mCaptureHint;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, "translationX", new float[]{0.0f, (float) textView.getHeight()});
            ofFloat.setDuration(600);
            ofFloat.setInterpolator(O00000Oo.INSTANCE);
            ofFloat.addListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    FragmentDollyZoomProcess.this.mCaptureHint.setVisibility(8);
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }
            });
            ofFloat.start();
        }
    }

    private boolean hideExitDialog() {
        Log.d(TAG, "hideExitDialog");
        if (this.mExitDialog.getVisibility() != 0) {
            return false;
        }
        setSnapButtonEnable(true, true);
        this.mExitDialog.setVisibility(8);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean hideShareSheet() {
        Log.d(TAG, "hideShareSheet");
        if (this.mShareDialog.getVisibility() != 0) {
            return false;
        }
        this.mShareDialog.setVisibility(8);
        return true;
    }

    private void initShutterButton() {
        Log.d(TAG, "initShutterButton");
        PaintConditionReferred create = PaintConditionReferred.create(this.mCurrentMode);
        create.setTargetFrameRatio(1.7777777f);
        this.mCameraSnapView.setParameters(create);
        this.mCameraSnapView.showCirclePaintItem();
        this.mCameraSnapView.showRoundPaintItem();
        this.mCameraSnapView.stopSpeech();
    }

    private void onSaveButtonClick(boolean z) {
        if (this.mSavedPath != null) {
            resetToPreview(true);
            return;
        }
        DollyZoomAction dollyZoomAction = (DollyZoomAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(677);
        String str = TAG;
        if (dollyZoomAction == null) {
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
            this.mSaveButton.setVisibility(8);
            setProgressBarVisible(true);
        }
        dollyZoomAction.onSaveClicked();
    }

    private void pauseVideoPlay() {
        if (this.mIsPlaying) {
            this.mIsPlaying = false;
            this.mTextureVideoView.pause();
            this.mPlayButton.setVisibility(0);
        }
    }

    private void resumeVideoPlay() {
        if (!this.mIsPlaying) {
            this.mIsPlaying = true;
            this.mTextureVideoView.setClearSurface(false);
            String str = this.mSavedPath;
            if (str != null) {
                this.mTextureVideoView.setVideoPath(str);
            }
            this.mTextureVideoView.resume();
            this.mPlayButton.setVisibility(8);
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

    private void setSnapButtonEnable(boolean z, boolean z2) {
        StringBuilder sb = new StringBuilder();
        sb.append("setSnapButtonEnable ");
        sb.append(z);
        Log.d(TAG, sb.toString());
        CameraSnapView cameraSnapView = this.mCameraSnapView;
        if (cameraSnapView != null) {
            cameraSnapView.setSnapClickEnable(z);
            this.mCameraSnapView.setEnabled(z);
            if (z || z2) {
                this.mCameraSnapView.setAlpha(z ? 1.0f : 0.5f);
            }
        }
    }

    private void showCaptureHint() {
        if (this.mCaptureHint.getVisibility() != 0) {
            TextView textView = this.mCaptureHint;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, "translationX", new float[]{(float) textView.getHeight(), 0.0f});
            ofFloat.setDuration(600);
            ofFloat.setInterpolator(O000000o.INSTANCE);
            ofFloat.start();
            this.mCaptureHint.setVisibility(0);
        }
    }

    private void showExitConfirm() {
        if (this.mExitDialog.getVisibility() != 0) {
            Log.d(TAG, "showExitConfirm");
            setSnapButtonEnable(false, false);
            this.mExitDialogMessage.setText(R.string.live_edit_exit_message);
            this.mExitDialogConfirm.setText(R.string.live_edit_exit_confirm);
            this.mExitDialogCancel.setText(R.string.snap_cancel);
            this.mExitDialogConfirm.setOnClickListener(new O00000o0(this));
            this.mExitDialogCancel.setOnClickListener(new O00000o(this));
            Completable.create(new AlphaInOnSubscribe(this.mExitDialog)).subscribe();
        }
    }

    private void showShareSheet() {
        this.mPendingShare = false;
        if (!this.mPaused) {
            Intent shareMediaIntent = Util.getShareMediaIntent(getContext(), this.mSavedPath, true);
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
                        FragmentDollyZoomProcess.this.mPageIndicatorView.setSelectedPage(i);
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
                        CameraStatUtils.trackDollyZoomClick(FilmAttr.VALUE_DOLLY_ZOOM_CLICK_PLAY_CANCEL);
                        FragmentDollyZoomProcess.this.hideShareSheet();
                    }
                });
            } else {
                this.mShareAdapter.setShareInfoList(queryIntentActivities);
                this.mShareAdapter.notifyDataSetChanged();
            }
            Completable.create(new AlphaInOnSubscribe(this.mShareDialog)).subscribe();
        }
    }

    private void startVideoPlay(String str) {
        if (!this.mIsPlaying) {
            this.mIsPlaying = true;
            this.mIsPause = false;
            this.mTextureVideoView.setClearSurface(true);
            this.mTextureVideoView.setVideoPath(str);
            this.mTextureVideoView.setVisibility(0);
            this.mTextureVideoView.start(0);
        }
    }

    private void stopVideoPlay() {
        if (this.mIsPlaying || this.mTextureVideoView.isPlaying()) {
            this.mIsPlaying = false;
            this.mTextureVideoView.stop();
            this.mTextureVideoView.setVisibility(4);
        }
    }

    public /* synthetic */ void O0000Oo(View view) {
        CameraStatUtils.trackDollyZoomClick(FilmAttr.VALUE_DOLLY_ZOOM_CLICK_EXIT_CONFIRM);
        enableUseGuideMenu(true);
        DollyZoomAction dollyZoomAction = (DollyZoomAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(677);
        if (dollyZoomAction != null) {
            dollyZoomAction.onExitClicked();
        }
        resetToPreview(true);
        hideExitDialog();
    }

    public /* synthetic */ void O0000OoO(View view) {
        hideExitDialog();
    }

    public boolean canSnap() {
        if (this.mStatus != 3) {
            CameraSnapView cameraSnapView = this.mCameraSnapView;
            if (cameraSnapView != null && cameraSnapView.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_DOLLY_ZOOM_PROCESS;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_dolly_zoom_process;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Log.d(TAG, "initView");
        this.mRootView = view;
        this.mSnapViewProgress = (ImageView) view.findViewById(R.id.dolly_zoom_snap_view_progress);
        this.mShareProgress = (ProgressBar) view.findViewById(R.id.dolly_zoom_share_progress);
        this.mCameraSnapView = (CameraSnapView) view.findViewById(R.id.dolly_zoom_shutter_button);
        this.mCameraSnapView.setSnapListener(this);
        this.mGiveUpToPreview = (ImageView) view.findViewById(R.id.dolly_zoom_give_up_to_preview);
        this.mSaveAndShare = (ImageView) view.findViewById(R.id.dolly_zoom_save_and_share);
        this.mStopCapture = (ImageView) view.findViewById(R.id.dolly_zoom_stop_capture);
        this.mCancelCapture = (ImageView) view.findViewById(R.id.dolly_zoom_cancel_capture);
        this.mSaveButton = (LottieAnimationView) view.findViewById(R.id.dolly_zoom_save_button);
        this.mCaptureHint = (TextView) view.findViewById(R.id.dolly_zoom_capture_hint);
        this.mExitDialog = view.findViewById(R.id.vv_dialog);
        this.mExitDialogMessage = (TextView) this.mExitDialog.findViewById(R.id.vv_dialog_message);
        this.mExitDialogConfirm = (TextView) this.mExitDialog.findViewById(R.id.vv_dialog_positive);
        this.mExitDialogCancel = (TextView) this.mExitDialog.findViewById(R.id.vv_dialog_negative);
        this.mShareDialog = view.findViewById(R.id.vv_share);
        this.mShareMessage = (TextView) this.mShareDialog.findViewById(R.id.vv_share_message);
        this.mShareRecyclerView = (RecyclerView) this.mShareDialog.findViewById(R.id.vv_share_recyclerview);
        this.mShareRecyclerView.setFocusable(false);
        this.mPageIndicatorView = (PageIndicatorView) this.mShareDialog.findViewById(R.id.vv_share_recyclerview_indicator);
        this.mPlayButton = (ImageView) view.findViewById(R.id.dolly_zoom_resume_play);
        this.mTextureVideoView = (TextureVideoView) view.findViewById(R.id.dolly_zoom_preview_texture_video);
        this.mTextureVideoView.setVisibility(4);
        this.mTextureVideoView.setOnClickListener(this);
        this.mTextureVideoView.setLoop(true);
        Rect displayRect = Util.getDisplayRect();
        int i = displayRect.top;
        int windowHeight = (i - ((Display.getWindowHeight() - i) - displayRect.height())) / 2;
        ViewCompat.setRotation(this.mCameraSnapView, 90.0f);
        ViewCompat.setRotation(this.mGiveUpToPreview, 90.0f);
        ViewCompat.setRotation(this.mSaveAndShare, 90.0f);
        ViewCompat.setRotation(this.mSaveButton, 90.0f);
        ViewCompat.setRotation(this.mStopCapture, 90.0f);
        ViewCompat.setRotation(this.mCancelCapture, 90.0f);
        ViewCompat.setRotation(this.mCaptureHint, 90.0f);
        ViewCompat.setRotation(this.mExitDialog, 90.0f);
        ViewCompat.setRotation(this.mShareDialog, 90.0f);
        ViewCompat.setRotation(this.mPlayButton, 90.0f);
        this.mCameraSnapView.setOnClickListener(this);
        this.mGiveUpToPreview.setOnClickListener(this);
        this.mSaveAndShare.setOnClickListener(this);
        this.mCancelCapture.setOnClickListener(this);
        this.mStopCapture.setOnClickListener(this);
        this.mSaveButton.setOnClickListener(this);
        this.mPlayButton.setOnClickListener(this);
        FolmeUtils.touchScaleTint(this.mCancelCapture, this.mStopCapture, this.mGiveUpToPreview, this.mSaveAndShare, this.mPlayButton);
        FolmeUtils.touchDialogButtonTint(this.mExitDialogConfirm, this.mExitDialogCancel, this.mShareMessage);
        FolmeUtils.touchScale(this.mSaveButton);
        this.mBottomActionView = (FrameLayout) view.findViewById(R.id.vv_preview_bottom_action);
        ((MarginLayoutParams) this.mBottomActionView.getLayoutParams()).height = Display.getBottomHeight();
        this.mBottomLayout = (RelativeLayout) view.findViewById(R.id.dolly_zoom_preview_bottom_parent);
        ((MarginLayoutParams) this.mBottomLayout.getLayoutParams()).bottomMargin = Display.getBottomMargin();
        if (this.mCurrentMode != 189) {
            quit();
        }
    }

    public boolean onBackEvent(int i) {
        return hideExitDialog() || hideShareSheet();
    }

    public void onBackPressed() {
        StringBuilder sb = new StringBuilder();
        sb.append("onBackPressed mStatus:");
        sb.append(this.mStatus);
        Log.d(TAG, sb.toString());
        int i = this.mStatus;
        if (i == 0) {
            CameraStatUtils.trackDollyZoomClick(FilmAttr.VALUE_DOLLY_ZOOM_CLICK_EXIT_PREVIEW);
            resetToPreview(true);
        } else if (i == 3) {
            showExitConfirm();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x008d, code lost:
        r4.mIsPause = !r4.mIsPause;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00b5, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        String sb;
        int visibility = this.mSnapViewProgress.getVisibility();
        String str = TAG;
        if (visibility == 0 || this.mShareProgress.getVisibility() == 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ignore onClick, progress show ");
            sb2.append(this.mSnapViewProgress.getVisibility());
            sb = sb2.toString();
        } else if (view.getId() == R.id.live_share_item || !(this.mShareDialog.getVisibility() == 0 || this.mExitDialog.getVisibility() == 0)) {
            switch (view.getId()) {
                case R.id.dolly_zoom_cancel_capture /*2131296493*/:
                    if (!this.mIsPendingShowComposeResult) {
                        enableUseGuideMenu(true);
                        DollyZoomAction dollyZoomAction = (DollyZoomAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(677);
                        if (dollyZoomAction != null) {
                            dollyZoomAction.onCancelClicked();
                        }
                        prepare(false);
                        break;
                    } else {
                        sb = "ignore cancel capture";
                        break;
                    }
                case R.id.dolly_zoom_give_up_to_preview /*2131296495*/:
                    showExitConfirm();
                    break;
                case R.id.dolly_zoom_preview_texture_video /*2131296497*/:
                    if (!this.mIsPause) {
                        pauseVideoPlay();
                        break;
                    }
                    break;
                case R.id.dolly_zoom_resume_play /*2131296498*/:
                    if (this.mIsPause) {
                        resumeVideoPlay();
                        break;
                    }
                    break;
                case R.id.dolly_zoom_save_and_share /*2131296499*/:
                    CameraStatUtils.trackDollyZoomClick(FilmAttr.VALUE_DOLLY_ZOOM_CLICK_PLAY_SHARE);
                    if (!checkAndShare()) {
                        this.mPendingShare = true;
                        onSaveButtonClick(true);
                        break;
                    }
                    break;
                case R.id.dolly_zoom_save_button /*2131296500*/:
                    CameraStatUtils.trackDollyZoomClick(FilmAttr.VALUE_DOLLY_ZOOM_CLICK_PLAY_SAVE);
                    onSaveButtonClick(false);
                    break;
                case R.id.dolly_zoom_stop_capture /*2131296504*/:
                    if (!this.mIsPendingShowComposeResult) {
                        stopCaptureToPreviewResult(false);
                        break;
                    } else {
                        sb = "ignore stop capture";
                        break;
                    }
                case R.id.live_share_item /*2131296726*/:
                    hideShareSheet();
                    ResolveInfo resolveInfo = (ResolveInfo) view.getTag();
                    Util.startShareMedia(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name, getContext(), this.mSavedPath, true);
                    break;
            }
        } else {
            sb = "ignore onClick, dialog show";
        }
        Log.w(str, sb);
    }

    public void onCreate(@Nullable Bundle bundle) {
        Log.d(TAG, ActivityTarget.ACTION_ON_CREATE);
        super.onCreate(bundle);
    }

    public void onDestroy() {
        super.onDestroy();
        ImageView imageView = this.mSnapViewProgress;
        if (imageView != null) {
            imageView.clearAnimation();
        }
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        this.mPaused = true;
        hideExitDialog();
        hideShareSheet();
    }

    public void onPreviewPrepare(ContentValues contentValues) {
        this.mSaveContentValues = contentValues;
    }

    public void onResume() {
        StringBuilder sb = new StringBuilder();
        sb.append("onResume mStatus:");
        sb.append(this.mStatus);
        Log.d(TAG, sb.toString());
        super.onResume();
        this.mPaused = false;
        if (this.mStatus != 3) {
            prepare(true);
            DollyZoomAction dollyZoomAction = (DollyZoomAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(677);
            if (dollyZoomAction != null) {
                dollyZoomAction.onFragmentResume();
            }
        }
    }

    public void onSaveFinish(Uri uri) {
        StringBuilder sb = new StringBuilder();
        sb.append("onSaveFinish ");
        sb.append(uri);
        Log.d(TAG, sb.toString());
        ContentValues contentValues = this.mSaveContentValues;
        if (contentValues != null) {
            this.mSavedPath = contentValues.getAsString("_data");
        }
        if (this.mPendingShare) {
            this.mShareProgress.setVisibility(8);
            this.mSaveButton.setVisibility(0);
            this.mSaveAndShare.setVisibility(0);
            showShareSheet();
            return;
        }
        resetToPreview(true);
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
                    if (cameraAction != null && this.mCurrentMode == 189) {
                        cameraAction.onShutterButtonClick(10);
                    }
                }
            }
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

    public void prepare(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("prepare E isLandScape ");
        sb.append(isLandScape());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        this.mSavedPath = null;
        this.mPendingShare = false;
        this.mRootView.setVisibility(0);
        this.mCurrentMode = 189;
        initShutterButton();
        setProgressBarVisible(false);
        this.mShareProgress.setVisibility(8);
        this.mSaveAndShare.setVisibility(8);
        this.mSaveButton.setVisibility(8);
        this.mGiveUpToPreview.setVisibility(8);
        this.mStopCapture.setVisibility(8);
        this.mCancelCapture.setVisibility(8);
        this.mPlayButton.setVisibility(8);
        if (!z) {
            this.mCaptureHint.setText("");
            this.mCaptureHint.setVisibility(8);
        } else {
            this.mCaptureHint.setText(getResources().getString(R.string.dolly_zoom_capture_tip1, new Object[]{Integer.valueOf(1)}));
            showCaptureHint();
        }
        enableUseGuideMenu(true);
        this.mIsPendingShowComposeResult = false;
        setSnapButtonEnable(true, true);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.enableMenuItem(true, 179);
            topAlert.enableMenuItem(true, 251);
            topAlert.enableMenuItem(true, 217);
        }
        stopVideoPlay();
        this.mStatus = 0;
        Log.d(str, "prepare X ");
    }

    public void processingFinish() {
        this.mCameraSnapView.triggerAnimation(BottomAnimationConfig.generate(false, this.mCurrentMode, false, false, false).configVariables());
        if (Util.isAccessible() && isAdded()) {
            this.mCameraSnapView.announceForAccessibility(getString(R.string.accessibility_camera_shutter_finish));
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_button));
        }
    }

    public void processingPrepare() {
        Log.d(TAG, "processingPrepare");
        this.mCameraSnapView.prepareRecording(BottomAnimationConfig.generate(false, this.mCurrentMode, true, false, false).configVariables());
    }

    public void processingStart() {
        TextureVideoView textureVideoView;
        String str = TAG;
        Log.d(str, "processingStart");
        if (this.mStatus != 0) {
            Log.w(str, "processingStart failed because current status not STATUS_PREPARE");
        }
        this.mCaptureHint.setVisibility(8);
        if (Util.isAccessible()) {
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_process_button));
        }
        this.mCameraSnapView.setSpecificProgress(0);
        Camera camera = (Camera) getContext();
        if (camera != null) {
            Rect displayRect = Util.getDisplayRect();
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mTextureVideoView.getLayoutParams();
            int orientation = camera.getOrientation();
            if (orientation % 180 != 0) {
                marginLayoutParams.width = displayRect.height();
                marginLayoutParams.height = displayRect.width();
                marginLayoutParams.topMargin = displayRect.top + ((marginLayoutParams.width - marginLayoutParams.height) / 2);
                marginLayoutParams.leftMargin = (marginLayoutParams.height - marginLayoutParams.width) / 2;
                this.mTextureVideoView.setLayoutParams(marginLayoutParams);
                textureVideoView = this.mTextureVideoView;
                orientation = (orientation + 180) % m.cQ;
            } else {
                marginLayoutParams.width = displayRect.width();
                marginLayoutParams.height = displayRect.height();
                marginLayoutParams.topMargin = displayRect.top;
                marginLayoutParams.leftMargin = displayRect.left;
                this.mTextureVideoView.setLayoutParams(marginLayoutParams);
                textureVideoView = this.mTextureVideoView;
            }
            ViewCompat.setRotation(textureVideoView, (float) orientation);
        }
        this.mStatus = 1;
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
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (this.mCurrentMode != 189) {
            this.mCaptureHint.setVisibility(8);
            this.mSaveButton.setVisibility(8);
        } else if (this.mIsPendingShowComposeResult) {
            this.mIsPendingShowComposeResult = false;
            Log.d(str, "provideAnimateElement restore ui");
            prepare(false);
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null && cameraAction.isDoingAction()) {
            StringBuilder sb = new StringBuilder();
            sb.append("ignore provideRotateItem newDegree ");
            sb.append(i);
            Log.d(TAG, sb.toString());
        }
    }

    public void quit() {
        Log.d(TAG, "quit");
        this.mRootView.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(676, this);
        registerBackStack(modeCoordinator, this);
    }

    public void resetToPreview(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("resetToPreview toModeSelect ");
        sb.append(z);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (this.mShareProgress.getVisibility() == 0) {
            this.mShareProgress.setVisibility(8);
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null) {
            Log.d(str, "resetToPreview error, action null");
            return;
        }
        if (z) {
            cameraAction.onReviewDoneClicked();
        } else {
            cameraAction.onReviewCancelClicked();
        }
    }

    public void showSaveAndGiveUp() {
        boolean z = this.mIsPendingShowComposeResult;
        String str = TAG;
        if (!z) {
            Log.w(str, "ignore showSaveAndGiveUp");
        } else if (this.mStatus != 2) {
            Log.w(str, "showSaveAndGiveUp ignore, not stop");
        } else {
            Log.d(str, "showSaveAndGiveUp");
            this.mStatus = 3;
            this.mCameraSnapView.hideCirclePaintItem();
            this.mCameraSnapView.hideRoundPaintItem();
            AlphaInOnSubscribe.directSetResult(this.mSaveButton);
            this.mSaveButton.setScale(0.38f);
            this.mSaveButton.O0000O0o((int) R.raw.vv_save);
            this.mSaveButton.O0000oO();
            this.mStopCapture.setVisibility(8);
            this.mCancelCapture.setVisibility(8);
            this.mCaptureHint.setVisibility(8);
            setProgressBarVisible(false);
            alphaAnimateIn(this.mSaveAndShare);
            alphaAnimateIn(this.mGiveUpToPreview);
            this.mIsPendingShowComposeResult = false;
            setSnapButtonEnable(true, true);
            startVideoPlay(DollyZoomModule.TEMP_VIDEO_PATH);
        }
    }

    public void stopCaptureToPreviewResult(boolean z) {
        DollyZoomAction dollyZoomAction = (DollyZoomAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(677);
        String str = TAG;
        if (dollyZoomAction == null) {
            Log.w(str, "stopCaptureToPreviewResult dollyZoomAction is null");
        } else if (this.mIsPendingShowComposeResult) {
            Log.w(str, "stopCaptureToPreviewResult ignore, pending show composeResult");
        } else if (this.mStatus != 1) {
            Log.w(str, "stopCaptureToPreviewResult ignore, not capturing");
        } else {
            String str2 = "stopCaptureToPreviewResult";
            Log.d(str2, str2);
            this.mStatus = 2;
            if (!z) {
                this.mCaptureHint.setVisibility(8);
            }
            dollyZoomAction.onStopClicked();
            this.mIsPendingShowComposeResult = true;
            showSaveAndGiveUp();
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(676, this);
        unRegisterBackStack(modeCoordinator, this);
    }

    public void updateCaptureMessage(int i, boolean z) {
        TextView textView;
        int i2;
        if (this.mStatus != 3) {
            if (!isAdded()) {
                Log.w(TAG, "ignore updateCaptureMessage, fragment not added");
            } else if (i == -1) {
                hideCaptureHint();
            } else {
                if (z) {
                    textView = this.mCaptureHint;
                    i2 = R.drawable.clone_warning_textview_corner_bg;
                } else {
                    textView = this.mCaptureHint;
                    i2 = R.drawable.clone_textview_corner_bg;
                }
                textView.setBackgroundResource(i2);
                if (i == R.string.dolly_zoom_capture_tip1) {
                    this.mCaptureHint.setText(getResources().getString(R.string.dolly_zoom_capture_tip1, new Object[]{Integer.valueOf(1)}));
                } else {
                    this.mCaptureHint.setText(i);
                }
                showCaptureHint();
            }
        }
    }

    public void updateZoomRatioHint(float f) {
        this.mCameraSnapView.setSpecificProgress((int) (((f - 1.0f) / 3.0f) * 100.0f));
    }
}
