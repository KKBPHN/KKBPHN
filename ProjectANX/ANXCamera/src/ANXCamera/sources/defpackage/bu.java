package defpackage;

import java.io.IOException;

/* renamed from: bu reason: default package */
public abstract class bu implements eh {
    protected int g = 0;

    public final ck a() {
        try {
            this = this;
            cg d = ck.d(f());
            O000000o(d.a);
            this = d.a();
            r4 = this;
            return this;
        } catch (IOException e) {
            String name = r4.getClass().getName();
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 62 + 10);
            sb.append("Serializing ");
            sb.append(name);
            sb.append(" to a ");
            sb.append("ByteString");
            sb.append(" threw an IOException (should never happen).");
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public void a(int i) {
        throw null;
    }

    public int k() {
        throw null;
    }

    public final byte[] m() {
        try {
            byte[] bArr = new byte[f()];
            cn a = cn.a(bArr);
            O000000o(a);
            a.a();
            return bArr;
        } catch (IOException e) {
            String name = getClass().getName();
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 62 + 10);
            sb.append("Serializing ");
            sb.append(name);
            sb.append(" to a ");
            sb.append("byte array");
            sb.append(" threw an IOException (should never happen).");
            throw new RuntimeException(sb.toString(), e);
        }
    }
}
