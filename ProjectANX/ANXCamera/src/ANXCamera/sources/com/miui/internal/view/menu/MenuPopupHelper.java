package com.miui.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow.OnDismissListener;
import com.miui.internal.R;
import com.miui.internal.view.menu.MenuPresenter.Callback;
import com.miui.internal.view.menu.MenuView.ItemView;
import java.util.ArrayList;
import miui.widget.ListPopupWindow;

public class MenuPopupHelper implements OnItemClickListener, OnKeyListener, OnGlobalLayoutListener, OnDismissListener, MenuPresenter {
    private static final int ITEM_LAYOUT = R.layout.popup_menu_item_layout;
    private MenuAdapter mAdapter;
    private View mAnchorView;
    private Context mContext;
    boolean mForceShowIcon;
    /* access modifiers changed from: private */
    public LayoutInflater mInflater;
    private ViewGroup mMeasureParent;
    /* access modifiers changed from: private */
    public MenuBuilder mMenu;
    /* access modifiers changed from: private */
    public int mMenuItemLayout;
    /* access modifiers changed from: private */
    public boolean mOverflowOnly;
    private ListPopupWindow mPopup;
    private int mPopupMaxWidth;
    private int mPopupVerticalOffset;
    private Callback mPresenterCallback;
    private ViewTreeObserver mTreeObserver;

    class MenuAdapter extends BaseAdapter {
        /* access modifiers changed from: private */
        public MenuBuilder mAdapterMenu;
        private int mExpandedIndex = -1;

        public MenuAdapter(MenuBuilder menuBuilder) {
            this.mAdapterMenu = menuBuilder;
            findExpandedIndex();
        }

        /* access modifiers changed from: 0000 */
        public void findExpandedIndex() {
            MenuItemImpl expandedItem = MenuPopupHelper.this.mMenu.getExpandedItem();
            if (expandedItem != null) {
                ArrayList nonActionItems = MenuPopupHelper.this.mMenu.getNonActionItems();
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
            ArrayList nonActionItems = MenuPopupHelper.this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
            return this.mExpandedIndex < 0 ? nonActionItems.size() : nonActionItems.size() - 1;
        }

        public MenuItemImpl getItem(int i) {
            ArrayList nonActionItems = MenuPopupHelper.this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
            int i2 = this.mExpandedIndex;
            if (i2 >= 0 && i >= i2) {
                i++;
            }
            return (MenuItemImpl) nonActionItems.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = MenuPopupHelper.this.mInflater.inflate(MenuPopupHelper.this.mMenuItemLayout, viewGroup, false);
            }
            ItemView itemView = (ItemView) view;
            if (MenuPopupHelper.this.mForceShowIcon) {
                ((ListMenuItemView) view).setForceShowIcon(true);
            }
            itemView.initialize(getItem(i), 0);
            return view;
        }

