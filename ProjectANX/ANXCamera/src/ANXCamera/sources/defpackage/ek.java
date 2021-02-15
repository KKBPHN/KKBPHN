package defpackage;

import com.adobe.xmp.options.PropertyOptions;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import sun.misc.Unsafe;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* renamed from: ek reason: default package */
final class ek implements es {
    private static final int[] a = new int[0];
    private static final Unsafe b = fr.a();
    private final int[] c;
    private final Object[] d;
    private final int e;
    private final int f;
    private final eh g;
    private final boolean h;
    private final boolean i;
    private final boolean j;
    private final int[] k;
    private final int l;
    private final int m;
    private final dv n;
    private final fh o;
    private final ej p;
    private final fs q;
    private final ff r;

    private ek(int[] iArr, Object[] objArr, int i2, int i3, eh ehVar, boolean z, boolean z2, int[] iArr2, int i4, int i5, fs fsVar, dv dvVar, fh fhVar, ej ejVar, ff ffVar, byte[] bArr) {
        ej ejVar2 = ejVar;
        this.c = iArr;
        this.d = objArr;
        this.e = i2;
        this.f = i3;
        this.i = z;
        boolean z3 = ejVar2 != null && ej.O000000o(ehVar);
        this.h = z3;
        this.j = false;
        this.k = iArr2;
        this.l = i4;
        this.m = i5;
        this.q = fsVar;
        this.n = dvVar;
        this.o = fhVar;
        this.p = ejVar2;
        this.g = ehVar;
        this.r = ffVar;
    }

