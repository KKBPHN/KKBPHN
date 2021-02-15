package com.android.camera.timerburst;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.android.camera.R;

public class CircleFullImageView extends ImageView {
    private int colorRGBA;
    private Context mContext;
    Paint mPaint;
    private float mRadius;

    public CircleFullImageView(Context context) {
        super(context);
        this.mContext = context;
        initGlobalValue();
    }

    public CircleFullImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initGlobalValue();
    }

    public CircleFullImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        initGlobalValue();
    }

    private void initGlobalValue() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), this.mRadius, this.mPaint);
    }

    public float getCircleRadius(int i) {
        Resources resources;
        int i2;
        if (i == 1) {
            resources = this.mContext.getResources();
            i2 = R.dimen.seek_bar_timer_circle_low;
        } else if (i == 2) {
            resources = this.mContext.getResources();
            i2 = R.dimen.seek_bar_timer_inner_low_height;
        } else if (i != 3) {
            return 0.0f;
        } else {
            resources = this.mContext.getResources();
            i2 = R.dimen.seek_bar_timer_inner_high_height;
        }
        return resources.getDimension(i2) / 2.0f;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    public void updateRadiusView(float f, int i) {
        this.mRadius = f;
        this.colorRGBA = i;
        this.mPaint.setColor(i);
        invalidate();
    }

    public void updateView(int i, int i2) {
        this.mRadius = getCircleRadius(i);
        this.colorRGBA = i2;
        this.mPaint.setColor(i2);
        invalidate();
    }
}
