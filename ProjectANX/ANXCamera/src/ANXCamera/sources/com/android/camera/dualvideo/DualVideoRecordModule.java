package com.android.camera.dualvideo;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CaptureResult;
import android.location.Location;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.android.camera.AudioMonitorPlayer;
import com.android.camera.AutoLockManager;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.SoundSetting;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.ComponentRunningDualVideo;
import com.android.camera.dualvideo.recorder.MiRecorder.MiRecorderListener;
import com.android.camera.dualvideo.recorder.MultiRecorderManager;
import com.android.camera.dualvideo.recorder.RecordType;
import com.android.camera.dualvideo.render.RenderManager;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.VideoBase;
import com.android.camera.module.VideoModule;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.MultiCameraAttr;
import com.android.camera.storage.Storage;
import com.android.camera2.Camera2Proxy.PictureCallbackWrapper;
import com.android.gallery3d.exif.ExifHelper;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.Iterator;

public class DualVideoRecordModule extends DualVideoModuleBase {
    private AudioMonitorPlayer mAudioMonitorPlayer;
    private boolean mCameraPickerAnimFlag;
    private boolean mIsStopKaraoke = false;
    private long mPauseClickTime = 0;
    private final MiRecorderListener mRecorderListener = new C0156O000OOoo(this);
    private int mRecorderPausedTimes;
    private int mRecorderResumeTimes;
    private int mRenderCaptureTimes;
    private long mVideoRecordTime;

    public final class JpegPictureCallback extends PictureCallbackWrapper {
        final Location mLocation;

        public JpegPictureCallback(Location location) {
            this.mLocation = location;
        }

        private void storeImage(byte[] bArr, Location location) {
            byte[] bArr2 = bArr;
            Location location2 = location;
            long currentTimeMillis = System.currentTimeMillis();
            DualVideoRecordModule.this.mActivity.getImageSaver().addImage(bArr2, false, Util.createJpegName(currentTimeMillis), null, System.currentTimeMillis(), null, location2, DualVideoRecordModule.this.mVideoSize.width, DualVideoRecordModule.this.mVideoSize.height, null, ExifHelper.getOrientation(bArr), false, false, true, false, false, null, null, -1, null);
        }

