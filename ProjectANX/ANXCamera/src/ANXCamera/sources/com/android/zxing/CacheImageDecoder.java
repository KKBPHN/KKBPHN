package com.android.zxing;

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import com.android.camera.log.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.AnchorPreviewCallback;
import com.android.camera2.Camera2Proxy.PreviewCallback;
import com.xiaomi.camera.base.ImageUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheImageDecoder extends Decoder {
    public static final int MAX_CACHED_COUNT = 20;
    public static final int MAX_NO_GAUSSIAN_TIME = 60000000;
    private static final String TAG = "CacheImage";
    private AtomicBoolean mCacheStarted = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public AtomicInteger mCachedCount = new AtomicInteger(0);
    private Map mCachedImageMap = new ConcurrentHashMap(20);
    private LinkedList mCachedImages = new LinkedList();
    /* access modifiers changed from: private */
    public WeakReference mCallback;
    private ReentrantReadWriteLock mLock = new ReentrantReadWriteLock();
    private PreviewCallback mPreviewCallback = new PreviewCallback() {
        public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
            if (!CacheImageDecoder.this.isNeedImage()) {
                return true;
            }
            CacheImageDecoder.this.onPreviewFrame(image);
            return false;
        }
    };
    private Lock mReadLock = this.mLock.readLock();
    private PublishSubject mSaver = PublishSubject.create();
    private Disposable mSaverDisposable = this.mSaver.subscribeOn(Schedulers.computation()).map(new Function() {
        public FrameInfo apply(FrameInfo frameInfo) {
            frameInfo.setImage(CacheImageDecoder.this.getAnchorImage(frameInfo.mTimestamp));
            return frameInfo;
        }
    }).map(new Function() {
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0085, code lost:
            if (r0 != null) goto L_0x00a5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x00a3, code lost:
            if (r0 != null) goto L_0x00a5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x00a5, code lost:
            r0.close();
            com.android.zxing.CacheImageDecoder.access$100(r10.this$0).decrementAndGet();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public Long apply(FrameInfo frameInfo) {
            long j;
            Image image;
            String str = CacheImageDecoder.TAG;
            ImageWrapper imageWrapper = frameInfo.mImageWrapper;
            if (imageWrapper != null) {
                Image image2 = imageWrapper.mImage;
                if (image2 != null) {
                    byte[] yUVFromPreviewImage = ImageUtil.getYUVFromPreviewImage(image2);
                    if (yUVFromPreviewImage != null) {
                        try {
                            AnchorPreviewCallback anchorPreviewCallback = (AnchorPreviewCallback) CacheImageDecoder.this.mCallback.get();
                            if (anchorPreviewCallback != null) {
                                YuvImage yuvImage = new YuvImage(yUVFromPreviewImage, 17, frameInfo.mWidth, frameInfo.mHeight, null);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                yuvImage.compressToJpeg(new Rect(0, 0, frameInfo.mWidth, frameInfo.mHeight), 80, byteArrayOutputStream);
                                anchorPreviewCallback.saveBitmapAsThumbnail(BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size()), frameInfo.mWidth, frameInfo.mHeight, true, frameInfo.mImageWrapper.noGaussian);
                                byteArrayOutputStream.close();
                            } else {
                                Log.e(str, "only camera module could anchor frame");
                            }
                        } catch (Exception e) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Error:");
                            sb.append(e.getMessage());
                            Log.e(str, sb.toString());
                            ImageWrapper imageWrapper2 = frameInfo.mImageWrapper;
                            if (imageWrapper2 != null) {
                                image = imageWrapper2.mImage;
                            }
                        } catch (Throwable th) {
                            ImageWrapper imageWrapper3 = frameInfo.mImageWrapper;
                            if (imageWrapper3 != null) {
                                Image image3 = imageWrapper3.mImage;
                                if (image3 != null) {
                                    image3.close();
                                    CacheImageDecoder.this.mCachedCount.decrementAndGet();
                                }
                            }
                            throw th;
                        }
                    }
                    ImageWrapper imageWrapper4 = frameInfo.mImageWrapper;
                    if (imageWrapper4 != null) {
                        image = imageWrapper4.mImage;
                    }
                    j = frameInfo.mTimestamp;
                    return Long.valueOf(j);
                }
            }
            j = 0;
            return Long.valueOf(j);
        }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new Consumer() {
        public void accept(Long l) {
            AnchorPreviewCallback anchorPreviewCallback = (AnchorPreviewCallback) CacheImageDecoder.this.mCallback.get();
            if (l.longValue() > 0) {
                if (anchorPreviewCallback != null) {
                    anchorPreviewCallback.onFrameThumbnailSuccess(l.longValue());
                }
            } else if (anchorPreviewCallback != null) {
                anchorPreviewCallback.onFrameThumbnailFail();
            }
        }
    });
    private Lock mWriteLock = this.mLock.writeLock();

    class FrameInfo {
        public String mFileName;
        public int mHeight;
        public ImageWrapper mImageWrapper;
        public int[] mStrides;
        public long mTimestamp;
        public int mWidth;

        public FrameInfo(long j, int i, int i2, int[] iArr, String str) {
            this.mTimestamp = j;
            this.mWidth = i;
            this.mHeight = i2;
            this.mStrides = iArr;
            this.mFileName = str;
        }

        public void setImage(ImageWrapper imageWrapper) {
            this.mImageWrapper = imageWrapper;
        }
    }

    class ImageWrapper {
        public Image mImage;
        public boolean noGaussian;

        public ImageWrapper(Image image, boolean z) {
            this.mImage = image;
            this.noGaussian = z;
        }
    }

    /* access modifiers changed from: private */
    public ImageWrapper getAnchorImage(long j) {
        ImageWrapper imageWrapper;
        String str = TAG;
        this.mReadLock.lock();
        Image image = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("target timestamp is ");
            sb.append(j);
            Log.d(str, sb.toString());
            if (this.mCachedImageMap.containsKey(Long.valueOf(j))) {
                Image image2 = (Image) this.mCachedImageMap.remove(Long.valueOf(j));
                StringBuilder sb2 = new StringBuilder();
                sb2.append("find image in cache ");
                sb2.append(j);
                sb2.append(" index ");
                sb2.append(this.mCachedImages.indexOf(image2));
                Log.d(str, sb2.toString());
                this.mCachedImages.remove(image2);
                imageWrapper = new ImageWrapper(image2, true);
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("could not find image in cache ");
                sb3.append(j);
                sb3.append(" use nearest");
                Log.d(str, sb3.toString());
                long j2 = Long.MAX_VALUE;
                Iterator it = this.mCachedImages.iterator();
                while (it.hasNext()) {
                    Image image3 = (Image) it.next();
                    if (Math.abs(image3.getTimestamp() - j) < j2) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("preview timestamp is ");
                        sb4.append(image3.getTimestamp());
                        Log.d(str, sb4.toString());
                        j2 = image3.getTimestamp();
                        image = image3;
                    }
                }
                boolean z = false;
                if (image != null) {
                    Image image4 = (Image) this.mCachedImageMap.remove(Long.valueOf(image.getTimestamp()));
                    this.mCachedImages.remove(image4);
                    if (Math.abs(image4.getTimestamp() - j) < 60000000) {
                        Log.d(str, "nearest timestamp is small than 60000000, no gaussian");
                        z = true;
                    } else {
                        Log.d(str, "nearest timestamp is bigger than 60000000, use gaussian");
                    }
                    imageWrapper = new ImageWrapper(image4, z);
                } else {
                    imageWrapper = new ImageWrapper(image, false);
                }
            }
            return imageWrapper;
        } finally {
            this.mReadLock.unlock();
        }
    }

    public PreviewCallback getAnchorPreviewCallback() {
        return this.mPreviewCallback;
    }

    public void init(int i) {
        String str = TAG;
        Log.d(str, "init");
        if (this.mCacheStarted.get()) {
            Log.e(str, "Cache Image already init");
        }
    }

    /* access modifiers changed from: protected */
    public boolean isNeedImage() {
        return this.mCacheStarted.get();
    }

    public boolean needPreviewFrame() {
        return this.mCacheStarted.get();
    }

    public void onPreviewFrame(Image image) {
        if (this.mCacheStarted.get()) {
            this.mWriteLock.lock();
            try {
                if (this.mCachedCount.get() >= 20) {
                    Image image2 = (Image) this.mCachedImages.removeFirst();
                    this.mCachedImageMap.remove(Long.valueOf(image2.getTimestamp()));
                    image2.close();
                    this.mCachedImages.addLast(image);
                    this.mCachedImageMap.put(Long.valueOf(image.getTimestamp()), image);
                    this.mCachedCount.getAndSet(20);
                } else {
                    this.mCachedImages.add(image);
                    this.mCachedImageMap.put(Long.valueOf(image.getTimestamp()), image);
                    this.mCachedCount.getAndIncrement();
                }
            } finally {
                this.mWriteLock.unlock();
            }
        }
    }

    public void onPreviewFrame(PreviewImage previewImage) {
    }

    public void quit() {
        super.quit();
        Log.d(TAG, "quit");
        reset();
    }

    public void reset() {
        String str = TAG;
        Log.d(str, BaseEvent.RESET);
        if (!this.mCacheStarted.get()) {
            Log.d(str, "already reset");
            return;
        }
        if (this.mCacheStarted.compareAndSet(true, false)) {
            this.mSaver.onComplete();
            this.mSaverDisposable.dispose();
            this.mSaver = null;
            this.mSaverDisposable = null;
            this.mWriteLock.lock();
            try {
                Iterator it = this.mCachedImages.iterator();
                while (it.hasNext()) {
                    ((Image) it.next()).close();
                }
                this.mCachedImages.clear();
                this.mCachedImageMap.clear();
                this.mCachedCount.getAndSet(0);
            } finally {
                this.mWriteLock.unlock();
            }
        }
    }

    public void saveAnchorFrameThumbnail(long j, int i, int i2, int[] iArr, String str) {
        if (!this.mCacheStarted.get()) {
            Log.d(TAG, "queue already quit");
            return;
        }
        if (this.mSaver != null) {
            Disposable disposable = this.mSaverDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                FrameInfo frameInfo = new FrameInfo(j, i, i2, iArr, str);
                this.mSaver.onNext(frameInfo);
            }
        }
    }

    public void setAnchorPreviewCallback(AnchorPreviewCallback anchorPreviewCallback) {
        this.mCallback = new WeakReference(anchorPreviewCallback);
    }

    public void startDecode() {
        String str = TAG;
        Log.d(str, "start decode");
        if (this.mCacheStarted.compareAndSet(false, true)) {
            Log.d(str, "cache image start decode success");
        }
    }

    public void stopDecode() {
        super.stopDecode();
    }
}
