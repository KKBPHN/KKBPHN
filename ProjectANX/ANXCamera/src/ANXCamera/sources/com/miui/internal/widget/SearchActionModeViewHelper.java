package com.miui.internal.widget;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import java.util.Locale;
import miui.R;
import miui.animation.utils.EaseManager;
import miui.animation.utils.EaseManager.InterpolateEaseStyle;
import miui.util.ViewUtils;

public class SearchActionModeViewHelper {
    public static final int ANIMATION_DURATION = 400;
    private ViewGroup mSearchContainer;
    private TextView mTextCancel;

    private boolean shouldHideCancelText() {
        String language = Locale.getDefault().getLanguage();
        return !"zh".equalsIgnoreCase(language) && !"en".equalsIgnoreCase(language);
    }

    public int getAnimatedPaddingHorizontal(Context context) {
        if (shouldHideCancelText()) {
            return 0;
        }
        return context.getResources().getDimensionPixelSize(R.dimen.search_mode_bg_padding);
    }

    public int getAnimatedPaddingTop(Context context) {
        return context.getResources().getDimensionPixelSize(com.miui.internal.R.dimen.search_mode_bg_padding_top);
    }

    public void initView(ViewGroup viewGroup) {
        this.mTextCancel = (TextView) viewGroup.findViewById(com.miui.internal.R.id.search_text_cancel);
        this.mSearchContainer = (ViewGroup) viewGroup.findViewById(com.miui.internal.R.id.search_container);
        if (shouldHideCancelText()) {
            this.mTextCancel.setVisibility(8);
            if (viewGroup.getPaddingEnd() == 0) {
                viewGroup.setPaddingRelative(viewGroup.getPaddingStart(), viewGroup.getPaddingTop(), viewGroup.getPaddingStart(), viewGroup.getPaddingBottom());
            }
        }
    }

    public TimeInterpolator obtainInterpolator() {
        InterpolateEaseStyle interpolateEaseStyle = new InterpolateEaseStyle(0);
        interpolateEaseStyle.setFactors(0.98f, 0.75f);
        return EaseManager.getInterpolator(interpolateEaseStyle);
    }

    public void performCancelViewClick() {
        this.mTextCancel.performClick();
    }

    public void setCancelViewClickListener(OnClickListener onClickListener) {
        this.mTextCancel.setOnClickListener(onClickListener);
    }

    public void translationCancelView() {
        TextView textView = this.mTextCancel;
        textView.setTranslationX((float) textView.getWidth());
    }

    public void updateCancelView(float f, int i) {
        float f2 = 1.0f - f;
        if (ViewUtils.isLayoutRtl(this.mTextCancel)) {
            f2 = f - 1.0f;
        }
        TextView textView = this.mTextCancel;
        textView.setTranslationX(((float) textView.getWidth()) * f2);
        if (this.mSearchContainer.getLayoutParams() instanceof MarginLayoutParams) {
            ((MarginLayoutParams) this.mSearchContainer.getLayoutParams()).setMarginEnd((int) ((((float) (this.mTextCancel.getWidth() - i)) * f) + ((float) i)));
        }
        this.mSearchContainer.requestLayout();
    }
}
