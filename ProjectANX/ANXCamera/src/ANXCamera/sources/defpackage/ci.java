package defpackage;

import java.nio.charset.Charset;

/* renamed from: ci reason: default package */
class ci extends ch {
    private static final long serialVersionUID = 1;
    protected final byte[] a;

    public ci(byte[] bArr) {
        if (bArr != null) {
            this.a = bArr;
            return;
        }
        throw null;
    }

    /* access modifiers changed from: protected */
    public final String O000000o(Charset charset) {
        return new String(this.a, b(), a(), charset);
    }

    public final void O000000o(eb ebVar) {
        ((cn) ebVar).O000000o(this.a, b(), a());
    }

    /* access modifiers changed from: protected */
    public void O000000o(byte[] bArr, int i) {
        throw null;
    }

    public byte a(int i) {
        return this.a[i];
    }

    public int a() {
        return this.a.length;
    }

    /* access modifiers changed from: protected */
    public final int a(int i, int i2) {
        return dj.O000000o(i, this.a, b(), i2);
    }

    public byte b(int i) {
        return this.a[i];
    }

    /* access modifiers changed from: protected */
    public int b() {
        return 0;
    }

    public final ck c(int i) {
        int O000000o2 = ck.O000000o(0, i, a());
        return O000000o2 != 0 ? new cd(this.a, b(), O000000o2) : ck.b;
    }

    public final boolean c() {
        int b = b();
        return fx.O000000o(this.a, b, a() + b);
    }

    public final boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ck) || a() != ((ck) obj).a()) {
            return false;
        }
        if (a() == 0) {
            return true;
        }
        if (!(obj instanceof ci)) {
            return obj.equals(this);
        }
        ci ciVar = (ci) obj;
        int i = this.c;
        int i2 = ciVar.c;
        if (i != 0 && i2 != 0 && i != i2) {
            return false;
        }
        int a2 = a();
        if (a2 > ciVar.a()) {
            int a3 = a();
            StringBuilder sb = new StringBuilder(40);
            sb.append("Length too large: ");
            sb.append(a2);
            sb.append(a3);
            throw new IllegalArgumentException(sb.toString());
        } else if (a2 <= ciVar.a()) {
            if (ciVar instanceof ci) {
                byte[] bArr = this.a;
                byte[] bArr2 = ciVar.a;
                int b = b() + a2;
                int b2 = b();
                int b3 = ciVar.b();
                while (true) {
                    if (b2 < b) {
                        if (bArr[b2] != bArr2[b3]) {
                            z = false;
                            break;
                        }
                        b2++;
                        b3++;
                    } else {
                        break;
                    }
                }
            } else {
                z = ciVar.c(a2).equals(c(a2));
            }
            return z;
        } else {
            int a4 = ciVar.a();
            StringBuilder sb2 = new StringBuilder(59);
            sb2.append("Ran off end of other: ");
            sb2.append(0);
            String str = ", ";
            sb2.append(str);
            sb2.append(a2);
            sb2.append(str);
            sb2.append(a4);
            throw new IllegalArgumentException(sb2.toString());
        }
    }
}
