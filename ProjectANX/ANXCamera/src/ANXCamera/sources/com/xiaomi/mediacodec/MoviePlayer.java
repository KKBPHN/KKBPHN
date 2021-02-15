package com.xiaomi.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

public class MoviePlayer {
    private static final String TAG = "MoviePlayer";
    private static final boolean VERBOSE = false;
    private MediaFormat mAudioFromate = null;
    private BufferInfo mBufferInfo = new BufferInfo();
    private long mDurationUs = 0;
    private boolean mEndOfDecoder = false;
    FrameCallback mFrameCallback;
    private volatile boolean mIsPause = false;
    private volatile boolean mIsStopRequested;
    private volatile boolean mLoop = false;
    private int mMaxAudioSize = 0;
    private int mMaxSize = 0;
    private long mOutputFrames = 0;
    private Surface mOutputSurface;
    private volatile int mSeekMode = 2;
    private volatile long mSeekPosMS = 0;
    private volatile boolean mSeeking = false;
    private File mSourceFile;
    private long mStartTime = 0;
    private MediaFormat mVideoFromate = null;
    private int mVideoHeight;
    private volatile boolean mVideoOnly = false;
    private int mVideoWidth;
    private final Object mWaitEvent = new Object();
    private long mWrittenPresentationTimeUs;
    int maudioTrack = -1;
    private final Semaphore semp = new Semaphore(1);

    public interface FrameCallback {
        void loopReset();

        void onAudioFormat(MediaFormat mediaFormat);

        void onAudioFrame(MediaFrame mediaFrame);

        void onDecoderFinished();

        void onStreamDuration(long j);

        void onVideoCrop(int i, int i2, int i3, int i4, int i5, int i6);

        void onVideoFrame(int i);

        void postRender();

        void preRender(long j);
    }

    public class MediaFrame {
        public ByteBuffer buffer;
        public BufferInfo info;

        public MediaFrame() {
        }
    }

    public class PlayTask implements Runnable {
        private static final int MSG_PLAY_STOPPED = 0;
        private static final int MSG_PLAY_STOPPED_WITH_ERROR = 1;
        private boolean mDoLoop;
        private boolean mError = false;
        private PlayerFeedback mFeedback;
        private LocalHandler mLocalHandler;
        private MoviePlayer mPlayer;
        private final Object mStopLock = new Object();
        private boolean mStopped = false;
        private Thread mThread;

