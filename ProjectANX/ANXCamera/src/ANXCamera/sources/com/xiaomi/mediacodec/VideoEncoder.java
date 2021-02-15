package com.xiaomi.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.Callback;
import android.media.MediaCodec.CodecException;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.view.Surface;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.xiaomi.mediacodec.MoviePlayer.MediaFrame;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

public class VideoEncoder {
    private static final int IFRAME_INTERVAL = 2;
    private static String TAG = "videoencoder";
    private String VIDEO_MIME_TYPE;
    private Queue audioFrames;
    private Callback encoderCallback;
    private boolean mAsync;
    private int mAudioTrackIndex = -1;
    private int mBitrate = 0;
    private BufferInfo mBufferInfo;
    /* access modifiers changed from: private */
    public VideoEncoderCallBack mCallBack;
    /* access modifiers changed from: private */
    public boolean mDump;
    private String mDumpPath;
    /* access modifiers changed from: private */
    public MediaCodec mEncoder;
    /* access modifiers changed from: private */
    public long mEncoderFrames;
    private int mFps;
    private int mHeight;
    private Surface mInputSurface;
    /* access modifiers changed from: private */
    public MediaMuxer mMuxer;
    /* access modifiers changed from: private */
    public boolean mMuxerStarted = false;
    private int mNum;
    /* access modifiers changed from: private */
    public FileOutputStream mOutputStream;
    private String mPath;
    private int mRotation = 0;
    /* access modifiers changed from: private */
    public int mTrackIndex = -1;
    private int mWidth;
    /* access modifiers changed from: private */
    public Queue videoFrames;

    public interface VideoEncoderCallBack {
        void onVideoEncodedFrame(boolean z);

        void onVideoEncoderEOF();

        void onVideoEncoderError(int i);
    }

