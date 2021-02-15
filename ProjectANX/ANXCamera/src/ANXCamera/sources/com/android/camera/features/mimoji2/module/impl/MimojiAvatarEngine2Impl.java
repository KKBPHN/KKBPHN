package com.android.camera.features.mimoji2.module.impl;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.location.Location;
import android.media.Image;
import android.opengl.GLES20;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Size;
import android.widget.Toast;
import com.android.camera.ActivityBase;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.Thumbnail;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import com.android.camera.features.mimoji2.bean.MimojiBgInfo;
import com.android.camera.features.mimoji2.bean.MimojiInfo2;
import com.android.camera.features.mimoji2.bean.MimojiTimbreInfo;
import com.android.camera.features.mimoji2.module.MimojiModule;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiBottomList;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiEditor2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiFullScreenProtocol;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiVideoEditor;
import com.android.camera.features.mimoji2.utils.BitmapUtils2;
import com.android.camera.features.mimoji2.widget.helper.AvatarEngineManager2;
import com.android.camera.features.mimoji2.widget.helper.MimojiHelper2;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.Module;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BluetoothHeadset;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.MimojiGifEditor;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera.upgrade.UpgradeManager;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.CameraCapabilities;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigInfo;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigValue;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarProcessInfo;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarProfileResult;
import com.arcsoft.avatar2.AvatarEngine;
import com.arcsoft.avatar2.BackgroundInfo;
import com.arcsoft.avatar2.RecordModule;
import com.arcsoft.avatar2.RecordModule.MediaResultCallback;
import com.arcsoft.avatar2.extrascene.ExtraSceneEngine.AvatarExtraSceneTriggerCallback;
import com.arcsoft.avatar2.recoder.RecordingListener;
import com.arcsoft.avatar2.util.ASVLOFFSCREEN;
import com.arcsoft.avatar2.util.AsvloffscreenUtil;
import com.arcsoft.avatar2.util.LOG;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.ParallelTaskDataParameter.Builder;
import com.xiaomi.camera.core.PictureInfo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MimojiAvatarEngine2Impl implements MimojiAvatarEngine2 {
    public static final int DELETE_GIF = 3;
    public static final int DELETE_MIMOJI_ALL = 0;
    public static final int DELETE_MIMOJI_EMOTICON = 2;
    public static final int DELETE_MIMOJI_VIDEO = 1;
    private static final int FLAG_HAS_FACE = 10;
    private static final int FLAG_INIT_FACE = 0;
    private static final String GIF_SUBTITLE_ASSETS_PATH = "gif_subtitle/3336a65c52528c9c368e942d3dd307f8-le64.cache-3";
    private static final String GIF_SUBTITLE_CACHE_FILE = "3336a65c52528c9c368e942d3dd307f8-le64.cache-3";
    private static final String GIF_SUBTITLE_DIR;
    private static final int HANDLER_RECORDING_CURRENT_FILE_SIZE = 3;
    private static final int HANDLER_RECORDING_CURRENT_TIME = 1;
    private static final int HANDLER_RECORDING_MAX_DURATION_REACHED = 2;
    private static final int HANDLER_RECORDING_MAX_FILE_SIZE_REACHED = 4;
    private static final int HANDLER_RESOURCE_ERROR_BROKEN = 0;
    private static final long START_OFFSET_MS = 450;
    private static final String TAG = "Mimoji2Impl";
    /* access modifiers changed from: private */
    public ActivityBase mActivityBase;
    private AvatarEngine mAvatar;
    private String mAvatarConfigPath;
    private final Object mAvatarLock;
    private String mAvatarTemplatePath;
    private V6CameraGLSurfaceView mCameraView;
    private MediaResultCallback mCaptureCallback;
    /* access modifiers changed from: private */
    public Handler mCaptureHandler;
    private HandlerThread mCaptureThread;
    /* access modifiers changed from: private */
    public Context mContext;
    private CountDownTimer mCountDownTimer;
    private int mCurrentScreenOrientation = 0;
    private MimojiBgInfo mCurrentTempMimojiBgInfo;
    private int mDeviceRotation = 90;
    private int mDisplayOrientation;
    private Size mDrawSize;
    private int mFaceDectectResult;
    /* access modifiers changed from: private */
    public CountDownLatch mGetThumCountDownLatch;
    private Bitmap mGifBitmap;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private volatile boolean mIsAvatarInited;
    private boolean mIsFaceDetectSuccess;
    private boolean mIsFrontCamera;
    private boolean mIsGifOn;
    private int mIsNoFaceResult;
    /* access modifiers changed from: private */
    public boolean mIsRecordStopping;
    /* access modifiers changed from: private */
    public volatile boolean mIsRecording;
    private boolean mIsShutterButtonClick;
    private boolean mIsStopRender;
    private boolean mLastInitResult;
    private boolean mLastNeedBeauty;
    private Handler mLoadHandler;
    private HandlerThread mLoadThread;
    private MainContentProtocol mMainProtocol;
    /* access modifiers changed from: private */
    public int mMaxVideoDurationInMs;
    private MimojiEditor2 mMimojiEditor2;
    private MimojiStatusManager2 mMimojiStatusManager2;
    private boolean mNeedCapture;
    private boolean mNeedThumbnail;
    private int mOrientation;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private RecordModule mRecordModule;
    private RecordingListener mRecordingListener;
    private AvatarExtraSceneTriggerCallback mSceneTriggerCallback;
    /* access modifiers changed from: private */
    public int mStopRecordType;
    private int[] mTextureId;
    /* access modifiers changed from: private */
    public long mTotalRecordingTime;
    /* access modifiers changed from: private */
    public Handler mUiHandler;
    private int uiStyle;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/voip-data/.fccache/");
        GIF_SUBTITLE_DIR = sb.toString();
    }

    private MimojiAvatarEngine2Impl(ActivityBase activityBase) {
        String str = "";
        this.mAvatarTemplatePath = str;
        this.mAvatarConfigPath = str;
        this.mIsShutterButtonClick = false;
        this.mIsStopRender = true;
        this.mFaceDectectResult = 1;
        this.mIsFaceDetectSuccess = false;
        this.mNeedCapture = false;
        this.mNeedThumbnail = false;
        this.mLoadThread = new HandlerThread("LoadConfig");
        this.mCaptureThread = new HandlerThread("Capture");
        this.mAvatarLock = new Object();
        this.mTextureId = new int[1];
        this.mIsRecordStopping = false;
        this.mLastNeedBeauty = true;
        this.mLastInitResult = false;
        this.mIsNoFaceResult = -1;
        this.mCaptureCallback = new MediaResultCallback() {
            public /* synthetic */ void O000000o(ByteBuffer byteBuffer) {
                MimojiAvatarEngine2Impl.this.CaptureCallback(byteBuffer);
            }

            public /* synthetic */ void O00o0oOo() {
                ToastUtils.showToast(MimojiAvatarEngine2Impl.this.mContext, (int) R.string.mimoji_gif_record_time_short, 80);
            }

            public void onCaptureResult(ByteBuffer byteBuffer) {
                Log.d(MimojiAvatarEngine2Impl.TAG, "onCapture Result");
                MimojiAvatarEngine2Impl.this.mIsRecording = false;
                MimojiAvatarEngine2Impl.this.mIsRecordStopping = false;
                MimojiAvatarEngine2Impl.this.mCaptureHandler.post(new O00000Oo(this, byteBuffer));
                ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                if (actionProcessing != null) {
                    actionProcessing.announceForAccessibility(R.string.accessibility_camera_shutter_finish);
                }
            }

            public void onVideoResult(boolean z) {
                Log.d(MimojiAvatarEngine2Impl.TAG, "stop video record callback");
                if (MimojiAvatarEngine2Impl.this.mGetThumCountDownLatch != null) {
                    MimojiAvatarEngine2Impl.this.mGetThumCountDownLatch.countDown();
                }
                MimojiAvatarEngine2Impl.this.mIsRecording = false;
                MimojiAvatarEngine2Impl.this.mIsRecordStopping = false;
                if (!CameraSettings.isGifOn()) {
                    MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
                    if (mimojiVideoEditor != null) {
                        mimojiVideoEditor.combineVideoAudio(MimojiAvatarEngine2Impl.this.getVideoCache(), MimojiAvatarEngine2Impl.this.mStopRecordType);
                    }
                } else if (MimojiAvatarEngine2Impl.this.mTotalRecordingTime < 1000) {
                    MimojiGifEditor mimojiGifEditor = (MimojiGifEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(251);
                    if (mimojiGifEditor != null) {
                        mimojiGifEditor.coverGifError();
                    }
                    MimojiAvatarEngine2Impl.this.mUiHandler.post(new O000000o(this));
                } else {
                    MimojiGifEditor mimojiGifEditor2 = (MimojiGifEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(251);
                    if (mimojiGifEditor2 != null) {
                        mimojiGifEditor2.combineVideoAudio(MimojiAvatarEngine2Impl.this.getVideoCache(), MimojiAvatarEngine2Impl.this.mTotalRecordingTime);
                    }
                }
            }
        };
        this.mRecordingListener = new RecordingListener() {
            public void onRecordingListener(int i, Object obj) {
                int i2;
                Message obtainMessage = MimojiAvatarEngine2Impl.this.mHandler.obtainMessage();
                switch (i) {
                    case 257:
                        obtainMessage.arg1 = (int) ((((Long) obj).longValue() / 1000) / 1000);
                        i2 = 2;
                        break;
                    case 258:
                        long longValue = (((Long) obj).longValue() / 1000) / 1000;
                        obtainMessage.arg1 = (int) longValue;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onRecordingListener_time = ");
                        sb.append(longValue);
                        Log.d(MimojiAvatarEngine2Impl.TAG, sb.toString());
                        i2 = 1;
                        break;
                    case 259:
                        obtainMessage.arg1 = (int) (((Long) obj).longValue() / 1024);
                        i2 = 4;
                        break;
                    case 260:
                        obtainMessage.arg1 = (int) (((Long) obj).longValue() / 1024);
                        i2 = 3;
                        break;
                    default:
                        obtainMessage.sendToTarget();
                }
                obtainMessage.what = i2;
                obtainMessage.sendToTarget();
            }
        };
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                int i = message.what;
                if (i != 0 && i != 1 && i != 2) {
                }
            }
        };
        boolean OOO00 = C0122O00000o.instance().getConfig().OOO00();
        String str2 = TAG;
        if (OOO00) {
            Log.d(str2, "mimoji2 init load so... ");
            System.loadLibrary("mimoji_tracking");
            System.loadLibrary("vvc++_shared");
            System.loadLibrary("ffmpeg");
            System.loadLibrary("mimoji_soundsupport");
            System.loadLibrary("mimoji_video2gif");
            System.loadLibrary("mimoji_avatarengine");
            System.loadLibrary("mimoji_jni");
        }
        this.mActivityBase = activityBase;
        this.mCameraView = activityBase.getGLView();
        this.mMaxVideoDurationInMs = CameraSettings.isGifOn() ? 5000 : 15000;
        this.mContext = activityBase.getCameraAppImpl().getApplicationContext();
        this.mMainProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        this.mMimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
        this.mLoadThread.start();
        this.mLoadHandler = new Handler(this.mLoadThread.getLooper());
        this.mCaptureThread.start();
        this.mCaptureHandler = new Handler(this.mCaptureThread.getLooper());
        this.mUiHandler = new Handler(activityBase.getMainLooper());
        this.mMimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
        this.mSceneTriggerCallback = new AvatarExtraSceneTriggerCallback() {
            public void onExtraSceneTrigger(String str, int i) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(i);
                CameraStatUtils.trackMimojiTrigger(sb.toString());
            }
        };
        setIsAvatarInited(false);
        Log.w(str2, "MimojiAvatarEngine2Impl:  constructor");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x01d9  */
    /* JADX WARNING: Removed duplicated region for block: B:105:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0089 A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x008b A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a9 A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00af A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00c3 A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00c4 A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0106 A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0107 A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0113 A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x011a A[Catch:{ Exception -> 0x0148, all -> 0x0145 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void CaptureCallback(ByteBuffer byteBuffer) {
        Bitmap bitmap;
        Bitmap bitmap2;
        Module currentModule;
        Module currentModule2;
        ByteBuffer byteBuffer2 = byteBuffer;
        Bitmap bitmap3 = null;
        try {
            if (this.mActivityBase == null || byteBuffer2 == null || !byteBuffer.hasArray()) {
                bitmap = null;
            } else {
                bitmap2 = Bitmap.createBitmap(this.mDrawSize.getWidth(), this.mDrawSize.getHeight(), Config.ARGB_8888);
                try {
                    bitmap2.copyPixelsFromBuffer(byteBuffer2);
                    Matrix matrix = new Matrix();
                    if (this.mIsFrontCamera) {
                        if (this.mIsFrontCamera) {
                            if (this.mDeviceRotation != 90) {
                                if (this.mDeviceRotation == 270) {
                                }
                            }
                        }
                        if (this.mDeviceRotation % 180 == 0) {
                            matrix.postScale(-1.0f, 1.0f);
                        }
                        bitmap = Bitmap.createBitmap(bitmap2, 0, 0, this.mDrawSize.getWidth(), this.mDrawSize.getHeight(), matrix, false);
                        int i = 0;
                        byte[] bitmapData = Util.getBitmapData(bitmap, CameraSettings.getEncodingQuality(false).toInteger(false));
                        int i2 = (this.mIsFrontCamera || this.mDeviceRotation % 180 != 0) ? this.mDeviceRotation : (this.mDeviceRotation + 180) % m.cQ;
                        Thumbnail createThumbnail = Thumbnail.createThumbnail(null, !this.mIsFrontCamera ? bitmap2 : bitmap, i2, this.mIsFrontCamera);
                        createThumbnail.startWaitingForUri();
                        int i3 = 1;
                        this.mActivityBase.getThumbnailUpdater().setThumbnail(createThumbnail, true, true);
                        MimojiModule mimojiModule = (MimojiModule) this.mActivityBase.getCurrentModule();
                        ParallelTaskData parallelTaskData = new ParallelTaskData(mimojiModule == null ? mimojiModule.getActualCameraId() : 0, System.currentTimeMillis(), -4, null);
                        parallelTaskData.fillJpegData(bitmapData, 0);
                        if (this.mIsFrontCamera) {
                            i3 = 0;
                        }
                        int jpegRotation = (Util.getJpegRotation(i3, this.mDeviceRotation) + 270) % m.cQ;
                        Builder builder = new Builder(this.mDrawSize, this.mDrawSize, this.mDrawSize, 256);
                        Location currentLocation = LocationManager.instance().getCurrentLocation();
                        Builder filterId = builder.setHasDualWaterMark(CameraSettings.isDualCameraWaterMarkOpen()).setJpegRotation(jpegRotation).setJpegQuality(CameraSettings.getEncodingQuality(false).toInteger(false)).setFilterId(FilterInfo.FILTER_ID_NONE);
                        if (-1 == this.mOrientation) {
                            i = this.mOrientation;
                        }
                        parallelTaskData.fillParameter(filterId.setOrientation(i).setTimeWaterMarkString(!CameraSettings.isTimeWaterMarkOpen() ? Util.getTimeWatermark(this.mActivityBase) : null).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setPictureInfo(getPictureInfo()).setLocation(currentLocation).build());
                        this.mActivityBase.getImageSaver().onParallelProcessFinish(parallelTaskData, null, null);
                        bitmap3 = bitmap2;
                    }
                    matrix.postScale(1.0f, -1.0f);
                    bitmap = Bitmap.createBitmap(bitmap2, 0, 0, this.mDrawSize.getWidth(), this.mDrawSize.getHeight(), matrix, false);
                    int i4 = 0;
                    try {
                        byte[] bitmapData2 = Util.getBitmapData(bitmap, CameraSettings.getEncodingQuality(false).toInteger(false));
                        if (this.mIsFrontCamera) {
                        }
                        Thumbnail createThumbnail2 = Thumbnail.createThumbnail(null, !this.mIsFrontCamera ? bitmap2 : bitmap, i2, this.mIsFrontCamera);
                        createThumbnail2.startWaitingForUri();
                        int i32 = 1;
                        this.mActivityBase.getThumbnailUpdater().setThumbnail(createThumbnail2, true, true);
                        MimojiModule mimojiModule2 = (MimojiModule) this.mActivityBase.getCurrentModule();
                        ParallelTaskData parallelTaskData2 = new ParallelTaskData(mimojiModule2 == null ? mimojiModule2.getActualCameraId() : 0, System.currentTimeMillis(), -4, null);
                        parallelTaskData2.fillJpegData(bitmapData2, 0);
                        if (this.mIsFrontCamera) {
                        }
                        int jpegRotation2 = (Util.getJpegRotation(i32, this.mDeviceRotation) + 270) % m.cQ;
                        Builder builder2 = new Builder(this.mDrawSize, this.mDrawSize, this.mDrawSize, 256);
                        Location currentLocation2 = LocationManager.instance().getCurrentLocation();
                        Builder filterId2 = builder2.setHasDualWaterMark(CameraSettings.isDualCameraWaterMarkOpen()).setJpegRotation(jpegRotation2).setJpegQuality(CameraSettings.getEncodingQuality(false).toInteger(false)).setFilterId(FilterInfo.FILTER_ID_NONE);
                        if (-1 == this.mOrientation) {
                        }
                        parallelTaskData2.fillParameter(filterId2.setOrientation(i4).setTimeWaterMarkString(!CameraSettings.isTimeWaterMarkOpen() ? Util.getTimeWatermark(this.mActivityBase) : null).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setPictureInfo(getPictureInfo()).setLocation(currentLocation2).build());
                        this.mActivityBase.getImageSaver().onParallelProcessFinish(parallelTaskData2, null, null);
                        bitmap3 = bitmap2;
                    } catch (Exception e) {
                        e = e;
                        bitmap3 = bitmap2;
                        String str = TAG;
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mimoji void CaptureCallback[byteBuffer] exception ");
                            sb.append(e);
                            Log.e(str, sb.toString());
                            if (bitmap3 != null && !bitmap3.isRecycled()) {
                                bitmap3.recycle();
                            }
                            if (bitmap != null && !bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                            currentModule2 = this.mActivityBase.getCurrentModule();
                            if (!(currentModule2 instanceof MimojiModule)) {
                                return;
                            }
                            ((MimojiModule) currentModule2).onMimojiCaptureCallback();
                        } catch (Throwable th) {
                            th = th;
                            bitmap2 = bitmap3;
                            if (bitmap2 != null && !bitmap2.isRecycled()) {
                                bitmap2.recycle();
                            }
                            if (bitmap != null && !bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                            currentModule = this.mActivityBase.getCurrentModule();
                            if (currentModule instanceof MimojiModule) {
                                ((MimojiModule) currentModule).onMimojiCaptureCallback();
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        bitmap2.recycle();
                        bitmap.recycle();
                        currentModule = this.mActivityBase.getCurrentModule();
                        if (currentModule instanceof MimojiModule) {
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    bitmap = null;
                    bitmap3 = bitmap2;
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("mimoji void CaptureCallback[byteBuffer] exception ");
                    sb2.append(e);
                    Log.e(str2, sb2.toString());
                    bitmap3.recycle();
                    bitmap.recycle();
                    currentModule2 = this.mActivityBase.getCurrentModule();
                    if (!(currentModule2 instanceof MimojiModule)) {
                    }
                    ((MimojiModule) currentModule2).onMimojiCaptureCallback();
                } catch (Throwable th3) {
                    th = th3;
                    bitmap = null;
                    bitmap2.recycle();
                    bitmap.recycle();
                    currentModule = this.mActivityBase.getCurrentModule();
                    if (currentModule instanceof MimojiModule) {
                    }
                    throw th;
                }
            }
            if (bitmap3 != null && !bitmap3.isRecycled()) {
                bitmap3.recycle();
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            currentModule2 = this.mActivityBase.getCurrentModule();
            if (!(currentModule2 instanceof MimojiModule)) {
                return;
            }
        } catch (Exception e3) {
            e = e3;
            bitmap = null;
            String str22 = TAG;
            StringBuilder sb22 = new StringBuilder();
            sb22.append("mimoji void CaptureCallback[byteBuffer] exception ");
            sb22.append(e);
            Log.e(str22, sb22.toString());
            bitmap3.recycle();
            bitmap.recycle();
            currentModule2 = this.mActivityBase.getCurrentModule();
            if (!(currentModule2 instanceof MimojiModule)) {
            }
            ((MimojiModule) currentModule2).onMimojiCaptureCallback();
        } catch (Throwable th4) {
            th = th4;
            bitmap2 = null;
            bitmap = null;
            bitmap2.recycle();
            bitmap.recycle();
            currentModule = this.mActivityBase.getCurrentModule();
            if (currentModule instanceof MimojiModule) {
            }
            throw th;
        }
        ((MimojiModule) currentModule2).onMimojiCaptureCallback();
    }

    static /* synthetic */ void O000000o(MimojiBottomList mimojiBottomList, boolean z) {
        mimojiBottomList.firstProgressShow(z);
        mimojiBottomList.refreshMimojiList();
    }

    static /* synthetic */ void O0000o0o(int i) {
    }

    private void animateCapture() {
        if (CameraSettings.isCameraSoundOpen()) {
            this.mActivityBase.playCameraSound(0);
        }
    }

    public static MimojiAvatarEngine2Impl create(ActivityBase activityBase) {
        return new MimojiAvatarEngine2Impl(activityBase);
    }

    private void createAvatar(byte[] bArr, int i, int i2) {
        int avatarProfile;
        Context context;
        int i3;
        String str = this.mAvatarTemplatePath;
        String str2 = AvatarEngineManager2.TEMPLATE_PATH_HUMAN;
        if (str != str2) {
            this.mAvatarTemplatePath = str2;
            this.mAvatarConfigPath = "";
            this.mAvatar.setTemplatePath(str2);
        }
        ASAvatarProfileResult aSAvatarProfileResult = new ASAvatarProfileResult();
        synchronized (this.mAvatarLock) {
            avatarProfile = this.mAvatar.avatarProfile(AvatarEngineManager2.TEMPLATE_PATH_HUMAN, i, i2, i * 4, bArr, 0, false, aSAvatarProfileResult, null, C0278O0000o0o.INSTANCE);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("avatarProfile res: ");
        sb.append(avatarProfile);
        sb.append(", status:");
        sb.append(aSAvatarProfileResult.status);
        sb.append(", gender: ");
        sb.append(aSAvatarProfileResult.gender);
        LOG.d(TAG, sb.toString());
        int i4 = aSAvatarProfileResult.status;
        if (i4 == 254 || i4 == 246) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("result = ");
            sb2.append(avatarProfile);
            Log.d(TAG, sb2.toString());
            this.mUiHandler.post(new O0000o00(this));
            return;
        }
        if (i4 == 1) {
            context = this.mContext;
            i3 = R.string.mimoji_detect_no_face_failed;
        } else if ((i4 & 2) == 0) {
            context = this.mContext;
            i3 = R.string.mimoji_detect_facial_failed;
        } else if ((i4 & 4) == 0) {
            context = this.mContext;
            i3 = R.string.mimoji_detect_hairstyle_failed;
        } else if ((i4 & 8) == 0) {
            context = this.mContext;
            i3 = R.string.mimoji_detect_haircolor_failed;
        } else if ((i4 & 16) == 0) {
            context = this.mContext;
            i3 = R.string.mimoji_detect_gender_failed;
        } else if ((i4 & 32) == 0) {
            context = this.mContext;
            i3 = R.string.mimoji_detect_skincolor_failed;
        } else if ((i4 & 64) == 0) {
            context = this.mContext;
            i3 = R.string.mimoji_detect_glass_failed;
        } else if ((i4 & 128) == 0) {
            context = this.mContext;
            i3 = R.string.mimoji_detect_faceshape_failed;
        } else {
            context = this.mContext;
            i3 = R.string.mimoji_detect_unknow_failed;
        }
        Toast.makeText(context, i3, 0).show();
        ActivityBase activityBase = this.mActivityBase;
        if (activityBase != null) {
            activityBase.runOnUiThread(new O0000o0(this));
        }
    }

    private void destoryAll() {
        this.mCaptureHandler.post(new O00000o(this, hashCode()));
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
        if (isDualCameraWaterMarkOpen || isFrontCameraWaterMarkOpen) {
            isDualCameraWaterMarkOpen = false;
            isFrontCameraWaterMarkOpen = true;
        }
        boolean z = isDualCameraWaterMarkOpen;
        boolean z2 = isFrontCameraWaterMarkOpen;
        if (z) {
            resourceFloat = CameraSettings.getResourceFloat(R.dimen.dualcamera_watermark_size_ratio, 0.0f);
            resourceFloat2 = CameraSettings.getResourceFloat(R.dimen.dualcamera_watermark_padding_x_ratio, 0.0f);
            i = R.dimen.dualcamera_watermark_padding_y_ratio;
        } else if (z2) {
            resourceFloat = CameraSettings.getResourceFloat(R.dimen.frontcamera_watermark_size_ratio, 0.0f);
            resourceFloat2 = CameraSettings.getResourceFloat(R.dimen.frontcamera_watermark_padding_x_ratio, 0.0f);
            i = R.dimen.frontcamera_watermark_padding_y_ratio;
        } else {
            f3 = 0.0f;
            f2 = 0.0f;
            f = 0.0f;
            DeviceWatermarkParam deviceWatermarkParam = new DeviceWatermarkParam(z, z2, CameraSettings.isUltraPixelRearOn(), CameraSettings.getDualCameraWaterMarkFilePathVendor(), f3, f2, f);
            return deviceWatermarkParam;
        }
        f = CameraSettings.getResourceFloat(i, 0.0f);
        f3 = resourceFloat;
        f2 = resourceFloat2;
        DeviceWatermarkParam deviceWatermarkParam2 = new DeviceWatermarkParam(z, z2, CameraSettings.isUltraPixelRearOn(), CameraSettings.getDualCameraWaterMarkFilePathVendor(), f3, f2, f);
        return deviceWatermarkParam2;
    }

    private Map getExtraMimojiPara() {
        String str;
        MimojiInfo2 currentMimojiInfo = this.mMimojiStatusManager2.getCurrentMimojiInfo();
        HashMap hashMap = new HashMap();
        String str2 = "";
        if (!TextUtils.isEmpty(this.mAvatarTemplatePath)) {
            String str3 = this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_HUMAN) ? "person" : this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_BEAR) ? "bear" : this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_ROYAN) ? "royan" : this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_RABBIT) ? "rabbit" : this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_RABBIT2) ? AvatarEngineManager2.CONFIG_PATH_FAKE_RABBIT2 : this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_FROG) ? AvatarEngineManager2.CONFIG_PATH_FAKE_FROG : this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_CAT) ? "cat" : str2;
            if (currentMimojiInfo != null && currentMimojiInfo.getFrame() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append("_hat");
                str3 = sb.toString();
            }
            hashMap.put(Mimoji.MIMOJI_SAVE_CARTOON, str3);
        }
        AvatarEngine avatarEngine = this.mAvatar;
        String extraSceneName = avatarEngine == null ? null : avatarEngine.getExtraSceneName();
        if (this.mAvatar != null) {
            ASAvatarConfigValue aSAvatarConfigValue = new ASAvatarConfigValue();
            this.mAvatar.getConfigValue(aSAvatarConfigValue);
            boolean isExtraResource = AvatarEngineManager2.isExtraResource(aSAvatarConfigValue.configHeadwearStyleID);
            String str4 = " ";
            String str5 = Mimoji.MIMOJI_SAVE_EXTRA_SCENE;
            if (isExtraResource) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(extraSceneName);
                sb2.append(str4);
                sb2.append(aSAvatarConfigValue.configHeadwearStyleID);
                hashMap.put(str5, sb2.toString());
            }
            if (currentMimojiInfo != null && currentMimojiInfo.isIsPreHuman()) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(extraSceneName);
                sb3.append(str4);
                sb3.append(currentMimojiInfo.mConfigPath);
                hashMap.put(str5, sb3.toString());
            }
        }
        MimojiTimbreInfo currentMimojiTimbreInfo = this.mMimojiStatusManager2.getCurrentMimojiTimbreInfo();
        String str6 = "null";
        if (currentMimojiTimbreInfo == null) {
            str = str6;
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(currentMimojiTimbreInfo.getTimbreId());
            sb4.append(str2);
            str = sb4.toString();
        }
        hashMap.put(Mimoji.MIMOJI_SAVE_CHANGE_TIMBRE, str);
        MimojiBgInfo currentMimojiBgInfo = this.mMimojiStatusManager2.getCurrentMimojiBgInfo();
        if (currentMimojiBgInfo != null) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(currentMimojiBgInfo.getBgId());
            sb5.append(str2);
            str6 = sb5.toString();
        }
        hashMap.put(Mimoji.MIMOJI_SAVE_CHANGE_BG, str6);
        return hashMap;
    }

    private Map getMimojiPara() {
        String str;
        Map hashMap = new HashMap();
        boolean isNeedShowAvatar = isNeedShowAvatar();
        String str2 = Mimoji.MIMOJI_CATEGORY;
        String str3 = "";
        String str4 = "null";
        if (isNeedShowAvatar) {
            ASAvatarConfigValue aSAvatarConfigValue = new ASAvatarConfigValue();
            this.mAvatar.getConfigValue(aSAvatarConfigValue);
            hashMap = AvatarEngineManager2.getMimojiConfigValue(aSAvatarConfigValue);
            String str5 = this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_HUMAN) ? "custom" : this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_BEAR) ? "bear" : this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_ROYAN) ? "royan" : this.mAvatarTemplatePath.equals(AvatarEngineManager2.TEMPLATE_PATH_RABBIT) ? "rabbit" : str3;
            hashMap.put(str2, str5);
        } else {
            hashMap.put(str2, str4);
        }
        MimojiInfo2 currentMimojiInfo = this.mMimojiStatusManager2.getCurrentMimojiInfo();
        AvatarEngine avatarEngine = this.mAvatar;
        String extraSceneName = avatarEngine == null ? null : avatarEngine.getExtraSceneName();
        if (!TextUtils.isEmpty(extraSceneName)) {
            hashMap.put(Mimoji.MIMOJI_SAVE_EXTRA_SCENE, extraSceneName);
        }
        if (currentMimojiInfo != null) {
            String str6 = currentMimojiInfo.mAvatarTemplatePath;
            if (str6 != null && AvatarEngineManager2.isPreCartoonModel(str6)) {
                hashMap.put(Mimoji.MIMOJI_SAVE_CARTOON, currentMimojiInfo.mAvatarTemplatePath);
            }
        }
        MimojiTimbreInfo currentMimojiTimbreInfo = this.mMimojiStatusManager2.getCurrentMimojiTimbreInfo();
        if (currentMimojiTimbreInfo == null) {
            str = str4;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(currentMimojiTimbreInfo.getTimbreId());
            sb.append(str3);
            str = sb.toString();
        }
        hashMap.put(Mimoji.MIMOJI_SAVE_CHANGE_TIMBRE, str);
        MimojiBgInfo currentMimojiBgInfo = this.mMimojiStatusManager2.getCurrentMimojiBgInfo();
        if (currentMimojiBgInfo != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(currentMimojiBgInfo.getBackgroundInfo().getName());
            sb2.append(str3);
            str4 = sb2.toString();
        }
        hashMap.put(Mimoji.MIMOJI_SAVE_CHANGE_BG, str4);
        return hashMap;
    }

    private PictureInfo getPictureInfo() {
        PictureInfo opMode = new PictureInfo().setFrontMirror(isFrontMirror()).setSensorType(true).setBokehFrontCamera(false).setHdrType("off").setOpMode(getOperatingMode());
        opMode.end();
        return opMode;
    }

    private void initMimojiDataResource() {
        if (!FileUtils.checkFileDirectoryConsist(MimojiHelper2.DATA_DIR)) {
            String str = TAG;
            Log.w(str, "MimojiAvatarEngine2Impl: initMimojiResource unzip...");
            long currentTimeMillis = System.currentTimeMillis();
            try {
                FileUtils.delDir(MimojiHelper2.DATA_DIR);
                FileUtils.makeNoMediaDir(MimojiHelper2.DATA_DIR);
                Util.verifyFileZip(this.mContext, "vendor/camera/mimoji/data.zip", MimojiHelper2.MIMOJI_DIR, 32768);
                StringBuilder sb = new StringBuilder();
                sb.append("init model spend time = ");
                sb.append(System.currentTimeMillis() - currentTimeMillis);
                Log.d(str, sb.toString());
            } catch (Exception e) {
                Log.e(str, "verify asset model zip failed...", (Throwable) e);
                CameraSettings.setMimojiModleVersion(null);
                FileUtils.delDir(MimojiHelper2.DATA_DIR);
            }
        }
    }

    private boolean isFrontMirror() {
        if (!this.mIsFrontCamera) {
            return false;
        }
        if (CameraSettings.isLiveShotOn()) {
            return true;
        }
        return CameraSettings.isFrontMirror();
    }

    private void onProfileFinish() {
        Log.d(TAG, "onProfileFinish");
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onPostSavingFinish();
        }
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertMimojiFaceDetect(false, -1);
        }
        this.mMimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
        releaseRender();
        this.mMainProtocol.mimojiEnd();
        this.mMimojiStatusManager2.setMode(6);
        MimojiEditor2 mimojiEditor2 = this.mMimojiEditor2;
        if (mimojiEditor2 != null) {
            mimojiEditor2.startMimojiEdit(203);
        }
        ActivityBase activityBase = this.mActivityBase;
        if (activityBase != null) {
            ((MimojiModule) activityBase.getCurrentModule()).onMimojiCreateCompleted(true);
        }
        CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_CREATE_CAPTURE, BaseEvent.CREATE);
    }

    private void showLoadProgress(boolean z) {
        MimojiBottomList mimojiBottomList = (MimojiBottomList) ModeCoordinatorImpl.getInstance().getAttachProtocol(248);
        if (mimojiBottomList != null) {
            this.mUiHandler.post(new C0276O0000Ooo(mimojiBottomList, z));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateBeauty(boolean z) {
        boolean z2;
        ActivityBase activityBase = this.mActivityBase;
        if (activityBase != null) {
            BaseModule baseModule = (BaseModule) activityBase.getCurrentModule();
            if (baseModule instanceof MimojiModule) {
                int i = z ? 3 : 0;
                int i2 = z ? 40 : 0;
                ComponentRunningShine componentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
                if (componentRunningShine.supportBeautyLevel()) {
                    if (CameraSettings.getFaceBeautyRatio("pref_beautify_level_key_capture") != i) {
                        CameraSettings.setFaceBeautyLevel(i);
                    }
                    z2 = false;
                    if (z2) {
                        baseModule.updatePreferenceInWorkThread(13);
                    }
                }
                if (componentRunningShine.supportSmoothLevel() && CameraSettings.getFaceBeautyRatio("pref_beautify_skin_smooth_ratio_key") != i2) {
                    CameraSettings.setFaceBeautySmoothLevel(i2);
                }
                z2 = false;
                if (z2) {
                }
                z2 = true;
                if (z2) {
                }
            }
        }
    }

    private void updateVideoOrientation(int i) {
        int i2;
        if ((i > 315 && i <= 360) || (i >= 0 && i <= 45)) {
            i2 = 0;
        } else if (i > 45 && i <= 135) {
            i2 = 90;
        } else if (i > 135 && i <= 225) {
            i2 = 180;
        } else if (i > 225 && i <= 315) {
            i2 = 270;
        } else {
            return;
        }
        this.mCurrentScreenOrientation = i2;
    }

    public /* synthetic */ void O000000o(ByteBuffer byteBuffer, int i, int i2) {
        Bitmap bitmap;
        byte[] array = byteBuffer.array();
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(array));
        if (this.mIsGifOn) {
            int i3 = this.mCurrentScreenOrientation;
            if (i3 == 0 || this.mDeviceRotation == 180) {
                bitmap = Util.rotateAndMirror(createBitmap, 180, true);
            } else {
                bitmap = Util.rotateAndMirror(createBitmap, i3 == 270 ? 90 : -90, true);
            }
            this.mGifBitmap = bitmap;
            return;
        }
        Bitmap rotateAndMirror = Util.rotateAndMirror(createBitmap, 180, true);
        MimojiFullScreenProtocol mimojiFullScreenProtocol = (MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249);
        if (mimojiFullScreenProtocol != null) {
            mimojiFullScreenProtocol.setPreviewCover(rotateAndMirror);
        }
    }

    public /* synthetic */ void O0000o(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("avatar destroy | ");
        sb.append(i);
        Log.d(TAG, sb.toString());
        synchronized (this.mAvatarLock) {
            if (isAvatarInited() && !this.mMimojiStatusManager2.isInMimojiEdit()) {
                this.mAvatar.saveConfig(AvatarEngineManager2.TempEditConfigPath);
            }
            if (this.mRecordModule != null) {
                this.mRecordModule.resetExtraScene();
                this.mRecordModule.unInit();
            }
            AvatarEngineManager2.getInstance().release();
            this.mActivityBase = null;
        }
    }

    public /* synthetic */ void O0000oO0(int i) {
        synchronized (this.mAvatarLock) {
            if (this.mAvatar != null) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("releaseRender | ");
                sb.append(i);
                Log.d(str, sb.toString());
                this.mAvatar.releaseRender();
            }
        }
    }

    public /* synthetic */ void O00o() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            boolean z = getIsNoFaceResult() && isNeedShowAvatar();
            topAlert.alertMimojiFaceDetect(z, R.string.mimoji_check_no_face);
        }
    }

    public /* synthetic */ void O00o0oo() {
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onPostSavingFinish();
        }
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertMimojiFaceDetect(false, -1);
        }
        this.mMainProtocol.mimojiEnd();
        ((MimojiModule) this.mActivityBase.getCurrentModule()).onMimojiCreateCompleted(false);
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).showOrHideMimojiProgress(false);
        backToPreview(false, false);
    }

    public /* synthetic */ void O00o0oo0() {
        setDisableSingleTapUp(true);
        onProfileFinish();
    }

    public /* synthetic */ void O00o0ooO() {
        if (!isAvatarInited()) {
            Log.d(TAG, "avatar need really init");
            int formatFileSize = (int) FileUtils.formatFileSize(FileUtils.getFileSize(MimojiHelper2.MODEL_PATH), 3);
            String O0ooOOo = C0122O00000o.instance().O0ooOOo();
            if (O0ooOOo.equals(CameraSettings.getMimojiModleVersion()) && FileUtils.checkFileDirectoryConsist(MimojiHelper2.DATA_DIR) && this.mMimojiStatusManager2.IsLoading()) {
                showLoadProgress(true);
            } else if ((!O0ooOOo.equals(CameraSettings.getMimojiModleVersion()) || formatFileSize < 200) && this.mMimojiStatusManager2.isInMimojiPreview()) {
                initMimojiResource();
            } else {
                reloadConfig();
                onMimojiInitFinish();
            }
        }
    }

    public /* synthetic */ void O00o0ooo() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            boolean z = getIsNoFaceResult() && isNeedShowAvatar();
            topAlert.alertMimojiFaceDetect(z, R.string.mimoji_check_no_face);
        }
    }

    public /* synthetic */ void O00oO00() {
        RecordModule recordModule = this.mRecordModule;
        if (recordModule != null) {
            recordModule.releaseRecordGLInfo();
        }
    }

    public /* synthetic */ void O00oO000() {
        int i;
        int i2;
        int i3;
        RecordingListener recordingListener;
        String str;
        RecordModule recordModule;
        if (this.mRecordModule != null) {
            String str2 = CameraSettings.getVideoEncoder() == 5 ? "video/hevc" : "video/avc";
            if (!CameraSettings.isGifOn()) {
                deleteMimojiCache(1);
                FileUtils.makeNoMediaDir(MimojiHelper2.VIDEO_CACHE_DIR);
                recordModule = this.mRecordModule;
                str = getVideoCache();
                recordingListener = this.mRecordingListener;
                i3 = this.mCurrentScreenOrientation;
                i2 = this.mPreviewWidth;
                i = this.mPreviewHeight;
            } else {
                deleteMimojiCache(3);
                FileUtils.makeNoMediaDir(MimojiHelper2.GIF_CACHE_DIR);
                recordModule = this.mRecordModule;
                str = getVideoCache();
                recordingListener = this.mRecordingListener;
                i3 = this.mCurrentScreenOrientation;
                i2 = 500;
                i = 500;
            }
            recordModule.startRecording(str, recordingListener, i3, i2, i, 10000000, str2);
            this.mUiHandler.post(new O00000o0(this));
        }
    }

    public /* synthetic */ void O00oO00O() {
        this.mRecordModule.stopRecording();
        this.mCameraView.queueEvent(new C0277O0000o0O(this));
    }

    public /* synthetic */ void O00oO0OO() {
        if (this.mAvatar != null) {
            Log.d(TAG, "release releaseRender | ");
            this.mAvatar.releaseRender();
        }
        RecordModule recordModule = this.mRecordModule;
        if (recordModule != null) {
            recordModule.releaseRecordGLInfo();
        }
        destoryAll();
    }

    public void backToPreview(boolean z, boolean z2) {
        MimojiInfo2 currentMimojiInfo = this.mMimojiStatusManager2.getCurrentMimojiInfo();
        this.mMimojiStatusManager2.setMode(2);
        this.mIsStopRender = false;
        onMimojiSelect(currentMimojiInfo, true);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        bottomPopupTips.reInitTipImage();
        topAlert.alertAiDetectTipHint(8, R.string.mimoji_check_normal, -1);
        topAlert.alertMimojiFaceDetect(false, -1);
        int[] iArr = (this.mMimojiStatusManager2.getMimojiRecordState() != 1 || !CameraSettings.isFrontCamera()) ? new int[]{197, 193, 162} : new int[]{197, 162};
        topAlert.enableMenuItem(true, iArr);
        topAlert.reInitAlert(true);
        topAlert.updateConfigItem(193);
        if (!z2 || this.mMimojiStatusManager2.getMimojiPanelState() == 1) {
            bottomPopupTips.updateMimojiBottomTipImage();
        } else {
            bottomPopupTips.showMimojiPanel(1);
        }
        AvatarEngine avatarEngine = this.mAvatar;
        if (avatarEngine != null) {
            avatarEngine.setRenderScene(true, 1.0f);
        }
        setDisableSingleTapUp(false);
    }

    public boolean changeIsNoFaceResult(boolean z) {
        if (!z) {
            int i = this.mIsNoFaceResult;
            if (i > 10) {
                return false;
            }
            this.mIsNoFaceResult = i + 1;
            if (this.mIsNoFaceResult == 10) {
                return true;
            }
        } else if (this.mIsNoFaceResult != 0) {
            this.mIsNoFaceResult = 0;
            return true;
        }
        return false;
    }

    public void changeToGif(boolean z) {
        setIsAvatarInited(false);
        if (z) {
            CameraStatUtils.trackMimoji2Click(null, Mimoji.GIF);
            MimojiStatusManager2 mimojiStatusManager2 = this.mMimojiStatusManager2;
            mimojiStatusManager2.setMimojiRecordStateFromGif(mimojiStatusManager2.getMimojiRecordState());
            return;
        }
        MimojiStatusManager2 mimojiStatusManager22 = this.mMimojiStatusManager2;
        mimojiStatusManager22.setMimojiRecordState(mimojiStatusManager22.getMimojiRecordStateFromGif());
    }

    public void checkIsNeedChangBg() {
        BackgroundInfo backgroundInfo;
        int i;
        if (!this.mMimojiStatusManager2.IsLoading()) {
            MimojiBgInfo currentMimojiBgInfo = this.mMimojiStatusManager2.getCurrentMimojiBgInfo();
            if (currentMimojiBgInfo != null && !this.mMimojiStatusManager2.isInMimojiCreate() && CameraSettings.isFrontCamera() && !CameraSettings.isGifOn()) {
                MimojiBgInfo mimojiBgInfo = this.mCurrentTempMimojiBgInfo;
                if (mimojiBgInfo == null || !mimojiBgInfo.getBackgroundInfo().getName().equals(currentMimojiBgInfo.getBackgroundInfo().getName())) {
                    this.mCurrentTempMimojiBgInfo = currentMimojiBgInfo.clone();
                    this.mAvatar.setRenderScene(false, 1.0f);
                }
                if (this.mCurrentTempMimojiBgInfo.getIsNeedRefresh()) {
                    this.mRecordModule.setBackgroundToSquare(false);
                    int i2 = this.uiStyle;
                    if (i2 != 0) {
                        if (i2 != 1) {
                            backgroundInfo = this.mCurrentTempMimojiBgInfo.getBackgroundInfo();
                            i = 3;
                        } else {
                            backgroundInfo = this.mCurrentTempMimojiBgInfo.getBackgroundInfo();
                            i = 2;
                        }
                        backgroundInfo.setResolutionMode(i);
                    } else {
                        this.mCurrentTempMimojiBgInfo.getBackgroundInfo().setResolutionMode(1);
                    }
                    try {
                        Bitmap decodeStream = BitmapFactory.decodeStream(new FileInputStream(this.mCurrentTempMimojiBgInfo.getBackgroundInfo().getBackGroundPath(this.mCurrentTempMimojiBgInfo.nextFrame())));
                        this.mRecordModule.setBackground(decodeStream, this.mCurrentTempMimojiBgInfo.getBackgroundInfo());
                        decodeStream.recycle();
                    } catch (FileNotFoundException e) {
                        this.mCurrentTempMimojiBgInfo = null;
                        StringBuilder sb = new StringBuilder();
                        sb.append("checkIsNeedChangBg  : ");
                        sb.append(e.getMessage());
                        Log.e(TAG, sb.toString());
                    }
                }
            } else if (this.mCurrentTempMimojiBgInfo != null) {
                this.mCurrentTempMimojiBgInfo = null;
                this.mRecordModule.setBackground(null, null);
                this.mAvatar.setRenderScene(true, 1.0f);
            }
        }
    }

    public boolean deleteMimojiCache(int i) {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("mimoji boolean deleteMimojiCache[type] : ");
        sb.append(i);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.d(str2, sb2);
        if (i == 0) {
            FileUtils.deleteFile(MimojiHelper2.VIDEO_CACHE_DIR);
            FileUtils.deleteFile(MimojiHelper2.GIF_CACHE_DIR);
            str = MimojiHelper2.EMOTICON_CACHE_DIR;
        } else if (i == 1) {
            str = MimojiHelper2.VIDEO_CACHE_DIR;
        } else if (i != 2) {
            if (i == 3) {
                try {
                    str = MimojiHelper2.GIF_CACHE_DIR;
                } catch (Exception e) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("mimoji void deleteMimojiCache[] ");
                    sb3.append(e.getMessage());
                    Log.e(str2, sb3.toString());
                    return false;
                }
            }
            return true;
        } else {
            str = MimojiHelper2.EMOTICON_CACHE_DIR;
        }
        FileUtils.deleteFile(str);
        return true;
    }

    public Bitmap getGifBitmap() {
        Bitmap bitmap = this.mGifBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        return this.mGifBitmap;
    }

    public boolean getIsNoFaceResult() {
        return this.mIsNoFaceResult < 10;
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return CameraCapabilities.SESSION_OPERATION_MODE_MIMOJI;
    }

    public String getTimeValue() {
        return Util.millisecondToTimeString(Util.clamp(this.mTotalRecordingTime, 1000, 15000), false, true);
    }

    public String getVideoCache() {
        return CameraSettings.isGifOn() ? MimojiHelper2.GIF_NORMAL_CACHE_FILE : MimojiHelper2.VIDEO_NORMAL_CACHE_FILE;
    }

    public void initAvatarEngine(int i, int i2, int i3, int i4, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("initAvatarEngine with parameters : displayOrientation = ");
        sb.append(i);
        sb.append(", width = ");
        sb.append(i3);
        sb.append(", height = ");
        sb.append(i4);
        sb.append(", isFrontCamera = ");
        sb.append(z);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        this.mDisplayOrientation = i;
        this.mPreviewWidth = i3;
        this.mPreviewHeight = i4;
        this.mIsFrontCamera = z;
        this.mOrientation = i2;
        this.mIsNoFaceResult = -1;
        this.mNeedThumbnail = false;
        this.mNeedCapture = false;
        setIsAvatarInited(false);
        this.uiStyle = DataRepository.dataItemRunning().getUiStyle();
        int hashCode = hashCode();
        initMimojiDataResource();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("avatar start init | ");
        sb3.append(hashCode);
        Log.d(str, sb3.toString());
        this.mAvatar = AvatarEngineManager2.getInstance().queryAvatar();
        this.mAvatar.setRenderScene(true, 1.0f);
        this.mIsGifOn = CameraSettings.isGifOn();
        Rect previewRect = Util.getPreviewRect(this.mContext);
        int i5 = previewRect.bottom - previewRect.top;
        int i6 = previewRect.right;
        if (this.mIsGifOn) {
            this.mDrawSize = new Size(i6, i6);
        } else {
            this.mDrawSize = new Size(i6, i5);
        }
        RecordModule recordModule = this.mRecordModule;
        if (recordModule == null) {
            this.mRecordModule = new RecordModule(this.mContext, this.mCaptureCallback);
            this.mRecordModule.init(i, this.mDrawSize.getWidth(), this.mDrawSize.getHeight(), this.mAvatar, z);
            this.mRecordModule.setExtraSceneTemplatePath(AvatarEngineManager2.TEMPLATE_PATH_HUMAN, this.mSceneTriggerCallback);
        } else {
            recordModule.setmImageOrientation(i);
            this.mRecordModule.setMirror(z);
            this.mRecordModule.setPreviewSize(this.mDrawSize.getWidth(), this.mDrawSize.getHeight());
        }
        this.mRecordModule.setBackgroundToSquare(this.mIsGifOn);
        releaseRender();
        boolean z2 = !isNeedShowAvatar() && this.mLastNeedBeauty;
        updateBeauty(z2);
        this.mIsStopRender = false;
        showLoadProgress(true);
        this.mLoadHandler.post(new C0273O00000oO(this));
    }

    public void initMimojiResource() {
        String str = TAG;
        String O0ooOOo = C0122O00000o.instance().O0ooOOo();
        if (this.mMimojiStatusManager2.IsLoading()) {
            showLoadProgress(true);
            return;
        }
        FileUtils.delDir(GIF_SUBTITLE_DIR);
        FileUtils.copyFileIfNeed(this.mContext, GIF_SUBTITLE_DIR, GIF_SUBTITLE_ASSETS_PATH, GIF_SUBTITLE_CACHE_FILE);
        long currentTimeMillis = System.currentTimeMillis();
        try {
            this.mMimojiStatusManager2.setIsLoading(true);
            this.mMimojiStatusManager2.setCurrentMimojiInfo(null);
            FileUtils.delDir(MimojiHelper2.MODEL_PATH);
            if (!UpgradeManager.mIsNeedUpgrade(UpgradeManager.currentVersionCode(this.mContext)) && !O0ooOOo.equals(CameraSettings.getMimojiModleVersion())) {
                FileUtils.delDir(MimojiHelper2.CUSTOM_DIR);
            }
            FileUtils.makeNoMediaDir(MimojiHelper2.MODEL_PATH);
            FileUtils.makeNoMediaDir(MimojiHelper2.CUSTOM_DIR);
            Util.verifyFileZip(this.mContext, "vendor/camera/mimoji/model2.zip", MimojiHelper2.MIMOJI_DIR, 32768);
            FileUtils.delDir(MimojiHelper2.DATA_DIR);
            FileUtils.makeNoMediaDir(MimojiHelper2.DATA_DIR);
            Util.verifyFileZip(this.mContext, "vendor/camera/mimoji/data.zip", MimojiHelper2.MIMOJI_DIR, 32768);
            StringBuilder sb = new StringBuilder();
            sb.append("init model spend time = ");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            Log.d(str, sb.toString());
            this.mMimojiStatusManager2.setIsLoading(false);
            CameraSettings.setMimojiModleVersion(O0ooOOo);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mAvatarTemplatePath = ");
            sb2.append(this.mAvatarTemplatePath);
            Log.i(str, sb2.toString());
            reloadConfig();
            onMimojiInitFinish();
        } catch (Exception e) {
            Log.e(str, "verify asset model zip failed...", (Throwable) e);
            this.mMimojiStatusManager2.setIsLoading(false);
            CameraSettings.setMimojiModleVersion(null);
            FileUtils.delDir(MimojiHelper2.MIMOJI_DIR);
        }
    }

    public boolean isAvatarInited() {
        return this.mIsAvatarInited;
    }

    public boolean isNeedShowAvatar() {
        MimojiInfo2 currentMimojiInfo = this.mMimojiStatusManager2.getCurrentMimojiInfo();
        return currentMimojiInfo != null && this.mAvatar != null && !TextUtils.isEmpty(currentMimojiInfo.mConfigPath) && !currentMimojiInfo.mConfigPath.equals("add_state") && !currentMimojiInfo.mConfigPath.equals("close_state") && !this.mMimojiStatusManager2.isInMimojiCreate();
    }

    public boolean isOnCreateMimoji() {
        return this.mMimojiStatusManager2.isInMimojiCreate();
    }

    public boolean isProcessorReady() {
        return this.mRecordModule != null && !this.mIsStopRender;
    }

    public boolean isRecordStopping() {
        return this.mIsRecordStopping;
    }

    public boolean isRecording() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recording = ");
        sb.append(this.mIsRecording);
        Log.d(TAG, sb.toString());
        return this.mIsRecording;
    }

    public void onCaptureImage() {
        if (this.mRecordModule != null) {
            ActivityBase activityBase = this.mActivityBase;
            if (activityBase != null) {
                this.mNeedCapture = true;
                CameraStatUtils.trackMimoji2CaptureOrRecord(getMimojiPara(), CameraSettings.getFlashMode(activityBase.getCurrentModuleIndex()), true, this.mIsFrontCamera, false);
                CameraStatUtils.trackMimojiSavePara(Mimoji.MIMOJI_CLICK_EDIT_SAVE, getExtraMimojiPara());
            }
        }
    }

    public boolean onCreateCapture() {
        Log.d(TAG, "onCreateCapture");
        if (this.mFaceDectectResult == 0 && this.mIsFaceDetectSuccess) {
            ActivityBase activityBase = this.mActivityBase;
            if (activityBase != null) {
                Module currentModule = activityBase.getCurrentModule();
                if (currentModule instanceof MimojiModule) {
                    MimojiModule mimojiModule = (MimojiModule) currentModule;
                    CameraSettings.setFaceBeautyLevel(0);
                    mimojiModule.updatePreferenceInWorkThread(13);
                }
                ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).showOrHideMimojiProgress(true);
                this.mIsShutterButtonClick = true;
                animateCapture();
                return true;
            }
        }
        return false;
    }

    public void onDeviceRotationChange(int i) {
        this.mDeviceRotation = i;
        updateVideoOrientation(i);
        MimojiEditor2 mimojiEditor2 = this.mMimojiEditor2;
        if (mimojiEditor2 != null) {
            mimojiEditor2.onDeviceRotationChange(i);
        }
    }

    public void onDrawFrame(Rect rect, int i, int i2, boolean z) {
        Rect rect2 = rect;
        if (this.mRecordModule != null && rect2 != null && !this.mIsStopRender) {
            int windowHeight = Display.getWindowHeight();
            int i3 = rect2.bottom;
            int i4 = windowHeight - i3;
            int i5 = i3 - rect2.top;
            int i6 = rect2.right;
            if (z) {
                GLES20.glViewport(0, 0, i, i2);
            } else {
                GLES20.glViewport(0, i4, i6, i5);
                this.mRecordModule.setDrawScope(0, i4, i6, i5);
                if (this.mNeedCapture) {
                    Log.d(TAG, "onCapture start");
                    this.mRecordModule.capture();
                    ActivityBase activityBase = this.mActivityBase;
                    if (activityBase != null) {
                        ((MimojiModule) activityBase.getCurrentModule()).setCameraStatePublic(3);
                    }
                    this.mNeedCapture = false;
                }
            }
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            RecordModule recordModule = this.mRecordModule;
            boolean z2 = this.mIsFrontCamera;
            int i7 = this.mDeviceRotation;
            int[] iArr = this.mTextureId;
            boolean z3 = isNeedShowAvatar() && !getIsNoFaceResult();
            recordModule.startRender(90, z2, i7, 0, false, iArr, null, z3);
            if (this.mNeedThumbnail) {
                this.mNeedThumbnail = false;
                ByteBuffer allocate = ByteBuffer.allocate(i6 * i5 * 4);
                GLES20.glReadPixels(0, i4, i6, i5, 6408, 5121, allocate);
                this.mLoadHandler.post(new O0000O0o(this, allocate, i6, i5));
            }
        }
    }

    public void onMimojiChangeBg(MimojiBgInfo mimojiBgInfo) {
        if (mimojiBgInfo == null || mimojiBgInfo.getBackgroundInfo() == null) {
            this.mMimojiStatusManager2.setCurrentMimojiBgInfo(null);
        } else {
            this.mMimojiStatusManager2.setCurrentMimojiBgInfo(mimojiBgInfo);
        }
        CameraStatUtils.trackMimoji2Click(null, Mimoji.MIMOJI_CHANGE_BG);
    }

    public void onMimojiChangeTimbre(MimojiTimbreInfo mimojiTimbreInfo, int i) {
        if (i == 0 || mimojiTimbreInfo == null || mimojiTimbreInfo.getTimbreId() <= 0) {
            this.mMimojiStatusManager2.setCurrentMimojiTimbreInfo(null);
        } else {
            this.mMimojiStatusManager2.setCurrentMimojiTimbreInfo(mimojiTimbreInfo);
        }
        int descId = mimojiTimbreInfo == null ? R.string.timbre_normal : mimojiTimbreInfo.getDescId();
        if (this.mMimojiStatusManager2.isInMimojiPreview()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null) {
                topAlert.alertTopHint(0, descId, 1000);
            }
        } else {
            MimojiFullScreenProtocol mimojiFullScreenProtocol = (MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249);
            if (mimojiFullScreenProtocol != null) {
                mimojiFullScreenProtocol.alertTop(0, descId, 1000);
            }
            MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
            if (mimojiVideoEditor != null) {
                mimojiVideoEditor.changeTimbre();
            }
        }
        CameraStatUtils.trackMimoji2Click(null, Mimoji.MIMOJI_CHANGE_TIMBRE);
        StringBuilder sb = new StringBuilder();
        sb.append("mimoji void onMimojiChangeTimbre[mimojiTimbreInfo]");
        sb.append(mimojiTimbreInfo);
        Log.d(TAG, sb.toString());
    }

    public void onMimojiCreate() {
        Log.d(TAG, "start create mimoji");
        this.mMimojiStatusManager2.setMode(4);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertAiDetectTipHint(8, R.string.mimoji_create_tips, -1);
        }
        this.mMainProtocol.mimojiStart();
        ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).prepareCreateMimoji();
    }

    public void onMimojiDeleted() {
        this.mMimojiStatusManager2.setCurrentMimojiInfo(null);
        this.mMimojiStatusManager2.setCurrentMimojiBgInfo(null);
        this.mRecordModule.resetExtraScene();
    }

    public void onMimojiInitFinish() {
        Log.d(TAG, "onMimojiInitFinish");
        setIsAvatarInited(true);
        showLoadProgress(false);
    }

    public void onMimojiSelect(MimojiInfo2 mimojiInfo2, boolean z) {
        Log.d(TAG, "mimoji void onMimojiSelect[mimojiInfo2]");
        this.mUiHandler.post(new O0000Oo(this));
        if (mimojiInfo2 == null || TextUtils.isEmpty(mimojiInfo2.mConfigPath) || mimojiInfo2.mConfigPath.equals("add_state") || mimojiInfo2.mConfigPath.equals("close_state") || this.mAvatar == null) {
            this.mMimojiStatusManager2.setCurrentMimojiInfo(null);
            MimojiBottomList mimojiBottomList = (MimojiBottomList) ModeCoordinatorImpl.getInstance().getAttachProtocol(248);
            if (mimojiBottomList != null && !z) {
                mimojiBottomList.refreshMimojiList();
            }
            return;
        }
        if (this.mMimojiStatusManager2.getCurrentMimojiInfo() == null) {
            this.mIsNoFaceResult = 2;
        }
        this.mMimojiStatusManager2.setCurrentMimojiInfo(mimojiInfo2);
        String str = mimojiInfo2.mAvatarTemplatePath;
        String str2 = mimojiInfo2.mConfigPath;
        StringBuilder sb = new StringBuilder();
        sb.append("change mimoji with path = ");
        sb.append(str);
        sb.append(", and config = ");
        sb.append(str2);
        Log.d(TAG, sb.toString());
        synchronized (this.mAvatarLock) {
            this.mRecordModule.setExtraSceneTemplatePath(AvatarEngineManager2.TEMPLATE_PATH_HUMAN, this.mSceneTriggerCallback);
            this.mRecordModule.resetExtraScene();
            boolean equals = this.mAvatarTemplatePath.equals(str);
            this.mAvatarTemplatePath = str;
            this.mAvatarConfigPath = str2 == null ? "" : str2;
            if (str2.isEmpty() || AvatarEngineManager2.isPreCartoonModel(str2)) {
                if (!equals) {
                    this.mAvatar.setTemplatePath(str);
                }
                if (mimojiInfo2.getAvatarConfigInfoArrayList() == null) {
                    mimojiInfo2.setAvatarConfigInfoArrayList(this.mAvatar.getConfig(12, 1));
                }
                CameraStatUtils.trackMimoji2Click(Mimoji.CARTOON, null);
                if (mimojiInfo2.getFrame() >= 0 && mimojiInfo2.getDefaultFrame() > 0) {
                    this.mAvatar.setConfig((ASAvatarConfigInfo) mimojiInfo2.getAvatarConfigInfoArrayList().get(mimojiInfo2.getFrame()));
                }
            } else {
                if (!equals) {
                    this.mRecordModule.changeHumanTemplate(str, str2);
                } else {
                    this.mAvatar.loadConfig(str2);
                }
                CameraStatUtils.trackMimoji2Click(Mimoji.HUMAN, null);
            }
            this.mRecordModule.updateAvatarConfigInfo(this.mAvatar);
            boolean z2 = false;
            this.mAvatar.setRenderScene(this.mCurrentTempMimojiBgInfo == null, 1.0f);
            if (!isNeedShowAvatar() && this.mLastNeedBeauty) {
                z2 = true;
            }
            updateBeauty(z2);
        }
    }

    public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
        boolean startProcess;
        boolean z = false;
        if (this.mRecordModule == null) {
            Log.d(TAG, "MimojiAvatarEngine2Impl onPreviewFrame mRecordModule null");
            this.mLastInitResult = false;
            this.mCameraView.requestRender();
            return true;
        }
        ASVLOFFSCREEN buildNV21SingleBuffer = AsvloffscreenUtil.buildNV21SingleBuffer(image);
        if (this.mIsShutterButtonClick) {
            this.mIsShutterButtonClick = false;
            setIsAvatarInited(false);
            Bitmap rotateBitmap = BitmapUtils2.rotateBitmap(BitmapUtils2.rawByteArray2RGBABitmap(buildNV21SingleBuffer.getYData(), image.getWidth(), image.getHeight(), image.getPlanes()[0].getRowStride()), this.mIsFrontCamera ? -90 : 90);
            int width = rotateBitmap.getWidth();
            int height = rotateBitmap.getHeight();
            ByteBuffer order = ByteBuffer.allocate(rotateBitmap.getRowBytes() * rotateBitmap.getHeight()).order(ByteOrder.nativeOrder());
            rotateBitmap.copyPixelsToBuffer(order);
            createAvatar(order.array(), width, height);
        }
        this.mMimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
        if (!this.mMimojiStatusManager2.isInMimojiEdit()) {
            if (this.mMimojiEditor2 != null && !this.mMimojiStatusManager2.isInMimojiEmoticon()) {
                this.mMimojiEditor2.requestRender(true);
                this.mMimojiEditor2.resetClickEnable(false);
            }
            synchronized (this.mAvatarLock) {
                checkIsNeedChangBg();
                startProcess = this.mRecordModule != null ? this.mRecordModule.startProcess(buildNV21SingleBuffer, MimojiHelper2.getOutlineOrientation(this.mOrientation, this.mDeviceRotation, this.mIsFrontCamera), isNeedShowAvatar()) : true;
            }
            if (changeIsNoFaceResult(startProcess) && !this.mMimojiStatusManager2.isInMimojiCreate()) {
                this.mUiHandler.post(new C0274O00000oo(this));
            }
            boolean isNoFaceResult = getIsNoFaceResult();
            if (this.mLastNeedBeauty != isNoFaceResult) {
                this.mLastNeedBeauty = isNoFaceResult;
                if (!isNeedShowAvatar() && isNoFaceResult) {
                    z = true;
                }
                updateBeauty(z);
            }
            this.mCameraView.requestRender();
        } else if (!isAvatarInited() || this.mAvatar == null) {
            MimojiEditor2 mimojiEditor2 = this.mMimojiEditor2;
            if (mimojiEditor2 != null) {
                mimojiEditor2.resetClickEnable(false);
                this.mMimojiEditor2.requestRender(true);
            }
            Log.d(TAG, "MimojiAvatarEngine2Impl onPreviewFrame need init, waiting......");
            return true;
        } else if (this.mMimojiEditor2 != null && isAvatarInited()) {
            ASAvatarProcessInfo aSAvatarProcessInfo = new ASAvatarProcessInfo();
            synchronized (this.mAvatarLock) {
                if (this.mAvatar != null) {
                    this.mAvatar.avatarProcessWithInfoEx(buildNV21SingleBuffer, 90, this.mIsFrontCamera, this.mOrientation, aSAvatarProcessInfo, true);
                }
            }
            this.mMimojiEditor2.requestRender(false);
            this.mMimojiEditor2.resetClickEnable(true);
            if (!this.mLastInitResult) {
                this.mMimojiEditor2.resetConfig();
            }
        }
        if (this.mMimojiStatusManager2.isInMimojiCreate() && isAvatarInited()) {
            synchronized (this.mAvatarLock) {
                this.mFaceDectectResult = this.mAvatar.outlineProcessEx(buildNV21SingleBuffer, MimojiHelper2.getOutlineOrientation(this.mOrientation, this.mDeviceRotation, this.mIsFrontCamera));
            }
            MainContentProtocol mainContentProtocol = this.mMainProtocol;
            if (mainContentProtocol != null) {
                mainContentProtocol.mimojiFaceDetect(this.mFaceDectectResult);
            }
        }
        this.mLastInitResult = true;
        return true;
    }

    public void onRecordStart() {
        Log.d(TAG, "start record...");
        if (!this.mIsRecording) {
            ActivityBase activityBase = this.mActivityBase;
            if (activityBase != null) {
                BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) ModeCoordinatorImpl.getInstance().getAttachProtocol(933);
                CameraStatUtils.trackMimoji2CaptureOrRecord(getMimojiPara(), CameraSettings.getFlashMode(activityBase.getCurrentModuleIndex()), false, this.mIsFrontCamera, bluetoothHeadset != null ? bluetoothHeadset.isBluetoothScoOn() : false);
                CameraStatUtils.trackMimojiSavePara(Mimoji.MIMOJI_CLICK_EDIT_SAVE, getExtraMimojiPara());
                this.mIsRecording = true;
                this.mNeedThumbnail = true;
                this.mCameraView.queueEvent(new O0000OOo(this));
            }
        }
    }

    public void onRecordStop(int i) {
        Log.d(TAG, "stop record...");
        this.mIsRecordStopping = true;
        this.mStopRecordType = i;
        if (i != 0) {
            this.mGetThumCountDownLatch = new CountDownLatch(1);
        }
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        new Thread(new C0275O0000OoO(this)).start();
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(246, this);
    }

    public void release() {
        Log.d(TAG, "avatar release");
        this.mMimojiStatusManager2.setCurrentMimojiBgInfo(null);
        this.mIsRecordStopping = false;
        CountDownLatch countDownLatch = this.mGetThumCountDownLatch;
        if (countDownLatch != null) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.mDrawSize = null;
        if (this.mMimojiStatusManager2.isInPreviewSurface()) {
            this.mCameraView.queueEvent(new O0000o(this));
        } else {
            destoryAll();
        }
    }

    public void releaseRender() {
        int hashCode = hashCode();
        this.mIsStopRender = true;
        if (this.mRecordModule != null) {
            Log.d(TAG, "mimoji void reloadConfig[]　extrascene init");
            this.mRecordModule.resetExtraScene();
        }
        if (this.mMimojiStatusManager2.isInPreviewSurface()) {
            this.mCameraView.queueEvent(new O0000Oo0(this, hashCode));
            return;
        }
        MimojiEditor2 mimojiEditor2 = this.mMimojiEditor2;
        if (mimojiEditor2 != null) {
            mimojiEditor2.releaseRender();
        }
    }

    public void reloadConfig() {
        String str = TAG;
        Log.d(str, "MimojiAvatarEngine2Impl reloadConfig");
        if (this.mRecordModule != null) {
            int mode = this.mMimojiStatusManager2.getMode();
            this.mAvatar = AvatarEngineManager2.getInstance().queryAvatar();
            if (mode == 2 || mode == 0) {
                this.mMimojiStatusManager2.setMode(2);
                MimojiInfo2 currentMimojiInfo = this.mMimojiStatusManager2.getCurrentMimojiInfo();
                ArrayList backgroundBmpInfo = this.mRecordModule.getBackgroundBmpInfo(AvatarEngineManager2.TEMPLATE_PATH_BG);
                if (backgroundBmpInfo != null) {
                    AvatarEngineManager2.getInstance().setBackgroundInfos(backgroundBmpInfo);
                }
                if (this.mRecordModule != null) {
                    Log.d(str, "mimoji void reloadConfig[]　extrascene init");
                    this.mRecordModule.resetExtraScene();
                }
                if (isNeedShowAvatar()) {
                    if (!this.mAvatarTemplatePath.equals(currentMimojiInfo.mAvatarTemplatePath)) {
                        this.mAvatar.setTemplatePath(currentMimojiInfo.mAvatarTemplatePath);
                        this.mAvatarTemplatePath = currentMimojiInfo.mAvatarTemplatePath;
                        this.mAvatarConfigPath = "";
                    }
                    String str2 = currentMimojiInfo.mConfigPath;
                    if (!AvatarEngineManager2.isPreCartoonModel(str2)) {
                        if (!this.mAvatarConfigPath.equals(str2)) {
                            this.mAvatar.loadConfig(str2);
                            this.mAvatarConfigPath = str2;
                        }
                    } else if (currentMimojiInfo.getFrame() > 0 && currentMimojiInfo.getAvatarConfigInfoArrayList() != null) {
                        this.mAvatar.setConfig((ASAvatarConfigInfo) currentMimojiInfo.getAvatarConfigInfoArrayList().get(currentMimojiInfo.getFrame()));
                    }
                    this.mRecordModule.updateAvatarConfigInfo(this.mAvatar);
                }
            } else if (mode == 6) {
                String str3 = AvatarEngineManager2.TEMPLATE_PATH_HUMAN;
                this.mAvatarTemplatePath = str3;
                this.mAvatar.setTemplatePath(str3);
                this.mMimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
                MimojiEditor2 mimojiEditor2 = this.mMimojiEditor2;
                if (mimojiEditor2 != null) {
                    mimojiEditor2.resetClickEnable(false);
                    this.mMimojiEditor2.resetConfig();
                } else {
                    Log.e(str, "MimojiAvatarEngine2Impl reloadConfig: error mimojiEditor is null");
                }
            }
        }
    }

    public void setDetectSuccess(boolean z) {
        this.mIsFaceDetectSuccess = z;
    }

    public void setDisableSingleTapUp(boolean z) {
        ActivityBase activityBase = this.mActivityBase;
        if (activityBase != null) {
            ((MimojiModule) activityBase.getCurrentModule()).setDisableSingleTapUp(z);
        }
    }

    public void setIsAvatarInited(boolean z) {
        this.mIsAvatarInited = z;
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(246, this);
        release();
    }

    /* access modifiers changed from: protected */
    /* renamed from: updateRecordingTime */
    public void O00oO0O0() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            this.mTotalRecordingTime = 0;
            countDownTimer.cancel();
        }
        this.mMaxVideoDurationInMs = CameraSettings.isGifOn() ? 5000 : 15000;
        AnonymousClass2 r1 = new CountDownTimer(450 + ((long) this.mMaxVideoDurationInMs), 1000) {
            public void onFinish() {
                if (MimojiAvatarEngine2Impl.this.mActivityBase != null || MimojiAvatarEngine2Impl.this.mStopRecordType == 0) {
                    ((MimojiModule) MimojiAvatarEngine2Impl.this.mActivityBase.getCurrentModule()).stopVideoRecording(0);
                }
            }

            public void onTick(long j) {
                String millisecondToTimeString = Util.millisecondToTimeString((950 + j) - 450, false);
                MimojiAvatarEngine2Impl mimojiAvatarEngine2Impl = MimojiAvatarEngine2Impl.this;
                mimojiAvatarEngine2Impl.mTotalRecordingTime = (((long) mimojiAvatarEngine2Impl.mMaxVideoDurationInMs) - j) + 450;
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
