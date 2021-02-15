package com.android.camera.module;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.SensorEvent;
import android.hardware.camera2.CameraCaptureSession;
import android.location.Location;
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
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.FilmDreamProcessing;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.effect.EffectController;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.log.Log;
import com.android.camera.module.impl.ImplFactory;
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
import com.android.camera.protocol.ModeProtocol.FilmDreamConfig;
import com.android.camera.protocol.ModeProtocol.FilmDreamProcess;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.FilmAttr;
import com.android.camera.storage.Storage;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.xiaomi.camera.rx.CameraSchedulers;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FilmDreamModule extends BaseModule implements FocusCallback, CameraAction, Listener, LifecycleOwner {
    private static final int MSG_WAIT_SHUTTER_SOUND_FINISH = 256;
    private static final long START_RECORDING_OFFSET = 300;
    /* access modifiers changed from: private */
    public static final String TAG = "FilmDreamModule";
    private boolean m3ALocked;
    private String mBaseFileName;
    private FilmDreamProcessing mFilmDreamProcessing;
    /* access modifiers changed from: private */
    public FocusManager2 mFocusManager;
    private boolean mIsPreviewing = false;
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    private FilmDreamConfig mLiveConfigChanges;
    private int mOldOriginVolumeStream;
    /* access modifiers changed from: private */
    public long mOnResumeTime;
    private int mQuality = 5;
    private boolean mSaved;
    protected SensorStateListener mSensorStateListener = new SensorStateListener() {
        public boolean isWorking() {
            return FilmDreamModule.this.isAlive() && FilmDreamModule.this.getCameraState() != 0;
        }

        public void notifyDevicePostureChanged() {
            FilmDreamModule.this.mActivity.getEdgeShutterView().onDevicePostureChanged();
        }

        public void onDeviceBecomeStable() {
            Log.v(FilmDreamModule.TAG, "onDeviceBecomeStable");
        }

        public void onDeviceBeginMoving() {
        }

        public void onDeviceKeepMoving(double d) {
            if (!FilmDreamModule.this.mMainProtocol.isEvAdjusted(true) && !FilmDreamModule.this.mPaused && Util.isTimeout(System.currentTimeMillis(), FilmDreamModule.this.mTouchFocusStartingTime, 3000) && !FilmDreamModule.this.is3ALocked() && FilmDreamModule.this.mFocusManager != null && FilmDreamModule.this.mFocusManager.isNeedCancelAutoFocus() && !FilmDreamModule.this.isRecording()) {
                FilmDreamModule.this.mFocusManager.onDeviceKeepMoving(d);
            }
        }

        public void onDeviceKeepStable() {
        }

        public void onDeviceLieChanged(boolean z) {
        }

        public void onDeviceOrientationChanged(float f, boolean z) {
            String access$300 = FilmDreamModule.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onDeviceOrientationChanged: ");
            sb.append(f);
            Log.d(access$300, sb.toString());
            FilmDreamModule filmDreamModule = FilmDreamModule.this;
            if (z) {
                f = (float) filmDreamModule.mOrientation;
            }
            filmDreamModule.mDeviceRotation = f;
            if (FilmDreamModule.this.isGradienterOn) {
                EffectController instance = EffectController.getInstance();
                FilmDreamModule filmDreamModule2 = FilmDreamModule.this;
                instance.setDeviceRotation(z, Util.getShootRotation(filmDreamModule2.mActivity, filmDreamModule2.mDeviceRotation));
            }
        }

        public void onDeviceRotationChanged(float[] fArr) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
        }
    };
    /* access modifiers changed from: private */
    public long mTouchFocusStartingTime;

    class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 2) {
                FilmDreamModule.this.getWindow().clearFlags(128);
            } else if (i == 4) {
                FilmDreamModule.this.checkActivityOrientation();
                if (SystemClock.uptimeMillis() - FilmDreamModule.this.mOnResumeTime < 5000) {
                    FilmDreamModule.this.mHandler.sendEmptyMessageDelayed(4, 100);
                }
            } else if (i == 9) {
                FilmDreamModule filmDreamModule = FilmDreamModule.this;
                filmDreamModule.mMainProtocol.initializeFocusView(filmDreamModule);
            } else if (i == 17) {
                FilmDreamModule.this.mHandler.removeMessages(17);
                FilmDreamModule.this.mHandler.removeMessages(2);
                FilmDreamModule.this.getWindow().addFlags(128);
                FilmDreamModule filmDreamModule2 = FilmDreamModule.this;
                filmDreamModule2.mHandler.sendEmptyMessageDelayed(2, (long) filmDreamModule2.getScreenDelay());
            } else if (i == 31) {
                FilmDreamModule.this.setOrientationParameter();
            } else if (i != 51) {
                if (i == 256) {
                    FilmDreamModule.this.startVideoRecording();
                }
            } else if (!FilmDreamModule.this.mActivity.isActivityPaused()) {
                FilmDreamModule filmDreamModule3 = FilmDreamModule.this;
                filmDreamModule3.mOpenCameraFail = true;
                filmDreamModule3.onCameraException();
            }
        }
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
        FilmDreamProcessing filmDreamProcessing = this.mFilmDreamProcessing;
        return filmDreamProcessing != null && filmDreamProcessing.getCurrentState() == 6;
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
                CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new C0365O000OoO0(this));
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

    private void showPreview() {
        this.mSaved = false;
        ((FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931)).onCombinePrepare(genContentValues(2, 0, false));
    }

    /* access modifiers changed from: private */
    @MainThread
    public void startVideoRecording() {
        if (!isDeparted() && !this.mLiveConfigChanges.isRecording()) {
            Log.e(TAG, "startVideoRecording");
            keepScreenOn();
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            recordState.onPrepare();
            this.mOldOriginVolumeStream = getActivity().getVolumeControlStream();
            getActivity().setVolumeControlStream(3);
            FilmDreamConfig filmDreamConfig = this.mLiveConfigChanges;
            if (filmDreamConfig != null) {
                filmDreamConfig.onOrientationChanged(0, this.mOrientationCompensation, this.mCameraDisplayOrientation);
                this.mLiveConfigChanges.startRecording();
            }
            recordState.onStart();
            listenPhoneState(true);
        }
    }

    private void updateBeauty() {
    }

    private void updateDeviceOrientation() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
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

    public /* synthetic */ void O0000OoO(DataWrap dataWrap) {
        onProcessingSateChanged(((Integer) dataWrap.get()).intValue());
    }

    public /* synthetic */ void O00oo0() {
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
                continue;
            } else if (i2 == 3) {
                updateFocusArea();
            } else if (!(i2 == 5 || i2 == 50)) {
                if (i2 == 58) {
                    updateBackSoftLightPreference();
                } else if (i2 == 66) {
                    continue;
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
    }

    public void fillFeatureControl(StartControl startControl) {
        StartControlFeatureDetail featureDetail = startControl.getFeatureDetail();
        featureDetail.addFragmentInfo(R.id.full_screen_feature, BaseFragmentDelegate.FRAGMENT_FILM_DREAM_PROCESS);
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

    /* access modifiers changed from: protected */
    public boolean is3ALocked() {
        return this.m3ALocked;
    }

    public boolean isDoingAction() {
        return isRecording() || getCameraState() == 3;
    }

    public boolean isNeedMute() {
        return isRecording();
    }

    public boolean isRecording() {
        Handler handler = this.mHandler;
        boolean z = true;
        if (handler != null && handler.hasMessages(256)) {
            return true;
        }
        FilmDreamConfig filmDreamConfig = this.mLiveConfigChanges;
        if (filmDreamConfig == null || !filmDreamConfig.isRecording()) {
            z = false;
        }
        return z;
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

    public boolean isZoomEnabled() {
        return !isRecording();
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
        if (isRecording()) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
                this.mLastBackPressedTime = currentTimeMillis;
                ToastUtils.showToast((Context) this.mActivity, (int) R.string.record_back_pressed_hint, true);
            } else {
                stopVideoRecording(true, false);
            }
            return true;
        }
        FilmDreamProcess filmDreamProcess = (FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931);
        if (filmDreamProcess == null) {
            return super.onBackPressed();
        }
        FilmDreamProcessing filmDreamProcessing = this.mFilmDreamProcessing;
        if (!(filmDreamProcessing == null || filmDreamProcessing.getCurrentState() == 6)) {
            filmDreamProcess.showExitConfirm();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        super.onCameraOpened();
        this.mLiveConfigChanges = (FilmDreamConfig) ModeCoordinatorImpl.getInstance().getAttachProtocol(930);
        if (this.mLiveConfigChanges == null) {
            getActivity().getImplFactory().initModulePersistent(getActivity(), 930);
            this.mLiveConfigChanges = (FilmDreamConfig) ModeCoordinatorImpl.getInstance().getAttachProtocol(930);
            this.mLiveConfigChanges.prepare();
        }
        this.mLiveConfigChanges.initResource();
        initializeFocusManager();
        updatePreferenceTrampoline(UpdateConstant.FUN_TYPES_INIT);
        startPreview();
        this.mFilmDreamProcessing = (FilmDreamProcessing) DataRepository.dataItemObservable().get(FilmDreamProcessing.class);
        this.mFilmDreamProcessing.startObservable(this, new O00O0Oo(this));
        this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_START);
        this.mOnResumeTime = SystemClock.uptimeMillis();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
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
        this.mActivity.getSensorStateManager().setRotationVectorEnabled(false);
        this.mActivity.getSensorStateManager().setTTARSensorEnabled(false);
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
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(256)) {
            this.mHandler.removeMessages(256);
            Log.d(TAG, "skip stopVideoRecording & remove startVideoRecording");
        }
        if (isRecording()) {
            stopVideoRecording(true, false);
        } else if (isSaving()) {
            return;
        }
        doLaterReleaseIfNeed();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.mPaused) {
            return true;
        }
        boolean z = false;
        if (!isFrameAvailable()) {
            return false;
        }
        FilmDreamProcessing filmDreamProcessing = this.mFilmDreamProcessing;
        int currentState = filmDreamProcessing != null ? filmDreamProcessing.getCurrentState() : 0;
        if (currentState != 0 && currentState != 1) {
            return false;
        }
        if (!(i == 24 || i == 25)) {
            if (i == 27 || i == 66) {
                if (keyEvent.getRepeatCount() == 0) {
                    FilmDreamProcess filmDreamProcess = (FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931);
                    if (filmDreamProcess != null) {
                        filmDreamProcess.onKeyCodeCamera();
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
                stopVideoRecording(true, false);
                return super.onKeyDown(i, keyEvent);
            }
        }
        if (i == 24 || i == 88) {
            z = true;
        }
        if (handleVolumeKeyEvent(z, true, keyEvent.getRepeatCount(), keyEvent.getDevice().isExternal())) {
            return true;
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
                    FilmDreamProcess filmDreamProcess = (FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931);
                    if (filmDreamProcess != null) {
                        ContentValues saveContentValues = filmDreamProcess.getSaveContentValues();
                        if (saveContentValues != null) {
                            String asString = saveContentValues.getAsString("title");
                            String asString2 = saveContentValues.getAsString("_data");
                            String access$300 = FilmDreamModule.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("newUri: ");
                            sb.append(str);
                            sb.append(" | ");
                            sb.append(asString);
                            Log.d(access$300, sb.toString());
                            if (asString.equals(str)) {
                                filmDreamProcess.onLiveSaveToLocalFinished(uri, asString2);
                            }
                        }
                    }
                }
            });
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        setOrientation(i, i2);
        FilmDreamConfig filmDreamConfig = this.mLiveConfigChanges;
        if (filmDreamConfig != null) {
            filmDreamConfig.onOrientationChanged(i, i2, this.mCameraDisplayOrientation);
        }
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.removeMessages();
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

    public void onPreviewLayoutChanged(Rect rect) {
        this.mActivity.onLayoutChange(rect);
        if (this.mFocusManager != null && this.mActivity.getCameraScreenNail() != null) {
            this.mFocusManager.setRenderSize(this.mActivity.getCameraScreenNail().getRenderWidth(), this.mActivity.getCameraScreenNail().getRenderHeight());
            this.mFocusManager.setPreviewSize(rect.width(), rect.height());
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
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.setAlertAnim(false);
        }
        recordState.onFinish();
        ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).configFilm(null, false, false);
        doLaterReleaseIfNeed();
    }

    public void onReviewDoneClicked() {
        getActivity().setVolumeControlStream(this.mOldOriginVolumeStream);
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.setAlertAnim(false);
        }
        recordState.onFinish();
        startSaveToLocal();
        ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).configFilm(null, false, true);
        doLaterReleaseIfNeed();
    }

    public void onShutterButtonClick(int i) {
        Log.u(TAG, "onShutterButtonClick ");
        if (!isRecording()) {
            FilmDreamConfig filmDreamConfig = this.mLiveConfigChanges;
            if (filmDreamConfig != null && !filmDreamConfig.isInited()) {
                return;
            }
            if (!checkCallingState()) {
                Log.d(TAG, "ignore in calling state");
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                recordState.onPrepare();
                recordState.onFailed();
                return;
            }
            CameraStatUtils.trackFilmDreamClick(FilmAttr.VALUE_FILM_DREAM_CLICK_START_RECORD);
            Handler handler = this.mHandler;
            if (handler == null || handler.hasMessages(256) || !CameraSettings.isCameraSoundOpen()) {
                Log.u(TAG, "onShutterButtonClick startVideoRecording");
                startVideoRecording();
            } else {
                playCameraSound(2);
                this.mHandler.sendEmptyMessageDelayed(256, 300);
            }
            setTriggerMode(i);
            BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
            if (backStack != null) {
                backStack.handleBackStackFromShutter();
            }
        } else if (this.mLiveConfigChanges.isRecording()) {
            Log.u(TAG, "onShutterButtonClick stopVideoRecording");
            CameraStatUtils.trackFilmDreamClick(FilmAttr.VALUE_FILM_DREAM_CLICK_STOP_RECORD);
            stopVideoRecording(true, true);
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
                } else if ((!isFrontCamera() || !this.mActivity.isScreenSlideOff()) && !((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromTapDown(i, i2) && !isRecording()) {
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
    }

    public boolean onWaitingFocusFinished() {
        return !isBlockSnap() && isAlive();
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
        if (!this.mMediaRecorderRecording) {
            playCameraSound(i);
        }
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
            Log.d(TAG, " startPreview");
            checkDisplayOrientation();
            new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture());
            int i = this.mAlgorithmPreviewSize != null ? 1 : 0;
            FilmDreamConfig filmDreamConfig = this.mLiveConfigChanges;
            CameraSize cameraSize = this.mPreviewSize;
            filmDreamConfig.initPreview(cameraSize.width, cameraSize.height, this.mBogusCameraId, this.mActivity.getCameraScreenNail());
            Surface surface = new Surface(this.mLiveConfigChanges.getInputSurfaceTexture());
            if (!isSelectingCapturedResult()) {
                this.mCamera2Device.startPreviewSession(surface, i, 0, null, getOperatingMode(), false, this);
            }
        }
    }

    public void startSaveToLocal() {
        if (!this.mSaved) {
            ContentValues saveContentValues = ((FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931)).getSaveContentValues();
            if (saveContentValues != null) {
                this.mSaved = true;
                saveContentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
                getActivity().getImageSaver().addVideo(saveContentValues.getAsString("_data"), saveContentValues, true);
            }
        }
    }

    public void stopObjectTracking(boolean z) {
    }

    public void stopVideoRecording(boolean z, boolean z2) {
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(256)) {
            this.mHandler.removeMessages(256);
            Log.d(TAG, "skip stopVideoRecording & remove startVideoRecording");
        } else if (!z2 || this.mLiveConfigChanges.canRecordingStop()) {
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (isAlive() && recordState != null) {
                Log.e(TAG, "stopVideoRecording");
                keepScreenOnAwhile();
                recordState.onPause();
                if (z) {
                    this.mLiveConfigChanges.stopRecording();
                }
                showPreview();
                if (this.mLiveConfigChanges.canFinishRecording()) {
                    recordState.onFinish();
                    if (!this.mPaused) {
                        playCameraSound(3);
                    }
                }
                listenPhoneState(false);
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
