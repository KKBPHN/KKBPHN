package miui.view.springback;

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
import java.util.ArrayList;
import java.util.List;
import miui.R;

public class SpringBackLayout extends ViewGroup {
    public static final int ANGLE = 4;
    public static final int HORIZONTAL = 1;
    private static final int INVALID_ID = -1;
    private static final int INVALID_POINTER = -1;
    public static final int SPRING_BACK_BOTTOM = 2;
    public static final int SPRING_BACK_TOP = 1;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "SpringBackLayout";
    public static final int UNCHECK_ORIENTATION = 0;
    public static final int VERTICAL = 2;
    private int mActivePointerId;
    private SpringBackLayoutHelper mHelper;
    private float mInitialDownX;
    private float mInitialDownY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private List mOnScrollListeners;
    private OnSpringListener mOnSpringListener;
    private int mOriginScrollOrientation;
    private final int mScreenHeight;
    private final int mScreenWith;
    private int mScrollOrientation;
    private int mScrollState;
    private boolean mSpringBackEnable;
    private int mSpringBackMode;
    private SpringScroller mSpringScroller;
    private View mTarget;
    private int mTargetId;
    private int mTouchSlop;

    public interface OnScrollListener {
        void onScrolled(SpringBackLayout springBackLayout, int i, int i2);

        void onStateChanged(int i, int i2);
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
        this.mSpringBackEnable = true;
        this.mOnScrollListeners = new ArrayList();
        this.mScrollState = 0;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SpringBackLayout);
        this.mTargetId = obtainStyledAttributes.getResourceId(R.styleable.SpringBackLayout_scrollableView, -1);
        this.mOriginScrollOrientation = obtainStyledAttributes.getInt(R.styleable.SpringBackLayout_scrollOrientation, 2);
        this.mSpringBackMode = obtainStyledAttributes.getInt(R.styleable.SpringBackLayout_springBackMode, 3);
        obtainStyledAttributes.recycle();
        this.mSpringScroller = new SpringScroller();
        this.mHelper = new SpringBackLayoutHelper(this, this.mOriginScrollOrientation);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        this.mScreenWith = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
        if (isInternationalBuild()) {
            this.mSpringBackEnable = false;
        }
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
                onStateChanged.onStateChanged(i2, i);
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
        View view = this.mTarget;
        if (view == null) {
            throw new IllegalArgumentException("fail to get target");
        } else if (view.getOverScrollMode() != 2) {
            this.mTarget.setOverScrollMode(2);
        }
    }

    private boolean isInternationalBuild() {
        try {
            Class cls = Class.forName("miui.os.Build");
            return cls.getField("IS_INTERNATIONAL_BUILD").getBoolean(cls);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return false;
        }
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
                } else if (i != 3) {
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
            }
            if (motionEvent.findPointerIndex(this.mActivePointerId) < 0) {
                Log.e(str, "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            }
            if (this.mIsBeingDragged) {
                this.mIsBeingDragged = false;
                springBack(i2);
            }
            this.mActivePointerId = -1;
            return false;
        }
        this.mActivePointerId = motionEvent.getPointerId(0);
        checkScrollStart(i2);
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
                } else if (i != 3) {
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
            }
            if (motionEvent.findPointerIndex(this.mActivePointerId) < 0) {
                Log.e(str, "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            }
            if (this.mIsBeingDragged) {
                this.mIsBeingDragged = false;
                springBack(i2);
            }
            this.mActivePointerId = -1;
            return false;
        }
        this.mActivePointerId = motionEvent.getPointerId(0);
        checkScrollStart(i2);
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

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0 && this.mScrollState == 2) {
            dispatchScrollState(1);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (motionEvent.getActionMasked() == 1 && this.mScrollState != 2) {
            dispatchScrollState(0);
        }
        return dispatchTouchEvent;
    }

    public void internalRequestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mSpringBackEnable) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (!this.mSpringScroller.isFinished() && actionMasked == 0) {
            this.mSpringScroller.forceStop();
        }
        if (!isEnabled() || !this.mSpringScroller.isFinished() || (VERSION.SDK_INT >= 21 && this.mTarget.isNestedScrollingEnabled())) {
            return false;
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

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        for (OnScrollListener onScrolled : this.mOnScrollListeners) {
            onScrolled.onScrolled(this, i - i3, i2 - i4);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (!this.mSpringScroller.isFinished() && actionMasked == 0) {
            this.mSpringScroller.forceStop();
        }
        if (!isEnabled() || !this.mSpringScroller.isFinished() || (VERSION.SDK_INT >= 21 && this.mTarget.isNestedScrollingEnabled())) {
            return false;
        }
        if (isTargetScrollOrientation(2)) {
            return onVerticalTouchEvent(motionEvent);
        }
        if (isTargetScrollOrientation(1)) {
            return onHorizontalTouchEvent(motionEvent);
        }
        return false;
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

    public void setOnSpringListener(OnSpringListener onSpringListener) {
        this.mOnSpringListener = onSpringListener;
    }

    public void setScrollOrientation(int i) {
        this.mOriginScrollOrientation = i;
        this.mHelper.mTargetScrollOrientation = i;
    }

    public void setTarget(View view) {
        this.mTarget = view;
    }

    public void smoothScrollTo(int i, int i2) {
        if (i - getScrollX() != 0 || i2 - getScrollY() != 0) {
            this.mSpringScroller.forceStop();
            this.mSpringScroller.scrollByFling((float) getScrollX(), (float) i, (float) getScrollY(), (float) i2, 0.0f, 2, true);
            dispatchScrollState(2);
            postInvalidateOnAnimation();
        }
    }
}
