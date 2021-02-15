package miuix.nestedheader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.nestedheader.R;
import miuix.nestedheader.widget.NestedScrollingLayout.OnNestedChangedListener;

public class NestedHeaderLayout extends NestedScrollingLayout {
    private static final String TAG = "NestedHeaderLayout";
    /* access modifiers changed from: private */
    public boolean isAniming;
    /* access modifiers changed from: private */
    public boolean isTouch;
    /* access modifiers changed from: private */
    public boolean mAutoAnim;
    private int mHeaderBottomMargin;
    private int mHeaderContentId;
    private float mHeaderContentMinHeight;
    private View mHeaderContentView;
    private int mHeaderInitTop;
    private int mHeaderMeasuredHeight;
    private int mHeaderTopmMargin;
    private View mHeaderView;
    private int mHeaderViewId;
    private int mLastScrollingProgress;
    private NestedHeaderChangedListener mNestedHeaderChangedListener;
    private OnNestedChangedListener mOnNestedChangedListener;
    private float mRangeOffset;
    /* access modifiers changed from: private */
    public int mScrollingFrom;
    /* access modifiers changed from: private */
    public int mScrollingTo;
    private int mTriggerBottomMargin;
    private int mTriggerContentId;
    private float mTriggerContentMinHeight;
    private View mTriggerContentView;
    private int mTriggerMeasuredHeight;
    private int mTriggerTopmMargin;
    private View mTriggerView;
    private int mTriggerViewId;

    public interface NestedHeaderChangedListener {
        void onHeaderClosed(View view);

        void onHeaderOpened(View view);

        void onTriggerClosed(View view);

        void onTriggerOpened(View view);
    }

    public NestedHeaderLayout(Context context) {
        this(context, null);
    }

