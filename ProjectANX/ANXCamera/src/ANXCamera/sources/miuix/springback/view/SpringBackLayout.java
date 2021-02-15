package miuix.springback.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.widget.ListViewCompat;
import androidx.core.widget.NestedScrollView;
import java.util.ArrayList;
import java.util.List;
import miuix.core.view.NestedCurrentFling;
import miuix.springback.R;

public class SpringBackLayout extends ViewGroup implements NestedScrollingParent3, NestedScrollingChild3, NestedCurrentFling {
    public static final int ANGLE = 4;
    public static final int HORIZONTAL = 1;
    private static final int INVALID_ID = -1;
    private static final int INVALID_POINTER = -1;
    private static final int MAX_FLING_CONSUME_COUNTER = 4;
    public static final int SPRING_BACK_BOTTOM = 2;
    public static final int SPRING_BACK_TOP = 1;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "SpringBackLayout";
    public static final int UNCHECK_ORIENTATION = 0;
    private static final int VELOCITY_THRADHOLD = 2000;
    public static final int VERTICAL = 2;
    private int consumeNestFlingCounter;
    private int mActivePointerId;
    private SpringBackLayoutHelper mHelper;
    private float mInitialDownX;
    private float mInitialDownY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private boolean mNestedFlingInProgress;
    private int mNestedScrollAxes;
    private boolean mNestedScrollInProgress;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final int[] mNestedScrollingV2ConsumedCompat;
    private List mOnScrollListeners;
    private OnSpringListener mOnSpringListener;
    private int mOriginScrollOrientation;
    private final int[] mParentOffsetInWindow;
    private final int[] mParentScrollConsumed;
    private final int mScreenHeight;
    private final int mScreenWith;
    private boolean mScrollByFling;
    private int mScrollOrientation;
    private int mScrollState;
    private boolean mSpringBackEnable;
    private int mSpringBackMode;
    private SpringScroller mSpringScroller;
    private View mTarget;
    private int mTargetId;
    private float mTotalFlingUnconsumed;
    private float mTotalScrollBottomUnconsumed;
    private float mTotalScrollTopUnconsumed;
    private int mTouchSlop;
    private float mVelocityX;
    private float mVelocityY;

    public interface OnScrollListener {
        void onScrolled(SpringBackLayout springBackLayout, int i, int i2);

        void onStateChanged(int i, int i2, boolean z);
    }

    public interface OnSpringListener {
        boolean onSpringBack();
    }

    public SpringBackLayout(Context context) {
        this(context, null);
    }

