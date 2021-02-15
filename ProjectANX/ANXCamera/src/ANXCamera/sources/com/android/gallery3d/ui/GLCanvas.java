package com.android.gallery3d.ui;

import android.graphics.SurfaceTexture;
import com.android.camera.effect.FrameBuffer;
import com.android.camera.effect.GLCanvasState;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.renders.RenderGroup;

public interface GLCanvas {
    void beginBindFrameBuffer(int i, int i2, int i3);

    void beginBindFrameBuffer(FrameBuffer frameBuffer);

    void clearBuffer();

    void deleteBuffer(int i);

    void deleteFrameBuffer(int i);

    void deleteProgram();

    void deleteProgram(int i);

    void deleteSurfaceTexture(SurfaceTexture surfaceTexture);

    boolean deleteTexture(int i);

    boolean deleteTexture(BasicTexture basicTexture);

    void draw(DrawAttribute drawAttribute);

    void endBindFrameBuffer();

    RenderGroup getEffectRenderGroup();

    int getHeight();

    GLCanvasState getState();

    int getWidth();

    void prepareBlurRenders();

    void prepareEffectRenders(boolean z, int i);

    void prepareYuvRenders();

    void recycledResources();

    void setPreviewSize(int i, int i2);

    void setSize(int i, int i2);
}
