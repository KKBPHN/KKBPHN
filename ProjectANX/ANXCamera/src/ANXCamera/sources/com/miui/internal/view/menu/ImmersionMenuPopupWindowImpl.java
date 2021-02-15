package com.miui.internal.view.menu;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import com.miui.internal.app.ActionBarDelegateImpl;
import miui.widget.ImmersionListPopupWindow;

public class ImmersionMenuPopupWindowImpl extends ImmersionListPopupWindow implements ImmersionMenuPopupWindow {
    /* access modifiers changed from: private */
    public ActionBarDelegateImpl mActionBarDelegate;
    /* access modifiers changed from: private */
    public ImmersionMenuAdapter mAdapter;
    /* access modifiers changed from: private */
    public View mLastAnchor;
    /* access modifiers changed from: private */
    public ViewGroup mLastParent;

    public ImmersionMenuPopupWindowImpl(ActionBarDelegateImpl actionBarDelegateImpl, Menu menu) {
        super(actionBarDelegateImpl.getThemedContext());
        Context themedContext = actionBarDelegateImpl.getThemedContext();
        this.mActionBarDelegate = actionBarDelegateImpl;
        this.mAdapter = new ImmersionMenuAdapter(themedContext, menu);
        setAdapter(this.mAdapter);
        setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                MenuItem item = ImmersionMenuPopupWindowImpl.this.mAdapter.getItem(i);
                if (item.hasSubMenu()) {
                    final SubMenu subMenu = item.getSubMenu();
                    ImmersionMenuPopupWindowImpl.this.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss() {
                            ImmersionMenuPopupWindowImpl.this.setOnDismissListener(null);
                            ImmersionMenuPopupWindowImpl.this.update(subMenu);
                            ImmersionMenuPopupWindowImpl immersionMenuPopupWindowImpl = ImmersionMenuPopupWindowImpl.this;
                            immersionMenuPopupWindowImpl.fastShow(immersionMenuPopupWindowImpl.mLastAnchor, ImmersionMenuPopupWindowImpl.this.mLastParent);
                        }
                    });
                } else {
                    ImmersionMenuPopupWindowImpl.this.mActionBarDelegate.onMenuItemSelected(0, item);
                }
                ImmersionMenuPopupWindowImpl.this.dismiss(true);
            }
        });
    }

    public void show(View view, ViewGroup viewGroup) {
        this.mLastAnchor = view;
        this.mLastParent = viewGroup;
        super.show(view, viewGroup);
    }

    public void update(Menu menu) {
        this.mAdapter.update(menu);
    }
}
