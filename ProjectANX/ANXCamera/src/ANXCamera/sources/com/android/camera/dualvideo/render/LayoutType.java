package com.android.camera.dualvideo.render;

public enum LayoutType {
    UNDEFINED(0, 0),
    MINI(10, 30),
    DOWN_FULL(1, 50),
    UP_FULL(2, 50),
    UP(11, 50),
    DOWN(12, 50),
    FULL(13, 80),
    PATCH_0(20, 100),
    PATCH_1(21, 100),
    PATCH_2(22, 100),
    PATCH_3(23, 100),
    PATCH_4(24, 100),
    PATCH_5(25, 100),
    PATCH_REMOTE(26, 100);
    
    private final int mIndex;
    private final int mWeight;

    private LayoutType(int i, int i2) {
        this.mIndex = i;
        this.mWeight = i2;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public int getWeight() {
        return this.mWeight;
    }

    public boolean isSelectWindowType() {
        return this.mIndex >= PATCH_0.getIndex();
    }
}
