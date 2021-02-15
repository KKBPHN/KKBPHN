package com.bumptech.glide.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LruCache {
    private final Map cache = new LinkedHashMap(100, 0.75f, true);
    private long currentSize;
    private final long initialMaxSize;
    private long maxSize;

    public LruCache(long j) {
        this.initialMaxSize = j;
        this.maxSize = j;
    }

    private void evict() {
        trimToSize(this.maxSize);
    }

    public void clearMemory() {
        trimToSize(0);
    }

    public synchronized boolean contains(@NonNull Object obj) {
        return this.cache.containsKey(obj);
    }

    @Nullable
    public synchronized Object get(@NonNull Object obj) {
        return this.cache.get(obj);
    }

    /* access modifiers changed from: protected */
    public synchronized int getCount() {
        return this.cache.size();
    }

    public synchronized long getCurrentSize() {
        return this.currentSize;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    /* access modifiers changed from: protected */
    public int getSize(@Nullable Object obj) {
        return 1;
    }

    /* access modifiers changed from: protected */
    public void onItemEvicted(@NonNull Object obj, @Nullable Object obj2) {
    }

    @Nullable
    public synchronized Object put(@NonNull Object obj, @Nullable Object obj2) {
        long size = (long) getSize(obj2);
        if (size >= this.maxSize) {
            onItemEvicted(obj, obj2);
            return null;
        }
        if (obj2 != null) {
            this.currentSize += size;
        }
        Object put = this.cache.put(obj, obj2);
        if (put != null) {
            this.currentSize -= (long) getSize(put);
            if (!put.equals(obj2)) {
                onItemEvicted(obj, put);
            }
        }
        evict();
        return put;
    }

    @Nullable
    public synchronized Object remove(@NonNull Object obj) {
        Object remove;
        remove = this.cache.remove(obj);
        if (remove != null) {
            this.currentSize -= (long) getSize(remove);
        }
        return remove;
    }

    public synchronized void setSizeMultiplier(float f) {
        if (f >= 0.0f) {
            this.maxSize = (long) Math.round(((float) this.initialMaxSize) * f);
            evict();
        } else {
            throw new IllegalArgumentException("Multiplier must be >= 0");
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void trimToSize(long j) {
        while (this.currentSize > j) {
            Iterator it = this.cache.entrySet().iterator();
            Entry entry = (Entry) it.next();
            Object value = entry.getValue();
            this.currentSize -= (long) getSize(value);
            Object key = entry.getKey();
            it.remove();
            onItemEvicted(key, value);
        }
    }
}
