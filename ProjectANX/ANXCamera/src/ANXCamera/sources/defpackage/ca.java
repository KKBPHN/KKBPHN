package defpackage;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

/* renamed from: ca reason: default package */
final class ca extends bw implements RandomAccess, di, eo {
    private static final ca b = new ca(new boolean[0], 0);
    private boolean[] c;
    private int d;

    static {
        b.b();
    }

    public ca() {
        this(new boolean[10], 0);
    }

    private ca(boolean[] zArr, int i) {
        this.c = zArr;
        this.d = i;
    }

    private final void b(int i) {
        if (i < 0 || i >= this.d) {
            throw new IndexOutOfBoundsException(c(i));
        }
    }

    private final String c(int i) {
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
            return new ca(Arrays.copyOf(this.c, i), this.d);
        }
        throw new IllegalArgumentException();
    }

    public final void a(boolean z) {
        c();
        int i = this.d;
        boolean[] zArr = this.c;
        if (i == zArr.length) {
            boolean[] zArr2 = new boolean[(((i * 3) / 2) + 1)];
            System.arraycopy(zArr, 0, zArr2, 0, i);
            this.c = zArr2;
        }
        boolean[] zArr3 = this.c;
        int i2 = this.d;
        this.d = i2 + 1;
        zArr3[i2] = z;
    }

    public final /* bridge */ /* synthetic */ void add(int i, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        c();
        if (i >= 0) {
            int i2 = this.d;
            if (i <= i2) {
                boolean[] zArr = this.c;
                if (i2 < zArr.length) {
                    System.arraycopy(zArr, i, zArr, i + 1, i2 - i);
                } else {
                    boolean[] zArr2 = new boolean[(((i2 * 3) / 2) + 1)];
                    System.arraycopy(zArr, 0, zArr2, 0, i);
                    System.arraycopy(this.c, i, zArr2, i + 1, this.d - i);
                    this.c = zArr2;
                }
                this.c[i] = booleanValue;
                this.d++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(c(i));
    }

    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        a(((Boolean) obj).booleanValue());
        return true;
    }

    public final boolean addAll(Collection collection) {
        c();
        dj.O00000oO(collection);
        if (!(collection instanceof ca)) {
            return super.addAll(collection);
        }
        ca caVar = (ca) collection;
        int i = caVar.d;
        if (i == 0) {
            return false;
        }
        int i2 = this.d;
        if (Integer.MAX_VALUE - i2 >= i) {
            int i3 = i2 + i;
            boolean[] zArr = this.c;
            if (i3 > zArr.length) {
                this.c = Arrays.copyOf(zArr, i3);
            }
            System.arraycopy(caVar.c, 0, this.c, this.d, caVar.d);
            this.d = i3;
            this.modCount++;
            return true;
        }
        throw new OutOfMemoryError();
    }

    public final boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ca)) {
            return super.equals(obj);
        }
        ca caVar = (ca) obj;
        if (this.d != caVar.d) {
            return false;
        }
        boolean[] zArr = caVar.c;
        for (int i = 0; i < this.d; i++) {
            if (this.c[i] != zArr[i]) {
                return false;
            }
        }
        return true;
    }

    public final /* bridge */ /* synthetic */ Object get(int i) {
        b(i);
        return Boolean.valueOf(this.c[i]);
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.d; i2++) {
            i = (i * 31) + dj.a(this.c[i2]);
        }
        return i;
    }

    public final int indexOf(Object obj) {
        if (obj instanceof Boolean) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            int i = this.d;
            for (int i2 = 0; i2 < i; i2++) {
                if (this.c[i2] == booleanValue) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public final /* bridge */ /* synthetic */ Object remove(int i) {
        c();
        b(i);
        boolean[] zArr = this.c;
        boolean z = zArr[i];
        int i2 = this.d;
        if (i < i2 - 1) {
            System.arraycopy(zArr, i + 1, zArr, i, (i2 - i) - 1);
        }
        this.d--;
        this.modCount++;
        return Boolean.valueOf(z);
    }

    public final boolean remove(Object obj) {
        c();
        for (int i = 0; i < this.d; i++) {
            if (obj.equals(Boolean.valueOf(this.c[i]))) {
                boolean[] zArr = this.c;
                System.arraycopy(zArr, i + 1, zArr, i, (this.d - i) - 1);
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
            boolean[] zArr = this.c;
            System.arraycopy(zArr, i2, zArr, i, this.d - i2);
            this.d -= i2 - i;
            this.modCount++;
            return;
        }
        throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }

    public final /* bridge */ /* synthetic */ Object set(int i, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        c();
        b(i);
        boolean[] zArr = this.c;
        boolean z = zArr[i];
        zArr[i] = booleanValue;
        return Boolean.valueOf(z);
    }

    public final int size() {
        return this.d;
    }
}
