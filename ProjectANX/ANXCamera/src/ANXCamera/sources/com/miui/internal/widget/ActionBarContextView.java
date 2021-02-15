package com.miui.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.util.FolmeAnimHelper;
import com.miui.internal.view.EditActionModeImpl;
import com.miui.internal.view.menu.ActionMenuItem;
import com.miui.internal.view.menu.ActionMenuPresenter;
import com.miui.internal.view.menu.ActionMenuView;
import com.miui.internal.view.menu.MenuBuilder;
import com.miui.internal.widget.AbsActionBarView.CollapseView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import miui.animation.physics.DynamicAnimation;
import miui.animation.physics.DynamicAnimation.OnAnimationEndListener;
import miui.animation.physics.SpringAnimation;
import miui.animation.physics.SpringAnimationSet;
import miui.animation.property.FloatProperty;
import miui.animation.property.ViewProperty;
import miui.app.ActionBarTransitionListener;
import miui.util.HapticFeedbackUtil;
import miui.util.ViewUtils;
import miui.view.ActionModeAnimationListener;
import miui.view.EditActionMode;

public class ActionBarContextView extends AbsActionBarView implements ActionModeView {
    private static final int ANIMATE_IDLE = 0;
    private static final int ANIMATE_IN = 1;
    private static final int ANIMATE_OUT = 2;
    private static final float DAMPING = 0.9f;
    private static final int DELAY_DURATION_100 = 100;
    private static final int DELAY_DURATION_50 = 50;
    private static final float STIFFNESS_HIGH = 986.96f;
    private static final float STIFFNESS_LOW = 322.27f;
    private static final float STIFFNESS_MEDIUM = 438.65f;
    private static final int TYPE_NON_TOUCH = 1;
    private static final int TYPE_TOUCH = 0;
    /* access modifiers changed from: private */
    public WeakReference mActionMode;
    private Drawable mActionModeBackground;
    /* access modifiers changed from: private */
    public boolean mAnimateStart;
    private boolean mAnimateToVisible;
    private List mAnimationListeners;
    /* access modifiers changed from: private */
    public int mAnimationMode;
    private float mAnimationProgress;
    private Button mButton1;
    /* access modifiers changed from: private */
    public ActionMenuItem mButton1MenuItem;
    private Button mButton2;
    /* access modifiers changed from: private */
    public ActionMenuItem mButton2MenuItem;
    private View mCollapseContainer;
    private CollapseView mCollapseController;
    /* access modifiers changed from: private */
    public int mCollapseHeight;
    private int mContentInset;
    private int mExpandTitleStyleRes;
    private TextView mExpandTitleView;
    /* access modifiers changed from: private */
    public FrameLayout mMovableContainer;
    private CollapseView mMovableController;
    private boolean mNonTouchScrolling;
    private OnClickListener mOnMenuItemClickListener;
    /* access modifiers changed from: private */
    public int mPendingHeight;
    private Runnable mPostScroll;
    /* access modifiers changed from: private */
    public Scroller mPostScroller;
    private boolean mRequestAnimation;
    private Drawable mSplitBackground;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private boolean mTitleOptional;
    private int mTitleStyleRes;
    private TextView mTitleView;
    private boolean mTouchScrolling;
    /* access modifiers changed from: private */
    public SpringAnimationSet mVisibilityAnim;

    public class DOnAnimationEndListener implements OnAnimationEndListener {
        boolean mFinalVisibility;

