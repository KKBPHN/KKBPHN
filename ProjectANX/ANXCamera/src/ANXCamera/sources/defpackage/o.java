package defpackage;

import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: o reason: default package */
public final class o extends dc implements ei {
    public static final o c;
    public int a;
    public int b;
    private byte e = 2;

    static {
        o oVar = new o();
        c = oVar;
        de.O000000o(o.class, (de) oVar);
    }

    private o() {
    }

    /* access modifiers changed from: protected */
    public final Object O000000o(int i, Object obj) {
        int i2 = i - 1;
        if (i2 == 0) {
            return Byte.valueOf(this.e);
        }
        byte b2 = 0;
        if (i2 == 1) {
            if (obj != null) {
                b2 = 1;
            }
            this.e = b2;
            return null;
        } else if (i2 != 2) {
            return i2 != 3 ? i2 != 4 ? c : new n() : new o();
        } else {
            return de.O000000o((eh) c, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001ဌ\u0000", new Object[]{SupportedConfigFactory.CLOSE_BY_HHT, SupportedConfigFactory.CLOSE_BY_GROUP, m.a()});
        }
    }
}
