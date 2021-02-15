package com.xiaomi.camera.core;

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
import com.xiaomi.camera.imagecodec.ImageReaderHelper;
import com.xiaomi.camera.imagecodec.ImageReaderHelper.ImageReaderType;
import com.xiaomi.engine.BufferFormat;
import com.xiaomi.engine.FrameData;
import com.xiaomi.engine.FrameData.FrameStatusCallback;
import com.xiaomi.engine.TaskSession.FrameCallback;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.util.ArrayList;
import java.util.List;

public class SingleCameraProcessor extends ImageProcessor {
    /* access modifiers changed from: private */
    public static final String TAG = "SingleCameraProcessor";

    SingleCameraProcessor(ImageProcessorStatusCallback imageProcessorStatusCallback, BufferFormat bufferFormat) {
        super(imageProcessorStatusCallback, bufferFormat);
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
        sb2.append("processCaptureResult: image = ");
        sb2.append(image);
        Log.d(str2, sb2.toString());
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("processCaptureResult: timestamp = ");
        sb3.append(image.getTimestamp());
        sb3.append(", target = ");
        sb3.append(i);
        Log.k(3, str3, sb3.toString());
        FrameData frameData = new FrameData(i, iCustomCaptureResult.getSequenceId(), iCustomCaptureResult.getFrameNumber(), results, iCustomCaptureResult.getParcelRequest(), image);
        Parcelable mainPhysicalResult = iCustomCaptureResult.getMainPhysicalResult();
        if (mainPhysicalResult != null) {
            frameData.setPhysicalResultMetadata(mainPhysicalResult);
        }
        frameData.setFrameCallback(new FrameStatusCallback() {
            public void onFrameImageClosed(Image image) {
                String access$000 = SingleCameraProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onFrameImageClosed: ");
                sb.append(image);
                Log.d(access$000, sb.toString());
                ImageProcessorStatusCallback imageProcessorStatusCallback = SingleCameraProcessor.this.mImageProcessorStatusCallback;
                if (imageProcessorStatusCallback != null) {
                    imageProcessorStatusCallback.onOriginalImageClosed(image);
                }
                ImagePool.getInstance().releaseImage(image);
            }
        });
        this.mTaskSession.processFrame(frameData, new FrameCallback() {
            public void onFrameProcessed(int i, String str, Object obj) {
                String access$000 = SingleCameraProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onFrameProcessed: [");
                sb.append(i);
                sb.append("]:{");
                sb.append(str);
                sb.append("}");
                Log.k(3, access$000, sb.toString());
            }
        });
    }

    public List configOutputConfigurations(BufferFormat bufferFormat, @Nullable CameraSize cameraSize) {
        ArrayList arrayList = new ArrayList();
        this.mEffectImageReaderHolder = ImageReader.newInstance(bufferFormat.getBufferWidth(), bufferFormat.getBufferHeight(), bufferFormat.getBufferFormat(), getImageBufferQueueSize());
        ImageReaderHelper.setImageReaderNameDepends(this.mEffectImageReaderHolder, ImageReaderType.EFFECT, true);
        this.mEffectImageReaderHolder.setOnImageAvailableListener(new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                Image acquireNextImage = imageReader.acquireNextImage();
                long timestamp = acquireNextImage.getTimestamp();
                PerformanceTracker.trackAlgorithmProcess("[  EFFECT]", 1);
                String access$000 = SingleCameraProcessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onImageAvailable: effectImage received: ");
                sb.append(timestamp);
                Log.k(3, access$000, sb.toString());
                Image queueImageToPool = SingleCameraProcessor.this.queueImageToPool(ImagePool.getInstance(), acquireNextImage);
                acquireNextImage.close();
                SingleCameraProcessor.this.dispatchFilterTask(new FilterTaskData(queueImageToPool, 0, true));
                SingleCameraProcessor.this.onProcessImageDone(timestamp);
            }
        }, getImageReaderHandler());
        arrayList.add(new OutputConfiguration(0, this.mEffectImageReaderHolder.getSurface()));
        if (isBokekMode()) {
            this.mRawImageReaderHolder = ImageReader.newInstance(bufferFormat.getBufferWidth(), bufferFormat.getBufferHeight(), bufferFormat.getBufferFormat(), getImageBufferQueueSize());
            this.mRawImageReaderHolder.setOnImageAvailableListener(new OnImageAvailableListener() {
                public void onImageAvailable(ImageReader imageReader) {
                    Image acquireNextImage = imageReader.acquireNextImage();
                    long timestamp = acquireNextImage.getTimestamp();
                    PerformanceTracker.trackAlgorithmProcess("[     RAW]", 1);
                    String access$000 = SingleCameraProcessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onImageAvailable: rawImage received: ");
                    sb.append(timestamp);
                    Log.d(access$000, sb.toString());
                    Image queueImageToPool = SingleCameraProcessor.this.queueImageToPool(ImagePool.getInstance(), acquireNextImage);
                    acquireNextImage.close();
                    SingleCameraProcessor.this.dispatchFilterTask(new FilterTaskData(queueImageToPool, 1, true));
                }
            }, getImageReaderHandler());
            arrayList.add(new OutputConfiguration(1, this.mRawImageReaderHolder.getSurface()));
            this.mDepthImageReaderHolder = ImageReader.newInstance(bufferFormat.getBufferWidth() / 2, bufferFormat.getBufferHeight() / 2, 540422489, getImageBufferQueueSize());
            this.mDepthImageReaderHolder.setOnImageAvailableListener(new OnImageAvailableListener() {
                public void onImageAvailable(ImageReader imageReader) {
                    Image acquireNextImage = imageReader.acquireNextImage();
                    PerformanceTracker.trackAlgorithmProcess("[   DEPTH]", 1);
                    String access$000 = SingleCameraProcessor.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onImageAvailable: depthImage received: ");
                    sb.append(acquireNextImage.getTimestamp());
                    Log.d(access$000, sb.toString());
                    ImageProcessorStatusCallback imageProcessorStatusCallback = SingleCameraProcessor.this.mImageProcessorStatusCallback;
                    if (imageProcessorStatusCallback != null) {
                        imageProcessorStatusCallback.onImageProcessed(acquireNextImage, 2, false);
                    }
                    acquireNextImage.close();
                    SingleCameraProcessor.this.mNeedProcessDepthImageSize.getAndDecrement();
                    SingleCameraProcessor.this.tryToStopWork();
                }
            }, getImageReaderHandler());
            arrayList.add(new OutputConfiguration(2, this.mDepthImageReaderHolder.getSurface()));
        }
        return arrayList;
    }

    public String getProcessorName() {
        return "S";
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
        if (isBokekMode()) {
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
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.xiaomi.camera.core.CaptureData$CaptureDataBean>, for r5v0, types: [java.util.List, java.util.List<com.xiaomi.camera.core.CaptureData$CaptureDataBean>] */
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
                PerformanceTracker.trackAlgorithmProcess("[ORIGINAL]", 0);
                processCaptureResult(captureDataBean.getResult(), mainImage, 0);
                Image subImage = captureDataBean.getSubImage();
                if (subImage != null) {
                    subImage.close();
                    ImageProcessorStatusCallback imageProcessorStatusCallback = this.mImageProcessorStatusCallback;
                    if (imageProcessorStatusCallback != null) {
                        imageProcessorStatusCallback.onOriginalImageClosed(subImage);
                    }
                    ImagePool.getInstance().releaseImage(subImage);
                }
            }
        }
    }
}
