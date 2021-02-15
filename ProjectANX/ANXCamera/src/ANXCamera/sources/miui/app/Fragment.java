package miui.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.internal.app.FragmentDelegate;

public class Fragment extends android.app.Fragment implements IFragment {
    private FragmentDelegate mDelegate;
    private boolean mHasMenu = true;
    private boolean mMenuVisible = true;

    public void dismissImmersionMenu(boolean z) {
        this.mDelegate.dismissImmersionMenu(z);
    }

    public ActionBar getActionBar() {
        return this.mDelegate.getActionBar();
    }

    public MenuInflater getMenuInflater() {
        return this.mDelegate.getMenuInflater();
    }

    public Context getThemedContext() {
        return this.mDelegate.getThemedContext();
    }

    public View getView() {
        return this.mDelegate.getView();
    }

    public void invalidateOptionsMenu() {
        FragmentDelegate fragmentDelegate = this.mDelegate;
        if (fragmentDelegate != null) {
            fragmentDelegate.updateOptionsMenu(1);
            if (!isHidden() && this.mHasMenu && !this.mDelegate.isImmersionMenuEnabled() && this.mMenuVisible && isAdded()) {
                this.mDelegate.invalidateOptionsMenu();
            }
        }
    }

    public final void onActionModeFinished(ActionMode actionMode) {
    }

    public final void onActionModeStarted(ActionMode actionMode) {
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDelegate.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDelegate = new FragmentDelegate(this);
        this.mDelegate.onCreate(bundle);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        if (i != 0 || !this.mHasMenu || this.mDelegate.isImmersionMenuEnabled() || !this.mMenuVisible || isHidden() || !isAdded()) {
            return false;
        }
        return onCreateOptionsMenu(menu);
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.mDelegate.onCreateView(viewGroup, bundle);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mDelegate.dismissImmersionMenu(false);
    }

    public final void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            FragmentDelegate fragmentDelegate = this.mDelegate;
            if (fragmentDelegate != null) {
                fragmentDelegate.invalidateOptionsMenu();
            }
        }
        onVisibilityChanged(!z);
    }

    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return null;
    }

    public void onPreparePanel(int i, View view, Menu menu) {
        if (i == 0 && this.mHasMenu && !this.mDelegate.isImmersionMenuEnabled() && this.mMenuVisible && !isHidden() && isAdded()) {
            onPrepareOptionsMenu(menu);
        }
    }

    public void onResume() {
        super.onResume();
        this.mDelegate.onPostResume();
    }

    public void onStop() {
        super.onStop();
        this.mDelegate.onStop();
    }

    public void onVisibilityChanged(boolean z) {
    }

    public boolean requestWindowFeature(int i) {
        return this.mDelegate.requestWindowFeature(i);
    }

    public void setHasOptionsMenu(boolean z) {
        super.setHasOptionsMenu(z);
        if (this.mHasMenu != z) {
            this.mHasMenu = z;
            if (this.mHasMenu) {
                FragmentDelegate fragmentDelegate = this.mDelegate;
                if (fragmentDelegate != null && !fragmentDelegate.isImmersionMenuEnabled() && !isHidden() && isAdded()) {
                    this.mDelegate.invalidateOptionsMenu();
                }
            }
        }
    }

    public void setImmersionMenuEnabled(boolean z) {
        this.mDelegate.setImmersionMenuEnabled(z);
    }

    public void setMenuVisibility(boolean z) {
        super.setMenuVisibility(z);
        if (this.mMenuVisible != z) {
            this.mMenuVisible = z;
            if (!isHidden() && isAdded()) {
                FragmentDelegate fragmentDelegate = this.mDelegate;
                if (fragmentDelegate != null) {
                    fragmentDelegate.invalidateOptionsMenu();
                }
            }
        }
    }

    public void setOnStatusBarChangeListener(OnStatusBarChangeListener onStatusBarChangeListener) {
        this.mDelegate.setOnStatusBarChangeListener(onStatusBarChangeListener);
    }

    public void setThemeRes(int i) {
        this.mDelegate.setExtraThemeRes(i);
    }

    public void showImmersionMenu() {
        this.mDelegate.showImmersionMenu();
    }

    public void showImmersionMenu(View view, ViewGroup viewGroup) {
        this.mDelegate.showImmersionMenu(view, viewGroup);
    }

    public ActionMode startActionMode(Callback callback) {
        return this.mDelegate.startActionMode(callback);
    }

    public void updateOptionsMenuContent() {
        if (this.mDelegate != null && !isHidden() && this.mHasMenu && !this.mDelegate.isImmersionMenuEnabled() && this.mMenuVisible && isAdded()) {
            this.mDelegate.invalidateOptionsMenu();
        }
    }
}
