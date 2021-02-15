package miui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import com.miui.internal.widget.ScrollableViewDrawerHelper;
import miui.R;

public class ScrollableViewDrawer extends FrameLayout {
    private static final boolean DEBUG_ENABLED = false;
    private static final String LOG_TAG = "ScrollableViewDrawer";
    private static final int STATE_CLOSED = 0;
    private static final int STATE_DRAGGING = 1;
    private static final int STATE_OPENED = 2;
    private int mActionPointerId;
    private View mContentView;
    private int mContentViewId;
    private boolean mDetermineHorizontal;
    private boolean mDetermineVertical;
    private boolean mDetermineVerticalDrag;
    private int mDividerHeight;
    private float mDownX;
    private float mDownY;
    private boolean mDragEnabled;
    private DrawerListener mDrawerListener;
    private float mLastMotionY;
    private Drawable mScrollDivider;
    private int mScrollableViewId;
    private IScrollableView mScrollableViewWrapper;
    private Scroller mScroller;
    private int mState;
    private View mTargetView;
    private int mTargetViewId;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private int mVerticalRange;
    private int mYOffset;

    class AdapterViewWrapper implements IScrollableView {
        private AdapterView iAdapterView;

        public AdapterViewWrapper(AdapterView adapterView) {
            this.iAdapterView = adapterView;
        }

        public boolean canScroll() {
            StringBuilder sb;
            int firstVisiblePosition = this.iAdapterView.getFirstVisiblePosition();
            String str = ScrollableViewDrawer.LOG_TAG;
            if (firstVisiblePosition > 0) {
                sb = new StringBuilder();
                sb.append("canScrollDown true first visible ");
                sb.append(firstVisiblePosition);
            } else {
                int paddingTop = this.iAdapterView.getPaddingTop();
                int childCount = this.iAdapterView.getChildCount();
                int i = 0;
                while (i < childCount) {
                    if (this.iAdapterView.getChildAt(i).getTop() < paddingTop) {
                        sb = new StringBuilder();
                        sb.append("canScrollDown true ");
                        sb.append(i);
                    } else {
                        i++;
                    }
                }
                return false;
            }
            Log.d(str, sb.toString());
            return true;
        }
    }

    public interface DrawerListener {
        void onDrawerClosed(ScrollableViewDrawer scrollableViewDrawer);

        void onDrawerOpened(ScrollableViewDrawer scrollableViewDrawer);

        void onDrawerSlide(ScrollableViewDrawer scrollableViewDrawer, float f);
    }

    public interface IScrollableView {
        boolean canScroll();
    }

    class ScrollViewWrapper implements IScrollableView {
        private ScrollView iScrollView;

        public ScrollViewWrapper(ScrollView scrollView) {
            this.iScrollView = scrollView;
        }

        public boolean canScroll() {
            return this.iScrollView.getScrollY() > 0;
        }
    }

