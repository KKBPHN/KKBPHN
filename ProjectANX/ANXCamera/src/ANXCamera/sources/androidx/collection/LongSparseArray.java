package androidx.collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LongSparseArray implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;

    public LongSparseArray() {
        this(10);
    }

    public LongSparseArray(int i) {
        Object[] objArr;
        this.mGarbage = false;
        if (i == 0) {
            this.mKeys = ContainerHelpers.EMPTY_LONGS;
            objArr = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            int idealLongArraySize = ContainerHelpers.idealLongArraySize(i);
            this.mKeys = new long[idealLongArraySize];
            objArr = new Object[idealLongArraySize];
        }
        this.mValues = objArr;
    }

    private void gc() {
        int i = this.mSize;
        long[] jArr = this.mKeys;
        Object[] objArr = this.mValues;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            Object obj = objArr[i3];
            if (obj != DELETED) {
                if (i3 != i2) {
                    jArr[i2] = jArr[i3];
                    objArr[i2] = obj;
                    objArr[i3] = null;
                }
                i2++;
            }
        }
        this.mGarbage = false;
        this.mSize = i2;
    }

    public void append(long j, Object obj) {
        int i = this.mSize;
        if (i == 0 || j > this.mKeys[i - 1]) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
            }
            int i2 = this.mSize;
            if (i2 >= this.mKeys.length) {
                int idealLongArraySize = ContainerHelpers.idealLongArraySize(i2 + 1);
                long[] jArr = new long[idealLongArraySize];
                Object[] objArr = new Object[idealLongArraySize];
                long[] jArr2 = this.mKeys;
                System.arraycopy(jArr2, 0, jArr, 0, jArr2.length);
                Object[] objArr2 = this.mValues;
                System.arraycopy(objArr2, 0, objArr, 0, objArr2.length);
                this.mKeys = jArr;
                this.mValues = objArr;
            }
            this.mKeys[i2] = j;
            this.mValues[i2] = obj;
            this.mSize = i2 + 1;
            return;
        }
        put(j, obj);
    }

    public void clear() {
        int i = this.mSize;
        Object[] objArr = this.mValues;
        for (int i2 = 0; i2 < i; i2++) {
            objArr[i2] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public LongSparseArray clone() {
        try {
            LongSparseArray longSparseArray = (LongSparseArray) super.clone();
            longSparseArray.mKeys = (long[]) this.mKeys.clone();
            longSparseArray.mValues = (Object[]) this.mValues.clone();
            return longSparseArray;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public boolean containsKey(long j) {
        return indexOfKey(j) >= 0;
    }

    public boolean containsValue(Object obj) {
        return indexOfValue(obj) >= 0;
    }

    @Deprecated
    public void delete(long j) {
        remove(j);
    }

    @Nullable
    public Object get(long j) {
        return get(j, null);
    }

    public Object get(long j, Object obj) {
        int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, j);
        if (binarySearch >= 0) {
            Object[] objArr = this.mValues;
            if (objArr[binarySearch] != DELETED) {
                return objArr[binarySearch];
            }
        }
        return obj;
    }

    public int indexOfKey(long j) {
        if (this.mGarbage) {
            gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, j);
    }

    public int indexOfValue(Object obj) {
        if (this.mGarbage) {
            gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == obj) {
                return i;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public long keyAt(int i) {
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[i];
    }

    public void put(long j, Object obj) {
        int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, j);
        if (binarySearch >= 0) {
            this.mValues[binarySearch] = obj;
        } else {
            int i = ~binarySearch;
            if (i < this.mSize) {
                Object[] objArr = this.mValues;
                if (objArr[i] == DELETED) {
                    this.mKeys[i] = j;
                    objArr[i] = obj;
                    return;
                }
            }
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
                i = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, j);
            }
            int i2 = this.mSize;
            if (i2 >= this.mKeys.length) {
                int idealLongArraySize = ContainerHelpers.idealLongArraySize(i2 + 1);
                long[] jArr = new long[idealLongArraySize];
                Object[] objArr2 = new Object[idealLongArraySize];
                long[] jArr2 = this.mKeys;
                System.arraycopy(jArr2, 0, jArr, 0, jArr2.length);
                Object[] objArr3 = this.mValues;
                System.arraycopy(objArr3, 0, objArr2, 0, objArr3.length);
                this.mKeys = jArr;
                this.mValues = objArr2;
            }
            int i3 = this.mSize;
            if (i3 - i != 0) {
                long[] jArr3 = this.mKeys;
                int i4 = i + 1;
                System.arraycopy(jArr3, i, jArr3, i4, i3 - i);
                Object[] objArr4 = this.mValues;
                System.arraycopy(objArr4, i, objArr4, i4, this.mSize - i);
            }
            this.mKeys[i] = j;
            this.mValues[i] = obj;
            this.mSize++;
        }
    }

    public void putAll(@NonNull LongSparseArray longSparseArray) {
        int size = longSparseArray.size();
        for (int i = 0; i < size; i++) {
            put(longSparseArray.keyAt(i), longSparseArray.valueAt(i));
        }
    }

    @Nullable
    public Object putIfAbsent(long j, Object obj) {
        Object obj2 = get(j);
        if (obj2 == null) {
            put(j, obj);
        }
        return obj2;
    }

    public void remove(long j) {
        int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, j);
        if (binarySearch >= 0) {
            Object[] objArr = this.mValues;
            Object obj = objArr[binarySearch];
            Object obj2 = DELETED;
            if (obj != obj2) {
                objArr[binarySearch] = obj2;
                this.mGarbage = true;
            }
        }
    }

    public boolean remove(long j, Object obj) {
        int indexOfKey = indexOfKey(j);
        if (indexOfKey >= 0) {
            Object valueAt = valueAt(indexOfKey);
            if (obj == valueAt || (obj != null && obj.equals(valueAt))) {
                removeAt(indexOfKey);
                return true;
            }
        }
        return false;
    }

    public void removeAt(int i) {
        Object[] objArr = this.mValues;
        Object obj = objArr[i];
        Object obj2 = DELETED;
        if (obj != obj2) {
            objArr[i] = obj2;
            this.mGarbage = true;
        }
    }

    @Nullable
    public Object replace(long j, Object obj) {
        int indexOfKey = indexOfKey(j);
        if (indexOfKey < 0) {
            return null;
        }
        Object[] objArr = this.mValues;
        Object obj2 = objArr[indexOfKey];
        objArr[indexOfKey] = obj;
        return obj2;
    }

    public boolean replace(long j, Object obj, Object obj2) {
        int indexOfKey = indexOfKey(j);
        if (indexOfKey >= 0) {
            Object obj3 = this.mValues[indexOfKey];
            if (obj3 == obj || (obj != null && obj.equals(obj3))) {
                this.mValues[indexOfKey] = obj2;
                return true;
            }
        }
        return false;
    }

    public void setValueAt(int i, Object obj) {
        if (this.mGarbage) {
            gc();
        }
        this.mValues[i] = obj;
    }

    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public String toString() {
        if (size() <= 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(keyAt(i));
            sb.append('=');
            Object valueAt = valueAt(i);
            if (valueAt != this) {
                sb.append(valueAt);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public Object valueAt(int i) {
        if (this.mGarbage) {
            gc();
        }
        return this.mValues[i];
    }
}
