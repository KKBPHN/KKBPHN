package com.miui.internal.widget;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window.Callback;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import com.miui.internal.R;
import com.miui.internal.app.ActionBarDelegateImpl;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.util.FolmeAnimHelper;
import com.miui.internal.view.ActionBarPolicy;
import com.miui.internal.view.menu.ActionMenuItem;
import com.miui.internal.view.menu.ActionMenuPresenter;
import com.miui.internal.view.menu.ActionMenuView;
import com.miui.internal.view.menu.MenuBuilder;
import com.miui.internal.view.menu.MenuItemImpl;
import com.miui.internal.view.menu.MenuPresenter;
import com.miui.internal.view.menu.MenuView;
import com.miui.internal.view.menu.SubMenuBuilder;
import com.miui.internal.widget.AbsActionBarView.CollapseView;
import java.lang.ref.WeakReference;
import miui.animation.Folme;
import miui.animation.IStateStyle;
import miui.animation.base.AnimConfig;
import miui.animation.listener.TransitionListener;
import miui.animation.property.IIntValueProperty;
import miui.app.ActionBarTransitionListener;
import miui.util.AttributeResolver;
import miui.util.ViewUtils;
import miui.view.springback.SpringBackLayout;

public class ActionBarView extends AbsActionBarView {
    private static final int DEFAULT_CUSTOM_GRAVITY = 8388627;
    public static final int DISPLAY_DEFAULT = 0;
    private static final int DISPLAY_RELAYOUT_MASK = 31;
    private static final int ICON_INITIALIZED = 1;
    private static final int LOGO_INITIALIZED = 2;
    private static final String TAG = "ActionBarView";
    private static final int TYPE_NON_TOUCH = 1;
    private static final int TYPE_TOUCH = 0;
    /* access modifiers changed from: private */
    public OnNavigationListener mCallback;
    private FrameLayout mCollapseContainer;
    /* access modifiers changed from: private */
    public CollapseView mCollapseController = new CollapseView();
    private FrameLayout mCollapseCustomContainer;
    /* access modifiers changed from: private */
    public int mCollapseHeight;
    private int mCollapseSubtitleStyleRes;
    private TextView mCollapseSubtitleView;
    /* access modifiers changed from: private */
    public ScrollingTabContainerView mCollapseTabs;
    /* access modifiers changed from: private */
    public LinearLayout mCollapseTitleLayout;
    private boolean mCollapseTitleShowable = true;
    private int mCollapseTitleStyleRes;
    private TextView mCollapseTitleView;
    private Context mContext;
    /* access modifiers changed from: private */
    public View mCustomNavView;
    private final TextWatcher mCustomTitleWatcher = new TextWatcher() {
        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            ActionBarView.this.mExpandTitleView.setText(charSequence);
        }
    };
    /* access modifiers changed from: private */
    public int mDisplayOptions = -1;
    private View mEndView;
    private Rect mExpandBounds = new Rect();
    private int mExpandSubtitleStyleRes;
    private TextView mExpandSubtitleView;
    /* access modifiers changed from: private */
    public ScrollingTabContainerView mExpandTabs;
    private LinearLayout mExpandTitleLayout;
    private int mExpandTitleStyleRes;
    /* access modifiers changed from: private */
    public TextView mExpandTitleView;
    View mExpandedActionView;
    private final OnClickListener mExpandedActionViewUpListener = new OnClickListener() {
        public void onClick(View view) {
            MenuItemImpl menuItemImpl = ActionBarView.this.mExpandedMenuPresenter.mCurrentExpandedItem;
            if (menuItemImpl != null) {
                menuItemImpl.collapseActionView();
            }
        }
    };
    /* access modifiers changed from: private */
    public HomeView mExpandedHomeLayout;
    /* access modifiers changed from: private */
    public ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private Drawable mHomeAsUpIndicatorDrawable;
    private int mHomeAsUpIndicatorResId;
    /* access modifiers changed from: private */
    public HomeView mHomeLayout;
    private final int mHomeResId;
    private Drawable mIcon;
    private int mIconLogoInitIndicator;
    private View mImmersionView;
    private boolean mInActionMode = false;
    /* access modifiers changed from: private */
    public boolean mInSearchMode = false;
    private boolean mIncludeTabs;
    private ProgressBar mIndeterminateProgressView;
    private boolean mIsCollapsed;
    private boolean mIsTitleHidding = false;
    private boolean mIsTitleShowing = false;
    private int mItemPadding;
    private LinearLayout mListNavLayout;
    private Drawable mLogo;
    /* access modifiers changed from: private */
    public ActionMenuItem mLogoNavItem;
    /* access modifiers changed from: private */
    public FrameLayout mMovableContainer;
    private CollapseView mMovableController = new CollapseView();
    private final OnItemSelectedListener mNavItemSelectedListener = new OnItemSelectedListener() {
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            if (ActionBarView.this.mCallback != null) {
                ActionBarView.this.mCallback.onNavigationItemSelected(i, j);
            }
        }

        public void onNothingSelected(AdapterView adapterView) {
        }
    };
    /* access modifiers changed from: private */
    public int mNavigationMode;
    private boolean mNonTouchScrolling = false;
    private MenuBuilder mOptionsMenu;
    /* access modifiers changed from: private */
    public int mPendingHeight;
    private Runnable mPostScroll = new Runnable() {
        public void run() {
            ActionBarView actionBarView;
            int i;
            if (ActionBarView.this.mPostScroller.computeScrollOffset()) {
                ActionBarView actionBarView2 = ActionBarView.this;
                actionBarView2.mPendingHeight = actionBarView2.mPostScroller.getCurrY() - ActionBarView.this.mCollapseHeight;
                ActionBarView.this.requestLayout();
                if (!ActionBarView.this.mPostScroller.isFinished()) {
                    ActionBarView.this.postOnAnimation(this);
                    return;
                }
                if (ActionBarView.this.mPostScroller.getCurrY() == ActionBarView.this.mCollapseHeight) {
                    actionBarView = ActionBarView.this;
                    i = 0;
                } else if (ActionBarView.this.mPostScroller.getCurrY() == ActionBarView.this.mCollapseHeight + ActionBarView.this.mMovableContainer.getMeasuredHeight()) {
                    actionBarView = ActionBarView.this;
                    i = 1;
                } else {
                    return;
                }
                actionBarView.setExpandState(i);
            }
        }
    };
    /* access modifiers changed from: private */
    public Scroller mPostScroller;
    private int mProgressBarPadding;
    private ProgressBar mProgressView;
    private Rect mSecondaryBounds = new Rect();
    private SpringBackLayout mSecondaryContainer;
    private CollapseView mSecondaryController = new CollapseView();
    /* access modifiers changed from: private */
    public ScrollingTabContainerView mSecondaryTabs;
    /* access modifiers changed from: private */
    public Spinner mSpinner;
    private SpinnerAdapter mSpinnerAdapter;
    private View mStartView;
    private IStateStyle mStateChangeAnimStateStyle = null;
    private CharSequence mSubtitle;
    /* access modifiers changed from: private */
    public boolean mTempResizable;
    private CharSequence mTitle;
    private boolean mTitleCenter;
    private final OnClickListener mTitleClickListener = new OnClickListener() {
        public void onClick(View view) {
            ActionBarView actionBarView = ActionBarView.this;
            actionBarView.mWindowCallback.onMenuItemSelected(0, actionBarView.mTitleNavItem);
        }
    };
    /* access modifiers changed from: private */
    public ActionMenuItem mTitleNavItem;
    private IStateStyle mTitleTransition = Folme.useValue("target", Integer.valueOf(0));
    private View mTitleUpView;
    private boolean mTouchScrolling = false;
    private int mTransitionTarget = 0;
    private int mUncollapsePaddingH;
    private final OnClickListener mUpClickListener = new OnClickListener() {
        public void onClick(View view) {
            ActionBarView actionBarView = ActionBarView.this;
            actionBarView.mWindowCallback.onMenuItemSelected(0, actionBarView.mLogoNavItem);
        }
    };
    private boolean mUserTitle;
    Callback mWindowCallback;

    class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        private ExpandedActionViewMenuPresenter() {
        }

        public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            View view = ActionBarView.this.mExpandedActionView;
            if (view instanceof CollapsibleActionView) {
                ((CollapsibleActionView) view).onActionViewCollapsed();
            }
            ActionBarView actionBarView = ActionBarView.this;
            actionBarView.removeView(actionBarView.mExpandedActionView);
            ActionBarView actionBarView2 = ActionBarView.this;
            actionBarView2.removeView(actionBarView2.mExpandedHomeLayout);
            ActionBarView actionBarView3 = ActionBarView.this;
            actionBarView3.mExpandedActionView = null;
            if ((actionBarView3.mDisplayOptions & 2) != 0) {
                ActionBarView.this.mHomeLayout.setVisibility(0);
            }
            if ((ActionBarView.this.mDisplayOptions & 8) != 0) {
                if (ActionBarView.this.mCollapseTitleLayout == null) {
                    ActionBarView.this.initTitle();
                } else {
                    ActionBarView.this.setTitleVisibility(true);
                }
            }
            if (ActionBarView.this.mCollapseTabs != null && ActionBarView.this.mNavigationMode == 2) {
                ActionBarView.this.mCollapseTabs.setVisibility(0);
            }
            if (ActionBarView.this.mExpandTabs != null && ActionBarView.this.mNavigationMode == 2) {
                ActionBarView.this.mExpandTabs.setVisibility(0);
            }
            if (ActionBarView.this.mSecondaryTabs != null && ActionBarView.this.mNavigationMode == 2) {
                ActionBarView.this.mSecondaryTabs.setVisibility(0);
            }
            if (ActionBarView.this.mSpinner != null && ActionBarView.this.mNavigationMode == 1) {
                ActionBarView.this.mSpinner.setVisibility(0);
            }
            if (!(ActionBarView.this.mCustomNavView == null || (ActionBarView.this.mDisplayOptions & 16) == 0)) {
                ActionBarView.this.mCustomNavView.setVisibility(0);
            }
            ActionBarView.this.mExpandedHomeLayout.setIcon(null);
            this.mCurrentExpandedItem = null;
            ActionBarView.this.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            return true;
        }

        public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            ActionBarView.this.mExpandedActionView = menuItemImpl.getActionView();
            ActionBarView.this.initExpandedHomeLayout();
            ActionBarView.this.mExpandedHomeLayout.setIcon(ActionBarView.this.getIcon().getConstantState().newDrawable(ActionBarView.this.getResources()));
            this.mCurrentExpandedItem = menuItemImpl;
            ViewParent parent = ActionBarView.this.mExpandedActionView.getParent();
            ActionBarView actionBarView = ActionBarView.this;
            if (parent != actionBarView) {
                actionBarView.addView(actionBarView.mExpandedActionView);
            }
            ViewParent parent2 = ActionBarView.this.mExpandedHomeLayout.getParent();
            ActionBarView actionBarView2 = ActionBarView.this;
            if (parent2 != actionBarView2) {
                actionBarView2.addView(actionBarView2.mExpandedHomeLayout);
            }
            if (ActionBarView.this.mHomeLayout != null) {
                ActionBarView.this.mHomeLayout.setVisibility(8);
            }
            if (ActionBarView.this.mCollapseTitleLayout != null) {
                ActionBarView.this.setTitleVisibility(false);
            }
            if (ActionBarView.this.mCollapseTabs != null) {
                ActionBarView.this.mCollapseTabs.setVisibility(8);
            }
            if (ActionBarView.this.mExpandTabs != null) {
                ActionBarView.this.mExpandTabs.setVisibility(8);
            }
            if (ActionBarView.this.mSecondaryTabs != null) {
                ActionBarView.this.mSecondaryTabs.setVisibility(8);
            }
            if (ActionBarView.this.mSpinner != null) {
                ActionBarView.this.mSpinner.setVisibility(8);
            }
            if (ActionBarView.this.mCustomNavView != null) {
                ActionBarView.this.mCustomNavView.setVisibility(8);
            }
            ActionBarView.this.requestLayout();
            menuItemImpl.setActionViewExpanded(true);
            View view = ActionBarView.this.mExpandedActionView;
            if (view instanceof CollapsibleActionView) {
                ((CollapsibleActionView) view).onActionViewExpanded();
            }
            return true;
        }

        public boolean flagActionItems() {
            return false;
        }

        public int getId() {
            return 0;
        }

        public MenuView getMenuView(ViewGroup viewGroup) {
            return null;
        }

        public void initForMenu(Context context, MenuBuilder menuBuilder) {
            MenuBuilder menuBuilder2 = this.mMenu;
            if (menuBuilder2 != null) {
                MenuItemImpl menuItemImpl = this.mCurrentExpandedItem;
                if (menuItemImpl != null) {
                    menuBuilder2.collapseItemActionView(menuItemImpl);
                }
            }
            this.mMenu = menuBuilder;
        }

        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        }

        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return false;
        }

        public void setCallback(MenuPresenter.Callback callback) {
        }

        public void updateMenuView(boolean z) {
            if (this.mCurrentExpandedItem != null) {
                MenuBuilder menuBuilder = this.mMenu;
                boolean z2 = false;
                if (menuBuilder != null) {
                    int size = menuBuilder.size();
                    int i = 0;
                    while (true) {
                        if (i >= size) {
                            break;
                        } else if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            z2 = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                if (!z2) {
                    collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }
    }

    class HomeView extends FrameLayout {
        private Drawable mDefaultUpIndicator;
        private ImageView mIconView;
        private int mUpIndicatorRes;
        private ImageView mUpView;
        private int mUpWidth;

        public HomeView(Context context) {
            this(context, null);
        }

        public HomeView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            CharSequence contentDescription = getContentDescription();
            if (!TextUtils.isEmpty(contentDescription)) {
                accessibilityEvent.getText().add(contentDescription);
            }
            return true;
        }

        public int getStartOffset() {
            return 0;
        }

        /* access modifiers changed from: protected */
        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            int i = this.mUpIndicatorRes;
            if (i != 0) {
                setUpIndicator(i);
            }
        }

        /* access modifiers changed from: protected */
        public void onFinishInflate() {
            this.mUpView = (ImageView) findViewById(R.id.up);
            this.mIconView = (ImageView) findViewById(R.id.home);
            this.mDefaultUpIndicator = this.mUpView.getDrawable();
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6 = (i4 - i2) / 2;
            boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
            if (this.mUpView.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) this.mUpView.getLayoutParams();
                int measuredHeight = this.mUpView.getMeasuredHeight();
                int measuredWidth = this.mUpView.getMeasuredWidth();
                int i7 = i6 - (measuredHeight / 2);
                ViewUtils.layoutChildView(this, this.mUpView, 0, i7, measuredWidth, i7 + measuredHeight);
                i5 = layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin;
                if (isLayoutRtl) {
                    i3 -= i5;
                } else {
                    i += i5;
                }
            } else {
                i5 = 0;
            }
            LayoutParams layoutParams2 = (LayoutParams) this.mIconView.getLayoutParams();
            int measuredHeight2 = this.mIconView.getMeasuredHeight();
            int measuredWidth2 = this.mIconView.getMeasuredWidth();
            int max = i5 + Math.max(layoutParams2.getMarginStart(), ((i3 - i) / 2) - (measuredWidth2 / 2));
            int max2 = Math.max(layoutParams2.topMargin, i6 - (measuredHeight2 / 2));
            ViewUtils.layoutChildView(this, this.mIconView, max, max2, max + measuredWidth2, max2 + measuredHeight2);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            measureChildWithMargins(this.mUpView, i, 0, i2, 0);
            LayoutParams layoutParams = (LayoutParams) this.mUpView.getLayoutParams();
            this.mUpWidth = layoutParams.leftMargin + this.mUpView.getMeasuredWidth() + layoutParams.rightMargin;
            int i3 = this.mUpView.getVisibility() == 8 ? 0 : this.mUpWidth;
            int measuredHeight = layoutParams.bottomMargin + layoutParams.topMargin + this.mUpView.getMeasuredHeight();
            measureChildWithMargins(this.mIconView, i, i3, i2, 0);
            LayoutParams layoutParams2 = (LayoutParams) this.mIconView.getLayoutParams();
            int measuredWidth = i3 + layoutParams2.leftMargin + this.mIconView.getMeasuredWidth() + layoutParams2.rightMargin;
            int max = Math.max(measuredHeight, layoutParams2.topMargin + this.mIconView.getMeasuredHeight() + layoutParams2.bottomMargin);
            int mode = MeasureSpec.getMode(i);
            int mode2 = MeasureSpec.getMode(i2);
            int size = MeasureSpec.getSize(i);
            int size2 = MeasureSpec.getSize(i2);
            if (mode == Integer.MIN_VALUE) {
                measuredWidth = Math.min(measuredWidth, size);
            } else if (mode == 1073741824) {
                measuredWidth = size;
            }
            if (mode2 == Integer.MIN_VALUE) {
                max = Math.min(max, size2);
            } else if (mode2 == 1073741824) {
                max = size2;
            }
            setMeasuredDimension(measuredWidth, max);
        }

        public void setIcon(Drawable drawable) {
            this.mIconView.setImageDrawable(drawable);
        }

        public void setUp(boolean z) {
            this.mUpView.setVisibility(z ? 0 : 8);
        }

        public void setUpIndicator(int i) {
            this.mUpIndicatorRes = i;
            this.mUpView.setImageDrawable(i != 0 ? getResources().getDrawable(i) : null);
        }

        public void setUpIndicator(Drawable drawable) {
            ImageView imageView = this.mUpView;
            if (drawable == null) {
                drawable = this.mDefaultUpIndicator;
            }
            imageView.setImageDrawable(drawable);
            this.mUpIndicatorRes = 0;
        }
    }

    class InnerTransitionListener extends TransitionListener {
        private int mNewState;
        private WeakReference mRef;

        public InnerTransitionListener(ActionBarView actionBarView, int i) {
            this.mRef = new WeakReference(actionBarView);
            this.mNewState = i;
        }

        public void onBegin(Object obj) {
            super.onBegin(obj);
            ActionBarView actionBarView = (ActionBarView) this.mRef.get();
            if (actionBarView != null) {
                actionBarView.mTempResizable = actionBarView.isResizable();
                actionBarView.setResizable(true);
                actionBarView.setExpandState(2);
                actionBarView.mCollapseController.setVisibility(4);
            }
        }

        public void onComplete(Object obj) {
            int i;
            CollapseView collapseView;
            super.onComplete(obj);
            ActionBarView actionBarView = (ActionBarView) this.mRef.get();
            if (actionBarView != null) {
                actionBarView.setExpandState(this.mNewState);
                actionBarView.setResizable(actionBarView.mTempResizable);
                if (actionBarView.mInSearchMode) {
                    collapseView = actionBarView.mCollapseController;
                    i = 4;
                } else {
                    collapseView = actionBarView.mCollapseController;
                    i = 0;
                }
                collapseView.setVisibility(i);
            }
        }

        public void onUpdate(Object obj, IIntValueProperty iIntValueProperty, int i, float f, boolean z) {
            super.onUpdate(obj, iIntValueProperty, i, f, z);
            ActionBarView actionBarView = (ActionBarView) this.mRef.get();
            if (actionBarView != null) {
                actionBarView.mPendingHeight = i;
                actionBarView.requestLayout();
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
        int expandedMenuItemId;
        boolean isOverflowOpen;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = parcel.readInt() != 0;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.expandedMenuItemId);
            parcel.writeInt(this.isOverflowOpen ? 1 : 0);
        }
    }

    public ActionBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.mPostScroller = new Scroller(context);
        this.mInActionMode = false;
        this.mInSearchMode = false;
        this.mUncollapsePaddingH = context.getResources().getDimensionPixelOffset(R.dimen.action_bar_title_horizontal_padding);
        this.mCollapseContainer = new FrameLayout(context);
        this.mCollapseContainer.setId(R.id.action_bar_collapse_container);
        this.mCollapseContainer.setForegroundGravity(17);
        this.mCollapseContainer.setVisibility(0);
        this.mMovableContainer = new FrameLayout(context);
        this.mMovableContainer.setId(R.id.action_bar_movable_container);
        this.mMovableContainer.setPaddingRelative(this.mUncollapsePaddingH, context.getResources().getDimensionPixelOffset(R.dimen.action_bar_title_top_padding), this.mUncollapsePaddingH, context.getResources().getDimensionPixelOffset(R.dimen.action_bar_title_bottom_padding));
        this.mMovableContainer.setVisibility(0);
        this.mSecondaryContainer = new SpringBackLayout(context);
        this.mSecondaryContainer.setId(R.id.action_bar_secondary_container);
        this.mSecondaryContainer.setScrollOrientation(1);
        this.mSecondaryContainer.setVisibility(0);
        this.mCollapseController.attach(this.mCollapseContainer);
        this.mMovableController.attach(this.mMovableContainer);
        setBackgroundResource(0);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, miui.R.styleable.ActionBar, 16843470, 0);
        this.mNavigationMode = obtainStyledAttributes.getInt(miui.R.styleable.ActionBar_android_navigationMode, 0);
        this.mTitle = obtainStyledAttributes.getText(miui.R.styleable.ActionBar_android_title);
        this.mSubtitle = obtainStyledAttributes.getText(miui.R.styleable.ActionBar_android_subtitle);
        this.mTitleCenter = obtainStyledAttributes.getBoolean(R.styleable.ActionBar_titleCenter, false);
        this.mLogo = obtainStyledAttributes.getDrawable(miui.R.styleable.ActionBar_android_logo);
        this.mIcon = obtainStyledAttributes.getDrawable(miui.R.styleable.ActionBar_android_icon);
        LayoutInflater from = LayoutInflater.from(context);
        this.mHomeResId = obtainStyledAttributes.getResourceId(miui.R.styleable.ActionBar_android_homeLayout, R.layout.action_bar_home);
        this.mCollapseTitleStyleRes = obtainStyledAttributes.getResourceId(miui.R.styleable.ActionBar_android_titleTextStyle, 0);
        this.mCollapseSubtitleStyleRes = obtainStyledAttributes.getResourceId(miui.R.styleable.ActionBar_android_subtitleTextStyle, 0);
        this.mExpandTitleStyleRes = obtainStyledAttributes.getResourceId(miui.R.styleable.ActionBar_miui_expandTitleTextStyle, 0);
        this.mExpandSubtitleStyleRes = obtainStyledAttributes.getResourceId(miui.R.styleable.ActionBar_miui_expandSubtitleTextStyle, 0);
        this.mProgressBarPadding = obtainStyledAttributes.getDimensionPixelOffset(miui.R.styleable.ActionBar_android_progressBarPadding, 0);
        this.mItemPadding = obtainStyledAttributes.getDimensionPixelOffset(miui.R.styleable.ActionBar_android_itemPadding, 0);
        setDisplayOptions(obtainStyledAttributes.getInt(miui.R.styleable.ActionBar_android_displayOptions, 0));
        int resourceId = obtainStyledAttributes.getResourceId(miui.R.styleable.ActionBar_android_customNavigationLayout, 0);
        if (resourceId != 0) {
            this.mCustomNavView = from.inflate(resourceId, this, false);
            this.mNavigationMode = 0;
        }
        this.mContentHeight = obtainStyledAttributes.getLayoutDimension(miui.R.styleable.ActionBar_android_height, 0);
        obtainStyledAttributes.recycle();
        Context context2 = context;
        ActionMenuItem actionMenuItem = new ActionMenuItem(context2, 0, 16908332, 0, 0, this.mTitle);
        this.mLogoNavItem = actionMenuItem;
        ActionMenuItem actionMenuItem2 = new ActionMenuItem(context2, 0, 16908310, 0, 0, this.mTitle);
        this.mTitleNavItem = actionMenuItem2;
    }

    private void addTabsContainer(ScrollingTabContainerView scrollingTabContainerView, ScrollingTabContainerView scrollingTabContainerView2, ScrollingTabContainerView scrollingTabContainerView3) {
        this.mCollapseTabs = scrollingTabContainerView;
        this.mExpandTabs = scrollingTabContainerView2;
        this.mSecondaryTabs = scrollingTabContainerView3;
        if (this.mCollapseContainer.getChildCount() == 0) {
            this.mCollapseTabs.setVisibility(0);
            this.mCollapseContainer.addView(this.mCollapseTabs);
            this.mMovableContainer.removeAllViews();
            this.mExpandTabs.setVisibility(0);
            this.mMovableContainer.addView(this.mExpandTabs);
            this.mSecondaryContainer.removeAllViews();
        } else if (this.mCollapseContainer.getChildCount() == 1) {
            ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(this.mContext);
            View childAt = this.mCollapseContainer.getChildAt(0);
            if (actionBarPolicy.isTightTitle() || (childAt instanceof ScrollingTabContainerView)) {
                this.mCollapseContainer.removeAllViews();
                this.mCollapseContainer.addView(this.mCollapseTabs);
                this.mMovableContainer.removeAllViews();
                this.mMovableContainer.addView(this.mExpandTabs);
            } else {
                this.mSecondaryContainer.removeAllViews();
                this.mSecondaryContainer.addView(this.mSecondaryTabs);
                this.mSecondaryContainer.setTarget(this.mSecondaryTabs);
                this.mSecondaryController.attach(this.mSecondaryTabs);
            }
        }
        ViewGroup.LayoutParams layoutParams = this.mCollapseTabs.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = -2;
            layoutParams.height = -1;
        }
        ViewGroup.LayoutParams layoutParams2 = this.mExpandTabs.getLayoutParams();
        if (layoutParams2 != null) {
            layoutParams2.width = -2;
            layoutParams2.height = -1;
        }
        ViewGroup.LayoutParams layoutParams3 = this.mSecondaryTabs.getLayoutParams();
        if (layoutParams3 != null) {
            layoutParams3.width = -2;
            layoutParams3.height = -1;
        }
        updateTightTitle();
        updateSandwichView();
    }

    private void addTitleView(View view, View view2) {
        if (freeCollapseContainer(false)) {
            this.mCollapseContainer.addView(view);
            this.mMovableContainer.addView(view2);
        }
    }

    private void addedCustomView() {
        FrameLayout frameLayout = (FrameLayout) this.mCustomNavView.findViewById(R.id.action_bar_expand_container);
        TextView customTitleView = getCustomTitleView(frameLayout);
        if (customTitleView != null) {
            freeCollapseContainer(true);
            this.mCollapseCustomContainer = frameLayout;
            this.mCollapseController.attach(this.mCollapseCustomContainer);
            this.mExpandTitleView.setText(customTitleView.getText());
            this.mExpandTitleView.setVisibility(0);
            this.mExpandTitleLayout.setVisibility(0);
            this.mExpandSubtitleView.setVisibility(8);
            this.mMovableContainer.addView(this.mExpandTitleLayout);
            customTitleView.addTextChangedListener(this.mCustomTitleWatcher);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00ec, code lost:
        if (r13 <= 0) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00ef, code lost:
        if (r13 >= 0) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00f7, code lost:
        r12.mSecondaryController.setTransparent(0, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void animateLayoutWithProcess(float f) {
        float f2 = 1.0f;
        float min = 1.0f - Math.min(1.0f, 3.0f * f);
        int i = this.mInnerExpandState;
        Integer valueOf = Integer.valueOf(0);
        if (i == 2) {
            if (min > 0.0f) {
                if (!this.mIsTitleShowing) {
                    this.mIsTitleShowing = true;
                    this.mIsTitleHidding = false;
                    this.mCollapseController.animTo(0.0f, 0, 20, this.mCollapseAnimHideConfig);
                    String str = "expand";
                    this.mTitleTransition.setup(Integer.valueOf(1)).setTo(str, Integer.valueOf(this.mTransitionTarget)).to(str, Integer.valueOf(20), this.mHideProcessConfig);
                }
            } else if (!this.mIsTitleHidding) {
                this.mIsTitleHidding = true;
                this.mIsTitleShowing = false;
                this.mCollapseController.animTo(this.mInActionMode ? 0.0f : 1.0f, 0, 0, this.mCollapseAnimShowConfig);
                String str2 = "collapse";
                this.mTitleTransition.setup(valueOf).setTo(str2, Integer.valueOf(this.mTransitionTarget)).to(str2, valueOf, this.mShowProcessConfig);
            }
            if (this.mInActionMode) {
                min = 0.0f;
            }
            this.mMovableController.animTo(min, 0, 0, this.mMovableAnimConfig);
            FrameLayout frameLayout = this.mMovableContainer;
            if (frameLayout != null) {
                frameLayout.setVisibility(f < 1.0f ? 0 : 4);
            }
        } else if (i == 1) {
            this.mCollapseController.setAnimFrom(0.0f, 0, 20);
            if (this.mInActionMode) {
                f2 = 0.0f;
            }
            this.mMovableController.setAnimFrom(f2, 0, 0);
            this.mTransitionTarget = 20;
        } else if (i == 0) {
            this.mCollapseController.setAnimFrom(1.0f, 0, 0);
            this.mMovableController.setAnimFrom(0.0f, 0, 0);
            this.mTransitionTarget = 0;
        }
        int width = (int) (((float) (((getWidth() - this.mSecondaryContainer.getMeasuredWidth()) / 2) - this.mUncollapsePaddingH)) * f);
        if (ViewUtils.isLayoutRtl(this)) {
            width = -width;
        }
        this.mSecondaryController.setTransparent(width, 0);
    }

    private boolean canTitleBeShown() {
        TextView textView = this.mCollapseTitleView;
        if (textView == null || this.mTitle == null) {
            return false;
        }
        return (!isResizable() && getExpandState() == 0) || textView.getPaint().measureText(this.mTitle.toString()) <= ((float) this.mCollapseTitleView.getMeasuredWidth());
    }

    private int computeTitleCenterLayoutStart(View view) {
        int width = (getWidth() - view.getMeasuredWidth()) / 2;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int i = 0;
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            i = 0 + ((LinearLayout.LayoutParams) layoutParams).getMarginStart();
        }
        return width - i;
    }

    private void configPresenters(MenuBuilder menuBuilder) {
        if (menuBuilder != null) {
            menuBuilder.addMenuPresenter(this.mActionMenuPresenter);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter);
        } else {
            this.mActionMenuPresenter.initForMenu(this.mContext, null);
            this.mExpandedMenuPresenter.initForMenu(this.mContext, null);
        }
        this.mActionMenuPresenter.updateMenuView(true);
        this.mExpandedMenuPresenter.updateMenuView(true);
    }

    private boolean freeCollapseContainer(boolean z) {
        if (this.mCollapseContainer.getChildCount() == 1) {
            ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(this.mContext);
            View childAt = this.mCollapseContainer.getChildAt(0);
            if (!(childAt instanceof ScrollingTabContainerView)) {
                childAt.setVisibility(8);
            } else if (!z && actionBarPolicy.isTightTitle()) {
                return false;
            } else {
                this.mCollapseContainer.removeAllViews();
                this.mSecondaryContainer.removeAllViews();
                this.mSecondaryContainer.addView(this.mSecondaryTabs);
                this.mSecondaryContainer.setTarget(this.mSecondaryTabs);
                this.mSecondaryController.attach(this.mSecondaryTabs);
            }
        }
        this.mMovableContainer.removeAllViews();
        removeView(this.mCollapseContainer);
        return true;
    }

    private ProgressBar getCircularProgressBar() {
        ProgressBar progressBar = this.mIndeterminateProgressView;
        if (progressBar != null) {
            progressBar.setVisibility(4);
        }
        return progressBar;
    }

    private TextView getCustomTitleView(View view) {
        if (view != null) {
            return (TextView) view.findViewById(16908310);
        }
        return null;
    }

    private ProgressBar getHorizontalProgressBar() {
        ProgressBar progressBar = this.mProgressView;
        if (progressBar != null) {
            progressBar.setVisibility(4);
        }
        return progressBar;
    }

    /* access modifiers changed from: private */
    public Drawable getIcon() {
        if ((this.mIconLogoInitIndicator & 1) != 1) {
            Context context = this.mContext;
            if (context instanceof Activity) {
                try {
                    this.mIcon = context.getPackageManager().getActivityIcon(((Activity) this.mContext).getComponentName());
                } catch (NameNotFoundException e) {
                    Log.e(TAG, "Activity component name not found!", e);
                }
            }
            if (this.mIcon == null) {
                this.mIcon = this.mContext.getApplicationInfo().loadIcon(this.mContext.getPackageManager());
            }
            this.mIconLogoInitIndicator |= 1;
        }
        return this.mIcon;
    }

    private Drawable getLogo() {
        if ((this.mIconLogoInitIndicator & 2) != 2) {
            if (VERSION.SDK_INT >= 9) {
                Context context = this.mContext;
                if (context instanceof Activity) {
                    try {
                        this.mLogo = context.getPackageManager().getActivityLogo(((Activity) this.mContext).getComponentName());
                    } catch (NameNotFoundException e) {
                        Log.e(TAG, "Activity component name not found!", e);
                    }
                }
                if (this.mLogo == null) {
                    this.mLogo = this.mContext.getApplicationInfo().loadLogo(this.mContext.getPackageManager());
                }
            }
            this.mIconLogoInitIndicator |= 2;
        }
        return this.mLogo;
    }

    private void hideProgressBars(ProgressBar progressBar, ProgressBar progressBar2) {
        if (progressBar2 != null && progressBar2.getVisibility() == 0) {
            progressBar2.setVisibility(4);
        }
        if (progressBar != null && progressBar.getVisibility() == 0) {
            progressBar.setVisibility(4);
        }
    }

    /* access modifiers changed from: private */
    public void initExpandedHomeLayout() {
        if (this.mExpandedHomeLayout == null) {
            this.mExpandedHomeLayout = (HomeView) LayoutInflater.from(this.mContext).inflate(this.mHomeResId, this, false);
            this.mExpandedHomeLayout.setUp(true);
            this.mExpandedHomeLayout.setOnClickListener(this.mExpandedActionViewUpListener);
        }
    }

    private void initHomeLayout() {
        if (this.mHomeLayout == null) {
            this.mHomeLayout = (HomeView) LayoutInflater.from(this.mContext).inflate(this.mHomeResId, this, false);
            this.mHomeLayout.setOnClickListener(this.mUpClickListener);
            this.mHomeLayout.setClickable(true);
            this.mHomeLayout.setFocusable(true);
            int i = this.mHomeAsUpIndicatorResId;
            if (i != 0) {
                this.mHomeLayout.setUpIndicator(i);
                this.mHomeAsUpIndicatorResId = 0;
            }
            Drawable drawable = this.mHomeAsUpIndicatorDrawable;
            if (drawable != null) {
                this.mHomeLayout.setUpIndicator(drawable);
                this.mHomeAsUpIndicatorDrawable = null;
            }
            addView(this.mHomeLayout);
        }
    }

    /* access modifiers changed from: private */
    public void initTitle() {
        if (this.mTitleUpView == null) {
            this.mTitleUpView = LayoutInflater.from(getContext()).inflate(R.layout.action_bar_title_up, this, false);
            this.mTitleUpView.setOnClickListener(this.mUpClickListener);
        }
        addView(this.mTitleUpView);
        if (this.mCollapseTitleLayout == null) {
            LayoutInflater from = LayoutInflater.from(getContext());
            this.mCollapseTitleLayout = (LinearLayout) from.inflate(R.layout.action_bar_title_item, null, false);
            this.mExpandTitleLayout = (LinearLayout) from.inflate(R.layout.action_bar_title_expand_item, null, false);
            this.mCollapseTitleView = (TextView) this.mCollapseTitleLayout.findViewById(R.id.action_bar_title);
            this.mExpandTitleView = (TextView) this.mExpandTitleLayout.findViewById(R.id.action_bar_title);
            this.mCollapseSubtitleView = (TextView) this.mCollapseTitleLayout.findViewById(R.id.action_bar_subtitle);
            this.mExpandSubtitleView = (TextView) this.mExpandTitleLayout.findViewById(R.id.action_bar_subtitle);
            this.mCollapseTitleLayout.setOnClickListener(this.mTitleClickListener);
            this.mExpandTitleLayout.setOnClickListener(this.mTitleClickListener);
            int i = this.mCollapseTitleStyleRes;
            if (i != 0) {
                this.mCollapseTitleView.setTextAppearance(this.mContext, i);
            }
            int i2 = this.mExpandTitleStyleRes;
            if (i2 != 0) {
                this.mExpandTitleView.setTextAppearance(this.mContext, i2);
            }
            CharSequence charSequence = this.mTitle;
            if (charSequence != null) {
                this.mCollapseTitleView.setText(charSequence);
                this.mExpandTitleView.setText(this.mTitle);
            }
            int i3 = this.mCollapseSubtitleStyleRes;
            if (i3 != 0) {
                this.mCollapseSubtitleView.setTextAppearance(this.mContext, i3);
            }
            int i4 = this.mExpandSubtitleStyleRes;
            if (i4 != 0) {
                this.mExpandSubtitleView.setTextAppearance(this.mContext, i4);
            }
            CharSequence charSequence2 = this.mSubtitle;
            if (charSequence2 != null) {
                this.mCollapseSubtitleView.setText(charSequence2);
                this.mCollapseSubtitleView.setVisibility(0);
                this.mExpandSubtitleView.setText(this.mSubtitle);
                this.mExpandSubtitleView.setVisibility(0);
            }
            int i5 = 4;
            boolean z = true;
            boolean z2 = (this.mDisplayOptions & 4) != 0;
            boolean z3 = (this.mDisplayOptions & 2) != 0;
            View view = this.mTitleUpView;
            if (z3) {
                i5 = 8;
            } else if (z2) {
                i5 = 0;
            }
            view.setVisibility(i5);
            View view2 = this.mTitleUpView;
            boolean z4 = z2 && !z3;
            view2.setEnabled(z4);
            LinearLayout linearLayout = this.mCollapseTitleLayout;
            boolean z5 = z2 && !z3;
            linearLayout.setEnabled(z5);
            LinearLayout linearLayout2 = this.mExpandTitleLayout;
            if (!z2 || z3) {
                z = false;
            }
            linearLayout2.setEnabled(z);
            updateTightTitle();
        }
        addTitleView(this.mCollapseTitleLayout, this.mExpandTitleLayout);
        post(new Runnable() {
            public void run() {
                if (ActionBarView.this.mCollapseTitleLayout != null) {
                    Rect rect = new Rect();
                    ActionBarView.this.mCollapseTitleLayout.getHitRect(rect);
                    rect.left -= AttributeResolver.resolveDimensionPixelSize(ActionBarView.this.getContext(), miui.R.attr.actionBarPaddingStart);
                    ActionBarView actionBarView = ActionBarView.this;
                    actionBarView.setTouchDelegate(new TouchDelegate(rect, actionBarView.mCollapseTitleLayout));
                }
            }
        });
        if (this.mExpandedActionView != null || (TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle))) {
            setTitleVisibility(false);
        }
        updateSandwichView();
    }

    private boolean isShowTitle() {
        return (this.mCollapseContainer.getChildCount() == 1 && this.mCollapseContainer.getChildAt(0).getVisibility() != 8) || !(this.mCustomNavView == null || this.mCollapseCustomContainer == null);
    }

    private boolean isSimpleCustomNavView() {
        View view = this.mCustomNavView;
        if (view == null || view.getVisibility() != 0) {
            return true;
        }
        ViewGroup.LayoutParams layoutParams = this.mCustomNavView.getLayoutParams();
        ActionBar.LayoutParams layoutParams2 = layoutParams instanceof ActionBar.LayoutParams ? (ActionBar.LayoutParams) layoutParams : null;
        return layoutParams2 != null && normalizeHorizontalGravity(layoutParams2.gravity, ViewUtils.isLayoutRtl(this)) == 8388613;
    }

    private boolean isTitleCenter() {
        if (this.mTitleCenter && isSimpleCustomNavView()) {
            HomeView homeView = this.mHomeLayout;
            if ((homeView == null || homeView.getVisibility() == 8) && !isTightTitleWithEmbeddedTabs()) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        return androidx.core.view.GravityCompat.START;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0012, code lost:
        if (r4 != false) goto L_0x0014;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001b, code lost:
        if (r4 != false) goto L_0x0016;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int normalizeHorizontalGravity(int i, boolean z) {
        int i2 = 8388615 & i;
        if ((8388608 & i2) != 0) {
            return i2;
        }
        if (i2 != 3) {
            if (i2 != 5) {
                return i2;
            }
        }
        return GravityCompat.END;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:106:0x01d9, code lost:
        if (r4 == -1) goto L_0x01db;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0185, code lost:
        if (r11 != null) goto L_0x0189;
     */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x01c9  */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01d9  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x01e6  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x01ef  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x01f9  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x01ff  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0221  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x023c  */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x0243  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0246  */
    /* JADX WARNING: Removed duplicated region for block: B:134:0x024c  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0259  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x017e  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0199  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x019c  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x019f  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x01a2  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01ab  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x01ba  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onLayoutCollapseViews(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int paddingEnd;
        View view;
        View view2;
        ProgressBar progressBar;
        ActionBar.LayoutParams layoutParams;
        int i6;
        int i7;
        int i8;
        int i9;
        int normalizeHorizontalGravity;
        int i10;
        int i11;
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop2 = (i4 - getPaddingTop()) - getPaddingBottom();
        if (paddingTop2 > 0) {
            HomeView homeView = this.mExpandedActionView != null ? this.mExpandedHomeLayout : this.mHomeLayout;
            if (homeView == null || homeView.getVisibility() == 8) {
                View view3 = this.mStartView;
                if (view3 != null) {
                    i11 = positionChild(view3, paddingStart, paddingTop, paddingTop2);
                }
                if (this.mExpandedActionView == null) {
                    boolean isShowTitle = isShowTitle();
                    if (isShowTitle) {
                        View view4 = this.mTitleUpView;
                        if (view4 != null) {
                            i5 += positionChild(view4, i5, paddingTop, paddingTop2);
                        }
                        int i12 = i5;
                        if (isTitleCenter()) {
                            FrameLayout frameLayout = this.mCollapseContainer;
                            int computeTitleCenterLayoutStart = (frameLayout == null || frameLayout.getVisibility() == 8) ? i12 : computeTitleCenterLayoutStart(this.mCollapseContainer);
                            this.mCollapseTitleShowable = canTitleBeShown();
                            updateTightTitle();
                            int measuredWidth = computeTitleCenterLayoutStart + this.mCollapseContainer.getMeasuredWidth();
                            int measuredHeight = this.mCollapseContainer.getMeasuredHeight();
                            int i13 = paddingTop + ((paddingTop2 - measuredHeight) / 2);
                            int i14 = i12;
                            ViewUtils.layoutChildView(this, this.mCollapseContainer, computeTitleCenterLayoutStart, i13, measuredWidth, i13 + measuredHeight);
                            i5 = i14;
                        } else {
                            int i15 = i12;
                            i5 = positionChild(this.mCollapseContainer, i15, paddingTop, paddingTop2) + i15;
                        }
                    }
                    int i16 = this.mNavigationMode;
                    if (i16 != 0) {
                        if (i16 != 1) {
                            if (i16 == 2) {
                                ScrollingTabContainerView scrollingTabContainerView = (this.mCollapseContainer.getChildCount() != 1 || !(this.mCollapseContainer.getChildAt(0) instanceof ScrollingTabContainerView)) ? null : (ScrollingTabContainerView) this.mCollapseContainer.getChildAt(0);
                                if (scrollingTabContainerView != null) {
                                    int measuredWidth2 = scrollingTabContainerView.getMeasuredWidth();
                                    int measuredHeight2 = scrollingTabContainerView.getMeasuredHeight();
                                    int width = (getWidth() - measuredWidth2) / 2;
                                    int width2 = (getWidth() + measuredWidth2) / 2;
                                    if (isShowTitle) {
                                        int i17 = this.mItemPadding;
                                        width += i17;
                                        width2 -= i17;
                                    }
                                    i5 = width2;
                                    this.mCollapseContainer.layout(isLayoutRtl ? getWidth() - i5 : width, paddingTop, isLayoutRtl ? getWidth() - width : i5, measuredHeight2 + paddingTop);
                                }
                            }
                        } else if (this.mListNavLayout != null) {
                            if (isShowTitle) {
                                i10 = i5 + this.mItemPadding;
                            }
                            i5 = i10 + positionChild(this.mListNavLayout, i10, paddingTop, paddingTop2) + this.mItemPadding;
                        }
                    }
                }
                paddingEnd = (i3 - i) - getPaddingEnd();
                ActionMenuView actionMenuView = this.mMenuView;
                if (actionMenuView != null && actionMenuView.getParent() == this) {
                    positionChildInverse(this.mMenuView, paddingEnd, paddingTop, paddingTop2);
                    paddingEnd -= this.mMenuView.getMeasuredWidth();
                }
                view = this.mEndView;
                if (view != null) {
                    positionChildInverse(view, paddingEnd, paddingTop, paddingTop2);
                    paddingEnd -= this.mEndView.getMeasuredWidth();
                }
                ProgressBar progressBar2 = this.mIndeterminateProgressView;
                if (!(progressBar2 == null || progressBar2.getVisibility() == 8)) {
                    positionChildInverse(this.mIndeterminateProgressView, paddingEnd - this.mProgressBarPadding, paddingTop, paddingTop2);
                    paddingEnd -= this.mIndeterminateProgressView.getMeasuredWidth() - (this.mProgressBarPadding * 2);
                }
                View view5 = this.mImmersionView;
                if (!(view5 == null || view5.getVisibility() == 8)) {
                    positionChildInverse(this.mImmersionView, paddingEnd, paddingTop, paddingTop2);
                    paddingEnd -= this.mImmersionView.getMeasuredWidth();
                }
                view2 = this.mExpandedActionView;
                if (view2 == null) {
                    if ((this.mDisplayOptions & 16) != 0) {
                        view2 = this.mCustomNavView;
                    }
                    view2 = null;
                }
                if (!(view2 == null || view2.getVisibility() == 8)) {
                    ViewGroup.LayoutParams layoutParams2 = view2.getLayoutParams();
                    layoutParams = !(layoutParams2 instanceof ActionBar.LayoutParams) ? (ActionBar.LayoutParams) layoutParams2 : null;
                    i6 = layoutParams == null ? layoutParams.gravity : DEFAULT_CUSTOM_GRAVITY;
                    int measuredWidth3 = view2.getMeasuredWidth();
                    if (layoutParams == null) {
                        i5 += layoutParams.getMarginStart();
                        paddingEnd -= layoutParams.getMarginEnd();
                        i7 = layoutParams.topMargin;
                        i8 = layoutParams.bottomMargin;
                    } else {
                        i8 = 0;
                        i7 = 0;
                    }
                    i9 = 8388615 & i6;
                    if (i9 != 1) {
                        int width3 = (getWidth() - measuredWidth3) / 2;
                        if (width3 >= i5) {
                            if (width3 + measuredWidth3 > paddingEnd) {
                                i9 = 8388613;
                            }
                            int paddingStart2 = getPaddingStart();
                            normalizeHorizontalGravity = normalizeHorizontalGravity(i9, isLayoutRtl);
                            if (normalizeHorizontalGravity == 1) {
                                i5 = (getWidth() - measuredWidth3) / 2;
                            } else if (normalizeHorizontalGravity != 8388611) {
                                i5 = normalizeHorizontalGravity != 8388613 ? paddingStart2 : paddingEnd - measuredWidth3;
                            }
                            int i18 = i6 & 112;
                            if (i6 == -1) {
                                i18 = 16;
                            }
                            int i19 = i18 != 16 ? i18 != 48 ? i18 != 80 ? 0 : ((getHeight() - getPaddingBottom()) - view2.getMeasuredHeight()) - i8 : getPaddingTop() + i7 : (((getHeight() - getPaddingBottom()) - getPaddingTop()) - view2.getMeasuredHeight()) / 2;
                            int measuredWidth4 = view2.getMeasuredWidth();
                            view2.layout(isLayoutRtl ? (getWidth() - i5) - measuredWidth4 : i5, i19, isLayoutRtl ? getWidth() - i5 : measuredWidth4 + i5, view2.getMeasuredHeight() + i19);
                        }
                    }
                    i9 = 8388611;
                    int paddingStart22 = getPaddingStart();
                    normalizeHorizontalGravity = normalizeHorizontalGravity(i9, isLayoutRtl);
                    if (normalizeHorizontalGravity == 1) {
                    }
                    int i182 = i6 & 112;
                    if (i6 == -1) {
                    }
                    if (i182 != 16) {
                    }
                    int measuredWidth42 = view2.getMeasuredWidth();
                    view2.layout(isLayoutRtl ? (getWidth() - i5) - measuredWidth42 : i5, i19, isLayoutRtl ? getWidth() - i5 : measuredWidth42 + i5, view2.getMeasuredHeight() + i19);
                }
                progressBar = this.mProgressView;
                if (progressBar != null) {
                    progressBar.bringToFront();
                    int measuredHeight3 = this.mProgressView.getMeasuredHeight() / 2;
                    ProgressBar progressBar3 = this.mProgressView;
                    int i20 = this.mProgressBarPadding;
                    progressBar3.layout(i20, -measuredHeight3, progressBar3.getMeasuredWidth() + i20, measuredHeight3);
                }
            }
            int startOffset = homeView.getStartOffset();
            i11 = positionChild(homeView, paddingStart + startOffset, paddingTop, paddingTop2) + startOffset;
            paddingStart += i11;
            if (this.mExpandedActionView == null) {
            }
            paddingEnd = (i3 - i) - getPaddingEnd();
            ActionMenuView actionMenuView2 = this.mMenuView;
            positionChildInverse(this.mMenuView, paddingEnd, paddingTop, paddingTop2);
            paddingEnd -= this.mMenuView.getMeasuredWidth();
            view = this.mEndView;
            if (view != null) {
            }
            ProgressBar progressBar22 = this.mIndeterminateProgressView;
            positionChildInverse(this.mIndeterminateProgressView, paddingEnd - this.mProgressBarPadding, paddingTop, paddingTop2);
            paddingEnd -= this.mIndeterminateProgressView.getMeasuredWidth() - (this.mProgressBarPadding * 2);
            View view52 = this.mImmersionView;
            positionChildInverse(this.mImmersionView, paddingEnd, paddingTop, paddingTop2);
            paddingEnd -= this.mImmersionView.getMeasuredWidth();
            view2 = this.mExpandedActionView;
            if (view2 == null) {
            }
            ViewGroup.LayoutParams layoutParams22 = view2.getLayoutParams();
            if (!(layoutParams22 instanceof ActionBar.LayoutParams)) {
            }
            if (layoutParams == null) {
            }
            int measuredWidth32 = view2.getMeasuredWidth();
            if (layoutParams == null) {
            }
            i9 = 8388615 & i6;
            if (i9 != 1) {
            }
            i9 = 8388611;
            int paddingStart222 = getPaddingStart();
            normalizeHorizontalGravity = normalizeHorizontalGravity(i9, isLayoutRtl);
            if (normalizeHorizontalGravity == 1) {
            }
            int i1822 = i6 & 112;
            if (i6 == -1) {
            }
            if (i1822 != 16) {
            }
            int measuredWidth422 = view2.getMeasuredWidth();
            view2.layout(isLayoutRtl ? (getWidth() - i5) - measuredWidth422 : i5, i19, isLayoutRtl ? getWidth() - i5 : measuredWidth422 + i5, view2.getMeasuredHeight() + i19);
            progressBar = this.mProgressView;
            if (progressBar != null) {
            }
        }
    }

    private void setTitleImpl(CharSequence charSequence) {
        this.mTitle = charSequence;
        TextView textView = this.mCollapseTitleView;
        if (textView != null) {
            textView.setText(charSequence);
            this.mExpandTitleView.setText(charSequence);
            boolean z = this.mExpandedActionView == null && (this.mDisplayOptions & 8) != 0 && (!TextUtils.isEmpty(this.mTitle) || !TextUtils.isEmpty(this.mSubtitle));
            setTitleVisibility(z);
            if (z) {
                this.mCollapseTitleView.setVisibility(0);
                this.mCollapseTitleShowable = true;
            }
        }
        ActionMenuItem actionMenuItem = this.mLogoNavItem;
        if (actionMenuItem != null) {
            actionMenuItem.setTitle(charSequence);
        }
        ActionMenuItem actionMenuItem2 = this.mTitleNavItem;
        if (actionMenuItem2 != null) {
            actionMenuItem2.setTitle(charSequence);
        }
    }

    /* access modifiers changed from: private */
    public void setTitleVisibility(boolean z) {
        LinearLayout linearLayout = this.mCollapseTitleLayout;
        if (linearLayout != null) {
            linearLayout.setVisibility(z ? 0 : 8);
        }
        LinearLayout linearLayout2 = this.mExpandTitleLayout;
        if (linearLayout2 != null) {
            linearLayout2.setVisibility(z ? 0 : 8);
        }
        View view = this.mTitleUpView;
        if (view != null) {
            if (z) {
                int i = 4;
                boolean z2 = true;
                boolean z3 = (this.mDisplayOptions & 4) != 0;
                if ((this.mDisplayOptions & 2) == 0) {
                    z2 = false;
                }
                View view2 = this.mTitleUpView;
                if (z2) {
                    i = 8;
                } else if (z3) {
                    i = 0;
                }
                view2.setVisibility(i);
            } else {
                view.setVisibility(8);
            }
        }
        updateSandwichView();
    }

    private void showProgressBars(ProgressBar progressBar, ProgressBar progressBar2) {
        if (progressBar2 != null && progressBar2.getVisibility() == 4) {
            progressBar2.setVisibility(0);
        }
        if (progressBar != null && progressBar.getProgress() < 10000) {
            progressBar.setVisibility(0);
        }
    }

    private void updateProgressBars(int i) {
        ProgressBar circularProgressBar = getCircularProgressBar();
        ProgressBar horizontalProgressBar = getHorizontalProgressBar();
        if (i == -1) {
            if (horizontalProgressBar != null) {
                int i2 = (horizontalProgressBar.isIndeterminate() || horizontalProgressBar.getProgress() < 10000) ? 0 : 4;
                horizontalProgressBar.setVisibility(i2);
            }
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(0);
            }
        } else if (i == -2) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setVisibility(8);
            }
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(8);
            }
        } else if (i == -3) {
            horizontalProgressBar.setIndeterminate(true);
        } else if (i == -4) {
            horizontalProgressBar.setIndeterminate(false);
        } else if (i >= 0 && i <= 10000) {
            horizontalProgressBar.setProgress(i + 0);
            if (i < 10000) {
                showProgressBars(horizontalProgressBar, circularProgressBar);
            } else {
                hideProgressBars(horizontalProgressBar, circularProgressBar);
            }
        }
    }

    private void updateSandwichView() {
        updateSingleContainer(this.mCollapseContainer);
        updateSingleContainer(this.mMovableContainer);
        updateSingleContainer(this.mSecondaryContainer);
    }

    private void updateSingleContainer(ViewGroup viewGroup) {
        removeView(viewGroup);
        if (viewGroup.getChildCount() == 1 && viewGroup.getChildAt(0).getVisibility() != 8) {
            addView(viewGroup, new LayoutParams(-1, -2));
        }
    }

    private void updateTightTitle() {
        boolean z = TextUtils.isEmpty(this.mTitle) && isTightTitleWithEmbeddedTabs() && ActionBarPolicy.get(this.mContext).isTightTitle();
        int i = 8;
        int i2 = (z || !this.mCollapseTitleShowable) ? 8 : 0;
        TextView textView = this.mCollapseTitleView;
        if (textView != null) {
            textView.setVisibility(i2);
        }
        if (!z && !TextUtils.isEmpty(this.mSubtitle) && this.mCollapseTitleShowable) {
            i = 0;
        }
        TextView textView2 = this.mCollapseSubtitleView;
        if (textView2 != null) {
            textView2.setVisibility(i);
        }
    }

    private void updateTitleCenter() {
        boolean isTitleCenter = isTitleCenter();
        TextView textView = this.mCollapseTitleView;
        int i = 1;
        if (textView != null) {
            ViewGroup viewGroup = (ViewGroup) textView.getParent();
            if (viewGroup instanceof LinearLayout) {
                ((LinearLayout) viewGroup).setGravity((isTitleCenter ? 1 : 8388611) | 16);
            }
            this.mCollapseTitleView.setGravity((isTitleCenter ? 1 : 8388611) | 16);
            this.mCollapseTitleView.setEllipsize(TruncateAt.END);
        }
        TextView textView2 = this.mCollapseSubtitleView;
        if (textView2 != null) {
            if (!isTitleCenter) {
                i = 8388611;
            }
            textView2.setGravity(i | 16);
            this.mCollapseSubtitleView.setEllipsize(TruncateAt.END);
        }
    }

    public /* bridge */ /* synthetic */ void animateToVisibility(int i) {
        super.animateToVisibility(i);
    }

    public void collapseActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl menuItemImpl = expandedActionViewMenuPresenter == null ? null : expandedActionViewMenuPresenter.mCurrentExpandedItem;
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    /* access modifiers changed from: protected */
    public ActionMenuPresenter createActionMenuPresenter(MenuPresenter.Callback callback) {
        ActionMenuPresenter actionMenuPresenter = new ActionMenuPresenter(this.mContext, R.layout.action_menu_layout, R.layout.action_menu_item_layout, R.layout.action_bar_expanded_menu_layout, R.layout.action_bar_list_menu_item_layout);
        actionMenuPresenter.setCallback(callback);
        actionMenuPresenter.setId(R.id.action_menu_presenter);
        return actionMenuPresenter;
    }

    /* access modifiers changed from: protected */
    public ExpandedActionViewMenuPresenter createExpandedActionViewMenuPresenter() {
        return new ExpandedActionViewMenuPresenter();
    }

    public /* bridge */ /* synthetic */ void dismissPopupMenus() {
        super.dismissPopupMenus();
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ActionBar.LayoutParams(DEFAULT_CUSTOM_GRAVITY);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ActionBar.LayoutParams(getContext(), attributeSet);
    }

    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams == null ? generateDefaultLayoutParams() : layoutParams;
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

    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    public View getCustomNavigationView() {
        return this.mCustomNavView;
    }

    public int getDisplayOptions() {
        return this.mDisplayOptions;
    }

    public SpinnerAdapter getDropdownAdapter() {
        return this.mSpinnerAdapter;
    }

    public int getDropdownSelectedPosition() {
        return this.mSpinner.getSelectedItemPosition();
    }

    public View getEndView() {
        return this.mEndView;
    }

    public /* bridge */ /* synthetic */ int getExpandState() {
        return super.getExpandState();
    }

    public /* bridge */ /* synthetic */ ActionMenuView getMenuView() {
        return super.getMenuView();
    }

    public int getNavigationMode() {
        return this.mNavigationMode;
    }

    public int getSplitActionBarHeight(boolean z) {
        if (z) {
            ActionBarContainer actionBarContainer = this.mSplitView;
            if (actionBarContainer != null) {
                return actionBarContainer.getCollapsedHeight();
            }
            return 0;
        } else if (this.mSplitActionBar) {
            return this.mSplitView.getHeight();
        } else {
            return 0;
        }
    }

    public View getStartView() {
        return this.mStartView;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public boolean hasExpandedActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        return (expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null) ? false : true;
    }

    public boolean hideImmersionMore() {
        View view = this.mImmersionView;
        if (view == null) {
            return false;
        }
        view.setVisibility(8);
        return true;
    }

    public /* bridge */ /* synthetic */ boolean hideOverflowMenu() {
        return super.hideOverflowMenu();
    }

    public void initImmersionMore(int i, final ActionBarDelegateImpl actionBarDelegateImpl) {
        String str = TAG;
        if (i <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Try to initialize invalid layout for immersion more button: ");
            sb.append(i);
            Log.w(str, sb.toString());
            return;
        }
        int i2 = this.mDisplayOptions;
        if ((i2 & 16) != 0) {
            Log.d(str, "Don't show immersion menu button for custom action bar");
        } else if (i2 == 0) {
            Log.d(str, "Don't show immersion menu button for null display option");
        } else {
            this.mImmersionView = LayoutInflater.from(getContext()).inflate(i, this, false);
            addView(this.mImmersionView);
            final View findViewById = this.mImmersionView.findViewById(R.id.more);
            if (findViewById != null) {
                findViewById.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        actionBarDelegateImpl.showImmersionMenu(findViewById, ActionBarView.this);
                    }
                });
            }
        }
    }

    public void initIndeterminateProgress() {
        this.mIndeterminateProgressView = new ProgressBar(this.mContext, null, miui.R.attr.actionBarIndeterminateProgressStyle);
        this.mIndeterminateProgressView.setId(R.id.progress_circular);
        this.mIndeterminateProgressView.setVisibility(8);
        this.mIndeterminateProgressView.setIndeterminate(true);
        addView(this.mIndeterminateProgressView);
    }

    public void initProgress() {
        this.mProgressView = new ProgressBar(this.mContext, null, miui.R.attr.actionBarProgressStyle);
        this.mProgressView.setId(R.id.progress_horizontal);
        this.mProgressView.setMax(10000);
        this.mProgressView.setVisibility(8);
        addView(this.mProgressView);
    }

    public boolean isCollapsed() {
        return this.mIsCollapsed;
    }

    public /* bridge */ /* synthetic */ boolean isOverflowMenuShowing() {
        return super.isOverflowMenuShowing();
    }

    public /* bridge */ /* synthetic */ boolean isOverflowReserved() {
        return super.isOverflowReserved();
    }

    public /* bridge */ /* synthetic */ boolean isResizable() {
        return super.isResizable();
    }

    public boolean isSplitActionBar() {
        return this.mSplitActionBar;
    }

    public boolean isTightTitleWithEmbeddedTabs() {
        return ActionBarPolicy.get(this.mContext).isTightTitle() && this.mIncludeTabs;
    }

    public void onActionModeEnd(boolean z) {
        this.mInActionMode = false;
        this.mInSearchMode = false;
        if (getExpandState() == 0) {
            this.mCollapseController.setAlpha(1.0f);
            this.mMovableController.setAlpha(0.0f);
        } else if (getExpandState() == 1) {
            this.mCollapseController.setAlpha(0.0f);
            this.mMovableController.setAlpha(1.0f);
        }
        if (z) {
            this.mMovableController.setAcceptAlphaChange(true);
            this.mCollapseController.setAcceptAlphaChange(true);
            this.mSecondaryController.setAcceptAlphaChange(true);
        }
        this.mSecondaryController.setAlpha(1.0f);
        if (z) {
            View view = this.mStartView;
            if (view != null) {
                view.setAlpha(1.0f);
            }
            View view2 = this.mEndView;
            if (view2 != null) {
                view2.setAlpha(1.0f);
            }
            View view3 = this.mTitleUpView;
            if (view3 != null) {
                view3.setAlpha(1.0f);
            }
        }
    }

    public void onActionModeStart(boolean z, boolean z2) {
        this.mInActionMode = true;
        this.mInSearchMode = z;
        this.mMovableController.setAlpha(0.0f);
        this.mCollapseController.setAlpha(0.0f);
        this.mSecondaryController.setAlpha(0.0f);
        if (z2) {
            View view = this.mStartView;
            if (view != null) {
                view.setAlpha(0.0f);
            }
            View view2 = this.mEndView;
            if (view2 != null) {
                view2.setAlpha(0.0f);
            }
            View view3 = this.mTitleUpView;
            if (view3 != null) {
                view3.setAlpha(0.0f);
            }
            this.mMovableController.setAcceptAlphaChange(false);
            this.mCollapseController.setAcceptAlphaChange(false);
            this.mSecondaryController.setAcceptAlphaChange(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onAnimatedExpandStateChanged(int i, int i2) {
        IStateStyle iStateStyle = this.mStateChangeAnimStateStyle;
        if (iStateStyle != null) {
            iStateStyle.cancel();
        }
        if (i == 1) {
            this.mPendingHeight = this.mMovableContainer.getMeasuredHeight();
        } else if (i == 0) {
            this.mPendingHeight = 0;
        }
        AnimConfig addListeners = new AnimConfig().addListeners(new InnerTransitionListener(this, i2));
        this.mStateChangeAnimStateStyle = Folme.useValue(new Object[0]).setTo((Object) Integer.valueOf(this.mPendingHeight)).to(Integer.valueOf(i2 == 1 ? this.mMovableContainer.getMeasuredHeight() : 0), addListeners);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ScrollingTabContainerView scrollingTabContainerView = this.mCollapseTabs;
        if (scrollingTabContainerView != null && this.mIncludeTabs) {
            ViewGroup.LayoutParams layoutParams = scrollingTabContainerView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.width = -2;
                layoutParams.height = -1;
            }
        }
        ScrollingTabContainerView scrollingTabContainerView2 = this.mExpandTabs;
        if (scrollingTabContainerView2 != null && this.mIncludeTabs) {
            ViewGroup.LayoutParams layoutParams2 = scrollingTabContainerView2.getLayoutParams();
            if (layoutParams2 != null) {
                layoutParams2.width = -2;
                layoutParams2.height = -1;
            }
        }
        ScrollingTabContainerView scrollingTabContainerView3 = this.mSecondaryTabs;
        if (scrollingTabContainerView3 != null && this.mIncludeTabs) {
            ViewGroup.LayoutParams layoutParams3 = scrollingTabContainerView3.getLayoutParams();
            if (layoutParams3 != null) {
                layoutParams3.width = -2;
                layoutParams3.height = -1;
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
        int measuredHeight = this.mSecondaryContainer.getMeasuredHeight();
        int i5 = this.mInnerExpandState;
        int i6 = i5 == 2 ? this.mPendingHeight : i5 == 1 ? this.mMovableContainer.getMeasuredHeight() : 0;
        int i7 = i6;
        int i8 = i4 - i2;
        int i9 = i8 - measuredHeight;
        int i10 = i9 - i7;
        boolean z2 = z;
        int i11 = i;
        int i12 = i3;
        onLayoutCollapseViews(z2, i11, i2, i12, i10);
        onLayoutExpandViews(z2, i11, i10, i12, i9);
        onLayoutSecondaryViews(z2, i11, i9, i12, i8);
        float measuredHeight2 = ((float) (this.mMovableContainer.getMeasuredHeight() - i7)) / ((float) this.mMovableContainer.getMeasuredHeight());
        ActionBarTransitionListener actionBarTransitionListener = this.mTransitionListener;
        if (actionBarTransitionListener != null) {
            actionBarTransitionListener.onActionBarMove(this.mLastProcess - measuredHeight2, measuredHeight2);
        }
        animateLayoutWithProcess(measuredHeight2);
        this.mLastProcess = measuredHeight2;
    }

    /* access modifiers changed from: protected */
    public void onLayoutExpandViews(boolean z, int i, int i2, int i3, int i4) {
        FrameLayout frameLayout = this.mMovableContainer;
        if (frameLayout != null && frameLayout.getVisibility() == 0 && this.mInnerExpandState != 0) {
            FrameLayout frameLayout2 = this.mMovableContainer;
            frameLayout2.layout(i, i4 - frameLayout2.getMeasuredHeight(), i3, i4);
            ScrollingTabContainerView scrollingTabContainerView = null;
            if (this.mMovableContainer.getChildCount() == 1 && (this.mMovableContainer.getChildAt(0) instanceof ScrollingTabContainerView)) {
                scrollingTabContainerView = (ScrollingTabContainerView) this.mMovableContainer.getChildAt(0);
            }
            if (scrollingTabContainerView != null) {
                int i5 = this.mUncollapsePaddingH;
                if (ViewUtils.isLayoutRtl(this)) {
                    i5 = (i3 - this.mUncollapsePaddingH) - scrollingTabContainerView.getMeasuredWidth();
                }
                scrollingTabContainerView.layout(i5, 0, scrollingTabContainerView.getMeasuredWidth() + i5, scrollingTabContainerView.getMeasuredHeight());
            }
            if (ViewUtils.isLayoutRtl(this)) {
                i = i3 - this.mMovableContainer.getMeasuredWidth();
            }
            this.mExpandBounds.set(i, this.mMovableContainer.getMeasuredHeight() - (i4 - i2), this.mMovableContainer.getMeasuredWidth() + i, this.mMovableContainer.getMeasuredHeight());
            this.mMovableContainer.setClipBounds(this.mExpandBounds);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayoutSecondaryViews(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        SpringBackLayout springBackLayout = this.mSecondaryContainer;
        if (springBackLayout != null && springBackLayout.getVisibility() == 0 && this.mSecondaryContainer.getChildCount() != 0) {
            SpringBackLayout springBackLayout2 = this.mSecondaryContainer;
            springBackLayout2.layout(this.mUncollapsePaddingH + i, i4 - springBackLayout2.getMeasuredHeight(), i3 - this.mUncollapsePaddingH, i4);
            ScrollingTabContainerView scrollingTabContainerView = null;
            if (this.mSecondaryContainer.getChildCount() == 1 && (this.mSecondaryContainer.getChildAt(0) instanceof ScrollingTabContainerView)) {
                scrollingTabContainerView = (ScrollingTabContainerView) this.mSecondaryContainer.getChildAt(0);
            }
            if (scrollingTabContainerView != null) {
                int measuredWidth = scrollingTabContainerView.getMeasuredWidth();
                if (ViewUtils.isLayoutRtl(this)) {
                    i6 = (i3 - (this.mUncollapsePaddingH * 2)) - scrollingTabContainerView.getMeasuredWidth();
                    i5 = i3 - (this.mUncollapsePaddingH * 2);
                } else {
                    i5 = measuredWidth;
                    i6 = 0;
                }
                scrollingTabContainerView.layout(i6, 0, i5, scrollingTabContainerView.getMeasuredHeight());
            }
            this.mSecondaryBounds.set(i, this.mSecondaryContainer.getMeasuredHeight() - (i4 - i2), i3 - this.mUncollapsePaddingH, this.mSecondaryContainer.getMeasuredHeight());
            this.mSecondaryContainer.setClipBounds(this.mSecondaryBounds);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0156, code lost:
        if (r14 != null) goto L_0x015b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x033b  */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x0354  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int measuredHeight;
        int i14;
        int i15;
        int i16;
        int childCount = getChildCount();
        int i17 = 0;
        for (int i18 = 0; i18 < childCount; i18++) {
            View childAt = getChildAt(i18);
            if (childAt.getVisibility() != 8) {
                ActionMenuView actionMenuView = this.mMenuView;
                if (childAt != actionMenuView || actionMenuView.getChildCount() != 0) {
                    i17++;
                }
            }
        }
        if (i17 == 0) {
            setMeasuredDimension(0, 0);
            this.mIsCollapsed = true;
            return;
        }
        this.mIsCollapsed = false;
        int size = MeasureSpec.getSize(i);
        int i19 = this.mContentHeight;
        if (i19 <= 0) {
            i19 = MeasureSpec.getSize(i2);
        }
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int i20 = i19 - paddingTop;
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(i20, 1073741824);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(i20, Integer.MIN_VALUE);
        int i21 = (size - paddingStart) - paddingEnd;
        int i22 = i21 / 2;
        View view = this.mStartView;
        if (view != null) {
            i21 = measureChildView(view, i21, makeMeasureSpec2, 0);
            paddingStart += this.mStartView.getMeasuredWidth();
        }
        View view2 = this.mEndView;
        if (view2 != null) {
            i21 = measureChildView(view2, i21, makeMeasureSpec2, 0);
            paddingEnd += this.mEndView.getMeasuredWidth();
        }
        HomeView homeView = this.mExpandedActionView != null ? this.mExpandedHomeLayout : this.mHomeLayout;
        if (!(this.mStartView == null || homeView == null)) {
            homeView.setVisibility(8);
        }
        if (homeView == null || homeView.getVisibility() == 8) {
            i4 = 0;
            i3 = i22;
        } else {
            int i23 = homeView.getLayoutParams().width;
            homeView.measure(i23 < 0 ? MeasureSpec.makeMeasureSpec(i21, Integer.MIN_VALUE) : MeasureSpec.makeMeasureSpec(i23, 1073741824), makeMeasureSpec);
            int measuredWidth = homeView.getMeasuredWidth() + homeView.getStartOffset();
            i4 = 0;
            i21 = Math.max(0, i21 - measuredWidth);
            i3 = Math.max(0, i21 - measuredWidth);
            paddingStart += measuredWidth;
        }
        ActionMenuView actionMenuView2 = this.mMenuView;
        if (actionMenuView2 != null && actionMenuView2.getParent() == this) {
            i21 = measureChildView(this.mMenuView, i21, makeMeasureSpec2, i4);
            i22 = Math.max(i4, i22 - this.mMenuView.getMeasuredWidth());
            paddingEnd += this.mMenuView.getMeasuredWidth();
        }
        ProgressBar progressBar = this.mIndeterminateProgressView;
        if (!(progressBar == null || progressBar.getVisibility() == 8)) {
            i21 = measureChildView(this.mIndeterminateProgressView, i21, makeMeasureSpec2, this.mProgressBarPadding * 2);
            i22 = Math.max(0, (i22 - this.mIndeterminateProgressView.getMeasuredWidth()) - (this.mProgressBarPadding * 2));
            paddingEnd += this.mIndeterminateProgressView.getMeasuredWidth();
        }
        View view3 = this.mImmersionView;
        if (!(view3 == null || view3.getVisibility() == 8)) {
            i21 = measureChildView(this.mImmersionView, i21, makeMeasureSpec2, 0);
            i22 = Math.max(0, i22 - this.mImmersionView.getMeasuredWidth());
            paddingEnd += this.mImmersionView.getMeasuredWidth();
        }
        int i24 = i22;
        boolean isShowTitle = isShowTitle();
        if (isShowTitle) {
            updateTitleCenter();
        }
        View view4 = this.mExpandedActionView;
        ActionBar.LayoutParams layoutParams = null;
        if (view4 == null) {
            if ((this.mDisplayOptions & 16) != 0) {
                view4 = this.mCustomNavView;
            }
            view4 = null;
        }
        if (!((this.mStartView == null && this.mEndView == null) || view4 == null)) {
            view4.setVisibility(8);
        }
        if (view4 == null || view4.getVisibility() == 8) {
            i6 = childCount;
            i8 = i19;
            i7 = paddingTop;
            i5 = makeMeasureSpec;
        } else {
            ViewGroup.LayoutParams generateLayoutParams = generateLayoutParams(view4.getLayoutParams());
            if (generateLayoutParams instanceof ActionBar.LayoutParams) {
                layoutParams = (ActionBar.LayoutParams) generateLayoutParams;
            }
            ActionBar.LayoutParams layoutParams2 = layoutParams;
            i8 = i19;
            if (layoutParams2 != null) {
                i7 = paddingTop;
                i15 = layoutParams2.leftMargin + layoutParams2.rightMargin;
                i16 = layoutParams2.bottomMargin + layoutParams2.topMargin;
            } else {
                i7 = paddingTop;
                i16 = 0;
                i15 = 0;
            }
            i6 = childCount;
            int i25 = (this.mContentHeight > 0 && generateLayoutParams.height != -2) ? 1073741824 : Integer.MIN_VALUE;
            int i26 = generateLayoutParams.height;
            if (i26 >= 0) {
                i20 = Math.min(i26, i20);
            }
            int max = Math.max(0, i20 - i16);
            int i27 = generateLayoutParams.width != -2 ? 1073741824 : Integer.MIN_VALUE;
            int i28 = generateLayoutParams.width;
            i5 = makeMeasureSpec;
            int max2 = Math.max(0, (i28 >= 0 ? Math.min(i28, i21) : i21) - i15);
            if (((layoutParams2 != null ? layoutParams2.gravity : DEFAULT_CUSTOM_GRAVITY) & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 1 && generateLayoutParams.width == -1) {
                max2 = Math.min(i3, i24) * 2;
            }
            view4.measure(MeasureSpec.makeMeasureSpec(max2, i27), MeasureSpec.makeMeasureSpec(max, i25));
            i21 -= i15 + view4.getMeasuredWidth();
        }
        if (this.mExpandedActionView != null || !isShowTitle) {
            i10 = 0;
        } else {
            int makeMeasureSpec3 = MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
            View view5 = this.mTitleUpView;
            if (view5 != null) {
                i9 = measureChildView(view5, i9, makeMeasureSpec3, 0);
                paddingStart += this.mTitleUpView.getMeasuredWidth();
            }
            if (isTitleCenter()) {
                i10 = 0;
                this.mCollapseContainer.measure(MeasureSpec.makeMeasureSpec(Math.max(0, size - (Math.max(paddingStart, paddingEnd) * 2)), Integer.MIN_VALUE), makeMeasureSpec3);
                i9 -= this.mCollapseContainer.getMeasuredWidth();
            } else {
                i10 = 0;
                i9 = measureChildView(this.mCollapseContainer, i9, makeMeasureSpec3, 0);
            }
        }
        this.mMovableContainer.measure(MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(i10, i10));
        SpringBackLayout springBackLayout = this.mSecondaryContainer;
        if (springBackLayout == null || springBackLayout.getVisibility() != 0 || this.mSecondaryContainer.getChildCount() == 0) {
            i12 = 0;
            i11 = 0;
        } else {
            this.mSecondaryContainer.measure(MeasureSpec.makeMeasureSpec(i10, i10), MeasureSpec.makeMeasureSpec(i10, i10));
            i11 = this.mSecondaryContainer.getMeasuredWidth();
            i12 = this.mSecondaryContainer.getMeasuredHeight();
        }
        if (this.mExpandedActionView == null) {
            int i29 = this.mNavigationMode;
            if (i29 == 1) {
                int i30 = i5;
                if (this.mListNavLayout != null) {
                    int i31 = this.mItemPadding;
                    if (isShowTitle) {
                        i31 *= 2;
                    }
                    i13 = 0;
                    this.mListNavLayout.measure(MeasureSpec.makeMeasureSpec(Math.max(0, i9 - i31), Integer.MIN_VALUE), i30);
                    if (this.mContentHeight <= 0) {
                    }
                    ProgressBar progressBar2 = this.mProgressView;
                    this.mProgressView.measure(MeasureSpec.makeMeasureSpec(size - (this.mProgressBarPadding * 2), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
                }
            } else if (i29 == 2) {
                if (this.mCollapseTabs != null) {
                    this.mCollapseTabs.measure(MeasureSpec.makeMeasureSpec(Math.max(0, this.mCollapseContainer.getMeasuredWidth()), this.mContext.getResources().getInteger(R.integer.action_bar_tab_layout_weight) == 0 ? Integer.MIN_VALUE : 1073741824), i5);
                }
                if (this.mExpandTabs != null) {
                    i14 = 0;
                    this.mExpandTabs.measure(MeasureSpec.makeMeasureSpec(Math.max(0, this.mMovableContainer.getMeasuredWidth() - this.mUncollapsePaddingH), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(0, 0));
                } else {
                    i14 = 0;
                }
                if (this.mSecondaryTabs != null) {
                    this.mSecondaryTabs.measure(MeasureSpec.makeMeasureSpec(Math.max(i14, Math.min(i11, size - (this.mUncollapsePaddingH * 2))), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(i14, i14));
                } else {
                    i13 = i14;
                    if (this.mContentHeight <= 0) {
                        int i32 = i13;
                        int i33 = i6;
                        while (i13 < i33) {
                            int measuredHeight2 = getChildAt(i13).getMeasuredHeight() + i7;
                            if (measuredHeight2 > i32) {
                                i32 = measuredHeight2;
                            }
                            i13++;
                        }
                        setMeasuredDimension(size, i32);
                    } else {
                        int i34 = i8 + i12;
                        this.mCollapseHeight = i34;
                        int i35 = this.mInnerExpandState;
                        if (i35 == 2) {
                            measuredHeight = this.mPendingHeight;
                        } else {
                            if (i35 == 1) {
                                measuredHeight = this.mMovableContainer.getMeasuredHeight();
                            }
                            setMeasuredDimension(size, i34);
                        }
                        i34 = i8 + measuredHeight + i12;
                        setMeasuredDimension(size, i34);
                    }
                    ProgressBar progressBar22 = this.mProgressView;
                    if (!(progressBar22 == null || progressBar22.getVisibility() == 8)) {
                        this.mProgressView.measure(MeasureSpec.makeMeasureSpec(size - (this.mProgressBarPadding * 2), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
                    }
                }
            }
        }
        i13 = 0;
        if (this.mContentHeight <= 0) {
        }
        ProgressBar progressBar222 = this.mProgressView;
        this.mProgressView.measure(MeasureSpec.makeMeasureSpec(size - (this.mProgressBarPadding * 2), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
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

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        int i = savedState.expandedMenuItemId;
        if (!(i == 0 || this.mExpandedMenuPresenter == null)) {
            MenuBuilder menuBuilder = this.mOptionsMenu;
            if (menuBuilder != null) {
                MenuItem findItem = menuBuilder.findItem(i);
                if (findItem != null) {
                    findItem.expandActionView();
                }
            }
        }
        if (savedState.isOverflowOpen) {
            postShowOverflowMenu();
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (expandedActionViewMenuPresenter != null) {
            MenuItemImpl menuItemImpl = expandedActionViewMenuPresenter.mCurrentExpandedItem;
            if (menuItemImpl != null) {
                savedState.expandedMenuItemId = menuItemImpl.getItemId();
            }
        }
        savedState.isOverflowOpen = isOverflowMenuShowing();
        return savedState;
    }

    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        boolean z = false;
        if (getContext().getResources().getConfiguration().orientation == 2) {
            return false;
        }
        if (this.mExpandedActionView == null && isShowTitle() && isResizable()) {
            z = true;
        }
        return z;
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
                } else if (getHeight() == this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight() || this.mPendingHeight == this.mMovableContainer.getMeasuredHeight()) {
                    setExpandState(1);
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

    public void onWindowHide() {
        this.mSplitView.onWindowHide();
    }

    public void onWindowShow() {
        this.mSplitView.onWindowShow();
    }

    public /* bridge */ /* synthetic */ void postShowOverflowMenu() {
        super.postShowOverflowMenu();
    }

    public /* bridge */ /* synthetic */ void setActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener) {
        super.setActionBarTransitionListener(actionBarTransitionListener);
    }

    public void setCallback(OnNavigationListener onNavigationListener) {
        this.mCallback = onNavigationListener;
    }

    public void setCollapsable(boolean z) {
    }

    public /* bridge */ /* synthetic */ void setContentHeight(int i) {
        super.setContentHeight(i);
    }

    public void setCustomNavigationView(View view) {
        boolean z = (this.mDisplayOptions & 16) != 0;
        View view2 = this.mCustomNavView;
        if (view2 != null && z) {
            removeView(view2);
        }
        this.mCustomNavView = view;
        View view3 = this.mCustomNavView;
        if (view3 == null || !z) {
            this.mCollapseController.attach(this.mCollapseContainer);
            return;
        }
        addView(view3);
        addedCustomView();
    }

    public void setDisplayOptions(int i) {
        HomeView homeView;
        Resources resources;
        int i2;
        int i3 = this.mDisplayOptions;
        int i4 = -1;
        if (i3 != -1) {
            i4 = i ^ i3;
        }
        this.mDisplayOptions = i;
        if ((i4 & 31) != 0) {
            boolean z = false;
            boolean z2 = (i & 2) != 0;
            int i5 = 8;
            if (z2) {
                initHomeLayout();
                this.mHomeLayout.setVisibility(this.mExpandedActionView == null ? 0 : 8);
                if ((i4 & 4) != 0) {
                    boolean z3 = (i & 4) != 0;
                    this.mHomeLayout.setUp(z3);
                    if (z3) {
                        setHomeButtonEnabled(true);
                    }
                }
                if ((i4 & 1) != 0) {
                    Drawable logo = getLogo();
                    boolean z4 = (logo == null || (i & 1) == 0) ? false : true;
                    HomeView homeView2 = this.mHomeLayout;
                    if (!z4) {
                        logo = getIcon();
                    }
                    homeView2.setIcon(logo);
                }
            } else {
                HomeView homeView3 = this.mHomeLayout;
                if (homeView3 != null) {
                    removeView(homeView3);
                }
            }
            if ((i4 & 8) != 0) {
                if ((i & 8) != 0) {
                    initTitle();
                } else {
                    this.mCollapseContainer.removeView(this.mCollapseTitleLayout);
                    this.mMovableContainer.removeView(this.mExpandTitleLayout);
                    removeView(this.mTitleUpView);
                    this.mCollapseTitleLayout = null;
                    this.mExpandTitleLayout = null;
                    this.mTitleUpView = null;
                    updateSandwichView();
                }
            }
            if (!(this.mCollapseTitleLayout == null || (i4 & 6) == 0)) {
                boolean z5 = (this.mDisplayOptions & 4) != 0;
                if (this.mCollapseTitleLayout.getVisibility() == 0) {
                    View view = this.mTitleUpView;
                    if (!z2) {
                        i5 = z5 ? 0 : 4;
                    }
                    view.setVisibility(i5);
                }
                LinearLayout linearLayout = this.mCollapseTitleLayout;
                boolean z6 = !z2 && z5;
                linearLayout.setEnabled(z6);
                LinearLayout linearLayout2 = this.mExpandTitleLayout;
                if (!z2 && z5) {
                    z = true;
                }
                linearLayout2.setEnabled(z);
            }
            if ((i4 & 16) != 0) {
                View view2 = this.mCustomNavView;
                if (view2 != null) {
                    if ((i & 16) != 0) {
                        addView(view2);
                        addedCustomView();
                    } else {
                        removeView(view2);
                    }
                }
            }
            requestLayout();
        } else {
            invalidate();
        }
        HomeView homeView4 = this.mHomeLayout;
        if (homeView4 == null) {
            return;
        }
        if (!homeView4.isEnabled()) {
            this.mHomeLayout.setContentDescription(null);
            return;
        }
        if ((i & 4) != 0) {
            homeView = this.mHomeLayout;
            resources = this.mContext.getResources();
            i2 = R.string.abc_action_bar_up_description;
        } else {
            homeView = this.mHomeLayout;
            resources = this.mContext.getResources();
            i2 = R.string.abc_action_bar_home_description;
        }
        homeView.setContentDescription(resources.getText(i2));
    }

    public void setDropdownAdapter(SpinnerAdapter spinnerAdapter) {
        this.mSpinnerAdapter = spinnerAdapter;
        Spinner spinner = this.mSpinner;
        if (spinner != null) {
            spinner.setAdapter(spinnerAdapter);
        }
    }

    public void setDropdownSelectedPosition(int i) {
        this.mSpinner.setSelection(i);
    }

    public void setEmbeddedTabView(ScrollingTabContainerView scrollingTabContainerView, ScrollingTabContainerView scrollingTabContainerView2, ScrollingTabContainerView scrollingTabContainerView3) {
        this.mIncludeTabs = scrollingTabContainerView != null;
        if (this.mIncludeTabs && this.mNavigationMode == 2) {
            addTabsContainer(scrollingTabContainerView, scrollingTabContainerView2, scrollingTabContainerView3);
        }
    }

    public void setEndView(View view) {
        View view2 = this.mEndView;
        if (view2 != null) {
            removeView(view2);
        }
        this.mEndView = view;
        View view3 = this.mEndView;
        if (view3 != null) {
            addView(view3);
            FolmeAnimHelper.addAlphaPressAnim(view);
        }
    }

    public void setExpandState(int i) {
        super.setExpandState(i);
        if (getParent() instanceof ActionBarContainer) {
            ActionBarContextView actionBarContextView = (ActionBarContextView) ((ActionBarContainer) getParent()).findViewById(R.id.action_context_bar);
            if (actionBarContextView != null) {
                actionBarContextView.setExpandState(i);
            }
        }
    }

    public /* bridge */ /* synthetic */ void setExpandState(int i, boolean z) {
        super.setExpandState(i, z);
    }

    public void setHomeAsUpIndicator(int i) {
        HomeView homeView = this.mHomeLayout;
        if (homeView != null) {
            homeView.setUpIndicator(i);
            return;
        }
        this.mHomeAsUpIndicatorDrawable = null;
        this.mHomeAsUpIndicatorResId = i;
    }

    public void setHomeAsUpIndicator(Drawable drawable) {
        HomeView homeView = this.mHomeLayout;
        if (homeView != null) {
            homeView.setUpIndicator(drawable);
            return;
        }
        this.mHomeAsUpIndicatorDrawable = drawable;
        this.mHomeAsUpIndicatorResId = 0;
    }

    public void setHomeButtonEnabled(boolean z) {
        HomeView homeView;
        Resources resources;
        int i;
        HomeView homeView2 = this.mHomeLayout;
        if (homeView2 != null) {
            homeView2.setEnabled(z);
            this.mHomeLayout.setFocusable(z);
            if (!z) {
                this.mHomeLayout.setContentDescription(null);
                return;
            }
            if ((this.mDisplayOptions & 4) != 0) {
                homeView = this.mHomeLayout;
                resources = this.mContext.getResources();
                i = R.string.abc_action_bar_up_description;
            } else {
                homeView = this.mHomeLayout;
                resources = this.mContext.getResources();
                i = R.string.abc_action_bar_home_description;
            }
            homeView.setContentDescription(resources.getText(i));
        }
    }

    public void setIcon(int i) {
        setIcon(this.mContext.getResources().getDrawable(i));
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
        this.mIconLogoInitIndicator |= 1;
        if (drawable != null && ((this.mDisplayOptions & 1) == 0 || getLogo() == null)) {
            HomeView homeView = this.mHomeLayout;
            if (homeView != null) {
                homeView.setIcon(drawable);
            }
        }
        if (this.mExpandedActionView != null) {
            this.mExpandedHomeLayout.setIcon(this.mIcon.getConstantState().newDrawable(getResources()));
        }
    }

    public void setLogo(int i) {
        setLogo(this.mContext.getResources().getDrawable(i));
    }

    public void setLogo(Drawable drawable) {
        this.mLogo = drawable;
        this.mIconLogoInitIndicator |= 2;
        if (drawable != null && (this.mDisplayOptions & 1) != 0) {
            HomeView homeView = this.mHomeLayout;
            if (homeView != null) {
                homeView.setIcon(drawable);
            }
        }
    }

    public void setMenu(Menu menu, MenuPresenter.Callback callback) {
        ActionMenuView actionMenuView;
        MenuBuilder menuBuilder = this.mOptionsMenu;
        if (menu != menuBuilder) {
            if (this.mSplitActionBar || menuBuilder != null) {
                MenuBuilder menuBuilder2 = this.mOptionsMenu;
                if (menuBuilder2 != null) {
                    menuBuilder2.removeMenuPresenter(this.mActionMenuPresenter);
                    this.mOptionsMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
                }
                MenuBuilder menuBuilder3 = (MenuBuilder) menu;
                this.mOptionsMenu = menuBuilder3;
                ActionMenuView actionMenuView2 = this.mMenuView;
                if (actionMenuView2 != null) {
                    ViewGroup viewGroup = (ViewGroup) actionMenuView2.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(this.mMenuView);
                    }
                }
                if (this.mActionMenuPresenter == null) {
                    this.mActionMenuPresenter = createActionMenuPresenter(callback);
                    this.mExpandedMenuPresenter = createExpandedActionViewMenuPresenter();
                }
                LayoutParams layoutParams = new LayoutParams(-2, -1);
                if (!this.mSplitActionBar) {
                    this.mActionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(R.bool.abc_action_bar_expanded_action_views_exclusive));
                    configPresenters(menuBuilder3);
                    actionMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                    ViewGroup viewGroup2 = (ViewGroup) actionMenuView.getParent();
                    if (!(viewGroup2 == null || viewGroup2 == this)) {
                        viewGroup2.removeView(actionMenuView);
                    }
                    addView(actionMenuView, layoutParams);
                } else {
                    this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
                    this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
                    layoutParams.width = -1;
                    layoutParams.height = -2;
                    layoutParams.gravity = DeviceHelper.IS_TABLET ? 17 : 80;
                    configPresenters(menuBuilder3);
                    actionMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                    if (this.mSplitView != null) {
                        ViewGroup viewGroup3 = (ViewGroup) actionMenuView.getParent();
                        if (!(viewGroup3 == null || viewGroup3 == this.mSplitView)) {
                            viewGroup3.removeView(actionMenuView);
                        }
                        actionMenuView.setVisibility(getAnimatedVisibility());
                        this.mSplitView.addView(actionMenuView, 1, layoutParams);
                        View findViewById = actionMenuView.findViewById(R.id.expanded_menu);
                        if (findViewById != null) {
                            findViewById.requestLayout();
                        }
                    } else {
                        actionMenuView.setLayoutParams(layoutParams);
                    }
                }
                this.mMenuView = actionMenuView;
            }
        }
    }

    public void setNavigationMode(int i) {
        int i2 = this.mNavigationMode;
        if (i != i2) {
            if (i2 == 1) {
                LinearLayout linearLayout = this.mListNavLayout;
                if (linearLayout != null) {
                    removeView(linearLayout);
                }
            }
            if (i == 1) {
                if (this.mSpinner == null) {
                    this.mSpinner = new Spinner(this.mContext, null, 16843479);
                    this.mListNavLayout = (LinearLayout) LayoutInflater.from(this.mContext).inflate(R.layout.action_bar_view_list_nav_layout, null);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
                    layoutParams.gravity = 17;
                    this.mListNavLayout.addView(this.mSpinner, layoutParams);
                }
                SpinnerAdapter adapter = this.mSpinner.getAdapter();
                SpinnerAdapter spinnerAdapter = this.mSpinnerAdapter;
                if (adapter != spinnerAdapter) {
                    this.mSpinner.setAdapter(spinnerAdapter);
                }
                this.mSpinner.setOnItemSelectedListener(this.mNavItemSelectedListener);
                addView(this.mListNavLayout);
            } else if (i == 2) {
                ScrollingTabContainerView scrollingTabContainerView = this.mCollapseTabs;
                if (scrollingTabContainerView != null) {
                    ScrollingTabContainerView scrollingTabContainerView2 = this.mExpandTabs;
                    if (scrollingTabContainerView2 != null && this.mIncludeTabs) {
                        addTabsContainer(scrollingTabContainerView, scrollingTabContainerView2, this.mSecondaryTabs);
                    }
                }
            }
            this.mNavigationMode = i;
            requestLayout();
        }
    }

    public void setProgress(int i) {
        updateProgressBars(i + 0);
    }

    public void setProgressBarIndeterminate(boolean z) {
        updateProgressBars(z ? -3 : -4);
    }

    public void setProgressBarIndeterminateVisibility(boolean z) {
        updateProgressBars(z ? -1 : -2);
    }

    public void setProgressBarVisibility(boolean z) {
        updateProgressBars(z ? -1 : -2);
    }

    public /* bridge */ /* synthetic */ void setResizable(boolean z) {
        super.setResizable(z);
    }

    public void setSplitActionBar(boolean z) {
        int i;
        ViewGroup.LayoutParams layoutParams;
        if (this.mSplitActionBar != z) {
            ActionMenuView actionMenuView = this.mMenuView;
            if (actionMenuView != null) {
                ViewGroup viewGroup = (ViewGroup) actionMenuView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(this.mMenuView);
                }
                if (z) {
                    ActionBarContainer actionBarContainer = this.mSplitView;
                    if (actionBarContainer != null) {
                        actionBarContainer.addView(this.mMenuView);
                    }
                    layoutParams = this.mMenuView.getLayoutParams();
                    i = -1;
                } else {
                    addView(this.mMenuView);
                    layoutParams = this.mMenuView.getLayoutParams();
                    i = -2;
                }
                layoutParams.width = i;
                this.mMenuView.requestLayout();
            }
            ActionBarContainer actionBarContainer2 = this.mSplitView;
            if (actionBarContainer2 != null) {
                actionBarContainer2.setVisibility(z ? 0 : 8);
            }
            ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
            if (actionMenuPresenter != null) {
                if (!z) {
                    actionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(R.bool.abc_action_bar_expanded_action_views_exclusive));
                } else {
                    actionMenuPresenter.setExpandedActionViewsExclusive(false);
                    this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
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

    public void setStartView(View view) {
        View view2 = this.mStartView;
        if (view2 != null) {
            removeView(view2);
        }
        this.mStartView = view;
        View view3 = this.mStartView;
        if (view3 != null) {
            addView(view3);
            FolmeAnimHelper.addAlphaPressAnim(view);
        }
    }

    public void setSubtitle(CharSequence charSequence) {
        this.mSubtitle = charSequence;
        TextView textView = this.mCollapseSubtitleView;
        if (textView != null) {
            textView.setText(charSequence);
            this.mExpandSubtitleView.setText(charSequence);
            boolean z = false;
            this.mCollapseSubtitleView.setVisibility(charSequence != null ? 0 : 8);
            this.mExpandSubtitleView.setVisibility(charSequence != null ? 0 : 8);
            if (this.mExpandedActionView == null && (this.mDisplayOptions & 8) != 0 && (!TextUtils.isEmpty(this.mTitle) || !TextUtils.isEmpty(this.mSubtitle))) {
                z = true;
            }
            setTitleVisibility(z);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mUserTitle = true;
        setTitleImpl(charSequence);
    }

    public /* bridge */ /* synthetic */ void setVisibility(int i) {
        super.setVisibility(i);
    }

    public void setWindowCallback(Callback callback) {
        this.mWindowCallback = callback;
    }

    public void setWindowTitle(CharSequence charSequence) {
        if (!this.mUserTitle) {
            setTitleImpl(charSequence);
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public boolean showImmersionMore() {
        View view = this.mImmersionView;
        if (view == null) {
            return false;
        }
        view.setVisibility(0);
        return true;
    }

    public /* bridge */ /* synthetic */ boolean showOverflowMenu() {
        return super.showOverflowMenu();
    }
}
