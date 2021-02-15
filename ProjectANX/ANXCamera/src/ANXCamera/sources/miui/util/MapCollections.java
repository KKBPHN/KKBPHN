package miui.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

abstract class MapCollections {
    EntrySet mEntrySet;
    KeySet mKeySet;
    ValuesCollection mValues;

    final class ArrayIterator implements Iterator {
        boolean mCanRemove = false;
        int mIndex;
        final int mOffset;
        int mSize;

        ArrayIterator(int i) {
            this.mOffset = i;
            this.mSize = MapCollections.this.colGetSize();
        }

        public boolean hasNext() {
            return this.mIndex < this.mSize;
        }

        public Object next() {
            Object colGetEntry = MapCollections.this.colGetEntry(this.mIndex, this.mOffset);
            this.mIndex++;
            this.mCanRemove = true;
            return colGetEntry;
        }

        public void remove() {
            if (this.mCanRemove) {
                this.mIndex--;
                this.mSize--;
                this.mCanRemove = false;
                MapCollections.this.colRemoveAt(this.mIndex);
                return;
            }
            throw new IllegalStateException();
        }
    }

    final class EntrySet implements Set {
        EntrySet() {
        }

        public boolean add(Entry entry) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<java.util.Map$Entry>, for r5v0, types: [java.util.Collection<java.util.Map$Entry>, java.util.Collection] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean addAll(Collection<Entry> collection) {
            int colGetSize = MapCollections.this.colGetSize();
            for (Entry entry : collection) {
                MapCollections.this.colPut(entry.getKey(), entry.getValue());
            }
            return colGetSize != MapCollections.this.colGetSize();
        }

        public void clear() {
            MapCollections.this.colClear();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            int colIndexOfKey = MapCollections.this.colIndexOfKey(entry.getKey());
            if (colIndexOfKey < 0) {
                return false;
            }
            return ContainerHelpers.equal(MapCollections.this.colGetEntry(colIndexOfKey, 1), entry.getValue());
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<java.lang.Object>, for r2v0, types: [java.util.Collection<java.lang.Object>, java.util.Collection] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean containsAll(Collection<Object> collection) {
            for (Object contains : collection) {
                if (!contains(contains)) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(Object obj) {
            return MapCollections.equalsSetHelper(this, obj);
        }

        public int hashCode() {
            int i = 0;
            for (int colGetSize = MapCollections.this.colGetSize() - 1; colGetSize >= 0; colGetSize--) {
                Object colGetEntry = MapCollections.this.colGetEntry(colGetSize, 0);
                Object colGetEntry2 = MapCollections.this.colGetEntry(colGetSize, 1);
                i += (colGetEntry == null ? 0 : colGetEntry.hashCode()) ^ (colGetEntry2 == null ? 0 : colGetEntry2.hashCode());
            }
            return i;
        }

        public boolean isEmpty() {
            return MapCollections.this.colGetSize() == 0;
        }

        public Iterator iterator() {
            return new MapIterator();
        }

        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return MapCollections.this.colGetSize();
        }

        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray(Object[] objArr) {
            throw new UnsupportedOperationException();
        }
    }

    final class KeySet implements Set {
        KeySet() {
        }

        public boolean add(Object obj) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            MapCollections.this.colClear();
        }

        public boolean contains(Object obj) {
            return MapCollections.this.colIndexOfKey(obj) >= 0;
        }

        public boolean containsAll(Collection collection) {
            return MapCollections.containsAllHelper(MapCollections.this.colGetMap(), collection);
        }

        public boolean equals(Object obj) {
            return MapCollections.equalsSetHelper(this, obj);
        }

        public int hashCode() {
            int i = 0;
            for (int colGetSize = MapCollections.this.colGetSize() - 1; colGetSize >= 0; colGetSize--) {
                Object colGetEntry = MapCollections.this.colGetEntry(colGetSize, 0);
                i += colGetEntry == null ? 0 : colGetEntry.hashCode();
            }
            return i;
        }

        public boolean isEmpty() {
            return MapCollections.this.colGetSize() == 0;
        }

        public Iterator iterator() {
            return new ArrayIterator(0);
        }

        public boolean remove(Object obj) {
            int colIndexOfKey = MapCollections.this.colIndexOfKey(obj);
            if (colIndexOfKey < 0) {
                return false;
            }
            MapCollections.this.colRemoveAt(colIndexOfKey);
            return true;
        }

        public boolean removeAll(Collection collection) {
            return MapCollections.removeAllHelper(MapCollections.this.colGetMap(), collection);
        }

        public boolean retainAll(Collection collection) {
            return MapCollections.retainAllHelper(MapCollections.this.colGetMap(), collection);
        }

        public int size() {
            return MapCollections.this.colGetSize();
        }

        public Object[] toArray() {
            return MapCollections.this.toArrayHelper(0);
        }

        public Object[] toArray(Object[] objArr) {
            return MapCollections.this.toArrayHelper(objArr, 0);
        }
    }

    final class MapIterator implements Iterator, Entry {
        int mEnd;
        boolean mEntryValid = false;
        int mIndex;

        MapIterator() {
            this.mEnd = MapCollections.this.colGetSize() - 1;
            this.mIndex = -1;
        }

