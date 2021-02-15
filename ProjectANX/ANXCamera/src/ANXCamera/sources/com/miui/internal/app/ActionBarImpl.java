package com.miui.internal.app;

import android.app.ActionBar.LayoutParams;
import android.app.ActionBar.OnMenuVisibilityListener;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.SpinnerAdapter;
import com.miui.internal.R;
import com.miui.internal.view.ActionBarPolicy;
import com.miui.internal.view.ActionModeImpl;
import com.miui.internal.view.ActionModeImpl.ActionModeCallback;
import com.miui.internal.view.EditActionModeImpl;
import com.miui.internal.view.SearchActionModeImpl;
import com.miui.internal.view.menu.ActionMenuView;
import com.miui.internal.view.menu.PhoneActionMenuView;
import com.miui.internal.widget.ActionBarContainer;
import com.miui.internal.widget.ActionBarContextView;
import com.miui.internal.widget.ActionBarOverlayLayout;
import com.miui.internal.widget.ActionBarView;
import com.miui.internal.widget.ActionModeView;
import com.miui.internal.widget.ScrollingTabContainerView;
import com.miui.internal.widget.SearchActionModeView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import miui.animation.Folme;
import miui.animation.IStateStyle;
import miui.animation.base.AnimConfig;
import miui.animation.controller.AnimState;
import miui.animation.listener.TransitionListener;
import miui.animation.listener.UpdateInfo;
import miui.animation.property.ViewProperty;
import miui.animation.utils.EaseManager;
import miui.app.ActionBar;
import miui.app.ActionBar.FragmentViewPagerChangeListener;
import miui.app.ActionBarTransitionListener;
import miui.app.IFragment;
import miui.view.SearchActionMode;

public class ActionBarImpl extends ActionBar {
    private static final int CONTEXT_DISPLAY_NORMAL = 0;
    private static final int CONTEXT_DISPLAY_SPLIT = 1;
    private static final int INVALID_POSITION = -1;
    public static final boolean IS_COMPLY_WITH_THEME = false;
    /* access modifiers changed from: private */
    public static TabListener sTabListenerWrapper = new TabListener() {
        public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
            TabImpl tabImpl = (TabImpl) tab;
            if (tabImpl.mInternalCallback != null) {
                tabImpl.mInternalCallback.onTabReselected(tab, fragmentTransaction);
            }
            if (tabImpl.mCallback != null) {
                tabImpl.mCallback.onTabReselected(tab, fragmentTransaction);
            }
        }

