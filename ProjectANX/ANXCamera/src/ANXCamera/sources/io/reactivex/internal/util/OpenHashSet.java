package io.reactivex.internal.util;

public final class OpenHashSet {
    private static final int INT_PHI = -1640531527;
    Object[] keys;
    final float loadFactor;
    int mask;
    int maxSize;
    int size;

    public OpenHashSet() {
        this(16, 0.75f);
    }

    public OpenHashSet(int i) {
        this(i, 0.75f);
    }

    public OpenHashSet(int i, float f) {
        this.loadFactor = f;
        int roundToPowerOfTwo = Pow2.roundToPowerOfTwo(i);
        this.mask = roundToPowerOfTwo - 1;
        this.maxSize = (int) (f * ((float) roundToPowerOfTwo));
        this.keys = new Object[roundToPowerOfTwo];
    }

    static int mix(int i) {
        int i2 = i * INT_PHI;
        return i2 ^ (i2 >>> 16);
    }

    public boolean add(Object obj) {
        int i;
        Object obj2;
        Object[] objArr = this.keys;
        int i2 = this.mask;
        int mix = mix(obj.hashCode()) & i2;
        Object obj3 = objArr[mix];
        if (obj3 != null) {
            if (obj3.equals(obj)) {
                return false;
            }
            do {
                mix = (mix + 1) & i2;
                obj2 = objArr[mix];
                if (obj2 == null) {
                }
            } while (!obj2.equals(obj));
            return false;
        }
        objArr[i] = obj;
        int i3 = this.size + 1;
        this.size = i3;
        if (i3 >= this.maxSize) {
            rehash();
        }
        return true;
    }

    public Object[] keys() {
        return this.keys;
    }

    /* access modifiers changed from: 0000 */
    public void rehash() {
        int i;
        Object[] objArr = this.keys;
        int length = objArr.length;
        int i2 = length << 1;
        int i3 = i2 - 1;
        Object[] objArr2 = new Object[i2];
        int i4 = this.size;
        while (true) {
            int i5 = i4 - 1;
            if (i4 != 0) {
                while (true) {
                    length--;
                    if (objArr[length] != null) {
                        break;
                    }
                }
                int mix = mix(objArr[length].hashCode()) & i3;
                if (objArr2[mix] != null) {
                    do {
                        mix = (i + 1) & i3;
                    } while (objArr2[mix] != null);
                }
                objArr2[mix] = objArr[length];
                i4 = i5;
            } else {
                this.mask = i3;
                this.maxSize = (int) (((float) i2) * this.loadFactor);
                this.keys = objArr2;
                return;
            }
        }
    }

    public boolean remove(Object obj) {
        Object obj2;
        Object[] objArr = this.keys;
        int i = this.mask;
        int mix = mix(obj.hashCode()) & i;
        Object obj3 = objArr[mix];
        if (obj3 == null) {
            return false;
        }
        if (obj3.equals(obj)) {
            return removeEntry(mix, objArr, i);
        }
        do {
            mix = (mix + 1) & i;
            obj2 = objArr[mix];
            if (obj2 == null) {
                return false;
            }
        } while (!obj2.equals(obj));
        return removeEntry(mix, objArr, i);
    }

    /* access modifiers changed from: 0000 */
    public boolean removeEntry(int i, Object[] objArr, int i2) {
        int i3;
        Object obj;
        this.size--;
        while (true) {
            int i4 = i + 1;
            while (true) {
                i3 = i4 & i2;
                obj = objArr[i3];
                if (obj == null) {
                    objArr[i] = null;
                    return true;
                }
                int mix = mix(obj.hashCode()) & i2;
                if (i <= i3) {
                    if (i >= mix || mix > i3) {
                        break;
                    }
                    i4 = i3 + 1;
                } else {
                    if (i >= mix && mix > i3) {
                        break;
                    }
                    i4 = i3 + 1;
                }
            }
            objArr[i] = obj;
            i = i3;
        }
    }

    public int size() {
        return this.size;
    }
}
