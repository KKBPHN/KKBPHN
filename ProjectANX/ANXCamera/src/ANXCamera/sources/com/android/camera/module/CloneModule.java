package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import androidx.annotation.WorkerThread;
import com.android.camera.Camera;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.LocationManager;
import com.android.camera.PictureSizeManager;
import com.android.camera.R;
import com.android.camera.SensorStateManager.SensorStateAdapter;
import com.android.camera.SensorStateManager.SensorStateListener;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.Util;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.clone.Status;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.log.Log;
import com.android.camera.module.loader.StartControl;
import com.android.camera.module.loader.StartControlFeatureDetail;
import com.android.camera.module.loader.camera2.FocusManager2;
import com.android.camera.module.loader.camera2.FocusManager2.Listener;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CloneAction;
import com.android.camera.protocol.ModeProtocol.CloneProcess;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.CloneAttr;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.storage.Storage;
import com.android.camera.ui.V6CameraGLSurfaceView.RendererListener;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.Camera2Proxy.PreviewCallback;
import com.android.camera2.CameraCapabilities;
import com.android.gallery3d.ui.GLCanvasImpl;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.fenshen.FenShenCam;
import com.xiaomi.fenshen.FenShenCam.Message;
import com.xiaomi.fenshen.FenShenCam.Mode;
import com.xiaomi.fenshen.FenShenCam.TEventType;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CloneModule extends BaseModule implements CameraPreviewCallback, FocusCallback, PreviewCallback, ExternalFrameProcessor, RendererListener, CameraAction, CloneAction, PictureCallback, Listener {
    public static final int DURATION_COPY_RECORDING = 10000;
    public static final int DURATION_VIDEO_RECORDING = 5000;
    protected static final int MAX_PHOTO_SUBJECT_COUNT = 4;
    private static final int MAX_VIDEO_SUBJECT_COUNT = 2;
    protected static final int MIN_FRAME_COUNT_TO_STOP = 15;
    protected static final int MIN_SUBJECT_COUNT = 2;
    protected static final long START_OFFSET_MS = 450;
    protected static final long START_RECORDING_OFFSET = 300;
    /* access modifiers changed from: private */
    public static final String TAG = "CloneModule";
    private FenShenCam.Listener mCloneListener = new StateListener(this);
    private CountDownTimer mCountDownTimer;
    /* access modifiers changed from: private */
    public FocusManager2 mFocusManager;
    protected int mFrameCount = 0;
    protected boolean mInRecording = false;
    protected boolean mIsDuringShooting = false;
    protected boolean mIsFinished;
    private boolean mIsSegmentRecording = false;
    private byte[] mJpgBytes;
    private Message mLastMessage;
    private int mLastSubjectCount = 0;
    protected Mode mMode;
    protected long mOnResumeTime;
    protected boolean mPendingStart;
    private boolean mScrolled = false;
    protected SensorStateListener mSensorStateListener = new SensorStateAdapter() {
        public boolean isWorking() {
            return CloneModule.this.isAlive() && CloneModule.this.getCameraState() != 0;
        }

        public void notifyDevicePostureChanged() {
            CloneModule.this.mActivity.getEdgeShutterView().onDevicePostureChanged();
        }

        public void onDeviceBecomeStable() {
            Log.v(CloneModule.TAG, "onDeviceBecomeStable");
        }

        public void onDeviceKeepMoving(double d) {
            if (!CloneModule.this.mMainProtocol.isEvAdjusted(true) && !CloneModule.this.mPaused && Util.isTimeout(System.currentTimeMillis(), CloneModule.this.mTouchFocusStartingTime, 3000)) {
                CloneModule cloneModule = CloneModule.this;
                if (!cloneModule.mIsDuringShooting && cloneModule.mFocusManager != null && CloneModule.this.mFocusManager.isNeedCancelAutoFocus() && !CloneModule.this.isRecording()) {
                    CloneModule.this.mFocusManager.onDeviceKeepMoving(d);
                }
            }
        }
    };
    private int mSubjectCount = 0;
    /* access modifiers changed from: private */
    public long mTouchFocusStartingTime;
    protected String mVideoFileName;
    protected String mVideoFilePath;

    /* renamed from: com.android.camera.module.CloneModule$4 reason: invalid class name */
    /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message = new int[Message.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|(3:21|22|24)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(24:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|24) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x007a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.PREVIEW_PERSON.ordinal()] = 1;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.START.ordinal()] = 2;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.PREVIEW_NO_PERSON.ordinal()] = 3;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.ALIGN_OK.ordinal()] = 4;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.ALIGN_WARNING.ordinal()] = 5;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.ALIGN_TOO_LARGE_OR_FAILED.ordinal()] = 6;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.NO_PERSON.ordinal()] = 7;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.SAVE_VIDEO_SUCCESS.ordinal()] = 8;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.PAUSED.ordinal()] = 9;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.ERROR_INIT.ordinal()] = 10;
            try {
                $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.ERROR_RUNTIME.ordinal()] = 11;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    class MainHandler extends Handler {
        protected static final int MSG_NEW_PREVIEW_IMAGE_ARRIVING = 257;
        protected static final int MSG_WAIT_SHUTTER_SOUND_FINISH = 256;

        public MainHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(android.os.Message message) {
            int i = message.what;
            if (i == 2) {
                CloneModule.this.getWindow().clearFlags(128);
            } else if (i == 4) {
                CloneModule.this.checkActivityOrientation();
                long uptimeMillis = SystemClock.uptimeMillis();
                CloneModule cloneModule = CloneModule.this;
                if (uptimeMillis - cloneModule.mOnResumeTime < 5000) {
                    cloneModule.mHandler.sendEmptyMessageDelayed(4, 100);
                }
            } else if (i == 9) {
                CloneModule cloneModule2 = CloneModule.this;
                cloneModule2.mMainProtocol.initializeFocusView(cloneModule2);
                CloneModule.this.initCloneMode();
            } else if (i == 17) {
                CloneModule.this.mHandler.removeMessages(17);
                CloneModule.this.mHandler.removeMessages(2);
                CloneModule.this.getWindow().addFlags(128);
                CloneModule cloneModule3 = CloneModule.this;
                cloneModule3.mHandler.sendEmptyMessageDelayed(2, (long) cloneModule3.getScreenDelay());
            } else if (i == 31) {
                CloneModule.this.setOrientationParameter();
            } else if (i != 51) {
                if (i == 256) {
                    CloneModule.this.startVideoRecording();
                } else if (i == 257) {
                    CloneModule cloneModule4 = CloneModule.this;
                    if (cloneModule4.mInRecording) {
                        cloneModule4.mFrameCount++;
                    }
                }
            } else if (!CloneModule.this.mActivity.isActivityPaused()) {
                CloneModule cloneModule5 = CloneModule.this;
                cloneModule5.mOpenCameraFail = true;
                cloneModule5.onCameraException();
            }
        }
    }

    class StateListener implements FenShenCam.Listener {
        private WeakReference mModuleRef;

        public StateListener(CloneModule cloneModule) {
            this.mModuleRef = new WeakReference(cloneModule);
        }

        public void onMessage(Message message) {
            CloneModule cloneModule = (CloneModule) this.mModuleRef.get();
            if (cloneModule != null) {
                cloneModule.onCloneMessage(message);
            }
        }

        public void onPhotoResult(byte[] bArr) {
            CloneModule cloneModule = (CloneModule) this.mModuleRef.get();
            String access$300 = CloneModule.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPhotoResult length = ");
            sb.append(bArr.length);
            sb.append(", module = ");
            sb.append(cloneModule);
            Log.d(access$300, sb.toString());
            if (cloneModule != null) {
                cloneModule.onPhotoResult(bArr);
            }
        }

        public void onStartPreview() {
            CloneModule cloneModule = (CloneModule) this.mModuleRef.get();
            String access$300 = CloneModule.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onStartPreview ");
            sb.append(cloneModule);
            Log.d(access$300, sb.toString());
            if (cloneModule != null && cloneModule.isAlive()) {
                cloneModule.resumePreview();
            }
        }

        public void onStopPreview() {
            CloneModule cloneModule = (CloneModule) this.mModuleRef.get();
            String access$300 = CloneModule.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onStopPreview ");
            sb.append(cloneModule);
            Log.d(access$300, sb.toString());
            if (cloneModule != null) {
                cloneModule.pausePreview();
            }
        }

        public void onStopRecord() {
            Log.d(CloneModule.TAG, "onStopRecord");
        }

        public void onSubjectCount(int i) {
            CloneModule cloneModule = (CloneModule) this.mModuleRef.get();
            if (cloneModule != null) {
                cloneModule.onSubjectCountChange(i);
            }
        }

        @WorkerThread
        public void onVideoSaved(int i) {
            String access$300 = CloneModule.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("saveVideo timeUsedMs ");
            sb.append(i);
            Log.k(3, access$300, sb.toString());
            CloneModule cloneModule = (CloneModule) this.mModuleRef.get();
            if (cloneModule != null) {
                cloneModule.onVideoSaveFinish();
            }
        }

        public void requestRender() {
            CloneModule cloneModule = (CloneModule) this.mModuleRef.get();
            if (cloneModule != null) {
                cloneModule.mActivity.getGLView().requestRender();
            }
        }
    }

    static /* synthetic */ void O00oOo0O() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess == null || !cloneProcess.isAdded()) {
            Log.w(TAG, "onCloneMessage PAUSED return cloneProcess is null");
        } else {
            cloneProcess.showPlayButton();
        }
    }

    static /* synthetic */ void O00oOoO() {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            mainContentProtocol.setCenterHint(8, null, null, 0);
        }
    }

    static /* synthetic */ void O00oOoo() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null) {
            cloneProcess.showStopAndCancel();
        }
    }

    private void cancelVideoCountDown() {
        if (this.mCountDownTimer != null) {
            Log.d(TAG, "cancelVideoCountDown");
            this.mCountDownTimer.cancel();
            this.mCountDownTimer = null;
            hiddenTopRecordingTime();
        }
    }

    private void hiddenTopRecordingTime() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.setRecordingTimeState(2);
        }
    }

    /* access modifiers changed from: private */
    public void initCloneMode() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess == null) {
            Log.w(TAG, "initCloneMode failed, cloneProcess is null");
            return;
        }
        this.mMode = cloneProcess.getMode();
        CameraSize cameraSize = this.mAlgorithmPreviewSize;
        FenShenCam.init(cameraSize.width, cameraSize.height, this.mActivity.getFilesDir().getAbsolutePath(), this.mActivity.getApplicationInfo().nativeLibraryDir);
        FenShenCam.setListener(this.mCloneListener);
        FenShenCam.setMode(this.mMode);
        this.mPendingStart = true;
        initCloneMode(this.mMode);
        this.mActivity.getGLView().setRendererListener(this);
    }

    private boolean isConsumeOnScrollEvent() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        return this.mMode == Mode.MCOPY && (cloneProcess == null ? null : cloneProcess.getStatus()) == Status.EDIT;
    }

    private boolean isConsumeTouchEvent() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        Status status = cloneProcess == null ? null : cloneProcess.getStatus();
        return this.mMode == Mode.MCOPY && (status == Status.SAVE || status == Status.EDIT);
    }

    /* access modifiers changed from: private */
    public void onPhotoResult(byte[] bArr) {
        this.mJpgBytes = bArr;
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess == null) {
            Log.w(TAG, "onPhotoResult cloneProcess is null");
        } else {
            this.mHandler.post(new O000O0o0(cloneProcess));
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002f, code lost:
        if (r4 == 4) goto L_0x003c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onSubjectCountChange(int i) {
        if (this.mLastSubjectCount != i) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onSubjectCountChange ");
            sb.append(i);
            Log.d(str, sb.toString());
            enableCameraControls(true);
            this.mLastSubjectCount = i;
            Mode mode = this.mMode;
            if (mode == Mode.PHOTO) {
                if (i == 2) {
                    showStopAndCancel();
                }
            } else if (mode != Mode.VIDEO) {
            }
            stopCaptureToPreviewResult();
        }
    }

    private void previewWhenSessionSuccess() {
        setCameraState(1);
        updatePreferenceInWorkThread(UpdateConstant.CLONE_TYPES_ON_PREVIEW_SUCCESS);
    }

    private void resetAndUnlock3A() {
        Log.d(TAG, "resetAndUnlock3A");
        this.mIsDuringShooting = false;
        this.mIsSegmentRecording = false;
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy == null) {
            Log.w(TAG, "resetAndUnlock3A, mCamera2Device is null");
            return;
        }
        if (this.mAeLockSupported) {
            camera2Proxy.setAELock(false);
        }
        if (this.mAwbLockSupported) {
            this.mCamera2Device.setAWBLock(false);
        }
        this.mCamera2Device.setFocusMode(4);
        this.mCamera2Device.resumePreview();
    }

    private void savePhoto(byte[] bArr) {
        byte[] bArr2 = bArr;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr2, 0, bArr2.length, options);
        if (options.outWidth == 0 || options.outHeight == 0) {
            Log.w(TAG, "savePhoto error, can't decode bounds");
            return;
        }
        String createJpegName = Util.createJpegName(System.currentTimeMillis());
        String str = createJpegName;
        Log.k(3, TAG, String.format("savePhoto title %s, length %s", new Object[]{createJpegName, Integer.valueOf(bArr2.length)}));
        this.mActivity.getImageSaver().addImage(bArr, true, str, null, System.currentTimeMillis(), null, null, options.outWidth, options.outHeight, null, 0, false, false, true, false, false, null, null, -1, null);
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
                CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new C0358O000O0oo(this));
            } else {
                updatePreferenceInWorkThread(35);
            }
        }
    }

    private void showStopAndCancel() {
        this.mHandler.post(O000O0o.INSTANCE);
    }

    private void startCountDown() {
        AnonymousClass1 r4 = new CountDownTimer(((long) getDurationVideoRecording()) + 450, 1000) {
            public void onFinish() {
                CloneModule.this.onShutterButtonClick(10);
            }

            public void onTick(long j) {
                CloneModule.this.updateRecordingTime(j);
            }
        };
        this.mCountDownTimer = r4;
        this.mCountDownTimer.start();
    }

    private void statCaptureHint(Message message) {
        if (message != this.mLastMessage) {
            CameraStatUtils.trackCloneCaptureHint(this.mMode, message);
        }
    }

    private void updateDeviceOrientation() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
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
        setFocusMode(this.mFocusManager.setFocusMode(CameraSettings.getFocusMode()));
    }

    private void updateLiveRelated() {
        this.mCamera2Device.startPreviewCallback(this, null);
    }

    private void updatePictureAndPreviewSize() {
        this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(SurfaceTexture.class);
        this.mPreviewSize = new CameraSize(1920, 1080);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updatePictureAndPreviewSize previewSize: ");
        sb.append(this.mPreviewSize.toString());
        Log.d(str, sb.toString());
        this.mAlgorithmPreviewSize = new CameraSize(1280, 960);
        this.mPictureSize = null;
        int O0oooOo = C0122O00000o.instance().O0oooOo();
        PictureSizeManager.initialize(this.mCameraCapabilities.getSupportedOutputSizeWithAssignedMode(35), O0oooOo, this.mModuleIndex, this.mBogusCameraId);
        this.mPictureSize = PictureSizeManager.getBestPictureSize(this.mModuleIndex);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("updatePictureAndPreviewSize sizeLimit ");
        sb2.append(O0oooOo);
        sb2.append(", mPictureSize ");
        sb2.append(this.mPictureSize);
        sb2.append(", mAlgorithmPreviewSize ");
        sb2.append(this.mAlgorithmPreviewSize);
        Log.d(str2, sb2.toString());
        CameraSize cameraSize = this.mPreviewSize;
        updateCameraScreenNailSize(cameraSize.width, cameraSize.height);
    }

    /* access modifiers changed from: private */
    public void updateRecordingTime(long j) {
        String millisecondToTimeString = Util.millisecondToTimeString((j + 950) - 450, false);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.updateRecordingTime(millisecondToTimeString);
        }
    }

    public /* synthetic */ void O000000o(Uri uri) {
        this.mJpgBytes = null;
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null) {
            cloneProcess.onSaveFinish(uri);
        }
    }

    public /* synthetic */ void O000000o(Message message, int i) {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess == null || !cloneProcess.isAdded()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateCaptureMessage return cloneProcess is null, ");
            sb.append(message.toString());
            Log.w(str, sb.toString());
            return;
        }
        String str2 = "too much movement, stop capture";
        if (message == Message.ALIGN_TOO_LARGE_OR_FAILED && this.mMode == Mode.MCOPY) {
            Log.d(TAG, str2);
            cloneProcess.updateCaptureMessage((int) R.string.clone_too_much_movement_stop, false);
            onShutterButtonClick(10, true);
        } else if (message == Message.ALIGN_TOO_LARGE_OR_FAILED && FenShenCam.getCurrentSubjectCount() >= 2) {
            Log.d(TAG, str2);
            cloneProcess.updateCaptureMessage((int) R.string.clone_too_much_movement_stop, false);
            cloneProcess.stopCaptureToPreviewResult(true);
        } else if (onCloneMessage(this.mMode, message, cloneProcess, i)) {
            if (message == Message.ALIGN_TOO_LARGE_OR_FAILED || (message == Message.NO_PERSON && FenShenCam.getCurrentSubjectCount() == 0)) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onCloneMessage ");
                sb2.append(message.toString());
                Log.d(str3, sb2.toString());
                resetAndUnlock3A();
                cancelVideoCountDown();
                cloneProcess.setDetectedPersonInPreview(false);
                cloneProcess.prepare(this.mMode, true);
                this.mHandler.postDelayed(O000OO0o.INSTANCE, 3000);
            }
        }
    }

    public /* synthetic */ void O00oOoo0() {
        this.mCamera2Device.setDeviceOrientation(this.mOrientation);
    }

    public /* synthetic */ void O00oo00() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null) {
            stopCaptureToPreviewResult(this.mMode);
            cloneProcess.stopCaptureToPreviewResult(false);
        }
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

    /* access modifiers changed from: protected */
    public void cancelPhotoOrVideo() {
        if (this.mMode == Mode.PHOTO) {
            FenShenCam.cancelPhoto();
        } else {
            FenShenCam.cancelVideo();
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

    /* access modifiers changed from: protected */
    public boolean checkShutterCondition() {
        if (isIgnoreTouchEvent()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("checkShutterCondition: ignoreTouchEvent=");
            sb.append(isIgnoreTouchEvent());
            Log.w(str, sb.toString());
            return false;
        } else if (Storage.isLowStorageAtLastPoint()) {
            Log.w(TAG, "checkShutterCondition: low storage");
            return false;
        } else {
            CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
            if (cloneProcess == null || !cloneProcess.canSnap()) {
                Log.w(TAG, "checkShutterCondition: can't snap");
                return false;
            }
            BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
            if (backStack != null) {
                backStack.handleBackStackFromShutter();
            }
            return true;
        }
    }

    public void closeCamera() {
        Log.d(TAG, "closeCamera E");
        Camera camera = this.mActivity;
        if (camera != null) {
            camera.getCameraScreenNail().setExternalFrameProcessor(null);
        }
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.setFocusCallback(null);
            this.mCamera2Device.setErrorCallback(null);
            this.mCamera2Device.stopPreviewCallback(true);
            this.mCamera2Device = null;
        }
        FocusManager2 focusManager2 = this.mFocusManager;
        if (focusManager2 != null) {
            focusManager2.setAeAwbLock(false);
            this.mFocusManager.destroy();
        }
        Log.d(TAG, "closeCamera X");
    }

    /* access modifiers changed from: protected */
    public void consumePreference(int... iArr) {
        for (int i : iArr) {
            if (i == 1) {
                updatePictureAndPreviewSize();
            } else if (i == 3) {
                updateFocusArea();
            } else if (i == 9) {
                updateAntiBanding(CameraSettings.getAntiBanding());
            } else if (i == 14) {
                updateFocusMode();
            } else if (i == 25) {
                focusCenter();
            } else if (i == 29) {
                updateExposureMeteringMode();
            } else if (i == 35) {
                updateDeviceOrientation();
            } else if (i == 58) {
                updateBackSoftLightPreference();
            } else if (i == 66) {
                updateThermalLevel();
            } else if (i == 54) {
                updateLiveRelated();
            } else if (i != 55) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("no consumer for this updateType: ");
                sb.append(i);
                Log.d(str, sb.toString());
            } else {
                updateModuleRelated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void delayTriggerShooting() {
        Handler handler = this.mHandler;
        if (handler != null && !handler.hasMessages(256)) {
            if (CameraSettings.isCameraSoundOpen()) {
                playCameraSound(2);
                this.mHandler.sendEmptyMessageDelayed(256, 300);
                return;
            }
            startVideoRecording();
        }
    }

    /* access modifiers changed from: protected */
    public void doLaterReleaseIfNeed() {
        Camera camera = this.mActivity;
        if (camera != null && camera.isActivityPaused()) {
            this.mActivity.pauseIfNotRecording();
            this.mActivity.releaseAll(true, true);
        }
    }

    public void fillFeatureControl(StartControl startControl) {
        StartControlFeatureDetail featureDetail = startControl.getFeatureDetail();
        featureDetail.addFragmentInfo(R.id.full_screen_feature, BaseFragmentDelegate.FRAGMENT_CLONE_PROCESS);
        featureDetail.hideFragment(R.id.bottom_action);
        featureDetail.hideFragment(R.id.bottom_popup_tips);
        featureDetail.hideFragment(R.id.bottom_popup_dual_camera_adjust);
    }

    /* access modifiers changed from: protected */
    public int getDurationVideoRecording() {
        return this.mMode == Mode.MCOPY ? 10000 : 5000;
    }

    /* access modifiers changed from: protected */
    public int getMaxVideoSubjectCount() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return CameraCapabilities.SESSION_OPERATION_MODE_CLONE_MODE;
    }

    public String getTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void initCloneMode(Mode mode) {
        if ((mode == Mode.PHOTO && !DataRepository.dataItemGlobal().isFirstUseClonePhoto()) || (mode == Mode.VIDEO && !DataRepository.dataItemGlobal().isFirstUseCloneVideo())) {
            FenShenCam.start();
            this.mPendingStart = false;
        }
    }

    public void initializeCapabilities() {
        super.initializeCapabilities();
        this.mContinuousFocusSupported = Util.isSupported(4, this.mCameraCapabilities.getSupportedFocusModes());
        this.mMaxFaceCount = this.mCameraCapabilities.getMaxFaceCount();
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

    public boolean isDoingAction() {
        return getCameraState() == 3;
    }

    public boolean isProcessorReady() {
        return isFrameAvailable();
    }

    public boolean isRecording() {
        Handler handler = this.mHandler;
        if (handler == null || !handler.hasMessages(256)) {
            return this.mIsDuringShooting;
        }
        return true;
    }

    public boolean isSelectingCapturedResult() {
        return false;
    }

    public boolean isUnInterruptable() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoMode() {
        return this.mMode == Mode.VIDEO;
    }

    public boolean isZoomEnabled() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean judgeTapableRectByUiStyle() {
        return false;
    }

    public boolean multiCapture() {
        return false;
    }

    public void notifyFocusAreaUpdate() {
        updatePreferenceTrampoline(3);
    }

    public void onAdjustClicked() {
        if (this.mMode == Mode.MCOPY) {
            Log.d(TAG, "onAdjustClicked");
            FenShenCam.editMultiCopy();
        }
    }

    public boolean onBackPressed() {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess == null) {
            return super.onBackPressed();
        }
        CameraStatUtils.trackCloneClick(CloneAttr.VALUE_BACK_CLICK);
        cloneProcess.onBackPress();
        return true;
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
        startPreviewSession();
        this.mOnResumeTime = SystemClock.uptimeMillis();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
    }

    public void onCancelClicked() {
        cancelPhotoOrVideo();
        resetAndUnlock3A();
        FenShenCam.start();
    }

    public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
    }

    public void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4) {
    }

    public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
        return parallelTaskData;
    }

    public void onCloneGuideClicked() {
        Log.d(TAG, "onCloneGuideClicked");
        this.mPendingStart = true;
        cancelPhotoOrVideo();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0042, code lost:
        statCaptureHint(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x004a, code lost:
        r1 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        r3.mLastMessage = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004d, code lost:
        if (r1 == -1) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        r3.mHandler.post(new com.android.camera.module.O000OO00(r3, r4, r1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCloneMessage(Message message) {
        int i = AnonymousClass4.$SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[message.ordinal()];
        int i2 = R.string.clone_no_person_found;
        switch (i) {
            case 1:
            case 2:
                i2 = R.string.clone_mode_start_hint;
                break;
            case 3:
                break;
            case 4:
                i2 = R.string.clone_do_not_move_phone;
                break;
            case 5:
                i2 = R.string.clone_put_phone_back_in_place;
                break;
            case 6:
                i2 = R.string.clone_offset_is_too_large;
                break;
            case 7:
                enableCameraControls(true);
                break;
            case 8:
                onVideoSaveFinish();
                break;
            case 9:
                Log.d(TAG, "onCloneMessage PAUSED");
                Mode mode = this.mMode;
                if (mode == Mode.VIDEO || mode == Mode.MCOPY) {
                    this.mHandler.post(C0357O000O0oO.INSTANCE);
                    break;
                }
            case 10:
            case 11:
                onError();
                break;
        }
    }

    /* access modifiers changed from: protected */
    public boolean onCloneMessage(Mode mode, Message message, CloneProcess cloneProcess, int i) {
        boolean z = false;
        if (message == Message.PREVIEW_NO_PERSON || message == Message.NO_PERSON) {
            cloneProcess.setDetectedPersonInPreview(false);
        } else if (message == Message.START) {
            cloneProcess.setDetectedPersonInPreview(true);
        }
        if (message == Message.ALIGN_TOO_LARGE_OR_FAILED) {
            z = true;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateCaptureMessage ");
        sb.append(message.toString());
        Log.d(str, sb.toString());
        cloneProcess.updateCaptureMessage(i, z);
        return true;
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        FenShenCam.initResources(this.mActivity);
        this.mActivity.getSensorStateManager().setSensorStateListener(this.mSensorStateListener);
        this.mHandler = new MainHandler(this.mActivity.getMainLooper());
        onCameraOpened();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
        this.mActivity.getSensorStateManager().setRotationVectorEnabled(true);
        onCreate(this.mMode);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Mode mode) {
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        this.mHandler.post(O00oOoOo.INSTANCE);
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessage(45);
        }
        this.mActivity.getSensorStateManager().setRotationVectorEnabled(false);
        this.mActivity.getGLView().setPendingChange(true);
        this.mActivity.getGLView().queueEvent(O000O0OO.INSTANCE);
    }

    public void onDrawFrame(Rect rect, int i, int i2, boolean z) {
        FenShenCam.render();
    }

    /* access modifiers changed from: protected */
    public void onError() {
    }

    public void onExitClicked() {
        Log.d(TAG, "onExitClicked");
        if (isVideoMode()) {
            cancelVideoCountDown();
        }
    }

    public void onFocusStateChanged(FocusTask focusTask) {
        if (isCreated() && !isDeparted()) {
            int focusTrigger = focusTask.getFocusTrigger();
            if (focusTrigger == 1) {
                Log.v(TAG, String.format(Locale.ENGLISH, "FocusTime=%1$dms focused=%2$b", new Object[]{Long.valueOf(focusTask.getElapsedTime()), Boolean.valueOf(focusTask.isSuccess())}));
                if (!this.mFocusManager.isFocusingSnapOnFinish() && getCameraState() != 3) {
                    setCameraState(1);
                }
                this.mFocusManager.onFocusResult(focusTask);
                this.mActivity.getSensorStateManager().reset();
            } else if (focusTrigger == 2 || focusTrigger == 3) {
                String str = null;
                if (focusTask.isFocusing()) {
                    str = "onAutoFocusMoving start";
                }
                if (Util.sIsDumpLog && str != null) {
                    Log.v(TAG, str);
                }
                if (getCameraState() != 3 || focusTask.getFocusTrigger() == 3) {
                    this.mFocusManager.onFocusResult(focusTask);
                }
            }
        }
    }

    public void onFragmentResume() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onFragmentResume, cameraState = ");
        sb.append(getCameraState());
        Log.d(str, sb.toString());
        if (this.mPendingStart) {
            this.mPendingStart = false;
            cancelPhotoOrVideo();
            FenShenCam.start();
        }
        if (getCameraState() == 0) {
            resumePreview();
        }
        if (this.mMode == Mode.MCOPY) {
            this.mActivity.getGLView().queueEvent(new Runnable() {
                public void run() {
                    CloneModule.this.onCloneMessage(Message.START);
                }
            });
        }
    }

    public void onGiveUpClicked() {
        Log.d(TAG, "onGiveUpClicked");
        cancelPhotoOrVideo();
        resetAndUnlock3A();
        FenShenCam.start();
    }

    public void onGiveUpEditClicked() {
        if (this.mMode == Mode.MCOPY) {
            Log.d(TAG, "onGiveUpEditClicked");
            FenShenCam.cancelEdit();
        }
    }

    public void onHostStopAndNotifyActionStop() {
        super.onHostStopAndNotifyActionStop();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onHostStopAndNotifyActionStop ");
        sb.append(this.mIsFinished);
        Log.d(str, sb.toString());
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(256)) {
            this.mHandler.removeMessages(256);
            Log.d(TAG, "remove delay message of 'startVideoRecording'");
        }
        onHostStopAndNotifyActionStop(this.mMode);
        resumePreviewIfNeeded();
        doLaterReleaseIfNeed();
    }

    /* access modifiers changed from: protected */
    public void onHostStopAndNotifyActionStop(Mode mode) {
        if (this.mIsSegmentRecording) {
            this.mIsSegmentRecording = false;
            stopVideoRecording(true);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
        if (r6 != 88) goto L_0x0068;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean z = false;
        if (!isFrameAvailable()) {
            return false;
        }
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null && cloneProcess.getStatus() == Status.SAVE) {
            return false;
        }
        if (!(i == 24 || i == 25)) {
            if (i == 27 || i == 66) {
                if (keyEvent.getRepeatCount() == 0) {
                    performKeyClicked(40, getString(R.string.pref_camera_volumekey_function_entryvalue_shutter), keyEvent.getRepeatCount(), true);
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

    @WorkerThread
    public void onNewUriArrived(Uri uri, String str) {
        super.onNewUriArrived(uri, str);
        if (isAlive()) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new C0356O000O0Oo(this, uri));
            }
        }
    }

    public void onOrientationChanged(int i, int i2, int i3) {
        setOrientation(i, i2);
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

    public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
    }

    public void onPictureTakenFinished(boolean z) {
    }

    public boolean onPictureTakenImageConsumed(Image image, TotalCaptureResult totalCaptureResult) {
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess == null || cloneProcess.getStatus() == Status.CAPTURING) {
            FenShenCam.addPhoto(image);
            HashMap hashMap = new HashMap();
            hashMap.put(Manual.PARAM_3A_LOCKED, Boolean.valueOf(false));
            trackGeneralInfo(hashMap, 1, false, null, false, 0);
            return true;
        }
        Log.w(TAG, "onPictureTakenImageConsumed not capturing");
        return true;
    }

    public void onPlayClicked() {
        if (this.mMode == Mode.MCOPY) {
            Log.d(TAG, "onPlayClicked");
            FenShenCam.playPreview();
        }
    }

    public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
        try {
            if (this.mInRecording) {
                this.mHandler.obtainMessage(257).sendToTarget();
            }
            FenShenCam.addPreviewFrame(image);
        } catch (IllegalStateException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("addPreviewFrame fail, ");
            sb.append(e.getMessage());
            Log.w(str, sb.toString());
        }
        return true;
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

    public void onResetEditClicked() {
        if (this.mMode == Mode.MCOPY) {
            Log.d(TAG, "onResetEditClicked");
            FenShenCam.resetEdit();
        }
    }

    public void onResume() {
        super.onResume();
        keepScreenOnAwhile();
    }

    public void onReviewCancelClicked() {
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges == null) {
            Log.w(TAG, "onReviewDoneClicked return, configChanges is null");
        } else {
            configChanges.configClone(Mode.PHOTO, false);
        }
    }

    public void onReviewDoneClicked() {
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges == null) {
            Log.w(TAG, "onReviewDoneClicked return, configChanges is null");
        } else {
            configChanges.configClone(Mode.PHOTO, false);
        }
    }

    public void onSaveClicked() {
        Mode mode = this.mMode;
        Mode mode2 = Mode.PHOTO;
        if (mode != mode2) {
            saveVideo();
        } else if (this.mJpgBytes == null) {
            Log.e(TAG, "onSaveClicked mJpgBytes is null");
        } else {
            CameraStatUtils.trackCloneCaptureParams(mode2, this.mSubjectCount, "");
            savePhoto(this.mJpgBytes);
        }
    }

    public void onSaveEditClicked() {
        if (this.mMode == Mode.MCOPY) {
            Log.d(TAG, "onConfirmClicked");
            FenShenCam.saveEdit();
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onScroll  (");
        sb.append(motionEvent.getX());
        String str2 = ",";
        sb.append(str2);
        sb.append(motionEvent.getY());
        sb.append(")  drag (");
        sb.append(motionEvent2.getX());
        sb.append(str2);
        sb.append(motionEvent2.getY());
        sb.append(")");
        Log.c(str, sb.toString());
        if (!isConsumeOnScrollEvent()) {
            return false;
        }
        FenShenCam.sendTouchEvent(TEventType.DRAG, 0.0f, motionEvent.getX(), motionEvent.getY(), motionEvent2.getX(), motionEvent2.getY());
        this.mScrolled = true;
        return true;
    }

    public void onShutterButtonClick(int i) {
        onShutterButtonClick(i, false);
    }

    public void onShutterButtonClick(int i, boolean z) {
        if (checkShutterCondition() || z) {
            if (i == 110) {
                this.mActivity.onUserInteraction();
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.hideRecommendDescTip(FragmentTopAlert.TIP_SPEECH_SHUTTER_DESC);
                }
            }
            setTriggerMode(i);
            MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
            if (mainContentProtocol != null) {
                mainContentProtocol.clearFocusView(1);
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onShutterButtonClick mIsDuringShooting = ");
            sb.append(this.mIsDuringShooting);
            Log.u(str, sb.toString());
            if (!this.mIsDuringShooting) {
                this.mIsDuringShooting = true;
                ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).disableMenuItem(true, 164);
                ((CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418)).processingStart();
                if (this.mAeLockSupported) {
                    this.mCamera2Device.setAELock(true);
                }
                if (this.mAwbLockSupported) {
                    this.mCamera2Device.setAWBLock(true);
                }
                this.mCamera2Device.setFocusMode(1);
                this.mCamera2Device.resumePreview();
            }
            onShutterButtonClick(this.mMode);
        }
    }

    /* access modifiers changed from: protected */
    public void onShutterButtonClick(Mode mode) {
        if (mode == Mode.PHOTO) {
            enableCameraControls(false);
            this.mCamera2Device.takePicture(this, null);
            playCameraSound(0);
            return;
        }
        Mode mode2 = this.mMode;
        String str = "onShutterButtonClick startVideoRecording";
        String str2 = "onShutterButtonClick stopVideoRecording";
        if (mode2 == Mode.VIDEO) {
            if (!this.mIsSegmentRecording) {
                Log.u(TAG, str);
                playCameraSound(2);
                startVideoRecording();
            } else {
                Log.u(TAG, str2);
                playCameraSound(3);
                stopVideoRecording(false);
            }
            this.mIsSegmentRecording = !this.mIsSegmentRecording;
            return;
        }
        if (mode2 == Mode.MCOPY) {
            if (!this.mIsSegmentRecording) {
                Log.u(TAG, str);
                delayTriggerShooting();
            } else if (this.mFrameCount < 15) {
                Log.d(TAG, "ignore onShutterButtonClick cause frameCount < 15");
                return;
            } else {
                Log.u(TAG, str2);
                stopVideoRecording(false);
                enableCameraControls(false);
            }
            this.mIsSegmentRecording = !this.mIsSegmentRecording;
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
        if (!this.mPaused && !this.mIsDuringShooting) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (!(camera2Proxy == null || !camera2Proxy.isSessionReady() || !isInTapableRect(i, i2) || getCameraState() == 3 || getCameraState() == 0)) {
                if (!isFrameAvailable()) {
                    Log.w(TAG, "onSingleTapUp: frame not available");
                    return;
                } else if (((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromTapDown(i, i2)) {
                    Log.w(TAG, "onSingleTapUp: ignore, handleBackStackFromTapDown");
                    return;
                } else {
                    this.mMainProtocol.setFocusViewType(true);
                    this.mTouchFocusStartingTime = System.currentTimeMillis();
                    Point point = new Point(i, i2);
                    mapTapCoordinate(point);
                    this.mFocusManager.onSingleTapUp(point.x, point.y, z);
                    return;
                }
            }
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSingleTapUp: ignore, mIsDuringShooting ");
        sb.append(this.mIsDuringShooting);
        sb.append(", getCameraState = ");
        sb.append(getCameraState());
        Log.w(str, sb.toString());
    }

    public void onStop() {
        super.onStop();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void onStopClicked() {
        Log.d(TAG, "onStopClicked");
        this.mSubjectCount = this.mLastSubjectCount;
        if (this.mMode == Mode.PHOTO) {
            FenShenCam.finishPhoto();
        }
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        Rect displayRect = Util.getDisplayRect(1);
        int i3 = i;
        int i4 = i2;
        FenShenCam.renderInit(i3, i4, Display.getStartMargin(), displayRect.top, (i - Display.getStartMargin()) - Display.getEndMargin(), displayRect.height());
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
    }

    public boolean onTapUp(float f, float f2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onTapUp  (");
        sb.append(f);
        sb.append(",");
        sb.append(f2);
        sb.append(")");
        Log.d(str, sb.toString());
        if (!isConsumeTouchEvent()) {
            return false;
        }
        FenShenCam.sendTouchEvent(TEventType.CLICK_UP, 0.0f, f, f2, 0.0f, 0.0f);
        return true;
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

    public void onTimeFreezeClicked() {
    }

    public boolean onTouchDown(float f, float f2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onTouchDown  (");
        sb.append(f);
        sb.append(",");
        sb.append(f2);
        sb.append(")");
        Log.d(str, sb.toString());
        if (!isConsumeTouchEvent()) {
            return false;
        }
        FenShenCam.sendTouchEvent(TEventType.CLICK_DOWN, 0.0f, f, f2, 0.0f, 0.0f);
        return true;
    }

    public boolean onTouchUp(float f, float f2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onTouchUp  (");
        sb.append(f);
        sb.append(",");
        sb.append(f2);
        sb.append(")");
        Log.d(str, sb.toString());
        if (isConsumeTouchEvent()) {
            FenShenCam.sendTouchEvent(TEventType.GENERIC_UP, 0.0f, f, f2, 0.0f, 0.0f);
            if (this.mScrolled) {
                CameraStatUtils.trackCloneClick(CloneAttr.VALUE_DRAG_SUBJECT_CLICK);
                this.mScrolled = false;
            }
            return true;
        }
        this.mScrolled = false;
        return false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0064  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onVideoSaveFinish() {
        ContentValues contentValues;
        Location currentLocation;
        CloneProcess cloneProcess;
        int i;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSaveFinish ");
        sb.append(this.mVideoFilePath);
        Log.k(3, str, sb.toString());
        String name = new File(this.mVideoFilePath).getName();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(name);
        int i2 = 2;
        sb2.append(Util.convertOutputFormatToFileExt(2));
        String sb3 = sb2.toString();
        String convertOutputFormatToMimeType = Util.convertOutputFormatToMimeType(2);
        int duration = (int) (Util.getDuration(this.mVideoFilePath) / 1000);
        Mode mode = this.mMode;
        if (mode == Mode.VIDEO) {
            i = 5;
        } else {
            if (mode == Mode.MCOPY) {
                i = 10;
            }
            int max = Math.max(duration, 1);
            if (this.mMode == Mode.MCOPY) {
                i2 = this.mSubjectCount;
            }
            Mode mode2 = this.mMode;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(max);
            sb4.append("s");
            CameraStatUtils.trackCloneCaptureParams(mode2, i2, sb4.toString());
            contentValues = new ContentValues(8);
            contentValues.put("title", name);
            contentValues.put("_display_name", sb3);
            contentValues.put("mime_type", convertOutputFormatToMimeType);
            contentValues.put("_data", this.mVideoFilePath);
            StringBuilder sb5 = new StringBuilder();
            sb5.append(this.mAlgorithmPreviewSize.width);
            sb5.append("x");
            sb5.append(this.mAlgorithmPreviewSize.height);
            contentValues.put("resolution", sb5.toString());
            currentLocation = LocationManager.instance().getCurrentLocation();
            if (!(currentLocation == null || (currentLocation.getLatitude() == 0.0d && currentLocation.getLongitude() == 0.0d))) {
                contentValues.put("latitude", Double.valueOf(currentLocation.getLatitude()));
                contentValues.put("longitude", Double.valueOf(currentLocation.getLongitude()));
            }
            cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
            if (cloneProcess != null) {
                cloneProcess.onPreviewPrepare(contentValues);
            }
            this.mActivity.getImageSaver().addVideo(this.mVideoFilePath, contentValues, true);
        }
        duration = Math.min(duration, i);
        int max2 = Math.max(duration, 1);
        if (this.mMode == Mode.MCOPY) {
        }
        Mode mode22 = this.mMode;
        StringBuilder sb42 = new StringBuilder();
        sb42.append(max2);
        sb42.append("s");
        CameraStatUtils.trackCloneCaptureParams(mode22, i2, sb42.toString());
        contentValues = new ContentValues(8);
        contentValues.put("title", name);
        contentValues.put("_display_name", sb3);
        contentValues.put("mime_type", convertOutputFormatToMimeType);
        contentValues.put("_data", this.mVideoFilePath);
        StringBuilder sb52 = new StringBuilder();
        sb52.append(this.mAlgorithmPreviewSize.width);
        sb52.append("x");
        sb52.append(this.mAlgorithmPreviewSize.height);
        contentValues.put("resolution", sb52.toString());
        currentLocation = LocationManager.instance().getCurrentLocation();
        contentValues.put("latitude", Double.valueOf(currentLocation.getLatitude()));
        contentValues.put("longitude", Double.valueOf(currentLocation.getLongitude()));
        cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess != null) {
        }
        this.mActivity.getImageSaver().addVideo(this.mVideoFilePath, contentValues, true);
    }

    public boolean onWaitingFocusFinished() {
        return !isBlockSnap() && isAlive();
    }

    public void pausePreview() {
        Log.d(TAG, "pausePreview");
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.pausePreview();
        }
        this.mIsFinished = true;
        setCameraState(0);
    }

    /* access modifiers changed from: protected */
    public void performKeyClicked(int i, String str, int i2, boolean z) {
        if (!this.mPaused && getCameraState() != 0 && i2 == 0) {
            if (z) {
                onShutterButtonFocus(true, 1);
                onShutterButtonClick(i);
                return;
            }
            onShutterButtonFocus(false, 0);
        }
    }

    public void playFocusSound(int i) {
        playCameraSound(i);
    }

    public void registerProtocol() {
        super.registerProtocol();
        ModeCoordinatorImpl.getInstance().attachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(419, this);
        getActivity().getImplFactory().initAdditional(getActivity(), 164, 212);
    }

    public void releaseRender() {
    }

    public void resumePreview() {
        Log.d(TAG, "resumePreview");
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy != null) {
            camera2Proxy.resumePreview();
            setCameraState(1);
        }
    }

    /* access modifiers changed from: protected */
    public void resumePreviewIfNeeded() {
        Log.d(TAG, "resumePreviewIfNeeded");
        this.mIsFinished = false;
        resumePreview();
        cancelPhotoOrVideo();
        FenShenCam.start();
    }

    /* access modifiers changed from: protected */
    public void saveVideo() {
        String format = new SimpleDateFormat(getString(R.string.video_file_name_format), Locale.ENGLISH).format(new Date(System.currentTimeMillis()));
        StringBuilder sb = new StringBuilder();
        sb.append(format);
        sb.append(Util.convertOutputFormatToFileExt(2));
        this.mVideoFileName = sb.toString();
        this.mVideoFilePath = Storage.generateFilepath(this.mVideoFileName);
        String str = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("saveVideo start, path = ");
        sb2.append(this.mVideoFilePath);
        Log.k(3, str, sb2.toString());
        setVideoCodec();
        FenShenCam.saveVideo(this.mVideoFilePath);
    }

    public void setFrameAvailable(boolean z) {
        super.setFrameAvailable(z);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFrameAvailable ");
        sb.append(z);
        Log.d(str, sb.toString());
        if (z) {
            this.mActivity.getCameraScreenNail().setExternalFrameProcessor(this);
            resumePreviewIfNeeded();
        }
    }

    /* access modifiers changed from: protected */
    public void setVideoCodec() {
        if (this.mMode == Mode.MCOPY) {
            FenShenCam.setVideoCodec(CameraSettings.getVideoEncoder() == 5 ? "video/hevc" : "video/avc");
        }
    }

    public boolean shouldCaptureDirectly() {
        return false;
    }

    public boolean shouldReleaseLater() {
        return this.mIsSegmentRecording || this.mIsFinished;
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
            checkDisplayOrientation();
            this.mCamera2Device.setDisplayOrientation(this.mCameraDisplayOrientation);
            if (this.mAeLockSupported) {
                this.mCamera2Device.setAELock(false);
            }
            if (this.mAwbLockSupported) {
                this.mCamera2Device.setAWBLock(false);
            }
            this.mCamera2Device.setFocusMode(4);
            this.mCamera2Device.resumePreview();
            setCameraState(1);
        }
    }

    /* access modifiers changed from: protected */
    public void startPreviewSession() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy == null) {
            Log.e(TAG, "startPreview: camera has been closed");
            return;
        }
        camera2Proxy.setDualCamWaterMarkEnable(false);
        this.mCamera2Device.setFocusCallback(this);
        this.mCamera2Device.setErrorCallback(this.mErrorCallback);
        this.mCamera2Device.setPreviewSize(this.mPreviewSize);
        this.mCamera2Device.setAlgorithmPreviewSize(this.mAlgorithmPreviewSize);
        this.mCamera2Device.setPictureSize(this.mPictureSize);
        this.mCamera2Device.setPictureMaxImages(3);
        this.mCamera2Device.setPictureFormat(35);
        this.mCamera2Device.startFaceDetection();
        this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
        this.mCamera2Device.startPreviewSession(new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture()), 1, 0, null, getOperatingMode(), false, this);
    }

    /* access modifiers changed from: protected */
    public void startVideoRecording() {
        Log.d(TAG, "startVideoRecording");
        CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
        if (cloneProcess == null) {
            Log.w(TAG, "startVideoRecording failed");
            return;
        }
        setCameraAudioRestriction(true);
        ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).setRecordingTimeState(1);
        cloneProcess.processingPrepare();
        cloneProcess.processingStart();
        startCountDown();
        startVideoRecording(this.mMode, cloneProcess);
    }

    /* access modifiers changed from: protected */
    public void startVideoRecording(Mode mode, CloneProcess cloneProcess) {
        this.mFrameCount = 0;
        this.mInRecording = true;
        FenShenCam.startRecordVideo();
    }

    /* access modifiers changed from: protected */
    public void stopCaptureToPreviewResult() {
        this.mHandler.post(new O000O00o(this));
    }

    /* access modifiers changed from: protected */
    public void stopCaptureToPreviewResult(Mode mode) {
        if (mode == Mode.MCOPY) {
            playCameraSound(3);
        }
    }

    /* access modifiers changed from: protected */
    public void stopVideoRecording(boolean z) {
        Handler handler = this.mHandler;
        if (handler == null || !handler.hasMessages(256)) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("stopVideoRecording giveUp ");
            sb.append(z);
            Log.d(str, sb.toString());
            setCameraAudioRestriction(false);
            cancelVideoCountDown();
            CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
            if (cloneProcess != null) {
                cloneProcess.processingFinish();
            }
            stopVideoRecording(z, this.mMode, cloneProcess);
            return;
        }
        this.mHandler.removeMessages(256);
        Log.d(TAG, "skip stopVideoRecording & remove startVideoRecording");
    }

    /* access modifiers changed from: protected */
    public void stopVideoRecording(boolean z, Mode mode, CloneProcess cloneProcess) {
        if (!z) {
            FenShenCam.stopRecordVideo();
            return;
        }
        FenShenCam.cancelVideo();
        FenShenCam.start();
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
        ModeCoordinatorImpl.getInstance().detachProtocol(419, this);
        getActivity().getImplFactory().detachAdditional();
    }

    public void updatePreviewSurface() {
        super.updatePreviewSurface();
        GLCanvasImpl gLCanvas = this.mActivity.getGLView().getGLCanvas();
        int width = gLCanvas.getWidth();
        int height = gLCanvas.getHeight();
        FenShenCam.renderInit(width, height, 0, 0, width, height);
    }
}
