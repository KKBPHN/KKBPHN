package com.xiaomi.camera.core;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.hardware.camera2.params.OutputConfiguration;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import com.android.camera.CameraSize;
import com.android.camera.log.Log;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.CaptureData.CaptureDataBean;
import com.xiaomi.camera.core.ImageProcessor.FilterTaskData;
import com.xiaomi.camera.core.ImageProcessor.ImageProcessorStatusCallback;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.engine.BufferFormat;
import com.xiaomi.engine.FrameData;
import com.xiaomi.engine.FrameData.FrameStatusCallback;
import com.xiaomi.engine.TaskSession.FrameCallback;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.util.ArrayList;
import java.util.List;

public class DualCameraProcessor extends ImageProcessor {
    /* access modifiers changed from: private */
    public static final String TAG = "DualCameraProcessor";
    private final boolean mIsFusionMode;

    DualCameraProcessor(ImageProcessorStatusCallback imageProcessorStatusCallback, BufferFormat bufferFormat) {
        super(imageProcessorStatusCallback, bufferFormat);
        this.mIsFusionMode = bufferFormat.getCameraCombinationMode() == 516;
    }

    private void processCaptureResult(ICustomCaptureResult iCustomCaptureResult, Image image, int i) {
        Parcelable results = iCustomCaptureResult.getResults();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("processCaptureResult: cameraMetadataNative = ");
        sb.append(results);
        Log.d(str, sb.toString());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("processCaptureResult: image flag = ");
        sb2.append(i);
        Log.d(str2, sb2.toString());
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("processCaptureResult: image = ");
        sb3.append(image);
        Log.d(str3, sb3.toString());
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("processCaptureResult: timestamp = ");
        sb4.append(image.getTimestamp());
        Log.d(str4, sb4.toString());
        FrameData frameData = new FrameData(i, iCustomCaptureResult.getSequenceId(), iCustomCaptureResult.getFrameNumber(), results, iCustomCaptureResult.getParcelRequest(), image);
        Parcelable mainPhysicalResult = i == 0 ? iCustomCaptureResult.getMainPhysicalResult() : iCustomCaptureResult.getSubPhysicalResult();
        if (mainPhysicalResult != null) {
            frameData.setPhysicalResultMetadata(mainPhysicalResult);
        }
        frameData.setFrameCallback(new FrameStatusCallback() {
            public void onFrameImageClosed(Image image) {
                String access$000 = DualCameraProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onFrameImageClosed: ");
                sb.append(image);
                Log.d(access$000, sb.toString());
                ImageProcessorStatusCallback imageProcessorStatusCallback = DualCameraProcessor.this.mImageProcessorStatusCallback;
                if (imageProcessorStatusCallback != null) {
                    imageProcessorStatusCallback.onOriginalImageClosed(image);
                }
                ImagePool.getInstance().releaseImage(image);
            }
        });
        this.mTaskSession.processFrame(frameData, new FrameCallback() {
            public void onFrameProcessed(int i, String str, Object obj) {
                String access$000 = DualCameraProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onFrameProcessed: ");
                sb.append(i);
                Log.d(access$000, sb.toString());
            }
        });
    }

