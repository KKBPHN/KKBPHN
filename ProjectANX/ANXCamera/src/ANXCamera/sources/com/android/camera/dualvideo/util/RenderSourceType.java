package com.android.camera.dualvideo.util;

import java.util.Arrays;

public enum RenderSourceType {
    MAIN(1),
    SUB(2),
    REMOTE(3);
    
    private int mIndex;

    private RenderSourceType(int i) {
        this.mIndex = i;
    }

    public static RenderSourceType getTagByIndex(int i) {
        return (RenderSourceType) Arrays.stream(values()).filter(new C0243O0000oO(i)).findAny().orElse(null);
    }

    public int getIndex() {
        return this.mIndex;
    }
}
