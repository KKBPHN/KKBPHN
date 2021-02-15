package com.android.camera.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.ColorConstant;
import com.android.camera.module.ModuleManager;

public class V6PreviewFrame extends V6RelativeLayout {
    public GradienterDrawer mGradienter;
    public ReferenceLineDrawer mReferenceLine;

    public V6PreviewFrame(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public View getReferenceLine() {
        return this.mReferenceLine;
    }

    public void hidePreviewGradienter() {
        GradienterDrawer gradienterDrawer = this.mGradienter;
        if (gradienterDrawer != null && gradienterDrawer.getVisibility() == 0) {
            this.mReferenceLine.setGradienterEnabled(false);
            this.mGradienter.setVisibility(8);
        }
    }

    public void hidePreviewReferenceLine() {
        if (this.mReferenceLine.getVisibility() == 0) {
            this.mGradienter.setReferenceLineEnabled(false);
            this.mReferenceLine.setVisibility(8);
        }
    }

    public void onCameraOpen() {
        super.onCameraOpen();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mReferenceLine = (ReferenceLineDrawer) findViewById(R.id.v6_reference_grid);
        this.mReferenceLine.initialize(3, 3);
        this.mReferenceLine.setBorderVisible(false, false);
        this.mReferenceLine.setLineColor(ColorConstant.COLOR_COMMON_DISABLE);
        this.mGradienter = (GradienterDrawer) findViewById(R.id.v6_reference_gradienter);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (Util.isAccessible()) {
            ReferenceLineDrawer referenceLineDrawer = this.mReferenceLine;
            if (referenceLineDrawer != null) {
                referenceLineDrawer.layout(i, i2, i3, i4);
            }
        }
    }

    public void onResume() {
    }

    public void updateReferenceGradienterSwitched(boolean z, boolean z2, boolean z3) {
        if (this.mGradienter != null) {
            ReferenceLineDrawer referenceLineDrawer = this.mReferenceLine;
            if (referenceLineDrawer != null) {
                referenceLineDrawer.setGradienterEnabled(z2);
                this.mGradienter.setReferenceLineEnabled(z);
                int i = 0;
                this.mReferenceLine.setVisibility(z ? 0 : 8);
                if (z2) {
                    this.mGradienter.setConfigInfo(getWidth(), getHeight(), z3);
                }
                GradienterDrawer gradienterDrawer = this.mGradienter;
                if (!z2) {
                    i = 8;
                }
                gradienterDrawer.setVisibility(i);
            }
        }
    }

    public void updateReferenceLineAccordSquare() {
        LayoutParams layoutParams = (LayoutParams) this.mReferenceLine.getLayoutParams();
        int windowWidth = ModuleManager.isSquareModule() ? Display.getWindowWidth() / 6 : 0;
        layoutParams.topMargin = windowWidth;
        layoutParams.bottomMargin = windowWidth;
        if (this.mReferenceLine.getVisibility() == 0) {
            this.mReferenceLine.requestLayout();
        }
    }
}
