package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraErrorCallbackImpl;
import com.android.camera.CameraPreferenceActivity;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.FileCompat;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.LocalParallelService.LocalBinder;
import com.android.camera.MutexModeManager;
import com.android.camera.MutexModeManager.MutexCallBack;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.SettingUiState;
import com.android.camera.ThermalDetector;
import com.android.camera.Util;
import com.android.camera.constant.AutoFocus;
import com.android.camera.constant.UpdateConstant.UpdateType;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.fragment.dialog.AutoHibernationFragment;
import com.android.camera.fragment.settings.CameraPreferenceFragment;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.log.Log;
import com.android.camera.module.loader.ActionHideLensDirtyDetectHint;
import com.android.camera.module.loader.ActionUpdateLensDirtyDetect;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.Camera2OpenManager;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera.preferences.SettingsOverrider;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraModuleSpecial;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.EvChangedProtocol;
import com.android.camera.protocol.ModeProtocol.LensProtocol;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import com.android.camera.protocol.ModeProtocol.ModeListManager;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.ZoomProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.AlgoAttr;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.BeautyAttr;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsWrapper.PictureTakenParameter;
import com.android.camera.storage.ImageSaver;
import com.android.camera.storage.Storage;
import com.android.camera.ui.FocusView.ExposureViewListener;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraMetaDataCallback;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CaptureResultParser;
import com.android.gallery3d.ui.GLCanvas;
import com.xiaomi.camera.device.CameraHandlerThread.Cookie;
import com.xiaomi.camera.device.CameraHandlerThread.CookieStore;
import com.xiaomi.camera.device.exception.CameraNotOpenException;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.camera.util.ThreadUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.view.animation.CubicEaseOutInterpolator;

