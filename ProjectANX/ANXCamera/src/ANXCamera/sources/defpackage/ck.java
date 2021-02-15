package defpackage;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Locale;

/* renamed from: ck reason: default package */
public abstract class ck implements Iterable, Serializable {
    private static final ce a = (!bx.a() ? new cc() : new cj());
    public static final ck b = new ci(dj.b);
    public int c = 0;

    static int O000000o(int i, int i2, int i3) {
        int i4 = i2 - i;
        if ((i | i2 | i4 | (i3 - i2)) >= 0) {
            return i4;
        }
        if (i < 0) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Beginning index: ");
            sb.append(i);
            sb.append(" < 0");
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i2 < i) {
            StringBuilder sb2 = new StringBuilder(66);
            sb2.append("Beginning index larger than ending index: ");
            sb2.append(i);
            sb2.append(", ");
            sb2.append(i2);
            throw new IndexOutOfBoundsException(sb2.toString());
        } else {
            StringBuilder sb3 = new StringBuilder(37);
            sb3.append("End index: ");
            sb3.append(i2);
            sb3.append(" >= ");
            sb3.append(i3);
            throw new IndexOutOfBoundsException(sb3.toString());
        }
    }

    public static ck O000000o(byte[] bArr, int i, int i2) {
        O000000o(i, i + i2, bArr.length);
        return new ci(a.O000000o(bArr, i, i2));
    }

    public static ck a(String str) {
        return new ci(str.getBytes(dj.a));
    }

    static cg d(int i) {
        return new cg(i);
    }

    public abstract String O000000o(Charset charset);

    public abstract void O000000o(eb ebVar);

    public abstract void O000000o(byte[] bArr, int i);

    public abstract byte a(int i);

    public abstract int a();

    public abstract int a(int i, int i2);

    public abstract byte b(int i);

    public abstract ck c(int i);

    public abstract boolean c();

    /* renamed from: d */
    public final cf iterator() {
        return new cb(this);
    }

    public final String e() {
        return a() != 0 ? O000000o(dj.a) : "";
    }

    public abstract boolean equals(Object obj);

    public final int hashCode() {
        int i = this.c;
        if (i == 0) {
            int a2 = a();
            i = a(a2, a2);
            if (i == 0) {
                i = 1;
            }
            this.c = i;
        }
        return i;
    }

    public final String toString() {
        Locale locale = Locale.ROOT;
        Object[] objArr = new Object[3];
        objArr[0] = Integer.toHexString(System.identityHashCode(this));
        objArr[1] = Integer.valueOf(a());
        objArr[2] = a() > 50 ? String.valueOf(ff.O000000o(c(47))).concat("...") : ff.O000000o(this);
        return String.format(locale, "<ByteString@%s size=%d contents=\"%s\">", objArr);
    }
}
