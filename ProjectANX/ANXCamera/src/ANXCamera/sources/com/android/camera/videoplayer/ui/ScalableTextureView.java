package com.android.camera.videoplayer.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.TextureView;

public abstract class ScalableTextureView extends TextureView {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "ScalableTextureView";
    private Integer mContentHeight;
    private float mContentRotation = 0.0f;
    private float mContentScaleMultiplier = 1.0f;
    private float mContentScaleX = 1.0f;
    private float mContentScaleY = 1.0f;
    private Integer mContentWidth;
    private int mContentX = 0;
    private int mContentY = 0;
    private float mPivotPointX = 0.0f;
    private float mPivotPointY = 0.0f;
    private ScaleType mScaleType;
    private final Matrix mTransformMatrix = new Matrix();

    /* renamed from: com.android.camera.videoplayer.ui.ScalableTextureView$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType = new int[ScaleType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[ScaleType.FILL.ordinal()] = 1;
            $SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[ScaleType.BOTTOM.ordinal()] = 2;
            $SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 3;
            try {
                $SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[ScaleType.TOP.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public enum ScaleType {
        CENTER_CROP,
        TOP,
        BOTTOM,
        FILL
    }

    public ScalableTextureView(Context context) {
        super(context);
    }

    public ScalableTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ScalableTextureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ScalableTextureView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    private void updateMatrixScaleRotate() {
        this.mTransformMatrix.reset();
        Matrix matrix = this.mTransformMatrix;
        float f = this.mContentScaleX;
        float f2 = this.mContentScaleMultiplier;
        matrix.setScale(f * f2, this.mContentScaleY * f2, this.mPivotPointX, this.mPivotPointY);
        this.mTransformMatrix.postRotate(this.mContentRotation, this.mPivotPointX, this.mPivotPointY);
        setTransform(this.mTransformMatrix);
    }

    private void updateMatrixTranslate() {
        float f = this.mContentScaleX;
        float f2 = this.mContentScaleMultiplier;
        float f3 = f * f2;
        float f4 = this.mContentScaleY * f2;
        this.mTransformMatrix.reset();
        this.mTransformMatrix.setScale(f3, f4, this.mPivotPointX, this.mPivotPointY);
        this.mTransformMatrix.postTranslate((float) this.mContentX, (float) this.mContentY);
        setTransform(this.mTransformMatrix);
    }

    public void centralizeContent() {
        getMeasuredWidth();
        getMeasuredHeight();
        getScaledContentWidth().intValue();
        getScaledContentHeight().intValue();
        this.mContentX = 0;
        this.mContentY = 0;
        updateMatrixScaleRotate();
    }

    public float getContentAspectRatio() {
        Integer num = this.mContentWidth;
        if (num == null || this.mContentHeight == null) {
            return 0.0f;
        }
        return ((float) num.intValue()) / ((float) this.mContentHeight.intValue());
    }

    /* access modifiers changed from: protected */
    public final Integer getContentHeight() {
        return this.mContentHeight;
    }

    public float getContentScale() {
        return this.mContentScaleMultiplier;
    }

    /* access modifiers changed from: protected */
    public final Integer getContentWidth() {
        return this.mContentWidth;
    }

    /* access modifiers changed from: protected */
    public final float getContentX() {
        return (float) this.mContentX;
    }

    /* access modifiers changed from: protected */
    public final float getContentY() {
        return (float) this.mContentY;
    }

    public float getPivotX() {
        return this.mPivotPointX;
    }

    public float getPivotY() {
        return this.mPivotPointY;
    }

    public float getRotation() {
        return this.mContentRotation;
    }

    public Integer getScaledContentHeight() {
        return Integer.valueOf((int) (this.mContentScaleY * this.mContentScaleMultiplier * ((float) getMeasuredHeight())));
    }

