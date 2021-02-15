package com.miui.internal.view.menu.context;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.miui.internal.R;
import com.miui.internal.view.menu.MenuBuilder;
import com.miui.internal.widget.ImmersionListPopupWindowHelper;
import miui.util.AttributeResolver;
import miui.widget.ImmersionListPopupWindow;

public class ContextMenuPopupWindowImpl extends ImmersionListPopupWindow implements ContextMenuPopupWindow {
    private static final float SCREEN_MARGIN_BOTTOM_PROPORTION = 0.1f;
    private static final float SCREEN_MARGIN_TOP_PROPORTION = 0.1f;
    /* access modifiers changed from: private */
    public ContextMenuAdapter mAdapter;
    /* access modifiers changed from: private */
    public View mLastAnchor;
    private int mMarginScreen;
    /* access modifiers changed from: private */
    public MenuBuilder mMenu;
    private LinearLayout mPopupContentView;
    /* access modifiers changed from: private */
    public MenuItem mSeparateMenuItem = this.mAdapter.getLastCategorySystemOrderMenuItem();
    private View mSeparateMenuView;
    /* access modifiers changed from: private */
    public float mX;
    /* access modifiers changed from: private */
    public float mY;

    public ContextMenuPopupWindowImpl(Context context, MenuBuilder menuBuilder, OnDismissListener onDismissListener) {
        super(context);
        this.mMenu = menuBuilder;
        this.mAdapter = new ContextMenuAdapter(context, this.mMenu);
        prepareMultipleChoiceMenu(context);
        setAdapter(this.mAdapter);
        setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                MenuItem item = ContextMenuPopupWindowImpl.this.mAdapter.getItem(i);
                ContextMenuPopupWindowImpl.this.mMenu.performItemAction(item, 0);
                if (item.hasSubMenu()) {
                    final SubMenu subMenu = item.getSubMenu();
                    ContextMenuPopupWindowImpl.this.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss() {
                            ContextMenuPopupWindowImpl.this.setOnDismissListener(null);
                            ContextMenuPopupWindowImpl.this.update(subMenu);
                            ContextMenuPopupWindowImpl contextMenuPopupWindowImpl = ContextMenuPopupWindowImpl.this;
                            contextMenuPopupWindowImpl.fastShowAsContextMenu(contextMenuPopupWindowImpl.mLastAnchor, ContextMenuPopupWindowImpl.this.mX, ContextMenuPopupWindowImpl.this.mY);
                        }
                    });
                }
                ContextMenuPopupWindowImpl.this.dismiss();
            }
        });
        if (onDismissListener != null) {
            setOnDismissListener(onDismissListener);
        }
        this.mMarginScreen = context.getResources().getDimensionPixelSize(R.dimen.context_menu_window_margin_screen);
    }

    /* access modifiers changed from: private */
    public void fastShowAsContextMenu(View view, float f, float f2) {
        setWidth(computePopupContentWidth());
        setHeight(-2);
        this.mSeparateMenuView.setVisibility(8);
        showWithAnchor(view, f, f2);
        this.mContentView.forceLayout();
    }

    private int getListViewHeight() {
        View view = this.mContentView;
        if (view instanceof ListView) {
            ListAdapter adapter = ((ListView) view).getAdapter();
            int i = 0;
            for (int i2 = 0; i2 < adapter.getCount(); i2++) {
                View view2 = adapter.getView(i2, null, (ListView) this.mContentView);
                view2.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
                i += view2.getMeasuredHeight();
            }
            return i;
        }
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        return this.mContentView.getMeasuredHeight() + 0;
    }

    private int getMultipleChoiceViewHeight() {
        if (this.mSeparateMenuView.getVisibility() != 0) {
            return 0;
        }
        LayoutParams layoutParams = this.mSeparateMenuView.getLayoutParams();
        int i = (layoutParams == null || !(layoutParams instanceof MarginLayoutParams)) ? 0 : ((MarginLayoutParams) this.mSeparateMenuView.getLayoutParams()).topMargin + 0;
        this.mSeparateMenuView.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        return i + this.mSeparateMenuView.getMeasuredHeight();
    }

    private void prepareMultipleChoiceMenu(Context context) {
        if (this.mSeparateMenuItem == null) {
            this.mSeparateMenuView.setVisibility(8);
            return;
        }
        TextView textView = (TextView) this.mSeparateMenuView.findViewById(16908308);
        textView.setText(this.mSeparateMenuItem.getTitle());
        Drawable resolveDrawable = AttributeResolver.resolveDrawable(context, R.attr.contextMenuSeparateItemBackground);
        if (resolveDrawable != null) {
            textView.setBackground(resolveDrawable);
        }
        this.mSeparateMenuView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ContextMenuPopupWindowImpl.this.mMenu.performItemAction(ContextMenuPopupWindowImpl.this.mSeparateMenuItem, 0);
                ContextMenuPopupWindowImpl.this.dismiss();
            }
        });
    }

    private void showWithAnchor(View view, float f, float f2) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        int i = iArr[0] + ((int) f);
        boolean z = true;
        int i2 = iArr[1] + ((int) f2);
        View rootView = view.getRootView();
        rootView.getLocationInWindow(iArr);
        if (i > rootView.getWidth() / 2) {
            z = false;
        }
        int listViewHeight = getListViewHeight();
        float listViewHeight2 = (float) (i2 - (getListViewHeight() / 2));
        if (listViewHeight2 < ((float) rootView.getHeight()) * 0.1f) {
            listViewHeight2 = ((float) rootView.getHeight()) * 0.1f;
        }
        float multipleChoiceViewHeight = (float) (listViewHeight + getMultipleChoiceViewHeight());
        if (listViewHeight2 + multipleChoiceViewHeight > ((float) rootView.getHeight()) * 0.9f) {
            listViewHeight2 = (((float) rootView.getHeight()) * 0.9f) - multipleChoiceViewHeight;
        }
        if (listViewHeight2 < ((float) rootView.getHeight()) * 0.1f) {
            listViewHeight2 = ((float) rootView.getHeight()) * 0.1f;
            setHeight((int) (((float) rootView.getHeight()) * 0.79999995f));
        }
        showAtLocation(view, 0, z ? this.mMarginScreen : (rootView.getWidth() - this.mMarginScreen) - getWidth(), (int) listViewHeight2);
    }

    /* access modifiers changed from: protected */
    public void prepareContentView(Context context) {
        this.mPopupContentView = new LinearLayout(context);
        this.mPopupContentView.setOrientation(1);
        this.mSeparateMenuView = LayoutInflater.from(context).inflate(R.layout.immersion_popup_menu_item, null, false);
        Drawable resolveDrawable = AttributeResolver.resolveDrawable(context, R.attr.immersionWindowBackground);
        if (resolveDrawable != null) {
            resolveDrawable.getPadding(this.mBackgroundPadding);
            this.mRootView.setBackground(resolveDrawable);
            this.mSeparateMenuView.setBackground(resolveDrawable.getConstantState().newDrawable());
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, context.getResources().getDimensionPixelSize(R.dimen.context_menu_separate_item_margin_top), 0, 0);
        this.mPopupContentView.addView(this.mRootView, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        this.mPopupContentView.addView(this.mSeparateMenuView, layoutParams);
        setBackgroundDrawable(null);
        super.setPopupWindowContentView(this.mPopupContentView);
    }

    public void show(View view, ViewGroup viewGroup, float f, float f2) {
        this.mLastAnchor = view;
        this.mX = f;
        this.mY = f2;
        if (prepareShow(view, viewGroup)) {
            this.mSeparateMenuView.setElevation((float) this.mElevation);
            ImmersionListPopupWindowHelper.setPopupShadowAlpha(this.mRootView);
            showWithAnchor(view, f, f2);
        }
    }

    public void update(Menu menu) {
        this.mAdapter.update(menu);
    }
}