    private static final int O000000o(fh fhVar, Object obj) {
        return ((fi) fhVar.O00000Oo(obj)).c();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00f5, code lost:
        r12.putObject(r1, r9, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0113, code lost:
        r12.putInt(r1, r13, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x01b5, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int O000000o(Object obj, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7, int i8, long j2, int i9, by byVar) {
        int i10;
        Object obj2;
        Object obj3 = obj;
        byte[] bArr2 = bArr;
        int i11 = i2;
        int i12 = i4;
        int i13 = i5;
        int i14 = i6;
        long j3 = j2;
        int i15 = i9;
        by byVar2 = byVar;
        Unsafe unsafe = b;
        long j4 = (long) (this.c[i15 + 2] & 1048575);
        switch (i8) {
            case 51:
                if (i14 == 1) {
                    unsafe.putObject(obj3, j3, Double.valueOf(eb.O00000o0(bArr, i2)));
                    unsafe.putInt(obj3, j4, i13);
                    return i11 + 8;
                }
            case 52:
                if (i14 == 5) {
                    unsafe.putObject(obj3, j3, Float.valueOf(eb.O00000o(bArr, i2)));
                    unsafe.putInt(obj3, j4, i13);
                    return i11 + 4;
                }
            case 53:
            case 54:
                if (i14 == 0) {
                    int O00000Oo2 = eb.O00000Oo(bArr2, i11, byVar2);
                    unsafe.putObject(obj3, j3, Long.valueOf(byVar2.b));
                    unsafe.putInt(obj3, j4, i13);
                    return O00000Oo2;
                }
            case 55:
            case 62:
                if (i14 == 0) {
                    int O000000o2 = eb.O000000o(bArr2, i11, byVar2);
                    unsafe.putObject(obj3, j3, Integer.valueOf(byVar2.a));
                    unsafe.putInt(obj3, j4, i13);
                    return O000000o2;
                }
            case 56:
            case 65:
                if (i14 == 1) {
                    unsafe.putObject(obj3, j3, Long.valueOf(eb.O00000Oo(bArr, i2)));
                    unsafe.putInt(obj3, j4, i13);
                    return i11 + 8;
                }
            case 57:
            case 64:
                if (i14 == 5) {
                    unsafe.putObject(obj3, j3, Integer.valueOf(eb.O000000o(bArr, i2)));
                    unsafe.putInt(obj3, j4, i13);
                    return i11 + 4;
                }
            case 58:
                if (i14 == 0) {
                    int O00000Oo3 = eb.O00000Oo(bArr2, i11, byVar2);
                    unsafe.putObject(obj3, j3, Boolean.valueOf(byVar2.b != 0));
                    unsafe.putInt(obj3, j4, i13);
                    return O00000Oo3;
                }
            case 59:
                if (i14 == 2) {
                    i10 = eb.O000000o(bArr2, i11, byVar2);
                    int i16 = byVar2.a;
                    if (i16 != 0) {
                        if ((i7 & PropertyOptions.DELETE_EXISTING) == 0 || fx.O000000o(bArr2, i10, i10 + i16)) {
                            unsafe.putObject(obj3, j3, new String(bArr2, i10, i16, dj.a));
                            i10 += i16;
                            break;
                        } else {
                            throw dl.f();
                        }
                    } else {
                        obj2 = "";
                        break;
                    }
                }
                break;
            case 60:
                if (i14 == 2) {
                    i10 = eb.O000000o(a(i15), bArr2, i11, i3, byVar2);
                    Object object = unsafe.getInt(obj3, j4) == i13 ? unsafe.getObject(obj3, j3) : null;
                    obj2 = byVar2.c;
                    if (object != null) {
                        obj2 = dj.O000000o(object, obj2);
                        break;
                    }
                }
                break;
            case 61:
                if (i14 == 2) {
                    int O00000oO = eb.O00000oO(bArr2, i11, byVar2);
                    unsafe.putObject(obj3, j3, byVar2.c);
                    unsafe.putInt(obj3, j4, i13);
                    return O00000oO;
                }
            case 63:
                if (i14 == 0) {
                    int O000000o3 = eb.O000000o(bArr2, i11, byVar2);
                    int i17 = byVar2.a;
                    dh c2 = c(i15);
                    if (c2 == null || c2.a(i17)) {
                        unsafe.putObject(obj3, j3, Integer.valueOf(i17));
                        unsafe.putInt(obj3, j4, i13);
                    } else {
                        O0000O0o(obj).O000000o(i12, (Object) Long.valueOf((long) i17));
                    }
                    i10 = O000000o3;
                    break;
                }
                break;
            case 66:
                if (i14 == 0) {
                    int O000000o4 = eb.O000000o(bArr2, i11, byVar2);
                    unsafe.putObject(obj3, j3, Integer.valueOf(cl.a(byVar2.a)));
                    unsafe.putInt(obj3, j4, i13);
                    return O000000o4;
                }
                i10 = i11;
                break;
            case 67:
                if (i14 == 0) {
                    int O00000Oo4 = eb.O00000Oo(bArr2, i11, byVar2);
                    unsafe.putObject(obj3, j3, Long.valueOf(cl.a(byVar2.b)));
                    unsafe.putInt(obj3, j4, i13);
                    return O00000Oo4;
                }
                i10 = i11;
                break;
            case 68:
                if (i14 == 3) {
                    i10 = eb.O000000o(a(i15), bArr, i2, i3, (i12 & -8) | 4, byVar);
                    Object object2 = unsafe.getInt(obj3, j4) == i13 ? unsafe.getObject(obj3, j3) : null;
                    obj2 = byVar2.c;
                    if (object2 != null) {
                        obj2 = dj.O000000o(object2, obj2);
                        break;
                    }
                }
                i10 = i11;
                break;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:123:0x0214, code lost:
        if (r7.b != 0) goto L_0x0216;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x0216, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x0218, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x0219, code lost:
        r11.a(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x021c, code lost:
        if (r0 >= r5) goto L_0x0231;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x021e, code lost:
        r1 = defpackage.eb.O000000o(r3, r0, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x0224, code lost:
        if (r2 != r7.a) goto L_0x0231;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x0226, code lost:
        r0 = defpackage.eb.O00000Oo(r3, r1, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x022e, code lost:
        if (r7.b == 0) goto L_0x0218;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x0231, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00f6, code lost:
        if (r1 != 0) goto L_0x00f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00f8, code lost:
        r11.add(defpackage.ck.O000000o(r3, r0, r1));
        r0 = r0 + r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0101, code lost:
        r11.add(defpackage.ck.b);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0106, code lost:
        if (r0 >= r5) goto L_0x0129;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0108, code lost:
        r1 = defpackage.eb.O000000o(r3, r0, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x010e, code lost:
        if (r2 != r7.a) goto L_0x0129;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0110, code lost:
        r0 = defpackage.eb.O000000o(r3, r1, r7);
        r1 = r7.a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0116, code lost:
        if (r1 < 0) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x011a, code lost:
        if (r1 > (r3.length - r0)) goto L_0x011f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x011c, code lost:
        if (r1 == 0) goto L_0x0101;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0123, code lost:
        throw defpackage.dl.a();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0128, code lost:
        throw defpackage.dl.b();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0129, code lost:
        return r0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x01db  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int O000000o(Object obj, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7, long j2, int i8, long j3, by byVar) {
        int i9;
        int i10;
        int i11;
        int O000000o2;
        Object obj2 = obj;
        byte[] bArr2 = bArr;
        int i12 = i2;
        int i13 = i3;
        int i14 = i4;
        int i15 = i6;
        int i16 = i7;
        long j4 = j3;
        by byVar2 = byVar;
        di diVar = (di) b.getObject(obj2, j4);
        if (!diVar.a()) {
            int size = diVar.size();
            diVar = diVar.a(size != 0 ? size + size : 10);
            b.putObject(obj2, j4, diVar);
        }
        switch (i8) {
            case 18:
            case 35:
                if (i15 == 2) {
                    i9 = eb.O00000oo(bArr2, i12, diVar, byVar2);
                    break;
                } else if (i15 == 1) {
                    cp cpVar = (cp) diVar;
                    cpVar.O000000o(eb.O00000o0(bArr, i2));
                    int i17 = i12 + 8;
                    while (i17 < i13) {
                        int O000000o3 = eb.O000000o(bArr2, i17, byVar2);
                        if (i14 != byVar2.a) {
                            return i17;
                        }
                        cpVar.O000000o(eb.O00000o0(bArr2, O000000o3));
                        i17 = O000000o3 + 8;
                    }
                    return i17;
                }
            case 19:
            case 36:
                if (i15 == 2) {
                    i9 = eb.O00000oO(bArr2, i12, diVar, byVar2);
                    break;
                } else if (i15 == 5) {
                    cx cxVar = (cx) diVar;
                    cxVar.a(eb.O00000o(bArr, i2));
                    int i18 = i12 + 4;
                    while (i18 < i13) {
                        int O000000o4 = eb.O000000o(bArr2, i18, byVar2);
                        if (i14 != byVar2.a) {
                            return i18;
                        }
                        cxVar.a(eb.O00000o(bArr2, O000000o4));
                        i18 = O000000o4 + 4;
                    }
                    return i18;
                }
            case 20:
            case 21:
            case 37:
            case 38:
                if (i15 == 2) {
                    i9 = eb.O00000Oo(bArr2, i12, diVar, byVar2);
                    break;
                } else if (i15 == 0) {
                    dw dwVar = (dw) diVar;
                    int O00000Oo2 = eb.O00000Oo(bArr2, i12, byVar2);
                    while (true) {
                        dwVar.a(byVar2.b);
                        if (O00000Oo2 < i13) {
                            int O000000o5 = eb.O000000o(bArr2, O00000Oo2, byVar2);
                            if (i14 == byVar2.a) {
                                O00000Oo2 = eb.O00000Oo(bArr2, O000000o5, byVar2);
                            }
                        }
                    }
                    return O00000Oo2;
                }
            case 22:
            case 29:
            case 39:
            case 43:
                if (i15 == 2) {
                    i9 = eb.O000000o(bArr2, i12, diVar, byVar2);
                    break;
                } else if (i15 == 0) {
                    return eb.O000000o(i4, bArr, i2, i3, diVar, byVar);
                }
            case 23:
            case 32:
            case 40:
            case 46:
                if (i15 == 2) {
                    i9 = eb.O00000o(bArr2, i12, diVar, byVar2);
                    break;
                } else if (i15 == 1) {
                    dw dwVar2 = (dw) diVar;
                    dwVar2.a(eb.O00000Oo(bArr, i2));
                    int i19 = i12 + 8;
                    while (i19 < i13) {
                        int O000000o6 = eb.O000000o(bArr2, i19, byVar2);
                        if (i14 != byVar2.a) {
                            return i19;
                        }
                        dwVar2.a(eb.O00000Oo(bArr2, O000000o6));
                        i19 = O000000o6 + 8;
                    }
                    return i19;
                }
            case 24:
            case 31:
            case 41:
            case 45:
                if (i15 == 2) {
                    i9 = eb.O00000o0(bArr2, i12, diVar, byVar2);
                    break;
                } else if (i15 == 5) {
                    df dfVar = (df) diVar;
                    dfVar.c(eb.O000000o(bArr, i2));
                    int i20 = i12 + 4;
                    while (i20 < i13) {
                        int O000000o7 = eb.O000000o(bArr2, i20, byVar2);
                        if (i14 != byVar2.a) {
                            return i20;
                        }
                        dfVar.c(eb.O000000o(bArr2, O000000o7));
                        i20 = O000000o7 + 4;
                    }
                    return i20;
                }
            case 25:
            case 42:
                if (i15 != 2) {
                    if (i15 == 0) {
                        ca caVar = (ca) diVar;
                        int O00000Oo3 = eb.O00000Oo(bArr2, i12, byVar2);
                        break;
                    }
                } else {
                    i9 = eb.O0000O0o(bArr2, i12, diVar, byVar2);
                    break;
                }
            case 26:
                if (i15 == 2) {
                    String str = "";
                    if ((j2 & IjkMediaMeta.AV_CH_STEREO_LEFT) != 0) {
                        int O000000o8 = eb.O000000o(bArr2, i12, byVar2);
                        int i21 = byVar2.a;
                        if (i21 >= 0) {
                            if (i21 == 0) {
                                diVar.add(str);
                                i10 = O000000o8;
                            } else {
                                i10 = O000000o8 + i21;
                                if (fx.O000000o(bArr2, O000000o8, i10)) {
                                    diVar.add(new String(bArr2, O000000o8, i21, dj.a));
                                } else {
                                    throw dl.f();
                                }
                            }
                            while (i10 < i13) {
                                int O000000o9 = eb.O000000o(bArr2, i10, byVar2);
                                if (i14 != byVar2.a) {
                                    i9 = i10;
                                    break;
                                } else {
                                    int O000000o10 = eb.O000000o(bArr2, O000000o9, byVar2);
                                    int i22 = byVar2.a;
                                    if (i22 < 0) {
                                        throw dl.b();
                                    } else if (i22 == 0) {
                                        diVar.add(str);
                                    } else {
                                        int i23 = O000000o10 + i22;
                                        if (fx.O000000o(bArr2, O000000o10, i23)) {
                                            diVar.add(new String(bArr2, O000000o10, i22, dj.a));
                                            O000000o10 = i23;
                                        } else {
                                            throw dl.f();
                                        }
                                    }
                                }
                            }
                            i9 = i10;
                        } else {
                            throw dl.b();
                        }
                    } else {
                        int O000000o11 = eb.O000000o(bArr2, i12, byVar2);
                        int i24 = byVar2.a;
                        if (i24 >= 0) {
                            if (i24 != 0) {
                                String str2 = new String(bArr2, O000000o11, i24, dj.a);
                                diVar.add(str2);
                                i9 = O000000o11 + i24;
                                if (i9 < i13) {
                                    int O000000o12 = eb.O000000o(bArr2, i9, byVar2);
                                    if (i14 == byVar2.a) {
                                        O000000o11 = eb.O000000o(bArr2, O000000o12, byVar2);
                                        i24 = byVar2.a;
                                        if (i24 >= 0) {
                                            if (i24 != 0) {
                                                str2 = new String(bArr2, O000000o11, i24, dj.a);
                                                diVar.add(str2);
                                                i9 = O000000o11 + i24;
                                                if (i9 < i13) {
                                                }
                                            }
                                        }
                                        throw dl.b();
                                    }
                                }
                            }
                            diVar.add(str);
                            if (i9 < i13) {
                            }
                        } else {
                            throw dl.b();
                        }
                    }
                }
                break;
            case 27:
                if (i15 == 2) {
                    return eb.O000000o(a(i16), i4, bArr, i2, i3, diVar, byVar);
                }
                i9 = i12;
                break;
            case 28:
                if (i15 == 2) {
                    int O000000o13 = eb.O000000o(bArr2, i12, byVar2);
                    int i25 = byVar2.a;
                    if (i25 < 0) {
                        throw dl.b();
                    } else if (i25 > bArr2.length - O000000o13) {
                        throw dl.a();
                    }
                }
                i9 = i12;
                break;
            case 30:
            case 44:
                if (i15 == 2) {
                    i11 = eb.O000000o(bArr2, i12, diVar, byVar2);
                } else if (i15 == 0) {
                    i11 = eb.O000000o(i4, bArr, i2, i3, diVar, byVar);
                }
                de deVar = (de) obj2;
                fi fiVar = deVar.h;
                if (fiVar == fi.a) {
                    fiVar = null;
                }
                fi fiVar2 = (fi) eu.O000000o(i5, diVar, c(i16), fiVar, this.o);
                if (fiVar2 == null) {
                    i9 = i11;
                    break;
                } else {
                    deVar.h = fiVar2;
                    return i11;
                }
            case 33:
            case 47:
                if (i15 == 2) {
                    i9 = eb.O0000OOo(bArr2, i12, diVar, byVar2);
                    break;
                } else if (i15 == 0) {
                    df dfVar2 = (df) diVar;
                    int O000000o14 = eb.O000000o(bArr2, i12, byVar2);
                    while (true) {
                        dfVar2.c(cl.a(byVar2.a));
                        if (O000000o14 < i13) {
                            int O000000o15 = eb.O000000o(bArr2, O000000o14, byVar2);
                            if (i14 == byVar2.a) {
                                O000000o14 = eb.O000000o(bArr2, O000000o15, byVar2);
                            }
                        }
                    }
                    return O000000o14;
                }
            case 34:
            case 48:
                if (i15 == 2) {
                    i9 = eb.O0000Oo0(bArr2, i12, diVar, byVar2);
                    break;
                } else if (i15 == 0) {
                    dw dwVar3 = (dw) diVar;
                    int O00000Oo4 = eb.O00000Oo(bArr2, i12, byVar2);
                    while (true) {
                        dwVar3.a(cl.a(byVar2.b));
                        if (O00000Oo4 < i13) {
                            int O000000o16 = eb.O000000o(bArr2, O00000Oo4, byVar2);
                            if (i14 == byVar2.a) {
                                O00000Oo4 = eb.O00000Oo(bArr2, O000000o16, byVar2);
                            }
                        }
                    }
                    return O00000Oo4;
                }
            default:
                if (i15 == 3) {
                    es a2 = a(i16);
                    int i26 = (i14 & -8) | 4;
                    es esVar = a2;
                    byte[] bArr3 = bArr;
                    int i27 = i2;
                    while (true) {
                        O000000o2 = eb.O000000o(esVar, bArr3, i27, i3, i26, byVar);
                        diVar.add(byVar2.c);
                        if (O000000o2 < i13) {
                            int O000000o17 = eb.O000000o(bArr2, O000000o2, byVar2);
                            if (i14 == byVar2.a) {
                                esVar = a2;
                                bArr3 = bArr;
                                i27 = O000000o17;
                            }
                        }
                    }
                    return O000000o2;
                }
        }
        i9 = i12;
        return i9;
    }

    private final int O000000o(Object obj, byte[] bArr, int i2, int i3, int i4, long j2, by byVar) {
        Unsafe unsafe = b;
        Object b2 = b(i4);
        Object object = unsafe.getObject(obj, j2);
        if (ff.O00000o(object)) {
            Object a2 = ff.a();
            ff.O00000Oo(a2, object);
            unsafe.putObject(obj, j2, a2);
        }
        ff.O00000Oo(b2);
        throw null;
    }

    static ek O000000o(ee eeVar, fs fsVar, dv dvVar, fh fhVar, ej ejVar, ff ffVar) {
        if (eeVar instanceof er) {
            return O000000o((er) eeVar, fsVar, dvVar, fhVar, ejVar, ffVar);
        }
        fd fdVar = (fd) eeVar;
        throw null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:141:0x02ba  */
    /* JADX WARNING: Removed duplicated region for block: B:148:0x02e8  */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x02eb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static ek O000000o(er erVar, fs fsVar, dv dvVar, fh fhVar, ej ejVar, ff ffVar) {
        int i2;
        int i3;
        int i4;
        int i5;
        int[] iArr;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        Object[] objArr;
        int i19;
        int i20;
        Field field;
        Field field2;
        char charAt;
        int i21;
        char charAt2;
        Object obj;
        Field field3;
        int i22;
        char charAt3;
        int i23;
        char charAt4;
        int i24;
        char charAt5;
        int i25;
        char c2;
        int i26;
        int i27;
        int i28;
        int i29;
        int i30;
        char charAt6;
        int i31;
        char charAt7;
        int i32;
        char charAt8;
        char charAt9;
        char charAt10;
        char charAt11;
        char charAt12;
        char charAt13;
        char charAt14;
        er erVar2 = erVar;
        boolean z = erVar.c() == 2;
        String str = erVar2.b;
        int length = str.length();
        char c3 = 55296;
        if (str.charAt(0) < 55296) {
            i2 = 1;
        } else {
            int i33 = 1;
            while (true) {
                i2 = i33 + 1;
                if (str.charAt(i33) < 55296) {
                    break;
                }
                i33 = i2;
            }
        }
        int i34 = i2 + 1;
        char charAt15 = str.charAt(i2);
        if (charAt15 >= 55296) {
            char c4 = charAt15 & 8191;
            int i35 = 13;
            while (true) {
                i3 = i34 + 1;
                charAt14 = str.charAt(i34);
                if (charAt14 < 55296) {
                    break;
                }
                c4 |= (charAt14 & 8191) << i35;
                i35 += 13;
                i34 = i3;
            }
            charAt15 = c4 | (charAt14 << i35);
        } else {
            i3 = i34;
        }
        if (charAt15 == 0) {
            i9 = 0;
            i8 = 0;
            i7 = 0;
            i6 = 0;
            i5 = 0;
            i4 = 0;
            iArr = a;
            i10 = 0;
        } else {
            int i36 = i3 + 1;
            char charAt16 = str.charAt(i3);
            if (charAt16 >= 55296) {
                char c5 = charAt16 & 8191;
                int i37 = 13;
                while (true) {
                    i25 = i36 + 1;
                    charAt13 = str.charAt(i36);
                    if (charAt13 < 55296) {
                        break;
                    }
                    c5 |= (charAt13 & 8191) << i37;
                    i37 += 13;
                    i36 = i25;
                }
                c2 = (charAt13 << i37) | c5;
            } else {
                i25 = i36;
                c2 = charAt16;
            }
            int i38 = i25 + 1;
            char charAt17 = str.charAt(i25);
            if (charAt17 >= 55296) {
                char c6 = charAt17 & 8191;
                int i39 = 13;
                while (true) {
                    i26 = i38 + 1;
                    charAt12 = str.charAt(i38);
                    if (charAt12 < 55296) {
                        break;
                    }
                    c6 |= (charAt12 & 8191) << i39;
                    i39 += 13;
                    i38 = i26;
                }
                charAt17 = c6 | (charAt12 << i39);
            } else {
                i26 = i38;
            }
            int i40 = i26 + 1;
            int charAt18 = str.charAt(i26);
            if (charAt18 >= 55296) {
                int i41 = charAt18 & 8191;
                int i42 = 13;
                while (true) {
                    i27 = i40 + 1;
                    charAt11 = str.charAt(i40);
                    if (charAt11 < 55296) {
                        break;
                    }
                    i41 |= (charAt11 & 8191) << i42;
                    i42 += 13;
                    i40 = i27;
                }
                charAt18 = (charAt11 << i42) | i41;
            } else {
                i27 = i40;
            }
            int i43 = i27 + 1;
            int charAt19 = str.charAt(i27);
            if (charAt19 >= 55296) {
                int i44 = charAt19 & 8191;
                int i45 = 13;
                while (true) {
                    i28 = i43 + 1;
                    charAt10 = str.charAt(i43);
                    if (charAt10 < 55296) {
                        break;
                    }
                    i44 |= (charAt10 & 8191) << i45;
                    i45 += 13;
                    i43 = i28;
                }
                charAt19 = (charAt10 << i45) | i44;
            } else {
                i28 = i43;
            }
            int i46 = i28 + 1;
            i7 = str.charAt(i28);
            if (i7 >= 55296) {
                int i47 = i7 & 8191;
                int i48 = 13;
                while (true) {
                    i29 = i46 + 1;
                    charAt9 = str.charAt(i46);
                    if (charAt9 < 55296) {
                        break;
                    }
                    i47 |= (charAt9 & 8191) << i48;
                    i48 += 13;
                    i46 = i29;
                }
                i7 = (charAt9 << i48) | i47;
            } else {
                i29 = i46;
            }
            int i49 = i29 + 1;
            char charAt20 = str.charAt(i29);
            if (charAt20 >= 55296) {
                char c7 = charAt20 & 8191;
                int i50 = 13;
                while (true) {
                    i32 = i49 + 1;
                    charAt8 = str.charAt(i49);
                    if (charAt8 < 55296) {
                        break;
                    }
                    c7 |= (charAt8 & 8191) << i50;
                    i50 += 13;
                    i49 = i32;
                }
                charAt20 = (charAt8 << i50) | c7;
                i49 = i32;
            }
            int i51 = i49 + 1;
            char charAt21 = str.charAt(i49);
            if (charAt21 >= 55296) {
                char c8 = charAt21 & 8191;
                int i52 = 13;
                while (true) {
                    i31 = i51 + 1;
                    charAt7 = str.charAt(i51);
                    if (charAt7 < 55296) {
                        break;
                    }
                    c8 |= (charAt7 & 8191) << i52;
                    i52 += 13;
                    i51 = i31;
                }
                charAt21 = c8 | (charAt7 << i52);
                i51 = i31;
            }
            int i53 = i51 + 1;
            char charAt22 = str.charAt(i51);
            if (charAt22 >= 55296) {
                int i54 = 13;
                int i55 = i53;
                int i56 = charAt22 & 8191;
                int i57 = i55;
                while (true) {
                    i30 = i57 + 1;
                    charAt6 = str.charAt(i57);
                    if (charAt6 < 55296) {
                        break;
                    }
                    i56 |= (charAt6 & 8191) << i54;
                    i54 += 13;
                    i57 = i30;
                }
                charAt22 = i56 | (charAt6 << i54);
                i53 = i30;
            }
            i8 = c2 + c2 + charAt17;
            i4 = charAt19;
            int[] iArr2 = new int[(charAt22 + charAt20 + charAt21)];
            i9 = c2;
            i10 = charAt20;
            i6 = charAt22;
            iArr = iArr2;
            int i58 = i53;
            i5 = charAt18;
            i3 = i58;
        }
        Unsafe unsafe = b;
        Object[] objArr2 = erVar2.c;
        Class cls = erVar2.a.getClass();
        int[] iArr3 = new int[(i7 * 3)];
        Object[] objArr3 = new Object[(i7 + i7)];
        int i59 = i6 + i10;
        int i60 = i8;
        int i61 = i6;
        int i62 = i59;
        int i63 = 0;
        int i64 = 0;
        while (i3 < length) {
            int i65 = i3 + 1;
            char charAt23 = str.charAt(i3);
            if (charAt23 >= c3) {
                int i66 = 13;
                int i67 = i65;
                int i68 = charAt23 & 8191;
                int i69 = i67;
                while (true) {
                    i24 = i69 + 1;
                    charAt5 = str.charAt(i69);
                    if (charAt5 < c3) {
                        break;
                    }
                    i68 |= (charAt5 & 8191) << i66;
                    i66 += 13;
                    i69 = i24;
                }
                i11 = i68 | (charAt5 << i66);
                i12 = i24;
            } else {
                int i70 = i65;
                i11 = charAt23;
                i12 = i70;
            }
            int i71 = i12 + 1;
            char charAt24 = str.charAt(i12);
            if (charAt24 >= c3) {
                int i72 = 13;
                int i73 = i71;
                int i74 = charAt24 & 8191;
                int i75 = i73;
                while (true) {
                    i23 = i75 + 1;
                    charAt4 = str.charAt(i75);
                    if (charAt4 < c3) {
                        break;
                    }
                    i74 |= (charAt4 & 8191) << i72;
                    i72 += 13;
                    i75 = i23;
                }
                charAt24 = i74 | (charAt4 << i72);
                i13 = i23;
            } else {
                i13 = i71;
            }
            int i76 = length;
            char c9 = charAt24 & 255;
            int i77 = i6;
            if ((charAt24 & 1024) != 0) {
                int i78 = i63 + 1;
                iArr[i63] = i64;
                i63 = i78;
            }
            if (c9 < '3') {
                int i79 = i60 + 1;
                i16 = i63;
                Field a2 = a(cls, (String) objArr2[i60]);
                if (c9 == 9 || c9 == 17) {
                    int i80 = i64 / 3;
                    objArr3[i80 + i80 + 1] = a2.getType();
                } else {
                    if (c9 == 27 || c9 == '1') {
                        int i81 = i64 / 3;
                        i21 = i79 + 1;
                        objArr3[i81 + i81 + 1] = objArr2[i79];
                    } else if (c9 == 12 || c9 == 30 || c9 == ',') {
                        if (!z) {
                            int i82 = i64 / 3;
                            i21 = i79 + 1;
                            objArr3[i82 + i82 + 1] = objArr2[i79];
                        }
                    } else if (c9 == '2') {
                        int i83 = i61 + 1;
                        iArr[i61] = i64;
                        int i84 = i64 / 3;
                        i21 = i79 + 1;
                        int i85 = i84 + i84;
                        objArr3[i85] = objArr2[i79];
                        if ((charAt24 & 2048) != 0) {
                            i79 = i21 + 1;
                            objArr3[i85 + 1] = objArr2[i21];
                            i61 = i83;
                        } else {
                            i61 = i83;
                        }
                    }
                    objArr = objArr3;
                    i18 = (int) unsafe.objectFieldOffset(a2);
                    if ((charAt24 & 4096) == 4096 && c9 <= 17) {
                        int i86 = i13 + 1;
                        charAt2 = str.charAt(i13);
                        char c10 = 55296;
                        if (charAt2 >= 55296) {
                            char c11 = charAt2 & 8191;
                            int i87 = 13;
                            while (true) {
                                i22 = i86 + 1;
                                charAt3 = str.charAt(i86);
                                if (charAt3 < c10) {
                                    break;
                                }
                                c11 |= (charAt3 & 8191) << i87;
                                i87 += 13;
                                i86 = i22;
                                c10 = 55296;
                            }
                            charAt2 = c11 | (charAt3 << i87);
                            i86 = i22;
                        }
                        int i88 = i9 + i9 + (charAt2 / ' ');
                        i14 = i9;
                        obj = objArr2[i88];
                        int i89 = i86;
                        if (!(obj instanceof Field)) {
                            field3 = (Field) obj;
                        } else {
                            field3 = a(cls, (String) obj);
                            objArr2[i88] = field3;
                        }
                        i17 = charAt2 % ' ';
                        i13 = i89;
                        i15 = (int) unsafe.objectFieldOffset(field3);
                    } else {
                        i14 = i9;
                        i17 = 0;
                        i15 = 1048575;
                    }
                    if (c9 >= 18 && c9 <= '1') {
                        int i90 = i62 + 1;
                        iArr[i62] = i18;
                        i62 = i90;
                    }
                    i60 = i21;
                }
                objArr = objArr3;
                i21 = i79;
                i18 = (int) unsafe.objectFieldOffset(a2);
                if ((charAt24 & 4096) == 4096) {
                    int i862 = i13 + 1;
                    charAt2 = str.charAt(i13);
                    char c102 = 55296;
                    if (charAt2 >= 55296) {
                    }
                    int i882 = i9 + i9 + (charAt2 / ' ');
                    i14 = i9;
                    obj = objArr2[i882];
                    int i892 = i862;
                    if (!(obj instanceof Field)) {
                    }
                    i17 = charAt2 % ' ';
                    i13 = i892;
                    i15 = (int) unsafe.objectFieldOffset(field3);
                    int i902 = i62 + 1;
                    iArr[i62] = i18;
                    i62 = i902;
                    i60 = i21;
                }
                i14 = i9;
                i17 = 0;
                i15 = 1048575;
                int i9022 = i62 + 1;
                iArr[i62] = i18;
                i62 = i9022;
                i60 = i21;
            } else {
                i16 = i63;
                i14 = i9;
                objArr = objArr3;
                int i91 = i13 + 1;
                char charAt25 = str.charAt(i13);
                if (charAt25 >= 55296) {
                    char c12 = charAt25 & 8191;
                    int i92 = 13;
                    while (true) {
                        i19 = i91 + 1;
                        charAt = str.charAt(i91);
                        if (charAt < 55296) {
                            break;
                        }
                        c12 |= (charAt & 8191) << i92;
                        i92 += 13;
                        i91 = i19;
                    }
                    charAt25 = c12 | (charAt << i92);
                } else {
                    i19 = i91;
                }
                int i93 = c9 - '3';
                if (i93 == 9 || i93 == 17) {
                    int i94 = i64 / 3;
                    i20 = i60 + 1;
                    objArr[i94 + i94 + 1] = objArr2[i60];
                } else if (i93 == 12 && !z) {
                    int i95 = i64 / 3;
                    i20 = i60 + 1;
                    objArr[i95 + i95 + 1] = objArr2[i60];
                } else {
                    i20 = i60;
                }
                int i96 = charAt25 + charAt25;
                Object obj2 = objArr2[i96];
                if (obj2 instanceof Field) {
                    field = (Field) obj2;
                } else {
                    field = a(cls, (String) obj2);
                    objArr2[i96] = field;
                }
                int i97 = i19;
                i18 = (int) unsafe.objectFieldOffset(field);
                int i98 = i96 + 1;
                Object obj3 = objArr2[i98];
                if (obj3 instanceof Field) {
                    field2 = (Field) obj3;
                } else {
                    field2 = a(cls, (String) obj3);
                    objArr2[i98] = field2;
                }
                i15 = (int) unsafe.objectFieldOffset(field2);
                i13 = i97;
                i60 = i20;
                i17 = 0;
            }
            int i99 = i64 + 1;
            iArr3[i64] = i11;
            int i100 = i99 + 1;
            iArr3[i99] = (c9 << 20) | ((charAt24 & 256) != 0 ? 268435456 : 0) | ((charAt24 & 512) != 0 ? PropertyOptions.DELETE_EXISTING : 0) | i18;
            int i101 = i100 + 1;
            iArr3[i100] = (i17 << 20) | i15;
            objArr3 = objArr;
            i64 = i101;
            i3 = i13;
            length = i76;
            i6 = i77;
            i63 = i16;
            i9 = i14;
            c3 = 55296;
            er erVar3 = erVar;
        }
        int i102 = i6;
        ek ekVar = new ek(iArr3, objArr3, i5, i4, erVar.a, z, false, iArr, i6, i59, fsVar, dvVar, fhVar, ejVar, ffVar, null);
        return ekVar;
    }

    private static List O000000o(Object obj, long j2) {
        return (List) fr.O00000oo(obj, j2);
    }

    private static final void O000000o(int i2, Object obj, gb gbVar) {
        if (obj instanceof String) {
            gbVar.a(i2, (String) obj);
        } else {
            gbVar.O000000o(i2, (ck) obj);
        }
    }

    private static final void O000000o(fh fhVar, Object obj, gb gbVar) {
        ((fi) fhVar.O00000Oo(obj)).O00000Oo(gbVar);
    }

    private final void O000000o(gb gbVar, int i2, Object obj, int i3) {
        if (obj != null) {
            ff.O00000Oo(b(i3));
            throw null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0026, code lost:
        if (r5 != null) goto L_0x001f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void O000000o(Object obj, Object obj2, int i2) {
        long O00oOoOo = O00oOoOo(e(i2));
        if (O000000o(obj2, i2)) {
            Object O00000oo = fr.O00000oo(obj, O00oOoOo);
            Object O00000oo2 = fr.O00000oo(obj2, O00oOoOo);
            if (O00000oo != null && O00000oo2 != null) {
                O00000oo2 = dj.O000000o(O00000oo, O00000oo2);
            }
            fr.O000000o(obj, O00oOoOo, O00000oo2);
            O00000Oo(obj, i2);
        }
    }

    private final boolean O000000o(Object obj, int i2) {
        int O000O0OO = O000O0OO(i2);
        long j2 = (long) (1048575 & O000O0OO);
        if (j2 != 1048575) {
            return (fr.O000000o(obj, j2) & (1 << (O000O0OO >>> 20))) != 0;
        }
        int e2 = e(i2);
        long O00oOoOo = O00oOoOo(e2);
        switch (O000O0Oo(e2)) {
            case 0:
                return fr.O00000oO(obj, O00oOoOo) != 0.0d;
            case 1:
                return fr.O00000o(obj, O00oOoOo) != 0.0f;
            case 2:
                return fr.O00000Oo(obj, O00oOoOo) != 0;
            case 3:
                return fr.O00000Oo(obj, O00oOoOo) != 0;
            case 4:
                return fr.O000000o(obj, O00oOoOo) != 0;
            case 5:
                return fr.O00000Oo(obj, O00oOoOo) != 0;
            case 6:
                return fr.O000000o(obj, O00oOoOo) != 0;
            case 7:
                return fr.O00000o0(obj, O00oOoOo);
            case 8:
                Object O00000oo = fr.O00000oo(obj, O00oOoOo);
                if (O00000oo instanceof String) {
                    return !((String) O00000oo).isEmpty();
                }
                if (O00000oo instanceof ck) {
                    return !ck.b.equals(O00000oo);
                }
                throw new IllegalArgumentException();
            case 9:
                return fr.O00000oo(obj, O00oOoOo) != null;
            case 10:
                return !ck.b.equals(fr.O00000oo(obj, O00oOoOo));
            case 11:
                return fr.O000000o(obj, O00oOoOo) != 0;
            case 12:
                return fr.O000000o(obj, O00oOoOo) != 0;
            case 13:
                return fr.O000000o(obj, O00oOoOo) != 0;
            case 14:
                return fr.O00000Oo(obj, O00oOoOo) != 0;
            case 15:
                return fr.O000000o(obj, O00oOoOo) != 0;
            case 16:
                return fr.O00000Oo(obj, O00oOoOo) != 0;
            case 17:
                return fr.O00000oo(obj, O00oOoOo) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean O000000o(Object obj, int i2, int i3) {
        return fr.O000000o(obj, (long) (O000O0OO(i3) & 1048575)) == i2;
    }

    private final boolean O000000o(Object obj, int i2, int i3, int i4, int i5) {
        return i3 != 1048575 ? (i4 & i5) != 0 : O000000o(obj, i2);
    }

    private static boolean O000000o(Object obj, int i2, es esVar) {
        return esVar.O00000o0(fr.O00000oo(obj, O00oOoOo(i2)));
    }

    private static double O00000Oo(Object obj, long j2) {
        return ((Double) fr.O00000oo(obj, j2)).doubleValue();
    }

    private final void O00000Oo(Object obj, int i2) {
        int O000O0OO = O000O0OO(i2);
        long j2 = (long) (1048575 & O000O0OO);
        if (j2 != 1048575) {
            fr.O000000o(obj, j2, (1 << (O000O0OO >>> 20)) | fr.O000000o(obj, j2));
        }
    }

    private final void O00000Oo(Object obj, int i2, int i3) {
        fr.O000000o(obj, (long) (O000O0OO(i3) & 1048575), i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:105:0x02ea, code lost:
        r15 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x049b, code lost:
        r5 = r5 + 3;
        r8 = 1048575;
     */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x04a4  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void O00000Oo(Object obj, gb gbVar) {
        Entry entry;
        Iterator it;
        int length;
        int i2;
        Entry entry2;
        int i3;
        boolean z;
        Object obj2 = obj;
        gb gbVar2 = gbVar;
        if (this.h) {
            cu O00000Oo2 = ej.O00000Oo(obj);
            if (!O00000Oo2.a()) {
                it = O00000Oo2.d();
                entry = (Entry) it.next();
                length = this.c.length;
                Unsafe unsafe = b;
                int i4 = 1048575;
                Entry entry3 = entry;
                int i5 = 1048575;
                i2 = 0;
                int i6 = 0;
                while (i2 < length) {
                    int e2 = e(i2);
                    int d2 = d(i2);
                    int O000O0Oo = O000O0Oo(e2);
                    if (!this.i && O000O0Oo <= 17) {
                        int i7 = this.c[i2 + 2];
                        int i8 = i7 & i4;
                        if (i8 != i5) {
                            i6 = unsafe.getInt(obj2, (long) i8);
                        } else {
                            i8 = i5;
                        }
                        i5 = i8;
                        i3 = 1 << (i7 >>> 20);
                    } else {
                        i3 = 0;
                    }
                    while (entry3 != null && ej.O000000o(entry3) <= d2) {
                        ej.O000000o(gbVar2, entry3);
                        entry3 = it.hasNext() ? (Entry) it.next() : null;
                    }
                    int i9 = i3;
                    long O00oOoOo = O00oOoOo(e2);
                    switch (O000O0Oo) {
                        case 0:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O000000o(d2, fr.O00000oO(obj2, O00oOoOo));
                                break;
                            }
                        case 1:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O000000o(d2, fr.O00000o(obj2, O00oOoOo));
                                break;
                            }
                        case 2:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.a(d2, unsafe.getLong(obj2, O00oOoOo));
                                break;
                            }
                        case 3:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O000000o(d2, unsafe.getLong(obj2, O00oOoOo));
                                break;
                            }
                        case 4:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O000000o(d2, unsafe.getInt(obj2, O00oOoOo));
                                break;
                            }
                        case 5:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O00000Oo(d2, unsafe.getLong(obj2, O00oOoOo));
                                break;
                            }
                        case 6:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O00000Oo(d2, unsafe.getInt(obj2, O00oOoOo));
                                break;
                            }
                        case 7:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O000000o(d2, fr.O00000o0(obj2, O00oOoOo));
                                break;
                            }
                        case 8:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                O000000o(d2, unsafe.getObject(obj2, O00oOoOo), gbVar2);
                                break;
                            }
                        case 9:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O00000Oo(d2, unsafe.getObject(obj2, O00oOoOo), a(i2));
                                break;
                            }
                        case 10:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O000000o(d2, (ck) unsafe.getObject(obj2, O00oOoOo));
                                break;
                            }
                        case 11:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O00000o0(d2, unsafe.getInt(obj2, O00oOoOo));
                                break;
                            }
                        case 12:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O00000oO(d2, unsafe.getInt(obj2, O00oOoOo));
                                break;
                            }
                        case 13:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.a(d2, unsafe.getInt(obj2, O00oOoOo));
                                break;
                            }
                        case 14:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O00000o(d2, unsafe.getLong(obj2, O00oOoOo));
                                break;
                            }
                        case 15:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O00000o(d2, unsafe.getInt(obj2, O00oOoOo));
                                break;
                            }
                        case 16:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O00000o0(d2, unsafe.getLong(obj2, O00oOoOo));
                                break;
                            }
                        case 17:
                            if ((i6 & i9) == 0) {
                                break;
                            } else {
                                gbVar2.O000000o(d2, unsafe.getObject(obj2, O00oOoOo), a(i2));
                                break;
                            }
                        case 18:
                            eu.O000000o(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 19:
                            eu.O00000Oo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 20:
                            eu.O00000o0(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 21:
                            eu.O00000o(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 22:
                            eu.O0000OOo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 23:
                            eu.O00000oo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 24:
                            eu.O0000OoO(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 25:
                            eu.O0000o0(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 26:
                            eu.O000000o(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2);
                            break;
                        case 27:
                            eu.O000000o(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, a(i2));
                            break;
                        case 28:
                            eu.O00000Oo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2);
                            break;
                        case 29:
                            z = false;
                            eu.O0000Oo0(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 30:
                            z = false;
                            eu.O0000o00(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 31:
                            z = false;
                            eu.O0000Ooo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 32:
                            z = false;
                            eu.O0000O0o(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 33:
                            z = false;
                            eu.O0000Oo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 34:
                            z = false;
                            eu.O00000oO(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, false);
                            break;
                        case 35:
                            eu.O000000o(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 36:
                            eu.O00000Oo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 37:
                            eu.O00000o0(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 38:
                            eu.O00000o(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 39:
                            eu.O0000OOo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 40:
                            eu.O00000oo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 41:
                            eu.O0000OoO(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 42:
                            eu.O0000o0(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 43:
                            eu.O0000Oo0(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 44:
                            eu.O0000o00(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 45:
                            eu.O0000Ooo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 46:
                            eu.O0000O0o(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 47:
                            eu.O0000Oo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 48:
                            eu.O00000oO(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, true);
                            break;
                        case 49:
                            eu.O00000Oo(d(i2), (List) unsafe.getObject(obj2, O00oOoOo), gbVar2, a(i2));
                            break;
                        case 50:
                            O000000o(gbVar2, d2, unsafe.getObject(obj2, O00oOoOo), i2);
                            break;
                        case 51:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O000000o(d2, O00000Oo(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 52:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O000000o(d2, O00000o0(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 53:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.a(d2, O00000oO(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 54:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O000000o(d2, O00000oO(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 55:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O000000o(d2, O00000o(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 56:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O00000Oo(d2, O00000oO(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 57:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O00000Oo(d2, O00000o(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 58:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O000000o(d2, O00000oo(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 59:
                            if (O000000o(obj2, d2, i2)) {
                                O000000o(d2, unsafe.getObject(obj2, O00oOoOo), gbVar2);
                                break;
                            }
                            break;
                        case 60:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O00000Oo(d2, unsafe.getObject(obj2, O00oOoOo), a(i2));
                                break;
                            }
                            break;
                        case 61:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O000000o(d2, (ck) unsafe.getObject(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 62:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O00000o0(d2, O00000o(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 63:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O00000oO(d2, O00000o(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 64:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.a(d2, O00000o(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 65:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O00000o(d2, O00000oO(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 66:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O00000o(d2, O00000o(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 67:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O00000o0(d2, O00000oO(obj2, O00oOoOo));
                                break;
                            }
                            break;
                        case 68:
                            if (O000000o(obj2, d2, i2)) {
                                gbVar2.O000000o(d2, unsafe.getObject(obj2, O00oOoOo), a(i2));
                                break;
                            }
                            break;
                    }
                }
                while (entry2 != null) {
                    ej.O000000o(gbVar2, entry2);
                    entry2 = it.hasNext() ? (Entry) it.next() : null;
                }
                O000000o(this.o, obj2, gbVar2);
            }
        }
        it = null;
        entry = null;
        length = this.c.length;
        Unsafe unsafe2 = b;
        int i42 = 1048575;
        Entry entry32 = entry;
        int i52 = 1048575;
        i2 = 0;
        int i62 = 0;
        while (i2 < length) {
        }
        while (entry2 != null) {
        }
        O000000o(this.o, obj2, gbVar2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002a, code lost:
        if (r6 != null) goto L_0x0023;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void O00000Oo(Object obj, Object obj2, int i2) {
        int e2 = e(i2);
        int d2 = d(i2);
        long O00oOoOo = O00oOoOo(e2);
        if (O000000o(obj2, d2, i2)) {
            Object O00000oo = fr.O00000oo(obj, O00oOoOo);
            Object O00000oo2 = fr.O00000oo(obj2, O00oOoOo);
            if (O00000oo != null && O00000oo2 != null) {
                O00000oo2 = dj.O000000o(O00000oo, O00000oo2);
            }
            fr.O000000o(obj, O00oOoOo, O00000oo2);
            O00000Oo(obj, d2, i2);
        }
    }

    /* JADX WARNING: type inference failed for: r32v0, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v0 */
    /* JADX WARNING: type inference failed for: r12v1, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r0v3, types: [int, byte] */
    /* JADX WARNING: type inference failed for: r17v0, types: [int] */
    /* JADX WARNING: type inference failed for: r12v2 */
    /* JADX WARNING: type inference failed for: r12v3 */
    /* JADX WARNING: type inference failed for: r0v7, types: [int] */
    /* JADX WARNING: type inference failed for: r1v7, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v4 */
    /* JADX WARNING: type inference failed for: r1v13, types: [int] */
    /* JADX WARNING: type inference failed for: r2v12, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v5 */
    /* JADX WARNING: type inference failed for: r2v15, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r2v16, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r5v4, types: [int] */
    /* JADX WARNING: type inference failed for: r12v7 */
    /* JADX WARNING: type inference failed for: r2v19, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r5v6, types: [int] */
    /* JADX WARNING: type inference failed for: r17v4 */
    /* JADX WARNING: type inference failed for: r3v16, types: [int] */
    /* JADX WARNING: type inference failed for: r17v5 */
    /* JADX WARNING: type inference failed for: r12v9 */
    /* JADX WARNING: type inference failed for: r12v10 */
    /* JADX WARNING: type inference failed for: r12v11 */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x0018, code lost:
        r12 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00bf, code lost:
        if (r3 == 0) goto L_0x0183;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x011e, code lost:
        r7.putObject(r14, r8, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0152, code lost:
        r6 = r6 | r21;
        r9 = r7;
        r7 = r10;
        r1 = r19;
        r10 = -1;
        r29 = r13;
        r13 = r2;
        r2 = r29;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0181, code lost:
        if (r3 == 0) goto L_0x0183;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0183, code lost:
        r0 = defpackage.eb.O000000o((byte[]) r12, r4, r11);
        r1 = r11.a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0189, code lost:
        r7.putInt(r14, r8, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x019f, code lost:
        r0.putLong(r1, r2, r4);
        r6 = r6 | r21;
        r9 = r7;
        r7 = r10;
        r2 = r13;
        r0 = r17;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01cd, code lost:
        r6 = r6 | r21;
        r9 = r7;
        r7 = r10;
        r2 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x01d2, code lost:
        r1 = r19;
        r10 = -1;
        r13 = r34;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01d9, code lost:
        r2 = r4;
        r8 = r7;
        r25 = r10;
        r20 = r13;
        r7 = r14;
        r9 = r15;
        r18 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x022c, code lost:
        if (r0 != r15) goto L_0x022e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0246, code lost:
        r9 = r30;
        r7 = r31;
        r2 = r0;
        r6 = r24;
        r8 = r28;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0284, code lost:
        if (r0 != r15) goto L_0x022e;
     */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=null, for r0v3, types: [int, byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r32v0, types: [byte[]] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r12v2
  assigns: []
  uses: []
  mth insns count: 371
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 16 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void O00000Oo(Object obj, byte[] r32, int i2, int i3, by byVar) {
        ? r12;
        ? r17;
        int i4;
        int i5;
        Unsafe unsafe;
        int i6;
        int i7;
        int i8;
        ek ekVar;
        Object obj2;
        int i9;
        int O000000o2;
        ? r122;
        int i10;
        Unsafe unsafe2;
        int i11;
        int i12;
        int O000000o3;
        Unsafe unsafe3;
        int i13;
        int i14;
        int i15;
        long j2;
        long j3;
        Object obj3;
        Unsafe unsafe4;
        int i16;
        Object obj4;
        ek ekVar2 = this;
        Object obj5 = obj;
        ? r123 = r32;
        int i17 = i3;
        by byVar2 = byVar;
        Unsafe unsafe5 = b;
        int i18 = -1;
        int i19 = i2;
        int i20 = -1;
        int i21 = 0;
        int i22 = 0;
        int i23 = 1048575;
        while (i19 < i17) {
            int i24 = i19 + 1;
            ? r0 = r12[i19];
            if (r0 < 0) {
                i4 = eb.O000000o((int) r0, (byte[]) r12, i24, byVar2);
                r17 = byVar2.a;
            } else {
                r17 = r0;
                i4 = i24;
            }
            int i25 = r17 >>> 3;
            int i26 = r17 & 7;
            int a2 = i25 > i20 ? ekVar2.a(i25, i21 / 3) : ekVar2.O000O0o0(i25);
            if (a2 != i18) {
                int i27 = ekVar2.c[a2 + 1];
                int O000O0Oo = O000O0Oo(i27);
                Unsafe unsafe6 = unsafe5;
                long O00oOoOo = O00oOoOo(i27);
                int i28 = i25;
                if (O000O0Oo <= 17) {
                    int i29 = ekVar2.c[a2 + 2];
                    boolean z = true;
                    int i30 = 1 << (i29 >>> 20);
                    int i31 = i29 & 1048575;
                    int i32 = i27;
                    int i33 = a2;
                    if (i31 == i23) {
                        i31 = i23;
                        unsafe3 = unsafe6;
                    } else {
                        if (i23 != 1048575) {
                            long j4 = (long) i23;
                            unsafe3 = unsafe6;
                            unsafe3.putInt(obj5, j4, i22);
                        } else {
                            unsafe3 = unsafe6;
                        }
                        if (i31 != 1048575) {
                            i22 = unsafe3.getInt(obj5, (long) i31);
                        }
                    }
                    switch (O000O0Oo) {
                        case 0:
                            i13 = i33;
                            i7 = i28;
                            if (i26 == 1) {
                                fr.O000000o(obj5, O00oOoOo, eb.O00000o0(r12, i4));
                                i14 = i4 + 8;
                                break;
                            }
                            break;
                        case 1:
                            i13 = i33;
                            i7 = i28;
                            if (i26 == 5) {
                                fr.O000000o(obj5, O00oOoOo, eb.O00000o(r12, i4));
                                i14 = i4 + 4;
                                break;
                            }
                            break;
                        case 2:
                        case 3:
                            i13 = i33;
                            i7 = i28;
                            if (i26 == 0) {
                                i15 = eb.O00000Oo(r12, i4, byVar2);
                                j2 = byVar2.b;
                                unsafe4 = unsafe3;
                                obj3 = obj;
                                j3 = O00oOoOo;
                                break;
                            }
                            break;
                        case 4:
                        case 11:
                            i13 = i33;
                            i7 = i28;
                            break;
                        case 5:
                        case 14:
                            int i34 = i3;
                            i13 = i33;
                            i7 = i28;
                            if (i26 == 1) {
                                long j5 = O00oOoOo;
                                int i35 = i4;
                                unsafe3.putLong(obj, j5, eb.O00000Oo(r12, i4));
                                i14 = i35 + 8;
                                break;
                            }
                            break;
                        case 6:
                        case 13:
                            i16 = i3;
                            i13 = i33;
                            i7 = i28;
                            if (i26 == 5) {
                                unsafe3.putInt(obj5, O00oOoOo, eb.O000000o(r12, i4));
                                i19 = i4 + 4;
                                break;
                            }
                            break;
                        case 7:
                            i16 = i3;
                            i13 = i33;
                            i7 = i28;
                            if (i26 == 0) {
                                i19 = eb.O00000Oo(r12, i4, byVar2);
                                if (byVar2.b == 0) {
                                    z = false;
                                }
                                fr.O000000o(obj5, O00oOoOo, z);
                                break;
                            }
                            break;
                        case 8:
                            i16 = i3;
                            i13 = i33;
                            i7 = i28;
                            if (i26 == 2) {
                                i19 = (i32 & PropertyOptions.DELETE_EXISTING) != 0 ? eb.O00000o(r12, i4, byVar2) : eb.O00000o0(r12, i4, byVar2);
                                obj4 = byVar2.c;
                                break;
                            }
                            break;
                        case 9:
                            i13 = i33;
                            i7 = i28;
                            if (i26 != 2) {
                                int i36 = i3;
                                break;
                            } else {
                                i16 = i3;
                                i19 = eb.O000000o(ekVar2.a(i13), (byte[]) r12, i4, i16, byVar2);
                                Object object = unsafe3.getObject(obj5, O00oOoOo);
                                if (object != null) {
                                    obj4 = dj.O000000o(object, byVar2.c);
                                    break;
                                } else {
                                    obj4 = byVar2.c;
                                    break;
                                }
                            }
                        case 10:
                            i13 = i33;
                            i7 = i28;
                            if (i26 == 2) {
                                i14 = eb.O00000oO(r12, i4, byVar2);
                                unsafe3.putObject(obj5, O00oOoOo, byVar2.c);
                                break;
                            }
                            break;
                        case 12:
                            i13 = i33;
                            i7 = i28;
                            break;
                        case 15:
                            i13 = i33;
                            i7 = i28;
                            if (i26 == 0) {
                                i14 = eb.O000000o((byte[]) r12, i4, byVar2);
                                int i37 = cl.a(byVar2.a);
                                break;
                            }
                            break;
                        case 16:
                            if (i26 != 0) {
                                i13 = i33;
                                i7 = i28;
                                break;
                            } else {
                                i15 = eb.O00000Oo(r12, i4, byVar2);
                                j2 = cl.a(byVar2.b);
                                unsafe4 = unsafe3;
                                obj3 = obj;
                                i13 = i33;
                                j3 = O00oOoOo;
                                i7 = i28;
                                break;
                            }
                        default:
                            i13 = i33;
                            i7 = i28;
                            break;
                    }
                } else {
                    i7 = i28;
                    int i38 = i27;
                    int i39 = a2;
                    Unsafe unsafe7 = unsafe6;
                    if (O000O0Oo != 27) {
                        if (O000O0Oo <= 49) {
                            int i40 = i4;
                            i11 = i22;
                            i5 = i23;
                            unsafe2 = unsafe7;
                            i8 = -1;
                            i6 = i39;
                            O000000o3 = O000000o(obj, (byte[]) r32, i4, i3, (int) r17, i7, i26, i39, (long) i38, O000O0Oo, O00oOoOo, byVar);
                        } else {
                            int i41 = i26;
                            i12 = i4;
                            i11 = i22;
                            i5 = i23;
                            long j6 = O00oOoOo;
                            unsafe2 = unsafe7;
                            i6 = i39;
                            int i42 = i38;
                            i8 = -1;
                            int i43 = O000O0Oo;
                            if (i43 != 50) {
                                O000000o3 = O000000o(obj, (byte[]) r32, i12, i3, (int) r17, i7, i41, i42, i43, j6, i6, byVar);
                            } else if (i41 == 2) {
                                O000000o(obj, r32, i12, i3, i6, j6, byVar);
                                throw null;
                            }
                        }
                        ekVar2 = this;
                        obj5 = obj;
                        r12 = r32;
                        i17 = i3;
                        byVar2 = byVar;
                        i18 = i8;
                        i20 = i7;
                        i21 = i6;
                        i22 = i11;
                        i23 = i5;
                        unsafe5 = unsafe2;
                        r123 = r12;
                    } else {
                        i12 = i4;
                        i11 = i22;
                        i5 = i23;
                        long j7 = O00oOoOo;
                        unsafe2 = unsafe7;
                        i6 = i39;
                        i8 = -1;
                        if (i26 == 2) {
                            Object obj6 = obj;
                            long j8 = j7;
                            unsafe = unsafe2;
                            di diVar = (di) unsafe.getObject(obj6, j8);
                            if (!diVar.a()) {
                                int size = diVar.size();
                                diVar = diVar.a(size != 0 ? size + size : 10);
                                unsafe.putObject(obj6, j8, diVar);
                            }
                            int i44 = i6;
                            O000000o2 = eb.O000000o(a(i44), (int) r17, (byte[]) r32, i12, i3, diVar, byVar);
                            r122 = r32;
                            i17 = i3;
                            byVar2 = byVar;
                            obj5 = obj6;
                            ekVar2 = this;
                            i10 = i44;
                            i18 = -1;
                            i20 = i7;
                            i22 = i11;
                            i23 = i5;
                            unsafe5 = unsafe;
                            r12 = r123;
                            r123 = r12;
                        }
                    }
                    ekVar = this;
                    obj2 = obj;
                    unsafe = unsafe2;
                    i9 = i12;
                    i6 = i6;
                    i22 = i11;
                }
            } else {
                i7 = i25;
                int i45 = i22;
                i5 = i23;
                unsafe = unsafe5;
                i8 = i18;
                obj2 = obj5;
                ekVar = ekVar2;
                i9 = i4;
                i6 = 0;
            }
            O000000o2 = eb.O000000o((int) r17, (byte[]) r32, i9, i3, O0000O0o(obj), byVar);
            r122 = r32;
            i17 = i3;
            byVar2 = byVar;
            obj5 = obj2;
            ekVar2 = ekVar;
            i18 = i8;
            i20 = i7;
            i10 = i6;
            i23 = i5;
            unsafe5 = unsafe;
            r12 = r123;
            r123 = r12;
        }
        int i46 = i22;
        int i47 = i23;
        Unsafe unsafe8 = unsafe5;
        Object obj7 = obj5;
        if (i47 != 1048575) {
            unsafe8.putInt(obj7, (long) i47, i46);
        }
        if (i19 != i3) {
            throw dl.e();
        }
    }

    private static int O00000o(Object obj, long j2) {
        return ((Integer) fr.O00000oo(obj, j2)).intValue();
    }

    private static float O00000o0(Object obj, long j2) {
        return ((Float) fr.O00000oo(obj, j2)).floatValue();
    }

    private final boolean O00000o0(Object obj, Object obj2, int i2) {
        return O000000o(obj, i2) == O000000o(obj2, i2);
    }

    private final int O00000oO(int i2, int i3) {
        int length = (this.c.length / 3) - 1;
        while (i3 <= length) {
            int i4 = (length + i3) >>> 1;
            int i5 = i4 * 3;
            int d2 = d(i5);
            if (i2 == d2) {
                return i5;
            }
            if (i2 >= d2) {
                i3 = i4 + 1;
            } else {
                length = i4 - 1;
            }
        }
        return -1;
    }

    private static long O00000oO(Object obj, long j2) {
        return ((Long) fr.O00000oo(obj, j2)).longValue();
    }

    private static boolean O00000oo(Object obj, long j2) {
        return ((Boolean) fr.O00000oo(obj, j2)).booleanValue();
    }

    static fi O0000O0o(Object obj) {
        de deVar = (de) obj;
        fi fiVar = deVar.h;
        if (fiVar != fi.a) {
            return fiVar;
        }
        fi a2 = fi.a();
        deVar.h = a2;
        return a2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0249, code lost:
        r3 = defpackage.cn.O000000o(r5, (defpackage.eh) defpackage.fr.O00000oo(r10, r6), a(r1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0262, code lost:
        r3 = defpackage.cn.O00000Oo(r5, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0271, code lost:
        r3 = defpackage.cn.O00000Oo(r5, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x027c, code lost:
        r3 = defpackage.cn.O000OOOo(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x0287, code lost:
        r3 = defpackage.cn.O000OO(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x0296, code lost:
        r3 = defpackage.cn.O00000o0(r5, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x02a5, code lost:
        r3 = defpackage.cn.O000000o(r5, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x02b0, code lost:
        r3 = defpackage.fr.O00000oo(r10, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x02b4, code lost:
        r3 = defpackage.cn.O000000o(r5, (defpackage.ck) r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x02c1, code lost:
        r3 = defpackage.eu.O00000Oo(r5, defpackage.fr.O00000oo(r10, r6), a(r1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x02db, code lost:
        if ((r3 instanceof defpackage.ck) != false) goto L_0x02b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x02de, code lost:
        r3 = defpackage.cn.a(r5, (java.lang.String) r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x02ec, code lost:
        r3 = defpackage.cn.O000O0o(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x02f8, code lost:
        r3 = defpackage.cn.O000O0oo(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x0304, code lost:
        r3 = defpackage.cn.O000OO00(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x0314, code lost:
        r3 = defpackage.cn.O00000oO(r5, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x0324, code lost:
        r3 = defpackage.cn.O000000o(r5, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:156:0x0334, code lost:
        r3 = defpackage.cn.O00000o(r5, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x0340, code lost:
        r3 = defpackage.cn.O000OO0o(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x034c, code lost:
        r3 = defpackage.cn.O000O0oO(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x0352, code lost:
        r1 = r1 + 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0094, code lost:
        if ((r3 instanceof defpackage.ck) != false) goto L_0x02b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x01ba, code lost:
        r2 = r2 + ((defpackage.cn.a(r5) + defpackage.cn.c(r3)) + r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0240, code lost:
        r2 = r2 + r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int O0000Oo(Object obj) {
        int O000O0oO;
        long j2;
        long j3;
        int i2;
        Object O00000oo;
        int i3;
        int i4;
        int i5;
        long j4;
        int i6;
        Unsafe unsafe = b;
        int i7 = 0;
        int i8 = 0;
        while (i7 < this.c.length) {
            int e2 = e(i7);
            int O000O0Oo = O000O0Oo(e2);
            int d2 = d(i7);
            long O00oOoOo = O00oOoOo(e2);
            if (O000O0Oo >= cw.DOUBLE_LIST_PACKED.Z && O000O0Oo <= cw.SINT64_LIST_PACKED.Z) {
                int i9 = this.c[i7 + 2];
            }
            switch (O000O0Oo) {
                case 0:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 1:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 2:
                    if (!O000000o(obj, i7)) {
                        break;
                    } else {
                        j2 = fr.O00000Oo(obj, O00oOoOo);
                        break;
                    }
                case 3:
                    if (!O000000o(obj, i7)) {
                        break;
                    } else {
                        j3 = fr.O00000Oo(obj, O00oOoOo);
                        break;
                    }
                case 4:
                    if (!O000000o(obj, i7)) {
                        break;
                    } else {
                        i2 = fr.O000000o(obj, O00oOoOo);
                        break;
                    }
                case 5:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 6:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 7:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 8:
                    if (!O000000o(obj, i7)) {
                        break;
                    } else {
                        O00000oo = fr.O00000oo(obj, O00oOoOo);
                        break;
                    }
                case 9:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 10:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 11:
                    if (!O000000o(obj, i7)) {
                        break;
                    } else {
                        i3 = fr.O000000o(obj, O00oOoOo);
                        break;
                    }
                case 12:
                    if (!O000000o(obj, i7)) {
                        break;
                    } else {
                        i4 = fr.O000000o(obj, O00oOoOo);
                        break;
                    }
                case 13:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 14:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 15:
                    if (!O000000o(obj, i7)) {
                        break;
                    } else {
                        i5 = fr.O000000o(obj, O00oOoOo);
                        break;
                    }
                case 16:
                    if (!O000000o(obj, i7)) {
                        break;
                    } else {
                        j4 = fr.O00000Oo(obj, O00oOoOo);
                        break;
                    }
                case 17:
                    if (!O000000o(obj, i7)) {
                        break;
                    }
                    break;
                case 18:
                case 23:
                case 32:
                    O000O0oO = eu.O00000oo(d2, O000000o(obj, O00oOoOo));
                    break;
                case 19:
                case 24:
                case 31:
                    O000O0oO = eu.O00000oO(d2, O000000o(obj, O00oOoOo));
                    break;
                case 20:
                    O000O0oO = eu.O0000OOo(d2, O000000o(obj, O00oOoOo));
                    break;
                case 21:
                    O000O0oO = eu.O0000Ooo(d2, O000000o(obj, O00oOoOo));
                    break;
                case 22:
                    O000O0oO = eu.O0000O0o(d2, O000000o(obj, O00oOoOo));
                    break;
                case 25:
                    O000O0oO = eu.O00000o0(d2, O000000o(obj, O00oOoOo));
                    break;
                case 26:
                    O000O0oO = eu.O000000o(d2, O000000o(obj, O00oOoOo));
                    break;
                case 27:
                    O000O0oO = eu.O000000o(d2, O000000o(obj, O00oOoOo), a(i7));
                    break;
                case 28:
                    O000O0oO = eu.O00000Oo(d2, O000000o(obj, O00oOoOo));
                    break;
                case 29:
                    O000O0oO = eu.O0000OoO(d2, O000000o(obj, O00oOoOo));
                    break;
                case 30:
                    O000O0oO = eu.O00000o(d2, O000000o(obj, O00oOoOo));
                    break;
                case 33:
                    O000O0oO = eu.O0000Oo0(d2, O000000o(obj, O00oOoOo));
                    break;
                case 34:
                    O000O0oO = eu.O0000Oo(d2, O000000o(obj, O00oOoOo));
                    break;
                case 35:
                    i6 = eu.O0000Oo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 36:
                    i6 = eu.O0000Oo0((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 37:
                    i6 = eu.a((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 38:
                    i6 = eu.b((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 39:
                    i6 = eu.O00000oo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 40:
                    i6 = eu.O0000Oo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 41:
                    i6 = eu.O0000Oo0((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 42:
                    i6 = eu.O0000OoO((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 43:
                    i6 = eu.O0000O0o((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 44:
                    i6 = eu.O00000oO((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 45:
                    i6 = eu.O0000Oo0((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 46:
                    i6 = eu.O0000Oo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 47:
                    i6 = eu.O0000OOo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 48:
                    i6 = eu.O00000o((List) unsafe.getObject(obj, O00oOoOo));
                    if (i6 <= 0) {
                        break;
                    }
                case 49:
                    O000O0oO = eu.O00000Oo(d2, O000000o(obj, O00oOoOo), a(i7));
                    break;
                case 50:
                    ff.O000000o(fr.O00000oo(obj, O00oOoOo), b(i7));
                    break;
                case 51:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
                case 52:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
                case 53:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    } else {
                        j2 = O00000oO(obj, O00oOoOo);
                        break;
                    }
                case 54:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    } else {
                        j3 = O00000oO(obj, O00oOoOo);
                        break;
                    }
                case 55:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    } else {
                        i2 = O00000o(obj, O00oOoOo);
                        break;
                    }
                case 56:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
                case 57:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
                case 58:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
                case 59:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    } else {
                        O00000oo = fr.O00000oo(obj, O00oOoOo);
                        break;
                    }
                case 60:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
                case 61:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
                case 62:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    } else {
                        i3 = O00000o(obj, O00oOoOo);
                        break;
                    }
                case 63:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    } else {
                        i4 = O00000o(obj, O00oOoOo);
                        break;
                    }
                case 64:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
                case 65:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
                case 66:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    } else {
                        i5 = O00000o(obj, O00oOoOo);
                        break;
                    }
                case 67:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    } else {
                        j4 = O00000oO(obj, O00oOoOo);
                        break;
                    }
                case 68:
                    if (!O000000o(obj, d2, i7)) {
                        break;
                    }
                    break;
            }
        }
        return i8 + O000000o(this.o, obj);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x026d, code lost:
        r4 = r4 + r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0274, code lost:
        r7 = defpackage.cn.O000000o(r8, (defpackage.eh) r0.getObject(r14, r11), a(r3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x028b, code lost:
        r7 = defpackage.cn.O00000Oo(r8, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0298, code lost:
        r7 = defpackage.cn.O00000Oo(r8, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x02a1, code lost:
        r7 = defpackage.cn.O000OOOo(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x02aa, code lost:
        r7 = defpackage.cn.O000OO(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x02b7, code lost:
        r7 = defpackage.cn.O00000o0(r8, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x02c4, code lost:
        r7 = defpackage.cn.O000000o(r8, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x02cd, code lost:
        r7 = r0.getObject(r14, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x02d1, code lost:
        r7 = defpackage.cn.O000000o(r8, (defpackage.ck) r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x02dc, code lost:
        r7 = defpackage.eu.O00000Oo(r8, r0.getObject(r14, r11), a(r3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x02f3, code lost:
        if ((r7 instanceof defpackage.ck) != false) goto L_0x02d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x02f6, code lost:
        r7 = defpackage.cn.a(r8, (java.lang.String) r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x0302, code lost:
        r7 = defpackage.cn.O000O0o(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x030c, code lost:
        r7 = defpackage.cn.O000O0oo(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x0316, code lost:
        r7 = defpackage.cn.O000OO00(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x0324, code lost:
        r7 = defpackage.cn.O00000oO(r8, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x0332, code lost:
        r7 = defpackage.cn.O000000o(r8, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x0340, code lost:
        r7 = defpackage.cn.O00000o(r8, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:161:0x034a, code lost:
        r7 = defpackage.cn.O000OO0o(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x0354, code lost:
        r7 = defpackage.cn.O000O0oO(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x035a, code lost:
        r3 = r3 + 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a3, code lost:
        if ((r7 instanceof defpackage.ck) != false) goto L_0x02d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01cb, code lost:
        r4 = r4 + ((defpackage.cn.a(r8) + defpackage.cn.c(r7)) + r7);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int O0000Oo0(Object obj) {
        int i2;
        int O000O0oO;
        long j2;
        long j3;
        int i3;
        Object object;
        int i4;
        int i5;
        int i6;
        long j4;
        int i7;
        Unsafe unsafe = b;
        int i8 = 1048575;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        while (i9 < this.c.length) {
            int e2 = e(i9);
            int d2 = d(i9);
            int O000O0Oo = O000O0Oo(e2);
            if (O000O0Oo <= 17) {
                int i12 = this.c[i9 + 2];
                int i13 = i12 & 1048575;
                i2 = 1 << (i12 >>> 20);
                if (i13 != i8) {
                    i11 = unsafe.getInt(obj, (long) i13);
                    i8 = i13;
                }
            } else {
                i2 = 0;
            }
            long O00oOoOo = O00oOoOo(e2);
            switch (O000O0Oo) {
                case 0:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 1:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 2:
                    if ((i11 & i2) == 0) {
                        break;
                    } else {
                        j2 = unsafe.getLong(obj, O00oOoOo);
                        break;
                    }
                case 3:
                    if ((i11 & i2) == 0) {
                        break;
                    } else {
                        j3 = unsafe.getLong(obj, O00oOoOo);
                        break;
                    }
                case 4:
                    if ((i11 & i2) == 0) {
                        break;
                    } else {
                        i3 = unsafe.getInt(obj, O00oOoOo);
                        break;
                    }
                case 5:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 6:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 7:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 8:
                    if ((i11 & i2) == 0) {
                        break;
                    } else {
                        object = unsafe.getObject(obj, O00oOoOo);
                        break;
                    }
                case 9:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 10:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 11:
                    if ((i11 & i2) == 0) {
                        break;
                    } else {
                        i4 = unsafe.getInt(obj, O00oOoOo);
                        break;
                    }
                case 12:
                    if ((i11 & i2) == 0) {
                        break;
                    } else {
                        i5 = unsafe.getInt(obj, O00oOoOo);
                        break;
                    }
                case 13:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 14:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 15:
                    if ((i11 & i2) == 0) {
                        break;
                    } else {
                        i6 = unsafe.getInt(obj, O00oOoOo);
                        break;
                    }
                case 16:
                    if ((i11 & i2) == 0) {
                        break;
                    } else {
                        j4 = unsafe.getLong(obj, O00oOoOo);
                        break;
                    }
                case 17:
                    if ((i11 & i2) == 0) {
                        break;
                    }
                    break;
                case 18:
                case 23:
                case 32:
                    O000O0oO = eu.O00000oo(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 19:
                case 24:
                case 31:
                    O000O0oO = eu.O00000oO(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 20:
                    O000O0oO = eu.O0000OOo(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 21:
                    O000O0oO = eu.O0000Ooo(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 22:
                    O000O0oO = eu.O0000O0o(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 25:
                    O000O0oO = eu.O00000o0(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 26:
                    O000O0oO = eu.O000000o(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 27:
                    O000O0oO = eu.O000000o(d2, (List) unsafe.getObject(obj, O00oOoOo), a(i9));
                    break;
                case 28:
                    O000O0oO = eu.O00000Oo(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 29:
                    O000O0oO = eu.O0000OoO(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 30:
                    O000O0oO = eu.O00000o(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 33:
                    O000O0oO = eu.O0000Oo0(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 34:
                    O000O0oO = eu.O0000Oo(d2, (List) unsafe.getObject(obj, O00oOoOo));
                    break;
                case 35:
                    i7 = eu.O0000Oo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 36:
                    i7 = eu.O0000Oo0((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 37:
                    i7 = eu.a((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 38:
                    i7 = eu.b((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 39:
                    i7 = eu.O00000oo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 40:
                    i7 = eu.O0000Oo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 41:
                    i7 = eu.O0000Oo0((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 42:
                    i7 = eu.O0000OoO((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 43:
                    i7 = eu.O0000O0o((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 44:
                    i7 = eu.O00000oO((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 45:
                    i7 = eu.O0000Oo0((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 46:
                    i7 = eu.O0000Oo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 47:
                    i7 = eu.O0000OOo((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 48:
                    i7 = eu.O00000o((List) unsafe.getObject(obj, O00oOoOo));
                    if (i7 <= 0) {
                        break;
                    }
                case 49:
                    O000O0oO = eu.O00000Oo(d2, (List) unsafe.getObject(obj, O00oOoOo), a(i9));
                    break;
                case 50:
                    ff.O000000o(unsafe.getObject(obj, O00oOoOo), b(i9));
                    break;
                case 51:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
                case 52:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
                case 53:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    } else {
                        j2 = O00000oO(obj, O00oOoOo);
                        break;
                    }
                case 54:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    } else {
                        j3 = O00000oO(obj, O00oOoOo);
                        break;
                    }
                case 55:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    } else {
                        i3 = O00000o(obj, O00oOoOo);
                        break;
                    }
                case 56:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
                case 57:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
                case 58:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
                case 59:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    } else {
                        object = unsafe.getObject(obj, O00oOoOo);
                        break;
                    }
                case 60:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
                case 61:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
                case 62:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    } else {
                        i4 = O00000o(obj, O00oOoOo);
                        break;
                    }
                case 63:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    } else {
                        i5 = O00000o(obj, O00oOoOo);
                        break;
                    }
                case 64:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
                case 65:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
                case 66:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    } else {
                        i6 = O00000o(obj, O00oOoOo);
                        break;
                    }
                case 67:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    } else {
                        j4 = O00000oO(obj, O00oOoOo);
                        break;
                    }
                case 68:
                    if (!O000000o(obj, d2, i9)) {
                        break;
                    }
                    break;
            }
        }
        int O000000o2 = i10 + O000000o(this.o, obj);
        if (!this.h) {
            return O000000o2;
        }
        cu O00000Oo2 = ej.O00000Oo(obj);
        int i14 = 0;
        for (int i15 = 0; i15 < O00000Oo2.a.a(); i15++) {
            Entry b2 = O00000Oo2.a.b(i15);
            i14 += cu.O00000Oo((dd) b2.getKey(), b2.getValue());
        }
        for (Entry entry : O00000Oo2.a.b()) {
            i14 += cu.O00000Oo((dd) entry.getKey(), entry.getValue());
        }
        return O000000o2 + i14;
    }

    private final int O000O0OO(int i2) {
        return this.c[i2 + 2];
    }

    private static int O000O0Oo(int i2) {
        return (i2 >>> 20) & 255;
    }

    private final int O000O0o0(int i2) {
        if (i2 < this.e || i2 > this.f) {
            return -1;
        }
        return O00000oO(i2, 0);
    }

    private static long O00oOoOo(int i2) {
        return (long) (i2 & 1048575);
    }

    private final int a(int i2, int i3) {
        if (i2 < this.e || i2 > this.f) {
            return -1;
        }
        return O00000oO(i2, i3);
    }

    private final es a(int i2) {
        int i3 = i2 / 3;
        int i4 = i3 + i3;
        Object[] objArr = this.d;
        es esVar = (es) objArr[i4];
        if (esVar != null) {
            return esVar;
        }
        es O00000Oo2 = ep.a.O00000Oo((Class) objArr[i4 + 1]);
        this.d[i4] = O00000Oo2;
        return O00000Oo2;
    }

    private static Field a(Class cls, String str) {
        try {
            r6 = cls;
            r6 = cls.getDeclaredField(str);
            r6 = r6;
            return r6;
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = r6.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            String name = r6.getName();
            String arrays = Arrays.toString(declaredFields);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 40 + String.valueOf(name).length() + String.valueOf(arrays).length());
            sb.append("Field ");
            sb.append(str);
            sb.append(" for ");
            sb.append(name);
            sb.append(" not found. Known fields are ");
            sb.append(arrays);
            throw new RuntimeException(sb.toString());
        }
    }

    private final Object b(int i2) {
        int i3 = i2 / 3;
        return this.d[i3 + i3];
    }

    private final dh c(int i2) {
        int i3 = i2 / 3;
        return (dh) this.d[i3 + i3 + 1];
    }

    private final int d(int i2) {
        return this.c[i2];
    }

    private final int e(int i2) {
        return this.c[i2 + 1];
    }

    /* JADX WARNING: type inference failed for: r31v0, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v0 */
    /* JADX WARNING: type inference failed for: r12v1, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r0v10, types: [int, byte] */
    /* JADX WARNING: type inference failed for: r4v1, types: [int] */
    /* JADX WARNING: type inference failed for: r12v2 */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* JADX WARNING: type inference failed for: r10v5 */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: type inference failed for: r12v4 */
    /* JADX WARNING: type inference failed for: r0v15, types: [int] */
    /* JADX WARNING: type inference failed for: r1v10, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r0v19, types: [int] */
    /* JADX WARNING: type inference failed for: r1v13, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r10v7 */
    /* JADX WARNING: type inference failed for: r11v6 */
    /* JADX WARNING: type inference failed for: r19v0 */
    /* JADX WARNING: type inference failed for: r10v8 */
    /* JADX WARNING: type inference failed for: r19v1 */
    /* JADX WARNING: type inference failed for: r1v18, types: [int] */
    /* JADX WARNING: type inference failed for: r2v12, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v6 */
    /* JADX WARNING: type inference failed for: r19v2 */
    /* JADX WARNING: type inference failed for: r2v18, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r2v19, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r5v9, types: [int] */
    /* JADX WARNING: type inference failed for: r19v3 */
    /* JADX WARNING: type inference failed for: r10v11 */
    /* JADX WARNING: type inference failed for: r19v4 */
    /* JADX WARNING: type inference failed for: r12v8 */
    /* JADX WARNING: type inference failed for: r1v22 */
    /* JADX WARNING: type inference failed for: r2v22, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r5v11, types: [int] */
    /* JADX WARNING: type inference failed for: r19v6 */
    /* JADX WARNING: type inference failed for: r11v13 */
    /* JADX WARNING: type inference failed for: r28v0 */
    /* JADX WARNING: type inference failed for: r10v13 */
    /* JADX WARNING: type inference failed for: r12v9 */
    /* JADX WARNING: type inference failed for: r11v15 */
    /* JADX WARNING: type inference failed for: r1v25 */
    /* JADX WARNING: type inference failed for: r11v17 */
    /* JADX WARNING: type inference failed for: r11v18 */
    /* JADX WARNING: type inference failed for: r11v19 */
    /* JADX WARNING: type inference failed for: r11v20 */
    /* JADX WARNING: type inference failed for: r11v21 */
    /* JADX WARNING: type inference failed for: r11v22 */
    /* JADX WARNING: type inference failed for: r11v23 */
    /* JADX WARNING: type inference failed for: r11v24 */
    /* JADX WARNING: type inference failed for: r11v25 */
    /* JADX WARNING: type inference failed for: r11v26 */
    /* JADX WARNING: type inference failed for: r11v27 */
    /* JADX WARNING: type inference failed for: r11v28 */
    /* JADX WARNING: type inference failed for: r11v29 */
    /* JADX WARNING: type inference failed for: r11v30 */
    /* JADX WARNING: type inference failed for: r11v31, types: [int] */
    /* JADX WARNING: type inference failed for: r11v32 */
    /* JADX WARNING: type inference failed for: r11v33 */
    /* JADX WARNING: type inference failed for: r11v34 */
    /* JADX WARNING: type inference failed for: r11v35 */
    /* JADX WARNING: type inference failed for: r11v36 */
    /* JADX WARNING: type inference failed for: r1v59, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v11 */
    /* JADX WARNING: type inference failed for: r4v27 */
    /* JADX WARNING: type inference failed for: r1v68, types: [int] */
    /* JADX WARNING: type inference failed for: r4v28 */
    /* JADX WARNING: type inference failed for: r12v12 */
    /* JADX WARNING: type inference failed for: r12v13 */
    /* JADX WARNING: type inference failed for: r1v70 */
    /* JADX WARNING: type inference failed for: r10v14 */
    /* JADX WARNING: type inference failed for: r1v71 */
    /* JADX WARNING: type inference failed for: r12v14 */
    /* JADX WARNING: type inference failed for: r11v37 */
    /* JADX WARNING: type inference failed for: r11v38 */
    /* JADX WARNING: type inference failed for: r11v39 */
    /* JADX WARNING: type inference failed for: r11v40 */
    /* JADX WARNING: type inference failed for: r11v41 */
    /* JADX WARNING: type inference failed for: r11v42 */
    /* JADX WARNING: type inference failed for: r11v43 */
    /* JADX WARNING: type inference failed for: r11v44 */
    /* JADX WARNING: type inference failed for: r11v45 */
    /* JADX WARNING: type inference failed for: r11v46 */
    /* JADX WARNING: type inference failed for: r11v47 */
    /* JADX WARNING: type inference failed for: r11v48 */
    /* JADX WARNING: type inference failed for: r11v49 */
    /* JADX WARNING: type inference failed for: r11v50 */
    /* JADX WARNING: type inference failed for: r11v51 */
    /* JADX WARNING: type inference failed for: r11v52 */
    /* JADX WARNING: type inference failed for: r11v53 */
    /* JADX WARNING: type inference failed for: r11v54 */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x02f5, code lost:
        if (r0 != r14) goto L_0x02a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x001a, code lost:
        r12 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00e9, code lost:
        r10.putInt(r14, r2, r1);
        r11 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x012d, code lost:
        r10.putObject(r14, r2, r1);
        r11 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x01cf, code lost:
        r2 = r1;
        r11 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0200, code lost:
        r0.putLong(r1, r2, r4);
        r5 = r6 | r24;
        r0 = r7;
        r11 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0231, code lost:
        r5 = r6 | r24;
        r12 = r12;
        r11 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0233, code lost:
        r3 = r8;
        r1 = r11;
        r2 = r21;
        r6 = r27;
        r11 = r34;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0251, code lost:
        r9 = r34;
        r23 = r6;
        r7 = r15;
        r22 = r21;
        r15 = r14;
        r28 = r11;
        r11 = r8;
        r8 = r10;
        r10 = r28;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x02a0, code lost:
        if (r0 != r21) goto L_0x02a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x02bb, code lost:
        r7 = r29;
        r11 = r32;
        r9 = r34;
        r2 = r0;
        r10 = r19;
        r8 = r24;
     */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=null, for r0v10, types: [int, byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r31v0, types: [byte[]] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r12v2
  assigns: []
  uses: []
  mth insns count: 490
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 37 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int O000000o(Object obj, byte[] r31, int i2, int i3, int i4, by byVar) {
        int i5;
        int i6;
        Unsafe unsafe;
        int i7;
        ek ekVar;
        Object obj2;
        int i8;
        ? r10;
        ? r4;
        ? r12;
        ? r1;
        int i9;
        int i10;
        int i11;
        int i12;
        int O000000o2;
        int i13;
        Unsafe unsafe2;
        ? r19;
        int i14;
        int i15;
        int O000000o3;
        int i16;
        ? r11;
        int i17;
        ? r112;
        int i18;
        ? r113;
        ? r114;
        int i19;
        long j2;
        long j3;
        Object obj3;
        Unsafe unsafe3;
        int i20;
        ? r115;
        int i21;
        long j4;
        int O000000o4;
        Object O000000o5;
        ? r116;
        long j5;
        int i22;
        ek ekVar2 = this;
        Object obj4 = obj;
        ? r122 = r31;
        int i23 = i3;
        int i24 = i4;
        by byVar2 = byVar;
        Unsafe unsafe4 = b;
        int i25 = i2;
        ? r13 = 0;
        int i26 = 0;
        int i27 = 0;
        int i28 = -1;
        int i29 = 1048575;
        while (true) {
            if (i25 < i23) {
                int i30 = i25 + 1;
                ? r0 = r122[i25];
                if (r0 < 0) {
                    int O000000o6 = eb.O000000o((int) r0, (byte[]) r122, i30, byVar2);
                    r4 = byVar2.a;
                    i30 = O000000o6;
                } else {
                    r4 = r0;
                }
                int i31 = r4 >>> 3;
                int i32 = r4 & 7;
                int a2 = i31 > i28 ? ekVar2.a(i31, i26 / 3) : ekVar2.O000O0o0(i31);
                if (a2 != -1) {
                    int i33 = ekVar2.c[a2 + 1];
                    int O000O0Oo = O000O0Oo(i33);
                    int i34 = i31;
                    int i35 = i30;
                    long O00oOoOo = O00oOoOo(i33);
                    if (O000O0Oo <= 17) {
                        int i36 = ekVar2.c[a2 + 2];
                        int i37 = 1 << (i36 >>> 20);
                        int i38 = i36 & 1048575;
                        int i39 = a2;
                        if (i38 == i29) {
                            i6 = i29;
                            i16 = i27;
                        } else {
                            if (i29 != 1048575) {
                                unsafe4.putInt(obj4, (long) i29, i27);
                            }
                            i6 = i38;
                            i16 = unsafe4.getInt(obj4, (long) i38);
                        }
                        switch (O000O0Oo) {
                            case 0:
                                r11 = r4;
                                i18 = i39;
                                i8 = i35;
                                long j6 = O00oOoOo;
                                if (i32 == 1) {
                                    fr.O000000o(obj4, j6, eb.O00000o0(r122, i8));
                                    i25 = i8 + 8;
                                    r113 = r11;
                                    break;
                                }
                                break;
                            case 1:
                                r11 = r4;
                                i18 = i39;
                                i8 = i35;
                                long j7 = O00oOoOo;
                                if (i32 == 5) {
                                    fr.O000000o(obj4, j7, eb.O00000o(r122, i8));
                                    i25 = i8 + 4;
                                    r113 = r11;
                                    break;
                                }
                                break;
                            case 2:
                            case 3:
                                r11 = r4;
                                i17 = i39;
                                i8 = i35;
                                long j8 = O00oOoOo;
                                if (i32 == 0) {
                                    i19 = eb.O00000Oo(r122, i8, byVar2);
                                    unsafe3 = unsafe4;
                                    obj3 = obj;
                                    j3 = j8;
                                    j2 = byVar2.b;
                                    r114 = r11;
                                    break;
                                }
                                break;
                            case 4:
                            case 11:
                                r11 = r4;
                                i18 = i39;
                                i8 = i35;
                                long j9 = O00oOoOo;
                                if (i32 == 0) {
                                    i25 = eb.O000000o((byte[]) r122, i8, byVar2);
                                    unsafe4.putInt(obj4, j9, byVar2.a);
                                    r113 = r11;
                                    break;
                                }
                                break;
                            case 5:
                            case 14:
                                ? r117 = r4;
                                i18 = i39;
                                i20 = i35;
                                long j10 = O00oOoOo;
                                if (i32 == 1) {
                                    int i40 = i20;
                                    unsafe4.putLong(obj, j10, eb.O00000Oo(r122, i20));
                                    i25 = i40 + 8;
                                    r113 = r117;
                                    break;
                                }
                                break;
                            case 6:
                            case 13:
                                ? r118 = r4;
                                i18 = i39;
                                i20 = i35;
                                long j11 = O00oOoOo;
                                if (i32 == 5) {
                                    unsafe4.putInt(obj4, j11, eb.O000000o(r122, i20));
                                    i25 = i20 + 4;
                                    r113 = r118;
                                    break;
                                }
                                break;
                            case 7:
                                ? r119 = r4;
                                i18 = i39;
                                i20 = i35;
                                long j12 = O00oOoOo;
                                if (i32 == 0) {
                                    i25 = eb.O00000Oo(r122, i20, byVar2);
                                    fr.O000000o(obj4, j12, byVar2.b != 0);
                                    r113 = r119;
                                    break;
                                }
                                break;
                            case 8:
                                ? r1110 = r4;
                                i18 = i39;
                                i20 = i35;
                                long j13 = O00oOoOo;
                                if (i32 == 2) {
                                    i25 = (536870912 & i33) != 0 ? eb.O00000o(r122, i20, byVar2) : eb.O00000o0(r122, i20, byVar2);
                                    unsafe4.putObject(obj4, j13, byVar2.c);
                                    r113 = r1110;
                                    break;
                                }
                                break;
                            case 9:
                                r115 = r4;
                                i21 = i39;
                                i20 = i35;
                                j4 = O00oOoOo;
                                if (i32 == 2) {
                                    O000000o4 = eb.O000000o(ekVar2.a(i21), (byte[]) r122, i20, i23, byVar2);
                                    if ((i16 & i37) != 0) {
                                        O000000o5 = dj.O000000o(unsafe4.getObject(obj4, j4), byVar2.c);
                                        r115 = r115;
                                        break;
                                    } else {
                                        O000000o5 = byVar2.c;
                                        break;
                                    }
                                }
                                break;
                            case 10:
                                ? r1111 = r4;
                                i21 = i39;
                                i20 = i35;
                                j4 = O00oOoOo;
                                if (i32 == 2) {
                                    O000000o4 = eb.O00000oO(r122, i20, byVar2);
                                    O000000o5 = byVar2.c;
                                    r115 = r1111;
                                    break;
                                }
                                break;
                            case 12:
                                r116 = r4;
                                i18 = i39;
                                i20 = i35;
                                j5 = O00oOoOo;
                                if (i32 == 0) {
                                    i25 = eb.O000000o((byte[]) r122, i20, byVar2);
                                    i22 = byVar2.a;
                                    dh c2 = ekVar2.c(i18);
                                    if (c2 != null && !c2.a(i22)) {
                                        O0000O0o(obj).O000000o((int) r116, (Object) Long.valueOf((long) i22));
                                        i27 = i16;
                                        r112 = r116;
                                        break;
                                    }
                                }
                                break;
                            case 15:
                                ? r1112 = r4;
                                i18 = i39;
                                i20 = i35;
                                j5 = O00oOoOo;
                                if (i32 == 0) {
                                    i25 = eb.O000000o((byte[]) r122, i20, byVar2);
                                    i22 = cl.a(byVar2.a);
                                    r116 = r1112;
                                    break;
                                }
                                break;
                            case 16:
                                if (i32 != 0) {
                                    r11 = r4;
                                    i17 = i39;
                                    i8 = i35;
                                    break;
                                } else {
                                    i19 = eb.O00000Oo(r122, i35, byVar2);
                                    int i41 = i34;
                                    j3 = O00oOoOo;
                                    unsafe3 = unsafe4;
                                    obj3 = obj;
                                    r114 = r4;
                                    i17 = i39;
                                    j2 = cl.a(byVar2.b);
                                    break;
                                }
                            default:
                                ? r1113 = r4;
                                i18 = i39;
                                i8 = i35;
                                long j14 = O00oOoOo;
                                if (i32 == 3) {
                                    long j15 = j14;
                                    i25 = eb.O000000o(ekVar2.a(i18), (byte[]) r31, i8, i3, (i34 << 3) | 4, byVar);
                                    unsafe4.putObject(obj4, j15, (i16 & i37) == 0 ? byVar2.c : dj.O000000o(unsafe4.getObject(obj4, j15), byVar2.c));
                                    i27 = i16 | i37;
                                    r122 = r31;
                                    i23 = i3;
                                    r112 = r1113;
                                    break;
                                }
                                break;
                        }
                    } else {
                        ? r1114 = r4;
                        int i42 = O000O0Oo;
                        int i43 = i34;
                        long j16 = O00oOoOo;
                        int i44 = a2;
                        int i45 = i35;
                        if (i42 != 27) {
                            if (i42 <= 49) {
                                int i46 = i45;
                                int i47 = i46;
                                i9 = i43;
                                i5 = i27;
                                i15 = i29;
                                i13 = i44;
                                r19 = r1114;
                                unsafe2 = unsafe4;
                                obj2 = obj4;
                                O000000o3 = O000000o(obj, (byte[]) r31, i46, i3, (int) r1114, i9, i32, i44, (long) i33, i42, j16, byVar);
                            } else {
                                i9 = i43;
                                i5 = i27;
                                i15 = i29;
                                i13 = i44;
                                unsafe2 = unsafe4;
                                r19 = r1114;
                                obj2 = obj4;
                                i14 = i45;
                                int i48 = i42;
                                if (i48 != 50) {
                                    O000000o3 = O000000o(obj, (byte[]) r31, i14, i3, (int) r19, i9, i32, i33, i48, j16, i13, byVar);
                                } else if (i32 == 2) {
                                    O000000o(obj, r31, i14, i3, i13, j16, byVar);
                                    throw null;
                                }
                            }
                            r12 = r31;
                            i26 = i13;
                            i23 = i3;
                            i24 = i4;
                            byVar2 = byVar;
                            obj4 = obj2;
                            r1 = r19;
                            i28 = i9;
                            i27 = i5;
                            unsafe4 = unsafe2;
                            i29 = i15;
                            ekVar2 = this;
                            r122 = r12;
                            r13 = r1;
                        } else {
                            i9 = i43;
                            i5 = i27;
                            i15 = i29;
                            i13 = i44;
                            unsafe2 = unsafe4;
                            r19 = r1114;
                            obj2 = obj4;
                            i14 = i45;
                            if (i32 == 2) {
                                Unsafe unsafe5 = unsafe2;
                                di diVar = (di) unsafe5.getObject(obj2, j16);
                                if (!diVar.a()) {
                                    int size = diVar.size();
                                    diVar = diVar.a(size != 0 ? size + size : 10);
                                    unsafe5.putObject(obj2, j16, diVar);
                                }
                                int i49 = i13;
                                Unsafe unsafe6 = unsafe5;
                                di diVar2 = diVar;
                                int i50 = r19;
                                i25 = eb.O000000o(a(i49), (int) i50, (byte[]) r31, i14, i3, diVar2, byVar);
                                r12 = r31;
                                i23 = i3;
                                i24 = i4;
                                unsafe4 = unsafe6;
                                i26 = i49;
                                obj4 = obj2;
                                i28 = i9;
                                i27 = i5;
                                i29 = i15;
                                byVar2 = byVar;
                                ekVar2 = this;
                                r1 = i50;
                                r122 = r12;
                                r13 = r1;
                            }
                        }
                        ekVar = this;
                        unsafe = unsafe2;
                        i10 = i13;
                        i8 = i14;
                        i11 = r19;
                        i7 = i4;
                    }
                } else {
                    i9 = i31;
                    i5 = i27;
                    i6 = i29;
                    unsafe = unsafe4;
                    ekVar = ekVar2;
                    obj2 = obj4;
                    i7 = i4;
                    i11 = r4;
                    i8 = i30;
                    i10 = 0;
                }
                if (i11 == i7 && i7 != 0) {
                    r10 = i11;
                } else {
                    by byVar3 = byVar;
                    if (ekVar.h && byVar3.d != cs.a()) {
                        i12 = i9;
                        cq O000000o7 = byVar3.d.O000000o(ekVar.g, i12);
                        if (O000000o7 != null) {
                            dc dcVar = (dc) obj2;
                            dcVar.d();
                            O000000o2 = eb.O000000o((int) i11, (byte[]) r31, i8, i3, dcVar, O000000o7, byVar);
                            r1 = i11;
                            i26 = i10;
                            i28 = i12;
                            obj4 = obj2;
                            i27 = i5;
                            i29 = i6;
                            i23 = i3;
                            ekVar2 = ekVar;
                            unsafe4 = unsafe;
                            i24 = i7;
                            byVar2 = byVar3;
                            r12 = r31;
                            r122 = r12;
                            r13 = r1;
                        }
                    } else {
                        i12 = i9;
                    }
                    O000000o2 = eb.O000000o((int) i11, (byte[]) r31, i8, i3, O0000O0o(obj), byVar);
                    r1 = i11;
                    i26 = i10;
                    i28 = i12;
                    obj4 = obj2;
                    i27 = i5;
                    i29 = i6;
                    i23 = i3;
                    ekVar2 = ekVar;
                    unsafe4 = unsafe;
                    i24 = i7;
                    byVar2 = byVar3;
                    r12 = r31;
                    r122 = r12;
                    r13 = r1;
                }
            } else {
                i5 = i27;
                i6 = i29;
                unsafe = unsafe4;
                i7 = i24;
                ekVar = ekVar2;
                obj2 = obj4;
                i8 = i25;
                r10 = r13;
            }
        }
        int i51 = i5;
        int i52 = i6;
        if (i52 != 1048575) {
            unsafe.putInt(obj2, (long) i52, i51);
        }
        int i53 = ekVar.l;
        while (i53 < ekVar.m) {
            int i54 = ekVar.k[i53];
            ekVar.d(i54);
            Object O00000oo = fr.O00000oo(obj2, O00oOoOo(ekVar.e(i54)));
            if (O00000oo == null || ekVar.c(i54) == null) {
                i53++;
            } else {
                ff.O0000O0o(O00000oo);
                ff.O00000Oo(ekVar.b(i54));
                throw null;
            }
        }
        int i55 = i3;
        if (i7 != 0) {
            if (i8 > i55 || r10 != i7) {
                throw dl.e();
            }
        } else if (i8 != i55) {
            throw dl.e();
        }
        return i8;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:105:0x03d0, code lost:
        r14.O000000o(r7, defpackage.fr.O00000oo(r13, O00oOoOo(r6)), a(r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x03ef, code lost:
        r14.O00000o0(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x0402, code lost:
        r14.O00000o(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x0415, code lost:
        r14.O00000o(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x0428, code lost:
        r14.a(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x043b, code lost:
        r14.O00000oO(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x044e, code lost:
        r14.O00000o0(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x0459, code lost:
        r14.O000000o(r7, (defpackage.ck) defpackage.fr.O00000oo(r13, O00oOoOo(r6)));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x046e, code lost:
        r14.O00000Oo(r7, defpackage.fr.O00000oo(r13, O00oOoOo(r6)), a(r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x0485, code lost:
        O000000o(r7, defpackage.fr.O00000oo(r13, O00oOoOo(r6)), r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x04a0, code lost:
        r14.O000000o(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x04b3, code lost:
        r14.O00000Oo(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x04c5, code lost:
        r14.O00000Oo(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x04d7, code lost:
        r14.O000000o(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x04e9, code lost:
        r14.O000000o(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x04fb, code lost:
        r14.a(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x050d, code lost:
        r14.O000000o(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x051f, code lost:
        r14.O000000o(r7, r8);
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x0528  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void O000000o(Object obj, gb gbVar) {
        Entry entry;
        Iterator it;
        int length;
        int i2;
        Entry entry2;
        double d2;
        float f2;
        long j2;
        long j3;
        int i3;
        long j4;
        int i4;
        boolean z;
        int i5;
        int i6;
        int i7;
        long j5;
        int i8;
        long j6;
        if (this.i) {
            if (this.h) {
                cu O00000Oo2 = ej.O00000Oo(obj);
                if (!O00000Oo2.a()) {
                    it = O00000Oo2.d();
                    entry = (Entry) it.next();
                    length = this.c.length;
                    Entry entry3 = entry;
                    i2 = 0;
                    while (i2 < length) {
                        int e2 = e(i2);
                        int d3 = d(i2);
                        while (entry3 != null && ej.O000000o(entry3) <= d3) {
                            ej.O000000o(gbVar, entry3);
                            entry3 = it.hasNext() ? (Entry) it.next() : null;
                        }
                        switch (O000O0Oo(e2)) {
                            case 0:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    d2 = fr.O00000oO(obj, O00oOoOo(e2));
                                }
                            case 1:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    f2 = fr.O00000o(obj, O00oOoOo(e2));
                                }
                            case 2:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    j2 = fr.O00000Oo(obj, O00oOoOo(e2));
                                }
                            case 3:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    j3 = fr.O00000Oo(obj, O00oOoOo(e2));
                                }
                            case 4:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    i3 = fr.O000000o(obj, O00oOoOo(e2));
                                }
                            case 5:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    j4 = fr.O00000Oo(obj, O00oOoOo(e2));
                                }
                            case 6:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    i4 = fr.O000000o(obj, O00oOoOo(e2));
                                }
                            case 7:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    z = fr.O00000o0(obj, O00oOoOo(e2));
                                }
                            case 8:
                                if (!O000000o(obj, i2)) {
                                    break;
                                }
                            case 9:
                                if (!O000000o(obj, i2)) {
                                    break;
                                }
                            case 10:
                                if (!O000000o(obj, i2)) {
                                    break;
                                }
                            case 11:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    i5 = fr.O000000o(obj, O00oOoOo(e2));
                                }
                            case 12:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    i6 = fr.O000000o(obj, O00oOoOo(e2));
                                }
                            case 13:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    i7 = fr.O000000o(obj, O00oOoOo(e2));
                                }
                            case 14:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    j5 = fr.O00000Oo(obj, O00oOoOo(e2));
                                }
                            case 15:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    i8 = fr.O000000o(obj, O00oOoOo(e2));
                                }
                            case 16:
                                if (!O000000o(obj, i2)) {
                                    break;
                                } else {
                                    j6 = fr.O00000Oo(obj, O00oOoOo(e2));
                                }
                            case 17:
                                if (!O000000o(obj, i2)) {
                                    break;
                                }
                            case 18:
                                eu.O000000o(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 19:
                                eu.O00000Oo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 20:
                                eu.O00000o0(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 21:
                                eu.O00000o(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 22:
                                eu.O0000OOo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 23:
                                eu.O00000oo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 24:
                                eu.O0000OoO(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 25:
                                eu.O0000o0(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 26:
                                eu.O000000o(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar);
                                break;
                            case 27:
                                eu.O000000o(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, a(i2));
                                break;
                            case 28:
                                eu.O00000Oo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar);
                                break;
                            case 29:
                                eu.O0000Oo0(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 30:
                                eu.O0000o00(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 31:
                                eu.O0000Ooo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 32:
                                eu.O0000O0o(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 33:
                                eu.O0000Oo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 34:
                                eu.O00000oO(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, false);
                                break;
                            case 35:
                                eu.O000000o(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 36:
                                eu.O00000Oo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 37:
                                eu.O00000o0(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 38:
                                eu.O00000o(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 39:
                                eu.O0000OOo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 40:
                                eu.O00000oo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 41:
                                eu.O0000OoO(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 42:
                                eu.O0000o0(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 43:
                                eu.O0000Oo0(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 44:
                                eu.O0000o00(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 45:
                                eu.O0000Ooo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 46:
                                eu.O0000O0o(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 47:
                                eu.O0000Oo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 48:
                                eu.O00000oO(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, true);
                                break;
                            case 49:
                                eu.O00000Oo(d(i2), (List) fr.O00000oo(obj, O00oOoOo(e2)), gbVar, a(i2));
                                break;
                            case 50:
                                O000000o(gbVar, d3, fr.O00000oo(obj, O00oOoOo(e2)), i2);
                                break;
                            case 51:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    d2 = O00000Oo(obj, O00oOoOo(e2));
                                }
                            case 52:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    f2 = O00000o0(obj, O00oOoOo(e2));
                                }
                            case 53:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    j2 = O00000oO(obj, O00oOoOo(e2));
                                }
                            case 54:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    j3 = O00000oO(obj, O00oOoOo(e2));
                                }
                            case 55:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    i3 = O00000o(obj, O00oOoOo(e2));
                                }
                            case 56:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    j4 = O00000oO(obj, O00oOoOo(e2));
                                }
                            case 57:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    i4 = O00000o(obj, O00oOoOo(e2));
                                }
                            case 58:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    z = O00000oo(obj, O00oOoOo(e2));
                                }
                            case 59:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                }
                            case 60:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                }
                            case 61:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                }
                            case 62:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    i5 = O00000o(obj, O00oOoOo(e2));
                                }
                            case 63:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    i6 = O00000o(obj, O00oOoOo(e2));
                                }
                            case 64:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    i7 = O00000o(obj, O00oOoOo(e2));
                                }
                            case 65:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    j5 = O00000oO(obj, O00oOoOo(e2));
                                }
                            case 66:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    i8 = O00000o(obj, O00oOoOo(e2));
                                }
                            case 67:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                } else {
                                    j6 = O00000oO(obj, O00oOoOo(e2));
                                }
                            case 68:
                                if (!O000000o(obj, d3, i2)) {
                                    break;
                                }
                        }
                        i2 += 3;
                    }
                    while (entry2 != null) {
                        ej.O000000o(gbVar, entry2);
                        entry2 = it.hasNext() ? (Entry) it.next() : null;
                    }
                    O000000o(this.o, obj, gbVar);
                    return;
                }
            }
            it = null;
            entry = null;
            length = this.c.length;
            Entry entry32 = entry;
            i2 = 0;
            while (i2 < length) {
            }
            while (entry2 != null) {
            }
            O000000o(this.o, obj, gbVar);
            return;
        }
        O00000Oo(obj, gbVar);
    }

    public final void O000000o(Object obj, byte[] bArr, int i2, int i3, by byVar) {
        if (!this.i) {
            O000000o(obj, bArr, i2, i3, 0, byVar);
        } else {
            O00000Oo(obj, bArr, i2, i3, byVar);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:83:0x01b7, code lost:
        r2 = r2 + 3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean O000000o(Object obj, Object obj2) {
        int length = this.c.length;
        int i2 = 0;
        while (i2 < length) {
            int e2 = e(i2);
            long O00oOoOo = O00oOoOo(e2);
            switch (O000O0Oo(e2)) {
                case 0:
                    if (O00000o0(obj, obj2, i2) && Double.doubleToLongBits(fr.O00000oO(obj, O00oOoOo)) == Double.doubleToLongBits(fr.O00000oO(obj2, O00oOoOo))) {
                        continue;
                    }
                case 1:
                    if (O00000o0(obj, obj2, i2) && Float.floatToIntBits(fr.O00000o(obj, O00oOoOo)) == Float.floatToIntBits(fr.O00000o(obj2, O00oOoOo))) {
                        continue;
                    }
                case 2:
                    if (O00000o0(obj, obj2, i2) && fr.O00000Oo(obj, O00oOoOo) == fr.O00000Oo(obj2, O00oOoOo)) {
                        continue;
                    }
                case 3:
                    if (O00000o0(obj, obj2, i2) && fr.O00000Oo(obj, O00oOoOo) == fr.O00000Oo(obj2, O00oOoOo)) {
                        continue;
                    }
                case 4:
                    if (O00000o0(obj, obj2, i2) && fr.O000000o(obj, O00oOoOo) == fr.O000000o(obj2, O00oOoOo)) {
                        continue;
                    }
                case 5:
                    if (O00000o0(obj, obj2, i2) && fr.O00000Oo(obj, O00oOoOo) == fr.O00000Oo(obj2, O00oOoOo)) {
                        continue;
                    }
                case 6:
                    if (O00000o0(obj, obj2, i2) && fr.O000000o(obj, O00oOoOo) == fr.O000000o(obj2, O00oOoOo)) {
                        continue;
                    }
                case 7:
                    if (O00000o0(obj, obj2, i2) && fr.O00000o0(obj, O00oOoOo) == fr.O00000o0(obj2, O00oOoOo)) {
                        continue;
                    }
                case 8:
                    if (O00000o0(obj, obj2, i2) && eu.O000000o(fr.O00000oo(obj, O00oOoOo), fr.O00000oo(obj2, O00oOoOo))) {
                        continue;
                    }
                case 9:
                    if (O00000o0(obj, obj2, i2) && eu.O000000o(fr.O00000oo(obj, O00oOoOo), fr.O00000oo(obj2, O00oOoOo))) {
                        continue;
                    }
                case 10:
                    if (O00000o0(obj, obj2, i2) && eu.O000000o(fr.O00000oo(obj, O00oOoOo), fr.O00000oo(obj2, O00oOoOo))) {
                        continue;
                    }
                case 11:
                    if (O00000o0(obj, obj2, i2) && fr.O000000o(obj, O00oOoOo) == fr.O000000o(obj2, O00oOoOo)) {
                        continue;
                    }
                case 12:
                    if (O00000o0(obj, obj2, i2) && fr.O000000o(obj, O00oOoOo) == fr.O000000o(obj2, O00oOoOo)) {
                        continue;
                    }
                case 13:
                    if (O00000o0(obj, obj2, i2) && fr.O000000o(obj, O00oOoOo) == fr.O000000o(obj2, O00oOoOo)) {
                        continue;
                    }
                case 14:
                    if (O00000o0(obj, obj2, i2) && fr.O00000Oo(obj, O00oOoOo) == fr.O00000Oo(obj2, O00oOoOo)) {
                        continue;
                    }
                case 15:
                    if (O00000o0(obj, obj2, i2) && fr.O000000o(obj, O00oOoOo) == fr.O000000o(obj2, O00oOoOo)) {
                        continue;
                    }
                case 16:
                    if (O00000o0(obj, obj2, i2) && fr.O00000Oo(obj, O00oOoOo) == fr.O00000Oo(obj2, O00oOoOo)) {
                        continue;
                    }
                case 17:
                    if (O00000o0(obj, obj2, i2) && eu.O000000o(fr.O00000oo(obj, O00oOoOo), fr.O00000oo(obj2, O00oOoOo))) {
                        continue;
                    }
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                    if (!eu.O000000o(fr.O00000oo(obj, O00oOoOo), fr.O00000oo(obj2, O00oOoOo))) {
                        break;
                    } else {
                        continue;
                    }
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                    long O000O0OO = (long) (O000O0OO(i2) & 1048575);
                    if (fr.O000000o(obj, O000O0OO) == fr.O000000o(obj2, O000O0OO) && eu.O000000o(fr.O00000oo(obj, O00oOoOo), fr.O00000oo(obj2, O00oOoOo))) {
                        continue;
                    }
            }
            return false;
        }
        if (!this.o.O00000Oo(obj).equals(this.o.O00000Oo(obj2))) {
            return false;
        }
        if (this.h) {
            return ej.O00000Oo(obj).equals(ej.O00000Oo(obj2));
        }
        return true;
    }

    public final int O00000Oo(Object obj) {
        return !this.i ? O0000Oo0(obj) : O0000Oo(obj);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002f, code lost:
        defpackage.fr.O000000o(r7, r2, defpackage.fr.O00000oo(r8, r2));
        O00000Oo(r7, r4, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0087, code lost:
        defpackage.fr.O000000o(r7, r2, defpackage.fr.O00000oo(r8, r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b1, code lost:
        defpackage.fr.O000000o(r7, r2, defpackage.fr.O000000o(r8, r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c6, code lost:
        defpackage.fr.O000000o(r7, r2, defpackage.fr.O00000Oo(r8, r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00e9, code lost:
        O00000Oo(r7, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00ec, code lost:
        r0 = r0 + 3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void O00000Oo(Object obj, Object obj2) {
        if (obj2 != null) {
            int i2 = 0;
            while (i2 < this.c.length) {
                int e2 = e(i2);
                long O00oOoOo = O00oOoOo(e2);
                int d2 = d(i2);
                switch (O000O0Oo(e2)) {
                    case 0:
                        if (!O000000o(obj2, i2)) {
                            break;
                        } else {
                            fr.O000000o(obj, O00oOoOo, fr.O00000oO(obj2, O00oOoOo));
                        }
                    case 1:
                        if (!O000000o(obj2, i2)) {
                            break;
                        } else {
                            fr.O000000o(obj, O00oOoOo, fr.O00000o(obj2, O00oOoOo));
                        }
                    case 2:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 3:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 4:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 5:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 6:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 7:
                        if (!O000000o(obj2, i2)) {
                            break;
                        } else {
                            fr.O000000o(obj, O00oOoOo, fr.O00000o0(obj2, O00oOoOo));
                        }
                    case 8:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 9:
                    case 17:
                        O000000o(obj, obj2, i2);
                        break;
                    case 10:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 11:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 12:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 13:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 14:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 15:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 16:
                        if (!O000000o(obj2, i2)) {
                            break;
                        }
                        break;
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                        this.n.O000000o(obj, obj2, O00oOoOo);
                        break;
                    case 50:
                        eu.O000000o(this.r, obj, obj2, O00oOoOo);
                        break;
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                        if (!O000000o(obj2, d2, i2)) {
                            break;
                        }
                    case 60:
                    case 68:
                        O00000Oo(obj, obj2, i2);
                        break;
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                        if (!O000000o(obj2, d2, i2)) {
                            break;
                        }
                }
            }
            eu.O000000o(this.o, obj, obj2);
            if (this.h) {
                eu.O000000o(this.p, obj, obj2);
                return;
            }
            return;
        }
        throw null;
    }

    public final void O00000o(Object obj) {
        int i2;
        int i3 = this.l;
        while (true) {
            i2 = this.m;
            if (i3 >= i2) {
                break;
            }
            long O00oOoOo = O00oOoOo(e(this.k[i3]));
            Object O00000oo = fr.O00000oo(obj, O00oOoOo);
            if (O00000oo != null) {
                ff.O00000o0(O00000oo);
                fr.O000000o(obj, O00oOoOo, O00000oo);
            }
            i3++;
        }
        int length = this.k.length;
        while (i2 < length) {
            this.n.O000000o(obj, (long) this.k[i2]);
            i2++;
        }
        this.o.O00000oO(obj);
        if (this.h) {
            this.p.O00000oO(obj);
        }
    }

    public final boolean O00000o0(Object obj) {
        int i2;
        int i3;
        Object obj2 = obj;
        int i4 = 1048575;
        int i5 = 0;
        int i6 = 0;
        while (i6 < this.l) {
            int i7 = this.k[i6];
            int d2 = d(i7);
            int e2 = e(i7);
            int i8 = this.c[i7 + 2];
            int i9 = i8 & 1048575;
            int i10 = 1 << (i8 >>> 20);
            if (i9 == i4) {
                i3 = i4;
                i2 = i5;
            } else {
                i2 = i9 != 1048575 ? b.getInt(obj2, (long) i9) : i5;
                i3 = i9;
            }
            if ((268435456 & e2) != 0 && !O000000o(obj, i7, i3, i2, i10)) {
                return false;
            }
            int O000O0Oo = O000O0Oo(e2);
            if (O000O0Oo != 9 && O000O0Oo != 17) {
                if (O000O0Oo != 27) {
                    if (O000O0Oo == 60 || O000O0Oo == 68) {
                        if (O000000o(obj2, d2, i7) && !O000000o(obj2, e2, a(i7))) {
                            return false;
                        }
                    } else if (O000O0Oo != 49) {
                        if (O000O0Oo == 50 && !ff.O00000oO(fr.O00000oo(obj2, O00oOoOo(e2))).isEmpty()) {
                            ff.O00000Oo(b(i7));
                            throw null;
                        }
                    }
                }
                List list = (List) fr.O00000oo(obj2, O00oOoOo(e2));
                if (!list.isEmpty()) {
                    es a2 = a(i7);
                    for (int i11 = 0; i11 < list.size(); i11++) {
                        if (!a2.O00000o0(list.get(i11))) {
                            return false;
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            } else if (O000000o(obj, i7, i3, i2, i10) && !O000000o(obj2, e2, a(i7))) {
                return false;
            }
            i6++;
            i4 = i3;
            i5 = i2;
        }
        return !this.h || ej.O00000Oo(obj).e();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x008c, code lost:
        r2 = r2 * 53;
        r3 = O00000o(r9, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00a1, code lost:
        r2 = r2 * 53;
        r3 = O00000oO(r9, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c7, code lost:
        if (r3 != null) goto L_0x00db;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00ca, code lost:
        r2 = r2 * 53;
        r3 = defpackage.fr.O00000oo(r9, r5).hashCode();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00d9, code lost:
        if (r3 != null) goto L_0x00db;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00db, code lost:
        r7 = r3.hashCode();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00df, code lost:
        r2 = (r2 * 53) + r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00e3, code lost:
        r2 = r2 * 53;
        r3 = ((java.lang.String) defpackage.fr.O00000oo(r9, r5)).hashCode();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00f6, code lost:
        r3 = defpackage.dj.a(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x010f, code lost:
        r3 = java.lang.Float.floatToIntBits(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x011a, code lost:
        r3 = java.lang.Double.doubleToLongBits(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x011e, code lost:
        r3 = defpackage.dj.a(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0122, code lost:
        r2 = r2 + r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0123, code lost:
        r1 = r1 + 3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int O00000oO(Object obj) {
        int i2;
        double d2;
        int i3;
        float f2;
        int i4;
        boolean z;
        Object obj2;
        int length = this.c.length;
        int i5 = 0;
        int i6 = 0;
        while (i5 < length) {
            int e2 = e(i5);
            int d3 = d(i5);
            long O00oOoOo = O00oOoOo(e2);
            int i7 = 37;
            switch (O000O0Oo(e2)) {
                case 0:
                    i3 = i6 * 53;
                    d2 = fr.O00000oO(obj, O00oOoOo);
                    break;
                case 1:
                    i4 = i6 * 53;
                    f2 = fr.O00000o(obj, O00oOoOo);
                    break;
                case 2:
                case 3:
                case 5:
                case 14:
                case 16:
                    i3 = i6 * 53;
                    long j2 = fr.O00000Oo(obj, O00oOoOo);
                    break;
                case 4:
                case 6:
                case 11:
                case 12:
                case 13:
                case 15:
                    i2 = i6 * 53;
                    int i8 = fr.O000000o(obj, O00oOoOo);
                    break;
                case 7:
                    i2 = i6 * 53;
                    z = fr.O00000o0(obj, O00oOoOo);
                    break;
                case 8:
                    break;
                case 9:
                    obj2 = fr.O00000oo(obj, O00oOoOo);
                    break;
                case 10:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                    break;
                case 17:
                    obj2 = fr.O00000oo(obj, O00oOoOo);
                    break;
                case 51:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    } else {
                        i3 = i6 * 53;
                        d2 = O00000Oo(obj, O00oOoOo);
                        break;
                    }
                case 52:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    } else {
                        i4 = i6 * 53;
                        f2 = O00000o0(obj, O00oOoOo);
                        break;
                    }
                case 53:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 54:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 55:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 56:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 57:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 58:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    } else {
                        i2 = i6 * 53;
                        z = O00000oo(obj, O00oOoOo);
                        break;
                    }
                case 59:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 60:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 61:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 62:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 63:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 64:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 65:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 66:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 67:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
                case 68:
                    if (!O000000o(obj, d3, i5)) {
                        break;
                    }
                    break;
            }
        }
        int hashCode = (i6 * 53) + this.o.O00000Oo(obj).hashCode();
        return this.h ? (hashCode * 53) + ej.O00000Oo(obj).hashCode() : hashCode;
    }

    public final Object a() {
        return fs.O00000oO((Object) this.g);
    }
}
