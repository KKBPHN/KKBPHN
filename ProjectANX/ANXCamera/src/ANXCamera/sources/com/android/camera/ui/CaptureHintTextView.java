package com.android.camera.ui;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView.BufferType;
import androidx.appcompat.widget.AppCompatTextView;
import com.android.camera.R;
import com.android.camera.Util;
import java.util.Locale;

public class CaptureHintTextView extends AppCompatTextView {
    int mMaxWidth;
    int mMinWidth;

    public CaptureHintTextView(Context context) {
        super(context);
        init();
    }

    public CaptureHintTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CaptureHintTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.mMinWidth = getResources().getDimensionPixelOffset(R.dimen.dolly_zoom_capture_hint_min_width);
        this.mMaxWidth = getResources().getDimensionPixelOffset(R.dimen.dolly_zoom_capture_hint_max_width);
        setSingleLine();
        setEllipsize(TruncateAt.MARQUEE);
        setFocusable(true);
        setMarqueeRepeatLimit(-1);
    }

    public boolean isFocused() {
        return !"cn".equalsIgnoreCase(Locale.getDefault().getCountry());
    }

    public void setText(CharSequence charSequence, BufferType bufferType) {
        super.setText(charSequence, bufferType);
        int ceil = ((int) Math.ceil((double) getPaint().measureText(getText().toString()))) + 120;
        Rect displayRect = Util.getDisplayRect();
        int min = Math.min(Math.max(ceil, this.mMinWidth), this.mMaxWidth);
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.clone_capture_hint_height);
        int i = dimensionPixelOffset / 2;
        int i2 = min / 2;
        int screenWidth = (Util.getScreenWidth(getContext()) - i) - i2;
        int i3 = -(i2 - i);
        int height = (displayRect.top + (displayRect.height() / 2)) - i;
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = min;
            layoutParams.height = dimensionPixelOffset;
            if (Util.isLayoutRTL(getContext())) {
                layoutParams.rightMargin = i3;
            } else {
                layoutParams.leftMargin = screenWidth;
            }
            layoutParams.topMargin = height;
            setLayoutParams(layoutParams);
        }
    }
}
