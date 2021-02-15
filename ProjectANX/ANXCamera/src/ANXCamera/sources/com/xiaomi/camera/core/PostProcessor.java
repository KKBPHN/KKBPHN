package com.xiaomi.camera.core;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.view.Surface;
import androidx.annotation.NonNull;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.JpegUtil;
import com.android.camera.LocalParallelService;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.ParallelSnapshotManager;
import com.android.camera.storage.HeifSaveRequest.SaveHeifCallback;
import com.android.camera.storage.ImageSaver;
import com.android.camera.storage.Storage;
import com.android.camera2.SuperNightReprocessHandler;
import com.xiaomi.camera.base.ImageUtil;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.CaptureData.CaptureDataBean;
import com.xiaomi.camera.core.ImageProcessor.FilterTaskData;
import com.xiaomi.camera.core.ImageProcessor.ImageProcessorStatusCallback;
import com.xiaomi.camera.core.ParallelDataZipper.DataListener;
import com.xiaomi.camera.core.ParallelTaskData.OnParallelTaskDataAddToProcessorListener;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.camera.imagecodec.ImagePool.ImageFormat;
import com.xiaomi.camera.imagecodec.ImageReaderHelper;
import com.xiaomi.camera.imagecodec.ImageReaderHelper.ImageReaderType;
import com.xiaomi.camera.imagecodec.ReprocessData;
import com.xiaomi.camera.imagecodec.ReprocessData.DataStatusCallback;
import com.xiaomi.camera.imagecodec.ReprocessData.OnDataAvailableListener;
import com.xiaomi.engine.BufferFormat;
import com.xiaomi.engine.MiCameraAlgo;
import com.xiaomi.engine.ResultData;
import com.xiaomi.engine.TaskSession;
import com.xiaomi.engine.TaskSession.SessionStatusCallback;
import com.xiaomi.protocol.ICustomCaptureResult;
import com.xiaomi.protocol.IImageReaderParameterSets;
import com.xiaomi.protocol.ISessionStatusCallBackListener;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import miui.os.Build;

