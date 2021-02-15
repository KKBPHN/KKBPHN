package com.miui.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import com.miui.internal.R;

public class DialogParentPanel extends LinearLayout {
    private TypedValue mFixedHeightMajor;
    private TypedValue mFixedHeightMinor;
    private TypedValue mFixedWidthMajor;
    private TypedValue mFixedWidthMinor;
    private TypedValue mMaxHeightMajor;
    private TypedValue mMaxHeightMinor;
    private TypedValue mMaxWidthMajor;
    private TypedValue mMaxWidthMinor;

    public DialogParentPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.Window);
        if (obtainStyledAttributes.hasValue(R.styleable.Window_windowFixedWidthMinor)) {
            this.mFixedWidthMinor = new TypedValue();
            obtainStyledAttributes.getValue(R.styleable.Window_windowFixedWidthMinor, this.mFixedWidthMinor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Window_windowFixedHeightMajor)) {
            this.mFixedHeightMajor = new TypedValue();
            obtainStyledAttributes.getValue(R.styleable.Window_windowFixedHeightMajor, this.mFixedHeightMajor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Window_windowFixedWidthMajor)) {
            this.mFixedWidthMajor = new TypedValue();
            obtainStyledAttributes.getValue(R.styleable.Window_windowFixedWidthMajor, this.mFixedWidthMajor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Window_windowFixedHeightMinor)) {
            this.mFixedHeightMinor = new TypedValue();
            obtainStyledAttributes.getValue(R.styleable.Window_windowFixedHeightMinor, this.mFixedHeightMinor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Window_windowMaxWidthMinor)) {
            this.mMaxWidthMinor = new TypedValue();
            obtainStyledAttributes.getValue(R.styleable.Window_windowMaxWidthMinor, this.mMaxWidthMinor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Window_windowMaxWidthMajor)) {
            this.mMaxWidthMajor = new TypedValue();
            obtainStyledAttributes.getValue(R.styleable.Window_windowMaxWidthMajor, this.mMaxWidthMajor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Window_windowMaxHeightMajor)) {
            this.mMaxHeightMajor = new TypedValue();
            obtainStyledAttributes.getValue(R.styleable.Window_windowMaxHeightMajor, this.mMaxHeightMajor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Window_windowMaxHeightMinor)) {
            this.mMaxHeightMinor = new TypedValue();
            obtainStyledAttributes.getValue(R.styleable.Window_windowMaxHeightMinor, this.mMaxHeightMinor);
        }
        obtainStyledAttributes.recycle();
    }

    private int getHeightMeasureSpec(int i) {
        return getMeasureSpec(i, false, this.mFixedHeightMinor, this.mFixedHeightMajor, this.mMaxHeightMinor, this.mMaxHeightMajor);
    }

    private int getMeasureSpec(int i, boolean z, TypedValue typedValue, TypedValue typedValue2, TypedValue typedValue3, TypedValue typedValue4) {
        if (MeasureSpec.getMode(i) != Integer.MIN_VALUE) {
            return i;
        }
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        boolean z2 = displayMetrics.widthPixels < displayMetrics.heightPixels;
        if (!z2) {
            typedValue = typedValue2;
        }
        int resolveDimension = resolveDimension(displayMetrics, typedValue, z);
        if (resolveDimension > 0) {
            return MeasureSpec.makeMeasureSpec(resolveDimension, 1073741824);
        }
        if (!z2) {
            typedValue3 = typedValue4;
        }
        int resolveDimension2 = resolveDimension(displayMetrics, typedValue3, z);
        return resolveDimension2 > 0 ? MeasureSpec.makeMeasureSpec(Math.min(resolveDimension2, MeasureSpec.getSize(i)), Integer.MIN_VALUE) : i;
    }

    private int getWidthMeasureSpec(int i) {
        return getMeasureSpec(i, true, this.mFixedWidthMinor, this.mFixedWidthMajor, this.mMaxWidthMinor, this.mMaxWidthMajor);
    }

    private int resolveDimension(DisplayMetrics displayMetrics, TypedValue typedValue, boolean z) {
        float fraction;
        if (typedValue != null) {
            int i = typedValue.type;
            if (i == 5) {
                fraction = typedValue.getDimension(displayMetrics);
            } else if (i == 6) {
                float f = (float) (z ? displayMetrics.widthPixels : displayMetrics.heightPixels);
                fraction = typedValue.getFraction(f, f);
            }
            return (int) fraction;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(getWidthMeasureSpec(i), getHeightMeasureSpec(i2));
    }
}