    public SpringBackLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mActivePointerId = -1;
        this.consumeNestFlingCounter = 0;
        this.mParentScrollConsumed = new int[2];
        this.mParentOffsetInWindow = new int[2];
        this.mNestedScrollingV2ConsumedCompat = new int[2];
        this.mSpringBackEnable = true;
        this.mOnScrollListeners = new ArrayList();
        this.mScrollState = 0;
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = miuix.core.view.NestedScrollingChildHelper.obtain(this);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SpringBackLayout);
        this.mTargetId = obtainStyledAttributes.getResourceId(R.styleable.SpringBackLayout_scrollableView, -1);
        this.mOriginScrollOrientation = obtainStyledAttributes.getInt(R.styleable.SpringBackLayout_scrollOrientation, 2);
        this.mSpringBackMode = obtainStyledAttributes.getInt(R.styleable.SpringBackLayout_springBackMode, 3);
        obtainStyledAttributes.recycle();
        this.mSpringScroller = new SpringScroller();
        this.mHelper = new SpringBackLayoutHelper(this, this.mOriginScrollOrientation);
        setNestedScrollingEnabled(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        this.mScreenWith = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
    }

    private void checkHorizontalScrollStart() {
        if (getScrollX() != 0) {
            this.mIsBeingDragged = true;
            float obtainTouchDistance = obtainTouchDistance((float) Math.abs(getScrollX()), 2);
            this.mInitialDownX = getScrollX() < 0 ? this.mInitialDownX - obtainTouchDistance : this.mInitialDownX + obtainTouchDistance;
            this.mInitialMotionX = this.mInitialDownX;
            return;
        }
        this.mIsBeingDragged = false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0064  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0068  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkOrientation(MotionEvent motionEvent) {
        this.mHelper.checkOrientation(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked == 6) {
                            onSecondaryPointerUp(motionEvent);
                            return;
                        }
                        return;
                    }
                } else if (this.mScrollOrientation == 0) {
                    int i = this.mHelper.mScrollOrientation;
                    if (i != 0) {
                        this.mScrollOrientation = i;
                        return;
                    }
                    return;
                } else {
                    return;
                }
            }
            disallowParentInterceptTouchEvent(false);
            if ((this.mOriginScrollOrientation & 2) != 0) {
                springBack(2);
            } else {
                springBack(1);
            }
        } else {
            SpringBackLayoutHelper springBackLayoutHelper = this.mHelper;
            this.mInitialDownY = springBackLayoutHelper.mInitialDownY;
            this.mInitialDownX = springBackLayoutHelper.mInitialDownX;
            this.mActivePointerId = springBackLayoutHelper.mActivePointerId;
            if (getScrollY() != 0) {
                this.mScrollOrientation = 2;
            } else if (getScrollX() != 0) {
                this.mScrollOrientation = 1;
            } else {
                this.mScrollOrientation = 0;
                if ((this.mOriginScrollOrientation & 2) == 0) {
                    checkScrollStart(2);
                    return;
                } else {
                    checkScrollStart(1);
                    return;
                }
            }
            requestDisallowParentInterceptTouchEvent(true);
            if ((this.mOriginScrollOrientation & 2) == 0) {
            }
        }
    }

    private void checkScrollStart(int i) {
        if (i == 2) {
            checkVerticalScrollStart();
        } else {
            checkHorizontalScrollStart();
        }
    }

    private void checkVerticalScrollStart() {
        if (getScrollY() != 0) {
            this.mIsBeingDragged = true;
            float obtainTouchDistance = obtainTouchDistance((float) Math.abs(getScrollY()), 2);
            this.mInitialDownY = getScrollY() < 0 ? this.mInitialDownY - obtainTouchDistance : this.mInitialDownY + obtainTouchDistance;
            this.mInitialMotionY = this.mInitialDownY;
            return;
        }
        this.mIsBeingDragged = false;
    }

    private void consumeDelta(int i, @NonNull int[] iArr, int i2) {
        if (i2 == 2) {
            iArr[1] = i;
        } else {
            iArr[0] = i;
        }
    }

    private void disallowParentInterceptTouchEvent(boolean z) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z);
        }
    }

    private void dispatchScrollState(int i) {
        int i2 = this.mScrollState;
        if (i2 != i) {
            this.mScrollState = i;
            for (OnScrollListener onStateChanged : this.mOnScrollListeners) {
                onStateChanged.onStateChanged(i2, i, this.mSpringScroller.isFinished());
            }
        }
    }

    private void ensureTarget() {
        if (this.mTarget == null) {
            int i = this.mTargetId;
            if (i != -1) {
                this.mTarget = findViewById(i);
            } else {
                throw new IllegalArgumentException("invalid target Id");
            }
        }
        if (this.mTarget != null) {
            if (VERSION.SDK_INT >= 21 && isEnabled()) {
                View view = this.mTarget;
                if ((view instanceof NestedScrollingChild3) && !view.isNestedScrollingEnabled()) {
                    this.mTarget.setNestedScrollingEnabled(true);
                }
            }
            if (this.mTarget.getOverScrollMode() != 2) {
                this.mTarget.setOverScrollMode(2);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("fail to get target");
    }

    private boolean isHorizontalTargetScrollToTop() {
        return !this.mTarget.canScrollHorizontally(-1);
    }

    private boolean isTargetScrollOrientation(int i) {
        return this.mScrollOrientation == i;
    }

    private boolean isTargetScrollToBottom(int i) {
        View view = this.mTarget;
        return i == 2 ? view instanceof ListView ? !ListViewCompat.canScrollList((ListView) view, 1) : !view.canScrollVertically(1) : !view.canScrollHorizontally(1);
    }

    private boolean isTargetScrollToTop(int i) {
        View view = this.mTarget;
        return i == 2 ? view instanceof ListView ? !ListViewCompat.canScrollList((ListView) view, -1) : !view.canScrollVertically(-1) : !view.canScrollHorizontally(-1);
    }

    private boolean isVerticalTargetScrollToTop() {
        View view = this.mTarget;
        return view instanceof ListView ? !ListViewCompat.canScrollList((ListView) view, -1) : !view.canScrollVertically(-1);
    }

    private void moveTarget(float f, int i) {
        int i2 = (int) (-f);
        if (i == 2) {
            scrollTo(0, i2);
        } else {
            scrollTo(i2, 0);
        }
    }

    private float obtainDampingDistance(float f, int i) {
        double min = (double) Math.min(f, 1.0f);
        return ((float) (((Math.pow(min, 3.0d) / 3.0d) - Math.pow(min, 2.0d)) + min)) * ((float) (i == 2 ? this.mScreenHeight : this.mScreenWith));
    }

    private float obtainMaxSpringBackDistance(int i) {
        return obtainDampingDistance(1.0f, i);
    }

    private float obtainSpringBackDistance(float f, int i) {
        return obtainDampingDistance(Math.min(Math.abs(f) / ((float) (i == 2 ? this.mScreenHeight : this.mScreenWith)), 1.0f), i);
    }

    private float obtainTouchDistance(float f, int i) {
        int i2 = i == 2 ? this.mScreenHeight : this.mScreenWith;
        double d = (double) i2;
        return (float) (d - (Math.pow(d, 0.6666666666666666d) * Math.pow((double) (((float) i2) - (f * 3.0f)), 0.3333333333333333d)));
    }

    private boolean onHorizontalInterceptTouchEvent(MotionEvent motionEvent) {
        float f;
        String str;
        boolean z = false;
        if (!isTargetScrollToTop(1) && !isTargetScrollToBottom(1)) {
            return false;
        }
        if (isTargetScrollToTop(1) && !supportTopSpringBackMode()) {
            return false;
        }
        if (isTargetScrollToBottom(1) && !supportBottomSpringBackMode()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int i = this.mActivePointerId;
                    String str2 = TAG;
                    if (i == -1) {
                        str = "Got ACTION_MOVE event but don't have an active pointer id.";
                    } else {
                        int findPointerIndex = motionEvent.findPointerIndex(i);
                        if (findPointerIndex < 0) {
                            str = "Got ACTION_MOVE event but have an invalid active pointer id.";
                        } else {
                            f = motionEvent.getX(findPointerIndex);
                            if (isTargetScrollToBottom(1) && isTargetScrollToTop(1)) {
                                z = true;
                            }
                            if ((z || !isTargetScrollToTop(1)) && (!z || f <= this.mInitialDownX) ? !(this.mInitialDownX - f <= ((float) this.mTouchSlop) || this.mIsBeingDragged) : !(f - this.mInitialDownX <= ((float) this.mTouchSlop) || this.mIsBeingDragged)) {
                                this.mIsBeingDragged = true;
                                dispatchScrollState(1);
                            }
                        }
                    }
                    Log.e(str2, str);
                    return false;
                } else if (actionMasked != 3) {
                    if (actionMasked == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
                return this.mIsBeingDragged;
            }
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            return this.mIsBeingDragged;
        }
        this.mActivePointerId = motionEvent.getPointerId(0);
        int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
        if (findPointerIndex2 < 0) {
            return false;
        }
        this.mInitialDownX = motionEvent.getX(findPointerIndex2);
        if (getScrollX() != 0) {
            this.mIsBeingDragged = true;
            f = this.mInitialDownX;
        } else {
            this.mIsBeingDragged = false;
            return this.mIsBeingDragged;
        }
        this.mInitialMotionX = f;
        return this.mIsBeingDragged;
    }

    private boolean onHorizontalTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (isTargetScrollToTop(1) || isTargetScrollToBottom(1)) {
            return (!isTargetScrollToTop(1) || !isTargetScrollToBottom(1)) ? isTargetScrollToBottom(1) ? onScrollUpEvent(motionEvent, actionMasked, 1) : onScrollDownEvent(motionEvent, actionMasked, 1) : onScrollEvent(motionEvent, actionMasked, 1);
        }
        return false;
    }

    private void onNestedPreScroll(int i, @NonNull int[] iArr, int i2) {
        float f;
        boolean z = this.mNestedScrollAxes == 2;
        int i3 = z ? 2 : 1;
        int abs = Math.abs(z ? getScrollY() : getScrollX());
        float f2 = 0.0f;
        if (i2 == 0) {
            if (i > 0) {
                float f3 = this.mTotalScrollTopUnconsumed;
                if (f3 > 0.0f) {
                    float f4 = (float) i;
                    if (f4 > f3) {
                        consumeDelta((int) f3, iArr, i3);
                        this.mTotalScrollTopUnconsumed = 0.0f;
                    } else {
                        this.mTotalScrollTopUnconsumed = f3 - f4;
                        consumeDelta(i, iArr, i3);
                    }
                    dispatchScrollState(1);
                    f = obtainSpringBackDistance(this.mTotalScrollTopUnconsumed, i3);
                }
            }
            if (i < 0) {
                float f5 = this.mTotalScrollBottomUnconsumed;
                if ((-f5) < 0.0f) {
                    float f6 = (float) i;
                    if (f6 < (-f5)) {
                        consumeDelta((int) f5, iArr, i3);
                        this.mTotalScrollBottomUnconsumed = 0.0f;
                    } else {
                        this.mTotalScrollBottomUnconsumed = f5 + f6;
                        consumeDelta(i, iArr, i3);
                    }
                    dispatchScrollState(1);
                    f = -obtainSpringBackDistance(this.mTotalScrollBottomUnconsumed, i3);
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            float f7 = i3 == 2 ? this.mVelocityY : this.mVelocityX;
            if (i > 0) {
                float f8 = this.mTotalScrollTopUnconsumed;
                if (f8 > 0.0f) {
                    if (f7 > 2000.0f) {
                        float obtainSpringBackDistance = obtainSpringBackDistance(f8, i3);
                        float f9 = (float) i;
                        if (f9 > obtainSpringBackDistance) {
                            consumeDelta((int) obtainSpringBackDistance, iArr, i3);
                            this.mTotalScrollTopUnconsumed = 0.0f;
                        } else {
                            consumeDelta(i, iArr, i3);
                            f2 = obtainSpringBackDistance - f9;
                            this.mTotalScrollTopUnconsumed = obtainTouchDistance(f2, i3);
                        }
                        moveTarget(f2, i3);
                        dispatchScrollState(1);
                        return;
                    }
                    if (!this.mScrollByFling) {
                        this.mScrollByFling = true;
                        springBack(f7, i3, false);
                    }
                    if (this.mSpringScroller.computeScrollOffset()) {
                        scrollTo(this.mSpringScroller.getCurrX(), this.mSpringScroller.getCurrY());
                        this.mTotalScrollTopUnconsumed = obtainTouchDistance((float) abs, i3);
                    } else {
                        this.mTotalScrollTopUnconsumed = 0.0f;
                    }
                    consumeDelta(i, iArr, i3);
                    return;
                }
            }
            if (i < 0) {
                float f10 = this.mTotalScrollBottomUnconsumed;
                if ((-f10) < 0.0f) {
                    if (f7 < -2000.0f) {
                        float obtainSpringBackDistance2 = obtainSpringBackDistance(f10, i3);
                        float f11 = (float) i;
                        if (f11 < (-obtainSpringBackDistance2)) {
                            consumeDelta((int) obtainSpringBackDistance2, iArr, i3);
                            this.mTotalScrollBottomUnconsumed = 0.0f;
                        } else {
                            consumeDelta(i, iArr, i3);
                            f2 = obtainSpringBackDistance2 + f11;
                            this.mTotalScrollBottomUnconsumed = obtainTouchDistance(f2, i3);
                        }
                        dispatchScrollState(1);
                        f = -f2;
                    } else {
                        if (!this.mScrollByFling) {
                            this.mScrollByFling = true;
                            springBack(f7, i3, false);
                        }
                        if (this.mSpringScroller.computeScrollOffset()) {
                            scrollTo(this.mSpringScroller.getCurrX(), this.mSpringScroller.getCurrY());
                            this.mTotalScrollBottomUnconsumed = obtainTouchDistance((float) abs, i3);
                        } else {
                            this.mTotalScrollBottomUnconsumed = 0.0f;
                        }
                        consumeDelta(i, iArr, i3);
                        return;
                    }
                }
            }
            if (i != 0) {
                if (!((this.mTotalScrollBottomUnconsumed == 0.0f || this.mTotalScrollTopUnconsumed == 0.0f) && this.mScrollByFling && getScrollY() == 0)) {
                    return;
                }
                consumeDelta(i, iArr, i3);
                return;
            }
            return;
        }
        moveTarget(f, i3);
    }

    private boolean onScrollDownEvent(MotionEvent motionEvent, int i, int i2) {
        float f;
        float f2;
        float f3;
        int i3;
        if (i != 0) {
            String str = TAG;
            if (i != 1) {
                if (i == 2) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex < 0) {
                        Log.e(str, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    } else if (this.mIsBeingDragged) {
                        if (i2 == 2) {
                            f2 = motionEvent.getY(findPointerIndex);
                            f = Math.signum(f2 - this.mInitialMotionY);
                            f3 = this.mInitialMotionY;
                        } else {
                            f2 = motionEvent.getX(findPointerIndex);
                            f = Math.signum(f2 - this.mInitialMotionX);
                            f3 = this.mInitialMotionX;
                        }
                        float obtainSpringBackDistance = f * obtainSpringBackDistance(f2 - f3, i2);
                        if (obtainSpringBackDistance > 0.0f) {
                            requestDisallowParentInterceptTouchEvent(true);
                            moveTarget(obtainSpringBackDistance, i2);
                        } else {
                            moveTarget(0.0f, i2);
                            return false;
                        }
                    }
                } else if (i == 3) {
                    return false;
                } else {
                    if (i == 5) {
                        int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex2 < 0) {
                            Log.e(str, "Got ACTION_POINTER_DOWN event but have an invalid active pointer id.");
                            return false;
                        }
                        String str2 = "Got ACTION_POINTER_DOWN event but have an invalid action index.";
                        if (i2 == 2) {
                            float y = motionEvent.getY(findPointerIndex2) - this.mInitialDownY;
                            i3 = motionEvent.getActionIndex();
                            if (i3 < 0) {
                                Log.e(str, str2);
                                return false;
                            }
                            this.mInitialDownY = motionEvent.getY(i3) - y;
                            this.mInitialMotionY = this.mInitialDownY;
                        } else {
                            float x = motionEvent.getX(findPointerIndex2) - this.mInitialDownX;
                            i3 = motionEvent.getActionIndex();
                            if (i3 < 0) {
                                Log.e(str, str2);
                                return false;
                            }
                            this.mInitialDownX = motionEvent.getX(i3) - x;
                            this.mInitialMotionX = this.mInitialDownX;
                        }
                        this.mActivePointerId = motionEvent.getPointerId(i3);
                    } else if (i == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
            } else if (motionEvent.findPointerIndex(this.mActivePointerId) < 0) {
                Log.e(str, "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            } else {
                if (this.mIsBeingDragged) {
                    this.mIsBeingDragged = false;
                    springBack(i2);
                }
                this.mActivePointerId = -1;
                return false;
            }
        } else {
            this.mActivePointerId = motionEvent.getPointerId(0);
            checkScrollStart(i2);
        }
        return true;
    }

    private boolean onScrollEvent(MotionEvent motionEvent, int i, int i2) {
        float f;
        float f2;
        float f3;
        int i3;
        if (i != 0) {
            String str = TAG;
            if (i != 1) {
                if (i == 2) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex < 0) {
                        Log.e(str, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    } else if (this.mIsBeingDragged) {
                        if (i2 == 2) {
                            f2 = motionEvent.getY(findPointerIndex);
                            f = Math.signum(f2 - this.mInitialMotionY);
                            f3 = this.mInitialMotionY;
                        } else {
                            f2 = motionEvent.getX(findPointerIndex);
                            f = Math.signum(f2 - this.mInitialMotionX);
                            f3 = this.mInitialMotionX;
                        }
                        float obtainSpringBackDistance = f * obtainSpringBackDistance(f2 - f3, i2);
                        requestDisallowParentInterceptTouchEvent(true);
                        moveTarget(obtainSpringBackDistance, i2);
                    }
                } else if (i == 3) {
                    return false;
                } else {
                    if (i == 5) {
                        int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex2 < 0) {
                            Log.e(str, "Got ACTION_POINTER_DOWN event but have an invalid active pointer id.");
                            return false;
                        }
                        String str2 = "Got ACTION_POINTER_DOWN event but have an invalid action index.";
                        if (i2 == 2) {
                            float y = motionEvent.getY(findPointerIndex2) - this.mInitialDownY;
                            i3 = motionEvent.getActionIndex();
                            if (i3 < 0) {
                                Log.e(str, str2);
                                return false;
                            }
                            this.mInitialDownY = motionEvent.getY(i3) - y;
                            this.mInitialMotionY = this.mInitialDownY;
                        } else {
                            float x = motionEvent.getX(findPointerIndex2) - this.mInitialDownX;
                            i3 = motionEvent.getActionIndex();
                            if (i3 < 0) {
                                Log.e(str, str2);
                                return false;
                            }
                            this.mInitialDownX = motionEvent.getX(i3) - x;
                            this.mInitialMotionX = this.mInitialDownX;
                        }
                        this.mActivePointerId = motionEvent.getPointerId(i3);
                    } else if (i == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
            } else if (motionEvent.findPointerIndex(this.mActivePointerId) < 0) {
                Log.e(str, "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            } else {
                if (this.mIsBeingDragged) {
                    this.mIsBeingDragged = false;
                    springBack(i2);
                }
                this.mActivePointerId = -1;
                return false;
            }
        } else {
            this.mActivePointerId = motionEvent.getPointerId(0);
            checkScrollStart(i2);
        }
        return true;
    }

    private boolean onScrollUpEvent(MotionEvent motionEvent, int i, int i2) {
        float f;
        float f2;
        float f3;
        int i3;
        if (i != 0) {
            String str = TAG;
            if (i != 1) {
                if (i == 2) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex < 0) {
                        Log.e(str, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    } else if (this.mIsBeingDragged) {
                        if (i2 == 2) {
                            f2 = motionEvent.getY(findPointerIndex);
                            f = Math.signum(this.mInitialMotionY - f2);
                            f3 = this.mInitialMotionY;
                        } else {
                            f2 = motionEvent.getX(findPointerIndex);
                            f = Math.signum(this.mInitialMotionX - f2);
                            f3 = this.mInitialMotionX;
                        }
                        float obtainSpringBackDistance = f * obtainSpringBackDistance(f3 - f2, i2);
                        if (obtainSpringBackDistance > 0.0f) {
                            requestDisallowParentInterceptTouchEvent(true);
                            moveTarget(-obtainSpringBackDistance, i2);
                        } else {
                            moveTarget(0.0f, i2);
                            return false;
                        }
                    }
                } else if (i == 3) {
                    return false;
                } else {
                    if (i == 5) {
                        int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex2 < 0) {
                            Log.e(str, "Got ACTION_POINTER_DOWN event but have an invalid active pointer id.");
                            return false;
                        }
                        String str2 = "Got ACTION_POINTER_DOWN event but have an invalid action index.";
                        if (i2 == 2) {
                            float y = motionEvent.getY(findPointerIndex2) - this.mInitialDownY;
                            i3 = motionEvent.getActionIndex();
                            if (i3 < 0) {
                                Log.e(str, str2);
                                return false;
                            }
                            this.mInitialDownY = motionEvent.getY(i3) - y;
                            this.mInitialMotionY = this.mInitialDownY;
                        } else {
                            float x = motionEvent.getX(findPointerIndex2) - this.mInitialDownX;
                            i3 = motionEvent.getActionIndex();
                            if (i3 < 0) {
                                Log.e(str, str2);
                                return false;
                            }
                            this.mInitialDownX = motionEvent.getX(i3) - x;
                            this.mInitialMotionX = this.mInitialDownX;
                        }
                        this.mActivePointerId = motionEvent.getPointerId(i3);
                    } else if (i == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
            } else if (motionEvent.findPointerIndex(this.mActivePointerId) < 0) {
                Log.e(str, "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            } else {
                if (this.mIsBeingDragged) {
                    this.mIsBeingDragged = false;
                    springBack(i2);
                }
                this.mActivePointerId = -1;
                return false;
            }
        } else {
            this.mActivePointerId = motionEvent.getPointerId(0);
            checkScrollStart(i2);
        }
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            this.mActivePointerId = motionEvent.getPointerId(actionIndex == 0 ? 1 : 0);
        }
    }

    private boolean onVerticalInterceptTouchEvent(MotionEvent motionEvent) {
        float f;
        String str;
        boolean z = false;
        if (!isTargetScrollToTop(2) && !isTargetScrollToBottom(2)) {
            return false;
        }
        if (isTargetScrollToTop(2) && !supportTopSpringBackMode()) {
            return false;
        }
        if (isTargetScrollToBottom(2) && !supportBottomSpringBackMode()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int i = this.mActivePointerId;
                    String str2 = TAG;
                    if (i == -1) {
                        str = "Got ACTION_MOVE event but don't have an active pointer id.";
                    } else {
                        int findPointerIndex = motionEvent.findPointerIndex(i);
                        if (findPointerIndex < 0) {
                            str = "Got ACTION_MOVE event but have an invalid active pointer id.";
                        } else {
                            f = motionEvent.getY(findPointerIndex);
                            if (isTargetScrollToBottom(2) && isTargetScrollToTop(2)) {
                                z = true;
                            }
                            if ((z || !isTargetScrollToTop(2)) && (!z || f <= this.mInitialDownY) ? !(this.mInitialDownY - f <= ((float) this.mTouchSlop) || this.mIsBeingDragged) : !(f - this.mInitialDownY <= ((float) this.mTouchSlop) || this.mIsBeingDragged)) {
                                this.mIsBeingDragged = true;
                                dispatchScrollState(1);
                            }
                        }
                    }
                    Log.e(str2, str);
                    return false;
                } else if (actionMasked != 3) {
                    if (actionMasked == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
                return this.mIsBeingDragged;
            }
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            return this.mIsBeingDragged;
        }
        this.mActivePointerId = motionEvent.getPointerId(0);
        int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
        if (findPointerIndex2 < 0) {
            return false;
        }
        this.mInitialDownY = motionEvent.getY(findPointerIndex2);
        if (getScrollY() != 0) {
            this.mIsBeingDragged = true;
            f = this.mInitialDownY;
        } else {
            this.mIsBeingDragged = false;
            return this.mIsBeingDragged;
        }
        this.mInitialMotionY = f;
        return this.mIsBeingDragged;
    }

    private boolean onVerticalTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (isTargetScrollToTop(2) || isTargetScrollToBottom(2)) {
            return (!isTargetScrollToTop(2) || !isTargetScrollToBottom(2)) ? isTargetScrollToBottom(2) ? onScrollUpEvent(motionEvent, actionMasked, 2) : onScrollDownEvent(motionEvent, actionMasked, 2) : onScrollEvent(motionEvent, actionMasked, 2);
        }
        return false;
    }

    private void springBack(float f, int i, boolean z) {
        OnSpringListener onSpringListener = this.mOnSpringListener;
        if (onSpringListener == null || !onSpringListener.onSpringBack()) {
            this.mSpringScroller.forceStop();
            this.mSpringScroller.scrollByFling((float) getScrollX(), 0.0f, (float) getScrollY(), 0.0f, f, i, false);
            dispatchScrollState(2);
            if (z) {
                postInvalidateOnAnimation();
            }
        }
    }

    private void springBack(int i) {
        springBack(0.0f, i, true);
    }

    private boolean supportBottomSpringBackMode() {
        return (this.mSpringBackMode & 2) != 0;
    }

    private boolean supportTopSpringBackMode() {
        return (this.mSpringBackMode & 1) != 0;
    }

    public void addOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListeners.add(onScrollListener);
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mSpringScroller.computeScrollOffset()) {
            scrollTo(this.mSpringScroller.getCurrX(), this.mSpringScroller.getCurrY());
            if (!this.mSpringScroller.isFinished()) {
                postInvalidateOnAnimation();
            } else {
                dispatchScrollState(0);
            }
        }
    }

    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(f, f2, z);
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(f, f2);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, @Nullable int[] iArr, @Nullable int[] iArr2, int i3) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2, i3);
    }

    public void dispatchNestedScroll(int i, int i2, int i3, int i4, @Nullable int[] iArr, int i5, @NonNull int[] iArr2) {
        this.mNestedScrollingChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
    }

    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, @Nullable int[] iArr, int i5) {
        return this.mNestedScrollingChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr, i5);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0 && this.mScrollState == 2 && this.mHelper.isTouchInTarget(motionEvent)) {
            dispatchScrollState(1);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (motionEvent.getActionMasked() == 1 && this.mScrollState != 2) {
            dispatchScrollState(0);
        }
        return dispatchTouchEvent;
    }

    public int getSpringBackMode() {
        return this.mSpringBackMode;
    }

    public boolean hasNestedScrollingParent(int i) {
        return this.mNestedScrollingChildHelper.hasNestedScrollingParent(i);
    }

    public boolean hasSpringListener() {
        return this.mOnSpringListener != null;
    }

    public void internalRequestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
    }

    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mSpringBackEnable || !isEnabled() || this.mNestedFlingInProgress || this.mNestedScrollInProgress || (VERSION.SDK_INT >= 21 && this.mTarget.isNestedScrollingEnabled())) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (!this.mSpringScroller.isFinished() && actionMasked == 0) {
            this.mSpringScroller.forceStop();
        }
        if (!supportTopSpringBackMode() && !supportBottomSpringBackMode()) {
            return false;
        }
        int i = this.mOriginScrollOrientation;
        if ((i & 4) != 0) {
            checkOrientation(motionEvent);
            if (isTargetScrollOrientation(2) && (this.mOriginScrollOrientation & 1) != 0 && ((float) getScrollX()) == 0.0f) {
                return false;
            }
            if (isTargetScrollOrientation(1) && (this.mOriginScrollOrientation & 2) != 0 && ((float) getScrollY()) == 0.0f) {
                return false;
            }
            if (isTargetScrollOrientation(2) || isTargetScrollOrientation(1)) {
                disallowParentInterceptTouchEvent(true);
            }
        } else {
            this.mScrollOrientation = i;
        }
        if (isTargetScrollOrientation(2)) {
            return onVerticalInterceptTouchEvent(motionEvent);
        }
        if (isTargetScrollOrientation(1)) {
            return onHorizontalInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        this.mTarget.layout(paddingLeft, paddingTop, ((measuredWidth - getPaddingLeft()) - getPaddingRight()) + paddingLeft, ((measuredHeight - getPaddingTop()) - getPaddingBottom()) + paddingTop);
    }

    public void onMeasure(int i, int i2) {
        ensureTarget();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        measureChild(this.mTarget, i, i2);
        if (size > this.mTarget.getMeasuredWidth()) {
            size = this.mTarget.getMeasuredWidth();
        }
        if (size2 > this.mTarget.getMeasuredHeight()) {
            size2 = this.mTarget.getMeasuredHeight();
        }
        if (mode != 1073741824) {
            size = this.mTarget.getMeasuredWidth();
        }
        if (mode2 != 1073741824) {
            size2 = this.mTarget.getMeasuredHeight();
        }
        setMeasuredDimension(size, size2);
    }

    public boolean onNestedCurrentFling(float f, float f2) {
        this.mVelocityX = f;
        this.mVelocityY = f2;
        return true;
    }

    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        return dispatchNestedFling(f, f2, z);
    }

    public boolean onNestedPreFling(View view, float f, float f2) {
        return dispatchNestedPreFling(f, f2);
    }

    public void onNestedPreScroll(@NonNull View view, int i, int i2, @NonNull int[] iArr, int i3) {
        if (this.mSpringBackEnable) {
            if (this.mNestedScrollAxes == 2) {
                onNestedPreScroll(i2, iArr, i3);
            } else {
                onNestedPreScroll(i, iArr, i3);
            }
        }
        int[] iArr2 = this.mParentScrollConsumed;
        if (dispatchNestedPreScroll(i - iArr[0], i2 - iArr[1], iArr2, null, i3)) {
            iArr[0] = iArr[0] + iArr2[0];
            iArr[1] = iArr[1] + iArr2[1];
        }
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        onNestedScroll(view, i, i2, i3, i4, 0, this.mNestedScrollingV2ConsumedCompat);
    }

    public void onNestedScroll(@NonNull View view, int i, int i2, int i3, int i4, int i5) {
        onNestedScroll(view, i, i2, i3, i4, i5, this.mNestedScrollingV2ConsumedCompat);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00b9, code lost:
        if (((float) (-r9)) <= r4) goto L_0x00bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00bb, code lost:
        r8.mSpringScroller.setFirstStep(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0151, code lost:
        if (((float) r9) <= r4) goto L_0x00bb;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNestedScroll(@NonNull View view, int i, int i2, int i3, int i4, int i5, @NonNull int[] iArr) {
        float f;
        int i6 = 0;
        boolean z = this.mNestedScrollAxes == 2;
        int i7 = z ? i2 : i;
        int i8 = z ? iArr[1] : iArr[0];
        dispatchNestedScroll(i, i2, i3, i4, this.mParentOffsetInWindow, i5, iArr);
        if (this.mSpringBackEnable) {
            int i9 = (z ? iArr[1] : iArr[0]) - i8;
            int i10 = z ? i4 - i9 : i3 - i9;
            if (i10 != 0) {
                i6 = i10;
            }
            int i11 = z ? 2 : 1;
            if (i6 >= 0 || !isTargetScrollToTop(i11) || !supportTopSpringBackMode()) {
                if (i6 > 0 && isTargetScrollToBottom(i11) && supportBottomSpringBackMode()) {
                    if (i5 != 0) {
                        float obtainMaxSpringBackDistance = obtainMaxSpringBackDistance(i11);
                        if (this.mVelocityY != 0.0f || this.mVelocityX != 0.0f) {
                            this.mScrollByFling = true;
                            if (i7 != 0) {
                            }
                            dispatchScrollState(2);
                        } else if (this.mTotalScrollBottomUnconsumed == 0.0f) {
                            float f2 = obtainMaxSpringBackDistance - this.mTotalFlingUnconsumed;
                            if (this.consumeNestFlingCounter < 4) {
                                if (f2 <= ((float) Math.abs(i6))) {
                                    this.mTotalFlingUnconsumed += f2;
                                    iArr[1] = (int) (((float) iArr[1]) + f2);
                                } else {
                                    this.mTotalFlingUnconsumed += (float) Math.abs(i6);
                                    iArr[1] = iArr[1] + i10;
                                }
                                dispatchScrollState(2);
                                f = -obtainSpringBackDistance(this.mTotalFlingUnconsumed, i11);
                            }
                        } else {
                            return;
                        }
                    } else if (this.mSpringScroller.isFinished()) {
                        this.mTotalScrollBottomUnconsumed += (float) Math.abs(i6);
                        dispatchScrollState(1);
                        moveTarget(-obtainSpringBackDistance(this.mTotalScrollBottomUnconsumed, i11), i11);
                        iArr[1] = iArr[1] + i10;
                    }
                }
            }
            if (i5 != 0) {
                float obtainMaxSpringBackDistance2 = obtainMaxSpringBackDistance(i11);
                if (this.mVelocityY != 0.0f || this.mVelocityX != 0.0f) {
                    this.mScrollByFling = true;
                    if (i7 != 0) {
                    }
                    dispatchScrollState(2);
                } else if (this.mTotalScrollTopUnconsumed == 0.0f) {
                    float f3 = obtainMaxSpringBackDistance2 - this.mTotalFlingUnconsumed;
                    if (this.consumeNestFlingCounter < 4) {
                        if (f3 <= ((float) Math.abs(i6))) {
                            this.mTotalFlingUnconsumed += f3;
                            iArr[1] = (int) (((float) iArr[1]) + f3);
                        } else {
                            this.mTotalFlingUnconsumed += (float) Math.abs(i6);
                            iArr[1] = iArr[1] + i10;
                        }
                        dispatchScrollState(2);
                        f = obtainSpringBackDistance(this.mTotalFlingUnconsumed, i11);
                    }
                } else {
                    return;
                }
            } else if (this.mSpringScroller.isFinished()) {
                this.mTotalScrollTopUnconsumed += (float) Math.abs(i6);
                dispatchScrollState(1);
                moveTarget(obtainSpringBackDistance(this.mTotalScrollTopUnconsumed, i11), i11);
                iArr[1] = iArr[1] + i10;
            }
            moveTarget(f, i11);
            this.consumeNestFlingCounter++;
        }
    }

    public void onNestedScrollAccepted(View view, View view2, int i) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        startNestedScroll(i & 2);
    }

    public void onNestedScrollAccepted(@NonNull View view, @NonNull View view2, int i, int i2) {
        if (this.mSpringBackEnable) {
            int i3 = 2;
            boolean z = this.mNestedScrollAxes == 2;
            if (!z) {
                i3 = 1;
            }
            float scrollY = (float) (z ? getScrollY() : getScrollX());
            if (i2 != 0) {
                if (scrollY == 0.0f) {
                    this.mTotalFlingUnconsumed = 0.0f;
                } else {
                    this.mTotalFlingUnconsumed = obtainTouchDistance(Math.abs(scrollY), i3);
                }
                this.mNestedFlingInProgress = true;
                this.consumeNestFlingCounter = 0;
            } else {
                if (scrollY == 0.0f) {
                    this.mTotalScrollTopUnconsumed = 0.0f;
                } else if (scrollY < 0.0f) {
                    this.mTotalScrollTopUnconsumed = obtainTouchDistance(Math.abs(scrollY), i3);
                } else {
                    this.mTotalScrollTopUnconsumed = 0.0f;
                    this.mTotalScrollBottomUnconsumed = obtainTouchDistance(Math.abs(scrollY), i3);
                    this.mNestedScrollInProgress = true;
                }
                this.mTotalScrollBottomUnconsumed = 0.0f;
                this.mNestedScrollInProgress = true;
            }
            this.mVelocityY = 0.0f;
            this.mVelocityX = 0.0f;
            this.mScrollByFling = false;
            this.mSpringScroller.forceStop();
        }
        onNestedScrollAccepted(view, view2, i);
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        for (OnScrollListener onScrolled : this.mOnScrollListeners) {
            onScrolled.onScrolled(this, i - i3, i2 - i4);
        }
    }

    public boolean onStartNestedScroll(View view, View view2, int i) {
        return isEnabled();
    }

    public boolean onStartNestedScroll(@NonNull View view, @NonNull View view2, int i, int i2) {
        if (this.mSpringBackEnable) {
            this.mNestedScrollAxes = i;
            int i3 = 2;
            boolean z = this.mNestedScrollAxes == 2;
            if (!z) {
                i3 = 1;
            }
            if ((i3 & this.mOriginScrollOrientation) == 0 || !onStartNestedScroll(view, view, i)) {
                return false;
            }
            float scrollY = (float) (z ? getScrollY() : getScrollX());
            if (!(i2 == 0 || scrollY == 0.0f || !(this.mTarget instanceof NestedScrollView))) {
                return false;
            }
        }
        boolean startNestedScroll = this.mNestedScrollingChildHelper.startNestedScroll(i, i2);
        return true;
    }

    public void onStopNestedScroll(@NonNull View view, int i) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view, i);
        stopNestedScroll(i);
        if (this.mSpringBackEnable) {
            int i2 = 1;
            boolean z = this.mNestedScrollAxes == 2;
            if (z) {
                i2 = 2;
            }
            if (this.mNestedScrollInProgress) {
                this.mNestedScrollInProgress = false;
                float scrollY = (float) (z ? getScrollY() : getScrollX());
                if (this.mNestedFlingInProgress || scrollY == 0.0f) {
                    if (scrollY != 0.0f) {
                        dispatchScrollState(2);
                    }
                }
            } else {
                if (this.mNestedFlingInProgress) {
                    this.mNestedFlingInProgress = false;
                    if (this.mScrollByFling) {
                        if (this.mSpringScroller.isFinished()) {
                            springBack(i2 == 2 ? this.mVelocityY : this.mVelocityX, i2, false);
                        }
                        postInvalidateOnAnimation();
                    }
                }
            }
            springBack(i2);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (!isEnabled() || this.mNestedFlingInProgress || this.mNestedScrollInProgress || (VERSION.SDK_INT >= 21 && this.mTarget.isNestedScrollingEnabled())) {
            return false;
        }
        if (!this.mSpringScroller.isFinished() && actionMasked == 0) {
            this.mSpringScroller.forceStop();
        }
        if (isTargetScrollOrientation(2)) {
            return onVerticalTouchEvent(motionEvent);
        }
        if (isTargetScrollOrientation(1)) {
            return onHorizontalTouchEvent(motionEvent);
        }
        return false;
    }

    public void removeOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListeners.remove(onScrollListener);
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (!isEnabled() || !this.mSpringBackEnable) {
            super.requestDisallowInterceptTouchEvent(z);
        }
    }

    public void requestDisallowParentInterceptTouchEvent(boolean z) {
        ViewParent parent = getParent();
        parent.requestDisallowInterceptTouchEvent(z);
        while (parent != null) {
            if (parent instanceof SpringBackLayout) {
                ((SpringBackLayout) parent).internalRequestDisallowInterceptTouchEvent(z);
            }
            parent = parent.getParent();
        }
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        View view = this.mTarget;
        if (view != null && (view instanceof NestedScrollingChild3) && VERSION.SDK_INT >= 21 && z != view.isNestedScrollingEnabled()) {
            this.mTarget.setNestedScrollingEnabled(z);
        }
    }

    public void setNestedScrollingEnabled(boolean z) {
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(z);
    }

    public void setOnSpringListener(OnSpringListener onSpringListener) {
        this.mOnSpringListener = onSpringListener;
    }

    public void setScrollOrientation(int i) {
        this.mOriginScrollOrientation = i;
        this.mHelper.mTargetScrollOrientation = i;
    }

    public void setSpringBackEnable(boolean z) {
        this.mSpringBackEnable = z;
    }

    public void setSpringBackMode(int i) {
        this.mSpringBackMode = i;
    }

    public void setTarget(@NonNull View view) {
        this.mTarget = view;
        if (VERSION.SDK_INT >= 21) {
            View view2 = this.mTarget;
            if ((view2 instanceof NestedScrollingChild3) && !view2.isNestedScrollingEnabled()) {
                this.mTarget.setNestedScrollingEnabled(true);
            }
        }
    }

    public void smoothScrollTo(int i, int i2) {
        if (i - getScrollX() != 0 || i2 - getScrollY() != 0) {
            this.mSpringScroller.forceStop();
            this.mSpringScroller.scrollByFling((float) getScrollX(), (float) i, (float) getScrollY(), (float) i2, 0.0f, 2, true);
            dispatchScrollState(2);
            postInvalidateOnAnimation();
        }
    }

    public boolean springBackEnable() {
        return this.mSpringBackEnable;
    }

    public boolean startNestedScroll(int i) {
        return this.mNestedScrollingChildHelper.startNestedScroll(i);
    }

    public boolean startNestedScroll(int i, int i2) {
        return this.mNestedScrollingChildHelper.startNestedScroll(i, i2);
    }

    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }

    public void stopNestedScroll(int i) {
        this.mNestedScrollingChildHelper.stopNestedScroll(i);
    }
}
