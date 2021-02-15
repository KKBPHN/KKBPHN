package com.android.camera.features.mimoji2.bean;

import java.util.ArrayList;

public class MimojiInfo2 implements Comparable {
    public static final String PRE_CARTOON = "pre_cartoon";
    public static final String PRE_HUMAN = "pre_human";
    private ArrayList mAvatarConfigInfoArrayList;
    public String mAvatarTemplatePath;
    public String mConfigPath;
    public byte[] mData;
    private int mDefaultFrame = 0;
    public long mDirectoryName;
    private int mFrame = -1;
    private boolean mIsNeedAnim = false;
    private boolean mIsPreHuman = false;
    public int mName;
    public int mName2;
    public String mPackPath;
    public String mThumbnailUrl;
    public String mThumbnailUrl2;

    public int compareTo(MimojiInfo2 mimojiInfo2) {
        long j = this.mDirectoryName;
        long j2 = mimojiInfo2.mDirectoryName;
        if (j > j2) {
            return -1;
        }
        return j < j2 ? 1 : 0;
    }

    public ArrayList getAvatarConfigInfoArrayList() {
        return this.mAvatarConfigInfoArrayList;
    }

    public int getDefaultFrame() {
        return this.mDefaultFrame;
    }

    public int getFrame() {
        return this.mFrame;
    }

    public boolean isIsNeedAnim() {
        return this.mIsNeedAnim;
    }

    public boolean isIsPreHuman() {
        return this.mIsPreHuman;
    }

    public int nextFrame() {
        ArrayList arrayList = this.mAvatarConfigInfoArrayList;
        if (arrayList != null && arrayList.size() > 1) {
            if (this.mFrame == this.mAvatarConfigInfoArrayList.size() - 1) {
                this.mFrame = -1;
            }
            this.mFrame++;
        }
        return this.mFrame;
    }

    public void setAvatarConfigInfoArrayList(ArrayList arrayList) {
        this.mAvatarConfigInfoArrayList = arrayList;
    }

    public void setDefaultFrame(int i) {
        this.mDefaultFrame = i;
    }

    public void setFrame(int i) {
        this.mFrame = i;
    }

    public void setIsNeedAnim(boolean z) {
        this.mIsNeedAnim = z;
    }

    public void setIsPreHuman(boolean z) {
        this.mIsPreHuman = z;
    }
}
