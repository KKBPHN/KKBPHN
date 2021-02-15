package com.android.camera.effect.renders;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FrameBuffer;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.draw_mode.DrawBasicTexAttribute;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.draw_mode.DrawIntTexAttribute;
import com.android.camera.effect.framework.gles.OpenGlUtils;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.GLCanvas;
import com.android.gallery3d.ui.RawTexture;
import com.android.gallery3d.ui.Utils;
import java.util.ArrayList;
import java.util.Iterator;

public final class PipeRenderPair extends RenderGroup {
    private static final int MIDDLE_BUFFER_RATIO = 12;
    private static final String TAG = "PipeRenderPair";
    private DrawBasicTexAttribute mBasicTextureAttr = new DrawBasicTexAttribute();
    private FrameBuffer mBlurFrameBuffer;
    private int mBufferHeight = -1;
    private int mBufferWidth = -1;
    private DrawExtTexAttribute mExtTexture = new DrawExtTexAttribute();
    private Render mFirstRender;
    private FrameBuffer mFrameBuffer;
    private ArrayList mFrameBuffers = new ArrayList();
    private FrameBuffer mMiddleFrameBuffer;
    private Render mSecondRender;
    private boolean mTextureFilled = false;
    private boolean mUseMiddleBuffer = false;

    public PipeRenderPair(GLCanvas gLCanvas) {
        super(gLCanvas);
    }

    public PipeRenderPair(GLCanvas gLCanvas, int i) {
        super(gLCanvas, i);
    }

    public PipeRenderPair(GLCanvas gLCanvas, int i, Render render, Render render2, boolean z) {
        super(gLCanvas, i);
        setRenderPairs(render, render2);
        this.mUseMiddleBuffer = z;
    }

    public PipeRenderPair(GLCanvas gLCanvas, Render render, Render render2, boolean z) {
        super(gLCanvas);
        setRenderPairs(render, render2);
        this.mUseMiddleBuffer = z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x006a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private FrameBuffer getFrameBuffer(int i, int i2) {
        FrameBuffer frameBuffer;
        double d;
        double d2;
        double d3;
        if (!this.mFrameBuffers.isEmpty()) {
            int size = this.mFrameBuffers.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                int width = ((FrameBuffer) this.mFrameBuffers.get(size)).getWidth();
                int height = ((FrameBuffer) this.mFrameBuffers.get(size)).getHeight();
                if (i < i2) {
                    d3 = ((double) height) / ((double) width);
                    d2 = (double) i2;
                    d = (double) i;
                } else {
                    d3 = ((double) width) / ((double) height);
                    d2 = (double) i;
                    d = (double) i2;
                }
                if (Math.abs(d3 - (d2 / d)) <= 0.1d && Utils.nextPowerOf2(width) == Utils.nextPowerOf2(i) && Utils.nextPowerOf2(height) == Utils.nextPowerOf2(i2)) {
                    frameBuffer = (FrameBuffer) this.mFrameBuffers.get(size);
                    break;
                }
                size--;
            }
            if (frameBuffer == null) {
                frameBuffer = new FrameBuffer(this.mGLCanvas, i, i2, this.mParentFrameBufferId);
                Object[] objArr = {Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(frameBuffer.getId())};
                Log.i("Counter", String.format("FrameBuffer alloc size %d*%d id %d", objArr));
                if (this.mFrameBuffers.size() > 5) {
                    ArrayList arrayList = this.mFrameBuffers;
                    arrayList.remove(arrayList.size() - 1);
                }
                this.mFrameBuffers.add(frameBuffer);
            }
            return frameBuffer;
        }
        frameBuffer = null;
        if (frameBuffer == null) {
        }
        return frameBuffer;
    }

    private void setFrameBufferInfo(Render render, FrameBuffer frameBuffer) {
        render.setPreviousFrameBufferInfo(frameBuffer.getId(), frameBuffer.getWidth(), frameBuffer.getHeight());
    }

    private void updateMiddleBuffer(int i, int i2) {
        if (this.mUseMiddleBuffer) {
            this.mBufferWidth = i / 12;
            i2 /= 12;
        } else {
            this.mBufferWidth = i;
        }
        this.mBufferHeight = i2;
    }

