package com.android.camera.ui.drawable.snap;

import android.graphics.Canvas;
import androidx.annotation.FloatRange;
import com.android.camera.ui.drawable.CameraPaintBase;

public abstract class PaintPattern {
    protected CameraPaintBase paintBase;

    public PaintPattern(CameraPaintBase cameraPaintBase) {
        this.paintBase = cameraPaintBase;
        prepareTargetPattern();
    }

    /* access modifiers changed from: protected */
    public final float calculateCurrentValue(float f, float f2, float f3) {
        return f + ((f2 - f) * f3);
    }

    public abstract void directlyResult();

    public void draw(Canvas canvas) {
    }

    public abstract boolean interceptDraw();

    public abstract void prepareTargetPattern();

    public void recycle() {
    }

    public abstract void updateValue(@FloatRange(from = 0.0d, to = 1.0d) float f);
}
