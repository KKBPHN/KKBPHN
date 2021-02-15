package com.ss.android.ttve.mediacodec;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.Surface;
import androidx.annotation.Keep;
import com.android.camera.constant.DurationConstant;
import com.ss.android.ttve.common.TEEglStateSaver;
import com.ss.android.ttve.common.TESharedContext;
import com.ss.android.ttve.common.TETextureDrawer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

@SuppressLint({"NewApi"})
@Keep
public class TEMediaCodecDecoder implements OnFrameAvailableListener {
    private static final int ERROR_EOF = -1;
    private static final int ERROR_FAIL = -2;
    private static final int ERROR_OK = 0;
    private static final int ERROR_UNUSUAL = -3;
    private static final String TAG = "MediaCodecDecoder";
    private static final String VIDEO_MIME_TYPE = "video/avc";
    private static final boolean m_verbose = false;
    private final int MAX_DELAY_COUNT = 10;
    private final long MAX_SLEEP_MS = 0;
    private boolean m_bIsNeedReconfigure = false;
    private BufferInfo m_bufferInfo = new BufferInfo();
    private MediaCodec m_decoder = null;
    private boolean m_decoderStarted = false;
    private TEEglStateSaver m_eglStateSaver = null;
    private ByteBuffer m_extraDataBuf = null;
    private MediaFormat m_format = null;
    private boolean m_frameAvailable = false;
    private final Object m_frameSyncObject = new Object();
    private int m_iCurCount = 0;
    private int m_iHeight = 0;
    private int m_iWidth = 0;
    private boolean m_inputBufferQueued = false;
    private int m_pendingInputFrameCount = 0;
    private ByteBuffer m_ppsBuf = null;
    private boolean m_sawInputEOS = false;
    private boolean m_sawOutputEOS = false;
    private TESharedContext m_sharedContext = null;
    private ByteBuffer m_spsBuf = null;
    private Surface m_surface = null;
    private int[] m_surfaceTexID = new int[1];
    private SurfaceTexture m_surfaceTexture = null;
    private TETextureDrawer m_textureDrawer = null;
    private SOES2DTextureRender m_textureRender = null;
    private long m_timestampOfCurTexFrame = Long.MIN_VALUE;
    private long m_timestampOfLastDecodedFrame = Long.MIN_VALUE;

    class SOES2DTextureRender {
        private static final int FLOAT_SIZE_BYTES = 4;
        private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
        private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
        private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 20;
        private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
        private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
        private float[] mMVPMatrix = new float[16];
        private int mProgram;
        private float[] mSTMatrix = new float[16];
        private FloatBuffer mTriangleVertices = ByteBuffer.allocateDirect(this.mTriangleVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        private final float[] mTriangleVerticesData = {-1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
        private int maPositionHandle;
        private int maTextureHandle;
        private int[] muFrameBuffer = new int[1];
        private int muMVPMatrixHandle;
        private int muSTMatrixHandle;

        public SOES2DTextureRender(SurfaceTexture surfaceTexture) {
            this.mTriangleVertices.put(this.mTriangleVerticesData).position(0);
            float[] fArr = this.mSTMatrix;
            if (surfaceTexture != null) {
                surfaceTexture.getTransformMatrix(fArr);
            } else {
                Matrix.setIdentityM(fArr, 0);
            }
        }

        private int createProgram(String str, String str2) {
            int loadShader = loadShader(35633, str);
            int i = 0;
            if (loadShader == 0) {
                return 0;
            }
            int loadShader2 = loadShader(35632, str2);
            if (loadShader2 == 0) {
                return 0;
            }
            int glCreateProgram = GLES20.glCreateProgram();
            checkGlError("glCreateProgram");
            String str3 = TEMediaCodecDecoder.TAG;
            if (glCreateProgram == 0) {
                Log.e(str3, "Could not create program");
            }
            GLES20.glAttachShader(glCreateProgram, loadShader);
            String str4 = "glAttachShader";
            checkGlError(str4);
            GLES20.glAttachShader(glCreateProgram, loadShader2);
            checkGlError(str4);
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] != 1) {
                Log.e(str3, "Could not link program: ");
                Log.e(str3, GLES20.glGetProgramInfoLog(glCreateProgram));
                GLES20.glDeleteProgram(glCreateProgram);
            } else {
                i = glCreateProgram;
            }
            return i;
        }

