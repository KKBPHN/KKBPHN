package com.miui.internal.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.miui.internal.R;
import com.miui.internal.variable.Android_View_Window_class.Factory;
import com.miui.internal.view.menu.MenuBuilder;
import com.miui.internal.view.menu.MenuPresenter.Callback;
import com.miui.internal.widget.ActionBarContainer;
import com.miui.internal.widget.ActionBarOverlayLayout;
import com.miui.internal.widget.ActionBarView;
import miui.app.ActionBar;
import miui.app.OnStatusBarChangeListener;
import miui.extension.ExtensionManager;
import miui.extension.target.ActivityTarget;
import miui.reflect.Method;
import miui.util.AppConstants;
import miui.util.AttributeResolver;
import miui.view.SearchActionMode;

public class ActivityDelegate extends ActionBarDelegateImpl implements Callback, MenuBuilder.Callback {
    private static final String ACTION_BAR_TAG = "miui:ActionBar";
    private static final Method ON_CREATE_PANEL_MENU = Method.of(Activity.class, "onCreatePanelMenu", "(ILandroid/view/Menu;)Z");
    private static final Method ON_MENU_ITEM_SELECTED = Method.of(Activity.class, "onMenuItemSelected", "(ILandroid/view/MenuItem;)Z");
    private static final Method ON_PREPARE_PANEL = Method.of(Activity.class, "onPreparePanel", "(ILandroid/view/View;Landroid/view/Menu;)Z");
    private ActionBarContainer mActionBarContainer;
    private final Class mActivityClass;
    private final Runnable mInvalidateMenuRunnable = new Runnable() {
        public void run() {
            MenuBuilder createMenu = ActivityDelegate.this.createMenu();
            if (ActivityDelegate.this.isImmersionMenuEnabled() || !ActivityDelegate.this.superOnCreatePanelMenu(0, createMenu) || !ActivityDelegate.this.superOnPreparePanel(0, null, createMenu)) {
                ActivityDelegate.this.setMenu(null);
            } else {
                ActivityDelegate.this.setMenu(createMenu);
            }
        }
    };
    private ActionBarOverlayLayout mSubDecor;

    public ActivityDelegate(Activity activity, Class cls) {
        super(activity);
        this.mActivityClass = cls;
    }

    public static int getDecorViewLayoutRes(Window window) {
        Context context = window.getContext();
        int i = AttributeResolver.resolveBoolean(context, R.attr.windowActionBar, false) ? AttributeResolver.resolveBoolean(context, R.attr.windowActionBarMovable, false) ? R.layout.screen_action_bar_movable : R.layout.screen_action_bar : R.layout.screen_simple;
        int resolve = AttributeResolver.resolve(context, miui.R.attr.startingWindowOverlay);
        if (resolve > 0 && isSystemProcess() && isWindowActionBarEnabled(context)) {
            i = resolve;
        }
        if (!window.isFloating() && (window.getCallback() instanceof Dialog)) {
            Factory.getInstance().get().setTranslucentStatus(window, AttributeResolver.resolveInt(context, R.attr.windowTranslucentStatus, 0));
        }
        return i;
    }

    private static boolean isSystemProcess() {
        return "android".equals(AppConstants.getCurrentApplication().getApplicationInfo().packageName);
    }

    private static boolean isWindowActionBarEnabled(Context context) {
        return AttributeResolver.resolveBoolean(context, miui.R.attr.windowActionBar, true);
    }

    /* access modifiers changed from: private */
    public boolean superOnCreatePanelMenu(int i, Menu menu) {
        return ON_CREATE_PANEL_MENU.invokeBoolean(this.mActivityClass, getActivity(), Integer.valueOf(i), menu);
    }

    private boolean superOnMenuItemSelected(int i, MenuItem menuItem) {
        return ON_MENU_ITEM_SELECTED.invokeBoolean(this.mActivityClass, getActivity(), Integer.valueOf(i), menuItem);
    }

    /* access modifiers changed from: private */
    public boolean superOnPreparePanel(int i, View view, Menu menu) {
        return ON_PREPARE_PANEL.invokeBoolean(this.mActivityClass, getActivity(), Integer.valueOf(i), view, menu);
    }

    public ActionBar createActionBar() {
        return new ActionBarImpl(this.mActivity);
    }

    public Context getThemedContext() {
        return this.mActivity;
    }

    public View getView() {
        return this.mSubDecor;
    }

