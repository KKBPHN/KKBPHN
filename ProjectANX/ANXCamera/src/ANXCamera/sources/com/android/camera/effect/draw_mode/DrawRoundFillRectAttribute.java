package com.android.camera.effect.draw_mode;

import com.android.camera.effect.renders.ShaderRender;
import com.android.camera.effect.renders.VertexHelper;
import java.nio.FloatBuffer;

public class DrawRoundFillRectAttribute extends DrawRectShapeAttributeBase {
    public int mColor;
    public FloatBuffer mVertexBuffer;

    public DrawRoundFillRectAttribute(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mX = i;
        this.mY = i2;
        this.mWidth = i3;
        this.mHeight = i4;
        this.mColor = i5;
        float[] genRoundRectVex = VertexHelper.genRoundRectVex(i3, i4, i6);
        this.mVertexBuffer = ShaderRender.allocateByteBuffer((genRoundRectVex.length * 32) / 8).asFloatBuffer();
        this.mVertexBuffer.put(genRoundRectVex);
        this.mVertexBuffer.position(0);
    }
}
