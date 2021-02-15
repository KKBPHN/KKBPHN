package androidx.collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ArrayMap extends SimpleArrayMap implements Map {
    @Nullable
    MapCollections mCollections;

    public ArrayMap() {
    }

    public ArrayMap(int i) {
        super(i);
    }

    public ArrayMap(SimpleArrayMap simpleArrayMap) {
        super(simpleArrayMap);
    }

    private MapCollections getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections() {
                /* access modifiers changed from: protected */
                public void colClear() {
                    ArrayMap.this.clear();
                }

                /* access modifiers changed from: protected */
                public Object colGetEntry(int i, int i2) {
                    return ArrayMap.this.mArray[(i << 1) + i2];
                }

                /* access modifiers changed from: protected */
                public Map colGetMap() {
                    return ArrayMap.this;
                }

                /* access modifiers changed from: protected */
                public int colGetSize() {
                    return ArrayMap.this.mSize;
                }

                /* access modifiers changed from: protected */
                public int colIndexOfKey(Object obj) {
                    return ArrayMap.this.indexOfKey(obj);
                }

                /* access modifiers changed from: protected */
                public int colIndexOfValue(Object obj) {
                    return ArrayMap.this.indexOfValue(obj);
                }

                /* access modifiers changed from: protected */
                public void colPut(Object obj, Object obj2) {
                    ArrayMap.this.put(obj, obj2);
                }

                /* access modifiers changed from: protected */
                public void colRemoveAt(int i) {
                    ArrayMap.this.removeAt(i);
                }

                /* access modifiers changed from: protected */
                public Object colSetValue(int i, Object obj) {
                    return ArrayMap.this.setValueAt(i, obj);
                }
            };
        }
        return this.mCollections;
    }

    public boolean containsAll(@NonNull Collection collection) {
        return MapCollections.containsAllHelper(this, collection);
    }

    public Set entrySet() {
        return getCollection().getEntrySet();
    }

    public Set keySet() {
        return getCollection().getKeySet();
    }

    public void putAll(Map map) {
        ensureCapacity(this.mSize + map.size());
        for (Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public boolean removeAll(@NonNull Collection collection) {
        return MapCollections.removeAllHelper(this, collection);
    }

    public boolean retainAll(@NonNull Collection collection) {
        return MapCollections.retainAllHelper(this, collection);
    }

    public Collection values() {
        return getCollection().getValues();
    }
}
