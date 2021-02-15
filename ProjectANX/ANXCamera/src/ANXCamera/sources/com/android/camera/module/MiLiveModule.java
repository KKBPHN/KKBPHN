package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.SensorEvent;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Range;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import com.android.camera.AudioMonitorPlayer;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.SensorStateManager.SensorStateListener;
import com.android.camera.SoundSetting;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FaceAnalyzeInfo;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.fragment.CtaNoticeFragment;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.log.Log;
import com.android.camera.module.impl.ImplFactory;
import com.android.camera.module.loader.FunctionAsdSceneDetect;
import com.android.camera.module.loader.FunctionFaceDetect;
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
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.KaleidoscopeProtocol;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.MiLiveConfigChanges;
import com.android.camera.protocol.ModeProtocol.MiLivePlayerControl;
import com.android.camera.protocol.ModeProtocol.MiLiveRecorderControl.IRecorderListener;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.MiLive;
import com.android.camera.statistic.MistatsConstants.VideoAttr;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.storage.Storage;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.FaceDetectionCallback;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CameraHardwareFace;
import com.android.gallery3d.ui.GLCanvas;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.recordmediaprocess.SystemUtil;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MiLiveModule extends BaseModule implements ILiveModule, CameraPreviewCallback, FocusCallback, FaceDetectionCallback, CameraAction, KaleidoscopeProtocol, Listener, PictureCallback {
    private static final long CAPTURE_THRESHOLD = 500;
    public static int LIB_LOAD_CALLER_PLAYER = 2;
    public static int LIB_LOAD_CALLER_RECORDER = 1;
    private static final int MSG_WAIT_SHUTTER_SOUND_FINISH = 256;
    private static final long START_RECORDING_OFFSET = 300;
    private static int sLibLoaded;
    /* access modifiers changed from: private */
    public final String TAG;
    private boolean m3ALocked;
    private AudioMonitorPlayer mAudioMonitorPlayer;
    private String mBaseFileName;
    protected BeautyValues mBeautyValues;
    private V6CameraGLSurfaceView mCameraView;
    private long mCaptureTime = 0;
    private CtaNoticeFragment mCtaNoticeFragment;
    protected boolean mFaceDetectionEnabled;
    protected boolean mFaceDetectionStarted;
    /* access modifiers changed from: private */
    public FocusManager2 mFocusManager;
    private boolean mIsPreviewing = false;
    private boolean mIsStopKaraoke = false;
    /* access modifiers changed from: private */
    public MiLiveConfigChanges mLiveConfigChanges;
    private Disposable mMetaDataDisposable;
    private FlowableEmitter mMetaDataFlowableEmitter;
    private int mOldOriginVolumeStream;
    /* access modifiers changed from: private */
    public long mOnResumeTime;
    private int mQuality = 6;
    private IRecorderListener mRecorderListener = new IRecorderListener() {
        public void onRecorderCancel() {
            Log.d(MiLiveModule.this.TAG, "onRecorderCancel");
            MiLiveModule.this.resetToIdle();
        }

        public void onRecorderError() {
            Log.d(MiLiveModule.this.TAG, "onRecorderError");
            MiLiveModule.this.resetToIdle();
        }

        public void onRecorderFinish(List list, String str) {
            boolean z = list != null && list.size() > 0 && MiLiveModule.this.mLiveConfigChanges.getTotalRecordingTime() >= 500;
            if (!z) {
                Log.d(MiLiveModule.this.TAG, "onFinish of no segments !!");
                MiLiveModule.this.resetToIdle();
            } else {
                MiLiveModule.this.listenPhoneState(false);
                MiLiveModule.this.trackLiveVideoParams();
                MiLiveModule.this.initReview(list, str);
            }
            if (!z) {
                MiLiveModule.this.resetZoomPreview();
            }
        }

        public void onRecorderPaused(@NonNull List list) {
        }
    };
    protected SensorStateListener mSensorStateListener = new SensorStateListener() {
        public boolean isWorking() {
            return MiLiveModule.this.isAlive() && MiLiveModule.this.getCameraState() != 0;
        }

        public void notifyDevicePostureChanged() {
            MiLiveModule.this.mActivity.getEdgeShutterView().onDevicePostureChanged();
        }

        public void onDeviceBecomeStable() {
            Log.v(MiLiveModule.this.TAG, "onDeviceBecomeStable");
        }

        public void onDeviceBeginMoving() {
        }

        public void onDeviceKeepMoving(double d) {
            if (!MiLiveModule.this.mMainProtocol.isEvAdjusted(true) && !MiLiveModule.this.mPaused && Util.isTimeout(System.currentTimeMillis(), MiLiveModule.this.mTouchFocusStartingTime, 3000) && !MiLiveModule.this.is3ALocked() && MiLiveModule.this.mFocusManager != null && MiLiveModule.this.mFocusManager.isNeedCancelAutoFocus() && !MiLiveModule.this.isRecording()) {
                MiLiveModule.this.mFocusManager.onDeviceKeepMoving(d);
            }
        }

        public void onDeviceKeepStable() {
        }

        public void onDeviceLieChanged(boolean z) {
        }

        public void onDeviceOrientationChanged(float f, boolean z) {
        }

        public void onDeviceRotationChanged(float[] fArr) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
        }
    };
    protected boolean mShowFace = false;
    /* access modifiers changed from: private */
    public long mTouchFocusStartingTime;
    private CameraSize mVideoSize;

    class LiveAsdConsumer implements Consumer {
        private LiveAsdConsumer() {
        }

        public void accept(Integer num) {
        }
    }

    class MainHandler extends Handler {
        private WeakReference mModule;

        public MainHandler(Looper looper, MiLiveModule miLiveModule) {
            super(looper);
            this.mModule = new WeakReference(miLiveModule);
        }

        public void handleMessage(Message message) {
            MiLiveModule miLiveModule = (MiLiveModule) this.mModule.get();
            if (miLiveModule != null) {
                if (!miLiveModule.isCreated()) {
                    removeCallbacksAndMessages(null);
                } else if (miLiveModule.getActivity() != null) {
                    int i = message.what;
                    if (i == 2) {
                        MiLiveModule.this.getWindow().clearFlags(128);
                    } else if (i == 4) {
                        MiLiveModule.this.checkActivityOrientation();
                        if (SystemClock.uptimeMillis() - MiLiveModule.this.mOnResumeTime < 5000) {
                            MiLiveModule.this.mHandler.sendEmptyMessageDelayed(4, 100);
                        }
                    } else if (i == 9) {
                        MiLiveModule miLiveModule2 = MiLiveModule.this;
                        miLiveModule2.mMainProtocol.initializeFocusView(miLiveModule2);
                    } else if (i == 17) {
                        MiLiveModule.this.mHandler.removeMessages(17);
                        MiLiveModule.this.mHandler.removeMessages(2);
                        MiLiveModule.this.getWindow().addFlags(128);
                        MiLiveModule miLiveModule3 = MiLiveModule.this;
                        miLiveModule3.mHandler.sendEmptyMessageDelayed(2, (long) miLiveModule3.getScreenDelay());
                    } else if (i == 31) {
                        MiLiveModule.this.setOrientationParameter();
                    } else if (i == 35) {
                        MiLiveModule miLiveModule4 = MiLiveModule.this;
                        boolean z = false;
                        boolean z2 = message.arg1 > 0;
                        if (message.arg2 > 0) {
                            z = true;
                        }
                        miLiveModule4.handleUpdateFaceView(z2, z);
                    } else if (i != 51) {
                        if (i == 64) {
                            BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
                            if (bluetoothHeadset != null) {
                                if (bluetoothHeadset.isSupportBluetoothSco(miLiveModule.getModuleIndex())) {
                                    miLiveModule.silenceOuterAudio();
                                }
                                bluetoothHeadset.startBluetoothSco(miLiveModule.getModuleIndex());
                            }
                        } else if (i == 256) {
                            MiLiveModule.this.startVideoRecording();
                        }
                    } else if (!MiLiveModule.this.mActivity.isActivityPaused()) {
                        MiLiveModule miLiveModule5 = MiLiveModule.this;
                        miLiveModule5.mOpenCameraFail = true;
                        miLiveModule5.onCameraException();
                    }
                }
            }
        }
    }

    public MiLiveModule() {
        StringBuilder sb = new StringBuilder();
        sb.append(MiLiveModule.class.getSimpleName());
        sb.append("@");
        sb.append(hashCode());
        this.TAG = sb.toString();
    }

    static /* synthetic */ void O00oo0O0() {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            mainContentProtocol.setCenterHint(8, null, null, 0);
        }
    }

    private boolean configReview(boolean z) {
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        boolean z2 = false;
        if (configChanges == null || baseDelegate == null) {
            Log.w(this.TAG, "configChanges is null");
            return false;
        }
        if (baseDelegate.getActiveFragment(R.id.full_screen_feature) == 1048561) {
            z2 = true;
        }
        boolean z3 = z ^ z2;
        String str = this.TAG;
        if (z3) {
            Log.d(str, "config live review~");
            configChanges.configLiveReview();
        } else {
            Log.d(str, "skip config live review~");
        }
        return true;
    }

    private String createName(long j, int i) {
        if (i > 0) {
            return this.mBaseFileName;
        }
        this.mBaseFileName = new SimpleDateFormat(getString(R.string.video_file_name_format), Locale.ENGLISH).format(new Date(j));
        return this.mBaseFileName;
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

    private ContentValues genContentValues(int i, int i2, boolean z) {
        String str;
        String createName = createName(System.currentTimeMillis(), i2);
        if (i2 > 0) {
            String format = String.format(Locale.ENGLISH, "_%d", new Object[]{Integer.valueOf(i2)});
            StringBuilder sb = new StringBuilder();
            sb.append(createName);
            sb.append(format);
            createName = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(createName);
        sb2.append(Util.convertOutputFormatToFileExt(i));
        String sb3 = sb2.toString();
        String convertOutputFormatToMimeType = Util.convertOutputFormatToMimeType(i);
        if (z) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(Storage.CAMERA_TEMP_DIRECTORY);
            sb4.append('/');
            sb4.append(sb3);
            str = sb4.toString();
            StringBuilder sb5 = new StringBuilder();
            sb5.append(Storage.CAMERA_TEMP_DIRECTORY);
            sb5.append(File.separator);
            sb5.append(Storage.AVOID_SCAN_FILE_NAME);
            Util.createFile(new File(sb5.toString()));
        } else {
            StringBuilder sb6 = new StringBuilder();
            sb6.append(Storage.DIRECTORY);
            sb6.append('/');
            sb6.append(sb3);
            str = sb6.toString();
        }
        String str2 = this.TAG;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("genContentValues: path=");
        sb7.append(str);
        Log.v(str2, sb7.toString());
        ContentValues contentValues = new ContentValues(8);
        contentValues.put("title", createName);
        contentValues.put("_display_name", sb3);
        contentValues.put("mime_type", convertOutputFormatToMimeType);
        contentValues.put("_data", str);
        StringBuilder sb8 = new StringBuilder();
        sb8.append(this.mVideoSize.width);
        sb8.append("x");
        sb8.append(this.mVideoSize.height);
        contentValues.put("resolution", sb8.toString());
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        if (!(currentLocation == null || (currentLocation.getLatitude() == 0.0d && currentLocation.getLongitude() == 0.0d))) {
            contentValues.put("latitude", Double.valueOf(currentLocation.getLatitude()));
            contentValues.put("longitude", Double.valueOf(currentLocation.getLongitude()));
        }
        return contentValues;
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

    private void initLiveConfig() {
        this.mLiveConfigChanges = (MiLiveConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(241);
        if (this.mLiveConfigChanges == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 241);
            this.mLiveConfigChanges = (MiLiveConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(241);
            this.mLiveConfigChanges.prepare();
        }
        this.mLiveConfigChanges.initResource();
        this.mLiveConfigChanges.setRecorderListener(this.mRecorderListener);
        this.mLiveConfigChanges.onOrientationChanged(this.mOrientation, this.mOrientationCompensation, 0);
    }

    private void initMetaParser() {
        this.mMetaDataDisposable = Flowable.create(new O000o00(this), BackpressureStrategy.DROP).map(new FunctionFaceDetect(this, isFrontCamera())).map(new FunctionAsdSceneDetect(this, getCameraCapabilities())).subscribe((Consumer) new LiveAsdConsumer());
    }

    /* access modifiers changed from: private */
    public void initReview(List list, String str) {
        ContentValues genContentValues = genContentValues(2, 0, false);
        MiLivePlayerControl miLivePlayerControl = (MiLivePlayerControl) ModeCoordinatorImpl.getInstance().getAttachProtocol(242);
        if (miLivePlayerControl != null) {
            miLivePlayerControl.prepare(genContentValues, list, str);
        } else {
            Log.d(this.TAG, "show review fail~");
            resetToIdle();
        }
        this.mIsPreviewing = true;
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

    private boolean isEisOn() {
        return (isBackCamera() || CameraSettings.isCurrentQualitySupportEis(this.mQuality, 30, this.mCameraCapabilities)) && CameraSettings.isMovieSolidOn() && this.mCameraCapabilities.isEISPreviewSupported();
    }

    private boolean isSaving() {
        return false;
    }

    public static void loadLibs(Context context, int i) {
        if (sLibLoaded == 0) {
            System.loadLibrary("vvc++_shared");
            System.loadLibrary("ffmpeg");
            System.loadLibrary("record_video");
            SystemUtil.Init(context, 50011);
        }
        sLibLoaded |= i;
        StringBuilder sb = new StringBuilder();
        sb.append("loadLibs sLibLoaded : ");
        sb.append(sLibLoaded);
        Log.d("MiLiveModule", sb.toString());
    }

    private void pauseVideoRecording(boolean z) {
        if (isAlive() && this.mLiveConfigChanges != null) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("pauseVideoRecording formRelease ");
            sb.append(z);
            Log.d(str, sb.toString());
            if (this.mLiveConfigChanges.canRecordingStop() || z) {
                CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_PAUSE);
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                if (recordState != null) {
                    recordState.onPause();
                } else {
                    Log.d(this.TAG, "recordState pause fail~");
                }
                this.mLiveConfigChanges.pauseRecording();
                resetZoomPreview();
            } else {
                Log.d(this.TAG, "too fast to pause recording.");
            }
        }
    }

    private void previewWhenSessionSuccess() {
        setCameraState(1);
        updatePreferenceInWorkThread(UpdateConstant.MI_LIVE_TYPES_ON_PREVIEW_SUCCESS);
        updatePreferenceTrampoline(71);
    }

    /* access modifiers changed from: private */
    public void resetToIdle() {
        Log.d(this.TAG, "resetToIdle");
        MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
        if (miLiveConfigChanges != null) {
            miLiveConfigChanges.reset();
        }
        configReview(false);
        DataRepository.dataItemLive().setMiLiveSegmentData(null);
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onFinish();
        }
        getActivity().setVolumeControlStream(this.mOldOriginVolumeStream);
        this.mIsPreviewing = false;
        resetZoomPreview();
        if (doLaterReleaseIfNeed()) {
            Log.d(this.TAG, "onReviewDoneClicked -- ");
            return;
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            if (camera2Proxy.isSessionReady()) {
                resumePreview();
            } else {
                startPreview();
            }
        }
    }

    /* access modifiers changed from: private */
    public void resetZoomPreview() {
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT && isBackCamera()) {
            updateZoomRatioToggleButtonState(false);
            if (isUltraWideBackCamera()) {
                setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                setMaxZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR * this.mCameraCapabilities.getMaxZoomRatio());
                return;
            }
            setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
            setVideoMaxZoomRatioByTele();
        }
    }

    private void resetZoomRecoding() {
        float min;
        float ultraTeleMinZoomRatio;
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
                return;
            }
            setMaxZoomRatio(min);
        }
    }

    private void resumeVideoRecording() {
        if (isAlive() && this.mLiveConfigChanges != null) {
            resetZoomRecoding();
            this.mLiveConfigChanges.resumeRecording();
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onResume();
            } else {
                Log.d(this.TAG, "recordState resume fail~");
            }
        }
    }

    private void setOrientation(int i, int i2) {
        if (i != -1) {
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
                CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new O000o000(this));
            } else {
                updatePreferenceInWorkThread(35);
            }
        }
    }

    private boolean shouldApplyUltraWideLDC() {
        if (CameraSettings.shouldUltraWideVideoLDCBeVisibleInMode(this.mModuleIndex) && this.mActualCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
            return CameraSettings.isUltraWideVideoLDCEnabled();
        }
        return false;
    }

    private void showReview() {
        MiLivePlayerControl miLivePlayerControl = (MiLivePlayerControl) ModeCoordinatorImpl.getInstance().getAttachProtocol(242);
        if (miLivePlayerControl != null) {
            miLivePlayerControl.show();
            return;
        }
        Log.d(this.TAG, "show review fail~");
        resetToIdle();
    }

    /* access modifiers changed from: private */
    @MainThread
    public void startVideoRecording() {
        Log.k(3, this.TAG, "MiLive startVideoRecording");
        setCameraAudioRestriction(true);
        keepScreenOn();
        if (this.mLiveConfigChanges != null) {
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            recordState.onPrepare();
            Handler handler = this.mHandler;
            if (handler != null && handler.hasMessages(64)) {
                this.mHandler.removeMessages(64);
                this.mHandler.sendEmptyMessage(64);
            }
            this.mLiveConfigChanges.startRecording();
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
            CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_START);
            this.mOldOriginVolumeStream = getActivity().getVolumeControlStream();
            getActivity().setVolumeControlStream(3);
            recordState.onStart();
            configReview(true);
            resetZoomRecoding();
            listenPhoneState(true);
        }
    }

    /* access modifiers changed from: private */
    public void trackLiveVideoParams() {
        MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
        CameraStatUtils.trackMiLiveRecordingParams(miLiveConfigChanges != null ? miLiveConfigChanges.getSegmentSize() : 0, CameraSettings.getCurrentLiveMusic()[1], CameraSettings.getShaderEffect(), Integer.valueOf(CameraSettings.getCurrentLiveSpeed()).intValue(), isFrontCamera(), this.mBeautyValues, this.mQuality, EffectController.getInstance().getCurrentKaleidoscope());
    }

    public static void unloadLibs(int i) {
        sLibLoaded = (~i) & sLibLoaded;
        StringBuilder sb = new StringBuilder();
        sb.append("unloadLibs sLibLoaded : ");
        sb.append(sLibLoaded);
        Log.d("MiLiveModule", sb.toString());
        if (sLibLoaded == 0) {
            SystemUtil.UnInit();
        }
    }

    private void updateBeauty() {
        if (this.mBeautyValues == null) {
            this.mBeautyValues = new BeautyValues();
        }
        CameraSettings.initBeautyValues(this.mBeautyValues, this.mModuleIndex);
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateBeauty(): ");
        sb.append(this.mBeautyValues);
        Log.d(str, sb.toString());
        this.mCamera2Device.setBeautyValues(this.mBeautyValues);
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
        String str = this.TAG;
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

    private void updateKaleidoscope() {
        EffectController.getInstance().setKaleidoscope(DataRepository.dataItemRunning().getComponentRunningKaleidoscope().getKaleidoscopeValue());
    }

    private void updateLiveRelated() {
        if (this.mAlgorithmPreviewSize != null) {
            this.mCamera2Device.startPreviewCallback(this.mLiveConfigChanges, null);
        }
    }

    private void updatePictureAndPreviewSize() {
        List supportedOutputSizeWithAssignedMode = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class);
        float previewRatio = this.mLiveConfigChanges.getPreviewRatio();
        this.mQuality = CameraSettings.getPreferLiveVideoQuality(this.mActualCameraId, this.mModuleIndex);
        this.mVideoSize = null;
        this.mVideoSize = this.mQuality != 6 ? new CameraSize(1280, Util.LIMIT_SURFACE_WIDTH) : new CameraSize(1920, 1080);
        this.mPreviewSize = Util.getOptimalPreviewSize(false, this.mBogusCameraId, supportedOutputSizeWithAssignedMode, (double) previewRatio, this.mVideoSize);
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("previewSize: ");
        sb.append(this.mPreviewSize.toString());
        Log.d(str, sb.toString());
        CameraSize cameraSize = this.mPreviewSize;
        this.mPictureSize = cameraSize;
        updateCameraScreenNailSize(cameraSize.width, cameraSize.height);
    }

    private void updateUltraWideLDC() {
        this.mCamera2Device.setUltraWideLDC(shouldApplyUltraWideLDC());
    }

    private void updateVideoBokeh() {
        float videoBokehRatio = CameraSettings.getVideoBokehRatio();
        if (isFrontCamera()) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("frontVideoBokeh: ");
            sb.append(videoBokehRatio);
            Log.i(str, sb.toString());
            this.mCamera2Device.setVideoBokehLevelFront(videoBokehRatio);
            return;
        }
        int i = (int) videoBokehRatio;
        String str2 = this.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("backVideoBokeh: ");
        sb2.append(i);
        Log.i(str2, sb2.toString());
        this.mCamera2Device.setVideoBokehLevelBack(i);
    }

    private void updateVideoStabilization() {
        if (isDeviceAlive()) {
            if (isEisOn()) {
                Log.d(this.TAG, "videoStabilization: EIS");
                this.mCamera2Device.setEnableEIS(true);
                this.mCamera2Device.setEnableOIS(false);
                if (!this.mCameraCapabilities.isEISPreviewSupported()) {
                    this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(true);
                }
            } else {
                Log.d(this.TAG, "videoStabilization: OIS");
                this.mCamera2Device.setEnableEIS(false);
                this.mCamera2Device.setEnableOIS(true);
                this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(false);
            }
        }
    }

    public /* synthetic */ void O000000o(FlowableEmitter flowableEmitter) {
        this.mMetaDataFlowableEmitter = flowableEmitter;
    }

    public /* synthetic */ void O000000o(String str, Uri uri) {
        MiLivePlayerControl miLivePlayerControl = (MiLivePlayerControl) ModeCoordinatorImpl.getInstance().getAttachProtocol(242);
        if (miLivePlayerControl != null) {
            ContentValues saveContentValues = miLivePlayerControl.getSaveContentValues();
            if (saveContentValues != null) {
                String asString = saveContentValues.getAsString("title");
                String asString2 = saveContentValues.getAsString("_data");
                String str2 = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("newUri: ");
                sb.append(str);
                sb.append(" | ");
                sb.append(asString);
                Log.d(str2, sb.toString());
                if (asString.equals(str)) {
                    miLivePlayerControl.onLiveSaveToLocalFinished(uri, asString2);
                }
            }
        }
    }

    public /* synthetic */ void O00oo0Oo() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
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
        for (int i : iArr) {
            switch (i) {
                case 1:
                    updatePictureAndPreviewSize();
                    break;
                case 2:
                    updateFilter();
                    break;
                case 3:
                    updateFocusArea();
                    break;
                case 5:
                    updateFace();
                    break;
                case 9:
                    updateAntiBanding(CameraSettings.getAntiBanding());
                    break;
                case 10:
                    updateFlashPreference();
                    break;
                case 11:
                case 20:
                case 30:
                case 34:
                case 42:
                case 43:
                case 46:
                case 48:
                case 50:
                case 79:
                    break;
                case 12:
                    setEvValue();
                    break;
                case 13:
                    updateBeauty();
                    break;
                case 14:
                    updateFocusMode();
                    break;
                case 19:
                    updateFpsRange();
                    break;
                case 24:
                    applyZoomRatio();
                    break;
                case 25:
                    focusCenter();
                    break;
                case 29:
                    updateExposureMeteringMode();
                    break;
                case 31:
                    updateVideoStabilization();
                    break;
                case 35:
                    updateDeviceOrientation();
                    break;
                case 47:
                    updateUltraWideLDC();
                    break;
                case 54:
                    updateLiveRelated();
                    break;
                case 55:
                    updateModuleRelated();
                    break;
                case 58:
                    updateBackSoftLightPreference();
                    break;
                case 66:
                    updateThermalLevel();
                    break;
                case 67:
                    updateVideoBokeh();
                    break;
                case 71:
                    updateKaleidoscope();
                    break;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("no consumer for this updateType: ");
                    sb.append(i);
                    throw new RuntimeException(sb.toString());
            }
        }
    }

    public void doReverse() {
        if (this.mLiveConfigChanges != null && !isRecording()) {
            Log.u(this.TAG, "doReverse");
            this.mLiveConfigChanges.deleteLastFragment();
            if (this.mLiveConfigChanges.getSegmentSize() == 0) {
                BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
                if (backStack != null) {
                    backStack.handleBackStackFromKeyBack();
                }
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                if (recordState != null) {
                    recordState.onFinish();
                }
                stopVideoRecording(false, false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean enableFaceDetection() {
        return DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_FACE_DETECTION, getResources().getBoolean(R.bool.pref_camera_facedetection_default));
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        int i = isEisOn() ? 32772 : (!CameraSettings.isVideoBokehOn() || !isFrontCamera()) ? this.mCameraCapabilities.isSupportVideoBeauty() ? CameraCapabilities.SESSION_OPERATION_MODE_VIDEO_BEAUTY : C0122O00000o.instance().Oo0Oo0O() ? CameraCapabilities.SESSION_OPERATION_MODE_MCTF : 0 : 32770;
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getOperatingMode: ");
        sb.append(Integer.toHexString(i));
        Log.k(3, str, sb.toString());
        return i;
    }

    public String getTag() {
        return this.TAG;
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
            if (i == 183 && !CameraSettings.isMacroModeEnabled(i) && isBackCamera() && !isRecording()) {
                return true;
            }
        }
        return false;
    }

    public boolean isDoingAction() {
        MiLivePlayerControl miLivePlayerControl = (MiLivePlayerControl) ModeCoordinatorImpl.getInstance().getAttachProtocol(242);
        return isRecording() || (miLivePlayerControl != null && miLivePlayerControl.isShowing());
    }

    public boolean isFaceDetectStarted() {
        return this.mFaceDetectionStarted;
    }

    public boolean isNeedMute() {
        return isRecording();
    }

    public boolean isRecording() {
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(256)) {
            return true;
        }
        MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
        return miLiveConfigChanges != null && miLiveConfigChanges.getCurState() == 2;
    }

    public boolean isRecordingPaused() {
        MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
        return miLiveConfigChanges != null && miLiveConfigChanges.getCurState() == 3;
    }

    public boolean isSelectingCapturedResult() {
        MiLivePlayerControl miLivePlayerControl = (MiLivePlayerControl) ModeCoordinatorImpl.getInstance().getAttachProtocol(242);
        return miLivePlayerControl != null && miLivePlayerControl.isShowing();
    }

    public boolean isShowAeAfLockIndicator() {
        return this.m3ALocked;
    }

    public boolean isShowCaptureButton() {
        return isSupportFocusShoot();
    }

    public boolean isSupportFocusShoot() {
        return false;
    }

    public boolean isUnInterruptable() {
        return false;
    }

    public boolean isUseFaceInfo() {
        return false;
    }

    public boolean isZoomEnabled() {
        return getCameraState() != 3 && !isFrontCamera() && !DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiCreate() && isFrameAvailable();
    }

    /* access modifiers changed from: protected */
    public void lockAEAF() {
        Log.d(this.TAG, "lockAEAF");
        if (this.mAeLockSupported) {
            this.mCamera2Device.setAELock(true);
        }
        this.mFocusManager.setAeAwbLock(true);
        this.m3ALocked = true;
    }

    public boolean multiCapture() {
        return false;
    }

    public void notifyAfterFirstFrameArrived() {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("notifyAfterFirstFrameArrived.m3ALocked: ");
        sb.append(this.m3ALocked);
        Log.d(str, sb.toString());
        if (this.m3ALocked) {
            unlockAEAF();
            FocusManager2 focusManager2 = this.mFocusManager;
            if (focusManager2 != null) {
                focusManager2.cancelFocus();
            }
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.directlyHideTips();
            }
        }
    }

    public void notifyError() {
        if (CameraSchedulers.isOnMainThread() && isRecording()) {
            stopVideoRecording(false, false);
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
        if (this.mLiveConfigChanges == null) {
            return false;
        }
        if (!isRecording() && !isRecordingPaused()) {
            return super.onBackPressed();
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
            this.mLastBackPressedTime = currentTimeMillis;
            ToastUtils.showToast((Context) this.mActivity, (int) R.string.record_back_pressed_hint, true);
        } else {
            stopVideoRecording(true, true);
        }
        return true;
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

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        super.onCameraOpened();
        initLiveConfig();
        initializeFocusManager();
        updatePreferenceTrampoline(UpdateConstant.FUN_TYPES_INIT);
        startPreview();
        DataRepository.dataItemGlobal().isFirstShowCTAConCollect();
        this.mOnResumeTime = SystemClock.uptimeMillis();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
        initMetaParser();
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
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        this.mHandler = new MainHandler(this.mActivity.getMainLooper(), this);
        onCameraOpened();
        this.mAudioMonitorPlayer = new AudioMonitorPlayer();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
        this.mActivity.getSensorStateManager().setRotationVectorEnabled(true);
        this.mCameraView = this.mActivity.getGLView();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(this.TAG, "onDestroy");
        CtaNoticeFragment ctaNoticeFragment = this.mCtaNoticeFragment;
        if (ctaNoticeFragment != null) {
            ctaNoticeFragment.dismiss();
        }
        this.mHandler.post(C0371O000Oooo.INSTANCE);
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
                }
            } else if (this.mObjectTrackingStarted) {
                this.mMainProtocol.setFaces(3, cameraHardwareFaceArr, getActiveArraySize(), rect);
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
                    Log.v(this.TAG, str);
                }
                if ((getCameraState() != 3 || focusTask.getFocusTrigger() == 3) && !this.m3ALocked) {
                    this.mFocusManager.onFocusResult(focusTask);
                }
            } else {
                Log.v(this.TAG, String.format(Locale.ENGLISH, "FocusTime=%1$dms focused=%2$b", new Object[]{Long.valueOf(focusTask.getElapsedTime()), Boolean.valueOf(focusTask.isSuccess())}));
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
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(256)) {
            this.mHandler.removeMessages(256);
            Log.d(this.TAG, "skip stopVideoRecording & remove startVideoRecording");
        }
        if (isRecording()) {
            pauseVideoRecording(true);
        } else if (isSaving()) {
            return;
        }
        doLaterReleaseIfNeed();
    }

    public void onKaleidoscopeChanged(String str) {
        updatePreferenceTrampoline(71);
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
                    if (this.mIsPreviewing) {
                        MiLivePlayerControl miLivePlayerControl = (MiLivePlayerControl) ModeCoordinatorImpl.getInstance().getAttachProtocol(242);
                        if (miLivePlayerControl != null) {
                            miLivePlayerControl.startLiveRecordSaving();
                        }
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
                stopVideoRecording(true, true);
                return super.onKeyDown(i, keyEvent);
            }
        }
        if (!this.mIsPreviewing) {
            if (i == 24 || i == 88) {
                z = true;
            }
            if (handleVolumeKeyEvent(z, true, keyEvent.getRepeatCount(), keyEvent.getDevice().isExternal())) {
                return true;
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

    public void onLongPress(float f, float f2) {
        int i = (int) f;
        int i2 = (int) f2;
        if (isInTapableRect(i, i2)) {
            onSingleTapUp(i, i2, true);
            if (isAEAFLockSupported() && CameraSettings.isAEAFLockSupport()) {
                lockAEAF();
            }
            this.mMainProtocol.performHapticFeedback(0);
        }
    }

    public void onNewUriArrived(Uri uri, String str) {
        super.onNewUriArrived(uri, str);
        if (isAlive()) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new C0372O000o00O(this, str, uri));
            }
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        setOrientation(i, i2);
        MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
        if (miLiveConfigChanges != null) {
            miLiveConfigChanges.onOrientationChanged(i, i2, i3);
        }
    }

    public void onPause() {
        super.onPause();
        Log.d(this.TAG, "onPause");
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.removeMessages();
        }
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

    public void onPauseButtonClick() {
        if (isRecording()) {
            Log.u(this.TAG, "onPauseButtonClick pauseVideoRecording");
            pauseVideoRecording(false);
            return;
        }
        Log.u(this.TAG, "onPauseButtonClick resumeVideoRecording");
        resumeVideoRecording();
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

    public void onPreviewRelease() {
        if (isRecording()) {
            stopVideoRecording(false, false);
        }
        super.onPreviewRelease();
    }

    public void onPreviewSessionClosed(CameraCaptureSession cameraCaptureSession) {
    }

    public void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession) {
        if (!isTextureExpired() || !retryOnceIfCameraError(this.mHandler)) {
            this.mHandler.sendEmptyMessage(51);
        } else {
            Log.d(this.TAG, "sessionFailed due to surfaceTexture expired, retry");
        }
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        if (cameraCaptureSession != null && isAlive()) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(64);
                this.mHandler.sendEmptyMessageDelayed(64, 300);
            }
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
        Log.d(this.TAG, "onResume");
    }

    public void onReviewCancelClicked() {
        Log.d(this.TAG, "onReviewCancelClicked");
        resetToIdle();
    }

    public void onReviewDoneClicked() {
        Log.d(this.TAG, "onReviewDoneClicked");
        MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
        if (miLiveConfigChanges != null) {
            miLiveConfigChanges.stopRecording();
        }
        resetToIdle();
    }

    public void onShineChanged(int i) {
        int[] iArr;
        if (i == 196) {
            iArr = new int[]{2};
        } else if (i == 239) {
            iArr = new int[]{13};
        } else {
            throw new RuntimeException("unknown configItem changed");
        }
        updatePreferenceInWorkThread(iArr);
    }

    public void onShutterButtonClick(int i) {
        String str;
        String str2;
        if (getCameraState() == 0) {
            str = this.TAG;
            str2 = "skip shutter caz preview paused.";
        } else {
            MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
            int curState = miLiveConfigChanges != null ? miLiveConfigChanges.getCurState() : 0;
            String str3 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onShutterButtonClick ");
            sb.append(curState);
            Log.u(str3, sb.toString());
            Log.k(3, this.TAG, String.format("onShutterButtonClick mode=%d, state=%d", new Object[]{Integer.valueOf(i), Integer.valueOf(curState)}));
            if (curState != 1) {
                if (curState == 2 || curState == 3) {
                    Log.u(this.TAG, "onShutterButtonClick stopVideoRecording");
                    stopVideoRecording(true, true);
                }
            } else if (!checkCallingState()) {
                str = this.TAG;
                str2 = "ignore in calling state";
            } else {
                Handler handler = this.mHandler;
                if (handler == null || handler.hasMessages(256) || !CameraSettings.isCameraSoundOpen()) {
                    setTriggerMode(i);
                    Log.u(this.TAG, "onShutterButtonClick startVideoRecording");
                    startVideoRecording();
                } else {
                    playCameraSound(2);
                    this.mHandler.sendEmptyMessageDelayed(256, 300);
                }
            }
            return;
        }
        Log.d(str, str2);
    }

    public void onShutterButtonFocus(boolean z, int i) {
    }

    public boolean onShutterButtonLongClick() {
        return false;
    }

    public void onShutterButtonLongClickCancel(boolean z) {
    }

    public void onSingleTapUp(int i, int i2, boolean z) {
        if (!this.mPaused) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (!(camera2Proxy == null || !camera2Proxy.isSessionReady() || !isInTapableRect(i, i2) || getCameraState() == 3 || getCameraState() == 0)) {
                if (!isFrameAvailable()) {
                    Log.w(this.TAG, "onSingleTapUp: frame not available");
                } else if ((!isFrontCamera() || !this.mActivity.isScreenSlideOff()) && !((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromTapDown(i, i2)) {
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
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void onSurfaceTextureReleased() {
        MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
        if (miLiveConfigChanges != null) {
            miLiveConfigChanges.onSurfaceTextureReleased();
        }
    }

    public void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
        MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
        if (miLiveConfigChanges != null) {
            miLiveConfigChanges.onSurfaceTextureUpdated(drawExtTexAttribute);
        }
    }

    /* access modifiers changed from: protected */
    public void onThermalConstrained() {
        super.onThermalConstrained();
        onReviewCancelClicked();
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
                Log.w(this.TAG, "ignore volume key");
                return;
            }
            onShutterButtonClick(i);
        }
    }

    public void playFocusSound(int i) {
        playCameraSound(i);
    }

    public void registerProtocol() {
        Camera camera;
        int[] iArr;
        ImplFactory implFactory;
        super.registerProtocol();
        Log.d(this.TAG, "registerProtocol");
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
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

    public void resumePreview() {
        previewWhenSessionSuccess();
    }

    public void setFrameAvailable(boolean z) {
        super.setFrameAvailable(z);
        if (z && CameraSettings.isCameraSoundOpen()) {
            this.mActivity.loadCameraSound(2);
            this.mActivity.loadCameraSound(3);
        }
    }

    public boolean shouldCaptureDirectly() {
        return false;
    }

    public boolean shouldReleaseLater() {
        return isRecording() || isSaving();
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
            Log.v(this.TAG, "startFocus");
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
            this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            Log.d(this.TAG, "LiveModule, startPreview");
            checkDisplayOrientation();
            MiLiveConfigChanges miLiveConfigChanges = this.mLiveConfigChanges;
            CameraSize cameraSize = this.mVideoSize;
            miLiveConfigChanges.initPreview(cameraSize.width, cameraSize.height, this.mBogusCameraId, this.mActivity.getCameraScreenNail());
            SurfaceTexture inputSurfaceTexture = this.mLiveConfigChanges.getInputSurfaceTexture();
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("InputSurfaceTexture ");
            sb.append(inputSurfaceTexture);
            Log.d(str, sb.toString());
            this.mCamera2Device.startPreviewSession(inputSurfaceTexture == null ? new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture()) : new Surface(inputSurfaceTexture), 0, 0, null, getOperatingMode(), false, this);
        }
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

    public void stopVideoRecording(boolean z, boolean z2) {
        Log.k(3, this.TAG, "MiLive stopVideoRecording");
        boolean z3 = false;
        setCameraAudioRestriction(false);
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(256)) {
            this.mHandler.removeMessages(256);
            Log.d(this.TAG, "skip stopVideoRecording & remove startVideoRecording");
        } else if (isAlive() && this.mLiveConfigChanges != null) {
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
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("stopVideoRecording checkRecordingTime ");
            sb.append(z);
            sb.append(", showReview = ");
            sb.append(z2);
            Log.d(str, sb.toString());
            keepScreenOnAwhile();
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (this.mLiveConfigChanges.canRecordingStop() || !z) {
                if (z2) {
                    if (recordState != null) {
                        recordState.onPostPreview();
                        showReview();
                    } else {
                        Log.d(this.TAG, "record state post preview fail~");
                    }
                }
                this.mLiveConfigChanges.stopRecording();
                playCameraSound(3);
                BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
                if (bluetoothHeadset != null) {
                    bluetoothHeadset.startBluetoothSco(getModuleIndex());
                    z3 = bluetoothHeadset.isBluetoothScoOn();
                }
                if (z3) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(VideoAttr.PARAM_BLUETOOTH_SCO, "on");
                    MistatsWrapper.mistatEvent("M_miLive_", hashMap);
                }
            } else {
                Log.d(this.TAG, "too fast to stop recording.");
            }
        }
    }

    public void takePreviewSnapShoot() {
        if (getCameraState() != 3 && System.currentTimeMillis() - this.mCaptureTime >= 500) {
            setCameraState(3);
            this.mCamera2Device.setShotType(-8);
            this.mCamera2Device.takeSimplePicture(this, this.mActivity.getImageSaver(), this.mActivity.getCameraScreenNail());
            this.mCaptureTime = System.currentTimeMillis();
        }
    }

    public void unRegisterModulePersistProtocol() {
        super.unRegisterModulePersistProtocol();
        Log.d(this.TAG, "unRegisterModulePersistProtocol");
        getActivity().getImplFactory().detachModulePersistent();
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        Log.d(this.TAG, "unRegisterProtocol");
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(236, this);
        getActivity().getImplFactory().detachAdditional();
    }

    /* access modifiers changed from: protected */
    public void unlockAEAF() {
        Log.d(this.TAG, "unlockAEAF");
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
            Log.e(this.TAG, "updateFocusArea: null camera device");
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
