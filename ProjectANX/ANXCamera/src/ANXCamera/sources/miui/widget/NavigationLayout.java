package miui.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.core.view.ViewCompat;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.widget.ViewDragHelper;
import com.miui.internal.widget.ViewDragHelper.Callback;
import miui.R;
import miui.util.ViewUtils;

public class NavigationLayout extends ViewGroup {
    public static final int ABSOLUTE = 0;
    private static final int DEFAULT_SCRIM_COLOR = -856295433;
    public static final int DRAWER_ENABLED_LANDSCAPE = 2;
    public static final int DRAWER_ENABLED_NONE = 0;
    public static final int DRAWER_ENABLED_PORTRAIT = 1;
    public static final int DRAWER_MODE_CONTENT_SQUEEZED = 2;
    public static final int DRAWER_MODE_OVERLAY = 0;
    public static final int DRAWER_MODE_PUSHED_AWAY = 1;
    private static final float HALF_OFFSET = 0.5f;
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 150;
    public static final int RELATIVE_TO_PARENT = 1;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private boolean mChildrenCanceledTouch;
    private View mContent;
    private Drawable mDivider;
    private int mDividerWidth;
    /* access modifiers changed from: private */
    public final ViewDragHelper mDragger;
    private boolean mDrawerEnabled;
    private int mDrawerEnabledOrientation;
    private int mDrawerMode;
    private boolean mFirstMeasure;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private WidthDescription mLandscapeWidthDescription;
    /* access modifiers changed from: private */
    public boolean mLayoutRtl;
    /* access modifiers changed from: private */
    public NavigationListener mListener;
    private int mLockMode;
    /* access modifiers changed from: private */
    public View mNavigation;
    /* access modifiers changed from: private */
    public Runnable mPeekRunnable;
    private WidthDescription mPortraitWidthDescription;
    /* access modifiers changed from: private */
    public View mScrimAnimationView;
    private ValueAnimator mScrimAnimator;
    private AnimatorUpdateListener mScrimAnimatorListener;
    private int mScrimColor;
    private float mScrimOpacity;
    /* access modifiers changed from: private */
    public float mScrimOpacityAnimatior;
    private Paint mScrimPaint;
    private Drawable mShadow;
    private Rect mTmpRect;

    public class LayoutParams extends MarginLayoutParams {
        boolean isPeeking;
        float offset;

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

    public interface NavigationListener {
        void onDrawerClosed();

        void onDrawerDragStateChanged(int i);

        void onDrawerEnableStateChange(boolean z);

        void onDrawerOpened();

        void onDrawerSlide(float f);
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
        float offset;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.offset = parcel.readFloat();
        }

