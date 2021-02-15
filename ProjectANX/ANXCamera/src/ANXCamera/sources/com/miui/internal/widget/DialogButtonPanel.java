package com.miui.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.miui.internal.R;

public class DialogButtonPanel extends LinearLayout {
    private final int HORIZONTAL_MARGIN;
    private final int VERTICAL_MARGIN;

    public DialogButtonPanel(Context context) {
        this(context, null);
    }

    public DialogButtonPanel(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DialogButtonPanel(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.HORIZONTAL_MARGIN = getResources().getDimensionPixelOffset(R.dimen.dialog_btn_margin_horizontal);
        this.VERTICAL_MARGIN = getResources().getDimensionPixelOffset(R.dimen.dialog_btn_margin_vertical);
    }

    private void clearParams(LayoutParams layoutParams) {
        layoutParams.setMarginStart(0);
        layoutParams.topMargin = 0;
        layoutParams.setMarginStart(0);
    }

    private void handleLayoutParams(LayoutParams layoutParams) {
        int childCount = getChildCount();
        if (getOrientation() == 1) {
            layoutParams.width = -1;
            if (childCount > 0) {
                layoutParams.topMargin = this.VERTICAL_MARGIN;
                return;
            }
            return;
        }
        layoutParams.width = 0;
        if (childCount > 0) {
            layoutParams.setMarginStart(this.HORIZONTAL_MARGIN);
        }
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        handleLayoutParams((LayoutParams) layoutParams);
        super.addView(view, i, layoutParams);
    }

    public void clearVisibleChildMargins() {
        View view;
        int childCount = getChildCount();
        int i = 0;
        while (true) {
            view = null;
            if (i >= childCount) {
                break;
            }
            view = getChildAt(i);
            if (view.getVisibility() == 0) {
                break;
            }
            i++;
        }
        if (view != null) {
            clearParams((LayoutParams) view.getLayoutParams());
        }
    }

    public void onViewRemoved(View view) {
        clearParams((LayoutParams) view.getLayoutParams());
    }
}
