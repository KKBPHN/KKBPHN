package com.android.camera.module;

import android.os.CountDownTimer;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.clone.Config;
import com.android.camera.fragment.clone.Status;
import com.android.camera.log.Log;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.StartControlFeatureDetail;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CloneProcess;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.FilmAttr;
import com.xiaomi.fenshen.FenShenCam;
import com.xiaomi.fenshen.FenShenCam.Message;
import com.xiaomi.fenshen.FenShenCam.Mode;
import com.xiaomi.fenshen.FenShenCam.TEventType;

public class TimeFreezeModule extends CloneModule {
    public static final int DURATION_VIDEO_RECORDING = 15000;
    private static final int MAX_VIDEO_SUBJECT_COUNT = 1;
    private static final String TAG = "TimeFreezeModule";
    private boolean mDetectedPerson = false;
    private boolean mInFreezing;
    private boolean mInPausing = false;
    private boolean mInPlaying = false;
    private boolean mInSaving = false;
    private CountDownTimer mTimeFreezeCountDownTimer;

    private void cancelTimeFreezeCountDown() {
        if (this.mTimeFreezeCountDownTimer != null) {
            Log.d(TAG, "cancelTimeFreezeCountDown");
            this.mTimeFreezeCountDownTimer.cancel();
            this.mTimeFreezeCountDownTimer = null;
        }
        if (!this.mInRecording) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TimeFreezeModule.this.resetTipHint();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void resetTipHint() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null) {
            if (cloneProcess.getStatus() != Status.CAPTURING) {
                Log.w(TAG, "onPictureTakenImageConsumed not capturing");
                return;
            }
            cloneProcess.updateCaptureMessage(-1, false);
        }
    }

    private void startFenShenCam() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null) {
            cloneProcess.updateCaptureMessage((int) R.string.clone_no_person_found, false);
            if (cloneProcess.getStatus() == Status.SHARE) {
                return;
            }
        }
        FenShenCam.start();
    }

    private void startTimeFreezeCountDown() {
        if (this.mTimeFreezeCountDownTimer == null) {
            AnonymousClass2 r1 = new CountDownTimer(5450, 1000) {
                public void onFinish() {
                    TimeFreezeModule.this.resetTipHint();
                    ((CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418)).onCountDownFinished();
                }

                public void onTick(long j) {
                }
            };
            this.mTimeFreezeCountDownTimer = r1;
        }
        this.mTimeFreezeCountDownTimer.cancel();
        this.mTimeFreezeCountDownTimer.start();
    }

    private void switchTimeFreeze(CloneProcess cloneProcess) {
        if (this.mInFreezing) {
            this.mInFreezing = false;
            FenShenCam.stopTimeFreeze();
            if (!this.mInRecording) {
                FenShenCam.cancelVideo();
                FenShenCam.start();
                if (!this.mDetectedPerson) {
                    onCloneMessage(Message.NO_PERSON);
                }
            }
            cancelTimeFreezeCountDown();
            return;
        }
        this.mInFreezing = true;
        FenShenCam.startTimeFreeze();
        if (!this.mInRecording) {
            startTimeFreezeCountDown();
            if (cloneProcess != null) {
                cloneProcess.updateCaptureMessage((int) R.string.film_timefreeze_hint, false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void cancelPhotoOrVideo() {
        FenShenCam.cancelVideo();
    }

    public boolean dispatchConfigChange(int i) {
        if (i == 251) {
            boolean timeFreezeFilmRatioEnabled = DataRepository.dataItemLive().getTimeFreezeFilmRatioEnabled();
            DataRepository.dataItemLive().setTimeFreezeFilmRatioEnabled(!timeFreezeFilmRatioEnabled);
            CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
            if (cloneProcess != null) {
                cloneProcess.onFilmRatioChanged(!timeFreezeFilmRatioEnabled, true);
            }
        }
        return false;
    }

    public void fillFeatureControl(StartControl startControl) {
        StartControlFeatureDetail featureDetail = startControl.getFeatureDetail();
        featureDetail.addFragmentInfo(R.id.full_screen_feature, BaseFragmentDelegate.FRAGMENT_FILM_TIME_FREEZE);
        featureDetail.hideFragment(R.id.bottom_action);
        featureDetail.hideFragment(R.id.bottom_popup_tips);
        featureDetail.hideFragment(R.id.bottom_popup_dual_camera_adjust);
    }

    /* access modifiers changed from: protected */
    public int getDurationVideoRecording() {
        return 15000;
    }

    /* access modifiers changed from: protected */
    public int getMaxVideoSubjectCount() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return 0;
    }

    public String getTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void initCloneMode(Mode mode) {
        if (mode == Mode.TIMEFREEZE) {
            startFenShenCam();
            this.mPendingStart = false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isVideoMode() {
        return this.mMode == Mode.TIMEFREEZE;
    }

    /* access modifiers changed from: protected */
    public boolean onCloneMessage(Mode mode, Message message, CloneProcess cloneProcess, int i) {
        if (cloneProcess.getStatus() == Status.SHARE) {
            return false;
        }
        this.mMainProtocol.clearFocusView(7);
        this.mDetectedPerson = false;
        if (message == Message.ALIGN_TOO_LARGE_OR_FAILED) {
            Log.d(TAG, "too much movement, stop capture");
            if (this.mInRecording) {
                cloneProcess.updateCaptureMessage((int) R.string.clone_too_much_movement_stop, false);
                onShutterButtonClick(10, true);
            } else if (this.mInFreezing) {
                cloneProcess.onCountDownFinished();
            }
            return false;
        }
        if (message == Message.PREVIEW_NO_PERSON || message == Message.NO_PERSON) {
            if (!this.mInRecording) {
                cloneProcess.setDetectedPersonInPreview(false);
            }
        } else if (message == Message.START || message == Message.PREVIEW_PERSON) {
            cloneProcess.setDetectedPersonInPreview(true);
            cloneProcess.updateCaptureMessage(-1, false);
            this.mDetectedPerson = true;
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateCaptureMessage ");
        sb.append(message.toString());
        Log.d(str, sb.toString());
        if ((!this.mInRecording || message != Message.NO_PERSON) && !this.mInFreezing && !this.mInPlaying) {
            cloneProcess.updateCaptureMessage(i, false);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Mode mode) {
        this.mInFreezing = false;
    }

    /* access modifiers changed from: protected */
    public void onError() {
        Log.e(TAG, "onError");
        this.mHandler.post(new C0364O000OoO(this));
        if (this.mInPlaying) {
            this.mHandler.post(new C0381O000oO(this));
        }
    }

    public void onExitClicked() {
        super.onExitClicked();
        CameraStatUtils.trackFilmTimeFreezeClick(FilmAttr.VALUE_TIME_FREEZE_CLICK_EXIT_CONFIRM);
    }

    public void onHostStopAndNotifyActionStop(Mode mode) {
        if (this.mInRecording) {
            this.mInRecording = false;
            stopVideoRecording(true);
        }
    }

    public void onPause() {
        if (this.mInFreezing) {
            switchTimeFreeze((CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418));
        }
        super.onPause();
    }

    public void onPlayClicked() {
        Log.d(TAG, "onPlayClicked");
        if (this.mInPausing) {
            FenShenCam.resumePlayEffect();
            this.mInPausing = false;
        }
    }

    public void onResume() {
        super.onResume();
        if (!this.mDetectedPerson) {
            onCloneMessage(Message.NO_PERSON);
        }
    }

    public void onReviewDoneClicked() {
        Config.setCloneMode(null);
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null) {
            cloneProcess.quit();
        }
        TopAlert topAlert = this.mTopAlert;
        if (topAlert != null) {
            topAlert.showConfigMenu();
        }
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null) {
            configChanges.configFilm(null, false, true);
        }
    }

    /* access modifiers changed from: protected */
    public void onShutterButtonClick(Mode mode) {
        if (!this.mInSaving) {
            if (!this.mInRecording) {
                TopAlert topAlert = this.mTopAlert;
                if (topAlert != null) {
                    topAlert.disableMenuItem(true, 251);
                }
                delayTriggerShooting();
            } else if (this.mFrameCount < 15) {
                Log.d(TAG, "ignore onShutterButtonClick cause frameCount < 15");
            } else {
                this.mInRecording = false;
                stopVideoRecording(false);
                stopCaptureToPreviewResult();
            }
        }
    }

    public void onSingleTapUp(int i, int i2, boolean z) {
    }

    public void onStop() {
        super.onStop();
        FenShenCam.cancelVideo();
    }

    public void onTimeFreezeClicked() {
        Log.d(TAG, "onTimeFreezeClicked");
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess == null || cloneProcess.getStatus() == Status.CAPTURING) {
            switchTimeFreeze(cloneProcess);
        } else {
            Log.w(TAG, "onPictureTakenImageConsumed not capturing");
        }
    }

    public boolean onTouchDown(float f, float f2) {
        if (!this.mInPlaying) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onTouchDown  (");
            sb.append(f);
            sb.append(",");
            sb.append(f2);
            sb.append(")");
            Log.d(str, sb.toString());
            FenShenCam.sendTouchEvent(TEventType.CLICK_DOWN, 0.0f, f, f2, 0.0f, 0.0f);
        } else {
            CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
            if (this.mInPausing) {
                FenShenCam.resumePlayEffect();
                this.mInPausing = false;
                if (cloneProcess != null) {
                    cloneProcess.onReplayResume();
                }
            } else {
                FenShenCam.pausePlayEffect();
                this.mInPausing = true;
                if (cloneProcess != null) {
                    cloneProcess.onReplayPause();
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onVideoSaveFinish() {
        this.mInSaving = false;
        super.onVideoSaveFinish();
        this.mInRecording = false;
    }

    public void playFocusSound(int i) {
    }

    /* access modifiers changed from: protected */
    public void resumePreviewIfNeeded() {
        Log.d(TAG, "resumePreviewIfNeeded");
        this.mIsFinished = false;
        resumePreview();
        cancelPhotoOrVideo();
    }

    /* access modifiers changed from: protected */
    public void saveVideo() {
        super.saveVideo();
        this.mInSaving = true;
    }

    public void setFrameAvailable(boolean z) {
        super.setFrameAvailable(z);
        if (z) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    FenShenCam.setFilmFormat(DataRepository.dataItemLive().getTimeFreezeFilmRatioEnabled());
                }
            }, 400);
            startFenShenCam();
            CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
            if (cloneProcess != null) {
                cloneProcess.onFrameAvailable();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setVideoCodec() {
        FenShenCam.setVideoCodec(CameraSettings.getVideoEncoder() == 5 ? "video/hevc" : "video/avc");
    }

    public boolean shouldReleaseLater() {
        return this.mInRecording || this.mIsFinished;
    }

    /* access modifiers changed from: protected */
    public void startPreviewSession() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess == null || cloneProcess.getStatus() != Status.SHARE) {
            super.startPreviewSession();
        }
    }

    /* access modifiers changed from: protected */
    public void startVideoRecording(Mode mode, CloneProcess cloneProcess) {
        super.startVideoRecording(mode, cloneProcess);
        cancelTimeFreezeCountDown();
        if (cloneProcess != null) {
            cloneProcess.updateCaptureMessage((int) R.string.clone_do_not_move_phone, false);
        }
        CameraStatUtils.trackFilmTimeFreezeRecord(this.mInFreezing);
    }

    /* access modifiers changed from: protected */
    public void stopCaptureToPreviewResult(Mode mode) {
        playCameraSound(3);
    }

    /* access modifiers changed from: protected */
    public void stopVideoRecording(boolean z, Mode mode, CloneProcess cloneProcess) {
        super.stopVideoRecording(z, mode, cloneProcess);
        if (cloneProcess != null) {
            cloneProcess.updateCaptureMessage(-1, false);
        }
        if (!z) {
            this.mInPlaying = true;
        }
    }
}
