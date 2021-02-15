package com.android.camera.videoplayer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerQueueLock {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "PlayerQueueLock";
    private final Condition mProcessQueueCondition = this.mQueueLock.newCondition();
    private final ReentrantLock mQueueLock = new ReentrantLock();

    public boolean isLocked(String str) {
        return this.mQueueLock.isLocked();
    }

    public void lock(String str) {
        this.mQueueLock.lock();
    }

    public void notify(String str) {
        this.mProcessQueueCondition.signal();
    }

    public void unlock(String str) {
        this.mQueueLock.unlock();
    }

    public void wait(String str) {
        this.mProcessQueueCondition.await();
    }
}
