package com.android.camera.data.data;

import android.text.TextUtils;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import com.android.camera.CameraAppImpl;

public class ComponentDataItem {
    @DrawableRes
    public static final int RES_NULL = -1;
    @StringRes
    public static final int STRING_NULL = -1;
    public static final String VALUE_NULL = "null";
    @StringRes
    public int mContentDescriptionRes;
    @StringRes
    public int mDisplayNameRes;
    public String mDisplayNameStr;
    @DrawableRes
    public int mIconDisabledRes;
    @DrawableRes
    public int mIconLabelRes = -1;
    @DrawableRes
    public int mIconRes;
    @DrawableRes
    public int mIconSelectedRes;
    @DrawableRes
    public int mIconShadowLabelRes = -1;
    @DrawableRes
    public int mIconShadowRes = -1;
    public boolean mIsDisabled;
    public boolean mIsShowText;
    @DrawableRes
    public int mNewIconRes;
    public int mSelectColor;
    public String mValue;

    public ComponentDataItem(@DrawableRes int i, @DrawableRes int i2, @DrawableRes int i3, @DrawableRes int i4, @DrawableRes int i5, @DrawableRes int i6, @StringRes int i7, String str) {
        this.mIconRes = i;
        this.mIconShadowRes = i2;
        this.mIconSelectedRes = i3;
        this.mIconDisabledRes = i;
        this.mIconSelectedRes = i4;
        this.mIconLabelRes = i5;
        this.mIconShadowLabelRes = i6;
        this.mDisplayNameRes = i7;
        this.mValue = str;
    }

    public ComponentDataItem(@DrawableRes int i, @DrawableRes int i2, @DrawableRes int i3, @StringRes int i4, @StringRes int i5, String str) {
        this.mIconRes = i;
        this.mIconShadowRes = i2;
        this.mIconSelectedRes = i3;
        this.mDisplayNameRes = i4;
        this.mContentDescriptionRes = i5;
        this.mValue = str;
    }

    public ComponentDataItem(@DrawableRes int i, @DrawableRes int i2, @DrawableRes int i3, @StringRes int i4, String str) {
        this.mIconRes = i;
        this.mIconShadowRes = i2;
        this.mIconSelectedRes = i3;
        this.mDisplayNameRes = i4;
        this.mValue = str;
    }

    public ComponentDataItem(@DrawableRes int i, @DrawableRes int i2, @StringRes int i3, @StringRes int i4, String str, boolean z, int i5) {
        this.mIconRes = i;
        this.mIconSelectedRes = i2;
        this.mDisplayNameRes = i3;
        this.mContentDescriptionRes = i4;
        this.mValue = str;
        this.mIsShowText = z;
        this.mSelectColor = i5;
    }

    public ComponentDataItem(@DrawableRes int i, @DrawableRes int i2, @StringRes int i3, String str) {
        this.mIconRes = i;
        this.mIconSelectedRes = i2;
        this.mDisplayNameRes = i3;
        this.mValue = str;
    }

    public ComponentDataItem(@DrawableRes int i, @DrawableRes int i2, String str, String str2) {
        this.mIconRes = i;
        this.mIconSelectedRes = i2;
        this.mDisplayNameStr = str;
        this.mValue = str2;
    }

    public ComponentDataItem(@DrawableRes int i, @DrawableRes int i2, boolean z, @StringRes int i3, String str) {
        this.mIconRes = i;
        this.mIconSelectedRes = i2;
        this.mDisplayNameRes = i3;
        this.mIsDisabled = z;
        this.mValue = str;
    }

    public ComponentDataItem(String str, @DrawableRes int i, @DrawableRes int i2, @StringRes int i3) {
        this.mValue = str;
        this.mIconRes = i;
        this.mNewIconRes = i2;
        this.mDisplayNameRes = i3;
    }

    public ComponentDataItem(String str, @DrawableRes int i, @DrawableRes int i2, String str2) {
        this.mValue = str;
        this.mIconRes = i;
        this.mNewIconRes = i2;
        this.mDisplayNameStr = str2;
    }

    public String toString() {
        String string = TextUtils.isEmpty(this.mDisplayNameStr) ? CameraAppImpl.getAndroidContext().getString(this.mDisplayNameRes) : this.mDisplayNameStr;
        StringBuilder sb = new StringBuilder();
        sb.append("ComponentDataItem{mDisplayName='");
        sb.append(string);
        sb.append('\'');
        sb.append("mValue='");
        sb.append(this.mValue);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
