package com.miui.internal.view.menu;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import com.miui.internal.R;
import com.miui.internal.view.menu.MenuPresenter.Callback;
import com.miui.internal.view.menu.MenuView.ItemView;
import java.util.ArrayList;

public class ListMenuPresenter implements MenuPresenter, OnItemClickListener {
    public static final String VIEWS_TAG = "android:menu:list";
    MenuAdapter mAdapter;
    private Callback mCallback;
    Context mContext;
    private int mId;
    LayoutInflater mInflater;
    /* access modifiers changed from: private */
    public int mItemIndexOffset;
    int mItemLayoutRes;
    MenuBuilder mMenu;
    int mMenuLayout;
    ExpandedMenuView mMenuView;
    int mThemeRes;

    class MenuAdapter extends BaseAdapter {
        private int mExpandedIndex = -1;

        public MenuAdapter() {
            findExpandedIndex();
        }

        /* access modifiers changed from: 0000 */
        public void findExpandedIndex() {
            MenuItemImpl expandedItem = ListMenuPresenter.this.mMenu.getExpandedItem();
            if (expandedItem != null) {
                ArrayList nonActionItems = ListMenuPresenter.this.mMenu.getNonActionItems();
                int size = nonActionItems.size();
                for (int i = 0; i < size; i++) {
                    if (((MenuItemImpl) nonActionItems.get(i)) == expandedItem) {
                        this.mExpandedIndex = i;
                        return;
                    }
                }
            }
            this.mExpandedIndex = -1;
        }

        public int getCount() {
            int size = ListMenuPresenter.this.mMenu.getNonActionItems().size() - ListMenuPresenter.this.mItemIndexOffset;
            return this.mExpandedIndex < 0 ? size : size - 1;
        }

        public MenuItemImpl getItem(int i) {
            ArrayList nonActionItems = ListMenuPresenter.this.mMenu.getNonActionItems();
            int access$000 = i + ListMenuPresenter.this.mItemIndexOffset;
            int i2 = this.mExpandedIndex;
            if (i2 >= 0 && access$000 >= i2) {
                access$000++;
            }
            return (MenuItemImpl) nonActionItems.get(access$000);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                ListMenuPresenter listMenuPresenter = ListMenuPresenter.this;
                view = listMenuPresenter.mInflater.inflate(listMenuPresenter.mItemLayoutRes, viewGroup, false);
            }
            ((ItemView) view).initialize(getItem(i), 0);
            return view;
        }

        public void notifyDataSetChanged() {
            findExpandedIndex();
            super.notifyDataSetChanged();
        }
    }

    public ListMenuPresenter(int i, int i2) {
        this(R.layout.expanded_menu_layout, i, i2);
    }

    public ListMenuPresenter(int i, int i2, int i3) {
        this.mItemLayoutRes = i2;
        this.mMenuLayout = i;
        this.mThemeRes = i3;
    }

    public ListMenuPresenter(Context context, int i, int i2) {
        this(i, i2, 0);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public boolean flagActionItems() {
        return false;
    }

    public ListAdapter getAdapter() {
        if (this.mAdapter == null) {
            this.mAdapter = new MenuAdapter();
        }
        return this.mAdapter;
    }

    public int getId() {
        return this.mId;
    }

    /* access modifiers changed from: 0000 */
    public int getItemIndexOffset() {
        return this.mItemIndexOffset;
    }

    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.mAdapter == null) {
            this.mAdapter = new MenuAdapter();
        }
        if (this.mAdapter.isEmpty()) {
            return null;
        }
        if (this.mMenuView == null) {
            this.mMenuView = (ExpandedMenuView) this.mInflater.inflate(this.mMenuLayout, viewGroup, false);
            this.mMenuView.setAdapter(this.mAdapter);
            this.mMenuView.setOnItemClickListener(this);
        }
        return this.mMenuView;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001c, code lost:
        if (r2.mInflater == null) goto L_0x000b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        MenuBuilder menuBuilder2;
        MenuAdapter menuAdapter;
        int i = this.mThemeRes;
        if (i != 0) {
            this.mContext = new ContextThemeWrapper(context, i);
        } else {
            if (this.mContext != null) {
                this.mContext = context;
            }
            menuBuilder2 = this.mMenu;
            if (menuBuilder2 != null) {
                menuBuilder2.removeMenuPresenter(this);
            }
            this.mMenu = menuBuilder;
            menuAdapter = this.mAdapter;
            if (menuAdapter == null) {
                menuAdapter.notifyDataSetChanged();
                return;
            }
            return;
        }
        this.mInflater = LayoutInflater.from(this.mContext);
        menuBuilder2 = this.mMenu;
        if (menuBuilder2 != null) {
        }
        this.mMenu = menuBuilder;
        menuAdapter = this.mAdapter;
        if (menuAdapter == null) {
        }
    }

    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, z);
        }
    }

    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        this.mMenu.performItemAction(this.mAdapter.getItem(i), 0);
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        restoreHierarchyState((Bundle) parcelable);
    }

    public Parcelable onSaveInstanceState() {
        if (this.mMenuView == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        saveHierarchyState(bundle);
        return bundle;
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        new MenuDialogHelper(subMenuBuilder).show(null);
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onOpenSubMenu(subMenuBuilder);
        }
        return true;
    }

    public void restoreHierarchyState(Bundle bundle) {
        SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
        if (sparseParcelableArray != null) {
            this.mMenuView.restoreHierarchyState(sparseParcelableArray);
        }
    }

    public void saveHierarchyState(Bundle bundle) {
        SparseArray sparseArray = new SparseArray();
        ExpandedMenuView expandedMenuView = this.mMenuView;
        if (expandedMenuView != null) {
            expandedMenuView.saveHierarchyState(sparseArray);
        }
        bundle.putSparseParcelableArray("android:menu:list", sparseArray);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setId(int i) {
        this.mId = i;
    }

    public void setItemIndexOffset(int i) {
        this.mItemIndexOffset = i;
        if (this.mMenuView != null) {
            updateMenuView(false);
        }
    }

    public void updateMenuView(boolean z) {
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }
}
