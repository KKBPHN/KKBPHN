package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.util.Util;
import java.util.NavigableMap;

@RequiresApi(19)
final class SizeStrategy implements LruPoolStrategy {
    private static final int MAX_SIZE_MULTIPLE = 8;
    private final GroupedLinkedMap groupedMap = new GroupedLinkedMap();
    private final KeyPool keyPool = new KeyPool();
    private final NavigableMap sortedSizes = new PrettyPrintTreeMap();

    @VisibleForTesting
    final class Key implements Poolable {
        private final KeyPool pool;
        int size;

        Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            return this.size == ((Key) obj).size;
        }

        public int hashCode() {
            return this.size;
        }

        public void init(int i) {
            this.size = i;
        }

        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return SizeStrategy.getBitmapString(this.size);
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

        public Key get(int i) {
            Key key = (Key) super.get();
            key.init(i);
            return key;
        }
    }

    SizeStrategy() {
    }

    private void decrementBitmapOfSize(Integer num) {
        Integer num2 = (Integer) this.sortedSizes.get(num);
        int intValue = num2.intValue();
        NavigableMap navigableMap = this.sortedSizes;
        if (intValue == 1) {
            navigableMap.remove(num);
        } else {
            navigableMap.put(num, Integer.valueOf(num2.intValue() - 1));
        }
    }

    static String getBitmapString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(i);
        sb.append("]");
        return sb.toString();
    }

    private static String getBitmapString(Bitmap bitmap) {
        return getBitmapString(Util.getBitmapByteSize(bitmap));
    }

    @Nullable
    public Bitmap get(int i, int i2, Config config) {
        int bitmapByteSize = Util.getBitmapByteSize(i, i2, config);
        Key key = this.keyPool.get(bitmapByteSize);
        Integer num = (Integer) this.sortedSizes.ceilingKey(Integer.valueOf(bitmapByteSize));
        if (!(num == null || num.intValue() == bitmapByteSize || num.intValue() > bitmapByteSize * 8)) {
            this.keyPool.offer(key);
            key = this.keyPool.get(num.intValue());
        }
        Bitmap bitmap = (Bitmap) this.groupedMap.get(key);
        if (bitmap != null) {
            bitmap.reconfigure(i, i2, config);
            decrementBitmapOfSize(num);
        }
        return bitmap;
    }

    public int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    public String logBitmap(int i, int i2, Config config) {
        return getBitmapString(Util.getBitmapByteSize(i, i2, config));
    }

    public String logBitmap(Bitmap bitmap) {
        return getBitmapString(bitmap);
    }

    public void put(Bitmap bitmap) {
        Key key = this.keyPool.get(Util.getBitmapByteSize(bitmap));
        this.groupedMap.put(key, bitmap);
        Integer num = (Integer) this.sortedSizes.get(Integer.valueOf(key.size));
        NavigableMap navigableMap = this.sortedSizes;
        Integer valueOf = Integer.valueOf(key.size);
        int i = 1;
        if (num != null) {
            i = 1 + num.intValue();
        }
        navigableMap.put(valueOf, Integer.valueOf(i));
    }

    @Nullable
    public Bitmap removeLast() {
        Bitmap bitmap = (Bitmap) this.groupedMap.removeLast();
        if (bitmap != null) {
            decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(bitmap)));
        }
        return bitmap;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SizeStrategy:\n  ");
        sb.append(this.groupedMap);
        sb.append("\n  SortedSizes");
        sb.append(this.sortedSizes);
        return sb.toString();
    }
}
