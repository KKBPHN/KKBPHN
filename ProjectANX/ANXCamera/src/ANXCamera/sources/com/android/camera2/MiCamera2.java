package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCaptureSession.StateCallback;
import android.hardware.camera2.CameraConstrainedHighSpeedCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.utils.SurfaceUtils;
import android.location.Location;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.ImageWriter;
import android.media.ImageWriter.OnImageReleasedListener;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.LocalParallelService.LocalBinder;
import com.android.camera.MemoryHelper;
import com.android.camera.PreviewMetadata;
import com.android.camera.SurfaceTextureScreenNail.PreviewSaveListener;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.module.loader.camera2.ParallelSnapshotManager;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.storage.ImageSaver;
import com.android.camera2.Camera2Proxy.CameraMetaDataCallback;
import com.android.camera2.Camera2Proxy.CameraPreviewCallback;
import com.android.camera2.Camera2Proxy.CaptureBusyCallback;
import com.android.camera2.Camera2Proxy.FocusCallback;
import com.android.camera2.Camera2Proxy.HDRStatus;
import com.android.camera2.Camera2Proxy.IFirstCaptureFocus;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.Camera2Proxy.PreviewCallback;
import com.android.camera2.Camera2Proxy.ScreenLightCallback;
import com.android.camera2.compat.MiCameraCompat;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTag;
import com.android.camera2.vendortag.VendorTagHelper;
import com.android.camera2.vendortag.struct.AWBFrameControl;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import com.android.zxing.CacheImageDecoder;
import com.xiaomi.camera.core.ParallelCallback;
import com.xiaomi.camera.device.CameraService;
import com.xiaomi.camera.device.callable.ShotBoostCallable;
import com.xiaomi.protocol.IImageReaderParameterSets;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import miui.text.ExtraTextUtils;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

@TargetApi(21)
public class MiCamera2 extends Camera2Proxy {
    private static final int DEF_QUICK_SHOT_THRESHOLD_INTERVAL_TIME = 50;
    private static final int DEF_QUICK_SHOT_THRESHOLD_SHOT_CACHE_TIME_OUT = 10000;
    private static final boolean FLASH_LOCK_DEBUG = SystemProperties.getBoolean("flash_time_lock", false);
    private static final int MSG_CHECK_CAMERA_ALIVE = 3;
    private static final int MSG_WAITING_AF_LOCK_TIMEOUT = 1;
    private static final int MSG_WAITING_LOCAL_PARALLEL_SERVICE_READY = 2;
    private static final int PARALLEL_SURFACE_INDEX_UNSET = -1;
    /* access modifiers changed from: private */
    public static final String TAG = "MiCamera2";
    private static final long TIME_WAITING_LOCK_AF_FLASH = 4000;
    private static final long TIME_WAITING_LOCK_AF_TORCH = 3000;
    static final MeteringRectangle[] ZERO_WEIGHT_3A_REGION;
    private final int AE_STATE_NULL = -1;
    private final int DEF_QUICK_SHOT_THRESHOLD_SHOT_CACHE_COUNT;
    private final int MAX_PARALLEL_REQUEST_NUMBER = 5;
    /* access modifiers changed from: private */
    public final boolean WAITING_AE_STATE_STRICT = C0124O00000oO.isMTKPlatform();
    private CameraPreviewCallback mCameraCloseCallback;
    private CameraDevice mCameraDevice;
    private Handler mCameraHandler;
    private Handler mCameraPreviewHandler;
    private final PreviewMetadata mCameraPreviewMetadata;
    /* access modifiers changed from: private */
    public final CameraCapabilities mCapabilities;
    private CaptureBusyCallback mCaptureBusyCallback = null;
    private PictureCaptureCallback mCaptureCallback;
    /* access modifiers changed from: private */
    public CameraCaptureSession mCaptureSession;
    private CaptureSessionStateCallback mCaptureSessionStateCallback;
    private long mCaptureTime = 0;
    /* access modifiers changed from: private */
    public CameraConfigs mConfigs;
    private long mCurrentFrameNum = -1;
    /* access modifiers changed from: private */
    public List mDeferOutputConfigurations = new ArrayList();
    /* access modifiers changed from: private */
    public Surface mDeferPreviewSurface;
    private ImageReader mDepthReader;
    private int mDisplayOrientation;
    private boolean mEnableParallelSession;
    private SurfaceTexture mFakeOutputTexture;
    private int mFakeTeleParallelSurfaceIndex = -1;
    /* access modifiers changed from: private */
    public int mFocusLockRequestHashCode;
    /* access modifiers changed from: private */
    public long mFrameCount = 0;
    /* access modifiers changed from: private */
    public long mFrameCountingStart = 0;
    /* access modifiers changed from: private */
    public Handler mHelperHandler;
    private Range mHighSpeedFpsRange;
    private volatile boolean mIsCameraClosed;
    /* access modifiers changed from: private */
    public volatile boolean mIsCaptureSessionClosed;
    private boolean mIsPreviewCallbackStarted;
    private long mLastFlashTimeMillis;
    private long mLastFrameNum = -1;
    private int mLastSatCameraId = -1;
    private int mMacroParallelSurfaceIndex = -1;
    private int mMaxImageBufferSize = 10;
    /* access modifiers changed from: private */
    public MiCamera2Shot mMiCamera2Shot;
    /* access modifiers changed from: private */
    public ConcurrentLinkedDeque mMiCamera2ShotQueue = new ConcurrentLinkedDeque();
    private boolean mNeedFlashForAuto;
    private List mParallelCaptureSurfaceList;
    /* access modifiers changed from: private */
    public volatile boolean mPendingNotifyVideoEnd;
    private ImageReader mPhotoImageReader;
    private ImageReader mPortraitRawImageReader;
    /* access modifiers changed from: private */
    public int mPreCaptureRequestHashCode;
    private int mPreviewCallbackType;
    /* access modifiers changed from: private */
    public MiCamera2Preview mPreviewControl;
    private ImageReader mPreviewImageReader;
    private CaptureRequest mPreviewRequest;
    /* access modifiers changed from: private */
    public Builder mPreviewRequestBuilder;
    /* access modifiers changed from: private */
    public Surface mPreviewSurface;
    private int mQcfaParallelSurfaceIndex = -1;
    private int mRawCallbackType;
    private ImageReader mRawImageReader;
    private ImageWriter mRawImageWriter;
    private int mRawSurfaceIndex = -1;
    private int mRawSurfaceIndexForTuningBuffer = -1;
    private Surface mRecordSurface;
    private List mRemoteImageReaderList = new ArrayList();
    private int mSATRemosicParallelSurfaceIndex = -1;
    private int mScreenLightColorTemperature;
    private CaptureSessionConfigurations mSessionConfigs;
    /* access modifiers changed from: private */
    public int mSessionId;
    /* access modifiers changed from: private */
    public final Object mSessionLock = new Object();
    /* access modifiers changed from: private */
    public boolean mSetRepeatingEarly = C0122O00000o.instance().OO0o0();
    private Consumer mShotBoostParams;
    /* access modifiers changed from: private */
    public final Object mShotQueueLock = new Object();
    private int mSubParallelSurfaceIndex = -1;
    private SuperNightReprocessHandler mSuperNightReprocessHandler;
    private final boolean mSupportFlashTimeLock;
    private int mTeleParallelSurfaceIndex = -1;
    /* access modifiers changed from: private */
    public boolean mToTele;
    private int mTuningBufferSurfaceIndex = -1;
    private int mUltraTeleParallelSurfaceIndex = -1;
    private int mUltraWideParallelSurfaceIndex = -1;
    /* access modifiers changed from: private */
    public int mVideoSessionId;
    private ImageReader mVideoSnapshotImageReader;
    private int mWideParallelSurfaceIndex = -1;
    private Surface mZoomMapSurface;
    private AtomicBoolean mZoomMapSurfaceAdded = new AtomicBoolean(false);

    class CaptureSessionStateCallback extends StateCallback {
        private WeakReference mClientCb;
        private int mId;

        public CaptureSessionStateCallback(int i, CameraPreviewCallback cameraPreviewCallback) {
            this.mId = i;
            this.mClientCb = new WeakReference(cameraPreviewCallback);
        }

