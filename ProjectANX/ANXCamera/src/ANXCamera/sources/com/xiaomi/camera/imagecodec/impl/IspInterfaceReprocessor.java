package com.xiaomi.camera.imagecodec.impl;

import android.content.Context;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.util.Size;
import com.android.camera.Util;
import com.xiaomi.camera.imagecodec.BaseReprocessor;
import com.xiaomi.camera.imagecodec.FeatureSetting;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.camera.imagecodec.ImagePool.ImageFormat;
import com.xiaomi.camera.imagecodec.OutputConfiguration;
import com.xiaomi.camera.imagecodec.QueryFeatureSettingParameter;
import com.xiaomi.camera.imagecodec.ReprocessData;
import com.xiaomi.camera.imagecodec.Reprocessor.Singleton;
import com.xiaomi.camera.isp.IspBuffer;
import com.xiaomi.camera.isp.IspInterface;
import com.xiaomi.camera.isp.IspInterfaceIO;
import com.xiaomi.camera.isp.IspInterfaceInfo;
import com.xiaomi.camera.isp.IspStream;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IspInterfaceReprocessor extends BaseReprocessor {
    private static final int MAX_IMAGE_BUFFER_SIZE = 2;
    private static final int MAX_INPUT_STREAM_ID = 100;
    /* access modifiers changed from: private */
    public static final String TAG = "IspInterfaceReprocessor";
    public static final Singleton sInstance = new Singleton() {
        /* access modifiers changed from: protected */
        public IspInterfaceReprocessor create() {
            return new IspInterfaceReprocessor();
        }
    };
    /* access modifiers changed from: private */
    public IspInterface mActiveIspInterface;
    /* access modifiers changed from: private */
    public final Object mCodecLock;
    private Handler mCodecOperationHandler;
    private HandlerThread mCodecOperationThread;
    /* access modifiers changed from: private */
    public ReprocessData mCurrentProcessingData;
    /* access modifiers changed from: private */
    public final Object mDataLock;
    private boolean mInitialized;
    /* access modifiers changed from: private */
    public HashMap mIspInterfaceInfoList;
    private int mMaxJpegSize;
    private int mNextStreamId;
    OnImageAvailableListener mPicListener;
    /* access modifiers changed from: private */
    public HashMap mRaw2YuvStatusMap;
    private Size mRawInputSize;
    /* access modifiers changed from: private */
    public long mReprocessStartTime;
    private Handler mRequestDispatchHandler;
    private HandlerThread mRequestDispatchThread;
    private LinkedList mTaskDataList;
    OnImageAvailableListener mTuningListener;
    private WakeLock mWakeLock;
    OnImageAvailableListener mYuvListener;
    private Size mYuvTuningBufferSize;

    class Raw2YuvStatus {
        public boolean isTuningBufferReady;
        public boolean isYuvBufferReady;

        private Raw2YuvStatus() {
        }

        /* access modifiers changed from: 0000 */
        public boolean isDone() {
            return this.isYuvBufferReady && this.isTuningBufferReady;
        }
    }

    class ReprocessHandler extends Handler {
        private static final int MSG_DESTROY_ENCODER = 2;
        private static final int MSG_REPROCESS_IMAGE = 1;

        ReprocessHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                Log.d(IspInterfaceReprocessor.TAG, "recv MSG_REPROCESS_IMAGE");
                if (IspInterfaceReprocessor.this.checkConditionIsReady()) {
                    IspInterfaceReprocessor.this.reprocessImage();
                }
            } else if (i != 2) {
                super.handleMessage(message);
            } else {
                Log.d(IspInterfaceReprocessor.TAG, "recv MSG_DESTROY_ENCODER");
                synchronized (IspInterfaceReprocessor.this.mCodecLock) {
                    for (IspInterfaceInfo ispInterfaceInfo : IspInterfaceReprocessor.this.mIspInterfaceInfoList.values()) {
                        if (ispInterfaceInfo != null) {
                            ispInterfaceInfo.release();
                        }
                    }
                    IspInterfaceReprocessor.this.mIspInterfaceInfoList.clear();
                    IspInterfaceReprocessor.this.mActiveIspInterface = null;
                }
                IspInterfaceReprocessor.this.releaseWakeLock();
            }
        }
    }

    private IspInterfaceReprocessor() {
        this.mCodecLock = new Object();
        this.mDataLock = new Object();
        this.mTaskDataList = new LinkedList();
        this.mYuvTuningBufferSize = new Size(1280, Util.LIMIT_SURFACE_WIDTH);
        this.mTuningListener = new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                boolean z;
                Image acquireNextImage = imageReader.acquireNextImage();
                long timeStamp = IspInterfaceReprocessor.this.mCurrentProcessingData.getTotalCaptureResult().getTimeStamp();
                acquireNextImage.setTimestamp(timeStamp);
                ImagePool.getInstance().queueImage(acquireNextImage);
                Image image = ImagePool.getInstance().getImage(timeStamp);
                String access$200 = IspInterfaceReprocessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("receive tuning image: ");
                sb.append(image);
                sb.append(" | ");
                sb.append(timeStamp);
                Log.d(access$200, sb.toString());
                ImagePool.getInstance().holdImage(image);
                synchronized (IspInterfaceReprocessor.this.mDataLock) {
                    z = true;
                    IspInterfaceReprocessor.this.mCurrentProcessingData.getResultListener().onTuningImageAvailable(image, IspInterfaceReprocessor.this.mCurrentProcessingData.getImageTag(), true);
                    Log.d(IspInterfaceReprocessor.TAG, String.format("tuning image return for %s. cost=%d", new Object[]{IspInterfaceReprocessor.this.mCurrentProcessingData.getImageTag(), Long.valueOf(System.currentTimeMillis() - IspInterfaceReprocessor.this.mReprocessStartTime)}));
                    Raw2YuvStatus raw2YuvStatus = (Raw2YuvStatus) IspInterfaceReprocessor.this.mRaw2YuvStatusMap.get(IspInterfaceReprocessor.this.mCurrentProcessingData);
                    raw2YuvStatus.isTuningBufferReady = true;
                    if (raw2YuvStatus.isDone()) {
                        IspInterfaceReprocessor.this.releaseReprocessData(IspInterfaceReprocessor.this.mCurrentProcessingData);
                        IspInterfaceReprocessor.this.mRaw2YuvStatusMap.remove(IspInterfaceReprocessor.this.mCurrentProcessingData);
                        IspInterfaceReprocessor.this.mCurrentProcessingData = null;
                    } else {
                        z = false;
                    }
                }
                if (z) {
                    IspInterfaceReprocessor.this.sendReprocessRequest();
                }
            }
        };
        this.mYuvListener = new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                boolean z;
                Image acquireNextImage = imageReader.acquireNextImage();
                long timeStamp = IspInterfaceReprocessor.this.mCurrentProcessingData.getTotalCaptureResult().getTimeStamp();
                acquireNextImage.setTimestamp(timeStamp);
                ImagePool.getInstance().queueImage(acquireNextImage);
                Image image = ImagePool.getInstance().getImage(timeStamp);
                String access$200 = IspInterfaceReprocessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("receive yuv image: ");
                sb.append(image);
                sb.append(" | ");
                sb.append(timeStamp);
                Log.d(access$200, sb.toString());
                ImagePool.getInstance().holdImage(image);
                synchronized (IspInterfaceReprocessor.this.mDataLock) {
                    z = true;
                    IspInterfaceReprocessor.this.mCurrentProcessingData.getResultListener().onYuvAvailable(image, IspInterfaceReprocessor.this.mCurrentProcessingData.getImageTag(), true);
                    Log.d(IspInterfaceReprocessor.TAG, String.format("yuv return for %s. cost=%d", new Object[]{IspInterfaceReprocessor.this.mCurrentProcessingData.getImageTag(), Long.valueOf(System.currentTimeMillis() - IspInterfaceReprocessor.this.mReprocessStartTime)}));
                    Raw2YuvStatus raw2YuvStatus = (Raw2YuvStatus) IspInterfaceReprocessor.this.mRaw2YuvStatusMap.get(IspInterfaceReprocessor.this.mCurrentProcessingData);
                    raw2YuvStatus.isYuvBufferReady = true;
                    if (raw2YuvStatus.isDone()) {
                        IspInterfaceReprocessor.this.releaseReprocessData(IspInterfaceReprocessor.this.mCurrentProcessingData);
                        IspInterfaceReprocessor.this.mRaw2YuvStatusMap.remove(IspInterfaceReprocessor.this.mCurrentProcessingData);
                        IspInterfaceReprocessor.this.mCurrentProcessingData = null;
                    } else {
                        z = false;
                    }
                }
                if (z) {
                    IspInterfaceReprocessor.this.sendReprocessRequest();
                }
            }
        };
        this.mPicListener = new OnImageAvailableListener() {
            public void onImageAvailable(ImageReader imageReader) {
                Image acquireNextImage = imageReader.acquireNextImage();
                String access$200 = IspInterfaceReprocessor.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onImageAvailable: received reprocessed image ");
                sb.append(acquireNextImage);
                Log.d(access$200, sb.toString());
                byte[] access$1100 = IspInterfaceReprocessor.getJpegData(acquireNextImage);
                acquireNextImage.close();
                synchronized (IspInterfaceReprocessor.this.mDataLock) {
                    if (IspInterfaceReprocessor.this.mCurrentProcessingData != null) {
                        IspInterfaceReprocessor.this.releaseReprocessData(IspInterfaceReprocessor.this.mCurrentProcessingData);
                        IspInterfaceReprocessor.this.mCurrentProcessingData.getResultListener().onJpegAvailable(access$1100, IspInterfaceReprocessor.this.mCurrentProcessingData.getImageTag());
                        Log.d(IspInterfaceReprocessor.TAG, String.format("jpeg return for %s. cost=%d", new Object[]{IspInterfaceReprocessor.this.mCurrentProcessingData.getImageTag(), Long.valueOf(System.currentTimeMillis() - IspInterfaceReprocessor.this.mReprocessStartTime)}));
                        IspInterfaceReprocessor.this.mCurrentProcessingData = null;
                    } else {
                        Log.w(IspInterfaceReprocessor.TAG, "onImageAvailable: null task!");
                    }
                }
                IspInterfaceReprocessor.this.sendReprocessRequest();
            }
        };
        this.mIspInterfaceInfoList = new HashMap();
        this.mRaw2YuvStatusMap = new HashMap();
    }

    private void acquireWakeLock() {
        if (!this.mWakeLock.isHeld()) {
            Log.d(TAG, "acquireWakeLock");
            this.mWakeLock.acquire();
        }
    }

    private int align(int i, int i2) {
        return (~(i2 - 1)) & ((i + i2) - 1);
    }

    private void cacheIspInterface(IspInterfaceIO ispInterfaceIO, IspInterfaceInfo ispInterfaceInfo) {
        synchronized (this.mCodecLock) {
            if (!this.mIspInterfaceInfoList.containsKey(ispInterfaceIO)) {
                this.mIspInterfaceInfoList.put(ispInterfaceIO, ispInterfaceInfo);
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001a, code lost:
        if (r1 != null) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        android.util.Log.w(TAG, "checkConditionIsReady: ignore null request!");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0023, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
        r0 = new com.xiaomi.camera.imagecodec.OutputConfiguration(r1.getOutputWidth(), r1.getOutputHeight(), r1.getOutputFormat());
        r3 = (android.media.Image) r1.getMainImage().get(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
        if (32 != r3.getFormat()) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0048, code lost:
        r2 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0049, code lost:
        if (r2 == false) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004b, code lost:
        r4 = r1.getYuvInputWidth();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0050, code lost:
        r4 = r3.getWidth();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0054, code lost:
        if (r2 == false) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0056, code lost:
        r3 = r1.getYuvInputHeight();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005b, code lost:
        r3 = r3.getHeight();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005f, code lost:
        r5 = new android.util.Size(r4, r3);
        r7 = TAG;
        r8 = new java.lang.StringBuilder();
        r8.append("yuvInputSize = ");
        r8.append(r5);
        android.util.Log.d(r7, r8.toString());
        r8 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007f, code lost:
        if (isMFNRSupported() != false) goto L_0x0086;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0081, code lost:
        if (r2 == false) goto L_0x0084;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0084, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0086, code lost:
        r2 = r1.getRawInputWidth();
        r7 = r1.getRawInputHeight();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x008e, code lost:
        if (r2 <= 0) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0090, code lost:
        if (r7 <= 0) goto L_0x00af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0092, code lost:
        r3 = new android.util.Size(r2, r7);
        r2 = TAG;
        r4 = new java.lang.StringBuilder();
        r4.append("rawInputSize = ");
        r4.append(r3);
        android.util.Log.d(r2, r4.toString());
        r2 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00af, code lost:
        r2 = new android.util.Size(r4, r3);
        r3 = TAG;
        r4 = new java.lang.StringBuilder();
        r4.append("override rawInputSize = ");
        r4.append(r2);
        android.util.Log.w(r3, r4.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00ca, code lost:
        r10.mRawInputSize = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00cc, code lost:
        r3 = new com.xiaomi.camera.isp.IspInterfaceIO(r5, r2, r0);
        r0 = getCachedIspInterface(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00d5, code lost:
        if (r0 != null) goto L_0x00e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00d7, code lost:
        r0 = r1.getTotalCaptureResult();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00db, code lost:
        if (r0 == null) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00dd, code lost:
        r8 = r0.getResults();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e1, code lost:
        r0 = createIspInterface(r3, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00e5, code lost:
        r10.mActiveIspInterface = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00e7, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @WorkerThread
    public boolean checkConditionIsReady() {
        synchronized (this.mDataLock) {
            boolean z = false;
            if (this.mCurrentProcessingData != null) {
                Log.d(TAG, "checkConditionIsReady: processor is busy!");
                return false;
            }
            ReprocessData reprocessData = (ReprocessData) this.mTaskDataList.peek();
        }
    }

    @WorkerThread
    private IspInterface createIspInterface(@NonNull IspInterfaceIO ispInterfaceIO, @NonNull Parcelable parcelable) {
        ImageReader imageReader;
        Size yuvInputSize = ispInterfaceIO.getYuvInputSize();
        Size rawInputSize = ispInterfaceIO.getRawInputSize();
        OutputConfiguration picOutputConfiguration = ispInterfaceIO.getPicOutputConfiguration();
        StringBuilder sb = new StringBuilder("createIspInterface>>");
        sb.append(String.format(Locale.ENGLISH, "yuvInput[%dx%d]", new Object[]{Integer.valueOf(yuvInputSize.getWidth()), Integer.valueOf(yuvInputSize.getHeight())}));
        if (rawInputSize != null) {
            sb.append(String.format(Locale.ENGLISH, " rawInput[%dx%d]", new Object[]{Integer.valueOf(rawInputSize.getWidth()), Integer.valueOf(rawInputSize.getHeight())}));
        }
        sb.append(String.format(Locale.ENGLISH, " output[%dx%d@%d]", new Object[]{Integer.valueOf(picOutputConfiguration.getWidth()), Integer.valueOf(picOutputConfiguration.getHeight()), Integer.valueOf(picOutputConfiguration.getFormat())}));
        Log.d(TAG, sb.toString());
        ArrayList arrayList = new ArrayList();
        IspStream ispStream = new IspStream((long) getNextStreamId(), yuvInputSize.getWidth(), yuvInputSize.getHeight(), getRowStride(yuvInputSize.getWidth(), yuvInputSize.getHeight(), 35), 35);
        arrayList.add(ispStream);
        ImageReader initImageReader = initImageReader(picOutputConfiguration.getWidth(), picOutputConfiguration.getHeight(), picOutputConfiguration.getFormat(), this.mPicListener);
        if (rawInputSize != null) {
            int[] rowStride = getRowStride(rawInputSize.getWidth(), rawInputSize.getHeight(), 32);
            OutputConfiguration yuvOutputConfiguration = ispInterfaceIO.getYuvOutputConfiguration();
            IspStream ispStream2 = new IspStream((long) getNextStreamId(), rawInputSize.getWidth(), rawInputSize.getHeight(), rowStride, 32);
            arrayList.add(ispStream2);
            imageReader = initImageReader(yuvOutputConfiguration.getWidth(), yuvOutputConfiguration.getHeight(), 35, this.mYuvListener);
        } else {
            imageReader = null;
        }
        ImageReader initImageReader2 = rawInputSize != null ? initImageReader(this.mYuvTuningBufferSize.getWidth(), this.mYuvTuningBufferSize.getHeight(), IjkMediaPlayer.SDL_FCC_YV12, this.mTuningListener) : null;
        IspInterface create = IspInterface.create(arrayList, initImageReader.getSurface(), imageReader == null ? null : imageReader.getSurface(), initImageReader2 == null ? null : initImageReader2.getSurface(), this.mMaxJpegSize, parcelable);
        IspInterfaceInfo ispInterfaceInfo = new IspInterfaceInfo(create, ispInterfaceIO, initImageReader, imageReader, initImageReader2);
        cacheIspInterface(ispInterfaceIO, ispInterfaceInfo);
        Log.d(TAG, "createIspInterface<<");
        return create;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001b, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private IspInterface getCachedIspInterface(IspInterfaceIO ispInterfaceIO) {
        synchronized (this.mCodecLock) {
            IspInterfaceInfo ispInterfaceInfo = (IspInterfaceInfo) this.mIspInterfaceInfoList.get(ispInterfaceIO);
            if (ispInterfaceInfo != null && ispInterfaceInfo.isValid()) {
                IspInterface ispInterface = ispInterfaceInfo.getIspInterface();
                return ispInterface;
            }
        }
    }

    /* access modifiers changed from: private */
    public static byte[] getJpegData(Image image) {
        Plane[] planes = image.getPlanes();
        if (planes.length <= 0) {
            return null;
        }
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] bArr = new byte[buffer.remaining()];
        buffer.get(bArr);
        return bArr;
    }

    private int getNextStreamId() {
        int i = this.mNextStreamId + 1;
        this.mNextStreamId = i;
        return i % 100;
    }

    private int[] getRowStride(int i, int i2, int i3) {
        Image anEmptyImage = ImagePool.getInstance().getAnEmptyImage(new ImageFormat(i, i2, i3));
        Plane[] planes = anEmptyImage.getPlanes();
        int[] iArr = new int[planes.length];
        for (int i4 = 0; i4 < planes.length; i4++) {
            iArr[i4] = planes[i4].getRowStride();
        }
        anEmptyImage.close();
        return iArr;
    }

    private ImageReader initImageReader(int i, int i2, int i3, OnImageAvailableListener onImageAvailableListener) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initImageReader>>");
        sb.append(i);
        sb.append("x");
        sb.append(i2);
        sb.append("@");
        sb.append(i3);
        Log.d(str, sb.toString());
        ImageReader newInstance = ImageReader.newInstance(i, i2, i3, 2);
        newInstance.setOnImageAvailableListener(onImageAvailableListener, this.mCodecOperationHandler);
        Log.d(TAG, "initImageReader<<");
        return newInstance;
    }

    /* access modifiers changed from: private */
    public void releaseReprocessData(ReprocessData reprocessData) {
        ArrayList mainImage = reprocessData.getMainImage();
        if (mainImage != null) {
            Iterator it = mainImage.iterator();
            while (it.hasNext()) {
                Image image = (Image) it.next();
                image.close();
                ImagePool.getInstance().releaseImage(image);
            }
        }
        if (!reprocessData.isKeepTuningImage()) {
            ArrayList tuningImage = reprocessData.getTuningImage();
            if (tuningImage != null) {
                Iterator it2 = tuningImage.iterator();
                while (it2.hasNext()) {
                    Image image2 = (Image) it2.next();
                    image2.close();
                    ImagePool.getInstance().releaseImage(image2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void releaseWakeLock() {
        if (this.mWakeLock.isHeld()) {
            Log.d(TAG, "releaseWakeLock");
            this.mWakeLock.release();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00c6, code lost:
        r11 = r0.mCurrentProcessingData.getReprocessFuntionType();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00d0, code lost:
        if (com.xiaomi.camera.imagecodec.ReprocessData.REPROCESS_FUNCTION_RAW_MFNR != r11) goto L_0x00d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00d4, code lost:
        if (r6 != 32) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00d6, code lost:
        r14 = 35;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00dc, code lost:
        if (com.xiaomi.camera.imagecodec.ReprocessData.REPROCESS_FUNCTION_RAW_SUPERNIGHT != r11) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00de, code lost:
        if (r6 != 32) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e1, code lost:
        r14 = r0.mCurrentProcessingData.getOutputFormat();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e8, code lost:
        r5 = new com.xiaomi.camera.isp.IspRequest((int) r2.getFrameNumber(), r0.mCurrentProcessingData.isFrontMirror() ? 1 : 0, r2.getResults(), r9, r0.mCurrentProcessingData.getCropRegion(), r11);
        r1 = TAG;
        r3 = new java.lang.StringBuilder();
        r3.append("reprocessImage: requestFrameNo=");
        r3.append(r2.getFrameNumber());
        android.util.Log.d(r1, r3.toString());
        r0.mReprocessStartTime = java.lang.System.currentTimeMillis();
        r7 = r0.mCodecOperationHandler;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0125, code lost:
        if (r7 == null) goto L_0x0136;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0127, code lost:
        r3 = r12;
        r4 = r13;
        r5 = r14;
        r6 = r5;
        r1 = new com.xiaomi.camera.imagecodec.impl.IspInterfaceReprocessor.AnonymousClass2(r17);
        r7.post(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0136, code lost:
        android.util.Log.d(TAG, "reprocessImage<<");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x013d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @WorkerThread
    public void reprocessImage() {
        IspInterface ispInterface;
        int width;
        int height;
        int format;
        Log.d(TAG, "reprocessImage>>");
        synchronized (this.mDataLock) {
            this.mCurrentProcessingData = (ReprocessData) this.mTaskDataList.poll();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("reprocessImage: tag=");
            sb.append(this.mCurrentProcessingData.getImageTag());
            Log.d(str, sb.toString());
            ICustomCaptureResult totalCaptureResult = this.mCurrentProcessingData.getTotalCaptureResult();
            if (totalCaptureResult == null) {
                Log.wtf(TAG, "reprocessImage<<null metadata!");
                return;
            }
            ArrayList mainImage = this.mCurrentProcessingData.getMainImage();
            int size = mainImage.size();
            if (size > 0) {
                ArrayList arrayList = new ArrayList(size);
                Image image = (Image) mainImage.get(0);
                int format2 = image.getFormat();
                boolean z = 32 == format2;
                if (z) {
                    this.mRaw2YuvStatusMap.put(this.mCurrentProcessingData, new Raw2YuvStatus());
                    ispInterface = this.mActiveIspInterface;
                    width = this.mRawInputSize.getWidth();
                    height = this.mRawInputSize.getHeight();
                    format = image.getFormat();
                } else {
                    ispInterface = this.mActiveIspInterface;
                    width = image.getWidth();
                    height = image.getHeight();
                    format = image.getFormat();
                }
                long inputStreamId = ispInterface.getInputStreamId(width, height, format);
                ArrayList tuningImage = this.mCurrentProcessingData.getTuningImage();
                for (int i = 0; i < size; i++) {
                    arrayList.add(new IspBuffer(inputStreamId, (Image) mainImage.get(i), tuningImage == null ? null : (Image) tuningImage.get(i)));
                }
            } else {
                Log.w(TAG, "reprocessImage<<null input buffer!");
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0044, code lost:
        if (r5.mRequestDispatchHandler.hasMessages(1) == false) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0046, code lost:
        android.util.Log.d(TAG, "sendReprocessRequest: BUSY");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004e, code lost:
        android.util.Log.d(TAG, "sendReprocessRequest: send MSG_REPROCESS_IMAGE");
        r5.mRequestDispatchHandler.sendEmptyMessageDelayed(1, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendReprocessRequest() {
        Log.i(TAG, "=============================================================");
        if (!this.mInitialized) {
            Log.w(TAG, "sendReprocessRequest: NOT initialized!");
            return;
        }
        synchronized (this.mDataLock) {
            if (this.mTaskDataList.isEmpty()) {
                Log.d(TAG, "sendReprocessRequest: idle. Try to close device 30s later.");
                this.mRequestDispatchHandler.sendEmptyMessageDelayed(2, 30000);
            } else if (this.mRequestDispatchHandler.hasMessages(2)) {
                this.mRequestDispatchHandler.removeMessages(2);
            }
        }
    }

    public void customize(HashMap hashMap) {
        Boolean bool = (Boolean) hashMap.get(Integer.valueOf(101));
        if (bool != null) {
            this.mIsMFNRSupported = bool.booleanValue();
        }
        Integer num = (Integer) hashMap.get(Integer.valueOf(102));
        if (num != null && num.intValue() > 0) {
            this.mMaxJpegSize = num.intValue();
        }
        Size size = (Size) hashMap.get(Integer.valueOf(103));
        if (size != null) {
            this.mYuvTuningBufferSize = size;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("customize: maxJpegSize=");
        sb.append(num);
        sb.append(", yuvTuningBufferSize=");
        sb.append(size);
        sb.append(", mfnrOn=");
        sb.append(this.mIsMFNRSupported);
        Log.d(str, sb.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r0 = r4.mIspInterfaceInfoList.values().iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0028, code lost:
        if (r0.hasNext() == false) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002a, code lost:
        r3 = (com.xiaomi.camera.isp.IspInterfaceInfo) r0.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r3 == null) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        r3.release();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
        r4.mIspInterfaceInfoList.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003b, code lost:
        monitor-exit(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        r0 = r4.mCodecOperationThread;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r0 == null) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0040, code lost:
        r0.quitSafely();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r4.mCodecOperationThread.join();
        r4.mCodecOperationThread = null;
        r4.mCodecOperationHandler = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004e, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
        r2 = r4.mCodecLock;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void deInit() {
        Log.d(TAG, "deInit>>");
        synchronized (this.mDataLock) {
            if (this.mInitialized) {
                this.mInitialized = false;
                this.mCurrentProcessingData = null;
            } else {
                return;
            }
        }
        HandlerThread handlerThread = this.mRequestDispatchThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            try {
                this.mRequestDispatchThread.join();
                this.mRequestDispatchThread = null;
                this.mRequestDispatchHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "deInit<<");
        Log.d(TAG, "deInit<<");
    }

    public void init(Context context) {
        Log.d(TAG, "init>>");
        synchronized (this.mDataLock) {
            if (!this.mInitialized) {
                this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, TAG);
                this.mWakeLock.setReferenceCounted(false);
                this.mCodecOperationThread = new HandlerThread("IspHandlerThread");
                this.mCodecOperationThread.start();
                this.mCodecOperationHandler = new Handler(this.mCodecOperationThread.getLooper());
                this.mRequestDispatchThread = new HandlerThread("RequestDispatcher");
                this.mRequestDispatchThread.start();
                this.mRequestDispatchHandler = new ReprocessHandler(this.mRequestDispatchThread.getLooper());
                this.mInitialized = true;
            }
        }
        Log.d(TAG, "init<<");
    }

    public FeatureSetting queryFeatureSetting(@NonNull IspInterfaceIO ispInterfaceIO, @NonNull Parcelable parcelable, @NonNull QueryFeatureSettingParameter queryFeatureSettingParameter, boolean z) {
        IspInterface cachedIspInterface = getCachedIspInterface(ispInterfaceIO);
        if (cachedIspInterface == null) {
            cachedIspInterface = createIspInterface(ispInterfaceIO, parcelable);
        }
        if (z) {
            return cachedIspInterface.queryFeatureSetting(queryFeatureSettingParameter);
        }
        return null;
    }

    public void setOutputPictureSpec(int i, int i2, int i3) {
    }

    public void submit(ReprocessData reprocessData) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("submit: ");
        sb.append(reprocessData.getImageTag());
        Log.d(str, sb.toString());
        if (!this.mInitialized) {
            throw new RuntimeException("NOT initialized. Call init() first!");
        } else if (reprocessData.getResultListener() == null) {
            releaseReprocessData(reprocessData);
            Log.d(TAG, "submit: drop this request due to no callback was provided!");
        } else {
            ArrayList mainImage = reprocessData.getMainImage();
            int size = mainImage == null ? 0 : mainImage.size();
            if (size == 0) {
                Log.w(TAG, "submit: empty data list");
                return;
            }
            acquireWakeLock();
            if (!reprocessData.isImageFromPool()) {
                int i = size + 1;
                ImageFormat imageQueueKey = ImagePool.getInstance().toImageQueueKey((Image) mainImage.get(0));
                while (ImagePool.getInstance().isImageQueueFull(imageQueueKey, i)) {
                    Log.w(TAG, "submit: wait main image pool>>");
                    ImagePool.getInstance().waitIfImageQueueFull(imageQueueKey, i, 0);
                    Log.w(TAG, "submit: wait main image pool<<");
                }
                ArrayList arrayList = new ArrayList(size);
                for (int i2 = 0; i2 < size; i2++) {
                    arrayList.add(queueImageToPool((Image) mainImage.get(i2)));
                }
                reprocessData.setMainImage(arrayList);
                Iterator it = mainImage.iterator();
                while (it.hasNext()) {
                    ((Image) it.next()).close();
                }
                reprocessData.getDataStatusCallback().onImageClosed(mainImage);
            }
            if (!reprocessData.isTuningImageFromPool() && !reprocessData.isKeepTuningImage()) {
                int i3 = size + 1;
                ArrayList tuningImage = reprocessData.getTuningImage();
                ImageFormat imageQueueKey2 = ImagePool.getInstance().toImageQueueKey((Image) tuningImage.get(0));
                while (ImagePool.getInstance().isImageQueueFull(imageQueueKey2, i3)) {
                    Log.w(TAG, "submit: wait tuning image pool>>");
                    ImagePool.getInstance().waitIfImageQueueFull(imageQueueKey2, i3, 0);
                    Log.w(TAG, "submit: wait tuning image pool<<");
                }
                ArrayList arrayList2 = new ArrayList(size);
                for (int i4 = 0; i4 < size; i4++) {
                    arrayList2.add(queueImageToPool((Image) tuningImage.get(i4)));
                }
                reprocessData.setTuningImage(arrayList2);
                Iterator it2 = tuningImage.iterator();
                while (it2.hasNext()) {
                    ((Image) it2.next()).close();
                }
                reprocessData.getDataStatusCallback().onImageClosed(tuningImage);
            }
            synchronized (this.mDataLock) {
                this.mTaskDataList.add(reprocessData);
            }
            sendReprocessRequest();
        }
    }
}
