package com.xiaomi.mediacodec;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.VideoCapabilities;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.opengl.EGLSurface;
import android.opengl.GLES30;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Range;
import android.view.Surface;
import com.xiaomi.mediacodec.MoviePlayer.FrameCallback;
import com.xiaomi.mediacodec.MoviePlayer.MediaFrame;
import com.xiaomi.mediacodec.MoviePlayer.PlayTask;
import com.xiaomi.mediacodec.MoviePlayer.PlayerFeedback;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import miui.text.ExtraTextUtils;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

public class MiHWDecoder implements OnFrameAvailableListener, PlayerFeedback, FrameCallback {
    private static final String AVC_MIME_TYPE_AVC = "video/avc";
    private static final String AVC_MIME_TYPE_HEVC = "video/hevc";
    private static final String AVC_MIME_TYPE_MPEG4 = "video/mp4v-es";
    private static final int CMD_AUDIO_BYTE = 7;
    private static final int CMD_AUDIO_FORMATE = 6;
    private static final int CMD_FBO_DRAW = 2;
    private static final int CMD_INIT = 1;
    private static final int CMD_READFILE_END = 5;
    private static final int CMD_RECODER_END = 8;
    private static final int CMD_RELEASE = 4;
    private static final int CMD_SCREEN_DRAW = 3;
    private static final int CMD_VIDEO_FRAME = 9;
    private static final String TAG = "MiHWDecoder";
    /* access modifiers changed from: private */
    public int mCameraTextureId;
    /* access modifiers changed from: private */
    public int mCaptureOne = 0;
    /* access modifiers changed from: private */
    public long mContext = 0;
    /* access modifiers changed from: private */
    public EglBase mEgl;
    private boolean mError = false;
    /* access modifiers changed from: private */
    public int mFrameBuffer;
    private int mFrameNums = 0;
    private EGLSurface mGLSurface;
    private GLHandler mGlHandler;
    private boolean mIsQcomm = false;
    boolean mIsStarted = false;
    private boolean mLoop = false;
    /* access modifiers changed from: private */
    public int mNums = 0;
    /* access modifiers changed from: private */
    public OriginalRenderDrawer mOriginalDrawer;
    PlayTask mPlayTask;
    /* access modifiers changed from: private */
    public volatile boolean mPlayerExit = false;
    /* access modifiers changed from: private */
    public int mRecoderHeight = 0;
    /* access modifiers changed from: private */
    public int mRecoderWidth = 0;
    /* access modifiers changed from: private */
    public long mSeekEndMS = -1;
    /* access modifiers changed from: private */
    public long mSeekStartMS = -1;
    private String mSourceFile = "";
    private int mSourceHeight = 0;
    private int mSourceWidth = 0;
    private long mStartTime = 0;
    /* access modifiers changed from: private */
    public SurfaceTexture mSurfaceTexture;
    private HandlerThread mThread;
    private boolean mVideoOnly = false;
    private int mlastCount = 0;
    private MoviePlayer player = null;
    /* access modifiers changed from: private */
    public volatile Context shader_egl_context;

