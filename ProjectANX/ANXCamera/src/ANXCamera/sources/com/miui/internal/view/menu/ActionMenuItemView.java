package com.miui.internal.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.internal.R;
import com.miui.internal.util.FolmeAnimHelper;
import com.miui.internal.view.menu.MenuBuilder.ItemInvoker;
import com.miui.internal.view.menu.MenuView.ItemView;

public class ActionMenuItemView extends LinearLayout implements ItemView {
    private ImageView mImageView;
    private boolean mIsCheckable;
    private MenuItemImpl mItemData;
    private ItemInvoker mItemInvoker;
    private TextView mTextView;

    public ActionMenuItemView(Context context) {
        this(context, null, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        FolmeAnimHelper.addAlphaPressAnim(this);
        setOrientation(1);
        setGravity(1);
        this.mImageView = new ImageView(context, null, R.attr.actionBarButtonIconViewStyle);
        addView(this.mImageView);
        this.mTextView = new TextView(context, null, R.attr.actionBarButtonTextViewStyle);
        addView(this.mTextView);
        this.mTextView.setVisibility(getContext().getResources().getConfiguration().orientation == 1 ? 0 : 8);
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void initialize(MenuItemImpl menuItemImpl, int i) {
        this.mItemData = menuItemImpl;
        setSelected(false);
        setTitle(menuItemImpl.getTitle());
        setIcon(menuItemImpl.getIcon());
        setCheckable(menuItemImpl.isCheckable());
        setChecked(menuItemImpl.isChecked());
        setEnabled(menuItemImpl.isEnabled());
        setClickable(true);
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
        if (super.performClick()) {
            return true;
        }
        ItemInvoker itemInvoker = this.mItemInvoker;
        if (itemInvoker == null || !itemInvoker.invokeItem(this.mItemData)) {
            return false;
        }
        playSoundEffect(0);
        return true;
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    public void setCheckable(boolean z) {
        this.mIsCheckable = z;
    }

    public void setChecked(boolean z) {
        if (this.mIsCheckable) {
            setSelected(z);
        }
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mImageView.setEnabled(z);
        this.mTextView.setEnabled(z);
    }

    public void setIcon(Drawable drawable) {
        if (this.mImageView.getDrawable() != drawable) {
            this.mImageView.setImageDrawable(drawable);
        }
    }

    public void setItemInvoker(ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    public void setShortcut(boolean z, char c) {
    }

    public void setTitle(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
    }

    public boolean showsIcon() {
        return true;
    }
}
