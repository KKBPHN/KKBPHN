package com.android.camera.ui.drawable;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;

public abstract class CameraPaintBase {
    public static int ALPHA_HINT = 102;
    public static int ALPHA_OPAQUE = 255;
    public static int ALPHA_OUTSTANDING = 255;
    public boolean isClockwise = true;
    public boolean isRecording;
    public int mBaseAlpha;
    public float mBaseRadius;
    public float mBaseStokeWidth;
    public float mBaseWidthPercent;
    public int mCurrentAlpha;
    @ColorInt
    public int mCurrentColor;
    public float mCurrentStrokeWidth;
    public float mCurrentWidthPercent;
    public int mDstAlpha;
    private float mDstBaseRadius;
    public int mDstColor;
    private float mDstMiddleX;
    private float mDstMiddleY;
    protected float mDstStrokeWidth;
    protected float mDstWidthPercent;
    public float mMiddleX;
    public float mMiddleY;
    public Paint mPaint = new Paint();
    public float mSpeed = 1.0f;
    public int mSrcAlpha;
    private float mSrcBaseRadius;
    public int mSrcColor;
    private float mSrcMiddleX;
    private float mSrcMiddleY;
    public float mSrcStrokeWidth;
    public float mSrcWidthPercent;
    public boolean needZero = true;
    public float timeAngle;
    public int visible;

    public CameraPaintBase(Context context) {
        initPaint(context);
    }

    /* access modifiers changed from: protected */
    public final float calculateCurrentValue(float f, float f2, float f3) {
        return f + ((f2 - f) * f3);
    }

    public abstract void draw(Canvas canvas);

    public void drawCanvas(Canvas canvas) {
        if (!isHide()) {
            draw(canvas);
        }
    }

    public int getVisible() {
        return this.visible;
    }

    /* access modifiers changed from: protected */
    public boolean immediatelyColorChange() {
        return false;
    }

    public abstract void initPaint(Context context);

    /* access modifiers changed from: protected */
    public boolean isHide() {
        return this.visible == 8;
    }

    public void resetRecordingState() {
        this.timeAngle = 0.0f;
        this.isClockwise = true;
        this.isRecording = false;
        this.needZero = true;
    }

    public void reverseClock() {
        this.isClockwise = !this.isClockwise;
        this.mCurrentAlpha = this.isClockwise ? ALPHA_HINT : ALPHA_OUTSTANDING;
    }

    public void setBaseWidthPercent(float f) {
        this.mBaseWidthPercent = f;
    }

    public CameraPaintBase setCurrentAlpha(int i) {
        this.mCurrentAlpha = i;
        this.mPaint.setAlpha(i);
        return this;
    }

    public CameraPaintBase setCurrentColor(int i) {
        this.mCurrentColor = i;
        this.mPaint.setColor(i);
        return this;
    }

    public CameraPaintBase setCurrentStrokeWidth(float f) {
        this.mCurrentStrokeWidth = f;
        this.mPaint.setStrokeWidth(f);
        return this;
    }

    public void setCurrentValues(float f, @ColorInt int i, int i2, float f2) {
        this.mCurrentWidthPercent = f;
        this.mCurrentColor = i;
        this.mCurrentAlpha = i2;
        this.mCurrentStrokeWidth = f2;
        this.mPaint.setColor(this.mCurrentColor);
        this.mPaint.setAlpha(this.mCurrentAlpha);
        this.mPaint.setStrokeWidth(this.mCurrentStrokeWidth);
    }

    public CameraPaintBase setCurrentWidthPercent(float f) {
        this.mCurrentWidthPercent = f;
        return this;
    }

    public void setMiddle(float f, float f2, float f3) {
        this.mMiddleX = f;
        this.mSrcMiddleX = f;
        this.mDstMiddleX = f;
        this.mMiddleY = f2;
        this.mSrcMiddleY = f2;
        this.mDstMiddleY = f2;
        this.mBaseRadius = f3;
        this.mSrcBaseRadius = f3;
        this.mDstBaseRadius = f3;
    }

