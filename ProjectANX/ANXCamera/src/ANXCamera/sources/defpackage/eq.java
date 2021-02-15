package defpackage;

import java.util.Arrays;
import java.util.RandomAccess;

/* renamed from: eq reason: default package */
public final class eq extends bw implements RandomAccess {
    public static final eq b = new eq(new Object[0], 0);
    private Object[] c;
    private int d;

    static {
        b.b();
    }

    public eq() {
        this(new Object[10], 0);
    }

    private eq(Object[] objArr, int i) {
        this.c = objArr;
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
            return new eq(Arrays.copyOf(this.c, i), this.d);
        }
        throw new IllegalArgumentException();
    }

    public final void add(int i, Object obj) {
        c();
        if (i >= 0) {
            int i2 = this.d;
            if (i <= i2) {
                Object[] objArr = this.c;
                if (i2 < objArr.length) {
                    System.arraycopy(objArr, i, objArr, i + 1, i2 - i);
                } else {
                    Object[] objArr2 = new Object[(((i2 * 3) / 2) + 1)];
                    System.arraycopy(objArr, 0, objArr2, 0, i);
                    System.arraycopy(this.c, i, objArr2, i + 1, this.d - i);
                    this.c = objArr2;
                }
                this.c[i] = obj;
                this.d++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(c(i));
    }

    public final boolean add(Object obj) {
        c();
        int i = this.d;
        Object[] objArr = this.c;
        if (i == objArr.length) {
            this.c = Arrays.copyOf(objArr, ((i * 3) / 2) + 1);
        }
        Object[] objArr2 = this.c;
        int i2 = this.d;
        this.d = i2 + 1;
        objArr2[i2] = obj;
        this.modCount++;
        return true;
    }

    public final Object get(int i) {
        b(i);
        return this.c[i];
    }

    public final Object remove(int i) {
        c();
        b(i);
        Object[] objArr = this.c;
        Object obj = objArr[i];
        int i2 = this.d;
        if (i < i2 - 1) {
            System.arraycopy(objArr, i + 1, objArr, i, (i2 - i) - 1);
        }
        this.d--;
        this.modCount++;
        return obj;
    }

    public final Object set(int i, Object obj) {
        c();
        b(i);
        Object[] objArr = this.c;
        Object obj2 = objArr[i];
        objArr[i] = obj;
        this.modCount++;
        return obj2;
    }

    public final int size() {
        return this.d;
    }
}
