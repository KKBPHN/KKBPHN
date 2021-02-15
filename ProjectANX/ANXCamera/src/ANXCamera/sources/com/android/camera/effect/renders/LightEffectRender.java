package com.android.camera.effect.renders;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.opengl.GLES20;
import com.android.camera.CameraAppImpl;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.ShaderUtil;
import com.android.camera.effect.draw_mode.DrawAttribute;
import com.android.camera.effect.draw_mode.DrawBasicTexAttribute;
import com.android.camera.effect.draw_mode.DrawIntTexAttribute;
import com.android.camera.log.Log;
import com.android.camera.sticker.glutils.GlUtil;
import com.android.gallery3d.ui.GLCanvas;
import java.io.IOException;
import java.io.InputStream;

public class LightEffectRender extends PixelEffectRender {
    private static final int FILTER_TYPE_BRIGHT_RED = 49;
    private static final int FILTER_TYPE_DAZZLING = 47;
    private static final int FILTER_TYPE_DREAMLAND = 50;
    private static final int FILTER_TYPE_GORGEOUS = 48;
    private static final int FILTER_TYPE_LANSHAN = 46;
    private static final int FILTER_TYPE_NEON = 42;
    private static final int FILTER_TYPE_NOSTALGIA = 44;
    private static final int FILTER_TYPE_PHANTOM = 43;
    private static final int FILTER_TYPE_RAINBOW = 45;
    private static final String TAG = "LightEffectRender";
    private final String ASSERT_FORMAT = ".webp";
    private Bitmap mDarkBitmap;
    private int mDarkTexture = 0;
    private int mFilterTexture = 0;
    private boolean mIsSnapshot = false;
    private int mLastRotation = -1;
    private Bitmap mLightBitmap;
    private int mLightTexture = 0;
    private boolean mNeedBlur = false;
    private boolean mNeedColorSeparation = false;
    private boolean mNeedFilter = false;
    private boolean mNeedNoise = false;
    private boolean mNeedUpdateTexture = false;
    private Bitmap mNoiseBitmap;
    private int mNoiseTexture = 0;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private int mUniformDarkTexture;
    private int mUniformFilterTexture;
    private int mUniformLightTexture;
    private int mUniformNeedBlur;
    private int mUniformNeedColorSeparation;
    private int mUniformNeedFilter;
    private int mUniformNeedNoise;
    private int mUniformNoiseTexture;
    private int mUniformVerticalScreen;
    private boolean mVerticalScreen = true;

    class CropOperation {
        Matrix matrix;
        Rect rect;

        private CropOperation() {
        }
    }

    public @interface LightEffectId {
    }

    public LightEffectRender(GLCanvas gLCanvas, @LightEffectId int i) {
        super(gLCanvas);
        prepareLightEffect(i);
    }

