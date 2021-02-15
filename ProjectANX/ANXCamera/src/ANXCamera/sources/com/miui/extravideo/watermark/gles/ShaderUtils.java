package com.miui.extravideo.watermark.gles;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderUtils {
    private static final String TAG = "ShaderUtils";

    public static void checkGlError(String str) {
        int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            String str2 = ": glError ";
            sb.append(str2);
            sb.append(glGetError);
            Log.e(TAG, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(str2);
            sb2.append(glGetError);
            throw new RuntimeException(sb2.toString());
        }
    }

    public static int createProgram(String str, String str2) {
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
        if (glCreateProgram != 0) {
            GLES20.glAttachShader(glCreateProgram, loadShader);
            String str3 = "glAttachShader";
            checkGlError(str3);
            GLES20.glAttachShader(glCreateProgram, loadShader2);
            checkGlError(str3);
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] != 1) {
                String str4 = TAG;
                Log.e(str4, "Could not link program: ");
                Log.e(str4, GLES20.glGetProgramInfoLog(glCreateProgram));
                GLES20.glDeleteProgram(glCreateProgram);
                return i;
            }
        }
        i = glCreateProgram;
        return i;
    }

    public static int loadShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader == 0) {
            return glCreateShader;
        }
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Could not compile shader ");
        sb.append(i);
        sb.append(":");
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.e(str2, sb2);
        Log.e(str2, GLES20.glGetShaderInfoLog(glCreateShader));
        GLES20.glDeleteShader(glCreateShader);
        return 0;
    }
}
