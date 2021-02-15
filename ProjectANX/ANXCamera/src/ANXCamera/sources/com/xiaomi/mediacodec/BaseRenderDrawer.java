package com.xiaomi.mediacodec;

import android.opengl.GLES30;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class BaseRenderDrawer {
    protected final int CoordsPerTextureCount = 2;
    protected final int CoordsPerVertexCount = 2;
    protected final int TextureStride = 8;
    protected final int VertexCount = (this.vertexData.length / 2);
    protected final int VertexStride = 8;
    protected float[] backTextureData = {0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f};
    protected float[] displayTextureData = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    protected float[] frameBufferData = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
    protected float[] frontTextureData = {1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
    protected int height;
    private FloatBuffer mBackTextureBuffer;
    protected int mBackTextureBufferId;
    private FloatBuffer mDisplayTextureBuffer;
    protected int mDisplayTextureBufferId;
    private FloatBuffer mFrameTextureBuffer;
    protected int mFrameTextureBufferId;
    private FloatBuffer mFrontTextureBuffer;
    protected int mFrontTextureBufferId;
    protected int mProgram;
    private FloatBuffer mVertexBuffer;
    protected int mVertexBufferId;
    protected float[] vertexData = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    protected int width;

    /* access modifiers changed from: protected */
    public void clear() {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        GLES30.glClear(16640);
    }

    public void create() {
        this.mProgram = GlesUtil.createProgram(getVertexSource(), getFragmentSource());
        initVertexBufferObjects();
        onCreated();
    }

    public void cropSize(int i, int i2, int i3, int i4, int i5, int i6) {
        onCroped(i, i2, i3, i4, i5, i6);
    }

    public void destroy() {
        releaseVertexBufferObjects();
        int i = this.mProgram;
        if (i != 0) {
            GlesUtil.DestoryProgram(i);
        }
    }

    public void draw(long j, float[] fArr) {
        clear();
        useProgram();
        viewPort(0, 0, this.width, this.height);
        onDraw();
    }

    public abstract String getFragmentSource();

    public abstract int getOutputTextureId();

    public abstract String getVertexSource();

    /* access modifiers changed from: protected */
    public void initVertexBufferObjects() {
        int[] iArr = new int[5];
        GLES30.glGenBuffers(5, iArr, 0);
        this.mVertexBuffer = ByteBuffer.allocateDirect(this.vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.vertexData);
        this.mVertexBuffer.position(0);
        this.mVertexBufferId = iArr[0];
        GLES30.glBindBuffer(34962, this.mVertexBufferId);
        GLES30.glBufferData(34962, this.vertexData.length * 4, this.mVertexBuffer, 35044);
        this.mBackTextureBuffer = ByteBuffer.allocateDirect(this.backTextureData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.backTextureData);
        this.mBackTextureBuffer.position(0);
        this.mBackTextureBufferId = iArr[1];
        GLES30.glBindBuffer(34962, this.mBackTextureBufferId);
        GLES30.glBufferData(34962, this.backTextureData.length * 4, this.mBackTextureBuffer, 35044);
        this.mFrontTextureBuffer = ByteBuffer.allocateDirect(this.frontTextureData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.frontTextureData);
        this.mFrontTextureBuffer.position(0);
        this.mFrontTextureBufferId = iArr[2];
        GLES30.glBindBuffer(34962, this.mFrontTextureBufferId);
        GLES30.glBufferData(34962, this.frontTextureData.length * 4, this.mFrontTextureBuffer, 35044);
        this.mDisplayTextureBuffer = ByteBuffer.allocateDirect(this.displayTextureData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.displayTextureData);
        this.mDisplayTextureBuffer.position(0);
        this.mDisplayTextureBufferId = iArr[3];
        GLES30.glBindBuffer(34962, this.mDisplayTextureBufferId);
        GLES30.glBufferData(34962, this.displayTextureData.length * 4, this.mDisplayTextureBuffer, 35044);
        this.mFrameTextureBuffer = ByteBuffer.allocateDirect(this.frameBufferData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.frameBufferData);
        this.mFrameTextureBuffer.position(0);
        this.mFrameTextureBufferId = iArr[4];
        GLES30.glBindBuffer(34962, this.mFrameTextureBufferId);
        GLES30.glBufferData(34962, this.frameBufferData.length * 4, this.mFrameTextureBuffer, 35044);
        GLES30.glBindBuffer(34962, 0);
    }

    public abstract void onChanged(int i, int i2);

    public abstract void onCreated();

    public abstract void onCroped(int i, int i2, int i3, int i4, int i5, int i6);

    public abstract void onDraw();

    public abstract void release();

    /* access modifiers changed from: protected */
    public void releaseVertexBufferObjects() {
        GLES30.glDeleteBuffers(1, new int[]{this.mVertexBufferId}, 0);
        GLES30.glDeleteBuffers(1, new int[]{this.mBackTextureBufferId}, 0);
        GLES30.glDeleteBuffers(1, new int[]{this.mFrontTextureBufferId}, 0);
        GLES30.glDeleteBuffers(1, new int[]{this.mDisplayTextureBufferId}, 0);
        GLES30.glDeleteBuffers(1, new int[]{this.mFrameTextureBufferId}, 0);
        this.mVertexBuffer = null;
        this.mBackTextureBuffer = null;
        this.mFrontTextureBuffer = null;
        this.mFrameTextureBuffer = null;
    }

    public abstract void setInputTextureId(int i);

    public abstract void setReserverResolution(boolean z);

    public void surfaceChangedSize(int i, int i2) {
        this.width = i;
        this.height = i2;
        onChanged(i, i2);
    }

    /* access modifiers changed from: protected */
    public void useProgram() {
        GLES30.glUseProgram(this.mProgram);
    }

    /* access modifiers changed from: protected */
    public void viewPort(int i, int i2, int i3, int i4) {
        GLES30.glViewport(i, i2, i3, i4);
    }
}
