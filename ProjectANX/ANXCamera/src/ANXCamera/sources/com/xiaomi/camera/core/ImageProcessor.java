package com.xiaomi.camera.core;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.LongSparseArray;
import android.util.Size;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.CameraSize;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.xiaomi.camera.base.ImageUtil;
import com.xiaomi.camera.core.CaptureData.CaptureDataBean;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.engine.BufferFormat;
import com.xiaomi.engine.TaskSession;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ImageProcessor {
    private static final int DEFAULT_IMAGE_BUFFER_QUEUE_SIZE = 4;
    private static final int MSG_CHECK_TIMEOUT_TASK = 3;
    private static final int MSG_IMAGE_DOFILTER = 2;
    private static final int MSG_IMAGE_RECEIVED = 1;
    private static final int PROCESS_TASK_TIMEOUT_MS = 15000;
    /* access modifiers changed from: private */
    public static final String TAG = "ImageProcessor";
    protected ConditionVariable mBlockVariable;
    ImageReader mDepthImageReaderHolder;
    ImageReader mEffectImageReaderHolder;
    AtomicInteger mEngineProcessingImageSize;
    protected FilterProcessor mFilterProcessor;
    private int mImageBufferQueueSize;
    ImageProcessorStatusCallback mImageProcessorStatusCallback;
    private Handler mImageReaderHandler;
    private HandlerThread mImageReaderThread;
    private LongSparseArray mInFlightTask;
    private final boolean mIsBokenMode;
    private boolean mIsNeedStopWork;
    private int mMaxParallelRequestNumber = 10;
    AtomicInteger mNeedProcessDepthImageSize;
    AtomicInteger mNeedProcessNormalImageSize;
    AtomicInteger mNeedProcessRawImageSize;
    ImageReader mRawImageReaderHolder;
    /* access modifiers changed from: private */
    public LinkedList mTaskDataList;
    /* access modifiers changed from: private */
    public Object mTaskDataLock;
    TaskSession mTaskSession;
    private Handler mWorkHandler;
    private HandlerThread mWorkThread;

    public class FilterTaskData {
        public Image image;
        public boolean isPoolImage;
        public int target;

        public FilterTaskData(Image image2, int i, boolean z) {
            this.image = image2;
            this.target = i;
            this.isPoolImage = z;
        }
    }

    public interface ImageProcessorStatusCallback {
        ParallelTaskData getParallelTaskData(long j);

        boolean isAnyFrontProcessing(ImageProcessor imageProcessor);

        void onImageProcessFailed(Image image, String str);

        void onImageProcessStart(long j);

        void onImageProcessed(Image image, int i, boolean z);

        void onMetadataReceived(int i, Object obj);

        void onOriginalImageClosed(Image image);
    }

    class TaskRecord {
        long processStartTime;
        long timestamp;

        public TaskRecord(long j, long j2) {
            this.timestamp = j;
            this.processStartTime = j2;
        }
    }

    public ImageProcessor(ImageProcessorStatusCallback imageProcessorStatusCallback, BufferFormat bufferFormat) {
        boolean z = false;
        this.mEngineProcessingImageSize = new AtomicInteger(0);
        this.mNeedProcessNormalImageSize = new AtomicInteger(0);
        this.mNeedProcessRawImageSize = new AtomicInteger(0);
        this.mNeedProcessDepthImageSize = new AtomicInteger(0);
        this.mImageBufferQueueSize = 4;
        this.mBlockVariable = new ConditionVariable();
        this.mTaskDataLock = new Object();
        this.mTaskDataList = new LinkedList();
        this.mInFlightTask = new LongSparseArray();
        Log.d(TAG, "create ImageProcessor");
        this.mImageProcessorStatusCallback = imageProcessorStatusCallback;
        String format = String.format(Locale.ENGLISH, "_%s_%dx%d_%d", new Object[]{getProcessorName(), Integer.valueOf(bufferFormat.getBufferWidth()), Integer.valueOf(bufferFormat.getBufferHeight()), Integer.valueOf(hashCode())});
        StringBuilder sb = new StringBuilder();
        sb.append("WorkerThread");
        sb.append(format);
        this.mWorkThread = new HandlerThread(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ReaderThread");
        sb2.append(format);
        this.mImageReaderThread = new HandlerThread(sb2.toString());
        this.mFilterProcessor = new FilterProcessor(this.mBlockVariable);
        this.mFilterProcessor.init(new Size(bufferFormat.getBufferWidth(), bufferFormat.getBufferHeight()));
        if (bufferFormat.getGraphDescriptor().getOperationModeID() == 32770) {
            z = true;
        }
        this.mIsBokenMode = z;
    }

    /* access modifiers changed from: private */
    public void calculateAndRemoveTimeoutTask(long j) {
        boolean z;
        synchronized (this.mTaskDataLock) {
            z = false;
            for (int size = this.mInFlightTask.size() - 1; size >= 0; size--) {
                TaskRecord taskRecord = (TaskRecord) this.mInFlightTask.valueAt(size);
                if (j - taskRecord.processStartTime > 15000) {
                    this.mInFlightTask.removeAt(size);
                    Log.e(TAG, "task %d is timeout. cost: %dms", Long.valueOf(taskRecord.timestamp), Long.valueOf(j - taskRecord.processStartTime));
                    Util.dumpBugReportLog();
                    z = true;
                }
            }
        }
        if (z) {
            sendProcessImageMessage();
        }
    }

    private boolean isAlive() {
        HandlerThread handlerThread = this.mWorkThread;
        return handlerThread != null && handlerThread.isAlive();
    }

    private void mockProcessImage(List list) {
        Log.d(TAG, "prop skip imageprocessor");
        if (list == null || list.size() == 0) {
            Log.w(TAG, "processImage: dataBeans is empty!");
            return;
        }
        onProcessImageStart();
        CaptureDataBean captureDataBean = (CaptureDataBean) list.get(0);
        for (int i = 1; i < list.size(); i++) {
            Image mainImage = ((CaptureDataBean) list.get(i)).getMainImage();
            if (mainImage != null) {
                mainImage.close();
                ImageProcessorStatusCallback imageProcessorStatusCallback = this.mImageProcessorStatusCallback;
                if (imageProcessorStatusCallback != null) {
                    imageProcessorStatusCallback.onOriginalImageClosed(mainImage);
                }
                ImagePool.getInstance().releaseImage(mainImage);
            }
            Image subImage = ((CaptureDataBean) list.get(i)).getSubImage();
            if (subImage != null) {
                subImage.close();
                ImageProcessorStatusCallback imageProcessorStatusCallback2 = this.mImageProcessorStatusCallback;
                if (imageProcessorStatusCallback2 != null) {
                    imageProcessorStatusCallback2.onOriginalImageClosed(subImage);
                }
                ImagePool.getInstance().releaseImage(subImage);
            }
            Image tuningImage = ((CaptureDataBean) list.get(i)).getTuningImage();
            if (tuningImage != null) {
                tuningImage.close();
                ImageProcessorStatusCallback imageProcessorStatusCallback3 = this.mImageProcessorStatusCallback;
                if (imageProcessorStatusCallback3 != null) {
                    imageProcessorStatusCallback3.onOriginalImageClosed(tuningImage);
                }
            }
        }
        Image subImage2 = captureDataBean.getSubImage();
        if (subImage2 != null) {
            subImage2.close();
            ImageProcessorStatusCallback imageProcessorStatusCallback4 = this.mImageProcessorStatusCallback;
            if (imageProcessorStatusCallback4 != null) {
                imageProcessorStatusCallback4.onOriginalImageClosed(subImage2);
            }
            ImagePool.getInstance().releaseImage(subImage2);
        }
        Image tuningImage2 = captureDataBean.getTuningImage();
        if (tuningImage2 != null) {
            tuningImage2.close();
            ImageProcessorStatusCallback imageProcessorStatusCallback5 = this.mImageProcessorStatusCallback;
            if (imageProcessorStatusCallback5 != null) {
                imageProcessorStatusCallback5.onOriginalImageClosed(tuningImage2);
            }
        }
        Image mainImage2 = captureDataBean.getMainImage();
        long timestamp = mainImage2.getTimestamp();
        Image queueImageToPool = queueImageToPool(ImagePool.getInstance(), mainImage2);
        mainImage2.close();
        ImageProcessorStatusCallback imageProcessorStatusCallback6 = this.mImageProcessorStatusCallback;
        if (imageProcessorStatusCallback6 != null) {
            imageProcessorStatusCallback6.onOriginalImageClosed(mainImage2);
        }
        ImagePool.getInstance().releaseImage(mainImage2);
        dispatchFilterTask(new FilterTaskData(queueImageToPool, 0, true));
        onProcessImageDone(timestamp);
    }

    /* access modifiers changed from: private */
    public void processImageInner(List list) {
        if (PostProcessor.SKIP_IMAGEPROCESSOR) {
            mockProcessImage(list);
            return;
        }
        onPreProcessImage(list);
        processImage(list);
    }

    private void removeInFlightTask(long j) {
        synchronized (this.mTaskDataLock) {
            this.mInFlightTask.remove(j);
        }
    }

    private void sendProcessImageMessage() {
        if (isAlive()) {
            Message obtainMessage = this.mWorkHandler.obtainMessage();
            obtainMessage.what = 1;
            this.mWorkHandler.sendMessage(obtainMessage);
            Log.c(TAG, "sendProcessImageMessage");
            return;
        }
        Object[] objArr = new Object[1];
        HandlerThread handlerThread = this.mWorkThread;
        objArr[0] = handlerThread == null ? "Thread" : handlerThread.getName();
        throw new RuntimeException(String.format("%s already die!", objArr));
    }

    public abstract List configOutputConfigurations(BufferFormat bufferFormat, @Nullable CameraSize cameraSize);

    public void dispatchFilterTask(@NonNull FilterTaskData filterTaskData) {
        if (isAlive()) {
            Message obtainMessage = this.mWorkHandler.obtainMessage();
            obtainMessage.what = 2;
            obtainMessage.obj = filterTaskData;
            this.mWorkHandler.sendMessage(obtainMessage);
            return;
        }
        throw new RuntimeException("Thread already die!");
    }

    public void dispatchTask(@NonNull List list) {
        synchronized (this.mTaskDataLock) {
            this.mTaskDataList.add(list);
            if (!isAlgorithmEngineBusy()) {
                sendProcessImageMessage();
            } else {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchTask: taskSize = ");
                sb.append(this.mTaskDataList.size());
                Log.v(str, sb.toString());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doFilter(@NonNull FilterTaskData filterTaskData) {
        AtomicInteger atomicInteger;
        Image image = filterTaskData.image;
        long timestamp = image.getTimestamp();
        int i = filterTaskData.target;
        ImageProcessorStatusCallback imageProcessorStatusCallback = this.mImageProcessorStatusCallback;
        if (imageProcessorStatusCallback != null) {
            ParallelTaskData parallelTaskData = imageProcessorStatusCallback.getParallelTaskData(timestamp);
            if (parallelTaskData != null && (i == 0 || 1 == i)) {
                if (C0124O00000oO.OOooOO() && (1 == parallelTaskData.getAlgoType() || 3 == parallelTaskData.getAlgoType())) {
                    TotalCaptureResult totalCaptureResult = ICustomCaptureResult.toTotalCaptureResult(parallelTaskData.getCaptureResult(), 0);
                    parallelTaskData.getCameraId();
                    Integer num = (Integer) totalCaptureResult.get(CaptureResult.SENSOR_SENSITIVITY);
                    if (num != null) {
                        num.intValue();
                    }
                }
                if (PostProcessor.SKIP_RENDER || 12 == parallelTaskData.getAlgoType()) {
                    Log.d(TAG, "prop skip render");
                } else {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("doFilter: ");
                    sb.append(timestamp);
                    sb.append("/");
                    sb.append(i);
                    Log.d(str, sb.toString());
                    this.mFilterProcessor.doFilterSync(parallelTaskData, image, i);
                }
            } else if (parallelTaskData == null) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("doFilter: no task data found for image ");
                sb2.append(timestamp);
                Log.w(str2, sb2.toString());
            }
            this.mImageProcessorStatusCallback.onImageProcessed(image, i, filterTaskData.isPoolImage);
        } else if (filterTaskData.isPoolImage) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("doFilter: release pool image ");
            sb3.append(timestamp);
            Log.w(str3, sb3.toString());
            image.close();
            ImagePool.getInstance().releaseImage(image);
        }
        if (!PostProcessor.SKIP_IMAGEPROCESSOR) {
            if (i == 0) {
                atomicInteger = this.mNeedProcessNormalImageSize;
            } else if (i == 1) {
                atomicInteger = this.mNeedProcessRawImageSize;
            } else if (i != 2) {
                String str4 = TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("invalid target: ");
                sb4.append(i);
                Log.e(str4, sb4.toString());
            } else {
                atomicInteger = this.mNeedProcessDepthImageSize;
            }
            atomicInteger.getAndDecrement();
        }
        tryToStopWork();
    }

    /* access modifiers changed from: protected */
    public int getImageBufferQueueSize() {
        return this.mImageBufferQueueSize;
    }

    /* access modifiers changed from: protected */
    public Handler getImageReaderHandler() {
        return this.mImageReaderHandler;
    }

    public String getProcessorName() {
        return TAG;
    }

    public TaskSession getTaskSession() {
        return this.mTaskSession;
    }

    public boolean isAlgorithmEngineBusy() {
        return this.mEngineProcessingImageSize.get() >= this.mMaxParallelRequestNumber;
    }

    public boolean isBokekMode() {
        return this.mIsBokenMode;
    }

    public abstract boolean isIdle();

    /* access modifiers changed from: protected */
    public boolean isImageValid(Image image) {
        if (image == null) {
            return false;
        }
        try {
            image.getHardwareBuffer();
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            Log.e(TAG, e.getMessage(), (Throwable) e);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onPreProcessImage(List list) {
        if (list != null) {
            synchronized (this.mTaskDataLock) {
                long timestamp = ((CaptureDataBean) list.get(0)).getTimestamp();
                long currentTimeMillis = System.currentTimeMillis();
                LongSparseArray longSparseArray = this.mInFlightTask;
                TaskRecord taskRecord = new TaskRecord(timestamp, currentTimeMillis);
                longSparseArray.put(timestamp, taskRecord);
            }
            if (isAlive()) {
                this.mWorkHandler.removeMessages(3);
                this.mWorkHandler.sendEmptyMessageDelayed(3, 15000);
                return;
            }
            Log.w(TAG, "onPreProcessImage: worker thread is dead");
        }
    }

    /* access modifiers changed from: protected */
    public void onProcessImageDone(long j) {
        this.mEngineProcessingImageSize.getAndDecrement();
        removeInFlightTask(j);
        if (!isAlgorithmEngineBusy()) {
            synchronized (this.mTaskDataLock) {
                int size = this.mTaskDataList.size();
                if (size > 0) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onProcessImageDone: taskSize = ");
                    sb.append(size);
                    Log.v(str, sb.toString());
                    sendProcessImageMessage();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onProcessImageStart() {
        this.mEngineProcessingImageSize.getAndIncrement();
    }

    public abstract void processImage(List list);

    /* access modifiers changed from: protected */
    public Image queueImageToPool(ImagePool imagePool, Image image) {
        return ImageUtil.queueImageToPool(imagePool, image, 2);
    }

    @CallSuper
    public void releaseResource() {
        this.mImageProcessorStatusCallback = null;
        ImageReader imageReader = this.mEffectImageReaderHolder;
        if (imageReader != null) {
            imageReader.close();
            this.mEffectImageReaderHolder = null;
        }
        ImageReader imageReader2 = this.mRawImageReaderHolder;
        if (imageReader2 != null) {
            imageReader2.close();
            this.mRawImageReaderHolder = null;
        }
        ImageReader imageReader3 = this.mDepthImageReaderHolder;
        if (imageReader3 != null) {
            imageReader3.close();
            this.mDepthImageReaderHolder = null;
        }
    }

    public void setImageBufferQueueSize(int i) {
        this.mImageBufferQueueSize = i;
    }

    public void setMaxParallelRequestNumber(int i) {
        if (i > 0) {
            this.mMaxParallelRequestNumber = i;
        }
    }

    public void setTaskSession(@NonNull TaskSession taskSession) {
        this.mTaskSession = taskSession;
    }

    public void startWork() {
        this.mWorkThread.start();
        this.mWorkHandler = new Handler(this.mWorkThread.getLooper()) {
            public void handleMessage(Message message) {
                List list;
                int i = message.what;
                if (i == 1) {
                    synchronized (ImageProcessor.this.mTaskDataLock) {
                        list = (List) ImageProcessor.this.mTaskDataList.poll();
                    }
                    if (list != null) {
                        String access$200 = ImageProcessor.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("processImage: ts = ");
                        sb.append(((CaptureDataBean) list.get(0)).getResult().getTimeStamp());
                        Log.d(access$200, sb.toString());
                        ImageProcessor.this.processImageInner(list);
                    }
                } else if (i == 2) {
                    ImageProcessor.this.doFilter((FilterTaskData) message.obj);
                } else if (i != 3) {
                    String access$2002 = ImageProcessor.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("handleMessage: unknown message: ");
                    sb2.append(message.what);
                    Log.d(access$2002, sb2.toString());
                } else {
                    ImageProcessor.this.calculateAndRemoveTimeoutTask(System.currentTimeMillis());
                }
            }
        };
        this.mImageReaderThread.start();
        this.mImageReaderHandler = new Handler(this.mImageReaderThread.getLooper());
        Log.d(TAG, String.format(Locale.ENGLISH, "startWork: %s started", new Object[]{this.mWorkThread.getName()}));
    }

    public synchronized void stopWork() {
        this.mWorkThread.quitSafely();
        Log.d(TAG, String.format(Locale.ENGLISH, "destroy ImageProcessor: %s stopped", new Object[]{this.mWorkThread.getName()}));
        if (this.mWorkHandler != null) {
            this.mWorkHandler.removeCallbacksAndMessages(null);
            this.mWorkHandler = null;
        }
        this.mImageReaderThread.quitSafely();
        if (this.mImageReaderHandler != null) {
            this.mImageReaderHandler.removeCallbacksAndMessages(null);
            this.mImageReaderHandler = null;
        }
        if (this.mFilterProcessor != null) {
            this.mFilterProcessor = null;
        }
        if (this.mTaskSession != null) {
            this.mTaskSession.close();
        }
        releaseResource();
    }

    public synchronized void stopWorkWhenIdle() {
        Log.d(TAG, "stopWorkWhenIdle");
        this.mIsNeedStopWork = true;
        tryToStopWork();
    }

    public synchronized void tryToStopWork() {
        if (this.mIsNeedStopWork && isIdle()) {
            stopWork();
        }
    }
}
