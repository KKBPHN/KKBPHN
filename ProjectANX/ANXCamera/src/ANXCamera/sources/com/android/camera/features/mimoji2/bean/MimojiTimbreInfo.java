package com.android.camera.features.mimoji2.bean;

import com.android.camera.R;

public class MimojiTimbreInfo {
    public static final int TIMBRE_BABY = 4;
    public static final int TIMBRE_GENTLEMEN = 1;
    public static final int TIMBRE_GIRL = 3;
    public static final int TIMBRE_LADY = 2;
    public static final int TIMBRE_ROBOT = 5;
    public static final int[] timbreTypes = {1, 2, 3, 4, 5};
    private int mDescId;
    private boolean mIsCompsing;
    private boolean mIsSelected;
    private int mResourceId;
    private int mTimbreId;

    public MimojiTimbreInfo() {
    }

    public MimojiTimbreInfo(int i, int i2, int i3) {
        this.mTimbreId = i;
        this.mResourceId = i2;
        this.mDescId = i3;
    }

    public MimojiTimbreInfo(boolean z) {
        this.mIsSelected = z;
        this.mDescId = R.string.timbre_normal;
    }

    public int getDescId() {
        return this.mDescId;
    }

    public int getResourceId() {
        return this.mResourceId;
    }

    public int getTimbreId() {
        return this.mTimbreId;
    }

    public boolean isCompsing() {
        return this.mIsCompsing;
    }

    public boolean isSelected() {
        return this.mIsSelected;
    }

    public void setDescId(int i) {
        this.mDescId = i;
    }

    public void setIsCompsing(boolean z) {
        this.mIsCompsing = z;
    }

    public void setResourceId(int i) {
        this.mResourceId = i;
    }

    public void setSelected(boolean z) {
        this.mIsSelected = z;
    }

    public void setTimbreId(int i) {
        this.mTimbreId = i;
    }
}
