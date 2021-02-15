package com.android.camera.aiwatermark.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import com.android.camera.log.Log;

@SuppressLint({"AppCompatCustomView"})
public class WatermarkCustomTextView extends TextView {
    private int mPadding;

    public WatermarkCustomTextView(Context context) {
        super(context);
        init();
    }

    public WatermarkCustomTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private int getAdditionalPadding() {
        float textSize = getTextSize();
        TextView textView = new TextView(getContext());
        textView.setTextSize(0, textSize);
        textView.setLines(1);
        textView.measure(0, 0);
        int measuredHeight = textView.getMeasuredHeight();
        float f = ((float) measuredHeight) - textSize;
        if (f > 0.0f) {
            this.mPadding = (int) f;
            StringBuilder sb = new StringBuilder();
            sb.append("onMeasure: height=");
            sb.append(measuredHeight);
            sb.append(" textSize=");
            sb.append(textSize);
            sb.append(" mAdditionalPadding=");
            sb.append(this.mPadding);
            Log.v("WatermarkCustomTextView", sb.toString());
        }
        return this.mPadding;
    }

    private void init() {
        setIncludeFontPadding(false);
    }

    private int measureHeight(String str, int i) {
        float textSize = getTextSize();
        TextView textView = new TextView(getContext());
        textView.setTextSize(0, textSize);
        textView.setText(str);
        textView.measure(i, 0);
        return textView.getMeasuredHeight();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i = this.mPadding;
        canvas.translate(0.0f, (float) (i >= 24 ? ((-i) * 3) / 4 : ((-i) * 1) / 2));
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        getAdditionalPadding();
        if (MeasureSpec.getMode(i2) != 1073741824) {
            i2 = MeasureSpec.makeMeasureSpec((measureHeight(getText().toString(), i) - this.mPadding) + getPaddingTop() + getPaddingBottom(), 1073741824);
        }
        super.onMeasure(i, i2);
    }
}
