package com.miui.internal.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.miui.internal.app.SystemVersionHelper;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.view.menu.ActionMenuView;
import com.miui.internal.view.menu.PhoneActionMenuView;
import miui.R;
import miui.app.ActionBar.FragmentViewPagerChangeListener;
import miui.util.AttributeResolver;
import miui.util.DrawableUtil;

public class ActionBarContainer extends FrameLayout implements FragmentViewPagerChangeListener {
    private static final int BG_EMBEDED_TABS_IDX = 1;
    private static final int BG_LENGTH = 3;
    private static final int BG_NORMAL_IDX = 0;
    private static final int BG_STACKED_IDX = 2;
    private boolean isShowBlurBackgroundView;
    private ActionBarContextView mActionBarContextView;
    private ActionBarView mActionBarView;
    private Drawable mBackground;
    private Drawable[] mBackgroundArray;
    private Drawable mBackgroundBackup;
    private BlurBackgroundView mBlurBackgroundView;
    private Drawable mBrandingBackground;
    private Drawable mBrandingBottomDivider;
    /* access modifiers changed from: private */
    public Animator mCurrentShowAnim;
    private boolean mCustomBackground;
    private boolean mCustomViewAutoFitSystemWindow;
    private boolean mFragmentViewPagerMode;
    private AnimatorListenerAdapter mHideListener;
    private boolean mIsSplit;
    private boolean mIsStacked;
    private boolean mIsTransitioning;
    private Rect mPendingInsets;
    private boolean mRequestAnimation;
    private AnimatorListenerAdapter mShowListener;
    private Drawable mSplitBackground;
    private Drawable mSplitBackgroundBackup;
    private Drawable mStackedBackground;
    private View mTabContainer;
    private int mTabContainerPaddingTop;

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        boolean z = false;
        this.isShowBlurBackgroundView = false;
        this.mCustomBackground = false;
        this.mHideListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ActionBarContainer.this.setVisibility(8);
                ActionBarContainer.this.mCurrentShowAnim = null;
            }
        };
        this.mShowListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ActionBarContainer.this.mCurrentShowAnim = null;
            }
        };
        setBackground(null);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ActionBar);
        this.mBackground = obtainStyledAttributes.getDrawable(R.styleable.ActionBar_android_background);
        this.mBackgroundArray = new Drawable[]{this.mBackground, obtainStyledAttributes.getDrawable(R.styleable.ActionBar_miui_actionBarEmbededTabsBackground), obtainStyledAttributes.getDrawable(R.styleable.ActionBar_miui_actionBarStackedBackground)};
        this.mStackedBackground = obtainStyledAttributes.getDrawable(R.styleable.ActionBar_android_backgroundStacked);
        this.mCustomViewAutoFitSystemWindow = obtainStyledAttributes.getBoolean(R.styleable.ActionBar_customViewAutoFitSystemWindow, false);
        if (getId() == com.miui.internal.R.id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = obtainStyledAttributes.getDrawable(R.styleable.ActionBar_android_backgroundSplit);
        }
        obtainStyledAttributes.recycle();
        this.mBrandingBackground = new ColorDrawable(AttributeResolver.resolveColor(context, R.attr.colorPrimary));
        if (!this.mIsSplit) {
            setPadding(0, 0, 0, 0);
        }
        addActionBarBlurView(context);
        if (!this.mIsSplit ? this.mBackground == null && this.mStackedBackground == null : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
    }

    private void addActionBarBlurView(Context context) {
        this.mBlurBackgroundView = new BlurBackgroundView(context);
        this.mBlurBackgroundView.setLayoutParams(new LayoutParams(-1, -1));
        addView(this.mBlurBackgroundView, 0);
    }

    private void applyInsets(View view) {
        if (view != null && view.getVisibility() == 0) {
            if (view != this.mActionBarView || !this.mCustomViewAutoFitSystemWindow) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
                Rect rect = this.mPendingInsets;
                marginLayoutParams.topMargin = rect != null ? rect.top : 0;
            }
        }
    }

    private void clearBackground() {
        this.mBackgroundBackup = this.mBackground;
        setPrimaryBackground(null);
        if (this.mIsSplit) {
            this.mSplitBackgroundBackup = this.mSplitBackground;
            setSplitBackground(null);
            return;
        }
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setBackground(null);
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            actionBarContextView.updateBackground(true);
        }
    }

    private void onMeasureSplit(int i, int i2) {
        if (MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            i = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824);
        }
        super.onMeasure(i, i2);
        int childCount = getChildCount();
        int i3 = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            if (!(getChildAt(i4) instanceof BlurBackgroundView)) {
                i3 = Math.max(i3, getChildAt(i4).getMeasuredHeight());
            }
        }
        if (i3 == 0) {
            setMeasuredDimension(0, 0);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        if (r0 != null) goto L_0x0015;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void resetBackground() {
        if (this.mIsSplit) {
            Drawable drawable = this.mSplitBackground;
            if (drawable == null) {
                drawable = this.mSplitBackgroundBackup;
                if (drawable == null) {
                    return;
                }
            }
            setSplitBackground(drawable);
            return;
        }
        Drawable drawable2 = this.mBackground;
        if (drawable2 == null) {
            drawable2 = this.mBackgroundBackup;
        }
        setPrimaryBackground(drawable2);
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            actionBarContextView.updateBackground(false);
        }
    }

    private void selectDrawable() {
        if (!this.mCustomBackground && !this.mIsSplit) {
            ActionBarView actionBarView = this.mActionBarView;
            if (actionBarView != null && this.mBackground != null) {
                Drawable[] drawableArr = this.mBackgroundArray;
                if (drawableArr != null && drawableArr.length >= 3) {
                    char c = 0;
                    if (actionBarView.isTightTitleWithEmbeddedTabs()) {
                        c = 1;
                        int displayOptions = this.mActionBarView.getDisplayOptions();
                        if (!((displayOptions & 2) == 0 && (displayOptions & 4) == 0 && (displayOptions & 16) == 0)) {
                            c = 2;
                        }
                    }
                    Drawable[] drawableArr2 = this.mBackgroundArray;
                    if (drawableArr2[c] != null) {
                        this.mBackground = drawableArr2[c];
                    }
                }
            }
        }
    }

    private void setAllClipChildren(ViewGroup viewGroup, boolean z) {
        viewGroup.setClipChildren(z);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                setAllClipChildren((ViewGroup) childAt, z);
            }
        }
    }

    private void setAllClipToPadding(ViewGroup viewGroup, boolean z) {
        viewGroup.setClipToPadding(z);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                setAllClipToPadding((ViewGroup) childAt, z);
            }
        }
    }

    private void updateAllClipView(boolean z) {
        if (getParent() != null && (getParent() instanceof ViewGroup)) {
            ViewGroup viewGroup = (ViewGroup) getParent();
            if (viewGroup instanceof ActionBarOverlayLayout) {
                ViewGroup viewGroup2 = (ViewGroup) viewGroup.findViewById(16908290);
                if (viewGroup2 != null) {
                    setAllClipChildren(viewGroup2, z);
                    setAllClipToPadding(viewGroup2, z);
                }
            }
        }
    }

    private void updateBackground(boolean z) {
        if (z) {
            clearBackground();
        } else {
            resetBackground();
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mRequestAnimation) {
            post(new Runnable() {
                public void run() {
                    ActionBarContainer.this.show(true);
                }
            });
            this.mRequestAnimation = false;
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mBackground;
        if (drawable != null && drawable.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null && drawable2.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null && drawable3.isStateful()) {
            this.mSplitBackground.setState(getDrawableState());
        }
    }

    /* access modifiers changed from: 0000 */
    public int getCollapsedHeight() {
        if (!this.mIsSplit) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if (getChildAt(i2) instanceof ActionMenuView) {
                ActionMenuView actionMenuView = (ActionMenuView) getChildAt(i2);
                if (actionMenuView.getVisibility() == 0 && actionMenuView.getCollapsedHeight() > 0) {
                    i = Math.max(i, actionMenuView.getCollapsedHeight());
                }
            }
        }
        return i;
    }

    /* access modifiers changed from: 0000 */
    public int getInsetHeight() {
        int collapsedHeight = getCollapsedHeight();
        if (!this.mFragmentViewPagerMode) {
            return collapsedHeight;
        }
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if (getChildAt(i2) instanceof ActionMenuView) {
                ActionMenuView actionMenuView = (ActionMenuView) getChildAt(i2);
                if (actionMenuView.getVisibility() == 0 && actionMenuView.getCollapsedHeight() > 0) {
                    i++;
                }
            }
        }
        if (i == 1 && this.mActionBarContextView.getAnimatedVisibility() == 0) {
            return collapsedHeight;
        }
        return 0;
    }

    public Rect getPendingInsets() {
        return this.mPendingInsets;
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    public void hide(boolean z) {
        Animator animator = this.mCurrentShowAnim;
        if (animator != null) {
            animator.cancel();
        }
        if (!z || !this.mIsSplit) {
            setVisibility(8);
            return;
        }
        this.mCurrentShowAnim = ObjectAnimator.ofFloat(this, "TranslationY", new float[]{0.0f, (float) getHeight()});
        this.mCurrentShowAnim.setDuration(DeviceHelper.FEATURE_WHOLE_ANIM ? (long) getContext().getResources().getInteger(17694720) : 0);
        this.mCurrentShowAnim.addListener(this.mHideListener);
        this.mCurrentShowAnim.start();
    }

    public boolean isBlurEnable() {
        return this.isShowBlurBackgroundView;
    }

    public boolean isSearchStubSupportBlur() {
        if (this.mIsSplit) {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof PhoneActionMenuView) {
                    return ((PhoneActionMenuView) getChildAt(i)).isSearchStubSupportBlur();
                }
            }
        } else if (!this.mBlurBackgroundView.isSupportBlur()) {
        }
        return false;
    }

    public void onDraw(Canvas canvas) {
        Drawable drawable;
        if (getWidth() != 0 && getHeight() != 0) {
            if (!this.mIsSplit) {
                Drawable drawable2 = this.mBackground;
                if (drawable2 != null) {
                    drawable2.draw(canvas);
                }
                if (this.mBrandingBackground != null && DrawableUtil.isPlaceholder(this.mBackground)) {
                    this.mBrandingBackground.draw(canvas);
                    if (this.mBrandingBottomDivider != null && !DrawableUtil.isPlaceholder(this.mBrandingBackground)) {
                        drawable = this.mBrandingBottomDivider;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else if (DeviceHelper.IS_TABLET) {
                drawable = this.mSplitBackground;
                if (drawable == null) {
                    return;
                }
            } else {
                return;
            }
            drawable.draw(canvas);
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = (ActionBarView) findViewById(com.miui.internal.R.id.action_bar);
        this.mActionBarContextView = (ActionBarContextView) findViewById(com.miui.internal.R.id.action_context_bar);
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        return !this.mIsSplit;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(motionEvent);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean z2;
        View view;
        int paddingLeft;
        int i5;
        super.onLayout(z, i, i2, i3, i4);
        int measuredHeight = getMeasuredHeight();
        View view2 = this.mTabContainer;
        if (!(view2 == null || view2.getVisibility() == 8)) {
            int measuredHeight2 = this.mTabContainer.getMeasuredHeight();
            ActionBarView actionBarView = this.mActionBarView;
            if (actionBarView == null || actionBarView.getVisibility() != 0 || this.mActionBarView.getMeasuredHeight() <= 0) {
                Rect rect = this.mPendingInsets;
                measuredHeight2 += rect != null ? rect.top : 0;
                view = this.mTabContainer;
                paddingLeft = view.getPaddingLeft();
                Rect rect2 = this.mPendingInsets;
                if (rect2 != null) {
                    i5 = rect2.top + this.mTabContainerPaddingTop;
                    view.setPadding(paddingLeft, i5, this.mTabContainer.getPaddingRight(), this.mTabContainer.getPaddingBottom());
                    this.mTabContainer.layout(i, measuredHeight - measuredHeight2, i3, measuredHeight);
                }
            } else {
                view = this.mTabContainer;
                paddingLeft = view.getPaddingLeft();
            }
            i5 = this.mTabContainerPaddingTop;
            view.setPadding(paddingLeft, i5, this.mTabContainer.getPaddingRight(), this.mTabContainer.getPaddingBottom());
            this.mTabContainer.layout(i, measuredHeight - measuredHeight2, i3, measuredHeight);
        }
        if (this.mIsSplit) {
            Drawable drawable = this.mSplitBackground;
            if (drawable != null) {
                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                z2 = true;
            } else {
                z2 = false;
            }
        } else {
            selectDrawable();
            Drawable drawable2 = this.mBackground;
            if (drawable2 != null) {
                drawable2.setBounds(0, 0, i3 - i, measuredHeight);
                z2 = true;
            } else {
                z2 = false;
            }
            Drawable drawable3 = this.mBrandingBackground;
            if (drawable3 != null) {
                drawable3.setBounds(0, 0, i3 - i, measuredHeight);
                z2 = true;
            }
            if (!SystemVersionHelper.isMiui11()) {
                if (this.mBrandingBottomDivider == null) {
                    this.mBrandingBottomDivider = AttributeResolver.resolveDrawable(getContext(), R.attr.colorDividerLine);
                }
                this.mBrandingBottomDivider.setBounds(0, measuredHeight - 1, i3 - i, measuredHeight);
            }
        }
        if (z2) {
            invalidate();
        }
    }

    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        if (this.mIsSplit) {
            onMeasureSplit(i, i2);
            return;
        }
        View view = this.mTabContainer;
        if (view != null) {
            view.setPadding(view.getPaddingLeft(), this.mTabContainerPaddingTop, this.mTabContainer.getPaddingRight(), this.mTabContainer.getPaddingBottom());
        }
        applyInsets(this.mActionBarView);
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            Rect rect = this.mPendingInsets;
            actionBarContextView.setContentInset(rect != null ? rect.top : 0);
        }
        super.onMeasure(i, i2);
        ActionBarView actionBarView = this.mActionBarView;
        boolean z = (actionBarView == null || actionBarView.getVisibility() == 8 || this.mActionBarView.getMeasuredHeight() <= 0) ? false : true;
        if (z) {
            LayoutParams layoutParams = (LayoutParams) this.mActionBarView.getLayoutParams();
            i3 = this.mActionBarView.isCollapsed() ? layoutParams.topMargin : this.mActionBarView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
        } else {
            i3 = 0;
        }
        View view2 = this.mTabContainer;
        if (!(view2 == null || view2.getVisibility() == 8 || MeasureSpec.getMode(i2) != Integer.MIN_VALUE)) {
            int size = MeasureSpec.getSize(i2);
            int measuredWidth = getMeasuredWidth();
            int min = Math.min(i3 + this.mTabContainer.getMeasuredHeight(), size);
            if (!z) {
                Rect rect2 = this.mPendingInsets;
                if (rect2 != null) {
                    i4 = rect2.top;
                    setMeasuredDimension(measuredWidth, min + i4);
                }
            }
            i4 = 0;
            setMeasuredDimension(measuredWidth, min + i4);
        }
        int i5 = 0;
        for (int i6 = 0; i6 < getChildCount(); i6++) {
            View childAt = getChildAt(i6);
            if (childAt != this.mBlurBackgroundView && childAt.getVisibility() == 0 && childAt.getMeasuredHeight() > 0 && childAt.getMeasuredWidth() > 0) {
                i5++;
            }
        }
        if (i5 == 0) {
            setMeasuredDimension(0, 0);
        }
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3, int[] iArr2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onNestedPreScroll(view, i, i2, new int[]{0, 0}, i3, new int[]{0, 0});
        }
        this.mActionBarView.onNestedPreScroll(view, i, i2, iArr, i3, iArr2);
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr, int[] iArr2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onNestedScroll(view, i, i2, i3, i4, i5, new int[]{0, 0}, new int[]{0, 0});
        }
        this.mActionBarView.onNestedScroll(view, i, i2, i3, i4, i5, iArr, iArr2);
    }

    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onNestedScrollAccepted(view, view2, i, i2);
        }
        this.mActionBarView.onNestedScrollAccepted(view, view2, i, i2);
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, boolean z, boolean z2) {
        if (this.mIsSplit) {
            ActionMenuView actionMenuView = (ActionMenuView) getChildAt(1);
            if (actionMenuView != null) {
                actionMenuView.onPageScrolled(i, f, z, z2);
            }
        }
    }

    public void onPageSelected(int i) {
    }

    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onStartNestedScroll(view, view2, i, i2);
        }
        return this.mActionBarView.onStartNestedScroll(view, view2, i, i2);
    }

    public void onStopNestedScroll(View view, int i) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onStopNestedScroll(view, i);
        }
        this.mActionBarView.onStopNestedScroll(view, i);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return !this.mIsSplit && super.onTouchEvent(motionEvent);
    }

    public void onWindowHide() {
        if (this.mActionBarView.getMenuView() != null) {
            this.mActionBarView.getMenuView().startLayoutAnimation();
        }
    }

    public void onWindowShow() {
        if (this.mActionBarView.getMenuView() != null) {
            this.mActionBarView.getMenuView().startLayoutAnimation();
        }
    }

    public void setActionBarContextView(ActionBarContextView actionBarContextView) {
        this.mActionBarContextView = actionBarContextView;
    }

    public void setAlpha(float f) {
        if (!this.isShowBlurBackgroundView) {
            super.setAlpha(f);
        } else if (this.mIsSplit) {
            for (int i = 0; i < getChildCount(); i++) {
                if (!(getChildAt(i) instanceof BlurBackgroundView)) {
                    if (getChildAt(i) instanceof PhoneActionMenuView) {
                        ((PhoneActionMenuView) getChildAt(i)).setAlpha(f);
                    } else {
                        getChildAt(i).setAlpha(f);
                    }
                }
            }
        }
    }

    public void setBackground(Drawable drawable) {
        super.setBackground(drawable);
    }

    public boolean setBlurBackground(boolean z) {
        boolean z2;
        if (this.isShowBlurBackgroundView == z) {
            return true;
        }
        if (this.mIsSplit) {
            this.isShowBlurBackgroundView = z;
            this.mBlurBackgroundView.setBlurBackground(false);
            updateBackground(z);
            z2 = false;
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof PhoneActionMenuView) {
                    PhoneActionMenuView phoneActionMenuView = (PhoneActionMenuView) getChildAt(i);
                    boolean blurBackground = phoneActionMenuView.setBlurBackground(z);
                    if (blurBackground) {
                        phoneActionMenuView.updateBackground(z);
                    }
                    z2 = blurBackground;
                }
            }
        } else {
            z2 = this.mBlurBackgroundView.setBlurBackground(z);
            if (z2) {
                updateAllClipView(!z);
                this.isShowBlurBackgroundView = z;
                updateBackground(z);
            }
        }
        return z2;
    }

    public void setFragmentViewPagerMode(boolean z) {
        this.mFragmentViewPagerMode = z;
    }

    public void setPendingInsets(Rect rect) {
        if (!this.mIsSplit) {
            if (this.mPendingInsets == null) {
                this.mPendingInsets = new Rect();
            }
            this.mPendingInsets.set(rect);
        }
    }

    public void setPrimaryBackground(Drawable drawable) {
        Rect rect;
        Drawable drawable2 = this.mBackground;
        if (drawable2 != null) {
            rect = drawable2.getBounds();
            this.mBackground.setCallback(null);
            unscheduleDrawable(this.mBackground);
        } else {
            rect = null;
        }
        this.mBackground = drawable;
        boolean z = true;
        if (drawable != null) {
            drawable.setCallback(this);
            if (rect == null) {
                requestLayout();
            } else {
                this.mBackground.setBounds(rect);
            }
            this.mCustomBackground = true;
        } else {
            this.mCustomBackground = false;
        }
        if (!this.mIsSplit ? !(this.mBackground == null && this.mStackedBackground == null) : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setSplitBackground(Drawable drawable) {
        Drawable drawable2 = this.mSplitBackground;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        boolean z = true;
        if (!this.mIsSplit ? !(this.mBackground == null && this.mStackedBackground == null) : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setStackedBackground(Drawable drawable) {
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        boolean z = true;
        if (!this.mIsSplit ? !(this.mBackground == null && this.mStackedBackground == null) : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
        View view = this.mTabContainer;
        if (view != null) {
            view.setBackground(this.mStackedBackground);
        }
    }

    public void setTabContainer(ScrollingTabContainerView scrollingTabContainerView) {
        View view = this.mTabContainer;
        if (view != null) {
            removeView(view);
        }
        if (scrollingTabContainerView != null) {
            addView(scrollingTabContainerView);
            ViewGroup.LayoutParams layoutParams = scrollingTabContainerView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            scrollingTabContainerView.setAllowCollapse(false);
            this.mTabContainerPaddingTop = scrollingTabContainerView.getPaddingTop();
        } else {
            View view2 = this.mTabContainer;
            if (view2 != null) {
                view2.setBackground(null);
            }
        }
        this.mTabContainer = scrollingTabContainerView;
    }

    public void setTransitioning(boolean z) {
        this.mIsTransitioning = z;
        setDescendantFocusability(z ? 393216 : 262144);
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        boolean z = i == 0;
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setVisible(z, false);
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.setVisible(z, false);
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null) {
            drawable3.setVisible(z, false);
        }
    }

    public void show(boolean z) {
        Animator animator = this.mCurrentShowAnim;
        if (animator != null) {
            animator.cancel();
        }
        setVisibility(0);
        if (!z) {
            setTranslationY(0.0f);
        } else if (this.mIsSplit) {
            this.mCurrentShowAnim = ObjectAnimator.ofFloat(this, "TranslationY", new float[]{(float) getHeight(), 0.0f});
            this.mCurrentShowAnim.setDuration(DeviceHelper.FEATURE_WHOLE_ANIM ? (long) getContext().getResources().getInteger(17694720) : 0);
            this.mCurrentShowAnim.addListener(this.mShowListener);
            this.mCurrentShowAnim.start();
            ActionMenuView actionMenuView = (ActionMenuView) getChildAt(1);
            if (actionMenuView != null) {
                actionMenuView.startLayoutAnimation();
            }
        }
    }

    public ActionMode startActionModeForChild(View view, Callback callback) {
        return null;
    }

    public void updateAllClipView() {
        boolean z = this.isShowBlurBackgroundView;
        if (z) {
            updateAllClipView(!z);
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return (drawable == this.mBackground && !this.mIsSplit) || (drawable == this.mStackedBackground && this.mIsStacked) || ((drawable == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(drawable));
    }
}
