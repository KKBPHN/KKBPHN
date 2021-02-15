package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.util.Range;
import android.util.SparseArray;
import android.view.Surface;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.camera.AutoLockManager;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.AutoFocus;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.constant.UpdateConstant.UpdateType;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.VMFeature;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.log.Log;
import com.android.camera.module.encoder.MediaAudioEncoder;
import com.android.camera.module.encoder.MediaEncoder;
import com.android.camera.module.encoder.MediaEncoder.MediaEncoderListener;
import com.android.camera.module.encoder.MediaMuxerWrapper;
import com.android.camera.module.encoder.MediaVideoEncoder;
import com.android.camera.module.impl.ImplFactory;
import com.android.camera.module.interceptor.BaseModuleInterceptor;
import com.android.camera.module.interceptor.CaptureInterceptor;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.StartControlFeatureDetail;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.CloneChooser;
import com.android.camera.protocol.ModeProtocol.KaleidoscopeProtocol;
import com.android.camera.protocol.ModeProtocol.LiveFilmChooser;
import com.android.camera.protocol.ModeProtocol.LiveSpeedChanges;
import com.android.camera.protocol.ModeProtocol.LiveVVChooser;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.StickerProtocol;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.MiLive;
import com.android.camera.storage.Storage;
import com.android.camera.ui.PopupManager;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.CameraCapabilities;
import com.android.gallery3d.ui.GLCanvas;
import com.android.gallery3d.ui.GLCanvasImpl;
import com.xiaomi.camera.core.ParallelTaskData;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class FunModule extends VideoBase implements StickerProtocol, LiveSpeedChanges, KaleidoscopeProtocol, PictureCallback, LifecycleOwner {
    private static final int FRAME_RATE = 30;
    private static final long START_OFFSET_MS = 450;
    private final float[] SPEEDS = {3.0f, 2.0f, 1.0f, 0.5f, 0.33f};
    private V6CameraGLSurfaceView mCameraView;
    private CountDownTimer mCountDownTimer;
    private SparseArray mInterceptors;
    private MediaMuxerWrapper mLastMuxer;
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    private MediaAudioEncoder mMediaAudioEncoder;
    private final MediaEncoderListener mMediaEncoderListener = new EncoderListener(this);
    private MediaVideoEncoder mMediaVideoEncoder;
    private MediaMuxerWrapper mMuxer;
    private ArrayList mPendingSaveTaskList = new ArrayList();
    private int mQuality;
    private long mRequestStartTime;
    /* access modifiers changed from: private */
    public float mSpeed = 1.0f;

    class EncoderListener implements MediaEncoderListener {
        private WeakReference mModule;

        public EncoderListener(FunModule funModule) {
            this.mModule = new WeakReference(funModule);
        }

        public void onPrepared(MediaEncoder mediaEncoder) {
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPrepared: encoder=");
            sb.append(mediaEncoder);
            Log.v(str, sb.toString());
        }

        public void onStopped(MediaEncoder mediaEncoder, boolean z) {
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onStopped: encoder=");
            sb.append(mediaEncoder);
            Log.v(str, sb.toString());
            if (z) {
                FunModule funModule = (FunModule) this.mModule.get();
                if (funModule != null) {
                    funModule.executeSaveTask(true);
                }
            }
        }
    }

    final class SaveVideoTask {
        public ContentValues contentValues;
        public String videoPath;

        public SaveVideoTask(String str, ContentValues contentValues2) {
            this.videoPath = str;
            this.contentValues = contentValues2;
        }
    }

    public FunModule() {
        super(FunModule.class.getSimpleName());
    }

    private String convertToFilePath(FileDescriptor fileDescriptor) {
        return null;
    }

    private boolean initializeRecorder() {
        String str;
        if (this.mCamera2Device == null) {
            Log.e(VideoBase.TAG, "initializeRecorder: null camera");
            return false;
        }
        Log.v(VideoBase.TAG, "initializeRecorder");
        closeVideoFileDescriptor();
        if (isCaptureIntent()) {
            parseIntent(this.mActivity.getIntent());
        }
        ParcelFileDescriptor parcelFileDescriptor = this.mVideoFileDescriptor;
        if (parcelFileDescriptor != null) {
            str = convertToFilePath(parcelFileDescriptor.getFileDescriptor());
        } else {
            this.mCurrentVideoValues = genContentValues(this.mOutputFormat, -1, null, false, true);
            this.mCurrentVideoFilename = this.mCurrentVideoValues.getAsString("_data");
            str = this.mCurrentVideoFilename;
        }
        this.mOrientationCompensationAtRecordStart = this.mOrientationCompensation;
        try {
            releaseLastMediaRecorder();
            this.mMuxer = new MediaMuxerWrapper(str);
            MediaVideoEncoder mediaVideoEncoder = new MediaVideoEncoder(getActivity().getGLView().getEGLContext14(), this.mMuxer, this.mMediaEncoderListener, this.mVideoSize.width, this.mVideoSize.height);
            this.mMediaVideoEncoder = mediaVideoEncoder;
            this.mMediaAudioEncoder = new MediaAudioEncoder(this.mMuxer, this.mMediaEncoderListener);
            this.mMediaVideoEncoder.setRecordSpeed(this.mSpeed);
            this.mMediaAudioEncoder.setRecordSpeed(this.mSpeed);
            this.mMuxer.prepare();
            String str2 = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("rotation: ");
            sb.append(this.mOrientationCompensation);
            Log.d(str2, sb.toString());
            this.mMuxer.setOrientationHint(this.mOrientationCompensation);
            return true;
        } catch (IOException e) {
            Log.e(VideoBase.TAG, "initializeRecorder: ", (Throwable) e);
            return false;
        }
    }

    private boolean isEisOn() {
        return isBackCamera() && C0122O00000o.instance().OO0OOo() && CameraSettings.isMovieSolidOn();
    }

    private boolean isSupportShortVideoBeautyBody() {
        return isBackCamera() && C0122O00000o.instance().isSupportShortVideoBeautyBody();
    }

    private void onStartRecorderFail() {
        enableCameraControls(true);
        restoreOuterAudio();
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onFailed();
        }
    }

    private void onStartRecorderSucceed() {
        float min;
        float ultraTeleMinZoomRatio;
        enableCameraControls(true);
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT && isBackCamera()) {
            updateZoomRatioToggleButtonState(true);
            if (isUltraWideBackCamera()) {
                setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                min = 2.0f;
            } else {
                if (isAuxCamera()) {
                    ultraTeleMinZoomRatio = HybridZoomingSystem.getTeleMinZoomRatio();
                } else if (isUltraTeleCamera()) {
                    ultraTeleMinZoomRatio = HybridZoomingSystem.getUltraTeleMinZoomRatio();
                } else {
                    setMinZoomRatio(1.0f);
                    min = Math.min(6.0f, this.mCameraCapabilities.getMaxZoomRatio());
                }
                setMinZoomRatio(ultraTeleMinZoomRatio);
                setVideoMaxZoomRatioByTele();
            }
            setMaxZoomRatio(min);
        }
        this.mActivity.sendBroadcast(new Intent(BaseModule.START_VIDEO_RECORDING_ACTION));
        this.mMediaRecorderRecording = true;
        this.mRecordingStartTime = SystemClock.uptimeMillis();
        listenPhoneState(true);
        updateRecordingTime();
        keepScreenOn();
        AutoLockManager.getInstance(this.mActivity).removeMessage();
        HashMap hashMap = new HashMap();
        hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
        trackGeneralInfo(hashMap, 1, false, this.mBeautyValues, false, 0);
    }

    private void releaseLastMediaRecorder() {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("releaseLastMediaRecorder ");
        sb.append(this.mLastMuxer != null);
        Log.d(str, sb.toString());
        MediaMuxerWrapper mediaMuxerWrapper = this.mLastMuxer;
        if (mediaMuxerWrapper != null) {
            mediaMuxerWrapper.join();
            this.mLastMuxer = null;
        }
    }

    private void releaseMediaRecorder() {
        Log.v(VideoBase.TAG, "releaseMediaRecorder");
        MediaMuxerWrapper mediaMuxerWrapper = this.mMuxer;
        if (mediaMuxerWrapper != null) {
            this.mLastMuxer = mediaMuxerWrapper;
            cleanupEmptyFile();
        }
    }

    private void releaseResources() {
        closeCamera();
        releaseMediaRecorder();
        releaseLastMediaRecorder();
    }

    private void setVideoSize(int i, int i2) {
        this.mVideoSize = this.mCameraDisplayOrientation % 180 == 0 ? new CameraSize(i, i2) : new CameraSize(i2, i);
    }

    private boolean shouldApplyUltraWideLDC() {
        if (CameraSettings.shouldUltraWideVideoLDCBeVisibleInMode(this.mModuleIndex) && this.mActualCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
            return CameraSettings.isUltraWideVideoLDCEnabled();
        }
        return false;
    }

    private boolean startRecorder() {
        if (!initializeRecorder()) {
            Log.e(VideoBase.TAG, "fail to initialize recorder");
            return false;
        }
        long currentTimeMillis = 450 - (System.currentTimeMillis() - this.mRequestStartTime);
        if (currentTimeMillis < 0) {
            currentTimeMillis = 0;
        }
        boolean startRecording = this.mMuxer.startRecording(currentTimeMillis);
        if (!startRecording) {
            this.mMuxer.stopRecording();
            showConfirmMessage(R.string.confirm_recording_fail_title, R.string.confirm_recording_fail_recorder_busy_alert);
            releaseMediaRecorder();
        }
        return startRecording;
    }

    private void updateFilter() {
        int shaderEffect = CameraSettings.getShaderEffect();
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateFilter: 0x");
        sb.append(Integer.toHexString(shaderEffect));
        Log.v(str, sb.toString());
        EffectController.getInstance().setEffect(shaderEffect);
    }

    private void updateFpsRange() {
        Camera2Proxy camera2Proxy;
        Range range;
        boolean isMTKPlatform = C0124O00000oO.isMTKPlatform();
        Integer valueOf = Integer.valueOf(30);
        if (isMTKPlatform) {
            this.mCamera2Device.setVideoFpsRange(new Range(Integer.valueOf(5), valueOf));
            camera2Proxy = this.mCamera2Device;
            range = new Range(Integer.valueOf(5), valueOf);
        } else {
            this.mCamera2Device.setVideoFpsRange(new Range(valueOf, valueOf));
            camera2Proxy = this.mCamera2Device;
            range = new Range(valueOf, valueOf);
        }
        camera2Proxy.setFpsRange(range);
    }

    private void updateKaleidoscope() {
        EffectController.getInstance().setKaleidoscope(DataRepository.dataItemRunning().getComponentRunningKaleidoscope().getKaleidoscopeValue());
    }

    private void updatePictureAndPreviewSize() {
        this.mPreviewSize = Util.getOptimalPreviewSize(false, this.mBogusCameraId, this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class), (double) CameraSettings.getPreviewAspectRatio(16, 9), CameraSettings.getPreferVideoQuality(this.mActualCameraId, this.mModuleIndex) != 5 ? new CameraSize(1920, 1080) : new CameraSize(1280, Util.LIMIT_SURFACE_WIDTH));
        CameraSize cameraSize = this.mPreviewSize;
        updateCameraScreenNailSize(cameraSize.width, cameraSize.height);
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("previewSize: ");
        sb.append(this.mPreviewSize);
        Log.d(str, sb.toString());
    }

    private void updateUltraWideLDC() {
        this.mCamera2Device.setUltraWideLDC(shouldApplyUltraWideLDC());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0064, code lost:
        if (r5.mCameraCapabilities.isEISPreviewSupported() == false) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0093, code lost:
        if (r5.mActualCameraId == com.android.camera.module.loader.camera2.Camera2DataContainer.getInstance().getUltraWideCameraId()) goto L_0x0066;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateVideoStabilization() {
        if (isDeviceAlive()) {
            if (!CameraSettings.isVideoBokehOn() || !isFrontCamera()) {
                if (isEisOn()) {
                    String str = VideoBase.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("videoStabilization: EIS isEISPreviewSupported = ");
                    sb.append(this.mCameraCapabilities.isEISPreviewSupported());
                    Log.d(str, sb.toString());
                    this.mCamera2Device.setEnableEIS(true);
                    this.mCamera2Device.setEnableOIS(false);
                } else {
                    this.mCamera2Device.setEnableEIS(false);
                    this.mCamera2Device.setEnableOIS(true);
                    this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(false);
                    if (C0124O00000oO.isMTKPlatform()) {
                    }
                    return;
                }
                this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(true);
                return;
            }
            Log.d(VideoBase.TAG, "videoStabilization: disabled EIS and OIS when VIDEO_BOKEH is opened");
            this.mCamera2Device.setEnableEIS(false);
            this.mCamera2Device.setEnableOIS(false);
            this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(false);
        }
    }

    public void addSaveTask(String str, ContentValues contentValues) {
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        SaveVideoTask saveVideoTask = new SaveVideoTask(str, contentValues);
        synchronized (this) {
            this.mPendingSaveTaskList.add(saveVideoTask);
        }
    }

    public void closeCamera() {
        super.closeCamera();
        this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_DESTROY);
    }

    public void consumePreference(@UpdateType int... iArr) {
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
            } else if (i2 == 71) {
                updateKaleidoscope();
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
            } else if (!(i2 == 42 || i2 == 43 || i2 == 54)) {
                if (i2 != 55) {
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
                            updateVideoFocusMode();
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
                                            continue;
                                        case 47:
                                            updateUltraWideLDC();
                                            break;
                                        default:
                                            String str = "no consumer for this updateType: ";
                                            if (!BaseModule.DEBUG) {
                                                String str2 = VideoBase.TAG;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append(str);
                                                sb.append(i2);
                                                Log.w(str2, sb.toString());
                                                break;
                                            } else {
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append(str);
                                                sb2.append(i2);
                                                throw new RuntimeException(sb2.toString());
                                            }
                                    }
                            }
                    }
                } else {
                    updateModuleRelated();
                }
            }
        }
    }

    public void executeSaveTask(boolean z) {
        synchronized (this) {
            do {
                if (this.mPendingSaveTaskList.isEmpty()) {
                    break;
                }
                SaveVideoTask saveVideoTask = (SaveVideoTask) this.mPendingSaveTaskList.remove(0);
                String str = VideoBase.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("executeSaveTask: ");
                sb.append(saveVideoTask.videoPath);
                Log.d(str, sb.toString());
                this.mActivity.getImageSaver().addVideo(saveVideoTask.videoPath, saveVideoTask.contentValues, true);
            } while (!z);
            doLaterReleaseIfNeed();
        }
    }

    public void fillFeatureControl(StartControl startControl) {
        CaptureInterceptor captureInterceptor;
        StartControlFeatureDetail featureDetail = startControl.getFeatureDetail();
        this.mInterceptors = new SparseArray();
        switch (startControl.mTargetMode) {
            case 209:
                featureDetail.addFragmentInfo(R.id.bottom_beauty, BaseFragmentDelegate.FRAGMENT_VV_GALLERY);
                featureDetail.addFragmentInfo(R.id.full_screen_feature, BaseFragmentDelegate.FRAGMENT_VV_PROCESS);
                captureInterceptor = new CaptureInterceptor() {
                    public int getPriority() {
                        return 1;
                    }

                    public void intercept() {
                        if (((MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929)).hasFeatureInstalled(VMFeature.getFeatureNameByLocalMode(209))) {
                            LiveVVChooser liveVVChooser = (LiveVVChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(229);
                            if (liveVVChooser != null && liveVVChooser.startShot()) {
                                FunModule.this.enableCameraControls(false);
                            }
                        }
                    }
                };
                break;
            case 210:
                featureDetail.addFragmentInfo(R.id.bottom_beauty, BaseFragmentDelegate.FRAGMENT_CLONE_GALLERY);
                featureDetail.addFragmentInfo(R.id.full_screen_feature, BaseFragmentDelegate.FRAGMENT_CLONE_PROCESS);
                captureInterceptor = new CaptureInterceptor() {
                    public int getPriority() {
                        return 1;
                    }

                    public void intercept() {
                        CloneChooser cloneChooser = (CloneChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(416);
                        if (cloneChooser != null && cloneChooser.startShot()) {
                            FunModule.this.enableCameraControls(false);
                        }
                    }
                };
                break;
            case 211:
                featureDetail.addFragmentInfo(R.id.bottom_beauty, BaseFragmentDelegate.FRAGMENT_FILM_GALLERY);
                featureDetail.addFragmentInfo(R.id.full_screen_feature, BaseFragmentDelegate.FRAGMENT_CLONE_PROCESS);
                captureInterceptor = new CaptureInterceptor() {
                    public int getPriority() {
                        return 1;
                    }

                    public void intercept() {
                        LiveFilmChooser liveFilmChooser = (LiveFilmChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(675);
                        if (liveFilmChooser != null) {
                            liveFilmChooser.startShot();
                        }
                    }
                };
                break;
            default:
                return;
        }
        this.mInterceptors.put(captureInterceptor.getScope(), captureInterceptor);
    }

    @NonNull
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        int i = isEisOn() ? 32772 : (!CameraSettings.isVideoBokehOn() || !isFrontCamera()) ? this.mCameraCapabilities.isSupportVideoBeauty() ? CameraCapabilities.SESSION_OPERATION_MODE_VIDEO_BEAUTY : C0122O00000o.instance().Oo0Oo0O() ? CameraCapabilities.SESSION_OPERATION_MODE_MCTF : 0 : 32770;
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getOperatingMode: ");
        sb.append(Integer.toHexString(i));
        Log.d(str, sb.toString());
        return i;
    }

    public float getRecordSpeed() {
        return this.mSpeed;
    }

    public String getTag() {
        return VideoBase.TAG;
    }

    /* access modifiers changed from: protected */
    public boolean isAEAFLockSupported() {
        int i = this.mModuleIndex;
        return (i == 209 || i == 210 || i == 211) ? false : true;
    }

    /* access modifiers changed from: protected */
    public boolean isCameraSwitchingDuringZoomingAllowed() {
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            int i = this.mModuleIndex;
            if (i == 161 && !CameraSettings.isMacroModeEnabled(i) && isBackCamera() && !this.mMediaRecorderRecording && !this.mMediaRecorderRecordingPaused) {
                return true;
            }
        }
        return false;
    }

    public boolean isNeedHapticFeedback() {
        return !this.mMediaRecorderRecording;
    }

    public boolean isNeedMute() {
        return this.mMediaRecorderRecording;
    }

    public boolean isZoomEnabled() {
        switch (this.mModuleIndex) {
            case 209:
            case 210:
            case 211:
                return false;
            default:
                return super.isZoomEnabled();
        }
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        super.onCameraOpened();
        readVideoPreferences();
        initializeFocusManager();
        updatePreferenceTrampoline(UpdateConstant.FUN_TYPES_INIT);
        startPreview();
        setRecordSpeed(Integer.valueOf(CameraSettings.getCurrentLiveSpeed()).intValue());
        this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_START);
    }

    public void onCaptureCompleted(boolean z) {
    }

    public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
    }

    public void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4) {
        this.mActivity.getCameraScreenNail().requestFullReadPixels();
        CameraStatUtils.trackKaleidoscopeClick(MiLive.VALUE_MI_LIVE_CLICK_KALEIDOSCOPE_CAPTURE);
    }

    public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
        return null;
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        setCaptureIntent(this.mActivity.getCameraIntentManager().isVideoCaptureIntent());
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        this.mCameraView = this.mActivity.getGLView();
        enableCameraControls(false);
        this.mVideoFocusMode = AutoFocus.LEGACY_CONTINUOUS_VIDEO;
        onCameraOpened();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.sendEmptyMessage(45);
    }

    public void onKaleidoscopeChanged(String str) {
        updatePreferenceTrampoline(71);
    }

    public void onPause() {
        super.onPause();
        waitStereoSwitchThread();
        releaseResources();
        closeVideoFileDescriptor();
        this.mActivity.getSensorStateManager().reset();
        stopFaceDetection(true);
        resetScreenOn();
        this.mHandler.removeCallbacksAndMessages(null);
        if (!this.mActivity.isActivityPaused()) {
            PopupManager.getInstance(this.mActivity).notifyShowPopup(null, 1);
        }
    }

    public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
    }

    public void onPictureTakenFinished(boolean z) {
        this.mActivity.getCameraScreenNail().animateCaptureWithDraw(this.mOrientation);
        this.mActivity.getCameraScreenNail().setPreviewSaveListener(null);
        setCameraState(1);
    }

    public boolean onPictureTakenImageConsumed(Image image, TotalCaptureResult totalCaptureResult) {
        return false;
    }

    public void onPreviewLayoutChanged(Rect rect) {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPreviewLayoutChanged: ");
        sb.append(rect);
        Log.v(str, sb.toString());
        this.mActivity.onLayoutChange(rect);
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setRenderSize(this.mActivity.getCameraScreenNail().getRenderWidth(), this.mActivity.getCameraScreenNail().getRenderHeight());
            this.mFocusManager.setPreviewSize(rect.width(), rect.height());
        }
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        super.onPreviewSessionSuccess(cameraCaptureSession);
        if (!isCreated()) {
            Log.w(VideoBase.TAG, "onPreviewSessionSuccess: module is not ready");
            return;
        }
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPreviewSessionSuccess: ");
        sb.append(cameraCaptureSession);
        Log.d(str, sb.toString());
        this.mFaceDetectionEnabled = false;
        updatePreferenceInWorkThread(UpdateConstant.FUN_TYPES_ON_PREVIEW_SUCCESS);
        updatePreferenceTrampoline(71);
    }

    /* access modifiers changed from: protected */
    @MainThread
    public void onPreviewStart() {
        if (this.mPreviewing) {
            this.mMainProtocol.initializeFocusView(this);
            onShutterButtonFocus(true, 3);
        }
    }

    public void onSharedPreferenceChanged() {
        if (!this.mPaused && this.mCamera2Device != null) {
            readVideoPreferences();
        }
    }

    public void onShineChanged(int i) {
        if (i == 196) {
            updatePreferenceTrampoline(2);
            this.mMainProtocol.updateEffectViewVisible();
        } else if (i == 239) {
            updatePreferenceInWorkThread(13);
        } else {
            throw new RuntimeException("unknown configItem changed");
        }
    }

    public void onShutterButtonClick(int i) {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onShutterButtonClick  isRecording=");
        sb.append(this.mMediaRecorderRecording);
        sb.append(" inStartingFocusRecording=");
        sb.append(this.mInStartingFocusRecording);
        Log.u(str, sb.toString());
        this.mInStartingFocusRecording = false;
        this.mLastBackPressedTime = 0;
        if (isIgnoreTouchEvent()) {
            Log.w(VideoBase.TAG, "onShutterButtonClick: ignore touch event");
            return;
        }
        SparseArray sparseArray = this.mInterceptors;
        if (sparseArray != null) {
            BaseModuleInterceptor baseModuleInterceptor = (BaseModuleInterceptor) sparseArray.get(1);
            if (baseModuleInterceptor != null) {
                baseModuleInterceptor.intercept();
                if (baseModuleInterceptor.asBlocker()) {
                    return;
                }
            }
        }
        if (!isFrontCamera() || !this.mActivity.isScreenSlideOff()) {
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (this.mMediaRecorderRecording) {
                Log.u(VideoBase.TAG, "onShutterButtonClick stopVideoRecording");
                stopVideoRecording(false);
            } else {
                recordState.onPrepare();
                if (!checkCallingState()) {
                    recordState.onFailed();
                    return;
                }
                this.mActivity.getScreenHint().updateHint();
                if (Storage.isLowStorageAtLastPoint()) {
                    recordState.onFailed();
                    return;
                }
                setTriggerMode(i);
                enableCameraControls(false);
                playCameraSound(2);
                this.mRequestStartTime = System.currentTimeMillis();
                if (this.mFocusManager.canRecord()) {
                    Log.u(VideoBase.TAG, "onShutterButtonClick startVideoRecording");
                    startVideoRecording();
                } else {
                    Log.v(VideoBase.TAG, "wait for autoFocus");
                    this.mInStartingFocusRecording = true;
                }
            }
        }
    }

    public void onShutterButtonFocus(boolean z, int i) {
    }

    public void onSingleTapUp(int i, int i2, boolean z) {
        if (!this.mPaused && this.mCamera2Device != null && !hasCameraException() && this.mCamera2Device.isSessionReady() && !this.mSnapshotInProgress && isInTapableRect(i, i2)) {
            if (!isFrameAvailable()) {
                Log.w(VideoBase.TAG, "onSingleTapUp: frame not available");
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

    public void onStickerChanged(String str) {
        String str2 = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onStickerChanged: ");
        sb.append(str);
        Log.v(str2, sb.toString());
        V6CameraGLSurfaceView v6CameraGLSurfaceView = this.mCameraView;
        if (v6CameraGLSurfaceView != null) {
            GLCanvasImpl gLCanvas = v6CameraGLSurfaceView.getGLCanvas();
            if (gLCanvas instanceof GLCanvasImpl) {
                gLCanvas.setSticker(str);
            }
        }
    }

    public void onStop() {
        super.onStop();
        EffectController.getInstance().setCurrentSticker(null);
    }

    public void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
        MediaVideoEncoder mediaVideoEncoder;
        synchronized (this) {
            mediaVideoEncoder = this.mMediaVideoEncoder;
            boolean z = this.mMediaRecorderRecording;
        }
        if (mediaVideoEncoder != null && z) {
            mediaVideoEncoder.frameAvailableSoon(drawExtTexAttribute);
        }
    }

    /* access modifiers changed from: protected */
    public void onThermalConstrained() {
        super.onThermalConstrained();
        if (this.mMediaRecorderRecording) {
            stopVideoRecording(false);
        }
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
        Log.v(VideoBase.TAG, "pausePreview");
        this.mPreviewing = false;
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.pausePreview();
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.resetFocused();
        }
    }

    /* access modifiers changed from: protected */
    public void readVideoPreferences() {
        this.mMaxVideoDurationInMs = 15450;
    }

    public void registerProtocol() {
        Camera camera;
        int[] iArr;
        ImplFactory implFactory;
        super.registerProtocol();
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(167, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(178, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(201, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(236, this);
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

    /* access modifiers changed from: protected */
    public void resizeForPreviewAspectRatio() {
        int i;
        float f;
        int sensorOrientation = ((this.mCameraCapabilities.getSensorOrientation() - Util.getDisplayRotation(this.mActivity)) + m.cQ) % 180;
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        CameraSize cameraSize = this.mPreviewSize;
        if (sensorOrientation == 0) {
            f = (float) cameraSize.height;
            i = cameraSize.width;
        } else {
            f = (float) cameraSize.width;
            i = cameraSize.height;
        }
        mainContentProtocol.setPreviewAspectRatio(f / ((float) i));
    }

    public void resumePreview() {
        Log.v(VideoBase.TAG, "resumePreview");
        this.mPreviewing = true;
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.resumePreview();
        }
    }

    public void setRecordSpeed(int i) {
        this.mSpeed = this.SPEEDS[i];
    }

    public void startPreview() {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startPreview: ");
        sb.append(this.mPreviewing);
        Log.d(str, sb.toString());
        if (isDeviceAlive()) {
            checkDisplayOrientation();
            CameraSize cameraSize = this.mPreviewSize;
            setVideoSize(cameraSize.width, cameraSize.height);
            this.mQuality = Util.convertSizeToQuality(this.mPreviewSize);
            this.mCamera2Device.setPictureSize(this.mPreviewSize);
            this.mCamera2Device.setErrorCallback(this.mErrorCallback);
            this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            this.mCamera2Device.startPreviewSession(new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture()), 0, 0, null, getOperatingMode(), false, this);
            this.mFocusManager.resetFocused();
            if (this.mAELockOnlySupported) {
                this.mCamera2Device.setFocusCallback(this);
            }
            this.mPreviewing = true;
        }
    }

    /* access modifiers changed from: protected */
    public void startVideoRecording() {
        Log.v(VideoBase.TAG, "startVideoRecording");
        this.mCurrentVideoUri = null;
        silenceOuterAudio();
        if (!startRecorder()) {
            onStartRecorderFail();
            return;
        }
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onStart();
        }
        Log.v(VideoBase.TAG, "startVideoRecording process done");
        onStartRecorderSucceed();
    }

    public void stopVideoRecording(boolean z) {
        if (this.mMediaRecorderRecording) {
            if (is3ALocked()) {
                unlockAEAF();
            }
            this.mMediaRecorderRecording = false;
            long uptimeMillis = SystemClock.uptimeMillis() - this.mRecordingStartTime;
            this.mMuxer.stopRecording();
            if (!this.mPaused) {
                playCameraSound(3);
            }
            releaseMediaRecorder();
            boolean z2 = this.mCurrentVideoFilename == null;
            if (!z2 && uptimeMillis < 1000) {
                deleteVideoFile(this.mCurrentVideoFilename);
                z2 = true;
            }
            if (!z2) {
                addSaveTask(this.mCurrentVideoFilename, this.mCurrentVideoValues);
            }
            if (HybridZoomingSystem.IS_3_OR_MORE_SAT && isBackCamera()) {
                updateZoomRatioToggleButtonState(false);
                if (isUltraWideBackCamera()) {
                    setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                    setMaxZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR * this.mCameraCapabilities.getMaxZoomRatio());
                } else {
                    setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                    setVideoMaxZoomRatioByTele();
                }
            }
            this.mActivity.sendBroadcast(new Intent(BaseModule.STOP_VIDEO_RECORDING_ACTION));
            listenPhoneState(false);
            animateHold();
            CountDownTimer countDownTimer = this.mCountDownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onFinish();
            }
            if (this.mCamera2Device != null) {
                CameraStatUtils.trackVideoRecorded(isFrontCamera(), getActualCameraId(), getModuleIndex(), false, false, CameraSettings.isUltraWideConfigOpen(getModuleIndex()), CameraSettings.VIDEO_MODE_FUN, this.mQuality, this.mCamera2Device.getFlashMode(), 30, 0, this.mBeautyValues, uptimeMillis / 1000, false, null, false, this.mIsAutoHibernationSupported, this.mEnterAutoHibernationCount, CameraSettings.isVhdrOn(this.mCameraCapabilities, this.mModuleIndex));
            }
            if (!z) {
                String str = this.mVideoFocusMode;
                String str2 = AutoFocus.LEGACY_CONTINUOUS_VIDEO;
                if (!str2.equals(str)) {
                    this.mMainProtocol.clearFocusView(2);
                    setVideoFocusMode(str2, false);
                    updatePreferenceInWorkThread(14);
                }
            }
            restoreOuterAudio();
            keepScreenOnAwhile();
            AutoLockManager.getInstance(this.mActivity).hibernateDelayed();
        }
    }

    public void takePreviewSnapShoot() {
        if (getCameraState() != 3) {
            setCameraState(3);
            this.mCamera2Device.setShotType(-8);
            this.mCamera2Device.takeSimplePicture(this, this.mActivity.getImageSaver(), this.mActivity.getCameraScreenNail());
        }
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(167, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(178, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(201, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(236, this);
        getActivity().getImplFactory().detachAdditional();
    }

    /* access modifiers changed from: protected */
    public void updateRecordingTime() {
        super.updateRecordingTime();
        if (this.mMediaRecorderRecording) {
            CountDownTimer countDownTimer = this.mCountDownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            float f = (float) this.mMaxVideoDurationInMs;
            float f2 = this.mSpeed;
            AnonymousClass1 r2 = new CountDownTimer((long) (f / f2), (long) (1000.0f / f2)) {
                public void onFinish() {
                    FunModule.this.stopVideoRecording(false);
                }

                public void onTick(long j) {
                    String millisecondToTimeString = Util.millisecondToTimeString((((long) (((float) j) * FunModule.this.mSpeed)) + 950) - 450, false);
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null) {
                        topAlert.updateRecordingTime(millisecondToTimeString);
                    }
                }
            };
            this.mCountDownTimer = r2;
            this.mCountDownTimer.start();
        }
    }
}
