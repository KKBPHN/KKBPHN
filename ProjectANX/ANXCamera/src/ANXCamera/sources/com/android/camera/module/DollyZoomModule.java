package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureResult;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import androidx.annotation.WorkerThread;
import com.android.camera.Camera;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.constant.UpdateConstant;
import com.android.camera.fragment.BaseFragmentDelegate;
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
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DollyZoomAction;
import com.android.camera.protocol.ModeProtocol.DollyZoomProcess;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.FilmAttr;
import com.android.camera.storage.Storage;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera.ui.V6CameraGLSurfaceView.RendererListener;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.PreviewCallback;
import com.android.camera2.CameraCapabilities;
import com.xiaomi.camera.rx.CameraSchedulers;
import com.xiaomi.dollyzoomprocess.MediaEffectCamera;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import miui.os.FileUtils;

public class DollyZoomModule extends BaseModule implements CameraPreviewCallback, PreviewCallback, ExternalFrameProcessor, RendererListener, CameraAction, DollyZoomAction, Listener {
    private static final int FIXED_AF_DISTANCE = 70;
    private static final long START_OFFSET_MS = 50;
    /* access modifiers changed from: private */
    public static final String TAG = "DollyZoomModule";
    public static final String TEMP_VIDEO_PATH = "/sdcard/.default_dz_video.mp4";
    public static final float ZOOM_SCALE_FORCE_SAVE = 2.0f;
    public static final float ZOOM_SCALE_STOP_RECORD = 4.0f;
    private Timer mCountTimer;
    private MediaEffectCamera mDollyZoomCamera = null;
    private FocusManager2 mFocusManager;
    private boolean mInitRender = false;
    private boolean mIsFinished;
    private boolean mIsRecording = false;
    private boolean mIsVideoSaveCancel = false;
    private boolean mIsVideoSaved = true;
    private int mLastDollyZoomState = -1;
    private int mLastRecordingState = -1;
    private final Object mLock = new Object();
    /* access modifiers changed from: private */
    public long mOnResumeTime;
    private boolean mPendingStart;
    /* access modifiers changed from: private */
    public long mRecordingStartTime;
    private Rect mRenderRect;
    private int mResetCaptureHintCont = 0;
    private String mVideoFileName;
    private String mVideoFilePath;

