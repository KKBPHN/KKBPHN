package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import com.android.camera.R;

public class ModeBackground extends View {
    private RectF mArcRectF;
    private int mHeight;
    private int mOffset;
    private Paint mPaint;
    private int mProgress;
    private int mWidth;

    public ModeBackground(Context context) {
        super(context);
        init(context);
    }

    public ModeBackground(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public ModeBackground(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mArcRectF = new RectF();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.mode_edit_icon_bg_stroke_size);
        this.mOffset = dimensionPixelSize;
        this.mPaint.setStrokeWidth((float) dimensionPixelSize);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(context.getColor(R.color.mode_icon_bg));
    }

    @IntRange(from = 0, to = 100)
    public int getProgress() {
        return this.mProgress;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int round = Math.round((((float) this.mProgress) / 100.0f) * 360.0f);
        this.mPaint.setStyle(Style.FILL);
        RectF rectF = this.mArcRectF;
        int i = this.mOffset;
        rectF.set((float) i, (float) i, (float) (this.mWidth - i), (float) (this.mHeight - i));
        canvas.drawArc(this.mArcRectF, -90.0f, (float) round, true, this.mPaint);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (MeasureSpec.getMode(i) != 1073741824) {
            super.onMeasure(i, i2);
            return;
        }
        this.mWidth = MeasureSpec.getSize(i);
        this.mHeight = MeasureSpec.getSize(i2);
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    public void setProgress(@IntRange(from = 0, to = 100) int i) {
        this.mProgress = i;
        invalidate();
    }
}
