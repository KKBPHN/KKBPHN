package com.android.camera.module;

import O00000Oo.O00000Oo.O000000o.O00000o;
import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Range;
import android.util.Size;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import com.android.camera.AutoLockManager;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraIntentManager.CameraExtras;
import com.android.camera.CameraPreferenceActivity;
import com.android.camera.CameraScreenNail;
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
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.aiwatermark.chain.AbstractPriorityChain;
import com.android.camera.aiwatermark.chain.PriorityChainFactory;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.constant.AutoFocus;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.constant.CameraScene;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.constant.UpdateConstant.UpdateType;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentConfigBokeh;
import com.android.camera.data.data.config.ComponentConfigFlash;
import com.android.camera.data.data.config.ComponentConfigHdr;
import com.android.camera.data.data.config.ComponentRunningMacroMode;
import com.android.camera.data.data.config.ComponentRunningUltraPixel;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.data.data.runing.ComponentRunningTiltValue;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.dualvideo.view.TouchEventView;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FaceAnalyzeInfo;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.GoogleLensFragment;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.fragment.top.FragmentTopConfig;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.encoder.LiveMediaRecorder;
import com.android.camera.module.encoder.LiveMediaRecorder.EncoderListener;
import com.android.camera.module.impl.component.CameraClickObservableImpl;
import com.android.camera.module.loader.FuncFaceDetect;
import com.android.camera.module.loader.FunctionAsdSceneDetect;
import com.android.camera.module.loader.FunctionHdrDetect;
import com.android.camera.module.loader.FunctionLivePhoto;
import com.android.camera.module.loader.FunctionNearRangeTip;
import com.android.camera.module.loader.FunctionParseAiScene;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.android.camera.module.loader.FunctionParseHistogramStats;
import com.android.camera.module.loader.FunctionParseSuperNight;
import com.android.camera.module.loader.PredicateFilterAiScene;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.module.loader.camera2.FocusManager2.Listener;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.module.loader.camera2.ParallelSnapshotManager;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera.preferences.CameraSettingPreferences;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AIWatermarkDetect;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.AutoHibernation;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CameraClickObservable;
import com.android.camera.protocol.ModeProtocol.CameraClickObservable.ClickObserver;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.FullScreenProtocol;
import com.android.camera.protocol.ModeProtocol.IDCardModeProtocol;
import com.android.camera.protocol.ModeProtocol.MagneticSensorDetect;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.android.camera.protocol.ModeProtocol.ManuallyAdjust;
import com.android.camera.protocol.ModeProtocol.MiAsdDetect;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.SnapShotIndicator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.TopConfigProtocol;
import com.android.camera.scene.FunctionMiAlgoASDEngine;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.CaptureAttr;
import com.android.camera.statistic.MistatsConstants.CaptureSence;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import com.android.camera.statistic.MistatsConstants.LiveShotAttr;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.Setting;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.statistic.MistatsWrapper.PictureTakenParameter;
import com.android.camera.statistic.ScenarioTrackUtil;
import com.android.camera.storage.ImageSaver;
import com.android.camera.storage.Storage;
import com.android.camera.timerburst.CameraTimer;
import com.android.camera.timerburst.TimerBurstController;
import com.android.camera.ui.ObjectView.ObjectViewListener;
import com.android.camera.ui.RotateTextToast;
import com.android.camera.ui.zoom.ZoomingAction;
import com.android.camera.zoommap.ZoomMapController;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.ASDSceneCallback;
import com.android.camera2.Camera2Proxy.AnchorPreviewCallback;
import com.android.camera2.Camera2Proxy.BeautyBodySlimCountCallback;
import com.android.camera2.Camera2Proxy.CameraMetaDataCallback;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.FaceDetectionCallback;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.Camera2Proxy.HDRStatus;
import com.android.camera2.Camera2Proxy.HdrCheckerCallback;
import com.android.camera2.Camera2Proxy.IFirstCaptureFocus;
import com.android.camera2.Camera2Proxy.LivePhotoResultCallback;
import com.android.camera2.Camera2Proxy.MagneticDetectedCallback;
import com.android.camera2.Camera2Proxy.NearRangeModeCallback;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.Camera2Proxy.PictureCallbackWrapper;
import com.android.camera2.Camera2Proxy.ScreenLightCallback;
import com.android.camera2.Camera2Proxy.SuperNightCallback;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CameraConfigs;
import com.android.camera2.CameraHardwareFace;
import com.android.camera2.CameraPreferredMode;
import com.android.camera2.CaptureResultParser;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import com.android.camera2.vendortag.struct.MarshalQueryableChiRect.ChiRect;
import com.android.camera2.vendortag.struct.MarshalQueryableDxoAsdScene;
import com.android.camera2.vendortag.struct.MarshalQueryableSuperNightExif.SuperNightExif;
import com.android.camera2.vendortag.struct.SuperNightEvValue;
import com.android.gallery3d.exif.ExifHelper;
import com.android.gallery3d.ui.GLCanvas;
import com.android.zxing.CacheImageDecoder;
import com.android.zxing.Decoder;
import com.android.zxing.DocumentDecoder;
import com.android.zxing.PreviewDecodeManager;
import com.android.zxing.PreviewImage;
import com.google.android.apps.photos.api.PhotosOemApi;
import com.miui.filtersdk.filter.helper.FilterFactory;
import com.miui.filtersdk.filter.helper.FilterType;
import com.xiaomi.camera.base.CameraDeviceUtil;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.BaseBoostFramework;
import com.xiaomi.camera.core.BoostFrameworkImpl;
import com.xiaomi.camera.core.ParallelDataZipper;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.ParallelTaskData.OnParallelTaskDataAddToProcessorListener;
import com.xiaomi.camera.core.ParallelTaskDataParameter;
import com.xiaomi.camera.core.ParallelTaskDataParameter.Builder;
import com.xiaomi.camera.core.PictureInfo;
import com.xiaomi.camera.liveshot.CircularMediaRecorder;
import com.xiaomi.camera.liveshot.LivePhotoResult;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.engine.BufferFormat;
import com.xiaomi.engine.GraphDescriptorBean;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess.EnhanceType;
import com.xiaomi.ocr.sdk.imgprocess.DocumentProcess.RotateFlags;
import com.xiaomi.protocol.ISessionStatusCallBackListener;
import com.xiaomi.protocol.ISessionStatusCallBackListener.Stub;
import com.xiaomi.stat.d;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import miui.text.ExtraTextUtils;

@TargetApi(21)
public class Camera2Module extends BaseModule implements Listener, ObjectViewListener, CameraMetaDataCallback, CameraAction, TopConfigProtocol, CameraPreviewCallback, HdrCheckerCallback, ScreenLightCallback, PictureCallback, FaceDetectionCallback, FocusCallback, BeautyBodySlimCountCallback, SuperNightCallback, LivePhotoResultCallback, MagneticDetectedCallback, NearRangeModeCallback, ASDSceneCallback, IDxoAsdSceneDetected, IFirstCaptureFocus, AnchorPreviewCallback {
    private static final int BURST_SHOOTING_DELAY = 0;
    private static final long CAPTURE_DURATION_THRESHOLD = 12000;
    private static final boolean DEBUG_ENABLE_DYNAMIC_HHT_FAST_SHOT = SystemProperties.getBoolean("debug.vendor.camera.app.dynamic.hht.quickshot.enable", true);
    private static final int MAX_HEIC_BURST_CAPTURE_COUNT = 50;
    private static final float MOON_AF_DISTANCE = 0.5f;
    private static final int MSG_START_RECORDING = 4097;
    private static final int PARALLEL_PERFORMANCE_SETTING = 0;
    private static final int PARALLEL_QUALITY_SETTING = 1;
    private static final int PARALLEL_QUEUE_SIZE = 2;
    private static final int REQUEST_CROP = 1000;
    /* access modifiers changed from: private */
    public static final String TAG = "Camera2Module";
    private static final int UW_MAX_BURST_SHOT_NUM = 30;
    private static boolean mIsBeautyFrontOn = false;
    private static final String sTempCropFilename = "crop-temp";
    private final CameraSize SIZE_108M = new CameraSize(12032, 9024);
    /* access modifiers changed from: private */
    public float[] curGyroscope;
    private volatile boolean isDetectedInHdr;
    /* access modifiers changed from: private */
    public volatile boolean isResetFromMutex = false;
    private boolean isSilhouette;
    /* access modifiers changed from: private */
    public float[] lastGyroscope;
    private boolean m3ALocked;
    private float mAECLux;
    private int mAFEndLogTimes;
    private boolean mAIWatermarkEnable = false;
    private Disposable mAiSceneDisposable;
    private boolean mAiSceneEnabled;
    /* access modifiers changed from: private */
    public FlowableEmitter mAiSceneFlowableEmitter;
    /* access modifiers changed from: private */
    public String mAlgorithmName;
    private float[] mApertures;
    private ASDScene[] mAsdScenes;
    private boolean mAutoHDRTargetState;
    private BeautyValues mBeautyValues;
    /* access modifiers changed from: private */
    public boolean mBlockQuickShot = (!CameraSettings.isCameraQuickShotEnable());
    private Intent mBroadcastIntent;
    /* access modifiers changed from: private */
    public Disposable mBurstDisposable;
    /* access modifiers changed from: private */
    public ObservableEmitter mBurstEmitter;
    private long mBurstNextDelayTime = 0;
    /* access modifiers changed from: private */
    public long mBurstStartTime;
    private CacheImageDecoder mCacheImageDecoder;
    private ClickObserver mCameraClickObserverAction = new ClickObserver() {
        public void action() {
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.directlyHideTips();
            }
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.alertAiDetectTipHint(8, 0, 0);
            }
        }

        public int getObserver() {
            return 161;
        }
    };
    private final Object mCameraDeviceLock = new Object();
    private CameraTimer mCameraTimer;
    private long mCaptureStartTime;
    private SuperNightExif mCaptureSuperNightExifInfo;
    private String mCaptureWaterMarkStr;
    private AbstractPriorityChain mChain = null;
    private CircularMediaRecorder mCircularMediaRecorder = null;
    private final Object mCircularMediaRecorderStateLock = new Object();
    private CountDownTimer mCountDownTimer;
    private BaseBoostFramework mCpuBoost;
    private String mCropValue;
    private int mCurrentAiScene;
    private int mCurrentAsdScene = -1;
    private int mCurrentDetectedScene;
    private String mDebugFaceInfos;
    /* access modifiers changed from: private */
    public boolean mEnableParallelSession;
    private boolean mEnableShot2Gallery;
    private boolean mEnabledPreviewThumbnail;
    private boolean mEnteringMoonMode;
    protected boolean mFaceDetected;
    private boolean mFaceDetectionEnabled;
    private boolean mFaceDetectionStarted;
    private FaceAnalyzeInfo mFaceInfo;
    private boolean mFirstCreateCapture;
    /* access modifiers changed from: private */
    public int mFixedShot2ShotTime = -1;
    private boolean mFlashChangeCapture;
    private float[] mFocalLengths;
    /* access modifiers changed from: private */
    public FocusManager2 mFocusManager;
    private FunctionParseAiScene mFunctionParseAiScene;
    private boolean mHHTDisabled;
    private boolean mHasAiSceneFilterEffect;
    private boolean mHdrCheckEnabled;
    private HdrTrigger mHdrTrigger;
    private Disposable mHistogramDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mHistogramEmitter;
    private String[] mIDCardPaths = new String[2];
    private boolean mIsAiConflict;
    private volatile boolean mIsAiShutterOn = false;
    private boolean mIsBeautyBodySlimOn;
    private boolean mIsCurrentLensEnabled;
    private boolean mIsFaceConflict;
    private boolean mIsGenderAgeOn;
    private volatile boolean mIsGoogleLensAvailable;
    private boolean mIsISORight4HWMFNR = false;
    /* access modifiers changed from: private */
    public boolean mIsImageCaptureIntent;
    private boolean mIsInHDR;
    private boolean mIsLLSNeeded;
    private boolean mIsMacroModeEnable;
    private boolean mIsMagicMirrorOn;
    private boolean mIsMicrophoneEnabled = true;
    /* access modifiers changed from: private */
    public boolean mIsMoonMode;
    private boolean mIsNearRangeMode;
    private boolean mIsNearRangeModeUITip;
    private boolean mIsNeedNightHDR;
    private boolean mIsParallelParameterSet = false;
    private boolean mIsPortraitLightingOn;
    private boolean mIsReprocessorCustomized;
    private boolean mIsSaveCaptureImage;
    /* access modifiers changed from: private */
    public int mIsShowLyingDirectHintStatus = -1;
    private boolean mIsShutterLongClickRecording;
    /* access modifiers changed from: private */
    public boolean mIsStartCount;
    private boolean mIsUltraWideConflict;
    /* access modifiers changed from: private */
    public int mJpegRotation;
    private boolean mKeepBitmapTexture;
    private long mLastCaptureTime;
    private long mLastChangeSceneTime = 0;
    private String mLastFlashMode;
    private String mLastHdrMode;
    private int mLightFilterId = FilterInfo.FILTER_ID_NONE;
    private LiveMediaRecorder mLiveMediaRecorder;
    private Queue mLivePhotoQueue = new LinkedBlockingQueue(120);
    private boolean mLiveShotEnabled;
    /* access modifiers changed from: private */
    public Location mLocation;
    private boolean mLongPressedAutoFocus;
    private MagneticSensorDetect mMagneticSensorDetect;
    private Object mMateDataParserLock = new Object();
    protected int mMaxVideoDurationInMs = 15000;
    private final EncoderListener mMediaEncoderListener = new EncoderListener(this);
    private Disposable mMetaDataDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mMetaDataFlowableEmitter;
    private boolean mMotionDetected;
    /* access modifiers changed from: private */
    public boolean mMultiSnapStatus = false;
    /* access modifiers changed from: private */
    public boolean mMultiSnapStopRequest = false;
    private boolean mNeedAutoFocus;
    private int mNormalFilterId;
    /* access modifiers changed from: private */
    public long mOnResumeTime;
    private int mOperatingMode;
    private int mParallelPerformance = 0;
    private int mParallelQuality = 0;
    private boolean mParallelSessionConfigured = false;
    private final Object mParallelSessionLock = new Object();
    private boolean mPendingMultiCapture;
    private ArrayList mPendingSaveTaskList = new ArrayList();
    private SuperNightExif mPreviewSuperNightExifInfo;
    private boolean mQuickCapture;
    private boolean mQuickShotAnimateEnable = false;
    private int mRawCallbackType;
    /* access modifiers changed from: private */
    public int mReceivedJpegCallbackNum = 0;
    protected int mRecordingStartDelay = 250;
    protected long mRecordingStartTime;
    private long mRequestStartTime;
    private RotateFlags mRotateFlags = RotateFlags.ROTATE_90;
    private Uri mSaveUri;
    private String mSceneMode;
    private float mSeLuxThreshold;
    private SensorStateListener mSensorStateListener = new SensorStateListener() {
        private TopAlert mTopAlert;

        public boolean isWorking() {
            return Camera2Module.this.isAlive() && Camera2Module.this.getCameraState() != 0;
        }

        public void notifyDevicePostureChanged() {
        }

        public void onDeviceBecomeStable() {
        }

        public void onDeviceBeginMoving() {
            if (!Camera2Module.this.mPaused && CameraSettings.isEdgePhotoEnable()) {
                Camera2Module.this.mActivity.getEdgeShutterView().onDeviceMoving();
            }
        }

        public void onDeviceKeepMoving(double d) {
            if (!Camera2Module.this.mPaused && Camera2Module.this.mFocusManager != null && !Camera2Module.this.mMultiSnapStatus && !Camera2Module.this.is3ALocked() && !Camera2Module.this.mMainProtocol.isEvAdjusted(true) && !Camera2Module.this.mIsMoonMode) {
                Camera2Module.this.mFocusManager.onDeviceKeepMoving(d);
            }
        }

        public void onDeviceKeepStable() {
        }

        public void onDeviceLieChanged(boolean z) {
            Handler handler;
            Message message;
            long j;
            if (!Camera2Module.this.mPaused) {
                int access$5300 = Camera2Module.this.mIsShowLyingDirectHintStatus;
                Camera2Module camera2Module = Camera2Module.this;
                int i = camera2Module.mOrientationCompensation;
                if (access$5300 != (z ? 1 : 0) + i) {
                    camera2Module.mIsShowLyingDirectHintStatus = i + z;
                    Camera2Module.this.mHandler.removeMessages(58);
                    if (this.mTopAlert == null) {
                        this.mTopAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    }
                    TopAlert topAlert = this.mTopAlert;
                    if (topAlert != null ? topAlert.isContainAlertRecommendTip(R.string.dirty_tip_toast, R.string.pic_flaw_blink_one, R.string.pic_flaw_blink_more, R.string.pic_flaw_cover) : false) {
                        z = false;
                    }
                    if (z) {
                        Camera2Module camera2Module2 = Camera2Module.this;
                        Handler handler2 = camera2Module2.mHandler;
                        handler2.sendMessageDelayed(handler2.obtainMessage(58, 1, camera2Module2.mOrientationCompensation), 400);
                        Camera2Module camera2Module3 = Camera2Module.this;
                        handler = camera2Module3.mHandler;
                        message = handler.obtainMessage(58, 0, camera2Module3.mOrientationCompensation);
                        j = 5000;
                    } else {
                        Camera2Module camera2Module4 = Camera2Module.this;
                        handler = camera2Module4.mHandler;
                        message = handler.obtainMessage(58, 0, camera2Module4.mOrientationCompensation);
                        j = 500;
                    }
                    handler.sendMessageDelayed(message, j);
                }
            }
        }

        public void onDeviceOrientationChanged(float f, boolean z) {
            Camera2Module camera2Module = Camera2Module.this;
            camera2Module.mDeviceRotation = !z ? f : (float) camera2Module.mOrientation;
            if (Camera2Module.this.getCameraState() != 3 || Camera2Module.this.isGradienterOn) {
                EffectController instance = EffectController.getInstance();
                Camera2Module camera2Module2 = Camera2Module.this;
                instance.setDeviceRotation(z, Util.getShootRotation(camera2Module2.mActivity, camera2Module2.mDeviceRotation));
            }
            Camera2Module.this.mHandler.removeMessages(33);
            if (!Camera2Module.this.mPaused && !z && f != -1.0f) {
                int roundOrientation = Util.roundOrientation(Math.round(f), Camera2Module.this.mOrientation);
                Camera2Module.this.mHandler.obtainMessage(33, roundOrientation, (Util.getDisplayRotation(Camera2Module.this.mActivity) + roundOrientation) % m.cQ).sendToTarget();
            }
        }

        public void onDeviceRotationChanged(float[] fArr) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            int type = sensorEvent.sensor.getType();
            if (type == 4) {
                Camera2Module camera2Module = Camera2Module.this;
                camera2Module.lastGyroscope = camera2Module.curGyroscope;
                Camera2Module.this.curGyroscope = sensorEvent.values;
            } else if (type == 14) {
                MagneticSensorDetect magneticSensorDetect = (MagneticSensorDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(2576);
                if (magneticSensorDetect != null) {
                    magneticSensorDetect.onMagneticSensorChanged(sensorEvent);
                }
            }
        }
    };
    private ServiceStatusListener mServiceStatusListener;
    private ISessionStatusCallBackListener mSessionStatusCallbackListener = new Stub() {
        public void onSessionStatusFlawResultData(int i, final int i2) {
            String access$400 = Camera2Module.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("resultId:");
            sb.append(i);
            sb.append(",flawResult:");
            sb.append(i2);
            Log.d(access$400, sb.toString());
            if (HybridZoomingSystem.isZoomRatioNone(Camera2Module.this.getZoomRatio(), Camera2Module.this.isFrontCamera()) && !CameraSettings.isMacroModeEnabled(Camera2Module.this.getModuleIndex())) {
                final FragmentTopConfig fragmentTopConfig = (FragmentTopConfig) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (fragmentTopConfig == null || !fragmentTopConfig.isCurrentRecommendTipText(R.string.super_night_hint)) {
                    Camera2Proxy camera2Proxy = Camera2Module.this.mCamera2Device;
                    if (camera2Proxy == null || !camera2Proxy.isCaptureBusy(true)) {
                        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                            public void run() {
                                String str;
                                HashMap hashMap = new HashMap();
                                int i = i2;
                                if (i != 0) {
                                    String str2 = CaptureAttr.PARAM_ASD_FLAW_TIP;
                                    if (i == 1) {
                                        FragmentTopConfig fragmentTopConfig = fragmentTopConfig;
                                        if (fragmentTopConfig != null) {
                                            fragmentTopConfig.alertAiDetectTipHint(0, R.string.pic_flaw_cover, 3000);
                                        }
                                        str = CaptureAttr.VALUE_ASD_FLAW_COVER;
                                    } else if (i == 2) {
                                        FragmentTopConfig fragmentTopConfig2 = fragmentTopConfig;
                                        if (fragmentTopConfig2 != null) {
                                            fragmentTopConfig2.alertAiDetectTipHint(0, R.string.pic_flaw_blink_one, 3000);
                                        }
                                        str = CaptureAttr.VALUE_ASD_FLAW_BLINK_ONE;
                                    } else if (i == 3) {
                                        FragmentTopConfig fragmentTopConfig3 = fragmentTopConfig;
                                        if (fragmentTopConfig3 != null) {
                                            fragmentTopConfig3.alertAiDetectTipHint(0, R.string.pic_flaw_blink_more, 3000);
                                        }
                                        str = CaptureAttr.VALUE_ASD_FLAW_BLINK_MORE;
                                    }
                                    hashMap.put(str2, str);
                                }
                                MistatsWrapper.mistatEvent(FeatureName.KEY_COMMON_TIPS, hashMap);
                            }
                        });
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public int mShootOrientation;
    /* access modifiers changed from: private */
    public float mShootRotation;
    private boolean mShouldDoMFNR;
    private boolean mShowLLSHint;
    private boolean mShowSuperNightHint;
    private long mShutterCallbackTime;
    private long mShutterLag;
    private Integer mSpecShotMode;
    /* access modifiers changed from: private */
    public Disposable mSuperNightDisposable;
    private Consumer mSuperNightEventConsumer;
    /* access modifiers changed from: private */
    public PublishSubject mSuperNightEventEmitter;
    private boolean mSupportAnchorFrameAsThumbnail;
    private boolean mSupportFlashHDR;
    private boolean mSupportShotBoost;
    /* access modifiers changed from: private */
    public int mTotalJpegCallbackNum = 1;
    private TouchEventView mTouchEventView;
    private volatile boolean mUltraWideAELocked;
    /* access modifiers changed from: private */
    public boolean mUpdateImageTitle = false;
    private boolean mUpscaleImageWithSR;
    private CameraSize mVideoSize;
    private boolean mVolumeLongPress = false;
    /* access modifiers changed from: private */
    public volatile boolean mWaitSaveFinish;
    /* access modifiers changed from: private */
    public boolean mWaitingSuperNightResult;
    private WatermarkItem mWatermarkItem;
    private ZoomMapController mZoomMapController;

    class AsdSceneConsumer implements Consumer {
        private WeakReference mModule;

        public AsdSceneConsumer(BaseModule baseModule) {
            this.mModule = new WeakReference(baseModule);
        }

        public void accept(Integer num) {
            WeakReference weakReference = this.mModule;
            if (weakReference != null && weakReference.get() != null) {
                BaseModule baseModule = (BaseModule) this.mModule.get();
                if (baseModule instanceof Camera2Module) {
                    ((Camera2Module) baseModule).consumeAsdSceneResult(num.intValue());
                }
            }
        }
    }

    class HdrTrigger {
        private static final int HDR_MODE_CHANGE_TIME_INTERVAL = 800;
        private boolean autoHdrModeChange;
        private long hdrModeChangeTime;
        private String userSelectedHdrMode;

        private HdrTrigger() {
        }

        private void updateHdrModeChangeTime() {
            this.hdrModeChangeTime = System.currentTimeMillis();
        }

        public boolean isUpdateHdrTip() {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.autoHdrModeChange && currentTimeMillis - this.hdrModeChangeTime < 800) {
                return false;
            }
            this.autoHdrModeChange = false;
            return true;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:3:0x0012, code lost:
            if ("normal".equals(r2.userSelectedHdrMode) != false) goto L_0x0014;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void setHdrModeChange(String str) {
            if (!"on".equals(this.userSelectedHdrMode)) {
            }
            if ("auto".equals(str)) {
                this.autoHdrModeChange = true;
                updateHdrModeChangeTime();
                Log.c(Camera2Module.TAG, "Cut from HDR_ON to HDR_AUTO，autoHdrModeChange = true");
                this.userSelectedHdrMode = str;
            }
            this.autoHdrModeChange = false;
            this.userSelectedHdrMode = str;
        }
    }

    final class JpegQuickPictureCallback extends PictureCallbackWrapper {
        String mBurstShotTitle;
        boolean mDropped;
        Location mLocation;
        String mPressDownTitle;
        int mSavedJpegCallbackNum;

        public JpegQuickPictureCallback(Location location) {
            this.mLocation = location;
        }

        private String getBurstShotTitle() {
            if (Camera2Module.this.mUpdateImageTitle) {
                String str = this.mBurstShotTitle;
                if (str != null && this.mSavedJpegCallbackNum == 1) {
                    this.mPressDownTitle = str;
                    this.mBurstShotTitle = null;
                }
            }
            if (this.mBurstShotTitle == null) {
                long currentTimeMillis = System.currentTimeMillis();
                this.mBurstShotTitle = Util.createJpegName(currentTimeMillis);
                if (this.mBurstShotTitle.length() != 19) {
                    this.mBurstShotTitle = Util.createJpegName(currentTimeMillis + 1000);
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(this.mBurstShotTitle);
            sb.append("_BURST");
            sb.append(this.mSavedJpegCallbackNum);
            return sb.toString();
        }

        public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
            int i;
            int i2;
            if (!Camera2Module.this.mPaused && bArr != null && Camera2Module.this.mReceivedJpegCallbackNum < Camera2Module.this.mTotalJpegCallbackNum && Camera2Module.this.mMultiSnapStatus) {
                if (this.mSavedJpegCallbackNum == 1 && !Camera2Module.this.mMultiSnapStopRequest) {
                    Camera2Module.this.mActivity.getImageSaver().updateImage(getBurstShotTitle(), this.mPressDownTitle);
                }
                if (Storage.isLowStorageAtLastPoint()) {
                    if (Camera2Module.this.mMultiSnapStatus) {
                        Camera2Module.this.stopMultiSnap();
                    }
                    Log.d(Camera2Module.TAG, "onPictureTaken: stop multiple shot due to low storage");
                    return;
                }
                Camera2Module.access$1204(Camera2Module.this);
                if (!Camera2Module.this.mActivity.getImageSaver().isSaveQueueFull()) {
                    this.mSavedJpegCallbackNum++;
                    Camera2Module.this.playCameraSound(4);
                    ViberatorContext.getInstance(Camera2Module.this.getActivity().getApplicationContext()).performBurstCapture();
                    Camera2Module.this.mBurstEmitter.onNext(Integer.valueOf(this.mSavedJpegCallbackNum));
                    int orientation = ExifHelper.getOrientation(bArr);
                    if ((Camera2Module.this.mJpegRotation + orientation) % 180 == 0) {
                        i2 = Camera2Module.this.mPictureSize.getWidth();
                        i = Camera2Module.this.mPictureSize.getHeight();
                    } else {
                        i2 = Camera2Module.this.mPictureSize.getHeight();
                        i = Camera2Module.this.mPictureSize.getWidth();
                    }
                    int i3 = i2;
                    int i4 = i;
                    String burstShotTitle = getBurstShotTitle();
                    boolean z = (Camera2Module.this.mReceivedJpegCallbackNum != 1 || Camera2Module.this.mMultiSnapStopRequest) && (Camera2Module.this.mReceivedJpegCallbackNum == Camera2Module.this.mTotalJpegCallbackNum || Camera2Module.this.mMultiSnapStopRequest || this.mDropped);
                    Camera2Module.this.mActivity.getImageSaver().addImage(bArr, z, burstShotTitle, null, System.currentTimeMillis(), null, this.mLocation, i3, i4, null, orientation, false, false, true, false, false, null, Camera2Module.this.getPictureInfo(), -1, captureResult);
                    this.mDropped = false;
                } else {
                    String access$400 = Camera2Module.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("CaptureBurst queue full and drop ");
                    sb.append(Camera2Module.this.mReceivedJpegCallbackNum);
                    Log.e(access$400, sb.toString());
                    this.mDropped = true;
                    if (Camera2Module.this.mReceivedJpegCallbackNum >= Camera2Module.this.mTotalJpegCallbackNum) {
                        Camera2Module.this.mActivity.getThumbnailUpdater().getLastThumbnailUncached();
                    }
                }
                if (Camera2Module.this.mReceivedJpegCallbackNum >= Camera2Module.this.mTotalJpegCallbackNum || Camera2Module.this.mMultiSnapStopRequest || this.mDropped) {
                    Camera2Module.this.stopMultiSnap();
                }
            }
        }

        public void onPictureTakenFinished(boolean z) {
            Camera2Module.this.stopMultiSnap();
            Camera2Module.this.mBurstEmitter.onComplete();
        }
    }

    final class JpegRepeatingCaptureCallback extends PictureCallbackWrapper {
        String mBurstShotTitle;
        private boolean mDropped;
        private WeakReference mModule;
        ParallelTaskDataParameter mParallelParameter = null;
        String mPressDownTitle;

        public JpegRepeatingCaptureCallback(Camera2Module camera2Module) {
            this.mModule = new WeakReference(camera2Module);
        }

        private String getBurstShotTitle() {
            if (Camera2Module.this.mUpdateImageTitle && this.mBurstShotTitle != null && Camera2Module.this.mReceivedJpegCallbackNum == 1) {
                this.mPressDownTitle = this.mBurstShotTitle;
                this.mBurstShotTitle = null;
            }
            if (this.mBurstShotTitle == null) {
                long currentTimeMillis = System.currentTimeMillis();
                this.mBurstShotTitle = Util.createJpegName(currentTimeMillis);
                if (this.mBurstShotTitle.length() != 19) {
                    this.mBurstShotTitle = Util.createJpegName(currentTimeMillis + 1000);
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(this.mBurstShotTitle);
            sb.append("_BURST");
            sb.append(Camera2Module.this.mReceivedJpegCallbackNum);
            return sb.toString();
        }

        private boolean tryCheckNeedStop() {
            if (!Storage.isLowStorageAtLastPoint()) {
                return false;
            }
            if (Camera2Module.this.mMultiSnapStatus) {
                Camera2Module.this.stopMultiSnap();
            }
            return true;
        }

        public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
            String str = "onCaptureStart: revNum = ";
            boolean z5 = true;
            if (!Camera2Module.this.mEnableParallelSession || Camera2Module.this.mPaused || Camera2Module.this.mReceivedJpegCallbackNum >= Camera2Module.this.mTotalJpegCallbackNum || !Camera2Module.this.mMultiSnapStatus) {
                String access$400 = Camera2Module.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(Camera2Module.this.mReceivedJpegCallbackNum);
                sb.append(" paused = ");
                sb.append(Camera2Module.this.mPaused);
                sb.append(" status = ");
                sb.append(Camera2Module.this.mMultiSnapStatus);
                Log.d(access$400, sb.toString());
                if (C0122O00000o.instance().OOo0oOO()) {
                    parallelTaskData.setRequireTuningData(true);
                }
                parallelTaskData.setAbandoned(true);
                return parallelTaskData;
            }
            if (Camera2Module.this.mReceivedJpegCallbackNum == 1 && !Camera2Module.this.mMultiSnapStopRequest) {
                if (!Camera2Module.this.is3ALocked()) {
                    Camera2Module.this.mFocusManager.onShutter();
                }
                Camera2Module.this.mActivity.getImageSaver().updateImage(getBurstShotTitle(), this.mPressDownTitle);
            }
            if (tryCheckNeedStop()) {
                Log.d(Camera2Module.TAG, "onCaptureStart: need stop multi capture, return null");
                return null;
            }
            if (this.mParallelParameter == null) {
                String access$4002 = Camera2Module.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onCaptureStart: inputSize = ");
                sb2.append(cameraSize);
                Log.d(access$4002, sb2.toString());
                if ((Camera2Module.this.isIn3OrMoreSatMode() || Camera2Module.this.isInMultiSurfaceSatMode()) && (!cameraSize.equals(Camera2Module.this.mPictureSize) || C0124O00000oO.isMTKPlatform())) {
                    Camera2Module camera2Module = Camera2Module.this;
                    camera2Module.mPictureSize = cameraSize;
                    camera2Module.updateOutputSize(cameraSize);
                }
                CameraSize cameraSize2 = Camera2Module.this.mOutputPictureSize;
                Size sizeObject = cameraSize2 == null ? cameraSize.toSizeObject() : cameraSize2.toSizeObject();
                String access$4003 = Camera2Module.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("onCaptureStart: outputSize = ");
                sb3.append(sizeObject);
                Log.k(3, access$4003, sb3.toString());
                boolean isHeicImageFormat = CompatibilityUtils.isHeicImageFormat(Camera2Module.this.mOutputPictureFormat);
                int access$2200 = Camera2Module.this.clampQuality(CameraSettings.getEncodingQuality(true).toInteger(isHeicImageFormat));
                String access$4004 = Camera2Module.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("onCaptureStart: isHeic = ");
                sb4.append(isHeicImageFormat);
                sb4.append(", quality = ");
                sb4.append(access$2200);
                Log.d(access$4004, sb4.toString());
                if (isHeicImageFormat && Camera2Module.this.mCameraCapabilities.isSupportZeroDegreeOrientationImage() && (Camera2Module.this.mJpegRotation == 90 || Camera2Module.this.mJpegRotation == 270)) {
                    Size size = new Size(sizeObject.getHeight(), sizeObject.getWidth());
                    String access$4005 = Camera2Module.TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("onCaptureStart: switched outputSize: ");
                    sb5.append(size);
                    Log.d(access$4005, sb5.toString());
                    sizeObject = size;
                }
                Location access$2400 = Camera2Module.this.mActivity.getCameraIntentManager().checkIntentLocationPermission(Camera2Module.this.mActivity) ? Camera2Module.this.mLocation : null;
                Builder filterId = new Builder(Camera2Module.this.mPreviewSize.toSizeObject(), cameraSize.toSizeObject(), sizeObject, Camera2Module.this.mOutputPictureFormat).setHasDualWaterMark(false).setMirror(Camera2Module.this.isFrontMirror()).setLightingPattern(CameraSettings.getPortraitLightingPattern()).setFilterId(FilterInfo.FILTER_ID_NONE);
                int i = Camera2Module.this.mOrientation;
                if (-1 == i) {
                    i = 0;
                }
                Builder jpegQuality = filterId.setOrientation(i).setJpegRotation(Camera2Module.this.mJpegRotation).setShootRotation(Camera2Module.this.mShootRotation).setShootOrientation(Camera2Module.this.mShootOrientation).setSupportZeroDegreeOrientationImage(Camera2Module.this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()).setLocation(access$2400).setFrontCamera(Camera2Module.this.isFrontCamera()).setBokehFrontCamera(Camera2Module.this.isPictureUseDualFrontCamera()).setAlgorithmName(Camera2Module.this.mAlgorithmName).setPictureInfo(Camera2Module.this.getPictureInfo()).setSuffix(Camera2Module.this.getSuffix()).setSaveGroupshotPrimitive(false).setDeviceWatermarkParam(Camera2Module.this.getDeviceWaterMarkParam()).setJpegQuality(access$2200);
                boolean z6 = Camera2Module.this.isZoomRatioBetweenUltraAndWide() && C0122O00000o.instance().OO0O0O();
                this.mParallelParameter = jpegQuality.setReprocessBurstShotPicture(z6).build();
            }
            parallelTaskData.fillParameter(this.mParallelParameter);
            if (C0122O00000o.instance().OOo0oOO()) {
                parallelTaskData.setRequireTuningData(true);
            }
            if (!Camera2Module.this.mActivity.getImageSaver().isSaveQueueFull()) {
                Camera2Module.access$1204(Camera2Module.this);
                Camera2Module.this.playCameraSound(4);
                ViberatorContext.getInstance(Camera2Module.this.getActivity().getApplicationContext()).performBurstCapture();
                String access$4006 = Camera2Module.TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str);
                sb6.append(Camera2Module.this.mReceivedJpegCallbackNum);
                Log.d(access$4006, sb6.toString());
                Camera2Module.this.mBurstEmitter.onNext(Integer.valueOf(Camera2Module.this.mReceivedJpegCallbackNum));
                if (Camera2Module.this.mReceivedJpegCallbackNum <= Camera2Module.this.mTotalJpegCallbackNum) {
                    String generateFilepath4Image = Storage.generateFilepath4Image(getBurstShotTitle(), CompatibilityUtils.isHeicImageFormat(Camera2Module.this.mOutputPictureFormat));
                    String access$4007 = Camera2Module.TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("onCaptureStart: savePath = ");
                    sb7.append(generateFilepath4Image);
                    Log.d(access$4007, sb7.toString());
                    parallelTaskData.setSavePath(generateFilepath4Image);
                    if (Camera2Module.this.mReceivedJpegCallbackNum != Camera2Module.this.mTotalJpegCallbackNum && !Camera2Module.this.mMultiSnapStopRequest && !this.mDropped) {
                        z5 = false;
                    }
                    parallelTaskData.setNeedThumbnail(z5);
                    Camera2Module.this.beginParallelProcess(parallelTaskData, false);
                    this.mDropped = false;
                    if (Camera2Module.this.mReceivedJpegCallbackNum >= Camera2Module.this.mTotalJpegCallbackNum || Camera2Module.this.mMultiSnapStopRequest || this.mDropped) {
                        Camera2Module.this.stopMultiSnap();
                    }
                    return parallelTaskData;
                }
            } else {
                String access$4008 = Camera2Module.TAG;
                StringBuilder sb8 = new StringBuilder();
                sb8.append("onCaptureStart: queue full and drop ");
                sb8.append(Camera2Module.this.mReceivedJpegCallbackNum);
                Log.e(access$4008, sb8.toString());
                this.mDropped = true;
                if (Camera2Module.this.mReceivedJpegCallbackNum >= Camera2Module.this.mTotalJpegCallbackNum) {
                    Camera2Module.this.mActivity.getThumbnailUpdater().getLastThumbnailUncached();
                }
            }
            parallelTaskData = null;
            Camera2Module.this.stopMultiSnap();
            return parallelTaskData;
        }

        public void onPictureTakenFinished(boolean z) {
            if (this.mModule.get() != null) {
                ((Camera2Module) this.mModule.get()).onBurstPictureTakenFinished(z);
            } else {
                Log.e(Camera2Module.TAG, "callback onShotFinished null");
            }
        }
    }

    class LocalParallelServiceStatusListener implements ServiceStatusListener {
        private final WeakReference mCamera2ModuleRef;
        private final WeakReference mCameraDevice;

        LocalParallelServiceStatusListener(Camera2Proxy camera2Proxy, Camera2Module camera2Module) {
            this.mCameraDevice = new WeakReference(camera2Proxy);
            this.mCamera2ModuleRef = new WeakReference(camera2Module);
        }

        public void onImagePostProcessEnd(ParallelTaskData parallelTaskData) {
            Camera2Module camera2Module = (Camera2Module) this.mCamera2ModuleRef.get();
            if (camera2Module != null && parallelTaskData != null && parallelTaskData.isJpegDataReady()) {
                if (camera2Module.mIsImageCaptureIntent || (DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode() && !C0122O00000o.instance().OOoOOO0())) {
                    camera2Module.onPictureTakenFinished(true);
                }
            }
        }

        public void onImagePostProcessStart(ParallelTaskData parallelTaskData) {
            Camera2Module camera2Module = (Camera2Module) this.mCamera2ModuleRef.get();
            if (camera2Module != null && 4 != parallelTaskData.getAlgoType()) {
                if (!camera2Module.mIsImageCaptureIntent && (!DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode() || C0122O00000o.instance().OOoOOO0())) {
                    camera2Module.onPictureTakenFinished(true);
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

        public MainHandler(Camera2Module camera2Module, Looper looper) {
            super(looper);
            this.mModule = new WeakReference(camera2Module);
        }

        /* JADX WARNING: Removed duplicated region for block: B:75:0x016e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(Message message) {
            Camera2Module camera2Module = (Camera2Module) this.mModule.get();
            if (camera2Module != null) {
                if (!camera2Module.isCreated()) {
                    removeCallbacksAndMessages(null);
                } else if (camera2Module.getActivity() != null) {
                    int i = message.what;
                    if (i == 2) {
                        camera2Module.getWindow().clearFlags(128);
                    } else if (i == 4) {
                        camera2Module.checkActivityOrientation();
                        if (SystemClock.uptimeMillis() - camera2Module.mOnResumeTime < 5000) {
                            sendEmptyMessageDelayed(4, 100);
                        }
                    } else if (i == 17) {
                        removeMessages(17);
                        removeMessages(2);
                        camera2Module.getWindow().addFlags(128);
                        sendEmptyMessageDelayed(2, (long) camera2Module.getScreenDelay());
                    } else if (i == 31) {
                        camera2Module.setOrientationParameter();
                    } else if (i != 33) {
                        boolean z = false;
                        if (i == 35) {
                            boolean z2 = message.arg1 > 0;
                            if (message.arg2 > 0) {
                                z = true;
                            }
                            camera2Module.handleUpdateFaceView(z2, z);
                        } else if (i == 4097) {
                            camera2Module.startVideoRecording();
                        } else if (i == 44) {
                            camera2Module.restartModule();
                        } else if (i != 45) {
                            switch (i) {
                                case 9:
                                    camera2Module.mMainProtocol.initializeFocusView(camera2Module);
                                    break;
                                case 10:
                                    if (!camera2Module.mActivity.isActivityPaused()) {
                                    }
                                    break;
                                case 11:
                                    break;
                                default:
                                    switch (i) {
                                        case 48:
                                            camera2Module.setCameraState(1);
                                            break;
                                        case 49:
                                            if (camera2Module.isAlive()) {
                                                camera2Module.stopMultiSnap();
                                                camera2Module.mBurstEmitter.onComplete();
                                                break;
                                            } else {
                                                return;
                                            }
                                        case 50:
                                            Log.w(Camera2Module.TAG, "Oops, capture timeout later release timeout!");
                                            camera2Module.onPictureTakenFinished(false);
                                            break;
                                        case 51:
                                            break;
                                        case 52:
                                            camera2Module.onShutterButtonClick(camera2Module.getTriggerMode());
                                            break;
                                        default:
                                            switch (i) {
                                                case 56:
                                                    MainContentProtocol mainContentProtocol = camera2Module.mMainProtocol;
                                                    if (mainContentProtocol != null && mainContentProtocol.isFaceExists(1) && camera2Module.mMainProtocol.isFocusViewVisible()) {
                                                        Camera2Proxy camera2Proxy = camera2Module.mCamera2Device;
                                                        if (camera2Proxy != null && 4 == camera2Proxy.getFocusMode()) {
                                                            camera2Module.mMainProtocol.clearFocusView(7);
                                                            break;
                                                        }
                                                    }
                                                case 57:
                                                    PreviewDecodeManager.getInstance().reset();
                                                    break;
                                                case 58:
                                                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                                                    if (configChanges != null) {
                                                        int i2 = camera2Module.mOrientationCompensation;
                                                        configChanges.configRotationChange(message.arg1, (360 - (i2 >= 0 ? i2 % m.cQ : (i2 % m.cQ) + m.cQ)) % m.cQ);
                                                        break;
                                                    }
                                                    break;
                                                case 59:
                                                    Log.d(Camera2Module.TAG, "receive MSG_FIXED_SHOT2SHOT_TIME_OUT");
                                                    camera2Module.resetStatusToIdle();
                                                    break;
                                                case 60:
                                                    Log.d(Camera2Module.TAG, "fallback timeout");
                                                    camera2Module.mIsSatFallback = 0;
                                                    camera2Module.mFallbackProcessed = false;
                                                    camera2Module.mLastSatFallbackRequestId = -1;
                                                    if (camera2Module.mWaitingSnapshot && camera2Module.getCameraState() == 1) {
                                                        camera2Module.mWaitingSnapshot = false;
                                                        sendEmptyMessage(62);
                                                        break;
                                                    }
                                                case 61:
                                                    Log.d(Camera2Module.TAG, "wait save finish timeout");
                                                    camera2Module.mWaitSaveFinish = false;
                                                    camera2Module.showOrHideLoadingProgress(false, true);
                                                    break;
                                                case 62:
                                                    camera2Module.onWaitingFocusFinished();
                                                    break;
                                                case 63:
                                                    CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                                                    if (cameraAction != null) {
                                                        cameraAction.onShutterButtonClick(120);
                                                        break;
                                                    }
                                                    break;
                                                case 64:
                                                    camera2Module.setCameraState(1);
                                                    camera2Module.enableCameraControls(true);
                                                    Log.d(Camera2Module.TAG, "X:resetStatusToIdle");
                                                    break;
                                                case 65:
                                                    sendEmptyMessageDelayed(66, 5000);
                                                    camera2Module.showAutoHibernationTip();
                                                    break;
                                                case 66:
                                                    camera2Module.enterAutoHibernation();
                                                    break;
                                                default:
                                                    StringBuilder sb = new StringBuilder();
                                                    sb.append("no consumer for this message: ");
                                                    sb.append(message.what);
                                                    throw new RuntimeException(sb.toString());
                                            }
                                            break;
                                    }
                                    if (!camera2Module.mActivity.isActivityPaused()) {
                                        camera2Module.mOpenCameraFail = true;
                                        camera2Module.onCameraException();
                                        break;
                                    }
                                    break;
                            }
                        } else {
                            camera2Module.setActivity(null);
                        }
                    } else {
                        camera2Module.setOrientation(message.arg1, message.arg2);
                    }
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

    class SuperNightEventConsumer implements Consumer {
        public static final int STATE_ON_CAPTURE_COMPLETE = 2;
        public static final int STATE_ON_PICTURE_COMPLETE = 4;
        public static final int STATE_START = 1;
        private final WeakReference mCamera2ModuleRef;
        private int mState;

        private SuperNightEventConsumer(Camera2Module camera2Module) {
            this.mState = 1;
            this.mCamera2ModuleRef = new WeakReference(camera2Module);
        }

        private void handleNewAnimation(int i) {
            String access$400 = Camera2Module.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("handleNewAnimation: E > ");
            sb.append(i);
            Log.d(access$400, sb.toString());
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                int i2 = this.mState;
                if ((i2 | i) == 1) {
                    String access$4002 = Camera2Module.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("handleNewAnimation: startAnimation  duration = ");
                    sb2.append(DataRepository.dataItemRunning().getMultiFrameTotalCaptureDuration());
                    Log.d(access$4002, sb2.toString());
                    PerformanceTracker.calCaptureDuration(0);
                    recordState.onPrepare();
                    recordState.onStart();
                } else if ((i2 | i) == 3) {
                    Log.d(Camera2Module.TAG, "handleNewAnimation: startWaitingImage >> ");
                    PerformanceTracker.calCaptureDuration(1);
                    recordState.onPostSavingStart();
                } else if ((i2 | i) == 5) {
                    Log.d(Camera2Module.TAG, "handleNewAnimation: finishAnimation");
                    recordState.onFinish();
                } else if ((i2 | i) == 7) {
                    if (i2 == 3) {
                        recordState.onPostSavingFinish();
                        Log.d(Camera2Module.TAG, "handleNewAnimation: finishWaitingImage");
                    }
                    DataRepository.dataItemRunning().resetMultiFrameTotalCaptureDuration();
                    if (this.mCamera2ModuleRef.get() != null) {
                        ((Camera2Module) this.mCamera2ModuleRef.get()).mSuperNightEventEmitter.onComplete();
                        Disposable access$800 = ((Camera2Module) this.mCamera2ModuleRef.get()).mSuperNightDisposable;
                        if (access$800 != null && !access$800.isDisposed()) {
                            access$800.dispose();
                            ((Camera2Module) this.mCamera2ModuleRef.get()).mSuperNightDisposable = null;
                        }
                    }
                    this.mState = 1;
                    return;
                }
                this.mState = i | this.mState;
                String access$4003 = Camera2Module.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("handleNewAnimation: mstate = ");
                sb3.append(this.mState);
                Log.d(access$4003, sb3.toString());
            }
        }

        public void accept(Integer num) {
            Camera2Module camera2Module = (Camera2Module) this.mCamera2ModuleRef.get();
            if (camera2Module != null && camera2Module.isAlive()) {
                int intValue = num.intValue();
                if (intValue == 1 || intValue == 2 || intValue == 4) {
                    handleNewAnimation(num.intValue());
                } else if (intValue == 300) {
                    Log.d(Camera2Module.TAG, "SuperNight: show capture instruction hint");
                    BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null) {
                        topAlert.alertAiDetectTipHint(0, R.string.super_night_toast, -1);
                    }
                } else if (intValue == 2000) {
                    Log.d(Camera2Module.TAG, "SuperNight: trigger shutter animation, sound and post saving");
                    camera2Module.mWaitingSuperNightResult = true;
                    camera2Module.animateCapture();
                    camera2Module.playCameraSound(0);
                    RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                    if (recordState != null) {
                        recordState.onPostSavingStart();
                    }
                }
            }
        }
    }

    static /* synthetic */ void O000000o(ActionProcessing actionProcessing, Bitmap bitmap, float[] fArr, Size size) {
        if (actionProcessing != null) {
            actionProcessing.showDocumentReviewViews(bitmap, fArr, size);
        } else {
            Log.d(TAG, "showDocumentPreview: actionProcessing == null");
        }
    }

    static /* synthetic */ void O00oOOO0() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertAiSceneSelector(8);
        }
    }

    static /* synthetic */ int access$1204(Camera2Module camera2Module) {
        int i = camera2Module.mReceivedJpegCallbackNum + 1;
        camera2Module.mReceivedJpegCallbackNum = i;
        return i;
    }

    private boolean anchorFrameWhenPortrait() {
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        return this.mModuleIndex == 171 && ((cameraCapabilities != null ? cameraCapabilities.getPortraitLightingVersion() : 1) > 1 || !this.mIsPortraitLightingOn);
    }

    /* access modifiers changed from: private */
    public void animateCapture() {
        if (!this.mIsImageCaptureIntent) {
            this.mActivity.getCameraScreenNail().animateCapture(getCameraRotation());
        }
    }

    private void applyBacklightEffect() {
        trackAISceneChanged(this.mModuleIndex, 23);
        setAiSceneEffect(23);
        updateHDR("normal");
        this.mCamera2Device.setASDScene(23);
        resetEvValue();
    }

    /* access modifiers changed from: private */
    public void beginParallelProcess(ParallelTaskData parallelTaskData, boolean z) {
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

    private void blockSnapClickUntilSaveFinish(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("blockSnapClickUntilFinish: ");
        sb.append(z);
        Log.i(str, sb.toString());
        this.mWaitSaveFinish = true;
        this.mHandler.sendEmptyMessageDelayed(61, 5000);
        if (z) {
            showOrHideLoadingProgress(true, false);
        }
    }

    private long calculateTimeout(int i) {
        long j = CAPTURE_DURATION_THRESHOLD;
        if (i == 167) {
            return (Long.parseLong(getManualValue(CameraSettings.KEY_QC_EXPOSURETIME, getString(R.string.pref_camera_exposuretime_default))) / ExtraTextUtils.MB) + CAPTURE_DURATION_THRESHOLD;
        }
        if (i == 173 || CameraSettings.isSuperNightOn()) {
            j = 24000;
        }
        return j;
    }

    private void callGalleryDocumentPage(String str, String str2, Camera camera) {
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("callGalleryDocumentPage effect: ");
        sb.append(str2);
        Log.k(4, str3, sb.toString());
        Intent intent = new Intent();
        intent.setAction(CameraIntentManager.ACTION_EDIT_DOCOCUMENT_IMAGE);
        intent.setData(Util.photoUri(str));
        intent.putExtra(CameraIntentManager.DOCUMENT_IMAGE_EFFECT, str2);
        if (camera.startFromKeyguard()) {
            intent.putExtra("StartActivityWhenLocked", true);
        }
        if (Util.startActivityForResultCatchException(camera, intent, Util.REQUEST_CODE_OPEN_MIUI_EXTRA_PHOTO)) {
            camera.setJumpFlag(6);
        }
    }

    private void callGalleryIDCardPage(String[] strArr, Camera camera) {
        Log.k(4, TAG, "callGalleryIDCardPage");
        int entranceMode = DataRepository.dataItemRunning().getEntranceMode(186);
        Intent intent = new Intent();
        intent.setAction(CameraIntentManager.ACTION_EDIT_IDCARD_IMAGE);
        ArrayList arrayList = new ArrayList();
        arrayList.add(Util.photoUri(strArr[0]));
        arrayList.add(Util.photoUri(strArr[1]));
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        if (camera.startFromKeyguard()) {
            intent.putExtra("StartActivityWhenLocked", true);
        }
        if (Util.startActivityForResultCatchException(camera, intent, Util.REQUEST_CODE_OPEN_MIUI_EXTRA_PHOTO)) {
            camera.setJumpFlag(6);
            ((DataItemGlobal) DataRepository.provider().dataGlobal()).setCurrentMode(entranceMode);
        }
    }

    private void checkLLS(CaptureResult captureResult) {
        boolean isLLSNeeded = CaptureResultParser.isLLSNeeded(captureResult);
        if (isLLSNeeded != this.mIsLLSNeeded) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("is lls needed = ");
            sb.append(isLLSNeeded);
            Log.d(str, sb.toString());
            this.mIsLLSNeeded = isLLSNeeded;
            this.mCamera2Device.setLLS(this.mIsLLSNeeded);
        }
    }

    private void checkMoreFrameCaptureLockAFAE(boolean z) {
        if (this.mCamera2Device == null) {
            Log.w(TAG, "mCamera2Device == null, return");
        } else if (C0122O00000o.instance().OOo0oO0()) {
            if (!ModuleManager.isSuperNightScene() && !this.mShowSuperNightHint && ((!this.mMutexModePicker.isHdr() || !isBackCamera()) && !this.mIsLLSNeeded && !this.mCamera2Device.getCameraConfigs().isSuperResolutionEnabled())) {
                return;
            }
            if ((!ModuleManager.isSuperNightScene() && !this.mShowSuperNightHint) || C0122O00000o.instance().OOoOOOO()) {
                if ((!this.mMutexModePicker.isHdr() || !this.mCamera2Device.useSingleCaptureForHdrPlusMfnr(this.mCameraCapabilities)) && !is3ALocked()) {
                    this.mCamera2Device.setMFLockAfAe(z);
                }
            }
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

    /* access modifiers changed from: private */
    public int clampQuality(int i) {
        return this.mModuleIndex == 186 ? EncodingQuality.LOW.toInteger(false) : DataRepository.dataItemRunning().getComponentUltraPixel().isRear108MPSwitchOn() ? Util.clamp(i, 0, 90) : i;
    }

    private void configParallelSession() {
        GraphDescriptorBean graphDescriptorBean;
        BufferFormat bufferFormat;
        int cameraCombinationMode = CameraDeviceUtil.getCameraCombinationMode(Camera2DataContainer.getInstance().getRoleIdByActualId(this.mActualCameraId));
        if (isPortraitMode()) {
            int i = ((!isDualFrontCamera() || C0122O00000o.instance().OO0OOO()) && !isDualCamera() && !isBokehUltraWideBackCamera()) ? 1 : 2;
            graphDescriptorBean = new GraphDescriptorBean(32770, i, true, cameraCombinationMode);
        } else {
            int i2 = this.mModuleIndex;
            if (i2 == 167) {
                graphDescriptorBean = new GraphDescriptorBean(32771, 1, true, cameraCombinationMode);
            } else if (i2 == 175) {
                graphDescriptorBean = new GraphDescriptorBean(33011, 1, true, cameraCombinationMode);
            } else if (DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode() || (DataRepository.dataItemGlobal().isOnSuperNightHalfAlgoUp() && (C0124O00000oO.O0o0O0O || C0124O00000oO.O0o0O0 || C0124O00000oO.Oo0000O() || C0124O00000oO.O0o0o00))) {
                graphDescriptorBean = new GraphDescriptorBean(32778, 1, true, cameraCombinationMode);
            } else {
                if (cameraCombinationMode == 0) {
                    cameraCombinationMode = 513;
                }
                graphDescriptorBean = new GraphDescriptorBean(0, 1, true, cameraCombinationMode);
            }
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configParallelSession:    streamNbr = ");
        sb.append(graphDescriptorBean.getStreamNumber());
        Log.d(str, sb.toString());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("configParallelSession:  pictureSize = ");
        sb2.append(this.mPictureSize);
        Log.d(str2, sb2.toString());
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("configParallelSession:   outputSize = ");
        sb3.append(this.mOutputPictureSize);
        Log.d(str3, sb3.toString());
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("configParallelSession: outputFormat = ");
        sb4.append(this.mOutputPictureFormat);
        Log.d(str4, sb4.toString());
        if (C0122O00000o.instance().OOOoooO() && this.mUpscaleImageWithSR) {
            CameraSize cameraSize = this.mOutputPictureSize;
            bufferFormat = new BufferFormat(cameraSize.width, cameraSize.height, 35, graphDescriptorBean);
        } else if ((getRawCallbackType(false) & 8) != 0) {
            CameraSize cameraSize2 = this.mSensorRawImageSize;
            bufferFormat = new BufferFormat(cameraSize2.width, cameraSize2.height, 32, graphDescriptorBean);
        } else if ((C0124O00000oO.O0o0OO || C0124O00000oO.O0o0O0O || C0124O00000oO.O0o0O0) && isFrontCamera() && this.mModuleIndex == 163) {
            CameraSize cameraSize3 = this.mPictureSize;
            bufferFormat = new BufferFormat(cameraSize3.width / 2, cameraSize3.height / 2, 35, graphDescriptorBean);
        } else {
            CameraSize cameraSize4 = this.mPictureSize;
            bufferFormat = new BufferFormat(cameraSize4.width, cameraSize4.height, 35, graphDescriptorBean);
        }
        AlgoConnector.getInstance().getLocalBinder(true).configCaptureSession(bufferFormat, this.mBokehDepthSize);
        synchronized (this.mParallelSessionLock) {
            this.mParallelSessionConfigured = true;
        }
    }

    /* access modifiers changed from: private */
    @MainThread
    public void consumeAiSceneResult(int i, boolean z) {
        if (this.mCamera2Device == null) {
            Log.e(TAG, "consumeAiSceneResult : camera device is null");
            return;
        }
        if (this.mAIWatermarkEnable) {
            AIWatermarkDetect aIWatermarkDetect = (AIWatermarkDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(254);
            if (aIWatermarkDetect != null) {
                aIWatermarkDetect.onASDChange(i);
            }
        }
        if (this.mAiSceneEnabled) {
            realConsumeAiSceneResult(i, z);
            int i2 = this.mCurrentAiScene;
            if (!(i2 == -1 || i2 == 23 || i2 == 24 || i2 == 35)) {
                this.mCamera2Device.setASDScene(0);
            }
            resumePreviewInWorkThread();
        }
    }

    /* access modifiers changed from: private */
    public void consumeAsdSceneResult(int i) {
        if (this.mCurrentAsdScene == i) {
            return;
        }
        if ((!isDoingAction() || isInCountDown()) && isAlive() && !this.mActivity.isActivityPaused()) {
            updateAsdSceneResult(i);
        }
    }

    private void customizeReprocessor() {
        if (C0122O00000o.instance().OOo0oOO()) {
            LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
            if (localBinder != null) {
                HashMap hashMap = new HashMap();
                hashMap.put(Integer.valueOf(101), Boolean.valueOf(C0122O00000o.instance().OOoO0O()));
                int maxJpegSize = Camera2DataContainer.getInstance().getMaxJpegSize();
                if (isFrontCamera() || 175 == getModuleIndex() || CameraSettings.isUltraPixelOn()) {
                    maxJpegSize *= 3;
                }
                if (!C0124O00000oO.O0o0oOo) {
                    hashMap.put(Integer.valueOf(102), Integer.valueOf(maxJpegSize));
                }
                CameraSize tuningBufferSize = this.mCameraCapabilities.getTuningBufferSize(1);
                if (tuningBufferSize != null && !tuningBufferSize.isEmpty()) {
                    hashMap.put(Integer.valueOf(103), new Size(tuningBufferSize.getWidth(), tuningBufferSize.getHeight()));
                }
                localBinder.customizeReprocessor(hashMap);
                this.mIsReprocessorCustomized = true;
            }
        }
    }

    /* JADX WARNING: type inference failed for: r3v0 */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v2, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v3, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r0v3, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r3v14, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v15, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v16, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r3v17 */
    /* JADX WARNING: type inference failed for: r3v18 */
    /* JADX WARNING: type inference failed for: r3v19 */
    /* JADX WARNING: type inference failed for: r3v20 */
    /* JADX WARNING: type inference failed for: r3v21 */
    /* JADX WARNING: type inference failed for: r3v22 */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ee, code lost:
        r6 = th;
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00f0, code lost:
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        r6.mActivity.setResult(0);
        r6.mActivity.finish();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00fa, code lost:
        com.android.camera.Util.closeSilently(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00fd, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00fe, code lost:
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
        r6.mActivity.setResult(0);
        r6.mActivity.finish();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0108, code lost:
        com.android.camera.Util.closeSilently(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x010b, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:44:0x00f0, B:48:0x00fe] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:44:0x00f0 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:48:0x00fe */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v4
  assigns: []
  uses: []
  mth insns count: 112
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:44:0x00f0=Splitter:B:44:0x00f0, B:48:0x00fe=Splitter:B:48:0x00fe} */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void doAttach() {
        ? r3;
        ? r32;
        String str = sTempCropFilename;
        if (!this.mPaused) {
            byte[] storedJpegData = this.mActivity.getImageSaver().getStoredJpegData();
            if (this.mIsSaveCaptureImage) {
                this.mActivity.getImageSaver().saveStoredData();
            }
            ? r33 = 0;
            if (this.mCropValue != null) {
                File fileStreamPath = this.mActivity.getFileStreamPath(str);
                fileStreamPath.delete();
                ? openFileOutput = this.mActivity.openFileOutput(str, 0);
                try {
                    openFileOutput.write(storedJpegData);
                    openFileOutput.close();
                    Uri fromFile = Uri.fromFile(fileStreamPath);
                    Util.closeSilently(null);
                    Bundle bundle = new Bundle();
                    if (ComponentRunningTiltValue.TILT_CIRCLE.equals(this.mCropValue)) {
                        bundle.putString("circleCrop", BaseEvent.VALUE_TRUE);
                    }
                    Uri uri = this.mSaveUri;
                    if (uri != null) {
                        bundle.putParcelable("output", uri);
                    } else {
                        bundle.putBoolean("return-data", true);
                    }
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setData(fromFile);
                    intent.putExtras(bundle);
                    this.mActivity.startActivityForResult(intent, 1000);
                } catch (FileNotFoundException unused) {
                    ? r34 = openFileOutput;
                } catch (IOException unused2) {
                    r33 = openFileOutput;
                } catch (Throwable th) {
                    th = th;
                    ? r35 = openFileOutput;
                    Util.closeSilently(r35);
                    throw th;
                }
            } else if (this.mSaveUri != null) {
                try {
                    r32 = r33;
                    ? openOutputStream = CameraAppImpl.getAndroidContext().getContentResolver().openOutputStream(this.mSaveUri);
                    openOutputStream.write(storedJpegData);
                    openOutputStream.close();
                    this.mActivity.setResult(-1);
                    r3 = openOutputStream;
                } catch (Exception e) {
                    Log.e(TAG, "Exception when doAttach: ", (Throwable) e);
                    r3 = r32;
                } catch (Throwable th2) {
                    this.mActivity.finish();
                    Util.closeSilently(r32);
                    throw th2;
                }
                this.mActivity.finish();
                Util.closeSilently(r3);
            } else {
                this.mActivity.setResult(-1, new Intent("inline-data").putExtra(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, Util.rotate(Util.makeBitmap(storedJpegData, 51200), ExifHelper.getOrientation(storedJpegData))));
                this.mActivity.finish();
            }
            this.mActivity.getImageSaver().releaseStoredJpegData();
        }
    }

    private void doLaterReleaseIfNeed() {
        if (this.mActivity == null) {
            Log.w(TAG, "doLaterReleaseIfNeed: mActivity is null...");
            return;
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy == null || !camera2Proxy.isSessionReady() || !this.mEnableParallelSession || !this.mCamera2Device.isShotQueueMultitasking()) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(50);
            }
            if (this.mActivity.isActivityPaused()) {
                Camera2Proxy camera2Proxy2 = this.mCamera2Device;
                boolean z = camera2Proxy2 != null && camera2Proxy2.isSessionReady();
                Log.d(TAG, z ? "doLaterRelease" : "doLaterRelease but session is closed");
                this.mActivity.releaseAll(true, z);
            } else if (isDeparted()) {
                Log.w(TAG, "doLaterReleaseIfNeed: isDeparted...");
            } else {
                this.mHandler.post(new C0387O000oOO0(this));
                if (isTextureExpired()) {
                    Log.d(TAG, "doLaterReleaseIfNeed: surfaceTexture expired, restartModule");
                    this.mHandler.post(new C0386O000oOO(this));
                }
            }
        }
    }

    private boolean enableFrontMFNR() {
        boolean z = false;
        if (C0124O00000oO.isMTKPlatform()) {
            if (C0124O00000oO.Oo00O0() && C0122O00000o.instance().OO0OOoO()) {
                z = true;
            }
            return z;
        } else if (!C0124O00000oO.Oo00O0()) {
            return false;
        } else {
            if (this.mOperatingMode == 32773) {
                return true;
            }
            if (C0122O00000o.instance().OOO0OO0()) {
                int i = this.mOperatingMode;
                if (i == 32770 || i == 36864) {
                    return true;
                }
            }
            if (C0122O00000o.instance().OO0OOoO()) {
                int i2 = this.mOperatingMode;
                if (i2 == 36865) {
                    return true;
                }
                return i2 == 36867 ? C0122O00000o.instance().OOOooO0() : isFrontCamera() && this.mOperatingMode == 36869;
            }
        }
    }

    private boolean enablePreviewAsThumbnail() {
        boolean z = false;
        if (!isAlive()) {
            return false;
        }
        if (this.mModuleIndex == 175) {
            return C0122O00000o.instance().OOoO();
        }
        if (CameraSettings.isUltraPixelOn()) {
            return false;
        }
        if (this.mEnableParallelSession) {
            return true;
        }
        if (this.mIsPortraitLightingOn) {
            return false;
        }
        if (CameraSettings.isLiveShotOn() || CameraSettings.isPortraitModeBackOn()) {
            return true;
        }
        int i = this.mModuleIndex;
        if (i == 167) {
            return false;
        }
        if (i != 173 && !CameraSettings.isSuperNightOn() && !CameraSettings.showGenderAge() && !CameraSettings.isMagicMirrorOn() && !CameraSettings.isTiltShiftOn()) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null && camera2Proxy.isNeedPreviewThumbnail()) {
                z = true;
            }
        }
        return z;
    }

    private void enterAsdScene(int i) {
        int[] iArr;
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        String str = "1";
        if (i != 0) {
            if (i == 7) {
                return;
            }
            if (i == 9) {
                String componentValue = DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex);
                if ("3".equals(componentValue)) {
                    topAlert.alertFlash(0, str, false);
                    iArr = new int[]{10};
                } else if (ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_AUTO.equals(componentValue)) {
                    topAlert.alertFlash(0, str, false);
                    Log.d(TAG, "enterAsdScene(): turn off HDR as FLASH has higher priority than HDR");
                    onHdrSceneChanged(false);
                    iArr = new int[]{10};
                } else {
                    return;
                }
                updatePreferenceInWorkThread(iArr);
            } else if (i == 4) {
            }
        } else if (getModuleIndex() == 182) {
            setCurrentAsdScene(-1);
        } else {
            Log.d(TAG, "alertFalsh");
            topAlert.alertFlash(0, str, false);
            updateHDRPreference();
        }
    }

    private void exitAsdScene(int i) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        String str = "1";
        if (i == 0) {
            String componentValue = DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex);
            if (!str.equals(componentValue) && !ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_ON.equals(componentValue) && !"2".equals(componentValue) && !"5".equals(componentValue)) {
                topAlert.alertFlash(8, str, false);
                updateHDRPreference();
            }
        } else if (i == 9) {
            String componentValue2 = DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex);
            if ("3".equals(componentValue2) || ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_AUTO.equals(componentValue2)) {
                topAlert.alertFlash(8, str, false);
            }
            updatePreferenceInWorkThread(10);
        } else if (i != 4 && i != 5) {
        }
    }

    private void findBestWatermarkItem(int i) {
        final WatermarkItem aIWatermarkItem = getAIWatermarkItem(i);
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(new Runnable() {
                public void run() {
                    ComponentRunningAIWatermark componentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
                    if (componentRunningAIWatermark != null && componentRunningAIWatermark.getAIWatermarkEnable() && componentRunningAIWatermark.getIWatermarkEnable()) {
                        Camera2Module.this.updateWatermarkUI(aIWatermarkItem);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: finishSuperNightState */
    public void O0000ooo(boolean z) {
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            if (z) {
                animateCapture();
                playCameraSound(0);
                recordState.onPostSavingStart();
            }
            recordState.onPostSavingFinish();
        }
    }

    private String generateFileTitle() {
        if (CameraSettings.isDocumentMode2On(this.mModuleIndex)) {
            this.mWaitSaveFinish = true;
            this.mMainProtocol.hideOrShowDocument(false);
            PreviewDecodeManager.getInstance().stopDecode(3);
        } else if (CameraSettings.isDocumentModeOn(this.mModuleIndex)) {
            Storage.createHideFile();
            blockSnapClickUntilSaveFinish(true);
            return Storage.DOCUMENT_PICTURE;
        } else if (this.mModuleIndex == 182) {
            Storage.createHideFile();
            String currentIDCardPictureName = getCurrentIDCardPictureName();
            blockSnapClickUntilSaveFinish(currentIDCardPictureName.equals(Storage.ID_CARD_PICTURE_2));
            return currentIDCardPictureName;
        }
        if (!TimerBurstController.isSupportTimerBurst(this.mModuleIndex) || !CameraSettings.isTimerBurstEnable()) {
            StringBuilder sb = new StringBuilder();
            sb.append(getPrefix());
            sb.append(Util.createJpegName(System.currentTimeMillis()));
            sb.append(getSuffix());
            return sb.toString();
        }
        TimerBurstController timerBurstController = DataRepository.dataItemLive().getTimerBurstController();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getPrefix());
        sb2.append(timerBurstController.getPictureTitle(Util.createJpegName(System.currentTimeMillis())));
        sb2.append(Storage.TIMER_BURST_SUFFIX);
        sb2.append(timerBurstController.getCaptureIndex());
        return sb2.toString();
    }

    private WatermarkItem getAIWatermarkItem(int i) {
        if (this.mChain == null) {
            this.mChain = new PriorityChainFactory().createPriorityChain(C0122O00000o.instance().OO00oO());
        }
        return (i != 89 ? this.mChain.createChain(this.mActivity) : this.mChain.createASDChain(this.mActivity)).handleRequest();
    }

    private int getBurstNum() {
        int OOoo00O = C0124O00000oO.OOoo00O();
        return isUltraWideBackCamera() ? Math.min(OOoo00O, 30) : isZoomRatioBetweenUltraAndWide() ? Math.min(OOoo00O, 30) : this.mIsNearRangeModeUITip ? Math.min(OOoo00O, 30) : OOoo00O;
    }

    private String getCalibrationDataFileName(int i) {
        return isFrontCamera() ? "front_dual_camera_caldata.bin" : i == Camera2DataContainer.getInstance().getUltraWideBokehCameraId() ? "back_dual_camera_caldata_wu.bin" : "back_dual_camera_caldata.bin";
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

    private String getCurrentAiSceneName() {
        int i = this.mCurrentAiScene;
        int i2 = this.mModuleIndex;
        if (i2 != 163 && i2 != 167) {
            return null;
        }
        if (!CameraSettings.getAiSceneOpen(this.mModuleIndex)) {
            return "off";
        }
        if (i == -1) {
            i = this.isSilhouette ? 24 : 23;
        }
        TypedArray obtainTypedArray = getResources().obtainTypedArray(R.array.ai_scene_names);
        String string = (i < 0 || i >= obtainTypedArray.length()) ? BaseEvent.UNSPECIFIED : obtainTypedArray.getString(i);
        obtainTypedArray.recycle();
        return string;
    }

    private String getCurrentIDCardPictureName() {
        return ((IDCardModeProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(233)).getCurrentPictureName();
    }

    /* access modifiers changed from: private */
    public DeviceWatermarkParam getDeviceWaterMarkParam() {
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

    private CameraSize getLimitSize(List list) {
        Rect activeArraySize = this.mCameraCapabilities.getActiveArraySize();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getLimitSize: maxSize = ");
        sb.append(activeArraySize.width());
        sb.append("x");
        sb.append(activeArraySize.height());
        Log.d(str, sb.toString());
        PictureSizeManager.initialize(list, activeArraySize.width() * activeArraySize.height(), this.mModuleIndex, this.mBogusCameraId);
        return PictureSizeManager.getBestPictureSize(this.mModuleIndex);
    }

    private String getManualValue(String str, String str2) {
        return ModuleManager.isProPhotoModule() ? CameraSettingPreferences.instance().getString(str, str2) : str2;
    }

    private int getPictureFormatSuitableForShot(int i) {
        if (CameraSettings.isDocumentModeOn(this.mModuleIndex) || this.mModuleIndex == 182) {
            return 256;
        }
        boolean z = CameraSettings.isLiveShotOn() && isLiveShotAvailable(i);
        if (z) {
            return 256;
        }
        return this.mOutputPictureFormat;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0133  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0148  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0167  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x018d  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01a3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public PictureInfo getPictureInfo() {
        float[] fArr;
        String superNightExif;
        float[] fArr2;
        Camera2Proxy camera2Proxy;
        StringBuilder sb;
        String str;
        String sb2;
        PictureInfo opMode = new PictureInfo().setFrontMirror(isFrontMirror()).setSensorType(isFrontCamera()).setBokehFrontCamera(isPictureUseDualFrontCamera()).setHdrType(DataRepository.dataItemConfig().getComponentHdr().getComponentValue(this.mModuleIndex)).setOpMode(getOperatingMode());
        opMode.setAiEnabled(this.mAiSceneEnabled);
        opMode.setAiType(this.mCurrentAiScene);
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
                superNightExif = DebugInfoUtil.getSuperNightExif(this.mCaptureSuperNightExifInfo);
                if (!TextUtils.isEmpty(superNightExif)) {
                    opMode.setPreviewSuperNightExif(superNightExif);
                }
                fArr2 = this.mApertures;
                if (fArr2 != null && fArr2.length > 0) {
                    opMode.setLensApertues(fArr2[0]);
                }
                if (!TextUtils.isEmpty(this.mDebugFaceInfos)) {
                    opMode.setFaceRoi(this.mDebugFaceInfos);
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
                if (this.mBeautyValues != null) {
                    if (!BeautyConstant.LEVEL_CLOSE.equals(CameraSettings.getFaceBeautifyLevel()) && DataRepository.dataItemRunning().getComponentRunningShine().getBeautyVersion() == 2) {
                        opMode.setBeautyLevel(this.mBeautyValues.mBeautyLevel);
                    }
                }
                if (this.mFaceDetectionEnabled) {
                    FaceAnalyzeInfo faceAnalyzeInfo = this.mFaceInfo;
                    if (faceAnalyzeInfo != null) {
                        opMode.setGender(faceAnalyzeInfo.mGender);
                        opMode.setBaby(this.mFaceInfo.mAge);
                    }
                }
                if (this.mModuleIndex == 173) {
                    opMode.setNightScene(1);
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
        superNightExif = DebugInfoUtil.getSuperNightExif(this.mCaptureSuperNightExifInfo);
        if (!TextUtils.isEmpty(superNightExif)) {
        }
        fArr2 = this.mApertures;
        opMode.setLensApertues(fArr2[0]);
        if (!TextUtils.isEmpty(this.mDebugFaceInfos)) {
        }
        opMode.setOperateMode(this.mOperatingMode);
        opMode.setZoomMulti(getZoomRatio());
        camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
        }
        if (this.mBeautyValues != null) {
        }
        if (this.mFaceDetectionEnabled) {
        }
        if (this.mModuleIndex == 173) {
        }
        opMode.end();
        return opMode;
    }

    private CameraSize getPictureSize(int i, int i2, CameraSize cameraSize) {
        CameraSize cameraSize2;
        CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(i);
        if (capabilities != null) {
            capabilities.setOperatingMode(this.mOperatingMode);
            List supportedOutputSizeWithAssignedMode = capabilities.getSupportedOutputSizeWithAssignedMode(i2);
            if (cameraSize != null) {
                List arrayList = new ArrayList(0);
                for (int i3 = 0; i3 < supportedOutputSizeWithAssignedMode.size(); i3++) {
                    CameraSize cameraSize3 = (CameraSize) supportedOutputSizeWithAssignedMode.get(i3);
                    if (cameraSize3.compareTo(cameraSize) <= 0) {
                        arrayList.add(cameraSize3);
                    }
                }
                supportedOutputSizeWithAssignedMode = arrayList;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getPictureSize: matchSizes = ");
            sb.append(supportedOutputSizeWithAssignedMode);
            Log.d(str, sb.toString());
            cameraSize2 = PictureSizeManager.getBestPictureSize(supportedOutputSizeWithAssignedMode, this.mModuleIndex);
        } else {
            cameraSize2 = null;
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getPictureSize: cameraId = ");
        sb2.append(i);
        sb2.append(" size = ");
        sb2.append(cameraSize2);
        Log.d(str2, sb2.toString());
        return cameraSize2;
    }

    private String getPrefix() {
        return isLivePhotoStarted() ? Storage.LIVE_SHOT_PREFIX : "";
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043 A[ADDED_TO_REGION, RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getRawCallbackType(boolean z) {
        int i;
        int i2 = this.mModuleIndex;
        if (i2 != 167) {
            if (i2 == 173) {
                i = DataRepository.dataItemGlobal().getRawSuperNightImpl();
                if (z && this.mSensorRawImageSize == null) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("raw image size null while we need raw for ");
                    sb.append(i);
                    Log.d(str, sb.toString());
                    return 0;
                }
            }
        } else if (DataRepository.dataItemConfig().getComponentConfigRaw().isSwitchOn(167)) {
            i = 1;
            return z ? i : i;
        }
        i = 0;
        if (z) {
        }
    }

    private String getRequestFlashMode() {
        if (isSupportSceneMode()) {
            String flashModeByScene = CameraSettings.getFlashModeByScene(this.mSceneMode);
            if (!TextUtils.isEmpty(flashModeByScene)) {
                return flashModeByScene;
            }
        }
        if (!this.mMutexModePicker.isSupportedFlashOn() && !this.mMutexModePicker.isSupportedTorch() && !this.mMutexModePicker.isHdrSupportTorch(this.mSupportFlashHDR)) {
            return "0";
        }
        String componentValue = DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex);
        if (this.mCurrentAsdScene == 9) {
            if (componentValue.equals("3")) {
                return "2";
            }
            if (componentValue.equals(ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_AUTO)) {
                return ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_ON;
            }
        }
        return componentValue;
    }

    private CameraSize getSatPictureSize() {
        int satMasterCameraId = this.mCamera2Device.getSatMasterCameraId();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getSatPictureSize: activeCameraId = ");
        sb.append(satMasterCameraId);
        Log.d(str, sb.toString());
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
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getSatPictureSize: invalid satMasterCameraId ");
        sb2.append(satMasterCameraId);
        Log.e(str2, sb2.toString());
        return this.mWidePictureSize;
    }

    /* access modifiers changed from: private */
    public String getSuffix() {
        return !this.mMutexModePicker.isNormal() ? this.mMutexModePicker.getSuffix() : "";
    }

    private static String getTiltShiftMode() {
        if (CameraSettings.isTiltShiftOn()) {
            return DataRepository.dataItemRunning().getComponentRunningTiltValue().getComponentValue(160);
        }
        return null;
    }

    private void handleLLSResultInCaptureMode() {
        if (this.mShowLLSHint) {
            this.mHandler.post(new C0389O00oOooO(this));
        }
    }

    private void handleSaveFinishIfNeed(String str) {
        this.mWaitSaveFinish = false;
        Camera camera = this.mActivity;
        if (camera != null && !camera.isActivityPaused() && isAlive() && str != null) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("handleSaveFinishIfNeed title: ");
            sb.append(str);
            Log.k(4, str2, sb.toString());
            boolean isDocumentPicture = Storage.isDocumentPicture(str);
            String str3 = Storage.JPEG_SUFFIX;
            if (isDocumentPicture) {
                Handler handler = this.mHandler;
                if (handler != null) {
                    handler.removeMessages(61);
                }
                AndroidSchedulers.mainThread().scheduleDirect(new C0348O0000oO(this));
                callGalleryDocumentPage(Storage.generateFilepath(str, str3), DataRepository.dataItemRunning().getComponentRunningDocument().getComponentValue(this.mModuleIndex), camera);
            } else if (Storage.isIdCardPicture(str)) {
                String generateFilepath = Storage.generateFilepath(str, str3);
                if (Storage.isIdCardPictureOne(str)) {
                    this.mIDCardPaths[0] = generateFilepath;
                    AndroidSchedulers.mainThread().scheduleDirect(O0000Oo0.INSTANCE);
                } else {
                    Handler handler2 = this.mHandler;
                    if (handler2 != null) {
                        handler2.removeMessages(61);
                    }
                    AndroidSchedulers.mainThread().scheduleDirect(new O0000o(this));
                    String[] strArr = this.mIDCardPaths;
                    strArr[1] = generateFilepath;
                    callGalleryIDCardPage(strArr, camera);
                }
            }
        }
    }

    private boolean handleSuperNightResultIfNeed() {
        if (this.mModuleIndex != 173) {
            return false;
        }
        stopCpuBoost();
        if (DataRepository.dataItemGlobal().isOnSuperNightAlgoUpAndQuickShot() && !DataRepository.dataItemRunning().isSuperNightCaptureWithKnownDuration()) {
            return false;
        }
        if (DataRepository.dataItemRunning().isSuperNightCaptureWithKnownDuration()) {
            PublishSubject publishSubject = this.mSuperNightEventEmitter;
            if (publishSubject != null) {
                publishSubject.onNext(Integer.valueOf(4));
            }
            return false;
        }
        Disposable disposable = this.mSuperNightDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mSuperNightDisposable.dispose();
            this.mSuperNightDisposable = null;
        }
        boolean z = !this.mWaitingSuperNightResult;
        this.mWaitingSuperNightResult = false;
        if (z) {
            Log.d(TAG, "SuperNight: force trigger shutter animation, sound and post saving");
        }
        if (CameraSchedulers.isOnMainThread()) {
            O0000ooo(z);
        } else {
            AndroidSchedulers.mainThread().scheduleDirect(new C0344O0000OoO(this, z));
        }
        return true;
    }

    private void handleSuperNightResultInCaptureMode() {
        if (this.mShowSuperNightHint) {
            this.mHandler.post(new C0355O0000ooo(this));
        }
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
        } else if ((this.mFaceDetectionStarted || isFaceBeautyMode()) && 1 != this.mCamera2Device.getFocusMode()) {
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

    private void hidePostCaptureAlert() {
        enableCameraControls(true);
        if (this.mCamera2Device.isSessionReady()) {
            resumePreview();
        } else {
            startPreview();
        }
        this.mMainProtocol.setEffectViewVisible(true);
        this.mMainProtocol.setIdPhotoBoxVisible(true);
        if (!this.mCameraCapabilities.isSupportLightTripartite()) {
            this.mMainProtocol.updateReferenceGradienterSwitched();
        }
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).delegateEvent(6);
    }

    private void hideSceneSelector() {
        this.mHandler.post(C0354O0000ooO.INSTANCE);
    }

    private void initAiSceneParser() {
        this.mFunctionParseAiScene = new FunctionParseAiScene(this.mModuleIndex, getCameraCapabilities());
        this.mAiSceneDisposable = Flowable.create(new FlowableOnSubscribe() {
            public void subscribe(FlowableEmitter flowableEmitter) {
                Camera2Module.this.mAiSceneFlowableEmitter = flowableEmitter;
            }
        }, BackpressureStrategy.DROP).observeOn(CameraSchedulers.sCameraSetupScheduler).map(this.mFunctionParseAiScene).filter(new PredicateFilterAiScene(this, C0122O00000o.instance().OOO0OoO())).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new Consumer() {
            public void accept(Integer num) {
                Camera2Module.this.consumeAiSceneResult(num.intValue(), false);
            }
        });
    }

    private void initFlashAutoStateForTrack(boolean z) {
        this.mFlashAutoModeState = null;
        String componentValue = DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex);
        if (componentValue.equals("3") || componentValue.equals(ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_AUTO)) {
            String str = (this.mCurrentAsdScene == 9 || z) ? BaseEvent.AUTO_ON : BaseEvent.AUTO_OFF;
            this.mFlashAutoModeState = str;
        }
    }

    private void initHistogramEmitter() {
        if (this.mModuleIndex == 167 && this.mCameraCapabilities.isSupportHistogram()) {
            this.mHistogramDisposable = Flowable.create(new FlowableOnSubscribe() {
                public void subscribe(FlowableEmitter flowableEmitter) {
                    Camera2Module.this.mHistogramEmitter = flowableEmitter;
                }
            }, BackpressureStrategy.DROP).observeOn(CameraSchedulers.sCameraSetupScheduler).map(new FunctionParseHistogramStats(this, this.mTopAlert)).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new Consumer() {
                public void accept(int[] iArr) {
                    if (Camera2Module.this.mMainProtocol != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("initHistogramEmitter 33  :    ");
                        sb.append(Camera2Module.this.mCameraCapabilities.isSupportHistogram());
                        Log.d("isSupportHistogram", sb.toString());
                        Camera2Module.this.mTopAlert.refreshHistogramStatsView();
                    }
                }
            });
        }
    }

    private void initMetaParser() {
        boolean isHighQualityPreferred = CameraSettings.isHighQualityPreferred();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initMetaParser: HQPreferred = ");
        sb.append(isHighQualityPreferred);
        Log.d(str, sb.toString());
        this.mMetaDataDisposable = Flowable.create(new FlowableOnSubscribe() {
            public void subscribe(FlowableEmitter flowableEmitter) {
                Camera2Module.this.mMetaDataFlowableEmitter = flowableEmitter;
            }
        }, BackpressureStrategy.DROP).observeOn(CameraSchedulers.sCameraSetupScheduler).map(new FunctionHdrDetect(this, this.mCameraCapabilities.isMotionDetectionSupported())).map(new FunctionParseSuperNight(this, !isHighQualityPreferred, this.mCameraCapabilities.isSuperNightExifTagDefined())).map(new FunctionMiAlgoASDEngine(this)).map(new FunctionDxoAsdSceneDetected(this)).sample(500, TimeUnit.MILLISECONDS).observeOn(CameraSchedulers.sCameraSetupScheduler).map(new FunctionAsdSceneDetect(this, getCameraCapabilities())).observeOn(AndroidSchedulers.mainThread()).onTerminateDetach().subscribe((Consumer) new AsdSceneConsumer(this));
    }

    /* access modifiers changed from: private */
    public void initParallelSession() {
        Log.d(TAG, "initParallelSession: E");
        configParallelSession();
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder(true);
        if (!this.mIsReprocessorCustomized) {
            customizeReprocessor();
        }
        localBinder.setImageSaver(this.mActivity.getImageSaver());
        CameraSize cameraSize = this.mOutputPictureSize;
        localBinder.setOutputPictureSpec(cameraSize.width, cameraSize.height, this.mOutputPictureFormat);
        localBinder.setSRRequireReprocess(C0122O00000o.instance().isSRRequireReprocess());
        Log.d(TAG, "initParallelSession: X");
    }

    private void initZoomMapControllerIfNeeded() {
        if (this.mZoomMapController == null && C0124O00000oO.isSupportedOpticalZoom() && isBackCamera() && !this.mIsImageCaptureIntent) {
            CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
            if (cameraCapabilities != null && cameraCapabilities.isSatPipSupported()) {
                int i = this.mModuleIndex;
                if ((i == 163 || i == 165) && !CameraSettings.isMacroModeEnabled(this.mModuleIndex)) {
                    this.mZoomMapController = new ZoomMapController(this.mActivity, CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex));
                }
            }
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

    /* access modifiers changed from: private */
    public boolean is3ALocked() {
        return this.m3ALocked;
    }

    private boolean isCannotGotoGallery() {
        return this.mPaused || this.isZooming || isKeptBitmapTexture() || this.mMultiSnapStatus || getCameraState() == 0 || (!this.mEnableParallelSession && isImageSaverFull()) || isInCountDown() || (getCameraState() == 3 && DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(this.mModuleIndex));
    }

    private boolean isCurrentRawDomainBasedSuperNight() {
        return this.mModuleIndex == 173 && C0122O00000o.instance().OOoO0OO();
    }

    private boolean isDisableWatermark() {
        if (C0124O00000oO.O0o0ooO && (C0124O00000oO.O0o000o || C0124O00000oO.O0o0o0O || C0124O00000oO.O0o00)) {
            float deviceBasedZoomRatio = getDeviceBasedZoomRatio();
            if (deviceBasedZoomRatio > 0.6f && deviceBasedZoomRatio < 1.0f) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnableQcfaForAlgoUp() {
        if (!this.mCameraCapabilities.isSupportedQcfa() || !this.mEnableParallelSession) {
            return false;
        }
        if (C0124O00000oO.isMTKPlatform()) {
            return CameraSettings.isUltraPixelOn();
        }
        boolean z = true;
        if (isInQCFAMode()) {
            return true;
        }
        if (!CameraSettings.isUltraPixelOn() || !C0122O00000o.instance().OOoOo00()) {
            z = false;
        }
        return z;
    }

    private boolean isFaceBeautyOn(BeautyValues beautyValues) {
        if (beautyValues == null) {
            return false;
        }
        return beautyValues.isFaceBeautyOn();
    }

    private boolean isFaceRecognizedInSuperNightse() {
        float f = com.xiaomi.camera.util.SystemProperties.getFloat("se_face_pre_value", this.mSeLuxThreshold);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("super night se face pre-value:");
        sb.append(f);
        Log.c(str, sb.toString());
        if (f > 0.0f) {
            SuperNightExif superNightExif = this.mPreviewSuperNightExifInfo;
            if (superNightExif != null && superNightExif.luxIndex > f) {
                return false;
            }
        }
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        boolean z = true;
        if (mainContentProtocol == null || !mainContentProtocol.isFaceExists(1)) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public boolean isFrontMirror() {
        if (!isFrontCamera()) {
            return false;
        }
        if (CameraSettings.isLiveShotOn() || this.mModuleIndex == 205) {
            return true;
        }
        return CameraSettings.isFrontMirror();
    }

    private boolean isHeicPreferred() {
        Log.d(TAG, "isHeicPreferred: %b %b %b", Boolean.valueOf(this.mCameraCapabilities.isHeicSupported()), Boolean.valueOf(C0122O00000o.instance().O0oOooO()), Boolean.valueOf(CameraSettings.isHeicImageFormatSelected()));
        if (!this.mIsImageCaptureIntent && this.mCameraCapabilities.isHeicSupported() && C0122O00000o.instance().O0oOooO() && CameraSettings.isHeicImageFormatSelected() && this.mEnableParallelSession) {
            int i = this.mModuleIndex;
            if (i == 163 || i == 165 || (!C0122O00000o.instance().OO0o000() && !C0124O00000oO.isMTKPlatform() && this.mModuleIndex == 175)) {
                return true;
            }
        }
        return false;
    }

    private boolean isImageSaverFull() {
        String str;
        String str2;
        ImageSaver imageSaver = this.mActivity.getImageSaver();
        if (imageSaver == null) {
            Log.w(TAG, "isParallelQueueFull: ImageSaver is null");
            return false;
        }
        if (imageSaver.isSaveQueueFull()) {
            str = TAG;
            str2 = "isParallelQueueFull: ImageSaver queue is full";
        } else if (!C0124O00000oO.isMTKPlatform() || Util.TOTAL_MEMORY_GB >= 6 || !CameraSettings.isHeicImageFormatSelected() || imageSaver.getInFlightTask() < 3) {
            return false;
        } else {
            str = TAG;
            str2 = "isParallelQueueFull: ImageSaver has too many HEIC tasks";
        }
        Log.d(str, str2);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean isIn3OrMoreSatMode() {
        return 36866 == this.mOperatingMode && HybridZoomingSystem.IS_3_OR_MORE_SAT;
    }

    private boolean isInCountDown() {
        CameraTimer cameraTimer = this.mCameraTimer;
        return cameraTimer != null && cameraTimer.isRunning();
    }

    /* access modifiers changed from: private */
    public boolean isInMultiSurfaceSatMode() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            return camera2Proxy.isInMultiSurfaceSatMode();
        }
        return false;
    }

    private boolean isInQCFAMode() {
        boolean z = false;
        if (getModuleIndex() != 163 && getModuleIndex() != 165 && getModuleIndex() != 186 && getModuleIndex() != 182 && getModuleIndex() != 205) {
            return false;
        }
        if (this.mCameraCapabilities.isSupportedQcfa() && isFrontCamera() && !C0122O00000o.instance().OO0OO()) {
            z = true;
        }
        return z;
    }

    private boolean isLaunchedByMainIntent() {
        String str;
        Camera camera = this.mActivity;
        if (camera != null) {
            Intent intent = camera.getIntent();
            if (intent != null) {
                str = intent.getAction();
                return "android.intent.action.MAIN".equals(str);
            }
        }
        str = null;
        return "android.intent.action.MAIN".equals(str);
    }

    private boolean isLimitSize() {
        return isBackCamera() && !CameraSettings.isUltraPixelOn() && C0122O00000o.instance().OO0OoOo();
    }

    private static boolean isLiveShotAvailable(int i) {
        return i == 0 || i == 5 || i == 8;
    }

    private boolean isNeedFixedShotTime() {
        boolean z = false;
        if (this.mIsAiShutterOn) {
            Log.d(TAG, "isNeedFixedShotTime mIsAiShutterOn: true");
            return false;
        }
        if (isParallelSessionEnable() && this.mParallelQuality == 0) {
            int i = this.mModuleIndex;
            if ((i == 163 || i == 165) && !isFrontCamera() && !CameraSettings.isMacroModeEnabled(this.mModuleIndex) && ((double) getZoomRatio()) == 1.0d && !isInCountDown()) {
                Camera2Proxy camera2Proxy = this.mCamera2Device;
                if ((camera2Proxy == null || !camera2Proxy.isNeedFlashOn()) && !this.mIsImageCaptureIntent && !CameraSettings.isLiveShotOn() && !this.mIsISORight4HWMFNR && ((C0122O00000o.instance().O0ooOo0() != 0 || DEBUG_ENABLE_DYNAMIC_HHT_FAST_SHOT) && !CameraSettings.isTiltShiftOn())) {
                    z = true;
                }
            }
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isNeedFixedShotTime nfst:");
        sb.append(z);
        sb.append(", mIsISORight4HWMFNR:");
        sb.append(this.mIsISORight4HWMFNR);
        Log.d(str, sb.toString());
        return z;
    }

    private boolean isParallelCameraSessionMode() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        return camera2Proxy != null && camera2Proxy.getCapabilities().isSupportParallelCameraDevice() && getModuleIndex() == 163 && !isParallelUnSupported() && this.mCamera2Device.getSATSubCameraIds() != null && ((this.mMutexModePicker.isHdr() && this.mCamera2Device.getCameraConfigs().isSupportParallelHDREnable()) || getZoomRatio() < HybridZoomingSystem.getTeleMinZoomRatio());
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
        if (z) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("isParallelQueueFull: isNeedWaitProcess:");
            sb.append(z);
            Log.w(str, sb.toString());
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

    private boolean isPortraitSuccessHintShowing() {
        return ((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175)).isPortraitHintVisible();
    }

    private boolean isPreviewThumbnailWhenFlash() {
        if (!this.mUseLegacyFlashMode) {
            return true;
        }
        if (!"3".equals(this.mLastFlashMode)) {
            if (!"1".equals(this.mLastFlashMode)) {
                return true;
            }
        }
        return false;
    }

    private boolean isProColorEnable() {
        return DataRepository.dataItemRunning().getComponentRunningColorEnhance().isEnabled(this.mModuleIndex);
    }

    private boolean isQueueFull() {
        return this.mEnableParallelSession ? isParallelQueueFull() : isImageSaverFull();
    }

    private boolean isSuperNightSeEnable() {
        String componentValue = DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex);
        return !"1".equals(componentValue) && !"2".equals(componentValue) && !com.xiaomi.camera.util.SystemProperties.getBoolean("se", false);
    }

    private boolean isTestImageCaptureWithoutLocation() {
        Uri uri = this.mSaveUri;
        if (uri == null || !uri.toString().contains("android.providerui.cts.fileprovider")) {
            return !this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity);
        }
        Log.d(TAG, "isTestImageCaptureWithoutLocation");
        return true;
    }

    private boolean isTriggerFlashHDR() {
        if (this.mSupportFlashHDR && !CameraSettings.isSuperNightOn() && getZoomRatio() == 1.0f && this.mCamera2Device.getCameraConfigs().getHDRMode() == 2) {
            String componentValue = DataRepository.dataItemConfig().getComponentHdr().getComponentValue(this.mModuleIndex);
            String componentValue2 = DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex);
            if (this.mCamera2Device != null && componentValue2.equals("3") && this.mCamera2Device.isNeedFlashForAuto(Integer.valueOf(-1), -1) && componentValue.equals("auto")) {
                return true;
            }
        }
        return false;
    }

    private boolean isTriggerQcfaModeChange(boolean z, boolean z2) {
        if (!this.mCameraCapabilities.isSupportedQcfa()) {
            return false;
        }
        if ((this.mModuleIndex != 171 || !isBokehFrontCamera()) && C0122O00000o.instance().O0ooo00() < 0 && z && !mIsBeautyFrontOn) {
            if (this.mOperatingMode == 32775) {
                return true;
            }
            DataRepository.dataItemConfig().getComponentHdr().getComponentValue(this.mModuleIndex);
        }
        return false;
    }

    private boolean isUseSwMfnr() {
        String str;
        String str2;
        if (CameraSettings.isGroupShotOn()) {
            str = TAG;
            str2 = "GroupShot is on";
        } else if (!C0122O00000o.instance().OO0Oo0O() && (isUltraWideBackCamera() || isZoomRatioBetweenUltraAndWide())) {
            str = TAG;
            str2 = "SwMfnr force off for ultra wide camera";
        } else if (!CameraSettings.isMfnrSatEnable()) {
            str = TAG;
            str2 = "Mfnr not enabled";
        } else if (!C0122O00000o.instance().OOoOOo0()) {
            str = TAG;
            str2 = "SwMfnr is not supported";
        } else if (!this.mMutexModePicker.isNormal()) {
            str = TAG;
            str2 = "Mutex mode is not normal";
        } else {
            if (C0122O00000o.instance().OO0Oo0O()) {
                int i = this.mModuleIndex;
                if (!(i == 167 || i == 173 || CameraSettings.isSuperNightOn())) {
                    Log.d(TAG, "For the devices does not have hardware MFNR, use software MFNR");
                    return true;
                }
            }
            if (!isFrontCamera() || isDualFrontCamera()) {
                return false;
            }
            if (this.mOperatingMode == 32773 && C0124O00000oO.Oo00O0()) {
                return true;
            }
            if (this.mOperatingMode != 32773 || C0124O00000oO.Oo00O0()) {
                return C0122O00000o.instance().isSupportUltraWide() || C0124O00000oO.O0Oooo0 || C0124O00000oO.O0o000;
            }
            return false;
        }
        Log.d(str, str2);
        return false;
    }

    private void lockAEAF() {
        Log.d(TAG, "lockAEAF");
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(true);
        }
        this.m3ALocked = true;
    }

    private boolean needActiveASD() {
        return DataRepository.dataItemRunning().getComponentRunningAIWatermark().needActive();
    }

    private boolean needQuickShot() {
        boolean z = false;
        if (!this.mBlockQuickShot && !this.mIsImageCaptureIntent && CameraSettings.isCameraQuickShotEnable()) {
            if (enablePreviewAsThumbnail()) {
                int i = this.mModuleIndex;
                if ((i == 163 || i == 165) && getZoomRatio() == 1.0f && !isFrontCamera() && !CameraSettings.isUltraWideConfigOpen(this.mModuleIndex) && !CameraSettings.isMacroModeEnabled(this.mModuleIndex) && !this.mCamera2Device.isNeedFlashOn() && !CameraSettings.isUltraPixelOn() && !CameraSettings.isLiveShotOn()) {
                    BeautyValues beautyValues = this.mBeautyValues;
                    if (beautyValues == null || !beautyValues.isFaceBeautyOn()) {
                        z = true;
                    }
                }
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("needQuickShot bRet:");
            sb.append(z);
            Log.d(str, sb.toString());
        }
        return z;
    }

    private boolean needShowThumbProgressImmediately() {
        return Long.parseLong(getManualValue(CameraSettings.KEY_QC_EXPOSURETIME, getString(R.string.pref_camera_exposuretime_default))) > 400000000 && this.mModuleIndex != 173 && !DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(this.mModuleIndex);
    }

    /* access modifiers changed from: private */
    public void onBurstPictureTakenFinished(boolean z) {
        stopMultiSnap();
        ObservableEmitter observableEmitter = this.mBurstEmitter;
        if (observableEmitter != null) {
            observableEmitter.onComplete();
        }
        onPictureTakenFinished(z);
        handleLLSResultInCaptureMode();
        PerformanceTracker.trackPictureCapture(1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00ac, code lost:
        if (com.android.camera.data.DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(r4.mModuleIndex) == false) goto L_0x00cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00b1, code lost:
        if (r8 != false) goto L_0x0067;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00cb, code lost:
        if (com.android.camera.data.DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(r4.mModuleIndex) == false) goto L_0x00cd;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onShutter(boolean z, boolean z2, boolean z3, boolean z4) {
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
        if (CameraSettings.isDocumentMode2On(this.mModuleIndex)) {
            Decoder decoder = PreviewDecodeManager.getInstance().getDecoder(3);
            if (decoder != null) {
                Pair cachePreview = ((DocumentDecoder) decoder).getCachePreview();
                showDocumentPreview((PreviewImage) cachePreview.first, (float[]) cachePreview.second);
            }
        } else {
            if (!this.mEnabledPreviewThumbnail || z2) {
                if (z2) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("anchor frame onshutter anchor ");
                    sb2.append(z2);
                    sb2.append(" doAnchor ");
                    sb2.append(z3);
                    sb2.append(" anchor read pixel ");
                    sb2.append(z4);
                    Log.d(str2, sb2.toString());
                    if (!z3 || z4) {
                        if (!z3) {
                        }
                    }
                } else if (this.mModuleIndex != 173) {
                    updateThumbProgress(false);
                }
                animateCapture();
                playCameraSound(0);
            }
            this.mActivity.getCameraScreenNail().requestReadPixels();
        }
    }

    private void onStartRecorderFail() {
        enableCameraControls(true);
        restoreOuterAudio();
        this.mMainProtocol.setEvAdjustVisible(true);
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onFailed();
        }
    }

    private void onStartRecorderSucceed() {
        enableCameraControls(true);
        this.mActivity.sendBroadcast(new Intent(BaseModule.START_VIDEO_RECORDING_ACTION));
        this.mMediaRecorderRecording = true;
        this.mRecordingStartTime = SystemClock.uptimeMillis();
        listenPhoneState(true);
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null && this.mVolumeLongPress) {
            baseDelegate.getAnimationComposite().setClickEnable(false);
        }
        this.mMainProtocol.setEvAdjustVisible(false);
        updateRecordingTime();
        keepScreenOn();
        AutoLockManager.getInstance(this.mActivity).removeMessage();
        CameraStatUtils.trackLongPressRecord();
    }

    private void parseIntent() {
        CameraIntentManager cameraIntentManager = this.mActivity.getCameraIntentManager();
        this.mIsImageCaptureIntent = cameraIntentManager.isImageCaptureIntent();
        if (this.mIsImageCaptureIntent) {
            this.mSaveUri = cameraIntentManager.getExtraSavedUri();
            this.mCropValue = cameraIntentManager.getExtraCropValue();
            this.mIsSaveCaptureImage = cameraIntentManager.getExtraShouldSaveCapture().booleanValue();
            this.mQuickCapture = cameraIntentManager.isQuickCapture().booleanValue();
        }
    }

    private void prepareAIWatermark() {
        this.mWatermarkItem = null;
        if (CameraSettings.isAIWatermarkOn(this.mModuleIndex)) {
            this.mWatermarkItem = DataRepository.dataItemRunning().getComponentRunningAIWatermark().getMajorWatermarkItem();
            WatermarkItem watermarkItem = this.mWatermarkItem;
            if (watermarkItem != null) {
                watermarkItem.setCaptureCoordinate(watermarkItem.getCoordinate());
            }
        }
    }

    private void prepareLLSInCaptureMode() {
        if (this.mIsLLSNeeded) {
            this.mShowLLSHint = true;
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.alertAiDetectTipHint(0, R.string.super_night_hint, -1);
            }
        }
    }

    private void prepareMultiCapture() {
        Log.d(TAG, "prepareMultiCapture");
        this.mFocusManager.removeMessages();
        this.mMultiSnapStatus = true;
        this.mMultiSnapStopRequest = false;
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setMultiSnapStopRequest(false);
        }
        Util.clearMemoryLimit();
        prepareNormalCapture();
        if (this.mMainProtocol.isFaceExists(1)) {
            this.mMainProtocol.hideFaceAnimator();
        }
        int burstNum = getBurstNum();
        if (isHeicPreferred()) {
            burstNum = Math.min(50, burstNum);
        }
        this.mTotalJpegCallbackNum = burstNum;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("For best user experience, burst capture count is limited to ");
        sb.append(this.mTotalJpegCallbackNum);
        Log.d(str, sb.toString());
        this.mHandler.removeMessages(49);
        if (!is3ALocked()) {
            this.mFocusManager.onShutter();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:106:0x02b2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void prepareNormalCapture() {
        String str;
        boolean z;
        boolean z2;
        Log.d(TAG, "prepareNormalCapture");
        CameraSize cameraSize = null;
        if (this.mMagneticSensorDetect != null && this.mActivity.getSensorStateManager().isMagneticFieldUncalibratedEnable()) {
            this.mMagneticSensorDetect.updateMagneticDetection();
            if (isHdrSceneDetectionStarted() && !this.mMagneticSensorDetect.isLockHDRChecker("prepareNormalCapture-2")) {
                if (this.mMutexModePicker.isHdr()) {
                    this.mCamera2Device.getCameraConfigs().setHdrCheckerEvValue(null);
                    this.mCamera2Device.getCameraConfigs().setHdrCheckerSceneType(0);
                    this.mCamera2Device.getCameraConfigs().setHdrCheckerAdrc(0);
                    if (!this.mIsInHDR) {
                        onHdrSceneChanged(false);
                        updateHDRTip(false);
                    }
                } else if (this.mIsInHDR) {
                    onHdrSceneChanged(true);
                    updateHDRTip(true);
                }
            }
            this.mMagneticSensorDetect.recordMagneticInfo();
        }
        initFlashAutoStateForTrack(this.mCamera2Device.isNeedFlashOn());
        this.mEnabledPreviewThumbnail = false;
        this.mTotalJpegCallbackNum = 1;
        this.mReceivedJpegCallbackNum = 0;
        this.mCaptureStartTime = System.currentTimeMillis();
        this.mCamera2Device.setCaptureTime(this.mCaptureStartTime);
        ScenarioTrackUtil.trackCaptureTimeStart(isFrontCamera(), this.mModuleIndex);
        ScenarioTrackUtil.trackShotToGalleryStart(isFrontCamera(), this.mModuleIndex, this.mCaptureStartTime);
        ScenarioTrackUtil.trackShotToViewStart(isFrontCamera(), this.mModuleIndex, this.mCaptureStartTime);
        this.mLastCaptureTime = this.mCaptureStartTime;
        synchronized (this.mMateDataParserLock) {
            setCameraState(3);
        }
        if (!this.mMultiSnapStatus && CameraSettings.isTimerBurstEnable()) {
            DataRepository.dataItemLive().getTimerBurstController();
            if (TimerBurstController.isSupportTimerBurst(this.mModuleIndex)) {
                this.mCamera2Device.setInTimerBurstShotting(true);
                AutoLockManager.getInstance(this.mActivity).cancelHibernate();
            }
        }
        if (this.mModuleIndex == 182) {
            this.mJpegRotation = 0;
        } else {
            this.mJpegRotation = Util.getJpegRotation(this.mBogusCameraId, this.mOrientation);
            if (this.mCamera2Device.isInTimerBurstShotting()) {
                DataRepository.dataItemLive().getTimerBurstController().getOrientation(true, this.mOrientation);
                this.mJpegRotation = DataRepository.dataItemLive().getTimerBurstController().getJpegRotation(this.mJpegRotation);
            }
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("prepareNormalCapture: mOrientation = ");
        sb.append(this.mOrientation);
        sb.append(", mJpegRotation = ");
        sb.append(this.mJpegRotation);
        Log.d(str2, sb.toString());
        this.mCamera2Device.setJpegRotation(this.mJpegRotation);
        prepareAIWatermark();
        Location updateLocation = updateLocation();
        this.mCamera2Device.setGpsLocation(updateLocation);
        this.mLocation = updateLocation;
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        if (mainContentProtocol == null || !mainContentProtocol.isFaceExists(1)) {
            this.mCamera2Device.setIsFaceExist(false);
            str = DebugInfoUtil.getRetriveFaceInfo(null);
        } else {
            this.mCamera2Device.setIsFaceExist(true);
            str = DebugInfoUtil.getRetriveFaceInfo(this.mMainProtocol.getViewRects(this.mPictureSize));
        }
        this.mDebugFaceInfos = str;
        this.mUpscaleImageWithSR = false;
        if (!this.mMultiSnapStatus) {
            z = shouldDoQCFA(this.mCamera2Device.getPreviewCaptureResult());
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("prepareNormalCapture: qcfa = ");
            sb2.append(z);
            Log.d(str3, sb2.toString());
            if (CameraSettings.isUltraPixelOn() && C0122O00000o.instance().OOOoooO() && CameraSettings.isSRTo108mModeOn() && !z) {
                this.mUpscaleImageWithSR = true;
                cameraSize = this.SIZE_108M;
            }
        } else {
            z = false;
        }
        this.mCamera2Device.getCameraConfigs().setLockedAlgoSize(cameraSize);
        this.mCamera2Device.getCameraConfigs().setDoRemosaic(z);
        updateSuperNight();
        updateFrontMirror();
        updateBeauty();
        updateSRAndMFNR();
        updateShotDetermine();
        updateDoDepurple();
        updateCaptureTriggerFlow();
        updateRawCapture();
        boolean z3 = CameraSettings.isLiveShotOn() && isLiveShotAvailable(this.mCamera2Device.getCameraConfigs().getShotType());
        int i = z3 ? 256 : this.mOutputPictureFormat;
        String generateFileTitle = generateFileTitle();
        String str4 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("prepareNormalCapture title = ");
        sb3.append(generateFileTitle);
        Log.k(3, str4, sb3.toString());
        String generateFilepath4Image = Storage.generateFilepath4Image(generateFileTitle, CompatibilityUtils.isHeicImageFormat(i));
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        boolean z4 = !this.mMultiSnapStatus && (this.mEnableParallelSession || this.mEnableShot2Gallery || updateAnchorFramePreview() || (CameraSettings.isTimerBurstEnable() && TimerBurstController.isSupportTimerBurst(this.mModuleIndex)));
        camera2Proxy.setShotSavePath(generateFilepath4Image, z4);
        int i2 = this.mModuleIndex;
        if (i2 == 163 || i2 == 165 || i2 == 171 || i2 == 175 || i2 == 186 || i2 == 182) {
            boolean z5 = CameraSettings.isCameraQuickShotEnable() || CameraSettings.isCameraQuickShotAnimateEnable();
            boolean z6 = this.mSupportShotBoost;
            if (z5 || z6) {
                z2 = true;
                this.mCamera2Device.setNeedSequence(z2);
                this.mCamera2Device.setModuleAnchorFrame(updateAnchorFramePreview());
                if (enablePreviewAsThumbnail() || this.mMutexModePicker.isHdr()) {
                    this.mQuickShotAnimateEnable = false;
                } else {
                    this.mQuickShotAnimateEnable = CameraSettings.isCameraQuickShotAnimateEnable();
                }
                setWaterMark();
                setPictureOrientation();
                updateJpegQuality();
                updateAlgorithmName();
                if (needShowThumbProgressImmediately()) {
                    updateThumbProgress(false);
                }
                prepareSuperNight();
                prepareSuperNightInCaptureMode();
                prepareLLSInCaptureMode();
                checkMoreFrameCaptureLockAFAE(true);
                this.mCamera2Device.getCameraConfigs().setOutputSize(this.mOutputPictureSize);
                this.mFirstCreateCapture = false;
                this.mFlashChangeCapture = false;
            }
        }
        z2 = false;
        this.mCamera2Device.setNeedSequence(z2);
        this.mCamera2Device.setModuleAnchorFrame(updateAnchorFramePreview());
        if (enablePreviewAsThumbnail()) {
        }
        this.mQuickShotAnimateEnable = false;
        setWaterMark();
        setPictureOrientation();
        updateJpegQuality();
        updateAlgorithmName();
        if (needShowThumbProgressImmediately()) {
        }
        prepareSuperNight();
        prepareSuperNightInCaptureMode();
        prepareLLSInCaptureMode();
        checkMoreFrameCaptureLockAFAE(true);
        this.mCamera2Device.getCameraConfigs().setOutputSize(this.mOutputPictureSize);
        this.mFirstCreateCapture = false;
        this.mFlashChangeCapture = false;
    }

    private void prepareSuperNight() {
        if (this.mModuleIndex == 173) {
            if (DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode()) {
                DataRepository.dataItemRunning().initMultiFrameTotalCaptureDuration(SuperNightEvValue.getTotalExposureTime(CaptureResultParser.getSuperNightCheckerEv(this.mCamera2Device.getPreviewCaptureResult())));
            }
            Log.d(TAG, "prepareSuperNight: startCpuBoost");
            startCpuBoost(4);
            if (!DataRepository.dataItemGlobal().isOnSuperNightAlgoUpAndQuickShot() || DataRepository.dataItemRunning().isSuperNightCaptureWithKnownDuration()) {
                if (this.mSuperNightEventConsumer == null) {
                    this.mSuperNightEventConsumer = new SuperNightEventConsumer();
                }
                if (DataRepository.dataItemRunning().isSuperNightCaptureWithKnownDuration()) {
                    this.mSuperNightEventEmitter = PublishSubject.create();
                    this.mSuperNightDisposable = this.mSuperNightEventEmitter.observeOn(AndroidSchedulers.mainThread()).subscribe(this.mSuperNightEventConsumer);
                    Log.d(TAG, "prepareSuperNight: emitter STATE START");
                    this.mSuperNightEventEmitter.onNext(Integer.valueOf(1));
                    return;
                }
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                if (recordState != null) {
                    recordState.onPrepare();
                    recordState.onStart();
                }
                this.mSuperNightDisposable = Observable.just(Integer.valueOf(300), Integer.valueOf(2000)).flatMap(new Function() {
                    public ObservableSource apply(Integer num) {
                        return Observable.just(num).delaySubscription((long) num.intValue(), TimeUnit.MILLISECONDS);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(this.mSuperNightEventConsumer);
            }
        }
    }

    private void prepareSuperNightInCaptureMode() {
        if (!isRepeatingRequestInProgress() && CameraSettings.isSuperNightOn() && !this.mCamera2Device.isNeedFlashOn()) {
            this.mShowSuperNightHint = true;
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null) {
                topAlert.alertAiDetectTipHint(0, R.string.super_night_hint, -1);
            }
        }
    }

    private void previewWhenSessionSuccess() {
        setCameraState(1);
        this.mFaceDetectionEnabled = false;
        updatePreferenceInWorkThread(UpdateConstant.CAMERA_TYPES_ON_PREVIEW_SUCCESS);
        if (ModuleManager.isProPhotoModule()) {
            updatePreferenceInWorkThread(UpdateConstant.CAMERA_TYPES_MANUALLY);
        }
    }

    @MainThread
    private void realConsumeAiSceneResult(int i, boolean z) {
        if (i == 36) {
            i = 0;
        }
        if (this.mCurrentAiScene == i) {
            if (i == 0) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null && topAlert.getCurrentAiSceneLevel() == i) {
                    return;
                }
            } else {
                return;
            }
        }
        if (!isDoingAction() && isAlive() && !this.mActivity.isActivityPaused() && (!z || !this.isResetFromMutex)) {
            if (!z) {
                this.isResetFromMutex = false;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("consumeAiSceneResult: ");
            sb.append(i);
            sb.append("; isReset: ");
            sb.append(z);
            Log.d(str, sb.toString());
            TopAlert topAlert2 = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            boolean z2 = true;
            if (!isFrontCamera() && !C0124O00000oO.Oo0000o()) {
                this.mCamera2Device.setCameraAI30(i == 25);
            }
            if (this.mIsGoogleLensAvailable) {
                if (!O00000o.O0000oo0(this.mCurrentAiScene) && O00000o.O0000oo0(i)) {
                    this.mIsAiConflict = true;
                    showOrHideChip(false);
                } else if (O00000o.O0000oo0(this.mCurrentAiScene) && !O00000o.O0000oo0(i)) {
                    this.mIsAiConflict = false;
                    showOrHideChip(true);
                }
            }
            closeMoonMode(i, 8);
            closeBacklightTip(i, 8);
            if (i != -1) {
                String str2 = "e";
                if (i == 1) {
                    int parseInt = Integer.parseInt(CameraSettings.getSharpness());
                    if (parseInt < 6) {
                        parseInt++;
                    }
                    this.mCurrentAiScene = i;
                    configChanges.restoreAllMutexElement(str2);
                    this.mCamera2Device.setSharpness(parseInt);
                } else if (i != 10) {
                    if (i == 15 || i == 19) {
                        int parseInt2 = Integer.parseInt(CameraSettings.getSharpness());
                        if (parseInt2 < 6) {
                            parseInt2++;
                        }
                        this.mCamera2Device.setSharpness(parseInt2);
                    } else if (i != 3) {
                        if (i == 4) {
                            this.mCamera2Device.setContrast(Integer.parseInt(CameraSettings.getContrast()));
                            this.mCurrentAiScene = i;
                            configChanges.restoreAllMutexElement(str2);
                            updateSuperResolution();
                        } else if (!(i == 7 || i == 8)) {
                            if (i != 34) {
                                if (i == 35) {
                                    if (showMoonMode(false)) {
                                        topAlert2.setAiSceneImageLevel(i);
                                        trackAISceneChanged(this.mModuleIndex, i);
                                        this.mCurrentAiScene = i;
                                        return;
                                    }
                                    z2 = false;
                                } else if (i != 37) {
                                    if (i != 38) {
                                        switch (i) {
                                            case 25:
                                                trackAISceneChanged(this.mModuleIndex, 25);
                                                topAlert2.setAiSceneImageLevel(25);
                                                setAiSceneEffect(25);
                                                this.mCurrentAiScene = i;
                                                updateHDRPreference();
                                                configChanges.restoreAllMutexElement(str2);
                                                resumePreviewInWorkThread();
                                                return;
                                            case 26:
                                            case 27:
                                            case 28:
                                            case 29:
                                            case 30:
                                            case 31:
                                                if (!C0122O00000o.instance().OOo0Oo0()) {
                                                    configChanges.restoreAllMutexElement(str2);
                                                    updatePreferenceInWorkThread(11);
                                                    updatePreferenceInWorkThread(UpdateConstant.AI_SCENE_CONFIG);
                                                    break;
                                                }
                                                break;
                                            default:
                                                if (this.mMagneticSensorDetect == null || !this.mActivity.getSensorStateManager().isMagneticFieldUncalibratedEnable() || !this.mMagneticSensorDetect.isLockHDRChecker("realConsumeAiSceneResult")) {
                                                    updateHDRPreference();
                                                }
                                                configChanges.restoreAllMutexElement(str2);
                                                updatePreferenceInWorkThread(UpdateConstant.AI_SCENE_CONFIG);
                                                break;
                                        }
                                    } else {
                                        boolean aIWatermarkEnable = DataRepository.dataItemRunning().getComponentRunningAIWatermark().getAIWatermarkEnable(this.mModuleIndex);
                                        if (C0122O00000o.instance().OOO0OoO()) {
                                            int i2 = this.mModuleIndex;
                                            if ((i2 == 163 || i2 == 165) && !CameraSettings.isMacroModeEnabled(this.mModuleIndex) && !CameraSettings.isUltraPixelPortraitFrontOn() && !aIWatermarkEnable) {
                                                trackAISceneChanged(this.mModuleIndex, i);
                                                topAlert2.setAiSceneImageLevel(i);
                                                this.mCurrentAiScene = i;
                                                return;
                                            }
                                        }
                                    }
                                    i = 0;
                                }
                            }
                            this.mCurrentAiScene = i;
                            z2 = false;
                        }
                    }
                    this.mCurrentAiScene = i;
                    configChanges.restoreAllMutexElement(str2);
                } else {
                    String componentValue = DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex);
                    if (C0122O00000o.instance().OOoOoOo() && (componentValue.equals("3") || componentValue.equals("1"))) {
                        configChanges.closeMutexElement(str2, 193);
                        setFlashMode("0");
                    }
                    updateMfnr(true);
                    updateOIS();
                }
                trackAISceneChanged(this.mModuleIndex, i);
                topAlert2.setAiSceneImageLevel(i);
                if (z2) {
                    setAiSceneEffect(i);
                }
                if (!z) {
                    this.mCurrentAiScene = i;
                }
                updateBeauty();
                resumePreviewInWorkThread();
                return;
            }
            showBacklightTip();
            topAlert2.setAiSceneImageLevel(23);
            this.mCurrentAiScene = i;
        }
    }

    /* access modifiers changed from: private */
    public void recheckAndKeepAutoHibernation() {
        updateAutoHibernation();
        if (this.mIsAutoHibernationSupported) {
            keepAutoHibernation();
        }
    }

    private void resetAiSceneInHdrOrFlashOn() {
        if (this.mAiSceneEnabled && !this.isResetFromMutex) {
            int i = this.mCurrentAiScene;
            if (i != 0) {
                if (i == -1 || i == 10) {
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            Camera2Module.this.consumeAiSceneResult(0, true);
                            Camera2Module.this.isResetFromMutex = true;
                        }
                    });
                }
            }
        }
    }

    private void resetAsdSceneInHdrOrFlashChange() {
        if (C0124O00000oO.Oo00Oo() && isFrontCamera()) {
            int i = this.mCurrentAsdScene;
            if (i != -1 && i == 9) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        Camera2Module.this.updateAsdSceneResult(-1);
                    }
                });
            }
        }
    }

    private void resumePreviewInWorkThread() {
        updatePreferenceInWorkThread(new int[0]);
    }

    private void setAiSceneEffect(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAiSceneEffect: ");
        sb.append(i);
        Log.d(str, sb.toString());
        boolean z = false;
        if (EffectController.getInstance().getAiColorCorrectionVersion() != 0 || !C0122O00000o.instance().OOo0000() || !CameraSettings.isBackCamera() || i != 25) {
            if (CameraSettings.isFrontCamera() || isPortraitMode()) {
                if (i != 0) {
                    Log.d(TAG, "setAiSceneEffect: front camera or portrait mode nonsupport!");
                    return;
                } else if (CameraSettings.getPortraitLightingPattern() != 0) {
                    Log.d(TAG, "setAiSceneEffect: scene = 0 but portrait lighting is on...");
                    return;
                }
            }
            int shaderEffect = CameraSettings.getShaderEffect();
            if (FilterInfo.getCategory(shaderEffect) != 5 && shaderEffect != FilterInfo.FILTER_ID_NONE) {
                return;
            }
            if (isProColorEnable()) {
                Log.d(TAG, "ProColor is enable, disable AI filter");
                return;
            }
            ArrayList filterInfo = EffectController.getInstance().getFilterInfo(5);
            if (i < 0 || i > filterInfo.size()) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("setAiSceneEffect: scene unknown: ");
                sb2.append(i);
                Log.e(str2, sb2.toString());
                return;
            }
            int aiColorCorrectionVersion = EffectController.getInstance().getAiColorCorrectionVersion();
            int createAiSceneEffectId = (aiColorCorrectionVersion != 1 && aiColorCorrectionVersion == 2 && this.mAiSceneEnabled && i == 0) ? EffectController.createAiSceneEffectId(FilterType.A_COMMON) : ((FilterInfo) filterInfo.get(i)).getId();
            EffectController.getInstance().setAiSceneEffect(createAiSceneEffectId, true);
            if (createAiSceneEffectId != FilterInfo.FILTER_ID_NONE) {
                z = true;
            }
            this.mHasAiSceneFilterEffect = z;
            return;
        }
        setAiSceneEffect(0);
        Log.d(TAG, "supportAi30: AI 3.0 back camera in HUMAN SCENE not apply filter! reset AiSceneEffect! ");
    }

    private void setCurrentAsdScene(int i) {
        this.mCurrentAsdScene = i;
    }

    private void setEffectFilter(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setEffectFilter: ");
        sb.append(i);
        Log.d(str, sb.toString());
        EffectController.getInstance().setEffect(i);
        this.mNormalFilterId = i;
        CircularMediaRecorder circularMediaRecorder = this.mCircularMediaRecorder;
        if (circularMediaRecorder != null) {
            circularMediaRecorder.setFilterId(i);
        }
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.setVideoStreamEffect(i);
        }
    }

    private void setLightingEffect() {
        int shaderEffect = CameraSettings.getShaderEffect();
        if (FilterInfo.getCategory(shaderEffect) != 5 && shaderEffect != FilterInfo.FILTER_ID_NONE) {
            return;
        }
        if (isProColorEnable()) {
            Log.d(TAG, "ProColor is enable, disable AI filter");
            return;
        }
        int portraitLightingPattern = CameraSettings.getPortraitLightingPattern();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setLightingEffect: ");
        sb.append(portraitLightingPattern);
        Log.d(str, sb.toString());
        ArrayList filterInfo = EffectController.getInstance().getFilterInfo(6);
        int i = FilterInfo.FILTER_ID_NONE;
        Iterator it = filterInfo.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            FilterInfo filterInfo2 = (FilterInfo) it.next();
            if (filterInfo2.getOrder() == portraitLightingPattern) {
                i = filterInfo2.getId();
                break;
            }
        }
        this.mLightFilterId = i;
        EffectController.getInstance().setLightingEffect(i);
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
                changeZoom4Crop();
            }
        }
    }

    /* access modifiers changed from: private */
    public void setOrientationParameter() {
        if (!isDeparted()) {
            if (!(this.mCamera2Device == null || this.mOrientation == -1)) {
                if (!isFrameAvailable() || getCameraState() != 1) {
                    CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new O0000Oo(this));
                } else {
                    updatePreferenceInWorkThread(35);
                }
            }
            CircularMediaRecorder circularMediaRecorder = this.mCircularMediaRecorder;
            if (circularMediaRecorder != null) {
                circularMediaRecorder.setOrientationHint(this.mOrientationCompensation);
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

    private void setPortraitSuccessHintVisible(int i) {
        ((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175)).setPortraitHintVisible(i);
    }

    private boolean setSceneMode(String str) {
        int parseInt = Util.parseInt(str, -1);
        this.mCamera2Device.setSceneMode(parseInt);
        if (!Util.isSupported(parseInt, this.mCameraCapabilities.getSupportedSceneModes())) {
            return false;
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("sceneMode=");
        sb.append(str);
        Log.d(str2, sb.toString());
        return true;
    }

    private void setVideoSize(int i, int i2) {
        this.mVideoSize = this.mCameraDisplayOrientation % 180 == 0 ? new CameraSize(i, i2) : new CameraSize(i2, i);
    }

    private void setWaterMark() {
        if (this.mMultiSnapStatus || CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex) || C0122O00000o.instance().OOo0ooO() || ((!this.mEnableParallelSession && (this.mModuleIndex == 165 || CameraSettings.getShaderEffect() != FilterInfo.FILTER_ID_NONE || this.mHasAiSceneFilterEffect || CameraSettings.isTiltShiftOn())) || ((this.mEnableParallelSession && ((!C0122O00000o.instance().OOoO000() || this.mModuleIndex == 171) && (!C0122O00000o.instance().OOo0ooo() || this.mModuleIndex != 175))) || this.mModuleIndex == 205))) {
            this.mCamera2Device.setDualCamWaterMarkEnable(false);
            this.mCamera2Device.setTimeWaterMarkEnable(false);
            return;
        }
        if (!CameraSettings.isDualCameraWaterMarkOpen() && !CameraSettings.isFrontCameraWaterMarkOpen()) {
            this.mCamera2Device.setDualCamWaterMarkEnable(false);
        } else {
            this.mCamera2Device.setDualCamWaterMarkEnable(true);
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

    private boolean shouldApplyNormalWideLDC() {
        if (CameraSettings.shouldNormalWideLDCBeVisibleInMode(this.mModuleIndex) && !CameraSettings.isMacroModeEnabled(this.mModuleIndex) && this.mActualCameraId != Camera2DataContainer.getInstance().getUltraWideCameraId() && !CameraSettings.isUltraPixelOn() && !isZoomRatioBetweenUltraAndWide()) {
            return CameraSettings.isNormalWideLDCEnabled();
        }
        return false;
    }

    private boolean shouldApplyUltraWideLDC() {
        if (!CameraSettings.shouldUltraWideLDCBeVisibleInMode(this.mModuleIndex)) {
            return false;
        }
        if (CameraSettings.isMacroModeEnabled(this.mModuleIndex) && !C0122O00000o.instance().OOoOO0o()) {
            return true;
        }
        if (this.mActualCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
            return CameraSettings.isUltraWideLDCEnabled();
        }
        if (isZoomRatioBetweenUltraAndWide()) {
            return CameraSettings.isUltraWideLDCEnabled();
        }
        return false;
    }

    private boolean shouldChangeAiScene(int i) {
        if (isDoingAction() || !isAlive() || this.mCurrentDetectedScene == i || System.currentTimeMillis() - this.mLastChangeSceneTime <= 300) {
            return false;
        }
        this.mCurrentDetectedScene = i;
        this.mLastChangeSceneTime = System.currentTimeMillis();
        return true;
    }

    private boolean shouldCheckLLS() {
        return this.mCameraCapabilities.isLLSSupported() && C0122O00000o.instance().OOo0oO0();
    }

    private boolean shouldDoMultiFrameCapture() {
        boolean z;
        String str;
        String str2;
        boolean z2 = false;
        if (!this.mIsMoonMode || C0122O00000o.instance().OOOOOO()) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy == null || this.mCameraCapabilities == null || !camera2Proxy.useLegacyFlashStrategy() || !this.mCamera2Device.isNeedFlashOn() || !this.mCameraCapabilities.isFlashSupported()) {
                boolean z3 = this.mModuleIndex == 167 && C0122O00000o.instance().OOoO0O0() && this.mCamera2Device.getCameraConfigs().isSuperResolutionEnabled();
                if (z3) {
                    str = TAG;
                    str2 = "shouldDoMultiFrameCapture: SR is enabled for pro mode";
                } else if (this.mUpscaleImageWithSR) {
                    str = TAG;
                    str2 = "shouldDoMultiFrameCapture: SR is enabled for upscaling image";
                } else {
                    if (C0122O00000o.instance().OOoOOoO()) {
                        z = !this.mHHTDisabled && (isFrontCamera() || this.mModuleIndex != 171);
                        String str3 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("shouldDoMultiFrameCapture: isShouldDoHHT=");
                        sb.append(z);
                        sb.append(", isHHTDisabled=");
                        sb.append(this.mHHTDisabled);
                        Log.d(str3, sb.toString());
                    } else {
                        z = false;
                    }
                    if (this.mMutexModePicker.isHdr() || z || this.mShouldDoMFNR || this.mMutexModePicker.isSuperResolution() || CameraSettings.isGroupShotOn() || DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode()) {
                        z2 = true;
                    }
                    String str4 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("shouldDoMultiFrameCapture: ");
                    sb2.append(z2);
                    sb2.append(" | ");
                    sb2.append(this.mShouldDoMFNR);
                    Log.d(str4, sb2.toString());
                    return z2;
                }
                Log.d(str, str2);
                return true;
            }
            Log.d(TAG, "shouldDoMultiFrameCapture: return false in case of flash");
            return false;
        }
        Log.d(TAG, "shouldDoMultiFrameCapture: return false in moon mode");
        return false;
    }

    private boolean shouldDoQCFA(CaptureResult captureResult) {
        boolean z = false;
        if (!isEnableQcfaForAlgoUp()) {
            return false;
        }
        if (CameraSettings.isFrontCamera() && !C0124O00000oO.O0o000O) {
            return false;
        }
        if (this.mCamera2Device.getCapabilities().isRemosaicDetecedSupported()) {
            return CaptureResultParser.isRemosaicDetected(captureResult);
        }
        Integer num = (Integer) captureResult.get(CaptureResult.SENSOR_SENSITIVITY);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("shouldDoQCFA: iso = ");
        sb.append(num);
        Log.d(str, sb.toString());
        if (num != null && num.intValue() <= 200) {
            z = true;
        }
        return z;
    }

    private void showDocumentDetectBlurHint() {
        if (!Util.isGlobalVersion()) {
            FragmentTopConfig fragmentTopConfig = (FragmentTopConfig) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (fragmentTopConfig != null) {
                fragmentTopConfig.alertAiDetectTipHint(0, R.string.document_blur_warn, 3000);
                CameraStatUtils.trackDocumentDetectBlurHintShow();
            }
        }
    }

    private boolean showMoonMode(boolean z) {
        if (!CameraSettings.isUltraWideConfigOpen(getModuleIndex()) && !CameraSettings.isMacroModeEnabled(this.mModuleIndex)) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                this.mEnteringMoonMode = true;
                topAlert.alertMoonModeSelector(0, z);
                if (!z) {
                    updateMoonNight();
                } else {
                    updateMoon(true);
                }
                if (topAlert.isHDRShowing()) {
                    topAlert.alertHDR(8, false, false);
                }
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("(moon_mode) show moon mode,button check status:");
                sb.append(z);
                Log.d(str, sb.toString());
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void showOrHideLoadingProgress(boolean z, boolean z2) {
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        if (actionProcessing != null) {
            actionProcessing.showOrHideLoadingProgress(z, z2);
        }
    }

    private void showPostCaptureAlert() {
        enableCameraControls(false);
        this.mFocusManager.removeMessages();
        stopFaceDetection(true);
        pausePreview();
        this.mMainProtocol.setEffectViewVisible(false);
        this.mMainProtocol.hideReferenceGradienter();
        this.mMainProtocol.clearFocusView(7);
        this.mMainProtocol.setIdPhotoBoxVisible(false);
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).delegateEvent(6);
        resetMetaDataManager();
    }

    /* access modifiers changed from: private */
    public void startCount(final int i, final int i2, final int i3) {
        if (!this.mMediaRecorderRecording) {
            this.mIsStartCount = true;
            if (!checkShutterCondition()) {
                this.mIsStartCount = false;
                if (i3 == 120) {
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            Camera2Module.this.startCount(i, i2, i3);
                        }
                    }, 300);
                }
            } else if (i3 != 120 || DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting()) {
                setTriggerMode(i3);
                tryRemoveCountDownMessage();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startCount: ");
                sb.append(i);
                Log.d(str, sb.toString());
                this.mCameraTimer = new CameraTimer();
                this.mCameraTimer.setCount(i).setRepeatTimes(i2).start(new Observer() {
                    public /* synthetic */ void O00ooO() {
                        Camera2Module.this.mTopAlert.hideAlert();
                    }

                    public void onComplete() {
                        Camera2Module.this.mIsStartCount = false;
                        Camera2Module.this.tryRemoveCountDownMessage();
                        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                        if (Camera2Module.this.isAlive()) {
                            Camera camera = Camera2Module.this.mActivity;
                            if (camera != null && !camera.isActivityPaused()) {
                                if (CameraSettings.isTimerBurstEnable()) {
                                    RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                                    if (TimerBurstController.isSupportTimerBurst(Camera2Module.this.mModuleIndex) && i3 != 120) {
                                        TimerBurstController timerBurstController = DataRepository.dataItemLive().getTimerBurstController();
                                        if (!timerBurstController.isInTimerBurstShotting()) {
                                            timerBurstController.setInTimerBurstShotting(true);
                                            timerBurstController.resetTimerRunningData();
                                            recordState.onPrepare();
                                            topAlert.setRecordingTimeState(1);
                                            if (Camera2Module.this.mModuleIndex == 167) {
                                                ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
                                                if (manuallyAdjust != null) {
                                                    manuallyAdjust.setManuallyLayoutVisible(false);
                                                }
                                            }
                                            Camera2Module.this.recheckAndKeepAutoHibernation();
                                        }
                                    }
                                } else if (topAlert != null) {
                                    Camera2Module.this.mTopAlert.reInitAlert(true);
                                }
                            }
                        }
                    }

                    public void onError(Throwable th) {
                        Camera2Module.this.mIsStartCount = false;
                    }

                    public void onNext(Long l) {
                        int intValue = l.intValue();
                        AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
                        if (intValue == i) {
                            Camera2Module.this.mTopAlert.hideAlert();
                            Camera2Module.this.playCameraSound(7);
                            Camera2Module.this.mTopAlert.showDelayNumber(intValue);
                            if (autoHibernation == null) {
                                return;
                            }
                        } else if (intValue == 0) {
                            Camera2Module.this.mTopAlert.hideDelayNumber();
                            if (Camera2Module.this.mIsSatFallback == 0 || !Camera2Module.this.shouldCheckSatFallbackState()) {
                                Camera2Module camera2Module = Camera2Module.this;
                                camera2Module.mWaitingSnapshot = false;
                                camera2Module.startNormalCapture(camera2Module.getTriggerMode());
                                if (Camera2Module.this.mModuleIndex == 167 && DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(167) && autoHibernation != null) {
                                    autoHibernation.updateTimerBurstAnimator();
                                }
                            } else {
                                Camera2Module.this.mWaitingSnapshot = true;
                                Log.w(Camera2Module.TAG, "capture check in startCount: sat fallback");
                            }
                            if (autoHibernation != null) {
                                autoHibernation.stopAutoHibernationCaptureDelayNumber();
                                return;
                            }
                            return;
                        } else {
                            Camera2Module.this.playCameraSound(5);
                            Camera2Module.this.mTopAlert.showDelayNumber(intValue);
                            if (autoHibernation == null) {
                                return;
                            }
                        }
                        autoHibernation.startAutoHibernationCaptureDelayNumber();
                    }

                    public void onSubscribe(Disposable disposable) {
                        if (Camera2Module.this.mTopAlert != null) {
                            AndroidSchedulers.mainThread().scheduleDirect(new C0343O00000oo(this), 120, TimeUnit.MILLISECONDS);
                        }
                        Camera2Module.this.mMainProtocol.clearFocusView(7);
                    }
                });
            }
        }
    }

    private void startCpuBoost(int i) {
        if (C0124O00000oO.isMTKPlatform()) {
            if (this.mCpuBoost == null) {
                this.mCpuBoost = new BoostFrameworkImpl();
            }
            BaseBoostFramework baseBoostFramework = this.mCpuBoost;
            if (baseBoostFramework != null) {
                baseBoostFramework.startBoost(0, i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void startLensActivity() {
        if (CameraSettings.supportGoogleLens()) {
            Camera camera = this.mActivity;
            if (camera != null) {
                camera.startLensActivity();
                return;
            }
            return;
        }
        Log.d(TAG, "start ai lens");
        try {
            Intent intent = new Intent();
            intent.setAction("android.media.action.XIAOAI_CONTROL");
            intent.setPackage(CameraSettings.AI_LENS_PACKAGE);
            intent.putExtra("preview_width", this.mPreviewSize.width);
            intent.putExtra("preview_height", this.mPreviewSize.height);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        } catch (Exception e) {
            Log.e(TAG, "onClick: occur a exception", (Throwable) e);
        }
    }

    private void startLiveShotAnimation() {
        synchronized (this.mCircularMediaRecorderStateLock) {
            if (!(this.mCircularMediaRecorder == null || this.mHandler == null)) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                        if (topAlert != null) {
                            topAlert.startLiveShotAnimation();
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void startNormalCapture(int i) {
        C0373O000o00o o000o00o;
        Camera2Proxy camera2Proxy;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startNormalCapture mode -> ");
        sb.append(i);
        Log.k(3, str, sb.toString());
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null && DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(this.mModuleIndex)) {
            recordState.onLongExposeStart();
        }
        this.mActivity.getScreenHint().updateHint();
        this.mActivity.getImageSaver().setSuperNightExifSupport(this.mCameraCapabilities.isSuperNightExifTagDefined());
        if (Storage.isLowStorageAtLastPoint()) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Not enough space or storage not ready. remaining=");
            sb2.append(Storage.getLeftSpace());
            Log.k(4, str2, sb2.toString());
            return;
        }
        if (this.mIsImageCaptureIntent) {
            Camera camera = this.mActivity;
            if (!(camera == null || camera.getImageSaver() == null)) {
                this.mActivity.getImageSaver().setDropBitmapTexture(false);
            }
        }
        prepareNormalCapture();
        if (!CameraSettings.isGroupShotOn() || isParallelSessionEnable()) {
            this.mHandler.sendEmptyMessageDelayed(50, calculateTimeout(this.mModuleIndex));
            this.mCamera2Device.setQuickShotAnimation(this.mQuickShotAnimateEnable);
            if (C0122O00000o.instance().OOOO0oO()) {
                if ((getModuleIndex() == 163 || getModuleIndex() == 165 || getModuleIndex() == 186 || getModuleIndex() == 182) && HybridZoomingSystem.isZoomRatioNone(getZoomRatio(), isFrontCamera())) {
                    this.mCamera2Device.setFlawDetectEnable(true);
                } else {
                    this.mCamera2Device.setFlawDetectEnable(false);
                }
            }
            if (this.mCamera2Device.getCameraConfigs().isHDREnabled() && this.mCameraCapabilities.isSupportAIIE() && this.mAiSceneEnabled && this.mCamera2Device.getCameraConfigs().isAIIEPreviewEnabled()) {
                this.mCamera2Device.setAIIEPreviewEnable(false);
                resumePreviewInWorkThread();
            }
            if (isFrontCamera() && this.mSupportShotBoost && !this.mIsImageCaptureIntent) {
                int i2 = this.mModuleIndex;
                if (i2 == 165 || i2 == 163) {
                    int i3 = this.mOperatingMode;
                    if (!(32775 == i3 || 32778 == i3)) {
                        camera2Proxy = this.mCamera2Device;
                        o000o00o = new C0373O000o00o(this);
                        camera2Proxy.setShotBoostParams(o000o00o);
                        this.mCamera2Device.takePicture(this, this.mActivity.getImageSaver());
                        if (needQuickShot() || i == 90 || this.mFixedShot2ShotTime != -1) {
                            this.mBlockQuickShot = true;
                            String str3 = TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("isParallelSessionEnable:");
                            sb3.append(isParallelSessionEnable());
                            sb3.append(", and block quick shot");
                            Log.d(str3, sb3.toString());
                        } else {
                            Log.d(TAG, "startNormalCapture force set CameraStateConstant.IDLE");
                            setCameraState(1);
                            enableCameraControls(true);
                        }
                    }
                }
            }
            camera2Proxy = this.mCamera2Device;
            o000o00o = null;
            camera2Proxy.setShotBoostParams(o000o00o);
            this.mCamera2Device.takePicture(this, this.mActivity.getImageSaver());
            if (needQuickShot()) {
            }
            this.mBlockQuickShot = true;
            String str32 = TAG;
            StringBuilder sb32 = new StringBuilder();
            sb32.append("isParallelSessionEnable:");
            sb32.append(isParallelSessionEnable());
            sb32.append(", and block quick shot");
            Log.d(str32, sb32.toString());
        } else {
            this.mCamera2Device.captureGroupShotPictures(this, this.mActivity.getImageSaver(), this.mTotalJpegCallbackNum, this.mActivity);
            this.mBlockQuickShot = true;
        }
    }

    private void stopCpuBoost() {
        if (C0124O00000oO.isMTKPlatform()) {
            BaseBoostFramework baseBoostFramework = this.mCpuBoost;
            if (baseBoostFramework != null) {
                baseBoostFramework.stopBoost();
                this.mCpuBoost = null;
            }
        }
    }

    /* access modifiers changed from: private */
    public void stopMultiSnap() {
        Log.d(TAG, "stopMultiSnap: start");
        this.mHandler.removeMessages(49);
        if (this.mMultiSnapStatus) {
            this.mLastCaptureTime = System.currentTimeMillis();
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null && this.mMultiSnapStatus) {
                camera2Proxy.captureAbortBurst();
                this.mMultiSnapStatus = false;
            }
            int i = this.mReceivedJpegCallbackNum;
            HashMap hashMap = new HashMap();
            hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
            boolean z = true;
            trackGeneralInfo(hashMap, i, true, this.mBeautyValues, this.mLocation != null, this.mCurrentAiScene);
            PictureTakenParameter pictureTakenParameter = new PictureTakenParameter();
            pictureTakenParameter.takenNum = i;
            pictureTakenParameter.burst = true;
            if (this.mLocation == null) {
                z = false;
            }
            pictureTakenParameter.location = z;
            pictureTakenParameter.aiSceneName = getCurrentAiSceneName();
            pictureTakenParameter.isEnteringMoon = this.mEnteringMoonMode;
            pictureTakenParameter.isSelectMoonMode = this.mIsMoonMode;
            pictureTakenParameter.isSuperNightInCaptureMode = this.mShowSuperNightHint;
            pictureTakenParameter.beautyValues = this.mBeautyValues;
            pictureTakenParameter.isNearRangeMode = this.mIsNearRangeMode;
            trackPictureTaken(pictureTakenParameter);
            animateCapture();
            this.mUpdateImageTitle = false;
            this.mHandler.sendEmptyMessageDelayed(48, 800);
        }
    }

    /* access modifiers changed from: private */
    public void stopTimerBurst() {
        keepScreenOnAwhile();
        AutoLockManager.getInstance(this.mActivity).cancelHibernate();
        TimerBurstController timerBurstController = DataRepository.dataItemLive().getTimerBurstController();
        timerBurstController.setPendingStopTimerBurst(true);
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onFinish();
        }
        this.mHandler.removeMessages(63);
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null) {
            dualController.showZoomButton();
        }
        CameraStatUtils.trackTimerBurst(CameraSettings.getTimerBurstTotalCount(), (float) timerBurstController.getIntervalTimer(), timerBurstController.getCaptureIndex() - 1, this.mIsAutoHibernationSupported, this.mEnterAutoHibernationCount);
        timerBurstController.setInTimerBurstShotting(false);
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setInTimerBurstShotting(false);
        }
        if (this.mModuleIndex == 167) {
            ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
            if (manuallyAdjust != null) {
                manuallyAdjust.setManuallyLayoutVisible(true);
            }
        }
        CameraTimer cameraTimer = this.mCameraTimer;
        if (cameraTimer != null && cameraTimer.isRunning()) {
            this.mTopAlert.hideDelayNumber();
            this.mCameraTimer.dispose();
        }
        this.mIsStartCount = false;
        this.mTopAlert.reInitAlert(true);
        if (this.mIsAutoHibernationSupported) {
            exitAutoHibernation();
            AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
            if (autoHibernation != null) {
                autoHibernation.dismissAutoHibernation();
            }
        }
    }

    private boolean supportAnchorFrameAsThumbnail() {
        boolean z = false;
        if (!CameraSettings.isCameraParallelProcessEnable() || this.mIsImageCaptureIntent || CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex) || C0122O00000o.instance().oOo00()) {
            return false;
        }
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities != null && cameraCapabilities.isSupportAnchorFrame()) {
            if (this.mCameraCapabilities.getAnchorFrameMask() == 0) {
                if (!isFrontCamera()) {
                    int i = this.mModuleIndex;
                    if (i == 163 || i == 165 || anchorFrameWhenPortrait()) {
                        z = true;
                    }
                }
                return z;
            } else if (isFrontCamera()) {
                int i2 = this.mModuleIndex;
                if (i2 == 163 || i2 == 165) {
                    return this.mCameraCapabilities.isAnchorFrameType(1, 100);
                }
                if (i2 != 171) {
                    return false;
                }
                return this.mCameraCapabilities.isAnchorFrameType(1, 101);
            } else {
                int i3 = this.mModuleIndex;
                if (i3 == 163 || i3 == 165) {
                    return this.mCameraCapabilities.isAnchorFrameType(0, 1);
                }
                if (i3 == 171 && this.mCameraCapabilities.isAnchorFrameType(0, 7)) {
                    return anchorFrameWhenPortrait();
                }
                if (CameraSettings.isUltraPixelOn()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void trackAISceneChanged(int i, int i2) {
        CameraStatUtils.trackAISceneChanged(i, i2, getResources());
    }

    private void trackBeautyInfo(int i, boolean z, BeautyValues beautyValues) {
        CameraStatUtils.trackBeautyInfo(i, z ? "front" : "back", beautyValues);
    }

    private void trackCaptureModuleInfo(Map map, int i, boolean z, boolean z2) {
        if (map == null) {
            map = new HashMap();
        }
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        DataRepository.dataItemConfig();
        int triggerMode = getTriggerMode();
        boolean isFrontCamera = isFrontCamera();
        int i2 = this.mModuleIndex;
        map.put("attr_trigger_mode", CameraStatUtils.triggerModeToName(triggerMode));
        String str = "on";
        String str2 = "off";
        map.put(LiveShotAttr.PARAM_LIVESHOT, CameraSettings.isLiveShotOn() ? str : str2);
        map.put(BaseEvent.QUALITY, CameraSettings.getEncodingQuality(z).name().toLowerCase(Locale.ENGLISH));
        map.put(Setting.PARAM_TIME_WATERMARK, CameraSettings.isTimeWaterMarkOpen() ? str : str2);
        boolean z3 = CameraSettings.isDualCameraWaterMarkOpen() || CameraSettings.isFrontCameraWaterMarkOpen();
        map.put(Setting.PARAM_DEVICE_WATERMARK, z3 ? str : str2);
        if (!isFrontCamera) {
            String componentValue = (z || !CameraSettings.isTiltShiftOn()) ? str2 : dataItemRunning.getComponentRunningTiltValue().getComponentValue(i2);
            map.put(Setting.PARAM_TILTSHIFT, componentValue);
            if (z || !this.isGradienterOn) {
                str = str2;
            }
            map.put(Setting.PARAM_GRADIENTER, str);
            if (C0122O00000o.instance().OO0ooo0() || C0122O00000o.instance().OO0ooo()) {
                map.put(Setting.PARAM_DOCUMENT_MODE, CameraStatUtils.getDocumentModeValue(this.mModuleIndex));
            }
        }
        map.put(Setting.PARAM_AICC, EffectController.getInstance().getAiColorCorrectionVersion() >= 1 ? Boolean.valueOf(this.mAiSceneEnabled) : Boolean.valueOf(false));
        map.put(Setting.PARAM_HEIC, isHeicPreferred() ? Boolean.valueOf(CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat)) : Boolean.valueOf(false));
        if (z2) {
            map.put(CaptureAttr.PARAM_NEAR_RANGE_MODE, CameraStatUtils.modeIdToName(this.mModuleIndex));
        }
        if (!DataRepository.dataItemGlobal().isIntentIDPhoto()) {
            MistatsWrapper.mistatEvent("M_capture_", map);
        }
        if (z) {
            HashMap hashMap = new HashMap();
            hashMap.put(CaptureSence.PARAM_BURST_COUNT, CameraStatUtils.burstShotNumToName(i));
            MistatsWrapper.mistatEventSimple(CaptureSence.KEY_BURST_SHOT_TIMES, hashMap);
        }
    }

    private void trackManualInfo(int i) {
        int i2 = i;
        CameraStatUtils.trackPictureTakenInManual(i2, getManualValue(CameraSettings.KEY_WHITE_BALANCE, getString(R.string.pref_camera_whitebalance_default)), getManualValue(CameraSettings.KEY_QC_EXPOSURETIME, getString(R.string.pref_camera_exposuretime_default)), getManualValue(CameraSettings.KEY_QC_ISO, getString(R.string.pref_camera_iso_default)), getManualValue(CameraSettings.KEY_QC_MANUAL_EXPOSURE_VALUE, getString(R.string.pref_camera_iso_default)), this.mModuleIndex, getActualCameraId());
    }

    private boolean triggerHDR(boolean z) {
        if (!this.isZooming && isDoingAction()) {
            return false;
        }
        ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
        if (componentHdr.isEmpty()) {
            return false;
        }
        String componentValue = componentHdr.getComponentValue(this.mModuleIndex);
        if (!"auto".equals(componentValue) && !componentHdr.isHdrOnWithChecker(componentValue)) {
            return false;
        }
        if (z) {
            if ((getZoomRatio() > HybridZoomingSystem.getZoomRatioNone(isFrontCamera(), this.mOrientation) && ComponentConfigHdr.getHdrUIStatus(componentValue) != 1 && !C0122O00000o.instance().OOo0OOO()) || this.mCurrentAiScene == -1) {
                return false;
            }
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null && camera2Proxy.isNeedFlashOn() && !this.mSupportFlashHDR) {
                return false;
            }
        }
        return !this.mIsNeedNightHDR;
    }

    private void unlockAEAF() {
        Log.d(TAG, "unlockAEAF");
        this.m3ALocked = false;
        if (this.mAeLockSupported) {
            if (isDeviceAlive()) {
                this.mCamera2Device.unlockExposure();
            }
            if (!C0122O00000o.instance().OOoOo0() && this.mUltraWideAELocked) {
                String focusMode = CameraSettings.getFocusMode();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unlockAEAF: focusMode = ");
                sb.append(focusMode);
                Log.d(str, sb.toString());
                setFocusMode(focusMode);
                this.mUltraWideAELocked = false;
            }
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(false);
        }
    }

    private void unregisterSensor() {
        if (this.isGradienterOn) {
            this.mActivity.getSensorStateManager().setGradienterEnabled(false);
        }
        this.mActivity.getSensorStateManager().setLieIndicatorEnabled(false);
        if (C0122O00000o.instance().OOo0O0o()) {
            this.mActivity.getSensorStateManager().setMagneticFieldUncalibratedEnable(false);
        }
        this.mIsShowLyingDirectHintStatus = -1;
        this.mHandler.removeMessages(58);
    }

    private boolean updateAIWatermark() {
        boolean needActiveASD = needActiveASD();
        if (this.mAIWatermarkEnable != needActiveASD) {
            FunctionParseAiScene functionParseAiScene = this.mFunctionParseAiScene;
            if (functionParseAiScene != null) {
                functionParseAiScene.resetScene();
            }
            this.mAIWatermarkEnable = needActiveASD;
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            boolean z = this.mAiSceneEnabled || this.mAIWatermarkEnable;
            camera2Proxy.setAiASDEnable(z);
            if (this.mAIWatermarkEnable) {
                this.mCamera2Device.setASDPeriod(300);
            }
        }
        return this.mAIWatermarkEnable;
    }

    private void updateASD() {
        if (163 == getModuleIndex() || 165 == getModuleIndex() || 171 == getModuleIndex() || 186 == getModuleIndex() || 182 == getModuleIndex() || 205 == getModuleIndex() || 188 == getModuleIndex()) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.setASDEnable(true);
            }
        }
    }

    private void updateASDDirtyDetect() {
        boolean isLensDirtyDetectEnabled = CameraSettings.isLensDirtyDetectEnabled();
        boolean z = DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_LENS_DIRTY_TIP, C0122O00000o.instance().O0oo00());
        Camera2Proxy cameraDevice = getCameraDevice();
        if (cameraDevice != null) {
            boolean z2 = isLensDirtyDetectEnabled && z && CameraSettings.shouldShowLensDirtyDetectHint();
            cameraDevice.setAsdDirtyEnable(z2);
        }
    }

    private void updateAiScene() {
        FunctionParseAiScene functionParseAiScene = this.mFunctionParseAiScene;
        if (functionParseAiScene != null) {
            functionParseAiScene.resetScene();
        }
        this.mCurrentAiScene = 0;
        this.mAiSceneEnabled = CameraSettings.getAiSceneOpen(this.mModuleIndex);
        if (isFrontCamera() && this.mActivity.isScreenSlideOff()) {
            this.mAiSceneEnabled = false;
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        boolean z = this.mAiSceneEnabled || this.mAIWatermarkEnable;
        camera2Proxy.setAiASDEnable(z);
        if (!this.mAiSceneEnabled || !this.mCameraCapabilities.isSupportAIIE()) {
            this.mCamera2Device.setAIIEPreviewEnable(false);
        } else {
            this.mCamera2Device.setAIIEPreviewEnable(true);
        }
        if ((isFrontCamera() && ModuleManager.isCapture()) || !this.mAiSceneEnabled) {
            this.mCamera2Device.setCameraAI30(this.mAiSceneEnabled);
        }
        setAiSceneEffect(this.mCurrentAiScene);
        this.mCamera2Device.setASDScene(this.mCurrentAiScene);
        if (this.mAiSceneEnabled) {
            this.mCamera2Device.setASDPeriod(300);
            return;
        }
        hideSceneSelector();
        if (this.mMagneticSensorDetect == null || !this.mActivity.getSensorStateManager().isMagneticFieldUncalibratedEnable() || !this.mMagneticSensorDetect.isLockHDRChecker("updateAiScene")) {
            updateHDRPreference();
        }
        updateFlashPreference();
        updateBeauty();
    }

    private void updateAiShutter() {
        if (this.mCameraCapabilities.isSupportAiShutter()) {
            this.mIsAiShutterOn = CameraSettings.isAiShutterOn(this.mModuleIndex);
            this.mCamera2Device.applyAiShutterEnable(this.mIsAiShutterOn);
        }
    }

    private void updateAiShutterExistMotion(CaptureResult captureResult) {
        if (this.mIsAiShutterOn) {
            boolean isAishutExistMotion = CaptureResultParser.isAishutExistMotion(captureResult);
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.setAiShutterExistMotion(isAishutExistMotion);
            }
        }
    }

    private void updateAlgorithmName() {
        String str;
        if (C0124O00000oO.OOoOooo()) {
            str = null;
        } else if (this.mCamera2Device.isSingleBokehEnabled()) {
            str = C0122O00000o.instance().O0ooOO0() > 0 ? Util.ALGORITHM_NAME_MI_SOFT_PORTRAIT_ENCRYPTED : Util.ALGORITHM_NAME_MI_SOFT_PORTRAIT;
        } else if (isPortraitMode()) {
            CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
            if (cameraCapabilities == null || !cameraCapabilities.isSupportMiDualBokeh()) {
                CameraCapabilities cameraCapabilities2 = this.mCameraCapabilities;
                str = (cameraCapabilities2 == null || !cameraCapabilities2.isSupportMegviiDualBokeh()) ? Util.ALGORITHM_NAME_DUAL_PORTRAIT : Util.ALGORITHM_NAME_MEGVII_DUAL_PORTRAIT;
            } else {
                str = Util.ALGORITHM_NAME_MI_DUAL_PORTRAIT;
            }
        } else {
            str = this.mMutexModePicker.getAlgorithmName();
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateAlgorithmName:");
        sb.append(str);
        Log.d(str2, sb.toString());
        this.mAlgorithmName = str;
    }

    private boolean updateAnchorFramePreview() {
        return supportAnchorFrameAsThumbnail();
    }

    /* access modifiers changed from: private */
    public void updateAsdSceneResult(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("update asd scene result,newResult:");
        sb.append(i);
        Log.e(str, sb.toString());
        exitAsdScene(this.mCurrentAsdScene);
        enterAsdScene(i);
        setCurrentAsdScene(i);
    }

    private void updateBeauty() {
        boolean z = this.mModuleIndex == 173 && C0122O00000o.instance().OOO0OOO();
        int i = this.mModuleIndex;
        if (i == 163 || i == 165 || i == 171 || z || i == 205) {
            if (this.mBeautyValues == null) {
                this.mBeautyValues = new BeautyValues();
            }
            CameraSettings.initBeautyValues(this.mBeautyValues, this.mModuleIndex);
            if (!DataRepository.dataItemConfig().getComponentConfigBeauty().isClosed(this.mModuleIndex) && this.mCurrentAiScene == 25 && !isFaceBeautyOn(this.mBeautyValues)) {
                ComponentRunningShine componentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
                if (componentRunningShine.supportBeautyLevel()) {
                    this.mBeautyValues.mBeautyLevel = BeautyConstant.LEVEL_LOW;
                } else {
                    componentRunningShine.supportSmoothLevel();
                }
                Log.d(TAG, String.format(Locale.ENGLISH, "Human scene mode detected, auto set beauty level from %s to %s", new Object[]{BeautyConstant.LEVEL_CLOSE, this.mBeautyValues.mBeautyLevel}));
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateBeauty(): ");
            sb.append(this.mBeautyValues);
            Log.d(str, sb.toString());
            this.mCamera2Device.setBeautyValues(this.mBeautyValues);
            this.mIsBeautyBodySlimOn = this.mBeautyValues.isBeautyBodyOn();
            updateFaceAgeAnalyze();
        }
    }

    private void updateBokeh() {
        ComponentConfigBokeh componentBokeh = DataRepository.dataItemConfig().getComponentBokeh();
        if (!ModuleManager.isPortraitModule()) {
            if (!"on".equals(componentBokeh.getComponentValue(this.mModuleIndex))) {
                this.mCamera2Device.setSingleBokeh(false);
                this.mCamera2Device.setDualBokehEnable(false);
                return;
            }
        }
        if (isSingleCamera() || (C0122O00000o.instance().OO0OOO() && ModuleManager.isPortraitModule() && isFrontCamera())) {
            this.mCamera2Device.setSingleBokeh(true);
            this.mCamera2Device.setDualBokehEnable(false);
        } else {
            this.mCamera2Device.setSingleBokeh(false);
            this.mCamera2Device.setDualBokehEnable(true);
        }
    }

    private void updateCaptureTriggerFlow() {
    }

    private void updateCinematicPhoto() {
        boolean isCinematicAspectRatioEnabled = CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex);
        this.mCamera2Device.setCinematicPhotoEnabled(isCinematicAspectRatioEnabled);
        if (isCinematicAspectRatioEnabled) {
            int i = this.mModuleIndex;
            if (i == 165 || i == 163 || i == 171) {
                EffectController.getInstance().setCinematicEnable(true);
                CircularMediaRecorder circularMediaRecorder = this.mCircularMediaRecorder;
                if (circularMediaRecorder != null) {
                    circularMediaRecorder.setCinematicEnable(true);
                }
            }
        }
    }

    private void updateColorEnhance() {
        if (C0122O00000o.instance().supportColorEnhance() && this.mCamera2Device != null) {
            this.mCamera2Device.setColorEnhanceEnable(DataRepository.dataItemRunning().getComponentRunningColorEnhance().isEnabled(getModuleIndex()));
        }
    }

    private void updateContrast() {
        this.mCamera2Device.setContrast(Integer.parseInt(getString(R.string.pref_camera_contrast_default)));
    }

    private void updateDecodePreview() {
        if (this.mIsGoogleLensAvailable || scanQRCodeEnabled() || C0124O00000oO.OOO0Oo() || CameraSettings.isDocumentMode2On(this.mModuleIndex)) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateDecodePreview: PreviewDecodeManager AlgorithmPreviewSize = ");
            sb.append(this.mCamera2Device.getAlgorithmPreviewSize());
            Log.d(str, sb.toString());
            this.mCamera2Device.startPreviewCallback(PreviewDecodeManager.getInstance().getPreviewCallback(), null);
            this.mMainProtocol.hideOrShowDocument(true);
            PreviewDecodeManager.getInstance().startDecode();
        }
        if (this.mSupportAnchorFrameAsThumbnail) {
            this.mCamera2Device.startPreviewCallback(null, this.mCacheImageDecoder.getAnchorPreviewCallback());
            this.mCacheImageDecoder.startDecode();
        }
    }

    private void updateDeviceOrientation() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005e, code lost:
        if (com.android.camera.data.DataRepository.dataItemGlobal().isOnSuperNightAlgoUpAndQuickShot() != false) goto L_0x0075;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0073, code lost:
        if (enablePreviewAsThumbnail() == false) goto L_0x0077;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateEnablePreviewThumbnail(boolean z) {
        if (!Util.isSaveToHidenFolder(this.mModuleIndex) && (this.mModuleIndex != 167 || (!DataRepository.dataItemRunning().isSwitchOn("pref_camera_peak_key") && !DataRepository.dataItemRunning().isSwitchOn("pref_camera_exposure_feedback")))) {
            int i = this.mModuleIndex;
            if ((!(i == 165 || i == 163) || !DataRepository.dataItemRunning().isSwitchOn("pref_camera_tilt_shift_mode")) && isPreviewThumbnailWhenFlash() && !this.mIsImageCaptureIntent) {
                if (this.mModuleIndex != 173) {
                    if (!this.mEnableParallelSession) {
                        if (!this.mEnableShot2Gallery) {
                            if (!z) {
                                if (this.mReceivedJpegCallbackNum == 0) {
                                }
                                this.mActivity.setPreviewThumbnail(this.mEnabledPreviewThumbnail);
                            }
                        }
                    }
                }
                this.mEnabledPreviewThumbnail = true;
                this.mActivity.setPreviewThumbnail(this.mEnabledPreviewThumbnail);
            }
        }
        this.mEnabledPreviewThumbnail = false;
        this.mActivity.setPreviewThumbnail(this.mEnabledPreviewThumbnail);
    }

    private void updateEvValue() {
        String manualValue = getManualValue(CameraSettings.KEY_QC_MANUAL_EXPOSURE_VALUE, "0");
        this.mEvValue = (int) (Float.parseFloat(manualValue) / this.mCameraCapabilities.getExposureCompensationStep());
        this.mEvState = 3;
        setEvValue();
    }

    private void updateExposureTime() {
        this.mCamera2Device.setExposureTime(Long.parseLong(getManualValue(CameraSettings.KEY_QC_EXPOSURETIME, getString(R.string.pref_camera_exposuretime_default))));
    }

    private void updateEyeLight() {
        if (isFrontCamera() && C0122O00000o.instance().OOo00oo()) {
            this.mCamera2Device.setEyeLight(Integer.parseInt(CameraSettings.getEyeLightType()));
        }
    }

    private void updateFNumber() {
        if (C0122O00000o.instance().isSupportBokehAdjust()) {
            this.mCamera2Device.setFNumber(CameraSettings.readFNumber());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0068  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateFace() {
        boolean z;
        boolean z2;
        MainContentProtocol mainContentProtocol;
        int i = this.mModuleIndex;
        if (i == 182 || i == 186 || this.mMultiSnapStatus) {
            z2 = false;
        } else if (CameraSettings.isMagicMirrorOn() || CameraSettings.isPortraitModeBackOn() || CameraSettings.isGroupShotOn() || CameraSettings.showGenderAge()) {
            z2 = true;
            z = true;
            mainContentProtocol = this.mMainProtocol;
            if (mainContentProtocol != null) {
                boolean z3 = !z2 || !z;
                mainContentProtocol.setSkipDrawFace(z3);
            }
            if (z2) {
                if (!this.mFaceDetectionEnabled) {
                    this.mFaceDetectionEnabled = true;
                    startFaceDetection();
                    return;
                }
                return;
            } else if (this.mFaceDetectionEnabled) {
                stopFaceDetection(true);
                this.mFaceDetectionEnabled = false;
                return;
            } else {
                return;
            }
        } else {
            z2 = DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_FACE_DETECTION, getResources().getBoolean(R.bool.pref_camera_facedetection_default));
            if (CameraSettings.isTiltShiftOn()) {
                z = false;
                mainContentProtocol = this.mMainProtocol;
                if (mainContentProtocol != null) {
                }
                if (z2) {
                }
            }
        }
        z = true;
        mainContentProtocol = this.mMainProtocol;
        if (mainContentProtocol != null) {
        }
        if (z2) {
        }
    }

    private void updateFaceAgeAnalyze() {
        boolean z;
        this.mIsGenderAgeOn = DataRepository.dataItemRunning().isSwitchOn("pref_camera_show_gender_age_key");
        if (this.mIsGenderAgeOn) {
            z = true;
        } else {
            BeautyValues beautyValues = this.mBeautyValues;
            z = beautyValues != null ? isFaceBeautyOn(beautyValues) : false;
        }
        this.mCamera2Device.setFaceAgeAnalyze(z);
    }

    private void updateFaceScore() {
        this.mIsMagicMirrorOn = DataRepository.dataItemRunning().isSwitchOn("pref_camera_magic_mirror_key");
        this.mCamera2Device.setFaceScore(this.mIsMagicMirrorOn);
    }

    private void updateFilter() {
        setEffectFilter(CameraSettings.getShaderEffect());
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

    /* JADX WARNING: Removed duplicated region for block: B:15:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateFocusMode() {
        String str;
        String str2;
        FocusManager2 focusManager2;
        String str3 = "manual";
        if (this.mIsMoonMode) {
            this.mFocusManager.removeMessages();
            if (this.mCamera2Device.getCapabilities().supportMoonAutoFocus()) {
                focusManager2 = this.mFocusManager;
                str2 = AutoFocus.LEGACY_CONTINUOUS_PICTURE;
            } else {
                str = this.mFocusManager.setFocusMode(str3);
                setFocusMode(str);
                if (CameraSettings.isFocusModeSwitching() && isBackCamera()) {
                    CameraSettings.setFocusModeSwitching(false);
                    this.mFocusManager.resetFocusStateIfNeeded();
                }
                if (!str.equals(str3)) {
                    float minimumFocusDistance = (this.mCameraCapabilities.getMinimumFocusDistance() * ((float) CameraSettings.getFocusPosition())) / 1000.0f;
                    if (!this.mCamera2Device.getCapabilities().supportMoonAutoFocus() && this.mIsMoonMode) {
                        minimumFocusDistance = 0.5f;
                    }
                    this.mCamera2Device.setFocusDistance(minimumFocusDistance);
                    return;
                }
                return;
            }
        } else {
            focusManager2 = this.mFocusManager;
            str2 = CameraSettings.getFocusMode();
        }
        str = focusManager2.setFocusMode(str2);
        setFocusMode(str);
        CameraSettings.setFocusModeSwitching(false);
        this.mFocusManager.resetFocusStateIfNeeded();
        if (!str.equals(str3)) {
        }
    }

    private void updateFpsRange() {
        Range[] supportedFpsRange = this.mCameraCapabilities.getSupportedFpsRange();
        Range range = supportedFpsRange[0];
        int length = supportedFpsRange.length;
        for (int i = 0; i < length; i++) {
            Range range2 = supportedFpsRange[i];
            if (((Integer) range.getUpper()).intValue() < ((Integer) range2.getUpper()).intValue() || (range.getUpper() == range2.getUpper() && ((Integer) range.getLower()).intValue() < ((Integer) range2.getLower()).intValue())) {
                range = range2;
            }
        }
        if (C0124O00000oO.O0Ooooo && CameraSettings.isPortraitModeBackOn()) {
            this.mCamera2Device.setFpsRange(new Range(Integer.valueOf(30), Integer.valueOf(30)));
        }
    }

    private void updateFrontMirror() {
        this.mCamera2Device.setFrontMirror(isFrontMirror());
    }

    private void updateHDR(String str) {
        if ("auto".equals(str)) {
            this.isDetectedInHdr = false;
        }
        int mutexHdrMode = getMutexHdrMode(str);
        stopObjectTracking(true);
        if (mutexHdrMode != 0) {
            this.mMutexModePicker.setMutexMode(mutexHdrMode);
        } else if (this.mMutexModePicker.isHdr()) {
            resetMutexModeManually();
            this.mIsNeedNightHDR = false;
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("resetMutexModeManually,mIsNeedNightHDR:");
            sb.append(this.mIsNeedNightHDR);
            Log.d(str2, sb.toString());
        }
        if (isFrontCamera() && isTriggerQcfaModeChange(false, true)) {
            this.mHandler.sendEmptyMessage(44);
        }
        if (str != null && !str.equals(this.mLastHdrMode)) {
            updateScene();
            this.mLastHdrMode = str;
        }
    }

    private void updateHighQualityPreferred() {
        this.mCamera2Device.applyHighQualityPreferred(CameraSettings.isHighQualityPreferred());
    }

    private void updateISO() {
        String string = getString(R.string.pref_camera_iso_default);
        String manualValue = getManualValue(CameraSettings.KEY_QC_ISO, string);
        if (manualValue == null || manualValue.equals(string)) {
            this.mCamera2Device.setISO(0);
        } else {
            this.mCamera2Device.setISO(Math.min(Util.parseInt(manualValue, 0), this.mCameraCapabilities.getMaxIso()));
        }
    }

    private void updateJpegQuality() {
        this.mCamera2Device.setJpegQuality(clampQuality(CameraSettings.getEncodingQuality(this.mMultiSnapStatus).toInteger(false)));
    }

    private void updateJpegThumbnailSize() {
        CameraSize jpegThumbnailSize = getJpegThumbnailSize();
        this.mCamera2Device.setJpegThumbnailSize(jpegThumbnailSize);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("thumbnailSize=");
        sb.append(jpegThumbnailSize);
        Log.d(str, sb.toString());
    }

    private void updateLiveShot() {
        if (C0122O00000o.instance().OOO0o0o() && this.mModuleIndex == 163) {
            if (CameraSettings.isLiveShotOn()) {
                startLiveShot();
            } else {
                stopLiveShot(false);
            }
        }
    }

    private Location updateLocation() {
        if (isTestImageCaptureWithoutLocation()) {
            return null;
        }
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        if (getModuleIndex() != 205 || CameraSettings.isRecordLocation()) {
            return currentLocation;
        }
        return null;
    }

    private void updateMacroMode() {
        this.mCamera2Device.setMacroMode(CameraSettings.isMacroModeEnabled(this.mModuleIndex));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0043, code lost:
        if (r8 == false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x007c, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOo0o0o() != false) goto L_0x007e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0090, code lost:
        if (java.lang.Long.parseLong(getManualValue(r5, getString(com.android.camera.R.string.pref_camera_exposuretime_default))) < 250000000) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ad, code lost:
        if (com.android.camera.data.DataRepository.dataItemRunning().getComponentUltraPixel().isRearSwitchOn() != false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00b9, code lost:
        if (isDualCamera() != false) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00d6, code lost:
        if (enableFrontMFNR() != false) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00e5, code lost:
        if (r7.mCameraCapabilities.isMfnrMacroZoomSupported() != false) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x010c, code lost:
        if (isZoomRatioBetweenUltraAndWide() != false) goto L_0x00af;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateMfnr(boolean z) {
        boolean z2 = true;
        boolean z3 = false;
        if (!isUseSwMfnr() && z) {
            boolean isUltraPixelOn = CameraSettings.isUltraPixelOn();
            String str = CameraSettings.KEY_QC_EXPOSURETIME;
            if (!isUltraPixelOn || !C0122O00000o.instance().OOOoooO()) {
                int i = this.mModuleIndex;
                if (i == 167) {
                    boolean z4 = C0122O00000o.instance().OOo0o() && (isSingleCamera() || isStandaloneMacroCamera() || isUltraWideBackCamera());
                    if (getRawCallbackType(false) == 0) {
                        if (!z4) {
                        }
                        if (this.mCamera2Device != null) {
                        }
                    }
                } else {
                    if (i == 175) {
                        if (C0122O00000o.instance().OOOOO00()) {
                        }
                    }
                    if (C0124O00000oO.O0OoooO) {
                    }
                    if (this.mMutexModePicker.isNormal()) {
                        if (!CameraSettings.isGroupShotOn()) {
                            if (isFrontCamera()) {
                            }
                            if (isStandaloneMacroCamera()) {
                            }
                            if (!C0122O00000o.instance().OOoOoO()) {
                                if (getZoomRatio() != 1.0f) {
                                    if (isBackCamera()) {
                                        if (!isUltraWideBackCamera()) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                z3 = z2;
            } else {
                boolean z5 = this.mModuleIndex == 167 && Long.parseLong(getManualValue(str, getString(R.string.pref_camera_exposuretime_default))) >= 250000000;
                if (!this.mUpscaleImageWithSR) {
                }
            }
            z2 = false;
            z3 = z2;
        }
        if (this.mCamera2Device != null) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setMfnr to ");
            sb.append(z3);
            Log.d(str2, sb.toString());
            this.mCamera2Device.setMfnr(z3);
        }
    }

    private void updateMute() {
    }

    private void updateNormalWideLDC() {
        this.mCamera2Device.setNormalWideLDC(shouldApplyNormalWideLDC());
    }

    private void updateOIS() {
        if (CameraSettings.isPortraitModeBackOn()) {
            this.mCamera2Device.setEnableOIS(false);
            return;
        }
        if (this.mModuleIndex == 167) {
            if (Long.parseLong(getManualValue(CameraSettings.KEY_QC_EXPOSURETIME, getString(R.string.pref_camera_exposuretime_default))) > ExtraTextUtils.GB) {
                this.mCamera2Device.setEnableOIS(false);
                return;
            }
        }
        if ((C0124O00000oO.O0o0O0 || C0124O00000oO.O0o0O || C0124O00000oO.O0oO000) && isDualCamera() && getZoomRatio() > 1.0f) {
            this.mCamera2Device.setEnableOIS(true);
        } else if (!isDualCamera() || this.mCameraCapabilities.isTeleOISSupported() || getZoomRatio() <= 1.0f) {
            this.mCamera2Device.setEnableOIS(true);
        } else {
            this.mCamera2Device.setEnableOIS(false);
        }
    }

    /* access modifiers changed from: private */
    public void updateOutputSize(@NonNull CameraSize cameraSize) {
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
        } else {
            this.mOutputPictureSize = cameraSize;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateOutputSize: ");
        sb.append(this.mOutputPictureSize);
        Log.d(str, sb.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:210:0x05ef  */
    /* JADX WARNING: Removed duplicated region for block: B:226:0x0670  */
    /* JADX WARNING: Removed duplicated region for block: B:255:0x070b  */
    /* JADX WARNING: Removed duplicated region for block: B:256:0x070d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updatePictureAndPreviewSize() {
        CameraSize cameraSize;
        List list;
        CameraSize bestPictureSize;
        CameraSize cameraSize2;
        CameraCapabilities cameraCapabilities;
        int i;
        boolean z;
        CameraSize cameraSize3;
        Object[] objArr;
        String str;
        Locale locale;
        String str2;
        CameraSize cameraSize4;
        int i2 = this.mEnableParallelSession ? 35 : 256;
        boolean OOo0oOO = C0122O00000o.instance().OOo0oOO();
        boolean z2 = OOo0oOO && C0122O00000o.instance().OOoO0O() && getModuleIndex() == 163;
        boolean isHeicPreferred = isHeicPreferred();
        this.mOutputPictureFormat = isHeicPreferred ? CompatibilityUtils.IMAGE_FORMAT_HEIC : 256;
        String str3 = TAG;
        Locale locale2 = Locale.ENGLISH;
        Object[] objArr2 = new Object[1];
        String str4 = "HEIC";
        String str5 = "JPEG";
        objArr2[0] = CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? str4 : str5;
        Log.d(str3, String.format(locale2, "updateSize: use %s as preferred output image format", objArr2));
        int[] sATSubCameraIds = this.mCamera2Device.getSATSubCameraIds();
        boolean z3 = sATSubCameraIds != null;
        if (z3) {
            String str6 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[SAT] camera list: ");
            sb.append(Arrays.toString(sATSubCameraIds));
            Log.d(str6, sb.toString());
            for (int i3 : sATSubCameraIds) {
                if (i3 == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                    if (this.mUltraCameraCapabilities == null) {
                        this.mUltraCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getUltraWideCameraId());
                    }
                    CameraCapabilities cameraCapabilities2 = this.mUltraCameraCapabilities;
                    if (cameraCapabilities2 != null) {
                        cameraCapabilities2.setOperatingMode(this.mOperatingMode);
                        this.mUltraWidePictureSize = getBestPictureSize(this.mUltraCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i2));
                        if (z2) {
                            this.mRawSizeOfUltraWide = getBestPictureSize(this.mUltraCameraCapabilities.getSupportedOutputSizeWithAssignedMode(32));
                            this.mCamera2Device.setRawSizeOfUltraWide(this.mRawSizeOfUltraWide);
                        }
                    }
                } else if (i3 == Camera2DataContainer.getInstance().getMainBackCameraId()) {
                    if (this.mWideCameraCapabilities == null) {
                        this.mWideCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getMainBackCameraId());
                    }
                    CameraCapabilities cameraCapabilities3 = this.mWideCameraCapabilities;
                    if (cameraCapabilities3 != null) {
                        cameraCapabilities3.setOperatingMode(this.mOperatingMode);
                        List supportedOutputSizeWithAssignedMode = this.mWideCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i2);
                        int OO00oOo = C0122O00000o.instance().OO00oOo();
                        if (OO00oOo != 0) {
                            PictureSizeManager.initializeLimitWidth(supportedOutputSizeWithAssignedMode, OO00oOo, this.mModuleIndex, this.mBogusCameraId);
                            cameraSize4 = PictureSizeManager.getBestPictureSize(this.mModuleIndex);
                        } else {
                            cameraSize4 = getBestPictureSize(supportedOutputSizeWithAssignedMode);
                        }
                        this.mWidePictureSize = cameraSize4;
                        if (z2) {
                            supportedOutputSizeWithAssignedMode = this.mWideCameraCapabilities.getSupportedOutputSizeWithAssignedMode(32);
                            this.mRawSizeOfWide = getBestPictureSize(supportedOutputSizeWithAssignedMode);
                            this.mCamera2Device.setRawSizeOfWide(this.mRawSizeOfWide);
                        }
                        if (C0122O00000o.instance().OOoO0oO()) {
                            this.mSATRemosicPictureSize = getBestPictureSize(supportedOutputSizeWithAssignedMode);
                        }
                        String str7 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("wide picture size ");
                        sb2.append(this.mWidePictureSize);
                        sb2.append(" remosic is ");
                        sb2.append(this.mSATRemosicPictureSize);
                        Log.d(str7, sb2.toString());
                    }
                } else if (i3 == Camera2DataContainer.getInstance().getAuxCameraId()) {
                    if (this.mTeleCameraCapabilities == null) {
                        this.mTeleCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getAuxCameraId());
                    }
                    CameraCapabilities cameraCapabilities4 = this.mTeleCameraCapabilities;
                    if (cameraCapabilities4 != null) {
                        cameraCapabilities4.setOperatingMode(this.mOperatingMode);
                        this.mTelePictureSize = getBestPictureSize(this.mTeleCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i2));
                        if (z2) {
                            this.mRawSizeOfTele = getBestPictureSize(this.mTeleCameraCapabilities.getSupportedOutputSizeWithAssignedMode(32));
                            this.mCamera2Device.setRawSizeOfTele(this.mRawSizeOfTele);
                        }
                    }
                } else if (i3 == Camera2DataContainer.getInstance().getUltraTeleCameraId()) {
                    if (this.mUltraTeleCameraCapabilities == null) {
                        this.mUltraTeleCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getUltraTeleCameraId());
                    }
                    CameraCapabilities cameraCapabilities5 = this.mUltraTeleCameraCapabilities;
                    if (cameraCapabilities5 != null) {
                        cameraCapabilities5.setOperatingMode(this.mOperatingMode);
                        this.mUltraTelePictureSize = getBestPictureSize(this.mUltraTeleCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i2));
                        if (z2) {
                            this.mRawSizeOfUltraTele = getBestPictureSize(this.mUltraTeleCameraCapabilities.getSupportedOutputSizeWithAssignedMode(32));
                            this.mCamera2Device.setRawSizeOfUltraTele(this.mRawSizeOfUltraTele);
                        }
                    }
                    this.mCamera2Device.setUltraTelePictureSize(this.mUltraTelePictureSize);
                } else if (i3 == Camera2DataContainer.getInstance().getStandaloneMacroCameraId()) {
                    if (this.mMacroCameraCapabilities == null) {
                        this.mMacroCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getStandaloneMacroCameraId());
                    }
                    CameraCapabilities cameraCapabilities6 = this.mMacroCameraCapabilities;
                    if (cameraCapabilities6 != null) {
                        cameraCapabilities6.setOperatingMode(this.mOperatingMode);
                        this.mMacroPictureSize = getBestPictureSize(this.mMacroCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i2));
                        if (z2) {
                            this.mRawSizeOfMacro = getBestPictureSize(this.mMacroCameraCapabilities.getSupportedOutputSizeWithAssignedMode(32));
                            this.mCamera2Device.setRawSizeOfMacro(this.mRawSizeOfMacro);
                        }
                    }
                    this.mCamera2Device.setMarcroPictureSize(this.mMacroPictureSize);
                }
            }
            if (this.mFakeTeleCameraCapabilities == null) {
                this.mFakeTeleCameraCapabilities = Camera2DataContainer.getInstance().getCapabilities(Camera2DataContainer.getInstance().getSATCameraId());
            }
            CameraCapabilities cameraCapabilities7 = this.mFakeTeleCameraCapabilities;
            if (cameraCapabilities7 != null && cameraCapabilities7.supportFakeSat()) {
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
            this.mCamera2Device.setSATRemosicPictureSize(this.mSATRemosicPictureSize);
            this.mCamera2Device.setTelePictureSize(this.mTelePictureSize);
            this.mPictureSize = getSatPictureSize();
            if (C0124O00000oO.O0o0OO0 || C0124O00000oO.O0oo0o) {
                Camera2Proxy camera2Proxy = this.mCamera2Device;
                if (camera2Proxy != null) {
                    List supportedOutputSizeWithAssignedMode2 = camera2Proxy.getSatCapabilities().getSupportedOutputSizeWithAssignedMode(this.mOutputPictureFormat);
                    if (supportedOutputSizeWithAssignedMode2 != null && !supportedOutputSizeWithAssignedMode2.contains(this.mPictureSize)) {
                        this.mPictureSize = PictureSizeManager.getBestPictureSize(supportedOutputSizeWithAssignedMode2, this.mModuleIndex);
                        String str8 = TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Could not match a proper Jpeg size in supportedPictureSizes, update by SAT size: ");
                        sb3.append(this.mPictureSize);
                        Log.d(str8, sb3.toString());
                    }
                }
            }
        } else {
            if (!isUltraTeleCamera() || this.mModuleIndex != 167) {
                z = false;
                i = 0;
            } else {
                i = C0122O00000o.instance().OO00Ooo();
                z = false;
            }
            if (getRawCallbackType(z) > 0) {
                List supportedOutputSizeWithAssignedMode3 = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(32);
                if (this.mModuleIndex != 167) {
                    cameraSize3 = getBestPictureSize(supportedOutputSizeWithAssignedMode3);
                } else if (supportedOutputSizeWithAssignedMode3 == null || supportedOutputSizeWithAssignedMode3.size() == 0) {
                    Log.w(TAG, "The supported raw size list return from hal is null!");
                    String str9 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("The best sensor raw image size: ");
                    sb4.append(this.mSensorRawImageSize);
                    Log.d(str9, sb4.toString());
                } else if (i == 0) {
                    cameraSize3 = getBestPictureSize(supportedOutputSizeWithAssignedMode3, 1.3333333f);
                } else {
                    PictureSizeManager.initializeLimitWidth(supportedOutputSizeWithAssignedMode3, i, this.mModuleIndex, this.mBogusCameraId);
                    cameraSize3 = PictureSizeManager.getBestPictureSize(1.3333333f);
                }
                this.mSensorRawImageSize = cameraSize3;
                String str92 = TAG;
                StringBuilder sb42 = new StringBuilder();
                sb42.append("The best sensor raw image size: ");
                sb42.append(this.mSensorRawImageSize);
                Log.d(str92, sb42.toString());
            } else if (z2) {
                CameraSize bestPictureSize2 = getBestPictureSize(this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(32));
                this.mCamera2Device.setSensorRawImageSize(bestPictureSize2, getRawCallbackType(false));
                String str10 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("updateSize: raw buffer size: ");
                sb5.append(bestPictureSize2);
                Log.d(str10, sb5.toString());
            }
            if (!this.mEnableParallelSession || !isPortraitMode()) {
                List supportedOutputSizeWithAssignedMode4 = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i2);
                CameraSize bestPictureSize3 = getBestPictureSize(supportedOutputSizeWithAssignedMode4);
                if (C0124O00000oO.O0o00O0 && getOperatingMode() == 36867) {
                    bestPictureSize3 = new CameraSize(bestPictureSize3.width / 2, bestPictureSize3.height / 2);
                }
                if (C0122O00000o.instance().OOO0O() && isFrontCamera() && (!C0124O00000oO.isMTKPlatform() || !isHeicPreferred())) {
                    bestPictureSize3 = new CameraSize(bestPictureSize3.width / 2, bestPictureSize3.height / 2);
                }
                if (isParallelSessionEnable() && !this.mCameraCapabilities.isSupportedQcfa() && CameraSettings.isUltraPixelOn()) {
                    bestPictureSize3 = new CameraSize(bestPictureSize3.width / 2, bestPictureSize3.height / 2);
                }
                if (i != 0) {
                    PictureSizeManager.initializeLimitWidth(supportedOutputSizeWithAssignedMode4, i, this.mModuleIndex, this.mBogusCameraId);
                    bestPictureSize3 = PictureSizeManager.getBestPictureSize(this.mModuleIndex);
                }
                if (isLimitSize()) {
                    bestPictureSize3 = getLimitSize(supportedOutputSizeWithAssignedMode4);
                }
                if (this.mModuleIndex == 173) {
                    int OO00oOo2 = C0122O00000o.instance().OO00oOo();
                    if (this.mCameraCapabilities.getSuperNightLimitSize() != null) {
                        bestPictureSize3 = PictureSizeManager.getBestPictureSize(this.mCameraCapabilities.getSuperNightLimitSize(), this.mModuleIndex);
                    } else if (OO00oOo2 != 0) {
                        PictureSizeManager.initializeLimitWidth(supportedOutputSizeWithAssignedMode4, OO00oOo2, this.mModuleIndex, this.mBogusCameraId);
                        bestPictureSize3 = PictureSizeManager.getBestPictureSize(this.mModuleIndex);
                    }
                }
                this.mPictureSize = bestPictureSize3;
                if (isEnableQcfaForAlgoUp()) {
                    CameraSize cameraSize5 = this.mPictureSize;
                    int i4 = cameraSize5.width / 2;
                    int i5 = cameraSize5.height / 2;
                    this.mBinningPictureSize = PictureSizeManager.getBestPictureSize(this.mCameraCapabilities.getSupportedOutputStreamSizes(35), Util.getRatio(CameraSettings.getPictureSizeRatioString(this.mModuleIndex)), i4 * i5);
                    if (this.mBinningPictureSize.isEmpty()) {
                        this.mBinningPictureSize = new CameraSize(i4, i5);
                    }
                }
            } else {
                updatePortraitPictureSize(i2);
            }
        }
        if (OOo0oOO) {
            CameraSize tuningBufferSize = this.mCameraCapabilities.getTuningBufferSize(1);
            this.mCamera2Device.setTuningBufferSize(tuningBufferSize);
            String str11 = TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("updateSize: yuv tuning buffer size: ");
            sb6.append(tuningBufferSize);
            Log.d(str11, sb6.toString());
            if (z2 || getRawCallbackType(false) != 0) {
                CameraSize tuningBufferSize2 = this.mCameraCapabilities.getTuningBufferSize(2);
                this.mCamera2Device.setRawSizeOfTuningBuffer(tuningBufferSize2);
                String str12 = TAG;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("updateSize: raw tuning buffer size: ");
                sb7.append(tuningBufferSize2);
                Log.d(str12, sb7.toString());
            }
        }
        List supportedOutputSizeWithAssignedMode5 = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class);
        CameraSize cameraSize6 = this.mPictureSize;
        double previewAspectRatio = (double) CameraSettings.getPreviewAspectRatio(cameraSize6.width, cameraSize6.height);
        this.mPreviewSize = Util.getOptimalPreviewSize(false, this.mBogusCameraId, supportedOutputSizeWithAssignedMode5, previewAspectRatio);
        this.mCamera2Device.setPreviewSize(this.mPreviewSize);
        if (this.mIsGoogleLensAvailable) {
            this.mCamera2Device.setAlgorithmPreviewSize(Util.getAlgorithmPreviewSize(supportedOutputSizeWithAssignedMode5, previewAspectRatio, this.mPreviewSize));
        } else {
            this.mCamera2Device.setAlgorithmPreviewSize(this.mPreviewSize);
        }
        int i6 = 35;
        this.mCamera2Device.setAlgorithmPreviewFormat(35);
        if (this.mEnableParallelSession) {
            if (!CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat)) {
                cameraCapabilities = this.mCameraCapabilities;
                i6 = 256;
            } else if (C0124O00000oO.isMTKPlatform() && isHeicPreferred) {
                cameraCapabilities = this.mCameraCapabilities;
            } else if (this.mCameraCapabilities.hasStandaloneHeicStreamConfigurations()) {
                list = this.mCameraCapabilities.getSupportedHeicOutputStreamSizes();
                if (!CameraSettings.isUltraPixelOn() && CameraSettings.isSRTo108mModeOn()) {
                    bestPictureSize = this.SIZE_108M;
                } else if (this.mModuleIndex != 165) {
                    CameraSize cameraSize7 = this.mPictureSize;
                    int min = Math.min(cameraSize7.width, cameraSize7.height);
                    boolean z4 = C0124O00000oO.isMTKPlatform() && isFrontCamera();
                    this.mOutputPictureSize = PictureSizeManager.getBestSquareSize(list, min, z4);
                    String str13 = "x";
                    if (this.mOutputPictureSize.isEmpty()) {
                        String str14 = TAG;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("Could not find a proper squared Jpeg size, defaults to: ");
                        sb8.append(min);
                        sb8.append(str13);
                        sb8.append(min);
                        Log.w(str14, sb8.toString());
                        cameraSize2 = new CameraSize(min, min);
                    } else {
                        if (C0124O00000oO.isMTKPlatform() && isHeicPreferred && this.mOutputPictureSize.getHeight() > min) {
                            String str15 = TAG;
                            StringBuilder sb9 = new StringBuilder();
                            sb9.append("force reset HEIF output size to: ");
                            sb9.append(min);
                            sb9.append(str13);
                            sb9.append(min);
                            Log.w(str15, sb9.toString());
                            cameraSize2 = new CameraSize(min, min);
                        }
                        if ((C0124O00000oO.O0o0OO0 || C0124O00000oO.O0oo0o) && !this.mPictureSize.equals(this.mOutputPictureSize) && list != null && !list.contains(this.mPictureSize) && this.mModuleIndex != 165) {
                            this.mPictureSize = this.mOutputPictureSize;
                            String str16 = TAG;
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append("Could not match a proper Jpeg size with YUV size, update to Jpeg size: ");
                            sb10.append(this.mPictureSize);
                            Log.d(str16, sb10.toString());
                        }
                        String str17 = TAG;
                        Locale locale3 = Locale.ENGLISH;
                        Object[] objArr3 = new Object[2];
                        objArr3[0] = CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? str4 : str5;
                        objArr3[1] = this.mOutputPictureSize;
                        Log.d(str17, String.format(locale3, "updateSize: algoUp output size (%s): %s", objArr3));
                    }
                    this.mOutputPictureSize = cameraSize2;
                    this.mPictureSize = this.mOutputPictureSize;
                    String str162 = TAG;
                    StringBuilder sb102 = new StringBuilder();
                    sb102.append("Could not match a proper Jpeg size with YUV size, update to Jpeg size: ");
                    sb102.append(this.mPictureSize);
                    Log.d(str162, sb102.toString());
                    String str172 = TAG;
                    Locale locale32 = Locale.ENGLISH;
                    Object[] objArr32 = new Object[2];
                    objArr32[0] = CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? str4 : str5;
                    objArr32[1] = this.mOutputPictureSize;
                    Log.d(str172, String.format(locale32, "updateSize: algoUp output size (%s): %s", objArr32));
                } else if (z3) {
                    bestPictureSize = this.mPictureSize;
                } else {
                    if (isPortraitMode() && this.mCameraCapabilities.isSupportOptimalBokehSize() && isBackCamera()) {
                        Size optimalPictureBokehSize = this.mCameraCapabilities.getOptimalPictureBokehSize(CameraSettings.getPictureSizeRatioString(this.mModuleIndex));
                        if (optimalPictureBokehSize != null) {
                            bestPictureSize = PictureSizeManager.getBestPictureSizeLimitWidth(list, Util.getRatio(CameraSettings.getPictureSizeRatioString(this.mModuleIndex)), optimalPictureBokehSize.getWidth());
                        }
                    } else if (isLimitSize()) {
                        bestPictureSize = getLimitSize(list);
                    }
                    bestPictureSize = PictureSizeManager.getBestPictureSize(list, this.mModuleIndex);
                }
                this.mOutputPictureSize = bestPictureSize;
                this.mPictureSize = this.mOutputPictureSize;
                String str1622 = TAG;
                StringBuilder sb1022 = new StringBuilder();
                sb1022.append("Could not match a proper Jpeg size with YUV size, update to Jpeg size: ");
                sb1022.append(this.mPictureSize);
                Log.d(str1622, sb1022.toString());
                String str1722 = TAG;
                Locale locale322 = Locale.ENGLISH;
                Object[] objArr322 = new Object[2];
                objArr322[0] = CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? str4 : str5;
                objArr322[1] = this.mOutputPictureSize;
                Log.d(str1722, String.format(locale322, "updateSize: algoUp output size (%s): %s", objArr322));
            } else {
                cameraCapabilities = this.mCameraCapabilities;
                i6 = this.mOutputPictureFormat;
            }
            list = cameraCapabilities.getSupportedOutputSizeWithAssignedMode(i6);
            if (!CameraSettings.isUltraPixelOn()) {
            }
            if (this.mModuleIndex != 165) {
            }
        }
        if (this.mIsImageCaptureIntent && this.mCameraCapabilities.isSupportLightTripartite() && this.mPictureSize.width > 4100) {
            CameraSize cameraSize8 = null;
            try {
                PictureSizeManager.initializeLimitWidth(this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i2), 4100, this.mModuleIndex, this.mBogusCameraId);
                cameraSize8 = PictureSizeManager.getBestPictureSize(this.mModuleIndex);
            } catch (Exception unused) {
                Log.e(TAG, "No find tempSize for tripartite used");
            }
            if (cameraSize8 != null && cameraSize8.width >= 3000) {
                if (this.mEnableParallelSession) {
                    List supportedOutputSizeWithAssignedMode6 = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(256);
                    if (this.mModuleIndex == 165) {
                        int min2 = Math.min(cameraSize8.width, cameraSize8.height);
                        cameraSize = new CameraSize(min2, min2);
                    } else {
                        cameraSize = cameraSize8;
                    }
                    if (supportedOutputSizeWithAssignedMode6.contains(cameraSize)) {
                        this.mPictureSize = cameraSize8;
                        this.mOutputPictureSize = cameraSize;
                        Log.d(TAG, String.format(Locale.ENGLISH, "updateSize: algoUp picture size for tripartite (%s): %s", new Object[]{str5, this.mOutputPictureSize}));
                    }
                } else {
                    this.mPictureSize = cameraSize8;
                }
            }
        }
        String str18 = TAG;
        Locale locale4 = Locale.ENGLISH;
        Object[] objArr4 = new Object[4];
        if (this.mEnableParallelSession) {
            str4 = "YUV";
        } else if (!CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat)) {
            str4 = str5;
        }
        objArr4[0] = str4;
        objArr4[1] = this.mPictureSize;
        objArr4[2] = this.mPreviewSize;
        objArr4[3] = this.mSensorRawImageSize;
        Log.d(str18, String.format(locale4, "updateSize: picture size (%s): %s, preview size: %s, sensor raw image size: %s", objArr4));
        CameraSize cameraSize9 = this.mPreviewSize;
        updateCameraScreenNailSize(cameraSize9.width, cameraSize9.height);
        checkDisplayOrientation();
        CameraSize cameraSize10 = this.mPreviewSize;
        setVideoSize(cameraSize10.width, cameraSize10.height);
    }

    private void updatePortraitLighting() {
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities != null) {
            FilterFactory.setLightingVersion(cameraCapabilities.getPortraitLightingVersion());
        }
        String valueOf = String.valueOf(CameraSettings.getPortraitLightingPattern());
        this.mIsPortraitLightingOn = !valueOf.equals("0");
        this.mCamera2Device.setPortraitLighting(Integer.parseInt(valueOf));
        setLightingEffect();
    }

    private boolean updatePortraitOptimalSize(int i) {
        String str;
        String str2;
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities != null) {
            int optimalMasterBokehId = cameraCapabilities.getOptimalMasterBokehId();
            int optimalSlaveBokehId = this.mCameraCapabilities.getOptimalSlaveBokehId();
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updatePortraitOptimalSize: masterId ");
            sb.append(optimalMasterBokehId);
            sb.append(" slaveId ");
            sb.append(optimalSlaveBokehId);
            Log.d(str3, sb.toString());
            if (optimalMasterBokehId <= -1 || optimalSlaveBokehId <= -1) {
                str = TAG;
                str2 = "updatePortraitOptimalSize error get master or slave size";
            } else {
                String pictureSizeRatioString = CameraSettings.getPictureSizeRatioString(this.mModuleIndex);
                Size optimalMasterBokehSize = this.mCameraCapabilities.getOptimalMasterBokehSize(pictureSizeRatioString);
                Size optimalSlaveBokehSize = this.mCameraCapabilities.getOptimalSlaveBokehSize(pictureSizeRatioString);
                CameraCapabilities capabilities = Camera2DataContainer.getInstance().getCapabilities(optimalMasterBokehId);
                CameraCapabilities capabilities2 = Camera2DataContainer.getInstance().getCapabilities(optimalSlaveBokehId);
                if (capabilities == null || capabilities2 == null) {
                    str = TAG;
                    str2 = "updatePortraitOptimalSize: could not get master or slave capabilities ";
                } else {
                    List supportedOutputSizeWithAssignedMode = capabilities.getSupportedOutputSizeWithAssignedMode(i, this.mOperatingMode);
                    List supportedOutputSizeWithAssignedMode2 = capabilities2.getSupportedOutputSizeWithAssignedMode(i, this.mOperatingMode);
                    this.mPictureSize = PictureSizeManager.getBestPictureSizeLimitWidth(supportedOutputSizeWithAssignedMode, Util.getRatio(pictureSizeRatioString), optimalMasterBokehSize.getWidth());
                    this.mSubPictureSize = PictureSizeManager.getBestPictureSizeLimitWidth(supportedOutputSizeWithAssignedMode2, Util.getRatio(pictureSizeRatioString), optimalSlaveBokehSize.getWidth());
                    String str4 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("updatePortraitOptimalSize: pictureSize is ");
                    sb2.append(this.mPictureSize);
                    sb2.append(" subPictureSize is ");
                    sb2.append(this.mSubPictureSize);
                    Log.d(str4, sb2.toString());
                    return true;
                }
            }
        } else {
            str = TAG;
            str2 = "updatePortraitOptimalSize: could not get logical capabilities";
        }
        Log.e(str, str2);
        return false;
    }

    private void updatePortraitPictureSize(int i) {
        int i2;
        boolean z;
        boolean z2;
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities == null || !cameraCapabilities.isSupportOptimalBokehSize() || !updatePortraitOptimalSize(i)) {
            if (!isFrontCamera()) {
                z2 = C0122O00000o.instance().OO0O00o();
                z = DataRepository.dataItemRunning().isSwitchOn("pref_ultra_wide_bokeh_enabled");
                i2 = z ? Camera2DataContainer.getInstance().getUltraWideCameraId() : C0124O00000oO.Oo00() ? this.mCamera2Device.getBokehAuxCameraId() : Camera2DataContainer.getInstance().getAuxCameraId();
            } else if (isDualFrontCamera()) {
                i2 = Camera2DataContainer.getInstance().getAuxFrontCameraId();
                z2 = true;
                z = false;
            } else {
                i2 = -1;
                z2 = false;
                z = false;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("BS = ");
            sb.append(z2);
            sb.append(" UW = ");
            sb.append(z);
            sb.append(" id = ");
            sb.append(i2);
            Log.d(str, sb.toString());
            this.mBokehDepthSize = this.mCameraCapabilities.getBokeBufferSize();
            PictureSizeManager.initializeLimitWidth(this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(i), isBackCamera() ? C0122O00000o.instance().O0ooOo() : 0, this.mModuleIndex, this.mBogusCameraId);
            CameraSize bestPictureSize = PictureSizeManager.getBestPictureSize(this.mModuleIndex);
            if (C0124O00000oO.O0o00O0 && getOperatingMode() == 36867) {
                bestPictureSize = new CameraSize(bestPictureSize.width / 2, bestPictureSize.height / 2);
            }
            if (C0122O00000o.instance().OOO0O() && isFrontCamera()) {
                bestPictureSize = new CameraSize(bestPictureSize.width / 2, bestPictureSize.height / 2);
            }
            CameraSize cameraSize = null;
            if (-1 == i2) {
                this.mPictureSize = bestPictureSize;
                this.mSubPictureSize = null;
            } else {
                if (z2) {
                    cameraSize = bestPictureSize;
                }
                if (isBackCamera() && C0122O00000o.instance().O0oooo() != 0) {
                    int O0oooo = C0122O00000o.instance().O0oooo();
                    CameraSize cameraSize2 = new CameraSize(O0oooo, (int) Math.ceil((double) (((float) O0oooo) / Util.getRatio(CameraSettings.getPictureSizeRatioString(this.mModuleIndex)))));
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("maxPhysicSize ");
                    sb2.append(cameraSize2);
                    Log.d(str2, sb2.toString());
                    cameraSize = cameraSize2;
                }
                CameraSize pictureSize = getPictureSize(i2, i, cameraSize);
                if (z || z2 || i2 == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                    this.mPictureSize = bestPictureSize;
                    this.mSubPictureSize = pictureSize;
                } else {
                    this.mPictureSize = pictureSize;
                    this.mSubPictureSize = bestPictureSize;
                }
            }
            Log.d(TAG, String.format(Locale.ENGLISH, "mainSize = %s subSize = %s", new Object[]{this.mPictureSize, this.mSubPictureSize}));
        }
    }

    private void updateRawCapture() {
        if (this.mModuleIndex == 167 && DataRepository.dataItemConfig().getComponentConfigRaw().isSwitchOn(this.mModuleIndex)) {
            this.mWaitSaveFinish = true;
            this.mHandler.sendEmptyMessageDelayed(61, FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME);
        }
    }

    private void updateSRAndMFNR() {
        if (!this.mMutexModePicker.isHdr()) {
            boolean z = (C0124O00000oO.O0o0O00 || C0124O00000oO.O0o0O0O) && isIn3OrMoreSatMode() && getZoomRatio() > 1.0f && this.mCamera2Device.getSatMasterCameraId() == 2;
            if (z) {
                this.mMutexModePicker.resetMutexMode();
            } else {
                updateSuperResolution();
            }
            if (!z && C0122O00000o.instance().OOOoooO()) {
                updateMfnr(CameraSettings.isMfnrSatEnable());
            }
        }
    }

    private void updateSaturation() {
        this.mCamera2Device.setSaturation(Integer.parseInt(getString(R.string.pref_camera_saturation_default)));
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x005e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateScene() {
        FocusManager2 focusManager2;
        String str;
        String componentValue;
        if (C0124O00000oO.Oo0o0o()) {
            DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
            String str2 = "-1";
            if (this.mMutexModePicker.isSceneHdr()) {
                componentValue = CameraScene.HDR;
            } else if (!dataItemRunning.isSwitchOn("pref_camera_scenemode_setting_key")) {
                this.mSceneMode = str2;
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("sceneMode=");
                sb.append(this.mSceneMode);
                sb.append(" mutexMode=");
                sb.append(this.mMutexModePicker.getMutexMode());
                Log.d(str3, sb.toString());
                if (!setSceneMode(this.mSceneMode)) {
                    this.mSceneMode = str2;
                }
                this.mHandler.post(new Runnable() {
                    public void run() {
                        Camera2Module.this.updateSceneModeUI();
                    }
                });
                if (!"0".equals(this.mSceneMode) || str2.equals(this.mSceneMode)) {
                    focusManager2 = this.mFocusManager;
                    str = null;
                } else {
                    focusManager2 = this.mFocusManager;
                    str = AutoFocus.LEGACY_CONTINUOUS_PICTURE;
                }
                focusManager2.overrideFocusMode(str);
            } else {
                componentValue = dataItemRunning.getComponentRunningSceneValue().getComponentValue(this.mModuleIndex);
            }
            this.mSceneMode = componentValue;
            String str32 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("sceneMode=");
            sb2.append(this.mSceneMode);
            sb2.append(" mutexMode=");
            sb2.append(this.mMutexModePicker.getMutexMode());
            Log.d(str32, sb2.toString());
            if (!setSceneMode(this.mSceneMode)) {
            }
            this.mHandler.post(new Runnable() {
                public void run() {
                    Camera2Module.this.updateSceneModeUI();
                }
            });
            if (!"0".equals(this.mSceneMode)) {
            }
            focusManager2 = this.mFocusManager;
            str = null;
            focusManager2.overrideFocusMode(str);
        }
    }

    /* access modifiers changed from: private */
    public void updateSceneModeUI() {
        if (DataRepository.dataItemRunning().isSwitchOn("pref_camera_scenemode_setting_key")) {
            DataRepository.dataItemConfig().getComponentHdr().setComponentValue(163, "off");
            String flashModeByScene = CameraSettings.getFlashModeByScene(this.mSceneMode);
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null) {
                topAlert.disableMenuItem(false, 194);
                if (flashModeByScene != null) {
                    this.mTopAlert.disableMenuItem(false, 193);
                } else {
                    this.mTopAlert.enableMenuItem(false, 193);
                }
                this.mTopAlert.hideExtraMenu();
            }
        } else {
            TopAlert topAlert2 = this.mTopAlert;
            if (topAlert2 != null) {
                topAlert2.enableMenuItem(false, 193, 194);
            }
        }
        TopAlert topAlert3 = this.mTopAlert;
        if (topAlert3 != null) {
            topAlert3.updateConfigItem(193, 194);
            if ("3".equals(DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex)) && this.mCurrentAsdScene == 0) {
                setCurrentAsdScene(-1);
            }
        }
        updatePreferenceInWorkThread(11, 10);
    }

    private void updateSessionParams() {
    }

    private void updateSharpness() {
        this.mCamera2Device.setSharpness(Integer.parseInt(getString(R.string.pref_camera_sharpness_default)));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:105:0x01a9, code lost:
        if (r10.mModuleIndex == 167) goto L_0x01c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x01af, code lost:
        if (com.android.camera.CameraSettings.isUltraPixelRawOn() != false) goto L_0x01c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x01bf, code lost:
        if (shouldDoMultiFrameCapture() != false) goto L_0x0155;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateShotDetermine() {
        int i;
        int i2 = 1;
        boolean z = this.mModuleIndex == 171 && (!isBackCamera() ? C0122O00000o.instance().OOoO0() || C0122O00000o.instance().OOo0O00() : C0124O00000oO.Oo00O00() || C0122O00000o.instance().OOoO00o());
        if (ParallelSnapshotManager.isParallelTagOpen && !this.mIsParallelParameterSet) {
            Map OO000OO = C0122O00000o.instance().OO000OO();
            this.mParallelPerformance = ((Integer) OO000OO.get(Integer.valueOf(0))).intValue();
            this.mParallelQuality = ((Integer) OO000OO.get(Integer.valueOf(1))).intValue();
            int intValue = ((Integer) OO000OO.get(Integer.valueOf(2))).intValue();
            ParallelSnapshotManager.getInstance().setMaxQueueSize(intValue);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mParallelPerformance:");
            sb.append(this.mParallelPerformance);
            sb.append(" mParallelQuality:");
            sb.append(this.mParallelQuality);
            sb.append(" maxQueueSize:");
            sb.append(intValue);
            Log.d(str, sb.toString());
            this.mCamera2Device.setParallelSettings(this.mParallelPerformance, this.mParallelQuality);
            this.mIsParallelParameterSet = true;
        }
        this.mEnableParallelSession = isParallelSessionEnable();
        if (!this.mIsImageCaptureIntent) {
            boolean z2 = !this.mEnableParallelSession && C0122O00000o.instance().OOoO() && enablePreviewAsThumbnail() && !CameraSettings.isLiveShotOn();
            this.mEnableShot2Gallery = z2;
            int moduleIndex = getModuleIndex();
            if (!(moduleIndex == 163 || moduleIndex == 165 || moduleIndex == 167)) {
                if (moduleIndex == 171) {
                    if (!this.mEnableParallelSession) {
                        if (z) {
                            i2 = 2;
                        }
                        i2 = 0;
                    } else if (!isBackCamera() || !C0122O00000o.instance().OOo0o00()) {
                        if (!shouldDoMultiFrameCapture()) {
                            if (isDualFrontCamera() || isDualCamera() || isBokehUltraWideBackCamera()) {
                                if (z) {
                                    i = 6;
                                }
                                i2 = 5;
                            } else {
                                if (z) {
                                    i = 7;
                                }
                                i2 = 5;
                            }
                        }
                        i2 = 8;
                    } else {
                        i2 = 11;
                    }
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("enableParallel=");
                    sb2.append(this.mEnableParallelSession);
                    sb2.append(" enableShot2Gallery=");
                    sb2.append(this.mEnableShot2Gallery);
                    sb2.append(" shotType=");
                    sb2.append(i2);
                    Log.d(str2, sb2.toString());
                    this.mCamera2Device.setShotType(i2);
                    this.mCamera2Device.setShot2Gallery(this.mEnableShot2Gallery);
                } else if (moduleIndex == 173) {
                    if (!DataRepository.dataItemGlobal().isOnSuperNightHalfAlgoUp()) {
                        if (!DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode() && getRawCallbackType(false) != 8) {
                            if (isCurrentRawDomainBasedSuperNight()) {
                                i = 10;
                            }
                            i2 = 0;
                            String str22 = TAG;
                            StringBuilder sb22 = new StringBuilder();
                            sb22.append("enableParallel=");
                            sb22.append(this.mEnableParallelSession);
                            sb22.append(" enableShot2Gallery=");
                            sb22.append(this.mEnableShot2Gallery);
                            sb22.append(" shotType=");
                            sb22.append(i2);
                            Log.d(str22, sb22.toString());
                            this.mCamera2Device.setShotType(i2);
                            this.mCamera2Device.setShot2Gallery(this.mEnableShot2Gallery);
                        }
                        i2 = 8;
                        String str222 = TAG;
                        StringBuilder sb222 = new StringBuilder();
                        sb222.append("enableParallel=");
                        sb222.append(this.mEnableParallelSession);
                        sb222.append(" enableShot2Gallery=");
                        sb222.append(this.mEnableShot2Gallery);
                        sb222.append(" shotType=");
                        sb222.append(i2);
                        Log.d(str222, sb222.toString());
                        this.mCamera2Device.setShotType(i2);
                        this.mCamera2Device.setShot2Gallery(this.mEnableShot2Gallery);
                    }
                    i2 = 5;
                    String str2222 = TAG;
                    StringBuilder sb2222 = new StringBuilder();
                    sb2222.append("enableParallel=");
                    sb2222.append(this.mEnableParallelSession);
                    sb2222.append(" enableShot2Gallery=");
                    sb2222.append(this.mEnableShot2Gallery);
                    sb2222.append(" shotType=");
                    sb2222.append(i2);
                    Log.d(str2222, sb2222.toString());
                    this.mCamera2Device.setShotType(i2);
                    this.mCamera2Device.setShot2Gallery(this.mEnableShot2Gallery);
                } else if (!(moduleIndex == 175 || moduleIndex == 182 || moduleIndex == 186 || moduleIndex == 205)) {
                    this.mEnableParallelSession = false;
                    return;
                }
            }
            if (!this.mEnableParallelSession) {
                if (this.mMultiSnapStatus) {
                    i2 = 3;
                } else if (getRawCallbackType(false) == 1) {
                }
            } else if (this.mMultiSnapStatus) {
                i2 = 9;
            }
            String str22222 = TAG;
            StringBuilder sb22222 = new StringBuilder();
            sb22222.append("enableParallel=");
            sb22222.append(this.mEnableParallelSession);
            sb22222.append(" enableShot2Gallery=");
            sb22222.append(this.mEnableShot2Gallery);
            sb22222.append(" shotType=");
            sb22222.append(i2);
            Log.d(str22222, sb22222.toString());
            this.mCamera2Device.setShotType(i2);
            this.mCamera2Device.setShot2Gallery(this.mEnableShot2Gallery);
        } else if (this.mEnableParallelSession) {
            i2 = -5;
            if (isDualFrontCamera() || isDualCamera() || isBokehUltraWideBackCamera()) {
                if (z) {
                    i = -7;
                }
                String str222222 = TAG;
                StringBuilder sb222222 = new StringBuilder();
                sb222222.append("enableParallel=");
                sb222222.append(this.mEnableParallelSession);
                sb222222.append(" enableShot2Gallery=");
                sb222222.append(this.mEnableShot2Gallery);
                sb222222.append(" shotType=");
                sb222222.append(i2);
                Log.d(str222222, sb222222.toString());
                this.mCamera2Device.setShotType(i2);
                this.mCamera2Device.setShot2Gallery(this.mEnableShot2Gallery);
            }
            if (z) {
                i = -6;
            }
            String str2222222 = TAG;
            StringBuilder sb2222222 = new StringBuilder();
            sb2222222.append("enableParallel=");
            sb2222222.append(this.mEnableParallelSession);
            sb2222222.append(" enableShot2Gallery=");
            sb2222222.append(this.mEnableShot2Gallery);
            sb2222222.append(" shotType=");
            sb2222222.append(i2);
            Log.d(str2222222, sb2222222.toString());
            this.mCamera2Device.setShotType(i2);
            this.mCamera2Device.setShot2Gallery(this.mEnableShot2Gallery);
        } else {
            i = z ? -3 : -2;
        }
        i2 = i;
        String str22222222 = TAG;
        StringBuilder sb22222222 = new StringBuilder();
        sb22222222.append("enableParallel=");
        sb22222222.append(this.mEnableParallelSession);
        sb22222222.append(" enableShot2Gallery=");
        sb22222222.append(this.mEnableShot2Gallery);
        sb22222222.append(" shotType=");
        sb22222222.append(i2);
        Log.d(str22222222, sb22222222.toString());
        this.mCamera2Device.setShotType(i2);
        this.mCamera2Device.setShot2Gallery(this.mEnableShot2Gallery);
    }

    private boolean updateSpecshotMode() {
        boolean z = false;
        if (!C0124O00000oO.isMTKPlatform()) {
            return false;
        }
        int i = this.mModuleIndex;
        if ((i == 163 || i == 165 || i == 175) && !CameraSettings.isFrontCamera() && ((Camera2DataContainer.getInstance().getMainBackCameraId() == getActualCameraId() || Camera2DataContainer.getInstance().getSATCameraId() == getActualCameraId()) && getZoomRatio() == 1.0f)) {
            z = true;
        }
        this.mCamera2Device.setSpecshotModeEnable(z);
        return z;
    }

    private void updateSuperNight() {
        boolean z = true;
        this.mCamera2Device.updateFlashAutoDetectionEnabled(true);
        if (this.mModuleIndex != 173 && !CameraSettings.isSuperNightOn()) {
            z = false;
        }
        this.mCaptureSuperNightExifInfo = this.mPreviewSuperNightExifInfo;
        if (CameraSettings.isSuperNightOn()) {
            this.mCamera2Device.updateFlashAutoDetectionEnabled(false);
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("<updateSuperNight>isSuperNightSeOn:");
        sb.append(CameraSettings.isSuperNightOn());
        Log.d(str, sb.toString());
        this.mCamera2Device.setSuperNight(z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0174, code lost:
        if (r6.mMutexModePicker.isSuperResolution() != false) goto L_0x0176;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateSuperResolution() {
        if (!isFrontCamera() && this.mModuleIndex != 173 && !CameraSettings.isSuperNightOn()) {
            if (isUltraWideBackCamera()) {
                Log.d(TAG, "SR force off for ultra wide camera");
            } else if (isStandaloneMacroCamera() && !C0122O00000o.instance().OOo0o0O()) {
                Log.d(TAG, "HAL doesn't support SR in macro mode.");
            } else if (isStandaloneMacroCamera() && this.mCameraCapabilities.isMfnrMacroZoomSupported()) {
                Log.d(TAG, "macro camera prefers MFNR to SR");
            } else if (!CameraSettings.isSREnable()) {
                Log.d(TAG, "SR is disabled");
            } else {
                if (CameraSettings.isUltraPixelOn()) {
                    if (C0122O00000o.instance().OOOoooO()) {
                        this.mCamera2Device.setSuperResolution(this.mUpscaleImageWithSR);
                        return;
                    } else if (C0122O00000o.instance().OOOOO00() && DataRepository.dataItemRunning().getComponentUltraPixel().isRearSwitchOn()) {
                        Log.d(TAG, "108MP or 64MP doesn't support SR");
                        return;
                    }
                }
                boolean z = true;
                if (this.mModuleIndex == 167) {
                    boolean OOoO0oo = C0122O00000o.instance().OOoO0oo();
                    if (getRawCallbackType(false) == 0 && OOoO0oo && ((isUltraTeleCamera() || isAuxCamera()) && this.mCamera2Device != null)) {
                        if (Long.parseLong(getManualValue(CameraSettings.KEY_QC_EXPOSURETIME, getString(R.string.pref_camera_exposuretime_default))) < 250000000) {
                            this.mCamera2Device.setSuperResolution(true);
                            return;
                        }
                    }
                    Camera2Proxy camera2Proxy = this.mCamera2Device;
                    if (camera2Proxy != null) {
                        camera2Proxy.setSuperResolution(false);
                    }
                    return;
                }
                float zoomRatio = getZoomRatio();
                String str = "pref_camera_super_resolution_key";
                if (zoomRatio > 1.0f || this.mUpscaleImageWithSR) {
                    if (C0124O00000oO.isMTKPlatform()) {
                        Camera2Proxy camera2Proxy2 = this.mCamera2Device;
                        if (camera2Proxy2 == null || camera2Proxy2.getSatMasterCameraId() != 1) {
                            z = false;
                        }
                        if (zoomRatio > 1.0f && z && !DataRepository.dataItemRunning().isSwitchOn(str)) {
                            String str2 = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("currentZoomRatio: ");
                            sb.append(zoomRatio);
                            sb.append("  isUW: ");
                            sb.append(z);
                            Log.w(str2, sb.toString());
                            if (this.mMutexModePicker.isSuperResolution()) {
                                this.mMutexModePicker.resetMutexMode();
                            } else {
                                Camera2Proxy camera2Proxy3 = this.mCamera2Device;
                                if (camera2Proxy3 != null) {
                                    camera2Proxy3.setSuperResolution(false);
                                }
                            }
                            return;
                        }
                    }
                    if (!CameraSettings.isGroupShotOn()) {
                        if (this.mMutexModePicker.isNormal()) {
                            this.mMutexModePicker.setMutexMode(9);
                        }
                    }
                } else if (!DataRepository.dataItemRunning().isSwitchOn(str)) {
                    if (!this.mMutexModePicker.isSuperResolution()) {
                        Camera2Proxy camera2Proxy4 = this.mCamera2Device;
                        if (camera2Proxy4 != null) {
                            camera2Proxy4.setSuperResolution(false);
                        }
                    }
                }
                this.mMutexModePicker.resetMutexMode();
            }
        }
    }

    private void updateSwMfnr() {
        boolean isUseSwMfnr = isUseSwMfnr();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setSwMfnr to ");
        sb.append(isUseSwMfnr);
        Log.d(str, sb.toString());
        this.mCamera2Device.setSwMfnr(isUseSwMfnr);
    }

    private void updateTargetZoom() {
        float readTargetZoom = CameraSettings.readTargetZoom();
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setTargetZoom(readTargetZoom);
        }
    }

    private void updateThumbProgress(boolean z) {
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        if (actionProcessing != null) {
            actionProcessing.updateLoading(z);
        }
    }

    private void updateUltraPixelPortrait() {
        this.mCamera2Device.setUltraPixelPortrait(CameraSettings.isUltraPixelPortraitFrontOn());
    }

    private void updateUltraWideLDC() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setUltraWideLDC(shouldApplyUltraWideLDC());
            CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
            if (cameraCapabilities != null && cameraCapabilities.supportSATUltraWideLDCEnable()) {
                this.mCamera2Device.setSATUltraWideLDC(CameraSettings.isUltraWideLDCEnabled());
            }
        }
    }

    private void updateWatermarkTag() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setNewWatermark(true);
            if (Util.isGlobalVersion()) {
                this.mCamera2Device.setGlobalWatermark();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateWatermarkUI(WatermarkItem watermarkItem) {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null && watermarkItem != null) {
            mainContentProtocol.updateWatermarkSample(watermarkItem);
        }
    }

    private void updateWhiteBalance() {
        setAWBMode(getManualValue(CameraSettings.KEY_WHITE_BALANCE, "1"));
    }

    private void updateZsl() {
        this.mCamera2Device.setEnableZsl(isZslPreferred());
    }

    public /* synthetic */ void O000000o(float f, float f2, int i, int i2, int i3) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onOptionClick: which = ");
        sb.append(i3);
        Log.d(str, sb.toString());
        CameraStatUtils.trackGoogleLensPickerValue(i3 == 0);
        String str2 = CameraSettings.KEY_LONG_PRESS_VIEWFINDER;
        if (i3 == 0) {
            DataRepository.dataItemGlobal().editor().putBoolean(CameraSettings.KEY_GOOGLE_LENS_OOBE, true).apply();
            DataRepository.dataItemGlobal().editor().putString(str2, getString(R.string.pref_camera_long_press_viewfinder_default)).apply();
            O00000o.getInstance().O000000o(2, f / ((float) Display.getWindowWidth()), f2 / ((float) Display.getWindowHeight()));
        } else if (i3 == 1) {
            DataRepository.dataItemGlobal().editor().putString(str2, getString(R.string.pref_camera_long_press_viewfinder_lock_ae_af)).apply();
            DataRepository.dataItemGlobal().editor().putBoolean(CameraSettings.KEY_EN_FIRST_CHOICE_LOCK_AE_AF_TOAST, true).apply();
            onSingleTapUp(i, i2, true);
            if (this.m3ALockSupported) {
                lockAEAF();
            }
            this.mMainProtocol.performHapticFeedback(0);
        }
    }

    public /* synthetic */ void O000000o(CameraHardwareFace[] cameraHardwareFaceArr) {
        if (cameraHardwareFaceArr.length > 0) {
            if (!this.mIsFaceConflict) {
                this.mIsFaceConflict = true;
                showOrHideChip(false);
            }
        } else if (this.mIsFaceConflict) {
            this.mIsFaceConflict = false;
            showOrHideChip(true);
        }
    }

    public /* synthetic */ void O00000Oo(String str, Bitmap bitmap) {
        try {
            String replace = str.replace("IMG_", "IMG_Preview_");
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("showDocumentPreview mShootOrientation = ");
            sb.append(this.mShootOrientation);
            Log.i(str2, sb.toString());
            if (this.mShootOrientation != 0) {
                bitmap = Util.adjustPhotoRotation(bitmap, this.mShootOrientation);
            }
            Util.saveToFile(bitmap, replace, 100, CompressFormat.JPEG);
            MediaScannerConnection.scanFile(this.mActivity, new String[]{replace}, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public /* synthetic */ void O00oOO() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertAiDetectTipHint(8, R.string.super_night_hint, -1);
        }
        this.mShowSuperNightHint = false;
    }

    public /* synthetic */ void O00oOO0() {
        showOrHideLoadingProgress(false, false);
    }

    public /* synthetic */ void O00oOO00() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertAiDetectTipHint(8, R.string.super_night_hint, -1);
            this.mShowLLSHint = false;
        }
    }

    public /* synthetic */ void O00oOOO() {
        this.mIsFaceConflict = false;
        this.mIsAiConflict = false;
        this.mIsUltraWideConflict = false;
        showOrHideChip(true);
    }

    public /* synthetic */ void O00oOOOO() {
        consumeAiSceneResult(0, true);
        this.isResetFromMutex = true;
    }

    public /* synthetic */ void O00oOOOo() {
        this.mActivity.getSensorStateManager().setLieIndicatorEnabled(true);
    }

    public /* synthetic */ void O00oOOo() {
        this.mWaitSaveFinish = false;
        this.mHandler.removeMessages(61);
        Log.i(TAG, "showDocumentPreview finished");
        this.mMainProtocol.hideOrShowDocument(true);
        PreviewDecodeManager.getInstance().startDecode();
    }

    public /* synthetic */ void O00oOOo0() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
    }

    public /* synthetic */ void O00oOOoO() {
        showOrHideChip(false);
        this.mIsFaceConflict = false;
        this.mIsUltraWideConflict = false;
        this.mIsAiConflict = false;
    }

    public /* synthetic */ void O00oOo() {
        showOrHideLoadingProgress(false, false);
    }

    public /* synthetic */ void O00oOo0() {
        this.mFocusManager.cancelFocus();
    }

    public /* synthetic */ void O00oOo00() {
        TopAlert topAlert = this.mTopAlert;
        if (topAlert != null && topAlert.canProvide()) {
            this.mTopAlert.updateConfigItem(194);
        }
    }

    public /* synthetic */ void O00oOooO(boolean z) {
        this.mMainProtocol.setEvAdjustable(z);
    }

    public void addSaveTask(String str, ContentValues contentValues) {
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        SaveVideoTask saveVideoTask = new SaveVideoTask(str, contentValues);
        synchronized (this) {
            this.mPendingSaveTaskList.add(saveVideoTask);
        }
    }

    /* access modifiers changed from: protected */
    public void applyZoomRatio() {
        super.applyZoomRatio();
        updateSpecshotMode();
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

    public void closeBacklightTip(int i, int i2) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        int i3 = this.mCurrentAiScene;
        if (i3 == -1 && i3 != i) {
            topAlert.alertAiSceneSelector(i2);
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
                if (this.mBurstDisposable != null) {
                    this.mBurstDisposable.dispose();
                }
                if (this.mHistogramEmitter != null) {
                    this.mHistogramEmitter.onComplete();
                }
                if (this.mHistogramDisposable != null && !this.mHistogramDisposable.isDisposed()) {
                    this.mHistogramDisposable.dispose();
                    this.mHistogramDisposable = null;
                }
                if (this.mMetaDataFlowableEmitter != null) {
                    this.mMetaDataFlowableEmitter.onComplete();
                }
                if (this.mMetaDataDisposable != null) {
                    this.mMetaDataDisposable.dispose();
                }
                if (this.mAiSceneFlowableEmitter != null) {
                    this.mAiSceneFlowableEmitter.onComplete();
                }
                if (this.mAiSceneDisposable != null) {
                    this.mAiSceneDisposable.dispose();
                }
                if (this.mSuperNightDisposable != null) {
                    this.mSuperNightDisposable.dispose();
                }
                this.mCamera2Device.setScreenLightCallback(null);
                this.mCamera2Device.setMetaDataCallback(null);
                this.mCamera2Device.setErrorCallback(null);
                this.mCamera2Device.setFocusCallback(null);
                this.mCamera2Device.setAiASDEnable(false);
                if (this.mCameraCapabilities.isSupportAIIE()) {
                    this.mCamera2Device.setAIIEPreviewEnable(false);
                }
                if (scanQRCodeEnabled() || C0124O00000oO.OOO0Oo() || this.mIsGoogleLensAvailable || this.mSupportAnchorFrameAsThumbnail || CameraSettings.isDocumentMode2On(this.mModuleIndex)) {
                    this.mCamera2Device.stopPreviewCallback(true);
                }
                if (this.mFaceDetectionStarted) {
                    this.mFaceDetectionStarted = false;
                }
                this.m3ALocked = false;
                this.mCamera2Device.setASDEnable(false);
                this.mCamera2Device.setColorEnhanceEnable(false);
                EffectController.getInstance().setAiSceneEffect(FilterInfo.FILTER_ID_NONE, false);
                synchronized (this.mDeviceLock) {
                    if (!isParallelCameraSessionMode()) {
                        this.mCamera2Device = null;
                    }
                }
            }
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(false);
            this.mFocusManager.destroy();
        }
        if (scanQRCodeEnabled() || C0124O00000oO.OOO0Oo() || this.mIsGoogleLensAvailable || CameraSettings.isDocumentMode2On(this.mModuleIndex)) {
            PreviewDecodeManager.getInstance().quit();
        }
        if (this.mSupportAnchorFrameAsThumbnail) {
            this.mCacheImageDecoder.quit();
        }
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
        if (localBinder != null) {
            localBinder.setOnSessionStatusCallBackListener(null);
        }
        stopCpuBoost();
        Log.d(TAG, "closeCamera: X");
    }

    public void closeMoonMode(int i, int i2) {
        if (this.mEnteringMoonMode) {
            int i3 = this.mCurrentAiScene;
            if ((i3 == 10 || i3 == 35) && i != this.mCurrentAiScene) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.alertMoonModeSelector(i2, false);
                    if (i2 != 0) {
                        this.mEnteringMoonMode = false;
                    }
                    if (8 == i2) {
                        Log.d(TAG, "(moon_mode) close moon mode");
                    }
                    ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
                    if (!componentHdr.isEmpty() && !topAlert.isHDRShowing()) {
                        String componentValue = componentHdr.getComponentValue(this.mModuleIndex);
                        if ("on".equals(componentValue) || "normal".equals(componentValue)) {
                            topAlert.alertHDR(0, false, false);
                        }
                    }
                }
                updateMoon(false);
                if (this.mMutexModePicker.isSuperResolution() && !C0122O00000o.instance().OOOOOO()) {
                    this.mCamera2Device.setSuperResolution(true);
                }
            }
        }
    }

    public void consumePreference(@UpdateType int... iArr) {
        int length = iArr.length;
        for (int i = 0; i < length; i++) {
            int i2 = iArr[i];
            if (i2 == 52) {
                updateMacroMode();
            } else if (i2 == 66) {
                updateThermalLevel();
            } else if (i2 == 70) {
                updateASD();
            } else if (i2 == 82) {
                updateAiShutter();
            } else if (i2 == 84) {
                updateHighQualityPreferred();
            } else if (i2 == 51966) {
                updateSessionParams();
            } else if (i2 == 73) {
                updateAIWatermark();
            } else if (i2 == 74) {
                updateColorEnhance();
            } else if (i2 == 88 || i2 == 89) {
                findBestWatermarkItem(i2);
            } else {
                switch (i2) {
                    case 1:
                        updatePictureAndPreviewSize();
                        continue;
                    case 2:
                        updateFilter();
                        continue;
                    case 3:
                        updateFocusArea();
                        continue;
                    case 4:
                        updateScene();
                        continue;
                    case 5:
                        updateFace();
                        continue;
                    case 6:
                        updateWhiteBalance();
                        continue;
                    case 7:
                        updateJpegQuality();
                        continue;
                    case 8:
                        updateJpegThumbnailSize();
                        continue;
                    case 9:
                        updateAntiBanding(CameraSettings.getAntiBanding());
                        continue;
                    case 10:
                        updateFlashPreference();
                        continue;
                    case 11:
                        updateHDRPreference();
                        continue;
                    case 12:
                        setEvValue();
                        continue;
                    case 13:
                        updateBeauty();
                        break;
                    case 14:
                        updateFocusMode();
                        continue;
                    case 15:
                        updateISO();
                        continue;
                    case 16:
                        updateExposureTime();
                        continue;
                    default:
                        switch (i2) {
                            case 19:
                                updateFpsRange();
                                continue;
                            case 20:
                                updateOIS();
                                continue;
                            case 21:
                                updateMute();
                                continue;
                            case 22:
                                updateZsl();
                                continue;
                            case 23:
                                updateDecodePreview();
                                continue;
                            case 24:
                                applyZoomRatio();
                                continue;
                            case 25:
                                focusCenter();
                                continue;
                            case 26:
                                updateContrast();
                                continue;
                            case 27:
                                updateSaturation();
                                continue;
                            case 28:
                                updateSharpness();
                                continue;
                            case 29:
                                updateExposureMeteringMode();
                                continue;
                            case 30:
                                updateSuperResolution();
                                continue;
                            default:
                                switch (i2) {
                                    case 34:
                                        updateMfnr(CameraSettings.isMfnrSatEnable());
                                        continue;
                                    case 35:
                                        updateDeviceOrientation();
                                        continue;
                                    case 36:
                                        updateAiScene();
                                        continue;
                                    case 37:
                                        updateBokeh();
                                        continue;
                                    case 38:
                                        updateFaceAgeAnalyze();
                                        continue;
                                    case 39:
                                        updateFaceScore();
                                        continue;
                                    case 40:
                                        updateFrontMirror();
                                        continue;
                                    default:
                                        switch (i2) {
                                            case 42:
                                                updateSwMfnr();
                                                continue;
                                            case 43:
                                                updatePortraitLighting();
                                                continue;
                                            case 44:
                                                updateShotDetermine();
                                                continue;
                                            case 45:
                                                break;
                                            case 46:
                                                updateNormalWideLDC();
                                                continue;
                                            case 47:
                                                updateUltraWideLDC();
                                                continue;
                                            case 48:
                                                updateFNumber();
                                                continue;
                                            case 49:
                                                updateLiveShot();
                                                continue;
                                            case 50:
                                                continue;
                                                continue;
                                                continue;
                                            default:
                                                switch (i2) {
                                                    case 55:
                                                        updateModuleRelated();
                                                        break;
                                                    case 56:
                                                        updateSuperNight();
                                                        break;
                                                    case 57:
                                                        updateUltraPixelPortrait();
                                                        break;
                                                    case 58:
                                                        updateBackSoftLightPreference();
                                                        break;
                                                    case 59:
                                                        updateOnTripMode();
                                                        break;
                                                    case 60:
                                                        updateCinematicPhoto();
                                                        break;
                                                    case 61:
                                                        updateASDDirtyDetect();
                                                        break;
                                                    case 62:
                                                        updateWatermarkTag();
                                                        break;
                                                    case 63:
                                                        updateEvValue();
                                                        break;
                                                    default:
                                                        switch (i2) {
                                                            case 76:
                                                                updateDoDepurple();
                                                                break;
                                                            case 77:
                                                                updateNearRangeMode(true, !CameraSettings.isSettingNearRangeEnable());
                                                                break;
                                                            case 78:
                                                                updateSpecshotMode();
                                                                break;
                                                            case 79:
                                                                updateTargetZoom();
                                                                continue;
                                                            default:
                                                                StringBuilder sb = new StringBuilder();
                                                                sb.append("no consumer for this updateType: ");
                                                                sb.append(i2);
                                                                throw new RuntimeException(sb.toString());
                                                        }
                                                }
                                        }
                                }
                        }
                }
                updateEyeLight();
            }
        }
    }

    public void dealTimerBurst(long j) {
        if (CameraSettings.isTimerBurstEnable() && TimerBurstController.isSupportTimerBurst(this.mModuleIndex)) {
            TimerBurstController timerBurstController = DataRepository.dataItemLive().getTimerBurstController();
            final int totalCount = timerBurstController.getTotalCount();
            long intervalTimer = timerBurstController.getIntervalTimer();
            if (totalCount <= 1) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        Camera2Module.this.stopTimerBurst();
                        DataRepository.dataItemLive().getTimerBurstController().decreaseCount(totalCount);
                    }
                });
            } else if (timerBurstController.isInTimerBurstShotting()) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dealTimerBurst: TimerTask");
                sb.append(intervalTimer);
                sb.append("   now:");
                sb.append(j);
                Log.i(str, sb.toString());
                DataRepository.dataItemLive().getTimerBurstController().decreaseCount(totalCount);
                Handler handler = this.mHandler;
                handler.sendMessage(handler.obtainMessage(63));
            }
        }
    }

    public void enterMutexMode(int i) {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy == null) {
            Log.d(TAG, "enterMutexMode error, mCamera2Device is null");
            return;
        }
        if (i == 1) {
            boolean z = false;
            if (C0122O00000o.instance().OOo0OOO()) {
                String componentValue = DataRepository.dataItemConfig().getComponentHdr().getComponentValue(this.mModuleIndex);
                if (getZoomRatio() > HybridZoomingSystem.getZoomRatioNone(isFrontCamera(), this.mOrientation) && "auto".equals(componentValue)) {
                    z = true;
                }
            }
            this.mCamera2Device.setHDR(new HDRStatus(true, this.mIsNeedNightHDR, z));
        } else if (i == 3) {
            camera2Proxy.setHHT(true);
        } else if (i == 9) {
            camera2Proxy.setSuperResolution(true);
        }
        updateMfnr(CameraSettings.isMfnrSatEnable());
        updateSwMfnr();
    }

    public void executeSaveTask(boolean z) {
        synchronized (this) {
            do {
                if (this.mPendingSaveTaskList.isEmpty()) {
                    break;
                }
                SaveVideoTask saveVideoTask = (SaveVideoTask) this.mPendingSaveTaskList.remove(0);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("executeSaveTask: ");
                sb.append(saveVideoTask.videoPath);
                Log.d(str, sb.toString());
                this.mActivity.getImageSaver().addVideo(saveVideoTask.videoPath, saveVideoTask.contentValues, true);
            } while (!z);
            doLaterReleaseIfNeed();
        }
    }

    public void exitMutexMode(int i) {
        if (i == 1) {
            this.mCamera2Device.setHDR(new HDRStatus(false, false, false));
            updateSuperResolution();
        } else if (i == 3) {
            this.mCamera2Device.setHHT(false);
        } else if (i == 9) {
            this.mCamera2Device.setSuperResolution(false);
        }
        updateMfnr(CameraSettings.isMfnrSatEnable());
        updateSwMfnr();
    }

    public void fillFeatureControl(StartControl startControl) {
        super.fillFeatureControl(startControl);
        int i = startControl.mTargetMode;
        if ((i == 182 || i == 186) && C0122O00000o.instance().OOO0OoO()) {
            startControl.getFeatureDetail().addFragmentInfo(R.id.id_card_content, BaseFragmentDelegate.FRAGMENT_ID_CARD);
        }
    }

    /* access modifiers changed from: protected */
    public void focusCenter() {
    }

    public boolean getAutoHDRTargetState() {
        return this.mAutoHDRTargetState;
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

    public long getCaptureStartTime() {
        return this.mCaptureStartTime;
    }

    public CircularMediaRecorder getCircularMediaRecorder() {
        CircularMediaRecorder circularMediaRecorder;
        synchronized (this.mCircularMediaRecorderStateLock) {
            circularMediaRecorder = this.mCircularMediaRecorder;
        }
        return circularMediaRecorder;
    }

    public int getCurrentAiScene() {
        return this.mCurrentAiScene;
    }

    public String getDebugInfo() {
        String str;
        StringBuilder sb = new StringBuilder();
        int moduleIndex = getModuleIndex();
        CameraCapabilities cameraCapabilities = getCameraCapabilities();
        String str2 = " ";
        if (cameraCapabilities != null) {
            CameraCharacteristics cameraCharacteristics = cameraCapabilities.getCameraCharacteristics();
            if (cameraCharacteristics != null) {
                float[] fArr = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
                float[] fArr2 = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);
                if (fArr != null && fArr.length > 0) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("LensFocal:");
                    sb2.append(fArr[0]);
                    sb2.append(str2);
                    sb.append(sb2.toString());
                }
                if (fArr2 != null && fArr2.length > 0) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("LensApertues:");
                    sb3.append(fArr2[0]);
                    sb3.append(str2);
                    sb.append(sb3.toString());
                }
            }
        }
        if (moduleIndex == 167) {
            sb.append("SceneProfession:true");
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("ZoomMultiple:");
        sb4.append(getZoomRatio());
        sb4.append(str2);
        sb.append(sb4.toString());
        Camera2Proxy cameraDevice = getCameraDevice();
        if (cameraDevice != null) {
            CameraConfigs cameraConfigs = cameraDevice.getCameraConfigs();
            if (cameraConfigs != null) {
                MeteringRectangle[] aFRegions = cameraConfigs.getAFRegions();
                if (aFRegions != null && aFRegions.length > 0) {
                    MeteringRectangle meteringRectangle = aFRegions[0];
                    if (meteringRectangle == null) {
                        str = "0";
                    } else {
                        int x = meteringRectangle.getX();
                        int y = meteringRectangle.getY();
                        int width = meteringRectangle.getWidth() + x;
                        int height = meteringRectangle.getHeight() + y;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("[");
                        sb5.append(x);
                        String str3 = ",";
                        sb5.append(str3);
                        sb5.append(y);
                        sb5.append(str3);
                        sb5.append(width);
                        sb5.append(str3);
                        sb5.append(height);
                        sb5.append("]");
                        str = sb5.toString();
                    }
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("afRoi:");
                    sb6.append(str);
                    sb6.append(str2);
                    sb.append(sb6.toString());
                }
            }
        }
        String retriveFaceInfo = DebugInfoUtil.getRetriveFaceInfo(this.mMainProtocol.getViewRects(this.mPictureSize));
        if (!TextUtils.isEmpty(retriveFaceInfo)) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("FaceRoi:");
            sb7.append(retriveFaceInfo);
            sb7.append(str2);
            sb.append(sb7.toString());
        }
        StringBuilder sb8 = new StringBuilder();
        sb8.append("FilterId:");
        sb8.append(CameraSettings.getShaderEffect());
        sb8.append(str2);
        sb.append(sb8.toString());
        StringBuilder sb9 = new StringBuilder();
        sb9.append("AIScene:");
        sb9.append(getCurrentAiScene());
        sb9.append(str2);
        sb.append(sb9.toString());
        return sb.toString();
    }

    public int getFilterId() {
        return this.mNormalFilterId;
    }

    /* access modifiers changed from: protected */
    public int getMaxPictureSize() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getMutexHdrMode(String str) {
        if ("normal".equals(str)) {
            return 1;
        }
        return (!C0124O00000oO.Oo00OOO() || !"live".equals(str)) ? 0 : 2;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x01c7, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.O0o0O0 != false) goto L_0x01bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x020b, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.O0o0O0 == false) goto L_0x01ca;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x01bb, code lost:
        if (r14.mCameraCapabilities.isSupportLightTripartite() != false) goto L_0x01bd;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getOperatingMode() {
        int i = 32778;
        if (isParallelSessionEnable()) {
            if (isInQCFAMode()) {
                Log.k(3, TAG, "getOperatingMode: SESSION_OPERATION_MODE_ALGO_UP_QCFA");
                i = CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_QCFA;
            } else if (167 == getModuleIndex()) {
                if (CameraSettings.isUltraPixelOn()) {
                    Log.k(3, TAG, "getOperatingMode: SESSION_OPERATION_MODE_ALGO_UP_MANUAL_ULTRA_PIXEL");
                    i = CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_MANUAL_ULTRA_PIXEL;
                } else {
                    Log.k(3, TAG, "getOperatingMode: SESSION_OPERATION_MODE_ALGO_UP_MANUAL");
                    i = C0124O00000oO.OOooOO() ? CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_MANUAL_G7 : CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_MANUAL;
                }
            } else if (171 != getModuleIndex()) {
                String str = "getOperatingMode: SESSION_OPERATION_MODE_ALGO_UP_NORMAL";
                if (182 != getModuleIndex()) {
                    if (DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode() || DataRepository.dataItemGlobal().isOnSuperNightHalfAlgoUp()) {
                        Log.k(3, TAG, "getOperatingMode: SESSION_OPERATION_MODE_SUPER_NIGHT");
                    } else if (CameraSettings.isUltraPixelOn()) {
                        Log.k(3, TAG, "getOperatingMode: SESSION_OPERATION_MODE_ALGO_UP_HD");
                        i = CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_HD;
                    } else if (!isUltraWideBackCamera() && CameraSettings.isSupportedOpticalZoom() && ((!this.mIsImageCaptureIntent || !this.mCameraCapabilities.isSupportLightTripartite()) && !isFrontCamera() && !DataRepository.dataItemRunning().getComponentRunningMacroMode().isSwitchOn(getModuleIndex()))) {
                        Log.k(3, TAG, "getOperatingMode: SESSION_OPERATION_MODE_ALGO_UP_SAT");
                        i = CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_SAT;
                    }
                }
                Log.k(3, TAG, str);
                i = 36869;
            } else if (!isFrontCamera() || isDualFrontCamera()) {
                Log.k(3, TAG, "getOperatingMode: SESSION_OPERATION_MODE_ALGO_UP_DUAL_BOKEH");
                i = 36864;
            } else {
                Log.k(3, TAG, "getOperatingMode: SESSION_OPERATION_MODE_ALGO_UP_SINGLE_BOKEH");
                i = CameraCapabilities.SESSION_OPERATION_MODE_ALGO_UP_SINGLE_BOKEH;
            }
            this.mOperatingMode = i;
            return i;
        }
        int i2 = 32770;
        if (isFrontCamera()) {
            mIsBeautyFrontOn = true;
            int i3 = 32775;
            if (!isPortraitMode() || !C0122O00000o.instance().OOoO0()) {
                if (!isPortraitMode() || !isBokehFrontCamera()) {
                    if (this.mCameraCapabilities.isSupportedQcfa() && !mIsBeautyFrontOn) {
                        if ("off".equals(DataRepository.dataItemConfig().getComponentHdr().getComponentValue(this.mModuleIndex)) && C0122O00000o.instance().O0ooo00() < 0) {
                            i2 = 32775;
                        }
                    }
                    i2 = 32773;
                }
            } else if (!isBokehFrontCamera()) {
                i2 = 33009;
            }
            if (this.mModuleIndex != 163 || !CameraSettings.isUltraPixelOn()) {
                i3 = i2;
            }
            if (!C0122O00000o.instance().OOo0Oo() || this.mModuleIndex != 173) {
                i = i3;
            }
        } else {
            ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
            int moduleIndex = getModuleIndex();
            if (moduleIndex != 163) {
                if (moduleIndex == 167) {
                    i = CameraSettings.isUltraPixelOn() ? CameraCapabilities.SESSION_OPERATION_MODE_PROFESSIONAL_ULTRA_PIXEL_PHOTOGRAPHY : 32771;
                } else if (moduleIndex == 171) {
                    i = 32770;
                } else if (moduleIndex != 173) {
                    if (moduleIndex != 175) {
                        if (!(moduleIndex == 182 || moduleIndex == 186 || moduleIndex == 205)) {
                            if (this.mIsImageCaptureIntent) {
                            }
                            if (componentRunningMacroMode.isSwitchOn(moduleIndex)) {
                            }
                            i = 32769;
                        }
                    }
                    i = 33011;
                }
            }
            if (!this.mIsImageCaptureIntent || !this.mCameraCapabilities.isSupportLightTripartite()) {
                if (!CameraSettings.isUltraPixelOn()) {
                    if (CameraSettings.isDualCameraSatEnable()) {
                        if (!C0122O00000o.instance().OOo00()) {
                            if (componentRunningMacroMode.isSwitchOn(moduleIndex)) {
                            }
                            i = 32769;
                        }
                    }
                }
                i = 33011;
            }
            i = 0;
        }
        this.mOperatingMode = i;
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getOperatingMode: ");
        sb.append(String.format("operatingMode = 0x%x", new Object[]{Integer.valueOf(i)}));
        Log.k(3, str2, sb.toString());
        return i;
    }

    public String getTag() {
        return TAG;
    }

    public void initializeCapabilities() {
        super.initializeCapabilities();
        this.mContinuousFocusSupported = Util.isSupported(4, this.mCameraCapabilities.getSupportedFocusModes());
        this.mMaxFaceCount = this.mCameraCapabilities.getMaxFaceCount();
    }

    public boolean isAutoFlashOff() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isAutoRestartInNonZSL() {
        return false;
    }

    public boolean isBeautyBodySlimCountDetectStarted() {
        return this.mIsBeautyBodySlimOn;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005e, code lost:
        if (getCameraState() == 3) goto L_0x0060;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x006c, code lost:
        if (r0.isParallelBusy(false) != false) goto L_0x0060;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBlockSnap() {
        boolean z;
        boolean z2 = DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode() && !C0122O00000o.instance().OOoOOO0();
        if (C0124O00000oO.Oo0O0Oo()) {
            z2 = z2 || isCaptureWillCostHugeMemory();
        }
        if (isParallelSessionEnable() && z2) {
            LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
            if (localBinder != null && !localBinder.isIdle()) {
                Log.i(TAG, "isBlockSnap: shooting super night or shooting with huge memory, then discard snap");
                return true;
            }
        }
        if (isParallelCameraSessionMode()) {
            if (!this.mCamera2Device.isNeedFlashOn()) {
                Camera2Proxy camera2Proxy = this.mCamera2Device;
                if (camera2Proxy != null) {
                }
                z = false;
            }
            z = true;
        } else {
            z = getCameraState() == 3;
            if (!z) {
                LocalBinder localBinder2 = AlgoConnector.getInstance().getLocalBinder();
                z = localBinder2 != null && localBinder2.isAnyRequestBlocked();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("isBlockSnap snapshotInProgress: getCameraState() : ");
                sb.append(getCameraState());
                Log.d(str, sb.toString());
            }
        }
        if (CameraSettings.isDocumentMode2On(this.mModuleIndex)) {
            Decoder decoder = PreviewDecodeManager.getInstance().getDecoder(3);
            if (decoder == null || ((DocumentDecoder) decoder).getCachePreview() == null) {
                Log.d(TAG, "isBlockSnap: document cache preview is null...");
                return true;
            }
        }
        if (this.mPaused) {
            Log.d(TAG, "isBlockSnap: paused");
            return true;
        } else if (this.isZooming) {
            Log.d(TAG, "isBlockSnap: zooming");
            return true;
        } else if (isKeptBitmapTexture()) {
            Log.d(TAG, "isBlockSnap: isKeptBitmapTexture");
            return true;
        } else if (this.mMultiSnapStatus) {
            Log.d(TAG, "isBlockSnap: multiSnap");
            return true;
        } else if (getCameraState() == 0) {
            Log.d(TAG, "isBlockSnap: getCameraState() = CameraStateConstant.PREVIEW_STOPPED");
            return true;
        } else if (z) {
            Log.d(TAG, "isBlockSnap: snapshot is in progress");
            return true;
        } else {
            Camera2Proxy camera2Proxy2 = this.mCamera2Device;
            if (camera2Proxy2 != null) {
                boolean z3 = this.mMutexModePicker.isHdr() && !isParallelCameraSessionMode();
                if (camera2Proxy2.isCaptureBusy(z3)) {
                    Log.d(TAG, "isBlockSnap: mCamera2Device's boolean is true");
                    return true;
                }
            }
            if (isQueueFull()) {
                Log.d(TAG, "isBlockSnap: queue is full");
                return true;
            } else if (isInCountDown()) {
                Log.d(TAG, "isBlockSnap: counting down");
                return true;
            } else if (this.mWaitSaveFinish) {
                Log.d(TAG, "isBlockSnap: waiting save finish");
                return true;
            } else if (!isParallelSessionConfigured()) {
                Log.d(TAG, "isBlockSnap: parallel session hasn't been configured");
                return true;
            } else if (!this.mHandler.hasMessages(62)) {
                return false;
            } else {
                Log.d(TAG, "isBlockSnap: has message MSG_RESUME_CAPTURE");
                return true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isCameraSwitchingDuringZoomingAllowed() {
        if (CameraSettings.isSupportedOpticalZoom() && !CameraSettings.isFakePartSAT()) {
            return super.isCameraSwitchingDuringZoomingAllowed();
        }
        boolean z = HybridZoomingSystem.IS_3_OR_MORE_SAT && !CameraSettings.isMacroModeEnabled(this.mModuleIndex) && !ModuleManager.isProModule() && isBackCamera();
        return z;
    }

    public boolean isCaptureIntent() {
        return this.mIsImageCaptureIntent;
    }

    public boolean isCaptureWillCostHugeMemory() {
        boolean z;
        String str;
        String str2;
        ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
        if (this.isDetectedInHdr && !componentHdr.isEmpty()) {
            if (!"off".equals(componentHdr.getComponentValue(this.mModuleIndex))) {
                z = true;
                boolean z2 = isDisableWatermark() && (CameraSettings.isTimeWaterMarkOpen() || CameraSettings.isDualCameraWaterMarkOpen());
                boolean isFaceBeautyOn = isFaceBeautyOn(this.mBeautyValues);
                if (this.mModuleIndex != 163 && isBackCamera() && z && this.mAiSceneEnabled && z2 && isFaceBeautyOn) {
                    str = TAG;
                    str2 = "isCaptureWillCostHugeMemory: true >>> hdr_ai_beauty_watermark_0 ";
                } else if (this.mModuleIndex == 171 || !isBackCamera() || !this.mIsPortraitLightingOn || !this.mAiSceneEnabled || !z2 || !isFaceBeautyOn) {
                    int shotType = this.mCamera2Device.getCameraConfigs().getShotType();
                    if (shotType == 5 || shotType == 7 || shotType == 6) {
                        Integer num = this.mSpecShotMode;
                        if (num != null && (num.intValue() == 1 || this.mSpecShotMode.intValue() == 2)) {
                            str = TAG;
                            str2 = "isCaptureWillCostHugeMemory: true >>> capture will trigger AINR ";
                        }
                    }
                    return false;
                } else {
                    str = TAG;
                    str2 = "isCaptureWillCostHugeMemory: true >>> portrait_studio_light_ai_beauty_watermark_0 ";
                }
                Log.d(str, str2);
                return true;
            }
        }
        z = false;
        if (isDisableWatermark()) {
        }
        boolean isFaceBeautyOn2 = isFaceBeautyOn(this.mBeautyValues);
        if (this.mModuleIndex != 163) {
        }
        if (this.mModuleIndex == 171) {
        }
        int shotType2 = this.mCamera2Device.getCameraConfigs().getShotType();
        Integer num2 = this.mSpecShotMode;
        str = TAG;
        str2 = "isCaptureWillCostHugeMemory: true >>> capture will trigger AINR ";
        Log.d(str, str2);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isDetectedHHT() {
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0054, code lost:
        if (r0.isAnyRequestBlocked() != false) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0062, code lost:
        if (r0.isCaptureBusy(true) != false) goto L_0x0056;
     */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0069  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isDoingAction() {
        boolean z;
        Handler handler;
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
            handler = this.mHandler;
            if (handler == null && handler.hasMessages(4097)) {
                return true;
            }
            if (!this.mPaused && !this.isZooming && !this.mMediaRecorderRecording && !isKeptBitmapTexture() && !this.mMultiSnapStatus && getCameraState() != 0 && !z && !isQueueFull() && !this.mWaitSaveFinish && !isInCountDown()) {
                z2 = false;
            }
            return z2;
        }
        Camera2Proxy camera2Proxy2 = this.mCamera2Device;
        if (camera2Proxy2 != null) {
        }
        z = false;
        handler = this.mHandler;
        if (handler == null) {
        }
        z2 = false;
        return z2;
        z = true;
        handler = this.mHandler;
        if (handler == null) {
        }
        z2 = false;
        return z2;
    }

    /* access modifiers changed from: protected */
    public boolean isFaceBeautyMode() {
        return false;
    }

    public boolean isFaceDetectStarted() {
        return this.mFaceDetectionStarted && !this.mMultiSnapStatus && !isDeparted();
    }

    public boolean isFirstCreateCapture() {
        return this.mFirstCreateCapture;
    }

    public boolean isFocusFrameAvailable() {
        return isFrameAvailable();
    }

    public boolean isGoogleLensAvailable() {
        return this.mIsGoogleLensAvailable;
    }

    public boolean isGyroStable() {
        return Util.isGyroscopeStable(this.curGyroscope, this.lastGyroscope);
    }

    public boolean isHdrSceneDetectionStarted() {
        return this.mHdrCheckEnabled;
    }

    public boolean isKeptBitmapTexture() {
        return this.mKeepBitmapTexture;
    }

    public boolean isLivePhotoStarted() {
        return this.mLiveShotEnabled;
    }

    public boolean isLockHDRChecker(String str) {
        MagneticSensorDetect magneticSensorDetect = this.mMagneticSensorDetect;
        if (magneticSensorDetect != null) {
            return magneticSensorDetect.isLockHDRChecker(str);
        }
        return false;
    }

    public boolean isLongPressedRecording() {
        return this.mMediaRecorderRecording;
    }

    public boolean isMatchCurrentHdrMode(int i) {
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities == null || !cameraCapabilities.isSupportHdrMode()) {
            return true;
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        return camera2Proxy != null && camera2Proxy.getHDRMode() == i;
    }

    public boolean isMeteringAreaOnly() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        boolean z = false;
        if (camera2Proxy == null) {
            return false;
        }
        int focusMode = camera2Proxy.getFocusMode();
        if ((!this.mFocusAreaSupported && this.mMeteringAreaSupported && !this.mFocusOrAELockSupported) || 5 == focusMode || focusMode == 0) {
            z = true;
        }
        return z;
    }

    public boolean isNeedMute() {
        return CameraSettings.isLiveShotOn();
    }

    public boolean isNeedNearRangeTip() {
        return !this.mMediaRecorderRecording && !this.mIsShutterLongClickRecording && !DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting();
    }

    /* access modifiers changed from: protected */
    public boolean isParallelSessionEnable() {
        if (CameraSettings.isUltraPixelRawOn() || !CameraSettings.isCameraParallelProcessEnable()) {
            return false;
        }
        if (getModuleIndex() == 173 && DataRepository.dataItemGlobal().isOnSuperNightHalfAlgoUp()) {
            return true;
        }
        if (getModuleIndex() == 173 && !DataRepository.dataItemGlobal().isOnSuperNightAlgoUpMode()) {
            return false;
        }
        if (getModuleIndex() == 173 && getRawCallbackType(false) == 8) {
            return true;
        }
        boolean isSwitchOn = DataRepository.dataItemConfig().getComponentConfigRaw().isSwitchOn(getModuleIndex());
        if (getModuleIndex() == 167 && (!C0122O00000o.instance().OO0Ooo() || isSwitchOn)) {
            return false;
        }
        if (getModuleIndex() == 175 && C0122O00000o.instance().OO0Ooo0()) {
            return false;
        }
        if (isStandaloneMacroCamera() && !C0122O00000o.instance().OO0o0OO()) {
            return false;
        }
        if (!isUltraWideBackCamera() || C0122O00000o.instance().OOOoO00()) {
            return (!this.mIsImageCaptureIntent || C0122O00000o.instance().OO0O0Oo()) && !DataRepository.dataItemGlobal().isForceMainBackCamera();
        }
        return false;
    }

    public boolean isRecording() {
        return this.mMediaRecorderRecording || DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting();
    }

    /* access modifiers changed from: protected */
    public boolean isRepeatingRequestInProgress() {
        return this.mMultiSnapStatus && 3 == getCameraState();
    }

    public boolean isSelectingCapturedResult() {
        boolean z = false;
        if (!this.mIsImageCaptureIntent) {
            return false;
        }
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null && baseDelegate.getActiveFragment(R.id.bottom_action) == 4083) {
            z = true;
        }
        return z;
    }

    public boolean isShot2GalleryOrEnableParallel() {
        return this.mEnableShot2Gallery || this.mEnableParallelSession || this.mSupportAnchorFrameAsThumbnail;
    }

    public boolean isShowAeAfLockIndicator() {
        return this.m3ALocked;
    }

    public boolean isShowBacklightTip() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            return topAlert.isShowBacklightSelector();
        }
        return false;
    }

    public boolean isShowCaptureButton() {
        return !this.mMutexModePicker.isBurstShoot() && isSupportFocusShoot();
    }

    public boolean isStartCountCapture() {
        return this.mIsStartCount;
    }

    public boolean isSupportFocusShoot() {
        return DataRepository.dataItemGlobal().isGlobalSwitchOn("pref_camera_focus_shoot_key") && !this.mMediaRecorderRecording;
    }

    /* access modifiers changed from: protected */
    public boolean isSupportSceneMode() {
        return false;
    }

    public boolean isSupportSuperNight() {
        if (!C0122O00000o.instance().OOOOo() || (C0124O00000oO.Oo0000o() && !Util.sSuperNightDefaultModeEnable)) {
            return false;
        }
        if (163 == getModuleIndex() || 165 == getModuleIndex()) {
            return (isBackCamera() || C0122O00000o.instance().OOo0Oo()) && 1.0f == CameraSettings.getRetainZoom(this.mModuleIndex) && !this.mIsMacroModeEnable && !isRepeatingRequestInProgress() && !this.mIsNearRangeModeUITip && isSuperNightSeEnable();
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001e  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isUnInterruptable() {
        String str;
        this.mUnInterruptableReason = null;
        if (isKeptBitmapTexture()) {
            str = "bitmap cover";
        } else {
            if (getCameraState() == 3) {
                str = "snapshot";
            }
            return this.mUnInterruptableReason == null;
        }
        this.mUnInterruptableReason = str;
        if (this.mUnInterruptableReason == null) {
        }
    }

    public boolean isUseFaceInfo() {
        return this.mIsGenderAgeOn || this.mIsMagicMirrorOn;
    }

    public boolean isZoomEnabled() {
        boolean z = CameraSettings.isTimerBurstEnable() && TimerBurstController.isSupportTimerBurst(this.mModuleIndex) && DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting();
        if (getCameraState() == 3 || CameraSettings.isPortraitModeBackOn() || isFrontCamera() || z) {
            return false;
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy == null || camera2Proxy.isCaptureBusy(true)) {
            return false;
        }
        return (!CameraSettings.isUltraPixelOn() || C0122O00000o.instance().OOOOO00()) && this.mModuleIndex != 182 && isFrameAvailable() && !this.mMediaRecorderRecording;
    }

    /* access modifiers changed from: protected */
    public boolean isZslPreferred() {
        boolean z = false;
        if (!C0122O00000o.instance().OOoOoOO()) {
            return false;
        }
        boolean isMTKPlatform = C0124O00000oO.isMTKPlatform();
        int i = this.mModuleIndex;
        if (!isMTKPlatform ? i != 167 : i == 163 || i == 165 || i == 186 || i == 182 || i == 171 || (i == 175 && C0122O00000o.instance().OOOO00o())) {
            z = true;
        }
        return z;
    }

    public boolean multiCapture() {
        PictureCallback pictureCallback;
        int i;
        Camera2Proxy camera2Proxy;
        if (isDoingAction() || !this.mPendingMultiCapture) {
            return false;
        }
        this.mPendingMultiCapture = false;
        this.mActivity.getScreenHint().updateHint();
        if (Storage.isLowStorageAtLastPoint()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Not enough space or storage not ready. remaining=");
            sb.append(Storage.getLeftSpace());
            Log.i(str, sb.toString());
            return false;
        } else if (this.mActivity.getImageSaver().isBusy()) {
            Log.d(TAG, "ImageSaver is busy, wait for a moment!");
            RotateTextToast.getInstance(this.mActivity).show(R.string.toast_saving, 0);
            return false;
        } else if (this.mIsMoonMode) {
            return false;
        } else {
            ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).closeMutexElement(SupportedConfigFactory.CLOSE_BY_BURST_SHOOT, 193, 194, 196, 239, 201, 206);
            BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
            if (backStack != null) {
                backStack.handleBackStackFromLongPressShutter();
            }
            prepareMultiCapture();
            Observable.create(new ObservableOnSubscribe() {
                public void subscribe(ObservableEmitter observableEmitter) {
                    Camera2Module.this.mBurstEmitter = observableEmitter;
                }
            }).observeOn(AndroidSchedulers.mainThread()).map(new Function() {
                public Integer apply(Integer num) {
                    SnapShotIndicator snapShotIndicator = (SnapShotIndicator) ModeCoordinatorImpl.getInstance().getAttachProtocol(184);
                    if (snapShotIndicator != null) {
                        if (snapShotIndicator instanceof FragmentTopConfig) {
                            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                            if (topAlert != null) {
                                topAlert.hideAlert();
                            }
                        }
                        snapShotIndicator.setSnapNumVisible(true, false);
                        snapShotIndicator.setSnapNumValue(num.intValue());
                    }
                    return num;
                }
            }).subscribe((Observer) new Observer() {
                public void onComplete() {
                    SnapShotIndicator snapShotIndicator = (SnapShotIndicator) ModeCoordinatorImpl.getInstance().getAttachProtocol(184);
                    if (snapShotIndicator != null) {
                        snapShotIndicator.setSnapNumVisible(false, true);
                        if (snapShotIndicator instanceof FragmentTopConfig) {
                            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                            if (topAlert != null) {
                                AndroidSchedulers.mainThread().scheduleDirect(new O0000O0o(topAlert), 500, TimeUnit.MILLISECONDS);
                            }
                        }
                    }
                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                    if (configChanges != null) {
                        configChanges.restoreAllMutexElement(SupportedConfigFactory.CLOSE_BY_BURST_SHOOT);
                    }
                }

                public void onError(Throwable th) {
                }

                public void onNext(Integer num) {
                }

                public void onSubscribe(Disposable disposable) {
                    Camera2Module.this.mBurstStartTime = System.currentTimeMillis();
                    Camera2Module.this.mBurstDisposable = disposable;
                }
            });
            this.mBurstNextDelayTime = 0;
            if (isParallelSessionEnable()) {
                this.mCamera2Device.setShotType(9);
                camera2Proxy = this.mCamera2Device;
                i = this.mTotalJpegCallbackNum;
                pictureCallback = new JpegRepeatingCaptureCallback(this);
            } else {
                ScenarioTrackUtil.trackScenarioAbort(ScenarioTrackUtil.sShotToGalleryTimeScenario, String.valueOf(this.mCaptureStartTime));
                ScenarioTrackUtil.trackScenarioAbort(ScenarioTrackUtil.sShotToViewTimeScenario, String.valueOf(this.mCaptureStartTime));
                this.mCamera2Device.setShotType(3);
                this.mCamera2Device.setAWBLock(true);
                camera2Proxy = this.mCamera2Device;
                i = this.mTotalJpegCallbackNum;
                pictureCallback = new JpegQuickPictureCallback(LocationManager.instance().getCurrentLocation());
            }
            camera2Proxy.captureBurstPictures(i, pictureCallback, this.mActivity.getImageSaver());
            return true;
        }
    }

    public void notifyAfterFirstFrameArrived() {
        String str = TAG;
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

    public void notifyFocusAreaUpdate() {
        updatePreferenceTrampoline(3);
    }

    public boolean onBackPressed() {
        if (!isFrameAvailable()) {
            return false;
        }
        this.mIsStartCount = false;
        tryRemoveCountDownMessage();
        if (this.mMultiSnapStatus) {
            onShutterButtonLongClickCancel(false);
            return true;
        } else if (getCameraState() != 3 || System.currentTimeMillis() - this.mLastCaptureTime >= CAPTURE_DURATION_THRESHOLD) {
            return super.onBackPressed();
        } else {
            return true;
        }
    }

    public void onBeautyBodySlimCountChange(final boolean z) {
        this.mHandler.post(new Runnable() {
            public void run() {
                int i;
                long j;
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    if (z) {
                        i = 0;
                        j = FunctionParseBeautyBodySlimCount.TIP_TIME;
                    } else {
                        i = 8;
                        j = 0;
                    }
                    topAlert.alertAiDetectTipHint(i, R.string.beauty_body_slim_count_tip, j);
                }
            }
        });
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
                    if (isBlockSnap() || isCaptureIntent()) {
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
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x008e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCameraOpened() {
        EffectController effectController;
        super.onCameraOpened();
        int i = 0;
        if (isBackCamera()) {
            int i2 = this.mModuleIndex;
            if (i2 == 163 || i2 == 165 || i2 == 205) {
                effectController = EffectController.getInstance();
                CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
                if (cameraCapabilities != null) {
                    i = cameraCapabilities.getAiColorCorrectionVersion();
                }
                effectController.setAiColorCorrectionVersion(i);
                initializeFocusManager();
                initZoomMapControllerIfNeeded();
                updatePreferenceTrampoline(UpdateConstant.CAMERA_TYPES_INIT);
                if (this.mEnableParallelSession) {
                    if (isPortraitMode()) {
                        Util.saveCameraCalibrationToFile(this.mCameraCapabilities.getCameraCalibrationData(), getCalibrationDataFileName(this.mActualCameraId));
                    }
                    customizeReprocessor();
                }
                if (this.mCameraCapabilities.isSatFusionShotSupported() && this.mCameraCapabilities.getCameraId() == Camera2DataContainer.getInstance().getSATCameraId()) {
                    Util.saveCameraCalibrationToFile(this.mCameraCapabilities.getSatFusionCalibrationDataArray());
                }
                if (!isKeptBitmapTexture()) {
                    startPreview();
                }
                initMetaParser();
                updateAutoHibernation();
                if (C0122O00000o.instance().OO0oO0o()) {
                    initAiSceneParser();
                }
                initHistogramEmitter();
                this.mOnResumeTime = SystemClock.uptimeMillis();
                this.mHandler.sendEmptyMessage(4);
                this.mHandler.sendEmptyMessage(31);
            }
        }
        effectController = EffectController.getInstance();
        effectController.setAiColorCorrectionVersion(i);
        initializeFocusManager();
        initZoomMapControllerIfNeeded();
        updatePreferenceTrampoline(UpdateConstant.CAMERA_TYPES_INIT);
        if (this.mEnableParallelSession) {
        }
        Util.saveCameraCalibrationToFile(this.mCameraCapabilities.getSatFusionCalibrationDataArray());
        if (!isKeptBitmapTexture()) {
        }
        initMetaParser();
        updateAutoHibernation();
        if (C0122O00000o.instance().OO0oO0o()) {
        }
        initHistogramEmitter();
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
        if (DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(this.mModuleIndex)) {
            Log.d(TAG, "onCaptureCompleted: playCameraSound");
            animateCapture();
            playCameraSound(0);
        }
        checkMoreFrameCaptureLockAFAE(false);
        handleLLSResultInCaptureMode();
        if (this.mCameraCapabilities.isSupportAIIE() && this.mAiSceneEnabled) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null && !camera2Proxy.getCameraConfigs().isAIIEPreviewEnabled()) {
                this.mCamera2Device.setAIIEPreviewEnable(true);
                resumePreviewInWorkThread();
            }
        }
        if (DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(this.mModuleIndex) && !DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting()) {
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                Handler handler = this.mHandler;
                Objects.requireNonNull(recordState);
                handler.post(new C0376O000o0OO(recordState));
            }
        } else if (DataRepository.dataItemRunning().isSuperNightCaptureWithKnownDuration()) {
            PublishSubject publishSubject = this.mSuperNightEventEmitter;
            if (publishSubject != null) {
                publishSubject.onNext(Integer.valueOf(2));
            }
        }
    }

    public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
        if (!isDeviceAlive()) {
            Log.d(TAG, "onCaptureProgress but departed");
            return;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCaptureProgressed quick ");
        sb.append(z);
        sb.append(" anchor ");
        sb.append(z2);
        sb.append(" doanchor ");
        sb.append(z3);
        sb.append(" anchorpixel ");
        sb.append(z4);
        Log.d(str, sb.toString());
        onShutter(z, z2, z3, z4);
    }

    public void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCaptureShutter: cameraState = ");
        sb.append(getCameraState());
        sb.append(", isParallel = ");
        sb.append(this.mEnableParallelSession);
        Log.d(str, sb.toString());
        onShutter(z, z2, z3, z4);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x024f, code lost:
        if (com.android.camera.Util.isStringValueContained((java.lang.Object) com.android.camera.aiwatermark.util.WatermarkConstant.ITEM_TAG, r0.mCamera2Device.getCameraConfigs().getWaterMarkAppliedList()) != false) goto L_0x0251;
     */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0260  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0263  */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x026c  */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0288  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x0291  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x02c3  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x02ee  */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x0392  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0395  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x03a8  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x03bb  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x03cc  */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x03f8  */
    /* JADX WARNING: Removed duplicated region for block: B:167:0x042b  */
    /* JADX WARNING: Removed duplicated region for block: B:170:0x0434  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x011f  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0135  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0166  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0169  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x01fa  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x023e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, @NonNull CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
        ArrayList arrayList;
        Size sizeObject;
        boolean z5;
        boolean z6;
        PreviewImage previewImage;
        float[] fArr;
        int i;
        ParallelTaskData parallelTaskData2 = parallelTaskData;
        CameraSize cameraSize2 = cameraSize;
        boolean z7 = z;
        boolean z8 = z2;
        boolean z9 = z3;
        boolean z10 = z4;
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.updateFlashStateTimeLock();
        }
        if (isDeparted()) {
            Log.w(TAG, "onCaptureStart: departed");
            if (C0122O00000o.instance().OOo0oOO()) {
                parallelTaskData2.setRequireTuningData(true);
            }
            parallelTaskData2.setAbandoned(true);
            return parallelTaskData2;
        }
        parallelTaskData2.setServiceStatusListener(this.mServiceStatusListener);
        int parallelType = parallelTaskData.getParallelType();
        boolean z11 = CameraSettings.isLiveShotOn() && isLiveShotAvailable(parallelType);
        if (z11) {
            startLiveShotAnimation();
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCapturestart quickshotanimation ");
        sb.append(z7);
        sb.append(" anchorFrame ");
        sb.append(z8);
        sb.append(" doAnchor ");
        sb.append(z9);
        sb.append(" doAnchorPixel ");
        sb.append(z10);
        Log.d(str, sb.toString());
        if (!z7 || (CameraSettings.isGroupShotOn() && !this.mEnableParallelSession)) {
            if (!CameraSettings.isSupportedZslShutter()) {
                updateEnablePreviewThumbnail(z7);
                if (this.mEnabledPreviewThumbnail) {
                    CameraSettings.setPlayToneOnCaptureStart(false);
                }
            }
            if (CameraSettings.isUltraPixelOn() && this.mEnabledPreviewThumbnail) {
                CameraSettings.setPlayToneOnCaptureStart(false);
            } else if (!this.mEnabledPreviewThumbnail && ((!C0122O00000o.instance().OO0OoOO() && !DataRepository.dataItemRunning().isSuperNightCaptureWithKnownDuration()) || parallelTaskData.getBurstNum() <= 1)) {
                onShutter(z7, z8, z9, z10);
                CameraSettings.setPlayToneOnCaptureStart(true);
            }
            parallelTaskData2.setAddToProcessorListener(new OnParallelTaskDataAddToProcessorListener() {
                public void OnParallelTaskDataAddToProcessor() {
                    if (!Camera2Module.this.isKeptBitmapTexture() && !Camera2Module.this.mMultiSnapStatus && Camera2Module.this.mBlockQuickShot && !CameraSettings.isGroupShotOn() && Camera2Module.this.mFixedShot2ShotTime == 0) {
                        Camera2Module.this.resetStatusToIdle();
                    }
                }
            });
        }
        if (CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()) {
            List faceWaterMarkInfos = ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).getFaceWaterMarkInfos();
            if (faceWaterMarkInfos != null && !faceWaterMarkInfos.isEmpty()) {
                arrayList = new ArrayList(faceWaterMarkInfos);
                if ((isIn3OrMoreSatMode() || isInMultiSurfaceSatMode()) && (!cameraSize2.equals(this.mPictureSize) || C0124O00000oO.isMTKPlatform())) {
                    if (C0122O00000o.instance().OOoO0oO()) {
                        this.mPictureSize = cameraSize2;
                        updateOutputSize(cameraSize2);
                    } else {
                        CameraSize cameraSize3 = this.mPictureSize;
                        this.mPictureSize = cameraSize2;
                        updateOutputSize(cameraSize3);
                    }
                }
                CameraSize cameraSize4 = this.mOutputPictureSize;
                sizeObject = cameraSize4 != null ? cameraSize.toSizeObject() : cameraSize4.toSizeObject();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onCaptureStart: outputSize = ");
                sb2.append(sizeObject);
                Log.k(3, str2, sb2.toString());
                int pictureFormatSuitableForShot = getPictureFormatSuitableForShot(parallelType);
                boolean isHeicImageFormat = CompatibilityUtils.isHeicImageFormat(pictureFormatSuitableForShot);
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("onCaptureStart: outputFormat = ");
                sb3.append(!isHeicImageFormat ? "HEIC" : "JPEG");
                Log.k(3, str3, sb3.toString());
                if (isHeicImageFormat && this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()) {
                    int i2 = this.mJpegRotation;
                    if (i2 == 90 || i2 == 270) {
                        Size size = new Size(sizeObject.getHeight(), sizeObject.getWidth());
                        String str4 = TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("onCaptureStart: switched outputSize: ");
                        sb4.append(size);
                        Log.d(str4, sb4.toString());
                        sizeObject = size;
                    }
                }
                int clampQuality = clampQuality(CameraSettings.getEncodingQuality(false).toInteger(isHeicImageFormat));
                String str5 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("onCaptureStart: outputQuality = ");
                sb5.append(clampQuality);
                Log.k(3, str5, sb5.toString());
                CameraCharacteristics cameraCharacteristics = this.mCameraCapabilities.getCameraCharacteristics();
                this.mFocalLengths = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
                this.mApertures = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);
                Builder builder = new Builder(this.mPreviewSize.toSizeObject(), cameraSize.toSizeObject(), sizeObject, pictureFormatSuitableForShot);
                if (parallelType == 1) {
                    CameraSize cameraSize5 = this.mSensorRawImageSize;
                    if (cameraSize5 != null && this.mRawCallbackType == 1 && this.mModuleIndex == 167) {
                        builder.setRawSize(cameraSize5.width, cameraSize5.height);
                    }
                }
                if (C0122O00000o.instance().OOoO000() || (C0122O00000o.instance().OOo0ooo() && this.mModuleIndex == 175)) {
                    if (!Util.isStringValueContained((Object) "device", this.mCamera2Device.getCameraConfigs().getWaterMarkAppliedList())) {
                    }
                    z5 = true;
                    Location location = this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity) ? this.mLocation : null;
                    if (CameraSettings.isDocumentMode2On(this.mModuleIndex)) {
                        Decoder decoder = PreviewDecodeManager.getInstance().getDecoder(3);
                        if (decoder != null) {
                            Pair cachePreview = ((DocumentDecoder) decoder).getCachePreview();
                            previewImage = (PreviewImage) cachePreview.first;
                            fArr = (float[]) cachePreview.second;
                            z6 = true;
                            boolean z12 = !CameraSettings.isDualCameraWaterMarkOpen() && !isDisableWatermark();
                            Builder filterId = builder.setHasDualWaterMark(z12).setVendorWaterMark(z5).setMirror(isFrontMirror()).setLightingPattern(CameraSettings.getPortraitLightingPattern()).setFilterId(EffectController.getInstance().getEffectForSaving(false));
                            i = this.mOrientation;
                            if (-1 == i) {
                                i = 0;
                            }
                            Builder location2 = filterId.setOrientation(i).setJpegRotation(this.mJpegRotation).setShootRotation(this.mShootRotation).setShootOrientation(this.mShootOrientation).setSupportZeroDegreeOrientationImage(this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()).setLocation(location);
                            String timeWatermark = (CameraSettings.isTimeWaterMarkOpen() || isDisableWatermark()) ? null : Util.getTimeWatermark(this.mActivity);
                            parallelTaskData2.fillParameter(location2.setTimeWaterMarkString(timeWatermark).setFaceWaterMarkList(arrayList).setAgeGenderAndMagicMirrorWater(CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()).setFrontCamera(isFrontCamera()).setBokehFrontCamera(isPictureUseDualFrontCamera()).setAlgorithmName(this.mAlgorithmName).setPictureInfo(getPictureInfo()).setSuffix(getSuffix()).setTiltShiftMode(getTiltShiftMode()).setSaveGroupshotPrimitive(CameraSettings.isSaveGroushotPrimitiveOn()).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setJpegQuality(clampQuality).setPrefix(getPrefix()).setMoonMode(this.mIsMoonMode).setMiMovieOpen(CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex)).setMajorAIWatermark(this.mWatermarkItem).setPortraitLightingVersion(this.mCameraCapabilities.getPortraitLightingVersion()).setDocumentShot(z6).setDocumentPreview(previewImage).setDocumentPoints(fArr).setSensorOrientation(this.mCameraCapabilities.getSensorOrientation()).setCameraPreferredMode((!CameraSettings.isHighQualityPreferred() ? CameraPreferredMode.HIGH_QUALITY_MODE : CameraPreferredMode.PERFORMANCE_MODE).ordinal()).build());
                            boolean z13 = z7 && !this.mEnabledPreviewThumbnail;
                            parallelTaskData2.setNeedThumbnail(z13);
                            parallelTaskData2.setCurrentModuleIndex(this.mModuleIndex);
                            CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
                            boolean z14 = cameraCapabilities == null && cameraCapabilities.isAdaptiveSnapshotSizeInSatModeSupported();
                            parallelTaskData2.setAdaptiveSnapshotSize(z14);
                            parallelTaskData2.setLiveShotTask(false);
                            if (z11) {
                                Camera camera = this.mActivity;
                                if (camera != null) {
                                    ImageSaver imageSaver = camera.getImageSaver();
                                    if (imageSaver != null) {
                                        synchronized (this.mCircularMediaRecorderStateLock) {
                                            if (this.mCircularMediaRecorder != null) {
                                                parallelTaskData2.setLiveShotTask(true);
                                                this.mCircularMediaRecorder.snapshot(this.mOrientationCompensation, imageSaver, parallelTaskData2, this.mNormalFilterId);
                                            }
                                        }
                                    }
                                }
                            }
                            if (C0122O00000o.instance().OOo0oOO()) {
                                parallelTaskData2.setRequireTuningData(true);
                            }
                            String str6 = TAG;
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("onCaptureStart: isParallel = ");
                            sb6.append(this.mEnableParallelSession);
                            sb6.append(", shotType = ");
                            sb6.append(parallelTaskData.getParallelType());
                            sb6.append(", isLiveShot = ");
                            sb6.append(z11);
                            Log.k(3, str6, sb6.toString());
                            if (this.mEnableParallelSession) {
                                beginParallelProcess(parallelTaskData2, true);
                            }
                            if (CameraSettings.isHandGestureOpen()) {
                                Log.d(TAG, "send msg: reset hand gesture");
                                this.mHandler.removeMessages(57);
                                this.mHandler.sendEmptyMessageDelayed(57, 0);
                            }
                            return parallelTaskData2;
                        }
                        z6 = true;
                    } else {
                        z6 = false;
                    }
                    fArr = null;
                    previewImage = null;
                    if (!CameraSettings.isDualCameraWaterMarkOpen()) {
                    }
                    Builder filterId2 = builder.setHasDualWaterMark(z12).setVendorWaterMark(z5).setMirror(isFrontMirror()).setLightingPattern(CameraSettings.getPortraitLightingPattern()).setFilterId(EffectController.getInstance().getEffectForSaving(false));
                    i = this.mOrientation;
                    if (-1 == i) {
                    }
                    Builder location22 = filterId2.setOrientation(i).setJpegRotation(this.mJpegRotation).setShootRotation(this.mShootRotation).setShootOrientation(this.mShootOrientation).setSupportZeroDegreeOrientationImage(this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()).setLocation(location);
                    if (CameraSettings.isTimeWaterMarkOpen()) {
                    }
                    parallelTaskData2.fillParameter(location22.setTimeWaterMarkString(timeWatermark).setFaceWaterMarkList(arrayList).setAgeGenderAndMagicMirrorWater(CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()).setFrontCamera(isFrontCamera()).setBokehFrontCamera(isPictureUseDualFrontCamera()).setAlgorithmName(this.mAlgorithmName).setPictureInfo(getPictureInfo()).setSuffix(getSuffix()).setTiltShiftMode(getTiltShiftMode()).setSaveGroupshotPrimitive(CameraSettings.isSaveGroushotPrimitiveOn()).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setJpegQuality(clampQuality).setPrefix(getPrefix()).setMoonMode(this.mIsMoonMode).setMiMovieOpen(CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex)).setMajorAIWatermark(this.mWatermarkItem).setPortraitLightingVersion(this.mCameraCapabilities.getPortraitLightingVersion()).setDocumentShot(z6).setDocumentPreview(previewImage).setDocumentPoints(fArr).setSensorOrientation(this.mCameraCapabilities.getSensorOrientation()).setCameraPreferredMode((!CameraSettings.isHighQualityPreferred() ? CameraPreferredMode.HIGH_QUALITY_MODE : CameraPreferredMode.PERFORMANCE_MODE).ordinal()).build());
                    if (z7) {
                    }
                    parallelTaskData2.setNeedThumbnail(z13);
                    parallelTaskData2.setCurrentModuleIndex(this.mModuleIndex);
                    CameraCapabilities cameraCapabilities2 = this.mCameraCapabilities;
                    if (cameraCapabilities2 == null) {
                    }
                    parallelTaskData2.setAdaptiveSnapshotSize(z14);
                    parallelTaskData2.setLiveShotTask(false);
                    if (z11) {
                    }
                    if (C0122O00000o.instance().OOo0oOO()) {
                    }
                    String str62 = TAG;
                    StringBuilder sb62 = new StringBuilder();
                    sb62.append("onCaptureStart: isParallel = ");
                    sb62.append(this.mEnableParallelSession);
                    sb62.append(", shotType = ");
                    sb62.append(parallelTaskData.getParallelType());
                    sb62.append(", isLiveShot = ");
                    sb62.append(z11);
                    Log.k(3, str62, sb62.toString());
                    if (this.mEnableParallelSession) {
                    }
                    if (CameraSettings.isHandGestureOpen()) {
                    }
                    return parallelTaskData2;
                }
                z5 = false;
                if (this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity)) {
                }
                if (CameraSettings.isDocumentMode2On(this.mModuleIndex)) {
                }
                fArr = null;
                previewImage = null;
                if (!CameraSettings.isDualCameraWaterMarkOpen()) {
                }
                Builder filterId22 = builder.setHasDualWaterMark(z12).setVendorWaterMark(z5).setMirror(isFrontMirror()).setLightingPattern(CameraSettings.getPortraitLightingPattern()).setFilterId(EffectController.getInstance().getEffectForSaving(false));
                i = this.mOrientation;
                if (-1 == i) {
                }
                Builder location222 = filterId22.setOrientation(i).setJpegRotation(this.mJpegRotation).setShootRotation(this.mShootRotation).setShootOrientation(this.mShootOrientation).setSupportZeroDegreeOrientationImage(this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()).setLocation(location);
                if (CameraSettings.isTimeWaterMarkOpen()) {
                }
                parallelTaskData2.fillParameter(location222.setTimeWaterMarkString(timeWatermark).setFaceWaterMarkList(arrayList).setAgeGenderAndMagicMirrorWater(CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()).setFrontCamera(isFrontCamera()).setBokehFrontCamera(isPictureUseDualFrontCamera()).setAlgorithmName(this.mAlgorithmName).setPictureInfo(getPictureInfo()).setSuffix(getSuffix()).setTiltShiftMode(getTiltShiftMode()).setSaveGroupshotPrimitive(CameraSettings.isSaveGroushotPrimitiveOn()).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setJpegQuality(clampQuality).setPrefix(getPrefix()).setMoonMode(this.mIsMoonMode).setMiMovieOpen(CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex)).setMajorAIWatermark(this.mWatermarkItem).setPortraitLightingVersion(this.mCameraCapabilities.getPortraitLightingVersion()).setDocumentShot(z6).setDocumentPreview(previewImage).setDocumentPoints(fArr).setSensorOrientation(this.mCameraCapabilities.getSensorOrientation()).setCameraPreferredMode((!CameraSettings.isHighQualityPreferred() ? CameraPreferredMode.HIGH_QUALITY_MODE : CameraPreferredMode.PERFORMANCE_MODE).ordinal()).build());
                if (z7) {
                }
                parallelTaskData2.setNeedThumbnail(z13);
                parallelTaskData2.setCurrentModuleIndex(this.mModuleIndex);
                CameraCapabilities cameraCapabilities22 = this.mCameraCapabilities;
                if (cameraCapabilities22 == null) {
                }
                parallelTaskData2.setAdaptiveSnapshotSize(z14);
                parallelTaskData2.setLiveShotTask(false);
                if (z11) {
                }
                if (C0122O00000o.instance().OOo0oOO()) {
                }
                String str622 = TAG;
                StringBuilder sb622 = new StringBuilder();
                sb622.append("onCaptureStart: isParallel = ");
                sb622.append(this.mEnableParallelSession);
                sb622.append(", shotType = ");
                sb622.append(parallelTaskData.getParallelType());
                sb622.append(", isLiveShot = ");
                sb622.append(z11);
                Log.k(3, str622, sb622.toString());
                if (this.mEnableParallelSession) {
                }
                if (CameraSettings.isHandGestureOpen()) {
                }
                return parallelTaskData2;
            }
        }
        arrayList = null;
        if (C0122O00000o.instance().OOoO0oO()) {
        }
        CameraSize cameraSize42 = this.mOutputPictureSize;
        if (cameraSize42 != null) {
        }
        String str22 = TAG;
        StringBuilder sb22 = new StringBuilder();
        sb22.append("onCaptureStart: outputSize = ");
        sb22.append(sizeObject);
        Log.k(3, str22, sb22.toString());
        int pictureFormatSuitableForShot2 = getPictureFormatSuitableForShot(parallelType);
        boolean isHeicImageFormat2 = CompatibilityUtils.isHeicImageFormat(pictureFormatSuitableForShot2);
        String str32 = TAG;
        StringBuilder sb32 = new StringBuilder();
        sb32.append("onCaptureStart: outputFormat = ");
        sb32.append(!isHeicImageFormat2 ? "HEIC" : "JPEG");
        Log.k(3, str32, sb32.toString());
        int i22 = this.mJpegRotation;
        Size size2 = new Size(sizeObject.getHeight(), sizeObject.getWidth());
        String str42 = TAG;
        StringBuilder sb42 = new StringBuilder();
        sb42.append("onCaptureStart: switched outputSize: ");
        sb42.append(size2);
        Log.d(str42, sb42.toString());
        sizeObject = size2;
        int clampQuality2 = clampQuality(CameraSettings.getEncodingQuality(false).toInteger(isHeicImageFormat2));
        String str52 = TAG;
        StringBuilder sb52 = new StringBuilder();
        sb52.append("onCaptureStart: outputQuality = ");
        sb52.append(clampQuality2);
        Log.k(3, str52, sb52.toString());
        CameraCharacteristics cameraCharacteristics2 = this.mCameraCapabilities.getCameraCharacteristics();
        this.mFocalLengths = (float[]) cameraCharacteristics2.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
        this.mApertures = (float[]) cameraCharacteristics2.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);
        Builder builder2 = new Builder(this.mPreviewSize.toSizeObject(), cameraSize.toSizeObject(), sizeObject, pictureFormatSuitableForShot2);
        if (parallelType == 1) {
        }
        if (!Util.isStringValueContained((Object) "device", this.mCamera2Device.getCameraConfigs().getWaterMarkAppliedList())) {
        }
        z5 = true;
        if (this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity)) {
        }
        if (CameraSettings.isDocumentMode2On(this.mModuleIndex)) {
        }
        fArr = null;
        previewImage = null;
        if (!CameraSettings.isDualCameraWaterMarkOpen()) {
        }
        Builder filterId222 = builder2.setHasDualWaterMark(z12).setVendorWaterMark(z5).setMirror(isFrontMirror()).setLightingPattern(CameraSettings.getPortraitLightingPattern()).setFilterId(EffectController.getInstance().getEffectForSaving(false));
        i = this.mOrientation;
        if (-1 == i) {
        }
        Builder location2222 = filterId222.setOrientation(i).setJpegRotation(this.mJpegRotation).setShootRotation(this.mShootRotation).setShootOrientation(this.mShootOrientation).setSupportZeroDegreeOrientationImage(this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()).setLocation(location);
        if (CameraSettings.isTimeWaterMarkOpen()) {
        }
        parallelTaskData2.fillParameter(location2222.setTimeWaterMarkString(timeWatermark).setFaceWaterMarkList(arrayList).setAgeGenderAndMagicMirrorWater(CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()).setFrontCamera(isFrontCamera()).setBokehFrontCamera(isPictureUseDualFrontCamera()).setAlgorithmName(this.mAlgorithmName).setPictureInfo(getPictureInfo()).setSuffix(getSuffix()).setTiltShiftMode(getTiltShiftMode()).setSaveGroupshotPrimitive(CameraSettings.isSaveGroushotPrimitiveOn()).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setJpegQuality(clampQuality2).setPrefix(getPrefix()).setMoonMode(this.mIsMoonMode).setMiMovieOpen(CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex)).setMajorAIWatermark(this.mWatermarkItem).setPortraitLightingVersion(this.mCameraCapabilities.getPortraitLightingVersion()).setDocumentShot(z6).setDocumentPreview(previewImage).setDocumentPoints(fArr).setSensorOrientation(this.mCameraCapabilities.getSensorOrientation()).setCameraPreferredMode((!CameraSettings.isHighQualityPreferred() ? CameraPreferredMode.HIGH_QUALITY_MODE : CameraPreferredMode.PERFORMANCE_MODE).ordinal()).build());
        if (z7) {
        }
        parallelTaskData2.setNeedThumbnail(z13);
        parallelTaskData2.setCurrentModuleIndex(this.mModuleIndex);
        CameraCapabilities cameraCapabilities222 = this.mCameraCapabilities;
        if (cameraCapabilities222 == null) {
        }
        parallelTaskData2.setAdaptiveSnapshotSize(z14);
        parallelTaskData2.setLiveShotTask(false);
        if (z11) {
        }
        if (C0122O00000o.instance().OOo0oOO()) {
        }
        String str6222 = TAG;
        StringBuilder sb6222 = new StringBuilder();
        sb6222.append("onCaptureStart: isParallel = ");
        sb6222.append(this.mEnableParallelSession);
        sb6222.append(", shotType = ");
        sb6222.append(parallelTaskData.getParallelType());
        sb6222.append(", isLiveShot = ");
        sb6222.append(z11);
        Log.k(3, str6222, sb6222.toString());
        if (this.mEnableParallelSession) {
        }
        if (CameraSettings.isHandGestureOpen()) {
        }
        return parallelTaskData2;
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        parseIntent();
        this.mHandler = new MainHandler(this, this.mActivity.getMainLooper());
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        boolean z = false;
        boolean z2 = 163 == getModuleIndex() && !this.mIsImageCaptureIntent && isBackCamera() && CameraSettings.isAvailableGoogleLens();
        this.mIsGoogleLensAvailable = z2;
        this.mSupportAnchorFrameAsThumbnail = supportAnchorFrameAsThumbnail();
        if (C0122O00000o.instance().OOOOOOO() && !CameraSettings.isCameraParallelProcessEnable()) {
            z = true;
        }
        this.mSupportShotBoost = z;
        onCameraOpened();
        this.mFirstCreateCapture = true;
        this.mHdrTrigger = new HdrTrigger();
    }

    public void onDestroy() {
        super.onDestroy();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessage(45);
        }
        ZoomMapController zoomMapController = this.mZoomMapController;
        if (zoomMapController != null) {
            zoomMapController.onModuleDestroy();
        }
    }

    public void onExtraMenuVisibilityChange(boolean z) {
        if (!z) {
            this.mCurrentAiScene = 0;
            setCurrentAsdScene(-1);
        }
    }

    public void onFaceDetected(CameraHardwareFace[] cameraHardwareFaceArr, FaceAnalyzeInfo faceAnalyzeInfo, Rect rect) {
        if (isAlive() && this.mActivity.getCameraScreenNail().getFrameAvailableFlag() && cameraHardwareFaceArr != null) {
            if (C0124O00000oO.OOooOoO()) {
                boolean z = cameraHardwareFaceArr.length > 0;
                if (z != this.mFaceDetected && isFrontCamera()) {
                    int i = this.mModuleIndex;
                    if (i == 163 || i == 165 || i == 171) {
                        this.mCamera2Device.resumePreview();
                    }
                }
                this.mFaceDetected = z;
            }
            this.mFaceInfo = faceAnalyzeInfo;
            if (!C0124O00000oO.Oo00o() || cameraHardwareFaceArr.length <= 0 || cameraHardwareFaceArr[0].faceType != 64206) {
                if (this.mIsGoogleLensAvailable) {
                    Camera camera = this.mActivity;
                    if (camera != null) {
                        camera.runOnUiThread(new C0352O0000oo(this, cameraHardwareFaceArr));
                    }
                }
                if (this.mMainProtocol.setFaces(1, cameraHardwareFaceArr, getActiveArraySize(), rect)) {
                    if (this.mIsPortraitLightingOn) {
                        this.mMainProtocol.lightingDetectFace(cameraHardwareFaceArr, false);
                    }
                    if (this.mMainProtocol.isFaceExists(1) && this.mMainProtocol.isFocusViewVisible()) {
                        FocusManager2 focusManager2 = this.mFocusManager;
                        if (focusManager2 != null && !focusManager2.isFromTouch()) {
                            if (!this.mHandler.hasMessages(56)) {
                                this.mHandler.sendEmptyMessage(56);
                            }
                        }
                    }
                    this.mHandler.removeMessages(56);
                }
                return;
            }
            if (this.mObjectTrackingStarted) {
                this.mMainProtocol.setFaces(3, cameraHardwareFaceArr, getActiveArraySize(), rect);
            }
        }
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

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0021, code lost:
        if (r0 != 3) goto L_0x0127;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onFocusStateChanged(FocusTask focusTask) {
        if (!isFrameAvailable()) {
            Log.d(TAG, "onFocusStateChanged: first frame has not arrived");
        } else if (!isDeparted()) {
            int focusTrigger = focusTask.getFocusTrigger();
            if (focusTrigger != 1) {
                if (focusTrigger == 2) {
                    MainContentProtocol mainContentProtocol = this.mMainProtocol;
                    boolean z = mainContentProtocol != null && mainContentProtocol.isFaceExists(1);
                    if (focusTask.isIsDepthFocus() && (!this.mFirstCreateCapture || !z)) {
                        return;
                    }
                }
                String str = null;
                if (focusTask.isFocusing()) {
                    this.mAFEndLogTimes = 0;
                    str = "onAutoFocusMoving start";
                } else if (this.mAFEndLogTimes == 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onAutoFocusMoving end. result=");
                    sb.append(focusTask.isSuccess());
                    str = sb.toString();
                    this.mAFEndLogTimes++;
                    if (this.mFirstCreateCapture) {
                        this.mFirstCreateCapture = false;
                    }
                }
                if (Util.sIsDumpLog && str != null) {
                    Log.v(TAG, str);
                }
                if (getCameraState() != 3 || focusTask.getFocusTrigger() == 3 ? !this.m3ALocked : focusTask.isSuccess()) {
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
                    if (!C0122O00000o.instance().OOoOo0() && isZoomRatioBetweenUltraAndWide()) {
                        CameraCapabilities cameraCapabilities = this.mUltraCameraCapabilities;
                        if (cameraCapabilities != null) {
                            boolean isAFRegionSupported = cameraCapabilities.isAFRegionSupported();
                            String str2 = TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("onFocusStateChanged: isUltraFocusAreaSupported = ");
                            sb2.append(isAFRegionSupported);
                            Log.d(str2, sb2.toString());
                            if (!isAFRegionSupported) {
                                this.mCamera2Device.setFocusMode(0);
                                this.mCamera2Device.setFocusDistance(0.0f);
                                this.mUltraWideAELocked = true;
                            }
                        }
                    }
                    this.mCamera2Device.lockExposure(true);
                }
            }
        }
    }

    public void onFrameThumbnailFail() {
        Log.e(TAG, "could not find anchor frame, use preview as thumbnail");
    }

    public void onFrameThumbnailSuccess(long j) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("anchor frame as thumbnail success ");
        sb.append(j);
        Log.d(str, sb.toString());
    }

    public void onHanGestureSwitched(boolean z) {
        if (z) {
            PreviewDecodeManager.getInstance().init(this.mBogusCameraId, 1);
            PreviewDecodeManager.getInstance().startDecode();
            return;
        }
        PreviewDecodeManager.getInstance().stopDecode(1);
    }

    public void onHdrMotionDetectionResult(boolean z) {
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities != null && cameraCapabilities.isMotionDetectionSupported() && this.mMotionDetected != z) {
            this.mMotionDetected = z;
            updateHDRPreference();
        }
    }

    public void onHdrSceneChanged(boolean z) {
        if (!this.mPaused) {
            this.mIsInHDR = z;
            if (this.isDetectedInHdr != z && triggerHDR(z)) {
                MagneticSensorDetect magneticSensorDetect = this.mMagneticSensorDetect;
                if (magneticSensorDetect == null || !magneticSensorDetect.isLockHDRChecker("onHdrSceneChanged")) {
                    if (getCameraDevice().getCapabilities().getMiAlgoASDVersion() < 3.0f) {
                        updateHDRTip(z);
                    }
                    synchronized (this.mMateDataParserLock) {
                        int cameraState = getCameraState();
                        if (cameraState == 3) {
                            Log.k(3, TAG, String.format("Need ignore HDR scene change. state=%d", new Object[]{Integer.valueOf(cameraState)}));
                            return;
                        }
                        if (z) {
                            if (this.mMutexModePicker.isNormal() || this.mMutexModePicker.isSuperResolution()) {
                                this.mMutexModePicker.setMutexMode(1);
                            }
                        } else if (this.mMutexModePicker.isMorphoHdr()) {
                            this.mMutexModePicker.resetMutexMode();
                        }
                        this.isDetectedInHdr = z;
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onHdrSceneChanged: ");
                        sb.append(z);
                        sb.append(", caller: ");
                        sb.append(Util.getCallers(3));
                        Log.d(str, sb.toString());
                    }
                }
            }
        }
    }

    public void onHostStopAndNotifyActionStop() {
        super.onHostStopAndNotifyActionStop();
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(4097)) {
            this.mHandler.removeMessages(4097);
        }
        boolean z = false;
        if (this.mInStartingFocusRecording) {
            this.mInStartingFocusRecording = false;
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onFinish();
            }
        }
        boolean isInTimerBurstShotting = DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting();
        stopTimerBurst();
        if (isRecording()) {
            stopVideoRecording(true, true);
        }
        if (this.mMultiSnapStatus) {
            onBurstPictureTakenFinished(true);
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(false);
            this.mFocusManager.destroy();
        }
        if (isInTimerBurstShotting || handleSuperNightResultIfNeed()) {
            z = true;
        }
        if (z) {
            doLaterReleaseIfNeed();
        }
        handleSaveFinishIfNeed(null);
    }

    /* access modifiers changed from: protected */
    public boolean onInterceptZoomingEvent(float f, float f2, int i) {
        if (f2 < 1.0f) {
            MiAsdDetect miAsdDetect = (MiAsdDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(235);
            if (miAsdDetect != null) {
                miAsdDetect.updateUltraWide(false, -1);
            }
        }
        if (C0122O00000o.instance().OOoOo0() && !this.mIsMoonMode) {
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
            } else {
                Camera2Proxy camera2Proxy = this.mCamera2Device;
                if (!(camera2Proxy == null || 4 == camera2Proxy.getFocusMode())) {
                    Log.d(TAG, "onInterceptZoomingEvent: should cancel focus.");
                    FocusManager2 focusManager22 = this.mFocusManager;
                    if (focusManager22 != null) {
                        focusManager22.cancelFocus();
                    }
                }
            }
        }
        return super.onInterceptZoomingEvent(f, f2, i);
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
                    performKeyClicked(i2, getString(R.string.pref_camera_volumekey_function_entryvalue_shutter), keyEvent.getRepeatCount(), false);
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

    public void onLivePhotoResultCallback(LivePhotoResult livePhotoResult) {
        this.mLivePhotoQueue.offer(livePhotoResult);
    }

    public void onLongPress(float f, float f2) {
        if (this.mModuleIndex != 182) {
            int i = (int) f;
            int i2 = (int) f2;
            if (isInTapableRect(i, i2)) {
                if (!this.mIsCurrentLensEnabled || !this.mIsGoogleLensAvailable || this.mActivity.startFromSecureKeyguard() || !CameraSettings.isAvailableLongPressGoogleLens()) {
                    onSingleTapUp(i, i2, true);
                    if (this.m3ALockSupported && this.mCamera2Device.getFocusMode() != AutoFocus.convertToFocusMode("manual")) {
                        lockAEAF();
                    }
                    this.mMainProtocol.performHapticFeedback(0);
                    return;
                }
                if (DataRepository.dataItemGlobal().getString(CameraSettings.KEY_LONG_PRESS_VIEWFINDER, null) == null) {
                    CameraStatUtils.trackGoogleLensPicker();
                    FragmentManager fragmentManager = this.mActivity.getFragmentManager();
                    C0345O0000Ooo o0000Ooo = new C0345O0000Ooo(this, f, f2, i, i2);
                    GoogleLensFragment.showOptions(fragmentManager, o0000Ooo);
                } else {
                    CameraStatUtils.trackGoogleLensTouchAndHold();
                    O00000o.getInstance().O000000o(0, f / ((float) Display.getWindowWidth()), f2 / ((float) Display.getWindowHeight()));
                }
            }
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

    public void onNewUriArrived(Uri uri, String str) {
        if (uri == null) {
            handleSaveFinishIfNeed(str);
        } else if (this.mModuleIndex == 167 && DataRepository.dataItemConfig().getComponentConfigRaw().isSwitchOn(this.mModuleIndex)) {
            Handler handler = this.mHandler;
            if (handler != null) {
                this.mWaitSaveFinish = false;
                handler.removeMessages(61);
            }
        }
    }

    public void onObjectStable() {
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        if (!this.isGradienterOn || this.mActivity.getSensorStateManager().isDeviceLying()) {
            setOrientation(i, i2);
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mIsImageCaptureIntent) {
            Camera camera = this.mActivity;
            if (!(camera == null || camera.getImageSaver() == null)) {
                Log.d(TAG, "onPause dropBitmapTexture");
                this.mActivity.getImageSaver().setDropBitmapTexture(true);
            }
        }
        stopLiveShot(true);
        LiveMediaRecorder liveMediaRecorder = this.mLiveMediaRecorder;
        if (liveMediaRecorder != null) {
            liveMediaRecorder.release();
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.removeMessages();
        }
        this.mWaitingSnapshot = false;
        unregisterSensor();
        this.mIsStartCount = false;
        tryRemoveCountDownMessage();
        this.mActivity.getSensorStateManager().reset();
        resetScreenOn();
        closeCamera();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        this.mIsNeedNightHDR = false;
        getActivity().cancelPresentationDisplay();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("(onPause)mIsNeedNightHDR:");
        sb.append(this.mIsNeedNightHDR);
        Log.e(str, sb.toString());
    }

    public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
    }

    public void onPictureTakenFinished(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPictureTakenFinished: succeed = ");
        sb.append(z);
        Log.k(3, str, sb.toString());
        long currentTimeMillis = System.currentTimeMillis();
        int i = this.mModuleIndex;
        if (z) {
            if (i != 173) {
                announceForAccessibility(R.string.accessibility_camera_shutter_finish);
            }
            HashMap hashMap = new HashMap();
            hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
            trackGeneralInfo(hashMap, 1, false, this.mBeautyValues, this.mLocation != null, this.mCurrentAiScene);
            PictureTakenParameter pictureTakenParameter = new PictureTakenParameter();
            pictureTakenParameter.takenNum = 1;
            pictureTakenParameter.burst = false;
            pictureTakenParameter.location = this.mLocation != null;
            pictureTakenParameter.aiSceneName = getCurrentAiSceneName();
            pictureTakenParameter.isEnteringMoon = this.mEnteringMoonMode;
            pictureTakenParameter.isSelectMoonMode = this.mIsMoonMode;
            pictureTakenParameter.isSuperNightInCaptureMode = this.mShowSuperNightHint;
            pictureTakenParameter.beautyValues = this.mBeautyValues;
            pictureTakenParameter.isNearRangeMode = this.mIsNearRangeMode;
            trackPictureTaken(pictureTakenParameter);
            long j = currentTimeMillis - this.mCaptureStartTime;
            CameraStatUtils.trackTakePictureCost(j, isFrontCamera(), this.mModuleIndex);
            ScenarioTrackUtil.trackCaptureTimeEnd();
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mCaptureStartTime(from onShutterButtonClick start to jpegCallback finished) = ");
            sb2.append(j);
            sb2.append(d.H);
            Log.d(str2, sb2.toString());
            if (this.mIsImageCaptureIntent) {
                if (this.mQuickCapture) {
                    doAttach();
                } else if (isAlive()) {
                    this.mKeepBitmapTexture = true;
                    Log.d(TAG, "onPictureTakenFinished: showPostCaptureAlert");
                    showPostCaptureAlert();
                } else {
                    Log.d(TAG, "onPictureTakenFinished: isAlive false");
                    Camera camera = this.mActivity;
                    if (!(camera == null || camera.getCameraScreenNail() == null)) {
                        Log.d(TAG, "releaseBitmapIfNeeded for isAlive false");
                        this.mActivity.getCameraScreenNail().releaseBitmapIfNeeded();
                    }
                }
            } else if (this.mLongPressedAutoFocus) {
                this.mLongPressedAutoFocus = false;
                this.mFocusManager.cancelLongPressedAutoFocus();
            }
        } else if (i == 205) {
            WatermarkItem watermarkItem = this.mWatermarkItem;
            if (watermarkItem != null) {
                watermarkItem.getCaptureCoordinate();
            }
        }
        if (!isKeptBitmapTexture() && !this.mMultiSnapStatus && this.mBlockQuickShot && ((!CameraSettings.isGroupShotOn() || (CameraSettings.isGroupShotOn() && z)) && this.mFixedShot2ShotTime == -1)) {
            resetStatusToIdle();
        }
        this.mHandler.removeMessages(50);
        handleSuperNightResultIfNeed();
        handleSuperNightResultInCaptureMode();
        PreviewDecodeManager.getInstance().resetScanResult();
        doLaterReleaseIfNeed();
        dealTimerBurst(currentTimeMillis);
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
            if (this.mZoomMapController != null) {
                this.mZoomMapController.setMapRect(DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting() ? new ChiRect(0, 0, 0, 0) : CaptureResultParser.getZoomMapRIO(this.mCameraCapabilities, captureResult));
            }
            Integer num = (Integer) captureResult.get(CaptureResult.SENSOR_SENSITIVITY);
            int minIso = this.mCameraCapabilities.getMinIso();
            boolean z = minIso != 0 ? !(num == null || num.intValue() < minIso * 8) : !(num == null || num.intValue() < 800);
            this.mIsISORight4HWMFNR = z;
            if (C0122O00000o.instance().OOoOOoO()) {
                Camera2Proxy camera2Proxy = this.mCamera2Device;
                boolean z2 = camera2Proxy != null && CaptureResultParser.isHHTDisabled(camera2Proxy.getCapabilities(), captureResult);
                this.mHHTDisabled = z2;
            }
            if ((C0122O00000o.instance().OO0OOoO() || !isFrontCamera()) && !C0122O00000o.instance().OOo000o()) {
                this.mShouldDoMFNR = false;
            } else if (C0124O00000oO.OOooO() || C0124O00000oO.OOoo0o()) {
                this.mShouldDoMFNR = true;
            } else {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onPreviewMetaDataUpdate: iso = ");
                sb.append(num);
                sb.append(" minIso = ");
                sb.append(minIso);
                Log.c(str, sb.toString());
                this.mShouldDoMFNR = this.mIsISORight4HWMFNR;
            }
            if (shouldCheckSatFallbackState()) {
                checkSatFallback(captureResult);
            }
            FlowableEmitter flowableEmitter = this.mHistogramEmitter;
            if (flowableEmitter != null) {
                flowableEmitter.onNext(captureResult);
            }
            FlowableEmitter flowableEmitter2 = this.mMetaDataFlowableEmitter;
            if (flowableEmitter2 != null) {
                flowableEmitter2.onNext(captureResult);
            }
            if (this.mAiSceneFlowableEmitter != null && ((this.mAiSceneEnabled || this.mAIWatermarkEnable) && this.mCamera2Device != null)) {
                this.mAiSceneFlowableEmitter.onNext(captureResult);
            }
            if (shouldCheckLLS()) {
                checkLLS(captureResult);
            }
            if (this.mMagneticSensorDetect != null && this.mActivity.getSensorStateManager().isMagneticFieldUncalibratedEnable()) {
                this.mMagneticSensorDetect.updatePreview(captureResult);
            }
            this.mAECLux = CaptureResultParser.getAecLux(captureResult);
            updateAiShutterExistMotion(captureResult);
            if (getCameraState() != 0 && C0124O00000oO.isMTKPlatform()) {
                Camera2Proxy camera2Proxy2 = this.mCamera2Device;
                if (camera2Proxy2 != null && camera2Proxy2.getCameraConfigs().isSpecshotModeEnable()) {
                    this.mSpecShotMode = CaptureResultParser.isSpecshotDetected(captureResult);
                }
            }
        }
    }

    public void onPreviewPixelsRead(byte[] bArr, int i, int i2) {
        if (!DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(this.mModuleIndex)) {
            animateCapture();
            playCameraSound(0);
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bArr));
        boolean z = isFrontCamera() && !isFrontMirror();
        synchronized (this.mCameraDeviceLock) {
            if (isAlive()) {
                if (isDeviceAlive()) {
                    if ((this.mEnableParallelSession || this.mEnableShot2Gallery || updateAnchorFramePreview()) && !this.mIsImageCaptureIntent) {
                        saveBitmapAsThumbnail(createBitmap, i, i2, false, false);
                        return;
                    }
                    int i3 = this.mShootOrientation - this.mDisplayRotation;
                    if (isFrontCamera() && C0124O00000oO.OOooO0o() && i3 % 180 == 0) {
                        i3 = 0;
                    }
                    Thumbnail createThumbnail = Thumbnail.createThumbnail(null, createBitmap, i3, z);
                    createThumbnail.startWaitingForUri();
                    this.mActivity.getThumbnailUpdater().setThumbnail(createThumbnail, true, true);
                    this.mCamera2Device.onPreviewThumbnailReceived(createThumbnail);
                    return;
                }
            }
            Log.d(TAG, "onPreviewPixelsRead: module is dead");
        }
    }

    public void onPreviewSessionClosed(CameraCaptureSession cameraCaptureSession) {
        Log.d(TAG, "onPreviewSessionClosed: ");
        setCameraState(0);
    }

    public void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession) {
        if (!isTextureExpired() || !retryOnceIfCameraError(this.mHandler)) {
            this.mHandler.sendEmptyMessage(51);
        } else {
            Log.d(TAG, "sessionFailed due to surfaceTexture expired, retry");
        }
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
                        Camera2Module.this.initParallelSession();
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

    public void onPreviewSizeChanged(int i, int i2) {
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setPreviewSize(i, i2);
        }
    }

    public void onResume() {
        super.onResume();
        this.mHandler.removeMessages(50);
        Camera camera = this.mActivity;
        CameraScreenNail cameraScreenNail = camera != null ? camera.getCameraScreenNail() : null;
        if ((cameraScreenNail != null && !cameraScreenNail.hasBitMapTexture()) || !isSelectingCapturedResult()) {
            this.mKeepBitmapTexture = false;
            if (cameraScreenNail != null) {
                Log.d(TAG, "onResume releaseBitmapIfNeeded");
                cameraScreenNail.releaseBitmapIfNeeded();
            }
            if (isSelectingCapturedResult()) {
                Log.d(TAG, "onResume hidePostCaptureAlert");
                hidePostCaptureAlert();
            }
        }
        keepScreenOnAwhile();
    }

    public void onReviewCancelClicked() {
        this.mKeepBitmapTexture = false;
        if (isSelectingCapturedResult()) {
            Camera camera = this.mActivity;
            if (camera != null) {
                camera.getCameraScreenNail().releaseBitmapIfNeeded();
            }
            hidePostCaptureAlert();
            return;
        }
        Camera camera2 = this.mActivity;
        if (camera2 != null) {
            camera2.setResult(0, new Intent());
            this.mActivity.finish();
        }
    }

    public void onReviewDoneClicked() {
        doAttach();
    }

    public void onShineChanged(int i) {
        if (i == 196) {
            updatePreferenceTrampoline(2);
            this.mMainProtocol.updateEffectViewVisible();
        } else if (i == 212 || i == 239) {
            updatePreferenceInWorkThread(C0124O00000oO.Oo00O0() ? new int[]{13, 34, 42} : new int[]{13});
        } else {
            throw new RuntimeException("unknown configItem changed");
        }
    }

    public void onShutterButtonClick(int i) {
        if (this.mMediaRecorderRecording) {
            Log.v(TAG, "skip shutter when recording.");
            return;
        }
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
        boolean z = countDownTimes > 0;
        TimerBurstController timerBurstController = DataRepository.dataItemLive().getTimerBurstController();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isInShotting: ");
        sb.append(timerBurstController.isInTimerBurstShotting());
        sb.append("\n(20:volume 10:shutter 120:timer) triggerMode:  ");
        sb.append(i);
        sb.append(",isMenuTimer = ");
        sb.append(z);
        Log.i(str, sb.toString());
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (CameraSettings.isTimerBurstEnable() && TimerBurstController.isSupportTimerBurst(this.mModuleIndex)) {
            TopAlert topAlert2 = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (!timerBurstController.isInTimerBurstShotting() && ((i == 10 || i == 20 || i == 110 || i == 100 || i == 90) && !z)) {
                keepScreenOn();
                topAlert2.hideAlert();
                timerBurstController.setInTimerBurstShotting(true);
                timerBurstController.resetTimerRunningData();
                recordState.onPrepare();
                topAlert2.setRecordingTimeState(1);
                if (this.mModuleIndex == 167) {
                    ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
                    if (manuallyAdjust != null) {
                        manuallyAdjust.setManuallyLayoutVisible(false);
                    }
                }
                recheckAndKeepAutoHibernation();
            } else if (timerBurstController.isInTimerBurstShotting() && i == 120) {
                AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
                if (autoHibernation != null) {
                    String valueOf = String.valueOf(timerBurstController.getCaptureIndex());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(CameraSettings.getTimerBurstTotalCount());
                    sb2.append("");
                    autoHibernation.updateAutoHibernationRecordingTimeOrCaptureCount(valueOf, sb2.toString());
                }
                topAlert2.setRecordingTimeState(1);
            } else if (timerBurstController.isInTimerBurstShotting() && i != 120) {
                stopTimerBurst();
                return;
            }
        } else if (DataRepository.dataItemConfig().getmComponentManuallyET().isLongExpose(this.mModuleIndex)) {
            recordState.onLongExposePrepare();
        }
        int timerBurstInterval = CameraSettings.getTimerBurstInterval();
        if (!CameraSettings.isTimerBurstEnable() || !TimerBurstController.isSupportTimerBurst(this.mModuleIndex)) {
            if (z) {
                startCount(countDownTimes, 1, i);
                return;
            }
        } else if (timerBurstController.getIsDecreasedCount()) {
            keepScreenOn();
            startCount(timerBurstInterval, 1, 120);
            return;
        } else if (z) {
            startCount(countDownTimes, 1, 10);
            return;
        }
        PictureTakenParameter pictureTakenParameter = new PictureTakenParameter();
        TopAlert topAlert3 = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert3 != null) {
            pictureTakenParameter.isASDBacklitTip = topAlert3.isShowBacklightSelector();
        }
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            pictureTakenParameter.isASDPortraitTip = bottomPopupTips.containTips(R.string.recommend_portrait);
        }
        if (checkShutterCondition()) {
            if (isNeedFixedShotTime()) {
                this.mFixedShot2ShotTime = C0122O00000o.instance().O0ooOo0();
            } else {
                this.mFixedShot2ShotTime = -1;
            }
            if (this.mFixedShot2ShotTime != -1) {
                this.mCamera2Device.setFixShotTimeEnabled(true);
                if (this.mFixedShot2ShotTime > 0) {
                    this.mHandler.removeMessages(59);
                    this.mHandler.sendEmptyMessageDelayed(59, (long) this.mFixedShot2ShotTime);
                    String str2 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(":send MSG_FIXED_SHOT2SHOT_TIME_OUT");
                    sb3.append(this.mFixedShot2ShotTime);
                    Log.d(str2, sb3.toString());
                }
            } else {
                this.mCamera2Device.setFixShotTimeEnabled(false);
            }
            setTriggerMode(i);
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder();
            String str4 = "onShutterButtonClick ";
            sb4.append(str4);
            sb4.append(getCameraState());
            Log.u(str3, sb4.toString());
            String str5 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str4);
            sb5.append(getCameraState());
            Log.k(3, str5, sb5.toString());
            this.mFocusManager.prepareCapture(this.mNeedAutoFocus, 2);
            this.mFocusManager.doSnap();
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
            Log.u(TAG, "onShutterButtonLongClick");
            boolean z = (C0124O00000oO.O0o000o || C0124O00000oO.O0oo0o) && isZoomRatioBetweenUltraAndWide();
            if (CameraSettings.isLongPressRecordEnable() && ModuleManager.isCameraModule() && !this.mIsImageCaptureIntent && !this.mMediaRecorderRecording) {
                Handler handler = this.mHandler;
                if (handler != null && !handler.hasMessages(4097) && !CameraSettings.isTiltShiftOn() && !CameraSettings.isAIWatermarkOn() && !CameraSettings.isDocumentModeOn(this.mModuleIndex)) {
                    ModeChangeController modeChangeController = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
                    if (modeChangeController == null || !modeChangeController.modeChanging()) {
                        this.mInStartingFocusRecording = false;
                        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                        if (recordState == null) {
                            return true;
                        }
                        recordState.onPrepare();
                        if (!checkCallingState()) {
                            recordState.onFailed();
                            return true;
                        }
                        this.mActivity.getScreenHint().updateHint();
                        if (Storage.isLowStorageAtLastPoint()) {
                            recordState.onFailed();
                            return true;
                        }
                        setTriggerMode(10);
                        enableCameraControls(false);
                        playVideoSound(false);
                        this.mRequestStartTime = System.currentTimeMillis();
                        if (this.mFocusManager.canRecord()) {
                            this.mIsShutterLongClickRecording = true;
                            this.mHandler.sendEmptyMessageDelayed(4097, 250);
                        } else {
                            Log.v(TAG, "wait for autoFocus");
                            this.mInStartingFocusRecording = true;
                        }
                        return true;
                    }
                    Log.d(TAG, "skip record caz mode changing.");
                    return true;
                }
            }
            if (!CameraSettings.isBurstShootingEnable() || !ModuleManager.isCameraModule() || this.mIsImageCaptureIntent || CameraSettings.isGroupShotOn() || CameraSettings.isTiltShiftOn() || DataRepository.dataItemRunning().isSwitchOn("pref_camera_hand_night_key") || DataRepository.dataItemRunning().isSwitchOn("pref_camera_scenemode_setting_key") || CameraSettings.isPortraitModeBackOn() || !isBackCamera() || this.mMultiSnapStatus || this.mHandler.hasMessages(24) || this.mPendingMultiCapture || isUltraWideBackCamera() || z || CameraSettings.isUltraPixelOn() || isStandaloneMacroCamera() || CameraSettings.isAIWatermarkOn() || CameraSettings.isDocumentModeOn(this.mModuleIndex) || CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex) || (C0124O00000oO.isMTKPlatform() && CameraSettings.isHeicImageFormatSelected())) {
                this.mLongPressedAutoFocus = true;
                this.mMainProtocol.setFocusViewType(false);
                unlockAEAF();
                this.mFocusManager.requestAutoFocus();
                this.mActivity.getScreenHint().updateHint();
                return false;
            }
            this.mPendingMultiCapture = true;
            this.mFocusManager.doMultiSnap(true);
            return true;
        } else {
            Log.d(TAG, "onShutterButtonLongClick: sat fallback");
            return false;
        }
    }

    public void onShutterButtonLongClickCancel(boolean z) {
        Log.d(TAG, "onShutterButtonLongClickCancel: start");
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(4097)) {
            this.mHandler.removeMessages(4097);
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onFailed();
            }
            Log.v(TAG, "onShutterButtonLongClickCancel, remove start recording task");
            enableCameraControls(true);
            if (HybridZoomingSystem.IS_3_OR_MORE_SAT && isBackCamera()) {
                updateZoomRatioToggleButtonState(false);
            }
        } else if (this.mMediaRecorderRecording) {
            this.mIsShutterLongClickRecording = false;
            stopVideoRecording(true, false);
        } else {
            this.mPendingMultiCapture = false;
            if (this.mMultiSnapStatus) {
                this.mHandler.sendEmptyMessageDelayed(49, 2000);
            }
            this.mMultiSnapStopRequest = true;
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.setMultiSnapStopRequest(true);
            }
            if (this.mLongPressedAutoFocus) {
                if (z) {
                    onShutterButtonClick(10);
                } else {
                    this.mLongPressedAutoFocus = false;
                    this.mFocusManager.cancelLongPressedAutoFocus();
                }
            }
        }
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
        sb.append("; Camera2Module: ");
        sb.append(this);
        Log.v(str, sb.toString());
        if (!this.mPaused && this.mCamera2Device != null && !hasCameraException() && this.mCamera2Device.isSessionReady() && this.mCamera2Device.isPreviewReady() && isInTapableRect(i, i2) && getCameraState() != 3 && getCameraState() != 4 && getCameraState() != 0 && !isInCountDown() && !this.mMultiSnapStatus && this.mModuleIndex != 182) {
            if (this.mIsMoonMode) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                boolean z2 = miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow();
                boolean z3 = topAlert != null && topAlert.isExtraMenuShowing();
                if (!z2 && !z3) {
                    return;
                }
            }
            if (isFrameAvailable()) {
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
    }

    public void onStop() {
        super.onStop();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0041, code lost:
        r4.mHandler.post(new com.android.camera.module.Camera2Module.AnonymousClass6(r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        if (r5 == false) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        if (r4.mMagneticSensorDetect == null) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005b, code lost:
        if (r4.mActivity.getSensorStateManager().isMagneticFieldUncalibratedEnable() == false) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0065, code lost:
        if (r4.mMagneticSensorDetect.isLockHDRChecker("realConsumeAiSceneResult") == false) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0067, code lost:
        resetMagneticInfo();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006a, code lost:
        updateHDRPreference();
        resumePreviewInWorkThread();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0070, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onSuperNightChanged(final boolean z) {
        synchronized (this.mMateDataParserLock) {
            if (z != CameraSettings.isSuperNightOn()) {
                if (!isDoingAction()) {
                    CameraSettings.setSuperNightOn(z);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("super night changed：");
                    sb.append(z);
                    Log.d(str, sb.toString());
                    if (z) {
                        this.mCamera2Device.setSuperResolution(false);
                    } else if (this.mMutexModePicker.isSuperResolution()) {
                        this.mCamera2Device.setSuperResolution(true);
                    }
                }
            }
        }
    }

    public void onSuperNightExif(SuperNightExif superNightExif) {
        this.mPreviewSuperNightExifInfo = superNightExif;
        SuperNightExif superNightExif2 = this.mPreviewSuperNightExifInfo;
        if (superNightExif2 != null) {
            this.mSeLuxThreshold = superNightExif2.luxThreshold;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("super_night_exif:");
            sb.append(this.mPreviewSuperNightExifInfo.toString());
            Log.c(str, sb.toString());
        }
    }

    public void onSurfaceTextureReleased() {
        Log.d(TAG, "onSurfaceTextureReleased: no further preview frame will be available");
    }

    public void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
        if (this.mMediaRecorderRecording) {
            LiveMediaRecorder liveMediaRecorder = this.mLiveMediaRecorder;
            if (liveMediaRecorder != null) {
                liveMediaRecorder.onSurfaceTextureUpdated(drawExtTexAttribute, this.mMediaRecorderRecording);
            }
            return;
        }
        CircularMediaRecorder circularMediaRecorder = this.mCircularMediaRecorder;
        if (circularMediaRecorder != null) {
            circularMediaRecorder.onSurfaceTextureUpdated(drawExtTexAttribute);
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.onPreviewComing();
        }
    }

    /* access modifiers changed from: protected */
    public void onThermalConstrained() {
        super.onThermalConstrained();
        if (this.mMediaRecorderRecording) {
            stopVideoRecording(true, false);
        }
    }

    public void onThumbnailClicked(View view) {
        String str;
        String str2;
        if (this.mWaitSaveFinish) {
            str = TAG;
            str2 = "onThumbnailClicked: CannotGotoGallery...mWaitSaveFinish";
        } else {
            if (this.mEnableParallelSession || this.mEnableShot2Gallery || this.mSupportAnchorFrameAsThumbnail) {
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

    public void onTiltShiftSwitched(boolean z) {
        if (z) {
            resetEvValue();
        }
        this.mMainProtocol.initEffectCropView();
        updatePreferenceTrampoline(2, 5);
        this.mMainProtocol.updateEffectViewVisible();
        this.mMainProtocol.setEvAdjustable(!z);
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        if (!isDoingAction()) {
            keepScreenOnAwhile();
        } else if (this.mIsAutoHibernationSupported && DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting()) {
            keepAutoHibernation();
        }
    }

    public boolean onWaitingFocusFinished() {
        if (isBlockSnap() || !isAlive()) {
            return false;
        }
        if (this.mInStartingFocusRecording) {
            this.mInStartingFocusRecording = false;
            if (this.mIsSatFallback == 0 || !shouldCheckSatFallbackState()) {
                startVideoRecording();
                return true;
            }
            Log.w(TAG, "video record check: sat fallback");
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

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onWindowFocusChanged: ");
        sb.append(z);
        Log.d(str, sb.toString());
        if (z && this.mIsAutoHibernationSupported && DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting()) {
            keepAutoHibernation();
        }
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
            DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
            if (CameraSettings.isUltraPixelOn() && !C0122O00000o.instance().OOOOO00() && (dualController == null || !dualController.isZoomVisible())) {
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
        if (this.mMagneticSensorDetect != null && this.mActivity.getSensorStateManager().isMagneticFieldUncalibratedEnable()) {
            this.mMagneticSensorDetect.resetMagneticInfo();
        }
        if (this.mSupportFlashHDR) {
            HDRStatus hDRStatus = this.mCamera2Device.getCameraConfigs().getHDRStatus();
            if (hDRStatus.isFlashHDR() && hDRStatus.isEnable()) {
                this.mIsNeedNightHDR = false;
                this.mMutexModePicker.resetMutexMode();
            }
        }
        ZoomMapController zoomMapController = this.mZoomMapController;
        if (zoomMapController != null) {
            zoomMapController.onZoomRatioUpdate(f);
        }
        return super.onZoomingActionUpdate(f, i);
    }

    /* access modifiers changed from: protected */
    public void openSettingActivity() {
        Intent intent = new Intent();
        intent.setClass(this.mActivity, CameraPreferenceActivity.class);
        intent.putExtra("from_where", this.mModuleIndex);
        intent.putExtra(":miui:starting_window_label", getResources().getString(R.string.pref_camera_settings_category));
        if (this.mActivity.startFromKeyguard()) {
            intent.putExtra("StartActivityWhenLocked", true);
        }
        this.mActivity.startActivity(intent);
        this.mActivity.setJumpFlag(2);
        CameraStatUtils.trackGotoSettings(this.mModuleIndex);
    }

    public void pauseOrResumeTimer() {
        CameraTimer cameraTimer = this.mCameraTimer;
        if (cameraTimer != null) {
            boolean isRunning = cameraTimer.isRunning();
            CameraTimer cameraTimer2 = this.mCameraTimer;
            if (isRunning) {
                cameraTimer2.pause();
            } else {
                cameraTimer2.resume();
            }
        }
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
            if (z) {
                if (i2 > 3 && !isInCountDown() && z && !this.mVolumeLongPress) {
                    this.mVolumeLongPress = onShutterButtonLongClick();
                    if (!this.mVolumeLongPress && this.mLongPressedAutoFocus) {
                        this.mVolumeLongPress = true;
                    }
                }
            } else if (this.mVolumeLongPress) {
                onShutterButtonFocus(false, 0);
                if (this.mVolumeLongPress) {
                    this.mVolumeLongPress = false;
                    onShutterButtonLongClickCancel(false);
                }
            } else {
                onShutterButtonFocus(true, 1);
                if (str.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_timer))) {
                    startCount(2, 1, 20);
                } else {
                    onShutterButtonClick(i);
                }
            }
        }
    }

    public void playFocusSound(int i) {
        if (!this.mMediaRecorderRecording) {
            playCameraSound(i);
        }
    }

    public void playVideoSound(boolean z) {
        int i;
        if (!z) {
            i = 2;
        } else if (!this.mPaused) {
            i = 3;
        } else {
            return;
        }
        playCameraSound(i);
    }

    public void reShowMoon() {
        if (this.mEnteringMoonMode) {
            showMoonMode(this.mIsMoonMode);
        }
    }

    /* access modifiers changed from: protected */
    public List registerMetaDataFunction() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new FuncFaceDetect(new WeakReference(this), new WeakReference(this)));
        arrayList.add(new FunctionNearRangeTip(new WeakReference(this)));
        arrayList.add(new FunctionLivePhoto(new WeakReference(this), new WeakReference(this)));
        return arrayList;
    }

    public void registerProtocol() {
        super.registerProtocol();
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(193, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(195, this);
        getActivity().getImplFactory().initAdditional(getActivity(), 164, 174, 234, 227, 235, 212, 254);
        boolean z = false;
        if (C0122O00000o.instance().OOo0O0o()) {
            getActivity().getImplFactory().initAdditional(getActivity(), 2576);
            this.mMagneticSensorDetect = (MagneticSensorDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(2576);
        }
        if (getModuleIndex() == 163) {
            CameraClickObservable cameraClickObservable = (CameraClickObservable) ModeCoordinatorImpl.getInstance().getAttachProtocol(227);
            if (cameraClickObservable != null) {
                cameraClickObservable.addObservable(new int[]{R.string.recommend_portrait, R.string.recommend_super_night, R.string.lens_dirty_detected_title_back, R.string.recommend_macro_mode, R.string.ultra_wide_recommend_tip_hint_sat}, this.mCameraClickObserverAction, 161, 162, 166, 163, 164, 165, 167, 168);
            }
        }
        if (getModuleIndex() == 173) {
            getActivity().getImplFactory().initAdditional(getActivity(), 212);
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("registerProtocol: mIsGoogleLensAvailable = ");
        sb.append(this.mIsGoogleLensAvailable);
        sb.append(", activity is null ? ");
        if (this.mActivity == null) {
            z = true;
        }
        sb.append(z);
        Log.d(str, sb.toString());
        if (this.mIsGoogleLensAvailable) {
            Camera camera = this.mActivity;
            if (camera != null) {
                camera.runOnUiThread(new O0000OOo(this));
            }
        }
        this.mIsMacroModeEnable = CameraSettings.isMacroModeEnabled(this.mModuleIndex);
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities != null) {
            this.mSupportFlashHDR = cameraCapabilities.isSupportFlashHdr();
        }
    }

    public void resetAiSceneInDocumentModeOn() {
        if (this.mAiSceneEnabled && !this.isResetFromMutex) {
            int i = this.mCurrentAiScene;
            if (i != 0) {
                if (i == -1 || i == 10 || i == 35) {
                    this.mHandler.post(new C0353O0000oo0(this));
                }
            }
        }
    }

    public void resetMagneticInfo() {
        if (this.mMagneticSensorDetect != null && this.mActivity.getSensorStateManager().isMagneticFieldUncalibratedEnable()) {
            this.mMagneticSensorDetect.resetMagneticInfo();
        }
    }

    /* access modifiers changed from: protected */
    public void resetMetaDataManager() {
        CameraSettings.isSupportedMetadata();
    }

    /* access modifiers changed from: protected */
    public void resetScreenOn() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(17);
            this.mHandler.removeMessages(2);
        }
    }

    /* access modifiers changed from: protected */
    public void resetStatusToIdle() {
        Log.d(TAG, "reset Status to Idle");
        setCameraState(1);
        enableCameraControls(true);
        this.mBlockQuickShot = false;
        this.mFixedShot2ShotTime = -1;
    }

    public void resetToIdleNoKeep(boolean z) {
        if (!isKeptBitmapTexture() && !this.mMultiSnapStatus && z) {
            setCameraState(1);
            enableCameraControls(true);
        }
    }

    public void resumePreview() {
        Log.v(TAG, "resumePreview");
        previewWhenSessionSuccess();
        this.mBlockQuickShot = !CameraSettings.isCameraQuickShotEnable();
    }

    public void saveBitmapAsThumbnail(Bitmap bitmap, int i, int i2, boolean z, boolean z2) {
        int i3 = i;
        int i4 = i2;
        boolean z3 = z;
        boolean z4 = z2;
        boolean z5 = isFrontCamera() && !isFrontMirror();
        int orientation = DataRepository.dataItemLive().getTimerBurstController().getOrientation(this.mCamera2Device.isInTimerBurstShotting(), this.mOrientation);
        Bitmap cropBitmap = Util.cropBitmap(bitmap, this.mShootRotation, z5, (float) orientation, this.mModuleIndex == 165, CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex));
        if (cropBitmap == null) {
            Log.w(TAG, "saveBitmapAsThumbnail: bitmap is null!");
            return;
        }
        byte[] bitmapData = Util.getBitmapData(cropBitmap, EncodingQuality.NORMAL.toInteger(false));
        if (bitmapData == null) {
            Log.w(TAG, "saveBitmapAsThumbnail: jpegData is null!");
            return;
        }
        int pictureFormatSuitableForShot = getPictureFormatSuitableForShot(this.mCamera2Device.getCameraConfigs().getShotType());
        int i5 = this.mNormalFilterId;
        if (i5 == FilterInfo.FILTER_ID_NONE) {
            i5 = this.mLightFilterId;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("saveBitmapAsThumbnail: isParallel = ");
        sb.append(this.mEnableParallelSession);
        sb.append(", shot2Gallery = ");
        sb.append(this.mEnableShot2Gallery);
        sb.append(", format = ");
        sb.append(CompatibilityUtils.isHeicImageFormat(pictureFormatSuitableForShot) ? "HEIC" : "JPEG");
        sb.append(", data = ");
        sb.append(bitmapData);
        sb.append(", anchorFrame= ");
        sb.append(z3);
        sb.append(", noGaussian= ");
        sb.append(z4);
        sb.append(", filterId= ");
        sb.append(i5);
        Log.d(str, sb.toString());
        ParallelTaskData parallelTaskData = new ParallelTaskData(this.mActualCameraId, System.currentTimeMillis(), -1, this.mCamera2Device.getParallelShotSavePath(), this.mCaptureStartTime);
        boolean z6 = this.mEnableParallelSession || this.mEnableShot2Gallery || updateAnchorFramePreview();
        parallelTaskData.setNeedThumbnail(z6);
        parallelTaskData.fillJpegData(bitmapData, 0);
        parallelTaskData.setNoGaussian(z4);
        parallelTaskData.fillParameter(new Builder(new Size(i3, i4), new Size(i3, i4), new Size(i3, i4), pictureFormatSuitableForShot).setOrientation(orientation).setJpegRotation(this.mJpegRotation).setShootRotation(this.mShootRotation).setShootOrientation(this.mShootOrientation).setSupportZeroDegreeOrientationImage(this.mCameraCapabilities.isSupportZeroDegreeOrientationImage()).setLocation(this.mLocation).setFilterId(i5).setAnchorPreview(z3).setPictureInfo(getPictureInfo()).setMirror(isFrontMirror()).setFrontCamera(isFrontCamera()).build());
        if (C0122O00000o.instance().OOo0oOO()) {
            parallelTaskData.setRequireTuningData(true);
        }
        this.mActivity.getImageSaver().onParallelProcessFinish(parallelTaskData, null, null);
    }

    public boolean scanQRCodeEnabled() {
        if (CameraSettings.isScanQRCode(this.mActivity)) {
            int i = this.mModuleIndex;
            if ((i == 163 || i == 165) && !this.mIsImageCaptureIntent && CameraSettings.isBackCamera() && !this.mMultiSnapStatus && !CameraSettings.isPortraitModeBackOn() && ((!C0122O00000o.instance().OO0OooO() || !CameraSettings.isUltraPixelOn()) && !CameraSettings.isUltraWideConfigOpen(this.mModuleIndex) && !CameraSettings.isMacroModeEnabled(this.mModuleIndex))) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void sendOpenFailMessage() {
        this.mHandler.sendEmptyMessage(10);
    }

    public void setAsdScenes(ASDScene[] aSDSceneArr) {
        this.mAsdScenes = aSDSceneArr;
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
        if (z && isBackCamera()) {
            int i = this.mModuleIndex;
            if ((i == 165 || i == 163) && CameraSettings.isCameraLyingHintOn()) {
                this.mHandler.post(new C0346O0000o0O(this));
            }
        }
    }

    public void setHdrModeChange(String str) {
        HdrTrigger hdrTrigger = this.mHdrTrigger;
        if (hdrTrigger != null) {
            hdrTrigger.setHdrModeChange(str);
        }
    }

    public void setIsUltraWideConflict(boolean z) {
        this.mIsUltraWideConflict = z;
    }

    public void setNearRangeMode(boolean z) {
        this.mIsNearRangeMode = z;
    }

    public void setNearRangeModeUIStatus(boolean z) {
        this.mIsNearRangeModeUITip = z;
    }

    public boolean shouldCaptureDirectly() {
        if (this.mUseLegacyFlashMode) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null && camera2Proxy.isNeedFlashOn()) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean shouldCheckSatFallbackState() {
        return isIn3OrMoreSatMode() && C0122O00000o.instance().shouldCheckSatFallbackState();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0055, code lost:
        if (r1.hasMessages(50) != false) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0090, code lost:
        if (getManualValue(com.android.camera.CameraSettings.KEY_QC_EXPOSURETIME, getString(com.android.camera.R.string.pref_camera_exposuretime_default)).equals(getString(com.android.camera.R.string.pref_camera_exposuretime_default)) != false) goto L_0x0092;
     */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0045  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldReleaseLater() {
        boolean z;
        boolean z2 = false;
        this.mIsStartCount = false;
        tryRemoveCountDownMessage();
        if (getCameraState() != 3) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy == null || !camera2Proxy.isCaptureBusy(true)) {
                z = false;
                boolean isInTimerBurstShotting = DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting();
                if (!this.mInStartingFocusRecording && !isRecording() && !isInTimerBurstShotting) {
                    Handler handler = this.mHandler;
                    if (handler == null || !handler.hasMessages(4097)) {
                        if (!this.mIsImageCaptureIntent) {
                            if (!z) {
                                if (this.mEnableShot2Gallery) {
                                    Handler handler2 = this.mHandler;
                                    if (handler2 != null) {
                                    }
                                }
                            }
                            Handler handler3 = this.mHandler;
                            if ((handler3 == null || (!handler3.hasMessages(48) && !this.mHandler.hasMessages(49))) && !this.mFocusManager.isFocusing()) {
                                if (this.mModuleIndex == 167) {
                                }
                                z2 = true;
                            }
                        }
                        return z2;
                    }
                }
                return true;
            }
        }
        z = true;
        boolean isInTimerBurstShotting2 = DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting();
        Handler handler4 = this.mHandler;
        if (!this.mIsImageCaptureIntent) {
        }
        return z2;
    }

    public void showBacklightTip() {
        if (!CameraSettings.isMacroModeEnabled(this.mModuleIndex)) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).closeMutexElement("e", 193);
            topAlert.alertHDR(8, false, false);
            topAlert.alertAiSceneSelector(0);
            applyBacklightEffect();
            resumePreviewInWorkThread();
        }
    }

    public void showDocumentPreview(PreviewImage previewImage, float[] fArr) {
        playCameraSound(0);
        String parallelShotSavePath = this.mCamera2Device.getParallelShotSavePath();
        String componentValue = DataRepository.dataItemRunning().getComponentRunningDocument().getComponentValue(this.mModuleIndex);
        Size size = new Size(previewImage.getWidth(), previewImage.getHeight());
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("showDocumentPreview: savePath = ");
        sb.append(parallelShotSavePath);
        sb.append(", value = ");
        sb.append(componentValue);
        Log.d(str, sb.toString());
        Bitmap doCropAndEnhance = DocumentProcess.getInstance().doCropAndEnhance(previewImage.getData(), previewImage.getWidth(), previewImage.getHeight(), fArr, EnhanceType.valueOf(componentValue.toUpperCase(Locale.ENGLISH)), false, this.mRotateFlags);
        float[] rotatePoints = DocumentProcess.getInstance().rotatePoints(fArr, previewImage.getWidth(), previewImage.getHeight(), this.mRotateFlags);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("showDocumentPreview: points = ");
        sb2.append(Arrays.toString(fArr));
        sb2.append(", rotatePoints = ");
        sb2.append(Arrays.toString(rotatePoints));
        Log.d(str2, sb2.toString());
        if (doCropAndEnhance == null) {
            Log.d(TAG, "showDocumentPreview cropImage is null...");
            this.mMainProtocol.hideOrShowDocument(true);
            PreviewDecodeManager.getInstance().startDecode();
            return;
        }
        this.mHandler.post(new C0350O0000oOO((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162), doCropAndEnhance, rotatePoints, size));
        this.mHandler.postDelayed(new O0000o0(this), Util.getEnterDuration() + Util.getSuspendDuration() + Util.getExitDuration());
        if (Util.isSaveDocPreview()) {
            Schedulers.io().scheduleDirect(new C0390O00oOooo(this, parallelShotSavePath, doCropAndEnhance));
        }
        byte[] bitmapData = Util.getBitmapData(doCropAndEnhance, EncodingQuality.NORMAL.toInteger(false));
        if (bitmapData.length < 1) {
            Log.w(TAG, "showDocumentPreview: jpegData is null!");
            return;
        }
        int width = doCropAndEnhance.getWidth();
        int height = doCropAndEnhance.getHeight();
        ParallelTaskData parallelTaskData = new ParallelTaskData(this.mActualCameraId, System.currentTimeMillis(), -1, parallelShotSavePath);
        boolean z = this.mEnableParallelSession || this.mEnableShot2Gallery;
        parallelTaskData.setNeedThumbnail(z);
        parallelTaskData.fillJpegData(bitmapData, 0);
        parallelTaskData.fillParameter(new Builder(new Size(width, height), new Size(width, height), new Size(width, height), 256).setOrientation(this.mOrientation).build());
        parallelTaskData.setRequireTuningData(true);
        this.mActivity.getImageSaver().onParallelProcessFinish(parallelTaskData, null, null);
    }

    public void showOrHideChip(boolean z) {
        if (this.mIsGoogleLensAvailable) {
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            boolean z2 = true;
            if (z) {
                boolean z3 = bottomPopupTips != null && bottomPopupTips.isTipShowing();
                DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                boolean z4 = dualController != null && dualController.isZoomPanelVisible();
                MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
                boolean z5 = makeupProtocol != null && makeupProtocol.isSeekBarVisible();
                MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
                boolean z6 = miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow();
                boolean z7 = !this.mIsAiConflict && !this.mIsFaceConflict && !this.mIsUltraWideConflict && !this.mIsMoonMode && !z3 && !z4 && !z5 && !z6;
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("pre showOrHideChip: isTipsShow = ");
                sb.append(z3);
                sb.append(", isZoomSlideVisible = ");
                sb.append(z4);
                sb.append(", isSeekBarVisible = ");
                sb.append(z5);
                sb.append(", isMakeupVisible = ");
                sb.append(z6);
                sb.append(", mIsAiConflict = ");
                sb.append(this.mIsAiConflict);
                sb.append(", mIsUltraWideConflict = ");
                sb.append(this.mIsUltraWideConflict);
                sb.append(", mIsMoonMode = ");
                sb.append(this.mIsMoonMode);
                sb.append(", mIsFaceConflict = ");
                sb.append(this.mIsFaceConflict);
                sb.append(", final isShow = ");
                sb.append(z7);
                sb.append(", mIsCurrentLensEnabled = ");
                sb.append(this.mIsCurrentLensEnabled);
                Log.d(str, sb.toString());
                z = z7;
            }
            if (this.mIsCurrentLensEnabled != z) {
                this.mIsCurrentLensEnabled = z;
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("showOrHideChip: show = ");
                sb2.append(z);
                sb2.append(", isChipsEnabled = ");
                sb2.append(CameraSettings.isAvailableChipsGoogleLens());
                Log.d(str2, sb2.toString());
                O00000o instance = O00000o.getInstance();
                if (!z || !CameraSettings.isAvailableChipsGoogleLens()) {
                    z2 = false;
                }
                instance.showOrHideChip(z2);
                if (bottomPopupTips != null) {
                    bottomPopupTips.reConfigQrCodeTip();
                }
            }
        }
    }

    public void showQRCodeResult() {
        if (!this.mPaused) {
            String scanResult = PreviewDecodeManager.getInstance().getScanResult();
            if (scanResult == null || scanResult.isEmpty()) {
                Log.e(TAG, "showQRCodeResult: get a null result!");
                return;
            }
            Camera camera = this.mActivity;
            camera.dismissKeyguard();
            Intent intent = new Intent(Util.QRCODE_RECEIVER_ACTION);
            intent.addFlags(32);
            intent.setPackage("com.xiaomi.scanner");
            intent.putExtra("result", scanResult);
            camera.sendBroadcast(intent);
            camera.setJumpFlag(3);
            PreviewDecodeManager.getInstance().resetScanResult();
        }
    }

    public void startAiLens() {
        this.mHandler.postDelayed(new C0347O0000o0o(this), 300);
    }

    public void startFaceDetection() {
        if (this.mFaceDetectionEnabled && !this.mFaceDetectionStarted) {
            Camera camera = this.mActivity;
            if (camera != null && !camera.isActivityPaused() && isAlive() && this.mMaxFaceCount > 0 && this.mCamera2Device != null) {
                this.mFaceDetectionStarted = true;
                this.mMainProtocol.setActiveIndicator(1);
                this.mCamera2Device.startFaceDetection();
                updateFaceView(true, true);
            }
        }
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

    public void startLiveShot() {
        synchronized (this.mCircularMediaRecorderStateLock) {
            try {
                if (this.mCircularMediaRecorder == null) {
                    CircularMediaRecorder circularMediaRecorder = new CircularMediaRecorder(this.mVideoSize.width, this.mVideoSize.height, getActivity().getGLView().getEGLContext14(), this.mIsMicrophoneEnabled, this.mLivePhotoQueue);
                    this.mCircularMediaRecorder = circularMediaRecorder;
                }
                this.mLiveShotEnabled = true;
                this.mCircularMediaRecorder.setOrientationHint(this.mOrientationCompensation);
                this.mCircularMediaRecorder.start();
            } catch (Exception e) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startLiveShot: ");
                sb.append(e.getMessage());
                Log.w(str, sb.toString());
                return;
            }
        }
        this.mActivity.getSensorStateManager().setGyroscopeEnabled(true);
    }

    public void startObjectTracking() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0165  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0171  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0174  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0181  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x019f  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01bc  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0202  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x020f  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0223  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x022a  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0284  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x028b  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0291  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startPreview() {
        boolean z;
        int i;
        boolean z2;
        SurfaceTexture surfaceTexture;
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setActivityHashCode(this.mActivity.hashCode());
            this.mCamera2Device.setFocusCallback(this);
            this.mCamera2Device.setFocusFrameAvailable(this);
            this.mCamera2Device.setMetaDataCallback(this);
            this.mCamera2Device.setScreenLightCallback(this);
            this.mCamera2Device.setErrorCallback(this.mErrorCallback);
            this.mCamera2Device.setMagneticDetectedCallback(this);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startPreview: set PictureSize with ");
            sb.append(this.mPictureSize);
            Log.k(3, str, sb.toString());
            this.mCamera2Device.setPictureSize(this.mPictureSize);
            this.mCamera2Device.setBokehDepthSize(this.mBokehDepthSize);
            this.mCamera2Device.setPictureFormat(this.mOutputPictureFormat);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("startPreview: set PictureFormat to ");
            sb2.append(CompatibilityUtils.isHeicImageFormat(this.mOutputPictureFormat) ? "HEIC" : "JPEG");
            Log.d(str2, sb2.toString());
            int rawCallbackType = getRawCallbackType(true);
            if (rawCallbackType > 0) {
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("startPreview: set SensorRawImageSize with ");
                sb3.append(this.mSensorRawImageSize);
                Log.d(str3, sb3.toString());
                this.mCamera2Device.setSensorRawImageSize(this.mSensorRawImageSize, rawCallbackType);
            }
            if (this.mEnableParallelSession && isPortraitMode()) {
                String str4 = TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("startPreview: set SubPictureSize with ");
                sb4.append(this.mSubPictureSize);
                Log.d(str4, sb4.toString());
                this.mCamera2Device.setSubPictureSize(this.mSubPictureSize);
            }
            CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
            if (cameraCapabilities != null && cameraCapabilities.supportPhysicCameraId() && VERSION.SDK_INT >= 30) {
                if (getModuleIndex() == 171) {
                    this.mCamera2Device.setExtendSceneMode(2);
                } else {
                    this.mCamera2Device.setExtendSceneMode(0);
                }
            }
            boolean isEnableQcfaForAlgoUp = isEnableQcfaForAlgoUp();
            String str5 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("[QCFA] startPreview: set qcfa enable ");
            sb5.append(isEnableQcfaForAlgoUp);
            Log.d(str5, sb5.toString());
            this.mCamera2Device.setQcfaEnable(isEnableQcfaForAlgoUp);
            if (isEnableQcfaForAlgoUp) {
                String str6 = TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("startPreview: set binning picture size to ");
                sb6.append(this.mBinningPictureSize);
                Log.d(str6, sb6.toString());
                this.mCamera2Device.setBinningPictureSize(this.mBinningPictureSize);
            }
            boolean scanQRCodeEnabled = scanQRCodeEnabled();
            if (isFrontCamera()) {
                int i2 = this.mModuleIndex;
                if ((i2 == 163 || i2 == 165 || i2 == 186 || i2 == 171 || i2 == 182 || i2 == 205) && DataRepository.dataItemRunning().supportHandGesture()) {
                    z = true;
                    if (!this.mIsGoogleLensAvailable) {
                        PreviewDecodeManager.getInstance().init(this.mActualCameraId, 2);
                        i = 8;
                    } else {
                        i = 0;
                    }
                    if (scanQRCodeEnabled) {
                        i |= 2;
                        PreviewDecodeManager.getInstance().init(this.mBogusCameraId, 0);
                    }
                    if (z) {
                        i |= 4;
                        PreviewDecodeManager.getInstance().init(this.mBogusCameraId, 1);
                    }
                    z2 = !isBackCamera() && CameraSettings.isDocumentMode2On(this.mModuleIndex);
                    if (z2) {
                        CameraCapabilities cameraCapabilities2 = this.mCameraCapabilities;
                        if (cameraCapabilities2 != null) {
                            this.mRotateFlags = DocumentDecoder.getRotateFlag(cameraCapabilities2.getSensorOrientation());
                        }
                        i |= 32;
                        PreviewDecodeManager.getInstance().init(this.mActualCameraId, 3);
                    }
                    if (this.mSupportAnchorFrameAsThumbnail) {
                        i |= 16;
                        this.mCacheImageDecoder = new CacheImageDecoder();
                        this.mCacheImageDecoder.init(this.mBogusCameraId);
                        this.mCacheImageDecoder.setAnchorPreviewCallback(this);
                        this.mCamera2Device.setCacheImageDecoder(this.mCacheImageDecoder);
                        this.mCamera2Device.setPreviewMaxImages(21);
                    }
                    int i3 = i;
                    surfaceTexture = this.mActivity.getCameraScreenNail().getSurfaceTexture();
                    String str7 = TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("startPreview: surfaceTexture = ");
                    sb7.append(surfaceTexture);
                    Log.k(3, str7, sb7.toString());
                    if (surfaceTexture == null) {
                        this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
                    } else {
                        String str8 = "startPreview: surfaceTexture unavailable!!!!";
                        Log.e(TAG, str8);
                        Log.e(TAG, str8);
                        Log.e(TAG, str8);
                    }
                    Surface surface = surfaceTexture == null ? new Surface(surfaceTexture) : null;
                    this.mRawCallbackType = getRawCallbackType(true);
                    int operatingMode = getOperatingMode();
                    if (CameraSettings.isMacro2Sat() && 36866 == operatingMode && C0122O00000o.instance().OO0Oo0o()) {
                        int lensIndex = CameraSettings.getLensIndex();
                        operatingMode |= lensIndex << 8;
                        String str9 = TAG;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("getOperatingMode = ");
                        sb8.append(operatingMode);
                        Log.d(str9, sb8.toString());
                        String str10 = TAG;
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("Index = ");
                        sb9.append(lensIndex);
                        Log.d(str10, sb9.toString());
                    }
                    int i4 = operatingMode;
                    if (CameraSettings.isMacro2Sat()) {
                        CameraSettings.setMacro2Sat(false);
                    }
                    ZoomMapController zoomMapController = this.mZoomMapController;
                    this.mCamera2Device.startPreviewSession(surface, i3, this.mRawCallbackType, zoomMapController == null ? zoomMapController.createZoomMapSurfaceIfNeeded() : null, i4, this.mEnableParallelSession, this);
                }
            }
            z = false;
            if (!this.mIsGoogleLensAvailable) {
            }
            if (scanQRCodeEnabled) {
            }
            if (z) {
            }
            if (!isBackCamera()) {
            }
            if (z2) {
            }
            if (this.mSupportAnchorFrameAsThumbnail) {
            }
            int i32 = i;
            surfaceTexture = this.mActivity.getCameraScreenNail().getSurfaceTexture();
            String str72 = TAG;
            StringBuilder sb72 = new StringBuilder();
            sb72.append("startPreview: surfaceTexture = ");
            sb72.append(surfaceTexture);
            Log.k(3, str72, sb72.toString());
            if (surfaceTexture == null) {
            }
            if (surfaceTexture == null) {
            }
            this.mRawCallbackType = getRawCallbackType(true);
            int operatingMode2 = getOperatingMode();
            int lensIndex2 = CameraSettings.getLensIndex();
            operatingMode2 |= lensIndex2 << 8;
            String str92 = TAG;
            StringBuilder sb82 = new StringBuilder();
            sb82.append("getOperatingMode = ");
            sb82.append(operatingMode2);
            Log.d(str92, sb82.toString());
            String str102 = TAG;
            StringBuilder sb92 = new StringBuilder();
            sb92.append("Index = ");
            sb92.append(lensIndex2);
            Log.d(str102, sb92.toString());
            int i42 = operatingMode2;
            if (CameraSettings.isMacro2Sat()) {
            }
            ZoomMapController zoomMapController2 = this.mZoomMapController;
            this.mCamera2Device.startPreviewSession(surface, i32, this.mRawCallbackType, zoomMapController2 == null ? zoomMapController2.createZoomMapSurfaceIfNeeded() : null, i42, this.mEnableParallelSession, this);
        }
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
        if (localBinder != null && CameraSettings.isPictureFlawCheckOn()) {
            localBinder.setOnSessionStatusCallBackListener(this.mSessionStatusCallbackListener);
        }
    }

    public void startScreenLight(final int i, final int i2) {
        if (!this.mPaused) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
                    if (fullScreenProtocol != null) {
                        fullScreenProtocol.setScreenLightColor(i);
                        if (fullScreenProtocol.showScreenLight()) {
                            Camera2Proxy camera2Proxy = Camera2Module.this.mCamera2Device;
                            if (camera2Proxy != null) {
                                camera2Proxy.setAELock(true);
                            }
                            Camera camera = Camera2Module.this.mActivity;
                            if (camera != null) {
                                camera.setWindowBrightness(i2);
                            }
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void startVideoRecording() {
        if (this.mCamera2Device == null) {
            Log.e(TAG, "initializeRecorder: null camera");
            return;
        }
        setCameraAudioRestriction(true);
        Log.v(TAG, "startVideoRecording");
        silenceOuterAudio();
        if (this.mLiveMediaRecorder == null) {
            this.mLiveMediaRecorder = new LiveMediaRecorder();
        }
        String genVideoPath = Util.genVideoPath(2, getString(R.string.video_file_name_format));
        CameraSize cameraSize = this.mVideoSize;
        if (!this.mLiveMediaRecorder.init(Util.genContentValues(2, genVideoPath, cameraSize.width, cameraSize.height), this.mOrientationCompensation, getActivity().getGLView().getEGLContext14(), this.mMediaEncoderListener) || !this.mLiveMediaRecorder.startRecorder(this.mRequestStartTime)) {
            onStartRecorderFail();
            return;
        }
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT && isBackCamera()) {
            updateZoomRatioToggleButtonState(true);
        }
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onStart();
        }
        Log.v(TAG, "startVideoRecording process done");
        onStartRecorderSucceed();
    }

    public void stopFaceDetection(boolean z) {
        if (this.mFaceDetectionEnabled && this.mFaceDetectionStarted) {
            if (!C0124O00000oO.isMTKPlatform() || !(getCameraState() == 3 || getCameraState() == 0)) {
                this.mCamera2Device.stopFaceDetection();
            }
            this.mFaceDetectionStarted = false;
            this.mMainProtocol.setActiveIndicator(2);
            updateFaceView(false, z);
        }
    }

    public void stopLiveShot(boolean z) {
        synchronized (this.mCircularMediaRecorderStateLock) {
            if (this.mCircularMediaRecorder != null) {
                if (z) {
                    this.mCircularMediaRecorder.moduleSwitched();
                }
                this.mCircularMediaRecorder.stop();
                if (z) {
                    this.mCircularMediaRecorder.release();
                    this.mCircularMediaRecorder = null;
                }
            }
            this.mLiveShotEnabled = false;
        }
        this.mActivity.getSensorStateManager().setGyroscopeEnabled(false);
        this.mLivePhotoQueue.clear();
    }

    public void stopObjectTracking(boolean z) {
    }

    public void stopScreenLight() {
        this.mHandler.post(new Runnable() {
            public void run() {
                Camera2Proxy camera2Proxy = Camera2Module.this.mCamera2Device;
                if (camera2Proxy != null) {
                    camera2Proxy.setAELock(false);
                }
                Camera camera = Camera2Module.this.mActivity;
                if (camera != null) {
                    camera.restoreWindowBrightness();
                }
                FullScreenProtocol fullScreenProtocol = (FullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(196);
                String access$400 = Camera2Module.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("stopScreenLight: protocol = ");
                sb.append(fullScreenProtocol);
                sb.append(", mHandler = ");
                sb.append(Camera2Module.this.mHandler);
                Log.d(access$400, sb.toString());
                if (fullScreenProtocol != null) {
                    fullScreenProtocol.hideScreenLight();
                }
            }
        });
    }

    public void stopVideoRecording(boolean z, boolean z2) {
        if (this.mMediaRecorderRecording) {
            setCameraAudioRestriction(false);
            if (is3ALocked()) {
                unlockAEAF();
            }
            this.mMediaRecorderRecording = false;
            LiveMediaRecorder liveMediaRecorder = this.mLiveMediaRecorder;
            if (liveMediaRecorder != null) {
                liveMediaRecorder.stopRecorder(this.mRecordingStartTime, false);
            }
            this.mActivity.sendBroadcast(new Intent(BaseModule.STOP_VIDEO_RECORDING_ACTION));
            listenPhoneState(false);
            CountDownTimer countDownTimer = this.mCountDownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            if (isBackCamera()) {
                updateZoomRatioToggleButtonState(false);
            }
            BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
            if (baseDelegate != null) {
                baseDelegate.getAnimationComposite().setClickEnable(true);
            }
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onFinish();
            }
            this.mMainProtocol.setEvAdjustVisible(true);
            restoreOuterAudio();
            keepScreenOnAwhile();
            AutoLockManager.getInstance(this.mActivity).hibernateDelayed();
        }
    }

    /* access modifiers changed from: protected */
    public void trackModeCustomInfo(Map map, boolean z, BeautyValues beautyValues, int i, boolean z2) {
        if (map == null) {
            map = new HashMap();
        }
        int i2 = this.mModuleIndex;
        if (i2 == 167) {
            trackManualInfo(i);
        } else if (i2 == 163 || i2 == 165 || i2 == 186 || i2 == 182 || i2 == 205) {
            CameraStatUtils.trackLyingDirectPictureTaken(map, this.mIsShowLyingDirectHintStatus);
            trackCaptureModuleInfo(map, i, z, z2);
            trackBeautyInfo(i, isFrontCamera(), beautyValues);
        }
    }

    public void tryRemoveCountDownMessage() {
        if (isInCountDown()) {
            this.mCameraTimer.dispose();
            this.mCameraTimer = null;
            this.mHandler.post(new Runnable() {
                public void run() {
                    Log.d(Camera2Module.TAG, "run: hide delay number in main thread");
                    Camera2Module.this.mTopAlert.hideDelayNumber();
                }
            });
        }
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unRegisterProtocol: mIsGoogleLensAvailable = ");
        sb.append(this.mIsGoogleLensAvailable);
        sb.append(", activity is null ? ");
        sb.append(this.mActivity == null);
        Log.d(str, sb.toString());
        if (this.mIsGoogleLensAvailable) {
            Camera camera = this.mActivity;
            if (camera != null) {
                camera.runOnUiThread(new C0349O0000oO0(this));
            }
        }
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(193, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(195, this);
        getActivity().getImplFactory().detachAdditional();
    }

    public void updateBacklight() {
        if (!isDoingAction() && isAlive()) {
            this.isSilhouette = false;
            applyBacklightEffect();
            resumePreviewInWorkThread();
        }
    }

    public void updateDoDepurple() {
        List asList = Arrays.asList(C0122O00000o.instance().OO0ooO().toUpperCase(Locale.ENGLISH).split(":"));
        boolean z = (C0122O00000o.instance().OO0ooOO() && getZoomRatio() >= HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR && getZoomRatio() < HybridZoomingSystem.getTeleMinZoomRatio() && getActualCameraId() == Camera2DataContainer.getInstance().getSATCameraId()) || (asList.contains("MACRO") && Camera2DataContainer.getInstance().getStandaloneMacroCameraId() == getActualCameraId()) || ((asList.contains("TELE") && Camera2DataContainer.getInstance().getAuxCameraId() == getActualCameraId()) || ((asList.contains("ULTRA_TELE") && Camera2DataContainer.getInstance().getUltraTeleCameraId() == getActualCameraId()) || ((asList.contains("WIDE") && Camera2DataContainer.getInstance().getMainBackCameraId() == getActualCameraId()) || ((asList.contains("ULTRA_WIDE") && Camera2DataContainer.getInstance().getUltraWideCameraId() == getActualCameraId()) || (asList.contains("SAT") && Camera2DataContainer.getInstance().getSATCameraId() == getActualCameraId())))));
        if (!this.mCamera2Device.getCameraConfigs().isHDREnabled()) {
            z = true;
        }
        if (this.mCamera2Device.isShotQueueMultitasking() || ((this.mModuleIndex == 173 && C0122O00000o.instance().OO0oOO()) || this.mCamera2Device.getCapabilities().isSensorDepurpleDisable())) {
            z = false;
        }
        this.mCamera2Device.setDodepurpleEnabled(z);
    }

    public void updateDxoAsdScene(MarshalQueryableDxoAsdScene.ASDScene aSDScene) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("confident:");
        sb.append(aSDScene.confident);
        sb.append(",value:");
        sb.append(aSDScene.value);
        Log.c(str, sb.toString());
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.getCameraConfigs().setDxoAsdScene(aSDScene);
        }
    }

    /* access modifiers changed from: protected */
    public void updateFaceView(boolean z, boolean z2) {
        if (this.mHandler.hasMessages(35)) {
            this.mHandler.removeMessages(35);
        }
        this.mHandler.obtainMessage(35, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
    }

    public void updateFlashPreference() {
        String componentValue = DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex);
        String requestFlashMode = getRequestFlashMode();
        if (Util.parseInt(requestFlashMode, 0) != 0) {
            resetAiSceneInHdrOrFlashOn();
        }
        setFlashMode(requestFlashMode);
        if (!TextUtils.equals(componentValue, this.mLastFlashMode) && (Util.parseInt(componentValue, 0) == 103 || Util.parseInt(componentValue, 0) == 0)) {
            resetAsdSceneInHdrOrFlashChange();
        }
        if (this.mLastFlashMode != componentValue) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.resetFlashStateTimeLock();
            }
        }
        this.mLastFlashMode = componentValue;
        stopObjectTracking(true);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            int[] iArr = new int[1];
            if (requestFlashMode.equals("200")) {
                iArr[0] = 193;
                topAlert.disableMenuItem(false, iArr);
            } else {
                iArr[0] = 193;
                topAlert.enableMenuItem(false, iArr);
            }
        }
        if ("3".equals(requestFlashMode)) {
            setCurrentAsdScene(-1);
        }
    }

    public void updateHDRPreference() {
        ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
        if (!componentHdr.isEmpty()) {
            String componentValue = componentHdr.getComponentValue(this.mModuleIndex);
            String str = "auto";
            if ((!HybridZoomingSystem.isZoomRatioNone(getZoomRatio(), isFrontCamera()) || this.mMotionDetected) && this.mMutexModePicker.isHdr() && str.equals(componentValue)) {
                onHdrSceneChanged(false);
            }
            boolean isHdrOnWithChecker = componentHdr.isHdrOnWithChecker(componentValue);
            String str2 = "normal";
            String str3 = "off";
            if (this.mIsMoonMode || this.mMotionDetected) {
                updateHDR(str3);
            } else if (isTriggerFlashHDR()) {
                this.mIsNeedNightHDR = true;
                updateHDR(str2);
                String str4 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("flash auto into hdr mode,mIsNeedNightHDR:");
                sb.append(this.mIsNeedNightHDR);
                Log.d(str4, sb.toString());
            } else if (isHdrOnWithChecker) {
                updateHDR(str);
            } else {
                updateHDR(componentValue);
            }
            if ((!str3.equals(componentValue) || this.mAiSceneEnabled) && ((getZoomRatio() <= HybridZoomingSystem.getZoomRatioNone(isFrontCamera(), this.mOrientation) || str2.equals(componentValue) || C0122O00000o.instance().OOo0OOO()) && !this.mIsMoonMode && ((!C0124O00000oO.O0o0O00 || !isStandaloneMacroCamera() || !str.equals(componentValue)) && !CameraSettings.isSuperNightOn()))) {
                resetAiSceneInHdrOrFlashOn();
                resetAsdSceneInHdrOrFlashChange();
                if (isHdrOnWithChecker || str.equals(componentValue)) {
                    this.mHdrCheckEnabled = true;
                    if (C0122O00000o.instance().OOo0O0o()) {
                        this.mActivity.getSensorStateManager().setMagneticFieldUncalibratedEnable(true);
                    }
                } else {
                    this.mHdrCheckEnabled = false;
                    if (C0122O00000o.instance().OOo0O0o()) {
                        this.mActivity.getSensorStateManager().setMagneticFieldUncalibratedEnable(false);
                    }
                }
                this.mCamera2Device.setHDRCheckerEnable(true);
            } else {
                this.mCamera2Device.setHDRCheckerEnable(false);
                this.mHdrCheckEnabled = false;
                if (C0122O00000o.instance().OOo0O0o()) {
                    this.mActivity.getSensorStateManager().setMagneticFieldUncalibratedEnable(false);
                }
                if (this.mMutexModePicker.isHdr()) {
                    resetMutexModeManually();
                }
            }
            this.mCamera2Device.setHDRCheckerStatus(ComponentConfigHdr.getHdrUIStatus(componentValue));
            this.mCamera2Device.setHDRMode(ComponentConfigHdr.getHdrUIStatus(componentValue));
        }
    }

    public void updateHDRTip(boolean z) {
        ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
        boolean z2 = !componentHdr.isEmpty() && componentHdr.isHdrOnWithChecker(componentHdr.getComponentValue(this.mModuleIndex));
        if (triggerHDR(z) && !z2) {
            HdrTrigger hdrTrigger = this.mHdrTrigger;
            if (hdrTrigger == null || hdrTrigger.isUpdateHdrTip()) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("targetHDRState:");
                sb.append(z);
                Log.c(str, sb.toString());
                MagneticSensorDetect magneticSensorDetect = this.mMagneticSensorDetect;
                if (magneticSensorDetect != null && magneticSensorDetect.isLockHDRChecker("updateHDRTip")) {
                    if (this.mCamera2Device.getCameraConfigs().isHDREnabled()) {
                        TopAlert topAlert = this.mTopAlert;
                        if (topAlert != null && !topAlert.isHDRShowing()) {
                            z = true;
                        }
                    }
                    if (!this.mCamera2Device.getCameraConfigs().isHDREnabled()) {
                        TopAlert topAlert2 = this.mTopAlert;
                        if (topAlert2 != null && topAlert2.isHDRShowing()) {
                            z = false;
                        }
                    }
                    return;
                }
                if (this.mAutoHDRTargetState != z) {
                    this.mAutoHDRTargetState = z;
                    if (isAlive()) {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("mAutoHDRTargetState:");
                        sb2.append(this.mAutoHDRTargetState);
                        Log.d(str2, sb2.toString());
                        this.mHandler.post(new C0342O00000oO(this));
                    }
                }
            } else {
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Intercept HDR tip(not tip),tartHDRState:");
                sb3.append(z);
                Log.c(str3, sb3.toString());
            }
        }
    }

    public void updateManualEvAdjust() {
        if (this.mModuleIndex == 167) {
            String manualValue = getManualValue(CameraSettings.KEY_QC_EXPOSURETIME, getString(R.string.pref_camera_exposuretime_default));
            String manualValue2 = getManualValue(CameraSettings.KEY_QC_ISO, getString(R.string.pref_camera_iso_default));
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("MODE_MANUAL: exposureTime = ");
            sb.append(manualValue);
            sb.append("iso = ");
            sb.append(manualValue2);
            Log.d(str, sb.toString());
            boolean z = C0124O00000oO.o00OO00() ? getString(R.string.pref_camera_exposuretime_default).equals(manualValue) : getString(R.string.pref_camera_iso_default).equals(manualValue2) || getString(R.string.pref_camera_exposuretime_default).equals(manualValue);
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new C0351O0000oOo(this, z));
            }
            if (1 == this.mCamera2Device.getFocusMode() && this.m3ALocked) {
                Camera camera = this.mActivity;
                if (camera != null) {
                    camera.runOnUiThread(new O0000o00(this));
                }
                unlockAEAF();
            }
        }
    }

    public void updateMoon(boolean z) {
        String str = "]";
        String str2 = ",";
        if (z) {
            this.mIsMoonMode = true;
            if (!C0122O00000o.instance().OOOOOO()) {
                this.mCamera2Device.setSuperResolution(false);
            }
            updateFocusMode();
            updateHDRPreference();
            this.mCurrentAiScene = 35;
            this.mCamera2Device.setASDScene(35);
            resumePreviewInWorkThread();
            if (this.mZoomSupported) {
                setMaxZoomRatio(Math.max(20.0f, this.mCameraCapabilities.getMaxZoomRatio()));
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("updateMoon(): Override zoom ratio range to: [");
                sb.append(getMinZoomRatio());
                sb.append(str2);
                sb.append(getMaxZoomRatio());
                sb.append(str);
                Log.d(str3, sb.toString());
            }
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null) {
                mainContentProtocol.clearFocusView(1);
            }
        } else if (this.mIsMoonMode) {
            this.mIsMoonMode = false;
            setFocusMode(this.mFocusManager.setFocusMode(CameraSettings.getFocusMode()));
            updateHDRPreference();
            this.mCamera2Device.setASDScene(-35);
            initializeZoomRangeFromCapabilities();
            String str4 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("updateMoon(): Restore zoom ratio range to: [");
            sb2.append(getMinZoomRatio());
            sb2.append(str2);
            sb2.append(getMaxZoomRatio());
            sb2.append(str);
            Log.d(str4, sb2.toString());
            if (getZoomRatio() > getMaxZoomRatio()) {
                onZoomingActionUpdate(getMaxZoomRatio(), -1);
            }
        }
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null) {
            dualController.hideAllPanel();
        }
        String str5 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("(moon_mode) updateMoon,status:");
        sb3.append(z);
        Log.d(str5, sb3.toString());
    }

    public void updateMoonNight() {
        this.mIsMoonMode = false;
        closeMoonMode(10, 0);
        ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).closeMutexElement("e", 193);
        setFlashMode("0");
        updateMfnr(true);
        updateOIS();
        setAiSceneEffect(10);
        this.mCurrentAiScene = 10;
        resumePreviewInWorkThread();
        Log.d(TAG, "(moon_mode) updateMoonNight");
    }

    public void updateNearRangeMode(boolean z, boolean z2) {
        this.mCamera2Device.sendSatFallbackDisableRequest(z, z2);
    }

    public void updateOnTripMode() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            ASDScene[] aSDSceneArr = this.mAsdScenes;
            if (aSDSceneArr != null) {
                camera2Proxy.setOnTripodModeStatus(aSDSceneArr);
            }
        }
    }

    public void updatePreviewSurface() {
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        if (mainContentProtocol != null) {
            mainContentProtocol.initEffectCropView();
        }
        checkDisplayOrientation();
        if (this.mActivity != null) {
            CameraSize cameraSize = this.mPreviewSize;
            if (cameraSize != null) {
                updateCameraScreenNailSize(cameraSize.width, cameraSize.height);
            }
            SurfaceTexture surfaceTexture = this.mActivity.getCameraScreenNail().getSurfaceTexture();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updatePreviewSurface: surfaceTexture = ");
            sb.append(surfaceTexture);
            Log.d(str, sb.toString());
            if (surfaceTexture != null) {
                this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            }
            synchronized (this.mDeviceLock) {
                if (this.mCamera2Device != null) {
                    this.mCamera2Device.updateDeferPreviewSession(new Surface(surfaceTexture));
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateRecordingTime() {
        if (this.mMediaRecorderRecording) {
            CountDownTimer countDownTimer = this.mCountDownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            AnonymousClass28 r1 = new CountDownTimer((long) (this.mMaxVideoDurationInMs + this.mRecordingStartDelay), 1000) {
                public void onFinish() {
                    Camera2Module.this.stopVideoRecording(true, false);
                }

                public void onTick(long j) {
                    String millisecondToTimeString = Util.millisecondToTimeString((j + 950) - 450, false);
                    TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                    if (topAlert != null) {
                        topAlert.updateRecordingTime(millisecondToTimeString);
                    }
                }
            };
            this.mCountDownTimer = r1;
            this.mCountDownTimer.start();
        }
    }

    public void updateSATZooming(boolean z) {
        if (C0122O00000o.instance().OO0o0oO() && HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.setSatIsZooming(z);
                resumePreviewInWorkThread();
            }
        }
    }

    public void updateSilhouette() {
        if (!isDoingAction() && isAlive()) {
            this.isSilhouette = true;
            trackAISceneChanged(this.mModuleIndex, 24);
            setAiSceneEffect(24);
            updateHDR("off");
            this.mCamera2Device.setASDScene(24);
            resumePreviewInWorkThread();
        }
    }
}
