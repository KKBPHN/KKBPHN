package defpackage;

import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: r reason: default package */
public final class r extends de implements ei {
    public static final r a;
    private int b;
    private int c;

    static {
        r rVar = new r();
        a = rVar;
        de.O000000o(r.class, (de) rVar);
    }

    private r() {
    }

    public static /* synthetic */ void O000000o(r rVar) {
        rVar.b |= 1;
        rVar.c = 2;
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
            return i2 != 3 ? i2 != 4 ? a : new q() : new r();
        }
        return de.O000000o((eh) a, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001င\u0000", new Object[]{SupportedConfigFactory.CLOSE_BY_GROUP, SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION});
    }
}