        private int loadShader(int i, String str) {
            int glCreateShader = GLES20.glCreateShader(i);
            StringBuilder sb = new StringBuilder();
            sb.append("glCreateShader type=");
            sb.append(i);
            checkGlError(sb.toString());
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] != 0) {
                return glCreateShader;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not compile shader ");
            sb2.append(i);
            sb2.append(":");
            String sb3 = sb2.toString();
            String str2 = TEMediaCodecDecoder.TAG;
            Log.e(str2, sb3);
            StringBuilder sb4 = new StringBuilder();
            sb4.append(" ");
            sb4.append(GLES20.glGetShaderInfoLog(glCreateShader));
            Log.e(str2, sb4.toString());
            GLES20.glDeleteShader(glCreateShader);
            return 0;
        }

        public void changeFragmentShader(String str) {
            if (str == null) {
                str = FRAGMENT_SHADER;
            }
            GLES20.glDeleteProgram(this.mProgram);
            this.mProgram = createProgram(VERTEX_SHADER, str);
            if (this.mProgram == 0) {
                throw new RuntimeException("failed creating program");
            }
        }

        public void checkGlError(String str) {
            int glGetError = GLES20.glGetError();
            if (glGetError != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                String str2 = ": glError ";
                sb.append(str2);
                sb.append(glGetError);
                Log.e(TEMediaCodecDecoder.TAG, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(str2);
                sb2.append(glGetError);
                throw new RuntimeException(sb2.toString());
            }
        }

        public void drawFrame(int i, int i2, int i3, int i4) {
            GLES20.glViewport(0, 0, i, i2);
            GLES20.glBindTexture(3553, i4);
            String str = "glBindTexture";
            checkGlError(str);
            GLES20.glBindFramebuffer(36160, this.muFrameBuffer[0]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, i4, 0);
            checkGlError("glFramebufferTexture2D");
            checkGlError("onDrawFrame start");
            GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glClear(16640);
            GLES20.glUseProgram(this.mProgram);
            checkGlError("glUseProgram");
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(36197, i3);
            checkGlError(str);
            this.mTriangleVertices.position(0);
            GLES20.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 20, this.mTriangleVertices);
            checkGlError("glVertexAttribPointer maPosition");
            GLES20.glEnableVertexAttribArray(this.maPositionHandle);
            checkGlError("glEnableVertexAttribArray maPositionHandle");
            this.mTriangleVertices.position(3);
            GLES20.glVertexAttribPointer(this.maTextureHandle, 2, 5126, false, 20, this.mTriangleVertices);
            checkGlError("glVertexAttribPointer maTextureHandle");
            GLES20.glEnableVertexAttribArray(this.maTextureHandle);
            checkGlError("glEnableVertexAttribArray maTextureHandle");
            Matrix.setIdentityM(this.mMVPMatrix, 0);
            GLES20.glUniformMatrix4fv(this.muMVPMatrixHandle, 1, false, this.mMVPMatrix, 0);
            GLES20.glUniformMatrix4fv(this.muSTMatrixHandle, 1, false, this.mSTMatrix, 0);
            GLES20.glDrawArrays(5, 0, 4);
            checkGlError("glDrawArrays");
            GLES20.glDisableVertexAttribArray(this.maPositionHandle);
            GLES20.glDisableVertexAttribArray(this.maTextureHandle);
            GLES20.glBindTexture(36197, 0);
            GLES20.glBindTexture(3553, 0);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, 0, 0);
            GLES20.glBindFramebuffer(36160, 0);
            GLES20.glFinish();
        }

        public void release() {
            int i = this.mProgram;
            if (i != 0) {
                GLES20.glDeleteProgram(i);
                this.mProgram = 0;
            }
            int[] iArr = this.muFrameBuffer;
            if (iArr[0] != 0) {
                GLES20.glDeleteFramebuffers(1, iArr, 0);
            }
        }

        public void surfaceCreated() {
            this.mProgram = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
            int i = this.mProgram;
            if (i != 0) {
                this.maPositionHandle = GLES20.glGetAttribLocation(i, "aPosition");
                checkGlError("glGetAttribLocation aPosition");
                if (this.maPositionHandle != -1) {
                    this.maTextureHandle = GLES20.glGetAttribLocation(this.mProgram, "aTextureCoord");
                    checkGlError("glGetAttribLocation aTextureCoord");
                    if (this.maTextureHandle != -1) {
                        this.muMVPMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uMVPMatrix");
                        checkGlError("glGetUniformLocation uMVPMatrix");
                        if (this.muMVPMatrixHandle != -1) {
                            this.muSTMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uSTMatrix");
                            checkGlError("glGetUniformLocation uSTMatrix");
                            if (this.muSTMatrixHandle != -1) {
                                GLES20.glGenFramebuffers(1, this.muFrameBuffer, 0);
                                checkGlError("glGenFramebuffers");
                                return;
                            }
                            throw new RuntimeException("Could not get attrib location for uSTMatrix");
                        }
                        throw new RuntimeException("Could not get attrib location for uMVPMatrix");
                    }
                    throw new RuntimeException("Could not get attrib location for aTextureCoord");
                }
                throw new RuntimeException("Could not get attrib location for aPosition");
            }
            throw new RuntimeException("failed creating program");
        }
    }

    private boolean AwaitNewImage() {
        synchronized (this.m_frameSyncObject) {
            do {
                if (!this.m_frameAvailable) {
                    try {
                        this.m_frameSyncObject.wait(500);
                    } catch (InterruptedException e) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(e.getMessage());
                        Log.e(str, sb.toString());
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    this.m_frameAvailable = false;
                    return true;
                }
            } while (this.m_frameAvailable);
            Log.e(TAG, "Frame wait timed out!");
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0099  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00aa  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int DecodeFrame2Surface(byte[] bArr, int i, long j) {
        boolean z;
        int i2;
        int dequeueOutputBuffer;
        String str;
        int i3 = i;
        boolean z2 = this.m_sawInputEOS;
        String str2 = TAG;
        boolean z3 = true;
        if (!z2) {
            int dequeueInputBuffer = this.m_decoder.dequeueInputBuffer(30000);
            int i4 = 0;
            while (true) {
                if (dequeueInputBuffer >= 0) {
                    break;
                }
                try {
                    Thread.sleep(5, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dequeueInputBuffer = this.m_decoder.dequeueInputBuffer(30000);
                i4++;
                if (i4 >= 20) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("try dequeueInputBuffer timeout -- ");
                    sb.append(i4);
                    Log.e(str2, sb.toString());
                    break;
                }
            }
            int i5 = dequeueInputBuffer;
            if (i5 >= 0) {
                ByteBuffer inputBufferByIdx = getInputBufferByIdx(i5);
                if (i3 == 0) {
                    this.m_decoder.queueInputBuffer(i5, 0, 0, 0, 4);
                    this.m_sawInputEOS = true;
                } else {
                    inputBufferByIdx.clear();
                    inputBufferByIdx.put(bArr, 0, i3);
                    this.m_decoder.queueInputBuffer(i5, 0, i, j, 0);
                    this.m_inputBufferQueued = true;
                    this.m_pendingInputFrameCount++;
                }
            } else {
                Log.e(str2, "Input buffer not available");
                z = true;
                i2 = !this.m_sawOutputEOS ? 600000 : this.m_pendingInputFrameCount > 2 ? DurationConstant.DURATION_VIDEO_BOKEH_RECORDING : 0;
                while (true) {
                    dequeueOutputBuffer = this.m_decoder.dequeueOutputBuffer(this.m_bufferInfo, (long) i2);
                    if (dequeueOutputBuffer == -3) {
                        if (dequeueOutputBuffer != -2) {
                            break;
                        }
                        this.m_decoder.getOutputFormat();
                    }
                }
                if (dequeueOutputBuffer != -1) {
                    return z ? -3 : -2;
                }
                if (dequeueOutputBuffer < 0) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unexpected result from decoder.dequeueOutputBuffer: ");
                    sb2.append(dequeueOutputBuffer);
                    str = sb2.toString();
                } else {
                    if ((this.m_bufferInfo.flags & 4) != 0) {
                        this.m_sawOutputEOS = true;
                    }
                    if (!this.m_sawOutputEOS) {
                        this.m_timestampOfLastDecodedFrame = this.m_bufferInfo.presentationTimeUs;
                        this.m_pendingInputFrameCount--;
                    } else {
                        z3 = false;
                    }
                    this.m_decoder.releaseOutputBuffer(dequeueOutputBuffer, z3);
                    if (!z3) {
                        return -1;
                    }
                    if (AwaitNewImage()) {
                        this.m_timestampOfCurTexFrame = this.m_bufferInfo.presentationTimeUs;
                        int glGetError = GLES20.glGetError();
                        if (glGetError == 0) {
                            return 0;
                        }
                        StringBuilder sb3 = new StringBuilder();
                        String str3 = ": glError ";
                        sb3.append(str3);
                        sb3.append(glGetError);
                        Log.e(str2, sb3.toString());
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str3);
                        sb4.append(glGetError);
                        throw new RuntimeException(sb4.toString());
                    }
                    str = "Render decoded frame to surface texture failed!";
                }
                Log.e(str2, str);
                return -2;
            }
        }
        z = false;
        if (!this.m_sawOutputEOS) {
        }
        while (true) {
            dequeueOutputBuffer = this.m_decoder.dequeueOutputBuffer(this.m_bufferInfo, (long) i2);
            if (dequeueOutputBuffer == -3) {
            }
        }
        if (dequeueOutputBuffer != -1) {
        }
    }

    public static boolean IsInAndriodHardwareBlacklist() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        if (str.compareTo("Meizu") == 0 && str2.compareTo("m2") == 0) {
            return true;
        }
        return str.compareTo("Xiaomi") == 0 && str2.compareTo("MI 4W") == 0;
    }

    public static boolean IsInAndriodHardwareWhitelist() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        if (str.compareTo("samsung") == 0 && str2.compareTo("GT-I9152") == 0) {
            return true;
        }
        return str.compareTo("HUAWEI") == 0 && str2.compareTo("HUAWEI P6-C00") == 0;
    }

    private boolean IsValid() {
        return this.m_decoder != null;
    }

    private boolean SetupDecoder(String str) {
        try {
            this.m_decoder = MediaCodec.createDecoderByType(str);
            this.m_decoder.configure(this.m_format, this.m_surface, null, 0);
            this.m_decoder.start();
            this.m_decoderStarted = true;
            this.m_iCurCount = 0;
            return true;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(e.getMessage());
            Log.e(TAG, sb.toString());
            e.printStackTrace();
            CleanupDecoder();
            return false;
        }
    }

    private int createTexture() {
        GLES20.glGenTextures(1, this.m_surfaceTexID, 0);
        int[] iArr = this.m_surfaceTexID;
        if (iArr[0] <= 0) {
            Log.e(TAG, "createTexture failed");
            return 0;
        }
        GLES20.glBindTexture(36197, iArr[0]);
        GLES20.glTexParameterf(36197, 10241, 9728.0f);
        GLES20.glTexParameterf(36197, 10240, 9729.0f);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        return this.m_surfaceTexID[0];
    }

    private void deleteTexture() {
        int[] iArr = this.m_surfaceTexID;
        if (iArr[0] != 0) {
            GLES20.glDeleteTextures(1, iArr, 0);
            this.m_surfaceTexID[0] = 0;
        }
    }

    private ByteBuffer getInputBufferByIdx(int i) {
        int i2 = VERSION.SDK_INT;
        MediaCodec mediaCodec = this.m_decoder;
        return i2 >= 21 ? mediaCodec.getInputBuffer(i) : mediaCodec.getInputBuffers()[i];
    }

    private int reconfigureMediaFormat() {
        try {
            this.m_format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, this.m_iWidth, this.m_iHeight);
            if (this.m_spsBuf != null) {
                this.m_format.setByteBuffer("csd-0", this.m_spsBuf);
            }
            if (this.m_ppsBuf != null) {
                this.m_format.setByteBuffer("csd-1", this.m_ppsBuf);
            }
            if (VERSION.SDK_INT != 16) {
                return 0;
            }
            this.m_format.setInteger("max-input-size", 0);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int restartDecoder() {
        stopDecoder();
        return startDecoder();
    }

    private int startDecoder() {
        boolean IsValid = IsValid();
        String str = TAG;
        if (IsValid) {
            Log.e(str, "You can't call startDecoder() twice!");
            return -1;
        }
        int i = this.m_surfaceTexID[0];
        if (i == 0) {
            i = createTexture();
        }
        if (i == 0) {
            return -1;
        }
        try {
            this.m_surfaceTexture = new SurfaceTexture(i);
            StringBuilder sb = new StringBuilder();
            sb.append("Surface texture with texture (id=");
            sb.append(i);
            sb.append(") has been created.");
            Log.e(str, sb.toString());
            this.m_surfaceTexture.setOnFrameAvailableListener(this);
            this.m_surface = new Surface(this.m_surfaceTexture);
            this.m_textureRender = new SOES2DTextureRender(this.m_surfaceTexture);
            this.m_textureRender.surfaceCreated();
            if (SetupDecoder(VIDEO_MIME_TYPE)) {
                return 0;
            }
            stopDecoder();
            return -1;
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(e.getMessage());
            Log.e(str, sb2.toString());
            e.printStackTrace();
            stopDecoder();
            return -1;
        }
    }

    private int stopDecoder() {
        CleanupDecoder();
        SOES2DTextureRender sOES2DTextureRender = this.m_textureRender;
        if (sOES2DTextureRender != null) {
            sOES2DTextureRender.release();
            this.m_textureRender = null;
        }
        Surface surface = this.m_surface;
        if (surface != null) {
            surface.release();
            this.m_surface = null;
        }
        SurfaceTexture surfaceTexture = this.m_surfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.setOnFrameAvailableListener(null);
            this.m_surfaceTexture.release();
            this.m_surfaceTexture = null;
        }
        return 0;
    }

    public void CleanupDecoder() {
        MediaCodec mediaCodec = this.m_decoder;
        if (mediaCodec != null) {
            if (this.m_decoderStarted) {
                try {
                    if (this.m_inputBufferQueued) {
                        mediaCodec.flush();
                        this.m_inputBufferQueued = false;
                    }
                    this.m_decoder.stop();
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(e.getMessage());
                    Log.e(TAG, sb.toString());
                    e.printStackTrace();
                }
                this.m_decoderStarted = false;
            }
            this.m_decoder.release();
            this.m_decoder = null;
        }
        this.m_timestampOfLastDecodedFrame = Long.MIN_VALUE;
        this.m_timestampOfCurTexFrame = Long.MIN_VALUE;
        this.m_pendingInputFrameCount = 0;
        this.m_sawInputEOS = false;
        this.m_sawOutputEOS = false;
    }

    public int closeEncoder() {
        stopDecoder();
        TETextureDrawer tETextureDrawer = this.m_textureDrawer;
        if (tETextureDrawer != null) {
            tETextureDrawer.release();
            this.m_textureDrawer = null;
        }
        deleteTexture();
        return 0;
    }

    public int decodeFrame(byte[] bArr, int i, long j, int i2) {
        if (this.m_eglStateSaver == null) {
            this.m_eglStateSaver = new TEEglStateSaver();
            this.m_eglStateSaver.saveEGLState();
        }
        boolean equals = EGL14.eglGetCurrentContext().equals(this.m_eglStateSaver.getSavedEGLContext());
        String str = TAG;
        if (!equals) {
            StringBuilder sb = new StringBuilder();
            sb.append("eglGetCurrentContext = ");
            sb.append(EGL14.eglGetCurrentContext());
            sb.append(" getSavedEGLContext = ");
            sb.append(this.m_eglStateSaver.getSavedEGLContext());
            Log.e(str, sb.toString());
            this.m_bIsNeedReconfigure = true;
        }
        if (this.m_bIsNeedReconfigure) {
            restartDecoder();
            this.m_bIsNeedReconfigure = false;
            this.m_eglStateSaver.saveEGLState();
        }
        if (this.m_decoder == null) {
            return -2;
        }
        long currentTimeMillis = System.currentTimeMillis();
        int DecodeFrame2Surface = DecodeFrame2Surface(bArr, i, j);
        long currentTimeMillis2 = 0 - (System.currentTimeMillis() - currentTimeMillis);
        if (this.m_iCurCount < 10 && currentTimeMillis2 > 0) {
            try {
                Thread.sleep(currentTimeMillis2, 0);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Sleep ");
                sb2.append(currentTimeMillis2);
                sb2.append("ms for delay output!!!");
                Log.e(str, sb2.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.m_iCurCount++;
        }
        if (DecodeFrame2Surface == 0) {
            this.m_surfaceTexture.updateTexImage();
            SOES2DTextureRender sOES2DTextureRender = this.m_textureRender;
            if (sOES2DTextureRender != null && i2 > 0) {
                sOES2DTextureRender.drawFrame(this.m_iWidth, this.m_iHeight, this.m_surfaceTexID[0], i2);
            }
        }
        return DecodeFrame2Surface;
    }

    public int flushDecoder() {
        StringBuilder sb = new StringBuilder();
        sb.append("flushDecoder m_decoder = ");
        sb.append(this.m_decoder);
        Log.e(TAG, sb.toString());
        MediaCodec mediaCodec = this.m_decoder;
        if (mediaCodec == null) {
            return -3;
        }
        try {
            if (!this.m_sawInputEOS) {
                if (!this.m_sawOutputEOS) {
                    if (!this.m_inputBufferQueued) {
                        return -3;
                    }
                    mediaCodec.flush();
                    this.m_inputBufferQueued = false;
                    this.m_pendingInputFrameCount = 0;
                    return 0;
                }
            }
            CleanupDecoder();
            if (!SetupDecoder(VIDEO_MIME_TYPE)) {
                return -2;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -3;
        }
    }

    public int getInfoByFlag(int[] iArr, int i) {
        if (i == 1) {
            long j = this.m_timestampOfLastDecodedFrame;
            iArr[0] = (int) (j & -1);
            iArr[1] = (int) ((j >> 32) & -1);
            return 2;
        } else if (i != 2) {
            return 0;
        } else {
            long j2 = this.m_timestampOfCurTexFrame;
            iArr[0] = (int) (j2 & -1);
            iArr[1] = (int) ((j2 >> 32) & -1);
            return 2;
        }
    }

    public int initDecoder(int i, int i2, byte[] bArr, int i3, byte[] bArr2, int i4) {
        StringBuilder sb = new StringBuilder();
        sb.append("width = ");
        sb.append(i);
        sb.append(" height = ");
        sb.append(i2);
        Log.e(TAG, sb.toString());
        int encoder = setEncoder(i, i2, bArr, i3, bArr2, i4);
        if (this.m_bIsNeedReconfigure) {
            encoder = reconfigureMediaFormat();
        }
        if (this.m_eglStateSaver == null) {
            this.m_eglStateSaver = new TEEglStateSaver();
            this.m_eglStateSaver.saveEGLState();
        }
        if (this.m_bIsNeedReconfigure) {
            restartDecoder();
            this.m_bIsNeedReconfigure = false;
        }
        return encoder;
    }

    public boolean initTextureDrawer() {
        TETextureDrawer tETextureDrawer = this.m_textureDrawer;
        if (tETextureDrawer != null) {
            tETextureDrawer.release();
            this.m_textureDrawer = null;
        }
        this.m_textureDrawer = TETextureDrawer.create();
        TETextureDrawer tETextureDrawer2 = this.m_textureDrawer;
        if (tETextureDrawer2 == null) {
            return false;
        }
        tETextureDrawer2.setRotation(0.0f);
        this.m_textureDrawer.setFlipScale(1.0f, -1.0f);
        return true;
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this.m_frameSyncObject) {
            if (this.m_frameAvailable) {
                Log.e(TAG, "m_frameAvailable already set, frame could be dropped!");
            }
            this.m_frameAvailable = true;
            this.m_frameSyncObject.notifyAll();
        }
    }

    public int setEncoder(int i, int i2, byte[] bArr, int i3, byte[] bArr2, int i4) {
        this.m_iWidth = i;
        this.m_iHeight = i2;
        this.m_spsBuf = null;
        this.m_ppsBuf = null;
        if (i3 > 0) {
            this.m_spsBuf = ByteBuffer.wrap(bArr, 0, i3);
        }
        if (i4 > 0) {
            this.m_ppsBuf = ByteBuffer.wrap(bArr2, 0, i4);
        }
        this.m_bIsNeedReconfigure = true;
        return 0;
    }
}
