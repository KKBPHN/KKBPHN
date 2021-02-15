package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class MaskCircleCornerView extends View {
    private Paint mPaint;
    private Xfermode mXfermode;

    public MaskCircleCornerView(Context context) {
        super(context);
        initView();
    }

    public MaskCircleCornerView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MaskCircleCornerView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MaskCircleCornerView(Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView();
    }

    private void initView() {
        this.mPaint = new Paint(1);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setStyle(Style.FILL);
        this.mXfermode = new PorterDuffXfermode(Mode.SRC_OUT);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int saveLayer = canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.mPaint);
        canvas.drawRoundRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), 15.0f, 15.0f, this.mPaint);
        this.mPaint.setXfermode(this.mXfermode);
        canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.mPaint);
        this.mPaint.setXfermode(null);
        canvas.restoreToCount(saveLayer);
    }
}
