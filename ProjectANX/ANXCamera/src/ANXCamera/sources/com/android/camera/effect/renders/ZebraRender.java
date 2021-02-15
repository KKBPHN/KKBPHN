package com.android.camera.effect.renders;

import android.graphics.Color;
import android.opengl.GLES20;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.gallery3d.ui.GLCanvas;

public class ZebraRender extends ConvolutionEffectRender {
    private static final String FRAG = "precision highp float; \nuniform sampler2D sTexture; \nvarying vec2 vTexCoord; \nuniform vec2 uSize; \nuniform vec2 uStep; \nuniform float uWidth; \nuniform float uGap; \nuniform float uOffset; \nuniform float uAlpha; \nuniform vec3 uOverColor; \nuniform vec3 uUnderColor; \nuniform float uOverExposure; \nuniform float uUnderExposure; \nfloat smoothstep_pulse(float e0, float e1, float e2, float e3, float x) {\n    return smoothstep(e0,e1,x) - smoothstep(e2,e3,x);\n}\nvoid main() { \n    vec3 color = texture2D(sTexture, vTexCoord).rgb; \n    vec3 factor = vec3(0.2125, 0.7154, 0.0721); \n    float Y = dot(color, factor); \n    vec2 coord = vTexCoord * uSize; // convert to world coordinate \n    float x = coord.x + uOffset; \n    float y = coord.y; \n    float diff; \n    if (y > x) { \n        diff = y - x; \n    } else { \n        diff = x - y + uWidth; \n    } \n    float smoothWidth = uWidth * 0.25; \n    float eachWidth = mod(diff, uGap + uWidth);\n    if (eachWidth < uWidth) { \n        if (Y >= uOverExposure) { \n            vec4 overlay = vec4(uOverColor, Y)*uAlpha; \n            vec3 mixed = mix(color,overlay.rgb,smoothstep_pulse(0.0,0.25,0.75,1.0,eachWidth/uWidth)); \n            gl_FragColor = vec4(mixed,1.0); \n        } else if (Y < uUnderExposure) { \n            vec4 overlay = vec4(uUnderColor, Y)*uAlpha; \n            vec3 mixed = mix(color,overlay.rgb,smoothstep_pulse(0.0,0.25,0.75,1.0,eachWidth/uWidth)); \n            gl_FragColor = vec4(mixed,1.0); \n        } else { \n            gl_FragColor = vec4(color, 1)*uAlpha; \n        } \n    } else { \n        gl_FragColor = vec4(color, 1)*uAlpha; \n    } \n}";
    protected float mGap = 10.0f;
    protected float mOffset = 2.0f;
    protected int mOverColor = 13458528;
    protected float mOverExposure = 0.99f;
    protected int mUnderColor = 7189236;
    protected float mUnderExposure = 0.01f;
    protected int mUniformGapH;
    protected int mUniformOffsetH;
    protected int mUniformOverColorH;
    protected int mUniformOverExposureH;
    protected int mUniformSizeH;
    protected int mUniformUnderColorH;
    protected int mUniformUnderExposureH;
    protected int mUniformWidthH;
    protected float mWidth = 8.0f;

    public ZebraRender(GLCanvas gLCanvas) {
        super(gLCanvas);
    }

    public ZebraRender(GLCanvas gLCanvas, int i) {
        super(gLCanvas, i);
    }

    public boolean draw(DrawAttribute drawAttribute) {
        this.mOffset += 1.0f;
        return super.draw(drawAttribute);
    }

    public String getFragShaderString() {
        return FRAG;
    }

    /* access modifiers changed from: protected */
    public void initShader() {
        super.initShader();
        this.mUniformSizeH = GLES20.glGetUniformLocation(this.mProgram, "uSize");
        this.mUniformOverExposureH = GLES20.glGetUniformLocation(this.mProgram, "uOverExposure");
        this.mUniformUnderExposureH = GLES20.glGetUniformLocation(this.mProgram, "uUnderExposure");
        this.mUniformOverColorH = GLES20.glGetUniformLocation(this.mProgram, "uOverColor");
        this.mUniformUnderColorH = GLES20.glGetUniformLocation(this.mProgram, "uUnderColor");
        this.mUniformWidthH = GLES20.glGetUniformLocation(this.mProgram, "uWidth");
        this.mUniformGapH = GLES20.glGetUniformLocation(this.mProgram, "uGap");
        this.mUniformOffsetH = GLES20.glGetUniformLocation(this.mProgram, "uOffset");
    }

    /* access modifiers changed from: protected */
    public void initShaderValue(boolean z) {
        super.initShaderValue(z);
        GLES20.glUniform2f(this.mUniformSizeH, (float) this.mPreviewWidth, (float) this.mPreviewHeight);
        GLES20.glUniform1f(this.mUniformOverExposureH, this.mOverExposure);
        GLES20.glUniform1f(this.mUniformUnderExposureH, this.mUnderExposure);
        GLES20.glUniform3f(this.mUniformOverColorH, ((float) Color.red(this.mOverColor)) / 255.0f, ((float) Color.green(this.mOverColor)) / 255.0f, ((float) Color.blue(this.mOverColor)) / 255.0f);
        GLES20.glUniform3f(this.mUniformUnderColorH, ((float) Color.red(this.mUnderColor)) / 255.0f, ((float) Color.green(this.mUnderColor)) / 255.0f, ((float) Color.blue(this.mUnderColor)) / 255.0f);
        GLES20.glUniform1f(this.mUniformWidthH, this.mWidth);
        GLES20.glUniform1f(this.mUniformGapH, this.mGap);
        GLES20.glUniform1f(this.mUniformOffsetH, this.mOffset);
    }

    public void setExposureThreshold(float f, float f2) {
        this.mOverExposure = f;
        this.mUnderExposure = f2;
    }

    public void setPreviewSize(int i, int i2) {
        super.setPreviewSize(i, i2);
    }
}
