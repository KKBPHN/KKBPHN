package com.miui.extravideo.watermark.gles;

import android.opengl.GLES20;
import java.util.HashMap;

public class ShaderProgram {
    private static int INVALID_VALUE = -1;
    private HashMap attributes = new HashMap();
    private int programHandle = INVALID_VALUE;

    private int loadShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        OpenGlUtils.checkGlError("glCreateShader");
        GLES20.glShaderSource(glCreateShader, str);
        OpenGlUtils.checkGlError("glShaderSource");
        GLES20.glCompileShader(glCreateShader);
        OpenGlUtils.checkGlError("glCompileShader");
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        String glGetShaderInfoLog = GLES20.glGetShaderInfoLog(glCreateShader);
        GLES20.glDeleteShader(glCreateShader);
        StringBuilder sb = new StringBuilder();
        sb.append("Shader compilation failed with: ");
        sb.append(glGetShaderInfoLog);
        throw new IllegalArgumentException(sb.toString());
    }

    public void create(String str, String str2) {
        int i = INVALID_VALUE;
        if (str != null) {
            i = loadShader(35633, str);
        }
        if (i == 0) {
            this.programHandle = 0;
            return;
        }
        int i2 = INVALID_VALUE;
        if (str2 != null) {
            i2 = loadShader(35632, str2);
        }
        if (i2 == 0) {
            this.programHandle = 0;
            return;
        }
        this.programHandle = GLES20.glCreateProgram();
        OpenGlUtils.checkGlError("glCreateProgram");
        GLES20.glAttachShader(this.programHandle, i);
        String str3 = "glAttachShader";
        OpenGlUtils.checkGlError(str3);
        GLES20.glAttachShader(this.programHandle, i2);
        OpenGlUtils.checkGlError(str3);
        GLES20.glLinkProgram(this.programHandle);
    }

    public int getAttributeLocation(String str) {
        if (this.attributes.containsKey(str)) {
            return ((Integer) this.attributes.get(str)).intValue();
        }
        int glGetAttribLocation = GLES20.glGetAttribLocation(this.programHandle, str);
        StringBuilder sb = new StringBuilder();
        sb.append("glGetAttribLocation ");
        sb.append(str);
        OpenGlUtils.checkGlError(sb.toString());
        if (glGetAttribLocation == -1) {
            glGetAttribLocation = GLES20.glGetUniformLocation(this.programHandle, str);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("glGetUniformLocation ");
            sb2.append(str);
            OpenGlUtils.checkGlError(sb2.toString());
        }
        if (glGetAttribLocation != -1) {
            this.attributes.put(str, Integer.valueOf(glGetAttribLocation));
            return glGetAttribLocation;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Can't find a location for attribute ");
        sb3.append(str);
        throw new IllegalStateException(sb3.toString());
    }

    public int getProgramHandle() {
        return this.programHandle;
    }

    public void unUse() {
        GLES20.glUseProgram(0);
    }

    public void use() {
        GLES20.glUseProgram(this.programHandle);
    }
}
