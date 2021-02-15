package com.xiaomi.camera.liveshot.writer;

import java.util.concurrent.CountDownLatch;

public abstract class SampleWriter implements Runnable {

    public class StatusNotifier {
        private final CountDownLatch mCountDownLatch = new CountDownLatch(1);
        private Object mStatus = null;

        public StatusNotifier(Object obj) {
            this.mStatus = obj;
        }

        public Object getStatus() {
            try {
                this.mCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.mStatus;
        }

        public void notify(Object obj) {
            this.mStatus = obj;
            this.mCountDownLatch.countDown();
        }
    }

    public void run() {
        writeSample();
    }

    public abstract void writeSample();
}
