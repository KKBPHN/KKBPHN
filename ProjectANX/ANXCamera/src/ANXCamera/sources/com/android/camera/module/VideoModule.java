package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.location.Location;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.media.CamcorderProfile;
import android.media.CameraProfile;
import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Range;
import android.util.Size;
import android.util.TypedValue;
import android.view.Surface;
import android.widget.Toast;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import com.android.camera.AudioCalculateDecibels;
import com.android.camera.AudioCalculateDecibels.OnVolumeValueListener;
import com.android.camera.AudioManagerAudioDeviceCallback;
import com.android.camera.AudioManagerAudioDeviceCallback.OnAudioDeviceChangeListener;
import com.android.camera.AudioMonitorPlayer;
import com.android.camera.AutoLockManager;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager.CameraExtras;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.FileCompat;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.SettingUiState;
import com.android.camera.SoundSetting;
import com.android.camera.ThermalHelper;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.constant.AutoFocus;
import com.android.camera.constant.FastMotionConstant;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.constant.UpdateConstant.UpdateType;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.DataItemBase;
import com.android.camera.data.data.config.ComponentConfigBokeh;
import com.android.camera.data.data.config.ComponentConfigHdr;
import com.android.camera.data.data.config.ComponentConfigSlowMotion;
import com.android.camera.data.data.config.ComponentRunningMacroMode;
import com.android.camera.data.data.runing.ComponentRunningAiAudio;
import com.android.camera.data.data.runing.ComponentRunningEisPro;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FilterInfo;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.jcodec.MP4UtilEx.VideoTag;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.loader.FunctionParseHistogramStats;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.preferences.CameraSettingPreferences;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.AutoHibernation;
import com.android.camera.protocol.ModeProtocol.AutoZoomModuleProtocol;
import com.android.camera.protocol.ModeProtocol.AutoZoomViewProtocol;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.BluetoothHeadset;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraClickObservable;
import com.android.camera.protocol.ModeProtocol.CameraClickObservable.ClickObserver;
import com.android.camera.protocol.ModeProtocol.ChangeGainProtocol;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.SubtitleRecording;
import com.android.camera.protocol.ModeProtocol.SubtitleRecording.Listener;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.TopConfigProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.MacroAttr;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.VideoAttr;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.statistic.ScenarioTrackUtil;
import com.android.camera.storage.ImageSaver;
import com.android.camera.storage.Storage;
import com.android.camera.ui.ObjectView.ObjectViewListener;
import com.android.camera.ui.PopupManager;
import com.android.camera.ui.RotateTextToast;
import com.android.camera.ui.VideoTagView;
import com.android.camera.ui.zoom.ZoomingAction;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.Camera2Proxy.PictureCallbackWrapper;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CameraConfigs;
import com.android.camera2.CaptureSessionConfigurations;
import com.android.camera2.autozoom.AutoZoomCaptureResult;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.gallery3d.exif.ExifHelper;
import com.miui.extravideo.interpolation.VideoInterpolator;
import com.xiaomi.camera.core.BoostFrameworkImpl;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.rx.CameraSchedulers;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.reflect.Method;
import org.jcodec.containers.mp4.boxes.MsrtBox;
import org.jcodec.containers.mp4.boxes.MtagBox;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class VideoModule extends VideoBase implements AutoZoomModuleProtocol, TopConfigProtocol, ChangeGainProtocol, OnErrorListener, OnInfoListener, PictureCallback, OnVolumeValueListener, ObjectViewListener {
    private static final String GAIN_DEFAULT_VALUE = "50";
    private static final HashMap HEVC_VIDEO_ENCODER_BITRATE = new HashMap();
    private static final int MAX_DURATION_4K = 480000;
    private static final int MAX_DURATION_8K = 360000;
    private static final int RESET_VIDEO_AUTO_FOCUS_TIME = 3000;
    public static final int SCO_OFF = 0;
    public static final int SCO_ON = 1;
    private static final String SETTING_BLUETOOTH_SCO_STATE = "miui_bluetooth_sco_state";
    public static final Size SIZE_1080 = new Size(1920, 1080);
    public static final Size SIZE_720 = new Size(1280, Util.LIMIT_SURFACE_WIDTH);
    private static final long START_OFFSET_MS = 450;
    private static final int VIDEO_HFR_FRAME_RATE_120 = 120;
    private static final int VIDEO_HFR_FRAME_RATE_240 = 240;
    private static final int VIDEO_HFR_FRAME_RATE_480 = 480;
    public static final long VIDEO_MAX_SINGLE_FILE_SIZE = 3670016000L;
    public static final long VIDEO_MIN_SINGLE_FILE_SIZE = Math.min(8388608, Storage.LOW_STORAGE_THRESHOLD);
    private static final int VIDEO_NORMAL_FRAME_RATE = 30;
    private static String mGainVal = "50";
    private AtomicBoolean isAutoZoomTracking = new AtomicBoolean(false);
    private AtomicBoolean isShowOrHideUltraWideHint = new AtomicBoolean(false);
    private boolean mAbandonExposureFilmModeRecord = false;
    private AudioCalculateDecibels mAudioCalculateDecibels;
    private OnAudioDeviceChangeListener mAudioDeviceChangeListener = new O000o(this);
    private AudioManager mAudioManager;
    private AudioManagerAudioDeviceCallback mAudioManagerAudioDeviceCallback;
    private AudioMonitorPlayer mAudioMonitorPlayer;
    private Disposable mAutoZoomDataDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mAutoZoomEmitter;
    private Disposable mAutoZoomUiDisposable;
    /* access modifiers changed from: private */
    public AutoZoomViewProtocol mAutoZoomViewProtocol;
    /* access modifiers changed from: private */
    public BoostFrameworkImpl mBoostFramework = null;
    private ClickObserver mCameraClickObserverAction = new ClickObserver() {
        public void action() {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.alertRecommendDescTip(FragmentTopAlert.TIP_480FPS_960FPS_DESC, 8, R.string.fps960_toast);
            }
        }

        public int getObserver() {
            return 161;
        }
    };
    private boolean mCaptureTimeLapse;
    private CountDownTimer mCountDownTimer;
    private volatile int mCurrentFileNumber;
    private Boolean mDumpOrig960 = null;
    private boolean mEnableVideoSnapshot = false;
    private Disposable mExposureFilmModeRecordDisposable;
    private boolean mFovcEnabled;
    /* access modifiers changed from: private */
    public int mFrameRate;
    private int mFrameRateTrack;
    private int mHfrFPSLower;
    private int mHfrFPSUpper;
    private Disposable mHistogramDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mHistogramEmitter;
    private boolean mIsStopKaraoke = false;
    private boolean mIsSubtitleSupported;
    private boolean mIsVideoTagSupported;
    private final Object mLock = new Object();
    protected MediaRecorder mMediaRecorder;
    /* access modifiers changed from: private */
    public boolean mMediaRecorderPostProcessing;
    private boolean mMediaRecorderWorking;
    private String mNextVideoFileName;
    private ContentValues mNextVideoValues;
    private long mPauseClickTime = 0;
    private volatile boolean mPendingStopRecorder;
    private CamcorderProfile mProfile;
    /* access modifiers changed from: private */
    public int mQuality = 5;
    private boolean mQuickCapture;
    protected Surface mRecorderSurface;
    private String mRecordingSecondTime;
    private String mRecordingTime;
    private boolean mRecordingTimeCountsDown;
    private String mSlowModeFps;
    /* access modifiers changed from: private */
    public boolean mSnapshotInProgress;
    /* access modifiers changed from: private */
    public String mSpeed = "normal";
    private boolean mSplitWhenReachMaxSize;
    /* access modifiers changed from: private */
    public CountDownLatch mStopRecorderDone;
    /* access modifiers changed from: private */
    public SubtitleRecording mSubtitleRecording;
    private String mTemporaryVideoPath;
    private int mTimeBetweenTimeLapseFrameCaptureMs = 0;
    private long mTimeLapseDuration = 0;
    private int mTrackLostCount;
    protected long mVideoRecordTime = 0;
    private long mVideoRecordedDuration;
    private String mVideoTagFileName;

    public final class JpegPictureCallback extends PictureCallbackWrapper {
        Location mLocation;

        public JpegPictureCallback(Location location) {
            this.mLocation = location;
        }

        private void storeImage(byte[] bArr, Location location) {
            byte[] bArr2 = bArr;
            Location location2 = location;
            long currentTimeMillis = System.currentTimeMillis();
            int orientation = ExifHelper.getOrientation(bArr);
            ImageSaver imageSaver = VideoModule.this.mActivity.getImageSaver();
            boolean access$1600 = VideoModule.this.needImageThumbnail(12);
            String createJpegName = Util.createJpegName(currentTimeMillis);
            long currentTimeMillis2 = System.currentTimeMillis();
            CameraSize cameraSize = VideoModule.this.mPictureSize;
            imageSaver.addImage(bArr2, access$1600, createJpegName, null, currentTimeMillis2, null, location2, cameraSize.width, cameraSize.height, null, orientation, false, false, true, false, false, null, null, -1, null);
        }

        public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
            Log.v(VideoBase.TAG, "onPictureTaken");
            VideoModule.this.mSnapshotInProgress = false;
            if (!VideoModule.this.mPaused && bArr != null) {
                Location location = null;
                if (VideoModule.this.mActivity.getCameraIntentManager().checkIntentLocationPermission(VideoModule.this.mActivity)) {
                    location = this.mLocation;
                }
                storeImage(bArr, location);
            }
        }
    }

    static {
        HEVC_VIDEO_ENCODER_BITRATE.put("3840x2160:30", Integer.valueOf(38500000));
        HEVC_VIDEO_ENCODER_BITRATE.put("1920x1080:30", Integer.valueOf(15400000));
        HEVC_VIDEO_ENCODER_BITRATE.put("1280x720:30", Integer.valueOf(10780000));
        HEVC_VIDEO_ENCODER_BITRATE.put("720x480:30", Integer.valueOf(1379840));
    }

    public VideoModule() {
        super(VideoModule.class.getSimpleName());
    }

    /* access modifiers changed from: private */
    public void consumeAutoZoomData(AutoZoomCaptureResult autoZoomCaptureResult) {
        if (isAlive() && !this.mActivity.isActivityPaused() && this.isAutoZoomTracking.get()) {
            this.mAutoZoomViewProtocol.feedData(autoZoomCaptureResult);
        }
    }

    private void countDownForVideoBokeh() {
        if (this.mMediaRecorderRecording) {
            CountDownTimer countDownTimer = this.mCountDownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            AnonymousClass8 r1 = new CountDownTimer(30450, 1000) {
                public void onFinish() {
                    VideoModule.this.stopVideoRecording(false);
                }

                public void onTick(long j) {
                    String millisecondToTimeString = Util.millisecondToTimeString((j + 950) - 450, false);
                    TopAlert topAlert = VideoModule.this.mTopAlert;
                    if (topAlert != null) {
                        topAlert.updateRecordingTime(millisecondToTimeString);
                    }
                }
            };
            this.mCountDownTimer = r1;
            this.mCountDownTimer.start();
        }
    }

    private void forceToNormalMode() {
        ProviderEditor editor = DataRepository.dataItemConfig().editor();
        String str = "normal";
        editor.putString(CameraSettings.KEY_VIDEO_SPEED, str);
        editor.apply();
        this.mSpeed = str;
    }

    private String[] getAIAudioTrackParams() {
        String[] strArr = new String[2];
        if (!C0122O00000o.instance().OO0oO0O()) {
            return null;
        }
        int i = this.mModuleIndex;
        if ((i != 162 && i != 180) || this.mActivity == null) {
            return null;
        }
        ComponentRunningAiAudio componentRunningAiAudio = DataRepository.dataItemRunning().getComponentRunningAiAudio();
        if (componentRunningAiAudio == null) {
            return null;
        }
        strArr[0] = componentRunningAiAudio.getCurrentRecTypeStr(this.mModuleIndex);
        strArr[1] = String.valueOf(getDeviceBasedZoomRatio());
        return strArr;
    }

    private long getFastmotionVideoDuration(String str) {
        ParcelFileDescriptor parcelFileDescriptor;
        if (!Storage.isUseDocumentMode()) {
            return Util.getDuration(str);
        }
        ParcelFileDescriptor parcelFileDescriptor2 = null;
        try {
            parcelFileDescriptor2 = FileCompat.getParcelFileDescriptor(str, false);
            long duration = Util.getDuration(parcelFileDescriptor2.getFileDescriptor());
            Util.closeSafely(parcelFileDescriptor2);
            FileCompat.removeDocumentFileForPath(str);
            return duration;
        } catch (Exception unused) {
            Util.closeSafely(parcelFileDescriptor);
            FileCompat.removeDocumentFileForPath(str);
            return 0;
        } catch (Throwable th) {
            Util.closeSafely(parcelFileDescriptor2);
            FileCompat.removeDocumentFileForPath(str);
            throw th;
        }
    }

    private int getHSRValue() {
        String hSRValue = CameraSettings.getHSRValue(isUltraWideBackCamera());
        if (hSRValue == null || hSRValue.isEmpty() || hSRValue.equals("off")) {
            return 0;
        }
        return Integer.parseInt(hSRValue);
    }

    private int getHevcVideoEncoderBitRate(CamcorderProfile camcorderProfile) {
        StringBuilder sb = new StringBuilder();
        sb.append(camcorderProfile.videoFrameWidth);
        sb.append("x");
        sb.append(camcorderProfile.videoFrameHeight);
        sb.append(":");
        sb.append(camcorderProfile.videoFrameRate);
        String sb2 = sb.toString();
        if (HEVC_VIDEO_ENCODER_BITRATE.containsKey(sb2)) {
            return ((Integer) HEVC_VIDEO_ENCODER_BITRATE.get(sb2)).intValue();
        }
        String str = VideoBase.TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("no pre-defined bitrate for ");
        sb3.append(sb2);
        Log.d(str, sb3.toString());
        return camcorderProfile.videoBitRate;
    }

    private String getManualValue(String str, String str2) {
        return (ModuleManager.isProVideoModule() || ModuleManager.isFastmotionModulePro()) ? CameraSettingPreferences.instance().getString(str, str2) : str2;
    }

    private long getRecorderMaxFileSize(int i) {
        long leftSpace = Storage.getLeftSpace() - Storage.LOW_STORAGE_THRESHOLD;
        if (i > 0) {
            long j = (long) i;
            if (j < leftSpace) {
                leftSpace = j;
            }
        }
        long j2 = VIDEO_MAX_SINGLE_FILE_SIZE;
        if (leftSpace <= VIDEO_MAX_SINGLE_FILE_SIZE || !C0122O00000o.instance().OO0o0O0()) {
            j2 = VIDEO_MIN_SINGLE_FILE_SIZE;
            if (leftSpace >= j2) {
                j2 = leftSpace;
            }
        }
        long j3 = this.mIntentRequestSize;
        return (j3 <= 0 || j3 >= j2) ? j2 : j3;
    }

    private int getRecorderOrientationHint() {
        int sensorOrientation = this.mCameraCapabilities.getSensorOrientation();
        if (this.mOrientation == -1) {
            return sensorOrientation;
        }
        boolean isFrontCamera = isFrontCamera();
        int i = this.mOrientation;
        return (isFrontCamera ? (sensorOrientation - i) + m.cQ : sensorOrientation + i) % m.cQ;
    }

    private float getResourceFloat(int i, float f) {
        TypedValue typedValue = new TypedValue();
        try {
            this.mActivity.getResources().getValue(i, typedValue, true);
            return typedValue.getFloat();
        } catch (Exception unused) {
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Missing resource ");
            sb.append(Integer.toHexString(i));
            Log.e(str, sb.toString());
            return f;
        }
    }

    private long getSpeedRecordVideoLength(long j, double d) {
        if (d == 0.0d) {
            return 0;
        }
        return (long) (((((double) j) / d) / ((double) getNormalVideoFrameRate())) * 1000.0d);
    }

    private void handleAiAudioTipsState(boolean z) {
        if (C0122O00000o.instance().OO0oO0O()) {
            int i = this.mModuleIndex;
            if ((i == 162 || i == 180) && !isFrontCamera()) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                int currentStringRes = DataRepository.dataItemRunning().getComponentRunningAiAudio().getCurrentStringRes(this.mModuleIndex);
                if (!(topAlert == null || currentStringRes == -1)) {
                    if (!Util.isWiredHeadsetOn()) {
                        topAlert.alertAiAudio(z ? 0 : 8, currentStringRes);
                    } else if (z) {
                        topAlert.alertAiAudioMutexToastIfNeed(getActivity(), currentStringRes);
                    }
                }
            }
        }
    }

    private void handleExposureFilmModeRecord() {
        if (CameraSettings.VIDEO_MODE_FILM_EXPOSUREDELAY.equals(this.mSpeed)) {
            Disposable disposable = this.mExposureFilmModeRecordDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                this.mExposureFilmModeRecordDisposable.dispose();
                this.mExposureFilmModeRecordDisposable = null;
            }
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                if (!this.mAbandonExposureFilmModeRecord) {
                    recordState.onPostSavingStart();
                } else {
                    recordState.onFinish();
                    deleteVideoFile(this.mCurrentVideoFilename);
                    this.mCurrentVideoFilename = null;
                }
            }
        }
    }

    private void hideHint() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertMacroModeHint(8, DataRepository.dataItemRunning().getComponentRunningMacroMode().getResText());
            topAlert.alertAiAudioBGHint(8, R.string.pref_camera_rec_type_audio_zoom);
            topAlert.alertAiEnhancedVideoHint(8, R.string.pref_camera_video_ai_scene_title);
            topAlert.alertSwitchTip(FragmentTopAlert.TIP_SUPER_EIS, 8, (int) R.string.super_eis);
            String str = FragmentTopAlert.TIP_VIDEO_BEAUTIFY;
            topAlert.alertSwitchTip(str, 8, (int) R.string.video_beauty_tip_beautification);
            topAlert.alertSwitchTip(str, 8, (int) R.string.video_beauty_tip);
            topAlert.hideSwitchTip();
        }
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
            bottomPopupTips.directlyHideTipsForce();
        }
    }

    private void initAutoZoom() {
        if (C0122O00000o.instance().OO0oOOo()) {
            if (CameraSettings.isAutoZoomEnabled(this.mModuleIndex)) {
                startAutoZoom();
            } else {
                stopAutoZoom();
            }
            this.mAutoZoomDataDisposable = Flowable.create(new FlowableOnSubscribe() {
                public void subscribe(FlowableEmitter flowableEmitter) {
                    VideoModule.this.mAutoZoomEmitter = flowableEmitter;
                }
            }, BackpressureStrategy.DROP).observeOn(CameraSchedulers.sCameraSetupScheduler).map(new Function() {
                public AutoZoomCaptureResult apply(CaptureResult captureResult) {
                    return new AutoZoomCaptureResult(captureResult);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new Consumer() {
                public void accept(AutoZoomCaptureResult autoZoomCaptureResult) {
                    VideoModule.this.consumeAutoZoomData(autoZoomCaptureResult);
                }
            });
        }
    }

    private void initHistogramEmitter() {
        if (this.mModuleIndex == 180 && this.mCameraCapabilities.isSupportHistogram()) {
            this.mHistogramDisposable = Flowable.create(new FlowableOnSubscribe() {
                public void subscribe(FlowableEmitter flowableEmitter) {
                    VideoModule.this.mHistogramEmitter = flowableEmitter;
                }
            }, BackpressureStrategy.DROP).observeOn(CameraSchedulers.sCameraSetupScheduler).map(new FunctionParseHistogramStats(this, this.mTopAlert)).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new Consumer() {
                public void accept(int[] iArr) {
                    VideoModule videoModule = VideoModule.this;
                    if (videoModule.mMainProtocol != null) {
                        videoModule.mTopAlert.refreshHistogramStatsView();
                    }
                }
            });
        }
    }

    private boolean initializeObjectTrack(RectF rectF, boolean z) {
        mapTapCoordinate(rectF);
        stopObjectTracking(false);
        return this.mMainProtocol.initializeObjectTrack(rectF, z);
    }

    private static boolean is4K30FpsEISSupported() {
        return C0122O00000o.instance().is4K30FpsEISSupported();
    }

    private boolean is4K60FpsEISSupported() {
        return this.mCameraCapabilities.is4K60FpsEISSupported();
    }

    /* access modifiers changed from: private */
    public boolean isActivityResumed() {
        Camera activity = getActivity();
        return activity != null && !activity.isActivityPaused();
    }

    private boolean isDump960Orig() {
        if (this.mDumpOrig960 == null) {
            this.mDumpOrig960 = SystemProperties.getBoolean("camera.record.960origdump", false) ? Boolean.TRUE : Boolean.FALSE;
        }
        return this.mDumpOrig960.booleanValue();
    }

    private boolean isEisOn() {
        String str;
        String str2;
        if (!C0124O00000oO.Oo00o0()) {
            return false;
        }
        if (CameraSettings.isMacroModeEnabled(this.mModuleIndex) && isUltraWideBackCamera()) {
            return true;
        }
        int i = this.mModuleIndex;
        if (i == 208 || i == 207 || this.mActualCameraId == Camera2DataContainer.getInstance().getStandaloneMacroCameraId()) {
            return false;
        }
        if (CameraSettings.isAiEnhancedVideoEnabled(this.mModuleIndex)) {
            str = VideoBase.TAG;
            str2 = "ai enhanced video enable EIS";
        } else if (CameraSettings.isAutoZoomEnabled(this.mModuleIndex)) {
            str = VideoBase.TAG;
            str2 = "videoStabilization: disabled EIS and OIS when AutoZoom is opened";
        } else if (CameraSettings.isSuperEISEnabled(this.mModuleIndex) || CameraSettings.isVideoEisBeautyMeanwhileEnable()) {
            return true;
        } else {
            if ((CameraSettings.isVhdrOn(this.mCameraCapabilities, this.mModuleIndex) && isFrontCamera()) || !CameraSettings.isMovieSolidOn()) {
                return false;
            }
            if ((!isNormalMode() && !isFastMode()) || needChooseVideoBeauty(this.mBeautyValues) || this.mQuality == 0) {
                return false;
            }
            int i2 = 60;
            boolean z = getHSRValue() == 60;
            CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
            int i3 = this.mQuality;
            if (!z) {
                i2 = 30;
            }
            if (cameraCapabilities.isCurrentQualitySupportEis(i3, i2)) {
                str = VideoBase.TAG;
                str2 = "isEisOn, current quality support eis";
            } else if (is8KCamcorder()) {
                return CameraSettings.isCurrentQualitySupportEis(Integer.parseInt("3001"), 24, this.mCameraCapabilities) && CameraSettings.isMovieSolidOn();
            } else {
                if (z) {
                    if (!is4K60FpsEISSupported()) {
                        return false;
                    }
                } else if (CameraSettings.is4KHigherVideoQuality(this.mQuality) && !is4K30FpsEISSupported()) {
                    return false;
                }
                return C0124O00000oO.OOooOoO() || !isFrontCamera();
            }
        }
        Log.d(str, str2);
        return true;
    }

    private boolean isEnableScreenShot() {
        boolean z;
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities != null && cameraCapabilities.isSupportVideoBeautyScreenshot()) {
            int i = this.mModuleIndex;
            if ((i == 162 || i == 180) && (needChooseVideoBeauty(this.mBeautyValues) || CameraSettings.isAiEnhancedVideoEnabled(this.mModuleIndex))) {
                z = true;
                String str = VideoBase.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("isEnableScreenShot: ");
                sb.append(z);
                Log.d(str, sb.toString());
                return z;
            }
        }
        z = false;
        String str2 = VideoBase.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isEnableScreenShot: ");
        sb2.append(z);
        Log.d(str2, sb2.toString());
        return z;
    }

    /* access modifiers changed from: private */
    public boolean isFPS120() {
        return ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_120.equals(this.mSlowModeFps);
    }

    private boolean isFPS1920() {
        return ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_1920.equals(this.mSlowModeFps);
    }

    /* access modifiers changed from: private */
    public boolean isFPS240() {
        return ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_240.equals(this.mSlowModeFps);
    }

    /* access modifiers changed from: private */
    public boolean isFPS480() {
        return ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_480.equals(this.mSlowModeFps);
    }

    private boolean isFPS480Direct() {
        return ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_480_DIRECT.equals(this.mSlowModeFps);
    }

    private boolean isFPS960() {
        return ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_960.equals(this.mSlowModeFps);
    }

    private boolean isFastMode() {
        return CameraSettings.VIDEO_SPEED_FAST.equals(this.mSpeed);
    }

    private boolean isNormalMode() {
        return "normal".equals(this.mSpeed);
    }

    private boolean isSplitWhenReachMaxSize() {
        return this.mSplitWhenReachMaxSize;
    }

    private boolean needDisableEISAndOIS() {
        String str;
        String str2;
        if (!CameraSettings.isVideoBokehOn() || !isFrontCamera()) {
            if (CameraSettings.isHdr10PlusVideoModeOn()) {
                Camera2Proxy camera2Proxy = this.mCamera2Device;
                if (camera2Proxy != null && camera2Proxy.getCameraConfigs().isVideoHdrEnable()) {
                    str = VideoBase.TAG;
                    str2 = "videoStabilization: disabled EIS and OIS when HDR10+ and HDR are opened at the same time.";
                }
            }
            return false;
        }
        str = VideoBase.TAG;
        str2 = "videoStabilization: disabled EIS and OIS when VIDEO_BOKEH is opened";
        Log.d(str, str2);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean needImageThumbnail(int i) {
        return i != 12;
    }

    private void notifyAutoZoomStartUiHint() {
        notifyAutoZoomStopUiHint();
        TopAlert topAlert = this.mTopAlert;
        if (topAlert == null || !topAlert.isExtraMenuShowing()) {
            this.mAutoZoomUiDisposable = Observable.timer(800, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new Consumer() {
                public void accept(Long l) {
                    TopAlert topAlert = VideoModule.this.mTopAlert;
                    if (topAlert != null) {
                        topAlert.alertAiDetectTipHint(0, R.string.autozoom_click_hint, -1);
                    }
                }
            });
        }
    }

    private void notifyAutoZoomStopUiHint() {
        Disposable disposable = this.mAutoZoomUiDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mAutoZoomUiDisposable.dispose();
            this.mAutoZoomUiDisposable = null;
        }
    }

    private void onMaxFileSizeReached() {
        String str = this.mCurrentVideoFilename;
        if (str != null) {
            saveVideo(str, this.mCurrentVideoValues, false, false);
            this.mCurrentVideoValues = null;
            this.mCurrentVideoFilename = null;
        }
    }

    private void onMediaRecorderReleased() {
        Log.d(VideoBase.TAG, "onMediaRecorderReleased>>");
        long currentTimeMillis = System.currentTimeMillis();
        setCurrentAiAudioParameters(false);
        restoreOuterAudio();
        handleExposureFilmModeRecord();
        if (isCaptureIntent() && !this.mPaused) {
            if (this.mCurrentVideoUri == null) {
                String str = this.mCurrentVideoFilename;
                if (str != null) {
                    this.mCurrentVideoUri = saveVideo(str, this.mCurrentVideoValues, true, true);
                    String str2 = VideoBase.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onMediaRecorderReleased: outputUri=");
                    sb.append(this.mCurrentVideoUri);
                    Log.d(str2, sb.toString());
                }
            }
            boolean z = this.mCurrentVideoUri != null;
            if (this.mQuickCapture) {
                doReturnToCaller(z);
            } else if (z && !getActivity().isActivityPaused()) {
                showAlert();
            }
        }
        if (this.mCurrentVideoFilename != null) {
            if (!isCaptureIntent()) {
                if (this.mModuleIndex == 169 && getFastmotionVideoDuration(this.mCurrentVideoFilename) == 0) {
                    ToastUtils.showToast((Context) this.mActivity, (int) R.string.mimoji_gif_record_time_short, 80);
                }
                saveVideo(this.mCurrentVideoFilename, this.mCurrentVideoValues, true, false);
            }
            this.mCurrentVideoFilename = null;
            this.mCurrentVideoValues = null;
        } else if (!this.mPaused && !this.mActivity.isActivityPaused()) {
            this.mActivity.getThumbnailUpdater().getLastThumbnail();
        }
        if (this.mMediaRecorderPostProcessing) {
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onPostSavingFinish();
            }
        }
        this.mActivity.sendBroadcast(new Intent(BaseModule.STOP_VIDEO_RECORDING_ACTION));
        listenPhoneState(false);
        if (this.mModuleIndex != 208) {
            enableCameraControls(true);
        }
        String str3 = this.mVideoFocusMode;
        String str4 = AutoFocus.LEGACY_CONTINUOUS_VIDEO;
        if (!str4.equals(str3)) {
            this.mFocusManager.resetFocusStateIfNeeded();
            if (!this.mPaused && !this.mActivity.isActivityPaused()) {
                this.mMainProtocol.clearFocusView(2);
                setVideoFocusMode(str4, false);
                updatePreferenceInWorkThread(14);
            }
        }
        keepScreenOnAwhile();
        String str5 = VideoBase.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onMediaRecorderReleased<<time=");
        sb2.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(str5, sb2.toString());
        ScenarioTrackUtil.trackStopVideoRecordEnd();
        doLaterReleaseIfNeed();
        if (this.mMediaRecorderPostProcessing) {
            this.mMediaRecorderPostProcessing = false;
        }
        this.mMediaRecorderWorking = false;
        this.mHandler.post(new Runnable() {
            public void run() {
                VideoModule.this.handlePendingScreenSlide();
            }
        });
    }

    private void onStartRecorderExposureFilmMode() {
        if (CameraSettings.VIDEO_MODE_FILM_EXPOSUREDELAY.equals(this.mSpeed)) {
            this.mAbandonExposureFilmModeRecord = true;
            this.mExposureFilmModeRecordDisposable = Observable.just(Integer.valueOf(5000)).delaySubscription(5000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new C0382O000oO0(this));
        }
    }

    private void onStartRecorderFail() {
        Log.k(5, VideoBase.TAG, "onStartRecorderFail");
        enableCameraControls(true);
        releaseMediaRecorder();
        restoreOuterAudio();
        ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).onFailed();
        updateVideoTagState(4);
    }

    private void onStartRecorderSucceed() {
        float min;
        float ultraTeleMinZoomRatio;
        Log.k(3, VideoBase.TAG, "onStartRecorderSucceed");
        if (!CameraSettings.isAlgoFPS(this.mModuleIndex)) {
            enableCameraControls(true);
        }
        this.mActivity.sendBroadcast(new Intent(BaseModule.START_VIDEO_RECORDING_ACTION));
        this.mMediaRecorderWorking = true;
        this.mMediaRecorderRecording = true;
        setCurrentAiAudioZoomLv();
        hideHint();
        if (this.mIsSubtitleSupported) {
            SubtitleRecording subtitleRecording = this.mSubtitleRecording;
            if (subtitleRecording != null) {
                subtitleRecording.handleSubtitleRecordingStart();
            }
        }
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        if (mainContentProtocol != null) {
            this.mVideoTagFileName = this.mCurrentVideoFilename;
            mainContentProtocol.processingStart(this.mIsVideoTagSupported ? this.mVideoTagFileName : null);
        }
        handleAiAudioTipsState(true);
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            int i = this.mModuleIndex;
            if (i != 172 && i != 180 && !CameraSettings.isMacroModeEnabled(i) && !CameraSettings.isAutoZoomEnabled(this.mModuleIndex) && !CameraSettings.isSuperEISEnabled(this.mModuleIndex) && isBackCamera()) {
                updateZoomRatioToggleButtonState(true);
                if (!isStandaloneMacroCamera()) {
                    if (!isUltraWideBackCamera()) {
                        if (isAuxCamera()) {
                            ultraTeleMinZoomRatio = HybridZoomingSystem.getTeleMinZoomRatio();
                        } else if (isUltraTeleCamera()) {
                            ultraTeleMinZoomRatio = HybridZoomingSystem.getUltraTeleMinZoomRatio();
                        } else {
                            if (C0122O00000o.instance().OOoOo()) {
                                if (isInVideoSAT()) {
                                    setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                                    min = C0122O00000o.instance().O0oooO0();
                                } else if (isStandaloneMacroCamera()) {
                                    setMinZoomRatio(1.0f);
                                    min = Math.min(2.0f, this.mCameraCapabilities.getMaxZoomRatio());
                                }
                                setMaxZoomRatio(min);
                            }
                            setMinZoomRatio(1.0f);
                            min = Math.min(6.0f, this.mCameraCapabilities.getMaxZoomRatio());
                            setMaxZoomRatio(min);
                        }
                        setMinZoomRatio(ultraTeleMinZoomRatio);
                        setVideoMaxZoomRatioByTele();
                    } else if (!CameraSettings.isMacroModeEnabled(this.mModuleIndex)) {
                        setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                        setMaxZoomRatio(2.0f);
                    }
                }
                setMinZoomRatio(HybridZoomingSystem.getMinimumMacroOpticalZoomRatio());
                min = HybridZoomingSystem.getMaximumMacroOpticalZoomRatio();
                setMaxZoomRatio(min);
            }
        }
        this.mMainProtocol.updateZoomRatio(getMinZoomRatio(), getMaxZoomRatio());
        this.mMediaRecorderRecordingPaused = false;
        this.mRecordingStartTime = SystemClock.uptimeMillis();
        this.mPauseClickTime = SystemClock.uptimeMillis();
        this.mRecordingTime = "";
        listenPhoneState(true);
        if (CameraSettings.isVideoBokehOn()) {
            countDownForVideoBokeh();
        } else {
            updateRecordingTime();
        }
        keepScreenOn();
        AutoLockManager.getInstance(this.mActivity).removeMessage();
        HashMap hashMap = new HashMap();
        hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(this.m3ALocked));
        if (this.mModuleIndex == 214) {
            CameraStatUtils.trackCaptureSuperNightVideo(hashMap);
        } else {
            trackGeneralInfo(hashMap, 1, false, this.mBeautyValues, false, 0);
        }
        if (this.mCaptureTimeLapse && (C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo())) {
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                configChanges.reCheckFastMotion(true);
            }
        }
        if (this.mModuleIndex == 180) {
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null) {
                topAlert.updateProVideoRecordingSimpleView(true);
            }
        }
        keepPowerSave();
        if (this.mIsAutoHibernationSupported) {
            keepAutoHibernation();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x010a, code lost:
        if (r9 == false) goto L_0x010c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x010c, code lost:
        com.android.camera.log.Log.e(com.android.camera.module.VideoBase.TAG, r1);
        r4.delete();
        r5.delete();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0120, code lost:
        if (0 != 0) goto L_0x0123;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0123, code lost:
        if (r9 == false) goto L_0x0129;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0125, code lost:
        r2 = r6.getAbsolutePath();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0129, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String postProcessVideo(String str) {
        int i;
        int i2;
        int i3;
        String str2 = str;
        String str3 = "960fps processing failed. delete the files.";
        String str4 = null;
        if (str2 == null) {
            return null;
        }
        String str5 = this.mSlowModeFps;
        char c = 65535;
        int hashCode = str5.hashCode();
        boolean z = false;
        if (hashCode != -1299788783) {
            if (hashCode != -1150304479) {
                if (hashCode == -1150299736 && str5.equals(ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_960)) {
                    c = 2;
                }
            } else if (str5.equals(ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_480)) {
                c = 0;
            }
        } else if (str5.equals(ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_1920)) {
            c = 3;
        }
        int i4 = VIDEO_HFR_FRAME_RATE_480;
        if (c != 0) {
            if (c != 3) {
                i4 = 240;
                i3 = 960;
            } else {
                i3 = 1920;
            }
            i2 = i4;
            i = i3;
        } else {
            i = VIDEO_HFR_FRAME_RATE_480;
            i2 = 120;
        }
        File file = new File(str2);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(".bak");
        File file2 = new File(sb.toString());
        File file3 = Storage.isUseDocumentMode() ? new File(Storage.generatePrimaryFilepath(file.getName())) : new File(Storage.generateFilepath(file.getName()));
        try {
            boolean z2 = CameraSettings.isSupportSlowMotionVideoEditor();
            boolean OO0ooO0 = C0122O00000o.instance().OO0ooO0();
            String str6 = VideoBase.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("postProcessVideo: start srcFPS:");
            sb2.append(i2);
            sb2.append(" dstFPS:");
            sb2.append(i);
            sb2.append(" supportDeFlicker:");
            sb2.append(OO0ooO0);
            Log.k(3, str6, sb2.toString());
            boolean doDecodeAndEncodeSync = VideoInterpolator.doDecodeAndEncodeSync(i2, i, file.getAbsolutePath(), file2.getAbsolutePath(), OO0ooO0, z2);
            Log.k(3, VideoBase.TAG, "postProcessVideo: end ");
            if (doDecodeAndEncodeSync && file2.renameTo(file3)) {
                z = true;
            }
            if (isDump960Orig()) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str2);
                sb3.append(".orig.mp4");
                file.renameTo(new File(sb3.toString()));
            } else {
                file.delete();
            }
        } catch (Throwable th) {
            if (0 == 0) {
                Log.e(VideoBase.TAG, str3);
                file.delete();
                file2.delete();
            }
            throw th;
        }
    }

    private void releaseResources() {
        FlowableEmitter flowableEmitter = this.mAutoZoomEmitter;
        if (flowableEmitter != null) {
            flowableEmitter.onComplete();
        }
        Disposable disposable = this.mAutoZoomUiDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mAutoZoomUiDisposable.dispose();
            this.mAutoZoomUiDisposable = null;
        }
        Disposable disposable2 = this.mAutoZoomDataDisposable;
        if (disposable2 != null && !disposable2.isDisposed()) {
            this.mAutoZoomDataDisposable.dispose();
            this.mAutoZoomDataDisposable = null;
        }
        FlowableEmitter flowableEmitter2 = this.mHistogramEmitter;
        if (flowableEmitter2 != null) {
            flowableEmitter2.onComplete();
        }
        Disposable disposable3 = this.mHistogramDisposable;
        if (disposable3 != null && !disposable3.isDisposed()) {
            this.mHistogramDisposable.dispose();
            this.mHistogramDisposable = null;
        }
        stopTracking(0);
        stopAutoZoom();
        this.mAudioCalculateDecibels.release();
        closeCamera();
        releaseMediaRecorder();
        handleTempVideoFile();
    }

    private Uri saveVideo(String str, ContentValues contentValues, boolean z, boolean z2) {
        if (this.mActivity != null) {
            String str2 = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("saveVideo: path=");
            sb.append(str);
            sb.append(" isFinal=");
            sb.append(z);
            sb.append(", isSync= ");
            sb.append(z2);
            Log.k(5, str2, sb.toString());
            contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
            if (z2) {
                return this.mActivity.getImageSaver().addVideoSync(str, contentValues, false);
            }
            applyTags(new C0385O000oO0o(this, str, contentValues, z));
        } else {
            String str3 = VideoBase.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("saveVideo: failed to save ");
            sb2.append(str);
            Log.k(5, str3, sb2.toString());
        }
        return null;
    }

    private void setCurrentAiAudioParameters(boolean z) {
        if (C0122O00000o.instance().OO0oO0O()) {
            ComponentRunningAiAudio componentRunningAiAudio = DataRepository.dataItemRunning().getComponentRunningAiAudio();
            if (componentRunningAiAudio != null) {
                String currentParameters = componentRunningAiAudio.getCurrentParameters(this.mModuleIndex, z, this.mOrientation);
                String str = VideoBase.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("setCurrentAiAudioParameters.parameters = ");
                sb.append(currentParameters);
                Log.d(str, sb.toString());
                AudioSystem.setParameters(currentParameters);
            }
        }
    }

    private void setCurrentAiAudioZoomLv() {
        if (C0122O00000o.instance().OO0oO0O() && !Util.isWiredHeadsetOn()) {
            int i = this.mModuleIndex;
            if ((i == 162 || i == 180) && !isFrontCamera() && !CameraSettings.isMacroLensOn(this.mModuleIndex) && DataRepository.dataItemRunning().getComponentRunningAiAudio().getCurrentRecType(this.mModuleIndex) != 1) {
                if (this.mMediaRecorderRecording || this.mMediaRecorderRecordingPaused) {
                    double deviceBasedZoomRatio = (double) getDeviceBasedZoomRatio();
                    StringBuilder sb = new StringBuilder();
                    sb.append("AURISYS_SET_PARAM,DSP,RECORD,FV_SPH,KEY_VALUE,audioZoom,ZoomLv@");
                    sb.append(Util.getEnglishDecimalFormat().format(deviceBasedZoomRatio));
                    sb.append("/=SET");
                    String sb2 = sb.toString();
                    String str = VideoBase.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("setCurrentAiAudioZoomLv.parameters = ");
                    sb3.append(sb2);
                    Log.d(str, sb3.toString());
                    AudioSystem.setParameters(sb2);
                }
            }
        }
    }

    private void setJpegQuality() {
        if (isDeviceAlive()) {
            int jpegEncodingQualityParameter = CameraProfile.getJpegEncodingQualityParameter(this.mBogusCameraId, 2);
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("jpegQuality=");
            sb.append(jpegEncodingQualityParameter);
            Log.d(str, sb.toString());
            this.mCamera2Device.setJpegQuality(jpegEncodingQualityParameter);
        }
    }

    private boolean setNextOutputFile(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.w(VideoBase.TAG, "setNextOutputFile, filePath is empty");
            return false;
        } else if (!Storage.isUseDocumentMode()) {
            return CompatibilityUtils.setNextOutputFile(this.mMediaRecorder, str);
        } else {
            ParcelFileDescriptor parcelFileDescriptor = null;
            try {
                parcelFileDescriptor = FileCompat.getParcelFileDescriptor(str, true);
                return CompatibilityUtils.setNextOutputFile(this.mMediaRecorder, parcelFileDescriptor.getFileDescriptor());
            } catch (Exception e) {
                String str2 = VideoBase.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("open file failed, filePath ");
                sb.append(str);
                Log.d(str2, sb.toString(), (Throwable) e);
                return false;
            } finally {
                Util.closeSafely(parcelFileDescriptor);
            }
        }
    }

    private void setParameterExtra(MediaRecorder mediaRecorder, String str) {
        Class[] clsArr = {MediaRecorder.class};
        Method method = Util.getMethod(clsArr, "setParameter", "(Ljava/lang/String;)V");
        if (method != null) {
            method.invoke(clsArr[0], mediaRecorder, str);
        }
    }

    private void setSplitWhenReachMaxSize(boolean z) {
        this.mSplitWhenReachMaxSize = z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:68:0x0250  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x025a  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x027c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setupRecorder(MediaRecorder mediaRecorder) {
        StringBuilder sb;
        String str;
        int i;
        Location location;
        long recorderMaxFileSize;
        double d;
        String str2;
        StringBuilder sb2;
        String str3;
        String str4;
        StringBuilder sb3;
        String str5;
        boolean isNormalMode = isNormalMode();
        boolean z = isNormalMode || ((isFPS120() || isFPS240() || isFPS480Direct()) && !C0122O00000o.instance().OO00ooO());
        mediaRecorder.setVideoSource(2);
        BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
        if (bluetoothHeadset != null && bluetoothHeadset.isSupportBluetoothSco(getModuleIndex())) {
            mediaRecorder.setAudioSource(0);
        } else if (z) {
            mediaRecorder.setAudioSource(5);
        }
        mediaRecorder.setOutputFormat(this.mProfile.fileFormat);
        mediaRecorder.setVideoEncoder(this.mProfile.videoCodec);
        CamcorderProfile camcorderProfile = this.mProfile;
        mediaRecorder.setVideoSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
        int hSRValue = getHSRValue();
        String str6 = "setVideoFrameRate: ";
        if (hSRValue > 0) {
            mediaRecorder.setVideoFrameRate(hSRValue);
            this.mFrameRateTrack = hSRValue;
            str = VideoBase.TAG;
            sb = new StringBuilder();
            sb.append(str6);
            sb.append(hSRValue);
        } else {
            mediaRecorder.setVideoFrameRate(this.mProfile.videoFrameRate);
            this.mFrameRateTrack = this.mProfile.videoFrameRate;
            str = VideoBase.TAG;
            sb = new StringBuilder();
            sb.append(str6);
            sb.append(this.mProfile.videoFrameRate);
        }
        Log.d(str, sb.toString());
        CamcorderProfile camcorderProfile2 = this.mProfile;
        if (5 == camcorderProfile2.videoCodec) {
            i = getHevcVideoEncoderBitRate(camcorderProfile2);
            String str7 = VideoBase.TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("H265 bitrate: ");
            sb4.append(i);
            Log.d(str7, sb4.toString());
            int i2 = !is8KCamcorder() ? 262144 : 1048576;
            if (CameraSettings.isHdr10Alive(this.mModuleIndex)) {
                this.mMediaRecorder.setVideoEncodingProfileLevel(4096, i2);
                str4 = VideoBase.TAG;
                sb3 = new StringBuilder();
                str5 = "setupRecorder: HEVCProfileMain10HDR10 & ";
            } else if (CameraSettings.isHdr10PlusAlive(this.mModuleIndex)) {
                this.mMediaRecorder.setVideoEncodingProfileLevel(2, i2);
                str4 = VideoBase.TAG;
                sb3 = new StringBuilder();
                str5 = "setupRecorder: HEVCProfileMain10 & ";
            }
            sb3.append(str5);
            sb3.append(i2);
            Log.d(str4, sb3.toString());
        } else {
            i = camcorderProfile2.videoBitRate;
            String str8 = VideoBase.TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("H264 bitrate: ");
            sb5.append(i);
            Log.d(str8, sb5.toString());
        }
        mediaRecorder.setVideoEncodingBitRate(i);
        if (z) {
            mediaRecorder.setAudioEncodingBitRate(this.mProfile.audioBitRate);
            mediaRecorder.setAudioChannels(this.mProfile.audioChannels);
            mediaRecorder.setAudioSamplingRate(this.mProfile.audioSampleRate);
            mediaRecorder.setAudioEncoder(this.mProfile.audioCodec);
        }
        if (this.mCaptureTimeLapse) {
            String str9 = " mTimeLapseDuration ";
            if (this.mModuleIndex == 208) {
                this.mTimeBetweenTimeLapseFrameCaptureMs = Integer.parseInt(FastMotionConstant.FAST_MOTION_SPEED_300X);
                this.mTimeLapseDuration = (long) (Integer.parseInt(DataRepository.dataItemRunning().getComponentRunningFastMotionDuration().getDefaultValue(160)) * 60 * 1000);
                str2 = VideoBase.TAG;
                sb2 = new StringBuilder();
                str3 = "MODE_FILM_EXPOSUREDELAY setupRecorder ";
            } else {
                if (C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo()) {
                    this.mTimeBetweenTimeLapseFrameCaptureMs = Integer.parseInt(DataRepository.dataItemRunning().getString(CameraSettings.KEY_NEW_VIDEO_TIME_LAPSE_FRAME_INTERVAL, DataRepository.dataItemRunning().getComponentRunningFastMotionSpeed().getDefaultValue(160)));
                    this.mTimeLapseDuration = (long) (Integer.parseInt(DataRepository.dataItemRunning().getString(CameraSettings.KEY_NEW_VIDEO_TIME_LAPSE_DURATION, DataRepository.dataItemRunning().getComponentRunningFastMotionDuration().getDefaultValue(160))) * 60 * 1000);
                    str2 = VideoBase.TAG;
                    sb2 = new StringBuilder();
                    str3 = "setupRecorder ";
                }
                d = 1000.0d / ((double) this.mTimeBetweenTimeLapseFrameCaptureMs);
            }
            sb2.append(str3);
            sb2.append(this.mTimeBetweenTimeLapseFrameCaptureMs);
            sb2.append(str9);
            sb2.append(this.mTimeLapseDuration);
            Log.i(str2, sb2.toString());
            d = 1000.0d / ((double) this.mTimeBetweenTimeLapseFrameCaptureMs);
        } else if (!isNormalMode) {
            if (ModuleManager.isVideoNewSlowMotion() && !C0122O00000o.instance().OO00ooO()) {
                mediaRecorder.setVideoFrameRate(this.mFrameRate);
                mediaRecorder.setVideoEncodingBitRate(VERSION.SDK_INT < 28 ? (int) ((((long) i) * ((long) this.mFrameRate)) / ((long) getNormalVideoFrameRate())) : ((this.mFrameRate / getNormalVideoFrameRate()) / 2) * i);
            }
            d = (double) this.mFrameRate;
        } else {
            if (hSRValue > 0) {
                mediaRecorder.setVideoFrameRate(hSRValue);
                d = (double) hSRValue;
            }
            mediaRecorder.setMaxDuration(this.mMaxVideoDurationInMs);
            location = null;
            if (this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity)) {
                location = LocationManager.instance().getCurrentLocation();
            }
            if (location != null) {
                mediaRecorder.setLocation((float) location.getLatitude(), (float) location.getLongitude());
            }
            int i3 = SystemProperties.getInt("camera.debug.video_max_size", 0);
            recorderMaxFileSize = getRecorderMaxFileSize(i3);
            if (recorderMaxFileSize > 0) {
                String str10 = VideoBase.TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("maxFileSize=");
                sb6.append(recorderMaxFileSize);
                Log.v(str10, sb6.toString());
                mediaRecorder.setMaxFileSize(recorderMaxFileSize);
                if (recorderMaxFileSize > VIDEO_MAX_SINGLE_FILE_SIZE) {
                    setParameterExtra(mediaRecorder, "param-use-64bit-offset=1");
                }
            }
            if (C0122O00000o.instance().OO0o0O0() || (i3 <= 0 && recorderMaxFileSize != VIDEO_MAX_SINGLE_FILE_SIZE)) {
                setSplitWhenReachMaxSize(false);
            } else {
                setSplitWhenReachMaxSize(true);
            }
            if ((isFPS240() || isFPS480Direct() || CameraSettings.isAlgoFPS(this.mModuleIndex)) && !C0122O00000o.instance().OO00ooO()) {
                setParameterExtra(mediaRecorder, "video-param-i-frames-interval=0.033");
            }
            mediaRecorder.setOrientationHint(getRecorderOrientationHint());
            StringBuilder sb7 = new StringBuilder();
            sb7.append("video_rotation=");
            sb7.append(this.mOrientation);
            AudioSystem.setParameters(sb7.toString());
            this.mOrientationCompensationAtRecordStart = this.mOrientationCompensation;
        }
        mediaRecorder.setCaptureRate(d);
        mediaRecorder.setMaxDuration(this.mMaxVideoDurationInMs);
        location = null;
        if (this.mActivity.getCameraIntentManager().checkIntentLocationPermission(this.mActivity)) {
        }
        if (location != null) {
        }
        int i32 = SystemProperties.getInt("camera.debug.video_max_size", 0);
        recorderMaxFileSize = getRecorderMaxFileSize(i32);
        if (recorderMaxFileSize > 0) {
        }
        if (C0122O00000o.instance().OO0o0O0()) {
        }
        setSplitWhenReachMaxSize(false);
        try {
            setParameterExtra(mediaRecorder, "video-param-i-frames-interval=0.033");
        } catch (Exception e) {
            Log.e(VideoBase.TAG, e.getMessage(), (Throwable) e);
        }
        mediaRecorder.setOrientationHint(getRecorderOrientationHint());
        StringBuilder sb72 = new StringBuilder();
        sb72.append("video_rotation=");
        sb72.append(this.mOrientation);
        AudioSystem.setParameters(sb72.toString());
        this.mOrientationCompensationAtRecordStart = this.mOrientationCompensation;
    }

    private boolean shouldApplyUltraWideLDC() {
        if (CameraSettings.shouldUltraWideVideoLDCBeVisibleInMode(this.mModuleIndex) && this.mActualCameraId == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
            return CameraSettings.isUltraWideVideoLDCEnabled();
        }
        return false;
    }

    private void startHighSpeedRecordSession() {
        Log.k(3, VideoBase.TAG, "startHighSpeedRecordSession");
        if (isDeviceAlive()) {
            checkDisplayOrientation();
            this.mCamera2Device.setErrorCallback(this.mErrorCallback);
            this.mCamera2Device.setPictureSize(this.mPreviewSize);
            if (this.mAELockOnlySupported) {
                this.mCamera2Device.setFocusCallback(this);
            }
            this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            this.mCamera2Device.startHighSpeedRecordSession(new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture()), this.mRecorderSurface, new Range(Integer.valueOf(this.mHfrFPSLower), Integer.valueOf(this.mHfrFPSUpper)), this);
            this.mFocusManager.resetFocused();
            Util.showSurfaceInfo(this.mRecorderSurface);
        }
    }

    private void startPreviewAfterRecord() {
        if (isDeviceAlive() && !this.mActivity.isActivityPaused()) {
            unlockAEAF();
            if (!isCaptureIntent()) {
                boolean isVideoNewSlowMotion = ModuleManager.isVideoNewSlowMotion();
                Camera2Proxy camera2Proxy = this.mCamera2Device;
                if (isVideoNewSlowMotion) {
                    camera2Proxy.startHighSpeedRecordPreview();
                } else {
                    camera2Proxy.startRecordPreview();
                }
            }
        }
    }

    private void startRecordSession() {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startRecordSession: mode=");
        sb.append(this.mSpeed);
        Log.d(str, sb.toString());
        if (isDeviceAlive()) {
            checkDisplayOrientation();
            this.mCamera2Device.setErrorCallback(this.mErrorCallback);
            this.mCamera2Device.setPictureSize(this.mPreviewSize);
            this.mCamera2Device.setVideoSnapshotSize(this.mPictureSize);
            if (this.mAELockOnlySupported) {
                this.mCamera2Device.setFocusCallback(this);
            }
            int operatingMode = getOperatingMode();
            Log.d(VideoBase.TAG, String.format("startRecordSession: operatingMode = 0x%x enableVideoSnapshot = %b", new Object[]{Integer.valueOf(operatingMode), Boolean.valueOf(this.mEnableVideoSnapshot)}));
            this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            updateFpsRange();
            this.mCamera2Device.startRecordSession(new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture()), this.mRecorderSurface, this.mEnableVideoSnapshot, operatingMode, this);
            this.mFocusManager.resetFocused();
            this.mPreviewing = true;
        }
    }

    private boolean startRecorder() {
        if (!initializeRecorder(true)) {
            return false;
        }
        if (C0122O00000o.instance().OOOOo0() && CameraSettings.is4KHigherVideoQuality(this.mQuality)) {
            int hSRValue = getHSRValue();
            if (hSRValue <= 0) {
                hSRValue = this.mProfile.videoFrameRate;
            }
            ThermalHelper.notifyThermalRecordStart(this.mQuality, hSRValue);
        }
        try {
            this.mMediaRecorder.start();
            this.mMediaRecorderWorking = true;
            return true;
        } catch (IllegalStateException e) {
            Log.k(6, VideoBase.TAG, String.format("could not start recorder: %s", new Object[]{e.getMessage()}));
            showConfirmMessage(R.string.confirm_recording_fail_title, R.string.confirm_recording_fail_recorder_busy_alert);
            return false;
        }
    }

    private void startVideoRecordingIfNeeded() {
        if (this.mActivity.getCameraIntentManager().checkCallerLegality() && !this.mActivity.isActivityPaused()) {
            if (!this.mActivity.getCameraIntentManager().isOpenOnly(this.mActivity)) {
                this.mActivity.getIntent().removeExtra(CameraExtras.CAMERA_OPEN_ONLY);
                if (this.mActivity.getCameraIntentManager().getTimerDurationSeconds() > 0) {
                    Log.e(VideoBase.TAG, "Video mode doesn't support Time duration!");
                } else if (!this.mActivity.isIntentPhotoDone()) {
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            VideoModule videoModule = VideoModule.this;
                            videoModule.onShutterButtonClick(videoModule.getTriggerMode());
                        }
                    }, 1500);
                    this.mActivity.setIntnetPhotoDone(true);
                }
            } else {
                this.mActivity.getIntent().removeExtra(CameraExtras.TIMER_DURATION_SECONDS);
            }
        }
    }

    private void trackProVideoInfo() {
        BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
        CameraStatUtils.trackRecordVideoInProMode(getManualValue(CameraSettings.KEY_QC_PRO_VIDEO_WHITEBALANCE_VALUE, getString(R.string.pref_camera_whitebalance_default)), getManualValue(CameraSettings.KEY_QC_PRO_VIDEO_EXPOSURETIME, getString(R.string.pref_camera_exposuretime_default)), getManualValue(CameraSettings.KEY_QC_PRO_VIDEO_ISO, getString(R.string.pref_camera_iso_default)), getManualValue(CameraSettings.KEY_QC_PRO_VIDEO_EXPOSURE_VALUE, getString(R.string.pref_camera_iso_default)), this.mModuleIndex, getActualCameraId(), bluetoothHeadset != null ? bluetoothHeadset.isBluetoothScoOn() : false, this.mIsAutoHibernationSupported, this.mEnterAutoHibernationCount);
    }

    private void updateAiEnhancedVideo() {
        if (this.mCameraCapabilities.supportAiEnhancedVideo()) {
            boolean isAiEnhancedVideoEnabled = CameraSettings.isAiEnhancedVideoEnabled(this.mModuleIndex);
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.setAiASDEnable(isAiEnhancedVideoEnabled);
                this.mCamera2Device.setAIIEPreviewEnable(isAiEnhancedVideoEnabled);
            }
        }
    }

    private void updateAutoZoomMode() {
        boolean isAutoZoomEnabled = CameraSettings.isAutoZoomEnabled(this.mModuleIndex);
        if (this.mCamera2Device != null && isAlive()) {
            this.mCamera2Device.setAutoZoomMode(isAutoZoomEnabled ? 1 : 0);
            if (isAutoZoomEnabled) {
                this.mCamera2Device.setAutoZoomScaleOffset(0.0f);
            }
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
        if (isSingleCamera()) {
            this.mCamera2Device.setSingleBokeh(true);
            this.mCamera2Device.setDualBokehEnable(false);
        } else {
            this.mCamera2Device.setSingleBokeh(false);
            this.mCamera2Device.setDualBokehEnable(true);
        }
    }

    private void updateCinematicVideo() {
        this.mCamera2Device.setCinematicVideoEnabled(CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex));
    }

    private void updateEvValue() {
        this.mEvValue = (int) (Float.parseFloat(getManualValue(this.mModuleIndex == 169 ? CameraSettings.KEY_QC_FASTMOTION_PRO_EXPOSURE_VALUE : CameraSettings.KEY_QC_PRO_VIDEO_EXPOSURE_VALUE, "0")) / this.mCameraCapabilities.getExposureCompensationStep());
        this.mEvState = 3;
        setEvValue();
    }

    private void updateExposureTime() {
        String str;
        String str2;
        Camera2Proxy camera2Proxy;
        long parseLong;
        int i = this.mModuleIndex;
        if (i == 207) {
            camera2Proxy = this.mCamera2Device;
            parseLong = 125000000;
        } else {
            if (i == 169) {
                str2 = getString(R.string.pref_camera_exposuretime_default);
                str = CameraSettings.KEY_QC_FASTMOTION_PRO_EXPOSURETIME;
            } else {
                str2 = getString(R.string.pref_camera_exposuretime_default);
                str = CameraSettings.KEY_QC_PRO_VIDEO_EXPOSURETIME;
            }
            String manualValue = getManualValue(str, str2);
            camera2Proxy = this.mCamera2Device;
            parseLong = Long.parseLong(manualValue);
        }
        camera2Proxy.setExposureTime(parseLong);
    }

    private void updateFilter() {
        EffectController.getInstance().setEffect(CameraSettings.getShaderEffect());
    }

    private void updateFrontMirror() {
        boolean z = isFrontCamera() && !C0124O00000oO.OOooOoO() && C0122O00000o.instance().OOOo() && CameraSettings.isFrontMirror();
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setFrontMirror(z);
        }
    }

    private void updateHFRDeflicker() {
        if (C0122O00000o.instance().OO0ooO0() && CameraSettings.isAlgoFPS(this.mModuleIndex)) {
            this.mCamera2Device.setHFRDeflickerEnable(true);
        }
    }

    private void updateHdr10VideoMode() {
        if (this.mCameraCapabilities.isVideoHDR10Supported() || this.mCameraCapabilities.isVideoHDR10PlusSupported()) {
            boolean z = CameraSettings.isHdr10Alive(this.mModuleIndex) || CameraSettings.isHdr10PlusAlive(this.mModuleIndex);
            this.mCamera2Device.setHdr10VideoMode(z);
        }
    }

    private void updateHfrFPSRange(Size size, int i) {
        Range[] supportedHighSpeedVideoFPSRange = this.mCameraCapabilities.getSupportedHighSpeedVideoFPSRange(size);
        int length = supportedHighSpeedVideoFPSRange.length;
        Range range = null;
        for (int i2 = 0; i2 < length; i2++) {
            Range range2 = supportedHighSpeedVideoFPSRange[i2];
            if (((Integer) range2.getUpper()).intValue() == i && (range == null || ((Integer) range.getLower()).intValue() < ((Integer) range2.getLower()).intValue())) {
                range = range2;
            }
        }
        this.mHfrFPSLower = ((Integer) range.getLower()).intValue();
        this.mHfrFPSUpper = ((Integer) range.getUpper()).intValue();
    }

    private void updateHistogramStats() {
        this.mCamera2Device.setHistogramStatsEnabled(CameraSettings.isProVideoHistogramOpen(this.mModuleIndex));
    }

    private void updateISO() {
        String string = getString(R.string.pref_camera_iso_default);
        String manualValue = getManualValue(this.mModuleIndex == 169 ? CameraSettings.KEY_QC_FASTMOTION_PRO_ISO : CameraSettings.KEY_QC_PRO_VIDEO_ISO, string);
        if (manualValue == null || manualValue.equals(string)) {
            this.mCamera2Device.setISO(0);
        } else {
            this.mCamera2Device.setISO(Math.min(Util.parseInt(manualValue, 0), this.mCameraCapabilities.getMaxIso()));
        }
    }

    private void updateMacroMode() {
        this.mCamera2Device.setMacroMode(CameraSettings.isMacroModeEnabled(this.mModuleIndex));
    }

    private void updateMutexModePreference() {
        if ("on".equals(DataRepository.dataItemConfig().getComponentHdr().getComponentValue(this.mModuleIndex))) {
            this.mMutexModePicker.setMutexMode(2);
        }
    }

    private void updateSessionParams() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            CaptureSessionConfigurations sessionConfigs = camera2Proxy.getSessionConfigs();
            CameraCapabilities capabilities = this.mCamera2Device.getCapabilities();
            int i = this.mModuleIndex;
            if ((i == 162 || i == 180) && capabilities != null && capabilities.is60fpsDynamicSupported() && capabilities.isDynamicFpsConfigSupported() && CameraSettings.isVideoDynamic60fpsOn(this.mModuleIndex) && CameraSettings.getHSRIntegerValue() == 60) {
                sessionConfigs.set(CaptureRequestVendorTags.DYNAMIC_FPS_CONFIG, (Object) new float[]{2.0f, 30.0f, 60.0f, 0.0f, 0.0f});
                Log.d(VideoBase.TAG, "updateSessionParams: DYNAMIC_FPS_CONFIG");
            }
        }
    }

    private void updateTargetZoom() {
        float readTargetZoom = CameraSettings.readTargetZoom();
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setTargetZoom(readTargetZoom);
        }
    }

    private void updateUltraWideLDC() {
        this.mCamera2Device.setUltraWideLDC(shouldApplyUltraWideLDC());
    }

    private void updateVideoBokeh(boolean z) {
        float videoBokehRatio = CameraSettings.getVideoBokehRatio();
        if (isFrontCamera()) {
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("frontVideoBokeh: ");
            sb.append(videoBokehRatio);
            Log.i(str, sb.toString());
            this.mCamera2Device.setVideoBokehLevelFront(videoBokehRatio);
        } else {
            int i = (int) videoBokehRatio;
            String str2 = VideoBase.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("backVideoBokeh: ");
            sb2.append(i);
            Log.i(str2, sb2.toString());
            this.mCamera2Device.setVideoBokehLevelBack(i);
        }
        if (z) {
            this.mCamera2Device.setVideoFilterColorRetentionMode(CameraSettings.getVideoBokehColorRetentionMode(), isFrontCamera());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0024, code lost:
        r3.mCamera2Device.setVideoFilterColorRetentionBack(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0018, code lost:
        if (r1 != false) goto L_0x001e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001c, code lost:
        if (r1 != false) goto L_0x001e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateVideoColorRetention() {
        boolean z;
        int videoMasterFilter = CameraSettings.isSupportMasterFilter() ? CameraSettings.getVideoMasterFilter() : CameraSettings.getShaderEffect();
        boolean isFrontCamera = isFrontCamera();
        if (videoMasterFilter == 200) {
            z = true;
        } else {
            z = false;
        }
        this.mCamera2Device.setVideoFilterColorRetentionFront(z);
    }

    private void updateVideoFilter() {
        int videoMasterFilter = CameraSettings.isSupportMasterFilter() ? CameraSettings.getVideoMasterFilter() : CameraSettings.getShaderEffect();
        if (videoMasterFilter == 200) {
            videoMasterFilter = FilterInfo.FILTER_ID_NONE;
        }
        if (videoMasterFilter == FilterInfo.FILTER_ID_NONE) {
            videoMasterFilter = 0;
        }
        this.mCamera2Device.setVideoFilterId(videoMasterFilter);
    }

    private void updateVideoLog() {
        this.mCamera2Device.setVideoLogEnabled(CameraSettings.isProVideoLogOpen(this.mModuleIndex));
    }

    private void updateVideoSubtitle() {
        this.mIsSubtitleSupported = DataRepository.dataItemRunning().getComponentRunningSubtitle().isSwitchOn(this.mModuleIndex);
    }

    private void updateVideoTag() {
        SettingUiState videoTagSettingNeedRemove = CameraSettings.getVideoTagSettingNeedRemove(this.mModuleIndex, isFrontCamera());
        boolean z = !videoTagSettingNeedRemove.isRomove && !videoTagSettingNeedRemove.isMutexEnable && CameraSettings.isVideoTagOn();
        this.mIsVideoTagSupported = z;
    }

    private void updateVideoTagState(int i) {
        if (this.mIsVideoTagSupported) {
            VideoTagView videoTagView = null;
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null) {
                videoTagView = topAlert.getVideoTag();
            }
            if (videoTagView != null) {
                if (i == 0) {
                    videoTagView.prepare();
                } else if (i == 1) {
                    videoTagView.start();
                } else if (i == 2) {
                    videoTagView.pause();
                } else if (i == 3) {
                    videoTagView.resume();
                } else if (i == 4) {
                    videoTagView.stop();
                }
            }
        }
    }

    private void updateWhiteBalance() {
        setAWBMode(getManualValue(this.mModuleIndex == 169 ? CameraSettings.KEY_FASTMOTION_PRO_WHITE_BALANCE : CameraSettings.KEY_PRO_VIDEO_WHITE_BALANCE, "1"));
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void O000000o(final OnTagsListener onTagsListener, final List list) {
        boolean z;
        if (this.mIsSubtitleSupported) {
            SubtitleRecording subtitleRecording = this.mSubtitleRecording;
            if (subtitleRecording != null) {
                subtitleRecording.getSubtitleContentAsync(new Listener() {
                    public void onResult(String str) {
                        if (!TextUtils.isEmpty(str)) {
                            String str2 = VideoBase.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onResult, sub title  ");
                            sb.append(str);
                            Log.e(str2, sb.toString());
                            list.add(new VideoTag("com.xiaomi.support_subtitle", str.getBytes(StandardCharsets.UTF_8), MsrtBox.fourcc()));
                        } else {
                            Log.e(VideoBase.TAG, "video subtitle is empty ");
                        }
                        onTagsListener.onTagsReady(list);
                    }

                    public void onTimeout() {
                        String subtitleContentSync = VideoModule.this.mSubtitleRecording.getSubtitleContentSync();
                        String str = VideoBase.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onTimeout, sub title  ");
                        sb.append(subtitleContentSync);
                        Log.e(str, sb.toString());
                        if (!TextUtils.isEmpty(subtitleContentSync)) {
                            list.add(new VideoTag("com.xiaomi.support_subtitle", subtitleContentSync.getBytes(StandardCharsets.UTF_8), MsrtBox.fourcc()));
                        } else {
                            Log.e(VideoBase.TAG, "video subtitle is empty ");
                        }
                        onTagsListener.onTagsReady(list);
                    }
                }, 500);
                z = true;
                if (this.mIsVideoTagSupported) {
                    TopAlert topAlert = this.mTopAlert;
                    String str = "video tag is empty ";
                    if (!(topAlert == null || topAlert.getVideoTag() == null)) {
                        String videoTagContent = this.mTopAlert.getVideoTag().getVideoTagContent();
                        if (!TextUtils.isEmpty(videoTagContent)) {
                            list.add(new VideoTag("com.xiaomi.support_tags", videoTagContent.getBytes(StandardCharsets.UTF_8), MtagBox.fourcc()));
                            onTagsListener.onTagsReady(list);
                            z = true;
                        }
                    }
                    Log.e(VideoBase.TAG, str);
                    onTagsListener.onTagsReady(list);
                    z = true;
                }
                if (z) {
                    onTagsListener.onTagsReady(list);
                    return;
                }
                return;
            }
        }
        z = false;
        if (this.mIsVideoTagSupported) {
        }
        if (z) {
        }
    }

    public /* synthetic */ void O000000o(String str, ContentValues contentValues, boolean z, List list) {
        this.mActivity.getImageSaver().addVideo(str, contentValues, z, false, list);
    }

    public /* synthetic */ void O00000o0(Boolean bool) {
        onMediaRecorderReleased();
    }

    public /* synthetic */ void O0000Oo0(Integer num) {
        if (num.intValue() == 5000) {
            this.mAbandonExposureFilmModeRecord = false;
            ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
            if (actionProcessing != null) {
                actionProcessing.enableStopButton(true, true);
            }
        }
    }

    public /* synthetic */ void O000O00o(boolean z) {
        this.mMainProtocol.setEvAdjustable(z);
    }

    public /* synthetic */ void O00oOooo(boolean z) {
        if (z) {
            int i = this.mModuleIndex;
            if (i == 162 || i == 180) {
                ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                boolean z2 = this.mMediaRecorderRecording || this.mMediaRecorderRecordingPaused;
                if (configChanges != null) {
                    configChanges.reConfigAiAudio(this.mActivity, this.mModuleIndex, z2);
                }
                if (z2) {
                    setCurrentAiAudioParameters(true);
                    setCurrentAiAudioZoomLv();
                }
            }
        }
    }

    public /* synthetic */ void O00ooO0O() {
        RotateTextToast.getInstance(this.mActivity).show(R.string.time_lapse_error, this.mOrientation);
    }

    public /* synthetic */ void O00ooO0o() {
        this.mFocusManager.cancelFocus();
    }

    /* access modifiers changed from: protected */
    public void applyTags(@NonNull OnTagsListener onTagsListener) {
        super.applyTags(new C0384O000oO0O(this, onTagsListener));
    }

    /* access modifiers changed from: protected */
    public void applyZoomRatio() {
        super.applyZoomRatio();
        setCurrentAiAudioZoomLv();
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
            } else if (i2 == 6) {
                updateWhiteBalance();
            } else if (i2 == 19) {
                updateFpsRange();
            } else if (i2 == 20) {
                continue;
            } else if (i2 == 24) {
                applyZoomRatio();
            } else if (i2 == 25) {
                focusCenter();
            } else if (!(i2 == 42 || i2 == 43)) {
                switch (i2) {
                    case 9:
                        updateAntiBanding(C0122O00000o.instance().OO00ooO() ? "0" : CameraSettings.getAntiBanding());
                        break;
                    case 10:
                        updateFlashPreference();
                        break;
                    case 11:
                        updateHDRPreference();
                        break;
                    case 12:
                        setEvValue();
                        break;
                    case 13:
                        updateBeauty();
                        break;
                    case 14:
                        updateVideoFocusMode();
                        break;
                    case 15:
                        updateISO();
                        break;
                    case 16:
                        updateExposureTime();
                        break;
                    case 37:
                        updateBokeh();
                        break;
                    case 40:
                        updateFrontMirror();
                        break;
                    case 55:
                        updateModuleRelated();
                        break;
                    case 58:
                        updateBackSoftLightPreference();
                        break;
                    case 60:
                        updateCinematicVideo();
                        break;
                    case 63:
                        updateEvValue();
                        break;
                    case 65:
                        updateVideoLog();
                        break;
                    case 66:
                        updateThermalLevel();
                        break;
                    case 67:
                        updateVideoBokeh(this.mCameraCapabilities.isSupportVideoBokehColorRetentionTag(isFrontCamera()));
                        break;
                    case 68:
                        updateVideoFilter();
                        break;
                    case 69:
                        updateVideoColorRetention();
                        break;
                    case 75:
                        updateHistogramStats();
                        break;
                    case 79:
                        updateTargetZoom();
                        break;
                    case 80:
                        updateHdr10VideoMode();
                        break;
                    case 81:
                        updateVideoBokeh(true);
                        break;
                    case 85:
                        updateAiEnhancedVideo();
                        break;
                    case UpdateConstant.TYPE_CAMERA_SESSION_PARAMS /*51966*/:
                        updateSessionParams();
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
                                    case 33:
                                        Camera2Proxy camera2Proxy = this.mCamera2Device;
                                        if (camera2Proxy == null) {
                                            break;
                                        } else {
                                            camera2Proxy.setVideoSnapshotSize(this.mPictureSize);
                                            break;
                                        }
                                    case 34:
                                        continue;
                                    case 35:
                                        updateDeviceOrientation();
                                        break;
                                    default:
                                        switch (i2) {
                                            case 45:
                                            case 46:
                                            case 48:
                                                continue;
                                            case 47:
                                                updateUltraWideLDC();
                                                break;
                                            default:
                                                switch (i2) {
                                                    case 50:
                                                        continue;
                                                    case 51:
                                                        updateAutoZoomMode();
                                                        break;
                                                    case 52:
                                                        updateMacroMode();
                                                        break;
                                                    case 53:
                                                        updateHFRDeflicker();
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
                        }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void createMediaRecorder() {
        this.mMediaRecorder = new MediaRecorder();
    }

    /* access modifiers changed from: protected */
    public void doLaterReleaseIfNeed() {
        super.doLaterReleaseIfNeed();
        if (CameraSettings.isAlgoFPS(this.mModuleIndex) && !this.mActivity.isActivityPaused()) {
            if (isTextureExpired()) {
                Log.d(VideoBase.TAG, "doLaterReleaseIfNeed: restartModule...");
                restartModule();
                return;
            }
            Log.d(VideoBase.TAG, "doLaterReleaseIfNeed: dismissBlurCover...");
            this.mActivity.dismissBlurCover();
        }
    }

    /* access modifiers changed from: protected */
    public boolean enableFaceDetection() {
        boolean z = false;
        if (C0122O00000o.instance().OOOoO0O() && isBackCamera()) {
            return false;
        }
        if (!ModuleManager.isVideoNewSlowMotion() || !isBackCamera()) {
            if (DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_FACE_DETECTION, getResources().getBoolean(R.bool.pref_camera_facedetection_default))) {
                z = true;
            }
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public CamcorderProfile fetchProfile(int i, int i2) {
        return CamcorderProfile.get(i, i2);
    }

    public void fillFeatureControl(StartControl startControl) {
        super.fillFeatureControl(startControl);
        int i = startControl.mTargetMode;
        if (i == 162 || i == 214) {
            startControl.getFeatureDetail().addFragmentInfo(R.id.main_subtitle, BaseFragmentDelegate.FRAGMENT_SUBTITLE);
        }
    }

    /* access modifiers changed from: protected */
    public int getNormalVideoFrameRate() {
        if (!C0122O00000o.instance().OO00ooO()) {
            CamcorderProfile camcorderProfile = this.mProfile;
            if (camcorderProfile != null) {
                return camcorderProfile.videoFrameRate;
            }
        }
        return 30;
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        boolean isFrontCamera = isFrontCamera();
        int i = CameraCapabilities.SESSION_OPERATION_MODE_VIDEO_HDR;
        int i2 = CameraCapabilities.SESSION_OPERATION_MODE_VIDEO_BEAUTY_WITH_PREVIEW_EIS;
        int i3 = 32772;
        if (!isFrontCamera) {
            boolean isEisOn = isEisOn();
            if (!needChooseVideoBeauty(this.mBeautyValues)) {
                i2 = CameraSettings.isAiEnhancedVideoEnabled(this.mModuleIndex) ? CameraCapabilities.SESSION_OPERATION_MODE_AI_VIDEO_ENHANCED : this.mQuality == 0 ? 0 : (CameraSettings.isSuperEISEnabled(this.mModuleIndex) || !CameraSettings.getSuperEISProValue(this.mModuleIndex).equals("off")) ? CameraSettings.getSuperEISProValue(this.mModuleIndex).equals(ComponentRunningEisPro.EIS_VALUE_PRO) ? CameraCapabilities.SESSION_OPERATION_MODE_VIDEO_SUPEREISPRO : CameraCapabilities.SESSION_OPERATION_MODE_VIDEO_SUPEREIS : isEisOn ? 32772 : 61456;
            } else if (!isEisOn) {
                i2 = 32777;
            }
            boolean z = true;
            if (getHSRValue() == 60) {
                CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
                boolean z2 = cameraCapabilities != null && cameraCapabilities.isCurrentQualitySupportEis(this.mQuality, 60);
                if ((!is4K60FpsEISSupported() && !z2) || !isEisOn) {
                    i3 = CameraCapabilities.SESSION_OPERATION_MODE_HSR_60;
                }
            } else {
                i3 = i2;
            }
            if (CameraSettings.isAutoZoomEnabled(this.mModuleIndex)) {
                i3 = CameraCapabilities.SESSION_OPERATION_MODE_AUTO_ZOOM;
            }
            boolean isHdr10Alive = CameraSettings.isHdr10Alive(this.mModuleIndex);
            int i4 = CameraCapabilities.SESSION_OPERATION_MODE_HDR10;
            if ((isHdr10Alive || CameraSettings.isHdr10PlusAlive(this.mModuleIndex)) && !isEisOn) {
                i3 = 32804;
            }
            if (!CameraSettings.isHdr10PlusAlive(this.mModuleIndex) || !isEisOn || !needDisableEISAndOIS()) {
                i4 = i3;
            }
            if (is8KCamcorder() && isEisOn) {
                i4 = CameraCapabilities.SESSION_OPERATION_MODE_VIDEO_EIS_8K;
            }
            if (this.mModuleIndex != 162 || !C0122O00000o.instance().getConfig().Oo0Ooo() || !CameraSettings.isVhdrOn(this.mModuleIndex)) {
                i = i4;
            }
            if (this.mCameraCapabilities.isFovcSupported()) {
                if (i == 0) {
                    z = false;
                }
                this.mFovcEnabled = z;
            }
            if (this.mModuleIndex == 214) {
                i = CameraCapabilities.SESSION_OPERATION_MODE_VIDEO_SUPER_NIGHT;
            }
        } else if (CameraSettings.isVideoBokehOn()) {
            i = 32770;
        } else if (isEisOn()) {
            i = needChooseVideoBeauty(this.mBeautyValues) ? 32793 : 32772;
        } else if (this.mModuleIndex != 162 || !C0122O00000o.instance().getConfig().Oo0Ooo() || !CameraSettings.isVhdrOn(this.mModuleIndex)) {
            i = this.mCameraCapabilities.isSupportVideoBeauty() ? 32777 : 0;
        }
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getOperatingMode: ");
        sb.append(Integer.toHexString(i));
        Log.d(str, sb.toString());
        return i;
    }

    public List getSupportedSettingKeys() {
        ArrayList arrayList = new ArrayList();
        if (isBackCamera()) {
            arrayList.add("pref_video_speed_fast_key");
        }
        return arrayList;
    }

    public String getTag() {
        return VideoBase.TAG;
    }

    /* access modifiers changed from: protected */
    public void handleTempVideoFile() {
        if (isCaptureIntent()) {
            String str = this.mTemporaryVideoPath;
            if (str == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(getActivity().getCacheDir().getPath());
                sb.append("/temp_video.mp4");
                this.mTemporaryVideoPath = sb.toString();
                String str2 = VideoBase.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("VideoModule: wq ");
                sb2.append(this.mTemporaryVideoPath);
                Log.d(str2, sb2.toString());
            } else {
                File file = new File(str);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void initRecorderSurface() {
        this.mRecorderSurface = MediaCodec.createPersistentInputSurface();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x014a A[Catch:{ Exception -> 0x016c, all -> 0x016a }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01cb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean initializeRecorder(boolean z) {
        MediaRecorder mediaRecorder;
        String str;
        MediaRecorder mediaRecorder2;
        FileDescriptor fileDescriptor;
        Log.d(VideoBase.TAG, "initializeRecorder>>");
        boolean z2 = false;
        if (C0124O00000oO.Oo000o0()) {
            if (this.mBoostFramework == null) {
                this.mBoostFramework = new BoostFrameworkImpl();
            }
            BoostFrameworkImpl boostFrameworkImpl = this.mBoostFramework;
            if (boostFrameworkImpl != null) {
                boostFrameworkImpl.startBoost(400, 0);
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (getActivity() == null) {
            Log.w(VideoBase.TAG, "initializeRecorder: null host");
            return false;
        }
        closeVideoFileDescriptor();
        cleanupEmptyFile();
        if (isCaptureIntent()) {
            handleTempVideoFile();
            parseIntent(this.mActivity.getIntent());
        }
        if (this.mVideoFileDescriptor == null) {
            this.mCurrentVideoValues = genContentValues(this.mOutputFormat, this.mCurrentFileNumber, this.mSlowModeFps, is8KCamcorder(), z);
            this.mCurrentVideoFilename = z ? this.mCurrentVideoValues.getAsString("_data") : new File(this.mActivity.getCacheDir(), this.mCurrentVideoValues.getAsString("_display_name")).getPath();
        }
        if (this.mStopRecorderDone != null) {
            long currentTimeMillis2 = System.currentTimeMillis();
            try {
                this.mStopRecorderDone.await(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String str2 = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("initializeRecorder: waitTime=");
            sb.append(System.currentTimeMillis() - currentTimeMillis2);
            Log.d(str2, sb.toString());
        }
        long currentTimeMillis3 = System.currentTimeMillis();
        synchronized (this.mLock) {
            if (this.mMediaRecorder == null) {
                createMediaRecorder();
            } else {
                this.mMediaRecorder.reset();
                if (BaseModule.DEBUG) {
                    String str3 = VideoBase.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("initializeRecorder: t1=");
                    sb2.append(System.currentTimeMillis() - currentTimeMillis3);
                    Log.v(str3, sb2.toString());
                }
            }
        }
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            setupRecorder(this.mMediaRecorder);
            if (z) {
                setCurrentAiAudioParameters(true);
            }
            if (this.mVideoFileDescriptor == null) {
                if (Storage.isUseDocumentMode()) {
                    if (z) {
                        parcelFileDescriptor = FileCompat.getParcelFileDescriptor(this.mCurrentVideoFilename, true);
                        mediaRecorder2 = this.mMediaRecorder;
                        fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    }
                }
                mediaRecorder = this.mMediaRecorder;
                str = this.mCurrentVideoFilename;
                mediaRecorder.setOutputFile(str);
                this.mMediaRecorder.setInputSurface(this.mRecorderSurface);
                long currentTimeMillis4 = System.currentTimeMillis();
                this.mMediaRecorder.prepare();
                this.mMediaRecorder.setOnErrorListener(this);
                this.mMediaRecorder.setOnInfoListener(this);
                if (BaseModule.DEBUG) {
                }
                Util.closeSilently(parcelFileDescriptor);
                z2 = true;
                Util.showSurfaceInfo(this.mRecorderSurface);
                String str4 = VideoBase.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("initializeRecorder<<time=");
                sb3.append(System.currentTimeMillis() - currentTimeMillis);
                Log.d(str4, sb3.toString());
                if (C0124O00000oO.Oo000o0()) {
                }
                return z2;
            } else if (z) {
                mediaRecorder2 = this.mMediaRecorder;
                fileDescriptor = this.mVideoFileDescriptor.getFileDescriptor();
            } else {
                mediaRecorder = this.mMediaRecorder;
                str = this.mTemporaryVideoPath;
                mediaRecorder.setOutputFile(str);
                this.mMediaRecorder.setInputSurface(this.mRecorderSurface);
                long currentTimeMillis42 = System.currentTimeMillis();
                this.mMediaRecorder.prepare();
                this.mMediaRecorder.setOnErrorListener(this);
                this.mMediaRecorder.setOnInfoListener(this);
                if (BaseModule.DEBUG) {
                    String str5 = VideoBase.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("initializeRecorder: t2=");
                    sb4.append(System.currentTimeMillis() - currentTimeMillis42);
                    Log.v(str5, sb4.toString());
                }
                Util.closeSilently(parcelFileDescriptor);
                z2 = true;
                if (z2 && BaseModule.DEBUG) {
                    Util.showSurfaceInfo(this.mRecorderSurface);
                }
                String str42 = VideoBase.TAG;
                StringBuilder sb32 = new StringBuilder();
                sb32.append("initializeRecorder<<time=");
                sb32.append(System.currentTimeMillis() - currentTimeMillis);
                Log.d(str42, sb32.toString());
                if (C0124O00000oO.Oo000o0()) {
                    BoostFrameworkImpl boostFrameworkImpl2 = this.mBoostFramework;
                    if (boostFrameworkImpl2 != null) {
                        boostFrameworkImpl2.stopBoost();
                    }
                }
                return z2;
            }
            mediaRecorder2.setOutputFile(fileDescriptor);
            this.mMediaRecorder.setInputSurface(this.mRecorderSurface);
            long currentTimeMillis422 = System.currentTimeMillis();
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.setOnErrorListener(this);
            this.mMediaRecorder.setOnInfoListener(this);
            if (BaseModule.DEBUG) {
            }
            Util.closeSilently(parcelFileDescriptor);
            z2 = true;
        } catch (Exception e2) {
            String str6 = "";
            if (e2 instanceof FileNotFoundException) {
                str6 = Util.getFilesState(this.mCurrentVideoFilename);
            }
            String str7 = VideoBase.TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("prepare failed for ");
            sb5.append(this.mCurrentVideoFilename);
            sb5.append(";");
            sb5.append(str6);
            Log.e(str7, sb5.toString(), (Throwable) e2);
            releaseMediaRecorder();
            Util.closeSilently(null);
        } catch (Throwable th) {
            Util.closeSilently(null);
            throw th;
        }
        Util.showSurfaceInfo(this.mRecorderSurface);
        String str422 = VideoBase.TAG;
        StringBuilder sb322 = new StringBuilder();
        sb322.append("initializeRecorder<<time=");
        sb322.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(str422, sb322.toString());
        if (C0124O00000oO.Oo000o0()) {
        }
        return z2;
    }

    public boolean is4KCamcorder() {
        return this.mQuality == CameraSettings.get4kProfile() || new CameraSize(3840, 2160).equals(this.mVideoSize);
    }

    public boolean is8KCamcorder() {
        return this.mQuality == CameraSettings.get8kProfile() || new CameraSize(7680, 4320).equals(this.mVideoSize);
    }

    /* access modifiers changed from: protected */
    public boolean isAEAFLockSupported() {
        return !this.mMediaRecorderRecording || !CameraSettings.isAlgoFPS(this.mModuleIndex);
    }

    /* access modifiers changed from: protected */
    public boolean isCameraSwitchingDuringZoomingAllowed() {
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            int i = this.mModuleIndex;
            if ((i == 162 || i == 169) && !CameraSettings.isMacroModeEnabled(this.mModuleIndex) && !CameraSettings.isSuperEISEnabled(this.mModuleIndex) && isBackCamera() && !this.mMediaRecorderRecording && !this.mMediaRecorderRecordingPaused && (DataRepository.dataItemGlobal().isNormalIntent() || !this.mCameraCapabilities.isSupportLightTripartite())) {
                return true;
            }
        }
        return false;
    }

    public boolean isNeedAlertAudioZoomIndicator() {
        int i = this.mModuleIndex;
        if ((i == 162 || i == 180) && !isFrontCamera()) {
            return (this.mMediaRecorderRecording || this.mMediaRecorderRecordingPaused) && DataRepository.dataItemRunning().getComponentRunningAiAudio().getCurrentRecType(this.mModuleIndex) == 2;
        }
        return false;
    }

    public boolean isNeedHapticFeedback() {
        return !this.mMediaRecorderRecording || this.mMediaRecorderRecordingPaused;
    }

    public boolean isNeedMute() {
        return this.mObjectTrackingStarted || (this.mMediaRecorderRecording && !this.mMediaRecorderRecordingPaused);
    }

    public boolean isPostProcessing() {
        return this.mMediaRecorderPostProcessing;
    }

    /* access modifiers changed from: protected */
    public boolean isShowHFRDuration() {
        return true;
    }

    public boolean isUnInterruptable() {
        if (!super.isUnInterruptable() && !isNormalMode() && this.mMediaRecorder != null && this.mMediaRecorderWorking) {
            this.mUnInterruptableReason = "recorder release";
        }
        return this.mUnInterruptableReason != null;
    }

    public boolean isZoomEnabled() {
        if ((CameraSettings.isAlgoFPS(this.mModuleIndex) && this.mMediaRecorderRecording) || CameraSettings.isAutoZoomEnabled(this.mModuleIndex) || CameraSettings.isSuperEISEnabled(this.mModuleIndex)) {
            return false;
        }
        if (this.mModuleIndex != 208 || !this.mMediaRecorderRecording) {
            return super.isZoomEnabled();
        }
        return false;
    }

    public boolean onBackPressed() {
        if (!isFrameAvailable()) {
            return false;
        }
        if (!this.mPaused && !this.mActivity.isActivityPaused()) {
            if (this.mStereoSwitchThread != null) {
                return false;
            }
            if (CameraSettings.isAlgoFPS(this.mModuleIndex) && this.mMediaRecorderPostProcessing) {
                return true;
            }
            if (this.mMediaRecorderRecording) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
                    this.mLastBackPressedTime = currentTimeMillis;
                    ToastUtils.showToast((Context) this.mActivity, (int) R.string.record_back_pressed_hint, true);
                } else {
                    stopVideoRecording(false);
                }
                return true;
            }
            int i = this.mModuleIndex;
            if (i == 208 || i == 207) {
                ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                if (configChanges != null) {
                    configChanges.configFilm(null, false, false);
                }
            } else if (!this.isAutoZoomTracking.get()) {
                return super.onBackPressed();
            } else {
                stopTracking(0);
                return true;
            }
        }
        return true;
    }

    public void onBluetoothHeadsetConnected() {
        super.onBluetoothHeadsetConnected();
        if (!this.mMediaRecorderRecording) {
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
        updateBeauty();
        updateVideoSubtitle();
        updateVideoTag();
        updateAutoHibernation();
        readVideoPreferences();
        initializeFocusManager();
        updatePreferenceTrampoline(UpdateConstant.VIDEO_TYPES_INIT);
        boolean initializeRecorder = initializeRecorder(false);
        this.mEnableVideoSnapshot = !isEnableScreenShot();
        if (!initializeRecorder) {
            startVideoPreviewSession();
        } else if (ModuleManager.isVideoNewSlowMotion()) {
            startHighSpeedRecordSession();
        } else {
            startRecordSession();
        }
        if (CameraSettings.is8KHigherVideoQuality(this.mQuality)) {
            this.mActivity.boostCameraByThreshold(C0122O00000o.instance().O0oo0oO());
        }
        if (CameraSettings.is4KHigherVideoQuality(this.mQuality)) {
            this.mActivity.boostCameraByThreshold(C0122O00000o.instance().O0oo0o0());
        }
        initAutoZoom();
        initHistogramEmitter();
        if (getModuleIndex() == 180) {
            SoundSetting.setGainState(getActivity(), mGainVal);
            this.mAudioCalculateDecibels.start();
            return;
        }
        SoundSetting.setGainState(getActivity(), "50");
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
    }

    public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
    }

    public void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4) {
        this.mActivity.getCameraScreenNail().requestFullReadPixelsWithFilmState(CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex));
    }

    public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
        return null;
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        if (C0122O00000o.instance().OO0oO0O()) {
            if (this.mAudioManager == null) {
                this.mAudioManager = (AudioManager) CameraAppImpl.getAndroidContext().getSystemService("audio");
            }
            if (this.mAudioManagerAudioDeviceCallback == null) {
                this.mAudioManagerAudioDeviceCallback = new AudioManagerAudioDeviceCallback();
            }
            this.mAudioManager.registerAudioDeviceCallback(this.mAudioManagerAudioDeviceCallback, this.mHandler);
            this.mAudioManagerAudioDeviceCallback.setOnAudioDeviceChangeListener(this.mAudioDeviceChangeListener);
        }
        this.mAudioCalculateDecibels = new AudioCalculateDecibels(1, getActivity());
        this.mAudioCalculateDecibels.setOnVolumeListener(this);
        this.mAudioMonitorPlayer = new AudioMonitorPlayer();
        setCaptureIntent(this.mActivity.getCameraIntentManager().isVideoCaptureIntent());
        EffectController.getInstance().setEffect(FilterInfo.FILTER_ID_NONE);
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        this.mQuickCapture = this.mActivity.getCameraIntentManager().isQuickCapture().booleanValue();
        enableCameraControls(false);
        this.mVideoFocusMode = AutoFocus.LEGACY_CONTINUOUS_VIDEO;
        this.mAutoZoomViewProtocol = (AutoZoomViewProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(214);
        this.mSubtitleRecording = (SubtitleRecording) ModeCoordinatorImpl.getInstance().getAttachProtocol(231);
        initRecorderSurface();
        onCameraOpened();
    }

    public void onDestroy() {
        super.onDestroy();
        releaseRecorderSurface();
        this.mHandler.sendEmptyMessage(45);
        if (C0122O00000o.instance().OO0oO0O()) {
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                AudioManagerAudioDeviceCallback audioManagerAudioDeviceCallback = this.mAudioManagerAudioDeviceCallback;
                if (audioManagerAudioDeviceCallback != null) {
                    audioManager.unregisterAudioDeviceCallback(audioManagerAudioDeviceCallback);
                    this.mAudioManagerAudioDeviceCallback.setOnAudioDeviceChangeListener(null);
                }
            }
        }
    }

    public void onError(MediaRecorder mediaRecorder, int i, int i2) {
        Log.k(6, VideoBase.TAG, String.format("MediaRecorder error. what=%d extra=%d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
        if (i == 1 || i == 100) {
            if (this.mMediaRecorderRecording) {
                stopVideoRecording(false);
            }
            this.mActivity.getScreenHint().updateHint();
        }
    }

    public boolean onGestureTrack(RectF rectF, boolean z) {
        if (this.mInStartingFocusRecording || !isBackCamera() || !C0124O00000oO.Oo00o() || CameraSettings.is4KHigherVideoQuality(this.mQuality) || isCaptureIntent()) {
            return false;
        }
        return initializeObjectTrack(rectF, z);
    }

    public void onGradienterSwitched(boolean z) {
        this.isGradienterOn = z;
        this.mActivity.getSensorStateManager().setGradienterEnabled(z);
        updatePreferenceTrampoline(2, 5);
    }

    public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
        if (!this.mMediaRecorderRecording) {
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onInfo: ignore event ");
            sb.append(i);
            Log.w(str, sb.toString());
            return;
        }
        switch (i) {
            case 800:
                stopVideoRecording(false);
                break;
            case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE /*801*/:
                String str2 = VideoBase.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("reached max size. fileNumber=");
                sb2.append(this.mCurrentFileNumber);
                Log.w(str2, sb2.toString());
                stopVideoRecording(false);
                if (!this.mActivity.getScreenHint().isScreenHintVisible()) {
                    Toast.makeText(this.mActivity, R.string.video_reach_size_limit, 1).show();
                    break;
                }
                break;
            case 802:
                boolean isSplitWhenReachMaxSize = isSplitWhenReachMaxSize();
                String str3 = VideoBase.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("max file size is approaching. split: ");
                sb3.append(isSplitWhenReachMaxSize);
                Log.d(str3, sb3.toString());
                if (isSplitWhenReachMaxSize) {
                    this.mCurrentFileNumber++;
                    ContentValues genContentValues = genContentValues(this.mOutputFormat, this.mCurrentFileNumber, this.mSlowModeFps, is8KCamcorder(), true);
                    String asString = genContentValues.getAsString("_data");
                    String str4 = VideoBase.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("nextVideoPath: ");
                    sb4.append(asString);
                    Log.d(str4, sb4.toString());
                    if (setNextOutputFile(asString)) {
                        this.mNextVideoValues = genContentValues;
                        this.mNextVideoFileName = asString;
                        break;
                    }
                }
                break;
            case 803:
                Log.d(VideoBase.TAG, "next output file started");
                onMaxFileSizeReached();
                this.mCurrentVideoValues = this.mNextVideoValues;
                this.mCurrentVideoFilename = this.mNextVideoFileName;
                this.mNextVideoValues = null;
                this.mNextVideoFileName = null;
                break;
            default:
                String str5 = VideoBase.TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("onInfo what : ");
                sb5.append(i);
                Log.w(str5, sb5.toString());
                break;
        }
    }

    /* access modifiers changed from: protected */
    public boolean onInterceptZoomingEvent(float f, float f2, int i) {
        if (C0122O00000o.instance().OOoOo() && CameraSettings.supportVideoSATForVideoQuality(getModuleIndex()) && !CameraSettings.isVideoQuality8KOpen(getModuleIndex()) && HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            int i2 = this.mModuleIndex;
            if ((i2 == 162 || i2 == 169) && !CameraSettings.isMacroModeEnabled(this.mModuleIndex) && !CameraSettings.isSuperEISEnabled(this.mModuleIndex) && isBackCamera()) {
                if (C0122O00000o.instance().OOoOo0()) {
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
                        if (!(camera2Proxy == null || 3 == camera2Proxy.getFocusMode())) {
                            Log.d(VideoBase.TAG, "onInterceptZoomingEvent: should cancel focus.");
                            FocusManager2 focusManager22 = this.mFocusManager;
                            if (focusManager22 != null) {
                                focusManager22.cancelFocus();
                            }
                        }
                    }
                }
                return false;
            }
        }
        return super.onInterceptZoomingEvent(f, f2, i);
    }

    public void onNewUriArrived(Uri uri, String str) {
        super.onNewUriArrived(uri, str);
        if (str != null && str.contains("VID")) {
            int i = this.mModuleIndex;
            if (i == 208 || i == 207) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                        if (configChanges != null) {
                            configChanges.configFilm(null, false, false);
                        }
                    }
                });
            }
        }
    }

    public void onObjectStable() {
    }

    public void onPause() {
        if (this.mCamera2Device != null && (this.mFovcEnabled || (this.mCameraCapabilities.isEISPreviewSupported() && isEisOn()))) {
            this.mCamera2Device.notifyVideoStreamEnd();
        }
        super.onPause();
        waitStereoSwitchThread();
        stopObjectTracking(false);
        releaseResources();
        closeVideoFileDescriptor();
        this.mActivity.getSensorStateManager().reset();
        stopFaceDetection(true);
        resetScreenOn();
        this.mHandler.removeCallbacksAndMessages(null);
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.removeMessages();
        }
        if (!this.mActivity.isActivityPaused()) {
            PopupManager.getInstance(this.mActivity).notifyShowPopup(null, 1);
        }
        BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
        if (bluetoothHeadset != null) {
            bluetoothHeadset.stopBluetoothSco(getModuleIndex());
        }
    }

    public void onPauseButtonClick() {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPauseButtonClick: isRecordingPaused=");
        sb.append(this.mMediaRecorderRecordingPaused);
        sb.append(" isRecording=");
        sb.append(this.mMediaRecorderRecording);
        Log.u(str, sb.toString());
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mMediaRecorderRecording && currentTimeMillis - this.mPauseClickTime >= 500) {
            this.mPauseClickTime = currentTimeMillis;
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (this.mMediaRecorderRecordingPaused) {
                Log.u(VideoBase.TAG, "onPauseButtonClick resumeVideoRecording");
                resumeVideoRecording(recordState);
                return;
            }
            pauseVideoRecording();
            updateVideoTagState(2);
            CameraStatUtils.trackPauseOrResumeVideoRecording(isFrontCamera(), false);
            if (this.mIsSubtitleSupported) {
                SubtitleRecording subtitleRecording = this.mSubtitleRecording;
                if (subtitleRecording != null) {
                    subtitleRecording.handleSubtitleRecordingPause();
                }
            }
            Log.u(VideoBase.TAG, "onPauseButtonClick onPause");
            recordState.onPause();
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
        if (this.mFocusManager != null && this.mActivity.getCameraScreenNail() != null) {
            this.mActivity.getCameraScreenNail().setDisplayArea(rect);
            this.mFocusManager.setRenderSize(this.mActivity.getCameraScreenNail().getRenderWidth(), this.mActivity.getCameraScreenNail().getRenderHeight());
            this.mFocusManager.setPreviewSize(rect.width(), rect.height());
        }
    }

    public void onPreviewMetaDataUpdate(CaptureResult captureResult) {
        super.onPreviewMetaDataUpdate(captureResult);
        if (this.isAutoZoomTracking.get()) {
            this.mAutoZoomEmitter.onNext(captureResult);
        }
        FlowableEmitter flowableEmitter = this.mHistogramEmitter;
        if (flowableEmitter != null) {
            flowableEmitter.onNext(captureResult);
        }
    }

    public void onPreviewRelease() {
        if (this.mMediaRecorderRecording) {
            stopRecorder();
        }
        super.onPreviewRelease();
    }

    public void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession) {
        super.onPreviewSessionFailed(cameraCaptureSession);
        enableCameraControls(true);
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        if (this.mCamera2Device != null) {
            super.onPreviewSessionSuccess(cameraCaptureSession);
            if (!isCreated()) {
                Log.w(VideoBase.TAG, "onPreviewSessionSuccess: module is not ready");
                enableCameraControls(true);
                return;
            }
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPreviewSessionSuccess: session=");
            sb.append(cameraCaptureSession);
            Log.d(str, sb.toString());
            this.mFaceDetectionEnabled = false;
            synchronized (this.mDeviceLock) {
                if (this.mCamera2Device != null) {
                    CameraConfigs cameraConfigs = this.mCamera2Device.getCameraConfigs();
                    boolean z = this.mCameraCapabilities.isSupportVideoBokehAdjust() && isVideoBokehEnabled();
                    cameraConfigs.setVideoBokehEnabled(z);
                }
            }
            updatePreferenceInWorkThread(UpdateConstant.VIDEO_TYPES_ON_PREVIEW_SUCCESS);
            enableCameraControls(true);
            int i = this.mModuleIndex;
            if (i == 180 || (i == 169 && C0122O00000o.instance().OOO00Oo() && isBackCamera())) {
                updatePreferenceInWorkThread(UpdateConstant.CAMERA_TYPES_MANUALLY);
            } else if (this.mModuleIndex == 207) {
                updatePreferenceInWorkThread(16);
            }
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeMessages(64);
                this.mHandler.sendEmptyMessageDelayed(64, 300);
            }
        }
    }

    /* access modifiers changed from: protected */
    @MainThread
    public void onPreviewStart() {
        if (this.mPreviewing) {
            this.mMainProtocol.initializeFocusView(this);
            updateMutexModePreference();
            onShutterButtonFocus(true, 3);
            startVideoRecordingIfNeeded();
        }
    }

    public void onSharedPreferenceChanged() {
        if (!this.mPaused && !this.mActivity.isActivityPaused() && this.mCamera2Device != null) {
            CamcorderProfile camcorderProfile = this.mProfile;
            int i = camcorderProfile.videoFrameWidth;
            int i2 = camcorderProfile.videoFrameHeight;
            readVideoPreferences();
            CamcorderProfile camcorderProfile2 = this.mProfile;
            if (!(camcorderProfile2.videoFrameWidth == i && camcorderProfile2.videoFrameHeight == i2)) {
                Log.d(VideoBase.TAG, String.format(Locale.ENGLISH, "profile size changed [%d %d]->[%d %d]", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(this.mProfile.videoFrameWidth), Integer.valueOf(this.mProfile.videoFrameHeight)}));
                updatePreferenceTrampoline(1);
            }
        }
    }

    public void onShutterButtonClick(int i) {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onShutterButtonClick isRecording=");
        sb.append(this.mMediaRecorderRecording);
        sb.append(" inStartingFocusRecording=");
        sb.append(this.mInStartingFocusRecording);
        Log.u(str, sb.toString());
        this.mInStartingFocusRecording = false;
        this.mLastBackPressedTime = 0;
        if (isIgnoreTouchEvent()) {
            Log.w(VideoBase.TAG, "onShutterButtonClick: ignore touch event");
        } else if (!isFrontCamera() || !this.mActivity.isScreenSlideOff()) {
            if (this.mMediaRecorderRecording) {
                Log.u(VideoBase.TAG, "onShutterButtonClick: stop");
                Log.k(3, VideoBase.TAG, String.format("onShutterButtonClick: stop mode=%d", new Object[]{Integer.valueOf(i)}));
                stopVideoRecording(false);
            } else {
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                if (this.mIsSatFallback == 0 || !shouldCheckSatFallbackState()) {
                    recordState.onPrepare();
                    updateVideoTagState(0);
                    if (!checkCallingState()) {
                        recordState.onFailed();
                        updateVideoTagState(4);
                        return;
                    }
                    this.mActivity.getScreenHint().updateHint();
                    if (Storage.isLowStorageAtLastPoint()) {
                        recordState.onFailed();
                        updateVideoTagState(4);
                        return;
                    }
                    setTriggerMode(i);
                    enableCameraControls(false);
                    playCameraSound(2);
                    if (this.mFocusManager.canRecord()) {
                        Log.u(VideoBase.TAG, "onShutterButtonClick: startVideoRecording");
                        Log.k(3, VideoBase.TAG, String.format("startVideoRecording mode = %d", new Object[]{Integer.valueOf(i)}));
                        startVideoRecording();
                    } else {
                        Log.v(VideoBase.TAG, "wait for autoFocus");
                        this.mInStartingFocusRecording = true;
                        this.mHandler.sendEmptyMessageDelayed(55, 3000);
                    }
                } else {
                    Log.w(VideoBase.TAG, "video record check: sat fallback");
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
                if (backStack != null && !backStack.handleBackStackFromTapDown(i, i2) && !this.isAutoZoomTracking.get()) {
                    if (this.mModuleIndex != 208 || !isRecording()) {
                        if (this.mObjectTrackingStarted) {
                            stopObjectTracking(false);
                        }
                        unlockAEAF();
                        this.mMainProtocol.setFocusViewType(true);
                        this.mTouchFocusStartingTime = System.currentTimeMillis();
                        Point point = new Point(i, i2);
                        mapTapCoordinate(point);
                        this.mFocusManager.onSingleTapUp(point.x, point.y, z);
                    }
                }
            }
        }
    }

    public void onStop() {
        super.onStop();
        this.mHandler.removeCallbacksAndMessages(null);
        exitSavePowerMode();
        if (this.mIsAutoHibernationSupported) {
            exitAutoHibernation();
        }
    }

    public void onTrackLost() {
        notifyAutoZoomStartUiHint();
    }

    public void onTrackLosting() {
        this.mTrackLostCount++;
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        if (this.mMediaRecorderRecording) {
            keepPowerSave();
            if (this.mIsAutoHibernationSupported) {
                keepAutoHibernation();
            }
        }
    }

    public void onVolumeValue(float[] fArr) {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.setVolumeValue(fArr);
        }
    }

    /* access modifiers changed from: protected */
    public void onWaitStopCallbackTimeout() {
        stopRecorder();
        startPreviewAfterRecord();
    }

    public void onZoomingActionEnd(int i) {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onZoomingActionEnd(): ");
        sb.append(ZoomingAction.toString(i));
        sb.append(" @hash: ");
        sb.append(hashCode());
        Log.d(str, sb.toString());
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null && dualController.isZoomPanelVisible()) {
            dualController.updateZoomIndexsButton();
        }
    }

    public void onZoomingActionStart(int i) {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onZoomingActionStart(): ");
        sb.append(ZoomingAction.toString(i));
        sb.append(" @hash: ");
        sb.append(hashCode());
        Log.d(str, sb.toString());
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null && topAlert.isExtraMenuShowing()) {
            topAlert.hideExtraMenu();
        }
    }

    /* access modifiers changed from: protected */
    public void pauseMediaRecorder(MediaRecorder mediaRecorder) {
        try {
            mediaRecorder.pause();
        } catch (IllegalArgumentException e) {
            Log.e(VideoBase.TAG, e.getMessage());
        }
    }

    public void pausePreview() {
        Log.v(VideoBase.TAG, "pausePreview");
        this.mPreviewing = false;
        if (CameraSchedulers.isOnMainThread()) {
            stopObjectTracking(false);
        }
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
    public void pauseVideoRecording() {
        Log.d(VideoBase.TAG, "pauseVideoRecording");
        if (this.mMediaRecorderRecording && !this.mMediaRecorderRecordingPaused) {
            try {
                pauseMediaRecorder(this.mMediaRecorder);
            } catch (IllegalStateException unused) {
                Log.e(VideoBase.TAG, "failed to pause media recorder");
            }
            this.mVideoRecordedDuration = SystemClock.uptimeMillis() - this.mRecordingStartTime;
            this.mMediaRecorderRecordingPaused = true;
            this.mHandler.removeMessages(42);
            updateRecordingTime();
        }
    }

    public void reShowMoon() {
        if (CameraSettings.isAutoZoomEnabled(this.mModuleIndex)) {
            notifyAutoZoomStartUiHint();
        }
    }

    /* access modifiers changed from: protected */
    public void readVideoPreferences() {
        int i;
        String str;
        String str2;
        DataItemBase dataItemBase;
        String string;
        int i2;
        int preferVideoQuality = !ModuleManager.isVideoNewSlowMotion() ? CameraSettings.getPreferVideoQuality(this.mActualCameraId, this.mModuleIndex) : 6;
        int videoQuality = getActivity().getCameraIntentManager().getVideoQuality();
        if (videoQuality > -1) {
            preferVideoQuality = videoQuality == 1 ? CameraSettings.getPreferVideoQuality(this.mActualCameraId, this.mModuleIndex) : videoQuality == 0 ? videoQuality : CameraSettings.getPreferVideoQuality(String.valueOf(videoQuality), this.mActualCameraId, this.mModuleIndex);
        }
        this.mSpeed = DataRepository.dataItemRunning().getVideoSpeed();
        int i3 = this.mModuleIndex;
        String str3 = CameraSettings.VIDEO_MODE_FILM_EXPOSUREDELAY;
        if (i3 == 208) {
            this.mSpeed = str3;
        } else if (i3 == 172) {
            this.mSpeed = CameraSettings.VIDEO_MODE_960;
        } else {
            this.mSlowModeFps = null;
        }
        this.mTimeBetweenTimeLapseFrameCaptureMs = 0;
        this.mCaptureTimeLapse = false;
        int i4 = 5;
        if (CameraSettings.VIDEO_SPEED_FAST.equals(this.mSpeed) || str3.equals(this.mSpeed)) {
            if (str3.equals(this.mSpeed)) {
                string = FastMotionConstant.FAST_MOTION_SPEED_300X;
            } else {
                if (C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo()) {
                    dataItemBase = DataRepository.dataItemRunning();
                    str2 = DataRepository.dataItemRunning().getComponentRunningFastMotionSpeed().getDefaultValue(160);
                    str = CameraSettings.KEY_NEW_VIDEO_TIME_LAPSE_FRAME_INTERVAL;
                } else {
                    dataItemBase = DataRepository.dataItemGlobal();
                    str2 = getString(R.string.pref_video_time_lapse_frame_interval_default);
                    str = CameraSettings.KEY_VIDEO_TIME_LAPSE_FRAME_INTERVAL;
                }
                string = dataItemBase.getString(str, str2);
            }
            this.mTimeBetweenTimeLapseFrameCaptureMs = Integer.parseInt(string);
            this.mCaptureTimeLapse = this.mTimeBetweenTimeLapseFrameCaptureMs != 0;
            if (this.mCaptureTimeLapse) {
                int i5 = preferVideoQuality + 1000;
                if (i5 < 1000 || i5 > 1008) {
                    i5 += NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
                    this.mCaptureTimeLapse = false;
                    forceToNormalMode();
                    this.mActivity.runOnUiThread(new C0379O000o0o0(this));
                }
            }
            this.mQuality = preferVideoQuality % 1000;
        } else if (this.mModuleIndex == 172) {
            this.mQuality = 6;
            Size size = SIZE_1080;
            int parseInt = Integer.parseInt(DataRepository.dataItemConfig().getComponentConfigSlowMotionQuality().getComponentValue(172));
            if (parseInt == 5) {
                size = SIZE_720;
                this.mQuality = parseInt;
            }
            this.mSlowModeFps = DataRepository.dataItemConfig().getComponentConfigSlowMotion().getComponentValue(172);
            if (isFPS120() || isFPS480()) {
                i2 = 120;
            } else if (isFPS240() || isFPS960()) {
                i2 = 240;
            } else {
                if (isFPS480Direct() || isFPS1920()) {
                    i2 = VIDEO_HFR_FRAME_RATE_480;
                }
                preferVideoQuality = parseInt;
            }
            updateHfrFPSRange(size, i2);
            preferVideoQuality = parseInt;
        } else {
            this.mQuality = preferVideoQuality;
        }
        CamcorderProfile camcorderProfile = this.mProfile;
        if (!(camcorderProfile == null || camcorderProfile.quality % 1000 == this.mQuality)) {
            stopObjectTracking(false);
        }
        this.mProfile = fetchProfile(this.mBogusCameraId, preferVideoQuality);
        CamcorderProfile camcorderProfile2 = this.mProfile;
        if (!CameraSettings.isHdr10Alive(this.mModuleIndex) && !CameraSettings.isHdr10PlusAlive(this.mModuleIndex)) {
            i4 = CameraSettings.getVideoEncoder();
        }
        camcorderProfile2.videoCodec = i4;
        this.mOutputFormat = this.mProfile.fileFormat;
        String str4 = VideoBase.TAG;
        Locale locale = Locale.ENGLISH;
        Object[] objArr = new Object[4];
        objArr[0] = Integer.valueOf(getHSRValue() > 0 ? getHSRValue() : this.mProfile.videoFrameRate);
        objArr[1] = Integer.valueOf(this.mProfile.videoFrameWidth);
        objArr[2] = Integer.valueOf(this.mProfile.videoFrameHeight);
        objArr[3] = Integer.valueOf(this.mProfile.videoCodec);
        Log.d(str4, String.format(locale, "frameRate=%d profileSize=%dx%d codec=%d", objArr));
        this.mFrameRate = ModuleManager.isVideoNewSlowMotion() ? this.mHfrFPSUpper : this.mProfile.videoFrameRate;
        this.mFrameRateTrack = this.mFrameRate;
        if (CameraSettings.isAlgoFPS(this.mModuleIndex)) {
            this.mMaxVideoDurationInMs = 2000;
            return;
        }
        try {
            this.mMaxVideoDurationInMs = this.mActivity.getCameraIntentManager().getVideoDurationTime() * 1000;
        } catch (RuntimeException unused) {
            if (!CameraSettings.is4KHigherVideoQuality(this.mQuality) || this.mCaptureTimeLapse) {
                this.mMaxVideoDurationInMs = 0;
            } else {
                boolean OO0o0oo = C0122O00000o.instance().OO0o0oo();
                if (C0122O00000o.instance().OO0o() && is8KCamcorder()) {
                    i = MAX_DURATION_8K;
                } else if (OO0o0oo && is4KCamcorder()) {
                    i = MAX_DURATION_4K;
                }
                this.mMaxVideoDurationInMs = i;
            }
        }
        int i6 = this.mMaxVideoDurationInMs;
        if (i6 != 0 && i6 < 1000) {
            this.mMaxVideoDurationInMs = 1000;
        }
    }

    public void registerProtocol() {
        super.registerProtocol();
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(167, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(215, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(193, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(935, this);
        getActivity().getImplFactory().initAdditional(getActivity(), 164, 174, 234, 212, 227);
        CameraClickObservable cameraClickObservable = (CameraClickObservable) ModeCoordinatorImpl.getInstance().getAttachProtocol(227);
        if (cameraClickObservable != null) {
            cameraClickObservable.addObservable(new int[]{R.string.fps960_toast}, this.mCameraClickObserverAction, 171);
        }
    }

    /* access modifiers changed from: protected */
    public void releaseMediaRecorder() {
        MediaRecorder mediaRecorder;
        Log.v(VideoBase.TAG, "releaseRecorder");
        synchronized (this.mLock) {
            mediaRecorder = this.mMediaRecorder;
            this.mMediaRecorder = null;
        }
        if (mediaRecorder != null) {
            cleanupEmptyFile();
            long currentTimeMillis = System.currentTimeMillis();
            mediaRecorder.reset();
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("releaseRecorder: t1=");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            Log.v(str, sb.toString());
            long currentTimeMillis2 = System.currentTimeMillis();
            mediaRecorder.release();
            String str2 = VideoBase.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("releaseRecorder: t2=");
            sb2.append(System.currentTimeMillis() - currentTimeMillis2);
            Log.v(str2, sb2.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void releaseRecorderSurface() {
        Surface surface = this.mRecorderSurface;
        if (surface != null) {
            surface.release();
        }
    }

    /* access modifiers changed from: protected */
    public void resizeForPreviewAspectRatio() {
        int i;
        float f;
        int sensorOrientation = ((this.mCameraCapabilities.getSensorOrientation() - Util.getDisplayRotation(this.mActivity)) + m.cQ) % 180;
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        CameraSize cameraSize = this.mVideoSize;
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

    /* access modifiers changed from: protected */
    public void resumeVideoRecording(RecordState recordState) {
        try {
            this.mMediaRecorder.resume();
            this.mRecordingStartTime = SystemClock.uptimeMillis() - this.mVideoRecordedDuration;
            this.mVideoRecordedDuration = 0;
            this.mMediaRecorderRecordingPaused = false;
            this.mHandler.removeMessages(42);
            this.mRecordingTime = "";
            updateRecordingTime();
            if (this.mIsSubtitleSupported && this.mSubtitleRecording != null) {
                this.mSubtitleRecording.handleSubtitleRecordingResume();
            }
            updateVideoTagState(3);
            recordState.onResume();
            CameraStatUtils.trackPauseOrResumeVideoRecording(isFrontCamera(), true);
        } catch (IllegalStateException e) {
            Log.e(VideoBase.TAG, "failed to resume media recorder", (Throwable) e);
            releaseMediaRecorder();
            recordState.onFailed();
            updateVideoTagState(4);
        }
    }

    public void set3DAudioParameter() {
        super.set3DAudioParameter();
        if (C0122O00000o.instance().OO0oO0O()) {
            int i = this.mModuleIndex;
            if (i == 162 || i == 180) {
                if ((this.mMediaRecorderRecording || this.mMediaRecorderRecordingPaused) && DataRepository.dataItemRunning().getComponentRunningAiAudio().getCurrentRecType(this.mModuleIndex) == 3) {
                    setCurrentAiAudioParameters(true);
                }
            }
        }
    }

    public void setAutoZoomMode(int i) {
        updatePreferenceInWorkThread(51);
    }

    public void setAutoZoomStartCapture(RectF rectF) {
        if (this.mCamera2Device != null && isAlive()) {
            this.mCamera2Device.setAutoZoomStartCapture(new float[]{rectF.left, rectF.top, rectF.width(), rectF.height()}, this.mMediaRecorderRecording);
        }
    }

    public void setAutoZoomStopCapture(int i) {
        if (this.mCamera2Device != null && isAlive()) {
            this.mCamera2Device.setAutoZoomStopCapture(i, this.mMediaRecorderRecording);
        }
    }

    public void setGainValue(String str) {
        mGainVal = str;
        if (getModuleIndex() == 180) {
            SoundSetting.setGainState(getActivity(), mGainVal);
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldCheckSatFallbackState() {
        return HybridZoomingSystem.IS_3_OR_MORE_SAT && isInVideoSAT() && !this.mInStartingFocusRecording;
    }

    public void startAiLens() {
    }

    public void startAutoZoom() {
        this.isShowOrHideUltraWideHint.getAndSet(true);
        this.isAutoZoomTracking.getAndSet(false);
        this.mHandler.post(new Runnable() {
            public void run() {
                if (VideoModule.this.mAutoZoomViewProtocol != null) {
                    VideoModule.this.mAutoZoomViewProtocol.onAutoZoomStarted();
                }
            }
        });
        notifyAutoZoomStartUiHint();
    }

    public void startObjectTracking() {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startObjectTracking: started=");
        sb.append(this.mObjectTrackingStarted);
        Log.d(str, sb.toString());
    }

    public void startPreview() {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startPreview: previewing=");
        sb.append(this.mPreviewing);
        Log.v(str, sb.toString());
        checkDisplayOrientation();
        this.mPreviewing = true;
    }

    public void startTracking(RectF rectF) {
        if (this.mCamera2Device != null && isAlive()) {
            TopAlert topAlert = this.mTopAlert;
            if (topAlert != null) {
                topAlert.alertAiDetectTipHint(4, 0, 0);
            }
            notifyAutoZoomStopUiHint();
            this.mCamera2Device.setAutoZoomStopCapture(-1, this.mMediaRecorderRecording);
            this.mCamera2Device.setAutoZoomStartCapture(new float[]{rectF.left, rectF.top, rectF.width(), rectF.height()}, this.mMediaRecorderRecording);
            this.mCamera2Device.setAutoZoomStartCapture(new float[]{0.0f, 0.0f, 0.0f, 0.0f}, this.mMediaRecorderRecording);
            this.isAutoZoomTracking.getAndSet(true);
            CameraStatUtils.trackSelectObject(this.mMediaRecorderRecording);
        }
    }

    /* access modifiers changed from: protected */
    public void startVideoPreviewSession() {
        Log.d(VideoBase.TAG, "startPreviewSession");
        if (isDeviceAlive()) {
            checkDisplayOrientation();
            this.mCamera2Device.setFocusCallback(this);
            this.mCamera2Device.setErrorCallback(this.mErrorCallback);
            this.mCamera2Device.setPictureSize(this.mPreviewSize);
            Surface surface = new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture());
            this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
            this.mCamera2Device.startVideoPreviewSession(surface, 0, 0, null, getOperatingMode(), false, this);
            this.mFocusManager.resetFocused();
            this.mPreviewing = true;
        }
    }

    /* access modifiers changed from: protected */
    public void startVideoRecording() {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startVideoRecording: mode=");
        sb.append(this.mSpeed);
        Log.k(2, str, sb.toString());
        if (isDeviceAlive()) {
            setCameraAudioRestriction(true);
            SoundSetting.setNoiseReductionState(getActivity(), getModuleIndex(), true);
            if (SoundSetting.isStartKaraoke(getActivity(), getModuleIndex())) {
                SoundSetting.openKaraokeEquipment(getActivity(), getModuleIndex());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("SoundSetting.isStartKaraoke121");
                sb2.append(SoundSetting.isStartKaraoke(getActivity(), getModuleIndex()));
                Log.d("isStartKaraoke", sb2.toString());
                this.mIsStopKaraoke = true;
                this.mAudioMonitorPlayer.startPlay();
                SoundSetting.openKaraokeState(getActivity(), getModuleIndex());
            }
            ScenarioTrackUtil.trackStartVideoRecordStart(this.mSpeed, isFrontCamera());
            this.mCurrentFileNumber = isCaptureIntent() ? -1 : 0;
            Handler handler = this.mHandler;
            if (handler != null && handler.hasMessages(64)) {
                this.mHandler.removeMessages(64);
                this.mHandler.sendEmptyMessage(64);
            }
            BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
            if (bluetoothHeadset == null || !bluetoothHeadset.isSupportBluetoothSco(getModuleIndex())) {
                silenceOuterAudio();
            }
            if (!startRecorder()) {
                onStartRecorderFail();
                if (C0122O00000o.instance().OOOOo0() && CameraSettings.is4KHigherVideoQuality(this.mQuality)) {
                    int hSRValue = getHSRValue();
                    if (hSRValue <= 0) {
                        hSRValue = this.mProfile.videoFrameRate;
                    }
                    ThermalHelper.notifyThermalRecordStop(this.mQuality, hSRValue);
                }
                return;
            }
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onStart();
                updateVideoTagState(1);
            }
            updatePreferenceTrampoline(UpdateConstant.VIDEO_TYPES_RECORD);
            if (ModuleManager.isVideoNewSlowMotion()) {
                this.mCamera2Device.startHighSpeedRecording();
            } else {
                this.mCamera2Device.startRecording(false);
            }
            Log.v(VideoBase.TAG, "startVideoRecording process done");
            this.mTrackLostCount = 0;
            ScenarioTrackUtil.trackStartVideoRecordEnd();
            onStartRecorderSucceed();
            onStartRecorderExposureFilmMode();
            this.mHandler.removeMessages(60);
            this.mHandler.sendEmptyMessage(60);
        }
    }

    public void stopAutoZoom() {
        this.isShowOrHideUltraWideHint.getAndSet(false);
        this.isAutoZoomTracking.getAndSet(false);
        this.mHandler.post(new Runnable() {
            public void run() {
                if (VideoModule.this.mAutoZoomViewProtocol != null) {
                    VideoModule.this.mAutoZoomViewProtocol.onAutoZoomStopped();
                }
            }
        });
        notifyAutoZoomStopUiHint();
    }

    public void stopObjectTracking(boolean z) {
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stopObjectTracking: started=");
        sb.append(this.mObjectTrackingStarted);
        Log.d(str, sb.toString());
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"CheckResult"})
    public void stopRecorder() {
        this.mPendingStopRecorder = false;
        this.mHandler.removeMessages(46);
        if (C0122O00000o.instance().OOOOo0() && CameraSettings.is4KHigherVideoQuality(this.mQuality)) {
            int hSRValue = getHSRValue();
            if (hSRValue <= 0) {
                hSRValue = this.mProfile.videoFrameRate;
            }
            ThermalHelper.notifyThermalRecordStop(this.mQuality, hSRValue);
        }
        Single.create(new SingleOnSubscribe() {
            public void subscribe(SingleEmitter singleEmitter) {
                String str;
                VideoModule.this.mStopRecorderDone = new CountDownLatch(1);
                long currentTimeMillis = System.currentTimeMillis();
                ScenarioTrackUtil.trackStopVideoRecordStart(VideoModule.this.mSpeed, VideoModule.this.isFrontCamera());
                try {
                    VideoModule.this.mMediaRecorder.setOnErrorListener(null);
                    VideoModule.this.mMediaRecorder.setOnInfoListener(null);
                    VideoModule.this.mMediaRecorder.stop();
                } catch (RuntimeException e) {
                    Log.e(VideoBase.TAG, "failed to stop media recorder", (Throwable) e);
                    VideoModule videoModule = VideoModule.this;
                    String str2 = videoModule.mCurrentVideoFilename;
                    if (str2 != null) {
                        videoModule.deleteVideoFile(str2);
                        VideoModule.this.mCurrentVideoFilename = null;
                    }
                }
                if (!VideoModule.this.mPaused && !VideoModule.this.mActivity.isActivityPaused()) {
                    VideoModule.this.playCameraSound(3);
                }
                VideoModule.this.releaseMediaRecorder();
                VideoModule.this.mStopRecorderDone.countDown();
                String str3 = VideoBase.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("releaseTime=");
                sb.append(System.currentTimeMillis() - currentTimeMillis);
                Log.d(str3, sb.toString());
                long uptimeMillis = SystemClock.uptimeMillis();
                VideoModule videoModule2 = VideoModule.this;
                long j = uptimeMillis - videoModule2.mRecordingStartTime;
                if (videoModule2.mCamera2Device != null && ModuleManager.isVideoNewSlowMotion() && (VideoModule.this.isFPS120() || VideoModule.this.isFPS240())) {
                    CameraStatUtils.trackNewSlowMotionVideoRecorded(VideoModule.this.isFPS120() ? CameraSettings.VIDEO_MODE_120 : CameraSettings.VIDEO_MODE_240, VideoModule.this.mQuality, VideoModule.this.mCamera2Device.getFlashMode(), VideoModule.this.mFrameRate, j / 1000);
                }
                VideoModule videoModule3 = VideoModule.this;
                if (videoModule3.mCurrentVideoFilename != null && CameraSettings.isAlgoFPS(videoModule3.mModuleIndex)) {
                    boolean access$900 = VideoModule.this.mMediaRecorderPostProcessing;
                    String str4 = BaseEvent.FEATURE_NAME;
                    String str5 = VideoAttr.KEY_VIDEO_960;
                    if (!access$900 || !VideoModule.this.isActivityResumed()) {
                        String str6 = VideoBase.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("uncomplete video.=");
                        sb2.append(j);
                        Log.d(str6, sb2.toString());
                        VideoModule videoModule4 = VideoModule.this;
                        videoModule4.deleteVideoFile(videoModule4.mCurrentVideoFilename);
                        VideoModule.this.mCurrentVideoFilename = null;
                        str = VideoAttr.VALUE_FPS960_TOO_SHORT;
                    } else {
                        if (C0124O00000oO.Oo000o0()) {
                            if (VideoModule.this.mBoostFramework == null) {
                                VideoModule.this.mBoostFramework = new BoostFrameworkImpl();
                            }
                            if (VideoModule.this.mBoostFramework != null) {
                                Log.d(VideoBase.TAG, "postProcessVideo boost");
                                VideoModule.this.mBoostFramework.startBoost(8000, 1);
                            }
                        }
                        long currentTimeMillis2 = System.currentTimeMillis();
                        VideoModule videoModule5 = VideoModule.this;
                        String access$1200 = videoModule5.postProcessVideo(videoModule5.mCurrentVideoFilename);
                        String str7 = VideoBase.TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("postProcessVideo processTime=");
                        sb3.append(System.currentTimeMillis() - currentTimeMillis2);
                        Log.k(3, str7, sb3.toString());
                        if (C0124O00000oO.Oo000o0() && VideoModule.this.mBoostFramework != null) {
                            VideoModule.this.mBoostFramework.stopBoost();
                        }
                        if (access$1200 == null) {
                            VideoModule videoModule6 = VideoModule.this;
                            videoModule6.mCurrentVideoFilename = null;
                            videoModule6.mCurrentVideoValues = null;
                            str = VideoAttr.VALUE_FPS960_PROCESS_FAILED;
                        } else {
                            VideoModule videoModule7 = VideoModule.this;
                            videoModule7.mCurrentVideoFilename = access$1200;
                            videoModule7.mCurrentVideoValues.put("_data", access$1200);
                            VideoModule videoModule8 = VideoModule.this;
                            if (videoModule8.mCamera2Device != null) {
                                CameraStatUtils.trackNewSlowMotionVideoRecorded(videoModule8.isFPS480() ? CameraSettings.VIDEO_MODE_480 : CameraSettings.VIDEO_MODE_960, VideoModule.this.mQuality, VideoModule.this.mCamera2Device.getFlashMode(), VideoModule.this.isFPS480() ? VideoModule.VIDEO_HFR_FRAME_RATE_480 : 960, 10);
                            }
                        }
                    }
                    MistatsWrapper.keyTriggerEvent(str5, str4, str);
                }
                singleEmitter.onSuccess(Boolean.TRUE);
            }
        }).subscribeOn(CameraSchedulers.sCameraSetupScheduler).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new C0378O000o0o(this));
    }

    public void stopTracking(int i) {
        if (this.isAutoZoomTracking.get()) {
            this.isAutoZoomTracking.getAndSet(false);
            if (this.mCamera2Device != null && isAlive()) {
                this.mCamera2Device.setAutoZoomStopCapture(0, this.mMediaRecorderRecording);
                this.mCamera2Device.setAutoZoomStopCapture(-1, this.mMediaRecorderRecording);
            }
            this.mAutoZoomViewProtocol.onTrackingStopped(i);
        }
        notifyAutoZoomStartUiHint();
    }

    /* JADX WARNING: Removed duplicated region for block: B:122:0x023a  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x023e  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x029c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stopVideoRecording(boolean z) {
        String str;
        String str2;
        float min;
        String str3 = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stopVideoRecording>>");
        sb.append(this.mMediaRecorderRecording);
        Log.k(2, str3, sb.toString());
        if (this.mMediaRecorderRecording) {
            boolean z2 = false;
            setCameraAudioRestriction(false);
            if (z || this.mModuleIndex != 207 || SystemClock.uptimeMillis() - this.mRecordingStartTime >= 1000) {
                if (this.isAutoZoomTracking.get()) {
                    stopTracking(0);
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
                this.mMediaRecorderRecording = false;
                this.mMediaRecorderRecordingPaused = false;
                long currentTimeMillis = System.currentTimeMillis();
                if (CameraSettings.isAlgoFPS(this.mModuleIndex)) {
                    if (2000 - (SystemClock.uptimeMillis() - this.mRecordingStartTime) <= 100) {
                        this.mMediaRecorderPostProcessing = true;
                    }
                }
                if (this.mIsSubtitleSupported) {
                    SubtitleRecording subtitleRecording = this.mSubtitleRecording;
                    if (subtitleRecording != null) {
                        subtitleRecording.handleSubtitleRecordingStop();
                    }
                }
                if (this.mModuleIndex == 180) {
                    TopAlert topAlert = this.mTopAlert;
                    if (topAlert != null) {
                        topAlert.updateProVideoRecordingSimpleView(false);
                    }
                }
                MainContentProtocol mainContentProtocol = this.mMainProtocol;
                if (mainContentProtocol != null) {
                    mainContentProtocol.processingFinish();
                    updateVideoTagState(4);
                }
                handleAiAudioTipsState(false);
                if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                    int i = this.mModuleIndex;
                    if (i != 172 && i != 180 && i != 214 && !CameraSettings.isMacroModeEnabled(i) && !CameraSettings.isAutoZoomEnabled(this.mModuleIndex) && !CameraSettings.isSuperEISEnabled(this.mModuleIndex) && isBackCamera()) {
                        updateZoomRatioToggleButtonState(false);
                        if (!isUltraWideBackCamera()) {
                            if (!DataRepository.dataItemGlobal().isNormalIntent() && this.mCameraCapabilities.isSupportLightTripartite()) {
                                setMinZoomRatio(1.0f);
                            } else if (C0122O00000o.instance().OOoOo()) {
                                if (CameraSettings.isVhdrOn(this.mCameraCapabilities, this.mModuleIndex)) {
                                    setMinZoomRatio(1.0f);
                                } else {
                                    setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                                }
                                if (isInVideoSAT()) {
                                    min = C0122O00000o.instance().O0oooO0();
                                } else if (isStandaloneMacroCamera()) {
                                    setMinZoomRatio(1.0f);
                                    min = Math.min(2.0f, this.mCameraCapabilities.getMaxZoomRatio());
                                }
                            } else {
                                setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                                if (!isInVideoSAT()) {
                                    setVideoMaxZoomRatioByTele();
                                }
                            }
                            min = Math.min(6.0f, this.mCameraCapabilities.getMaxZoomRatio());
                        } else if (CameraSettings.isMacroModeEnabled(this.mModuleIndex)) {
                            setMinZoomRatio(HybridZoomingSystem.getMinimumMacroOpticalZoomRatio());
                            min = HybridZoomingSystem.getMaximumMacroOpticalZoomRatio();
                        } else {
                            setMinZoomRatio(HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR);
                            min = HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR * this.mCameraCapabilities.getMaxZoomRatio();
                        }
                        setMaxZoomRatio(min);
                    }
                }
                enableCameraControls(false);
                Camera2Proxy camera2Proxy = this.mCamera2Device;
                if (camera2Proxy != null) {
                    camera2Proxy.stopRecording();
                }
                if (this.mCountDownTimer != null && CameraSettings.isVideoBokehOn()) {
                    this.mCountDownTimer.cancel();
                }
                RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
                if (recordState != null) {
                    if (this.mMediaRecorderPostProcessing) {
                        recordState.onPostSavingStart();
                    } else {
                        if (!CameraSettings.VIDEO_MODE_FILM_EXPOSUREDELAY.equals(this.mSpeed)) {
                            recordState.onFinish();
                        }
                    }
                }
                if (this.mCamera2Device != null && !ModuleManager.isVideoNewSlowMotion()) {
                    boolean isAutoZoomEnabled = CameraSettings.isAutoZoomEnabled(this.mModuleIndex);
                    boolean isSuperEISEnabled = CameraSettings.isSuperEISEnabled(this.mModuleIndex);
                    String str4 = this.mSpeed;
                    if (isFPS120() || isFPS240() || isFPS480Direct() || CameraSettings.isAlgoFPS(this.mModuleIndex)) {
                        str2 = VideoAttr.VALUE_SPEED_SLOW;
                    } else if (this.mModuleIndex == 214) {
                        str2 = VideoAttr.VALUE_SUPER_NIGHT_VIDEO;
                    } else {
                        str = str4;
                        ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
                        if (componentRunningMacroMode != null && componentRunningMacroMode.isSwitchOn(getModuleIndex())) {
                            HashMap hashMap = new HashMap();
                            hashMap.put(MacroAttr.PARAM_SLOW_MOTION_MACRO, this.mSlowModeFps);
                            MistatsWrapper.mistatEvent(MacroAttr.FUCNAME_MACRO_MODE, hashMap);
                        }
                        if (this.mModuleIndex != 180) {
                            trackProVideoInfo();
                        } else {
                            BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
                            if (bluetoothHeadset != null) {
                                z2 = bluetoothHeadset.isBluetoothScoOn();
                            }
                            CameraStatUtils.trackVideoRecorded(isFrontCamera(), getActualCameraId(), getModuleIndex(), isAutoZoomEnabled, isSuperEISEnabled, CameraSettings.isUltraWideConfigOpen(getModuleIndex()), str, this.mQuality, this.mCamera2Device.getFlashMode(), this.mFrameRateTrack, this.mTimeBetweenTimeLapseFrameCaptureMs, this.mBeautyValues, this.mVideoRecordTime, this.mIsSubtitleSupported, getAIAudioTrackParams(), z2, this.mIsAutoHibernationSupported, this.mEnterAutoHibernationCount, CameraSettings.isVhdrOn(this.mCameraCapabilities, this.mModuleIndex));
                        }
                        if (isAutoZoomEnabled) {
                            String str5 = VideoBase.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("track count is ");
                            sb2.append(this.mTrackLostCount);
                            Log.v(str5, sb2.toString());
                            CameraStatUtils.trackLostCount(this.mTrackLostCount);
                        }
                    }
                    str = str2;
                    ComponentRunningMacroMode componentRunningMacroMode2 = DataRepository.dataItemRunning().getComponentRunningMacroMode();
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put(MacroAttr.PARAM_SLOW_MOTION_MACRO, this.mSlowModeFps);
                    MistatsWrapper.mistatEvent(MacroAttr.FUCNAME_MACRO_MODE, hashMap2);
                    if (this.mModuleIndex != 180) {
                    }
                    if (isAutoZoomEnabled) {
                    }
                }
                this.mVideoRecordTime = 0;
                stopRecorder();
                startPreviewAfterRecord();
                handleTempVideoFile();
                AutoLockManager.getInstance(this.mActivity).hibernateDelayed();
                if (this.mCaptureTimeLapse && (C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo())) {
                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                    if (configChanges != null) {
                        configChanges.reCheckFastMotion(true);
                    }
                }
                exitSavePowerMode();
                if (this.mIsAutoHibernationSupported) {
                    exitAutoHibernation();
                    AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
                    if (autoHibernation != null) {
                        autoHibernation.dismissAutoHibernation();
                    }
                }
                BluetoothHeadset bluetoothHeadset2 = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
                if (bluetoothHeadset2 != null) {
                    bluetoothHeadset2.startBluetoothSco(getModuleIndex());
                }
                String str6 = VideoBase.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("stopVideoRecording<<time=");
                sb3.append(System.currentTimeMillis() - currentTimeMillis);
                Log.v(str6, sb3.toString());
            }
        }
    }

    public void takePreviewSnapShoot() {
        if (getCameraState() != 3) {
            setCameraState(3);
            this.mCamera2Device.setShotType(-8);
            this.mCamera2Device.takeSimplePicture(this, this.mActivity.getImageSaver(), this.mActivity.getCameraScreenNail());
        }
    }

    public boolean takeVideoSnapShoot() {
        if (!this.mEnableVideoSnapshot) {
            takePreviewSnapShoot();
            return false;
        } else if (this.mPaused || this.mActivity.isActivityPaused() || this.mSnapshotInProgress || !this.mMediaRecorderRecording || !isDeviceAlive()) {
            return false;
        } else {
            if (Storage.isLowStorageAtLastPoint()) {
                Log.w(VideoBase.TAG, "capture: low storage");
                stopVideoRecording(false);
                return false;
            } else if (this.mActivity.getImageSaver().isBusy()) {
                Log.w(VideoBase.TAG, "capture: ImageSaver is full");
                RotateTextToast.getInstance(this.mActivity).show(R.string.toast_saving, 0);
                return false;
            } else {
                this.mCamera2Device.setJpegRotation(Util.getJpegRotation(this.mBogusCameraId, this.mOrientation));
                Location currentLocation = LocationManager.instance().getCurrentLocation();
                this.mCamera2Device.setGpsLocation(currentLocation);
                setJpegQuality();
                updateFrontMirror();
                this.mActivity.getCameraScreenNail().animateCapture(getCameraRotation());
                Log.v(VideoBase.TAG, "capture: start");
                this.mCamera2Device.captureVideoSnapshot(new JpegPictureCallback(currentLocation));
                this.mSnapshotInProgress = true;
                CameraStatUtils.trackVideoSnapshot(isFrontCamera());
                return true;
            }
        }
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        ModeCoordinatorImpl.getInstance().detachProtocol(215, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(167, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(193, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(935, this);
        getActivity().getImplFactory().detachAdditional();
    }

    /* access modifiers changed from: protected */
    public void updateAutoHibernationFirstRecordingTime() {
        AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
        if (autoHibernation != null) {
            autoHibernation.updateAutoHibernationFirstRecordingTime(this.mRecordingTime, this.mRecordingSecondTime);
        }
    }

    /* access modifiers changed from: protected */
    public void updateFpsRange() {
        if (isDeviceAlive()) {
            if (ModuleManager.isVideoNewSlowMotion()) {
                String str = VideoBase.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("mHfrFPSLower = ");
                sb.append(this.mHfrFPSLower);
                sb.append(", mHfrFPSUpper = ");
                sb.append(this.mHfrFPSUpper);
                Log.k(3, str, sb.toString());
                this.mCamera2Device.setVideoFpsRange(new Range(Integer.valueOf(this.mHfrFPSLower), Integer.valueOf(this.mHfrFPSUpper)));
            } else if (this.mModuleIndex == 208) {
                Range supportedVideoExposureDelay = this.mCameraCapabilities.getSupportedVideoExposureDelay();
                String str2 = VideoBase.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("MODE_FILM_EXPOSUREDELAY bestRange = ");
                sb2.append(supportedVideoExposureDelay);
                Log.d(str2, sb2.toString());
                this.mCamera2Device.setFpsRange(supportedVideoExposureDelay);
                this.mCamera2Device.setVideoFpsRange(supportedVideoExposureDelay);
                this.mCamera2Device.setTuningMode(1);
            } else {
                Range[] supportedFpsRange = this.mCameraCapabilities.getSupportedFpsRange();
                int i = 0;
                Range range = supportedFpsRange[0];
                int length = supportedFpsRange.length;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Range range2 = supportedFpsRange[i];
                    int hSRValue = getHSRValue();
                    if (hSRValue == 60) {
                        range = CameraSettings.isVideoDynamic60fpsOn(this.mModuleIndex) ? new Range(Integer.valueOf(30), Integer.valueOf(hSRValue)) : new Range(Integer.valueOf(hSRValue), Integer.valueOf(hSRValue));
                    } else {
                        if (hSRValue == 24) {
                            range = Range.create(Integer.valueOf(24), Integer.valueOf(24));
                            this.mFrameRateTrack = 24;
                        } else if (((Integer) range.getUpper()).intValue() < ((Integer) range2.getUpper()).intValue() || (range.getUpper() == range2.getUpper() && ((Integer) range.getLower()).intValue() < ((Integer) range2.getLower()).intValue())) {
                            range = range2;
                        }
                        i++;
                    }
                }
                String str3 = VideoBase.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("bestRange = ");
                sb3.append(range);
                Log.d(str3, sb3.toString());
                this.mCamera2Device.setFpsRange(range);
                this.mCamera2Device.setVideoFpsRange(range);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateHDRPreference() {
        ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
        if (!componentHdr.isEmpty()) {
            boolean equals = "normal".equals(componentHdr.getComponentValue(this.mModuleIndex));
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.applyVideoHdrMode(equals);
            }
        }
    }

    public void updateManualEvAdjust() {
        if (this.mModuleIndex == 167) {
            String manualValue = getManualValue(CameraSettings.KEY_QC_EXPOSURETIME, getString(R.string.pref_camera_exposuretime_default));
            String manualValue2 = getManualValue(CameraSettings.KEY_QC_ISO, getString(R.string.pref_camera_iso_default));
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("MODE_MANUAL: exposureTime = ");
            sb.append(manualValue);
            sb.append("iso = ");
            sb.append(manualValue2);
            Log.d(str, sb.toString());
            boolean z = C0124O00000oO.o00OO00() ? getString(R.string.pref_camera_exposuretime_default).equals(manualValue) : getString(R.string.pref_camera_iso_default).equals(manualValue2) || getString(R.string.pref_camera_exposuretime_default).equals(manualValue);
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new C0380O000o0oo(this, z));
            }
            if (1 == this.mCamera2Device.getFocusMode()) {
                Camera camera = this.mActivity;
                if (camera != null) {
                    camera.runOnUiThread(new C0383O000oO00(this));
                }
                unlockAEAF();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updatePictureAndPreviewSize() {
        int i;
        int i2;
        int i3;
        CamcorderProfile camcorderProfile = this.mProfile;
        double d = ((double) camcorderProfile.videoFrameWidth) / ((double) camcorderProfile.videoFrameHeight);
        List supportedOutputSizeWithAssignedMode = this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(MediaRecorder.class);
        CamcorderProfile camcorderProfile2 = this.mProfile;
        CameraSize optimalVideoSnapshotPictureSize = Util.getOptimalVideoSnapshotPictureSize(supportedOutputSizeWithAssignedMode, d, camcorderProfile2.videoFrameWidth, camcorderProfile2.videoFrameHeight);
        this.mVideoSize = optimalVideoSnapshotPictureSize;
        String str = VideoBase.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("videoSize: ");
        sb.append(optimalVideoSnapshotPictureSize.toString());
        Log.k(3, str, sb.toString());
        int i4 = Integer.MAX_VALUE;
        if (C0124O00000oO.Oo0O0O()) {
            i4 = optimalVideoSnapshotPictureSize.width;
            i = optimalVideoSnapshotPictureSize.height;
        } else {
            i = Integer.MAX_VALUE;
        }
        this.mPictureSize = Util.getOptimalVideoSnapshotPictureSize(this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(256), d, i4, i);
        String str2 = VideoBase.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("pictureSize: ");
        sb2.append(this.mPictureSize);
        Log.k(3, str2, sb2.toString());
        if (optimalVideoSnapshotPictureSize.width <= Display.getWindowHeight()) {
            i2 = optimalVideoSnapshotPictureSize.width;
            if (i2 >= 720) {
                i3 = optimalVideoSnapshotPictureSize.height;
                this.mPreviewSize = Util.getOptimalVideoSnapshotPictureSize(this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class), d, i2, i3);
                CameraSize cameraSize = this.mPreviewSize;
                updateCameraScreenNailSize(cameraSize.width, cameraSize.height);
            }
        }
        int windowHeight = Display.getWindowHeight();
        int windowWidth = Display.getWindowWidth();
        int[] O0ooo = C0122O00000o.instance().O0ooo();
        if (O0ooo != null && O0ooo.length == 2) {
            windowHeight = O0ooo[0] > Display.getWindowHeight() ? Display.getWindowHeight() : O0ooo[0];
            windowWidth = O0ooo[1] > Display.getWindowWidth() ? Display.getWindowWidth() : O0ooo[1];
        }
        int i5 = windowWidth;
        i2 = windowHeight;
        i3 = i5;
        this.mPreviewSize = Util.getOptimalVideoSnapshotPictureSize(this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class), d, i2, i3);
        CameraSize cameraSize2 = this.mPreviewSize;
        updateCameraScreenNailSize(cameraSize2.width, cameraSize2.height);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x013b  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0143  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateRecordingTime() {
        String str;
        super.updateRecordingTime();
        if (this.mMediaRecorderRecording && !CameraSettings.isAlgoFPS(this.mModuleIndex) && !CameraSettings.isVideoBokehOn()) {
            long uptimeMillis = SystemClock.uptimeMillis() - this.mRecordingStartTime;
            if (this.mMediaRecorderRecordingPaused) {
                uptimeMillis = this.mVideoRecordedDuration;
            }
            int i = this.mMaxVideoDurationInMs;
            boolean z = i != 0 && uptimeMillis >= ((long) (i - 60000));
            long max = z ? Math.max(0, ((long) this.mMaxVideoDurationInMs) - uptimeMillis) + 999 : uptimeMillis;
            boolean isNormalMode = isNormalMode();
            String str2 = CameraSettings.VIDEO_MODE_FILM_EXPOSUREDELAY;
            long j = 1000;
            if (isNormalMode) {
                this.mVideoRecordTime = max / 1000;
            } else {
                if (CameraSettings.VIDEO_SPEED_FAST.equals(this.mSpeed) || str2.equals(this.mSpeed)) {
                    double d = (double) this.mTimeBetweenTimeLapseFrameCaptureMs;
                    long j2 = (long) d;
                    str = ((this.mModuleIndex != 169 || !C0122O00000o.instance().OOO00Oo()) && !str2.equals(this.mSpeed)) ? Util.millisecondToTimeString(getSpeedRecordVideoLength(uptimeMillis, d), true) : Util.millisecondToTimeString(getSpeedRecordVideoLength(uptimeMillis, d), false);
                    str.equals(this.mRecordingTime);
                    if (((this.mModuleIndex != 169 || !C0122O00000o.instance().OOO00Oo()) && !str2.equals(this.mSpeed)) || j2 <= 1000) {
                        j = j2;
                    }
                    String millisecondToTimeString = Util.millisecondToTimeString(uptimeMillis, false, false, true);
                    if (this.mTopAlert != null) {
                        if ((this.mModuleIndex != 169 || !C0122O00000o.instance().OOO00Oo()) && !str2.equals(this.mSpeed)) {
                            this.mTopAlert.updateRecordingTime(str);
                        } else {
                            this.mTopAlert.updateFastmotionProRecordingTime(Util.millisecondToTimeString(uptimeMillis, false), str);
                        }
                        if (this.mIsAutoHibernationSupported) {
                            AutoHibernation autoHibernation = (AutoHibernation) ModeCoordinatorImpl.getInstance().getAttachProtocol(936);
                            int i2 = this.mModuleIndex;
                            if (i2 == 169) {
                                if (autoHibernation != null) {
                                    if (C0122O00000o.instance().OOO00Oo()) {
                                        autoHibernation.updateAutoHibernationRecordingTimeOrCaptureCount(Util.millisecondToTimeString(uptimeMillis, false), str);
                                    } else {
                                        autoHibernation.updateAutoHibernationRecordingTimeOrCaptureCount(Util.millisecondToTimeString(uptimeMillis, false), Util.millisecondToTimeString(getSpeedRecordVideoLength(uptimeMillis, (double) this.mTimeBetweenTimeLapseFrameCaptureMs), false));
                                    }
                                }
                            } else if ((i2 == 162 || i2 == 180) && autoHibernation != null) {
                                autoHibernation.updateAutoHibernationRecordingTimeOrCaptureCount(str, "");
                            }
                        }
                    }
                    this.mRecordingTime = str;
                    this.mRecordingSecondTime = millisecondToTimeString;
                    if (this.mRecordingTimeCountsDown != z) {
                        this.mRecordingTimeCountsDown = z;
                    }
                    long j3 = 500;
                    if (!this.mMediaRecorderRecordingPaused) {
                        j3 = j - (uptimeMillis % j);
                    }
                    this.mHandler.sendEmptyMessageDelayed(42, j3);
                    if (this.mCaptureTimeLapse && this.mTimeLapseDuration > 0 && ((C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo()) && uptimeMillis > this.mTimeLapseDuration)) {
                        stopVideoRecording(false);
                        String str3 = VideoBase.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("updateRecordingTime ");
                        sb.append(uptimeMillis);
                        sb.append(" mTimeLapseDuration ");
                        sb.append(this.mTimeLapseDuration);
                        Log.d(str3, sb.toString());
                    }
                }
            }
            str = Util.millisecondToTimeString(max, false);
            String millisecondToTimeString2 = Util.millisecondToTimeString(uptimeMillis, false, false, true);
            if (this.mTopAlert != null) {
            }
            this.mRecordingTime = str;
            this.mRecordingSecondTime = millisecondToTimeString2;
            if (this.mRecordingTimeCountsDown != z) {
            }
            long j32 = 500;
            if (!this.mMediaRecorderRecordingPaused) {
            }
            this.mHandler.sendEmptyMessageDelayed(42, j32);
            stopVideoRecording(false);
            String str32 = VideoBase.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("updateRecordingTime ");
            sb2.append(uptimeMillis);
            sb2.append(" mTimeLapseDuration ");
            sb2.append(this.mTimeLapseDuration);
            Log.d(str32, sb2.toString());
        }
    }

    public void updateSATZooming(boolean z) {
        if (C0122O00000o.instance().OO0o0oO() && HybridZoomingSystem.IS_3_OR_MORE_SAT && this.mCamera2Device != null && C0122O00000o.instance().OOoOo() && isInVideoSAT()) {
            this.mCamera2Device.setSatIsZooming(z);
            resumePreview();
        }
    }

    /* access modifiers changed from: protected */
    public void updateVideoStabilization() {
        if (isDeviceAlive()) {
            if (needDisableEISAndOIS()) {
                this.mCamera2Device.setEnableEIS(false);
                this.mCamera2Device.setEnableOIS(false);
                this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(false);
                return;
            }
            if (isEisOn()) {
                Log.d(VideoBase.TAG, "videoStabilization: EIS");
                this.mCamera2Device.setEnableOIS(false);
                this.mCamera2Device.setEnableEIS(true);
                if (!this.mCameraCapabilities.isEISPreviewSupported()) {
                    this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(true);
                }
            } else {
                Log.d(VideoBase.TAG, "videoStabilization: OIS");
                this.mCamera2Device.setEnableEIS(false);
                this.mCamera2Device.setEnableOIS(true);
                this.mActivity.getCameraScreenNail().setVideoStabilizationCropped(false);
            }
        }
    }
}
