package com.miui.internal.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.miui.internal.widget.BlurBackgroundView;

public class ExpandedMenuBlurView extends FrameLayout {
    private BlurBackgroundView mBackgroundView;

    public ExpandedMenuBlurView(Context context) {
        this(context, null);
    }

    public ExpandedMenuBlurView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        addBlurView(context);
    }

    private void addBlurView(Context context) {
        this.mBackgroundView = new BlurBackgroundView(context);
        this.mBackgroundView.setLayoutParams(new LayoutParams(-1, -1));
        addView(this.mBackgroundView, 0);
        setBlurBackground(false);
    }

    public boolean isSearchStubSupportBlur() {
        return this.mBackgroundView.isSupportBlur();
    }

    public boolean setBlurBackground(boolean z) {
        return this.mBackgroundView.setBlurBackground(z);
    }
}
