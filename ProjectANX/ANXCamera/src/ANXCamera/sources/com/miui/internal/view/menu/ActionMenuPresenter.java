package com.miui.internal.view.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.util.FolmeAnimHelper;
import com.miui.internal.view.ActionBarPolicy;
import com.miui.internal.view.menu.ActionMenuView.ActionMenuChildView;
import com.miui.internal.view.menu.MenuBuilder.ItemInvoker;
import com.miui.internal.view.menu.MenuPresenter.Callback;
import com.miui.internal.view.menu.MenuView.ItemView;
import java.util.ArrayList;

public class ActionMenuPresenter extends BaseMenuPresenter {
    private final SparseBooleanArray mActionButtonGroups;
    /* access modifiers changed from: private */
    public ActionButtonSubMenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    /* access modifiers changed from: private */
    public int mListItemLayoutRes;
    /* access modifiers changed from: private */
    public int mListLayoutRes;
    private OverflowMenu mListOverflowMenu;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    int mOpenSubMenuId;
    /* access modifiers changed from: private */
    public View mOverflowButton;
    /* access modifiers changed from: private */
    public OverflowMenu mOverflowMenu;
    /* access modifiers changed from: private */
    public int mOverflowMenuAttrs;
    private MenuItemImpl mOverflowMenuItem;
    final PopupPresenterCallback mPopupPresenterCallback;
    /* access modifiers changed from: private */
    public OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private View mScrapActionButtonView;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;

    class ActionButtonSubMenu extends MenuDialogHelper {
        public ActionButtonSubMenu(SubMenuBuilder subMenuBuilder) {
            super(subMenuBuilder);
            ActionMenuPresenter.this.setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        public void onDismiss(DialogInterface dialogInterface) {
            super.onDismiss(dialogInterface);
            ActionMenuPresenter.this.mActionButtonPopup = null;
            ActionMenuPresenter.this.mOpenSubMenuId = 0;
        }
    }

    class ListOverflowMenu implements OverflowMenu {
        private ListMenuPresenter mListMenuPresenter;

        private ListOverflowMenu() {
        }

        private ListMenuPresenter getListMenuPresenter(MenuBuilder menuBuilder) {
            if (this.mListMenuPresenter == null) {
                ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
                this.mListMenuPresenter = new ListMenuPresenter(actionMenuPresenter.mContext, actionMenuPresenter.mListLayoutRes, ActionMenuPresenter.this.mListItemLayoutRes);
            }
            menuBuilder.addMenuPresenter(this.mListMenuPresenter);
            return this.mListMenuPresenter;
        }

        public void dismiss(boolean z) {
            ((PhoneActionMenuView) ActionMenuPresenter.this.mMenuView).hideOverflowMenu();
        }

        public View getOverflowMenuView(MenuBuilder menuBuilder) {
            if (menuBuilder == null || menuBuilder.getNonActionItems().size() <= 0) {
                return null;
            }
            return (View) getListMenuPresenter(menuBuilder).getMenuView((ViewGroup) ActionMenuPresenter.this.mMenuView);
        }

        public boolean isShowing() {
            return ((PhoneActionMenuView) ActionMenuPresenter.this.mMenuView).isOverflowMenuShowing();
        }

        public boolean tryShow() {
            return ((PhoneActionMenuView) ActionMenuPresenter.this.mMenuView).showOverflowMenu();
        }

        public void update(MenuBuilder menuBuilder) {
            ((PhoneActionMenuView) ActionMenuPresenter.this.mMenuView).setOverflowMenuView(getOverflowMenuView(menuBuilder));
        }
    }

    class OpenOverflowRunnable implements Runnable {
        private OverflowMenu mPopup;

        public OpenOverflowRunnable(OverflowMenu overflowMenu) {
            this.mPopup = overflowMenu;
        }