        public void notifyDataSetChanged() {
            findExpandedIndex();
            super.notifyDataSetChanged();
        }
    }

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder) {
        this(context, menuBuilder, null, false);
    }

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view) {
        this(context, menuBuilder, view, false);
    }

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view, boolean z) {
        this.mMenuItemLayout = ITEM_LAYOUT;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMenu = menuBuilder;
        this.mOverflowOnly = z;
        Resources resources = context.getResources();
        this.mPopupMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R.dimen.config_prefDialogWidth));
        this.mAnchorView = view;
        menuBuilder.addMenuPresenter(this);
    }

    private int measureContentWidth(ListAdapter listAdapter) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(this.mPopupMaxWidth, Integer.MIN_VALUE);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(this.mPopupMaxWidth, Integer.MIN_VALUE);
        int count = listAdapter.getCount();
        View view = null;
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < count; i3++) {
            int itemViewType = listAdapter.getItemViewType(i3);
            if (itemViewType != i2) {
                view = null;
                i2 = itemViewType;
            }
            if (this.mMeasureParent == null) {
                this.mMeasureParent = new FrameLayout(this.mContext);
            }
            view = listAdapter.getView(i3, view, this.mMeasureParent);
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            i = Math.max(i, view.getMeasuredWidth());
        }
        return i;
    }

    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public void dismiss(boolean z) {
        if (isShowing()) {
            this.mPopup.dismiss(z);
        }
    }

    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public boolean flagActionItems() {
        return false;
    }

    public int getId() {
        return 0;
    }

    public MenuView getMenuView(ViewGroup viewGroup) {
        throw new UnsupportedOperationException("MenuPopupHelpers manage their own views");
    }

    public void initForMenu(Context context, MenuBuilder menuBuilder) {
    }

    public boolean isShowing() {
        ListPopupWindow listPopupWindow = this.mPopup;
        return listPopupWindow != null && listPopupWindow.isShowing();
    }

    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        if (menuBuilder == this.mMenu) {
            dismiss(true);
            Callback callback = this.mPresenterCallback;
            if (callback != null) {
                callback.onCloseMenu(menuBuilder, z);
            }
        }
    }

    public void onDismiss() {
        this.mPopup = null;
        this.mMenu.close();
        ViewTreeObserver viewTreeObserver = this.mTreeObserver;
        if (viewTreeObserver != null) {
            if (!viewTreeObserver.isAlive()) {
                this.mTreeObserver = this.mAnchorView.getViewTreeObserver();
            }
            this.mTreeObserver.removeOnGlobalLayoutListener(this);
            this.mTreeObserver = null;
        }
    }

    public void onGlobalLayout() {
        if (isShowing()) {
            View view = this.mAnchorView;
            if (view == null || !view.isShown()) {
                dismiss(false);
            } else if (isShowing()) {
                this.mPopup.setContentWidth(Math.min(measureContentWidth(this.mAdapter), this.mPopupMaxWidth));
                this.mPopup.show();
            }
        }
    }

    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        MenuAdapter menuAdapter = this.mAdapter;
        menuAdapter.mAdapterMenu.performItemAction(menuAdapter.getItem(i), 0);
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || i != 82) {
            return false;
        }
        dismiss(false);
        return true;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
    }

    public Parcelable onSaveInstanceState() {
        return null;
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        boolean z;
        if (subMenuBuilder.hasVisibleItems()) {
            MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this.mContext, subMenuBuilder, this.mAnchorView, false);
            menuPopupHelper.setCallback(this.mPresenterCallback);
            int size = subMenuBuilder.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    z = false;
                    break;
                }
                MenuItem item = subMenuBuilder.getItem(i);
                if (item.isVisible() && item.getIcon() != null) {
                    z = true;
                    break;
                }
                i++;
            }
            menuPopupHelper.setForceShowIcon(z);
            if (menuPopupHelper.tryShow()) {
                Callback callback = this.mPresenterCallback;
                if (callback != null) {
                    callback.onOpenSubMenu(subMenuBuilder);
                }
                return true;
            }
        }
        return false;
    }

    public void setAnchorView(View view) {
        this.mAnchorView = view;
    }

    public void setCallback(Callback callback) {
        this.mPresenterCallback = callback;
    }

    public void setForceShowIcon(boolean z) {
        this.mForceShowIcon = z;
    }

    public void setMenuItemLayout(int i) {
        this.mMenuItemLayout = i;
    }

    public void setVerticalOffset(int i) {
        this.mPopupVerticalOffset = i;
    }

    public void show() {
        if (!tryShow()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    public boolean tryShow() {
        this.mPopup = new ListPopupWindow(this.mContext, null, 16843520);
        this.mPopup.setOnDismissListener(this);
        this.mPopup.setOnItemClickListener(this);
        this.mPopup.setVerticalOffset(this.mPopupVerticalOffset);
        this.mAdapter = new MenuAdapter(this.mMenu);
        this.mPopup.setAdapter(this.mAdapter);
        this.mPopup.setModal(true);
        View view = this.mAnchorView;
        boolean z = false;
        if (view == null) {
            return false;
        }
        if (this.mTreeObserver == null) {
            z = true;
        }
        this.mTreeObserver = view.getViewTreeObserver();
        if (z) {
            this.mTreeObserver.addOnGlobalLayoutListener(this);
        }
        this.mPopup.setAnchorView(view);
        this.mPopup.setContentWidth(Math.min(measureContentWidth(this.mAdapter), this.mPopupMaxWidth));
        this.mPopup.setInputMethodMode(2);
        this.mPopup.show();
        this.mPopup.getListView().setOnKeyListener(this);
        return true;
    }

    public void updateMenuView(boolean z) {
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }
}
