package miui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.miui.internal.R;
import com.miui.internal.widget.SlidingButtonHelper;

public class SlidingSwitch extends Switch {
    private SlidingButtonHelper mHelper;

    public SlidingSwitch(Context context) {
        this(context, null);
    }

    public SlidingSwitch(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.slidingButtonStyle);
    }

    public SlidingSwitch(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHelper = new SlidingButtonHelper(this);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SlidingButton, i, R.style.Widget_SlidingButton);
        this.mHelper.initResource(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.setSliderDrawState();
        }
    }

    public float getAlpha() {
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        return slidingButtonHelper != null ? slidingButtonHelper.getAlpha() : super.getAlpha();
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.jumpDrawablesToCurrentState();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper == null) {
            super.onDraw(canvas);
        } else {
            slidingButtonHelper.onDraw(canvas);
        }
    }

    public void onMeasure(int i, int i2) {
        setMeasuredDimension(this.mHelper.getMeasuredWidth(), this.mHelper.getMeasuredHeight());
        this.mHelper.setParentClipChildren();
    }

    /* access modifiers changed from: protected */
    public boolean onSetAlpha(int i) {
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.onTouchEvent(motionEvent);
        }
        return true;
    }

    public boolean performClick() {
        super.performClick();
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.notifyCheckedChangeListener();
        }
        return true;
    }

    public void setAlpha(float f) {
        super.setAlpha(f);
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.setAlpha(f);
        }
        invalidate();
    }

    public void setButtonDrawable(Drawable drawable) {
    }

    public void setChecked(boolean z) {
        if (isChecked() != z) {
            super.setChecked(z);
            boolean isChecked = isChecked();
            SlidingButtonHelper slidingButtonHelper = this.mHelper;
            if (slidingButtonHelper != null) {
                slidingButtonHelper.setChecked(isChecked);
            }
        }
    }

    public void setOnPerformCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        SlidingButtonHelper slidingButtonHelper = this.mHelper;
        if (slidingButtonHelper != null) {
            slidingButtonHelper.setOnPerformCheckedChangeListener(onCheckedChangeListener);
        }
    }

    public void setPressed(boolean z) {
        super.setPressed(z);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        if (!super.verifyDrawable(drawable)) {
            SlidingButtonHelper slidingButtonHelper = this.mHelper;
            if (slidingButtonHelper == null || !slidingButtonHelper.verifyDrawable(drawable)) {
                return false;
            }
        }
        return true;
    }
}
