package androidx.appcompat.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.appcompat.R;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ActionMode.Callback;
import androidx.core.view.KeyEventDispatcher;
import androidx.core.view.KeyEventDispatcher.Component;

public class AppCompatDialog extends Dialog implements AppCompatCallback {
    private AppCompatDelegate mDelegate;
    private final Component mKeyDispatcher;

    public AppCompatDialog(Context context) {
        this(context, 0);
    }

    public AppCompatDialog(Context context, int i) {
        super(context, getThemeResId(context, i));
        this.mKeyDispatcher = new Component() {
            public boolean superDispatchKeyEvent(KeyEvent keyEvent) {
                return AppCompatDialog.this.superDispatchKeyEvent(keyEvent);
            }
        };
        AppCompatDelegate delegate = getDelegate();
        delegate.setTheme(getThemeResId(context, i));
        delegate.onCreate(null);
    }

    protected AppCompatDialog(Context context, boolean z, OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
        this.mKeyDispatcher = new Component() {
            public boolean superDispatchKeyEvent(KeyEvent keyEvent) {
                return AppCompatDialog.this.superDispatchKeyEvent(keyEvent);
            }
        };
    }

    private static int getThemeResId(Context context, int i) {
        if (i != 0) {
            return i;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.dialogTheme, typedValue, true);
        return typedValue.resourceId;
    }

    public void addContentView(View view, LayoutParams layoutParams) {
        getDelegate().addContentView(view, layoutParams);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return KeyEventDispatcher.dispatchKeyEvent(this.mKeyDispatcher, getWindow().getDecorView(), this, keyEvent);
    }

    @Nullable
    public View findViewById(@IdRes int i) {
        return getDelegate().findViewById(i);
    }

    public AppCompatDelegate getDelegate() {
        if (this.mDelegate == null) {
            this.mDelegate = AppCompatDelegate.create((Dialog) this, (AppCompatCallback) this);
        }
        return this.mDelegate;
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        getDelegate().installViewFactory();
        super.onCreate(bundle);
        getDelegate().onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    public void onSupportActionModeFinished(ActionMode actionMode) {
    }

    public void onSupportActionModeStarted(ActionMode actionMode) {
    }

    @Nullable
    public ActionMode onWindowStartingSupportActionMode(Callback callback) {
        return null;
    }

    public void setContentView(@LayoutRes int i) {
        getDelegate().setContentView(i);
    }

    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    public void setContentView(View view, LayoutParams layoutParams) {
        getDelegate().setContentView(view, layoutParams);
    }

    public void setTitle(int i) {
        super.setTitle(i);
        getDelegate().setTitle(getContext().getString(i));
    }

    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        getDelegate().setTitle(charSequence);
    }

    /* access modifiers changed from: 0000 */
    public boolean superDispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean supportRequestWindowFeature(int i) {
        return getDelegate().requestWindowFeature(i);
    }
}
