package com.android.camera.features.mimoji2.module;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.SensorEvent;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Range;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import androidx.annotation.MainThread;
import com.android.camera.AudioMonitorPlayer;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.SensorStateManager.SensorStateListener;
import com.android.camera.SoundSetting;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FaceAnalyzeInfo;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiFullScreenProtocol;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiVideoEditor;
import com.android.camera.features.mimoji2.widget.helper.MimojiHelper2;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.fragment.beauty.LiveBeautyFilterFragment.LiveFilterItem;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.impl.ImplFactory;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.module.loader.FunctionAsdSceneDetect;
import com.android.camera.module.loader.FunctionFaceDetect;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.module.loader.camera2.FocusManager2.Listener;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BluetoothHeadset;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CloneChooser;
import com.android.camera.protocol.ModeProtocol.FullScreenProtocol;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.MimojiGifEditor;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import com.android.camera.storage.Storage;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.FaceDetectionCallback;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CameraHardwareFace;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.rx.CameraSchedulers;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MimojiModule extends BaseModule implements CameraPreviewCallback, FocusCallback, FaceDetectionCallback, CameraAction, PictureCallback, Listener {
    private static final int BEAUTY_SWITCH = 8;
    private static final int FILTER_SWITCH = 2;
    public static final int IGNORE_GIF_TIME = 1000;
    private static final int STICKER_SWITCH = 4;
    public static final int STOP_RECORD_FOROM_BACK = 2;
    public static final int STOP_RECORD_FOROM_NORMAL = 0;
    public static final int STOP_RECORD_FOROM_RELEASE = 1;
    /* access modifiers changed from: private */
    public static final String TAG = "MimojiModule";
    public static final int VALID_VIDEO_TIME = 500;
    private boolean m3ALocked;
    private AudioMonitorPlayer mAudioMonitorPlayer;
    private String mBaseFileName;
    protected BeautyValues mBeautyValues;
    private String mCaptureWaterMarkStr;
    private int mDeviceOrientation = 90;
    private boolean mDisableSingleTapUp = false;
    protected boolean mFaceDetectionEnabled;
    protected boolean mFaceDetectionStarted;
    /* access modifiers changed from: private */
    public FocusManager2 mFocusManager;
    private boolean mIsLowLight;
    private boolean mIsStopKaraoke = false;
    private Disposable mMetaDataDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mMetaDataFlowableEmitter;
    private MimojiVideoEditor mMimojiVideoEditor;
    private int mOldOriginVolumeStream;
    /* access modifiers changed from: private */
    public long mOnResumeTime;
    private int mQuality = 5;
    private long mRecordTime;
    protected SensorStateListener mSensorStateListener = new SensorStateListener() {
        public boolean isWorking() {
            return MimojiModule.this.isAlive() && MimojiModule.this.getCameraState() != 0;
        }

        public void notifyDevicePostureChanged() {
            MimojiModule.this.mActivity.getEdgeShutterView().onDevicePostureChanged();
        }

        public void onDeviceBecomeStable() {
            Log.v(MimojiModule.TAG, "onDeviceBecomeStable");
        }

        public void onDeviceBeginMoving() {
        }

        public void onDeviceKeepMoving(double d) {
            if (!MimojiModule.this.mMainProtocol.isEvAdjusted(true) && !MimojiModule.this.mPaused && Util.isTimeout(System.currentTimeMillis(), MimojiModule.this.mTouchFocusStartingTime, 3000) && !MimojiModule.this.is3ALocked() && MimojiModule.this.mFocusManager != null && MimojiModule.this.mFocusManager.isNeedCancelAutoFocus() && !MimojiModule.this.isRecording()) {
                MimojiModule.this.mFocusManager.onDeviceKeepMoving(d);
            }
        }

        public void onDeviceKeepStable() {
        }

        public void onDeviceLieChanged(boolean z) {
        }

        public void onDeviceOrientationChanged(float f, boolean z) {
            MimojiModule mimojiModule = MimojiModule.this;
            if (z) {
                f = (float) mimojiModule.mOrientation;
            }
            mimojiModule.mDeviceRotation = f;
            if (MimojiModule.this.isGradienterOn) {
                EffectController.getInstance().setDeviceRotation(z, Util.getShootRotation(MimojiModule.this.mActivity, MimojiModule.this.mDeviceRotation));
            }
        }

        public void onDeviceRotationChanged(float[] fArr) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
        }
    };
    protected boolean mShowFace = false;
    /* access modifiers changed from: private */
    public long mTouchFocusStartingTime;
    private MimojiAvatarEngine2 mimojiAvatarEngine2;
    private boolean misFaceLocationOk;

    class LiveAsdConsumer implements Consumer {
        private LiveAsdConsumer() {
        }

        public void accept(Integer num) {
            MimojiModule.this.mimojiLightDetect(num);
        }
    }

    class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 2) {
                MimojiModule.this.getWindow().clearFlags(128);
            } else if (i == 4) {
                MimojiModule.this.checkActivityOrientation();
                if (SystemClock.uptimeMillis() - MimojiModule.this.mOnResumeTime < 5000) {
                    MimojiModule.this.mHandler.sendEmptyMessageDelayed(4, 100);
                }
            } else if (i == 9) {
                MimojiModule.this.mMainProtocol.initializeFocusView(MimojiModule.this);
            } else if (i == 17) {
                MimojiModule.this.mHandler.removeMessages(17);
                MimojiModule.this.mHandler.removeMessages(2);
                MimojiModule.this.getWindow().addFlags(128);
                MimojiModule.this.mHandler.sendEmptyMessageDelayed(2, (long) MimojiModule.this.getScreenDelay());
            } else if (i == 31) {
                MimojiModule.this.setOrientationParameter();
            } else if (i == 35) {
                MimojiModule mimojiModule = MimojiModule.this;
                boolean z = false;
                boolean z2 = message.arg1 > 0;
                if (message.arg2 > 0) {
                    z = true;
                }
                mimojiModule.handleUpdateFaceView(z2, z);
            } else if (i == 51 && !MimojiModule.this.mActivity.isActivityPaused()) {
                MimojiModule.this.mOpenCameraFail = true;
                MimojiModule.this.onCameraException();
            }
        }
    }

    static /* synthetic */ void O00oO0o0() {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            mainContentProtocol.setCenterHint(8, null, null, 0);
        }
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.hideSwitchTip();
        }
    }

    private String createName(long j, int i) {
        if (i > 0) {
            return this.mBaseFileName;
        }
        this.mBaseFileName = new SimpleDateFormat(getString(R.string.video_file_name_format), Locale.ENGLISH).format(new Date(j));
        return this.mBaseFileName;
    }

    private void deleteMimojiCache() {
        MimojiAvatarEngine2 mimojiAvatarEngine22 = this.mimojiAvatarEngine2;
        if (mimojiAvatarEngine22 != null) {
            mimojiAvatarEngine22.deleteMimojiCache(0);
        } else {
            Log.e(TAG, "mimoji void deleteMimojiCache[] null");
        }
    }

    private boolean doLaterReleaseIfNeed() {
        Camera camera = this.mActivity;
        if (camera == null || !camera.isActivityPaused()) {
            return false;
        }
        this.mActivity.pauseIfNotRecording();
        this.mActivity.releaseAll(true, true);
        return true;
    }

    /* access modifiers changed from: private */
    public void handleUpdateFaceView(boolean z, boolean z2) {
        int i;
        boolean z3;
        boolean z4;
        boolean z5;
        MainContentProtocol mainContentProtocol;
        boolean isFrontCamera = isFrontCamera();
        if (!z) {
            mainContentProtocol = this.mMainProtocol;
            z3 = false;
            i = -1;
            z5 = z;
            z4 = z2;
        } else if (this.mFaceDetectionStarted && 1 != this.mCamera2Device.getFocusMode()) {
            mainContentProtocol = this.mMainProtocol;
            z4 = true;
            z3 = true;
            i = this.mCameraDisplayOrientation;
            z5 = z;
        } else {
            return;
        }
        mainContentProtocol.updateFaceView(z5, z4, isFrontCamera, z3, i);
    }

    private void initMetaParser() {
        this.mMetaDataDisposable = Flowable.create(new FlowableOnSubscribe() {
            public void subscribe(FlowableEmitter flowableEmitter) {
                MimojiModule.this.mMetaDataFlowableEmitter = flowableEmitter;
            }
        }, BackpressureStrategy.DROP).map(new FunctionFaceDetect(this, isFrontCamera())).map(new FunctionAsdSceneDetect(this, getCameraCapabilities())).subscribe((Consumer) new LiveAsdConsumer());
    }

    private void initMimojiEngine() {
        this.mOldOriginVolumeStream = getActivity().getVolumeControlStream();
        this.mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
        if (this.mimojiAvatarEngine2 == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 246);
            this.mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
            this.mimojiAvatarEngine2.onDeviceRotationChange(this.mDeviceOrientation);
        }
        this.mMimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        if (this.mMimojiVideoEditor == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 252);
            this.mMimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        }
    }

    private void initializeFocusManager() {
        this.mFocusManager = new FocusManager2(this.mCameraCapabilities, this, isFrontCamera(), this.mActivity.getMainLooper());
        Rect renderRect = this.mActivity.getCameraScreenNail() != null ? this.mActivity.getCameraScreenNail().getRenderRect() : null;
        if (renderRect == null || renderRect.width() <= 0) {
            this.mFocusManager.setRenderSize(Display.getWindowWidth(), Display.getWindowHeight());
            this.mFocusManager.setPreviewSize(Display.getWindowWidth(), Display.getWindowHeight());
            return;
        }
        this.mFocusManager.setRenderSize(this.mActivity.getCameraScreenNail().getRenderWidth(), this.mActivity.getCameraScreenNail().getRenderHeight());
        this.mFocusManager.setPreviewSize(renderRect.width(), renderRect.height());
    }

    private boolean isPreviewEisOn() {
        return isBackCamera() && CameraSettings.isMovieSolidOn() && this.mCameraCapabilities.isEISPreviewSupported();
    }

    private boolean isSaving() {
        MimojiFullScreenProtocol mimojiFullScreenProtocol = (MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249);
        return false;
    }

    /* access modifiers changed from: private */
    public void mimojiLightDetect(Integer num) {
        MimojiAvatarEngine2 mimojiAvatarEngine22 = this.mimojiAvatarEngine2;
        if (mimojiAvatarEngine22 != null && mimojiAvatarEngine22.isOnCreateMimoji()) {
            if (!this.misFaceLocationOk) {
                MainContentProtocol mainContentProtocol = this.mMainProtocol;
                if (mainContentProtocol != null) {
                    mainContentProtocol.updateMimojiFaceDetectResultTip(false);
                }
                return;
            }
            MainContentProtocol mainContentProtocol2 = this.mMainProtocol;
            if (mainContentProtocol2 != null) {
                mainContentProtocol2.setMimojiDetectTipType(162);
            }
            int intValue = num.intValue();
            if (intValue == 6 || intValue == 9) {
                this.mIsLowLight = true;
                MainContentProtocol mainContentProtocol3 = this.mMainProtocol;
                if (mainContentProtocol3 != null) {
                    mainContentProtocol3.updateMimojiFaceDetectResultTip(true);
                }
            } else {
                this.mIsLowLight = false;
                MainContentProtocol mainContentProtocol4 = this.mMainProtocol;
                if (mainContentProtocol4 != null) {
                    mainContentProtocol4.updateMimojiFaceDetectResultTip(false);
                }
            }
        }
    }

    private void onMimojiCapture() {
        this.mCamera2Device.setShotType(-1);
        this.mCamera2Device.takePicture(this, this.mActivity.getImageSaver());
    }

    private void previewWhenSessionSuccess() {
        setCameraState(1);
        updatePreferenceInWorkThread(UpdateConstant.FUN_TYPES_ON_PREVIEW_SUCCESS);
    }

    private void setOrientation(int i, int i2) {
        if (isAlive() && i != -1) {
            this.mOrientation = i;
            checkActivityOrientation();
            if (this.mOrientationCompensation != i2) {
                this.mOrientationCompensation = i2;
                setOrientationParameter();
            }
        }
    }

    /* access modifiers changed from: private */
    public void setOrientationParameter() {
        if (!(isDeparted() || this.mCamera2Device == null || this.mOrientation == -1)) {
            if (!isFrameAvailable() || getCameraState() != 1) {
                CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new O00000o(this));
            } else {
                updatePreferenceInWorkThread(35);
            }
        }
    }

    private void setWaterMark() {
        if (isDeviceAlive()) {
            if (CameraSettings.isDualCameraWaterMarkOpen()) {
                this.mCamera2Device.setDualCamWaterMarkEnable(true);
            } else {
                this.mCamera2Device.setDualCamWaterMarkEnable(false);
            }
            if (CameraSettings.isTimeWaterMarkOpen()) {
                this.mCamera2Device.setTimeWaterMarkEnable(true);
                this.mCaptureWaterMarkStr = Util.getTimeWatermark(this.mActivity);
                this.mCamera2Device.setTimeWatermarkValue(this.mCaptureWaterMarkStr);
            } else {
                this.mCaptureWaterMarkStr = null;
                this.mCamera2Device.setTimeWaterMarkEnable(false);
            }
        }
    }

    private boolean shouldApplyUltraWideLDC() {
        boolean z = false;
        if (C0124O00000oO.isMTKPlatform()) {
            if (this.mModuleIndex == 174 && this.mActualCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                z = true;
            }
            return z;
        } else if (CameraSettings.shouldUltraWideVideoLDCBeVisibleInMode(this.mModuleIndex) && this.mActualCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
            return CameraSettings.isUltraWideVideoLDCEnabled();
        } else {
            return false;
        }
    }

    private void showPreview() {
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (isAlive() && recordState != null) {
            keepScreenOnAwhile();
            recordState.onPostPreview();
            pausePreview();
            MimojiAvatarEngine2 mimojiAvatarEngine22 = this.mimojiAvatarEngine2;
            if (mimojiAvatarEngine22 != null) {
                mimojiAvatarEngine22.releaseRender();
                if (CameraSettings.isGifOn()) {
                    BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
                    if (baseDelegate != null) {
                        baseDelegate.delegateEvent(20);
                    }
                } else if (!FileUtils.checkFileConsist(this.mimojiAvatarEngine2.getVideoCache())) {
                    onReviewCancelClicked();
                } else {
                    ((MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249)).startMimojiRecordPreview();
                }
            } else {
                Log.e(TAG, " mimoji  showPreview contentValues null error");
            }
        }
    }

    private boolean startScreenLight(int i, int i2) {
        if (this.mPaused) {
            return false;
        }
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.setWindowBrightness(i2);
        }
        FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
        if (fullScreenProtocol == null) {
            return true;
        }
        fullScreenProtocol.setScreenLightColor(i);
        return fullScreenProtocol.showScreenLight();
    }

    @MainThread
    private void startVideoRecording() {
        MimojiGifEditor mimojiGifEditor = (MimojiGifEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(251);
        setCameraAudioRestriction(true);
        if (!this.mimojiAvatarEngine2.isRecordStopping() && mimojiGifEditor == null && System.currentTimeMillis() - this.mRecordTime >= 500) {
            keepScreenOn();
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
            this.mOldOriginVolumeStream = getActivity().getVolumeControlStream();
            getActivity().setVolumeControlStream(3);
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            recordState.onPrepare();
            recordState.onStart();
            listenPhoneState(true);
            if (!checkCallingState()) {
                recordState.onFailed();
                return;
            }
            MimojiAvatarEngine2 mimojiAvatarEngine22 = this.mimojiAvatarEngine2;
            if (mimojiAvatarEngine22 != null) {
                mimojiAvatarEngine22.onRecordStart();
                this.mRecordTime = System.currentTimeMillis();
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (bottomPopupTips != null) {
                    bottomPopupTips.hideTipImage();
                }
            }
            MimojiVideoEditor mimojiVideoEditor = this.mMimojiVideoEditor;
            if (mimojiVideoEditor != null) {
                CameraSize cameraSize = this.mPreviewSize;
                mimojiVideoEditor.setRecordParameter(cameraSize.height, cameraSize.width, this.mOrientation);
            }
            HashMap hashMap = new HashMap();
            hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
            trackGeneralInfo(hashMap, 1, false, this.mBeautyValues, false, -1);
        }
    }

    private void stopScreenLight() {
        this.mHandler.post(new O00000o0(this));
    }

    private void trackLiveRecordingParams() {
        boolean z;
        boolean z2;
        int i;
        boolean z3;
        int liveAllSwitchAllValue = CameraSettings.getLiveAllSwitchAllValue();
        String str = CameraSettings.getCurrentLiveMusic()[1];
        boolean z4 = !str.isEmpty();
        LiveFilterItem findLiveFilter = EffectController.getInstance().findLiveFilter(CameraAppImpl.getAndroidContext(), DataRepository.dataItemLive().getLiveFilter());
        if (!findLiveFilter.directoryName.isEmpty()) {
            if ((liveAllSwitchAllValue & 2) == 0) {
                liveAllSwitchAllValue += 2;
            }
            z = true;
        } else {
            z = false;
        }
        String currentLiveStickerName = CameraSettings.getCurrentLiveStickerName();
        if (!currentLiveStickerName.isEmpty()) {
            if ((liveAllSwitchAllValue & 4) == 0) {
                liveAllSwitchAllValue += 4;
            }
            z2 = true;
        } else {
            z2 = false;
        }
        String currentLiveSpeedText = CameraSettings.getCurrentLiveSpeedText();
        int faceBeautyRatio = CameraSettings.getFaceBeautyRatio("key_live_shrink_face_ratio");
        int faceBeautyRatio2 = CameraSettings.getFaceBeautyRatio("key_live_enlarge_eye_ratio");
        int faceBeautyRatio3 = CameraSettings.getFaceBeautyRatio("key_live_smooth_strength");
        if (faceBeautyRatio > 0 || faceBeautyRatio2 > 0 || faceBeautyRatio3 > 0) {
            if ((i & 8) == 0) {
                i += 8;
            }
            z3 = true;
        } else {
            z3 = false;
        }
        CameraStatUtils.trackLiveRecordingParams(z4, str, z, findLiveFilter.directoryName, z2, currentLiveStickerName, currentLiveSpeedText, z3, faceBeautyRatio, faceBeautyRatio2, faceBeautyRatio3, this.mQuality, isFrontCamera());
        CameraSettings.setLiveAllSwitchAddValue(i);
    }

    private void updateBeauty() {
        if (DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPreview()) {
            if (this.mBeautyValues == null) {
                this.mBeautyValues = new BeautyValues();
            }
            CameraSettings.initBeautyValues(this.mBeautyValues, this.mModuleIndex);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateBeauty(): ");
            sb.append(this.mBeautyValues);
            Log.d(str, sb.toString());
            this.mCamera2Device.setBeautyValues(this.mBeautyValues);
        }
    }

    private void updateDeviceOrientation() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
    }

    private void updateFaceView(boolean z, boolean z2) {
        if (this.mHandler.hasMessages(35)) {
            this.mHandler.removeMessages(35);
        }
        this.mHandler.obtainMessage(35, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
    }

    private void updateFilter() {
        int shaderEffect = CameraSettings.getShaderEffect();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateFilter: 0x");
        sb.append(Integer.toHexString(shaderEffect));
        Log.v(str, sb.toString());
        EffectController.getInstance().setEffect(shaderEffect);
    }

    private void updateFocusMode() {
        setFocusMode(this.mFocusManager.setFocusMode(CameraSettings.getFocusMode()));
    }

    private void updateFpsRange() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        Integer valueOf = Integer.valueOf(30);
        camera2Proxy.setFpsRange(new Range(valueOf, valueOf));
    }

    private void updateGif() {
    }

    private void updateLiveRelated() {
        this.mCamera2Device.startPreviewCallback(this.mimojiAvatarEngine2, null);
        MimojiAvatarEngine2 mimojiAvatarEngine22 = this.mimojiAvatarEngine2;
        int i = this.mCameraDisplayOrientation;
        int i2 = isFrontCamera() ? 270 : 90;
        CameraSize cameraSize = this.mPreviewSize;
        mimojiAvatarEngine22.initAvatarEngine(i, i2, cameraSize.width, cameraSize.height, isFrontCamera());
    }

    private void updateMimojiVideoCache() {
        new Thread(new C0272O00000oO(this)).start();
    }

    private void updatePictureAndPreviewSize() {
        CameraSize cameraSize;
        double d;
        int i;
        boolean z;
        List supportedOutputSizeWithAssignedMode = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class);
        float ratio = Util.getRatio(CameraSettings.getPictureSizeRatioString(this.mModuleIndex));
        if (CameraSettings.isGifOn()) {
            z = false;
            i = this.mBogusCameraId;
            d = (double) ratio;
            cameraSize = new CameraSize(1280, Util.LIMIT_SURFACE_WIDTH);
        } else {
            z = false;
            i = this.mBogusCameraId;
            d = (double) ratio;
            cameraSize = new CameraSize(1920, 1080);
        }
        CameraSize optimalPreviewSize = Util.getOptimalPreviewSize(z, i, supportedOutputSizeWithAssignedMode, d, cameraSize);
        this.mPreviewSize = optimalPreviewSize;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("previewSize: ");
        sb.append(this.mPreviewSize.toString());
        Log.d(str, sb.toString());
        CameraSize optimalVideoSnapshotPictureSize = Util.getOptimalVideoSnapshotPictureSize(supportedOutputSizeWithAssignedMode, (double) CameraSettings.getPreviewAspectRatio(16, 9), Display.getWindowHeight(), Display.getWindowWidth());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("displaySize: ");
        sb2.append(optimalVideoSnapshotPictureSize.toString());
        Log.d(str2, sb2.toString());
        this.mCamera2Device.setAlgorithmPreviewSize(optimalPreviewSize);
        this.mCamera2Device.setAlgorithmPreviewFormat(35);
        updateCameraScreenNailSize(optimalPreviewSize.width, optimalPreviewSize.height);
    }

    private void updateUltraWideLDC() {
        this.mCamera2Device.setUltraWideLDC(shouldApplyUltraWideLDC());
    }

    private void updateVideoStabilization() {
        if (isDeviceAlive()) {
            if (isPreviewEisOn()) {
                Log.d(TAG, "videoStabilization: EIS");
                this.mCamera2Device.setEnableEIS(true);
                this.mCamera2Device.setEnableOIS(false);
            } else {
                Log.d(TAG, "videoStabilization: OIS");
                this.mCamera2Device.setEnableEIS(false);
                this.mCamera2Device.setEnableOIS(true);
            }
            this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(false);
        }
    }

    public /* synthetic */ void O00oO() {
        MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
        if (mimojiStatusManager2.isInMimojiPreview() && mimojiStatusManager2.getMimojiPanelState() == 0) {
            String str = null;
            if (mimojiStatusManager2.getCurrentMimojiTimbreInfo() != null) {
                if (FileUtils.checkFileConsist(MimojiHelper2.VIDEO_DEAL_CACHE_FILE)) {
                    str = MimojiHelper2.VIDEO_DEAL_CACHE_FILE;
                } else if (FileUtils.checkFileConsist(MimojiHelper2.VIDEO_NORMAL_CACHE_FILE)) {
                    MimojiVideoEditor mimojiVideoEditor = this.mMimojiVideoEditor;
                    if (mimojiVideoEditor != null) {
                        mimojiVideoEditor.combineVideoAudio(MimojiHelper2.VIDEO_NORMAL_CACHE_FILE, 2);
                    }
                }
            } else if (FileUtils.checkFileConsist(MimojiHelper2.VIDEO_NORMAL_CACHE_FILE)) {
                str = MimojiHelper2.VIDEO_NORMAL_CACHE_FILE;
            }
            if (!TextUtils.isEmpty(str)) {
                Log.d(TAG, "mimoji void updateMimojiVideoCache[]");
                StringBuilder sb = new StringBuilder();
                sb.append(Storage.DIRECTORY);
                sb.append(File.separator);
                sb.append(FileUtils.createtFileName("MIMOJI_", "mp4"));
                String sb2 = sb.toString();
                try {
                    FileUtils.copyFile(new File(str), new File(sb2));
                    startSaveToLocal(sb2);
                } catch (IOException e) {
                    String str2 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("mimoji void updateMimojiVideoCache[] ");
                    sb3.append(e.getMessage());
                    Log.e(str2, sb3.toString());
                }
                deleteMimojiCache();
            }
        }
    }

    public /* synthetic */ void O00oO0o() {
        deleteMimojiCache();
        startSaveToLocal(null);
    }

    public /* synthetic */ void O00oO0oO() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
    }

    public /* synthetic */ void O00oO0oo() {
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.restoreWindowBrightness();
        }
        FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stopScreenLight: protocol = ");
        sb.append(fullScreenProtocol);
        sb.append(", mHandler = ");
        sb.append(this.mHandler);
        Log.d(str, sb.toString());
        if (fullScreenProtocol != null) {
            fullScreenProtocol.hideScreenLight();
        }
    }

    public void cancelFocus(boolean z) {
        if (isAlive() && isFrameAvailable() && getCameraState() != 0) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                if (z) {
                    camera2Proxy.setFocusMode(4);
                }
                this.mCamera2Device.cancelFocus(this.mModuleIndex);
            }
            if (getCameraState() != 3) {
                setCameraState(1);
            }
        }
    }

    public void checkDisplayOrientation() {
        if (isCreated()) {
            super.checkDisplayOrientation();
            MainContentProtocol mainContentProtocol = this.mMainProtocol;
            if (mainContentProtocol != null) {
                mainContentProtocol.setCameraDisplayOrientation(this.mCameraDisplayOrientation);
            }
            FocusManager2 focusManager2 = this.mFocusManager;
            if (focusManager2 != null) {
                focusManager2.setDisplayOrientation(this.mCameraDisplayOrientation);
            }
        }
    }

    public void closeCamera() {
        FlowableEmitter flowableEmitter = this.mMetaDataFlowableEmitter;
        if (flowableEmitter != null) {
            flowableEmitter.onComplete();
        }
        Disposable disposable = this.mMetaDataDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.getCameraScreenNail().setExternalFrameProcessor(null);
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setFocusCallback(null);
            this.mCamera2Device.setErrorCallback(null);
            this.mCamera2Device.setMetaDataCallback(null);
            this.mCamera2Device.stopPreviewCallback(true);
            this.mCamera2Device = null;
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(false);
            this.mFocusManager.destroy();
        }
    }

    /* access modifiers changed from: protected */
    public void consumePreference(int... iArr) {
        int length = iArr.length;
        for (int i = 0; i < length; i++) {
            int i2 = iArr[i];
            if (i2 == 1) {
                updatePictureAndPreviewSize();
            } else if (i2 == 2) {
                updateFilter();
            } else if (i2 == 3) {
                updateFocusArea();
            } else if (i2 == 5) {
                updateFace();
            } else if (i2 == 50) {
                continue;
            } else if (i2 == 58) {
                updateBackSoftLightPreference();
            } else if (i2 == 66) {
                updateThermalLevel();
            } else if (i2 == 72) {
                updateGif();
            } else if (i2 == 19) {
                updateFpsRange();
            } else if (i2 == 20) {
                continue;
            } else if (i2 == 24) {
                applyZoomRatio();
            } else if (i2 == 25) {
                focusCenter();
            } else if (i2 == 34) {
                continue;
            } else if (i2 == 35) {
                updateDeviceOrientation();
            } else if (!(i2 == 42 || i2 == 43)) {
                if (i2 == 54) {
                    updateLiveRelated();
                } else if (i2 != 55) {
                    switch (i2) {
                        case 9:
                            updateAntiBanding(CameraSettings.getAntiBanding());
                            break;
                        case 10:
                            updateFlashPreference();
                            break;
                        case 11:
                            continue;
                        case 12:
                            setEvValue();
                            break;
                        case 13:
                            updateBeauty();
                            break;
                        case 14:
                            updateFocusMode();
                            break;
                        default:
                            switch (i2) {
                                case 29:
                                    updateExposureMeteringMode();
                                    break;
                                case 30:
                                    continue;
                                case 31:
                                    updateVideoStabilization();
                                    break;
                                default:
                                    switch (i2) {
                                        case 46:
                                        case 48:
                                            break;
                                        case 47:
                                            updateUltraWideLDC();
                                            break;
                                        default:
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("no consumer for this updateType: ");
                                            sb.append(i2);
                                            throw new RuntimeException(sb.toString());
                                    }
                            }
                    }
                } else {
                    updateModuleRelated();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean enableFaceDetection() {
        return DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_FACE_DETECTION, getResources().getBoolean(R.bool.pref_camera_facedetection_default));
    }

    public void fillFeatureControl(StartControl startControl) {
        super.fillFeatureControl(startControl);
        if (startControl.mTargetMode == 184) {
            startControl.getFeatureDetail().addFragmentInfo(R.id.mimoji_full_screen, 65523);
        }
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return CameraCapabilities.SESSION_OPERATION_MODE_MIMOJI;
    }

    public String getTag() {
        return TAG;
    }

    public void initializeCapabilities() {
        super.initializeCapabilities();
        this.mContinuousFocusSupported = Util.isSupported(4, this.mCameraCapabilities.getSupportedFocusModes());
        this.mMaxFaceCount = this.mCameraCapabilities.getMaxFaceCount();
    }

    /* access modifiers changed from: protected */
    public boolean is3ALocked() {
        return this.m3ALocked;
    }

    /* access modifiers changed from: protected */
    public boolean isAEAFLockSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isCameraSwitchingDuringZoomingAllowed() {
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            int i = this.mModuleIndex;
            if (i == 174 && !CameraSettings.isMacroModeEnabled(i) && isBackCamera()) {
                return true;
            }
        }
        return false;
    }

    public boolean isDoingAction() {
        MimojiFullScreenProtocol mimojiFullScreenProtocol = (MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249);
        if (!isRecording() && getCameraState() != 3) {
            MimojiVideoEditor mimojiVideoEditor = this.mMimojiVideoEditor;
            if ((mimojiVideoEditor == null || !mimojiVideoEditor.isComposing()) && (mimojiFullScreenProtocol == null || !mimojiFullScreenProtocol.isMimojiRecordPreviewShowing())) {
                return false;
            }
        }
        return true;
    }

    public boolean isFaceDetectStarted() {
        return this.mFaceDetectionStarted;
    }

    public boolean isNeedMute() {
        return isRecording();
    }

    public boolean isRecording() {
        MimojiAvatarEngine2 mimojiAvatarEngine22 = this.mimojiAvatarEngine2;
        return mimojiAvatarEngine22 != null && mimojiAvatarEngine22.isRecording();
    }

    public boolean isSelectingCapturedResult() {
        MimojiFullScreenProtocol mimojiFullScreenProtocol = (MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249);
        return mimojiFullScreenProtocol != null && mimojiFullScreenProtocol.isMimojiRecordPreviewShowing();
    }

    public boolean isShowCaptureButton() {
        return isSupportFocusShoot();
    }

    public boolean isSupportFocusShoot() {
        return DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPhoto() && DataRepository.dataItemGlobal().isGlobalSwitchOn("pref_camera_focus_shoot_key");
    }

    public boolean isUnInterruptable() {
        return false;
    }

    public boolean isUseFaceInfo() {
        return false;
    }

    public boolean isZoomEnabled() {
        return getCameraState() != 3 && !isFrontCamera() && !DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate() && isFrameAvailable();
    }

    /* access modifiers changed from: protected */
    public void lockAEAF() {
        Log.d(TAG, "lockAEAF");
        if (this.mAeLockSupported) {
            this.mCamera2Device.setAELock(true);
        }
        this.mFocusManager.setAeAwbLock(true);
        this.m3ALocked = true;
    }

    public boolean multiCapture() {
        return false;
    }

    public void notifyError() {
        if (CameraSchedulers.isOnMainThread() && isRecording()) {
            stopVideoRecording(1);
        }
        super.notifyError();
    }

    public void notifyFocusAreaUpdate() {
        updatePreferenceTrampoline(3);
    }

    public boolean onBackPressed() {
        if (!isFrameAvailable()) {
            return isSelectingCapturedResult();
        }
        if (this.mimojiAvatarEngine2 != null && DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
            ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onMimojiCreateBack();
            CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_CREATE_SOFT_BACK, BaseEvent.CREATE);
            return true;
        } else if (!isRecording()) {
            return super.onBackPressed();
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
                this.mLastBackPressedTime = currentTimeMillis;
                ToastUtils.showToast((Context) this.mActivity, (int) R.string.record_back_pressed_hint, 80);
            } else {
                stopVideoRecording(2);
            }
            return true;
        }
    }

    public void onBluetoothHeadsetConnected() {
        super.onBluetoothHeadsetConnected();
        if (!isRecording()) {
            BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
            if (bluetoothHeadset != null) {
                bluetoothHeadset.startBluetoothSco(getModuleIndex());
            }
        }
    }

    public void onBluetoothHeadsetDisconnected() {
        super.onBluetoothHeadsetDisconnected();
        BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
        if (bluetoothHeadset != null) {
            bluetoothHeadset.stopBluetoothSco(getModuleIndex());
        }
    }

    public void onBroadcastReceived(Context context, Intent intent) {
        if (intent != null && isAlive()) {
            if (CameraIntentManager.ACTION_SPEECH_SHUTTER.equals(intent.getAction())) {
                Log.d(TAG, "on Receive speech shutter broadcast action intent");
                onShutterButtonClick(110);
            }
            super.onBroadcastReceived(context, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        super.onCameraOpened();
        initializeFocusManager();
        updatePreferenceTrampoline(UpdateConstant.FUN_TYPES_INIT);
        initMimojiEngine();
        startPreview();
        this.mOnResumeTime = SystemClock.uptimeMillis();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
        initMetaParser();
        updateMimojiVideoCache();
        BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
        if (bluetoothHeadset != null) {
            bluetoothHeadset.startBluetoothSco(getModuleIndex());
        }
    }

    public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
    }

    public void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4) {
        setWaterMark();
        this.mActivity.getCameraScreenNail().animateCapture(this.mOrientation);
        playCameraSound(0);
        this.mimojiAvatarEngine2.onCaptureImage();
    }

    public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
        return null;
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        this.mHandler = new MainHandler(this.mActivity.getMainLooper());
        onCameraOpened();
        this.mAudioMonitorPlayer = new AudioMonitorPlayer();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
        this.mActivity.getSensorStateManager().setRotationVectorEnabled(true);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.post(O00000Oo.INSTANCE);
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessage(45);
        }
        this.mActivity.getSensorStateManager().setRotationVectorEnabled(false);
        this.mActivity.getSensorStateManager().setTTARSensorEnabled(false);
    }

    public void onFaceDetected(CameraHardwareFace[] cameraHardwareFaceArr, FaceAnalyzeInfo faceAnalyzeInfo, Rect rect) {
        if (isCreated() && cameraHardwareFaceArr != null) {
            if (!C0124O00000oO.Oo00o() || cameraHardwareFaceArr.length <= 0 || cameraHardwareFaceArr[0].faceType != 64206) {
                if (!this.mMainProtocol.setFaces(1, cameraHardwareFaceArr, getActiveArraySize(), rect)) {
                    return;
                }
            } else if (this.mObjectTrackingStarted) {
                this.mMainProtocol.setFaces(3, cameraHardwareFaceArr, getActiveArraySize(), rect);
            }
            MimojiAvatarEngine2 mimojiAvatarEngine22 = this.mimojiAvatarEngine2;
            if (mimojiAvatarEngine22 != null && mimojiAvatarEngine22.isOnCreateMimoji()) {
                this.mMainProtocol.lightingDetectFace(cameraHardwareFaceArr, true);
                this.misFaceLocationOk = this.mMainProtocol.isFaceLocationOK();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0019, code lost:
        if (r0 != 3) goto L_0x00a0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onFocusStateChanged(FocusTask focusTask) {
        if (isCreated() && !isDeparted()) {
            int focusTrigger = focusTask.getFocusTrigger();
            if (focusTrigger != 1) {
                if (focusTrigger == 2) {
                    if (focusTask.isIsDepthFocus()) {
                        return;
                    }
                }
                String str = null;
                if (focusTask.isFocusing()) {
                    str = "onAutoFocusMoving start";
                }
                if (Util.sIsDumpLog && str != null) {
                    Log.v(TAG, str);
                }
                if ((getCameraState() != 3 || focusTask.getFocusTrigger() == 3) && !this.m3ALocked) {
                    this.mFocusManager.onFocusResult(focusTask);
                }
            } else {
                Log.v(TAG, String.format(Locale.ENGLISH, "FocusTime=%1$dms focused=%2$b", new Object[]{Long.valueOf(focusTask.getElapsedTime()), Boolean.valueOf(focusTask.isSuccess())}));
                if (!this.mFocusManager.isFocusingSnapOnFinish() && getCameraState() != 3) {
                    setCameraState(1);
                }
                this.mFocusManager.onFocusResult(focusTask);
                this.mActivity.getSensorStateManager().reset();
                if (focusTask.isSuccess() && this.m3ALocked) {
                    this.mCamera2Device.lockExposure(true);
                }
            }
        }
    }

    public void onHostStopAndNotifyActionStop() {
        if (isRecording()) {
            stopVideoRecording(1);
        }
        if (!isSaving()) {
            doLaterReleaseIfNeed();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        int i2;
        if (this.mPaused) {
            return true;
        }
        boolean z = false;
        if (!isFrameAvailable()) {
            return false;
        }
        if (!(i == 24 || i == 25)) {
            if (i == 27 || i == 66) {
                if (keyEvent.getRepeatCount() == 0) {
                    if (isSelectingCapturedResult()) {
                        ((MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249)).startMimojiRecordSaving();
                    } else {
                        if (!Util.isFingerPrintKeyEvent(keyEvent)) {
                            i2 = 40;
                        } else if (CameraSettings.isFingerprintCaptureEnable()) {
                            i2 = 30;
                        }
                        performKeyClicked(i2, getString(R.string.pref_camera_volumekey_function_entryvalue_shutter), keyEvent.getRepeatCount(), true);
                    }
                    return true;
                }
                return super.onKeyDown(i, keyEvent);
            } else if (!(i == 87 || i == 88)) {
                if (i != 700) {
                    if (i == 701 && isRecording() && !isPostProcessing()) {
                        if (!isFrontCamera()) {
                            return false;
                        }
                    }
                    return super.onKeyDown(i, keyEvent);
                }
                if (isRecording() && !isPostProcessing()) {
                    if (!isBackCamera()) {
                        return false;
                    }
                }
                return super.onKeyDown(i, keyEvent);
                stopVideoRecording(0);
                return super.onKeyDown(i, keyEvent);
            }
        }
        if (!isSelectingCapturedResult()) {
            CloneChooser cloneChooser = (CloneChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(416);
            if (cloneChooser == null || !cloneChooser.isShow()) {
                if (i == 24 || i == 88) {
                    z = true;
                }
                if (handleVolumeKeyEvent(z, true, keyEvent.getRepeatCount(), keyEvent.getDevice().isExternal())) {
                    return true;
                }
            } else {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("ignore onKeyDown ");
                sb.append(i);
                Log.d(str, sb.toString());
                return false;
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 4) {
            if (i == 27 || i == 66) {
                return true;
            }
        } else if (((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromKeyBack()) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    public void onMimojiCaptureCallback() {
        setCameraState(1);
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.onParallelImagePostProcStart();
        }
        if (isFrontCamera() && this.mCamera2Device.isNeedFlashOn()) {
            stopScreenLight();
        }
    }

    public void onMimojiCreateCompleted(boolean z) {
        setCameraState(1);
    }

    public void onNewUriArrived(Uri uri, String str) {
        super.onNewUriArrived(uri, str);
        if (isAlive()) {
            MimojiFullScreenProtocol mimojiFullScreenProtocol = (MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249);
            if (mimojiFullScreenProtocol != null) {
                mimojiFullScreenProtocol.onMimojiSaveToLocalFinished(uri);
            }
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        setOrientation(i, i2);
        this.mDeviceOrientation = i;
        MimojiAvatarEngine2 mimojiAvatarEngine22 = this.mimojiAvatarEngine2;
        if (mimojiAvatarEngine22 != null) {
            mimojiAvatarEngine22.onDeviceRotationChange(i);
        }
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.removeMessages();
        }
        getActivity();
        tryRemoveCountDownMessage();
        this.mActivity.getSensorStateManager().reset();
        resetScreenOn();
        closeCamera();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
        if (bluetoothHeadset != null) {
            bluetoothHeadset.stopBluetoothSco(getModuleIndex());
        }
    }

    public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
    }

    public void onPictureTakenFinished(boolean z) {
    }

    public boolean onPictureTakenImageConsumed(Image image, TotalCaptureResult totalCaptureResult) {
        return false;
    }

    public void onPreviewLayoutChanged(Rect rect) {
        this.mActivity.onLayoutChange(rect);
        if (this.mFocusManager != null && this.mActivity.getCameraScreenNail() != null) {
            this.mFocusManager.setRenderSize(this.mActivity.getCameraScreenNail().getRenderWidth(), this.mActivity.getCameraScreenNail().getRenderHeight());
            this.mFocusManager.setPreviewSize(rect.width(), rect.height());
        }
    }

    public void onPreviewMetaDataUpdate(CaptureResult captureResult) {
        if (captureResult != null) {
            super.onPreviewMetaDataUpdate(captureResult);
            FlowableEmitter flowableEmitter = this.mMetaDataFlowableEmitter;
            if (flowableEmitter != null) {
                flowableEmitter.onNext(captureResult);
            }
        }
    }

    public void onPreviewPixelsRead(byte[] bArr, int i, int i2) {
        super.onPreviewPixelsRead(bArr, i, i2);
    }

    public void onPreviewSessionClosed(CameraCaptureSession cameraCaptureSession) {
    }

    public void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession) {
        if (!isTextureExpired() || !retryOnceIfCameraError(this.mHandler)) {
            this.mHandler.sendEmptyMessage(51);
        } else {
            Log.d(TAG, "sessionFailed due to surfaceTexture expired, retry");
        }
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        if (cameraCaptureSession != null && isAlive()) {
            this.mHandler.sendEmptyMessage(9);
            previewWhenSessionSuccess();
        }
    }

    public void onPreviewSizeChanged(int i, int i2) {
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setPreviewSize(i, i2);
        }
    }

    public void onResume() {
        super.onResume();
        keepScreenOnAwhile();
    }

    public void onReviewCancelClicked() {
        getActivity().setVolumeControlStream(this.mOldOriginVolumeStream);
        ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onFinish();
        if (!doLaterReleaseIfNeed()) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                if (camera2Proxy.isSessionReady()) {
                    resumePreview();
                } else {
                    startPreview();
                }
            }
        }
    }

    public void onReviewDoneClicked() {
        Log.d(TAG, "mimoji void onReviewDoneClicked[]");
        getActivity().setVolumeControlStream(this.mOldOriginVolumeStream);
        ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onFinish();
        announceForAccessibility(R.string.accessibility_camera_shutter_finish);
        new Thread(new O000000o(this)).start();
        if (!doLaterReleaseIfNeed()) {
            if (this.mCamera2Device.isSessionReady()) {
                resumePreview();
            } else {
                startPreview();
            }
        }
    }

    public void onShineChanged(int i) {
        if (i == 239) {
            updatePreferenceInWorkThread(13);
            return;
        }
        throw new RuntimeException("unknown configItem changed");
    }

    public void onShutterButtonClick(int i) {
        Log.u(TAG, "onShutterButtonClick");
        if (i == 110) {
            this.mActivity.onUserInteraction();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.hideRecommendDescTip(FragmentTopAlert.TIP_SPEECH_SHUTTER_DESC);
            }
        }
        setTriggerMode(i);
        if (isRecording()) {
            Log.u(TAG, "onShutterButtonClick stopVideoRecording");
            stopVideoRecording(0);
        } else if (!isDoingAction()) {
            if (this.mimojiAvatarEngine2 == null) {
                Log.e(TAG, "onShutterButtonClick  mimojiAvatarEngine2 NULL");
            } else if (DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPreview()) {
                BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
                if (backStack != null) {
                    backStack.handleBackStackFromShutter();
                }
                if (DataRepository.dataItemLive().getMimojiStatusManager2().getMimojiRecordState() == 1) {
                    Log.u(TAG, "onShutterButtonClick startVideoRecording");
                    startVideoRecording();
                } else if (turnOnFlash()) {
                    setCameraState(3);
                    onMimojiCapture();
                }
                HashMap hashMap = new HashMap();
                hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
                trackGeneralInfo(hashMap, 1, false, this.mBeautyValues, false, 0);
            } else if (DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate()) {
                Log.d(TAG, "start create mimoji");
                if (this.mIsLowLight) {
                    Log.d(TAG, "mimoji create low light!");
                } else if (this.mimojiAvatarEngine2.onCreateCapture()) {
                    Log.d(TAG, "create mimoji success");
                    setCameraState(3);
                }
            }
        }
    }

    public void onShutterButtonFocus(boolean z, int i) {
    }

    public boolean onShutterButtonLongClick() {
        return false;
    }

    public void onShutterButtonLongClickCancel(boolean z) {
    }

    public void onSingleTapUp(int i, int i2, boolean z) {
        if (!this.mPaused && !this.mDisableSingleTapUp && this.mCamera2Device != null && !hasCameraException() && this.mCamera2Device.isSessionReady() && isInTapableRect(i, i2) && getCameraState() != 3 && getCameraState() != 0) {
            if (!isFrameAvailable()) {
                Log.w(TAG, "onSingleTapUp: frame not available");
            } else if (!isFrontCamera() || !this.mActivity.isScreenSlideOff()) {
                BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
                if (backStack != null && !backStack.handleBackStackFromTapDown(i, i2)) {
                    this.mMainProtocol.setFocusViewType(true);
                    this.mTouchFocusStartingTime = System.currentTimeMillis();
                    Point point = new Point(i, i2);
                    mapTapCoordinate(point);
                    unlockAEAF();
                    this.mFocusManager.onSingleTapUp(point.x, point.y, z);
                }
            }
        }
    }

    public void onStop() {
        super.onStop();
        DataRepository.dataItemLive().getMimojiStatusManager2().setCurrentMimojiBgInfo(null);
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /* access modifiers changed from: protected */
    public void onThermalConstrained() {
        super.onThermalConstrained();
        if (isRecording()) {
            stopVideoRecording(1);
            onReviewCancelClicked();
        }
    }

    public void onThumbnailClicked(View view) {
        if (!isDoingAction() && this.mActivity.getThumbnailUpdater().getThumbnail() != null) {
            this.mActivity.gotoGallery();
        }
    }

    public boolean onWaitingFocusFinished() {
        return !isBlockSnap() && isAlive();
    }

    public void onZoomingActionEnd(int i) {
    }

    public void onZoomingActionStart(int i) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && topAlert.isExtraMenuShowing()) {
            topAlert.hideExtraMenu();
        }
    }

    public void pausePreview() {
        if (this.mCamera2Device.getFlashMode() == 2 || this.mCamera2Device.getFlashMode() == 5) {
            this.mCamera2Device.forceTurnFlashOffAndPausePreview();
        } else {
            this.mCamera2Device.pausePreview();
        }
        setCameraState(0);
    }

    /* access modifiers changed from: protected */
    public void performKeyClicked(int i, String str, int i2, boolean z) {
        if (i2 == 0 && z) {
            if (isIgnoreTouchEvent()) {
                Log.w(TAG, "ignore volume key");
                return;
            }
            onShutterButtonClick(i);
        }
    }

    public void playFocusSound(int i) {
        playCameraSound(i);
    }

    public void preTransferOrientation(int i, int i2) {
        super.preTransferOrientation(i, i2);
        this.mDeviceOrientation = i;
    }

    public void registerProtocol() {
        Camera camera;
        int[] iArr;
        ImplFactory implFactory;
        super.registerProtocol();
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(167, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(167, this);
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            implFactory = getActivity().getImplFactory();
            camera = getActivity();
            iArr = new int[]{164, 174, 234, 212};
        } else {
            implFactory = getActivity().getImplFactory();
            camera = getActivity();
            iArr = new int[]{164, 234, 212};
        }
        implFactory.initAdditional(camera, iArr);
    }

    public void resumePreview() {
        previewWhenSessionSuccess();
    }

    public void setCameraStatePublic(int i) {
        setCameraState(i);
    }

    public void setDisableSingleTapUp(boolean z) {
        this.mDisableSingleTapUp = z;
    }

    public boolean shouldCaptureDirectly() {
        return false;
    }

    public boolean shouldReleaseLater() {
        return isRecording() || isSelectingCapturedResult();
    }

    public void startFaceDetection() {
        if (this.mFaceDetectionEnabled && !this.mFaceDetectionStarted && isAlive() && this.mMaxFaceCount > 0) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                this.mFaceDetectionStarted = true;
                camera2Proxy.startFaceDetection();
                updateFaceView(true, true);
            }
        }
    }

    public void startFocus() {
        if (isDeviceAlive() && isFrameAvailable()) {
            Log.v(TAG, "startFocus");
            if (this.mFocusOrAELockSupported) {
                this.mCamera2Device.startFocus(FocusTask.create(1), this.mModuleIndex);
            } else {
                this.mCamera2Device.resumePreview();
            }
        }
    }

    public void startPreview() {
        if (isDeviceAlive()) {
            this.mCamera2Device.setFocusCallback(this);
            this.mCamera2Device.setMetaDataCallback(this);
            this.mCamera2Device.setErrorCallback(this.mErrorCallback);
            this.mCamera2Device.setPictureSize(this.mPictureSize);
            this.mCamera2Device.setPreviewSize(this.mPreviewSize);
            this.mQuality = Util.convertSizeToQuality(this.mPreviewSize);
            this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            Log.d(TAG, "MimojiModule, startPreview");
            checkDisplayOrientation();
            boolean isFrontCamera = isFrontCamera();
            Surface surface = new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture());
            this.mActivity.getCameraScreenNail().setExternalFrameProcessor(this.mimojiAvatarEngine2);
            if (!isSelectingCapturedResult()) {
                this.mCamera2Device.startPreviewSession(surface, 1, 0, null, getOperatingMode(), false, this);
            }
        }
    }

    public void startSaveToLocal(String str) {
        MimojiAvatarEngine2 mimojiAvatarEngine22 = this.mimojiAvatarEngine2;
        if (mimojiAvatarEngine22 == null || mimojiAvatarEngine22.isRecordStopping()) {
            Log.e(TAG, "mimoji void startSaveToLocal[] error");
            return;
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mimoji void startSaveToLocal[]");
        sb.append(str);
        Log.d(str2, sb.toString());
        if (TextUtils.isEmpty(str)) {
            str = ((MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249)).getMimojiVideoSavePath();
        }
        if (TextUtils.isEmpty(str)) {
            Log.e(TAG, "mimoji void startSaveToLocal[videoSavePath] null");
            return;
        }
        CameraSize cameraSize = this.mPreviewSize;
        ContentValues genContentValues = Util.genContentValues(2, str, cameraSize.width, cameraSize.height);
        genContentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        getActivity().getImageSaver().addVideo(genContentValues.getAsString("_data"), genContentValues, true);
    }

    public void stopFaceDetection(boolean z) {
        if (this.mFaceDetectionEnabled && this.mFaceDetectionStarted) {
            this.mFaceDetectionStarted = false;
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.stopFaceDetection();
            }
            this.mMainProtocol.setActiveIndicator(2);
            updateFaceView(false, z);
        }
    }

    public void stopObjectTracking(boolean z) {
    }

    public void stopVideoRecording(int i) {
        listenPhoneState(false);
        boolean z = i != 0;
        setCameraAudioRestriction(false);
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
        if (CameraSettings.isGifOn()) {
            this.mimojiAvatarEngine2.onRecordStop(i);
            if (System.currentTimeMillis() - this.mRecordTime > 1000) {
                showPreview();
            } else {
                this.mRecordTime = System.currentTimeMillis();
                ToastUtils.showToast((Context) this.mActivity, (int) R.string.mimoji_gif_record_time_short, 80);
                onReviewCancelClicked();
            }
            if (z) {
                this.mRecordTime = System.currentTimeMillis();
            }
        } else {
            BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
            if (bluetoothHeadset != null) {
                bluetoothHeadset.startBluetoothSco(getModuleIndex());
            }
            if (z) {
                this.mRecordTime = System.currentTimeMillis();
                this.mimojiAvatarEngine2.onRecordStop(i);
                onReviewCancelClicked();
            } else if (System.currentTimeMillis() - this.mRecordTime > 500 && !this.mimojiAvatarEngine2.isRecordStopping()) {
                this.mRecordTime = System.currentTimeMillis();
                this.mimojiAvatarEngine2.onRecordStop(i);
                showPreview();
            }
        }
    }

    public boolean turnOnFlash() {
        if (!this.mCamera2Device.isNeedFlashOn() || 101 != this.mCamera2Device.getFlashMode()) {
            return true;
        }
        return startScreenLight(Util.getScreenLightColor(SystemProperties.getInt("camera_screen_light_wb", 0)), CameraSettings.getScreenLightBrightness());
    }

    public void unRegisterModulePersistProtocol() {
        super.unRegisterModulePersistProtocol();
        getActivity().getImplFactory().detachModulePersistent();
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(167, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        getActivity().getImplFactory().detachAdditional();
    }

    /* access modifiers changed from: protected */
    public void unlockAEAF() {
        Log.d(TAG, "unlockAEAF");
        this.m3ALocked = false;
        if (this.mAeLockSupported) {
            this.mCamera2Device.setAELock(false);
        }
        this.mFocusManager.setAeAwbLock(false);
    }

    /* access modifiers changed from: protected */
    public void updateFace() {
        boolean enableFaceDetection = enableFaceDetection();
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        if (mainContentProtocol != null) {
            boolean z = !enableFaceDetection || !this.mShowFace;
            mainContentProtocol.setSkipDrawFace(z);
        }
        if (enableFaceDetection) {
            if (!this.mFaceDetectionEnabled) {
                this.mFaceDetectionEnabled = true;
                startFaceDetection();
            }
        } else if (this.mFaceDetectionEnabled) {
            stopFaceDetection(true);
            this.mFaceDetectionEnabled = false;
        }
    }

    public void updateFlashPreference() {
        setFlashMode(DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex));
    }

    /* access modifiers changed from: protected */
    public void updateFocusArea() {
        if (this.mCamera2Device == null) {
            Log.e(TAG, "updateFocusArea: null camera device");
            return;
        }
        Rect cropRegion = getCropRegion();
        Rect activeArraySize = getActiveArraySize();
        this.mActivity.getSensorStateManager().setFocusSensorEnabled(this.mFocusManager.getMeteringAreas(cropRegion, activeArraySize) != null);
        this.mCamera2Device.setAERegions(this.mFocusManager.getMeteringAreas(cropRegion, activeArraySize));
        if (this.mFocusAreaSupported) {
            this.mCamera2Device.setAFRegions(this.mFocusManager.getFocusAreas(cropRegion, activeArraySize));
        } else {
            this.mCamera2Device.resumePreview();
        }
    }
}