    class GLHandler extends Handler {
        private GLHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            boolean z = true;
            switch (message.what) {
                case 1:
                    StringBuilder sb = new StringBuilder();
                    sb.append(" createPbufferSurface width ");
                    sb.append(GlUtil.mWidht);
                    sb.append(" height ");
                    sb.append(GlUtil.mHeight);
                    sb.append(" shader_egl_context:");
                    sb.append(MiHWDecoder.this.shader_egl_context);
                    Logg.LogI(sb.toString());
                    try {
                        MiHWDecoder.this.mEgl = EglBase.create(MiHWDecoder.this.shader_egl_context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        MiHWDecoder.this.mEgl = EglBase.create();
                    }
                    MiHWDecoder.this.mEgl.createPbufferSurface(GlUtil.mWidht, GlUtil.mHeight);
                    MiHWDecoder.this.mEgl.makeCurrent();
                    MiHWDecoder.this.mFrameBuffer = GlesUtil.createFrameBuffer();
                    if (!(MiHWDecoder.this.mRecoderWidth == GlUtil.mWidht && MiHWDecoder.this.mRecoderHeight == GlUtil.mHeight)) {
                        z = false;
                    }
                    MiHWDecoder.this.mOriginalDrawer.setReserverResolution(z);
                    MiHWDecoder.this.mCameraTextureId = GlesUtil.createCameraTexture(z);
                    MiHWDecoder miHWDecoder = MiHWDecoder.this;
                    miHWDecoder.mSurfaceTexture = new SurfaceTexture(miHWDecoder.mCameraTextureId);
                    MiHWDecoder.this.mSurfaceTexture.setOnFrameAvailableListener(MiHWDecoder.this);
                    MiHWDecoder.this.Play();
                    MiHWDecoder.this.mOriginalDrawer.create();
                    MiHWDecoder.this.mOriginalDrawer.setInputTextureId(MiHWDecoder.this.mCameraTextureId);
                    MiHWDecoder.this.mOriginalDrawer.surfaceChangedSize(GlUtil.mWidht, GlUtil.mHeight);
                    MiHWDecoder.this.mOriginalDrawer.getOutputTextureId();
                    MiHWDecoder.this.mPlayTask.execute();
                    return;
                case 2:
                    MiHWDecoder.this.mSurfaceTexture.updateTexImage();
                    GlesUtil.checkError();
                    float[] fArr = new float[16];
                    MiHWDecoder.this.mSurfaceTexture.getTransformMatrix(fArr);
                    long timestamp = MiHWDecoder.this.mSurfaceTexture.getTimestamp();
                    GlesUtil.bindFrameBuffer(MiHWDecoder.this.mFrameBuffer, MiHWDecoder.this.mOriginalDrawer.getOutputTextureId());
                    GlesUtil.checkError();
                    MiHWDecoder.this.mOriginalDrawer.draw(timestamp, fArr);
                    GlesUtil.checkError();
                    GLES30.glFlush();
                    GlesUtil.checkError();
                    GlesUtil.unBindFrameBuffer();
                    GlesUtil.checkError();
                    if (MiHWDecoder.this.mCaptureOne < 0) {
                        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(GlUtil.mWidht * GlUtil.mHeight * 4);
                        GLES30.glBindFramebuffer(36160, MiHWDecoder.this.mFrameBuffer);
                        allocateDirect.order(ByteOrder.LITTLE_ENDIAN);
                        GLES30.glReadPixels(0, 0, GlUtil.mWidht, GlUtil.mHeight, 6408, 5121, allocateDirect);
                        allocateDirect.rewind();
                        Bitmap createBitmap = Bitmap.createBitmap(GlUtil.mWidht, GlUtil.mHeight, Config.ARGB_8888);
                        createBitmap.copyPixelsFromBuffer(allocateDirect);
                        GLES30.glBindFramebuffer(36160, 0);
                        MiHWDecoder.this.mCaptureOne = MiHWDecoder.this.mCaptureOne + 1;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("kkk");
                        sb2.append(MiHWDecoder.this.mCaptureOne);
                        sb2.append(".jpeg");
                        GlUtil.saveFile(createBitmap, "/sdcard/kk", sb2.toString());
                    }
                    MiHWDecoder.this.mNums = MiHWDecoder.this.mNums + 1;
                    if ((MiHWDecoder.this.mSeekStartMS == -1 || timestamp >= MiHWDecoder.this.mSeekStartMS * 1000 * 1000) && (MiHWDecoder.this.mSeekEndMS == -1 || timestamp <= MiHWDecoder.this.mSeekEndMS * 1000 * 1000)) {
                        MiHWDecoder miHWDecoder2 = MiHWDecoder.this;
                        long access$1300 = miHWDecoder2.mContext;
                        long j = timestamp / ExtraTextUtils.MB;
                        miHWDecoder2.onVideoFrameJni(access$1300, MiHWDecoder.this.mOriginalDrawer.getOutputTextureId(), GlUtil.mWidht, GlUtil.mHeight, j);
                        return;
                    }
                    MiHWDecoder.this.getPlayer().getOneFrame();
                    return;
                case 4:
                    if (MiHWDecoder.this.mOriginalDrawer != null) {
                        GlesUtil.deleteFrameBuffer(MiHWDecoder.this.mFrameBuffer, MiHWDecoder.this.mOriginalDrawer.getOutputTextureId());
                        GLES30.glDeleteTextures(1, new int[]{MiHWDecoder.this.mCameraTextureId}, 0);
                        MiHWDecoder.this.mOriginalDrawer.destroy();
                        Logg.LogI(" detete frame ");
                    }
                    if (MiHWDecoder.this.mSurfaceTexture != null) {
                        MiHWDecoder.this.mSurfaceTexture.release();
                        MiHWDecoder.this.mSurfaceTexture = null;
                    }
                    MiHWDecoder.this.mOriginalDrawer = null;
                    if (MiHWDecoder.this.mEgl != null) {
                        MiHWDecoder.this.mEgl.release();
                    }
                    MiHWDecoder.this.mPlayerExit = true;
                    MiHWDecoder.this.mEgl = null;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(" recoder end ");
                    sb3.append(MiHWDecoder.this.mPlayerExit);
                    Logg.LogI(sb3.toString());
                    return;
                case 6:
                    MediaFormat mediaFormat = (MediaFormat) message.obj;
                    int integer = mediaFormat.getInteger("channel-count");
                    int integer2 = mediaFormat.getInteger("sample-rate");
                    MiHWDecoder miHWDecoder3 = MiHWDecoder.this;
                    miHWDecoder3.onAudioFormatJni(miHWDecoder3.mContext, integer, integer2);
                    return;
                case 7:
                    MediaFrame mediaFrame = (MediaFrame) message.obj;
                    byte[] bArr = new byte[mediaFrame.buffer.remaining()];
                    mediaFrame.buffer.get(bArr, 0, bArr.length);
                    MiHWDecoder miHWDecoder4 = MiHWDecoder.this;
                    long access$13002 = miHWDecoder4.mContext;
                    BufferInfo bufferInfo = mediaFrame.info;
                    miHWDecoder4.onAudioFrameJni(access$13002, bArr, bufferInfo.size, bufferInfo.presentationTimeUs / 1000);
                    return;
                default:
                    return;
            }
        }
    }

