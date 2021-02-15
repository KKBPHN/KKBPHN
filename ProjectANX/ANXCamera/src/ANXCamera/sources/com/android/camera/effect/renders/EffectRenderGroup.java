package com.android.camera.effect.renders;

import android.graphics.Color;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.ComponentRunningTiltValue;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.EffectController.EffectChangedListener;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.draw_mode.DrawFillRectAttribute;
import com.android.camera.effect.framework.gles.OpenGlUtils;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.GLCanvas;

public class EffectRenderGroup extends RenderGroup implements EffectChangedListener {
    private static final String TAG = "EffectRenderGroup";
    private int mEffectId = FilterInfo.FILTER_ID_NONE;
    private Boolean mEffectPopup = new Boolean(false);
    private boolean mFirstFrameDrawn;
    private boolean mIsFocusPeakEnabled;
    private boolean mIsKaleidoscopeEnabled;
    private boolean mIsMakeupEnabled;
    private boolean mIsZebraEnabled;
    private PipeRenderPair mPreviewPipeRender;
    private PipeRender mPreviewSecondRender;
    private RenderGroup mRenderCaches;
    private Boolean mRenderChanged = new Boolean(false);
    private final Object mRenderChangedLock = new Object();
    private String mTiltShiftMode;

    public EffectRenderGroup(GLCanvas gLCanvas) {
        super(gLCanvas);
        this.mRenderCaches = gLCanvas.getEffectRenderGroup();
        this.mPreviewPipeRender = new PipeRenderPair(gLCanvas);
        this.mPreviewPipeRender.setFirstRender(new SurfaceTextureRender(gLCanvas));
        this.mPreviewSecondRender = new PipeRender(gLCanvas);
        addRender(this.mPreviewPipeRender);
    }

