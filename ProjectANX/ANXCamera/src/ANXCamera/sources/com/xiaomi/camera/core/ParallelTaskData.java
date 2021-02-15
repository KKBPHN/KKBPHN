package com.xiaomi.camera.core;

import android.graphics.Rect;
import android.media.Image;
import com.android.camera.LocalParallelService.ServiceStatusListener;
import com.android.camera.log.Log;
import com.xiaomi.camera.core.ParallelTaskDataParameter.Builder;
import com.xiaomi.camera.liveshot.CircularMediaRecorder.VideoClipSavingCallback;
import com.xiaomi.protocol.ICustomCaptureResult;

public class ParallelTaskData {
    private static final String GROUPSHOT_ORIGINAL_SUFFIX = "_ORG";
    private static final String TAG = "ParallelTaskData";
    private int currentModuleIndex;
    private boolean isAbandoned;
    private boolean isAdaptiveSnapshotSize;
    private boolean isHWMFNRProcessing;
    private boolean isLiveShotTask;
    private boolean isNeedThumbnail;
    private boolean isPictureFilled;
    private Rect mActiveRegion;
    public OnParallelTaskDataAddToProcessorListener mAddToProcessorCallback;
    private int mAlgoType;
    private int mBurstNum;
    private int mCameraId;
    private ICustomCaptureResult mCaptureResult;
    private long mCaptureTime;
    private int[] mCoordinatesOfTheRegionUnderWatermarks;
    private long mCoverFrameTimestamp;
    private byte[] mDataOfTheRegionUnderWatermarks;
    private ParallelTaskDataParameter mDataParameter;
    private long mDateTakenTime;
    private boolean mInTimerBurstShotting;
    public boolean mIsFrontProcessing;
    private boolean mIsHdrSR;
    private boolean mIsParallelVTCameraSnapshot;
    private boolean mIsSatFusionShot;
    private boolean mIsSaveToHiddenFolder;
    private boolean mIsShot2Gallery;
    private byte[] mJpegImageData;
    private boolean mMemDebug;
    private int mParallelType;
    private byte[] mPortraitDepthData;
    private byte[] mPortraitRawData;
    private boolean mRaw2YuvDone;
    private ImageProcessor mRaw2YuvProcessor;
    private byte[] mRawImageData;
    private int mRawInputHeight;
    private int mRawInputWidth;
    private boolean mRequireTuningData;
    private String mSavePath;
    private ServiceStatusListener mServiceStatusListener;
    private long mTimestamp;
    private Image mTuningImage;
    private String mVideoPath;
    private float mZoomRatio;
    private boolean noGaussian;
    private int previewThumbnailHash;

    public interface OnParallelTaskDataAddToProcessorListener {
        void OnParallelTaskDataAddToProcessor();
    }

    public ParallelTaskData(int i, long j, int i2, String str) {
        this(i, j, i2, str, 0);
    }

    public ParallelTaskData(int i, long j, int i2, String str, long j2) {
        this.currentModuleIndex = -1;
        this.isHWMFNRProcessing = false;
        this.isAbandoned = false;
        this.mCameraId = i;
        this.mTimestamp = j;
        this.mParallelType = i2;
        this.mSavePath = str;
        this.mDateTakenTime = System.currentTimeMillis();
        this.mCaptureTime = j2;
    }

