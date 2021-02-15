package com.xiaomi.camera.liveshot.encoder;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.Callback;
import android.media.MediaCodec.CodecException;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.android.camera.effect.FilterInfo;
import com.android.camera.log.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.xiaomi.camera.liveshot.LivePhotoResult;
import com.xiaomi.camera.liveshot.MediaCodecCapability;
import com.xiaomi.camera.liveshot.util.BackgroundWorker;
import com.xiaomi.camera.liveshot.util.HandlerHelper;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

public abstract class CircularMediaEncoder extends Callback {
    private static final boolean DEBUG = true;
    private static final int MSG_RELEASE_ENCODER = 2;
    private static final int MSG_START_ENCODING = 0;
    private static final int MSG_STOP_ENCODING = 1;
    /* access modifiers changed from: private */
    public static final String TAG = "CircularMediaEncoder";
    protected final BufferInfo mBufferInfo;
    protected final long mBufferingDurationUs;
    protected final long mCaptureDurationUs;
    protected volatile long mCurrentPresentationTimeUs;
    protected CyclicBuffer mCyclicBuffer;
    protected final MediaFormat mDesiredMediaFormat;
    protected final BackgroundWorker mEncodingThread;
    protected final EventHandler mEventHandler;
    protected final HandlerHelper mHandlerHelper;
    protected volatile boolean mIsBuffering;
    protected volatile boolean mIsInitialized = false;
    private Queue mLivePhotoQueue;
    protected MediaCodec mMediaCodec;
    private volatile boolean mModuleSwitched;
    private volatile boolean mOutputFormatChanged;
    protected final long mPostCaptureDurationUs;
    protected final long mPreCaptureDurationUs;
    private Handler mSnapshotListHandler;
    private HandlerThread mSnapshotListHandlerThread;
    protected final List mSnapshots;

    public final class CyclicBuffer {
        private static final boolean DEBUG = true;
        private static final String TAG = "CyclicBuffer";
        private final byte[] mDataBuffer;
        private final LivePhotoResult[] mLivePhotoResults;
        private int mMetaHead;
        private int mMetaTail;
        private final int[] mPacketFlags;
        private final int[] mPacketLength;
        private final long[] mPacketPtsUs;
        private final int[] mPacketStart;

        public CyclicBuffer(MediaFormat mediaFormat, long j) {
            if (mediaFormat != null) {
                String string = mediaFormat.getString(IMediaFormat.KEY_MIME);
                if (string != null) {
                    int integer = mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_BITRATE);
                    int i = (int) ((((long) integer) * j) / 8000);
                    this.mDataBuffer = new byte[i];
                    int integer2 = (int) ((((double) i) / ((((double) integer) / (string.contains("video") ? (double) mediaFormat.getInteger("frame-rate") : ((double) mediaFormat.getInteger("sample-rate")) / 1024.0d)) / 8.0d)) + 1.0d);
                    int i2 = integer2 * 2;
                    this.mPacketFlags = new int[i2];
                    this.mPacketPtsUs = new long[i2];
                    this.mPacketStart = new int[i2];
                    this.mPacketLength = new int[i2];
                    this.mLivePhotoResults = new LivePhotoResult[i2];
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("DesiredSpan = ");
                    sb.append(j);
                    sb.append(", dataBufferSize = ");
                    sb.append(i);
                    sb.append(", metaBufferCount = ");
                    sb.append(i2);
                    sb.append(", estimatedPacketCount = ");
                    sb.append(integer2);
                    Log.d(str, sb.toString());
                    return;
                }
                throw new IllegalArgumentException("The desired mimeType is not specified");
            }
            throw new IllegalArgumentException("The desired MediaFormat must not be null");
        }

