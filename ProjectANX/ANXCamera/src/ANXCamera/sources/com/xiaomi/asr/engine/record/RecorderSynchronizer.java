package com.xiaomi.asr.engine.record;

import java.util.concurrent.CountDownLatch;

public class RecorderSynchronizer {
    private static final RecorderSynchronizer ourInstance = new RecorderSynchronizer();
    private CountDownLatch latch = new CountDownLatch(0);
    private final Object mSyncObj = new Object();
    private volatile boolean recording = false;

    private RecorderSynchronizer() {
    }

    public static RecorderSynchronizer getInstance() {
        return ourInstance;
    }

    public void countDown() {
        synchronized (this.mSyncObj) {
            this.latch.countDown();
        }
    }

    public CountDownLatch getCountDownLatch() {
        CountDownLatch countDownLatch;
        synchronized (this.mSyncObj) {
            countDownLatch = this.latch;
        }
        return countDownLatch;
    }

    public boolean isRecording() {
        return this.recording;
    }

    public void start() {
        this.recording = true;
        synchronized (this.mSyncObj) {
            this.latch = new CountDownLatch(1);
        }
    }

    public void stop() {
        this.recording = false;
    }
}
