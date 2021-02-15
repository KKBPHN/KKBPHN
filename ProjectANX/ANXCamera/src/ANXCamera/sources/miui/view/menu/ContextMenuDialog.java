package miui.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.view.MenuItem;
import com.miui.internal.view.menu.MenuBuilder;
import com.miui.internal.view.menu.MenuBuilder.Callback;
import com.miui.internal.view.menu.MenuDialogHelper;
import java.util.ArrayList;
import java.util.List;

public class ContextMenuDialog {
    private ContextMenuBuilder mBuilder;
    private final Context mContext;
    /* access modifiers changed from: private */
    public final List mMenuRunnables = new ArrayList();

    class ContextMenuBuilder extends MenuBuilder {
        public ContextMenuBuilder(Context context) {
            super(context);
        }

        public MenuBuilder setHeaderIcon(int i) {
            super.setHeaderIconInt(i);
            return this;
        }

        public MenuBuilder setHeaderIcon(Drawable drawable) {
            super.setHeaderIconInt(drawable);
            return this;
        }

        public MenuBuilder setHeaderTitle(int i) {
            super.setHeaderTitleInt(i);
            return this;
        }

        public MenuBuilder setHeaderTitle(CharSequence charSequence) {
            super.setHeaderTitleInt(charSequence);
            return this;
        }

        public MenuDialogHelper show(IBinder iBinder) {
            if (getVisibleItems().size() <= 0) {
                return null;
            }
            MenuDialogHelper menuDialogHelper = new MenuDialogHelper(this);
            menuDialogHelper.show(iBinder);
            return menuDialogHelper;
        }
    }

    public ContextMenuDialog(Context context) {
        this.mContext = context;
        this.mBuilder = new ContextMenuBuilder(this.mContext);
        this.mBuilder.setCallback(new Callback() {
            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                ((Runnable) ContextMenuDialog.this.mMenuRunnables.get(menuItem.getItemId())).run();
                return true;
            }

            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }
        });
    }

    public void addMenuItem(int i, Runnable runnable) {
        addMenuItem(this.mContext.getString(i), runnable);
    }

    public void addMenuItem(String str, Runnable runnable) {
        this.mBuilder.add(0, this.mMenuRunnables.size(), 0, (CharSequence) str);
        this.mMenuRunnables.add(runnable);
    }

    public void setIcon(int i) {
        this.mBuilder.setHeaderIcon(i);
    }

    public void setIcon(Drawable drawable) {
        this.mBuilder.setHeaderIcon(drawable);
    }

    public void setTitle(int i) {
        this.mBuilder.setHeaderTitle(i);
    }

    public void setTitle(String str) {
        this.mBuilder.setHeaderTitle((CharSequence) str);
    }

    public void show() {
        this.mBuilder.show(null);
    }
}
