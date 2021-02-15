package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.view.ViewCompat;

@RestrictTo({Scope.LIBRARY})
public class ContentFrameLayout extends FrameLayout {
    private OnAttachListener mAttachListener;
    private final Rect mDecorPadding;
    private TypedValue mFixedHeightMajor;
    private TypedValue mFixedHeightMinor;
    private TypedValue mFixedWidthMajor;
    private TypedValue mFixedWidthMinor;
    private TypedValue mMinWidthMajor;
    private TypedValue mMinWidthMinor;

    public interface OnAttachListener {
        void onAttachedFromWindow();

        void onDetachedFromWindow();
    }

    public ContentFrameLayout(Context context) {
        this(context, null);
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDecorPadding = new Rect();
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void dispatchFitSystemWindows(Rect rect) {
        fitSystemWindows(rect);
    }

    public TypedValue getFixedHeightMajor() {
        if (this.mFixedHeightMajor == null) {
            this.mFixedHeightMajor = new TypedValue();
        }
        return this.mFixedHeightMajor;
    }

    public TypedValue getFixedHeightMinor() {
        if (this.mFixedHeightMinor == null) {
            this.mFixedHeightMinor = new TypedValue();
        }
        return this.mFixedHeightMinor;
    }

    public TypedValue getFixedWidthMajor() {
        if (this.mFixedWidthMajor == null) {
            this.mFixedWidthMajor = new TypedValue();
        }
        return this.mFixedWidthMajor;
    }

    public TypedValue getFixedWidthMinor() {
        if (this.mFixedWidthMinor == null) {
            this.mFixedWidthMinor = new TypedValue();
        }
        return this.mFixedWidthMinor;
    }

    public TypedValue getMinWidthMajor() {
        if (this.mMinWidthMajor == null) {
            this.mMinWidthMajor = new TypedValue();
        }
        return this.mMinWidthMajor;
    }

    public TypedValue getMinWidthMinor() {
        if (this.mMinWidthMinor == null) {
            this.mMinWidthMinor = new TypedValue();
        }
        return this.mMinWidthMinor;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        OnAttachListener onAttachListener = this.mAttachListener;
        if (onAttachListener != null) {
            onAttachListener.onAttachedFromWindow();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        OnAttachListener onAttachListener = this.mAttachListener;
        if (onAttachListener != null) {
            onAttachListener.onDetachedFromWindow();
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:59:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int i, int i2) {
        boolean z;
        int i3;
        TypedValue typedValue;
        int i4;
        float fraction;
        int i5;
        float fraction2;
        int i6;
        float fraction3;
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        boolean z2 = true;
        boolean z3 = displayMetrics.widthPixels < displayMetrics.heightPixels;
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        if (mode == Integer.MIN_VALUE) {
            TypedValue typedValue2 = z3 ? this.mFixedWidthMinor : this.mFixedWidthMajor;
            if (typedValue2 != null) {
                int i7 = typedValue2.type;
                if (i7 != 0) {
                    if (i7 == 5) {
                        fraction3 = typedValue2.getDimension(displayMetrics);
                    } else if (i7 == 6) {
                        int i8 = displayMetrics.widthPixels;
                        fraction3 = typedValue2.getFraction((float) i8, (float) i8);
                    } else {
                        i6 = 0;
                        if (i6 > 0) {
                            Rect rect = this.mDecorPadding;
                            i3 = MeasureSpec.makeMeasureSpec(Math.min(i6 - (rect.left + rect.right), MeasureSpec.getSize(i)), 1073741824);
                            z = true;
                            if (mode2 == Integer.MIN_VALUE) {
                                TypedValue typedValue3 = z3 ? this.mFixedHeightMajor : this.mFixedHeightMinor;
                                if (typedValue3 != null) {
                                    int i9 = typedValue3.type;
                                    if (i9 != 0) {
                                        if (i9 == 5) {
                                            fraction2 = typedValue3.getDimension(displayMetrics);
                                        } else if (i9 == 6) {
                                            int i10 = displayMetrics.heightPixels;
                                            fraction2 = typedValue3.getFraction((float) i10, (float) i10);
                                        } else {
                                            i5 = 0;
                                            if (i5 > 0) {
                                                Rect rect2 = this.mDecorPadding;
                                                i2 = MeasureSpec.makeMeasureSpec(Math.min(i5 - (rect2.top + rect2.bottom), MeasureSpec.getSize(i2)), 1073741824);
                                            }
                                        }
                                        i5 = (int) fraction2;
                                        if (i5 > 0) {
                                        }
                                    }
                                }
                            }
                            super.onMeasure(i3, i2);
                            int measuredWidth = getMeasuredWidth();
                            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824);
                            if (!z && mode == Integer.MIN_VALUE) {
                                typedValue = !z3 ? this.mMinWidthMinor : this.mMinWidthMajor;
                                if (typedValue != null) {
                                    int i11 = typedValue.type;
                                    if (i11 != 0) {
                                        if (i11 == 5) {
                                            fraction = typedValue.getDimension(displayMetrics);
                                        } else if (i11 == 6) {
                                            int i12 = displayMetrics.widthPixels;
                                            fraction = typedValue.getFraction((float) i12, (float) i12);
                                        } else {
                                            i4 = 0;
                                            if (i4 > 0) {
                                                Rect rect3 = this.mDecorPadding;
                                                i4 -= rect3.left + rect3.right;
                                            }
                                            if (measuredWidth < i4) {
                                                makeMeasureSpec = MeasureSpec.makeMeasureSpec(i4, 1073741824);
                                                if (z2) {
                                                    super.onMeasure(makeMeasureSpec, i2);
                                                    return;
                                                }
                                                return;
                                            }
                                        }
                                        i4 = (int) fraction;
                                        if (i4 > 0) {
                                        }
                                        if (measuredWidth < i4) {
                                        }
                                    }
                                }
                            }
                            z2 = false;
                            if (z2) {
                            }
                        }
                    }
                    i6 = (int) fraction3;
                    if (i6 > 0) {
                    }
                }
            }
        }
        i3 = i;
        z = false;
        if (mode2 == Integer.MIN_VALUE) {
        }
        super.onMeasure(i3, i2);
        int measuredWidth2 = getMeasuredWidth();
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(measuredWidth2, 1073741824);
        if (!z3) {
        }
        if (typedValue != null) {
        }
        z2 = false;
        if (z2) {
        }
    }

    public void setAttachListener(OnAttachListener onAttachListener) {
        this.mAttachListener = onAttachListener;
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setDecorPadding(int i, int i2, int i3, int i4) {
        this.mDecorPadding.set(i, i2, i3, i4);
        if (ViewCompat.isLaidOut(this)) {
            requestLayout();
        }
    }
}
