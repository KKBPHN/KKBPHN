package com.android.camera.effect.renders;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import com.android.camera.CameraSettings;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.GLCanvas;
import java.util.Locale;

public class VideoRecorderRender extends RenderGroup {
    private static final String TAG = "VideoRecorderRender";
    private boolean mCinematicEnabled;
    private int mEffectId = FilterInfo.FILTER_ID_NONE;
    private boolean mFirstFrameDrawn;
    private boolean mKaleidoscopeEnabled;
    private PipeRenderPair mRenderPipe;
    private PipeRender mSecondRender;

    public VideoRecorderRender(GLCanvas gLCanvas) {
        super(gLCanvas);
        this.mRenderPipe = new PipeRenderPair(gLCanvas);
        this.mRenderPipe.setFirstRender(new SurfaceTextureRender(gLCanvas));
        updateSecondRender();
        addRender(this.mRenderPipe);
        addRender(new BasicRender(gLCanvas));
        addCustomRender();
    }

    private void addCustomRender() {
        if (C0122O00000o.instance().oO0OO0() && CameraSettings.isProAmbilightOpen()) {
            addRender(new Yuv444ToRgbRender(this.mGLCanvas));
        }
    }

    private boolean drawPreview(DrawAttribute drawAttribute) {
        if (!this.mFirstFrameDrawn) {
            this.mFirstFrameDrawn = true;
            setViewportSize(this.mViewportWidth, this.mViewportHeight);
            setPreviewSize(this.mPreviewWidth, this.mPreviewHeight);
            PipeRender pipeRender = this.mSecondRender;
            if (pipeRender != null) {
                pipeRender.setFrameBufferSize(this.mPreviewWidth, this.mPreviewHeight);
            }
        }
        synchronized (this) {
            super.draw(drawAttribute);
        }
        return true;
    }

    private PipeRender getSecondPipeRender() {
        PipeRender pipeRender = this.mSecondRender;
        if (pipeRender != null) {
            return pipeRender;
        }
        this.mSecondRender = new PipeRender(this.mGLCanvas);
        return this.mSecondRender;
    }

    private Render getSecondRender(int i, boolean z, boolean z2) {
        Render render;
        Render render2;
        Render render3;
        if (i != FilterInfo.FILTER_ID_NONE) {
            Render render4 = this.mGLCanvas.getEffectRenderGroup().getRender(i);
            if (render4 == null) {
                this.mGLCanvas.prepareEffectRenders(false, i);
                render = this.mGLCanvas.getEffectRenderGroup().getRender(i);
            } else {
                render = render4;
            }
        } else {
            render = null;
        }
        if (z) {
            render2 = this.mGLCanvas.getEffectRenderGroup().getRender(FilterInfo.FILTER_ID_KALEIDOSCOPE);
            if (render2 == null) {
                this.mGLCanvas.prepareEffectRenders(false, FilterInfo.FILTER_ID_KALEIDOSCOPE);
                render2 = this.mGLCanvas.getEffectRenderGroup().getRender(FilterInfo.FILTER_ID_KALEIDOSCOPE);
            }
            render2.setKaleidoscope(EffectController.getInstance().getCurrentKaleidoscope());
        } else {
            render2 = null;
        }
        if (z2) {
            render3 = this.mGLCanvas.getEffectRenderGroup().getRender(FilterInfo.FILTER_ID_CINEMATIC);
            if (render3 == null) {
                this.mGLCanvas.prepareEffectRenders(false, FilterInfo.FILTER_ID_CINEMATIC);
                render3 = this.mGLCanvas.getEffectRenderGroup().getRender(FilterInfo.FILTER_ID_CINEMATIC);
            }
        } else {
            render3 = null;
        }
        PipeRender pipeRender = this.mSecondRender;
        if (pipeRender != null) {
            pipeRender.clearRenders();
        }
        if (render != null) {
            getSecondPipeRender().addRender(render);
        }
        if (render2 != null) {
            getSecondPipeRender().addRender(render2);
        }
        if (render3 != null) {
            getSecondPipeRender().addRender(render3);
        }
        if (getSecondPipeRender().getRenderSize() > 0) {
            return this.mSecondRender;
        }
        return null;
    }

    private void updateSecondRender() {
        int i = this.mEffectId;
        this.mEffectId = EffectController.getInstance().getEffectForPreview(false);
        boolean z = this.mKaleidoscopeEnabled;
        this.mKaleidoscopeEnabled = EffectController.getInstance().isKaleidoscopeEnable();
        Log.d(TAG, String.format(Locale.ENGLISH, "effectId: 0x%x->0x%x KaleidoscopeEnabled: %b->%b", new Object[]{Integer.valueOf(i), Integer.valueOf(this.mEffectId), Boolean.valueOf(z), Boolean.valueOf(this.mKaleidoscopeEnabled)}));
        boolean z2 = this.mCinematicEnabled;
        this.mCinematicEnabled = EffectController.getInstance().isCinematicEnable();
        if (this.mEffectId != i || this.mKaleidoscopeEnabled != z || z2 != this.mCinematicEnabled) {
            this.mFirstFrameDrawn = false;
            this.mRenderPipe.setSecondRender(getSecondRender(this.mEffectId, this.mKaleidoscopeEnabled, this.mCinematicEnabled));
        }
    }

    public void deleteBuffer() {
        PipeRender pipeRender = this.mSecondRender;
        if (pipeRender != null) {
            pipeRender.deleteBuffer();
        }
        super.deleteBuffer();
    }

    public void destroy() {
        PipeRender pipeRender = this.mSecondRender;
        if (pipeRender != null) {
            pipeRender.destroy();
        }
        super.destroy();
    }

    public boolean draw(DrawAttribute drawAttribute) {
        int target = drawAttribute.getTarget();
        if (target == 4 || target == 5 || target == 8 || target == 11) {
            return drawPreview(drawAttribute);
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unsupported target ");
        sb.append(drawAttribute.getTarget());
        Log.e(str, sb.toString());
        return false;
    }

    public void setFilterId(int i) {
        if (i != this.mEffectId) {
            synchronized (this) {
                updateSecondRender();
            }
        }
    }

    public void updateCinematic() {
        updateSecondRender();
    }
}
