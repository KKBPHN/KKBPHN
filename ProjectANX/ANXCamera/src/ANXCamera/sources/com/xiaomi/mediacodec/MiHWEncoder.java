package com.xiaomi.mediacodec;

import android.media.MediaFormat;
import android.opengl.GLES30;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.xiaomi.mediacodec.MoviePlayer.MediaFrame;
import com.xiaomi.mediacodec.VideoEncoder.VideoEncoderCallBack;
import java.util.concurrent.Semaphore;

public class MiHWEncoder extends BaseRenderDrawer implements VideoEncoderCallBack {
    private static String TAG = "MiHWEncoder";
    private int af_Position;
    private int av_Position;
    boolean encoded_end;
    long indexframe;
    private AudioEncoder mAudioEncoder;
    private int mBitrate;
    int mCaptureOne;
    private String mCodecName;
    private EglBase mEgl;
    private volatile boolean mEncodeExit;
    private boolean mError;
    private int mFps;
    long mJniContext;
    private Handler mMsgHandler;
    private String mPath;
    private int mRotate;
    private Context mSharedContext;
    private int mTextureId;
    private HandlerThread mThread;
    private volatile boolean mThreadHandlerExit;
    private volatile boolean mThreadHandlerStart;
    private VideoEncoder mVideoEncoder;
    private final Object mWaitEvent;
    private volatile boolean misStarted;
    long num;
    private int s_Texture;
    private int sample_rate;
    private final Semaphore semp;

    class MsgHandler extends Handler {
        public static final int MSG_AUDIO_FORMAT = 8;
        public static final int MSG_AUDIO_FRAME = 9;
        public static final int MSG_ENCODER_EOF = 7;
        public static final int MSG_FRAME = 5;
        public static final int MSG_QUIT = 6;
        public static final int MSG_START_RECORD = 1;
        public static final int MSG_STOP_RECORD = 2;
        public static final int MSG_UPDATE_CONTEXT = 3;
        public static final int MSG_UPDATE_SIZE = 4;

