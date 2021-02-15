package com.miui.extravideo.watermark.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.miui.extravideo.watermark.gles.ShaderProgram;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class WatermarkTextureRenderExt {
    private static final String FRAGMENT_SHADER = "precision highp float;\nuniform sampler2D sTexture;\nuniform vec2 uResolution;\nvarying vec2 vTextureCoord;\nvec4 blur(vec4 color) {\n    vec2 step = uResolution;\n    vec4 sum = vec4(0.0, 0.0, 0.0,0.0);\n    sum += color * 3.0;\n    sum += texture2D(sTexture, vTextureCoord + vec2( step.x,-step.y)) ;\n    sum += texture2D(sTexture, vTextureCoord + vec2( step.x, step.y)) ;\n    sum += texture2D(sTexture, vTextureCoord + vec2( -step.x, -step.y));\n    sum += texture2D(sTexture, vTextureCoord + vec2( -step.x,  step.y));\n    sum += texture2D(sTexture, vTextureCoord + vec2(0, -step.y));\n    sum += texture2D(sTexture, vTextureCoord + vec2(0,  step.y));\n    sum += texture2D(sTexture, vTextureCoord + vec2( step.x, 0));\n    sum += texture2D(sTexture, vTextureCoord + vec2( -step.x,  0));\n    return sum / 11.0;\n}void main() {\n   vec4 inputColor = texture2D(sTexture, vTextureCoord);\n   vec4 ret = blur(inputColor);\n   gl_FragColor = ret * 1.2;}";
    private static final String VERTEXT_SHADER = "attribute vec2 aPosition;\nuniform mat4 uOrientationM;\nattribute vec2 aTexCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    vTextureCoord =  aTexCoord;\n    gl_Position = uOrientationM * vec4(aPosition, 0.0, 1.0);\n}";
    private final byte[] TEX_COORDINATES = {0, 1, 0, 0, 1, 1, 1, 0};
    private ByteBuffer fullQuadTexcoord;
    private FloatBuffer fullQuadVertices;
    private int mHeight;
    private final float[] mOrientationMatrix = new float[16];
    private int mWidth;
    private ShaderProgram shader;

    public WatermarkTextureRenderExt(int i, int i2, int[] iArr) {
        if (this.shader != null) {
            this.shader = null;
        }
        this.shader = new ShaderProgram();
        this.shader.create(VERTEXT_SHADER, FRAGMENT_SHADER);
        this.fullQuadVertices = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        float f = (float) i;
        float f2 = ((float) iArr[2]) / f;
        float f3 = (float) i2;
        float f4 = ((float) iArr[3]) / f3;
        float f5 = ((float) iArr[0]) / f;
        float f6 = ((float) ((i2 - iArr[3]) - iArr[1])) / f3;
        this.mWidth = iArr[2];
        this.mHeight = iArr[3];
        float f7 = (f5 * 2.0f) - 1.0f;
        float f8 = (f6 * 2.0f) - 1.0f;
        float f9 = (f4 * 2.0f) + f8;
        float f10 = (f2 * 2.0f) + f7;
        this.fullQuadVertices.put(new float[]{f7, f9, f7, f8, f10, f9, f10, f8}).position(0);
        this.fullQuadTexcoord = ByteBuffer.allocateDirect(8);
        this.fullQuadTexcoord.put(this.TEX_COORDINATES).position(0);
    }

    private void renderQuad(int i, int i2) {
        GLES20.glUniform2f(this.shader.getAttributeLocation("uResolution"), 0.5f / ((float) this.mWidth), 0.5f / ((float) this.mHeight));
        GLES20.glVertexAttribPointer(i, 2, 5126, false, 0, this.fullQuadVertices);
        GLES20.glEnableVertexAttribArray(i);
        GLES20.glVertexAttribPointer(i2, 2, 5120, false, 0, this.fullQuadTexcoord);
        GLES20.glEnableVertexAttribArray(i2);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisableVertexAttribArray(i);
        GLES20.glDisableVertexAttribArray(i2);
        GLES20.glDisable(3042);
    }

    public void draw(int i, int i2) {
        this.shader.use();
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        int attributeLocation = this.shader.getAttributeLocation("uOrientationM");
        Matrix.setRotateM(this.mOrientationMatrix, 0, (float) (-i2), 0.0f, 0.0f, 1.0f);
        GLES20.glUniformMatrix4fv(attributeLocation, 1, false, this.mOrientationMatrix, 0);
        renderQuad(this.shader.getAttributeLocation("aPosition"), this.shader.getAttributeLocation("aTexCoord"));
        this.shader.unUse();
    }

    public void release() {
        this.shader = null;
        this.fullQuadVertices = null;
    }
}
