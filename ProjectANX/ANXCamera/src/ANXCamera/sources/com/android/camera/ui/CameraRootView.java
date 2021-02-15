package com.android.camera.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CameraRootView extends FrameLayout {
    private boolean mDisableTouchevt = false;
    private boolean mHostPaused;

    public CameraRootView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void disableTouchEvent() {
        this.mDisableTouchevt = true;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.mDisableTouchevt || (motionEvent.getActionMasked() != 0 && motionEvent.getActionMasked() != 5)) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return true;
    }

    public void enableTouchEvent() {
        this.mDisableTouchevt = false;
    }

    public void onHostPause() {
        this.mHostPaused = true;
    }

    public void onHostResume() {
        this.mHostPaused = false;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (!this.mHostPaused) {
            super.onLayout(z, i, i2, i3, i4);
        }
    }
}
