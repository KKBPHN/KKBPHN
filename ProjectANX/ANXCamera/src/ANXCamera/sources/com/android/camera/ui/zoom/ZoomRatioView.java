package com.android.camera.ui.zoom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;

public class ZoomRatioView extends FrameLayout {
    private static final String TAG = "ZoomRatioView";
    private static final boolean UI_DEBUG_ENABLED = Log.isLoggable(TAG, 3);
    private ZoomTextImageView mZoomRatioIcon;
    private int mZoomRatioIndex;
    private String mZoomRatioString;
    private ZoomTextImageView mZoomRatioText;

    public ZoomRatioView(Context context) {
        this(context, null);
    }

    public ZoomRatioView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZoomRatioView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ZoomRatioView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    private static void debugUi(String str, String str2) {
        if (UI_DEBUG_ENABLED) {
            com.android.camera.log.Log.d(str, str2);
        }
    }

    public CharSequence getContentDescriptionString() {
        if (TextUtils.isEmpty(this.mZoomRatioString)) {
            return "";
        }
        return getContext().getString(R.string.accessibility_focus_status, new Object[]{this.mZoomRatioString});
    }

    public ZoomTextImageView getIconView() {
        return this.mZoomRatioIcon;
    }

    public ZoomTextImageView getTextView() {
        return this.mZoomRatioText;
    }

    public int getZoomRatioIndex() {
        return this.mZoomRatioIndex;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mZoomRatioIcon = (ZoomTextImageView) findViewById(R.id.zoom_ratio_item_image);
        this.mZoomRatioIcon.setIcon();
        this.mZoomRatioText = (ZoomTextImageView) findViewById(R.id.zoom_ratio_item_text);
    }

    public void resetScale() {
        this.mZoomRatioIcon.setScaleX(1.0f);
        this.mZoomRatioIcon.setScaleY(1.0f);
        this.mZoomRatioText.setScaleX(1.0f);
        this.mZoomRatioText.setScaleY(1.0f);
    }

    public void setAlpha(float f) {
        StringBuilder sb = new StringBuilder();
        sb.append("setAlpha(): index = ");
        sb.append(this.mZoomRatioIndex);
        sb.append(", alpha = ");
        sb.append(f);
        debugUi(TAG, sb.toString());
        this.mZoomRatioIcon.setAlpha(f);
        this.mZoomRatioText.setAlpha(1.0f - f);
    }

    public void setBackgroundColor(int i) {
        this.mZoomRatioText.setCircleColor(i);
    }

    public void setCaptureCount(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", new Object[]{Integer.valueOf(i)}));
        this.mZoomRatioText.setText(1, sb.toString());
    }

    public void setIconify(boolean z) {
        setAlpha(z ? 1.0f : 0.0f);
    }

    public void setRotation(float f) {
        this.mZoomRatioIcon.setRotation(f);
        this.mZoomRatioText.setRotation(f);
    }

    public void setVisibility(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("setVisibility(): index = ");
        sb.append(this.mZoomRatioIndex);
        sb.append(", visibility = ");
        sb.append(Util.viewVisibilityToString(i));
        debugUi(TAG, sb.toString());
        super.setVisibility(i);
    }

    public void setZoomRatio(float f) {
        StringBuilder sb = new StringBuilder();
        float decimal = HybridZoomingSystem.toDecimal(f);
        int i = (int) decimal;
        if (((int) ((10.0f * decimal) - ((float) (i * 10)))) == 0 || i >= 100) {
            sb.append(i);
        } else {
            sb.append(decimal);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("setZoomRatio(): ");
        sb2.append(f);
        debugUi(TAG, sb2.toString());
        this.mZoomRatioText.setText(0, sb.toString());
        this.mZoomRatioString = String.valueOf(decimal);
        setContentDescription(getContentDescriptionString());
    }

    public void setZoomRatioIndex(int i) {
        this.mZoomRatioIndex = i;
    }
}