        private SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeFloat(this.offset);
        }
    }

    class ViewDragCallback extends Callback {
        private ViewDragCallback() {
        }

        public int clampViewPositionHorizontal(View view, int i, int i2) {
            int width = NavigationLayout.this.mLayoutRtl ? NavigationLayout.this.getWidth() - view.getWidth() : -view.getWidth();
            return Math.max(width, Math.min(i, view.getWidth() + width));
        }

        public int getViewHorizontalDragRange(View view) {
            if (view == NavigationLayout.this.mNavigation) {
                return NavigationLayout.this.mNavigation.getWidth();
            }
            return 0;
        }

        public void onEdgeDragStarted(int i, int i2) {
            if (NavigationLayout.this.getDrawerLockMode() == 0) {
                NavigationLayout.this.mDragger.captureChildView(NavigationLayout.this.mNavigation, i2);
                NavigationLayout navigationLayout = NavigationLayout.this;
                navigationLayout.removeCallbacks(navigationLayout.mPeekRunnable);
            }
        }

        public void onEdgeTouched(int i, int i2) {
            NavigationLayout navigationLayout = NavigationLayout.this;
            navigationLayout.postDelayed(navigationLayout.mPeekRunnable, 150);
        }

        public void onViewCaptured(View view, int i) {
            ((LayoutParams) view.getLayoutParams()).isPeeking = false;
        }

        public void onViewDragStateChanged(int i) {
            if (NavigationLayout.this.mListener != null) {
                if (i == 0) {
                    if (NavigationLayout.this.isNavigationDrawerOpen()) {
                        NavigationLayout.this.mListener.onDrawerOpened();
                    } else {
                        NavigationLayout.this.mListener.onDrawerClosed();
                    }
                }
                NavigationLayout.this.mListener.onDrawerDragStateChanged(i);
            }
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            if (view == NavigationLayout.this.mNavigation) {
                int width = NavigationLayout.this.mNavigation.getWidth();
                NavigationLayout.this.setNavigationOffset(((float) (NavigationLayout.this.mLayoutRtl ? NavigationLayout.this.getWidth() - i : i + width)) / ((float) width));
                NavigationLayout.this.invalidate();
            }
        }

        public void onViewReleased(View view, float f, float f2) {
            float access$900 = NavigationLayout.this.getNavigationOffset();
            int width = view.getWidth();
            boolean z = false;
            int width2 = NavigationLayout.this.mLayoutRtl ? NavigationLayout.this.getWidth() - width : 0;
            int width3 = NavigationLayout.this.mLayoutRtl ? NavigationLayout.this.getWidth() : -width;
            if (!NavigationLayout.this.mLayoutRtl ? f > 0.0f : f < 0.0f) {
                z = true;
            }
            if (z || (f == 0.0f && access$900 > 0.5f)) {
                width3 = width2;
            }
            NavigationLayout.this.mDragger.settleCapturedViewAt(width3, view.getTop());
            NavigationLayout.this.invalidate();
        }

        public boolean tryCaptureView(View view, int i) {
            return view == NavigationLayout.this.mNavigation && NavigationLayout.this.getDrawerLockMode() == 0;
        }
    }

    class WidthDescription {
        public int type;
        public float value;

        private WidthDescription() {
        }

        static WidthDescription parseValue(TypedValue typedValue, Resources resources) {
            WidthDescription widthDescription = new WidthDescription();
            if (typedValue == null) {
                widthDescription.type = 1;
                widthDescription.value = 0.3f;
            } else {
                int i = typedValue.type;
                if (i == 6) {
                    widthDescription.type = 1;
                    widthDescription.value = TypedValue.complexToFloat(typedValue.data);
                    return widthDescription;
                } else if (i == 4) {
                    widthDescription.type = 1;
                    widthDescription.value = typedValue.getFloat();
                    return widthDescription;
                } else if (i == 5) {
                    widthDescription.type = 0;
                    widthDescription.value = (float) TypedValue.complexToDimensionPixelSize(typedValue.data, resources.getDisplayMetrics());
                    return widthDescription;
                }
            }
            widthDescription.type = 1;
            widthDescription.value = 0.3f;
            return widthDescription;
        }
    }

    public NavigationLayout(Context context) {
        this(context, null);
    }

    public NavigationLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.navigationLayoutStyle);
    }

    public NavigationLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTmpRect = new Rect();
        this.mScrimColor = DEFAULT_SCRIM_COLOR;
        this.mScrimPaint = new Paint();
        this.mFirstMeasure = true;
        this.mLockMode = 0;
        this.mPeekRunnable = new Runnable() {
            public void run() {
                int i;
                View access$000 = NavigationLayout.this.mNavigation;
                int edgeSize = NavigationLayout.this.mDragger.getEdgeSize();
                int i2 = 0;
                if (NavigationLayout.this.mLayoutRtl) {
                    if (access$000 != null) {
                        i2 = NavigationLayout.this.getWidth();
                    }
                    i = i2 - edgeSize;
                } else {
                    if (access$000 != null) {
                        i2 = -access$000.getWidth();
                    }
                    i = i2 + edgeSize;
                }
                if (access$000 != null && access$000.getLeft() < i && NavigationLayout.this.getDrawerLockMode() == 0) {
                    LayoutParams layoutParams = (LayoutParams) access$000.getLayoutParams();
                    NavigationLayout.this.mDragger.smoothSlideViewTo(access$000, i, access$000.getTop());
                    layoutParams.isPeeking = true;
                    NavigationLayout.this.invalidate();
                    NavigationLayout.this.cancelChildViewTouch();
                }
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.NavigationLayout, i, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.NavigationLayout_navigationDivider);
        if (drawable != null) {
            setDivider(drawable);
        }
        Drawable drawable2 = obtainStyledAttributes.getDrawable(R.styleable.NavigationLayout_navigationShadow);
        if (drawable2 != null) {
            setNavigationShadow(drawable2);
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.NavigationLayout_navigationDividerWidth, 0);
        if (dimensionPixelSize != 0) {
            setDividerWidth(dimensionPixelSize);
        }
        this.mScrimColor = obtainStyledAttributes.getColor(R.styleable.NavigationLayout_navigationScrimColor, DEFAULT_SCRIM_COLOR);
        this.mDrawerEnabledOrientation = obtainStyledAttributes.getInt(R.styleable.NavigationLayout_drawerEnabledOrientation, 0);
        this.mPortraitWidthDescription = WidthDescription.parseValue(obtainStyledAttributes.peekValue(R.styleable.NavigationLayout_portraitNavigationWidth), getResources());
        this.mLandscapeWidthDescription = WidthDescription.parseValue(obtainStyledAttributes.peekValue(R.styleable.NavigationLayout_landscapeNavigationWidth), getResources());
        this.mDrawerMode = obtainStyledAttributes.getInt(R.styleable.NavigationLayout_drawerMode, 0);
        obtainStyledAttributes.recycle();
        this.mDragger = ViewDragHelper.create(this, 0.5f, new ViewDragCallback());
        this.mDragger.setMinVelocity(getResources().getDisplayMetrics().density * 400.0f);
        setFocusableInTouchMode(true);
    }

    /* access modifiers changed from: private */
    public void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).dispatchTouchEvent(obtain);
            }
            obtain.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }

    private void closePeekingDrawer() {
        LayoutParams layoutParams = (LayoutParams) this.mNavigation.getLayoutParams();
        if (layoutParams.isPeeking) {
            layoutParams.isPeeking = false;
            closeNavigationDrawer(true);
        }
    }

    private void drawDivider(Canvas canvas) {
        Rect rect = this.mTmpRect;
        int measuredWidth = this.mNavigation.getMeasuredWidth();
        if (this.mLayoutRtl) {
            measuredWidth = (getWidth() - measuredWidth) - this.mDividerWidth;
        }
        rect.set(measuredWidth, getPaddingTop(), this.mDividerWidth + measuredWidth, getBottom() - getPaddingBottom());
        Drawable drawable = this.mDivider;
        drawable.setBounds(rect);
        drawable.draw(canvas);
    }

    private void drawNavigationDisableScrim(Canvas canvas) {
        float f = this.mScrimOpacityAnimatior;
        if (f > 0.0f && this.mScrimAnimationView != null) {
            int i = this.mScrimColor;
            this.mScrimPaint.setColor((((int) (((float) ((-16777216 & i) >>> 24)) * f)) << 24) | (i & ViewCompat.MEASURED_SIZE_MASK));
            canvas.drawRect((float) this.mScrimAnimationView.getLeft(), (float) this.mScrimAnimationView.getTop(), (float) this.mScrimAnimationView.getRight(), (float) this.mScrimAnimationView.getBottom(), this.mScrimPaint);
        }
    }

    private void drawScrim(Canvas canvas) {
        float f = this.mScrimOpacity;
        if (f > 0.0f) {
            int i = this.mScrimColor;
            this.mScrimPaint.setColor((((int) (((float) ((-16777216 & i) >>> 24)) * f)) << 24) | (i & ViewCompat.MEASURED_SIZE_MASK));
            canvas.drawRect((float) (this.mLayoutRtl ? 0 : this.mNavigation.getRight()), 0.0f, (float) (this.mLayoutRtl ? this.mNavigation.getLeft() : getWidth()), (float) getHeight(), this.mScrimPaint);
        }
    }

    private void drawShadow(Canvas canvas) {
        Drawable drawable = this.mShadow;
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int left = this.mLayoutRtl ? this.mNavigation.getLeft() - intrinsicWidth : this.mNavigation.getRight();
            this.mShadow.setBounds(left, this.mNavigation.getTop(), intrinsicWidth + left, this.mNavigation.getBottom());
            this.mShadow.draw(canvas);
        }
    }

    /* access modifiers changed from: private */
    public float getNavigationOffset() {
        return ((LayoutParams) this.mNavigation.getLayoutParams()).offset;
    }

    private AnimatorUpdateListener getScrimAnimatorListener() {
        if (this.mScrimAnimatorListener == null) {
            this.mScrimAnimatorListener = new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (NavigationLayout.this.mScrimAnimationView != null) {
                        NavigationLayout.this.mScrimOpacityAnimatior = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                        NavigationLayout navigationLayout = NavigationLayout.this;
                        navigationLayout.postInvalidateOnAnimation(navigationLayout.mScrimAnimationView.getLeft(), NavigationLayout.this.mScrimAnimationView.getTop(), NavigationLayout.this.mScrimAnimationView.getRight(), NavigationLayout.this.mScrimAnimationView.getBottom());
                    }
                }
            };
        }
        return this.mScrimAnimatorListener;
    }

    private boolean isDrawerPeeking() {
        return ((LayoutParams) this.mNavigation.getLayoutParams()).isPeeking;
    }

    private void pullChildren() {
        if (this.mContent == null) {
            this.mContent = findViewById(R.id.content);
            this.mNavigation = findViewById(R.id.navigation);
        }
    }

    private void setChildViewEnabled(View view, boolean z) {
        if (view.isEnabled() != z) {
            View view2 = this.mScrimAnimationView;
            if (view2 == null || view2 == view || view2.isEnabled() || z) {
                view.setEnabled(z);
                ValueAnimator valueAnimator = this.mScrimAnimator;
                float f = 1.0f;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    ValueAnimator valueAnimator2 = this.mScrimAnimator;
                    float[] fArr = new float[2];
                    fArr[0] = z ? 1.0f : 0.0f;
                    fArr[1] = z ? 0.0f : 1.0f;
                    valueAnimator2.setFloatValues(fArr);
                } else {
                    float[] fArr2 = new float[2];
                    fArr2[0] = z ? 1.0f : 0.0f;
                    fArr2[1] = z ? 0.0f : 1.0f;
                    this.mScrimAnimator = ValueAnimator.ofFloat(fArr2);
                }
                this.mScrimAnimationView = view;
                this.mScrimAnimator.setDuration(DeviceHelper.FEATURE_WHOLE_ANIM ? 500 : 0);
                this.mScrimAnimator.addUpdateListener(getScrimAnimatorListener());
                if (!z) {
                    f = 0.0f;
                }
                this.mScrimOpacityAnimatior = f;
                this.mScrimAnimator.start();
            }
        }
    }

    /* access modifiers changed from: private */
    public void setNavigationOffset(float f) {
        LayoutParams layoutParams = (LayoutParams) this.mNavigation.getLayoutParams();
        if (f != layoutParams.offset) {
            layoutParams.offset = f;
            NavigationListener navigationListener = this.mListener;
            if (navigationListener != null) {
                navigationListener.onDrawerSlide(f);
            }
            int i = this.mDrawerMode;
            if (i == 0) {
                this.mContent.setScrollX(0);
            } else if (i == 1) {
                int width = this.mNavigation.getWidth();
                View view = this.mContent;
                if (!this.mLayoutRtl) {
                    width = -width;
                }
                view.setScrollX((int) (((float) width) * layoutParams.offset));
            } else {
                requestLayout();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void closeNavigationDrawer(boolean z) {
        if (this.mFirstMeasure) {
            z = false;
        }
        if (!z) {
            setNavigationOffset(0.0f);
            NavigationListener navigationListener = this.mListener;
            if (navigationListener != null) {
                navigationListener.onDrawerClosed();
            }
        } else if (this.mDrawerEnabled) {
            int width = this.mLayoutRtl ? getWidth() : -this.mNavigation.getWidth();
            ViewDragHelper viewDragHelper = this.mDragger;
            View view = this.mNavigation;
            viewDragHelper.smoothSlideViewTo(view, width, view.getTop());
        } else {
            return;
        }
        invalidate();
        removeCallbacks(this.mPeekRunnable);
    }

    public void computeScroll() {
        super.computeScroll();
        this.mScrimOpacity = this.mDrawerMode == 2 ? 0.0f : getNavigationOffset();
        if (this.mDragger.continueSettling(true)) {
            postInvalidateOnAnimation();
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mDrawerEnabled) {
            drawScrim(canvas);
            drawShadow(canvas);
            return;
        }
        drawDivider(canvas);
        drawNavigationDisableScrim(canvas);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (super.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        if (getDrawerLockMode() != 0 || !this.mDrawerEnabled || keyEvent.getKeyCode() != 4 || keyEvent.getAction() != 1) {
            return false;
        }
        boolean isNavigationDrawerOpen = isNavigationDrawerOpen();
        closeNavigationDrawer(true);
        return isNavigationDrawerOpen;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        View view = this.mScrimAnimationView;
        if (view != null && !view.isEnabled()) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int left = this.mScrimAnimationView.getLeft();
            int right = this.mScrimAnimationView.getRight();
            int top = this.mScrimAnimationView.getTop();
            int bottom = this.mScrimAnimationView.getBottom();
            if (left < x && x < right && top < y && y < bottom) {
                return true;
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public int getDrawerEnabledOrientation() {
        return this.mDrawerEnabledOrientation;
    }

    public int getDrawerLockMode() {
        return this.mLockMode;
    }

    public boolean isNavigationDrawerOpen() {
        return ((LayoutParams) this.mNavigation.getLayoutParams()).offset == 1.0f;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        pullChildren();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001e, code lost:
        if (r1 != 3) goto L_0x0039;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z;
        boolean shouldInterceptTouchEvent = this.mDragger.shouldInterceptTouchEvent(motionEvent);
        if (!this.mDrawerEnabled || this.mLockMode != 0) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        boolean z2 = true;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (this.mDragger.checkTouchSlop(3)) {
                        removeCallbacks(this.mPeekRunnable);
                    }
                }
                z = false;
            }
            removeCallbacks(this.mPeekRunnable);
            closePeekingDrawer();
            this.mChildrenCanceledTouch = false;
            z = false;
        } else {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            this.mInitialMotionX = x;
            this.mInitialMotionY = y;
            z = getNavigationOffset() > 0.0f && this.mDragger.findTopChildUnder((int) x, (int) y) == this.mContent;
            this.mChildrenCanceledTouch = false;
        }
        if (!shouldInterceptTouchEvent && !z && !isDrawerPeeking() && !this.mChildrenCanceledTouch) {
            z2 = false;
        }
        return z2;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7 = i;
        if (this.mDrawerEnabled) {
            int measuredWidth = this.mNavigation.getMeasuredWidth();
            LayoutParams layoutParams = (LayoutParams) this.mNavigation.getLayoutParams();
            float f = (float) (-measuredWidth);
            float f2 = (float) measuredWidth;
            int i8 = (int) ((layoutParams.offset * f2) + f);
            View view = this.mNavigation;
            ViewUtils.layoutChildView(this, view, i8, i2, i8 + measuredWidth, i2 + view.getMeasuredHeight());
            int measuredWidth2 = this.mContent.getMeasuredWidth() + i7;
            int i9 = this.mDrawerMode;
            if (i9 == 1) {
                int i10 = (int) (f * layoutParams.offset);
                View view2 = this.mContent;
                if (this.mLayoutRtl) {
                    i10 = 0 - i10;
                }
                view2.setScrollX(i10);
            } else if (i9 == 0) {
                this.mContent.setScrollX(0);
            } else if (i9 == 2) {
                i5 = i3;
                i6 = (int) (((float) i7) + (f2 * layoutParams.offset));
                View view3 = this.mContent;
                ViewUtils.layoutChildView(this, view3, i6, i2, i5, i2 + view3.getMeasuredHeight());
                return;
            }
            i5 = measuredWidth2;
            i6 = i7;
            View view32 = this.mContent;
            ViewUtils.layoutChildView(this, view32, i6, i2, i5, i2 + view32.getMeasuredHeight());
            return;
        }
        View view4 = this.mNavigation;
        int i11 = i2;
        ViewUtils.layoutChildView(this, view4, i, i11, i7 + view4.getMeasuredWidth(), i2 + this.mNavigation.getMeasuredHeight());
        int measuredWidth3 = this.mNavigation.getMeasuredWidth() + i7 + this.mDividerWidth;
        View view5 = this.mContent;
        ViewUtils.layoutChildView(this, view5, measuredWidth3, i11, measuredWidth3 + view5.getMeasuredWidth(), i2 + this.mContent.getMeasuredHeight());
        this.mContent.setScrollX(0);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        boolean z = false;
        this.mFirstMeasure = false;
        pullChildren();
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        setMeasuredDimension(size, size2);
        boolean z2 = getResources().getConfiguration().orientation == 2;
        WidthDescription widthDescription = z2 ? this.mLandscapeWidthDescription : this.mPortraitWidthDescription;
        int i4 = widthDescription.type;
        int i5 = i4 != 0 ? i4 != 1 ? 0 : (int) (widthDescription.value * ((float) size)) : (int) widthDescription.value;
        measureChild(this.mNavigation, MeasureSpec.makeMeasureSpec(i5, 1073741824), MeasureSpec.makeMeasureSpec(size2, 1073741824));
        if (this.mDrawerMode == 2) {
            LayoutParams layoutParams = (LayoutParams) this.mNavigation.getLayoutParams();
            int measuredWidth = layoutParams.offset > 0.5f ? this.mNavigation.getMeasuredWidth() : 0;
            this.mContent.setAlpha(Math.abs(layoutParams.offset - 0.5f) / 0.5f);
            i3 = measuredWidth;
        } else {
            i3 = 0;
        }
        if (((this.mDrawerEnabledOrientation & 2) == 0 || !z2) && ((this.mDrawerEnabledOrientation & 1) == 0 || z2)) {
            measureChildWithMargins(this.mContent, i, this.mNavigation.getMeasuredWidth() + this.mDividerWidth, i2, 0);
        } else {
            measureChildWithMargins(this.mContent, i, i3, i2, 0);
            z = true;
        }
        if (this.mDrawerEnabled != z) {
            this.mDrawerEnabled = z;
            NavigationListener navigationListener = this.mListener;
            if (navigationListener != null) {
                navigationListener.onDrawerEnableStateChange(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setNavigationOffset(savedState.offset);
        if (savedState.offset >= 0.5f) {
            openNavigationDrawer(false);
        } else {
            closeNavigationDrawer(false);
        }
    }

    public void onRtlPropertiesChanged(int i) {
        int i2 = 1;
        this.mLayoutRtl = i == 1;
        ViewDragHelper viewDragHelper = this.mDragger;
        if (this.mLayoutRtl) {
            i2 = 2;
        }
        viewDragHelper.setEdgeTrackingEnabled(i2);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.offset = getNavigationOffset();
        return savedState;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mDragger.processTouchEvent(motionEvent);
        if (!this.mDrawerEnabled || this.mLockMode != 0) {
            return super.onTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        boolean z = false;
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                float f = x - this.mInitialMotionX;
                float f2 = y - this.mInitialMotionY;
                int touchSlop = this.mDragger.getTouchSlop();
                View findTopChildUnder = this.mDragger.findTopChildUnder((int) x, (int) y);
                if (findTopChildUnder == null || findTopChildUnder != this.mContent || (f * f) + (f2 * f2) >= ((float) (touchSlop * touchSlop)) || !isNavigationDrawerOpen()) {
                    z = true;
                }
                removeCallbacks(this.mPeekRunnable);
                if (z) {
                    closePeekingDrawer();
                } else if (this.mLockMode == 0) {
                    closeNavigationDrawer(true);
                }
            } else if (actionMasked == 3) {
                closePeekingDrawer();
            }
            return true;
        }
        this.mInitialMotionX = x;
        this.mInitialMotionY = y;
        this.mChildrenCanceledTouch = false;
        return true;
    }

    public void openNavigationDrawer(boolean z) {
        if (this.mFirstMeasure) {
            z = false;
        }
        if (!z) {
            setNavigationOffset(1.0f);
            NavigationListener navigationListener = this.mListener;
            if (navigationListener != null) {
                navigationListener.onDrawerOpened();
            }
        } else if (this.mDrawerEnabled) {
            ViewDragHelper viewDragHelper = this.mDragger;
            View view = this.mNavigation;
            viewDragHelper.smoothSlideViewTo(view, 0, view.getTop());
        } else {
            return;
        }
        invalidate();
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (!this.mDragger.isEdgeTouched(this.mLayoutRtl ? 2 : 1)) {
            super.requestDisallowInterceptTouchEvent(z);
        }
        if (z) {
            closePeekingDrawer();
        }
    }

    public void setContentEnabled(boolean z) {
        setChildViewEnabled(this.mContent, z);
    }

    public void setDivider(Drawable drawable) {
        this.mDividerWidth = drawable != null ? drawable.getIntrinsicHeight() : 0;
        this.mDivider = drawable;
        requestLayout();
        invalidate();
    }

    public void setDividerWidth(int i) {
        this.mDividerWidth = i;
        requestLayout();
        invalidate();
    }

    public void setDrawerEnabledOrientation(int i) {
        this.mDrawerEnabledOrientation = i;
        requestLayout();
    }

    public void setDrawerLockMode(int i) {
        if (this.mLockMode != i) {
            this.mLockMode = i;
            if (i != 0) {
                this.mDragger.cancel();
            }
            if (i == 1) {
                closeNavigationDrawer(false);
            } else if (i == 2) {
                openNavigationDrawer(false);
            }
        }
    }

    public void setDrawerMode(int i) {
        this.mDrawerMode = i;
        requestLayout();
    }

    public void setNavigationEanbled(boolean z) {
        setChildViewEnabled(this.mNavigation, z);
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.mListener = navigationListener;
    }

    public void setNavigationShadow(int i) {
        setNavigationShadow(getResources().getDrawable(i));
    }

    public void setNavigationShadow(Drawable drawable) {
        this.mShadow = drawable;
        invalidate();
    }

    public void setScrimColor(int i) {
        this.mScrimColor = i;
    }
}