    public NestedHeaderLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NestedHeaderLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHeaderBottomMargin = 0;
        this.mHeaderTopmMargin = 0;
        this.mTriggerBottomMargin = 0;
        this.mTriggerTopmMargin = 0;
        this.mHeaderInitTop = 0;
        this.mHeaderMeasuredHeight = 0;
        this.mTriggerMeasuredHeight = 0;
        this.mLastScrollingProgress = 0;
        this.isAniming = false;
        this.isTouch = false;
        this.mAutoAnim = true;
        this.mOnNestedChangedListener = new OnNestedChangedListener() {
            public void onStartNestedScroll(int i) {
                if (i == 0) {
                    NestedHeaderLayout.this.isTouch = true;
                }
            }

            public void onStopNestedScroll(int i) {
                if (i == 0) {
                    NestedHeaderLayout.this.isTouch = false;
                }
            }

            public void onStopNestedScrollAccepted(int i) {
                if (NestedHeaderLayout.this.mAutoAnim && !NestedHeaderLayout.this.isAniming && NestedHeaderLayout.this.getScrollingProgress() != 0 && NestedHeaderLayout.this.getScrollingProgress() < NestedHeaderLayout.this.mScrollingTo && NestedHeaderLayout.this.getScrollingProgress() > NestedHeaderLayout.this.mScrollingFrom) {
                    int i2 = 0;
                    if (NestedHeaderLayout.this.getScrollingProgress() > NestedHeaderLayout.this.mScrollingFrom && ((float) NestedHeaderLayout.this.getScrollingProgress()) < ((float) NestedHeaderLayout.this.mScrollingFrom) * 0.5f) {
                        i2 = NestedHeaderLayout.this.mScrollingFrom;
                    } else if ((((float) NestedHeaderLayout.this.getScrollingProgress()) < ((float) NestedHeaderLayout.this.mScrollingFrom) * 0.5f || NestedHeaderLayout.this.getScrollingProgress() >= 0) && ((NestedHeaderLayout.this.getScrollingProgress() <= 0 || ((float) NestedHeaderLayout.this.getScrollingProgress()) >= ((float) NestedHeaderLayout.this.mScrollingTo) * 0.5f) && ((float) NestedHeaderLayout.this.getScrollingProgress()) >= ((float) NestedHeaderLayout.this.mScrollingTo) * 0.5f && NestedHeaderLayout.this.getScrollingProgress() < NestedHeaderLayout.this.mScrollingTo)) {
                        i2 = NestedHeaderLayout.this.mScrollingTo;
                    }
                    NestedHeaderLayout.this.autoAdsorption(i2);
                }
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.NestedHeaderLayout);
        this.mHeaderViewId = obtainStyledAttributes.getResourceId(R.styleable.NestedHeaderLayout_headerView, R.id.header_view);
        this.mTriggerViewId = obtainStyledAttributes.getResourceId(R.styleable.NestedHeaderLayout_triggerView, R.id.trigger_view);
        this.mHeaderContentId = obtainStyledAttributes.getResourceId(R.styleable.NestedHeaderLayout_headerContentId, R.id.header_content_view);
        this.mTriggerContentId = obtainStyledAttributes.getResourceId(R.styleable.NestedHeaderLayout_triggerContentId, R.id.trigger_content_view);
        this.mHeaderContentMinHeight = obtainStyledAttributes.getDimension(R.styleable.NestedHeaderLayout_headerContentMinHeight, context.getResources().getDimension(R.dimen.miuix_nested_header_layout_content_min_height));
        this.mTriggerContentMinHeight = obtainStyledAttributes.getDimension(R.styleable.NestedHeaderLayout_triggerContentMinHeight, context.getResources().getDimension(R.dimen.miuix_nested_header_layout_content_min_height));
        this.mRangeOffset = obtainStyledAttributes.getDimension(R.styleable.NestedHeaderLayout_rangeOffset, 0.0f);
        obtainStyledAttributes.recycle();
        addOnScrollListener(this.mOnNestedChangedListener);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<android.view.View>, for r2v0, types: [java.util.List, java.util.List<android.view.View>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyContentAlpha(List<View> list, float f) {
        if (list != null) {
            float max = Math.max(0.0f, Math.min(1.0f, f));
            for (View alpha : list) {
                alpha.setAlpha(max);
            }
        }
    }

    /* access modifiers changed from: private */
    public void autoAdsorption(int i) {
        String str = "targe";
        Folme.useValue(new Object[0]).setTo(str, Integer.valueOf(getScrollingProgress())).to(str, Integer.valueOf(i), new AnimConfig().addListeners(new TransitionListener() {
            public void onComplete(Object obj) {
                NestedHeaderLayout.this.isAniming = false;
            }

            public void onUpdate(Object obj, Collection collection) {
                UpdateInfo findByName = UpdateInfo.findByName(collection, "targe");
                if (findByName != null && !NestedHeaderLayout.this.isTouch) {
                    NestedHeaderLayout.this.syncScrollingProgress(findByName.getIntValue());
                }
            }
        }));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0064, code lost:
        if (getHeaderViewVisible() == false) goto L_0x0042;
     */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x006f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkSendHeaderChangeListener(int i, int i2, boolean z) {
        if (this.mNestedHeaderChangedListener != null) {
            if (z) {
                if (i2 == 0 && getHeaderViewVisible()) {
                    this.mNestedHeaderChangedListener.onHeaderOpened(this.mHeaderView);
                } else if (i2 == this.mScrollingTo && getTriggerViewVisible()) {
                    this.mNestedHeaderChangedListener.onTriggerOpened(this.mTriggerView);
                }
                if (i < 0 && i2 > 0 && getHeaderViewVisible()) {
                    this.mNestedHeaderChangedListener.onHeaderOpened(this.mHeaderView);
                }
            } else {
                if (i2 != 0 || !getTriggerViewVisible()) {
                    if (i2 != this.mScrollingFrom || !getHeaderViewVisible()) {
                        if (i2 == this.mScrollingFrom) {
                        }
                        int i3 = getHeaderViewVisible() ? 0 : this.mScrollingFrom;
                        if (i > i3 && i2 < i3 && getTriggerViewVisible()) {
                            this.mNestedHeaderChangedListener.onTriggerClosed(this.mTriggerView);
                        }
                    } else {
                        this.mNestedHeaderChangedListener.onHeaderClosed(this.mHeaderView);
                        if (getHeaderViewVisible()) {
                        }
                        this.mNestedHeaderChangedListener.onTriggerClosed(this.mTriggerView);
                    }
                }
                this.mNestedHeaderChangedListener.onTriggerClosed(this.mTriggerView);
                if (getHeaderViewVisible()) {
                }
                this.mNestedHeaderChangedListener.onTriggerClosed(this.mTriggerView);
            }
        }
    }

    private List makeContentViewList(View view, boolean z) {
        if (view == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (z) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    arrayList.add(viewGroup.getChildAt(i));
                }
            } else if (view != null) {
                arrayList.add(view);
            }
            return arrayList;
        }
        arrayList.add(view);
        return arrayList;
    }

    private List makeHeaderContentViewList(View view) {
        return makeContentViewList(view, this.mHeaderContentId == R.id.header_content_view);
    }

    private List makeTriggerContentViewList(View view) {
        return makeContentViewList(view, this.mTriggerContentId == R.id.trigger_content_view);
    }

    private void relayoutContent(View view, View view2, int i, int i2) {
        view.layout(view.getLeft(), i, view.getRight(), Math.max(i, view.getMeasuredHeight() + i + i2));
        if (view != view2) {
            view2.layout(view2.getLeft(), view2.getTop(), view2.getRight(), Math.max(view2.getTop(), view2.getTop() + view2.getMeasuredHeight() + i2));
        }
    }

    /* access modifiers changed from: private */
    public void syncScrollingProgress(int i) {
        updateScrollingProgress(i);
        onScrollingProgressUpdated(i);
    }

    private void updateScrollingRange(boolean z, boolean z2, boolean z3) {
        boolean z4;
        int i;
        boolean z5;
        int i2;
        int i3;
        View view = this.mHeaderView;
        if (view == null || view.getVisibility() == 8) {
            i = 0;
            z4 = false;
        } else {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mHeaderView.getLayoutParams();
            this.mHeaderBottomMargin = marginLayoutParams.bottomMargin;
            this.mHeaderTopmMargin = marginLayoutParams.topMargin;
            this.mHeaderMeasuredHeight = this.mHeaderView.getMeasuredHeight();
            i = ((int) (((((float) (-this.mHeaderMeasuredHeight)) + this.mRangeOffset) - ((float) this.mHeaderTopmMargin)) - ((float) this.mHeaderBottomMargin))) + 0;
            z4 = true;
        }
        View view2 = this.mTriggerView;
        if (view2 == null || view2.getVisibility() == 8) {
            i3 = i;
            i2 = 0;
            z5 = false;
        } else {
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mTriggerView.getLayoutParams();
            this.mTriggerBottomMargin = marginLayoutParams2.bottomMargin;
            this.mTriggerTopmMargin = marginLayoutParams2.topMargin;
            this.mTriggerMeasuredHeight = this.mTriggerView.getMeasuredHeight();
            int i4 = this.mTriggerMeasuredHeight + this.mTriggerTopmMargin + this.mTriggerBottomMargin + 0;
            if (!z4) {
                i3 = -i4;
                z5 = true;
                i2 = 0;
            } else {
                i3 = i;
                z5 = true;
                i2 = i4;
            }
        }
        this.mScrollingFrom = i3;
        this.mScrollingTo = i2;
        setScrollingRange(i3, i2, z4, z5, z, z2, z3);
    }

    public boolean getHeaderViewVisible() {
        View view = this.mHeaderView;
        return view != null && view.getVisibility() == 0;
    }

    public boolean getTriggerViewVisible() {
        View view = this.mTriggerView;
        return view != null && view.getVisibility() == 0;
    }

    public boolean isAutoAnim() {
        return this.mAutoAnim;
    }

    public boolean isHeaderOpen() {
        return getHeaderViewVisible() && getScrollingProgress() >= 0;
    }

    public boolean isTriggerOpen() {
        return getTriggerViewVisible() && ((getHeaderViewVisible() && getScrollingProgress() >= this.mScrollingTo) || (!getHeaderViewVisible() && getScrollingProgress() >= 0));
    }

    /* access modifiers changed from: protected */
    @RequiresApi(api = 21)
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mHeaderView = findViewById(this.mHeaderViewId);
        this.mTriggerView = findViewById(this.mTriggerViewId);
        if (this.mHeaderView == null && this.mTriggerView == null) {
            throw new IllegalArgumentException("The headerView or triggerView attribute is required and must refer to a valid child.");
        }
        View view = this.mHeaderView;
        if (view != null) {
            this.mHeaderContentView = view.findViewById(this.mHeaderContentId);
            if (this.mHeaderContentView == null) {
                this.mHeaderContentView = this.mHeaderView.findViewById(16908318);
            }
        }
        View view2 = this.mTriggerView;
        if (view2 != null) {
            this.mTriggerContentView = view2.findViewById(this.mTriggerContentId);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateScrollingRange(true, false, false);
    }

    /* access modifiers changed from: protected */
    public void onScrollingProgressUpdated(int i) {
        int i2;
        int i3;
        super.onScrollingProgressUpdated(i);
        View view = this.mTriggerView;
        if (view == null || view.getVisibility() == 8) {
            i3 = i;
            i2 = 0;
        } else {
            i3 = i - Math.max(0, Math.min(this.mScrollingTo, i));
            int max = Math.max(this.mScrollingFrom, Math.min(this.mScrollingTo, i));
            int i4 = this.mTriggerTopmMargin;
            View view2 = this.mHeaderView;
            if (view2 == null || view2.getVisibility() == 8) {
                i2 = this.mTriggerTopmMargin + this.mTriggerBottomMargin + this.mTriggerMeasuredHeight;
                max += i2;
            } else {
                i4 = this.mHeaderTopmMargin + this.mHeaderMeasuredHeight + this.mHeaderBottomMargin + this.mTriggerTopmMargin;
                i2 = 0;
            }
            View view3 = this.mTriggerContentView;
            if (view3 == null) {
                view3 = this.mTriggerView;
            }
            relayoutContent(this.mTriggerView, view3, i4, ((max - this.mTriggerBottomMargin) - this.mTriggerTopmMargin) - this.mTriggerMeasuredHeight);
            float f = ((float) (max - this.mTriggerBottomMargin)) / this.mTriggerContentMinHeight;
            this.mTriggerView.setAlpha(Math.max(0.0f, Math.min(1.0f, f)));
            applyContentAlpha(makeTriggerContentViewList(view3), f - 1.0f);
        }
        View view4 = this.mHeaderView;
        if (!(view4 == null || view4.getVisibility() == 8)) {
            int i5 = this.mHeaderInitTop + this.mHeaderTopmMargin;
            View view5 = this.mHeaderContentView;
            if (view5 == null) {
                view5 = this.mHeaderView;
            }
            relayoutContent(this.mHeaderView, view5, i5, i3);
            float f2 = (float) i3;
            float f3 = this.mHeaderContentMinHeight;
            float f4 = (f2 + f3) / f3;
            this.mHeaderView.setAlpha(Math.max(0.0f, Math.min(1.0f, f4 + 1.0f)));
            applyContentAlpha(makeHeaderContentViewList(view5), f4);
            i2 = this.mHeaderMeasuredHeight + this.mHeaderTopmMargin + this.mHeaderBottomMargin;
        }
        View view6 = this.mScrollableView;
        view6.offsetTopAndBottom((i2 + i) - view6.getTop());
        int i6 = this.mLastScrollingProgress;
        if (i - i6 > 0) {
            checkSendHeaderChangeListener(i6, i, true);
        } else if (i - i6 < 0) {
            checkSendHeaderChangeListener(i6, i, false);
        }
        this.mLastScrollingProgress = i;
        updateHeaderOpen(isHeaderOpen());
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        View view = this.mHeaderView;
        if (view != null) {
            this.mHeaderInitTop = view.getTop();
        }
    }

    public void removeNestedHeaderChangedListener() {
        this.mNestedHeaderChangedListener = null;
    }

    public void setAutoAllClose(boolean z) {
        if (z) {
            int scrollingProgress = getScrollingProgress();
            int i = this.mScrollingFrom;
            if (scrollingProgress > i) {
                autoAdsorption(i);
                return;
            }
        }
        syncScrollingProgress(this.mScrollingFrom);
    }

    public void setAutoAllOpen(boolean z) {
        if (z) {
            int scrollingProgress = getScrollingProgress();
            int i = this.mScrollingTo;
            if (scrollingProgress < i) {
                autoAdsorption(i);
                return;
            }
        }
        syncScrollingProgress(this.mScrollingTo);
    }

    public void setAutoAnim(boolean z) {
        this.mAutoAnim = z;
    }

    public void setAutoHeaderClose(boolean z) {
        if (getHeaderViewVisible()) {
            int scrollingProgress = getScrollingProgress();
            int i = this.mScrollingFrom;
            if (scrollingProgress <= i) {
                return;
            }
            if (z) {
                autoAdsorption(i);
            } else if (getHeaderViewVisible()) {
                syncScrollingProgress(this.mScrollingFrom);
            }
        }
    }

    public void setAutoHeaderOpen(boolean z) {
        if (getHeaderViewVisible() && getScrollingProgress() < 0) {
            if (z) {
                autoAdsorption(0);
            } else {
                syncScrollingProgress(0);
            }
        }
    }

    public void setAutoTriggerClose(boolean z) {
        int i;
        if (!getTriggerViewVisible() || !getHeaderViewVisible() || getScrollingProgress() <= 0) {
            if (getTriggerViewVisible() && !getHeaderViewVisible()) {
                int scrollingProgress = getScrollingProgress();
                int i2 = this.mScrollingFrom;
                if (scrollingProgress > i2) {
                    i = i2;
                }
            }
            i = -1;
        } else {
            i = 0;
        }
        if (i != -1 && z) {
            autoAdsorption(i);
        } else if (i != -1) {
            syncScrollingProgress(i);
        }
    }

    public void setAutoTriggerOpen(boolean z) {
        if (getTriggerViewVisible()) {
            int scrollingProgress = getScrollingProgress();
            int i = this.mScrollingTo;
            if (scrollingProgress >= i) {
                return;
            }
            if (z) {
                autoAdsorption(i);
            } else {
                syncScrollingProgress(i);
            }
        }
    }

    public void setHeaderViewVisible(boolean z) {
        View view = this.mHeaderView;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
            updateScrollingRange(false, false, z);
        }
    }

    public void setNestedHeaderChangedListener(NestedHeaderChangedListener nestedHeaderChangedListener) {
        this.mNestedHeaderChangedListener = nestedHeaderChangedListener;
    }

    public void setTriggerViewVisible(boolean z) {
        View view = this.mTriggerView;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
            updateScrollingRange(false, z, false);
        }
    }
}