    public MiHWDecoder(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("eglGetCurrentContext:");
        sb.append(EglBase.getCurrentContext());
        Logg.LogI(sb.toString());
        this.shader_egl_context = EglBase.getCurrentContext();
        this.mVideoOnly = z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0042, code lost:
        if (r5.isEncoder() != false) goto L_0x0044;
     */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x019d A[Catch:{ Exception -> 0x01c3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x01a3 A[Catch:{ Exception -> 0x01c3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01ac A[Catch:{ Exception -> 0x01c3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01b6 A[Catch:{ Exception -> 0x01c3 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean findHwCodec(String str, int i, int i2, float f, boolean z) {
        String str2;
        int[] iArr;
        boolean z2;
        float f2;
        String[] supportedTypes;
        String str3 = str;
        try {
            int max = Math.max(i, i2);
            int min = Math.min(i, i2);
            StringBuilder sb = new StringBuilder();
            sb.append("sdk version is: ");
            sb.append(VERSION.SDK_INT);
            Logg.LogI(sb.toString());
            if (VERSION.SDK_INT < 16) {
                return false;
            }
            int i3 = 0;
            while (true) {
                str2 = "codec name: ";
                if (i3 >= MediaCodecList.getCodecCount()) {
                    break;
                }
                MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i3);
                if (z) {
                    if (!codecInfoAt.isEncoder()) {
                    }
                    for (String str4 : codecInfoAt.getSupportedTypes()) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(str4);
                        sb2.append(" company:");
                        sb2.append(codecInfoAt.getName());
                        Logg.LogI(sb2.toString());
                        if (codecInfoAt.getName().contains(C0124O00000oO.O0Ooo)) {
                            this.mIsQcomm = true;
                        }
                    }
                }
                i3++;
            }
            int i4 = 0;
            while (true) {
                if (i4 >= MediaCodecList.getCodecCount()) {
                    break;
                }
                MediaCodecInfo codecInfoAt2 = MediaCodecList.getCodecInfoAt(i4);
                if (z) {
                    if (!codecInfoAt2.isEncoder()) {
                        i4++;
                    }
                } else if (codecInfoAt2.isEncoder()) {
                    i4++;
                }
                String str5 = null;
                String[] supportedTypes2 = codecInfoAt2.getSupportedTypes();
                int length = supportedTypes2.length;
                int i5 = 0;
                while (true) {
                    if (i5 >= length) {
                        break;
                    }
                    String str6 = supportedTypes2[i5];
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str2);
                    sb3.append(str6);
                    Logg.LogI(sb3.toString());
                    if (str6.equals(str)) {
                        str5 = codecInfoAt2.getName();
                        break;
                    }
                    i5++;
                }
                if (str5 == null) {
                    i4++;
                } else {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Found candidate encoder ");
                    sb4.append(str5);
                    Logg.LogI(sb4.toString());
                    CodecCapabilities capabilitiesForType = codecInfoAt2.getCapabilitiesForType(str);
                    for (int i6 : capabilitiesForType.colorFormats) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("   Color: 0x");
                        sb5.append(Integer.toHexString(i6));
                        Logg.LogI(sb5.toString());
                    }
                    VideoCapabilities videoCapabilities = capabilitiesForType.getVideoCapabilities();
                    Range supportedWidths = videoCapabilities.getSupportedWidths();
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("support width lower: ");
                    sb6.append(supportedWidths.getLower());
                    sb6.append(" upper: ");
                    sb6.append(supportedWidths.getUpper());
                    Logg.LogI(sb6.toString());
                    if (max <= ((Integer) supportedWidths.getUpper()).intValue() && max >= ((Integer) supportedWidths.getLower()).intValue()) {
                        Range supportedHeightsFor = videoCapabilities.getSupportedHeightsFor(max);
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("support height lower: ");
                        sb7.append(supportedHeightsFor.getLower());
                        sb7.append(" upper:");
                        sb7.append(supportedHeightsFor.getUpper());
                        Logg.LogI(sb7.toString());
                        if (min <= ((Integer) supportedHeightsFor.getUpper()).intValue() && min >= ((Integer) supportedHeightsFor.getLower()).intValue()) {
                            f2 = f;
                            z2 = true;
                            Logg.LogI(!videoCapabilities.areSizeAndRateSupported(max, min, (double) f2) ? "videoCapblility.areSizeAndRateSupported OK" : "videoCapblility.areSizeAndRateSupported failed");
                            if (!videoCapabilities.isSizeSupported(max, min)) {
                                Logg.LogI("videoCapblility.isSizeSupported OK, :");
                                GlUtil.mWidht = i;
                                GlUtil.mHeight = i2;
                                return true;
                            } else if (z2) {
                                Logg.LogI("videoCapblility.isSizeSupported failed but resolution OK");
                                GlUtil.mWidht = i;
                                GlUtil.mHeight = i2;
                                return true;
                            }
                        }
                    }
                    f2 = f;
                    z2 = false;
                    Logg.LogI(!videoCapabilities.areSizeAndRateSupported(max, min, (double) f2) ? "videoCapblility.areSizeAndRateSupported OK" : "videoCapblility.areSizeAndRateSupported failed");
                    if (!videoCapabilities.isSizeSupported(max, min)) {
                    }
                }
            }
            return false;
        } catch (Exception e) {
            StringBuilder sb8 = new StringBuilder();
            sb8.append("find exception at findHwEncoder:");
            sb8.append(e);
            Logg.LogE(sb8.toString());
            return false;
        }
    }

    private static int selectTrack(MediaExtractor mediaExtractor) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
            String string = trackFormat.getString(IMediaFormat.KEY_MIME);
            if (string.startsWith("video/")) {
                StringBuilder sb = new StringBuilder();
                sb.append("Extractor selected track ");
                sb.append(i);
                sb.append(" (");
                sb.append(string);
                sb.append("): ");
                sb.append(trackFormat);
                Logg.LogI(sb.toString());
                return i;
            }
        }
        return -1;
    }

    public void Play() {
        Surface surface = new Surface(this.mSurfaceTexture);
        try {
            MoviePlayer moviePlayer = new MoviePlayer(new File(this.mSourceFile), surface, this, this.mSeekStartMS, this.mVideoOnly);
            this.player = moviePlayer;
            if (this.mRecoderWidth == 0 || this.mRecoderHeight == 0) {
                this.mRecoderWidth = this.player.getVideoWidth();
                this.mRecoderHeight = this.player.getVideoHeight();
            }
            this.mPlayTask = new PlayTask(this.player, this);
            this.mPlayTask.setLoopMode(this.mLoop);
            Logg.LogI("start play");
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to play movie");
            sb.append(e);
            Logg.LogE(sb.toString());
            surface.release();
        }
    }

    public void frameReceived() {
        Logg.LogI("MiHWDecoder frameReceived! ");
        PlayTask playTask = this.mPlayTask;
        if (playTask != null) {
            playTask.frameReceived();
        }
    }

    public MoviePlayer getPlayer() {
        return this.player;
    }

    public int getVideoHeight() {
        StringBuilder sb = new StringBuilder();
        sb.append("getVideoHeight: ");
        sb.append(GlUtil.mHeight);
        Logg.LogI(sb.toString());
        return GlUtil.mHeight;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x007f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean getVideoInfo(String str) {
        MediaExtractor mediaExtractor;
        String str2 = "rotation-degrees";
        File file = new File(str);
        try {
            mediaExtractor = new MediaExtractor();
            try {
                mediaExtractor.setDataSource(file.toString());
                int selectTrack = selectTrack(mediaExtractor);
                if (selectTrack < 0) {
                    return false;
                }
                mediaExtractor.selectTrack(selectTrack);
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(selectTrack);
                GlUtil.mPictureRotation = 0;
                if (trackFormat.containsKey(str2)) {
                    GlUtil.mPictureRotation = trackFormat.getInteger(str2);
                }
                GlUtil.mWidht = trackFormat.getInteger("width");
                GlUtil.mHeight = trackFormat.getInteger("height");
                StringBuilder sb = new StringBuilder();
                sb.append(" MoviePlayer play url ");
                sb.append(file.getAbsolutePath());
                sb.append(" width ");
                sb.append(GlUtil.mWidht);
                sb.append(" height ");
                sb.append(GlUtil.mHeight);
                sb.append(" rotation ");
                sb.append(GlUtil.mPictureRotation);
                Logg.LogI(sb.toString());
                mediaExtractor.release();
                return true;
            } catch (Exception unused) {
                if (mediaExtractor != null) {
                    mediaExtractor.release();
                }
                return false;
            }
        } catch (Exception unused2) {
            mediaExtractor = null;
            if (mediaExtractor != null) {
            }
            return false;
        }
    }

    public int getVideoWidth() {
        StringBuilder sb = new StringBuilder();
        sb.append("getVideoWidth: ");
        sb.append(GlUtil.mWidht);
        Logg.LogI(sb.toString());
        return GlUtil.mWidht;
    }

    public boolean isAvcDecoderSupported(int i, int i2, float f) {
        StringBuilder sb = new StringBuilder();
        sb.append("width: ");
        sb.append(i);
        sb.append(", height: ");
        sb.append(i2);
        sb.append(", frameRate: ");
        sb.append(f);
        Logg.LogI(sb.toString());
        return findHwCodec(AVC_MIME_TYPE_AVC, i, i2, f, false);
    }

    public boolean isHevcDecoderSupported(int i, int i2, float f) {
        StringBuilder sb = new StringBuilder();
        sb.append("width: ");
        sb.append(i);
        sb.append(", height: ");
        sb.append(i2);
        sb.append(", frameRate: ");
        sb.append(f);
        Logg.LogI(sb.toString());
        return findHwCodec(AVC_MIME_TYPE_HEVC, i, i2, f, false);
    }

    public boolean isMpeg4DecoderSupported(int i, int i2, float f) {
        StringBuilder sb = new StringBuilder();
        sb.append("width: ");
        sb.append(i);
        sb.append(", height: ");
        sb.append(i2);
        sb.append(", frameRate: ");
        sb.append(f);
        Logg.LogI(sb.toString());
        return findHwCodec(AVC_MIME_TYPE_AVC, i, i2, f, false);
    }

    public void loopReset() {
    }

    public void onAudioFormat(MediaFormat mediaFormat) {
        this.mGlHandler.sendMessage(this.mGlHandler.obtainMessage(6, mediaFormat));
    }

    public native void onAudioFormatJni(long j, int i, int i2);

    public void onAudioFrame(MediaFrame mediaFrame) {
        this.mGlHandler.sendMessage(this.mGlHandler.obtainMessage(7, mediaFrame));
    }

    public native void onAudioFrameJni(long j, byte[] bArr, int i, long j2);

    public void onDecoderFinished() {
        Logg.LogE("onDecoderFinished! ");
        onDecoderFinishedJni(this.mContext);
    }

    public native void onDecoderFinishedJni(long j);

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Logg.LogI("MiHWDecoder onFrameAvailable ");
        if (this.mIsStarted) {
            this.mGlHandler.removeMessages(2);
            this.mGlHandler.sendEmptyMessage(2);
        }
    }

    public void onStreamDuration(long j) {
        onStreamDurationJni(this.mContext, j);
    }

    public native void onStreamDurationJni(long j, long j2);

    public void onVideoCrop(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mOriginalDrawer.cropSize(i, i2, i3, i4, i5, i6);
    }

    public void onVideoFrame(int i) {
        int i2 = this.mlastCount;
        if (i < i2) {
            i = i2 + 1;
        } else {
            this.mlastCount = i;
        }
        this.mGlHandler.sendMessage(this.mGlHandler.obtainMessage(9, Integer.valueOf(i)));
    }

    public native void onVideoFrameJni(long j, int i, int i2, int i3, long j2);

    public void pauseDecoder() {
        Logg.LogI("pauseDecoder");
        if (this.mIsStarted) {
            MoviePlayer moviePlayer = this.player;
            if (moviePlayer != null) {
                moviePlayer.requestPause();
            }
        }
    }

    public void playbackStopped(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(" playbackStopped ");
        sb.append(i);
        Logg.LogI(sb.toString());
        if (i > 0) {
            this.mError = true;
        }
        GLHandler gLHandler = this.mGlHandler;
        if (gLHandler != null) {
            gLHandler.sendEmptyMessage(5);
            this.mGlHandler.sendEmptyMessage(4);
        }
    }

    public void postRender() {
    }

    public void preRender(long j) {
    }

    public void resumeDecoder() {
        Logg.LogI("resumeDecoder");
        if (this.mIsStarted) {
            MoviePlayer moviePlayer = this.player;
            if (moviePlayer != null) {
                moviePlayer.requestResume();
            }
        }
    }

    public boolean seekTo(long j, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("MiHWDecoder seekTo: msec: ");
        sb.append(j);
        sb.append(" seekMode: ");
        sb.append(i);
        Logg.LogI(sb.toString());
        if (!this.mIsStarted) {
            this.mSeekStartMS = j;
            startDecoder(this.mSourceFile, this.mContext);
        }
        PlayTask playTask = this.mPlayTask;
        if (playTask != null) {
            return playTask.seekTo(j, i);
        }
        return false;
    }

    public void setLoopMode(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("setLoopMode: ");
        sb.append(z);
        Logg.LogI(sb.toString());
        this.mLoop = z;
        PlayTask playTask = this.mPlayTask;
        if (playTask != null) {
            playTask.setLoopMode(this.mLoop);
        }
    }

    public void setTransferDurationTime(long j, long j2) {
        StringBuilder sb = new StringBuilder();
        String str = " startTransfer mSeekStartMS ";
        sb.append(str);
        sb.append(j);
        String str2 = " mSeekEndMS ";
        sb.append(str2);
        sb.append(j2);
        Logg.LogI(sb.toString());
        this.mSeekStartMS = j;
        this.mSeekEndMS = j2;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(this.mSeekStartMS);
        sb2.append(str2);
        sb2.append(this.mSeekEndMS);
        Logg.LogI(sb2.toString());
    }

    public void startDecoder(String str, long j) {
        if (str.isEmpty()) {
            Logg.LogI(" startDecoder sourcePath is empty !");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" startDecoder sourcePath: ");
        sb.append(str);
        sb.append(" context: ");
        sb.append(j);
        Logg.LogI(sb.toString());
        this.mOriginalDrawer = new OriginalRenderDrawer();
        this.mSourceFile = str;
        this.mContext = j;
        this.mlastCount = 0;
        this.mIsStarted = true;
        this.mError = false;
        this.mThread = new HandlerThread("GL thread");
        this.mThread.start();
        this.mGlHandler = new GLHandler(this.mThread.getLooper());
        Message obtainMessage = this.mGlHandler.obtainMessage(1);
        this.mGlHandler.removeMessages(1);
        this.mGlHandler.sendMessage(obtainMessage);
        Logg.LogI(" startDecoder done");
        this.mPlayerExit = false;
    }

    public void stopDecoder() {
        Logg.LogI("stopDecoder");
        if (this.mIsStarted) {
            MoviePlayer moviePlayer = this.player;
            if (moviePlayer != null) {
                moviePlayer.requestStop();
            }
            this.mIsStarted = false;
            long currentTimeMillis = System.currentTimeMillis();
            while (!this.mPlayerExit) {
                if (System.currentTimeMillis() - currentTimeMillis > 2000) {
                    break;
                }
            }
            HandlerThread handlerThread = this.mThread;
            if (handlerThread != null) {
                handlerThread.quitSafely();
                this.mGlHandler.removeCallbacksAndMessages(null);
                this.mThread = null;
                this.mGlHandler = null;
            }
            if (this.player != null) {
                this.player = null;
            }
            if (this.mPlayTask != null) {
                this.mPlayTask = null;
            }
            Logg.LogI("stopDecoder done");
        }
    }
}
