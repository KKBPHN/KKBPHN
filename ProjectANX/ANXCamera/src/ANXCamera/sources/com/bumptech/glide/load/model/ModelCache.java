package com.bumptech.glide.load.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.util.Queue;

public class ModelCache {
    private static final int DEFAULT_SIZE = 250;
    private final LruCache cache;

    @VisibleForTesting
    final class ModelKey {
        private static final Queue KEY_QUEUE = Util.createQueue(0);
        private int height;
        private Object model;
        private int width;

        private ModelKey() {
        }

        static ModelKey get(Object obj, int i, int i2) {
            ModelKey modelKey;
            synchronized (KEY_QUEUE) {
                modelKey = (ModelKey) KEY_QUEUE.poll();
            }
            if (modelKey == null) {
                modelKey = new ModelKey();
            }
            modelKey.init(obj, i, i2);
            return modelKey;
        }

        private void init(Object obj, int i, int i2) {
            this.model = obj;
            this.width = i;
            this.height = i2;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ModelKey)) {
                return false;
            }
            ModelKey modelKey = (ModelKey) obj;
            return this.width == modelKey.width && this.height == modelKey.height && this.model.equals(modelKey.model);
        }

        public int hashCode() {
            return (((this.height * 31) + this.width) * 31) + this.model.hashCode();
        }

        public void release() {
            synchronized (KEY_QUEUE) {
                KEY_QUEUE.offer(this);
            }
        }
    }

    public ModelCache() {
        this(250);
    }

    public ModelCache(long j) {
        this.cache = new LruCache(j) {
            /* access modifiers changed from: protected */
            public void onItemEvicted(@NonNull ModelKey modelKey, @Nullable Object obj) {
                modelKey.release();
            }
        };
    }

    public void clear() {
        this.cache.clearMemory();
    }

    @Nullable
    public Object get(Object obj, int i, int i2) {
        ModelKey modelKey = ModelKey.get(obj, i, i2);
        Object obj2 = this.cache.get(modelKey);
        modelKey.release();
        return obj2;
    }

    public void put(Object obj, int i, int i2, Object obj2) {
        this.cache.put(ModelKey.get(obj, i, i2), obj2);
    }
}
