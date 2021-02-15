package com.android.camera.module.impl.component;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.location.Location;
import android.media.Image;
import android.opengl.GLES20;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Size;
import android.widget.Toast;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.FileCompat;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import com.android.camera.fragment.mimoji.AvatarEngineManager;
import com.android.camera.fragment.mimoji.BitmapUtils;
import com.android.camera.fragment.mimoji.MimojiHelper;
import com.android.camera.fragment.mimoji.MimojiInfo;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.LiveModule;
import com.android.camera.module.Module;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.MimojiAlert;
import com.android.camera.protocol.ModeProtocol.MimojiAvatarEngine;
import com.android.camera.protocol.ModeProtocol.MimojiEditor;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import com.android.camera.storage.Storage;
import com.android.camera.ui.V6CameraGLSurfaceView;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.PreviewCallback;
import com.android.camera2.CameraCapabilities;
import com.arcsoft.avatar.AvatarConfig.ASAvatarConfigValue;
import com.arcsoft.avatar.AvatarConfig.ASAvatarProcessInfo;
import com.arcsoft.avatar.AvatarConfig.ASAvatarProfileResult;
import com.arcsoft.avatar.AvatarEngine;
import com.arcsoft.avatar.RecordModule;
import com.arcsoft.avatar.RecordModule.MediaResultCallback;
import com.arcsoft.avatar.recoder.RecordingListener;
import com.arcsoft.avatar.util.ASVLOFFSCREEN;
import com.arcsoft.avatar.util.AsvloffscreenUtil;
import com.arcsoft.avatar.util.LOG;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.ParallelTaskDataParameter.Builder;
import com.xiaomi.camera.core.PictureInfo;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MimojiAvatarEngineImpl implements MimojiAvatarEngine, ExternalFrameProcessor, PreviewCallback {
    private static final int FLAG_HAS_FACE = 5;
    private static final int FLAG_INIT_FACE = 0;
    private static final int HANDLER_RECORDING_CURRENT_FILE_SIZE = 3;
    private static final int HANDLER_RECORDING_CURRENT_TIME = 1;
    private static final int HANDLER_RECORDING_MAX_DURATION_REACHED = 2;
    private static final int HANDLER_RECORDING_MAX_FILE_SIZE_REACHED = 4;
    private static final int HANDLER_RESOURCE_ERROR_BROKEN = 0;
    private static final long START_OFFSET_MS = 450;
    /* access modifiers changed from: private */
    public static final String TAG = "MimojiAvatarEngineImpl";
    /* access modifiers changed from: private */
    public ActivityBase mActivityBase;
    /* access modifiers changed from: private */
    public AvatarEngine mAvatar;
    /* access modifiers changed from: private */
    public final Object mAvatarLock = new Object();
    /* access modifiers changed from: private */
    public String mAvatarTemplatePath = "";
    private V6CameraGLSurfaceView mCameraView;
    /* access modifiers changed from: private */
    public MediaResultCallback mCaptureCallback = new MediaResultCallback() {
        public void onCaptureResult(final ByteBuffer byteBuffer) {
            Log.d(MimojiAvatarEngineImpl.TAG, "onCapture Result");
            MimojiAvatarEngineImpl.this.mLoadHandler.post(new Runnable() {
                public void run() {
                    MimojiAvatarEngineImpl.this.CaptureCallback(byteBuffer);
                }
            });
            ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
            if (actionProcessing != null) {
                actionProcessing.announceForAccessibility(R.string.accessibility_camera_shutter_finish);
            }
        }

        public void onVideoResult(boolean z) {
            Log.d(MimojiAvatarEngineImpl.TAG, "stop video record callback");
            MimojiAvatarEngineImpl.this.mIsRecording = false;
            MimojiAvatarEngineImpl.this.mIsRecordStopping = false;
            if (MimojiAvatarEngineImpl.this.mActivityBase != null) {
                MimojiAvatarEngineImpl.this.mActivityBase.getImageSaver().addVideo(MimojiAvatarEngineImpl.this.mSaveVideoPath, MimojiAvatarEngineImpl.this.mContentValues, true);
            }
            if (MimojiAvatarEngineImpl.this.mVideoFileStream != null) {
                try {
                    MimojiAvatarEngineImpl.this.mVideoFileStream.close();
                } catch (IOException e) {
                    Log.e(MimojiAvatarEngineImpl.TAG, "fail to close file stream", (Throwable) e);
                }
                MimojiAvatarEngineImpl.this.mVideoFileStream = null;
            }
            MimojiAvatarEngineImpl.this.mVideoFileDescriptor = null;
            if (MimojiAvatarEngineImpl.this.mGetThumCountDownLatch != null) {
                MimojiAvatarEngineImpl.this.mGetThumCountDownLatch.countDown();
            }
        }
    };
    /* access modifiers changed from: private */
    public ContentValues mContentValues;
    /* access modifiers changed from: private */
    public Context mContext;
    private CountDownTimer mCountDownTimer;
    /* access modifiers changed from: private */
    public int mCurrentScreenOrientation = 0;
    private int mDeviceRotation = 90;
    private int mDisplayOrientation;
    /* access modifiers changed from: private */
    public Size mDrawSize;
    private int mFaceDectectResult = 1;
    /* access modifiers changed from: private */
    public CountDownLatch mGetThumCountDownLatch;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i != 0 && i != 1 && i != 2) {
            }
        }
    };
    /* access modifiers changed from: private */
    public volatile boolean mIsAvatarInited;
    private boolean mIsFaceDetectSuccess = false;
    private boolean mIsFrontCamera;
    private boolean mIsNeedShowTip = false;
    private int mIsNoFaceResult = -1;
    /* access modifiers changed from: private */
    public boolean mIsRecordStopping = false;
    /* access modifiers changed from: private */
    public volatile boolean mIsRecording;
    private boolean mIsShutterButtonClick = false;
    /* access modifiers changed from: private */
    public boolean mIsStopRender = true;
    private boolean mLastNeedBeauty = false;
    /* access modifiers changed from: private */
    public Handler mLoadHandler;
    private Handler mLoadResourceHandler;
    private HandlerThread mLoadResourceThread = new HandlerThread("LoadResource");
    private HandlerThread mLoadThread = new HandlerThread("LoadConfig");
    private MainContentProtocol mMainProtocol;
    private int mMaxVideoDurationInMs = 15000;
    private MimojiEditor mMimojiEditor;
    /* access modifiers changed from: private */
    public MimojiStatusManager mMimojiStatusManager;
    private boolean mNeedCapture = false;
    private int mOrientation;
    /* access modifiers changed from: private */
    public int mPreviewHeight;
    /* access modifiers changed from: private */
    public int mPreviewWidth;
    /* access modifiers changed from: private */
    public volatile RecordModule mRecordModule;
    /* access modifiers changed from: private */
    public RecordingListener mRecordingListener = new RecordingListener() {
        public void onRecordingListener(int i, Object obj) {
            int i2;
            Message obtainMessage = MimojiAvatarEngineImpl.this.mHandler.obtainMessage();
            switch (i) {
                case 257:
                    obtainMessage.arg1 = (int) ((((Long) obj).longValue() / 1000) / 1000);
                    i2 = 2;
                    break;
                case 258:
                    long longValue = (((Long) obj).longValue() / 1000) / 1000;
                    obtainMessage.arg1 = (int) longValue;
                    String access$100 = MimojiAvatarEngineImpl.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onRecordingListener_time = ");
                    sb.append(longValue);
                    Log.d(access$100, sb.toString());
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
    /* access modifiers changed from: private */
    public String mSaveVideoPath;
    private int[] mTextureId = new int[1];
    /* access modifiers changed from: private */
    public Handler mUiHandler;
    /* access modifiers changed from: private */
    public FileDescriptor mVideoFileDescriptor;
    /* access modifiers changed from: private */
    public FileOutputStream mVideoFileStream;

    private MimojiAvatarEngineImpl(ActivityBase activityBase) {
        this.mActivityBase = activityBase;
        this.mCameraView = activityBase.getGLView();
        this.mContext = activityBase.getCameraAppImpl().getApplicationContext();
        this.mMainProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        this.mMimojiEditor = (MimojiEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(224);
        this.mLoadResourceThread.start();
        this.mLoadResourceHandler = new Handler(this.mLoadResourceThread.getLooper());
        this.mLoadThread.start();
        this.mLoadHandler = new Handler(this.mLoadThread.getLooper());
        this.mUiHandler = new Handler(activityBase.getMainLooper());
        this.mMimojiStatusManager = DataRepository.dataItemLive().getMimojiStatusManager();
        setIsAvatarInited(false);
        Log.w(TAG, "MimojiAvatarEngineImpl:  constructor");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0035, code lost:
        if (r3 != 270) goto L_0x0038;
     */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ef  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00fa  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0101  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void CaptureCallback(ByteBuffer byteBuffer) {
        int i;
        int i2;
        ActivityBase activityBase = this.mActivityBase;
        if (activityBase != null) {
            Bitmap createBitmap = Bitmap.createBitmap(this.mDrawSize.getWidth(), this.mDrawSize.getHeight(), Config.ARGB_8888);
            createBitmap.copyPixelsFromBuffer(byteBuffer);
            Matrix matrix = new Matrix();
            boolean z = this.mIsFrontCamera;
            if (z) {
                if (z) {
                    int i3 = this.mDeviceRotation;
                    if (i3 != 90) {
                    }
                }
                if (this.mDeviceRotation % 180 == 0) {
                    matrix.postScale(-1.0f, 1.0f);
                }
                Bitmap createBitmap2 = Bitmap.createBitmap(createBitmap, 0, 0, this.mDrawSize.getWidth(), this.mDrawSize.getHeight(), matrix, false);
                int i4 = 0;
                byte[] bitmapData = Util.getBitmapData(createBitmap2, CameraSettings.getEncodingQuality(false).toInteger(false));
                if (this.mIsFrontCamera) {
                    int i5 = this.mDeviceRotation;
                    if (i5 % 180 == 0) {
                        i = (i5 + 180) % m.cQ;
                        Thumbnail createThumbnail = Thumbnail.createThumbnail(null, this.mIsFrontCamera ? createBitmap : createBitmap2, i, this.mIsFrontCamera);
                        createThumbnail.startWaitingForUri();
                        activityBase.getThumbnailUpdater().setThumbnail(createThumbnail, true, true);
                        LiveModule liveModule = (LiveModule) activityBase.getCurrentModule();
                        ParallelTaskData parallelTaskData = new ParallelTaskData(liveModule != null ? liveModule.getActualCameraId() : 0, System.currentTimeMillis(), -4, null);
                        parallelTaskData.fillJpegData(bitmapData, 0);
                        int jpegRotation = (Util.getJpegRotation(this.mIsFrontCamera ? 1 : 0, this.mDeviceRotation) + 270) % m.cQ;
                        Size size = this.mDrawSize;
                        Builder builder = new Builder(size, size, size, 256);
                        Location currentLocation = LocationManager.instance().getCurrentLocation();
                        Builder filterId = builder.setHasDualWaterMark(CameraSettings.isDualCameraWaterMarkOpen()).setJpegRotation(jpegRotation).setJpegQuality(CameraSettings.getEncodingQuality(false).toInteger(false)).setFilterId(FilterInfo.FILTER_ID_NONE);
                        i2 = this.mOrientation;
                        if (-1 != i2) {
                            i4 = i2;
                        }
                        parallelTaskData.fillParameter(filterId.setOrientation(i4).setTimeWaterMarkString(CameraSettings.isTimeWaterMarkOpen() ? Util.getTimeWatermark(this.mActivityBase) : null).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setPictureInfo(getPictureInfo()).setLocation(currentLocation).build());
                        activityBase.getImageSaver().onParallelProcessFinish(parallelTaskData, null, null);
                        createBitmap.recycle();
                        createBitmap2.recycle();
                        ((LiveModule) activityBase.getCurrentModule()).onMimojiCaptureCallback();
                    }
                }
                i = this.mDeviceRotation;
                Thumbnail createThumbnail2 = Thumbnail.createThumbnail(null, this.mIsFrontCamera ? createBitmap : createBitmap2, i, this.mIsFrontCamera);
                createThumbnail2.startWaitingForUri();
                activityBase.getThumbnailUpdater().setThumbnail(createThumbnail2, true, true);
                LiveModule liveModule2 = (LiveModule) activityBase.getCurrentModule();
                ParallelTaskData parallelTaskData2 = new ParallelTaskData(liveModule2 != null ? liveModule2.getActualCameraId() : 0, System.currentTimeMillis(), -4, null);
                parallelTaskData2.fillJpegData(bitmapData, 0);
                int jpegRotation2 = (Util.getJpegRotation(this.mIsFrontCamera ? 1 : 0, this.mDeviceRotation) + 270) % m.cQ;
                Size size2 = this.mDrawSize;
                Builder builder2 = new Builder(size2, size2, size2, 256);
                Location currentLocation2 = LocationManager.instance().getCurrentLocation();
                Builder filterId2 = builder2.setHasDualWaterMark(CameraSettings.isDualCameraWaterMarkOpen()).setJpegRotation(jpegRotation2).setJpegQuality(CameraSettings.getEncodingQuality(false).toInteger(false)).setFilterId(FilterInfo.FILTER_ID_NONE);
                i2 = this.mOrientation;
                if (-1 != i2) {
                }
                parallelTaskData2.fillParameter(filterId2.setOrientation(i4).setTimeWaterMarkString(CameraSettings.isTimeWaterMarkOpen() ? Util.getTimeWatermark(this.mActivityBase) : null).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setPictureInfo(getPictureInfo()).setLocation(currentLocation2).build());
                activityBase.getImageSaver().onParallelProcessFinish(parallelTaskData2, null, null);
                createBitmap.recycle();
                createBitmap2.recycle();
                ((LiveModule) activityBase.getCurrentModule()).onMimojiCaptureCallback();
            }
            matrix.postScale(1.0f, -1.0f);
            Bitmap createBitmap22 = Bitmap.createBitmap(createBitmap, 0, 0, this.mDrawSize.getWidth(), this.mDrawSize.getHeight(), matrix, false);
            int i42 = 0;
            byte[] bitmapData2 = Util.getBitmapData(createBitmap22, CameraSettings.getEncodingQuality(false).toInteger(false));
            if (this.mIsFrontCamera) {
            }
            i = this.mDeviceRotation;
            Thumbnail createThumbnail22 = Thumbnail.createThumbnail(null, this.mIsFrontCamera ? createBitmap : createBitmap22, i, this.mIsFrontCamera);
            createThumbnail22.startWaitingForUri();
            activityBase.getThumbnailUpdater().setThumbnail(createThumbnail22, true, true);
            LiveModule liveModule22 = (LiveModule) activityBase.getCurrentModule();
            ParallelTaskData parallelTaskData22 = new ParallelTaskData(liveModule22 != null ? liveModule22.getActualCameraId() : 0, System.currentTimeMillis(), -4, null);
            parallelTaskData22.fillJpegData(bitmapData2, 0);
            int jpegRotation22 = (Util.getJpegRotation(this.mIsFrontCamera ? 1 : 0, this.mDeviceRotation) + 270) % m.cQ;
            Size size22 = this.mDrawSize;
            Builder builder22 = new Builder(size22, size22, size22, 256);
            Location currentLocation22 = LocationManager.instance().getCurrentLocation();
            Builder filterId22 = builder22.setHasDualWaterMark(CameraSettings.isDualCameraWaterMarkOpen()).setJpegRotation(jpegRotation22).setJpegQuality(CameraSettings.getEncodingQuality(false).toInteger(false)).setFilterId(FilterInfo.FILTER_ID_NONE);
            i2 = this.mOrientation;
            if (-1 != i2) {
            }
            parallelTaskData22.fillParameter(filterId22.setOrientation(i42).setTimeWaterMarkString(CameraSettings.isTimeWaterMarkOpen() ? Util.getTimeWatermark(this.mActivityBase) : null).setDeviceWatermarkParam(getDeviceWaterMarkParam()).setPictureInfo(getPictureInfo()).setLocation(currentLocation22).build());
            activityBase.getImageSaver().onParallelProcessFinish(parallelTaskData22, null, null);
            createBitmap.recycle();
            createBitmap22.recycle();
            ((LiveModule) activityBase.getCurrentModule()).onMimojiCaptureCallback();
        }
    }

    static /* synthetic */ void O0000oOO(int i) {
    }

    private void animateCapture() {
        if (CameraSettings.isCameraSoundOpen()) {
            this.mActivityBase.playCameraSound(0);
        }
    }

    public static MimojiAvatarEngineImpl create(ActivityBase activityBase) {
        return new MimojiAvatarEngineImpl(activityBase);
    }

    private void createAvatar(byte[] bArr, int i, int i2) {
        int avatarProfile;
        Context context;
        int i3;
        String str = this.mAvatarTemplatePath;
        String str2 = AvatarEngineManager.PersonTemplatePath;
        if (str != str2) {
            this.mAvatarTemplatePath = str2;
            this.mAvatar.setTemplatePath(str2);
        }
        ASAvatarProfileResult aSAvatarProfileResult = new ASAvatarProfileResult();
        synchronized (this.mAvatarLock) {
            avatarProfile = this.mAvatar.avatarProfile(AvatarEngineManager.PersonTemplatePath, i, i2, i * 4, bArr, 0, false, aSAvatarProfileResult, null, C0406O000O0oO.INSTANCE);
        }
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("avatarProfile res: ");
        sb.append(avatarProfile);
        sb.append(", status:");
        sb.append(aSAvatarProfileResult.status);
        sb.append(", gender: ");
        sb.append(aSAvatarProfileResult.gender);
        LOG.d(str3, sb.toString());
        int i4 = aSAvatarProfileResult.status;
        if (i4 == 254 || i4 == 246) {
            String str4 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("result = ");
            sb2.append(avatarProfile);
            Log.d(str4, sb2.toString());
            this.mUiHandler.post(new O000OO0o(this));
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
            activityBase.runOnUiThread(new O000OO00(this));
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

    private Map getMimojiPara() {
        String str;
        Map hashMap = new HashMap();
        boolean isNeedShowAvatar = isNeedShowAvatar();
        String str2 = Mimoji.MIMOJI_CATEGORY;
        if (isNeedShowAvatar) {
            ASAvatarConfigValue aSAvatarConfigValue = new ASAvatarConfigValue();
            this.mAvatar.getConfigValue(aSAvatarConfigValue);
            hashMap = AvatarEngineManager.getMimojiConfigValue(aSAvatarConfigValue);
            str = this.mAvatarTemplatePath.equals(AvatarEngineManager.PersonTemplatePath) ? "custom" : this.mAvatarTemplatePath.equals(AvatarEngineManager.PigTemplatePath) ? "pig" : this.mAvatarTemplatePath.equals(AvatarEngineManager.BearTemplatePath) ? "bear" : this.mAvatarTemplatePath.equals(AvatarEngineManager.RoyanTemplatePath) ? "royan" : this.mAvatarTemplatePath.equals(AvatarEngineManager.RabbitTemplatePath) ? "rabbit" : "";
        } else {
            str = "null";
        }
        hashMap.put(str2, str);
        return hashMap;
    }

    private PictureInfo getPictureInfo() {
        PictureInfo opMode = new PictureInfo().setFrontMirror(isFrontMirror()).setSensorType(true).setBokehFrontCamera(false).setHdrType("off").setOpMode(getOperatingMode());
        opMode.end();
        return opMode;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0083 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initMimojiResource() {
        final String O0ooOOo = C0122O00000o.instance().O0ooOOo();
        if (this.mMimojiStatusManager.IsInMimojiPreview() && !this.mMimojiStatusManager.IsLoading() && !O0ooOOo.equals(CameraSettings.getMimojiModleVersion())) {
            this.mIsNeedShowTip = true;
        }
        if (!O0ooOOo.equals(CameraSettings.getMimojiModleVersion()) || ((CameraAppImpl) this.mActivityBase.getApplication()).isMimojiNeedUpdate()) {
            Log.w(TAG, "MimojiAvatarEngineImpl: initMimojiResource unzip...");
            if (!O0ooOOo.equals(CameraSettings.getMimojiModleVersion())) {
                if (FileUtils.hasDir(MimojiHelper.MIMOJI_DIR)) {
                    FileUtils.delDir(MimojiHelper.MIMOJI_DIR);
                }
                DataRepository.dataItemLive().getMimojiStatusManager().setIsLoading(true);
            }
            if (!FileUtils.hasDir(MimojiHelper.MIMOJI_DIR)) {
                FileUtils.delDir(MimojiHelper.MIMOJI_DIR);
            }
            Util.verifyAssetZip(this.mContext, "data.zip", MimojiHelper.MIMOJI_DIR, 32768);
            try {
                Util.verifyFileZip(this.mContext, "vendor/camera/mimoji/data.zip", MimojiHelper.MIMOJI_DIR, 32768);
            } catch (Exception e) {
                Log.e(TAG, "verify asset data zip failed...", (Throwable) e);
            }
            this.mLoadResourceHandler.post(new Runnable() {
                /* JADX WARNING: Failed to process nested try/catch */
                /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0017 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void run() {
                    long currentTimeMillis = System.currentTimeMillis();
                    Util.verifyAssetZip(MimojiAvatarEngineImpl.this.mContext, "model.zip", MimojiHelper.MIMOJI_DIR, 32768);
                    try {
                        Util.verifyFileZip(MimojiAvatarEngineImpl.this.mContext, "vendor/camera/mimoji/model.zip", MimojiHelper.MIMOJI_DIR, 32768);
                    } catch (Exception e) {
                        Log.e(MimojiAvatarEngineImpl.TAG, "verify asset model zip failed...", (Throwable) e);
                    }
                    String access$100 = MimojiAvatarEngineImpl.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("init model spend time = ");
                    sb.append(System.currentTimeMillis() - currentTimeMillis);
                    Log.d(access$100, sb.toString());
                    DataRepository.dataItemLive().getMimojiStatusManager().setIsLoading(false);
                    CameraSettings.setMimojiModleVersion(O0ooOOo);
                    String access$1002 = MimojiAvatarEngineImpl.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("mAvatarTemplatePath = ");
                    sb2.append(MimojiAvatarEngineImpl.this.mAvatarTemplatePath);
                    Log.i(access$1002, sb2.toString());
                    MimojiAvatarEngineImpl.this.mUiHandler.post(new Runnable() {
                        public void run() {
                            MimojiAlert mimojiAlert = (MimojiAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(226);
                            if (mimojiAlert != null) {
                                mimojiAlert.firstProgressShow(false);
                            } else {
                                Log.i(MimojiAvatarEngineImpl.TAG, "mimojiAlert finish == null");
                            }
                        }
                    });
                }
            });
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
        this.mMimojiEditor = (MimojiEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(224);
        this.mMainProtocol.mimojiEnd();
        this.mMimojiStatusManager.setMode(MimojiStatusManager.MIMOJI_EDIT_MID);
        MimojiEditor mimojiEditor = this.mMimojiEditor;
        if (mimojiEditor != null) {
            mimojiEditor.startMimojiEdit(false, 105);
        }
        ActivityBase activityBase = this.mActivityBase;
        if (activityBase != null) {
            ((LiveModule) activityBase.getCurrentModule()).onMimojiCreateCompleted(true);
        }
        CameraStatUtils.trackMimojiClick(Mimoji.MIMOJI_CLICK_CREATE_CAPTURE, BaseEvent.CREATE);
    }

    private void quit() {
        this.mLoadThread.quitSafely();
        this.mLoadResourceThread.quitSafely();
    }

    /* access modifiers changed from: private */
    public void reloadConfig() {
        Log.e(TAG, "MimojiAvatarEngineImpl reloadConfig");
        int mode = this.mMimojiStatusManager.getMode();
        if (mode == MimojiStatusManager.MIMOJI_PREVIEW || mode == MimojiStatusManager.MIMOJI_NONE) {
            this.mMimojiStatusManager.setMode(MimojiStatusManager.MIMOJI_PREVIEW);
            MimojiInfo mimojiInfo = this.mMimojiStatusManager.getmCurrentMimojiInfo();
            if (isNeedShowAvatar()) {
                if (!this.mAvatarTemplatePath.equals(mimojiInfo.mAvatarTemplatePath)) {
                    this.mAvatar.setTemplatePath(mimojiInfo.mAvatarTemplatePath);
                    this.mAvatarTemplatePath = mimojiInfo.mAvatarTemplatePath;
                }
                String str = mimojiInfo.mConfigPath;
                if (!AvatarEngineManager.isPrefabModel(str)) {
                    this.mAvatar.loadConfig(str);
                }
            }
        } else if (mode == MimojiStatusManager.MIMOJI_EDIT_MID || mode == MimojiStatusManager.MIMOJI_EIDT) {
            String str2 = AvatarEngineManager.PersonTemplatePath;
            this.mAvatarTemplatePath = str2;
            this.mAvatar.setTemplatePath(str2);
            this.mAvatar.loadConfig(AvatarEngineManager.TempEditConfigPath);
            this.mMimojiEditor = (MimojiEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(224);
            MimojiEditor mimojiEditor = this.mMimojiEditor;
            if (mimojiEditor != null) {
                mimojiEditor.resetClickEnable(false);
                this.mMimojiEditor.resetConfig();
                return;
            }
            Log.e(TAG, "MimojiAvatarEngineImpl reloadConfig: error mimojiEditor is null");
        }
    }

    private void updateBeauty() {
        ActivityBase activityBase = this.mActivityBase;
        if (activityBase != null) {
            BaseModule baseModule = (BaseModule) activityBase.getCurrentModule();
            if (baseModule instanceof LiveModule) {
                ComponentRunningShine componentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
                if (componentRunningShine.supportBeautyLevel()) {
                    CameraSettings.setFaceBeautyLevel(3);
                } else if (componentRunningShine.supportSmoothLevel()) {
                    CameraSettings.setFaceBeautySmoothLevel(40);
                }
                baseModule.updatePreferenceInWorkThread(13);
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

    public /* synthetic */ void O00ooOo() {
        setDisableSingleTapUp(true);
        onProfileFinish();
    }

    public /* synthetic */ void O00ooOoO() {
        RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
        if (recordState != null) {
            recordState.onPostSavingFinish();
        }
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertMimojiFaceDetect(false, -1);
        }
        this.mMainProtocol.mimojiEnd();
        ((LiveModule) this.mActivityBase.getCurrentModule()).onMimojiCreateCompleted(false);
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).showOrHideMimojiProgress(false);
        backToPreview(false, true);
    }

    public /* synthetic */ void O00ooOoo() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            boolean z = getIsNoFaceResult() && isNeedShowAvatar();
            topAlert.alertMimojiFaceDetect(z, R.string.mimoji_check_no_face);
        }
    }

    public /* synthetic */ void O00ooo00() {
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            topAlert.alertAiDetectTipHint(0, R.string.mimoji_tips, 3000);
            this.mIsNeedShowTip = false;
        }
    }

    public void backToPreview(boolean z, boolean z2) {
        if ((this.mMimojiStatusManager.IsInMimojiEdit() || this.mMimojiStatusManager.IsInMimojiEditMid() || this.mMimojiStatusManager.IsInMimojiCreate()) && !z && this.mMimojiStatusManager.getmCurrentMimojiInfo() != null) {
            MimojiInfo mimojiInfo = this.mMimojiStatusManager.getmCurrentMimojiInfo();
            if (mimojiInfo != null) {
                this.mAvatarTemplatePath = mimojiInfo.mAvatarTemplatePath;
                if (AvatarEngineManager.isPrefabModel(mimojiInfo.mConfigPath)) {
                    this.mAvatar.setTemplatePath(mimojiInfo.mAvatarTemplatePath);
                } else {
                    this.mAvatar.loadConfig(mimojiInfo.mConfigPath);
                }
            }
        }
        this.mMimojiStatusManager.setMode(MimojiStatusManager.MIMOJI_PREVIEW);
        this.mIsStopRender = false;
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        bottomPopupTips.reInitTipImage();
        if (z2) {
            bottomPopupTips.hideCenterTipImage();
            if (!DataRepository.dataItemLive().getMimojiStatusManager().getMimojiPannelState()) {
                bottomPopupTips.showOrHideMimojiPanel();
            }
        }
        topAlert.alertAiDetectTipHint(8, R.string.mimoji_check_normal, -1);
        topAlert.alertMimojiFaceDetect(false, -1);
        topAlert.enableMenuItem(true, 197, 193);
        AvatarEngine avatarEngine = this.mAvatar;
        if (avatarEngine != null) {
            avatarEngine.setRenderScene(true, 1.0f);
        }
        setDisableSingleTapUp(false);
    }

    public boolean changeIsNoFaceResult(boolean z) {
        if (!z) {
            int i = this.mIsNoFaceResult;
            if (i > 5) {
                return true;
            }
            this.mIsNoFaceResult = i + 1;
        } else if (this.mIsNoFaceResult != 0) {
            this.mIsNoFaceResult = 0;
            return true;
        }
        return false;
    }

    public boolean getIsNoFaceResult() {
        return this.mIsNoFaceResult <= 5;
    }

    /* access modifiers changed from: protected */
    public int getOperatingMode() {
        return CameraCapabilities.SESSION_OPERATION_MODE_MIMOJI;
    }

    public void initAvatarEngine(int i, int i2, int i3, int i4, boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initAvatarEngine with parameters : displayOrientation = ");
        sb.append(i);
        sb.append(", width = ");
        sb.append(i3);
        sb.append(", height = ");
        sb.append(i4);
        sb.append(", isFrontCamera = ");
        sb.append(z);
        Log.d(str, sb.toString());
        this.mDisplayOrientation = i;
        this.mPreviewWidth = i3;
        this.mPreviewHeight = i4;
        this.mIsFrontCamera = z;
        this.mOrientation = i2;
        initMimojiResource();
        final int hashCode = hashCode();
        Handler handler = this.mLoadHandler;
        final int i5 = i;
        final int i6 = i3;
        final int i7 = i4;
        final boolean z2 = z;
        AnonymousClass1 r2 = new Runnable() {
            public void run() {
                synchronized (MimojiAvatarEngineImpl.this.mAvatarLock) {
                    String access$100 = MimojiAvatarEngineImpl.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("avatar start init | ");
                    sb.append(hashCode);
                    Log.d(access$100, sb.toString());
                    MimojiAvatarEngineImpl.this.mAvatar = AvatarEngineManager.getInstance().queryAvatar();
                    if (MimojiAvatarEngineImpl.this.mRecordModule == null) {
                        MimojiAvatarEngineImpl.this.mRecordModule = new RecordModule(MimojiAvatarEngineImpl.this.mContext, MimojiAvatarEngineImpl.this.mCaptureCallback);
                        MimojiAvatarEngineImpl.this.mRecordModule.init(i5, i6, i7, MimojiAvatarEngineImpl.this.mAvatar, z2);
                    } else {
                        MimojiAvatarEngineImpl.this.mRecordModule.setmImageOrientation(i5);
                        MimojiAvatarEngineImpl.this.mRecordModule.setMirror(z2);
                        MimojiAvatarEngineImpl.this.mRecordModule.setPreviewSize(i6, i7);
                    }
                    Rect previewRect = Util.getPreviewRect(MimojiAvatarEngineImpl.this.mContext);
                    MimojiAvatarEngineImpl.this.mRecordModule.setDrawScope(0, Display.getWindowHeight() - previewRect.bottom, previewRect.right, previewRect.bottom - previewRect.top);
                    MimojiAvatarEngineImpl.this.mDrawSize = new Size(previewRect.right, previewRect.bottom - previewRect.top);
                    MimojiAvatarEngineImpl.this.mIsStopRender = false;
                    if (!MimojiAvatarEngineImpl.this.mIsAvatarInited) {
                        Log.d(MimojiAvatarEngineImpl.TAG, "avatar need really init");
                        MimojiAvatarEngineImpl.this.mAvatar.init(AvatarEngineManager.TRACK_DATA, AvatarEngineManager.FACE_MODEL);
                        MimojiAvatarEngineImpl.this.mAvatar.setRenderScene(true, 1.0f);
                        MimojiAvatarEngineImpl.this.mAvatar.createOutlineEngine(AvatarEngineManager.TRACK_DATA);
                        MimojiAvatarEngineImpl.this.reloadConfig();
                    }
                    MimojiAvatarEngineImpl.this.onMimojiInitFinish();
                }
            }
        };
        handler.post(r2);
    }

    public boolean isNeedShowAvatar() {
        MimojiInfo mimojiInfo = this.mMimojiStatusManager.getmCurrentMimojiInfo();
        return mimojiInfo != null && this.mAvatar != null && !TextUtils.isEmpty(mimojiInfo.mConfigPath) && !mimojiInfo.mConfigPath.equals("add_state") && !mimojiInfo.mConfigPath.equals("close_state") && !this.mMimojiStatusManager.IsInMimojiCreate();
    }

    public boolean isOnCreateMimoji() {
        return this.mMimojiStatusManager.IsInMimojiCreate();
    }

    public boolean isProcessorReady() {
        return this.mRecordModule != null;
    }

    public boolean isRecordStopping() {
        return this.mIsRecordStopping;
    }

    public boolean isRecording() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Recording = ");
        sb.append(this.mIsRecording);
        Log.d(str, sb.toString());
        return this.mIsRecording;
    }

    public void onCaptureImage() {
        if (this.mRecordModule != null) {
            ActivityBase activityBase = this.mActivityBase;
            if (activityBase != null && ((Camera) activityBase).isCurrentModuleAlive()) {
                this.mNeedCapture = true;
                CameraStatUtils.trackMimojiCaptureOrRecord(getMimojiPara(), CameraSettings.getFlashMode(this.mActivityBase.getCurrentModuleIndex()), true, this.mIsFrontCamera);
            }
        }
    }

    public boolean onCreateCapture() {
        Log.d(TAG, "onCreateCapture");
        if (this.mFaceDectectResult != 0 || !this.mIsFaceDetectSuccess || this.mActivityBase == null) {
            return false;
        }
        releaseRender();
        Module currentModule = this.mActivityBase.getCurrentModule();
        if (currentModule instanceof LiveModule) {
            LiveModule liveModule = (LiveModule) currentModule;
            CameraSettings.setFaceBeautyLevel(0);
            liveModule.updatePreferenceInWorkThread(13);
        }
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).showOrHideMimojiProgress(true);
        this.mIsShutterButtonClick = true;
        animateCapture();
        return true;
    }

    public void onDeviceRotationChange(int i) {
        this.mDeviceRotation = i;
        updateVideoOrientation(i);
        MimojiEditor mimojiEditor = this.mMimojiEditor;
        if (mimojiEditor != null) {
            mimojiEditor.onDeviceRotationChange(i);
        }
    }

    public void onDrawFrame(Rect rect, int i, int i2, boolean z) {
        if (this.mRecordModule != null && rect != null && !this.mIsStopRender) {
            boolean z2 = false;
            if (z) {
                GLES20.glViewport(0, 0, i, i2);
            } else {
                int windowHeight = Display.getWindowHeight();
                int i3 = rect.bottom;
                GLES20.glViewport(0, windowHeight - i3, rect.right, i3 - rect.top);
                if (this.mNeedCapture) {
                    Log.d(TAG, "onCapture start");
                    this.mRecordModule.capture();
                    ActivityBase activityBase = this.mActivityBase;
                    if (activityBase != null) {
                        ((LiveModule) activityBase.getCurrentModule()).setCameraStatePublic(3);
                    }
                    this.mNeedCapture = false;
                }
            }
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            RecordModule recordModule = this.mRecordModule;
            boolean z3 = this.mIsFrontCamera;
            int i4 = this.mDeviceRotation;
            int[] iArr = this.mTextureId;
            if (isNeedShowAvatar() && !getIsNoFaceResult()) {
                z2 = true;
            }
            recordModule.startRender(90, z3, i4, 0, false, iArr, null, z2);
        }
    }

    public void onMimojiCreate() {
        Log.d(TAG, "start create mimoji");
        this.mMimojiStatusManager.setMode(MimojiStatusManager.MIMOJI_CREATE);
        this.mMainProtocol.mimojiStart();
        ((RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212)).prepareCreateMimoji();
    }

    public void onMimojiDeleted() {
        this.mMimojiStatusManager.setmCurrentMimojiInfo(null);
    }

    public void onMimojiInitFinish() {
        Log.d(TAG, "onMimojiInitFinish");
        this.mCameraView.requestRender();
        setIsAvatarInited(true);
    }

    public void onMimojiSelect(final MimojiInfo mimojiInfo) {
        this.mLoadHandler.post(new Runnable() {
            public void run() {
                if (mimojiInfo == null || MimojiAvatarEngineImpl.this.mAvatar == null) {
                    MimojiAvatarEngineImpl.this.mMimojiStatusManager.setmCurrentMimojiInfo(null);
                    return;
                }
                MimojiAvatarEngineImpl.this.mMimojiStatusManager.setmCurrentMimojiInfo(mimojiInfo);
                MimojiInfo mimojiInfo = mimojiInfo;
                String str = mimojiInfo.mAvatarTemplatePath;
                String str2 = mimojiInfo.mConfigPath;
                String access$100 = MimojiAvatarEngineImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("change mimoji with path = ");
                sb.append(str);
                sb.append(", and config = ");
                sb.append(str2);
                Log.d(access$100, sb.toString());
                synchronized (MimojiAvatarEngineImpl.this.mAvatarLock) {
                    boolean equals = MimojiAvatarEngineImpl.this.mAvatarTemplatePath.equals(str);
                    MimojiAvatarEngineImpl.this.mAvatarTemplatePath = str;
                    if (str2.isEmpty() || AvatarEngineManager.isPrefabModel(str2)) {
                        if (!equals) {
                            MimojiAvatarEngineImpl.this.mAvatar.setTemplatePath(str);
                        }
                    } else if (!equals) {
                        MimojiAvatarEngineImpl.this.mRecordModule.changeHumanTemplate(str, str2);
                    } else {
                        MimojiAvatarEngineImpl.this.mAvatar.loadConfig(str2);
                    }
                }
            }
        });
    }

    public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
        boolean startProcess;
        String str;
        String str2;
        if (this.mRecordModule == null) {
            str = TAG;
            str2 = "MimojiAvatarEngineImpl onPreviewFrame mRecordModule null";
        } else {
            this.mMimojiEditor = (MimojiEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(224);
            if ((this.mMimojiStatusManager.IsInMimojiEditMid() || this.mMimojiStatusManager.IsInMimojiEdit()) && (!this.mIsAvatarInited || this.mAvatar == null)) {
                MimojiEditor mimojiEditor = this.mMimojiEditor;
                if (mimojiEditor != null) {
                    mimojiEditor.resetClickEnable(false);
                    this.mMimojiEditor.requestRender(true);
                }
                str = TAG;
                str2 = "MimojiAvatarEngineImpl onPreviewFrame need init, waiting......";
            } else {
                ASVLOFFSCREEN buildNV21SingleBuffer = AsvloffscreenUtil.buildNV21SingleBuffer(image);
                if (this.mIsShutterButtonClick) {
                    this.mIsShutterButtonClick = false;
                    setIsAvatarInited(false);
                    Bitmap rotateBitmap = BitmapUtils.rotateBitmap(BitmapUtils.rawByteArray2RGBABitmap(buildNV21SingleBuffer.getYData(), image.getWidth(), image.getHeight(), image.getPlanes()[0].getRowStride()), this.mIsFrontCamera ? -90 : 90);
                    int width = rotateBitmap.getWidth();
                    int height = rotateBitmap.getHeight();
                    ByteBuffer order = ByteBuffer.allocate(rotateBitmap.getRowBytes() * rotateBitmap.getHeight()).order(ByteOrder.nativeOrder());
                    rotateBitmap.copyPixelsToBuffer(order);
                    createAvatar(order.array(), width, height);
                }
                if (!this.mMimojiStatusManager.IsInMimojiEditMid() && !this.mMimojiStatusManager.IsInMimojiEdit()) {
                    MimojiEditor mimojiEditor2 = this.mMimojiEditor;
                    if (mimojiEditor2 != null) {
                        mimojiEditor2.requestRender(true);
                        this.mMimojiEditor.resetClickEnable(false);
                    }
                    synchronized (this.mAvatarLock) {
                        startProcess = this.mRecordModule.startProcess(buildNV21SingleBuffer, MimojiHelper.getOutlineOrientation(this.mOrientation, this.mDeviceRotation, this.mIsFrontCamera), isNeedShowAvatar());
                    }
                    if (changeIsNoFaceResult(startProcess) && !this.mMimojiStatusManager.IsInMimojiCreate()) {
                        this.mUiHandler.post(new C0407O000O0oo(this));
                    }
                    if (this.mIsNeedShowTip) {
                        this.mUiHandler.post(new O000OO(this));
                    }
                    if (this.mLastNeedBeauty != startProcess) {
                        this.mLastNeedBeauty = startProcess;
                        updateBeauty();
                    }
                    this.mCameraView.requestRender();
                } else if (this.mMimojiEditor != null) {
                    ASAvatarProcessInfo aSAvatarProcessInfo = new ASAvatarProcessInfo();
                    synchronized (this.mAvatarLock) {
                        this.mAvatar.avatarProcessWithInfoEx(buildNV21SingleBuffer, 90, this.mIsFrontCamera, this.mOrientation, aSAvatarProcessInfo, true);
                    }
                    this.mMimojiEditor.requestRender(false);
                    this.mMimojiEditor.resetClickEnable(true);
                }
                if (this.mMimojiStatusManager.IsInMimojiCreate()) {
                    synchronized (this.mAvatarLock) {
                        this.mFaceDectectResult = this.mAvatar.outlineProcessEx(buildNV21SingleBuffer, MimojiHelper.getOutlineOrientation(this.mOrientation, this.mDeviceRotation, this.mIsFrontCamera));
                    }
                    MainContentProtocol mainContentProtocol = this.mMainProtocol;
                    if (mainContentProtocol != null) {
                        mainContentProtocol.mimojiFaceDetect(this.mFaceDectectResult);
                    }
                }
                return true;
            }
        }
        Log.d(str, str2);
        return true;
    }

    public void onRecordStart(ContentValues contentValues) {
        FileDescriptor fd;
        Log.d(TAG, "start record...");
        if (!this.mIsRecording) {
            ActivityBase activityBase = this.mActivityBase;
            if (activityBase != null) {
                CameraStatUtils.trackMimojiCaptureOrRecord(getMimojiPara(), CameraSettings.getFlashMode(activityBase.getCurrentModuleIndex()), false, this.mIsFrontCamera);
                this.mIsRecording = true;
                this.mIsRecordStopping = false;
                this.mContentValues = contentValues;
                this.mSaveVideoPath = contentValues.getAsString("_data");
                try {
                    if (Storage.isUseDocumentMode()) {
                        fd = FileCompat.getParcelFileDescriptor(this.mSaveVideoPath, true).getFileDescriptor();
                    } else {
                        this.mVideoFileStream = new FileOutputStream(this.mSaveVideoPath);
                        fd = this.mVideoFileStream.getFD();
                    }
                    this.mVideoFileDescriptor = fd;
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                this.mCameraView.queueEvent(new Runnable() {
                    public void run() {
                        if (MimojiAvatarEngineImpl.this.mRecordModule != null) {
                            MimojiAvatarEngineImpl.this.mRecordModule.startRecording(MimojiAvatarEngineImpl.this.mVideoFileDescriptor, MimojiAvatarEngineImpl.this.mRecordingListener, MimojiAvatarEngineImpl.this.mCurrentScreenOrientation, MimojiAvatarEngineImpl.this.mPreviewWidth, MimojiAvatarEngineImpl.this.mPreviewHeight, 10000000, CameraSettings.getVideoEncoder() == 5 ? "video/hevc" : "video/avc");
                        }
                    }
                });
                updateRecordingTime();
            }
        }
    }

    public void onRecordStop(boolean z) {
        Log.d(TAG, "stop record...");
        this.mIsRecordStopping = true;
        if (z) {
            this.mGetThumCountDownLatch = new CountDownLatch(1);
        }
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.mCameraView.queueEvent(new Runnable() {
            public void run() {
                if (MimojiAvatarEngineImpl.this.mRecordModule != null) {
                    new Thread(new Runnable() {
                        public void run() {
                            MimojiAvatarEngineImpl.this.mRecordModule.stopRecording();
                        }
                    }).start();
                }
            }
        });
    }

    public void onResume() {
        Log.d(TAG, BaseEvent.RESET);
        if (this.mRecordModule != null) {
            this.mRecordModule.reset();
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(217, this);
    }

    public void release() {
        Log.d(TAG, "avatar release");
        CountDownLatch countDownLatch = this.mGetThumCountDownLatch;
        if (countDownLatch != null) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        final int hashCode = hashCode();
        this.mLoadHandler.post(new Runnable() {
            public void run() {
                synchronized (MimojiAvatarEngineImpl.this.mAvatarLock) {
                    if (MimojiAvatarEngineImpl.this.mAvatar != null) {
                        String access$100 = MimojiAvatarEngineImpl.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("avatar destroy | ");
                        sb.append(hashCode);
                        Log.d(access$100, sb.toString());
                        MimojiAvatarEngineImpl.this.mAvatar.saveConfig(AvatarEngineManager.TempEditConfigPath);
                        MimojiAvatarEngineImpl.this.mAvatar.destroyOutlineEngine();
                        MimojiAvatarEngineImpl.this.mAvatar.unInit();
                        if (MimojiAvatarEngineImpl.this.mRecordModule != null) {
                            MimojiAvatarEngineImpl.this.mRecordModule.unInit();
                        }
                        AvatarEngineManager.getInstance().releaseAvatar();
                    }
                }
            }
        });
        FileOutputStream fileOutputStream = this.mVideoFileStream;
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e2) {
                Log.e(TAG, "fail to close file stream", (Throwable) e2);
            }
            this.mVideoFileStream = null;
        }
        this.mIsStopRender = true;
        this.mVideoFileDescriptor = null;
        this.mActivityBase = null;
    }

    public void releaseRender() {
        final int hashCode = hashCode();
        if (this.mMimojiStatusManager.IsInPreviewSurface()) {
            this.mCameraView.queueEvent(new Runnable() {
                public void run() {
                    synchronized (MimojiAvatarEngineImpl.this.mAvatarLock) {
                        if (MimojiAvatarEngineImpl.this.mAvatar != null) {
                            String access$100 = MimojiAvatarEngineImpl.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("releaseRender | ");
                            sb.append(hashCode);
                            Log.d(access$100, sb.toString());
                            MimojiAvatarEngineImpl.this.mAvatar.releaseRender();
                        }
                    }
                }
            });
            return;
        }
        MimojiEditor mimojiEditor = this.mMimojiEditor;
        if (mimojiEditor != null) {
            mimojiEditor.releaseRender();
        }
    }

    public void setDetectSuccess(boolean z) {
        this.mIsFaceDetectSuccess = z;
    }

    public void setDisableSingleTapUp(boolean z) {
        ActivityBase activityBase = this.mActivityBase;
        if (activityBase != null) {
            ((LiveModule) activityBase.getCurrentModule()).setDisableSingleTapUp(z);
        }
    }

    public void setIsAvatarInited(boolean z) {
        this.mIsAvatarInited = z;
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(217, this);
        release();
        quit();
    }

    /* access modifiers changed from: protected */
    public void updateRecordingTime() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        AnonymousClass7 r1 = new CountDownTimer(450 + ((long) this.mMaxVideoDurationInMs), 1000) {
            public void onFinish() {
                if (MimojiAvatarEngineImpl.this.mActivityBase != null) {
                    ((LiveModule) MimojiAvatarEngineImpl.this.mActivityBase.getCurrentModule()).stopVideoRecording(false);
                }
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
