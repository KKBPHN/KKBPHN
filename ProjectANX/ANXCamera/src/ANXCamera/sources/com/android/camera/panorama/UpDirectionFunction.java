package com.android.camera.panorama;

import android.util.Size;

public class UpDirectionFunction extends DirectionFunction {
    public UpDirectionFunction(int i, int i2, int i3, int i4, float f, int i5) {
        super(i, i2, i3, i4, f, i5);
    }

    public boolean enabled() {
        return true;
    }

    public Size getPreviewSize() {
        return getVerticalPreviewSize();
    }
}