    public List configOutputConfigurations(BufferFormat bufferFormat, @Nullable CameraSize cameraSize) {
        ArrayList arrayList = new ArrayList();
        this.mEffectImageReaderHolder = ImageReader.newInstance(bufferFormat.getBufferWidth(), bufferFormat.getBufferHeight(), bufferFormat.getBufferFormat(), getImageBufferQueueSize());
        this.mEffectImageReaderHolder.setOnImageAvailableListener(new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                Image acquireNextImage = imageReader.acquireNextImage();
                long timestamp = acquireNextImage.getTimestamp();
                PerformanceTracker.trackAlgorithmProcess("[  EFFECT]", 1);
                String access$000 = DualCameraProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onImageAvailable: effectImage received: ");
                sb.append(timestamp);
                Log.d(access$000, sb.toString());
                Image queueImageToPool = DualCameraProcessor.this.queueImageToPool(ImagePool.getInstance(), acquireNextImage);
                acquireNextImage.close();
                DualCameraProcessor.this.dispatchFilterTask(new FilterTaskData(queueImageToPool, 0, true));
                DualCameraProcessor.this.onProcessImageDone(timestamp);
            }
        }, getImageReaderHandler());
        arrayList.add(new OutputConfiguration(0, this.mEffectImageReaderHolder.getSurface()));
        if (!this.mIsFusionMode) {
            this.mRawImageReaderHolder = ImageReader.newInstance(bufferFormat.getBufferWidth(), bufferFormat.getBufferHeight(), bufferFormat.getBufferFormat(), getImageBufferQueueSize());
            this.mRawImageReaderHolder.setOnImageAvailableListener(new OnImageAvailableListener() {
                public void onImageAvailable(ImageReader imageReader) {
                    Image acquireNextImage = imageReader.acquireNextImage();
                    long timestamp = acquireNextImage.getTimestamp();
                    PerformanceTracker.trackAlgorithmProcess("[     RAW]", 1);
                    String access$000 = DualCameraProcessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onImageAvailable: rawImage received: ");
                    sb.append(timestamp);
                    Log.d(access$000, sb.toString());
                    Image queueImageToPool = DualCameraProcessor.this.queueImageToPool(ImagePool.getInstance(), acquireNextImage);
                    acquireNextImage.close();
                    DualCameraProcessor.this.dispatchFilterTask(new FilterTaskData(queueImageToPool, 1, true));
                }
            }, getImageReaderHandler());
            arrayList.add(new OutputConfiguration(1, this.mRawImageReaderHolder.getSurface()));
            int bufferWidth = bufferFormat.getBufferWidth();
            int bufferHeight = bufferFormat.getBufferHeight();
            if (!C0124O00000oO.O0o0O0o) {
                bufferWidth /= 2;
                bufferHeight /= 2;
            }
            if (cameraSize != null) {
                int i = cameraSize.width;
                if (i > 0) {
                    int i2 = cameraSize.height;
                    if (i2 > 0) {
                        bufferHeight = i2;
                        bufferWidth = i;
                    }
                }
            }
            this.mDepthImageReaderHolder = ImageReader.newInstance(bufferWidth, bufferHeight, 540422489, getImageBufferQueueSize());
            this.mDepthImageReaderHolder.setOnImageAvailableListener(new OnImageAvailableListener() {
                public void onImageAvailable(ImageReader imageReader) {
                    Image acquireNextImage = imageReader.acquireNextImage();
                    PerformanceTracker.trackAlgorithmProcess("[   DEPTH]", 1);
                    String access$000 = DualCameraProcessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onImageAvailable: depthImage received: ");
                    sb.append(acquireNextImage.getTimestamp());
                    Log.d(access$000, sb.toString());
                    ImageProcessorStatusCallback imageProcessorStatusCallback = DualCameraProcessor.this.mImageProcessorStatusCallback;
                    if (imageProcessorStatusCallback != null) {
                        imageProcessorStatusCallback.onImageProcessed(acquireNextImage, 2, false);
                    }
                    acquireNextImage.close();
                    DualCameraProcessor.this.mNeedProcessDepthImageSize.getAndDecrement();
                    DualCameraProcessor.this.tryToStopWork();
                }
            }, getImageReaderHandler());
            arrayList.add(new OutputConfiguration(2, this.mDepthImageReaderHolder.getSurface()));
        }
        return arrayList;
    }

    public String getProcessorName() {
        return "D";
    }

    /* access modifiers changed from: 0000 */
    public boolean isIdle() {
        ImageProcessorStatusCallback imageProcessorStatusCallback = this.mImageProcessorStatusCallback;
        boolean z = true;
        boolean z2 = imageProcessorStatusCallback != null && imageProcessorStatusCallback.isAnyFrontProcessing(this);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isIdle: ");
        sb.append(this.mNeedProcessNormalImageSize.get());
        sb.append(" processing: ");
        sb.append(z2);
        Log.d(str, sb.toString());
        if (!this.mIsFusionMode) {
            if (!(this.mNeedProcessNormalImageSize.get() == 0 && this.mNeedProcessRawImageSize.get() == 0 && this.mNeedProcessDepthImageSize.get() == 0)) {
                z = false;
            }
            return z;
        }
        if (this.mNeedProcessNormalImageSize.get() != 0 || z2) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.xiaomi.camera.core.CaptureData$CaptureDataBean>, for r6v0, types: [java.util.List, java.util.List<com.xiaomi.camera.core.CaptureData$CaptureDataBean>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void processImage(List<CaptureDataBean> list) {
        if (list == null || list.size() == 0) {
            Log.w(TAG, "processImage: dataBeans is empty!");
            return;
        }
        onProcessImageStart();
        for (CaptureDataBean captureDataBean : list) {
            Image mainImage = captureDataBean.getMainImage();
            if (isImageValid(mainImage)) {
                Image subImage = captureDataBean.getSubImage();
                if (isImageValid(subImage)) {
                    PerformanceTracker.trackAlgorithmProcess("[ORIGINAL]", 0);
                    ICustomCaptureResult result = captureDataBean.getResult();
                    processCaptureResult(result, mainImage, 0);
                    processCaptureResult(result, subImage, 1);
                }
            }
        }
    }
}
