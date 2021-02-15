package defpackage;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: cn reason: default package */
public final class cn extends eb {
    public static final boolean a = fr.b;
    private static final Logger c = Logger.getLogger(cn.class.getName());
    co b = this;
    private final byte[] d;
    private final int e;
    private int f;

    public cn() {
    }

    public cn(byte[] bArr, int i) {
        this();
        if (bArr != null) {
            int length = bArr.length;
            if (((length - i) | i) >= 0) {
                this.d = bArr;
                this.f = 0;
                this.e = i;
                return;
            }
            throw new IllegalArgumentException(String.format("Array range is invalid. Buffer.length=%d, offset=%d, length=%d", new Object[]{Integer.valueOf(length), Integer.valueOf(0), Integer.valueOf(i)}));
        }
        throw new NullPointerException("buffer");
    }

    public static int O000000o(int i, int i2) {
        return a(i) + c(i2);
    }

    public static int O000000o(int i, long j) {
        return a(i) + a(j);
    }

    public static int O000000o(int i, ck ckVar) {
        return a(i) + O000000o(ckVar);
    }

    public static int O000000o(int i, dq dqVar) {
        return a(i) + O000000o(dqVar);
    }

    @Deprecated
    static int O000000o(int i, eh ehVar, es esVar) {
        int a2 = a(i);
        int i2 = a2 + a2;
        bu buVar = (bu) ehVar;
        int k = buVar.k();
        if (k == -1) {
            k = esVar.O00000Oo(buVar);
            buVar.a(k);
        }
        return i2 + k;
    }

    public static int O000000o(ck ckVar) {
        return e(ckVar.a());
    }

    public static int O000000o(dq dqVar) {
        int i = dqVar.b != null ? dqVar.b.a() : dqVar.a != null ? dqVar.a.f() : 0;
        return e(i);
    }

    public static int O000000o(eh ehVar) {
        return e(ehVar.f());
    }

    static int O000000o(eh ehVar, es esVar) {
        bu buVar = (bu) ehVar;
        int k = buVar.k();
        if (k == -1) {
            k = esVar.O00000Oo(buVar);
            buVar.a(k);
        }
        return e(k);
    }

    public static int O00000Oo(int i, int i2) {
        return a(i) + d(i2);
    }

    public static int O00000Oo(int i, long j) {
        return a(i) + b(j);
    }

    public static int O00000o(int i, long j) {
        return a(i) + a(j);
    }

    public static int O00000o0(int i, int i2) {
        return a(i) + b(i2);
    }

    @Deprecated
    public static int O00000o0(eh ehVar) {
        return ehVar.f();
    }

    public static int O00000oO(int i, int i2) {
        return a(i) + b(i2);
    }

    public static int O000O0OO(int i) {
        return (i >> 31) ^ (i + i);
    }

    public static int O000O0o(int i) {
        return a(i) + 1;
    }

    public static int O000O0oO(int i) {
        return a(i) + 8;
    }

    public static int O000O0oo(int i) {
        return a(i) + 4;
    }

    public static int O000OO(int i) {
        return a(i) + 4;
    }

    public static int O000OO00(int i) {
        return a(i) + 8;
    }

    public static int O000OO0o(int i) {
        return a(i) + 4;
    }

    public static int O000OOOo(int i) {
        return a(i) + 8;
    }

    public static int a(int i) {
        return c(ga.a(i, 0));
    }

    public static int a(int i, String str) {
        return a(i) + a(str);
    }

