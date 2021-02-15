package com.android.camera.fragment.ambilight;

import androidx.annotation.NonNull;

public class AmbilightInfo implements Comparable {
    private int mIconResId;
    private int mIndex;
    private boolean mIsNeedRect;
    private int mNameResId;
    private int mOrder;
    private int mSceneMode;
    private int tagUniqueFilterId;

    AmbilightInfo(int i, int i2, int i3, int i4, int i5) {
        this.mIndex = i;
        this.mOrder = i4;
        this.mNameResId = i2;
        this.mIconResId = i3;
        this.mSceneMode = i5;
    }

    public static int getIndex(int i) {
        return i & 65535;
    }

    public int compareTo(@NonNull AmbilightInfo ambilightInfo) {
        return Integer.compare(this.mOrder, ambilightInfo.mOrder);
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof AmbilightInfo)) {
            return false;
        }
        if (this.mIndex == ((AmbilightInfo) obj).mIndex) {
            z = true;
        }
        return z;
    }

    public int getIconResId() {
        return this.mIconResId;
    }

    public int getId() {
        return this.mIndex;
    }

    public int getNameResId() {
        return this.mNameResId;
    }

    public int getOrder() {
        return this.mOrder;
    }

    public int getSceneMode() {
        return this.mSceneMode;
    }

    public int getTagUniqueFilterId() {
        return this.tagUniqueFilterId;
    }

    public boolean isNeedRect() {
        return this.mIsNeedRect;
    }

    public void setIconResId(int i) {
        this.mIconResId = i;
    }

    public void setNameResId(int i) {
        this.mNameResId = i;
    }

    public void setNeedRect(boolean z) {
        this.mIsNeedRect = z;
    }

    public void setOrder(int i) {
        this.mOrder = i;
    }

    public void setTagUniqueFilterId(int i) {
        this.tagUniqueFilterId = i;
    }
}
