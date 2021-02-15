package com.android.camera.module.impl.component;

import java.util.List;

public class TimeSpeedModel {
    long duration;
    double speed;

    public TimeSpeedModel(long j, double d) {
        this.duration = j;
        this.speed = d;
    }

    public static int calculateRealTime(int i, double d) {
        return (int) ((((double) i) * 1.0d) / d);
    }

    public static long calculateRealTime(long j, double d) {
        return (long) ((((double) j) * 1.0d) / d);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.module.impl.component.TimeSpeedModel>, for r6v0, types: [java.util.List<com.android.camera.module.impl.component.TimeSpeedModel>, java.util.List] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long calculateRealTime(List<TimeSpeedModel> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        int i = 0;
        for (TimeSpeedModel timeSpeedModel : list) {
            i = (int) (((long) i) + calculateRealTime(timeSpeedModel.duration, timeSpeedModel.speed));
        }
        return (long) i;
    }

    public long getDuration() {
        return this.duration;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setDuration(int i) {
        this.duration = (long) i;
    }

    public void setSpeed(float f) {
        this.speed = (double) f;
    }
}
