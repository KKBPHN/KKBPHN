package com.ss.android.medialib;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecInfo.EncoderCapabilities;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.os.Build.VERSION;
import android.util.Pair;
import android.view.Surface;
import com.ss.android.medialib.common.TextureDrawer;
import com.ss.android.medialib.log.VEMonitorKeys;
import com.ss.android.ttve.monitor.TEMonitor;
import com.ss.android.ttve.monitor.TEMonitorNewKeys;
import com.ss.android.vesdk.VELogUtil;
import com.xiaomi.stat.MiStat.Param;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.Queue;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

@TargetApi(18)
public class AVCEncoder {
    private static final String[] BITRATE_MODES = {"BITRATE_MODE_CQ", "BITRATE_MODE_VBR", "BITRATE_MODE_CBR"};
    private static final boolean DEBUG = true;
    private static final String TAG = "AVCEncoder";
    private static int TIMEOUT_USEC = 5000;
    static AVCEncoderInterface mEncoderCaller = null;
    private byte[] codec_config;
    BufferedOutputStream fileWriter = null;
    ByteBuffer[] inputBuffers;
    BufferInfo mBufferInfo = null;
    private String mCodecName = "video/avc";
    private int mColorFormat;
    private int mDrawCount = 0;
    private int mEncodeCount = 0;
    private boolean mFlag = false;
    int mFrameRate = 30;
    private int mHeight;
    private boolean mIsError = false;
    private MediaCodec mMediaCodec = null;
    private MediaCodecInfo mMediaCodecInfo = null;
    private Queue mPTSQueue = new LinkedList();
    private int mProfile = 1;
    private Surface mSurface;
    private TextureDrawer mTextureDrawer;
    private int mWidth;
    ByteBuffer[] outputBuffers;
    int status = 0;

    interface Status {
        public static final int INITED = 1;
        public static final int STARTED = 2;
        public static final int STOPPED = 3;
        public static final int UNSET = 0;
    }

    static {
        new Thread(new Runnable() {
            public void run() {
                synchronized (AVCEncoder.class) {
                    MediaCodecList.getCodecCount();
                }
            }
        }).start();
    }

