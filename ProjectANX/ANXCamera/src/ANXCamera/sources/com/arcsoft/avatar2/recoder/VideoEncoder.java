package com.arcsoft.avatar2.recoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.view.Surface;
import com.android.camera.storage.Storage;
import com.arcsoft.avatar2.gl.EGLWrapper;
import com.arcsoft.avatar2.gl.GLFramebuffer;
import com.arcsoft.avatar2.gl.GLRender;
import com.arcsoft.avatar2.util.CodecLog;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.locks.ReentrantLock;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class VideoEncoder extends BaseEncoder {
    public static final String ENCODER_THREAD_NAME = "Arc_Video_Encoder";
    private static String F = "video/hevc";
    public static final String NAME = "ARC_V";
    private static final String v = "Arc_VideoEncoder";
    private static final long w = 1000000000;
    private static final int x = 10000000;
    private static final int y = 30;
    private static final int z = 10;
    private MediaFormat A;
    /* access modifiers changed from: private */
    public int B;
    /* access modifiers changed from: private */
    public int C;
    private boolean D;
    private int E;
    private Surface G;
    private Thread H;
    /* access modifiers changed from: private */
    public EGLWrapper I;
    private EGLContext J = EGL14.EGL_NO_CONTEXT;
    /* access modifiers changed from: private */
    public GLRender K;
    private int L;
    protected long u;

    public class SaveThread extends Thread {
        private ByteBuffer b;

        public SaveThread(ByteBuffer byteBuffer) {
            this.b = byteBuffer;
        }

        public void run() {
            super.run();
            Bitmap createBitmap = Bitmap.createBitmap(VideoEncoder.this.B, VideoEncoder.this.C, Config.ARGB_8888);
            createBitmap.copyPixelsFromBuffer(this.b);
            StringBuilder sb = new StringBuilder();
            sb.append("/sdcard/Pictures/_");
            sb.append(System.currentTimeMillis());
            sb.append(Storage.JPEG_SUFFIX);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(sb.toString());
                createBitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
                createBitmap.recycle();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public VideoEncoder(MuxerWrapper muxerWrapper, int i, int i2, Object obj, RecordingListener recordingListener, EGLContext eGLContext, int i3, String str) {
        super(muxerWrapper, obj, recordingListener);
        this.B = i;
        this.C = i2;
        this.H = null;
        this.L = i3;
        this.J = eGLContext;
        F = str;
        prepare(true);
        b();
        this.q = new ReentrantLock();
        this.r = this.q.newCondition();
        StringBuilder sb = new StringBuilder();
        sb.append("VideoEncoder constructor mCustomerBitRate = ");
        sb.append(this.L);
        String sb2 = sb.toString();
        String str2 = v;
        CodecLog.d(str2, sb2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("VideoEncoder constructor mWidth = ");
        sb3.append(i);
        sb3.append(" ,mHeight = ");
        sb3.append(i2);
        CodecLog.d(str2, sb3.toString());
    }

    /* access modifiers changed from: private */
    public void a(float f) {
        this.K = new GLRender(this.B, this.C, this.E, true);
        this.K.initRender(false, f);
        StringBuilder sb = new StringBuilder();
        sb.append("VideoEncoder initGL glError = ");
        sb.append(GLES20.glGetError());
        CodecLog.d(v, sb.toString());
    }

    private void a(boolean z2) {
        String str = v;
        CodecLog.d(str, "initVideoEncoder()->in");
        this.A = MediaFormat.createVideoFormat(F, this.B, this.C);
        this.A.setInteger("color-format", 2130708361);
        this.A.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, this.L);
        this.A.setInteger("frame-rate", 30);
        this.A.setInteger("i-frame-interval", 10);
        try {
            this.i = MediaCodec.createEncoderByType(F);
            StringBuilder sb = new StringBuilder();
            sb.append("initVideoEncoder(): selected_codec_name = ");
            sb.append(this.i.getName());
            CodecLog.i(str, sb.toString());
        } catch (IOException e) {
            CodecLog.e(str, "initVideoEncoder()->createEncoderByType failed.");
            e.printStackTrace();
            RecordingListener recordingListener = this.o;
            if (recordingListener != null) {
                recordingListener.onRecordingListener(561, Integer.valueOf(0));
            }
        }
        try {
            this.i.configure(this.A, null, null, 1);
        } catch (Exception e2) {
            CodecLog.e(str, "initVideoEncoder()->configure failed.");
            e2.printStackTrace();
            RecordingListener recordingListener2 = this.o;
            if (recordingListener2 != null) {
                recordingListener2.onRecordingListener(562, Integer.valueOf(0));
            }
        }
        if (z2) {
            try {
                this.G = this.i.createInputSurface();
            } catch (Exception e3) {
                CodecLog.e(str, "initVideoEncoder()->createInputSurface failed.");
                e3.printStackTrace();
                RecordingListener recordingListener3 = this.o;
                if (recordingListener3 != null) {
                    recordingListener3.onRecordingListener(562, Integer.valueOf(0));
                }
            }
        } else {
            this.G = null;
        }
        CodecLog.d(str, "initVideoEncoder()->out");
    }

    private void b() {
        this.I = new EGLWrapper(getInputSurface(), this.J);
    }

    /* access modifiers changed from: private */
    public void c() {
        this.K.unInitRender();
        this.K = null;
    }

    private void d() {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.B * this.C * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, this.B, this.C, 6408, 5121, allocateDirect);
        new SaveThread(allocateDirect).start();
    }

    public Surface getInputSurface() {
        return this.i != null ? this.G : super.getInputSurface();
    }

    public void notifyNewFrameAvailable() {
    }

    public void pauseRecording() {
        if (!this.e) {
            this.e = true;
            this.u = System.nanoTime();
        }
    }

    public void prepare(boolean z2) {
        a(z2);
        if (this.i == null) {
            throw new RuntimeException("Init video encoder is failed.");
        }
    }

    public void release(boolean z2) {
        String str = v;
        try {
            this.q.lock();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("release()-> meet error when get lock : ");
            sb.append(e.getMessage());
            CodecLog.e(str, sb.toString());
        } catch (Throwable th) {
            sinalCondition();
            this.q.unlock();
            throw th;
        }
        sinalCondition();
        this.q.unlock();
        Thread thread = this.H;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e2) {
                CodecLog.d(str, "Encoder Thread has been Interrupted, errors may be occurred.");
                e2.printStackTrace();
            } catch (Throwable th2) {
                this.H = null;
                throw th2;
            }
            this.H = null;
        }
        EGLWrapper eGLWrapper = this.I;
        if (eGLWrapper != null) {
            eGLWrapper.release();
            this.I = null;
        }
        this.J = EGL14.EGL_NO_CONTEXT;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("VideoEncoder release() encoder thread exit. threadName =");
        sb2.append("Arc_Video_Encoder");
        CodecLog.d(str, sb2.toString());
        this.G = null;
        this.q = null;
        this.r = null;
        this.s = null;
        super.release(z2);
    }

    public void resumeRecording() {
        if (this.e) {
            this.e = false;
            this.g += System.nanoTime() - this.u;
            this.n.add(Long.valueOf(this.g));
        }
    }

    public void startRecording() {
        if (this.H == null) {
            super.startRecording();
            this.H = new Thread("Arc_Video_Encoder") {
                public void run() {
                    super.run();
                    setName("ARC_V");
                    try {
                        VideoEncoder.this.i.start();
                        VideoEncoder.this.I.makeCurrent();
                        VideoEncoder videoEncoder = VideoEncoder.this;
                        videoEncoder.a(videoEncoder.t);
                        while (true) {
                            VideoEncoder videoEncoder2 = VideoEncoder.this;
                            if (!videoEncoder2.d) {
                                FrameItem frameItem = null;
                                try {
                                    videoEncoder2.lock();
                                    while (VideoEncoder.this.s.queueSize() == 0 && !VideoEncoder.this.d) {
                                        try {
                                            VideoEncoder.this.r.await();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    frameItem = VideoEncoder.this.s.getFrameForConsumer();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                } catch (Throwable th) {
                                    VideoEncoder.this.unLock();
                                    throw th;
                                }
                                VideoEncoder.this.unLock();
                                if (frameItem != null) {
                                    GLFramebuffer gLFramebuffer = frameItem.mFramebuffer;
                                    VideoEncoder.this.drain();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("VideoEncoder frame_item_index = ");
                                    sb.append(frameItem.mFrameIndex);
                                    String sb2 = sb.toString();
                                    String str = VideoEncoder.v;
                                    CodecLog.d(str, sb2);
                                    long j = frameItem.a;
                                    if (0 != j) {
                                        GLES30.glWaitSync(j, 0, -1);
                                    }
                                    VideoEncoder.this.K.renderWithTextureId(gLFramebuffer.getTextureId());
                                    try {
                                        VideoEncoder.this.lock();
                                        VideoEncoder.this.s.addEmptyFrameForConsumer();
                                    } catch (Exception e3) {
                                        e3.printStackTrace();
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("VideoEncoder meet exception when add item : ");
                                        sb3.append(e3.getMessage());
                                        CodecLog.e(str, sb3.toString());
                                    } catch (Throwable th2) {
                                        VideoEncoder.this.unLock();
                                        throw th2;
                                    }
                                    VideoEncoder.this.unLock();
                                    VideoEncoder.this.I.swapBuffers();
                                }
                            } else {
                                videoEncoder2.a = true;
                                videoEncoder2.i.signalEndOfInputStream();
                                VideoEncoder.this.drain();
                                VideoEncoder.this.c();
                                VideoEncoder.this.I.makeUnCurrent();
                                return;
                            }
                        }
                    } catch (Exception e4) {
                        e4.printStackTrace();
                        RecordingListener recordingListener = VideoEncoder.this.o;
                        if (recordingListener != null) {
                            recordingListener.onRecordingListener(563, Integer.valueOf(0));
                        }
                    }
                }
            };
            this.H.start();
            CodecLog.d(v, "VideoEncoder is started.");
            return;
        }
        throw new RuntimeException("Video encoder thread has been started already, can not start twice.");
    }

    public void stopRecording() {
        super.stopRecording();
        try {
            this.q.lock();
        } catch (Exception e) {
            String str = v;
            StringBuilder sb = new StringBuilder();
            sb.append("stopRecording()-> meet error when get lock : ");
            sb.append(e.getMessage());
            CodecLog.e(str, sb.toString());
        } catch (Throwable th) {
            sinalCondition();
            this.q.unlock();
            throw th;
        }
        sinalCondition();
        this.q.unlock();
    }
}