    public VideoEncoder(int i, int i2, int i3, int i4, int i5, String str, String str2, VideoEncoderCallBack videoEncoderCallBack) {
        String str3 = "video/avc";
        this.VIDEO_MIME_TYPE = str3;
        this.mEncoderFrames = 0;
        this.mNum = 0;
        this.mDump = false;
        this.mDumpPath = "/sdcard/voip-data/dump.h264";
        this.mAsync = true;
        this.audioFrames = new LinkedList();
        this.videoFrames = new LinkedList();
        if (str2.equals("hevc")) {
            this.VIDEO_MIME_TYPE = "video/hevc";
        } else {
            this.VIDEO_MIME_TYPE = str3;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" video encoder setup with width ");
        sb.append(i);
        sb.append(" height ");
        sb.append(i2);
        sb.append(" bitrate ");
        sb.append(i4);
        sb.append(" fps ");
        sb.append(i3);
        sb.append(" codecName ");
        sb.append(str2);
        Logg.LogI(sb.toString());
        this.mCallBack = videoEncoderCallBack;
        this.mWidth = i;
        this.mHeight = i2;
        this.mFps = i3;
        this.mPath = str;
        this.mBitrate = i4;
        this.mRotation = i5;
        if (this.mDump) {
            try {
                this.mOutputStream = new FileOutputStream(this.mDumpPath);
            } catch (FileNotFoundException e) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" found exception at dump ");
                sb2.append(e);
                Logg.LogI(sb2.toString());
            }
        }
        this.encoderCallback = new Callback() {
            public void onError(MediaCodec mediaCodec, CodecException codecException) {
                StringBuilder sb = new StringBuilder();
                sb.append(" MediaCodec ");
                sb.append(mediaCodec.getName());
                sb.append(" onError:");
                sb.append(codecException.toString());
                sb.append(codecException);
                Logg.LogE(sb.toString());
            }

            public void onInputBufferAvailable(MediaCodec mediaCodec, int i) {
                Logg.LogI(" Input Buffer Avail");
            }

            public void onOutputBufferAvailable(MediaCodec mediaCodec, int i, BufferInfo bufferInfo) {
                if (VideoEncoder.this.mCallBack != null) {
                    VideoEncoder.this.mCallBack.onVideoEncodedFrame(true);
                }
                ByteBuffer outputBuffer = VideoEncoder.this.mEncoder.getOutputBuffer(i);
                if (outputBuffer != null) {
                    if ((bufferInfo.flags & 2) != 0) {
                        bufferInfo.size = 0;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("add video info.size ");
                    sb.append(bufferInfo.size);
                    sb.append(" time:");
                    sb.append(bufferInfo.presentationTimeUs);
                    sb.append("index:");
                    sb.append(i);
                    Logg.LogI(sb.toString());
                    if (bufferInfo.size != 0) {
                        VideoEncoder.this.mEncoderFrames = 1 + VideoEncoder.this.mEncoderFrames;
                        outputBuffer.position(bufferInfo.offset);
                        outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                        BufferInfo bufferInfo2 = new BufferInfo();
                        ByteBuffer allocate = ByteBuffer.allocate(outputBuffer.capacity());
                        allocate.clear();
                        allocate.position(bufferInfo.offset);
                        int i2 = bufferInfo.size;
                        byte[] bArr = new byte[i2];
                        outputBuffer.get(bArr, 0, i2);
                        allocate.put(bArr, 0, bufferInfo.size);
                        allocate.position(bufferInfo.offset);
                        allocate.limit(bufferInfo.offset + bufferInfo.size);
                        bufferInfo2.set(bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
                        if (VideoEncoder.this.mMuxerStarted) {
                            VideoEncoder.this.mMuxer.writeSampleData(VideoEncoder.this.mTrackIndex, allocate, bufferInfo2);
                        } else {
                            MediaFrame mediaFrame = new MediaFrame();
                            mediaFrame.buffer = allocate;
                            mediaFrame.info = bufferInfo2;
                            VideoEncoder.this.videoFrames.add(mediaFrame);
                        }
                    }
                    VideoEncoder.this.mEncoder.releaseOutputBuffer(i, false);
                    if ((bufferInfo.flags & 4) != 0 && VideoEncoder.this.mCallBack != null) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("encode EOF mEncoderNums ");
                        sb2.append(VideoEncoder.this.mEncoderFrames);
                        Logg.LogI(sb2.toString());
                        VideoEncoder.this.mCallBack.onVideoEncoderEOF();
                        VideoEncoder.this.mTrackIndex = -1;
                        if (VideoEncoder.this.mDump) {
                            try {
                                VideoEncoder.this.mOutputStream.close();
                            } catch (IOException e) {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(" found exception at dump ");
                                sb3.append(e);
                                Logg.LogI(sb3.toString());
                            }
                        }
                    }
                } else {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("couldn't fetch buffer at index ");
                    sb4.append(i);
                    throw new RuntimeException(sb4.toString());
                }
            }

            public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
                StringBuilder sb = new StringBuilder();
                sb.append("encoder Output Format changed ");
                sb.append(mediaFormat);
                Logg.LogI(sb.toString());
                if (VideoEncoder.this.mTrackIndex < 0) {
                    VideoEncoder videoEncoder = VideoEncoder.this;
                    videoEncoder.addMediaTrack(videoEncoder.mEncoder.getOutputFormat());
                    return;
                }
                throw new RuntimeException("format changed twice");
            }
        };
        setupEncoder();
    }

    private void setupEncoder() {
        if (!this.mAsync) {
            this.mBufferInfo = new BufferInfo();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("GlUtil.mPictureRotation ");
        sb.append(GlUtil.mPictureRotation);
        Logg.LogI(sb.toString());
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.VIDEO_MIME_TYPE, this.mWidth, this.mHeight);
        int i = this.mFps;
        createVideoFormat.setInteger("color-format", 2130708361);
        if (this.mBitrate <= 0) {
            this.mBitrate = this.mWidth * this.mHeight * 4 * 2;
        }
        createVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, this.mBitrate);
        createVideoFormat.setInteger("frame-rate", i);
        createVideoFormat.setInteger("capture-rate", i);
        createVideoFormat.setInteger("repeat-previous-frame-after", 1000000 / i);
        createVideoFormat.setInteger("i-frame-interval", 2);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" video encoder setup with mwidth ");
        sb2.append(this.mWidth);
        sb2.append(" mheight ");
        sb2.append(this.mHeight);
        sb2.append(" mBitrate ");
        sb2.append(this.mBitrate);
        sb2.append(" frameRate ");
        sb2.append(i);
        sb2.append(" codecName ");
        sb2.append(this.VIDEO_MIME_TYPE);
        Logg.LogI(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" set video encoder format ");
        sb3.append(createVideoFormat);
        Logg.LogI(sb3.toString());
        try {
            this.mEncoder = MediaCodec.createEncoderByType(this.VIDEO_MIME_TYPE);
            this.mEncoder.configure(createVideoFormat, null, null, 1);
            this.mInputSurface = this.mEncoder.createInputSurface();
            if (this.mAsync) {
                this.mEncoder.setCallback(this.encoderCallback);
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append("create encoder and start path:");
            sb4.append(this.mPath);
            Logg.LogI(sb4.toString());
            this.mEncoder.start();
            this.mMuxer = new MediaMuxer(this.mPath, 0);
            this.mMuxer.setOrientationHint(this.mRotation);
            if (GlUtil.location != null) {
                this.mMuxer.setLocation(GlUtil.location[0], GlUtil.location[1]);
            }
        } catch (Exception e) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("find exception at set up encoder:");
            sb5.append(e);
            Logg.LogI(sb5.toString());
            this.mCallBack.onVideoEncoderError(1);
            release();
        }
    }

    public void addMediaTrack(MediaFormat mediaFormat) {
        String str;
        if (mediaFormat.getString(IMediaFormat.KEY_MIME).startsWith("audio/")) {
            this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
        } else {
            this.mTrackIndex = this.mMuxer.addTrack(mediaFormat);
        }
        if (!(this.mTrackIndex == -1 || this.mAudioTrackIndex == -1 || this.mMuxerStarted)) {
            this.mMuxer.start();
            this.mMuxerStarted = true;
        }
        if (this.mMuxerStarted) {
            while (true) {
                str = " time:";
                if (this.audioFrames.size() <= 0) {
                    break;
                }
                MediaFrame mediaFrame = (MediaFrame) this.audioFrames.peek();
                StringBuilder sb = new StringBuilder();
                sb.append("track audio info.size ");
                sb.append(mediaFrame.info.size);
                sb.append(str);
                sb.append(mediaFrame.info.presentationTimeUs);
                Logg.LogI(sb.toString());
                this.mMuxer.writeSampleData(this.mAudioTrackIndex, mediaFrame.buffer, mediaFrame.info);
                this.audioFrames.remove();
            }
            while (this.videoFrames.size() > 0) {
                MediaFrame mediaFrame2 = (MediaFrame) this.videoFrames.peek();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("track video info.size ");
                sb2.append(mediaFrame2.info.size);
                sb2.append(str);
                sb2.append(mediaFrame2.info.presentationTimeUs);
                Logg.LogI(sb2.toString());
                this.mMuxer.writeSampleData(this.mTrackIndex, mediaFrame2.buffer, mediaFrame2.info);
                this.videoFrames.remove();
            }
        }
    }

    public void drainEncoder(boolean z) {
        String sb;
        if (!this.mAsync) {
            if (z) {
                Logg.LogI("sending EOS to encoder");
                this.mEncoder.signalEndOfInputStream();
            }
            while (true) {
                int dequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME);
                if (dequeueOutputBuffer == -1) {
                    Logg.LogI("MediaCodec.INFO_TRY_AGAIN_LATER");
                    if (!z) {
                        break;
                    }
                    sb = "no output available, spinning to await EOS";
                } else if (dequeueOutputBuffer == -2) {
                    Logg.LogI("MediaCodec.INFO_OUTPUT_FORMAT_CHANGED");
                    if (!this.mMuxerStarted) {
                        MediaFormat outputFormat = this.mEncoder.getOutputFormat();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("encoder output format changed: ");
                        sb2.append(outputFormat);
                        Logg.LogI(sb2.toString());
                        this.mTrackIndex = this.mMuxer.addTrack(outputFormat);
                        this.mMuxer.start();
                        this.mMuxerStarted = true;
                    } else {
                        throw new RuntimeException("format changed twice");
                    }
                } else if (dequeueOutputBuffer < 0) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("unexpected result from encoder.dequeueOutputBuffer: ");
                    sb3.append(dequeueOutputBuffer);
                    sb = sb3.toString();
                } else {
                    ByteBuffer outputBuffer = this.mEncoder.getOutputBuffer(dequeueOutputBuffer);
                    if (outputBuffer != null) {
                        if ((this.mBufferInfo.flags & 2) != 0) {
                            Logg.LogI("ignoring BUFFER_FLAG_CODEC_CONFIG");
                            this.mBufferInfo.size = 0;
                        }
                        BufferInfo bufferInfo = this.mBufferInfo;
                        if (bufferInfo.size == 0) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("drainEncoder mBufferInfo: ");
                            sb4.append(this.mBufferInfo.size);
                            Logg.LogI(sb4.toString());
                        } else if (this.mMuxerStarted) {
                            outputBuffer.position(bufferInfo.offset);
                            BufferInfo bufferInfo2 = this.mBufferInfo;
                            outputBuffer.limit(bufferInfo2.offset + bufferInfo2.size);
                            this.mMuxer.writeSampleData(this.mTrackIndex, outputBuffer, this.mBufferInfo);
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("sent ");
                            sb5.append(this.mBufferInfo.size);
                            sb5.append(" bytes to muxer, ts=");
                            sb5.append(this.mBufferInfo.presentationTimeUs);
                            String str = " ";
                            sb5.append(str);
                            sb5.append(outputBuffer.get(0));
                            sb5.append(str);
                            sb5.append(outputBuffer.get(1));
                            sb5.append(str);
                            sb5.append(outputBuffer.get(2));
                            sb5.append(str);
                            sb5.append(outputBuffer.get(3));
                            sb5.append(" type ");
                            sb5.append(outputBuffer.get(4) & 31);
                            sb5.append(" mTrackIndex ");
                            sb5.append(this.mTrackIndex);
                            sb5.append(" mNum ");
                            sb5.append(this.mNum);
                            Logg.LogI(sb5.toString());
                            this.mNum++;
                        } else {
                            throw new RuntimeException("muxer hasn't started");
                        }
                        this.mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                        if ((this.mBufferInfo.flags & 4) != 0) {
                            if (this.mCallBack != null) {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("encode EOF mEncoderNums ");
                                sb6.append(this.mEncoderFrames);
                                Logg.LogI(sb6.toString());
                                this.mCallBack.onVideoEncoderEOF();
                            }
                            if (!z) {
                                Logg.LogW("reached end of stream unexpectedly");
                            } else {
                                Logg.LogI("end of stream reached");
                            }
                        }
                    } else {
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("encoderOutputBuffer ");
                        sb7.append(dequeueOutputBuffer);
                        sb7.append(" was null");
                        throw new RuntimeException(sb7.toString());
                    }
                }
                Logg.LogI(sb);
            }
        }
    }

    public void flush() {
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            mediaCodec.flush();
        }
        if (this.mAsync) {
            MediaCodec mediaCodec2 = this.mEncoder;
            if (mediaCodec2 != null) {
                mediaCodec2.start();
            }
        }
    }

    public Surface getInputSurface() {
        return this.mInputSurface;
    }

    public void release() {
        Logg.LogI("releasing encoder objects");
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            try {
                mediaCodec.stop();
                this.mEncoder.release();
                this.mInputSurface.release();
                this.mBufferInfo = null;
            } catch (Exception unused) {
            }
            this.mEncoder = null;
        }
        if (this.mMuxer != null) {
            Logg.LogI("to stop muxter");
            try {
                this.mMuxer.stop();
                Logg.LogI("to release muxter");
                this.mMuxer.release();
                if (this.mCallBack != null) {
                    this.mCallBack.onVideoEncoderEOF();
                }
            } catch (Exception unused2) {
            }
            this.mMuxer = null;
        }
    }

    public void stopEncoder() {
        StringBuilder sb = new StringBuilder();
        sb.append("stopEncoder mAsync:");
        sb.append(this.mAsync);
        Logg.LogI(sb.toString());
        try {
            if (!this.mAsync) {
                drainEncoder(true);
            } else if (this.mEncoder != null) {
                this.mEncoder.signalEndOfInputStream();
            }
        } catch (Exception unused) {
        }
    }

    public void writeAudioSample(MediaFrame mediaFrame) {
        if (!this.mMuxerStarted) {
            this.audioFrames.add(mediaFrame);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("add audio info.size ");
        sb.append(mediaFrame.info.size);
        sb.append(" time:");
        sb.append(mediaFrame.info.presentationTimeUs);
        Logg.LogI(sb.toString());
        this.mMuxer.writeSampleData(this.mAudioTrackIndex, mediaFrame.buffer, mediaFrame.info);
    }
}
