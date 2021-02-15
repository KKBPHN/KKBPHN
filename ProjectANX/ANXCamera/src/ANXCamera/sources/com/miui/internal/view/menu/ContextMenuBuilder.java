package com.miui.internal.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.EventLog;
import android.view.ContextMenu;
import android.view.View;
import com.miui.internal.view.menu.context.ContextMenuPopupWindowHelper;

public class ContextMenuBuilder extends MenuBuilder implements ContextMenu {
    public ContextMenuBuilder(Context context) {
        super(context);
    }

    public ContextMenu setHeaderIcon(int i) {
        super.setHeaderIconInt(i);
        return this;
    }

    public ContextMenu setHeaderIcon(Drawable drawable) {
        super.setHeaderIconInt(drawable);
        return this;
    }

    public ContextMenu setHeaderTitle(int i) {
        super.setHeaderTitleInt(i);
        return this;
    }

    public ContextMenu setHeaderTitle(CharSequence charSequence) {
        super.setHeaderTitleInt(charSequence);
        return this;
    }

    public ContextMenu setHeaderView(View view) {
        super.setHeaderViewInt(view);
        return this;
    }

    public MenuDialogHelper show(View view, IBinder iBinder) {
        if (view != null) {
            ContextMenuHelper.createContextMenu(view, this);
        }
        if (getVisibleItems().size() <= 0) {
            return null;
        }
        EventLog.writeEvent(50001, 1);
        MenuDialogHelper menuDialogHelper = new MenuDialogHelper(this);
        menuDialogHelper.show(iBinder);
        return menuDialogHelper;
    }

    public ContextMenuPopupWindowHelper show(View view, IBinder iBinder, float f, float f2) {
        if (view != null) {
            ContextMenuHelper.createContextMenu(view, this);
        }
        if (getVisibleItems().size() <= 0) {
            return null;
        }
        EventLog.writeEvent(50001, 1);
        ContextMenuPopupWindowHelper contextMenuPopupWindowHelper = new ContextMenuPopupWindowHelper(this);
        contextMenuPopupWindowHelper.show(iBinder, view, f, f2);
        return contextMenuPopupWindowHelper;
    }
}