        public DOnAnimationEndListener(boolean z) {
            this.mFinalVisibility = z;
        }

        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            int i = 0;
            ActionBarContextView.this.setSplitAnimating(false);
            ActionBarContextView.this.mAnimateStart = false;
            ActionBarContextView.this.notifyAnimationEnd(this.mFinalVisibility);
            if (ActionBarContextView.this.mAnimationMode == 2) {
                ActionBarContextView.this.killMode();
            }
            ActionBarContextView.this.mAnimationMode = 0;
            ActionBarContextView.this.mVisibilityAnim = null;
            ActionBarContextView.this.setVisibility(this.mFinalVisibility ? 0 : 8);
            ActionBarContextView actionBarContextView = ActionBarContextView.this;
            if (actionBarContextView.mSplitView != null) {
                ActionMenuView actionMenuView = actionBarContextView.mMenuView;
                if (actionMenuView != null) {
                    if (!this.mFinalVisibility) {
                        i = 8;
                    }
                    actionMenuView.setVisibility(i);
                }
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
        public CharSequence defaultButtonText;
        public boolean isOverflowOpen;
        public CharSequence title;

        SavedState(Parcel parcel) {
            super(parcel);
            this.isOverflowOpen = parcel.readInt() != 0;
            this.title = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.defaultButtonText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.isOverflowOpen ? 1 : 0);
            TextUtils.writeToParcel(this.title, parcel, 0);
            TextUtils.writeToParcel(this.defaultButtonText, parcel, 0);
        }
    }

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16843668);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOnMenuItemClickListener = new OnClickListener() {
            public void onClick(View view) {
                ActionMenuItem access$000 = view.getId() == 16908313 ? ActionBarContextView.this.mButton1MenuItem : ActionBarContextView.this.mButton2MenuItem;
                EditActionModeImpl editActionModeImpl = (EditActionModeImpl) ActionBarContextView.this.mActionMode.get();
                if (editActionModeImpl != null) {
                    editActionModeImpl.onMenuItemSelected((MenuBuilder) editActionModeImpl.getMenu(), access$000);
                }
                if (HapticFeedbackUtil.isSupportLinearMotorVibrate(268435456)) {
                    view.performHapticFeedback(268435456);
                }
            }
        };
        this.mCollapseController = new CollapseView();
        this.mMovableController = new CollapseView();
        this.mTouchScrolling = false;
        this.mNonTouchScrolling = false;
        this.mPostScroll = new Runnable() {
            public void run() {
                ActionBarContextView actionBarContextView;
                int i;
                if (ActionBarContextView.this.mPostScroller.computeScrollOffset()) {
                    ActionBarContextView actionBarContextView2 = ActionBarContextView.this;
                    actionBarContextView2.mPendingHeight = actionBarContextView2.mPostScroller.getCurrY() - ActionBarContextView.this.mCollapseHeight;
                    ActionBarContextView.this.requestLayout();
                    if (!ActionBarContextView.this.mPostScroller.isFinished()) {
                        ActionBarContextView.this.postOnAnimation(this);
                        return;
                    }
                    if (ActionBarContextView.this.mPostScroller.getCurrY() == ActionBarContextView.this.mCollapseHeight) {
                        actionBarContextView = ActionBarContextView.this;
                        i = 0;
                    } else if (ActionBarContextView.this.mPostScroller.getCurrY() == ActionBarContextView.this.mCollapseHeight + ActionBarContextView.this.mMovableContainer.getMeasuredHeight()) {
                        actionBarContextView = ActionBarContextView.this;
                        i = 1;
                    } else {
                        return;
                    }
                    actionBarContextView.setExpandState(i);
                }
            }
        };
        this.mPostScroller = new Scroller(context);
        this.mMovableContainer = new FrameLayout(context);
        this.mMovableContainer.setId(R.id.action_bar_movable_container);
        this.mMovableContainer.setPaddingRelative(context.getResources().getDimensionPixelOffset(R.dimen.action_bar_title_horizontal_padding), context.getResources().getDimensionPixelOffset(R.dimen.action_bar_title_top_padding), context.getResources().getDimensionPixelOffset(R.dimen.action_bar_title_horizontal_padding), context.getResources().getDimensionPixelOffset(R.dimen.action_bar_title_bottom_padding));
        this.mMovableContainer.setVisibility(0);
        this.mMovableController.attach(this.mMovableContainer);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, miui.R.styleable.ActionMode, i, 0);
        this.mActionModeBackground = obtainStyledAttributes.getDrawable(miui.R.styleable.ActionMode_android_background);
        setBackground(this.mActionModeBackground);
        this.mTitleStyleRes = obtainStyledAttributes.getResourceId(miui.R.styleable.ActionMode_android_titleTextStyle, 0);
        this.mExpandTitleStyleRes = obtainStyledAttributes.getResourceId(miui.R.styleable.ActionMode_miui_expandTitleTextStyle, 0);
        this.mContentHeight = obtainStyledAttributes.getLayoutDimension(miui.R.styleable.ActionMode_android_height, 0);
        this.mSplitBackground = obtainStyledAttributes.getDrawable(miui.R.styleable.ActionMode_android_backgroundSplit);
        Context context2 = context;
        ActionMenuItem actionMenuItem = new ActionMenuItem(context2, 0, EditActionMode.BUTTON1, 0, 0, context.getString(17039360));
        this.mButton1MenuItem = actionMenuItem;
        ActionMenuItem actionMenuItem2 = new ActionMenuItem(context2, 0, EditActionMode.BUTTON2, 0, 0, context.getString(miui.R.string.action_mode_select_all));
        this.mButton2MenuItem = actionMenuItem2;
        obtainStyledAttributes.recycle();
    }

    private void animateLayoutWithProcess(float f) {
        float min = 1.0f - Math.min(1.0f, f * 3.0f);
        int i = this.mInnerExpandState;
        if (i == 2) {
            if (min > 0.0f) {
                this.mCollapseController.animTo(0.0f, 0, 20, this.mCollapseAnimHideConfig);
            } else {
                this.mCollapseController.animTo(1.0f, 0, 0, this.mCollapseAnimShowConfig);
            }
            this.mMovableController.animTo(min, 0, 0, this.mMovableAnimConfig);
        } else if (i == 1) {
            this.mCollapseController.setAnimFrom(0.0f, 0, 20);
            this.mMovableController.setAnimFrom(1.0f, 0, 0);
        } else if (i == 0) {
            this.mCollapseController.setAnimFrom(1.0f, 0, 0);
            this.mMovableController.setAnimFrom(0.0f, 0, 0);
        }
    }

    private void clearBackground() {
        setBackground(null);
        if (this.mSplitActionBar) {
            ActionMenuView actionMenuView = this.mMenuView;
            if (actionMenuView != null) {
                actionMenuView.setBackground(null);
            }
        }
        FrameLayout frameLayout = this.mMovableContainer;
        if (frameLayout != null) {
            frameLayout.setBackground(null);
        }
    }

    private SpringAnimation getViewSpringAnima(View view, float f, float f2, float f3) {
        SpringAnimation springAnimation = new SpringAnimation(view, ViewProperty.ALPHA, f3);
        springAnimation.setStartValue(f2);
        springAnimation.getSpring().setStiffness(f);
        springAnimation.getSpring().setDampingRatio(0.9f);
        springAnimation.setMinimumVisibleChange(0.00390625f);
        return springAnimation;
    }

    private void onLayoutCollapseViews(int i, int i2, int i3, int i4) {
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop() + this.mContentInset;
        int paddingTop2 = (((i4 - i2) - getPaddingTop()) - getPaddingBottom()) - this.mContentInset;
        LinearLayout linearLayout = this.mTitleLayout;
        if (!(linearLayout == null || linearLayout.getVisibility() == 8)) {
            positionChild(this.mTitleLayout, paddingStart, paddingTop, paddingTop2);
        }
        int paddingEnd = (i3 - i) - getPaddingEnd();
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null && actionMenuView.getParent() == this) {
            positionChildInverse(this.mMenuView, paddingEnd, paddingTop, paddingTop2);
        }
        if (this.mRequestAnimation) {
            this.mAnimationMode = 1;
            makeInOutAnimator(true).start();
            this.mRequestAnimation = false;
        }
    }

    private void resetBackground() {
        setBackground(this.mActionModeBackground);
        if (this.mSplitActionBar) {
            ActionMenuView actionMenuView = this.mMenuView;
            if (actionMenuView != null) {
                actionMenuView.setBackground(this.mSplitBackground);
            }
        }
    }

    private void setButtonContentDescription(int i, int i2) {
        int i3;
        Resources resources;
        Button button = i == 16908313 ? this.mButton1 : i == 16908314 ? this.mButton2 : null;
        if (button != null) {
            if (R.drawable.action_mode_title_button_cancel_light == i2 || R.drawable.action_mode_title_button_cancel_dark == i2) {
                resources = getResources();
                i3 = R.string.cancel_description;
            } else if (R.drawable.action_mode_title_button_confirm_light == i2 || R.drawable.action_mode_title_button_confirm_dark == i2) {
                resources = getResources();
                i3 = R.string.confirm_description;
            } else if (R.drawable.action_mode_title_button_select_all_light == i2 || R.drawable.action_mode_title_button_select_all_dark == i2) {
                resources = getResources();
                i3 = R.string.select_all_description;
            } else if (R.drawable.action_mode_title_button_deselect_all_light == i2 || R.drawable.action_mode_title_button_deselect_all_dark == i2) {
                resources = getResources();
                i3 = R.string.deselect_all_description;
            } else {
                if (R.drawable.action_mode_title_button_delete_light == i2 || R.drawable.action_mode_title_button_delete_dark == i2) {
                    resources = getResources();
                    i3 = R.string.delete_description;
                }
            }
            button.setContentDescription(resources.getString(i3));
        }
    }

    /* access modifiers changed from: private */
    public void setSplitAnimating(boolean z) {
        ActionBarContainer actionBarContainer = this.mSplitView;
        if (actionBarContainer != null) {
            ((ActionBarOverlayLayout) actionBarContainer.getParent()).setAnimating(z);
        }
    }

    public void addAnimationListener(ActionModeAnimationListener actionModeAnimationListener) {
        if (actionModeAnimationListener != null) {
            if (this.mAnimationListeners == null) {
                this.mAnimationListeners = new ArrayList();
            }
            this.mAnimationListeners.add(actionModeAnimationListener);
        }
    }

    public /* bridge */ /* synthetic */ void animateToVisibility(int i) {
        super.animateToVisibility(i);
    }

    public void animateToVisibility(boolean z) {
        cancelAnimation();
        setSplitAnimating(true);
        if (z) {
            setVisibility(0);
            this.mRequestAnimation = true;
            return;
        }
        makeInOutAnimator(z).start();
    }

    /* access modifiers changed from: protected */
    public void cancelAnimation() {
        SpringAnimationSet springAnimationSet = this.mVisibilityAnim;
        if (springAnimationSet != null) {
            springAnimationSet.cancel();
            this.mVisibilityAnim = null;
        }
        setSplitAnimating(false);
    }

    public void closeMode() {
        endAnimation();
        this.mAnimationMode = 2;
    }

    public /* bridge */ /* synthetic */ void dismissPopupMenus() {
        super.dismissPopupMenus();
    }

    /* access modifiers changed from: protected */
    public void endAnimation() {
        SpringAnimationSet springAnimationSet = this.mVisibilityAnim;
        if (springAnimationSet != null) {
            springAnimationSet.endAnimation();
            this.mVisibilityAnim = null;
        }
        setSplitAnimating(false);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new MarginLayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: 0000 */
    public int getActionBarStyle() {
        return 16843668;
    }

    public /* bridge */ /* synthetic */ ActionBarTransitionListener getActionBarTransitionListener() {
        return super.getActionBarTransitionListener();
    }

    public /* bridge */ /* synthetic */ ActionMenuView getActionMenuView() {
        return super.getActionMenuView();
    }

    public /* bridge */ /* synthetic */ int getAnimatedVisibility() {
        return super.getAnimatedVisibility();
    }

    public float getAnimationProgress() {
        return this.mAnimationProgress;
    }

    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    public /* bridge */ /* synthetic */ int getExpandState() {
        return super.getExpandState();
    }

    public /* bridge */ /* synthetic */ ActionMenuView getMenuView() {
        return super.getMenuView();
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu(false);
    }

    public void initForMode(ActionMode actionMode) {
        if (this.mActionMode != null) {
            cancelAnimation();
            killMode();
        }
        initTitle();
        this.mActionMode = new WeakReference(actionMode);
        MenuBuilder menuBuilder = (MenuBuilder) actionMode.getMenu();
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus(false);
        }
        ActionMenuPresenter actionMenuPresenter2 = new ActionMenuPresenter(getContext(), R.layout.action_menu_layout, R.layout.action_mode_menu_item_layout, R.layout.action_bar_expanded_menu_layout, R.layout.action_bar_list_menu_item_layout);
        this.mActionMenuPresenter = actionMenuPresenter2;
        this.mActionMenuPresenter.setReserveOverflow(true);
        this.mActionMenuPresenter.setActionEditMode(true);
        int i = -2;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -1);
        if (!this.mSplitActionBar) {
            menuBuilder.addMenuPresenter(this.mActionMenuPresenter);
            this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
            this.mMenuView.setBackground(null);
            addView(this.mMenuView, layoutParams);
            return;
        }
        this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
        layoutParams.width = -1;
        if (DeviceHelper.IS_TABLET) {
            i = -1;
        }
        layoutParams.height = i;
        layoutParams.gravity = DeviceHelper.IS_TABLET ? 17 : 80;
        menuBuilder.addMenuPresenter(this.mActionMenuPresenter);
        this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
        this.mMenuView.setBackground(this.mSplitBackground);
        this.mSplitView.addView(this.mMenuView, layoutParams);
    }

    /* access modifiers changed from: protected */
    public void initTitle() {
        int i = 0;
        if (this.mTitleLayout == null) {
            this.mTitleLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.action_mode_title_item, this, false);
            this.mButton1 = (Button) this.mTitleLayout.findViewById(EditActionMode.BUTTON1);
            this.mButton2 = (Button) this.mTitleLayout.findViewById(EditActionMode.BUTTON2);
            Button button = this.mButton1;
            if (button != null) {
                button.setOnClickListener(this.mOnMenuItemClickListener);
                FolmeAnimHelper.addAlphaPressAnim(this.mButton1);
            }
            Button button2 = this.mButton2;
            if (button2 != null) {
                button2.setOnClickListener(this.mOnMenuItemClickListener);
                FolmeAnimHelper.addAlphaPressAnim(this.mButton2);
            }
            this.mTitleView = (TextView) this.mTitleLayout.findViewById(16908310);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(getContext(), this.mTitleStyleRes);
            }
            this.mExpandTitleView = new TextView(getContext());
            if (this.mExpandTitleStyleRes != 0) {
                this.mExpandTitleView.setTextAppearance(getContext(), this.mExpandTitleStyleRes);
            }
        }
        this.mTitleView.setText(this.mTitle);
        this.mExpandTitleView.setText(this.mTitle);
        this.mCollapseContainer = this.mTitleView;
        this.mCollapseController.attach(this.mCollapseContainer);
        boolean z = !TextUtils.isEmpty(this.mTitle);
        this.mTitleLayout.setVisibility(z ? 0 : 8);
        TextView textView = this.mExpandTitleView;
        if (!z) {
            i = 8;
        }
        textView.setVisibility(i);
        if (this.mTitleLayout.getParent() == null) {
            addView(this.mTitleLayout);
        }
        if (this.mExpandTitleView.getParent() == null) {
            this.mMovableContainer.addView(this.mExpandTitleView);
        }
        if (this.mMovableContainer.getParent() == null) {
            addView(this.mMovableContainer);
        }
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    public /* bridge */ /* synthetic */ boolean isOverflowReserved() {
        return super.isOverflowReserved();
    }

    public /* bridge */ /* synthetic */ boolean isResizable() {
        return super.isResizable();
    }

    public boolean isTitleOptional() {
        return this.mTitleOptional;
    }

    public void killMode() {
        removeAllViews();
        List list = this.mAnimationListeners;
        if (list != null) {
            list.clear();
            this.mAnimationListeners = null;
        }
        ActionBarContainer actionBarContainer = this.mSplitView;
        if (actionBarContainer != null) {
            actionBarContainer.removeView(this.mMenuView);
        }
        this.mMenuView = null;
        this.mActionMode = null;
    }

    /* access modifiers changed from: protected */
    public SpringAnimationSet makeInOutAnimator(boolean z) {
        float f;
        float f2;
        int i;
        int i2;
        SpringAnimationSet springAnimationSet;
        boolean z2 = z;
        if (z2 == this.mAnimateToVisible && this.mVisibilityAnim != null) {
            return new SpringAnimationSet();
        }
        this.mAnimateToVisible = z2;
        ActionMenuView actionMenuView = this.mMenuView;
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) this.mSplitView.getParent();
        int collapsedHeight = actionMenuView.getCollapsedHeight();
        float translationY = actionMenuView.getTranslationY();
        if (z2) {
            f = 0.0f;
            f2 = 1.0f;
            i = collapsedHeight;
            i2 = 0;
        } else {
            f2 = 0.0f;
            f = 1.0f;
            i2 = collapsedHeight;
            i = 0;
        }
        SpringAnimationSet springAnimationSet2 = new SpringAnimationSet();
        float f3 = STIFFNESS_HIGH;
        SpringAnimation viewSpringAnima = getViewSpringAnima(this, z2 ? STIFFNESS_LOW : 986.96f, f, f2);
        viewSpringAnima.setStartDelay(z2 ? 50 : 0);
        springAnimationSet2.play(viewSpringAnima);
        setAlpha(f);
        if (!this.mSplitActionBar) {
            viewSpringAnima.addEndListener(new DOnAnimationEndListener(z2));
            this.mVisibilityAnim = springAnimationSet2;
            return springAnimationSet2;
        }
        this.mAnimateStart = false;
        int i3 = z2 ? 100 : 0;
        if (z2) {
            f3 = STIFFNESS_MEDIUM;
        }
        AnonymousClass2 r17 = r0;
        float f4 = f3;
        int i4 = i3;
        final ActionMenuView actionMenuView2 = actionMenuView;
        SpringAnimationSet springAnimationSet3 = springAnimationSet2;
        final float f5 = translationY;
        int i5 = i2;
        final int i6 = collapsedHeight;
        int i7 = i;
        final boolean z3 = z;
        float f6 = f2;
        final int i8 = i7;
        float f7 = f;
        final int i9 = i5;
        AnonymousClass2 r0 = new FloatProperty("") {
            public float getValue(ActionBarOverlayLayout actionBarOverlayLayout) {
                return 0.0f;
            }

            public void setValue(ActionBarOverlayLayout actionBarOverlayLayout, float f) {
                ActionMenuView actionMenuView = actionMenuView2;
                if (actionMenuView != null) {
                    actionMenuView.setTranslationY((f5 + ((float) i6)) - f);
                }
                actionBarOverlayLayout.animateContentMarginBottom((int) f);
                if (!ActionBarContextView.this.mAnimateStart) {
                    ActionBarContextView.this.notifyAnimationStart(z3);
                    ActionBarContextView.this.mAnimateStart = true;
                    return;
                }
                int i = i8;
                int i2 = i9;
                ActionBarContextView.this.notifyAnimationUpdate(z3, i == i2 ? 1.0f : (f - ((float) i2)) / ((float) (i - i2)));
            }
        };
        SpringAnimation springAnimation = new SpringAnimation(actionBarOverlayLayout, r17, (float) i7);
        int i10 = i5;
        float f8 = (float) i10;
        springAnimation.setStartValue(f8);
        springAnimation.getSpring().setStiffness(f4);
        springAnimation.getSpring().setDampingRatio(0.9f);
        long j = (long) i4;
        springAnimation.setStartDelay(j);
        float f9 = f4;
        springAnimation.addEndListener(new DOnAnimationEndListener(z3));
        if (actionMenuView != null) {
            actionMenuView.setTranslationY((translationY + ((float) collapsedHeight)) - f8);
        }
        actionBarOverlayLayout.animateContentMarginBottom(i10);
        if (actionMenuView != null) {
            float f10 = f7;
            SpringAnimation viewSpringAnima2 = getViewSpringAnima(actionMenuView, f9, f10, f6);
            viewSpringAnima2.setStartDelay(j);
            actionMenuView.setAlpha(f10);
            SpringAnimation[] springAnimationArr = {springAnimation, viewSpringAnima2};
            springAnimationSet = springAnimationSet3;
            springAnimationSet.playTogether(springAnimationArr);
        } else {
            springAnimationSet = springAnimationSet3;
            springAnimationSet.play(springAnimation);
        }
        this.mVisibilityAnim = springAnimationSet;
        return springAnimationSet;
    }

    public void notifyAnimationEnd(boolean z) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list != null) {
            for (ActionModeAnimationListener onStop : list) {
                onStop.onStop(z);
            }
        }
    }

    public void notifyAnimationStart(boolean z) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list != null) {
            for (ActionModeAnimationListener onStart : list) {
                onStart.onStart(z);
            }
        }
    }

    public void notifyAnimationUpdate(boolean z, float f) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list != null) {
            for (ActionModeAnimationListener onUpdate : list) {
                onUpdate.onUpdate(z, f);
            }
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.hideOverflowMenu(false);
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    /* access modifiers changed from: protected */
    public void onExpandStateChanged(int i, int i2) {
        if (i == 2) {
            this.mPendingHeight = 0;
            if (!this.mPostScroller.isFinished()) {
                this.mPostScroller.forceFinished(true);
            }
        }
        if (i2 != 1) {
            this.mCollapseController.setVisibility(0);
        }
        if (i2 != 0) {
            this.mMovableContainer.setVisibility(0);
        }
        if (i2 == 1) {
            this.mCollapseController.setVisibility(4);
        } else if (i2 == 0) {
            this.mMovableContainer.setVisibility(8);
        } else {
            this.mPendingHeight = getHeight() - this.mCollapseHeight;
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = this.mInnerExpandState;
        int i6 = i5 == 2 ? this.mPendingHeight : i5 == 1 ? this.mMovableContainer.getMeasuredHeight() : 0;
        int i7 = i4 - i2;
        int i8 = i7 - i6;
        onLayoutCollapseViews(i, i2, i3, i8);
        onLayoutExpandViews(z, i, i8, i3, i7);
        animateLayoutWithProcess(((float) (this.mMovableContainer.getMeasuredHeight() - i6)) / ((float) this.mMovableContainer.getMeasuredHeight()));
    }

    /* access modifiers changed from: protected */
    public void onLayoutExpandViews(boolean z, int i, int i2, int i3, int i4) {
        FrameLayout frameLayout = this.mMovableContainer;
        if (frameLayout != null && frameLayout.getVisibility() == 0 && this.mInnerExpandState != 0) {
            FrameLayout frameLayout2 = this.mMovableContainer;
            frameLayout2.layout(i, i4 - frameLayout2.getMeasuredHeight(), i3, i4);
            if (ViewUtils.isLayoutRtl(this)) {
                i = i3 - this.mMovableContainer.getMeasuredWidth();
            }
            Rect rect = new Rect();
            rect.set(i, this.mMovableContainer.getMeasuredHeight() - (i4 - i2), this.mMovableContainer.getMeasuredWidth() + i, this.mMovableContainer.getMeasuredHeight());
            this.mMovableContainer.setClipBounds(rect);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int measuredHeight;
        int size = MeasureSpec.getSize(i);
        int i6 = this.mContentHeight;
        if (i6 <= 0) {
            i6 = MeasureSpec.getSize(i2);
        }
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(i6 - paddingTop, Integer.MIN_VALUE);
        ActionMenuView actionMenuView = this.mMenuView;
        int i7 = 0;
        if (actionMenuView == null || actionMenuView.getParent() != this) {
            i3 = 0;
        } else {
            paddingLeft = measureChildView(this.mMenuView, paddingLeft, makeMeasureSpec, 0);
            i3 = this.mMenuView.getMeasuredHeight() + 0;
        }
        LinearLayout linearLayout = this.mTitleLayout;
        if (!(linearLayout == null || linearLayout.getVisibility() == 8)) {
            this.mTitleLayout.measure(MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824), makeMeasureSpec);
            i3 += this.mTitleLayout.getMeasuredHeight();
        }
        int i8 = this.mContentHeight;
        if (i8 <= 0) {
            int childCount = getChildCount();
            int i9 = 0;
            for (int i10 = 0; i10 < childCount; i10++) {
                int measuredHeight2 = getChildAt(i10).getMeasuredHeight() + paddingTop;
                if (measuredHeight2 > i9) {
                    i9 = measuredHeight2;
                }
            }
            if (i9 > 0) {
                i7 = i9 + this.mContentInset;
            }
            setMeasuredDimension(size, i7);
            return;
        }
        this.mCollapseHeight = i3 > 0 ? this.mContentInset + i8 : 0;
        this.mMovableContainer.measure(MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(0, 0));
        int i11 = this.mInnerExpandState;
        if (i11 == 2) {
            i5 = this.mCollapseHeight;
            measuredHeight = this.mPendingHeight;
        } else if (i11 == 1) {
            i5 = this.mCollapseHeight;
            measuredHeight = this.mMovableContainer.getMeasuredHeight();
        } else {
            i4 = this.mCollapseHeight;
            setMeasuredDimension(size, i4);
        }
        i4 = i5 + measuredHeight;
        setMeasuredDimension(size, i4);
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3, int[] iArr2) {
        if (i2 > 0 && getHeight() > this.mCollapseHeight) {
            int height = getHeight() - i2;
            int i4 = this.mPendingHeight;
            int i5 = this.mCollapseHeight;
            if (height >= i5) {
                this.mPendingHeight = i4 - i2;
                iArr[1] = iArr[1] + i2;
            } else {
                int height2 = i5 - getHeight();
                this.mPendingHeight = 0;
                iArr[1] = iArr[1] + (-height2);
            }
            int i6 = this.mPendingHeight;
            if (i6 != i4) {
                iArr2[1] = i4 - i6;
                requestLayout();
            }
        }
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr, int[] iArr2) {
        if (i4 < 0 && getHeight() < this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight()) {
            int i6 = this.mPendingHeight;
            if (getHeight() - i4 <= this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight()) {
                this.mPendingHeight -= i4;
                iArr[1] = iArr[1] + i4;
            } else {
                int measuredHeight = (this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight()) - getHeight();
                this.mPendingHeight = this.mMovableContainer.getMeasuredHeight();
                iArr[1] = iArr[1] + (-measuredHeight);
            }
            int i7 = this.mPendingHeight;
            if (i7 != i6) {
                iArr2[1] = i6 - i7;
                requestLayout();
            }
        }
    }

    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        if (i2 == 0) {
            this.mTouchScrolling = true;
        } else {
            this.mNonTouchScrolling = true;
        }
        if (!this.mPostScroller.isFinished()) {
            this.mPostScroller.forceFinished(true);
        }
        setExpandState(2);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setTitle(savedState.title);
        setButton(EditActionMode.BUTTON2, savedState.defaultButtonText);
        if (savedState.isOverflowOpen) {
            postShowOverflowMenu();
        }
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isOverflowOpen = isOverflowMenuShowing();
        savedState.title = getTitle();
        Button button = this.mButton2;
        if (button != null) {
            savedState.defaultButtonText = button.getText();
        }
        return savedState;
    }

    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        if (getContext().getResources().getConfiguration().orientation == 2) {
            return false;
        }
        return isResizable();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000a, code lost:
        if (r3.mNonTouchScrolling == false) goto L_0x0013;
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0018  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onStopNestedScroll(View view, int i) {
        boolean z;
        int i2;
        Scroller scroller;
        int i3;
        if (this.mTouchScrolling) {
            this.mTouchScrolling = false;
        } else {
            if (this.mNonTouchScrolling) {
                this.mNonTouchScrolling = false;
            }
            z = false;
            if (z) {
                if (getHeight() == this.mCollapseHeight) {
                    setExpandState(0);
                    return;
                } else if (getHeight() == this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight() && this.mPendingHeight == this.mMovableContainer.getMeasuredHeight()) {
                    setExpandState(1);
                    return;
                } else {
                    if (getHeight() > this.mCollapseHeight + (this.mMovableContainer.getMeasuredHeight() / 2)) {
                        scroller = this.mPostScroller;
                        i2 = getHeight();
                        i3 = this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight();
                    } else {
                        scroller = this.mPostScroller;
                        i2 = getHeight();
                        i3 = this.mCollapseHeight;
                    }
                    scroller.startScroll(0, i2, 0, i3 - getHeight());
                    postOnAnimation(this.mPostScroll);
                }
            }
        }
        z = true;
        if (z) {
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public /* bridge */ /* synthetic */ void postShowOverflowMenu() {
        super.postShowOverflowMenu();
    }

    public void removeAnimationListener(ActionModeAnimationListener actionModeAnimationListener) {
        if (actionModeAnimationListener != null) {
            List list = this.mAnimationListeners;
            if (list != null) {
                list.remove(actionModeAnimationListener);
            }
        }
    }

    public /* bridge */ /* synthetic */ void setActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener) {
        super.setActionBarTransitionListener(actionBarTransitionListener);
    }

    public void setAnimationProgress(float f) {
        this.mAnimationProgress = f;
        notifyAnimationUpdate(this.mAnimateToVisible, this.mAnimationProgress);
    }

    public void setButton(int i, CharSequence charSequence) {
        ActionMenuItem actionMenuItem;
        initTitle();
        int i2 = 8;
        if (i == 16908313) {
            Button button = this.mButton1;
            if (button != null) {
                if (!TextUtils.isEmpty(charSequence)) {
                    i2 = 0;
                }
                button.setVisibility(i2);
                this.mButton1.setText(charSequence);
            }
            actionMenuItem = this.mButton1MenuItem;
        } else if (i == 16908314) {
            Button button2 = this.mButton2;
            if (button2 != null) {
                if (!TextUtils.isEmpty(charSequence)) {
                    i2 = 0;
                }
                button2.setVisibility(i2);
                this.mButton2.setText(charSequence);
            }
            actionMenuItem = this.mButton2MenuItem;
        } else {
            return;
        }
        actionMenuItem.setTitle(charSequence);
    }

    public void setButton(int i, CharSequence charSequence, int i2) {
        ActionMenuItem actionMenuItem;
        initTitle();
        int i3 = 8;
        if (i == 16908313) {
            Button button = this.mButton1;
            if (button != null) {
                if (!TextUtils.isEmpty(charSequence) || i2 != 0) {
                    i3 = 0;
                }
                button.setVisibility(i3);
                this.mButton1.setText(charSequence);
                this.mButton1.setBackgroundResource(i2);
            }
            actionMenuItem = this.mButton1MenuItem;
        } else {
            if (i == 16908314) {
                Button button2 = this.mButton2;
                if (button2 != null) {
                    if (!TextUtils.isEmpty(charSequence) || i2 != 0) {
                        i3 = 0;
                    }
                    button2.setVisibility(i3);
                    this.mButton2.setText(charSequence);
                    this.mButton2.setBackgroundResource(i2);
                }
                actionMenuItem = this.mButton2MenuItem;
            }
            if (TextUtils.isEmpty(charSequence) && i2 != 0) {
                setButtonContentDescription(i, i2);
                return;
            }
        }
        actionMenuItem.setTitle(charSequence);
        if (TextUtils.isEmpty(charSequence)) {
        }
    }

    public /* bridge */ /* synthetic */ void setContentHeight(int i) {
        super.setContentHeight(i);
    }

    public void setContentInset(int i) {
        this.mContentInset = i;
    }

    public /* bridge */ /* synthetic */ void setExpandState(int i) {
        super.setExpandState(i);
    }

    public /* bridge */ /* synthetic */ void setExpandState(int i, boolean z) {
        super.setExpandState(i, z);
    }

    public /* bridge */ /* synthetic */ void setResizable(boolean z) {
        super.setResizable(z);
    }

    public void setSplitActionBar(boolean z) {
        if (this.mSplitActionBar != z) {
            if (this.mActionMenuPresenter != null) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -1);
                if (!z) {
                    this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                    this.mMenuView.setBackground(null);
                    ViewGroup viewGroup = (ViewGroup) this.mMenuView.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(this.mMenuView);
                    }
                    addView(this.mMenuView, layoutParams);
                } else {
                    this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
                    layoutParams.width = -1;
                    layoutParams.height = -2;
                    layoutParams.gravity = DeviceHelper.IS_TABLET ? 17 : 80;
                    this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                    this.mMenuView.setBackground(this.mSplitBackground);
                    ViewGroup viewGroup2 = (ViewGroup) this.mMenuView.getParent();
                    if (viewGroup2 != null) {
                        viewGroup2.removeView(this.mMenuView);
                    }
                    this.mSplitView.addView(this.mMenuView, layoutParams);
                }
            }
            super.setSplitActionBar(z);
        }
    }

    public /* bridge */ /* synthetic */ void setSplitView(ActionBarContainer actionBarContainer) {
        super.setSplitView(actionBarContainer);
    }

    public /* bridge */ /* synthetic */ void setSplitWhenNarrow(boolean z) {
        super.setSplitWhenNarrow(z);
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        initTitle();
    }

    public void setTitleOptional(boolean z) {
        if (z != this.mTitleOptional) {
            requestLayout();
        }
        this.mTitleOptional = z;
    }

    public /* bridge */ /* synthetic */ void setVisibility(int i) {
        super.setVisibility(i);
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
    }

    public void updateBackground(boolean z) {
        if (z) {
            clearBackground();
        } else {
            resetBackground();
        }
    }
}
