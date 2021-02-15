package com.miui.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class SingleCenterTextView extends TextView {
    private boolean mEnableSingleCenter;

    public SingleCenterTextView(Context context) {
        super(context);
    }

    public SingleCenterTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SingleCenterTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public SingleCenterTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mEnableSingleCenter && getLayout().getLineCount() == 1 && getGravity() != 1) {
            setGravity(1);
        }
    }

    public void setEnableSingleCenter(boolean z) {
        this.mEnableSingleCenter = z;
    }
}