    public void setResult() {
        this.mCurrentWidthPercent = this.mDstWidthPercent;
        this.mCurrentColor = this.mDstColor;
        this.mCurrentAlpha = this.mDstAlpha;
        this.mCurrentStrokeWidth = this.mDstStrokeWidth;
        this.mMiddleX = this.mDstMiddleX;
        this.mMiddleY = this.mDstMiddleY;
        this.mBaseRadius = this.mDstBaseRadius;
        this.mPaint.setColor(this.mCurrentColor);
        this.mPaint.setAlpha(this.mCurrentAlpha);
        this.mPaint.setStrokeWidth(this.mCurrentStrokeWidth);
        this.mSrcWidthPercent = this.mCurrentWidthPercent;
        this.mSrcColor = this.mCurrentColor;
        this.mSrcAlpha = this.mCurrentAlpha;
        this.mSrcStrokeWidth = this.mCurrentStrokeWidth;
        this.mSrcMiddleX = this.mMiddleX;
        this.mSrcMiddleY = this.mMiddleY;
        this.mSrcBaseRadius = this.mBaseRadius;
    }

    public CameraPaintBase setTargetAlpha(int i) {
        this.mDstAlpha = i;
        this.mSrcAlpha = this.mCurrentAlpha;
        return this;
    }

    public CameraPaintBase setTargetColor(int i) {
        this.mDstColor = i;
        this.mSrcColor = this.mCurrentColor;
        return this;
    }

    public void setTargetMiddle(float f, float f2, float f3) {
        this.mDstMiddleX = f;
        this.mDstMiddleY = f2;
        this.mDstBaseRadius = f3;
        this.mSrcMiddleX = this.mMiddleX;
        this.mSrcMiddleY = this.mMiddleY;
        this.mSrcBaseRadius = this.mBaseRadius;
    }

    public CameraPaintBase setTargetStrokeWidth(float f) {
        this.mDstStrokeWidth = f;
        this.mSrcStrokeWidth = this.mCurrentStrokeWidth;
        return this;
    }

    public void setTargetValues(float f, @ColorInt int i, int i2, float f2) {
        this.mBaseWidthPercent = f;
        this.mBaseStokeWidth = f2;
        this.mBaseAlpha = i2;
        this.mDstWidthPercent = f;
        this.mDstColor = i;
        this.mDstAlpha = i2;
        this.mDstStrokeWidth = f2;
        float f3 = this.mCurrentWidthPercent;
        this.mSrcWidthPercent = f3;
        this.mSrcColor = this.mCurrentColor;
        int i3 = this.mCurrentAlpha;
        this.mSrcAlpha = i3;
        this.mSrcStrokeWidth = this.mCurrentStrokeWidth;
        if (this.mDstWidthPercent != f3) {
            if (this.mDstAlpha == 0 && i3 > 0) {
                this.mSrcAlpha = 5;
                this.mCurrentAlpha = 5;
            }
            if (immediatelyColorChange()) {
                this.mSrcColor = this.mDstColor;
            }
        }
    }

    public CameraPaintBase setTargetWidthPercent(float f) {
        this.mDstWidthPercent = f;
        this.mSrcWidthPercent = this.mCurrentWidthPercent;
        return this;
    }

    public void setVisible(int i) {
        this.visible = i;
    }

    public void updateValue(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        float f2 = this.mDstMiddleX;
        float f3 = this.mSrcMiddleX;
        float f4 = f2 - f3;
        float f5 = this.mDstMiddleY;
        float f6 = this.mSrcMiddleY;
        float f7 = f5 - f6;
        float f8 = this.mDstBaseRadius;
        float f9 = this.mSrcBaseRadius;
        float f10 = f8 - f9;
        this.mMiddleX = f3 + (f4 * f);
        this.mMiddleY = f6 + (f7 * f);
        this.mBaseRadius = f9 + (f10 * f);
        float f11 = this.mDstWidthPercent;
        float f12 = this.mSrcWidthPercent;
        this.mCurrentWidthPercent = f12 + ((f11 - f12) * f);
        float f13 = this.mDstStrokeWidth;
        float f14 = this.mSrcStrokeWidth;
        this.mCurrentStrokeWidth = f14 + ((f13 - f14) * f);
        this.mPaint.setStrokeWidth(this.mCurrentStrokeWidth);
        if (this.mCurrentColor != this.mDstColor) {
            this.mCurrentColor = ((Integer) new ArgbEvaluator().evaluate(f, Integer.valueOf(this.mSrcColor), Integer.valueOf(this.mDstColor))).intValue();
            this.mPaint.setColor(this.mCurrentColor);
        }
        int i = this.mCurrentAlpha;
        int i2 = this.mDstAlpha;
        if (i != i2) {
            int i3 = this.mSrcAlpha;
            this.mCurrentAlpha = Math.round(((float) i3) + (((float) (i2 - i3)) * f));
        }
        this.mPaint.setAlpha(this.mCurrentAlpha);
        if (f == 1.0f) {
            setResult();
        }
    }
}
