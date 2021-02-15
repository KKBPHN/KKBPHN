package com.xiaomi.mediacodec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;

public class GlesUtil {
    private static String TAG = "GlesUtil";

    public static void DestoryProgram(int i) {
        GLES30.glDeleteProgram(i);
    }

    public static void bindFrameBuffer(int i, int i2) {
        GLES30.glBindFramebuffer(36160, i);
        GLES30.glFramebufferTexture2D(36160, 36064, 3553, i2, 0);
    }

    public static void bindFrameRender(int i, int i2, int i3, int i4) {
        GLES30.glBindFramebuffer(36160, i);
        GLES30.glBindRenderbuffer(36161, i2);
        GLES30.glRenderbufferStorage(36161, 33189, i3, i4);
        GLES30.glFramebufferRenderbuffer(36160, 36096, 36161, i2);
        GLES30.glBindRenderbuffer(36161, 0);
        GLES30.glBindFramebuffer(36160, 0);
    }

    public static void bindFrameTexture(int i, int i2) {
        GLES30.glBindFramebuffer(36160, i);
        GLES30.glBindTexture(3553, i2);
        GLES30.glFramebufferTexture2D(36160, 36064, 3553, i2, 0);
        GLES30.glBindTexture(3553, 0);
        GLES30.glBindFramebuffer(36160, 0);
        checkError();
    }

    public static void checkError() {
        if (GLES30.glGetError() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("elg error: ");
            sb.append(GLES30.glGetError());
            Log.e("checkError", sb.toString());
        }
    }

    public static void checkFrameBufferError() {
        int glCheckFramebufferStatus = GLES30.glCheckFramebufferStatus(36160);
        if (glCheckFramebufferStatus != 36053) {
            StringBuilder sb = new StringBuilder();
            sb.append("checkFrameBuffer error: ");
            sb.append(glCheckFramebufferStatus);
            Logg.LogE(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("status:");
            sb2.append(glCheckFramebufferStatus);
            sb2.append(", hex:");
            sb2.append(Integer.toHexString(glCheckFramebufferStatus));
            throw new RuntimeException(sb2.toString());
        }
    }

    public static int createCameraTexture(boolean z) {
        float f;
        int[] iArr = new int[1];
        GLES30.glGenTextures(1, iArr, 0);
        GLES30.glBindTexture(36197, iArr[0]);
        StringBuilder sb = new StringBuilder();
        sb.append("android.os.Build.MODEL ");
        sb.append(Build.MODEL);
        Logg.LogI(sb.toString());
        if (z) {
            f = 9728.0f;
        } else if (Build.MODEL.toLowerCase().equals("redmi note 5a")) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("in ");
            sb2.append(Build.MODEL);
            sb2.append(" we use gl_linear");
            Logg.LogI(sb2.toString());
            GLES30.glTexParameterf(36197, 10241, 9729.0f);
            GLES30.glTexParameterf(36197, 10240, 9729.0f);
            GLES30.glTexParameteri(36197, 10242, 33071);
            GLES30.glTexParameteri(36197, 10243, 33071);
            GLES30.glBindTexture(36197, 0);
            return iArr[0];
        } else {
            f = 9987.0f;
        }
        GLES30.glTexParameterf(36197, 10241, f);
        GLES30.glTexParameterf(36197, 10240, 9729.0f);
        GLES30.glTexParameteri(36197, 10242, 33071);
        GLES30.glTexParameteri(36197, 10243, 33071);
        GLES30.glBindTexture(36197, 0);
        return iArr[0];
    }

    public static int createFrameBuffer() {
        int[] iArr = new int[1];
        GLES30.glGenFramebuffers(1, iArr, 0);
        checkError();
        return iArr[0];
    }

