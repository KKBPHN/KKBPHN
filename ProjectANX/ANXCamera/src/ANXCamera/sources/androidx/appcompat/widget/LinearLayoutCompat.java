package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import com.ss.android.vesdk.VEResult;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
    private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.widget.LinearLayoutCompat";
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerMode {
    }

    public class LayoutParams extends MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2);
            this.gravity = -1;
            this.weight = f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.LinearLayoutCompat, i, 0);
        int i2 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }

    private void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.height == -1) {
                    int i4 = layoutParams.width;
                    layoutParams.width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                    layoutParams.width = i4;
                }
            }
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersHorizontal(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i4 = 0;
        while (i4 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i4);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i4))) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                drawVerticalDivider(canvas, isLayoutRtl ? virtualChildAt.getRight() + layoutParams.rightMargin : (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth);
            }
            i4++;
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    i3 = virtualChildAt2.getLeft();
                    i2 = layoutParams2.leftMargin;
                } else {
                    i = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                    drawVerticalDivider(canvas, i);
                }
            } else if (isLayoutRtl) {
                i = getPaddingLeft();
                drawVerticalDivider(canvas, i);
            } else {
                i3 = getWidth();
                i2 = getPaddingRight();
            }
            i = (i3 - i2) - this.mDividerWidth;
            drawVerticalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersVertical(Canvas canvas) {
        int i;
        int virtualChildCount = getVirtualChildCount();
        int i2 = 0;
        while (i2 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i2))) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
            i2++;
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                i = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                i = virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: 0000 */
    public void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i = this.mBaselineAlignedChildIndex;
        if (childCount > i) {
            View childAt = getChildAt(i);
            int baseline = childAt.getBaseline();
            if (baseline != -1) {
                int i2 = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    int i3 = this.mGravity & 112;
                    if (i3 != 48) {
                        if (i3 == 16) {
                            i2 += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                        } else if (i3 == 80) {
                            i2 = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                        }
                    }
                }
                return i2 + ((LayoutParams) childAt.getLayoutParams()).topMargin + baseline;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    /* access modifiers changed from: 0000 */
    public int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public int getGravity() {
        return this.mGravity;
    }

    /* access modifiers changed from: 0000 */
    public int getLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getNextLocationOffset(View view) {
        return 0;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    /* access modifiers changed from: 0000 */
    public View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    /* access modifiers changed from: 0000 */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    /* access modifiers changed from: protected */
    @RestrictTo({Scope.LIBRARY})
    public boolean hasDividerBeforeChildAt(int i) {
        boolean z;
        boolean z2 = false;
        if (i == 0) {
            if ((this.mShowDividers & 1) != 0) {
                z2 = true;
            }
            return z2;
        } else if (i == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                z2 = true;
            }
            return z2;
        } else {
            if ((this.mShowDividers & 2) != 0) {
                int i2 = i - 1;
                while (true) {
                    if (i2 < 0) {
                        break;
                    } else if (getChildAt(i2).getVisibility() != 8) {
                        z = true;
                        break;
                    } else {
                        i2--;
                    }
                }
            }
            return z;
        }
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00af  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0100  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        boolean z;
        int i8;
        int i9;
        int i10;
        int i11;
        boolean z2;
        int i12;
        int i13;
        int i14;
        int i15;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop = getPaddingTop();
        int i16 = i4 - i2;
        int paddingBottom = i16 - getPaddingBottom();
        int paddingBottom2 = (i16 - paddingTop) - getPaddingBottom();
        int virtualChildCount = getVirtualChildCount();
        int i17 = this.mGravity;
        int i18 = 8388615 & i17;
        int i19 = i17 & 112;
        boolean z3 = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i18, ViewCompat.getLayoutDirection(this));
        boolean z4 = true;
        int i20 = absoluteGravity != 1 ? absoluteGravity != 5 ? getPaddingLeft() : ((getPaddingLeft() + i3) - i) - this.mTotalLength : getPaddingLeft() + (((i3 - i) - this.mTotalLength) / 2);
        if (isLayoutRtl) {
            i6 = virtualChildCount - 1;
            i5 = -1;
        } else {
            i6 = 0;
            i5 = 1;
        }
        int i21 = 0;
        while (i21 < virtualChildCount) {
            int i22 = i6 + (i5 * i21);
            View virtualChildAt = getVirtualChildAt(i22);
            if (virtualChildAt == null) {
                i10 = i20 + measureNullChild(i22);
                z2 = z4;
                i7 = paddingTop;
                i11 = virtualChildCount;
                i8 = i19;
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i23 = i21;
                if (z3) {
                    i9 = virtualChildCount;
                    if (layoutParams.height != -1) {
                        i12 = virtualChildAt.getBaseline();
                        i13 = layoutParams.gravity;
                        if (i13 < 0) {
                            i13 = i19;
                        }
                        i14 = i13 & 112;
                        i8 = i19;
                        if (i14 != 16) {
                            z = true;
                            i15 = ((((paddingBottom2 - measuredHeight) / 2) + paddingTop) + layoutParams.topMargin) - layoutParams.bottomMargin;
                        } else if (i14 != 48) {
                            if (i14 != 80) {
                                i15 = paddingTop;
                            } else {
                                int i24 = (paddingBottom - measuredHeight) - layoutParams.bottomMargin;
                                if (i12 != -1) {
                                    i24 -= iArr2[2] - (virtualChildAt.getMeasuredHeight() - i12);
                                }
                                i15 = i24;
                            }
                            z = true;
                        } else {
                            int i25 = layoutParams.topMargin + paddingTop;
                            z = true;
                            if (i12 != -1) {
                                i25 += iArr[1] - i12;
                            }
                            i15 = i25;
                        }
                        if (hasDividerBeforeChildAt(i22)) {
                            i20 += this.mDividerWidth;
                        }
                        int i26 = layoutParams.leftMargin + i20;
                        View view = virtualChildAt;
                        View view2 = view;
                        int i27 = i22;
                        i7 = paddingTop;
                        LayoutParams layoutParams2 = layoutParams;
                        setChildFrame(view, i26 + getLocationOffset(virtualChildAt), i15, measuredWidth, measuredHeight);
                        View view3 = view2;
                        i21 = i23 + getChildrenSkipCount(view3, i27);
                        i10 = i26 + measuredWidth + layoutParams2.rightMargin + getNextLocationOffset(view3);
                        i21++;
                        virtualChildCount = i9;
                        i19 = i8;
                        z4 = z;
                        paddingTop = i7;
                    }
                } else {
                    i9 = virtualChildCount;
                }
                i12 = -1;
                i13 = layoutParams.gravity;
                if (i13 < 0) {
                }
                i14 = i13 & 112;
                i8 = i19;
                if (i14 != 16) {
                }
                if (hasDividerBeforeChildAt(i22)) {
                }
                int i262 = layoutParams.leftMargin + i20;
                View view4 = virtualChildAt;
                View view22 = view4;
                int i272 = i22;
                i7 = paddingTop;
                LayoutParams layoutParams22 = layoutParams;
                setChildFrame(view4, i262 + getLocationOffset(virtualChildAt), i15, measuredWidth, measuredHeight);
                View view32 = view22;
                i21 = i23 + getChildrenSkipCount(view32, i272);
                i10 = i262 + measuredWidth + layoutParams22.rightMargin + getNextLocationOffset(view32);
                i21++;
                virtualChildCount = i9;
                i19 = i8;
                z4 = z;
                paddingTop = i7;
            } else {
                int i28 = i21;
                i7 = paddingTop;
                i11 = virtualChildCount;
                i8 = i19;
                z2 = true;
            }
            i21++;
            virtualChildCount = i9;
            i19 = i8;
            z4 = z;
            paddingTop = i7;
        }
    }

    /* access modifiers changed from: 0000 */
    public void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int paddingLeft = getPaddingLeft();
        int i9 = i3 - i;
        int paddingRight = i9 - getPaddingRight();
        int paddingRight2 = (i9 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i10 = this.mGravity;
        int i11 = i10 & 112;
        int i12 = i10 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i13 = i11 != 16 ? i11 != 80 ? getPaddingTop() : ((getPaddingTop() + i4) - i2) - this.mTotalLength : getPaddingTop() + (((i4 - i2) - this.mTotalLength) / 2);
        int i14 = 0;
        while (i14 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i14);
            if (virtualChildAt == null) {
                i6 = i13 + measureNullChild(i14);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i15 = layoutParams.gravity;
                if (i15 < 0) {
                    i15 = i12;
                }
                int absoluteGravity = GravityCompat.getAbsoluteGravity(i15, ViewCompat.getLayoutDirection(this)) & 7;
                if (absoluteGravity == 1) {
                    i7 = ((paddingRight2 - measuredWidth) / 2) + paddingLeft + layoutParams.leftMargin;
                    i8 = i7 - layoutParams.rightMargin;
                } else if (absoluteGravity != 5) {
                    i8 = layoutParams.leftMargin + paddingLeft;
                } else {
                    i7 = paddingRight - measuredWidth;
                    i8 = i7 - layoutParams.rightMargin;
                }
                int i16 = i8;
                if (hasDividerBeforeChildAt(i14)) {
                    i13 += this.mDividerHeight;
                }
                int i17 = i13 + layoutParams.topMargin;
                LayoutParams layoutParams2 = layoutParams;
                setChildFrame(virtualChildAt, i16, i17 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                i14 += getChildrenSkipCount(virtualChildAt, i14);
                i6 = i17 + measuredHeight + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt);
                i5 = 1;
                i14 += i5;
            }
            i5 = 1;
            i14 += i5;
        }
    }

    /* access modifiers changed from: 0000 */
    public void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x03a8, code lost:
        if (r8 > 0) goto L_0x03b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x03b3, code lost:
        if (r8 < 0) goto L_0x03b5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x03b5, code lost:
        r8 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x03b6, code lost:
        r14.measure(android.view.View.MeasureSpec.makeMeasureSpec(r8, r4), r2);
        r9 = android.view.View.combineMeasuredStates(r9, r14.getMeasuredState() & androidx.core.view.ViewCompat.MEASURED_STATE_MASK);
        r2 = r23;
        r4 = r24;
     */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x0439  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0187  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01c7  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01d2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureHorizontal(int i, int i2) {
        int[] iArr;
        int i3;
        int i4;
        boolean z;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        float f;
        int i11;
        boolean z2;
        int i12;
        int i13;
        boolean z3;
        boolean z4;
        int i14;
        View view;
        int i15;
        boolean z5;
        char c;
        int i16;
        int i17;
        int i18 = i;
        int i19 = i2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] iArr2 = this.mMaxAscent;
        int[] iArr3 = this.mMaxDescent;
        iArr2[3] = -1;
        iArr2[2] = -1;
        iArr2[1] = -1;
        iArr2[0] = -1;
        iArr3[3] = -1;
        iArr3[2] = -1;
        iArr3[1] = -1;
        iArr3[0] = -1;
        boolean z6 = this.mBaselineAligned;
        boolean z7 = this.mUseLargestChild;
        int i20 = 1073741824;
        boolean z8 = mode == 1073741824;
        int i21 = 0;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        int i25 = 0;
        boolean z9 = false;
        int i26 = 0;
        boolean z10 = false;
        boolean z11 = true;
        float f2 = 0.0f;
        while (true) {
            iArr = iArr3;
            if (i21 >= virtualChildCount) {
                break;
            }
            View virtualChildAt = getVirtualChildAt(i21);
            if (virtualChildAt == null) {
                this.mTotalLength += measureNullChild(i21);
            } else if (virtualChildAt.getVisibility() == 8) {
                i21 += getChildrenSkipCount(virtualChildAt, i21);
            } else {
                if (hasDividerBeforeChildAt(i21)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                float f3 = f2 + layoutParams.weight;
                if (mode == i20 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                    int i27 = this.mTotalLength;
                    this.mTotalLength = z8 ? i27 + layoutParams.leftMargin + layoutParams.rightMargin : Math.max(i27, layoutParams.leftMargin + i27 + layoutParams.rightMargin);
                    if (z6) {
                        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChildAt.measure(makeMeasureSpec, makeMeasureSpec);
                        i14 = i21;
                        z4 = z7;
                        z3 = z6;
                        view = virtualChildAt;
                    } else {
                        i14 = i21;
                        z4 = z7;
                        z3 = z6;
                        view = virtualChildAt;
                        z9 = true;
                        i15 = 1073741824;
                        if (mode2 == i15 && layoutParams.height == -1) {
                            z5 = true;
                            z10 = true;
                        } else {
                            z5 = false;
                        }
                        int i28 = layoutParams.topMargin + layoutParams.bottomMargin;
                        int measuredHeight = view.getMeasuredHeight() + i28;
                        int combineMeasuredStates = View.combineMeasuredStates(i26, view.getMeasuredState());
                        if (z3) {
                            int baseline = view.getBaseline();
                            if (baseline != -1) {
                                int i29 = layoutParams.gravity;
                                if (i29 < 0) {
                                    i29 = this.mGravity;
                                }
                                int i30 = (((i29 & 112) >> 4) & -2) >> 1;
                                iArr2[i30] = Math.max(iArr2[i30], baseline);
                                iArr[i30] = Math.max(iArr[i30], measuredHeight - baseline);
                            }
                        }
                        int max = Math.max(i23, measuredHeight);
                        boolean z12 = !z11 && layoutParams.height == -1;
                        if (layoutParams.weight <= 0.0f) {
                            if (!z5) {
                                i28 = measuredHeight;
                            }
                            i25 = Math.max(i25, i28);
                        } else {
                            int i31 = i25;
                            if (z5) {
                                measuredHeight = i28;
                            }
                            i24 = Math.max(i24, measuredHeight);
                            i25 = i31;
                        }
                        int i32 = i14;
                        i23 = max;
                        i26 = combineMeasuredStates;
                        z11 = z12;
                        i21 = getChildrenSkipCount(view, i32) + i32;
                        f2 = f3;
                        i21++;
                        int i33 = i2;
                        iArr3 = iArr;
                        z7 = z4;
                        z6 = z3;
                        i20 = 1073741824;
                    }
                } else {
                    if (layoutParams.width != 0 || layoutParams.weight <= 0.0f) {
                        c = 65534;
                        i16 = Integer.MIN_VALUE;
                    } else {
                        c = 65534;
                        layoutParams.width = -2;
                        i16 = 0;
                    }
                    i14 = i21;
                    int i34 = i16;
                    z4 = z7;
                    z3 = z6;
                    char c2 = c;
                    View view2 = virtualChildAt;
                    measureChildBeforeLayout(virtualChildAt, i14, i, f3 == 0.0f ? this.mTotalLength : 0, i2, 0);
                    int i35 = i34;
                    if (i35 != Integer.MIN_VALUE) {
                        layoutParams.width = i35;
                    }
                    int measuredWidth = view2.getMeasuredWidth();
                    if (z8) {
                        view = view2;
                        i17 = this.mTotalLength + layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin + getNextLocationOffset(view);
                    } else {
                        view = view2;
                        int i36 = this.mTotalLength;
                        i17 = Math.max(i36, i36 + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view));
                    }
                    this.mTotalLength = i17;
                    if (z4) {
                        i22 = Math.max(measuredWidth, i22);
                    }
                }
                i15 = 1073741824;
                if (mode2 == i15) {
                }
                z5 = false;
                int i282 = layoutParams.topMargin + layoutParams.bottomMargin;
                int measuredHeight2 = view.getMeasuredHeight() + i282;
                int combineMeasuredStates2 = View.combineMeasuredStates(i26, view.getMeasuredState());
                if (z3) {
                }
                int max2 = Math.max(i23, measuredHeight2);
                if (!z11) {
                }
                if (layoutParams.weight <= 0.0f) {
                }
                int i322 = i14;
                i23 = max2;
                i26 = combineMeasuredStates2;
                z11 = z12;
                i21 = getChildrenSkipCount(view, i322) + i322;
                f2 = f3;
                i21++;
                int i332 = i2;
                iArr3 = iArr;
                z7 = z4;
                z6 = z3;
                i20 = 1073741824;
            }
            z4 = z7;
            z3 = z6;
            i21++;
            int i3322 = i2;
            iArr3 = iArr;
            z7 = z4;
            z6 = z3;
            i20 = 1073741824;
        }
        boolean z13 = z7;
        boolean z14 = z6;
        int i37 = i23;
        int i38 = i24;
        int i39 = i25;
        int i40 = i26;
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
            i3 = i40;
        } else {
            i3 = i40;
            i37 = Math.max(i37, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
        }
        if (z13 && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            int i41 = 0;
            while (i41 < virtualChildCount) {
                View virtualChildAt2 = getVirtualChildAt(i41);
                if (virtualChildAt2 == null) {
                    this.mTotalLength += measureNullChild(i41);
                } else if (virtualChildAt2.getVisibility() == 8) {
                    i41 += getChildrenSkipCount(virtualChildAt2, i41);
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                    int i42 = this.mTotalLength;
                    if (z8) {
                        this.mTotalLength = i42 + layoutParams2.leftMargin + i22 + layoutParams2.rightMargin + getNextLocationOffset(virtualChildAt2);
                    } else {
                        i13 = i4;
                        this.mTotalLength = Math.max(i42, i42 + i22 + layoutParams2.leftMargin + layoutParams2.rightMargin + getNextLocationOffset(virtualChildAt2));
                        i41++;
                        i4 = i13;
                    }
                }
                i13 = i4;
                i41++;
                i4 = i13;
            }
        }
        int i43 = i4;
        this.mTotalLength += getPaddingLeft() + getPaddingRight();
        int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumWidth()), i18, 0);
        int i44 = (16777215 & resolveSizeAndState) - this.mTotalLength;
        if (z9 || (i44 != 0 && f2 > 0.0f)) {
            float f4 = this.mWeightSum;
            if (f4 > 0.0f) {
                f2 = f4;
            }
            iArr2[3] = -1;
            iArr2[2] = -1;
            iArr2[1] = -1;
            iArr2[0] = -1;
            iArr[3] = -1;
            iArr[2] = -1;
            iArr[1] = -1;
            iArr[0] = -1;
            this.mTotalLength = 0;
            int i45 = i38;
            int i46 = -1;
            int i47 = i3;
            float f5 = f2;
            int i48 = 0;
            while (i48 < virtualChildCount) {
                View virtualChildAt3 = getVirtualChildAt(i48);
                if (virtualChildAt3 == null || virtualChildAt3.getVisibility() == 8) {
                    i10 = i44;
                    i9 = virtualChildCount;
                    int i49 = i2;
                } else {
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                    float f6 = layoutParams3.weight;
                    if (f6 > 0.0f) {
                        int i50 = (int) ((((float) i44) * f6) / f5);
                        float f7 = f5 - f6;
                        int i51 = i44 - i50;
                        i9 = virtualChildCount;
                        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom() + layoutParams3.topMargin + layoutParams3.bottomMargin, layoutParams3.height);
                        if (layoutParams3.width == 0) {
                            i12 = 1073741824;
                            if (mode == 1073741824) {
                            }
                        } else {
                            i12 = 1073741824;
                        }
                        i50 = virtualChildAt3.getMeasuredWidth() + i50;
                    } else {
                        i10 = i44;
                        i9 = virtualChildCount;
                        int i52 = i2;
                    }
                    int i53 = this.mTotalLength;
                    if (z8) {
                        this.mTotalLength = i53 + virtualChildAt3.getMeasuredWidth() + layoutParams3.leftMargin + layoutParams3.rightMargin + getNextLocationOffset(virtualChildAt3);
                        f = f5;
                    } else {
                        f = f5;
                        this.mTotalLength = Math.max(i53, virtualChildAt3.getMeasuredWidth() + i53 + layoutParams3.leftMargin + layoutParams3.rightMargin + getNextLocationOffset(virtualChildAt3));
                    }
                    boolean z15 = mode2 != 1073741824 && layoutParams3.height == -1;
                    int i54 = layoutParams3.topMargin + layoutParams3.bottomMargin;
                    int measuredHeight3 = virtualChildAt3.getMeasuredHeight() + i54;
                    i46 = Math.max(i46, measuredHeight3);
                    if (!z15) {
                        i54 = measuredHeight3;
                    }
                    int max3 = Math.max(i45, i54);
                    if (z11) {
                        i11 = -1;
                        if (layoutParams3.height == -1) {
                            z2 = true;
                            if (z14) {
                                int baseline2 = virtualChildAt3.getBaseline();
                                if (baseline2 != i11) {
                                    int i55 = layoutParams3.gravity;
                                    if (i55 < 0) {
                                        i55 = this.mGravity;
                                    }
                                    int i56 = (((i55 & 112) >> 4) & -2) >> 1;
                                    iArr2[i56] = Math.max(iArr2[i56], baseline2);
                                    iArr[i56] = Math.max(iArr[i56], measuredHeight3 - baseline2);
                                    i45 = max3;
                                    z11 = z2;
                                    f5 = f;
                                }
                            }
                            i45 = max3;
                            z11 = z2;
                            f5 = f;
                        }
                    } else {
                        i11 = -1;
                    }
                    z2 = false;
                    if (z14) {
                    }
                    i45 = max3;
                    z11 = z2;
                    f5 = f;
                }
                i48++;
                int i57 = i;
                i44 = i10;
                virtualChildCount = i9;
            }
            i6 = i2;
            i5 = virtualChildCount;
            this.mTotalLength += getPaddingLeft() + getPaddingRight();
            int max4 = (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) ? i46 : Math.max(i46, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
            i7 = max4;
            i3 = i47;
            i8 = i45;
        } else {
            i8 = Math.max(i38, i39);
            if (z13 && mode != 1073741824) {
                for (int i58 = 0; i58 < virtualChildCount; i58++) {
                    View virtualChildAt4 = getVirtualChildAt(i58);
                    if (!(virtualChildAt4 == null || virtualChildAt4.getVisibility() == 8 || ((LayoutParams) virtualChildAt4.getLayoutParams()).weight <= 0.0f)) {
                        virtualChildAt4.measure(MeasureSpec.makeMeasureSpec(i22, 1073741824), MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredHeight(), 1073741824));
                    }
                }
            }
            i6 = i2;
            i5 = virtualChildCount;
            i7 = i43;
        }
        if (z || mode2 == 1073741824) {
            i8 = i7;
        }
        setMeasuredDimension(resolveSizeAndState | (i3 & ViewCompat.MEASURED_STATE_MASK), View.resolveSizeAndState(Math.max(i8 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i6, i3 << 16));
        if (z10) {
            forceUniformHeight(i5, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public int measureNullChild(int i) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x02df, code lost:
        if (r15 > 0) goto L_0x02ed;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x02ea, code lost:
        if (r15 < 0) goto L_0x02ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x02ec, code lost:
        r15 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x02ed, code lost:
        r13.measure(r9, android.view.View.MeasureSpec.makeMeasureSpec(r15, r10));
        r1 = android.view.View.combineMeasuredStates(r1, r13.getMeasuredState() & androidx.core.view.InputDeviceCompat.SOURCE_ANY);
     */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0329  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureVertical(int i, int i2) {
        int i3;
        boolean z;
        int i4;
        int i5;
        int i6;
        float f;
        int i7;
        int i8;
        int i9;
        boolean z2;
        int i10;
        int i11;
        int max;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        View view;
        int i19;
        boolean z3;
        int i20;
        int i21;
        int i22;
        int i23 = i;
        int i24 = i2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int i25 = this.mBaselineAlignedChildIndex;
        boolean z4 = this.mUseLargestChild;
        int i26 = 0;
        int i27 = 0;
        int i28 = 0;
        int i29 = 0;
        int i30 = 0;
        int i31 = 0;
        boolean z5 = false;
        boolean z6 = false;
        float f2 = 0.0f;
        boolean z7 = true;
        while (true) {
            int i32 = 8;
            int i33 = i29;
            if (i31 < virtualChildCount) {
                View virtualChildAt = getVirtualChildAt(i31);
                if (virtualChildAt == null) {
                    this.mTotalLength += measureNullChild(i31);
                    i12 = virtualChildCount;
                    i29 = i33;
                } else {
                    int i34 = i26;
                    if (virtualChildAt.getVisibility() == 8) {
                        i31 += getChildrenSkipCount(virtualChildAt, i31);
                        i12 = virtualChildCount;
                        i29 = i33;
                        i26 = i34;
                    } else {
                        if (hasDividerBeforeChildAt(i31)) {
                            this.mTotalLength += this.mDividerHeight;
                        }
                        LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                        float f3 = f2 + layoutParams.weight;
                        if (mode2 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                            int i35 = this.mTotalLength;
                            int i36 = i27;
                            this.mTotalLength = Math.max(i35, layoutParams.topMargin + i35 + layoutParams.bottomMargin);
                            i19 = i28;
                            view = virtualChildAt;
                            i14 = i30;
                            i22 = virtualChildCount;
                            z5 = true;
                            i18 = i34;
                            i15 = i36;
                            i17 = i31;
                            int i37 = i33;
                            i13 = mode2;
                            i16 = i37;
                        } else {
                            int i38 = i27;
                            if (layoutParams.height != 0 || layoutParams.weight <= 0.0f) {
                                i21 = Integer.MIN_VALUE;
                            } else {
                                layoutParams.height = -2;
                                i21 = 0;
                            }
                            i18 = i34;
                            int i39 = i21;
                            i15 = i38;
                            int i40 = i28;
                            View view2 = virtualChildAt;
                            i22 = virtualChildCount;
                            int i41 = i33;
                            i13 = mode2;
                            i16 = i41;
                            i14 = i30;
                            i17 = i31;
                            measureChildBeforeLayout(virtualChildAt, i31, i, 0, i2, f3 == 0.0f ? this.mTotalLength : 0);
                            int i42 = i39;
                            if (i42 != Integer.MIN_VALUE) {
                                layoutParams.height = i42;
                            }
                            int measuredHeight = view2.getMeasuredHeight();
                            int i43 = this.mTotalLength;
                            view = view2;
                            this.mTotalLength = Math.max(i43, i43 + measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
                            i19 = z4 ? Math.max(measuredHeight, i40) : i40;
                        }
                        if (i25 >= 0 && i25 == i17 + 1) {
                            this.mBaselineChildTop = this.mTotalLength;
                        }
                        if (i17 >= i25 || layoutParams.weight <= 0.0f) {
                            if (mode == 1073741824 || layoutParams.width != -1) {
                                z3 = false;
                            } else {
                                z3 = true;
                                z6 = true;
                            }
                            int i44 = layoutParams.leftMargin + layoutParams.rightMargin;
                            int measuredWidth = view.getMeasuredWidth() + i44;
                            int max2 = Math.max(i15, measuredWidth);
                            int combineMeasuredStates = View.combineMeasuredStates(i18, view.getMeasuredState());
                            boolean z8 = z7 && layoutParams.width == -1;
                            if (layoutParams.weight > 0.0f) {
                                if (!z3) {
                                    i44 = measuredWidth;
                                }
                                i16 = Math.max(i16, i44);
                                i20 = i14;
                            } else {
                                if (!z3) {
                                    i44 = measuredWidth;
                                }
                                i20 = Math.max(i14, i44);
                            }
                            i28 = i19;
                            z7 = z8;
                            i29 = i16;
                            f2 = f3;
                            int i45 = max2;
                            i30 = i20;
                            i26 = combineMeasuredStates;
                            i31 = getChildrenSkipCount(view, i17) + i17;
                            i27 = i45;
                            i31++;
                            int i46 = i;
                            int i47 = i2;
                            mode2 = i13;
                            virtualChildCount = i12;
                        } else {
                            throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                        }
                    }
                }
                i13 = mode2;
                i31++;
                int i462 = i;
                int i472 = i2;
                mode2 = i13;
                virtualChildCount = i12;
            } else {
                int i48 = i26;
                int i49 = i28;
                int i50 = i30;
                int i51 = virtualChildCount;
                int i52 = i27;
                int i53 = i33;
                int i54 = mode2;
                int i55 = i53;
                if (this.mTotalLength > 0) {
                    i3 = i51;
                    if (hasDividerBeforeChildAt(i3)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                } else {
                    i3 = i51;
                }
                int i56 = i54;
                if (z4 && (i56 == Integer.MIN_VALUE || i56 == 0)) {
                    this.mTotalLength = 0;
                    int i57 = 0;
                    while (i57 < i3) {
                        View virtualChildAt2 = getVirtualChildAt(i57);
                        if (virtualChildAt2 == null) {
                            max = this.mTotalLength + measureNullChild(i57);
                        } else if (virtualChildAt2.getVisibility() == i32) {
                            i57 += getChildrenSkipCount(virtualChildAt2, i57);
                            i57++;
                            i32 = 8;
                        } else {
                            LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                            int i58 = this.mTotalLength;
                            max = Math.max(i58, i58 + i49 + layoutParams2.topMargin + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt2));
                        }
                        this.mTotalLength = max;
                        i57++;
                        i32 = 8;
                    }
                }
                this.mTotalLength += getPaddingTop() + getPaddingBottom();
                int i59 = i2;
                int i60 = i49;
                int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumHeight()), i59, 0);
                int i61 = (16777215 & resolveSizeAndState) - this.mTotalLength;
                if (z5 || (i61 != 0 && f2 > 0.0f)) {
                    float f4 = this.mWeightSum;
                    if (f4 > 0.0f) {
                        f2 = f4;
                    }
                    this.mTotalLength = 0;
                    float f5 = f2;
                    int i62 = 0;
                    int i63 = i48;
                    int i64 = i50;
                    i5 = i63;
                    while (i62 < i3) {
                        View virtualChildAt3 = getVirtualChildAt(i62);
                        if (virtualChildAt3.getVisibility() == 8) {
                            f = f5;
                            int i65 = i;
                        } else {
                            LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                            float f6 = layoutParams3.weight;
                            if (f6 > 0.0f) {
                                int i66 = (int) ((((float) i61) * f6) / f5);
                                i7 = i61 - i66;
                                f = f5 - f6;
                                int childMeasureSpec = ViewGroup.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + layoutParams3.leftMargin + layoutParams3.rightMargin, layoutParams3.width);
                                if (layoutParams3.height == 0) {
                                    i11 = 1073741824;
                                    if (i56 == 1073741824) {
                                    }
                                } else {
                                    i11 = 1073741824;
                                }
                                i66 = virtualChildAt3.getMeasuredHeight() + i66;
                            } else {
                                float f7 = f5;
                                int i67 = i;
                                i7 = i61;
                                f = f7;
                            }
                            int i68 = layoutParams3.leftMargin + layoutParams3.rightMargin;
                            int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i68;
                            i52 = Math.max(i52, measuredWidth2);
                            if (mode != 1073741824) {
                                i9 = i8;
                                i10 = -1;
                                if (layoutParams3.width == -1) {
                                    z2 = true;
                                    if (!z2) {
                                        i68 = measuredWidth2;
                                    }
                                    i64 = Math.max(i64, i68);
                                    boolean z9 = !z7 && layoutParams3.width == i10;
                                    int i69 = this.mTotalLength;
                                    this.mTotalLength = Math.max(i69, virtualChildAt3.getMeasuredHeight() + i69 + layoutParams3.topMargin + layoutParams3.bottomMargin + getNextLocationOffset(virtualChildAt3));
                                    z7 = z9;
                                    i61 = i7;
                                    i5 = i9;
                                }
                            } else {
                                i9 = i8;
                                i10 = -1;
                            }
                            z2 = false;
                            if (!z2) {
                            }
                            i64 = Math.max(i64, i68);
                            if (!z7) {
                            }
                            int i692 = this.mTotalLength;
                            this.mTotalLength = Math.max(i692, virtualChildAt3.getMeasuredHeight() + i692 + layoutParams3.topMargin + layoutParams3.bottomMargin + getNextLocationOffset(virtualChildAt3));
                            z7 = z9;
                            i61 = i7;
                            i5 = i9;
                        }
                        i62++;
                        f5 = f;
                    }
                    i4 = i;
                    this.mTotalLength += getPaddingTop() + getPaddingBottom();
                    i6 = i64;
                } else {
                    i6 = Math.max(i50, i55);
                    if (z4 && i56 != 1073741824) {
                        for (int i70 = 0; i70 < i3; i70++) {
                            View virtualChildAt4 = getVirtualChildAt(i70);
                            if (!(virtualChildAt4 == null || virtualChildAt4.getVisibility() == 8 || ((LayoutParams) virtualChildAt4.getLayoutParams()).weight <= 0.0f)) {
                                virtualChildAt4.measure(MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(i60, 1073741824));
                            }
                        }
                    }
                    i4 = i;
                    i5 = i48;
                }
                if (z || mode == 1073741824) {
                    i6 = i52;
                }
                setMeasuredDimension(View.resolveSizeAndState(Math.max(i6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i4, i5), resolveSizeAndState);
                if (z6) {
                    forceUniformWidth(i3, i59);
                    return;
                }
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            StringBuilder sb = new StringBuilder();
            sb.append("base aligned child index out of range (0, ");
            sb.append(getChildCount());
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        this.mBaselineAlignedChildIndex = i;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable != this.mDivider) {
            this.mDivider = drawable;
            boolean z = false;
            if (drawable != null) {
                this.mDividerWidth = drawable.getIntrinsicWidth();
                this.mDividerHeight = drawable.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (drawable == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= GravityCompat.START;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | (-8388616 & i3);
            requestLayout();
        }
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & VEResult.TER_NO_SPACE);
            requestLayout();
        }
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
