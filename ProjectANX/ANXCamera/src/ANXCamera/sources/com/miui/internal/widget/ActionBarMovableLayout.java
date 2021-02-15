package com.miui.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import miui.app.ActionBar.OnScrollListener;

public class ActionBarMovableLayout extends ActionBarOverlayLayout {
    private static final boolean DBG = false;
    public static final int DEFAULT_SPRING_BACK_DURATION = 800;
    public static final int STATE_DOWN = 1;
    public static final int STATE_UNKNOWN = -1;
    public static final int STATE_UP = 0;
    private static final String TAG = "ActionBarMovableLayout";
    private int mActivePointerId;
    private boolean mFlinging;
    private int mInitialMotionY = -1;
    private boolean mInitialMotionYSet;
    private boolean mIsBeingDragged;
    private boolean mIsSpringBackEnabled = true;
    private float mLastMotionX;
    private float mLastMotionY;
    private final int mMaximumVelocity;
    private final int mMinimumVelocity;
    private int mMotionY;
    private OnScrollListener mOnScrollListener;
    private int mOverScrollDistance;
    private int mScrollRange = -1;
    private int mScrollStart;
    private OverScroller mScroller;
    private int mState = -1;
    private View mTabScrollView;
    private int mTabViewVisibility = 8;
    private final int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public ActionBarMovableLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ActionBarMovableLayout, R.attr.actionBarMovableLayoutStyle, 0);
        if (DeviceHelper.FEATURE_WHOLE_ANIM) {
            this.mOverScrollDistance = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionBarMovableLayout_overScrollRange, 0);
        }
        this.mScrollRange = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionBarMovableLayout_scrollRange, -1);
        this.mInitialMotionY = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionBarMovableLayout_scrollStart, -1);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mScroller = new OverScroller(context);
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        setOverScrollMode(0);
        obtainStyledAttributes.recycle();
    }

    private boolean inChild(View view, int i, int i2) {
        boolean z = false;
        if (view == null) {
            return false;
        }
        int y = (int) view.getY();
        int x = (int) view.getX();
        int y2 = (int) (view.getY() + ((float) view.getHeight()));
        int x2 = (int) (view.getX() + ((float) view.getWidth()));
        if (view == this.mTabScrollView) {
            int top = this.mActionBarTop.getTop();
            y += top;
            y2 += top;
        }
        if (i2 >= y && i2 < y2 && i >= x && i < x2) {
            z = true;
        }
        return z;
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private boolean isTabViewVisibilityChanged() {
        ensureTabScrollView();
        View view = this.mTabScrollView;
        if (view != null) {
            int visibility = view.getVisibility();
            if (visibility != this.mTabViewVisibility) {
                this.mTabViewVisibility = visibility;
                return true;
            }
        }
        return false;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & 65280) >> 8;
        if (motionEvent.getPointerId(action) == this.mActivePointerId) {
            int i = action == 0 ? 1 : 0;
            this.mLastMotionY = (float) ((int) motionEvent.getY(i));
            this.mActivePointerId = motionEvent.getPointerId(i);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    /* access modifiers changed from: protected */
    public void applyTranslationY(float f) {
        float motionToTranslation = motionToTranslation(f);
        this.mContentView.setTranslationY(motionToTranslation);
        ensureTabScrollView();
        View view = this.mTabScrollView;
        if (view != null) {
            view.setTranslationY(motionToTranslation);
        }
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int i = this.mMotionY;
            int currY = this.mScroller.getCurrY();
            if (i != currY) {
                overScrollBy(0, currY - i, 0, this.mMotionY, 0, getScrollRange(), 0, getOverScrollDistance(), true);
            }
            postInvalidateOnAnimation();
        } else if (this.mFlinging) {
            springBack();
            this.mFlinging = false;
        }
    }

    /* access modifiers changed from: protected */
    public int computeVerticalScrollExtent() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int computeVerticalScrollRange() {
        return getScrollRange();
    }

    /* access modifiers changed from: protected */
    public int computeVerticalVelocity() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
        return (int) velocityTracker.getYVelocity(this.mActivePointerId);
    }

    /* access modifiers changed from: 0000 */
    public void ensureTabScrollView() {
        this.mTabScrollView = this.mActionBarTop.getTabContainer();
    }

    /* access modifiers changed from: protected */
    public void fling(int i) {
        int overScrollDistance = getOverScrollDistance();
        this.mScroller.fling(0, this.mMotionY, 0, i, 0, 0, 0, getScrollRange(), 0, overScrollDistance);
        this.mFlinging = true;
        postInvalidate();
    }

    public int getOverScrollDistance() {
        if (DeviceHelper.FEATURE_WHOLE_ANIM) {
            return this.mOverScrollDistance;
        }
        return 0;
    }

    public int getScrollRange() {
        return this.mScrollRange;
    }

    public int getScrollStart() {
        return this.mScrollStart;
    }

    /* access modifiers changed from: protected */
    public void measureChildWithMargins(View view, int i, int i2, int i3, int i4) {
        if (view != this.mContentView) {
            super.measureChildWithMargins(view, i, i2, i3, i4);
            return;
        }
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        view.measure(FrameLayout.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i2, marginLayoutParams.width), FrameLayout.getChildMeasureSpec(i3, ((((((getPaddingTop() + getPaddingBottom()) + marginLayoutParams.bottomMargin) + this.mActionBarView.getMeasuredHeight()) + ((MarginLayoutParams) this.mActionBarView.getLayoutParams()).topMargin) - getScrollRange()) - getOverScrollDistance()) - this.mScrollStart, marginLayoutParams.height));
    }

    /* access modifiers changed from: protected */
    public float motionToTranslation(float f) {
        float f2 = ((((float) (-this.mOverScrollDistance)) + f) - ((float) this.mScrollRange)) - ((float) this.mScrollStart);
        ensureTabScrollView();
        View view = this.mTabScrollView;
        return (view == null || view.getVisibility() != 0) ? f2 : f2 - ((float) this.mTabScrollView.getHeight());
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        View contentMask = getContentMask();
        if (contentMask != null && contentMask.getVisibility() == 0) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        int i = action & 255;
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 6) {
                            onSecondaryPointerUp(motionEvent);
                        }
                    }
                } else if (shouldStartScroll(motionEvent)) {
                    this.mIsBeingDragged = true;
                    initVelocityTrackerIfNotExists();
                    this.mVelocityTracker.addMovement(motionEvent);
                    onScrollBegin();
                }
            }
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            recycleVelocityTracker();
            onScrollEnd();
        } else {
            this.mLastMotionY = motionEvent.getY();
            this.mLastMotionX = motionEvent.getX();
            this.mActivePointerId = motionEvent.getPointerId(0);
            initOrResetVelocityTracker();
            this.mVelocityTracker.addMovement(motionEvent);
            this.mScroller.forceFinished(true);
        }
        return this.mIsBeingDragged;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        boolean z2 = !this.mInitialMotionYSet || isTabViewVisibilityChanged();
        if (!this.mInitialMotionYSet) {
            if (this.mInitialMotionY < 0) {
                this.mInitialMotionY = this.mScrollRange;
            }
            this.mMotionY = this.mInitialMotionY;
            this.mInitialMotionYSet = true;
        }
        if (z2) {
            applyTranslationY((float) this.mMotionY);
        }
    }

    /* access modifiers changed from: protected */
    public void onOverScrolled(int i, int i2, boolean z, boolean z2) {
        onScroll((float) i2);
        this.mMotionY = i2;
        if (this.mMotionY == 0 && z2) {
            int computeVerticalVelocity = computeVerticalVelocity();
            if (Math.abs(computeVerticalVelocity) > this.mMinimumVelocity * 2) {
                OnScrollListener onScrollListener = this.mOnScrollListener;
                if (onScrollListener != null) {
                    onScrollListener.onFling(((float) (-computeVerticalVelocity)) * 0.2f, 500);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onScroll(float f) {
        applyTranslationY(f);
        OnScrollListener onScrollListener = this.mOnScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScroll(this.mState, f / ((float) this.mScrollRange));
        }
    }

    /* access modifiers changed from: protected */
    public void onScrollBegin() {
        OnScrollListener onScrollListener = this.mOnScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onStartScroll();
        }
    }

    /* access modifiers changed from: protected */
    public void onScrollEnd() {
        this.mState = -1;
        OnScrollListener onScrollListener = this.mOnScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onStopScroll();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        MotionEvent motionEvent2 = motionEvent;
        initVelocityTrackerIfNotExists();
        this.mVelocityTracker.addMovement(motionEvent2);
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                    if (action != 3) {
                        if (action == 5) {
                            int actionIndex = motionEvent.getActionIndex();
                            this.mLastMotionY = (float) ((int) motionEvent2.getY(actionIndex));
                            i = motionEvent2.getPointerId(actionIndex);
                        } else if (action == 6) {
                            onSecondaryPointerUp(motionEvent);
                            this.mLastMotionY = (float) ((int) motionEvent2.getY(motionEvent2.findPointerIndex(this.mActivePointerId)));
                        }
                    }
                } else if (this.mIsBeingDragged) {
                    int findPointerIndex = motionEvent2.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex == -1) {
                        return false;
                    }
                    float y = motionEvent2.getY(findPointerIndex);
                    boolean overScrollBy = overScrollBy(0, (int) (y - this.mLastMotionY), 0, this.mMotionY, 0, getScrollRange(), 0, getOverScrollDistance(), true);
                    this.mLastMotionY = y;
                    if (overScrollBy) {
                        if (this.mMotionY == 0) {
                            this.mIsBeingDragged = false;
                            this.mActivePointerId = -1;
                            motionEvent2.setAction(0);
                            dispatchTouchEvent(motionEvent);
                        }
                        this.mVelocityTracker.clear();
                    }
                } else if (shouldStartScroll(motionEvent)) {
                    this.mIsBeingDragged = true;
                    initVelocityTrackerIfNotExists();
                    this.mVelocityTracker.addMovement(motionEvent2);
                    onScrollBegin();
                }
                return true;
            }
            if (this.mIsBeingDragged) {
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                int computeVerticalVelocity = computeVerticalVelocity();
                if (Math.abs(computeVerticalVelocity) > this.mMinimumVelocity) {
                    fling(computeVerticalVelocity);
                } else {
                    if (this.mScroller.springBack(0, this.mMotionY, 0, 0, 0, getScrollRange())) {
                        invalidate();
                    } else {
                        springBack();
                    }
                }
            }
            return true;
        }
        this.mLastMotionY = motionEvent.getY();
        i = motionEvent2.getPointerId(0);
        this.mActivePointerId = i;
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        int overScrollMode = getOverScrollMode();
        boolean z2 = true;
        boolean z3 = overScrollMode == 0 || (overScrollMode == 1 && (computeVerticalScrollRange() > computeVerticalScrollExtent()));
        int i9 = i2 + i4;
        if (!z3) {
            i8 = 0;
        }
        int i10 = i8 + i6;
        if (i9 <= i10) {
            if (i9 < 0) {
                i10 = 0;
            } else {
                i10 = i9;
                z2 = false;
            }
        }
        onOverScrolled(0, i10, false, z2);
        return z2;
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
    }

    public void setInitialMotionY(int i) {
        this.mInitialMotionY = i;
    }

    public void setMotionY(int i) {
        this.mMotionY = i;
        onScroll((float) i);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setOverScrollDistance(int i) {
        if (DeviceHelper.FEATURE_WHOLE_ANIM) {
            this.mOverScrollDistance = i;
        }
    }

    public void setScrollRange(int i) {
        this.mScrollRange = i;
    }

    public void setScrollStart(int i) {
        this.mScrollStart = i;
    }

    public void setSpringBackEnabled(boolean z) {
        this.mIsSpringBackEnabled = z;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005d, code lost:
        if (r3.onContentScrolled() != false) goto L_0x0075;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0070, code lost:
        if (r3.onContentScrolled() != false) goto L_0x0075;
     */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0078  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldStartScroll(MotionEvent motionEvent) {
        boolean z;
        int i = this.mActivePointerId;
        int i2 = 0;
        if (i == -1) {
            return false;
        }
        int findPointerIndex = motionEvent.findPointerIndex(i);
        if (findPointerIndex == -1) {
            Log.w(TAG, "invalid pointer index");
            return false;
        }
        float x = motionEvent.getX(findPointerIndex);
        float y = motionEvent.getY(findPointerIndex);
        int i3 = (int) (y - this.mLastMotionY);
        int abs = Math.abs(i3);
        int abs2 = (int) Math.abs(x - this.mLastMotionX);
        int i4 = (int) x;
        int i5 = (int) y;
        boolean z2 = inChild(this.mContentView, i4, i5) || inChild(this.mTabScrollView, i4, i5);
        if (z2 && abs > this.mTouchSlop && abs > abs2) {
            int i6 = this.mMotionY;
            if (i6 == 0) {
                if (i3 >= 0) {
                    OnScrollListener onScrollListener = this.mOnScrollListener;
                    if (onScrollListener != null) {
                    }
                }
            } else if (i3 > 0 && i6 >= getOverScrollDistance()) {
                OnScrollListener onScrollListener2 = this.mOnScrollListener;
                if (onScrollListener2 != null) {
                }
            }
            z = true;
            if (z) {
                this.mLastMotionY = y;
                this.mLastMotionX = x;
                if (i3 > 0) {
                    i2 = 1;
                }
                this.mState = i2;
                ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }
            return z;
        }
        z = false;
        if (z) {
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void springBack() {
        if (this.mIsSpringBackEnabled) {
            int scrollRange = getScrollRange();
            int i = this.mMotionY;
            this.mScroller.startScroll(0, this.mMotionY, 0, i > scrollRange / 2 ? scrollRange - i : -i, 800);
            postInvalidateOnAnimation();
        }
    }
}
