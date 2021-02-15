package com.miui.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.miui.internal.R;
import com.miui.internal.view.menu.ActionMenuPresenter;
import com.miui.internal.view.menu.ActionMenuView;
import java.util.Collection;
import miui.animation.Folme;
import miui.animation.IStateStyle;
import miui.animation.base.AnimConfig;
import miui.animation.controller.AnimState;
import miui.animation.listener.TransitionListener;
import miui.animation.property.ViewProperty;
import miui.app.ActionBarTransitionListener;
import miui.util.ViewUtils;

abstract class AbsActionBarView extends ViewGroup {
    protected static final int COLLAPSE_LAYOUT_MAX_TRANSY = 20;
    static final int INNER_STATE_COLLAPSE = 0;
    static final int INNER_STATE_EXPAND = 1;
    static final int INNER_STATE_RESIZING = 2;
    protected ActionMenuPresenter mActionMenuPresenter;
    protected TransitionListener mAnimConfigListener;
    protected AnimConfig mCollapseAnimHideConfig;
    protected AnimConfig mCollapseAnimShowConfig;
    protected int mContentHeight;
    private int mExpandState;
    protected AnimConfig mHideProcessConfig;
    int mInnerExpandState;
    float mLastProcess;
    protected ActionMenuView mMenuView;
    protected AnimConfig mMovableAnimConfig;
    private boolean mResizable;
    protected AnimConfig mShowProcessConfig;
    protected boolean mSplitActionBar;
    protected ActionBarContainer mSplitView;
    protected boolean mSplitWhenNarrow;
    ActionBarTransitionListener mTransitionListener;

    public class CollapseView {
        private boolean isAcceptAlphaChange = true;
        private float mAlpha;
        private IStateStyle mTouchAnim;
        private View mView;

        protected CollapseView() {
        }

        public void animTo(float f, int i, int i2, AnimConfig animConfig) {
            cancelAnim();
            AnimState animState = new AnimState("to");
            ViewProperty viewProperty = ViewProperty.ALPHA;
            if (!this.isAcceptAlphaChange) {
                f = this.mAlpha;
            }
            AnimState add = animState.add(viewProperty, f, new long[0]).add(ViewProperty.TRANSLATION_X, i, new long[0]).add(ViewProperty.TRANSLATION_Y, i2, new long[0]);
            this.mTouchAnim.to(add, animConfig);
        }

        public void attach(View view) {
            this.mView = view;
            this.mTouchAnim = Folme.useAt(this.mView).state();
        }

        public void cancelAnim() {
            this.mTouchAnim.cancel();
        }

        public void setAcceptAlphaChange(boolean z) {
            this.isAcceptAlphaChange = z;
        }

        public void setAlpha(float f) {
            this.mAlpha = f;
            View view = this.mView;
            if (view != null) {
                view.setAlpha(f);
            }
        }

        public void setAnimFrom(float f, int i, int i2) {
            AnimState animState = new AnimState("from");
            ViewProperty viewProperty = ViewProperty.ALPHA;
            if (!this.isAcceptAlphaChange) {
                f = this.mAlpha;
            }
            this.mTouchAnim.setTo((Object) animState.add(viewProperty, f, new long[0]).add(ViewProperty.TRANSLATION_X, i, new long[0]).add(ViewProperty.TRANSLATION_Y, i2, new long[0]));
        }

        public void setTransparent(int i, int i2) {
            View view = this.mView;
            if (view != null) {
                view.setTranslationX((float) i);
                this.mView.setTranslationY((float) i2);
            }
        }

