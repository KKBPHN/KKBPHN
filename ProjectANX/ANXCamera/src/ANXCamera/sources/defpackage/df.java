package defpackage;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

/* renamed from: df reason: default package */
final class df extends bw implements RandomAccess, di, eo {
    private static final df b = new df(new int[0], 0);
    private int[] c;
    private int d;

    static {
        b.b();
    }

    public df() {
        this(new int[10], 0);
    }

    private df(int[] iArr, int i) {
        this.c = iArr;
        this.d = i;
    }

    private final void d(int i) {
        if (i < 0 || i >= this.d) {
            throw new IndexOutOfBoundsException(e(i));
        }
    }

    private final String e(int i) {
        int i2 = this.d;
        StringBuilder sb = new StringBuilder(35);
        sb.append("Index:");
        sb.append(i);
        sb.append(", Size:");
        sb.append(i2);
        return sb.toString();
    }

    public final /* bridge */ /* synthetic */ di a(int i) {
        if (i >= this.d) {
            return new df(Arrays.copyOf(this.c, i), this.d);
        }
        throw new IllegalArgumentException();
    }

    public final /* bridge */ /* synthetic */ void add(int i, Object obj) {
        int intValue = ((Integer) obj).intValue();
        c();
        if (i >= 0) {
            int i2 = this.d;
            if (i <= i2) {
                int[] iArr = this.c;
                if (i2 < iArr.length) {
                    System.arraycopy(iArr, i, iArr, i + 1, i2 - i);
                } else {
                    int[] iArr2 = new int[(((i2 * 3) / 2) + 1)];
                    System.arraycopy(iArr, 0, iArr2, 0, i);
                    System.arraycopy(this.c, i, iArr2, i + 1, this.d - i);
                    this.c = iArr2;
                }
                this.c[i] = intValue;
                this.d++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(e(i));
    }

    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        c(((Integer) obj).intValue());
        return true;
    }

    public final boolean addAll(Collection collection) {
        c();
        dj.O00000oO(collection);
        if (!(collection instanceof df)) {
            return super.addAll(collection);
        }
        df dfVar = (df) collection;
        int i = dfVar.d;
        if (i == 0) {
            return false;
        }
        int i2 = this.d;
        if (Integer.MAX_VALUE - i2 >= i) {
            int i3 = i2 + i;
            int[] iArr = this.c;
            if (i3 > iArr.length) {
                this.c = Arrays.copyOf(iArr, i3);
            }
            System.arraycopy(dfVar.c, 0, this.c, this.d, dfVar.d);
            this.d = i3;
            this.modCount++;
            return true;
        }
        throw new OutOfMemoryError();
    }

    public final int b(int i) {
        d(i);
        return this.c[i];
    }

    public final void c(int i) {
        c();
        int i2 = this.d;
        int[] iArr = this.c;
        if (i2 == iArr.length) {
            int[] iArr2 = new int[(((i2 * 3) / 2) + 1)];
            System.arraycopy(iArr, 0, iArr2, 0, i2);
            this.c = iArr2;
        }
        int[] iArr3 = this.c;
        int i3 = this.d;
        this.d = i3 + 1;
        iArr3[i3] = i;
    }

    public final boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof df)) {
            return super.equals(obj);
        }
        df dfVar = (df) obj;
        if (this.d != dfVar.d) {
            return false;
        }
        int[] iArr = dfVar.c;
        for (int i = 0; i < this.d; i++) {
            if (this.c[i] != iArr[i]) {
                return false;
            }
        }
        return true;
    }

    public final /* bridge */ /* synthetic */ Object get(int i) {
        return Integer.valueOf(b(i));
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.d; i2++) {
            i = (i * 31) + this.c[i2];
        }
        return i;
    }

    public final int indexOf(Object obj) {
        if (obj instanceof Integer) {
            int intValue = ((Integer) obj).intValue();
            int i = this.d;
            for (int i2 = 0; i2 < i; i2++) {
                if (this.c[i2] == intValue) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public final /* bridge */ /* synthetic */ Object remove(int i) {
        c();
        d(i);
        int[] iArr = this.c;
        int i2 = iArr[i];
        int i3 = this.d;
        if (i < i3 - 1) {
            System.arraycopy(iArr, i + 1, iArr, i, (i3 - i) - 1);
        }
        this.d--;
        this.modCount++;
        return Integer.valueOf(i2);
    }

    public final boolean remove(Object obj) {
        c();
        for (int i = 0; i < this.d; i++) {
            if (obj.equals(Integer.valueOf(this.c[i]))) {
                int[] iArr = this.c;
                System.arraycopy(iArr, i + 1, iArr, i, (this.d - i) - 1);
                this.d--;
                this.modCount++;
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public final void removeRange(int i, int i2) {
        c();
        if (i2 >= i) {
            int[] iArr = this.c;
            System.arraycopy(iArr, i2, iArr, i, this.d - i2);
            this.d -= i2 - i;
            this.modCount++;
            return;
        }
        throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }

    public final /* bridge */ /* synthetic */ Object set(int i, Object obj) {
        int intValue = ((Integer) obj).intValue();
        c();
        d(i);
        int[] iArr = this.c;
        int i2 = iArr[i];
        iArr[i] = intValue;
        return Integer.valueOf(i2);
    }

    public final int size() {
        return this.d;
    }
}