    public ParallelTaskData(ParallelTaskData parallelTaskData) {
        this.currentModuleIndex = -1;
        this.isHWMFNRProcessing = false;
        this.isAbandoned = false;
        this.mParallelType = parallelTaskData.mParallelType;
        this.mIsShot2Gallery = parallelTaskData.mIsShot2Gallery;
        this.mIsSaveToHiddenFolder = parallelTaskData.mIsSaveToHiddenFolder;
        this.mTimestamp = parallelTaskData.mTimestamp;
        this.mCaptureResult = parallelTaskData.mCaptureResult;
        this.mJpegImageData = parallelTaskData.mJpegImageData;
        this.mRawImageData = parallelTaskData.mRawImageData;
        this.mPortraitRawData = parallelTaskData.mPortraitRawData;
        this.mPortraitDepthData = parallelTaskData.mPortraitDepthData;
        this.mSavePath = parallelTaskData.mSavePath;
        this.mDataParameter = parallelTaskData.mDataParameter;
        this.isNeedThumbnail = parallelTaskData.isNeedThumbnail;
        this.mVideoPath = parallelTaskData.mVideoPath;
        this.mCoverFrameTimestamp = parallelTaskData.mCoverFrameTimestamp;
        this.isLiveShotTask = parallelTaskData.isLiveShotTask;
        this.isPictureFilled = parallelTaskData.isPictureFilled;
        this.mDataOfTheRegionUnderWatermarks = parallelTaskData.mDataOfTheRegionUnderWatermarks;
        this.mCoordinatesOfTheRegionUnderWatermarks = parallelTaskData.mCoordinatesOfTheRegionUnderWatermarks;
        this.mCameraId = parallelTaskData.mCameraId;
        this.mDateTakenTime = parallelTaskData.mDateTakenTime;
        this.mCaptureTime = parallelTaskData.mCaptureTime;
        this.mRequireTuningData = parallelTaskData.mRequireTuningData;
        this.mTuningImage = parallelTaskData.mTuningImage;
        this.noGaussian = parallelTaskData.noGaussian;
        this.mMemDebug = parallelTaskData.mMemDebug;
        this.mIsHdrSR = parallelTaskData.mIsHdrSR;
        this.mRaw2YuvProcessor = parallelTaskData.mRaw2YuvProcessor;
        this.mRaw2YuvDone = parallelTaskData.mRaw2YuvDone;
        this.mRawInputWidth = parallelTaskData.mRawInputWidth;
        this.mRawInputHeight = parallelTaskData.mRawInputHeight;
        this.mActiveRegion = parallelTaskData.mActiveRegion;
        this.mZoomRatio = parallelTaskData.mZoomRatio;
        this.mAddToProcessorCallback = parallelTaskData.mAddToProcessorCallback;
    }

    public void checkThread() {
    }

    public ParallelTaskData cloneTaskData(int i) {
        String str;
        ParallelTaskData parallelTaskData = new ParallelTaskData(this);
        String savePath = getSavePath();
        String str2 = GROUPSHOT_ORIGINAL_SUFFIX;
        if (i > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("_");
            sb.append(i);
            str2 = sb.toString();
        }
        int lastIndexOf = savePath.lastIndexOf(".");
        if (lastIndexOf > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(savePath.substring(0, lastIndexOf));
            sb2.append(str2);
            sb2.append(savePath.substring(lastIndexOf));
            str = sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(savePath);
            sb3.append(str2);
            str = sb3.toString();
        }
        String str3 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("[1] cloneTaskData: path=");
        sb4.append(str);
        Log.d(str3, sb4.toString());
        parallelTaskData.setSavePath(str);
        parallelTaskData.setNeedThumbnail(false);
        Builder builder = new Builder(getDataParameter());
        builder.setHasDualWaterMark(false);
        builder.setTimeWaterMarkString(null);
        builder.setSaveGroupshotPrimitive(false);
        parallelTaskData.fillParameter(builder.build());
        return parallelTaskData;
    }

