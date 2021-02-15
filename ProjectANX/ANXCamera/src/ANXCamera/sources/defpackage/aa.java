package defpackage;

import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: aa reason: default package */
public final class aa extends de implements ei {
    public static final aa e;
    public int a;
    public z b;
    public x c;
    public int d = -1;
    private int f;

    static {
        aa aaVar = new aa();
        e = aaVar;
        de.O000000o(aa.class, (de) aaVar);
    }

    private aa() {
    }

    /* access modifiers changed from: protected */
    public final Object O000000o(int i, Object obj) {
        int i2 = i - 1;
        if (i2 == 0) {
            return Byte.valueOf(1);
        }
        if (i2 == 1) {
            return null;
        }
        if (i2 != 2) {
            return i2 != 3 ? i2 != 4 ? e : new t() : new aa();
        }
        return de.O000000o((eh) e, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0000\u0001င\u0000\u0002ဉ\u0001\u0003ဉ\u0002", new Object[]{SupportedConfigFactory.CLOSE_BY_BOKEH, SupportedConfigFactory.CLOSE_BY_HHT, SupportedConfigFactory.CLOSE_BY_GROUP, SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION});
    }
}
