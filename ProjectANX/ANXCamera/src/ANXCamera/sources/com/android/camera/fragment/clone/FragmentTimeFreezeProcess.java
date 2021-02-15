package com.android.camera.fragment.clone;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout.LayoutParams;
import androidx.core.view.ViewCompat;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.bottom.BottomAnimationConfig;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CloneAction;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.FilmAttr;
import com.android.camera.ui.CameraSnapView;
import com.android.camera.ui.ScrollTextview;
import com.android.camera.ui.TextureVideoView;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import com.xiaomi.fenshen.FenShenCam.Mode;

public class FragmentTimeFreezeProcess extends FragmentCloneProcess {
    private static final String TAG = "FragmentTimeFreezeProcess";
    private boolean mGotoShare = false;
    private boolean mInRecording = false;
    private boolean mIsPause;
    private boolean mIsPlaying;
    private TextureVideoView mTextureVideoView;
    private ScrollTextview mTimeFreezeButton;
    private boolean mTimeFreezing;

    private void hideCaptureHint() {
        if (this.mCaptureHint.getVisibility() == 0) {
            this.mCaptureHint.setVisibility(4);
        }
    }

    private void onTimeFreezeClick() {
        boolean z;
        CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
        String str = TAG;
        if (cloneAction == null) {
            Log.w(str, "onTimeFreezeClicked: no clone action");
            return;
        }
        Log.d(str, "onTimeFreezeClicked");
        if (this.mTimeFreezing) {
            this.mTimeFreezeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_vector_timefreeze, null), null, null, null);
            this.mTimeFreezeButton.setText(R.string.film_timefreeze);
            z = false;
        } else {
            this.mTimeFreezeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_vector_timefreeze_reset, null), null, null, null);
            this.mTimeFreezeButton.setText(R.string.film_timefreeze_reset);
            z = true;
        }
        this.mTimeFreezing = z;
        cloneAction.onTimeFreezeClicked();
    }

    private void pauseVideoPlay() {
        if (this.mIsPlaying) {
            this.mIsPlaying = false;
            this.mTextureVideoView.pause();
            showPlayButton();
        }
    }

    private void resetTimeFreezeButton() {
        if (this.mTimeFreezing) {
            this.mTimeFreezeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_vector_timefreeze, null), null, null, null);
            this.mTimeFreezeButton.setText(R.string.film_timefreeze);
            this.mTimeFreezing = false;
        }
    }

    private void resumeVideoPlay() {
        if (!this.mIsPlaying) {
            this.mIsPlaying = true;
            this.mTextureVideoView.setClearSurface(false);
            this.mTextureVideoView.resume();
            hiddenPlayButton();
        }
    }

    /* access modifiers changed from: private */
    public void showTimeFreezeButton() {
        onFilmRatioChanged(DataRepository.dataItemLive().getTimeFreezeFilmRatioEnabled(), false);
    }

    private void startVideoPlay(String str) {
        if (!this.mIsPlaying) {
            this.mIsPlaying = true;
            this.mIsPause = false;
            this.mTextureVideoView.setClearSurface(true);
            this.mTextureVideoView.setVideoPath(str);
            this.mTextureVideoView.setVisibility(0);
            this.mTextureVideoView.start(0);
            hiddenPlayButton();
        }
    }

    private void stopVideoPlay() {
        if (this.mIsPlaying || this.mTextureVideoView.isPlaying()) {
            this.mIsPlaying = false;
            this.mTextureVideoView.stop();
            this.mTextureVideoView.setVisibility(4);
        }
    }

    public /* synthetic */ void O00000Oo(int i, boolean z) {
        boolean z2 = this.mPaused;
        String str = TAG;
        if (z2 && i == R.string.clone_offset_is_too_large) {
            Log.w(str, "ignore updateCaptureMessage, paused");
        } else if (!isAdded()) {
            Log.w(str, "ignore updateCaptureMessage, fragment not added");
        } else if (this.mCaptureHintPined) {
            Log.w(str, "ignore updateCaptureMessage, hint has been pined");
        } else {
            if (i == R.string.clone_too_much_movement_stop) {
                this.mCaptureHintPined = true;
                this.mCaptureHint.postDelayed(new Runnable() {
                    public void run() {
                        FragmentTimeFreezeProcess fragmentTimeFreezeProcess = FragmentTimeFreezeProcess.this;
                        fragmentTimeFreezeProcess.mCaptureHintPined = false;
                        fragmentTimeFreezeProcess.updateCaptureMessage(-1, false);
                    }
                }, 2000);
            }
            if (i == -1) {
                hideCaptureHint();
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

    /* access modifiers changed from: protected */
    public void checkModeValidation() {
        if (this.mCurrentMode != 213) {
            quit();
        }
    }

    /* access modifiers changed from: protected */
    public void enableUseGuideMenu(boolean z) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.enableMenuItem(z, 164);
            topAlert.enableMenuItem(z, 251);
        }
    }

    /* access modifiers changed from: protected */
    public int getDurationVideoRecording() {
        return 15000;
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_FILM_TIME_FREEZE;
    }

    /* access modifiers changed from: protected */
    public boolean hideShareSheet() {
        boolean hideShareSheet = super.hideShareSheet();
        if (hideShareSheet) {
            CameraStatUtils.trackFilmTimeFreezeClick(FilmAttr.VALUE_TIME_FREEZE_CLICK_PLAY_SHARE_CANCEL);
        }
        return hideShareSheet;
    }

    /* access modifiers changed from: protected */
    public void initShutterButton(Mode mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("initShutterButton ");
        sb.append(mode);
        Log.d(TAG, sb.toString());
        this.mCurrentMode = 213;
        this.mMode = mode;
        if (mode == Mode.TIMEFREEZE) {
            PaintConditionReferred create = PaintConditionReferred.create(this.mCurrentMode);
            create.setTargetFrameRatio(1.7777777f);
            this.mCameraSnapView.setParameters(create);
            this.mCameraSnapView.showCirclePaintItem();
            this.mCameraSnapView.showRoundPaintItem();
            this.mCameraSnapView.stopSpeech();
        }
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mTimeFreezeButton = (ScrollTextview) view.findViewById(R.id.clone_time_freeze_button);
        this.mTextureVideoView = (TextureVideoView) view.findViewById(R.id.clone_texture_video);
        this.mTextureVideoView.setVisibility(4);
        this.mTextureVideoView.setOnClickListener(this);
        this.mTextureVideoView.setLoop(true);
        super.initView(view);
        ViewCompat.setRotation(this.mTimeFreezeButton, 90.0f);
        FolmeUtils.touchScaleTint(this.mTimeFreezeButton);
        this.mTimeFreezeButton.setOnClickListener(this);
        this.mLandscapeHint.setText(R.string.film_landscape_timefreeze_hint);
    }

    /* access modifiers changed from: protected */
    public boolean isVideoMode() {
        return this.mMode == Mode.TIMEFREEZE;
    }

    public void onBackPress() {
        enableUseGuideMenu(true);
        CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
        if (cloneAction != null) {
            cloneAction.onExitClicked();
        }
        resetToPreview();
    }

    /* access modifiers changed from: protected */
    public boolean onClick(View view, Mode mode) {
        switch (view.getId()) {
            case R.id.clone_give_up_to_preview /*2131296426*/:
                showExitConfirm(true);
                return true;
            case R.id.clone_save_button /*2131296446*/:
                CameraStatUtils.trackFilmTimeFreezeClick(FilmAttr.VALUE_TIME_FREEZE_CLICK_PLAY_SAVE);
                onSaveButtonClick(false);
                break;
            case R.id.clone_texture_video /*2131296453*/:
                if (this.mIsPause) {
                    resumeVideoPlay();
                } else {
                    pauseVideoPlay();
                }
                this.mIsPause = !this.mIsPause;
                return true;
            case R.id.clone_time_freeze_button /*2131296454*/:
                onTimeFreezeClick();
                CameraStatUtils.trackFilmTimeFreezeClick(this.mTimeFreezing ? FilmAttr.VALUE_TIME_FREEZE_CLICK_RESET : FilmAttr.VALUE_TIME_FREEZE_CLICK_FREEZE);
                return true;
            case R.id.live_share_item /*2131296726*/:
                CameraStatUtils.trackFilmTimeFreezeClick(FilmAttr.VALUE_TIME_FREEZE_CLICK_PLAY_SHARE);
                hideShareSheet();
                this.mExitToDummyClone = false;
                this.mGotoShare = true;
                this.mStatus = Status.SHARE;
                ResolveInfo resolveInfo = (ResolveInfo) view.getTag();
                boolean isVideoMode = isVideoMode();
                showBackButton(false);
                Util.startShareMedia(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name, getContext(), this.mSavedPath, isVideoMode);
                return true;
        }
        return false;
    }

    public void onCountDownFinished() {
        onTimeFreezeClick();
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onFilmRatioChanged(boolean z, boolean z2) {
        ScrollTextview scrollTextview;
        int i;
        Rect displayRect = Util.getDisplayRect();
        Context androidContext = CameraAppImpl.getAndroidContext();
        int dimensionPixelOffset = androidContext.getResources().getDimensionPixelOffset(R.dimen.clone_adjust_width);
        int dimensionPixelOffset2 = androidContext.getResources().getDimensionPixelOffset(R.dimen.clone_adjust_height);
        int dimensionPixelOffset3 = androidContext.getResources().getDimensionPixelOffset(R.dimen.clone_adjust_margin_left);
        int i2 = dimensionPixelOffset2 / 2;
        int i3 = (dimensionPixelOffset3 + i2) - (dimensionPixelOffset / 2);
        int height = (displayRect.top + (displayRect.height() / 2)) - i2;
        if (z) {
            i3 += dimensionPixelOffset3;
        }
        LayoutParams layoutParams = (LayoutParams) this.mTimeFreezeButton.getLayoutParams();
        layoutParams.leftMargin = i3;
        layoutParams.topMargin = height;
        LayoutParams layoutParams2 = (LayoutParams) this.mLandscapeHint.getLayoutParams();
        int dimensionPixelOffset4 = androidContext.getResources().getDimensionPixelOffset(R.dimen.vv_hint_text_size);
        layoutParams2.width = Util.getScreenWidth(androidContext);
        this.mLandscapeHint.setGravity(1);
        layoutParams2.gravity = 1;
        layoutParams2.topMargin = (displayRect.top + (displayRect.height() / 2)) - (dimensionPixelOffset4 / 2);
        if (z2) {
            scrollTextview = this.mTimeFreezeButton;
            i = 8;
        } else {
            if (!this.mGotoShare) {
                scrollTextview = this.mTimeFreezeButton;
                i = 0;
            }
            if (!this.mTimeFreezing) {
                onTimeFreezeClick();
                return;
            }
            return;
        }
        scrollTextview.setVisibility(i);
        if (!this.mTimeFreezing) {
        }
    }

    public void onFrameAvailable() {
        this.mTimeFreezeButton.post(new Runnable() {
            public void run() {
                FragmentTimeFreezeProcess.this.showTimeFreezeButton();
            }
        });
    }

    public void onPause() {
        super.onPause();
        if (this.mTimeFreezing) {
            onTimeFreezeClick();
        }
    }

    public void onReplayPause() {
        showPlayButton();
    }

    public void onReplayResume() {
        hiddenPlayButton();
    }

    /* access modifiers changed from: protected */
    public void onSaveFinish(Mode mode, Uri uri) {
        resetTimeFreezeButton();
        ContentValues contentValues = this.mSaveContentValues;
        if (contentValues != null) {
            this.mSavedPath = contentValues.getAsString("_data");
        }
    }

    /* access modifiers changed from: protected */
    public void onSnapClick(CameraAction cameraAction) {
        if (this.mInRecording) {
            CameraStatUtils.trackFilmTimeFreezeClick(FilmAttr.VALUE_TIME_FREEZE_CLICK_STOP_RECORD);
        }
        cameraAction.onShutterButtonClick(10);
    }

    public void prepare(Mode mode, boolean z) {
        if (!this.mGotoShare) {
            super.prepare(mode, z);
            hiddenPlayButton();
            stopVideoPlay();
        }
    }

    public void processingFinish() {
        this.mInRecording = false;
        this.mCameraSnapView.triggerAnimation(BottomAnimationConfig.generate(false, this.mCurrentMode, false, false, false).configVariables());
        if (Util.isAccessible() && isAdded()) {
            this.mCameraSnapView.announceForAccessibility(getString(R.string.accessibility_camera_shutter_finish));
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_button));
        }
    }

    public void processingStart() {
        TextureVideoView textureVideoView;
        super.processingStart();
        Camera camera = (Camera) getContext();
        this.mInRecording = true;
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
    }

    /* access modifiers changed from: protected */
    public void provideAnimateElement(int i) {
        if (!this.mGotoShare) {
            this.mLandscapeHint.setVisibility(8);
            this.mCaptureHint.setVisibility(8);
            this.mSaveButton.setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public void resetToPreview(Mode mode) {
        this.mTimeFreezeButton.setVisibility(0);
        resetTimeFreezeButton();
    }

    /* access modifiers changed from: protected */
    public void resumeFragment() {
        if (this.mGotoShare) {
            String str = this.mSavedPath;
            if (str != null) {
                startVideoPlay(str);
            }
            return;
        }
        resetTimeFreezeButton();
        showTimeFreezeButton();
        this.mTimeFreezeButton.setAlpha(0.5f);
        Mode cloneMode = Config.getCloneMode();
        if (cloneMode != null && this.mCurrentMode == 213) {
            prepare(cloneMode, true);
        }
        CloneAction cloneAction = (CloneAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(419);
        if (cloneAction != null) {
            cloneAction.onFragmentResume();
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
                this.mTimeFreezeButton.setEnabled(z);
                if (z || z2) {
                    float f = 1.0f;
                    this.mCameraSnapView.setAlpha(z ? 1.0f : 0.5f);
                    ScrollTextview scrollTextview = this.mTimeFreezeButton;
                    if (!z) {
                        f = 0.5f;
                    }
                    scrollTextview.setAlpha(f);
                }
            }
            return;
        }
        Log.w(str, "setSnapButtonEnable ignore, is not landScape");
    }

    /* access modifiers changed from: protected */
    public void showSaveAndGiveUp(Mode mode) {
        showBackButton(false);
        this.mTimeFreezeButton.setVisibility(8);
    }

    public void updateCaptureMessage(int i, boolean z) {
        this.mCaptureHint.post(new C0296O00000oO(this, i, z));
    }

    public void updateCaptureMessage(String str, boolean z) {
        boolean z2 = this.mPaused;
        String str2 = TAG;
        if (z2) {
            Log.w(str2, "ignore updateCaptureMessage, paused");
        } else if (!isAdded()) {
            Log.w(str2, "ignore updateCaptureMessage, fragment not added");
        } else if (isLandScape()) {
            this.mCaptureHint.setTag(Boolean.valueOf(z));
            updateCaptureHintBackground(z);
            this.mCaptureHint.setText(str);
            showCaptureHint();
        } else {
            this.mCaptureHint.setTag(Boolean.valueOf(z));
            this.mCaptureHint.setText(str);
        }
    }

    /* access modifiers changed from: protected */
    public void updatePreviewUI() {
        resetTimeFreezeButton();
        showSaveAndGiveUp(true);
    }
}