    class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 2) {
                DollyZoomModule.this.getWindow().clearFlags(128);
            } else if (i == 4) {
                DollyZoomModule.this.checkActivityOrientation();
                if (SystemClock.uptimeMillis() - DollyZoomModule.this.mOnResumeTime < 5000) {
                    DollyZoomModule.this.mHandler.sendEmptyMessageDelayed(4, 100);
                }
            } else if (i == 9) {
                DollyZoomModule dollyZoomModule = DollyZoomModule.this;
                dollyZoomModule.mMainProtocol.initializeFocusView(dollyZoomModule);
                DollyZoomModule.this.initDollyZoomMode();
            } else if (i == 17) {
                DollyZoomModule.this.mHandler.removeMessages(17);
                DollyZoomModule.this.mHandler.removeMessages(2);
                DollyZoomModule.this.getWindow().addFlags(128);
                DollyZoomModule dollyZoomModule2 = DollyZoomModule.this;
                dollyZoomModule2.mHandler.sendEmptyMessageDelayed(2, (long) dollyZoomModule2.getScreenDelay());
            } else if (i == 31) {
                DollyZoomModule.this.setOrientationParameter();
            } else if (i == 51 && !DollyZoomModule.this.mActivity.isActivityPaused()) {
                DollyZoomModule dollyZoomModule3 = DollyZoomModule.this;
                dollyZoomModule3.mOpenCameraFail = true;
                dollyZoomModule3.onCameraException();
            }
        }
    }

    class SaveVideoThread extends Thread {
        final File mDstFile;
        private WeakReference mModuleRef;
        final File mSrcFile;

        public SaveVideoThread(File file, File file2, DollyZoomModule dollyZoomModule) {
            this.mSrcFile = file;
            this.mDstFile = file2;
            this.mModuleRef = new WeakReference(dollyZoomModule);
        }

        public void run() {
            boolean z;
            setName("SaveVideoThread");
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            Log.d(DollyZoomModule.TAG, "saveVideo start");
            try {
                z = FileUtils.copyFile(this.mSrcFile, this.mDstFile);
                if (!z) {
                    this.mSrcFile.delete();
                    this.mDstFile.delete();
                }
            } catch (Throwable th) {
                Log.e(DollyZoomModule.TAG, "saveVideo failed.", th);
                z = true;
            }
            String access$300 = DollyZoomModule.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("saveVideo done, success=");
            sb.append(z);
            sb.append(" cost=");
            sb.append(System.currentTimeMillis() - valueOf.longValue());
            Log.d(access$300, sb.toString());
            DollyZoomModule dollyZoomModule = (DollyZoomModule) this.mModuleRef.get();
            if (dollyZoomModule != null) {
                dollyZoomModule.onVideoSaveFinish();
            }
        }
    }

    static /* synthetic */ void O00000Oo(Uri uri) {
        DollyZoomProcess dollyZoomProcess = (DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676);
        if (dollyZoomProcess != null) {
            dollyZoomProcess.onSaveFinish(uri);
        }
    }

    static /* synthetic */ void O00oo00O() {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            mainContentProtocol.setCenterHint(8, null, null, 0);
        }
    }

    private void cancelRecordingCount() {
        if (this.mCountTimer != null) {
            Log.d(TAG, "cancelRecordingCount");
            this.mCountTimer.cancel();
            this.mCountTimer = null;
            hiddenTopRecordingTime();
        }
    }

    private boolean checkShutterCondition() {
        if (this.mDollyZoomCamera == null || !this.mInitRender) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("checkShutterCondition: mInitRender:");
            sb.append(this.mInitRender);
            sb.append(" mDollyZoomCamera:");
            sb.append(this.mDollyZoomCamera);
            Log.w(str, sb.toString());
            return false;
        } else if (!this.mIsRecording && !this.mIsVideoSaved) {
            Log.w(TAG, "checkShutterCondition: The video has not been saved");
            return false;
        } else if (this.mIsRecording && System.currentTimeMillis() - this.mRecordingStartTime < 200) {
            Log.w(TAG, "checkShutterCondition: Stop recording too quickly");
            return false;
        } else if (isIgnoreTouchEvent()) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("checkShutterCondition: ignoreTouchEvent=");
            sb2.append(isIgnoreTouchEvent());
            Log.w(str2, sb2.toString());
            return false;
        } else if (Storage.isLowStorageAtLastPoint()) {
            Log.w(TAG, "checkShutterCondition: low storage");
            return false;
        } else {
            DollyZoomProcess dollyZoomProcess = (DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676);
            if (dollyZoomProcess == null || !dollyZoomProcess.canSnap()) {
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

    private void doLaterReleaseIfNeed() {
        Camera camera = this.mActivity;
        if (camera != null && camera.isActivityPaused()) {
            this.mActivity.pauseIfNotRecording();
            this.mActivity.releaseAll(true, true);
        }
    }

    private void hiddenTopRecordingTime() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.setRecordingTimeState(2);
        }
    }

    /* access modifiers changed from: private */
    public void initDollyZoomMode() {
        if (((DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676)) == null) {
            Log.w(TAG, "initDollyZoomMode failed, dollyZoomProcess is null");
            return;
        }
        boolean isCinematicAspectRatioEnabled = CameraSettings.isCinematicAspectRatioEnabled(this.mModuleIndex);
        int i = CameraSettings.getVideoEncoder() == 5 ? 1 : 0;
        boolean Oo0Oo = C0122O00000o.instance().getConfig().Oo0Oo();
        int orientation = (this.mActivity.getOrientation() + 90) % m.cQ;
        V6CameraGLSurfaceView gLView = this.mActivity.getGLView();
        C0361O000Oo00 o000Oo00 = new C0361O000Oo00(this, Oo0Oo ? 1 : 0, isCinematicAspectRatioEnabled ? 1 : 0, i, orientation);
        gLView.queueEvent(o000Oo00);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initDollyZoomMode filmSizeState:");
        sb.append(isCinematicAspectRatioEnabled ? 1 : 0);
        Log.d(str, sb.toString());
        this.mPendingStart = true;
        this.mActivity.getGLView().setRendererListener(this);
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

    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v1, types: [int] */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: type inference failed for: r1v13 */
    /* JADX WARNING: type inference failed for: r1v14 */
    /* JADX WARNING: type inference failed for: r1v15 */
    /* JADX WARNING: type inference failed for: r1v16 */
    /* JADX WARNING: type inference failed for: r1v17 */
    /* JADX WARNING: type inference failed for: r1v18 */
    /* JADX WARNING: type inference failed for: r1v19 */
    /* JADX WARNING: type inference failed for: r1v20 */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x008b, code lost:
        r6.append(r7);
        r6.append(r3);
        r6.append(r5);
        r6.append(r0);
        com.android.camera.log.Log.d(r4, r6.toString());
        r1 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x00b2, code lost:
        r6.append(r7);
        r6.append(r3);
        r6.append(r5);
        r6.append(r0);
        com.android.camera.log.Log.d(r1, r6.toString());
        r1 = com.android.camera.R.string.dolly_zoom_capture_tip1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00d1, code lost:
        r8.mLastDollyZoomState = r2;
        r2 = r8.mLastDollyZoomState;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00d6, code lost:
        if (r2 == 4) goto L_0x00e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00d9, code lost:
        if (r2 == -2) goto L_0x00e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00dc, code lost:
        if (r2 != 7) goto L_0x00df;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00e0, code lost:
        if (r2 != 5) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00e2, code lost:
        r2 = com.android.camera.statistic.MistatsConstants.FilmAttr.VALUE_DOLLY_ZOOM_STATE_FRAMER_OUT;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00e5, code lost:
        r2 = com.android.camera.statistic.MistatsConstants.FilmAttr.VALUE_DOLLY_ZOOM_STATE_TARGET_LOST;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00e7, code lost:
        com.android.camera.statistic.CameraStatUtils.trackDollyZoomClick(r2);
        r1 = r1;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v7
  assigns: []
  uses: []
  mth insns count: 88
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
    /* JADX WARNING: Unknown variable types count: 5 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onDollyZoomState() {
        ? r1;
        String str;
        StringBuilder sb;
        String str2;
        String str3;
        StringBuilder sb2;
        String str4;
        MediaEffectCamera mediaEffectCamera = this.mDollyZoomCamera;
        if (mediaEffectCamera == null) {
            Log.w(TAG, "mDollyZoomCamera is null, onDollyZoomState fail");
            return;
        }
        ? r12 = -1;
        int GetNowState = mediaEffectCamera.GetNowState();
        float GetNowScale = (float) mediaEffectCamera.GetNowScale();
        int GetEncoderState = mediaEffectCamera.GetEncoderState();
        if (this.mLastDollyZoomState != GetNowState) {
            String str5 = " recordingState:";
            switch (GetNowState) {
                case -2:
                    str = TAG;
                    sb = new StringBuilder();
                    str2 = "onDollyZoomState RUN_STATE_FAILED zoomScale:";
                    break;
                case 0:
                    str4 = TAG;
                    sb2 = new StringBuilder();
                    str3 = "onDollyZoomState RUN_STATE_WAITING zoomScale:";
                    break;
                case 1:
                    str4 = TAG;
                    sb2 = new StringBuilder();
                    str3 = "onDollyZoomState RUN_STATE_INITIALIZED zoomScale:";
                    break;
                case 2:
                    str = TAG;
                    sb = new StringBuilder();
                    str2 = "onDollyZoomState RUN_STATE_RUNNING zoomScale:";
                    r12 = 2131755641;
                    break;
                case 3:
                    str4 = TAG;
                    sb2 = new StringBuilder();
                    str3 = "onDollyZoomState RUN_STATE_NORMAL_END zoomScale:";
                    break;
                case 4:
                    str = TAG;
                    sb = new StringBuilder();
                    str2 = "onDollyZoomState RUN_STATE_BAD_ALGO_RESLUT zoomScale:";
                    r12 = 2131755642;
                    break;
                case 5:
                    str = TAG;
                    sb = new StringBuilder();
                    str2 = "onDollyZoomState RUN_STATE_MOVE_OUT_FRAME zoomScale:";
                    r12 = 2131755643;
                    break;
                case 6:
                    str4 = TAG;
                    sb2 = new StringBuilder();
                    str3 = "onDollyZoomState RUN_STATE_MOVE_OUT_ZOOM zoomScale:";
                    break;
                case 7:
                    str = TAG;
                    sb = new StringBuilder();
                    str2 = "onDollyZoomState RUN_STATE_EARLY_STOP_BY_ALGO zoomScale:";
                    r12 = 2131755526;
                    break;
                default:
                    String str6 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("onDollyZoomState unknown state:");
                    sb3.append(GetNowState);
                    sb3.append(str5);
                    sb3.append(GetEncoderState);
                    Log.w(str6, sb3.toString());
                    r1 = r12;
                    break;
            }
        }
        this.mHandler.post(new O000OOo0(this, GetEncoderState, GetNowScale, r1));
    }

    /* access modifiers changed from: private */
    public void onVideoSaveFinish() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSaveFinish ");
        sb.append(this.mVideoFilePath);
        Log.d(str, sb.toString());
        String name = new File(this.mVideoFilePath).getName();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(name);
        sb2.append(Util.convertOutputFormatToFileExt(2));
        String sb3 = sb2.toString();
        String convertOutputFormatToMimeType = Util.convertOutputFormatToMimeType(2);
        ContentValues contentValues = new ContentValues(8);
        contentValues.put("title", name);
        contentValues.put("_display_name", sb3);
        contentValues.put("mime_type", convertOutputFormatToMimeType);
        contentValues.put("_data", this.mVideoFilePath);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mPreviewSize.width);
        sb4.append("x");
        sb4.append(this.mPreviewSize.height);
        contentValues.put("resolution", sb4.toString());
        Location currentLocation = LocationManager.instance().getCurrentLocation();
        if (!(currentLocation == null || (currentLocation.getLatitude() == 0.0d && currentLocation.getLongitude() == 0.0d))) {
            contentValues.put("latitude", Double.valueOf(currentLocation.getLatitude()));
            contentValues.put("longitude", Double.valueOf(currentLocation.getLongitude()));
        }
        DollyZoomProcess dollyZoomProcess = (DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676);
        if (dollyZoomProcess != null) {
            dollyZoomProcess.onPreviewPrepare(contentValues);
        }
        this.mActivity.getImageSaver().addVideo(this.mVideoFilePath, contentValues, true);
    }

    private void previewWhenSessionSuccess() {
        setCameraState(1);
        updatePreferenceInWorkThread(UpdateConstant.MI_LIVE_TYPES_ON_PREVIEW_SUCCESS);
    }

    private void resetAndUnlock3A() {
        Log.d(TAG, "resetAndUnlock3A");
        this.mIsRecording = false;
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

    private void resumePreviewIfNeeded() {
        Log.d(TAG, "resumePreviewIfNeeded");
        this.mIsFinished = false;
        resumePreview();
    }

    /* JADX INFO: finally extract failed */
    private void saveVideo() {
        boolean z;
        String format = new SimpleDateFormat(getString(R.string.video_file_name_format), Locale.ENGLISH).format(new Date(System.currentTimeMillis()));
        StringBuilder sb = new StringBuilder();
        sb.append(format);
        sb.append(Util.convertOutputFormatToFileExt(2));
        this.mVideoFileName = sb.toString();
        this.mVideoFilePath = Storage.generateFilepath(this.mVideoFileName);
        File file = new File(TEMP_VIDEO_PATH);
        File file2 = new File(this.mVideoFilePath);
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        Log.d(TAG, "saveVideo start");
        try {
            z = file.renameTo(file2);
            if (!z) {
                file.delete();
                file2.delete();
            }
        } catch (Throwable th) {
            file.delete();
            file2.delete();
            throw th;
        }
        String str = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("saveVideo done, success=");
        sb2.append(z);
        sb2.append(" cost=");
        sb2.append(System.currentTimeMillis() - valueOf.longValue());
        Log.d(str, sb2.toString());
        onVideoSaveFinish();
        Log.d(TAG, "prepare save video");
    }

    private void setOrientation(int i, int i2) {
        if (i != -1) {
            if (this.mOrientation != i) {
                MediaEffectCamera mediaEffectCamera = this.mDollyZoomCamera;
                if (mediaEffectCamera != null) {
                    int i3 = (i + 90) % m.cQ;
                    mediaEffectCamera.SetRotation(i3);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("DollyZoomCamera SetRotation orientation:");
                    sb.append(i);
                    sb.append(" rotation:");
                    sb.append(i3);
                    Log.d(str, sb.toString());
                }
            }
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
                CameraSchedulers.sCameraSetupScheduler.scheduleDirect(new O000OOo(this));
            } else {
                updatePreferenceInWorkThread(35);
            }
        }
    }

    private void startPreviewSession() {
        Camera2Proxy camera2Proxy = this.mCamera2Device;
        if (camera2Proxy == null) {
            Log.e(TAG, "startPreview: camera has been closed");
            return;
        }
        camera2Proxy.setDualCamWaterMarkEnable(false);
        this.mCamera2Device.setErrorCallback(this.mErrorCallback);
        this.mCamera2Device.setPreviewSize(this.mPreviewSize);
        this.mCamera2Device.setAlgorithmPreviewSize(this.mAlgorithmPreviewSize);
        this.mCamera2Device.startFaceDetection();
        this.mSurfaceCreatedTimestamp = this.mActivity.getCameraScreenNail().getSurfaceCreatedTimestamp();
        this.mCamera2Device.startPreviewSession(new Surface(this.mActivity.getCameraScreenNail().getSurfaceTexture()), 1, 0, null, getOperatingMode(), false, this);
    }

    private void startRecordingCount() {
        Log.d(TAG, "startRecordingCount");
        Timer timer = this.mCountTimer;
        if (timer != null) {
            timer.cancel();
            this.mCountTimer = null;
        }
        this.mCountTimer = new Timer();
        AnonymousClass1 r2 = new TimerTask() {
            static /* synthetic */ void O00000oo(long j) {
                String access$300 = DollyZoomModule.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startRecordingCount duration:");
                sb.append(j);
                Log.d(access$300, sb.toString());
                String millisecondToTimeString = Util.millisecondToTimeString(j, false);
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.updateRecordingTime(millisecondToTimeString);
                }
            }

            public void run() {
                DollyZoomModule.this.mHandler.post(new O000OO(System.currentTimeMillis() - DollyZoomModule.this.mRecordingStartTime));
            }
        };
        this.mRecordingStartTime = System.currentTimeMillis();
        this.mCountTimer.schedule(r2, 0, 1000);
    }

    private void startVideoRecording() {
        Log.d(TAG, "startVideoRecording");
        playCameraSound(2);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        topAlert.updateRecordingTime(Util.millisecondToTimeString(0, false));
        topAlert.setRecordingTimeState(1);
        DollyZoomProcess dollyZoomProcess = (DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676);
        dollyZoomProcess.processingPrepare();
        dollyZoomProcess.processingStart();
        synchronized (this.mLock) {
            if (this.mDollyZoomCamera != null) {
                this.mDollyZoomCamera.StartRecording();
            }
        }
        startRecordingCount();
    }

    private void stopVideoRecording() {
        Log.d(TAG, "stopVideoRecording");
        playCameraSound(3);
        cancelRecordingCount();
        DollyZoomProcess dollyZoomProcess = (DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676);
        if (dollyZoomProcess != null) {
            dollyZoomProcess.processingFinish();
        }
        synchronized (this.mLock) {
            if (this.mDollyZoomCamera != null) {
                this.mDollyZoomCamera.StopRecording();
            }
        }
        this.mIsVideoSaveCancel = System.currentTimeMillis() - this.mRecordingStartTime < 500;
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
        setFocusMode(this.mFocusManager.setFocusMode("manual"));
        this.mCamera2Device.setFocusDistance((this.mCameraCapabilities.getMinimumFocusDistance() * ((float) 70)) / 1000.0f);
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
        this.mAlgorithmPreviewSize = new CameraSize(3840, 2160);
        this.mPictureSize = null;
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("updatePictureAndPreviewSize mAlgorithmPreviewSize ");
        sb2.append(this.mAlgorithmPreviewSize);
        Log.d(str2, sb2.toString());
        CameraSize cameraSize = this.mPreviewSize;
        updateCameraScreenNailSize(cameraSize.width, cameraSize.height);
    }

    private void updateVideoStabilization() {
        if (isDeviceAlive()) {
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                camera2Proxy.setEnableEIS(true);
                this.mCamera2Device.setEnableOIS(false);
                Log.d(TAG, "updateVideoStabilization EIS enable: true");
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0112  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:68:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void O000000o(int i, float f, int i2) {
        boolean z;
        boolean z2;
        boolean z3;
        int i3 = i;
        float f2 = f;
        int i4 = i2;
        DollyZoomProcess dollyZoomProcess = (DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676);
        if (dollyZoomProcess != null && dollyZoomProcess.isAdded()) {
            int i5 = this.mLastDollyZoomState;
            if (i5 == 0) {
                if (i3 == 2) {
                    this.mIsVideoSaved = true;
                    Log.w(TAG, "onDollyZoomState record video failed");
                }
            } else if (i5 == 4 || i5 == 5 || i5 == 6 || i5 == 7) {
                if (f2 > 2.0f) {
                    cancelRecordingCount();
                    z2 = false;
                    z = false;
                    z3 = true;
                } else {
                    if (this.mIsRecording) {
                        CameraStatUtils.trackDollyZoomZoomValue(true, f2);
                        cancelRecordingCount();
                        dollyZoomProcess.prepare(true);
                    }
                    this.mIsRecording = false;
                    this.mIsVideoSaved = true;
                    if (i4 != -1) {
                        this.mHandler.postDelayed(new C0362O000Oo0O(this, dollyZoomProcess), 3000);
                        z3 = false;
                        z2 = true;
                        z = true;
                    }
                }
                if (z3 && this.mLastRecordingState != i3) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDollyZoomState mLastRecordingState:");
                    sb.append(this.mLastRecordingState);
                    sb.append(" GetEncoderState:");
                    sb.append(i3);
                    Log.d(str, sb.toString());
                    if (this.mLastRecordingState == 0) {
                        int i6 = this.mLastDollyZoomState;
                        boolean z4 = (i6 == 3 || i6 == 2) ? false : true;
                        CameraStatUtils.trackDollyZoomZoomValue(z4, f2);
                        if (i3 == 1) {
                            if (this.mIsVideoSaveCancel) {
                                dollyZoomProcess.prepare(false);
                            } else {
                                dollyZoomProcess.stopCaptureToPreviewResult(false);
                                z2 = false;
                            }
                            String str2 = TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("onDollyZoomState mIsVideoSaveCancel:");
                            sb2.append(this.mIsVideoSaveCancel);
                            Log.w(str2, sb2.toString());
                        }
                        this.mIsRecording = false;
                        this.mIsVideoSaved = true;
                    } else if (i3 == 2) {
                        CameraStatUtils.trackDollyZoomZoomValue(true, f2);
                        dollyZoomProcess.prepare(false);
                        dollyZoomProcess.updateCaptureMessage(R.string.dolly_zoom_capture_tip5, true);
                        this.mHandler.postDelayed(new C0363O000Oo0o(dollyZoomProcess), 3000);
                        this.mIsRecording = false;
                        this.mIsVideoSaved = true;
                        z2 = false;
                    }
                }
                this.mLastRecordingState = i3;
                if (i4 != -1 && z2) {
                    if (i4 != R.string.dolly_zoom_capture_tip1) {
                        this.mResetCaptureHintCont++;
                        if (i4 == R.string.dolly_zoom_capture_tip2) {
                            this.mHandler.postDelayed(new O000Oo0(this, dollyZoomProcess), 3000);
                        }
                    }
                    dollyZoomProcess.updateCaptureMessage(i4, z);
                }
                if (this.mLastDollyZoomState == 2) {
                    dollyZoomProcess.updateZoomRatioHint(f2);
                    return;
                }
                return;
            } else if (i5 == 3 || i5 == 2) {
                if (this.mLastDollyZoomState == 3 && f2 > 4.0f) {
                    cancelRecordingCount();
                }
                z = false;
                z3 = true;
                z2 = true;
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("onDollyZoomState mLastRecordingState:");
                sb3.append(this.mLastRecordingState);
                sb3.append(" GetEncoderState:");
                sb3.append(i3);
                Log.d(str3, sb3.toString());
                if (this.mLastRecordingState == 0) {
                }
                this.mLastRecordingState = i3;
                if (i4 != R.string.dolly_zoom_capture_tip1) {
                }
                dollyZoomProcess.updateCaptureMessage(i4, z);
                if (this.mLastDollyZoomState == 2) {
                }
            }
            z3 = false;
            z = false;
            z2 = true;
            String str32 = TAG;
            StringBuilder sb32 = new StringBuilder();
            sb32.append("onDollyZoomState mLastRecordingState:");
            sb32.append(this.mLastRecordingState);
            sb32.append(" GetEncoderState:");
            sb32.append(i3);
            Log.d(str32, sb32.toString());
            if (this.mLastRecordingState == 0) {
            }
            this.mLastRecordingState = i3;
            if (i4 != R.string.dolly_zoom_capture_tip1) {
            }
            dollyZoomProcess.updateCaptureMessage(i4, z);
            if (this.mLastDollyZoomState == 2) {
            }
        }
    }

    public /* synthetic */ void O000000o(int i, int i2, int i3, int i4) {
        synchronized (this.mLock) {
            this.mDollyZoomCamera = new MediaEffectCamera();
            this.mDollyZoomCamera.ConstructMediaEffectCamera(this.mAlgorithmPreviewSize.width, this.mAlgorithmPreviewSize.height, i);
            this.mDollyZoomCamera.SetFilmSizeState(i2);
            this.mDollyZoomCamera.SetEncodeType(i3);
            this.mDollyZoomCamera.SetSavePath(TEMP_VIDEO_PATH);
            this.mDollyZoomCamera.SetRotation(i4);
        }
    }

    public /* synthetic */ void O000000o(DollyZoomProcess dollyZoomProcess) {
        int i = this.mResetCaptureHintCont - 1;
        this.mResetCaptureHintCont = i;
        this.mResetCaptureHintCont = i < 0 ? 0 : this.mResetCaptureHintCont;
        if (this.mResetCaptureHintCont == 0) {
            dollyZoomProcess.updateCaptureMessage(R.string.dolly_zoom_capture_tip1, false);
        }
    }

    public /* synthetic */ void O000000o(MediaEffectCamera mediaEffectCamera) {
        synchronized (this.mLock) {
            mediaEffectCamera.DestructRender();
            mediaEffectCamera.DestructMediaEffectCamera();
            this.mInitRender = false;
        }
    }

    public /* synthetic */ void O00000o0(DollyZoomProcess dollyZoomProcess) {
        int i = this.mResetCaptureHintCont - 1;
        this.mResetCaptureHintCont = i;
        this.mResetCaptureHintCont = i < 0 ? 0 : this.mResetCaptureHintCont;
        if (this.mResetCaptureHintCont == 0) {
            dollyZoomProcess.updateCaptureMessage(-1, false);
        }
    }

    public /* synthetic */ void O00oo00o() {
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
            } else if (i == 31) {
                updateVideoStabilization();
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

    public void fillFeatureControl(StartControl startControl) {
        StartControlFeatureDetail featureDetail = startControl.getFeatureDetail();
        featureDetail.addFragmentInfo(R.id.full_screen_feature, BaseFragmentDelegate.FRAGMENT_DOLLY_ZOOM_PROCESS);
        featureDetail.hideFragment(R.id.bottom_action);
        featureDetail.hideFragment(R.id.bottom_popup_tips);
        featureDetail.hideFragment(R.id.bottom_popup_dual_camera_adjust);
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return CameraCapabilities.SESSION_OPERATION_MODE_DOLLY_ZOOM;
    }

    public String getTag() {
        return TAG;
    }

    public void initializeCapabilities() {
        super.initializeCapabilities();
        this.mContinuousFocusSupported = Util.isSupported(4, this.mCameraCapabilities.getSupportedFocusModes());
        this.mMaxFaceCount = this.mCameraCapabilities.getMaxFaceCount();
    }

    public boolean isDoingAction() {
        return getCameraState() == 3;
    }

    public boolean isProcessorReady() {
        return isFrameAvailable();
    }

    public boolean isRecording() {
        return this.mIsRecording;
    }

    public boolean isSelectingCapturedResult() {
        return false;
    }

    public boolean isUnInterruptable() {
        return false;
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

    public boolean onBackPressed() {
        DollyZoomProcess dollyZoomProcess = (DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676);
        if (this.mIsRecording) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
                this.mLastBackPressedTime = currentTimeMillis;
                ToastUtils.showToast((Context) this.mActivity, (int) R.string.record_back_pressed_hint, true);
            } else {
                stopVideoRecording();
            }
            return true;
        } else if (dollyZoomProcess == null) {
            return super.onBackPressed();
        } else {
            dollyZoomProcess.onBackPressed();
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
        startPreviewSession();
        this.mOnResumeTime = SystemClock.uptimeMillis();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
    }

    public void onCancelClicked() {
        resetAndUnlock3A();
    }

    public void onCreate(int i, int i2) {
        super.onCreate(i, i2);
        this.mHandler = new MainHandler(this.mActivity.getMainLooper());
        onCameraOpened();
        this.mHandler.sendEmptyMessage(4);
        this.mHandler.sendEmptyMessage(31);
        this.mActivity.getSensorStateManager().setRotationVectorEnabled(true);
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        this.mHandler.post(O000OOOo.INSTANCE);
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessage(45);
        }
        this.mActivity.getSensorStateManager().setRotationVectorEnabled(false);
        this.mActivity.getGLView().setPendingChange(true);
        if (this.mDollyZoomCamera != null) {
            Log.d(TAG, "onDestroy mDollyZoomCamera will be destructed");
            this.mActivity.getGLView().queueEvent(new C0360O000OOoo(this, this.mDollyZoomCamera));
            this.mDollyZoomCamera = null;
        }
    }

    public void onDrawFrame(Rect rect, int i, int i2, boolean z) {
        MediaEffectCamera mediaEffectCamera = this.mDollyZoomCamera;
        if (mediaEffectCamera == null) {
            Log.w(TAG, "mDollyZoomCamera is null, PushExtraYAndUVFrame fail");
            return;
        }
        if (!this.mInitRender) {
            mediaEffectCamera.ConstructRender();
            MediaEffectCamera mediaEffectCamera2 = this.mDollyZoomCamera;
            Rect rect2 = this.mRenderRect;
            mediaEffectCamera2.InitRender(rect2.left, rect2.top, rect2.right, rect2.bottom);
            this.mInitRender = true;
        }
        this.mDollyZoomCamera.RenderFrame();
    }

    public void onExitClicked() {
        Log.d(TAG, "onExitClicked");
        cancelRecordingCount();
    }

    public void onFragmentResume() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onFragmentResume, cameraState = ");
        sb.append(getCameraState());
        Log.d(str, sb.toString());
        if (this.mPendingStart) {
            this.mPendingStart = false;
        }
        if (getCameraState() == 0) {
            resumePreview();
        }
    }

    public void onGiveUpClicked() {
        Log.d(TAG, "onGiveUpClicked");
        resetAndUnlock3A();
    }

    public void onGuideClicked() {
        Log.d(TAG, "onGuideClicked");
        this.mPendingStart = true;
    }

    public void onHostStopAndNotifyActionStop() {
        super.onHostStopAndNotifyActionStop();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onHostStopAndNotifyActionStop ");
        sb.append(this.mIsFinished);
        Log.d(str, sb.toString());
        if (this.mIsRecording) {
            this.mIsRecording = false;
            stopVideoRecording();
        }
        resumePreviewIfNeeded();
        doLaterReleaseIfNeed();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        if (r6 != 88) goto L_0x0066;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean z = false;
        if (!isFrameAvailable()) {
            return false;
        }
        DollyZoomProcess dollyZoomProcess = (DollyZoomProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(676);
        if (dollyZoomProcess == null || !dollyZoomProcess.canSnap()) {
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
                handler.post(new C0359O000OOoO(uri));
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

    public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
        try {
            if (this.mDollyZoomCamera == null) {
                Log.w(TAG, "mDollyZoomCamera is null, PushExtraYAndUVFrame fail");
                return true;
            }
            synchronized (this.mLock) {
                if (this.mInitRender) {
                    this.mDollyZoomCamera.PushExtraYAndUVFrame(image);
                    onDollyZoomState();
                }
            }
            this.mActivity.getGLView().requestRender();
            return true;
        } catch (IllegalStateException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("addPreviewFrame fail, ");
            sb.append(e.getMessage());
            Log.w(str, sb.toString());
        }
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

    public void onResume() {
        super.onResume();
        keepScreenOnAwhile();
    }

    public void onReviewCancelClicked() {
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges == null) {
            Log.w(TAG, "onReviewDoneClicked return, configChanges is null");
        } else {
            configChanges.configFilm(null, false, false);
        }
    }

    public void onReviewDoneClicked() {
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges == null) {
            Log.w(TAG, "onReviewDoneClicked return, configChanges is null");
        } else {
            configChanges.configFilm(null, false, false);
        }
    }

    public void onSaveClicked() {
        saveVideo();
    }

    public void onShutterButtonClick(int i) {
        if (checkShutterCondition()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (i == 110) {
                this.mActivity.onUserInteraction();
                if (topAlert != null) {
                    topAlert.hideRecommendDescTip(FragmentTopAlert.TIP_SPEECH_SHUTTER_DESC);
                }
            }
            setTriggerMode(i);
            if (!this.mIsRecording) {
                CameraStatUtils.trackDollyZoomClick(FilmAttr.VALUE_DOLLY_ZOOM_CLICK_START_RECORD);
                if (topAlert != null) {
                    topAlert.disableMenuItem(true, 179);
                    topAlert.disableMenuItem(true, 251);
                    topAlert.disableMenuItem(true, 217);
                }
                Log.u(TAG, "onShutterButtonClick startVideoRecording");
                startVideoRecording();
            } else {
                Log.u(TAG, "onShutterButtonClick stopVideoRecording");
                this.mIsVideoSaved = false;
                stopVideoRecording();
            }
            Camera2Proxy camera2Proxy = this.mCamera2Device;
            if (camera2Proxy != null) {
                if (this.mAeLockSupported) {
                    camera2Proxy.setAELock(!this.mIsRecording);
                }
                if (this.mAwbLockSupported) {
                    this.mCamera2Device.setAWBLock(!this.mIsRecording);
                }
                this.mCamera2Device.resumePreview();
            }
            this.mIsRecording = !this.mIsRecording;
        }
    }

    public void onShutterButtonFocus(boolean z, int i) {
    }

    public boolean onShutterButtonLongClick() {
        return false;
    }

    public void onShutterButtonLongClickCancel(boolean z) {
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
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        Rect displayRect = Util.getDisplayRect(1);
        this.mRenderRect = new Rect();
        Rect rect = this.mRenderRect;
        rect.right = i;
        rect.bottom = (int) (((float) i) * 1.7777778f);
        rect.left = 0;
        rect.top = (i2 - rect.bottom) - displayRect.top;
        this.mInitRender = false;
        Log.d(TAG, "onSurfaceChanged");
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
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
        ModeCoordinatorImpl.getInstance().attachProtocol(677, this);
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

    public boolean shouldCaptureDirectly() {
        return false;
    }

    public boolean shouldReleaseLater() {
        return this.mIsRecording || this.mIsFinished;
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

    public void unRegisterModulePersistProtocol() {
        super.unRegisterModulePersistProtocol();
        Log.d(TAG, "unRegisterModulePersistProtocol");
        getActivity().getImplFactory().detachModulePersistent();
    }

    public void unRegisterProtocol() {
        super.unRegisterProtocol();
        ModeCoordinatorImpl.getInstance().detachProtocol(161, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(169, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(677, this);
        getActivity().getImplFactory().detachAdditional();
    }
}
