package com.android.camera.effect;

import com.android.camera.effect.framework.gles.OpenGlUtils;
import com.android.gallery3d.ui.GLCanvas;

public class DoubleBuffer {
    private FrameBuffer mFrameBufferIn;
    private FrameBuffer mFrameBufferOut;

    public DoubleBuffer(int i, int i2) {
        this.mFrameBufferIn = new FrameBuffer(null, i, i2, 0);
        this.mFrameBufferOut = new FrameBuffer(null, i, i2, 0);
    }

    public DoubleBuffer(GLCanvas gLCanvas, int i, int i2, int i3) {
        this.mFrameBufferIn = new FrameBuffer(gLCanvas, i, i2, i3);
        this.mFrameBufferOut = new FrameBuffer(gLCanvas, i, i2, i3);
    }

    public int getHeight() {
        return this.mFrameBufferIn.getHeight();
    }

    public FrameBuffer getInputBuffer() {
        return this.mFrameBufferIn;
    }

    public FrameBuffer getOutputBuffer() {
        return this.mFrameBufferOut;
    }

    public int getWidth() {
        return this.mFrameBufferIn.getWidth();
    }

    public void release() {
        FrameBuffer frameBuffer = this.mFrameBufferIn;
        if (frameBuffer != null) {
            frameBuffer.delete();
            OpenGlUtils.safeDeleteTexture(this.mFrameBufferIn.getTexture().getId());
            this.mFrameBufferIn = null;
        }
        FrameBuffer frameBuffer2 = this.mFrameBufferOut;
        if (frameBuffer2 != null) {
            frameBuffer2.delete();
            OpenGlUtils.safeDeleteTexture(this.mFrameBufferOut.getTexture().getId());
            this.mFrameBufferOut = null;
        }
    }

    public void swapBuffer() {
        FrameBuffer frameBuffer = this.mFrameBufferIn;
        this.mFrameBufferIn = this.mFrameBufferOut;
        this.mFrameBufferOut = frameBuffer;
    }
}
