package com.android.camera.dualvideo.render;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.util.SparseArray;
import com.android.camera.CameraSettings;
import com.android.camera.Util;
import com.android.camera.constant.ColorConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.dualvideo.util.SelectIndex;
import com.android.camera.effect.draw_mode.DrawBasicTexAttribute;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.draw_mode.DrawFillRectAttribute;
import com.android.camera.effect.draw_mode.DrawRectShapeAttributeBase;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.BasicTexture;
import com.android.gallery3d.ui.GLCanvas;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.IIntValueProperty;

public class CameraItem implements CameraItemInterface {
    private static final int HANDLER_OFFSET = 10;
    private static final String TAG = "CameraItem";
    /* access modifiers changed from: private */
    public float mAlpha;
    private final SparseArray mDrawAttriList = new SparseArray();
    private final FaceType mFaceType;
    /* access modifiers changed from: private */
    public boolean mIsAnimating;
    /* access modifiers changed from: private */
    public boolean mIsPressedInSelectWindow = false;
    /* access modifiers changed from: private */
    public boolean mIsVisible = true;
    private LayoutType mLastLayoutType;
    private LayoutType mRenderLayoutType;
    private float mSelectWindowFlagAlpha;
    private EaseOutAnim mSelectWindowFlagAnim;
    private float mSelectWindowFlagDstAlpha;
    private final LayoutType mSelectWindowLayoutType;
    private float mSelectedFrameAlpha;
    private EaseOutAnim mSelectedFrameAnim;
    private float mSelectedFrameDstAlpha;
    private SelectIndex mSelectedType;
    private ShrinkAnimListener mShrinkListener;

