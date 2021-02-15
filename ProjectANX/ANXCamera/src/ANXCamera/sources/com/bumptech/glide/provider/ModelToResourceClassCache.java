package com.bumptech.glide.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import com.bumptech.glide.util.MultiClassKey;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ModelToResourceClassCache {
    private final ArrayMap registeredResourceClassCache = new ArrayMap();
    private final AtomicReference resourceClassKeyRef = new AtomicReference();

    public void clear() {
        synchronized (this.registeredResourceClassCache) {
            this.registeredResourceClassCache.clear();
        }
    }

    @Nullable
    public List get(@NonNull Class cls, @NonNull Class cls2, @NonNull Class cls3) {
        List list;
        MultiClassKey multiClassKey = (MultiClassKey) this.resourceClassKeyRef.getAndSet(null);
        if (multiClassKey == null) {
            multiClassKey = new MultiClassKey(cls, cls2, cls3);
        } else {
            multiClassKey.set(cls, cls2, cls3);
        }
        synchronized (this.registeredResourceClassCache) {
            list = (List) this.registeredResourceClassCache.get(multiClassKey);
        }
        this.resourceClassKeyRef.set(multiClassKey);
        return list;
    }

    public void put(@NonNull Class cls, @NonNull Class cls2, @NonNull Class cls3, @NonNull List list) {
        synchronized (this.registeredResourceClassCache) {
            this.registeredResourceClassCache.put(new MultiClassKey(cls, cls2, cls3), list);
        }
    }
}