        private boolean canAdd(int i) {
            int length = this.mDataBuffer.length;
            int length2 = this.mPacketStart.length;
            if (i <= length) {
                int i2 = this.mMetaHead;
                int i3 = this.mMetaTail;
                if (i2 == i3) {
                    return true;
                }
                String str = ")";
                if ((i2 + 1) % length2 == i3) {
                    String str2 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Ran out of metadata (head=");
                    sb.append(this.mMetaHead);
                    sb.append(" tail=");
                    sb.append(this.mMetaTail);
                    sb.append(str);
                    Log.v(str2, sb.toString());
                    return false;
                }
                int headStart = getHeadStart();
                int i4 = this.mPacketStart[this.mMetaTail];
                int i5 = ((i4 + length) - headStart) % length;
                String str3 = " free=";
                if (i > i5) {
                    String str4 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Ran out of data (tailStart=");
                    sb2.append(i4);
                    sb2.append(" headStart=");
                    sb2.append(headStart);
                    sb2.append(" req=");
                    sb2.append(i);
                    sb2.append(str3);
                    sb2.append(i5);
                    sb2.append(str);
                    Log.v(str4, sb2.toString());
                    return false;
                }
                String str5 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Okay: size=");
                sb3.append(i);
                sb3.append(str3);
                sb3.append(i5);
                sb3.append(" metaFree=");
                sb3.append((((this.mMetaTail + length2) - this.mMetaHead) % length2) - 1);
                Log.v(str5, sb3.toString());
                return true;
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Enormous packet: ");
            sb4.append(i);
            sb4.append(" vs. buffer ");
            sb4.append(length);
            throw new RuntimeException(sb4.toString());
        }

        private int getHeadStart() {
            int i = this.mMetaHead;
            if (i == this.mMetaTail) {
                return 0;
            }
            int length = this.mDataBuffer.length;
            int[] iArr = this.mPacketStart;
            int length2 = iArr.length;
            int i2 = ((i + length2) - 1) % length2;
            return ((iArr[i2] + this.mPacketLength[i2]) + 1) % length;
        }

        private void removeTail() {
            int i = this.mMetaHead;
            int i2 = this.mMetaTail;
            if (i != i2) {
                this.mMetaTail = (i2 + 1) % this.mPacketStart.length;
                return;
            }
            throw new RuntimeException("Can't removeTail() in empty buffer");
        }

        public void add(ByteBuffer byteBuffer, int i, long j, LivePhotoResult livePhotoResult) {
            int limit = byteBuffer.limit() - byteBuffer.position();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Add size=");
            sb.append(limit);
            sb.append(" flags=0x");
            sb.append(Integer.toHexString(i));
            sb.append(" pts=");
            sb.append(j);
            Log.d(str, sb.toString());
            while (!canAdd(limit)) {
                Log.d(TAG, "Cached buffer removed from tail");
                removeTail();
            }
            int length = this.mDataBuffer.length;
            int length2 = this.mPacketStart.length;
            int headStart = getHeadStart();
            int[] iArr = this.mPacketFlags;
            int i2 = this.mMetaHead;
            iArr[i2] = i;
            this.mPacketPtsUs[i2] = j;
            this.mPacketStart[i2] = headStart;
            this.mPacketLength[i2] = limit;
            this.mLivePhotoResults[i2] = livePhotoResult;
            if (headStart + limit < length) {
                byteBuffer.get(this.mDataBuffer, headStart, limit);
            } else {
                int i3 = length - headStart;
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Split, firstsize=");
                sb2.append(i3);
                sb2.append(" size=");
                sb2.append(limit);
                Log.v(str2, sb2.toString());
                byteBuffer.get(this.mDataBuffer, headStart, i3);
                byteBuffer.get(this.mDataBuffer, 0, limit - i3);
            }
            this.mMetaHead = (this.mMetaHead + 1) % length2;
        }

        public void clear() {
            Arrays.fill(this.mDataBuffer, 0);
            Arrays.fill(this.mPacketFlags, 0);
            Arrays.fill(this.mPacketPtsUs, 0);
            Arrays.fill(this.mPacketStart, 0);
            Arrays.fill(this.mPacketLength, 0);
            Arrays.fill(this.mLivePhotoResults, null);
            this.mMetaHead = 0;
            this.mMetaTail = 0;
        }

        public long computeTimeSpanUsec() {
            int length = this.mPacketStart.length;
            int i = this.mMetaHead;
            int i2 = this.mMetaTail;
            if (i == i2) {
                return 0;
            }
            int i3 = ((i + length) - 1) % length;
            long[] jArr = this.mPacketPtsUs;
            return jArr[i3] - jArr[i2];
        }

        public ByteBuffer getChunk(int i, BufferInfo bufferInfo) {
            int length = this.mDataBuffer.length;
            int i2 = this.mPacketStart[i];
            int i3 = this.mPacketLength[i];
            bufferInfo.flags = this.mPacketFlags[i];
            bufferInfo.offset = i2;
            bufferInfo.presentationTimeUs = this.mPacketPtsUs[i];
            bufferInfo.size = i3;
            if (i2 + i3 <= length) {
                ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i3);
                allocateDirect.put(this.mDataBuffer, i2, i3);
                bufferInfo.offset = 0;
                return allocateDirect;
            }
            ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(i3);
            int i4 = length - i2;
            allocateDirect2.put(this.mDataBuffer, this.mPacketStart[i], i4);
            allocateDirect2.put(this.mDataBuffer, 0, i3 - i4);
            bufferInfo.offset = 0;
            return allocateDirect2;
        }

        public int getFirstIndex() {
            int i = this.mMetaTail;
            if (i == this.mMetaHead) {
                return -1;
            }
            return i;
        }

        public LivePhotoResult getLivePhotoResult(int i) {
            return this.mLivePhotoResults[i];
        }

        public int getNextIndex(int i) {
            int length = (i + 1) % this.mPacketStart.length;
            if (length == this.mMetaHead) {
                return -1;
            }
            return length;
        }
    }

    public class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            Object obj;
            Message message2;
            int i = message.what;
            if (i == 0) {
                CircularMediaEncoder.this.doStart();
                message2 = ((Handler) message.obj).obtainMessage();
                obj = new Object();
            } else if (i == 1) {
                CircularMediaEncoder.this.doStop();
                message2 = ((Handler) message.obj).obtainMessage();
                obj = new Object();
            } else if (i != 2) {
                String access$100 = CircularMediaEncoder.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown message ");
                sb.append(message.what);
                Log.w(access$100, sb.toString());
                return;
            } else {
                CircularMediaEncoder.this.doRelease();
                message2 = ((Handler) message.obj).obtainMessage();
                obj = new Object();
            }
            message2.obj = obj;
            message2.sendToTarget();
        }
    }

    public final class Sample {
        public static final Sample EOS_SAMPLE_ENTRY;
        public final ByteBuffer data;
        public final BufferInfo info;
        public final LivePhotoResult livePhotoResult;

        static {
            BufferInfo bufferInfo = new BufferInfo();
            bufferInfo.set(0, 0, 0, 4);
            EOS_SAMPLE_ENTRY = create(ByteBuffer.allocate(0), bufferInfo);
        }

        private Sample(ByteBuffer byteBuffer, BufferInfo bufferInfo) {
            this(byteBuffer, bufferInfo, null);
        }

        private Sample(ByteBuffer byteBuffer, BufferInfo bufferInfo, LivePhotoResult livePhotoResult2) {
            this.data = byteBuffer;
            BufferInfo bufferInfo2 = new BufferInfo();
            bufferInfo2.set(bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
            this.info = bufferInfo2;
            this.livePhotoResult = livePhotoResult2;
        }

        public static Sample create(ByteBuffer byteBuffer, BufferInfo bufferInfo) {
            return new Sample(byteBuffer, bufferInfo);
        }

        public static Sample create(ByteBuffer byteBuffer, BufferInfo bufferInfo, LivePhotoResult livePhotoResult2) {
            return new Sample(byteBuffer, bufferInfo, livePhotoResult2);
        }
    }

    public final class Snapshot {
        public int filterId;
        private boolean forceEos = false;
        public final MediaFormat format;
        public final long head;
        public long offset;
        public long position = -1;
        public volatile BlockingQueue samples = new LinkedBlockingQueue();
        public final long tail;
        public long time;

        public Snapshot(long j, long j2, long j3, int i, MediaFormat mediaFormat) {
            this.head = j;
            this.tail = j2;
            this.time = j3;
            this.format = mediaFormat;
            this.filterId = i;
        }

        public void clear() {
            while (!this.samples.isEmpty()) {
                ((Sample) this.samples.poll()).data.clear();
            }
        }

        public boolean eos() {
            return this.forceEos || this.position >= this.tail;
        }

        public void put(ByteBuffer byteBuffer, BufferInfo bufferInfo, LivePhotoResult livePhotoResult) {
            if (!eos()) {
                this.samples.put(Sample.create(byteBuffer, bufferInfo, livePhotoResult));
                this.position = bufferInfo.presentationTimeUs;
                if (eos()) {
                    String str = this.format.getString(IMediaFormat.KEY_MIME).split("/")[0];
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(": max range has reached: ");
                    sb.append(this.head);
                    String str2 = ":";
                    sb.append(str2);
                    sb.append(this.tail);
                    sb.append(str2);
                    sb.append(this.position);
                    Log.d("Snapshot", sb.toString());
                    putEos();
                }
            }
        }

        public void putEos() {
            this.samples.put(Sample.EOS_SAMPLE_ENTRY);
            this.forceEos = true;
        }
    }

    class SnapshotListHandler extends Handler {
        public SnapshotListHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 0) {
                CircularMediaEncoder.this.putSnapshot((String) message.obj);
            }
        }
    }

    public CircularMediaEncoder(MediaFormat mediaFormat, long j, long j2, Queue queue) {
        if (mediaFormat == null) {
            throw new IllegalArgumentException("The desired MediaFormat must not be null");
        } else if (j2 > j) {
            throw new IllegalArgumentException("The preCaptureDurationUs must not be greater than captureDurationUs");
        } else if (j <= 0 || j2 <= 0) {
            throw new IllegalArgumentException("Both captureDurationUs and preCaptureDurationUs must be positive integers");
        } else {
            String string = mediaFormat.getString(IMediaFormat.KEY_MIME);
            if (string == null) {
                throw new IllegalArgumentException("The desired mimeType is not specified");
            } else if (MediaCodecCapability.isFormatSupported(mediaFormat, string)) {
                this.mLivePhotoQueue = queue;
                this.mDesiredMediaFormat = mediaFormat;
                this.mCaptureDurationUs = j;
                this.mPreCaptureDurationUs = j2;
                this.mPostCaptureDurationUs = j - j2;
                this.mBufferingDurationUs = j * 2;
                this.mCyclicBuffer = new CyclicBuffer(this.mDesiredMediaFormat, TimeUnit.MICROSECONDS.toMillis(this.mBufferingDurationUs));
                this.mBufferInfo = new BufferInfo();
                this.mSnapshots = new ArrayList();
                this.mEncodingThread = new BackgroundWorker(string.contains("video") ? "VideoEncodingThread" : "AudioEncodingThread");
                this.mEventHandler = new EventHandler(this.mEncodingThread.getLooper());
                this.mHandlerHelper = new HandlerHelper();
                this.mSnapshotListHandlerThread = new HandlerThread("snapshot_list");
                this.mSnapshotListHandlerThread.start();
                this.mSnapshotListHandler = new SnapshotListHandler(this.mSnapshotListHandlerThread.getLooper());
                this.mIsBuffering = false;
            } else {
                throw new IllegalArgumentException("The desired MediaFormat is not supported");
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0150  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x012c A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void putSnapshot(String str) {
        String str2;
        StringBuilder sb;
        String str3 = str;
        ArrayList<Snapshot> arrayList = new ArrayList<>();
        synchronized (this.mSnapshots) {
            arrayList.addAll(this.mSnapshots);
        }
        for (Snapshot snapshot : arrayList) {
            int firstIndex = this.mCyclicBuffer.getFirstIndex();
            if (firstIndex < 0) {
                String str4 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str3);
                sb2.append(": Unable to get the first index");
                Log.w(str4, sb2.toString());
            } else {
                int i = 1;
                boolean z = snapshot.position == -1;
                while (true) {
                    ByteBuffer chunk = this.mCyclicBuffer.getChunk(firstIndex, this.mBufferInfo);
                    LivePhotoResult livePhotoResult = this.mCyclicBuffer.getLivePhotoResult(firstIndex);
                    BufferInfo bufferInfo = this.mBufferInfo;
                    long j = bufferInfo.presentationTimeUs;
                    boolean z2 = (bufferInfo.flags & i) != 0 ? i : 0;
                    if (z) {
                        try {
                            if (j >= snapshot.head) {
                                String str5 = TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(str3);
                                sb3.append(": Snapshot.put oldcache E ");
                                sb3.append(snapshot.head);
                                sb3.append(":");
                                sb3.append(snapshot.tail);
                                sb3.append(":");
                                sb3.append(j);
                                sb3.append(":");
                                sb3.append(z2);
                                Log.d(str5, sb3.toString());
                                snapshot.put(chunk, this.mBufferInfo, livePhotoResult);
                                str2 = TAG;
                                sb = new StringBuilder();
                                sb.append(str3);
                                sb.append(": Snapshot.put oldcache X");
                            }
                        } catch (InterruptedException unused) {
                            String str6 = TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str3);
                            sb4.append(": Snapshot.put: meet interrupted exception");
                            Log.e(str6, sb4.toString());
                        }
                        if (snapshot.eos()) {
                            synchronized (this.mSnapshots) {
                                String str7 = TAG;
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(str3);
                                sb5.append(": Snapshot.put: removed from queue");
                                Log.e(str7, sb5.toString());
                                this.mSnapshots.remove(snapshot);
                            }
                            break;
                        }
                        firstIndex = this.mCyclicBuffer.getNextIndex(firstIndex);
                        if (firstIndex < 0) {
                            break;
                        } else if (snapshot.eos()) {
                            break;
                        } else {
                            i = 1;
                        }
                    } else {
                        if (j > snapshot.position) {
                            String str8 = TAG;
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str3);
                            sb6.append(": Snapshot.put incoming E ");
                            sb6.append(snapshot.head);
                            sb6.append(":");
                            sb6.append(snapshot.tail);
                            sb6.append(":");
                            sb6.append(j);
                            sb6.append(":");
                            sb6.append(z2);
                            Log.d(str8, sb6.toString());
                            snapshot.put(chunk, this.mBufferInfo, livePhotoResult);
                            str2 = TAG;
                            sb = new StringBuilder();
                            sb.append(str3);
                            sb.append(": Snapshot.put incoming X");
                        }
                        if (snapshot.eos()) {
                        }
                    }
                    Log.d(str2, sb.toString());
                    if (snapshot.eos()) {
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doRelease() {
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            mediaCodec.release();
            this.mMediaCodec = null;
        }
        BackgroundWorker backgroundWorker = this.mEncodingThread;
        if (backgroundWorker != null) {
            try {
                backgroundWorker.quit();
            } catch (InterruptedException e) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to stop encoding thread: ");
                sb.append(e);
                Log.d(str, sb.toString());
            }
        }
        HandlerThread handlerThread = this.mSnapshotListHandlerThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }

    /* access modifiers changed from: protected */
    public void doStart() {
        this.mOutputFormatChanged = false;
        this.mModuleSwitched = false;
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            mediaCodec.start();
        }
    }

    /* access modifiers changed from: protected */
    public void doStop() {
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            try {
                mediaCodec.flush();
                this.mMediaCodec.stop();
                this.mMediaCodec.reset();
            } catch (Exception e) {
                Log.w(TAG, "doStop: ", (Throwable) e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public long getNextPresentationTimeUs(long j) {
        return j;
    }

    public void moduleSwitched() {
        Log.d(TAG, "moduleSwitched");
        synchronized (this) {
            this.mModuleSwitched = true;
            notifyAll();
        }
    }

    public void onError(MediaCodec mediaCodec, CodecException codecException) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("MediaCodec Exception occurred: ");
        sb.append(codecException);
        Log.e(str, sb.toString());
    }

    public void onInputBufferAvailable(MediaCodec mediaCodec, int i) {
    }

    public void onOutputBufferAvailable(MediaCodec mediaCodec, int i, BufferInfo bufferInfo) {
        ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(i);
        String str = mediaCodec.getOutputFormat(i).getString(IMediaFormat.KEY_MIME).split("/")[0];
        if (outputBuffer != null && bufferInfo.size != 0 && this.mIsBuffering) {
            outputBuffer.position(bufferInfo.offset);
            outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
            bufferInfo.presentationTimeUs = getNextPresentationTimeUs(bufferInfo.presentationTimeUs);
            Queue queue = this.mLivePhotoQueue;
            LivePhotoResult livePhotoResult = queue != null ? (LivePhotoResult) queue.poll() : null;
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": CyclicBuffer.add E ");
            sb.append(bufferInfo.presentationTimeUs);
            Log.d(str2, sb.toString());
            this.mCyclicBuffer.add(outputBuffer, bufferInfo.flags, bufferInfo.presentationTimeUs, livePhotoResult);
            this.mCurrentPresentationTimeUs = bufferInfo.presentationTimeUs;
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(": CyclicBuffer.add X ");
            sb2.append(bufferInfo.presentationTimeUs);
            Log.d(str3, sb2.toString());
            mediaCodec.releaseOutputBuffer(i, false);
            this.mSnapshotListHandler.removeCallbacksAndMessages(null);
            HandlerThread handlerThread = this.mSnapshotListHandlerThread;
            if (handlerThread != null && handlerThread.isAlive()) {
                Message obtainMessage = this.mSnapshotListHandler.obtainMessage();
                obtainMessage.what = 0;
                obtainMessage.obj = str;
                this.mSnapshotListHandler.sendMessage(obtainMessage);
            }
        }
    }

    public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("MediaCodec Output Format Changed: ");
        sb.append(mediaFormat);
        Log.e(str, sb.toString());
        synchronized (this) {
            this.mOutputFormatChanged = true;
            notifyAll();
        }
    }

    public void release() {
        Log.d(TAG, "release");
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(2));
        this.mHandlerHelper.release();
    }

    public Snapshot snapshot() {
        return snapshot(FilterInfo.FILTER_ID_NONE);
    }

    public Snapshot snapshot(int i) {
        if (this.mIsBuffering) {
            long j = this.mCurrentPresentationTimeUs + this.mPostCaptureDurationUs;
            long max = Math.max(0, j - this.mCaptureDurationUs);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Snapshot[");
            sb.append(max);
            sb.append(", ");
            sb.append(this.mCurrentPresentationTimeUs);
            sb.append(", ");
            sb.append(j);
            sb.append("]");
            Log.d(str, sb.toString());
            Snapshot snapshot = new Snapshot(max, j, this.mCurrentPresentationTimeUs, i, this.mMediaCodec.getOutputFormat());
            synchronized (this.mSnapshots) {
                this.mSnapshots.add(snapshot);
            }
            return snapshot;
        }
        throw new IllegalStateException("MediaCodec has not been started yet");
    }

    public void start() {
        Log.d(TAG, "start");
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(0));
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:9|10|11|12|13|14|4|3) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0022 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stop() {
        int i;
        Log.d(TAG, BaseEvent.STOP);
        synchronized (this) {
            i = 10;
            while (!this.mOutputFormatChanged && i > 0 && !this.mModuleSwitched) {
                i--;
                Log.d(TAG, "waiting for MediaCodec getting stable before stop: E");
                wait(200);
                Log.d(TAG, "waiting for MediaCodec getting stable before stop: X");
            }
        }
        if (i == 0) {
            Log.d(TAG, "waiting for MediaCodec getting stable before stop has timed out");
        }
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(1));
    }
}
