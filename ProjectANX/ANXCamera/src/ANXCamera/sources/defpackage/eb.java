package defpackage;

import android.os.Looper;
import android.util.Log;

/* renamed from: eb reason: default package */
public class eb {
    static int O000000o(int i, byte[] bArr, int i2, int i3, by byVar) {
        if (ga.b(i) != 0) {
            int a = ga.a(i);
            if (a == 0) {
                return O00000Oo(bArr, i2, byVar);
            }
            if (a == 1) {
                return i2 + 8;
            }
            if (a == 2) {
                return O000000o(bArr, i2, byVar) + byVar.a;
            }
            if (a == 3) {
                int i4 = (i & -8) | 4;
                int i5 = 0;
                while (i2 < i3) {
                    i2 = O000000o(bArr, i2, byVar);
                    i5 = byVar.a;
                    if (i5 == i4) {
                        break;
                    }
                    i2 = O000000o(i5, bArr, i2, i3, byVar);
                }
                if (i2 <= i3 && i5 == i4) {
                    return i2;
                }
                throw dl.e();
            } else if (a == 5) {
                return i2 + 4;
            } else {
                throw dl.c();
            }
        } else {
            throw dl.c();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x006d, code lost:
        r2 = r14.c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x009c, code lost:
        r2 = java.lang.Integer.valueOf(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00a7, code lost:
        r2 = java.lang.Long.valueOf(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00b4, code lost:
        r10 = r10 + 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00bf, code lost:
        r10 = r10 + 8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00c1, code lost:
        r8 = r13.a().ordinal();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00cb, code lost:
        if (r8 == 9) goto L_0x00d2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00cf, code lost:
        if (r8 == 10) goto L_0x00d2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00d2, code lost:
        r8 = r12.O000000o(r13.d);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00d8, code lost:
        if (r8 == null) goto L_0x00de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00da, code lost:
        r2 = defpackage.dj.O000000o(r8, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00de, code lost:
        r12.O000000o(r13.d, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e3, code lost:
        return r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static int O000000o(int i, byte[] bArr, int i2, int i3, dc dcVar, cq cqVar, by byVar) {
        Object valueOf;
        long j;
        int i4;
        cu cuVar = dcVar.d;
        int i5 = i >>> 3;
        Object obj = null;
        if (cqVar.a() != fy.ENUM) {
            switch (cqVar.a().ordinal()) {
                case 0:
                    valueOf = Double.valueOf(O00000o0(bArr, i2));
                    break;
                case 1:
                    obj = Float.valueOf(O00000o(bArr, i2));
                    break;
                case 2:
                case 3:
                    r10 = O00000Oo(bArr, i2, byVar);
                    j = byVar.b;
                    break;
                case 4:
                case 12:
                    r10 = O000000o(bArr, i2, byVar);
                    i4 = byVar.a;
                    break;
                case 5:
                case 15:
                    valueOf = Long.valueOf(O00000Oo(bArr, i2));
                    break;
                case 6:
                case 14:
                    obj = Integer.valueOf(O000000o(bArr, i2));
                    break;
                case 7:
                    i2 = O00000Oo(bArr, i2, byVar);
                    obj = Boolean.valueOf(byVar.b != 0);
                    break;
                case 8:
                    i2 = O00000o0(bArr, i2, byVar);
                    break;
                case 9:
                    i2 = O000000o(ep.a.O00000Oo(cqVar.c.getClass()), bArr, i2, i3, (i5 << 3) | 4, byVar);
                    break;
                case 10:
                    i2 = O000000o(ep.a.O00000Oo(cqVar.c.getClass()), bArr, i2, i3, byVar);
                    break;
                case 11:
                    i2 = O00000oO(bArr, i2, byVar);
                    break;
                case 13:
                    throw new IllegalStateException("Shouldn't reach here.");
                case 16:
                    r10 = O000000o(bArr, i2, byVar);
                    i4 = cl.a(byVar.a);
                    break;
                case 17:
                    r10 = O00000Oo(bArr, i2, byVar);
                    j = cl.a(byVar.b);
                    break;
            }
        } else {
            O000000o(bArr, i2, byVar);
            throw null;
        }
    }

    static int O000000o(int i, byte[] bArr, int i2, int i3, di diVar, by byVar) {
        df dfVar = (df) diVar;
        int O000000o2 = O000000o(bArr, i2, byVar);
        while (true) {
            dfVar.c(byVar.a);
            if (O000000o2 >= i3) {
                break;
            }
            int O000000o3 = O000000o(bArr, O000000o2, byVar);
            if (i != byVar.a) {
                break;
            }
            O000000o2 = O000000o(bArr, O000000o3, byVar);
        }
        return O000000o2;
    }

    static int O000000o(int i, byte[] bArr, int i2, int i3, fi fiVar, by byVar) {
        if (ga.b(i) != 0) {
            int a = ga.a(i);
            if (a == 0) {
                int O00000Oo2 = O00000Oo(bArr, i2, byVar);
                fiVar.O000000o(i, (Object) Long.valueOf(byVar.b));
                return O00000Oo2;
            } else if (a == 1) {
                fiVar.O000000o(i, (Object) Long.valueOf(O00000Oo(bArr, i2)));
                return i2 + 8;
            } else if (a == 2) {
                int O000000o2 = O000000o(bArr, i2, byVar);
                int i4 = byVar.a;
                if (i4 < 0) {
                    throw dl.b();
                } else if (i4 <= bArr.length - O000000o2) {
                    fiVar.O000000o(i, (Object) i4 != 0 ? ck.O000000o(bArr, O000000o2, i4) : ck.b);
                    return O000000o2 + i4;
                } else {
                    throw dl.a();
                }
            } else if (a == 3) {
                int i5 = (i & -8) | 4;
                fi a2 = fi.a();
                int i6 = 0;
                while (true) {
                    if (i2 >= i3) {
                        break;
                    }
                    int O000000o3 = O000000o(bArr, i2, byVar);
                    int i7 = byVar.a;
                    i6 = i7;
                    if (i7 == i5) {
                        i2 = O000000o3;
                        break;
                    }
                    int O000000o4 = O000000o(i6, bArr, O000000o3, i3, a2, byVar);
                    i6 = i7;
                    i2 = O000000o4;
                }
                if (i2 > i3 || i6 != i5) {
                    throw dl.e();
                }
                fiVar.O000000o(i, (Object) a2);
                return i2;
            } else if (a == 5) {
                fiVar.O000000o(i, (Object) Integer.valueOf(O000000o(bArr, i2)));
                return i2 + 4;
            } else {
                throw dl.c();
            }
        } else {
            throw dl.c();
        }
    }

    static int O000000o(int i, byte[] bArr, int i2, by byVar) {
        int i3;
        int i4;
        int i5 = i & 127;
        int i6 = i2 + 1;
        byte b = bArr[i2];
        if (b < 0) {
            int i7 = i5 | ((b & Byte.MAX_VALUE) << 7);
            int i8 = i6 + 1;
            byte b2 = bArr[i6];
            if (b2 < 0) {
                i5 = i7 | ((b2 & Byte.MAX_VALUE) << 14);
                i6 = i8 + 1;
                byte b3 = bArr[i8];
                if (b3 < 0) {
                    i7 = i5 | ((b3 & Byte.MAX_VALUE) << 21);
                    i8 = i6 + 1;
                    byte b4 = bArr[i6];
                    if (b4 < 0) {
                        int i9 = i7 | ((b4 & Byte.MAX_VALUE) << 28);
                        while (true) {
                            int i10 = i8 + 1;
                            if (bArr[i8] < 0) {
                                i8 = i10;
                            } else {
                                byVar.a = i9;
                                return i10;
                            }
                        }
                    } else {
                        i4 = b4 << 28;
                    }
                } else {
                    i3 = b3 << 21;
                }
            } else {
                i4 = b2 << 14;
            }
            byVar.a = i7 | i4;
            return i8;
        }
        i3 = b << 7;
        byVar.a = i5 | i3;
        return i6;
    }

    static int O000000o(ea eaVar, Object obj, Object obj2) {
        fy fyVar = eaVar.a;
        int O000000o2 = cu.O000000o((fy) null, 1, obj);
        fy fyVar2 = eaVar.c;
        return O000000o2 + cu.O000000o((fy) null, 2, obj2);
    }

    static int O000000o(es esVar, int i, byte[] bArr, int i2, int i3, di diVar, by byVar) {
        int O000000o2 = O000000o(esVar, bArr, i2, i3, byVar);
        while (true) {
            diVar.add(byVar.c);
            if (O000000o2 >= i3) {
                break;
            }
            int O000000o3 = O000000o(bArr, O000000o2, byVar);
            if (i != byVar.a) {
                break;
            }
            O000000o2 = O000000o(esVar, bArr, O000000o3, i3, byVar);
        }
        return O000000o2;
    }

    static int O000000o(es esVar, byte[] bArr, int i, int i2, int i3, by byVar) {
        ek ekVar = (ek) esVar;
        Object a = ekVar.a();
        int O000000o2 = ekVar.O000000o(a, bArr, i, i2, i3, byVar);
        ekVar.O00000o(a);
        byVar.c = a;
        return O000000o2;
    }

    /* JADX WARNING: type inference failed for: r8v2, types: [int] */
    /* JADX WARNING: type inference failed for: r8v5 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static int O000000o(es esVar, byte[] bArr, int i, int i2, by byVar) {
        int i3 = i + 1;
        byte b = bArr[i];
        if (b < 0) {
            i3 = O000000o((int) b, bArr, i3, byVar);
            b = byVar.a;
        }
        int i4 = i3;
        if (b < 0 || b > i2 - i4) {
            throw dl.a();
        }
        Object a = esVar.a();
        int i5 = b + i4;
        esVar.O000000o(a, bArr, i4, i5, byVar);
        esVar.O00000o(a);
        byVar.c = a;
        return i5;
    }

    static int O000000o(byte[] bArr, int i) {
        return ((bArr[i + 3] & -1) << 24) | (bArr[i] & -1) | ((bArr[i + 1] & -1) << 8) | ((bArr[i + 2] & -1) << 16);
    }

    static int O000000o(byte[] bArr, int i, by byVar) {
        int i2 = i + 1;
        byte b = bArr[i];
        if (b < 0) {
            return O000000o((int) b, bArr, i2, byVar);
        }
        byVar.a = b;
        return i2;
    }

    static int O000000o(byte[] bArr, int i, di diVar, by byVar) {
        df dfVar = (df) diVar;
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a + O000000o2;
        while (O000000o2 < i2) {
            O000000o2 = O000000o(bArr, O000000o2, byVar);
            dfVar.c(byVar.a);
        }
        if (O000000o2 == i2) {
            return O000000o2;
        }
        throw dl.a();
    }

    static void O000000o(cn cnVar, ea eaVar, Object obj, Object obj2) {
        fy fyVar = eaVar.a;
        cu.O000000o(cnVar, 1, obj);
        fy fyVar2 = eaVar.c;
        cu.O000000o(cnVar, 2, obj2);
    }

    private static void O000000o(RuntimeException runtimeException) {
        Log.e("Preconditions", "Precondition broken. Build is not strict; continuing...", runtimeException);
    }

    static int O00000Oo(byte[] bArr, int i, by byVar) {
        int i2 = i + 1;
        long j = (long) bArr[i];
        if (j < 0) {
            int i3 = i2 + 1;
            byte b = bArr[i2];
            long j2 = (j & 127) | (((long) (b & Byte.MAX_VALUE)) << 7);
            int i4 = 7;
            while (b < 0) {
                int i5 = i3 + 1;
                byte b2 = bArr[i3];
                i4 += 7;
                j2 |= ((long) (b2 & Byte.MAX_VALUE)) << i4;
                int i6 = i5;
                b = b2;
                i3 = i6;
            }
            byVar.b = j2;
            return i3;
        }
        byVar.b = j;
        return i2;
    }

    static int O00000Oo(byte[] bArr, int i, di diVar, by byVar) {
        dw dwVar = (dw) diVar;
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a + O000000o2;
        while (O000000o2 < i2) {
            O000000o2 = O00000Oo(bArr, O000000o2, byVar);
            dwVar.a(byVar.b);
        }
        if (O000000o2 == i2) {
            return O000000o2;
        }
        throw dl.a();
    }

    static long O00000Oo(byte[] bArr, int i) {
        return ((((long) bArr[i + 7]) & 255) << 56) | (((long) bArr[i]) & 255) | ((((long) bArr[i + 1]) & 255) << 8) | ((((long) bArr[i + 2]) & 255) << 16) | ((((long) bArr[i + 3]) & 255) << 24) | ((((long) bArr[i + 4]) & 255) << 32) | ((((long) bArr[i + 5]) & 255) << 40) | ((((long) bArr[i + 6]) & 255) << 48);
    }

    static float O00000o(byte[] bArr, int i) {
        return Float.intBitsToFloat(O000000o(bArr, i));
    }

    static int O00000o(byte[] bArr, int i, by byVar) {
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a;
        if (i2 < 0) {
            throw dl.b();
        } else if (i2 != 0) {
            byVar.c = fx.a.O00000o(bArr, O000000o2, i2);
            return O000000o2 + i2;
        } else {
            byVar.c = "";
            return O000000o2;
        }
    }

    static int O00000o(byte[] bArr, int i, di diVar, by byVar) {
        dw dwVar = (dw) diVar;
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a + O000000o2;
        while (O000000o2 < i2) {
            dwVar.a(O00000Oo(bArr, O000000o2));
            O000000o2 += 8;
        }
        if (O000000o2 == i2) {
            return O000000o2;
        }
        throw dl.a();
    }

    static double O00000o0(byte[] bArr, int i) {
        return Double.longBitsToDouble(O00000Oo(bArr, i));
    }

    static int O00000o0(byte[] bArr, int i, by byVar) {
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a;
        if (i2 < 0) {
            throw dl.b();
        } else if (i2 != 0) {
            byVar.c = new String(bArr, O000000o2, i2, dj.a);
            return O000000o2 + i2;
        } else {
            byVar.c = "";
            return O000000o2;
        }
    }

    static int O00000o0(byte[] bArr, int i, di diVar, by byVar) {
        df dfVar = (df) diVar;
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a + O000000o2;
        while (O000000o2 < i2) {
            dfVar.c(O000000o(bArr, O000000o2));
            O000000o2 += 4;
        }
        if (O000000o2 == i2) {
            return O000000o2;
        }
        throw dl.a();
    }

    static int O00000oO(byte[] bArr, int i, by byVar) {
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a;
        if (i2 < 0) {
            throw dl.b();
        } else if (i2 > bArr.length - O000000o2) {
            throw dl.a();
        } else if (i2 != 0) {
            byVar.c = ck.O000000o(bArr, O000000o2, i2);
            return O000000o2 + i2;
        } else {
            byVar.c = ck.b;
            return O000000o2;
        }
    }

    static int O00000oO(byte[] bArr, int i, di diVar, by byVar) {
        cx cxVar = (cx) diVar;
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a + O000000o2;
        while (O000000o2 < i2) {
            cxVar.a(O00000o(bArr, O000000o2));
            O000000o2 += 4;
        }
        if (O000000o2 == i2) {
            return O000000o2;
        }
        throw dl.a();
    }

    public static void O00000oO(Object obj) {
        if (obj == null) {
            O000000o(new NullPointerException());
        }
    }

    static int O00000oo(byte[] bArr, int i, di diVar, by byVar) {
        cp cpVar = (cp) diVar;
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a + O000000o2;
        while (O000000o2 < i2) {
            cpVar.O000000o(O00000o0(bArr, O000000o2));
            O000000o2 += 8;
        }
        if (O000000o2 == i2) {
            return O000000o2;
        }
        throw dl.a();
    }

    static int O0000O0o(byte[] bArr, int i, di diVar, by byVar) {
        ca caVar = (ca) diVar;
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a + O000000o2;
        while (O000000o2 < i2) {
            O000000o2 = O00000Oo(bArr, O000000o2, byVar);
            caVar.a(byVar.b != 0);
        }
        if (O000000o2 == i2) {
            return O000000o2;
        }
        throw dl.a();
    }

    static int O0000OOo(byte[] bArr, int i, di diVar, by byVar) {
        df dfVar = (df) diVar;
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a + O000000o2;
        while (O000000o2 < i2) {
            O000000o2 = O000000o(bArr, O000000o2, byVar);
            dfVar.c(cl.a(byVar.a));
        }
        if (O000000o2 == i2) {
            return O000000o2;
        }
        throw dl.a();
    }

    static int O0000Oo0(byte[] bArr, int i, di diVar, by byVar) {
        dw dwVar = (dw) diVar;
        int O000000o2 = O000000o(bArr, i, byVar);
        int i2 = byVar.a + O000000o2;
        while (O000000o2 < i2) {
            O000000o2 = O00000Oo(bArr, O000000o2, byVar);
            dwVar.a(cl.a(byVar.b));
        }
        if (O000000o2 == i2) {
            return O000000o2;
        }
        throw dl.a();
    }

    public static void a(boolean z, String str) {
        if (!z) {
            O000000o(new IllegalStateException(str));
        }
    }

    public static void c() {
        a(Looper.myLooper() == Looper.getMainLooper(), "This should be running on the main thread.");
    }
}
