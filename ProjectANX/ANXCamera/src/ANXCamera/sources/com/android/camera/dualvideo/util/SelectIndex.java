package com.android.camera.dualvideo.util;

public enum SelectIndex {
    INDEX_0(0),
    INDEX_1(1),
    INDEX_2(2);
    
    private int mIndex;

    private SelectIndex(int i) {
        this.mIndex = i;
    }

    public int getIndex() {
        return this.mIndex;
    }
}
