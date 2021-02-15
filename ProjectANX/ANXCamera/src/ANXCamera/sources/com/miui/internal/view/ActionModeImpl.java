package com.miui.internal.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.miui.internal.view.menu.MenuBuilder;
import com.miui.internal.view.menu.MenuBuilder.Callback;
import com.miui.internal.widget.ActionModeView;
import java.lang.ref.WeakReference;
import miui.view.ActionModeAnimationListener;

public class ActionModeImpl extends ActionMode implements Callback, ActionModeAnimationListener {
    public static final int TYPE_FLOATING = 1;
    public static final int TYPE_PRIMARY = 0;
    private ActionModeCallback mActionModeCallback;
    protected WeakReference mActionModeView;
    private ActionMode.Callback mCallback;
    protected Context mContext;
    boolean mFinishing = false;
    private MenuBuilder mMenu;

    public interface ActionModeCallback {
        void onActionModeFinish(ActionMode actionMode);
    }

    public ActionModeImpl(Context context, ActionMode.Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
        this.mMenu = new MenuBuilder(context).setDefaultShowAsAction(1);
        this.mMenu.setCallback(this);
    }

    public boolean dispatchOnCreate() {
        this.mMenu.stopDispatchingItemsChanged();
        try {
            boolean onCreateActionMode = this.mCallback.onCreateActionMode(this, this.mMenu);
            return onCreateActionMode;
        } finally {
            this.mMenu.startDispatchingItemsChanged();
        }
    }

    public void finish() {
        if (!this.mFinishing) {
            this.mFinishing = true;
            ((ActionModeView) this.mActionModeView.get()).closeMode();
            ActionModeCallback actionModeCallback = this.mActionModeCallback;
            if (actionModeCallback != null) {
                actionModeCallback.onActionModeFinish(this);
            }
            ActionMode.Callback callback = this.mCallback;
            if (callback != null) {
                callback.onDestroyActionMode(this);
                this.mCallback = null;
            }
        }
    }

    public View getCustomView() {
        throw new UnsupportedOperationException("getCustomView not supported");
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public MenuInflater getMenuInflater() {
        return new MenuInflater(this.mContext);
    }

    public CharSequence getSubtitle() {
        throw new UnsupportedOperationException("getSubtitle not supported");
    }

    public CharSequence getTitle() {
        throw new UnsupportedOperationException("getTitle not supported");
    }

    public void invalidate() {
        this.mMenu.stopDispatchingItemsChanged();
        try {
            this.mCallback.onPrepareActionMode(this, this.mMenu);
        } finally {
            this.mMenu.startDispatchingItemsChanged();
        }
    }

    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        ActionMode.Callback callback = this.mCallback;
        return callback != null && callback.onActionItemClicked(this, menuItem);
    }

    public void onMenuModeChange(MenuBuilder menuBuilder) {
        if (this.mCallback != null) {
            invalidate();
        }
    }

    public void onStart(boolean z) {
    }

    public void onStop(boolean z) {
        if (!z) {
            ActionMode.Callback callback = this.mCallback;
            if (callback != null) {
                callback.onDestroyActionMode(this);
                this.mCallback = null;
            }
        }
    }

    public void onUpdate(boolean z, float f) {
    }

    public void setActionModeCallback(ActionModeCallback actionModeCallback) {
        this.mActionModeCallback = actionModeCallback;
    }

    public void setActionModeView(ActionModeView actionModeView) {
        actionModeView.addAnimationListener(this);
        this.mActionModeView = new WeakReference(actionModeView);
    }

    public void setCustomView(View view) {
        throw new UnsupportedOperationException("setCustomView not supported");
    }

    public void setSubtitle(int i) {
        throw new UnsupportedOperationException("setSubTitle not supported");
    }

    public void setSubtitle(CharSequence charSequence) {
        throw new UnsupportedOperationException("setSubTitle not supported");
    }

    public void setTitle(int i) {
        throw new UnsupportedOperationException("setTitle not supported");
    }

    public void setTitle(CharSequence charSequence) {
        throw new UnsupportedOperationException("setTitle not supported");
    }
}
