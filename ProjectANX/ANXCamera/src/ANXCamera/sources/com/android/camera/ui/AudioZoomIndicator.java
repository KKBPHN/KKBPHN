package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.android.camera.R;

public class AudioZoomIndicator extends View {
    private static final int MAX_CIRCLE = 10;
    private float mHalfPointAlpha = 0.0f;
    private int mOffAreaColor;
    private Paint mOffAreaPaint;
    private int mOnAreaColor;
    private Paint mOnAreaPaint;
    private int mOnCount = 1;
    private float mPadding;
    private float mRadius;

    public AudioZoomIndicator(Context context) {
        super(context);
        init(context);
    }

    public AudioZoomIndicator(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public AudioZoomIndicator(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private void init(Context context) {
        this.mOnAreaColor = context.getColor(R.color.on_area_color);
        this.mOnAreaPaint = new Paint();
        this.mOnAreaPaint.setColor(this.mOnAreaColor);
        this.mOnAreaPaint.setAntiAlias(true);
        this.mOffAreaColor = context.getColor(R.color.off_area_color);
        this.mOffAreaPaint = new Paint();
        this.mOffAreaPaint.setColor(this.mOffAreaColor);
        this.mOffAreaPaint.setAntiAlias(true);
        this.mRadius = (float) context.getResources().getDimensionPixelSize(R.dimen.audio_zoom_indicator_radius);
        this.mPadding = (float) context.getResources().getDimensionPixelSize(R.dimen.audio_zoom_indicator_padding);
    }

    public void draw(Canvas canvas) {
        int i;
        super.draw(canvas);
        float f = this.mRadius;
        this.mOnAreaPaint.setAlpha(255);
        float f2 = f;
        int i2 = 0;
        while (true) {
            i = this.mOnCount;
            if (i2 >= i) {
                break;
            }
            float f3 = this.mRadius;
            canvas.drawCircle(f2, f3, f3, this.mOnAreaPaint);
            f2 += (this.mRadius * 2.0f) + this.mPadding;
            i2++;
        }
        int i3 = 10 - i;
        float f4 = f2;
        for (int i4 = 0; i4 < i3; i4++) {
            float f5 = this.mRadius;
            canvas.drawCircle(f4, f5, f5, this.mOffAreaPaint);
            f4 += (this.mRadius * 2.0f) + this.mPadding;
        }
        float f6 = this.mHalfPointAlpha;
        if (f6 > 0.0f) {
            this.mOnAreaPaint.setAlpha((int) (f6 * 255.0f));
            float f7 = this.mRadius;
            canvas.drawCircle(f2, f7, f7, this.mOnAreaPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension((int) Math.ceil((double) ((this.mRadius * 2.0f * 10.0f) + (this.mPadding * 9.0f))), (int) Math.ceil((double) (this.mRadius * 2.0f)));
    }

    public void update(float f, float f2, float f3) {
        float f4 = (f2 - f) / 10.0f;
        float f5 = f3 - f;
        int i = (int) (f5 / f4);
        this.mOnCount = Math.max(i, 1);
        if (i > 1) {
            this.mHalfPointAlpha = (f5 % f4) / f4;
        } else {
            this.mHalfPointAlpha = 0.0f;
        }
        invalidate();
    }
}