    private Bitmap getBitmapFromAssert(String str) {
        try {
            InputStream open = CameraAppImpl.getAndroidContext().getAssets().open(str);
            if (open.available() == 0) {
                return null;
            }
            Options options = new Options();
            options.inPreferredConfig = Config.ARGB_8888;
            Bitmap decodeStream = BitmapFactory.decodeStream(open, null, options);
            open.close();
            return decodeStream;
        } catch (IOException unused) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("get assert failed, path:");
            sb.append(str);
            Log.e(str2, sb.toString());
            return null;
        }
    }

    private CropOperation getCropOperationForCapture(int i) {
        int i2;
        float f;
        int i3;
        Matrix matrix = null;
        CropOperation cropOperation = new CropOperation();
        int width = this.mLightBitmap.getWidth();
        int height = this.mLightBitmap.getHeight();
        int i4 = (i + 90) % m.cQ;
        int i5 = 0;
        if (i4 != 0) {
            if (i4 == 90) {
                width = (this.mPreviewHeight * height) / this.mPreviewWidth;
                i3 = (this.mLightBitmap.getWidth() - width) / 2;
                matrix = new Matrix();
                f = 270.0f;
            } else if (i4 == 180) {
                height = (this.mPreviewHeight * width) / this.mPreviewWidth;
                i2 = (this.mLightBitmap.getHeight() - height) / 2;
                matrix = new Matrix();
                matrix.setRotate(180.0f);
            } else if (i4 != 270) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unknown degree:");
                sb.append(i4);
                Log.w(str, sb.toString());
                i2 = 0;
            } else {
                width = (this.mPreviewHeight * height) / this.mPreviewWidth;
                i3 = (this.mLightBitmap.getWidth() - width) / 2;
                matrix = new Matrix();
                f = 90.0f;
            }
            matrix.setRotate(f);
            i5 = i3;
            i2 = 0;
        } else {
            height = (this.mPreviewHeight * width) / this.mPreviewWidth;
            i2 = (this.mLightBitmap.getHeight() - height) / 2;
        }
        cropOperation.rect = new Rect(i5, i2, width, height);
        cropOperation.matrix = matrix;
        return cropOperation;
    }

    private CropOperation getCropOperationForPreview(int i) {
        int i2;
        int i3;
        float f;
        Matrix matrix;
        Matrix matrix2 = null;
        CropOperation cropOperation = new CropOperation();
        int width = this.mLightBitmap.getWidth();
        int height = this.mLightBitmap.getHeight();
        int i4 = 0;
        if (i != 0) {
            if (i == 90) {
                height = (this.mPreviewWidth * width) / this.mPreviewHeight;
                i2 = (this.mLightBitmap.getHeight() - height) / 2;
                matrix = new Matrix();
                f = 270.0f;
            } else if (i == 180) {
                width = (this.mPreviewWidth * height) / this.mPreviewHeight;
                i3 = (this.mLightBitmap.getWidth() - width) / 2;
                matrix2 = new Matrix();
                matrix2.setRotate(180.0f);
            } else if (i != 270) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unknown degree:");
                sb.append(i);
                Log.w(str, sb.toString());
                i2 = 0;
                cropOperation.rect = new Rect(i4, i2, width, height);
                cropOperation.matrix = matrix2;
                return cropOperation;
            } else {
                height = (this.mPreviewWidth * width) / this.mPreviewHeight;
                i2 = (this.mLightBitmap.getHeight() - height) / 2;
                matrix = new Matrix();
                f = 90.0f;
            }
            matrix2.setRotate(f);
            cropOperation.rect = new Rect(i4, i2, width, height);
            cropOperation.matrix = matrix2;
            return cropOperation;
        }
        width = (this.mPreviewWidth * height) / this.mPreviewHeight;
        i3 = (this.mLightBitmap.getWidth() - width) / 2;
        i4 = i3;
        i2 = 0;
        cropOperation.rect = new Rect(i4, i2, width, height);
        cropOperation.matrix = matrix2;
        return cropOperation;
    }

    private void prepareLightEffect(@LightEffectId int i) {
        String str;
        String str2 = "";
        switch (i) {
            case 42:
                str = "effect/light_effect/NEON";
                break;
            case 43:
                str = "effect/light_effect/PHANTOM";
                break;
            case 44:
                this.mNeedNoise = true;
                str = "effect/light_effect/NOSTALGIA";
                str2 = "effect/light_effect/NOSTALGIA_NOISE";
                break;
            case 45:
                str = "effect/light_effect/RAINBOW";
                break;
            case 46:
                str = "effect/light_effect/LANSHAN";
                break;
            case 47:
                str = "effect/light_effect/DAZZLING";
                break;
            case 48:
                str = "effect/light_effect/GORGEOUS";
                break;
            case 49:
                str = "effect/light_effect/BRIGHT_RED";
                break;
            case 50:
                str = "effect/light_effect/DREAMLAND";
                break;
            default:
                str = str2;
                break;
        }
        String str3 = ".webp";
        if (!str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(str3);
            this.mLightBitmap = getBitmapFromAssert(sb.toString());
        }
        if (!str2.isEmpty()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(str3);
            this.mNoiseBitmap = getBitmapFromAssert(sb2.toString());
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("effect/light_effect/DARK");
        sb3.append(str3);
        this.mDarkBitmap = getBitmapFromAssert(sb3.toString());
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("prepareLightEffect: ");
        sb4.append(i);
        Log.d(str4, sb4.toString());
    }

    private void safeDeleteTexture(int i) {
        GLES20.glDeleteTextures(1, new int[]{i}, 0);
    }

    private void updateLightTexture(int i) {
        CropOperation cropOperationForCapture = this.mIsSnapshot ? getCropOperationForCapture(i) : getCropOperationForPreview(i);
        Bitmap bitmap = this.mLightBitmap;
        if (bitmap != null) {
            Rect rect = cropOperationForCapture.rect;
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.right, rect.bottom, cropOperationForCapture.matrix, false);
            int i2 = this.mLightTexture;
            if (i2 != 0) {
                safeDeleteTexture(i2);
            }
            this.mLightTexture = GlUtil.createTexture(3553, createBitmap);
        }
        if (this.mNeedNoise) {
            Bitmap bitmap2 = this.mNoiseBitmap;
            if (bitmap2 != null) {
                Rect rect2 = cropOperationForCapture.rect;
                Bitmap createBitmap2 = Bitmap.createBitmap(bitmap2, rect2.left, rect2.top, rect2.right, rect2.bottom, cropOperationForCapture.matrix, false);
                int i3 = this.mNoiseTexture;
                if (i3 != 0) {
                    safeDeleteTexture(i3);
                }
                this.mNoiseTexture = GlUtil.createTexture(3553, createBitmap2);
            }
        }
        Bitmap bitmap3 = this.mDarkBitmap;
        if (bitmap3 != null && this.mDarkTexture == 0) {
            this.mDarkTexture = GlUtil.createTexture(3553, bitmap3);
        }
        this.mLastRotation = i;
        Log.d(TAG, String.format("updateLightTexture rotation(%d) bitmap(%d,%d,%d,%d) preview(%d,%d) mIsSnapshot(%b)", new Object[]{Integer.valueOf(i), Integer.valueOf(cropOperationForCapture.rect.left), Integer.valueOf(cropOperationForCapture.rect.top), Integer.valueOf(cropOperationForCapture.rect.right), Integer.valueOf(cropOperationForCapture.rect.bottom), Integer.valueOf(this.mPreviewWidth), Integer.valueOf(this.mPreviewHeight), Boolean.valueOf(this.mIsSnapshot)}));
    }

    public void destroy() {
        int i = this.mLightTexture;
        if (i != 0) {
            safeDeleteTexture(i);
            this.mLightTexture = 0;
        }
        int i2 = this.mNoiseTexture;
        if (i2 != 0) {
            safeDeleteTexture(i2);
            this.mNoiseTexture = 0;
        }
        int i3 = this.mDarkTexture;
        if (i3 != 0) {
            safeDeleteTexture(i3);
            this.mDarkTexture = 0;
        }
        Bitmap bitmap = this.mLightBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.mLightBitmap = null;
        }
        Bitmap bitmap2 = this.mNoiseBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.mNoiseBitmap = null;
        }
        Bitmap bitmap3 = this.mDarkBitmap;
        if (bitmap3 != null) {
            bitmap3.recycle();
            this.mDarkBitmap = null;
        }
        super.destroy();
    }

    public boolean draw(DrawAttribute drawAttribute) {
        boolean z;
        if (!isAttriSupported(drawAttribute.getTarget())) {
            return false;
        }
        int orientation = EffectController.getInstance().getOrientation();
        int target = drawAttribute.getTarget();
        if (target == 5) {
            DrawBasicTexAttribute drawBasicTexAttribute = (DrawBasicTexAttribute) drawAttribute;
            this.mFilterTexture = drawBasicTexAttribute.mBasicTexture.getId();
            z = drawBasicTexAttribute.mIsSnapshot;
        } else if (target != 6) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("attr unsupported, target:");
            sb.append(drawAttribute.getTarget());
            Log.w(str, sb.toString());
            if (this.mNeedUpdateTexture || this.mLastRotation != orientation) {
                updateLightTexture(orientation);
                this.mNeedUpdateTexture = false;
            }
            return super.draw(drawAttribute);
        } else {
            DrawIntTexAttribute drawIntTexAttribute = (DrawIntTexAttribute) drawAttribute;
            this.mFilterTexture = drawIntTexAttribute.mTexId;
            z = drawIntTexAttribute.mIsSnapshot;
        }
        this.mIsSnapshot = z;
        updateLightTexture(orientation);
        this.mNeedUpdateTexture = false;
        return super.draw(drawAttribute);
    }

    public String getFragShaderString() {
        return ShaderUtil.loadFromAssetsFile("frag_light.c");
    }

    /* access modifiers changed from: protected */
    public void initShader() {
        super.initShader();
        this.mUniformFilterTexture = GLES20.glGetUniformLocation(this.mProgram, "text_filter");
        this.mUniformLightTexture = GLES20.glGetUniformLocation(this.mProgram, "text_light");
        this.mUniformNoiseTexture = GLES20.glGetUniformLocation(this.mProgram, "text_noise");
        this.mUniformDarkTexture = GLES20.glGetUniformLocation(this.mProgram, "text_dark");
        this.mUniformNeedFilter = GLES20.glGetUniformLocation(this.mProgram, "needFilter");
        this.mUniformNeedNoise = GLES20.glGetUniformLocation(this.mProgram, "needNoise");
        this.mUniformNeedBlur = GLES20.glGetUniformLocation(this.mProgram, "needBlur");
        this.mUniformNeedColorSeparation = GLES20.glGetUniformLocation(this.mProgram, "needColorSeparation");
        this.mUniformVerticalScreen = GLES20.glGetUniformLocation(this.mProgram, "verticalScreen");
    }

    /* access modifiers changed from: protected */
    public void initShaderValue(boolean z) {
        super.initShaderValue(z);
        int i = this.mFilterTexture;
        if (i != 0) {
            bindTexture(i, 33985);
            GLES20.glUniform1i(this.mUniformFilterTexture, 1);
        }
        int i2 = this.mLightTexture;
        if (i2 != 0) {
            bindTexture(i2, 33986);
            GLES20.glUniform1i(this.mUniformLightTexture, 2);
        }
        int i3 = this.mNoiseTexture;
        if (i3 != 0) {
            bindTexture(i3, 33987);
            GLES20.glUniform1i(this.mUniformNoiseTexture, 3);
        }
        int i4 = this.mDarkTexture;
        if (i4 != 0) {
            bindTexture(i4, 33988);
            GLES20.glUniform1i(this.mUniformDarkTexture, 4);
        }
        GLES20.glUniform1i(this.mUniformNeedFilter, this.mNeedFilter ? 1 : 0);
        GLES20.glUniform1i(this.mUniformNeedNoise, this.mNeedNoise ? 1 : 0);
        GLES20.glUniform1i(this.mUniformNeedBlur, this.mNeedBlur ? 1 : 0);
        GLES20.glUniform1i(this.mUniformNeedColorSeparation, this.mNeedColorSeparation ? 1 : 0);
        GLES20.glUniform1i(this.mUniformVerticalScreen, this.mVerticalScreen ? 1 : 0);
    }

    public void setPreviewSize(int i, int i2) {
        super.setPreviewSize(i, i2);
        if (this.mPreviewWidth != i || this.mPreviewHeight != i2) {
            this.mNeedUpdateTexture = this.mPreviewWidth * i != this.mPreviewHeight * i2;
            this.mPreviewWidth = i;
            this.mPreviewHeight = i2;
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Preview size change w=");
            sb.append(i);
            sb.append(" h=");
            sb.append(i2);
            Log.d(str, sb.toString());
        }
    }
}
