package miui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Scroller;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.variable.Android_View_View_class;
import com.miui.internal.variable.Android_View_View_class.Factory;
import java.security.InvalidParameterException;

public class ScreenView extends ViewGroup {
    private static final int AUTO_HIDE_ANIMATION_DURATION = 500;
    private static final int AUTO_HIDE_TIMEOUT_DURATION = 1000;
    private static final float BASELINE_FLING_VELOCITY = 2500.0f;
    private static final float DEFAULT_OVER_SHOOT_TENSION = 1.3f;
    private static final int DEFAULT_SCREEN_SNAP_DURATION = 300;
    public static final int FLING_ALIGN = 4;
    public static final int FLING_CANCEL = 3;
    public static final int FLING_LEFT = 1;
    public static final int FLING_RIGHT = 2;
    private static final float FLING_VELOCITY_INFLUENCE = 0.4f;
    private static final int INVALID_POINTER = -1;
    private static final int INVALID_SCREEN = -1;
    private static final int MINIMAL_SLIDE_BAR_POINT_WIDTH = 48;
    private static final float NANOTIME_DIV = 1.0E9f;
    public static final int SCREEN_ALIGN_CENTER = 2;
    public static final int SCREEN_ALIGN_CUSTOMIZED = 0;
    public static final int SCREEN_ALIGN_LEFT = 1;
    public static final int SCREEN_ALIGN_RIGHT = 3;
    public static final int SCREEN_TRANSITION_TYPE_CLASSIC = 0;
    public static final int SCREEN_TRANSITION_TYPE_CLASSIC_NO_OVER_SHOOT = 1;
    public static final int SCREEN_TRANSITION_TYPE_CROSSFADE = 2;
    public static final int SCREEN_TRANSITION_TYPE_CUBE = 4;
    public static final int SCREEN_TRANSITION_TYPE_CUSTOM = 9;
    public static final int SCREEN_TRANSITION_TYPE_FALLDOWN = 3;
    public static final int SCREEN_TRANSITION_TYPE_LEFTPAGE = 5;
    public static final int SCREEN_TRANSITION_TYPE_RIGHTPAGE = 6;
    public static final int SCREEN_TRANSITION_TYPE_ROTATE = 8;
    public static final int SCREEN_TRANSITION_TYPE_STACK = 7;
    private static final LayoutParams SEEK_POINT_LAYOUT_PARAMS = new LayoutParams(-1, -1, 1.0f);
    private static final float SMOOTHING_CONSTANT = ((float) (0.016d / Math.log(0.75d)));
    private static final float SMOOTHING_SPEED = 0.75f;
    private static final int SNAP_VELOCITY = 300;
    private static final String TAG = "ScreenView";
    protected static final int TOUCH_STATE_PINCHING = 4;
    protected static final int TOUCH_STATE_REST = 0;
    protected static final int TOUCH_STATE_SCROLLING = 1;
    protected static final int TOUCH_STATE_SLIDING = 3;
    /* access modifiers changed from: private */
    public static final Android_View_View_class ViewClass = Factory.getInstance().get();
    private final float DEFAULT_CAMERA_DISTANCE;
    private int mActivePointerId;
    private boolean mAllowLongPress;
    private ArrowIndicator mArrowLeft;
    private int mArrowLeftOffResId;
    private int mArrowLeftOnResId;
    private ArrowIndicator mArrowRight;
    private int mArrowRightOffResId;
    private int mArrowRightOnResId;
    private Runnable mAutoHideTimer;
    protected int mChildScreenWidth;
    private float mConfirmHorizontalScrollRatio;
    private boolean mCurrentGestureFinished;
    protected int mCurrentScreen;
    private boolean mFirstLayout;
    private GestureVelocityTracker mGestureVelocityTracker;
    private int mHeightMeasureSpec;
    private int mIndicatorCount;
    private boolean mIsSlideBarAutoHide;
    protected float mLastMotionX;
    protected float mLastMotionY;
    private int mMaximumVelocity;
    /* access modifiers changed from: private */
    public int mNextScreen;
    private float mOverScrollRatio;
    /* access modifiers changed from: private */
    public float mOvershootTension;
    private int mPrevScreenWidth;
    private ScaleGestureDetector mScaleDetector;
    private int mScreenAlignment;
    private int mScreenCounter;
    protected int mScreenOffset;
    private int mScreenPaddingBottom;
    private int mScreenPaddingTop;
    private SeekBarIndicator mScreenSeekBar;
    private int mScreenSnapDuration;
    private int mScreenTransitionType;
    private int mScreenWidth;
    private ScreenViewOvershootInterpolator mScrollInterpolator;
    private int mScrollLeftBound;
    private int mScrollOffset;
    private int mScrollRightBound;
    private boolean mScrollWholeScreen;
    /* access modifiers changed from: private */
    public Scroller mScroller;
    private int mSeekPointResId;
    /* access modifiers changed from: private */
    public SlideBar mSlideBar;
    private float mSmoothingTime;
    private SnapScreenOnceNotification mSnapScreenOnceNotification;
    private boolean mTouchIntercepted;
    /* access modifiers changed from: private */
    public int mTouchSlop;
    /* access modifiers changed from: private */
    public int mTouchState;
    private float mTouchX;
    private float mVisibleExtentionRatio;
    protected int mVisibleRange;
    private int mWidthMeasureSpec;

    class ArrowIndicator extends ImageView implements Indicator {
        public ArrowIndicator(Context context) {
            super(context);
        }

        public boolean fastOffset(int i) {
            if (getLeft() == i) {
                return false;
            }
            ScreenView.ViewClass.setRightDirectly(this, (getRight() + i) - getLeft());
            ScreenView.ViewClass.setLeftDirectly(this, i);
            return true;
        }
    }

    class GestureVelocityTracker {
        private static final float mMinFoldDist = 3.0f;
        private int mPointerId;
        private float mPrevX;
        private float mStartX;
        private VelocityTracker mVelocityTracker;

        private GestureVelocityTracker() {
            this.mPointerId = -1;
            this.mStartX = -1.0f;
            this.mPrevX = -1.0f;
        }

        private void reset() {
            this.mPointerId = -1;
            float f = (float) -1;
            this.mStartX = f;
            this.mPrevX = f;
        }

        public void addMovement(MotionEvent motionEvent) {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            float x = motionEvent.getX();
            int i = this.mPointerId;
            if (i != -1) {
                int findPointerIndex = motionEvent.findPointerIndex(i);
                if (findPointerIndex != -1) {
                    x = motionEvent.getX(findPointerIndex);
                } else {
                    this.mPointerId = -1;
                }
            }
            if (this.mStartX < 0.0f) {
                this.mStartX = x;
                return;
            }
            int i2 = (this.mPrevX > 0.0f ? 1 : (this.mPrevX == 0.0f ? 0 : -1));
            this.mPrevX = x;
        }

        public int getFlingDirection(float f) {
            if (f <= 300.0f) {
                return 4;
            }
            int i = Math.abs(this.mPrevX - this.mStartX) > ((float) ScreenView.this.mTouchSlop) ? this.mPrevX > this.mStartX ? 1 : 2 : 3;
            return i;
        }

