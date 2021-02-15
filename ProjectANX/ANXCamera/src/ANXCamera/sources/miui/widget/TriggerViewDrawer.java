package miui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import miui.R;
import miui.util.HapticFeedbackUtil;
import miui.view.MiuiHapticFeedbackConstants;

public class TriggerViewDrawer extends RelativeLayout {
    private static final boolean DEBUG_ENABLED = false;
    private static final int DETECT_MODE_DONE = 2;
    private static final int DETECT_MODE_OFF = 0;
    private static final int DETECT_MODE_ON = 1;
    private static final float FRICTION = 2.0f;
    private static final String LOG_TAG = "TriggerViewDrawer";
    private static final int TOUCH_MODE_NONE = 0;
    private static final int TOUCH_MODE_SCROLL = 2;
    private static final int TOUCH_MODE_TRANSLATE = 1;
    private int mActionPointerId;
    private boolean mAutoClose;
    private boolean mCancelForNestedScroll;
    private int mContentDistance;
    private View mContentView;
    private int mContentViewId;
    private int mDetectMode;
    private int mDividerHeight;
    private View mDownView;
    private float mDownX;
    private float mDownY;
    private boolean mDragEnabled;
    private DrawerListener mDrawerListener;
    private float mLastY;
    private float mModeScrollDownY;
    private final int[] mScrollConsumed;
    private Drawable mScrollDivider;
    private final int[] mScrollOffset;
    private int mScrollableViewId;
    private ScrollableView mScrollableViewWrapper;
    private Scroller mScroller;
    private boolean mScrolling;
    private View mTargetView;
    private View mTargetViewAncestor;
    private int mTargetViewId;
    private int mTouchMode;
    private int mTouchSlop;
    private int mTriggerDistance;
    private TriggerListener mTriggerListener;
    private boolean mTriggerOn;
    private View mTriggerView;
    private int mTriggerViewId;
    private VelocityTracker mVelocityTracker;

    class AdapterViewWrapper implements ScrollableView {
        private AdapterView iAdapterView;

        public AdapterViewWrapper(AdapterView adapterView) {
            this.iAdapterView = adapterView;
        }

        public boolean canScrollDown() {
            StringBuilder sb;
            int firstVisiblePosition = this.iAdapterView.getFirstVisiblePosition();
            String str = TriggerViewDrawer.LOG_TAG;
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
        void onDrawerClose(TriggerViewDrawer triggerViewDrawer);

        void onDrawerOpen(TriggerViewDrawer triggerViewDrawer);

        void onDrawerSlide(TriggerViewDrawer triggerViewDrawer, float f);
    }

    class FixedViewWrapper implements ScrollableView {
        public boolean canScrollDown() {
            return false;
        }
    }

    class ScrollViewWrapper implements ScrollableView {
        private ScrollView iScrollView;

        public ScrollViewWrapper(ScrollView scrollView) {
            this.iScrollView = scrollView;
        }

        public boolean canScrollDown() {
            return this.iScrollView.getScrollY() > 0;
        }
    }

    public interface ScrollableView {
        boolean canScrollDown();
    }

    public interface TriggerListener {
        void onTriggerOpen(TriggerViewDrawer triggerViewDrawer);

        void onTriggerSlide(TriggerViewDrawer triggerViewDrawer, float f);
    }

    public TriggerViewDrawer(Context context) {
        this(context, null);
    }

