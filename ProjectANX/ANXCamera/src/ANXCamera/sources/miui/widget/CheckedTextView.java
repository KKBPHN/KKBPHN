package miui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import com.miui.internal.widget.CheckedTextViewHelper;
import miui.util.ViewUtils;

public class CheckedTextView extends android.widget.CheckedTextView {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int CHECK_MARK_PADDING = 0;
    private int mBasePadding;
    private Drawable mCheckMarkDrawable;
    private boolean mConstructed;

    public CheckedTextView(Context context) {
        this(context, null);
    }

    public CheckedTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16843720);
    }

    public CheckedTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mConstructed = false;
        this.mBasePadding = getPaddingEnd();
        this.mConstructed = true;
        appendCheckMarkPadding();
    }

    private void appendCheckMarkPadding() {
        if (getCheckWidth() > 0) {
            invokeSuperSetPaddingRelative(getPaddingStart(), getPaddingTop(), this.mBasePadding + getCheckWidth(), getPaddingBottom());
        }
    }

    private void drawCheckMark(Canvas canvas) {
        int i;
        int i2;
        Drawable checkMarkDrawable = getCheckMarkDrawable();
        if (checkMarkDrawable != null) {
            int intrinsicWidth = checkMarkDrawable.getCurrent().getIntrinsicWidth();
            if (ViewUtils.isLayoutRtl(this)) {
                i2 = getPaddingLeft();
                i = getCheckWidth();
            } else {
                i2 = getWidth();
                i = getPaddingRight();
            }
            int scrollX = (i2 - i) + getScrollX();
            int paddingTop = getPaddingTop();
            int paddingTop2 = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            int intrinsicHeight = checkMarkDrawable.getIntrinsicHeight();
            if (checkMarkDrawable.getCurrent() instanceof NinePatchDrawable) {
                intrinsicHeight = (getHeight() - paddingTop) - getPaddingBottom();
            } else {
                int gravity = getGravity() & 112;
                if (gravity == 16) {
                    paddingTop = CheckedTextViewHelper.getCheckMarkPositionY(getHeight(), intrinsicHeight, paddingTop2, paddingBottom);
                } else if (gravity == 80) {
                    paddingTop = getHeight() - intrinsicHeight;
                }
            }
            checkMarkDrawable.setBounds(scrollX, paddingTop, intrinsicWidth + scrollX, intrinsicHeight + paddingTop);
            checkMarkDrawable.draw(canvas);
        }
    }

    private int getCheckWidth() {
        Drawable checkMarkDrawable = getCheckMarkDrawable();
        if (checkMarkDrawable == null) {
            return 0;
        }
        return checkMarkDrawable.getCurrent().getIntrinsicWidth();
    }

    private void invokeSuperSetPadding(int i, int i2, int i3, int i4) {
        super.setPadding(i, i2, i3, i4);
    }

    private void invokeSuperSetPaddingRelative(int i, int i2, int i3, int i4) {
        super.setPaddingRelative(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.setState(getDrawableState());
            invalidate();
        }
    }

    public Drawable getCheckMarkDrawable() {
        return this.mCheckMarkDrawable;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mCheckMarkDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (isChecked()) {
            android.widget.CheckedTextView.mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        drawCheckMark(canvas);
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0020, code lost:
        if (r1 > r3) goto L_0x0024;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int i, int i2) {
        int i3;
        super.onMeasure(i, i2);
        int checkWidth = getCheckWidth();
        if (checkWidth != 0) {
            int mode = MeasureSpec.getMode(i);
            if (mode != 1073741824) {
                int measuredWidth = getMeasuredWidth() + checkWidth;
                if (mode == Integer.MIN_VALUE) {
                    i3 = MeasureSpec.getSize(i);
                }
                i3 = measuredWidth;
                setMeasuredDimension(i3, getMeasuredHeight());
            }
        }
    }

    public void setCheckMarkDrawable(Drawable drawable) {
        Drawable drawable2 = this.mCheckMarkDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.mCheckMarkDrawable);
        }
        if (drawable != null) {
            drawable.setCallback(this);
            drawable.setVisible(getVisibility() == 0, false);
            drawable.setState(CHECKED_STATE_SET);
            setMinHeight(drawable.getIntrinsicHeight());
            drawable.setState(getDrawableState());
        }
        this.mCheckMarkDrawable = drawable;
        if (this.mConstructed) {
            appendCheckMarkPadding();
        }
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mBasePadding = i3;
        invokeSuperSetPadding(i, i2, i3 + (i3 == getPaddingRight() ? 0 : getCheckWidth()), i4);
    }

    public void setPaddingRelative(int i, int i2, int i3, int i4) {
        this.mBasePadding = i3;
        invokeSuperSetPaddingRelative(i, i2, i3 + (i3 == getPaddingEnd() ? 0 : getCheckWidth()), i4);
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mCheckMarkDrawable;
    }
}
