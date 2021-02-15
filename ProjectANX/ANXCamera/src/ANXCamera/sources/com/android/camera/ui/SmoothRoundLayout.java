package com.android.camera.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.android.camera.R;

public class SmoothRoundLayout extends FrameLayout {
    protected static final PorterDuffXfermode XFERMODE_DST_OUT = new PorterDuffXfermode(Mode.DST_OUT);
    protected static final PorterDuffXfermode XFERMODE_SRC_OUT = new PorterDuffXfermode(Mode.SRC_OUT);
    protected SmoothDrawHelper mHelper = new SmoothDrawHelper();
    protected Rect mLayer = new Rect();
    protected RectF mSavedLayer = new RectF();
    private boolean mSmooth;

    public SmoothRoundLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SmoothRoundLayout);
        setCornerRadius(obtainStyledAttributes.getDimension(1, 1.0f));
        setStrokeWidth((int) obtainStyledAttributes.getDimension(3, 0.0f));
        setStrokeColor(obtainStyledAttributes.getColor(2, 0));
        this.mSmooth = obtainStyledAttributes.getBoolean(0, true);
        obtainStyledAttributes.recycle();
    }

    private void updateBackground() {
        updateBounds();
        invalidateOutline();
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        int saveLayer = canvas.saveLayer(this.mSavedLayer, null, 31);
        super.dispatchDraw(canvas);
        this.mHelper.drawMask(canvas, getDispatchXfermode());
        canvas.restoreToCount(saveLayer);
        this.mHelper.drawStroke(canvas, getStorkeXfermode());
    }

    public void draw(Canvas canvas) {
        int saveLayer = canvas.saveLayer(this.mSavedLayer, null, 31);
        super.draw(canvas);
        this.mHelper.drawMask(canvas, getXfermode());
        canvas.restoreToCount(saveLayer);
        this.mHelper.drawStroke(canvas, getStorkeXfermode());
    }

    public float getCornerRadius() {
        return this.mHelper.getRadius();
    }

    /* access modifiers changed from: protected */
    public PorterDuffXfermode getDispatchXfermode() {
        return XFERMODE_DST_OUT;
    }

    /* access modifiers changed from: protected */
    public PorterDuffXfermode getStorkeXfermode() {
        return null;
    }

    public int getStrokeColor() {
        return this.mHelper.getStrokeColor();
    }

    public int getStrokeWidth() {
        return this.mHelper.getStrokeWidth();
    }

    /* access modifiers changed from: protected */
    public PorterDuffXfermode getXfermode() {
        return XFERMODE_DST_OUT;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mLayer.set(0, 0, i, i2);
        this.mSavedLayer.set(0.0f, 0.0f, (float) i, (float) i2);
        updateBounds();
    }

    public void setCornerRadius(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.mHelper.setRadius(f);
        this.mHelper.setRadii(null);
        updateBackground();
    }

    public void setStrokeColor(int i) {
        if (this.mHelper.getStrokeColor() != i) {
            this.mHelper.setStrokeColor(i);
            updateBackground();
        }
    }

    public void setStrokeWidth(int i) {
        if (this.mHelper.getStrokeWidth() != i) {
            this.mHelper.setStrokeWidth(i);
            updateBackground();
        }
    }

    /* access modifiers changed from: protected */
    public void updateBounds() {
        this.mHelper.onBoundsChange(this.mLayer, this.mSmooth);
    }
}
