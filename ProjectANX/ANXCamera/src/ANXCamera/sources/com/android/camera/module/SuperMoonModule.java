package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.SensorEvent;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.location.Location;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Size;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import androidx.annotation.NonNull;
import com.android.camera.Camera;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraIntentManager.CameraExtras;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.EncodingQuality;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.LocalParallelService.LocalBinder;
import com.android.camera.LocalParallelService.ServiceStatusListener;
import com.android.camera.LocationManager;
import com.android.camera.PictureSizeManager;
import com.android.camera.R;
import com.android.camera.SensorStateManager.SensorStateListener;
import com.android.camera.Util;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.constant.AutoFocus;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentRunningUltraPixel;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.CameraClickObservableImpl;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.module.loader.camera2.FocusManager2.Listener;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CameraClickObservable;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.MagneticSensorDetect;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.TopConfigProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsWrapper.PictureTakenParameter;
import com.android.camera.statistic.ScenarioTrackUtil;
import com.android.camera.storage.ImageSaver;
import com.android.camera.storage.Storage;
import com.android.camera.ui.zoom.ZoomingAction;
import com.android.camera.zoommap.ZoomMapController;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraMetaDataCallback;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CaptureResultParser;
import com.android.gallery3d.ui.GLCanvas;
import com.android.zxing.PreviewDecodeManager;
import com.xiaomi.camera.base.CameraDeviceUtil;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.ParallelDataZipper;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.ParallelTaskDataParameter.Builder;
import com.xiaomi.camera.core.PictureInfo;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.engine.BufferFormat;
import com.xiaomi.engine.GraphDescriptorBean;
import com.xiaomi.stat.d;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SuperMoonModule extends BaseModule implements Listener, CameraAction, TopConfigProtocol, FocusCallback, PictureCallback, CameraMetaDataCallback, CameraPreviewCallback {
    private static final long CAPTURE_DURATION_THRESHOLD = 12000;
    /* access modifiers changed from: private */
    public static final String TAG = "SuperMoonModule";
    /* access modifiers changed from: private */
    public float[] curGyroscope;
    /* access modifiers changed from: private */
    public float[] lastGyroscope;
    private boolean m3ALocked;
    private int mAFEndLogTimes;
    private String mAlgorithmName;
    private float[] mApertures;
    private boolean mBlockQuickShot = (!CameraSettings.isCameraQuickShotEnable());
    private Intent mBroadcastIntent;
    private final Object mCameraDeviceLock = new Object();
    private long mCaptureStartTime;
    private boolean mConfigRawStream;
    /* access modifiers changed from: private */
    public Disposable mCountdownDisposable;
    private int mCurrentAiScene = -1;
    private boolean mEnableParallelSession;
    private boolean mEnableShot2Gallery;
    private boolean mEnabledPreviewThumbnail;
    private boolean mFirstCreateCapture;
    private float[] mFocalLengths;
    /* access modifiers changed from: private */
    public FocusManager2 mFocusManager;
    /* access modifiers changed from: private */
    public int mIsShowLyingDirectHintStatus = -1;
    /* access modifiers changed from: private */
    public boolean mIsStartCount;
    private int mJpegRotation;
    private long mLastCaptureTime;
    private Location mLocation;
    private boolean mLongPressedAutoFocus;
    private WatermarkItem mMajorItem;
    private WatermarkItem mMinorItem;
    /* access modifiers changed from: private */
    public boolean mMultiSnapStatus = false;
    /* access modifiers changed from: private */
    public long mOnResumeTime;
    private int mOperatingMode;
    private boolean mParallelSessionConfigured = false;
    private final Object mParallelSessionLock = new Object();
    private boolean mQuickShotAnimateEnable = false;
    private SensorStateListener mSensorStateListener = new SensorStateListener() {
        private TopAlert mTopAlert;

        public boolean isWorking() {
            return SuperMoonModule.this.isAlive() && SuperMoonModule.this.getCameraState() != 0;
        }

        public void notifyDevicePostureChanged() {
        }

        public void onDeviceBecomeStable() {
        }

        public void onDeviceBeginMoving() {
            if (!SuperMoonModule.this.mPaused && CameraSettings.isEdgePhotoEnable()) {
                SuperMoonModule.this.mActivity.getEdgeShutterView().onDeviceMoving();
            }
        }

        public void onDeviceKeepMoving(double d) {
            if (!SuperMoonModule.this.mPaused && SuperMoonModule.this.mFocusManager != null && !SuperMoonModule.this.mMultiSnapStatus && !SuperMoonModule.this.is3ALocked() && !SuperMoonModule.this.mMainProtocol.isEvAdjusted(true)) {
                SuperMoonModule.this.mFocusManager.onDeviceKeepMoving(d);
            }
        }

        public void onDeviceKeepStable() {
        }

        public void onDeviceLieChanged(boolean z) {
            Handler handler;
            Message message;
            long j;
            if (!SuperMoonModule.this.mPaused) {
                int access$800 = SuperMoonModule.this.mIsShowLyingDirectHintStatus;
                SuperMoonModule superMoonModule = SuperMoonModule.this;
                int i = superMoonModule.mOrientationCompensation;
                if (access$800 != (z ? 1 : 0) + i) {
                    superMoonModule.mIsShowLyingDirectHintStatus = i + z;
                    SuperMoonModule.this.mHandler.removeMessages(58);
                    if (this.mTopAlert == null) {
                        this.mTopAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    }
                    TopAlert topAlert = this.mTopAlert;
                    if (topAlert != null ? topAlert.isContainAlertRecommendTip(R.string.dirty_tip_toast, R.string.pic_flaw_blink_one, R.string.pic_flaw_blink_more, R.string.pic_flaw_cover) : false) {
                        z = false;
                    }
                    if (z) {
                        SuperMoonModule superMoonModule2 = SuperMoonModule.this;
                        Handler handler2 = superMoonModule2.mHandler;
                        handler2.sendMessageDelayed(handler2.obtainMessage(58, 1, superMoonModule2.mOrientationCompensation), 400);
                        SuperMoonModule superMoonModule3 = SuperMoonModule.this;
                        handler = superMoonModule3.mHandler;
                        message = handler.obtainMessage(58, 0, superMoonModule3.mOrientationCompensation);
                        j = 5000;
                    } else {
                        SuperMoonModule superMoonModule4 = SuperMoonModule.this;
                        handler = superMoonModule4.mHandler;
                        message = handler.obtainMessage(58, 0, superMoonModule4.mOrientationCompensation);
                        j = 500;
                    }
                    handler.sendMessageDelayed(message, j);
                }
            }
        }

        public void onDeviceOrientationChanged(float f, boolean z) {
            SuperMoonModule superMoonModule = SuperMoonModule.this;
            superMoonModule.mDeviceRotation = !z ? f : (float) superMoonModule.mOrientation;
            if (SuperMoonModule.this.getCameraState() != 3 || SuperMoonModule.this.isGradienterOn) {
                EffectController instance = EffectController.getInstance();
                SuperMoonModule superMoonModule2 = SuperMoonModule.this;
                instance.setDeviceRotation(z, Util.getShootRotation(superMoonModule2.mActivity, superMoonModule2.mDeviceRotation));
            }
            SuperMoonModule.this.mHandler.removeMessages(33);
            if (!SuperMoonModule.this.mPaused && !z && f != -1.0f) {
                int roundOrientation = Util.roundOrientation(Math.round(f), SuperMoonModule.this.mOrientation);
                SuperMoonModule.this.mHandler.obtainMessage(33, roundOrientation, (Util.getDisplayRotation(SuperMoonModule.this.mActivity) + roundOrientation) % m.cQ).sendToTarget();
            }
        }

        public void onDeviceRotationChanged(float[] fArr) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            int type = sensorEvent.sensor.getType();
            if (type == 4) {
                SuperMoonModule superMoonModule = SuperMoonModule.this;
                superMoonModule.lastGyroscope = superMoonModule.curGyroscope;
                SuperMoonModule.this.curGyroscope = sensorEvent.values;
            } else if (type == 14) {
                MagneticSensorDetect magneticSensorDetect = (MagneticSensorDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(2576);
                if (magneticSensorDetect != null) {
                    magneticSensorDetect.onMagneticSensorChanged(sensorEvent);
                }
            }
        }
    };
    private ServiceStatusListener mServiceStatusListener;
    private int mShootOrientation;
    private float mShootRotation;
    private long mShutterCallbackTime;
    private long mShutterLag;
    private volatile boolean mUltraWideAELocked;
    private boolean mVolumeLongPress = false;
    /* access modifiers changed from: private */
    public volatile boolean mWaitSaveFinish;
    private ZoomMapController mZoomMapController;

    class LocalParallelServiceStatusListener implements ServiceStatusListener {
        private final WeakReference mCameraDevice;
        private final WeakReference mSuperMoonModuleRef;

        LocalParallelServiceStatusListener(Camera2Proxy camera2Proxy, SuperMoonModule superMoonModule) {
            this.mCameraDevice = new WeakReference(camera2Proxy);
            this.mSuperMoonModuleRef = new WeakReference(superMoonModule);
        }

        public void onImagePostProcessEnd(ParallelTaskData parallelTaskData) {
            SuperMoonModule superMoonModule = (SuperMoonModule) this.mSuperMoonModuleRef.get();
            if (superMoonModule != null && parallelTaskData != null && parallelTaskData.isJpegDataReady() && DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode() && !C0122O00000o.instance().OOoOOO0()) {
                superMoonModule.onPictureTakenFinished(true);
            }
        }

        public void onImagePostProcessStart(ParallelTaskData parallelTaskData) {
            SuperMoonModule superMoonModule = (SuperMoonModule) this.mSuperMoonModuleRef.get();
            if (superMoonModule != null && 4 != parallelTaskData.getAlgoType()) {
                if (!DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode() || C0122O00000o.instance().OOoOOO0()) {
                    superMoonModule.onPictureTakenFinished(true);
                }
                PerformanceTracker.trackPictureCapture(1);
                Camera2Proxy camera2Proxy = (Camera2Proxy) this.mCameraDevice.get();
                if (camera2Proxy != null) {
                    camera2Proxy.onParallelImagePostProcStart();
                }
            }
        }
    }

    class MainHandler extends Handler {
        private WeakReference mModule;

        public MainHandler(SuperMoonModule superMoonModule, Looper looper) {
            super(looper);
            this.mModule = new WeakReference(superMoonModule);
        }

        /* JADX WARNING: Removed duplicated region for block: B:62:0x0108  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(Message message) {
            SuperMoonModule superMoonModule = (SuperMoonModule) this.mModule.get();
            if (superMoonModule != null) {
                if (!superMoonModule.isCreated()) {
                    removeCallbacksAndMessages(null);
                } else if (superMoonModule.getActivity() != null) {
                    int i = message.what;
                    if (i == 2) {
                        superMoonModule.getWindow().clearFlags(128);
                    } else if (i == 4) {
                        superMoonModule.checkActivityOrientation();
                        if (SystemClock.uptimeMillis() - superMoonModule.mOnResumeTime < 5000) {
                            sendEmptyMessageDelayed(4, 100);
                        }
                    } else if (i == 17) {
                        removeMessages(17);
                        removeMessages(2);
                        superMoonModule.getWindow().addFlags(128);
                        sendEmptyMessageDelayed(2, (long) superMoonModule.getScreenDelay());
                    } else if (i == 31) {
                        superMoonModule.setOrientationParameter();
                    } else if (i == 33) {
                        superMoonModule.setOrientation(message.arg1, message.arg2);
                    } else if (i == 44) {
                        superMoonModule.restartModule();
                    } else if (i != 45) {
                        switch (i) {
                            case 9:
                                superMoonModule.mMainProtocol.initializeFocusView(superMoonModule);
                                break;
                            case 10:
                                if (!superMoonModule.mActivity.isActivityPaused()) {
                                }
                                break;
                            case 11:
                                break;
                            default:
                                switch (i) {
                                    case 50:
                                        Log.w(SuperMoonModule.TAG, "Oops, capture timeout later release timeout!");
                                        superMoonModule.onPictureTakenFinished(false);
                                        break;
                                    case 51:
                                        break;
                                    case 52:
                                        superMoonModule.onShutterButtonClick(superMoonModule.getTriggerMode());
                                        break;
                                    default:
                                        switch (i) {
                                            case 56:
                                                MainContentProtocol mainContentProtocol = superMoonModule.mMainProtocol;
                                                if (mainContentProtocol != null && mainContentProtocol.isFaceExists(1) && superMoonModule.mMainProtocol.isFocusViewVisible()) {
                                                    Camera2Proxy camera2Proxy = superMoonModule.mCamera2Device;
                                                    if (camera2Proxy != null && 4 == camera2Proxy.getFocusMode()) {
                                                        superMoonModule.mMainProtocol.clearFocusView(7);
                                                        break;
                                                    }
                                                }
                                            case 57:
                                                PreviewDecodeManager.getInstance().reset();
                                                break;
                                            case 58:
                                                ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                                                if (configChanges != null) {
                                                    int i2 = superMoonModule.mOrientationCompensation;
                                                    configChanges.configRotationChange(message.arg1, (360 - (i2 >= 0 ? i2 % m.cQ : (i2 % m.cQ) + m.cQ)) % m.cQ);
                                                    break;
                                                }
                                                break;
                                            default:
                                                switch (i) {
                                                    case 60:
                                                        Log.d(SuperMoonModule.TAG, "fallback timeout");
                                                        superMoonModule.mIsSatFallback = 0;
                                                        superMoonModule.mFallbackProcessed = false;
                                                        superMoonModule.mLastSatFallbackRequestId = -1;
                                                        if (superMoonModule.mWaitingSnapshot && superMoonModule.getCameraState() == 1) {
                                                            superMoonModule.mWaitingSnapshot = false;
                                                            sendEmptyMessage(62);
                                                            break;
                                                        }
                                                    case 61:
                                                        Log.d(SuperMoonModule.TAG, "wait save finish timeout");
                                                        superMoonModule.mWaitSaveFinish = false;
                                                        break;
                                                    case 62:
                                                        superMoonModule.onWaitingFocusFinished();
                                                        break;
                                                    default:
                                                        StringBuilder sb = new StringBuilder();
                                                        sb.append("no consumer for this message: ");
                                                        sb.append(message.what);
                                                        throw new RuntimeException(sb.toString());
                                                }
                                        }
                                        break;
                                }
                                if (!superMoonModule.mActivity.isActivityPaused()) {
                                    superMoonModule.mOpenCameraFail = true;
                                    superMoonModule.onCameraException();
                                    break;
                                }
                                break;
                        }
                    } else {
                        superMoonModule.setActivity(null);
                    }
                }
            }
        }
    }

    private void beginParallelProcess(ParallelTaskData parallelTaskData, boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("algo begin: ");
        sb.append(parallelTaskData.getSavePath());
        sb.append(" | ");
        sb.append(Thread.currentThread().getName());
        Log.i(str, sb.toString());
        if (this.mServiceStatusListener == null) {
            this.mServiceStatusListener = new LocalParallelServiceStatusListener(this.mCamera2Device, this);
            AlgoConnector.getInstance().setServiceStatusListener(this.mServiceStatusListener);
        }
    }

    private void blockSnapClickUntilSaveFinish() {
        Log.i(TAG, "blockSnapClickUntilFinish");
        this.mWaitSaveFinish = true;
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessageDelayed(61, 5000);
        }
    }

    private boolean checkShutterCondition() {
        if (isBlockSnap() || isIgnoreTouchEvent()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("checkShutterCondition: blockSnap=");
            sb.append(isBlockSnap());
            sb.append(" ignoreTouchEvent=");
            sb.append(isIgnoreTouchEvent());
            Log.w(str, sb.toString());
            return false;
        } else if (Storage.isLowStorageAtLastPoint()) {
            Log.w(TAG, "checkShutterCondition: low storage");
            return false;
        } else if (!isFrontCamera() || !this.mActivity.isScreenSlideOff()) {
            if (isIn3OrMoreSatMode()) {
                DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                if (dualController != null && !dualController.isZoomSliderViewIdle()) {
                    Log.w(TAG, "checkShutterCondition: 3SAT zooming");
                    return false;
                }
            }
            BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
            if (backStack != null) {
                backStack.handleBackStackFromShutter();
            }
            return true;
        } else {
            Log.w(TAG, "checkShutterCondition: screen is slide off");
            return false;
        }
    }

    private int clampQuality(int i) {
        return DataRepository.dataItemRunning().getComponentUltraPixel().isRear108MPSwitchOn() ? Util.clamp(i, 0, 90) : i;
    }

    /* access modifiers changed from: private */
    public void configParallelSession() {
        GraphDescriptorBean graphDescriptorBean;
        int cameraCombinationMode = CameraDeviceUtil.getCameraCombinationMode(Camera2DataContainer.getInstance().getRoleIdByActualId(this.mActualCameraId));
        if (isPortraitMode()) {
            int i = ((!isDualFrontCamera() || C0122O00000o.instance().OO0OOO()) && !isDualCamera() && !isBokehUltraWideBackCamera()) ? 1 : 2;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configParallelSession: inputStreamNum = ");
            sb.append(i);
            Log.d(str, sb.toString());
            graphDescriptorBean = new GraphDescriptorBean(32770, i, true, cameraCombinationMode);
        } else if (this.mModuleIndex == 167) {
            graphDescriptorBean = new GraphDescriptorBean(32771, 1, true, cameraCombinationMode);
        } else if (DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode()) {
            graphDescriptorBean = new GraphDescriptorBean(32778, 1, true, cameraCombinationMode);
        } else {
            if (cameraCombinationMode == 0) {
                cameraCombinationMode = 513;
            }
            graphDescriptorBean = new GraphDescriptorBean(0, 1, true, cameraCombinationMode);
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("configParallelSession: pictureSize = ");
        sb2.append(this.mPictureSize);
        Log.d(str2, sb2.toString());
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("configParallelSession: outputSize = ");
        sb3.append(this.mOutputPictureSize);
        Log.d(str3, sb3.toString());
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("configParallelSession: outputFormat = ");
        sb4.append(this.mOutputPictureFormat);
        Log.d(str4, sb4.toString());
        CameraSize cameraSize = this.mPictureSize;
        BufferFormat bufferFormat = new BufferFormat(cameraSize.width, cameraSize.height, 35, graphDescriptorBean);
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder(true);
        localBinder.configCaptureSession(bufferFormat, null);
        localBinder.setImageSaver(this.mActivity.getImageSaver());
        CameraSize cameraSize2 = this.mOutputPictureSize;
        localBinder.setOutputPictureSpec(cameraSize2.width, cameraSize2.height, this.mOutputPictureFormat);
        localBinder.setSRRequireReprocess(C0122O00000o.instance().isSRRequireReprocess());
        synchronized (this.mParallelSessionLock) {
            this.mParallelSessionConfigured = true;
        }
    }

    private boolean enablePreviewAsThumbnail() {
        return this.mEnableParallelSession;
    }

    private int getCountDownTimes(int i) {
        Intent intent = this.mBroadcastIntent;
        int timerDurationSeconds = (intent != null ? CameraIntentManager.getInstance(intent) : this.mActivity.getCameraIntentManager()).getTimerDurationSeconds();
        if (timerDurationSeconds != -1) {
            Intent intent2 = this.mBroadcastIntent;
            String str = CameraExtras.TIMER_DURATION_SECONDS;
            if (intent2 != null) {
                intent2.removeExtra(str);
            } else {
                this.mActivity.getIntent().removeExtra(str);
            }
            if (timerDurationSeconds != 0) {
                return timerDurationSeconds != 5 ? 3 : 5;
            }
            return 0;
        } else if (i != 100 || !CameraSettings.isHandGestureOpen()) {
            return CameraSettings.getCountDownTimes();
        } else {
            int countDownTimes = CameraSettings.getCountDownTimes();
            if (countDownTimes == 0) {
                countDownTimes = 3;
            }
            return countDownTimes;
        }
    }

    private DeviceWatermarkParam getDeviceWaterMarkParam() {
        float f;
        float f2;
        float f3;
        float resourceFloat;
        float resourceFloat2;
        int i;
        boolean isDualCameraWaterMarkOpen = CameraSettings.isDualCameraWaterMarkOpen();
        boolean isFrontCameraWaterMarkOpen = CameraSettings.isFrontCameraWaterMarkOpen();
        if (isDualCameraWaterMarkOpen) {
            resourceFloat = CameraSettings.getResourceFloat(R.dimen.dualcamera_watermark_size_ratio, 0.0f);
            resourceFloat2 = CameraSettings.getResourceFloat(R.dimen.dualcamera_watermark_padding_x_ratio, 0.0f);
            i = R.dimen.dualcamera_watermark_padding_y_ratio;
        } else if (isFrontCameraWaterMarkOpen) {
            resourceFloat = CameraSettings.getResourceFloat(R.dimen.global_frontcamera_watermark_size_ratio, 0.0f);
            if (!Util.isGlobalVersion() || resourceFloat == 0.0f) {
                resourceFloat = CameraSettings.getResourceFloat(R.dimen.frontcamera_watermark_size_ratio, 0.0f);
            }
            resourceFloat2 = CameraSettings.getResourceFloat(R.dimen.frontcamera_watermark_padding_x_ratio, 0.0f);
            i = R.dimen.frontcamera_watermark_padding_y_ratio;
        } else {
            f3 = 0.0f;
            f2 = 0.0f;
            f = 0.0f;
            DeviceWatermarkParam deviceWatermarkParam = new DeviceWatermarkParam(isDualCameraWaterMarkOpen, isFrontCameraWaterMarkOpen, CameraSettings.isUltraPixelRearOn(), CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex), CameraSettings.getDualCameraWaterMarkFilePathVendor(), f3, f2, f);
            return deviceWatermarkParam;
        }
        f = CameraSettings.getResourceFloat(i, 0.0f);
        f3 = resourceFloat;
        f2 = resourceFloat2;
        DeviceWatermarkParam deviceWatermarkParam2 = new DeviceWatermarkParam(isDualCameraWaterMarkOpen, isFrontCameraWaterMarkOpen, CameraSettings.isUltraPixelRearOn(), CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex), CameraSettings.getDualCameraWaterMarkFilePathVendor(), f3, f2, f);
        return deviceWatermarkParam2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x012a  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x013d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private PictureInfo getPictureInfo() {
        float[] fArr;
        float[] fArr2;
        String retriveFaceInfo;
        Camera2Proxy camera2Proxy;
        StringBuilder sb;
        String str;
        String sb2;
        PictureInfo opMode = new PictureInfo().setFrontMirror(isFrontMirror()).setSensorType(isFrontCamera()).setBokehFrontCamera(isPictureUseDualFrontCamera()).setHdrType(DataRepository.dataItemConfig().getComponentHdr().getComponentValue(this.mModuleIndex)).setOpMode(getOperatingMode());
        opMode.setAiEnabled(false);
        opMode.setAiType(0);
        int i = this.mModuleIndex;
        if (i == 166) {
            opMode.setPanorama(true);
        } else if (i == 167) {
            opMode.setProfession(true);
        }
        opMode.setShotBurst(this.mMultiSnapStatus);
        opMode.setFilter(CameraSettings.getShaderEffect());
        CameraSettings.getCameraLensType(this.mModuleIndex);
        if (isFrontCamera()) {
            sb2 = "front";
        } else {
            int actualCameraId = getActualCameraId();
            if (actualCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                sb = new StringBuilder();
                sb.append(actualCameraId);
                str = "_RearUltra";
            } else if (actualCameraId == Camera2DataContainer.getInstance().getStandaloneMacroCameraId()) {
                sb = new StringBuilder();
                sb.append(actualCameraId);
                str = "_RearMacro";
            } else if (actualCameraId == Camera2DataContainer.getInstance().getAuxCameraId()) {
                sb = new StringBuilder();
                sb.append(actualCameraId);
                str = PictureInfo.SENSOR_TYPE_REAR_TELE;
            } else if (actualCameraId == Camera2DataContainer.getInstance().getUltraTeleCameraId()) {
                sb = new StringBuilder();
                sb.append(actualCameraId);
                str = "_RearTele4x";
            } else if (actualCameraId == Camera2DataContainer.getInstance().getMainBackCameraId()) {
                sb = new StringBuilder();
                sb.append(actualCameraId);
                str = "_RearWide";
            } else {
                if (actualCameraId == Camera2DataContainer.getInstance().getSATCameraId()) {
                    sb = new StringBuilder();
                    sb.append(String.valueOf(actualCameraId));
                    sb.append("_");
                    str = PictureInfo.SENSOR_TYPE_REAR;
                }
                fArr = this.mFocalLengths;
                if (fArr != null && fArr.length > 0) {
                    opMode.setLensfocal(fArr[0]);
                }
                fArr2 = this.mApertures;
                if (fArr2 != null && fArr2.length > 0) {
                    opMode.setLensApertues(fArr2[0]);
                }
                retriveFaceInfo = DebugInfoUtil.getRetriveFaceInfo(this.mMainProtocol.getViewRects(this.mPictureSize));
                if (!TextUtils.isEmpty(retriveFaceInfo)) {
                    opMode.setFaceRoi(retriveFaceInfo);
                }
                opMode.setOperateMode(this.mOperatingMode);
                opMode.setZoomMulti(getZoomRatio());
                camera2Proxy = this.mCamera2Device;
                if (camera2Proxy != null) {
                    opMode.setEvValue(camera2Proxy.getExposureCompensation());
                    MeteringRectangle[] aFRegions = this.mCamera2Device.getCameraConfigs().getAFRegions();
                    if (aFRegions != null && aFRegions.length > 0) {
                        opMode.setTouchRoi(aFRegions[0]);
                    }
                }
                opMode.end();
                return opMode;
            }
            sb.append(str);
            sb2 = sb.toString();
        }
        opMode.setLensType(sb2);
        fArr = this.mFocalLengths;
        opMode.setLensfocal(fArr[0]);
        fArr2 = this.mApertures;
        opMode.setLensApertues(fArr2[0]);
        retriveFaceInfo = DebugInfoUtil.getRetriveFaceInfo(this.mMainProtocol.getViewRects(this.mPictureSize));
        if (!TextUtils.isEmpty(retriveFaceInfo)) {
        }
        opMode.setOperateMode(this.mOperatingMode);
        opMode.setZoomMulti(getZoomRatio());
        camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
        }
        opMode.end();
        return opMode;
    }

    private String getPrefix() {
        return "";
    }

    private CameraSize getSatPictureSize() {
        int satMasterCameraId = this.mCamera2Device.getSatMasterCameraId();
        if (satMasterCameraId == 1) {
            return this.mUltraWidePictureSize;
        }
        if (satMasterCameraId == 2) {
            return this.mWidePictureSize;
        }
        if (satMasterCameraId == 3) {
            return this.mTelePictureSize;
        }
        if (satMasterCameraId == 4) {
            return this.mUltraTelePictureSize;
        }
        if (satMasterCameraId == 5) {
            return this.mFakeTelePictureSize;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getSatPictureSize: invalid satMasterCameraId ");
        sb.append(satMasterCameraId);
        Log.e(str, sb.toString());
        return this.mWidePictureSize;
    }

    private String getSuffix() {
        return !this.mMutexModePicker.isNormal() ? this.mMutexModePicker.getSuffix() : "";
    }

    private static String getTiltShiftMode() {
        if (CameraSettings.isTiltShiftOn()) {
            return DataRepository.dataItemRunning().getComponentRunningTiltValue().getComponentValue(160);
        }
        return null;
    }

    private void handleSaveFinishIfNeed() {
        this.mWaitSaveFinish = false;
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(61);
        }
    }

    private void initZoomMapControllerIfNeeded() {
        if (this.mZoomMapController == null && C0124O00000oO.isSupportedOpticalZoom() && isBackCamera()) {
            CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
            if (cameraCapabilities != null && cameraCapabilities.isSatPipSupported()) {
                this.mZoomMapController = new ZoomMapController(this.mActivity, false);
            }
        }
    }

    private void initializeFocusManager() {
        this.mFocusManager = new FocusManager2(this.mCameraCapabilities, this, false, this.mActivity.getMainLooper());
        Rect renderRect = this.mActivity.getCameraScreenNail() != null ? this.mActivity.getCameraScreenNail().getRenderRect() : null;
        if (renderRect == null || renderRect.width() <= 0) {
            this.mFocusManager.setRenderSize(Display.getWindowWidth(), Display.getWindowHeight());
            this.mFocusManager.setPreviewSize(Display.getWindowWidth(), Display.getWindowHeight());
            return;
        }
        this.mFocusManager.setRenderSize(this.mActivity.getCameraScreenNail().getRenderWidth(), this.mActivity.getCameraScreenNail().getRenderHeight());
        this.mFocusManager.setPreviewSize(renderRect.width(), renderRect.height());
    }

    /* access modifiers changed from: private */
    public boolean is3ALocked() {
        return this.m3ALocked;
    }

    private boolean isCannotGotoGallery() {
        return this.mPaused || this.isZooming || isKeptBitmapTexture() || this.mMultiSnapStatus || getCameraState() == 0 || isQueueFull() || isInCountDown();
    }

    private boolean isFrontMirror() {
        return false;
    }

    private boolean isImageSaverFull() {
        ImageSaver imageSaver = this.mActivity.getImageSaver();
        if (imageSaver == null) {
            Log.w(TAG, "isParallelQueueFull: ImageSaver is null");
            return false;
        } else if (!imageSaver.isSaveQueueFull()) {
            return false;
        } else {
            Log.d(TAG, "isParallelQueueFull: ImageSaver queue is full");
            return true;
        }
    }

    private boolean isIn3OrMoreSatMode() {
        return 36866 == this.mOperatingMode && HybridZoomingSystem.IS_3_OR_MORE_SAT;
    }

    private boolean isInCountDown() {
        Disposable disposable = this.mCountdownDisposable;
        return disposable != null && !disposable.isDisposed();
    }

    private boolean isInMultiSurfaceSatMode() {
        return this.mCamera2Device.isInMultiSurfaceSatMode();
    }

    private boolean isParallelCameraSessionMode() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        return camera2Proxy != null && camera2Proxy.getCapabilities().isSupportParallelCameraDevice() && !isParallelUnSupported() && this.mCamera2Device.getSATSubCameraIds() != null && getZoomRatio() < HybridZoomingSystem.getTeleMinZoomRatio();
    }

    private boolean isParallelQueueFull() {
        boolean z = false;
        if (!this.mEnableParallelSession || this.mActivity.getImageSaver() == null) {
            return false;
        }
        if (isImageSaverFull()) {
            return true;
        }
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
        if (localBinder != null) {
            z = localBinder.needWaitProcess();
        } else {
            Log.w(TAG, "isParallelQueueFull: NOTICE: CHECK WHY BINDER IS NULL!");
        }
        return z;
    }

    private boolean isParallelSessionConfigured() {
        boolean z;
        if (!this.mEnableParallelSession) {
            return true;
        }
        synchronized (this.mParallelSessionLock) {
            z = this.mParallelSessionConfigured;
        }
        return z;
    }

    private boolean isParallelUnSupported() {
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        return camera2Proxy != null && (!camera2Proxy.getCameraConfigs().isParallelSupportedCaptureMode() || (localBinder != null && localBinder.isAnyRequestBlocked()));
    }

    private boolean isQueueFull() {
        return this.mEnableParallelSession ? isParallelQueueFull() : isImageSaverFull();
    }

    private void lockAEAF() {
        Log.d(TAG, "lockAEAF");
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(true);
        }
        this.m3ALocked = true;
    }

    private void onShutter(boolean z) {
        if (getCameraState() == 0) {
            Log.d(TAG, "onShutter: preview stopped");
            return;
        }
        this.mShutterCallbackTime = System.currentTimeMillis();
        this.mShutterLag = this.mShutterCallbackTime - this.mCaptureStartTime;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mShutterLag = ");
        sb.append(this.mShutterLag);
        sb.append(d.H);
        Log.v(str, sb.toString());
        updateEnablePreviewThumbnail(z);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onShutter mEnabledPreviewThumbnail:");
        sb2.append(this.mEnabledPreviewThumbnail);
        Log.d(str2, sb2.toString());
        if (this.mEnabledPreviewThumbnail) {
            this.mActivity.getCameraScreenNail().requestReadPixels();
        }
        if (!isKeptBitmapTexture() && !this.mMultiSnapStatus && this.mBlockQuickShot && !CameraSettings.isGroupShotOn()) {
            resetStatusToIdle();
        }
    }

    private void prepareNormalCapture() {
        Log.d(TAG, "prepareNormalCapture E");
        this.mEnabledPreviewThumbnail = false;
        this.mCaptureStartTime = System.currentTimeMillis();
        this.mCamera2Device.setCaptureTime(this.mCaptureStartTime);
        ScenarioTrackUtil.trackCaptureTimeStart(isFrontCamera(), this.mModuleIndex);
        ScenarioTrackUtil.trackShotToGalleryStart(isFrontCamera(), this.mModuleIndex, this.mCaptureStartTime);
        ScenarioTrackUtil.trackShotToViewStart(isFrontCamera(), this.mModuleIndex, this.mCaptureStartTime);
        this.mLastCaptureTime = this.mCaptureStartTime;
        setCameraState(3);
        WatermarkItem watermarkItem = null;
        this.mMajorItem = null;
        this.mMinorItem = null;
        ComponentRunningAIWatermark componentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
        String str = WatermarkConstant.SUPER_MOON_RESET;
        if (componentRunningAIWatermark != null) {
            this.mMajorItem = DataRepository.dataItemRunning().getComponentRunningAIWatermark().getMajorWatermarkItem();
            WatermarkItem watermarkItem2 = this.mMajorItem;
            WatermarkItem watermarkItem3 = (watermarkItem2 == null || !watermarkItem2.getKey().equals(str)) ? this.mMajorItem : null;
            this.mMajorItem = watermarkItem3;
            this.mMinorItem = DataRepository.dataItemRunning().getComponentRunningAIWatermark().getMinorWatermarkItem();
            WatermarkItem watermarkItem4 = this.mMinorItem;
            if (watermarkItem4 == null || !watermarkItem4.getKey().equals(str)) {
                watermarkItem = this.mMinorItem;
            }
            this.mMinorItem = watermarkItem;
        }
        WatermarkItem watermarkItem5 = this.mMajorItem;
        String key = watermarkItem5 == null ? str : watermarkItem5.getKey();
        WatermarkItem watermarkItem6 = this.mMinorItem;
        if (watermarkItem6 != null) {
            str = watermarkItem6.getKey();
        }
        String str2 = (this.mMajorItem == null && this.mMinorItem == null) ? BaseEvent.VALUE_FALSE : BaseEvent.VALUE_TRUE;
        CameraStatUtils.trackSuperMoonCapture(key, str, str2);
        if (this.mMajorItem == null && this.mMinorItem == null) {
            this.mJpegRotation = Util.getJpegRotation(this.mBogusCameraId, this.mOrientation);
        } else {
            this.mJpegRotation = 90;
            Log.d(TAG, "prepareNormalCapture: watermark switch on, force change jpeg rotation to 90");
        }
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("prepareNormalCapture: mOrientation = ");
        sb.append(this.mOrientation);
        sb.append(", mJpegRotation = ");
        sb.append(this.mJpegRotation);
        Log.d(str3, sb.toString());
        this.mCamera2Device.setJpegRotation(this.mJpegRotation);
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        this.mCamera2Device.setGpsLocation(currentLocation);
        this.mLocation = currentLocation;
        updateMfnr();
        updateSuperResolution();
        updateShotDetermine();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getPrefix());
        sb2.append(Util.createJpegName(System.currentTimeMillis()));
        sb2.append(getSuffix());
        String sb3 = sb2.toString();
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("prepareNormalCapture title = ");
        sb4.append(sb3);
        Log.k(3, str4, sb4.toString());
        this.mCamera2Device.setShotSavePath(Storage.generateFilepath4Image(sb3, CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat)), true);
        this.mCamera2Device.setNeedSequence(false);
        this.mAlgorithmName = this.mMutexModePicker.getAlgorithmName();
        setPictureOrientation();
        Log.d(TAG, "prepareNormalCapture X");
    }

    private void previewWhenSessionSuccess() {
        setCameraState(1);
        updatePreferenceInWorkThread(UpdateConstant.SUPER_MOON_TYPES_ON_PREVIEW_SUCCESS);
    }

    private void resumePreviewInWorkThread() {
        updatePreferenceInWorkThread(new int[0]);
    }

    /* access modifiers changed from: private */
    public void setOrientation(int i, int i2) {
        if (i != -1) {
            this.mOrientation = i;
            EffectController.getInstance().setOrientation(Util.getShootOrientation(this.mActivity, this.mOrientation));
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
                CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new C0374O000o0O(this));
            } else {
                updatePreferenceInWorkThread(35);
            }
        }
    }

    private void setPictureOrientation() {
        this.mShootRotation = this.mActivity.getSensorStateManager().isDeviceLying() ? (float) this.mOrientation : this.mDeviceRotation;
        int i = this.mOrientation;
        if (i == -1) {
            i = 0;
        }
        this.mShootOrientation = i;
    }

    private void startCount(final int i, int i2) {
        if (!this.mMediaRecorderRecording) {
            this.mIsStartCount = true;
            if (!checkShutterCondition()) {
                this.mIsStartCount = false;
                return;
            }
            setTriggerMode(i2);
            tryRemoveCountDownMessage();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startCount: ");
            sb.append(i);
            Log.d(str, sb.toString());
            Observable.interval(1, TimeUnit.SECONDS).take((long) i).observeOn(AndroidSchedulers.mainThread()).subscribe((Observer) new Observer() {
                public void onComplete() {
                    SuperMoonModule.this.mIsStartCount = false;
                    SuperMoonModule.this.tryRemoveCountDownMessage();
                    if (SuperMoonModule.this.isAlive()) {
                        Camera camera = SuperMoonModule.this.mActivity;
                        if (camera != null && !camera.isActivityPaused()) {
                            SuperMoonModule.this.onShutterButtonFocus(true, 3);
                            SuperMoonModule superMoonModule = SuperMoonModule.this;
                            superMoonModule.startNormalCapture(superMoonModule.getTriggerMode());
                            SuperMoonModule.this.onShutterButtonFocus(false, 0);
                            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                            if (topAlert != null) {
                                topAlert.reInitAlert(true);
                            }
                        }
                    }
                }

                public void onError(Throwable th) {
                    SuperMoonModule.this.mIsStartCount = false;
                }

                public void onNext(Long l) {
                    int intValue = i - (l.intValue() + 1);
                    if (intValue > 0) {
                        SuperMoonModule.this.playCameraSound(5);
                        SuperMoonModule.this.mTopAlert.showDelayNumber(intValue);
                    }
                }

                public void onSubscribe(Disposable disposable) {
                    SuperMoonModule.this.mCountdownDisposable = disposable;
                    SuperMoonModule.this.playCameraSound(7);
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null) {
                        AndroidSchedulers.mainThread().scheduleDirect(new O000o0(topAlert), 120, TimeUnit.MILLISECONDS);
                    }
                    SuperMoonModule.this.mMainProtocol.clearFocusView(7);
                    SuperMoonModule.this.mTopAlert.showDelayNumber(i);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startNormalCapture(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startNormalCapture mode -> ");
        sb.append(i);
        Log.k(3, str, sb.toString());
        this.mActivity.getScreenHint().updateHint();
        if (Storage.isLowStorageAtLastPoint()) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Not enough space or storage not ready. remaining=");
            sb2.append(Storage.getLeftSpace());
            Log.k(4, str2, sb2.toString());
            return;
        }
        blockSnapClickUntilSaveFinish();
        prepareNormalCapture();
        this.mHandler.sendEmptyMessageDelayed(50, CAPTURE_DURATION_THRESHOLD);
        this.mCamera2Device.setQuickShotAnimation(false);
        if (C0122O00000o.instance().OOOO0oO()) {
            if ((getModuleIndex() == 163 || getModuleIndex() == 165 || getModuleIndex() == 186 || getModuleIndex() == 182) && getZoomRatio() == 1.0f) {
                this.mCamera2Device.setFlawDetectEnable(true);
            } else {
                this.mCamera2Device.setFlawDetectEnable(false);
            }
        }
        this.mCamera2Device.takePicture(this, this.mActivity.getImageSaver());
        this.mBlockQuickShot = true;
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("isParallelSessionEnable:");
        sb3.append(isParallelSessionEnable());
        sb3.append(", and block quick shot");
        Log.d(str3, sb3.toString());
    }

    private void unlockAEAF() {
        Log.d(TAG, "unlockAEAF");
        this.m3ALocked = false;
        if (this.mAeLockSupported && isDeviceAlive()) {
            this.mCamera2Device.unlockExposure();
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(false);
        }
    }

    private void updateASD() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setASDEnable(true);
            Log.d(TAG, "updateASD call setASDEnable with true");
        }
    }

    private void updateAiScene() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setAiASDEnable(true);
            this.mCamera2Device.setASDScene(35);
            Log.d(TAG, "updateAiScene call setASDScene with AI_SCENE_MODE_MOON");
        }
    }

    private void updateEnablePreviewThumbnail(boolean z) {
        this.mEnabledPreviewThumbnail = enablePreviewAsThumbnail();
        this.mActivity.setPreviewThumbnail(this.mEnabledPreviewThumbnail);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateEnablePreviewThumbnail mEnabledPreviewThumbnail:");
        sb.append(this.mEnabledPreviewThumbnail);
        Log.d(str, sb.toString());
    }

    private void updateFilter() {
        int shaderEffect = CameraSettings.getShaderEffect();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateFilter: 0x");
        sb.append(Integer.toHexString(shaderEffect));
        Log.v(str, sb.toString());
        EffectController.getInstance().setEffect(shaderEffect);
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.resumePreview();
        }
    }

    private void updateFocusArea() {
        Camera camera = this.mActivity;
        if (camera != null && !camera.isActivityPaused() && isAlive()) {
            Rect cropRegion = getCropRegion();
            Rect activeArraySize = getActiveArraySize();
            this.mActivity.getSensorStateManager().setFocusSensorEnabled(this.mFocusManager.getMeteringAreas(cropRegion, activeArraySize) != null);
            this.mCamera2Device.setAERegions(this.mFocusManager.getMeteringAreas(cropRegion, activeArraySize));
            if (this.mFocusAreaSupported) {
                this.mCamera2Device.setAFRegions(this.mFocusManager.getFocusAreas(cropRegion, activeArraySize));
            }
            String focusMode = CameraSettings.getFocusMode();
            if (!this.mFocusAreaSupported || "manual".equals(focusMode)) {
                this.mCamera2Device.resumePreview();
            }
        }
    }

    private void updateFocusMode() {
        Log.d(TAG, "updateFocusMode E");
        String focusMode = this.mFocusManager.setFocusMode(CameraSettings.getFocusMode());
        setFocusMode(focusMode);
        if (CameraSettings.isFocusModeSwitching() && isBackCamera()) {
            CameraSettings.setFocusModeSwitching(false);
            this.mFocusManager.resetFocusStateIfNeeded();
        }
        if (focusMode.equals("manual")) {
            this.mCamera2Device.setFocusDistance((this.mCameraCapabilities.getMinimumFocusDistance() * ((float) CameraSettings.getFocusPosition())) / 1000.0f);
        }
        Log.d(TAG, "updateFocusMode X");
    }

    private void updateMfnr() {
        boolean z = !C0122O00000o.instance().getConfig().Oo0OoO0();
        if (this.mCamera2Device != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateMfnr setMfnr to ");
            sb.append(z);
            Log.d(str, sb.toString());
            this.mCamera2Device.setMfnr(z);
        }
    }

    private void updateOIS() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setEnableOIS(true);
            Log.d(TAG, "updateOIS call setEnableOIS with true");
        }
    }

    private void updateOutputSize(@NonNull CameraSize cameraSize) {
        if (C0124O00000oO.isMTKPlatform()) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                cameraSize = PictureSizeManager.getBestPictureSize(camera2Proxy.getSatCapabilities().getSupportedOutputSizeWithAssignedMode(256), this.mModuleIndex);
            }
        }
        if (165 == this.mModuleIndex) {
            int i = cameraSize.width;
            int i2 = cameraSize.height;
            if (i <= i2) {
                i2 = i;
            }
            this.mOutputPictureSize = new CameraSize(i2, i2);
            return;
        }
        this.mOutputPictureSize = cameraSize;
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x0326  */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x0328  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updatePictureAndPreviewSize() {
        String str;
        Object[] objArr;
        Locale locale;
        String str2;
        CameraSize cameraSize;
        CameraSize cameraSize2;
        this.mCameraCapabilities.setOperatingMode(getOperatingMode());
        int i = this.mEnableParallelSession ? 35 : 256;
        int[] sATSubCameraIds = this.mCamera2Device.getSATSubCameraIds();
        boolean z = sATSubCameraIds != null;
        if (z) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[SAT] camera list: ");
            sb.append(Arrays.toString(sATSubCameraIds));
            Log.d(str3, sb.toString());
            for (int i2 : sATSubCameraIds) {
                if (i2 == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                    if (this.mUltraCameraCapabilities == null) {
                        this.mUltraCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getUltraWideCameraId());
                    }
                    CameraCapabilities cameraCapabilities = this.mUltraCameraCapabilities;
                    if (cameraCapabilities != null) {
                        cameraCapabilities.setOperatingMode(this.mOperatingMode);
                        this.mUltraWidePictureSize = getBestPictureSize(this.mUltraCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i));
                    }
                } else if (i2 == Camera2DataContainer.getInstance().getMainBackCameraId()) {
                    if (this.mWideCameraCapabilities == null) {
                        this.mWideCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getMainBackCameraId());
                    }
                    CameraCapabilities cameraCapabilities2 = this.mWideCameraCapabilities;
                    if (cameraCapabilities2 != null) {
                        cameraCapabilities2.setOperatingMode(this.mOperatingMode);
                        List supportedOutputSizeWithAssignedMode = this.mWideCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i);
                        int OO00oOo = C0122O00000o.instance().OO00oOo();
                        if (OO00oOo != 0) {
                            PictureSizeManager.initializeLimitWidth(supportedOutputSizeWithAssignedMode, OO00oOo, this.mModuleIndex, this.mBogusCameraId);
                            cameraSize2 = PictureSizeManager.getBestPictureSize(this.mModuleIndex);
                        } else {
                            cameraSize2 = getBestPictureSize(supportedOutputSizeWithAssignedMode);
                        }
                        this.mWidePictureSize = cameraSize2;
                    }
                } else if (i2 == Camera2DataContainer.getInstance().getAuxCameraId()) {
                    if (this.mTeleCameraCapabilities == null) {
                        this.mTeleCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getAuxCameraId());
                    }
                    CameraCapabilities cameraCapabilities3 = this.mTeleCameraCapabilities;
                    if (cameraCapabilities3 != null) {
                        cameraCapabilities3.setOperatingMode(this.mOperatingMode);
                        this.mTelePictureSize = getBestPictureSize(this.mTeleCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i));
                    }
                } else if (i2 == Camera2DataContainer.getInstance().getUltraTeleCameraId()) {
                    if (this.mUltraTeleCameraCapabilities == null) {
                        this.mUltraTeleCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getUltraTeleCameraId());
                    }
                    CameraCapabilities cameraCapabilities4 = this.mUltraTeleCameraCapabilities;
                    if (cameraCapabilities4 != null) {
                        cameraCapabilities4.setOperatingMode(this.mOperatingMode);
                        this.mUltraTelePictureSize = getBestPictureSize(this.mUltraTeleCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i));
                    }
                    this.mCamera2Device.setUltraTelePictureSize(this.mUltraTelePictureSize);
                } else if (i2 == Camera2DataContainer.getInstance().getStandaloneMacroCameraId()) {
                    if (this.mMacroCameraCapabilities == null) {
                        this.mMacroCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getStandaloneMacroCameraId());
                    }
                    CameraCapabilities cameraCapabilities5 = this.mMacroCameraCapabilities;
                    if (cameraCapabilities5 != null) {
                        cameraCapabilities5.setOperatingMode(this.mOperatingMode);
                        this.mMacroPictureSize = getBestPictureSize(this.mMacroCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i));
                    }
                    this.mCamera2Device.setMarcroPictureSize(this.mMacroPictureSize);
                }
            }
            if (this.mFakeTeleCameraCapabilities == null) {
                this.mFakeTeleCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getSATCameraId());
            }
            CameraCapabilities cameraCapabilities6 = this.mFakeTeleCameraCapabilities;
            if (cameraCapabilities6 != null && cameraCapabilities6.supportFakeSat()) {
                this.mFakeTeleCameraCapabilities.setOperatingMode(this.mOperatingMode);
                this.mFakeTelePictureSize = getBestPictureSize(this.mFakeTeleCameraCapabilities.getSupportFakeSatYuvSizes());
                this.mCamera2Device.setFakeTelePictureSize(this.mFakeTelePictureSize);
                this.mFakeSatOutSize = getBestPictureSize(this.mFakeTeleCameraCapabilities.getSupportFakeSatJpegSizes());
                this.mCamera2Device.setFakeTeleOutputSize(this.mFakeSatOutSize);
                Log.d(TAG, String.format(Locale.ENGLISH, "add fakesat size: %s, output size: %s", new Object[]{this.mFakeTelePictureSize, this.mFakeSatOutSize}));
            }
            if (C0122O00000o.instance().OOOOoOo()) {
                str2 = TAG;
                locale = Locale.ENGLISH;
                objArr = new Object[]{this.mUltraWidePictureSize, this.mWidePictureSize, this.mTelePictureSize, this.mUltraTelePictureSize};
                str = "ultraWideSize: %s, wideSize: %s, teleSize: %s, ultraTeleSize:%s";
            } else {
                str2 = TAG;
                locale = Locale.ENGLISH;
                objArr = new Object[]{this.mUltraWidePictureSize, this.mWidePictureSize, this.mTelePictureSize, this.mFakeTelePictureSize};
                str = "ultraWideSize: %s, wideSize: %s, teleSize: %s, fakeTeleSize: %s";
            }
            Log.d(str2, String.format(locale, str, objArr));
            this.mCamera2Device.setUltraWidePictureSize(this.mUltraWidePictureSize);
            this.mCamera2Device.setWidePictureSize(this.mWidePictureSize);
            this.mCamera2Device.setTelePictureSize(this.mTelePictureSize);
            this.mPictureSize = getSatPictureSize();
            List supportedOutputSizeWithAssignedMode2 = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class);
            CameraSize cameraSize3 = this.mPictureSize;
            this.mPreviewSize = Util.getOptimalPreviewSize(false, this.mBogusCameraId, supportedOutputSizeWithAssignedMode2, (double) CameraSettings.getPreviewAspectRatio(cameraSize3.width, cameraSize3.height));
            this.mCamera2Device.setPreviewSize(this.mPreviewSize);
            this.mCamera2Device.setAlgorithmPreviewSize(this.mPreviewSize);
            this.mCamera2Device.setAlgorithmPreviewFormat(35);
            this.mOutputPictureFormat = 256;
            String str4 = TAG;
            Locale locale2 = Locale.ENGLISH;
            Object[] objArr2 = new Object[1];
            String str5 = "HEIC";
            String str6 = "JPEG";
            objArr2[0] = CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? str5 : str6;
            Log.d(str4, String.format(locale2, "updateSize: use %s as preferred output image format", objArr2));
            if (this.mEnableParallelSession) {
                List list = CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? this.mCameraCapabilities.hasStandaloneHeicStreamConfigurations() ? this.mCameraCapabilities.getSupportedHeicOutputStreamSizes() : this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(this.mOutputPictureFormat) : this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(256);
                int i3 = this.mModuleIndex;
                if (i3 == 165) {
                    CameraSize cameraSize4 = this.mPictureSize;
                    int min = Math.min(cameraSize4.width, cameraSize4.height);
                    boolean z2 = C0124O00000oO.isMTKPlatform() && isFrontCamera();
                    this.mOutputPictureSize = PictureSizeManager.getBestSquareSize(list, min, z2);
                    if (this.mOutputPictureSize.isEmpty()) {
                        String str7 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Could not find a proper squared Jpeg size, defaults to: ");
                        sb2.append(min);
                        sb2.append("x");
                        sb2.append(min);
                        Log.w(str7, sb2.toString());
                        cameraSize = new CameraSize(min, min);
                    }
                    String str8 = TAG;
                    Locale locale3 = Locale.ENGLISH;
                    Object[] objArr3 = new Object[2];
                    objArr3[0] = !CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? str5 : str6;
                    objArr3[1] = this.mOutputPictureSize;
                    Log.d(str8, String.format(locale3, "updateSize: algoUp picture size (%s): %s", objArr3));
                } else {
                    cameraSize = z ? this.mPictureSize : PictureSizeManager.getBestPictureSize(list, i3);
                }
                this.mOutputPictureSize = cameraSize;
                String str82 = TAG;
                Locale locale32 = Locale.ENGLISH;
                Object[] objArr32 = new Object[2];
                objArr32[0] = !CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? str5 : str6;
                objArr32[1] = this.mOutputPictureSize;
                Log.d(str82, String.format(locale32, "updateSize: algoUp picture size (%s): %s", objArr32));
            }
            String str9 = TAG;
            Locale locale4 = Locale.ENGLISH;
            Object[] objArr4 = new Object[4];
            if (this.mEnableParallelSession) {
                str5 = "YUV";
            } else if (!CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat)) {
                str5 = str6;
            }
            objArr4[0] = str5;
            objArr4[1] = this.mPictureSize;
            objArr4[2] = this.mPreviewSize;
            objArr4[3] = this.mSensorRawImageSize;
            Log.d(str9, String.format(locale4, "updateSize: picture size (%s): %s, preview size: %s, sensor raw image size: %s", objArr4));
            CameraSize cameraSize5 = this.mPreviewSize;
            updateCameraScreenNailSize(cameraSize5.width, cameraSize5.height);
            checkDisplayOrientation();
            return;
        }
        throw new RuntimeException("SuperMoon Mode must with SAT!!");
    }

    private void updateShotDetermine() {
        this.mEnableParallelSession = isParallelSessionEnable();
        boolean z = !this.mEnableParallelSession && C0122O00000o.instance().OOoO() && enablePreviewAsThumbnail() && !CameraSettings.isLiveShotOn();
        this.mEnableShot2Gallery = z;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("enableParallel=");
        sb.append(this.mEnableParallelSession);
        sb.append(" mEnableShot2Gallery=");
        sb.append(this.mEnableShot2Gallery);
        sb.append(" shotType=");
        sb.append(8);
        Log.k(3, str, sb.toString());
        this.mCamera2Device.setShotType(8);
        this.mCamera2Device.setShot2Gallery(this.mEnableShot2Gallery);
    }

    private void updateSuperResolution() {
        boolean Oo0OoO0 = C0122O00000o.instance().getConfig().Oo0OoO0();
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setSuperResolution(Oo0OoO0);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateSuperResolution call setSuperResolution with ");
            sb.append(Oo0OoO0);
            Log.d(str, sb.toString());
        }
    }

    private void updateZsl() {
        boolean z = !C0124O00000oO.O0o0O0O && !C0124O00000oO.O0o0OOO && C0122O00000o.instance().OOoOoOO();
        if (this.mCamera2Device != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateZsl setEnableZsl to ");
            sb.append(z);
            Log.d(str, sb.toString());
            this.mCamera2Device.setEnableZsl(z);
        }
    }

    public /* synthetic */ void O00oo0o() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
    }

    public /* synthetic */ void O00oo0o0() {
        this.mActivity.getSensorStateManager().setLieIndicatorEnabled(true);
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
        Log.d(TAG, "closeCamera: E");
        setCameraState(0);
        synchronized (this.mCameraDeviceLock) {
            if (this.mCamera2Device != null) {
                if (this.mMultiSnapStatus) {
                    this.mCamera2Device.captureAbortBurst();
                    this.mMultiSnapStatus = false;
                }
                this.mCamera2Device.setScreenLightCallback(null);
                this.mCamera2Device.setMetaDataCallback(null);
                this.mCamera2Device.setErrorCallback(null);
                this.mCamera2Device.setFocusCallback(null);
                this.mCamera2Device.setAiASDEnable(false);
                if (this.mCameraCapabilities.isSupportAIIE()) {
                    this.mCamera2Device.setAIIEPreviewEnable(false);
                }
                if (C0124O00000oO.OOO0Oo()) {
                    this.mCamera2Device.stopPreviewCallback(true);
                }
                this.m3ALocked = false;
                this.mCamera2Device.setASDEnable(false);
                this.mCamera2Device.setAiMoonEffectEnable(false);
                this.mCamera2Device.setEnableOIS(false);
                this.mCamera2Device.setSuperResolution(false);
                this.mCamera2Device.setMfnr(false);
                this.mCamera2Device.setColorEnhanceEnable(false);
                if (!isParallelCameraSessionMode()) {
                    this.mCamera2Device = null;
                }
            }
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(false);
            this.mFocusManager.destroy();
        }
        if (C0124O00000oO.OOO0Oo()) {
            PreviewDecodeManager.getInstance().quit();
        }
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
        if (localBinder != null) {
            localBinder.setOnSessionStatusCallBackListener(null);
        }
        Log.d(TAG, "closeCamera: X");
    }

    /* access modifiers changed from: protected */
    public void consumePreference(int... iArr) {
        for (int i : iArr) {
            if (i == 1) {
                updatePictureAndPreviewSize();
            } else if (i == 2) {
                updateFilter();
            } else if (i != 3) {
                switch (i) {
                    case 12:
                        setEvValue();
                        break;
                    case 14:
                        updateFocusMode();
                        break;
                    case 20:
                        updateOIS();
                        break;
                    case 22:
                        updateZsl();
                        break;
                    case 24:
                        applyZoomRatio();
                        break;
                    case 30:
                        updateSuperResolution();
                        break;
                    case 34:
                        updateMfnr();
                        break;
                    case 36:
                        updateAiScene();
                        break;
                    case 44:
                        updateShotDetermine();
                        break;
                    case 55:
                        updateModuleRelated();
                        break;
                    case 70:
                        updateASD();
                        break;
                }
            } else {
                updateFocusArea();
            }
        }
    }

    public void fillFeatureControl(StartControl startControl) {
        super.fillFeatureControl(startControl);
    }

    /* access modifiers changed from: protected */
    public CameraSize getBestPictureSize(List list) {
        PictureSizeManager.initialize(list, getMaxPictureSize(), this.mModuleIndex, this.mBogusCameraId);
        return PictureSizeManager.getBestPictureSize(this.mModuleIndex);
    }

    /* access modifiers changed from: protected */
    public CameraSize getBestPictureSize(List list, float f) {
        PictureSizeManager.initialize(list, getMaxPictureSize(), this.mModuleIndex, this.mBogusCameraId);
        return PictureSizeManager.getBestPictureSize(f);
    }

    /* access modifiers changed from: protected */
    public int getMaxPictureSize() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        this.mOperatingMode = CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_SAT;
        return this.mOperatingMode;
    }

    public String getTag() {
        return TAG;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0041, code lost:
        if (getCameraState() == 3) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x004f, code lost:
        if (r0.isParallelBusy(false) != false) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006b, code lost:
        if (r0.isAnyRequestBlocked() != false) goto L_0x0043;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBlockSnap() {
        boolean z;
        boolean z2 = true;
        if (DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode() && !C0122O00000o.instance().OOoOOO0()) {
            LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
            if (localBinder != null && !localBinder.isIdle()) {
                Log.i(TAG, "is shoting super night and discard snap");
                return true;
            }
        }
        if (!isParallelCameraSessionMode()) {
            z = getCameraState() == 3;
            if (!z) {
                LocalBinder localBinder2 = AlgoConnector.getInstance().getLocalBinder();
                if (localBinder2 != null) {
                }
                z = false;
            }
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            z2 = false;
            return z2;
        } else if (!this.mCamera2Device.isNeedFlashOn()) {
            Camera2Proxy camera2Proxy2 = this.mCamera2Device;
            if (camera2Proxy2 != null) {
            }
            z = false;
            if (!this.mPaused && !this.isZooming && !isKeptBitmapTexture() && !this.mMultiSnapStatus && getCameraState() != 0 && !z) {
                Camera2Proxy camera2Proxy3 = this.mCamera2Device;
                if ((camera2Proxy3 == null || !camera2Proxy3.isCaptureBusy(this.mMutexModePicker.isHdr())) && !isQueueFull() && !isInCountDown() && !this.mWaitSaveFinish && isParallelSessionConfigured() && !this.mHandler.hasMessages(62)) {
                    z2 = false;
                }
            }
            return z2;
        }
        z = true;
        Camera2Proxy camera2Proxy32 = this.mCamera2Device;
        z2 = false;
        return z2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0054, code lost:
        if (r0.isAnyRequestBlocked() != false) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0062, code lost:
        if (r0.isCaptureBusy(true) != false) goto L_0x0056;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isDoingAction() {
        boolean z;
        boolean z2 = true;
        if (C0122O00000o.instance().OOo000o()) {
            LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
            if (localBinder != null && !localBinder.isIdle()) {
                Log.i(TAG, "[ALGOUP|MMCAMERA] Doing action");
                return true;
            }
        }
        if (getCameraState() == 3) {
            return true;
        }
        if (isParallelCameraSessionMode()) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            z = camera2Proxy != null && camera2Proxy.isParallelBusy(true);
            if (!z) {
                LocalBinder localBinder2 = AlgoConnector.getInstance().getLocalBinder();
                if (localBinder2 != null) {
                }
                z = false;
            }
            if (!this.mPaused && !this.isZooming && !isKeptBitmapTexture() && !this.mMultiSnapStatus && getCameraState() != 0 && !z && !isQueueFull() && !this.mWaitSaveFinish && !isInCountDown()) {
                z2 = false;
            }
            return z2;
        }
        Camera2Proxy camera2Proxy2 = this.mCamera2Device;
        if (camera2Proxy2 != null) {
        }
        z = false;
        z2 = false;
        return z2;
        z = true;
        z2 = false;
        return z2;
    }

    /* access modifiers changed from: protected */
    public boolean isParallelSessionEnable() {
        return true;
    }

    public boolean isShowAeAfLockIndicator() {
        return this.m3ALocked;
    }

    public boolean isShowCaptureButton() {
        return !this.mMutexModePicker.isBurstShoot() && isSupportFocusShoot();
    }

    public boolean isSupportFocusShoot() {
        return DataRepository.dataItemGlobal().isGlobalSwitchOn("pref_camera_focus_shoot_key");
    }

    public boolean isUnInterruptable() {
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0020, code lost:
        if (r3.mMediaRecorderRecording == false) goto L_0x0024;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isZoomEnabled() {
        boolean z = true;
        if (getCameraState() != 3 && !isFrontCamera()) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                if (!camera2Proxy.isCaptureBusy(true)) {
                    if (isFrameAvailable()) {
                    }
                }
            }
        }
        z = false;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isZoomEnabled: ");
        sb.append(z);
        Log.d(str, sb.toString());
        return z;
    }

    public boolean multiCapture() {
        return false;
    }

    public void notifyFocusAreaUpdate() {
        updatePreferenceTrampoline(3);
    }

    public void onBroadcastReceived(Context context, Intent intent) {
        if (intent != null && isAlive()) {
            if (CameraIntentManager.ACTION_VOICE_CONTROL.equals(intent.getAction())) {
                Log.d(TAG, "on Receive voice control broadcast action intent");
                String voiceControlAction = CameraIntentManager.getInstance(intent).getVoiceControlAction();
                this.mBroadcastIntent = intent;
                char c = 65535;
                if (voiceControlAction.hashCode() == 1270567718 && voiceControlAction.equals("CAPTURE")) {
                    c = 0;
                }
                if (c == 0) {
                    if (isBlockSnap()) {
                        Log.w(TAG, "on voice control: block snap");
                        this.mBroadcastIntent = null;
                        return;
                    }
                    onShutterButtonClick(getTriggerMode());
                    this.mBroadcastIntent = null;
                }
                CameraIntentManager.removeInstance(intent);
            } else {
                if (CameraIntentManager.ACTION_SPEECH_SHUTTER.equals(intent.getAction())) {
                    Log.d(TAG, "on Receive speech shutter broadcast action intent");
                    if (isBlockSnap()) {
                        Log.w(TAG, "on Speech shutter: block snap");
                        return;
                    }
                    onShutterButtonClick(110);
                }
            }
            super.onBroadcastReceived(context, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        super.onCameraOpened();
        initZoomMapControllerIfNeeded();
        initializeFocusManager();
        updatePreferenceTrampoline(UpdateConstant.SUPER_MOON_TYPES_INIT);
        startPreview();
        this.mOnResumeTime = SystemClock.uptimeMillis();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
    }

    /* access modifiers changed from: protected */
    public void onCapabilityChanged(CameraCapabilities cameraCapabilities) {
        super.onCapabilityChanged(cameraCapabilities);
        this.mUltraWideAELocked = false;
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setCharacteristics(cameraCapabilities);
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.onCapabilityChanged(cameraCapabilities);
        }
    }

    public void onCaptureCompleted(boolean z) {
        Log.k(3, TAG, String.format("onCaptureCompleted success=%s", new Object[]{Boolean.valueOf(true)}));
    }

    public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
    }

    public void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCaptureShutter: cameraState = ");
        sb.append(getCameraState());
        sb.append(", isParallel = ");
        sb.append(this.mEnableParallelSession);
        Log.d(str, sb.toString());
        onShutter(z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0177, code lost:
        if (com.android.camera.Util.isStringValueContained((java.lang.Object) com.android.camera.aiwatermark.util.WatermarkConstant.ITEM_TAG, r9.mCamera2Device.getCameraConfigs().getWaterMarkAppliedList()) != false) goto L_0x0179;
     */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x018a  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x018d  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x01bb  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x01e6  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0269  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x027c  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0295  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x02c0  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x02c9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, @NonNull CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
        List list;
        boolean z5;
        int i;
        this.mCamera2Device.updateFlashStateTimeLock();
        if (isDeparted()) {
            Log.w(TAG, "onCaptureStart: departed");
            if (C0122O00000o.instance().OOo0oOO()) {
                parallelTaskData.setRequireTuningData(true);
            }
            parallelTaskData.setAbandoned(true);
            return parallelTaskData;
        }
        parallelTaskData.setServiceStatusListener(this.mServiceStatusListener);
        int parallelType = parallelTaskData.getParallelType();
        if (!this.mEnabledPreviewThumbnail && ((!C0122O00000o.instance().OO0OoOO() && !DataRepository.dataItemRunning().isSuperNightCaptureWithKnownDuration()) || parallelTaskData.getBurstNum() <= 1)) {
            onShutter(z);
            CameraSettings.setPlayToneOnCaptureStart(true);
        }
        String str = null;
        if (CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()) {
            List faceWaterMarkInfos = ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).getFaceWaterMarkInfos();
            if (faceWaterMarkInfos != null && !faceWaterMarkInfos.isEmpty()) {
                list = new ArrayList(faceWaterMarkInfos);
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureStart: inputSize = ");
                sb.append(cameraSize);
                Log.k(3, str2, sb.toString());
                if ((isIn3OrMoreSatMode() || isInMultiSurfaceSatMode()) && (!cameraSize.equals(this.mPictureSize) || C0124O00000oO.isMTKPlatform())) {
                    this.mPictureSize = cameraSize;
                    updateOutputSize(cameraSize);
                }
                CameraSize cameraSize2 = this.mOutputPictureSize;
                Size sizeObject = cameraSize2 != null ? cameraSize.toSizeObject() : cameraSize2.toSizeObject();
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onCaptureStart: outputSize = ");
                sb2.append(sizeObject);
                Log.k(3, str3, sb2.toString());
                int i2 = this.mOutputPictureFormat;
                boolean isHeicImageFormat = CompatibilityUtils.isHeicImageFormat(i2);
                String str4 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("onCaptureStart: outputFormat = ");
                sb3.append(!isHeicImageFormat ? "HEIC" : "JPEG");
                Log.k(3, str4, sb3.toString());
                int clampQuality = clampQuality(CameraSettings.getEncodingQuality(false).toInteger(isHeicImageFormat));
                String str5 = TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("onCaptureStart: outputQuality = ");
                sb4.append(clampQuality);
                Log.k(3, str5, sb4.toString());
                CameraCharacteristics cameraCharacteristics = this.mCameraCapabilities.getCameraCharacteristics();
                this.mFocalLengths = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
                this.mApertures = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);
                Builder builder = new Builder(this.mPreviewSize.toSizeObject(), cameraSize.toSizeObject(), sizeObject, i2);
                if (parallelType == 1) {
                    CameraSize cameraSize3 = this.mSensorRawImageSize;
                    builder.setRawSize(cameraSize3.width, cameraSize3.height);
                }
                if (C0122O00000o.instance().OOoO000()) {
                    if (!Util.isStringValueContained((Object) "device", this.mCamera2Device.getCameraConfigs().getWaterMarkAppliedList())) {
                    }
                    z5 = true;
                    Location location = this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity) ? this.mLocation : null;
                    Builder filterId = builder.setHasDualWaterMark(CameraSettings.isDualCameraWaterMarkOpen()).setVendorWaterMark(z5).setMirror(isFrontMirror()).setLightingPattern(CameraSettings.getPortraitLightingPattern()).setFilterId(EffectController.getInstance().getEffectForSaving(false));
                    i = this.mOrientation;
                    if (-1 == i) {
                        i = 0;
                    }
                    Builder location2 = filterId.setOrientation(i).setJpegRotation(this.mJpegRotation).setShootRotation(this.mShootRotation).setShootOrientation(this.mShootOrientation).setSupportZeroDegreeOrientationImage(this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()).setLocation(location);
                    if (CameraSettings.isTimeWaterMarkOpen()) {
                        str = Util.getTimeWatermark(this.mActivity);
                    }
                    parallelTaskData.fillParameter(location2.setTimeWaterMarkString(str).setFaceWaterMarkList(list).setAgeGenderAndMagicMirrorWater(CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()).setFrontCamera(isFrontCamera()).setBokehFrontCamera(isPictureUseDualFrontCamera()).setAlgorithmName(this.mAlgorithmName).setPictureInfo(getPictureInfo()).setSuffix(getSuffix()).setTiltShiftMode(getTiltShiftMode()).setSaveGroupshotPrimitive(CameraSettings.isSaveGroushotPrimitiveOn()).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setJpegQuality(clampQuality).setPrefix(getPrefix()).setMoonMode(true).setMiMovieOpen(CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex)).setMajorAIWatermark(this.mMajorItem).setMinorAIWatermark(this.mMinorItem).build());
                    boolean z6 = z && !this.mEnabledPreviewThumbnail;
                    parallelTaskData.setNeedThumbnail(z6);
                    parallelTaskData.setCurrentModuleIndex(this.mModuleIndex);
                    CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
                    boolean z7 = cameraCapabilities == null && cameraCapabilities.isAdaptiveSnapshotSizeInSatModeSupported();
                    parallelTaskData.setAdaptiveSnapshotSize(z7);
                    parallelTaskData.setLiveShotTask(false);
                    if (C0122O00000o.instance().OOo0oOO()) {
                        parallelTaskData.setRequireTuningData(true);
                    }
                    String str6 = TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("onCaptureStart: isParallel = ");
                    sb5.append(this.mEnableParallelSession);
                    sb5.append(", shotType = ");
                    sb5.append(parallelTaskData.getParallelType());
                    Log.k(3, str6, sb5.toString());
                    if (this.mEnableParallelSession) {
                        beginParallelProcess(parallelTaskData, true);
                    }
                    if (CameraSettings.isHandGestureOpen()) {
                        Log.d(TAG, "send msg: reset hand gesture");
                        this.mHandler.removeMessages(57);
                        this.mHandler.sendEmptyMessageDelayed(57, 0);
                    }
                    return parallelTaskData;
                }
                z5 = false;
                if (this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity)) {
                }
                Builder filterId2 = builder.setHasDualWaterMark(CameraSettings.isDualCameraWaterMarkOpen()).setVendorWaterMark(z5).setMirror(isFrontMirror()).setLightingPattern(CameraSettings.getPortraitLightingPattern()).setFilterId(EffectController.getInstance().getEffectForSaving(false));
                i = this.mOrientation;
                if (-1 == i) {
                }
                Builder location22 = filterId2.setOrientation(i).setJpegRotation(this.mJpegRotation).setShootRotation(this.mShootRotation).setShootOrientation(this.mShootOrientation).setSupportZeroDegreeOrientationImage(this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()).setLocation(location);
                if (CameraSettings.isTimeWaterMarkOpen()) {
                }
                parallelTaskData.fillParameter(location22.setTimeWaterMarkString(str).setFaceWaterMarkList(list).setAgeGenderAndMagicMirrorWater(CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()).setFrontCamera(isFrontCamera()).setBokehFrontCamera(isPictureUseDualFrontCamera()).setAlgorithmName(this.mAlgorithmName).setPictureInfo(getPictureInfo()).setSuffix(getSuffix()).setTiltShiftMode(getTiltShiftMode()).setSaveGroupshotPrimitive(CameraSettings.isSaveGroushotPrimitiveOn()).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setJpegQuality(clampQuality).setPrefix(getPrefix()).setMoonMode(true).setMiMovieOpen(CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex)).setMajorAIWatermark(this.mMajorItem).setMinorAIWatermark(this.mMinorItem).build());
                if (z) {
                }
                parallelTaskData.setNeedThumbnail(z6);
                parallelTaskData.setCurrentModuleIndex(this.mModuleIndex);
                CameraCapabilities cameraCapabilities2 = this.mCameraCapabilities;
                if (cameraCapabilities2 == null) {
                }
                parallelTaskData.setAdaptiveSnapshotSize(z7);
                parallelTaskData.setLiveShotTask(false);
                if (C0122O00000o.instance().OOo0oOO()) {
                }
                String str62 = TAG;
                StringBuilder sb52 = new StringBuilder();
                sb52.append("onCaptureStart: isParallel = ");
                sb52.append(this.mEnableParallelSession);
                sb52.append(", shotType = ");
                sb52.append(parallelTaskData.getParallelType());
                Log.k(3, str62, sb52.toString());
                if (this.mEnableParallelSession) {
                }
                if (CameraSettings.isHandGestureOpen()) {
                }
                return parallelTaskData;
            }
        }
        list = null;
        String str22 = TAG;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("onCaptureStart: inputSize = ");
        sb6.append(cameraSize);
        Log.k(3, str22, sb6.toString());
        this.mPictureSize = cameraSize;
        updateOutputSize(cameraSize);
        CameraSize cameraSize22 = this.mOutputPictureSize;
        if (cameraSize22 != null) {
        }
        String str32 = TAG;
        StringBuilder sb22 = new StringBuilder();
        sb22.append("onCaptureStart: outputSize = ");
        sb22.append(sizeObject);
        Log.k(3, str32, sb22.toString());
        int i22 = this.mOutputPictureFormat;
        boolean isHeicImageFormat2 = CompatibilityUtils.isHeicImageFormat(i22);
        String str42 = TAG;
        StringBuilder sb32 = new StringBuilder();
        sb32.append("onCaptureStart: outputFormat = ");
        sb32.append(!isHeicImageFormat2 ? "HEIC" : "JPEG");
        Log.k(3, str42, sb32.toString());
        int clampQuality2 = clampQuality(CameraSettings.getEncodingQuality(false).toInteger(isHeicImageFormat2));
        String str52 = TAG;
        StringBuilder sb42 = new StringBuilder();
        sb42.append("onCaptureStart: outputQuality = ");
        sb42.append(clampQuality2);
        Log.k(3, str52, sb42.toString());
        CameraCharacteristics cameraCharacteristics2 = this.mCameraCapabilities.getCameraCharacteristics();
        this.mFocalLengths = (float[]) cameraCharacteristics2.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
        this.mApertures = (float[]) cameraCharacteristics2.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);
        Builder builder2 = new Builder(this.mPreviewSize.toSizeObject(), cameraSize.toSizeObject(), sizeObject, i22);
        if (parallelType == 1) {
        }
        if (C0122O00000o.instance().OOoO000()) {
        }
        z5 = false;
        if (this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity)) {
        }
        Builder filterId22 = builder2.setHasDualWaterMark(CameraSettings.isDualCameraWaterMarkOpen()).setVendorWaterMark(z5).setMirror(isFrontMirror()).setLightingPattern(CameraSettings.getPortraitLightingPattern()).setFilterId(EffectController.getInstance().getEffectForSaving(false));
        i = this.mOrientation;
        if (-1 == i) {
        }
        Builder location222 = filterId22.setOrientation(i).setJpegRotation(this.mJpegRotation).setShootRotation(this.mShootRotation).setShootOrientation(this.mShootOrientation).setSupportZeroDegreeOrientationImage(this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()).setLocation(location);
        if (CameraSettings.isTimeWaterMarkOpen()) {
        }
        parallelTaskData.fillParameter(location222.setTimeWaterMarkString(str).setFaceWaterMarkList(list).setAgeGenderAndMagicMirrorWater(CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()).setFrontCamera(isFrontCamera()).setBokehFrontCamera(isPictureUseDualFrontCamera()).setAlgorithmName(this.mAlgorithmName).setPictureInfo(getPictureInfo()).setSuffix(getSuffix()).setTiltShiftMode(getTiltShiftMode()).setSaveGroupshotPrimitive(CameraSettings.isSaveGroushotPrimitiveOn()).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setJpegQuality(clampQuality2).setPrefix(getPrefix()).setMoonMode(true).setMiMovieOpen(CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex)).setMajorAIWatermark(this.mMajorItem).setMinorAIWatermark(this.mMinorItem).build());
        if (z) {
        }
        parallelTaskData.setNeedThumbnail(z6);
        parallelTaskData.setCurrentModuleIndex(this.mModuleIndex);
        CameraCapabilities cameraCapabilities22 = this.mCameraCapabilities;
        if (cameraCapabilities22 == null) {
        }
        parallelTaskData.setAdaptiveSnapshotSize(z7);
        parallelTaskData.setLiveShotTask(false);
        if (C0122O00000o.instance().OOo0oOO()) {
        }
        String str622 = TAG;
        StringBuilder sb522 = new StringBuilder();
        sb522.append("onCaptureStart: isParallel = ");
        sb522.append(this.mEnableParallelSession);
        sb522.append(", shotType = ");
        sb522.append(parallelTaskData.getParallelType());
        Log.k(3, str622, sb522.toString());
        if (this.mEnableParallelSession) {
        }
        if (CameraSettings.isHandGestureOpen()) {
        }
        return parallelTaskData;
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        this.mHandler = new MainHandler(this, this.mActivity.getMainLooper());
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        onCameraOpened();
        this.mFirstCreateCapture = true;
    }

    public void onDestroy() {
        super.onDestroy();
        ZoomMapController zoomMapController = this.mZoomMapController;
        if (zoomMapController != null) {
            zoomMapController.onModuleDestroy();
        }
    }

    public void onExtraMenuVisibilityChange(boolean z) {
    }

    public void onFocusAreaChanged(int i, int i2) {
        Camera camera = this.mActivity;
        if (camera != null && !camera.isActivityPaused() && isAlive()) {
            Rect cropRegion = getCropRegion();
            Rect activeArraySize = getActiveArraySize();
            this.mActivity.getSensorStateManager().setFocusSensorEnabled(this.mFocusManager.getMeteringAreas(cropRegion, activeArraySize) != null);
            this.mCamera2Device.setAFRegions(this.mFocusManager.getMeteringOrFocusAreas(i, i2, cropRegion, activeArraySize, true));
            this.mCamera2Device.startFocus(FocusTask.create(1), this.mModuleIndex);
        }
    }

    public void onFocusStateChanged(FocusTask focusTask) {
        if (isFrameAvailable() && !isDeparted()) {
            int focusTrigger = focusTask.getFocusTrigger();
            if (focusTrigger == 1) {
                Log.v(TAG, String.format(Locale.ENGLISH, "FocusTime=%1$dms focused=%2$b", new Object[]{Long.valueOf(focusTask.getElapsedTime()), Boolean.valueOf(focusTask.isSuccess())}));
                if (!this.mFocusManager.isFocusingSnapOnFinish() && getCameraState() != 3) {
                    setCameraState(1);
                }
                this.mFocusManager.onFocusResult(focusTask);
                this.mActivity.getSensorStateManager().reset();
                if (focusTask.isSuccess() && this.m3ALocked) {
                    if (!C0122O00000o.instance().OOoOo0() && isZoomRatioBetweenUltraAndWide()) {
                        CameraCapabilities cameraCapabilities = this.mUltraCameraCapabilities;
                        if (cameraCapabilities != null) {
                            boolean isAFRegionSupported = cameraCapabilities.isAFRegionSupported();
                            String str = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onFocusStateChanged: isUltraFocusAreaSupported = ");
                            sb.append(isAFRegionSupported);
                            Log.d(str, sb.toString());
                            if (!isAFRegionSupported) {
                                this.mCamera2Device.setFocusMode(0);
                                this.mCamera2Device.setFocusDistance(0.0f);
                                this.mUltraWideAELocked = true;
                            }
                        }
                    }
                    this.mCamera2Device.lockExposure(true);
                }
            } else if (focusTrigger != 2 && focusTrigger == 3) {
                String str2 = null;
                if (focusTask.isFocusing()) {
                    this.mAFEndLogTimes = 0;
                    str2 = "onAutoFocusMoving start";
                } else if (this.mAFEndLogTimes == 0) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onAutoFocusMoving end. result=");
                    sb2.append(focusTask.isSuccess());
                    str2 = sb2.toString();
                    this.mAFEndLogTimes++;
                }
                if (Util.sIsDumpLog && str2 != null) {
                    Log.v(TAG, str2);
                }
                if (getCameraState() != 3 || focusTask.getFocusTrigger() == 3 ? !this.m3ALocked : focusTask.isSuccess()) {
                    this.mFocusManager.onFocusResult(focusTask);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
        if (r6 != 88) goto L_0x006e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        int i2;
        boolean z = false;
        if (!isFrameAvailable()) {
            return false;
        }
        if (!(i == 24 || i == 25)) {
            if (i == 27 || i == 66) {
                if (keyEvent.getRepeatCount() == 0) {
                    if (!Util.isFingerPrintKeyEvent(keyEvent)) {
                        i2 = 40;
                    } else if (CameraSettings.isFingerprintCaptureEnable()) {
                        i2 = 30;
                    }
                    performKeyClicked(i2, getString(R.string.pref_camera_volumekey_function_entryvalue_shutter), keyEvent.getRepeatCount(), true);
                }
                return true;
            } else if (i == 80) {
                if (keyEvent.getRepeatCount() == 0) {
                    onShutterButtonFocus(true, 1);
                }
                return true;
            } else if (i != 87) {
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
        if (i == 4 && ((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromKeyBack()) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    public void onLongPress(float f, float f2) {
        int i = (int) f;
        int i2 = (int) f2;
        if (isInTapableRect(i, i2)) {
            onSingleTapUp(i, i2, true);
            if (this.m3ALockSupported && this.mCamera2Device.getFocusMode() != AutoFocus.convertToFocusMode("manual")) {
                lockAEAF();
            }
            this.mMainProtocol.performHapticFeedback(0);
        }
    }

    public void onMeteringAreaChanged(int i, int i2) {
        Camera camera = this.mActivity;
        if (camera != null && !camera.isActivityPaused() && isAlive()) {
            Rect cropRegion = getCropRegion();
            Rect activeArraySize = getActiveArraySize();
            this.mActivity.getSensorStateManager().setFocusSensorEnabled(this.mFocusManager.getMeteringAreas(cropRegion, activeArraySize) != null);
            this.mCamera2Device.setAERegions(this.mFocusManager.getMeteringOrFocusAreas(i, i2, cropRegion, activeArraySize, false));
            this.mCamera2Device.resumePreview();
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        if (!this.isGradienterOn || this.mActivity.getSensorStateManager().isDeviceLying()) {
            setOrientation(i, i2);
        }
    }

    public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
    }

    public void onPictureTakenFinished(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPictureTakenFinished: succeed = ");
        sb.append(z);
        Log.k(3, str, sb.toString());
        if (z) {
            announceForAccessibility(R.string.accessibility_camera_shutter_finish);
            long currentTimeMillis = System.currentTimeMillis() - this.mCaptureStartTime;
            HashMap hashMap = new HashMap();
            hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
            trackGeneralInfo(hashMap, 1, false, null, this.mLocation != null, this.mCurrentAiScene);
            CameraStatUtils.trackTakePictureCost(currentTimeMillis, isFrontCamera(), this.mModuleIndex);
            ScenarioTrackUtil.trackCaptureTimeEnd();
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mCaptureStartTime(from onShutterButtonClick start to jpegCallback finished) = ");
            sb2.append(currentTimeMillis);
            sb2.append(d.H);
            Log.d(str2, sb2.toString());
            if (this.mLongPressedAutoFocus) {
                this.mLongPressedAutoFocus = false;
                this.mFocusManager.cancelLongPressedAutoFocus();
            }
            handleSaveFinishIfNeed();
        }
        if (!isKeptBitmapTexture() && !this.mMultiSnapStatus && this.mBlockQuickShot && (!CameraSettings.isGroupShotOn() || (CameraSettings.isGroupShotOn() && z))) {
            resetStatusToIdle();
        }
        this.mHandler.removeMessages(50);
    }

    public boolean onPictureTakenImageConsumed(Image image, TotalCaptureResult totalCaptureResult) {
        return false;
    }

    public void onPreviewLayoutChanged(Rect rect) {
        this.mActivity.onLayoutChange(rect);
    }

    public void onPreviewMetaDataUpdate(CaptureResult captureResult) {
        if (captureResult != null) {
            super.onPreviewMetaDataUpdate(captureResult);
            if (this.mZoomMapController != null) {
                this.mZoomMapController.setMapRect(CaptureResultParser.getZoomMapRIO(this.mCameraCapabilities, captureResult));
            }
            int asdDetectedModes = CaptureResultParser.getAsdDetectedModes(captureResult);
            if (this.mCurrentAiScene != asdDetectedModes) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("consumeAiSceneResult mCurrentAiScene:");
                sb.append(this.mCurrentAiScene);
                sb.append(" newResult:");
                sb.append(asdDetectedModes);
                Log.d(str, sb.toString());
                this.mCurrentAiScene = asdDetectedModes;
                this.mCamera2Device.setAiMoonEffectEnable(this.mCurrentAiScene == 35);
                resumePreviewInWorkThread();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0121  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onPreviewPixelsRead(byte[] bArr, int i, int i2) {
        boolean z;
        int i3 = i;
        int i4 = i2;
        Log.d(TAG, "onPreviewPixelsRead E");
        this.mActivity.getCameraScreenNail().animateCapture(getCameraRotation());
        playCameraSound(0);
        Bitmap createBitmap = Bitmap.createBitmap(i3, i4, Config.ARGB_8888);
        createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bArr));
        boolean z2 = isFrontCamera() && !isFrontMirror();
        synchronized (this.mCameraDeviceLock) {
            if (isAlive()) {
                if (isDeviceAlive()) {
                    Bitmap cropBitmap = Util.cropBitmap(createBitmap, this.mShootRotation, z2, (float) this.mOrientation, this.mModuleIndex == 165, CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex));
                    if (cropBitmap == null) {
                        Log.w(TAG, "onPreviewPixelsRead: bitmap is null!");
                        return;
                    }
                    byte[] bitmapData = Util.getBitmapData(cropBitmap, EncodingQuality.NORMAL.toInteger(false));
                    if (bitmapData == null) {
                        Log.w(TAG, "onPreviewPixelsRead: jpegData is null!");
                        return;
                    }
                    this.mCamera2Device.getCameraConfigs().getShotType();
                    int i5 = this.mOutputPictureFormat;
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onPreviewPixelsRead: isParallel = ");
                    sb.append(this.mEnableParallelSession);
                    sb.append(", shot2Gallery = ");
                    sb.append(this.mEnableShot2Gallery);
                    sb.append(", format = ");
                    sb.append(CompatibilityUtils.isHeicImageFormat(i5) ? "HEIC" : "JPEG");
                    sb.append(", data = ");
                    sb.append(bitmapData);
                    Log.d(str, sb.toString());
                    ParallelTaskData parallelTaskData = new ParallelTaskData(this.mActualCameraId, System.currentTimeMillis(), -1, this.mCamera2Device.getParallelShotSavePath(), this.mCaptureStartTime);
                    if (!this.mEnableParallelSession) {
                        if (!this.mEnableShot2Gallery) {
                            z = false;
                            parallelTaskData.setNeedThumbnail(z);
                            parallelTaskData.fillJpegData(bitmapData, 0);
                            parallelTaskData.fillParameter(new Builder(new Size(i3, i4), new Size(i3, i4), new Size(i3, i4), i5).setOrientation(this.mOrientation).build());
                            if (C0122O00000o.instance().OOo0oOO()) {
                                parallelTaskData.setRequireTuningData(true);
                            }
                            this.mActivity.getImageSaver().onParallelProcessFinish(parallelTaskData, null, null);
                            Log.d(TAG, "onPreviewPixelsRead X");
                            return;
                        }
                    }
                    z = true;
                    parallelTaskData.setNeedThumbnail(z);
                    parallelTaskData.fillJpegData(bitmapData, 0);
                    parallelTaskData.fillParameter(new Builder(new Size(i3, i4), new Size(i3, i4), new Size(i3, i4), i5).setOrientation(this.mOrientation).build());
                    if (C0122O00000o.instance().OOo0oOO()) {
                    }
                    this.mActivity.getImageSaver().onParallelProcessFinish(parallelTaskData, null, null);
                    Log.d(TAG, "onPreviewPixelsRead X");
                    return;
                }
            }
            Log.d(TAG, "onPreviewPixelsRead: module is dead");
        }
    }

    public void onPreviewSessionClosed(CameraCaptureSession cameraCaptureSession) {
    }

    public void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession) {
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        String str;
        StringBuilder sb;
        String str2;
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onPreviewSessionSuccess: ");
        sb2.append(Thread.currentThread().getName());
        sb2.append(" ");
        sb2.append(this);
        Log.d(str3, sb2.toString());
        if (cameraCaptureSession == null) {
            str = TAG;
            sb = new StringBuilder();
            str2 = "onPreviewSessionSuccess null session.";
        } else if (!isAlive()) {
            str = TAG;
            sb = new StringBuilder();
            str2 = "onPreviewSessionSuccess module not alive.";
        } else {
            if (!isKeptBitmapTexture()) {
                this.mHandler.sendEmptyMessage(9);
            }
            if (this.mEnableParallelSession) {
                ParallelDataZipper.getInstance().getHandler().post(new Runnable() {
                    public void run() {
                        SuperMoonModule.this.configParallelSession();
                    }
                });
            }
            previewWhenSessionSuccess();
            if (this.mActivity.getCameraIntentManager().checkCallerLegality() && !this.mActivity.isActivityPaused()) {
                if (!this.mActivity.getCameraIntentManager().isOpenOnly(this.mActivity)) {
                    this.mActivity.getIntent().removeExtra(CameraExtras.CAMERA_OPEN_ONLY);
                    if (!this.mActivity.isIntentPhotoDone()) {
                        this.mHandler.sendEmptyMessageDelayed(52, 1000);
                        this.mActivity.setIntnetPhotoDone(true);
                    }
                } else {
                    this.mActivity.getIntent().removeExtra(CameraExtras.TIMER_DURATION_SECONDS);
                }
            }
            return;
        }
        sb.append(str2);
        sb.append(Util.getCallers(1));
        Log.d(str, sb.toString());
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        keepScreenOnAwhile();
    }

    public void onReviewCancelClicked() {
    }

    public void onReviewDoneClicked() {
    }

    public void onShutterButtonClick(int i) {
        if (i == 100) {
            this.mActivity.onUserInteraction();
        } else if (i == 110) {
            this.mActivity.onUserInteraction();
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.hideRecommendDescTip(FragmentTopAlert.TIP_SPEECH_SHUTTER_DESC);
            }
        }
        int countDownTimes = getCountDownTimes(i);
        if (countDownTimes > 0) {
            startCount(countDownTimes, i);
            return;
        }
        PictureTakenParameter pictureTakenParameter = new PictureTakenParameter();
        TopAlert topAlert2 = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert2 != null) {
            pictureTakenParameter.isASDBacklitTip = topAlert2.isShowBacklightSelector();
        }
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            pictureTakenParameter.isASDPortraitTip = bottomPopupTips.containTips(R.string.recommend_portrait);
        }
        if (checkShutterCondition()) {
            this.mCamera2Device.setFixShotTimeEnabled(false);
            setTriggerMode(i);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            String str2 = "onShutterButtonClick ";
            sb.append(str2);
            sb.append(getCameraState());
            Log.u(str, sb.toString());
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(getCameraState());
            Log.k(3, str3, sb2.toString());
            this.mFocusManager.prepareCapture(false, 2);
            this.mFocusManager.doSnap();
            this.mFirstCreateCapture = false;
            if (this.mFocusManager.isFocusingSnapOnFinish()) {
                enableCameraControls(false);
            }
        }
    }

    public void onShutterButtonFocus(boolean z, int i) {
    }

    public boolean onShutterButtonLongClick() {
        if (isDoingAction()) {
            Log.d(TAG, "onShutterButtonLongClick: doing action");
            return false;
        } else if (this.mIsSatFallback == 0 || !shouldCheckSatFallbackState()) {
            this.mLongPressedAutoFocus = true;
            this.mMainProtocol.setFocusViewType(false);
            unlockAEAF();
            this.mFocusManager.requestAutoFocus();
            this.mActivity.getScreenHint().updateHint();
            return false;
        } else {
            Log.d(TAG, "onShutterButtonLongClick: sat fallback");
            return false;
        }
    }

    public void onShutterButtonLongClickCancel(boolean z) {
        if (!this.mLongPressedAutoFocus) {
            return;
        }
        if (z) {
            onShutterButtonClick(10);
            return;
        }
        this.mLongPressedAutoFocus = false;
        this.mFocusManager.cancelLongPressedAutoFocus();
    }

    public void onSingleTapUp(int i, int i2, boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSingleTapUp mPaused: ");
        sb.append(this.mPaused);
        sb.append("; mCamera2Device: ");
        sb.append(this.mCamera2Device);
        sb.append("; isInCountDown: ");
        sb.append(isInCountDown());
        sb.append("; getCameraState: ");
        sb.append(getCameraState());
        sb.append("; mMultiSnapStatus: ");
        sb.append(this.mMultiSnapStatus);
        sb.append("; SuperMoonModule: ");
        sb.append(this);
        Log.v(str, sb.toString());
        if (!this.mPaused && this.mCamera2Device != null && !hasCameraException() && this.mCamera2Device.isSessionReady() && this.mCamera2Device.isPreviewReady() && isInTapableRect(i, i2) && getCameraState() != 3 && getCameraState() != 4 && getCameraState() != 0 && !isInCountDown() && !this.mMultiSnapStatus && isFrameAvailable()) {
            if (!isFrontCamera() || !this.mActivity.isScreenSlideOff()) {
                BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
                if (backStack != null && !backStack.handleBackStackFromTapDown(i, i2)) {
                    this.mIsStartCount = false;
                    tryRemoveCountDownMessage();
                    if (this.mFocusAreaSupported || this.mMeteringAreaSupported) {
                        if (this.mObjectTrackingStarted) {
                            stopObjectTracking(true);
                        }
                        this.mMainProtocol.setFocusViewType(true);
                        Point point = new Point(i, i2);
                        mapTapCoordinate(point);
                        unlockAEAF();
                        setCameraState(2);
                        this.mFocusManager.onSingleTapUp(point.x, point.y, z);
                        if (!this.mFocusAreaSupported && this.mMeteringAreaSupported) {
                            this.mActivity.getSensorStateManager().reset();
                        }
                        CameraClickObservableImpl cameraClickObservableImpl = (CameraClickObservableImpl) ModeCoordinatorImpl.getInstance().getAttachProtocol(227);
                        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                        if (!z && cameraClickObservableImpl != null) {
                            cameraClickObservableImpl.subscribe(165);
                        }
                    }
                }
            }
        }
    }

    public void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.onPreviewComing();
        }
    }

    public void onThumbnailClicked(View view) {
        String str;
        String str2;
        if (this.mWaitSaveFinish) {
            str = TAG;
            str2 = "onThumbnailClicked: CannotGotoGallery...mWaitSaveFinish";
        } else {
            if (this.mEnableParallelSession || this.mEnableShot2Gallery) {
                if (isCannotGotoGallery()) {
                    str = TAG;
                    str2 = "onThumbnailClicked: CannotGotoGallery...";
                }
            } else if (isDoingAction()) {
                str = TAG;
                str2 = "onThumbnailClicked: DoingAction..";
            }
            if (this.mActivity.getThumbnailUpdater().getThumbnail() != null) {
                this.mActivity.gotoGallery();
            }
            return;
        }
        Log.d(str, str2);
    }

    public boolean onWaitingFocusFinished() {
        if (isBlockSnap() || !isAlive()) {
            return false;
        }
        if (this.mIsSatFallback == 0 || !shouldCheckSatFallbackState()) {
            this.mWaitingSnapshot = false;
            startNormalCapture(getTriggerMode());
        } else {
            this.mWaitingSnapshot = true;
            Log.w(TAG, "capture check: sat fallback");
        }
        return true;
    }

    public void onZoomingActionEnd(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onZoomingActionEnd(): ");
        sb.append(ZoomingAction.toString(i));
        Log.d(str, sb.toString());
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null && dualController.isZoomPanelVisible()) {
            dualController.updateZoomIndexsButton();
        }
    }

    public void onZoomingActionStart(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onZoomingActionStart(): ");
        sb.append(ZoomingAction.toString(i));
        Log.d(str, sb.toString());
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && topAlert.isExtraMenuShowing()) {
            topAlert.hideExtraMenu();
        }
        if (!isZoomEnabled()) {
            if (CameraSettings.isUltraPixelOn() && !C0122O00000o.instance().OOOOO00()) {
                BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                if (bottomPopupTips != null) {
                    bottomPopupTips.showTips(15, ComponentRunningUltraPixel.getNoSupportZoomTip(), 1);
                }
            }
            Log.d(TAG, "onZoomingActionStart(): zoom is currently disallowed");
            return;
        }
        CameraClickObservable cameraClickObservable = (CameraClickObservable) ModeCoordinatorImpl.getInstance().getAttachProtocol(227);
        if (cameraClickObservable != null) {
            cameraClickObservable.subscribe(168);
        }
    }

    /* access modifiers changed from: protected */
    public boolean onZoomingActionUpdate(float f, int i) {
        ZoomMapController zoomMapController = this.mZoomMapController;
        if (zoomMapController != null) {
            zoomMapController.onZoomRatioUpdate(f);
        }
        return super.onZoomingActionUpdate(f, i);
    }

    public void pausePreview() {
        Log.v(TAG, "pausePreview");
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.pausePreview();
        }
        setCameraState(0);
    }

    /* access modifiers changed from: protected */
    public void performKeyClicked(int i, String str, int i2, boolean z) {
        if (!this.mPaused && getCameraState() != 0) {
            if (!isDoingAction()) {
                restoreBottom();
            }
            if (i2 == 0) {
                if (z) {
                    onShutterButtonFocus(true, 1);
                    if (str.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_timer))) {
                        startCount(2, 20);
                    } else {
                        onShutterButtonClick(i);
                    }
                } else {
                    onShutterButtonFocus(false, 0);
                    if (this.mVolumeLongPress) {
                        this.mVolumeLongPress = false;
                        onShutterButtonLongClickCancel(false);
                    }
                }
            } else if (!isInCountDown() && z && !this.mVolumeLongPress) {
                this.mVolumeLongPress = onShutterButtonLongClick();
                if (!this.mVolumeLongPress && this.mLongPressedAutoFocus) {
                    this.mVolumeLongPress = true;
                }
            }
        }
    }

    public void playFocusSound(int i) {
        if (!this.mMediaRecorderRecording) {
            playCameraSound(i);
        }
    }

    public void reShowMoon() {
    }

    public void registerProtocol() {
        super.registerProtocol();
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(193, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(195, this);
        getActivity().getImplFactory().initAdditional(getActivity(), 164, 174, 234, 227, 235, 212, 254);
    }

    /* access modifiers changed from: protected */
    public void resetStatusToIdle() {
        Log.d(TAG, "reset Status to Idle");
        setCameraState(1);
        enableCameraControls(true);
        this.mBlockQuickShot = false;
    }

    public void resumePreview() {
        Log.v(TAG, "resumePreview");
        previewWhenSessionSuccess();
        this.mBlockQuickShot = !CameraSettings.isCameraQuickShotEnable();
    }

    public void setFrameAvailable(boolean z) {
        super.setFrameAvailable(z);
        if (z && this.mActivity != null && CameraSettings.isCameraSoundOpen()) {
            this.mActivity.loadCameraSound(1);
            this.mActivity.loadCameraSound(0);
            this.mActivity.loadCameraSound(4);
            this.mActivity.loadCameraSound(5);
            this.mActivity.loadCameraSound(7);
            this.mActivity.loadCameraSound(2);
            this.mActivity.loadCameraSound(3);
        }
        if (z) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.releaseFakeSurfaceIfNeed();
            }
        }
        if (z && CameraSettings.isCameraLyingHintOn()) {
            this.mHandler.post(new C0375O000o0O0(this));
        }
    }

    public boolean shouldCaptureDirectly() {
        return false;
    }

    public void startAiLens() {
    }

    public void startFocus() {
        if (isDeviceAlive() && isFrameAvailable()) {
            if (this.mFocusOrAELockSupported) {
                this.mCamera2Device.startFocus(FocusTask.create(1), this.mModuleIndex);
            } else {
                this.mCamera2Device.resumePreview();
            }
        }
    }

    public void startPreview() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setActivityHashCode(this.mActivity.hashCode());
            this.mCamera2Device.setFocusCallback(this);
            this.mCamera2Device.setMetaDataCallback(this);
            this.mCamera2Device.setErrorCallback(this.mErrorCallback);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startPreview: set PictureSize with ");
            sb.append(this.mPictureSize);
            Log.d(str, sb.toString());
            this.mCamera2Device.setPictureSize(this.mPictureSize);
            this.mCamera2Device.setPictureFormat(this.mOutputPictureFormat);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("startPreview: set PictureFormat to ");
            sb2.append(CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? "HEIC" : "JPEG");
            Log.d(str2, sb2.toString());
            SurfaceTexture surfaceTexture = this.mActivity.getCameraScreenNail().getSurfaceTexture();
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("startPreview: surfaceTexture = ");
            sb3.append(surfaceTexture);
            Log.d(str3, sb3.toString());
            if (surfaceTexture != null) {
                this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            }
            Surface surface = null;
            Surface surface2 = surfaceTexture != null ? new Surface(surfaceTexture) : null;
            this.mConfigRawStream = false;
            ZoomMapController zoomMapController = this.mZoomMapController;
            if (zoomMapController != null) {
                surface = zoomMapController.createZoomMapSurfaceIfNeeded();
            }
            this.mCamera2Device.startPreviewSession(surface2, 0, 0, surface, getOperatingMode(), this.mEnableParallelSession, this);
        }
    }

    public void tryRemoveCountDownMessage() {
        Disposable disposable = this.mCountdownDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mCountdownDisposable.dispose();
            this.mCountdownDisposable = null;
            this.mHandler.post(new Runnable() {
                public void run() {
                    Log.d(SuperMoonModule.TAG, "run: hide delay number in main thread");
                    SuperMoonModule.this.mTopAlert.hideDelayNumber();
                }
            });
        }
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        Log.d(TAG, "unRegisterProtocol");
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(193, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(195, this);
        getActivity().getImplFactory().detachAdditional();
    }
}
