package com.android.camera.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.android.camera.customization.TintColor;

public class ColorActivateTextView extends TextView {
    private int mActivateColor;
    private int mNormalCor;

    public ColorActivateTextView(Context context) {
        this(context, null);
    }

    public ColorActivateTextView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorActivateTextView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z && isActivated()) {
            setTextColor(TintColor.tintColor());
        }
    }

    public void setActivateColor(int i) {
        this.mActivateColor = i;
    }

    public void setActivated(boolean z) {
        setTextColor(z ? TintColor.tintColor() : this.mNormalCor);
        super.setActivated(z);
    }

    public void setNormalCor(int i) {
        this.mNormalCor = i;
    }
}