    /* renamed from: com.android.camera.dualvideo.render.CameraItem$2 reason: invalid class name */
    /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$render$FaceType = new int[FaceType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$render$LayoutType = new int[LayoutType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$util$SelectIndex = new int[SelectIndex.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(25:0|(2:1|2)|3|(2:5|6)|7|9|10|11|12|13|(2:15|16)|17|19|20|21|22|23|24|25|26|27|28|29|30|32) */
        /* JADX WARNING: Can't wrap try/catch for region: R(27:0|1|2|3|(2:5|6)|7|9|10|11|12|13|15|16|17|19|20|21|22|23|24|25|26|27|28|29|30|32) */
        /* JADX WARNING: Can't wrap try/catch for region: R(28:0|1|2|3|5|6|7|9|10|11|12|13|15|16|17|19|20|21|22|23|24|25|26|27|28|29|30|32) */
        /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0032 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0064 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0079 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0084 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            try {
                $SwitchMap$com$android$camera$dualvideo$util$SelectIndex[SelectIndex.INDEX_1.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$camera$dualvideo$util$SelectIndex[SelectIndex.INDEX_2.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_BACK.ordinal()] = 1;
            $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_FRONT.ordinal()] = 2;
            try {
                $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_REMOTE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.FULL.ordinal()] = 1;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.UP.ordinal()] = 2;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.DOWN.ordinal()] = 3;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.UP_FULL.ordinal()] = 4;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.DOWN_FULL.ordinal()] = 5;
            $SwitchMap$com$android$camera$dualvideo$render$LayoutType[LayoutType.MINI.ordinal()] = 6;
        }
    }

    class RenderTypeChangeAnimListener extends TransitionListener {
        private final Rect dstRenderArea;
        private final Rect srcRenderArea;

        public RenderTypeChangeAnimListener(RegionHelper regionHelper) {
            DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) CameraItem.this.getRenderAttri(101);
            int i = drawExtTexAttribute.mX;
            int i2 = drawExtTexAttribute.mY;
            this.srcRenderArea = new Rect(i, i2, drawExtTexAttribute.mWidth + i, drawExtTexAttribute.mHeight + i2);
            this.dstRenderArea = regionHelper.getRenderAreaFor(CameraItem.this.getRenderLayoutType());
        }

        public void animatePreview(int i) {
            Rect mixArea = RenderUtil.mixArea(this.srcRenderArea, this.dstRenderArea, ((float) i) / 1000.0f);
            DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) CameraItem.this.getRenderAttri(101);
            float[] generatePreviewTransMatrix = RenderUtil.generatePreviewTransMatrix(CameraItem.this.getFaceType(), CameraItem.this.getRenderLayoutType(), drawExtTexAttribute.mExtTexture, mixArea);
            CameraItem cameraItem = CameraItem.this;
            DrawExtTexAttribute drawExtTexAttribute2 = new DrawExtTexAttribute(drawExtTexAttribute.mExtTexture, generatePreviewTransMatrix, mixArea.left, mixArea.top, mixArea.width(), mixArea.height());
            cameraItem.setRenderAttri(drawExtTexAttribute2, 101);
        }

        public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
            animatePreview(i);
            if (z) {
                CameraItem.this.mIsAnimating = false;
            }
        }
    }

    class ShrinkAnimListener extends TransitionListener {
        final DrawExtTexAttribute mAttri = ((DrawExtTexAttribute) CameraItem.this.getRenderAttri(101));
        private final Rect srcRenderArea;

        ShrinkAnimListener() {
            DrawExtTexAttribute drawExtTexAttribute = this.mAttri;
            int i = drawExtTexAttribute.mX;
            int i2 = drawExtTexAttribute.mY;
            this.srcRenderArea = new Rect(i, i2, drawExtTexAttribute.mWidth + i, drawExtTexAttribute.mHeight + i2);
        }

        public void animatePreview(int i) {
            Rect shrinkRect = RenderUtil.shrinkRect(this.srcRenderArea, 1.0f - ((((float) i) / 1000.0f) * 0.05f));
            DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) CameraItem.this.getRenderAttri(101);
            CameraItem cameraItem = CameraItem.this;
            DrawExtTexAttribute drawExtTexAttribute2 = new DrawExtTexAttribute(drawExtTexAttribute.mExtTexture, drawExtTexAttribute.mTextureTransform, shrinkRect.left, shrinkRect.top, shrinkRect.width(), shrinkRect.height());
            cameraItem.setRenderAttri(drawExtTexAttribute2, 101);
        }

        public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
            animatePreview(i);
            if (z && i == 0) {
                Log.d(CameraItem.TAG, "onUpdate: isCompleted");
                CameraItem.this.mIsAnimating = false;
                CameraItem.this.mIsPressedInSelectWindow = false;
            }
        }
    }

    public CameraItem(LayoutType layoutType, LayoutType layoutType2, FaceType faceType) {
        this.mSelectWindowLayoutType = layoutType;
        this.mRenderLayoutType = layoutType2;
        this.mLastLayoutType = layoutType2;
        this.mFaceType = faceType;
        this.mAlpha = 1.0f;
        this.mSelectedType = SelectIndex.INDEX_0;
    }

    private void animShrink() {
        AnimConfig[] animConfigArr;
        IStateStyle iStateStyle;
        StringBuilder sb = new StringBuilder();
        sb.append("animShrink: ");
        sb.append(hashCode());
        Log.d(TAG, sb.toString());
        boolean z = this.mIsAnimating;
        Integer valueOf = Integer.valueOf(1000);
        String str = "animShrink";
        if (z) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(hashCode());
            iStateStyle = Folme.useValue(sb2.toString());
            animConfigArr = new AnimConfig[]{new AnimConfig().setEase(6, 200.0f).addListeners(this.mShrinkListener)};
        } else {
            this.mIsAnimating = true;
            this.mShrinkListener = new ShrinkAnimListener();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(hashCode());
            iStateStyle = Folme.useValue(sb3.toString()).setTo((Object) Integer.valueOf(0));
            animConfigArr = new AnimConfig[]{new AnimConfig().setEase(6, 200.0f).addListeners(this.mShrinkListener)};
        }
        iStateStyle.to(valueOf, animConfigArr);
    }

    private void drawBlurCover(GLCanvas gLCanvas, MiscTextureManager miscTextureManager) {
        String str;
        DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) getRenderAttri(101);
        int i = AnonymousClass2.$SwitchMap$com$android$camera$dualvideo$render$FaceType[getFaceType().ordinal()];
        if (i == 1) {
            str = MiscTextureManager.TEX_BACK_BLUR;
        } else if (i == 2) {
            str = MiscTextureManager.TEX_FRONT_BLUR;
        } else if (i != 3) {
            Log.e(TAG, "drawBlurCover: face type error!!");
            return;
        } else {
            str = MiscTextureManager.TEX_REMOTE_BLUR;
        }
        BasicTexture texture = miscTextureManager.getTexture(str);
        float[] fArr = (float[]) drawExtTexAttribute.mTextureTransform.clone();
        RenderUtil.flipCenter(fArr);
        DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(texture, drawExtTexAttribute.mX, drawExtTexAttribute.mY, drawExtTexAttribute.mWidth, drawExtTexAttribute.mHeight, fArr);
        gLCanvas.draw(drawBasicTexAttribute);
    }

    private void drawDarkCorner(GLCanvas gLCanvas, MiscTextureManager miscTextureManager) {
        if (getRenderLayoutType().isSelectWindowType()) {
            DrawRectShapeAttributeBase renderAttri = getRenderAttri(101);
            BasicTexture texture = miscTextureManager.getTexture(MiscTextureManager.TEX_DARK_CORNER_TOP);
            BasicTexture texture2 = miscTextureManager.getTexture(MiscTextureManager.TEX_DARK_CORNER_BOTTOM);
            DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(texture, renderAttri.mX, renderAttri.mY, renderAttri.mWidth, texture.getHeight());
            gLCanvas.draw(drawBasicTexAttribute);
            DrawBasicTexAttribute drawBasicTexAttribute2 = new DrawBasicTexAttribute(texture2, renderAttri.mX, (renderAttri.mY + renderAttri.mHeight) - texture2.getHeight(), renderAttri.mWidth, texture2.getHeight());
            gLCanvas.draw(drawBasicTexAttribute2);
        }
    }

    private void drawExpand(GLCanvas gLCanvas, MiscTextureManager miscTextureManager) {
        DrawRectShapeAttributeBase renderAttri = getRenderAttri(101);
        BasicTexture texture = miscTextureManager.getTexture(MiscTextureManager.TEX_EXPAND);
        DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(texture, renderAttri.mX + 10, renderAttri.mY + 10, texture.getWidth(), texture.getHeight());
        gLCanvas.draw(drawBasicTexAttribute);
    }

    @SuppressLint({"SwitchIntDef"})
    private void drawExpandAndShrinkHandler(GLCanvas gLCanvas, MiscTextureManager miscTextureManager) {
        if (!CameraSettings.getDualVideoConfig().ismDrawSelectWindow()) {
            int i = AnonymousClass2.$SwitchMap$com$android$camera$dualvideo$render$LayoutType[getRenderLayoutType().ordinal()];
            if (i == 1) {
                drawShrink(gLCanvas, miscTextureManager);
            } else if (i == 2 || i == 3 || i == 4 || i == 5) {
                drawExpand(gLCanvas, miscTextureManager);
            }
        }
    }

    @SuppressLint({"SwitchIntDef"})
    private void drawGridLine(GLCanvas gLCanvas) {
        float f;
        boolean z = DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_REFERENCE_LINE, false);
        if ((!isAnimating() || isPressedInSelectWindow()) && z) {
            DrawRectShapeAttributeBase renderAttri = getRenderAttri(101);
            int i = AnonymousClass2.$SwitchMap$com$android$camera$dualvideo$render$LayoutType[getRenderLayoutType().ordinal()];
            if (i != 1) {
                if (i != 6) {
                    f = 0.5f;
                }
            }
            RenderUtil.drawHorizontalLine(gLCanvas, renderAttri, 0.33333334f);
            RenderUtil.drawVerticalLine(gLCanvas, renderAttri, 0.33333334f);
            f = 0.6666667f;
            RenderUtil.drawHorizontalLine(gLCanvas, renderAttri, f);
            RenderUtil.drawVerticalLine(gLCanvas, renderAttri, f);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x003f, code lost:
        if (r9 != 270) goto L_0x00b3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void drawLabel(GLCanvas gLCanvas, MiscTextureManager miscTextureManager) {
        DrawBasicTexAttribute drawBasicTexAttribute;
        BasicTexture texture = miscTextureManager.getTexture(DualVideoConfigManager.instance().getConfigDescription(getSelectWindowLayoutType()));
        gLCanvas.getState().pushState();
        gLCanvas.getState().setAlpha(getSelectWindowFlagAlpha());
        gLCanvas.getState().setTexMatrix(miscTextureManager.getTexTransMatrix());
        int texOrientation = miscTextureManager.getTexOrientation();
        if (texOrientation != 0) {
            if (texOrientation != 90) {
                if (texOrientation != 180) {
                }
            }
            drawBasicTexAttribute = new DrawBasicTexAttribute(texture, Util.dpToPixel(7.27f) + getRenderAttri(101).mX, ((getRenderAttri(101).mY + getRenderAttri(101).mHeight) - texture.getWidth()) - Util.dpToPixel(7.27f), texture.getHeight(), texture.getWidth());
            gLCanvas.draw(drawBasicTexAttribute);
            gLCanvas.getState().popState();
        }
        drawBasicTexAttribute = new DrawBasicTexAttribute(texture, Util.dpToPixel(10.91f) + getRenderAttri(101).mX, ((getRenderAttri(101).mY + getRenderAttri(101).mHeight) - texture.getHeight()) - Util.dpToPixel(4.36f), texture.getWidth(), texture.getHeight());
        gLCanvas.draw(drawBasicTexAttribute);
        gLCanvas.getState().popState();
    }

    private void drawPreview(GLCanvas gLCanvas) {
        gLCanvas.draw((DrawExtTexAttribute) getRenderAttri(101));
    }

    private void drawSelectedFrame(GLCanvas gLCanvas, MiscTextureManager miscTextureManager) {
        String str;
        GLCanvas gLCanvas2 = gLCanvas;
        MiscTextureManager miscTextureManager2 = miscTextureManager;
        if (getSelectFrameAlpha() >= 0.01f) {
            gLCanvas.getState().setAlpha(getSelectFrameAlpha());
            DrawRectShapeAttributeBase renderAttri = getRenderAttri(101);
            DrawFillRectAttribute drawFillRectAttribute = new DrawFillRectAttribute(renderAttri.mX, renderAttri.mY, RenderUtil.SELECT_FRAME_LINE_WIDTH, renderAttri.mHeight, ColorConstant.COLOR_COMMON_SELECTED);
            gLCanvas2.draw(drawFillRectAttribute);
            int i = renderAttri.mX + renderAttri.mWidth;
            int i2 = RenderUtil.SELECT_FRAME_LINE_WIDTH;
            DrawFillRectAttribute drawFillRectAttribute2 = new DrawFillRectAttribute(i - i2, renderAttri.mY, i2, renderAttri.mHeight, ColorConstant.COLOR_COMMON_SELECTED);
            gLCanvas2.draw(drawFillRectAttribute2);
            String str2 = MiscTextureManager.TEX_SELECT_FRAME_TOP;
            DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(miscTextureManager2.getTexture(str2), renderAttri.mX, renderAttri.mY, renderAttri.mWidth, miscTextureManager2.getTexture(str2).getHeight());
            gLCanvas2.draw(drawBasicTexAttribute);
            String str3 = MiscTextureManager.TEX_SELECT_FRAME_BOTTOM;
            DrawBasicTexAttribute drawBasicTexAttribute2 = new DrawBasicTexAttribute(miscTextureManager2.getTexture(str3), renderAttri.mX, (renderAttri.mY + renderAttri.mHeight) - miscTextureManager2.getTexture(str3).getHeight(), renderAttri.mWidth, miscTextureManager2.getTexture(str3).getHeight());
            gLCanvas2.draw(drawBasicTexAttribute2);
            BasicTexture texture = miscTextureManager2.getTexture(MiscTextureManager.TEX_SELECTED_BG);
            DrawBasicTexAttribute drawBasicTexAttribute3 = new DrawBasicTexAttribute(texture, (renderAttri.mX + renderAttri.mWidth) - texture.getWidth(), (renderAttri.mY + renderAttri.mHeight) - texture.getHeight(), texture.getWidth(), texture.getHeight());
            gLCanvas2.draw(drawBasicTexAttribute3);
            int i3 = AnonymousClass2.$SwitchMap$com$android$camera$dualvideo$util$SelectIndex[getSelectedIndex().ordinal()];
            if (i3 == 1) {
                str = MiscTextureManager.TEX_SELECTED_FIRST;
            } else if (i3 == 2) {
                str = MiscTextureManager.TEX_SELECTED_SECOND;
            } else {
                return;
            }
            BasicTexture texture2 = miscTextureManager2.getTexture(str);
            gLCanvas.getState().pushState();
            gLCanvas.getState().setTexMatrix(miscTextureManager.gemAnimTexTransMatrix());
            DrawBasicTexAttribute drawBasicTexAttribute4 = new DrawBasicTexAttribute(texture2, (renderAttri.mX + renderAttri.mWidth) - texture2.getWidth(), (renderAttri.mY + renderAttri.mHeight) - texture2.getHeight(), texture2.getWidth(), texture2.getHeight());
            gLCanvas2.draw(drawBasicTexAttribute4);
            gLCanvas.getState().popState();
            gLCanvas.getState().setAlpha(1.0f);
        }
    }

    private void drawShrink(GLCanvas gLCanvas, MiscTextureManager miscTextureManager) {
        DrawRectShapeAttributeBase renderAttri = getRenderAttri(101);
        BasicTexture texture = miscTextureManager.getTexture(MiscTextureManager.TEX_SHRINK);
        DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(texture, renderAttri.mX + 10, renderAttri.mY + 10, texture.getWidth(), texture.getHeight());
        gLCanvas.draw(drawBasicTexAttribute);
    }

    private DrawExtTexAttribute genPreviewAttri(RegionHelper regionHelper) {
        Rect renderAreaFor = regionHelper.getRenderAreaFor(getRenderLayoutType());
        DrawExtTexAttribute drawExtTexAttribute = (DrawExtTexAttribute) getRenderAttri(101);
        DrawExtTexAttribute drawExtTexAttribute2 = new DrawExtTexAttribute(drawExtTexAttribute.mExtTexture, RenderUtil.generatePreviewTransMatrix(getFaceType(), getRenderLayoutType(), drawExtTexAttribute.mExtTexture, renderAreaFor), renderAreaFor.left, renderAreaFor.top, renderAreaFor.width(), renderAreaFor.height());
        return drawExtTexAttribute2;
    }

    private void restoreAnimShrink() {
        StringBuilder sb = new StringBuilder();
        sb.append("restoreAnimShrink: ");
        sb.append(hashCode());
        Log.d(TAG, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("animShrink");
        sb2.append(hashCode());
        Folme.useValue(sb2.toString()).to(Integer.valueOf(0), new AnimConfig().setEase(6, 200.0f).addListeners(this.mShrinkListener));
    }

    public void alphaInSelectWindowFlag(boolean z) {
        this.mSelectWindowFlagAnim = new EaseOutAnim(z ? 200 : 100);
        this.mSelectWindowFlagDstAlpha = z ? 1.0f : 0.0f;
    }

    public void alphaInSelectedFrame(boolean z) {
        if (getSelectedIndex() != SelectIndex.INDEX_0 || !z) {
            this.mSelectedFrameAnim = new EaseOutAnim(z ? 200 : 100);
            this.mSelectedFrameDstAlpha = z ? 1.0f : 0.0f;
        }
    }

    public void draw(GLCanvas gLCanvas, int i, MiscTextureManager miscTextureManager) {
        switch (i) {
            case 101:
                drawPreview(gLCanvas);
                return;
            case 102:
                drawBlurCover(gLCanvas, miscTextureManager);
                return;
            case 103:
                drawGridLine(gLCanvas);
                return;
            case 104:
                drawSelectedFrame(gLCanvas, miscTextureManager);
                return;
            case 105:
                drawLabel(gLCanvas, miscTextureManager);
                return;
            case 106:
                drawDarkCorner(gLCanvas, miscTextureManager);
                return;
            case 107:
                drawExpandAndShrinkHandler(gLCanvas, miscTextureManager);
                return;
            default:
                return;
        }
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public FaceType getFaceType() {
        return this.mFaceType;
    }

    @SuppressLint({"SwitchIntDef"})
    public Rect getHandleArea(MiscTextureManager miscTextureManager) {
        int i = AnonymousClass2.$SwitchMap$com$android$camera$dualvideo$render$LayoutType[getRenderLayoutType().ordinal()];
        if (i != 1 && i != 2 && i != 3 && i != 4 && i != 5) {
            return null;
        }
        DrawRectShapeAttributeBase renderAttri = getRenderAttri(101);
        BasicTexture texture = miscTextureManager.getTexture(MiscTextureManager.TEX_EXPAND);
        int i2 = renderAttri.mX;
        return new Rect(i2 + 10, renderAttri.mY + 10, i2 + texture.getWidth() + 10, renderAttri.mY + texture.getHeight() + 10);
    }

    public LayoutType getLastRenderLayoutType() {
        return this.mLastLayoutType;
    }

    public DrawRectShapeAttributeBase getRenderAttri(int i) {
        return (DrawRectShapeAttributeBase) this.mDrawAttriList.get(i);
    }

    public LayoutType getRenderLayoutType() {
        return this.mRenderLayoutType;
    }

    public float getSelectFrameAlpha() {
        EaseOutAnim easeOutAnim = this.mSelectedFrameAnim;
        if (easeOutAnim == null) {
            return this.mSelectedFrameAlpha;
        }
        if (easeOutAnim.isFinished()) {
            this.mSelectedFrameAlpha = this.mSelectedFrameDstAlpha;
            this.mSelectedFrameAnim = null;
            return this.mSelectedFrameAlpha;
        }
        float ratio = this.mSelectedFrameAnim.getRatio();
        return (this.mSelectedFrameAlpha * (1.0f - ratio)) + (this.mSelectedFrameDstAlpha * ratio);
    }

    public float getSelectWindowFlagAlpha() {
        EaseOutAnim easeOutAnim = this.mSelectWindowFlagAnim;
        if (easeOutAnim == null) {
            return this.mSelectWindowFlagAlpha;
        }
        if (easeOutAnim.isFinished()) {
            this.mSelectWindowFlagAlpha = this.mSelectWindowFlagDstAlpha;
            this.mSelectWindowFlagAnim = null;
            return this.mSelectWindowFlagAlpha;
        }
        float ratio = this.mSelectWindowFlagAnim.getRatio();
        return (this.mSelectWindowFlagAlpha * (1.0f - ratio)) + (this.mSelectWindowFlagDstAlpha * ratio);
    }

    public LayoutType getSelectWindowLayoutType() {
        return this.mSelectWindowLayoutType;
    }

    public SelectIndex getSelectedIndex() {
        return this.mSelectedType;
    }

    public boolean isAnimating() {
        return this.mIsAnimating || isPressedInSelectWindow();
    }

    public boolean isPressedInSelectWindow() {
        return this.mIsPressedInSelectWindow;
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public void onKeyDown() {
        if (!this.mIsAnimating || this.mIsPressedInSelectWindow) {
            this.mIsPressedInSelectWindow = true;
            animShrink();
            Log.d(TAG, "onKeyDown: ");
        }
    }

    public void onKeyUp() {
        if (this.mIsPressedInSelectWindow) {
            restoreAnimShrink();
        }
        Log.d(TAG, "onKeyUp: ");
    }

    public void setRenderAttri(DrawExtTexAttribute drawExtTexAttribute, int i) {
        this.mDrawAttriList.put(i, drawExtTexAttribute);
    }

    public void setRenderLayoutTypeWithAnim(LayoutType layoutType, RegionHelper regionHelper, boolean z) {
        if (!this.mIsAnimating) {
            LayoutType layoutType2 = this.mRenderLayoutType;
            if (layoutType != layoutType2) {
                this.mLastLayoutType = layoutType2;
                this.mRenderLayoutType = layoutType;
                StringBuilder sb = new StringBuilder();
                sb.append("setComposeTypeWithAnimation: from: ");
                sb.append(this.mLastLayoutType);
                sb.append(" to: ");
                sb.append(this.mRenderLayoutType);
                Log.d(TAG, sb.toString());
                AnimConfig animConfig = new AnimConfig();
                animConfig.setEase(-2, 0.9f, 0.3f);
                RenderTypeChangeAnimListener renderTypeChangeAnimListener = new RenderTypeChangeAnimListener(regionHelper);
                animConfig.addListeners(renderTypeChangeAnimListener);
                if (z) {
                    this.mIsAnimating = true;
                    Folme.useValue(new Object[0]).setTo((Object) Integer.valueOf(0)).to(Integer.valueOf(1000), animConfig);
                } else {
                    renderTypeChangeAnimListener.animatePreview(1000);
                }
            }
        }
    }

    public void setSelectTypeWithAnim(SelectIndex selectIndex, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("setSelectedTypeWithAnim: ");
        sb.append(selectIndex);
        Log.d(TAG, sb.toString());
        SelectIndex selectIndex2 = this.mSelectedType;
        if (selectIndex2 != selectIndex) {
            SelectIndex selectIndex3 = SelectIndex.INDEX_0;
            if (selectIndex2 == selectIndex3 || selectIndex == selectIndex3) {
                this.mSelectedType = selectIndex;
                SelectIndex selectIndex4 = this.mSelectedType;
                if (z) {
                    alphaInSelectedFrame(selectIndex4 != SelectIndex.INDEX_0);
                } else {
                    this.mSelectedFrameAlpha = selectIndex4 == SelectIndex.INDEX_0 ? 0.0f : 1.0f;
                }
                return;
            }
            this.mSelectedType = selectIndex;
        }
    }

    public void setVisibilityWithAnim(boolean z, boolean z2) {
        if (this.mIsVisible != z) {
            if (z2) {
                this.mIsVisible = true;
                int i = z ? 200 : 100;
                int i2 = 1000;
                int i3 = z ? 0 : 1000;
                if (!z) {
                    i2 = 0;
                }
                Folme.useValue(new Object[0]).setTo((Object) Integer.valueOf(i3)).to(Integer.valueOf(i2), new AnimConfig().setEase(6, (float) i).addListeners(new TransitionListener() {
                    public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
                        CameraItem.this.mAlpha = ((float) i) / 1000.0f;
                        if (z) {
                            CameraItem.this.mIsVisible = i == 1000;
                        }
                    }
                }));
            } else {
                this.mIsVisible = z;
                this.mAlpha = z ? 1.0f : 0.0f;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RenderItem{mLastPreviewComposeType=");
        sb.append(this.mLastLayoutType);
        sb.append(", mRenderComposeType=");
        sb.append(this.mRenderLayoutType);
        sb.append(", m6patchComposeType=");
        sb.append(this.mSelectWindowLayoutType);
        sb.append(", mFacing=");
        sb.append(this.mFaceType);
        sb.append(", mIsAnimating=");
        sb.append(this.mIsAnimating);
        sb.append(", mIsVisible=");
        sb.append(this.mIsVisible);
        sb.append(", mSelectedType=");
        sb.append(this.mSelectedType);
        sb.append('}');
        return sb.toString();
    }

    public void updateRenderAttri(RegionHelper regionHelper) {
        setRenderAttri(genPreviewAttri(regionHelper), 101);
    }
}