        public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
            Log.v(VideoBase.TAG, "onPictureTaken");
            DualVideoRecordModule.this.mSnapshotInProgress = false;
            if (!DualVideoRecordModule.this.mPaused && bArr != null) {
                Location location = null;
                if (DualVideoRecordModule.this.mActivity.getCameraIntentManager().checkIntentLocationPermission(DualVideoRecordModule.this.mActivity)) {
                    location = this.mLocation;
                }
                storeImage(bArr, location);
            }
        }
    }

    public DualVideoRecordModule() {
        super(DualVideoRecordModule.class.getSimpleName());
    }

    private Location getLocation() {
        if (this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity)) {
            return LocationManager.instance().getCurrentLocation();
        }
        return null;
    }

    private long getRecorderMaxFileSize(int i) {
        long leftSpace = Storage.getLeftSpace() - Storage.LOW_STORAGE_THRESHOLD;
        if (i > 0) {
            long j = (long) i;
            if (j < leftSpace) {
                leftSpace = j;
            }
        }
        long j2 = VideoModule.VIDEO_MAX_SINGLE_FILE_SIZE;
        if (leftSpace <= VideoModule.VIDEO_MAX_SINGLE_FILE_SIZE || !C0122O00000o.instance().OO0o0O0()) {
            j2 = VideoModule.VIDEO_MIN_SINGLE_FILE_SIZE;
            if (leftSpace >= j2) {
                j2 = leftSpace;
            }
        }
        long j3 = this.mIntentRequestSize;
        return (j3 <= 0 || j3 >= j2) ? j2 : j3;
    }

    private int getRecorderOrientationHint() {
        int i = this.mOrientation;
        if (i == -1) {
            return 0;
        }
        return i;
    }

    private String getZoomGroupForTrack() {
        ArrayList selectedData = DataRepository.dataItemRunning().getComponentRunningDualVideo().getSelectedData();
        selectedData.sort(C0160O000OoO.INSTANCE);
        StringBuilder sb = new StringBuilder();
        Iterator it = selectedData.iterator();
        while (it.hasNext()) {
            sb.append(DualVideoConfigManager.instance().getConfigDescription(((UserSelectData) it.next()).getmSelectWindowLayoutType()));
            sb.append(":");
        }
        return sb.toString();
    }

    private void hideHint() {
        getTopAlert().ifPresent(C0157O000Oo00.INSTANCE);
    }

    private void onMediaRecorderReleased() {
        restoreOuterAudio();
        this.mActivity.sendBroadcast(new Intent(BaseModule.STOP_VIDEO_RECORDING_ACTION));
        enableCameraControls(true);
        keepScreenOnAwhile();
        doLaterReleaseIfNeed();
    }

    private void onStartRecorderSucceed() {
        this.mActivity.sendBroadcast(new Intent(BaseModule.START_VIDEO_RECORDING_ACTION));
        AutoLockManager.getInstance(this.mActivity).removeMessage();
        updateRecordingTime();
        keepScreenOn();
        keepPowerSave();
        hideHint();
    }

    private void pauseVideoRecording() {
        pauseVideoRecording2();
        updateReselectButton();
    }

    private void pauseVideoRecording2() {
        Log.d(VideoBase.TAG, "pauseVideoRecording");
        if (this.mMultiRecorderManager.isRecording() && !this.mMultiRecorderManager.isRecordingPaused()) {
            try {
                this.mMultiRecorderManager.pauseRecorder();
                this.mMediaRecorderRecordingPaused = true;
            } catch (IllegalStateException unused) {
                Log.e(VideoBase.TAG, "failed to pause media recorder");
            }
            this.mHandler.removeMessages(42);
            updateRecordingTime();
        }
    }

    private void resumeVideoRecording(RecordState recordState) {
        resumeVideoRecording2(recordState);
        updateReselectButton();
    }

    private void resumeVideoRecording2(RecordState recordState) {
        try {
            this.mMultiRecorderManager.resume();
            this.mHandler.removeMessages(42);
            updateRecordingTime();
            recordState.onResume();
            this.mMediaRecorderRecordingPaused = false;
        } catch (IllegalStateException e) {
            Log.e(VideoBase.TAG, "failed to resume media recorder", (Throwable) e);
            this.mMultiRecorderManager.release();
            recordState.onFailed();
        }
        if (!getZoomGroupForTrack().equals(CameraStatUtils.mZoomPair)) {
            CameraStatUtils.trackDualVideoCommonAttr(MultiCameraAttr.ATTR_DEVICE_TYPE, getZoomGroupForTrack());
            CameraStatUtils.mZoomPair = getZoomGroupForTrack();
        }
        forceTrackLayoutType(false);
    }

    private void startRecordingRelate() {
        silenceOuterAudio();
        this.mActivity.getScreenHint().updateHint();
        int[] iArr = {RenderSourceType.MAIN.getIndex()};
        if (CameraSettings.getDualVideoConfig().getRecordType() == RecordType.STANDALONE) {
            iArr = getSubCamera2Device().isPresent() ? new int[]{RenderSourceType.MAIN.getIndex(), RenderSourceType.SUB.getIndex()} : new int[]{RenderSourceType.MAIN.getIndex(), RenderSourceType.REMOTE.getIndex()};
        }
        int[] iArr2 = iArr;
        if (this.mAudioMonitorPlayer == null) {
            this.mAudioMonitorPlayer = new AudioMonitorPlayer();
        }
        SoundSetting.setNoiseReductionState(getActivity(), getModuleIndex(), true);
        if (SoundSetting.isStartKaraoke(getActivity(), getModuleIndex())) {
            SoundSetting.openKaraokeEquipment(getActivity(), getModuleIndex());
            StringBuilder sb = new StringBuilder();
            sb.append("SoundSetting.isStartKaraoke121");
            sb.append(SoundSetting.isStartKaraoke(getActivity(), getModuleIndex()));
            Log.d("isStartKaraoke", sb.toString());
            this.mIsStopKaraoke = true;
            AudioMonitorPlayer audioMonitorPlayer = this.mAudioMonitorPlayer;
            if (audioMonitorPlayer != null) {
                audioMonitorPlayer.startPlay();
            }
            SoundSetting.openKaraokeState(getActivity(), getModuleIndex());
        }
        this.mMultiRecorderManager.startRecorder(iArr2, getLocation(), this.mVideoSize, this.mRecorderListener, getRecorderMaxFileSize(0), getRecorderOrientationHint());
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            if (Storage.isLowStorageAtLastPoint()) {
                recordState.onFailed();
                return;
            } else {
                recordState.onPrepare();
                recordState.onStart();
            }
        }
        onStartRecorderSucceed();
        this.mMediaRecorderRecording = true;
        this.mMediaRecorderRecordingPaused = false;
        CameraStatUtils.trackDualVideoCommonAttr(MultiCameraAttr.ATTR_DEVICE_TYPE, getZoomGroupForTrack());
        CameraStatUtils.mZoomPair = getZoomGroupForTrack();
        forceTrackLayoutType(true);
    }

    @SuppressLint({"CheckResult"})
    private void stopRecorder() {
        Single.create(new O00O0Oo(this)).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new C0159O000Oo0o(this));
    }

    private void switchPreviewToSelectWindow(int i) {
        if (isMultiCameraMode() && !isSwitching() && this.mMainFrameIsAvailable) {
            ComponentRunningDualVideo componentRunningDualVideo = DataRepository.dataItemRunning().getComponentRunningDualVideo();
            if (!componentRunningDualVideo.ismDrawSelectWindow()) {
                switchThumbnailFunction(true);
                componentRunningDualVideo.setmDrawSelectWindow(true);
                getRenderManager().ifPresent(C0138O00000oO.INSTANCE);
                getRenderManager().ifPresent(O000Oo0.INSTANCE);
                getActivity().getGLView().requestRender();
                if (this.mActivity.isActivityPaused()) {
                    switchCameraLens(false, true, false, i);
                } else {
                    switchCameraLens(false, false, false, i);
                }
            }
        }
    }

    private void trackDualVideo() {
        if (!getRenderManager().isPresent()) {
            return;
        }
        if (C0122O00000o.instance().OOO000o()) {
            CameraStatUtils.trackMultiCameraDualVideo(String.valueOf(this.mVideoRecordTime), CameraSettings.getDualVideoConfig().getRecordType() == RecordType.MERGED ? MultiCameraAttr.VALUE_RECORD_MERGED : MultiCameraAttr.VALUE_RECORD_STANDALONE, this.mRecorderPausedTimes, this.mRecorderResumeTimes, this.mRenderCaptureTimes);
        } else {
            CameraStatUtils.trackDualVideoCommonAttr("attr_video_duration", String.valueOf(this.mVideoRecordTime));
        }
    }

    public /* synthetic */ void O00000Oo(SingleEmitter singleEmitter) {
        this.mMultiRecorderManager.stopRecorder(singleEmitter);
    }

    public /* synthetic */ void O00000Oo(Boolean bool) {
        onMediaRecorderReleased();
    }

    public /* synthetic */ void O00000oO(RenderManager renderManager) {
        if (!renderManager.isRecording()) {
            ((RenderManager) getRenderManager().get()).startRecording(getActivity().getGLView().getEGLContext14(), this.mMultiRecorderManager.getRecorderSurface());
        }
    }

    public /* synthetic */ void O00ooO0() {
        stopVideoRecording(false);
    }

    public boolean onBackPressed() {
        if (!isFrameAvailable()) {
            return false;
        }
        if (this.mPaused || this.mActivity.isActivityPaused()) {
            return true;
        }
        if (this.mMultiRecorderManager.isRecording() && isMultiCameraMode()) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
                this.mLastBackPressedTime = currentTimeMillis;
                ToastUtils.showToast((Context) this.mActivity, (int) R.string.record_back_pressed_hint, true);
            } else {
                stopVideoRecording(false);
            }
            return true;
        } else if (this.mMultiRecorderManager.isRecording() || !isMultiCameraMode()) {
            return super.onBackPressed();
        } else {
            CameraStatUtils.trackDualVideoCommonClick(MultiCameraAttr.VALUE_BACK_SELECT);
            switchPreviewToSelectWindow(2);
            return true;
        }
    }

    public boolean onCameraPickerClicked(View view) {
        Log.d(VideoBase.TAG, "onCameraPickerClicked: ");
        if (getRenderManager().isPresent() && ((RenderManager) getRenderManager().get()).switchTopBottom()) {
            ViberatorContext.getInstance(CameraAppImpl.getAndroidContext()).performSwitchCamera();
            ViewCompat.animate(view).rotationBy(this.mCameraPickerAnimFlag ? -180.0f : 180.0f).setDuration(300).start();
            this.mCameraPickerAnimFlag = !this.mCameraPickerAnimFlag;
            if (Util.isAccessible()) {
                announceForAccessibility(R.string.accessibility_dual_preview_change_finish);
            }
            CameraStatUtils.trackDualVideoCommonClick(this.mMultiRecorderManager.isRecordingPaused() ? MultiCameraAttr.VALUE_PAUSE_SWITCH : MultiCameraAttr.VALUE_IDLE_SWITCH);
        }
        return true;
    }

    public void onPauseButtonClick() {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPauseButtonClick: isRecordingPaused=");
        sb.append(this.mMultiRecorderManager.isRecordingPaused());
        sb.append(" isRecording=");
        sb.append(this.mMultiRecorderManager.isRecording());
        Log.d(str, sb.toString());
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mMultiRecorderManager.isRecording() && currentTimeMillis - this.mPauseClickTime >= 500) {
            this.mPauseClickTime = currentTimeMillis;
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (this.mMultiRecorderManager.isRecordingPaused()) {
                resumeVideoRecording(recordState);
                CameraStatUtils.trackPauseOrResumeVideoRecording(isFrontCamera(), true);
            } else {
                pauseVideoRecording();
                CameraStatUtils.trackPauseOrResumeVideoRecording(isFrontCamera(), false);
                recordState.onPause();
            }
            Log.d(VideoBase.TAG, "onPauseButtonClick");
        }
    }

    public void onResume() {
        super.onResume();
        if (isMultiCameraMode()) {
            showModeSwitchLayout(false);
        }
    }

    public void onReviewCancelClicked() {
        if (isMultiCameraMode()) {
            CameraStatUtils.trackDualVideoCommonClick(MultiCameraAttr.VALUE_BACK_SELECT);
            switchPreviewToSelectWindow(2);
            return;
        }
        super.onReviewCancelClicked();
    }

    public void onShutterButtonClick(int i) {
        Log.d(VideoBase.TAG, "onShutterButtonClick");
        if (this.mMultiRecorderManager.isRecording()) {
            stopVideoRecording(false);
            return;
        }
        playCameraSound(2);
        startVideoRecording();
    }

    public void reselectCamera() {
        this.mKeepRecorderWhenSwitching = true;
        this.mMainProtocol.clearFocusView(7);
        showOrHideBottom(false);
        showModeSwitchLayout(false);
        CameraStatUtils.trackDualVideoCommonClick(MultiCameraAttr.VALUE_RESELECT);
        switchPreviewToSelectWindow(-1);
    }

    public boolean shouldReleaseLater() {
        if (!this.mActivity.isActivityPaused()) {
            return super.shouldReleaseLater();
        }
        stopVideoRecording(true);
        return false;
    }

    /* access modifiers changed from: protected */
    public void startVideoRecording() {
        startRecordingRelate();
        getRenderManager().ifPresent(new C0161O000OoO0(this));
    }

    public void stopVideoRecording(boolean z) {
        Log.d(VideoBase.TAG, "stopVideoRecording: ");
        if (getRenderManager().isPresent() && ((Boolean) getRenderManager().map(O000o000.INSTANCE).orElse(Boolean.valueOf(false))).booleanValue()) {
            if (isMultiCameraMode()) {
                switchThumbnailFunction(true);
            }
            this.mRenderCaptureTimes = ((Integer) getRenderManager().map(C0158O000Oo0O.INSTANCE).orElse(Integer.valueOf(0))).intValue();
            getRenderManager().ifPresent(C0169O000o00o.INSTANCE);
        }
        SoundSetting.setNoiseReductionState(getActivity(), getModuleIndex(), false);
        SoundSetting.closeKaraokeState(getActivity(), getModuleIndex());
        if (this.mIsStopKaraoke) {
            this.mIsStopKaraoke = false;
            AudioMonitorPlayer audioMonitorPlayer = this.mAudioMonitorPlayer;
            if (audioMonitorPlayer != null) {
                audioMonitorPlayer.stopPlay();
            }
        }
        SoundSetting.closeKaraokeEquipment(getActivity(), getModuleIndex());
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stopVideoRecording>>");
        sb.append(this.mMultiRecorderManager.isRecording());
        sb.append("|");
        sb.append(true);
        Log.v(str, sb.toString());
        if (this.mMultiRecorderManager.isRecording()) {
            long currentTimeMillis = System.currentTimeMillis();
            MainContentProtocol mainContentProtocol = this.mMainProtocol;
            if (mainContentProtocol != null) {
                mainContentProtocol.processingFinish();
            }
            enableCameraControls(false);
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onFinish();
            }
            MultiRecorderManager multiRecorderManager = this.mMultiRecorderManager;
            this.mRecorderPausedTimes = multiRecorderManager.mStatPausedTimes;
            this.mRecorderResumeTimes = multiRecorderManager.mStatResumeTimes;
            trackDualVideo();
            this.mVideoRecordTime = 0;
            stopRecorder();
            this.mMediaRecorderRecording = false;
            this.mMediaRecorderRecordingPaused = false;
            if (!this.mPaused && !this.mActivity.isActivityPaused()) {
                playCameraSound(3);
            }
            AutoLockManager.getInstance(this.mActivity).hibernateDelayed();
            exitSavePowerMode();
            String str2 = VideoBase.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("stopVideoRecording<<time=");
            sb2.append(System.currentTimeMillis() - currentTimeMillis);
            Log.v(str2, sb2.toString());
            switchPreviewToSelectWindow(2);
            this.mActivity.getThumbnailUpdater().getLastThumbnail();
        }
    }

    public void takeVideoSnapShot() {
        Log.d(VideoBase.TAG, "takeVideoSnapShot");
        if (!getRenderManager().isPresent()) {
            this.mActivity.getCameraScreenNail().animateCapture(getCameraRotation());
        }
        if (getRenderManager().isPresent()) {
            ((RenderManager) getRenderManager().get()).captureVideoSnapshot(new JpegPictureCallback(LocationManager.instance().getCurrentLocation()), this.mActivity.getOrientation(), this.mCamera2Device.getPreviewCaptureResult());
        }
    }

    /* access modifiers changed from: protected */
    public void updateRecordingTime() {
        super.updateRecordingTime();
        if (this.mMultiRecorderManager.isRecording()) {
            long duration = this.mMultiRecorderManager.getDuration();
            this.mVideoRecordTime = duration / 1000;
            String millisecondToTimeString = Util.millisecondToTimeString(duration, false);
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null) {
                topAlert.updateRecordingTime(millisecondToTimeString);
            }
            long j = 500;
            if (!this.mMultiRecorderManager.isRecordingPaused()) {
                j = 1000 - (duration % 1000);
            }
            this.mHandler.sendEmptyMessageDelayed(42, j);
        }
    }

    /* access modifiers changed from: protected */
    public void updateReselectButton() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert == null) {
            Log.w(VideoBase.TAG, "topAlert is null");
            return;
        }
        topAlert.updateConfigItem(513);
    }
}
