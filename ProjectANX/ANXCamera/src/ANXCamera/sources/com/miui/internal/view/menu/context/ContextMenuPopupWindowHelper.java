package com.miui.internal.view.menu.context;

import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow.OnDismissListener;
import com.miui.internal.view.menu.MenuBuilder;
import com.miui.internal.view.menu.MenuPresenter.Callback;

public class ContextMenuPopupWindowHelper implements OnDismissListener {
    private ContextMenuPopupWindow mContextMenuPopupWindow;
    private MenuBuilder mMenu;
    private Callback mPresenterCallback;

    public ContextMenuPopupWindowHelper(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    public void dismiss() {
        ContextMenuPopupWindow contextMenuPopupWindow = this.mContextMenuPopupWindow;
        if (contextMenuPopupWindow != null) {
            contextMenuPopupWindow.dismiss();
        }
    }

    public void onDismiss() {
        Callback callback = this.mPresenterCallback;
        if (callback != null) {
            callback.onCloseMenu(this.mMenu, true);
        }
        this.mMenu.clearAll();
    }

    public void setPresenterCallback(Callback callback) {
        this.mPresenterCallback = callback;
    }

    public void show(IBinder iBinder, View view, float f, float f2) {
        this.mContextMenuPopupWindow = new ContextMenuPopupWindowImpl(this.mMenu.getContext(), this.mMenu, this);
        this.mContextMenuPopupWindow.show(view, (ViewGroup) view.getParent(), f, f2);
    }
}