        public void onClosed(@NonNull CameraCaptureSession cameraCaptureSession) {
            String access$000 = MiCamera2.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onClosed: id=");
            sb.append(this.mId);
            sb.append(" sessionId=");
            sb.append(MiCamera2.this.mSessionId);
            Log.e(access$000, sb.toString());
            if (this.mId == MiCamera2.this.mSessionId) {
                WeakReference weakReference = this.mClientCb;
                if (weakReference != null) {
                    CameraPreviewCallback cameraPreviewCallback = (CameraPreviewCallback) weakReference.get();
                    if (cameraPreviewCallback != null) {
                        cameraPreviewCallback.onPreviewSessionClosed(cameraCaptureSession);
                    }
                }
            }
        }

        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
            String access$000 = MiCamera2.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onConfigureFailed: id=");
            sb.append(this.mId);
            sb.append(" sessionId=");
            sb.append(MiCamera2.this.mSessionId);
            Log.e(access$000, sb.toString());
            if (this.mId == MiCamera2.this.mSessionId) {
                WeakReference weakReference = this.mClientCb;
                if (weakReference != null) {
                    CameraPreviewCallback cameraPreviewCallback = (CameraPreviewCallback) weakReference.get();
                    if (cameraPreviewCallback != null) {
                        cameraPreviewCallback.onPreviewSessionFailed(cameraCaptureSession);
                    }
                }
            }
        }

        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            boolean isEmpty;
            if (this.mId == MiCamera2.this.mSessionId) {
                synchronized (MiCamera2.this.mSessionLock) {
                    String access$000 = MiCamera2.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onConfigured: id = ");
                    sb.append(this.mId);
                    sb.append(", session = ");
                    sb.append(cameraCaptureSession);
                    sb.append(", reprocessable = ");
                    sb.append(cameraCaptureSession.isReprocessable());
                    Log.d(access$000, sb.toString());
                    MiCamera2.this.mCaptureSession = cameraCaptureSession;
                    if (MiCamera2.this.mCaptureSession.isReprocessable()) {
                        MiCamera2.this.prepareRawImageWriter(MiCamera2.this.mConfigs.getSensorRawImageSize(), MiCamera2.this.mCaptureSession.getInputSurface());
                    }
                }
                MiCamera2.this.mIsCaptureSessionClosed = false;
                if (MiCamera2.this.mPendingNotifyVideoEnd && this.mId == MiCamera2.this.mVideoSessionId) {
                    MiCamera2.this.notifyVideoStreamEnd();
                    MiCamera2.this.mPendingNotifyVideoEnd = false;
                }
                synchronized (MiCamera2.this.mSessionLock) {
                    isEmpty = MiCamera2.this.mDeferOutputConfigurations.isEmpty();
                    String access$0002 = MiCamera2.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onConfigured: is mDeferOutputConfigurations null: ");
                    sb2.append(isEmpty);
                    Log.d(access$0002, sb2.toString());
                }
                if (isEmpty) {
                    onPreviewSessionSuccess();
                } else {
                    if (MiCamera2.this.mSetRepeatingEarly) {
                        MiCamera2.this.resumePreview();
                    }
                    MiCamera2 miCamera2 = MiCamera2.this;
                    miCamera2.updateDeferPreviewSession(miCamera2.mDeferPreviewSurface);
                }
                if (!MiCamera2.this.mCapabilities.isSupportParallelCameraDevice()) {
                    return;
                }
                if (MiCamera2.this.isIn3OrMoreSatMode() || MiCamera2.this.isInMultiSurfaceSatMode()) {
                    LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder(true);
                    if (localBinder != null) {
                        Log.d(MiCamera2.TAG, "prepareParallelCapture");
                        localBinder.prepareParallelCapture();
                    }
                }
            }
        }

        public void onPreviewSessionSuccess() {
            boolean isEmpty;
            synchronized (MiCamera2.this.mSessionLock) {
                isEmpty = MiCamera2.this.mDeferOutputConfigurations.isEmpty();
            }
            if (isEmpty && this.mId == MiCamera2.this.mSessionId) {
                WeakReference weakReference = this.mClientCb;
                if (weakReference != null) {
                    CameraPreviewCallback cameraPreviewCallback = (CameraPreviewCallback) weakReference.get();
                    if (cameraPreviewCallback != null) {
                        cameraPreviewCallback.onPreviewSessionSuccess(MiCamera2.this.mCaptureSession);
                    }
                }
            }
        }

        public void setClientCb(CameraPreviewCallback cameraPreviewCallback) {
            this.mClientCb = new WeakReference(cameraPreviewCallback);
        }
    }

    class HighSpeedCaptureSessionStateCallback extends StateCallback {
        private final WeakReference mClientCb;
        private final int mId;

        public HighSpeedCaptureSessionStateCallback(int i, CameraPreviewCallback cameraPreviewCallback) {
            this.mId = i;
            this.mClientCb = new WeakReference(cameraPreviewCallback);
        }

        public void onClosed(@NonNull CameraCaptureSession cameraCaptureSession) {
            String access$000 = MiCamera2.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onHighSpeedClosed: ");
            sb.append(cameraCaptureSession);
            Log.d(access$000, sb.toString());
            synchronized (MiCamera2.this.mSessionLock) {
                if (MiCamera2.this.mCaptureSession != null && MiCamera2.this.mCaptureSession.equals(cameraCaptureSession)) {
                    MiCamera2.this.mCaptureSession = null;
                }
            }
            if (this.mClientCb.get() != null) {
                ((CameraPreviewCallback) this.mClientCb.get()).onPreviewSessionClosed(cameraCaptureSession);
            }
        }

        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
            String access$000 = MiCamera2.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onHighSpeedConfigureFailed: id=");
            sb.append(this.mId);
            sb.append(" sessionId=");
            sb.append(MiCamera2.this.mSessionId);
            Log.e(access$000, sb.toString());
            if (this.mClientCb.get() != null) {
                ((CameraPreviewCallback) this.mClientCb.get()).onPreviewSessionFailed(cameraCaptureSession);
            }
        }

        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            if (this.mId == MiCamera2.this.mSessionId) {
                synchronized (MiCamera2.this.mSessionLock) {
                    String access$000 = MiCamera2.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onHighSpeedConfigured: id=");
                    sb.append(this.mId);
                    sb.append(" highSpeedSession=");
                    sb.append(cameraCaptureSession);
                    Log.d(access$000, sb.toString());
                    MiCamera2.this.mCaptureSession = cameraCaptureSession;
                }
                MiCamera2.this.mIsCaptureSessionClosed = false;
                MiCamera2 miCamera2 = MiCamera2.this;
                miCamera2.applySettingsForVideo(miCamera2.mPreviewRequestBuilder);
                MiCameraCompat.applyIsHfrPreview(MiCamera2.this.mPreviewRequestBuilder, true);
                if (this.mClientCb.get() != null) {
                    ((CameraPreviewCallback) this.mClientCb.get()).onPreviewSessionSuccess(cameraCaptureSession);
                }
            }
        }
    }

    class PictureCaptureCallback extends CaptureCallback {
        private boolean mAELockOnlySupported;
        private FocusTask mAutoFocusTask;
        /* access modifiers changed from: private */
        public boolean mFocusAreaSupported;
        private int mLastResultAEState = -1;
        private int mLastResultAFState = -1;
        private FocusTask mManuallyFocusTask;
        private boolean mPartialResultSupported;
        private CaptureResult mPreviewCaptureResult;
        private final Object mPreviewCaptureResultLock = new Object();
        private CaptureResult mPreviewPartialCaptureResult;
        private final Object mPreviewPartialCaptureResultLock = new Object();
        private int mState = 0;
        private final Object mStateLock = new Object();
        private FocusTask mTorchFocusTask;

        PictureCaptureCallback() {
            this.mPartialResultSupported = MiCamera2.this.mCapabilities.isPartialMetadataSupported();
            onCapabilityChanged(MiCamera2.this.mCapabilities);
        }

        private boolean isAeLocked(Integer num) {
            int intValue = num.intValue();
            return intValue == 2 || intValue == 3 || intValue == 4;
        }

        private Boolean isAutoFocusing(Integer num, boolean z) {
            int intValue = num.intValue();
            if (intValue != 0) {
                if (intValue == 1 || intValue == 3) {
                    return Boolean.TRUE;
                }
            } else if (z) {
                IFirstCaptureFocus iFirstCaptureFocus = MiCamera2.this.mFocusFrameAvailable;
                if (iFirstCaptureFocus != null && iFirstCaptureFocus.isFirstCreateCapture()) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }

        private Boolean isFocusLocked(Integer num) {
            int intValue = num.intValue();
            if (intValue == 2 || intValue == 4) {
                return Boolean.TRUE;
            }
            if (intValue == 5 || intValue == 6) {
                return Boolean.FALSE;
            }
            String access$000 = MiCamera2.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("isFocusLocked: ");
            sb.append(num);
            Log.w(access$000, sb.toString());
            return null;
        }

        private void outputFps() {
            long nanoTime = System.nanoTime();
            if (MiCamera2.this.mFrameCountingStart == 0) {
                MiCamera2.this.mFrameCountingStart = nanoTime;
            } else if (nanoTime - MiCamera2.this.mFrameCountingStart > ExtraTextUtils.GB) {
                String access$000 = MiCamera2.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("camhal output fps: ");
                sb.append((((double) MiCamera2.this.mFrameCount) * 1.0E9d) / ((double) (nanoTime - MiCamera2.this.mFrameCountingStart)));
                Log.k(3, access$000, sb.toString());
                MiCamera2.this.mFrameCountingStart = nanoTime;
                MiCamera2.this.mFrameCount = 0;
            }
            MiCamera2.access$3804(MiCamera2.this);
        }

        private void process(@NonNull CaptureResult captureResult) {
            synchronized (this.mPreviewCaptureResultLock) {
                this.mPreviewCaptureResult = captureResult;
            }
            if (HybridZoomingSystem.IS_2_SAT && MiCamera2.this.mToTele && CaptureResultParser.getFastZoomResult(captureResult)) {
                Log.d(MiCamera2.TAG, "process: CaptureResultParser fast zoom...");
                MiCamera2.this.setOpticalZoomToTele(false);
                MiCamera2.this.resumePreview();
            }
            CameraMetaDataCallback metadataCallback = MiCamera2.this.getMetadataCallback();
            int state = getState();
            if (state != 1) {
                switch (state) {
                    case 9:
                        if (MiCamera2.this.mIsCaptureSessionClosed) {
                            Log.w(MiCamera2.TAG, "process: STATE_WAITING_FLASH_CLOSE but capture session is closed");
                            return;
                        }
                        Integer num = (Integer) captureResult.get(CaptureResult.FLASH_STATE);
                        if (num == null || num.intValue() == 2) {
                            setState(8);
                            MiCamera2.this.captureStillPicture();
                            return;
                        }
                        return;
                    case 10:
                        Integer num2 = (Integer) captureResult.get(CaptureResult.FLASH_STATE);
                        if (num2 != null && 3 == num2.intValue()) {
                            MiCamera2.this.triggerPrecapture();
                            return;
                        }
                        return;
                    case 11:
                        Integer num3 = (Integer) captureResult.get(CaptureResult.FLASH_STATE);
                        if (num3 == null || num3.intValue() == 2) {
                            setState(0);
                            MiCamera2.this.pausePreview();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else {
                if (metadataCallback != null) {
                    metadataCallback.onPreviewMetaDataUpdate(captureResult);
                }
                MiCamera2.this.onPreviewMetadata(captureResult);
                if (Util.DEBUG_FPS) {
                    outputFps();
                }
            }
        }

        private void processAeResult(CaptureResult captureResult) {
            Integer num = (Integer) captureResult.get(CaptureResult.CONTROL_AE_STATE);
            if (num != null) {
                FocusCallback focusCallback = MiCamera2.this.getFocusCallback();
                if (focusCallback != null && this.mManuallyFocusTask != null) {
                    Log.d(MiCamera2.TAG, String.format(Locale.ENGLISH, "aeState changed from %s to %s,", new Object[]{Util.controlAEStateToString(Integer.valueOf(this.mLastResultAEState)), Util.controlAEStateToString(num)}));
                    this.mLastResultAEState = num.intValue();
                    if (this.mAutoFocusTask != null) {
                        this.mAutoFocusTask = null;
                    } else if (!this.mManuallyFocusTask.isTaskProcessed()) {
                        String access$000 = MiCamera2.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("the task's request is not process yet. task=");
                        sb.append(this.mManuallyFocusTask.hashCode());
                        sb.append(", request=");
                        sb.append(captureResult.getRequest().hashCode());
                        Log.d(access$000, sb.toString());
                    } else {
                        if (isAeLocked(num)) {
                            Log.d(MiCamera2.TAG, "AE has been already converged, lock AE");
                            this.mManuallyFocusTask.setResult(true);
                            focusCallback.onFocusStateChanged(this.mManuallyFocusTask);
                            this.mManuallyFocusTask = null;
                        }
                    }
                }
            }
        }

        private void processAfResult(CaptureResult captureResult) {
            Integer num = (Integer) captureResult.get(CaptureResult.CONTROL_AF_STATE);
            if (num != null) {
                IFirstCaptureFocus iFirstCaptureFocus = MiCamera2.this.mFocusFrameAvailable;
                if (iFirstCaptureFocus == null || iFirstCaptureFocus.isFocusFrameAvailable()) {
                    FocusCallback focusCallback = MiCamera2.this.getFocusCallback();
                    if (focusCallback != null && num.intValue() != this.mLastResultAFState) {
                        Log.d(MiCamera2.TAG, String.format(Locale.ENGLISH, "processAfResult: afState changed from %d to %d", new Object[]{Integer.valueOf(this.mLastResultAFState), Integer.valueOf(num.intValue())}));
                        this.mLastResultAFState = num.intValue();
                        if (this.mManuallyFocusTask == null) {
                            boolean isDepthFocus = CaptureResultParser.isDepthFocus(captureResult, MiCamera2.this.mCapabilities);
                            if (isAutoFocusing(num, isDepthFocus).booleanValue()) {
                                this.mAutoFocusTask = FocusTask.create(2);
                                this.mAutoFocusTask.setIsDepthFocus(isDepthFocus);
                                focusCallback.onFocusStateChanged(this.mAutoFocusTask);
                            } else {
                                Boolean isFocusLocked = isFocusLocked(num);
                                if (isFocusLocked != null) {
                                    FocusTask focusTask = this.mAutoFocusTask;
                                    if (focusTask != null) {
                                        focusTask.setResult(isFocusLocked.booleanValue());
                                        focusCallback.onFocusStateChanged(this.mAutoFocusTask);
                                        this.mAutoFocusTask = null;
                                    }
                                }
                            }
                        } else if (this.mAutoFocusTask != null) {
                            Log.d(MiCamera2.TAG, "reset auto focus task");
                            this.mAutoFocusTask = null;
                        } else {
                            Boolean isFocusLocked2 = isFocusLocked(num);
                            if (isFocusLocked2 != null) {
                                this.mManuallyFocusTask.setResult(isFocusLocked2.booleanValue());
                                focusCallback.onFocusStateChanged(this.mManuallyFocusTask);
                                this.mManuallyFocusTask = null;
                            }
                        }
                    }
                }
            }
        }

        private void processPartial(@NonNull CaptureResult captureResult, @NonNull CaptureRequest captureRequest) {
            String str;
            String str2;
            String access$000;
            String str3;
            String str4;
            String str5;
            synchronized (this.mPreviewPartialCaptureResultLock) {
                this.mPreviewPartialCaptureResult = captureResult;
            }
            FocusTask focusTask = this.mManuallyFocusTask;
            if (focusTask != null) {
                focusTask.processResult(captureResult);
            }
            if (this.mFocusAreaSupported) {
                processAfResult(captureResult);
            } else if (this.mAELockOnlySupported) {
                processAeResult(captureResult);
            }
            int state = getState();
            if (state != 1) {
                if (state == 12) {
                    Integer num = (Integer) captureResult.get(CaptureResult.CONTROL_AF_STATE);
                    Integer num2 = (Integer) captureResult.get(CaptureResult.CONTROL_AE_STATE);
                    Integer num3 = (Integer) captureResult.get(CaptureResult.CONTROL_AWB_STATE);
                    String access$0002 = MiCamera2.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("STATE_WAITING_MF_3A_LOCKED:  AF = ");
                    sb.append(Util.controlAFStateToString(num));
                    Log.d(access$0002, sb.toString());
                    String access$0003 = MiCamera2.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("STATE_WAITING_MF_3A_LOCKED:  AE = ");
                    sb2.append(Util.controlAEStateToString(num2));
                    Log.d(access$0003, sb2.toString());
                    String access$0004 = MiCamera2.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("STATE_WAITING_MF_3A_LOCKED: AWB = ");
                    sb3.append(Util.controlAWBStateToString(num3));
                    Log.d(access$0004, sb3.toString());
                    if ((num2 == null || num2.intValue() == 3) && (num3 == null || num3.intValue() == 3)) {
                        access$000 = MiCamera2.TAG;
                        str3 = "STATE_WAITING_MF_3A_LOCKED: runCaptureSequence()";
                    } else {
                        str2 = MiCamera2.TAG;
                        str = "STATE_WAITING_MF_3A_LOCKED: keep stay in STATE_WAITING_MF_3A_LOCKED";
                        Log.d(str2, str);
                        return;
                    }
                } else if (state == 3) {
                    Integer num4 = (Integer) captureResult.get(CaptureResult.CONTROL_AF_STATE);
                    if (num4 != null) {
                        if (4 == num4.intValue() || 5 == num4.intValue() || 2 == num4.intValue() || 6 == num4.intValue() || (MiCamera2.this.mFocusLockRequestHashCode == captureResult.getRequest().hashCode() && num4.intValue() == 0)) {
                            if (MiCamera2.this.mFocusLockRequestHashCode == captureResult.getRequest().hashCode() || MiCamera2.this.mFocusLockRequestHashCode == 0) {
                                if (MiCamera2.this.mHelperHandler != null) {
                                    MiCamera2.this.mHelperHandler.removeMessages(1);
                                }
                                if (!MiCamera2.this.needOptimizedFlash() && !C0124O00000oO.isMTKPlatform() && !MiCamera2.this.mConfigs.isMFAfAeLock()) {
                                    MiCamera2.this.runPreCaptureSequence();
                                    return;
                                }
                                MiCamera2.this.runCaptureSequence();
                                return;
                            }
                            return;
                        } else if (MiCamera2.this.mFocusLockRequestHashCode == captureResult.getRequest().hashCode()) {
                            MiCamera2.this.mFocusLockRequestHashCode = 0;
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (state != 4) {
                    if (state == 5) {
                        Integer num5 = (Integer) captureResult.get(CaptureResult.CONTROL_AF_STATE);
                        Integer num6 = (Integer) captureResult.get(CaptureResult.CONTROL_AE_STATE);
                        Integer num7 = (Integer) captureResult.get(CaptureResult.CONTROL_AWB_STATE);
                        String access$0005 = MiCamera2.TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("STATE_WAITING_AE_CONVERGED:  AF = ");
                        sb4.append(Util.controlAFStateToString(num5));
                        Log.d(access$0005, sb4.toString());
                        String access$0006 = MiCamera2.TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("STATE_WAITING_AE_CONVERGED:  AE = ");
                        sb5.append(Util.controlAEStateToString(num6));
                        Log.d(access$0006, sb5.toString());
                        String access$0007 = MiCamera2.TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("STATE_WAITING_AE_CONVERGED: AWB = ");
                        sb6.append(Util.controlAWBStateToString(num7));
                        Log.d(access$0007, sb6.toString());
                        if (num6 == null && MiCamera2.this.WAITING_AE_STATE_STRICT) {
                            num6 = Integer.valueOf(-1);
                        }
                        if (num6 != null && num6.intValue() != 2 && num6.intValue() != 4) {
                            str2 = MiCamera2.TAG;
                            str = "STATE_WAITING_AE_CONVERGED: keep stay in STATE_WAITING_AE_CONVERGED";
                        } else if (MiCamera2.this.mCapabilities.isPreCaptureSupportAF() || !MiCamera2.this.mCapabilities.isAutoFocusSupported() || MiCamera2.this.mConfigs.getFocusMode() == 0) {
                            access$000 = MiCamera2.TAG;
                            str3 = "STATE_WAITING_AE_CONVERGED: runCaptureSequence()";
                        } else {
                            Log.d(MiCamera2.TAG, "STATE_WAITING_AE_CONVERGED: lockFocus()");
                            MiCamera2.this.lockFocus();
                            return;
                        }
                    } else if (state == 6) {
                        Integer num8 = (Integer) captureResult.get(CaptureResult.CONTROL_AF_STATE);
                        Integer num9 = (Integer) captureResult.get(CaptureResult.CONTROL_AE_STATE);
                        Integer num10 = (Integer) captureResult.get(CaptureResult.CONTROL_AWB_STATE);
                        String access$0008 = MiCamera2.TAG;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("STATE_WAITING_PRECAPTURE:  AF = ");
                        sb7.append(Util.controlAFStateToString(num8));
                        Log.d(access$0008, sb7.toString());
                        String access$0009 = MiCamera2.TAG;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("STATE_WAITING_PRECAPTURE:  AE = ");
                        sb8.append(Util.controlAEStateToString(num9));
                        Log.d(access$0009, sb8.toString());
                        String access$00010 = MiCamera2.TAG;
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("STATE_WAITING_PRECAPTURE: AWB = ");
                        sb9.append(Util.controlAWBStateToString(num10));
                        Log.d(access$00010, sb9.toString());
                        if (num9 == null && MiCamera2.this.WAITING_AE_STATE_STRICT) {
                            num9 = Integer.valueOf(-1);
                        }
                        if (num9 == null || num9.intValue() == 5 || (num9.intValue() == 4 && !MiCamera2.this.WAITING_AE_STATE_STRICT)) {
                            str5 = MiCamera2.TAG;
                            str4 = "STATE_WAITING_PRECAPTURE: switch to STATE_WAITING_NON_PRECAPTURE(1)";
                        } else if (MiCamera2.this.mPreCaptureRequestHashCode == captureResult.getRequest().hashCode()) {
                            str5 = MiCamera2.TAG;
                            str4 = "STATE_WAITING_PRECAPTURE: switch to STATE_WAITING_NON_PRECAPTURE(2)";
                        } else {
                            str2 = MiCamera2.TAG;
                            str = "STATE_WAITING_PRECAPTURE: keep stay in STATE_WAITING_PRECAPTURE";
                        }
                        Log.d(str5, str4);
                        setState(7);
                        return;
                    } else if (state == 7) {
                        Integer num11 = (Integer) captureResult.get(CaptureResult.CONTROL_AF_STATE);
                        Integer num12 = (Integer) captureResult.get(CaptureResult.CONTROL_AE_STATE);
                        Integer num13 = (Integer) captureResult.get(CaptureResult.CONTROL_AWB_STATE);
                        Boolean bool = (Boolean) captureRequest.get(CaptureRequest.CONTROL_AE_LOCK);
                        String access$00011 = MiCamera2.TAG;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("STATE_WAITING_NON_PRECAPTURE:  aeRequestLock = ");
                        sb10.append(bool);
                        Log.d(access$00011, sb10.toString());
                        String access$00012 = MiCamera2.TAG;
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("STATE_WAITING_NON_PRECAPTURE:  AF = ");
                        sb11.append(Util.controlAFStateToString(num11));
                        Log.d(access$00012, sb11.toString());
                        String access$00013 = MiCamera2.TAG;
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append("STATE_WAITING_NON_PRECAPTURE:  AE = ");
                        sb12.append(Util.controlAEStateToString(num12));
                        Log.d(access$00013, sb12.toString());
                        String access$00014 = MiCamera2.TAG;
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append("STATE_WAITING_NON_PRECAPTURE: AWB = ");
                        sb13.append(Util.controlAWBStateToString(num13));
                        Log.d(access$00014, sb13.toString());
                        if (num12 == null && MiCamera2.this.WAITING_AE_STATE_STRICT) {
                            num12 = Integer.valueOf(5);
                        }
                        if (num12 == null || num12.intValue() != 5) {
                            if (!MiCamera2.this.needOptimizedFlash()) {
                                if (!C0124O00000oO.isMTKPlatform() || MiCamera2.this.needScreenLight()) {
                                    Log.d(MiCamera2.TAG, "STATE_WAITING_NON_PRECAPTURE: lockExposure()");
                                    MiCamera2.this.lockExposure(false);
                                    return;
                                } else if ((num12.intValue() == 2 || num12.intValue() == 4) && bool != null && bool.booleanValue()) {
                                    setState(4);
                                    return;
                                }
                            }
                            setState(5);
                            return;
                        }
                        str2 = MiCamera2.TAG;
                        str = "STATE_WAITING_NON_PRECAPTURE: keep stay in STATE_WAITING_NON_PRECAPTURE";
                    } else {
                        return;
                    }
                    Log.d(str2, str);
                    return;
                } else {
                    Integer num14 = (Integer) captureResult.get(CaptureResult.CONTROL_AF_STATE);
                    Integer num15 = (Integer) captureResult.get(CaptureResult.CONTROL_AE_STATE);
                    Integer num16 = (Integer) captureResult.get(CaptureResult.CONTROL_AWB_STATE);
                    String access$00015 = MiCamera2.TAG;
                    StringBuilder sb14 = new StringBuilder();
                    sb14.append("STATE_WAITING_AE_LOCK:  AF = ");
                    sb14.append(Util.controlAFStateToString(num14));
                    Log.d(access$00015, sb14.toString());
                    String access$00016 = MiCamera2.TAG;
                    StringBuilder sb15 = new StringBuilder();
                    sb15.append("STATE_WAITING_AE_LOCK:  AE = ");
                    sb15.append(Util.controlAEStateToString(num15));
                    Log.d(access$00016, sb15.toString());
                    String access$00017 = MiCamera2.TAG;
                    StringBuilder sb16 = new StringBuilder();
                    sb16.append("STATE_WAITING_AE_LOCK: AWB = ");
                    sb16.append(Util.controlAWBStateToString(num16));
                    Log.d(access$00017, sb16.toString());
                    if (num15 == null && MiCamera2.this.WAITING_AE_STATE_STRICT) {
                        num15 = Integer.valueOf(-1);
                    }
                    if (num15 == null || num15.intValue() == 3) {
                        access$000 = MiCamera2.TAG;
                        str3 = "STATE_WAITING_AE_LOCK: runCaptureSequence()";
                    } else {
                        str2 = MiCamera2.TAG;
                        str = "STATE_WAITING_AE_LOCK: keep stay in STATE_WAITING_AE_LOCK";
                        Log.d(str2, str);
                        return;
                    }
                }
                Log.d(access$000, str3);
                MiCamera2.this.runCaptureSequence();
                return;
            }
            MiCamera2.this.onPartialPreviewMetadata(captureResult);
        }

        /* access modifiers changed from: 0000 */
        public Integer getCurrentAEState() {
            if (getPreviewCaptureResult() == null) {
                return null;
            }
            return (Integer) getPreviewCaptureResult().get(CaptureResult.CONTROL_AE_STATE);
        }

        /* access modifiers changed from: 0000 */
        public int getCurrentColorTemperature() {
            CaptureResult previewCaptureResult = getPreviewCaptureResult();
            int i = 0;
            if (previewCaptureResult == null) {
                return 0;
            }
            AWBFrameControl aWBFrameControl = CaptureResultParser.getAWBFrameControl(previewCaptureResult);
            if (aWBFrameControl != null) {
                i = aWBFrameControl.getColorTemperature();
            }
            return i;
        }

        /* access modifiers changed from: 0000 */
        public Integer getCurrentFlashState() {
            if (getPreviewCaptureResult() == null) {
                return null;
            }
            return (Integer) getPreviewCaptureResult().get(CaptureResult.FLASH_STATE);
        }

        /* access modifiers changed from: 0000 */
        public FocusTask getFocusTask() {
            return this.mManuallyFocusTask;
        }

        /* access modifiers changed from: 0000 */
        public CaptureResult getPreviewCaptureResult() {
            CaptureResult captureResult;
            synchronized (this.mPreviewCaptureResultLock) {
                if (this.mPreviewCaptureResult == null) {
                    String access$000 = MiCamera2.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("returned a null PreviewCaptureResult, mState is ");
                    sb.append(this.mState);
                    Log.w(access$000, sb.toString());
                }
                captureResult = this.mPreviewCaptureResult;
            }
            return captureResult;
        }

        /* access modifiers changed from: 0000 */
        public CaptureResult getPreviewPartialCaptureResult() {
            CaptureResult captureResult;
            synchronized (this.mPreviewPartialCaptureResultLock) {
                if (this.mPreviewPartialCaptureResult == null) {
                    String access$000 = MiCamera2.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("returned a null PreviewPartialCaptureResult, mState is ");
                    sb.append(this.mState);
                    Log.w(access$000, sb.toString());
                }
                captureResult = this.mPreviewPartialCaptureResult;
            }
            return captureResult;
        }

        public int getState() {
            int i;
            synchronized (this.mStateLock) {
                i = this.mState;
            }
            return i;
        }

        /* access modifiers changed from: 0000 */
        public void onCapabilityChanged(CameraCapabilities cameraCapabilities) {
            if (cameraCapabilities != null) {
                this.mFocusAreaSupported = cameraCapabilities.isAFRegionSupported();
                boolean z = C0122O00000o.instance().OO0O0oo() && !this.mFocusAreaSupported && cameraCapabilities.isAERegionSupported() && cameraCapabilities.isAELockSupported();
                this.mAELockOnlySupported = z;
            }
        }

        public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
            if (totalCaptureResult.getFrameNumber() == 0) {
                String access$000 = MiCamera2.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureCompleted Sequence: ");
                sb.append(totalCaptureResult.getSequenceId());
                sb.append(" first frame received");
                Log.d(access$000, sb.toString());
                MiCamera2.this.triggerDeviceChecking(true, false);
            }
            if (getState() == 0) {
                setState(1);
            }
            if (!this.mPartialResultSupported) {
                processPartial(totalCaptureResult, captureRequest);
            }
            process(totalCaptureResult);
            MiCamera2.this.updateFrameNumber(totalCaptureResult.getFrameNumber());
        }

        public void onCaptureProgressed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureResult captureResult) {
            processPartial(captureResult, captureRequest);
        }

        /* access modifiers changed from: 0000 */
        public void setFocusTask(FocusTask focusTask) {
            this.mManuallyFocusTask = focusTask;
        }

        /* access modifiers changed from: 0000 */
        public void setState(int i) {
            synchronized (this.mStateLock) {
                String access$000 = MiCamera2.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("setState: ");
                sb.append(i);
                Log.d(access$000, sb.toString());
                this.mState = i;
            }
        }

        /* access modifiers changed from: 0000 */
        public void showAutoFocusFinish(boolean z) {
            if (this.mTorchFocusTask != null) {
                FocusCallback focusCallback = MiCamera2.this.getFocusCallback();
                if (focusCallback != null) {
                    this.mTorchFocusTask.setResult(z);
                    focusCallback.onFocusStateChanged(this.mTorchFocusTask);
                    this.mTorchFocusTask = null;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void showAutoFocusStart() {
            FocusCallback focusCallback = MiCamera2.this.getFocusCallback();
            if (focusCallback != null) {
                this.mTorchFocusTask = FocusTask.create(3);
                focusCallback.onFocusStateChanged(this.mTorchFocusTask);
            }
        }
    }

    static {
        MeteringRectangle meteringRectangle = new MeteringRectangle(0, 0, 0, 0, 0);
        ZERO_WEIGHT_3A_REGION = new MeteringRectangle[]{meteringRectangle};
    }

    public MiCamera2(int i, CameraDevice cameraDevice, CameraCapabilities cameraCapabilities, @NonNull Handler handler, @NonNull Handler handler2) {
        super(i);
        this.mCameraDevice = cameraDevice;
        this.mCapabilities = cameraCapabilities;
        this.mIsCameraClosed = false;
        this.mConfigs = new CameraConfigs();
        this.mSessionConfigs = new CaptureSessionConfigurations(cameraCapabilities);
        this.mCameraHandler = handler;
        this.mCameraPreviewHandler = handler2;
        this.mHelperHandler = initHelperHandler(this.mCameraHandler.getLooper());
        this.mCaptureCallback = new PictureCaptureCallback();
        this.mSupportFlashTimeLock = C0122O00000o.instance().ooOo();
        this.mCameraPreviewMetadata = new PreviewMetadata();
        this.DEF_QUICK_SHOT_THRESHOLD_SHOT_CACHE_COUNT = C0122O00000o.instance().OO000o0();
    }

    static /* synthetic */ int O000000o(HashMap hashMap, Integer num, Integer num2) {
        return ((Float) hashMap.get(num)).floatValue() < ((Float) hashMap.get(num2)).floatValue() ? 1 : -1;
    }

    private void abortCaptures() {
        String str;
        String str2;
        if (C0122O00000o.instance().OO0oO00()) {
            synchronized (this.mSessionLock) {
                if (this.mCaptureSession != null) {
                    try {
                        Log.d(TAG, "abortCaptures E");
                        this.mCaptureSession.abortCaptures();
                        Log.d(TAG, "abortCaptures X");
                    } catch (CameraAccessException e) {
                        e = e;
                        str = TAG;
                        str2 = "abortCaptures(): failed";
                    } catch (IllegalStateException e2) {
                        e = e2;
                        str = TAG;
                        str2 = "abortCaptures, IllegalState";
                    }
                }
            }
        }
        return;
        Log.e(str, str2, e);
    }

    static /* synthetic */ long access$3804(MiCamera2 miCamera2) {
        long j = miCamera2.mFrameCount + 1;
        miCamera2.mFrameCount = j;
        return j;
    }

    private void applyCommonSettings(Builder builder, int i) {
        builder.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(1));
        CaptureRequestBuilder.applyFocusMode(builder, this.mConfigs);
        CaptureRequestBuilder.applyFaceDetection(builder, this.mConfigs);
        CaptureRequestBuilder.applyAntiBanding(builder, this.mConfigs);
        CaptureRequestBuilder.applyExposureCompensation(builder, i, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyZoomRatio(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyBeautyValues(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyHighQualityPreferred(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyAELock(builder, this.mConfigs.isAELocked());
        CaptureRequestBuilder.applyAWBLock(builder, this.mConfigs.isAWBLocked());
        CaptureRequestBuilder.applyEyeLight(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyWaterMark(builder, i, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyCinematicPhoto(builder, i, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyThermal(builder, this.mCapabilities, this.mConfigs);
        if (this.mPreviewControl.needForCapture()) {
            CaptureRequestBuilder.applyContrast(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applySaturation(builder, this.mConfigs);
            CaptureRequestBuilder.applySharpness(builder, this.mConfigs);
            CaptureRequestBuilder.applyExposureMeteringMode(builder, this.mConfigs);
            CaptureRequestBuilder.applySceneMode(builder, this.mConfigs);
            CaptureRequestBuilder.applySuperNightScene(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyHHT(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyHDR(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applySuperResolution(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyHwMfnr(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applySwMfnr(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applySingleBokeh(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyDualBokeh(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyFaceAgeAnalyze(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyFaceScore(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyFrontMirror(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyCameraAi30Enable(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyMacroMode(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyUltraPixelPortrait(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyColorEnhance(builder, this.mCapabilities, this.mConfigs);
        }
        if (this.mPreviewControl.needForPortrait()) {
            CaptureRequestBuilder.applyContrast(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applySaturation(builder, this.mConfigs);
            CaptureRequestBuilder.applySharpness(builder, this.mConfigs);
            if (this.mPreviewControl.needForFrontCamera()) {
                CaptureRequestBuilder.applySingleBokeh(builder, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applyFrontMirror(builder, i, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applyFaceAgeAnalyze(builder, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applyFaceScore(builder, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applyHwMfnr(builder, i, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applySwMfnr(builder, i, this.mCapabilities, this.mConfigs);
            } else {
                CaptureRequestBuilder.applySingleBokeh(builder, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applyDualBokeh(builder, this.mCapabilities, this.mConfigs);
            }
            if (this.mCapabilities.isMFNRBokehSupported()) {
                CaptureRequestBuilder.applyHwMfnr(builder, i, this.mCapabilities, this.mConfigs);
            }
            CaptureRequestBuilder.applyPortraitLighting(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyFNumber(builder, this.mCapabilities, this.mConfigs);
        }
        if (this.mPreviewControl.needForManually()) {
            CaptureRequestBuilder.applyAWBMode(builder, this.mConfigs.getAWBMode());
            CaptureRequestBuilder.applyCustomAWB(builder, this.mConfigs.getAwbCustomValue());
            CaptureRequestBuilder.applyIso(builder, i, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyExposureTime(builder, i, this.mConfigs);
            CaptureRequestBuilder.applyExposureCompensation(builder, i, this.mCapabilities, this.mConfigs);
        }
        if (this.mPreviewControl.needForNormalCapture()) {
            CaptureRequestBuilder.applyAiShutterExistMotion(builder, i, this.mCapabilities, this.mConfigs);
        }
        CaptureRequestBuilder.applyFocusDistance(builder, this.mConfigs);
        CaptureRequestBuilder.applyNormalWideLDC(builder, i, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyUltraWideLDC(builder, this.mCapabilities, this.mConfigs);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x006b, code lost:
        if (r0.mPreviewControl.needForCapture() != false) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0070, code lost:
        r12 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0089, code lost:
        if (r0.mPreviewControl.needForCapture() != false) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0092, code lost:
        if (useLegacyFlashStrategy() == false) goto L_0x0080;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x012d, code lost:
        if (r14 != null) goto L_0x012f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01c7, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.isMTKPlatform() != false) goto L_0x01c9;
     */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00de  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00ea  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x01ef  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyFlashMode(Builder builder, int i) {
        boolean z;
        int i2;
        Key key;
        Integer valueOf;
        Integer num;
        Key key2;
        Builder builder2 = builder;
        int i3 = i;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("applyFlashMode: request = ");
        sb.append(builder2);
        sb.append(", applyType = ");
        sb.append(i3);
        Log.d(str, sb.toString());
        if (builder2 != null) {
            int flashMode = this.mConfigs.getFlashMode();
            CaptureRequestBuilder.applyFlashMode(builder2, this.mCapabilities, this.mConfigs);
            if (i3 != 3) {
                if (i3 != 6) {
                    if (i3 == 7 && C0124O00000oO.O0o0O0O && flashMode == 3) {
                        CaptureRequestBuilder.applyBackSoftLight(this.mCapabilities, this.mPreviewRequestBuilder, false);
                        this.mConfigs.setFlashCurrent(0);
                        CaptureRequestBuilder.applyFlashCurrent(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
                    }
                } else if (needOptimizedFlash()) {
                }
                i2 = flashMode;
                z = false;
                ScreenLightCallback screenLightCallback = getScreenLightCallback();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("applyFlashMode: flashMode = ");
                sb2.append(i2);
                sb2.append(", mScreenLightCallback = ");
                sb2.append(screenLightCallback);
                Log.d(str2, sb2.toString());
                CaptureRequestBuilder.applyScreenLightHint(this.mCapabilities, builder2, i2 != 101);
                if (!(i2 == 200 || i2 == 0)) {
                    if (!C0124O00000oO.O0o0O00) {
                        boolean z2 = flashMode == 5 || flashMode == 3;
                        CaptureRequestBuilder.applyBackSoftLight(this.mCapabilities, builder2, z2);
                    } else {
                        CaptureRequestBuilder.applyBackSoftLight(this.mCapabilities, builder2, i2 == 5);
                    }
                }
                if (i2 == 0) {
                    if (i2 != 1) {
                        if (i2 != 2) {
                            if (i2 == 3) {
                                builder2.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(2));
                            } else if (i2 == 4) {
                                key2 = CaptureRequest.CONTROL_AE_MODE;
                                num = Integer.valueOf(4);
                                builder2.set(key2, num);
                            } else if (i2 != 5) {
                                if (i2 != 101) {
                                    if (i2 == 103) {
                                        String str3 = TAG;
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("applyFlashMode: FLASH_MODE_SCREEN_LIGHT_AUTO applyType = ");
                                        sb3.append(i3);
                                        Log.d(str3, sb3.toString());
                                    } else if (i2 == 200) {
                                        if (C0124O00000oO.isMTKPlatform()) {
                                            key = CaptureRequest.CONTROL_AE_MODE;
                                            valueOf = Integer.valueOf(0);
                                        } else {
                                            key = CaptureRequest.CONTROL_AE_MODE;
                                            valueOf = Integer.valueOf(1);
                                        }
                                    }
                                }
                                if (screenLightCallback != null) {
                                    if (i3 == 6) {
                                        this.mScreenLightColorTemperature = this.mCaptureCallback.getCurrentColorTemperature();
                                    }
                                    int screenLightColor = Util.getScreenLightColor(SystemProperties.getInt("camera_screen_light_wb", this.mScreenLightColorTemperature));
                                    int screenLightBrightness = CameraSettings.getScreenLightBrightness();
                                    int i4 = SystemProperties.getInt("camera_screen_light_delay", 0);
                                    String str4 = TAG;
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("applyFlashMode: FLASH_MODE_SCREEN_LIGHT_ON color = ");
                                    sb4.append(screenLightColor);
                                    sb4.append(", brightness = ");
                                    sb4.append(screenLightBrightness);
                                    sb4.append(", delay = ");
                                    sb4.append(i4);
                                    sb4.append(", mCameraHandler = ");
                                    sb4.append(this.mCameraHandler);
                                    Log.d(str4, sb4.toString());
                                    if (i3 == 6 || i3 == 3) {
                                        screenLightCallback.startScreenLight(screenLightColor, screenLightBrightness);
                                    } else if (i3 == 7) {
                                        if (i4 != 0) {
                                            this.mCameraHandler.postDelayed(new O00000o0(screenLightCallback), (long) i4);
                                        }
                                    }
                                }
                                screenLightCallback.stopScreenLight();
                            }
                        } else if (this.mCapabilities.isSupportSnapShotTorch()) {
                            MiCameraCompat.applySnapshotTorch(builder2, z);
                        }
                        builder2.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(1));
                        key2 = CaptureRequest.FLASH_MODE;
                        num = Integer.valueOf(2);
                        builder2.set(key2, num);
                    }
                    builder2.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(3));
                    key2 = CaptureRequest.FLASH_MODE;
                    num = Integer.valueOf(1);
                    builder2.set(key2, num);
                }
                boolean z3 = C0124O00000oO.isMTKPlatform() && (this.mConfigs.getISO() > 0 || this.mConfigs.getExposureTime() > 0);
                if (z3) {
                    key = CaptureRequest.CONTROL_AE_MODE;
                    valueOf = Integer.valueOf(0);
                } else {
                    key = CaptureRequest.CONTROL_AE_MODE;
                    valueOf = Integer.valueOf(1);
                }
                builder2.set(key, valueOf);
                key2 = CaptureRequest.FLASH_MODE;
                num = Integer.valueOf(0);
                builder2.set(key2, num);
            }
            if (!needOptimizedFlash()) {
                if (flashMode == 3) {
                }
                i2 = flashMode;
                z = false;
                ScreenLightCallback screenLightCallback2 = getScreenLightCallback();
                String str22 = TAG;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("applyFlashMode: flashMode = ");
                sb22.append(i2);
                sb22.append(", mScreenLightCallback = ");
                sb22.append(screenLightCallback2);
                Log.d(str22, sb22.toString());
                CaptureRequestBuilder.applyScreenLightHint(this.mCapabilities, builder2, i2 != 101);
                if (!C0124O00000oO.O0o0O00) {
                }
                if (i2 == 0) {
                }
                builder2.set(key, valueOf);
                key2 = CaptureRequest.FLASH_MODE;
                num = Integer.valueOf(0);
                builder2.set(key2, num);
            } else if (getExposureTime() <= 0) {
            }
            i2 = 0;
            z = false;
            ScreenLightCallback screenLightCallback22 = getScreenLightCallback();
            String str222 = TAG;
            StringBuilder sb222 = new StringBuilder();
            sb222.append("applyFlashMode: flashMode = ");
            sb222.append(i2);
            sb222.append(", mScreenLightCallback = ");
            sb222.append(screenLightCallback22);
            Log.d(str222, sb222.toString());
            CaptureRequestBuilder.applyScreenLightHint(this.mCapabilities, builder2, i2 != 101);
            if (!C0124O00000oO.O0o0O00) {
            }
            if (i2 == 0) {
            }
            builder2.set(key, valueOf);
            key2 = CaptureRequest.FLASH_MODE;
            num = Integer.valueOf(0);
            builder2.set(key2, num);
            i2 = 2;
            z = true;
            ScreenLightCallback screenLightCallback222 = getScreenLightCallback();
            String str2222 = TAG;
            StringBuilder sb2222 = new StringBuilder();
            sb2222.append("applyFlashMode: flashMode = ");
            sb2222.append(i2);
            sb2222.append(", mScreenLightCallback = ");
            sb2222.append(screenLightCallback222);
            Log.d(str2222, sb2222.toString());
            CaptureRequestBuilder.applyScreenLightHint(this.mCapabilities, builder2, i2 != 101);
            if (!C0124O00000oO.O0o0O00) {
            }
            if (i2 == 0) {
            }
            builder2.set(key, valueOf);
            key2 = CaptureRequest.FLASH_MODE;
            num = Integer.valueOf(0);
            builder2.set(key2, num);
        }
    }

    private void applySettingsForFocusCapture(Builder builder) {
        CaptureRequestBuilder.applyAFRegions(builder, this.mConfigs);
        CaptureRequestBuilder.applyAERegions(builder, this.mConfigs);
        CaptureRequestBuilder.applyZoomRatio(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyHighQualityPreferred(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyAWBMode(builder, this.mConfigs.getAWBMode());
        CaptureRequestBuilder.applyCustomAWB(builder, this.mConfigs.getAwbCustomValue());
        CaptureRequestBuilder.applyExposureCompensation(builder, 1, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyAntiShake(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyBeautyValues(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyFrontMirror(builder, 3, this.mCapabilities, this.mConfigs);
        if (this.mPreviewControl.needForCapture()) {
            CaptureRequestBuilder.applyContrast(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applySaturation(builder, this.mConfigs);
            CaptureRequestBuilder.applySharpness(builder, this.mConfigs);
        }
        if (this.mPreviewControl.needForManually()) {
            CaptureRequestBuilder.applyIso(builder, 1, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyExposureTime(builder, 1, this.mConfigs);
        }
        if (this.mPreviewControl.needForVideo()) {
            CaptureRequestBuilder.applyVideoFpsRange(builder, this.mConfigs);
        }
        if (this.mPreviewControl.needForProVideo()) {
            CaptureRequestBuilder.applyAWBMode(builder, this.mConfigs.getAWBMode());
            CaptureRequestBuilder.applyCustomAWB(builder, this.mConfigs.getAwbCustomValue());
            CaptureRequestBuilder.applyIso(builder, 3, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyExposureTime(builder, 3, this.mConfigs);
            CaptureRequestBuilder.applyExposureCompensation(builder, 3, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyFocusMode(builder, this.mConfigs);
            CaptureRequestBuilder.applyFocusDistance(builder, this.mConfigs);
        }
        builder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(1));
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(0));
        applyFlashMode(builder, 1);
        CaptureRequestBuilder.applyFpsRange(builder, this.mConfigs);
        CaptureRequestBuilder.applyUltraWideLDC(builder, this.mCapabilities, this.mConfigs);
    }

    private void applySettingsForLockFocus(Builder builder, boolean z) {
        Key key = CaptureRequest.CONTROL_AF_TRIGGER;
        Integer valueOf = Integer.valueOf(1);
        builder.set(key, valueOf);
        CaptureRequestBuilder.applyAFRegions(builder, this.mConfigs);
        CaptureRequestBuilder.applyAERegions(builder, this.mConfigs);
        CaptureRequestBuilder.applyFpsRange(builder, this.mConfigs);
        applyCommonSettings(builder, 1);
        if (!useLegacyFlashStrategy() && !z) {
            builder.set(CaptureRequest.CONTROL_AF_MODE, valueOf);
        }
        if (needOptimizedFlash() || needScreenLight() || C0124O00000oO.isMTKPlatform() || z) {
            applyFlashMode(builder, 6);
        }
        CaptureRequestBuilder.applyAntiShake(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applySessionParameters(builder, this.mSessionConfigs);
    }

    private void applySettingsForPreCapture(Builder builder) {
        applyCommonSettings(builder, 1);
        applyFlashMode(builder, 6);
        builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, Integer.valueOf(1));
        CaptureRequestBuilder.applyFpsRange(builder, this.mConfigs);
        CaptureRequestBuilder.applySessionParameters(builder, this.mSessionConfigs);
    }

    private void applySettingsForPreview(Builder builder) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("applySettingsForPreview: ");
        sb.append(builder);
        Log.d(str, sb.toString());
        if (builder != null) {
            applyFlashMode(builder, 1);
            applyCommonSettings(builder, 1);
            CaptureRequestBuilder.applyLensDirtyDetect(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyAELock(builder, this.mConfigs.isAELocked());
            CaptureRequestBuilder.applyAWBLock(builder, this.mConfigs.isAWBLocked());
            CaptureRequestBuilder.applyAntiShake(builder, this.mCapabilities, this.mConfigs);
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(0));
            CaptureRequestBuilder.applyFpsRange(builder, this.mConfigs);
            CaptureRequestBuilder.applySessionParameters(builder, this.mSessionConfigs);
            CaptureRequestBuilder.applyColorEnhance(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applySatFallbackDisable(builder, this.mCapabilities, this.mConfigs.isSatFallbackDisable());
            CaptureRequestBuilder.applyAiASDEnable(builder, this.mConfigs);
            CaptureRequestBuilder.applyASDScene(this.mCapabilities, builder, this.mConfigs);
            if ((this.mRawCallbackType & 8) != 0) {
                MiCameraCompat.applySuperNightRawEnabled(builder, true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void applySettingsForVideo(Builder builder) {
        if (builder != null) {
            builder.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(1));
            applyFlashMode(builder, 1);
            CaptureRequestBuilder.applyLensDirtyDetect(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyFocusMode(builder, this.mConfigs);
            CaptureRequestBuilder.applyFaceDetection(builder, this.mConfigs);
            CaptureRequestBuilder.applyAntiBanding(builder, this.mConfigs);
            CaptureRequestBuilder.applyExposureMeteringMode(builder, this.mConfigs);
            CaptureRequestBuilder.applyExposureCompensation(builder, 1, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyZoomRatio(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyAntiShake(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyAELock(builder, this.mConfigs.isAELocked());
            CaptureRequestBuilder.applyBeautyValues(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyVideoFilterId(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyVideoFpsRange(builder, this.mConfigs);
            CaptureRequestBuilder.applyTuningMode(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyFrontMirror(builder, 3, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyDeviceOrientation(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyUltraWideLDC(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyMacroMode(builder, 1, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyHFRDeflicker(builder, 1, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyVideoBokehLevelFront(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyColorRetentionFront(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyVideoBokehLevelBack(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyColorRetentionBack(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyHDR10Video(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyCinematicVideo(builder, this.mCapabilities, this.mConfigs);
            if (this.mPreviewControl.needForProVideo()) {
                CaptureRequestBuilder.applyAWBMode(builder, this.mConfigs.getAWBMode());
                CaptureRequestBuilder.applyCustomAWB(builder, this.mConfigs.getAwbCustomValue());
                CaptureRequestBuilder.applyIso(builder, 3, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applyExposureTime(builder, 3, this.mConfigs);
                CaptureRequestBuilder.applyExposureCompensation(builder, 3, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applyVideoLog(builder, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applyFocusDistance(builder, this.mConfigs);
                CaptureRequestBuilder.applyHistogramStats(builder, this.mCapabilities, this.mConfigs);
            }
            CaptureRequestBuilder.applySessionParameters(builder, this.mSessionConfigs);
        }
    }

    private void applyVideoHdrModeIfNeed() {
        if (this.mCapabilities.isSupportVideoHdr() && this.mConfigs.isVideoHdrEnable()) {
            Log.d(TAG, "turns video.hdr.mode on");
            if (C0124O00000oO.isMTKPlatform()) {
                this.mSessionConfigs.set(CaptureRequestVendorTags.MTK_HDR_KEY_DETECTION_MODE, (Object) CaptureRequestVendorTags.MTK_HDR_FEATURE_HDR_MODE_VIDEO_ON);
                MiCameraCompat.applyVideoHdrMode(this.mPreviewRequestBuilder, CaptureRequestVendorTags.MTK_HDR_FEATURE_HDR_MODE_VIDEO_ON);
            } else {
                this.mSessionConfigs.set(CaptureRequestVendorTags.QCOM_VIDEO_HDR_ENABLED, (Object) Integer.valueOf(1));
                MiCameraCompat.applyVideoHdrMode(this.mPreviewRequestBuilder, true);
            }
        }
    }

    private void assertRemoteSurfaceIndexIsValid(int i) {
        if (i < 0 || i > this.mParallelCaptureSurfaceList.size() - 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("invalid remote surface index ");
            sb.append(i);
            throw new RuntimeException(sb.toString());
        }
    }

    private int capture(CaptureRequest captureRequest, CaptureCallback captureCallback, Handler handler, FocusTask focusTask) {
        synchronized (this.mSessionLock) {
            if (this.mCaptureSession == null) {
                Log.w(TAG, "capture: null session");
                return 0;
            } else if (this.mCaptureSession instanceof CameraConstrainedHighSpeedCaptureSession) {
                List<CaptureRequest> createHighSpeedRequestList = createHighSpeedRequestList(captureRequest);
                if (focusTask != null) {
                    focusTask.setRequest((CaptureRequest) createHighSpeedRequestList.get(0));
                }
                for (CaptureRequest captureRequest2 : createHighSpeedRequestList) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("capture burst for camera ");
                    sb.append(getId());
                    Log.dumpRequest(sb.toString(), captureRequest2);
                }
                int captureBurst = this.mCaptureSession.captureBurst(createHighSpeedRequestList, captureCallback, handler);
                return captureBurst;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("capture for camera ");
                sb2.append(getId());
                Log.dumpRequest(sb2.toString(), captureRequest);
                int capture = this.mCaptureSession.capture(captureRequest, captureCallback, handler);
                return capture;
            }
        }
    }

    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r0v3, types: [com.android.camera2.MiCamera2Shot, java.lang.Object] */
    /* JADX WARNING: type inference failed for: r0v11, types: [com.android.camera2.MiCamera2ShotSimplePreview] */
    /* JADX WARNING: type inference failed for: r0v12, types: [com.android.camera2.MiCamera2Shot] */
    /* JADX WARNING: type inference failed for: r0v13, types: [com.android.camera2.MiCamera2Shot] */
    /* JADX WARNING: type inference failed for: r0v14, types: [com.android.camera2.MiCamera2ShotParallelStill] */
    /* JADX WARNING: type inference failed for: r0v15, types: [com.android.camera2.MiCamera2Shot] */
    /* JADX WARNING: type inference failed for: r0v16, types: [com.android.camera2.MiCamera2ShotStill] */
    /* JADX WARNING: type inference failed for: r0v17, types: [com.android.camera2.MiCamera2ShotPreview] */
    /* JADX WARNING: type inference failed for: r0v18 */
    /* JADX WARNING: type inference failed for: r0v19, types: [com.android.camera2.MiCamera2ShotParallelStill] */
    /* JADX WARNING: type inference failed for: r0v21, types: [com.android.camera2.MiCamera2ShotParallelBurst] */
    /* JADX WARNING: type inference failed for: r0v24, types: [com.android.camera2.MiCamera2ShotRawBurst] */
    /* JADX WARNING: type inference failed for: r0v27, types: [com.android.camera2.MiCamera2ShotBurst] */
    /* JADX WARNING: type inference failed for: r0v28 */
    /* JADX WARNING: type inference failed for: r0v29 */
    /* JADX WARNING: type inference failed for: r0v30 */
    /* JADX WARNING: type inference failed for: r0v31 */
    /* JADX WARNING: type inference failed for: r0v32 */
    /* JADX WARNING: type inference failed for: r0v33 */
    /* JADX WARNING: type inference failed for: r0v34 */
    /* JADX WARNING: type inference failed for: r0v35 */
    /* JADX WARNING: type inference failed for: r0v36 */
    /* JADX WARNING: type inference failed for: r0v37 */
    /* JADX WARNING: type inference failed for: r0v38 */
    /* JADX WARNING: type inference failed for: r0v39 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00e6, code lost:
        r0.setQuickShotAnimation(r5.mConfigs.isQuickShotAnimation());
        r0 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0107, code lost:
        r0.setQuickShotAnimation(r5.mConfigs.isQuickShotAnimation());
        r0 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0110, code lost:
        r0.setMagneticDetectedCallback(getMagneticDetectedCallback());
        r0 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0124, code lost:
        if (r5.mConfigs.getShotType() != -8) goto L_0x0129;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0126, code lost:
        r5.mMiCamera2Shot = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0128, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x012f, code lost:
        if (r5.mMiCamera2ShotQueue.offerLast(r0) == false) goto L_0x0154;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0131, code lost:
        r5.mCaptureTime = java.lang.System.currentTimeMillis();
        r1 = TAG;
        r2 = new java.lang.StringBuilder();
        r2.append("capture: mMiCamera2ShotQueue.offer, size: ");
        r2.append(r5.mMiCamera2ShotQueue.size());
        com.android.camera.log.Log.d(r1, r2.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0154, code lost:
        r1 = TAG;
        r2 = new java.lang.StringBuilder();
        r2.append("capture: mMiCamera2ShotQueue.offer failure, size: ");
        r2.append(r5.mMiCamera2ShotQueue.size());
        com.android.camera.log.Log.e(r1, r2.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0170, code lost:
        r5.mMiCamera2Shot = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0174, code lost:
        if (r5.mMiCamera2Shot == null) goto L_0x01bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x017a, code lost:
        if (isIn3OrMoreSatMode() == false) goto L_0x0185;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0180, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.isMTKPlatform() != false) goto L_0x0185;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0182, code lost:
        disableSat();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0185, code lost:
        r0 = TAG;
        r1 = new java.lang.StringBuilder();
        r1.append("startShot holder: ");
        r1.append(r5.mMiCamera2Shot.hashCode());
        com.android.camera.log.Log.d(r0, r1.toString());
        r5.mMiCamera2Shot.setPictureCallback(getPictureCallback());
        r5.mMiCamera2Shot.setParallelCallback(getParallelCallback());
        r5.mMiCamera2Shot.startShot();
        triggerDeviceChecking(true, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x01bb, code lost:
        return;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 12 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void captureStillPicture() {
        ? r0;
        ? r02;
        ? r03;
        if (checkCaptureSession(BaseEvent.CAPTURE)) {
            MiCamera2Shot miCamera2Shot = 0;
            switch (this.mConfigs.getShotType()) {
                case -8:
                    r0 = new MiCamera2ShotSimplePreview(this);
                    break;
                case -7:
                case -6:
                case -5:
                case 6:
                case 7:
                    r02 = new MiCamera2ShotParallelStill(this, this.mCaptureCallback.getPreviewCaptureResult(), useParallelVTCameraSnapshot(false), getCameraConfigs().getDoRemosaic());
                    break;
                case -3:
                case -2:
                case 0:
                case 1:
                case 2:
                    r03 = new MiCamera2ShotStill(this);
                    break;
                case -1:
                    r0 = new MiCamera2ShotPreview(this, this.mCaptureCallback.getPreviewCaptureResult());
                    break;
                case 5:
                    if (C0122O00000o.instance().OOoO0O() && this.mConfigs.getFlashMode() == 0) {
                        MiCamera2ShotParallelRawBurst miCamera2ShotParallelRawBurst = new MiCamera2ShotParallelRawBurst(this, this.mCaptureCallback.getPreviewCaptureResult());
                        if (miCamera2ShotParallelRawBurst.shouldApply()) {
                            miCamera2Shot = miCamera2ShotParallelRawBurst;
                        }
                    }
                    if (r0 == 0) {
                        r02 = new MiCamera2ShotParallelStill(this, this.mCaptureCallback.getPreviewCaptureResult(), useParallelVTCameraSnapshot(false), getCameraConfigs().getDoRemosaic());
                        break;
                    }
                    break;
                case 8:
                case 11:
                    ? r04 = new MiCamera2ShotParallelBurst(this, this.mCaptureCallback.getPreviewCaptureResult(), useParallelVTCameraSnapshot(true));
                    break;
                case 10:
                    if (this.mSuperNightReprocessHandler == null) {
                        HandlerThread handlerThread = new HandlerThread("SNReprocessThread");
                        handlerThread.start();
                        this.mSuperNightReprocessHandler = new SuperNightReprocessHandler(handlerThread.getLooper(), this, C0122O00000o.instance().OO00Oo0());
                    }
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("SuperNightReprocessHandler@");
                    sb.append(this.mSuperNightReprocessHandler.hashCode());
                    Log.d(str, sb.toString());
                    r03 = new MiCamera2ShotRawBurst(this, this.mSuperNightReprocessHandler);
                    break;
                case 12:
                    r0 = new MiCamera2ShotBurst(this, -1, this.mConfigs.isNeedPausePreview());
                    break;
                default:
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("unexpected shot type: ");
                    sb2.append(this.mConfigs.getShotType());
                    Log.e(str2, sb2.toString());
                    r0 = miCamera2Shot;
                    break;
            }
        }
    }

    private boolean checkCameraDevice(String str) {
        if (this.mCameraDevice != null && !this.mIsCameraClosed) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("camera ");
        sb.append(getId());
        sb.append(" is closed when ");
        sb.append(str);
        String sb2 = sb.toString();
        if (this.mIsCameraClosed) {
            Log.d(TAG, sb2);
            return false;
        }
        RuntimeException runtimeException = new RuntimeException(sb2);
        if (!Build.IS_DEBUGGABLE) {
            Log.w(TAG, sb2, (Throwable) runtimeException);
            return false;
        }
        throw runtimeException;
    }

    private boolean checkCaptureSession(String str) {
        synchronized (this.mSessionLock) {
            if (this.mCaptureSession != null) {
                return true;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("session for camera ");
            sb.append(getId());
            sb.append(" is closed when ");
            sb.append(str);
            String sb2 = sb.toString();
            if (this.mIsCaptureSessionClosed) {
                Log.d(TAG, sb2);
                return false;
            }
            RuntimeException runtimeException = new RuntimeException(sb2);
            if (Build.IS_DEBUGGABLE) {
                if (!miui.os.Build.IS_STABLE_VERSION) {
                    throw runtimeException;
                }
            }
            Log.w(TAG, sb2, (Throwable) runtimeException);
            return false;
        }
    }

    private void closeDepthImageReader() {
        ImageReader imageReader = this.mDepthReader;
        if (imageReader != null) {
            imageReader.close();
            this.mDepthReader = null;
        }
    }

    private void closePhotoImageReader() {
        ImageReader imageReader = this.mPhotoImageReader;
        if (imageReader != null) {
            imageReader.close();
            this.mPhotoImageReader = null;
        }
    }

    private void closePortraitRawImageReader() {
        ImageReader imageReader = this.mPortraitRawImageReader;
        if (imageReader != null) {
            imageReader.close();
            this.mPortraitRawImageReader = null;
        }
    }

    private void closePreviewImageReader() {
        ImageReader imageReader = this.mPreviewImageReader;
        if (imageReader != null) {
            imageReader.close();
            this.mPreviewImageReader = null;
        }
    }

    private void closeRawImageReader() {
        ImageReader imageReader = this.mRawImageReader;
        if (imageReader != null) {
            imageReader.close();
            this.mRawImageReader = null;
        }
    }

    private void closeVideoSnapshotImageReader() {
        ImageReader imageReader = this.mVideoSnapshotImageReader;
        if (imageReader != null) {
            imageReader.close();
            this.mVideoSnapshotImageReader = null;
        }
    }

    private void configMaxParallelRequestNumberLock() {
        int i = 1;
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder(true);
        if (localBinder != null) {
            int OO000Oo = (!C0124O00000oO.O0o00o || DataRepository.dataItemGlobal().getCurrentCameraId() != 1) ? C0122O00000o.instance().OO000Oo() : 3;
            if (Util.TOTAL_MEMORY_GB >= 4 || C0124O00000oO.O0o0oo) {
                i = OO000Oo;
            } else {
                Log.d(TAG, "current total memory is 4G or lower ");
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("configMaxParallelRequestNumberLock prNum:");
            sb.append(i);
            Log.v(str, sb.toString());
            if (i <= 0) {
                i = 5;
            }
            localBinder.configMaxParallelRequestNumber(i);
        }
    }

    private List createHighSpeedRequestList(CaptureRequest captureRequest) {
        Integer num;
        Key key;
        if (captureRequest != null) {
            Collection targets = captureRequest.getTargets();
            Range range = (Range) captureRequest.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("createHighSpeedRequestList() fpsRange = ");
            sb.append(range);
            Log.d(str, sb.toString());
            int intValue = ((Integer) range.getUpper()).intValue() / 30;
            ArrayList arrayList = new ArrayList();
            int i = 0;
            Builder constructCaptureRequestBuilder = CompatibilityUtils.constructCaptureRequestBuilder(new CameraMetadataNative(captureRequest.getNativeCopy()), false, -1, captureRequest);
            Iterator it = targets.iterator();
            Surface surface = (Surface) it.next();
            if (targets.size() != 1 || SurfaceUtils.isSurfaceForHwVideoEncoder(surface)) {
                key = CaptureRequest.CONTROL_CAPTURE_INTENT;
                num = Integer.valueOf(3);
            } else {
                key = CaptureRequest.CONTROL_CAPTURE_INTENT;
                num = Integer.valueOf(1);
            }
            constructCaptureRequestBuilder.set(key, num);
            constructCaptureRequestBuilder.setPartOfCHSRequestList(true);
            Builder builder = null;
            if (targets.size() == 2) {
                builder = CompatibilityUtils.constructCaptureRequestBuilder(new CameraMetadataNative(captureRequest.getNativeCopy()), false, -1, captureRequest);
                builder.set(CaptureRequest.CONTROL_CAPTURE_INTENT, Integer.valueOf(3));
                builder.addTarget(surface);
                Surface surface2 = (Surface) it.next();
                builder.addTarget(surface2);
                builder.setPartOfCHSRequestList(true);
                if (SurfaceUtils.isSurfaceForHwVideoEncoder(surface)) {
                    surface2 = surface;
                }
                constructCaptureRequestBuilder.addTarget(surface2);
            } else {
                constructCaptureRequestBuilder.addTarget(surface);
            }
            while (i < intValue) {
                CaptureRequest build = (i != 0 || builder == null) ? constructCaptureRequestBuilder.build() : builder.build();
                arrayList.add(build);
                i++;
            }
            return Collections.unmodifiableList(arrayList);
        }
        throw new IllegalArgumentException("Input capture request must not be null");
    }

    private void disableSat() {
        Log.d(TAG, "disableSat: E");
        CaptureRequestBuilder.applySmoothTransition(this.mPreviewRequestBuilder, this.mCapabilities, false);
        resumePreview();
        Log.d(TAG, "disableSat: X");
    }

    private int genSessionId() {
        int i = this.mSessionId + 1;
        this.mSessionId = i;
        if (i == Integer.MAX_VALUE) {
            this.mSessionId = 0;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("generateSessionId: id=");
        sb.append(this.mSessionId);
        Log.v(str, sb.toString());
        return this.mSessionId;
    }

    private long getCaptureInterval() {
        long OO00OOo = C0122O00000o.instance().OO00OOo() - (System.currentTimeMillis() - this.mCaptureTime);
        if (this.mConfigs.isHDREnabled() || this.mConfigs.isHDRCheckerEnabled()) {
            OO00OOo += 800;
        }
        if (CameraSettings.isUltraPixelFront32MPOn()) {
            OO00OOo += 600;
        }
        if (this.mConfigs.isSingleBokehEnabled()) {
            OO00OOo += 800;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getCaptureInterval: return ");
        sb.append(OO00OOo);
        Log.d(str, sb.toString());
        if (OO00OOo > 0) {
            return OO00OOo;
        }
        return 0;
    }

    private long getExposureTime() {
        return this.mConfigs.getExposureTime();
    }

    private int getMaxImageBufferSize() {
        this.mMaxImageBufferSize = C0122O00000o.instance().O0ooo0o();
        return CameraSettings.isUltraPixelOn() ? C0122O00000o.instance().O0ooo0() : this.mMaxImageBufferSize;
    }

    private Surface getRemoteSurface(int i) {
        assertRemoteSurfaceIndexIsValid(i);
        return (Surface) this.mParallelCaptureSurfaceList.get(i);
    }

    private Builder initFocusRequestBuilder(int i) {
        if (i == 160) {
            throw new IllegalArgumentException("Module index is error!");
        } else if (i == 166) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("initFocusRequestBuilder: error caller for ");
            sb.append(i);
            Log.e(str, sb.toString());
            return null;
        } else if (this.mCameraDevice == null) {
            return null;
        } else {
            Builder initRequestBuilder = initRequestBuilder(i);
            initRequestBuilder.addTarget(this.mPreviewSurface);
            if (isHighSpeedRecording()) {
                initRequestBuilder.addTarget(this.mRecordSurface);
                initRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.mHighSpeedFpsRange);
            } else if (this.mRecordSurface != null && this.mPreviewRequestBuilder.build().containsTarget(this.mRecordSurface)) {
                initRequestBuilder.addTarget(this.mRecordSurface);
            }
            CaptureRequestBuilder.applySessionParameters(initRequestBuilder, this.mSessionConfigs);
            return initRequestBuilder;
        }
    }

    private Handler initHelperHandler(Looper looper) {
        return new Handler(looper) {
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    Log.e(MiCamera2.TAG, "waiting af lock timeOut");
                    MiCamera2.this.runCaptureSequence();
                } else if (i == 2) {
                    MiCamera2 miCamera2 = MiCamera2.this;
                    boolean updateDeferPreviewSession = miCamera2.updateDeferPreviewSession(miCamera2.mPreviewSurface);
                    String access$000 = MiCamera2.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("handleMessage: MSG_WAITING_LOCAL_PARALLEL_SERVICE_READY updateDeferPreviewSession result = ");
                    sb.append(updateDeferPreviewSession);
                    Log.d(access$000, sb.toString());
                } else if (i == 3) {
                    if (message.arg1 == 1 && MiCamera2.this.mPreviewControl.needForManually() && MiCamera2.this.mConfigs.getExposureTime() / ExtraTextUtils.MB >= 5000) {
                        removeMessages(3);
                        sendEmptyMessageDelayed(3, (MiCamera2.this.mConfigs.getExposureTime() / ExtraTextUtils.MB) + 5000);
                    } else if (message.arg1 != 0) {
                    } else {
                        if (MiCamera2.this.isDeviceAlive()) {
                            sendEmptyMessageDelayed(3, 5000);
                        } else {
                            MiCamera2.this.notifyOnError(238);
                        }
                    }
                }
            }
        };
    }

    private Builder initRequestBuilder(int i) {
        CameraDevice cameraDevice;
        int i2;
        if (i == 162 || i == 169 || i == 172 || i == 180 || i == 204 || i == 207 || i == 208) {
            cameraDevice = this.mCameraDevice;
            i2 = 3;
        } else {
            cameraDevice = this.mCameraDevice;
            i2 = 1;
        }
        return cameraDevice.createCaptureRequest(i2);
    }

    /* access modifiers changed from: private */
    public boolean isDeviceAlive() {
        long j = this.mCurrentFrameNum;
        if (0 > j || j != this.mLastFrameNum) {
            this.mLastFrameNum = this.mCurrentFrameNum;
            return true;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("camera device maybe dead, current framenum is ");
        sb.append(this.mLastFrameNum);
        Log.e(str, sb.toString());
        return false;
    }

    private boolean isHighSpeedRecording() {
        if (this.mCaptureSession instanceof CameraConstrainedHighSpeedCaptureSession) {
            return true;
        }
        CaptureSessionConfigurations captureSessionConfigurations = this.mSessionConfigs;
        boolean z = false;
        if (captureSessionConfigurations == null) {
            return false;
        }
        if (((int[]) captureSessionConfigurations.get(CaptureRequestVendorTags.SMVR_MODE)) != null) {
            z = true;
        }
        return z;
    }

    private boolean isLocalParallelServiceReady() {
        return !this.mEnableParallelSession || AlgoConnector.getInstance().getLocalBinder() != null;
    }

    /* access modifiers changed from: private */
    public void lockFocus() {
        String str = "lockFocus";
        if (checkCaptureSession(str)) {
            if (this.mCaptureCallback.getFocusTask() == null || !useLegacyFlashStrategy()) {
                Log.v(TAG, str);
                try {
                    Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(1);
                    createCaptureRequest.addTarget(this.mPreviewSurface);
                    applySettingsForLockFocus(createCaptureRequest, false);
                    CaptureRequest build = createCaptureRequest.build();
                    this.mFocusLockRequestHashCode = build.hashCode();
                    this.mCaptureCallback.setState(3);
                    this.mCaptureCallback.showAutoFocusStart();
                    capture(build, this.mCaptureCallback, this.mCameraHandler, null);
                    if (!useLegacyFlashStrategy()) {
                        setAFModeToPreview(1);
                    }
                    if (this.mHelperHandler != null) {
                        this.mHelperHandler.removeMessages(1);
                        this.mHelperHandler.sendEmptyMessageDelayed(1, useLegacyFlashStrategy() ? 4000 : 3000);
                    }
                } catch (CameraAccessException | IllegalStateException e) {
                    e.printStackTrace();
                    Log.k(6, TAG, e.getMessage());
                    notifyOnError(-1);
                }
                return;
            }
            this.mFocusLockRequestHashCode = 0;
            this.mCaptureCallback.setState(3);
        }
    }

    private boolean lockFocusInCAF(boolean z) {
        if (!checkCaptureSession("lockFocusInCAF")) {
            return false;
        }
        Integer num = (Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_MODE);
        Integer num2 = (Integer) this.mPreviewRequestBuilder.get(CaptureRequest.CONTROL_AF_MODE);
        if (num == null || num2 == null || num.intValue() != 1 || num2.intValue() != 4 || !this.mCaptureCallback.mFocusAreaSupported) {
            Log.w(TAG, "should call this in CAF!");
            return false;
        }
        CameraDevice cameraDevice = this.mCameraDevice;
        if (cameraDevice == null) {
            return false;
        }
        try {
            Builder createCaptureRequest = cameraDevice.createCaptureRequest(1);
            createCaptureRequest.addTarget(this.mPreviewSurface);
            applySettingsForLockFocus(createCaptureRequest, true);
            if (z) {
                Log.d(TAG, "lockFocusInCAF lock!");
            } else {
                Log.d(TAG, "lockFocusInCAF unlock!");
                createCaptureRequest.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(2));
            }
            capture(createCaptureRequest.build(), this.mCaptureCallback, this.mCameraHandler, null);
            return true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.k(6, TAG, e.getMessage());
            return false;
        }
    }

    /* access modifiers changed from: private */
    public boolean needOptimizedFlash() {
        return this.mConfigs.isNeedFlash() && (this.mConfigs.getFlashMode() == 1 || this.mConfigs.getFlashMode() == 3 || getExposureTime() > 0) && !useLegacyFlashStrategy();
    }

    /* access modifiers changed from: private */
    public boolean needScreenLight() {
        return this.mConfigs.isNeedFlash() && this.mConfigs.getFlashMode() == 101;
    }

    private boolean needUnlockFocusAfterCapture() {
        return (!useLegacyFlashStrategy() || 2 == this.mConfigs.getFlashMode() || this.mConfigs.getFlashMode() == 0 || 200 == this.mConfigs.getFlashMode()) ? false : true;
    }

    /* access modifiers changed from: private */
    public void notifyCaptureBusyCallback(boolean z) {
        boolean z2;
        synchronized (this.mShotQueueLock) {
            if (this.mMiCamera2ShotQueue != null) {
                if (!this.mMiCamera2ShotQueue.isEmpty()) {
                    z2 = false;
                    if (z2 && isIn3OrMoreSatMode() && !C0124O00000oO.isMTKPlatform()) {
                        enableSat();
                    }
                }
            }
            z2 = true;
            enableSat();
        }
        CaptureBusyCallback captureBusyCallback = this.mCaptureBusyCallback;
        if (captureBusyCallback != null && z2) {
            captureBusyCallback.onCaptureCompleted(z);
            this.mCaptureBusyCallback = null;
        }
    }

    private void prepareDepthImageReader(CameraSize cameraSize) {
        closeDepthImageReader();
        AnonymousClass6 r0 = new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                Image acquireNextImage = imageReader.acquireNextImage();
                if (acquireNextImage != null) {
                    if (MiCamera2.this.mMiCamera2Shot == null) {
                        Log.w(MiCamera2.TAG, "onImageAvailable: NO depth image processor!");
                        acquireNextImage.close();
                        return;
                    }
                    MiCamera2.this.mMiCamera2Shot.onImageReceived(acquireNextImage, 2);
                }
            }
        };
        this.mDepthReader = ImageReader.newInstance(cameraSize.getWidth() / 2, cameraSize.getHeight() / 2, 540422489, 2);
        this.mDepthReader.setOnImageAvailableListener(r0, this.mCameraHandler);
    }

    private void preparePhotoImageReader() {
        preparePhotoImageReader(this.mConfigs.getPhotoSize(), this.mConfigs.getPhotoFormat(), this.mConfigs.getPhotoMaxImages());
    }

    private void preparePhotoImageReader(@NonNull CameraSize cameraSize, int i, int i2) {
        closePhotoImageReader();
        this.mPhotoImageReader = ImageReader.newInstance(cameraSize.getWidth(), cameraSize.getHeight(), i, i2);
        this.mPhotoImageReader.setOnImageAvailableListener(new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                MiCamera2Shot miCamera2Shot;
                ConcurrentLinkedDeque access$700;
                Log.d(MiCamera2.TAG, "onImageAvailable: main");
                Image acquireNextImage = imageReader.acquireNextImage();
                if (acquireNextImage == null) {
                    Log.w(MiCamera2.TAG, "onImageAvailable: null image");
                    return;
                }
                synchronized (MiCamera2.this.mShotQueueLock) {
                    if (!MiCamera2.this.mMiCamera2ShotQueue.isEmpty()) {
                        miCamera2Shot = (MiCamera2Shot) MiCamera2.this.mMiCamera2ShotQueue.peek();
                        if (miCamera2Shot instanceof MiCamera2ShotStill) {
                            if (acquireNextImage.getTimestamp() != ((MiCamera2ShotStill) miCamera2Shot).getTimeStamp()) {
                                miCamera2Shot = MiCamera2.this.replaceCorrectShot(acquireNextImage);
                                String access$000 = MiCamera2.TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("onImageAvailable: mMiCamera2ShotQueue.poll, size:");
                                sb.append(MiCamera2.this.mMiCamera2ShotQueue.size());
                                Log.d(access$000, sb.toString());
                                MiCamera2.this.notifyCaptureBusyCallback(true);
                            } else {
                                access$700 = MiCamera2.this.mMiCamera2ShotQueue;
                            }
                        } else if (miCamera2Shot instanceof MiCamera2ShotBurst) {
                            Log.d(MiCamera2.TAG, "repeating request is ongoing");
                            String access$0002 = MiCamera2.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("onImageAvailable: mMiCamera2ShotQueue.poll, size:");
                            sb2.append(MiCamera2.this.mMiCamera2ShotQueue.size());
                            Log.d(access$0002, sb2.toString());
                            MiCamera2.this.notifyCaptureBusyCallback(true);
                        } else {
                            access$700 = MiCamera2.this.mMiCamera2ShotQueue;
                        }
                        access$700.removeFirst();
                        String access$00022 = MiCamera2.TAG;
                        StringBuilder sb22 = new StringBuilder();
                        sb22.append("onImageAvailable: mMiCamera2ShotQueue.poll, size:");
                        sb22.append(MiCamera2.this.mMiCamera2ShotQueue.size());
                        Log.d(access$00022, sb22.toString());
                        MiCamera2.this.notifyCaptureBusyCallback(true);
                    } else {
                        miCamera2Shot = MiCamera2.this.mMiCamera2Shot;
                    }
                }
                if (miCamera2Shot != null) {
                    miCamera2Shot.onImageReceived(acquireNextImage, 0);
                } else {
                    acquireNextImage.close();
                    Log.w(MiCamera2.TAG, "onImageAvailable: NO main image processor!");
                }
            }
        }, this.mCameraHandler);
    }

    private void preparePortraitRawImageReader(CameraSize cameraSize) {
        closePortraitRawImageReader();
        AnonymousClass7 r0 = new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                Image acquireNextImage = imageReader.acquireNextImage();
                if (acquireNextImage != null) {
                    if (MiCamera2.this.mMiCamera2Shot == null) {
                        Log.w(MiCamera2.TAG, "onImageAvailable: NO portrait image processor!");
                        acquireNextImage.close();
                        return;
                    }
                    MiCamera2.this.mMiCamera2Shot.onImageReceived(acquireNextImage, 1);
                }
            }
        };
        this.mPortraitRawImageReader = ImageReader.newInstance(cameraSize.getWidth(), cameraSize.getHeight(), 256, 2);
        this.mPortraitRawImageReader.setOnImageAvailableListener(r0, this.mCameraHandler);
    }

    private void preparePreviewImageReader() {
        preparePreviewImageReader(this.mConfigs.getAlgorithmPreviewSize(), this.mConfigs.getAlgorithmPreviewFormat(), this.mConfigs.getPreviewMaxImages());
    }

    private void preparePreviewImageReader(@NonNull CameraSize cameraSize, int i, int i2) {
        closePreviewImageReader();
        this.mPreviewImageReader = ImageReader.newInstance(cameraSize.getWidth(), cameraSize.getHeight(), i, i2);
        this.mPreviewImageReader.setOnImageAvailableListener(new O00000o(this), this.mCameraPreviewHandler);
    }

    private void prepareRawImageReader(@NonNull CameraSize cameraSize, int i) {
        int i2 = i > 1 ? 10 : 2;
        ImageReader imageReader = this.mRawImageReader;
        if (!(imageReader == null || (imageReader.getWidth() == cameraSize.getWidth() && this.mRawImageReader.getHeight() == cameraSize.getHeight() && this.mRawImageReader.getMaxImages() == i2))) {
            closeRawImageReader();
        }
        AnonymousClass3 r0 = new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                Log.d(MiCamera2.TAG, "onImageAvailable: raw");
                Image acquireNextImage = imageReader.acquireNextImage();
                if (acquireNextImage != null) {
                    if (MiCamera2.this.mMiCamera2Shot == null) {
                        Log.w(MiCamera2.TAG, "onImageAvailable: NO raw image processor!");
                        acquireNextImage.close();
                        return;
                    }
                    MiCamera2.this.mMiCamera2Shot.onImageReceived(acquireNextImage, 3);
                }
            }
        };
        this.mRawImageReader = ImageReader.newInstance(cameraSize.getWidth(), cameraSize.getHeight(), 32, i2);
        this.mRawImageReader.setOnImageAvailableListener(r0, this.mCameraHandler);
    }

    /* access modifiers changed from: private */
    public void prepareRawImageWriter(@NonNull CameraSize cameraSize, @NonNull Surface surface) {
        ImageWriter imageWriter = this.mRawImageWriter;
        if (imageWriter != null) {
            imageWriter.close();
        }
        AnonymousClass4 r2 = new OnImageReleasedListener() {
            public void onImageReleased(ImageWriter imageWriter) {
                Log.d(MiCamera2.TAG, "The enqueued imaged has be consumed");
            }
        };
        this.mRawImageWriter = ImageWriter.newInstance(surface, 2);
        this.mRawImageWriter.setOnImageReleasedListener(r2, this.mCameraHandler);
    }

    private List prepareRemoteImageReader(@Nullable List list) {
        CameraSize cameraSize;
        int i;
        int i2;
        String str;
        StringBuilder sb;
        String str2;
        int i3;
        int i4;
        if (list == null || list.size() == 0) {
            List<IImageReaderParameterSets> arrayList = list == null ? new ArrayList<>() : list;
            int[] sATSubCameraIds = getSATSubCameraIds();
            boolean z = sATSubCameraIds != null;
            boolean isSupportParallelCameraDevice = this.mCapabilities.isSupportParallelCameraDevice();
            boolean OOoO0O = C0122O00000o.instance().OOoO0O();
            this.mMaxImageBufferSize = getMaxImageBufferSize();
            if (z) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("[SAT] camera list: ");
                sb2.append(Arrays.toString(sATSubCameraIds));
                Log.d(str3, sb2.toString());
                int length = sATSubCameraIds.length;
                CameraSize cameraSize2 = null;
                int i5 = 0;
                int i6 = 0;
                int i7 = 0;
                CameraSize cameraSize3 = null;
                CameraSize cameraSize4 = null;
                CameraSize cameraSize5 = null;
                CameraSize cameraSize6 = null;
                CameraSize cameraSize7 = null;
                while (i6 < length) {
                    int i8 = sATSubCameraIds[i6];
                    int[] iArr = sATSubCameraIds;
                    if (i8 == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                        CameraSize ultraWidePhotoSize = this.mConfigs.getUltraWidePhotoSize();
                        i3 = length;
                        IImageReaderParameterSets iImageReaderParameterSets = new IImageReaderParameterSets(ultraWidePhotoSize.getWidth(), ultraWidePhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 4);
                        iImageReaderParameterSets.setPhysicCameraId(i8);
                        arrayList.add(iImageReaderParameterSets);
                        i4 = i7 + 1;
                        this.mUltraWideParallelSurfaceIndex = i7;
                        if (isSupportParallelCameraDevice) {
                            IImageReaderParameterSets iImageReaderParameterSets2 = new IImageReaderParameterSets(ultraWidePhotoSize.getWidth(), ultraWidePhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 4);
                            iImageReaderParameterSets2.isParallel = true;
                            iImageReaderParameterSets2.setPhysicCameraId(i8);
                            arrayList.add(iImageReaderParameterSets2);
                            i4++;
                            int i9 = i5 + 1;
                            ParallelSnapshotManager.getInstance().setSurfaceIndex(1, i5);
                            i5 = i9;
                        }
                        if (OOoO0O) {
                            CameraSize rawSizeOfUltraWide = this.mConfigs.getRawSizeOfUltraWide();
                            if (rawSizeOfUltraWide != null && (cameraSize2 == null || cameraSize2.compareTo(rawSizeOfUltraWide) < 0)) {
                                cameraSize2 = rawSizeOfUltraWide;
                            }
                        }
                        cameraSize4 = ultraWidePhotoSize;
                    } else {
                        i3 = length;
                        if (i8 == Camera2DataContainer.getInstance().getMainBackCameraId()) {
                            CameraSize widePhotoSize = this.mConfigs.getWidePhotoSize();
                            IImageReaderParameterSets iImageReaderParameterSets3 = new IImageReaderParameterSets(widePhotoSize.getWidth(), widePhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 3);
                            iImageReaderParameterSets3.setPhysicCameraId(i8);
                            arrayList.add(iImageReaderParameterSets3);
                            int i10 = i7 + 1;
                            this.mWideParallelSurfaceIndex = i7;
                            if (C0122O00000o.instance().OOoO0oO()) {
                                cameraSize3 = this.mConfigs.getSATRemosicPhotoSize();
                                IImageReaderParameterSets iImageReaderParameterSets4 = new IImageReaderParameterSets(cameraSize3.getWidth(), cameraSize3.getHeight(), 35, this.mMaxImageBufferSize, 0, 3);
                                iImageReaderParameterSets4.setPhysicCameraId(i8);
                                arrayList.add(iImageReaderParameterSets4);
                                int i11 = i10 + 1;
                                this.mSATRemosicParallelSurfaceIndex = i10;
                                i10 = i11;
                            }
                            if (isSupportParallelCameraDevice) {
                                IImageReaderParameterSets iImageReaderParameterSets5 = new IImageReaderParameterSets(widePhotoSize.getWidth(), widePhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 3);
                                iImageReaderParameterSets5.isParallel = true;
                                iImageReaderParameterSets5.setPhysicCameraId(i8);
                                arrayList.add(iImageReaderParameterSets5);
                                i10++;
                                int i12 = i5 + 1;
                                ParallelSnapshotManager.getInstance().setSurfaceIndex(2, i5);
                                i5 = i12;
                            }
                            if (OOoO0O) {
                                CameraSize rawSizeOfWide = this.mConfigs.getRawSizeOfWide();
                                if (rawSizeOfWide != null && (cameraSize2 == null || cameraSize2.compareTo(rawSizeOfWide) < 0)) {
                                    cameraSize2 = rawSizeOfWide;
                                }
                            }
                            cameraSize5 = widePhotoSize;
                        } else if (i8 == Camera2DataContainer.getInstance().getAuxCameraId()) {
                            CameraSize telePhotoSize = this.mConfigs.getTelePhotoSize();
                            IImageReaderParameterSets iImageReaderParameterSets6 = new IImageReaderParameterSets(telePhotoSize.getWidth(), telePhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 5);
                            iImageReaderParameterSets6.setPhysicCameraId(i8);
                            arrayList.add(iImageReaderParameterSets6);
                            int i13 = i7 + 1;
                            this.mTeleParallelSurfaceIndex = i7;
                            if (isSupportParallelCameraDevice) {
                                IImageReaderParameterSets iImageReaderParameterSets7 = new IImageReaderParameterSets(telePhotoSize.getWidth(), telePhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 5);
                                i13++;
                                iImageReaderParameterSets7.isParallel = true;
                                iImageReaderParameterSets7.setPhysicCameraId(i8);
                                arrayList.add(iImageReaderParameterSets7);
                                int i14 = i5 + 1;
                                ParallelSnapshotManager.getInstance().setSurfaceIndex(3, i5);
                                int i15 = i14;
                            }
                            if (OOoO0O) {
                                CameraSize rawSizeOfTele = this.mConfigs.getRawSizeOfTele();
                                if (rawSizeOfTele != null && (cameraSize2 == null || cameraSize2.compareTo(rawSizeOfTele) < 0)) {
                                    CameraSize cameraSize8 = rawSizeOfTele;
                                }
                            }
                            cameraSize6 = telePhotoSize;
                        } else if (i8 == Camera2DataContainer.getInstance().getUltraTeleCameraId()) {
                            CameraSize standalonePhotoSize = this.mConfigs.getStandalonePhotoSize();
                            IImageReaderParameterSets iImageReaderParameterSets8 = new IImageReaderParameterSets(standalonePhotoSize.getWidth(), standalonePhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 6);
                            iImageReaderParameterSets8.setPhysicCameraId(i8);
                            arrayList.add(iImageReaderParameterSets8);
                            int i16 = i7 + 1;
                            this.mUltraTeleParallelSurfaceIndex = i7;
                            if (isSupportParallelCameraDevice) {
                                IImageReaderParameterSets iImageReaderParameterSets9 = new IImageReaderParameterSets(standalonePhotoSize.getWidth(), standalonePhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 6);
                                i16++;
                                iImageReaderParameterSets9.isParallel = true;
                                iImageReaderParameterSets9.setPhysicCameraId(i8);
                                arrayList.add(iImageReaderParameterSets9);
                                int i17 = i5 + 1;
                                ParallelSnapshotManager.getInstance().setSurfaceIndex(4, i5);
                                int i18 = i17;
                            }
                            if (OOoO0O) {
                                CameraSize rawSizeOfUltraTele = this.mConfigs.getRawSizeOfUltraTele();
                                if (rawSizeOfUltraTele != null && (cameraSize2 == null || cameraSize2.compareTo(rawSizeOfUltraTele) < 0)) {
                                    CameraSize cameraSize9 = rawSizeOfUltraTele;
                                }
                            }
                            cameraSize7 = standalonePhotoSize;
                        } else {
                            if (i8 == Camera2DataContainer.getInstance().getStandaloneMacroCameraId()) {
                                CameraSize macroPhotoSize = this.mConfigs.getMacroPhotoSize();
                                IImageReaderParameterSets iImageReaderParameterSets10 = new IImageReaderParameterSets(macroPhotoSize.getWidth(), macroPhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 2);
                                iImageReaderParameterSets10.setPhysicCameraId(i8);
                                arrayList.add(iImageReaderParameterSets10);
                                int i19 = i7 + 1;
                                this.mMacroParallelSurfaceIndex = i7;
                                if (OOoO0O) {
                                    CameraSize rawSizeOfMacro = this.mConfigs.getRawSizeOfMacro();
                                    if (rawSizeOfMacro != null && (cameraSize2 == null || cameraSize2.compareTo(rawSizeOfMacro) < 0)) {
                                        i7 = i19;
                                        cameraSize2 = rawSizeOfMacro;
                                    }
                                }
                                i7 = i19;
                            }
                            i6++;
                            length = i3;
                            sATSubCameraIds = iArr;
                        }
                    }
                    i7 = i4;
                    i6++;
                    length = i3;
                    sATSubCameraIds = iArr;
                }
                CameraSize fakeTelePhotoSize = this.mConfigs.getFakeTelePhotoSize();
                if (!this.mCapabilities.supportFakeSat() || Objects.isNull(fakeTelePhotoSize)) {
                    i = i7;
                } else {
                    IImageReaderParameterSets iImageReaderParameterSets11 = new IImageReaderParameterSets(fakeTelePhotoSize.getWidth(), fakeTelePhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 5);
                    iImageReaderParameterSets11.setPhysicCameraId(Camera2DataContainer.getInstance().getMainBackCameraId());
                    arrayList.add(iImageReaderParameterSets11);
                    i = i7 + 1;
                    this.mFakeTeleParallelSurfaceIndex = i7;
                }
                if (C0122O00000o.instance().OOOOoOo()) {
                    Log.d(TAG, String.format(Locale.ENGLISH, "[4SAT]prepareRemoteImageReader:uwSize = %s wideSize = %s teleSize = %s ultraTeleSize = %s", new Object[]{cameraSize4, cameraSize5, cameraSize6, cameraSize7}));
                    if (OOoO0O) {
                        str2 = TAG;
                        sb = new StringBuilder();
                        str = "prepareRemoteImageReader: [4SAT]rawSize = ";
                    }
                    cameraSize = cameraSize2;
                } else {
                    Log.d(TAG, String.format(Locale.ENGLISH, "[3SAT]prepareRemoteImageReader:uwSize = %s wideSize = %s teleSize = %s fakeTeleSize = %s satRemosic = %s", new Object[]{cameraSize4, cameraSize5, cameraSize6, fakeTelePhotoSize, cameraSize3}));
                    if (OOoO0O) {
                        str2 = TAG;
                        sb = new StringBuilder();
                        str = "prepareRemoteImageReader: [3SAT]rawSize = ";
                    }
                    cameraSize = cameraSize2;
                }
                sb.append(str);
                sb.append(cameraSize2);
                Log.d(str2, sb.toString());
                cameraSize = cameraSize2;
            } else if ((!this.mCapabilities.isQcfaMode() || !C0122O00000o.instance().OO0Oooo()) && (this.mRawCallbackType & 8) == 0) {
                CameraSize photoSize = this.mConfigs.getPhotoSize();
                IImageReaderParameterSets iImageReaderParameterSets12 = new IImageReaderParameterSets(photoSize.getWidth(), photoSize.getHeight(), 35, this.mMaxImageBufferSize, 0, 1);
                arrayList.add(iImageReaderParameterSets12);
                this.mWideParallelSurfaceIndex = 0;
                String str4 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("prepareRemoteImageReader: mainSize = ");
                sb3.append(photoSize);
                Log.d(str4, sb3.toString());
                if (!OOoO0O || this.mRawCallbackType != 0) {
                    cameraSize = null;
                } else {
                    cameraSize = getSensorRawImageSize();
                    String str5 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("prepareRemoteImageReader: rawSize = ");
                    sb4.append(cameraSize);
                    Log.d(str5, sb4.toString());
                }
                i = 1;
            } else if ((this.mRawCallbackType & 8) != 0) {
                cameraSize = getSensorRawImageSize();
                String str6 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("raw supernight: rawSize = ");
                sb5.append(cameraSize);
                Log.d(str6, sb5.toString());
                i = 0;
            } else {
                i = 0;
                cameraSize = null;
            }
            if (this.mConfigs.isParallelDualShotType()) {
                CameraSize subPhotoSize = this.mConfigs.getSubPhotoSize();
                IImageReaderParameterSets iImageReaderParameterSets13 = new IImageReaderParameterSets(subPhotoSize.getWidth(), subPhotoSize.getHeight(), 35, this.mMaxImageBufferSize, 1, 1);
                arrayList.add(iImageReaderParameterSets13);
                int i20 = i + 1;
                this.mSubParallelSurfaceIndex = i;
                String str7 = TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("prepareRemoteImageReader: subSize = ");
                sb6.append(subPhotoSize);
                Log.d(str7, sb6.toString());
                i2 = i20;
            } else if (!isQcfaEnable() || alwaysUseRemosaicSize()) {
                i2 = i;
            } else {
                int i21 = this.mConfigs.getBinningPhotoSize().width;
                int i22 = this.mConfigs.getBinningPhotoSize().height;
                String str8 = TAG;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("prepareRemoteImageReader: qcfaSize = ");
                sb7.append(i21);
                sb7.append("x");
                sb7.append(i22);
                Log.d(str8, sb7.toString());
                IImageReaderParameterSets iImageReaderParameterSets14 = new IImageReaderParameterSets(i21, i22, 35, this.mMaxImageBufferSize, 0, 1);
                iImageReaderParameterSets14.setShouldHoldImages(false);
                arrayList.add(iImageReaderParameterSets14);
                i2 = i + 1;
                this.mQcfaParallelSurfaceIndex = i;
            }
            if (C0122O00000o.instance().OOo0oOO()) {
                if (!(cameraSize == null && (this.mRawCallbackType & 8) == 0)) {
                    IImageReaderParameterSets iImageReaderParameterSets15 = new IImageReaderParameterSets(cameraSize.getWidth(), cameraSize.getHeight(), 32, this.mMaxImageBufferSize, 0, 1);
                    arrayList.add(iImageReaderParameterSets15);
                    int i23 = i2 + 1;
                    this.mRawSurfaceIndex = i2;
                    String str9 = TAG;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("prepareRemoteImageReader: rawBufferSize = ");
                    sb8.append(cameraSize);
                    Log.d(str9, sb8.toString());
                    CameraSize rawSizeOfTuningBuffer = this.mConfigs.getRawSizeOfTuningBuffer();
                    if (rawSizeOfTuningBuffer == null || rawSizeOfTuningBuffer.isEmpty()) {
                        i2 = i23;
                    } else {
                        IImageReaderParameterSets iImageReaderParameterSets16 = new IImageReaderParameterSets(rawSizeOfTuningBuffer.getWidth(), rawSizeOfTuningBuffer.getHeight(), IjkMediaPlayer.SDL_FCC_YV12, this.mMaxImageBufferSize, 2, 1);
                        arrayList.add(iImageReaderParameterSets16);
                        int i24 = i23 + 1;
                        this.mRawSurfaceIndexForTuningBuffer = i23;
                        String str10 = TAG;
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("prepareRemoteImageReader: rawTuningSize = ");
                        sb9.append(rawSizeOfTuningBuffer);
                        Log.d(str10, sb9.toString());
                        i2 = i24;
                    }
                }
                CameraSize tuningBufferSize = this.mConfigs.getTuningBufferSize();
                if (tuningBufferSize != null && !tuningBufferSize.isEmpty() && (this.mRawCallbackType & 8) == 0) {
                    IImageReaderParameterSets iImageReaderParameterSets17 = new IImageReaderParameterSets(tuningBufferSize.getWidth(), tuningBufferSize.getHeight(), IjkMediaPlayer.SDL_FCC_YV12, this.mMaxImageBufferSize, 2, 1);
                    arrayList.add(iImageReaderParameterSets17);
                    this.mTuningBufferSurfaceIndex = i2;
                    String str11 = TAG;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("prepareRemoteImageReader: yuvTuningSize = ");
                    sb10.append(tuningBufferSize);
                    Log.d(str11, sb10.toString());
                }
            }
            LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder(true);
            if (localBinder == null) {
                Log.d(TAG, "prepareRemoteImageReader: ParallelService is not ready");
                ArrayList arrayList2 = new ArrayList();
                for (IImageReaderParameterSets iImageReaderParameterSets18 : arrayList) {
                    ImageReader newInstance = ImageReader.newInstance(iImageReaderParameterSets18.width, iImageReaderParameterSets18.height, iImageReaderParameterSets18.format, iImageReaderParameterSets18.maxImages);
                    arrayList2.add(newInstance.getSurface());
                    this.mRemoteImageReaderList.add(newInstance);
                }
                return arrayList2;
            }
            try {
                String str12 = TAG;
                StringBuilder sb11 = new StringBuilder();
                sb11.append("prepareRemoteImageReader: configurations: ");
                sb11.append(arrayList);
                Log.d(str12, sb11.toString());
                List configCaptureOutputBuffer = localBinder.configCaptureOutputBuffer(arrayList, this.mConfigs.getActivityHashCode());
                if (configCaptureOutputBuffer != null) {
                    return configCaptureOutputBuffer;
                }
                throw new RemoteException("Config capture output buffer failed!");
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.k(6, TAG, e.getMessage());
                return null;
            }
        } else {
            throw new IllegalArgumentException("The given \"params\" should be null or an empty list");
        }
    }

    private void prepareVideoSnapshotImageReader(CameraSize cameraSize) {
        closeVideoSnapshotImageReader();
        AnonymousClass5 r0 = new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                Image acquireNextImage = imageReader.acquireNextImage();
                if (acquireNextImage != null) {
                    if (MiCamera2.this.mMiCamera2Shot == null) {
                        Log.w(MiCamera2.TAG, "onImageAvailable: NO video image processor!");
                        acquireNextImage.close();
                        return;
                    }
                    MiCamera2.this.mMiCamera2Shot.onImageReceived(acquireNextImage, 0);
                }
            }
        };
        this.mVideoSnapshotImageReader = ImageReader.newInstance(cameraSize.getWidth(), cameraSize.getHeight(), 256, 2);
        this.mVideoSnapshotImageReader.setOnImageAvailableListener(r0, this.mCameraHandler);
    }

    /* access modifiers changed from: private */
    public MiCamera2Shot replaceCorrectShot(Image image) {
        Iterator it = this.mMiCamera2ShotQueue.iterator();
        while (it.hasNext()) {
            MiCamera2Shot miCamera2Shot = (MiCamera2Shot) it.next();
            if ((miCamera2Shot instanceof MiCamera2ShotStill) && ((MiCamera2ShotStill) miCamera2Shot).getTimeStamp() == image.getTimestamp()) {
                it.remove();
                return miCamera2Shot;
            }
        }
        return (MiCamera2Shot) this.mMiCamera2ShotQueue.pollFirst();
    }

    private void reset() {
        Log.v(TAG, "E: reset");
        this.mIsCaptureSessionClosed = true;
        synchronized (this.mSessionLock) {
            this.mCaptureSession = null;
        }
        this.mCameraDevice = null;
        this.mPreviewSurface = null;
        this.mZoomMapSurface = null;
        this.mDeferPreviewSurface = null;
        this.mRecordSurface = null;
        this.mSessionId = 0;
        this.mPhotoImageReader = null;
        this.mRawImageReader = null;
        this.mPreviewImageReader = null;
        this.mVideoSnapshotImageReader = null;
        this.mDepthReader = null;
        this.mPortraitRawImageReader = null;
        releaseCameraPreviewCallback(null);
        resetShotQueue(BaseEvent.RESET);
        MemoryHelper.clear();
        Log.v(TAG, "X: reset");
    }

    private void resetShotQueue(String str) {
        synchronized (this.mShotQueueLock) {
            if (!this.mMiCamera2ShotQueue.isEmpty()) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("resetShotQueue !!! ");
                sb.append(str);
                sb.append(" size:");
                sb.append(this.mMiCamera2ShotQueue.size());
                Log.d(str2, sb.toString());
                Iterator it = this.mMiCamera2ShotQueue.iterator();
                while (it.hasNext()) {
                    ((MiCamera2Shot) it.next()).makeClobber();
                }
                this.mMiCamera2ShotQueue.clear();
                notifyCaptureBusyCallback(false);
            }
        }
    }

    /* access modifiers changed from: private */
    public void runCaptureSequence() {
        this.mCaptureCallback.showAutoFocusFinish(true);
        if (C0124O00000oO.isMTKPlatform() || getExposureTime() <= 0) {
            this.mCaptureCallback.setState(8);
            captureStillPicture();
            return;
        }
        waitFlashClosed();
    }

    /* access modifiers changed from: private */
    public void runPreCaptureSequence() {
        Log.v(TAG, "runPreCaptureSequence");
        try {
            Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(1);
            createCaptureRequest.addTarget(this.mPreviewSurface);
            applySettingsForPreCapture(createCaptureRequest);
            CaptureRequest build = createCaptureRequest.build();
            this.mPreCaptureRequestHashCode = build.hashCode();
            this.mCaptureCallback.setState(6);
            capture(build, this.mCaptureCallback, this.mCameraHandler, null);
        } catch (CameraAccessException | IllegalStateException e) {
            e.printStackTrace();
            Log.k(6, TAG, e.getMessage());
            notifyOnError(-1);
        }
    }

    private void setAFModeToPreview(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAFModeToPreview: focusMode=");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(i));
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(0));
        CaptureRequestBuilder.applyAFRegions(this.mPreviewRequestBuilder, this.mConfigs);
        CaptureRequestBuilder.applyAERegions(this.mPreviewRequestBuilder, this.mConfigs);
        CaptureRequestBuilder.applySessionParameters(this.mPreviewRequestBuilder, this.mSessionConfigs);
        resumePreview();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x002e, code lost:
        if (r1.isValid() != false) goto L_0x0020;
     */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0035  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setVideoRecordControl(int i) {
        Surface surface;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setVideoRecordControl: ");
        sb.append(i);
        Log.d(str, sb.toString());
        Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(3);
        if (1 != i) {
            Surface surface2 = this.mPreviewSurface;
            if (surface2 != null) {
            }
            surface = this.mRecordSurface;
            if (surface != null) {
                createCaptureRequest.addTarget(surface);
            }
            applySettingsForVideo(createCaptureRequest);
            VendorTagHelper.setValue(createCaptureRequest, CaptureRequestVendorTags.VIDEO_RECORD_CONTROL, Integer.valueOf(i));
            capture(createCaptureRequest.build(), this.mCaptureCallback, this.mCameraHandler, null);
        }
        createCaptureRequest.addTarget(this.mPreviewSurface);
        surface = this.mRecordSurface;
        if (surface != null) {
        }
        applySettingsForVideo(createCaptureRequest);
        VendorTagHelper.setValue(createCaptureRequest, CaptureRequestVendorTags.VIDEO_RECORD_CONTROL, Integer.valueOf(i));
        capture(createCaptureRequest.build(), this.mCaptureCallback, this.mCameraHandler, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:51:0x0163 A[Catch:{ CameraAccessException -> 0x04b1, IllegalStateException -> 0x0494, IllegalArgumentException -> 0x0474, all -> 0x04d2 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startPreviewSessionImpl(Surface surface, int i, int i2, Surface surface2, int i3, boolean z, CameraPreviewCallback cameraPreviewCallback, int i4) {
        byte b;
        CameraDevice cameraDevice;
        CaptureRequest build;
        CaptureSessionStateCallback captureSessionStateCallback;
        Handler handler;
        byte b2;
        byte b3;
        OutputConfiguration outputConfiguration;
        ArrayList arrayList;
        int reason;
        int i5 = i;
        int i6 = i2;
        Surface surface3 = surface2;
        int i7 = i3;
        boolean z2 = z;
        CameraPreviewCallback cameraPreviewCallback2 = cameraPreviewCallback;
        if (checkCameraDevice("startPreviewSession")) {
            int i8 = 0;
            Log.d(TAG, String.format(Locale.ENGLISH, "startPreviewSession: opMode=0x%x previewCallback=%d rawCallbackType=%d", new Object[]{Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i2)}));
            synchronized (this.mSessionLock) {
                try {
                    this.mEnableParallelSession = z2;
                    this.mPreviewSurface = surface;
                    this.mZoomMapSurface = surface3;
                    this.mPreviewCallbackType = i5;
                    this.mRawCallbackType = i6;
                    this.mSessionId = genSessionId();
                    this.mDeferOutputConfigurations.clear();
                    ArrayList arrayList2 = new ArrayList();
                    if (!z2) {
                        if (this.mConfigs.getPhotoSize() != null) {
                            preparePhotoImageReader();
                            arrayList2.add(new OutputConfiguration(this.mPhotoImageReader.getSurface()));
                        }
                        if (this.mConfigs.getShotType() == 2 || this.mConfigs.getShotType() == -3) {
                            prepareDepthImageReader(this.mConfigs.getPhotoSize());
                            arrayList2.add(new OutputConfiguration(this.mDepthReader.getSurface()));
                            preparePortraitRawImageReader(this.mConfigs.getPhotoSize());
                            arrayList2.add(new OutputConfiguration(this.mPortraitRawImageReader.getSurface()));
                        }
                    } else {
                        ArrayList arrayList3 = new ArrayList();
                        this.mParallelCaptureSurfaceList = prepareRemoteImageReader(arrayList3);
                        configMaxParallelRequestNumberLock();
                        boolean isLocalParallelServiceReady = isLocalParallelServiceReady();
                        int size = this.mParallelCaptureSurfaceList.size();
                        while (i8 < size) {
                            if (((IImageReaderParameterSets) arrayList3.get(i8)).isParallel) {
                                arrayList = arrayList3;
                            } else {
                                IImageReaderParameterSets iImageReaderParameterSets = (IImageReaderParameterSets) arrayList3.get(i8);
                                OutputConfiguration outputConfiguration2 = new OutputConfiguration((Surface) this.mParallelCaptureSurfaceList.get(i8));
                                if (C0124O00000oO.isMTKPlatform()) {
                                    arrayList = arrayList3;
                                    if (VERSION.SDK_INT >= 28 && this.mConfigs.isParallelDualShotType() && this.mCapabilities.getFacing() == 1 && i8 < 2) {
                                        setPhysicalCameraIdForPortrait(iImageReaderParameterSets, outputConfiguration2, true);
                                        if (iImageReaderParameterSets.getPhysicCameraId() != -1 && VERSION.SDK_INT >= 30 && this.mCapabilities.supportPhysicCameraId()) {
                                            CompatibilityUtils.setPhysicalCameraId(outputConfiguration2, String.valueOf(iImageReaderParameterSets.getPhysicCameraId()));
                                        }
                                        if (!isLocalParallelServiceReady) {
                                            outputConfiguration2.enableSurfaceSharing();
                                            String str = TAG;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("add surface to deferredOutputConfig: ");
                                            sb.append(outputConfiguration2.getSurface());
                                            Log.d(str, sb.toString());
                                            this.mDeferOutputConfigurations.add(outputConfiguration2);
                                        }
                                        arrayList2.add(outputConfiguration2);
                                    }
                                } else {
                                    arrayList = arrayList3;
                                }
                                if (VERSION.SDK_INT >= 30 && this.mConfigs.isParallelDualShotType() && this.mCapabilities.supportPhysicCameraId() && this.mCapabilities.getFacing() == 1) {
                                    setPhysicalCameraIdForPortrait(iImageReaderParameterSets, outputConfiguration2, false);
                                }
                                CompatibilityUtils.setPhysicalCameraId(outputConfiguration2, String.valueOf(iImageReaderParameterSets.getPhysicCameraId()));
                                if (!isLocalParallelServiceReady) {
                                }
                                arrayList2.add(outputConfiguration2);
                            }
                            i8++;
                            arrayList3 = arrayList;
                        }
                    }
                    this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(i4);
                    applySettingsForPreview(this.mPreviewRequestBuilder);
                    InputConfiguration inputConfiguration = null;
                    this.mCaptureSession = null;
                    this.mIsCaptureSessionClosed = true;
                    if (i5 > 0) {
                        preparePreviewImageReader(this.mConfigs.getAlgorithmPreviewSize(), this.mConfigs.getAlgorithmPreviewFormat(), this.mConfigs.getPreviewMaxImages());
                        arrayList2.add(new OutputConfiguration(this.mPreviewImageReader.getSurface()));
                    }
                    if (!((this.mRawCallbackType & 1) == 0 && (this.mRawCallbackType & 2) == 0 && (this.mRawCallbackType & 4) == 0)) {
                        prepareRawImageReader(this.mConfigs.getSensorRawImageSize(), i6);
                        arrayList2.add(new OutputConfiguration(this.mRawImageReader.getSurface()));
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("startPreviewSession: needsRawStream = true , size = ");
                        sb2.append(this.mRawImageReader.getWidth());
                        sb2.append("x");
                        sb2.append(this.mRawImageReader.getHeight());
                        Log.d(str2, sb2.toString());
                        if ((this.mRawCallbackType & 2) != 0) {
                            int i9 = this.mConfigs.getSensorRawImageSize().width;
                            int i10 = this.mConfigs.getSensorRawImageSize().height;
                            inputConfiguration = new InputConfiguration(i9, i10, 32);
                            String str3 = TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("startPreviewSession: setup input configuration: w = ");
                            sb3.append(i9);
                            sb3.append(", h = ");
                            sb3.append(i10);
                            sb3.append(", fmt = ");
                            sb3.append(32);
                            Log.d(str3, sb3.toString());
                        }
                    }
                    if (surface3 != null) {
                        arrayList2.add(new OutputConfiguration(surface3));
                        Log.d(TAG, "startPreviewSession: enableZoomMap = true");
                    }
                    this.mCameraCloseCallback = cameraPreviewCallback2;
                    this.mCaptureSessionStateCallback = new CaptureSessionStateCallback(this.mSessionId, cameraPreviewCallback2);
                    if (this.mPreviewSurface == null) {
                        CameraSize previewSize = getPreviewSize();
                        this.mFakeOutputTexture = new SurfaceTexture(false);
                        if (this.mSetRepeatingEarly) {
                            this.mFakeOutputTexture.setDefaultBufferSize(previewSize.width, previewSize.height);
                            Surface surface4 = new Surface(this.mFakeOutputTexture);
                            outputConfiguration = new OutputConfiguration(surface4);
                            outputConfiguration.enableSurfaceSharing();
                            this.mPreviewRequestBuilder.addTarget(surface4);
                        } else {
                            outputConfiguration = new OutputConfiguration(new Size(previewSize.width, previewSize.height), SurfaceHolder.class);
                        }
                        this.mDeferOutputConfigurations.add(0, outputConfiguration);
                        arrayList2.add(0, outputConfiguration);
                        b = 0;
                    } else {
                        String str4 = TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("startPreviewSession: add preview surface to HAL: ");
                        sb4.append(this.mPreviewSurface);
                        sb4.append("->");
                        sb4.append(SurfaceUtils.getSurfaceSize(this.mPreviewSurface));
                        Log.k(3, str4, sb4.toString());
                        b = 0;
                        arrayList2.add(0, new OutputConfiguration(this.mPreviewSurface));
                        this.mPreviewRequestBuilder.addTarget(this.mPreviewSurface);
                    }
                    if (C0124O00000oO.isMTKPlatform()) {
                        byte b4 = (this.mRawCallbackType != 0 || !this.mConfigs.isZslEnabled()) ? b : 1;
                        if (b4 != 0) {
                            Log.d(TAG, "turns capture.zsl.mode on");
                            this.mSessionConfigs.set(CaptureRequestVendorTags.ZSL_CAPTURE_MODE, (Object) Byte.valueOf(1));
                            MiCameraCompat.applyZsd(this.mPreviewRequestBuilder, true);
                        }
                        applyAiShutterIfNeed();
                        Log.d(TAG, "turns PQ feature on");
                        this.mSessionConfigs.set(CaptureRequestVendorTags.MTK_CONFIGURE_SETTING_PROPRIETARY, (Object) CaptureRequestVendorTags.MTK_CONFIGURE_SETTING_PROPRIETARY_ON);
                        MiCameraCompat.applyPqFeature(this.mPreviewRequestBuilder, true);
                        if (this.mConfigs.getmMtkPipDevices() != null) {
                            CaptureRequestBuilder.applyMtkPipDevices(this.mPreviewRequestBuilder, this.mConfigs);
                        }
                        Log.d(TAG, "turns SAT crop region feature on");
                        int[] mTKCropRegion = HybridZoomingSystem.toMTKCropRegion(this.mConfigs != null ? this.mConfigs.getZoomRatio() : 1.0f, this.mCapabilities.getActiveArraySize());
                        this.mSessionConfigs.set(CaptureRequestVendorTags.MTK_MULTI_CAM_CONFIG_SCALER_CROP_REGION, (Object) mTKCropRegion);
                        MiCameraCompat.applyCropFeature(this.mPreviewRequestBuilder, mTKCropRegion);
                        applyMtkQuickPreview();
                        if (!C0122O00000o.instance().OOO0oo0() || i7 != 32770) {
                            if (!isIn3OrMoreSatMode()) {
                                if (!isInMultiSurfaceSatMode()) {
                                    b3 = -1;
                                    b = -1;
                                }
                            }
                            b3 = -1;
                        } else {
                            b3 = -1;
                            b = 1;
                        }
                        if (b != b3) {
                            String str5 = TAG;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("applyFeatureMode: ");
                            sb5.append(b);
                            Log.d(str5, sb5.toString());
                            this.mSessionConfigs.set(CaptureRequestVendorTags.MTK_MULTI_CAM_FEATURE_MODE, (Object) Integer.valueOf(b));
                            MiCameraCompat.applyFeatureMode(this.mPreviewRequestBuilder, b);
                        }
                        if (C0122O00000o.instance().OOo0oOO() && i7 != 32776) {
                            Log.d(TAG, "turns tuning buffer on");
                            this.mSessionConfigs.set(CaptureRequestVendorTags.CONTROL_CAPTURE_ISP_META_ENABLE, (Object) Byte.valueOf(2));
                            MiCameraCompat.applyIspMetaEnable(this.mPreviewRequestBuilder, true);
                        }
                        cameraDevice = this.mCameraDevice;
                        build = this.mPreviewRequestBuilder.build();
                        captureSessionStateCallback = this.mCaptureSessionStateCallback;
                        handler = this.mCameraHandler;
                    } else {
                        if (VERSION.SDK_INT >= 30) {
                            if (this.mCapabilities.supportPhysicCameraId()) {
                                this.mSessionConfigs.set(CompatibilityUtils.KEY_CONTROL_EXTENDED_SCENE_MODE, (Object) Integer.valueOf(this.mConfigs.getExtendSceneMode()));
                                CaptureRequestBuilder.applyExtendSceneMode(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
                            }
                            if (this.mCapabilities.isCinematicVideoSupported()) {
                                CaptureSessionConfigurations captureSessionConfigurations = this.mSessionConfigs;
                                VendorTag vendorTag = CaptureRequestVendorTags.CINEMATIC_VIDEO_ENABLED;
                                if (this.mConfigs.isCinematicVideoEnabled()) {
                                    b2 = 1;
                                }
                                captureSessionConfigurations.set(vendorTag, (Object) Byte.valueOf(b2));
                                CaptureRequestBuilder.applyCinematicVideo(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
                            }
                        }
                        CaptureRequestBuilder.applySmoothTransition(this.mPreviewRequestBuilder, this.mCapabilities, true);
                        cameraDevice = this.mCameraDevice;
                        build = this.mPreviewRequestBuilder.build();
                        captureSessionStateCallback = this.mCaptureSessionStateCallback;
                        handler = this.mCameraHandler;
                    }
                    CompatibilityUtils.createCaptureSessionWithSessionConfiguration(cameraDevice, i3, inputConfiguration, arrayList2, build, captureSessionStateCallback, handler);
                } catch (CameraAccessException e) {
                    CameraAccessException cameraAccessException = e;
                    String str6 = TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Failed to start preview session: CAE: ");
                    sb6.append(this.mIsCameraClosed);
                    Log.e(str6, sb6.toString(), (Throwable) cameraAccessException);
                    reason = cameraAccessException.getReason();
                } catch (IllegalStateException e2) {
                    IllegalStateException illegalStateException = e2;
                    String str7 = TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("Failed to start preview session: ISE: ");
                    sb7.append(this.mIsCameraClosed);
                    Log.e(str7, sb7.toString(), (Throwable) illegalStateException);
                    reason = 256;
                } catch (IllegalArgumentException e3) {
                    IllegalArgumentException illegalArgumentException = e3;
                    String str8 = TAG;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("Failed to start preview session: IAE: ");
                    sb8.append(this.mIsCameraClosed);
                    Log.e(str8, sb8.toString(), (Throwable) illegalArgumentException);
                    reason = 256;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return;
        notifyOnError(reason);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x007f, code lost:
        if (r1 > 0) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x008a, code lost:
        if (r1 > 0) goto L_0x008c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void triggerCapture() {
        int i;
        int i2;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isNeedFlashOn:");
        sb.append(isNeedFlashOn());
        Log.d(str, sb.toString());
        boolean isNeedFlashOn = isNeedFlashOn();
        Integer valueOf = Integer.valueOf(1);
        if (isNeedFlashOn) {
            Log.d(TAG, "trigger capture need flash");
            this.mConfigs.setNeedFlash(true);
            if (needOptimizedFlash()) {
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, valueOf);
                this.mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, Integer.valueOf(2));
                CaptureRequestBuilder.applyAELock(this.mPreviewRequestBuilder, false);
                if (this.mCapabilities.isSupportSnapShotTorch()) {
                    MiCameraCompat.applySnapshotTorch(this.mPreviewRequestBuilder, true);
                }
                if (this.mCapabilities.isSupportCustomFlashCurrent() && this.mConfigs.getFlashMode() == 3) {
                    if (this.mConfigs.isFaceExist()) {
                        i = 30;
                        i2 = SystemProperties.getInt("flash_auto_face", -1);
                    } else {
                        i = 120;
                        i2 = SystemProperties.getInt("flash_auto_no_face", -1);
                    }
                    i = i2;
                    CaptureRequestBuilder.applyBackSoftLight(this.mCapabilities, this.mPreviewRequestBuilder, true);
                    this.mConfigs.setFlashCurrent(i);
                    CaptureRequestBuilder.applyFlashCurrent(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
                }
                resumePreview();
                this.mCaptureCallback.setState(10);
            } else {
                if (needScreenLight()) {
                    this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, valueOf);
                    resumePreview();
                }
                triggerPrecapture();
            }
        } else {
            if (this.mConfigs.isMFAfAeLock()) {
                lockFocusInCAF(true);
                setAWBLock(true);
                lockExposure(true, true);
                if ((C0122O00000o.instance().OOo0O0O() && this.mConfigs.isHDREnabled()) || (C0122O00000o.instance().oO0OO0() && CameraSettings.isProAmbilightOpen())) {
                    this.mCaptureCallback.setState(12);
                    return;
                }
            }
            this.mConfigs.setNeedFlash(false);
            captureStillPicture();
        }
    }

    /* access modifiers changed from: private */
    public void triggerDeviceChecking(boolean z, boolean z2) {
        if (C0124O00000oO.O0o0ooO && C0122O00000o.instance().OO0ooOo() && this.mHelperHandler != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("triggerDeviceChecking ");
            sb.append(z);
            Log.d(str, sb.toString());
            Handler handler = this.mHelperHandler;
            if (z) {
                handler.sendMessage(handler.obtainMessage(3, z2 ? 1 : 0, 0));
            } else {
                handler.removeMessages(3);
            }
        }
    }

    private boolean triggerFlashStateTimeLock() {
        boolean z = false;
        if (!this.mSupportFlashTimeLock) {
            return false;
        }
        if (System.currentTimeMillis() - this.mLastFlashTimeMillis < 4000) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void triggerPrecapture() {
        boolean z = this.mConfigs.getISO() == 0 || this.mConfigs.getExposureTime() == 0;
        if (!this.mCapabilities.isAutoFocusSupported() || this.mConfigs.getFocusMode() == 0) {
            if (!z) {
                runCaptureSequence();
                return;
            }
        } else if (!C0122O00000o.instance().OOOoOo() || !z) {
            if (!needOptimizedFlash() || !C0122O00000o.instance().OOOoOoO() || !z) {
                lockFocus();
                return;
            } else {
                this.mCaptureCallback.setState(5);
                return;
            }
        }
        runPreCaptureSequence();
    }

    private void unlockAfAeForMultiFrame() {
        if (this.mConfigs.isMFAfAeLock()) {
            this.mConfigs.setMFAfAeLock(false);
            setAWBLock(false);
            unlockExposure();
            lockFocusInCAF(false);
        }
    }

    private void unlockFocusForCapture() {
        int i;
        String str = "unlockFocusForCapture";
        if (checkCaptureSession(str)) {
            Log.d(TAG, str);
            try {
                Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(1);
                createCaptureRequest.addTarget(this.mPreviewSurface);
                createCaptureRequest.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(2));
                applyCommonSettings(createCaptureRequest, 1);
                CaptureRequestBuilder.applySessionParameters(createCaptureRequest, this.mSessionConfigs);
                capture(createCaptureRequest.build(), this.mCaptureCallback, this.mCameraHandler, null);
                CaptureRequestBuilder.applyFocusMode(this.mPreviewRequestBuilder, this.mConfigs);
                applyFlashMode(this.mPreviewRequestBuilder, 1);
                CaptureRequestBuilder.applyAELock(this.mPreviewRequestBuilder, this.mConfigs.isAELocked());
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(0));
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, Integer.valueOf(0));
                this.mCaptureCallback.setState(1);
                setAFModeToPreview(this.mConfigs.getFocusMode());
            } catch (CameraAccessException e) {
                e.printStackTrace();
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unlockFocusForCapture: ");
                sb.append(e.getMessage());
                Log.k(6, str2, sb.toString());
                i = e.getReason();
            } catch (IllegalStateException e2) {
                Log.e(TAG, "Failed to unlock focus, IllegalState", (Throwable) e2);
                i = 256;
            }
        }
        return;
        notifyOnError(i);
    }

    private boolean useParallelVTCameraSnapshot(boolean z) {
        if ((!isIn3OrMoreSatMode() && !isInMultiSurfaceSatMode()) || !ParallelSnapshotManager.getInstance().isParallelSessionReady() || !this.mCapabilities.isSupportParallelCameraDevice() || !this.mConfigs.isParallelSupportedCaptureMode()) {
            return false;
        }
        if (z) {
            return this.mConfigs.isHDREnabled();
        }
        int satMasterCameraId = getSatMasterCameraId();
        boolean z2 = true;
        if (!(satMasterCameraId == 2 || satMasterCameraId == 1)) {
            z2 = false;
        }
        return z2;
    }

    private void waitFlashClosed() {
        this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(1));
        this.mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, Integer.valueOf(0));
        resumePreview();
        this.mCaptureCallback.setState(9);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0030, code lost:
        if (r0.onPreviewFrame(r3, r2, r2.mConfigs.getDeviceOrientation()) == false) goto L_0x003d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void O00000Oo(ImageReader imageReader) {
        Image acquireNextImage = imageReader.acquireNextImage();
        if (acquireNextImage != null) {
            boolean z = true;
            PreviewCallback previewCallback = getPreviewCallback();
            if (previewCallback != null) {
                z = previewCallback.onPreviewFrame(acquireNextImage, this, this.mConfigs.getDeviceOrientation());
            }
            if (z) {
                if ((this.mPreviewCallbackType & 16) != 0) {
                    PreviewCallback anchorCallback = getAnchorCallback();
                    if (anchorCallback != null) {
                    }
                }
                acquireNextImage.close();
            } else {
                Log.e(TAG, "oooh, someone close the image before anchor");
            }
        }
    }

    public boolean alwaysUseRemosaicSize() {
        return C0124O00000oO.isMTKPlatform();
    }

    public void applyAiShutterEnable(boolean z) {
        if (this.mConfigs.setAiShutterEnable(z)) {
            CaptureRequestBuilder.applyAiShutterEnable(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void applyAiShutterIfNeed() {
        if (this.mCapabilities.isSupportAiShutter()) {
            boolean isAiShutterEnable = this.mConfigs.isAiShutterEnable();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("turns ai shutter on ");
            sb.append(isAiShutterEnable);
            Log.d(str, sb.toString());
            this.mSessionConfigs.set(CaptureRequestVendorTags.XIAOMI_AISHUTTER_FEATURE_ENABLED, (Object) Boolean.valueOf(isAiShutterEnable));
            MiCameraCompat.applyAiShutterEnable(this.mPreviewRequestBuilder, isAiShutterEnable);
        }
    }

    public void applyHighQualityPreferred(boolean z) {
        if (this.mCapabilities.isSupportHighQualityPreferred()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyHighQualityPreferred: ");
            sb.append(z);
            Log.v(str, sb.toString());
            if (this.mConfigs.setHighQualityPreferred(z)) {
                CaptureRequestBuilder.applyHighQualityPreferred(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
            }
        }
    }

    public void applyMtkQuickPreview() {
        Log.d(TAG, "turns quick preview on");
        this.mSessionConfigs.set(CaptureRequestVendorTags.CONTROL_QUICK_PREVIEW, (Object) CaptureRequestVendorTags.CONTROL_QUICK_PREVIEW_ON);
        MiCameraCompat.applyQuickPreview(this.mPreviewRequestBuilder, true);
    }

    /* access modifiers changed from: 0000 */
    public void applySettingsForCapture(Builder builder, int i) {
        if (builder != null) {
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(0));
            applyFlashMode(builder, i);
            applyCommonSettings(builder, i);
            applySettingsForJpeg(builder);
            CaptureRequestBuilder.applyZsl(builder, this.mConfigs);
            CaptureRequestBuilder.applyAntiShake(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyFpsRange(builder, this.mConfigs);
            CameraCapabilities cameraCapabilities = this.mCapabilities;
            boolean z = needScreenLight() || needOptimizedFlash();
            CaptureRequestBuilder.applyBackwardCaptureHint(cameraCapabilities, builder, z);
            if (C0124O00000oO.isMTKPlatform() && needScreenLight()) {
                MiCameraCompat.applyZsl(builder, false);
            }
            CaptureRequestBuilder.applySessionParameters(builder, this.mSessionConfigs);
            if (isIn3OrMoreSatMode()) {
                int satMasterCameraId = getSatMasterCameraId();
                int i2 = this.mLastSatCameraId;
                if (i2 != -1 && i2 != satMasterCameraId) {
                    CaptureRequestBuilder.applyShrinkMemoryMode(builder, this.mCapabilities, 1);
                } else if (MemoryHelper.shouldTrimMemory(hashCode())) {
                    CaptureRequestBuilder.applyShrinkMemoryMode(builder, this.mCapabilities, 2);
                } else {
                    CaptureRequestBuilder.applyShrinkMemoryMode(builder, this.mCapabilities, 0);
                    this.mLastSatCameraId = satMasterCameraId;
                }
                MemoryHelper.resetCapturedNumber(hashCode());
                this.mLastSatCameraId = satMasterCameraId;
            } else if (isAlgoUpUltraPixelMode()) {
                CaptureRequestBuilder.applyShrinkMemoryMode(builder, this.mCapabilities, 1);
            }
            if (C0122O00000o.instance().OOo0oOO()) {
                MiCameraCompat.applyIspMetaType(builder, 2);
            }
            CaptureRequestBuilder.applyAmbilightMode(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyAmbilightAeTarget(builder, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyDepurpleEnable(builder, i, this.mCapabilities, this.mConfigs);
            if (i == 3 && this.mConfigs.isParallelShotType()) {
                CaptureRequestBuilder.applyParallelSnapshot(builder, this.mCapabilities, true);
            }
        }
    }

    public void applySettingsForJpeg(Builder builder) {
        if (builder != null) {
            Location gpsLocation = this.mConfigs.getGpsLocation();
            if (gpsLocation != null) {
                builder.set(CaptureRequest.JPEG_GPS_LOCATION, new Location(gpsLocation));
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("jpegRotation=");
            sb.append(this.mConfigs.getJpegRotation());
            Log.d(str, sb.toString());
            builder.set(CaptureRequest.JPEG_ORIENTATION, Integer.valueOf(this.mConfigs.getJpegRotation()));
            CameraSize thumbnailSize = this.mConfigs.getThumbnailSize();
            if (thumbnailSize != null) {
                builder.set(CaptureRequest.JPEG_THUMBNAIL_SIZE, new Size(thumbnailSize.getWidth(), thumbnailSize.getHeight()));
            }
            byte jpegQuality = (byte) this.mConfigs.getJpegQuality();
            builder.set(CaptureRequest.JPEG_THUMBNAIL_QUALITY, Byte.valueOf(jpegQuality));
            builder.set(CaptureRequest.JPEG_QUALITY, Byte.valueOf(jpegQuality));
        }
    }

    /* access modifiers changed from: 0000 */
    public void applySettingsForVideoShot(Builder builder, int i) {
        applySettingsForJpeg(builder);
        CaptureRequestBuilder.applyAERegions(builder, this.mConfigs);
        CaptureRequestBuilder.applyVideoFlash(builder, this.mConfigs);
        CaptureRequestBuilder.applyExposureCompensation(builder, i, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyZoomRatio(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyAntiShake(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyBeautyValues(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyVideoFilterId(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyVideoBokehLevelFront(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyColorRetentionFront(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyVideoBokehLevelBack(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyColorRetentionBack(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyFrontMirror(builder, i, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyAELock(builder, this.mConfigs.isAELocked());
        CaptureRequestBuilder.applyFpsRange(builder, this.mConfigs);
        CaptureRequestBuilder.applyUltraWideLDC(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyMacroMode(builder, i, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applyCinematicPhoto(builder, i, this.mCapabilities, this.mConfigs.isCinematicVideoEnabled());
        CaptureRequestBuilder.applyCinematicVideo(builder, this.mCapabilities, this.mConfigs);
        CaptureRequestBuilder.applySessionParameters(builder, this.mSessionConfigs);
        if (this.mPreviewControl.needForProVideo()) {
            CaptureRequestBuilder.applyAWBMode(builder, this.mConfigs.getAWBMode());
            CaptureRequestBuilder.applyCustomAWB(builder, this.mConfigs.getAwbCustomValue());
            CaptureRequestBuilder.applyIso(builder, 3, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyExposureTime(builder, 3, this.mConfigs);
            CaptureRequestBuilder.applyExposureCompensation(builder, 3, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyFocusMode(builder, this.mConfigs);
            CaptureRequestBuilder.applyFocusDistance(builder, this.mConfigs);
        }
    }

    public void applyVideoHdrMode(boolean z) {
        if (this.mConfigs.setVideoHdrEnable(z)) {
            CaptureRequestBuilder.applyVideoHdrMode(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void cancelContinuousShot() {
    }

    public void cancelFocus(int i) {
        int i2;
        if (checkCaptureSession("cancelFocus")) {
            try {
                Builder initFocusRequestBuilder = initFocusRequestBuilder(i);
                if (initFocusRequestBuilder == null) {
                    Log.w(TAG, "cancelFocus afBuilder == null, return");
                    return;
                }
                initFocusRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(1));
                initFocusRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(2));
                CaptureRequestBuilder.applyZoomRatio(initFocusRequestBuilder, this.mCapabilities, this.mConfigs);
                applyFlashMode(initFocusRequestBuilder, 1);
                CaptureRequestBuilder.applyAWBMode(initFocusRequestBuilder, this.mConfigs.getAWBMode());
                CaptureRequestBuilder.applyCustomAWB(initFocusRequestBuilder, this.mConfigs.getAwbCustomValue());
                CaptureRequestBuilder.applyExposureCompensation(initFocusRequestBuilder, 1, this.mCapabilities, this.mConfigs);
                CaptureRequestBuilder.applyAntiShake(initFocusRequestBuilder, this.mCapabilities, this.mConfigs);
                if (this.mPreviewControl.needForCapture()) {
                    CaptureRequestBuilder.applyContrast(initFocusRequestBuilder, this.mCapabilities, this.mConfigs);
                    CaptureRequestBuilder.applySaturation(initFocusRequestBuilder, this.mConfigs);
                    CaptureRequestBuilder.applySharpness(initFocusRequestBuilder, this.mConfigs);
                }
                if (this.mPreviewControl.needForManually()) {
                    CaptureRequestBuilder.applyIso(initFocusRequestBuilder, 1, this.mCapabilities, this.mConfigs);
                    CaptureRequestBuilder.applyExposureTime(initFocusRequestBuilder, 1, this.mConfigs);
                }
                if (this.mPreviewControl.needForProVideo()) {
                    CaptureRequestBuilder.applyAWBMode(initFocusRequestBuilder, this.mConfigs.getAWBMode());
                    CaptureRequestBuilder.applyCustomAWB(initFocusRequestBuilder, this.mConfigs.getAwbCustomValue());
                    CaptureRequestBuilder.applyIso(initFocusRequestBuilder, 3, this.mCapabilities, this.mConfigs);
                    CaptureRequestBuilder.applyExposureTime(initFocusRequestBuilder, 3, this.mConfigs);
                    CaptureRequestBuilder.applyExposureCompensation(initFocusRequestBuilder, 3, this.mCapabilities, this.mConfigs);
                    CaptureRequestBuilder.applyFocusMode(initFocusRequestBuilder, this.mConfigs);
                    CaptureRequestBuilder.applyFocusDistance(initFocusRequestBuilder, this.mConfigs);
                }
                CaptureRequestBuilder.applyFpsRange(initFocusRequestBuilder, this.mConfigs);
                if (this.mConfigs.getmMtkPipDevices() != null) {
                    CaptureRequestBuilder.applyMtkPipDevices(initFocusRequestBuilder, this.mConfigs);
                }
                capture(initFocusRequestBuilder.build(), this.mCaptureCallback, this.mCameraHandler, null);
                this.mConfigs.setAERegions(null);
                this.mConfigs.setAFRegions(null);
                setAFModeToPreview(this.mConfigs.getFocusMode());
            } catch (CameraAccessException e) {
                e.printStackTrace();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("cancelFocus: ");
                sb.append(e.getMessage());
                Log.k(6, str, sb.toString());
                i2 = e.getReason();
                notifyOnError(i2);
            } catch (IllegalStateException e2) {
                Log.e(TAG, "Failed to cancel focus, IllegalState", (Throwable) e2);
                i2 = 256;
                notifyOnError(i2);
            }
        }
    }

    public void cancelSession() {
        String str;
        String str2;
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("cancelSession: id=");
        sb.append(getId());
        Log.d(str3, sb.toString());
        this.mIsCaptureSessionClosed = true;
        try {
            this.mSessionId = genSessionId();
            synchronized (this.mSessionLock) {
                if (this.mCaptureSession != null) {
                    this.mCaptureSession.stopRepeating();
                    abortCaptures();
                    if (this.mCaptureSession instanceof CameraConstrainedHighSpeedCaptureSession) {
                        this.mCaptureSession.replaceSessionClose();
                    } else {
                        this.mCaptureSession.replaceSessionClose();
                    }
                    String str4 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("cancelSession: reset session ");
                    sb2.append(this.mCaptureSession);
                    Log.d(str4, sb2.toString());
                    this.mCaptureSession = null;
                }
            }
            resetConfigs();
            return;
        } catch (CameraAccessException e) {
            e = e;
            str2 = TAG;
            str = "Failed to stop repeating session";
        } catch (IllegalStateException e2) {
            e = e2;
            str2 = TAG;
            str = "Failed to stop repeating, IllegalState";
        }
        Log.e(str2, str, e);
    }

    public void captureAbortBurst() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("captureAbortBurst: shot queue size: ");
        sb.append(this.mMiCamera2ShotQueue.size());
        Log.d(str, sb.toString());
        synchronized (this.mSessionLock) {
            if (this.mCaptureSession == null || this.mIsCameraClosed) {
                String str2 = TAG;
                String str3 = "captureAbortBurst: session is null %s, cameraDevice is close %s";
                Object[] objArr = new Object[2];
                objArr[0] = Boolean.valueOf(this.mCaptureSession == null);
                objArr[1] = Boolean.valueOf(this.mIsCameraClosed);
                Log.w(str2, str3, objArr);
                return;
            }
            try {
                this.mCaptureSession.stopRepeating();
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.k(6, TAG, e.getMessage());
                notifyOnError(e.getReason());
            } catch (IllegalStateException e2) {
                Log.e(TAG, "Failed to abort burst, IllegalState", (Throwable) e2);
                notifyOnError(256);
            }
        }
    }

    public void captureBurstPictures(int i, @NonNull PictureCallback pictureCallback, @NonNull ParallelCallback parallelCallback) {
        if (this.mConfigs.getShotType() == 9) {
            if (isIn3OrMoreSatMode() && !C0124O00000oO.isMTKPlatform()) {
                disableSat();
            }
            this.mMiCamera2Shot = new MiCamera2ShotParallelRepeating(this, i, this.mZoomMapSurface);
            this.mMiCamera2Shot.setPictureCallback(pictureCallback);
            this.mMiCamera2Shot.setParallelCallback(parallelCallback);
            this.mMiCamera2ShotQueue.offerLast(this.mMiCamera2Shot);
            this.mCaptureTime = System.currentTimeMillis();
        } else {
            this.mMiCamera2Shot = new MiCamera2ShotBurst(this, i, this.mConfigs.isNeedPausePreview());
            this.mMiCamera2Shot.setPictureCallback(pictureCallback);
            this.mMiCamera2Shot.setParallelCallback(parallelCallback);
            this.mMiCamera2ShotQueue.offerLast(this.mMiCamera2Shot);
        }
        this.mMiCamera2Shot.startShot();
    }

    public void captureGroupShotPictures(@NonNull PictureCallback pictureCallback, @NonNull ParallelCallback parallelCallback, int i, Context context) {
        this.mMiCamera2Shot = new MiCamera2ShotGroup(this, i, context, this.mCaptureCallback.getPreviewCaptureResult());
        this.mMiCamera2Shot.setPictureCallback(pictureCallback);
        this.mMiCamera2Shot.setParallelCallback(parallelCallback);
        this.mMiCamera2Shot.startShot();
    }

    public void captureVideoSnapshot(PictureCallback pictureCallback) {
        this.mMiCamera2Shot = new MiCamera2ShotVideo(this);
        this.mMiCamera2Shot.setPictureCallback(pictureCallback);
        this.mMiCamera2Shot.startShot();
    }

    public boolean close() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("E: close: cameraId = ");
        sb.append(getId());
        Log.k(3, str, sb.toString());
        SuperNightReprocessHandler superNightReprocessHandler = this.mSuperNightReprocessHandler;
        if (superNightReprocessHandler != null) {
            superNightReprocessHandler.cancel();
            this.mSuperNightReprocessHandler.getLooper().quitSafely();
            this.mSuperNightReprocessHandler = null;
        }
        boolean z = true;
        this.mIsCameraClosed = true;
        abortCaptures();
        if (this.mCameraDevice != null) {
            if (C0122O00000o.instance().OOO0O0O() && !C0122O00000o.instance().OO0oO00()) {
                try {
                    this.mCameraDevice.flush();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    Log.k(6, TAG, e.getMessage());
                }
            }
            this.mCameraDevice.close();
        } else {
            z = false;
        }
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
        if (localBinder != null) {
            localBinder.onCameraClosed();
        }
        closePhotoImageReader();
        closePreviewImageReader();
        closeRawImageReader();
        closeVideoSnapshotImageReader();
        closeDepthImageReader();
        closePortraitRawImageReader();
        unregisterPreviewMetadata();
        MiCamera2Shot miCamera2Shot = this.mMiCamera2Shot;
        if (miCamera2Shot != null) {
            miCamera2Shot.makeClobber();
            this.mMiCamera2Shot = null;
        }
        reset();
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("X: close: cameraId = ");
        sb2.append(getId());
        Log.k(3, str2, sb2.toString());
        return z;
    }

    public void enableSat() {
        Log.d(TAG, "enableSat: E");
        CaptureRequestBuilder.applySmoothTransition(this.mPreviewRequestBuilder, this.mCapabilities, true);
        resumePreview();
        Log.d(TAG, "enableSat: X");
    }

    public void forceTurnFlashONAndPausePreview() {
        int flashMode = this.mConfigs.getFlashMode();
        this.mConfigs.setFlashMode(2);
        applyFlashMode(this.mPreviewRequestBuilder, 1);
        resumePreview();
        this.mCaptureCallback.setState(10);
        this.mConfigs.setFlashMode(flashMode);
    }

    public void forceTurnFlashOffAndPausePreview() {
        this.mConfigs.setFlashMode(0);
        applyFlashMode(this.mPreviewRequestBuilder, 1);
        resumePreview();
        this.mCaptureCallback.setState(11);
    }

    /* access modifiers changed from: protected */
    public CameraSize getActiveRawSize(int i) {
        CameraSize cameraSize;
        if (isIn3OrMoreSatMode() || isInMultiSurfaceSatMode()) {
            if (i != 1) {
                if (i != 2) {
                    if (i == 3) {
                        cameraSize = this.mConfigs.getRawSizeOfTele();
                    } else if (i != 4) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("getActiveRawSize: invalid satMasterCameraId ");
                        sb.append(i);
                        Log.e(str, sb.toString());
                    } else {
                        cameraSize = this.mConfigs.getRawSizeOfUltraTele();
                    }
                }
                cameraSize = this.mConfigs.getRawSizeOfWide();
            } else {
                cameraSize = this.mConfigs.getRawSizeOfUltraWide();
            }
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getActiveRawSize: cameraId = ");
            sb2.append(i);
            sb2.append(", size = ");
            sb2.append(cameraSize);
            Log.d(str2, sb2.toString());
            return cameraSize;
        }
        CameraSize sensorRawImageSize = getSensorRawImageSize();
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("getActiveRawSize: ");
        sb3.append(sensorRawImageSize);
        Log.d(str3, sb3.toString());
        return sensorRawImageSize;
    }

    /* access modifiers changed from: protected */
    public Surface getActiveRawSurface() {
        int i = this.mRawSurfaceIndex;
        if (i != -1) {
            return getRemoteSurface(i);
        }
        return null;
    }

    public int getActivityHashCode() {
        return this.mConfigs.getActivityHashCode();
    }

    public int getAlgorithmPreviewFormat() {
        return this.mConfigs.getAlgorithmPreviewFormat();
    }

    public CameraSize getAlgorithmPreviewSize() {
        return this.mConfigs.getAlgorithmPreviewSize();
    }

    public CameraSize getBinningPictureSize() {
        return this.mConfigs.getBinningPhotoSize();
    }

    public int getBokehAuxCameraId() {
        HashSet hashSet = new HashSet(this.mCapabilities.getPhysicalCameraIds());
        hashSet.remove(String.valueOf(Camera2DataContainer.getInstance().getMainBackCameraId()));
        hashSet.remove(String.valueOf(Camera2DataContainer.getInstance().getFrontCameraId()));
        if (!hashSet.isEmpty()) {
            return Integer.parseInt(((String[]) hashSet.toArray(new String[0]))[0]);
        }
        return -1;
    }

    public CameraSize getBokehDepthSize() {
        return this.mConfigs.getBokehDepthSize();
    }

    public CacheImageDecoder getCacheImageDecoder() {
        return this.mCacheImageDecoder;
    }

    public CameraConfigs getCameraConfigs() {
        return this.mConfigs;
    }

    public CameraDevice getCameraDevice() {
        return this.mCameraDevice;
    }

    public Handler getCameraHandler() {
        return this.mCameraHandler;
    }

    public CameraCapabilities getCapabilities() {
        return this.mCapabilities;
    }

    /* access modifiers changed from: protected */
    public CameraCaptureSession getCaptureSession() {
        return this.mCaptureSession;
    }

    public Integer getCurrentAEState() {
        return this.mCaptureCallback.getCurrentAEState();
    }

    /* access modifiers changed from: protected */
    public ImageReader getDepthImageReader() {
        return this.mDepthReader;
    }

    public int getExposureCompensation() {
        return this.mConfigs.getExposureCompensationIndex();
    }

    public int getExtendSceneMode() {
        return this.mConfigs.getExtendSceneMode();
    }

    public CameraSize getFakeSatOutputSize() {
        if (!this.mCapabilities.supportFakeSat()) {
            return null;
        }
        return this.mConfigs.getFakeSatOutputSize();
    }

    /* access modifiers changed from: protected */
    public Surface getFakeTeleRemoteSurface() {
        return getRemoteSurface(this.mFakeTeleParallelSurfaceIndex);
    }

    public int getFlashMode() {
        return this.mConfigs.getFlashMode();
    }

    public int getFocusMode() {
        return this.mConfigs.getFocusMode();
    }

    public int getHDRMode() {
        return this.mConfigs.getHDRMode();
    }

    /* access modifiers changed from: protected */
    public Surface getMainCaptureSurface(int i) {
        if (!isIn3OrMoreSatMode() && !isInMultiSurfaceSatMode()) {
            return getWideRemoteSurface();
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getMainCaptureSurface: satMasterCameraId = ");
        sb.append(i);
        Log.d(str, sb.toString());
        if (i == 1) {
            return getUltraWideRemoteSurface();
        }
        if (i == 2) {
            return getWideRemoteSurface();
        }
        if (i == 3) {
            return getTeleRemoteSurface();
        }
        if (i == 4) {
            return getUltraTeleRemoteSurface();
        }
        if (i == 5) {
            return getFakeTeleRemoteSurface();
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getMainCaptureSurface: invalid satMasterCameraId ");
        sb2.append(i);
        Log.e(str2, sb2.toString());
        return getWideRemoteSurface();
    }

    public String getParallelShotSavePath() {
        return this.mConfigs.getThumbnailShotPath();
    }

    /* access modifiers changed from: protected */
    public ImageReader getPhotoImageReader() {
        return this.mPhotoImageReader;
    }

    public int getPhysicalBokehMainId() {
        CameraCapabilities cameraCapabilities = this.mCapabilities;
        return (cameraCapabilities == null || cameraCapabilities.getOptimalMasterBokehId() == -1) ? Camera2DataContainer.getInstance().getMainBackCameraId() : this.mCapabilities.getOptimalMasterBokehId();
    }

    public int getPhysicalBokehSubId() {
        CameraCapabilities cameraCapabilities = this.mCapabilities;
        return (cameraCapabilities == null || cameraCapabilities.getOptimalSlaveBokehId() == -1) ? C0124O00000oO.Oo00() ? getBokehAuxCameraId() : Camera2DataContainer.getInstance().getAuxCameraId() : this.mCapabilities.getOptimalSlaveBokehId();
    }

    public int getPictureFormat() {
        return this.mConfigs.getPhotoFormat();
    }

    public int getPictureMaxImages() {
        return this.mConfigs.getPhotoMaxImages();
    }

    public CameraSize getPictureSize() {
        return this.mConfigs.getPhotoSize();
    }

    /* access modifiers changed from: protected */
    public ImageReader getPortraitRawImageReader() {
        return this.mPortraitRawImageReader;
    }

    public int getPreviewCallbackEnabled() {
        return this.mPreviewCallbackType;
    }

    public CaptureResult getPreviewCaptureResult() {
        return this.mCaptureCallback.getPreviewCaptureResult();
    }

    public int getPreviewMaxImages() {
        return this.mConfigs.getPreviewMaxImages();
    }

    /* access modifiers changed from: protected */
    public Builder getPreviewRequestBuilder() {
        return this.mPreviewRequestBuilder;
    }

    public CameraSize getPreviewSize() {
        return this.mConfigs.getPreviewSize();
    }

    /* access modifiers changed from: protected */
    public Surface getPreviewSurface() {
        return this.mPreviewSurface;
    }

    /* access modifiers changed from: protected */
    public Surface getQcfaRemoteSurface() {
        return getRemoteSurface(this.mQcfaParallelSurfaceIndex);
    }

    public int getRawCallbackType() {
        return this.mRawCallbackType;
    }

    /* access modifiers changed from: protected */
    public ImageReader getRawImageReader() {
        return this.mRawImageReader;
    }

    /* access modifiers changed from: protected */
    public ImageWriter getRawImageWriter() {
        return this.mRawImageWriter;
    }

    /* access modifiers changed from: protected */
    public Surface getRawSurface() {
        return this.mRawImageReader.getSurface();
    }

    /* access modifiers changed from: protected */
    public Surface getRawSurfaceForTuningBuffer() {
        int i = this.mRawSurfaceIndexForTuningBuffer;
        if (i != -1) {
            return getRemoteSurface(i);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Surface getRecordSurface() {
        return this.mRecordSurface;
    }

    /* access modifiers changed from: protected */
    public List getRemoteSurfaceList() {
        return this.mParallelCaptureSurfaceList;
    }

    /* access modifiers changed from: protected */
    public Surface getSATRemosicRemoteSurface() {
        return getRemoteSurface(this.mSATRemosicParallelSurfaceIndex);
    }

    public int[] getSATSubCameraIds() {
        int[] iArr;
        if (isInMultiSurfaceSatMode()) {
            Set<String> physicalCameraIds = this.mCapabilities.getPhysicalCameraIds();
            HashMap hashMap = new HashMap(physicalCameraIds.size());
            for (String parseInt : physicalCameraIds) {
                int parseInt2 = Integer.parseInt(parseInt);
                hashMap.put(Integer.valueOf(parseInt2), Float.valueOf(Camera2DataContainer.getInstance().getCapabilities(parseInt2).getViewAngle(false)));
            }
            ArrayList arrayList = new ArrayList(hashMap.keySet());
            arrayList.sort(new C0421O00000oO(hashMap));
            iArr = new int[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                iArr[i] = ((Integer) arrayList.get(i)).intValue();
            }
        } else if (!isIn3OrMoreSatMode()) {
            iArr = null;
        } else if (C0122O00000o.instance().OOOOoOo()) {
            return new int[]{Camera2DataContainer.getInstance().getUltraWideCameraId(), Camera2DataContainer.getInstance().getMainBackCameraId(), Camera2DataContainer.getInstance().getAuxCameraId(), Camera2DataContainer.getInstance().getUltraTeleCameraId()};
        } else {
            return new int[]{Camera2DataContainer.getInstance().getUltraWideCameraId(), Camera2DataContainer.getInstance().getMainBackCameraId(), Camera2DataContainer.getInstance().getAuxCameraId()};
        }
        return iArr;
    }

    public CameraCapabilities getSatCapabilities() {
        int mainBackCameraId = Camera2DataContainer.getInstance().getMainBackCameraId();
        int satMasterCameraId = getSatMasterCameraId();
        if (satMasterCameraId != 1) {
            if (satMasterCameraId != 2) {
                if (satMasterCameraId == 3) {
                    mainBackCameraId = Camera2DataContainer.getInstance().getAuxCameraId();
                } else if (satMasterCameraId == 4) {
                    mainBackCameraId = Camera2DataContainer.getInstance().getUltraTeleCameraId();
                } else if (satMasterCameraId != 5) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getSatCapabilities: invalid satMasterCameraId ");
                    sb.append(mainBackCameraId);
                    Log.e(str, sb.toString());
                }
            }
            mainBackCameraId = Camera2DataContainer.getInstance().getMainBackCameraId();
        } else {
            mainBackCameraId = Camera2DataContainer.getInstance().getUltraWideCameraId();
        }
        return Camera2DataContainer.getInstance().getCapabilities(mainBackCameraId);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0028, code lost:
        if ("raphael".equals(O00000Oo.O00000oO.O000000o.C0124O00000oO.O0Ooo0o) != false) goto L_0x002a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getSatMasterCameraId() {
        String str;
        StringBuilder sb;
        int satMasterCameraId = CaptureResultParser.getSatMasterCameraId(this.mCaptureCallback.getPreviewCaptureResult());
        if (isFakeSatEnable()) {
            satMasterCameraId = 5;
        }
        if (!C0124O00000oO.O0o000o) {
            if (!"davinci".equals(C0124O00000oO.O0Ooo0o)) {
            }
        }
        float zoomRatio = getZoomRatio();
        String str2 = " zoomRatio = ";
        String str3 = "getSatMasterCameraId: error satCameraId = ";
        if (zoomRatio >= 1.0f || satMasterCameraId == 1) {
            if (zoomRatio >= HybridZoomingSystem.getTeleMinZoomRatio() && satMasterCameraId == 1) {
                str = TAG;
                sb = new StringBuilder();
            }
            return satMasterCameraId;
        }
        str = TAG;
        sb = new StringBuilder();
        sb.append(str3);
        sb.append(satMasterCameraId);
        sb.append(str2);
        sb.append(zoomRatio);
        Log.w(str, sb.toString());
        return 2;
    }

    public int getSatPhysicalCameraId() {
        int satMasterCameraId = getSatMasterCameraId();
        int mainBackCameraId = Camera2DataContainer.getInstance().getMainBackCameraId();
        if (satMasterCameraId == 1) {
            return Camera2DataContainer.getInstance().getUltraWideCameraId();
        }
        if (satMasterCameraId != 2) {
            if (satMasterCameraId == 3) {
                return Camera2DataContainer.getInstance().getAuxCameraId();
            }
            if (satMasterCameraId == 4) {
                return Camera2DataContainer.getInstance().getUltraTeleCameraId();
            }
            if (satMasterCameraId != 5) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("getSatCapabilities: invalid satMasterCameraId ");
                sb.append(satMasterCameraId);
                Log.e(str, sb.toString());
                return mainBackCameraId;
            }
        }
        return Camera2DataContainer.getInstance().getMainBackCameraId();
    }

    public int getSceneMode() {
        return this.mConfigs.getSceneMode();
    }

    public CameraSize getSensorRawImageSize() {
        return this.mConfigs.getSensorRawImageSize();
    }

    public CaptureSessionConfigurations getSessionConfigs() {
        return this.mSessionConfigs;
    }

    public Consumer getShotBoostParams() {
        return this.mShotBoostParams;
    }

    /* access modifiers changed from: protected */
    public Surface getSubRemoteSurface() {
        return getRemoteSurface(this.mSubParallelSurfaceIndex);
    }

    public boolean getSuperNight() {
        return this.mConfigs.isSuperNightEnabled();
    }

    /* access modifiers changed from: protected */
    public Surface getTeleRemoteSurface() {
        return getRemoteSurface(this.mTeleParallelSurfaceIndex);
    }

    /* access modifiers changed from: protected */
    public Surface getTuningRemoteSurface() {
        int i = this.mTuningBufferSurfaceIndex;
        if (i != -1) {
            return getRemoteSurface(i);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Surface getUltraTeleRemoteSurface() {
        return getRemoteSurface(this.mUltraTeleParallelSurfaceIndex);
    }

    /* access modifiers changed from: protected */
    public Surface getUltraWideRemoteSurface() {
        return getRemoteSurface(this.mUltraWideParallelSurfaceIndex);
    }

    /* access modifiers changed from: protected */
    public ImageReader getVideoSnapshotImageReader() {
        return this.mVideoSnapshotImageReader;
    }

    /* access modifiers changed from: protected */
    public Surface getWideRemoteSurface() {
        return getRemoteSurface(this.mWideParallelSurfaceIndex);
    }

    public float getZoomRatio() {
        return this.mConfigs.getZoomRatio();
    }

    /* access modifiers changed from: protected */
    public boolean isAlgoUpUltraPixelMode() {
        return 36868 == this.mCapabilities.getOperatingMode();
    }

    /* access modifiers changed from: protected */
    public boolean isBeautyOn() {
        BeautyValues beautyValues = this.mConfigs.getBeautyValues();
        if (beautyValues != null) {
            return beautyValues.isFaceBeautyOn();
        }
        Log.d(TAG, "Assume front beauty is off in case beautyValues is unavailable.");
        return false;
    }

    public boolean isCaptureBusy(boolean z) {
        String str;
        StringBuilder sb;
        String sb2;
        String str2;
        Object obj;
        if (this.mMiCamera2ShotQueue.isEmpty()) {
            return false;
        }
        if (CameraService.hasPendingCallable(101)) {
            str = TAG;
            sb2 = "isCaptureBusy: shotboost going on";
        } else {
            long currentTimeMillis = System.currentTimeMillis() - this.mCaptureTime;
            if (currentTimeMillis > FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("isCaptureBusy: timeout:");
                sb3.append(currentTimeMillis);
                resetShotQueue(sb3.toString());
                return false;
            } else if (z) {
                str = TAG;
                sb2 = "isCaptureBusy: simple return true";
            } else {
                PictureCaptureCallback pictureCaptureCallback = this.mCaptureCallback;
                if (!(pictureCaptureCallback == null || pictureCaptureCallback.getPreviewCaptureResult() == null || !CameraSettings.isCameraQuickShotEnable())) {
                    Integer num = (Integer) this.mCaptureCallback.getPreviewCaptureResult().get(CaptureResult.SENSOR_SENSITIVITY);
                    if (num == 0 || num.intValue() >= 800) {
                        str = TAG;
                        sb = new StringBuilder();
                        str2 = "isCaptureBusy: iso:";
                        obj = num;
                    } else if (this.mCapabilities.isSensorHdrSupported()) {
                        Byte b = (Byte) VendorTagHelper.getValue(this.mCaptureCallback.getPreviewCaptureResult(), CaptureResultVendorTags.SENSOR_HDR_ENABLE);
                        if (b != 0 && b.byteValue() > 0) {
                            str = TAG;
                            sb = new StringBuilder();
                            str2 = "isCaptureBusy: sensorHdr:";
                            obj = b;
                        }
                    }
                    sb.append(str2);
                    sb.append(obj);
                    sb2 = sb.toString();
                }
                if (currentTimeMillis < 50) {
                    str = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("isCaptureBusy: time:");
                    sb4.append(currentTimeMillis);
                    sb2 = sb4.toString();
                } else {
                    int size = this.mMiCamera2ShotQueue.size();
                    if (size < this.DEF_QUICK_SHOT_THRESHOLD_SHOT_CACHE_COUNT) {
                        return false;
                    }
                    str = TAG;
                    sb = new StringBuilder();
                    sb.append("isCaptureBusy: size:");
                    sb.append(size);
                    sb2 = sb.toString();
                }
            }
        }
        Log.d(str, sb2);
        return true;
    }

    public boolean isDisconnected() {
        return this.mIsCameraClosed;
    }

    public boolean isFacingFront() {
        return this.mCapabilities.getFacing() == 0;
    }

    public boolean isFakeSatEnable() {
        return CaptureResultParser.isFakeSatEnable(this.mCaptureCallback.getPreviewCaptureResult()) && !this.mConfigs.isHDREnabled() && !this.mConfigs.isNeedFlash() && this.mConfigs.getShotType() != 9;
    }

    /* access modifiers changed from: protected */
    public boolean isIn3OrMoreSatMode() {
        return 36866 == this.mCapabilities.getOperatingMode() && HybridZoomingSystem.IS_3_OR_MORE_SAT && !CameraSettings.isFakePartSAT();
    }

    public boolean isInMultiSurfaceSatMode() {
        if (36866 == this.mCapabilities.getOperatingMode()) {
            CameraCapabilities cameraCapabilities = this.mCapabilities;
            if (cameraCapabilities != null && cameraCapabilities.getPhysicalCameraIds() != null && !this.mCapabilities.getPhysicalCameraIds().isEmpty() && C0124O00000oO.Oo00()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInTimerBurstShotting() {
        return this.mConfigs.isInTimerBurstShotting();
    }

    public boolean isMacroMode() {
        return this.mConfigs.isMacroMode();
    }

    public boolean isModuleAnchorFrame() {
        return this.mConfigs.isModuleAnchorFrame();
    }

    /* access modifiers changed from: protected */
    public boolean isMultiSnapStopRequest() {
        return this.mConfigs.isMultiSnapStopRequest();
    }

    public boolean isNeedFlashForAuto(Integer num, int i) {
        if (num != null && num.intValue() < 0) {
            num = getCurrentAEState();
        }
        if (i < 0) {
            i = getFlashMode();
        }
        if (FLASH_LOCK_DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("flashMode:");
            sb.append(i);
            sb.append(",currentAEState:");
            sb.append(num);
            Log.d(str, sb.toString());
        }
        boolean z = true;
        if (num == null || num.intValue() != 4) {
            z = false;
        }
        if (!C0124O00000oO.Oo00OOo()) {
            z = false;
        }
        if (3 != i) {
            z = false;
        }
        if (triggerFlashStateTimeLock()) {
            if (FLASH_LOCK_DEBUG) {
                Log.d(TAG, "trigger flash state time lock!");
            }
            z = this.mNeedFlashForAuto;
        }
        this.mNeedFlashForAuto = z;
        if (FLASH_LOCK_DEBUG) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mNeedFlashForAuto: ");
            sb2.append(this.mNeedFlashForAuto);
            Log.d(str2, sb2.toString());
        }
        return this.mNeedFlashForAuto;
    }

    public boolean isNeedFlashOn() {
        int flashMode = this.mConfigs.getFlashMode();
        if (flashMode != 1) {
            if (flashMode == 3) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("isNeedFlashOn: auto mode state:  ae:");
                sb.append(this.mCaptureCallback.getCurrentAEState());
                sb.append(", flash:");
                sb.append(this.mCaptureCallback.getCurrentFlashState());
                Log.d(str, sb.toString());
                if (this.mSupportFlashTimeLock) {
                    return this.mNeedFlashForAuto;
                }
                Integer currentAEState = this.mCaptureCallback.getCurrentAEState();
                Integer currentFlashState = this.mCaptureCallback.getCurrentFlashState();
                if (!(currentAEState == null || currentFlashState == null)) {
                    int intValue = currentAEState.intValue();
                    if (intValue != 1 && intValue != 2) {
                        return intValue == 4;
                    }
                    if (currentFlashState.intValue() == 3) {
                        return true;
                    }
                }
                return false;
            } else if (flashMode != 101) {
                return false;
            }
        }
        return true;
    }

    public boolean isNeedPreviewThumbnail() {
        return !this.mConfigs.isHDREnabled() && (this.mConfigs.isMfnrEnabled() || this.mConfigs.isSwMfnrEnabled() || this.mConfigs.isSuperResolutionEnabled());
    }

    public boolean isParallelBusy(boolean z) {
        if (!z && !ParallelSnapshotManager.getInstance().isParallelSessionReady()) {
            Log.e(TAG, "isParallelBusy: Session is null or pending surface list is not null");
            return true;
        } else if (this.mMiCamera2ShotQueue.isEmpty()) {
            return false;
        } else {
            long currentTimeMillis = System.currentTimeMillis() - this.mCaptureTime;
            if (currentTimeMillis > FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME) {
                StringBuilder sb = new StringBuilder();
                sb.append("isParallelBusy: timeout:");
                sb.append(currentTimeMillis);
                resetShotQueue(sb.toString());
                return false;
            }
            Iterator it = this.mMiCamera2ShotQueue.iterator();
            while (it.hasNext()) {
                if (!((MiCamera2Shot) it.next()).isShutterReturned()) {
                    Log.d(TAG, "isParallelBusy: shutter is not return");
                    return true;
                }
            }
            if (z || AlgoConnector.getInstance().getLocalBinder().getFrontProcessingCount() < ParallelSnapshotManager.getInstance().getMaxQueueSize()) {
                return false;
            }
            Log.d(TAG, "isParallelBusy: FrontProcessingCount is full");
            return true;
        }
    }

    public boolean isPreviewReady() {
        return (this.mCaptureCallback.getPreviewCaptureResult() == null || this.mCaptureCallback.getState() == 0) ? false : true;
    }

    public boolean isQcfaEnable() {
        return this.mConfigs.isQcfaEnable();
    }

    public boolean isSessionReady() {
        boolean z;
        synchronized (this.mSessionLock) {
            z = this.mCaptureSession != null;
        }
        return z;
    }

    public boolean isShotQueueMultitasking() {
        ConcurrentLinkedDeque concurrentLinkedDeque = this.mMiCamera2ShotQueue;
        return concurrentLinkedDeque != null && concurrentLinkedDeque.size() > 1;
    }

    public boolean isSingleBokehEnabled() {
        return this.mConfigs.isSingleBokehEnabled();
    }

    public void lockExposure(boolean z) {
        if (checkCaptureSession("lockExposure")) {
            if (z) {
                setAELock(true);
            } else {
                this.mCaptureCallback.setState(4);
            }
            CaptureRequestBuilder.applyAELock(this.mPreviewRequestBuilder, true);
            resumePreview();
        }
    }

    public void lockExposure(boolean z, boolean z2) {
        if (checkCaptureSession("lockExposure")) {
            if (!z2) {
                this.mCaptureCallback.setState(4);
            }
            if (z) {
                setAELock(true);
            }
            CaptureRequestBuilder.applyAELock(this.mPreviewRequestBuilder, true);
            resumePreview();
        }
    }

    public void notifyVideoStreamEnd() {
        try {
            synchronized (this.mSessionLock) {
                if (this.mCaptureSession == null || this.mCameraDevice == null || this.mRecordSurface == null) {
                    Log.w(TAG, "notifyVideoStreamEnd: null session");
                    this.mPendingNotifyVideoEnd = true;
                    return;
                }
                this.mCaptureSession.stopRepeating();
                Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(3);
                Surface surface = (1 != this.mCapabilities.getEndOfStreamType() || this.mPreviewSurface == null || !this.mPreviewSurface.isValid()) ? this.mRecordSurface : this.mPreviewSurface;
                createCaptureRequest.addTarget(surface);
                applySettingsForVideo(createCaptureRequest);
                MiCameraCompat.applyVideoStreamState(createCaptureRequest, false);
                int capture = capture(createCaptureRequest.build(), this.mCaptureCallback, this.mCameraHandler, null);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("notifyVideoStreamEnd: requestId=");
                sb.append(capture);
                Log.v(str, sb.toString());
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage(), (Throwable) e);
            notifyOnError(e.getReason());
        } catch (IllegalArgumentException | IllegalStateException e2) {
            Log.e(TAG, "notifyVideoStreamEnd: ", e2);
        }
    }

    public void onCameraDisconnected() {
        this.mIsCameraClosed = true;
        CameraPreviewCallback cameraPreviewCallback = this.mCameraCloseCallback;
        if (cameraPreviewCallback != null) {
            cameraPreviewCallback.onPreviewRelease();
            this.mCameraCloseCallback = null;
        }
    }

    public void onCapabilityChanged(CameraCapabilities cameraCapabilities) {
        PictureCaptureCallback pictureCaptureCallback = this.mCaptureCallback;
        if (pictureCaptureCallback != null) {
            pictureCaptureCallback.onCapabilityChanged(cameraCapabilities);
        }
    }

    /* access modifiers changed from: protected */
    public void onCapturePictureFinished(boolean z, MiCamera2Shot miCamera2Shot) {
        boolean z2 = true;
        boolean z3 = this.mConfigs.isNeedFlash() || this.mConfigs.isSuperNightEnabled();
        this.mConfigs.setNeedFlash(false);
        if (needUnlockFocusAfterCapture()) {
            unlockFocusForCapture();
        }
        unlockAfAeForMultiFrame();
        this.mCaptureCallback.setState(1);
        applyFlashMode(this.mPreviewRequestBuilder, 7);
        applySettingsForPreview(this.mPreviewRequestBuilder);
        if (z3) {
            resumePreview();
        }
        PictureCallback pictureCallback = miCamera2Shot.getPictureCallback();
        if (pictureCallback != null) {
            pictureCallback.onCaptureCompleted(z);
            if (!z) {
                pictureCallback.onPictureTakenFinished(false);
            } else {
                Consumer shotBoostParams = miCamera2Shot.getShotBoostParams();
                if (shotBoostParams != null) {
                    Log.d(TAG, "do shotboost later");
                    CameraService.removeShotBoostCallable();
                    String valueOf = String.valueOf(this.mActualCameraId);
                    if (this.mMiCamera2ShotQueue.size() >= C0122O00000o.instance().OO00OOO()) {
                        z2 = false;
                    }
                    CameraService.addShotBoostCallableDelayed(new ShotBoostCallable(valueOf, z2, shotBoostParams), getCaptureInterval());
                }
            }
        }
        if (!z) {
            synchronized (this.mShotQueueLock) {
                if (!this.mMiCamera2ShotQueue.isEmpty()) {
                    boolean remove = this.mMiCamera2ShotQueue.remove(miCamera2Shot);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onCapturePictureFinished failure: mMiCamera2ShotQueue.poll, size: ");
                    sb.append(this.mMiCamera2ShotQueue.size());
                    sb.append(" removeResult: ");
                    sb.append(remove);
                    Log.d(str, sb.toString());
                }
                notifyCaptureBusyCallback(false);
            }
        }
    }

    public void onMultiSnapEnd(boolean z, MiCamera2Shot miCamera2Shot) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onMultiSnapEnd: ");
        sb.append(z);
        sb.append(" | ");
        sb.append(miCamera2Shot);
        Log.d(str, sb.toString());
        if (this.mMiCamera2ShotQueue.remove(miCamera2Shot)) {
            notifyCaptureBusyCallback(z);
        }
    }

    public void onParallelImagePostProcStart() {
        synchronized (this.mShotQueueLock) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onParallelImagePostProcStart: mMiCamera2ShotQueue.poll, size:");
            sb.append(this.mMiCamera2ShotQueue.size());
            Log.d(str, sb.toString());
            if (!this.mMiCamera2ShotQueue.isEmpty()) {
                this.mMiCamera2ShotQueue.pollFirst();
            }
            notifyCaptureBusyCallback(true);
        }
    }

    public void onPartialPreviewMetadata(CaptureResult captureResult) {
        PreviewMetadata previewMetadata = this.mCameraPreviewMetadata;
        if (previewMetadata != null) {
            previewMetadata.onPartialPreviewMetadata(captureResult);
        }
    }

    public void onPreviewComing() {
        synchronized (this.mShotQueueLock) {
            if (!this.mMiCamera2ShotQueue.isEmpty()) {
                Iterator it = this.mMiCamera2ShotQueue.iterator();
                while (it.hasNext()) {
                    ((MiCamera2Shot) it.next()).onPreviewComing();
                }
            }
        }
    }

    public void onPreviewMetadata(CaptureResult captureResult) {
        PreviewMetadata previewMetadata = this.mCameraPreviewMetadata;
        if (previewMetadata != null) {
            previewMetadata.onPreviewMetadata(captureResult);
        }
    }

    public void onPreviewThumbnailReceived(Thumbnail thumbnail) {
        MiCamera2Shot miCamera2Shot = this.mMiCamera2Shot;
        if (miCamera2Shot != null) {
            miCamera2Shot.onPreviewThumbnailReceived(thumbnail);
        }
    }

    public void pausePreview() {
        triggerDeviceChecking(false, false);
        if (checkCaptureSession("pausePreview")) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("pausePreview: cameraId=");
            sb.append(getId());
            Log.v(str, sb.toString());
            synchronized (this.mSessionLock) {
                if (this.mCaptureSession == null) {
                    Log.w(TAG, "pausePreview: null session");
                    return;
                }
                try {
                    this.mCaptureSession.stopRepeating();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to pause preview ");
                    sb2.append(e.getMessage());
                    Log.k(6, str2, sb2.toString());
                    notifyOnError(e.getReason());
                } catch (IllegalStateException e2) {
                    Log.e(TAG, "Failed to pause preview, IllegalState", (Throwable) e2);
                    notifyOnError(256);
                }
            }
        }
    }

    public void registerPreviewMeatedata(List list) {
        PreviewMetadata previewMetadata = this.mCameraPreviewMetadata;
        if (previewMetadata != null) {
            previewMetadata.registerPreviewMeatedata(list);
        }
    }

    public void registerPreviewPartialMetadata(List list) {
        PreviewMetadata previewMetadata = this.mCameraPreviewMetadata;
        if (previewMetadata != null) {
            previewMetadata.registerPreviewPartialMetadata(list);
        }
    }

    public void releaseCameraPreviewCallback(@Nullable CameraPreviewCallback cameraPreviewCallback) {
        CaptureSessionStateCallback captureSessionStateCallback = this.mCaptureSessionStateCallback;
        if (captureSessionStateCallback != null) {
            captureSessionStateCallback.setClientCb(cameraPreviewCallback);
        }
    }

    public void releaseFakeSurfaceIfNeed() {
        if (this.mFakeOutputTexture != null) {
            this.mFakeOutputTexture = null;
        }
    }

    public void releasePreview(int i) {
        Handler handler = this.mHelperHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (i == 0) {
            synchronized (this.mSessionLock) {
                if (this.mCaptureSession == null) {
                    Log.w(TAG, "releasePreview: null session");
                    return;
                }
                try {
                    Log.v(TAG, "E: releasePreview");
                    this.mCaptureSession.stopRepeating();
                    abortCaptures();
                    this.mCaptureSession.close();
                    Log.v(TAG, "X: releasePreview");
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to release preview ");
                    sb.append(e.getMessage());
                    Log.k(6, str, sb.toString());
                    notifyOnError(e.getReason());
                } catch (IllegalStateException e2) {
                    try {
                        Log.e(TAG, "Failed to release preview, IllegalState", (Throwable) e2);
                        notifyOnError(256);
                    } catch (Throwable th) {
                        this.mCaptureSession = null;
                        throw th;
                    }
                }
                this.mCaptureSession = null;
            }
        } else {
            synchronized (this.mSessionLock) {
                this.mCaptureSession = null;
            }
        }
        this.mIsCaptureSessionClosed = true;
    }

    public void resetConfigs() {
        Log.v(TAG, "E: resetConfigs");
        if (this.mConfigs != null) {
            this.mConfigs = new CameraConfigs();
        }
        CaptureSessionConfigurations captureSessionConfigurations = this.mSessionConfigs;
        if (captureSessionConfigurations != null) {
            captureSessionConfigurations.reset();
        }
        this.mSessionId = 0;
        releaseCameraPreviewCallback(null);
        Log.v(TAG, "X: resetConfigs");
    }

    public void resetFlashStateTimeLock() {
        if (this.mSupportFlashTimeLock) {
            if (FLASH_LOCK_DEBUG) {
                Log.d(TAG, "reset flash state time lock!");
            }
            this.mLastFlashTimeMillis = 0;
        }
    }

    public int resumePreview() {
        int repeatingRequest;
        String str;
        String sb;
        int i;
        int i2;
        int i3 = 0;
        if (!checkCameraDevice("resumePreview") || !checkCaptureSession("resumePreview")) {
            return 0;
        }
        boolean z = this.mCaptureSession instanceof CameraConstrainedHighSpeedCaptureSession;
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("resumePreview: cameraId=");
        sb2.append(getId());
        sb2.append(" highSpeed=");
        sb2.append(z);
        Log.v(str2, sb2.toString());
        synchronized (this.mSessionLock) {
            if (this.mCaptureSession != null) {
                if (z) {
                    try {
                        this.mPreviewRequest = this.mPreviewRequestBuilder.build();
                        List<CaptureRequest> createHighSpeedRequestList = createHighSpeedRequestList(this.mPreviewRequest);
                        for (CaptureRequest captureRequest : createHighSpeedRequestList) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("high speed repeating for camera ");
                            sb3.append(getId());
                            Log.dumpRequest(sb3.toString(), captureRequest);
                        }
                        repeatingRequest = this.mCaptureSession.setRepeatingBurst(createHighSpeedRequestList, this.mCaptureCallback, this.mCameraHandler);
                        try {
                            str = TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("high speed repeating sequenceId: ");
                            sb4.append(repeatingRequest);
                            sb = sb4.toString();
                        } catch (CameraAccessException e) {
                            CameraAccessException cameraAccessException = e;
                            i3 = i;
                            e = cameraAccessException;
                        } catch (IllegalArgumentException | IllegalStateException e2) {
                            Throwable th = e2;
                            i3 = i2;
                            e = th;
                            Log.e(TAG, "Failed to resume preview, IllegalState", e);
                            notifyOnError(256);
                            return i3;
                        }
                    } catch (CameraAccessException e3) {
                        e = e3;
                        Log.e(TAG, "Failed to resume preview", (Throwable) e);
                        notifyOnError(e.getReason());
                        return i3;
                    } catch (IllegalArgumentException | IllegalStateException e4) {
                        e = e4;
                        Log.e(TAG, "Failed to resume preview, IllegalState", e);
                        notifyOnError(256);
                        return i3;
                    }
                } else {
                    if (this.mZoomMapSurface != null) {
                        if (this.mConfigs.getZoomRatio() >= 15.0f && this.mZoomMapSurfaceAdded.compareAndSet(false, true)) {
                            Log.d(TAG, "resumePreview addTarget mZoomMapSurface");
                            this.mPreviewRequestBuilder.addTarget(this.mZoomMapSurface);
                        } else if (this.mConfigs.getZoomRatio() < 15.0f && this.mZoomMapSurfaceAdded.compareAndSet(true, false)) {
                            Log.d(TAG, "resumePreview removeTarget mZoomMapSurface");
                            this.mPreviewRequestBuilder.removeTarget(this.mZoomMapSurface);
                        }
                    }
                    this.mPreviewRequest = this.mPreviewRequestBuilder.build();
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("normal repeating for camera ");
                    sb5.append(getId());
                    Log.dumpRequest(sb5.toString(), this.mPreviewRequest);
                    repeatingRequest = this.mCaptureSession.setRepeatingRequest(this.mPreviewRequest, this.mCaptureCallback, this.mCameraHandler);
                    str = TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("repeating sequenceId: ");
                    sb6.append(repeatingRequest);
                    sb = sb6.toString();
                }
                Log.d(str, sb);
                i3 = repeatingRequest;
            }
        }
        return i3;
    }

    public void sendSatFallbackDisableRequest(boolean z, boolean z2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("sendSatFallbackDisableRequest: E,,disable = ");
        sb.append(z2);
        sb.append(",,isRepeatingRequest = ");
        sb.append(z);
        Log.d(str, sb.toString());
        if (!z) {
            try {
                Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(1);
                createCaptureRequest.addTarget(this.mPreviewSurface);
                applySettingsForPreview(createCaptureRequest);
                CaptureRequestBuilder.applySatFallbackDisable(createCaptureRequest, this.mCapabilities, z2);
                capture(createCaptureRequest.build(), this.mCaptureCallback, this.mCameraHandler, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.k(6, TAG, e.getMessage());
            }
        } else {
            this.mConfigs.setSatFallbackDisable(z2);
            CaptureRequestBuilder.applySatFallbackDisable(this.mPreviewRequestBuilder, this.mCapabilities, z2);
        }
        Log.d(TAG, "sendSatFallbackDisableRequest: X.");
    }

    public int sendSatFallbackRequest(int i) {
        int i2 = -1;
        if (!this.mMiCamera2ShotQueue.isEmpty()) {
            return -1;
        }
        Log.d(TAG, "sendSatFallbackRequest: E");
        try {
            Builder initRequestBuilder = initRequestBuilder(i);
            initRequestBuilder.addTarget(this.mPreviewSurface);
            if (i == 162) {
                applySettingsForVideo(initRequestBuilder);
            } else {
                applySettingsForPreview(initRequestBuilder);
            }
            CaptureRequestBuilder.applySatFallback(initRequestBuilder, this.mCapabilities, true);
            i2 = capture(initRequestBuilder.build(), this.mCaptureCallback, this.mCameraHandler, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.k(6, TAG, e.getMessage());
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("sendSatFallbackRequest: X. requestId = ");
        sb.append(i2);
        Log.d(str, sb.toString());
        return i2;
    }

    public void setAELock(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAELock: ");
        sb.append(z);
        Log.v(str, sb.toString());
        if (this.mConfigs.setAELock(z)) {
            CaptureRequestBuilder.applyAELock(this.mPreviewRequestBuilder, z);
        }
    }

    public void setAERegions(MeteringRectangle[] meteringRectangleArr) {
        Log.v(TAG, "setAERegions");
        if (this.mConfigs.setAERegions(meteringRectangleArr)) {
            CaptureRequestBuilder.applyAERegions(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setAFRegions(MeteringRectangle[] meteringRectangleArr) {
        Log.v(TAG, "setAFRegions");
        if (this.mConfigs.setAFRegions(meteringRectangleArr)) {
            CaptureRequestBuilder.applyAFRegions(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setAIIEPreviewEnable(boolean z) {
        if (this.mConfigs.setAiAIIEPreviewEnable(z)) {
            CaptureRequestBuilder.applyAiAIIEPreviewEnable(this.mCapabilities, this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setASDEnable(boolean z) {
        boolean aSDEnable = this.mConfigs.setASDEnable(z);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setASDEnable: ");
        sb.append(z);
        Log.v(str, sb.toString());
        if (aSDEnable) {
            CaptureRequestBuilder.applyASDEnable(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setASDPeriod(int i) {
        if (this.mConfigs.setAiSceneDetectPeriod(i)) {
            CaptureRequestBuilder.applyAiSceneDetectPeriod(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setASDScene(int i) {
        if (this.mConfigs.setASDScene(i)) {
            CaptureRequestBuilder.applyASDScene(this.mCapabilities, this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setAWBLock(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAWBLock: ");
        sb.append(z);
        Log.v(str, sb.toString());
        if (this.mConfigs.setAWBLock(z)) {
            CaptureRequestBuilder.applyAWBLock(this.mPreviewRequestBuilder, z);
        }
    }

    public void setAWBMode(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAWBMode: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setAWBMode(i)) {
            CaptureRequestBuilder.applyAWBMode(this.mPreviewRequestBuilder, this.mConfigs.getAWBMode());
        }
    }

    public void setActivityHashCode(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setActivityHashCode(): ");
        sb.append(i);
        Log.v(str, sb.toString());
        this.mConfigs.setActivityHashCode(i);
    }

    public void setAiASDEnable(boolean z) {
        if (this.mConfigs.setAiASDEnable(z)) {
            CaptureRequestBuilder.applyAiASDEnable(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setAiMoonEffectEnable(boolean z) {
        if (this.mConfigs.setAiMoonEffectEnable(z)) {
            CaptureRequestBuilder.applyAiMoonEffectEnable(this.mCapabilities, this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setAiShutterExistMotion(boolean z) {
        this.mConfigs.setAiShutterExistMotion(z);
    }

    public void setAlgorithmPreviewFormat(int i) {
        if (i != this.mConfigs.getAlgorithmPreviewFormat()) {
            this.mConfigs.setAlgorithmPreviewFormat(i);
            if (this.mPreviewCallbackType > 0) {
                preparePreviewImageReader();
            }
        }
    }

    public void setAlgorithmPreviewSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getAlgorithmPreviewSize(), cameraSize)) {
            this.mConfigs.setAlgorithmPreviewSize(cameraSize);
            if (this.mPreviewCallbackType > 0) {
                preparePreviewImageReader();
            }
        }
    }

    public void setAmbilightAeTarget(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAmbilightAeTarget: ");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mConfigs.setAmbilightAeTarget(i);
        CaptureRequestBuilder.applyAmbilightAeTarget(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setAmbilightMode(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAmbilightMode: ");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mConfigs.setAmbilightMode(i);
        CaptureRequestBuilder.applyAmbilightMode(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    /* access modifiers changed from: 0000 */
    public void setAnchorCallback(PreviewCallback previewCallback) {
        super.setAnchorCallback(previewCallback);
    }

    public void setAntiBanding(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAntiBanding: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setAntiBanding(i)) {
            CaptureRequestBuilder.applyAntiBanding(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setAsdDirtyEnable(boolean z) {
        this.mConfigs.setAsdDirtyEnable(z);
        CaptureRequestBuilder.applyAsdDirtyEnable(this.mCapabilities, this.mPreviewRequestBuilder, this.mConfigs);
    }

    public void setAutoZoomMode(int i) {
        this.mConfigs.setAutoZoomMode(i);
        CaptureRequestBuilder.applyAutoZoomMode(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setAutoZoomScaleOffset(float f) {
        this.mConfigs.setAutoZoomScaleOffset(f);
        CaptureRequestBuilder.applyAutoZoomScaleOffset(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setAutoZoomStartCapture(float[] fArr, boolean z) {
        if (checkCameraDevice("setAutoZoomStartCapture")) {
            try {
                Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(3);
                createCaptureRequest.addTarget(this.mPreviewSurface);
                if (z) {
                    createCaptureRequest.addTarget(this.mRecordSurface);
                }
                applySettingsForVideo(createCaptureRequest);
                VendorTagHelper.setValue(createCaptureRequest, CaptureRequestVendorTags.AUTOZOOM_START, fArr);
                capture(createCaptureRequest.build(), this.mCaptureCallback, this.mCameraHandler, null);
            } catch (CameraAccessException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void setAutoZoomStopCapture(int i, boolean z) {
        if (checkCameraDevice("setAutoZoomStopCapture ")) {
            try {
                Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(3);
                createCaptureRequest.addTarget(this.mPreviewSurface);
                if (z) {
                    createCaptureRequest.addTarget(this.mRecordSurface);
                }
                applySettingsForVideo(createCaptureRequest);
                VendorTagHelper.setValue(createCaptureRequest, CaptureRequestVendorTags.AUTOZOOM_STOP, Integer.valueOf(i));
                capture(createCaptureRequest.build(), this.mCaptureCallback, this.mCameraHandler, null);
            } catch (CameraAccessException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void setBeautyValues(BeautyValues beautyValues) {
        this.mConfigs.setBeautyValues(beautyValues);
        CaptureRequestBuilder.applyBeautyValues(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setBinningPictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getBinningPhotoSize(), cameraSize)) {
            this.mConfigs.setBinningPhotoSize(cameraSize);
        }
    }

    public void setBokehDepthSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getBokehDepthSize(), cameraSize)) {
            this.mConfigs.setBokehDepthSize(cameraSize);
        }
    }

    public void setBurstShotSpeed(int i) {
    }

    public void setCacheImageDecoder(CacheImageDecoder cacheImageDecoder) {
        this.mCacheImageDecoder = cacheImageDecoder;
    }

    public void setCameraAI30(boolean z) {
        if (this.mConfigs.setCameraAi30Enable(z)) {
            CaptureRequestBuilder.applyCameraAi30Enable(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setCaptureBusyCallback(CaptureBusyCallback captureBusyCallback) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setCaptureBusyCallback: ");
        sb.append(captureBusyCallback);
        Log.d(str, sb.toString());
        if (captureBusyCallback == null) {
            this.mCaptureBusyCallback = null;
            return;
        }
        synchronized (this.mShotQueueLock) {
            if (this.mMiCamera2ShotQueue.isEmpty()) {
                Log.d(TAG, "setCaptureBusyCallback: shot queue empty");
                captureBusyCallback.onCaptureCompleted(true);
            } else {
                this.mCaptureBusyCallback = captureBusyCallback;
            }
        }
    }

    public void setCaptureTime(long j) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setCaptureTime: ");
        sb.append(j);
        Log.d(str, sb.toString());
        this.mConfigs.setCaptureTime(j);
    }

    public void setCaptureTriggerFlow(int[] iArr) {
        this.mConfigs.setCaptureTriggerFlow(iArr);
    }

    public void setCinematicPhotoEnabled(boolean z) {
        this.mConfigs.setCinematicPhotoEnabled(z);
    }

    public void setCinematicVideoEnabled(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setCinematicVideoEnabled: ");
        sb.append(z);
        Log.v(str, sb.toString());
        this.mConfigs.setCinematicVideoEnabled(z);
        CaptureRequestBuilder.applyCinematicVideo(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setColorEffect(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setColorEffect: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setColorEffect(i)) {
            CaptureRequestBuilder.applyColorEffect(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setColorEnhanceEnable(boolean z) {
        if (this.mConfigs.setColorEnhanceEnabled(z)) {
            CaptureRequestBuilder.applyColorEnhance(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setContrast(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setContrast: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setContrastLevel(i)) {
            CaptureRequestBuilder.applyContrast(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setCustomAWB(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setCustomAWB: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setCustomAWB(i)) {
            CaptureRequestBuilder.applyCustomAWB(this.mPreviewRequestBuilder, this.mConfigs.getAwbCustomValue());
        }
    }

    public void setDeviceOrientation(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setDeviceOrientation: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setDeviceOrientation(i)) {
            CaptureRequestBuilder.applyDeviceOrientation(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setDisplayOrientation(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setDisplayOrientation: ");
        sb.append(i);
        Log.v(str, sb.toString());
        this.mDisplayOrientation = i;
    }

    public void setDodepurpleEnabled(boolean z) {
        if (this.mConfigs.setDodepurpleEnabled(z)) {
            CaptureRequestBuilder.applyDepurpleEnable(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setDualBokehEnable(boolean z) {
        if (this.mConfigs.setDualBokehEnable(z)) {
            CaptureRequestBuilder.applyDualBokeh(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setDualCamWaterMarkEnable(boolean z) {
        this.mConfigs.setDualCamWaterMarkEnable(z);
    }

    public void setEnableEIS(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setEnableEIS: ");
        sb.append(z);
        Log.v(str, sb.toString());
        if (this.mConfigs.setEnableEIS(z)) {
            CaptureRequestBuilder.applyAntiShake(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setEnableOIS(boolean z) {
        if (this.mCapabilities.isSupportOIS()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setEnableOIS ");
            sb.append(z);
            Log.v(str, sb.toString());
            this.mConfigs.setEnableOIS(z);
            CaptureRequestBuilder.applyAntiShake(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setEnableZsl(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setEnableZsl ");
        sb.append(z);
        Log.v(str, sb.toString());
        this.mConfigs.setEnableZsl(z);
        CaptureRequestBuilder.applyZsl(this.mPreviewRequestBuilder, this.mConfigs);
    }

    public void setExposureCompensation(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setExposureCompensation: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setExposureCompensationIndex(i)) {
            CaptureRequestBuilder.applyExposureCompensation(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setExposureMeteringMode(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setExposureMeteringMode: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setExposureMeteringMode(i)) {
            CaptureRequestBuilder.applyExposureMeteringMode(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setExposureTime(long j) {
        if (this.mConfigs.setExposureTime(j)) {
            applyFlashMode(this.mPreviewRequestBuilder, 1);
            CaptureRequestBuilder.applySceneMode(this.mPreviewRequestBuilder, this.mConfigs);
            CaptureRequestBuilder.applyExposureCompensation(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyIso(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyExposureTime(this.mPreviewRequestBuilder, 1, this.mConfigs);
        }
    }

    public void setExtendSceneMode(int i) {
        boolean extendSceneMode = this.mConfigs.setExtendSceneMode(i);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setExtendSceneMode: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (extendSceneMode) {
            CaptureRequestBuilder.applyExtendSceneMode(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setEyeLight(int i) {
        if (this.mConfigs.setEyeLight(i)) {
            CaptureRequestBuilder.applyEyeLight(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setFNumber(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFNumber ");
        sb.append(str);
        sb.append(" for ");
        sb.append(this.mPreviewRequestBuilder);
        Log.d(str2, sb.toString());
        this.mConfigs.setFNumber(str);
        CaptureRequestBuilder.applyFNumber(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setFaceAgeAnalyze(boolean z) {
        if (this.mConfigs.setFaceAgeAnalyzeEnabled(z)) {
            CaptureRequestBuilder.applyFaceAgeAnalyze(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setFaceScore(boolean z) {
        if (this.mConfigs.setFaceScoreEnabled(z)) {
            CaptureRequestBuilder.applyFaceScore(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setFaceWaterMarkEnable(boolean z) {
        this.mConfigs.setFaceWaterMarkEnable(z);
    }

    public void setFaceWaterMarkFormat(String str) {
        this.mConfigs.setFaceWaterMarkFormat(str);
    }

    public void setFakeTeleOutputSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getFakeSatOutputSize(), cameraSize)) {
            this.mConfigs.setFakeSatOutputSize(cameraSize);
        }
    }

    public void setFakeTelePictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getFakeTelePhotoSize(), cameraSize)) {
            this.mConfigs.setFakeTelePhotoSize(cameraSize);
        }
    }

    public void setFlashMode(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFlashMode: ");
        sb.append(i);
        Log.v(str, sb.toString());
        this.mConfigs.setFlashMode(i);
        applyFlashMode(this.mPreviewRequestBuilder, 1);
    }

    public void setFlawDetectEnable(boolean z) {
        this.mConfigs.setFlawDetectEnable(z);
    }

    public void setFocusDistance(float f) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFocusDistance: ");
        sb.append(f);
        Log.v(str, sb.toString());
        if (this.mConfigs.setFocusDistance(f)) {
            CaptureRequestBuilder.applyFocusDistance(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setFocusMode(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFocusMode: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setFocusMode(i)) {
            CaptureRequestBuilder.applyFocusMode(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setFpsRange(Range range) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFpsRange: ");
        sb.append(range);
        Log.v(str, sb.toString());
        this.mConfigs.setPreviewFpsRange(range);
        CaptureRequestBuilder.applyFpsRange(this.mPreviewRequestBuilder, this.mConfigs);
    }

    public void setFrontMirror(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setFrontMirror: ");
        sb.append(z);
        Log.d(str, sb.toString());
        this.mConfigs.setFrontMirror(z);
    }

    public void setGlobalWatermark() {
        this.mConfigs.setGlobalWatermark();
    }

    public void setGpsLocation(Location location) {
        this.mConfigs.setGpsLocation(location);
    }

    @NonNull
    public void setHDR(HDRStatus hDRStatus) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setHDR ");
        sb.append(hDRStatus.toString());
        sb.append(", caller: ");
        sb.append(Util.getCallers(7));
        Log.d(str, sb.toString());
        if (this.mConfigs.setHDRStatus(hDRStatus)) {
            CaptureRequestBuilder.applyHDR(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setHDRCheckerEnable(boolean z) {
        if (this.mConfigs.setHDRCheckerEnabled(z)) {
            CaptureRequestBuilder.applyHDRCheckerEnable(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setHDRCheckerStatus(int i) {
        if (this.mConfigs.setHDRCheckerStatus(i)) {
            CaptureRequestBuilder.applyHDRCheckerStatus(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setHDRMode(int i) {
        if (this.mConfigs.setHDRMode(i)) {
            CaptureRequestBuilder.applyHDRMode(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setHFRDeflickerEnable(boolean z) {
        if (this.mConfigs.setHFRDeflickerEnable(z)) {
            CaptureRequestBuilder.applyHFRDeflicker(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setHHT(boolean z) {
        if (this.mConfigs.setHHTEnabled(z)) {
            CaptureRequestBuilder.applyHHT(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setHdr10VideoMode(boolean z) {
        CameraConfigs cameraConfigs;
        int i;
        if (!z) {
            cameraConfigs = this.mConfigs;
            i = 0;
        } else if (CameraSettings.isHdr10VideoModeOn()) {
            cameraConfigs = this.mConfigs;
            i = 2;
        } else {
            if (CameraSettings.isHdr10PlusVideoModeOn()) {
                cameraConfigs = this.mConfigs;
                i = 3;
            }
            CaptureRequestBuilder.applyHDR10Video(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
        cameraConfigs.setHDR10Video(i);
        CaptureRequestBuilder.applyHDR10Video(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setHistogramStatsEnabled(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setHistogramStatsEnabled: ");
        sb.append(z);
        Log.v(str, sb.toString());
        this.mConfigs.setHistogramStatsEnabled(z);
        CaptureRequestBuilder.applyHistogramStats(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setISO(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setISO: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setISO(i)) {
            applyFlashMode(this.mPreviewRequestBuilder, 1);
            CaptureRequestBuilder.applyExposureCompensation(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyIso(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
            CaptureRequestBuilder.applyExposureTime(this.mPreviewRequestBuilder, 1, this.mConfigs);
        }
    }

    public void setInTimerBurstShotting(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setInTimerBurstShotting: =");
        sb.append(z);
        Log.d(str, sb.toString());
        this.mConfigs.setInTimerBurstShotting(z);
    }

    public void setIsFaceExist(boolean z) {
        this.mConfigs.setIsFaceExist(z);
    }

    public void setJpegQuality(int i) {
        this.mConfigs.setJpegQuality(i);
    }

    public void setJpegRotation(int i) {
        this.mConfigs.setJpegRotation(i);
    }

    public void setJpegThumbnailSize(CameraSize cameraSize) {
        this.mConfigs.setThumbnailSize(cameraSize);
    }

    public void setLLS(boolean z) {
        this.mConfigs.setLLSEnabled(z);
    }

    public void setLensDirtyDetect(boolean z) {
        if (this.mConfigs.setLensDirtyDetectEnabled(z)) {
            CaptureRequestBuilder.applyLensDirtyDetect(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setMFLockAfAe(boolean z) {
        this.mConfigs.setMFAfAeLock(z);
    }

    public void setMacroMode(boolean z) {
        if (this.mConfigs.setMacroMode(z)) {
            CaptureRequestBuilder.applyMacroMode(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setMarcroPictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getMacroPhotoSize(), cameraSize)) {
            this.mConfigs.setMacroPhotoSize(cameraSize);
        }
    }

    public void setMfnr(boolean z) {
        if (this.mConfigs.setMfnrEnabled(z)) {
            CaptureRequestBuilder.applyHwMfnr(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setModuleAnchorFrame(boolean z) {
        this.mConfigs.setModuleAnchorFrame(z);
    }

    public void setModuleParameter(int i, int i2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setModuleParameter: ");
        sb.append(Util.getCallers(5));
        Log.d(str, sb.toString());
        this.mPreviewControl = new MiCamera2PreviewNormal(i, i2);
    }

    public void setMtkPipDevices(int[] iArr) {
        this.mConfigs.setMtkPipDevices(iArr);
        CaptureRequestBuilder.applyMtkPipDevices(this.mPreviewRequestBuilder, this.mConfigs);
    }

    public void setMultiSnapStopRequest(boolean z) {
        this.mConfigs.setMultiSnapStopRequest(z);
    }

    public void setNeedPausePreview(boolean z) {
        this.mConfigs.setPausePreview(z);
    }

    public void setNeedSequence(boolean z) {
        this.mConfigs.setNeedSequence(z);
    }

    public void setNewWatermark(boolean z) {
        this.mConfigs.setNewWatermark(z);
    }

    public void setNormalWideLDC(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setNormalWideLDC: ");
        sb.append(z);
        Log.v(str, sb.toString());
        if (this.mConfigs.setNormalWideLDCEnabled(z)) {
            CaptureRequestBuilder.applyNormalWideLDC(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setOnTripodModeStatus(ASDScene[] aSDSceneArr) {
        this.mConfigs.setOnTripodScenes(aSDSceneArr);
        CaptureRequestBuilder.applyOnTripodModeStatus(this.mPreviewRequestBuilder, this.mConfigs);
    }

    public void setOpticalZoomToTele(boolean z) {
        if (C0122O00000o.instance().OOOo0O() && this.mCapabilities.isSupportFastZoomIn()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setOpticalZoomToTele: toTele = ");
            sb.append(z);
            Log.d(str, sb.toString());
            this.mToTele = z;
        }
        MiCameraCompat.applyStFastZoomIn(this.mPreviewRequestBuilder, z);
    }

    public void setParallelSettings(int i, int i2) {
        this.mConfigs.setParallelSettings(i, i2);
    }

    public void setPhysicalCameraIdForPortrait(IImageReaderParameterSets iImageReaderParameterSets, OutputConfiguration outputConfiguration, boolean z) {
        String str;
        StringBuilder sb;
        String str2;
        int i;
        int i2 = iImageReaderParameterSets.imageType;
        if (i2 == 0) {
            if (!z) {
                i = getPhysicalBokehMainId();
                CompatibilityUtils.setPhysicalCameraId(outputConfiguration, String.valueOf(i));
                str2 = TAG;
                sb = new StringBuilder();
                str = "Binds main output stream to camera ";
            } else {
                return;
            }
        } else if (i2 == 1) {
            i = getPhysicalBokehSubId();
            CompatibilityUtils.setPhysicalCameraId(outputConfiguration, String.valueOf(i));
            str2 = TAG;
            sb = new StringBuilder();
            str = "Binds sub output stream to camera ";
        } else {
            return;
        }
        sb.append(str);
        sb.append(i);
        Log.d(str2, sb.toString());
    }

    public void setPictureFormat(int i) {
        if (this.mConfigs.getPhotoFormat() != i) {
            this.mConfigs.setPhotoFormat(i);
            preparePhotoImageReader();
        }
    }

    public void setPictureMaxImages(int i) {
        if (i > this.mConfigs.getPhotoMaxImages()) {
            this.mConfigs.setPhotoMaxImages(i);
            preparePhotoImageReader();
        }
    }

    public void setPictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getPhotoSize(), cameraSize)) {
            this.mConfigs.setPhotoSize(cameraSize);
            preparePhotoImageReader();
        }
    }

    public void setPortraitLighting(int i) {
        if (this.mConfigs.setPortraitLightingPattern(i)) {
            CaptureRequestBuilder.applyPortraitLighting(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setPreviewMaxImages(int i) {
        if (i > this.mConfigs.getPreviewMaxImages()) {
            this.mConfigs.setPreviewMaxImages(i);
        }
    }

    public void setPreviewSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getPreviewSize(), cameraSize)) {
            this.mConfigs.setPreviewSize(cameraSize);
        }
    }

    public void setQcfaEnable(boolean z) {
        this.mConfigs.setQcfaEnable(z);
    }

    public void setQuickShotAnimation(boolean z) {
        this.mConfigs.setQuickShotAnimation(z);
    }

    public void setRawSizeOfMacro(CameraSize cameraSize) {
        this.mConfigs.setRawSizeOfMacro(cameraSize);
    }

    public void setRawSizeOfTele(CameraSize cameraSize) {
        this.mConfigs.setRawSizeOfTele(cameraSize);
    }

    public void setRawSizeOfTuningBuffer(CameraSize cameraSize) {
        this.mConfigs.setRawSizeOfTuningBuffer(cameraSize);
    }

    public void setRawSizeOfUltraTele(CameraSize cameraSize) {
        this.mConfigs.setRawSizeOfUltraTele(cameraSize);
    }

    public void setRawSizeOfUltraWide(CameraSize cameraSize) {
        this.mConfigs.setRawSizeOfUltraWide(cameraSize);
    }

    public void setRawSizeOfWide(CameraSize cameraSize) {
        this.mConfigs.setRawSizeOfWide(cameraSize);
    }

    public void setSATRemosicPictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getSATRemosicPhotoSize(), cameraSize)) {
            this.mConfigs.setSATRemosicPhotoSize(cameraSize);
        }
    }

    public void setSATUltraWideLDC(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setSATUltraWideLDC: ");
        sb.append(z);
        Log.v(str, sb.toString());
        CaptureRequestBuilder.applySATUltraWideLDC(this.mPreviewRequestBuilder, this.mCapabilities, z);
    }

    public void setSatIsZooming(boolean z) {
        this.mConfigs.setSatIsZooming(z);
        CaptureRequestBuilder.applySatIsZooming(this.mPreviewRequestBuilder, this.mConfigs);
    }

    public void setSaturation(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setSaturation: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setSaturationLevel(i)) {
            CaptureRequestBuilder.applySaturation(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setSceneMode(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setSceneMode: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setSceneMode(i)) {
            CaptureRequestBuilder.applySceneMode(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setSensorRawImageSize(CameraSize cameraSize, int i) {
        if (!Objects.equals(this.mConfigs.getSensorRawImageSize(), cameraSize)) {
            this.mConfigs.setSensorRawImageSize(cameraSize);
        }
    }

    public void setSharpness(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setSharpness: ");
        sb.append(i);
        Log.v(str, sb.toString());
        if (this.mConfigs.setSharpnessLevel(i)) {
            CaptureRequestBuilder.applySharpness(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setShot2Gallery(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setShot2Gallery: isShot2Gallery=");
        sb.append(z);
        Log.d(str, sb.toString());
        this.mConfigs.setShot2Gallery(z);
    }

    public void setShotBoostParams(Consumer consumer) {
        this.mShotBoostParams = consumer;
    }

    public void setShotSavePath(String str, boolean z) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setShotSavePath: ");
        sb.append(str);
        sb.append(", isParallel:");
        sb.append(z);
        Log.d(str2, sb.toString());
        this.mConfigs.setShotPath(str, z);
    }

    public void setShotType(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setShotType: algo=");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mConfigs.setShotType(i);
    }

    public void setSingleBokeh(boolean z) {
        if (this.mConfigs.setSingleBokehEnabled(z)) {
            CaptureRequestBuilder.applySingleBokeh(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setSpecshotModeEnable(boolean z) {
        if (!this.mCapabilities.isSpecshotModeSupported()) {
            z = false;
        }
        this.mConfigs.setSpecshotModeEnable(z);
    }

    public void setSubPictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getSubPhotoSize(), cameraSize)) {
            this.mConfigs.setSubPhotoSize(cameraSize);
        }
    }

    public void setSuperNight(boolean z) {
        this.mConfigs.setSuperNightEnabled(z);
    }

    public void setSuperResolution(boolean z) {
        if (this.mConfigs.setSuperResolutionEnabled(z)) {
            CaptureRequestBuilder.applySuperResolution(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setSwMfnr(boolean z) {
        if (this.mConfigs.setSwMfnrEnabled(z)) {
            CaptureRequestBuilder.applySwMfnr(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setTargetZoom(float f) {
        if (this.mConfigs.setTargetZoom(f)) {
            CaptureRequestBuilder.applyTargetZoom(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setTelePictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getTelePhotoSize(), cameraSize)) {
            this.mConfigs.setTelePhotoSize(cameraSize);
        }
    }

    public void setThermalLevel(int i) {
        this.mConfigs.setThermalLevel(i);
        CaptureRequestBuilder.applyThermal(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setTimeWaterMarkEnable(boolean z) {
        this.mConfigs.setTimeWaterMarkEnable(z);
    }

    public void setTimeWatermarkValue(String str) {
        this.mConfigs.setTimeWaterMarkValue(str);
    }

    public void setTuningBufferSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getTuningBufferSize(), cameraSize)) {
            this.mConfigs.setTuningBufferSize(cameraSize);
        }
    }

    public void setTuningMode(byte b) {
        this.mConfigs.setTuningMode(b);
        CaptureRequestBuilder.applyTuningMode(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setUltraPixelPortrait(boolean z) {
        if (this.mConfigs.setUltraPixelPortraitEnabled(z)) {
            CaptureRequestBuilder.applyUltraPixelPortrait(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
        }
    }

    public void setUltraTelePictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getStandalonePhotoSize(), cameraSize)) {
            this.mConfigs.setUltraTelePhotoSize(cameraSize);
        }
    }

    public void setUltraWideLDC(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setUltraWideLDC: ");
        sb.append(z);
        Log.v(str, sb.toString());
        if (this.mConfigs.setUltraWideLDCEnabled(z)) {
            CaptureRequestBuilder.applyUltraWideLDC(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setUltraWidePictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getUltraWidePhotoSize(), cameraSize)) {
            this.mConfigs.setUltraWidePhotoSize(cameraSize);
        }
    }

    public void setUseLegacyFlashMode(boolean z) {
        this.mConfigs.setUseLegacyFlashMode(z);
    }

    public void setVendorSetting(Key key, Object obj) {
        Builder builder = this.mPreviewRequestBuilder;
        if (builder != null) {
            builder.set(key, obj);
        }
    }

    public void setVideoBokehLevelBack(int i) {
        this.mConfigs.setVideoBokehLevelBack(i);
        CaptureRequestBuilder.applyVideoBokehLevelBack(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setVideoBokehLevelFront(float f) {
        this.mConfigs.setVideoBokehLevelFront(f);
        CaptureRequestBuilder.applyVideoBokehLevelFront(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setVideoFilterColorRetentionBack(boolean z) {
        if (this.mConfigs.setVideoFilterColorRetentionBack(z)) {
            CaptureRequestBuilder.applyColorRetentionBack(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setVideoFilterColorRetentionFront(boolean z) {
        if (this.mConfigs.setVideoFilterColorRetentionFront(z)) {
            CaptureRequestBuilder.applyColorRetentionFront(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setVideoFilterColorRetentionMode(int i, boolean z) {
        if (this.mConfigs.setVideoBokehColorRetentionMode(i, z)) {
            CaptureRequestBuilder.applyVideoBokehColorRetentionMode(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs, z);
        }
    }

    public void setVideoFilterId(int i) {
        this.mConfigs.setVideoFilterId(i);
        CaptureRequestBuilder.applyVideoFilterId(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setVideoFpsRange(Range range) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setVideoFpsRange: ");
        sb.append(range);
        Log.v(str, sb.toString());
        if (this.mConfigs.setVideoFpsRange(range)) {
            CaptureRequestBuilder.applyVideoFpsRange(this.mPreviewRequestBuilder, this.mConfigs);
        }
    }

    public void setVideoLogEnabled(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setVideoLogEnabled: ");
        sb.append(z);
        Log.v(str, sb.toString());
        this.mConfigs.setIsVideoLogEnabled(z);
        CaptureRequestBuilder.applyVideoLog(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
    }

    public void setVideoSnapshotSize(CameraSize cameraSize) {
        this.mConfigs.setVideoSnapshotSize(cameraSize);
    }

    public void setWidePictureSize(CameraSize cameraSize) {
        if (!Objects.equals(this.mConfigs.getWidePhotoSize(), cameraSize)) {
            this.mConfigs.setWidePhotoSize(cameraSize);
        }
    }

    public void setZoomRatio(float f) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setZoomRatio(): ");
        sb.append(f);
        Log.v(str, sb.toString());
        if (this.mConfigs.setZoomRatio(f)) {
            CaptureRequestBuilder.applyZoomRatio(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
        }
    }

    public void setZoomRatioForCapture(float f) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setZoomRatioForCapture(): ");
        sb.append(f);
        Log.v(str, sb.toString());
        this.mConfigs.setZoomRatio(f);
    }

    public void startFaceDetection() {
        Log.v(TAG, "startFaceDetection");
        this.mConfigs.setFaceDetectionEnabled(true);
        CaptureRequestBuilder.applyFaceDetection(this.mPreviewRequestBuilder, this.mConfigs);
    }

    public void startFocus(FocusTask focusTask, int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startFocus: ");
        sb.append(i);
        Log.v(str, sb.toString());
        try {
            this.mCaptureCallback.setFocusTask(focusTask);
            Builder initFocusRequestBuilder = initFocusRequestBuilder(i);
            if (initFocusRequestBuilder == null) {
                Log.w(TAG, "startFocus afBuilder == null, return");
                return;
            }
            initFocusRequestBuilder.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(1));
            applySettingsForFocusCapture(initFocusRequestBuilder);
            initFocusRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(1));
            if (this.mConfigs.getmMtkPipDevices() != null) {
                CaptureRequestBuilder.applyMtkPipDevices(initFocusRequestBuilder, this.mConfigs);
            }
            CaptureRequest build = initFocusRequestBuilder.build();
            focusTask.setRequest(build);
            capture(build, this.mCaptureCallback, this.mCameraHandler, focusTask);
            this.mConfigs.setFocusMode(1);
            this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(1));
            if (this.mPreviewControl.needForVideo()) {
                applySettingsForVideo(this.mPreviewRequestBuilder);
            } else {
                applySettingsForPreview(this.mPreviewRequestBuilder);
            }
            resumePreview();
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.k(6, TAG, e.getMessage());
            notifyOnError(e.getReason());
        } catch (IllegalArgumentException e2) {
            Log.w(TAG, "Failed to start focus: ", (Throwable) e2);
        }
    }

    public void startHighSpeedRecordPreview() {
        String str = "startHighSpeedRecordPreview";
        if (checkCameraDevice(str)) {
            Log.d(TAG, str);
            applySettingsForVideo(this.mPreviewRequestBuilder);
            MiCameraCompat.applyIsHfrPreview(this.mPreviewRequestBuilder, true);
            resumePreview();
        }
    }

    public void startHighSpeedRecordSession(@NonNull Surface surface, @NonNull Surface surface2, Range range, CameraPreviewCallback cameraPreviewCallback) {
        CameraDevice cameraDevice;
        HighSpeedCaptureSessionStateCallback highSpeedCaptureSessionStateCallback;
        Handler handler;
        ArrayList arrayList;
        CameraDevice cameraDevice2;
        int i;
        InputConfiguration inputConfiguration;
        CaptureRequest build;
        HighSpeedCaptureSessionStateCallback highSpeedCaptureSessionStateCallback2;
        Handler handler2;
        int[] iArr;
        if (checkCameraDevice("startHighSpeedRecordSession")) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startHighSpeedRecordSession: previewSurface = ");
            sb.append(surface);
            sb.append(" recordSurface = ");
            sb.append(surface2);
            sb.append(" fpsRange = ");
            sb.append(range);
            Log.d(str, sb.toString());
            this.mPreviewSurface = surface;
            this.mRecordSurface = surface2;
            this.mHighSpeedFpsRange = range;
            this.mSessionId = genSessionId();
            try {
                if (VERSION.SDK_INT >= 30 && this.mCapabilities.isCinematicVideoSupported()) {
                    this.mSessionConfigs.set(CaptureRequestVendorTags.CINEMATIC_VIDEO_ENABLED, (Object) Byte.valueOf(this.mConfigs.isCinematicVideoEnabled() ? (byte) 1 : 0));
                    CaptureRequestBuilder.applyCinematicVideo(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
                }
                this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(3);
                this.mPreviewRequestBuilder.addTarget(this.mPreviewSurface);
                this.mPreviewRequestBuilder.addTarget(this.mRecordSurface);
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.mHighSpeedFpsRange);
                synchronized (this.mSessionLock) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("startHighSpeedRecordSession: reset session ");
                    sb2.append(this.mCaptureSession);
                    Log.d(str2, sb2.toString());
                    this.mCaptureSession = null;
                }
                this.mCameraCloseCallback = cameraPreviewCallback;
                List<Surface> asList = Arrays.asList(new Surface[]{this.mPreviewSurface, this.mRecordSurface});
                if (C0124O00000oO.isMTKPlatform()) {
                    Log.d(TAG, "turns PQ feature on");
                    this.mSessionConfigs.set(CaptureRequestVendorTags.MTK_CONFIGURE_SETTING_PROPRIETARY, (Object) CaptureRequestVendorTags.MTK_CONFIGURE_SETTING_PROPRIETARY_ON);
                    MiCameraCompat.applyPqFeature(this.mPreviewRequestBuilder, true);
                    int intValue = ((Integer) this.mHighSpeedFpsRange.getUpper()).intValue();
                    if (C0122O00000o.instance().OOOoO0()) {
                        if (intValue == 120) {
                            iArr = CaptureRequestVendorTags.VALUE_SMVR_MODE_120FPS;
                        } else if (intValue == 240) {
                            iArr = CaptureRequestVendorTags.VALUE_SMVR_MODE_240FPS;
                        } else {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Unsupported Slow Motion Recording: ");
                            sb3.append(this.mHighSpeedFpsRange);
                            throw new UnsupportedOperationException(sb3.toString());
                        }
                        this.mSessionConfigs.set(CaptureRequestVendorTags.SMVR_MODE, (Object) iArr);
                        MiCameraCompat.applySlowMotionVideoRecordingMode(this.mPreviewRequestBuilder, iArr);
                        String str3 = TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("startHighSpeedRecordSession: turns smvrmode to ");
                        sb4.append(intValue);
                        Log.d(str3, sb4.toString());
                        arrayList = new ArrayList();
                        for (Surface outputConfiguration : asList) {
                            arrayList.add(new OutputConfiguration(outputConfiguration));
                        }
                        cameraDevice2 = this.mCameraDevice;
                        i = 0;
                        inputConfiguration = null;
                        build = this.mPreviewRequestBuilder.build();
                        highSpeedCaptureSessionStateCallback2 = new HighSpeedCaptureSessionStateCallback(this.mSessionId, cameraPreviewCallback);
                        handler2 = this.mCameraHandler;
                    } else if (intValue == 120) {
                        cameraDevice = this.mCameraDevice;
                        highSpeedCaptureSessionStateCallback = new HighSpeedCaptureSessionStateCallback(this.mSessionId, cameraPreviewCallback);
                        handler = this.mCameraHandler;
                        cameraDevice.createConstrainedHighSpeedCaptureSession(asList, highSpeedCaptureSessionStateCallback, handler);
                    } else if (intValue == 240) {
                        this.mSessionConfigs.set(CaptureRequestVendorTags.SMVR_MODE, (Object) CaptureRequestVendorTags.VALUE_SMVR_MODE_240FPS);
                        MiCameraCompat.applySlowMotionVideoRecordingMode(this.mPreviewRequestBuilder, CaptureRequestVendorTags.VALUE_SMVR_MODE_240FPS);
                        Log.d(TAG, "startHighSpeedRecordSession: turns smvrmode to 240");
                        arrayList = new ArrayList();
                        for (Surface outputConfiguration2 : asList) {
                            arrayList.add(new OutputConfiguration(outputConfiguration2));
                        }
                        cameraDevice2 = this.mCameraDevice;
                        i = 0;
                        inputConfiguration = null;
                        build = this.mPreviewRequestBuilder.build();
                        highSpeedCaptureSessionStateCallback2 = new HighSpeedCaptureSessionStateCallback(this.mSessionId, cameraPreviewCallback);
                        handler2 = this.mCameraHandler;
                    } else {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Unsupported Slow Motion Recording: ");
                        sb5.append(this.mHighSpeedFpsRange);
                        throw new UnsupportedOperationException(sb5.toString());
                    }
                } else if (((Integer) this.mHighSpeedFpsRange.getUpper()).intValue() == 120 && !C0122O00000o.instance().OOOoO0()) {
                    ArrayList arrayList2 = new ArrayList();
                    for (Surface outputConfiguration3 : asList) {
                        arrayList2.add(new OutputConfiguration(outputConfiguration3));
                    }
                    this.mCameraDevice.createCustomCaptureSession(null, arrayList2, 32888, new HighSpeedCaptureSessionStateCallback(this.mSessionId, cameraPreviewCallback), this.mCameraHandler);
                } else if (C0122O00000o.instance().OO0oOo()) {
                    arrayList = new ArrayList();
                    for (Surface outputConfiguration4 : asList) {
                        arrayList.add(new OutputConfiguration(outputConfiguration4));
                    }
                    cameraDevice2 = this.mCameraDevice;
                    i = 1;
                    inputConfiguration = null;
                    build = this.mPreviewRequestBuilder.build();
                    highSpeedCaptureSessionStateCallback2 = new HighSpeedCaptureSessionStateCallback(this.mSessionId, cameraPreviewCallback);
                    handler2 = this.mCameraHandler;
                } else {
                    cameraDevice = this.mCameraDevice;
                    highSpeedCaptureSessionStateCallback = new HighSpeedCaptureSessionStateCallback(this.mSessionId, cameraPreviewCallback);
                    handler = this.mCameraHandler;
                    cameraDevice.createConstrainedHighSpeedCaptureSession(asList, highSpeedCaptureSessionStateCallback, handler);
                }
                CompatibilityUtils.createCaptureSessionWithSessionConfiguration(cameraDevice2, i, inputConfiguration, arrayList, build, highSpeedCaptureSessionStateCallback2, handler2);
            } catch (Exception e) {
                notifyOnError(-1);
                Log.e(TAG, "Failed to start high speed record session", (Throwable) e);
            }
        }
    }

    public void startHighSpeedRecording() {
        String str = "startHighSpeedRecording";
        if (checkCaptureSession(str)) {
            Log.d(TAG, str);
            MiCameraCompat.applyIsHfrPreview(this.mPreviewRequestBuilder, false);
            if (C0122O00000o.instance().O0oo00o()) {
                Log.d(TAG, "startHighSpeedRecording: CAF is disabled");
                this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(1));
            }
            CaptureRequestBuilder.applySessionParameters(this.mPreviewRequestBuilder, this.mSessionConfigs);
            resumePreview();
        }
    }

    public void startObjectTrack(RectF rectF) {
    }

    public void startPreviewCallback(PreviewCallback previewCallback, PreviewCallback previewCallback2) {
        String str = "startPreviewCallback";
        if (checkCaptureSession(str)) {
            Log.v(TAG, str);
            int i = this.mPreviewCallbackType;
            if (i > 0) {
                if (!((i & 16) == 0 || previewCallback2 == null)) {
                    setAnchorCallback(previewCallback2);
                }
                if (previewCallback != null) {
                    setPreviewCallback(previewCallback);
                }
                if (!this.mIsPreviewCallbackStarted) {
                    this.mIsPreviewCallbackStarted = true;
                    this.mPreviewRequestBuilder.addTarget(this.mPreviewImageReader.getSurface());
                }
            }
        }
    }

    public void startPreviewSession(Surface surface, int i, int i2, Surface surface2, int i3, boolean z, CameraPreviewCallback cameraPreviewCallback) {
        startPreviewSessionImpl(surface, i, i2, surface2, i3, z, cameraPreviewCallback, 1);
    }

    public void startRecordPreview() {
        int i;
        String str = "startRecordPreview";
        if (checkCameraDevice(str)) {
            Log.d(TAG, str);
            try {
                this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(3);
                this.mPreviewRequestBuilder.addTarget(this.mPreviewSurface);
                if (this.mConfigs.isEnableRecordControl()) {
                    VendorTagHelper.setValue(this.mPreviewRequestBuilder, CaptureRequestVendorTags.VIDEO_RECORD_CONTROL, Integer.valueOf(0));
                }
                applySettingsForVideo(this.mPreviewRequestBuilder);
                resumePreview();
            } catch (CameraAccessException e) {
                Log.e(TAG, "Failed to start record preview", (Throwable) e);
                i = e.getReason();
                notifyOnError(i);
            } catch (IllegalStateException e2) {
                Log.e(TAG, "Failed to start record preview, IllegalState", (Throwable) e2);
                i = 256;
                notifyOnError(i);
            }
        }
    }

    public void startRecordSession(@NonNull Surface surface, @NonNull Surface surface2, boolean z, int i, CameraPreviewCallback cameraPreviewCallback) {
        int i2;
        List<Surface> list;
        CameraDevice cameraDevice;
        InputConfiguration inputConfiguration;
        CaptureRequest build;
        CaptureSessionStateCallback captureSessionStateCallback;
        Handler handler;
        if (checkCameraDevice("startRecordSession")) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startRecordSession: previewSurface=");
            sb.append(surface);
            sb.append(" recordSurface=");
            sb.append(surface2);
            Log.d(str, sb.toString());
            this.mPreviewSurface = surface;
            this.mRecordSurface = surface2;
            this.mSessionId = genSessionId();
            this.mVideoSessionId = this.mSessionId;
            try {
                this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(3);
                this.mPreviewRequestBuilder.addTarget(this.mPreviewSurface);
                boolean z2 = false;
                if (this.mConfigs.isEnableRecordControl()) {
                    VendorTagHelper.setValue(this.mPreviewRequestBuilder, CaptureRequestVendorTags.VIDEO_RECORD_CONTROL, Integer.valueOf(0));
                }
                CaptureRequestBuilder.applyFpsRange(this.mPreviewRequestBuilder, this.mConfigs);
                synchronized (this.mSessionLock) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("startRecordSession: reset session ");
                    sb2.append(this.mCaptureSession);
                    Log.d(str2, sb2.toString());
                    this.mCaptureSession = null;
                }
                if (z) {
                    prepareVideoSnapshotImageReader(this.mConfigs.getVideoSnapshotSize());
                    list = Arrays.asList(new Surface[]{this.mPreviewSurface, this.mRecordSurface, this.mVideoSnapshotImageReader.getSurface()});
                } else {
                    list = Arrays.asList(new Surface[]{this.mPreviewSurface, this.mRecordSurface});
                }
                ArrayList arrayList = new ArrayList(list.size());
                for (Surface outputConfiguration : list) {
                    arrayList.add(new OutputConfiguration(outputConfiguration));
                }
                this.mCameraCloseCallback = cameraPreviewCallback;
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("startRecordSession: operatingMode is ");
                sb3.append(Integer.toHexString(i));
                Log.d(str3, sb3.toString());
                this.mSessionConfigs.set(CaptureRequestVendorTags.CINEMATIC_VIDEO_ENABLED, (Object) Byte.valueOf(this.mConfigs.isCinematicVideoEnabled() ? (byte) 1 : 0));
                applyVideoHdrModeIfNeed();
                if (C0124O00000oO.isMTKPlatform()) {
                    Log.d(TAG, "turns PQ feature on");
                    this.mSessionConfigs.set(CaptureRequestVendorTags.MTK_CONFIGURE_SETTING_PROPRIETARY, (Object) CaptureRequestVendorTags.MTK_CONFIGURE_SETTING_PROPRIETARY_ON);
                    MiCameraCompat.applyPqFeature(this.mPreviewRequestBuilder, true);
                    applyMtkQuickPreview();
                    if (CameraSettings.getHSRIntegerValue() == 60) {
                        z2 = true;
                    }
                    if (z2) {
                        this.mSessionConfigs.set(CaptureRequestVendorTags.HFPSVR_MODE, (Object) Integer.valueOf(1));
                        MiCameraCompat.applyHighFpsVideoRecordingMode(this.mPreviewRequestBuilder, true);
                        Log.d(TAG, "startRecordSession: turns hfpsmode on");
                    }
                    cameraDevice = this.mCameraDevice;
                    inputConfiguration = null;
                    build = this.mPreviewRequestBuilder.build();
                    captureSessionStateCallback = new CaptureSessionStateCallback(this.mSessionId, cameraPreviewCallback);
                    handler = this.mCameraHandler;
                } else {
                    CaptureRequestBuilder.applyCinematicVideo(this.mPreviewRequestBuilder, this.mCapabilities, this.mConfigs);
                    CaptureRequestBuilder.applyExposureCompensation(this.mPreviewRequestBuilder, 1, this.mCapabilities, this.mConfigs);
                    CaptureRequestBuilder.applySessionParameters(this.mPreviewRequestBuilder, this.mSessionConfigs);
                    cameraDevice = this.mCameraDevice;
                    inputConfiguration = null;
                    build = this.mPreviewRequestBuilder.build();
                    captureSessionStateCallback = new CaptureSessionStateCallback(this.mSessionId, cameraPreviewCallback);
                    handler = this.mCameraHandler;
                }
                CompatibilityUtils.createCaptureSessionWithSessionConfiguration(cameraDevice, i, inputConfiguration, arrayList, build, captureSessionStateCallback, handler);
            } catch (CameraAccessException e) {
                Log.e(TAG, "Failed to start recording session", (Throwable) e);
                i2 = e.getReason();
            } catch (IllegalStateException e2) {
                Log.e(TAG, "Failed to start recording session, IllegalState", (Throwable) e2);
                i2 = 256;
            }
        }
        return;
        notifyOnError(i2);
    }

    public void startRecording(boolean z) {
        int i;
        if (checkCaptureSession("startRecording")) {
            try {
                Log.d(TAG, "E: startRecording");
                if (this.mConfigs.isEnableRecordControl()) {
                    setVideoRecordControl(1);
                }
                this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(3);
                this.mPreviewRequestBuilder.addTarget(this.mPreviewSurface);
                if (!z) {
                    this.mPreviewRequestBuilder.addTarget(this.mRecordSurface);
                }
                applySettingsForVideo(this.mPreviewRequestBuilder);
                resumePreview();
                Log.d(TAG, "X: startRecording");
            } catch (CameraAccessException e) {
                Log.e(TAG, "Failed to start recording", (Throwable) e);
                i = e.getReason();
                notifyOnError(i);
            } catch (IllegalStateException e2) {
                Log.e(TAG, "Failed to start recording, IllegalState", (Throwable) e2);
                i = 256;
                notifyOnError(i);
            }
        }
    }

    public void startVideoPreviewSession(Surface surface, int i, int i2, Surface surface2, int i3, boolean z, CameraPreviewCallback cameraPreviewCallback) {
        startPreviewSessionImpl(surface, i, i2, surface2, i3, z, cameraPreviewCallback, 3);
    }

    public void stopFaceDetection() {
        Log.v(TAG, "stopFaceDetection");
        this.mConfigs.setFaceDetectionEnabled(false);
        CaptureRequestBuilder.applyFaceDetection(this.mPreviewRequestBuilder, this.mConfigs);
    }

    public void stopObjectTrack() {
    }

    public void stopPreviewCallback(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stopPreviewCallback(): isRelease = ");
        sb.append(z);
        Log.v(str, sb.toString());
        if (this.mPreviewCallbackType > 0 && this.mIsPreviewCallbackStarted && this.mPreviewImageReader != null) {
            this.mIsPreviewCallbackStarted = false;
            setPreviewCallback(null);
            setAnchorCallback(null);
            Surface surface = this.mPreviewImageReader.getSurface();
            this.mPreviewRequestBuilder.removeTarget(surface);
            surface.release();
            if (!z && checkCaptureSession("stopPreviewCallback")) {
                resumePreview();
            }
        }
    }

    public void stopRecording() {
        int i;
        String str = "stopRecording";
        if (checkCaptureSession(str)) {
            Log.d(TAG, str);
            if (this.mConfigs.isEnableRecordControl()) {
                try {
                    setVideoRecordControl(2);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    String str2 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to stop recording ");
                    sb.append(e.getMessage());
                    Log.k(6, str2, sb.toString());
                    i = e.getReason();
                } catch (IllegalStateException e2) {
                    Log.e(TAG, "Failed to stop recording, IllegalState", (Throwable) e2);
                    i = 256;
                }
            }
        }
        return;
        notifyOnError(i);
    }

    public void takePicture(@NonNull PictureCallback pictureCallback, @NonNull ParallelCallback parallelCallback) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("takePicture ");
        sb.append(pictureCallback);
        Log.v(str, sb.toString());
        setPictureCallback(pictureCallback);
        setParallelCallback(parallelCallback);
        triggerCapture();
    }

    public void takeSimplePicture(@NonNull PictureCallback pictureCallback, @NonNull ImageSaver imageSaver, @NonNull CameraScreenNail cameraScreenNail) {
        Log.v(TAG, "takeSimplePicture");
        setPictureCallback(pictureCallback);
        captureStillPicture();
        MiCamera2Shot miCamera2Shot = this.mMiCamera2Shot;
        if (miCamera2Shot != null && (miCamera2Shot instanceof MiCamera2ShotSimplePreview)) {
            cameraScreenNail.setPreviewSaveListener((PreviewSaveListener) miCamera2Shot);
            this.mMiCamera2Shot.setPictureCallback(getPictureCallback());
            ((MiCamera2ShotSimplePreview) this.mMiCamera2Shot).setImageSaver(imageSaver);
            this.mMiCamera2Shot.startShot();
        }
    }

    public void unlockExposure() {
        if (checkCaptureSession("unlockExposure")) {
            this.mCaptureCallback.setState(1);
            setAELock(false);
            CaptureRequestBuilder.applyAELock(this.mPreviewRequestBuilder, false);
            resumePreview();
        }
    }

    public void unregisterPreviewMetadata() {
        PreviewMetadata previewMetadata = this.mCameraPreviewMetadata;
        if (previewMetadata != null) {
            previewMetadata.unregisterPreviewMetadata();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00f4, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean updateDeferPreviewSession(Surface surface) {
        synchronized (this.mSessionLock) {
            if (this.mPreviewSurface == null) {
                this.mPreviewSurface = surface;
                this.mDeferPreviewSurface = surface;
            }
            if (this.mDeferOutputConfigurations.isEmpty()) {
                Log.d(TAG, "updateDeferPreviewSession: it is no need to update:");
                return false;
            }
            if (this.mCaptureSession != null) {
                if (this.mPreviewSurface != null) {
                    if (!isLocalParallelServiceReady()) {
                        Log.d(TAG, "updateDeferPreviewSession: ParallelService is not ready");
                        this.mHelperHandler.removeMessages(2);
                        this.mHelperHandler.sendEmptyMessageDelayed(2, 10);
                        return false;
                    }
                    this.mPreviewRequestBuilder.addTarget(this.mPreviewSurface);
                    try {
                        ArrayList arrayList = new ArrayList();
                        if (this.mFakeOutputTexture != null) {
                            OutputConfiguration outputConfiguration = (OutputConfiguration) this.mDeferOutputConfigurations.get(0);
                            this.mDeferOutputConfigurations.remove(0);
                            if (this.mSetRepeatingEarly) {
                                this.mPreviewRequestBuilder.removeTarget(outputConfiguration.getSurface());
                            }
                            outputConfiguration.addSurface(this.mPreviewSurface);
                            arrayList.add(outputConfiguration);
                        }
                        if (this.mEnableParallelSession && !this.mRemoteImageReaderList.isEmpty()) {
                            this.mParallelCaptureSurfaceList = prepareRemoteImageReader(null);
                            if (this.mParallelCaptureSurfaceList != null) {
                                for (int i = 0; i < this.mDeferOutputConfigurations.size(); i++) {
                                    OutputConfiguration outputConfiguration2 = (OutputConfiguration) this.mDeferOutputConfigurations.get(i);
                                    outputConfiguration2.addSurface((Surface) this.mParallelCaptureSurfaceList.get(i));
                                    arrayList.add(outputConfiguration2);
                                }
                            }
                        }
                        if (this.mEnableParallelSession) {
                            configMaxParallelRequestNumberLock();
                        }
                        this.mCaptureSession.finalizeOutputConfigurations(arrayList);
                        Log.d(TAG, "updateDeferPreviewSession: finalizeOutputConfigurations success");
                    } catch (Exception e) {
                        Log.e(TAG, "updateDeferPreviewSession: finalizeOutputConfigurations failed", (Throwable) e);
                    }
                    for (ImageReader close : this.mRemoteImageReaderList) {
                        close.close();
                    }
                    this.mRemoteImageReaderList.clear();
                    this.mDeferOutputConfigurations.clear();
                    if (this.mCaptureSessionStateCallback != null) {
                        this.mCaptureSessionStateCallback.onPreviewSessionSuccess();
                    }
                }
            }
            Log.d(TAG, "updateDeferPreviewSession: it is no ready to update:");
            return false;
        }
    }

    public void updateFlashAutoDetectionEnabled(boolean z) {
        this.mConfigs.updateFlashAutoDetectionEnabled(z);
    }

    public void updateFlashStateTimeLock() {
        if (this.mSupportFlashTimeLock) {
            if (FLASH_LOCK_DEBUG) {
                Log.d(TAG, "update flash state time lock!");
            }
            this.mLastFlashTimeMillis = System.currentTimeMillis();
        }
    }

    public void updateFrameNumber(long j) {
        this.mCurrentFrameNum = j;
    }

    public boolean useLegacyFlashStrategy() {
        return this.mConfigs.isUseLegacyFlashMode();
    }

    public boolean useSingleCaptureForHdrPlusMfnr(CameraCapabilities cameraCapabilities) {
        if (getId() == Camera2DataContainer.getInstance().getSATCameraId() && C0122O00000o.instance().getConfig().Oo0OO() == 0) {
            return true;
        }
        boolean z = CameraSettings.isHighQualityPreferred() && C0122O00000o.instance().OOOoo0O() && getCapabilities().getFacing() == 1 && !isMacroMode() && !getCameraConfigs().isDualBokehEnabled();
        int supportedHdrType = cameraCapabilities.getSupportedHdrType();
        if (supportedHdrType == 0) {
            return true;
        }
        if (supportedHdrType != 1) {
            return z;
        }
        return false;
    }
}
