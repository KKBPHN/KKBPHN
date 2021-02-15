package androidx.recyclerview.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList {
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    Object[] mData;
    private int mNewDataStart;
    private Object[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class mTClass;

    public class BatchedCallback extends Callback {
        private final BatchingListUpdateCallback mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
        final Callback mWrappedCallback;

        public BatchedCallback(Callback callback) {
            this.mWrappedCallback = callback;
        }

        public boolean areContentsTheSame(Object obj, Object obj2) {
            return this.mWrappedCallback.areContentsTheSame(obj, obj2);
        }

        public boolean areItemsTheSame(Object obj, Object obj2) {
            return this.mWrappedCallback.areItemsTheSame(obj, obj2);
        }

        public int compare(Object obj, Object obj2) {
            return this.mWrappedCallback.compare(obj, obj2);
        }

        public void dispatchLastEvent() {
            this.mBatchingListUpdateCallback.dispatchLastEvent();
        }

        @Nullable
        public Object getChangePayload(Object obj, Object obj2) {
            return this.mWrappedCallback.getChangePayload(obj, obj2);
        }

        public void onChanged(int i, int i2) {
            this.mBatchingListUpdateCallback.onChanged(i, i2, null);
        }

        public void onChanged(int i, int i2, Object obj) {
            this.mBatchingListUpdateCallback.onChanged(i, i2, obj);
        }

        public void onInserted(int i, int i2) {
            this.mBatchingListUpdateCallback.onInserted(i, i2);
        }

        public void onMoved(int i, int i2) {
            this.mBatchingListUpdateCallback.onMoved(i, i2);
        }

        public void onRemoved(int i, int i2) {
            this.mBatchingListUpdateCallback.onRemoved(i, i2);
        }
    }

    public abstract class Callback implements Comparator, ListUpdateCallback {
        public abstract boolean areContentsTheSame(Object obj, Object obj2);

        public abstract boolean areItemsTheSame(Object obj, Object obj2);

        public abstract int compare(Object obj, Object obj2);

        @Nullable
        public Object getChangePayload(Object obj, Object obj2) {
            return null;
        }

        public abstract void onChanged(int i, int i2);

        public void onChanged(int i, int i2, Object obj) {
            onChanged(i, i2);
        }
    }

    public SortedList(@NonNull Class cls, @NonNull Callback callback) {
        this(cls, callback, 10);
    }

    public SortedList(@NonNull Class cls, @NonNull Callback callback, int i) {
        this.mTClass = cls;
        this.mData = (Object[]) Array.newInstance(cls, i);
        this.mCallback = callback;
        this.mSize = 0;
    }

    private int add(Object obj, boolean z) {
        int findIndexOf = findIndexOf(obj, this.mData, 0, this.mSize, 1);
        if (findIndexOf == -1) {
            findIndexOf = 0;
        } else if (findIndexOf < this.mSize) {
            Object obj2 = this.mData[findIndexOf];
            if (this.mCallback.areItemsTheSame(obj2, obj)) {
                if (this.mCallback.areContentsTheSame(obj2, obj)) {
                    this.mData[findIndexOf] = obj;
                    return findIndexOf;
                }
                this.mData[findIndexOf] = obj;
                Callback callback = this.mCallback;
                callback.onChanged(findIndexOf, 1, callback.getChangePayload(obj2, obj));
                return findIndexOf;
            }
        }
        addToData(findIndexOf, obj);
        if (z) {
            this.mCallback.onInserted(findIndexOf, 1);
        }
        return findIndexOf;
    }

    private void addAllInternal(Object[] objArr) {
        if (objArr.length >= 1) {
            int sortAndDedup = sortAndDedup(objArr);
            if (this.mSize == 0) {
                this.mData = objArr;
                this.mSize = sortAndDedup;
                this.mCallback.onInserted(0, sortAndDedup);
            } else {
                merge(objArr, sortAndDedup);
            }
        }
    }

    private void addToData(int i, Object obj) {
        int i2 = this.mSize;
        if (i <= i2) {
            Object[] objArr = this.mData;
            if (i2 == objArr.length) {
                Object[] objArr2 = (Object[]) Array.newInstance(this.mTClass, objArr.length + 10);
                System.arraycopy(this.mData, 0, objArr2, 0, i);
                objArr2[i] = obj;
                System.arraycopy(this.mData, i, objArr2, i + 1, this.mSize - i);
                this.mData = objArr2;
            } else {
                System.arraycopy(objArr, i, objArr, i + 1, i2 - i);
                this.mData[i] = obj;
            }
            this.mSize++;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("cannot add item to ");
        sb.append(i);
        sb.append(" because size is ");
        sb.append(this.mSize);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    private Object[] copyArray(Object[] objArr) {
        Object[] objArr2 = (Object[]) Array.newInstance(this.mTClass, objArr.length);
        System.arraycopy(objArr, 0, objArr2, 0, objArr.length);
        return objArr2;
    }

    private int findIndexOf(Object obj, Object[] objArr, int i, int i2, int i3) {
        while (i < i2) {
            int i4 = (i + i2) / 2;
            Object obj2 = objArr[i4];
            int compare = this.mCallback.compare(obj2, obj);
            if (compare < 0) {
                i = i4 + 1;
            } else if (compare != 0) {
                i2 = i4;
            } else if (this.mCallback.areItemsTheSame(obj2, obj)) {
                return i4;
            } else {
                int linearEqualitySearch = linearEqualitySearch(obj, i4, i, i2);
                if (i3 == 1 && linearEqualitySearch == -1) {
                    linearEqualitySearch = i4;
                }
                return linearEqualitySearch;
            }
        }
        if (i3 != 1) {
            i = -1;
        }
        return i;
    }

    private int findSameItem(Object obj, Object[] objArr, int i, int i2) {
        while (i < i2) {
            if (this.mCallback.areItemsTheSame(objArr[i], obj)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private int linearEqualitySearch(Object obj, int i, int i2, int i3) {
        Object obj2;
        int i4 = i - 1;
        while (i4 >= i2) {
            Object obj3 = this.mData[i4];
            if (this.mCallback.compare(obj3, obj) != 0) {
                break;
            } else if (this.mCallback.areItemsTheSame(obj3, obj)) {
                return i4;
            } else {
                i4--;
            }
        }
        do {
            i++;
            if (i < i3) {
                obj2 = this.mData[i];
                if (this.mCallback.compare(obj2, obj) != 0) {
                }
            }
            return -1;
        } while (!this.mCallback.areItemsTheSame(obj2, obj));
        return i;
    }

    private void merge(Object[] objArr, int i) {
        int i2 = 0;
        boolean z = !(this.mCallback instanceof BatchedCallback);
        if (z) {
            beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        int i3 = this.mSize;
        this.mOldDataSize = i3;
        this.mData = (Object[]) Array.newInstance(this.mTClass, i3 + i + 10);
        this.mNewDataStart = 0;
        while (true) {
            if (this.mOldDataStart >= this.mOldDataSize && i2 >= i) {
                break;
            }
            int i4 = this.mOldDataStart;
            int i5 = this.mOldDataSize;
            if (i4 == i5) {
                int i6 = i - i2;
                System.arraycopy(objArr, i2, this.mData, this.mNewDataStart, i6);
                this.mNewDataStart += i6;
                this.mSize += i6;
                this.mCallback.onInserted(this.mNewDataStart - i6, i6);
                break;
            } else if (i2 == i) {
                int i7 = i5 - i4;
                System.arraycopy(this.mOldData, i4, this.mData, this.mNewDataStart, i7);
                this.mNewDataStart += i7;
                break;
            } else {
                Object obj = this.mOldData[i4];
                Object obj2 = objArr[i2];
                int compare = this.mCallback.compare(obj, obj2);
                if (compare > 0) {
                    Object[] objArr2 = this.mData;
                    int i8 = this.mNewDataStart;
                    this.mNewDataStart = i8 + 1;
                    objArr2[i8] = obj2;
                    this.mSize++;
                    i2++;
                    this.mCallback.onInserted(this.mNewDataStart - 1, 1);
                } else if (compare != 0 || !this.mCallback.areItemsTheSame(obj, obj2)) {
                    Object[] objArr3 = this.mData;
                    int i9 = this.mNewDataStart;
                    this.mNewDataStart = i9 + 1;
                    objArr3[i9] = obj;
                    this.mOldDataStart++;
                } else {
                    Object[] objArr4 = this.mData;
                    int i10 = this.mNewDataStart;
                    this.mNewDataStart = i10 + 1;
                    objArr4[i10] = obj2;
                    i2++;
                    this.mOldDataStart++;
                    if (!this.mCallback.areContentsTheSame(obj, obj2)) {
                        Callback callback = this.mCallback;
                        callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(obj, obj2));
                    }
                }
            }
        }
        this.mOldData = null;
        if (z) {
            endBatchedUpdates();
        }
    }

    private boolean remove(Object obj, boolean z) {
        int findIndexOf = findIndexOf(obj, this.mData, 0, this.mSize, 2);
        if (findIndexOf == -1) {
            return false;
        }
        removeItemAtIndex(findIndexOf, z);
        return true;
    }

    private void removeItemAtIndex(int i, boolean z) {
        Object[] objArr = this.mData;
        System.arraycopy(objArr, i + 1, objArr, i, (this.mSize - i) - 1);
        this.mSize--;
        this.mData[this.mSize] = null;
        if (z) {
            this.mCallback.onRemoved(i, 1);
        }
    }

    private void replaceAllInsert(Object obj) {
        Object[] objArr = this.mData;
        int i = this.mNewDataStart;
        objArr[i] = obj;
        this.mNewDataStart = i + 1;
        this.mSize++;
        this.mCallback.onInserted(this.mNewDataStart - 1, 1);
    }

    private void replaceAllInternal(@NonNull Object[] objArr) {
        boolean z = !(this.mCallback instanceof BatchedCallback);
        if (z) {
            beginBatchedUpdates();
        }
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        this.mOldData = this.mData;
        this.mNewDataStart = 0;
        int sortAndDedup = sortAndDedup(objArr);
        this.mData = (Object[]) Array.newInstance(this.mTClass, sortAndDedup);
        while (true) {
            if (this.mNewDataStart >= sortAndDedup && this.mOldDataStart >= this.mOldDataSize) {
                break;
            }
            int i = this.mOldDataStart;
            int i2 = this.mOldDataSize;
            if (i >= i2) {
                int i3 = this.mNewDataStart;
                int i4 = sortAndDedup - i3;
                System.arraycopy(objArr, i3, this.mData, i3, i4);
                this.mNewDataStart += i4;
                this.mSize += i4;
                this.mCallback.onInserted(i3, i4);
                break;
            }
            int i5 = this.mNewDataStart;
            if (i5 >= sortAndDedup) {
                int i6 = i2 - i;
                this.mSize -= i6;
                this.mCallback.onRemoved(i5, i6);
                break;
            }
            Object obj = this.mOldData[i];
            Object obj2 = objArr[i5];
            int compare = this.mCallback.compare(obj, obj2);
            if (compare < 0) {
                replaceAllRemove();
            } else {
                if (compare <= 0) {
                    if (!this.mCallback.areItemsTheSame(obj, obj2)) {
                        replaceAllRemove();
                    } else {
                        Object[] objArr2 = this.mData;
                        int i7 = this.mNewDataStart;
                        objArr2[i7] = obj2;
                        this.mOldDataStart++;
                        this.mNewDataStart = i7 + 1;
                        if (!this.mCallback.areContentsTheSame(obj, obj2)) {
                            Callback callback = this.mCallback;
                            callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(obj, obj2));
                        }
                    }
                }
                replaceAllInsert(obj2);
            }
        }
        this.mOldData = null;
        if (z) {
            endBatchedUpdates();
        }
    }

    private void replaceAllRemove() {
        this.mSize--;
        this.mOldDataStart++;
        this.mCallback.onRemoved(this.mNewDataStart, 1);
    }

    private int sortAndDedup(@NonNull Object[] objArr) {
        if (objArr.length == 0) {
            return 0;
        }
        Arrays.sort(objArr, this.mCallback);
        int i = 0;
        int i2 = 1;
        for (int i3 = 1; i3 < objArr.length; i3++) {
            Object obj = objArr[i3];
            if (this.mCallback.compare(objArr[i], obj) == 0) {
                int findSameItem = findSameItem(obj, objArr, i, i2);
                if (findSameItem != -1) {
                    objArr[findSameItem] = obj;
                } else {
                    if (i2 != i3) {
                        objArr[i2] = obj;
                    }
                    i2++;
                }
            } else {
                if (i2 != i3) {
                    objArr[i2] = obj;
                }
                i = i2;
                i2++;
            }
        }
        return i2;
    }

    private void throwIfInMutationOperation() {
        if (this.mOldData != null) {
            throw new IllegalStateException("Data cannot be mutated in the middle of a batch update operation such as addAll or replaceAll.");
        }
    }

    public int add(Object obj) {
        throwIfInMutationOperation();
        return add(obj, true);
    }

    public void addAll(@NonNull Collection collection) {
        addAll(collection.toArray((Object[]) Array.newInstance(this.mTClass, collection.size())), true);
    }

    public void addAll(@NonNull Object... objArr) {
        addAll(objArr, false);
    }

    public void addAll(@NonNull Object[] objArr, boolean z) {
        throwIfInMutationOperation();
        if (objArr.length != 0) {
            if (!z) {
                objArr = copyArray(objArr);
            }
            addAllInternal(objArr);
        }
    }

    public void beginBatchedUpdates() {
        throwIfInMutationOperation();
        Callback callback = this.mCallback;
        if (!(callback instanceof BatchedCallback)) {
            if (this.mBatchedCallback == null) {
                this.mBatchedCallback = new BatchedCallback(callback);
            }
            this.mCallback = this.mBatchedCallback;
        }
    }

    public void clear() {
        throwIfInMutationOperation();
        int i = this.mSize;
        if (i != 0) {
            Arrays.fill(this.mData, 0, i, null);
            this.mSize = 0;
            this.mCallback.onRemoved(0, i);
        }
    }

    public void endBatchedUpdates() {
        throwIfInMutationOperation();
        Callback callback = this.mCallback;
        if (callback instanceof BatchedCallback) {
            ((BatchedCallback) callback).dispatchLastEvent();
        }
        Callback callback2 = this.mCallback;
        BatchedCallback batchedCallback = this.mBatchedCallback;
        if (callback2 == batchedCallback) {
            this.mCallback = batchedCallback.mWrappedCallback;
        }
    }

    public Object get(int i) {
        if (i >= this.mSize || i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Asked to get item at ");
            sb.append(i);
            sb.append(" but size is ");
            sb.append(this.mSize);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        Object[] objArr = this.mOldData;
        if (objArr != null) {
            int i2 = this.mNewDataStart;
            if (i >= i2) {
                return objArr[(i - i2) + this.mOldDataStart];
            }
        }
        return this.mData[i];
    }

    public int indexOf(Object obj) {
        if (this.mOldData != null) {
            int findIndexOf = findIndexOf(obj, this.mData, 0, this.mNewDataStart, 4);
            if (findIndexOf != -1) {
                return findIndexOf;
            }
            int findIndexOf2 = findIndexOf(obj, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
            if (findIndexOf2 != -1) {
                return (findIndexOf2 - this.mOldDataStart) + this.mNewDataStart;
            }
            return -1;
        }
        return findIndexOf(obj, this.mData, 0, this.mSize, 4);
    }

    public void recalculatePositionOfItemAt(int i) {
        throwIfInMutationOperation();
        Object obj = get(i);
        removeItemAtIndex(i, false);
        int add = add(obj, false);
        if (i != add) {
            this.mCallback.onMoved(i, add);
        }
    }

    public boolean remove(Object obj) {
        throwIfInMutationOperation();
        return remove(obj, true);
    }

    public Object removeItemAt(int i) {
        throwIfInMutationOperation();
        Object obj = get(i);
        removeItemAtIndex(i, true);
        return obj;
    }

    public void replaceAll(@NonNull Collection collection) {
        replaceAll(collection.toArray((Object[]) Array.newInstance(this.mTClass, collection.size())), true);
    }

    public void replaceAll(@NonNull Object... objArr) {
        replaceAll(objArr, false);
    }

    public void replaceAll(@NonNull Object[] objArr, boolean z) {
        throwIfInMutationOperation();
        if (!z) {
            objArr = copyArray(objArr);
        }
        replaceAllInternal(objArr);
    }

    public int size() {
        return this.mSize;
    }

    public void updateItemAt(int i, Object obj) {
        throwIfInMutationOperation();
        Object obj2 = get(i);
        boolean z = obj2 == obj || !this.mCallback.areContentsTheSame(obj2, obj);
        if (obj2 == obj || this.mCallback.compare(obj2, obj) != 0) {
            if (z) {
                Callback callback = this.mCallback;
                callback.onChanged(i, 1, callback.getChangePayload(obj2, obj));
            }
            removeItemAtIndex(i, false);
            int add = add(obj, false);
            if (i != add) {
                this.mCallback.onMoved(i, add);
            }
            return;
        }
        this.mData[i] = obj;
        if (z) {
            Callback callback2 = this.mCallback;
            callback2.onChanged(i, 1, callback2.getChangePayload(obj2, obj));
        }
    }
}