    /* access modifiers changed from: protected */
    public void installSubDecor() {
        if (!this.mSubDecorInstalled) {
            this.mSubDecorInstalled = true;
            Window window = this.mActivity.getWindow();
            TypedArray obtainStyledAttributes = this.mActivity.obtainStyledAttributes(miui.R.styleable.Window);
            if (obtainStyledAttributes.getInt(miui.R.styleable.Window_windowLayoutMode, 0) == 1) {
                this.mActivity.getWindow().setGravity(80);
            }
            if (obtainStyledAttributes.hasValue(miui.R.styleable.Window_windowActionBar)) {
                if (obtainStyledAttributes.getBoolean(miui.R.styleable.Window_windowActionBar, false)) {
                    requestWindowFeature(8);
                }
                if (obtainStyledAttributes.getBoolean(miui.R.styleable.Window_windowActionBarOverlay, false)) {
                    requestWindowFeature(9);
                }
                setTranslucentStatus(obtainStyledAttributes.getInt(R.styleable.Window_windowTranslucentStatus, 0));
                ViewGroup viewGroup = (ViewGroup) this.mActivity.getWindow().getDecorView();
                View childAt = viewGroup.getChildAt(0);
                ViewGroup viewGroup2 = null;
                if (childAt instanceof ViewGroup) {
                    viewGroup2 = (ViewGroup) childAt;
                }
                if (viewGroup2 instanceof ActionBarOverlayLayout) {
                    this.mSubDecor = (ActionBarOverlayLayout) viewGroup2;
                } else if (viewGroup2 == null || !(viewGroup2.getChildAt(0) instanceof ActionBarOverlayLayout)) {
                    View findViewById = viewGroup.findViewById(16908290);
                    ((ViewGroup) findViewById.getParent()).removeView(findViewById);
                    viewGroup.removeAllViews();
                    View.inflate(this.mActivity, getDecorViewLayoutRes(window), viewGroup);
                    this.mSubDecor = (ActionBarOverlayLayout) viewGroup.getChildAt(0);
                    View findViewById2 = this.mSubDecor.findViewById(16908290);
                    int indexOfChild = this.mSubDecor.indexOfChild(findViewById2);
                    this.mSubDecor.removeView(findViewById2);
                    this.mSubDecor.addView(findViewById, indexOfChild);
                    this.mSubDecor.setContentView(findViewById);
                } else {
                    this.mSubDecor = (ActionBarOverlayLayout) viewGroup2.getChildAt(0);
                }
                this.mSubDecor.setCallback(this.mActivity);
                this.mSubDecor.setTranslucentStatus(getTranslucentStatus());
                if (this.mHasActionBar) {
                    this.mActionBarContainer = (ActionBarContainer) this.mSubDecor.findViewById(R.id.action_bar_container);
                    this.mSubDecor.setOverlayMode(this.mOverlayActionBar);
                    this.mActionBarView = (ActionBarView) this.mSubDecor.findViewById(R.id.action_bar);
                    this.mActionBarView.setWindowCallback(this.mActivity);
                    if (this.mFeatureProgress) {
                        this.mActionBarView.initProgress();
                    }
                    if (this.mFeatureIndeterminateProgress) {
                        this.mActionBarView.initIndeterminateProgress();
                    }
                    this.mImmersionLayoutResourceId = obtainStyledAttributes.getResourceId(R.styleable.Window_immersionMenuLayout, 0);
                    if (isImmersionMenuEnabled()) {
                        this.mActionBarView.initImmersionMore(this.mImmersionLayoutResourceId, this);
                    }
                    if (this.mActionBarView.getCustomNavigationView() != null) {
                        ActionBarView actionBarView = this.mActionBarView;
                        actionBarView.setDisplayOptions(actionBarView.getDisplayOptions() | 16);
                    }
                    boolean equals = ActionBarDelegateImpl.UI_OPTION_SPLIT_ACTION_BAR_WHEN_NARROW.equals(getUiOptionsFromMetadata());
                    boolean z = equals ? this.mActivity.getResources().getBoolean(R.bool.abc_split_action_bar_is_narrow) : obtainStyledAttributes.getBoolean(miui.R.styleable.Window_windowSplitActionBar, false);
                    if (z) {
                        addSplitActionBar(z, equals, this.mSubDecor);
                    }
                    this.mActivity.getWindow().getDecorView().post(this.mInvalidateMenuRunnable);
                }
                if (obtainStyledAttributes.getBoolean(R.styleable.Window_immersionMenuEnabled, false)) {
                    setImmersionMenuEnabled(true);
                }
                obtainStyledAttributes.recycle();
                return;
            }
            obtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a miui theme (or descendant) with this activity.");
        }
    }