    public static int createFrameTexture(int i, int i2, boolean z) {
        String str;
        int i3;
        if (i <= 0 || i2 <= 0) {
            str = "createOutputTexture: width or height is 0";
        } else {
            int[] iArr = new int[1];
            GLES30.glGenTextures(1, iArr, 0);
            if (iArr[0] == 0) {
                str = "createFrameTexture: glGenTextures is 0";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("android.os.Build.MODEL ");
                sb.append(Build.MODEL);
                Logg.LogI(sb.toString());
                GLES30.glBindTexture(3553, iArr[0]);
                GLES30.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, 5121, null);
                GLES30.glTexParameteri(3553, 10242, 33071);
                GLES30.glTexParameteri(3553, 10243, 33071);
                if (z) {
                    i3 = 9728;
                } else if (Build.MODEL.toLowerCase().equals("redmi note 5a")) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("in ");
                    sb2.append(Build.MODEL);
                    sb2.append(" we use gl_linear");
                    Logg.LogI(sb2.toString());
                    GLES30.glTexParameteri(3553, 10241, 9729);
                    GLES30.glTexParameteri(3553, 10240, 9729);
                    checkError();
                    return iArr[0];
                } else {
                    i3 = 9987;
                }
                GLES30.glTexParameteri(3553, 10241, i3);
                GLES30.glTexParameteri(3553, 10240, 9729);
                checkError();
                return iArr[0];
            }
        }
        Logg.LogE(str);
        return -1;
    }

    public static int createPixelsBuffer(int i, int i2) {
        int[] iArr = new int[1];
        GLES30.glGenBuffers(1, iArr, 0);
        checkError();
        return iArr[0];
    }

    public static void createPixelsBuffers(int[] iArr, int i, int i2) {
        GLES30.glGenBuffers(iArr.length, iArr, 0);
        for (int glBindBuffer : iArr) {
            GLES30.glBindBuffer(35051, glBindBuffer);
            GLES30.glBufferData(35051, i * i2 * 4, null, 35049);
        }
        GLES30.glBindBuffer(35051, 0);
    }

    public static int createProgram(String str, String str2) {
        int loadShader = loadShader(35633, str);
        int loadShader2 = loadShader(35632, str2);
        int glCreateProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(glCreateProgram, loadShader);
        GLES30.glAttachShader(glCreateProgram, loadShader2);
        GLES30.glLinkProgram(glCreateProgram);
        int[] iArr = new int[1];
        GLES30.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] != 1) {
            Logg.LogE("createProgam: link error");
            StringBuilder sb = new StringBuilder();
            sb.append("createProgam: ");
            sb.append(GLES30.glGetProgramInfoLog(glCreateProgram));
            Logg.LogE(sb.toString());
            GLES30.glDeleteProgram(glCreateProgram);
            return 0;
        }
        GLES30.glDeleteShader(loadShader);
        GLES30.glDeleteShader(loadShader2);
        return glCreateProgram;
    }

    public static int createRenderBuffer() {
        int[] iArr = new int[1];
        GLES30.glGenRenderbuffers(1, iArr, 0);
        checkError();
        return iArr[0];
    }

    public static void deleteFrameBuffer(int i, int i2) {
        GLES30.glDeleteFramebuffers(1, new int[]{i}, 0);
        GLES30.glDeleteTextures(1, new int[]{i2}, 0);
    }

    public static int loadBitmapTexture(Context context, int i) {
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i);
        if (decodeResource == null) {
            Logg.LogE("loadBitmapTexture:bitmap is null");
            return -1;
        }
        int loadBitmapTexture = loadBitmapTexture(decodeResource);
        decodeResource.recycle();
        return loadBitmapTexture;
    }

    public static int loadBitmapTexture(Bitmap bitmap) {
        int[] iArr = new int[1];
        GLES30.glGenTextures(1, iArr, 0);
        if (iArr[0] == 0) {
            Logg.LogE("loadBitmapTexture: glGenTextures is 0");
            return -1;
        }
        GLES30.glBindTexture(3553, iArr[0]);
        if (Build.MODEL.toLowerCase().equals("redmi note 5a")) {
            StringBuilder sb = new StringBuilder();
            sb.append("in ");
            sb.append(Build.MODEL);
            sb.append(" we use gl_linear");
            Logg.LogI(sb.toString());
            GLES30.glTexParameterf(3553, 10241, 9729.0f);
        } else {
            GLES30.glTexParameterf(3553, 10241, 9987.0f);
        }
        GLES30.glTexParameterf(3553, 10240, 9729.0f);
        GLES30.glTexParameterf(3553, 10242, 33071.0f);
        GLES30.glTexParameterf(3553, 10243, 33071.0f);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        GLES30.glGenerateMipmap(3553);
        GLES30.glBindTexture(3553, 0);
        return iArr[0];
    }

    public static int loadShader(int i, String str) {
        int glCreateShader = GLES30.glCreateShader(i);
        GLES30.glShaderSource(glCreateShader, str);
        GLES30.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES30.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        Logg.LogE("loadShader: compiler error");
        StringBuilder sb = new StringBuilder();
        sb.append("loadShader: ");
        sb.append(GLES30.glGetShaderInfoLog(glCreateShader));
        Logg.LogE(sb.toString());
        GLES30.glDeleteShader(glCreateShader);
        return 0;
    }

    public static void unBindFrameBuffer() {
        GLES30.glBindFramebuffer(36160, 0);
    }
}