        class LocalHandler extends Handler {
            private LocalHandler() {
            }

            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    ((PlayerFeedback) message.obj).playbackStopped(0);
                } else if (i == 1) {
                    ((PlayerFeedback) message.obj).playbackStopped(1);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown msg ");
                    sb.append(i);
                    throw new RuntimeException(sb.toString());
                }
            }
        }

        public PlayTask(MoviePlayer moviePlayer, PlayerFeedback playerFeedback) {
            this.mPlayer = moviePlayer;
            this.mFeedback = playerFeedback;
            this.mError = false;
            this.mLocalHandler = new LocalHandler();
        }

        public void execute() {
            this.mThread = new Thread(this, "Movie Player");
            this.mThread.start();
        }

        public void frameReceived() {
            this.mPlayer.frameReceived();
        }

        public void requestStop() {
            Logg.LogI("playtask requestStop! ");
            this.mPlayer.requestStop();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
            r1 = r5.mLocalHandler;
            r1.sendMessage(r1.obtainMessage(0, r5.mFeedback));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0022, code lost:
            r0 = r5.mLocalHandler;
            r0.sendMessage(r0.obtainMessage(1, r5.mFeedback));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0057, code lost:
            if (r5.mError != false) goto L_0x0022;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
            if (r5.mError == false) goto L_0x0016;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            try {
                this.mPlayer.play();
                synchronized (this.mStopLock) {
                    this.mStopped = true;
                    this.mStopLock.notifyAll();
                }
            } catch (Exception e) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("find exception at mPlayer run:");
                    sb.append(e);
                    Logg.LogE(sb.toString());
                    this.mError = true;
                    synchronized (this.mStopLock) {
                        this.mStopped = true;
                        this.mStopLock.notifyAll();
                    }
                } catch (Throwable th) {
                    synchronized (this.mStopLock) {
                        this.mStopped = true;
                        this.mStopLock.notifyAll();
                        if (!this.mError) {
                            LocalHandler localHandler = this.mLocalHandler;
                            localHandler.sendMessage(localHandler.obtainMessage(0, this.mFeedback));
                        } else {
                            LocalHandler localHandler2 = this.mLocalHandler;
                            localHandler2.sendMessage(localHandler2.obtainMessage(1, this.mFeedback));
                        }
                        throw th;
                    }
                }
            }
        }

        public boolean seekTo(long j, int i) {
            return this.mPlayer.seekTo(j, i);
        }

        public void setLoopMode(boolean z) {
            this.mDoLoop = z;
            this.mPlayer.setLoopMode(this.mDoLoop);
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0003 */
        /* JADX WARNING: Removed duplicated region for block: B:2:0x0003 A[LOOP:0: B:2:0x0003->B:14:0x0003, LOOP_START, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void waitForStop() {
            synchronized (this.mStopLock) {
                while (!this.mStopped) {
                    this.mStopLock.wait();
                }
            }
        }
    }

    public interface PlayerFeedback {
        void playbackStopped(int i);
    }

    public class SpeedControlCallback implements FrameCallback {
        private static final boolean CHECK_SLEEP_TIME = false;
        private static final long ONE_MILLION = 1000000;
        private static final String TAG = "SpeedControlCallback";
        private long mFixedFrameDurationUsec;
        private boolean mLoopReset;
        private long mPrevMonoUsec;
        private long mPrevPresentUsec;

        public SpeedControlCallback() {
        }

        public void loopReset() {
            this.mLoopReset = true;
        }

        public void onAudioFormat(MediaFormat mediaFormat) {
        }

        public void onAudioFrame(MediaFrame mediaFrame) {
        }

        public void onDecoderFinished() {
        }

        public void onStreamDuration(long j) {
        }

        public void onVideoCrop(int i, int i2, int i3, int i4, int i5, int i6) {
        }

        public void onVideoFrame(int i) {
        }

        public void postRender() {
        }

        public void preRender(long j) {
            long j2 = 0;
            if (this.mPrevMonoUsec == 0) {
                this.mPrevMonoUsec = System.nanoTime() / 1000;
            } else {
                if (this.mLoopReset) {
                    this.mPrevPresentUsec = j - 33333;
                    this.mLoopReset = false;
                }
                long j3 = this.mFixedFrameDurationUsec;
                long j4 = j3 != 0 ? j3 : j - this.mPrevPresentUsec;
                int i = (j4 > 0 ? 1 : (j4 == 0 ? 0 : -1));
                if (i < 0) {
                    Log.w(TAG, "Weird, video times went backward");
                } else {
                    if (i == 0) {
                        Logg.LogI("Warning: current frame and previous frame had same timestamp");
                    } else if (j4 > 10000000) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Inter-frame pause was ");
                        sb.append(j4 / 1000000);
                        sb.append("sec, capping at 5 sec");
                        Logg.LogI(sb.toString());
                        j2 = 5000000;
                    }
                    j2 = j4;
                }
                long j5 = this.mPrevMonoUsec + j2;
                while (true) {
                    long nanoTime = System.nanoTime() / 1000;
                    if (nanoTime >= j5 - 100) {
                        break;
                    }
                    long j6 = j5 - nanoTime;
                    if (j6 > 500000) {
                        j6 = 500000;
                    }
                    try {
                        Thread.sleep(j6 / 1000, ((int) (j6 % 1000)) * 1000);
                    } catch (InterruptedException unused) {
                    }
                }
                this.mPrevMonoUsec += j2;
                j = this.mPrevPresentUsec + j2;
            }
            this.mPrevPresentUsec = j;
        }

        public void setFixedPlaybackRate(int i) {
            this.mFixedFrameDurationUsec = 1000000 / ((long) i);
        }
    }

    public MoviePlayer() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0134  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MoviePlayer(File file, Surface surface, FrameCallback frameCallback, long j, boolean z) {
        MediaExtractor mediaExtractor;
        String str = "rotation-degrees";
        this.mSourceFile = file;
        this.mSeekPosMS = j;
        this.mVideoOnly = z;
        if (frameCallback == null) {
            frameCallback = new SpeedControlCallback();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("  == ");
        sb.append(file.getAbsolutePath());
        Logg.LogI(sb.toString());
        this.mOutputSurface = surface;
        this.mFrameCallback = frameCallback;
        try {
            mediaExtractor = new MediaExtractor();
            try {
                mediaExtractor.setDataSource(file.toString());
                if (!this.mVideoOnly) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Need audio format when mVideoOnly ");
                    sb2.append(this.mVideoOnly);
                    Logg.LogI(sb2.toString());
                    int selectTrack = selectTrack(mediaExtractor, "audio");
                    if (selectTrack != -1) {
                        this.mAudioFromate = mediaExtractor.getTrackFormat(selectTrack);
                        this.mFrameCallback.onAudioFormat(this.mAudioFromate);
                    }
                }
                int selectTrack2 = selectTrack(mediaExtractor);
                if (selectTrack2 >= 0) {
                    mediaExtractor.selectTrack(selectTrack2);
                    MediaFormat trackFormat = mediaExtractor.getTrackFormat(selectTrack2);
                    this.mVideoFromate = trackFormat;
                    GlUtil.mPictureRotation = 0;
                    if (trackFormat.containsKey(str)) {
                        GlUtil.mPictureRotation = trackFormat.getInteger(str);
                    }
                    this.mVideoWidth = trackFormat.getInteger("width");
                    this.mVideoHeight = trackFormat.getInteger("height");
                    GlUtil.mWidht = this.mVideoWidth;
                    GlUtil.mHeight = this.mVideoHeight;
                    getMetadata();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(" MoviePlayer play url ");
                    sb3.append(file.getAbsolutePath());
                    sb3.append(" width ");
                    sb3.append(this.mVideoWidth);
                    sb3.append(" height ");
                    sb3.append(this.mVideoHeight);
                    sb3.append(" rotation ");
                    sb3.append(GlUtil.mPictureRotation);
                    Logg.LogI(sb3.toString());
                    mediaExtractor.release();
                    return;
                }
                StringBuilder sb4 = new StringBuilder();
                sb4.append("No video track found in ");
                sb4.append(this.mSourceFile);
                throw new RuntimeException(sb4.toString());
            } catch (Throwable th) {
                th = th;
                if (mediaExtractor != null) {
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            mediaExtractor = null;
            if (mediaExtractor != null) {
                mediaExtractor.release();
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x015e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void doExtract(MediaExtractor mediaExtractor, int i, MediaCodec mediaCodec, FrameCallback frameCallback) {
        long j;
        long j2;
        long j3;
        int i2;
        int i3;
        int i4;
        int i5;
        String sb;
        MediaExtractor mediaExtractor2 = mediaExtractor;
        int i6 = i;
        MediaCodec mediaCodec2 = mediaCodec;
        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
        long j4 = 1000;
        mediaExtractor2.seekTo(this.mSeekPosMS * 1000, 0);
        boolean z = false;
        boolean z2 = false;
        long j5 = -1;
        while (true) {
            boolean z3 = true;
            if (z) {
                break;
            } else if (this.mIsStopRequested) {
                Logg.LogI("Stop requested");
                break;
            } else if (this.mIsPause) {
                synchronized (this.mWaitEvent) {
                    try {
                        this.mWaitEvent.wait(10);
                    } catch (InterruptedException e) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(" found InterruptedException exception at doExtract ");
                        sb2.append(e);
                        Logg.LogI(sb2.toString());
                    }
                }
            } else {
                if (this.mSeeking) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("doExtract seekTo: mSeekPos: ");
                    sb3.append(this.mSeekPosMS);
                    sb3.append(" seekMode: ");
                    sb3.append(this.mSeekMode);
                    Logg.LogI(sb3.toString());
                    mediaExtractor2.seekTo(this.mSeekPosMS * j4, this.mSeekMode);
                    this.mSeeking = false;
                }
                if (mediaExtractor.getSampleTrackIndex() == this.maudioTrack) {
                    MediaFrame mediaFrame = new MediaFrame();
                    mediaFrame.buffer = ByteBuffer.allocate(this.mMaxAudioSize);
                    int readSampleData = mediaExtractor2.readSampleData(mediaFrame.buffer, 0);
                    if (readSampleData > 0 && mediaExtractor.getSampleTrackIndex() == this.maudioTrack) {
                        if (!this.mVideoOnly) {
                            mediaFrame.info = new BufferInfo();
                            mediaFrame.info.set(0, readSampleData, mediaExtractor.getSampleTime(), mediaExtractor.getSampleFlags());
                            this.mFrameCallback.onAudioFrame(mediaFrame);
                        }
                        mediaExtractor.advance();
                    }
                }
                if (!z2) {
                    int dequeueInputBuffer = mediaCodec2.dequeueInputBuffer(FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME);
                    if (dequeueInputBuffer >= 0) {
                        if (j5 == -1) {
                            j5 = System.nanoTime();
                        }
                        long j6 = j5;
                        int readSampleData2 = mediaExtractor2.readSampleData(inputBuffers[dequeueInputBuffer], 0);
                        System.currentTimeMillis();
                        if (readSampleData2 < 0 || this.mIsStopRequested) {
                            j = 10000;
                            mediaCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                            Logg.LogI("sent input EOS");
                            z2 = true;
                        } else {
                            if (mediaExtractor.getSampleTrackIndex() != i6) {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("WEIRD: got sample from track ");
                                sb4.append(mediaExtractor.getSampleTrackIndex());
                                sb4.append(", expected ");
                                sb4.append(i6);
                                Log.w(TAG, sb4.toString());
                            }
                            long sampleTime = mediaExtractor.getSampleTime();
                            if (this.mStartTime == 0) {
                                this.mStartTime = sampleTime;
                            }
                            long j7 = sampleTime;
                            MediaCodec mediaCodec3 = mediaCodec;
                            int i7 = dequeueInputBuffer;
                            int i8 = readSampleData2;
                            j = FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME;
                            mediaCodec3.queueInputBuffer(i7, 0, i8, j7, 0);
                            mediaExtractor.advance();
                        }
                        j2 = j6;
                        if (!z) {
                            int dequeueOutputBuffer = mediaCodec2.dequeueOutputBuffer(this.mBufferInfo, j);
                            if (dequeueOutputBuffer == -1) {
                                sb = "no output from decoder available";
                            } else if (dequeueOutputBuffer != -3) {
                                if (dequeueOutputBuffer == -2) {
                                    MediaFormat outputFormat = mediaCodec.getOutputFormat();
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("== AMEDIACODEC_INFO_OUTPUT_FORMAT_CHANGED ");
                                    sb5.append(outputFormat.toString());
                                    Logg.LogI(sb5.toString());
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("decoder output format changed: ");
                                    sb6.append(outputFormat);
                                    Logg.LogI(sb6.toString());
                                    if (outputFormat.containsKey("crop-top")) {
                                        int integer = outputFormat.getInteger("crop-top");
                                        StringBuilder sb7 = new StringBuilder();
                                        sb7.append("Crop-top:");
                                        sb7.append(integer);
                                        Logg.LogI(sb7.toString());
                                        i2 = integer;
                                    } else {
                                        i2 = 0;
                                    }
                                    if (outputFormat.containsKey("crop-bottom")) {
                                        int integer2 = outputFormat.getInteger("crop-bottom");
                                        StringBuilder sb8 = new StringBuilder();
                                        sb8.append("Crop-bottom:");
                                        sb8.append(integer2);
                                        Logg.LogI(sb8.toString());
                                        i3 = integer2;
                                    } else {
                                        i3 = 0;
                                    }
                                    if (outputFormat.containsKey("crop-left")) {
                                        int integer3 = outputFormat.getInteger("crop-left");
                                        StringBuilder sb9 = new StringBuilder();
                                        sb9.append("Crop-left:");
                                        sb9.append(integer3);
                                        Logg.LogI(sb9.toString());
                                        i4 = integer3;
                                    } else {
                                        i4 = 0;
                                    }
                                    if (outputFormat.containsKey("crop-right")) {
                                        int integer4 = outputFormat.getInteger("crop-right");
                                        StringBuilder sb10 = new StringBuilder();
                                        sb10.append("Crop-right:");
                                        sb10.append(integer4);
                                        Logg.LogI(sb10.toString());
                                        i5 = integer4;
                                    } else {
                                        i5 = 0;
                                    }
                                    int integer5 = outputFormat.getInteger("width");
                                    int integer6 = outputFormat.getInteger("height");
                                    StringBuilder sb11 = new StringBuilder();
                                    sb11.append("width :");
                                    sb11.append(integer5);
                                    sb11.append(" height:");
                                    sb11.append(integer6);
                                    Logg.LogI(sb11.toString());
                                    if (outputFormat.containsKey("color-format")) {
                                        outputFormat.getInteger("color-format");
                                        StringBuilder sb12 = new StringBuilder();
                                        sb12.append("Color format:");
                                        sb12.append(outputFormat.getInteger("color-format"));
                                        Logg.LogI(sb12.toString());
                                    }
                                    int integer7 = outputFormat.getInteger("stride");
                                    int integer8 = outputFormat.getInteger("slice-height");
                                    this.mFrameCallback.onVideoCrop(integer5, integer6, i2, i4, i3, i5);
                                    StringBuilder sb13 = new StringBuilder();
                                    sb13.append(" stride:");
                                    sb13.append(integer7);
                                    sb13.append(" height stride:");
                                    sb13.append(integer8);
                                    sb = sb13.toString();
                                } else if (dequeueOutputBuffer >= 0) {
                                    if (j2 != 0) {
                                        long nanoTime = System.nanoTime();
                                        StringBuilder sb14 = new StringBuilder();
                                        sb14.append("startup lag ");
                                        sb14.append(((double) (nanoTime - j2)) / 1000000.0d);
                                        sb14.append(" ms");
                                        Logg.LogI(sb14.toString());
                                        long j8 = 0;
                                    }
                                    if ((this.mBufferInfo.flags & 4) != 0) {
                                        Logg.LogI("output EOS");
                                        if (!this.mLoop) {
                                            this.mFrameCallback.onDecoderFinished();
                                            Logg.LogI("output EOS onDecoderFinished!");
                                        }
                                        z = true;
                                    }
                                    BufferInfo bufferInfo = this.mBufferInfo;
                                    this.mWrittenPresentationTimeUs = bufferInfo.presentationTimeUs;
                                    if (bufferInfo.size == 0) {
                                        z3 = false;
                                    }
                                    mediaCodec2.releaseOutputBuffer(dequeueOutputBuffer, z3);
                                    System.currentTimeMillis();
                                    this.mOutputFrames++;
                                    FrameCallback frameCallback2 = this.mFrameCallback;
                                    long j9 = this.mDurationUs;
                                    frameCallback2.onVideoFrame((int) (j9 == 0 ? 0 : (this.mWrittenPresentationTimeUs * 100) / j9));
                                    StringBuilder sb15 = new StringBuilder();
                                    sb15.append("onVideoFrame mDurationUs: ");
                                    sb15.append(this.mDurationUs);
                                    sb15.append(" mWrittenPresentationTimeUs: ");
                                    sb15.append(this.mWrittenPresentationTimeUs);
                                    sb15.append(" stop:");
                                    sb15.append(this.mIsStopRequested);
                                    Logg.LogI(sb15.toString());
                                    if (!z) {
                                        try {
                                            this.semp.acquire();
                                        } catch (InterruptedException unused) {
                                            Logg.LogI("semp faild!");
                                        }
                                    }
                                    if (this.mLoop) {
                                        Logg.LogI("Reached EOS, looping");
                                        mediaExtractor2.seekTo(0, 2);
                                        mediaCodec.flush();
                                        frameCallback.loopReset();
                                        j3 = j2;
                                        z = false;
                                        z2 = false;
                                        j4 = 1000;
                                    }
                                } else {
                                    StringBuilder sb16 = new StringBuilder();
                                    sb16.append("unexpected result from decoder.dequeueOutputBuffer: ");
                                    sb16.append(dequeueOutputBuffer);
                                    throw new RuntimeException(sb16.toString());
                                }
                            }
                            Logg.LogI(sb);
                        }
                        j3 = j2;
                        j4 = 1000;
                    }
                }
                j = 10000;
                j2 = j5;
                if (!z) {
                }
                j3 = j2;
                j4 = 1000;
            }
        }
        this.mEndOfDecoder = true;
        Logg.LogI(" end of decoder ");
    }

    private void getMetadata() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.mSourceFile.toString());
        if (VERSION.SDK_INT >= 19) {
            GlUtil.locationString = mediaMetadataRetriever.extractMetadata(23);
            StringBuilder sb = new StringBuilder();
            sb.append("get location: ");
            sb.append(GlUtil.locationString);
            Logg.LogI(sb.toString());
            String str = GlUtil.locationString;
            if (str != null) {
                GlUtil.location = ISO6709LocationParser(str);
                if (GlUtil.location == null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to parse the location metadata: ");
                    sb2.append(GlUtil.locationString);
                    Logg.LogI(sb2.toString());
                }
            }
        }
        try {
            this.mDurationUs = Long.parseLong(mediaMetadataRetriever.extractMetadata(9)) * 1000;
        } catch (NumberFormatException unused) {
            this.mDurationUs = -1;
        }
        this.mFrameCallback.onStreamDuration(this.mDurationUs);
        mediaMetadataRetriever.release();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Duration (us): ");
        sb3.append(this.mDurationUs);
        Logg.LogI(sb3.toString());
    }

    private static int selectTrack(MediaExtractor mediaExtractor) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            if (mediaExtractor.getTrackFormat(i).getString(IMediaFormat.KEY_MIME).startsWith("video/")) {
                return i;
            }
        }
        return -1;
    }

    private static int selectTrack(MediaExtractor mediaExtractor, String str) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            String string = mediaExtractor.getTrackFormat(i).getString(IMediaFormat.KEY_MIME);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("/");
            if (string.startsWith(sb.toString())) {
                return i;
            }
        }
        return -1;
    }

    public float[] ISO6709LocationParser(String str) {
        Pattern compile = Pattern.compile("([+\\-][0-9.]+)([+\\-][0-9.]+)");
        if (str == null) {
            return null;
        }
        Matcher matcher = compile.matcher(str);
        if (matcher.find() && matcher.groupCount() == 2) {
            String group = matcher.group(1);
            String group2 = matcher.group(2);
            try {
                return new float[]{Float.parseFloat(group), Float.parseFloat(group2)};
            } catch (NumberFormatException unused) {
            }
        }
        return null;
    }

    public void frameReceived() {
        Logg.LogI("MoviePlayer frameReceived: semp.release() ");
        this.semp.release();
    }

    public MediaFormat getAudioFromate() {
        return this.mAudioFromate;
    }

    public void getOneFrame() {
        if (!this.mEndOfDecoder) {
            synchronized (this.mWaitEvent) {
                this.mWaitEvent.notifyAll();
            }
        }
    }

    public long getVideoDuration() {
        return this.mDurationUs;
    }

    public MediaFormat getVideoFromate() {
        return this.mVideoFromate;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00e6 A[SYNTHETIC, Splitter:B:42:0x00e6] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00fb A[Catch:{ Exception -> 0x00f7 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void play() {
        MediaCodec mediaCodec;
        MediaExtractor mediaExtractor;
        String str = "find exception at mPlayer stop:";
        if (this.mSourceFile.canRead()) {
            try {
                mediaExtractor = new MediaExtractor();
                try {
                    mediaExtractor.setDataSource(this.mSourceFile.toString());
                    int selectTrack = selectTrack(mediaExtractor);
                    if (selectTrack >= 0) {
                        this.maudioTrack = selectTrack(mediaExtractor, "audio");
                        String str2 = "max-input-size";
                        if (this.maudioTrack != -1) {
                            this.mMaxAudioSize = mediaExtractor.getTrackFormat(this.maudioTrack).getInteger(str2);
                            StringBuilder sb = new StringBuilder();
                            sb.append(" get audio input size  ");
                            sb.append(this.mMaxAudioSize);
                            Logg.LogI(sb.toString());
                            mediaExtractor.selectTrack(this.maudioTrack);
                        }
                        mediaExtractor.selectTrack(selectTrack);
                        MediaFormat trackFormat = mediaExtractor.getTrackFormat(selectTrack);
                        this.mMaxSize = trackFormat.getInteger(str2);
                        mediaCodec = MediaCodec.createDecoderByType(trackFormat.getString(IMediaFormat.KEY_MIME));
                        try {
                            mediaCodec.configure(trackFormat, this.mOutputSurface, null, 0);
                            mediaCodec.start();
                            doExtract(mediaExtractor, selectTrack, mediaCodec, this.mFrameCallback);
                            if (mediaCodec != null) {
                                try {
                                    mediaCodec.flush();
                                    mediaCodec.stop();
                                    mediaCodec.release();
                                    this.mOutputSurface.release();
                                    this.mOutputSurface = null;
                                } catch (Exception e) {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(str);
                                    sb2.append(e);
                                    Logg.LogI(sb2.toString());
                                    throw e;
                                }
                            }
                            mediaExtractor.release();
                        } catch (Exception e2) {
                            e = e2;
                            try {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("find exception at mPlayer play:");
                                sb3.append(e);
                                Logg.LogI(sb3.toString());
                                throw e;
                            } catch (Throwable th) {
                                th = th;
                                if (mediaCodec != null) {
                                    try {
                                        mediaCodec.flush();
                                        mediaCodec.stop();
                                        mediaCodec.release();
                                        this.mOutputSurface.release();
                                        this.mOutputSurface = null;
                                    } catch (Exception e3) {
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append(str);
                                        sb4.append(e3);
                                        Logg.LogI(sb4.toString());
                                        throw e3;
                                    }
                                }
                                if (mediaExtractor != null) {
                                    mediaExtractor.release();
                                }
                                throw th;
                            }
                        }
                    } else {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("No video track found in ");
                        sb5.append(this.mSourceFile);
                        throw new RuntimeException(sb5.toString());
                    }
                } catch (Exception e4) {
                    e = e4;
                    mediaCodec = null;
                    StringBuilder sb32 = new StringBuilder();
                    sb32.append("find exception at mPlayer play:");
                    sb32.append(e);
                    Logg.LogI(sb32.toString());
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                    mediaCodec = null;
                    if (mediaCodec != null) {
                    }
                    if (mediaExtractor != null) {
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                mediaExtractor = null;
                mediaCodec = null;
                StringBuilder sb322 = new StringBuilder();
                sb322.append("find exception at mPlayer play:");
                sb322.append(e);
                Logg.LogI(sb322.toString());
                throw e;
            } catch (Throwable th3) {
                th = th3;
                mediaExtractor = null;
                mediaCodec = null;
                if (mediaCodec != null) {
                }
                if (mediaExtractor != null) {
                }
                throw th;
            }
        } else {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Unable to read ");
            sb6.append(this.mSourceFile);
            throw new FileNotFoundException(sb6.toString());
        }
    }

    public void requestPause() {
        Logg.LogI("MoviePlayer requestPause! ");
        this.mIsPause = true;
    }

    public void requestResume() {
        Logg.LogI("MoviePlayer requestResume! ");
        this.mIsPause = false;
        synchronized (this.mWaitEvent) {
            this.mWaitEvent.notifyAll();
        }
    }

    public void requestStop() {
        Logg.LogI("MoviePlayer requestStop! ");
        this.mIsStopRequested = true;
        this.semp.release();
        Logg.LogI("MoviePlayer requestStop: semp.release() ");
    }

    public boolean seekTo(long j, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("MoviePlayer seekTo: msec: ");
        sb.append(j);
        sb.append(" seekMode: ");
        sb.append(i);
        Logg.LogI(sb.toString());
        this.mSeekPosMS = j;
        this.mSeeking = true;
        int i2 = 2;
        if (i != 0) {
            if (i == 2) {
                i2 = 0;
            }
            return true;
        }
        this.mSeekMode = i2;
        return true;
    }

    public void setLoopMode(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("MoviePlayer setLoopMode: ");
        sb.append(z);
        Logg.LogI(sb.toString());
        this.mLoop = z;
    }
}