    private void drawAnimationMask(DrawAttribute drawAttribute) {
        int blurAnimationValue = EffectController.getInstance().getBlurAnimationValue();
        if (blurAnimationValue > 0) {
            GLCanvas gLCanvas = this.mGLCanvas;
            DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) drawAttribute;
            DrawFillRectAttribute drawFillRectAttribute = new DrawFillRectAttribute(0, 0, drawExtTexAttribute.mWidth, drawExtTexAttribute.mHeight, Color.argb(blurAnimationValue, 0, 0, 0));
            gLCanvas.draw(drawFillRectAttribute);
        }
    }

    private boolean drawPreview(DrawAttribute drawAttribute) {
        PipeRenderPair pipeRenderPair;
        PipeRender pipeRender;
        if (!this.mFirstFrameDrawn) {
            this.mFirstFrameDrawn = true;
            setViewportSize(this.mViewportWidth, this.mViewportHeight);
            setPreviewSize(this.mPreviewWidth, this.mPreviewHeight);
        }
        if (updatePreviewSecondRender(((DrawExtTexAttribute) drawAttribute).mEffectPopup)) {
            if (this.mPreviewSecondRender.getRenderSize() == 0) {
                pipeRenderPair = this.mPreviewPipeRender;
                pipeRender = null;
            } else if (this.mPreviewPipeRender.getRenderSize() == 1) {
                pipeRenderPair = this.mPreviewPipeRender;
                pipeRender = this.mPreviewSecondRender;
            }
            pipeRenderPair.setSecondRender(pipeRender);
        }
        this.mPreviewPipeRender.setUsedMiddleBuffer(EffectController.getInstance().isBackGroundBlur());
        this.mPreviewPipeRender.draw(drawAttribute);
        drawAnimationMask(drawAttribute);
        return true;
    }

    private Render fetchRender(int i) {
        Render render = this.mRenderCaches.getRender(i);
        if (render != null) {
            return render;
        }
        this.mGLCanvas.prepareEffectRenders(false, i);
        return this.mRenderCaches.getRender(i);
    }

    private Render getTiltShitRender() {
        int i;
        String str = this.mTiltShiftMode;
        if (str != null) {
            if (ComponentRunningTiltValue.TILT_CIRCLE.equals(str)) {
                i = FilterInfo.FILTER_ID_GAUSSIAN;
            } else {
                if (ComponentRunningTiltValue.TILT_PARALLEL.equals(this.mTiltShiftMode)) {
                    i = FilterInfo.FILTER_ID_TILTSHIFT;
                }
            }
            return fetchRender(i);
        }
        return null;
    }

    private void removeCache(int i) {
        this.mRenderCaches.removeRender(i);
    }

    private boolean updatePreviewSecondRender(boolean z) {
        Render fetchRender;
        PipeRender pipeRender;
        if (!this.mRenderChanged.booleanValue() && this.mEffectPopup.booleanValue() == z) {
            return false;
        }
        synchronized (this.mRenderChangedLock) {
            OpenGlUtils.checkGlErrorAndWarning(TAG, "before updatePreviewSecondRender");
            this.mPreviewSecondRender.clearRenders();
            if (EffectController.getInstance().needDestroyMakeup()) {
                removeCache(FilterInfo.RENDER_ID_MAKEUP);
                EffectController.getInstance().setDestroyMakeup(false);
            }
            if (this.mIsMakeupEnabled && EffectController.getInstance().isBeautyFrameReady()) {
                Render fetchRender2 = fetchRender(FilterInfo.RENDER_ID_MAKEUP);
                if (fetchRender2 != null) {
                    this.mPreviewSecondRender.addRender(fetchRender2);
                }
            }
            if (this.mIsKaleidoscopeEnabled) {
                Render fetchRender3 = fetchRender(FilterInfo.FILTER_ID_KALEIDOSCOPE);
                if (fetchRender3 != null) {
                    this.mPreviewSecondRender.addRender(fetchRender3);
                    fetchRender3.setKaleidoscope(EffectController.getInstance().getCurrentKaleidoscope());
                }
            }
            if (!z && this.mIsZebraEnabled) {
                Render fetchRender4 = fetchRender(FilterInfo.FILTER_ID_ZEBRA);
                if (fetchRender4 != null) {
                    this.mPreviewSecondRender.addRender(fetchRender4);
                }
            }
            if (this.mEffectId != FilterInfo.FILTER_ID_NONE) {
                Render fetchRender5 = fetchRender(this.mEffectId);
                if (fetchRender5 != null) {
                    this.mPreviewSecondRender.addRender(fetchRender5);
                }
            }
            if (!z) {
                if (this.mTiltShiftMode != null) {
                    fetchRender = getTiltShitRender();
                    if (fetchRender != null) {
                        pipeRender = this.mPreviewSecondRender;
                    }
                } else if (this.mIsFocusPeakEnabled) {
                    fetchRender = fetchRender(FilterInfo.FILTER_ID_PEAKINGMF);
                    if (fetchRender != null) {
                        pipeRender = this.mPreviewSecondRender;
                    }
                }
                pipeRender.addRender(fetchRender);
            }
            this.mPreviewSecondRender.setFrameBufferSize(this.mPreviewWidth, this.mPreviewHeight);
            this.mRenderChanged = Boolean.valueOf(false);
            this.mEffectPopup = Boolean.valueOf(z);
            OpenGlUtils.checkGlErrorAndWarning(TAG, "after updatePreviewSecondRender");
        }
        return true;
    }

    public boolean draw(DrawAttribute drawAttribute) {
        if (EffectController.getInstance().getEffectForPreview(true) != this.mEffectId && EffectController.getInstance().isBackGroundBlur()) {
            this.mPreviewPipeRender.prepareCopyBlurTexture();
        }
        if (drawAttribute.getTarget() != 8) {
            return false;
        }
        return drawPreview(drawAttribute);
    }

    public void onEffectChanged(int... iArr) {
        synchronized (this.mRenderChangedLock) {
            EffectController instance = EffectController.getInstance();
            DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
            for (int i : iArr) {
                if (i == 1) {
                    this.mEffectId = instance.getEffectForPreview(true);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onEffectChanged: EFFECT_CHANGE_FILTER mEffectId = ");
                    sb.append(this.mEffectId);
                    Log.d(str, sb.toString());
                } else if (i == 3) {
                    this.mIsMakeupEnabled = instance.isMakeupEnable();
                } else if (i == 4) {
                    this.mIsFocusPeakEnabled = instance.isNeedDrawPeaking();
                } else if (i != 5) {
                    if (i == 7) {
                        this.mIsZebraEnabled = instance.isNeedDrawExposure();
                    } else if (i == 8) {
                        this.mIsKaleidoscopeEnabled = instance.isKaleidoscopeEnable();
                    }
                } else if (instance.isDrawTilt()) {
                    this.mTiltShiftMode = dataItemRunning.getComponentRunningTiltValue().getComponentValue(160);
                } else {
                    this.mTiltShiftMode = null;
                }
            }
            this.mRenderChanged = Boolean.valueOf(true);
        }
    }

    public void onRealtimePreviewFilterChanged(int i) {
        synchronized (this.mRenderChangedLock) {
            this.mEffectId = i;
            this.mRenderChanged = Boolean.valueOf(true);
        }
    }
}