    private MediaCodecInfo getMediaCodecInfo() {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                String name = codecInfoAt.getName();
                if (!name.startsWith("OMX.google.") && !name.startsWith("OMX.Nvidia.") && !name.equals("OMX.TI.DUCATI1.VIDEO.H264E")) {
                    String[] supportedTypes = codecInfoAt.getSupportedTypes();
                    for (String equalsIgnoreCase : supportedTypes) {
                        if (equalsIgnoreCase.equalsIgnoreCase(this.mCodecName)) {
                            return codecInfoAt;
                        }
                    }
                    continue;
                }
            }
        }
        return null;
    }

    @TargetApi(21)
    private MediaCodecInfo getMediaCodecInfo21() {
        MediaCodecInfo[] codecInfos = new MediaCodecList(1).getCodecInfos();
        if (!(codecInfos == null || codecInfos.length == 0)) {
            int length = codecInfos.length;
            for (int i = 0; i < length; i++) {
                MediaCodecInfo mediaCodecInfo = codecInfos[i];
                if (mediaCodecInfo != null && mediaCodecInfo.isEncoder()) {
                    String name = mediaCodecInfo.getName();
                    if (!name.startsWith("OMX.google.") && !name.startsWith("OMX.Nvidia.") && !name.equals("OMX.TI.DUCATI1.VIDEO.H264E")) {
                        String[] supportedTypes = mediaCodecInfo.getSupportedTypes();
                        for (String equalsIgnoreCase : supportedTypes) {
                            if (equalsIgnoreCase.equalsIgnoreCase(this.mCodecName)) {
                                return mediaCodecInfo;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    private int getOneColorFormat() {
        int[] colorFormats = getColorFormats();
        if (colorFormats == null) {
            return -1;
        }
        for (int i = 0; i < colorFormats.length; i++) {
            if (colorFormats[i] == 2130708361) {
                VELogUtil.i(TAG, "====== mColorFormat support COLOR_FormatSurface ======");
                return colorFormats[i];
            }
        }
        return -1;
    }

    public static void setDrainWaitTimeout(int i) {
        TIMEOUT_USEC = i;
    }

    private void testCode(boolean z) {
        ByteBuffer order = ByteBuffer.allocateDirect(this.mWidth * this.mHeight * 4).order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, this.mWidth, this.mHeight, 6408, 5121, order);
        try {
            if (this.fileWriter == null) {
                this.fileWriter = new BufferedOutputStream(new FileOutputStream("/storage/emulated/0/xzw/rgbaBig.rgba"));
            }
            this.fileWriter.write(order.array());
            try {
                if (this.fileWriter != null) {
                    this.fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (this.fileWriter != null) {
                this.fileWriter.close();
            }
        } catch (Throwable th) {
            try {
                if (this.fileWriter != null) {
                    this.fileWriter.close();
                }
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            throw th;
        }
        Bitmap createBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Config.ARGB_8888);
        createBitmap.copyPixelsFromBuffer(order);
        saveBitmap(createBitmap, z ? "/sdcard/aweme/picture/record_e.jpeg" : "/sdcard/aweme/picture/record_s.jpeg");
        createBitmap.recycle();
    }

    public synchronized void createEncoder() {
        String[] supportedTypes;
        if (this.status == 0) {
            this.mColorFormat = getOneColorFormat();
            if (this.mColorFormat >= 0) {
                try {
                    this.mMediaCodec = MediaCodec.createEncoderByType(this.mCodecName);
                    MediaCodecInfo codecInfo = this.mMediaCodec.getCodecInfo();
                    if (!codecInfo.getName().startsWith("OMX.google.")) {
                        for (String str : codecInfo.getSupportedTypes()) {
                            String str2 = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("CodecNames: ");
                            sb.append(str);
                            VELogUtil.i(str2, sb.toString());
                        }
                        this.status = 1;
                    }
                } catch (IOException e) {
                    String str3 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("createEncoderByTyp: ");
                    sb2.append(e.getMessage());
                    VELogUtil.e(str3, sb2.toString());
                }
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:22|23|24|25|26|27) */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0137, code lost:
        if (r9 >= 0) goto L_0x0147;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0139, code lost:
        com.ss.android.vesdk.VELogUtil.d(TAG, "encode: error.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0146, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0149, code lost:
        if (android.os.Build.VERSION.SDK_INT < 21) goto L_0x0152;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x014b, code lost:
        r4 = r1.mMediaCodec.getOutputBuffer(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0152, code lost:
        r4 = r1.outputBuffers[r9];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0156, code lost:
        r11 = r4;
        r11.position(r1.mBufferInfo.offset);
        r11.limit(r1.mBufferInfo.offset + r1.mBufferInfo.size);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x016f, code lost:
        if ((r1.mBufferInfo.flags & 2) == 0) goto L_0x0187;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0171, code lost:
        com.ss.android.vesdk.VELogUtil.d(TAG, "mEncoderCaller.onSetCodecConfig");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x017a, code lost:
        if (mEncoderCaller == null) goto L_0x0181;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x017c, code lost:
        mEncoderCaller.onSetCodecConfig(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0181, code lost:
        r1.mBufferInfo.size = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x018c, code lost:
        if ((r1.mBufferInfo.flags & 1) == 0) goto L_0x0191;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x018e, code lost:
        r17 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0191, code lost:
        r17 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0193, code lost:
        com.ss.android.vesdk.VELogUtil.d(TAG, "mEncoderCaller.onWriteFile");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x019c, code lost:
        if (mEncoderCaller == null) goto L_0x0200;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x019e, code lost:
        r4 = TAG;
        r8 = new java.lang.StringBuilder();
        r8.append("encode: pts queue size = ");
        r8.append(r1.mPTSQueue.size());
        com.ss.android.vesdk.VELogUtil.d(r4, r8.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x01c0, code lost:
        if (r1.mPTSQueue.size() <= 0) goto L_0x01f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x01c2, code lost:
        r1.mEncodeCount++;
        r4 = (android.util.Pair) r1.mPTSQueue.poll();
        r14 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01d7, code lost:
        if (r1.mBufferInfo.presentationTimeUs <= 0) goto L_0x01dd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01d9, code lost:
        r14 = r1.mBufferInfo.presentationTimeUs;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x01dd, code lost:
        mEncoderCaller.onWriteFile(r11, r14 / 1000, (long) ((java.lang.Integer) r4.first).intValue(), ((java.lang.Integer) r4.second).intValue(), r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01f8, code lost:
        com.ss.android.vesdk.VELogUtil.w(TAG, "encode: no available pts!!!");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0200, code lost:
        com.ss.android.vesdk.VELogUtil.d(TAG, "encode: no output.");
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x00b7 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int encode(int i, int i2, int i3, boolean z) {
        int dequeueOutputBuffer;
        int i4 = i;
        int i5 = i2;
        boolean z2 = z;
        StringBuilder sb = new StringBuilder();
        sb.append("encodeTexture::texID: ");
        sb.append(i4);
        sb.append(" pts: ");
        sb.append(i5);
        sb.append(" duration:");
        sb.append(i3);
        sb.append("  isEndStream = ");
        sb.append(z2);
        VELogUtil.d(TAG, sb.toString());
        synchronized (this) {
            if (this.status == 2) {
                if (this.mMediaCodec != null) {
                    if (i4 > 0) {
                        if (i5 >= 0) {
                            if (this.mTextureDrawer == null && !initEGLCtx()) {
                                return -1;
                            }
                            this.mPTSQueue.offer(Pair.create(Integer.valueOf(i2), Integer.valueOf(i3)));
                            GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
                            String str = TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("encode: width = ");
                            sb2.append(this.mWidth);
                            sb2.append(" height = ");
                            sb2.append(this.mHeight);
                            VELogUtil.d(str, sb2.toString());
                            this.mTextureDrawer.drawTexture(i4);
                            GLES20.glFinish();
                            this.mDrawCount++;
                            mEncoderCaller.onSwapGlBuffers();
                            if (this.mFlag) {
                                testCode(z2);
                                this.mFlag = false;
                            }
                            if (z2) {
                                this.mMediaCodec.signalEndOfInputStream();
                                this.mIsError = true;
                                return -2;
                            }
                            loop0:
                            while (true) {
                                int i6 = 0;
                                while (true) {
                                    i6++;
                                    try {
                                        if (VERSION.SDK_INT < 21) {
                                            this.outputBuffers = this.mMediaCodec.getOutputBuffers();
                                        }
                                        dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, (long) TIMEOUT_USEC);
                                        String str2 = TAG;
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("outputBufferIndex = ");
                                        sb3.append(dequeueOutputBuffer);
                                        VELogUtil.d(str2, sb3.toString());
                                        String str3 = TAG;
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append("mBufferInfo.flags = ");
                                        sb4.append(this.mBufferInfo.flags);
                                        VELogUtil.d(str3, sb4.toString());
                                        if (dequeueOutputBuffer != -1) {
                                            if (dequeueOutputBuffer != -3) {
                                                if (dequeueOutputBuffer != -2) {
                                                    break;
                                                }
                                                VELogUtil.d(TAG, "encode: output format change!");
                                            } else {
                                                this.outputBuffers = this.mMediaCodec.getOutputBuffers();
                                            }
                                        } else {
                                            if (z2 && TIMEOUT_USEC < 5000) {
                                                TIMEOUT_USEC = 10000;
                                            }
                                            if (!z2 || this.mDrawCount == this.mEncodeCount || i6 >= 10) {
                                                break loop0;
                                                break loop0;
                                            }
                                        }
                                    } catch (Throwable unused) {
                                        this.mIsError = true;
                                        return -3;
                                    }
                                }
                                this.mMediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                            }
                            if (z2) {
                                releaseEGLCtx();
                            }
                        }
                    }
                    String str4 = TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("encode: invalidate params: texID = ");
                    sb5.append(i4);
                    sb5.append(", pts = ");
                    sb5.append(i5);
                    VELogUtil.e(str4, sb5.toString());
                    return -1;
                }
            }
            VELogUtil.w(TAG, "encode: codec is not ready.");
            return -1;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:63:0x01ab, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int encode(byte[] bArr, int i, boolean z) {
        byte[] bArr2 = bArr;
        int i2 = i;
        boolean z2 = z;
        synchronized (this) {
            if (this.status == 2) {
                if (this.mMediaCodec != null) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("encodeBuffer pts: ");
                    sb.append(i2);
                    sb.append("  isEndStream = ");
                    sb.append(z2);
                    VELogUtil.d(str, sb.toString());
                    if (VERSION.SDK_INT >= 21) {
                        int dequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(-1);
                        if (dequeueInputBuffer >= 0) {
                            ByteBuffer inputBuffer = this.mMediaCodec.getInputBuffer(dequeueInputBuffer);
                            inputBuffer.clear();
                            inputBuffer.put(bArr2, 0, bArr2.length);
                            this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, bArr2.length, (long) i2, z2 ? 4 : 0);
                        }
                        int dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, (long) TIMEOUT_USEC);
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("outputBufferIndex = ");
                        sb2.append(dequeueOutputBuffer);
                        VELogUtil.d(str2, sb2.toString());
                        String str3 = TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("mBufferInfo.flags = ");
                        sb3.append(this.mBufferInfo.flags);
                        VELogUtil.d(str3, sb3.toString());
                        while (dequeueOutputBuffer >= 0) {
                            ByteBuffer outputBuffer = this.mMediaCodec.getOutputBuffer(dequeueOutputBuffer);
                            outputBuffer.position(this.mBufferInfo.offset);
                            outputBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                            if ((this.mBufferInfo.flags & 2) != 0) {
                                VELogUtil.d(TAG, "mEncoderCaller.onSetCodecConfig");
                                if (mEncoderCaller != null) {
                                    mEncoderCaller.onSetCodecConfig(outputBuffer);
                                }
                                this.mBufferInfo.size = 0;
                            } else {
                                int i3 = (int) this.mBufferInfo.presentationTimeUs;
                                int i4 = (this.mBufferInfo.flags & 1) != 0 ? 1 : 0;
                                VELogUtil.d(TAG, "mEncoderCaller.onWriteFile");
                                if (mEncoderCaller != null) {
                                    mEncoderCaller.onWriteFile(outputBuffer, i3, 0, i4);
                                }
                            }
                            this.mMediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                            dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, 0);
                        }
                    } else {
                        int dequeueInputBuffer2 = this.mMediaCodec.dequeueInputBuffer(-1);
                        if (dequeueInputBuffer2 >= 0) {
                            ByteBuffer byteBuffer = this.inputBuffers[dequeueInputBuffer2];
                            byteBuffer.clear();
                            byteBuffer.put(bArr2, 0, bArr2.length);
                            this.mMediaCodec.queueInputBuffer(dequeueInputBuffer2, 0, bArr2.length, (long) i2, z2 ? 4 : 0);
                        }
                        for (int dequeueOutputBuffer2 = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, (long) TIMEOUT_USEC); dequeueOutputBuffer2 >= 0; dequeueOutputBuffer2 = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, 0)) {
                            if (dequeueOutputBuffer2 == -3) {
                                this.outputBuffers = this.mMediaCodec.getOutputBuffers();
                            } else if (dequeueOutputBuffer2 != -2) {
                                ByteBuffer byteBuffer2 = this.outputBuffers[dequeueOutputBuffer2];
                                byteBuffer2.position(this.mBufferInfo.offset);
                                byteBuffer2.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                                if ((this.mBufferInfo.flags & 2) != 0) {
                                    VELogUtil.d(TAG, "mEncoderCaller.onSetCodecConfig");
                                    if (mEncoderCaller != null) {
                                        mEncoderCaller.onSetCodecConfig(byteBuffer2);
                                    }
                                    this.mBufferInfo.size = 0;
                                } else {
                                    int i5 = (int) this.mBufferInfo.presentationTimeUs;
                                    int i6 = (this.mBufferInfo.flags & 1) != 0 ? 1 : 0;
                                    VELogUtil.d(TAG, "mEncoderCaller.onWriteFile");
                                    if (mEncoderCaller != null) {
                                        mEncoderCaller.onWriteFile(byteBuffer2, i5, 0, i6);
                                    }
                                }
                                this.mMediaCodec.releaseOutputBuffer(dequeueOutputBuffer2, false);
                            }
                        }
                    }
                }
            }
            return -1;
        }
    }

    public int[] getColorFormats() {
        String str = TAG;
        VELogUtil.i(str, "start == ");
        this.mMediaCodecInfo = VERSION.SDK_INT >= 21 ? getMediaCodecInfo21() : getMediaCodecInfo();
        VELogUtil.i(str, "end == ");
        if (this.mMediaCodecInfo == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("mMediaCodecInfo name = ");
        sb.append(this.mMediaCodecInfo.getName());
        VELogUtil.i(str, sb.toString());
        CodecCapabilities capabilitiesForType = this.mMediaCodecInfo.getCapabilitiesForType(this.mCodecName);
        int length = capabilitiesForType.colorFormats.length;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            iArr[i] = capabilitiesForType.colorFormats[i];
        }
        return iArr;
    }

    public int getProfile() {
        return this.mProfile;
    }

    public Surface initAVCEncoder(int i, int i2, int i3, int i4) {
        return initAVCEncoder(i, i2, i3, 1, 8, i4, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0118, code lost:
        if (r13.profile < 8) goto L_0x011a;
     */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0120 A[Catch:{ Exception -> 0x0321 }, LOOP:0: B:24:0x00c7->B:41:0x0120, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x011f A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Surface initAVCEncoder(int i, int i2, int i3, int i4, int i5, int i6, boolean z) {
        CodecProfileLevel codecProfileLevel;
        int i7;
        float f;
        float f2;
        int i8 = i;
        int i9 = i2;
        int i10 = i3;
        int i11 = i4;
        int i12 = i5;
        boolean z2 = z;
        if (!z2 || VERSION.SDK_INT < 18) {
            return null;
        }
        this.mDrawCount = 0;
        this.mEncodeCount = 0;
        VELogUtil.i(TAG, "initAVCEncoder == enter");
        if (i11 < 0 || i11 > 2) {
            StringBuilder sb = new StringBuilder();
            sb.append("Do not support bitrate mode ");
            sb.append(i11);
            sb.append(", set VBR mode");
            VELogUtil.i(TAG, sb.toString());
            i11 = 1;
        }
        if (i12 < 1 || i12 > 64) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Do not support profile ");
            sb2.append(i12);
            sb2.append(", use baseline");
            VELogUtil.i(TAG, sb2.toString());
            i12 = 1;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width + ");
        sb3.append(i8);
        sb3.append("\theight = ");
        sb3.append(i9);
        sb3.append("\tbitrate = ");
        sb3.append(i10);
        sb3.append("\tuseTextureInput = ");
        sb3.append(z2);
        VELogUtil.i(TAG, sb3.toString());
        if (i8 <= 0 || i9 <= 0) {
            return null;
        }
        this.mWidth = i8;
        this.mHeight = i9;
        synchronized (this) {
            try {
                createEncoder();
                if (mEncoderCaller != null) {
                    mEncoderCaller.setColorFormat(this.mColorFormat);
                }
                MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.mCodecName, i8, i9);
                CodecCapabilities capabilitiesForType = this.mMediaCodec.getCodecInfo().getCapabilitiesForType(this.mCodecName);
                CodecProfileLevel[] codecProfileLevelArr = capabilitiesForType.profileLevels;
                int length = codecProfileLevelArr.length;
                CodecProfileLevel codecProfileLevel2 = null;
                int i13 = 0;
                while (true) {
                    if (i13 >= length) {
                        break;
                    }
                    CodecProfileLevel codecProfileLevel3 = codecProfileLevelArr[i13];
                    String str = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Profile = ");
                    sb4.append(codecProfileLevel3.profile);
                    sb4.append(", Level = ");
                    sb4.append(codecProfileLevel3.level);
                    VELogUtil.i(str, sb4.toString());
                    if (codecProfileLevel3.profile == 1) {
                        VELogUtil.i(TAG, "Support Baseline Profile!");
                    } else if (codecProfileLevel3.profile == 2) {
                        VELogUtil.i(TAG, "Support Main Profile!");
                        if (codecProfileLevel2.profile < 2) {
                        }
                        if (codecProfileLevel3.profile == i12) {
                            break;
                        }
                        i13++;
                    } else {
                        if (codecProfileLevel3.profile == 8) {
                            VELogUtil.i(TAG, "Support High Profile!");
                        }
                        if (codecProfileLevel3.profile == i12) {
                        }
                    }
                    codecProfileLevel2 = codecProfileLevel3;
                    if (codecProfileLevel3.profile == i12) {
                    }
                }
                if (VERSION.SDK_INT >= 21) {
                    EncoderCapabilities encoderCapabilities = capabilitiesForType.getEncoderCapabilities();
                    for (int i14 = 0; i14 < 3; i14++) {
                        String str2 = TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(BITRATE_MODES[i14]);
                        sb5.append(": ");
                        sb5.append(encoderCapabilities.isBitrateModeSupported(i14));
                        VELogUtil.d(str2, sb5.toString());
                    }
                }
                if (VERSION.SDK_INT < 24 || codecProfileLevel == null) {
                    String str3 = TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Do not support profile ");
                    sb6.append(i12);
                    sb6.append(", use baseline");
                    VELogUtil.w(str3, sb6.toString());
                    TEMonitor.perfLong(0, TEMonitorNewKeys.TE_MEDIACODEC_PROFILE, 1);
                    TEMonitor.perfLong(VEMonitorKeys.IESMMTRACKER_KEY_RECORD_MEDIACODEC_PROFILE, 1);
                    i7 = i10;
                } else {
                    String str4 = TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("Set Profile: ");
                    sb7.append(codecProfileLevel.profile);
                    sb7.append(", Level = ");
                    sb7.append(codecProfileLevel.level);
                    VELogUtil.i(str4, sb7.toString());
                    this.mProfile = codecProfileLevel.profile;
                    createVideoFormat.setInteger("profile", codecProfileLevel.profile);
                    createVideoFormat.setInteger(Param.LEVEL, codecProfileLevel.level);
                    int i15 = codecProfileLevel.profile;
                    if (i15 == 2) {
                        VELogUtil.i(TAG, "Set Main Profile");
                        f2 = (float) i10;
                        f = 0.85f;
                    } else if (i15 != 8) {
                        i7 = i10;
                        TEMonitor.perfLong(0, TEMonitorNewKeys.TE_MEDIACODEC_PROFILE, (long) codecProfileLevel.profile);
                        TEMonitor.perfLong(VEMonitorKeys.IESMMTRACKER_KEY_RECORD_MEDIACODEC_PROFILE, (long) codecProfileLevel.profile);
                    } else {
                        VELogUtil.i(TAG, "Set High Profile");
                        f2 = (float) i10;
                        f = 0.75f;
                    }
                    i7 = (int) (f2 * f);
                    TEMonitor.perfLong(0, TEMonitorNewKeys.TE_MEDIACODEC_PROFILE, (long) codecProfileLevel.profile);
                    TEMonitor.perfLong(VEMonitorKeys.IESMMTRACKER_KEY_RECORD_MEDIACODEC_PROFILE, (long) codecProfileLevel.profile);
                }
                if (i7 > 12000000) {
                    i7 = 12000000;
                }
                String str5 = TAG;
                StringBuilder sb8 = new StringBuilder();
                sb8.append("bitrate = ");
                sb8.append((((float) i7) * 1.0f) / 1000000.0f);
                sb8.append("Mb/s");
                VELogUtil.i(str5, sb8.toString());
                String str6 = TAG;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("speed = ");
                sb9.append(i6);
                VELogUtil.i(str6, sb9.toString());
                createVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, i7);
                if (VERSION.SDK_INT >= 21) {
                    EncoderCapabilities encoderCapabilities2 = capabilitiesForType.getEncoderCapabilities();
                    for (int i16 = 0; i16 < 3; i16++) {
                        String str7 = TAG;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(BITRATE_MODES[i16]);
                        sb10.append(": ");
                        sb10.append(encoderCapabilities2.isBitrateModeSupported(i16));
                        VELogUtil.i(str7, sb10.toString());
                    }
                    createVideoFormat.setInteger("bitrate-mode", i11);
                    String str8 = TAG;
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append("Bitrate mode = ");
                    sb11.append(i11);
                    VELogUtil.i(str8, sb11.toString());
                    TEMonitor.perfLong(0, TEMonitorNewKeys.TE_RECORD_MEDIACODEC_RATE_CONTROL, (long) i11);
                    createVideoFormat.setInteger("max-bitrate", i10);
                    String str9 = TAG;
                    StringBuilder sb12 = new StringBuilder();
                    sb12.append("Encoder ComplexityRange: ");
                    sb12.append(encoderCapabilities2.getComplexityRange().toString());
                    VELogUtil.i(str9, sb12.toString());
                }
                createVideoFormat.setInteger("color-format", this.mColorFormat);
                createVideoFormat.setInteger("frame-rate", this.mFrameRate);
                createVideoFormat.setInteger("i-frame-interval", 1);
                TEMonitor.perfLong(0, TEMonitorNewKeys.TE_RECORD_VIDEO_ENCODE_GOP, (long) (this.mFrameRate * 1));
                String str10 = TAG;
                StringBuilder sb13 = new StringBuilder();
                sb13.append("initAVCEncoder: format = ");
                sb13.append(createVideoFormat);
                VELogUtil.i(str10, sb13.toString());
                this.mMediaCodec.configure(createVideoFormat, null, null, 1);
                this.mSurface = this.mMediaCodec.createInputSurface();
                this.mMediaCodec.start();
                this.status = 2;
                if (VERSION.SDK_INT < 21) {
                    this.inputBuffers = this.mMediaCodec.getInputBuffers();
                    this.outputBuffers = this.mMediaCodec.getOutputBuffers();
                }
                this.mBufferInfo = new BufferInfo();
                if (this.mSurface == null) {
                    return null;
                }
                VELogUtil.i(TAG, "initAVCEncoder == exit");
                return this.mSurface;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public Surface initAVCEncoder(int i, int i2, int i3, int i4, boolean z) {
        return initAVCEncoder(i, i2, i3, 1, 1, i4, z);
    }

    public boolean initEGLCtx() {
        if (this.mSurface == null) {
            VELogUtil.e(TAG, "initEGLCtx: MediaCodec should initialized ahead.");
            return false;
        }
        this.mTextureDrawer = TextureDrawer.create();
        this.mTextureDrawer.setRotation(0.0f);
        this.mTextureDrawer.setFlipScale(1.0f, -1.0f);
        return true;
    }

    public boolean isError() {
        return this.mIsError;
    }

    public void releaseEGLCtx() {
        TextureDrawer textureDrawer = this.mTextureDrawer;
        if (textureDrawer != null) {
            textureDrawer.release();
            this.mTextureDrawer = null;
        }
    }

    public synchronized void releaseEncoder() {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            this.mMediaCodec.release();
        } catch (Exception unused) {
        }
        this.mMediaCodec = null;
        this.status = 0;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("time cost: ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        VELogUtil.i(str, sb.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0070 A[SYNTHETIC, Splitter:B:31:0x0070] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x007a A[SYNTHETIC, Splitter:B:36:0x007a] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0085 A[SYNTHETIC, Splitter:B:42:0x0085] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x008f A[SYNTHETIC, Splitter:B:47:0x008f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveBitmap(Bitmap bitmap, String str) {
        FileOutputStream fileOutputStream;
        BufferedOutputStream bufferedOutputStream;
        StringBuilder sb = new StringBuilder();
        sb.append("saving Bitmap : ");
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = TAG;
        VELogUtil.i(str2, sb2);
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            fileOutputStream = new FileOutputStream(str);
            try {
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            } catch (IOException e) {
                e = e;
                try {
                    VELogUtil.e(str2, "Err when saving bitmap...");
                    e.printStackTrace();
                    if (bufferedOutputStream2 != null) {
                        try {
                            bufferedOutputStream2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    if (bufferedOutputStream2 != null) {
                        try {
                            bufferedOutputStream2.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
            try {
                bitmap.compress(CompressFormat.JPEG, 100, bufferedOutputStream);
                bufferedOutputStream.flush();
                try {
                    bufferedOutputStream.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
                try {
                    fileOutputStream.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Bitmap ");
                sb3.append(str);
                sb3.append(" saved!");
                VELogUtil.i(str2, sb3.toString());
            } catch (IOException e8) {
                e = e8;
                bufferedOutputStream2 = bufferedOutputStream;
                VELogUtil.e(str2, "Err when saving bitmap...");
                e.printStackTrace();
                if (bufferedOutputStream2 != null) {
                }
                if (fileOutputStream != null) {
                }
            } catch (Throwable th2) {
                th = th2;
                bufferedOutputStream2 = bufferedOutputStream;
                if (bufferedOutputStream2 != null) {
                }
                if (fileOutputStream != null) {
                }
                throw th;
            }
        } catch (IOException e9) {
            e = e9;
            fileOutputStream = null;
            VELogUtil.e(str2, "Err when saving bitmap...");
            e.printStackTrace();
            if (bufferedOutputStream2 != null) {
            }
            if (fileOutputStream != null) {
            }
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
            if (bufferedOutputStream2 != null) {
            }
            if (fileOutputStream != null) {
            }
            throw th;
        }
    }

    @TargetApi(21)
    public MediaFormat setBitrateMode(MediaFormat mediaFormat) {
        mediaFormat.setInteger("bitrate-mode", 0);
        return mediaFormat;
    }

    public void setEncoderCaller(AVCEncoderInterface aVCEncoderInterface) {
        mEncoderCaller = aVCEncoderInterface;
    }

    public void setFrameRate(int i) {
        this.mFrameRate = i;
    }

    public void uninitAVCEncoder() {
        VELogUtil.i(TAG, "uninitAVCEncoder == enter");
        synchronized (this) {
            if (this.status != 0) {
                if (this.mMediaCodec != null) {
                    if (this.status == 2) {
                        try {
                            this.mMediaCodec.stop();
                        } catch (Exception unused) {
                            VELogUtil.e(TAG, "MediaCodec Exception");
                        }
                    }
                    this.status = 3;
                    if (this.mSurface != null) {
                        this.mSurface.release();
                    }
                    releaseEncoder();
                    VELogUtil.i(TAG, "uninitAVCEncoder == exit");
                }
            }
        }
    }
}
