package com.android.camera.features.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import com.android.camera.log.Log;

@SuppressLint({"AppCompatCustomView"})
public class AdaptiveEditText extends EditText {
    public static final String TAG = " AdaptiveTv";
    private float mMaxFontScale = 1.0f;
    private float mRealFontScale = 0.0f;

    public AdaptiveEditText(Context context) {
        super(context);
    }

    public AdaptiveEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AdaptiveEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public AdaptiveEditText(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setText(CharSequence charSequence, BufferType bufferType) {
        super.setText(charSequence, bufferType);
        float f = getResources().getConfiguration().fontScale;
        if (f > this.mMaxFontScale) {
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

    public void setmMaxFontScale(float f) {
        this.mMaxFontScale = f;
    }
}