        public final boolean equals(Object obj) {
            if (this.mEntryValid) {
                boolean z = false;
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry entry = (Entry) obj;
                if (ContainerHelpers.equal(entry.getKey(), MapCollections.this.colGetEntry(this.mIndex, 0)) && ContainerHelpers.equal(entry.getValue(), MapCollections.this.colGetEntry(this.mIndex, 1))) {
                    z = true;
                }
                return z;
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public Object getKey() {
            if (this.mEntryValid) {
                return MapCollections.this.colGetEntry(this.mIndex, 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public Object getValue() {
            if (this.mEntryValid) {
                return MapCollections.this.colGetEntry(this.mIndex, 1);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }

        public final int hashCode() {
            if (this.mEntryValid) {
                int i = 0;
                Object colGetEntry = MapCollections.this.colGetEntry(this.mIndex, 0);
                Object colGetEntry2 = MapCollections.this.colGetEntry(this.mIndex, 1);
                int hashCode = colGetEntry == null ? 0 : colGetEntry.hashCode();
                if (colGetEntry2 != null) {
                    i = colGetEntry2.hashCode();
                }
                return hashCode ^ i;
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public Entry next() {
            this.mIndex++;
            this.mEntryValid = true;
            return this;
        }

        public void remove() {
            if (this.mEntryValid) {
                this.mIndex--;
                this.mEnd--;
                this.mEntryValid = false;
                MapCollections.this.colRemoveAt(this.mIndex);
                return;
            }
            throw new IllegalStateException();
        }

        public Object setValue(Object obj) {
            if (this.mEntryValid) {
                return MapCollections.this.colSetValue(this.mIndex, obj);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public final String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getKey());
            sb.append("=");
            sb.append(getValue());
            return sb.toString();
        }
    }

    final class ValuesCollection implements Collection {
        ValuesCollection() {
        }

        public boolean add(Object obj) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            MapCollections.this.colClear();
        }

        public boolean contains(Object obj) {
            return MapCollections.this.colIndexOfValue(obj) >= 0;
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<java.lang.Object>, for r2v0, types: [java.util.Collection<java.lang.Object>, java.util.Collection] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean containsAll(Collection<Object> collection) {
            for (Object contains : collection) {
                if (!contains(contains)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            return MapCollections.this.colGetSize() == 0;
        }

        public Iterator iterator() {
            return new ArrayIterator(1);
        }

        public boolean remove(Object obj) {
            int colIndexOfValue = MapCollections.this.colIndexOfValue(obj);
            if (colIndexOfValue < 0) {
                return false;
            }
            MapCollections.this.colRemoveAt(colIndexOfValue);
            return true;
        }

        public boolean removeAll(Collection collection) {
            int colGetSize = MapCollections.this.colGetSize();
            int i = 0;
            boolean z = false;
            while (i < colGetSize) {
                if (collection.contains(MapCollections.this.colGetEntry(i, 1))) {
                    MapCollections.this.colRemoveAt(i);
                    i--;
                    colGetSize--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        public boolean retainAll(Collection collection) {
            int colGetSize = MapCollections.this.colGetSize();
            int i = 0;
            boolean z = false;
            while (i < colGetSize) {
                if (!collection.contains(MapCollections.this.colGetEntry(i, 1))) {
                    MapCollections.this.colRemoveAt(i);
                    i--;
                    colGetSize--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        public int size() {
            return MapCollections.this.colGetSize();
        }

        public Object[] toArray() {
            return MapCollections.this.toArrayHelper(1);
        }

        public Object[] toArray(Object[] objArr) {
            return MapCollections.this.toArrayHelper(objArr, 1);
        }
    }

    MapCollections() {
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<java.lang.Object>, for r2v0, types: [java.util.Collection<java.lang.Object>, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean containsAllHelper(Map map, Collection<Object> collection) {
        for (Object containsKey : collection) {
            if (!map.containsKey(containsKey)) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalsSetHelper(Set set, Object obj) {
        boolean z = true;
        if (set == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set2 = (Set) obj;
            try {
                if (set.size() != set2.size() || !set.containsAll(set2)) {
                    z = false;
                }
                return z;
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<java.lang.Object>, for r3v0, types: [java.util.Collection<java.lang.Object>, java.util.Collection] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean removeAllHelper(Map map, Collection<Object> collection) {
        int size = map.size();
        for (Object remove : collection) {
            map.remove(remove);
        }
        return size != map.size();
    }

    public static boolean retainAllHelper(Map map, Collection collection) {
        int size = map.size();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        }
        return size != map.size();
    }

    public abstract void colClear();

    public abstract Object colGetEntry(int i, int i2);

    public abstract Map colGetMap();

    public abstract int colGetSize();

    public abstract int colIndexOfKey(Object obj);

    public abstract int colIndexOfValue(Object obj);

    public abstract void colPut(Object obj, Object obj2);

    public abstract void colRemoveAt(int i);

    public abstract Object colSetValue(int i, Object obj);

    public Set getEntrySet() {
        if (this.mEntrySet == null) {
            this.mEntrySet = new EntrySet();
        }
        return this.mEntrySet;
    }

    public Set getKeySet() {
        if (this.mKeySet == null) {
            this.mKeySet = new KeySet();
        }
        return this.mKeySet;
    }

    public Collection getValues() {
        if (this.mValues == null) {
            this.mValues = new ValuesCollection();
        }
        return this.mValues;
    }

    public Object[] toArrayHelper(int i) {
        int colGetSize = colGetSize();
        Object[] objArr = new Object[colGetSize];
        for (int i2 = 0; i2 < colGetSize; i2++) {
            objArr[i2] = colGetEntry(i2, i);
        }
        return objArr;
    }

    public Object[] toArrayHelper(Object[] objArr, int i) {
        int colGetSize = colGetSize();
        if (objArr.length < colGetSize) {
            objArr = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), colGetSize);
        }
        for (int i2 = 0; i2 < colGetSize; i2++) {
            objArr[i2] = colGetEntry(i2, i);
        }
        if (objArr.length > colGetSize) {
            objArr[colGetSize] = null;
        }
        return objArr;
    }
}