        public MsgHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    MiHWEncoder.this.prepareVideoEncoder((Context) message.obj, message.arg1, message.arg2);
                    return;
                case 2:
                    MiHWEncoder.this.stopVideoEncoder();
                    return;
                case 3:
                    Logg.LogI(" TO update context");
                    MiHWEncoder.this.updateEglContext((Context) message.obj);
                    return;
                case 4:
                    MiHWEncoder.this.updateChangedSize(message.arg1, message.arg2);
                    return;
                case 5:
                    MiHWEncoder.this.drawFrame((long) message.arg1, message.arg2);
                    return;
                case 6:
                    MiHWEncoder.this.quitLooper();
                    return;
                case 7:
                    MiHWEncoder.this.handleEncoderEOF();
                    return;
                case 8:
                    MiHWEncoder.this.handleAudioFormat((MediaFormat) message.obj);
                    return;
                case 9:
                    MiHWEncoder.this.handleAudioFrame((MediaFrame) message.obj);
                    return;
                default:
                    return;
            }
        }
    }

    public MiHWEncoder() {
        this.mError = false;
        this.encoded_end = false;
        this.mJniContext = 0;
        this.misStarted = false;
        this.mEncodeExit = false;
        this.mThreadHandlerExit = false;
        this.mThreadHandlerStart = false;
        this.sample_rate = 44100;
        this.semp = new Semaphore(2);
        this.mWaitEvent = new Object();
        this.mRotate = 0;
        this.num = 0;
        this.mCaptureOne = 0;
        this.mError = false;
        StringBuilder sb = new StringBuilder();
        sb.append("eglGetCurrentContext:");
        sb.append(EglBase.getCurrentContext());
        Logg.LogI(sb.toString());
        this.mSharedContext = EglBase.getCurrentContext();
        this.mThread = new HandlerThread("encoder thread");
        this.mThread.start();
        this.mMsgHandler = new MsgHandler(this.mThread.getLooper());
    }

    /* access modifiers changed from: private */
    public void drawFrame(long j, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("drawFrame timetamp:");
        sb.append(j);
        sb.append(" texture id:");
        sb.append(i);
        Logg.LogI(sb.toString());
        setInputTextureId(i);
        this.mEgl.makeCurrent();
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.drainEncoder(false);
        }
        onDraw();
        this.mEgl.setPresentTime(j);
        this.mEgl.swapBuffers();
        OnVideoFrameCompeletedJni(this.mJniContext, j);
    }

    /* access modifiers changed from: private */
    public void handleAudioFormat(MediaFormat mediaFormat) {
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.addMediaTrack(mediaFormat);
        }
    }

    /* access modifiers changed from: private */
    public void handleAudioFrame(MediaFrame mediaFrame) {
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.writeAudioSample(mediaFrame);
        }
    }

    /* access modifiers changed from: private */
    public void handleEncoderEOF() {
        Logg.LogI("handleEncoderEOF ");
    }

    /* access modifiers changed from: private */
    public void prepareVideoEncoder(Context context, int i, int i2) {
        this.mThreadHandlerStart = true;
        VideoEncoder videoEncoder = new VideoEncoder(i, i2, this.mFps, this.mBitrate, this.mRotate, this.mPath, this.mCodecName, this);
        this.mVideoEncoder = videoEncoder;
        if (this.mError) {
            this.mVideoEncoder = null;
        }
        this.mEgl = EglBase.create(context);
        try {
            this.mEgl.createSurface(this.mVideoEncoder.getInputSurface());
            this.mEgl.makeCurrent();
            onCreated();
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public void quitLooper() {
        StringBuilder sb = new StringBuilder();
        sb.append("to quitLooper encoder:");
        sb.append(this.mVideoEncoder);
        String str = " mEgl:";
        sb.append(str);
        sb.append(this.mEgl);
        Logg.LogI(sb.toString());
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.drainEncoder(true);
            this.mVideoEncoder.release();
            this.mVideoEncoder = null;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("to quitLooper encoder222:");
            sb2.append(this.mVideoEncoder);
            sb2.append(str);
            sb2.append(this.mEgl);
            Logg.LogI(sb2.toString());
        }
        AudioEncoder audioEncoder = this.mAudioEncoder;
        if (audioEncoder != null) {
            audioEncoder.releaseMedicacodec();
        }
        destroy();
        EglBase eglBase = this.mEgl;
        if (eglBase != null) {
            eglBase.release();
            this.mEgl = null;
        }
        this.mThreadHandlerExit = true;
    }

    /* access modifiers changed from: private */
    public void stopVideoEncoder() {
        StringBuilder sb = new StringBuilder();
        sb.append("to signal stop encoder:");
        sb.append(this.mVideoEncoder);
        Logg.LogI(sb.toString());
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.stopEncoder();
        }
    }

    /* access modifiers changed from: private */
    public void updateChangedSize(int i, int i2) {
        onChanged(i, i2);
    }

    /* access modifiers changed from: private */
    public void updateEglContext(Context context) {
        this.mEgl.release();
        this.mEgl = EglBase.create(context);
        this.mEgl.createSurface(this.mVideoEncoder.getInputSurface());
        this.mEgl.makeCurrent();
    }

    public boolean CreateEncoder(int i, int i2, int i3, int i4, int i5, String str, String str2, long j) {
        StringBuilder sb = new StringBuilder();
        sb.append("CreateEncoder: width:");
        int i6 = i;
        sb.append(i);
        sb.append(" height:");
        int i7 = i2;
        sb.append(i2);
        sb.append(" fps:");
        int i8 = i3;
        sb.append(i3);
        sb.append(" bit:");
        int i9 = i4;
        sb.append(i4);
        sb.append(" path:");
        String str3 = str;
        sb.append(str3);
        sb.append(" codec name:");
        String str4 = str2;
        sb.append(str4);
        Logg.LogI(sb.toString());
        this.mJniContext = j;
        setParams(str4, i6, i7, i8, i9, i5, str3);
        this.mAudioEncoder = new AudioEncoder();
        this.mAudioEncoder.SetEncoderDataCallback(this);
        this.mAudioEncoder.initMediacodec(this.sample_rate);
        return true;
    }

    public boolean EncodeAudioData(byte[] bArr, int i, int i2, int i3, double d) {
        StringBuilder sb = new StringBuilder();
        sb.append("jni EncodeAudioData() size: ");
        sb.append(i);
        sb.append("timeStamp:");
        sb.append(d);
        Logg.LogI(sb.toString());
        return this.mAudioEncoder.encodecPcmToAAc(i, bArr, d);
    }

    public boolean EncodeVideoData(int i, long j) {
        StringBuilder sb = new StringBuilder();
        sb.append("java EncodeVideoData: texture_id:");
        sb.append(i);
        sb.append(" timestamp:");
        sb.append(j);
        Logg.LogI(sb.toString());
        drawTexture(j, null, i);
        return true;
    }

    public void Flush() {
        Logg.LogI("java Flush():");
        stopRecord();
        if (!this.mEncodeExit) {
            quit();
            this.mThread.quitSafely();
            this.mEncodeExit = true;
        }
    }

    public native void OnVideoEncoderEOFJni(long j);

    public native void OnVideoEncoderErrorJni(long j);

    public native void OnVideoFrameCompeletedJni(long j, long j2);

    public void Release() {
        Logg.LogI("java Release()");
        if (!this.mEncodeExit) {
            quit();
            this.mThread.quitSafely();
        }
        if (this.mThreadHandlerStart) {
            long currentTimeMillis = System.currentTimeMillis();
            while (!this.mThreadHandlerExit) {
                if (System.currentTimeMillis() - currentTimeMillis > 3000) {
                    return;
                }
            }
        }
    }

    public void addAudioFormat(Object obj) {
        if (obj != null) {
            this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(8, obj));
        }
    }

    public void addAudioFrame(Object obj) {
        this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(9, obj));
    }

    public void create() {
    }

    public void draw(long j, float[] fArr) {
        this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(5, (int) j, this.mTextureId));
    }

    public void drawTexture(long j, float[] fArr, int i) {
        try {
            this.semp.acquire();
            StringBuilder sb = new StringBuilder();
            sb.append("drawTexture timestamp :");
            sb.append(j);
            sb.append(" drawTexture indexframe:");
            long j2 = this.indexframe;
            this.indexframe = j2 + 1;
            sb.append(j2);
            Logg.LogI(sb.toString());
            this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(5, (int) j, i));
            this.indexframe++;
        } catch (InterruptedException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("drawTexture timestamp faild:");
            sb2.append(j);
            Logg.LogI(sb2.toString());
        }
    }

    /* access modifiers changed from: protected */
    public String getFragmentSource() {
        return "precision mediump float;\nvarying vec2 v_texPo;\nuniform sampler2D s_Texture;\nvoid main() {\n   vec4 tc = texture2D(s_Texture, v_texPo);\n   gl_FragColor = texture2D(s_Texture, v_texPo);\n}";
    }

    public int getOutputTextureId() {
        return this.mTextureId;
    }

    /* access modifiers changed from: protected */
    public String getVertexSource() {
        return "attribute vec4 av_Position; attribute vec2 af_Position; varying vec2 v_texPo; void main() {     v_texPo = af_Position;     gl_Position = av_Position; }";
    }

    /* access modifiers changed from: protected */
    public void onChanged(int i, int i2) {
    }

    /* access modifiers changed from: protected */
    public void onCreated() {
        this.mProgram = GlesUtil.createProgram(getVertexSource(), getFragmentSource());
        initVertexBufferObjects();
        this.av_Position = GLES30.glGetAttribLocation(this.mProgram, "av_Position");
        this.af_Position = GLES30.glGetAttribLocation(this.mProgram, "af_Position");
        this.s_Texture = GLES30.glGetUniformLocation(this.mProgram, "s_Texture");
        StringBuilder sb = new StringBuilder();
        sb.append("onCreated: av_Position ");
        sb.append(this.av_Position);
        Logg.LogI(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onCreated: af_Position ");
        sb2.append(this.af_Position);
        Logg.LogI(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("onCreated: s_Texture ");
        sb3.append(this.s_Texture);
        Logg.LogI(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("onCreated: error ");
        sb4.append(GLES30.glGetError());
        Logg.LogI(sb4.toString());
    }

    /* access modifiers changed from: protected */
    public void onCroped(int i, int i2, int i3, int i4, int i5, int i6) {
    }

    /* access modifiers changed from: protected */
    public void onDraw() {
        clear();
        useProgram();
        viewPort(0, 0, this.width, this.height);
        GLES30.glEnableVertexAttribArray(this.av_Position);
        GLES30.glEnableVertexAttribArray(this.af_Position);
        GLES30.glBindBuffer(34962, this.mVertexBufferId);
        GLES30.glVertexAttribPointer(this.av_Position, 2, 5126, false, 0, 0);
        GLES30.glBindBuffer(34962, this.mDisplayTextureBufferId);
        GLES30.glVertexAttribPointer(this.af_Position, 2, 5126, false, 0, 0);
        GLES30.glBindBuffer(34962, 0);
        GLES30.glGenerateMipmap(3553);
        GLES30.glActiveTexture(33984);
        GLES30.glBindTexture(3553, this.mTextureId);
        GLES30.glUniform1i(this.s_Texture, 0);
        GLES30.glDrawArrays(5, 0, this.VertexCount);
        GLES30.glFlush();
        GLES30.glDisableVertexAttribArray(this.av_Position);
        GLES30.glDisableVertexAttribArray(this.af_Position);
        GLES30.glBindTexture(3553, 0);
    }

    public void onVideoEncodedFrame(boolean z) {
        Logg.LogI("video encoded one frame ");
        this.semp.release();
    }

    public void onVideoEncoderEOF() {
        Logg.LogI("on recv encoder eof");
        if (!this.encoded_end) {
            this.encoded_end = true;
        } else {
            OnVideoEncoderEOFJni(this.mJniContext);
        }
    }

    public void onVideoEncoderError(int i) {
        Logg.LogE("video encoder setup failed ");
        this.mError = true;
        OnVideoEncoderErrorJni(this.mJniContext);
    }

    public void quit() {
        Logg.LogI(" to quit Recoder thread ");
        Handler handler = this.mMsgHandler;
        handler.sendMessage(handler.obtainMessage(6));
    }

    /* access modifiers changed from: protected */
    public void release() {
    }

    public void setInputTextureId(int i) {
        this.mTextureId = i;
        StringBuilder sb = new StringBuilder();
        sb.append("setInputTextureId: ");
        sb.append(i);
        Logg.LogI(sb.toString());
    }

    public void setParams(String str, int i, int i2, int i3, int i4, int i5, String str2) {
        this.mPath = str2;
        this.width = i;
        this.height = i2;
        this.mFps = i3;
        this.mBitrate = i4;
        this.mCodecName = str;
        this.mRotate = i5;
    }

    public void setReserverResolution(boolean z) {
    }

    public void startRecord() {
        Logg.LogI("java startRecord context : ");
        if (!this.mEncodeExit) {
            this.misStarted = true;
            this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(1, this.width, this.height, this.mSharedContext));
        }
    }

    public void stopRecord() {
        if (this.misStarted) {
            Logg.LogI("stopRecord");
            Handler handler = this.mMsgHandler;
            handler.sendMessage(handler.obtainMessage(2));
            while (!this.encoded_end) {
                synchronized (this.mWaitEvent) {
                    try {
                        Logg.LogI("wait encoder end callback");
                        this.mWaitEvent.wait(10);
                    } catch (InterruptedException e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(" found InterruptedException exception at doExtract ");
                        sb.append(e);
                        Logg.LogI(sb.toString());
                    }
                }
            }
            this.misStarted = false;
        }
    }

    public void surfaceChangedSize(int i, int i2) {
        this.width = i;
        this.height = i2;
    }
}
