package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
import android.os.SystemProperties;
import android.util.Range;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import androidx.annotation.MainThread;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.SensorStateManager.SensorStateListener;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FaceAnalyzeInfo;
import com.android.camera.fragment.CtaNoticeFragment;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.fragment.beauty.LiveBeautyFilterFragment.LiveFilterItem;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.log.Log;
import com.android.camera.module.impl.ImplFactory;
import com.android.camera.module.impl.component.MimojiAvatarEngineImpl;
import com.android.camera.module.loader.FunctionAsdSceneDetect;
import com.android.camera.module.loader.FunctionFaceDetect;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.module.loader.camera2.FocusManager2.Listener;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.FullScreenProtocol;
import com.android.camera.protocol.ModeProtocol.LiveConfigChanges;
import com.android.camera.protocol.ModeProtocol.LiveVideoEditor;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.Live;
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
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LiveModule extends BaseModule implements ILiveModule, CameraPreviewCallback, FocusCallback, FaceDetectionCallback, CameraAction, PictureCallback, Listener {
    private static final int BEAUTY_SWITCH = 8;
    private static final int FILTER_SWITCH = 2;
    private static final int FRAME_RATE = 30;
    private static final int STICKER_SWITCH = 4;
    /* access modifiers changed from: private */
    public static final String TAG = "LiveModule";
    private static final int VALID_VIDEO_TIME = 500;
    private boolean m3ALocked;
    private String mBaseFileName;
    protected BeautyValues mBeautyValues;
    private String mCaptureWaterMarkStr;
    private CtaNoticeFragment mCtaNoticeFragment;
    private int mDeviceOrientation = 90;
    private boolean mDisableSingleTapUp = false;
    protected boolean mFaceDetectionEnabled;
    protected boolean mFaceDetectionStarted;
    /* access modifiers changed from: private */
    public FocusManager2 mFocusManager;
    private boolean mIsLowLight;
    private boolean mIsPreviewing = false;
    /* access modifiers changed from: private */
    public LiveConfigChanges mLiveConfigChanges;
    private LiveVideoEditor mLiveVideoEditor;
    private Disposable mMetaDataDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mMetaDataFlowableEmitter;
    private MimojiAvatarEngineImpl mMimojiAvatarEngine;
    private int mOldOriginVolumeStream;
    /* access modifiers changed from: private */
    public long mOnResumeTime;
    private boolean mOpenFlash = false;
    private int mQuality = 5;
    private boolean mSaved;
    protected SensorStateListener mSensorStateListener = new SensorStateListener() {
        public boolean isWorking() {
            return LiveModule.this.isAlive() && LiveModule.this.getCameraState() != 0;
        }

        public void notifyDevicePostureChanged() {
            LiveModule.this.mActivity.getEdgeShutterView().onDevicePostureChanged();
        }

        public void onDeviceBecomeStable() {
            Log.v(LiveModule.TAG, "onDeviceBecomeStable");
        }

        public void onDeviceBeginMoving() {
        }

        public void onDeviceKeepMoving(double d) {
            if (!LiveModule.this.mMainProtocol.isEvAdjusted(true) && !LiveModule.this.mPaused && Util.isTimeout(System.currentTimeMillis(), LiveModule.this.mTouchFocusStartingTime, 3000) && !LiveModule.this.is3ALocked() && LiveModule.this.mFocusManager != null && LiveModule.this.mFocusManager.isNeedCancelAutoFocus() && !LiveModule.this.isRecording()) {
                LiveModule.this.mFocusManager.onDeviceKeepMoving(d);
            }
        }

        public void onDeviceKeepStable() {
        }

        public void onDeviceLieChanged(boolean z) {
        }

        public void onDeviceOrientationChanged(float f, boolean z) {
            LiveModule liveModule = LiveModule.this;
            if (z) {
                f = (float) liveModule.mOrientation;
            }
            liveModule.mDeviceRotation = f;
            if (LiveModule.this.isGradienterOn) {
                EffectController instance = EffectController.getInstance();
                LiveModule liveModule2 = LiveModule.this;
                instance.setDeviceRotation(z, Util.getShootRotation(liveModule2.mActivity, liveModule2.mDeviceRotation));
            }
        }

        public void onDeviceRotationChanged(float[] fArr) {
            if (LiveModule.this.mLiveConfigChanges != null) {
                LiveModule.this.mLiveConfigChanges.onDeviceRotationChange(fArr);
            }
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (LiveModule.this.mLiveConfigChanges != null) {
                LiveModule.this.mLiveConfigChanges.onSensorChanged(sensorEvent);
            }
        }
    };
    protected boolean mShowFace = false;
    /* access modifiers changed from: private */
    public long mTouchFocusStartingTime;
    private volatile boolean mVideoRecordStopped = false;
    private boolean misFaceLocationOk;

    class LiveAsdConsumer implements Consumer {
        private LiveAsdConsumer() {
        }

        public void accept(Integer num) {
            LiveModule.this.mimojiLightDetect(num);
        }
    }

    class MainHandler extends Handler {
        private WeakReference mModule;

        public MainHandler(LiveModule liveModule, Looper looper) {
            super(looper);
            this.mModule = new WeakReference(liveModule);
        }

        public void handleMessage(Message message) {
            LiveModule liveModule = (LiveModule) this.mModule.get();
            if (liveModule != null) {
                if (!liveModule.isCreated()) {
                    removeCallbacksAndMessages(null);
                } else if (liveModule.getActivity() != null) {
                    int i = message.what;
                    if (i == 2) {
                        liveModule.getWindow().clearFlags(128);
                    } else if (i == 4) {
                        liveModule.checkActivityOrientation();
                        if (SystemClock.uptimeMillis() - liveModule.mOnResumeTime < 5000) {
                            sendEmptyMessageDelayed(4, 100);
                        }
                    } else if (i == 9) {
                        if (liveModule.getActivity().getVolumeControlStream() != 1) {
                            liveModule.getActivity().setVolumeControlStream(1);
                        }
                        liveModule.mMainProtocol.initializeFocusView(liveModule);
                    } else if (i == 17) {
                        removeMessages(17);
                        removeMessages(2);
                        liveModule.getWindow().addFlags(128);
                        sendEmptyMessageDelayed(2, (long) liveModule.getScreenDelay());
                    } else if (i == 31) {
                        liveModule.setOrientationParameter();
                    } else if (i == 35) {
                        boolean z = false;
                        boolean z2 = message.arg1 > 0;
                        if (message.arg2 > 0) {
                            z = true;
                        }
                        liveModule.handleUpdateFaceView(z2, z);
                    } else if (i == 51 && liveModule.getActivity() != null && liveModule.getActivity().isActivityPaused()) {
                        liveModule.mOpenCameraFail = true;
                        liveModule.onCameraException();
                    }
                }
            }
        }
    }

    static /* synthetic */ void O00oo0O0() {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            mainContentProtocol.setCenterHint(8, null, null, 0);
        }
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
        String str2 = TAG;
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
        sb8.append(Integer.toString(this.mPreviewSize.width));
        sb8.append("x");
        sb8.append(Integer.toString(this.mPreviewSize.height));
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
        } else if (this.mFaceDetectionStarted) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null && 1 != camera2Proxy.getFocusMode()) {
                mainContentProtocol = this.mMainProtocol;
                z4 = true;
                z3 = true;
                i = this.mCameraDisplayOrientation;
                z5 = z;
            } else {
                return;
            }
        } else {
            return;
        }
        mainContentProtocol.updateFaceView(z5, z4, isFrontCamera, z3, i);
    }

    private int initLiveConfig() {
        this.mLiveConfigChanges = (LiveConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(201);
        this.mLiveVideoEditor = (LiveVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(209);
        if (this.mLiveConfigChanges == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 201, 209);
            this.mLiveConfigChanges = (LiveConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(201);
            this.mLiveVideoEditor = (LiveVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(209);
            this.mLiveConfigChanges.initResource();
        }
        return this.mLiveConfigChanges.getAuthResult();
    }

    private void initMetaParser() {
        this.mMetaDataDisposable = Flowable.create(new FlowableOnSubscribe() {
            public void subscribe(FlowableEmitter flowableEmitter) {
                LiveModule.this.mMetaDataFlowableEmitter = flowableEmitter;
            }
        }, BackpressureStrategy.DROP).map(new FunctionFaceDetect(this, isFrontCamera())).map(new FunctionAsdSceneDetect(this, getCameraCapabilities())).subscribe((Consumer) new LiveAsdConsumer());
    }

    private void initMimojiEngine() {
        this.mMimojiAvatarEngine = (MimojiAvatarEngineImpl) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
        if (this.mMimojiAvatarEngine == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 217);
            this.mMimojiAvatarEngine = (MimojiAvatarEngineImpl) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
            this.mMimojiAvatarEngine.onDeviceRotationChange(this.mDeviceOrientation);
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
        FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
        if (fullScreenProtocol == null) {
            return false;
        }
        return fullScreenProtocol.isLiveRecordSaving();
    }

    /* access modifiers changed from: private */
    public void mimojiLightDetect(Integer num) {
        MimojiAvatarEngineImpl mimojiAvatarEngineImpl = this.mMimojiAvatarEngine;
        if (mimojiAvatarEngineImpl != null && mimojiAvatarEngineImpl.isOnCreateMimoji()) {
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

    private void resetZoomPreview() {
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
                CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new C0367O000OoOo(this));
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

    private void showAuthError() {
        this.mHandler.post(new Runnable() {
            public void run() {
                Camera camera = LiveModule.this.mActivity;
                if (camera != null && !camera.isFinishing()) {
                    Builder builder = new Builder(LiveModule.this.mActivity);
                    builder.setTitle(R.string.live_error_title);
                    builder.setMessage(R.string.live_error_message);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.live_error_confirm, new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LiveModule.this.mActivity.startActivity(new Intent("android.settings.DATE_SETTINGS"));
                        }
                    });
                    builder.setNegativeButton(R.string.snap_cancel, new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    private void showPreview() {
        pausePreview();
        this.mSaved = false;
        ((FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196)).startLiveRecordPreview(genContentValues(2, 0, false));
        this.mIsPreviewing = true;
    }

    private void startScreenLight(final int i, final int i2) {
        if (!this.mPaused) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    Camera camera = LiveModule.this.mActivity;
                    if (camera != null) {
                        camera.setWindowBrightness(i2);
                    }
                    FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
                    if (fullScreenProtocol != null) {
                        fullScreenProtocol.setScreenLightColor(i);
                        fullScreenProtocol.showScreenLight();
                    }
                }
            });
        }
    }

    @MainThread
    private void startVideoRecording() {
        setCameraAudioRestriction(true);
        keepScreenOn();
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        recordState.onPrepare();
        LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
        if (liveConfigChanges != null) {
            liveConfigChanges.onRecordStart();
            DataRepository.dataItemLive().setLiveStartOrientation(this.mOrientation);
            CameraStatUtils.trackLiveClick(Live.LIVE_CLICK_START);
            trackLiveRecordingParams();
            this.mOldOriginVolumeStream = getActivity().getVolumeControlStream();
            getActivity().setVolumeControlStream(3);
        }
        recordState.onStart();
        resetZoomRecoding();
        listenPhoneState(true);
        if (this.mMimojiAvatarEngine != null) {
            this.mMimojiAvatarEngine.onRecordStart(genContentValues(2, 0, false));
        }
        this.mVideoRecordStopped = false;
        HashMap hashMap = new HashMap();
        hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
        trackGeneralInfo(hashMap, 1, false, this.mBeautyValues, false, -1);
    }

    private void stopScreenLight() {
        this.mHandler.post(new Runnable() {
            public void run() {
                Camera camera = LiveModule.this.mActivity;
                if (camera != null) {
                    camera.restoreWindowBrightness();
                }
                FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
                String access$600 = LiveModule.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("stopScreenLight: protocol = ");
                sb.append(fullScreenProtocol);
                sb.append(", mHandler = ");
                sb.append(LiveModule.this.mHandler);
                Log.d(access$600, sb.toString());
                if (fullScreenProtocol != null) {
                    fullScreenProtocol.hideScreenLight();
                }
            }
        });
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

    private void trackLiveVideoParams() {
        int segmentSize = this.mLiveConfigChanges.getSegmentSize();
        float totalRecordingTime = (float) this.mLiveConfigChanges.getTotalRecordingTime();
        int liveAllSwitchAllValue = CameraSettings.getLiveAllSwitchAllValue();
        boolean z = true;
        boolean z2 = (liveAllSwitchAllValue & 2) != 0;
        boolean z3 = (liveAllSwitchAllValue & 4) != 0;
        if ((liveAllSwitchAllValue & 8) == 0) {
            z = false;
        }
        CameraSettings.setLiveAllSwitchAddValue(0);
        CameraStatUtils.trackLiveVideoParams(segmentSize, totalRecordingTime / 1000.0f, z2, z3, z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0066, code lost:
        if (r4 != null) goto L_0x0071;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x006f, code lost:
        if (r4 != null) goto L_0x0071;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateBeauty() {
        LiveConfigChanges liveConfigChanges;
        boolean z;
        if (this.mModuleIndex != 174) {
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
            return;
        }
        float faceBeautyRatio = ((float) CameraSettings.getFaceBeautyRatio("key_live_shrink_face_ratio")) / 100.0f;
        float faceBeautyRatio2 = ((float) CameraSettings.getFaceBeautyRatio("key_live_enlarge_eye_ratio")) / 100.0f;
        float faceBeautyRatio3 = ((float) CameraSettings.getFaceBeautyRatio("key_live_smooth_strength")) / 100.0f;
        if (faceBeautyRatio > 0.0f || faceBeautyRatio2 > 0.0f || faceBeautyRatio3 > 0.0f) {
            z = true;
            CameraSettings.setLiveBeautyStatus(true);
            liveConfigChanges = this.mLiveConfigChanges;
        } else {
            z = false;
            CameraSettings.setLiveBeautyStatus(false);
            liveConfigChanges = this.mLiveConfigChanges;
        }
        liveConfigChanges.setBeautyFaceReshape(z, faceBeautyRatio2, faceBeautyRatio);
        this.mLiveConfigChanges.setBeautify(z, faceBeautyRatio3);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("shrinkFaceRatio->");
        sb2.append(faceBeautyRatio);
        sb2.append(",enlargeEyeRatio->");
        sb2.append(faceBeautyRatio2);
        sb2.append(",smoothStrengthRatio->");
        sb2.append(faceBeautyRatio3);
        Log.d(str2, sb2.toString());
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

    private void updateLiveRelated() {
        int i = this.mModuleIndex;
        if (i == 177 || i == 184) {
            MimojiAvatarEngineImpl mimojiAvatarEngineImpl = this.mMimojiAvatarEngine;
            int i2 = this.mCameraDisplayOrientation;
            int i3 = isFrontCamera() ? 270 : 90;
            CameraSize cameraSize = this.mPreviewSize;
            mimojiAvatarEngineImpl.initAvatarEngine(i2, i3, cameraSize.width, cameraSize.height, isFrontCamera());
            this.mCamera2Device.startPreviewCallback(this.mMimojiAvatarEngine, null);
        }
    }

    private void updatePictureAndPreviewSize() {
        List supportedOutputSizeWithAssignedMode = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class);
        int i = this.mModuleIndex;
        CameraSize optimalPreviewSize = Util.getOptimalPreviewSize(false, this.mBogusCameraId, supportedOutputSizeWithAssignedMode, (double) (i == 177 ? Util.getRatio(CameraSettings.getPictureSizeRatioString(i)) : 1.7777777f), new CameraSize(1280, Util.LIMIT_SURFACE_WIDTH));
        this.mPreviewSize = optimalPreviewSize;
        this.mPictureSize = this.mPreviewSize;
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
        int i2 = this.mModuleIndex;
        if (i2 != 177) {
            if (i2 == 174) {
                updateCameraScreenNailSize(optimalVideoSnapshotPictureSize.height, optimalVideoSnapshotPictureSize.width);
                return;
            } else if (i2 != 184) {
                return;
            }
        }
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

    public /* synthetic */ void O00oo0O() {
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
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.getCameraScreenNail().setExternalFrameProcessor(null);
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setFocusCallback(null);
            this.mCamera2Device.setErrorCallback(null);
            this.mCamera2Device.setMetaDataCallback(null);
            if (this.mModuleIndex == 177) {
                this.mCamera2Device.stopPreviewCallback(true);
            }
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

    public void doReverse() {
        LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
        if (liveConfigChanges != null && !liveConfigChanges.isRecording()) {
            Log.u(TAG, "doReverse");
            this.mLiveConfigChanges.onRecordReverse();
            if (!this.mLiveConfigChanges.canRecordingStop()) {
                BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
                if (backStack != null) {
                    backStack.handleBackStackFromKeyBack();
                }
                stopVideoRecording(false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean enableFaceDetection() {
        return DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_FACE_DETECTION, getResources().getBoolean(R.bool.pref_camera_facedetection_default));
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        int i = isPreviewEisOn() ? 32772 : C0122O00000o.instance().Oo0Oo0O() ? CameraCapabilities.SESSION_OPERATION_MODE_MCTF : 0;
        return this.mModuleIndex == 177 ? CameraCapabilities.SESSION_OPERATION_MODE_MIMOJI : i;
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
                LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
                if (liveConfigChanges != null && !liveConfigChanges.isRecording()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDoingAction() {
        FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
        return isRecording() || getCameraState() == 3 || (fullScreenProtocol != null && fullScreenProtocol.isLiveRecordPreviewShown()) || this.mOpenFlash;
    }

    public boolean isFaceDetectStarted() {
        return this.mFaceDetectionStarted;
    }

    public boolean isNeedMute() {
        return isRecording();
    }

    public boolean isRecording() {
        LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
        if (liveConfigChanges == null || !liveConfigChanges.isRecording()) {
            MimojiAvatarEngineImpl mimojiAvatarEngineImpl = this.mMimojiAvatarEngine;
            if (mimojiAvatarEngineImpl == null || !mimojiAvatarEngineImpl.isRecording()) {
                return false;
            }
        }
        return true;
    }

    public boolean isRecordingPaused() {
        LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
        return liveConfigChanges != null && liveConfigChanges.isRecordingPaused();
    }

    public boolean isSelectingCapturedResult() {
        FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
        return fullScreenProtocol != null && fullScreenProtocol.isLiveRecordPreviewShown();
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
            stopVideoRecording(true);
        }
        super.notifyError();
    }

    public void notifyFocusAreaUpdate() {
        updatePreferenceTrampoline(3);
    }

    public boolean onBackPressed() {
        if (!isFrameAvailable()) {
            if (isSelectingCapturedResult()) {
                return true;
            }
            if (DataRepository.dataItemLive().getRecordSegmentTimeInfo() == null) {
                return false;
            }
            Log.d(TAG, "onBackPressed skip caz recorder paused.");
            return true;
        } else if (this.mMimojiAvatarEngine != null && DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiCreate()) {
            ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onMimojiCreateBack();
            CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_CREATE_SOFT_BACK, BaseEvent.CREATE);
            return true;
        } else if (this.mModuleIndex != 177) {
            LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
            if (liveConfigChanges == null) {
                return super.onBackPressed();
            }
            if (!liveConfigChanges.isRecording() && !this.mLiveConfigChanges.isRecordingPaused()) {
                return super.onBackPressed();
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
                this.mLastBackPressedTime = currentTimeMillis;
                ToastUtils.showToast((Context) this.mActivity, (int) R.string.record_back_pressed_hint, true);
            } else {
                stopVideoRecording(false);
            }
            return true;
        } else if (!isRecording()) {
            return super.onBackPressed();
        } else {
            stopVideoRecording(true);
            return true;
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
        int i = this.mModuleIndex;
        if (i == 174) {
            int initLiveConfig = initLiveConfig();
            if (!(initLiveConfig == 0 || initLiveConfig == 1 || (initLiveConfig != 2 && initLiveConfig != 3 && initLiveConfig != 4))) {
                showAuthError();
                return;
            }
        } else if (i == 177) {
            initMimojiEngine();
        }
        startPreview();
        if (DataRepository.dataItemGlobal().isFirstShowCTAConCollect() && this.mModuleIndex == 174) {
            this.mCtaNoticeFragment = CtaNoticeFragment.showCta(getActivity().getFragmentManager(), null, 1);
        }
        this.mOnResumeTime = SystemClock.uptimeMillis();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
        initMetaParser();
    }

    public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
    }

    public void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4) {
        setWaterMark();
        this.mActivity.getCameraScreenNail().animateCapture(this.mOrientation);
        playCameraSound(0);
        this.mMimojiAvatarEngine.onCaptureImage();
    }

    public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
        return null;
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        this.mHandler = new MainHandler(this, this.mActivity.getMainLooper());
        onCameraOpened();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
        this.mActivity.getSensorStateManager().setRotationVectorEnabled(true);
    }

    public void onDestroy() {
        super.onDestroy();
        CtaNoticeFragment ctaNoticeFragment = this.mCtaNoticeFragment;
        if (ctaNoticeFragment != null) {
            ctaNoticeFragment.dismiss();
        }
        this.mHandler.post(C0366O000OoOO.INSTANCE);
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
            MimojiAvatarEngineImpl mimojiAvatarEngineImpl = this.mMimojiAvatarEngine;
            if (mimojiAvatarEngineImpl != null && mimojiAvatarEngineImpl.isOnCreateMimoji()) {
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
            onPauseButtonClick();
        } else if (isSaving()) {
            return;
        }
        doLaterReleaseIfNeed();
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
                        ((FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196)).startLiveRecordSaving();
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
                stopVideoRecording(false);
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

    public void onMimojiCaptureCallback() {
        setCameraState(1);
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.onParallelImagePostProcStart();
        }
        if (this.mOpenFlash) {
            this.mOpenFlash = false;
            if (isFrontCamera()) {
                stopScreenLight();
            }
        }
    }

    public void onMimojiCreateCompleted(boolean z) {
        setCameraState(1);
    }

    public void onNewUriArrived(final Uri uri, final String str) {
        super.onNewUriArrived(uri, str);
        if (isAlive()) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
                    if (fullScreenProtocol != null) {
                        ContentValues saveContentValues = fullScreenProtocol.getSaveContentValues();
                        if (saveContentValues != null) {
                            String asString = saveContentValues.getAsString("title");
                            String asString2 = saveContentValues.getAsString("_data");
                            String access$600 = LiveModule.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("newUri: ");
                            sb.append(str);
                            sb.append(" | ");
                            sb.append(asString);
                            Log.d(access$600, sb.toString());
                            if (asString.equals(str)) {
                                fullScreenProtocol.onLiveSaveToLocalFinished(uri, asString2);
                            }
                        }
                    }
                }
            });
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        setOrientation(i, i2);
        if (this.mLiveConfigChanges != null) {
            if (i <= 45 || i > 315) {
                i = 0;
            }
            if (i > 45 && i <= 135) {
                i = 90;
            }
            if (i > 135 && i <= 225) {
                i = 180;
            }
            if (i > 225) {
                i = 270;
            }
            this.mLiveConfigChanges.updateRotation(0.0f, 0.0f, (float) i);
        }
        this.mDeviceOrientation = i;
        MimojiAvatarEngineImpl mimojiAvatarEngineImpl = this.mMimojiAvatarEngine;
        if (mimojiAvatarEngineImpl != null) {
            mimojiAvatarEngineImpl.onDeviceRotationChange(i);
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
    }

    public void onPauseButtonClick() {
        Log.u(TAG, "onPauseButtonClick");
        if (this.mModuleIndex == 177) {
            if (isRecording()) {
                Log.u(TAG, "onPauseButtonClick stopVideoRecording");
                stopVideoRecording(true);
            }
        } else if (!this.mVideoRecordStopped) {
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (this.mLiveConfigChanges.isRecording()) {
                this.mLiveConfigChanges.onRecordPause();
                resetZoomPreview();
                if (recordState != null) {
                    Log.u(TAG, "onPauseButtonClick onPause");
                    recordState.onPause();
                }
            } else {
                resetZoomRecoding();
                trackLiveRecordingParams();
                this.mLiveConfigChanges.onRecordResume();
                if (recordState != null) {
                    Log.u(TAG, "onPauseButtonClick onResume");
                    recordState.onResume();
                }
            }
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

    public void onPreviewRelease() {
        if (isRecording()) {
            LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
            if (liveConfigChanges != null) {
                liveConfigChanges.onRecordStop();
            }
        }
        super.onPreviewRelease();
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
        LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
        if (liveConfigChanges != null) {
            liveConfigChanges.onRecordStop();
        }
        ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onFinish();
        getActivity().setVolumeControlStream(this.mOldOriginVolumeStream);
        this.mIsPreviewing = false;
        resetZoomPreview();
        if (!doLaterReleaseIfNeed()) {
            if (this.mCamera2Device.isSessionReady()) {
                resumePreview();
            } else {
                startPreview();
            }
        }
    }

    public void onReviewDoneClicked() {
        LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
        if (liveConfigChanges != null) {
            liveConfigChanges.onRecordStop();
        }
        ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onFinish();
        getActivity().setVolumeControlStream(this.mOldOriginVolumeStream);
        this.mIsPreviewing = false;
        startSaveToLocal();
        resetZoomPreview();
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
        Log.u(TAG, "onShutterButtonClick ");
        if (i == 110) {
            this.mActivity.onUserInteraction();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.hideRecommendDescTip(FragmentTopAlert.TIP_SPEECH_SHUTTER_DESC);
            }
        }
        if (!isRecording()) {
            LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
            if (liveConfigChanges == null || !liveConfigChanges.isRecordingPaused()) {
                if (this.mModuleIndex != 177 && !checkCallingState()) {
                    Log.d(TAG, "ignore in calling state");
                    RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                    recordState.onPrepare();
                    recordState.onFailed();
                    return;
                } else if (this.mMimojiAvatarEngine != null) {
                    if (DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiPreview()) {
                        turnOnFlashIfNeed();
                        onMimojiCapture();
                        HashMap hashMap = new HashMap();
                        hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
                        trackGeneralInfo(hashMap, 1, false, this.mBeautyValues, false, 0);
                    } else if (DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiCreate()) {
                        Log.d(TAG, "start create mimoji");
                        if (this.mIsLowLight) {
                            Log.d(TAG, "mimoji create low light!");
                            return;
                        } else if (this.mMimojiAvatarEngine.onCreateCapture()) {
                            Log.d(TAG, "create mimoji success");
                            setCameraState(3);
                        }
                    }
                    return;
                } else {
                    Log.u(TAG, "onShutterButtonClick startVideoRecording");
                    startVideoRecording();
                }
            }
        }
        Log.u(TAG, "onShutterButtonClick stopVideoRecording");
        stopVideoRecording(false);
    }

    public void onShutterButtonFocus(boolean z, int i) {
    }

    public boolean onShutterButtonLongClick() {
        if (!checkCallingState()) {
            Log.d(TAG, "ignore onShutterButtonLongClick in calling state");
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            recordState.onPrepare();
            recordState.onFailed();
            return false;
        }
        if (!isRecording()) {
            MimojiAvatarEngineImpl mimojiAvatarEngineImpl = this.mMimojiAvatarEngine;
            if (mimojiAvatarEngineImpl == null || mimojiAvatarEngineImpl.isOnCreateMimoji()) {
                return false;
            }
            Log.u(TAG, "onShutterButtonLongClick startVideoRecording");
            startVideoRecording();
            return true;
        }
        return false;
    }

    public void onShutterButtonLongClickCancel(boolean z) {
        if (isRecording()) {
            MimojiAvatarEngineImpl mimojiAvatarEngineImpl = this.mMimojiAvatarEngine;
            if (mimojiAvatarEngineImpl != null && !mimojiAvatarEngineImpl.isRecordStopping()) {
                stopVideoRecording(false);
            }
        }
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
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /* access modifiers changed from: protected */
    public void onThermalConstrained() {
        super.onThermalConstrained();
        if (!isRecording()) {
            LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
            if (liveConfigChanges == null || !liveConfigChanges.isRecordingPaused()) {
                return;
            }
        }
        stopVideoRecording(false);
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
        LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
        if (liveConfigChanges != null) {
            liveConfigChanges.setEffectAudio(false);
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
        LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
        if (liveConfigChanges != null) {
            liveConfigChanges.setEffectAudio(true);
        }
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
            Log.d(TAG, "LiveModule, startPreview");
            checkDisplayOrientation();
            int i = isFrontCamera() ? 270 : 90;
            Surface surface = new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture());
            Surface surface2 = null;
            int i2 = 0;
            int i3 = this.mModuleIndex;
            if (i3 == 174) {
                surface2 = new Surface(this.mLiveConfigChanges.getInputSurfaceTexture());
                LiveConfigChanges liveConfigChanges = this.mLiveConfigChanges;
                CameraSize cameraSize = this.mPreviewSize;
                liveConfigChanges.initPreview(cameraSize.height, cameraSize.width, isFrontCamera(), i);
                LiveVideoEditor liveVideoEditor = this.mLiveVideoEditor;
                CameraSize cameraSize2 = this.mPreviewSize;
                liveVideoEditor.setRecordParameter(cameraSize2.height, cameraSize2.width, DataRepository.dataItemLive().getLiveStartOrientation());
                this.mLiveConfigChanges.startPreview(surface);
            } else if (i3 == 177) {
                this.mActivity.getCameraScreenNail().setExternalFrameProcessor(this.mMimojiAvatarEngine);
                i2 = 1;
            }
            int i4 = i2;
            if (this.mModuleIndex == 174) {
                this.mCamera2Device.setFpsRange(new Range(Integer.valueOf(30), Integer.valueOf(30)));
            }
            if (!isSelectingCapturedResult()) {
                this.mCamera2Device.startPreviewSession(surface2 == null ? surface : surface2, i4, 0, null, getOperatingMode(), false, this);
            }
        }
    }

    public void startSaveToLocal() {
        if (!this.mSaved) {
            ContentValues saveContentValues = ((FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196)).getSaveContentValues();
            if (saveContentValues != null) {
                this.mSaved = true;
                saveContentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
                getActivity().getImageSaver().addVideo(saveContentValues.getAsString("_data"), saveContentValues, true);
            }
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

    /* JADX WARNING: Removed duplicated region for block: B:24:0x00ea  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stopVideoRecording(boolean z) {
        String str;
        String sb;
        boolean z2 = z;
        boolean z3 = false;
        setCameraAudioRestriction(false);
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (isAlive() && recordState != null) {
            keepScreenOnAwhile();
            if (this.mMimojiAvatarEngine != null) {
                recordState.onFinish();
                this.mMimojiAvatarEngine.onRecordStop(z2);
                return;
            }
            this.mLiveConfigChanges.onRecordPause();
            int liveStartOrientation = DataRepository.dataItemLive().getLiveStartOrientation();
            LiveVideoEditor liveVideoEditor = this.mLiveVideoEditor;
            CameraSize cameraSize = this.mPreviewSize;
            liveVideoEditor.setRecordParameter(cameraSize.height, cameraSize.width, liveStartOrientation);
            boolean canRecordingStop = this.mLiveConfigChanges.canRecordingStop();
            boolean z4 = this.mLiveConfigChanges.getTotalRecordingTime() < 500;
            if (!canRecordingStop) {
                str = TAG;
                sb = "onFinish of no segments !!";
            } else if (z4) {
                str = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Discard , total capture time is :");
                sb2.append(this.mLiveConfigChanges.getTotalRecordingTime());
                sb = sb2.toString();
            } else {
                recordState.onPostPreview();
                listenPhoneState(false);
                trackLiveVideoParams();
                CameraStatUtils.trackVideoRecorded(isFrontCamera(), getActualCameraId(), getModuleIndex(), false, false, CameraSettings.isUltraWideConfigOpen(getModuleIndex()), "live", this.mQuality, this.mCamera2Device.getFlashMode(), 30, 0, null, this.mLiveConfigChanges.getTotalRecordingTime() / 1000, false, null, false, this.mIsAutoHibernationSupported, this.mEnterAutoHibernationCount, CameraSettings.isVhdrOn(this.mCameraCapabilities, this.mModuleIndex));
                if (!z2) {
                    showPreview();
                }
                if (z2 || !canRecordingStop || z4) {
                    z3 = true;
                }
                if (z3) {
                    resetZoomPreview();
                }
                this.mVideoRecordStopped = true;
            }
            Log.d(str, sb);
            this.mLiveConfigChanges.onRecordStop();
            recordState.onFinish();
            z3 = true;
            if (z3) {
            }
            this.mVideoRecordStopped = true;
        }
    }

    public void turnOnFlashIfNeed() {
        if (this.mCamera2Device.isNeedFlashOn()) {
            this.mOpenFlash = true;
            if (101 == this.mCamera2Device.getFlashMode()) {
                startScreenLight(Util.getScreenLightColor(SystemProperties.getInt("camera_screen_light_wb", 0)), CameraSettings.getScreenLightBrightness());
            }
        }
    }

    public void unRegisterModulePersistProtocol() {
        super.unRegisterModulePersistProtocol();
        Log.d(TAG, "unRegisterModulePersistProtocol");
        getActivity().getImplFactory().detachModulePersistent();
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(167, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(167, this);
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
