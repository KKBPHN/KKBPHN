package androidx.collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class LruCache {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int i) {
        if (i > 0) {
            this.maxSize = i;
            this.map = new LinkedHashMap(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    private int safeSizeOf(Object obj, Object obj2) {
        int sizeOf = sizeOf(obj, obj2);
        if (sizeOf >= 0) {
            return sizeOf;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Negative size: ");
        sb.append(obj);
        sb.append("=");
        sb.append(obj2);
        throw new IllegalStateException(sb.toString());
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Object create(@NonNull Object obj) {
        return null;
    }

    public final synchronized int createCount() {
        return this.createCount;
    }

    /* access modifiers changed from: protected */
    public void entryRemoved(boolean z, @NonNull Object obj, @NonNull Object obj2, @Nullable Object obj3) {
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final synchronized int evictionCount() {
        return this.evictionCount;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001a, code lost:
        r0 = create(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001e, code lost:
        if (r0 != null) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0022, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r4.createCount++;
        r1 = r4.map.put(r5, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
        if (r1 == null) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0031, code lost:
        r4.map.put(r5, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0037, code lost:
        r4.size += safeSizeOf(r5, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0040, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0041, code lost:
        if (r1 == null) goto L_0x0048;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0043, code lost:
        entryRemoved(false, r5, r0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0047, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0048, code lost:
        trimToSize(r4.maxSize);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004d, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    public final Object get(@NonNull Object obj) {
        if (obj != null) {
            synchronized (this) {
                Object obj2 = this.map.get(obj);
                if (obj2 != null) {
                    this.hitCount++;
                    return obj2;
                }
                this.missCount++;
            }
        } else {
            throw new NullPointerException("key == null");
        }
    }

    public final synchronized int hitCount() {
        return this.hitCount;
    }

    public final synchronized int maxSize() {
        return this.maxSize;
    }

    public final synchronized int missCount() {
        return this.missCount;
    }

    @Nullable
    public final Object put(@NonNull Object obj, @NonNull Object obj2) {
        Object put;
        if (obj == null || obj2 == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.putCount++;
            this.size += safeSizeOf(obj, obj2);
            put = this.map.put(obj, obj2);
            if (put != null) {
                this.size -= safeSizeOf(obj, put);
            }
        }
        if (put != null) {
            entryRemoved(false, obj, put, obj2);
        }
        trimToSize(this.maxSize);
        return put;
    }

    public final synchronized int putCount() {
        return this.putCount;
    }

    @Nullable
    public final Object remove(@NonNull Object obj) {
        Object remove;
        if (obj != null) {
            synchronized (this) {
                remove = this.map.remove(obj);
                if (remove != null) {
                    this.size -= safeSizeOf(obj, remove);
                }
            }
            if (remove != null) {
                entryRemoved(false, obj, remove, null);
            }
            return remove;
        }
        throw new NullPointerException("key == null");
    }

    public void resize(int i) {
        if (i > 0) {
            synchronized (this) {
                this.maxSize = i;
            }
            trimToSize(i);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    public final synchronized int size() {
        return this.size;
    }

    /* access modifiers changed from: protected */
    public int sizeOf(@NonNull Object obj, @NonNull Object obj2) {
        return 1;
    }

    public final synchronized Map snapshot() {
        return new LinkedHashMap(this.map);
    }

    public final synchronized String toString() {
        int i;
        i = this.hitCount + this.missCount;
        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(i != 0 ? (this.hitCount * 100) / i : 0)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0070, code lost:
        throw new java.lang.IllegalStateException(r0.toString());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void trimToSize(int i) {
        Object key;
        Object value;
        while (true) {
            synchronized (this) {
                if (this.size < 0 || (this.map.isEmpty() && this.size != 0)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getClass().getName());
                    sb.append(".sizeOf() is reporting inconsistent results!");
                } else if (this.size > i) {
                    if (this.map.isEmpty()) {
                        break;
                    }
                    Entry entry = (Entry) this.map.entrySet().iterator().next();
                    key = entry.getKey();
                    value = entry.getValue();
                    this.map.remove(key);
                    this.size -= safeSizeOf(key, value);
                    this.evictionCount++;
                }
            }
            entryRemoved(true, key, value, null);
        }
    }
}
