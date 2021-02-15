package defpackage;

import java.util.Arrays;

/* renamed from: fi reason: default package */
public final class fi {
    public static final fi a = new fi(0, new int[0], new Object[0], false);
    public int b;
    public int[] c;
    public Object[] d;
    public boolean e;
    private int f;

    private fi() {
        this(0, new int[8], new Object[8], true);
    }

    private fi(int i, int[] iArr, Object[] objArr, boolean z) {
        this.f = -1;
        this.b = i;
        this.c = iArr;
        this.d = objArr;
        this.e = z;
    }

    static fi O000000o(fi fiVar, fi fiVar2) {
        int i = fiVar.b + fiVar2.b;
        int[] copyOf = Arrays.copyOf(fiVar.c, i);
        System.arraycopy(fiVar2.c, 0, copyOf, fiVar.b, fiVar2.b);
        Object[] copyOf2 = Arrays.copyOf(fiVar.d, i);
        System.arraycopy(fiVar2.d, 0, copyOf2, fiVar.b, fiVar2.b);
        return new fi(i, copyOf, copyOf2, true);
    }

    private static void O000000o(int i, Object obj, gb gbVar) {
        int b2 = ga.b(i);
        int a2 = ga.a(i);
        if (a2 == 0) {
            gbVar.a(b2, ((Long) obj).longValue());
        } else if (a2 == 1) {
            gbVar.O00000Oo(b2, ((Long) obj).longValue());
        } else if (a2 == 2) {
            gbVar.O000000o(b2, (ck) obj);
        } else if (a2 == 3) {
            gbVar.a(b2);
            ((fi) obj).O00000Oo(gbVar);
            gbVar.b(b2);
        } else if (a2 == 5) {
            gbVar.O00000Oo(b2, ((Integer) obj).intValue());
        } else {
            throw new RuntimeException(dl.d());
        }
    }

    /* access modifiers changed from: 0000 */
    public static fi a() {
        return new fi(0, new int[8], new Object[8], true);
    }

    /* access modifiers changed from: 0000 */
    public final void O000000o(int i, Object obj) {
        if (this.e) {
            int i2 = this.b;
            int[] iArr = this.c;
            if (i2 == iArr.length) {
                int i3 = i2 + (i2 >= 4 ? i2 >> 1 : 8);
                this.c = Arrays.copyOf(iArr, i3);
                this.d = Arrays.copyOf(this.d, i3);
            }
            int[] iArr2 = this.c;
            int i4 = this.b;
            iArr2[i4] = i;
            this.d[i4] = obj;
            this.b = i4 + 1;
            return;
        }
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: 0000 */
    public final void O000000o(gb gbVar) {
        for (int i = 0; i < this.b; i++) {
            gbVar.O000000o(ga.b(this.c[i]), this.d[i]);
        }
    }

    public final void O00000Oo(gb gbVar) {
        if (this.b != 0) {
            for (int i = 0; i < this.b; i++) {
                O000000o(this.c[i], this.d[i], gbVar);
            }
        }
    }

    public final int b() {
        int i = this.f;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.b; i3++) {
            int i4 = this.c[i3];
            ck ckVar = (ck) this.d[i3];
            int a2 = cn.a(1);
            i2 += a2 + a2 + cn.O000000o(2, ga.b(i4)) + cn.O000000o(3, ckVar);
        }
        this.f = i2;
        return i2;
    }

    public final int c() {
        int i;
        int i2 = this.f;
        if (i2 != -1) {
            return i2;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.b; i4++) {
            int i5 = this.c[i4];
            int b2 = ga.b(i5);
            int a2 = ga.a(i5);
            if (a2 == 0) {
                i = cn.O000000o(b2, ((Long) this.d[i4]).longValue());
            } else if (a2 == 1) {
                ((Long) this.d[i4]).longValue();
                i = cn.O000OO00(b2);
            } else if (a2 == 2) {
                i = cn.O000000o(b2, (ck) this.d[i4]);
            } else if (a2 == 3) {
                int a3 = cn.a(b2);
                i = a3 + a3 + ((fi) this.d[i4]).c();
            } else if (a2 == 5) {
                ((Integer) this.d[i4]).intValue();
                i = cn.O000O0oo(b2);
            } else {
                throw new IllegalStateException(dl.d());
            }
            i3 += i;
        }
        this.f = i3;
        return i3;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof fi)) {
            fi fiVar = (fi) obj;
            int i = this.b;
            if (i == fiVar.b) {
                int[] iArr = this.c;
                int[] iArr2 = fiVar.c;
                int i2 = 0;
                while (true) {
                    if (i2 < i) {
                        if (iArr[i2] != iArr2[i2]) {
                            break;
                        }
                        i2++;
                    } else {
                        Object[] objArr = this.d;
                        Object[] objArr2 = fiVar.d;
                        int i3 = this.b;
                        int i4 = 0;
                        while (i4 < i3) {
                            if (objArr[i4].equals(objArr2[i4])) {
                                i4++;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public final int hashCode() {
        int i = this.b;
        int i2 = (i + 527) * 31;
        int[] iArr = this.c;
        int i3 = 17;
        int i4 = 17;
        for (int i5 = 0; i5 < i; i5++) {
            i4 = (i4 * 31) + iArr[i5];
        }
        int i6 = (i2 + i4) * 31;
        Object[] objArr = this.d;
        for (int i7 = 0; i7 < this.b; i7++) {
            i3 = (i3 * 31) + objArr[i7].hashCode();
        }
        return i6 + i3;
    }
}
