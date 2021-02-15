package com.android.camera.fragment.top;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.R;
import com.android.camera.ui.ColorImageView;

public class LabelItemView extends FrameLayout {
    private ColorImageView mLabelImageView;
    private ColorImageView mMainImageView;

    public LabelItemView(@NonNull Context context) {
        this(context, null);
    }

    public LabelItemView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.auto_type_item_view, this, true);
        this.mMainImageView = (ColorImageView) inflate.findViewById(R.id.main_image);
        this.mLabelImageView = (ColorImageView) inflate.findViewById(R.id.label_image);
    }

    public ColorImageView getLabelView() {
        return this.mLabelImageView;
    }

    public ColorImageView getMainView() {
        return this.mMainImageView;
    }

    public boolean isLabelVisible() {
        return this.mLabelImageView.getVisibility() == 0;
    }

    public void setColorAndRefresh(@ColorInt int i) {
        if (this.mMainImageView.getVisibility() == 0) {
            this.mMainImageView.setColorAndRefresh(i);
        }
        if (this.mLabelImageView.getVisibility() == 0) {
            this.mLabelImageView.setColorAndRefresh(i);
        }
    }

    public void setLabelImageResource(@DrawableRes int i) {
        if (i >= 0) {
            this.mLabelImageView.setImageResource(i);
        }
    }

    public void setLabelImageShadowResource(@DrawableRes int i) {
        ColorImageView colorImageView = this.mLabelImageView;
        if (i < 0) {
            colorImageView.setBackground(null);
        } else {
            colorImageView.setBackgroundResource(i);
        }
    }

    public void setLabelViewVisibility(int i) {
        this.mLabelImageView.setVisibility(i);
    }

    public void setMainImageResource(@DrawableRes int i) {
        if (i >= 0) {
            this.mMainImageView.setImageResource(i);
        }
    }

    public void setMainImageShadowResource(@DrawableRes int i) {
        ColorImageView colorImageView = this.mMainImageView;
        if (i < 0) {
            colorImageView.setBackground(null);
        } else {
            colorImageView.setBackgroundResource(i);
        }
    }

    public void setMainViewVisibility(int i) {
        this.mMainImageView.setVisibility(i);
    }
}