        public void run() {
            ActionMenuPresenter.this.mMenu.changeMenuMode();
            View view = (View) ActionMenuPresenter.this.mMenuView;
            if (!(view == null || view.getWindowToken() == null || !this.mPopup.tryShow())) {
                ActionMenuPresenter.this.mOverflowMenu = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }

    interface OverflowMenu {
        void dismiss(boolean z);

        boolean isShowing();

        boolean tryShow();

        void update(MenuBuilder menuBuilder);
    }

    class OverflowMenuButton extends LinearLayout implements ActionMenuChildView {
        private ImageView mImageView;
        private TextView mTextView;

        public OverflowMenuButton(ActionMenuPresenter actionMenuPresenter, Context context) {
            this(actionMenuPresenter, context, null);
        }

        public OverflowMenuButton(ActionMenuPresenter actionMenuPresenter, Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public OverflowMenuButton(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, ActionMenuPresenter.this.mOverflowMenuAttrs);
            setOrientation(1);
            setGravity(1);
            this.mImageView = new ImageView(context, null, R.attr.actionBarButtonIconViewStyle);
            addView(this.mImageView);
            this.mTextView = new TextView(context, null, R.attr.actionBarButtonTextViewStyle);
            addView(this.mTextView);
            if (getContext().getResources().getConfiguration().orientation == 1) {
                this.mTextView.setVisibility(0);
            } else {
                this.mTextView.setVisibility(8);
            }
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.OverflowMenuButton, ActionMenuPresenter.this.mOverflowMenuAttrs, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.OverflowMenuButton_android_drawableTop);
            CharSequence text = obtainStyledAttributes.getText(R.styleable.OverflowMenuButton_android_text);
            this.mImageView.setImageDrawable(drawable);
            this.mTextView.setText(text);
            setClickable(true);
            setFocusable(true);
            setVisibility(0);
            setEnabled(true);
            FolmeAnimHelper.addAlphaPressAnim(this);
        }

        private boolean isVisible() {
            while (r2 != null && r2.getVisibility() == 0) {
                ViewParent parent = r2.getParent();
                r2 = parent instanceof ViewGroup ? (ViewGroup) parent : null;
            }
            return r2 == null;
        }

        public boolean needsDividerAfter() {
            return false;
        }

        public boolean needsDividerBefore() {
            return false;
        }

        /* access modifiers changed from: protected */
        public void onConfigurationChanged(Configuration configuration) {
            TextView textView;
            int i;
            super.onConfigurationChanged(configuration);
            if (getContext().getResources().getConfiguration().orientation == 1) {
                textView = this.mTextView;
                i = 0;
            } else {
                textView = this.mTextView;
                i = 8;
            }
            textView.setVisibility(i);
            setPaddingRelative(getPaddingStart(), getResources().getDimensionPixelSize(R.dimen.action_button_bg_top_padding), getPaddingEnd(), getResources().getDimensionPixelSize(R.dimen.action_button_bg_bottom_padding));
        }

        public boolean performClick() {
            if (super.performClick() || !isVisible()) {
                return true;
            }
            MenuBuilder menuBuilder = ActionMenuPresenter.this.mMenu;
            if (menuBuilder != null) {
                menuBuilder.dispatchMenuItemSelected(menuBuilder.getRootMenu(), ActionMenuPresenter.this.getOverflowMenuItem());
            }
            playSoundEffect(0);
            boolean isSelected = isSelected();
            ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
            if (isSelected) {
                actionMenuPresenter.hideOverflowMenu(true);
            } else {
                actionMenuPresenter.showOverflowMenu();
            }
            return true;
        }

        public void setEnabled(boolean z) {
            super.setEnabled(z);
            this.mImageView.setEnabled(z);
            this.mTextView.setEnabled(z);
        }
    }

    class PopupOverflowMenu extends MenuPopupHelper implements OverflowMenu {
        public PopupOverflowMenu(Context context, MenuBuilder menuBuilder, View view, boolean z) {
            super(context, menuBuilder, view, z);
            setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
            setMenuItemLayout(R.layout.overflow_popup_menu_item_layout);
        }

        public void dismiss(boolean z) {
            super.dismiss(z);
            if (ActionMenuPresenter.this.mOverflowButton != null) {
                ActionMenuPresenter.this.mOverflowButton.setSelected(false);
            }
        }

        public void onDismiss() {
            super.onDismiss();
            ActionMenuPresenter.this.mMenu.close();
            ActionMenuPresenter.this.mOverflowMenu = null;
        }

        public void update(MenuBuilder menuBuilder) {
        }
    }

