package defpackage;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

/* renamed from: cp reason: default package */
final class cp extends bw implements RandomAccess, di, eo {
    private static final cp b = new cp(new double[0], 0);
    private double[] c;
    private int d;

    static {
        b.b();
    }

    public cp() {
        this(new double[10], 0);
    }

    private cp(double[] dArr, int i) {
        this.c = dArr;
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

    public final void O000000o(double d2) {
        c();
        int i = this.d;
        double[] dArr = this.c;
        if (i == dArr.length) {
            double[] dArr2 = new double[(((i * 3) / 2) + 1)];
            System.arraycopy(dArr, 0, dArr2, 0, i);
            this.c = dArr2;
        }
        double[] dArr3 = this.c;
        int i2 = this.d;
        this.d = i2 + 1;
        dArr3[i2] = d2;
    }

    public final /* bridge */ /* synthetic */ di a(int i) {
        if (i >= this.d) {
            return new cp(Arrays.copyOf(this.c, i), this.d);
        }
        throw new IllegalArgumentException();
    }

    public final /* bridge */ /* synthetic */ void add(int i, Object obj) {
        double doubleValue = ((Double) obj).doubleValue();
        c();
        if (i >= 0) {
            int i2 = this.d;
            if (i <= i2) {
                double[] dArr = this.c;
                if (i2 < dArr.length) {
                    System.arraycopy(dArr, i, dArr, i + 1, i2 - i);
                } else {
                    double[] dArr2 = new double[(((i2 * 3) / 2) + 1)];
                    System.arraycopy(dArr, 0, dArr2, 0, i);
                    System.arraycopy(this.c, i, dArr2, i + 1, this.d - i);
                    this.c = dArr2;
                }
                this.c[i] = doubleValue;
                this.d++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(c(i));
    }

    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        O000000o(((Double) obj).doubleValue());
        return true;
    }

    public final boolean addAll(Collection collection) {
        c();
        dj.O00000oO(collection);
        if (!(collection instanceof cp)) {
            return super.addAll(collection);
        }
        cp cpVar = (cp) collection;
        int i = cpVar.d;
        if (i == 0) {
            return false;
        }
        int i2 = this.d;
        if (Integer.MAX_VALUE - i2 >= i) {
            int i3 = i2 + i;
            double[] dArr = this.c;
            if (i3 > dArr.length) {
                this.c = Arrays.copyOf(dArr, i3);
            }
            System.arraycopy(cpVar.c, 0, this.c, this.d, cpVar.d);
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
        if (!(obj instanceof cp)) {
            return super.equals(obj);
        }
        cp cpVar = (cp) obj;
        if (this.d != cpVar.d) {
            return false;
        }
        double[] dArr = cpVar.c;
        for (int i = 0; i < this.d; i++) {
            if (Double.doubleToLongBits(this.c[i]) != Double.doubleToLongBits(dArr[i])) {
                return false;
            }
        }
        return true;
    }

    public final /* bridge */ /* synthetic */ Object get(int i) {
        b(i);
        return Double.valueOf(this.c[i]);
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.d; i2++) {
            i = (i * 31) + dj.a(Double.doubleToLongBits(this.c[i2]));
        }
        return i;
    }

    public final int indexOf(Object obj) {
        if (obj instanceof Double) {
            double doubleValue = ((Double) obj).doubleValue();
            int i = this.d;
            for (int i2 = 0; i2 < i; i2++) {
                if (this.c[i2] == doubleValue) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public final /* bridge */ /* synthetic */ Object remove(int i) {
        c();
        b(i);
        double[] dArr = this.c;
        double d2 = dArr[i];
        int i2 = this.d;
        if (i < i2 - 1) {
            System.arraycopy(dArr, i + 1, dArr, i, (i2 - i) - 1);
        }
        this.d--;
        this.modCount++;
        return Double.valueOf(d2);
    }

    public final boolean remove(Object obj) {
        c();
        for (int i = 0; i < this.d; i++) {
            if (obj.equals(Double.valueOf(this.c[i]))) {
                double[] dArr = this.c;
                System.arraycopy(dArr, i + 1, dArr, i, (this.d - i) - 1);
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
            double[] dArr = this.c;
            System.arraycopy(dArr, i2, dArr, i, this.d - i2);
            this.d -= i2 - i;
            this.modCount++;
            return;
        }
        throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }

    public final /* bridge */ /* synthetic */ Object set(int i, Object obj) {
        double doubleValue = ((Double) obj).doubleValue();
        c();
        b(i);
        double[] dArr = this.c;
        double d2 = dArr[i];
        dArr[i] = doubleValue;
        return Double.valueOf(d2);
    }

    public final int size() {
        return this.d;
    }
}
