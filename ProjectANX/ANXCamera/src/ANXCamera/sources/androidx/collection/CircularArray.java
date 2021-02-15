package androidx.collection;

public final class CircularArray {
    private int mCapacityBitmask;
    private Object[] mElements;
    private int mHead;
    private int mTail;

    public CircularArray() {
        this(8);
    }

    public CircularArray(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        } else if (i <= 1073741824) {
            if (Integer.bitCount(i) != 1) {
                i = Integer.highestOneBit(i - 1) << 1;
            }
            this.mCapacityBitmask = i - 1;
            this.mElements = new Object[i];
        } else {
            throw new IllegalArgumentException("capacity must be <= 2^30");
        }
    }

    private void doubleCapacity() {
        Object[] objArr = this.mElements;
        int length = objArr.length;
        int i = this.mHead;
        int i2 = length - i;
        int i3 = length << 1;
        if (i3 >= 0) {
            Object[] objArr2 = new Object[i3];
            System.arraycopy(objArr, i, objArr2, 0, i2);
            System.arraycopy(this.mElements, 0, objArr2, i2, this.mHead);
            this.mElements = objArr2;
            this.mHead = 0;
            this.mTail = length;
            this.mCapacityBitmask = i3 - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }

    public void addFirst(Object obj) {
        this.mHead = (this.mHead - 1) & this.mCapacityBitmask;
        Object[] objArr = this.mElements;
        int i = this.mHead;
        objArr[i] = obj;
        if (i == this.mTail) {
            doubleCapacity();
        }
    }

    public void addLast(Object obj) {
        Object[] objArr = this.mElements;
        int i = this.mTail;
        objArr[i] = obj;
        this.mTail = this.mCapacityBitmask & (i + 1);
        if (this.mTail == this.mHead) {
            doubleCapacity();
        }
    }

    public void clear() {
        removeFromStart(size());
    }

    public Object get(int i) {
        if (i < 0 || i >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mElements[this.mCapacityBitmask & (this.mHead + i)];
    }

    public Object getFirst() {
        int i = this.mHead;
        if (i != this.mTail) {
            return this.mElements[i];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public Object getLast() {
        int i = this.mHead;
        int i2 = this.mTail;
        if (i != i2) {
            return this.mElements[this.mCapacityBitmask & (i2 - 1)];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public boolean isEmpty() {
        return this.mHead == this.mTail;
    }

    public Object popFirst() {
        int i = this.mHead;
        if (i != this.mTail) {
            Object[] objArr = this.mElements;
            Object obj = objArr[i];
            objArr[i] = null;
            this.mHead = (i + 1) & this.mCapacityBitmask;
            return obj;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public Object popLast() {
        int i = this.mHead;
        int i2 = this.mTail;
        if (i != i2) {
            int i3 = this.mCapacityBitmask & (i2 - 1);
            Object[] objArr = this.mElements;
            Object obj = objArr[i3];
            objArr[i3] = null;
            this.mTail = i3;
            return obj;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromEnd(int i) {
        int i2;
        if (i > 0) {
            if (i <= size()) {
                int i3 = 0;
                int i4 = this.mTail;
                if (i < i4) {
                    i3 = i4 - i;
                }
                int i5 = i3;
                while (true) {
                    i2 = this.mTail;
                    if (i5 >= i2) {
                        break;
                    }
                    this.mElements[i5] = null;
                    i5++;
                }
                int i6 = i2 - i3;
                int i7 = i - i6;
                this.mTail = i2 - i6;
                if (i7 > 0) {
                    this.mTail = this.mElements.length;
                    int i8 = this.mTail - i7;
                    for (int i9 = i8; i9 < this.mTail; i9++) {
                        this.mElements[i9] = null;
                    }
                    this.mTail = i8;
                }
                return;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void removeFromStart(int i) {
        if (i > 0) {
            if (i <= size()) {
                int length = this.mElements.length;
                int i2 = this.mHead;
                if (i < length - i2) {
                    length = i2 + i;
                }
                for (int i3 = this.mHead; i3 < length; i3++) {
                    this.mElements[i3] = null;
                }
                int i4 = this.mHead;
                int i5 = length - i4;
                int i6 = i - i5;
                this.mHead = this.mCapacityBitmask & (i4 + i5);
                if (i6 > 0) {
                    for (int i7 = 0; i7 < i6; i7++) {
                        this.mElements[i7] = null;
                    }
                    this.mHead = i6;
                }
                return;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public int size() {
        return this.mCapacityBitmask & (this.mTail - this.mHead);
    }
}
