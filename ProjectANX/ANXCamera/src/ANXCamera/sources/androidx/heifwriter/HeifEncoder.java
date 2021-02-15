package androidx.heifwriter;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.Image;
import android.media.Image.Plane;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.CodecException;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.EncoderCapabilities;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Range;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.text.ExtraTextUtils;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

public final class HeifEncoder implements AutoCloseable, OnFrameAvailableListener {
    private static final boolean DEBUG = false;
    private static final int GRID_HEIGHT = 512;
    private static final int GRID_WIDTH = 512;
    private static final int INPUT_BUFFER_POOL_SIZE = 2;
    public static final int INPUT_MODE_BITMAP = 2;
    public static final int INPUT_MODE_BUFFER = 0;
    public static final int INPUT_MODE_SURFACE = 1;
    private static final double MAX_COMPRESS_RATIO = 0.25d;
    private static final String TAG = "HeifEncoder";
    private static final MediaCodecList sMCL = new MediaCodecList(0);
    final Callback mCallback;
    final ArrayList mCodecInputBuffers = new ArrayList();
    private ByteBuffer mCurrentBuffer;
    private final Rect mDstRect;
    SurfaceEOSTracker mEOSTracker;
    private final ArrayList mEmptyBuffers = new ArrayList();
    MediaCodec mEncoder;
    private EglWindowSurface mEncoderEglSurface;
    private Surface mEncoderSurface;
    private final ArrayList mFilledBuffers = new ArrayList();
    final int mGridCols;
    final int mGridHeight;
    final int mGridRows;
    final int mGridWidth;
    final Handler mHandler;
    private final HandlerThread mHandlerThread;
    final int mHeight;
    boolean mInputEOS;
    private int mInputIndex;
    private final int mInputMode;
    private Surface mInputSurface;
    private SurfaceTexture mInputTexture;
    private final int mNumTiles;
    private EglRectBlt mRectBlt;
    private final Rect mSrcRect;
    private final AtomicBoolean mStopping = new AtomicBoolean(false);
    private int mTextureId;
    private final float[] mTmpMatrix = new float[16];
    final boolean mUseGrid;
    final int mWidth;

    public abstract class Callback {
        public abstract void onComplete(@NonNull HeifEncoder heifEncoder);

        public abstract void onDrainOutputBuffer(@NonNull HeifEncoder heifEncoder, @NonNull ByteBuffer byteBuffer);

        public abstract void onError(@NonNull HeifEncoder heifEncoder, @NonNull CodecException codecException);

        public abstract void onOutputFormatChanged(@NonNull HeifEncoder heifEncoder, @NonNull MediaFormat mediaFormat);
    }

    class EncoderCallback extends android.media.MediaCodec.Callback {
        private boolean mOutputEOS;

        EncoderCallback() {
        }

        private void stopAndNotify(@Nullable CodecException codecException) {
            HeifEncoder.this.stopInternal();
            HeifEncoder heifEncoder = HeifEncoder.this;
            if (codecException == null) {
                heifEncoder.mCallback.onComplete(heifEncoder);
            } else {
                heifEncoder.mCallback.onError(heifEncoder, codecException);
            }
        }

        public void onError(MediaCodec mediaCodec, CodecException codecException) {
            if (mediaCodec == HeifEncoder.this.mEncoder) {
                StringBuilder sb = new StringBuilder();
                sb.append("onError: ");
                sb.append(codecException);
                Log.e(HeifEncoder.TAG, sb.toString());
                stopAndNotify(codecException);
            }
        }

        public void onInputBufferAvailable(MediaCodec mediaCodec, int i) {
            HeifEncoder heifEncoder = HeifEncoder.this;
            if (mediaCodec == heifEncoder.mEncoder && !heifEncoder.mInputEOS) {
                heifEncoder.mCodecInputBuffers.add(Integer.valueOf(i));
                HeifEncoder.this.maybeCopyOneTileYUV();
            }
        }

