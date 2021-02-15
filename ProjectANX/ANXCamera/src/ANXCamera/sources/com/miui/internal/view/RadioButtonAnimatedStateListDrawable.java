package com.miui.internal.view;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.miui.internal.R;
import com.miui.internal.view.CheckWidgetAnimatedStateListDrawable.CheckWidgetConstantState;

public class RadioButtonAnimatedStateListDrawable extends CheckBoxAnimatedStateListDrawable {
    private int mDrawPadding = 19;

    public class RadioButtonConstantState extends CheckWidgetConstantState {
        protected RadioButtonConstantState() {
        }

        /* access modifiers changed from: protected */
        public Drawable newAnimatedStateListDrawable(Resources resources, Theme theme, CheckWidgetConstantState checkWidgetConstantState) {
            return new RadioButtonAnimatedStateListDrawable(resources, theme, checkWidgetConstantState);
        }
    }

    public RadioButtonAnimatedStateListDrawable() {
    }

    public RadioButtonAnimatedStateListDrawable(Resources resources, Theme theme, CheckWidgetConstantState checkWidgetConstantState) {
        super(resources, theme, checkWidgetConstantState);
        if (resources != null) {
            this.mDrawPadding = resources.getDimensionPixelSize(R.dimen.radio_button_drawable_padding);
        }
    }

    /* access modifiers changed from: protected */
    public int getCheckWidgetDrawableStyle() {
        return R.style.CheckWidgetDrawable_RadioButton;
    }

    /* access modifiers changed from: protected */
    public boolean isSingleSelectionWidget() {
        return true;
    }

    /* access modifiers changed from: protected */
    public CheckWidgetConstantState newCheckWidgetConstantState() {
        return new RadioButtonConstantState();
    }

    /* access modifiers changed from: protected */
    public void setCheckWidgetDrawableBounds(int i, int i2, int i3, int i4) {
        int i5 = this.mDrawPadding;
        super.setCheckWidgetDrawableBounds(i + i5, i2 + i5, i3 - i5, i4 - i5);
    }

    /* access modifiers changed from: protected */
    public void setCheckWidgetDrawableBounds(Rect rect) {
        int i = this.mDrawPadding;
        rect.inset(i, i);
        super.setCheckWidgetDrawableBounds(rect);
    }
}
