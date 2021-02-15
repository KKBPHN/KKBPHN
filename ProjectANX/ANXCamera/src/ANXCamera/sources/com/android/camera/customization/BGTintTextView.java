package com.android.camera.customization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class BGTintTextView extends TextView {
    private Integer mBGColor = null;
    private Paint mPaint;
    private int mRadius;

    public BGTintTextView(Context context) {
        super(context);
        init();
    }

    public BGTintTextView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public BGTintTextView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public BGTintTextView(Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    private void init() {
        ViewCompat.setLayerType(this, 2, null);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setXfermode(new PorterDuffXfermode(Mode.OVERLAY));
        this.mRadius = 6;
    }

    public void onDraw(Canvas canvas) {
        Paint paint = this.mPaint;
        Integer num = this.mBGColor;
        paint.setColor(num == null ? TintColor.tintColor() : num.intValue());
        int saveLayer = canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, 31);
        super.onDraw(canvas);
        float width = (float) getWidth();
        float height = (float) getHeight();
        int i = this.mRadius;
        canvas.drawRoundRect(0.0f, 0.0f, width, height, (float) i, (float) i, this.mPaint);
        canvas.restoreToCount(saveLayer);
    }

    public void setBGColor(int i) {
        this.mBGColor = Integer.valueOf(i);
        invalidate();
    }
}