    public void addRender(Render render) {
        if (getRenderSize() < 2) {
            super.addRender(render);
            return;
        }
        throw new RuntimeException("At most 2 renders are supported in PipeRenderPair!");
    }

    public void copyBlurTexture(DrawExtTexAttribute drawExtTexAttribute) {
        if (EffectController.getInstance().isBackGroundBlur() && !this.mTextureFilled) {
            OpenGlUtils.checkGlErrorAndWarning(TAG, "before copyBlurTexture draw");
            FrameBuffer frameBuffer = this.mBlurFrameBuffer;
            if (!(frameBuffer != null && frameBuffer.getWidth() == drawExtTexAttribute.mWidth && this.mBlurFrameBuffer.getHeight() == drawExtTexAttribute.mHeight)) {
                FrameBuffer frameBuffer2 = this.mBlurFrameBuffer;
                if (frameBuffer2 != null) {
                    frameBuffer2.release();
                }
                this.mBlurFrameBuffer = new FrameBuffer(this.mGLCanvas, drawExtTexAttribute.mWidth, drawExtTexAttribute.mHeight, this.mParentFrameBufferId);
            }
            beginBindFrameBuffer(this.mBlurFrameBuffer);
            this.mSecondRender.draw(this.mBasicTextureAttr.init((this.mUseMiddleBuffer ? this.mMiddleFrameBuffer : this.mFrameBuffer).getTexture(), drawExtTexAttribute.mX, drawExtTexAttribute.mY, drawExtTexAttribute.mWidth, drawExtTexAttribute.mHeight));
            endBindFrameBuffer();
            this.mTextureFilled = true;
            OpenGlUtils.checkGlErrorAndWarning(TAG, "after copyBlurTexture draw");
        }
    }

    public void deleteBuffer() {
        super.deleteBuffer();
        FrameBuffer frameBuffer = this.mBlurFrameBuffer;
        if (frameBuffer != null) {
            frameBuffer.release();
            this.mBlurFrameBuffer = null;
        }
        Iterator it = this.mFrameBuffers.iterator();
        while (it.hasNext()) {
            ((FrameBuffer) it.next()).release();
        }
        this.mFrameBuffers.clear();
        this.mFrameBuffer = null;
        this.mMiddleFrameBuffer = null;
    }

