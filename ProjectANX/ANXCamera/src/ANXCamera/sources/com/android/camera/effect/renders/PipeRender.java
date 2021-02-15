package com.android.camera.effect.renders;

import com.android.camera.effect.DoubleBuffer;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.draw_mode.DrawBasicTexAttribute;
import com.android.camera.effect.draw_mode.DrawIntTexAttribute;
import com.android.camera.effect.draw_mode.DrawYuvAttribute;
import com.android.camera.effect.framework.gles.OpenGlUtils;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.GLCanvas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class PipeRender extends RenderGroup {
    private static final boolean DUMP_TEXTURE = false;
    private static final String TAG = "PipeRender";
    private int mBufferHeight;
    private HashMap mBufferMap = new HashMap(1);
    private int mBufferWidth;
    private DoubleBuffer mDoubleBuffer;

    public PipeRender(GLCanvas gLCanvas) {
        super(gLCanvas);
    }

    public PipeRender(GLCanvas gLCanvas, int i) {
        super(gLCanvas, i);
    }

    private synchronized void destroyFrameBuffers() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("destroyFrameBuffers: count=");
        sb.append(this.mBufferMap.size());
        Log.v(str, sb.toString());
        for (DoubleBuffer release : this.mBufferMap.values()) {
            release.release();
        }
        this.mBufferMap.clear();
        this.mDoubleBuffer = null;
    }

    public void deleteBuffer() {
        super.deleteBuffer();
        destroyFrameBuffers();
    }

    public boolean draw(DrawAttribute drawAttribute) {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ArrayList arrayList;
        int i6;
        int i7;
        char c;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        boolean z2;
        int i13;
        int i14;
        DrawAttribute drawAttribute2 = drawAttribute;
        if (this.mDoubleBuffer == null) {
            Log.e(TAG, "framebuffer hasn't been initialized");
            return false;
        }
        int target = drawAttribute.getTarget();
        if (target == 5) {
            DrawBasicTexAttribute drawBasicTexAttribute = (DrawBasicTexAttribute) drawAttribute2;
            i4 = drawBasicTexAttribute.mX;
            i3 = drawBasicTexAttribute.mY;
            i2 = drawBasicTexAttribute.mWidth;
            i = drawBasicTexAttribute.mHeight;
            i12 = drawBasicTexAttribute.mBasicTexture.getId();
            z2 = drawBasicTexAttribute.mIsSnapshot;
        } else if (target != 6) {
            if (target != 11) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unsupported target ");
                sb.append(target);
                Log.w(str, sb.toString());
                i5 = 0;
                i4 = 0;
                i3 = 0;
                i2 = 0;
                i = 0;
                z = false;
            } else {
                DrawYuvAttribute drawYuvAttribute = (DrawYuvAttribute) drawAttribute2;
                if (drawYuvAttribute.mBlockWidth == 0 && drawYuvAttribute.mBlockHeight == 0) {
                    i13 = drawYuvAttribute.mPictureSize.getWidth();
                    i14 = drawYuvAttribute.mPictureSize.getHeight();
                } else {
                    i13 = drawYuvAttribute.mBlockWidth;
                    i14 = drawYuvAttribute.mBlockHeight;
                }
                i3 = 0;
                i = i14;
                i2 = i13;
                z = true;
                i5 = 0;
                i4 = 0;
            }
            if (i2 != 0 || i == 0) {
                Log.e(TAG, String.format(Locale.ENGLISH, "invalid size: %dx%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i)}));
                return false;
            }
            int i15 = this.mBufferWidth;
            int i16 = this.mBufferHeight;
            ArrayList renders = getRenders();
            if (renders != null) {
                int size = renders.size();
                int i17 = 0;
                DrawIntTexAttribute drawIntTexAttribute = null;
                while (i17 < size) {
                    Render render = (Render) renders.get(i17);
                    int i18 = size;
                    boolean z3 = i17 < size + -1;
                    String str2 = TAG;
                    ArrayList arrayList2 = renders;
                    StringBuilder sb2 = new StringBuilder();
                    int i19 = i16;
                    sb2.append("renders[");
                    sb2.append(i17);
                    sb2.append("]=");
                    sb2.append(render);
                    sb2.append(" start");
                    OpenGlUtils.checkGlErrorAndWarning(str2, sb2.toString());
                    if (z3) {
                        beginBindFrameBuffer(this.mDoubleBuffer.getOutputBuffer());
                    }
                    if (i17 == 0) {
                        if (11 == target || !z3) {
                            i7 = i17;
                            int i20 = i15;
                            i6 = i18;
                            arrayList = arrayList2;
                            i11 = i19;
                            c = 11;
                            render.draw(drawAttribute2);
                            i15 = i20;
                        } else {
                            i7 = i17;
                            i6 = i18;
                            arrayList = arrayList2;
                            i11 = i19;
                            c = 11;
                            int i21 = i15;
                            drawIntTexAttribute = new DrawIntTexAttribute(i5, 0, 0, i15, i11, z);
                            render.draw(drawIntTexAttribute);
                        }
                        i8 = i11;
                    } else {
                        i7 = i17;
                        int i22 = i15;
                        i6 = i18;
                        arrayList = arrayList2;
                        int i23 = i19;
                        c = 11;
                        i8 = i23;
                        render.setPreviousFrameBufferInfo(this.mDoubleBuffer.getInputBuffer().getId(), i15, i8);
                        if (!z3) {
                            drawIntTexAttribute.mX = i4;
                            drawIntTexAttribute.mY = i3;
                            drawIntTexAttribute.mWidth = i2;
                            drawIntTexAttribute.mHeight = i;
                        }
                        render.draw(drawIntTexAttribute);
                    }
                    if (z3) {
                        endBindFrameBuffer();
                        i9 = i8;
                        i10 = i15;
                        drawIntTexAttribute = new DrawIntTexAttribute(this.mDoubleBuffer.getOutputBuffer().getTexture().getId(), 0, 0, i15, i9, z);
                        this.mDoubleBuffer.swapBuffer();
                    } else {
                        i9 = i8;
                        i10 = i15;
                    }
                    i17 = i7 + 1;
                    i15 = i10;
                    i16 = i9;
                    char c2 = c;
                    size = i6;
                    renders = arrayList;
                }
            }
            return true;
        } else {
            DrawIntTexAttribute drawIntTexAttribute2 = (DrawIntTexAttribute) drawAttribute2;
            i4 = drawIntTexAttribute2.mX;
            i3 = drawIntTexAttribute2.mY;
            i2 = drawIntTexAttribute2.mWidth;
            i = drawIntTexAttribute2.mHeight;
            i12 = drawIntTexAttribute2.mTexId;
            z2 = drawIntTexAttribute2.mIsSnapshot;
        }
        z = z2;
        i5 = i12;
        if (i2 != 0) {
        }
        Log.e(TAG, String.format(Locale.ENGLISH, "invalid size: %dx%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i)}));
        return false;
    }

    public boolean drawOnExtraFrameBufferOnce(DrawAttribute drawAttribute) {
        int i;
        int i2;
        int target = drawAttribute.getTarget();
        if (target == 5) {
            DrawBasicTexAttribute drawBasicTexAttribute = (DrawBasicTexAttribute) drawAttribute;
            i = drawBasicTexAttribute.mWidth;
            i2 = drawBasicTexAttribute.mHeight;
        } else if (target != 6) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("unsupported target ");
            sb.append(target);
            Log.w(str, sb.toString());
            i2 = 0;
            i = 0;
        } else {
            DrawIntTexAttribute drawIntTexAttribute = (DrawIntTexAttribute) drawAttribute;
            i = drawIntTexAttribute.mWidth;
            i2 = drawIntTexAttribute.mHeight;
        }
        if (i == 0 || i2 == 0) {
            Log.e(TAG, String.format(Locale.ENGLISH, "invalid size: %dx%d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
            return false;
        }
        ArrayList renders = getRenders();
        if (renders != null) {
            int size = renders.size();
            if (size == 1) {
                ((Render) renders.get(0)).draw(drawAttribute);
            } else {
                Log.e(TAG, String.format(Locale.ENGLISH, "renders more than 1: %d", new Object[]{Integer.valueOf(size)}));
            }
        }
        return true;
    }

    public void reInitFrameBuffers(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("x");
        sb.append(i2);
        String sb2 = sb.toString();
        DoubleBuffer doubleBuffer = (DoubleBuffer) this.mBufferMap.get(sb2);
        if (doubleBuffer == null) {
            doubleBuffer = new DoubleBuffer(i, i2);
            this.mBufferMap.put(sb2, doubleBuffer);
        }
        this.mDoubleBuffer = doubleBuffer;
    }

    public void setFrameBufferSize(int i, int i2) {
        if (this.mBufferWidth != i || this.mBufferHeight != i2) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setFrameBufferSize w:");
            sb.append(i);
            sb.append(",h:");
            sb.append(i2);
            Log.d(str, sb.toString());
            this.mBufferWidth = i;
            this.mBufferHeight = i2;
            reInitFrameBuffers(i, i2);
        }
    }
}
