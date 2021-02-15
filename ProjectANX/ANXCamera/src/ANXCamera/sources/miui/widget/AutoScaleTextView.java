package miui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.RemotableViewMethod;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import miui.R;

public class AutoScaleTextView extends TextView {
    private static final float TEXT_SIZE_PLUS = 0.001f;
    private float mMinTextSize;
    private float mOrgTextSize;
    private boolean mSkipLayout;

    public AutoScaleTextView(Context context) {
        super(context);
        updateTextSize();
    }

    public AutoScaleTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initializeAttributes(context, attributeSet, 0);
        updateTextSize();
    }

    public AutoScaleTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initializeAttributes(context, attributeSet, 0);
        updateTextSize();
    }

    private void initializeAttributes(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AutoScaleTextView, i, 0);
        this.mMinTextSize = (float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.AutoScaleTextView_minTextSize, 0);
        obtainStyledAttributes.recycle();
    }

    private void updateTextSize() {
        this.mOrgTextSize = super.getTextSize();
    }

    public float getMinTextSize() {
        return this.mMinTextSize;
    }

    public float getTextSize() {
        return this.mOrgTextSize;
    }

    public void invalidate() {
        if (!this.mSkipLayout) {
            super.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        float f = this.mMinTextSize;
        if (f <= 0.0f || f >= this.mOrgTextSize || getMaxLines() > 1) {
            super.onMeasure(i, i2);
        } else if (MeasureSpec.getMode(i) == 0) {
            super.onMeasure(i, i2);
        } else {
            int size = MeasureSpec.getSize(i);
            super.onMeasure(0, i2);
            int measuredWidth = getMeasuredWidth();
            if (measuredWidth > size) {
                int paddingLeft = getPaddingLeft() + getPaddingRight();
                float textSize = (super.getTextSize() * ((float) (size - paddingLeft))) / (((float) (measuredWidth - paddingLeft)) + TEXT_SIZE_PLUS);
                float f2 = this.mMinTextSize;
                if (textSize >= f2) {
                    f2 = textSize;
                }
                this.mSkipLayout = true;
                super.setTextSize(0, f2);
                this.mSkipLayout = false;
                super.onMeasure(i, i2);
            }
        }
    }

    public void requestLayout() {
        if (!this.mSkipLayout) {
            super.requestLayout();
        }
    }

    @RemotableViewMethod
    public void setMinTextSize(float f) {
        setMinTextSize(2, f);
    }

    public void setMinTextSize(int i, float f) {
        Context context = getContext();
        this.mMinTextSize = TypedValue.applyDimension(i, f, (context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics());
    }

    public void setTextSize(float f) {
        super.setTextSize(f);
        updateTextSize();
    }

    public void setTextSize(int i, float f) {
        super.setTextSize(i, f);
        updateTextSize();
    }
}