    public static int a(long j) {
        int i;
        if ((-128 & j) == 0) {
            return 1;
        }
        if (j < 0) {
            return 10;
        }
        if ((-34359738368L & j) != 0) {
            j >>>= 28;
            i = 6;
        } else {
            i = 2;
        }
        if ((-2097152 & j) != 0) {
            i += 2;
            j >>>= 14;
        }
        if ((j & -16384) != 0) {
            i++;
        }
        return i;
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.CharSequence, java.lang.String] */
    /* JADX WARNING: type inference failed for: r1v1, types: [int] */
    /* JADX WARNING: type inference failed for: r1v3, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r1v5, types: [int] */
    /* JADX WARNING: type inference failed for: r1v6, types: [int] */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.String, code=null, for r1v0, types: [java.lang.CharSequence, java.lang.String] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v7
  assigns: [int]
  uses: [java.lang.String, int]
  mth insns count: 10
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
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int a(String r1) {
        try {
            r1 = r1;
            r1 = fx.O000000o(r1);
            r1 = r1;
        } catch (fv unused) {
            r1 = r1.getBytes(dj.a).length;
        }
        return e(r1);
    }

    public static cn a(byte[] bArr) {
        return new cn(bArr, bArr.length);
    }

    public static int b(int i) {
        if (i >= 0) {
            return c(i);
        }
        return 10;
    }

    public static int b(long j) {
        return a(c(j));
    }

    public static int b(byte[] bArr) {
        return e(bArr.length);
    }

    public static int c(int i) {
        if ((i & -128) == 0) {
            return 1;
        }
        if ((i & -16384) == 0) {
            return 2;
        }
        if ((-2097152 & i) != 0) {
            return (i & -268435456) != 0 ? 5 : 4;
        }
        return 3;
    }

    public static long c(long j) {
        return (j >> 63) ^ (j + j);
    }

    public static int d(int i) {
        return c(O000O0OO(i));
    }

    static int e(int i) {
        return c(i) + i;
    }

