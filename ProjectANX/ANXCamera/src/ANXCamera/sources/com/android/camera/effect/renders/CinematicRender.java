package com.android.camera.effect.renders;

import com.android.gallery3d.ui.GLCanvas;

public class CinematicRender extends PixelEffectRender {
    private static final String FRAG = "precision mediump float; \nuniform float uAlpha; \nuniform sampler2D sTexture; \nvarying vec2 vTexCoord; \nvoid main() { \n    gl_FragColor = texture2D(sTexture, vTexCoord)*uAlpha; \n}";

    public CinematicRender(GLCanvas gLCanvas, int i) {
        super(gLCanvas, i);
    }

    public String getFragShaderString() {
        return FRAG;
    }

    /* access modifiers changed from: protected */
    public void initVertexData() {
        float[] fArr = {0.128f, 1.0f, 0.872f, 1.0f, 0.128f, 0.0f, 0.872f, 0.0f};
        float[] fArr2 = {0.128f, 1.0f, 0.872f, 1.0f, 0.128f, 0.0f, 0.872f, 0.0f};
        this.mVertexBuffer = ShaderRender.allocateByteBuffer((fArr.length * 32) / 8).asFloatBuffer();
        this.mVertexBuffer.put(fArr);
        this.mVertexBuffer.position(0);
        this.mTexCoorBuffer = ShaderRender.allocateByteBuffer((fArr2.length * 32) / 8).asFloatBuffer();
        this.mTexCoorBuffer.put(fArr2);
        this.mTexCoorBuffer.position(0);
    }
}