    public boolean draw(DrawAttribute drawAttribute) {
        String str;
        String str2;
        StringBuilder sb;
        if (getRenderSize() == 0) {
            return false;
        }
        if (getRenderSize() == 1 || this.mFirstRender == this.mSecondRender) {
            return getRenderByIndex(0).draw(drawAttribute);
        }
        String str3 = "after mSecondRender=";
        String str4 = "before mSecondRender=";
        String str5 = "after mFirstRender=";
        String str6 = "before mFirstRender=";
        if (drawAttribute.getTarget() == 8) {
            DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) drawAttribute;
            String str7 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str6);
            sb2.append(this.mFirstRender);
            String str8 = " drawExt";
            sb2.append(str8);
            OpenGlUtils.checkGlErrorAndWarning(str7, sb2.toString());
            this.mFrameBuffer = getFrameBuffer(this.mPreviewWidth, this.mPreviewHeight);
            beginBindFrameBuffer(this.mFrameBuffer);
            this.mFirstRender.draw(this.mExtTexture.init(drawExtTexAttribute.mExtTexture, drawExtTexAttribute.mTextureTransform, 0, 0, this.mFrameBuffer.getTexture().getTextureWidth(), this.mFrameBuffer.getTexture().getTextureHeight()));
            String str9 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str5);
            sb3.append(this.mFirstRender);
            sb3.append(str8);
            OpenGlUtils.checkGlErrorAndWarning(str9, sb3.toString());
            endBindFrameBuffer();
            setFrameBufferInfo(this.mSecondRender, this.mFrameBuffer);
            if (this.mUseMiddleBuffer) {
                String str10 = TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("before MiddleFrameBuffer=");
                sb4.append(this.mFirstRender);
                sb4.append(str8);
                OpenGlUtils.checkGlErrorAndWarning(str10, sb4.toString());
                updateMiddleBuffer(this.mPreviewWidth, this.mPreviewHeight);
                this.mMiddleFrameBuffer = getFrameBuffer(this.mBufferWidth, this.mBufferHeight);
                beginBindFrameBuffer(this.mMiddleFrameBuffer);
                this.mFirstRender.draw(this.mExtTexture.init(drawExtTexAttribute.mExtTexture, drawExtTexAttribute.mTextureTransform, 0, 0, this.mBufferWidth, this.mBufferHeight));
                endBindFrameBuffer();
                String str11 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("after MiddleFrameBuffer=");
                sb5.append(this.mFirstRender);
                sb5.append(str8);
                OpenGlUtils.checkGlErrorAndWarning(str11, sb5.toString());
            }
            if (EffectController.getInstance().isMainFrameDisplay()) {
                if (!C0124O00000oO.OOoooOo() || !EffectController.getInstance().isBackGroundBlur()) {
                    String str12 = TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str4);
                    sb6.append(this.mSecondRender);
                    sb6.append(str8);
                    OpenGlUtils.checkGlErrorAndWarning(str12, sb6.toString());
                    this.mSecondRender.draw(this.mBasicTextureAttr.init((this.mUseMiddleBuffer ? this.mMiddleFrameBuffer : this.mFrameBuffer).getTexture(), drawExtTexAttribute.mX, drawExtTexAttribute.mY, drawExtTexAttribute.mWidth, drawExtTexAttribute.mHeight));
                    String str13 = TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str3);
                    sb7.append(this.mSecondRender);
                    sb7.append(str8);
                    OpenGlUtils.checkGlErrorAndWarning(str13, sb7.toString());
                } else {
                    copyBlurTexture(drawExtTexAttribute);
                    drawBlurTexture(drawExtTexAttribute);
                }
            }
            return true;
        }
        if (drawAttribute.getTarget() == 5 || drawAttribute.getTarget() == 10) {
            DrawBasicTexAttribute drawBasicTexAttribute = (DrawBasicTexAttribute) drawAttribute;
            if (drawAttribute.getTarget() == 10) {
                updateMiddleBuffer(drawBasicTexAttribute.mWidth, drawBasicTexAttribute.mHeight);
            }
            String str14 = TAG;
            StringBuilder sb8 = new StringBuilder();
            sb8.append(str6);
            sb8.append(this.mFirstRender);
            str = " drawBasic";
            sb8.append(str);
            OpenGlUtils.checkGlErrorAndWarning(str14, sb8.toString());
            this.mFrameBuffer = getFrameBuffer(this.mBufferWidth, this.mBufferHeight);
            beginBindFrameBuffer(this.mFrameBuffer);
            this.mFirstRender.draw(this.mBasicTextureAttr.init(drawBasicTexAttribute.mBasicTexture, 0, 0, this.mFrameBuffer.getTexture().getTextureWidth(), this.mFrameBuffer.getTexture().getTextureHeight()));
            endBindFrameBuffer();
            String str15 = TAG;
            StringBuilder sb9 = new StringBuilder();
            sb9.append(str5);
            sb9.append(this.mFirstRender);
            sb9.append(str);
            OpenGlUtils.checkGlErrorAndWarning(str15, sb9.toString());
            setFrameBufferInfo(this.mSecondRender, this.mFrameBuffer);
            String str16 = TAG;
            StringBuilder sb10 = new StringBuilder();
            sb10.append(str4);
            sb10.append(this.mSecondRender);
            sb10.append(str);
            OpenGlUtils.checkGlErrorAndWarning(str16, sb10.toString());
            this.mSecondRender.draw(this.mBasicTextureAttr.init(this.mFrameBuffer.getTexture(), drawBasicTexAttribute.mX, drawBasicTexAttribute.mY, drawBasicTexAttribute.mWidth, drawBasicTexAttribute.mHeight));
            str2 = TAG;
            sb = new StringBuilder();
        } else if (drawAttribute.getTarget() != 6) {
            return false;
        } else {
            DrawIntTexAttribute drawIntTexAttribute = (DrawIntTexAttribute) drawAttribute;
            String str17 = TAG;
            StringBuilder sb11 = new StringBuilder();
            sb11.append(str6);
            sb11.append(this.mFirstRender);
            str = " drawInt";
            sb11.append(str);
            OpenGlUtils.checkGlErrorAndWarning(str17, sb11.toString());
            this.mFrameBuffer = getFrameBuffer(drawIntTexAttribute.mWidth, drawIntTexAttribute.mHeight);
            beginBindFrameBuffer(this.mFrameBuffer);
            Render render = this.mFirstRender;
            DrawIntTexAttribute drawIntTexAttribute2 = new DrawIntTexAttribute(drawIntTexAttribute.mTexId, 0, 0, this.mFrameBuffer.getTexture().getTextureWidth(), this.mFrameBuffer.getTexture().getTextureHeight(), drawIntTexAttribute.mIsSnapshot);
            render.draw(drawIntTexAttribute2);
            endBindFrameBuffer();
            String str18 = TAG;
            StringBuilder sb12 = new StringBuilder();
            sb12.append(str5);
            sb12.append(this.mFirstRender);
            sb12.append(str);
            OpenGlUtils.checkGlErrorAndWarning(str18, sb12.toString());
            setFrameBufferInfo(this.mSecondRender, this.mFrameBuffer);
            String str19 = TAG;
            StringBuilder sb13 = new StringBuilder();
            sb13.append(str4);
            sb13.append(this.mSecondRender);
            sb13.append(str);
            OpenGlUtils.checkGlErrorAndWarning(str19, sb13.toString());
            this.mSecondRender.draw(this.mBasicTextureAttr.init(this.mFrameBuffer.getTexture(), drawIntTexAttribute.mX, drawIntTexAttribute.mY, drawIntTexAttribute.mWidth, drawIntTexAttribute.mHeight, drawIntTexAttribute.mIsSnapshot));
            str2 = TAG;
            sb = new StringBuilder();
        }
        sb.append(str3);
        sb.append(this.mSecondRender);
        sb.append(str);
        OpenGlUtils.checkGlErrorAndWarning(str2, sb.toString());
        return true;
    }

    public void drawBlurTexture(DrawExtTexAttribute drawExtTexAttribute) {
        if (EffectController.getInstance().isBackGroundBlur() && this.mTextureFilled) {
            OpenGlUtils.checkGlErrorAndWarning(TAG, "before drawBlurTexture draw");
            GLCanvas gLCanvas = this.mGLCanvas;
            DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(this.mBlurFrameBuffer.getTexture(), drawExtTexAttribute.mX, drawExtTexAttribute.mY, drawExtTexAttribute.mWidth, drawExtTexAttribute.mHeight);
            gLCanvas.draw(drawBasicTexAttribute);
            OpenGlUtils.checkGlErrorAndWarning(TAG, "after drawBlurTexture draw");
        }
    }

    public RawTexture getTexture() {
        FrameBuffer frameBuffer = this.mFrameBuffer;
        if (frameBuffer == null) {
            return null;
        }
        return frameBuffer.getTexture();
    }

    public void prepareCopyBlurTexture() {
        this.mTextureFilled = false;
    }

    public void setFirstRender(Render render) {
        clearRenders();
        if (render != null) {
            addRender(render);
        }
        this.mFirstRender = render;
        Render render2 = this.mSecondRender;
        if (render2 != null) {
            addRender(render2);
        }
    }

    public void setPreviewSize(int i, int i2) {
        super.setPreviewSize(i, i2);
        this.mBufferWidth = this.mUseMiddleBuffer ? this.mPreviewWidth / 12 : this.mPreviewWidth;
        this.mBufferHeight = this.mUseMiddleBuffer ? this.mPreviewHeight / 12 : this.mPreviewHeight;
    }

    public void setRenderPairs(Render render, Render render2) {
        if (render != this.mFirstRender || render2 != this.mSecondRender) {
            clearRenders();
            if (render != null) {
                addRender(render);
            }
            if (render2 != null) {
                addRender(render2);
            }
            this.mFirstRender = render;
            this.mSecondRender = render2;
        }
    }

    public void setSecondRender(Render render) {
        clearRenders();
        Render render2 = this.mFirstRender;
        if (render2 != null) {
            addRender(render2);
        }
        if (render != null) {
            addRender(render);
        }
        this.mSecondRender = render;
    }

    public void setUsedMiddleBuffer(boolean z) {
        this.mUseMiddleBuffer = z;
    }
}