    public synchronized void fillJpegData(byte[] bArr, int i) {
        checkThread();
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i == 3) {
                        if (this.mRawImageData == null) {
                            this.mRawImageData = bArr;
                        } else {
                            throw new RuntimeException("algo fillJpegData: raw already set");
                        }
                    }
                } else if (this.mPortraitDepthData == null) {
                    this.mPortraitDepthData = new byte[bArr.length];
                    System.arraycopy(bArr, 0, this.mPortraitDepthData, 0, bArr.length);
                } else {
                    throw new RuntimeException("algo fillJpegData: depth already set");
                }
            } else if (this.mPortraitRawData == null) {
                this.mPortraitRawData = bArr;
            } else {
                throw new RuntimeException("algo fillJpegData: portrait raw already set");
            }
        } else if (this.mJpegImageData == null) {
            this.isPictureFilled = true;
            this.mJpegImageData = bArr;
        } else {
            throw new RuntimeException("algo fillJpegData: jpeg already set");
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("fillJpegData: jpegData=");
        sb.append(bArr);
        sb.append("; imageType=");
        sb.append(i);
        Log.d(str, sb.toString());
    }

    public void fillParameter(ParallelTaskDataParameter parallelTaskDataParameter) {
        this.mDataParameter = parallelTaskDataParameter;
    }

    public synchronized void fillVideoPath(String str, long j) {
        boolean z;
        checkThread();
        if (this.mVideoPath == null) {
            this.mVideoPath = str;
            this.mCoverFrameTimestamp = j;
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("fillVideoPath: isVideoEmpty = ");
            if (str != null) {
                if (!VideoClipSavingCallback.EMPTY_VIDEO_PATH.equals(str)) {
                    z = false;
                    sb.append(z);
                    sb.append(", timestamp = ");
                    sb.append(j);
                    Log.d(str2, sb.toString());
                }
            }
            z = true;
            sb.append(z);
            sb.append(", timestamp = ");
            sb.append(j);
            Log.d(str2, sb.toString());
        } else {
            throw new IllegalStateException("fillVideoPath: micro video already set");
        }
    }

    public Rect getActiveRegion() {
        return this.mActiveRegion;
    }

    public int getAlgoType() {
        return this.mAlgoType;
    }

    public int getBurstNum() {
        return this.mBurstNum;
    }

    public int getCameraId() {
        return this.mCameraId;
    }

    public ICustomCaptureResult getCaptureResult() {
        return this.mCaptureResult;
    }

    public long getCaptureTime() {
        return this.mCaptureTime;
    }

    public int[] getCoordinatesOfTheRegionUnderWatermarks() {
        return this.mCoordinatesOfTheRegionUnderWatermarks;
    }

    public synchronized long getCoverFrameTimestamp() {
        return this.mCoverFrameTimestamp;
    }

    public int getCurrentModuleIndex() {
        return this.currentModuleIndex;
    }

    public byte[] getDataOfTheRegionUnderWatermarks() {
        return this.mDataOfTheRegionUnderWatermarks;
    }

    public ParallelTaskDataParameter getDataParameter() {
        return this.mDataParameter;
    }

    public long getDateTakenTime() {
        return this.mDateTakenTime;
    }

    public byte[] getJpegImageData() {
        return this.mJpegImageData;
    }

    public synchronized String getMicroVideoPath() {
        return this.mVideoPath;
    }

    public int getParallelType() {
        return this.mParallelType;
    }

    public byte[] getPortraitDepthData() {
        return this.mPortraitDepthData;
    }

    public byte[] getPortraitRawData() {
        return this.mPortraitRawData;
    }

    public int getPreviewThumbnailHash() {
        return this.previewThumbnailHash;
    }

    public ImageProcessor getRaw2YuvProcessor() {
        return this.mRaw2YuvProcessor;
    }

    public byte[] getRawImageData() {
        return this.mRawImageData;
    }

    public int getRawInputHeight() {
        return this.mRawInputHeight;
    }

    public int getRawInputWidth() {
        return this.mRawInputWidth;
    }

    public String getSavePath() {
        return this.mSavePath;
    }

    public ServiceStatusListener getServiceStatusListener() {
        return this.mServiceStatusListener;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public Image getTuningImage() {
        return this.mTuningImage;
    }

    public float getZoomRatio() {
        return this.mZoomRatio;
    }

    public boolean isAbandoned() {
        return this.isAbandoned;
    }

    public boolean isAdaptiveSnapshotSize() {
        return this.isAdaptiveSnapshotSize;
    }

    public boolean isDataFilled(int i) {
        boolean z;
        boolean z2;
        boolean z3 = false;
        if (i == 0) {
            if (this.mJpegImageData != null) {
                z = true;
            }
            return z;
        } else if (i == 1) {
            if (this.mPortraitRawData != null) {
                z2 = true;
            }
            return z2;
        } else if (i == 2) {
            if (this.mPortraitDepthData != null) {
                z3 = true;
            }
            return z3;
        } else if (i != 3) {
            return false;
        } else {
            if (this.mRawImageData != null) {
                z3 = true;
            }
            return z3;
        }
    }

    public boolean isHWMFNRProcessing() {
        return this.isHWMFNRProcessing;
    }

    public boolean isHdrSR() {
        return this.mIsHdrSR;
    }

    public boolean isInTimerBurstShotting() {
        return this.mInTimerBurstShotting;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0026, code lost:
        r3 = TAG;
        r4 = new java.lang.StringBuilder();
        r4.append("isJpegDataReady: object = ");
        r4.append(r7);
        r4.append("; mParallelType = ");
        r4.append(r7.mParallelType);
        r4.append("; mJpegImageData = ");
        r4.append(r7.mJpegImageData);
        r4.append("; mRawImageData = ");
        r4.append(r7.mRawImageData);
        r4.append("; mPortraitRawData = ");
        r4.append(r7.mPortraitRawData);
        r4.append("; mPortraitDepthData = ");
        r4.append(r7.mPortraitDepthData);
        r4.append("; isVideoEmpty = ");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006e, code lost:
        if (r7.mVideoPath == null) goto L_0x007c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0078, code lost:
        if (com.xiaomi.camera.liveshot.CircularMediaRecorder.VideoClipSavingCallback.EMPTY_VIDEO_PATH.equals(r7.mVideoPath) == false) goto L_0x007b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x007b, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007c, code lost:
        r4.append(r1);
        r4.append("; result = ");
        r4.append(r0);
        com.android.camera.log.Log.d(r3, r4.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0008, code lost:
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0010, code lost:
        if (r7.mRawImageData != null) goto L_0x0012;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        r0 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean isJpegDataReady() {
        boolean z;
        boolean z2 = true;
        switch (this.mParallelType) {
            case -7:
            case -3:
            case 2:
            case 6:
            case 7:
            case 11:
                if (!(this.mJpegImageData == null || this.mPortraitRawData == null || this.mPortraitDepthData == null)) {
                    break;
                }
            case -6:
            case -5:
            case -2:
            case -1:
            case 0:
            case 5:
            case 8:
            case 9:
            case 10:
                if (this.mJpegImageData != null) {
                    break;
                }
                break;
            case 1:
                if (this.mJpegImageData != null) {
                    break;
                }
                break;
        }
        return z;
    }

    public synchronized boolean isLiveShotTask() {
        return this.isLiveShotTask;
    }

    public boolean isMemDebug() {
        return this.mMemDebug;
    }

    public boolean isNeedThumbnail() {
        return this.isNeedThumbnail;
    }

    public boolean isParallelVTCameraSnapshot() {
        return this.mIsParallelVTCameraSnapshot;
    }

    public synchronized boolean isPictureFilled() {
        return this.isPictureFilled;
    }

    public boolean isRaw2YuvDone() {
        return this.mRaw2YuvDone;
    }

    public boolean isRequireTuningData() {
        return this.mRequireTuningData;
    }

    public boolean isSatFusionShot() {
        return this.mIsSatFusionShot;
    }

    public boolean isSaveToHiddenFolder() {
        return this.mIsSaveToHiddenFolder;
    }

    public boolean isShot2Gallery() {
        return this.mIsShot2Gallery;
    }

    public boolean noGaussian() {
        return this.noGaussian;
    }

    public void refillJpegData(byte[] bArr) {
        this.mJpegImageData = bArr;
        this.isPictureFilled = true;
    }

    public void releaseImageData() {
        this.mVideoPath = null;
        this.mJpegImageData = null;
        this.mRawImageData = null;
        this.mPortraitRawData = null;
        this.mPortraitDepthData = null;
        this.isPictureFilled = false;
        this.mDataOfTheRegionUnderWatermarks = null;
        this.mCoordinatesOfTheRegionUnderWatermarks = null;
        this.mRequireTuningData = false;
        this.mTuningImage = null;
        this.mIsSatFusionShot = false;
    }

    public void setAbandoned(boolean z) {
        this.isAbandoned = z;
    }

    public void setActiveRegion(Rect rect) {
        this.mActiveRegion = rect;
    }

    public void setAdaptiveSnapshotSize(boolean z) {
        this.isAdaptiveSnapshotSize = z;
    }

    public void setAddToProcessorListener(OnParallelTaskDataAddToProcessorListener onParallelTaskDataAddToProcessorListener) {
        this.mAddToProcessorCallback = onParallelTaskDataAddToProcessorListener;
    }

    public void setAlgoType(int i) {
        this.mAlgoType = i;
    }

    public void setBurstNum(int i) {
        this.mBurstNum = i;
    }

    public void setCaptureResult(ICustomCaptureResult iCustomCaptureResult) {
        this.mCaptureResult = iCustomCaptureResult;
    }

    public void setCoordinatesOfTheRegionUnderWatermarks(int[] iArr) {
        this.mCoordinatesOfTheRegionUnderWatermarks = iArr;
    }

    public void setCurrentModuleIndex(int i) {
        this.currentModuleIndex = i;
    }

    public void setDataOfTheRegionUnderWatermarks(byte[] bArr) {
        this.mDataOfTheRegionUnderWatermarks = bArr;
    }

    public void setHWMFNRProcessing(boolean z) {
        this.isHWMFNRProcessing = z;
    }

    public void setHdrSR(boolean z) {
        this.mIsHdrSR = z;
    }

    public boolean setInTimerBurstShotting(boolean z) {
        if (this.mInTimerBurstShotting == z) {
            return false;
        }
        this.mInTimerBurstShotting = z;
        return true;
    }

    public void setIsSatFusionShot(boolean z) {
        this.mIsSatFusionShot = z;
    }

    public synchronized void setLiveShotTask(boolean z) {
        this.isLiveShotTask = z;
    }

    public void setMemDebug(boolean z) {
        this.mMemDebug = z;
    }

    public void setNeedThumbnail(boolean z) {
        this.isNeedThumbnail = z;
    }

    public void setNoGaussian(boolean z) {
        this.noGaussian = z;
    }

    public void setParallelVTCameraSnapshot(boolean z) {
        this.mIsParallelVTCameraSnapshot = z;
    }

    public synchronized void setPictureFilled(boolean z) {
        this.isPictureFilled = z;
    }

    public void setPreviewThumbnailHash(int i) {
        this.previewThumbnailHash = i;
    }

    public void setRaw2YuvDone(boolean z) {
        this.mRaw2YuvDone = z;
    }

    public void setRaw2YuvProcessor(ImageProcessor imageProcessor) {
        this.mRaw2YuvProcessor = imageProcessor;
    }

    public void setRawInputSize(int i, int i2) {
        this.mRawInputWidth = i;
        this.mRawInputHeight = i2;
    }

    public void setRequireTuningData(boolean z) {
        this.mRequireTuningData = z;
    }

    public void setSavePath(String str) {
        this.mSavePath = str;
    }

    public void setSaveToHiddenFolder(boolean z) {
        this.mIsSaveToHiddenFolder = z;
    }

    public void setServiceStatusListener(ServiceStatusListener serviceStatusListener) {
        this.mServiceStatusListener = serviceStatusListener;
    }

    public boolean setShot2Gallery(boolean z) {
        if (this.mIsShot2Gallery == z) {
            return false;
        }
        this.mIsShot2Gallery = z;
        return true;
    }

    public void setTimestamp(long j) {
        this.mTimestamp = j;
    }

    public void setTuningImage(Image image) {
        this.mTuningImage = image;
    }

    public void setZoomRatio(float f) {
        this.mZoomRatio = f;
    }
}
