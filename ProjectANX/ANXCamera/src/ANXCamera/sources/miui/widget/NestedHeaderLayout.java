package miui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import miui.R;

@TargetApi(21)
public class NestedHeaderLayout extends NestedScrollingLayout {
    private View mHeaderView;
    private int mHeaderViewId;
    private float mRangeOffset;

    public NestedHeaderLayout(Context context) {
        this(context, null);
    }

    public NestedHeaderLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NestedHeaderLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.NestedHeaderLayout);
        this.mHeaderViewId = obtainStyledAttributes.getResourceId(R.styleable.NestedHeaderLayout_headerView, R.id.header_view);
        this.mRangeOffset = obtainStyledAttributes.getDimension(R.styleable.NestedHeaderLayout_rangeOffset, 0.0f);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mHeaderView = findViewById(this.mHeaderViewId);
        if (this.mHeaderView == null) {
            throw new IllegalArgumentException("The headerView attribute is required and must refer to a valid child.");
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setScrollingRange((int) (((float) (-this.mHeaderView.getMeasuredHeight())) + this.mRangeOffset), 0);
    }

    /* access modifiers changed from: protected */
    public void onScrollingProgressUpdated(int i) {
        super.onScrollingProgressUpdated(i);
        View view = this.mHeaderView;
        view.offsetTopAndBottom(i - view.getTop());
        this.mScrollableView.offsetTopAndBottom((i + this.mHeaderView.getMeasuredHeight()) - this.mScrollableView.getTop());
    }
}
