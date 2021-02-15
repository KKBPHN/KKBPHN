package com.miui.internal.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import com.miui.internal.R;
import com.miui.internal.variable.Android_View_Window_class.Factory;
import com.miui.internal.view.menu.ImmersionMenuPopupWindow;
import com.miui.internal.view.menu.ImmersionMenuPopupWindowImpl;
import com.miui.internal.view.menu.MenuBuilder;
import com.miui.internal.view.menu.MenuPresenter.Callback;
import com.miui.internal.widget.ActionBarContainer;
import com.miui.internal.widget.ActionBarContextView;
import com.miui.internal.widget.ActionBarOverlayLayout;
import com.miui.internal.widget.ActionBarView;
import miui.app.ActionBar;

public abstract class ActionBarDelegateImpl implements ActionBarDelegate, Callback, MenuBuilder.Callback {
    static final String METADATA_UI_OPTIONS = "android.support.UI_OPTIONS";
    private static final String TAG = "ActionBarDelegate";
    public static final String UI_OPTION_SPLIT_ACTION_BAR_WHEN_NARROW = "splitActionBarWhenNarrow";
    private boolean hasAddSplitActionBar = false;
    private ActionBar mActionBar;
    protected ActionBarView mActionBarView;
    protected ActionMode mActionMode;
    final Activity mActivity;
    protected boolean mFeatureIndeterminateProgress;
    protected boolean mFeatureProgress;
    boolean mHasActionBar;
    protected int mImmersionLayoutResourceId;
    private MenuBuilder mImmersionMenu;
    private boolean mImmersionMenuEnabled;
    protected MenuBuilder mMenu;
    private MenuInflater mMenuInflater;
    private ImmersionMenuPopupWindow mMenuPopupWindow;
    boolean mOverlayActionBar;
    protected boolean mSubDecorInstalled;
    private int mTranslucentStatus = 0;

    ActionBarDelegateImpl(Activity activity) {
        this.mActivity = activity;
    }

    public void addContentMask(ActionBarOverlayLayout actionBarOverlayLayout) {
        if (actionBarOverlayLayout instanceof ActionBarOverlayLayout) {
            ViewStub viewStub = (ViewStub) actionBarOverlayLayout.findViewById(R.id.content_mask_vs);
            actionBarOverlayLayout.setContentMask(viewStub != null ? viewStub.inflate() : actionBarOverlayLayout.findViewById(R.id.content_mask));
        }
    }

    public void addSplitActionBar(boolean z, boolean z2, ActionBarOverlayLayout actionBarOverlayLayout) {
        if (!this.hasAddSplitActionBar) {
            this.hasAddSplitActionBar = true;
            ViewStub viewStub = (ViewStub) actionBarOverlayLayout.findViewById(R.id.split_action_bar_vs);
            ActionBarContainer actionBarContainer = (ActionBarContainer) (viewStub != null ? viewStub.inflate() : actionBarOverlayLayout.findViewById(R.id.split_action_bar));
            if (actionBarContainer != null) {
                this.mActionBarView.setSplitView(actionBarContainer);
                this.mActionBarView.setSplitActionBar(z);
                this.mActionBarView.setSplitWhenNarrow(z2);
                ViewStub viewStub2 = (ViewStub) actionBarOverlayLayout.findViewById(R.id.action_context_bar_vs);
                ActionBarContextView actionBarContextView = (ActionBarContextView) (viewStub2 != null ? viewStub2.inflate() : actionBarOverlayLayout.findViewById(R.id.action_context_bar));
                actionBarContainer.setActionBarContextView(actionBarContextView);
                actionBarContextView.setSplitView(actionBarContainer);
                actionBarContextView.setSplitActionBar(z);
                actionBarContextView.setSplitWhenNarrow(z2);
                actionBarOverlayLayout.setSplitActionBarView(actionBarContainer);
                actionBarOverlayLayout.setActionBarContextView(actionBarContextView);
                addContentMask(actionBarOverlayLayout);
            }
        }
    }

    /* access modifiers changed from: protected */
    public MenuBuilder createMenu() {
        MenuBuilder menuBuilder = new MenuBuilder(getActionBarThemedContext());
        menuBuilder.setCallback(this);
        return menuBuilder;
    }

    public void dismissImmersionMenu(boolean z) {
        ImmersionMenuPopupWindow immersionMenuPopupWindow = this.mMenuPopupWindow;
        if (immersionMenuPopupWindow != null) {
            immersionMenuPopupWindow.dismiss(z);
        }
    }

    public final ActionBar getActionBar() {
        ActionBar actionBar;
        if (this.mHasActionBar || this.mOverlayActionBar) {
            if (this.mActionBar == null) {
                actionBar = createActionBar();
            }
            return this.mActionBar;
        }
        actionBar = null;
        this.mActionBar = actionBar;
        return this.mActionBar;
    }

    /* access modifiers changed from: protected */
    public final Context getActionBarThemedContext() {
        Activity activity = this.mActivity;
        ActionBar actionBar = getActionBar();
        return actionBar != null ? actionBar.getThemedContext() : activity;
    }

