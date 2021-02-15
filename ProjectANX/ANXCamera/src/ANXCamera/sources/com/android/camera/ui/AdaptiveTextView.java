package com.android.camera.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.android.camera.R;
import com.android.camera.log.Log;

@SuppressLint({"AppCompatCustomView"})
public class AdaptiveTextView extends TextView {
    public static final String TAG = " AdaptiveTv";
    private float mMaxFontScale = 1.4f;
    private float mRealFontScale = 0.0f;

    public AdaptiveTextView(Context context) {
        super(context);
    }

    public AdaptiveTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public AdaptiveTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public AdaptiveTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AdaptiveTextView);
        this.mMaxFontScale = obtainStyledAttributes.getFloat(0, 1.4f);
        obtainStyledAttributes.recycle();
        StringBuilder sb = new StringBuilder();
        sb.append("init:  mMaxFontScale :");
        sb.append(this.mMaxFontScale);
        Log.i(" AdaptiveTv", sb.toString());
    }

    public boolean isFocused() {
        return true;
    }

    public void setText(CharSequence charSequence, BufferType bufferType) {
        super.setText(charSequence, bufferType);
        if (this.mMaxFontScale > 0.0f) {
            float f = getResources().getConfiguration().fontScale;
            if (f >= this.mMaxFontScale) {
                if (this.mRealFontScale == 0.0f) {
                    this.mRealFontScale = getTextSize() * (this.mMaxFontScale / f);
                }
                setTextSize(0, this.mRealFontScale);
                StringBuilder sb = new StringBuilder();
                sb.append("setText:  mMaxFontScale :");
                sb.append(this.mMaxFontScale);
                sb.append("   mRealFontScale : ");
                sb.append(this.mRealFontScale);
                Log.i(" AdaptiveTv", sb.toString());
            }
        }
    }

    public void setmMaxFontScale(float f) {
        this.mMaxFontScale = f;
    }
}