        public void setVisibility(int i) {
            View view = this.mView;
            if (view != null) {
                view.setVisibility(i);
            }
        }
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
        int expandState;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.expandState = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.expandState);
        }
    }

    AbsActionBarView(Context context) {
        this(context, null);
    }

    AbsActionBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    AbsActionBarView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mInnerExpandState = 1;
        this.mExpandState = 1;
        this.mResizable = true;
        this.mLastProcess = 0.0f;
        this.mAnimConfigListener = new TransitionListener() {
            public void onBegin(Object obj) {
                super.onBegin(obj);
                ActionBarTransitionListener actionBarTransitionListener = AbsActionBarView.this.mTransitionListener;
                if (actionBarTransitionListener != null) {
                    actionBarTransitionListener.onTransitionBegin(obj);
                }
            }

            public void onComplete(Object obj) {
                super.onComplete(obj);
                ActionBarTransitionListener actionBarTransitionListener = AbsActionBarView.this.mTransitionListener;
                if (actionBarTransitionListener != null) {
                    actionBarTransitionListener.onTransitionComplete(obj);
                }
            }

            public void onUpdate(Object obj, Collection collection) {
                super.onUpdate(obj, collection);
                ActionBarTransitionListener actionBarTransitionListener = AbsActionBarView.this.mTransitionListener;
                if (actionBarTransitionListener != null) {
                    actionBarTransitionListener.onTransitionUpdate(obj, collection);
                }
            }
        };
        this.mCollapseAnimShowConfig = new AnimConfig().setEase(-2, 1.0f, 0.3f);
        this.mShowProcessConfig = new AnimConfig().setEase(-2, 1.0f, 0.3f).addListeners(this.mAnimConfigListener);
        this.mCollapseAnimHideConfig = new AnimConfig().setEase(-2, 1.0f, 0.15f);
        this.mHideProcessConfig = new AnimConfig().setEase(-2, 1.0f, 0.15f).addListeners(this.mAnimConfigListener);
        this.mMovableAnimConfig = new AnimConfig().setEase(-2, 1.0f, 0.6f);
        if (getContext().getResources().getConfiguration().orientation == 2) {
            this.mInnerExpandState = 0;
            this.mExpandState = 0;
            return;
        }
        this.mInnerExpandState = 1;
        this.mExpandState = 1;
    }

    public void animateToVisibility(int i) {
        clearAnimation();
        if (i != getVisibility()) {
            Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), i == 0 ? R.anim.action_bar_fade_in : R.anim.action_bar_fade_out);
            startAnimation(loadAnimation);
            setVisibility(i);
            if (this.mSplitView != null) {
                ActionMenuView actionMenuView = this.mMenuView;
                if (actionMenuView != null) {
                    actionMenuView.startAnimation(loadAnimation);
                    this.mMenuView.setVisibility(i);
                }
            }
        }
    }

    public void dismissPopupMenus() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus(false);
        }
    }

    /* access modifiers changed from: 0000 */
    public int getActionBarStyle() {
        return 16843470;
    }

    public ActionBarTransitionListener getActionBarTransitionListener() {
        return this.mTransitionListener;
    }

    public ActionMenuView getActionMenuView() {
        return this.mMenuView;
    }

    public int getAnimatedVisibility() {
        return getVisibility();
    }

    public int getContentHeight() {
        return this.mContentHeight;
    }

    public int getExpandState() {
        return this.mExpandState;
    }

    public ActionMenuView getMenuView() {
        return this.mMenuView;
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu(false);
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    public boolean isOverflowReserved() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowReserved();
    }

    public boolean isResizable() {
        return this.mResizable;
    }

    /* access modifiers changed from: protected */
    public int measureChildView(View view, int i, int i2, int i3) {
        view.measure(MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE), i2);
        return Math.max(0, (i - view.getMeasuredWidth()) - i3);
    }

    /* access modifiers changed from: protected */
    public void onAnimatedExpandStateChanged(int i, int i2) {
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        if (VERSION.SDK_INT >= 8) {
            super.onConfigurationChanged(configuration);
        }
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(null, miui.R.styleable.ActionBar, getActionBarStyle(), 0);
        setContentHeight(obtainStyledAttributes.getLayoutDimension(miui.R.styleable.ActionBar_android_height, 0));
        obtainStyledAttributes.recycle();
        if (this.mSplitWhenNarrow) {
            setSplitActionBar(getContext().getResources().getBoolean(R.bool.abc_split_action_bar_is_narrow));
        }
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.onConfigurationChanged(configuration);
        }
        if (getContext().getResources().getConfiguration().orientation == 2) {
            setExpandState(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onExpandStateChanged(int i, int i2) {
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3, int[] iArr2) {
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr, int[] iArr2) {
    }

    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setExpandState(savedState.expandState);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int i = this.mInnerExpandState;
        if (i == 2) {
            i = 0;
        }
        savedState.expandState = i;
        return savedState;
    }

    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        return false;
    }

    public void onStopNestedScroll(View view, int i) {
    }

    /* access modifiers changed from: protected */
    public int positionChild(View view, int i, int i2, int i3) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i4 = i2 + ((i3 - measuredHeight) / 2);
        ViewUtils.layoutChildView(this, view, i, i4, i + measuredWidth, i4 + measuredHeight);
        return measuredWidth;
    }

    /* access modifiers changed from: protected */
    public int positionChildInverse(View view, int i, int i2, int i3) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i4 = i2 + ((i3 - measuredHeight) / 2);
        ViewUtils.layoutChildView(this, view, i - measuredWidth, i4, i, i4 + measuredHeight);
        return measuredWidth;
    }

    public void postShowOverflowMenu() {
        post(new Runnable() {
            public void run() {
                AbsActionBarView.this.showOverflowMenu();
            }
        });
    }

    public void setActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener) {
        this.mTransitionListener = actionBarTransitionListener;
    }

    public void setContentHeight(int i) {
        this.mContentHeight = i;
        requestLayout();
    }

    public void setExpandState(int i) {
        setExpandState(i, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002b, code lost:
        if (r3 == 1) goto L_0x0027;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setExpandState(int i, boolean z) {
        if ((getContext().getResources().getConfiguration().orientation != 2 || i == 0) && this.mResizable) {
            int i2 = this.mInnerExpandState;
            if (i2 != i) {
                if (z) {
                    onAnimatedExpandStateChanged(i2, i);
                } else {
                    this.mInnerExpandState = i;
                    int i3 = i == 0 ? 0 : 1;
                    this.mExpandState = i3;
                    onExpandStateChanged(i2, i);
                    requestLayout();
                }
            }
        }
    }

    public void setResizable(boolean z) {
        this.mResizable = z;
    }

    public void setSplitActionBar(boolean z) {
        this.mSplitActionBar = z;
    }

    public void setSplitView(ActionBarContainer actionBarContainer) {
        this.mSplitView = actionBarContainer;
    }

    public void setSplitWhenNarrow(boolean z) {
        this.mSplitWhenNarrow = z;
    }

    public void setVisibility(int i) {
        if (i != getVisibility()) {
            super.setVisibility(i);
        }
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
    }
}
