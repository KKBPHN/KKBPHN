package com.bumptech.glide.load.engine;

import androidx.annotation.NonNull;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.FactoryPools.Factory;
import com.bumptech.glide.util.pool.FactoryPools.Poolable;
import com.bumptech.glide.util.pool.StateVerifier;

final class LockedResource implements Resource, Poolable {
    private static final Pool POOL = FactoryPools.threadSafe(20, new Factory() {
        public LockedResource create() {
            return new LockedResource();
        }
    });
    private boolean isLocked;
    private boolean isRecycled;
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    private Resource toWrap;

    LockedResource() {
    }

    private void init(Resource resource) {
        this.isRecycled = false;
        this.isLocked = true;
        this.toWrap = resource;
    }

    @NonNull
    static LockedResource obtain(Resource resource) {
        LockedResource lockedResource = (LockedResource) POOL.acquire();
        Preconditions.checkNotNull(lockedResource);
        LockedResource lockedResource2 = lockedResource;
        lockedResource2.init(resource);
        return lockedResource2;
    }

    private void release() {
        this.toWrap = null;
        POOL.release(this);
    }

    @NonNull
    public Object get() {
        return this.toWrap.get();
    }

    @NonNull
    public Class getResourceClass() {
        return this.toWrap.getResourceClass();
    }

    public int getSize() {
        return this.toWrap.getSize();
    }

    @NonNull
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    public synchronized void recycle() {
        this.stateVerifier.throwIfRecycled();
        this.isRecycled = true;
        if (!this.isLocked) {
            this.toWrap.recycle();
            release();
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void unlock() {
        this.stateVerifier.throwIfRecycled();
        if (this.isLocked) {
            this.isLocked = false;
            if (this.isRecycled) {
                recycle();
            }
        } else {
            throw new IllegalStateException("Already unlocked");
        }
    }
}
