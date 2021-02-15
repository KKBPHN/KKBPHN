package miui.preference;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.miui.internal.R;
import com.miui.internal.app.ActivityDelegate;
import com.miui.internal.variable.Android_App_PreferenceActivity_class.Factory;
import miui.app.ActionBar;
import miui.app.IActivity;
import miui.util.AttributeResolver;

public class PreferenceActivity extends android.preference.PreferenceActivity implements IActivity {
    private ActivityDelegate mDelegate = new ActivityDelegate(this, android.preference.PreferenceActivity.class);

    public void dismissImmersionMenu(boolean z) {
        this.mDelegate.dismissImmersionMenu(z);
    }

    public ActionBar getActionBar() {
        return this.mDelegate.getActionBar();
    }

    public MenuInflater getMenuInflater() {
        return this.mDelegate.getMenuInflater();
    }

    public int getTranslucentStatus() {
        return this.mDelegate.getTranslucentStatus();
    }

    public void invalidateOptionsMenu() {
        this.mDelegate.invalidateOptionsMenu();
    }

    public void onActionModeFinished(ActionMode actionMode) {
        this.mDelegate.onActionModeFinished(actionMode);
    }

    public void onActionModeStarted(ActionMode actionMode) {
        this.mDelegate.onActionModeStarted(actionMode);
    }

    public void onBackPressed() {
        if (!this.mDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDelegate.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDelegate.onCreate(bundle);
        ListView listView = (ListView) findViewById(16908298);
        if (listView != null) {
            listView.setClipToPadding(false);
            listView.setPadding(0, 0, 0, (int) AttributeResolver.resolveDimension(this, R.attr.preferenceScreenPaddingBottom));
            ViewGroup viewGroup = (ViewGroup) listView.getParent();
            if (viewGroup != null) {
                viewGroup.setPadding(0, 0, 0, 0);
            }
        }
        Factory.getInstance().get().onCreate(this, bundle);
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        return this.mDelegate.onCreatePanelMenu(i, menu);
    }

    public View onCreatePanelView(int i) {
        return i == 0 ? this.mDelegate.onCreatePanelView(i) : super.onCreatePanelView(i);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mDelegate.dismissImmersionMenu(false);
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        return this.mDelegate.onMenuItemSelected(i, menuItem);
    }

    /* access modifiers changed from: protected */
    public void onPostResume() {
        super.onPostResume();
        this.mDelegate.onPostResume();
    }

    public boolean onPreparePanel(int i, View view, Menu menu) {
        return this.mDelegate.onPreparePanel(i, view, menu);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mDelegate.onRestoreInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mDelegate.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.mDelegate.onStop();
    }

    /* access modifiers changed from: protected */
    public void onTitleChanged(CharSequence charSequence, int i) {
        super.onTitleChanged(charSequence, i);
        this.mDelegate.onTitleChanged(charSequence);
    }

    public ActionMode onWindowStartingActionMode(Callback callback) {
        return this.mDelegate.onWindowStartingActionMode(callback);
    }

    public boolean requestExtraWindowFeature(int i) {
        return this.mDelegate.requestWindowFeature(i);
    }

    public void setImmersionMenuEnabled(boolean z) {
        this.mDelegate.setImmersionMenuEnabled(z);
    }

    public void setTranslucentStatus(int i) {
        this.mDelegate.setTranslucentStatus(i);
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
}
