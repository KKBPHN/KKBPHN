package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
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
import android.util.Range;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.SensorStateManager.SensorStateListener;
import com.android.camera.Util;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.data.observeable.VMProcessing;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FaceAnalyzeInfo;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.CtaNoticeFragment;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.log.Log;
import com.android.camera.module.impl.ImplFactory;
import com.android.camera.module.loader.FunctionAsdSceneDetect;
import com.android.camera.module.loader.FunctionFaceDetect;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.StartControlFeatureDetail;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.module.loader.camera2.FocusManager2.Listener;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.LiveConfigVV;
import com.android.camera.protocol.ModeProtocol.LiveVVProcess;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.storage.Storage;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.FaceDetectionCallback;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.Camera2Proxy.PictureCallback;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LiveModuleSubVV extends BaseModule implements CameraPreviewCallback, FocusCallback, FaceDetectionCallback, CameraAction, PictureCallback, Listener, LifecycleOwner {
    private static final int BEAUTY_SWITCH = 8;
    private static final int FILTER_SWITCH = 2;
    private static final int FRAME_RATE = 30;
    private static final int STICKER_SWITCH = 4;
    /* access modifiers changed from: private */
    public static final String TAG = "LiveModuleSubVV";
    private boolean m3ALocked;
    private String mBaseFileName;
    protected BeautyValues mBeautyValues;
    private String mCaptureWaterMarkStr;
    private CtaNoticeFragment mCtaNoticeFragment;
    protected boolean mFaceDetectionEnabled;
    protected boolean mFaceDetectionStarted;
    /* access modifiers changed from: private */
    public FocusManager2 mFocusManager;
    private boolean mIsPreviewing = false;
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    private LiveConfigVV mLiveConfigChanges;
    private Disposable mMetaDataDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mMetaDataFlowableEmitter;
    private int mOldOriginVolumeStream;
    /* access modifiers changed from: private */
    public long mOnResumeTime;
    private int mQuality = 5;
    private boolean mSaved;
    protected SensorStateListener mSensorStateListener = new SensorStateListener() {
        public boolean isWorking() {
            return LiveModuleSubVV.this.isAlive() && LiveModuleSubVV.this.getCameraState() != 0;
        }

        public void notifyDevicePostureChanged() {
            LiveModuleSubVV.this.mActivity.getEdgeShutterView().onDevicePostureChanged();
        }

        public void onDeviceBecomeStable() {
            Log.v(LiveModuleSubVV.TAG, "onDeviceBecomeStable");
        }

        public void onDeviceBeginMoving() {
        }

        public void onDeviceKeepMoving(double d) {
            if (!LiveModuleSubVV.this.mMainProtocol.isEvAdjusted(true) && !LiveModuleSubVV.this.mPaused && Util.isTimeout(System.currentTimeMillis(), LiveModuleSubVV.this.mTouchFocusStartingTime, 3000) && !LiveModuleSubVV.this.is3ALocked() && LiveModuleSubVV.this.mFocusManager != null && LiveModuleSubVV.this.mFocusManager.isNeedCancelAutoFocus() && !LiveModuleSubVV.this.isRecording()) {
                LiveModuleSubVV.this.mFocusManager.onDeviceKeepMoving(d);
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
    private VMProcessing mVmProcessing;

    class LiveAsdConsumer implements Consumer {
        private LiveAsdConsumer() {
        }

        public void accept(Integer num) {
            LiveModuleSubVV.this.consumeAsdSceneResult(num.intValue());
        }
    }

    class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 2) {
                LiveModuleSubVV.this.getWindow().clearFlags(128);
            } else if (i == 4) {
                LiveModuleSubVV.this.checkActivityOrientation();
                if (SystemClock.uptimeMillis() - LiveModuleSubVV.this.mOnResumeTime < 5000) {
                    LiveModuleSubVV.this.mHandler.sendEmptyMessageDelayed(4, 100);
                }
            } else if (i == 9) {
                LiveModuleSubVV liveModuleSubVV = LiveModuleSubVV.this;
                liveModuleSubVV.mMainProtocol.initializeFocusView(liveModuleSubVV);
            } else if (i == 17) {
                LiveModuleSubVV.this.mHandler.removeMessages(17);
                LiveModuleSubVV.this.mHandler.removeMessages(2);
                LiveModuleSubVV.this.getWindow().addFlags(128);
                LiveModuleSubVV liveModuleSubVV2 = LiveModuleSubVV.this;
                liveModuleSubVV2.mHandler.sendEmptyMessageDelayed(2, (long) liveModuleSubVV2.getScreenDelay());
            } else if (i == 31) {
                LiveModuleSubVV.this.setOrientationParameter();
            } else if (i == 35) {
                LiveModuleSubVV liveModuleSubVV3 = LiveModuleSubVV.this;
                boolean z = false;
                boolean z2 = message.arg1 > 0;
                if (message.arg2 > 0) {
                    z = true;
                }
                liveModuleSubVV3.handleUpdateFaceView(z2, z);
            } else if (i == 51 && !LiveModuleSubVV.this.mActivity.isActivityPaused()) {
                LiveModuleSubVV liveModuleSubVV4 = LiveModuleSubVV.this;
                liveModuleSubVV4.mOpenCameraFail = true;
                liveModuleSubVV4.onCameraException();
            }
        }
    }

    static /* synthetic */ void O00oO0o0() {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            mainContentProtocol.setCenterHint(8, null, null, 0);
        }
    }

    /* access modifiers changed from: private */
    public void consumeAsdSceneResult(int i) {
    }

    private String createName(long j, int i) {
        if (i > 0) {
            return this.mBaseFileName;
        }
        this.mBaseFileName = new SimpleDateFormat(getString(R.string.video_file_name_format), Locale.ENGLISH).format(new Date(j));
        return this.mBaseFileName;
    }

    private void doLaterReleaseIfNeed() {
        Camera camera = this.mActivity;
        if (camera != null && camera.isActivityPaused()) {
            this.mActivity.pauseIfNotRecording();
            this.mActivity.releaseAll(true, true);
        }
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
        Log.k(2, TAG, String.format("genContentValues: path=%s", new Object[]{str}));
        ContentValues contentValues = new ContentValues(8);
        contentValues.put("title", createName);
        contentValues.put("_display_name", sb3);
        contentValues.put("mime_type", convertOutputFormatToMimeType);
        contentValues.put("_data", str);
        StringBuilder sb7 = new StringBuilder();
        sb7.append(Integer.toString(this.mPreviewSize.width));
        sb7.append("x");
        sb7.append(Integer.toString(this.mPreviewSize.height));
        contentValues.put("resolution", sb7.toString());
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
        this.mLiveConfigChanges = (LiveConfigVV) ModeCoordinatorImpl.getInstance().getAttachProtocol(228);
        if (this.mLiveConfigChanges == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 228);
            this.mLiveConfigChanges = (LiveConfigVV) ModeCoordinatorImpl.getInstance().getAttachProtocol(228);
            this.mLiveConfigChanges.prepare();
        }
        this.mLiveConfigChanges.initResource();
        return this.mLiveConfigChanges.getAuthResult();
    }

    private void initMetaParser() {
        this.mMetaDataDisposable = Flowable.create(new FlowableOnSubscribe() {
            public void subscribe(FlowableEmitter flowableEmitter) {
                LiveModuleSubVV.this.mMetaDataFlowableEmitter = flowableEmitter;
            }
        }, BackpressureStrategy.DROP).map(new FunctionFaceDetect(this, isFrontCamera())).map(new FunctionAsdSceneDetect(this, getCameraCapabilities())).subscribe((Consumer) new LiveAsdConsumer());
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
        return isBackCamera() && this.mCameraCapabilities.isEISPreviewSupported();
    }

    private boolean isSaving() {
        VMProcessing vMProcessing = this.mVmProcessing;
        return vMProcessing != null && vMProcessing.getCurrentState() == 7;
    }

    private void onProcessingSateChanged(int i) {
        if (i == 7) {
            pausePreview();
        }
    }

    private void previewWhenSessionSuccess() {
        setCameraState(1);
        updatePreferenceInWorkThread(UpdateConstant.FUN_TYPES_ON_PREVIEW_SUCCESS);
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
                CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new C0368O000Ooo(this));
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

    private void showAuthError() {
        this.mHandler.post(new Runnable() {
            public void run() {
                Builder builder = new Builder(LiveModuleSubVV.this.mActivity);
                builder.setTitle(R.string.live_error_title);
                builder.setMessage(R.string.live_error_message);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.live_error_confirm, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LiveModuleSubVV.this.mActivity.startActivity(new Intent("android.settings.DATE_SETTINGS"));
                    }
                });
                builder.setNegativeButton(R.string.snap_cancel, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }

    @MainThread
    private void startVideoRecording() {
        keepScreenOn();
        setCameraAudioRestriction(true);
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        recordState.onPrepare();
        LiveConfigVV liveConfigVV = this.mLiveConfigChanges;
        if (liveConfigVV != null) {
            liveConfigVV.startRecordingNewFragment();
            this.mOldOriginVolumeStream = getActivity().getVolumeControlStream();
            getActivity().setVolumeControlStream(3);
        }
        recordState.onStart();
        listenPhoneState(true);
        HashMap hashMap = new HashMap();
        hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
        trackGeneralInfo(hashMap, 1, false, this.mBeautyValues, false, 0);
    }

    private void trackLiveRecordingParams() {
    }

    private void trackLiveVideoParams() {
        this.mLiveConfigChanges.trackVideoParams();
    }

    private void updateBeauty() {
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
        if (this.mAlgorithmPreviewSize != null) {
            this.mCamera2Device.startPreviewCallback(this.mLiveConfigChanges, null);
        }
    }

    private void updatePictureAndPreviewSize() {
        List supportedOutputSizeWithAssignedMode = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class);
        float previewRatio = this.mLiveConfigChanges.getPreviewRatio();
        Util.getOptimalPreviewSize(false, this.mBogusCameraId, supportedOutputSizeWithAssignedMode, (double) previewRatio);
        this.mPreviewSize = new CameraSize(3840, 2160);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("previewSize: ");
        sb.append(this.mPreviewSize.toString());
        Log.d(str, sb.toString());
        this.mPictureSize = null;
        CameraSize optimalVideoSnapshotPictureSize = Util.getOptimalVideoSnapshotPictureSize(supportedOutputSizeWithAssignedMode, (double) CameraSettings.getPreviewAspectRatio(previewRatio), Display.getWindowHeight(), Display.getWindowWidth());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("displaySize: ");
        sb2.append(optimalVideoSnapshotPictureSize.toString());
        Log.d(str2, sb2.toString());
        if (this.mAlgorithmPreviewSize != null) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("AlgorithmPreviewSize: ");
            sb3.append(this.mAlgorithmPreviewSize.toString());
            Log.d(str3, sb3.toString());
            this.mCamera2Device.setAlgorithmPreviewSize(this.mAlgorithmPreviewSize);
            this.mCamera2Device.setAlgorithmPreviewFormat(35);
            this.mCamera2Device.setPreviewMaxImages(1);
        }
        updateCameraScreenNailSize(optimalVideoSnapshotPictureSize.width, optimalVideoSnapshotPictureSize.height);
    }

    private void updateUltraWideLDC() {
        this.mCamera2Device.setUltraWideLDC(shouldApplyUltraWideLDC());
    }

    private void updateVideoStabilization() {
        if (isDeviceAlive()) {
            if (isEisOn()) {
                Log.d(TAG, "videoStabilization: EIS");
                this.mCamera2Device.setEnableEIS(true);
                this.mCamera2Device.setEnableOIS(false);
                if (!this.mCameraCapabilities.isEISPreviewSupported()) {
                    this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(true);
                }
            } else {
                Log.d(TAG, "videoStabilization: OIS");
                this.mCamera2Device.setEnableEIS(false);
                this.mCamera2Device.setEnableOIS(true);
                this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(false);
            }
        }
    }

    public /* synthetic */ void O0000Ooo(DataWrap dataWrap) {
        onProcessingSateChanged(((Integer) dataWrap.get()).intValue());
    }

    public /* synthetic */ void O00oo0OO() {
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
            this.mCamera2Device.stopPreviewCallback(true);
            this.mCamera2Device = null;
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(false);
            this.mFocusManager.destroy();
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_DESTROY);
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
        LiveConfigVV liveConfigVV = this.mLiveConfigChanges;
        if (liveConfigVV != null && !liveConfigVV.isRecording()) {
            Log.k(3, TAG, "doReverse");
            this.mLiveConfigChanges.deleteLastFragment();
        }
    }

    /* access modifiers changed from: protected */
    public boolean enableFaceDetection() {
        return DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_FACE_DETECTION, getResources().getBoolean(R.bool.pref_camera_facedetection_default));
    }

    public void fillFeatureControl(StartControl startControl) {
        StartControlFeatureDetail featureDetail = startControl.getFeatureDetail();
        featureDetail.addFragmentInfo(R.id.full_screen_feature, BaseFragmentDelegate.FRAGMENT_VV_PROCESS);
        featureDetail.hideFragment(R.id.bottom_action);
        featureDetail.hideFragment(R.id.bottom_popup_tips);
        featureDetail.hideFragment(R.id.bottom_popup_dual_camera_adjust);
    }

    @NonNull
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return 32780;
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
                LiveConfigVV liveConfigVV = this.mLiveConfigChanges;
                if (liveConfigVV != null && !liveConfigVV.isRecording()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDoingAction() {
        return isRecording() || getCameraState() == 3;
    }

    public boolean isFaceDetectStarted() {
        return this.mFaceDetectionStarted;
    }

    public boolean isNeedMute() {
        return isRecording();
    }

    public boolean isRecording() {
        LiveConfigVV liveConfigVV = this.mLiveConfigChanges;
        return liveConfigVV != null && liveConfigVV.isRecording();
    }

    public boolean isSelectingCapturedResult() {
        return false;
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
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean judgeTapableRectByUiStyle() {
        return false;
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
        LiveVVProcess liveVVProcess = (LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230);
        if (liveVVProcess == null) {
            return super.onBackPressed();
        }
        VMProcessing vMProcessing = this.mVmProcessing;
        if (!(vMProcessing == null || vMProcessing.getCurrentState() == 7)) {
            liveVVProcess.showExitConfirm();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        super.onCameraOpened();
        int initLiveConfig = initLiveConfig();
        if (initLiveConfig == 0 || initLiveConfig == 1 || !(initLiveConfig == 2 || initLiveConfig == 3 || initLiveConfig == 4)) {
            initializeFocusManager();
            updatePreferenceTrampoline(UpdateConstant.FUN_TYPES_INIT);
            startPreview();
            if (DataRepository.dataItemGlobal().isFirstShowCTAConCollect() && this.mModuleIndex == 174) {
                this.mCtaNoticeFragment = CtaNoticeFragment.showCta(getActivity().getFragmentManager(), null, 1);
            }
            this.mVmProcessing = (VMProcessing) DataRepository.dataItemObservable().get(VMProcessing.class);
            this.mVmProcessing.startObservable(this, new C0369O000Ooo0(this));
            this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_START);
            this.mOnResumeTime = SystemClock.uptimeMillis();
            this.mHandler.sendEmptyMessage(4);
            this.mHandler.sendEmptyMessage(31);
            initMetaParser();
            return;
        }
        showAuthError();
    }

    public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
    }

    public void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4) {
    }

    public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
        return null;
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        this.mHandler = new MainHandler(this.mActivity.getMainLooper());
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
        this.mHandler.post(C0370O000OooO.INSTANCE);
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
            stopVideoRecording(true);
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
                        LiveVVProcess liveVVProcess = (LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230);
                        if (liveVVProcess != null) {
                            liveVVProcess.onKeyCodeCamera();
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
                stopVideoRecording(false);
                return super.onKeyDown(i, keyEvent);
            }
        }
        boolean canFinishRecording = this.mLiveConfigChanges.canFinishRecording();
        if (!this.mIsPreviewing || !canFinishRecording) {
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

    public void onNewUriArrived(final Uri uri, final String str) {
        super.onNewUriArrived(uri, str);
        if (isAlive()) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    LiveVVProcess liveVVProcess = (LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230);
                    if (liveVVProcess != null) {
                        ContentValues saveContentValues = liveVVProcess.getSaveContentValues();
                        if (saveContentValues != null) {
                            String asString = saveContentValues.getAsString("title");
                            String asString2 = saveContentValues.getAsString("_data");
                            String access$600 = LiveModuleSubVV.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("newUri: ");
                            sb.append(str);
                            sb.append(" | ");
                            sb.append(asString);
                            Log.d(access$600, sb.toString());
                            if (asString.equals(str)) {
                                liveVVProcess.onLiveSaveToLocalFinished(uri, asString2);
                            }
                        }
                    }
                }
            });
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        setOrientation(i, i2);
        LiveConfigVV liveConfigVV = this.mLiveConfigChanges;
        if (liveConfigVV != null) {
            liveConfigVV.onOrientationChanged(i, i2, i3);
        }
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.removeMessages();
        }
        if (getActivity().startFromKeyguard()) {
            DataRepository.dataItemLive().getMimojiStatusManager().reset();
        }
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
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (this.mLiveConfigChanges.isRecording()) {
            this.mLiveConfigChanges.onRecordingNewFragmentFinished();
            if (recordState != null) {
                Log.u(TAG, "onPauseButtonClick onPause");
                recordState.onPause();
                return;
            }
            return;
        }
        trackLiveRecordingParams();
        this.mLiveConfigChanges.startRecordingNextFragment();
        if (recordState != null) {
            Log.u(TAG, "onPauseButtonClick onResume");
            recordState.onResume();
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
        this.mIsPreviewing = false;
        ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).configLiveVV(null, null, false, false);
        doLaterReleaseIfNeed();
    }

    public void onReviewDoneClicked() {
        getActivity().setVolumeControlStream(this.mOldOriginVolumeStream);
        ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onFinish();
        this.mIsPreviewing = false;
        startSaveToLocal();
        ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).configLiveVV(null, null, false, true);
        doLaterReleaseIfNeed();
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
        LiveConfigVV liveConfigVV = this.mLiveConfigChanges;
        int nextRecordStep = liveConfigVV != null ? liveConfigVV.getNextRecordStep() : 1;
        if (nextRecordStep != 1) {
            if (nextRecordStep != 2) {
                if (nextRecordStep == 3) {
                    Log.u(TAG, "onShutterButtonClick stopVideoRecording");
                    stopVideoRecording(false);
                }
            } else if (!checkCallingState()) {
                Log.d(TAG, "ignore in calling state");
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                recordState.onPrepare();
                recordState.onFailed();
            } else {
                Log.u(TAG, "onShutterButtonClick startVideoRecording");
                startVideoRecording();
                BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
                if (backStack != null) {
                    backStack.handleBackStackFromShutter();
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
        if (!this.mPaused) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (!(camera2Proxy == null || !camera2Proxy.isSessionReady() || !isInTapableRect(i, i2) || getCameraState() == 3 || getCameraState() == 0)) {
                if (!isFrameAvailable()) {
                    Log.w(TAG, "onSingleTapUp: frame not available");
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
                Log.w(TAG, "ignore volume key");
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
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
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

    public boolean shouldCaptureDirectly() {
        return false;
    }

    public boolean shouldReleaseLater() {
        return isRecording() || isSaving();
    }

    public void showPreview() {
        this.mSaved = false;
        ((LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230)).onCombinePrepare(genContentValues(2, 0, false));
        this.mIsPreviewing = true;
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
            this.mCamera2Device.setPreviewSize(this.mPreviewSize);
            this.mQuality = Util.convertSizeToQuality(this.mPreviewSize);
            this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            Log.d(TAG, "LiveModule, startPreview");
            checkDisplayOrientation();
            new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture());
            int i = this.mAlgorithmPreviewSize != null ? 1 : 0;
            LiveConfigVV liveConfigVV = this.mLiveConfigChanges;
            CameraSize cameraSize = this.mPreviewSize;
            liveConfigVV.initPreview(cameraSize.width, cameraSize.height, this.mBogusCameraId, this.mActivity.getCameraScreenNail());
            Surface surface = new Surface(this.mLiveConfigChanges.getInputSurfaceTexture());
            if (!isSelectingCapturedResult()) {
                this.mCamera2Device.startPreviewSession(surface, i, 0, null, getOperatingMode(), false, this);
            }
        }
    }

    public void startSaveToLocal() {
        if (!this.mSaved) {
            ContentValues saveContentValues = ((LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230)).getSaveContentValues();
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

    public void stopVideoRecording(boolean z) {
        Log.k(3, TAG, String.format("stopVideoRecording fromRelease=%s", new Object[]{Boolean.valueOf(z)}));
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (isAlive() && recordState != null) {
            setCameraAudioRestriction(false);
            keepScreenOnAwhile();
            recordState.onPause();
            this.mLiveConfigChanges.onRecordingNewFragmentFinished();
            if (this.mLiveConfigChanges.canFinishRecording()) {
                trackLiveVideoParams();
                showPreview();
                recordState.onFinish();
            }
            listenPhoneState(false);
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
