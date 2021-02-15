package com.android.camera.storage;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureResult;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSettings;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.EffectController.EffectRectAttribute;
import com.android.camera.effect.draw_mode.DrawJPEGAttribute;
import com.android.camera.effect.renders.DeviceWatermarkParam;
import com.android.camera.effect.renders.SnapshotEffectRender;
import com.android.camera.log.Log;
import com.android.camera.module.DebugInfoUtil;
import com.android.camera.module.ModuleManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.storage.HeifSaveRequest.SaveHeifCallback;
import com.android.camera.ui.ScreenHint;
import com.android.camera2.CaptureResultParser;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import com.android.camera2.vendortag.struct.HdrEvValue;
import com.android.camera2.vendortag.struct.MarshalQueryableSuperNightExif;
import com.android.camera2.vendortag.struct.MarshalQueryableSuperNightExif.SuperNightExif;
import com.android.gallery3d.exif.ExifHelper;
import com.android.gallery3d.exif.ExifInterface;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.ParallelCallback;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.ParallelTaskDataParameter;
import com.xiaomi.camera.core.PictureInfo;
import com.xiaomi.camera.liveshot.CircularMediaRecorder.VideoClipSavingCallback;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageSaver implements ParallelCallback, SaverCallback, VideoClipSavingCallback {
    private static final Executor CAMERA_SAVER_EXECUTOR;
    private static final int HOST_STATE_DESTROY = 2;
    private static final int HOST_STATE_PAUSE = 1;
    private static final int HOST_STATE_RESUME = 0;
    private static final Executor PREVIEW_SAVER_EXECUTOR;
    private static final int QUEUE_BUSY_SIZE = 40;
    private static final String TAG = "ImageSaver";
    private static final BlockingQueue mPreviewRequestQueue = new LinkedBlockingQueue(32);
    private static final BlockingQueue mSaveRequestQueue = new LinkedBlockingQueue(128);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable runnable) {
            StringBuilder sb = new StringBuilder();
            sb.append("camera-saver-");
            sb.append(this.mCount.getAndIncrement());
            Thread thread = new Thread(runnable, sb.toString());
            thread.setPriority(10);
            return thread;
        }
    };
    protected final Object mBitmapTextureLock = new Object();
    private Context mContext;
    private boolean mDropBitmapTexture;
    private SnapshotEffectRender mEffectProcessor;
    private final Object mEffectProcessorLock = new Object();
    private Handler mHandler;
    private Handler mHeifHandler;
    private HandlerThread mHeifHandlerThread;
    private int mHostState;
    private volatile boolean mIsBusy;
    private boolean mIsCaptureIntent;
    private Uri mLastImageUri;
    private final Queue mLiveShotPendingTaskQueue = new ConcurrentLinkedQueue();
    private MemoryManager mMemoryManager;
    private Thumbnail mPendingThumbnail;
    private AtomicInteger mSaveQueueSize;
    /* access modifiers changed from: private */
    public WeakReference mSaverCallback;
    private ParallelTaskData mStoredTaskData;
    private boolean mSupportSuperNightExif;
    private ThumbnailUpdater mUpdateThumbnail = new ThumbnailUpdater();
    private final Object mUpdateThumbnailLock = new Object();
    private AtomicBoolean mbModuleSwitch;

    public interface ImageSaverCallback {
        CameraScreenNail getCameraScreenNail();

        int getDisplayRotation();

        ScreenHint getScreenHint();

        com.android.camera.ThumbnailUpdater getThumbnailUpdater();

        boolean isActivityPaused();

        void onNewUriArrived(Uri uri, String str);
    }

    class ThumbnailUpdater implements Runnable {
        private boolean mNeedAnimation = true;

        public ThumbnailUpdater() {
        }

        public void run() {
            ImageSaverCallback imageSaverCallback = (ImageSaverCallback) ImageSaver.this.mSaverCallback.get();
            if (imageSaverCallback != null) {
                imageSaverCallback.getScreenHint().updateHint();
            }
            ImageSaver.this.updateThumbnail(this.mNeedAnimation);
        }

        public void setNeedAnimation(boolean z) {
            this.mNeedAnimation = z;
        }
    }

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 10, TimeUnit.SECONDS, mSaveRequestQueue, sThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        CAMERA_SAVER_EXECUTOR = threadPoolExecutor;
        ThreadPoolExecutor threadPoolExecutor2 = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, mPreviewRequestQueue, sThreadFactory);
        threadPoolExecutor2.allowCoreThreadTimeOut(true);
        PREVIEW_SAVER_EXECUTOR = threadPoolExecutor2;
    }

    public ImageSaver(ImageSaverCallback imageSaverCallback, Handler handler, boolean z) {
        this.mSaverCallback = new WeakReference(imageSaverCallback);
        this.mHandler = handler;
        this.mIsCaptureIntent = z;
        this.mMemoryManager = new MemoryManager();
        this.mMemoryManager.initMemory();
        this.mbModuleSwitch = new AtomicBoolean(false);
        this.mSaveQueueSize = new AtomicInteger(0);
        this.mContext = CameraAppImpl.getAndroidContext();
    }

    private void addSaveRequest(SaveRequest saveRequest) {
        addSaveRequest(saveRequest, false);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:15|16) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r2.mIsBusy = true;
        com.android.camera.log.Log.w(TAG, "stop snapshot due to thread pool is full");
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0032 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addSaveRequest(SaveRequest saveRequest, boolean z) {
        synchronized (this) {
            if (2 == this.mHostState) {
                Log.v(TAG, "addSaveRequest: host is being destroyed.");
            }
            if (isSaveQueueFull()) {
                this.mIsBusy = true;
            }
            this.mSaveQueueSize.incrementAndGet();
            addUsedMemory(saveRequest.getSize());
            saveRequest.setContextAndCallback(this.mContext, this);
            (z ? PREVIEW_SAVER_EXECUTOR : CAMERA_SAVER_EXECUTOR).execute(saveRequest);
        }
    }

    private void dealExif(ParallelTaskData parallelTaskData, CaptureResult captureResult) {
        String str;
        String str2;
        if (captureResult != null) {
            ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
            PictureInfo pictureInfo = dataParameter.getPictureInfo();
            StringBuilder sb = new StringBuilder(256);
            Boolean bool = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.IS_HDR_ENABLE);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" hdrEnable:");
            sb2.append(bool);
            sb.append(sb2.toString());
            String hdrEvValue = new HdrEvValue((byte[]) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.HDR_CHECKER_EV_VALUES)).toString();
            if (!TextUtils.isEmpty(hdrEvValue)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(" hdrEv:");
                sb3.append(hdrEvValue);
                sb.append(sb3.toString());
            }
            Boolean bool2 = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.IS_SR_ENABLE);
            StringBuilder sb4 = new StringBuilder();
            sb4.append(" superResolution:");
            sb4.append(bool2);
            sb.append(sb4.toString());
            Boolean bool3 = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.MFNR_ENABLED);
            StringBuilder sb5 = new StringBuilder();
            sb5.append(" mfnrEnable:");
            sb5.append(bool3);
            sb.append(sb5.toString());
            Boolean bool4 = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.SW_MFNR_ENABLED);
            StringBuilder sb6 = new StringBuilder();
            sb6.append(" swMfnrEnable:");
            sb6.append(bool4);
            sb.append(sb6.toString());
            int satMasterCameraId = CaptureResultParser.getSatMasterCameraId(captureResult);
            StringBuilder sb7 = new StringBuilder();
            sb7.append(" 180cameraID:");
            sb7.append(satMasterCameraId);
            sb.append(sb7.toString());
            Boolean bool5 = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.SUPER_NIGHT_SCENE_ENABLED);
            StringBuilder sb8 = new StringBuilder();
            sb8.append(" superNight:");
            sb8.append(bool5);
            sb.append(sb8.toString());
            Boolean bool6 = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.FRONT_SINGLE_CAMERA_BOKEH);
            StringBuilder sb9 = new StringBuilder();
            sb9.append(" frontPortraitBokeh:");
            sb9.append(bool6);
            sb.append(sb9.toString());
            String str3 = " remosaic:";
            if (C0124O00000oO.isMTKPlatform()) {
                Boolean bool7 = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.CONTROL_ENABLE_REMOSAIC);
                StringBuilder sb10 = new StringBuilder();
                sb10.append(str3);
                sb10.append(bool7);
                sb.append(sb10.toString());
                Integer num = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.CONTROL_ENABLE_SPECSHOT_MODE);
                StringBuilder sb11 = new StringBuilder();
                sb11.append(" specshot:");
                sb11.append(num);
                str = sb11.toString();
            } else {
                Boolean bool8 = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.REMOSAIC_DETECTED);
                StringBuilder sb12 = new StringBuilder();
                sb12.append(str3);
                sb12.append(bool8);
                str = sb12.toString();
            }
            sb.append(str);
            if (pictureInfo.getOperateMode() == 36864) {
                str2 = " bokehEnable:true";
            } else {
                Boolean bool9 = (Boolean) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.REAR_BOKEH_ENABLE);
                StringBuilder sb13 = new StringBuilder();
                sb13.append(" bokehEnable:");
                sb13.append(bool9);
                str2 = sb13.toString();
            }
            sb.append(str2);
            Byte b = (Byte) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.DEPURPLE);
            String str4 = (b == null || b.byteValue() != 1) ? " Depurple:false " : " Depurple:true ";
            sb.append(str4);
            Byte b2 = (Byte) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.ULTRA_WIDE_LENS_DISTORTION_CORRECTION_LEVEL);
            String str5 = (b2 == null || b2.byteValue() != 1) ? " uwldc:false " : " uwldc:true ";
            sb.append(str5);
            String str6 = (String) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_LEVEL);
            StringBuilder sb14 = new StringBuilder();
            sb14.append(" beautyLevel: ");
            sb14.append(str6);
            sb.append(sb14.toString());
            Integer num2 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_SKIN_COLOR);
            StringBuilder sb15 = new StringBuilder();
            sb15.append(" beautySkinColor: ");
            sb15.append(num2);
            sb.append(sb15.toString());
            Integer num3 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_SLIM_FACE);
            StringBuilder sb16 = new StringBuilder();
            sb16.append(" beautySlimFace: ");
            sb16.append(num3);
            sb.append(sb16.toString());
            Integer num4 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_SKIN_SMOOTH);
            StringBuilder sb17 = new StringBuilder();
            sb17.append(" beautySlimSmooth: ");
            sb17.append(num4);
            sb.append(sb17.toString());
            Integer num5 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_ENLARGE_EYE);
            StringBuilder sb18 = new StringBuilder();
            sb18.append(" beautyEnlargeEye: ");
            sb18.append(num5);
            sb.append(sb18.toString());
            Integer num6 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_NOSE);
            StringBuilder sb19 = new StringBuilder();
            sb19.append(" beautyNose: ");
            sb19.append(num6);
            sb.append(sb19.toString());
            Integer num7 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_RISORIUS);
            StringBuilder sb20 = new StringBuilder();
            sb20.append(" beautyRisorius: ");
            sb20.append(num7);
            sb.append(sb20.toString());
            Integer num8 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_LIPS);
            StringBuilder sb21 = new StringBuilder();
            sb21.append(" beautyLips: ");
            sb21.append(num8);
            sb.append(sb21.toString());
            Integer num9 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_CHIN);
            StringBuilder sb22 = new StringBuilder();
            sb22.append(" beautyChin: ");
            sb22.append(num9);
            sb.append(sb22.toString());
            Integer num10 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_SMILE);
            StringBuilder sb23 = new StringBuilder();
            sb23.append(" beautySmile: ");
            sb23.append(num10);
            sb.append(sb23.toString());
            Integer num11 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_SLIM_NOSE);
            StringBuilder sb24 = new StringBuilder();
            sb24.append(" beautySlimNose: ");
            sb24.append(num11);
            sb.append(sb24.toString());
            Integer num12 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_HAIRLINE);
            StringBuilder sb25 = new StringBuilder();
            sb25.append(" beautyHairLine: ");
            sb25.append(num12);
            sb.append(sb25.toString());
            Integer num13 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_EYEBROW_DYE);
            StringBuilder sb26 = new StringBuilder();
            sb26.append(" beautyEyebowDye: ");
            sb26.append(num13);
            sb.append(sb26.toString());
            Integer num14 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_PUPIL_LINE);
            StringBuilder sb27 = new StringBuilder();
            sb27.append(" beautyPupilLine: ");
            sb27.append(num14);
            sb.append(sb27.toString());
            Integer num15 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_JELLY_LIPS);
            StringBuilder sb28 = new StringBuilder();
            sb28.append(" beautyJellyLips: ");
            sb28.append(num15);
            sb.append(sb28.toString());
            Integer num16 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_BLUSHER);
            StringBuilder sb29 = new StringBuilder();
            sb29.append(" beautyBluser: ");
            sb29.append(num16);
            sb.append(sb29.toString());
            Integer num17 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.EYE_LIGHT_TYPE);
            StringBuilder sb30 = new StringBuilder();
            sb30.append(" beautyEyeLight: ");
            sb30.append(num17);
            sb.append(sb30.toString());
            Integer num18 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.EYE_LIGHT_STRENGTH);
            StringBuilder sb31 = new StringBuilder();
            sb31.append(" beautyEyeLightStrength: ");
            sb31.append(num18);
            sb.append(sb31.toString());
            Integer num19 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_HEAD_SLIM);
            StringBuilder sb32 = new StringBuilder();
            sb32.append(" beautyHeadSlim: ");
            sb32.append(num19);
            sb.append(sb32.toString());
            Integer num20 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_BODY_SLIM);
            StringBuilder sb33 = new StringBuilder();
            sb33.append(" beautyBodySlim: ");
            sb33.append(num20);
            sb.append(sb33.toString());
            Integer num21 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_SHOULDER_SLIM);
            StringBuilder sb34 = new StringBuilder();
            sb34.append(" beautyShoulderSlim: ");
            sb34.append(num21);
            sb.append(sb34.toString());
            Integer num22 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BEAUTY_LEG_SLIM);
            StringBuilder sb35 = new StringBuilder();
            sb35.append(" beautyLegSlim: ");
            sb35.append(num22);
            sb.append(sb35.toString());
            Integer num23 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.WHOLE_BODY_SLIM);
            StringBuilder sb36 = new StringBuilder();
            sb36.append(" beautyWholeBodySlim: ");
            sb36.append(num23);
            sb.append(sb36.toString());
            Integer num24 = (Integer) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.BUTT_SLIM);
            StringBuilder sb37 = new StringBuilder();
            sb37.append(" beautyButtSlim: ");
            sb37.append(num24);
            sb.append(sb37.toString());
            StringBuilder sb38 = new StringBuilder();
            sb38.append(" cameraPreferredMode: ");
            sb38.append(dataParameter.getCameraPreferredMode());
            sb.append(sb38.toString());
            String sb39 = sb.toString();
            byte[] exifValues = CaptureResultParser.getExifValues(captureResult);
            if (exifValues != null && exifValues.length > 0) {
                pictureInfo.setCaptureResult(new String(exifValues));
            }
            if (!TextUtils.isEmpty(sb39)) {
                pictureInfo.setAlgoExif(sb39);
            }
            setSuperNightExif(captureResult, pictureInfo);
        }
    }

    private DrawJPEGAttribute getDrawJPEGAttribute(byte[] bArr, int i, int i2, int i3, boolean z, int i4, int i5, Location location, String str, int i6, int i7, float f, String str2, boolean z2, boolean z3, String str3, boolean z4, boolean z5, DeviceWatermarkParam deviceWatermarkParam, List list, boolean z6, PictureInfo pictureInfo, int i8, int i9, boolean z7) {
        int i10 = i4;
        int i11 = i5;
        Location location2 = location;
        int max = i10 > i11 ? Math.max(i, i2) : Math.min(i, i2);
        int max2 = i11 > i10 ? Math.max(i, i2) : Math.min(i, i2);
        EffectRectAttribute copyEffectRectAttribute = EffectController.getInstance().copyEffectRectAttribute();
        Location location3 = location2 == null ? null : new Location(location2);
        long currentTimeMillis = System.currentTimeMillis();
        boolean isFrontMirror = pictureInfo.isFrontMirror();
        boolean z8 = CameraSettings.isDualCameraWaterMarkOpen() || CameraSettings.isFrontCameraWaterMarkOpen();
        DrawJPEGAttribute drawJPEGAttribute = new DrawJPEGAttribute(bArr, z, max, max2, i4, i5, i3, copyEffectRectAttribute, location3, str, currentTimeMillis, i6, i7, f, isFrontMirror, str2, z2, pictureInfo, list, z8, z3, CameraSettings.isTimeWaterMarkOpen() ? str3 : null, z4, z5, deviceWatermarkParam, z6, i8, i9, z7);
        return drawJPEGAttribute;
    }

    private void initEffectProcessorLocked() {
        if (this.mEffectProcessor == null) {
            this.mEffectProcessor = new SnapshotEffectRender((ImageSaverCallback) this.mSaverCallback.get(), this.mIsCaptureIntent);
            this.mEffectProcessor.setImageSaver(this);
            this.mEffectProcessor.setQuality(CameraSettings.getEncodingQuality(false).toInteger(false));
        }
    }

    private void insertImageSaveRequest(ParallelTaskData parallelTaskData) {
        addSaveRequest(new ImageSaveRequest(parallelTaskData, this));
    }

    private void insertParallelSaveRequest(ParallelTaskData parallelTaskData) {
        addSaveRequest(new ParallelSaveRequest(parallelTaskData, this));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0038, code lost:
        insertImageSaveRequest(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0022, code lost:
        insertParallelSaveRequest(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x002d, code lost:
        if (r2.isShot2Gallery() == false) goto L_0x0038;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void insertParallelTaskData(ParallelTaskData parallelTaskData, @Nullable CaptureResult captureResult, @Nullable CameraCharacteristics cameraCharacteristics) {
        switch (parallelTaskData.getParallelType()) {
            case -7:
            case -6:
            case -5:
                processParallelIntentResult(parallelTaskData);
                return;
            case -4:
            case 9:
                break;
            case -3:
            case -2:
                processIntentResult(parallelTaskData);
                return;
            case -1:
                insertPreviewSaveRequest(parallelTaskData);
                return;
            case 0:
            case 2:
            case 10:
                break;
            case 1:
                insertRawImageSaveRequest(parallelTaskData, captureResult, cameraCharacteristics);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown shot type: ");
                sb.append(parallelTaskData.getParallelType());
                throw new RuntimeException(sb.toString());
        }
    }

    private void insertPreviewSaveRequest(ParallelTaskData parallelTaskData) {
        addSaveRequest(new PreviewSaveRequest(parallelTaskData, this, parallelTaskData.noGaussian()), true);
    }

    private boolean isLastImageForThumbnail() {
        return true;
    }

    private void processIntentResult(ParallelTaskData parallelTaskData) {
        ImageSaveRequest imageSaveRequest = new ImageSaveRequest(parallelTaskData, this);
        imageSaveRequest.setSaverCallback(this);
        imageSaveRequest.parserParallelTaskData();
        showCaptureResultOnCover(parallelTaskData, imageSaveRequest.width, imageSaveRequest.orientation);
    }

    private void processParallelIntentResult(ParallelTaskData parallelTaskData) {
        ParallelSaveRequest parallelSaveRequest = new ParallelSaveRequest(parallelTaskData, this);
        parallelSaveRequest.setSaverCallback(this);
        parallelSaveRequest.parserParallelTaskData();
        showCaptureResultOnCover(parallelTaskData, parallelSaveRequest.width, parallelSaveRequest.orientation);
    }

    private void releaseEffectProcessor() {
        if (!ModuleManager.isCapture() && !ModuleManager.isPortraitModule()) {
            synchronized (this.mEffectProcessorLock) {
                if (this.mEffectProcessor != null) {
                    Log.i(TAG, "release Effect Processor");
                    this.mEffectProcessor.releaseIfNeeded();
                    this.mEffectProcessor = null;
                }
            }
        }
    }

    private void releaseResourcesIfQueueEmpty() {
        if (this.mHostState == 2 && mSaveRequestQueue.size() <= 0 && mPreviewRequestQueue.size() <= 0 && this.mLiveShotPendingTaskQueue.size() <= 0) {
            this.mStoredTaskData = null;
        }
    }

    private void setSuperNightExif(CaptureResult captureResult, PictureInfo pictureInfo) {
        if (captureResult != null && pictureInfo != null) {
            SuperNightExif superNightExif = MarshalQueryableSuperNightExif.getSuperNightExif(captureResult, this.mSupportSuperNightExif);
            if (superNightExif != null) {
                String superNightExif2 = DebugInfoUtil.getSuperNightExif(superNightExif);
                if (!TextUtils.isEmpty(superNightExif2)) {
                    pictureInfo.setSuperNightExif(superNightExif2);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0059, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showCaptureResultOnCover(ParallelTaskData parallelTaskData, int i, int i2) {
        synchronized (this.mBitmapTextureLock) {
            if (this.mDropBitmapTexture) {
                Log.d(TAG, "showCaptureResultOnCover drop it");
                this.mDropBitmapTexture = false;
                return;
            }
            ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
            this.mStoredTaskData = parallelTaskData;
            ImageSaverCallback imageSaverCallback = (ImageSaverCallback) this.mSaverCallback.get();
            Bitmap createBitmap = Thumbnail.createBitmap(parallelTaskData.getJpegImageData(), i2 + (360 - dataParameter.getShootOrientation()) + (imageSaverCallback == null ? 0 : imageSaverCallback.getDisplayRotation()), false, Integer.highestOneBit((int) Math.ceil(((double) i) / ((double) dataParameter.getPreviewSize().getWidth()))));
            if (!(createBitmap == null || imageSaverCallback == null)) {
                imageSaverCallback.getCameraScreenNail().renderBitmapToCanvas(createBitmap);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateThumbnail(boolean z) {
        Thumbnail thumbnail;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateThumbnail needAnimation:");
        sb.append(z);
        Log.d(str, sb.toString());
        synchronized (this.mUpdateThumbnailLock) {
            this.mHandler.removeCallbacks(this.mUpdateThumbnail);
            thumbnail = this.mPendingThumbnail;
            this.mPendingThumbnail = null;
        }
        if (thumbnail != null) {
            ImageSaverCallback imageSaverCallback = (ImageSaverCallback) this.mSaverCallback.get();
            if (imageSaverCallback != null) {
                imageSaverCallback.getThumbnailUpdater().setThumbnail(thumbnail, true, z);
                if (imageSaverCallback.isActivityPaused()) {
                    imageSaverCallback.getThumbnailUpdater().saveThumbnailToFile();
                }
            }
        }
    }

    public void addGif(String str, int i, int i2) {
        synchronized (this) {
            if (2 == this.mHostState) {
                Log.v(TAG, "addVideo: host is being destroyed.");
            }
            String str2 = str;
            GifSaveRequest gifSaveRequest = new GifSaveRequest(str2, System.currentTimeMillis() - 1, Util.getFileTitleFromPath(str), i, i2, 90);
            addSaveRequest(gifSaveRequest);
        }
    }

    public Uri addGifSync(String str, int i, int i2) {
        Uri uri;
        synchronized (this) {
            if (2 == this.mHostState) {
                Log.v(TAG, "addVideo: host is being destroyed.");
            }
            String str2 = str;
            GifSaveRequest gifSaveRequest = new GifSaveRequest(str2, System.currentTimeMillis() - 1, Util.getFileTitleFromPath(str), i, i2, 90);
            gifSaveRequest.setContextAndCallback(this.mContext, this);
            gifSaveRequest.save();
            uri = gifSaveRequest.mUri;
        }
        return uri;
    }

    public void addHeif(@NonNull Image image, @NonNull CaptureResult captureResult, @NonNull ParallelTaskData parallelTaskData, @NonNull SaveHeifCallback saveHeifCallback) {
        if (this.mHeifHandlerThread == null) {
            this.mHeifHandlerThread = new HandlerThread("HeifSaverThread");
            this.mHeifHandlerThread.start();
            this.mHeifHandler = new Handler(this.mHeifHandlerThread.getLooper());
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("HeifSaverThread: id = ");
            sb.append(this.mHeifHandlerThread.getThreadId());
            Log.v(str, sb.toString());
        }
        HeifSaveRequest heifSaveRequest = new HeifSaveRequest(image, captureResult, parallelTaskData, saveHeifCallback, this.mHeifHandler);
        addSaveRequest(heifSaveRequest, true);
    }

    public void addImage(byte[] bArr, boolean z, String str, String str2, long j, Uri uri, Location location, int i, int i2, ExifInterface exifInterface, int i3, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, String str3, PictureInfo pictureInfo, int i4, CaptureResult captureResult) {
        Uri uri2 = uri;
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isParallelProcess: parallel=");
        sb.append(z6);
        sb.append(" uri=");
        sb.append(uri2);
        sb.append(" algo=");
        sb.append(str3);
        Log.d(str4, sb.toString());
        if (str2 != null && uri2 == null) {
            uri2 = this.mLastImageUri;
        }
        Uri uri3 = uri2;
        PerformanceTracker.trackImageSaver(bArr, 0);
        setSuperNightExif(captureResult, pictureInfo);
        ImageSaveRequest imageSaveRequest = new ImageSaveRequest(bArr, z, str, str2, j, uri3, location, i, i2, exifInterface, i3, z2, z3, z4, z5, z6, str3, pictureInfo, i4);
        addSaveRequest(imageSaveRequest);
    }

    public void addSimpleImage(byte[] bArr, boolean z, String str, String str2, long j, Uri uri, Location location, int i, int i2, ExifInterface exifInterface, int i3, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, String str3, PictureInfo pictureInfo, int i4, CaptureResult captureResult) {
        Uri uri2 = uri;
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isParallelProcess: parallel=");
        sb.append(z6);
        sb.append(" uri=");
        sb.append(uri2);
        sb.append(" algo=");
        sb.append(str3);
        Log.d(str4, sb.toString());
        if (str2 != null && uri2 == null) {
            uri2 = this.mLastImageUri;
        }
        Uri uri3 = uri2;
        PerformanceTracker.trackImageSaver(bArr, 0);
        setSuperNightExif(captureResult, pictureInfo);
        SimpleImageSaveRequest simpleImageSaveRequest = new SimpleImageSaveRequest(bArr, z, str, str2, j, uri3, location, i, i2, exifInterface, i3, z2, z3, z4, z5, z6, str3, pictureInfo, i4);
        addSaveRequest(simpleImageSaveRequest);
    }

    public synchronized void addUsedMemory(int i) {
        this.mMemoryManager.addUsedMemory(i);
    }

    public Uri addVideo(String str, ContentValues contentValues, boolean z, boolean z2, List list) {
        synchronized (this) {
            if (2 == this.mHostState) {
                Log.v(TAG, "addVideo: host is being destroyed.");
            }
            VideoSaveRequest videoSaveRequest = new VideoSaveRequest(str, contentValues, z);
            videoSaveRequest.setTags(list);
            if (z2) {
                videoSaveRequest.setContextAndCallback(this.mContext, this);
                videoSaveRequest.save();
                Uri uri = videoSaveRequest.mUri;
                return uri;
            }
            addSaveRequest(videoSaveRequest);
            return null;
        }
    }

    public void addVideo(String str, ContentValues contentValues, boolean z) {
        synchronized (this) {
            if (2 == this.mHostState) {
                Log.v(TAG, "addVideo: host is being destroyed.");
            }
            addSaveRequest(new VideoSaveRequest(str, contentValues, z));
        }
    }

    public Uri addVideoSync(String str, ContentValues contentValues, boolean z) {
        Uri uri;
        synchronized (this) {
            if (2 == this.mHostState) {
                Log.v(TAG, "addVideo: host is being destroyed.");
            }
            VideoSaveRequest videoSaveRequest = new VideoSaveRequest(str, contentValues, z);
            videoSaveRequest.setContextAndCallback(this.mContext, this);
            videoSaveRequest.save();
            uri = videoSaveRequest.mUri;
        }
        return uri;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        super.finalize();
        HandlerThread handlerThread = this.mHeifHandlerThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            this.mHeifHandlerThread = null;
            this.mHeifHandler = null;
        }
    }

    public int getBurstDelay() {
        return this.mMemoryManager.getBurstDelay();
    }

    public int getInFlightTask() {
        return this.mSaveQueueSize.get();
    }

    public byte[] getStoredJpegData() {
        return this.mStoredTaskData.getJpegImageData();
    }

    public float getSuitableBurstShotSpeed() {
        return 0.66f;
    }

    public void insertRawImageSaveRequest(ParallelTaskData parallelTaskData, CaptureResult captureResult, CameraCharacteristics cameraCharacteristics) {
        String str;
        byte[] rawImageData = parallelTaskData.getRawImageData();
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        if (parallelTaskData.isShot2Gallery() || parallelTaskData.isInTimerBurstShotting()) {
            str = Util.getFileTitleFromPath(parallelTaskData.getSavePath());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(Util.createJpegName(System.currentTimeMillis()));
            sb.append(parallelTaskData.getDataParameter().getSuffix());
            str = sb.toString();
        }
        String str2 = str;
        int width = dataParameter.getRawSize().getWidth();
        int height = dataParameter.getRawSize().getHeight();
        int intValue = ((Integer) captureResult.get(CaptureResult.JPEG_ORIENTATION)).intValue();
        long dateTakenTime = parallelTaskData.getDateTakenTime() - 1;
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("insertRawImageSaveRequest title = ");
        sb2.append(str2);
        sb2.append(", orientation = ");
        sb2.append(intValue);
        Log.d(str3, sb2.toString());
        PerformanceTracker.trackImageSaver(rawImageData, 0);
        RawImageSaveRequest rawImageSaveRequest = new RawImageSaveRequest(rawImageData, captureResult, cameraCharacteristics, dateTakenTime, str2, width, height, intValue);
        addSaveRequest(rawImageSaveRequest);
    }

    public boolean isBusy() {
        return this.mIsBusy;
    }

    public boolean isNeedSlowDown() {
        return this.mMemoryManager.isNeedSlowDown();
    }

    public boolean isNeedStopCapture() {
        return this.mMemoryManager.isNeedStopCapture();
    }

    public boolean isPendingSave() {
        return this.mLiveShotPendingTaskQueue.size() > 0 || mSaveRequestQueue.size() > 0 || mPreviewRequestQueue.size() > 0;
    }

    public synchronized boolean isSaveQueueFull() {
        boolean isSaveQueueFull;
        isSaveQueueFull = this.mMemoryManager.isSaveQueueFull();
        this.mIsBusy |= isSaveQueueFull;
        return isSaveQueueFull;
    }

    public boolean needThumbnail(boolean z) {
        boolean z2;
        synchronized (this) {
            if (z) {
                try {
                    if (isLastImageForThumbnail() && !this.mIsCaptureIntent) {
                        z2 = true;
                    }
                } finally {
                }
            }
            z2 = false;
        }
        return z2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r5 != null) goto L_0x0030;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyNewMediaData(Uri uri, String str, int i) {
        ImageSaverCallback imageSaverCallback;
        if (!this.mIsCaptureIntent) {
            synchronized (this) {
                if (i == 1) {
                    this.mContext.sendBroadcast(new Intent("android.hardware.action.NEW_VIDEO", uri));
                    imageSaverCallback = (ImageSaverCallback) this.mSaverCallback.get();
                    if (imageSaverCallback != null) {
                    }
                } else if (i == 2) {
                    Util.broadcastNewPicture(this.mContext, uri);
                    this.mLastImageUri = uri;
                    imageSaverCallback = (ImageSaverCallback) this.mSaverCallback.get();
                } else if (i == 3) {
                    ImageSaverCallback imageSaverCallback2 = (ImageSaverCallback) this.mSaverCallback.get();
                    if (imageSaverCallback2 != null) {
                        imageSaverCallback2.onNewUriArrived(null, str);
                    }
                }
                imageSaverCallback.onNewUriArrived(uri, str);
            }
        }
    }

    public void onHostDestroy() {
        synchronized (this) {
            this.mHostState = 2;
            releaseResourcesIfQueueEmpty();
        }
        synchronized (this.mUpdateThumbnailLock) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mPendingThumbnail = null;
        }
        if (this.mSaveQueueSize.get() == 0) {
            synchronized (this.mEffectProcessorLock) {
                if (this.mEffectProcessor != null) {
                    Log.i(TAG, "release Effect Processor");
                    this.mEffectProcessor.releaseIfNeeded();
                    this.mEffectProcessor = null;
                }
            }
        }
        Log.v(TAG, "onHostDestroy");
    }

    public void onHostPause() {
        synchronized (this) {
            this.mHostState = 1;
        }
        synchronized (this.mUpdateThumbnailLock) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mPendingThumbnail = null;
        }
        Log.v(TAG, "onHostPause");
    }

    public void onHostResume(boolean z) {
        synchronized (this) {
            this.mIsCaptureIntent = z;
            this.mHostState = 0;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onHostResume: isCapture=");
            sb.append(this.mIsCaptureIntent);
            Log.v(str, sb.toString());
        }
    }

    public void onModuleDestroy() {
        if (this.mSaveQueueSize.get() != 0 || ModuleManager.isCapture() || ModuleManager.isPortraitModule()) {
            synchronized (this.mEffectProcessorLock) {
                this.mbModuleSwitch.set(true);
            }
            return;
        }
        synchronized (this.mEffectProcessorLock) {
            if (this.mEffectProcessor != null) {
                Log.i(TAG, "release Effect Processor");
                this.mEffectProcessor.releaseIfNeeded();
                this.mEffectProcessor = null;
                this.mbModuleSwitch.set(false);
            }
        }
    }

    public boolean onParallelProcessFinish(ParallelTaskData parallelTaskData, @Nullable CaptureResult captureResult, @Nullable CameraCharacteristics cameraCharacteristics) {
        int i;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onParallelProcessFinish: path: ");
        sb.append(parallelTaskData.getSavePath());
        Log.k(4, str, sb.toString());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onParallelProcessFinish: live: ");
        sb2.append(parallelTaskData.isLiveShotTask());
        Log.i(str2, sb2.toString());
        String str3 = "onParallelProcessFinish: insert: ";
        if (parallelTaskData.isLiveShotTask()) {
            if (parallelTaskData.getMicroVideoPath() != null) {
                String str4 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(parallelTaskData.hashCode());
                Log.d(str4, sb3.toString());
                if (this.mLiveShotPendingTaskQueue.remove(parallelTaskData)) {
                    String str5 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("onParallelProcessFinish: ");
                    sb4.append(parallelTaskData.hashCode());
                    Log.d(str5, sb4.toString());
                }
                if (parallelTaskData.getJpegImageData() != null) {
                    insertParallelTaskData(parallelTaskData, null, null);
                } else {
                    Log.e(TAG, "onParallelProcessFinish: error: jpeg data is null");
                    return false;
                }
            } else {
                String str6 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("onParallelProcessFinish: enqueue: ");
                sb5.append(parallelTaskData.hashCode());
                Log.d(str6, sb5.toString());
                this.mLiveShotPendingTaskQueue.offer(parallelTaskData);
                byte[] jpegImageData = parallelTaskData.getJpegImageData();
                if (jpegImageData != null) {
                    i = jpegImageData.length;
                    addUsedMemory(i);
                } else {
                    i = 0;
                }
                String str7 = TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("onParallelProcessFinish: memory[+]: ");
                sb6.append(i);
                sb6.append(", task: ");
                sb6.append(parallelTaskData.hashCode());
                Log.d(str7, sb6.toString());
            }
            String str8 = TAG;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("onParallelProcessFinish: pending: ");
            sb7.append(this.mLiveShotPendingTaskQueue.size());
            Log.d(str8, sb7.toString());
            return false;
        }
        String str9 = TAG;
        StringBuilder sb8 = new StringBuilder();
        sb8.append(str3);
        sb8.append(parallelTaskData.hashCode());
        Log.d(str9, sb8.toString());
        dealExif(parallelTaskData, captureResult);
        insertParallelTaskData(parallelTaskData, captureResult, cameraCharacteristics);
        return false;
    }

    public void onSaveFinish(int i) {
        synchronized (this) {
            reduceUsedMemory(i);
            if (this.mSaveQueueSize.decrementAndGet() == 0 && (this.mbModuleSwitch.get() || 2 == this.mHostState || 1 == this.mHostState)) {
                synchronized (this.mEffectProcessorLock) {
                    if (this.mEffectProcessor != null) {
                        Log.i(TAG, "release Effect Processor");
                        this.mEffectProcessor.releaseIfNeeded();
                        this.mEffectProcessor = null;
                    }
                    this.mbModuleSwitch.set(false);
                }
            }
            if (!isSaveQueueFull() && mSaveRequestQueue.size() < 40 && mPreviewRequestQueue.size() < 40) {
                this.mIsBusy = false;
            }
            releaseResourcesIfQueueEmpty();
        }
    }

    public void onVideoClipSavingCancelled(@Nullable Object obj) {
        Log.d(TAG, "onVideoClipSavingCancelled: video = 0, timestamp = -1");
        onVideoClipSavingCompleted(obj, VideoClipSavingCallback.EMPTY_VIDEO_PATH, -1);
    }

    public void onVideoClipSavingCompleted(@Nullable Object obj, String str, long j) {
        if (!ParallelTaskData.class.isInstance(obj)) {
            Log.d(TAG, "onVideoClipSavingCompleted: Oops, corresponding task is not found");
            return;
        }
        ParallelTaskData parallelTaskData = (ParallelTaskData) obj;
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onVideoClipSavingCompleted: timestamp = ");
        sb.append(j);
        Log.d(str2, sb.toString());
        parallelTaskData.fillVideoPath(str, j);
        String str3 = ", task: ";
        String str4 = "onVideoClipSavingCompleted: memory[-]: ";
        if (parallelTaskData.isJpegDataReady()) {
            if (this.mLiveShotPendingTaskQueue.remove(parallelTaskData)) {
                int length = parallelTaskData.getJpegImageData().length;
                reduceUsedMemory(length);
                String str5 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str4);
                sb2.append(length);
                sb2.append(str3);
                sb2.append(parallelTaskData.hashCode());
                Log.d(str5, sb2.toString());
            }
            insertParallelTaskData(parallelTaskData, null, null);
        } else if (parallelTaskData.isPictureFilled()) {
            Log.e(TAG, "onVideoClipSavingCompleted: get error jpeg data, ignore this liveshot");
            if (this.mLiveShotPendingTaskQueue.remove(parallelTaskData)) {
                int length2 = parallelTaskData.getJpegImageData().length;
                reduceUsedMemory(length2);
                String str6 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str4);
                sb3.append(length2);
                sb3.append(str3);
                sb3.append(parallelTaskData.hashCode());
                Log.d(str6, sb3.toString());
            }
        } else {
            String str7 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("onVideoClipSavingCompleted: enqueue: ");
            sb4.append(parallelTaskData.hashCode());
            Log.d(str7, sb4.toString());
            this.mLiveShotPendingTaskQueue.offer(parallelTaskData);
        }
        String str8 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("onVideoClipSavingCompleted: pending: ");
        sb5.append(this.mLiveShotPendingTaskQueue.size());
        Log.d(str8, sb5.toString());
    }

    public void onVideoClipSavingException(@Nullable Object obj, @NonNull Throwable th) {
        Log.d(TAG, "onVideoClipSavingException: video = 0, timestamp = -1");
        onVideoClipSavingCompleted(obj, VideoClipSavingCallback.EMPTY_VIDEO_PATH, -1);
    }

    public void postHideThumbnailProgressing() {
        synchronized (this.mUpdateThumbnailLock) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
                    if (actionProcessing != null) {
                        actionProcessing.updateLoading(true);
                    }
                }
            });
        }
    }

    public void postUpdateThumbnail(Thumbnail thumbnail, boolean z) {
        synchronized (this.mUpdateThumbnailLock) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("postUpdateThumbnail ");
            sb.append(z);
            Log.d(str, sb.toString());
            this.mPendingThumbnail = thumbnail;
            this.mUpdateThumbnail.setNeedAnimation(z);
            this.mHandler.post(this.mUpdateThumbnail);
        }
    }

    public void processorJpegSync(boolean z, DrawJPEGAttribute... drawJPEGAttributeArr) {
        synchronized (this.mEffectProcessorLock) {
            initEffectProcessorLocked();
            if (this.mEffectProcessor != null) {
                for (DrawJPEGAttribute drawJPEGAttribute : drawJPEGAttributeArr) {
                    if (drawJPEGAttribute != null) {
                        this.mEffectProcessor.processorJpegSync(drawJPEGAttribute, z);
                    }
                }
            } else {
                Log.d(TAG, "processorJpegSync(): mEffectProcessor is null");
            }
        }
    }

    public synchronized void reduceUsedMemory(int i) {
        this.mMemoryManager.reduceUsedMemory(i);
    }

    public void releaseStoredJpegData() {
        this.mStoredTaskData.releaseImageData();
    }

    public void saveStoredData() {
        int i;
        int i2;
        ParallelTaskData parallelTaskData = this.mStoredTaskData;
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        String createJpegName = Util.createJpegName(System.currentTimeMillis());
        int width = dataParameter.getOutputSize().getWidth();
        int height = dataParameter.getOutputSize().getHeight();
        int orientation = ExifHelper.getOrientation(this.mStoredTaskData.getJpegImageData());
        if ((dataParameter.getJpegRotation() + orientation) % 180 == 0) {
            i2 = width;
            i = height;
        } else {
            i = width;
            i2 = height;
        }
        addImage(this.mStoredTaskData.getJpegImageData(), parallelTaskData.isNeedThumbnail(), createJpegName, null, System.currentTimeMillis(), null, dataParameter.getLocation(), i2, i, null, orientation, false, false, true, false, false, dataParameter.getAlgorithmName(), dataParameter.getPictureInfo(), -1, null);
    }

    public void setDropBitmapTexture(boolean z) {
        synchronized (this.mBitmapTextureLock) {
            this.mDropBitmapTexture = z;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setDropBitmapTexture ");
            sb.append(z);
            Log.d(str, sb.toString());
        }
    }

    public void setSuperNightExifSupport(boolean z) {
        this.mSupportSuperNightExif = z;
    }

    public void updateImage(String str, String str2) {
        ImageSaveRequest imageSaveRequest = new ImageSaveRequest();
        imageSaveRequest.title = str;
        imageSaveRequest.oldTitle = str2;
        addSaveRequest(imageSaveRequest);
    }

    public void updatePreviewThumbnailUri(int i, Uri uri) {
        synchronized (this.mUpdateThumbnailLock) {
            ImageSaverCallback imageSaverCallback = (ImageSaverCallback) this.mSaverCallback.get();
            Thumbnail thumbnail = null;
            if (imageSaverCallback != null) {
                thumbnail = imageSaverCallback.getThumbnailUpdater().getThumbnail();
            }
            if (thumbnail != null) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("previewThumbnailHash:");
                sb.append(i);
                sb.append(" current thumbnail hash:");
                sb.append(thumbnail.hashCode());
                Log.d(str, sb.toString());
                if (i <= 0 || thumbnail.hashCode() == i) {
                    thumbnail.setUri(uri);
                }
            }
        }
    }
}