public class PostProcessor {
    private static final int MSG_CROP_REGION = 1;
    private static final int MSG_RESULT_FLAW = 0;
    public static final boolean SKIP_IMAGEPROCESSOR = SystemProperties.getBoolean(SKIP_IMAGEPROCESS_PROP, false);
    public static final String SKIP_IMAGEPROCESS_PROP = "camera.skip.imageprocessor";
    public static final boolean SKIP_MULTI = SystemProperties.getBoolean(SKIP_MULTI_PROP, false);
    public static final String SKIP_MULTI_PROP = "camera.skip.multi";
    public static final boolean SKIP_RENDER = SystemProperties.getBoolean(SKIP_RENDER_PROP, false);
    public static final String SKIP_RENDER_PROP = "camera.skip.render";
    /* access modifiers changed from: private */
    public static final String TAG = "PostProcessor";
    private BoostFrameworkImpl mBoostFramework;
    /* access modifiers changed from: private */
    public CaptureDataListener mCaptureDataListener = new CaptureDataListener() {
        public void onCaptureDataAvailable(@NonNull CaptureData captureData) {
            long captureTimestamp = captureData.getCaptureTimestamp();
            int algoType = captureData.getAlgoType();
            boolean z = false;
            if (2 == algoType || ((3 == algoType && PostProcessor.this.isSRRequireReprocess()) || 5 == algoType)) {
                CaptureDataBean multiFrameProcessResult = captureData.getMultiFrameProcessResult();
                if (multiFrameProcessResult != null) {
                    ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(captureTimestamp));
                    long timeStamp = multiFrameProcessResult.getResult().getTimeStamp();
                    String access$100 = PostProcessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("[1] onCaptureDataAvailable: timestamp: ");
                    sb.append(captureTimestamp);
                    sb.append(" | ");
                    sb.append(timeStamp);
                    Log.d(access$100, sb.toString());
                    if (timeStamp != captureTimestamp) {
                        parallelTaskData.setTimestamp(timeStamp);
                        PostProcessor.this.mParallelTaskHashMap.remove(Long.valueOf(captureTimestamp));
                        PostProcessor.this.mParallelTaskHashMap.put(Long.valueOf(timeStamp), parallelTaskData);
                    }
                    if (5 == algoType && parallelTaskData.getDataParameter().isSaveGroupshotPrimitive()) {
                        for (int i = 0; i < captureData.getCaptureDataBeanList().size(); i++) {
                            ParallelTaskData cloneTaskData = parallelTaskData.cloneTaskData(i);
                            long timeStamp2 = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(i)).getResult().getTimeStamp();
                            if (timeStamp2 == timeStamp) {
                                timeStamp2++;
                            }
                            cloneTaskData.setTimestamp(timeStamp2);
                            PostProcessor.this.mParallelTaskHashMap.put(Long.valueOf(timeStamp2), cloneTaskData);
                            String access$1002 = PostProcessor.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("[1] onCaptureDataAvailable: add ");
                            sb2.append(timeStamp2);
                            Log.d(access$1002, sb2.toString());
                        }
                    }
                    if (3 != algoType || !PostProcessor.this.isSRRequireReprocess() || !captureData.isHdrSR() || captureData.getHDRSRResult() == null) {
                        captureData.getCaptureDataBeanList().add(multiFrameProcessResult);
                    } else {
                        captureData.getCaptureDataBeanList().addAll(captureData.getHDRSRResult());
                        captureData.getHDRSRResult().clear();
                    }
                } else {
                    throw new RuntimeException("No multi-frame process result!");
                }
            }
            if (4 == algoType) {
                if (Build.IS_DEBUGGABLE) {
                    Log.d(PostProcessor.TAG, "onCaptureDataAvailable: start process multi-shot image...");
                }
                CaptureDataBean captureDataBean = (CaptureDataBean) captureData.getCaptureDataBeanList().get(0);
                ICustomCaptureResult result = captureDataBean.getResult();
                Image mainImage = captureDataBean.getMainImage();
                ParallelTaskData parallelTaskData2 = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(captureTimestamp));
                if (parallelTaskData2 != null) {
                    parallelTaskData2.setCaptureResult(result);
                    if (parallelTaskData2.getDataParameter().shouldReprocessBurstShotPicture()) {
                        captureData.getImageProcessor().dispatchTask(captureData.getCaptureDataBeanList());
                    } else {
                        PostProcessor.this.mImageProcessorStatusCb.onImageProcessStart(mainImage.getTimestamp());
                        PostProcessor.this.mImageProcessorStatusCb.onImageProcessed(mainImage, 0, false);
                    }
                } else {
                    String access$1003 = PostProcessor.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("[1] onCaptureDataAvailable: no captureResult ");
                    sb3.append(captureTimestamp);
                    Log.e(access$1003, sb3.toString());
                }
                mainImage.close();
                onOriginalImageClosed(mainImage);
            } else {
                List<CaptureDataBean> captureDataBeanList = captureData.getCaptureDataBeanList();
                if (captureDataBeanList != null && !captureDataBeanList.isEmpty()) {
                    if (CameraSettings.isHighQualityPreferred() && C0122O00000o.instance().OOo00OO() && captureData.isSatFusionShot() && captureData.getAlgoType() == 3) {
                        PostProcessor.dumpFusionInputs(captureData);
                    }
                    Iterator it = captureDataBeanList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            CaptureData captureData2 = captureData;
                            break;
                        }
                        CaptureDataBean captureDataBean2 = (CaptureDataBean) it.next();
                        if (captureDataBean2.isFirstResult()) {
                            long timeStamp3 = captureDataBean2.getResult().getTimeStamp();
                            ParallelTaskData parallelTaskData3 = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(timeStamp3));
                            if (parallelTaskData3 != null) {
                                parallelTaskData3.setCaptureResult(PostProcessor.this.chooseCaptureResult(captureData, algoType));
                            } else {
                                CaptureData captureData3 = captureData;
                                String access$1004 = PostProcessor.TAG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("[1] onCaptureDataAvailable: no task data with timestamp ");
                                sb4.append(timeStamp3);
                                Log.e(access$1004, sb4.toString(), (Throwable) new RuntimeException());
                                z = true;
                            }
                        } else {
                            CaptureData captureData4 = captureData;
                        }
                    }
                    if (z) {
                        for (CaptureDataBean captureDataBean3 : captureDataBeanList) {
                            Image mainImage2 = captureDataBean3.getMainImage();
                            mainImage2.close();
                            onOriginalImageClosed(mainImage2);
                            ImagePool.getInstance().releaseImage(mainImage2);
                            Image subImage = captureDataBean3.getSubImage();
                            if (subImage != null) {
                                subImage.close();
                                onOriginalImageClosed(subImage);
                                ImagePool.getInstance().releaseImage(subImage);
                            }
                            Image tuningImage = captureDataBean3.getTuningImage();
                            if (tuningImage != null) {
                                tuningImage.close();
                                onOriginalImageClosed(tuningImage);
                                ImagePool.getInstance().releaseImage(tuningImage);
                            }
                        }
                        return;
                    }
                    ImageProcessor imageProcessor = captureData.getImageProcessor();
                    if (imageProcessor != PostProcessor.this.mImageProcessor) {
                        Log.w(PostProcessor.TAG, "[1] onCaptureDataAvailable: image processor switched");
                    }
                    imageProcessor.dispatchTask(captureDataBeanList);
                } else if (!Build.IS_DEBUGGABLE) {
                    Log.e(PostProcessor.TAG, "[1] onCaptureDataAvailable: There are no result to process!");
                } else {
                    throw new RuntimeException("There are no result to process!");
                }
            }
        }

        public void onOriginalImageClosed(Image image) {
            if (PostProcessor.this.mImageMemoryManager != null && image != null) {
                String access$100 = PostProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onOriginalImageClosed: ");
                sb.append(image);
                Log.d(access$100, sb.toString());
                PostProcessor.this.mImageMemoryManager.releaseAnImage(image);
            }
        }
    };
    private CaptureStatusListener mCaptureStatusListener = new CaptureStatusListener();
    /* access modifiers changed from: private */
    public SaveHeifCallback mHeifSaverCallback;
    /* access modifiers changed from: private */
    public ImageMemoryManager mImageMemoryManager;
    /* access modifiers changed from: private */
    public ImageProcessor mImageProcessor;
    private List mImageProcessorList = new ArrayList();
    /* access modifiers changed from: private */
    public ImageProcessorStatusCallback mImageProcessorStatusCb = new ImageProcessorStatusCallback() {
        public ParallelTaskData getParallelTaskData(long j) {
            return (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(j));
        }

        public boolean isAnyFrontProcessing(ImageProcessor imageProcessor) {
            return ParallelDataZipper.getInstance().isAnyFrontProcessingByProcessor(imageProcessor);
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=android.media.Image, code=java.lang.Object, for r3v0, types: [android.media.Image, java.lang.Object] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onImageProcessFailed(Object obj, String str) {
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onImageProcessFailed: image=");
            if (obj == null) {
                obj = "null";
            }
            sb.append(obj);
            sb.append(" reason=");
            sb.append(str);
            Log.d(access$100, sb.toString());
        }

        public void onImageProcessStart(long j) {
            if (PostProcessor.this.mPostProcessStatusCallback != null) {
                String access$100 = PostProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onImageProcessStart: get parallelTaskData: ");
                sb.append(j);
                Log.k(3, access$100, sb.toString());
                ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(j));
                if (!parallelTaskData.isRaw2YuvDone()) {
                    PostProcessor.this.mPostProcessStatusCallback.onImagePostProcessStart(parallelTaskData);
                }
            }
        }

        public void onImageProcessed(Image image, int i, boolean z) {
            Image image2 = image;
            int i2 = i;
            long timestamp = image.getTimestamp();
            StringBuilder sb = new StringBuilder();
            sb.append(timestamp);
            sb.append(File.separator);
            sb.append(i2);
            String sb2 = sb.toString();
            String access$100 = PostProcessor.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("[2] onImageProcessed: ");
            sb3.append(image2);
            sb3.append(" | ");
            sb3.append(sb2);
            Log.k(3, access$100, sb3.toString());
            if (i2 == 2) {
                PerformanceTracker.trackJpegReprocess(i2, 0);
                PostProcessor.this.mJpegEncoderListener.onJpegAvailable(ImageUtil.getFirstPlane(image), sb2);
            } else {
                ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(timestamp));
                if (parallelTaskData != null) {
                    ICustomCaptureResult captureResult = parallelTaskData.getCaptureResult();
                    String access$1002 = PostProcessor.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("[2] onImageProcessed: captureResult = ");
                    sb4.append(captureResult.getResults());
                    Log.d(access$1002, sb4.toString());
                    if (!(1212500294 == parallelTaskData.getDataParameter().getOutputFormat()) || !C0124O00000oO.isMTKPlatform()) {
                        int algoType = parallelTaskData.getAlgoType();
                        if (algoType == 12) {
                            ReprocessData access$1700 = PostProcessor.this.generateReprocessData(parallelTaskData, image, sb2, ReprocessData.REPROCESS_FUNCTION_RAW_SUPERNIGHT, z);
                            if (PostProcessor.this.mWaitingReprocessDatas == null) {
                                PostProcessor.this.mWaitingReprocessDatas = new HashMap(4);
                            }
                            PostProcessor.this.mWaitingReprocessDatas.put(Long.valueOf(timestamp), access$1700);
                            if (PostProcessor.this.mWaitingCropRegions == null) {
                                PostProcessor.this.mWaitingCropRegions = new HashMap(4);
                            }
                            if (PostProcessor.this.mWaitingCropRegions.containsKey(Long.valueOf(timestamp))) {
                                Log.d(PostProcessor.TAG, "both reprocessData dan cropRegion ready");
                                Rect rect = (Rect) PostProcessor.this.mWaitingCropRegions.get(Long.valueOf(timestamp));
                                SuperNightReprocessHandler.convert(rect, parallelTaskData.getRawInputWidth(), parallelTaskData.getRawInputHeight(), parallelTaskData.getActiveRegion(), parallelTaskData.getZoomRatio());
                                int i3 = rect.left;
                                int i4 = rect.top;
                                access$1700.setCropRegion(new int[]{i3, i4, rect.right - i3, rect.bottom - i4});
                                try {
                                    LocalParallelService.getReprocessor().submit(access$1700);
                                } catch (Exception e) {
                                    PostProcessor.this.mJpegEncoderListener.onError(e.getMessage(), sb2);
                                } catch (Throwable th) {
                                    PostProcessor.this.mWaitingCropRegions.remove(Long.valueOf(timestamp));
                                    PostProcessor.this.mWaitingReprocessDatas.remove(Long.valueOf(timestamp));
                                    throw th;
                                }
                                PostProcessor.this.mWaitingCropRegions.remove(Long.valueOf(timestamp));
                                PostProcessor.this.mWaitingReprocessDatas.remove(Long.valueOf(timestamp));
                            } else {
                                Log.d(PostProcessor.TAG, "crop region not ready, should wait");
                            }
                        } else if (algoType == 13) {
                            Log.d(PostProcessor.TAG, "raw algo 2nd, try to encode jpeg");
                            if (!z) {
                                image2 = ImageUtil.queueImageToPool(ImagePool.getInstance(), image2, 1);
                            }
                            PostProcessor.this.doEncodeJpeg(timestamp, image2);
                        } else {
                            ReprocessData access$17002 = PostProcessor.this.generateReprocessData(parallelTaskData, image, sb2, ReprocessData.REPROCESS_FUNCTION_NONE, z);
                            PerformanceTracker.trackJpegReprocess(i2, 0);
                            try {
                                LocalParallelService.getReprocessor().submit(access$17002);
                            } catch (Exception e2) {
                                PostProcessor.this.mJpegEncoderListener.onError(e2.getMessage(), sb2);
                            }
                        }
                    } else {
                        if (!z) {
                            image2 = ImageUtil.queueImageToPool(ImagePool.getInstance(), image2, 1);
                        }
                        PostProcessor.this.mImageSaver.addHeif(image2, ICustomCaptureResult.toTotalCaptureResult(captureResult, -1), parallelTaskData, PostProcessor.this.mHeifSaverCallback);
                    }
                } else {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("no parallelTaskData with timestamp ");
                    sb5.append(timestamp);
                    throw new RuntimeException(sb5.toString());
                }
            }
        }

        public void onMetadataReceived(int i, Object obj) {
            if (i == 1) {
                ResultData resultData = (ResultData) obj;
                int[] cropRegion = resultData.getCropRegion();
                long timeStamp = resultData.getTimeStamp();
                ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(timeStamp));
                if (parallelTaskData != null) {
                    if (cropRegion == null || cropRegion.length != 4 || timeStamp <= 0) {
                        Log.e(PostProcessor.TAG, "error get crop region");
                    } else {
                        Log.d(PostProcessor.TAG, String.format(Locale.ENGLISH, "crop region is [ %d,%d,%d,%d]", new Object[]{Integer.valueOf(cropRegion[0]), Integer.valueOf(cropRegion[1]), Integer.valueOf(cropRegion[2]), Integer.valueOf(cropRegion[3])}));
                    }
                    if (PostProcessor.this.mWaitingCropRegions == null) {
                        PostProcessor.this.mWaitingCropRegions = new HashMap(4);
                    }
                    PostProcessor.this.mWaitingCropRegions.put(Long.valueOf(timeStamp), new Rect(cropRegion[0], cropRegion[1], cropRegion[2], cropRegion[3]));
                    if (PostProcessor.this.mWaitingReprocessDatas == null) {
                        PostProcessor.this.mWaitingReprocessDatas = new HashMap(4);
                    }
                    if (PostProcessor.this.mWaitingReprocessDatas.containsKey(Long.valueOf(timeStamp))) {
                        ReprocessData reprocessData = (ReprocessData) PostProcessor.this.mWaitingReprocessDatas.get(Long.valueOf(timeStamp));
                        Log.d(PostProcessor.TAG, "both reprocessData dan cropRegion ready");
                        Rect rect = (Rect) PostProcessor.this.mWaitingCropRegions.get(Long.valueOf(timeStamp));
                        SuperNightReprocessHandler.convert(rect, parallelTaskData.getRawInputWidth(), parallelTaskData.getRawInputHeight(), parallelTaskData.getActiveRegion(), parallelTaskData.getZoomRatio());
                        int i2 = rect.left;
                        int i3 = rect.top;
                        reprocessData.setCropRegion(new int[]{i2, i3, rect.right - i2, rect.bottom - i3});
                        try {
                            LocalParallelService.getReprocessor().submit(reprocessData);
                        } catch (Exception e) {
                            OnDataAvailableListener access$1400 = PostProcessor.this.mJpegEncoderListener;
                            String message = e.getMessage();
                            StringBuilder sb = new StringBuilder();
                            sb.append("could not reprocess timeStamp ");
                            sb.append(timeStamp);
                            access$1400.onError(message, sb.toString());
                        } catch (Throwable th) {
                            PostProcessor.this.mWaitingReprocessDatas.remove(Long.valueOf(timeStamp));
                            PostProcessor.this.mWaitingCropRegions.remove(Long.valueOf(timeStamp));
                            throw th;
                        }
                        PostProcessor.this.mWaitingReprocessDatas.remove(Long.valueOf(timeStamp));
                        PostProcessor.this.mWaitingCropRegions.remove(Long.valueOf(timeStamp));
                        return;
                    }
                    Log.d(PostProcessor.TAG, "reprocessData not ready, should wait");
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("no parallelTaskData with timestamp ");
                sb2.append(timeStamp);
                throw new RuntimeException(sb2.toString());
            }
        }

        public void onOriginalImageClosed(Image image) {
            PostProcessor.this.mCaptureDataListener.onOriginalImageClosed(image);
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onOriginalImageClosed: ");
            sb.append(image);
            Log.d(access$100, sb.toString());
        }
    };
    private List mImageReaderList = new ArrayList();
    /* access modifiers changed from: private */
    public ImageSaver mImageSaver;
    /* access modifiers changed from: private */
    public OnDataAvailableListener mJpegEncoderListener = new OnDataAvailableListener() {
        private void startRaw2YuvBottomHalf(ParallelTaskData parallelTaskData, long j) {
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("startRaw2YuvBottomHalf: E. timestamp = ");
            sb.append(j);
            Log.d(access$100, sb.toString());
            parallelTaskData.setRaw2YuvDone(true);
            CaptureData captureData = new CaptureData(0, 1, 1, j, false, parallelTaskData.getRaw2YuvProcessor());
            captureData.setDataListener(PostProcessor.this.mZipperResultListener);
            captureData.setRequireTuningData(true);
            ParallelDataZipper.getInstance().startTask(captureData);
            ParallelDataZipper.getInstance().join(parallelTaskData.getCaptureResult(), true);
            Log.d(PostProcessor.TAG, "startRaw2YuvBottomHalf: X");
        }

        public void onError(String str, String str2) {
            String[] split = str2.split(File.separator);
            long parseLong = Long.parseLong(split[0]);
            int parseInt = Integer.parseInt(split[1]);
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[3] onError: ");
            sb.append(parseLong);
            String str3 = " | ";
            sb.append(str3);
            sb.append(parseInt);
            sb.append(str3);
            sb.append(str);
            Log.e(access$100, sb.toString());
            PerformanceTracker.trackJpegReprocess(parseInt, 1);
            ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(parseLong));
            if (parallelTaskData != null) {
                PostProcessor.this.closePoolImage(parallelTaskData.getTuningImage());
                parallelTaskData.releaseImageData();
                PostProcessor.this.mParallelTaskHashMap.remove(Long.valueOf(parseLong));
                String access$1002 = PostProcessor.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("[3] onError: remove task ");
                sb2.append(parseLong);
                sb2.append(str3);
                sb2.append(parseInt);
                Log.e(access$1002, sb2.toString());
            }
            PostProcessor.this.tryToCloseSession();
        }

        public void onJpegAvailable(byte[] bArr, String str) {
            String access$100;
            String sb;
            String[] split = str.split(File.separator);
            long parseLong = Long.parseLong(split[0]);
            int parseInt = Integer.parseInt(split[1]);
            String access$1002 = PostProcessor.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[3] onJpegAvailable: ");
            sb2.append(parseLong);
            sb2.append(" | ");
            sb2.append(parseInt);
            Log.d(access$1002, sb2.toString());
            PerformanceTracker.trackJpegReprocess(parseInt, 1);
            ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(parseLong));
            if (parallelTaskData != null) {
                parallelTaskData.fillJpegData(bArr, parseInt);
                if (parallelTaskData.isJpegDataReady() || PostProcessor.SKIP_IMAGEPROCESSOR) {
                    parallelTaskData.setMemDebug(PostProcessor.SKIP_IMAGEPROCESSOR);
                    TotalCaptureResult totalCaptureResult = ICustomCaptureResult.toTotalCaptureResult(parallelTaskData.getCaptureResult(), -1);
                    String access$1003 = PostProcessor.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("[3] onJpegAvailable: save image start. dataLen=");
                    sb3.append(bArr.length);
                    Log.d(access$1003, sb3.toString());
                    boolean onParallelProcessFinish = PostProcessor.this.mImageSaver.onParallelProcessFinish(parallelTaskData, totalCaptureResult, null);
                    PostProcessor.this.closePoolImage(parallelTaskData.getTuningImage());
                    if (onParallelProcessFinish) {
                        parallelTaskData.releaseImageData();
                    }
                    if (PostProcessor.this.isNeedCallBackFinished(parallelTaskData) && PostProcessor.this.mPostProcessStatusCallback != null) {
                        PostProcessor.this.mPostProcessStatusCallback.onImagePostProcessEnd(parallelTaskData);
                    }
                    PostProcessor.this.mParallelTaskHashMap.remove(Long.valueOf(parseLong));
                    access$100 = PostProcessor.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("[3] onJpegAvailable: parallelTaskHashMap remove ");
                    sb4.append(parseLong);
                    sb = sb4.toString();
                } else {
                    access$100 = PostProcessor.TAG;
                    sb = "[3] onJpegAvailable: jpeg data isn't ready, save action has been ignored.";
                }
                Log.d(access$100, sb);
            } else {
                String access$1004 = PostProcessor.TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("[3] onJpegAvailable: null task data. timestamp=");
                sb5.append(parseLong);
                Log.w(access$1004, sb5.toString());
            }
            ImagePool.getInstance().trimPoolBuffer();
            PostProcessor.this.tryToStopBoost();
            PostProcessor.this.tryToCloseSession();
        }

        public void onJpegImageAvailable(Image image, String str, boolean z) {
            String access$100;
            String sb;
            String[] split = str.split(File.separator);
            long parseLong = Long.parseLong(split[0]);
            int parseInt = Integer.parseInt(split[1]);
            String access$1002 = PostProcessor.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[3] onJpegImageAvailable: ");
            sb2.append(parseLong);
            sb2.append(" | ");
            sb2.append(parseInt);
            Log.k(3, access$1002, sb2.toString());
            PerformanceTracker.trackJpegReprocess(parseInt, 1);
            ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(parseLong));
            if (parallelTaskData != null) {
                Plane[] planesExtra = JpegUtil.getPlanesExtra(image);
                byte[] jpegData = JpegUtil.getJpegData(planesExtra, 0);
                if (jpegData == null) {
                    jpegData = Util.getFirstPlane(image);
                }
                parallelTaskData.fillJpegData(jpegData, parseInt);
                byte[] jpegData2 = JpegUtil.getJpegData(planesExtra, 1);
                if (jpegData2 != null) {
                    parallelTaskData.setDataOfTheRegionUnderWatermarks(jpegData2);
                    ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
                    int[] vendorWatermarkRange = Util.getVendorWatermarkRange(dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), dataParameter.getJpegRotation());
                    String access$1003 = PostProcessor.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("onJpegImageAvailable: rotation = ");
                    sb3.append(dataParameter.getJpegRotation());
                    sb3.append(", watermarkRange = ");
                    sb3.append(Arrays.toString(vendorWatermarkRange));
                    Log.d(access$1003, sb3.toString());
                    parallelTaskData.setCoordinatesOfTheRegionUnderWatermarks(vendorWatermarkRange);
                    if (Util.WATER_MARK_DUMP) {
                        String savePath = parallelTaskData.getSavePath();
                        String substring = savePath.substring(savePath.lastIndexOf(File.separator));
                        String access$1004 = PostProcessor.TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("dump_water_mark watermarkRange: path = ");
                        sb4.append(savePath);
                        sb4.append(", name = ");
                        sb4.append(substring);
                        sb4.append(", rect = ");
                        sb4.append(Arrays.toString(vendorWatermarkRange));
                        Log.d(access$1004, sb4.toString());
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("_");
                        sb5.append(Arrays.toString(vendorWatermarkRange));
                        String str2 = Storage.JPEG_SUFFIX;
                        sb5.append(str2);
                        String replace = substring.replace(str2, sb5.toString());
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(CameraAppImpl.getAndroidContext().getExternalCacheDir().getAbsolutePath());
                        sb6.append(replace);
                        Util.saveBlobToFile(jpegData2, sb6.toString());
                    }
                }
                if (parallelTaskData.isJpegDataReady() || PostProcessor.SKIP_IMAGEPROCESSOR) {
                    parallelTaskData.setMemDebug(PostProcessor.SKIP_IMAGEPROCESSOR);
                    TotalCaptureResult totalCaptureResult = ICustomCaptureResult.toTotalCaptureResult(parallelTaskData.getCaptureResult(), -1);
                    String access$1005 = PostProcessor.TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("[3] onJpegImageAvailable: save image start. dataLen=");
                    sb7.append(jpegData.length);
                    Log.d(access$1005, sb7.toString());
                    boolean onParallelProcessFinish = PostProcessor.this.mImageSaver.onParallelProcessFinish(parallelTaskData, totalCaptureResult, null);
                    PostProcessor.this.closePoolImage(parallelTaskData.getTuningImage());
                    if (onParallelProcessFinish) {
                        parallelTaskData.releaseImageData();
                    }
                    if (PostProcessor.this.isNeedCallBackFinished(parallelTaskData) && PostProcessor.this.mPostProcessStatusCallback != null) {
                        PostProcessor.this.mPostProcessStatusCallback.onImagePostProcessEnd(parallelTaskData);
                    }
                    PostProcessor.this.mParallelTaskHashMap.remove(Long.valueOf(parseLong));
                    access$100 = PostProcessor.TAG;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("[3] onJpegImageAvailable: parallelTaskHashMap remove ");
                    sb8.append(parseLong);
                    sb = sb8.toString();
                } else {
                    access$100 = PostProcessor.TAG;
                    sb = "[3] onJpegImageAvailable: jpeg data isn't ready, save action has been ignored.";
                }
                Log.d(access$100, sb);
            } else {
                String access$1006 = PostProcessor.TAG;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("[3] onJpegImageAvailable: null task data. timestamp=");
                sb9.append(parseLong);
                Log.w(access$1006, sb9.toString());
            }
            ImagePool.getInstance().trimPoolBuffer();
            PostProcessor.this.tryToStopBoost();
            PostProcessor.this.tryToCloseSession();
        }

        public void onTuningImageAvailable(Image image, String str, boolean z) {
            String[] split = str.split(File.separator);
            long parseLong = Long.parseLong(split[0]);
            int parseInt = Integer.parseInt(split[1]);
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[z] onTuningAvailable: ");
            sb.append(parseLong);
            String str2 = " | ";
            sb.append(str2);
            sb.append(parseInt);
            sb.append(str2);
            sb.append(image.getTimestamp());
            Log.d(access$100, sb.toString());
            ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(parseLong));
            if (parallelTaskData != null) {
                if (parseLong != image.getTimestamp()) {
                    image.setTimestamp(parseLong);
                }
                if (parallelTaskData.getAlgoType() != 12) {
                    if (!parallelTaskData.isRaw2YuvDone()) {
                        startRaw2YuvBottomHalf(parallelTaskData, parseLong);
                    }
                    ParallelDataZipper.getInstance().join(image, 2, 1, z);
                    return;
                }
                PostProcessor.this.receiveSuperNightYuvImage(parallelTaskData, image, str, 2, z);
                return;
            }
            throw new RuntimeException("[z] onTuningAvailable: could not get parallel data");
        }

        public void onYuvAvailable(Image image, String str, boolean z) {
            String[] split = str.split(File.separator);
            long parseLong = Long.parseLong(split[0]);
            int parseInt = Integer.parseInt(split[1]);
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[z] onYuvAvailable: ");
            sb.append(parseLong);
            String str2 = " | ";
            sb.append(str2);
            sb.append(parseInt);
            sb.append(str2);
            sb.append(image.getTimestamp());
            Log.d(access$100, sb.toString());
            if (parseLong != image.getTimestamp()) {
                image.setTimestamp(parseLong);
            }
            ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(parseLong));
            if (parallelTaskData == null) {
                throw new RuntimeException("[z] onYuvAvailable: could not get parallel data");
            } else if (parallelTaskData.getAlgoType() != 12) {
                if (!parallelTaskData.isRaw2YuvDone()) {
                    startRaw2YuvBottomHalf(parallelTaskData, parseLong);
                }
                ParallelDataZipper.getInstance().join(image, parseInt, 1, z);
            } else {
                PostProcessor.this.receiveSuperNightYuvImage(parallelTaskData, image, str, 0, z);
            }
        }
    };
    private int mMaxParallelRequestNumber = 10;
    private List mObsoleteImageReaderList = new ArrayList();
    /* access modifiers changed from: private */
    public ConcurrentHashMap mParallelTaskHashMap = new ConcurrentHashMap();
    private List mParams;
    /* access modifiers changed from: private */
    public PostProcessStatusCallback mPostProcessStatusCallback;
    private DataStatusCallback mRawDataStatusCallback = new DataStatusCallback() {
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<android.media.Image>, for r3v0, types: [java.util.List, java.util.List<android.media.Image>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onImageClosed(List<Image> list) {
            for (Image onOriginalImageClosed : list) {
                PostProcessor.this.mCaptureDataListener.onOriginalImageClosed(onOriginalImageClosed);
            }
        }
    };
    private boolean mSRRequireReprocess;
    private SessionStatusCallback mSessionStatusCb = new SessionStatusCallback() {
        public void onSessionCallback(int i, String str, Object obj) {
            if (i == 0) {
                ResultData resultData = (ResultData) obj;
                if (!(PostProcessor.this.mSessionStatusCbListener == null || resultData == null)) {
                    ISessionStatusCallBackListener iSessionStatusCallBackListener = (ISessionStatusCallBackListener) PostProcessor.this.mSessionStatusCbListener.get();
                    if (iSessionStatusCallBackListener != null) {
                        try {
                            iSessionStatusCallBackListener.onSessionStatusFlawResultData(resultData.getResultId(), resultData.getFlawResult());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                String access$100 = PostProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onSessionCallback FLAW getResultId: ");
                sb.append(resultData.getResultId());
                sb.append(", getFlawResult: ");
                sb.append(resultData.getFlawResult());
                Log.d(access$100, sb.toString());
            } else if (i != 1) {
                String access$1002 = PostProcessor.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown result type ");
                sb2.append(i);
                Log.e(access$1002, sb2.toString());
            } else if (PostProcessor.this.mImageProcessorStatusCb != null) {
                PostProcessor.this.mImageProcessorStatusCb.onMetadataReceived(1, obj);
            }
        }
    };
    /* access modifiers changed from: private */
    public WeakReference mSessionStatusCbListener;
    private boolean mShouldDestroyWhenTasksFinished = false;
    private List mSurfaceList = new ArrayList();
    private int mToken = -1;
    /* access modifiers changed from: private */
    public Map mWaitingCropRegions;
    private Map mWaitingEncodeDatas;
    /* access modifiers changed from: private */
    public Map mWaitingReprocessDatas;
    private Handler mWorkerHandler;
    private HandlerThread mWorkerThread = new HandlerThread("CallbackHandleThread");
    /* access modifiers changed from: private */
    public DataListener mZipperResultListener = new DataListener() {
        public void onParallelDataAbandoned(CaptureData captureData) {
            if (captureData != null) {
                String access$100 = PostProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onParallelDataAbandoned: ");
                sb.append(captureData);
                Log.d(access$100, sb.toString());
                PostProcessor.this.mParallelTaskHashMap.remove(Long.valueOf(captureData.getCaptureTimestamp()));
                for (CaptureDataBean captureDataBean : captureData.getCaptureDataBeanList()) {
                    if (captureDataBean != null) {
                        Image mainImage = captureDataBean.getMainImage();
                        String access$1002 = PostProcessor.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("onParallelDataAbandoned: mainImage = ");
                        sb2.append(mainImage);
                        Log.d(access$1002, sb2.toString());
                        if (mainImage != null) {
                            mainImage.close();
                            PostProcessor.this.mCaptureDataListener.onOriginalImageClosed(mainImage);
                        }
                        Image subImage = captureDataBean.getSubImage();
                        String access$1003 = PostProcessor.TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("onParallelDataAbandoned: subImage = ");
                        sb3.append(subImage);
                        Log.d(access$1003, sb3.toString());
                        if (subImage != null) {
                            subImage.close();
                            PostProcessor.this.mCaptureDataListener.onOriginalImageClosed(subImage);
                        }
                        Image tuningImage = captureDataBean.getTuningImage();
                        if (tuningImage != null) {
                            tuningImage.close();
                            PostProcessor.this.mCaptureDataListener.onOriginalImageClosed(tuningImage);
                        }
                    }
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:68:0x031e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onParallelDataAvailable(@NonNull CaptureData captureData) {
            TaskSession taskSession;
            MultiFrameProcessor multiFrameProcessor;
            String str;
            Image image;
            StringBuilder sb;
            String access$100 = PostProcessor.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[z] onParallelDataAvailable: ");
            sb2.append(captureData.getCaptureTimestamp());
            Log.d(access$100, sb2.toString());
            if (Build.IS_DEBUGGABLE) {
                for (CaptureDataBean captureDataBean : captureData.getCaptureDataBeanList()) {
                    Log.d(PostProcessor.TAG, "[z] onParallelDataAvailable: ------------------------");
                    String access$1002 = PostProcessor.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("[z] Result timestamp: ");
                    sb3.append(captureDataBean.getResult().getTimeStamp());
                    Log.d(access$1002, sb3.toString());
                }
            }
            if (CameraSettings.isHighQualityPreferred() && C0122O00000o.instance().OOo00OO() && 2 == captureData.getStreamNum() && !captureData.isSatFusionShot()) {
                String str2 = "evZeroSubImage";
                String str3 = "evZeroMainImage";
                if (captureData.getCaptureDataBeanList().size() > 1) {
                    CaptureDataBean captureDataBean2 = (CaptureDataBean) captureData.getCaptureDataBeanList().get(0);
                    Integer num = (Integer) ICustomCaptureResult.toTotalCaptureResult(captureDataBean2.getResult(), captureDataBean2.getResult().getSessionId()).get(TotalCaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION);
                    String str4 = "evMinusMainImage";
                    if (num == null || num.intValue() >= 0) {
                        Image mainImage = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(0)).getMainImage();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str3);
                        sb4.append(captureData.getCaptureTimestamp());
                        ImageUtil.dumpYuvImageAppendWH(mainImage, sb4.toString());
                        Image subImage = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(0)).getSubImage();
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str2);
                        sb5.append(captureData.getCaptureTimestamp());
                        ImageUtil.dumpYuvImageAppendWH(subImage, sb5.toString());
                        image = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(1)).getMainImage();
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str4);
                        sb6.append(captureData.getCaptureTimestamp());
                        str = sb6.toString();
                        ImageUtil.dumpYuvImageAppendWH(image, str);
                    } else {
                        Image mainImage2 = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(0)).getMainImage();
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(str4);
                        sb7.append(captureData.getCaptureTimestamp());
                        ImageUtil.dumpYuvImageAppendWH(mainImage2, sb7.toString());
                        Image mainImage3 = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(1)).getMainImage();
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str3);
                        sb8.append(captureData.getCaptureTimestamp());
                        ImageUtil.dumpYuvImageAppendWH(mainImage3, sb8.toString());
                        image = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(1)).getSubImage();
                        sb = new StringBuilder();
                    }
                } else if (captureData.getCaptureDataBeanList().size() == 1) {
                    Image mainImage4 = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(0)).getMainImage();
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(str3);
                    sb9.append(captureData.getCaptureTimestamp());
                    ImageUtil.dumpYuvImageAppendWH(mainImage4, sb9.toString());
                    image = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(0)).getSubImage();
                    sb = new StringBuilder();
                }
                sb.append(str2);
                sb.append(captureData.getCaptureTimestamp());
                str = sb.toString();
                ImageUtil.dumpYuvImageAppendWH(image, str);
            }
            int algoType = captureData.getAlgoType();
            String access$1003 = PostProcessor.TAG;
            StringBuilder sb10 = new StringBuilder();
            sb10.append("[z] onParallelDataAvailable: algoType = ");
            sb10.append(algoType);
            Log.k(3, access$1003, sb10.toString());
            ParallelTaskData parallelTaskData = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(captureData.getCaptureTimestamp()));
            if (algoType != 4) {
                ImageFormat imageQueueKey = ImagePool.getInstance().toImageQueueKey(((CaptureDataBean) captureData.getCaptureDataBeanList().get(0)).getMainImage());
                if (ImagePool.getInstance().isImageQueueFull(imageQueueKey, 4)) {
                    Log.w(PostProcessor.TAG, "[z] wait image pool>>");
                    ImagePool.getInstance().waitIfImageQueueFull(imageQueueKey, 4, 0);
                    Log.w(PostProcessor.TAG, "[z] wait image pool<<");
                }
                if (!PostProcessor.SKIP_IMAGEPROCESSOR) {
                    ImageProcessor imageProcessor = captureData.getImageProcessor();
                    if (imageProcessor != null && (parallelTaskData == null || !parallelTaskData.isRaw2YuvDone())) {
                        imageProcessor.mNeedProcessNormalImageSize.getAndIncrement();
                        if (imageProcessor.isBokekMode()) {
                            imageProcessor.mNeedProcessRawImageSize.getAndIncrement();
                            imageProcessor.mNeedProcessDepthImageSize.getAndIncrement();
                        }
                    }
                }
                PostProcessor.this.mImageProcessorStatusCb.onImageProcessStart(captureData.getCaptureTimestamp());
            }
            PostProcessor.this.chooseAndCloseTuningData(algoType, parallelTaskData, captureData);
            boolean z = 11 == algoType;
            if (2 == algoType || (3 == algoType && PostProcessor.this.isSRRequireReprocess())) {
                captureData.setMultiFrameProcessListener(PostProcessor.this.mCaptureDataListener);
                if (parallelTaskData != null) {
                    captureData.setMoonMode(parallelTaskData.getDataParameter().isMoonMode());
                    captureData.setHdrSR(parallelTaskData.isHdrSR());
                    captureData.setCapturedByFrontCamera(parallelTaskData.getDataParameter().isFrontCamera());
                    captureData.setAlgoSize(parallelTaskData.getDataParameter().getPictureSize());
                }
                multiFrameProcessor = MultiFrameProcessor.getInstance();
                taskSession = PostProcessor.this.mImageProcessor.getTaskSession();
            } else if (5 == algoType) {
                captureData.setMultiFrameProcessListener(PostProcessor.this.mCaptureDataListener);
                ParallelTaskData parallelTaskData2 = (ParallelTaskData) PostProcessor.this.mParallelTaskHashMap.get(Long.valueOf(captureData.getCaptureTimestamp()));
                if (parallelTaskData2 != null && parallelTaskData2.getDataParameter().isSaveGroupshotPrimitive()) {
                    captureData.setSaveInputImage(true);
                }
                multiFrameProcessor = MultiFrameProcessor.getInstance();
                taskSession = null;
            } else {
                if (z) {
                    parallelTaskData.setRaw2YuvProcessor(captureData.getImageProcessor());
                    PostProcessor.this.processRaw(captureData);
                } else {
                    PostProcessor.this.mCaptureDataListener.onCaptureDataAvailable(captureData);
                }
                if (parallelTaskData != null) {
                    parallelTaskData.mIsFrontProcessing = false;
                }
                PostProcessor.this.tryToCloseSession();
            }
            multiFrameProcessor.processData(captureData, taskSession);
            if (parallelTaskData != null) {
            }
            PostProcessor.this.tryToCloseSession();
        }
    };

    public class CaptureStatusListener {
        public CaptureStatusListener() {
        }

        public void onCaptureCompleted(ICustomCaptureResult iCustomCaptureResult, boolean z) {
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[0] onCaptureCompleted: timestamp = ");
            sb.append(iCustomCaptureResult.getTimeStamp());
            sb.append(" frameNo = ");
            sb.append(iCustomCaptureResult.getFrameNumber());
            Log.d(access$100, sb.toString());
            ParallelDataZipper.getInstance().join(iCustomCaptureResult, z);
        }

        public void onCaptureFailed(long j, int i) {
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[0] onCaptureFailed: reason = ");
            sb.append(i);
            sb.append(" timestamp = ");
            sb.append(j);
            Log.w(access$100, sb.toString());
            PostProcessor.this.mParallelTaskHashMap.remove(Long.valueOf(j));
        }

        public void onCaptureStarted(@NonNull ParallelTaskData parallelTaskData) {
            PostProcessor.this.startBoost(2);
            long timestamp = parallelTaskData.getTimestamp();
            boolean isSatFusionShot = parallelTaskData.isSatFusionShot();
            String savePath = parallelTaskData.getSavePath();
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[0] onCaptureStarted: timestamp = ");
            sb.append(timestamp);
            sb.append(", savePath = ");
            sb.append(savePath);
            sb.append(", fusion = ");
            sb.append(isSatFusionShot);
            Log.d(access$100, sb.toString());
            if (!parallelTaskData.isAbandoned()) {
                PostProcessor.this.mParallelTaskHashMap.put(Long.valueOf(timestamp), parallelTaskData);
                OnParallelTaskDataAddToProcessorListener onParallelTaskDataAddToProcessorListener = parallelTaskData.mAddToProcessorCallback;
                if (onParallelTaskDataAddToProcessorListener != null) {
                    onParallelTaskDataAddToProcessorListener.OnParallelTaskDataAddToProcessor();
                }
                parallelTaskData.mIsFrontProcessing = true;
            }
            CaptureData captureData = new CaptureData(parallelTaskData.getAlgoType(), PostProcessor.this.mImageProcessor instanceof DualCameraProcessor ? 2 : 1, parallelTaskData.getBurstNum(), timestamp, parallelTaskData.isAbandoned(), PostProcessor.this.mImageProcessor);
            captureData.setDataListener(PostProcessor.this.mZipperResultListener);
            captureData.setRequireTuningData(parallelTaskData.isRequireTuningData());
            captureData.setIsSatFusionShot(parallelTaskData.isSatFusionShot());
            ParallelDataZipper.getInstance().startTask(captureData);
        }
    }

    class ImageAvailableListener implements OnImageAvailableListener {
        private int mCameraType;
        private int mImageFlag;
        private ImageMemoryManager mMemoryManager;

        ImageAvailableListener(int i, int i2, ImageMemoryManager imageMemoryManager) {
            this.mImageFlag = i;
            this.mCameraType = i2;
            this.mMemoryManager = imageMemoryManager;
        }

        public void onImageAvailable(ImageReader imageReader) {
            if (imageReader == null) {
                Log.k(6, PostProcessor.TAG, "[0] onImageAvailable: null imageReader!");
                return;
            }
            int hashCode = imageReader.hashCode();
            this.mMemoryManager.waitImageCloseIfNeeded(hashCode);
            Image acquireNextImage = imageReader.acquireNextImage();
            String access$100 = PostProcessor.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[0] onImageAvailable: timestamp = ");
            sb.append(acquireNextImage.getTimestamp());
            sb.append(", target = ");
            sb.append(this.mImageFlag);
            sb.append(", type = ");
            sb.append(this.mCameraType);
            Log.k(3, access$100, sb.toString());
            this.mMemoryManager.holdAnImage(hashCode, acquireNextImage);
            if (Util.isDumpImageEnabled()) {
                ImageUtil.dumpImage(acquireNextImage, "hal");
            }
            ParallelDataZipper.getInstance().join(acquireNextImage, this.mImageFlag, this.mCameraType, false);
        }
    }

    public interface PostProcessStatusCallback {
        void onImagePostProcessEnd(ParallelTaskData parallelTaskData);

        void onImagePostProcessStart(ParallelTaskData parallelTaskData);

        void onPostProcessorClosed(PostProcessor postProcessor);
    }

    class RawSuperNightData {
        boolean mainImageReceived;
        ReprocessData reprocessData;
        boolean tuningImageReceived;

        private RawSuperNightData() {
        }
    }

    public PostProcessor(Context context, PostProcessStatusCallback postProcessStatusCallback) {
        this.mWorkerThread.start();
        this.mWorkerHandler = new Handler(this.mWorkerThread.getLooper());
        this.mPostProcessStatusCallback = postProcessStatusCallback;
        init();
    }

    private void beginProcessYuv(ParallelTaskData parallelTaskData, Image image, String str, int i, boolean z) {
        ReprocessData reprocessData;
        Image image2 = image;
        int i2 = i;
        if (this.mWaitingEncodeDatas == null) {
            this.mWaitingEncodeDatas = new HashMap(2);
        }
        String str2 = str;
        long parseLong = Long.parseLong(str.split(File.separator)[0]);
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("process yuv timestamp ");
        sb.append(parseLong);
        sb.append(" for flag ");
        sb.append(i2);
        Log.d(str3, sb.toString());
        RawSuperNightData rawSuperNightData = (RawSuperNightData) this.mWaitingEncodeDatas.get(Long.valueOf(parseLong));
        if (rawSuperNightData == null) {
            Log.d(TAG, "create super night data");
            RawSuperNightData rawSuperNightData2 = new RawSuperNightData();
            if (i2 == 0) {
                reprocessData = generateReprocessData(parallelTaskData, image, str, ReprocessData.REPROCESS_FUNCTION_NONE, z);
                rawSuperNightData2.mainImageReceived = true;
            } else {
                reprocessData = generateReprocessData(parallelTaskData, null, str, ReprocessData.REPROCESS_FUNCTION_NONE, z);
                reprocessData.setTuningImage(image);
                Image tuningImage = parallelTaskData.getTuningImage();
                if (tuningImage != null) {
                    ImagePool.getInstance().releaseImage(tuningImage);
                    tuningImage.close();
                }
                parallelTaskData.setTuningImage(image);
                rawSuperNightData2.tuningImageReceived = true;
            }
            rawSuperNightData2.reprocessData = reprocessData;
            this.mWaitingEncodeDatas.put(Long.valueOf(parseLong), rawSuperNightData2);
            return;
        }
        Log.d(TAG, "another yuv image received");
        ReprocessData reprocessData2 = rawSuperNightData.reprocessData;
        if (i2 == 0) {
            reprocessData2.setMainImage(image);
            rawSuperNightData.mainImageReceived = true;
        } else {
            reprocessData2.setTuningImage(image);
            Image tuningImage2 = parallelTaskData.getTuningImage();
            if (tuningImage2 != null) {
                ImagePool.getInstance().releaseImage(tuningImage2);
                tuningImage2.close();
            }
            parallelTaskData.setTuningImage(image);
            rawSuperNightData.tuningImageReceived = true;
        }
        if (rawSuperNightData.mainImageReceived && rawSuperNightData.tuningImageReceived) {
            ParallelTaskData parallelTaskData2 = parallelTaskData;
            parallelTaskData.setAlgoType(13);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0094  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a5 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void chooseAndCloseTuningData(int i, ParallelTaskData parallelTaskData, CaptureData captureData) {
        boolean z;
        boolean z2;
        boolean z3 = 11 == i;
        if (parallelTaskData != null) {
            parallelTaskData.setHWMFNRProcessing(false);
        }
        if (!z3 && parallelTaskData != null && parallelTaskData.isRequireTuningData()) {
            List captureDataBeanList = captureData.getCaptureDataBeanList();
            int i2 = 0;
            boolean z4 = false;
            while (i2 < captureDataBeanList.size()) {
                CaptureDataBean captureDataBean = (CaptureDataBean) captureDataBeanList.get(i2);
                Image tuningImage = captureDataBean.getTuningImage();
                boolean isTuningImageFromPool = captureDataBean.isTuningImageFromPool();
                if (i != 12) {
                    if (i2 == 0) {
                        if (isTuningImageFromPool) {
                            parallelTaskData.setTuningImage(tuningImage);
                            z = z4;
                        } else {
                            parallelTaskData.setTuningImage(ImageUtil.queueImageToPool(ImagePool.getInstance(), tuningImage, 1));
                        }
                    }
                    z = z4;
                    z2 = true;
                    if (!z2) {
                        tuningImage.close();
                        this.mCaptureDataListener.onOriginalImageClosed(tuningImage);
                        if (isTuningImageFromPool) {
                            ImagePool.getInstance().releaseImage(tuningImage);
                        }
                    }
                    i2++;
                    z4 = z;
                } else {
                    Integer num = (Integer) captureDataBean.getResult().getRequest().get(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION);
                    if (num != null && num.intValue() == 0 && !z4) {
                        Log.d(TAG, String.format(Locale.ENGLISH, "choose tuning buffer: get the %d frame metadata", new Object[]{Integer.valueOf(i2)}));
                        if (isTuningImageFromPool) {
                            parallelTaskData.setTuningImage(tuningImage);
                            z = true;
                        } else {
                            parallelTaskData.setTuningImage(ImageUtil.queueImageToPool(ImagePool.getInstance(), tuningImage, 1));
                            z2 = true;
                            z = true;
                            if (!z2) {
                            }
                            i2++;
                            z4 = z;
                        }
                    }
                    z = z4;
                    z2 = true;
                    if (!z2) {
                    }
                    i2++;
                    z4 = z;
                }
                z2 = false;
                if (!z2) {
                }
                i2++;
                z4 = z;
            }
        }
    }

    /* access modifiers changed from: private */
    public ICustomCaptureResult chooseCaptureResult(CaptureData captureData, int i) {
        if (C0122O00000o.instance().OOo000o()) {
            int burstNum = captureData.getBurstNum() - 1;
            CaptureDataBean captureDataBean = (CaptureDataBean) captureData.getCaptureDataBeanList().get(burstNum);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("[ALGOUP|MMCAMERA]: Add last metadata, index = ");
            sb.append(burstNum);
            Log.i(str, sb.toString());
            return captureDataBean.getResult();
        }
        if (i == 1 || i == 10 || i == 12 || i == 13) {
            int i2 = 0;
            while (i2 < captureData.getCaptureDataBeanList().size()) {
                ICustomCaptureResult result = ((CaptureDataBean) captureData.getCaptureDataBeanList().get(i2)).getResult();
                Integer num = (Integer) result.getRequest().get(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION);
                if (num == null || num.intValue() != 0) {
                    i2++;
                } else {
                    Log.d(TAG, String.format(Locale.ENGLISH, "chooseCaptureResult: get the %d frame metadata", new Object[]{Integer.valueOf(i2)}));
                    return result;
                }
            }
        }
        return ((CaptureDataBean) captureData.getCaptureDataBeanList().get(0)).getResult();
    }

    /* access modifiers changed from: private */
    public void closePoolImage(Image image) {
        if (image != null) {
            image.close();
            ImagePool.getInstance().releaseImage(image);
        }
    }

    /* access modifiers changed from: private */
    public void doEncodeJpeg(long j, Image image) {
        String str = "doEncodeJpeg: X";
        RawSuperNightData rawSuperNightData = (RawSuperNightData) this.mWaitingEncodeDatas.get(Long.valueOf(j));
        if (rawSuperNightData != null) {
            try {
                Log.d(TAG, "doEncodeJpeg: E");
                rawSuperNightData.reprocessData.setMainImage(image);
                LocalParallelService.getReprocessor().submit(rawSuperNightData.reprocessData);
            } catch (Exception e) {
                OnDataAvailableListener onDataAvailableListener = this.mJpegEncoderListener;
                String message = e.getMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("could not reprocess timeStamp ");
                sb.append(j);
                onDataAvailableListener.onError(message, sb.toString());
            } catch (Throwable th) {
                this.mWaitingEncodeDatas.remove(Long.valueOf(j));
                Log.d(TAG, str);
                throw th;
            }
            this.mWaitingEncodeDatas.remove(Long.valueOf(j));
            Log.d(TAG, str);
            return;
        }
        Log.d(TAG, "could not encode jpeg for null supernight data");
    }

    /* access modifiers changed from: private */
    public static void dumpFusionInputs(CaptureData captureData) {
        if (captureData.getCaptureDataBeanList().size() != 1) {
            Log.e(TAG, "dumpFusionInputs: illegal fusion input frame count!");
        } else if (captureData.getMultiFrameProcessResult() != captureData.getCaptureDataBeanList().get(0)) {
            Log.e(TAG, "dumpFusionInputs: illegal fusion input frame data!");
        } else {
            CaptureDataBean multiFrameProcessResult = captureData.getMultiFrameProcessResult();
            if (multiFrameProcessResult.getMainImage() == null) {
                Log.e(TAG, "dumpFusionInputs: fusion input frame main is null!");
            } else if (multiFrameProcessResult.getSubImage() == null) {
                Log.e(TAG, "dumpFusionInputs: fusion input frame sub is null!");
            } else if (multiFrameProcessResult.getResult() == null) {
                Log.e(TAG, "dumpFusionInputs: fusion input frame meta is null!");
            } else {
                Image mainImage = multiFrameProcessResult.getMainImage();
                StringBuilder sb = new StringBuilder();
                sb.append("fusionMainImage");
                sb.append(multiFrameProcessResult.getResult().getTimeStamp());
                ImageUtil.dumpYuvImageAppendWH(mainImage, sb.toString());
                Image subImage = multiFrameProcessResult.getSubImage();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("fusionSubImage");
                sb2.append(multiFrameProcessResult.getResult().getTimeStamp());
                ImageUtil.dumpYuvImageAppendWH(subImage, sb2.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public ReprocessData generateReprocessData(ParallelTaskData parallelTaskData, Image image, String str, int i, boolean z) {
        ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
        ICustomCaptureResult captureResult = parallelTaskData.getCaptureResult();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("[2] onImageProcessed: captureResult = ");
        sb.append(captureResult.getResults());
        Log.d(str2, sb.toString());
        ReprocessData reprocessData = new ReprocessData(image, str, captureResult, dataParameter.isFrontCamera(), dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), dataParameter.getOutputFormat(), this.mJpegEncoderListener);
        reprocessData.setReprocessFunctionType(i);
        reprocessData.setJpegQuality(dataParameter.getJpegQuality());
        reprocessData.setImageFromPool(z);
        if (parallelTaskData.isRequireTuningData()) {
            reprocessData.setTuningImage(parallelTaskData.getTuningImage());
            reprocessData.setKeepTuningImage(true);
            reprocessData.setTuningImageFromPool(true);
        }
        if (1212500294 == reprocessData.getOutputFormat() && dataParameter.getSupportZeroDegreeOrientationImage()) {
            reprocessData.setRotateOrientationToZero(true);
        }
        reprocessData.setYuvInputSize(dataParameter.getPictureSize().getWidth(), dataParameter.getPictureSize().getHeight());
        reprocessData.setRawInputSize(parallelTaskData.getRawInputWidth(), parallelTaskData.getRawInputHeight());
        reprocessData.setDataStatusCallback(this.mRawDataStatusCallback);
        return reprocessData;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.xiaomi.protocol.IImageReaderParameterSets>, for r3v0, types: [java.util.List, java.util.List<com.xiaomi.protocol.IImageReaderParameterSets>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getMinHoldImageNum(List<IImageReaderParameterSets> list) {
        int i = 0;
        if (list == null || list.isEmpty()) {
            Log.e(TAG, "getMinHoldImageNum: empty param");
            return 0;
        }
        for (IImageReaderParameterSets iImageReaderParameterSets : list) {
            if (i == 0 || iImageReaderParameterSets.maxImages < i) {
                i = iImageReaderParameterSets.maxImages;
            }
        }
        return i;
    }

    private boolean isAnyFrontProcessing() {
        for (ParallelTaskData parallelTaskData : this.mParallelTaskHashMap.values()) {
            if (parallelTaskData.mIsFrontProcessing) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public boolean isNeedCallBackFinished(ParallelTaskData parallelTaskData) {
        boolean z = false;
        if (parallelTaskData == null) {
            return false;
        }
        if (parallelTaskData.getParallelType() == -7 || parallelTaskData.getParallelType() == -5 || parallelTaskData.getParallelType() == -6 || parallelTaskData.getAlgoType() == 10 || parallelTaskData.getAlgoType() == 12 || parallelTaskData.getAlgoType() == 13) {
            z = true;
        }
        return z;
    }

    private boolean isParallelSupportedAlgo(int i) {
        return i == 0 || i == 1 || i == 8 || i == 10 || i == 12 || i == 13;
    }

    /* access modifiers changed from: private */
    public void processRaw(@NonNull CaptureData captureData) {
        Log.d(TAG, "processRaw: E");
        List<CaptureDataBean> captureDataBeanList = captureData.getCaptureDataBeanList();
        if (captureDataBeanList == null || captureDataBeanList.size() == 0) {
            Log.w(TAG, "processRaw: null data bean list");
            return;
        }
        long timestamp = ((CaptureDataBean) captureDataBeanList.get(0)).getMainImage().getTimestamp();
        StringBuilder sb = new StringBuilder();
        sb.append(timestamp);
        sb.append(File.separator);
        sb.append(0);
        String sb2 = sb.toString();
        ParallelTaskData parallelTaskData = (ParallelTaskData) this.mParallelTaskHashMap.get(Long.valueOf(timestamp));
        if (parallelTaskData != null) {
            ArrayList arrayList = new ArrayList(captureDataBeanList.size());
            ArrayList arrayList2 = new ArrayList(captureDataBeanList.size());
            for (CaptureDataBean captureDataBean : captureDataBeanList) {
                arrayList.add(captureDataBean.getMainImage());
                arrayList2.add(captureDataBean.getTuningImage());
            }
            ICustomCaptureResult iCustomCaptureResult = null;
            Iterator it = captureData.getCaptureDataBeanList().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CaptureDataBean captureDataBean2 = (CaptureDataBean) it.next();
                if (captureDataBean2.isFirstResult()) {
                    iCustomCaptureResult = captureDataBean2.getResult();
                    parallelTaskData.setCaptureResult(iCustomCaptureResult);
                    break;
                }
            }
            ICustomCaptureResult iCustomCaptureResult2 = iCustomCaptureResult;
            if (iCustomCaptureResult2 != null) {
                ParallelTaskDataParameter dataParameter = parallelTaskData.getDataParameter();
                ReprocessData reprocessData = new ReprocessData(arrayList, sb2, iCustomCaptureResult2, dataParameter.isFrontCamera(), dataParameter.getOutputSize().getWidth(), dataParameter.getOutputSize().getHeight(), dataParameter.getOutputFormat(), this.mJpegEncoderListener);
                reprocessData.setReprocessFunctionType(ReprocessData.REPROCESS_FUNCTION_RAW_MFNR);
                reprocessData.setFrontMirror(dataParameter.isMirror());
                reprocessData.setTuningImage(arrayList2);
                reprocessData.setYuvInputSize(dataParameter.getPictureSize().getWidth(), dataParameter.getPictureSize().getHeight());
                reprocessData.setRawInputSize(parallelTaskData.getRawInputWidth(), parallelTaskData.getRawInputHeight());
                reprocessData.setDataStatusCallback(this.mRawDataStatusCallback);
                try {
                    LocalParallelService.getReprocessor().submit(reprocessData);
                } catch (Exception e) {
                    String str = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("processRaw: ");
                    sb3.append(e.getMessage());
                    Log.e(str, sb3.toString(), (Throwable) e);
                    this.mJpegEncoderListener.onError(e.getMessage(), sb2);
                }
                Log.d(TAG, "processRaw: X");
                return;
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append("no raw CaptureResult with timestamp ");
            sb4.append(timestamp);
            throw new RuntimeException(sb4.toString());
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("no raw parallelTaskData with timestamp ");
        sb5.append(timestamp);
        throw new RuntimeException(sb5.toString());
    }

    /* access modifiers changed from: private */
    public void receiveSuperNightYuvImage(ParallelTaskData parallelTaskData, Image image, String str, int i, boolean z) {
        long parseLong = Long.parseLong(str.split(File.separator)[0]);
        if (!z) {
            image = ImageUtil.queueImageToPool(ImagePool.getInstance(), image, 1);
        }
        image.setTimestamp(parseLong);
        beginProcessYuv(parallelTaskData, image, str, i, true);
        if (parallelTaskData.getAlgoType() == 13) {
            if (i != 0) {
                image = (Image) ((RawSuperNightData) this.mWaitingEncodeDatas.get(Long.valueOf(parseLong))).reprocessData.getMainImage().get(0);
            }
            if (this.mImageProcessor != null) {
                Log.d(TAG, "[z] onHidlImageAvailable: try to do filter for raw");
                this.mImageProcessor.dispatchFilterTask(new FilterTaskData(image, 0, true));
                return;
            }
            Log.d(TAG, "[z] onHidlImageAvailable: could not do filter for image processor null");
            doEncodeJpeg(parseLong, image);
            return;
        }
        Log.d(TAG, "[z] onHidlImageAvailable: waiting image");
    }

    /* access modifiers changed from: private */
    public void startBoost(int i) {
        if (C0124O00000oO.isMTKPlatform()) {
            if (this.mBoostFramework == null) {
                this.mBoostFramework = new BoostFrameworkImpl();
            }
            this.mBoostFramework.startBoost(0, i);
        }
    }

    private void stopBoost() {
        if (C0124O00000oO.isMTKPlatform()) {
            BoostFrameworkImpl boostFrameworkImpl = this.mBoostFramework;
            if (boostFrameworkImpl != null) {
                boostFrameworkImpl.stopBoost();
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void tryToCloseSession() {
        String str;
        String sb;
        if (this.mShouldDestroyWhenTasksFinished && !isAnyFrontProcessing()) {
            Iterator it = this.mParams.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((IImageReaderParameterSets) it.next()).isParallel) {
                        Log.d(TAG, "tryToCloseSession: closeParallelSession");
                        ParallelSnapshotManager.getInstance().closeCaptureSession(this.mSurfaceList);
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (!this.mParallelTaskHashMap.isEmpty() || !this.mShouldDestroyWhenTasksFinished) {
            str = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("tryToCloseSession: ignore  this:");
            sb2.append(this);
            sb = sb2.toString();
        } else {
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("tryToCloseSession: E  this:");
            sb3.append(this);
            Log.d(str2, sb3.toString());
            finish();
            deInit();
            if (this.mPostProcessStatusCallback != null) {
                this.mPostProcessStatusCallback.onPostProcessorClosed(this);
            }
            if (this.mWaitingCropRegions != null) {
                this.mWaitingCropRegions.clear();
                this.mWaitingCropRegions = null;
            }
            if (this.mWaitingReprocessDatas != null) {
                this.mWaitingReprocessDatas.clear();
                this.mWaitingReprocessDatas = null;
            }
            if (this.mWaitingEncodeDatas != null) {
                this.mWaitingEncodeDatas.clear();
                this.mWaitingEncodeDatas = null;
            }
            stopBoost();
            str = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("tryToCloseSession: X  this:");
            sb4.append(this);
            sb = sb4.toString();
        }
        Log.d(str, sb);
    }

    /* access modifiers changed from: private */
    public void tryToStopBoost() {
        Log.d(TAG, "tryToStopBoost");
        if (isIdle()) {
            Log.d(TAG, "stopBoost");
            stopBoost();
        }
    }

    public void clearFrontProcessingTask() {
        for (ParallelTaskData parallelTaskData : this.mParallelTaskHashMap.values()) {
            if (parallelTaskData.mIsFrontProcessing && !parallelTaskData.isParallelVTCameraSnapshot()) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("clearFrontProcessingTask: timestamp > ");
                sb.append(parallelTaskData.getTimestamp());
                Log.i(str, sb.toString());
                ParallelDataZipper.getInstance().releaseData(parallelTaskData.getTimestamp());
            }
        }
    }

    public synchronized void configCaptureSession(BufferFormat bufferFormat, CameraSize cameraSize) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configCaptureSession: ");
        sb.append(bufferFormat);
        Log.d(str, sb.toString());
        if (this.mImageProcessor != null) {
            this.mImageProcessor.stopWorkWhenIdle();
        }
        this.mImageProcessor = bufferFormat.getGraphDescriptor().getStreamNumber() == 2 ? new DualCameraProcessor(this.mImageProcessorStatusCb, bufferFormat) : new SingleCameraProcessor(this.mImageProcessorStatusCb, bufferFormat);
        this.mImageProcessor.setMaxParallelRequestNumber(this.mMaxParallelRequestNumber);
        this.mImageProcessor.setImageBufferQueueSize(this.mMaxParallelRequestNumber);
        this.mImageProcessorList.add(this.mImageProcessor);
        this.mImageProcessor.startWork();
        this.mImageProcessor.setTaskSession(MiCameraAlgo.createSessionByOutputConfigurations(bufferFormat, this.mImageProcessor.configOutputConfigurations(bufferFormat, cameraSize), this.mSessionStatusCb));
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.xiaomi.protocol.IImageReaderParameterSets>, for r6v0, types: [java.util.List, java.util.List<com.xiaomi.protocol.IImageReaderParameterSets>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public List configHALOutputSurface(@NonNull List<IImageReaderParameterSets> list) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configHALOutputSurface: paramsNum=");
        sb.append(list.size());
        Log.d(str, sb.toString());
        if (!this.mImageReaderList.isEmpty()) {
            Log.d(TAG, "save obsolete image readers");
            this.mObsoleteImageReaderList.addAll(this.mImageReaderList);
            this.mImageReaderList.clear();
        }
        if (!this.mSurfaceList.isEmpty()) {
            this.mSurfaceList.clear();
        }
        int minHoldImageNum = getMinHoldImageNum(list);
        this.mImageMemoryManager = new ImageMemoryManager(minHoldImageNum);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("configHALOutputSurface: holdNum=");
        sb2.append(minHoldImageNum);
        Log.d(str2, sb2.toString());
        for (IImageReaderParameterSets iImageReaderParameterSets : list) {
            ImageReader newInstance = ImageReader.newInstance(iImageReaderParameterSets.width, iImageReaderParameterSets.height, iImageReaderParameterSets.format, iImageReaderParameterSets.maxImages);
            ImageReaderHelper.setImageReaderNameDepends(newInstance, ImageReaderType.ORIGINAL, false);
            newInstance.setOnImageAvailableListener(new ImageAvailableListener(iImageReaderParameterSets.imageType, iImageReaderParameterSets.cameraType, this.mImageMemoryManager), this.mWorkerHandler);
            this.mSurfaceList.add(newInstance.getSurface());
            this.mImageReaderList.add(newInstance);
        }
        return this.mSurfaceList;
    }

    public boolean configParallelCaptureSession(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("configParallelCaptureSession: mSurfaceList = ");
        sb.append(this.mSurfaceList);
        Log.d(str, sb.toString());
        List list = this.mSurfaceList;
        if (list != null && list.size() > 0 && this.mSurfaceList.size() == this.mParams.size()) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.mParams.size(); i++) {
                if (((IImageReaderParameterSets) this.mParams.get(i)).isParallel) {
                    arrayList.add((Surface) this.mSurfaceList.get(i));
                }
            }
            if (arrayList.size() > 0) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("configParallelCaptureSession: surfaceList.size = ");
                sb2.append(arrayList.size());
                Log.d(str2, sb2.toString());
                if (z) {
                    ParallelSnapshotManager.getInstance().createCaptureSession(arrayList);
                }
                return true;
            }
        }
        return false;
    }

    public void deInit() {
        Log.d(TAG, "deInit");
        if (!this.mImageReaderList.isEmpty()) {
            for (ImageReader close : this.mImageReaderList) {
                close.close();
            }
            this.mImageReaderList.clear();
        }
        if (!this.mObsoleteImageReaderList.isEmpty()) {
            for (ImageReader close2 : this.mObsoleteImageReaderList) {
                close2.close();
            }
            this.mObsoleteImageReaderList.clear();
        }
        if (!this.mSurfaceList.isEmpty()) {
            this.mSurfaceList.clear();
        }
        this.mHeifSaverCallback = null;
    }

    public synchronized void destroyWhenTasksFinished(int i) {
        int token = getToken();
        if (token == -1 || i == token) {
            this.mShouldDestroyWhenTasksFinished = true;
            tryToCloseSession();
        }
    }

    public void finish() {
        ImageProcessor imageProcessor = this.mImageProcessor;
        if (imageProcessor != null) {
            imageProcessor.stopWork();
            this.mImageProcessor = null;
        }
        if (!this.mImageProcessorList.isEmpty() && isIdle()) {
            for (ImageProcessor stopWork : this.mImageProcessorList) {
                stopWork.stopWork();
            }
            this.mImageProcessorList.clear();
        }
        HandlerThread handlerThread = this.mWorkerThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            try {
                this.mWorkerThread.join();
                this.mWorkerThread = null;
                this.mWorkerHandler = null;
            } catch (InterruptedException e) {
                Log.w(TAG, "finish: failed!", (Throwable) e);
            }
        }
    }

    public CaptureStatusListener getCaptureStatusListener() {
        return this.mCaptureStatusListener;
    }

    public int getFrontProcessingCount() {
        int i = 0;
        for (ParallelTaskData parallelTaskData : this.mParallelTaskHashMap.values()) {
            if (parallelTaskData.mIsFrontProcessing) {
                i++;
            }
        }
        return i;
    }

    public List getParams() {
        return this.mParams;
    }

    public List getSurfaceList() {
        return this.mSurfaceList;
    }

    public synchronized int getToken() {
        return this.mToken;
    }

    public void init() {
        if (C0124O00000oO.isMTKPlatform() && CameraSettings.isHeicImageFormatSelected()) {
            this.mHeifSaverCallback = new SaveHeifCallback() {
                public void onSaveFinish(Image image, ParallelTaskData parallelTaskData) {
                    image.close();
                    PostProcessor.this.mCaptureDataListener.onOriginalImageClosed(image);
                    ImagePool.getInstance().releaseImage(image);
                    PostProcessor.this.closePoolImage(parallelTaskData.getTuningImage());
                    parallelTaskData.releaseImageData();
                    if (PostProcessor.this.isNeedCallBackFinished(parallelTaskData) && PostProcessor.this.mPostProcessStatusCallback != null) {
                        PostProcessor.this.mPostProcessStatusCallback.onImagePostProcessEnd(parallelTaskData);
                    }
                    long timestamp = parallelTaskData.getTimestamp();
                    PostProcessor.this.mParallelTaskHashMap.remove(Long.valueOf(timestamp));
                    String access$100 = PostProcessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("[HEIF] onSaveFinish: parallelTaskHashMap remove ");
                    sb.append(timestamp);
                    Log.d(access$100, sb.toString());
                }
            };
        }
    }

    public boolean isAnyRequestBlocked() {
        for (ParallelTaskData parallelTaskData : this.mParallelTaskHashMap.values()) {
            if (!isParallelSupportedAlgo(parallelTaskData.getAlgoType()) && parallelTaskData.mIsFrontProcessing) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("isAnyRequestBlocked: taskData algoType:");
                sb.append(parallelTaskData.getAlgoType());
                sb.append(", timestamp:");
                sb.append(parallelTaskData.getTimestamp());
                Log.d(str, sb.toString());
                return true;
            }
        }
        return false;
    }

    public boolean isAnyRequestIsHWMFNRProcessing() {
        for (ParallelTaskData isHWMFNRProcessing : this.mParallelTaskHashMap.values()) {
            if (isHWMFNRProcessing.isHWMFNRProcessing()) {
                return true;
            }
        }
        return false;
    }

    public boolean isIdle() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isIdle ");
        sb.append(this.mParallelTaskHashMap.size());
        Log.d(str, sb.toString());
        return this.mParallelTaskHashMap.isEmpty();
    }

    public boolean isSRRequireReprocess() {
        return this.mSRRequireReprocess;
    }

    public synchronized boolean isStopping() {
        return this.mShouldDestroyWhenTasksFinished;
    }

    public boolean needWaitAlgorithmEngine() {
        ImageProcessor imageProcessor = this.mImageProcessor;
        boolean z = imageProcessor != null && imageProcessor.isAlgorithmEngineBusy();
        String str = TAG;
        if (z) {
            Log.d(str, "needWaitAlgorithmEngine: return true");
        } else {
            Log.c(str, "needWaitAlgorithmEngine: return false");
        }
        return z;
    }

    public boolean needWaitImageClose() {
        ImageMemoryManager imageMemoryManager = this.mImageMemoryManager;
        boolean z = imageMemoryManager != null && imageMemoryManager.needWaitImageClose();
        String str = TAG;
        if (z) {
            Log.d(str, "needWaitImageClose: return true");
        } else {
            Log.c(str, "needWaitImageClose: return false");
        }
        return z;
    }

    public void setImageSaver(ImageSaver imageSaver) {
        this.mImageSaver = imageSaver;
    }

    public void setMaxParallelRequestNumber(int i) {
        if (i > 0) {
            this.mMaxParallelRequestNumber = i;
        }
    }

    public void setOnSessionStatusCallBackListener(ISessionStatusCallBackListener iSessionStatusCallBackListener) {
        this.mSessionStatusCbListener = new WeakReference(iSessionStatusCallBackListener);
    }

    public void setParams(List list) {
        this.mParams = list;
    }

    public void setSRRequireReprocess(boolean z) {
        this.mSRRequireReprocess = z;
    }

    public synchronized void setToken(int i) {
        this.mToken = i;
    }

    public synchronized boolean tryToReuse() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("tryToReuse: mShouldDestroyWhenTasksFinished = ");
        sb.append(this.mShouldDestroyWhenTasksFinished);
        Log.d(str, sb.toString());
        if (!this.mShouldDestroyWhenTasksFinished || isAnyFrontProcessing() || !configParallelCaptureSession(false)) {
            Log.w(TAG, "tryToReuse: fail to create configParallelCaptureSession");
            return false;
        }
        this.mShouldDestroyWhenTasksFinished = false;
        return true;
    }
}
