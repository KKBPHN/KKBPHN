package com.android.camera.dualvideo.render;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Size;
import com.android.camera.CameraSettings;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.dualvideo.util.Assert;
import com.android.camera.dualvideo.util.DualVideoConfigManager;
import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import com.android.camera.dualvideo.util.RenderSourceType;
import com.android.camera.dualvideo.util.UserSelectData;
import com.android.camera.effect.FrameBuffer;
import com.android.camera.effect.draw_mode.DrawBlurTexAttribute;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.draw_mode.DrawFillRectAttribute;
import com.android.camera.effect.draw_mode.DrawRectShapeAttributeBase;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.gallery3d.ui.BasicTexture;
import com.android.gallery3d.ui.GLCanvas;
import com.android.gallery3d.ui.RawTexture;
import com.xiaomi.stat.d;
import java.util.Optional;

public class RenderUtil {
    public static final int ANIM_DURATION_100 = 100;
    public static final int ANIM_DURATION_200 = 200;
    public static final Size BACK_PREVIEW = new Size(1920, 1080);
    public static final Size FRONT_PREVIEW = new Size(1280, Util.LIMIT_SURFACE_WIDTH);
    public static final int GRID_LINE_COLOR = 1795162111;
    public static final int GRID_LINE_WIDTH = Util.dpToPixel(0.73f);
    public static final Size OUTPUT_SIZE = new Size(1080, 1920);
    public static final Size REMOTE_SIZE = new Size(Util.LIMIT_SURFACE_WIDTH, 1280);
    public static final int SELECT_FRAME_LINE_WIDTH = Util.dpToPixel(2.18f);
    public static final int SUB_CAMERA_SKIP_FRAMES = 3;

    /* renamed from: com.android.camera.dualvideo.render.RenderUtil$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$render$FaceType = new int[FaceType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_FRONT.ordinal()] = 1;
            $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_BACK.ordinal()] = 2;
            try {
                $SwitchMap$com$android$camera$dualvideo$render$FaceType[FaceType.FACE_REMOTE.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    static /* synthetic */ Float O000000o(float f, ConfigItem configItem) {
        return configItem.getFaceType() == FaceType.FACE_BACK ? Float.valueOf(configItem.mPresentZoom / f) : Float.valueOf(1.0f);
    }

    static /* synthetic */ boolean O000000o(LayoutType layoutType, ConfigItem configItem) {
        return configItem.mLayoutType == layoutType;
    }

    static /* synthetic */ boolean O00000Oo(LayoutType layoutType, UserSelectData userSelectData) {
        return userSelectData.getmSelectWindowLayoutType() == layoutType;
    }

