package com.android.camera.timerburst;

public class TimerBurstBean {
    private boolean countdown;
    private long intervalTimer;
    private int totalCount;

    public long getIntervalTimer() {
        return this.intervalTimer;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public boolean isCountdown() {
        return this.countdown;
    }

    public void setCountdown(boolean z) {
        this.countdown = z;
    }

    public void setIntervalTimer(long j) {
        this.intervalTimer = j;
    }

    public void setTotalCount(int i) {
        this.totalCount = i;
    }
}
