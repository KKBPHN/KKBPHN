package com.xiaomi.mediacodec;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.opengl.GLES30;
import android.os.Build;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GlUtil {
    public static float[] location = null;
    public static String locationString = null;
    public static int mHeight = 4320;
    public static int mPictureRotation = 0;
    public static int mWidht = 7680;

    public static void checkGlError(String str) {
        int glGetError = GLES30.glGetError();
        if (glGetError != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" :0x");
            sb.append(glGetError);
            throw new RuntimeException(sb.toString());
        }
    }

    public static void checkLocation(int i, String str) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to locate '");
            sb.append(str);
            sb.append("' in program");
            throw new RuntimeException(sb.toString());
        }
    }

    public static int createProgram(String str, String str2) {
        int glCreateProgram = GLES30.glCreateProgram();
        checkGlError("glCreateProgram fail");
        int loadShader = loadShader(35633, str);
        int loadShader2 = loadShader(35632, str2);
        GLES30.glAttachShader(glCreateProgram, loadShader);
        checkGlError("glAttachVertexShader fail");
        GLES30.glAttachShader(glCreateProgram, loadShader2);
        checkGlError("glAttachFragmentShader fail");
        GLES30.glLinkProgram(glCreateProgram);
        int[] iArr = new int[1];
        GLES30.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] == 1) {
            GLES30.glDeleteShader(loadShader);
            GLES30.glDeleteShader(loadShader2);
            return glCreateProgram;
        }
        GLES30.glDeleteProgram(glCreateProgram);
        throw new RuntimeException("Could not link program");
    }

    public static int genTextureId(int i) {
        int[] iArr = new int[1];
        GLES30.glGenTextures(1, iArr, 0);
        checkGlError("glGenTextures");
        int i2 = iArr[0];
        GLES30.glBindTexture(i, i2);
        checkGlError("glBindTexture");
        StringBuilder sb = new StringBuilder();
        sb.append("android.os.Build.MODEL ");
        sb.append(Build.MODEL);
        Logg.LogI(sb.toString());
        if (Build.MODEL.toLowerCase().equals("redmi note 5a")) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("in ");
            sb2.append(Build.MODEL);
            sb2.append(" we use gl_linear");
            Logg.LogI(sb2.toString());
            GLES30.glTexParameterf(i, 10241, 9729.0f);
        } else {
            GLES30.glTexParameterf(i, 10241, 9987.0f);
        }
        GLES30.glTexParameterf(i, 10240, 9729.0f);
        GLES30.glTexParameteri(i, 10242, 33071);
        GLES30.glTexParameteri(i, 10243, 33071);
        checkGlError("glTexParameter");
        GLES30.glBindTexture(i, 0);
        return i2;
    }

    public static int loadShader(int i, String str) {
        int glCreateShader = GLES30.glCreateShader(i);
        StringBuilder sb = new StringBuilder();
        sb.append("glCreateShader fail, type = ");
        sb.append(i);
        checkGlError(sb.toString());
        GLES30.glShaderSource(glCreateShader, str);
        GLES30.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES30.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        GLES30.glDeleteShader(glCreateShader);
        throw new RuntimeException("glCompileShader fail");
    }

    public static File saveFile(Bitmap bitmap, String str, String str2) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(str, str2);
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            bitmap.compress(CompressFormat.JPEG, 80, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return file2;
    }
}
