package com.android.camera.panorama;

import android.annotation.SuppressLint;
import android.util.Size;

@SuppressLint({"NewApi"})
public class DirectionFunction {
    public static final int DIRECTION_DOWN = 3;
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_NONE = -1;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_UP = 2;
    public static final int ERROR_NO_EFFECTIVE_PIXEL = -1;
    public static final int SUCCEEDED = 0;
    private final int mAngle;
    protected int mDirection = -1;
    private final int mInputHeight;
    private final int mInputWidth;
    private final int mMaxHeight;
    private final int mMaxWidth;
    private final float mScale;

    public DirectionFunction(int i, int i2, int i3, int i4, float f, int i5) {
        this.mInputWidth = i;
        this.mInputHeight = i2;
        this.mScale = f;
        this.mAngle = i5;
        this.mMaxWidth = i3;
        this.mMaxHeight = i4;
    }

    public boolean enabled() {
        return false;
    }

    public int getAngle() {
        return this.mAngle;
    }

    public int getDirection() {
        return this.mDirection;
    }

    /* access modifiers changed from: protected */
    public Size getHorizontalPreviewSize() {
        float f = (float) this.mMaxWidth;
        float f2 = this.mScale;
        int i = (int) (f / f2);
        int i2 = this.mAngle;
        int i3 = (90 == i2 || 270 == i2) ? (int) (((float) this.mInputWidth) / this.mScale) : (int) (((float) this.mInputHeight) / f2);
        return new Size(i & -2, i3 & -2);
    }

    public Size getPreviewSize() {
        return new Size(this.mInputWidth, this.mInputHeight);
    }

    public float getScale() {
        return this.mScale;
    }

    /* access modifiers changed from: protected */
    public Size getVerticalPreviewSize() {
        float f = (float) this.mMaxHeight;
        float f2 = this.mScale;
        int i = (int) (f / f2);
        int i2 = this.mAngle;
        int i3 = (90 == i2 || 270 == i2) ? (int) (((float) this.mInputHeight) / this.mScale) : (int) (((float) this.mInputWidth) / f2);
        return new Size(i3 & -2, i & -2);
    }
}
