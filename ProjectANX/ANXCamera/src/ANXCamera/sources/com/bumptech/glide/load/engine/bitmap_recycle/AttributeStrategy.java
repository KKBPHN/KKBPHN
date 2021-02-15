package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.util.Util;

class AttributeStrategy implements LruPoolStrategy {
    private final GroupedLinkedMap groupedMap = new GroupedLinkedMap();
    private final KeyPool keyPool = new KeyPool();

    @VisibleForTesting
    class Key implements Poolable {
        private Config config;
        private int height;
        private final KeyPool pool;
        private int width;

        public Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            return this.width == key.width && this.height == key.height && this.config == key.config;
        }

        public int hashCode() {
            int i = ((this.width * 31) + this.height) * 31;
            Config config2 = this.config;
            return i + (config2 != null ? config2.hashCode() : 0);
        }

        public void init(int i, int i2, Config config2) {
            this.width = i;
            this.height = i2;
            this.config = config2;
        }

        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return AttributeStrategy.getBitmapString(this.width, this.height, this.config);
        }
    }

    @VisibleForTesting
    class KeyPool extends BaseKeyPool {
        KeyPool() {
        }

        /* access modifiers changed from: protected */
        public Key create() {
            return new Key(this);
        }

        /* access modifiers changed from: 0000 */
        public Key get(int i, int i2, Config config) {
            Key key = (Key) get();
            key.init(i, i2, config);
            return key;
        }
    }

    AttributeStrategy() {
    }

    static String getBitmapString(int i, int i2, Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(i);
        sb.append("x");
        sb.append(i2);
        sb.append("], ");
        sb.append(config);
        return sb.toString();
    }

    private static String getBitmapString(Bitmap bitmap) {
        return getBitmapString(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
    }

    public Bitmap get(int i, int i2, Config config) {
        return (Bitmap) this.groupedMap.get(this.keyPool.get(i, i2, config));
    }

    public int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    public String logBitmap(int i, int i2, Config config) {
        return getBitmapString(i, i2, config);
    }

    public String logBitmap(Bitmap bitmap) {
        return getBitmapString(bitmap);
    }

    public void put(Bitmap bitmap) {
        this.groupedMap.put(this.keyPool.get(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig()), bitmap);
    }

    public Bitmap removeLast() {
        return (Bitmap) this.groupedMap.removeLast();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AttributeStrategy:\n  ");
        sb.append(this.groupedMap);
        return sb.toString();
    }
}