    class PopupPresenterCallback implements Callback {
        private PopupPresenterCallback() {
        }

        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            if (menuBuilder instanceof SubMenuBuilder) {
                menuBuilder.getRootMenu().close(false);
            }
        }

        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            if (menuBuilder == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder) menuBuilder).getItem().getItemId();
            return false;
        }
    }

    class SavedState implements Parcelable {
        public static final Creator CREATOR = new Creator() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public int openSubMenuId;

        SavedState() {
        }

        SavedState(Parcel parcel) {
            this.openSubMenuId = parcel.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.openSubMenuId);
        }
    }

    public ActionMenuPresenter(Context context, int i, int i2) {
        this(context, i, i2, 0, 0);
    }

    public ActionMenuPresenter(Context context, int i, int i2, int i3, int i4) {
        super(context, i, i2);
        this.mOverflowMenuAttrs = 16843510;
        this.mActionButtonGroups = new SparseBooleanArray();
        this.mPopupPresenterCallback = new PopupPresenterCallback();
        this.mListLayoutRes = i3;
        this.mListItemLayoutRes = i4;
    }

    private View findViewForItem(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup) this.mMenuView;
        if (viewGroup == null) {
            return null;
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if ((childAt instanceof ItemView) && ((ItemView) childAt).getItemData() == menuItem) {
                return childAt;
            }
        }
        return null;
    }

    private OverflowMenu getOverflowMenu() {
        if (DeviceHelper.IS_TABLET) {
            PopupOverflowMenu popupOverflowMenu = new PopupOverflowMenu(this.mContext, this.mMenu, this.mOverflowButton, true);
            return popupOverflowMenu;
        }
        if (this.mListOverflowMenu == null) {
            this.mListOverflowMenu = new ListOverflowMenu();
        }
        return this.mListOverflowMenu;
    }

    /* access modifiers changed from: private */
    public MenuItemImpl getOverflowMenuItem() {
        if (this.mOverflowMenuItem == null) {
            MenuItemImpl menuItemImpl = new MenuItemImpl(this.mMenu, 0, R.id.more, 0, 0, this.mContext.getString(R.string.more), 0);
            this.mOverflowMenuItem = menuItemImpl;
        }
        return this.mOverflowMenuItem;
    }

    public void bindItemView(MenuItemImpl menuItemImpl, ItemView itemView) {
        itemView.initialize(menuItemImpl, 0);
        itemView.setItemInvoker((ItemInvoker) this.mMenuView);
    }

    /* access modifiers changed from: protected */
    public View createOverflowMenuButton(Context context) {
        return new OverflowMenuButton(this, context);
    }

    public boolean dismissPopupMenus(boolean z) {
        return hideOverflowMenu(z);
    }

    public boolean flagActionItems() {
        ArrayList visibleItems = this.mMenu.getVisibleItems();
        int size = visibleItems.size();
        int i = this.mMaxItems;
        if (i < size) {
            i--;
        }
        int i2 = i;
        int i3 = 0;
        while (true) {
            boolean z = true;
            if (i3 < size && i2 > 0) {
                MenuItemImpl menuItemImpl = (MenuItemImpl) visibleItems.get(i3);
                if (!menuItemImpl.requestsActionButton() && !menuItemImpl.requiresActionButton()) {
                    z = false;
                }
                menuItemImpl.setIsActionButton(z);
                if (z) {
                    i2--;
                }
                i3++;
            }
        }
        while (i3 < size) {
            ((MenuItemImpl) visibleItems.get(i3)).setIsActionButton(false);
            i3++;
        }
        return true;
    }

    public View getItemView(MenuItemImpl menuItemImpl, View view, ViewGroup viewGroup) {
        View actionView = menuItemImpl.getActionView();
        if (actionView == null || menuItemImpl.hasCollapsibleActionView()) {
            if (!(view instanceof ActionMenuItemView)) {
                view = null;
            }
            actionView = super.getItemView(menuItemImpl, view, viewGroup);
        }
        actionView.setVisibility(menuItemImpl.isActionViewExpanded() ? 8 : 0);
        ActionMenuView actionMenuView = (ActionMenuView) viewGroup;
        LayoutParams layoutParams = actionView.getLayoutParams();
        if (!actionMenuView.checkLayoutParams(layoutParams)) {
            actionView.setLayoutParams(actionMenuView.generateLayoutParams(layoutParams));
        }
        return actionView;
    }

    public MenuView getMenuView(ViewGroup viewGroup) {
        MenuView menuView = super.getMenuView(viewGroup);
        ((ActionMenuView) menuView).setPresenter(this);
        return menuView;
    }

    public boolean hideOverflowMenu(boolean z) {
        if (this.mPostedOpenRunnable == null || this.mMenuView == null) {
            OverflowMenu overflowMenu = this.mOverflowMenu;
            if (overflowMenu == null) {
                return false;
            }
            boolean isShowing = overflowMenu.isShowing();
            if (isShowing) {
                this.mOverflowButton.setSelected(false);
            }
            this.mOverflowMenu.dismiss(z);
            return isShowing;
        }
        this.mOverflowButton.setSelected(false);
        ((View) this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
        this.mPostedOpenRunnable = null;
        return true;
    }

    public boolean hideSubMenus() {
        ActionButtonSubMenu actionButtonSubMenu = this.mActionButtonPopup;
        if (actionButtonSubMenu == null) {
            return false;
        }
        actionButtonSubMenu.dismiss();
        return true;
    }

    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        super.initForMenu(context, menuBuilder);
        context.getResources();
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(context);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = actionBarPolicy.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = actionBarPolicy.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = actionBarPolicy.getMaxActionButtons();
        }
        int i = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = createOverflowMenuButton(this.mSystemContext);
                int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
                this.mOverflowButton.measure(makeMeasureSpec, makeMeasureSpec);
            }
            i -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = i;
        this.mScrapActionButtonView = null;
    }

    public boolean isOverflowMenuShowing() {
        OverflowMenu overflowMenu = this.mOverflowMenu;
        return overflowMenu != null && overflowMenu.isShowing();
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        dismissPopupMenus(true);
        super.onCloseMenu(menuBuilder, z);
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = this.mContext.getResources().getInteger(R.integer.abc_max_action_buttons);
        }
        MenuBuilder menuBuilder = this.mMenu;
        if (menuBuilder != null) {
            menuBuilder.onItemsChanged(true);
        }
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        int i = ((SavedState) parcelable).openSubMenuId;
        if (i > 0) {
            MenuItem findItem = this.mMenu.findItem(i);
            if (findItem != null) {
                onSubMenuSelected((SubMenuBuilder) findItem.getSubMenu());
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        savedState.openSubMenuId = this.mOpenSubMenuId;
        return savedState;
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        SubMenuBuilder subMenuBuilder2 = subMenuBuilder;
        while (subMenuBuilder2.getParentMenu() != this.mMenu) {
            subMenuBuilder2 = (SubMenuBuilder) subMenuBuilder2.getParentMenu();
        }
        if (findViewForItem(subMenuBuilder2.getItem()) == null && this.mOverflowButton == null) {
            return false;
        }
        this.mOpenSubMenuId = subMenuBuilder.getItem().getItemId();
        this.mActionButtonPopup = new ActionButtonSubMenu(subMenuBuilder);
        this.mActionButtonPopup.show(null);
        super.onSubMenuSelected(subMenuBuilder);
        return true;
    }

    public void onSubUiVisibilityChanged(boolean z) {
        if (z) {
            super.onSubMenuSelected(null);
        } else {
            this.mMenu.close(false);
        }
    }

    public void setActionEditMode(boolean z) {
        if (z) {
            this.mOverflowMenuAttrs = R.attr.actionModeOverflowButtonStyle;
        }
    }

    public void setExpandedActionViewsExclusive(boolean z) {
        this.mExpandedActionViewsExclusive = z;
    }

    public void setItemLimit(int i) {
        this.mMaxItems = i;
        this.mMaxItemsSet = true;
    }

    public void setReserveOverflow(boolean z) {
        this.mReserveOverflow = z;
        this.mReserveOverflowSet = true;
    }

    public void setWidthLimit(int i, boolean z) {
        this.mWidthLimit = i;
        this.mStrictWidthLimit = z;
        this.mWidthLimitSet = true;
    }

    public boolean shouldIncludeItem(int i, MenuItemImpl menuItemImpl) {
        return menuItemImpl.isActionButton();
    }

    public boolean showOverflowMenu() {
        if (!this.mReserveOverflow || isOverflowMenuShowing() || this.mMenu == null || this.mMenuView == null || this.mPostedOpenRunnable != null) {
            return false;
        }
        this.mPostedOpenRunnable = new OpenOverflowRunnable(getOverflowMenu());
        ((View) this.mMenuView).post(this.mPostedOpenRunnable);
        super.onSubMenuSelected(null);
        this.mOverflowButton.setSelected(true);
        return true;
    }

    public void updateMenuView(boolean z) {
        super.updateMenuView(z);
        if (this.mMenuView != null) {
            MenuBuilder menuBuilder = this.mMenu;
            ArrayList nonActionItems = menuBuilder != null ? menuBuilder.getNonActionItems() : null;
            boolean z2 = false;
            if (this.mReserveOverflow && nonActionItems != null) {
                int size = nonActionItems.size();
                if (size == 1) {
                    z2 = !((MenuItemImpl) nonActionItems.get(0)).isActionViewExpanded();
                } else if (size > 0) {
                    z2 = true;
                }
            }
            View view = this.mOverflowButton;
            if (z2) {
                if (view == null) {
                    this.mOverflowButton = createOverflowMenuButton(this.mSystemContext);
                } else {
                    view.setTranslationY(0.0f);
                }
                ViewGroup viewGroup = (ViewGroup) this.mOverflowButton.getParent();
                if (viewGroup != this.mMenuView) {
                    if (viewGroup != null) {
                        viewGroup.removeView(this.mOverflowButton);
                    }
                    ActionMenuView actionMenuView = (ActionMenuView) this.mMenuView;
                    actionMenuView.addView(this.mOverflowButton, actionMenuView.generateOverflowButtonLayoutParams());
                }
            } else if (view != null) {
                ViewParent parent = view.getParent();
                MenuView menuView = this.mMenuView;
                if (parent == menuView) {
                    ((ViewGroup) menuView).removeView(this.mOverflowButton);
                }
            }
            ((ActionMenuView) this.mMenuView).setOverflowReserved(this.mReserveOverflow);
            if (!DeviceHelper.IS_TABLET) {
                getOverflowMenu().update(this.mMenu);
            }
        }
    }
}
