package defpackage;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

/* renamed from: cx reason: default package */
final class cx extends bw implements RandomAccess, di, eo {
    private static final cx b = new cx(new float[0], 0);
    private float[] c;
    private int d;

    static {
        b.b();
    }

    public cx() {
        this(new float[10], 0);
    }

    private cx(float[] fArr, int i) {
        this.c = fArr;
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
            return new cx(Arrays.copyOf(this.c, i), this.d);
        }
        throw new IllegalArgumentException();
    }

    public final void a(float f) {
        c();
        int i = this.d;
        float[] fArr = this.c;
        if (i == fArr.length) {
            float[] fArr2 = new float[(((i * 3) / 2) + 1)];
            System.arraycopy(fArr, 0, fArr2, 0, i);
            this.c = fArr2;
        }
        float[] fArr3 = this.c;
        int i2 = this.d;
        this.d = i2 + 1;
        fArr3[i2] = f;
    }

    public final /* bridge */ /* synthetic */ void add(int i, Object obj) {
        float floatValue = ((Float) obj).floatValue();
        c();
        if (i >= 0) {
            int i2 = this.d;
            if (i <= i2) {
                float[] fArr = this.c;
                if (i2 < fArr.length) {
                    System.arraycopy(fArr, i, fArr, i + 1, i2 - i);
                } else {
                    float[] fArr2 = new float[(((i2 * 3) / 2) + 1)];
                    System.arraycopy(fArr, 0, fArr2, 0, i);
                    System.arraycopy(this.c, i, fArr2, i + 1, this.d - i);
                    this.c = fArr2;
                }
                this.c[i] = floatValue;
                this.d++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(c(i));
    }

    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        a(((Float) obj).floatValue());
        return true;
    }

    public final boolean addAll(Collection collection) {
        c();
        dj.O00000oO(collection);
        if (!(collection instanceof cx)) {
            return super.addAll(collection);
        }
        cx cxVar = (cx) collection;
        int i = cxVar.d;
        if (i == 0) {
            return false;
        }
        int i2 = this.d;
        if (Integer.MAX_VALUE - i2 >= i) {
            int i3 = i2 + i;
            float[] fArr = this.c;
            if (i3 > fArr.length) {
                this.c = Arrays.copyOf(fArr, i3);
            }
            System.arraycopy(cxVar.c, 0, this.c, this.d, cxVar.d);
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
        if (!(obj instanceof cx)) {
            return super.equals(obj);
        }
        cx cxVar = (cx) obj;
        if (this.d != cxVar.d) {
            return false;
        }
        float[] fArr = cxVar.c;
        for (int i = 0; i < this.d; i++) {
            if (Float.floatToIntBits(this.c[i]) != Float.floatToIntBits(fArr[i])) {
                return false;
            }
        }
        return true;
    }

    public final /* bridge */ /* synthetic */ Object get(int i) {
        b(i);
        return Float.valueOf(this.c[i]);
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.d; i2++) {
            i = (i * 31) + Float.floatToIntBits(this.c[i2]);
        }
        return i;
    }

    public final int indexOf(Object obj) {
        if (obj instanceof Float) {
            float floatValue = ((Float) obj).floatValue();
            int i = this.d;
            for (int i2 = 0; i2 < i; i2++) {
                if (this.c[i2] == floatValue) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public final /* bridge */ /* synthetic */ Object remove(int i) {
        c();
        b(i);
        float[] fArr = this.c;
        float f = fArr[i];
        int i2 = this.d;
        if (i < i2 - 1) {
            System.arraycopy(fArr, i + 1, fArr, i, (i2 - i) - 1);
        }
        this.d--;
        this.modCount++;
        return Float.valueOf(f);
    }

    public final boolean remove(Object obj) {
        c();
        for (int i = 0; i < this.d; i++) {
            if (obj.equals(Float.valueOf(this.c[i]))) {
                float[] fArr = this.c;
                System.arraycopy(fArr, i + 1, fArr, i, (this.d - i) - 1);
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
            float[] fArr = this.c;
            System.arraycopy(fArr, i2, fArr, i, this.d - i2);
            this.d -= i2 - i;
            this.modCount++;
            return;
        }
        throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }

    public final /* bridge */ /* synthetic */ Object set(int i, Object obj) {
        float floatValue = ((Float) obj).floatValue();
        c();
        b(i);
        float[] fArr = this.c;
        float f = fArr[i];
        fArr[i] = floatValue;
        return Float.valueOf(f);
    }

    public final int size() {
        return this.d;
    }
}
