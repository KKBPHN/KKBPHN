package com.android.camera.dualvideo.render;

public class EaseOutAnim {
    private final int mDuration;
    private final Long mStartTime = Long.valueOf(System.currentTimeMillis());

    public EaseOutAnim(int i) {
        this.mDuration = i;
    }

    private float transEaseOut(float f) {
        double d = (double) f;
        float pow = (float) (((Math.pow(d, 3.0d) * 0.6596d) - (Math.pow(d, 2.0d) * 2.415d)) + (d * 2.746d) + 0.00618d);
        if (pow > 1.0f) {
            pow = 1.0f;
        }
        if (pow < 0.0f) {
            return 0.0f;
        }
        return pow;
    }

    public float getRatio() {
        long currentTimeMillis = System.currentTimeMillis() - this.mStartTime.longValue();
        if (isFinished()) {
            return 1.0f;
        }
        return transEaseOut(((float) currentTimeMillis) / ((float) this.mDuration));
    }

    public boolean isFinished() {
        return System.currentTimeMillis() - this.mStartTime.longValue() > ((long) this.mDuration);
    }
}