    public ScrollableViewDrawer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScrollableViewDrawer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mActionPointerId = -1;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mScroller = new Scroller(context);
        this.mVelocityTracker = VelocityTracker.obtain();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ScrollableViewDrawer, i, 0);
        this.mContentViewId = obtainStyledAttributes.getResourceId(R.styleable.ScrollableViewDrawer_contentView, 0);
        if (this.mContentViewId != 0) {
            this.mTargetViewId = obtainStyledAttributes.getResourceId(R.styleable.ScrollableViewDrawer_targetView, 0);
            if (this.mTargetViewId != 0) {
                this.mScrollableViewId = obtainStyledAttributes.getResourceId(R.styleable.ScrollableViewDrawer_scrollableView, 0);
                if (this.mScrollableViewId != 0) {
                    this.mScrollDivider = obtainStyledAttributes.getDrawable(R.styleable.ScrollableViewDrawer_scrollDivider);
                    if (this.mScrollDivider == null) {
                        this.mScrollDivider = ScrollableViewDrawerHelper.getDividerDrawable(context);
                    }
                    Drawable drawable = this.mScrollDivider;
                    if (drawable != null) {
                        this.mDividerHeight = drawable.getIntrinsicHeight();
                    }
                    this.mDragEnabled = obtainStyledAttributes.getBoolean(R.styleable.ScrollableViewDrawer_dragEnabled, true);
                    obtainStyledAttributes.recycle();
                    return;
                }
                throw new IllegalArgumentException("The scrollableView attribute is required and must refer to a valid child.");
            }
            throw new IllegalArgumentException("The targetView attribute is required and must refer to a valid child.");
        }
        throw new IllegalArgumentException("The contentView attribute is required and must refer to a valid child.");
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x001c  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0022  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void computeStateChanged() {
        int i;
        int i2;
        int i3 = this.mYOffset;
        if (i3 == 0) {
            if (!this.mDetermineVerticalDrag) {
                i = 0;
                i2 = this.mState;
                if (i2 != i) {
                    this.mState = i;
                } else if (i2 != 1) {
                    return;
                }
                notifyStateChanged();
            }
        } else if (i3 == getVerticalRange() && !this.mDetermineVerticalDrag) {
            i = 2;
            i2 = this.mState;
            if (i2 != i) {
            }
            notifyStateChanged();
        }
        i = 1;
        i2 = this.mState;
        if (i2 != i) {
        }
        notifyStateChanged();
    }

    private int getVerticalRange() {
        if (this.mVerticalRange == 0) {
            this.mVerticalRange = this.mContentView.getMeasuredHeight();
        }
        return this.mVerticalRange;
    }

    private void notifyStateChanged() {
        DrawerListener drawerListener = this.mDrawerListener;
        if (drawerListener != null) {
            int i = this.mState;
            if (i == 0) {
                drawerListener.onDrawerClosed(this);
            } else if (i == 1) {
                drawerListener.onDrawerSlide(this, ((float) this.mYOffset) / ((float) getVerticalRange()));
            } else if (i == 2) {
                drawerListener.onDrawerOpened(this);
            }
        }
    }

    private void onTranslationYChange() {
        if (this.mDragEnabled) {
            View view = this.mTargetView;
            if (view != null) {
                view.setTranslationY((float) this.mYOffset);
                computeStateChanged();
            }
        }
    }

    private boolean shouldInterceptDown() {
        return this.mTargetView != null && this.mYOffset == 0 && !this.mScrollableViewWrapper.canScroll();
    }

    public void closeDrawer() {
        closeDrawer(false);
    }

    public void closeDrawer(boolean z) {
        if (z) {
            Scroller scroller = this.mScroller;
            int i = this.mYOffset;
            scroller.startScroll(0, i, 0, -i);
        } else {
            this.mYOffset = 0;
            onTranslationYChange();
        }
        invalidate();
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mTargetView != null && this.mScroller.computeScrollOffset()) {
            this.mYOffset = this.mScroller.getCurrY();
            onTranslationYChange();
            postInvalidateOnAnimation();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        float f;
        if (!this.mDragEnabled) {
            return super.dispatchTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        boolean z = true;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActionPointerId);
                    if (findPointerIndex == -1) {
                        this.mActionPointerId = motionEvent.getPointerId(0);
                        findPointerIndex = 0;
                    }
                    float x = motionEvent.getX(findPointerIndex) - this.mDownX;
                    float y = motionEvent.getY(findPointerIndex) - this.mDownY;
                    if (!this.mDetermineVerticalDrag && !this.mDetermineHorizontal) {
                        if (this.mYOffset == getVerticalRange() && y < 0.0f) {
                            this.mDetermineVerticalDrag = true;
                        } else if (y > ((float) this.mTouchSlop)) {
                            this.mDetermineVerticalDrag = shouldInterceptDown();
                        }
                        if (this.mDetermineVerticalDrag) {
                            Log.d(LOG_TAG, "determine drag");
                            ViewParent parent = getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                        if (Math.abs(y) > ((float) this.mTouchSlop) && !this.mDetermineHorizontal) {
                            this.mDetermineVertical = true;
                        }
                        if (Math.abs(x) > ((float) this.mTouchSlop) && !this.mDetermineVertical) {
                            this.mDetermineHorizontal = true;
                        }
                    }
                    if (!this.mDetermineVerticalDrag) {
                        this.mVelocityTracker.addMovement(motionEvent);
                        f = motionEvent.getY(findPointerIndex);
                    }
                } else if (actionMasked != 3) {
                    if (actionMasked == 6) {
                        int actionIndex = motionEvent.getActionIndex();
                        if (motionEvent.getPointerId(actionIndex) == this.mActionPointerId) {
                            int i = actionIndex == 0 ? 1 : 0;
                            this.mActionPointerId = motionEvent.getPointerId(i);
                            this.mDownX = motionEvent.getX(i);
                            f = motionEvent.getY(i);
                            this.mDownY = f;
                        }
                    }
                }
                this.mLastMotionY = f;
            }
            if (!this.mDetermineVerticalDrag) {
                this.mVelocityTracker.clear();
            }
            this.mDetermineVerticalDrag = false;
            this.mDetermineHorizontal = false;
            this.mDetermineVertical = false;
        } else {
            this.mActionPointerId = motionEvent.getPointerId(0);
            this.mDetermineVerticalDrag = false;
            this.mVelocityTracker.clear();
            this.mVelocityTracker.addMovement(motionEvent);
            this.mDownX = motionEvent.getX(0);
            float y2 = motionEvent.getY(0);
            this.mDownY = y2;
            this.mLastMotionY = y2;
            this.mYOffset = (int) this.mTargetView.getTranslationY();
        }
        if (!super.dispatchTouchEvent(motionEvent) && !this.mDetermineVerticalDrag) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        if (view == this.mContentView) {
            int paddingLeft = getPaddingLeft();
            int width = getWidth() - getPaddingRight();
            int top = view.getTop();
            int i = this.mYOffset + top;
            int i2 = this.mDividerHeight;
            if (i2 > 0) {
                this.mScrollDivider.setBounds(paddingLeft, i - i2, width, i);
                this.mScrollDivider.draw(canvas);
                i -= this.mDividerHeight;
            }
            canvas.save();
            canvas.clipRect(paddingLeft, top, width, i);
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        if (view == this.mContentView) {
            canvas.restore();
        }
        return drawChild;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        IScrollableView adapterViewWrapper;
        this.mContentView = findViewById(this.mContentViewId);
        View view = this.mContentView;
        if (view == null) {
            throw new IllegalArgumentException("The contentView attribute is must refer to an existing child.");
        } else if (view.getParent() == this) {
            this.mTargetView = findViewById(this.mTargetViewId);
            View view2 = this.mTargetView;
            if (view2 != null) {
                View findViewById = view2.findViewById(this.mScrollableViewId);
                if (findViewById == null) {
                    throw new IllegalArgumentException("The scrollableView attribute is must refer to an existing child of targetView.");
                } else if (findViewById instanceof IScrollableView) {
                    this.mScrollableViewWrapper = (IScrollableView) findViewById;
                } else {
                    if (findViewById instanceof ScrollView) {
                        adapterViewWrapper = new ScrollViewWrapper((ScrollView) findViewById);
                    } else if (findViewById instanceof AdapterView) {
                        adapterViewWrapper = new AdapterViewWrapper((AdapterView) findViewById);
                    } else {
                        throw new IllegalArgumentException("The scrollableView attribute is must refer to an ScrollView or AdapterView or IScrollableViewWrapper.");
                    }
                    this.mScrollableViewWrapper = adapterViewWrapper;
                }
            } else {
                throw new IllegalArgumentException("The targetView attribute is must refer to an existing child.");
            }
        } else {
            throw new IllegalArgumentException("The contentView attribute is must be a direct child of ScrollableViewDrawer.");
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mDragEnabled && this.mDetermineVerticalDrag;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        int i2;
        Scroller scroller;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                this.mVelocityTracker.addMovement(motionEvent);
                this.mVelocityTracker.computeCurrentVelocity(1000);
                int i3 = this.mYOffset;
                if (!(i3 == 0 || i3 == getVerticalRange())) {
                    if (this.mVelocityTracker.getYVelocity() > 5.0f) {
                        scroller = this.mScroller;
                        i2 = this.mYOffset;
                        i = getVerticalRange() - this.mYOffset;
                    } else {
                        scroller = this.mScroller;
                        i2 = this.mYOffset;
                        i = -i2;
                    }
                    scroller.startScroll(0, i2, 0, i);
                }
                computeStateChanged();
                this.mVelocityTracker.clear();
            } else if (actionMasked == 2) {
                this.mVelocityTracker.addMovement(motionEvent);
                int findPointerIndex = motionEvent.findPointerIndex(this.mActionPointerId);
                if (findPointerIndex == -1) {
                    this.mActionPointerId = motionEvent.getPointerId(0);
                    findPointerIndex = 0;
                }
                float y = motionEvent.getY(findPointerIndex);
                this.mYOffset = Math.max(0, Math.min(((int) (y - this.mLastMotionY)) + this.mYOffset, getVerticalRange()));
                this.mLastMotionY = y;
                onTranslationYChange();
                invalidate();
            } else if (actionMasked == 3) {
                int i4 = this.mYOffset;
                if (!(i4 == 0 || i4 == getVerticalRange())) {
                    Scroller scroller2 = this.mScroller;
                    int i5 = this.mYOffset;
                    scroller2.startScroll(0, i5, 0, -i5);
                }
                computeStateChanged();
                this.mVelocityTracker.clear();
            }
            invalidate();
            this.mVelocityTracker.clear();
        }
        return super.onTouchEvent(motionEvent);
    }

    public void openDrawer() {
        openDrawer(false);
    }

    public void openDrawer(boolean z) {
        if (z) {
            this.mScroller.startScroll(0, this.mYOffset, 0, getVerticalRange() - this.mYOffset);
        } else {
            this.mYOffset = getVerticalRange();
            onTranslationYChange();
        }
        invalidate();
    }

    public void resetState() {
        closeDrawer();
    }

    public void setDragEnabled(boolean z) {
        this.mDragEnabled = z;
    }

    public void setDrawerListener(DrawerListener drawerListener) {
        this.mDrawerListener = drawerListener;
    }
}
