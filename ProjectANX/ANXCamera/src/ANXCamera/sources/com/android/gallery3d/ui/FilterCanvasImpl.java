package com.android.gallery3d.ui;

import android.opengl.GLES20;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.renders.NoneEffectRender;
import com.android.camera.effect.renders.PipeRenderPair;
import com.android.camera.effect.renders.RenderGroup;
import com.android.camera.effect.renders.SurfaceTextureRender;

public class FilterCanvasImpl extends BaseGLCanvas {
    private static final String TAG = "FilterCanvasImpl";
    private int mCurrentTarget = -1;
    private boolean mHasEffectRender = false;
    private PipeRenderPair mPipeRender = new PipeRenderPair(this);

    public FilterCanvasImpl() {
        this.mRenderCaches.addRender(new SurfaceTextureRender(this));
        this.mRenderCaches.addRender(new NoneEffectRender(this));
        this.mRenderGroup.addRender(this.mPipeRender);
        initialize();
    }

    private void targetChange() {
        RenderGroup renderGroup;
        PipeRenderPair pipeRenderPair;
        this.mPipeRender.setFirstRender(null);
        this.mPipeRender.setSecondRender(null);
        int i = 2;
        if (this.mCurrentTarget == 8) {
            this.mPipeRender.setFirstRender(this.mRenderCaches.getRenderByIndex(0));
            if (this.mHasEffectRender) {
                this.mPipeRender.setSecondRender(this.mRenderCaches.getRenderByIndex(2));
                return;
            }
            return;
        }
        if (this.mHasEffectRender) {
            pipeRenderPair = this.mPipeRender;
            renderGroup = this.mRenderCaches;
        } else {
            pipeRenderPair = this.mPipeRender;
            renderGroup = this.mRenderCaches;
            i = 1;
        }
        pipeRenderPair.setFirstRender(renderGroup.getRenderByIndex(i));
    }

    public void deleteProgram() {
        super.deleteProgram();
        this.mRenderCaches.destroy();
        this.mRenderGroup.destroy();
    }

    public void draw(DrawAttribute drawAttribute) {
        if (this.mCurrentTarget != drawAttribute.getTarget()) {
            this.mCurrentTarget = drawAttribute.getTarget();
            targetChange();
        }
        this.mRenderGroup.draw(drawAttribute);
    }

    /* access modifiers changed from: protected */
    public void initialize() {
        super.initialize();
        GLES20.glEnable(3024);
        GLES20.glLineWidth(1.0f);
    }

    public void prepareEffectRenders(boolean z, int i) {
        if (this.mRenderCaches.isNeedInit(i)) {
            EffectController.getInstance().getEffectGroup(this, this.mRenderCaches, z, false, i);
            if (this.mRenderCaches.getRender(i) != null) {
                this.mHasEffectRender = true;
            }
        }
    }
}
