package com.android.camera.data.data;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public final class AmbilightDescriptionItem {
    @DrawableRes
    public static final int RES_NULL = -1;
    @StringRes
    public static final int STRING_NULL = -1;
    public String mDescriptionPart1;
    public String mDescriptionPart2;
    @StringRes
    public int mDisplayNameRes;
    @DrawableRes
    public int mImageRes;
    @DrawableRes
    public int mImageRes2;

    public AmbilightDescriptionItem(@StringRes int i, @DrawableRes int i2, String str) {
        this.mDisplayNameRes = i;
        this.mImageRes = i2;
        this.mDescriptionPart1 = str;
    }

    public AmbilightDescriptionItem(@StringRes int i, @DrawableRes int i2, String str, @DrawableRes int i3, String str2) {
        this.mDisplayNameRes = i;
        this.mImageRes = i2;
        this.mImageRes2 = i3;
        this.mDescriptionPart1 = str;
        this.mDescriptionPart2 = str2;
    }
}
