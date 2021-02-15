package com.miui.internal.view.menu;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.view.menu.MenuBuilder.ItemInvoker;

public abstract class ActionMenuView extends LinearLayout implements ItemInvoker, MenuView {
    private MenuBuilder mMenu;
    private OpenCloseAnimator mOpenCloseAnimator;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    public interface ActionMenuChildView {
        boolean needsDividerAfter();

        boolean needsDividerBefore();
    }

    public class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        @ExportedProperty
        public int cellsUsed;
        @ExportedProperty
        public boolean expandable;
        public boolean expanded;
        @ExportedProperty
        public int extraPixels;
        @ExportedProperty
        public boolean isOverflowButton;
        @ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.isOverflowButton = false;
        }

        public LayoutParams(int i, int i2, boolean z) {
            super(i, i2);
            this.isOverflowButton = z;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }
    }

    class OpenCloseAnimator implements AnimatorListener {
        Animator mCloseAnimator;
        Animator mCurrentAnimator;
        boolean mIsOpen;

        public OpenCloseAnimator() {
        }

        /* access modifiers changed from: 0000 */
        public void cancel() {
            initialize();
            Animator animator = this.mCurrentAnimator;
            if (animator != null) {
                animator.cancel();
                this.mCurrentAnimator = null;
            }
        }

        /* access modifiers changed from: 0000 */
        public void close() {
            cancel();
            this.mIsOpen = false;
            this.mCloseAnimator.start();
        }

        /* access modifiers changed from: 0000 */
        public void initialize() {
            if (this.mCloseAnimator == null) {
                this.mCloseAnimator = ObjectAnimator.ofFloat(this, "TranslationY", new float[]{(float) ActionMenuView.this.getHeight()});
                this.mCloseAnimator.setDuration((long) ActionMenuView.this.getResources().getInteger(17694720));
                this.mCloseAnimator.addListener(this);
            }
        }

        public void onAnimationCancel(Animator animator) {
        }

        public void onAnimationEnd(Animator animator) {
            this.mCurrentAnimator = null;
        }

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
            this.mCurrentAnimator = animator;
        }

        /* access modifiers changed from: 0000 */
        public void open() {
            cancel();
            this.mIsOpen = true;
            setTranslationY(0.0f);
            ActionMenuView.this.startLayoutAnimation();
        }

        public void setTranslationY(float f) {
            for (int i = 0; i < ActionMenuView.this.getChildCount(); i++) {
                ActionMenuView.this.getChildAt(i).setTranslationY(f);
            }
        }
    }

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBaselineAligned(false);
        this.mOpenCloseAnimator = new OpenCloseAnimator();
        setLayoutAnimation(null);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams != null && (layoutParams instanceof LayoutParams);
    }

    /* access modifiers changed from: protected */
    public float computeAlpha(float f, boolean z, boolean z2) {
        int i;
        if (z && z2) {
            return 1.0f;
        }
        if (z) {
            i = (int) ((1.0f - f) * 10.0f);
        } else if (!z2) {
            return 1.0f;
        } else {
            i = (int) (f * 10.0f);
        }
        return ((float) i) / 10.0f;
    }

    /* access modifiers changed from: protected */
    public float computeTranslationY(float f, boolean z, boolean z2) {
        float collapsedHeight = (float) getCollapsedHeight();
        if (z && z2) {
            f = ((double) f) < 0.5d ? f * 2.0f : (1.0f - f) * 2.0f;
        } else if (z2) {
            f = 1.0f - f;
        }
        return f * collapsedHeight;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    public boolean filterLeftoverView(int i) {
        View childAt = getChildAt(i);
        childAt.clearAnimation();
        removeView(childAt);
        return true;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : generateDefaultLayoutParams();
    }

    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
        generateDefaultLayoutParams.isOverflowButton = true;
        return generateDefaultLayoutParams;
    }

    public abstract int getCollapsedHeight();

    public ActionMenuPresenter getPresenter() {
        return this.mPresenter;
    }

    public int getWindowAnimations() {
        return 0;
    }

    public boolean hasBackgroundView() {
        return false;
    }

    public boolean hasBlurBackgroundView() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int i) {
        View childAt = getChildAt(i - 1);
        View childAt2 = getChildAt(i);
        boolean z = false;
        if (i < getChildCount() && (childAt instanceof ActionMenuChildView)) {
            z = false | ((ActionMenuChildView) childAt).needsDividerAfter();
        }
        return (i <= 0 || !(childAt2 instanceof ActionMenuChildView)) ? z : z | ((ActionMenuChildView) childAt2).needsDividerBefore();
    }

    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    public boolean invokeItem(MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction(menuItemImpl, 0);
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (VERSION.SDK_INT >= 8) {
            super.onConfigurationChanged(configuration);
        }
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu(false);
                this.mPresenter.showOverflowMenu();
            }
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPresenter.dismissPopupMenus(false);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        } else {
            super.onMeasure(i, i2);
        }
    }

    public void onPageScrolled(int i, float f, boolean z, boolean z2) {
        if (DeviceHelper.FEATURE_WHOLE_ANIM) {
            setAlpha(computeAlpha(f, z, z2));
        }
        float computeTranslationY = computeTranslationY(f, z, z2);
        if (!z || !z2 || getTranslationY() != 0.0f) {
            setTranslationY(computeTranslationY);
        }
        this.mOpenCloseAnimator.setTranslationY(computeTranslationY);
    }

    public void playCloseAnimator() {
        this.mOpenCloseAnimator.close();
    }

    public void playOpenAnimator() {
        this.mOpenCloseAnimator.open();
    }

    public void setOverflowReserved(boolean z) {
        this.mReserveOverflow = z;
    }

    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.mPresenter = actionMenuPresenter;
    }
}
