package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.Nullable;

public class InnerSpringImageView extends ImageView {
    private static final String TAG = "InnerSpringImageView";
    private RectF mSavedLayer = new RectF();
    private float mTranX;
    private float mTranY;

    public InnerSpringImageView(Context context) {
        super(context);
    }

    public InnerSpringImageView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public InnerSpringImageView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void draw(Canvas canvas) {
        int saveLayer = canvas.saveLayer(this.mSavedLayer, null, 31);
        canvas.translate(this.mTranX, this.mTranY);
        super.draw(canvas);
        canvas.restoreToCount(saveLayer);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mSavedLayer.set(0.0f, 0.0f, (float) i, (float) i2);
    }

    public void updateXY(float f, float f2) {
        if (this.mTranX != f || this.mTranY != f2) {
            this.mTranX = f;
            this.mTranY = f2;
            invalidate();
        }
    }
}