    public static void blurTex(GLCanvas gLCanvas, RawTexture rawTexture) {
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            renderBlurTexture(gLCanvas, rawTexture);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("blur tex  cost time = ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append(d.H);
        Log.d("DualVideoUtil", sb.toString());
    }

    public static void centerCropHorizontal(float[] fArr, float f) {
        Matrix.translateM(fArr, 0, (1.0f - f) / 2.0f, 0.0f, 0.0f);
        Matrix.scaleM(fArr, 0, f, 1.0f, 1.0f);
    }

    public static void centerCropVertical(float[] fArr, float f) {
        Matrix.translateM(fArr, 0, 0.0f, (1.0f - f) / 2.0f, 0.0f);
        Matrix.scaleM(fArr, 0, 1.0f, f, 1.0f);
    }

    public static float[] centerScaleMatrix(float[] fArr, LayoutType layoutType, BasicTexture basicTexture, Rect rect) {
        float expandRatio = getExpandRatio(layoutType);
        if (expandRatio > 1.01f) {
            expandTex(fArr, expandRatio);
        }
        centerCropVertical(fArr, getCropRatio(basicTexture, rect));
        return fArr;
    }

    public static int compare(CameraItemInterface cameraItemInterface, CameraItemInterface cameraItemInterface2) {
        return Integer.compare((cameraItemInterface2.getRenderLayoutType().getWeight() * 6) + (cameraItemInterface2.getLastRenderLayoutType().getWeight() * 4), (cameraItemInterface.getRenderLayoutType().getWeight() * 6) + (cameraItemInterface.getLastRenderLayoutType().getWeight() * 4));
    }

    public static void copyPreviewTexture(GLCanvas gLCanvas, RawTexture rawTexture, DrawExtTexAttribute drawExtTexAttribute) {
        int width = rawTexture.getWidth();
        int height = rawTexture.getHeight();
        FrameBuffer frameBuffer = new FrameBuffer(gLCanvas, rawTexture, 0);
        gLCanvas.beginBindFrameBuffer(frameBuffer);
        DrawExtTexAttribute drawExtTexAttribute2 = new DrawExtTexAttribute(drawExtTexAttribute.mExtTexture, getIdentityMatrix(), 0, 0, width, height);
        gLCanvas.draw(drawExtTexAttribute2);
        GLES20.glFinish();
        gLCanvas.endBindFrameBuffer();
        frameBuffer.delete();
    }

    public static void drawHorizontalLine(GLCanvas gLCanvas, DrawRectShapeAttributeBase drawRectShapeAttributeBase, float f) {
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        int i = drawRectShapeAttributeBase.mX;
        float f2 = ((float) drawRectShapeAttributeBase.mY) + (((float) drawRectShapeAttributeBase.mHeight) * f);
        int i2 = GRID_LINE_WIDTH;
        DrawFillRectAttribute drawFillRectAttribute = new DrawFillRectAttribute(i, (int) (f2 - (((float) i2) / 2.0f)), drawRectShapeAttributeBase.mWidth, i2, GRID_LINE_COLOR);
        gLCanvas.draw(drawFillRectAttribute);
        GLES20.glDisable(3042);
    }

    public static void drawVerticalLine(GLCanvas gLCanvas, DrawRectShapeAttributeBase drawRectShapeAttributeBase, float f) {
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        float f2 = ((float) drawRectShapeAttributeBase.mX) + (((float) drawRectShapeAttributeBase.mWidth) * f);
        int i = GRID_LINE_WIDTH;
        DrawFillRectAttribute drawFillRectAttribute = new DrawFillRectAttribute((int) (f2 - ((float) i)), drawRectShapeAttributeBase.mY, i, drawRectShapeAttributeBase.mHeight, GRID_LINE_COLOR);
        gLCanvas.draw(drawFillRectAttribute);
        GLES20.glDisable(3042);
    }

    public static void enableStencilTest(int i) {
        int i2;
        if (i == 1) {
            GLES20.glEnable(2960);
            GLES20.glClearStencil(0);
            GLES20.glClear(1024);
            GLES20.glStencilFunc(512, 0, 255);
            i2 = 7682;
        } else if (i == 2) {
            GLES20.glStencilFunc(514, 1, 255);
            i2 = 7680;
        } else {
            GLES20.glDisable(2960);
            return;
        }
        GLES20.glStencilOp(i2, i2, i2);
    }

    public static void expandTex(float[] fArr, float f) {
        float f2 = 1.0f / f;
        float f3 = (1.0f - f2) / 2.0f;
        Matrix.translateM(fArr, 0, f3, f3, 0.0f);
        Matrix.scaleM(fArr, 0, f2, f2, 1.0f);
    }

    public static void flipCenter(float[] fArr) {
        Matrix.translateM(fArr, 0, 1.0f, 1.0f, 0.0f);
        Matrix.rotateM(fArr, 0, 180.0f, 0.0f, 0.0f, 1.0f);
    }

    public static float[] genNotCenterCropTransMatrix(float f, float f2) {
        float[] fArr = new float[16];
        Matrix.setIdentityM(fArr, 0);
        Matrix.translateM(fArr, 0, 0.0f, f2, 0.0f);
        Matrix.scaleM(fArr, 0, 1.0f, f, 1.0f);
        return fArr;
    }

    public static float[] generatePreviewTransMatrix(FaceType faceType, LayoutType layoutType, BasicTexture basicTexture, Rect rect) {
        float[] fArr = new float[16];
        Matrix.setIdentityM(fArr, 0);
        int i = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$render$FaceType[faceType.ordinal()];
        if (i == 1) {
            Matrix.translateM(fArr, 0, 0.5f, 0.5f, 0.0f);
            Matrix.rotateM(fArr, 0, (float) getFrontCameraRotation(), 0.0f, 0.0f, 1.0f);
        } else if (i != 2) {
            if (i == 3) {
                Matrix.translateM(fArr, 0, 0.5f, 0.5f, 0.0f);
                Matrix.scaleM(fArr, 0, 1.0f, -1.0f, 1.0f);
            }
            centerScaleMatrix(fArr, layoutType, basicTexture, rect);
            return fArr;
        } else {
            Matrix.translateM(fArr, 0, 0.5f, 0.5f, 0.0f);
            Matrix.rotateM(fArr, 0, 90.0f, 0.0f, 0.0f, 1.0f);
            Matrix.scaleM(fArr, 0, -1.0f, 1.0f, 1.0f);
        }
        Matrix.translateM(fArr, 0, -0.5f, -0.5f, 0.0f);
        centerScaleMatrix(fArr, layoutType, basicTexture, rect);
        return fArr;
    }

    public static Bitmap getBitmap(Resources resources, int i) {
        Drawable drawable = resources.getDrawable(i, null);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(resources, i);
        }
        if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        }
        throw new IllegalArgumentException("unsupported drawable type");
    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap createBitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        createBitmap.setPremultiplied(true);
        return createBitmap;
    }

    public static float getCropRatio(BasicTexture basicTexture, Rect rect) {
        float f;
        int i;
        float height = ((float) rect.height()) / ((float) rect.width());
        if (basicTexture.getWidth() > basicTexture.getHeight()) {
            f = (float) basicTexture.getWidth();
            i = basicTexture.getHeight();
        } else {
            f = (float) basicTexture.getHeight();
            i = basicTexture.getWidth();
        }
        float f2 = f / ((float) i);
        if (height > f2 - 0.01f) {
            return 1.0f;
        }
        return height / f2;
    }

    public static float getExpandRatio(LayoutType layoutType) {
        return ((Float) DualVideoConfigManager.instance().getConfigs().stream().filter(new C0226O000oOoO(layoutType)).findFirst().map(new C0225O000oOo0(DualVideoConfigManager.instance().getMinZoom())).orElse(Float.valueOf(1.0f))).floatValue();
    }

    private static int getFrontCameraRotation() {
        Camera2DataContainer instance = Camera2DataContainer.getInstance();
        int sensorOrientation = instance.getCapabilities(instance.getFrontCameraId()).getSensorOrientation();
        StringBuilder sb = new StringBuilder();
        sb.append("orientation:");
        sb.append(sensorOrientation);
        android.util.Log.d("RenderUtil", sb.toString());
        return sensorOrientation;
    }

    private static float[] getIdentityMatrix() {
        float[] fArr = new float[16];
        Matrix.setIdentityM(fArr, 0);
        return fArr;
    }

    public static LayoutType getRecordType(LayoutType layoutType) {
        Optional map = CameraSettings.getDualVideoConfig().getSelectedData().stream().filter(new C0224O000oOo(layoutType)).findAny().map(C0227O000oOoo.INSTANCE);
        Assert.check(map.isPresent());
        return (LayoutType) map.get();
    }

    public static Size getSubPreviewSize() {
        return Camera2DataContainer.getInstance().isFrontCameraId(((Integer) DataRepository.dataItemRunning().getComponentRunningDualVideo().getIds().get(RenderSourceType.SUB)).intValue()) ? FRONT_PREVIEW : BACK_PREVIEW;
    }

    private static TextPaint getTextPaint(int i) {
        TextPaint textPaint = new TextPaint(1);
        textPaint.setTextSize(36.0f);
        textPaint.setAntiAlias(true);
        textPaint.setColor(i);
        textPaint.setTypeface(Typeface.create("mipro-regular", 0));
        textPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        textPaint.setStrokeWidth(1.0f);
        textPaint.setStyle(Style.FILL_AND_STROKE);
        return textPaint;
    }

    public static Rect mixArea(Rect rect, Rect rect2, float f) {
        float f2 = 1.0f - f;
        return new Rect((int) ((((float) rect.left) * f2) + (((float) rect2.left) * f)), (int) ((((float) rect.top) * f2) + (((float) rect2.top) * f)), (int) ((((float) rect.right) * f2) + (((float) rect2.right) * f)), (int) ((((float) rect.bottom) * f2) + (((float) rect2.bottom) * f)));
    }

    private static void renderBlurTexture(GLCanvas gLCanvas, RawTexture rawTexture) {
        int width = rawTexture.getWidth();
        int height = rawTexture.getHeight();
        FrameBuffer frameBuffer = new FrameBuffer(gLCanvas, rawTexture, 0);
        gLCanvas.prepareBlurRenders();
        gLCanvas.beginBindFrameBuffer(frameBuffer);
        DrawBlurTexAttribute drawBlurTexAttribute = new DrawBlurTexAttribute(rawTexture, 0, 0, width, height);
        gLCanvas.draw(drawBlurTexAttribute);
        GLES20.glFinish();
        gLCanvas.endBindFrameBuffer();
        frameBuffer.delete();
    }

    public static Rect shrinkRect(Rect rect, float f) {
        float width = (((float) rect.width()) * f) / 2.0f;
        float height = (((float) rect.height()) * f) / 2.0f;
        float exactCenterX = rect.exactCenterX();
        float exactCenterY = rect.exactCenterY();
        return new Rect((int) (exactCenterX - width), (int) (exactCenterY - height), (int) (exactCenterX + width), (int) (exactCenterY + height));
    }

    public static Bitmap textAsBitmap(String str, int i) {
        TextPaint textPaint = getTextPaint(i);
        textPaint.setAntiAlias(true);
        StaticLayout staticLayout = new StaticLayout(str, textPaint, (int) textPaint.measureText(str), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        Bitmap createBitmap = Bitmap.createBitmap((int) textPaint.measureText(str), Util.dpToPixel(17.45f), Config.ARGB_8888);
        staticLayout.draw(new Canvas(createBitmap));
        return createBitmap;
    }
}
