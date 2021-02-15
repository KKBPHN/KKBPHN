package com.android.camera.features.mimoji2.bean;

import com.android.camera.R;
import com.arcsoft.avatar2.BackgroundInfo;

public class MimojiBgInfo implements Cloneable {
    private BackgroundInfo mBackgroundInfo;
    private int mBgId;
    private int mDescId;
    private int mFrame;
    private boolean mIsSelected;
    private long mLastRefreshTime;
    private int mResourceId;

    public MimojiBgInfo() {
        this.mFrame = -1;
        this.mFrame = -1;
    }

    public MimojiBgInfo(BackgroundInfo backgroundInfo, int i, int i2, int i3) {
        this.mFrame = -1;
        this.mBackgroundInfo = backgroundInfo;
        this.mResourceId = i;
        this.mDescId = i2;
        this.mBgId = i3;
        this.mFrame = -1;
    }

    public MimojiBgInfo(boolean z) {
        this.mFrame = -1;
        this.mIsSelected = z;
        this.mDescId = R.string.lighting_pattern_null;
    }

    public MimojiBgInfo clone() {
        MimojiBgInfo mimojiBgInfo = new MimojiBgInfo();
        mimojiBgInfo.setBackgroundInfo(getBackgroundInfo());
        mimojiBgInfo.setResourceId(getResourceId());
        mimojiBgInfo.setBgId(getBgId());
        mimojiBgInfo.mFrame = 0;
        return mimojiBgInfo;
    }

    public BackgroundInfo getBackgroundInfo() {
        return this.mBackgroundInfo;
    }

    public int getBgId() {
        return this.mBgId;
    }

    public int getDescId() {
        return this.mDescId;
    }

    public boolean getIsNeedRefresh() {
        long currentTimeMillis = System.currentTimeMillis();
        BackgroundInfo backgroundInfo = this.mBackgroundInfo;
        if (backgroundInfo == null) {
            return false;
        }
        if (currentTimeMillis - this.mLastRefreshTime < ((long) backgroundInfo.getDelayMillis()) && this.mFrame >= 0) {
            return false;
        }
        this.mLastRefreshTime = currentTimeMillis;
        return true;
    }

    public long getRefreshTime() {
        return this.mLastRefreshTime;
    }

    public int getResourceId() {
        return this.mResourceId;
    }

    public boolean isSelected() {
        return this.mIsSelected;
    }

    public int nextFrame() {
        int i;
        int i2 = this.mFrame;
        BackgroundInfo backgroundInfo = this.mBackgroundInfo;
        if (backgroundInfo == null || backgroundInfo.getCount() <= 0) {
            i = 0;
        } else {
            if (this.mFrame == this.mBackgroundInfo.getCount() - 1) {
                this.mFrame = -1;
            }
            i = this.mFrame + 1;
        }
        this.mFrame = i;
        return i2;
    }

    public void setBackgroundInfo(BackgroundInfo backgroundInfo) {
        this.mBackgroundInfo = backgroundInfo;
    }

    public void setBgId(int i) {
        this.mBgId = i;
    }

    public void setDescId(int i) {
        this.mDescId = i;
    }

    public void setRefreshTime(long j) {
        this.mLastRefreshTime = j;
    }

    public void setResourceId(int i) {
        this.mResourceId = i;
    }

    public void setSelected(boolean z) {
        this.mIsSelected = z;
    }
}