        public void onOutputBufferAvailable(MediaCodec mediaCodec, int i, BufferInfo bufferInfo) {
            if (mediaCodec == HeifEncoder.this.mEncoder && !this.mOutputEOS) {
                if (bufferInfo.size > 0 && (bufferInfo.flags & 2) == 0) {
                    ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(i);
                    outputBuffer.position(bufferInfo.offset);
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    SurfaceEOSTracker surfaceEOSTracker = HeifEncoder.this.mEOSTracker;
                    if (surfaceEOSTracker != null) {
                        surfaceEOSTracker.updateLastOutputTime(bufferInfo.presentationTimeUs);
                    }
                    HeifEncoder heifEncoder = HeifEncoder.this;
                    heifEncoder.mCallback.onDrainOutputBuffer(heifEncoder, outputBuffer);
                }
                this.mOutputEOS = ((bufferInfo.flags & 4) != 0) | this.mOutputEOS;
                mediaCodec.releaseOutputBuffer(i, false);
                if (this.mOutputEOS) {
                    stopAndNotify(null);
                }
            }
        }

        public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
            if (mediaCodec == HeifEncoder.this.mEncoder) {
                String str = IMediaFormat.KEY_MIME;
                String str2 = "image/vnd.android.heic";
                if (!str2.equals(mediaFormat.getString(str))) {
                    mediaFormat.setString(str, str2);
                    mediaFormat.setInteger("width", HeifEncoder.this.mWidth);
                    mediaFormat.setInteger("height", HeifEncoder.this.mHeight);
                    HeifEncoder heifEncoder = HeifEncoder.this;
                    if (heifEncoder.mUseGrid) {
                        mediaFormat.setInteger("tile-width", heifEncoder.mGridWidth);
                        mediaFormat.setInteger("tile-height", HeifEncoder.this.mGridHeight);
                        mediaFormat.setInteger("grid-rows", HeifEncoder.this.mGridRows);
                        mediaFormat.setInteger("grid-cols", HeifEncoder.this.mGridCols);
                    }
                }
                HeifEncoder heifEncoder2 = HeifEncoder.this;
                heifEncoder2.mCallback.onOutputFormatChanged(heifEncoder2, mediaFormat);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface InputMode {
    }

    class SurfaceEOSTracker {
        private static final boolean DEBUG_EOS = false;
        final boolean mCopyTiles;
        long mEncoderEOSTimeUs = -1;
        long mInputEOSTimeNs = -1;
        long mLastEncoderTimeUs = -1;
        long mLastInputTimeNs = -1;
        long mLastOutputTimeUs = -1;
        boolean mSignaled;

        SurfaceEOSTracker(boolean z) {
            this.mCopyTiles = z;
        }

        private void doSignalEOSLocked() {
            HeifEncoder.this.mHandler.post(new Runnable() {
                public void run() {
                    MediaCodec mediaCodec = HeifEncoder.this.mEncoder;
                    if (mediaCodec != null) {
                        mediaCodec.signalEndOfInputStream();
                    }
                }
            });
            this.mSignaled = true;
        }

        private void updateEOSLocked() {
            if (!this.mSignaled) {
                if (this.mEncoderEOSTimeUs < 0) {
                    long j = this.mInputEOSTimeNs;
                    if (j >= 0 && this.mLastInputTimeNs >= j) {
                        long j2 = this.mLastEncoderTimeUs;
                        if (j2 < 0) {
                            doSignalEOSLocked();
                            return;
                        }
                        this.mEncoderEOSTimeUs = j2;
                    }
                }
                long j3 = this.mEncoderEOSTimeUs;
                if (j3 >= 0 && j3 <= this.mLastOutputTimeUs) {
                    doSignalEOSLocked();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public synchronized void updateInputEOSTime(long j) {
            if (this.mCopyTiles) {
                if (this.mInputEOSTimeNs < 0) {
                    this.mInputEOSTimeNs = j;
                }
            } else if (this.mEncoderEOSTimeUs < 0) {
                this.mEncoderEOSTimeUs = j / 1000;
            }
            updateEOSLocked();
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:10:0x0015  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized boolean updateLastInputAndEncoderTime(long j, long j2) {
            boolean z;
            if (this.mInputEOSTimeNs >= 0) {
                if (j > this.mInputEOSTimeNs) {
                    z = false;
                    if (z) {
                        this.mLastEncoderTimeUs = j2;
                    }
                    this.mLastInputTimeNs = j;
                    updateEOSLocked();
                }
            }
            z = true;
            if (z) {
            }
            this.mLastInputTimeNs = j;
            updateEOSLocked();
            return z;
        }

        /* access modifiers changed from: 0000 */
        public synchronized void updateLastOutputTime(long j) {
            this.mLastOutputTimeUs = j;
            updateEOSLocked();
        }
    }

    public HeifEncoder(int i, int i2, boolean z, int i3, int i4, @Nullable Handler handler, @NonNull Callback callback) {
        CodecCapabilities codecCapabilities;
        boolean z2;
        int i5;
        int i6;
        int intValue;
        String str;
        int i7 = i;
        int i8 = i2;
        int i9 = i3;
        int i10 = i4;
        String str2 = "video/hevc";
        String str3 = "image/vnd.android.heic";
        if (i7 < 0 || i8 < 0 || i9 < 0 || i9 > 100) {
            throw new IllegalArgumentException("invalid encoder inputs");
        }
        int i11 = 512;
        int i12 = 1;
        boolean z3 = i7 > 512 || i8 > 512;
        boolean z4 = z & z3;
        try {
            this.mEncoder = MediaCodec.createEncoderByType(str3);
            CodecCapabilities capabilitiesForType = this.mEncoder.getCodecInfo().getCapabilitiesForType(str3);
            if (capabilitiesForType.getVideoCapabilities().isSizeSupported(i7, i8)) {
                codecCapabilities = capabilitiesForType;
                z2 = true;
                this.mInputMode = i10;
                this.mCallback = callback;
                Looper looper = handler != null ? handler.getLooper() : null;
                if (looper == null) {
                    this.mHandlerThread = new HandlerThread("HeifEncoderThread", -2);
                    this.mHandlerThread.start();
                    looper = this.mHandlerThread.getLooper();
                } else {
                    this.mHandlerThread = null;
                }
                this.mHandler = new Handler(looper);
                boolean z5 = i10 == 1 || i10 == 2;
                int i13 = z5 ? 2130708361 : 2135033992;
                boolean z6 = (z4 && !z2) || i10 == 2;
                this.mWidth = i7;
                this.mHeight = i8;
                this.mUseGrid = z4;
                if (z4) {
                    i6 = ((i8 + 512) - 1) / 512;
                    i12 = ((i7 + 512) - 1) / 512;
                    i5 = 512;
                } else {
                    i11 = this.mWidth;
                    i5 = this.mHeight;
                    i6 = 1;
                }
                MediaFormat createVideoFormat = z2 ? MediaFormat.createVideoFormat(str3, this.mWidth, this.mHeight) : MediaFormat.createVideoFormat(str2, i11, i5);
                if (z4) {
                    createVideoFormat.setInteger("tile-width", i11);
                    createVideoFormat.setInteger("tile-height", i5);
                    createVideoFormat.setInteger("grid-cols", i12);
                    createVideoFormat.setInteger("grid-rows", i6);
                }
                if (z2) {
                    this.mGridWidth = i7;
                    this.mGridHeight = i8;
                    this.mGridRows = 1;
                    this.mGridCols = 1;
                } else {
                    this.mGridWidth = i11;
                    this.mGridHeight = i5;
                    this.mGridRows = i6;
                    this.mGridCols = i12;
                }
                this.mNumTiles = this.mGridRows * this.mGridCols;
                createVideoFormat.setInteger("i-frame-interval", 0);
                createVideoFormat.setInteger("color-format", i13);
                createVideoFormat.setInteger("frame-rate", this.mNumTiles);
                createVideoFormat.setInteger("operating-rate", this.mNumTiles > 1 ? 120 : 30);
                String str4 = TAG;
                if (z5 && !z6) {
                    Log.d(str4, "Setting fixed pts gap");
                    createVideoFormat.setLong("max-pts-gap-to-encoder", -1000000);
                }
                EncoderCapabilities encoderCapabilities = codecCapabilities.getEncoderCapabilities();
                String str5 = "bitrate-mode";
                if (encoderCapabilities.isBitrateModeSupported(0)) {
                    Log.d(str4, "Setting bitrate mode to constant quality");
                    Range qualityRange = encoderCapabilities.getQualityRange();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Quality range: ");
                    sb.append(qualityRange);
                    Log.d(str4, sb.toString());
                    createVideoFormat.setInteger(str5, 0);
                    intValue = (int) (((double) ((Integer) qualityRange.getLower()).intValue()) + (((double) ((((Integer) qualityRange.getUpper()).intValue() - ((Integer) qualityRange.getLower()).intValue()) * i3)) / 100.0d));
                    str = "quality";
                } else {
                    int i14 = i3;
                    if (encoderCapabilities.isBitrateModeSupported(2)) {
                        Log.d(str4, "Setting bitrate mode to constant bitrate");
                        createVideoFormat.setInteger(str5, 2);
                    } else {
                        Log.d(str4, "Setting bitrate mode to variable bitrate");
                        createVideoFormat.setInteger(str5, 1);
                    }
                    intValue = ((Integer) codecCapabilities.getVideoCapabilities().getBitrateRange().clamp(Integer.valueOf((int) (((((((double) (i7 * i8)) * 1.5d) * 8.0d) * MAX_COMPRESS_RATIO) * ((double) i14)) / 100.0d)))).intValue();
                    str = IjkMediaMeta.IJKM_KEY_BITRATE;
                }
                createVideoFormat.setInteger(str, intValue);
                this.mEncoder.setCallback(new EncoderCallback(), this.mHandler);
                this.mEncoder.configure(createVideoFormat, null, null, 1);
                if (z5) {
                    this.mEncoderSurface = this.mEncoder.createInputSurface();
                    this.mEOSTracker = new SurfaceEOSTracker(z6);
                    if (z6) {
                        this.mEncoderEglSurface = new EglWindowSurface(this.mEncoderSurface);
                        this.mEncoderEglSurface.makeCurrent();
                        int i15 = i4;
                        this.mRectBlt = new EglRectBlt(new Texture2dProgram(i15 == 2 ? 0 : 1), this.mWidth, this.mHeight);
                        this.mTextureId = this.mRectBlt.createTextureObject();
                        if (i15 == 1) {
                            this.mInputTexture = new SurfaceTexture(this.mTextureId, true);
                            this.mInputTexture.setOnFrameAvailableListener(this);
                            this.mInputTexture.setDefaultBufferSize(this.mWidth, this.mHeight);
                            this.mInputSurface = new Surface(this.mInputTexture);
                        }
                        this.mEncoderEglSurface.makeUnCurrent();
                    } else {
                        this.mInputSurface = this.mEncoderSurface;
                    }
                } else {
                    for (int i16 = 0; i16 < 2; i16++) {
                        this.mEmptyBuffers.add(ByteBuffer.allocateDirect(((this.mWidth * this.mHeight) * 3) / 2));
                    }
                }
                this.mDstRect = new Rect(0, 0, this.mGridWidth, this.mGridHeight);
                this.mSrcRect = new Rect();
                return;
            }
            this.mEncoder.release();
            this.mEncoder = null;
            throw new Exception();
        } catch (Exception unused) {
            this.mEncoder = MediaCodec.createByCodecName(findHevcFallback());
            CodecCapabilities capabilitiesForType2 = this.mEncoder.getCodecInfo().getCapabilitiesForType(str2);
            z4 |= !capabilitiesForType2.getVideoCapabilities().isSizeSupported(i7, i8);
            codecCapabilities = capabilitiesForType2;
            z2 = false;
        }
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0003 */
    /* JADX WARNING: Removed duplicated region for block: B:2:0x0003 A[LOOP:0: B:2:0x0003->B:20:0x0003, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ByteBuffer acquireEmptyBuffer() {
        ByteBuffer byteBuffer;
        synchronized (this.mEmptyBuffers) {
            while (!this.mInputEOS && this.mEmptyBuffers.isEmpty()) {
                this.mEmptyBuffers.wait();
            }
            byteBuffer = this.mInputEOS ? null : (ByteBuffer) this.mEmptyBuffers.remove(0);
        }
        return byteBuffer;
    }

    private void addYuvBufferInternal(@Nullable byte[] bArr) {
        ByteBuffer acquireEmptyBuffer = acquireEmptyBuffer();
        if (acquireEmptyBuffer != null) {
            acquireEmptyBuffer.clear();
            if (bArr != null) {
                acquireEmptyBuffer.put(bArr);
            }
            acquireEmptyBuffer.flip();
            synchronized (this.mFilledBuffers) {
                this.mFilledBuffers.add(acquireEmptyBuffer);
            }
            this.mHandler.post(new Runnable() {
                public void run() {
                    HeifEncoder.this.maybeCopyOneTileYUV();
                }
            });
        }
    }

    private long computePresentationTime(int i) {
        return ((((long) i) * ExtraTextUtils.MB) / ((long) this.mNumTiles)) + 132;
    }

    private static void copyOneTileYUV(ByteBuffer byteBuffer, Image image, int i, int i2, Rect rect, Rect rect2) {
        int i3;
        int i4;
        Rect rect3 = rect;
        Rect rect4 = rect2;
        if (rect.width() == rect2.width() && rect.height() == rect2.height()) {
            if (i % 2 == 0 && i2 % 2 == 0) {
                int i5 = 2;
                if (rect3.left % 2 == 0 && rect3.top % 2 == 0 && rect3.right % 2 == 0 && rect3.bottom % 2 == 0 && rect4.left % 2 == 0 && rect4.top % 2 == 0 && rect4.right % 2 == 0 && rect4.bottom % 2 == 0) {
                    Plane[] planes = image.getPlanes();
                    int i6 = 0;
                    while (i6 < planes.length) {
                        ByteBuffer buffer = planes[i6].getBuffer();
                        int pixelStride = planes[i6].getPixelStride();
                        int min = Math.min(rect.width(), i - rect3.left);
                        int min2 = Math.min(rect.height(), i2 - rect3.top);
                        if (i6 > 0) {
                            i4 = ((i * i2) * (i6 + 3)) / 4;
                            i3 = i5;
                        } else {
                            i3 = 1;
                            i4 = 0;
                        }
                        for (int i7 = 0; i7 < min2 / i3; i7++) {
                            byteBuffer.position(((((rect3.top / i3) + i7) * i) / i3) + i4 + (rect3.left / i3));
                            buffer.position((((rect4.top / i3) + i7) * planes[i6].getRowStride()) + ((rect4.left * pixelStride) / i3));
                            int i8 = 0;
                            while (true) {
                                int i9 = min / i3;
                                if (i8 >= i9) {
                                    break;
                                }
                                buffer.put(byteBuffer.get());
                                if (pixelStride > 1 && i8 != i9 - 1) {
                                    buffer.position((buffer.position() + pixelStride) - 1);
                                }
                                i8++;
                            }
                        }
                        ByteBuffer byteBuffer2 = byteBuffer;
                        i6++;
                        i5 = 2;
                    }
                    return;
                }
            }
            throw new IllegalArgumentException("src or dst are not aligned!");
        }
        throw new IllegalArgumentException("src and dst rect size are different!");
    }

    private void copyTilesGL() {
        GLES20.glViewport(0, 0, this.mGridWidth, this.mGridHeight);
        for (int i = 0; i < this.mGridRows; i++) {
            int i2 = 0;
            while (i2 < this.mGridCols) {
                int i3 = this.mGridWidth;
                int i4 = i2 * i3;
                int i5 = this.mGridHeight;
                int i6 = i * i5;
                this.mSrcRect.set(i4, i6, i3 + i4, i5 + i6);
                try {
                    this.mRectBlt.copyRect(this.mTextureId, Texture2dProgram.V_FLIP_MATRIX, this.mSrcRect);
                    EglWindowSurface eglWindowSurface = this.mEncoderEglSurface;
                    int i7 = this.mInputIndex;
                    this.mInputIndex = i7 + 1;
                    eglWindowSurface.setPresentationTime(computePresentationTime(i7) * 1000);
                    this.mEncoderEglSurface.swapBuffers();
                    i2++;
                } catch (RuntimeException e) {
                    if (!this.mStopping.get()) {
                        throw e;
                    }
                    return;
                }
            }
        }
    }

    private String findHevcFallback() {
        MediaCodecInfo[] codecInfos;
        String str = null;
        String str2 = null;
        for (MediaCodecInfo mediaCodecInfo : sMCL.getCodecInfos()) {
            if (mediaCodecInfo.isEncoder()) {
                try {
                    CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType("video/hevc");
                    if (capabilitiesForType.getVideoCapabilities().isSizeSupported(512, 512)) {
                        if (capabilitiesForType.getEncoderCapabilities().isBitrateModeSupported(0)) {
                            if (mediaCodecInfo.isHardwareAccelerated()) {
                                return mediaCodecInfo.getName();
                            }
                            if (str == null) {
                                str = mediaCodecInfo.getName();
                            }
                        }
                        if (str2 == null) {
                            str2 = mediaCodecInfo.getName();
                        }
                    } else {
                        continue;
                    }
                } catch (IllegalArgumentException unused) {
                    continue;
                }
            }
        }
        if (str == null) {
            str = str2;
        }
        return str;
    }

    private ByteBuffer getCurrentBuffer() {
        if (!this.mInputEOS && this.mCurrentBuffer == null) {
            synchronized (this.mFilledBuffers) {
                this.mCurrentBuffer = this.mFilledBuffers.isEmpty() ? null : (ByteBuffer) this.mFilledBuffers.remove(0);
            }
        }
        if (this.mInputEOS) {
            return null;
        }
        return this.mCurrentBuffer;
    }

    private void returnEmptyBufferAndNotify(boolean z) {
        synchronized (this.mEmptyBuffers) {
            this.mInputEOS = z | this.mInputEOS;
            this.mEmptyBuffers.add(this.mCurrentBuffer);
            this.mEmptyBuffers.notifyAll();
        }
        this.mCurrentBuffer = null;
    }

    public void addBitmap(@NonNull Bitmap bitmap) {
        if (this.mInputMode != 2) {
            throw new IllegalStateException("addBitmap is only allowed in bitmap input mode");
        } else if (this.mEOSTracker.updateLastInputAndEncoderTime(computePresentationTime(this.mInputIndex) * 1000, computePresentationTime((this.mInputIndex + this.mNumTiles) - 1))) {
            synchronized (this) {
                if (this.mEncoderEglSurface != null) {
                    this.mEncoderEglSurface.makeCurrent();
                    this.mRectBlt.loadTexture(this.mTextureId, bitmap);
                    copyTilesGL();
                    this.mEncoderEglSurface.makeUnCurrent();
                }
            }
        }
    }

    public void addYuvBuffer(int i, @NonNull byte[] bArr) {
        if (this.mInputMode != 0) {
            throw new IllegalStateException("addYuvBuffer is only allowed in buffer input mode");
        } else if (bArr == null || bArr.length != ((this.mWidth * this.mHeight) * 3) / 2) {
            throw new IllegalArgumentException("invalid data");
        } else {
            addYuvBufferInternal(bArr);
        }
    }

    public void close() {
        synchronized (this.mEmptyBuffers) {
            this.mInputEOS = true;
            this.mEmptyBuffers.notifyAll();
        }
        this.mHandler.postAtFrontOfQueue(new Runnable() {
            public void run() {
                HeifEncoder.this.stopInternal();
            }
        });
    }

    @NonNull
    public Surface getInputSurface() {
        if (this.mInputMode == 1) {
            return this.mInputSurface;
        }
        throw new IllegalStateException("getInputSurface is only allowed in surface input mode");
    }

    /* access modifiers changed from: 0000 */
    public void maybeCopyOneTileYUV() {
        while (true) {
            ByteBuffer currentBuffer = getCurrentBuffer();
            if (currentBuffer != null && !this.mCodecInputBuffers.isEmpty()) {
                int i = 0;
                int intValue = ((Integer) this.mCodecInputBuffers.remove(0)).intValue();
                boolean z = this.mInputIndex % this.mNumTiles == 0 && currentBuffer.remaining() == 0;
                if (!z) {
                    Image inputImage = this.mEncoder.getInputImage(intValue);
                    int i2 = this.mGridWidth;
                    int i3 = this.mInputIndex;
                    int i4 = this.mGridCols;
                    int i5 = (i3 % i4) * i2;
                    int i6 = this.mGridHeight;
                    int i7 = ((i3 / i4) % this.mGridRows) * i6;
                    this.mSrcRect.set(i5, i7, i2 + i5, i6 + i7);
                    copyOneTileYUV(currentBuffer, inputImage, this.mWidth, this.mHeight, this.mSrcRect, this.mDstRect);
                }
                MediaCodec mediaCodec = this.mEncoder;
                int capacity = z ? 0 : mediaCodec.getInputBuffer(intValue).capacity();
                int i8 = this.mInputIndex;
                this.mInputIndex = i8 + 1;
                long computePresentationTime = computePresentationTime(i8);
                if (z) {
                    i = 4;
                }
                mediaCodec.queueInputBuffer(intValue, 0, capacity, computePresentationTime, i);
                if (z || this.mInputIndex % this.mNumTiles == 0) {
                    returnEmptyBufferAndNotify(z);
                }
            } else {
                return;
            }
        }
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this) {
            if (this.mEncoderEglSurface != null) {
                this.mEncoderEglSurface.makeCurrent();
                surfaceTexture.updateTexImage();
                surfaceTexture.getTransformMatrix(this.mTmpMatrix);
                if (this.mEOSTracker.updateLastInputAndEncoderTime(surfaceTexture.getTimestamp(), computePresentationTime((this.mInputIndex + this.mNumTiles) - 1))) {
                    copyTilesGL();
                }
                surfaceTexture.releaseTexImage();
                this.mEncoderEglSurface.makeUnCurrent();
            }
        }
    }

    public void setEndOfInputStreamTimestamp(long j) {
        if (this.mInputMode == 1) {
            SurfaceEOSTracker surfaceEOSTracker = this.mEOSTracker;
            if (surfaceEOSTracker != null) {
                surfaceEOSTracker.updateInputEOSTime(j);
                return;
            }
            return;
        }
        throw new IllegalStateException("setEndOfInputStreamTimestamp is only allowed in surface input mode");
    }

    public void start() {
        this.mEncoder.start();
    }

    public void stopAsync() {
        int i = this.mInputMode;
        if (i == 2) {
            this.mEOSTracker.updateInputEOSTime(0);
        } else if (i == 0) {
            addYuvBufferInternal(null);
        }
    }

    /* access modifiers changed from: 0000 */
    public void stopInternal() {
        this.mStopping.set(true);
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mEncoder.release();
            this.mEncoder = null;
        }
        synchronized (this.mEmptyBuffers) {
            this.mInputEOS = true;
            this.mEmptyBuffers.notifyAll();
        }
        synchronized (this) {
            if (this.mRectBlt != null) {
                this.mRectBlt.release(false);
                this.mRectBlt = null;
            }
            if (this.mEncoderEglSurface != null) {
                this.mEncoderEglSurface.release();
                this.mEncoderEglSurface = null;
            }
            if (this.mInputTexture != null) {
                this.mInputTexture.release();
                this.mInputTexture = null;
            }
        }
    }
}
