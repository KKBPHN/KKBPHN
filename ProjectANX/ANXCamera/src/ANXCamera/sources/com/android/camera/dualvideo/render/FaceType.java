package com.android.camera.dualvideo.render;

public enum FaceType {
    FACE_FRONT(1),
    FACE_BACK(2),
    FACE_REMOTE(3);
    
    private int mIndex;

    private FaceType(int i) {
        this.mIndex = i;
    }

    public int getIndex() {
        return this.mIndex;
    }
}
