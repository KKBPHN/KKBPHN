package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.util.Util;
import java.util.Queue;

abstract class BaseKeyPool {
    private static final int MAX_SIZE = 20;
    private final Queue keyPool = Util.createQueue(20);

    BaseKeyPool() {
    }

    public abstract Poolable create();

    /* access modifiers changed from: 0000 */
    public Poolable get() {
        Poolable poolable = (Poolable) this.keyPool.poll();
        return poolable == null ? create() : poolable;
    }

    public void offer(Poolable poolable) {
        if (this.keyPool.size() < 20) {
            this.keyPool.offer(poolable);
        }
    }
}