public abstract class BaseModule implements Module, ExposureViewListener, CameraMetaDataCallback, CameraPreviewCallback, EvChangedProtocol, ZoomProtocol, CameraModuleSpecial, MutexCallBack, LensProtocol, ModeListManager, Consumer {
    protected static final int BACK_PRESSED_TIME_INTERVAL = 3000;
    public static final int[] CAMERA_MODES = {0, 2, 4, 6};
    public static final int CAMERA_MODE_IMAGE_CAPTURE = 2;
    public static final int CAMERA_MODE_NORMAL = 0;
    public static final int CAMERA_MODE_SCAN_QR_CODE = 6;
    public static final int CAMERA_MODE_VIDEO_CAPTURE = 4;
    protected static final boolean DEBUG = Util.isDebugOsBuild();
    public static final int DOCUMENT_BLUR_DETECT_HINT_DURATION_3S = 3000;
    public static final int LENS_DIRTY_DETECT_HINT_DURATION_3S = 3000;
    public static final int LENS_DIRTY_DETECT_HINT_DURATION_8S = 8000;
    protected static final int LENS_DIRTY_DETECT_TIMEOUT = 15000;
    protected static final int SCREEN_DELAY = 60000;
    protected static final int SCREEN_DELAY_KEYGUARD = 30000;
    public static final int SHUTTER_DOWN_FROM_BUTTON = 2;
    public static final int SHUTTER_DOWN_FROM_HARD_KEY = 1;
    public static final int SHUTTER_DOWN_FROM_UNKNOWN = 0;
    public static final int SHUTTER_DOWN_FROM_UNLOCK_OR_LONG_CLICK_OR_AUDIO = 3;
    public static final String START_VIDEO_RECORDING_ACTION = "com.android.camera.action.start_video_recording";
    public static final String STOP_VIDEO_RECORDING_ACTION = "com.android.camera.action.stop_video_recording";
    private static final String TAG = "BaseModule";
    /* access modifiers changed from: protected */
    public boolean isGradienterOn;
    private boolean isShowPreviewDebugInfo;
    protected boolean isZooming;
    protected boolean m3ALockSupported;
    protected boolean mAELockOnlySupported;
    /* access modifiers changed from: protected */
    public Camera mActivity;
    protected int mActualCameraId;
    protected boolean mAeLockSupported;
    protected CameraSize mAlgorithmPreviewSize;
    private AudioController mAudioController;
    protected boolean mAutoHibernationMode;
    protected boolean mAwbLockSupported;
    protected CameraSize mBinningPictureSize;
    protected int mBogusCameraId;
    protected CameraSize mBokehDepthSize;
    protected Camera2Proxy mCamera2Device;
    protected CameraCapabilities mCameraCapabilities;
    protected boolean mCameraDisabled;
    protected int mCameraDisplayOrientation;
    protected boolean mCameraHardwareError;
    private int mCameraState = 0;
    protected boolean mContinuousFocusSupported;
    private boolean mDetectLensDirty;
    protected Object mDeviceLock = new Object();
    /* access modifiers changed from: protected */
    public float mDeviceRotation = -1.0f;
    private AlertDialog mDialog;
    protected Rect mDisplayRect;
    protected int mDisplayRotation = -1;
    protected int mEnterAutoHibernationCount = 0;
    protected CameraErrorCallbackImpl mErrorCallback;
    protected int mEvState;
    protected int mEvValue;
    private int mEvValueForTrack;
    protected CameraSize mFakeSatOutSize;
    protected CameraCapabilities mFakeTeleCameraCapabilities;
    protected CameraSize mFakeTelePictureSize;
    protected volatile boolean mFallbackProcessed;
    protected String mFlashAutoModeState;
    protected boolean mFocusAreaSupported;
    protected boolean mFocusOrAELockSupported;
    /* access modifiers changed from: protected */
    public Handler mHandler;
    protected boolean mIgnoreFocusChanged;
    private volatile boolean mIgnoreTouchEvent;
    protected boolean mInStartingFocusRecording;
    protected boolean mIsAutoHibernationSupported;
    private AtomicBoolean mIsCreated = new AtomicBoolean(false);
    private AtomicBoolean mIsDeparted = new AtomicBoolean(false);
    private AtomicBoolean mIsFrameAvailable = new AtomicBoolean(false);
    protected volatile int mIsSatFallback = 0;
    protected long mLastBackPressedTime;
    protected volatile int mLastSatFallbackRequestId = -1;
    private float mLastZoomRatio = 1.0f;
    private Disposable mLensDirtyDetectDisposable;
    private boolean mLensDirtyDetectEnable;
    private Disposable mLensDirtyDetectHintDisposable;
    protected CameraCapabilities mMacroCameraCapabilities;
    protected CameraSize mMacroPictureSize;
    /* access modifiers changed from: protected */
    public MainContentProtocol mMainProtocol;
    protected int mMaxFaceCount;
    private float mMaxZoomRatio = 1.0f;
    protected volatile boolean mMediaRecorderRecording;
    private int mMessageId;
    protected boolean mMeteringAreaSupported;
    private float mMinZoomRatio = 1.0f;
    protected int mModuleIndex;
    protected MutexModeManager mMutexModePicker;
    protected boolean mObjectTrackingStarted;
    /* access modifiers changed from: protected */
    public boolean mOpenCameraFail;
    /* access modifiers changed from: protected */
    public int mOrientation = 0;
    protected int mOrientationCompensation = 0;
    protected int mOutputPictureFormat;
    protected CameraSize mOutputPictureSize;
    /* access modifiers changed from: protected */
    public volatile boolean mPaused;
    protected int mPendingScreenSlideKeyCode;
    protected final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int i, String str) {
            if (i == 2 && BaseModule.this.isRecording()) {
                Log.w(BaseModule.TAG, "CALL_STATE_OFFHOOK");
                BaseModule.this.onHostStopAndNotifyActionStop();
            }
            super.onCallStateChanged(i, str);
        }
    };
    protected CameraSize mPictureSize;
    protected CameraSize mPreviewSize;
    protected CameraSize mRawSizeOfMacro;
    protected CameraSize mRawSizeOfTele;
    protected CameraSize mRawSizeOfUltraTele;
    protected CameraSize mRawSizeOfUltraWide;
    protected CameraSize mRawSizeOfWide;
    private boolean mRestoring;
    protected CameraSize mSATRemosicPictureSize;
    protected CameraSize mSensorRawImageSize;
    protected SettingsOverrider mSettingsOverrider = new SettingsOverrider();
    protected CameraCapabilities mStandaloneMacroCameraCapabilities;
    protected CameraSize mSubPictureSize;
    protected long mSurfaceCreatedTimestamp;
    protected CameraCapabilities mTeleCameraCapabilities;
    protected CameraSize mTelePictureSize;
    protected TelephonyManager mTelephonyManager;
    private int mThermalLevel = 0;
    private int mTitleId;
    protected TopAlert mTopAlert;
    protected int mTriggerMode = 10;
    protected int mUIStyle = -1;
    protected CameraCapabilities mUltraCameraCapabilities;
    protected CameraCapabilities mUltraTeleCameraCapabilities;
    protected CameraSize mUltraTelePictureSize;
    protected CameraSize mUltraWidePictureSize;
    protected String mUnInterruptableReason;
    private Disposable mUpdateWorkThreadDisposable;
    /* access modifiers changed from: private */
    public ObservableEmitter mUpdateWorkThreadEmitter;
    protected boolean mUseLegacyFlashMode;
    private ValueAnimator mValueAnimator;
    protected volatile boolean mWaitingSnapshot;
    protected CameraCapabilities mWideCameraCapabilities;
    protected CameraSize mWidePictureSize;
    protected float mZoomRatio = 1.0f;
    private float mZoomScaled;
    protected boolean mZoomSupported;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraMode {
    }

    private void detectLensDirty(CaptureResult captureResult) {
        if (this.mDetectLensDirty && CaptureResultParser.isLensDirtyDetected(captureResult)) {
            showLensDirtyTip();
        }
    }

    protected static String getColorEffectKey() {
        return C0124O00000oO.Oo00oo() ? "pref_camera_shader_coloreffect_key" : CameraSettings.KEY_COLOR_EFFECT;
    }

    public static int getPreferencesLocalId(int i, int i2) {
        return i + i2;
    }

    private boolean isTeleSupportVideoQuality() {
        int i = this.mModuleIndex;
        if (i != 161 && i != 162 && i != 168 && i != 169 && i != 172 && i != 174 && i != 179 && i != 180 && i != 183 && i != 185 && i != 213 && i != 204 && i != 214) {
            return false;
        }
        int ultraTeleCameraId = C0122O00000o.instance().OOo000() ? Camera2DataContainer.getInstance().getUltraTeleCameraId() : Camera2DataContainer.getInstance().getAuxCameraId();
        return this.mModuleIndex == 183 ? DataRepository.dataItemLive().getComponentLiveVideoQuality().isSupportVideoQuality(this.mModuleIndex, ultraTeleCameraId) : DataRepository.dataItemConfig().getComponentConfigVideoQuality().isSupportVideoQuality(this.mModuleIndex, ultraTeleCameraId);
    }

    private boolean limitZoomByAIWatermark(float f) {
        return DataRepository.dataItemRunning().getComponentRunningAIWatermark().getAIWatermarkEnable() && f < 1.0f;
    }

    private void setCameraDevice(Camera2Proxy camera2Proxy) {
        this.mCamera2Device = camera2Proxy;
        this.mCameraCapabilities = camera2Proxy.getCapabilities();
        this.mZoomSupported = this.mCameraCapabilities.isZoomSupported();
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT && !C0122O00000o.instance().OOOOoo()) {
            this.mUltraCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getUltraWideCameraId());
            CameraCapabilities cameraCapabilities = this.mUltraCameraCapabilities;
            if (cameraCapabilities != null) {
                cameraCapabilities.setOperatingMode(getOperatingMode());
            }
        }
        if (C0122O00000o.instance().OOoOO0o()) {
            this.mStandaloneMacroCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getStandaloneMacroCameraId());
            CameraCapabilities cameraCapabilities2 = this.mStandaloneMacroCameraCapabilities;
            if (cameraCapabilities2 != null) {
                cameraCapabilities2.setOperatingMode(getOperatingMode());
            }
        }
    }

    private void setCreated(boolean z) {
        this.mIsCreated.set(z);
    }

    private void setIgnoreTouchEvent(boolean z) {
        this.mIgnoreTouchEvent = z;
    }

    private void showAutoHibernationFragment() {
        if (this.mActivity != null) {
            AutoHibernationFragment autoHibernationFragment = new AutoHibernationFragment();
            autoHibernationFragment.registerProtocol();
            autoHibernationFragment.setModeIndex(this.mModuleIndex);
            autoHibernationFragment.initOrientation(this.mOrientation);
            autoHibernationFragment.setStyle(2, R.style.TTMusicDialogFragment);
            this.mActivity.getSupportFragmentManager().beginTransaction().add((Fragment) autoHibernationFragment, AutoHibernationFragment.TAG).commitAllowingStateLoss();
        }
    }

    private void showDebug(final CaptureResult captureResult, final boolean z) {
        if (Util.isShowAfRegionView() && z) {
            this.mMainProtocol.setAfRegionView((MeteringRectangle[]) captureResult.get(CaptureResult.CONTROL_AF_REGIONS), getActiveArraySize(), getDeviceBasedZoomRatio());
        }
        if (Util.isShowDebugInfoView()) {
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (z) {
                        BaseModule baseModule = BaseModule.this;
                        baseModule.mActivity.showDebugInfo(Util.getDebugInformation(captureResult, baseModule.getDebugInfo()));
                        return;
                    }
                    BaseModule.this.mActivity.showDebugInfo("");
                }
            });
        }
    }

    private void updateUltraWideCapability(float f) {
        if (this.mUltraCameraCapabilities == null) {
            this.mUltraCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getUltraWideCameraId());
            CameraCapabilities cameraCapabilities = this.mUltraCameraCapabilities;
            if (cameraCapabilities != null) {
                cameraCapabilities.setOperatingMode(getOperatingMode());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("updateUltraWideCapability: currZoomRatio = ");
        sb.append(f);
        Log.i(TAG, sb.toString());
        onCapabilityChanged(f < 1.0f ? this.mUltraCameraCapabilities : this.mCameraCapabilities);
    }

    public /* synthetic */ void O00000o(ValueAnimator valueAnimator) {
        setZoomRatio(((Float) valueAnimator.getAnimatedValue()).floatValue());
        updatePreferenceInWorkThread(24);
    }

    public void accept(@UpdateType int[] iArr) {
        StringBuilder sb;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("accept ");
        sb2.append(join(iArr));
        sb2.append(". ");
        sb2.append(this);
        String sb3 = sb2.toString();
        String str = TAG;
        Log.e(str, sb3);
        if (this.mUpdateWorkThreadDisposable.isDisposed()) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("mUpdateWorkThreadDisposable isDisposed. ");
            sb4.append(this);
            sb4.append(" ");
            sb4.append(this.mUpdateWorkThreadDisposable);
            Log.e(str, sb4.toString());
        } else if (isDeviceAlive()) {
            Log.e(str, "begin to consumePreference..");
            consumePreference(iArr);
            if (!isAlive() || this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp() != this.mSurfaceCreatedTimestamp || isRepeatingRequestInProgress()) {
                sb = new StringBuilder();
                sb.append("skip resumePreview on accept. isAlive = ");
                sb.append(isAlive());
                sb.append(" isRequestInProgress = ");
                sb.append(isRepeatingRequestInProgress());
            } else {
                int resumePreview = this.mCamera2Device.resumePreview();
                if (shouldCheckSatFallbackState()) {
                    int length = iArr.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        } else if (24 != iArr[i]) {
                            i++;
                        } else if (Math.abs(this.mLastZoomRatio - this.mCamera2Device.getZoomRatio()) > 0.001f && maySwitchCameraLens(this.mLastZoomRatio, this.mCamera2Device.getZoomRatio())) {
                            this.mLastZoomRatio = this.mCamera2Device.getZoomRatio();
                            this.mIsSatFallback = 1;
                            this.mFallbackProcessed = false;
                            this.mLastSatFallbackRequestId = resumePreview;
                            this.mHandler.removeMessages(60);
                            this.mHandler.sendEmptyMessageDelayed(60, 2500);
                            sb = new StringBuilder();
                            sb.append("lastFallbackRequestId = ");
                            sb.append(this.mLastSatFallbackRequestId);
                        }
                    }
                }
            }
            Log.d(str, sb.toString());
        }
    }

    public void addSaveTask(String str, ContentValues contentValues) {
    }

    /* access modifiers changed from: protected */
    public boolean addZoom(float f) {
        return onZoomingActionUpdate(HybridZoomingSystem.add(this.mZoomRatio, f), 1);
    }

    public void announceForAccessibility(@StringRes int i) {
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        if (actionProcessing != null) {
            actionProcessing.announceForAccessibility(i);
        }
    }

    /* access modifiers changed from: protected */
    public void applyZoomRatio() {
        if (this.mCamera2Device != null) {
            float deviceBasedZoomRatio = getDeviceBasedZoomRatio();
            StringBuilder sb = new StringBuilder();
            sb.append("applyZoomRatio(): apply zoom ratio to device = ");
            sb.append(deviceBasedZoomRatio);
            Log.d(TAG, sb.toString());
            this.mCamera2Device.setZoomRatio(deviceBasedZoomRatio);
        }
    }

    public boolean canIgnoreFocusChanged() {
        return this.mIgnoreFocusChanged;
    }

    public void changeZoom4Crop() {
        float[] fArr;
        if (ModuleManager.isSupportCropFrontMode() && isFrontCamera() && C0122O00000o.instance().OO0oo0O()) {
            ValueAnimator valueAnimator = this.mValueAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mValueAnimator.cancel();
            }
            int i = this.mOrientation;
            if (i == 0 || i == 180) {
                fArr = new float[]{getZoomRatio(), C0122O00000o.instance().O0ooO0O()};
            } else if (i == 90 || i == 270) {
                fArr = new float[]{getZoomRatio(), 1.0f};
            } else {
                return;
            }
            this.mValueAnimator = ValueAnimator.ofFloat(fArr);
            this.mValueAnimator.setDuration(300);
            this.mValueAnimator.setInterpolator(new CubicEaseOutInterpolator());
            this.mValueAnimator.addUpdateListener(new O00000o(this));
            this.mValueAnimator.start();
        }
    }

    public void checkActivityOrientation() {
        if (isDeviceAlive() && this.mDisplayRotation != Util.getDisplayRotation(this.mActivity)) {
            checkDisplayOrientation();
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkCallingState() {
        if (Storage.isLowStorageAtLastPoint()) {
            this.mActivity.getScreenHint().updateHint();
            return false;
        }
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null || 2 != telephonyManager.getCallState()) {
            return true;
        }
        showConfirmMessage(R.string.confirm_recording_fail_title, R.string.confirm_recording_fail_calling_alert);
        return false;
    }

    public void checkDisplayOrientation() {
        Camera camera = this.mActivity;
        String str = TAG;
        if (camera == null) {
            Log.w(str, "checkDisplayOrientation: activity == null");
            return;
        }
        this.mDisplayRotation = Util.getDisplayRotation(camera);
        this.mCameraDisplayOrientation = Util.getDisplayOrientation(this.mDisplayRotation, this.mBogusCameraId);
        StringBuilder sb = new StringBuilder();
        sb.append("checkDisplayOrientation: ");
        sb.append(this.mDisplayRotation);
        sb.append(" | ");
        sb.append(this.mCameraDisplayOrientation);
        Log.v(str, sb.toString());
        if (camera.getCameraScreenNail() != null) {
            camera.getCameraScreenNail().setDisplayOrientation(this.mDisplayRotation);
        }
    }

    /* access modifiers changed from: protected */
    public final void checkSatFallback(CaptureResult captureResult) {
        boolean isSatFallbackDetected = CaptureResultParser.isSatFallbackDetected(captureResult);
        int i = this.mIsSatFallback;
        String str = TAG;
        if (i != 2 && isSatFallbackDetected && !this.mWaitingSnapshot) {
            int sendSatFallbackRequest = this.mCamera2Device.sendSatFallbackRequest(this.mModuleIndex);
            StringBuilder sb = new StringBuilder();
            sb.append("checkSatFallback: lastFallbackRequestId = ");
            sb.append(sendSatFallbackRequest);
            sb.append(",fallbackDetected = ");
            sb.append(isSatFallbackDetected);
            Log.d(str, sb.toString());
            if (sendSatFallbackRequest >= 0) {
                this.mIsSatFallback = 2;
                this.mFallbackProcessed = false;
                this.mLastSatFallbackRequestId = sendSatFallbackRequest;
                this.mHandler.removeMessages(60);
                this.mHandler.sendEmptyMessageDelayed(60, 2500);
            }
        } else if (this.mLastSatFallbackRequestId >= 0) {
            if (this.mLastSatFallbackRequestId <= captureResult.getSequenceId()) {
                this.mFallbackProcessed = true;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("checkSatFallback: fallbackDetected = ");
            sb2.append(isSatFallbackDetected);
            sb2.append(" mFallbackProcessed = ");
            sb2.append(this.mFallbackProcessed);
            sb2.append(" requestId = ");
            sb2.append(captureResult.getSequenceId());
            sb2.append("|");
            sb2.append(captureResult.getFrameNumber());
            Log.d(str, sb2.toString());
            if (this.mFallbackProcessed && !isSatFallbackDetected) {
                this.mIsSatFallback = 0;
                this.mFallbackProcessed = false;
                this.mLastSatFallbackRequestId = -1;
                this.mHandler.removeMessages(60);
                if (this.mWaitingSnapshot && getCameraState() == 1) {
                    this.mWaitingSnapshot = false;
                    this.mHandler.sendEmptyMessage(62);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void consumePreference(@UpdateType int... iArr) {
    }

    public boolean dispatchConfigChange(int i) {
        return false;
    }

    public void enableCameraControls(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("enableCameraControls: enable = ");
        sb.append(z);
        sb.append(", caller: ");
        sb.append(Util.getCallers(1));
        Log.d(TAG, sb.toString());
        setIgnoreTouchEvent(!z);
    }

    /* access modifiers changed from: protected */
    public void enterAutoHibernation() {
        this.mEnterAutoHibernationCount++;
        showAutoHibernationFragment();
        updateAutoHibernationFirstRecordingTime();
        this.mHandler.post(new Runnable() {
            public void run() {
                if (BaseModule.this.mActivity != null) {
                    Log.d(BaseModule.TAG, "enterAutoHibernation");
                    BaseModule.this.mActivity.setWindowBrightness(100);
                    BaseModule.this.mAutoHibernationMode = true;
                }
            }
        });
    }

    public void enterMutexMode(int i) {
    }

    public void executeSaveTask(boolean z) {
    }

    public void exitAutoHibernation() {
        this.mHandler.removeMessages(65);
        this.mHandler.removeMessages(66);
        if (this.mAutoHibernationMode) {
            this.mAutoHibernationMode = false;
            Log.d(TAG, "exitAutoHibernation");
            this.mHandler.post(new Runnable() {
                public void run() {
                    Camera camera = BaseModule.this.mActivity;
                    if (camera != null) {
                        camera.restoreWindowBrightness();
                    }
                }
            });
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.alertAutoHibernationDescTip(FragmentTopAlert.TIP_AUTO_HIBERNATION_DESC, 8, R.string.auto_hibernation_enter_tip, 5000);
                }
            }
        });
    }

    public void exitMutexMode(int i) {
    }

    public void fillFeatureControl(StartControl startControl) {
    }

    public View findViewById(int i) {
        return this.mActivity.findViewById(i);
    }

    /* access modifiers changed from: protected */
    public void focusCenter() {
    }

    /* access modifiers changed from: protected */
    public Rect getActiveArraySize() {
        return this.mCameraCapabilities.getActiveArraySize();
    }

    public Camera getActivity() {
        return this.mActivity;
    }

    public int getActualCameraId() {
        return this.mActualCameraId;
    }

    public int getBogusCameraId() {
        return this.mBogusCameraId;
    }

    public CameraCapabilities getCameraCapabilities() {
        return this.mCameraCapabilities;
    }

    public Camera2Proxy getCameraDevice() {
        return this.mCamera2Device;
    }

    /* access modifiers changed from: protected */
    public int getCameraRotation() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getCameraState() {
        return this.mCameraState;
    }

    /* access modifiers changed from: protected */
    public CookieStore getCookieStore() {
        return Camera2OpenManager.getInstance().getCookieStore();
    }

    /* access modifiers changed from: protected */
    public Rect getCropRegion() {
        return HybridZoomingSystem.toCropRegion(getDeviceBasedZoomRatio(), getActiveArraySize());
    }

    /* access modifiers changed from: protected */
    public String getDebugInfo() {
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0044, code lost:
        if (com.android.camera.CameraSettings.isSuperNightUWOpen(r6.mModuleIndex) == false) goto L_0x011b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final float getDeviceBasedZoomRatio() {
        float f;
        float f2;
        float ultraTeleMinZoomRatio;
        float f3;
        int i = this.mModuleIndex;
        if (i == 182) {
            return 2.0f;
        }
        float f4 = this.mZoomRatio;
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            int i2 = this.mActualCameraId;
            if (!(i == 165 || i == 167 || i == 169 || i == 180 || i == 183 || i == 186 || i == 188)) {
                if (i != 214 && i != 173) {
                    if (i != 174) {
                        switch (i) {
                            case 161:
                            case 162:
                            case 163:
                                break;
                        }
                    }
                }
            }
            if (i2 == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                String OO00o00 = C0122O00000o.instance().OO00o00();
                if (!CameraSettings.isMacroModeEnabled(this.mModuleIndex) || HybridZoomingSystem.sDefaultMacroOpticalZoomRatio == HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR) {
                    f3 = HybridZoomingSystem.clamp(this.mZoomRatio / HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR, 1.0f, this.mCameraCapabilities.getMaxZoomRatio());
                } else if (CameraSettings.isMacroModeEnabled(this.mModuleIndex) && !TextUtils.isEmpty(OO00o00)) {
                    f3 = HybridZoomingSystem.clamp(this.mZoomRatio * Float.valueOf(OO00o00).floatValue(), getMinZoomRatio() * Float.valueOf(OO00o00).floatValue(), getMaxZoomRatio() * Float.valueOf(OO00o00).floatValue());
                }
                f4 = f3;
            } else {
                if (i2 == Camera2DataContainer.getInstance().getAuxCameraId()) {
                    String OO00oO0 = C0122O00000o.instance().OO00oO0();
                    f = this.mCameraCapabilities.getMaxZoomRatio();
                    int i3 = this.mModuleIndex;
                    if ((i3 == 162 || i3 == 180) && OO00oO0 != null) {
                        f = Math.min(f, Float.parseFloat(OO00oO0));
                    }
                    f2 = this.mZoomRatio;
                    ultraTeleMinZoomRatio = HybridZoomingSystem.getTeleMinZoomRatio();
                } else if (i2 == Camera2DataContainer.getInstance().getUltraTeleCameraId()) {
                    String OO00oO02 = C0122O00000o.instance().OO00oO0();
                    float maxZoomRatio = this.mCameraCapabilities.getMaxZoomRatio();
                    int i4 = this.mModuleIndex;
                    if ((i4 == 162 || i4 == 180) && OO00oO02 != null) {
                        maxZoomRatio = Math.min(maxZoomRatio, Float.parseFloat(OO00oO02));
                    }
                    f2 = this.mZoomRatio;
                    ultraTeleMinZoomRatio = HybridZoomingSystem.getUltraTeleMinZoomRatio();
                }
                f4 = HybridZoomingSystem.clamp(f2 / ultraTeleMinZoomRatio, 1.0f, f);
            }
        }
        return f4;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    /* access modifiers changed from: protected */
    public CameraSize getJpegThumbnailSize() {
        return Util.getOptimalJpegThumbnailSize(this.mCameraCapabilities.getSupportedThumbnailSizes(), ((double) this.mPreviewSize.getWidth()) / ((double) this.mPreviewSize.getHeight()));
    }

    public float getMaxZoomRatio() {
        return this.mMaxZoomRatio;
    }

    public float getMinZoomRatio() {
        return this.mMinZoomRatio;
    }

    public int getModuleIndex() {
        return this.mModuleIndex;
    }

    public MutexModeManager getMutexModePicker() {
        return this.mMutexModePicker;
    }

    public abstract int getOperatingMode();

    public int getPortraitLightingVersion() {
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities != null) {
            return cameraCapabilities.getPortraitLightingVersion();
        }
        return 1;
    }

    public CameraSize getPreviewSize() {
        return this.mPreviewSize;
    }

    public Resources getResources() {
        return this.mActivity.getResources();
    }

    /* access modifiers changed from: protected */
    public int getScreenDelay() {
        Camera camera = this.mActivity;
        return (camera == null || camera.startFromKeyguard()) ? 30000 : 60000;
    }

    public String getString(int i) {
        return CameraAppImpl.getAndroidContext().getString(i);
    }

    public List getSupportedSettingKeys() {
        return new ArrayList();
    }

    public abstract String getTag();

    /* access modifiers changed from: protected */
    public int getTriggerMode() {
        return this.mTriggerMode;
    }

    public String getUnInterruptableReason() {
        return this.mUnInterruptableReason;
    }

    public Window getWindow() {
        return this.mActivity.getWindow();
    }

    public float getZoomRatio() {
        return this.mZoomRatio;
    }

    /* access modifiers changed from: protected */
    public void handlePendingScreenSlide() {
        if (this.mPendingScreenSlideKeyCode > 0 && this.mActivity != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("process pending screen slide: ");
            sb.append(this.mPendingScreenSlideKeyCode);
            Log.d(TAG, sb.toString());
            this.mActivity.handleScreenSlideKeyEvent(this.mPendingScreenSlideKeyCode, null);
            this.mPendingScreenSlideKeyCode = 0;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0083, code lost:
        if (186 != r3) goto L_0x0094;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x008d, code lost:
        if (r9.equals(getString(com.android.camera.R.string.pref_camera_volumekey_function_entryvalue_timer)) == false) goto L_0x0094;
     */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00a0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean handleVolumeKeyEvent(boolean z, boolean z2, int i, boolean z3) {
        String str;
        DataItemGlobal dataItemGlobal;
        String str2;
        String str3;
        if (!isAlive()) {
            return true;
        }
        if (!z3) {
            int i2 = this.mModuleIndex;
            String str4 = "";
            if (!(i2 == 161 || i2 == 174 || i2 == 183 || (i2 == 184 && !DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPhoto()))) {
                int i3 = this.mModuleIndex;
                if (!(i3 == 179 || i3 == 209)) {
                    if (ModuleManager.isVideoCategory(getModuleIndex())) {
                        if (getModuleIndex() == 180) {
                            dataItemGlobal = DataRepository.dataItemGlobal();
                            str3 = getString(R.string.pref_pro_video_volumekey_function_default);
                            str2 = CameraSettings.KEY_VOLUME_PRO_VIDEO_FUNCTION;
                        } else {
                            dataItemGlobal = DataRepository.dataItemGlobal();
                            str3 = getString(R.string.pref_video_volumekey_function_default);
                            str2 = CameraSettings.KEY_VOLUME_VIDEO_FUNCTION;
                        }
                        str = dataItemGlobal.getString(str2, str3);
                    } else {
                        str = CameraSettings.getVolumeCameraFunction(str4);
                        int moduleIndex = getModuleIndex();
                        if (173 != moduleIndex) {
                        }
                    }
                    if (!str.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_shutter)) || str.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_timer))) {
                        performKeyClicked(20, str, i, z2);
                        return true;
                    } else if (ModuleManager.isPanoramaModule() || ModuleManager.isWideSelfieModule() || !str.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_zoom))) {
                        return false;
                    } else {
                        if (isZoomSupported()) {
                            if (isZoomEnabled()) {
                                if (z2) {
                                    onZoomingActionStart(1);
                                    boolean zoomIn = z ? zoomIn(0.1f) : zoomOut(0.1f);
                                    if (i == 0 && zoomIn) {
                                        CameraStatUtils.trackZoomAdjusted("volume", false);
                                    }
                                } else {
                                    onZoomingActionEnd(1);
                                }
                            }
                            return true;
                        }
                        performKeyClicked(20, getString(R.string.pref_camera_volumekey_function_entryvalue_shutter), i, z2);
                        return true;
                    }
                }
            }
            str = CameraSettings.getKeyVolumeLiveFunction(str4);
            if (!str.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_shutter))) {
            }
            performKeyClicked(20, str, i, z2);
            return true;
        }
        str = getString(R.string.pref_camera_volumekey_function_entryvalue_shutter);
        if (!str.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_shutter))) {
        }
        performKeyClicked(20, str, i, z2);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean hasCameraException() {
        return this.mCameraDisabled || this.mOpenCameraFail || this.mCameraHardwareError || !this.mActivity.couldShowErrorDialog();
    }

    public void hideMoreMode(boolean z) {
        Log.d(TAG, "hideMore");
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges != null && isMoreModeShowing(z)) {
            configChanges.configModeMore(z);
        }
    }

    /* access modifiers changed from: protected */
    public void hideTipMessage(@StringRes int i) {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips == null) {
            return;
        }
        if (i <= 0 || bottomPopupTips.containTips(i)) {
            bottomPopupTips.directlyHideTips();
        }
    }

    public void initByCapability(CameraCapabilities cameraCapabilities) {
        if (cameraCapabilities == null) {
            Log.w(TAG, "init by capability(capability == null)");
            return;
        }
        this.mAeLockSupported = cameraCapabilities.isAELockSupported();
        this.mAwbLockSupported = cameraCapabilities.isAWBLockSupported();
        this.mFocusAreaSupported = cameraCapabilities.isAFRegionSupported();
        this.mMeteringAreaSupported = cameraCapabilities.isAERegionSupported();
        C0122O00000o instance = C0122O00000o.instance();
        boolean z = true;
        boolean z2 = instance.OO0O0oo() && !this.mFocusAreaSupported && this.mMeteringAreaSupported && this.mAeLockSupported;
        this.mAELockOnlySupported = z2;
        boolean z3 = this.mFocusAreaSupported || this.mAELockOnlySupported;
        this.mFocusOrAELockSupported = z3;
        boolean o00OOOOO = instance.o00OOOOO();
        if (!CameraSettings.isAEAFLockSupport() || (!o00OOOOO && !isBackCamera() && !this.mAELockOnlySupported)) {
            z = false;
        }
        this.m3ALockSupported = z;
    }

    /* access modifiers changed from: protected */
    public void initializeCapabilities() {
        this.mCameraCapabilities.setOperatingMode(getOperatingMode());
        initByCapability(this.mCameraCapabilities);
        this.mMaxFaceCount = this.mCameraCapabilities.getMaxFaceCount();
        initializeZoomRangeFromCapabilities();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x02a8, code lost:
        if (com.android.camera.CameraSettings.isMacroModeEnabled(r7.mModuleIndex) != false) goto L_0x0136;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x02bd, code lost:
        if (com.android.camera.CameraSettings.isMacroModeEnabled(r7.mModuleIndex) == false) goto L_0x02c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004e, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOOOO00() != false) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005f, code lost:
        if (com.android.camera.CameraSettings.isSuperNightUWOpen(r7.mModuleIndex) == false) goto L_0x02c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0063, code lost:
        setMinZoomRatio(com.android.camera.HybridZoomingSystem.getMinimumOpticalZoomRatio(r7.mModuleIndex));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0101, code lost:
        if (com.android.camera.CameraSettings.isMacroModeEnabled(r7.mModuleIndex) == false) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0162, code lost:
        if (com.android.camera.CameraSettings.isMacroModeEnabled(r7.mModuleIndex) == false) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x018b, code lost:
        if (com.android.camera.CameraSettings.isUltraWideConfigOpen(r7.mModuleIndex) != false) goto L_0x0077;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initializeZoomRangeFromCapabilities() {
        float f;
        float f2;
        float f3;
        float maxZoomRatio;
        float ultraTeleMinZoomRatio;
        float maxZoomRatio2;
        float ultraTeleMinZoomRatio2;
        if (this.mZoomSupported) {
            float f4 = 1.0f;
            if (isBackCamera()) {
                int moduleIndex = getModuleIndex();
                if (moduleIndex != 165) {
                    if (moduleIndex != 167) {
                        if (moduleIndex != 169) {
                            if (moduleIndex != 180) {
                                if (moduleIndex != 183) {
                                    if (moduleIndex != 186) {
                                        if (moduleIndex != 188) {
                                            if (moduleIndex == 214 || moduleIndex == 204) {
                                                setMinZoomRatio(1.0f);
                                                setMaxZoomRatio(6.0f);
                                                return;
                                            } else if (moduleIndex != 205) {
                                                switch (moduleIndex) {
                                                    case 161:
                                                        break;
                                                    case 162:
                                                        if (DataRepository.dataItemGlobal().isNormalIntent() || !this.mCameraCapabilities.isSupportLightTripartite()) {
                                                            if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                                                                if (!CameraSettings.isMacroModeEnabled(this.mModuleIndex)) {
                                                                    if (!isInVideoSAT()) {
                                                                        if (!CameraSettings.isVideoQuality8KOpen(this.mModuleIndex) && !CameraSettings.isVhdrOn(getCameraCapabilities(), this.mModuleIndex)) {
                                                                            f4 = HybridZoomingSystem.getMinimumOpticalZoomRatio(this.mModuleIndex);
                                                                        }
                                                                        setMinZoomRatio(f4);
                                                                        break;
                                                                    } else {
                                                                        if (!CameraSettings.isVideoQuality8KOpen(this.mModuleIndex)) {
                                                                            f4 = HybridZoomingSystem.getMinimumOpticalZoomRatio(this.mModuleIndex);
                                                                        }
                                                                        setMinZoomRatio(f4);
                                                                        f = C0122O00000o.instance().O0oooO0();
                                                                        break;
                                                                    }
                                                                }
                                                            } else {
                                                                setMinZoomRatio(1.0f);
                                                                if (!CameraSettings.isUltraWideConfigOpen(this.mModuleIndex)) {
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        break;
                                                    case 163:
                                                        break;
                                                    default:
                                                        switch (moduleIndex) {
                                                            case 172:
                                                                boolean isUltraWideBackCamera = isUltraWideBackCamera();
                                                                setMinZoomRatio(1.0f);
                                                                if (!isUltraWideBackCamera) {
                                                                    f = 3.0f;
                                                                    break;
                                                                }
                                                                break;
                                                            case 173:
                                                                break;
                                                            case 174:
                                                                break;
                                                            case 175:
                                                                break;
                                                        }
                                                }
                                            } else {
                                                setMinZoomRatio(1.0f);
                                                f3 = 10.0f;
                                            }
                                        }
                                        setMinZoomRatio(HybridZoomingSystem.getMinimumOpticalZoomRatio(moduleIndex));
                                        f3 = HybridZoomingSystem.getMaximumOpticalZoomRatio(moduleIndex);
                                    }
                                }
                                if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                                    if (!CameraSettings.isMacroModeEnabled(this.mModuleIndex)) {
                                        ultraTeleMinZoomRatio2 = HybridZoomingSystem.getMinimumOpticalZoomRatio(this.mModuleIndex);
                                        setMinZoomRatio(ultraTeleMinZoomRatio2);
                                        setVideoMaxZoomRatioByTele();
                                        return;
                                    }
                                    setMinZoomRatio(HybridZoomingSystem.getMinimumMacroOpticalZoomRatio());
                                    f = HybridZoomingSystem.getMaximumMacroOpticalZoomRatio();
                                    setMaxZoomRatio(f);
                                }
                                setMinZoomRatio(1.0f);
                                if (!CameraSettings.isUltraWideConfigOpen(this.mModuleIndex)) {
                                }
                                setMaxZoomRatio(2.0f);
                                return;
                            }
                        } else if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                            setMinZoomRatio(HybridZoomingSystem.getMinimumOpticalZoomRatio(this.mModuleIndex));
                            if (isInVideoSAT()) {
                                f3 = C0122O00000o.instance().O0oooO0();
                            }
                            setVideoMaxZoomRatioByTele();
                            return;
                        } else {
                            setMinZoomRatio(1.0f);
                        }
                        maxZoomRatio = this.mCameraCapabilities.getMaxZoomRatio();
                        f = Math.min(f3, maxZoomRatio);
                        setMaxZoomRatio(f);
                    }
                    if (!C0122O00000o.instance().OOOOO00() || !DataRepository.dataItemRunning().getComponentUltraPixel().isRearSwitchOn()) {
                        if (!CameraSettings.isMacroModeEnabled(this.mModuleIndex)) {
                            if (!"macro".equals(DataRepository.dataItemConfig().getManuallyDualLens().getComponentValue(this.mModuleIndex))) {
                                if (!CameraSettings.isUltraWideConfigOpen(this.mModuleIndex)) {
                                    if (!ComponentManuallyDualLens.LENS_ULTRA.equals(DataRepository.dataItemConfig().getManuallyDualLens().getComponentValue(this.mModuleIndex))) {
                                        setMinZoomRatio(1.0f);
                                        if (this.mModuleIndex == 180) {
                                            if (isAuxCamera()) {
                                                ultraTeleMinZoomRatio2 = HybridZoomingSystem.getTeleMinZoomRatio();
                                            } else {
                                                if (isUltraTeleCamera()) {
                                                    ultraTeleMinZoomRatio2 = HybridZoomingSystem.getUltraTeleMinZoomRatio();
                                                }
                                                setMinZoomRatio(1.0f);
                                                f = Math.min(6.0f, this.mCameraCapabilities.getMaxZoomRatio());
                                                setMaxZoomRatio(f);
                                            }
                                            setMinZoomRatio(ultraTeleMinZoomRatio2);
                                            setVideoMaxZoomRatioByTele();
                                            return;
                                        }
                                        if (isAuxCamera()) {
                                            setMinZoomRatio(HybridZoomingSystem.getTeleMinZoomRatio());
                                            if (!C0124O00000oO.isSupportedOpticalZoom() || C0122O00000o.instance().OOOOoOo()) {
                                                ultraTeleMinZoomRatio = HybridZoomingSystem.getTeleMinZoomRatio();
                                            } else {
                                                maxZoomRatio2 = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getSATCameraId()).getMaxZoomRatio();
                                                f2 = HybridZoomingSystem.getTeleMinZoomRatio();
                                                maxZoomRatio = f2 * this.mCameraCapabilities.getMaxZoomRatio();
                                                f = Math.min(f3, maxZoomRatio);
                                                setMaxZoomRatio(f);
                                            }
                                        } else if (isUltraTeleCamera()) {
                                            setMinZoomRatio(HybridZoomingSystem.getUltraTeleMinZoomRatio());
                                            if (C0124O00000oO.isSupportedOpticalZoom()) {
                                                maxZoomRatio2 = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getSATCameraId()).getMaxZoomRatio();
                                                f2 = HybridZoomingSystem.getUltraTeleMinZoomRatio();
                                                maxZoomRatio = f2 * this.mCameraCapabilities.getMaxZoomRatio();
                                                f = Math.min(f3, maxZoomRatio);
                                                setMaxZoomRatio(f);
                                            }
                                            ultraTeleMinZoomRatio = HybridZoomingSystem.getUltraTeleMinZoomRatio();
                                        }
                                        f = ultraTeleMinZoomRatio * this.mCameraCapabilities.getMaxZoomRatio();
                                        setMaxZoomRatio(f);
                                    }
                                }
                                setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                                setMaxZoomRatio(2.0f);
                                return;
                            }
                        }
                        setMinZoomRatio(HybridZoomingSystem.getMinimumMacroOpticalZoomRatio());
                        f = HybridZoomingSystem.getMaximumMacroOpticalZoomRatio();
                        setMaxZoomRatio(f);
                    }
                    setMinZoomRatio(1.0f);
                    f = HybridZoomingSystem.getMaximumOpticalZoomRatio(175);
                    setMaxZoomRatio(f);
                }
                if (DataRepository.dataItemGlobal().isNormalIntent() || !this.mCameraCapabilities.isSupportLightTripartite()) {
                    if (!HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                        setMinZoomRatio(1.0f);
                        if (!CameraSettings.isUltraWideConfigOpen(this.mModuleIndex)) {
                        }
                        setMaxZoomRatio(2.0f);
                        return;
                    }
                }
            }
            setMinZoomRatio(1.0f);
            f = this.mCameraCapabilities.getMaxZoomRatio();
            setMaxZoomRatio(f);
        }
    }

    /* access modifiers changed from: protected */
    public void initializeZoomRatio() {
        float f = HybridZoomingSystem.toFloat(((DataItemRunning) DataRepository.provider().dataRunning()).getComponentRunningZoom().getComponentValue(this.mModuleIndex), 1.0f);
        if (!HybridZoomingSystem.IS_3_OR_MORE_SAT || !isBackCamera()) {
            if (C0122O00000o.instance().OO0oo0O()) {
                int i = this.mOrientation;
                boolean z = (i == 0 || i == 180) && ModuleManager.isSupportCropFrontMode();
                if (z) {
                    f = C0122O00000o.instance().O0ooO0O();
                }
            }
            setZoomRatio(f);
            return;
        }
        setZoomRatio(f);
        if (C0124O00000oO.isSupportedOpticalZoom()) {
            float f2 = this.mZoomRatio;
            if (f2 < 1.0f) {
                updateUltraWideCapability(f2);
            }
        }
    }

    public boolean isAlive() {
        return isCreated() && !isDeparted();
    }

    /* access modifiers changed from: protected */
    public final boolean isAuxCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getAuxCameraId();
    }

    /* access modifiers changed from: protected */
    public final boolean isBackCamera() {
        return this.mBogusCameraId == 0;
    }

    public boolean isBlockSnap() {
        return isDoingAction();
    }

    /* access modifiers changed from: protected */
    public final boolean isBokehFrontCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getBokehFrontCameraId();
    }

    /* access modifiers changed from: protected */
    public final boolean isBokehUltraWideBackCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getUltraWideBokehCameraId();
    }

    /* access modifiers changed from: protected */
    public boolean isCameraSwitchingDuringZoomingAllowed() {
        return CameraSettings.isSuperNightUWOpen(this.mModuleIndex) && HybridZoomingSystem.IS_3_OR_MORE_SAT && !CameraSettings.isMacroModeEnabled(this.mModuleIndex) && isBackCamera();
    }

    public boolean isCaptureIntent() {
        return false;
    }

    public boolean isCreated() {
        return this.mIsCreated.get();
    }

    public boolean isDeparted() {
        return this.mIsDeparted.get();
    }

    public boolean isDeviceAlive() {
        boolean z = this.mCamera2Device != null && isAlive();
        if (!z) {
            Object[] objArr = new Object[3];
            objArr[0] = this.mCamera2Device != null ? "valid" : "invalid";
            objArr[1] = isCreated() ? "created" : "destroyed";
            objArr[2] = isDeparted() ? "departed" : "alive";
            String format = String.format("device: %s module: %s|%s", objArr);
            StringBuilder sb = new StringBuilder();
            sb.append(Util.getCallers(1));
            sb.append("|");
            sb.append(format);
            Log.e(TAG, sb.toString());
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public final boolean isDualCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getSATCameraId() || this.mActualCameraId == Camera2DataContainer.getInstance().getBokehCameraId();
    }

    /* access modifiers changed from: protected */
    public final boolean isDualFrontCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getSATFrontCameraId() || this.mActualCameraId == Camera2DataContainer.getInstance().getBokehFrontCameraId();
    }

    public boolean isFrameAvailable() {
        return this.mIsFrameAvailable.get();
    }

    public final boolean isFrontCamera() {
        return this.mBogusCameraId == 1;
    }

    public boolean isIgnoreTouchEvent() {
        return this.mIgnoreTouchEvent;
    }

    public boolean isInDisplayRect(int i, int i2) {
        Rect rect = this.mDisplayRect;
        if (rect == null) {
            return false;
        }
        return rect.contains(i, i2);
    }

    public boolean isInTapableRect(int i, int i2) {
        if (this.mDisplayRect == null) {
            return false;
        }
        return Util.getTapableRectWithEdgeSlop(judgeTapableRectByUiStyle(), this.mDisplayRect, this.mModuleIndex, this.mActivity).contains(i, i2);
    }

    /* access modifiers changed from: protected */
    public boolean isInVideoSAT() {
        return CameraSettings.supportVideoSATForVideoQuality(getModuleIndex()) && getActualCameraId() == Camera2DataContainer.getInstance().getVideoSATCameraId();
    }

    public boolean isKeptBitmapTexture() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMainBackCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getMainBackCameraId();
    }

    public boolean isMeteringAreaOnly() {
        return false;
    }

    public boolean isMimojiMode() {
        int i = this.mModuleIndex;
        return i == 177 || i == 184;
    }

    public boolean isMoreModeShowing(boolean z) {
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        boolean z2 = false;
        if (baseDelegate != null) {
            int activeFragment = baseDelegate.getActiveFragment(R.id.rotation_full_screen_feature);
            if (z) {
                if (activeFragment == 65526) {
                    z2 = true;
                }
                return z2;
            } else if (activeFragment == 65525) {
                z2 = true;
            }
        }
        return z2;
    }

    public boolean isNeedHapticFeedback() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isNeedMute() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isParallelSessionEnable() {
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean isPictureUseDualFrontCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getBokehFrontCameraId() && !C0122O00000o.instance().OO0OOO();
    }

    public boolean isPortraitMode() {
        return this.mModuleIndex == 171;
    }

    public boolean isPostProcessing() {
        return false;
    }

    public boolean isRecording() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isRepeatingRequestInProgress() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isRestoring() {
        return this.mRestoring;
    }

    public boolean isSelectingCapturedResult() {
        return false;
    }

    public boolean isShot2GalleryOrEnableParallel() {
        return false;
    }

    public boolean isShowAeAfLockIndicator() {
        return false;
    }

    public boolean isShowCaptureButton() {
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean isSingleCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getMainBackCameraId() || this.mActualCameraId == Camera2DataContainer.getInstance().getFrontCameraId();
    }

    /* access modifiers changed from: protected */
    public boolean isSquareModeChange() {
        return ModuleManager.isSquareModule() != (this.mActivity.getCameraScreenNail().getRenderTargetRatio() == 2);
    }

    /* access modifiers changed from: protected */
    public final boolean isStandaloneMacroCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getStandaloneMacroCameraId();
    }

    public boolean isStartCountCapture() {
        return false;
    }

    public boolean isSupportAELockOnly() {
        return this.mAELockOnlySupported;
    }

    public boolean isSupportFocusShoot() {
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean isTextureExpired() {
        Camera camera = this.mActivity;
        return camera == null || camera.getCameraScreenNail().getSurfaceCreatedTimestamp() != this.mSurfaceCreatedTimestamp;
    }

    public boolean isThermalThreshold() {
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean isUltraTeleCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getUltraTeleCameraId();
    }

    /* access modifiers changed from: protected */
    public final boolean isUltraWideBackCamera() {
        return this.mActualCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId();
    }

    public boolean isZoomEnabled() {
        return true;
    }

    public boolean isZoomRatioBetweenUltraAndWide() {
        float f = this.mZoomRatio;
        return f >= HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR && f < 1.0f;
    }

    public boolean isZoomRatioBetweenUltraAndWide(float f) {
        return f >= HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR && f < 1.0f;
    }

    /* access modifiers changed from: protected */
    public boolean isZoomSupported() {
        return this.mZoomSupported && !isFrontCamera();
    }

    /* access modifiers changed from: protected */
    public boolean isZslPreferred() {
        return false;
    }

    public String join(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        for (int append : iArr) {
            sb.append(append);
            sb.append(",");
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public boolean judgeTapableRectByUiStyle() {
        return !isRecording();
    }

    /* access modifiers changed from: protected */
    public void keepAutoHibernation() {
        Log.d(TAG, "keepAutoHibernation");
        exitAutoHibernation();
        this.mHandler.sendEmptyMessageDelayed(65, 175000);
    }

    /* access modifiers changed from: protected */
    public void keepScreenOn() {
        if (!this.mActivity.isActivityPaused()) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(17);
                this.mHandler.removeMessages(2);
                this.mHandler.removeMessages(52);
                getWindow().addFlags(128);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void keepScreenOnAwhile() {
        if (!this.mActivity.isActivityPaused()) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendEmptyMessageDelayed(17, 1000);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void listenPhoneState(boolean z) {
        String str;
        if (this.mActivity != null) {
            TelephonyManager telephonyManager = this.mTelephonyManager;
            if (telephonyManager != null) {
                String str2 = TAG;
                PhoneStateListener phoneStateListener = this.mPhoneStateListener;
                if (z) {
                    telephonyManager.listen(phoneStateListener, 32);
                    str = "listen call state";
                } else {
                    telephonyManager.listen(phoneStateListener, 0);
                    str = "listen none";
                }
                Log.v(str2, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void mapTapCoordinate(Object obj) {
        if (obj instanceof Point) {
            Point point = (Point) obj;
            int i = point.x;
            Rect rect = this.mDisplayRect;
            point.x = i - rect.left;
            point.y -= rect.top;
        } else if (obj instanceof RectF) {
            RectF rectF = (RectF) obj;
            float f = rectF.left;
            Rect rect2 = this.mDisplayRect;
            int i2 = rect2.left;
            rectF.left = f - ((float) i2);
            rectF.right -= (float) i2;
            float f2 = rectF.top;
            int i3 = rect2.top;
            rectF.top = f2 - ((float) i3);
            rectF.bottom -= (float) i3;
        }
    }

    /* access modifiers changed from: protected */
    public boolean maySwitchCameraLens(float f, float f2) {
        float teleMinZoomRatio = HybridZoomingSystem.getTeleMinZoomRatio();
        if (f < f2) {
            if (f < 1.0f && f2 >= 1.0f) {
                return true;
            }
            if (f < teleMinZoomRatio && f2 >= teleMinZoomRatio) {
                return true;
            }
            if (HybridZoomingSystem.IS_4_OR_MORE_SAT && f < 3.7f && f2 >= 3.7f) {
                return true;
            }
        } else if (f > f2) {
            if (HybridZoomingSystem.IS_4_OR_MORE_SAT && f >= 3.7f && f2 < 3.7f) {
                return true;
            }
            if (f >= teleMinZoomRatio && f2 < teleMinZoomRatio) {
                return true;
            }
            if (f >= 1.0f && f2 < 1.0f) {
                return true;
            }
        }
        return false;
    }

    public void notifyAfterFirstFrameArrived() {
    }

    public void notifyDualZoom(boolean z) {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null && HybridZoomingSystem.IS_2_SAT) {
            camera2Proxy.setOpticalZoomToTele(z);
            this.mCamera2Device.resumePreview();
        }
    }

    public void notifyError() {
        this.mCameraHardwareError = true;
        setCameraState(0);
        if (!this.mActivity.isActivityPaused()) {
            onCameraException();
            return;
        }
        this.mActivity.releaseAll(true, true);
        this.mActivity.finish();
    }

    public void notifyZooming(boolean z) {
        this.isZooming = z;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public boolean onBackPressed() {
        if (!isCaptureIntent() && !DataRepository.dataItemGlobal().getComponentModuleList().isCommonMode(this.mModuleIndex)) {
            ModeChangeController modeChangeController = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
            if (modeChangeController != null) {
                modeChangeController.resetToCommonMode();
                return true;
            }
        }
        return false;
    }

    public void onBluetoothHeadsetConnected() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(getModuleIndex());
        sb.append("> onBluetoothHeadsetConnected");
        Log.v(TAG, sb.toString());
    }

    public void onBluetoothHeadsetConnecting() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(getModuleIndex());
        sb.append("> onBluetoothHeadsetConnecting");
        Log.v(TAG, sb.toString());
    }

    public void onBluetoothHeadsetDisconnected() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(getModuleIndex());
        sb.append("> onBluetoothHeadsetDisconnected");
        Log.v(TAG, sb.toString());
    }

    @CallSuper
    public void onBroadcastReceived(Context context, Intent intent) {
        String str;
        Camera camera = this.mActivity;
        if (camera != null && !camera.isFinishing() && intent != null) {
            String action = intent.getAction();
            StringBuilder sb = new StringBuilder();
            sb.append("onReceive: action=");
            sb.append(action);
            String sb2 = sb.toString();
            String str2 = TAG;
            Log.v(str2, sb2);
            if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
                Log.d(str2, "SD card available");
                Storage.initStorage(context);
                this.mActivity.getScreenHint().updateHint();
            } else if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
                Log.d(str2, "SD card unavailable");
                FileCompat.updateSDPath();
                this.mActivity.getScreenHint().updateHint();
                this.mActivity.getThumbnailUpdater().getLastThumbnail();
            } else {
                if ("android.intent.action.MEDIA_SCANNER_STARTED".equals(action)) {
                    str = "media scanner started";
                } else if ("android.intent.action.MEDIA_SCANNER_FINISHED".equals(action)) {
                    this.mActivity.getThumbnailUpdater().getLastThumbnail();
                    str = "media scanner finisheded";
                } else {
                    return;
                }
                Log.d(str2, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCameraException() {
        StringBuilder sb = new StringBuilder();
        sb.append("onCameraException: mid = ");
        sb.append(this.mModuleIndex);
        sb.append(", cid = ");
        sb.append(this.mActualCameraId);
        String sb2 = sb.toString();
        String str = TAG;
        Log.e(str, sb2);
        if (CameraSchedulers.isOnMainThread()) {
            Camera camera = this.mActivity;
            if (camera == null || camera.getCurrentModule() == this) {
                if ((this.mOpenCameraFail || this.mCameraHardwareError) && !this.mActivity.isActivityPaused() && this.mActivity.couldShowErrorDialog()) {
                    Util.showErrorAndFinish(this.mActivity, CameraSettings.updateOpenCameraFailTimes() > 1 ? R.string.cannot_connect_camera_twice : R.string.cannot_connect_camera_once, false);
                    this.mActivity.showErrorDialog();
                }
                if (this.mCameraDisabled && this.mActivity.couldShowErrorDialog()) {
                    Util.showErrorAndFinish(this.mActivity, R.string.camera_disabled, false);
                    this.mActivity.showErrorDialog();
                }
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("onCameraException: module changed: prev = ");
                sb3.append(this);
                Log.d(str, sb3.toString());
                StringBuilder sb4 = new StringBuilder();
                sb4.append("onCameraException: module changed: curr = ");
                sb4.append(this.mActivity.getCurrentModule());
                Log.d(str, sb4.toString());
                return;
            }
        } else {
            sendOpenFailMessage();
        }
        enableCameraControls(false);
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        initializeCapabilities();
        initializeZoomRatio();
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.registerPreviewPartialMetadata(registerPartialMetaDataFunction());
            this.mCamera2Device.registerPreviewMeatedata(registerMetaDataFunction());
        }
        this.mCamera2Device.setMetaDataCallback(this);
        if (isFrontCamera() && this.mActivity.isScreenSlideOff()) {
            this.mCamera2Device.setAELock(true);
        }
        updateLensDirtyDetect(false);
    }

    /* access modifiers changed from: protected */
    public void onCapabilityChanged(CameraCapabilities cameraCapabilities) {
        initByCapability(cameraCapabilities);
        StringBuilder sb = new StringBuilder();
        sb.append("onCapabilityChanged: mFocusAreaSupported = ");
        sb.append(this.mFocusAreaSupported);
        sb.append(", mAELockOnlySupported = ");
        sb.append(this.mAELockOnlySupported);
        Log.d(TAG, sb.toString());
    }

    public void onCreate(int i, int i2) {
        ThreadUtils.assertCameraSetupThread();
        String format = String.format("onCreate: moduleIndex->%d, cameraId->%d@%s", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), this});
        String str = TAG;
        Log.k(3, str, format);
        this.mModuleIndex = i;
        this.mBogusCameraId = i2;
        this.mActualCameraId = Camera2DataContainer.getInstance().getActualOpenCameraId(i2, i);
        for (Cookie cookie : getCookieStore().getCookies()) {
            StringBuilder sb = new StringBuilder();
            sb.append("enumerating: ");
            sb.append(cookie.mCamera2Device);
            Log.d(str, sb.toString());
            Camera2Proxy camera2Proxy = cookie.mCamera2Device;
            if (camera2Proxy != null && camera2Proxy.getId() == this.mActualCameraId) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("setCameraDevice: ");
                sb2.append(cookie.mCamera2Device);
                Log.d(str, sb2.toString());
                setCameraDevice(cookie.mCamera2Device);
            }
        }
        if (this.mCamera2Device != null) {
            this.mErrorCallback = new CameraErrorCallbackImpl(this.mActivity);
            this.mTelephonyManager = (TelephonyManager) this.mActivity.getSystemService("phone");
            this.mMainProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            this.mTopAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            this.mMutexModePicker = new MutexModeManager(this);
            this.mUpdateWorkThreadDisposable = Observable.create(new ObservableOnSubscribe() {
                public void subscribe(ObservableEmitter observableEmitter) {
                    BaseModule.this.mUpdateWorkThreadEmitter = observableEmitter.serialize();
                }
            }).observeOn(CameraSchedulers.sCameraSetupScheduler).subscribe((Consumer) this);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("create disposable ");
            sb3.append(this);
            sb3.append(" ");
            sb3.append(this.mUpdateWorkThreadDisposable);
            Log.d(str, sb3.toString());
            if ((getModuleIndex() == 165 || getModuleIndex() == 163) && C0122O00000o.instance().OOO0o00() && CameraSettings.isLensDirtyDetectEnabled()) {
                if (DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_LENS_DIRTY_TIP, C0122O00000o.instance().O0oo00())) {
                    if (this.mCameraCapabilities.getMiAlgoASDVersion() >= 2.0f) {
                        this.mDetectLensDirty = false;
                    } else {
                        this.mDetectLensDirty = true;
                    }
                    if (this.mCameraCapabilities.getMiAlgoASDVersion() >= 3.0f) {
                        this.mLensDirtyDetectEnable = true;
                    } else {
                        this.mLensDirtyDetectDisposable = Completable.complete().delay(15000, TimeUnit.MILLISECONDS, CameraSchedulers.sCameraSetupScheduler).doOnComplete(new ActionUpdateLensDirtyDetect(this, true)).subscribe();
                    }
                }
            }
            setCreated(true);
            this.mIsDeparted.set(false);
            this.isShowPreviewDebugInfo = Util.isShowPreviewDebugInfo();
            this.isGradienterOn = CameraSettings.isGradienterOn();
            return;
        }
        throw new CameraNotOpenException();
    }

    @CallSuper
    public void onDestroy() {
        String str = TAG;
        Log.d(str, "onDestroy: E");
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.getSensorStateManager().setSensorStateListener(null);
        }
        setCreated(false);
        ImageSaver imageSaver = this.mActivity.getImageSaver();
        if (imageSaver != null) {
            imageSaver.onModuleDestroy();
        }
        if (isParallelSessionEnable()) {
            LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
            if (localBinder != null) {
                localBinder.stopPostProcessor(this.mActivity.hashCode());
            }
        }
        Log.d(str, "onDestroy: X");
    }

    @MainThread
    public void onEvChanged(int i, int i2) {
        if (isAlive()) {
            this.mEvValue = i;
            this.mEvState = i2;
            if (i2 == 1 || i2 == 3) {
                this.mEvValueForTrack = i;
                CameraSettings.writeExposure(i);
                ViberatorContext.getInstance(getActivity().getApplicationContext()).performEVChange();
            }
            updatePreferenceInWorkThread(12);
        }
    }

    public void onFocusAreaChanged(int i, int i2) {
    }

    public boolean onGestureTrack(RectF rectF, boolean z) {
        return true;
    }

    public void onGradienterSwitched(boolean z) {
        this.isGradienterOn = z;
        this.mActivity.getSensorStateManager().setGradienterEnabled(z);
        this.mActivity.getSensorStateManager().register();
        updatePreferenceTrampoline(2, 5, 43);
    }

    public void onHostStopAndNotifyActionStop() {
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0026  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptZoomingEvent(float f, float f2, int i) {
        boolean z;
        int i2;
        if (isCameraSwitchingDuringZoomingAllowed()) {
            if (!CameraSettings.isSupportedOpticalZoom()) {
                int i3 = this.mModuleIndex;
                if (i3 == 163 || i3 == 165 || i3 == 186) {
                    z = false;
                    i2 = (f2 > 1.0f ? 1 : (f2 == 1.0f ? 0 : -1));
                    if (i2 < 0) {
                        CameraSettings.setVideoQuality8KOff(this.mModuleIndex);
                    }
                    if (!C0122O00000o.instance().OOOo000() || C0122O00000o.instance().OOo000()) {
                        float[] O00oOooo = C0122O00000o.instance().O00oOooo(173);
                        if (this.mModuleIndex == 173 && O00oOooo != null && O00oOooo.length == 3 && C0122O00000o.instance().getConfig().OOOo0oo() && C0122O00000o.instance().getConfig().Oo0OoOo()) {
                            if (f < 1.0f && i2 < 0) {
                                switchCameraLens(z, false, false, 2);
                                return true;
                            } else if (f >= 1.0f && f2 >= 1.0f && f2 < HybridZoomingSystem.getUltraTeleMinZoomRatio()) {
                                switchCameraLens(z, false, false, 2);
                                return true;
                            } else if (f < HybridZoomingSystem.getUltraTeleMinZoomRatio() && f2 >= 1.0f && f2 < HybridZoomingSystem.getUltraTeleMinZoomRatio()) {
                                switchCameraLens(z, false, false, 2);
                                return true;
                            } else if (f < HybridZoomingSystem.getUltraTeleMinZoomRatio() && f2 >= HybridZoomingSystem.getUltraTeleMinZoomRatio()) {
                                switchCameraLens(z, false, false, 2);
                                return true;
                            }
                        }
                        float ultraTeleMinZoomRatio = C0122O00000o.instance().OOo000() ? HybridZoomingSystem.getUltraTeleMinZoomRatio() : HybridZoomingSystem.getTeleMinZoomRatio();
                        int i4 = (f > 1.0f ? 1 : (f == 1.0f ? 0 : -1));
                        if (i4 < 0 || f >= ultraTeleMinZoomRatio || i2 >= 0) {
                            int i5 = (f > 1.0f ? 1 : (f == 1.0f ? 0 : -1));
                            if (i5 < 0 && f2 >= 1.0f && f2 < ultraTeleMinZoomRatio) {
                                switchCameraLens(z, false, false, 2);
                                return true;
                            } else if (i4 < 0 || f >= ultraTeleMinZoomRatio || f2 < ultraTeleMinZoomRatio || !isTeleSupportVideoQuality()) {
                                int i6 = (f > ultraTeleMinZoomRatio ? 1 : (f == ultraTeleMinZoomRatio ? 0 : -1));
                                if (i6 < 0 || f2 < 1.0f || f2 >= ultraTeleMinZoomRatio || (!isAuxCamera() && !isUltraTeleCamera())) {
                                    if (i5 < 0 && f2 >= ultraTeleMinZoomRatio) {
                                        switchCameraLens(z, false, false, 2);
                                        return true;
                                    } else if (i6 >= 0 && i2 < 0) {
                                        switchCameraLens(z, false, false, 2);
                                        return true;
                                    } else if (isUltraWideBackCamera() && f2 >= 1.0f) {
                                        switchCameraLens(z, false, false, 2);
                                        return true;
                                    } else if (isMainBackCamera() && f2 >= ultraTeleMinZoomRatio && isTeleSupportVideoQuality()) {
                                        switchCameraLens(z, false, false, 2);
                                        return true;
                                    }
                                } else if (CameraSettings.isSuperNightUWOpen(this.mModuleIndex)) {
                                    return false;
                                } else {
                                    if (CameraSettings.isVideoQuality8KOpen(getModuleIndex()) && (!C0122O00000o.instance().OOOOoOo() || !CameraSettings.is8KCamcorderSupported(Camera2DataContainer.getInstance().getUltraTeleCameraId()))) {
                                        return false;
                                    }
                                    switchCameraLens(z, false, false, 2);
                                    return true;
                                }
                            } else if (CameraSettings.isSuperNightUWOpen(this.mModuleIndex)) {
                                return false;
                            } else {
                                switchCameraLens(z, false, false, 2);
                                return true;
                            }
                        } else {
                            switchCameraLens(z, false, false, 2);
                            return true;
                        }
                    } else if (f >= 1.0f && i2 < 0) {
                        switchCameraLens(z, false, false, 2);
                        return true;
                    } else if (f < 1.0f && f2 >= 1.0f) {
                        switchCameraLens(z, false, false, 2);
                        return true;
                    } else if (isUltraWideBackCamera() && f2 >= 1.0f) {
                        switchCameraLens(z, false, false, 2);
                        return true;
                    }
                }
            }
            z = true;
            i2 = (f2 > 1.0f ? 1 : (f2 == 1.0f ? 0 : -1));
            if (i2 < 0) {
            }
            if (!C0122O00000o.instance().OOOo000()) {
            }
            float[] O00oOooo2 = C0122O00000o.instance().O00oOooo(173);
            if (f < 1.0f) {
            }
            if (f >= 1.0f) {
            }
            if (f < HybridZoomingSystem.getUltraTeleMinZoomRatio()) {
            }
            switchCameraLens(z, false, false, 2);
            return true;
        }
        return false;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 701 && i != 700) {
            ModeProtocol.KeyEvent keyEvent2 = (ModeProtocol.KeyEvent) ModeCoordinatorImpl.getInstance().getAttachProtocol(239);
            if (keyEvent2 != null) {
                return keyEvent2.onKeyDown(i, keyEvent);
            }
            return false;
        } else if (!isUnInterruptable()) {
            this.mPendingScreenSlideKeyCode = 0;
            this.mActivity.handleScreenSlideKeyEvent(i, keyEvent);
            return true;
        } else if (i != 701 || !this.mActivity.getCameraIntentManager().isFromScreenSlide().booleanValue() || this.mActivity.isModeSwitched()) {
            this.mPendingScreenSlideKeyCode = i;
            StringBuilder sb = new StringBuilder();
            sb.append("pending screen slide: ");
            sb.append(i);
            sb.append(", reason: ");
            sb.append(getUnInterruptableReason());
            Log.d(TAG, sb.toString());
            return false;
        } else {
            this.mActivity.moveTaskToBack(false);
            this.mActivity.overridePendingTransition(R.anim.anim_screen_slide_fade_in, R.anim.anim_screen_slide_fade_out);
            return true;
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 82 || this.mActivity.startFromSecureKeyguard()) {
            if (i == 24 || i == 25 || i == 87 || i == 88) {
                boolean z = i == 24 || i == 88;
                if (handleVolumeKeyEvent(z, false, keyEvent.getRepeatCount(), keyEvent.getDevice().isExternal())) {
                    return true;
                }
            }
            ModeProtocol.KeyEvent keyEvent2 = (ModeProtocol.KeyEvent) ModeCoordinatorImpl.getInstance().getAttachProtocol(239);
            if (keyEvent2 != null) {
                return keyEvent2.onKeyUp(i, keyEvent);
            }
            return false;
        }
        openSettingActivity();
        return true;
    }

    public void onLongPress(float f, float f2) {
    }

    public void onMeteringAreaChanged(int i, int i2) {
    }

    public void onNewIntent() {
    }

    public void onNewUriArrived(Uri uri, String str) {
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        if (i != -1) {
            this.mOrientation = i;
            EffectController.getInstance().setOrientation(Util.getShootOrientation(this.mActivity, this.mOrientation));
            checkActivityOrientation();
            if (this.mOrientationCompensation != i2) {
                this.mOrientationCompensation = i2;
            }
        }
    }

    @CallSuper
    public void onPause() {
        Log.d(TAG, "onPause");
        this.mPaused = true;
        this.mPendingScreenSlideKeyCode = 0;
        this.mUpdateWorkThreadDisposable.dispose();
        Disposable disposable = this.mLensDirtyDetectDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        Disposable disposable2 = this.mLensDirtyDetectHintDisposable;
        if (disposable2 != null) {
            disposable2.dispose();
            Camera camera = this.mActivity;
            if (camera != null) {
                camera.hideLensDirtyDetectedHint();
            }
        }
        this.mLensDirtyDetectEnable = false;
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.unregisterPreviewMetadata();
        }
    }

    public void onPreviewLayoutChanged(Rect rect) {
    }

    @CallSuper
    public void onPreviewMetaDataUpdate(CaptureResult captureResult) {
        detectLensDirty(captureResult);
        if (this.isShowPreviewDebugInfo) {
            boolean z = true;
            if (isDoingAction() && this.mModuleIndex != 162) {
                z = false;
            }
            if (this.mModuleIndex == 166) {
                z = false;
            }
            showDebug(captureResult, z);
        }
        if (shouldCheckSatFallbackState()) {
            checkSatFallback(captureResult);
        }
    }

    public void onPreviewPixelsRead(byte[] bArr, int i, int i2) {
    }

    public void onPreviewRelease() {
        Camera camera = this.mActivity;
        if (camera != null && !camera.isActivityPaused()) {
            this.mActivity.finish();
        }
    }

    public void onPreviewSizeChanged(int i, int i2) {
    }

    @CallSuper
    public void onResume() {
        Log.d(TAG, "onResume");
        this.mPaused = false;
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(Util.KEY_KILLED_MODULE_INDEX, ModuleManager.getActiveModuleIndex());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0072, code lost:
        if (r5.mZoomRatio < 1.0f) goto L_0x0046;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onScale(float f, float f2, float f3) {
        float f4;
        StringBuilder sb = new StringBuilder();
        sb.append("onScale(): scale = ");
        sb.append(f3);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (f3 == 0.0f) {
            Log.d(str, "onScale(): scale illegal 0.0");
            return true;
        }
        if (isZoomEnabled()) {
            this.mZoomScaled += (f3 - 1.0f) / 4.0f;
            float f5 = 10.0f;
            float min = Math.min(this.mMaxZoomRatio, 10.0f);
            if (HybridZoomingSystem.IS_4_OR_MORE_SAT) {
                float f6 = this.mZoomRatio;
                if (f6 >= 1.0f) {
                    if (f6 < 5.0f) {
                        f4 = this.mMaxZoomRatio;
                        f5 = HybridZoomingSystem.getUltraTeleMinZoomRatio();
                    } else if (f6 < 10.0f) {
                        f4 = this.mMaxZoomRatio;
                    } else {
                        f4 = this.mMaxZoomRatio;
                        f5 = 30.0f;
                    }
                    min = Math.min(f4, f5);
                    float f7 = min * this.mZoomScaled;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("onScale(): delta = ");
                    sb3.append(f7);
                    sb3.append(", mZoomRatio = ");
                    sb3.append(this.mZoomRatio);
                    Log.d(str, sb3.toString());
                    if (Math.abs(f7) >= 0.01f && onZoomingActionUpdate(this.mZoomRatio + f7, 2)) {
                        this.mZoomScaled = 0.0f;
                        return true;
                    }
                }
            } else {
                if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                }
                float f72 = min * this.mZoomScaled;
                StringBuilder sb32 = new StringBuilder();
                sb32.append("onScale(): delta = ");
                sb32.append(f72);
                sb32.append(", mZoomRatio = ");
                sb32.append(this.mZoomRatio);
                Log.d(str, sb32.toString());
                this.mZoomScaled = 0.0f;
                return true;
            }
            f4 = this.mMaxZoomRatio;
            f5 = HybridZoomingSystem.getTeleMinZoomRatio();
            min = Math.min(f4, f5);
            float f722 = min * this.mZoomScaled;
            StringBuilder sb322 = new StringBuilder();
            sb322.append("onScale(): delta = ");
            sb322.append(f722);
            sb322.append(", mZoomRatio = ");
            sb322.append(this.mZoomRatio);
            Log.d(str, sb322.toString());
            this.mZoomScaled = 0.0f;
            return true;
        }
        return false;
    }

    public boolean onScaleBegin(float f, float f2) {
        this.mZoomScaled = 0.0f;
        updateSATZooming(true);
        onZoomingActionStart(2);
        return true;
    }

    public void onScaleEnd() {
        Log.d(TAG, "onScaleEnd()");
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null && !dualController.isZoomPanelVisible()) {
            updateSATZooming(false);
        }
        onZoomingActionEnd(2);
    }

    public void onSharedPreferenceChanged() {
    }

    public void onShineChanged(int i) {
    }

    public void onSingleTapUp(int i, int i2, boolean z) {
    }

    public void onStop() {
    }

    public boolean onSurfaceTexturePending(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
        return false;
    }

    public void onSurfaceTextureReleased() {
    }

    public void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
    }

    public void onSwitchLens(boolean z, boolean z2) {
        switchCameraLens(z, true, z2, 2);
    }

    /* access modifiers changed from: protected */
    public void onThermalConstrained() {
    }

    public void onUserInteraction() {
    }

    public void onWindowFocusChanged(boolean z) {
        if (z) {
            this.mIgnoreFocusChanged = false;
        }
    }

    public void onZoomRatioChanged(float f, int i) {
        onZoomingActionUpdate(f, i);
    }

    public void onZoomSwitchCamera() {
        if (isAlive() && CameraSettings.isSupportedOpticalZoom() && CameraSettings.isZoomByCameraSwitchingSupported()) {
            this.mActivity.getCameraScreenNail().disableSwitchAnimationOnce();
        }
    }

    public void onZoomingActionEnd(int i) {
    }

    public void onZoomingActionStart(int i) {
    }

    /* access modifiers changed from: protected */
    public boolean onZoomingActionUpdate(float f, int i) {
        if (!isDeviceAlive() || limitZoomByAIWatermark(f)) {
            return false;
        }
        String simpleName = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("onZoomingActionUpdate(): newValue = ");
        sb.append(f);
        sb.append(", minValue = ");
        sb.append(this.mMinZoomRatio);
        sb.append(", maxValue = ");
        sb.append(this.mMaxZoomRatio);
        Log.d(simpleName, sb.toString());
        float f2 = this.mZoomRatio;
        float clamp = HybridZoomingSystem.clamp(f, this.mMinZoomRatio, this.mMaxZoomRatio);
        if (f2 == clamp) {
            if (((double) Math.abs(clamp - HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR)) < 0.001d) {
                this.mZoomScaled = 0.0f;
            }
            return false;
        }
        String simpleName2 = getClass().getSimpleName();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onZoomingActionUpdate(): changed from ");
        sb2.append(f2);
        sb2.append(" to ");
        sb2.append(clamp);
        Log.d(simpleName2, sb2.toString());
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        if (mainContentProtocol != null) {
            mainContentProtocol.updateCurrentZoomRatio(clamp);
        }
        setZoomRatio(clamp);
        if (onInterceptZoomingEvent(f2, clamp, i)) {
            return false;
        }
        boolean z = f2 <= 1.0f || clamp <= 1.0f;
        if (z) {
            updatePreferenceTrampoline(11, 30, 34, 42, 20);
            if (C0124O00000oO.isSupportedOpticalZoom()) {
                updateUltraWideCapability(clamp);
            }
        }
        updatePreferenceInWorkThread(HybridZoomingSystem.IS_3_OR_MORE_SAT ? new int[]{24, 46, 47} : new int[]{24});
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (dualController != null && dualController.isButtonVisible()) {
            dualController.updateSlideAndZoomRatio(i);
        }
        if (i != 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(HybridZoomingSystem.toDecimal(this.mZoomRatio));
            sb3.append("X");
            topAlert.alertUpdateValue(1, 0, sb3.toString());
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void openCamera() {
    }

    /* access modifiers changed from: protected */
    public void openSettingActivity() {
        Intent intent = new Intent();
        intent.setClass(this.mActivity, CameraPreferenceActivity.class);
        intent.putExtra("from_where", this.mModuleIndex);
        intent.putExtra(":miui:starting_window_label", getResources().getString(R.string.pref_camera_settings_category));
        int intentType = DataRepository.dataItemGlobal().getIntentType();
        intent.putExtra(CameraPreferenceFragment.INTENT_TYPE, intentType);
        if (intentType == 2) {
            intent.putExtra(CameraPreferenceFragment.INTENT_VIDEO_QUALITY, DataRepository.dataItemGlobal().getIntentVideoQuality());
        }
        if (this.mActivity.startFromKeyguard()) {
            intent.putExtra("StartActivityWhenLocked", true);
        }
        this.mActivity.startActivity(intent);
        this.mActivity.setJumpFlag(2);
        CameraStatUtils.trackGotoSettings(this.mModuleIndex);
    }

    /* access modifiers changed from: protected */
    public void performKeyClicked(int i, String str, int i2, boolean z) {
    }

    /* access modifiers changed from: protected */
    public final void playCameraSound(int i) {
        Camera camera = this.mActivity;
        if (camera != null && !camera.isActivityPaused() && !isNeedMute() && CameraSettings.isCameraSoundOpen()) {
            this.mActivity.playCameraSound(i);
        }
    }

    public void playVideoSound(boolean z) {
    }

    public void preTransferOrientation(int i, int i2) {
        if (i == -1) {
            i = 0;
        }
        this.mOrientation = i;
        this.mOrientationCompensation = i2;
    }

    public void quickEnterAutoHibernation() {
        if (this.mIsAutoHibernationSupported) {
            Log.d(TAG, "quickEnterAutoHibernation");
            this.mHandler.removeMessages(65);
            this.mHandler.removeMessages(66);
            this.mHandler.sendEmptyMessage(65);
        }
    }

    /* access modifiers changed from: protected */
    public List registerMetaDataFunction() {
        return new ArrayList();
    }

    /* access modifiers changed from: protected */
    public List registerPartialMetaDataFunction() {
        return new ArrayList();
    }

    public void registerProtocol() {
        Log.d(TAG, "registerProtocol");
        ModeCoordinatorImpl.getInstance().attachProtocol(170, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(200, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(2560, this);
    }

    public void requestRender() {
    }

    public void resetAiSceneInDocumentModeOn() {
    }

    public void resetEvValue() {
        if (isDeviceAlive()) {
            this.mCamera2Device.setExposureCompensation(0);
            this.mCamera2Device.setAWBLock(false);
            CameraSettings.resetExposure();
            updatePreferenceInWorkThread(new int[0]);
        }
    }

    @CallSuper
    public void resetMutexModeManually() {
        this.mMutexModePicker.resetMutexMode();
    }

    /* access modifiers changed from: protected */
    public void resetScreenOn() {
        if (!this.mActivity.isActivityPaused()) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(17);
                this.mHandler.removeMessages(2);
                this.mHandler.removeMessages(52);
            }
        }
    }

    public final void restartModule() {
        if (!this.mActivity.isActivityPaused()) {
            this.mActivity.onModeSelected(StartControl.create(this.mModuleIndex).setViewConfigType(3).setNeedBlurAnimation(true).setNeedReConfigureCamera(true));
        }
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void restoreBottom() {
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
        if (backStack != null) {
            backStack.handleBackStackFromShutter();
        }
        if (baseDelegate.getActiveFragment(R.id.bottom_action) != 241) {
            baseDelegate.delegateEvent(7);
        }
    }

    /* access modifiers changed from: protected */
    public void restoreOuterAudio() {
        if (this.mAudioController == null) {
            this.mAudioController = new AudioController(this.mActivity.getApplicationContext());
        }
        this.mAudioController.restoreAudio();
    }

    /* access modifiers changed from: protected */
    public boolean retryOnceIfCameraError(Handler handler) {
        if (DataRepository.dataItemGlobal().isRetriedIfCameraError() || this.mActivity.isActivityPaused()) {
            return false;
        }
        Log.e(TAG, "onCameraException: retry1");
        DataRepository.dataItemGlobal().setRetriedIfCameraError(true);
        handler.post(new Runnable() {
            public void run() {
                BaseModule baseModule = BaseModule.this;
                baseModule.mActivity.onModeSelected(StartControl.create(baseModule.mModuleIndex).setViewConfigType(1).setNeedBlurAnimation(false).setNeedReConfigureCamera(true));
            }
        });
        return true;
    }

    /* access modifiers changed from: protected */
    public void sendOpenFailMessage() {
    }

    /* access modifiers changed from: protected */
    public void setAWBMode(String str) {
        if (isDeviceAlive()) {
            this.mCamera2Device.setAWBLock(false);
            if (str.equals("manual")) {
                if (C0124O00000oO.isMTKPlatform()) {
                    this.mCamera2Device.setAWBMode(10);
                } else {
                    this.mCamera2Device.setAWBMode(0);
                }
                this.mCamera2Device.setCustomAWB(CameraSettings.getCustomWB());
            } else {
                int parseInt = Util.parseInt(str, 1);
                boolean isSupported = Util.isSupported(parseInt, this.mCameraCapabilities.getSupportedAWBModes());
                Camera2Proxy camera2Proxy = this.mCamera2Device;
                if (isSupported) {
                    camera2Proxy.setAWBMode(parseInt);
                } else {
                    camera2Proxy.setAWBMode(1);
                }
            }
        }
    }

    public void setActivity(Camera camera) {
        this.mActivity = camera;
    }

    public void setCameraAudioRestriction(boolean z) {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null && camera2Proxy.getCameraDevice() != null && VERSION.SDK_INT >= 30) {
            this.mCamera2Device.getCameraDevice();
        }
    }

    public void setCameraId(int i) {
        this.mBogusCameraId = i;
    }

    /* access modifiers changed from: protected */
    public void setCameraState(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("setCameraState: ");
        sb.append(i);
        Log.d(TAG, sb.toString());
        this.mCameraState = i;
    }

    /* access modifiers changed from: protected */
    public void setColorEffect(String str) {
        if (isDeviceAlive()) {
            int parseInt = Util.parseInt(str, 0);
            if (Util.isSupported(parseInt, this.mCameraCapabilities.getSupportedColorEffects())) {
                StringBuilder sb = new StringBuilder();
                sb.append("colorEffect: ");
                sb.append(str);
                Log.d(TAG, sb.toString());
                this.mCamera2Device.setColorEffect(parseInt);
            }
        }
    }

    public void setDeparted() {
        Log.d(TAG, "setDeparted");
        Disposable disposable = this.mUpdateWorkThreadDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        Disposable disposable2 = this.mLensDirtyDetectDisposable;
        if (disposable2 != null) {
            disposable2.dispose();
        }
        Disposable disposable3 = this.mLensDirtyDetectHintDisposable;
        if (disposable3 != null) {
            disposable3.dispose();
            Camera camera = this.mActivity;
            if (camera != null) {
                camera.hideLensDirtyDetectedHint();
            }
        }
        this.mLensDirtyDetectEnable = false;
        this.mIsDeparted.set(true);
        this.mIsFrameAvailable.set(false);
    }

    public void setDisplayRectAndUIStyle(Rect rect, int i) {
        this.mDisplayRect = rect;
        this.mUIStyle = i;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public void setEvValue() {
        if (isAlive()) {
            int i = this.mEvState;
            if (i != 2) {
                if (i == 1 || i == 3) {
                    this.mCamera2Device.setExposureCompensation(this.mEvValue);
                    if (this.mEvState == 1) {
                        if (this.mEvValue != 0) {
                            this.mCamera2Device.setAWBLock(true);
                        }
                    }
                }
            }
            this.mCamera2Device.setAWBLock(false);
        }
    }

    /* access modifiers changed from: protected */
    public void setFlashMode(String str) {
        if (isDeviceAlive()) {
            StringBuilder sb = new StringBuilder();
            sb.append("flashMode: ");
            sb.append(str);
            Log.d(TAG, sb.toString());
            int parseInt = Util.parseInt(str, 0);
            if ((ThermalDetector.getInstance().thermalCloseFlash() && DataRepository.dataItemConfig().getComponentFlash().isHardwareSupported()) || (isFrontCamera() && this.mActivity.isScreenSlideOff())) {
                parseInt = 0;
            }
            this.mUseLegacyFlashMode = C0122O00000o.instance().OOoOoOo();
            this.mCamera2Device.setUseLegacyFlashMode(this.mUseLegacyFlashMode);
            this.mCamera2Device.setFlashMode(parseInt);
        }
    }

    /* access modifiers changed from: protected */
    public void setFocusMode(String str) {
        if (isDeviceAlive()) {
            int convertToFocusMode = AutoFocus.convertToFocusMode(str);
            if (Util.isSupported(convertToFocusMode, this.mCameraCapabilities.getSupportedFocusModes())) {
                this.mCamera2Device.setFocusMode(convertToFocusMode);
            }
        }
    }

    public void setFrameAvailable(boolean z) {
        this.mIsFrameAvailable.set(z);
    }

    public void setMaxZoomRatio(float f) {
        StringBuilder sb = new StringBuilder();
        sb.append("setMaxZoomRatio(): ");
        sb.append(f);
        Log.d(TAG, sb.toString());
        this.mMaxZoomRatio = f;
        this.mMainProtocol.updateZoomRatio(this.mMinZoomRatio, this.mMaxZoomRatio);
    }

    public void setMinZoomRatio(float f) {
        StringBuilder sb = new StringBuilder();
        sb.append("setMinZoomRatio(): ");
        sb.append(f);
        Log.d(TAG, sb.toString());
        this.mMinZoomRatio = f;
    }

    public void setModuleIndex(int i) {
        this.mModuleIndex = i;
    }

    public void setRestoring(boolean z) {
        this.mRestoring = z;
    }

    public void setThermalLevel(int i) {
        this.mThermalLevel = i;
    }

    /* access modifiers changed from: protected */
    public void setTriggerMode(int i) {
        this.mTriggerMode = i;
        StringBuilder sb = new StringBuilder();
        sb.append("setTriggerMode ");
        sb.append(i);
        Log.u(TAG, sb.toString());
    }

    /* access modifiers changed from: protected */
    public void setVideoMaxZoomRatioByTele() {
        String OO00oO0 = C0122O00000o.instance().OO00oO0();
        if (!Camera2DataContainer.getInstance().hasTeleCamera() || ((!C0122O00000o.instance().OOOo000() && !C0122O00000o.instance().OOo000()) || OO00oO0 == null || !isTeleSupportVideoQuality())) {
            setMaxZoomRatio(Math.min(6.0f, this.mCameraCapabilities.getMaxZoomRatio()));
        } else {
            setMaxZoomRatio((isUltraTeleCamera() ? HybridZoomingSystem.getUltraTeleMinZoomRatio() : HybridZoomingSystem.getTeleMinZoomRatio()) * Float.parseFloat(OO00oO0));
        }
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public void setZoomRatio(float f) {
        StringBuilder sb = new StringBuilder();
        sb.append("setZoomRatio(): ");
        sb.append(f);
        Log.d(TAG, sb.toString());
        this.mZoomRatio = f;
        CameraSettings.setRetainZoom(f, this.mModuleIndex);
    }

    /* access modifiers changed from: protected */
    public boolean shouldCheckSatFallbackState() {
        return false;
    }

    public boolean shouldReleaseLater() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void showAutoHibernationTip() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertAutoHibernationDescTip(FragmentTopAlert.TIP_AUTO_HIBERNATION_DESC, 0, R.string.auto_hibernation_enter_tip, 5000);
        }
    }

    /* access modifiers changed from: protected */
    public void showConfirmMessage(int i, int i2) {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            if (this.mTitleId != i && this.mMessageId != i2) {
                this.mDialog.dismiss();
            } else {
                return;
            }
        }
        this.mTitleId = i;
        this.mMessageId = i2;
        Camera camera = this.mActivity;
        this.mDialog = RotateDialogController.showSystemAlertDialog(camera, camera.getString(i), this.mActivity.getString(i2), this.mActivity.getString(17039370), null, null, null, null, null);
    }

    public void showLensDirtyTip() {
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities == null || cameraCapabilities.getMiAlgoASDVersion() < 3.0f) {
            Disposable disposable = this.mLensDirtyDetectDisposable;
            if (disposable != null) {
                disposable.dispose();
                this.mLensDirtyDetectDisposable = null;
            } else {
                return;
            }
        } else if (this.mLensDirtyDetectEnable) {
            this.mLensDirtyDetectEnable = false;
        } else {
            return;
        }
        Completable.complete().observeOn(CameraSchedulers.sCameraSetupScheduler).doOnComplete(new ActionUpdateLensDirtyDetect(this, false)).subscribe();
        if (CameraSettings.shouldShowLensDirtyDetectHint()) {
            this.mActivity.showLensDirtyDetectedHint();
            this.mLensDirtyDetectHintDisposable = Completable.complete().delay(8000, TimeUnit.MILLISECONDS, CameraSchedulers.sCameraSetupScheduler).doOnComplete(new ActionHideLensDirtyDetectHint(this)).subscribe();
        }
    }

    public void showMoreMode(boolean z) {
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        boolean isMoreModeShowing = isMoreModeShowing(z);
        StringBuilder sb = new StringBuilder();
        sb.append("showMore isShowing = ");
        sb.append(isMoreModeShowing);
        sb.append(",configChanges = ");
        sb.append(configChanges);
        Log.d(TAG, sb.toString());
        if (configChanges != null && !isMoreModeShowing(z)) {
            configChanges.configModeMore(z);
        }
    }

    public void showOrHideChip(boolean z) {
    }

    public void showQRCodeResult() {
    }

    /* access modifiers changed from: protected */
    public void silenceOuterAudio() {
        if (this.mAudioController == null) {
            this.mAudioController = new AudioController(this.mActivity.getApplicationContext());
        }
        this.mAudioController.silenceAudio();
    }

    /* access modifiers changed from: protected */
    public void switchCameraLens(boolean z, boolean z2, boolean z3, int i) {
        DataItemGlobal dataItemGlobal = (DataItemGlobal) DataRepository.provider().dataGlobal();
        int currentMode = dataItemGlobal.getCurrentMode();
        dataItemGlobal.setCameraId(0);
        this.mActivity.onModeSelected(StartControl.create(currentMode).setStartDelay(0).setResetType(z3 ? 7 : 5).setViewConfigType(i).setNeedBlurAnimation(z2));
    }

    public final void thermalConstrained() {
        if (!this.mActivity.isActivityPaused()) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new C0388O000oOOO(this));
            }
        }
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("@");
        sb.append(hashCode());
        sb.append(": mid = ");
        sb.append(this.mModuleIndex);
        sb.append(", cid = ");
        sb.append(this.mActualCameraId);
        sb.append(", created = ");
        sb.append(isCreated());
        sb.append(", departed = ");
        sb.append(isDeparted());
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void trackGeneralInfo(Map map, int i, boolean z, BeautyValues beautyValues, boolean z2, int i2) {
        Map hashMap = map == null ? new HashMap() : map;
        hashMap.put(Manual.PARAM_EV, Integer.valueOf(this.mEvValueForTrack));
        hashMap.put(BaseEvent.COUNT, String.valueOf(i));
        hashMap.put(AlgoAttr.PARAM_AI_SCENE, Integer.valueOf(i2));
        CameraStatUtils.trackGeneralInfo(hashMap, z, z2, this.mModuleIndex, getTriggerMode(), isFrontCamera(), getActualCameraId(), beautyValues, this.mMutexModePicker, this.mFlashAutoModeState);
    }

    /* access modifiers changed from: protected */
    public void trackModeCustomInfo(Map map, boolean z, BeautyValues beautyValues, int i, boolean z2) {
    }

    /* access modifiers changed from: protected */
    public void trackPictureTaken(PictureTakenParameter pictureTakenParameter) {
        if (pictureTakenParameter != null) {
            CameraStatUtils.trackMacroModeTaken(this.mModuleIndex);
            HashMap hashMap = new HashMap();
            if (!DataRepository.dataItemGlobal().isIntentIDPhoto()) {
                hashMap.put(BaseEvent.PARAM_PHOTO_RATIO_MOVIE, CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex) ? "on" : "off");
            }
            int i = this.mModuleIndex;
            String str = BeautyAttr.PARAM_BEAUTY_LEVEL;
            if (i == 163) {
                if (DataRepository.dataItemGlobal().isIntentIDPhoto()) {
                    BeautyValues beautyValues = pictureTakenParameter.beautyValues;
                    if (beautyValues != null) {
                        hashMap.put(str, Integer.valueOf(beautyValues.mBeautySkinSmooth));
                    }
                    CameraStatUtils.trackIdPhoto(hashMap);
                } else {
                    CameraStatUtils.trackMoonMode(hashMap, pictureTakenParameter.isEnteringMoon, pictureTakenParameter.isSelectMoonMode);
                    CameraStatUtils.trackSuperNightInCaptureMode(hashMap, pictureTakenParameter.isSuperNightInCaptureMode);
                }
            } else if (i == 171) {
                BeautyValues beautyValues2 = pictureTakenParameter.beautyValues;
                if (beautyValues2 != null) {
                    hashMap.put(str, Integer.valueOf(beautyValues2.mBeautySkinSmooth));
                }
                CameraStatUtils.trackCapturePortrait(hashMap);
            } else if (i == 173) {
                CameraStatUtils.trackCaptureSuperNight(hashMap);
            }
            trackModeCustomInfo(hashMap, pictureTakenParameter.burst, pictureTakenParameter.beautyValues, pictureTakenParameter.takenNum, pictureTakenParameter.isNearRangeMode);
        }
    }

    public void tryRemoveCountDownMessage() {
    }

    public void unRegisterModulePersistProtocol() {
        Log.d(TAG, "unRegisterModulePersist");
    }

    public void unRegisterProtocol() {
        Log.d(TAG, "unRegisterProtocol");
        ModeCoordinatorImpl.getInstance().detachProtocol(170, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(200, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(2560, this);
    }

    /* access modifiers changed from: protected */
    public void updateAntiBanding(String str) {
        if (isDeviceAlive()) {
            int parseInt = Util.parseInt(str, 3);
            if (Util.isSupported(parseInt, this.mCameraCapabilities.getSupportedAntiBandingModes())) {
                StringBuilder sb = new StringBuilder();
                sb.append("antiBanding: ");
                sb.append(str);
                Log.d(TAG, sb.toString());
                this.mCamera2Device.setAntiBanding(parseInt);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateAutoHibernation() {
        SettingUiState autoHibernationSettingNeedRemove = CameraSettings.getAutoHibernationSettingNeedRemove(this.mModuleIndex, isFrontCamera());
        boolean z = !autoHibernationSettingNeedRemove.isRomove && !autoHibernationSettingNeedRemove.isMutexEnable && CameraSettings.isAutoHibernationOn();
        this.mIsAutoHibernationSupported = z;
    }

    /* access modifiers changed from: protected */
    public void updateAutoHibernationFirstRecordingTime() {
    }

    /* access modifiers changed from: protected */
    public void updateBackSoftLightPreference() {
        setFlashMode("0");
    }

    /* access modifiers changed from: protected */
    public void updateCameraScreenNailSize(int i, int i2) {
        String simpleName = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("updateCameraScreenNailSize: ");
        sb.append(i);
        sb.append("x");
        sb.append(i2);
        Log.d(simpleName, sb.toString());
        this.mActivity.getCameraScreenNail().setPreviewSize(i, i2);
        this.mMainProtocol.setPreviewSize(i, i2);
    }

    /* access modifiers changed from: protected */
    public final void updateExposureMeteringMode() {
        if (isDeviceAlive()) {
            this.mCamera2Device.setExposureMeteringMode(CameraSettings.getExposureMeteringMode());
        }
    }

    /* access modifiers changed from: protected */
    public void updateFlashPreference() {
    }

    /* access modifiers changed from: protected */
    public void updateHDRPreference() {
    }

    public void updateLensDirtyDetect(boolean z) {
        if (this.mCamera2Device == null) {
            Log.e(TAG, "updateLensDirtyDetect: mCamera2Device is null...");
            return;
        }
        boolean isLensDirtyDetectEnabled = CameraSettings.isLensDirtyDetectEnabled();
        if (!isLensDirtyDetectEnabled) {
            Disposable disposable = this.mLensDirtyDetectDisposable;
            if (disposable != null) {
                disposable.dispose();
                this.mLensDirtyDetectDisposable = null;
            }
        }
        this.mCamera2Device.setLensDirtyDetect(isLensDirtyDetectEnabled);
        if (z && isFrameAvailable() && !isDoingAction() && !isRecording()) {
            this.mCamera2Device.resumePreview();
        }
    }

    /* access modifiers changed from: protected */
    public void updateModuleRelated() {
        this.mCamera2Device.setModuleParameter(this.mModuleIndex, this.mBogusCameraId);
    }

    @WorkerThread
    public final void updatePreferenceInWorkThread(@UpdateType int... iArr) {
        Disposable disposable = this.mUpdateWorkThreadDisposable;
        String str = TAG;
        if (disposable == null || disposable.isDisposed()) {
            StringBuilder sb = new StringBuilder();
            sb.append("the mUpdateWorkThreadDisposable is not available.");
            sb.append(this.mUpdateWorkThreadDisposable);
            sb.append(". ");
            sb.append(this);
            Log.d(str, sb.toString());
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("types:");
        sb2.append(join(iArr));
        sb2.append(", ");
        sb2.append(this);
        Log.e(str, sb2.toString());
        this.mUpdateWorkThreadEmitter.onNext(iArr);
    }

    public final void updatePreferenceTrampoline(@UpdateType int... iArr) {
        consumePreference(iArr);
    }

    public void updatePreviewSurface() {
    }

    public void updateSATZooming(boolean z) {
    }

    public void updateScreenSlide(boolean z) {
        if (z) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.setAELock(false);
                int i = this.mModuleIndex;
                int[] iArr = (i == 163 || i == 171 || i == 165 || i == 188) ? new int[]{10, 36} : new int[]{10};
                updatePreferenceInWorkThread(iArr);
            }
        }
    }

    public void updateThermalLevel() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setThermalLevel(this.mThermalLevel);
        }
    }

    /* access modifiers changed from: protected */
    public void updateTipMessage(int i, @StringRes int i2, int i3) {
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.showTips(i, i2, i3);
        }
    }

    /* access modifiers changed from: protected */
    public void updateZoomRatioToggleButtonState(boolean z) {
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null) {
            dualController.setRecordingOrPausing(z);
            if (z) {
                dualController.hideZoomButton();
            } else {
                MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                if (miBeautyProtocol == null || !miBeautyProtocol.isBeautyPanelShow()) {
                    dualController.showZoomButton();
                }
            }
        }
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert == null) {
            return;
        }
        if (z) {
            topAlert.alertUpdateValue(0, 0, null);
        } else if (!CameraSettings.isMacroModeEnabled(this.mModuleIndex)) {
            topAlert.clearAlertStatus();
        }
    }

    public boolean zoomIn(float f) {
        if (f <= 0.0f) {
            return false;
        }
        return addZoom(f);
    }

    public boolean zoomOut(float f) {
        if (f <= 0.0f) {
            return false;
        }
        return addZoom(-f);
    }
}
