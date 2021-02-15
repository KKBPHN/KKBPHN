package miui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;
import miui.R;
import miui.util.Log;

@TargetApi(21)
public class NestedScrollingLayout extends FrameLayout {
    private static final int OFFSET_ANIMATION_DURATION = 300;
    private static final String TAG = "NestedScrollingLayout";
    private boolean mNeedScroll;
    private int mScrollTarget;
    protected View mScrollableView;
    private int mScrollableViewId;
    private Scroller mScroller;
    private int mScrollingFrom;
    private int mScrollingProgress;
    private int mScrollingTo;
    private boolean mWasNestedFlung;

    public NestedScrollingLayout(Context context) {
        this(context, null);
    }

    public NestedScrollingLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NestedScrollingLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mScroller = new Scroller(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.NestedScrollingLayout);
        this.mScrollableViewId = obtainStyledAttributes.getResourceId(R.styleable.NestedScrollingLayout_scrollableView, 16908298);
        obtainStyledAttributes.recycle();
    }

    private void dispatchScrollingProgressUpdated() {
        onScrollingProgressUpdated(this.mScrollingProgress);
    }

    private void doScroll() {
        int i = this.mScrollTarget;
        int i2 = this.mScrollingProgress;
        if (i != i2) {
            this.mScroller.startScroll(0, i2, 0, i - i2, 300);
            postInvalidate();
        }
    }

    private void prepareToScroll(int i) {
        this.mNeedScroll = i != this.mScrollingProgress;
        this.mScrollTarget = i;
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller.computeScrollOffset()) {
            this.mScrollingProgress = this.mScroller.getCurrY();
            dispatchScrollingProgressUpdated();
            postInvalidateOnAnimation();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0 && !this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if ((actionMasked == 1 || actionMasked == 3) && this.mNeedScroll) {
            doScroll();
        }
        return dispatchTouchEvent;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mScrollableView = findViewById(this.mScrollableViewId);
        View view = this.mScrollableView;
        if (view != null) {
            view.setNestedScrollingEnabled(true);
            return;
        }
        throw new IllegalArgumentException("The scrollableView attribute is required and must refer to a valid child.");
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        dispatchScrollingProgressUpdated();
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0011  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        boolean z2;
        if (z) {
            int i = this.mScrollingProgress;
            if (i < this.mScrollingTo && i > this.mScrollingFrom) {
                z2 = true;
                if (z2) {
                    prepareToScroll(f2 > 0.0f ? this.mScrollingFrom : this.mScrollingTo);
                }
                this.mWasNestedFlung = z2;
                return z2;
            }
        }
        z2 = false;
        if (z2) {
        }
        this.mWasNestedFlung = z2;
        return z2;
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        super.onNestedPreScroll(view, i, i2, iArr);
        int max = Math.max(this.mScrollingFrom, Math.min(this.mScrollingTo, this.mScrollingProgress - i2));
        iArr[1] = this.mScrollingProgress - max;
        this.mScrollingProgress = max;
        dispatchScrollingProgressUpdated();
        if (iArr[1] == 0) {
            view.stopNestedScroll();
        }
    }

    /* access modifiers changed from: protected */
    public void onScrollingProgressUpdated(int i) {
    }

    public boolean onStartNestedScroll(View view, View view2, int i) {
        return i == 2 || super.onStartNestedScroll(view, view2, i);
    }

    public void onStopNestedScroll(View view) {
        super.onStopNestedScroll(view);
        if (!this.mWasNestedFlung) {
            int i = this.mScrollingProgress;
            int i2 = this.mScrollingFrom;
            int i3 = this.mScrollingTo;
            if (i > (i2 + i3) / 2) {
                i2 = i3;
            }
            prepareToScroll(i2);
        }
        this.mWasNestedFlung = false;
    }

    public void setScrollingRange(int i, int i2) {
        if (i > i2) {
            Log.w(TAG, "wrong scrolling range: [%d, %d], making from=to");
            i = i2;
        }
        this.mScrollingFrom = i;
        this.mScrollingTo = i2;
        int i3 = this.mScrollingProgress;
        int i4 = this.mScrollingFrom;
        if (i3 < i4) {
            this.mScrollingProgress = i4;
            dispatchScrollingProgressUpdated();
        }
        int i5 = this.mScrollingProgress;
        int i6 = this.mScrollingTo;
        if (i5 > i6) {
            this.mScrollingProgress = i6;
            dispatchScrollingProgressUpdated();
        }
    }
}