        public float getXVelocity(int i, int i2, int i3) {
            this.mVelocityTracker.computeCurrentVelocity(i, (float) i2);
            return this.mVelocityTracker.getXVelocity(i3);
        }

        public void init(int i) {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                velocityTracker.clear();
            }
            reset();
            this.mPointerId = i;
        }

        public void recycle() {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            reset();
        }
    }

    interface Indicator {
        boolean fastOffset(int i);
    }

    class SavedState extends BaseSavedState {
        public static final Creator CREATOR = new Creator() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int currentScreen;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.currentScreen = -1;
            this.currentScreen = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
            this.currentScreen = -1;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.currentScreen);
        }
    }

    class ScaleDetectorListener implements OnScaleGestureListener {
        private static final float VALID_PINCH_IN_RATIO = 0.8f;
        private static final float VALID_PINCH_OUT_RATIO = 1.2f;
        private static final float VALID_PINCH_RATIO = 0.95f;
        private static final float VALID_PINCH_TIME = 200.0f;

        private ScaleDetectorListener() {
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            if (ScreenView.this.mTouchState == 0 && (((float) scaleGestureDetector.getTimeDelta()) > 200.0f || scaleFactor < VALID_PINCH_RATIO || scaleFactor > 1.0526316f)) {
                ScreenView.this.setTouchState(null, 4);
            }
            if (scaleFactor < 0.8f) {
                ScreenView.this.onPinchIn(scaleGestureDetector);
                return true;
            } else if (scaleFactor <= VALID_PINCH_OUT_RATIO) {
                return false;
            } else {
                ScreenView.this.onPinchOut(scaleGestureDetector);
                return true;
            }
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return ScreenView.this.mTouchState == 0;
        }

        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            ScreenView.this.finishCurrentGesture();
        }
    }

    class ScreenViewOvershootInterpolator implements Interpolator {
        /* access modifiers changed from: private */
        public float mTension;

        public ScreenViewOvershootInterpolator() {
            this.mTension = ScreenView.this.mOvershootTension;
        }

        public void disableSettle() {
            this.mTension = 0.0f;
        }

        public float getInterpolation(float f) {
            float f2 = f - 1.0f;
            float f3 = f2 * f2;
            float f4 = this.mTension;
            return (f3 * (((f4 + 1.0f) * f2) + f4)) + 1.0f;
        }

        public void setDistance(int i, int i2) {
            this.mTension = i > 0 ? ScreenView.this.mOvershootTension / ((float) i) : ScreenView.this.mOvershootTension;
        }
    }

    class SeekBarIndicator extends LinearLayout implements Indicator {
        public SeekBarIndicator(Context context) {
            super(context);
        }

        public boolean fastOffset(int i) {
            if (getLeft() == i) {
                return false;
            }
            ScreenView.ViewClass.setRightDirectly(this, (getRight() + i) - getLeft());
            ScreenView.ViewClass.setLeftDirectly(this, i);
            return true;
        }
    }

    class SlideBar extends FrameLayout implements Indicator {
        private Rect mPadding = new Rect();
        private Rect mPos = new Rect();
        private NinePatch mSlidePoint;
        private Bitmap mSlidePointBmp;

        public SlideBar(Context context, int i, int i2) {
            super(context);
            this.mSlidePointBmp = BitmapFactory.decodeResource(getResources(), i);
            Bitmap bitmap = this.mSlidePointBmp;
            if (bitmap != null) {
                byte[] ninePatchChunk = bitmap.getNinePatchChunk();
                if (ninePatchChunk != null) {
                    this.mSlidePoint = new NinePatch(this.mSlidePointBmp, ninePatchChunk, null);
                    FrameLayout frameLayout = new FrameLayout(getContext());
                    frameLayout.setBackgroundResource(i2);
                    addView(frameLayout, new FrameLayout.LayoutParams(-1, -2, 80));
                    this.mPadding.left = frameLayout.getPaddingLeft();
                    this.mPadding.top = frameLayout.getPaddingTop();
                    this.mPadding.right = frameLayout.getPaddingRight();
                    this.mPadding.bottom = frameLayout.getPaddingBottom();
                    Rect rect = this.mPos;
                    rect.top = this.mPadding.top;
                    rect.bottom = rect.top + this.mSlidePointBmp.getHeight();
                }
            }
        }

        /* access modifiers changed from: protected */
        public void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            NinePatch ninePatch = this.mSlidePoint;
            if (ninePatch != null) {
                ninePatch.draw(canvas, this.mPos);
            }
        }

        public boolean fastOffset(int i) {
            if (getLeft() == i) {
                return false;
            }
            ScreenView.ViewClass.setRightDirectly(this, (getRight() + i) - getLeft());
            ScreenView.ViewClass.setLeftDirectly(this, i);
            return true;
        }

        public int getSlideWidth() {
            int measuredWidth = getMeasuredWidth();
            Rect rect = this.mPadding;
            return (measuredWidth - rect.left) - rect.right;
        }

        /* access modifiers changed from: protected */
        public int getSuggestedMinimumHeight() {
            return Math.max(this.mSlidePointBmp.getHeight(), super.getSuggestedMinimumHeight());
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            NinePatch ninePatch = this.mSlidePoint;
            if (ninePatch != null) {
                Rect rect = this.mPos;
                rect.bottom = (i4 - i2) - this.mPadding.bottom;
                rect.top = rect.bottom - ninePatch.getHeight();
            }
        }

        public void setPosition(int i, int i2) {
            Rect rect = this.mPos;
            Rect rect2 = this.mPadding;
            rect.left = i + rect2.left;
            rect.right = i2 + rect2.left;
        }
    }

    class SliderTouchListener implements OnTouchListener {
        private SliderTouchListener() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:5:0x0031, code lost:
            if (r3 != 3) goto L_0x0075;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int width = view.getWidth();
            float max = Math.max(0.0f, Math.min(motionEvent.getX(), (float) (width - 1)));
            int screenCount = ScreenView.this.getScreenCount();
            float f = (float) width;
            int floor = (int) Math.floor((double) ((((float) screenCount) * max) / f));
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action != 1) {
                    if (action == 2) {
                        ScreenView.this.setCurrentScreenInner(floor);
                        ScreenView screenView = ScreenView.this;
                        int i = screenView.mChildScreenWidth;
                        screenView.scrollTo((int) (((((float) (screenCount * i)) * max) / f) - ((float) (i / 2))), 0);
                    }
                }
                ScreenView.this.snapToScreen(floor);
                ScreenView screenView2 = ScreenView.this;
                screenView2.updateSeekPoints(screenView2.mCurrentScreen, screenView2.mNextScreen);
            } else {
                if (!ScreenView.this.mScroller.isFinished()) {
                    ScreenView.this.mScroller.abortAnimation();
                }
                ScreenView.this.setTouchState(motionEvent, 3);
            }
            return true;
        }
    }

    public interface SnapScreenOnceNotification {
        void onSnapCancelled(ScreenView screenView);

        void onSnapEnd(ScreenView screenView);
    }

    public ScreenView(Context context) {
        super(context);
        this.DEFAULT_CAMERA_DISTANCE = Resources.getSystem().getDisplayMetrics().density * 1280.0f;
        this.mFirstLayout = true;
        this.mArrowLeftOnResId = R.drawable.screen_view_arrow_left;
        this.mArrowLeftOffResId = R.drawable.screen_view_arrow_left_gray;
        this.mArrowRightOnResId = R.drawable.screen_view_arrow_right;
        this.mArrowRightOffResId = R.drawable.screen_view_arrow_right_gray;
        this.mSeekPointResId = R.drawable.screen_view_seek_point_selector;
        this.mAutoHideTimer = new Runnable() {
            public void run() {
                ScreenView.this.startHideSlideBar();
            }
        };
        this.mVisibleRange = 1;
        this.mPrevScreenWidth = 0;
        this.mScreenWidth = 0;
        this.mNextScreen = -1;
        this.mOverScrollRatio = 0.33333334f;
        this.mVisibleExtentionRatio = 0.5f;
        this.mScreenCounter = 0;
        this.mTouchState = 0;
        this.mAllowLongPress = true;
        this.mActivePointerId = -1;
        this.mConfirmHorizontalScrollRatio = 0.5f;
        this.mScreenSnapDuration = 300;
        this.mScreenTransitionType = 0;
        this.mOvershootTension = DEFAULT_OVER_SHOOT_TENSION;
        this.mGestureVelocityTracker = new GestureVelocityTracker();
        initScreenView();
    }

    public ScreenView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScreenView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.DEFAULT_CAMERA_DISTANCE = Resources.getSystem().getDisplayMetrics().density * 1280.0f;
        this.mFirstLayout = true;
        this.mArrowLeftOnResId = R.drawable.screen_view_arrow_left;
        this.mArrowLeftOffResId = R.drawable.screen_view_arrow_left_gray;
        this.mArrowRightOnResId = R.drawable.screen_view_arrow_right;
        this.mArrowRightOffResId = R.drawable.screen_view_arrow_right_gray;
        this.mSeekPointResId = R.drawable.screen_view_seek_point_selector;
        this.mAutoHideTimer = new Runnable() {
            public void run() {
                ScreenView.this.startHideSlideBar();
            }
        };
        this.mVisibleRange = 1;
        this.mPrevScreenWidth = 0;
        this.mScreenWidth = 0;
        this.mNextScreen = -1;
        this.mOverScrollRatio = 0.33333334f;
        this.mVisibleExtentionRatio = 0.5f;
        this.mScreenCounter = 0;
        this.mTouchState = 0;
        this.mAllowLongPress = true;
        this.mActivePointerId = -1;
        this.mConfirmHorizontalScrollRatio = 0.5f;
        this.mScreenSnapDuration = 300;
        this.mScreenTransitionType = 0;
        this.mOvershootTension = DEFAULT_OVER_SHOOT_TENSION;
        this.mGestureVelocityTracker = new GestureVelocityTracker();
        initScreenView();
    }

    private ImageView createSeekPoint() {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ScaleType.CENTER);
        imageView.setImageResource(this.mSeekPointResId);
        return imageView;
    }

    private View doGetScreen(int i, boolean z) {
        if (i < 0 || i >= getScreenCount()) {
            return null;
        }
        return getChildAt(toInnerIndex(i, z));
    }

    private void doScrollToScreen(int i, boolean z) {
        int innerIndex = toInnerIndex(i, z);
        if (this.mScrollWholeScreen) {
            innerIndex -= innerIndex % this.mVisibleRange;
        }
        measure(this.mWidthMeasureSpec, this.mHeightMeasureSpec);
        scrollTo((this.mChildScreenWidth * innerIndex) - this.mScrollOffset, 0);
    }

    private void doSetCurrentScreen(int i, boolean z) {
        int i2;
        if (this.mScrollWholeScreen) {
            int max = Math.max(0, Math.min(i, getScreenCount() - 1));
            i2 = max - (max % this.mVisibleRange);
        } else {
            i2 = Math.max(0, Math.min(i, getScreenCount() - this.mVisibleRange));
        }
        setCurrentScreenInner(toInnerIndex(i2, z));
        if (!this.mFirstLayout) {
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            doScrollToScreen(this.mCurrentScreen, true);
            invalidate();
        }
    }

    private void initScreenView() {
        setAlwaysDrawnWithCacheEnabled(true);
        setClipToPadding(true);
        this.mScrollInterpolator = new ScreenViewOvershootInterpolator();
        this.mScroller = new Scroller(getContext(), this.mScrollInterpolator);
        setCurrentScreenInner(0);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        setMaximumSnapVelocity(viewConfiguration.getScaledMaximumFlingVelocity());
        this.mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleDetectorListener());
    }

    private boolean isRTL() {
        return getResources().getConfiguration().getLayoutDirection() == 1;
    }

    private void onTouchEventUnique(MotionEvent motionEvent) {
        this.mGestureVelocityTracker.addMovement(motionEvent);
        int i = this.mTouchState;
        if (i == 0 || 4 == i) {
            this.mScaleDetector.onTouchEvent(motionEvent);
        }
    }

    private void refreshScrollBound() {
        int i = this.mChildScreenWidth;
        this.mScrollLeftBound = ((int) (((float) (-i)) * this.mOverScrollRatio)) - this.mScrollOffset;
        this.mScrollRightBound = !this.mScrollWholeScreen ? ((int) ((((float) i) * (((float) getScreenCount()) + this.mOverScrollRatio)) - ((float) this.mScreenWidth))) + this.mScrollOffset : (int) (((float) (((getScreenCount() - 1) / this.mVisibleRange) * this.mScreenWidth)) + (((float) this.mChildScreenWidth) * this.mOverScrollRatio));
    }

    private int revertIndex(int i) {
        return (getScreenCount() - 1) - i;
    }

    private boolean scrolledFarEnough(MotionEvent motionEvent) {
        float abs = Math.abs(motionEvent.getX(0) - this.mLastMotionX);
        return abs > Math.abs(motionEvent.getY(0) - this.mLastMotionY) * this.mConfirmHorizontalScrollRatio && abs > ((float) (this.mTouchSlop * motionEvent.getPointerCount()));
    }

    private void setCameraDistance(View view, float f) {
        if (view.getCameraDistance() != f) {
            view.setCameraDistance(f);
        }
    }

    /* access modifiers changed from: private */
    public void setCurrentScreenInner(int i) {
        updateSeekPoints(this.mCurrentScreen, i);
        this.mCurrentScreen = i;
        this.mNextScreen = -1;
    }

    /* access modifiers changed from: private */
    public void setTouchState(MotionEvent motionEvent, int i) {
        this.mTouchState = i;
        getParent().requestDisallowInterceptTouchEvent(this.mTouchState != 0);
        if (this.mTouchState == 0) {
            this.mActivePointerId = -1;
            this.mAllowLongPress = false;
            this.mGestureVelocityTracker.recycle();
        } else {
            if (motionEvent != null) {
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
            }
            if (this.mAllowLongPress) {
                this.mAllowLongPress = false;
                View childAt = getChildAt(this.mCurrentScreen);
                if (childAt != null) {
                    childAt.cancelLongPress();
                }
            }
            if (this.mTouchState == 1) {
                this.mTouchX = (float) getScrollX();
                this.mSmoothingTime = ((float) System.nanoTime()) / NANOTIME_DIV;
            }
        }
        showSlideBar();
    }

    private void showSlideBar() {
        if (this.mSlideBar != null && this.mIsSlideBarAutoHide) {
            removeCallbacks(this.mAutoHideTimer);
            this.mSlideBar.animate().cancel();
            this.mSlideBar.setAlpha(1.0f);
            this.mSlideBar.setVisibility(0);
            if (this.mTouchState == 0) {
                postDelayed(this.mAutoHideTimer, 1000);
            }
        }
    }

    private void snapByVelocity(int i) {
        if (this.mChildScreenWidth > 0 && getCurrentScreen() != null) {
            int xVelocity = (int) this.mGestureVelocityTracker.getXVelocity(1000, this.mMaximumVelocity, i);
            snapByVelocity(xVelocity, this.mGestureVelocityTracker.getFlingDirection((float) Math.abs(xVelocity)));
        }
    }

    /* access modifiers changed from: private */
    public void startHideSlideBar() {
        if (this.mIsSlideBarAutoHide) {
            this.mSlideBar.animate().setDuration(DeviceHelper.FEATURE_WHOLE_ANIM ? 500 : 0).alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    ScreenView.this.mSlideBar.setVisibility(4);
                }
            });
        }
    }

    private int toInnerIndex(int i, boolean z) {
        return (z || !isRTL()) ? i : revertIndex(i);
    }

    private int toOuterIndex(int i) {
        return isRTL() ? revertIndex(i) : i;
    }

    private void updateArrowIndicatorResource(int i) {
        ArrowIndicator arrowIndicator = this.mArrowLeft;
        if (arrowIndicator != null) {
            arrowIndicator.setImageResource(i <= 0 ? this.mArrowLeftOffResId : this.mArrowLeftOnResId);
            this.mArrowRight.setImageResource(i >= ((getScreenCount() * this.mChildScreenWidth) - this.mScreenWidth) - this.mScrollOffset ? this.mArrowRightOffResId : this.mArrowRightOnResId);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x009a, code lost:
        r11.setPivotX(0.0f);
        r11.setPivotY(0.0f);
        r11.setRotation(0.0f);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x00a3, code lost:
        r11.setRotationX(0.0f);
        r11.setRotationY(0.0f);
        setCameraDistance(r11, r10.DEFAULT_CAMERA_DISTANCE);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00ec, code lost:
        r11.setRotationY((-r5) * r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00f1, code lost:
        setCameraDistance(r11, 5000.0f);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x017f, code lost:
        resetTransformation(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0182, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateChildStaticTransformation(View view) {
        float f;
        if (!(view instanceof Indicator)) {
            float measuredWidth = (float) view.getMeasuredWidth();
            float measuredHeight = (float) view.getMeasuredHeight();
            float f2 = measuredWidth / 2.0f;
            float f3 = measuredHeight / 2.0f;
            float scrollX = (((((float) getScrollX()) + (((float) getMeasuredWidth()) / 2.0f)) - ((float) view.getLeft())) - f2) / measuredWidth;
            switch (this.mScreenTransitionType) {
                case 0:
                case 1:
                    break;
                case 2:
                    if (scrollX != 0.0f && Math.abs(scrollX) <= 1.0f) {
                        view.setAlpha(((1.0f - Math.abs(scrollX)) * 0.7f) + 0.3f);
                        view.setTranslationX(0.0f);
                        view.setTranslationY(0.0f);
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        break;
                    }
                case 3:
                    if (scrollX != 0.0f && Math.abs(scrollX) <= 1.0f) {
                        view.setAlpha(1.0f);
                        view.setTranslationX(0.0f);
                        view.setTranslationY(0.0f);
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        view.setPivotX(f2);
                        view.setPivotY(measuredHeight);
                        view.setRotation((-scrollX) * 30.0f);
                        break;
                    }
                case 4:
                    if (scrollX != 0.0f && Math.abs(scrollX) <= 1.0f) {
                        view.setAlpha(1.0f);
                        view.setTranslationX(0.0f);
                        view.setTranslationY(0.0f);
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        if (scrollX < 0.0f) {
                            measuredWidth = 0.0f;
                        }
                        view.setPivotX(measuredWidth);
                        view.setPivotY(f3);
                        view.setRotation(0.0f);
                        view.setRotationX(0.0f);
                        view.setRotationY(scrollX * -90.0f);
                        break;
                    }
                case 5:
                    if (scrollX != 0.0f && Math.abs(scrollX) <= 1.0f) {
                        view.setAlpha(1.0f - Math.abs(scrollX));
                        view.setTranslationY(0.0f);
                        view.setTranslationX((measuredWidth * scrollX) - ((measuredWidth * Math.abs(scrollX)) * 0.3f));
                        float f4 = (0.3f * scrollX) + 1.0f;
                        view.setScaleX(f4);
                        view.setScaleY(f4);
                        view.setPivotX(0.0f);
                        view.setPivotY(f3);
                        view.setRotation(0.0f);
                        view.setRotationX(0.0f);
                        f = 45.0f;
                        break;
                    }
                case 7:
                    if (scrollX > 0.0f) {
                        float f5 = 1.0f - scrollX;
                        view.setAlpha(f5);
                        float f6 = (f5 * 0.4f) + 0.6f;
                        float f7 = 1.0f - f6;
                        view.setTranslationX(measuredWidth * f7 * 3.0f);
                        view.setTranslationY(measuredHeight * f7 * 0.5f);
                        view.setScaleX(f6);
                        view.setScaleY(f6);
                        break;
                    }
                    break;
                case 8:
                    if (scrollX != 0.0f && Math.abs(scrollX) <= 1.0f) {
                        view.setAlpha(1.0f - Math.abs(scrollX));
                        view.setTranslationX(measuredWidth * scrollX);
                        view.setTranslationY(0.0f);
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        view.setPivotX(f2);
                        view.setPivotY(f3);
                        view.setRotation(0.0f);
                        view.setRotationX(0.0f);
                        f = 90.0f;
                        break;
                    }
                case 9:
                    updateChildStaticTransformationByScreen(view, scrollX);
                    break;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0068  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateIndicatorPositions(int i, boolean z) {
        int i2;
        int i3;
        int i4;
        int i5;
        if (getWidth() > 0) {
            int screenCount = getScreenCount();
            int width = getWidth();
            int height = getHeight();
            for (int i6 = 0; i6 < this.mIndicatorCount; i6++) {
                View childAt = getChildAt(i6 + screenCount);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i7 = layoutParams.gravity;
                if (i7 != -1) {
                    int absoluteGravity = Gravity.getAbsoluteGravity(i7, getLayoutDirection()) & 7;
                    int i8 = i7 & 112;
                    if (absoluteGravity == 1) {
                        i5 = ((width - measuredWidth) / 2) + layoutParams.leftMargin;
                    } else if (absoluteGravity == 3 || absoluteGravity != 5) {
                        i2 = layoutParams.leftMargin;
                        if (i8 != 16) {
                            i4 = ((height - measuredHeight) / 2) + layoutParams.topMargin;
                        } else if (i8 == 48 || i8 != 80) {
                            i3 = layoutParams.topMargin;
                        } else {
                            i4 = height - measuredHeight;
                        }
                        i3 = i4 - layoutParams.bottomMargin;
                    } else {
                        i5 = width - measuredWidth;
                    }
                    i2 = i5 - layoutParams.rightMargin;
                    if (i8 != 16) {
                    }
                    i3 = i4 - layoutParams.bottomMargin;
                } else {
                    i3 = 0;
                    i2 = 0;
                }
                if (z || childAt.getHeight() <= 0 || childAt.getWidth() <= 0) {
                    childAt.layout(i2, i3, measuredWidth + i2, measuredHeight + i3);
                } else {
                    childAt.setTranslationX((float) i);
                }
            }
        }
    }

    private void updateScreenOffset() {
        int i;
        int i2 = this.mScreenAlignment;
        if (i2 == 0) {
            i = this.mScreenOffset;
        } else if (i2 == 1) {
            i = 0;
        } else if (i2 != 2) {
            if (i2 == 3) {
                i = this.mScreenWidth - this.mChildScreenWidth;
            }
            this.mScrollOffset += getPaddingLeft();
        } else {
            i = (this.mScreenWidth - this.mChildScreenWidth) / 2;
        }
        this.mScrollOffset = i;
        this.mScrollOffset += getPaddingLeft();
    }

    /* access modifiers changed from: private */
    public void updateSeekPoints(int i, int i2) {
        if (this.mScreenSeekBar != null) {
            int screenCount = getScreenCount();
            int i3 = 0;
            if (i >= 0) {
                for (int i4 = 0; i4 < this.mVisibleRange; i4++) {
                    int i5 = i + i4;
                    if (i5 >= screenCount) {
                        break;
                    }
                    this.mScreenSeekBar.getChildAt(i5).setSelected(false);
                }
            }
            while (i3 < this.mVisibleRange) {
                int i6 = i2 + i3;
                if (i6 < screenCount) {
                    this.mScreenSeekBar.getChildAt(i6).setSelected(true);
                    i3++;
                } else {
                    return;
                }
            }
        }
    }

    private void updateSlidePointPosition(int i) {
        int screenCount = getScreenCount();
        SlideBar slideBar = this.mSlideBar;
        if (slideBar != null && screenCount > 0) {
            int slideWidth = slideBar.getSlideWidth();
            int max = Math.max((slideWidth / screenCount) * this.mVisibleRange, 48);
            int i2 = this.mChildScreenWidth * screenCount;
            int i3 = i2 <= slideWidth ? 0 : ((slideWidth - max) * i) / (i2 - slideWidth);
            this.mSlideBar.setPosition(i3, max + i3);
            if (isHardwareAccelerated()) {
                this.mSlideBar.invalidate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addIndicator(View view, FrameLayout.LayoutParams layoutParams) {
        this.mIndicatorCount++;
        super.addView(view, -1, layoutParams);
    }

    /* access modifiers changed from: protected */
    public void addIndicatorAt(View view, FrameLayout.LayoutParams layoutParams, int i) {
        int max = Math.max(-1, Math.min(i, this.mIndicatorCount));
        if (max >= 0) {
            max += getScreenCount();
        }
        this.mIndicatorCount++;
        super.addView(view, max, layoutParams);
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        int screenCount = getScreenCount();
        boolean isRTL = isRTL();
        int i2 = 0;
        if (i >= 0) {
            if (isRTL) {
                i2 = screenCount - i;
            }
            screenCount = Math.min(i, screenCount);
        }
        if (isRTL) {
            screenCount = i2;
        }
        SeekBarIndicator seekBarIndicator = this.mScreenSeekBar;
        if (seekBarIndicator != null) {
            seekBarIndicator.addView(createSeekPoint(), screenCount, SEEK_POINT_LAYOUT_PARAMS);
        }
        this.mScreenCounter++;
        refreshScrollBound();
        super.addView(view, screenCount, layoutParams);
    }

    public boolean allowLongPress() {
        return this.mAllowLongPress;
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.mTouchX = (float) this.mScroller.getCurrX();
            ViewClass.setScrollXDirectly(this, (int) this.mTouchX);
            this.mSmoothingTime = ((float) System.nanoTime()) / NANOTIME_DIV;
            ViewClass.setScrollYDirectly(this, this.mScroller.getCurrY());
            postInvalidateOnAnimation();
        } else {
            int i = this.mNextScreen;
            if (i != -1) {
                setCurrentScreenInner(Math.max(0, Math.min(i, getScreenCount() - 1)));
                this.mNextScreen = -1;
                SnapScreenOnceNotification snapScreenOnceNotification = this.mSnapScreenOnceNotification;
                if (snapScreenOnceNotification != null) {
                    snapScreenOnceNotification.onSnapEnd(this);
                    this.mSnapScreenOnceNotification = null;
                }
            } else if (this.mTouchState == 1) {
                float nanoTime = ((float) System.nanoTime()) / NANOTIME_DIV;
                float scrollX = this.mTouchX - ((float) getScrollX());
                ViewClass.setScrollXDirectly(this, (int) (((float) getScrollX()) + (((float) Math.exp((double) ((nanoTime - this.mSmoothingTime) / SMOOTHING_CONSTANT))) * scrollX)));
                this.mSmoothingTime = nanoTime;
                if (scrollX > 1.0f || scrollX < -1.0f) {
                    postInvalidate();
                }
            }
        }
        updateIndicatorPositions(getScrollX(), false);
        updateSlidePointPosition(getScrollX());
        updateArrowIndicatorResource(getScrollX());
    }

    public boolean dispatchUnhandledMove(View view, int i) {
        if (i == 17) {
            int i2 = this.mCurrentScreen;
            if (i2 > 0) {
                snapToScreen(i2 - 1, 0, false);
                return true;
            }
        } else if (i == 66 && this.mCurrentScreen < getScreenCount() - 1) {
            snapToScreen(this.mCurrentScreen + 1, 0, false);
            return true;
        }
        return super.dispatchUnhandledMove(view, i);
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        updateChildStaticTransformation(view);
        return super.drawChild(canvas, view, j);
    }

    /* access modifiers changed from: protected */
    public void finishCurrentGesture() {
        this.mCurrentGestureFinished = true;
        setTouchState(null, 0);
    }

    public View getCurrentScreen() {
        return doGetScreen(this.mCurrentScreen, true);
    }

    public int getCurrentScreenIndex() {
        int i = this.mNextScreen;
        if (i == -1) {
            i = this.mCurrentScreen;
        }
        return toOuterIndex(i);
    }

    public View getScreen(int i) {
        return doGetScreen(i, false);
    }

    public final int getScreenCount() {
        return this.mScreenCounter;
    }

    public int getScreenTransitionType() {
        return this.mScreenTransitionType;
    }

    public ImageView getSeekPointView(int i) {
        return (ImageView) this.mScreenSeekBar.getChildAt(toInnerIndex(i, false));
    }

    /* access modifiers changed from: protected */
    public int getTouchState() {
        return this.mTouchState;
    }

    public int getVisibleRange() {
        return this.mVisibleRange;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        computeScroll();
        showSlideBar();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        if (r0 != 3) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001e, code lost:
        if (scrolledFarEnough(r6) != false) goto L_0x0050;
     */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0062  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    onTouchEventUnique(motionEvent);
                    if (this.mTouchState == 0) {
                    }
                }
                if (2 != (motionEvent.getAction() & 255)) {
                    onTouchEventUnique(motionEvent);
                }
                if (!this.mCurrentGestureFinished) {
                    int i = this.mTouchState;
                    if (i == 0 || i == 3) {
                        return false;
                    }
                }
                return true;
            }
            setTouchState(motionEvent, 0);
            if (2 != (motionEvent.getAction() & 255)) {
            }
            if (!this.mCurrentGestureFinished) {
            }
            return true;
        }
        motionEvent.setAction(3);
        this.mScaleDetector.onTouchEvent(motionEvent);
        motionEvent.setAction(0);
        this.mCurrentGestureFinished = false;
        this.mTouchIntercepted = false;
        this.mLastMotionX = motionEvent.getX();
        this.mLastMotionY = motionEvent.getY();
        if (this.mScroller.isFinished()) {
            this.mAllowLongPress = true;
            if (2 != (motionEvent.getAction() & 255)) {
            }
            if (!this.mCurrentGestureFinished) {
            }
            return true;
        }
        this.mScroller.abortAnimation();
        setTouchState(motionEvent, 1);
        if (2 != (motionEvent.getAction() & 255)) {
        }
        if (!this.mCurrentGestureFinished) {
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        updateIndicatorPositions(getScrollX(), true);
        int screenCount = getScreenCount();
        int i6 = 0;
        for (int i7 = 0; i7 < screenCount; i7++) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() != 8) {
                childAt.layout(i6, getPaddingTop() + this.mScreenPaddingTop, childAt.getMeasuredWidth() + i6, getPaddingTop() + this.mScreenPaddingTop + childAt.getMeasuredHeight());
                i6 += childAt.getMeasuredWidth();
            }
        }
        if (this.mScrollWholeScreen) {
            int i8 = this.mCurrentScreen;
            int i9 = this.mVisibleRange;
            if (i8 % i9 > 0) {
                i5 = i8 - (i8 % i9);
                doSetCurrentScreen(i5, true);
                this.mPrevScreenWidth = this.mScreenWidth;
            }
        }
        int i10 = this.mPrevScreenWidth;
        if (i10 > 0 && i10 != this.mScreenWidth) {
            i5 = this.mCurrentScreen;
            doSetCurrentScreen(i5, true);
        }
        this.mPrevScreenWidth = this.mScreenWidth;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        this.mWidthMeasureSpec = i;
        this.mHeightMeasureSpec = i2;
        int screenCount = getScreenCount();
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < this.mIndicatorCount; i5++) {
            View childAt = getChildAt(i5 + screenCount);
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            childAt.measure(ViewGroup.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight(), layoutParams.width), ViewGroup.getChildMeasureSpec(i2, getPaddingTop() + this.mScreenPaddingTop + getPaddingBottom() + this.mScreenPaddingBottom, layoutParams.height));
            i3 = Math.max(i3, childAt.getMeasuredWidth());
            i4 = Math.max(i4, childAt.getMeasuredHeight());
        }
        int i6 = 0;
        int i7 = 0;
        for (int i8 = 0; i8 < screenCount; i8++) {
            View childAt2 = getChildAt(i8);
            ViewGroup.LayoutParams layoutParams2 = childAt2.getLayoutParams();
            childAt2.measure(ViewGroup.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight(), layoutParams2.width), ViewGroup.getChildMeasureSpec(i2, getPaddingTop() + this.mScreenPaddingTop + getPaddingBottom() + this.mScreenPaddingBottom, layoutParams2.height));
            i6 = Math.max(i6, childAt2.getMeasuredWidth());
            i7 = Math.max(i7, childAt2.getMeasuredHeight());
        }
        int max = Math.max(i6, i3);
        setMeasuredDimension(ViewGroup.resolveSize(max + getPaddingLeft() + getPaddingRight(), i), ViewGroup.resolveSize(Math.max(i7, i4) + getPaddingTop() + this.mScreenPaddingTop + getPaddingBottom() + this.mScreenPaddingBottom, i2));
        if (screenCount > 0) {
            this.mChildScreenWidth = i6;
            this.mScreenWidth = (MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight();
            updateScreenOffset();
            int i9 = this.mChildScreenWidth;
            if (i9 > 0) {
                this.mVisibleRange = Math.max(1, (this.mScreenWidth + ((int) (((float) i9) * this.mVisibleExtentionRatio))) / i9);
            }
            setOverScrollRatio(this.mOverScrollRatio);
        }
        if (this.mFirstLayout && this.mVisibleRange > 0) {
            this.mFirstLayout = false;
            setHorizontalScrollBarEnabled(false);
            setCurrentScreen(this.mCurrentScreen);
            setHorizontalScrollBarEnabled(true);
        }
    }

    public void onPause() {
        if (!this.mScroller.isFinished()) {
            int currX = this.mScroller.getCurrX();
            int i = this.mChildScreenWidth;
            doSetCurrentScreen((int) Math.floor((double) ((currX + (i / 2)) / i)), true);
            this.mScroller.abortAnimation();
        }
    }

    /* access modifiers changed from: protected */
    public void onPinchIn(ScaleGestureDetector scaleGestureDetector) {
    }

    /* access modifiers changed from: protected */
    public void onPinchOut(ScaleGestureDetector scaleGestureDetector) {
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        int i = savedState.currentScreen;
        if (i != -1) {
            doSetCurrentScreen(i, true);
        }
    }

    public void onResume() {
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentScreen = this.mCurrentScreen;
        return savedState;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mCurrentGestureFinished) {
            return true;
        }
        if (this.mTouchIntercepted) {
            onTouchEventUnique(motionEvent);
        }
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            int i = 0;
            if (action != 1) {
                if (action == 2) {
                    if (this.mTouchState == 0 && scrolledFarEnough(motionEvent)) {
                        setTouchState(motionEvent, 1);
                    }
                    if (this.mTouchState == 1) {
                        int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex == -1) {
                            setTouchState(motionEvent, 1);
                            findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                        }
                        float x = motionEvent.getX(findPointerIndex);
                        float f = this.mLastMotionX - x;
                        this.mLastMotionX = x;
                        if (f != 0.0f) {
                            scrollTo(Math.round(this.mTouchX + f), 0);
                        } else {
                            awakenScrollBars();
                        }
                    }
                } else if (action != 3) {
                    if (action == 6) {
                        int action2 = (motionEvent.getAction() & 65280) >> 8;
                        if (motionEvent.getPointerId(action2) == this.mActivePointerId) {
                            if (action2 == 0) {
                                i = 1;
                            }
                            this.mLastMotionX = motionEvent.getX(i);
                            this.mActivePointerId = motionEvent.getPointerId(i);
                            this.mGestureVelocityTracker.init(this.mActivePointerId);
                        }
                    }
                }
            }
            if (this.mTouchState == 1) {
                snapByVelocity(this.mActivePointerId);
            }
            setTouchState(motionEvent, 0);
        }
        this.mTouchIntercepted = true;
        return true;
    }

    public void removeAllScreens() {
        removeScreensInLayout(0, getScreenCount());
        requestLayout();
        invalidate();
    }

    public void removeAllViewsInLayout() {
        this.mIndicatorCount = 0;
        this.mScreenCounter = 0;
        super.removeAllViewsInLayout();
    }

    /* access modifiers changed from: protected */
    public void removeIndicator(View view) {
        int indexOfChild = indexOfChild(view);
        if (indexOfChild >= getScreenCount()) {
            this.mIndicatorCount--;
            super.removeViewAt(indexOfChild);
            return;
        }
        throw new InvalidParameterException("The view passed through the parameter must be indicator.");
    }

    public void removeScreen(int i) {
        if (i < getScreenCount()) {
            int innerIndex = toInnerIndex(i, false);
            if (innerIndex == this.mCurrentScreen) {
                if (!this.mScrollWholeScreen) {
                    doSetCurrentScreen(Math.max(0, -1), true);
                } else if (i != 0 && i == getScreenCount() - 1) {
                    snapToScreen(i - 1);
                }
            }
            SeekBarIndicator seekBarIndicator = this.mScreenSeekBar;
            if (seekBarIndicator != null) {
                seekBarIndicator.removeViewAt(innerIndex);
            }
            this.mScreenCounter--;
            super.removeViewAt(innerIndex);
            return;
        }
        throw new InvalidParameterException("The view specified by the index must be a screen.");
    }

    public void removeScreensInLayout(int i, int i2) {
        if (isRTL()) {
            i = (toInnerIndex(i, false) - i2) + 1;
        }
        if (i >= 0 && i < getScreenCount()) {
            int min = Math.min(i2, getScreenCount() - i);
            SeekBarIndicator seekBarIndicator = this.mScreenSeekBar;
            if (seekBarIndicator != null) {
                seekBarIndicator.removeViewsInLayout(i, min);
            }
            this.mScreenCounter = 0;
            super.removeViewsInLayout(i, min);
        }
    }

    public void removeView(View view) {
        throw new UnsupportedOperationException("ScreenView doesn't support remove view directly.");
    }

    public void removeViewAt(int i) {
        throw new UnsupportedOperationException("ScreenView doesn't support remove view directly.");
    }

    public void removeViewInLayout(View view) {
        throw new UnsupportedOperationException("ScreenView doesn't support remove view directly.");
    }

    public void removeViews(int i, int i2) {
        throw new UnsupportedOperationException("ScreenView doesn't support remove view directly.");
    }

    public void removeViewsInLayout(int i, int i2) {
        throw new UnsupportedOperationException("ScreenView doesn't support remove view directly.");
    }

    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
        int indexOfChild = indexOfChild(view);
        if (indexOfChild >= getScreenCount()) {
            return super.requestChildRectangleOnScreen(view, rect, z);
        }
        if (indexOfChild == this.mCurrentScreen && this.mScroller.isFinished()) {
            return false;
        }
        snapToScreen(indexOfChild);
        return true;
    }

    /* access modifiers changed from: protected */
    public void resetTransformation(View view) {
        view.setAlpha(1.0f);
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);
        view.setPivotX(0.0f);
        view.setPivotY(0.0f);
        view.setRotation(0.0f);
        view.setRotationX(0.0f);
        view.setRotationY(0.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        setCameraDistance(view, this.DEFAULT_CAMERA_DISTANCE);
    }

    public void scrollTo(int i, int i2) {
        this.mTouchX = (float) Math.max(this.mScrollLeftBound, Math.min(i, this.mScrollRightBound));
        this.mSmoothingTime = ((float) System.nanoTime()) / NANOTIME_DIV;
        super.scrollTo((int) this.mTouchX, i2);
    }

    public void scrollToScreen(int i) {
        doScrollToScreen(i, false);
    }

    public void setAllowLongPress(boolean z) {
        this.mAllowLongPress = z;
    }

    public void setArrowIndicatorMarginRect(Rect rect) {
        FrameLayout.LayoutParams layoutParams;
        FrameLayout.LayoutParams layoutParams2;
        if (rect != null) {
            ArrowIndicator arrowIndicator = this.mArrowLeft;
            if (arrowIndicator == null) {
                layoutParams2 = new FrameLayout.LayoutParams(-2, -2, 19);
                this.mArrowLeft = new ArrowIndicator(getContext());
                this.mArrowLeft.setImageResource(this.mArrowLeftOnResId);
                addIndicator(this.mArrowLeft, layoutParams2);
                layoutParams = new FrameLayout.LayoutParams(-2, -2, 21);
                this.mArrowRight = new ArrowIndicator(getContext());
                this.mArrowRight.setImageResource(this.mArrowRightOnResId);
                addIndicator(this.mArrowRight, layoutParams);
            } else {
                layoutParams2 = (FrameLayout.LayoutParams) arrowIndicator.getLayoutParams();
                layoutParams = (FrameLayout.LayoutParams) this.mArrowRight.getLayoutParams();
            }
            layoutParams2.setMargins(rect.left, rect.top, 0, rect.bottom);
            layoutParams.setMargins(0, rect.top, rect.right, rect.bottom);
            return;
        }
        ArrowIndicator arrowIndicator2 = this.mArrowLeft;
        if (arrowIndicator2 != null) {
            removeIndicator(arrowIndicator2);
            removeIndicator(this.mArrowRight);
            this.mArrowLeft = null;
            this.mArrowRight = null;
        }
    }

    public void setArrowIndicatorResource(int i, int i2, int i3, int i4) {
        this.mArrowLeftOnResId = i;
        this.mArrowLeftOffResId = i2;
        this.mArrowRightOnResId = i3;
        this.mArrowRightOffResId = i4;
    }

    public void setConfirmHorizontalScrollRatio(float f) {
        this.mConfirmHorizontalScrollRatio = f;
    }

    public void setCurrentScreen(int i) {
        doSetCurrentScreen(i, false);
    }

    public void setIndicatorBarVisibility(int i) {
        setSeekBarVisibility(i);
        setSlideBarVisibility(i);
    }

    public void setMaximumSnapVelocity(int i) {
        this.mMaximumVelocity = i;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        int screenCount = getScreenCount();
        for (int i = 0; i < screenCount; i++) {
            getChildAt(i).setOnLongClickListener(onLongClickListener);
        }
    }

    public void setOverScrollRatio(float f) {
        this.mOverScrollRatio = f;
        refreshScrollBound();
    }

    public void setOvershootTension(float f) {
        this.mOvershootTension = f;
        ScreenViewOvershootInterpolator screenViewOvershootInterpolator = this.mScrollInterpolator;
        if (screenViewOvershootInterpolator != null) {
            screenViewOvershootInterpolator.mTension = f;
        }
    }

    public void setScreenAlignment(int i) {
        this.mScreenAlignment = i;
    }

    public void setScreenOffset(int i) {
        this.mScreenOffset = i;
        this.mScreenAlignment = 0;
        requestLayout();
    }

    public void setScreenPadding(Rect rect) {
        if (rect != null) {
            this.mScreenPaddingTop = rect.top;
            this.mScreenPaddingBottom = rect.bottom;
            setPadding(rect.left, 0, rect.right, 0);
            return;
        }
        throw new InvalidParameterException("The padding parameter can not be null.");
    }

    public void setScreenSnapDuration(int i) {
        this.mScreenSnapDuration = i;
    }

    public void setScreenTransitionType(int i) {
        if (i != this.mScreenTransitionType) {
            this.mScreenTransitionType = i;
            switch (this.mScreenTransitionType) {
                case 0:
                case 3:
                    setOvershootTension(DEFAULT_OVER_SHOOT_TENSION);
                    setScreenSnapDuration(300);
                    return;
                case 1:
                case 2:
                case 7:
                    setOvershootTension(0.0f);
                    setScreenSnapDuration(270);
                    return;
                case 4:
                case 5:
                    setOvershootTension(0.0f);
                    break;
                case 8:
                    setOvershootTension(DEFAULT_OVER_SHOOT_TENSION);
                    break;
                default:
                    return;
            }
            setScreenSnapDuration(m.cq);
        }
    }

    public void setScrollWholeScreen(boolean z) {
        this.mScrollWholeScreen = z;
    }

    public void setSeekBarPosition(FrameLayout.LayoutParams layoutParams) {
        if (layoutParams != null) {
            SeekBarIndicator seekBarIndicator = this.mScreenSeekBar;
            if (seekBarIndicator == null) {
                this.mScreenSeekBar = new SeekBarIndicator(getContext());
                this.mScreenSeekBar.setLayoutDirection(0);
                this.mScreenSeekBar.setGravity(16);
                this.mScreenSeekBar.setAnimationCacheEnabled(false);
                for (int i = 0; i < getChildCount(); i++) {
                    this.mScreenSeekBar.addView(createSeekPoint(), SEEK_POINT_LAYOUT_PARAMS);
                }
            } else if (seekBarIndicator.getParent() != null) {
                this.mScreenSeekBar.setLayoutParams(layoutParams);
                return;
            }
            addIndicator(this.mScreenSeekBar, layoutParams);
            return;
        }
        SeekBarIndicator seekBarIndicator2 = this.mScreenSeekBar;
        if (seekBarIndicator2 != null) {
            removeIndicator(seekBarIndicator2);
            this.mScreenSeekBar = null;
        }
    }

    public void setSeekBarVisibility(int i) {
        SeekBarIndicator seekBarIndicator = this.mScreenSeekBar;
        if (seekBarIndicator != null) {
            seekBarIndicator.setVisibility(i);
        }
    }

    public void setSeekPointResource(int i) {
        this.mSeekPointResId = i;
    }

    public void setSlideBarPosition(FrameLayout.LayoutParams layoutParams) {
        setSlideBarPosition(layoutParams, R.drawable.screen_view_slide_bar, R.drawable.screen_view_slide_bar_bg, false);
    }

    public void setSlideBarPosition(FrameLayout.LayoutParams layoutParams, int i, int i2, boolean z) {
        this.mIsSlideBarAutoHide = z;
        if (layoutParams != null) {
            SlideBar slideBar = this.mSlideBar;
            if (slideBar == null) {
                this.mSlideBar = new SlideBar(getContext(), i, i2);
                this.mSlideBar.setOnTouchListener(new SliderTouchListener());
                this.mSlideBar.setAnimationCacheEnabled(false);
                addIndicator(this.mSlideBar, layoutParams);
                return;
            }
            slideBar.setLayoutParams(layoutParams);
            return;
        }
        SlideBar slideBar2 = this.mSlideBar;
        if (slideBar2 != null) {
            removeIndicator(slideBar2);
            this.mSlideBar = null;
        }
    }

    public void setSlideBarVisibility(int i) {
        SlideBar slideBar = this.mSlideBar;
        if (slideBar != null) {
            slideBar.setVisibility(i);
        }
    }

    public void setTouchSlop(int i) {
        this.mTouchSlop = i;
    }

    public void setVisibility(int i) {
        if (i == 0) {
            showSlideBar();
        }
        super.setVisibility(i);
    }

    public void setVisibleExtentionRatio(float f) {
        this.mVisibleExtentionRatio = f;
    }

    /* access modifiers changed from: protected */
    public void snapByVelocity(int i, int i2) {
        int i3;
        if (i2 == 1) {
            int i4 = this.mCurrentScreen;
            if (i4 > 0) {
                snapToScreen(i4 - this.mVisibleRange, i, true);
                return;
            }
        }
        if (i2 == 2 && this.mCurrentScreen < getScreenCount() - 1) {
            i3 = this.mCurrentScreen + this.mVisibleRange;
        } else if (i2 == 3) {
            i3 = this.mCurrentScreen;
        } else {
            i3 = (getScrollX() + ((this.mChildScreenWidth * (this.mScrollWholeScreen ? this.mVisibleRange : 1)) >> 1)) / this.mChildScreenWidth;
            i = 0;
        }
        snapToScreen(i3, i, true);
    }

    public void snapToScreen(int i) {
        snapToScreen(toInnerIndex(i, false), 0, false);
    }

    /* access modifiers changed from: protected */
    public void snapToScreen(int i, int i2, boolean z) {
        snapToScreen(i, i2, z, null);
    }

    /* access modifiers changed from: protected */
    public void snapToScreen(int i, int i2, boolean z, SnapScreenOnceNotification snapScreenOnceNotification) {
        int i3;
        if (this.mScreenWidth > 0) {
            if (this.mScrollWholeScreen) {
                this.mNextScreen = Math.max(0, Math.min(i, getScreenCount() - 1));
                int i4 = this.mNextScreen;
                i3 = i4 - (i4 % this.mVisibleRange);
            } else {
                i3 = Math.max(0, Math.min(i, getScreenCount() - this.mVisibleRange));
            }
            this.mNextScreen = i3;
            int max = Math.max(1, Math.abs(this.mNextScreen - this.mCurrentScreen));
            if (!this.mScroller.isFinished()) {
                SnapScreenOnceNotification snapScreenOnceNotification2 = this.mSnapScreenOnceNotification;
                if (snapScreenOnceNotification2 != null) {
                    snapScreenOnceNotification2.onSnapCancelled(this);
                }
                this.mScroller.abortAnimation();
            }
            this.mSnapScreenOnceNotification = snapScreenOnceNotification;
            int abs = Math.abs(i2);
            if (z) {
                this.mScrollInterpolator.setDistance(max, abs);
            } else {
                this.mScrollInterpolator.disableSettle();
            }
            int scrollX = ((this.mNextScreen * this.mChildScreenWidth) - this.mScrollOffset) - getScrollX();
            if (scrollX != 0) {
                int abs2 = (Math.abs(scrollX) * this.mScreenSnapDuration) / this.mScreenWidth;
                if (abs > 0) {
                    abs2 += (int) ((((float) abs2) / (((float) abs) / BASELINE_FLING_VELOCITY)) * 0.4f);
                }
                int max2 = Math.max(this.mScreenSnapDuration, abs2);
                this.mScroller.startScroll(getScrollX(), 0, scrollX, 0, max <= 1 ? Math.min(max2, this.mScreenSnapDuration * 2) : max2);
                invalidate();
            }
        }
    }

    public void snapToScreen(int i, SnapScreenOnceNotification snapScreenOnceNotification) {
        snapToScreen(toInnerIndex(i, false), 0, false, snapScreenOnceNotification);
    }

    /* access modifiers changed from: protected */
    public void updateChildStaticTransformationByScreen(View view, float f) {
    }
}
