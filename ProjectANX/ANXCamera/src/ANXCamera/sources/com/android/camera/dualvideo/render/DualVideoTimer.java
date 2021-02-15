package com.android.camera.dualvideo.render;

public class DualVideoTimer {
    private long mDuration;
    private long mStartTime;

    public void init(long j) {
        this.mStartTime = System.currentTimeMillis();
        this.mDuration = j;
    }

    public boolean isValid() {
        return this.mDuration > System.currentTimeMillis() - this.mStartTime;
    }
}
