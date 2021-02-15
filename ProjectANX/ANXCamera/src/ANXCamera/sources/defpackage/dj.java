package defpackage;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/* renamed from: dj reason: default package */
public final class dj {
    static final Charset a = Charset.forName("UTF-8");
    public static final byte[] b = new byte[0];

    static {
        Charset.forName("ISO-8859-1");
        ByteBuffer.wrap(b);
        int length = b.length;
        cl clVar = new cl(0, length);
        try {
            if (length <= clVar.e) {
                clVar.e = length;
                int i = clVar.a + clVar.b;
                clVar.a = i;
                if (i > length) {
                    int i2 = i - length;
                    clVar.b = i2;
                    clVar.a = i - i2;
                    return;
                }
                clVar.b = 0;
                return;
            }
            throw dl.a();
        } catch (dl e) {
            throw new IllegalArgumentException(e);
        }
    }

    static int O000000o(int i, byte[] bArr, int i2, int i3) {
        int i4 = i;
        for (int i5 = i2; i5 < i2 + i3; i5++) {
            i4 = (i4 * 31) + bArr[i5];
        }
        return i4;
    }

    static Object O000000o(Object obj, Object obj2) {
        return ((eh) obj).b().O000000o((eh) obj2).g();
    }

    static boolean O000000o(eh ehVar) {
        if (!(ehVar instanceof bv)) {
            return false;
        }
        bv bvVar = (bv) ehVar;
        throw null;
    }

    static void O00000oO(Object obj) {
        if (obj == null) {
            throw null;
        }
    }

    public static int a(long j) {
        return (int) (j ^ (j >>> 32));
    }

    public static int a(boolean z) {
        return z ? 1231 : 1237;
    }

    static void a(Object obj, String str) {
        if (obj == null) {
            throw new NullPointerException(str);
        }
    }

    public static boolean a(byte[] bArr) {
        return fx.a.O000000o(bArr, 0, bArr.length);
    }

    public static String b(byte[] bArr) {
        return new String(bArr, a);
    }

    public static int c(byte[] bArr) {
        int length = bArr.length;
        int O000000o2 = O000000o(length, bArr, 0, length);
        if (O000000o2 != 0) {
            return O000000o2;
        }
        return 1;
    }
}
