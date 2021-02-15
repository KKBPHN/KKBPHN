package miui.util;

import android.util.Log;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class ArrayMap implements Map {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    MapCollections mCollections;
    int[] mHashes;
    int mSize;

    public ArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public ArrayMap(int i) {
        if (i == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            allocArrays(i);
        }
        this.mSize = 0;
    }

    public ArrayMap(ArrayMap arrayMap) {
        this();
        if (arrayMap != null) {
            putAll(arrayMap);
        }
    }

    private void allocArrays(int i) {
        if (i == 8) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCache != null) {
                    Object[] objArr = mTwiceBaseCache;
                    this.mArray = objArr;
                    mTwiceBaseCache = (Object[]) objArr[0];
                    this.mHashes = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    mTwiceBaseCacheSize--;
                    return;
                }
            }
        } else if (i == 4) {
            synchronized (ArrayMap.class) {
                if (mBaseCache != null) {
                    Object[] objArr2 = mBaseCache;
                    this.mArray = objArr2;
                    mBaseCache = (Object[]) objArr2[0];
                    this.mHashes = (int[]) objArr2[1];
                    objArr2[1] = null;
                    objArr2[0] = null;
                    mBaseCacheSize--;
                    return;
                }
            }
        }
        this.mHashes = new int[i];
        this.mArray = new Object[(i << 1)];
    }

    private static void freeArrays(int[] iArr, Object[] objArr, int i) {
        if (iArr.length == 8) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCacheSize < 10) {
                    objArr[0] = mTwiceBaseCache;
                    objArr[1] = iArr;
                    for (int i2 = (i << 1) - 1; i2 >= 2; i2--) {
                        objArr[i2] = null;
                    }
                    mTwiceBaseCache = objArr;
                    mTwiceBaseCacheSize++;
                }
            }
        } else if (iArr.length == 4) {
            synchronized (ArrayMap.class) {
                if (mBaseCacheSize < 10) {
                    objArr[0] = mBaseCache;
                    objArr[1] = iArr;
                    for (int i3 = (i << 1) - 1; i3 >= 2; i3--) {
                        objArr[i3] = null;
                    }
                    mBaseCache = objArr;
                    mBaseCacheSize++;
                }
            }
        }
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
                    ArrayMap arrayMap = ArrayMap.this;
                    return obj == null ? arrayMap.indexOfNull() : arrayMap.indexOf(obj, obj.hashCode());
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

    public void append(Object obj, Object obj2) {
        int i = this.mSize;
        int hashCode = obj == null ? 0 : obj.hashCode();
        int[] iArr = this.mHashes;
        if (i < iArr.length) {
            if (i > 0) {
                int i2 = i - 1;
                if (iArr[i2] > hashCode) {
                    RuntimeException runtimeException = new RuntimeException("here");
                    runtimeException.fillInStackTrace();
                    StringBuilder sb = new StringBuilder();
                    sb.append("New hash ");
                    sb.append(hashCode);
                    sb.append(" is before end of array hash ");
                    sb.append(this.mHashes[i2]);
                    sb.append(" at index ");
                    sb.append(i);
                    sb.append(" key ");
                    sb.append(obj);
                    Log.w(TAG, sb.toString(), runtimeException);
                    put(obj, obj2);
                    return;
                }
            }
            this.mSize = i + 1;
            this.mHashes[i] = hashCode;
            int i3 = i << 1;
            Object[] objArr = this.mArray;
            objArr[i3] = obj;
            objArr[i3 + 1] = obj2;
            return;
        }
        throw new IllegalStateException("Array is full");
    }

    public void clear() {
        int i = this.mSize;
        if (i > 0) {
            freeArrays(this.mHashes, this.mArray, i);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        }
    }

    public boolean containsAll(Collection collection) {
        return MapCollections.containsAllHelper(this, collection);
    }

    public boolean containsKey(Object obj) {
        if (obj == null) {
            if (indexOfNull() >= 0) {
                return true;
            }
        } else if (indexOf(obj, obj.hashCode()) >= 0) {
            return true;
        }
        return false;
    }

    public boolean containsValue(Object obj) {
        return indexOfValue(obj) >= 0;
    }

    public void ensureCapacity(int i) {
        int[] iArr = this.mHashes;
        if (iArr.length < i) {
            Object[] objArr = this.mArray;
            allocArrays(i);
            int i2 = this.mSize;
            if (i2 > 0) {
                System.arraycopy(iArr, 0, this.mHashes, 0, i2);
                System.arraycopy(objArr, 0, this.mArray, 0, this.mSize << 1);
            }
            freeArrays(iArr, objArr, this.mSize);
        }
    }

    public Set entrySet() {
        return getCollection().getEntrySet();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (size() != map.size()) {
                return false;
            }
            int i = 0;
            while (i < this.mSize) {
                try {
                    Object keyAt = keyAt(i);
                    Object valueAt = valueAt(i);
                    Object obj2 = map.get(keyAt);
                    if (valueAt == null) {
                        if (obj2 != null || !map.containsKey(keyAt)) {
                            return false;
                        }
                    } else if (!valueAt.equals(obj2)) {
                        return false;
                    }
                    i++;
                } catch (ClassCastException | NullPointerException unused) {
                }
            }
            return true;
        }
        return false;
    }

    public void erase() {
        int i = this.mSize;
        if (i > 0) {
            int i2 = i << 1;
            Object[] objArr = this.mArray;
            for (int i3 = 0; i3 < i2; i3++) {
                objArr[i3] = null;
            }
            this.mSize = 0;
        }
    }

    public Object get(Object obj) {
        int indexOfNull = obj == null ? indexOfNull() : indexOf(obj, obj.hashCode());
        if (indexOfNull >= 0) {
            return this.mArray[(indexOfNull << 1) + 1];
        }
        return null;
    }

    public int hashCode() {
        int[] iArr = this.mHashes;
        Object[] objArr = this.mArray;
        int i = this.mSize;
        int i2 = 1;
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            Object obj = objArr[i2];
            i4 += (obj == null ? 0 : obj.hashCode()) ^ iArr[i3];
            i3++;
            i2 += 2;
        }
        return i4;
    }

    /* access modifiers changed from: 0000 */
    public int indexOf(Object obj, int i) {
        int i2 = this.mSize;
        if (i2 == 0) {
            return -1;
        }
        int binarySearch = ContainerHelpers.binarySearch(this.mHashes, i2, i);
        if (binarySearch < 0 || obj.equals(this.mArray[binarySearch << 1])) {
            return binarySearch;
        }
        int i3 = binarySearch + 1;
        while (i3 < i2 && this.mHashes[i3] == i) {
            if (obj.equals(this.mArray[i3 << 1])) {
                return i3;
            }
            i3++;
        }
        int i4 = binarySearch - 1;
        while (i4 >= 0 && this.mHashes[i4] == i) {
            if (obj.equals(this.mArray[i4 << 1])) {
                return i4;
            }
            i4--;
        }
        return ~i3;
    }

    /* access modifiers changed from: 0000 */
    public int indexOfNull() {
        int i = this.mSize;
        if (i == 0) {
            return -1;
        }
        int binarySearch = ContainerHelpers.binarySearch(this.mHashes, i, 0);
        if (binarySearch < 0 || this.mArray[binarySearch << 1] == null) {
            return binarySearch;
        }
        int i2 = binarySearch + 1;
        while (i2 < i && this.mHashes[i2] == 0) {
            if (this.mArray[i2 << 1] == null) {
                return i2;
            }
            i2++;
        }
        int i3 = binarySearch - 1;
        while (i3 >= 0 && this.mHashes[i3] == 0) {
            if (this.mArray[i3 << 1] == null) {
                return i3;
            }
            i3--;
        }
        return ~i2;
    }

    /* access modifiers changed from: 0000 */
    public int indexOfValue(Object obj) {
        int i = this.mSize * 2;
        Object[] objArr = this.mArray;
        if (obj == null) {
            for (int i2 = 1; i2 < i; i2 += 2) {
                if (objArr[i2] == null) {
                    return i2 >> 1;
                }
            }
        } else {
            for (int i3 = 1; i3 < i; i3 += 2) {
                if (obj.equals(objArr[i3])) {
                    return i3 >> 1;
                }
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return this.mSize <= 0;
    }

    public Object keyAt(int i) {
        return this.mArray[i << 1];
    }

    public Set keySet() {
        return getCollection().getKeySet();
    }

    public Object put(Object obj, Object obj2) {
        int i;
        int i2;
        if (obj == null) {
            i2 = indexOfNull();
            i = 0;
        } else {
            int hashCode = obj.hashCode();
            i = hashCode;
            i2 = indexOf(obj, hashCode);
        }
        if (i2 >= 0) {
            int i3 = (i2 << 1) + 1;
            Object[] objArr = this.mArray;
            Object obj3 = objArr[i3];
            objArr[i3] = obj2;
            return obj3;
        }
        int i4 = ~i2;
        int i5 = this.mSize;
        if (i5 >= this.mHashes.length) {
            int i6 = 4;
            if (i5 >= 8) {
                i6 = (i5 >> 1) + i5;
            } else if (i5 >= 4) {
                i6 = 8;
            }
            int[] iArr = this.mHashes;
            Object[] objArr2 = this.mArray;
            allocArrays(i6);
            int[] iArr2 = this.mHashes;
            if (iArr2.length > 0) {
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                System.arraycopy(objArr2, 0, this.mArray, 0, objArr2.length);
            }
            freeArrays(iArr, objArr2, this.mSize);
        }
        int i7 = this.mSize;
        if (i4 < i7) {
            int[] iArr3 = this.mHashes;
            int i8 = i4 + 1;
            System.arraycopy(iArr3, i4, iArr3, i8, i7 - i4);
            Object[] objArr3 = this.mArray;
            System.arraycopy(objArr3, i4 << 1, objArr3, i8 << 1, (this.mSize - i4) << 1);
        }
        this.mHashes[i4] = i;
        Object[] objArr4 = this.mArray;
        int i9 = i4 << 1;
        objArr4[i9] = obj;
        objArr4[i9 + 1] = obj2;
        this.mSize++;
        return null;
    }

    public void putAll(Map map) {
        ensureCapacity(this.mSize + map.size());
        for (Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public void putAll(ArrayMap arrayMap) {
        int i = arrayMap.mSize;
        ensureCapacity(this.mSize + i);
        if (this.mSize != 0) {
            for (int i2 = 0; i2 < i; i2++) {
                put(arrayMap.keyAt(i2), arrayMap.valueAt(i2));
            }
        } else if (i > 0) {
            System.arraycopy(arrayMap.mHashes, 0, this.mHashes, 0, i);
            System.arraycopy(arrayMap.mArray, 0, this.mArray, 0, i << 1);
            this.mSize = i;
        }
    }

    public Object remove(Object obj) {
        int indexOfNull = obj == null ? indexOfNull() : indexOf(obj, obj.hashCode());
        if (indexOfNull >= 0) {
            return removeAt(indexOfNull);
        }
        return null;
    }

    public boolean removeAll(Collection collection) {
        return MapCollections.removeAllHelper(this, collection);
    }

    public Object removeAt(int i) {
        Object[] objArr = this.mArray;
        int i2 = i << 1;
        Object obj = objArr[i2 + 1];
        int i3 = this.mSize;
        if (i3 <= 1) {
            freeArrays(this.mHashes, objArr, i3);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        } else {
            int[] iArr = this.mHashes;
            int i4 = 8;
            if (iArr.length <= 8 || i3 >= iArr.length / 3) {
                this.mSize--;
                int i5 = this.mSize;
                if (i < i5) {
                    int[] iArr2 = this.mHashes;
                    int i6 = i + 1;
                    System.arraycopy(iArr2, i6, iArr2, i, i5 - i);
                    Object[] objArr2 = this.mArray;
                    System.arraycopy(objArr2, i6 << 1, objArr2, i2, (this.mSize - i) << 1);
                }
                Object[] objArr3 = this.mArray;
                int i7 = this.mSize;
                objArr3[i7 << 1] = null;
                objArr3[(i7 << 1) + 1] = null;
            } else {
                if (i3 > 8) {
                    i4 = i3 + (i3 >> 1);
                }
                int[] iArr3 = this.mHashes;
                Object[] objArr4 = this.mArray;
                allocArrays(i4);
                this.mSize--;
                if (i > 0) {
                    System.arraycopy(iArr3, 0, this.mHashes, 0, i);
                    System.arraycopy(objArr4, 0, this.mArray, 0, i2);
                }
                int i8 = this.mSize;
                if (i < i8) {
                    int i9 = i + 1;
                    System.arraycopy(iArr3, i9, this.mHashes, i, i8 - i);
                    System.arraycopy(objArr4, i9 << 1, this.mArray, i2, (this.mSize - i) << 1);
                }
            }
        }
        return obj;
    }

    public boolean retainAll(Collection collection) {
        return MapCollections.retainAllHelper(this, collection);
    }

    public Object setValueAt(int i, Object obj) {
        int i2 = (i << 1) + 1;
        Object[] objArr = this.mArray;
        Object obj2 = objArr[i2];
        objArr[i2] = obj;
        return obj2;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            Object keyAt = keyAt(i);
            String str = "(this Map)";
            if (keyAt != this) {
                sb.append(keyAt);
            } else {
                sb.append(str);
            }
            sb.append('=');
            Object valueAt = valueAt(i);
            if (valueAt != this) {
                sb.append(valueAt);
            } else {
                sb.append(str);
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public Object valueAt(int i) {
        return this.mArray[(i << 1) + 1];
    }

    public Collection values() {
        return getCollection().getValues();
    }
}
