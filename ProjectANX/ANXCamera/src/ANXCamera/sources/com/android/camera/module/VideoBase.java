package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.SensorEvent;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureResult;
import android.location.Location;
import android.media.AudioSystem;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import androidx.core.app.FrameMetricsAggregator;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.ChangeManager;
import com.android.camera.Display;
import com.android.camera.FileCompat;
import com.android.camera.LocationManager;
import com.android.camera.OnClickAttr;
import com.android.camera.R;
import com.android.camera.SensorStateManager.SensorStateListener;
import com.android.camera.ThermalDetector;
import com.android.camera.Thumbnail;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.constant.AutoFocus;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentConfigSlowMotion;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FaceAnalyzeInfo;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.jcodec.MP4UtilEx.VideoTag;
import com.android.camera.log.Log;
import com.android.camera.module.loader.FunctionFaceDetect;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.module.loader.camera2.FocusManager2.Listener;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.permission.PermissionManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.BluetoothHeadset;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CloneChooser;
import com.android.camera.protocol.ModeProtocol.LiveVVChooser;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.PlayVideoProtocol;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.storage.Storage;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.FaceDetectionCallback;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CameraHardwareFace;
import com.xiaomi.camera.rx.CameraSchedulers;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class VideoBase extends BaseModule implements FaceDetectionCallback, FocusCallback, CameraPreviewCallback, Listener, CameraAction, PlayVideoProtocol {
    protected static final int FILE_NUMBER_SINGLE = -1;
    private static final boolean HOLD_WHEN_SAVING_VIDEO = false;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int MIN_BACK_RECORDING_MINUTE = 20;
    private static final int MIN_FRONT_RECORDING_MINUTE = 10;
    /* access modifiers changed from: protected */
    public static String TAG = null;
    private static final int THREE_MINUTE = 3;
    public boolean m3ALocked;
    protected String mBaseFileName;
    protected BeautyValues mBeautyValues;
    protected CameraCaptureSession mCurrentSession;
    protected String mCurrentVideoFilename;
    protected Uri mCurrentVideoUri;
    protected ContentValues mCurrentVideoValues;
    protected boolean mFaceDetected;
    protected boolean mFaceDetectionEnabled;
    protected boolean mFaceDetectionStarted;
    protected FocusManager2 mFocusManager;
    protected long mIntentRequestSize;
    private boolean mIsSessionReady;
    private boolean mIsVideoCaptureIntent;
    private boolean mIsVideoFaceViewShown;
    protected int mMaxVideoDurationInMs;
    protected boolean mMediaRecorderRecordingPaused;
    private Disposable mMetaDataDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mMetaDataFlowableEmitter;
    protected long mOnResumeTime;
    protected int mOrientationCompensationAtRecordStart;
    protected int mOriginalMusicVolume;
    protected int mOutputFormat = 2;
    protected boolean mPreviewing;
    protected long mRecordingStartTime;
    protected boolean mSavePowerMode;
    protected SensorStateListener mSensorStateListener = new SensorStateListener() {
        public boolean isWorking() {
            return VideoBase.this.isAlive() && VideoBase.this.mPreviewing;
        }

        public void notifyDevicePostureChanged() {
            VideoBase.this.mActivity.getEdgeShutterView().onDevicePostureChanged();
        }

        public void onDeviceBecomeStable() {
            Log.v(VideoBase.TAG, "onDeviceBecomeStable");
        }

        public void onDeviceBeginMoving() {
        }

        public void onDeviceKeepMoving(double d) {
            MainContentProtocol mainContentProtocol = VideoBase.this.mMainProtocol;
            if (mainContentProtocol != null && !mainContentProtocol.isEvAdjusted(true) && !VideoBase.this.mPaused && Util.isTimeout(System.currentTimeMillis(), VideoBase.this.mTouchFocusStartingTime, 3000) && !VideoBase.this.is3ALocked()) {
                VideoBase.this.resetFocusState(d);
            }
        }

        public void onDeviceKeepStable() {
        }

        public void onDeviceLieChanged(boolean z) {
        }

        public void onDeviceOrientationChanged(float f, boolean z) {
            String str = VideoBase.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onDeviceOrientationChanged: ");
            sb.append(f);
            Log.d(str, sb.toString());
            VideoBase videoBase = VideoBase.this;
            if (z) {
                f = (float) videoBase.mOrientation;
            }
            videoBase.mDeviceRotation = f;
            if (VideoBase.this.isGradienterOn) {
                EffectController instance = EffectController.getInstance();
                VideoBase videoBase2 = VideoBase.this;
                instance.setDeviceRotation(z, Util.getShootRotation(videoBase2.mActivity, videoBase2.mDeviceRotation));
            }
        }

        public void onDeviceRotationChanged(float[] fArr) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
        }
    };
    /* access modifiers changed from: protected */
    public boolean mSnapshotInProgress;
    protected StereoSwitchThread mStereoSwitchThread;
    protected long mTouchFocusStartingTime;
    protected volatile boolean mUltraWideAELocked;
    protected ParcelFileDescriptor mVideoFileDescriptor;
    protected String mVideoFocusMode;
    /* access modifiers changed from: protected */
    public CameraSize mVideoSize;

    class MainHandler extends Handler {
        private WeakReference mModule;

        public MainHandler(VideoBase videoBase) {
            this.mModule = new WeakReference(videoBase);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:52:0x00d0, code lost:
            if (r0.mActivity.isActivityPaused() == false) goto L_0x00e0;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(Message message) {
            VideoBase videoBase = (VideoBase) this.mModule.get();
            if (videoBase != null) {
                if (!videoBase.isCreated()) {
                    removeCallbacksAndMessages(null);
                } else if (videoBase.getActivity() != null) {
                    int i = message.what;
                    if (i != 2) {
                        if (i == 4) {
                            if (Util.getDisplayRotation(videoBase.mActivity) != videoBase.mDisplayRotation && !videoBase.mMediaRecorderRecording) {
                                videoBase.startPreview();
                            }
                            if (SystemClock.uptimeMillis() - videoBase.mOnResumeTime < 5000) {
                                sendEmptyMessageDelayed(4, 100);
                            }
                        } else if (i != 17) {
                            boolean z = false;
                            if (i == 35) {
                                boolean z2 = message.arg1 > 0;
                                if (message.arg2 > 0) {
                                    z = true;
                                }
                                videoBase.handleUpdateFaceView(z2, z);
                            } else if (i == 42) {
                                videoBase.updateRecordingTime();
                            } else if (i == 55) {
                                Log.e(VideoBase.TAG, "autoFocus timeout!");
                                videoBase.mFocusManager.resetFocused();
                                videoBase.onWaitingFocusFinished();
                            } else if (i == 60) {
                                Log.d(VideoBase.TAG, "fallback timeout");
                                videoBase.mIsSatFallback = 0;
                                videoBase.mFallbackProcessed = false;
                                videoBase.mLastSatFallbackRequestId = -1;
                            } else if (i != 9) {
                                if (i == 10) {
                                    videoBase.stopVideoRecording(false);
                                } else if (i == 45) {
                                    videoBase.setActivity(null);
                                } else if (i == 46) {
                                    videoBase.onWaitStopCallbackTimeout();
                                } else if (i == 51) {
                                    videoBase.stopVideoRecording(false);
                                } else if (i != 52) {
                                    switch (i) {
                                        case 64:
                                            BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
                                            if (bluetoothHeadset != null) {
                                                if (bluetoothHeadset.isSupportBluetoothSco(videoBase.getModuleIndex())) {
                                                    videoBase.silenceOuterAudio();
                                                }
                                                bluetoothHeadset.startBluetoothSco(videoBase.getModuleIndex());
                                                break;
                                            }
                                            break;
                                        case 65:
                                            sendEmptyMessageDelayed(66, 5000);
                                            videoBase.showAutoHibernationTip();
                                            break;
                                        case 66:
                                            videoBase.enterAutoHibernation();
                                            break;
                                        default:
                                            String str = "no consumer for this message: ";
                                            if (!BaseModule.DEBUG) {
                                                String str2 = VideoBase.TAG;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append(str);
                                                sb.append(message.what);
                                                Log.e(str2, sb.toString());
                                                break;
                                            } else {
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append(str);
                                                sb2.append(message.what);
                                                throw new RuntimeException(sb2.toString());
                                            }
                                    }
                                } else {
                                    videoBase.enterSavePowerMode();
                                }
                                videoBase.mOpenCameraFail = true;
                                videoBase.onCameraException();
                            } else {
                                videoBase.onPreviewStart();
                                videoBase.mStereoSwitchThread = null;
                                if (videoBase.getActivity().getVolumeControlStream() != 1) {
                                    videoBase.getActivity().setVolumeControlStream(1);
                                }
                            }
                        } else {
                            removeMessages(17);
                            removeMessages(2);
                            videoBase.getWindow().addFlags(128);
                            sendEmptyMessageDelayed(2, (long) videoBase.getScreenDelay());
                        }
                    }
                    videoBase.getWindow().clearFlags(128);
                }
            }
        }
    }

    interface OnTagsListener {
        void onTagsReady(List list);
    }

    public class StereoSwitchThread extends Thread {
        private volatile boolean mCancelled;

        protected StereoSwitchThread() {
        }

        public void cancel() {
            this.mCancelled = true;
        }

        public void run() {
            VideoBase.this.closeCamera();
            if (!this.mCancelled) {
                VideoBase.this.openCamera();
                if (VideoBase.this.hasCameraException()) {
                    VideoBase.this.onCameraException();
                } else if (!this.mCancelled) {
                    CameraSettings.resetRetainZoom();
                    CameraSettings.resetExposure();
                    VideoBase.this.onCameraOpened();
                    VideoBase.this.readVideoPreferences();
                    VideoBase.this.resizeForPreviewAspectRatio();
                    if (!this.mCancelled) {
                        VideoBase.this.startPreview();
                        VideoBase.this.onPreviewStart();
                        VideoBase.this.mHandler.sendEmptyMessage(9);
                    }
                }
            }
        }
    }

    public VideoBase(String str) {
        TAG = str;
        this.mHandler = new MainHandler(this);
    }

    private String createName(long j, int i) {
        if (i > 0) {
            return this.mBaseFileName;
        }
        this.mBaseFileName = new SimpleDateFormat(getString(R.string.video_file_name_format), Locale.ENGLISH).format(new Date(j));
        return this.mBaseFileName;
    }

    private void deleteCurrentVideo() {
        String str = this.mCurrentVideoFilename;
        if (str != null) {
            deleteVideoFile(str);
            this.mCurrentVideoFilename = null;
            Uri uri = this.mCurrentVideoUri;
            if (uri != null) {
                Util.safeDelete(uri, null, null);
                this.mCurrentVideoUri = null;
            }
        }
        this.mActivity.getScreenHint().updateHint();
    }

    private Bitmap getReviewBitmap() {
        Bitmap bitmap;
        ParcelFileDescriptor parcelFileDescriptor = this.mVideoFileDescriptor;
        if (parcelFileDescriptor != null) {
            bitmap = Thumbnail.createVideoThumbnailBitmap(parcelFileDescriptor.getFileDescriptor(), this.mDisplayRect.width(), this.mDisplayRect.height());
        } else {
            String str = this.mCurrentVideoFilename;
            bitmap = str != null ? Thumbnail.createVideoThumbnailBitmap(str, this.mDisplayRect.width(), this.mDisplayRect.height()) : null;
        }
        if (bitmap == null) {
            return bitmap;
        }
        boolean z = isFrontCamera() && !C0124O00000oO.OOooOoO() && (!C0122O00000o.instance().OOOo() || !CameraSettings.isFrontMirror());
        return Util.rotateAndMirror(bitmap, -this.mOrientationCompensationAtRecordStart, z);
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

    private void hideAlert() {
        if (this.mCamera2Device.isSessionReady()) {
            resumePreview();
        } else {
            startPreview();
        }
        this.mMainProtocol.hideReviewViews();
        enableCameraControls(true);
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).delegateEvent(6);
    }

    private void initMetaParser() {
        this.mMetaDataDisposable = Flowable.create(new FlowableOnSubscribe() {
            public void subscribe(FlowableEmitter flowableEmitter) {
                VideoBase.this.mMetaDataFlowableEmitter = flowableEmitter;
            }
        }, BackpressureStrategy.DROP).map(new FunctionFaceDetect(this, isFrontCamera())).subscribe();
    }

    private boolean isFaceBeautyOn(BeautyValues beautyValues) {
        if (beautyValues == null) {
            return false;
        }
        return beautyValues.isFaceBeautyOn();
    }

    private void onStereoModeChanged() {
        enableCameraControls(false);
        this.mActivity.getSensorStateManager().setFocusSensorEnabled(false);
        cancelFocus(false);
        this.mStereoSwitchThread = new StereoSwitchThread();
        this.mStereoSwitchThread.start();
    }

    private void restorePreferences() {
        if (isZoomSupported()) {
            setZoomRatio(1.0f);
        }
        onSharedPreferenceChanged();
    }

    private void startPlayVideoActivity() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(this.mCurrentVideoUri, Util.convertOutputFormatToMimeType(this.mOutputFormat));
        intent.setFlags(1);
        try {
            this.mActivity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("failed to view video ");
            sb.append(this.mCurrentVideoUri);
            Log.e(str, sb.toString(), (Throwable) e);
        }
    }

    private void updateFaceView(boolean z, boolean z2) {
        if (this.mHandler.hasMessages(35)) {
            this.mHandler.removeMessages(35);
        }
        this.mHandler.obtainMessage(35, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
    }

    public /* synthetic */ void O00oo0oo() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
    }

    /* access modifiers changed from: protected */
    public void animateHold() {
    }

    /* access modifiers changed from: protected */
    public void animateSlide() {
    }

    /* access modifiers changed from: protected */
    public void applyTags(OnTagsListener onTagsListener) {
        ArrayList arrayList = new ArrayList();
        if (CameraSettings.isProVideoLogOpen(this.mModuleIndex)) {
            arrayList.add(new VideoTag("com.xiaomi.record_log", null, null));
        }
        if (CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex)) {
            arrayList.add(new VideoTag("com.xiaomi.record_mimovie", null, null));
        }
        if (CameraSettings.isAiAudioOn(this.mModuleIndex)) {
            arrayList.add(new VideoTag("com.xiaomi.ai_audio", null, null));
        }
        if (CameraSettings.isVideoQuality8KOpen(this.mModuleIndex) && CameraSettings.isReal8K()) {
            arrayList.add(new VideoTag("com.xiaomi.real_8k", null, null));
        }
        if (onTagsListener != null) {
            onTagsListener.onTagsReady(arrayList);
        }
    }

    public void cancelFocus(boolean z) {
        if (isDeviceAlive()) {
            if (!isFrameAvailable()) {
                Log.e(TAG, "cancelFocus: frame not available");
                return;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("cancelFocus: ");
            sb.append(z);
            Log.v(str, sb.toString());
            if (z) {
                setVideoFocusMode(AutoFocus.LEGACY_CONTINUOUS_VIDEO, true);
            }
            this.mCamera2Device.cancelFocus(this.mModuleIndex);
        }
    }

    public void checkDisplayOrientation() {
        if (isCreated()) {
            super.checkDisplayOrientation();
            FocusManager2 focusManager2 = this.mFocusManager;
            if (focusManager2 != null) {
                focusManager2.setDisplayOrientation(this.mCameraDisplayOrientation);
            }
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.setDisplayOrientation(this.mCameraDisplayOrientation);
            }
            this.mMainProtocol.setCameraDisplayOrientation(this.mCameraDisplayOrientation);
        }
    }

    /* access modifiers changed from: protected */
    public void cleanupEmptyFile() {
        String str;
        StringBuilder sb;
        String str2;
        String str3 = this.mCurrentVideoFilename;
        if (str3 != null) {
            File file = new File(str3);
            if (!file.exists()) {
                str = TAG;
                sb = new StringBuilder();
                str2 = "no video file: ";
            } else if (file.length() == 0) {
                if (!Storage.isUseDocumentMode()) {
                    file.delete();
                } else {
                    FileCompat.deleteFile(this.mCurrentVideoFilename);
                }
                str = TAG;
                sb = new StringBuilder();
                str2 = "delete empty video file: ";
            } else {
                return;
            }
            sb.append(str2);
            sb.append(this.mCurrentVideoFilename);
            Log.d(str, sb.toString());
            this.mCurrentVideoFilename = null;
        }
    }

    public void closeCamera() {
        Log.v(TAG, "closeCamera: E");
        this.mPreviewing = false;
        this.mSnapshotInProgress = false;
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
            camera2Proxy.setMetaDataCallback(null);
            this.mCamera2Device.setFocusCallback(null);
            this.mCamera2Device.setErrorCallback(null);
            unlockAEAF();
            synchronized (this.mDeviceLock) {
                this.mCamera2Device = null;
            }
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.destroy();
        }
        Log.v(TAG, "closeCamera: X");
    }

    /* access modifiers changed from: protected */
    public void closeVideoFileDescriptor() {
        ParcelFileDescriptor parcelFileDescriptor = this.mVideoFileDescriptor;
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
            } catch (IOException e) {
                Log.e(TAG, "fail to close fd", (Throwable) e);
            }
            this.mVideoFileDescriptor = null;
        }
    }

    /* access modifiers changed from: protected */
    public void deleteVideoFile(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("delete invalid video ");
        sb.append(str);
        Log.v(str2, sb.toString());
        if (!new File(str).delete()) {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("fail to delete ");
            sb2.append(str);
            Log.v(str3, sb2.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void doLaterReleaseIfNeed() {
        Camera camera = this.mActivity;
        if (camera != null && camera.isActivityPaused()) {
            Log.d(TAG, "doLaterRelease");
            this.mActivity.pauseIfNotRecording();
            this.mActivity.releaseAll(true, true);
        }
    }

    /* access modifiers changed from: protected */
    public void doReturnToCaller(boolean z) {
        int i;
        Intent intent = new Intent();
        if (z) {
            i = -1;
            intent.setData(this.mCurrentVideoUri);
            intent.setFlags(1);
        } else {
            i = 0;
        }
        this.mActivity.setResult(i, intent);
        this.mActivity.finish();
    }

    /* access modifiers changed from: protected */
    public boolean enableFaceDetection() {
        return DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_FACE_DETECTION, getResources().getBoolean(R.bool.pref_camera_facedetection_default));
    }

    public void enterMutexMode(int i) {
        setZoomRatio(1.0f);
        this.mSettingsOverrider.overrideSettings(CameraSettings.KEY_WHITE_BALANCE, null, CameraSettings.KEY_COLOR_EFFECT, null);
        onSharedPreferenceChanged();
    }

    /* access modifiers changed from: protected */
    public void enterSavePowerMode() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("currentBrightness: ");
        sb.append(this.mActivity.getCurrentBrightness());
        Log.d(str, sb.toString());
        Camera camera = this.mActivity;
        if (camera != null && camera.getCurrentBrightness() == 255) {
            Log.d(TAG, "enterSavePowerMode");
            this.mHandler.post(new Runnable() {
                public void run() {
                    VideoBase videoBase = VideoBase.this;
                    Camera camera = videoBase.mActivity;
                    if (camera != null) {
                        if (!videoBase.mIsAutoHibernationSupported) {
                            camera.setWindowBrightness(81);
                        }
                        VideoBase.this.mSavePowerMode = true;
                    }
                }
            });
        }
    }

    public void exitMutexMode(int i) {
        this.mSettingsOverrider.restoreSettings();
        onSharedPreferenceChanged();
    }

    /* access modifiers changed from: protected */
    public void exitSavePowerMode() {
        this.mHandler.removeMessages(52);
        if (this.mSavePowerMode) {
            Log.d(TAG, "exitSavePowerMode");
            this.mHandler.post(new Runnable() {
                public void run() {
                    VideoBase videoBase = VideoBase.this;
                    Camera camera = videoBase.mActivity;
                    if (camera != null) {
                        if (!videoBase.mIsAutoHibernationSupported) {
                            camera.restoreWindowBrightness();
                        }
                        VideoBase.this.mSavePowerMode = false;
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0096  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ContentValues genContentValues(int i, int i2, String str, boolean z, boolean z2) {
        String str2;
        String str3;
        boolean z3;
        StringBuilder sb;
        String str4;
        String createName = createName(System.currentTimeMillis(), i2);
        if (i2 > 0) {
            String format = String.format(Locale.ENGLISH, "_%d", new Object[]{Integer.valueOf(i2)});
            StringBuilder sb2 = new StringBuilder();
            sb2.append(createName);
            sb2.append(format);
            createName = sb2.toString();
        }
        if (z) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(createName);
            sb3.append(Storage.VIDEO_8K_SUFFIX);
            createName = sb3.toString();
        }
        if (!TextUtils.isEmpty(str)) {
            int hashCode = str.hashCode();
            if (hashCode != -1150307548) {
                if (hashCode != -1150306525) {
                    if (hashCode == 272876231 && str.equals(ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_480_DIRECT)) {
                        z3 = true;
                        if (z3) {
                            sb = new StringBuilder();
                            sb.append(createName);
                            str4 = Storage.HSR_120_SUFFIX;
                        } else if (z3) {
                            sb = new StringBuilder();
                            sb.append(createName);
                            str4 = Storage.HSR_240_SUFFIX;
                        } else if (z3) {
                            sb = new StringBuilder();
                            sb.append(createName);
                            str4 = Storage.HSR_480_SUFFIX;
                        }
                        sb.append(str4);
                        createName = sb.toString();
                    }
                } else if (str.equals(ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_240)) {
                    z3 = true;
                    if (z3) {
                    }
                    sb.append(str4);
                    createName = sb.toString();
                }
            } else if (str.equals(ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_120)) {
                z3 = false;
                if (z3) {
                }
                sb.append(str4);
                createName = sb.toString();
            }
            z3 = true;
            if (z3) {
            }
            sb.append(str4);
            createName = sb.toString();
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append(createName);
        sb4.append(Util.convertOutputFormatToFileExt(i));
        String sb5 = sb4.toString();
        String convertOutputFormatToMimeType = Util.convertOutputFormatToMimeType(i);
        if (!TextUtils.isEmpty(str) && (str.equals(ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_1920) || str.equals(ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_960) || str.equals(ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_480))) {
            String generatePrimaryTempFile = Storage.isUseDocumentMode() ? Storage.generatePrimaryTempFile() : Storage.generateTempFilepath();
            StringBuilder sb6 = new StringBuilder();
            sb6.append(generatePrimaryTempFile);
            sb6.append('/');
            sb6.append(sb5);
            str2 = sb6.toString();
            StringBuilder sb7 = new StringBuilder();
            sb7.append(generatePrimaryTempFile);
            sb7.append(File.separator);
            sb7.append(Storage.AVOID_SCAN_FILE_NAME);
            Util.createFile(new File(sb7.toString()));
        } else if (z2) {
            str2 = Storage.generateFilepath(sb5);
        } else {
            String generatePrimaryDirectoryPath = Storage.generatePrimaryDirectoryPath();
            Util.mkdirs(new File(generatePrimaryDirectoryPath), FrameMetricsAggregator.EVERY_DURATION, -1, -1);
            if (Util.isPathExist(generatePrimaryDirectoryPath)) {
                str3 = Storage.generatePrimaryFilepath(sb5);
            } else {
                StringBuilder sb8 = new StringBuilder();
                sb8.append(Storage.DIRECTORY);
                sb8.append('/');
                sb8.append(sb5);
                str3 = sb8.toString();
            }
            str2 = str3;
        }
        Log.k(3, TAG, String.format("genContentValues: path=%s", new Object[]{str2}));
        ContentValues contentValues = new ContentValues(8);
        contentValues.put("title", createName);
        contentValues.put("_display_name", sb5);
        contentValues.put("mime_type", convertOutputFormatToMimeType);
        contentValues.put("_data", str2);
        StringBuilder sb9 = new StringBuilder();
        sb9.append(Integer.toString(this.mVideoSize.width));
        sb9.append("x");
        sb9.append(Integer.toString(this.mVideoSize.height));
        contentValues.put("resolution", sb9.toString());
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        if (!(currentLocation == null || (currentLocation.getLatitude() == 0.0d && currentLocation.getLongitude() == 0.0d))) {
            contentValues.put("latitude", Double.valueOf(currentLocation.getLatitude()));
            contentValues.put("longitude", Double.valueOf(currentLocation.getLongitude()));
        }
        return contentValues;
    }

    /* access modifiers changed from: protected */
    public int getCameraRotation() {
        return ((this.mOrientationCompensation - this.mDisplayRotation) + m.cQ) % m.cQ;
    }

    public CameraSize getVideoSize() {
        return this.mVideoSize;
    }

    /* access modifiers changed from: protected */
    public void initializeCapabilities() {
        super.initializeCapabilities();
        this.mContinuousFocusSupported = Util.isSupported(3, this.mCameraCapabilities.getSupportedFocusModes());
    }

    /* access modifiers changed from: protected */
    public void initializeFocusManager() {
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

    /* access modifiers changed from: protected */
    public boolean is3ALocked() {
        return this.m3ALocked;
    }

    /* access modifiers changed from: protected */
    public boolean isAEAFLockSupported() {
        return true;
    }

    public boolean isCameraEnabled() {
        return this.mPreviewing;
    }

    /* access modifiers changed from: protected */
    public boolean isCameraSessionReady() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        return camera2Proxy != null && camera2Proxy.isSessionReady();
    }

    public boolean isCaptureIntent() {
        return this.mIsVideoCaptureIntent;
    }

    public boolean isDoingAction() {
        return this.mMediaRecorderRecording && !this.mMediaRecorderRecordingPaused && !ModuleManager.isProVideoModule();
    }

    public boolean isFaceDetectStarted() {
        return this.mFaceDetectionStarted;
    }

    public boolean isMeteringAreaOnly() {
        return !this.mFocusAreaSupported && this.mMeteringAreaSupported && !this.mFocusOrAELockSupported;
    }

    public boolean isRecording() {
        return this.mMediaRecorderRecording;
    }

    public boolean isSelectingCapturedResult() {
        return isCaptureIntent() && ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).getActiveFragment(R.id.bottom_action) == 4083;
    }

    /* access modifiers changed from: protected */
    public boolean isSessionReady() {
        return this.mIsSessionReady;
    }

    public boolean isShowAeAfLockIndicator() {
        return this.m3ALocked;
    }

    public boolean isThermalThreshold() {
        boolean z;
        if (!this.mMediaRecorderRecording) {
            return false;
        }
        long uptimeMillis = (SystemClock.uptimeMillis() - this.mRecordingStartTime) / 60000;
        boolean z2 = true;
        if (isFrontCamera()) {
            if (uptimeMillis < 10) {
                z2 = false;
            }
            return z2;
        } else if (C0124O00000oO.OOooOO()) {
            if (uptimeMillis < 3) {
                z2 = false;
            }
            return z2;
        } else {
            if (uptimeMillis < 20) {
                z = false;
            }
            return z;
        }
    }

    public boolean isUnInterruptable() {
        this.mUnInterruptableReason = null;
        if (isPostProcessing()) {
            this.mUnInterruptableReason = "post process";
        }
        return this.mUnInterruptableReason != null;
    }

    public boolean isUseFaceInfo() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoBokehEnabled() {
        return CameraSettings.isVideoBokehOn() || (DataRepository.dataItemRunning().getComponentRunningShine().isVideoShineForceOn(this.mModuleIndex) && this.mCameraCapabilities.isSupportVideoBokehAdjust());
    }

    public boolean isZoomEnabled() {
        return !CameraSettings.isFrontCamera() && !CameraSettings.isVideoBokehOn() && isCameraEnabled() && isFrameAvailable();
    }

    /* access modifiers changed from: protected */
    public void keepPowerSave() {
        Log.d(TAG, "keepPowerSave");
        exitSavePowerMode();
        this.mHandler.sendEmptyMessageDelayed(52, 1500000);
    }

    /* access modifiers changed from: protected */
    public void lockAEAF() {
        Log.d(TAG, "lockAEAF");
        if (this.mAeLockSupported) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.setAELock(true);
            }
        }
        this.mFocusManager.setAeAwbLock(true);
        this.m3ALocked = true;
    }

    public boolean multiCapture() {
        Log.v(TAG, "multiCapture");
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean needChooseVideoBeauty(BeautyValues beautyValues) {
        if (!this.mCameraCapabilities.isSupportVideoBeauty()) {
            return false;
        }
        return CameraSettings.isMasterFilterOn(this.mModuleIndex) || DataRepository.dataItemRunning().getComponentRunningShine().isVideoShineForceOn(this.mModuleIndex) || isFaceBeautyOn(beautyValues);
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

    public void notifyError() {
        if (CameraSchedulers.isOnMainThread() && isRecording() && !isPostProcessing()) {
            stopVideoRecording(false);
        }
        super.notifyError();
    }

    public void notifyFocusAreaUpdate() {
        updatePreferenceTrampoline(3);
    }

    public boolean onBackPressed() {
        if (!isFrameAvailable() || this.mStereoSwitchThread != null) {
            return false;
        }
        if (!this.mMediaRecorderRecording) {
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
    }

    public void onBroadcastReceived(Context context, Intent intent) {
        super.onBroadcastReceived(context, intent);
        String action = intent.getAction();
        if ("android.intent.action.MEDIA_EJECT".equals(action)) {
            if (Storage.isCurrentStorageIsSecondary()) {
                Storage.switchToPhoneStorage();
            } else {
                return;
            }
        } else if ("android.intent.action.ACTION_SHUTDOWN".equals(action) || "android.intent.action.REBOOT".equals(action)) {
            Log.i(TAG, "onBroadcastReceived: device shutdown or reboot");
        } else {
            return;
        }
        stopVideoRecording(false);
    }

    /* access modifiers changed from: protected */
    public void onCameraOpened() {
        super.onCameraOpened();
        initMetaParser();
    }

    public void onFaceDetected(CameraHardwareFace[] cameraHardwareFaceArr, FaceAnalyzeInfo faceAnalyzeInfo, Rect rect) {
        if (isCreated() && cameraHardwareFaceArr != null) {
            if (C0124O00000oO.OOooOoO()) {
                boolean z = cameraHardwareFaceArr.length > 0;
                if (z != this.mFaceDetected && isFrontCamera() && this.mModuleIndex == 162) {
                    this.mCamera2Device.resumePreview();
                }
                this.mFaceDetected = z;
            }
            if (this.mIsVideoFaceViewShown) {
                if (!C0124O00000oO.Oo00o() || cameraHardwareFaceArr.length <= 0 || cameraHardwareFaceArr[0].faceType != 64206) {
                    if (!this.mMainProtocol.setFaces(1, cameraHardwareFaceArr, getActiveArraySize(), rect)) {
                    }
                } else if (this.mObjectTrackingStarted) {
                    this.mMainProtocol.setFaces(3, cameraHardwareFaceArr, getActiveArraySize(), rect);
                }
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

    public void onFocusStateChanged(FocusTask focusTask) {
        if (isCreated() && !isDeparted()) {
            int focusTrigger = focusTask.getFocusTrigger();
            if (focusTrigger == 1) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("focusTime=");
                sb.append(focusTask.getElapsedTime());
                sb.append("ms focused=");
                sb.append(focusTask.isSuccess());
                sb.append(" waitForRecording=");
                sb.append(this.mFocusManager.isFocusingSnapOnFinish());
                Log.v(str, sb.toString());
                this.mMainProtocol.setFocusViewType(true);
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
            } else if (focusTrigger == 2 && !focusTask.isIsDepthFocus() && !this.mMediaRecorderRecording && !this.m3ALocked) {
                this.mFocusManager.onFocusResult(focusTask);
            }
        }
    }

    public void onHostStopAndNotifyActionStop() {
        if (this.mInStartingFocusRecording) {
            this.mInStartingFocusRecording = false;
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onFinish();
            }
        }
        if (isRecording() && isCameraSessionReady()) {
            stopVideoRecording(true);
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
                    if (isIgnoreTouchEvent()) {
                        return true;
                    }
                    if (Util.isFingerPrintKeyEvent(keyEvent)) {
                        if (CameraSettings.isFingerprintCaptureEnable() && !this.mMainProtocol.isShowReviewViews()) {
                            i2 = 30;
                        }
                        return true;
                    }
                    i2 = 40;
                    performKeyClicked(i2, getString(R.string.pref_camera_volumekey_function_entryvalue_shutter), keyEvent.getRepeatCount(), true);
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
        if (isIgnoreTouchEvent() || !isCameraEnabled()) {
            Log.w(TAG, "preview stop or need ignore this touch event.");
            return true;
        }
        LiveVVChooser liveVVChooser = (LiveVVChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(229);
        if (liveVVChooser != null && liveVVChooser.isShow()) {
            return false;
        }
        CloneChooser cloneChooser = (CloneChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(416);
        if (cloneChooser != null && cloneChooser.isShow()) {
            return false;
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

    public void onNewIntent() {
        setCaptureIntent(this.mActivity.getCameraIntentManager().isVideoCaptureIntent());
    }

    public void onOrientationChanged(int i, int i2, int i3) {
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

    public void onPause() {
        super.onPause();
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
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPreviewSessionClosed: ");
        sb.append(cameraCaptureSession);
        Log.d(str, sb.toString());
        CameraCaptureSession cameraCaptureSession2 = this.mCurrentSession;
        if (cameraCaptureSession2 != null && cameraCaptureSession2.equals(cameraCaptureSession)) {
            this.mCurrentSession = null;
            setSessionReady(false);
        }
    }

    public void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession) {
        if (!isTextureExpired() || !retryOnceIfCameraError(this.mHandler)) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPreviewSessionFailed: ");
            sb.append(cameraCaptureSession);
            Log.d(str, sb.toString());
            CameraCaptureSession cameraCaptureSession2 = this.mCurrentSession;
            if (cameraCaptureSession2 != null && cameraCaptureSession2.equals(cameraCaptureSession)) {
                this.mCurrentSession = null;
                setSessionReady(false);
            }
            this.mHandler.sendEmptyMessage(51);
            return;
        }
        Log.d(TAG, "sessionFailed due to surfaceTexture expired, retry");
    }

    public void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPreviewSessionSuccess: ");
        sb.append(cameraCaptureSession);
        Log.d(str, sb.toString());
        if (cameraCaptureSession != null && isAlive()) {
            this.mCurrentSession = cameraCaptureSession;
            setSessionReady(true);
        }
    }

    public void onPreviewSizeChanged(int i, int i2) {
        Log.v(TAG, String.format(Locale.ENGLISH, "onPreviewSizeChanged: %dx%d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setPreviewSize(i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public void onPreviewStart() {
    }

    public void onResume() {
        super.onResume();
        if (!isRecording() && !this.mOpenCameraFail && !this.mCameraDisabled && PermissionManager.checkCameraLaunchPermissions()) {
            if (!this.mPreviewing) {
                startPreview();
            }
            this.mHandler.sendEmptyMessage(9);
            keepScreenOnAwhile();
            onSettingsBack();
            if (this.mPreviewing) {
                this.mOnResumeTime = SystemClock.uptimeMillis();
                this.mHandler.sendEmptyMessageDelayed(4, 100);
            }
        }
    }

    @OnClickAttr
    public void onReviewCancelClicked() {
        if (isSelectingCapturedResult()) {
            deleteCurrentVideo();
            hideAlert();
            return;
        }
        stopVideoRecording(false);
        doReturnToCaller(false);
    }

    @OnClickAttr
    public void onReviewDoneClicked() {
        this.mMainProtocol.hideReviewViews();
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.delegateEvent(6);
        }
        doReturnToCaller(true);
    }

    @OnClickAttr
    public void onReviewPlayClicked(View view) {
        startPlayVideoActivity();
    }

    /* access modifiers changed from: protected */
    public void onSettingsBack() {
        ChangeManager changeManager = CameraSettings.sCameraChangeManager;
        if (changeManager.check(3)) {
            changeManager.clear(3);
            restorePreferences();
        } else if (changeManager.check(1)) {
            changeManager.clear(1);
            onSharedPreferenceChanged();
        }
    }

    public void onShineChanged(int i) {
        int[] iArr;
        if (i == 196) {
            iArr = new int[]{68, 69};
        } else if (i == 239) {
            iArr = new int[]{13};
        } else if (i == 243) {
            iArr = new int[]{67};
        } else if (i == 244) {
            iArr = new int[]{81};
        } else {
            throw new RuntimeException("unknown configItem changed");
        }
        updatePreferenceInWorkThread(iArr);
    }

    public void onShutterButtonClick(int i) {
    }

    public void onShutterButtonFocus(boolean z, int i) {
    }

    public boolean onShutterButtonLongClick() {
        Log.v(TAG, "onShutterButtonLongClick");
        return false;
    }

    public void onShutterButtonLongClickCancel(boolean z) {
        onShutterButtonFocus(false, 2);
    }

    /* access modifiers changed from: protected */
    public void onThermalConstrained() {
        super.onThermalConstrained();
        if (this.mMediaRecorderRecording) {
            stopVideoRecording(false);
        }
    }

    @OnClickAttr
    public void onThumbnailClicked(View view) {
        if (!this.mMediaRecorderRecording && this.mActivity.getThumbnailUpdater().getThumbnail() != null) {
            this.mActivity.gotoGallery();
        }
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        if (!this.mMediaRecorderRecording) {
            keepScreenOnAwhile();
        }
    }

    /* access modifiers changed from: protected */
    public void onWaitStopCallbackTimeout() {
    }

    public boolean onWaitingFocusFinished() {
        if (!isFrameAvailable()) {
            return false;
        }
        Log.v(TAG, BaseEvent.CAPTURE);
        this.mHandler.removeMessages(55);
        if (!this.mInStartingFocusRecording) {
            return false;
        }
        this.mInStartingFocusRecording = false;
        if (this.mIsSatFallback == 0 || !shouldCheckSatFallbackState()) {
            startVideoRecording();
            return true;
        }
        Log.w(TAG, "video record check: sat fallback");
        return false;
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onWindowFocusChanged: ");
        sb.append(z);
        Log.d(str, sb.toString());
        if (!this.mMediaRecorderRecording) {
            return;
        }
        if (z) {
            keepPowerSave();
            if (this.mIsAutoHibernationSupported) {
                keepAutoHibernation();
                return;
            }
            return;
        }
        exitSavePowerMode();
        if (this.mIsAutoHibernationSupported) {
            exitAutoHibernation();
        }
    }

    /* access modifiers changed from: protected */
    public void parseIntent(Intent intent) {
        if (intent.getExtras() != null) {
            this.mIntentRequestSize = this.mActivity.getCameraIntentManager().getRequestSize();
            Uri extraSavedUri = this.mActivity.getCameraIntentManager().getExtraSavedUri();
            if (extraSavedUri != null) {
                try {
                    this.mVideoFileDescriptor = CameraAppImpl.getAndroidContext().getContentResolver().openFileDescriptor(extraSavedUri, "rw");
                    this.mCurrentVideoUri = extraSavedUri;
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("parseIntent: outputUri=");
                    sb.append(extraSavedUri);
                    Log.d(str, sb.toString());
                } catch (FileNotFoundException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void performKeyClicked(int i, String str, int i2, boolean z) {
        if (i2 == 0 && z) {
            if (isIgnoreTouchEvent()) {
                Log.w(TAG, "ignore volume key");
                return;
            }
            LiveVVChooser liveVVChooser = (LiveVVChooser) ModeCoordinatorImpl.getInstance().getAttachProtocol(229);
            if (liveVVChooser == null || !liveVVChooser.isShow()) {
                restoreBottom();
                onShutterButtonClick(i);
            } else {
                liveVVChooser.startShot();
            }
        }
    }

    public void playFocusSound(int i) {
        playCameraSound(i);
    }

    public void playVideo() {
        startPlayVideoActivity();
    }

    /* access modifiers changed from: protected */
    public void readVideoPreferences() {
    }

    /* access modifiers changed from: protected */
    public void resetFocusState(double d) {
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null && focusManager2.isNeedCancelAutoFocus() && !isRecording()) {
            this.mFocusManager.onDeviceKeepMoving(d);
        }
    }

    /* access modifiers changed from: protected */
    public void resizeForPreviewAspectRatio() {
    }

    /* access modifiers changed from: protected */
    public void sendOpenFailMessage() {
        this.mHandler.sendEmptyMessage(10);
    }

    public void set3DAudioParameter() {
    }

    /* access modifiers changed from: protected */
    public void setCaptureIntent(boolean z) {
        this.mIsVideoCaptureIntent = z;
    }

    public void setFrameAvailable(boolean z) {
        super.setFrameAvailable(z);
        if (z && CameraSettings.isCameraSoundOpen()) {
            this.mActivity.loadCameraSound(1);
            this.mActivity.loadCameraSound(0);
            this.mActivity.loadCameraSound(2);
            this.mActivity.loadCameraSound(3);
        }
    }

    /* access modifiers changed from: protected */
    public void setOrientationParameter() {
        if (!(isDeparted() || this.mCamera2Device == null || this.mOrientation == -1)) {
            if (CameraSettings.isAutoZoomEnabled(DataRepository.dataItemGlobal().getCurrentMode()) || isVideoBokehEnabled() || CameraSettings.isMasterFilterOn(this.mModuleIndex)) {
                if (this.mPreviewing) {
                    updatePreferenceInWorkThread(35);
                } else {
                    CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new C0377O000o0Oo(this));
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("video_rotation=");
            sb.append(this.mOrientation);
            AudioSystem.setParameters(sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void setSessionReady(boolean z) {
        this.mIsSessionReady = z;
    }

    /* access modifiers changed from: protected */
    public void setVideoFocusMode(String str, boolean z) {
        this.mVideoFocusMode = str;
        if (z) {
            updateVideoFocusMode();
        }
    }

    public boolean shouldCaptureDirectly() {
        return false;
    }

    public boolean shouldReleaseLater() {
        return this.mInStartingFocusRecording || isRecording();
    }

    /* access modifiers changed from: protected */
    public void showAlert() {
        pausePreview();
        this.mMainProtocol.showReviewViews(getReviewBitmap());
        enableCameraControls(false);
        ((BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160)).delegateEvent(6);
    }

    public void startFaceDetection() {
        if (this.mFaceDetectionEnabled && !this.mFaceDetectionStarted && isAlive() && this.mMaxFaceCount > 0) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                this.mFaceDetectionStarted = true;
                camera2Proxy.startFaceDetection();
                if (this.mIsVideoFaceViewShown) {
                    this.mMainProtocol.setActiveIndicator(1);
                    updateFaceView(true, true);
                }
            }
        }
    }

    public void startFocus() {
        if (isDeviceAlive() && isFrameAvailable()) {
            Log.v(TAG, "startFocus");
            if (this.mFocusOrAELockSupported) {
                setVideoFocusMode("auto", true);
                this.mCamera2Device.startFocus(FocusTask.create(1), this.mModuleIndex);
            } else {
                this.mCamera2Device.resumePreview();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void startVideoRecording() {
    }

    public void stopFaceDetection(boolean z) {
        if (this.mFaceDetectionEnabled && this.mFaceDetectionStarted) {
            this.mFaceDetectionStarted = false;
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.stopFaceDetection();
            }
            if (this.mIsVideoFaceViewShown) {
                this.mMainProtocol.setActiveIndicator(2);
                updateFaceView(false, z);
            }
        }
    }

    public void stopObjectTracking(boolean z) {
    }

    public void stopVideoRecording(boolean z) {
    }

    /* access modifiers changed from: protected */
    public boolean supportTouchFocus() {
        return !isFrontCamera();
    }

    /* access modifiers changed from: protected */
    public void switchMutexHDR() {
        if ("off".equals(DataRepository.dataItemConfig().getComponentHdr().getComponentValue(this.mModuleIndex))) {
            resetMutexModeManually();
        } else {
            this.mMutexModePicker.setMutexMode(2);
        }
    }

    /* access modifiers changed from: protected */
    public void unlockAEAF() {
        Log.d(TAG, "unlockAEAF");
        this.m3ALocked = false;
        if (this.mAeLockSupported) {
            this.mCamera2Device.setAELock(false);
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

    /* access modifiers changed from: protected */
    public void updateBeauty() {
        CameraCapabilities cameraCapabilities = this.mCameraCapabilities;
        if (cameraCapabilities != null && cameraCapabilities.isSupportVideoBeauty()) {
            int i = this.mModuleIndex;
            if (i == 162 || i == 161) {
                if (this.mBeautyValues == null) {
                    this.mBeautyValues = new BeautyValues();
                }
                CameraSettings.initBeautyValues(this.mBeautyValues, this.mModuleIndex);
                this.mCamera2Device.setBeautyValues(this.mBeautyValues);
                return;
            }
        }
        this.mBeautyValues = null;
    }

    /* access modifiers changed from: protected */
    public void updateDeviceOrientation() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setDeviceOrientation(this.mOrientation);
        }
    }

    /* access modifiers changed from: protected */
    public void updateFace() {
        boolean enableFaceDetection = enableFaceDetection();
        MainContentProtocol mainContentProtocol = this.mMainProtocol;
        if (mainContentProtocol != null) {
            mainContentProtocol.setSkipDrawFace(!enableFaceDetection);
        }
        if (enableFaceDetection) {
            if (!this.mFaceDetectionEnabled) {
                this.mFaceDetectionEnabled = true;
                this.mIsVideoFaceViewShown = CameraSettings.isVideoFaceViewShownEnable();
                startFaceDetection();
            }
        } else if (this.mFaceDetectionEnabled) {
            stopFaceDetection(true);
            this.mFaceDetectionEnabled = false;
        }
    }

    public void updateFlashPreference() {
        if (!this.mMutexModePicker.isNormal() && !this.mMutexModePicker.isSupportedFlashOn() && !this.mMutexModePicker.isSupportedTorch()) {
            resetMutexModeManually();
        }
        setFlashMode(DataRepository.dataItemConfig().getComponentFlash().getComponentValue(this.mModuleIndex));
    }

    /* access modifiers changed from: protected */
    public void updateFocusArea() {
        Camera camera = this.mActivity;
        if (camera != null && !camera.isActivityPaused() && isAlive()) {
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
            }
            String focusMode = CameraSettings.getFocusMode();
            if (!this.mFocusAreaSupported || "manual".equals(focusMode)) {
                this.mCamera2Device.resumePreview();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateFocusCallback() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy == null) {
            Log.e(TAG, "updateFocusCallback: null camera device");
        } else if (this.mContinuousFocusSupported) {
            if (AutoFocus.LEGACY_CONTINUOUS_VIDEO.equals(this.mVideoFocusMode)) {
                this.mCamera2Device.setFocusCallback(this);
            }
        } else {
            if (this.mAELockOnlySupported) {
                camera2Proxy.setFocusCallback(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateMotionFocusManager() {
        this.mActivity.getSensorStateManager().setFocusSensorEnabled("auto".equals(this.mVideoFocusMode));
    }

    /* access modifiers changed from: protected */
    public void updateRecordingTime() {
        if (isThermalThreshold()) {
            if (!"0".equals(CameraSettings.getFlashMode(this.mModuleIndex))) {
                ThermalDetector.getInstance().onThermalNotification();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateVideoFocusMode() {
        if (this.mCamera2Device == null) {
            Log.e(TAG, "updateVideoFocusMode: null camera device");
            return;
        }
        int[] supportedFocusModes = this.mCameraCapabilities.getSupportedFocusModes();
        int convertToFocusMode = AutoFocus.convertToFocusMode(this.mVideoFocusMode);
        if (Util.isSupported(convertToFocusMode, supportedFocusModes)) {
            this.mCamera2Device.setFocusMode(convertToFocusMode);
            updateMotionFocusManager();
            updateFocusCallback();
        }
        String focusMode = CameraSettings.getFocusMode();
        int i = this.mModuleIndex;
        if ((i == 180 || i == 169) && focusMode.equals("manual")) {
            this.mFocusManager.setFocusMode(focusMode);
            setFocusMode(focusMode);
            this.mCamera2Device.setFocusDistance((this.mCameraCapabilities.getMinimumFocusDistance() * ((float) CameraSettings.getFocusPosition())) / 1000.0f);
        }
    }

    /* access modifiers changed from: protected */
    public void waitStereoSwitchThread() {
        try {
            if (this.mStereoSwitchThread != null) {
                this.mStereoSwitchThread.cancel();
                this.mStereoSwitchThread.join();
                this.mStereoSwitchThread = null;
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), (Throwable) e);
        }
    }
}