        public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
            TabImpl tabImpl = (TabImpl) tab;
            if (tabImpl.mInternalCallback != null) {
                tabImpl.mInternalCallback.onTabSelected(tab, fragmentTransaction);
            }
            if (tabImpl.mCallback != null) {
                tabImpl.mCallback.onTabSelected(tab, fragmentTransaction);
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
            TabImpl tabImpl = (TabImpl) tab;
            if (tabImpl.mInternalCallback != null) {
                tabImpl.mInternalCallback.onTabUnselected(tab, fragmentTransaction);
            }
            if (tabImpl.mCallback != null) {
                tabImpl.mCallback.onTabUnselected(tab, fragmentTransaction);
            }
        }
    };
    ActionMode mActionMode;
    private ActionModeCallback mActionModeCallback = new ActionModeCallback() {
        public void onActionModeFinish(ActionMode actionMode) {
            ActionBarImpl.this.animateToMode(false);
            ActionBarImpl.this.mActionMode = null;
        }
    };
    private ActionModeView mActionModeView;
    private ActionBarView mActionView;
    private IStateStyle mContainerAnim;
    private ActionBarContainer mContainerView;
    private View mContentMask;
    private OnClickListener mContentMaskOnClickListenr;
    /* access modifiers changed from: private */
    public Context mContext;
    private int mContextDisplayMode;
    private ActionBarContextView mContextView;
    private int mCurWindowVisibility = 0;
    private int mCurrentAccessibilityImportant;
    private int mCurrentExpandState;
    private boolean mCurrentResizable;
    private boolean mDisplayHomeAsUpSet;
    /* access modifiers changed from: private */
    public ScrollingTabContainerView mExpanedTabScrollView;
    private FragmentManager mFragmentManager;
    private boolean mHiddenByApp;
    private boolean mHiddenBySystem;
    private ArrayList mMenuVisibilityListeners = new ArrayList();
    private boolean mNowShowing = true;
    private ActionBarOverlayLayout mOverlayLayout;
    private int mSavedTabPosition = -1;
    private SearchActionModeView mSearchActionModeView;
    /* access modifiers changed from: private */
    public ScrollingTabContainerView mSecondaryTabScrollView;
    private TabImpl mSelectedTab;
    private boolean mShowHideAnimationEnabled;
    private boolean mShowingForMode;
    /* access modifiers changed from: private */
    public PhoneActionMenuView mSplitMenuView;
    private ActionBarContainer mSplitView;
    private IStateStyle mSpliterAnim;
    /* access modifiers changed from: private */
    public ScrollingTabContainerView mTabScrollView;
    private ArrayList mTabs = new ArrayList();
    private Context mThemedContext;
    private ActionBarViewPagerController mViewPagerController;

    public class TabImpl extends Tab {
        /* access modifiers changed from: private */
        public TabListener mCallback;
        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        /* access modifiers changed from: private */
        public TabListener mInternalCallback;
        private int mPosition = -1;
        private Object mTag;
        private CharSequence mText;

        public TabImpl() {
        }

        public TabListener getCallback() {
            return ActionBarImpl.sTabListenerWrapper;
        }

        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }

        public View getCustomView() {
            return this.mCustomView;
        }

        public Drawable getIcon() {
            return this.mIcon;
        }

        public int getPosition() {
            return this.mPosition;
        }

        public Object getTag() {
            return this.mTag;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public void select() {
            ActionBarImpl.this.selectTab(this);
        }

        public Tab setContentDescription(int i) {
            return setContentDescription(ActionBarImpl.this.mContext.getResources().getText(i));
        }

        public Tab setContentDescription(CharSequence charSequence) {
            this.mContentDesc = charSequence;
            if (this.mPosition >= 0) {
                ActionBarImpl.this.mTabScrollView.updateTab(this.mPosition);
                ActionBarImpl.this.mExpanedTabScrollView.updateTab(this.mPosition);
                ActionBarImpl.this.mSecondaryTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        public Tab setCustomView(int i) {
            return setCustomView(LayoutInflater.from(ActionBarImpl.this.getThemedContext()).inflate(i, null));
        }

        public Tab setCustomView(View view) {
            this.mCustomView = view;
            ActionBarImpl.this.setExpandState(0);
            ActionBarImpl.this.setResizable(false);
            if (this.mPosition >= 0) {
                ActionBarImpl.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        public Tab setIcon(int i) {
            return setIcon(ActionBarImpl.this.mContext.getResources().getDrawable(i));
        }

        public Tab setIcon(Drawable drawable) {
            this.mIcon = drawable;
            if (this.mPosition >= 0) {
                ActionBarImpl.this.mTabScrollView.updateTab(this.mPosition);
                ActionBarImpl.this.mExpanedTabScrollView.updateTab(this.mPosition);
                ActionBarImpl.this.mSecondaryTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        public Tab setInternalTabListener(TabListener tabListener) {
            this.mInternalCallback = tabListener;
            return this;
        }

        public void setPosition(int i) {
            this.mPosition = i;
        }

        public Tab setTabListener(TabListener tabListener) {
            this.mCallback = tabListener;
            return this;
        }

        public Tab setTag(Object obj) {
            this.mTag = obj;
            return this;
        }

        public Tab setText(int i) {
            return setText(ActionBarImpl.this.mContext.getResources().getText(i));
        }

        public Tab setText(CharSequence charSequence) {
            this.mText = charSequence;
            if (this.mPosition >= 0) {
                ActionBarImpl.this.mTabScrollView.updateTab(this.mPosition);
                ActionBarImpl.this.mExpanedTabScrollView.updateTab(this.mPosition);
                ActionBarImpl.this.mSecondaryTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }
    }

    class ViewHideTransitionListener extends TransitionListener {
        private WeakReference mRef;

        public ViewHideTransitionListener(View view) {
            this.mRef = new WeakReference(view);
        }

        public void onComplete(Object obj, UpdateInfo updateInfo) {
            if (this.mRef.get() != null) {
                ((View) this.mRef.get()).setVisibility(8);
            }
        }
    }

    public ActionBarImpl(Activity activity) {
        this.mContext = activity;
        this.mFragmentManager = activity.getFragmentManager();
        init((ViewGroup) activity.getWindow().getDecorView().findViewById(R.id.action_bar_overlay_layout));
        setTitle(activity.getTitle());
    }

    public ActionBarImpl(Dialog dialog) {
        this.mContext = dialog.getContext();
        init((ViewGroup) dialog.getWindow().getDecorView().findViewById(R.id.action_bar_overlay_layout));
    }

    public ActionBarImpl(Fragment fragment) {
        this.mContext = ((IFragment) fragment).getThemedContext();
        this.mFragmentManager = fragment.getFragmentManager();
        init((ViewGroup) fragment.getView());
        Activity activity = fragment.getActivity();
        setTitle(activity != null ? activity.getTitle() : null);
    }

    private void addContentMask() {
        ViewStub viewStub = (ViewStub) this.mOverlayLayout.findViewById(R.id.content_mask_vs);
        this.mOverlayLayout.setContentMask(viewStub != null ? viewStub.inflate() : this.mOverlayLayout.findViewById(R.id.content_mask));
    }

    private static boolean checkShowingFlags(boolean z, boolean z2, boolean z3) {
        if (z3) {
            return true;
        }
        return !z && !z2;
    }

    private void cleanupTabs() {
        if (this.mSelectedTab != null) {
            selectTab(null);
        }
        this.mTabs.clear();
        ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
        if (scrollingTabContainerView != null) {
            scrollingTabContainerView.removeAllTabs();
        }
        ScrollingTabContainerView scrollingTabContainerView2 = this.mExpanedTabScrollView;
        if (scrollingTabContainerView2 != null) {
            scrollingTabContainerView2.removeAllTabs();
        }
        ScrollingTabContainerView scrollingTabContainerView3 = this.mSecondaryTabScrollView;
        if (scrollingTabContainerView3 != null) {
            scrollingTabContainerView3.removeAllTabs();
        }
        this.mSavedTabPosition = -1;
    }

    private void configureTab(Tab tab, int i) {
        TabImpl tabImpl = (TabImpl) tab;
        if (tabImpl.getCallback() != null) {
            tabImpl.setPosition(i);
            this.mTabs.add(i, tabImpl);
            int size = this.mTabs.size();
            while (true) {
                i++;
                if (i < size) {
                    ((TabImpl) this.mTabs.get(i)).setPosition(i);
                } else {
                    return;
                }
            }
        } else {
            throw new IllegalStateException("Action Bar Tab must have a Callback");
        }
    }

    private ActionMode createActionMode(Callback callback) {
        return callback instanceof SearchActionMode.Callback ? new SearchActionModeImpl(this.mContext, callback) : new EditActionModeImpl(this.mContext, callback);
    }

    private void createContextView(boolean z, boolean z2) {
        ViewStub viewStub = (ViewStub) this.mOverlayLayout.findViewById(R.id.split_action_bar_vs);
        ActionBarContainer actionBarContainer = (ActionBarContainer) (viewStub != null ? viewStub.inflate() : this.mOverlayLayout.findViewById(R.id.split_action_bar));
        if (actionBarContainer != null) {
            this.mActionView.setSplitView(actionBarContainer);
            this.mActionView.setSplitActionBar(z);
            this.mActionView.setSplitWhenNarrow(z2);
            ViewStub viewStub2 = (ViewStub) this.mOverlayLayout.findViewById(R.id.action_context_bar_vs);
            this.mContextView = (ActionBarContextView) (viewStub2 != null ? viewStub2.inflate() : this.mOverlayLayout.findViewById(R.id.action_context_bar));
            ActionBarContextView actionBarContextView = this.mContextView;
            if (actionBarContextView != null) {
                actionBarContainer.setActionBarContextView(actionBarContextView);
                this.mContextView.setSplitView(actionBarContainer);
                this.mContextView.setSplitActionBar(z);
                this.mContextView.setSplitWhenNarrow(z2);
                this.mOverlayLayout.setSplitActionBarView(actionBarContainer);
                this.mOverlayLayout.setActionBarContextView(this.mContextView);
                addContentMask();
            }
        }
    }

    private void ensureTabsExist() {
        if (this.mTabScrollView == null) {
            ScrollingTabContainerView scrollingTabContainerView = new ScrollingTabContainerView(this.mContext, 0);
            ScrollingTabContainerView scrollingTabContainerView2 = new ScrollingTabContainerView(this.mContext, 1);
            ScrollingTabContainerView scrollingTabContainerView3 = new ScrollingTabContainerView(this.mContext, 2);
            scrollingTabContainerView.setVisibility(0);
            scrollingTabContainerView2.setVisibility(0);
            scrollingTabContainerView3.setVisibility(0);
            this.mActionView.setEmbeddedTabView(scrollingTabContainerView, scrollingTabContainerView2, scrollingTabContainerView3);
            scrollingTabContainerView.setEmbeded(true);
            this.mTabScrollView = scrollingTabContainerView;
            this.mExpanedTabScrollView = scrollingTabContainerView2;
            this.mSecondaryTabScrollView = scrollingTabContainerView3;
        }
    }

    public static ActionBarImpl getActionBar(View view) {
        while (view != null) {
            if (view instanceof ActionBarOverlayLayout) {
                return (ActionBarImpl) ((ActionBarOverlayLayout) view).getActionBar();
            }
            view = view.getParent() instanceof View ? (View) view.getParent() : null;
        }
        return null;
    }

    private int getBlurOptioons() {
        int i = 32768;
        boolean z = true;
        int i2 = 0;
        boolean z2 = (getDisplayOptions() & 32768) != 0;
        if ((getDisplayOptions() & 16384) == 0) {
            z = false;
        }
        if (!z2) {
            i = 0;
        }
        if (z) {
            i2 = 16384;
        }
        return i | i2;
    }

    private int getSplitHeight() {
        int height = this.mSplitView.getHeight();
        if (this.mSplitView.getChildCount() != 1) {
            return height;
        }
        View childAt = this.mSplitView.getChildAt(1);
        if (childAt == null || !(childAt instanceof PhoneActionMenuView)) {
            return height;
        }
        PhoneActionMenuView phoneActionMenuView = (PhoneActionMenuView) childAt;
        return !phoneActionMenuView.isOverflowMenuShowing() ? phoneActionMenuView.getCollapsedHeight() : height;
    }

    private void setHasEmbeddedTabs(boolean z) {
        this.mContainerView.setTabContainer(null);
        this.mActionView.setEmbeddedTabView(this.mTabScrollView, this.mExpanedTabScrollView, this.mSecondaryTabScrollView);
        boolean z2 = getNavigationMode() == 2;
        ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
        if (scrollingTabContainerView != null) {
            if (z2) {
                scrollingTabContainerView.setVisibility(0);
            } else {
                scrollingTabContainerView.setVisibility(8);
            }
            this.mTabScrollView.setEmbeded(true);
        }
        ScrollingTabContainerView scrollingTabContainerView2 = this.mExpanedTabScrollView;
        if (scrollingTabContainerView2 != null) {
            if (z2) {
                scrollingTabContainerView2.setVisibility(0);
            } else {
                scrollingTabContainerView2.setVisibility(8);
            }
            this.mExpanedTabScrollView.setEmbeded(true);
        }
        ScrollingTabContainerView scrollingTabContainerView3 = this.mSecondaryTabScrollView;
        if (scrollingTabContainerView3 != null) {
            if (z2) {
                scrollingTabContainerView3.setVisibility(0);
            } else {
                scrollingTabContainerView3.setVisibility(8);
            }
            this.mSecondaryTabScrollView.setEmbeded(true);
        }
        this.mActionView.setCollapsable(false);
    }

    private IStateStyle startContainerViewAnimation(boolean z, String str, AnimState animState) {
        AnimConfig[] animConfigArr;
        IStateStyle iStateStyle;
        AnimState animState2;
        int height = this.mContainerView.getHeight();
        if (z) {
            AnimConfig animConfig = new AnimConfig();
            animConfig.setEase(EaseManager.getStyle(-2, 0.9f, 0.25f));
            animState2 = new AnimState(str).add(ViewProperty.TRANSLATION_Y, 0, new long[0]).add(ViewProperty.ALPHA, 1.0f, new long[0]);
            iStateStyle = Folme.useAt(this.mContainerView).state();
            if (animState != null) {
                animState.setTag(str);
                iStateStyle = iStateStyle.setTo((Object) animState);
            }
            animConfigArr = new AnimConfig[]{animConfig};
        } else {
            AnimConfig animConfig2 = new AnimConfig();
            animConfig2.setEase(EaseManager.getStyle(-2, 1.0f, 0.35f));
            animConfig2.addListeners(new ViewHideTransitionListener(this.mContainerView));
            animState2 = new AnimState(str).add(ViewProperty.TRANSLATION_Y, (-height) - 100, new long[0]).add(ViewProperty.ALPHA, 0.0f, new long[0]);
            IStateStyle state = Folme.useAt(this.mContainerView).state();
            if (animState != null) {
                animState.setTag(str);
                state = state.setTo((Object) animState);
            }
            animConfigArr = new AnimConfig[]{animConfig2};
        }
        return iStateStyle.to(animState2, animConfigArr);
    }

    private IStateStyle startSplitViewAnimation(boolean z, String str, AnimState animState) {
        AnimConfig[] animConfigArr;
        IStateStyle iStateStyle;
        AnimState animState2;
        int splitHeight = getSplitHeight();
        if (z) {
            AnimConfig animConfig = new AnimConfig();
            animConfig.setEase(EaseManager.getStyle(-2, 0.9f, 0.25f));
            animState2 = new AnimState(str).add(ViewProperty.TRANSLATION_Y, 0, new long[0]).add(ViewProperty.ALPHA, 1, new long[0]);
            iStateStyle = Folme.useAt(this.mSplitView).state();
            if (animState != null) {
                animState.setTag(str);
                iStateStyle = iStateStyle.setTo((Object) animState);
            }
            animConfigArr = new AnimConfig[]{animConfig};
        } else {
            AnimConfig animConfig2 = new AnimConfig();
            animConfig2.setEase(EaseManager.getStyle(-2, 1.0f, 0.35f));
            animConfig2.addListeners(new ViewHideTransitionListener(this.mSplitView));
            animState2 = new AnimState(str).add(ViewProperty.TRANSLATION_Y, splitHeight + 100, new long[0]).add(ViewProperty.ALPHA, 0, new long[0]);
            IStateStyle state = Folme.useAt(this.mSplitView).state();
            if (animState != null) {
                animState.setTag(str);
                state = state.setTo((Object) animState);
            }
            animConfigArr = new AnimConfig[]{animConfig2};
        }
        return iStateStyle.to(animState2, animConfigArr);
    }

    private void updateContentMaskVisibility(boolean z) {
        if (this.mSplitView.getChildCount() == 2 && (this.mSplitView.getChildAt(1) instanceof PhoneActionMenuView)) {
            this.mSplitMenuView = (PhoneActionMenuView) this.mSplitView.getChildAt(1);
            if (this.mSplitMenuView.isOverflowMenuShowing() && this.mContentMask != null) {
                (z ? this.mOverlayLayout.getContentMaskAnimator(this.mContentMaskOnClickListenr).show() : this.mOverlayLayout.getContentMaskAnimator(null).hide()).start();
            }
        }
    }

    private void updateVisibility(boolean z) {
        if (checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
            if (!this.mNowShowing) {
                this.mNowShowing = true;
                doShow(z);
            }
        } else if (this.mNowShowing) {
            this.mNowShowing = false;
            doHide(z);
        }
    }

    public int addFragmentTab(String str, Tab tab, int i, Class cls, Bundle bundle, boolean z) {
        return this.mViewPagerController.addFragmentTab(str, tab, i, cls, bundle, z);
    }

    public int addFragmentTab(String str, Tab tab, Class cls, Bundle bundle, boolean z) {
        return this.mViewPagerController.addFragmentTab(str, tab, cls, bundle, z);
    }

    public void addOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener fragmentViewPagerChangeListener) {
        this.mViewPagerController.addOnFragmentViewPagerChangeListener(fragmentViewPagerChangeListener);
    }

    public void addOnMenuVisibilityListener(OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.add(onMenuVisibilityListener);
    }

    public void addTab(Tab tab) {
        addTab(tab, this.mTabs.isEmpty());
    }

    public void addTab(Tab tab, int i) {
        addTab(tab, i, this.mTabs.isEmpty());
    }

    public void addTab(Tab tab, int i, boolean z) {
        if (!isFragmentViewPagerMode()) {
            internalAddTab(tab, i, z);
            return;
        }
        throw new IllegalStateException("Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void addTab(Tab tab, boolean z) {
        if (!isFragmentViewPagerMode()) {
            internalAddTab(tab, z);
            return;
        }
        throw new IllegalStateException("Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    /* access modifiers changed from: 0000 */
    public void animateToMode(boolean z) {
        if (z) {
            showForActionMode();
        } else {
            hideForActionMode();
        }
        this.mActionModeView.animateToVisibility(z);
        if (this.mTabScrollView != null && !this.mActionView.isTightTitleWithEmbeddedTabs() && this.mActionView.isCollapsed()) {
            this.mTabScrollView.setEnabled(!z);
            this.mExpanedTabScrollView.setEnabled(!z);
            this.mSecondaryTabScrollView.setEnabled(!z);
        }
    }

    public ActionModeView createActionModeView(Callback callback) {
        if (callback instanceof SearchActionMode.Callback) {
            if (this.mSearchActionModeView == null) {
                this.mSearchActionModeView = createSearchActionModeView();
            }
            Rect pendingInsets = this.mContainerView.getPendingInsets();
            if (pendingInsets != null) {
                this.mSearchActionModeView.setStatusBarPaddingTop(pendingInsets.top);
            }
            if (this.mOverlayLayout != this.mSearchActionModeView.getParent()) {
                this.mOverlayLayout.addView(this.mSearchActionModeView);
            }
            return this.mSearchActionModeView;
        }
        if (this.mContextView == null) {
            createContextView(true, true);
        }
        Rect pendingInsets2 = this.mContainerView.getPendingInsets();
        if (pendingInsets2 != null) {
            this.mContextView.setContentInset(pendingInsets2.top);
        }
        return this.mContextView;
    }

    public SearchActionModeView createSearchActionModeView() {
        SearchActionModeView searchActionModeView = (SearchActionModeView) LayoutInflater.from(getThemedContext()).inflate(R.layout.search_action_mode_view, this.mOverlayLayout, false);
        searchActionModeView.setOnBackClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActionMode actionMode = ActionBarImpl.this.mActionMode;
                if (actionMode != null) {
                    actionMode.finish();
                }
            }
        });
        return searchActionModeView;
    }

    public void doHide(boolean z) {
        AnimState animState;
        IStateStyle iStateStyle = this.mContainerAnim;
        AnimState animState2 = null;
        if (iStateStyle != null) {
            animState = iStateStyle.getCurrentState();
            this.mContainerAnim.cancel();
        } else {
            animState = null;
        }
        boolean z2 = isShowHideAnimationEnabled() || z;
        if (z2) {
            this.mContainerAnim = startContainerViewAnimation(false, "HideActionBar", animState);
        } else {
            ActionBarContainer actionBarContainer = this.mContainerView;
            actionBarContainer.setTranslationY((float) (-actionBarContainer.getHeight()));
            this.mContainerView.setAlpha(0.0f);
            this.mContainerView.setVisibility(8);
        }
        if (this.mSplitView != null) {
            IStateStyle iStateStyle2 = this.mSpliterAnim;
            if (iStateStyle2 != null) {
                animState2 = iStateStyle2.getCurrentState();
                this.mSpliterAnim.cancel();
            }
            if (z2) {
                this.mSpliterAnim = startSplitViewAnimation(false, "SpliterHide", animState2);
            } else {
                this.mSplitView.setTranslationY((float) getSplitHeight());
                this.mSplitView.setAlpha(0.0f);
                this.mSplitView.setVisibility(8);
            }
            updateContentMaskVisibility(false);
        }
    }

    public void doShow(boolean z) {
        AnimState animState;
        IStateStyle iStateStyle = this.mContainerAnim;
        AnimState animState2 = null;
        if (iStateStyle != null) {
            animState = iStateStyle.getCurrentState();
            this.mContainerAnim.cancel();
        } else {
            animState = null;
        }
        boolean z2 = isShowHideAnimationEnabled() || z;
        this.mContainerView.setVisibility(0);
        if (z2) {
            this.mContainerAnim = startContainerViewAnimation(true, "ShowActionBar", animState);
        } else {
            this.mContainerView.setTranslationY(0.0f);
            this.mContainerView.setAlpha(1.0f);
        }
        if (this.mSplitView != null) {
            IStateStyle iStateStyle2 = this.mSpliterAnim;
            if (iStateStyle2 != null) {
                animState2 = iStateStyle2.getCurrentState();
                this.mSpliterAnim.cancel();
            }
            this.mSplitView.setVisibility(0);
            if (z2) {
                this.mSpliterAnim = startSplitViewAnimation(true, "SpliterShow", animState2);
                if (this.mActionView.isSplitActionBar() && this.mSplitView.getChildCount() > 0) {
                    View childAt = this.mSplitView.getChildAt(1);
                    if (childAt != null && (childAt instanceof PhoneActionMenuView) && (!((PhoneActionMenuView) childAt).isOverflowMenuShowing())) {
                        ((ActionMenuView) childAt).startLayoutAnimation();
                    }
                }
            } else {
                this.mSplitView.setTranslationY(0.0f);
                this.mSplitView.setAlpha(1.0f);
            }
            updateContentMaskVisibility(true);
        }
    }

    /* access modifiers changed from: 0000 */
    public ActionBarOverlayLayout getActionBarOverlayLayout() {
        return this.mOverlayLayout;
    }

    public ActionBarTransitionListener getActionBarTransitionListener() {
        return this.mActionView.getActionBarTransitionListener();
    }

    public View getCustomView() {
        return this.mActionView.getCustomNavigationView();
    }

    public int getDisplayOptions() {
        return this.mActionView.getDisplayOptions();
    }

    public View getEndView() {
        return this.mActionView.getEndView();
    }

    public int getExpandState() {
        return this.mActionView.getExpandState();
    }

    public Fragment getFragmentAt(int i) {
        return this.mViewPagerController.getFragmentAt(i);
    }

    public int getFragmentTabCount() {
        return this.mViewPagerController.getFragmentTabCount();
    }

    public int getHeight() {
        return this.mContainerView.getHeight();
    }

    public int getNavigationItemCount() {
        int navigationMode = this.mActionView.getNavigationMode();
        int i = 0;
        if (navigationMode == 1) {
            SpinnerAdapter dropdownAdapter = this.mActionView.getDropdownAdapter();
            if (dropdownAdapter != null) {
                i = dropdownAdapter.getCount();
            }
            return i;
        } else if (navigationMode != 2) {
            return 0;
        } else {
            return this.mTabs.size();
        }
    }

    public int getNavigationMode() {
        return this.mActionView.getNavigationMode();
    }

    public int getSelectedNavigationIndex() {
        int navigationMode = this.mActionView.getNavigationMode();
        if (navigationMode == 1) {
            return this.mActionView.getDropdownSelectedPosition();
        }
        int i = -1;
        if (navigationMode != 2) {
            return -1;
        }
        TabImpl tabImpl = this.mSelectedTab;
        if (tabImpl != null) {
            i = tabImpl.getPosition();
        }
        return i;
    }

    public Tab getSelectedTab() {
        return this.mSelectedTab;
    }

    public View getStartView() {
        return this.mActionView.getStartView();
    }

    public CharSequence getSubtitle() {
        return this.mActionView.getSubtitle();
    }

    public Tab getTabAt(int i) {
        return (Tab) this.mTabs.get(i);
    }

    public int getTabCount() {
        return this.mTabs.size();
    }

    public Context getThemedContext() {
        if (this.mThemedContext == null) {
            TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(16843671, typedValue, true);
            int i = typedValue.resourceId;
            if (i != 0) {
                this.mThemedContext = new ContextThemeWrapper(this.mContext, i);
            } else {
                this.mThemedContext = this.mContext;
            }
        }
        return this.mThemedContext;
    }

    public CharSequence getTitle() {
        return this.mActionView.getTitle();
    }

    public int getViewPagerOffscreenPageLimit() {
        return this.mViewPagerController.getViewPagerOffscreenPageLimit();
    }

    public boolean hasNonEmbeddedTabs() {
        return false;
    }

    public void hide() {
        if (!this.mHiddenByApp) {
            this.mHiddenByApp = true;
            updateVisibility(false);
        }
    }

    /* access modifiers changed from: 0000 */
    public void hideForActionMode() {
        if (this.mShowingForMode) {
            this.mShowingForMode = false;
            this.mActionView.onActionModeEnd((getDisplayOptions() & 32768) != 0);
            updateVisibility(false);
            setResizable(true);
            ActionModeView actionModeView = this.mActionModeView;
            if (actionModeView instanceof SearchActionModeView) {
                setExpandState(this.mCurrentExpandState, true);
            } else {
                this.mCurrentExpandState = ((ActionBarContextView) actionModeView).getExpandState();
                this.mCurrentResizable = ((ActionBarContextView) this.mActionModeView).isResizable();
                setExpandState(this.mCurrentExpandState);
            }
            setResizable(this.mCurrentResizable);
            this.mActionView.setImportantForAccessibility(this.mCurrentAccessibilityImportant);
        }
    }

    /* access modifiers changed from: protected */
    public void init(ViewGroup viewGroup) {
        this.mOverlayLayout = (ActionBarOverlayLayout) viewGroup;
        this.mOverlayLayout.setActionBar(this);
        this.mActionView = (ActionBarView) viewGroup.findViewById(R.id.action_bar);
        this.mContextView = (ActionBarContextView) viewGroup.findViewById(R.id.action_context_bar);
        this.mContainerView = (ActionBarContainer) viewGroup.findViewById(R.id.action_bar_container);
        this.mSplitView = (ActionBarContainer) viewGroup.findViewById(R.id.split_action_bar);
        this.mContentMask = viewGroup.findViewById(R.id.content_mask);
        if (this.mContentMask != null) {
            this.mContentMaskOnClickListenr = new OnClickListener() {
                public void onClick(View view) {
                    if (ActionBarImpl.this.mSplitMenuView != null && ActionBarImpl.this.mSplitMenuView.isOverflowMenuShowing()) {
                        ActionBarImpl.this.mSplitMenuView.getPresenter().hideOverflowMenu(true);
                    }
                }
            };
        }
        if (this.mActionView == null && this.mContextView == null && this.mContainerView == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(ActionBarImpl.class.getSimpleName());
            sb.append(" can only be used with a compatible window decor layout");
            throw new IllegalStateException(sb.toString());
        }
        this.mContextDisplayMode = this.mActionView.isSplitActionBar() ? 1 : 0;
        boolean z = false;
        boolean z2 = (this.mActionView.getDisplayOptions() & 4) != 0;
        if (z2) {
            this.mDisplayHomeAsUpSet = true;
        }
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(this.mContext);
        if (actionBarPolicy.enableHomeButtonByDefault() || z2) {
            z = true;
        }
        setHomeButtonEnabled(z);
        setHasEmbeddedTabs(actionBarPolicy.hasEmbeddedTabs());
    }

    /* access modifiers changed from: 0000 */
    public void internalAddTab(Tab tab) {
        internalAddTab(tab, getTabCount() == 0);
    }

    /* access modifiers changed from: 0000 */
    public void internalAddTab(Tab tab, int i) {
        internalAddTab(tab, i, i == getTabCount());
    }

    /* access modifiers changed from: 0000 */
    public void internalAddTab(Tab tab, int i, boolean z) {
        ensureTabsExist();
        this.mTabScrollView.addTab(tab, i, z);
        this.mExpanedTabScrollView.addTab(tab, i, z);
        this.mSecondaryTabScrollView.addTab(tab, i, z);
        configureTab(tab, i);
        if (z) {
            selectTab(tab);
        }
    }

    /* access modifiers changed from: 0000 */
    public void internalAddTab(Tab tab, boolean z) {
        ensureTabsExist();
        this.mTabScrollView.addTab(tab, z);
        this.mExpanedTabScrollView.addTab(tab, z);
        this.mSecondaryTabScrollView.addTab(tab, z);
        configureTab(tab, this.mTabs.size());
        if (z) {
            selectTab(tab);
        }
    }

    /* access modifiers changed from: 0000 */
    public void internalRemoveAllTabs() {
        cleanupTabs();
    }

    /* access modifiers changed from: 0000 */
    public void internalRemoveTab(Tab tab) {
        internalRemoveTabAt(tab.getPosition());
    }

    /* access modifiers changed from: 0000 */
    public void internalRemoveTabAt(int i) {
        if (this.mTabScrollView != null) {
            TabImpl tabImpl = this.mSelectedTab;
            int position = tabImpl != null ? tabImpl.getPosition() : this.mSavedTabPosition;
            this.mTabScrollView.removeTabAt(i);
            this.mExpanedTabScrollView.removeTabAt(i);
            this.mSecondaryTabScrollView.removeTabAt(i);
            TabImpl tabImpl2 = (TabImpl) this.mTabs.remove(i);
            if (tabImpl2 != null) {
                tabImpl2.setPosition(-1);
            }
            int size = this.mTabs.size();
            for (int i2 = i; i2 < size; i2++) {
                ((TabImpl) this.mTabs.get(i2)).setPosition(i2);
            }
            if (position == i) {
                selectTab(this.mTabs.isEmpty() ? null : (Tab) this.mTabs.get(Math.max(0, i - 1)));
            }
        }
    }

    public boolean isFragmentViewPagerMode() {
        return this.mViewPagerController != null;
    }

    public boolean isResizable() {
        return this.mActionView.isResizable();
    }

    /* access modifiers changed from: 0000 */
    public boolean isShowHideAnimationEnabled() {
        return this.mShowHideAnimationEnabled;
    }

    public boolean isShowing() {
        return this.mNowShowing;
    }

    public Tab newTab() {
        return new TabImpl();
    }

    public void onConfigurationChanged(Configuration configuration) {
        setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
    }

    public void removeAllFragmentTab() {
        this.mViewPagerController.removeAllFragmentTab();
    }

    public void removeAllTabs() {
        if (!isFragmentViewPagerMode()) {
            internalRemoveAllTabs();
            return;
        }
        throw new IllegalStateException("Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void removeFragmentTab(Tab tab) {
        this.mViewPagerController.removeFragmentTab(tab);
    }

    public void removeFragmentTab(Fragment fragment) {
        this.mViewPagerController.removeFragment(fragment);
    }

    public void removeFragmentTab(String str) {
        this.mViewPagerController.removeFragmentTab(str);
    }

    public void removeFragmentTabAt(int i) {
        this.mViewPagerController.removeFragmentAt(i);
    }

    public void removeOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener fragmentViewPagerChangeListener) {
        this.mViewPagerController.removeOnFragmentViewPagerChangeListener(fragmentViewPagerChangeListener);
    }

    public void removeOnMenuVisibilityListener(OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.remove(onMenuVisibilityListener);
    }

    public void removeTab(Tab tab) {
        if (!isFragmentViewPagerMode()) {
            internalRemoveTab(tab);
            return;
        }
        throw new IllegalStateException("Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void removeTabAt(int i) {
        if (!isFragmentViewPagerMode()) {
            internalRemoveTabAt(i);
            return;
        }
        throw new IllegalStateException("Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void selectTab(Tab tab) {
        int i = -1;
        if (getNavigationMode() != 2) {
            if (tab != null) {
                i = tab.getPosition();
            }
            this.mSavedTabPosition = i;
            return;
        }
        FragmentTransaction disallowAddToBackStack = this.mFragmentManager.beginTransaction().disallowAddToBackStack();
        TabImpl tabImpl = this.mSelectedTab;
        if (tabImpl != tab) {
            this.mTabScrollView.setTabSelected(tab != null ? tab.getPosition() : -1);
            this.mExpanedTabScrollView.setTabSelected(tab != null ? tab.getPosition() : -1);
            ScrollingTabContainerView scrollingTabContainerView = this.mSecondaryTabScrollView;
            if (tab != null) {
                i = tab.getPosition();
            }
            scrollingTabContainerView.setTabSelected(i);
            TabImpl tabImpl2 = this.mSelectedTab;
            if (tabImpl2 != null) {
                tabImpl2.getCallback().onTabUnselected(this.mSelectedTab, disallowAddToBackStack);
            }
            this.mSelectedTab = (TabImpl) tab;
            TabImpl tabImpl3 = this.mSelectedTab;
            if (tabImpl3 != null) {
                tabImpl3.getCallback().onTabSelected(this.mSelectedTab, disallowAddToBackStack);
            }
        } else if (tabImpl != null) {
            tabImpl.getCallback().onTabReselected(this.mSelectedTab, disallowAddToBackStack);
            this.mTabScrollView.animateToTab(tab.getPosition());
            this.mExpanedTabScrollView.animateToTab(tab.getPosition());
            this.mSecondaryTabScrollView.animateToTab(tab.getPosition());
        }
        if (!disallowAddToBackStack.isEmpty()) {
            disallowAddToBackStack.commit();
        }
    }

    public void setActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener) {
        this.mActionView.setActionBarTransitionListener(actionBarTransitionListener);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        boolean z = (getDisplayOptions() & 32768) != 0;
        ActionBarContainer actionBarContainer = this.mContainerView;
        if (z) {
            drawable = null;
        }
        actionBarContainer.setPrimaryBackground(drawable);
    }

    public void setCustomView(int i) {
        setCustomView(LayoutInflater.from(getThemedContext()).inflate(i, this.mActionView, false));
    }

    public void setCustomView(View view) {
        this.mActionView.setCustomNavigationView(view);
    }

    public void setCustomView(View view, LayoutParams layoutParams) {
        view.setLayoutParams(layoutParams);
        this.mActionView.setCustomNavigationView(view);
    }

    public void setDisplayHomeAsUpEnabled(boolean z) {
        setDisplayOptions(z ? getBlurOptioons() | 4 : 0, getBlurOptioons() | 4);
    }

    public void setDisplayOptions(int i) {
        boolean z = true;
        if ((i & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mActionView.setDisplayOptions(i);
        int displayOptions = this.mActionView.getDisplayOptions();
        ActionBarContainer actionBarContainer = this.mContainerView;
        if (actionBarContainer != null) {
            actionBarContainer.setBlurBackground((32768 & displayOptions) != 0);
        }
        ActionBarContainer actionBarContainer2 = this.mSplitView;
        if (actionBarContainer2 != null) {
            if ((displayOptions & 16384) == 0) {
                z = false;
            }
            actionBarContainer2.setBlurBackground(z);
        }
    }

    public void setDisplayOptions(int i, int i2) {
        int displayOptions = this.mActionView.getDisplayOptions();
        if ((i2 & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mActionView.setDisplayOptions((i & i2) | ((~i2) & displayOptions));
        int displayOptions2 = this.mActionView.getDisplayOptions();
        ActionBarContainer actionBarContainer = this.mContainerView;
        boolean z = false;
        if (actionBarContainer != null) {
            actionBarContainer.setBlurBackground((32768 & displayOptions2) != 0);
        }
        ActionBarContainer actionBarContainer2 = this.mSplitView;
        if (actionBarContainer2 != null) {
            if ((displayOptions2 & 16384) != 0) {
                z = true;
            }
            actionBarContainer2.setBlurBackground(z);
        }
    }

    public void setDisplayShowCustomEnabled(boolean z) {
        setDisplayOptions(z ? getBlurOptioons() | 16 : 0, getBlurOptioons() | 16);
    }

    public void setDisplayShowHomeEnabled(boolean z) {
        setDisplayOptions(z ? getBlurOptioons() | 2 : 0, getBlurOptioons() | 2);
    }

    public void setDisplayShowTitleEnabled(boolean z) {
        setDisplayOptions(z ? getBlurOptioons() | 8 : 0, getBlurOptioons() | 8);
    }

    public void setDisplayUseLogoEnabled(boolean z) {
        setDisplayOptions(z ? getBlurOptioons() | 1 : 0, getBlurOptioons() | 1);
    }

    public void setEndView(View view) {
        this.mActionView.setEndView(view);
    }

    public void setExpandState(int i) {
        this.mActionView.setExpandState(i);
    }

    public void setExpandState(int i, boolean z) {
        this.mActionView.setExpandState(i, z);
    }

    public void setExpandTabTextAppearance(int i, int i2) {
        this.mExpanedTabScrollView.setTextAppearance(i, i2);
    }

    public void setFragmentActionMenuAt(int i, boolean z) {
        this.mViewPagerController.setFragmentActionMenuAt(i, z);
    }

    public void setFragmentViewPagerMode(Context context, FragmentManager fragmentManager) {
        setFragmentViewPagerMode(context, fragmentManager, false);
    }

    public void setFragmentViewPagerMode(Context context, FragmentManager fragmentManager, boolean z) {
        if (!isFragmentViewPagerMode()) {
            removeAllTabs();
            setNavigationMode(2);
            this.mViewPagerController = new ActionBarViewPagerController(this, fragmentManager, z);
            addOnFragmentViewPagerChangeListener(this.mTabScrollView);
            addOnFragmentViewPagerChangeListener(this.mExpanedTabScrollView);
            addOnFragmentViewPagerChangeListener(this.mSecondaryTabScrollView);
            ActionBarContainer actionBarContainer = this.mSplitView;
            if (actionBarContainer != null) {
                addOnFragmentViewPagerChangeListener(actionBarContainer);
                this.mSplitView.setFragmentViewPagerMode(true);
            }
        }
    }

    public void setHomeButtonEnabled(boolean z) {
        this.mActionView.setHomeButtonEnabled(z);
    }

    public void setIcon(int i) {
        this.mActionView.setIcon(i);
    }

    public void setIcon(Drawable drawable) {
        this.mActionView.setIcon(drawable);
    }

    public void setListNavigationCallbacks(SpinnerAdapter spinnerAdapter, OnNavigationListener onNavigationListener) {
        this.mActionView.setDropdownAdapter(spinnerAdapter);
        this.mActionView.setCallback(onNavigationListener);
    }

    public void setLogo(int i) {
        this.mActionView.setLogo(i);
    }

    public void setLogo(Drawable drawable) {
        this.mActionView.setLogo(drawable);
    }

    public void setNavigationMode(int i) {
        if (this.mActionView.getNavigationMode() == 2) {
            ensureTabsExist();
            this.mSavedTabPosition = getSelectedNavigationIndex();
            selectTab(null);
            this.mTabScrollView.setVisibility(8);
            this.mExpanedTabScrollView.setVisibility(8);
            this.mSecondaryTabScrollView.setVisibility(8);
        }
        this.mActionView.setNavigationMode(i);
        if (i == 2) {
            ensureTabsExist();
            this.mTabScrollView.setVisibility(0);
            this.mExpanedTabScrollView.setVisibility(0);
            this.mSecondaryTabScrollView.setVisibility(0);
            int i2 = this.mSavedTabPosition;
            if (i2 != -1) {
                setSelectedNavigationItem(i2);
                this.mSavedTabPosition = -1;
            }
        }
        this.mActionView.setCollapsable(false);
    }

    public void setProgress(int i) {
        this.mActionView.setProgress(i);
    }

    public void setProgressBarIndeterminate(boolean z) {
        this.mActionView.setProgressBarIndeterminate(z);
    }

    public void setProgressBarIndeterminateVisibility(boolean z) {
        this.mActionView.setProgressBarIndeterminateVisibility(z);
    }

    public void setProgressBarVisibility(boolean z) {
        this.mActionView.setProgressBarVisibility(z);
    }

    public void setResizable(boolean z) {
        this.mActionView.setResizable(z);
    }

    public void setSecondaryTabTextAppearance(int i, int i2) {
        this.mSecondaryTabScrollView.setTextAppearance(i, i2);
    }

    public void setSelectedNavigationItem(int i) {
        int navigationMode = this.mActionView.getNavigationMode();
        if (navigationMode == 1) {
            this.mActionView.setDropdownSelectedPosition(i);
        } else if (navigationMode == 2) {
            selectTab((Tab) this.mTabs.get(i));
        } else {
            throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
        }
    }

    public void setShowHideAnimationEnabled(boolean z) {
        this.mShowHideAnimationEnabled = z;
        if (z) {
            return;
        }
        if (isShowing()) {
            doShow(false);
        } else {
            doHide(false);
        }
    }

    public void setSplitBackgroundDrawable(Drawable drawable) {
        boolean z = (getDisplayOptions() & 16384) != 0;
        if (this.mSplitView != null) {
            for (int i = 0; i < this.mSplitView.getChildCount(); i++) {
                if (this.mSplitView.getChildAt(i) instanceof ActionMenuView) {
                    this.mSplitView.getChildAt(i).setBackground(z ? null : drawable);
                }
            }
        }
    }

    public void setStartView(View view) {
        this.mActionView.setStartView(view);
    }

    public void setSubtitle(int i) {
        setSubtitle((CharSequence) this.mContext.getString(i));
    }

    public void setSubtitle(CharSequence charSequence) {
        this.mActionView.setSubtitle(charSequence);
    }

    public void setTabBadgeVisibility(int i, boolean z) {
        this.mTabScrollView.setBadgeVisibility(i, z);
        this.mExpanedTabScrollView.setBadgeVisibility(i, z);
        this.mSecondaryTabScrollView.setBadgeVisibility(i, z);
    }

    public void setTabIconWithPosition(int i, int i2, int i3, int i4, int i5, int i6) {
        Drawable drawable = null;
        Drawable drawable2 = i3 != 0 ? this.mContext.getDrawable(i3) : null;
        Drawable drawable3 = i4 != 0 ? this.mContext.getDrawable(i4) : null;
        Drawable drawable4 = i5 != 0 ? this.mContext.getDrawable(i5) : null;
        if (i6 != 0) {
            drawable = this.mContext.getDrawable(i6);
        }
        setTabIconWithPosition(i, i2, drawable2, drawable3, drawable4, drawable);
    }

    public void setTabIconWithPosition(int i, int i2, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        this.mTabScrollView.setTabIconWithPosition(i, i2, drawable, drawable2, drawable3, drawable4);
        this.mExpanedTabScrollView.setTabIconWithPosition(i, i2, drawable, drawable2, drawable3, drawable4);
        this.mSecondaryTabScrollView.setTabIconWithPosition(i, i2, drawable, drawable2, drawable3, drawable4);
    }

    public void setTabTextAppearance(int i, int i2) {
        this.mTabScrollView.setTextAppearance(i, i2);
    }

    public void setTabsMode(boolean z) {
        setHasEmbeddedTabs(z);
    }

    public void setTitle(int i) {
        setTitle((CharSequence) this.mContext.getString(i));
    }

    public void setTitle(CharSequence charSequence) {
        this.mActionView.setTitle(charSequence);
    }

    public void setViewPagerDecor(View view) {
        this.mViewPagerController.setViewPagerDecor(view);
    }

    public void setViewPagerOffscreenPageLimit(int i) {
        this.mViewPagerController.setViewPagerOffscreenPageLimit(i);
    }

    public void show() {
        if (this.mHiddenByApp) {
            this.mHiddenByApp = false;
            updateVisibility(false);
        }
    }

    public void showActionBarShadow(boolean z) {
    }

    /* access modifiers changed from: 0000 */
    public void showForActionMode() {
        if (!this.mShowingForMode) {
            boolean z = true;
            this.mShowingForMode = true;
            updateVisibility(false);
            this.mCurrentExpandState = getExpandState();
            this.mCurrentResizable = isResizable();
            ActionModeView actionModeView = this.mActionModeView;
            if (actionModeView instanceof SearchActionModeView) {
                setExpandState(0, true);
                setResizable(false);
            } else {
                ((ActionBarContextView) actionModeView).setExpandState(this.mCurrentExpandState);
                ((ActionBarContextView) this.mActionModeView).setResizable(this.mCurrentResizable);
            }
            this.mCurrentAccessibilityImportant = this.mActionView.getImportantForAccessibility();
            this.mActionView.setImportantForAccessibility(4);
            ActionBarView actionBarView = this.mActionView;
            boolean z2 = this.mActionModeView instanceof SearchActionModeView;
            if ((getDisplayOptions() & 32768) == 0) {
                z = false;
            }
            actionBarView.onActionModeStart(z2, z);
        }
    }

    public void showSplitActionBar(boolean z, boolean z2) {
        if (this.mActionView.isSplitActionBar()) {
            ActionBarContainer actionBarContainer = this.mSplitView;
            if (z) {
                actionBarContainer.show(z2);
            } else {
                actionBarContainer.hide(z2);
            }
        }
    }

    public ActionMode startActionMode(Callback callback) {
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        ActionMode createActionMode = createActionMode(callback);
        if (((this.mActionModeView instanceof SearchActionModeView) && (createActionMode instanceof SearchActionModeImpl)) || ((this.mActionModeView instanceof ActionBarContextView) && (createActionMode instanceof EditActionModeImpl))) {
            this.mActionModeView.closeMode();
            this.mActionModeView.killMode();
        }
        this.mActionModeView = createActionModeView(callback);
        ActionModeView actionModeView = this.mActionModeView;
        if (actionModeView != null) {
            if (createActionMode instanceof ActionModeImpl) {
                ActionModeImpl actionModeImpl = (ActionModeImpl) createActionMode;
                actionModeImpl.setActionModeView(actionModeView);
                actionModeImpl.setActionModeCallback(this.mActionModeCallback);
                if (actionModeImpl.dispatchOnCreate()) {
                    createActionMode.invalidate();
                    this.mActionModeView.initForMode(createActionMode);
                    animateToMode(true);
                    ActionBarContainer actionBarContainer = this.mSplitView;
                    if (!(actionBarContainer == null || this.mContextDisplayMode != 1 || actionBarContainer.getVisibility() == 0)) {
                        this.mSplitView.setVisibility(0);
                    }
                    ActionModeView actionModeView2 = this.mActionModeView;
                    if (actionModeView2 instanceof ActionBarContextView) {
                        ((ActionBarContextView) actionModeView2).sendAccessibilityEvent(32);
                    }
                    this.mActionMode = createActionMode;
                    return createActionMode;
                }
            }
            return null;
        }
        throw new IllegalStateException("not set split, maybe windowSplitActionBar not set true!");
    }
}
