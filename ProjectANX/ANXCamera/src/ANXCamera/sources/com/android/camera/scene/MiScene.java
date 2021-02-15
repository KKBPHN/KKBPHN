package com.android.camera.scene;

import android.util.SparseArray;

public class MiScene {
    private float lastResult;
    private boolean mEnable;
    public int type;
    public SparseArray valueArray = new SparseArray();

    public MiScene() {
        this.valueArray.put(0, Integer.valueOf(-1111111));
    }

    public static MiScene create() {
        return new MiScene();
    }

    public boolean isChange(float f) {
        boolean z = this.lastResult != f;
        this.lastResult = f;
        return z;
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public void setEnable(boolean z) {
        this.mEnable = z;
    }
}
