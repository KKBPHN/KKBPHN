package com.android.camera.fragment.fastmotion;

public class FastMotionDescriptionItem {
    public boolean mIsVideo;
    public String mType;
    public String mTypeDesc;
    public int mVideoCover;
    public String mVideoDesc;
    public int mVideoId;

    public FastMotionDescriptionItem(boolean z, int i, String str, int i2, String str2, String str3) {
        this.mIsVideo = z;
        this.mVideoId = i;
        this.mVideoDesc = str;
        this.mVideoCover = i2;
        this.mType = str2;
        this.mTypeDesc = str3;
    }
}
