package com.miui.internal.widget;

public class VerticalSeekBarHelper {
    public static int calcTop(float f, int i, int i2, int i3) {
        int i4 = (((int) (f * ((float) i))) + i2) - i3;
        int i5 = i2 - i3;
        if (i4 < i5) {
            i4 = i5;
        }
        int i6 = i + i5;
        return i4 > i6 ? i6 : i4;
    }
}
