package com.android.camera2.vendortag;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class VendorTag {
    private final AtomicReference mCachedValue = new AtomicReference(Optional.empty());

    public abstract Object create();

    /* access modifiers changed from: 0000 */
    public final Object getKey() {
        if (!((Optional) this.mCachedValue.get()).isPresent()) {
            synchronized (this.mCachedValue) {
                if (!((Optional) this.mCachedValue.get()).isPresent()) {
                    this.mCachedValue.set(Optional.ofNullable(create()));
                }
            }
        }
        return ((Optional) this.mCachedValue.get()).get();
    }

    public abstract String getName();

    public String toString() {
        return getName();
    }
}
