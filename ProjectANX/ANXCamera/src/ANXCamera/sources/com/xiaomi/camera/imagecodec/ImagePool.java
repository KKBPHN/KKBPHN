package com.xiaomi.camera.imagecodec;

import android.annotation.TargetApi;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.ImageWriter;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.Surface;
import com.xiaomi.camera.imagecodec.ImageReaderHelper.ImageReaderType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class ImagePool {
    private static final String TAG = "ImagePool";
    private static final int TRIM_POOL_IMAGES_COUNT = 10;
    private static boolean sInited = false;
    private static int sMaxAcquireImageCount = 30;
    private static int sMaxDequeueImageCount = 4;
    private Map mAcquiredImageCountMap = new HashMap();
    private Map mHoldImages = new HashMap();
    private OnImageAvailableListener mImageAvailableListener = new OnImageAvailableListener() {
        public void onImageAvailable(ImageReader imageReader) {
            synchronized (ImagePool.this.mImageLock) {
                Image acquireNextImage = imageReader.acquireNextImage();
                ImagePool.this.mImageLongSparseArray.append(acquireNextImage.getTimestamp(), acquireNextImage);
                ImagePool.this.mImageLock.notify();
            }
        }
    };
    /* access modifiers changed from: private */
    public final Object mImageLock = new Object();
    /* access modifiers changed from: private */
    public LongSparseArray mImageLongSparseArray = new LongSparseArray();
    /* access modifiers changed from: private */
    public HandlerThread mImageReaderHandlerThread = new HandlerThread("ImageReaderHandlerThread");
    private Map mImageReaderMap = new HashMap();
    /* access modifiers changed from: private */
    public HandlerThread mImageWriterHandlerThread = new HandlerThread("ImageWriterHandlerThread");
    private Map mImageWriterMap = new HashMap();
    private Map mPooledImageCountMap = new HashMap();
    private final Object mQueueSizeLock = new Object();

    public class ImageFormat {
        private int mFormat;
        private int mHeight;
        private int mWidth;

        public ImageFormat(int i, int i2, int i3) {
            this.mWidth = i;
            this.mHeight = i2;
            this.mFormat = i3;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ImageFormat)) {
                return super.equals(obj);
            }
            ImageFormat imageFormat = (ImageFormat) obj;
            boolean z = this.mWidth == imageFormat.getWidth() && this.mHeight == imageFormat.getHeight() && this.mFormat == imageFormat.getFormat();
            return z;
        }

        public int getFormat() {
            return this.mFormat;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int hashCode() {
            int i = this.mWidth;
            int i2 = (i >>> 8) | (i << 8);
            int i3 = this.mHeight;
            return (this.mFormat ^ i2) ^ ((i3 >>> 16) | (i3 << 16));
        }
    }

    class ImagePoolHolder {
        static ImagePool sInstance = new ImagePool();

        static {
            sInstance.mImageReaderHandlerThread.start();
            sInstance.mImageWriterHandlerThread.start();
        }

        ImagePoolHolder() {
        }
    }

    private int changeAcquiredImageCountLocked(ImageFormat imageFormat, int i) {
        Integer num = (Integer) this.mAcquiredImageCountMap.get(imageFormat);
        Integer valueOf = Integer.valueOf(0);
        if (num == null) {
            num = valueOf;
        }
        Integer valueOf2 = Integer.valueOf(num.intValue() + i);
        if (valueOf2.intValue() < 0) {
            valueOf2 = valueOf;
        }
        this.mAcquiredImageCountMap.put(imageFormat, valueOf2);
        return valueOf2.intValue();
    }

    private int changePooledImageCountLocked(ImageFormat imageFormat, int i) {
        Integer num = (Integer) this.mPooledImageCountMap.get(imageFormat);
        Integer valueOf = Integer.valueOf(0);
        if (num == null) {
            num = valueOf;
        }
        Integer valueOf2 = Integer.valueOf(num.intValue() + i);
        if (valueOf2.intValue() < 0) {
            valueOf2 = valueOf;
        }
        this.mPooledImageCountMap.put(imageFormat, valueOf2);
        return valueOf2.intValue();
    }

    private int getAcquiredImageCountLocked(ImageFormat imageFormat) {
        if (imageFormat != null) {
            Integer num = (Integer) this.mAcquiredImageCountMap.get(imageFormat);
            if (num != null) {
                return num.intValue();
            }
        }
        return 0;
    }

    private ImageWriter getImageWriter(@NonNull ImageFormat imageFormat) {
        if (this.mImageWriterMap.containsKey(imageFormat)) {
            return (ImageWriter) this.mImageWriterMap.get(imageFormat);
        }
        ImageReader newInstance = ImageReader.newInstance(imageFormat.getWidth(), imageFormat.getHeight(), imageFormat.getFormat(), sMaxAcquireImageCount);
        ImageReaderHelper.setImageReaderNameDepends(newInstance, ImageReaderType.IMAGEPOOL, true);
        newInstance.setOnImageAvailableListener(this.mImageAvailableListener, new Handler(this.mImageReaderHandlerThread.getLooper()));
        ImageWriter makeImageWriter = makeImageWriter(newInstance.getSurface(), sMaxDequeueImageCount, true);
        makeImageWriter.setOnImageReleasedListener(null, new Handler(this.mImageWriterHandlerThread.getLooper()));
        this.mImageReaderMap.put(imageFormat, newInstance);
        this.mImageWriterMap.put(imageFormat, makeImageWriter);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getImageWriter: mImageWriterMap.size = ");
        sb.append(this.mImageWriterMap.size());
        Log.d(str, sb.toString());
        if (this.mImageReaderMap.size() > 10) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getImageWriter: there are too much ImageWriter and ImageReader instance in map, size is : ");
            sb2.append(this.mImageReaderMap.size());
            Log.e(str2, sb2.toString());
        }
        return makeImageWriter;
    }

    public static ImagePool getInstance() {
        return ImagePoolHolder.sInstance;
    }

    public static void init(int i, int i2) {
        if (!sInited) {
            String str = " maxDequeueCount=";
            if (i <= 0 || i2 <= 0) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("invalid parameter: maxAcquireCount=");
                sb.append(i);
                sb.append(str);
                sb.append(i2);
                Log.e(str2, sb.toString());
            } else if (i + i2 >= 64) {
                Log.e(TAG, String.format(Locale.ENGLISH, "maxAcquireCount(%d) + maxDequeueCount(%d) should not be larger than 64", new Object[]{Integer.valueOf(i), Integer.valueOf(i)}));
            } else {
                sInited = true;
                sMaxAcquireImageCount = i;
                sMaxDequeueImageCount = i2;
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("init: maxAcquireCount=");
                sb2.append(i);
                sb2.append(str);
                sb2.append(i2);
                Log.d(str3, sb2.toString());
            }
        }
    }

    public static ImageWriter makeImageWriter(Surface surface, int i, boolean z) {
        ImageWriter imageWriter;
        if (VERSION.SDK_INT <= 29) {
            return ImageWriter.newInstance(surface, i);
        }
        try {
            Method declaredMethod = ImageWriter.class.getDeclaredMethod("newInstance", new Class[]{Surface.class, Integer.TYPE, Boolean.TYPE});
            declaredMethod.setAccessible(true);
            imageWriter = (ImageWriter) declaredMethod.invoke(null, new Object[]{surface, Integer.valueOf(i), Boolean.valueOf(z)});
        } catch (Exception unused) {
            Log.d(TAG, "could not find our method, try android method");
            imageWriter = ImageWriter.newInstance(surface, i);
        }
        return imageWriter;
    }

    private boolean needTrimPoolBuffer() {
        int min = Math.min(sMaxAcquireImageCount, 10);
        synchronized (this.mQueueSizeLock) {
            for (Entry entry : this.mPooledImageCountMap.entrySet()) {
                if (entry.getValue() != null && ((Integer) entry.getValue()).intValue() >= min) {
                    return true;
                }
            }
            for (Entry entry2 : this.mAcquiredImageCountMap.entrySet()) {
                if (entry2.getValue() != null && ((Integer) entry2.getValue()).intValue() >= min) {
                    return true;
                }
            }
            return false;
        }
    }

    public void clear() {
        Log.d(TAG, "clear: E");
        int i = 0;
        while (i < this.mImageLongSparseArray.size()) {
            try {
                ((Image) this.mImageLongSparseArray.valueAt(i)).close();
                i++;
            } catch (Exception e) {
                Log.e(TAG, "clear ImagePool cause error: ", e);
            }
        }
        for (ImageReader close : this.mImageReaderMap.values()) {
            close.close();
        }
        this.mImageReaderMap.clear();
        for (ImageWriter close2 : this.mImageWriterMap.values()) {
            close2.close();
        }
        this.mImageWriterMap.clear();
        synchronized (this.mQueueSizeLock) {
            this.mAcquiredImageCountMap.clear();
            this.mPooledImageCountMap.clear();
        }
        Log.d(TAG, "clear: X");
    }

    public Image getAnEmptyImage(@NonNull ImageFormat imageFormat) {
        Image dequeueInputImage = getImageWriter(imageFormat).dequeueInputImage();
        dequeueInputImage.setTimestamp(System.currentTimeMillis());
        return dequeueInputImage;
    }

    public Image getImage(long j) {
        Image image = (Image) this.mImageLongSparseArray.get(j);
        this.mImageLongSparseArray.remove(j);
        return image;
    }

    public void holdImage(Image image) {
        synchronized (this.mQueueSizeLock) {
            if (image != null) {
                ImageFormat imageQueueKey = toImageQueueKey(image);
                this.mHoldImages.put(image, imageQueueKey);
                int changeAcquiredImageCountLocked = changeAcquiredImageCountLocked(imageQueueKey, 1);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("holdImage: image: ");
                sb.append(image);
                sb.append(" | ");
                sb.append(image.getTimestamp());
                sb.append(" qSize: ");
                sb.append(changeAcquiredImageCountLocked);
                Log.d(str, sb.toString());
                this.mQueueSizeLock.notify();
            }
        }
    }

    public boolean isImageQueueFull(ImageFormat imageFormat, int i) {
        boolean z;
        synchronized (this.mQueueSizeLock) {
            int i2 = sMaxAcquireImageCount - i;
            z = false;
            if (getAcquiredImageCountLocked(imageFormat) >= Math.max(0, i2)) {
                z = true;
            }
        }
        return z;
    }

    public void queueImage(Image image) {
        long timestamp = image.getTimestamp();
        if (this.mImageLongSparseArray.get(timestamp) == null) {
            ImageFormat imageQueueKey = toImageQueueKey(image);
            synchronized (this.mImageLock) {
                ImageWriter imageWriter = getImageWriter(imageQueueKey);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("queueImage: start. image: ");
                sb.append(image);
                sb.append(" | ");
                sb.append(image.getTimestamp());
                Log.d(str, sb.toString());
                imageWriter.queueInputImage(image);
                try {
                    this.mImageLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "queueImage: end");
            }
            synchronized (this.mQueueSizeLock) {
                changePooledImageCountLocked(imageQueueKey, 1);
            }
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Image has exist: ");
        sb2.append(timestamp);
        throw new RuntimeException(sb2.toString());
    }

    public void releaseImage(Image image) {
        synchronized (this.mQueueSizeLock) {
            if (image != null) {
                ImageFormat imageFormat = (ImageFormat) this.mHoldImages.get(image);
                if (imageFormat != null) {
                    this.mHoldImages.remove(image);
                    int changeAcquiredImageCountLocked = changeAcquiredImageCountLocked(imageFormat, -1);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("releaseImage: image: ");
                    sb.append(image);
                    sb.append(" qSize: ");
                    sb.append(changeAcquiredImageCountLocked);
                    Log.d(str, sb.toString());
                    this.mQueueSizeLock.notify();
                } else {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("releaseImage: not hold image ");
                    sb2.append(image);
                    Log.w(str2, sb2.toString());
                }
            }
        }
    }

    public ImageFormat toImageQueueKey(Image image) {
        if (image != null) {
            return new ImageFormat(image.getWidth(), image.getHeight(), image.getFormat());
        }
        return null;
    }

    @TargetApi(28)
    public void trimPoolBuffer() {
        Log.d(TAG, "trimPoolBuffer: E");
        for (ImageReader discardFreeBuffers : this.mImageReaderMap.values()) {
            discardFreeBuffers.discardFreeBuffers();
        }
        synchronized (this.mQueueSizeLock) {
            this.mPooledImageCountMap.clear();
        }
        Log.d(TAG, "trimPoolBuffer: X");
    }

    public void trimPoolBufferIfNeeded() {
        if (needTrimPoolBuffer()) {
            Log.d(TAG, "trimPoolBufferIfNeeded");
            trimPoolBuffer();
        }
    }

    public void waitIfImageQueueFull(ImageFormat imageFormat, int i, int i2) {
        int max = Math.max(0, sMaxAcquireImageCount - i);
        synchronized (this.mQueueSizeLock) {
            while (getAcquiredImageCountLocked(imageFormat) >= max) {
                if (i2 > 0) {
                    try {
                        this.mQueueSizeLock.wait((long) i2);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                } else {
                    this.mQueueSizeLock.wait();
                }
            }
        }
    }
}
