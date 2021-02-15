package com.bumptech.glide.load.engine.bitmap_recycle;

import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class LruArrayPool implements ArrayPool {
    private static final int DEFAULT_SIZE = 4194304;
    @VisibleForTesting
    static final int MAX_OVER_SIZE_MULTIPLE = 8;
    private static final int SINGLE_ARRAY_MAX_SIZE_DIVISOR = 2;
    private final Map adapters;
    private int currentSize;
    private final GroupedLinkedMap groupedMap;
    private final KeyPool keyPool;
    private final int maxSize;
    private final Map sortedSizes;

    final class Key implements Poolable {
        private Class arrayClass;
        private final KeyPool pool;
        int size;

        Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            return this.size == key.size && this.arrayClass == key.arrayClass;
        }

        public int hashCode() {
            int i = this.size * 31;
            Class cls = this.arrayClass;
            return i + (cls != null ? cls.hashCode() : 0);
        }

        /* access modifiers changed from: 0000 */
        public void init(int i, Class cls) {
            this.size = i;
            this.arrayClass = cls;
        }

        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Key{size=");
            sb.append(this.size);
            sb.append("array=");
            sb.append(this.arrayClass);
            sb.append('}');
            return sb.toString();
        }
    }

    final class KeyPool extends BaseKeyPool {
        KeyPool() {
        }

        /* access modifiers changed from: protected */
        public Key create() {
            return new Key(this);
        }

        /* access modifiers changed from: 0000 */
        public Key get(int i, Class cls) {
            Key key = (Key) get();
            key.init(i, cls);
            return key;
        }
    }

    @VisibleForTesting
    public LruArrayPool() {
        this.groupedMap = new GroupedLinkedMap();
        this.keyPool = new KeyPool();
        this.sortedSizes = new HashMap();
        this.adapters = new HashMap();
        this.maxSize = 4194304;
    }

    public LruArrayPool(int i) {
        this.groupedMap = new GroupedLinkedMap();
        this.keyPool = new KeyPool();
        this.sortedSizes = new HashMap();
        this.adapters = new HashMap();
        this.maxSize = i;
    }

    private void decrementArrayOfSize(int i, Class cls) {
        NavigableMap sizesForAdapter = getSizesForAdapter(cls);
        Integer num = (Integer) sizesForAdapter.get(Integer.valueOf(i));
        if (num == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to decrement empty size, size: ");
            sb.append(i);
            sb.append(", this: ");
            sb.append(this);
            throw new NullPointerException(sb.toString());
        } else if (num.intValue() == 1) {
            sizesForAdapter.remove(Integer.valueOf(i));
        } else {
            sizesForAdapter.put(Integer.valueOf(i), Integer.valueOf(num.intValue() - 1));
        }
    }

    private void evict() {
        evictToSize(this.maxSize);
    }

    private void evictToSize(int i) {
        while (this.currentSize > i) {
            Object removeLast = this.groupedMap.removeLast();
            Preconditions.checkNotNull(removeLast);
            ArrayAdapterInterface adapterFromObject = getAdapterFromObject(removeLast);
            this.currentSize -= adapterFromObject.getArrayLength(removeLast) * adapterFromObject.getElementSizeInBytes();
            decrementArrayOfSize(adapterFromObject.getArrayLength(removeLast), removeLast.getClass());
            if (Log.isLoggable(adapterFromObject.getTag(), 2)) {
                String tag = adapterFromObject.getTag();
                StringBuilder sb = new StringBuilder();
                sb.append("evicted: ");
                sb.append(adapterFromObject.getArrayLength(removeLast));
                Log.v(tag, sb.toString());
            }
        }
    }

    private ArrayAdapterInterface getAdapterFromObject(Object obj) {
        return getAdapterFromType(obj.getClass());
    }

    private ArrayAdapterInterface getAdapterFromType(Class cls) {
        ArrayAdapterInterface arrayAdapterInterface = (ArrayAdapterInterface) this.adapters.get(cls);
        if (arrayAdapterInterface == null) {
            if (cls.equals(int[].class)) {
                arrayAdapterInterface = new IntegerArrayAdapter();
            } else if (cls.equals(byte[].class)) {
                arrayAdapterInterface = new ByteArrayAdapter();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No array pool found for: ");
                sb.append(cls.getSimpleName());
                throw new IllegalArgumentException(sb.toString());
            }
            this.adapters.put(cls, arrayAdapterInterface);
        }
        return arrayAdapterInterface;
    }

    @Nullable
    private Object getArrayForKey(Key key) {
        return this.groupedMap.get(key);
    }

    private Object getForKey(Key key, Class cls) {
        ArrayAdapterInterface adapterFromType = getAdapterFromType(cls);
        Object arrayForKey = getArrayForKey(key);
        if (arrayForKey != null) {
            this.currentSize -= adapterFromType.getArrayLength(arrayForKey) * adapterFromType.getElementSizeInBytes();
            decrementArrayOfSize(adapterFromType.getArrayLength(arrayForKey), cls);
        }
        if (arrayForKey != null) {
            return arrayForKey;
        }
        if (Log.isLoggable(adapterFromType.getTag(), 2)) {
            String tag = adapterFromType.getTag();
            StringBuilder sb = new StringBuilder();
            sb.append("Allocated ");
            sb.append(key.size);
            sb.append(" bytes");
            Log.v(tag, sb.toString());
        }
        return adapterFromType.newArray(key.size);
    }

    private NavigableMap getSizesForAdapter(Class cls) {
        NavigableMap navigableMap = (NavigableMap) this.sortedSizes.get(cls);
        if (navigableMap != null) {
            return navigableMap;
        }
        TreeMap treeMap = new TreeMap();
        this.sortedSizes.put(cls, treeMap);
        return treeMap;
    }

    private boolean isNoMoreThanHalfFull() {
        int i = this.currentSize;
        return i == 0 || this.maxSize / i >= 2;
    }

    private boolean isSmallEnoughForReuse(int i) {
        return i <= this.maxSize / 2;
    }

    private boolean mayFillRequest(int i, Integer num) {
        return num != null && (isNoMoreThanHalfFull() || num.intValue() <= i * 8);
    }

    public synchronized void clearMemory() {
        evictToSize(0);
    }

    public synchronized Object get(int i, Class cls) {
        Integer num;
        num = (Integer) getSizesForAdapter(cls).ceilingKey(Integer.valueOf(i));
        return getForKey(mayFillRequest(i, num) ? this.keyPool.get(num.intValue(), cls) : this.keyPool.get(i, cls), cls);
    }

    /* access modifiers changed from: 0000 */
    public int getCurrentSize() {
        int i = 0;
        for (Class cls : this.sortedSizes.keySet()) {
            for (Integer num : ((NavigableMap) this.sortedSizes.get(cls)).keySet()) {
                i += num.intValue() * ((Integer) ((NavigableMap) this.sortedSizes.get(cls)).get(num)).intValue() * getAdapterFromType(cls).getElementSizeInBytes();
            }
        }
        return i;
    }

    public synchronized Object getExact(int i, Class cls) {
        return getForKey(this.keyPool.get(i, cls), cls);
    }

    public synchronized void put(Object obj) {
        Class cls = obj.getClass();
        ArrayAdapterInterface adapterFromType = getAdapterFromType(cls);
        int arrayLength = adapterFromType.getArrayLength(obj);
        int elementSizeInBytes = adapterFromType.getElementSizeInBytes() * arrayLength;
        if (isSmallEnoughForReuse(elementSizeInBytes)) {
            Key key = this.keyPool.get(arrayLength, cls);
            this.groupedMap.put(key, obj);
            NavigableMap sizesForAdapter = getSizesForAdapter(cls);
            Integer num = (Integer) sizesForAdapter.get(Integer.valueOf(key.size));
            Integer valueOf = Integer.valueOf(key.size);
            int i = 1;
            if (num != null) {
                i = 1 + num.intValue();
            }
            sizesForAdapter.put(valueOf, Integer.valueOf(i));
            this.currentSize += elementSizeInBytes;
            evict();
        }
    }

    @Deprecated
    public void put(Object obj, Class cls) {
        put(obj);
    }

    public synchronized void trimMemory(int i) {
        if (i >= 40) {
            try {
                clearMemory();
            } catch (Throwable th) {
                throw th;
            }
        } else if (i >= 20 || i == 15) {
            evictToSize(this.maxSize / 2);
        }
    }
}