    public TriggerViewDrawer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.triggerViewDrawerStyle);
    }

    public TriggerViewDrawer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mActionPointerId = -1;
        this.mModeScrollDownY = -1.0f;
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mCancelForNestedScroll = false;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mScroller = new Scroller(context);
        this.mVelocityTracker = VelocityTracker.obtain();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TriggerViewDrawer, i, R.style.Widget_TriggerViewDrawer);
        this.mContentViewId = obtainStyledAttributes.getResourceId(R.styleable.TriggerViewDrawer_contentView, 0);
        this.mTargetViewId = obtainStyledAttributes.getResourceId(R.styleable.TriggerViewDrawer_targetView, 0);
        if (this.mTargetViewId != 0) {
            this.mScrollableViewId = obtainStyledAttributes.getResourceId(R.styleable.TriggerViewDrawer_scrollableView, 0);
            this.mTriggerViewId = obtainStyledAttributes.getResourceId(R.styleable.TriggerViewDrawer_triggerView, 0);
            this.mTriggerDistance = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TriggerViewDrawer_triggerDistance, 0);
            this.mScrollDivider = obtainStyledAttributes.getDrawable(R.styleable.TriggerViewDrawer_scrollDivider);
            Drawable drawable = this.mScrollDivider;
            if (drawable != null) {
                this.mDividerHeight = drawable.getIntrinsicHeight();
            }
            this.mDragEnabled = obtainStyledAttributes.getBoolean(R.styleable.TriggerViewDrawer_dragEnabled, false);
            obtainStyledAttributes.recycle();
            return;
        }
        throw new IllegalArgumentException("The targetView attribute is required and must refer to a valid child.");
    }

    private void dispatchTouchEventToChild(View view, MotionEvent motionEvent) {
        dispatchTouchEventToChild(view, motionEvent, (float) (-view.getLeft()), (float) (-view.getTop()));
    }

    private void dispatchTouchEventToChild(View view, MotionEvent motionEvent, float f, float f2) {
        motionEvent.offsetLocation(f, f2);
        view.dispatchTouchEvent(motionEvent);
        motionEvent.offsetLocation(-f, -f2);
    }

    private void dispatchTouchEventToChild(View view, MotionEvent motionEvent, float f, float f2, int i) {
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(i);
        dispatchTouchEventToChild(view, obtain, f, f2);
        obtain.recycle();
    }

    private void dispatchTouchEventToChild(View view, MotionEvent motionEvent, int i) {
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(i);
        dispatchTouchEventToChild(view, obtain);
        obtain.recycle();
    }

    private boolean drawContentView(Canvas canvas, View view, long j) {
        int translationY = (int) this.mTargetView.getTranslationY();
        if (translationY > this.mContentDistance) {
            return super.drawChild(canvas, view, j);
        }
        int width = getWidth();
        int i = this.mDividerHeight;
        if (i > 0) {
            this.mScrollDivider.setBounds(0, translationY - i, width, translationY);
            this.mScrollDivider.draw(canvas);
            translationY -= this.mDividerHeight;
        }
        canvas.save();
        canvas.clipRect(0, 0, width, translationY);
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    private boolean drawTriggerView(Canvas canvas, View view, long j) {
        int translationY = (int) this.mTargetView.getTranslationY();
        if (translationY < this.mTriggerDistance - view.getHeight()) {
            return true;
        }
        if (translationY > this.mTriggerDistance) {
            return super.drawChild(canvas, view, j);
        }
        int width = getWidth();
        int y = (int) view.getY();
        canvas.save();
        canvas.clipRect(0, y, width, translationY);
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    private View getTouchDownView(int i, int i2) {
        Rect rect = new Rect();
        View[] viewArr = {this.mTargetViewAncestor, this.mContentView};
        int length = viewArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            View view = viewArr[i3];
            if (view != null && view.getVisibility() == 0) {
                int x = (int) view.getX();
                int y = (int) view.getY();
                rect.set(x, y, view.getWidth() + x, view.getHeight() + y);
                if (rect.contains(i, i2)) {
                    return view;
                }
            }
        }
        return null;
    }

    private boolean isDistanceInvalid() {
        int i = this.mTriggerDistance;
        return i > 0 && i <= this.mContentDistance;
    }

    private void notifyDrawerStateChanged() {
        if (this.mDrawerListener != null && this.mContentView != null) {
            int translationY = (int) this.mTargetView.getTranslationY();
            if (this.mDetectMode == 2) {
                this.mDrawerListener.onDrawerSlide(this, ((float) Math.min(this.mContentDistance, Math.max(translationY, 0))) / ((float) this.mContentDistance));
            } else if (translationY == 0) {
                this.mDrawerListener.onDrawerClose(this);
            } else {
                int i = this.mContentDistance;
                if (translationY == i) {
                    this.mDrawerListener.onDrawerOpen(this);
                } else {
                    this.mDrawerListener.onDrawerSlide(this, ((float) translationY) / ((float) i));
                }
            }
        }
    }

    private void notifyTriggerStateChanged() {
        if (this.mTriggerListener != null) {
            int i = this.mTriggerDistance;
            if (i > 0 && this.mDetectMode == 2) {
                int i2 = i - this.mContentDistance;
                int translationY = ((int) this.mTargetView.getTranslationY()) - this.mContentDistance;
                this.mTriggerListener.onTriggerSlide(this, ((float) translationY) / ((float) i2));
                if (translationY >= i2) {
                    if (!this.mTriggerOn) {
                        this.mTriggerOn = true;
                        this.mTriggerListener.onTriggerOpen(this);
                        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TRIGGER_DRAWER)) {
                            performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TRIGGER_DRAWER);
                        }
                    }
                } else if (this.mTriggerOn) {
                    this.mTriggerOn = false;
                }
            }
        }
    }

    private void offsetTranslationAnimation(float f) {
        int translationY = (int) this.mTargetView.getTranslationY();
        int i = this.mContentDistance;
        float f2 = (translationY > i ? ((float) i) + (((float) (translationY - i)) * 2.0f) : (float) translationY) + f;
        int i2 = this.mContentDistance;
        updateTranslationAnimation(f2 > ((float) i2) ? i2 + ((int) ((f2 - ((float) i2)) / 2.0f)) : (int) f2);
        invalidate();
    }

    private void startScroll(int i, int i2) {
        this.mScroller.startScroll(0, i, 0, i2);
        this.mScrolling = true;
        invalidate();
    }

    private void updateTranslationAnimation(int i) {
        if (this.mTriggerDistance <= 0) {
            i = Math.min(this.mContentDistance, i);
        }
        this.mTargetView.setTranslationY((float) i);
        notifyTriggerStateChanged();
    }

    public void closeDrawer() {
        closeDrawer(false);
    }

    public void closeDrawer(boolean z) {
        if (z) {
            int translationY = (int) this.mTargetView.getTranslationY();
            startScroll(translationY, -translationY);
            return;
        }
        updateTranslationAnimation(0);
        notifyDrawerStateChanged();
        invalidate();
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mTargetView == null) {
            return;
        }
        if (this.mScroller.computeScrollOffset()) {
            updateTranslationAnimation(this.mScroller.getCurrY());
            postInvalidateOnAnimation();
        } else if (this.mScrolling) {
            this.mScrolling = false;
            notifyDrawerStateChanged();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0149, code lost:
        if (r1.canScrollDown() != false) goto L_0x014b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x015d, code lost:
        if (r6.mScrollableViewWrapper != null) goto L_0x014b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0238  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0241  */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x0251  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0164  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x017c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int translationY;
        int i;
        int i2;
        MotionEvent motionEvent2 = motionEvent;
        if (!this.mDragEnabled || isDistanceInvalid() || ((this.mContentDistance <= 0 && this.mTriggerDistance <= 0) || (motionEvent.getActionMasked() != 0 && this.mDetectMode == 0))) {
            return super.dispatchTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int findPointerIndex = motionEvent2.findPointerIndex(this.mActionPointerId);
                    if (findPointerIndex == -1) {
                        this.mActionPointerId = motionEvent2.getPointerId(0);
                        findPointerIndex = 0;
                    }
                    float x = motionEvent2.getX(findPointerIndex);
                    float y = motionEvent2.getY(findPointerIndex);
                    float abs = Math.abs(x - this.mDownX);
                    float abs2 = Math.abs(y - this.mDownY);
                    if (this.mDetectMode == 1) {
                        if (abs > ((float) this.mTouchSlop) && abs > abs2) {
                            this.mDetectMode = 0;
                        } else if (abs2 > ((float) this.mTouchSlop)) {
                            this.mDetectMode = 2;
                            getParent().requestDisallowInterceptTouchEvent(true);
                            View view = this.mContentView;
                            if (view != null && this.mDownView == view) {
                                dispatchTouchEventToChild(view, motionEvent2, 3);
                            }
                        }
                    }
                    float f = y - this.mLastY;
                    if (this.mDetectMode == 2 && (f >= 1.0f || f <= -1.0f)) {
                        boolean z = this.mTargetView.getTranslationY() <= 0.0f;
                        if (VERSION.SDK_INT < 21 || !z || !dispatchNestedPreScroll(0, (int) (this.mLastY - y), this.mScrollConsumed, this.mScrollOffset)) {
                            this.mCancelForNestedScroll = false;
                            int i3 = this.mTouchMode;
                            if (f > 0.0f) {
                                if (this.mTriggerDistance > 0 || this.mTargetView.getTranslationY() < ((float) this.mContentDistance)) {
                                    ScrollableView scrollableView = this.mScrollableViewWrapper;
                                    if (scrollableView != null) {
                                    }
                                    i2 = 1;
                                    if (i2 != 1) {
                                        int i4 = this.mTouchMode;
                                        if (i4 == 2 || (this.mDownView == this.mTargetViewAncestor && i4 == 0)) {
                                            dispatchTouchEventToChild(this.mTargetViewAncestor, motionEvent2, 3);
                                        }
                                        offsetTranslationAnimation(f);
                                        notifyDrawerStateChanged();
                                    } else if (i2 == 2) {
                                        int i5 = this.mTouchMode;
                                        if (i5 == 1 || (this.mDownView != this.mTargetViewAncestor && i5 == 0)) {
                                            dispatchTouchEventToChild(this.mTargetViewAncestor, motionEvent, this.mDownX - motionEvent.getX(), 0.0f, 0);
                                            this.mModeScrollDownY = motionEvent.getY();
                                        } else {
                                            dispatchTouchEventToChild(this.mTargetViewAncestor, motionEvent2, this.mDownX - motionEvent.getX(), 0.0f);
                                        }
                                    }
                                    this.mTouchMode = i2;
                                    this.mLastY = y;
                                }
                            } else if (f < 0.0f) {
                                if (this.mTargetView.getTranslationY() <= 0.0f) {
                                }
                                i2 = 1;
                                if (i2 != 1) {
                                }
                                this.mTouchMode = i2;
                                this.mLastY = y;
                            } else {
                                i2 = i3;
                                if (i2 != 1) {
                                }
                                this.mTouchMode = i2;
                                this.mLastY = y;
                            }
                            i2 = 2;
                            if (i2 != 1) {
                            }
                            this.mTouchMode = i2;
                            this.mLastY = y;
                        } else {
                            this.mLastY = y - ((float) this.mScrollOffset[1]);
                            this.mCancelForNestedScroll = true;
                            this.mTouchMode = 0;
                            this.mTargetViewAncestor.setPressed(false);
                            this.mTargetViewAncestor.cancelLongPress();
                            this.mTargetViewAncestor.cancelPendingInputEvents();
                            if (Math.abs(motionEvent.getY() - this.mModeScrollDownY) < ((float) this.mTouchSlop)) {
                            }
                            this.mModeScrollDownY = -1.0f;
                            if (this.mDetectMode == 2) {
                            }
                            return true;
                        }
                    }
                } else if (actionMasked != 3) {
                    if (actionMasked == 6) {
                        int actionIndex = motionEvent.getActionIndex();
                        if (motionEvent2.getPointerId(actionIndex) == this.mActionPointerId) {
                            int i6 = actionIndex == 0 ? 1 : 0;
                            this.mActionPointerId = motionEvent2.getPointerId(i6);
                            this.mDownX = motionEvent2.getX(i6);
                            float y2 = motionEvent2.getY(i6);
                            this.mDownY = y2;
                            this.mLastY = y2;
                        }
                    }
                    if (this.mModeScrollDownY > 0.0f && motionEvent.getActionMasked() == 1) {
                        if (Math.abs(motionEvent.getY() - this.mModeScrollDownY) < ((float) this.mTouchSlop)) {
                            motionEvent2.setAction(3);
                        }
                        this.mModeScrollDownY = -1.0f;
                    }
                    if (this.mDetectMode == 2) {
                        if (this.mCancelForNestedScroll && motionEvent.getActionMasked() == 1) {
                            motionEvent2.setAction(3);
                            this.mCancelForNestedScroll = false;
                        }
                        super.dispatchTouchEvent(motionEvent);
                    } else if (motionEvent.getActionMasked() != 2 && this.mTouchMode == 2) {
                        dispatchTouchEventToChild(this.mTargetViewAncestor, motionEvent2);
                    }
                    return true;
                } else {
                    translationY = (int) this.mTargetView.getTranslationY();
                    if (translationY != 0 && (this.mAutoClose || translationY != this.mContentDistance)) {
                        i = -translationY;
                    }
                    this.mVelocityTracker.clear();
                }
                this.mVelocityTracker.addMovement(motionEvent2);
                if (Math.abs(motionEvent.getY() - this.mModeScrollDownY) < ((float) this.mTouchSlop)) {
                }
                this.mModeScrollDownY = -1.0f;
                if (this.mDetectMode == 2) {
                }
                return true;
            }
            translationY = (int) this.mTargetView.getTranslationY();
            if (!this.mAutoClose || translationY == 0) {
                if (!(translationY == 0 || translationY == this.mContentDistance)) {
                    this.mVelocityTracker.addMovement(motionEvent2);
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    if (this.mVelocityTracker.getYVelocity() > 5.0f) {
                        i = this.mContentDistance - translationY;
                    }
                }
                this.mVelocityTracker.clear();
            }
            i = -translationY;
            startScroll(translationY, i);
            this.mVelocityTracker.clear();
        } else {
            this.mActionPointerId = motionEvent2.getPointerId(0);
            this.mVelocityTracker.clear();
            this.mVelocityTracker.addMovement(motionEvent2);
            this.mDownX = motionEvent2.getX(0);
            float y3 = motionEvent2.getY(0);
            this.mLastY = y3;
            this.mDownY = y3;
            this.mDownView = getTouchDownView((int) this.mDownX, (int) this.mDownY);
        }
        this.mDetectMode = 1;
        this.mTouchMode = 0;
        this.mTriggerOn = false;
        if (Math.abs(motionEvent.getY() - this.mModeScrollDownY) < ((float) this.mTouchSlop)) {
        }
        this.mModeScrollDownY = -1.0f;
        if (this.mDetectMode == 2) {
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        return view == this.mContentView ? drawContentView(canvas, view, j) : view == this.mTriggerView ? drawTriggerView(canvas, view, j) : super.drawChild(canvas, view, j);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        ScrollableView fixedViewWrapper;
        ScrollableView adapterViewWrapper;
        super.onFinishInflate();
        int i = this.mContentViewId;
        if (i > 0) {
            this.mContentView = findViewById(i);
            View view = this.mContentView;
            if (view == null) {
                throw new IllegalArgumentException("The contentView attribute is must refer to an existing child.");
            } else if (view.getParent() != this) {
                throw new IllegalArgumentException("The contentView attribute is must be a direct child of TriggerViewDrawer.");
            }
        }
        this.mTargetView = findViewById(this.mTargetViewId);
        View view2 = this.mTargetView;
        if (view2 != null) {
            while (true) {
                this.mTargetViewAncestor = view2;
                View view3 = this.mTargetViewAncestor;
                if (view3 == null || view3.getParent() == this) {
                    int i2 = this.mScrollableViewId;
                } else {
                    view2 = (View) this.mTargetViewAncestor.getParent();
                }
            }
            int i22 = this.mScrollableViewId;
            if (i22 > 0) {
                View findViewById = this.mTargetView.findViewById(i22);
                if (findViewById != null) {
                    if (findViewById instanceof ScrollableView) {
                        fixedViewWrapper = (ScrollableView) findViewById;
                    } else {
                        if (findViewById instanceof ScrollView) {
                            adapterViewWrapper = new ScrollViewWrapper((ScrollView) findViewById);
                        } else if (findViewById instanceof AdapterView) {
                            adapterViewWrapper = new AdapterViewWrapper((AdapterView) findViewById);
                        } else {
                            Log.w(LOG_TAG, "The scrollableView is a fixed view that can't scroll all the time");
                            fixedViewWrapper = new FixedViewWrapper();
                        }
                        this.mScrollableViewWrapper = adapterViewWrapper;
                    }
                    this.mScrollableViewWrapper = fixedViewWrapper;
                } else {
                    throw new IllegalArgumentException("The scrollableView attribute is must refer to an existing child of targetView.");
                }
            }
            int i3 = this.mTriggerViewId;
            if (i3 > 0) {
                this.mTriggerView = findViewById(i3);
                View view4 = this.mTriggerView;
                if (view4 == null) {
                    throw new IllegalArgumentException("The triggerView attribute is must refer to an existing child.");
                } else if (view4.getParent() != this) {
                    throw new IllegalArgumentException("The triggerView attribute is must be a direct child of TriggerViewDrawer.");
                }
            }
        } else {
            throw new IllegalArgumentException("The targetView attribute is must refer to an existing child.");
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        View view = this.mContentView;
        if (view != null) {
            this.mContentDistance = ((int) view.getY()) + this.mContentView.getHeight();
        }
        View view2 = this.mTriggerView;
        if (view2 != null) {
            this.mTriggerDistance = ((int) view2.getY()) + this.mTriggerView.getHeight();
        }
        if (isDistanceInvalid()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Height of trigger must bigger than height of content, mTriggerDistance=");
            sb.append(this.mTriggerDistance);
            sb.append(", mContentDistance=");
            sb.append(this.mContentDistance);
            Log.w(LOG_TAG, sb.toString());
        }
    }

    public void onNestedScrollAccepted(View view, View view2, int i) {
        super.onNestedScrollAccepted(view, view2, i);
        if (VERSION.SDK_INT >= 21) {
            startNestedScroll(2);
        }
    }

    public boolean onStartNestedScroll(View view, View view2, int i) {
        return i == 2 && (this.mTargetView.getTranslationY() <= 0.0f);
    }

    public void openDrawer() {
        openDrawer(false);
    }

    public void openDrawer(boolean z) {
        if (z) {
            int translationY = (int) this.mTargetView.getTranslationY();
            startScroll(translationY, this.mContentDistance - translationY);
            return;
        }
        updateTranslationAnimation(this.mContentDistance);
        notifyDrawerStateChanged();
        invalidate();
    }

    public void setAutoClose(boolean z) {
        this.mAutoClose = z;
    }

    public void setDragEnabled(boolean z) {
        this.mDragEnabled = z;
    }

    public void setDrawerListener(DrawerListener drawerListener) {
        this.mDrawerListener = drawerListener;
    }

    public void setNestedScrollingEnabled(boolean z) {
        super.setNestedScrollingEnabled(z);
        if (VERSION.SDK_INT >= 21 && z) {
            this.mTargetView.findViewById(this.mScrollableViewId).setNestedScrollingEnabled(true);
        }
    }

    public void setTriggerDistance(int i) {
        this.mTriggerDistance = i;
    }

    public void setTriggerListener(TriggerListener triggerListener) {
        this.mTriggerListener = triggerListener;
    }
}