    public void invalidateOptionsMenu() {
        this.mInvalidateMenuRunnable.run();
    }

    public void onActionModeFinished(ActionMode actionMode) {
        this.mActionMode = null;
    }

    public void onActionModeStarted(ActionMode actionMode) {
        this.mActionMode = actionMode;
    }

    public boolean onBackPressed() {
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
            return true;
        }
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView == null || !actionBarView.hasExpandedActionView()) {
            return false;
        }
        this.mActionBarView.collapseActionView();
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        installSubDecor();
        ExtensionManager.getInstance().invoke(ActivityTarget.TARGET, ActivityTarget.ACTION_ON_CREATE, this.mActivity, bundle);
    }

    /* access modifiers changed from: protected */
    public boolean onCreateImmersionMenu(MenuBuilder menuBuilder) {
        return this.mActivity.onCreateOptionsMenu(menuBuilder);
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        return i != 0 && superOnCreatePanelMenu(i, menu);
    }

    public View onCreatePanelView(int i) {
        if (i == 0 && !isImmersionMenuEnabled()) {
            MenuBuilder menuBuilder = this.mMenu;
            boolean z = true;
            if (this.mActionMode == null) {
                if (menuBuilder == null) {
                    menuBuilder = createMenu();
                    setMenu(menuBuilder);
                    menuBuilder.stopDispatchingItemsChanged();
                    z = superOnCreatePanelMenu(0, menuBuilder);
                }
                if (z) {
                    menuBuilder.stopDispatchingItemsChanged();
                    z = superOnPreparePanel(0, null, menuBuilder);
                }
            } else if (menuBuilder == null) {
                z = false;
            }
            if (z) {
                menuBuilder.startDispatchingItemsChanged();
            } else {
                setMenu(null);
            }
        }
        return null;
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        if (!superOnMenuItemSelected(i, menuItem) && i == 0 && menuItem.getItemId() == 16908332 && getActionBar() != null && (getActionBar().getDisplayOptions() & 4) != 0) {
            if (!(this.mActivity.getParent() == null ? this.mActivity.onNavigateUp() : this.mActivity.getParent().onNavigateUpFromChild(this.mActivity))) {
                this.mActivity.finish();
            }
        }
        return true;
    }

    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        return this.mActivity.onMenuItemSelected(0, menuItem);
    }

    public void onPostResume() {
        ActionBarImpl actionBarImpl = (ActionBarImpl) getActionBar();
        if (actionBarImpl != null) {
            actionBarImpl.setShowHideAnimationEnabled(true);
        }
    }

    /* access modifiers changed from: protected */
    public boolean onPrepareImmersionMenu(MenuBuilder menuBuilder) {
        return this.mActivity.onPrepareOptionsMenu(menuBuilder);
    }

    public boolean onPreparePanel(int i, View view, Menu menu) {
        return i != 0 && superOnPreparePanel(i, view, menu);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        if (this.mActionBarContainer != null) {
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray(ACTION_BAR_TAG);
            if (sparseParcelableArray != null) {
                this.mActionBarContainer.restoreHierarchyState(sparseParcelableArray);
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.mActionBarContainer != null) {
            SparseArray sparseArray = new SparseArray();
            this.mActionBarContainer.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray(ACTION_BAR_TAG, sparseArray);
        }
    }

    public void onStop() {
        dismissImmersionMenu(false);
        ActionBarImpl actionBarImpl = (ActionBarImpl) getActionBar();
        if (actionBarImpl != null) {
            actionBarImpl.setShowHideAnimationEnabled(false);
            ActionBarOverlayLayout actionBarOverlayLayout = actionBarImpl.getActionBarOverlayLayout();
            if (actionBarOverlayLayout != null) {
                actionBarOverlayLayout.dismissContextMenu();
            }
        }
    }

    public void onTitleChanged(CharSequence charSequence) {
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setWindowTitle(charSequence);
        }
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (getActionBar() != null) {
            return ((ActionBarImpl) getActionBar()).startActionMode(callback);
        }
        return null;
    }

    public void setOnStatusBarChangeListener(OnStatusBarChangeListener onStatusBarChangeListener) {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setOnStatusBarChangeListener(onStatusBarChangeListener);
        }
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        if (callback instanceof SearchActionMode.Callback) {
            addContentMask(this.mSubDecor);
        }
        return this.mSubDecor.startActionMode(callback);
    }
}