    public Activity getActivity() {
        return this.mActivity;
    }

    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                this.mMenuInflater = new MenuInflater(actionBar.getThemedContext());
            } else {
                this.mMenuInflater = new MenuInflater(this.mActivity);
            }
        }
        return this.mMenuInflater;
    }

    public abstract Context getThemedContext();

    public int getTranslucentStatus() {
        return this.mTranslucentStatus;
    }

    public final String getUiOptionsFromMetadata() {
        String str = null;
        try {
            ActivityInfo activityInfo = this.mActivity.getPackageManager().getActivityInfo(this.mActivity.getComponentName(), 128);
            if (activityInfo.metaData != null) {
                str = activityInfo.metaData.getString(METADATA_UI_OPTIONS);
            }
            return str;
        } catch (NameNotFoundException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("getUiOptionsFromMetadata: Activity '");
            sb.append(this.mActivity.getClass().getSimpleName());
            sb.append("' not in manifest");
            Log.e(TAG, sb.toString());
            return null;
        }
    }

    public abstract View getView();

    public boolean isImmersionMenuEnabled() {
        return this.mImmersionMenuEnabled;
    }

    public void onActionModeFinished(ActionMode actionMode) {
    }

    public void onActionModeStarted(ActionMode actionMode) {
    }

    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        this.mActivity.closeOptionsMenu();
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            ((ActionBarImpl) getActionBar()).onConfigurationChanged(configuration);
        }
    }

    public void onCreate(Bundle bundle) {
    }

    public abstract boolean onCreateImmersionMenu(MenuBuilder menuBuilder);

    public abstract /* synthetic */ boolean onMenuItemSelected(int i, MenuItem menuItem);

    public void onMenuModeChange(MenuBuilder menuBuilder) {
        reopenMenu(menuBuilder, true);
    }

    public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
        return false;
    }

    public void onPostResume() {
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            ActionBarImpl actionBarImpl = (ActionBarImpl) getActionBar();
            if (actionBarImpl != null) {
                actionBarImpl.setShowHideAnimationEnabled(true);
            }
        }
    }

    public abstract boolean onPrepareImmersionMenu(MenuBuilder menuBuilder);

    public void onStop() {
        dismissImmersionMenu(false);
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            ActionBarImpl actionBarImpl = (ActionBarImpl) getActionBar();
            if (actionBarImpl != null) {
                actionBarImpl.setShowHideAnimationEnabled(false);
            }
        }
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void reopenMenu(MenuBuilder menuBuilder, boolean z) {
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView == null || !actionBarView.isOverflowReserved()) {
            menuBuilder.close();
            return;
        }
        if (this.mActionBarView.isOverflowMenuShowing() && z) {
            this.mActionBarView.hideOverflowMenu();
        } else if (this.mActionBarView.getVisibility() == 0) {
            this.mActionBarView.showOverflowMenu();
        }
    }

    public boolean requestWindowFeature(int i) {
        if (i == 2) {
            this.mFeatureProgress = true;
            return true;
        } else if (i == 5) {
            this.mFeatureIndeterminateProgress = true;
            return true;
        } else if (i == 8) {
            this.mHasActionBar = true;
            return true;
        } else if (i != 9) {
            return this.mActivity.requestWindowFeature(i);
        } else {
            this.mOverlayActionBar = true;
            return true;
        }
    }

    public void setImmersionMenuEnabled(boolean z) {
        this.mImmersionMenuEnabled = z;
        if (this.mSubDecorInstalled && this.mHasActionBar) {
            if (!z) {
                this.mActionBarView.hideImmersionMore();
            } else if (!this.mActionBarView.showImmersionMore()) {
                this.mActionBarView.initImmersionMore(this.mImmersionLayoutResourceId, this);
            }
            invalidateOptionsMenu();
        }
    }

    /* access modifiers changed from: protected */
    public void setMenu(MenuBuilder menuBuilder) {
        if (menuBuilder != null && menuBuilder.size() > 0 && !this.hasAddSplitActionBar) {
            boolean equals = UI_OPTION_SPLIT_ACTION_BAR_WHEN_NARROW.equals(getUiOptionsFromMetadata());
            View view = getView();
            if (view instanceof ActionBarOverlayLayout) {
                addSplitActionBar(true, equals, (ActionBarOverlayLayout) view);
            }
        }
        if (menuBuilder != this.mMenu) {
            this.mMenu = menuBuilder;
            ActionBarView actionBarView = this.mActionBarView;
            if (actionBarView != null) {
                actionBarView.setMenu(menuBuilder, this);
            }
        }
    }

    public void setTranslucentStatus(int i) {
        int integer = this.mActivity.getResources().getInteger(R.integer.window_translucent_status);
        if (integer >= 0 && integer <= 2) {
            i = integer;
        }
        if (this.mTranslucentStatus != i && Factory.getInstance().get().setTranslucentStatus(this.mActivity.getWindow(), i)) {
            this.mTranslucentStatus = i;
        }
    }

    public void showImmersionMenu() {
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            View findViewById = actionBarView.findViewById(R.id.more);
            if (findViewById != null) {
                showImmersionMenu(findViewById, this.mActionBarView);
                return;
            }
        }
        throw new IllegalStateException("Can't find anchor view in actionbar. Do you use default actionbar and immersion menu is enabled?");
    }

    public void showImmersionMenu(View view, ViewGroup viewGroup) {
        if (!this.mImmersionMenuEnabled) {
            Log.w(TAG, "Try to show immersion menu when immersion menu disabled");
        } else if (view != null) {
            if (this.mImmersionMenu == null) {
                this.mImmersionMenu = createMenu();
                onCreateImmersionMenu(this.mImmersionMenu);
            }
            if (onPrepareImmersionMenu(this.mImmersionMenu) && this.mImmersionMenu.hasVisibleItems()) {
                ImmersionMenuPopupWindow immersionMenuPopupWindow = this.mMenuPopupWindow;
                if (immersionMenuPopupWindow == null) {
                    this.mMenuPopupWindow = new ImmersionMenuPopupWindowImpl(this, this.mImmersionMenu);
                } else {
                    immersionMenuPopupWindow.update(this.mImmersionMenu);
                }
                if (!this.mMenuPopupWindow.isShowing()) {
                    this.mMenuPopupWindow.show(view, viewGroup);
                }
            }
        } else {
            throw new IllegalArgumentException("You must specify a valid anchor view");
        }
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return null;
    }
}
