package com.android.camera.module.impl.component;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.android.camera.log.Log;
import com.xiaomi.inceptionmediaprocess.OpenGlRender;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MiFilmDreamGLSurfaceViewRender {
    private static final String TAG = "MiFilmDreamGLSurfaceViewRender";
    private static final String cameraFragmentShaderString = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;uniform samplerExternalOES tex_rgb;varying vec2 textureOut;uniform bool isFrontCamera;void main() {vec2 uv = textureOut;if(isFrontCamera) { uv.y = 1.0 - textureOut.y;}gl_FragColor = texture2D(tex_rgb, uv);}";
    private static final String dispalyFragmentShaderString = "precision mediump float;uniform sampler2D tex_rgb;varying vec2 textureOut;void main() {vec4 res = texture2D(tex_rgb, textureOut);gl_FragColor = vec4(res.rgb, 1.0);}";
    private static final String previewShaderString = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES tex_rgb;uniform sampler2D  filter_rgb;\nuniform bool extraVideoFilter;\nvarying vec2 textureOut;\nuniform bool isFrontCamera;uniform int rotate_angle;uniform bool enable_film_picture;vec4 InceptionTransition(vec2 uv) { \n    if(enable_film_picture) { \n        float half_y = (1.0 - 1920.0 /2.39 /1080.0)/2.0; \n        if(uv.y < half_y || uv.y > (1.0 - half_y)) { \n            return vec4(0.0,0.0,0.0,1.0); \n        } \n    } \n    if(rotate_angle == 0 || rotate_angle == 90 || rotate_angle == 270 || rotate_angle == 180) { \n        if(isFrontCamera) { \n            if(uv.y > 0.5) { \n                uv.y = 1.0 - uv.y; \n            } \n        } else { \n            if(uv.y < 0.5) { \n                uv.y = 1.0 - uv.y; \n            } \n        } \n    } else { \n        if(isFrontCamera) { \n            if(uv.y < 0.5) { \n                uv.y =  1.0 - uv.y; \n            } \n        } else { \n            if(uv.y > 0.5) { \n                uv.y =  1.0 - uv.y; \n            } \n        }  \n    } \n    vec4 res = texture2D(tex_rgb, uv); \n    if (extraVideoFilter) { \n        float quadx, quady, x, y; \n        float bi = floor(res.b * 63.0); \n        float mixratio = res.b * 63.0 - floor(res.b * 63.0); \n        quady = floor(bi / 8.0); \n        quadx = bi - quady * 8.0; \n        x = quadx * 64.0 + clamp(res.r * 63.0, 1.0, 63.0); \n        y = quady * 64.0 + clamp(res.g * 63.0, 1.0, 63.0); \n        vec2 poss1 = vec2(x / 512.0, y / 512.0); \n        bi = bi + 1.0; \n        quady = floor(bi / 8.0); \n        quadx = bi - quady * 8.0; \n        x = quadx * 64.0 + clamp(res.r * 63.0, 1.0, 63.0); \n        y = quady * 64.0 + clamp(res.g * 63.0, 1.0, 63.0); \n        vec2 poss2 = vec2(x / 512.0, y / 512.0); \n        vec4 color1 = texture2D(filter_rgb, poss1); \n        vec4 color2 = texture2D(filter_rgb, poss2); \n        res = mix(color1, color2, mixratio); \n    } \n    return res; \n} \nvoid main() {\n    vec2 uv = vec2(textureOut.x, textureOut.y);\n gl_FragColor = InceptionTransition(uv);\n}";
    private static float[] textureVertices = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private static final String vertexShaderString = "attribute vec4 vertexIn;attribute vec2 textureIn;varying vec2 textureOut;uniform mat4 modelViewProjectionMatrix;void main() {gl_Position = modelViewProjectionMatrix*vertexIn ;textureOut = (vec4(textureIn.x ,  textureIn.y , 0.0, 1.0)).xy;}";
    private static float[] vertexVertices = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    public int ATTRIB_TEXTURE = 0;
    public int ATTRIB_TEXTURE2 = 0;
    public int ATTRIB_VERTEX = 0;
    public int ATTRIB_VERTEX2 = 0;
    ByteBuffer RGBColor = null;
    private final int TABLESIZE = 512;
    private int backcamera;
    private int[] camera_texture_id = new int[1];
    private boolean currentCamera = true;
    private boolean enable_film_picture = false;
    private int enable_film_picture_id;
    private int extraVideoFilter;
    private int fbobackcamera;
    private int filter_rgb;
    private boolean isFrontCamera = false;
    private int mFbo;
    private int[] mFboTexture;
    private int mFragshaderRgb;
    private int mFramebufferTexture;
    private SurfaceTexture mInputSurfaceTexture;
    private boolean mNeedDrawFrame = false;
    private OpenGlRender mOpenGlRender;
    private int mProgramID;
    private int mProgramID2;
    private int[] mRgbTexture = new int[1];
    /* access modifiers changed from: private */
    public GLSurfaceView mTargetSurface;
    private final OnFrameAvailableListener mUpdateListener = new OnFrameAvailableListener() {
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            StringBuilder sb = new StringBuilder();
            sb.append("camera surceface texture available: ");
            sb.append(surfaceTexture);
            Log.d(MiFilmDreamGLSurfaceViewRender.TAG, sb.toString());
            MiFilmDreamGLSurfaceViewRender.this.mTargetSurface.requestRender();
        }
    };
    private int mWindowHeight;
    private int mWindowWidth;
    private int mcamera_fragshader_texture;
    private int mcamera_texture;
    private int mmodelMatrix;
    private int mmodelMatrixPreviewFilter;
    private int mpreviewFilterProgramID;
    private int mrotate_angle = 0;
    private float[] mtransformMatrix = new float[16];
    private int rotate_angle_id;
    ByteBuffer textureVertices_buffer = null;
    ByteBuffer vertexVertices_buffer = null;

    public MiFilmDreamGLSurfaceViewRender(OpenGlRender openGlRender) {
        this.mOpenGlRender = openGlRender;
    }

    private void TransferExternalImagetoFbo() {
        GLES20.glViewport(0, 0, 3840, 2160);
        GLES20.glBindFramebuffer(36160, this.mFbo);
        GLES20.glUseProgram(this.mProgramID);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, this.mcamera_texture);
        checkNoGLES2Error();
        GLES20.glUniform1i(this.mcamera_fragshader_texture, 0);
        checkNoGLES2Error();
        GLES20.glUniform1i(this.fbobackcamera, this.isFrontCamera ? 1 : 0);
        checkNoGLES2Error();
        Matrix.setIdentityM(this.mtransformMatrix, 0);
        if (this.isFrontCamera) {
            Matrix.rotateM(this.mtransformMatrix, 0, 180.0f, 0.0f, 0.0f, 1.0f);
        }
        GLES20.glUniformMatrix4fv(this.mmodelMatrix, 1, false, this.mtransformMatrix, 0);
        checkNoGLES2Error();
        GLES20.glEnableVertexAttribArray(this.ATTRIB_VERTEX);
        checkNoGLES2Error();
        GLES20.glVertexAttribPointer(this.ATTRIB_VERTEX, 2, 5126, false, 0, this.vertexVertices_buffer);
        checkNoGLES2Error();
        GLES20.glEnableVertexAttribArray(this.ATTRIB_TEXTURE);
        checkNoGLES2Error();
        GLES20.glVertexAttribPointer(this.ATTRIB_TEXTURE, 2, 5126, false, 0, this.textureVertices_buffer);
        checkNoGLES2Error();
        GLES20.glDrawArrays(5, 0, 4);
        checkNoGLES2Error();
        GLES20.glFlush();
        checkNoGLES2Error();
        GLES20.glDisableVertexAttribArray(this.ATTRIB_VERTEX);
        GLES20.glDisableVertexAttribArray(this.ATTRIB_TEXTURE);
        checkNoGLES2Error();
        GLES20.glBindFramebuffer(36160, 0);
        checkNoGLES2Error();
    }

    private static void abortUnless(boolean z, String str) {
        if (!z) {
            throw new RuntimeException(str);
        }
    }

    private static void checkNoGLES2Error() {
        int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("GLES20 error:");
            sb.append(glGetError);
            Log.e(TAG, sb.toString());
        }
        boolean z = glGetError == 0;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("GLES20 error: ");
        sb2.append(glGetError);
        abortUnless(z, sb2.toString());
    }

    private int loadShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        StringBuilder sb = new StringBuilder();
        sb.append("shader: ");
        sb.append(glCreateShader);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.d(str2, sb2);
        if (glCreateShader != 0) {
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                GLES20.glDeleteShader(glCreateShader);
                glCreateShader = 0;
            }
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("end shader: ");
        sb3.append(glCreateShader);
        Log.d(str2, sb3.toString());
        return glCreateShader;
    }

    public void DrawCameraPreview(int i, int i2, int i3, int i4) {
        float f;
        int i5;
        float[] fArr;
        TransferExternalImagetoFbo();
        GLES20.glViewport(i, i2, i3, i4);
        GLES20.glUseProgram(this.mpreviewFilterProgramID);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, this.mcamera_texture);
        checkNoGLES2Error();
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(3553, this.mRgbTexture[0]);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glUniform1i(this.filter_rgb, 1);
        GLES20.glTexImage2D(3553, 0, 6408, 512, 512, 0, 6408, 5121, this.RGBColor);
        GLES20.glUniform1i(this.extraVideoFilter, this.RGBColor != null ? 1 : 0);
        checkNoGLES2Error();
        GLES20.glUniform1i(this.rotate_angle_id, this.mrotate_angle);
        checkNoGLES2Error();
        GLES20.glUniform1i(this.enable_film_picture_id, this.enable_film_picture ? 1 : 0);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.mpreviewFilterProgramID, "tex_rgb"), 0);
        checkNoGLES2Error();
        boolean z = this.currentCamera;
        boolean z2 = this.isFrontCamera;
        if (z != z2) {
            this.currentCamera = z2;
        } else {
            GLES20.glUniform1i(this.backcamera, z2 ? 1 : 0);
            checkNoGLES2Error();
            if (this.isFrontCamera) {
                Matrix.setIdentityM(this.mtransformMatrix, 0);
                fArr = this.mtransformMatrix;
                i5 = 0;
                f = 90.0f;
            } else {
                Matrix.setIdentityM(this.mtransformMatrix, 0);
                fArr = this.mtransformMatrix;
                i5 = 0;
                f = -90.0f;
            }
            Matrix.rotateM(fArr, i5, f, 0.0f, 0.0f, 1.0f);
            GLES20.glUniformMatrix4fv(this.mmodelMatrix, 1, false, this.mtransformMatrix, 0);
            checkNoGLES2Error();
        }
        int glGetAttribLocation = GLES20.glGetAttribLocation(this.mpreviewFilterProgramID, "vertexIn");
        GLES20.glEnableVertexAttribArray(glGetAttribLocation);
        checkNoGLES2Error();
        GLES20.glVertexAttribPointer(glGetAttribLocation, 2, 5126, false, 0, this.vertexVertices_buffer);
        checkNoGLES2Error();
        int glGetAttribLocation2 = GLES20.glGetAttribLocation(this.mpreviewFilterProgramID, "textureIn");
        GLES20.glEnableVertexAttribArray(glGetAttribLocation2);
        checkNoGLES2Error();
        GLES20.glVertexAttribPointer(glGetAttribLocation2, 2, 5126, false, 0, this.textureVertices_buffer);
        checkNoGLES2Error();
        GLES20.glDrawArrays(5, 0, 4);
        checkNoGLES2Error();
        GLES20.glFlush();
        checkNoGLES2Error();
        GLES20.glDisableVertexAttribArray(glGetAttribLocation);
        GLES20.glDisableVertexAttribArray(glGetAttribLocation2);
        checkNoGLES2Error();
    }

    public void EnableFilmPicture(boolean z) {
        this.enable_film_picture = z;
    }

    /* access modifiers changed from: 0000 */
    public void InitShaders() {
        this.vertexVertices_buffer = ByteBuffer.allocateDirect(vertexVertices.length * 4);
        this.vertexVertices_buffer.order(ByteOrder.nativeOrder());
        this.vertexVertices_buffer.asFloatBuffer().put(vertexVertices);
        this.vertexVertices_buffer.position(0);
        this.textureVertices_buffer = ByteBuffer.allocateDirect(textureVertices.length * 4);
        this.textureVertices_buffer.order(ByteOrder.nativeOrder());
        this.textureVertices_buffer.asFloatBuffer().put(textureVertices);
        this.textureVertices_buffer.position(0);
        String str = vertexShaderString;
        this.mProgramID = createProgram(str, cameraFragmentShaderString);
        String str2 = "vertexIn";
        this.ATTRIB_VERTEX = GLES20.glGetAttribLocation(this.mProgramID, str2);
        int i = this.ATTRIB_VERTEX;
        String str3 = TAG;
        if (i == -1) {
            Log.d(str3, "glGetAttribLocation error ");
        }
        this.ATTRIB_TEXTURE = GLES20.glGetAttribLocation(this.mProgramID, "textureIn");
        if (this.ATTRIB_TEXTURE == -1) {
            Log.d(str3, "glGetAttribLocation error ");
        }
        GLES20.glUseProgram(this.mProgramID);
        this.mcamera_fragshader_texture = GLES20.glGetUniformLocation(this.mProgramID, "tex_rgb");
        StringBuilder sb = new StringBuilder();
        sb.append("glGetAttribLocation mcamera_fragshader_texture: ");
        sb.append(this.mcamera_fragshader_texture);
        Log.d(str3, sb.toString());
        this.mmodelMatrix = GLES20.glGetUniformLocation(this.mProgramID, "modelViewProjectionMatrix");
        this.fbobackcamera = GLES20.glGetUniformLocation(this.mProgramID, "isFrontCamera");
        int i2 = 1;
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        this.mcamera_texture = iArr[0];
        GLES20.glBindTexture(36197, this.mcamera_texture);
        GLES20.glTexParameteri(36197, 10240, 9729);
        GLES20.glTexParameteri(36197, 10241, 9729);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        checkNoGLES2Error();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("glGetAttribLocation mcamera_texture: ");
        sb2.append(this.mcamera_texture);
        Log.d(str3, sb2.toString());
        this.mProgramID2 = createProgram(str, dispalyFragmentShaderString);
        this.ATTRIB_VERTEX2 = GLES20.glGetAttribLocation(this.mProgramID2, str2);
        if (this.ATTRIB_VERTEX2 < 0) {
            Log.d(str3, "programID_2 glGet vertex Location error ");
        }
        this.ATTRIB_TEXTURE2 = GLES20.glGetAttribLocation(this.mProgramID2, "textureIn");
        if (this.ATTRIB_TEXTURE2 < 0) {
            Log.d(str3, "programID_2 glGet texture bLocation error ");
        }
        GLES20.glUseProgram(this.mProgramID2);
        checkNoGLES2Error();
        this.mFragshaderRgb = GLES20.glGetUniformLocation(this.mProgramID2, "tex_rgb");
        StringBuilder sb3 = new StringBuilder();
        sb3.append("programID_2 param ATTRIB_VERTEX2: ");
        sb3.append(this.ATTRIB_VERTEX2);
        sb3.append(" ATTRIB_TEXTURE2:");
        sb3.append(this.ATTRIB_TEXTURE2);
        sb3.append(" textuer2d samp:");
        sb3.append(this.mFragshaderRgb);
        Log.d(str3, sb3.toString());
        this.mFboTexture = new int[1];
        GLES20.glGenFramebuffers(1, this.mFboTexture, 0);
        checkNoGLES2Error();
        this.mFbo = this.mFboTexture[0];
        GLES20.glBindFramebuffer(36160, this.mFbo);
        checkNoGLES2Error();
        GLES20.glGenTextures(1, this.mFboTexture, 0);
        checkNoGLES2Error();
        this.mFramebufferTexture = this.mFboTexture[0];
        GLES20.glBindTexture(3553, this.mFramebufferTexture);
        checkNoGLES2Error();
        GLES20.glTexImage2D(3553, 0, 6408, 3840, 2160, 0, 6408, 5121, null);
        checkNoGLES2Error();
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        checkNoGLES2Error();
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        checkNoGLES2Error();
        GLES20.glTexParameterf(3553, 10242, 33071.0f);
        checkNoGLES2Error();
        GLES20.glTexParameterf(3553, 10243, 33071.0f);
        checkNoGLES2Error();
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.mFramebufferTexture, 0);
        checkNoGLES2Error();
        GLES20.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
        checkNoGLES2Error();
        GLES20.glBindTexture(3553, 0);
        checkNoGLES2Error();
        GLES20.glBindFramebuffer(36160, 0);
        checkNoGLES2Error();
        StringBuilder sb4 = new StringBuilder();
        sb4.append("fbo id:");
        sb4.append(this.mFbo);
        sb4.append(" mFramebufferTexture:");
        sb4.append(this.mFramebufferTexture);
        Log.d(str3, sb4.toString());
        this.mpreviewFilterProgramID = createProgram(str, previewShaderString);
        GLES20.glUseProgram(this.mpreviewFilterProgramID);
        this.filter_rgb = GLES20.glGetUniformLocation(this.mpreviewFilterProgramID, "filter_rgb");
        this.extraVideoFilter = GLES20.glGetUniformLocation(this.mpreviewFilterProgramID, "extraVideoFilter");
        this.mmodelMatrixPreviewFilter = GLES20.glGetUniformLocation(this.mpreviewFilterProgramID, "modelViewProjectionMatrix");
        this.backcamera = GLES20.glGetUniformLocation(this.mpreviewFilterProgramID, "isFrontCamera");
        this.enable_film_picture_id = GLES20.glGetUniformLocation(this.mpreviewFilterProgramID, "enable_film_picture");
        StringBuilder sb5 = new StringBuilder();
        sb5.append("glGetAttribLocation filter rgb id: ");
        sb5.append(this.filter_rgb);
        sb5.append(" extraVideoFilter id:");
        sb5.append(this.extraVideoFilter);
        Log.d(str3, sb5.toString());
        GLES20.glGenTextures(1, this.mRgbTexture, 0);
        StringBuilder sb6 = new StringBuilder();
        sb6.append("generate texture rgb id: ");
        sb6.append(this.mRgbTexture[0]);
        Log.d(str3, sb6.toString());
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(3553, this.mRgbTexture[0]);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glUniform1i(this.filter_rgb, 1);
        GLES20.glTexImage2D(3553, 0, 6408, 512, 512, 0, 6408, 5121, this.RGBColor);
        int i3 = this.extraVideoFilter;
        if (this.RGBColor == null) {
            i2 = 0;
        }
        GLES20.glUniform1i(i3, i2);
        checkNoGLES2Error();
        this.rotate_angle_id = GLES20.glGetUniformLocation(this.mpreviewFilterProgramID, "rotate_angle");
        GLES20.glUniform1i(this.rotate_angle_id, 0);
        checkNoGLES2Error();
    }

    public void SetColorFilter(String str) {
        String sb;
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.outWidth = 512;
        options.outHeight = 512;
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
        String str2 = TAG;
        if (decodeFile == null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Construct SetColorFilter path error: ");
            sb2.append(str);
            sb = sb2.toString();
        } else {
            int[] iArr = new int[262144];
            for (int i = 0; i < 512; i++) {
                for (int i2 = 0; i2 < 512; i2++) {
                    int pixel = decodeFile.getPixel(i2, i);
                    iArr[(i * 512) + i2] = ((((((decodeFile.hasAlpha() ? Color.alpha(pixel) : 255) * 256) + Color.blue(pixel)) * 256) + Color.green(pixel)) * 256) + Color.red(pixel);
                }
            }
            this.RGBColor = ByteBuffer.allocateDirect(iArr.length * 32);
            this.RGBColor.order(ByteOrder.nativeOrder());
            this.RGBColor.asIntBuffer().put(iArr);
            this.RGBColor.position(0);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Construct SetColorFilter path :");
            sb3.append(str);
            sb3.append("RGBColor:");
            sb3.append(this.RGBColor);
            sb = sb3.toString();
        }
        Log.d(str2, sb);
    }

    public void SetFrontCamera(boolean z) {
    }

    public void SetRotateAngle(int i) {
        this.mrotate_angle = i;
        StringBuilder sb = new StringBuilder();
        sb.append("mrotate_angle");
        sb.append(this.mrotate_angle);
        Log.d(TAG, sb.toString());
    }

    public void bind(Rect rect, int i, int i2) {
        TransferExternalImagetoFbo();
    }

    public int createProgram(String str, String str2) {
        int loadShader = loadShader(35633, str);
        int loadShader2 = loadShader(35632, str2);
        StringBuilder sb = new StringBuilder();
        sb.append("vertex shader: ");
        sb.append(str);
        String str3 = " -- ";
        sb.append(str3);
        sb.append(loadShader);
        String sb2 = sb.toString();
        String str4 = TAG;
        Log.d(str4, sb2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("fragment shader: ");
        sb3.append(str2);
        sb3.append(str3);
        sb3.append(loadShader2);
        Log.d(str4, sb3.toString());
        int glCreateProgram = GLES20.glCreateProgram();
        abortUnless(glCreateProgram > 0, "Create OpenGL program failed.");
        StringBuilder sb4 = new StringBuilder();
        sb4.append("program: ");
        sb4.append(glCreateProgram);
        Log.d(str4, sb4.toString());
        if (glCreateProgram != 0) {
            GLES20.glAttachShader(glCreateProgram, loadShader);
            GLES20.glAttachShader(glCreateProgram, loadShader2);
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] != 1) {
                GLES20.glDeleteProgram(glCreateProgram);
                glCreateProgram = 0;
            }
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append(" end if program: ");
        sb5.append(glCreateProgram);
        Log.d(str4, sb5.toString());
        return glCreateProgram;
    }

    public void init(SurfaceTexture surfaceTexture) {
        Log.d(TAG, "init :");
        InitShaders();
        byte[] bArr = new byte[this.vertexVertices_buffer.remaining()];
        this.vertexVertices_buffer.get(bArr);
        this.vertexVertices_buffer.position(0);
        byte[] bArr2 = new byte[this.textureVertices_buffer.remaining()];
        this.textureVertices_buffer.get(bArr2);
        this.textureVertices_buffer.position(0);
        this.mOpenGlRender.SetOpengGlRenderParams(this.mProgramID2, this.mFramebufferTexture, this.mFragshaderRgb, this.ATTRIB_VERTEX2, this.ATTRIB_TEXTURE2, bArr, bArr2);
        this.mOpenGlRender.SetCurrentGLContext(this.mFramebufferTexture);
        this.mInputSurfaceTexture = surfaceTexture;
        this.mInputSurfaceTexture.attachToGLContext(this.mcamera_texture);
    }

    public void release() {
        GLES20.glDeleteTextures(1, this.camera_texture_id, 0);
        GLES20.glDeleteTextures(1, this.mRgbTexture, 0);
        GLES20.glDeleteTextures(1, this.mRgbTexture, 0);
        GLES20.glDeleteTextures(1, this.mFboTexture, 0);
        GLES20.glDeleteProgram(this.mProgramID);
        GLES20.glDeleteProgram(this.mProgramID2);
        GLES20.glDeleteProgram(this.mpreviewFilterProgramID);
    }

    public void updateTexImage() {
        this.mInputSurfaceTexture.updateTexImage();
        this.mInputSurfaceTexture.getTransformMatrix(this.mtransformMatrix);
        Matrix.setIdentityM(this.mtransformMatrix, 0);
        Matrix.rotateM(this.mtransformMatrix, 0, -90.0f, 0.0f, 0.0f, 1.0f);
    }
}
