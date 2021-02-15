package com.android.camera.features.mimoji2.bean;

import android.graphics.Bitmap;
import com.arcsoft.avatar2.emoticon.EmoInfo;

public class MimojiEmoticonInfo implements Cloneable {
    private Bitmap mBitmap;
    private EmoInfo mEmoInfo;
    private boolean mIsSelected;
    private int mName;

    public MimojiEmoticonInfo(EmoInfo emoInfo) {
        this.mEmoInfo = emoInfo;
    }

    public MimojiEmoticonInfo(EmoInfo emoInfo, Bitmap bitmap, int i) {
        this.mEmoInfo = emoInfo;
        this.mBitmap = bitmap;
        this.mName = i;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public EmoInfo getEmoInfo() {
        return this.mEmoInfo;
    }

    public int getName() {
        return this.mName;
    }

    public boolean isSelected() {
        return this.mIsSelected;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public void setEmoInfo(EmoInfo emoInfo) {
        this.mEmoInfo = emoInfo;
    }

    public void setName(int i) {
        this.mName = i;
    }

    public void setSelected(boolean z) {
        this.mIsSelected = z;
    }
}
