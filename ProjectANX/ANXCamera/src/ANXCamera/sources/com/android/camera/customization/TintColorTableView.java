package com.android.camera.customization;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.Nullable;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.customization.TintShapeView.ShapeType;
import com.android.camera.customization.TintShapeView.TintShape;
import java.util.List;

public class TintColorTableView extends LinearLayout implements OnClickListener {
    private int itemPaddingLeft = 0;
    private List mColorOptions;
    private int mCurrentSelection = 0;
    private OnColorChangeListener mOnColorChangeListener;

    interface OnColorChangeListener {
        void onColorChange(int i);
    }

    public TintColorTableView(Context context) {
        super(context);
    }

    public TintColorTableView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TintColorTableView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public TintColorTableView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    private void addColorItem(int i) {
        boolean z = false;
        TintShapeView tintShapeView = (TintShapeView) LayoutInflater.from(getContext()).inflate(R.layout.tint_color_item, this, false);
        tintShapeView.setTag(Integer.valueOf(i));
        tintShapeView.setOnClickListener(this);
        tintShapeView.config(new TintShape().shape(ShapeType.CIRCLE).innerColor(((TintColor) this.mColorOptions.get(i)).color()).outerColor(-1).innerRadius(getResources().getDimensionPixelSize(R.dimen.custom_tint_color_item) / 2).outerRadius((getResources().getDimensionPixelSize(R.dimen.custom_tint_color_item_outer) / 2) - getResources().getDimensionPixelSize(R.dimen.custom_tint_color_item_stroke)).stroke((float) getResources().getDimensionPixelSize(R.dimen.custom_tint_color_item_stroke)));
        tintShapeView.setSelection(i == this.mCurrentSelection);
        LayoutParams layoutParams = (LayoutParams) tintShapeView.getLayoutParams();
        layoutParams.setMarginStart(0);
        layoutParams.setMarginEnd(i == this.mColorOptions.size() - 1 ? 0 : this.itemPaddingLeft);
        if (i == this.mCurrentSelection) {
            z = true;
        }
        setAccessible(tintShapeView, i, z);
        addView(tintShapeView, layoutParams);
    }

    private int calculateLeftPadding() {
        int size = this.mColorOptions.size();
        int windowWidth = (Display.getWindowWidth() - (getResources().getDimensionPixelSize(R.dimen.custom_tint_color_table_margin) * 2)) - (getResources().getDimensionPixelSize(R.dimen.custom_tint_color_item_outer) * size);
        if (size == 1) {
            return 0;
        }
        return windowWidth / (size - 1);
    }

    private void setAccessible(View view, int i, boolean z) {
        String string = view.getContext().getString(((TintColor) this.mColorOptions.get(i)).name());
        if (z) {
            StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(", ");
            sb.append(view.getContext().getString(R.string.accessibility_selected));
            view.setContentDescription(sb.toString());
            if (Util.isAccessible()) {
                view.postDelayed(new O000000o(view), 100);
                return;
            }
            return;
        }
        view.setContentDescription(string);
    }

    public void initialize(List list, int i) {
        this.mColorOptions = list;
        this.mCurrentSelection = i;
        removeAllViews();
        this.itemPaddingLeft = calculateLeftPadding();
        for (int i2 = 0; i2 < this.mColorOptions.size(); i2++) {
            addColorItem(i2);
        }
    }

    public void onClick(View view) {
        setCurrent(((Integer) view.getTag()).intValue());
        OnColorChangeListener onColorChangeListener = this.mOnColorChangeListener;
        if (onColorChangeListener != null) {
            onColorChangeListener.onColorChange(this.mCurrentSelection);
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setCurrent(int i) {
        int i2 = this.mCurrentSelection;
        if (i != i2) {
            View childAt = getChildAt(i2);
            ((TintShapeView) childAt).setSelection(false);
            setAccessible(childAt, this.mCurrentSelection, false);
            View childAt2 = getChildAt(i);
            ((TintShapeView) childAt2).setSelection(true);
            setAccessible(childAt2, i, true);
            this.mCurrentSelection = i;
        }
    }

    public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener) {
        this.mOnColorChangeListener = onColorChangeListener;
    }
}