    public void O000000o(byte b2) {
        try {
            byte[] bArr = this.d;
            int i = this.f;
            this.f = i + 1;
            bArr[i] = b2;
        } catch (IndexOutOfBoundsException e2) {
            throw new cm(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.f), Integer.valueOf(this.e), Integer.valueOf(1)}), e2);
        }
    }

    public final void O000000o(int i, double d2) {
        O00000oO(i, Double.doubleToRawLongBits(d2));
    }

    public final void O000000o(int i, float f2) {
        O0000Oo0(i, Float.floatToRawIntBits(f2));
    }

    public void O000000o(int i, eh ehVar) {
        O00000o(1, 3);
        O0000OOo(2, i);
        O00000o(3, 2);
        O00oOoOo(ehVar.f());
        ehVar.O000000o(this);
        O00000o(1, 4);
    }

    public void O000000o(int i, boolean z) {
        O00000o(i, 0);
        O000000o(z ? (byte) 1 : 0);
    }

    /* access modifiers changed from: 0000 */
    public final void O000000o(String str, fv fvVar) {
        c.logp(Level.WARNING, "com.google.protobuf.CodedOutputStream", "inefficientWriteStringNoTag", "Converting ill-formed UTF-16. Your Protocol Buffer will not round trip correctly!", fvVar);
        byte[] bytes = str.getBytes(dj.a);
        try {
            int length = bytes.length;
            O00oOoOo(length);
            O00000o(bytes, 0, length);
        } catch (IndexOutOfBoundsException e2) {
            throw new cm(e2);
        } catch (cm e3) {
            throw e3;
        }
    }

    public void O000000o(byte[] bArr, int i, int i2) {
        try {
            System.arraycopy(bArr, i, this.d, this.f, i2);
            this.f += i2;
        } catch (IndexOutOfBoundsException e2) {
            throw new cm(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.f), Integer.valueOf(this.e), Integer.valueOf(i2)}), e2);
        }
    }

    public void O00000Oo(int i, ck ckVar) {
        O00000o(i, 2);
        O00oOoOo(ckVar.a());
        ckVar.O000000o((eb) this);
    }

    public void O00000Oo(int i, eh ehVar, es esVar) {
        O00000o(i, 2);
        bu buVar = (bu) ehVar;
        int k = buVar.k();
        if (k == -1) {
            k = esVar.O00000Oo(buVar);
            buVar.a(k);
        }
        O00oOoOo(k);
        esVar.O000000o((Object) ehVar, (gb) this.b);
    }

    public void O00000Oo(int i, String str) {
        O00000o(i, 2);
        b(str);
    }

    @Deprecated
    public final void O00000Oo(eh ehVar) {
        ehVar.O000000o(this);
    }

    public void O00000o(int i, int i2) {
        O00oOoOo(ga.a(i, i2));
    }

    public void O00000o(byte[] bArr, int i, int i2) {
        O000000o(bArr, 0, i2);
    }

    public void O00000o0(int i, long j) {
        O00000o(i, 0);
        d(j);
    }

    public void O00000o0(int i, ck ckVar) {
        O00000o(1, 3);
        O0000OOo(2, i);
        O00000Oo(3, ckVar);
        O00000o(1, 4);
    }

    public void O00000oO(int i, long j) {
        O00000o(i, 1);
        O00000oO(j);
    }

    public void O00000oO(long j) {
        try {
            byte[] bArr = this.d;
            int i = this.f;
            int i2 = i + 1;
            this.f = i2;
            bArr[i] = (byte) (((int) j) & 255);
            int i3 = i2 + 1;
            this.f = i3;
            bArr[i2] = (byte) (((int) (j >> 8)) & 255);
            int i4 = i3 + 1;
            this.f = i4;
            bArr[i3] = (byte) (((int) (j >> 16)) & 255);
            int i5 = i4 + 1;
            this.f = i5;
            bArr[i4] = (byte) (((int) (j >> 24)) & 255);
            int i6 = i5 + 1;
            this.f = i6;
            bArr[i5] = (byte) (((int) (j >> 32)) & 255);
            int i7 = i6 + 1;
            this.f = i7;
            bArr[i6] = (byte) (((int) (j >> 40)) & 255);
            int i8 = i7 + 1;
            this.f = i8;
            bArr[i7] = (byte) (((int) (j >> 48)) & 255);
            this.f = i8 + 1;
            bArr[i8] = (byte) (((int) (j >> 56)) & 255);
        } catch (IndexOutOfBoundsException e2) {
            throw new cm(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.f), Integer.valueOf(this.e), Integer.valueOf(1)}), e2);
        }
    }

    public void O0000O0o(int i, int i2) {
        O00000o(i, 0);
        O000O0Oo(i2);
    }

    public void O0000OOo(int i, int i2) {
        O00000o(i, 0);
        O00oOoOo(i2);
    }

    public void O0000Oo0(int i, int i2) {
        O00000o(i, 5);
        O000O0o0(i2);
    }

    public void O000O0Oo(int i) {
        if (i < 0) {
            d((long) i);
        } else {
            O00oOoOo(i);
        }
    }

    public void O000O0o0(int i) {
        try {
            byte[] bArr = this.d;
            int i2 = this.f;
            int i3 = i2 + 1;
            this.f = i3;
            bArr[i2] = (byte) (i & 255);
            int i4 = i3 + 1;
            this.f = i4;
            bArr[i3] = (byte) ((i >> 8) & 255);
            int i5 = i4 + 1;
            this.f = i5;
            bArr[i4] = (byte) ((i >> 16) & 255);
            this.f = i5 + 1;
            bArr[i5] = (byte) ((i >> 24) & 255);
        } catch (IndexOutOfBoundsException e2) {
            throw new cm(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.f), Integer.valueOf(this.e), Integer.valueOf(1)}), e2);
        }
    }

    public void O00oOoOo(int i) {
        byte b2;
        long j;
        byte[] bArr;
        if (a && !bx.a() && b() >= 5) {
            if ((i & -128) != 0) {
                byte[] bArr2 = this.d;
                int i2 = this.f;
                this.f = i2 + 1;
                fr.O000000o(bArr2, (long) i2, (byte) (i | 128));
                i >>>= 7;
                if ((i & -128) != 0) {
                    byte[] bArr3 = this.d;
                    int i3 = this.f;
                    this.f = i3 + 1;
                    fr.O000000o(bArr3, (long) i3, (byte) (i | 128));
                    i >>>= 7;
                    if ((i & -128) != 0) {
                        byte[] bArr4 = this.d;
                        int i4 = this.f;
                        this.f = i4 + 1;
                        fr.O000000o(bArr4, (long) i4, (byte) (i | 128));
                        i >>>= 7;
                        if ((i & -128) != 0) {
                            byte[] bArr5 = this.d;
                            int i5 = this.f;
                            this.f = i5 + 1;
                            fr.O000000o(bArr5, (long) i5, (byte) (i | 128));
                            bArr = this.d;
                            int i6 = this.f;
                            this.f = i6 + 1;
                            j = (long) i6;
                            b2 = (byte) (i >>> 7);
                            fr.O000000o(bArr, j, b2);
                            return;
                        }
                    }
                }
            }
            bArr = this.d;
            int i7 = this.f;
            this.f = i7 + 1;
            j = (long) i7;
            b2 = (byte) i;
            fr.O000000o(bArr, j, b2);
            return;
        }
        while ((i & -128) != 0) {
            try {
                byte[] bArr6 = this.d;
                int i8 = this.f;
                this.f = i8 + 1;
                bArr6[i8] = (byte) ((i & 127) | 128);
                i >>>= 7;
            } catch (IndexOutOfBoundsException e2) {
                throw new cm(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.f), Integer.valueOf(this.e), Integer.valueOf(1)}), e2);
            }
        }
        byte[] bArr7 = this.d;
        int i9 = this.f;
        this.f = i9 + 1;
        bArr7[i9] = (byte) i;
    }

    public final void a() {
        if (b() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public final void a(int i, int i2) {
        O0000OOo(i, O000O0OO(i2));
    }

    public final void a(int i, long j) {
        O00000o0(i, c(j));
    }

    public int b() {
        return this.e - this.f;
    }

    public void b(String str) {
        int i = this.f;
        try {
            int c2 = c(str.length() * 3);
            int c3 = c(str.length());
            if (c3 != c2) {
                O00oOoOo(fx.O000000o(str));
                this.f = fx.O000000o(str, this.d, this.f, b());
                return;
            }
            int i2 = i + c3;
            this.f = i2;
            int O000000o2 = fx.O000000o(str, this.d, i2, b());
            this.f = i;
            O00oOoOo((O000000o2 - i) - c3);
            this.f = O000000o2;
        } catch (fv e2) {
            this.f = i;
            O000000o(str, e2);
        } catch (IndexOutOfBoundsException e3) {
            throw new cm(e3);
        }
    }

    public void d(long j) {
        if (!a || b() < 10) {
            while ((j & -128) != 0) {
                try {
                    byte[] bArr = this.d;
                    int i = this.f;
                    this.f = i + 1;
                    bArr[i] = (byte) ((((int) j) & 127) | 128);
                    j >>>= 7;
                } catch (IndexOutOfBoundsException e2) {
                    throw new cm(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.f), Integer.valueOf(this.e), Integer.valueOf(1)}), e2);
                }
            }
            byte[] bArr2 = this.d;
            int i2 = this.f;
            this.f = i2 + 1;
            bArr2[i2] = (byte) ((int) j);
            return;
        }
        while ((j & -128) != 0) {
            byte[] bArr3 = this.d;
            int i3 = this.f;
            this.f = i3 + 1;
            fr.O000000o(bArr3, (long) i3, (byte) ((((int) j) & 127) | 128));
            j >>>= 7;
        }
        byte[] bArr4 = this.d;
        int i4 = this.f;
        this.f = i4 + 1;
        fr.O000000o(bArr4, (long) i4, (byte) ((int) j));
    }
}