    public Integer getScaledContentWidth() {
        return Integer.valueOf((int) (this.mContentScaleX * this.mContentScaleMultiplier * ((float) getMeasuredWidth())));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mContentWidth != null && this.mContentHeight != null) {
            updateTextureViewSize();
        }
    }

    /* access modifiers changed from: protected */
    public final void setContentHeight(int i) {
        this.mContentHeight = Integer.valueOf(i);
    }

    public void setContentScale(float f) {
        this.mContentScaleMultiplier = f;
        updateMatrixScaleRotate();
    }

    /* access modifiers changed from: protected */
    public final void setContentWidth(int i) {
        this.mContentWidth = Integer.valueOf(i);
    }

    public final void setContentX(float f) {
        this.mContentX = ((int) f) - ((getMeasuredWidth() - getScaledContentWidth().intValue()) / 2);
        updateMatrixTranslate();
    }

    public final void setContentY(float f) {
        this.mContentY = ((int) f) - ((getMeasuredHeight() - getScaledContentHeight().intValue()) / 2);
        updateMatrixTranslate();
    }

    public void setPivotX(float f) {
        this.mPivotPointX = f;
    }

    public void setPivotY(float f) {
        this.mPivotPointY = f;
    }

    public void setRotation(float f) {
        this.mContentRotation = f;
        updateMatrixScaleRotate();
    }

    public void setScaleType(ScaleType scaleType) {
        this.mScaleType = scaleType;
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00e5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateTextureViewSize() {
        float f;
        float f2;
        int i;
        float f3;
        float f4;
        if (this.mContentWidth == null || this.mContentHeight == null) {
            throw new RuntimeException("null content size");
        }
        float measuredWidth = (float) getMeasuredWidth();
        float measuredHeight = (float) getMeasuredHeight();
        float intValue = (float) this.mContentWidth.intValue();
        float intValue2 = (float) this.mContentHeight.intValue();
        int i2 = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
        float f5 = 1.0f;
        if (i2 != 1) {
            if (i2 == 2 || i2 == 3 || i2 == 4) {
                if (intValue > measuredWidth && intValue2 > measuredHeight) {
                    float f6 = intValue2 / measuredHeight;
                    f = intValue / measuredWidth;
                    f2 = f6;
                    i = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
                    float f7 = 0.0f;
                    if (i != 1) {
                    }
                    int i3 = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
                    if (this.mContentHeight.intValue() <= this.mContentWidth.intValue()) {
                    }
                    this.mContentScaleX = f * f5;
                    this.mContentScaleY = f5 * f2;
                    this.mPivotPointX = f7;
                    this.mPivotPointY = f3;
                    updateMatrixScaleRotate();
                } else if (intValue < measuredWidth && intValue2 < measuredHeight) {
                    f2 = measuredWidth / intValue;
                    f = measuredHeight / intValue2;
                    i = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
                    float f72 = 0.0f;
                    if (i != 1) {
                    }
                    int i32 = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
                    if (this.mContentHeight.intValue() <= this.mContentWidth.intValue()) {
                    }
                    this.mContentScaleX = f * f5;
                    this.mContentScaleY = f5 * f2;
                    this.mPivotPointX = f72;
                    this.mPivotPointY = f3;
                    updateMatrixScaleRotate();
                } else if (measuredWidth > intValue) {
                    f2 = (measuredWidth / intValue) / (measuredHeight / intValue2);
                    f = 1.0f;
                    i = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
                    float f722 = 0.0f;
                    if (i != 1) {
                        f722 = this.mPivotPointX;
                        f3 = this.mPivotPointY;
                    } else if (i == 2) {
                        f722 = measuredWidth;
                        f3 = measuredHeight;
                    } else if (i == 3) {
                        f722 = measuredWidth / 2.0f;
                        f3 = measuredHeight / 2.0f;
                    } else if (i == 4) {
                        f3 = 0.0f;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("pivotPointX, pivotPointY for ScaleType ");
                        sb.append(this.mScaleType);
                        sb.append(" are not defined");
                        throw new IllegalStateException(sb.toString());
                    }
                    int i322 = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
                    if (i322 != 1 && (i322 == 2 || i322 == 3 || i322 == 4)) {
                        f5 = this.mContentHeight.intValue() <= this.mContentWidth.intValue() ? measuredWidth / (measuredWidth * f) : measuredHeight / (measuredHeight * f2);
                    }
                    this.mContentScaleX = f * f5;
                    this.mContentScaleY = f5 * f2;
                    this.mPivotPointX = f722;
                    this.mPivotPointY = f3;
                    updateMatrixScaleRotate();
                } else if (measuredHeight > intValue2) {
                    f4 = (measuredHeight / intValue2) / (measuredWidth / intValue);
                }
            }
            f2 = 1.0f;
            f = 1.0f;
            i = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
            float f7222 = 0.0f;
            if (i != 1) {
            }
            int i3222 = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
            if (this.mContentHeight.intValue() <= this.mContentWidth.intValue()) {
            }
            this.mContentScaleX = f * f5;
            this.mContentScaleY = f5 * f2;
            this.mPivotPointX = f7222;
            this.mPivotPointY = f3;
            updateMatrixScaleRotate();
        } else if (measuredWidth > measuredHeight) {
            f4 = (intValue * measuredHeight) / (intValue2 * measuredWidth);
        } else {
            f2 = (intValue2 * measuredWidth) / (intValue * measuredHeight);
            f = 1.0f;
            i = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
            float f72222 = 0.0f;
            if (i != 1) {
            }
            int i32222 = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
            if (this.mContentHeight.intValue() <= this.mContentWidth.intValue()) {
            }
            this.mContentScaleX = f * f5;
            this.mContentScaleY = f5 * f2;
            this.mPivotPointX = f72222;
            this.mPivotPointY = f3;
            updateMatrixScaleRotate();
        }
        f = f4;
        f2 = 1.0f;
        i = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
        float f722222 = 0.0f;
        if (i != 1) {
        }
        int i322222 = AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$ScalableTextureView$ScaleType[this.mScaleType.ordinal()];
        if (this.mContentHeight.intValue() <= this.mContentWidth.intValue()) {
        }
        this.mContentScaleX = f * f5;
        this.mContentScaleY = f5 * f2;
        this.mPivotPointX = f722222;
        this.mPivotPointY = f3;
        updateMatrixScaleRotate();
    }
}
